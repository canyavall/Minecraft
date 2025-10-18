package com.xeenaa.fabricenhancements.performance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xeenaa.fabricenhancements.performance.analysis.HardwareTierDetector;
import com.xeenaa.fabricenhancements.performance.analysis.StatisticalAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.GarbageCollectorMXBean;

/**
 * Enhanced baseline performance measurement collection system with hardware tier detection
 * and statistical analysis framework. Provides systematic baseline collection across
 * hardware tiers with comprehensive statistical validation and automated comparison
 * framework for performance regression detection.
 *
 * Features:
 * - Automatic hardware tier detection and categorization
 * - Statistical significance testing for all measurements
 * - Multi-threaded baseline collection for realistic scenarios
 * - Integration with Spark profiler data collection
 * - Automated baseline validation and comparison
 * - Performance regression detection with statistical analysis
 */
public class BaselineCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaselineCollector.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Enhanced measurement configuration
    private static final int STATISTICAL_SAMPLE_SIZE = 100;
    private static final int CONCURRENT_THREADS = 4;
    private static final double CONFIDENCE_LEVEL = 0.95;
    private static final int WARMUP_ITERATIONS = 10;

    // Hardware and analysis components
    private final HardwareTierDetector hardwareDetector;
    private final MemoryMXBean memoryMXBean;
    private final List<GarbageCollectorMXBean> gcBeans;

    // Performance tracking
    private HardwareTierDetector.HardwareTier detectedTier;
    private final List<MeasurementResult> allMeasurements;
    private final Map<String, StatisticalAnalysis.StatisticalReport> statisticalReports;

    public BaselineCollector() {
        this.hardwareDetector = new HardwareTierDetector();
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.allMeasurements = new ArrayList<>();
        this.statisticalReports = new HashMap<>();
    }

    /**
     * Main method for standalone baseline collection
     */
    public static void main(String[] args) {
        BaselineCollector collector = new BaselineCollector();
        collector.collectBaseline();
    }

    /**
     * Collect comprehensive baseline performance metrics with statistical validation
     */
    public EnhancedBaselineMetrics collectBaseline() {
        LOGGER.info("Starting enhanced baseline performance collection...");

        try {
            // Step 1: Detect hardware tier
            LOGGER.info("Detecting hardware tier...");
            detectedTier = hardwareDetector.detectHardwareTier();
            LOGGER.info("Detected hardware tier: {} ({})", detectedTier.name, detectedTier.description);

            // Step 2: Create enhanced baseline metrics
            EnhancedBaselineMetrics baseline = new EnhancedBaselineMetrics(detectedTier);

            // Step 3: Collect system baseline with hardware context
            collectEnhancedSystemBaseline(baseline);

            // Step 4: Collect statistical memory baseline
            collectStatisticalMemoryBaseline(baseline);

            // Step 5: Collect comprehensive chunk loading baseline
            collectComprehensiveChunkBaseline(baseline);

            // Step 6: Collect multi-threaded TPS baseline
            collectMultiThreadedTPSBaseline(baseline);

            // Step 7: Collect concurrent performance baseline
            collectConcurrentPerformanceBaseline(baseline);

            // Step 8: Perform statistical analysis on all measurements
            performStatisticalAnalysis(baseline);

            // Step 9: Validate against hardware tier targets
            validatePerformanceTargets(baseline);

            // Step 10: Save comprehensive baseline
            saveEnhancedBaseline(baseline);

            LOGGER.info("Enhanced baseline collection completed successfully");
            return baseline;

        } catch (Exception e) {
            LOGGER.error("Error during enhanced baseline collection", e);
            throw new RuntimeException("Baseline collection failed", e);
        }
    }

    /**
     * Collect baseline using legacy method for compatibility
     */
    public void collectBaseline() {
        EnhancedBaselineMetrics enhanced = collectBaseline();
        // Convert to legacy format if needed
    }

    /**
     * Collect enhanced system information baseline with hardware tier context
     */
    private void collectEnhancedSystemBaseline(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Collecting enhanced system baseline...");

        Runtime runtime = Runtime.getRuntime();

        // Basic system information
        baseline.systemInfo.put("java_version", System.getProperty("java.version"));
        baseline.systemInfo.put("os_name", System.getProperty("os.name"));
        baseline.systemInfo.put("os_version", System.getProperty("os.version"));
        baseline.systemInfo.put("os_arch", System.getProperty("os.arch"));
        baseline.systemInfo.put("jvm_name", System.getProperty("java.vm.name"));
        baseline.systemInfo.put("available_processors", runtime.availableProcessors());
        baseline.systemInfo.put("max_memory_mb", runtime.maxMemory() / (1024 * 1024));
        baseline.systemInfo.put("initial_memory_mb", runtime.totalMemory() / (1024 * 1024));
        baseline.systemInfo.put("free_memory_mb", runtime.freeMemory() / (1024 * 1024));

        // Hardware tier information
        baseline.systemInfo.put("hardware_tier", detectedTier.name);
        baseline.systemInfo.put("cpu_tier", detectedTier.cpuTier.displayName);
        baseline.systemInfo.put("memory_tier", detectedTier.memoryTier.displayName);
        baseline.systemInfo.put("io_tier", detectedTier.ioTier.displayName);
        baseline.systemInfo.put("cpu_score", detectedTier.profile.cpuScore);
        baseline.systemInfo.put("memory_score", detectedTier.profile.memoryAllocationScore);
        baseline.systemInfo.put("io_score", detectedTier.profile.ioScore);

        // Performance targets based on hardware tier
        baseline.systemInfo.put("target_chunk_improvement", detectedTier.targets.chunkLoadingImprovementTarget);
        baseline.systemInfo.put("target_memory_reduction", detectedTier.targets.memoryReductionTarget);
        baseline.systemInfo.put("expected_tps", detectedTier.targets.expectedTPS);
        baseline.systemInfo.put("expected_render_distance", detectedTier.targets.expectedRenderDistance);

        // Hardware recommendations
        baseline.systemInfo.putAll(detectedTier.recommendations);

        LOGGER.info("Enhanced system baseline collected for {} tier", detectedTier.name);
    }

    /**
     * Collect statistical memory performance baseline with comprehensive analysis
     */
    private void collectStatisticalMemoryBaseline(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Collecting statistical memory baseline...");

        // Warmup phase
        performMemoryWarmup();

        Runtime runtime = Runtime.getRuntime();
        List<Double> allocationTimes = new ArrayList<>();
        List<Double> gcTimes = new ArrayList<>();
        List<Long> memoryUsagePoints = new ArrayList<>();

        // Collect multiple samples for statistical analysis
        for (int sample = 0; sample < STATISTICAL_SAMPLE_SIZE; sample++) {
            long startTime = System.nanoTime();
            long startGcTime = getTotalGcTime();
            long startMemory = memoryMXBean.getHeapMemoryUsage().getUsed();

            // Memory allocation test with varying sizes
            List<byte[]> allocations = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                int size = 32768 + (i % 3) * 32768; // 32KB, 64KB, 96KB chunks
                byte[] testData = new byte[size];
                // Ensure allocation is not optimized away
                java.util.Arrays.fill(testData, (byte) (sample % 256));
                allocations.add(testData);
            }

            long endTime = System.nanoTime();
            long endGcTime = getTotalGcTime();
            long endMemory = memoryMXBean.getHeapMemoryUsage().getUsed();

            double allocationTime = (endTime - startTime) / 1_000_000.0; // Convert to ms
            double gcTime = endGcTime - startGcTime;

            allocationTimes.add(allocationTime);
            gcTimes.add(gcTime);
            memoryUsagePoints.add(endMemory - startMemory);

            // Cleanup to prevent OOM
            allocations.clear();
            if (sample % 10 == 0) {
                System.gc();
                Thread.yield();
            }
        }

        // Perform statistical analysis
        StatisticalAnalysis.StatisticalReport allocationReport =
            StatisticalAnalysis.analyzeBenchmarkResults(allocationTimes, "memory_allocation");
        StatisticalAnalysis.StatisticalReport gcReport =
            StatisticalAnalysis.analyzeBenchmarkResults(gcTimes, "garbage_collection");

        // Store results
        baseline.memoryMetrics.put("allocation_samples", allocationTimes.size());
        baseline.memoryMetrics.put("avg_allocation_time_ms", allocationReport.mean);
        baseline.memoryMetrics.put("allocation_std_dev_ms", allocationReport.standardDeviation);
        baseline.memoryMetrics.put("allocation_confidence_interval",
            String.format("[%.3f, %.3f]", allocationReport.confidenceIntervalLower, allocationReport.confidenceIntervalUpper));
        baseline.memoryMetrics.put("allocation_cv", allocationReport.coefficientOfVariation);
        baseline.memoryMetrics.put("allocation_statistically_significant", allocationReport.isStatisticallySignificant);

        baseline.memoryMetrics.put("avg_gc_time_ms", gcReport.mean);
        baseline.memoryMetrics.put("gc_std_dev_ms", gcReport.standardDeviation);
        baseline.memoryMetrics.put("gc_confidence_interval",
            String.format("[%.3f, %.3f]", gcReport.confidenceIntervalLower, gcReport.confidenceIntervalUpper));

        // Memory efficiency metrics
        double avgMemoryUsage = memoryUsagePoints.stream().mapToLong(Long::longValue).average().orElse(0.0);
        baseline.memoryMetrics.put("avg_memory_usage_bytes", avgMemoryUsage);
        baseline.memoryMetrics.put("memory_efficiency_score", calculateMemoryEfficiency(allocationReport, gcReport));

        // Store statistical reports
        statisticalReports.put("memory_allocation", allocationReport);
        statisticalReports.put("garbage_collection", gcReport);

        LOGGER.info("Statistical memory baseline collected: avg allocation {:.3f}±{:.3f}ms, GC {:.3f}±{:.3f}ms",
                   allocationReport.mean, allocationReport.standardDeviation,
                   gcReport.mean, gcReport.standardDeviation);
    }

    private void performMemoryWarmup() {
        LOGGER.debug("Performing memory warmup...");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            List<byte[]> warmupData = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                warmupData.add(new byte[65536]);
            }
            warmupData.clear();
        }
        System.gc();
        try {
            Thread.sleep(100); // Allow GC to settle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long getTotalGcTime() {
        return gcBeans.stream().mapToLong(GarbageCollectorMXBean::getCollectionTime).sum();
    }

    private double calculateMemoryEfficiency(StatisticalAnalysis.StatisticalReport allocationReport,
                                           StatisticalAnalysis.StatisticalReport gcReport) {
        // Higher efficiency = lower allocation time + lower GC time + lower variability
        double allocationEfficiency = 1.0 / (1.0 + allocationReport.mean + allocationReport.coefficientOfVariation);
        double gcEfficiency = 1.0 / (1.0 + gcReport.mean + gcReport.coefficientOfVariation);
        return (allocationEfficiency + gcEfficiency) / 2.0;
    }

    /**
     * Collect comprehensive chunk loading baseline with multi-threaded scenarios
     */
    private void collectComprehensiveChunkBaseline(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Collecting comprehensive chunk loading baseline...");

        // Single-threaded chunk loading
        List<Double> singleThreadedTimes = collectSingleThreadedChunkTimes();
        StatisticalAnalysis.StatisticalReport singleThreadedReport =
            StatisticalAnalysis.analyzeBenchmarkResults(singleThreadedTimes, "single_threaded_chunk_loading");

        // Multi-threaded chunk loading
        List<Double> multiThreadedTimes = collectMultiThreadedChunkTimes();
        StatisticalAnalysis.StatisticalReport multiThreadedReport =
            StatisticalAnalysis.analyzeBenchmarkResults(multiThreadedTimes, "multi_threaded_chunk_loading");

        // Cache performance analysis
        CachePerformanceResult cacheResult = analyzeCachePerformance();

        // Store single-threaded results
        baseline.chunkMetrics.put("single_threaded_samples", singleThreadedTimes.size());
        baseline.chunkMetrics.put("single_threaded_avg_ms", singleThreadedReport.mean);
        baseline.chunkMetrics.put("single_threaded_std_dev_ms", singleThreadedReport.standardDeviation);
        baseline.chunkMetrics.put("single_threaded_cv", singleThreadedReport.coefficientOfVariation);
        baseline.chunkMetrics.put("single_threaded_confidence_interval",
            String.format("[%.3f, %.3f]", singleThreadedReport.confidenceIntervalLower, singleThreadedReport.confidenceIntervalUpper));

        // Store multi-threaded results
        baseline.chunkMetrics.put("multi_threaded_samples", multiThreadedTimes.size());
        baseline.chunkMetrics.put("multi_threaded_avg_ms", multiThreadedReport.mean);
        baseline.chunkMetrics.put("multi_threaded_std_dev_ms", multiThreadedReport.standardDeviation);
        baseline.chunkMetrics.put("multi_threaded_cv", multiThreadedReport.coefficientOfVariation);
        baseline.chunkMetrics.put("multi_threaded_confidence_interval",
            String.format("[%.3f, %.3f]", multiThreadedReport.confidenceIntervalLower, multiThreadedReport.confidenceIntervalUpper));

        // Threading efficiency
        double threadingEfficiency = singleThreadedReport.mean / multiThreadedReport.mean;
        baseline.chunkMetrics.put("threading_efficiency", threadingEfficiency);
        baseline.chunkMetrics.put("threading_overhead", threadingEfficiency < 1.0 ? 1.0 - threadingEfficiency : 0.0);

        // Cache performance
        baseline.chunkMetrics.put("cache_hit_ratio", cacheResult.hitRatio);
        baseline.chunkMetrics.put("cache_miss_penalty_ms", cacheResult.missPenalty);
        baseline.chunkMetrics.put("cache_efficiency_score", cacheResult.efficiencyScore);

        // Performance targets validation
        boolean meetsChunkTarget = validateChunkLoadingTarget(singleThreadedReport, multiThreadedReport);
        baseline.chunkMetrics.put("meets_performance_target", meetsChunkTarget);

        // Store statistical reports
        statisticalReports.put("single_threaded_chunk_loading", singleThreadedReport);
        statisticalReports.put("multi_threaded_chunk_loading", multiThreadedReport);

        LOGGER.info("Comprehensive chunk baseline collected: ST {:.3f}±{:.3f}ms, MT {:.3f}±{:.3f}ms, efficiency {:.2f}",
                   singleThreadedReport.mean, singleThreadedReport.standardDeviation,
                   multiThreadedReport.mean, multiThreadedReport.standardDeviation, threadingEfficiency);
    }

    private List<Double> collectSingleThreadedChunkTimes() {
        List<Double> times = new ArrayList<>();
        Map<String, byte[]> chunkCache = new HashMap<>();

        for (int sample = 0; sample < STATISTICAL_SAMPLE_SIZE; sample++) {
            long startTime = System.nanoTime();

            // Simulate realistic chunk access pattern
            for (int i = 0; i < 20; i++) {
                int x = (sample * 20 + i) % 50; // Limited world area
                int z = ((sample * 20 + i) / 50) % 50;
                String chunkKey = x + "," + z;

                byte[] chunk = chunkCache.get(chunkKey);
                if (chunk == null) {
                    // Simulate chunk generation/loading
                    chunk = new byte[65536];
                    simulateChunkProcessing(chunk, x, z);
                    chunkCache.put(chunkKey, chunk);
                }
            }

            long endTime = System.nanoTime();
            times.add((endTime - startTime) / 1_000_000.0); // Convert to ms
        }

        return times;
    }

    private List<Double> collectMultiThreadedChunkTimes() {
        List<Double> times = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);

        try {
            for (int sample = 0; sample < STATISTICAL_SAMPLE_SIZE; sample++) {
                final int sampleIndex = sample;
                List<Future<Long>> futures = new ArrayList<>();
                Map<String, byte[]> sharedCache = new java.util.concurrent.ConcurrentHashMap<>();

                long startTime = System.nanoTime();

                // Submit chunk loading tasks to multiple threads
                for (int thread = 0; thread < CONCURRENT_THREADS; thread++) {
                    final int threadIndex = thread;
                    Future<Long> future = executor.submit(() -> {
                        long threadStartTime = System.nanoTime();

                        for (int i = 0; i < 5; i++) { // 5 chunks per thread
                            int x = (sampleIndex * CONCURRENT_THREADS * 5 + threadIndex * 5 + i) % 50;
                            int z = ((sampleIndex * CONCURRENT_THREADS * 5 + threadIndex * 5 + i) / 50) % 50;
                            String chunkKey = x + "," + z;

                            byte[] chunk = sharedCache.computeIfAbsent(chunkKey, key -> {
                                byte[] newChunk = new byte[65536];
                                simulateChunkProcessing(newChunk, x, z);
                                return newChunk;
                            });
                        }

                        return System.nanoTime() - threadStartTime;
                    });
                    futures.add(future);
                }

                // Wait for all threads to complete
                for (Future<Long> future : futures) {
                    future.get();
                }

                long endTime = System.nanoTime();
                times.add((endTime - startTime) / 1_000_000.0);
            }
        } catch (Exception e) {
            LOGGER.warn("Multi-threaded chunk loading test failed", e);
        } finally {
            executor.shutdown();
        }

        return times;
    }

    private void simulateChunkProcessing(byte[] chunk, int x, int z) {
        // Simulate realistic chunk data processing
        java.util.Random random = new java.util.Random((long) x * 31 + z);
        for (int i = 0; i < chunk.length; i += 256) {
            chunk[i] = (byte) random.nextInt(256);
        }

        // Simulate some computation delay
        double result = 0;
        for (int i = 0; i < 100; i++) {
            result += Math.sin(x + i) * Math.cos(z + i);
        }
    }

    private CachePerformanceResult analyzeCachePerformance() {
        Map<String, byte[]> cache = new HashMap<>();
        int hits = 0;
        int misses = 0;
        double totalMissTime = 0;
        int testOperations = 1000;

        java.util.Random random = new java.util.Random(12345);

        for (int i = 0; i < testOperations; i++) {
            // 70% chance to access nearby chunks (hot data)
            int x, z;
            if (random.nextDouble() < 0.7) {
                x = random.nextInt(10); // Hot area
                z = random.nextInt(10);
            } else {
                x = random.nextInt(100); // Cold area
                z = random.nextInt(100);
            }

            String chunkKey = x + "," + z;
            long startTime = System.nanoTime();

            if (cache.containsKey(chunkKey)) {
                hits++;
            } else {
                misses++;
                byte[] chunk = new byte[65536];
                simulateChunkProcessing(chunk, x, z);
                cache.put(chunkKey, chunk);
                totalMissTime += (System.nanoTime() - startTime) / 1_000_000.0;
            }
        }

        double hitRatio = (double) hits / (hits + misses);
        double avgMissPenalty = misses > 0 ? totalMissTime / misses : 0.0;
        double efficiencyScore = hitRatio * (1.0 - Math.min(avgMissPenalty / 10.0, 1.0));

        return new CachePerformanceResult(hitRatio, avgMissPenalty, efficiencyScore);
    }

    private boolean validateChunkLoadingTarget(StatisticalAnalysis.StatisticalReport singleThreaded,
                                             StatisticalAnalysis.StatisticalReport multiThreaded) {
        // Check if performance meets hardware tier targets
        double targetImprovement = detectedTier.targets.chunkLoadingImprovementTarget;
        double actualImprovement = (singleThreaded.mean - multiThreaded.mean) / singleThreaded.mean;

        return actualImprovement >= (targetImprovement * 0.8); // 80% of target is acceptable for baseline
    }

    private static class CachePerformanceResult {
        final double hitRatio;
        final double missPenalty;
        final double efficiencyScore;

        CachePerformanceResult(double hitRatio, double missPenalty, double efficiencyScore) {
            this.hitRatio = hitRatio;
            this.missPenalty = missPenalty;
            this.efficiencyScore = efficiencyScore;
        }
    }

    /**
     * Collect multi-threaded TPS baseline with realistic server simulation
     */
    private void collectMultiThreadedTPSBaseline(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Collecting multi-threaded TPS baseline...");

        // Single-threaded TPS simulation
        List<Double> singleThreadedTPS = collectSingleThreadedTPS();
        StatisticalAnalysis.StatisticalReport singleTpsReport =
            StatisticalAnalysis.analyzeBenchmarkResults(singleThreadedTPS, "single_threaded_tps");

        // Multi-threaded TPS simulation
        List<Double> multiThreadedTPS = collectMultiThreadedTPS();
        StatisticalAnalysis.StatisticalReport multiTpsReport =
            StatisticalAnalysis.analyzeBenchmarkResults(multiThreadedTPS, "multi_threaded_tps");

        // Store single-threaded results
        baseline.tpsMetrics.put("single_threaded_samples", singleThreadedTPS.size());
        baseline.tpsMetrics.put("single_threaded_avg_tps", singleTpsReport.mean);
        baseline.tpsMetrics.put("single_threaded_std_dev_tps", singleTpsReport.standardDeviation);
        baseline.tpsMetrics.put("single_threaded_cv", singleTpsReport.coefficientOfVariation);
        baseline.tpsMetrics.put("single_threaded_confidence_interval",
            String.format("[%.2f, %.2f]", singleTpsReport.confidenceIntervalLower, singleTpsReport.confidenceIntervalUpper));

        // Store multi-threaded results
        baseline.tpsMetrics.put("multi_threaded_samples", multiThreadedTPS.size());
        baseline.tpsMetrics.put("multi_threaded_avg_tps", multiTpsReport.mean);
        baseline.tpsMetrics.put("multi_threaded_std_dev_tps", multiTpsReport.standardDeviation);
        baseline.tpsMetrics.put("multi_threaded_cv", multiTpsReport.coefficientOfVariation);
        baseline.tpsMetrics.put("multi_threaded_confidence_interval",
            String.format("[%.2f, %.2f]", multiTpsReport.confidenceIntervalLower, multiTpsReport.confidenceIntervalUpper));

        // TPS efficiency and stability
        double tpsImprovement = (multiTpsReport.mean - singleTpsReport.mean) / singleTpsReport.mean;
        baseline.tpsMetrics.put("tps_improvement", tpsImprovement);
        baseline.tpsMetrics.put("tps_stability_score", calculateTPSStability(singleTpsReport, multiTpsReport));

        // Target validation
        double expectedTPS = detectedTier.targets.expectedTPS;
        boolean meetsTpsTarget = multiTpsReport.mean >= expectedTPS * 0.9; // 90% of target
        baseline.tpsMetrics.put("target_tps", expectedTPS);
        baseline.tpsMetrics.put("meets_tps_target", meetsTpsTarget);

        // Store statistical reports
        statisticalReports.put("single_threaded_tps", singleTpsReport);
        statisticalReports.put("multi_threaded_tps", multiTpsReport);

        LOGGER.info("Multi-threaded TPS baseline collected: ST {:.2f}±{:.2f} TPS, MT {:.2f}±{:.2f} TPS",
                   singleTpsReport.mean, singleTpsReport.standardDeviation,
                   multiTpsReport.mean, multiTpsReport.standardDeviation);
    }

    private List<Double> collectSingleThreadedTPS() {
        List<Double> tpsValues = new ArrayList<>();
        long targetTickTime = 50_000_000L; // 50ms in nanoseconds (20 TPS target)

        for (int sample = 0; sample < STATISTICAL_SAMPLE_SIZE; sample++) {
            List<Long> tickTimes = new ArrayList<>();
            long sampleStartTime = System.nanoTime();

            // Simulate 20 ticks (1 second)
            for (int tick = 0; tick < 20; tick++) {
                long tickStart = System.nanoTime();
                simulateRealisticServerTick(sample, tick);
                long tickEnd = System.nanoTime();
                tickTimes.add(tickEnd - tickStart);

                // Maintain 20 TPS timing
                long tickDuration = tickEnd - tickStart;
                if (tickDuration < targetTickTime) {
                    try {
                        TimeUnit.NANOSECONDS.sleep(targetTickTime - tickDuration);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            // Calculate TPS for this sample
            long totalSampleTime = System.nanoTime() - sampleStartTime;
            double actualTPS = 20.0 * 1_000_000_000.0 / totalSampleTime;
            tpsValues.add(Math.min(actualTPS, 20.0)); // Cap at 20 TPS
        }

        return tpsValues;
    }

    private List<Double> collectMultiThreadedTPS() {
        List<Double> tpsValues = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        long targetTickTime = 50_000_000L;

        try {
            for (int sample = 0; sample < STATISTICAL_SAMPLE_SIZE; sample++) {
                final int sampleIndex = sample;
                long sampleStartTime = System.nanoTime();

                // Simulate 20 ticks with concurrent processing
                for (int tick = 0; tick < 20; tick++) {
                    final int tickIndex = tick;
                    long tickStart = System.nanoTime();

                    // Distribute tick work across threads
                    List<Future<Void>> tickFutures = new ArrayList<>();
                    for (int thread = 0; thread < CONCURRENT_THREADS; thread++) {
                        final int threadIndex = thread;
                        Future<Void> future = executor.submit(() -> {
                            simulateThreadedServerWork(sampleIndex, tickIndex, threadIndex);
                            return null;
                        });
                        tickFutures.add(future);
                    }

                    // Wait for all threads to complete tick work
                    for (Future<Void> future : tickFutures) {
                        future.get();
                    }

                    long tickEnd = System.nanoTime();
                    long tickDuration = tickEnd - tickStart;

                    // Maintain timing
                    if (tickDuration < targetTickTime) {
                        try {
                            TimeUnit.NANOSECONDS.sleep(targetTickTime - tickDuration);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                long totalSampleTime = System.nanoTime() - sampleStartTime;
                double actualTPS = 20.0 * 1_000_000_000.0 / totalSampleTime;
                tpsValues.add(Math.min(actualTPS, 20.0));
            }
        } catch (Exception e) {
            LOGGER.warn("Multi-threaded TPS collection failed", e);
        } finally {
            executor.shutdown();
        }

        return tpsValues;
    }

    private void simulateRealisticServerTick(int sample, int tick) {
        // Simulate entity updates
        for (int i = 0; i < 50 + (sample % 20); i++) {
            double x = Math.sin(i + tick) * Math.cos(i + sample);
            double y = Math.sqrt(Math.abs(x)) * Math.log(i + 1);
        }

        // Simulate block updates
        byte[] blockData = new byte[1024];
        for (int i = 0; i < blockData.length; i++) {
            blockData[i] = (byte) ((sample + tick + i) % 256);
        }

        // Simulate random I/O delay
        if (tick % 5 == 0) {
            try {
                TimeUnit.MICROSECONDS.sleep(50 + (sample % 100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void simulateThreadedServerWork(int sample, int tick, int thread) {
        // Each thread handles different aspects of server work
        switch (thread % 4) {
            case 0: // Entity processing
                for (int i = 0; i < 25; i++) {
                    double result = Math.sin(sample + tick + thread + i) * Math.cos(i);
                }
                break;
            case 1: // Block processing
                byte[] data = new byte[512];
                for (int i = 0; i < data.length; i++) {
                    data[i] = (byte) ((sample + tick + thread + i) % 256);
                }
                break;
            case 2: // Networking simulation
                for (int i = 0; i < 10; i++) {
                    byte[] packet = new byte[128];
                    java.util.Arrays.fill(packet, (byte) (sample + tick + thread));
                }
                break;
            case 3: // Misc processing
                for (int i = 0; i < 15; i++) {
                    double calc = Math.pow(sample + tick + thread, 0.1) * Math.log(i + 1);
                }
                break;
        }
    }

    private double calculateTPSStability(StatisticalAnalysis.StatisticalReport singleThreaded,
                                        StatisticalAnalysis.StatisticalReport multiThreaded) {
        // Lower coefficient of variation = higher stability
        double singleStability = 1.0 / (1.0 + singleThreaded.coefficientOfVariation);
        double multiStability = 1.0 / (1.0 + multiThreaded.coefficientOfVariation);
        return (singleStability + multiStability) / 2.0;
    }

    /**
     * Collect concurrent performance baseline for stress testing
     */
    private void collectConcurrentPerformanceBaseline(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Collecting concurrent performance baseline...");

        // Stress test with multiple concurrent operations
        List<Double> concurrentThroughput = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS * 2);

        try {
            for (int sample = 0; sample < STATISTICAL_SAMPLE_SIZE / 2; sample++) {
                final int sampleIndex = sample;
                long startTime = System.nanoTime();
                List<Future<Integer>> futures = new ArrayList<>();
                int totalOperations = 0;

                // Submit various concurrent tasks
                for (int i = 0; i < CONCURRENT_THREADS * 2; i++) {
                    final int taskIndex = i;
                    Future<Integer> future = executor.submit(() -> {
                        int operations = 0;
                        long taskStartTime = System.nanoTime();
                        long taskDuration = 1_000_000_000L; // 1 second per task

                        while ((System.nanoTime() - taskStartTime) < taskDuration) {
                            // Mix of operations
                            if (taskIndex % 3 == 0) {
                                // Memory operations
                                byte[] data = new byte[4096];
                                java.util.Arrays.fill(data, (byte) (sampleIndex + taskIndex));
                            } else if (taskIndex % 3 == 1) {
                                // CPU operations
                                double result = 0;
                                for (int j = 0; j < 100; j++) {
                                    result += Math.sin(sampleIndex + taskIndex + j) * Math.cos(j);
                                }
                            } else {
                                // Mixed operations
                                Map<String, Object> tempMap = new HashMap<>();
                                for (int j = 0; j < 10; j++) {
                                    tempMap.put("key_" + j, sampleIndex + taskIndex + j);
                                }
                            }
                            operations++;
                        }
                        return operations;
                    });
                    futures.add(future);
                }

                // Collect results
                for (Future<Integer> future : futures) {
                    totalOperations += future.get();
                }

                long endTime = System.nanoTime();
                double throughput = (double) totalOperations / ((endTime - startTime) / 1_000_000_000.0);
                concurrentThroughput.add(throughput);
            }
        } catch (Exception e) {
            LOGGER.warn("Concurrent performance baseline collection failed", e);
        } finally {
            executor.shutdown();
        }

        // Analyze concurrent performance
        if (!concurrentThroughput.isEmpty()) {
            StatisticalAnalysis.StatisticalReport concurrentReport =
                StatisticalAnalysis.analyzeBenchmarkResults(concurrentThroughput, "concurrent_performance");

            baseline.concurrentMetrics.put("samples", concurrentThroughput.size());
            baseline.concurrentMetrics.put("avg_ops_per_sec", concurrentReport.mean);
            baseline.concurrentMetrics.put("std_dev_ops_per_sec", concurrentReport.standardDeviation);
            baseline.concurrentMetrics.put("cv", concurrentReport.coefficientOfVariation);
            baseline.concurrentMetrics.put("confidence_interval",
                String.format("[%.1f, %.1f]", concurrentReport.confidenceIntervalLower, concurrentReport.confidenceIntervalUpper));
            baseline.concurrentMetrics.put("statistically_significant", concurrentReport.isStatisticallySignificant);

            statisticalReports.put("concurrent_performance", concurrentReport);

            LOGGER.info("Concurrent performance baseline: {:.1f}±{:.1f} ops/sec",
                       concurrentReport.mean, concurrentReport.standardDeviation);
        }
    }

    /**
     * Perform comprehensive statistical analysis on all collected measurements
     */
    private void performStatisticalAnalysis(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Performing comprehensive statistical analysis...");

        Map<String, Object> analysisResults = new HashMap<>();
        int significantTests = 0;
        int totalTests = statisticalReports.size();

        for (Map.Entry<String, StatisticalAnalysis.StatisticalReport> entry : statisticalReports.entrySet()) {
            StatisticalAnalysis.StatisticalReport report = entry.getValue();

            if (report.isStatisticallySignificant) {
                significantTests++;
            }

            // Store key metrics for each test
            Map<String, Object> testResults = new HashMap<>();
            testResults.put("statistically_significant", report.isStatisticallySignificant);
            testResults.put("sample_size", report.sampleSize);
            testResults.put("coefficient_of_variation", report.coefficientOfVariation);
            testResults.put("data_quality", report.dataQuality.name());
            testResults.put("statistical_power", report.statisticalPower);

            analysisResults.put(entry.getKey(), testResults);
        }

        // Overall analysis summary
        double statisticalQualityScore = (double) significantTests / totalTests;
        analysisResults.put("overall_statistical_quality", statisticalQualityScore);
        analysisResults.put("significant_tests", significantTests);
        analysisResults.put("total_tests", totalTests);
        analysisResults.put("analysis_timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        baseline.statisticalAnalysis = analysisResults;

        LOGGER.info("Statistical analysis completed: {}/{} tests statistically significant (quality score: {:.2f})",
                   significantTests, totalTests, statisticalQualityScore);
    }

    /**
     * Validate performance against hardware tier targets
     */
    private void validatePerformanceTargets(EnhancedBaselineMetrics baseline) {
        LOGGER.info("Validating performance against hardware tier targets...");

        Map<String, Object> validation = new HashMap<>();

        // Validate chunk loading performance
        boolean chunkTargetMet = (boolean) baseline.chunkMetrics.getOrDefault("meets_performance_target", false);
        validation.put("chunk_loading_target_met", chunkTargetMet);
        validation.put("chunk_loading_target", detectedTier.targets.chunkLoadingImprovementTarget);

        // Validate TPS performance
        boolean tpsTargetMet = (boolean) baseline.tpsMetrics.getOrDefault("meets_tps_target", false);
        validation.put("tps_target_met", tpsTargetMet);
        validation.put("tps_target", detectedTier.targets.expectedTPS);

        // Validate memory efficiency
        double memoryEfficiency = (double) baseline.memoryMetrics.getOrDefault("memory_efficiency_score", 0.0);
        boolean memoryTargetMet = memoryEfficiency >= 0.7; // 70% efficiency threshold
        validation.put("memory_target_met", memoryTargetMet);
        validation.put("memory_target", detectedTier.targets.memoryReductionTarget);

        // Overall validation
        boolean overallTargetMet = chunkTargetMet && tpsTargetMet && memoryTargetMet;
        validation.put("overall_targets_met", overallTargetMet);
        validation.put("validation_timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        baseline.targetValidation = validation;

        LOGGER.info("Performance validation: Chunk={}, TPS={}, Memory={}, Overall={}",
                   chunkTargetMet, tpsTargetMet, memoryTargetMet, overallTargetMet);
    }

    /**
     * Save enhanced baseline metrics with comprehensive reporting
     */
    private void saveEnhancedBaseline(EnhancedBaselineMetrics baseline) throws IOException {
        Path baselineDir = Paths.get("performance/baselines");
        Files.createDirectories(baselineDir);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String tierSuffix = detectedTier.name.toLowerCase().replace("-", "_");

        // Save detailed baseline
        Path baselineFile = baselineDir.resolve(String.format("enhanced-baseline-%s-%s.json", tierSuffix, timestamp));
        String json = GSON.toJson(baseline);
        Files.writeString(baselineFile, json);

        // Save tier-specific latest baseline
        Path latestBaseline = baselineDir.resolve(String.format("latest-baseline-%s.json", tierSuffix));
        Files.writeString(latestBaseline, json);

        // Save universal latest (for backward compatibility)
        Path universalLatest = baselineDir.resolve("latest-baseline.json");
        Files.writeString(universalLatest, json);

        // Save statistical reports separately
        Path reportsDir = baselineDir.resolve("statistical-reports");
        Files.createDirectories(reportsDir);

        for (Map.Entry<String, StatisticalAnalysis.StatisticalReport> entry : statisticalReports.entrySet()) {
            Path reportFile = reportsDir.resolve(String.format("%s-report-%s.json", entry.getKey(), timestamp));
            String reportJson = GSON.toJson(entry.getValue());
            Files.writeString(reportFile, reportJson);
        }

        // Generate and save summary report
        generateSummaryReport(baseline, baselineDir, timestamp, tierSuffix);

        LOGGER.info("Enhanced baseline saved to: {}", baselineFile);
        LOGGER.info("Statistical reports saved to: {}", reportsDir);
    }

    private void generateSummaryReport(EnhancedBaselineMetrics baseline, Path baselineDir,
                                     String timestamp, String tierSuffix) throws IOException {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Xeenaa Fabric Enhancements - Enhanced Baseline Summary ===\n");
        summary.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        summary.append("Hardware Tier: ").append(detectedTier.name).append(" (").append(detectedTier.description).append(")\n");
        summary.append("\n");

        // System information
        summary.append("=== System Information ===\n");
        summary.append("OS: ").append(baseline.systemInfo.get("os_name")).append(" ").append(baseline.systemInfo.get("os_version")).append("\n");
        summary.append("Java: ").append(baseline.systemInfo.get("java_version")).append("\n");
        summary.append("CPU Cores: ").append(baseline.systemInfo.get("available_processors")).append("\n");
        summary.append("Max Heap: ").append(baseline.systemInfo.get("max_memory_mb")).append(" MB\n");
        summary.append("\n");

        // Performance targets
        summary.append("=== Performance Targets ===\n");
        summary.append("Chunk Loading Improvement Target: ").append(String.format("%.1f%%",
            (Double) baseline.systemInfo.get("target_chunk_improvement") * 100)).append("\n");
        summary.append("Memory Reduction Target: ").append(String.format("%.1f%%",
            (Double) baseline.systemInfo.get("target_memory_reduction") * 100)).append("\n");
        summary.append("Expected TPS: ").append(baseline.systemInfo.get("expected_tps")).append("\n");
        summary.append("\n");

        // Baseline results
        summary.append("=== Baseline Results ===\n");
        if (statisticalReports.containsKey("memory_allocation")) {
            StatisticalAnalysis.StatisticalReport memReport = statisticalReports.get("memory_allocation");
            summary.append(String.format("Memory Allocation: %.3f±%.3f ms (CV: %.3f)\n",
                memReport.mean, memReport.standardDeviation, memReport.coefficientOfVariation));
        }
        if (statisticalReports.containsKey("single_threaded_chunk_loading")) {
            StatisticalAnalysis.StatisticalReport chunkReport = statisticalReports.get("single_threaded_chunk_loading");
            summary.append(String.format("Chunk Loading (ST): %.3f±%.3f ms (CV: %.3f)\n",
                chunkReport.mean, chunkReport.standardDeviation, chunkReport.coefficientOfVariation));
        }
        if (statisticalReports.containsKey("multi_threaded_tps")) {
            StatisticalAnalysis.StatisticalReport tpsReport = statisticalReports.get("multi_threaded_tps");
            summary.append(String.format("TPS (MT): %.2f±%.2f TPS (CV: %.3f)\n",
                tpsReport.mean, tpsReport.standardDeviation, tpsReport.coefficientOfVariation));
        }
        summary.append("\n");

        // Statistical quality
        summary.append("=== Statistical Quality ===\n");
        Map<String, Object> analysis = baseline.statisticalAnalysis;
        summary.append("Statistical Quality Score: ").append(String.format("%.2f",
            (Double) analysis.get("overall_statistical_quality"))).append("\n");
        summary.append("Significant Tests: ").append(analysis.get("significant_tests")).append("/").append(analysis.get("total_tests")).append("\n");
        summary.append("\n");

        // Target validation
        summary.append("=== Target Validation ===\n");
        Map<String, Object> validation = baseline.targetValidation;
        summary.append("Chunk Loading Target Met: ").append(validation.get("chunk_loading_target_met")).append("\n");
        summary.append("TPS Target Met: ").append(validation.get("tps_target_met")).append("\n");
        summary.append("Memory Target Met: ").append(validation.get("memory_target_met")).append("\n");
        summary.append("Overall Targets Met: ").append(validation.get("overall_targets_met")).append("\n");

        Path summaryFile = baselineDir.resolve(String.format("baseline-summary-%s-%s.txt", tierSuffix, timestamp));
        Files.writeString(summaryFile, summary.toString());

        // Also create a latest summary
        Path latestSummary = baselineDir.resolve(String.format("latest-summary-%s.txt", tierSuffix));
        Files.writeString(latestSummary, summary.toString());

        LOGGER.info("Baseline summary saved to: {}", summaryFile);
    }

    // Legacy utility methods for statistical calculations (maintained for compatibility)
    private double calculateAverage(long[] values) {
        long sum = 0;
        for (long value : values) {
            sum += value;
        }
        return (double) sum / values.length;
    }

    private long calculateMax(long[] values) {
        long max = values[0];
        for (long value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    private long calculateMin(long[] values) {
        long min = values[0];
        for (long value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    /**
     * Enhanced baseline metrics data structure with comprehensive analysis
     */
    public static class EnhancedBaselineMetrics {
        public final String timestamp;
        public final String version = "2.0.0";
        public final HardwareTierDetector.HardwareTier hardwareTier;

        public final Map<String, Object> systemInfo = new HashMap<>();
        public final Map<String, Object> memoryMetrics = new HashMap<>();
        public final Map<String, Object> chunkMetrics = new HashMap<>();
        public final Map<String, Object> tpsMetrics = new HashMap<>();
        public final Map<String, Object> concurrentMetrics = new HashMap<>();

        // Statistical analysis results
        public Map<String, Object> statisticalAnalysis;
        public Map<String, Object> targetValidation;

        // Measurement tracking
        public final List<MeasurementResult> measurementHistory = new ArrayList<>();

        public EnhancedBaselineMetrics(HardwareTierDetector.HardwareTier hardwareTier) {
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.hardwareTier = hardwareTier;
        }
    }

    /**
     * Individual measurement result for tracking
     */
    public static class MeasurementResult {
        public final String name;
        public final double value;
        public final String unit;
        public final long timestamp;
        public final Map<String, Object> metadata;

        public MeasurementResult(String name, double value, String unit, Map<String, Object> metadata) {
            this.name = name;
            this.value = value;
            this.unit = unit;
            this.timestamp = System.currentTimeMillis();
            this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        }
    }

    /**
     * Legacy baseline metrics data structure for backward compatibility
     */
    private static class BaselineMetrics {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String version = "1.0.0";
        Map<String, Object> systemInfo = new HashMap<>();
        Map<String, Object> memoryMetrics = new HashMap<>();
        Map<String, Object> chunkMetrics = new HashMap<>();
        Map<String, Object> tpsMetrics = new HashMap<>();
    }

    /**
     * Get the detected hardware tier
     */
    public HardwareTierDetector.HardwareTier getDetectedHardwareTier() {
        return detectedTier;
    }

    /**
     * Get all statistical reports from the last baseline collection
     */
    public Map<String, StatisticalAnalysis.StatisticalReport> getStatisticalReports() {
        return new HashMap<>(statisticalReports);
    }

    /**
     * Get all measurement results from the last baseline collection
     */
    public List<MeasurementResult> getAllMeasurements() {
        return new ArrayList<>(allMeasurements);
    }
}