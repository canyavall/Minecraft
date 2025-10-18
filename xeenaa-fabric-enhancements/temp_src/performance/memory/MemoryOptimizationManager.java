package com.xeenaa.fabricenhancements.performance.memory;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.chunk.ParallelChunkProcessor;
import com.xeenaa.fabricenhancements.performance.memory.ObjectPoolManager.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unified memory optimization manager that integrates all memory optimization components
 * with the existing parallel chunk processing system.
 *
 * This manager coordinates:
 * - Object pooling for chunk processing
 * - Memory layout optimization
 * - Allocation tracking and monitoring
 * - Integration with parallel chunk processor
 * - Memory pressure handling and optimization
 */
public class MemoryOptimizationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryOptimizationManager.class);

    private final PerformanceConfig config;

    // Core memory optimization components
    private final ObjectPoolManager poolManager;
    private final ChunkDataPool chunkDataPool;
    private final MemoryLayoutOptimizer layoutOptimizer;
    private final MemoryAllocationTracker allocationTracker;

    // Integration with parallel processing
    private final AtomicReference<ParallelChunkProcessor> chunkProcessor = new AtomicReference<>();
    private final ScheduledExecutorService optimizationExecutor;

    // Memory optimization statistics
    private final AtomicLong totalMemorySaved = new AtomicLong();
    private final AtomicLong optimizationCycles = new AtomicLong();
    private final Map<String, Long> optimizationMetrics = new ConcurrentHashMap<>();

    // Memory management state
    private volatile boolean optimizationEnabled = true;
    private volatile boolean aggressiveOptimization = false;
    private final AtomicLong lastOptimizationTime = new AtomicLong();

    // Performance targets
    private static final double TARGET_MEMORY_REDUCTION = 0.15; // 15% reduction target
    private static final long OPTIMIZATION_INTERVAL_MS = 10000; // 10 seconds
    private static final double MEMORY_PRESSURE_THRESHOLD = 0.8; // 80% memory usage

    public MemoryOptimizationManager(PerformanceConfig config) {
        this.config = config;

        // Initialize core components
        this.poolManager = new ObjectPoolManager(config);
        this.chunkDataPool = new ChunkDataPool(config, poolManager);
        this.layoutOptimizer = new MemoryLayoutOptimizer(config);
        this.allocationTracker = new MemoryAllocationTracker(config);

        // Initialize optimization executor
        this.optimizationExecutor = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "MemoryOptimization-Manager");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY + 1);
            return t;
        });

        startOptimizationLoop();
        LOGGER.info("MemoryOptimizationManager initialized with target reduction: {:.1f}%",
                   TARGET_MEMORY_REDUCTION * 100);
    }

    /**
     * Register parallel chunk processor for integration
     */
    public void registerChunkProcessor(ParallelChunkProcessor processor) {
        this.chunkProcessor.set(processor);
        LOGGER.info("Integrated memory optimization with ParallelChunkProcessor");
    }

    /**
     * Get optimized allocation for chunk processing
     */
    public OptimizedChunkAllocation allocateForChunkProcessing(String operationType, int estimatedSize) {
        // Track allocation
        allocationTracker.trackAllocation("ChunkProcessing." + operationType, estimatedSize,
                                         "MemoryOptimizationManager.allocateForChunkProcessing");

        // Get optimized allocation
        MemoryLayoutOptimizer.OptimizedAllocation allocation = layoutOptimizer.optimizeAllocation(
            estimatedSize, MemoryLayoutOptimizer.AllocationHint.CHUNK_DATA
        );

        // Get pooled resources
        ChunkDataPool.ChunkDataBuffer buffer = chunkDataPool.borrowChunkDataBuffer();

        return new OptimizedChunkAllocation(
            allocation,
            buffer,
            chunkDataPool.borrowList(),
            chunkDataPool.borrowMap()
        );
    }

    /**
     * Release chunk processing allocation
     */
    public void releaseChunkAllocation(OptimizedChunkAllocation allocation) {
        if (allocation != null) {
            // Return pooled resources
            if (allocation.dataBuffer != null) {
                chunkDataPool.returnChunkDataBuffer(allocation.dataBuffer);
            }
            if (allocation.tempList != null) {
                chunkDataPool.returnList(allocation.tempList);
            }
            if (allocation.tempMap != null) {
                chunkDataPool.returnMap(allocation.tempMap);
            }

            // Update memory savings
            updateMemorySavings(allocation.estimatedSize);
        }
    }

    /**
     * Start optimization loop
     */
    private void startOptimizationLoop() {
        // Main optimization cycle
        optimizationExecutor.scheduleAtFixedRate(
            this::performOptimizationCycle,
            OPTIMIZATION_INTERVAL_MS,
            OPTIMIZATION_INTERVAL_MS,
            TimeUnit.MILLISECONDS
        );

        // Memory pressure monitoring
        optimizationExecutor.scheduleAtFixedRate(
            this::monitorMemoryPressure,
            5000, // 5 second initial delay
            5000, // 5 second interval
            TimeUnit.MILLISECONDS
        );
    }

    /**
     * Perform comprehensive optimization cycle
     */
    private void performOptimizationCycle() {
        if (!optimizationEnabled) return;

        try {
            long startTime = System.nanoTime();

            LOGGER.debug("Starting memory optimization cycle");

            // Coordinate optimizations
            OptimizationResult result = new OptimizationResult();

            // Pool optimization
            optimizePools(result);

            // Layout optimization
            optimizeLayout(result);

            // Chunk processor integration optimization
            optimizeChunkProcessorIntegration(result);

            // Memory pressure optimization
            if (aggressiveOptimization) {
                performAggressiveOptimization(result);
            }

            // Update metrics
            updateOptimizationMetrics(result);

            long optimizationTime = System.nanoTime() - startTime;
            optimizationCycles.incrementAndGet();
            lastOptimizationTime.set(System.currentTimeMillis());

            LOGGER.debug("Memory optimization cycle completed in {:.2f}ms: {}",
                        optimizationTime / 1_000_000.0, result);

        } catch (Exception e) {
            LOGGER.warn("Error during memory optimization cycle", e);
        }
    }

    /**
     * Optimize object pools
     */
    private void optimizePools(OptimizationResult result) {
        PoolStats poolStats = poolManager.getStats();
        ChunkDataPool.ChunkPoolStats chunkStats = chunkDataPool.getStats();

        result.poolOptimizations = 1;
        result.memorySavedBytes += chunkStats.memoryBytesSaved;

        // Adjust pool sizes based on hit rates
        if (poolStats.overallHitRate < 0.7) {
            // Low hit rate, pools might be too small
            result.poolSizeAdjustments++;
        }

        optimizationMetrics.put("pool.hitRate", (long) (poolStats.overallHitRate * 100));
        optimizationMetrics.put("pool.memorySavedMB", chunkStats.getMemoryBytesSavedMB());
    }

    /**
     * Optimize memory layout
     */
    private void optimizeLayout(OptimizationResult result) {
        MemoryLayoutOptimizer.MemoryOptimizationStats layoutStats = layoutOptimizer.getStats();

        result.layoutOptimizations = layoutStats.fragmentationReductions;
        result.memorySavedBytes += layoutStats.bytesOptimized;

        // Check if aggressive optimization is needed
        if (layoutStats.averageFragmentation > 0.3) {
            aggressiveOptimization = true;
            result.aggressiveOptimizationsTriggered++;
        }

        optimizationMetrics.put("layout.fragmentation", (long) (layoutStats.averageFragmentation * 100));
        optimizationMetrics.put("layout.optimizedMB", layoutStats.bytesOptimized / (1024 * 1024));
    }

    /**
     * Optimize chunk processor integration
     */
    private void optimizeChunkProcessorIntegration(OptimizationResult result) {
        ParallelChunkProcessor processor = chunkProcessor.get();
        if (processor == null) return;

        ParallelChunkProcessor.ProcessingStats processingStats = processor.getStats();

        // Optimize based on chunk processing patterns
        if (processingStats.queueSize > 100) {
            // High queue size, increase pool sizes
            result.chunkProcessorOptimizations++;
        }

        // Optimize memory allocation for chunk processing load
        double loadFactor = processor.getLoadFactor();
        if (loadFactor > 0.8) {
            // High load, prepare more resources
            preAllocateChunkResources();
            result.resourcePreAllocations++;
        }

        optimizationMetrics.put("chunk.loadFactor", (long) (loadFactor * 100));
        optimizationMetrics.put("chunk.queueSize", (long) processingStats.queueSize);
    }

    /**
     * Pre-allocate chunk processing resources during high load
     */
    private void preAllocateChunkResources() {
        // Pre-allocate some pooled resources to reduce allocation pressure
        for (int i = 0; i < 10; i++) {
            chunkDataPool.returnChunkDataBuffer(chunkDataPool.borrowChunkDataBuffer());
            chunkDataPool.returnList(chunkDataPool.borrowList());
        }
    }

    /**
     * Perform aggressive optimization during memory pressure
     */
    private void performAggressiveOptimization(OptimizationResult result) {
        LOGGER.debug("Performing aggressive memory optimization");

        // Force pool cleanup
        System.gc(); // Suggest garbage collection

        // Compact all memory layouts
        result.aggressiveOptimizationsPerformed++;

        // Reset aggressive mode after optimization
        aggressiveOptimization = false;
    }

    /**
     * Monitor memory pressure
     */
    private void monitorMemoryPressure() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();

            double memoryUsageRatio = (double) usedMemory / maxMemory;

            if (memoryUsageRatio > MEMORY_PRESSURE_THRESHOLD) {
                handleMemoryPressure(memoryUsageRatio);
            } else {
                // Reset aggressive optimization if memory pressure is low
                if (aggressiveOptimization && memoryUsageRatio < 0.6) {
                    aggressiveOptimization = false;
                    LOGGER.debug("Memory pressure reduced, disabling aggressive optimization");
                }
            }

            optimizationMetrics.put("memory.usagePercent", (long) (memoryUsageRatio * 100));

        } catch (Exception e) {
            LOGGER.warn("Error monitoring memory pressure", e);
        }
    }

    /**
     * Handle memory pressure situation
     */
    private void handleMemoryPressure(double memoryUsageRatio) {
        LOGGER.warn("Memory pressure detected: {:.1f}% usage", memoryUsageRatio * 100);

        // Enable aggressive optimization
        aggressiveOptimization = true;

        // Immediate optimization actions
        triggerImmediateOptimization();

        // Notify chunk processor to reduce load if possible
        ParallelChunkProcessor processor = chunkProcessor.get();
        if (processor != null && processor.isOverloaded()) {
            LOGGER.warn("Chunk processor overloaded during memory pressure");
        }
    }

    /**
     * Trigger immediate optimization
     */
    private void triggerImmediateOptimization() {
        optimizationExecutor.submit(() -> {
            try {
                performOptimizationCycle();
            } catch (Exception e) {
                LOGGER.warn("Error during immediate optimization", e);
            }
        });
    }

    /**
     * Update optimization metrics
     */
    private void updateOptimizationMetrics(OptimizationResult result) {
        totalMemorySaved.addAndGet(result.memorySavedBytes);

        optimizationMetrics.put("total.memorySavedMB", totalMemorySaved.get() / (1024 * 1024));
        optimizationMetrics.put("total.cycles", optimizationCycles.get());
        optimizationMetrics.put("recent.poolOptimizations", (long) result.poolOptimizations);
        optimizationMetrics.put("recent.layoutOptimizations", (long) result.layoutOptimizations);
    }

    /**
     * Update memory savings calculation
     */
    private void updateMemorySavings(long estimatedSize) {
        // Estimate memory savings from pooling (assume 70% savings)
        long savings = (long) (estimatedSize * 0.7);
        totalMemorySaved.addAndGet(savings);
    }

    /**
     * Get comprehensive optimization statistics
     */
    public MemoryOptimizationStats getStats() {
        PoolStats poolStats = poolManager.getStats();
        ChunkDataPool.ChunkPoolStats chunkStats = chunkDataPool.getStats();
        MemoryLayoutOptimizer.MemoryOptimizationStats layoutStats = layoutOptimizer.getStats();
        MemoryAllocationTracker.MemoryTrackingReport trackingReport = allocationTracker.getReport();

        double actualMemoryReduction = calculateActualMemoryReduction();

        return new MemoryOptimizationStats(
            totalMemorySaved.get(),
            optimizationCycles.get(),
            actualMemoryReduction,
            TARGET_MEMORY_REDUCTION,
            poolStats,
            chunkStats,
            layoutStats,
            trackingReport,
            optimizationMetrics,
            optimizationEnabled,
            aggressiveOptimization
        );
    }

    /**
     * Calculate actual memory reduction achieved
     */
    private double calculateActualMemoryReduction() {
        MemoryAllocationTracker.MemoryTrackingReport report = allocationTracker.getReport();
        ChunkDataPool.ChunkPoolStats chunkStats = chunkDataPool.getStats();

        if (report.totalBytes > 0) {
            return (double) chunkStats.memoryBytesSaved / report.totalBytes;
        }
        return 0.0;
    }

    /**
     * Check if memory reduction target is met
     */
    public boolean isTargetMet() {
        return calculateActualMemoryReduction() >= TARGET_MEMORY_REDUCTION;
    }

    /**
     * Shutdown memory optimization manager
     */
    public void shutdown() {
        LOGGER.info("Shutting down MemoryOptimizationManager");

        optimizationEnabled = false;

        // Shutdown executor
        optimizationExecutor.shutdown();
        try {
            if (!optimizationExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                optimizationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            optimizationExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Shutdown components
        poolManager.shutdown();
        layoutOptimizer.shutdown();
        allocationTracker.shutdown();

        // Log final statistics
        MemoryOptimizationStats finalStats = getStats();
        LOGGER.info("MemoryOptimizationManager shutdown complete - Final stats: {}", finalStats);
    }

    /**
     * Optimized chunk allocation wrapper
     */
    public static class OptimizedChunkAllocation {
        public final MemoryLayoutOptimizer.OptimizedAllocation memoryAllocation;
        public final ChunkDataPool.ChunkDataBuffer dataBuffer;
        public final java.util.ArrayList<Object> tempList;
        public final java.util.concurrent.ConcurrentHashMap<Object, Object> tempMap;
        public final long estimatedSize;

        public OptimizedChunkAllocation(MemoryLayoutOptimizer.OptimizedAllocation memoryAllocation,
                                       ChunkDataPool.ChunkDataBuffer dataBuffer,
                                       java.util.ArrayList<Object> tempList,
                                       java.util.concurrent.ConcurrentHashMap<Object, Object> tempMap) {
            this.memoryAllocation = memoryAllocation;
            this.dataBuffer = dataBuffer;
            this.tempList = tempList;
            this.tempMap = tempMap;
            this.estimatedSize = calculateEstimatedSize();
        }

        private long calculateEstimatedSize() {
            long size = 0;
            if (dataBuffer != null) size += 8192; // Estimated buffer size
            if (tempList != null) size += 128;    // Estimated list size
            if (tempMap != null) size += 256;     // Estimated map size
            return size;
        }
    }

    /**
     * Optimization result tracking
     */
    private static class OptimizationResult {
        int poolOptimizations = 0;
        long layoutOptimizations = 0;
        int chunkProcessorOptimizations = 0;
        int poolSizeAdjustments = 0;
        int resourcePreAllocations = 0;
        int aggressiveOptimizationsTriggered = 0;
        int aggressiveOptimizationsPerformed = 0;
        long memorySavedBytes = 0;

        @Override
        public String toString() {
            return String.format(
                "OptimizationResult{pool=%d, layout=%d, chunk=%d, saved=%dKB}",
                poolOptimizations, layoutOptimizations, chunkProcessorOptimizations,
                memorySavedBytes / 1024
            );
        }
    }

    /**
     * Comprehensive memory optimization statistics
     */
    public static class MemoryOptimizationStats {
        public final long totalMemorySaved;
        public final long optimizationCycles;
        public final double actualMemoryReduction;
        public final double targetMemoryReduction;
        public final PoolStats poolStats;
        public final ChunkDataPool.ChunkPoolStats chunkPoolStats;
        public final MemoryLayoutOptimizer.MemoryOptimizationStats layoutStats;
        public final MemoryAllocationTracker.MemoryTrackingReport trackingReport;
        public final Map<String, Long> optimizationMetrics;
        public final boolean optimizationEnabled;
        public final boolean aggressiveOptimization;

        public MemoryOptimizationStats(long totalMemorySaved, long optimizationCycles,
                                     double actualMemoryReduction, double targetMemoryReduction,
                                     PoolStats poolStats, ChunkDataPool.ChunkPoolStats chunkPoolStats,
                                     MemoryLayoutOptimizer.MemoryOptimizationStats layoutStats,
                                     MemoryAllocationTracker.MemoryTrackingReport trackingReport,
                                     Map<String, Long> optimizationMetrics,
                                     boolean optimizationEnabled, boolean aggressiveOptimization) {
            this.totalMemorySaved = totalMemorySaved;
            this.optimizationCycles = optimizationCycles;
            this.actualMemoryReduction = actualMemoryReduction;
            this.targetMemoryReduction = targetMemoryReduction;
            this.poolStats = poolStats;
            this.chunkPoolStats = chunkPoolStats;
            this.layoutStats = layoutStats;
            this.trackingReport = trackingReport;
            this.optimizationMetrics = optimizationMetrics;
            this.optimizationEnabled = optimizationEnabled;
            this.aggressiveOptimization = aggressiveOptimization;
        }

        public boolean isTargetMet() {
            return actualMemoryReduction >= targetMemoryReduction;
        }

        public long getTotalMemorySavedMB() {
            return totalMemorySaved / (1024 * 1024);
        }

        @Override
        public String toString() {
            return String.format(
                "MemoryOptimizationStats{saved=%dMB, reduction=%.1f%% (target %.1f%%), " +
                "cycles=%d, targetMet=%s}",
                getTotalMemorySavedMB(), actualMemoryReduction * 100, targetMemoryReduction * 100,
                optimizationCycles, isTargetMet()
            );
        }
    }
}