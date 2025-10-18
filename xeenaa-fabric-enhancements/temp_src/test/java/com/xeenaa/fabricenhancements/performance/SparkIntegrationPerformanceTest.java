package com.xeenaa.fabricenhancements.performance;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.profiling.SparkIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance validation tests for Spark Profiler Integration.
 *
 * These tests ensure that our Spark integration meets the requirement
 * of having less than 1% performance impact on server operations.
 *
 * Tests include:
 * - Baseline performance measurement
 * - Spark integration overhead validation
 * - Memory usage impact assessment
 * - TPS monitoring overhead evaluation
 * - Chunk metrics collection performance
 */
public class SparkIntegrationPerformanceTest {

    private PerformanceConfig config;
    private SparkIntegration sparkIntegration;
    private PerformanceManager performanceManager;

    // Performance thresholds
    private static final double MAX_OVERHEAD_PERCENT = 1.0;
    private static final long MAX_MEMORY_OVERHEAD_MB = 50; // 50MB max overhead
    private static final int PERFORMANCE_TEST_ITERATIONS = 10000;
    private static final int WARM_UP_ITERATIONS = 1000;

    @BeforeEach
    void setUp() {
        config = PerformanceConfig.getDevelopmentConfig();
        config.maxPerformanceOverheadPercent = MAX_OVERHEAD_PERCENT;
        config.enablePerformanceValidation = true;

        performanceManager = new PerformanceManager(config);
        sparkIntegration = new SparkIntegration();
    }

    @Test
    @DisplayName("Baseline Performance Measurement")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testBaselinePerformance() {
        // Test chunk load simulation without Spark integration
        long startTime = System.nanoTime();

        for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS; i++) {
            simulateChunkLoad(i % 100, i % 100, false);
        }

        long baselineTime = System.nanoTime() - startTime;
        double baselineMs = baselineTime / 1_000_000.0;

        System.out.printf("Baseline performance: %.3f ms for %d operations%n",
                         baselineMs, PERFORMANCE_TEST_ITERATIONS);

