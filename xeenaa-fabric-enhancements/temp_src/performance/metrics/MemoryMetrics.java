package com.xeenaa.fabricenhancements.performance.metrics;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.memory.MemoryOptimizationManager;
import io.micrometer.core.instrument.*;
import org.openjdk.jol.info.GraphLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Metrics collector for memory usage and allocation patterns.
 * Tracks heap usage, GC performance, and allocation rates.
 */
public class MemoryMetrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryMetrics.class);

    private final PerformanceConfig config;
    private final MeterRegistry meterRegistry;
    private final MemoryMXBean memoryMXBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    private final AtomicReference<MemoryOptimizationManager> memoryOptimizer = new AtomicReference<>();

    // Metrics
    private final Gauge heapUsedGauge;
    private final Gauge heapMaxGauge;
    private final Gauge nonHeapUsedGauge;
    private final Counter allocationCounter;
    private final Timer gcTimeTimer;
    private final DistributionSummary allocationSizeDistribution;

    // Memory optimization metrics
    private final Gauge memoryOptimizationSavedGauge;
    private final Gauge poolHitRateGauge;
    private final Counter optimizationCyclesCounter;

    // Internal tracking
    private long lastGCCollections = 0;
    private long lastGCTime = 0;
    private final LongAdder totalAllocations = new LongAdder();
    private final DoubleAdder totalAllocationSize = new DoubleAdder();

    // Baseline collection
    private boolean collectingBaseline = false;
    private final DoubleAdder baselineHeapUsage = new DoubleAdder();
    private final LongAdder baselineGCCollections = new LongAdder();
    private final DoubleAdder baselineGCTime = new DoubleAdder();
    private int baselineSamples = 0;

    public MemoryMetrics(PerformanceConfig config) {
        this(config, null);
    }

    public MemoryMetrics(PerformanceConfig config, MeterRegistry meterRegistry) {
        this.config = config;
        this.meterRegistry = meterRegistry != null ? meterRegistry : Metrics.globalRegistry;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        // Initialize metrics
        this.heapUsedGauge = Gauge.builder("xeenaa.memory.heap_used_bytes")
                .description("Used heap memory in bytes")
                .register(this.meterRegistry, this, MemoryMetrics::getHeapUsedBytes);

        this.heapMaxGauge = Gauge.builder("xeenaa.memory.heap_max_bytes")
                .description("Maximum heap memory in bytes")
                .register(this.meterRegistry, this, MemoryMetrics::getHeapMaxBytes);

        this.nonHeapUsedGauge = Gauge.builder("xeenaa.memory.non_heap_used_bytes")
                .description("Used non-heap memory in bytes")
                .register(this.meterRegistry, this, MemoryMetrics::getNonHeapUsedBytes);

        this.allocationCounter = Counter.builder("xeenaa.memory.allocations_total")
                .description("Total number of memory allocations")
                .register(this.meterRegistry);

        this.gcTimeTimer = Timer.builder("xeenaa.memory.gc_time")
                .description("Garbage collection time")
                .register(this.meterRegistry);

        this.allocationSizeDistribution = DistributionSummary.builder("xeenaa.memory.allocation_size_distribution")
                .description("Distribution of allocation sizes")
                .baseUnit("bytes")
                .register(this.meterRegistry);

        // Initialize memory optimization metrics
        this.memoryOptimizationSavedGauge = Gauge.builder("xeenaa.memory.optimization_saved_bytes")
                .description("Memory bytes saved by optimization")
                .register(this.meterRegistry, this, MemoryMetrics::getMemoryOptimizationSavedBytes);

        this.poolHitRateGauge = Gauge.builder("xeenaa.memory.pool_hit_rate")
                .description("Object pool hit rate")
                .register(this.meterRegistry, this, MemoryMetrics::getPoolHitRate);

        this.optimizationCyclesCounter = Counter.builder("xeenaa.memory.optimization_cycles_total")
                .description("Total number of memory optimization cycles")
                .register(this.meterRegistry);

        // Initialize GC tracking
        initializeGCTracking();
    }

    /**
     * Set memory optimization manager for enhanced metrics
     */
    public void setMemoryOptimizer(MemoryOptimizationManager optimizer) {
        this.memoryOptimizer.set(optimizer);
        LOGGER.info("Memory optimization integrated with MemoryMetrics");
    }

    /**
     * Initialize garbage collection tracking
     */
    private void initializeGCTracking() {
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            lastGCCollections += gcBean.getCollectionCount();
            lastGCTime += gcBean.getCollectionTime();

            // Register GC-specific metrics
            Gauge.builder("xeenaa.memory.gc_collections_total")
                    .description("Total garbage collections")
                    .tag("gc", gcBean.getName())
                    .register(meterRegistry, gcBean, GarbageCollectorMXBean::getCollectionCount);

            Gauge.builder("xeenaa.memory.gc_time_total")
                    .description("Total garbage collection time")
                    .tag("gc", gcBean.getName())
                    .register(meterRegistry, gcBean, bean -> bean.getCollectionTime() / 1000.0);
        }
    }

    /**
     * Collect memory metrics
     */
    public void collect() {
        try {
            // Update GC metrics
            updateGCMetrics();

            // Baseline collection
            if (collectingBaseline) {
                baselineHeapUsage.add(getCurrentMemoryUsageMB());
                baselineGCCollections.add(getCurrentGCCollections() - lastGCCollections);
                baselineGCTime.add(getCurrentGCTime() - lastGCTime);
                baselineSamples++;
            }

            // Update memory optimization metrics
            updateMemoryOptimizationMetrics();

            // Check memory thresholds
            checkMemoryThresholds();

        } catch (Exception e) {
            LOGGER.warn("Error collecting memory metrics", e);
        }
    }

    /**
     * Update garbage collection metrics
     */
    private void updateGCMetrics() {
        long currentGCCollections = getCurrentGCCollections();
        long currentGCTime = getCurrentGCTime();

        long newCollections = currentGCCollections - lastGCCollections;
        long newGCTime = currentGCTime - lastGCTime;

        if (newCollections > 0) {
            gcTimeTimer.record(newGCTime, java.util.concurrent.TimeUnit.MILLISECONDS);
            LOGGER.debug("GC event: {} collections, {}ms total time", newCollections, newGCTime);
        }

        lastGCCollections = currentGCCollections;
        lastGCTime = currentGCTime;
    }

    /**
     * Update memory optimization metrics
     */
    private void updateMemoryOptimizationMetrics() {
        MemoryOptimizationManager optimizer = memoryOptimizer.get();
        if (optimizer != null) {
            try {
                MemoryOptimizationManager.MemoryOptimizationStats stats = optimizer.getStats();

                // Update counter for new optimization cycles
                long currentCycles = stats.optimizationCycles;
                long lastRecordedCycles = (long) optimizationCyclesCounter.count();
                if (currentCycles > lastRecordedCycles) {
                    optimizationCyclesCounter.increment(currentCycles - lastRecordedCycles);
                }

                // Memory optimization stats are available via gauges

            } catch (Exception e) {
                LOGGER.debug("Error updating memory optimization metrics", e);
            }
        }
    }

    /**
     * Get total GC collections across all collectors
     */
    private long getCurrentGCCollections() {
        return gcBeans.stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionCount)
                .sum();
    }

    /**
     * Get total GC time across all collectors
     */
    private long getCurrentGCTime() {
        return gcBeans.stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionTime)
                .sum();
    }

    /**
     * Profile memory allocation patterns
     */
    public void profileAllocation() {
        try {
            // Simulate allocation tracking (in real implementation, this would use
            // JFR or other profiling tools)
            Runtime runtime = Runtime.getRuntime();
            long beforeGC = runtime.freeMemory();

            // Trigger minor allocation activity to measure
            Object testAllocation = new byte[1024];

            long afterGC = runtime.freeMemory();
            long allocated = beforeGC - afterGC;

            if (allocated > 0) {
                allocationCounter.increment();
                allocationSizeDistribution.record(allocated);
                totalAllocations.increment();
                totalAllocationSize.add(allocated);
            }

        } catch (Exception e) {
            LOGGER.warn("Error during allocation profiling", e);
        }
    }

    /**
     * Check if GC should be triggered based on memory usage
     */
    public boolean shouldTriggerGC(int thresholdMB) {
        double currentUsageMB = getCurrentMemoryUsageMB();
        double maxMemoryMB = getMaxMemoryMB();

        double usagePercentage = currentUsageMB / maxMemoryMB;

        return currentUsageMB > thresholdMB || usagePercentage > 0.85;
    }

    /**
     * Check memory usage thresholds and log warnings
     */
    private void checkMemoryThresholds() {
        double currentUsageMB = getCurrentMemoryUsageMB();
        double maxMemoryMB = getMaxMemoryMB();
        double usagePercentage = currentUsageMB / maxMemoryMB;

        if (usagePercentage > 0.9) {
            LOGGER.warn("High memory usage: {:.1f}MB / {:.1f}MB ({:.1f}%)",
                       currentUsageMB, maxMemoryMB, usagePercentage * 100);
        } else if (usagePercentage > 0.8) {
            LOGGER.debug("Elevated memory usage: {:.1f}MB / {:.1f}MB ({:.1f}%)",
                        currentUsageMB, maxMemoryMB, usagePercentage * 100);
        }
    }

    /**
     * Get current memory usage in MB
     */
    public double getCurrentMemoryUsageMB() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        return heapUsage.getUsed() / (1024.0 * 1024.0);
    }

    /**
     * Get maximum memory in MB
     */
    public double getMaxMemoryMB() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        return heapUsage.getMax() / (1024.0 * 1024.0);
    }

    /**
     * Get free memory in MB
     */
    public double getFreeMemoryMB() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        return (heapUsage.getMax() - heapUsage.getUsed()) / (1024.0 * 1024.0);
    }

    /**
     * Get allocated memory in MB
     */
    public double getAllocatedMemoryMB() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        return heapUsage.getCommitted() / (1024.0 * 1024.0);
    }

    // Gauge value providers
    private double getHeapUsedBytes() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    private double getHeapMaxBytes() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }

    private double getNonHeapUsedBytes() {
        return memoryMXBean.getNonHeapMemoryUsage().getUsed();
    }

    // Memory optimization gauge value providers
    private double getMemoryOptimizationSavedBytes() {
        MemoryOptimizationManager optimizer = memoryOptimizer.get();
        if (optimizer != null) {
            try {
                return optimizer.getStats().getTotalMemorySavedMB() * 1024 * 1024;
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private double getPoolHitRate() {
        MemoryOptimizationManager optimizer = memoryOptimizer.get();
        if (optimizer != null) {
            try {
                return optimizer.getStats().poolStats.overallHitRate * 100;
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    /**
     * Start baseline collection
     */
    public void startBaselineCollection() {
        collectingBaseline = true;
        baselineHeapUsage.reset();
        baselineGCCollections.reset();
        baselineGCTime.reset();
        baselineSamples = 0;
        LOGGER.info("Started memory metrics baseline collection");
    }

    /**
     * Stop baseline collection and save results
     */
    public void stopBaselineCollection() {
        if (!collectingBaseline) return;

        collectingBaseline = false;

        try {
            // Calculate baseline metrics
            double avgHeapUsage = baselineSamples > 0 ? baselineHeapUsage.sum() / baselineSamples : 0.0;
            long totalGCCollections = baselineGCCollections.sum();
            double avgGCTime = baselineSamples > 0 ? baselineGCTime.sum() / baselineSamples : 0.0;

            // Save baseline to file
            saveBaseline(avgHeapUsage, totalGCCollections, avgGCTime);

            LOGGER.info("Memory metrics baseline collection completed: {:.2f}MB avg heap, {} GC collections, {:.2f}ms avg GC time",
                       avgHeapUsage, totalGCCollections, avgGCTime);

        } catch (Exception e) {
            LOGGER.error("Error saving memory metrics baseline", e);
        }
    }

    /**
     * Save baseline metrics to file
     */
    private void saveBaseline(double avgHeapUsage, long gcCollections, double avgGCTime) throws IOException {
        Path baselineDir = Paths.get("performance/baselines");
        Files.createDirectories(baselineDir);

        Path baselineFile = baselineDir.resolve("memory-metrics-baseline.json");
        String baseline = String.format(
                "{\n" +
                "  \"timestamp\": %d,\n" +
                "  \"average_heap_usage_mb\": %.3f,\n" +
                "  \"max_heap_mb\": %.3f,\n" +
                "  \"gc_collections\": %d,\n" +
                "  \"average_gc_time_ms\": %.3f,\n" +
                "  \"samples\": %d\n" +
                "}",
                System.currentTimeMillis(),
                avgHeapUsage,
                getMaxMemoryMB(),
                gcCollections,
                avgGCTime,
                baselineSamples
        );

        Files.writeString(baselineFile, baseline);
    }

    /**
     * Generate memory metrics report
     */
    public MemoryMetricsReport getReport() {
        MemoryOptimizationManager.MemoryOptimizationStats optimizationStats = null;
        MemoryOptimizationManager optimizer = memoryOptimizer.get();
        if (optimizer != null) {
            try {
                optimizationStats = optimizer.getStats();
            } catch (Exception e) {
                LOGGER.debug("Error getting optimization stats for report", e);
            }
        }

        return new MemoryMetricsReport(
                getCurrentMemoryUsageMB(),
                getMaxMemoryMB(),
                getFreeMemoryMB(),
                getAllocatedMemoryMB(),
                getCurrentGCCollections(),
                getCurrentGCTime(),
                totalAllocations.sum(),
                optimizationStats
        );
    }

    /**
     * Memory metrics report data class
     */
    public static class MemoryMetricsReport {
        public final double currentUsageMB;
        public final double maxMemoryMB;
        public final double freeMemoryMB;
        public final double allocatedMemoryMB;
        public final long totalGCCollections;
        public final long totalGCTimeMs;
        public final long totalAllocations;
        public final MemoryOptimizationManager.MemoryOptimizationStats optimizationStats;

        public MemoryMetricsReport(double currentUsageMB, double maxMemoryMB, double freeMemoryMB,
                                  double allocatedMemoryMB, long totalGCCollections, long totalGCTimeMs,
                                  long totalAllocations, MemoryOptimizationManager.MemoryOptimizationStats optimizationStats) {
            this.currentUsageMB = currentUsageMB;
            this.maxMemoryMB = maxMemoryMB;
            this.freeMemoryMB = freeMemoryMB;
            this.allocatedMemoryMB = allocatedMemoryMB;
            this.totalGCCollections = totalGCCollections;
            this.totalGCTimeMs = totalGCTimeMs;
            this.totalAllocations = totalAllocations;
            this.optimizationStats = optimizationStats;
        }

        @Override
        public String toString() {
            String baseStats = String.format(
                    "MemoryMetrics{used=%.1fMB, max=%.1fMB, free=%.1fMB, gc=%d collections/%.1fms, allocations=%d}",
                    currentUsageMB, maxMemoryMB, freeMemoryMB, totalGCCollections, totalGCTimeMs / 1000.0, totalAllocations
            );

            if (optimizationStats != null) {
                return baseStats + String.format(", optimization{saved=%.1fMB, reduction=%.1f%%, targetMet=%s}",
                        optimizationStats.getTotalMemorySavedMB(),
                        optimizationStats.actualMemoryReduction * 100,
                        optimizationStats.isTargetMet());
            }

            return baseStats;
        }
    }
}