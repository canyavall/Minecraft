package com.xeenaa.fabricenhancements.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Enhanced configuration class for performance monitoring and Spark profiler integration.
 *
 * This configuration provides comprehensive settings for:
 * - Spark profiler integration with 2ms sampling interval
 * - Real-time TPS monitoring with alert threshold at 18.0 TPS
 * - Memory monitoring with alert threshold at 7GB for 8GB servers
 * - Web interface enabled on port 4567
 * - Chunk performance monitoring with custom thresholds
 * - Performance validation with <1% overhead requirement
 *
 * The configuration includes both production and development presets
 * to ensure optimal performance in different environments.
 */
public class PerformanceConfig {
    private static final String CONFIG_FILE = "xeenaa-fabric-enhancements.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Chunk Performance Settings
    public boolean enableChunkOptimizations = true;
    public int chunkLoadingThreads = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
    public int maxConcurrentChunkLoads = 16;
    public boolean enableChunkCaching = true;
    public int chunkCacheSize = 1024;

    // Parallel Chunk Loading Settings (C2ME-inspired)
    public boolean enableParallelChunkLoading = true;
    public boolean enableChunkLoadingPipeline = true;
    public int parallelChunkThreads = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() * 3, 32));
    public int chunkProcessingBatchSize = Math.max(4, Runtime.getRuntime().availableProcessors() / 2);
    public long chunkLoadTimeoutMs = 5000;
    public int maxChunkLoadRetries = 3;
    public boolean enableAdaptiveOptimization = true;
    public boolean enableChunkCircuitBreaker = true;
    public int circuitBreakerFailureThreshold = 10;
    public long circuitBreakerResetTimeoutMs = 60000;

    // Memory Management Settings
    public boolean enableMemoryOptimizations = true;
    public int gcThresholdMB = 1024;
    public long memoryAlertThresholdMB = 7168; // 7GB alert threshold for 8GB servers
    public boolean enableMemoryProfiling = true;
    public int memoryProfilingInterval = 5000; // ms
    public int memoryMonitoringInterval = 5000; // ms for Spark monitoring

    // Performance Monitoring Settings
    public boolean enablePerformanceMonitoring = true;
    public int metricsCollectionInterval = 1000; // ms
    public boolean enableSparkIntegration = true;
    public boolean enableJMHBenchmarks = false; // Disabled by default for production

    // Spark Profiler Settings
    public int sparkSamplingIntervalMs = 2; // High-frequency sampling for chunk operations
    public boolean sparkWebInterfaceEnabled = true;
    public int sparkWebInterfacePort = 4567;
    public boolean sparkAutoProfilingEnabled = true;
    public int sparkAutoProfilingDurationSeconds = 30;

    // TPS Monitoring Settings
    public double targetTPS = 20.0;
    public double minimumAcceptableTPS = 19.5;
    public double tpsAlertThreshold = 18.0; // Spark alert threshold
    public int tpsAveragingWindow = 100; // ticks
    public boolean enableRealTimeTpsMonitoring = true;

    // Cache Performance Settings
    public double targetCacheHitRatio = 0.8;
    public boolean enableCacheMetrics = true;
    public int cacheStatisticsInterval = 10000; // ms

    // Chunk Performance Thresholds
    public double slowChunkLoadThresholdMs = 50.0;
    public double verySlowChunkLoadThresholdMs = 200.0;
    public boolean enableChunkPerformanceAlerting = true;
    public int maxSlowChunkLoadHistory = 100;

    // Baseline Collection Settings
    public boolean collectBaselines = false;
    public int baselineWarmupTicks = 200;
    public int baselineMeasurementTicks = 1000;

    // Performance Targets (for regression testing)
    public double chunkLoadTimeImprovementTarget = 0.15; // 15% improvement
    public double memoryUsageReductionTarget = 0.10; // 10% reduction
    public double minimumTPS = 19.5;
    public double targetCacheHitRatio = 0.8;

    // Parallel Chunk Loading Performance Targets
    public double parallelChunkLoadImprovementTarget = 0.20; // 20% improvement target
    public double minParallelChunkLoadImprovement = 0.15; // 15% minimum improvement
    public double maxParallelChunkLoadImprovement = 0.25; // 25% maximum expected improvement
    public double threadPoolUtilizationTarget = 0.80; // 80% thread pool utilization target
    public double maxThreadPoolOverhead = 0.05; // 5% maximum overhead from thread management

    // Spark Integration Validation
    public double maxPerformanceOverheadPercent = 1.0; // Maximum 1% performance impact
    public boolean enablePerformanceValidation = true;
    public int performanceValidationIntervalMinutes = 15;

    /**
     * Load configuration from file or create default if not exists
     */
    public static PerformanceConfig load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                return GSON.fromJson(json, PerformanceConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load performance config", e);
            }
        } else {
            PerformanceConfig config = new PerformanceConfig();
            config.save();
            return config;
        }
    }

    /**
     * Save configuration to file
     */
    public void save() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

        try {
            Files.createDirectories(configPath.getParent());
            String json = GSON.toJson(this);
            Files.writeString(configPath, json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save performance config", e);
        }
    }

    /**
     * Validate configuration settings for Spark integration and parallel chunk loading
     */
    public boolean validateSparkSettings() {
        if (sparkSamplingIntervalMs < 1 || sparkSamplingIntervalMs > 100) {
            throw new IllegalArgumentException("Spark sampling interval must be between 1-100ms");
        }
        if (sparkWebInterfacePort < 1024 || sparkWebInterfacePort > 65535) {
            throw new IllegalArgumentException("Web interface port must be between 1024-65535");
        }
        if (tpsAlertThreshold < 5.0 || tpsAlertThreshold > 20.0) {
            throw new IllegalArgumentException("TPS alert threshold must be between 5.0-20.0");
        }
        if (memoryAlertThresholdMB < 1024 || memoryAlertThresholdMB > 32768) {
            throw new IllegalArgumentException("Memory alert threshold must be between 1GB-32GB");
        }
        return true;
    }

    /**
     * Validate parallel chunk loading settings
     */
    public boolean validateParallelChunkSettings() {
        if (parallelChunkThreads < 2 || parallelChunkThreads > 64) {
            throw new IllegalArgumentException("Parallel chunk threads must be between 2-64");
        }
        if (chunkProcessingBatchSize < 2 || chunkProcessingBatchSize > 32) {
            throw new IllegalArgumentException("Chunk processing batch size must be between 2-32");
        }
        if (chunkLoadTimeoutMs < 1000 || chunkLoadTimeoutMs > 30000) {
            throw new IllegalArgumentException("Chunk load timeout must be between 1-30 seconds");
        }
        if (maxChunkLoadRetries < 1 || maxChunkLoadRetries > 10) {
            throw new IllegalArgumentException("Max chunk load retries must be between 1-10");
        }
        if (circuitBreakerFailureThreshold < 5 || circuitBreakerFailureThreshold > 100) {
            throw new IllegalArgumentException("Circuit breaker failure threshold must be between 5-100");
        }
        if (circuitBreakerResetTimeoutMs < 10000 || circuitBreakerResetTimeoutMs > 300000) {
            throw new IllegalArgumentException("Circuit breaker reset timeout must be between 10-300 seconds");
        }
        return true;
    }

    /**
     * Get optimized settings for production environments
     */
    public static PerformanceConfig getProductionConfig() {
        PerformanceConfig config = new PerformanceConfig();

        // Conservative settings for production
        config.sparkSamplingIntervalMs = 5; // Slightly lower frequency for production
        config.metricsCollectionInterval = 2000; // Less frequent collection
        config.memoryProfilingInterval = 10000; // Less frequent memory profiling
        config.enableJMHBenchmarks = false; // Never enabled in production
        config.collectBaselines = false; // Disable baseline collection

        // Production parallel chunk loading settings
        config.enableParallelChunkLoading = true;
        config.enableChunkLoadingPipeline = true;
        config.parallelChunkThreads = Math.max(4, Runtime.getRuntime().availableProcessors() * 2); // Conservative threading
        config.chunkProcessingBatchSize = Math.max(4, Runtime.getRuntime().availableProcessors() / 2);
        config.enableAdaptiveOptimization = true;
        config.enableChunkCircuitBreaker = true;

        return config;
    }

    /**
     * Get optimized settings for development environments
     */
    public static PerformanceConfig getDevelopmentConfig() {
        PerformanceConfig config = new PerformanceConfig();

        // Aggressive monitoring for development
        config.sparkSamplingIntervalMs = 2; // High frequency for detailed profiling
        config.metricsCollectionInterval = 500; // Frequent collection
        config.memoryProfilingInterval = 2000; // Frequent memory profiling
        config.enableJMHBenchmarks = true; // Enable benchmarks for development
        config.collectBaselines = true; // Enable baseline collection
        config.enablePerformanceValidation = true; // Enable validation

        // Development parallel chunk loading settings (aggressive)
        config.enableParallelChunkLoading = true;
        config.enableChunkLoadingPipeline = true;
        config.parallelChunkThreads = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() * 4, 32)); // Aggressive threading
        config.chunkProcessingBatchSize = Math.max(2, Runtime.getRuntime().availableProcessors());
        config.enableAdaptiveOptimization = true;
        config.enableChunkCircuitBreaker = true;
        config.chunkLoadTimeoutMs = 3000; // Shorter timeout for faster feedback

        return config;
    }

    @Override
    public String toString() {
        return "PerformanceConfig{" +
                "chunkOptimizations=" + enableChunkOptimizations +
                ", parallelChunkLoading=" + enableParallelChunkLoading +
                ", parallelChunkThreads=" + parallelChunkThreads +
                ", memoryOptimizations=" + enableMemoryOptimizations +
                ", performanceMonitoring=" + enablePerformanceMonitoring +
                ", sparkIntegration=" + enableSparkIntegration +
                ", sparkSamplingInterval=" + sparkSamplingIntervalMs + "ms" +
                ", sparkWebInterface=" + sparkWebInterfaceEnabled +
                ", tpsAlertThreshold=" + tpsAlertThreshold +
                ", memoryAlertThreshold=" + memoryAlertThresholdMB + "MB" +
                ", targetTPS=" + targetTPS +
                ", targetCacheHitRatio=" + targetCacheHitRatio +
                ", parallelImprovementTarget=" + parallelChunkLoadImprovementTarget +
                '}';
    }
}