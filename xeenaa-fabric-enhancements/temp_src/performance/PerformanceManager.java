package com.xeenaa.fabricenhancements.performance;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.metrics.ChunkMetrics;
import com.xeenaa.fabricenhancements.performance.metrics.MemoryMetrics;
import com.xeenaa.fabricenhancements.performance.metrics.TPSMetrics;
import com.xeenaa.fabricenhancements.performance.metrics.MetricsCollector;
import com.xeenaa.fabricenhancements.performance.profiling.SparkIntegration;
import com.xeenaa.fabricenhancements.performance.chunk.ChunkLoadingOptimizer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enhanced central performance management system with Spark profiler integration.
 *
 * This system coordinates all performance monitoring and optimization components,
 * including:
 * - Real-time TPS monitoring with Spark integration
 * - Memory monitoring with 7GB alert threshold
 * - Chunk performance tracking with custom metrics
 * - Automated profiling sessions triggered by performance issues
 * - Performance validation ensuring <1% overhead
 * - Comprehensive reporting with Spark data integration
 *
 * The system integrates seamlessly with Spark profiler to provide
 * production-ready performance monitoring with minimal impact.
 */
public class PerformanceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceManager.class);

    private final PerformanceConfig config;
    private final ScheduledExecutorService scheduler;

    private ChunkMetrics chunkMetrics;
    private MemoryMetrics memoryMetrics;
    private TPSMetrics tpsMetrics;
    private MetricsCollector metricsCollector;
    private SparkIntegration sparkIntegration;
    private ChunkLoadingOptimizer chunkLoadingOptimizer;

    private MinecraftServer server;
    private long lastTickTime;
    private boolean initialized = false;
    private final AtomicLong performanceValidationCounter = new AtomicLong(0);
    private double baselinePerformanceOverhead = 0.0;

    public PerformanceManager(PerformanceConfig config) {
        this.config = config;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    /**
     * Initialize the performance manager with server instance
     */
    public void initialize(MinecraftServer server) {
        this.server = server;
        this.lastTickTime = System.nanoTime();

        // Initialize metrics collectors
        this.chunkMetrics = new ChunkMetrics(config);
        this.memoryMetrics = new MemoryMetrics(config);
        this.tpsMetrics = new TPSMetrics(config);
        this.metricsCollector = new MetricsCollector(config);

        // Initialize Spark integration if enabled
        if (config.enableSparkIntegration) {
            try {
                this.sparkIntegration = new SparkIntegration();
                this.sparkIntegration.initialize(server);
                LOGGER.info("Spark integration initialized successfully");
            } catch (Exception e) {
                LOGGER.warn("Failed to initialize Spark integration, continuing without it", e);
                this.sparkIntegration = null;
            }
        }

        // Initialize chunk loading optimizer if enabled
        if (config.enableParallelChunkLoading) {
            try {
                this.chunkLoadingOptimizer = new ChunkLoadingOptimizer(config, chunkMetrics);

                // Set baseline if we have chunk metrics
                if (chunkMetrics.getTotalChunkLoads() > 100) {
                    chunkLoadingOptimizer.setBaseline(chunkMetrics.getAverageLoadTime());
                }

                LOGGER.info("Chunk loading optimizer initialized successfully");
            } catch (Exception e) {
                LOGGER.warn("Failed to initialize chunk loading optimizer", e);
                this.chunkLoadingOptimizer = null;
            }
        }

        // Initialize metrics collector
        this.metricsCollector.initialize(server);
        this.metricsCollector.startCollection();

        // Schedule periodic tasks
        if (config.enablePerformanceMonitoring) {
            scheduler.scheduleAtFixedRate(
                    this::collectMetrics,
                    0,
                    config.metricsCollectionInterval,
                    TimeUnit.MILLISECONDS
            );
        }

        if (config.enableMemoryProfiling) {
            scheduler.scheduleAtFixedRate(
                    this::profileMemory,
                    0,
                    config.memoryProfilingInterval,
                    TimeUnit.MILLISECONDS
            );
        }

        // Schedule performance validation for Spark integration
        if (config.enablePerformanceValidation && sparkIntegration != null) {
            scheduler.scheduleAtFixedRate(
                    this::validateSparkPerformanceImpact,
                    config.performanceValidationIntervalMinutes,
                    config.performanceValidationIntervalMinutes,
                    TimeUnit.MINUTES
            );
        }

        // Schedule Spark report generation
        if (sparkIntegration != null) {
            scheduler.scheduleAtFixedRate(
                    this::generateSparkReport,
                    30,
                    30,
                    TimeUnit.SECONDS
            );
        }

        this.initialized = true;
        LOGGER.info("Performance manager initialized with monitoring interval: {}ms",
                   config.metricsCollectionInterval);
    }

    /**
     * Called when server has fully started
     */
    public void onServerStarted(MinecraftServer server) {
        if (config.collectBaselines) {
            LOGGER.info("Starting baseline collection after warmup period");
            scheduler.schedule(this::startBaselineCollection,
                             config.baselineWarmupTicks * 50, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Called on each server tick
     */
    public void onServerTick(MinecraftServer server) {
        if (!initialized) return;

        long currentTime = System.nanoTime();
        long tickTime = currentTime - lastTickTime;
        lastTickTime = currentTime;

        // Update TPS metrics
        tpsMetrics.recordTick(tickTime);

        // Update metrics collector
        if (metricsCollector != null) {
            metricsCollector.onServerTick(server);
        }

        // Check for performance issues
        checkPerformanceThresholds();
    }

    /**
     * Collect all performance metrics
     */
    private void collectMetrics() {
        if (!initialized || server == null) return;

        try {
            chunkMetrics.collect(server);
            memoryMetrics.collect();

            // Log metrics periodically
            if (System.currentTimeMillis() % 30000 < config.metricsCollectionInterval) {
                logCurrentMetrics();
            }
        } catch (Exception e) {
            LOGGER.warn("Error collecting metrics", e);
        }
    }

    /**
     * Profile memory usage and allocation patterns
     */
    private void profileMemory() {
        if (!initialized) return;

        try {
            memoryMetrics.profileAllocation();

            // Force GC if memory usage is high
            if (memoryMetrics.shouldTriggerGC(config.gcThresholdMB)) {
                LOGGER.debug("Triggering garbage collection due to high memory usage");
                System.gc();
            }
        } catch (Exception e) {
            LOGGER.warn("Error during memory profiling", e);
        }
    }

    /**
     * Check if performance metrics are meeting thresholds
     */
    private void checkPerformanceThresholds() {
        double currentTPS = tpsMetrics.getCurrentTPS();
        if (currentTPS < config.minimumAcceptableTPS) {
            LOGGER.warn("TPS below threshold: {} < {}", currentTPS, config.minimumAcceptableTPS);

            // Notify Spark integration of low TPS
            if (sparkIntegration != null && sparkIntegration.isInitialized()) {
                // Spark integration will handle auto-profiling based on its own thresholds
            }
        }

        double cacheHitRatio = chunkMetrics.getCacheHitRatio();
        if (cacheHitRatio < config.targetCacheHitRatio) {
            LOGGER.debug("Cache hit ratio below target: {} < {}", cacheHitRatio, config.targetCacheHitRatio);
        }

        // Check memory usage for Spark integration
        double memoryUsageMB = memoryMetrics.getCurrentMemoryUsageMB();
        if (memoryUsageMB > config.memoryAlertThresholdMB) {
            LOGGER.warn("Memory usage above threshold: {:.2f}MB > {}MB", memoryUsageMB, config.memoryAlertThresholdMB);
        }

        // Check chunk loading optimizer performance
        if (chunkLoadingOptimizer != null && chunkLoadingOptimizer.isEnabled()) {
            ChunkLoadingOptimizer.OptimizationStats optimizationStats = chunkLoadingOptimizer.getStats();

            // Check if we're meeting performance targets
            if (optimizationStats.totalOptimizedLoads > 100) {
                if (optimizationStats.improvementPercentage < config.minParallelChunkLoadImprovement * 100) {
                    LOGGER.warn("Chunk optimization improvement {:.1f}% below minimum target {:.1f}%",
                               optimizationStats.improvementPercentage, config.minParallelChunkLoadImprovement * 100);
                }

                if (optimizationStats.threadPoolUtilization > 0.95) {
                    LOGGER.warn("Thread pool utilization very high: {:.1f}%", optimizationStats.threadPoolUtilization * 100);
                }
            }
        }
    }

    /**
     * Start baseline performance collection
     */
    private void startBaselineCollection() {
        LOGGER.info("Starting baseline performance collection for {} ticks",
                   config.baselineMeasurementTicks);

        chunkMetrics.startBaselineCollection();
        memoryMetrics.startBaselineCollection();
        tpsMetrics.startBaselineCollection();

        // Stop baseline collection after measurement period
        scheduler.schedule(() -> {
            stopBaselineCollection();
        }, config.baselineMeasurementTicks * 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop baseline collection and save results
     */
    private void stopBaselineCollection() {
        LOGGER.info("Stopping baseline collection and saving results");

        chunkMetrics.stopBaselineCollection();
        memoryMetrics.stopBaselineCollection();
        tpsMetrics.stopBaselineCollection();
    }

    /**
     * Log current performance metrics with Spark integration and optimization data
     */
    private void logCurrentMetrics() {
        StringBuilder metrics = new StringBuilder();
        metrics.append(String.format("Performance Metrics - TPS: %.2f, Chunk Cache Hit: %.2f%%, Memory: %.2f MB",
                                   tpsMetrics.getCurrentTPS(),
                                   chunkMetrics.getCacheHitRatio() * 100,
                                   memoryMetrics.getCurrentMemoryUsageMB()));

        if (sparkIntegration != null && sparkIntegration.isInitialized()) {
            metrics.append(String.format(", Spark TPS: %.2f, Total Chunk Loads: %d, Cache Hit Ratio: %.2f%%",
                                        sparkIntegration.getCurrentTPS(),
                                        sparkIntegration.getTotalChunkLoads(),
                                        sparkIntegration.getCacheHitRatio() * 100));
        }

        if (chunkLoadingOptimizer != null && chunkLoadingOptimizer.isEnabled()) {
            ChunkLoadingOptimizer.OptimizationStats optimizationStats = chunkLoadingOptimizer.getStats();
            if (optimizationStats.totalOptimizedLoads > 0) {
                metrics.append(String.format(", Parallel Optimization: %.1f%% improvement, %.1f chunks/s throughput",
                                           optimizationStats.improvementPercentage,
                                           optimizationStats.processingThroughput));
            }
        }

        LOGGER.info(metrics.toString());
    }

    /**
     * Generate comprehensive performance report including Spark data
     */
    public void generateReport() {
        if (!initialized) return;

        LOGGER.info("Generating comprehensive performance report...");

        try {
            PerformanceReport.Builder reportBuilder = new PerformanceReport.Builder()
                    .withChunkMetrics(chunkMetrics.getReport())
                    .withMemoryMetrics(memoryMetrics.getReport())
                    .withTPSMetrics(tpsMetrics.getReport());

            // Include Spark integration data if available
            if (sparkIntegration != null && sparkIntegration.isInitialized()) {
                reportBuilder.withSparkMetrics(sparkIntegration.getPerformanceMetricsSummary());
            }

            PerformanceReport report = reportBuilder.build();
            report.saveToFile();
            LOGGER.info("Performance report saved");
        } catch (Exception e) {
            LOGGER.error("Failed to generate performance report", e);
        }
    }

    /**
     * Generate Spark-specific performance report
     */
    private void generateSparkReport() {
        if (sparkIntegration != null && sparkIntegration.isInitialized()) {
            try {
                sparkIntegration.generateSparkReport();
            } catch (Exception e) {
                LOGGER.warn("Error generating Spark report", e);
            }
        }
    }

    /**
     * Validate Spark integration performance impact
     */
    private void validateSparkPerformanceImpact() {
        if (sparkIntegration == null || !sparkIntegration.isInitialized()) return;

        try {
            long validationCount = performanceValidationCounter.incrementAndGet();
            double currentTPS = tpsMetrics.getCurrentTPS();

            // Calculate performance overhead
            if (baselinePerformanceOverhead == 0.0 && validationCount == 1) {
                // Establish baseline
                baselinePerformanceOverhead = 20.0 - currentTPS;
                LOGGER.info("Established performance baseline: {:.2f} TPS overhead", baselinePerformanceOverhead);
                return;
            }

            double currentOverhead = 20.0 - currentTPS;
            double sparkOverhead = currentOverhead - baselinePerformanceOverhead;
            double sparkOverheadPercent = (sparkOverhead / 20.0) * 100.0;

            if (sparkOverheadPercent > config.maxPerformanceOverheadPercent) {
                LOGGER.warn("Spark integration performance overhead {:.2f}% exceeds threshold {:.2f}%",
                           sparkOverheadPercent, config.maxPerformanceOverheadPercent);

                // Consider reducing sampling frequency or disabling features
                LOGGER.info("Consider adjusting Spark configuration to reduce performance impact");
            } else {
                LOGGER.debug("Spark integration performance overhead: {:.2f}% (within threshold)",
                            sparkOverheadPercent);
            }

        } catch (Exception e) {
            LOGGER.warn("Error validating Spark performance impact", e);
        }
    }

    /**
     * Record chunk load event for Spark integration
     */
    public void recordChunkLoadEvent(int chunkX, int chunkZ, long loadTimeNanos, boolean cacheHit) {
        // Record in local metrics
        if (chunkMetrics != null) {
            chunkMetrics.recordChunkLoad(new net.minecraft.util.math.ChunkPos(chunkX, chunkZ),
                                       System.nanoTime() - loadTimeNanos, cacheHit);
        }

        // Record in Spark integration
        if (sparkIntegration != null && sparkIntegration.isInitialized()) {
            sparkIntegration.recordChunkLoadEvent(chunkX, chunkZ, loadTimeNanos);
            sparkIntegration.recordCacheHit(cacheHit);
        }
    }

    /**
     * Get chunk loading optimizer instance
     */
    public ChunkLoadingOptimizer getChunkLoadingOptimizer() {
        return chunkLoadingOptimizer;
    }

    /**
     * Get chunk metrics instance
     */
    public ChunkMetrics getChunkMetrics() {
        return chunkMetrics;
    }

    /**
     * Get memory metrics instance
     */
    public MemoryMetrics getMemoryMetrics() {
        return memoryMetrics;
    }

    /**
     * Get TPS metrics instance
     */
    public TPSMetrics getTPSMetrics() {
        return tpsMetrics;
    }

    /**
     * Get metrics collector instance
     */
    public MetricsCollector getMetricsCollector() {
        return metricsCollector;
    }

    /**
     * Get Spark integration instance
     */
    public SparkIntegration getSparkIntegration() {
        return sparkIntegration;
    }

    /**
     * Check if Spark integration is available and initialized
     */
    public boolean isSparkIntegrationAvailable() {
        return sparkIntegration != null && sparkIntegration.isInitialized();
    }

    /**
     * Get comprehensive performance summary including Spark data
     */
    public String getPerformanceSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Performance Summary ===\n");
        summary.append(String.format("TPS: %.2f | Memory: %.2f MB | Cache Hit: %.2f%%\n",
                                   tpsMetrics.getCurrentTPS(),
                                   memoryMetrics.getCurrentMemoryUsageMB(),
                                   chunkMetrics.getCacheHitRatio() * 100));

        if (chunkMetrics != null) {
            summary.append(chunkMetrics.getPerformanceSummary()).append("\n");
        }

        if (chunkLoadingOptimizer != null && chunkLoadingOptimizer.isEnabled()) {
            summary.append("=== Parallel Chunk Loading Optimization ===\n");
            ChunkLoadingOptimizer.OptimizationStats optimizationStats = chunkLoadingOptimizer.getStats();
            summary.append(String.format("Optimized Loads: %d | Improvement: %.1f%% | Throughput: %.1f chunks/s\n",
                                       optimizationStats.totalOptimizedLoads,
                                       optimizationStats.improvementPercentage,
                                       optimizationStats.processingThroughput));
            summary.append(String.format("Time Saved: %.1f seconds | Thread Pool Utilization: %.1f%%\n",
                                       optimizationStats.timeSavedSeconds,
                                       optimizationStats.threadPoolUtilization * 100));
        }

        if (sparkIntegration != null && sparkIntegration.isInitialized()) {
            summary.append("=== Spark Integration ===\n");
            summary.append(sparkIntegration.getPerformanceMetricsSummary());
        }

        return summary.toString();
    }

    /**
     * Shutdown the performance manager and all integrations
     */
    public void shutdown() {
        LOGGER.info("Shutting down performance manager...");

        // Stop metrics collection
        if (metricsCollector != null) {
            try {
                metricsCollector.stopCollection();
            } catch (Exception e) {
                LOGGER.warn("Error stopping metrics collection", e);
            }
        }

        // Shutdown chunk loading optimizer
        if (chunkLoadingOptimizer != null) {
            try {
                chunkLoadingOptimizer.shutdown();
            } catch (Exception e) {
                LOGGER.warn("Error shutting down chunk loading optimizer", e);
            }
        }

        // Shutdown Spark integration
        if (sparkIntegration != null) {
            try {
                sparkIntegration.shutdown();
            } catch (Exception e) {
                LOGGER.warn("Error shutting down Spark integration", e);
            }
        }

        // Shutdown scheduler
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        initialized = false;
        LOGGER.info("Performance manager shutdown completed");
    }
}