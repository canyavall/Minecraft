package com.xeenaa.fabricenhancements.performance.chunk;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Hardware-aware thread pool manager for parallel chunk operations.
 *
 * Inspired by C2ME's approach, this manager provides:
 * - Hardware-aware thread pool sizing (2-4 threads per CPU core)
 * - Graceful degradation under load
 * - Custom work stealing for chunk operations
 * - Thread pool utilization monitoring
 * - Dynamic load balancing
 */
public class ThreadPoolManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolManager.class);

    private final PerformanceConfig config;

    // Core thread pools
    private final ForkJoinPool chunkProcessingPool;
    private final ScheduledExecutorService scheduledPool;
    private final ThreadPoolExecutor ioPool;

    // Performance tracking
    private final AtomicLong completedTasks = new AtomicLong();
    private final AtomicLong rejectedTasks = new AtomicLong();
    private final AtomicInteger activeTasks = new AtomicInteger();

    // Hardware detection
    private final int cpuCores;
    private final int optimalChunkThreads;
    private final int maxIoThreads;

    // Performance thresholds
    private static final double MAX_UTILIZATION = 0.9;
    private static final int IO_QUEUE_LIMIT = 1000;
    private static final long THREAD_KEEP_ALIVE_MS = 60000;

    public ThreadPoolManager(PerformanceConfig config) {
        this.config = config;
        this.cpuCores = Runtime.getRuntime().availableProcessors();

        // C2ME-inspired thread calculation: 2-4 threads per core
        this.optimalChunkThreads = Math.max(2, Math.min(cpuCores * 3, 32));
        this.maxIoThreads = Math.max(2, cpuCores / 2);

        LOGGER.info("Initializing ThreadPoolManager: {} CPU cores, {} chunk threads, {} I/O threads",
                   cpuCores, optimalChunkThreads, maxIoThreads);

        // Initialize pools
        this.chunkProcessingPool = createChunkProcessingPool();
        this.scheduledPool = createScheduledPool();
        this.ioPool = createIoPool();

        // Start monitoring
        schedulePerformanceMonitoring();
    }

    /**
     * Create optimized ForkJoinPool for chunk processing
     */
    private ForkJoinPool createChunkProcessingPool() {
        return new ForkJoinPool(
            optimalChunkThreads,
            new ChunkProcessingThreadFactory(),
            this::handleUncaughtException,
            true, // Enable async mode for better I/O performance
            optimalChunkThreads,
            optimalChunkThreads * 4, // Allow some queue growth
            1, // Minimal spare threads
            null,
            60, TimeUnit.SECONDS
        );
    }

    /**
     * Create scheduled executor for periodic tasks
     */
    private ScheduledExecutorService createScheduledPool() {
        return Executors.newScheduledThreadPool(2, new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "XeenaaScheduled-" + counter.incrementAndGet());
                t.setDaemon(true);
                t.setPriority(Thread.NORM_PRIORITY - 1);
                return t;
            }
        });
    }

    /**
     * Create I/O thread pool for disk operations
     */
    private ThreadPoolExecutor createIoPool() {
        return new ThreadPoolExecutor(
            maxIoThreads / 2, // Core pool size
            maxIoThreads,     // Maximum pool size
            THREAD_KEEP_ALIVE_MS, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(IO_QUEUE_LIMIT),
            new IoThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy() // Graceful degradation
        );
    }

    /**
     * Submit chunk processing task with load balancing
     */
    public CompletableFuture<Void> submitChunkTask(Runnable task) {
        if (isOverloaded()) {
            rejectedTasks.incrementAndGet();
            return CompletableFuture.completedFuture(null);
        }

        activeTasks.incrementAndGet();
        return CompletableFuture.runAsync(() -> {
            try {
                task.run();
                completedTasks.incrementAndGet();
            } finally {
                activeTasks.decrementAndGet();
            }
        }, chunkProcessingPool);
    }

    /**
     * Submit I/O task with queue management
     */
    public CompletableFuture<Void> submitIoTask(Runnable task) {
        return CompletableFuture.runAsync(task, ioPool);
    }

    /**
     * Submit scheduled task
     */
    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit unit) {
        return scheduledPool.schedule(task, delay, unit);
    }

    /**
     * Submit recurring task
     */
    public ScheduledFuture<?> scheduleRepeatingTask(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduledPool.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    /**
     * Check if thread pools are overloaded
     */
    private boolean isOverloaded() {
        double utilization = getCurrentUtilization();
        return utilization > MAX_UTILIZATION;
    }

    /**
     * Get current thread pool utilization
     */
    public double getCurrentUtilization() {
        int activeCount = chunkProcessingPool.getActiveThreadCount();
        int parallelism = chunkProcessingPool.getParallelism();
        return parallelism > 0 ? (double) activeCount / parallelism : 0.0;
    }

    /**
     * Get performance statistics
     */
    public ThreadPoolStats getStats() {
        return new ThreadPoolStats(
            chunkProcessingPool.getActiveThreadCount(),
            chunkProcessingPool.getParallelism(),
            chunkProcessingPool.getQueuedTaskCount(),
            ioPool.getActiveCount(),
            ioPool.getMaximumPoolSize(),
            ioPool.getQueue().size(),
            completedTasks.get(),
            rejectedTasks.get(),
            activeTasks.get(),
            getCurrentUtilization()
        );
    }

    /**
     * Schedule performance monitoring
     */
    private void schedulePerformanceMonitoring() {
        scheduleRepeatingTask(this::logPerformanceStats, 30, 30, TimeUnit.SECONDS);
        scheduleRepeatingTask(this::adjustPoolSizes, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * Log performance statistics
     */
    private void logPerformanceStats() {
        ThreadPoolStats stats = getStats();

        if (stats.utilization > 0.8) {
            LOGGER.info("High thread pool utilization: {:.1f}% - Active: {}/{}, Queue: {}, Completed: {}",
                       stats.utilization * 100, stats.activeChunkThreads, stats.maxChunkThreads,
                       stats.queuedChunkTasks, stats.completedTasks);
        }

        if (stats.rejectedTasks > 0) {
            LOGGER.warn("Thread pool rejected {} tasks - consider tuning pool sizes", stats.rejectedTasks);
        }
    }

    /**
     * Dynamic pool size adjustment based on load
     */
    private void adjustPoolSizes() {
        double utilization = getCurrentUtilization();

        // Adjust I/O pool based on queue size
        int queueSize = ioPool.getQueue().size();
        if (queueSize > IO_QUEUE_LIMIT * 0.8 && ioPool.getCorePoolSize() < maxIoThreads) {
            ioPool.setCorePoolSize(Math.min(ioPool.getCorePoolSize() + 1, maxIoThreads));
            LOGGER.debug("Increased I/O pool size due to high queue: {}", queueSize);
        } else if (queueSize < IO_QUEUE_LIMIT * 0.2 && ioPool.getCorePoolSize() > 1) {
            ioPool.setCorePoolSize(Math.max(ioPool.getCorePoolSize() - 1, 1));
            LOGGER.debug("Decreased I/O pool size due to low queue: {}", queueSize);
        }
    }

    /**
     * Handle uncaught exceptions in chunk processing
     */
    private void handleUncaughtException(Thread t, Throwable e) {
        LOGGER.error("Uncaught exception in chunk processing thread {}", t.getName(), e);

        // Don't propagate exceptions to avoid thread pool corruption
        rejectedTasks.incrementAndGet();
    }

    /**
     * Graceful shutdown
     */
    public void shutdown() {
        LOGGER.info("Shutting down ThreadPoolManager");

        // Shutdown pools gracefully
        chunkProcessingPool.shutdown();
        scheduledPool.shutdown();
        ioPool.shutdown();

        try {
            // Wait for completion
            if (!chunkProcessingPool.awaitTermination(10, TimeUnit.SECONDS)) {
                chunkProcessingPool.shutdownNow();
            }
            if (!scheduledPool.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledPool.shutdownNow();
            }
            if (!ioPool.awaitTermination(10, TimeUnit.SECONDS)) {
                ioPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Interrupted during shutdown");
        }

        logFinalStats();
    }

    /**
     * Log final performance statistics
     */
    private void logFinalStats() {
        ThreadPoolStats stats = getStats();
        LOGGER.info("ThreadPoolManager final stats - Completed: {}, Rejected: {}, Peak utilization: {:.1f}%",
                   stats.completedTasks, stats.rejectedTasks, stats.utilization * 100);
    }

    /**
     * Custom thread factory for chunk processing
     */
    private static class ChunkProcessingThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            thread.setName("XeenaaChunk-" + counter.incrementAndGet());
            thread.setPriority(Thread.NORM_PRIORITY + 1); // Slightly higher priority
            return thread;
        }
    }

    /**
     * Custom thread factory for I/O operations
     */
    private static class IoThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "XeenaaIO-" + counter.incrementAndGet());
            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY - 1); // Lower priority for I/O
            return t;
        }
    }

    /**
     * Thread pool performance statistics
     */
    public static class ThreadPoolStats {
        public final int activeChunkThreads;
        public final int maxChunkThreads;
        public final long queuedChunkTasks;
        public final int activeIoThreads;
        public final int maxIoThreads;
        public final int queuedIoTasks;
        public final long completedTasks;
        public final long rejectedTasks;
        public final int currentActiveTasks;
        public final double utilization;

        public ThreadPoolStats(int activeChunkThreads, int maxChunkThreads, long queuedChunkTasks,
                              int activeIoThreads, int maxIoThreads, int queuedIoTasks,
                              long completedTasks, long rejectedTasks, int currentActiveTasks,
                              double utilization) {
            this.activeChunkThreads = activeChunkThreads;
            this.maxChunkThreads = maxChunkThreads;
            this.queuedChunkTasks = queuedChunkTasks;
            this.activeIoThreads = activeIoThreads;
            this.maxIoThreads = maxIoThreads;
            this.queuedIoTasks = queuedIoTasks;
            this.completedTasks = completedTasks;
            this.rejectedTasks = rejectedTasks;
            this.currentActiveTasks = currentActiveTasks;
            this.utilization = utilization;
        }

        @Override
        public String toString() {
            return String.format("ThreadPoolStats{chunk=%d/%d, queue=%d, io=%d/%d, utilization=%.1f%%, completed=%d}",
                               activeChunkThreads, maxChunkThreads, queuedChunkTasks,
                               activeIoThreads, maxIoThreads, utilization * 100, completedTasks);
        }
    }
}