package com.xeenaa.fabricenhancements.performance.memory;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe object pooling system inspired by FerriteCore's memory optimization techniques.
 *
 * Features:
 * - Lock-free pooling with minimal contention
 * - Automatic pool sizing based on usage patterns
 * - Thread-local pool optimization for hot paths
 * - Memory leak prevention with TTL-based cleanup
 * - Comprehensive pool monitoring and metrics
 * - Zero-allocation pool operations where possible
 */
public class ObjectPoolManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectPoolManager.class);

    private final PerformanceConfig config;
    private final Map<Class<?>, ObjectPool<?>> pools = new ConcurrentHashMap<>();
    private final ScheduledExecutorService maintenanceExecutor;

    // Global pool statistics
    private final LongAdder totalBorrows = new LongAdder();
    private final LongAdder totalReturns = new LongAdder();
    private final LongAdder poolHits = new LongAdder();
    private final LongAdder poolMisses = new LongAdder();

    // Pool maintenance settings
    private static final int MAINTENANCE_INTERVAL_SECONDS = 30;
    private static final int DEFAULT_POOL_SIZE = 64;
    private static final int MAX_POOL_SIZE = 512;
    private static final long OBJECT_TTL_MS = 300_000; // 5 minutes

    private volatile boolean isShuttingDown = false;

    public ObjectPoolManager(PerformanceConfig config) {
        this.config = config;
        this.maintenanceExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ObjectPool-Maintenance");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });

        // Schedule pool maintenance
        maintenanceExecutor.scheduleAtFixedRate(
            this::performMaintenance,
            MAINTENANCE_INTERVAL_SECONDS,
            MAINTENANCE_INTERVAL_SECONDS,
            TimeUnit.SECONDS
        );

        LOGGER.info("ObjectPoolManager initialized with maintenance interval: {}s", MAINTENANCE_INTERVAL_SECONDS);
    }

    /**
     * Create or get existing pool for specified type
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getPool(Class<T> type, Supplier<T> factory) {
        return (ObjectPool<T>) pools.computeIfAbsent(type, k -> new ObjectPool<>(type, factory, this));
    }

    /**
     * Create or get existing pool with custom configuration
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getPool(Class<T> type, Supplier<T> factory, PoolConfig poolConfig) {
        return (ObjectPool<T>) pools.computeIfAbsent(type, k -> new ObjectPool<>(type, factory, this, poolConfig));
    }

    /**
     * Remove pool for specified type
     */
    public void removePool(Class<?> type) {
        ObjectPool<?> pool = pools.remove(type);
        if (pool != null) {
            pool.clear();
            LOGGER.debug("Removed pool for type: {}", type.getSimpleName());
        }
    }

    /**
     * Get pool statistics
     */
    public PoolStats getStats() {
        Map<String, PoolStats.PoolInfo> poolInfos = new ConcurrentHashMap<>();
        for (Map.Entry<Class<?>, ObjectPool<?>> entry : pools.entrySet()) {
            ObjectPool<?> pool = entry.getValue();
            poolInfos.put(entry.getKey().getSimpleName(), new PoolStats.PoolInfo(
                pool.getSize(),
                pool.getMaxSize(),
                pool.getBorrowCount(),
                pool.getReturnCount(),
                pool.getHitRate()
            ));
        }

        return new PoolStats(
            totalBorrows.sum(),
            totalReturns.sum(),
            poolHits.sum(),
            poolMisses.sum(),
            calculateOverallHitRate(),
            poolInfos
        );
    }

    /**
     * Calculate overall hit rate across all pools
     */
    private double calculateOverallHitRate() {
        long hits = poolHits.sum();
        long total = hits + poolMisses.sum();
        return total > 0 ? (double) hits / total : 0.0;
    }

    /**
     * Perform maintenance on all pools
     */
    private void performMaintenance() {
        if (isShuttingDown) return;

        try {
            long currentTime = System.currentTimeMillis();
            int poolsCleaned = 0;
            int objectsRemoved = 0;

            for (ObjectPool<?> pool : pools.values()) {
                int removed = pool.performMaintenance(currentTime);
                if (removed > 0) {
                    poolsCleaned++;
                    objectsRemoved += removed;
                }
            }

            if (poolsCleaned > 0) {
                LOGGER.debug("Pool maintenance completed: {} pools cleaned, {} objects removed",
                           poolsCleaned, objectsRemoved);
            }

            // Log pool statistics periodically
            logPoolStatistics();

        } catch (Exception e) {
            LOGGER.warn("Error during pool maintenance", e);
        }
    }

    /**
     * Log pool statistics
     */
    private void logPoolStatistics() {
        if (!LOGGER.isDebugEnabled()) return;

        PoolStats stats = getStats();
        if (stats.totalBorrows > 0) {
            LOGGER.debug("Pool stats - Borrows: {}, Returns: {}, Hit rate: {:.1f}%, Pools: {}",
                       stats.totalBorrows, stats.totalReturns, stats.overallHitRate * 100, stats.poolInfos.size());
        }
    }

    /**
     * Shutdown pool manager
     */
    public void shutdown() {
        LOGGER.info("Shutting down ObjectPoolManager");
        isShuttingDown = true;

        // Shutdown maintenance executor
        maintenanceExecutor.shutdown();
        try {
            if (!maintenanceExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                maintenanceExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            maintenanceExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Clear all pools
        for (ObjectPool<?> pool : pools.values()) {
            pool.clear();
        }
        pools.clear();

        // Log final statistics
        PoolStats finalStats = getStats();
        LOGGER.info("ObjectPoolManager shutdown complete - Final stats: {}", finalStats);
    }

    // Package-private methods for pool callbacks
    void recordBorrow() {
        totalBorrows.increment();
    }

    void recordReturn() {
        totalReturns.increment();
    }

    void recordHit() {
        poolHits.increment();
    }

    void recordMiss() {
        poolMisses.increment();
    }

    /**
     * Individual object pool implementation
     */
    public static class ObjectPool<T> {
        private final Class<T> type;
        private final Supplier<T> factory;
        private final ObjectPoolManager manager;
        private final PoolConfig config;

        // Pool storage - using ConcurrentLinkedQueue for lock-free operations
        private final ConcurrentLinkedQueue<PooledObject<T>> availableObjects = new ConcurrentLinkedQueue<>();
        private final AtomicInteger currentSize = new AtomicInteger(0);
        private final AtomicInteger maxSize = new AtomicInteger(DEFAULT_POOL_SIZE);

        // Statistics
        private final LongAdder borrowCount = new LongAdder();
        private final LongAdder returnCount = new LongAdder();
        private final LongAdder hitCount = new LongAdder();
        private final LongAdder missCount = new LongAdder();

        // Thread-local storage for hot paths
        private final ThreadLocal<T> threadLocalObject = new ThreadLocal<>();

        ObjectPool(Class<T> type, Supplier<T> factory, ObjectPoolManager manager) {
            this(type, factory, manager, new PoolConfig());
        }

        ObjectPool(Class<T> type, Supplier<T> factory, ObjectPoolManager manager, PoolConfig config) {
            this.type = type;
            this.factory = factory;
            this.manager = manager;
            this.config = config;
            this.maxSize.set(config.maxSize);
        }

        /**
         * Borrow object from pool
         */
        public T borrow() {
            manager.recordBorrow();
            borrowCount.increment();

            // Try thread-local first for hot paths
            if (config.useThreadLocal) {
                T threadLocal = threadLocalObject.get();
                if (threadLocal != null) {
                    threadLocalObject.remove();
                    manager.recordHit();
                    hitCount.increment();
                    return threadLocal;
                }
            }

            // Try pool
            PooledObject<T> pooled = availableObjects.poll();
            if (pooled != null && !pooled.isExpired()) {
                currentSize.decrementAndGet();
                manager.recordHit();
                hitCount.increment();
                return pooled.object;
            }

            // Create new object
            manager.recordMiss();
            missCount.increment();
            return factory.get();
        }

        /**
         * Return object to pool
         */
        public void returnObject(T object) {
            if (object == null) return;

            manager.recordReturn();
            returnCount.increment();

            // Try thread-local first for hot paths
            if (config.useThreadLocal && threadLocalObject.get() == null) {
                threadLocalObject.set(object);
                return;
            }

            // Check pool capacity
            if (currentSize.get() >= maxSize.get()) {
                // Pool full, discard object
                return;
            }

            // Reset object if resettable
            if (config.resetBeforeReturn && object instanceof Resettable) {
                ((Resettable) object).reset();
            }

            // Return to pool
            PooledObject<T> pooled = new PooledObject<>(object, System.currentTimeMillis());
            availableObjects.offer(pooled);
            currentSize.incrementAndGet();
        }

        /**
         * Perform maintenance on pool
         */
        int performMaintenance(long currentTime) {
            int removed = 0;
            int targetSize = calculateOptimalSize();

            // Adjust max pool size based on usage patterns
            adjustPoolSize(targetSize);

            // Remove expired objects
            PooledObject<T> pooled;
            while ((pooled = availableObjects.peek()) != null) {
                if (pooled.isExpired(currentTime, OBJECT_TTL_MS) || currentSize.get() > maxSize.get()) {
                    if (availableObjects.remove(pooled)) {
                        currentSize.decrementAndGet();
                        removed++;
                    }
                } else {
                    break; // Objects are roughly ordered by creation time
                }
            }

            return removed;
        }

        /**
         * Calculate optimal pool size based on usage patterns
         */
        private int calculateOptimalSize() {
            long borrows = borrowCount.sum();
            long returns = returnCount.sum();

            if (borrows == 0) return DEFAULT_POOL_SIZE;

            // Calculate average pool utilization
            double hitRate = getHitRate();
            int current = maxSize.get();

            // Increase size if hit rate is low and we're getting good utilization
            if (hitRate < 0.75 && returns > borrows * 0.8) {
                return Math.min(current * 2, MAX_POOL_SIZE);
            }

            // Decrease size if hit rate is high and pool is underutilized
            if (hitRate > 0.95 && currentSize.get() < current * 0.3) {
                return Math.max(current / 2, DEFAULT_POOL_SIZE / 2);
            }

            return current;
        }

        /**
         * Adjust pool size
         */
        private void adjustPoolSize(int targetSize) {
            int current = maxSize.get();
            if (targetSize != current) {
                maxSize.set(targetSize);
                LOGGER.debug("Adjusted pool size for {}: {} -> {}", type.getSimpleName(), current, targetSize);
            }
        }

        /**
         * Get pool hit rate
         */
        public double getHitRate() {
            long hits = hitCount.sum();
            long total = hits + missCount.sum();
            return total > 0 ? (double) hits / total : 0.0;
        }

        /**
         * Clear pool
         */
        public void clear() {
            availableObjects.clear();
            currentSize.set(0);
        }

        // Getters for statistics
        public int getSize() { return currentSize.get(); }
        public int getMaxSize() { return maxSize.get(); }
        public long getBorrowCount() { return borrowCount.sum(); }
        public long getReturnCount() { return returnCount.sum(); }
    }

    /**
     * Pooled object wrapper with timestamp
     */
    private static class PooledObject<T> {
        final T object;
        final long createdAt;

        PooledObject(T object, long createdAt) {
            this.object = object;
            this.createdAt = createdAt;
        }

        boolean isExpired() {
            return isExpired(System.currentTimeMillis(), OBJECT_TTL_MS);
        }

        boolean isExpired(long currentTime, long ttlMs) {
            return currentTime - createdAt > ttlMs;
        }
    }

    /**
     * Pool configuration
     */
    public static class PoolConfig {
        public final int maxSize;
        public final boolean useThreadLocal;
        public final boolean resetBeforeReturn;

        public PoolConfig() {
            this(DEFAULT_POOL_SIZE, true, true);
        }

        public PoolConfig(int maxSize, boolean useThreadLocal, boolean resetBeforeReturn) {
            this.maxSize = Math.min(maxSize, MAX_POOL_SIZE);
            this.useThreadLocal = useThreadLocal;
            this.resetBeforeReturn = resetBeforeReturn;
        }
    }

    /**
     * Interface for objects that can be reset
     */
    public interface Resettable {
        void reset();
    }

    /**
     * Pool statistics
     */
    public static class PoolStats {
        public final long totalBorrows;
        public final long totalReturns;
        public final long totalHits;
        public final long totalMisses;
        public final double overallHitRate;
        public final Map<String, PoolInfo> poolInfos;

        public PoolStats(long totalBorrows, long totalReturns, long totalHits, long totalMisses,
                        double overallHitRate, Map<String, PoolInfo> poolInfos) {
            this.totalBorrows = totalBorrows;
            this.totalReturns = totalReturns;
            this.totalHits = totalHits;
            this.totalMisses = totalMisses;
            this.overallHitRate = overallHitRate;
            this.poolInfos = poolInfos;
        }

        public static class PoolInfo {
            public final int currentSize;
            public final int maxSize;
            public final long borrowCount;
            public final long returnCount;
            public final double hitRate;

            public PoolInfo(int currentSize, int maxSize, long borrowCount, long returnCount, double hitRate) {
                this.currentSize = currentSize;
                this.maxSize = maxSize;
                this.borrowCount = borrowCount;
                this.returnCount = returnCount;
                this.hitRate = hitRate;
            }
        }

        @Override
        public String toString() {
            return String.format("PoolStats{borrows=%d, returns=%d, hitRate=%.1f%%, pools=%d}",
                               totalBorrows, totalReturns, overallHitRate * 100, poolInfos.size());
        }
    }
}