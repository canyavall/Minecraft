package com.xeenaa.fabricenhancements.performance.chunk;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.metrics.ChunkMetrics;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.Map;
import java.util.List;

/**
 * Main coordination class for parallel chunk loading optimization.
 *
 * This class orchestrates all components of the C2ME-inspired parallel chunk loading system:
 * - ThreadPoolManager for hardware-aware thread management
 * - ParallelChunkProcessor for core parallel processing
 * - ChunkLoadingPipeline for async CompletableFuture-based processing
 * - Performance monitoring and metrics integration
 * - Adaptive optimization based on system performance
 *
 * Target: 15-25% improvement in bulk chunk loading performance
 */
public class ChunkLoadingOptimizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkLoadingOptimizer.class);

    private final PerformanceConfig config;
    private final ChunkMetrics chunkMetrics;

    // Core components
    private final ThreadPoolManager threadPoolManager;
    private final ParallelChunkProcessor chunkProcessor;
    private final ChunkLoadingPipeline loadingPipeline;

    // Performance monitoring
    private final AtomicLong totalOptimizedLoads = new AtomicLong();
    private final AtomicLong totalTimeSavedNanos = new AtomicLong();
    private final AtomicBoolean isEnabled = new AtomicBoolean(true);

    // Adaptive optimization
    private final ScheduledExecutorService monitoringExecutor;
    private final AdaptiveOptimizer adaptiveOptimizer;

    // Performance baseline
    private volatile double baselineLoadTimeMs = 0.0;
    private volatile boolean hasBaseline = false;

    public ChunkLoadingOptimizer(PerformanceConfig config, ChunkMetrics chunkMetrics) {
        this.config = config;
        this.chunkMetrics = chunkMetrics;

        LOGGER.info("Initializing ChunkLoadingOptimizer with C2ME-inspired parallel processing");

        // Initialize core components
        this.threadPoolManager = new ThreadPoolManager(config);
        this.chunkProcessor = new ParallelChunkProcessor(config, threadPoolManager);
        this.loadingPipeline = new ChunkLoadingPipeline(config, threadPoolManager, chunkProcessor);

        // Initialize monitoring
        this.monitoringExecutor = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "ChunkOptimizerMonitor");
            t.setDaemon(true);
            return t;
        });

        this.adaptiveOptimizer = new AdaptiveOptimizer();

        // Start monitoring
        startPerformanceMonitoring();

        LOGGER.info("ChunkLoadingOptimizer initialized successfully");
    }

    /**
     * Optimized chunk loading entry point
     */
    public CompletableFuture<WorldChunk> loadChunk(ServerChunkManager chunkManager,
                                                  int x, int z,
                                                  ChunkStatus status,
                                                  boolean load) {
        if (!isEnabled.get()) {
            // Fall back to original implementation
            return CompletableFuture.supplyAsync(() ->
                chunkManager.getChunk(x, z, status, load));
        }

        long startTime = System.nanoTime();
        ChunkPos pos = new ChunkPos(x, z);

        return loadingPipeline.loadChunk(chunkManager, pos, status, load)
            .whenComplete((chunk, throwable) -> {
                long endTime = System.nanoTime();
                recordOptimizationMetrics(pos, startTime, endTime, chunk != null, throwable == null);
            });
    }

    /**
     * Optimized batch chunk loading
     */
    public CompletableFuture<Map<ChunkPos, WorldChunk>> loadChunkBatch(
            ServerChunkManager chunkManager,
            List<ChunkPos> positions,
            ChunkStatus status,
            boolean load) {

        if (!isEnabled.get() || positions.size() < 2) {
            // Use individual loads for small batches or when disabled
            return loadIndividualChunks(chunkManager, positions, status, load);
        }

        long startTime = System.nanoTime();
        return loadingPipeline.loadChunkBatch(chunkManager, positions, status, load)
            .whenComplete((results, throwable) -> {
                long endTime = System.nanoTime();
                recordBatchMetrics(positions, startTime, endTime, results, throwable == null);
            });
    }

    /**
     * Load chunks individually (fallback)
     */
    private CompletableFuture<Map<ChunkPos, WorldChunk>> loadIndividualChunks(
            ServerChunkManager chunkManager,
            List<ChunkPos> positions,
            ChunkStatus status,
            boolean load) {

        Map<ChunkPos, CompletableFuture<WorldChunk>> futures = new ConcurrentHashMap<>();
        for (ChunkPos pos : positions) {
            futures.put(pos, loadChunk(chunkManager, pos.x, pos.z, status, load));
        }

        return CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                Map<ChunkPos, WorldChunk> results = new ConcurrentHashMap<>();
                futures.forEach((pos, future) -> {
                    try {
                        WorldChunk chunk = future.get();
                        if (chunk != null) {
                            results.put(pos, chunk);
                        }
                    } catch (Exception e) {
                        LOGGER.debug("Failed to load chunk {} in individual batch", pos, e);
                    }
                });
                return results;
            });
    }

    /**
     * Record optimization metrics for single chunk
     */
    private void recordOptimizationMetrics(ChunkPos pos, long startTime, long endTime,
                                         boolean success, boolean noErrors) {
        if (!success || !noErrors) return;

        long loadTimeNanos = endTime - startTime;
        double loadTimeMs = loadTimeNanos / 1_000_000.0;

        totalOptimizedLoads.incrementAndGet();

        // Calculate time saved compared to baseline
        if (hasBaseline && loadTimeMs < baselineLoadTimeMs) {
            long savedNanos = (long) ((baselineLoadTimeMs - loadTimeMs) * 1_000_000);
            totalTimeSavedNanos.addAndGet(savedNanos);
        }

        // Record in chunk metrics
        if (chunkMetrics != null) {
            chunkMetrics.recordChunkLoad(pos, startTime, loadTimeMs < 5.0);
        }

        // Feed to adaptive optimizer
        adaptiveOptimizer.recordLoad(loadTimeMs, chunkProcessor.getLoadFactor());
    }

    /**
     * Record batch optimization metrics
     */
    private void recordBatchMetrics(List<ChunkPos> positions, long startTime, long endTime,
                                   Map<ChunkPos, WorldChunk> results, boolean success) {
        if (!success) return;

        long totalTimeNanos = endTime - startTime;
        double avgTimeMs = (totalTimeNanos / 1_000_000.0) / positions.size();

        // Estimate time saved through batching
        if (hasBaseline) {
            double estimatedSequentialTime = baselineLoadTimeMs * positions.size();
            double actualTime = totalTimeNanos / 1_000_000.0;
            if (actualTime < estimatedSequentialTime) {
                long savedNanos = (long) ((estimatedSequentialTime - actualTime) * 1_000_000);
                totalTimeSavedNanos.addAndGet(savedNanos);
            }
        }

        totalOptimizedLoads.addAndGet(results.size());

        LOGGER.debug("Batch loaded {} chunks in {:.1f}ms ({:.1f}ms avg per chunk)",
                    results.size(), totalTimeNanos / 1_000_000.0, avgTimeMs);
    }

    /**
     * Set performance baseline for optimization measurement
     */
    public void setBaseline(double averageLoadTimeMs) {
        this.baselineLoadTimeMs = averageLoadTimeMs;
        this.hasBaseline = true;
        LOGGER.info("Chunk loading baseline set to {:.2f}ms", averageLoadTimeMs);
    }

    /**
     * Get current optimization statistics
     */
    public OptimizationStats getStats() {
        ThreadPoolManager.ThreadPoolStats poolStats = threadPoolManager.getStats();
        ParallelChunkProcessor.ProcessingStats processorStats = chunkProcessor.getStats();
        ChunkLoadingPipeline.PipelineStats pipelineStats = loadingPipeline.getStats();

        double improvementPercentage = calculateImprovementPercentage();
        double timeSavedSeconds = totalTimeSavedNanos.get() / 1_000_000_000.0;

        return new OptimizationStats(
            totalOptimizedLoads.get(),
            timeSavedSeconds,
            improvementPercentage,
            isEnabled.get(),
            poolStats.utilization,
            processorStats.throughput,
            pipelineStats.averageLatencyMs,
            adaptiveOptimizer.getCurrentMode(),
            poolStats,
            processorStats,
            pipelineStats
        );
    }

    /**
     * Calculate improvement percentage compared to baseline
     */
    private double calculateImprovementPercentage() {
        if (!hasBaseline || totalOptimizedLoads.get() == 0) return 0.0;

        double timeSavedSeconds = totalTimeSavedNanos.get() / 1_000_000_000.0;
        double totalBaselineTime = (baselineLoadTimeMs / 1000.0) * totalOptimizedLoads.get();

        if (totalBaselineTime <= 0) return 0.0;

        return (timeSavedSeconds / totalBaselineTime) * 100.0;
    }

    /**
     * Start performance monitoring and adaptive optimization
     */
    private void startPerformanceMonitoring() {
        // Performance reporting every 30 seconds
        monitoringExecutor.scheduleAtFixedRate(
            this::reportPerformance,
            30, 30, TimeUnit.SECONDS
        );

        // Adaptive optimization every 10 seconds
        monitoringExecutor.scheduleAtFixedRate(
            this::runAdaptiveOptimization,
            10, 10, TimeUnit.SECONDS
        );

        // Health monitoring every 5 seconds
        monitoringExecutor.scheduleAtFixedRate(
            this::monitorHealth,
            5, 5, TimeUnit.SECONDS
        );
    }

    /**
     * Report performance statistics
     */
    private void reportPerformance() {
        OptimizationStats stats = getStats();

        if (stats.totalOptimizedLoads > 0) {
            LOGGER.info("Chunk Loading Optimization: {} chunks, {:.1f}% improvement, {:.1f}s saved, {:.1f}% pool utilization",
                       stats.totalOptimizedLoads,
                       stats.improvementPercentage,
                       stats.timeSavedSeconds,
                       stats.threadPoolUtilization * 100);
        }
    }

    /**
     * Run adaptive optimization
     */
    private void runAdaptiveOptimization() {
        adaptiveOptimizer.optimize();
    }

    /**
     * Monitor system health and automatically disable if needed
     */
    private void monitorHealth() {
        ThreadPoolManager.ThreadPoolStats poolStats = threadPoolManager.getStats();

        // Disable if too many rejections
        if (poolStats.rejectedTasks > 100) {
            LOGGER.warn("Disabling chunk optimization due to high task rejection rate: {}",
                       poolStats.rejectedTasks);
            isEnabled.set(false);
        }

        // Re-enable if rejection rate drops
        if (!isEnabled.get() && poolStats.rejectedTasks < 10) {
            LOGGER.info("Re-enabling chunk optimization - rejection rate normalized");
            isEnabled.set(true);
        }
    }

    /**
     * Enable or disable the optimizer
     */
    public void setEnabled(boolean enabled) {
        boolean wasEnabled = isEnabled.getAndSet(enabled);
        if (enabled != wasEnabled) {
            LOGGER.info("ChunkLoadingOptimizer {}", enabled ? "enabled" : "disabled");
        }
    }

    /**
     * Check if optimizer is enabled
     */
    public boolean isEnabled() {
        return isEnabled.get();
    }

    /**
     * Graceful shutdown
     */
    public void shutdown() {
        LOGGER.info("Shutting down ChunkLoadingOptimizer");

        // Stop monitoring
        monitoringExecutor.shutdown();

        try {
            if (!monitoringExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                monitoringExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            monitoringExecutor.shutdownNow();
        }

        // Shutdown components
        loadingPipeline.shutdown();
        chunkProcessor.shutdown();
        threadPoolManager.shutdown();

        logFinalStats();
    }

    /**
     * Log final optimization statistics
     */
    private void logFinalStats() {
        OptimizationStats stats = getStats();

        LOGGER.info("ChunkLoadingOptimizer final stats:");
        LOGGER.info("  Total optimized loads: {}", stats.totalOptimizedLoads);
        LOGGER.info("  Time saved: {:.1f} seconds", stats.timeSavedSeconds);
        LOGGER.info("  Performance improvement: {:.1f}%", stats.improvementPercentage);
        LOGGER.info("  Peak throughput: {:.1f} chunks/s", stats.processingThroughput);

        if (stats.improvementPercentage >= 15.0) {
            LOGGER.info("✓ Target improvement of 15-25% achieved!");
        } else {
            LOGGER.warn("✗ Target improvement of 15-25% not reached");
        }
    }

    /**
     * Adaptive optimizer for dynamic performance tuning
     */
    private class AdaptiveOptimizer {
        private volatile OptimizationMode currentMode = OptimizationMode.BALANCED;
        private double recentAvgLoadTime = 0.0;
        private double recentAvgLoadFactor = 0.0;
        private int sampleCount = 0;

        public void recordLoad(double loadTimeMs, double loadFactor) {
            sampleCount++;
            recentAvgLoadTime = ((recentAvgLoadTime * (sampleCount - 1)) + loadTimeMs) / sampleCount;
            recentAvgLoadFactor = ((recentAvgLoadFactor * (sampleCount - 1)) + loadFactor) / sampleCount;

            // Reset samples periodically
            if (sampleCount >= 100) {
                sampleCount = 50; // Keep half for continuity
                recentAvgLoadTime /= 2;
                recentAvgLoadFactor /= 2;
            }
        }

        public void optimize() {
            if (sampleCount < 10) return; // Need enough samples

            OptimizationMode newMode = determineOptimalMode();
            if (newMode != currentMode) {
                LOGGER.debug("Switching optimization mode from {} to {} (avg load: {:.1f}ms, load factor: {:.1f})",
                           currentMode, newMode, recentAvgLoadTime, recentAvgLoadFactor);
                currentMode = newMode;
                applyOptimizationMode(newMode);
            }
        }

        private OptimizationMode determineOptimalMode() {
            // High load factor suggests aggressive optimization
            if (recentAvgLoadFactor > 0.8) {
                return OptimizationMode.AGGRESSIVE;
            }

            // Low load suggests conservative approach
            if (recentAvgLoadFactor < 0.3) {
                return OptimizationMode.CONSERVATIVE;
            }

            // Slow loads suggest we need more optimization
            if (recentAvgLoadTime > 20.0) {
                return OptimizationMode.AGGRESSIVE;
            }

            return OptimizationMode.BALANCED;
        }

        private void applyOptimizationMode(OptimizationMode mode) {
            // Mode-specific optimizations could be applied here
            // For now, this is primarily for monitoring
        }

        public OptimizationMode getCurrentMode() {
            return currentMode;
        }
    }

    /**
     * Optimization modes for adaptive tuning
     */
    public enum OptimizationMode {
        CONSERVATIVE, // Minimal overhead, safe settings
        BALANCED,     // Standard optimization
        AGGRESSIVE    // Maximum performance, higher resource usage
    }

    /**
     * Comprehensive optimization statistics
     */
    public static class OptimizationStats {
        public final long totalOptimizedLoads;
        public final double timeSavedSeconds;
        public final double improvementPercentage;
        public final boolean isEnabled;
        public final double threadPoolUtilization;
        public final double processingThroughput;
        public final double averageLatencyMs;
        public final OptimizationMode currentMode;

        // Component stats
        public final ThreadPoolManager.ThreadPoolStats threadPoolStats;
        public final ParallelChunkProcessor.ProcessingStats processingStats;
        public final ChunkLoadingPipeline.PipelineStats pipelineStats;

        public OptimizationStats(long totalOptimizedLoads, double timeSavedSeconds,
                                double improvementPercentage, boolean isEnabled,
                                double threadPoolUtilization, double processingThroughput,
                                double averageLatencyMs, OptimizationMode currentMode,
                                ThreadPoolManager.ThreadPoolStats threadPoolStats,
                                ParallelChunkProcessor.ProcessingStats processingStats,
                                ChunkLoadingPipeline.PipelineStats pipelineStats) {
            this.totalOptimizedLoads = totalOptimizedLoads;
            this.timeSavedSeconds = timeSavedSeconds;
            this.improvementPercentage = improvementPercentage;
            this.isEnabled = isEnabled;
            this.threadPoolUtilization = threadPoolUtilization;
            this.processingThroughput = processingThroughput;
            this.averageLatencyMs = averageLatencyMs;
            this.currentMode = currentMode;
            this.threadPoolStats = threadPoolStats;
            this.processingStats = processingStats;
            this.pipelineStats = pipelineStats;
        }

        @Override
        public String toString() {
            return String.format("OptimizationStats{loads=%d, saved=%.1fs, improvement=%.1f%%, enabled=%s, mode=%s}",
                               totalOptimizedLoads, timeSavedSeconds, improvementPercentage, isEnabled, currentMode);
        }
    }
}