        // Baseline should complete within reasonable time
        assertTrue(baselineMs < 1000, "Baseline performance too slow: " + baselineMs + "ms");
    }

    @Test
    @DisplayName("Spark Integration Overhead Validation")
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testSparkIntegrationOverhead() {
        // Measure baseline performance
        long baselineTime = measureBaselinePerformance();

        // Measure performance with Spark integration
        long sparkTime = measureSparkIntegrationPerformance();

        // Calculate overhead percentage
        double overheadPercent = ((double) (sparkTime - baselineTime) / baselineTime) * 100.0;

        System.out.printf("Performance overhead: %.2f%% (%.3f ms vs %.3f ms)%n",
                         overheadPercent,
                         sparkTime / 1_000_000.0,
                         baselineTime / 1_000_000.0);

        // Validate overhead is within acceptable limits
        assertTrue(overheadPercent <= MAX_OVERHEAD_PERCENT,
                  String.format("Spark integration overhead %.2f%% exceeds limit %.2f%%",
                               overheadPercent, MAX_OVERHEAD_PERCENT));
    }

    @Test
    @DisplayName("Memory Impact Assessment")
    @Timeout(value = 45, unit = TimeUnit.SECONDS)
    void testMemoryImpact() {
        // Force garbage collection for accurate measurement
        System.gc();
        Thread.yield();

        long baselineMemory = getUsedMemoryMB();

        // Initialize Spark integration
        try {
            sparkIntegration.initialize(null); // Mock server for testing

            // Simulate operations to trigger memory usage
            for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS; i++) {
                sparkIntegration.recordChunkLoadEvent(i % 100, i % 100,
                                                    (long) (Math.random() * 50_000_000)); // 0-50ms
                sparkIntegration.recordCacheHit(Math.random() < 0.8);
            }

            // Force garbage collection again
            System.gc();
            Thread.yield();

            long sparkMemory = getUsedMemoryMB();
            long memoryOverhead = sparkMemory - baselineMemory;

            System.out.printf("Memory overhead: %d MB (%d MB vs %d MB)%n",
                             memoryOverhead, sparkMemory, baselineMemory);

            // Validate memory overhead is within acceptable limits
            assertTrue(memoryOverhead <= MAX_MEMORY_OVERHEAD_MB,
                      String.format("Memory overhead %d MB exceeds limit %d MB",
                                   memoryOverhead, MAX_MEMORY_OVERHEAD_MB));

        } finally {
            sparkIntegration.shutdown();
        }
    }

    @Test
    @DisplayName("TPS Monitoring Performance")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testTPSMonitoringPerformance() {
        try {
            sparkIntegration.initialize(null);

            // Warm up
            for (int i = 0; i < WARM_UP_ITERATIONS; i++) {
                sparkIntegration.getCurrentTPS();
            }

            // Measure TPS calculation performance
            long startTime = System.nanoTime();

            for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS; i++) {
                double tps = sparkIntegration.getCurrentTPS();
                assertNotNull(tps);
                assertTrue(tps >= 0.0 && tps <= 20.0);
            }

            long endTime = System.nanoTime();
            double avgTimePerCall = (endTime - startTime) / (double) PERFORMANCE_TEST_ITERATIONS / 1000.0; // microseconds

            System.out.printf("TPS monitoring: %.3f μs per call%n", avgTimePerCall);

            // TPS calculation should be very fast (less than 10 microseconds per call)
            assertTrue(avgTimePerCall < 10.0,
                      "TPS monitoring too slow: " + avgTimePerCall + "μs per call");

        } finally {
            sparkIntegration.shutdown();
        }
    }

    @Test
    @DisplayName("Chunk Metrics Collection Performance")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testChunkMetricsPerformance() {
        try {
            sparkIntegration.initialize(null);

            // Measure chunk metrics recording performance
            long startTime = System.nanoTime();

            for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS; i++) {
                int x = i % 1000;
                int z = i % 1000;
                long loadTime = (long) (Math.random() * 100_000_000); // 0-100ms
                boolean cacheHit = Math.random() < 0.8;

                sparkIntegration.recordChunkLoadEvent(x, z, loadTime);
                sparkIntegration.recordCacheHit(cacheHit);
            }

            long endTime = System.nanoTime();
            double avgTimePerRecord = (endTime - startTime) / (double) PERFORMANCE_TEST_ITERATIONS / 1000.0; // microseconds

            System.out.printf("Chunk metrics recording: %.3f μs per operation%n", avgTimePerRecord);

            // Metrics recording should be very fast (less than 5 microseconds per operation)
            assertTrue(avgTimePerRecord < 5.0,
                      "Chunk metrics recording too slow: " + avgTimePerRecord + "μs per operation");

            // Validate metrics accuracy
            assertTrue(sparkIntegration.getTotalChunkLoads() == PERFORMANCE_TEST_ITERATIONS);
            assertTrue(sparkIntegration.getCacheHitRatio() > 0.75 && sparkIntegration.getCacheHitRatio() < 0.85);

        } finally {
            sparkIntegration.shutdown();
        }
    }

    @Test
    @DisplayName("Concurrent Performance Test")
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testConcurrentPerformance() {
        try {
            sparkIntegration.initialize(null);

            int threadCount = Runtime.getRuntime().availableProcessors();
            AtomicLong totalOperations = new AtomicLong(0);
            Thread[] threads = new Thread[threadCount];

            long startTime = System.nanoTime();

            // Start concurrent threads
            for (int t = 0; t < threadCount; t++) {
                final int threadId = t;
                threads[t] = new Thread(() -> {
                    for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS / threadCount; i++) {
                        int x = (threadId * 1000) + (i % 100);
                        int z = (threadId * 1000) + (i % 100);
                        long loadTime = (long) (Math.random() * 50_000_000);

                        sparkIntegration.recordChunkLoadEvent(x, z, loadTime);
                        sparkIntegration.recordCacheHit(Math.random() < 0.8);
                        totalOperations.incrementAndGet();
                    }
                });
                threads[t].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            long endTime = System.nanoTime();
            double totalTimeMs = (endTime - startTime) / 1_000_000.0;
            double operationsPerSecond = (totalOperations.get() * 1000.0) / totalTimeMs;

            System.out.printf("Concurrent performance: %.0f operations/second with %d threads%n",
                             operationsPerSecond, threadCount);

            // Should handle at least 100,000 operations per second
            assertTrue(operationsPerSecond > 100_000,
                      "Concurrent performance too low: " + operationsPerSecond + " ops/sec");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        } finally {
            sparkIntegration.shutdown();
        }
    }

    @Test
    @DisplayName("Long-Running Stability Test")
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void testLongRunningStability() {
        try {
            sparkIntegration.initialize(null);

            long startMemory = getUsedMemoryMB();
            long operationCount = 0;

            // Run for 60 seconds
            long testDuration = 60_000; // 60 seconds
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < testDuration) {
                // Simulate continuous chunk operations
                for (int i = 0; i < 1000; i++) {
                    sparkIntegration.recordChunkLoadEvent(i % 100, i % 100,
                                                        (long) (Math.random() * 50_000_000));
                    sparkIntegration.recordCacheHit(Math.random() < 0.8);
                    operationCount++;
                }

                // Brief pause to simulate realistic load
                Thread.sleep(10);
            }

            long endMemory = getUsedMemoryMB();
            long memoryGrowth = endMemory - startMemory;

            System.out.printf("Stability test: %d operations over 60s, memory growth: %d MB%n",
                             operationCount, memoryGrowth);

            // Memory growth should be minimal (less than 100MB over 60 seconds)
            assertTrue(memoryGrowth < 100,
                      "Excessive memory growth: " + memoryGrowth + " MB");

            // Should have processed a significant number of operations
            assertTrue(operationCount > 100_000,
                      "Too few operations processed: " + operationCount);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        } finally {
            sparkIntegration.shutdown();
        }
    }

    // Helper methods

    private long measureBaselinePerformance() {
        // Warm up
        for (int i = 0; i < WARM_UP_ITERATIONS; i++) {
            simulateChunkLoad(i % 100, i % 100, false);
        }

        long startTime = System.nanoTime();

        for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS; i++) {
            simulateChunkLoad(i % 100, i % 100, false);
        }

        return System.nanoTime() - startTime;
    }

    private long measureSparkIntegrationPerformance() {
        try {
            sparkIntegration.initialize(null);

            // Warm up
            for (int i = 0; i < WARM_UP_ITERATIONS; i++) {
                simulateChunkLoad(i % 100, i % 100, true);
            }

            long startTime = System.nanoTime();

            for (int i = 0; i < PERFORMANCE_TEST_ITERATIONS; i++) {
                simulateChunkLoad(i % 100, i % 100, true);
            }

            return System.nanoTime() - startTime;

        } finally {
            sparkIntegration.shutdown();
        }
    }

    private void simulateChunkLoad(int x, int z, boolean withSpark) {
        long loadTime = (long) (Math.random() * 50_000_000); // 0-50ms
        boolean cacheHit = Math.random() < 0.8;

        if (withSpark) {
            sparkIntegration.recordChunkLoadEvent(x, z, loadTime);
            sparkIntegration.recordCacheHit(cacheHit);
        } else {
            // Simulate equivalent work without Spark
            @SuppressWarnings("unused")
            double dummy = Math.sqrt(x * x + z * z) + loadTime / 1_000_000.0;
        }
    }

    private long getUsedMemoryMB() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }
}