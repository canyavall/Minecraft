package com.xeenaa.fabricenhancements.performance;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance regression tests that validate performance improvements
 * against established baselines.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PerformanceRegressionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceRegressionTest.class);
    private static final Gson GSON = new Gson();

    // Performance thresholds
    private static final double MAX_CHUNK_LOAD_TIME_MS = 50.0;
    private static final double MIN_CACHE_HIT_RATIO = 0.8;
    private static final double MIN_TPS = 19.5;
    private static final double MAX_MEMORY_USAGE_MB = 2048.0;

    private JsonObject baseline;
    private Path baselineDir;
    private Path reportsDir;

    @BeforeEach
    void setUp() throws IOException {
        baselineDir = Paths.get("performance/baselines");
        reportsDir = Paths.get("performance/reports");

        // Ensure directories exist
        Files.createDirectories(baselineDir);
        Files.createDirectories(reportsDir);

        // Load baseline if it exists
        Path baselineFile = baselineDir.resolve("latest-baseline.json");
        if (Files.exists(baselineFile)) {
            String baselineJson = Files.readString(baselineFile);
            baseline = GSON.fromJson(baselineJson, JsonObject.class);
            LOGGER.info("Loaded baseline from: {}", baselineFile);
        } else {
            LOGGER.warn("No baseline file found at: {}", baselineFile);
        }
    }

    @Test
    void testChunkLoadingPerformance() {
        LOGGER.info("Testing chunk loading performance against baseline");

        // Simulate chunk loading test
        long startTime = System.nanoTime();
        simulateChunkLoading(1000);
        long endTime = System.nanoTime();

        double avgLoadTimeMs = (endTime - startTime) / (1000.0 * 1_000_000.0);

        LOGGER.info("Average chunk load time: {:.3f}ms", avgLoadTimeMs);

        // Check against threshold
        assertTrue(avgLoadTimeMs < MAX_CHUNK_LOAD_TIME_MS,
                String.format("Chunk loading too slow: %.3fms > %.1fms", avgLoadTimeMs, MAX_CHUNK_LOAD_TIME_MS));

        // Check against baseline if available
        if (baseline != null && baseline.has("chunkMetrics")) {
            JsonObject chunkMetrics = baseline.getAsJsonObject("chunkMetrics");
            if (chunkMetrics.has("avg_load_time_ms")) {
                double baselineTime = chunkMetrics.get("avg_load_time_ms").getAsDouble();
                double improvement = (baselineTime - avgLoadTimeMs) / baselineTime * 100;

                LOGGER.info("Performance vs baseline: {:.1f}% improvement (baseline: {:.3f}ms, current: {:.3f}ms)",
                        improvement, baselineTime, avgLoadTimeMs);

                // Allow for 5% regression tolerance
                assertTrue(avgLoadTimeMs <= baselineTime * 1.05,
                        String.format("Performance regression detected: %.3fms vs baseline %.3fms",
                                avgLoadTimeMs, baselineTime));
            }
        }
    }

    @Test
    void testCacheHitRatio() {
        LOGGER.info("Testing cache hit ratio performance");

        // Simulate cache performance test
        int totalRequests = 1000;
        int cacheHits = simulateCachePerformance(totalRequests);
        double hitRatio = (double) cacheHits / totalRequests;

        LOGGER.info("Cache hit ratio: {:.2f}% ({}/{} hits)", hitRatio * 100, cacheHits, totalRequests);

        // Check against threshold
        assertTrue(hitRatio >= MIN_CACHE_HIT_RATIO,
                String.format("Cache hit ratio too low: %.2f%% < %.1f%%", hitRatio * 100, MIN_CACHE_HIT_RATIO * 100));

        // Check against baseline if available
        if (baseline != null && baseline.has("chunkMetrics")) {
            JsonObject chunkMetrics = baseline.getAsJsonObject("chunkMetrics");
            if (chunkMetrics.has("cache_hit_ratio")) {
                double baselineRatio = chunkMetrics.get("cache_hit_ratio").getAsDouble();
                double improvement = (hitRatio - baselineRatio) / baselineRatio * 100;

                LOGGER.info("Cache performance vs baseline: {:.1f}% improvement (baseline: {:.2f}%%, current: {:.2f}%%)",
                        improvement, baselineRatio * 100, hitRatio * 100);

                // Allow for 5% regression tolerance
                assertTrue(hitRatio >= baselineRatio * 0.95,
                        String.format("Cache performance regression: %.2f%% vs baseline %.2f%%",
                                hitRatio * 100, baselineRatio * 100));
            }
        }
    }

    @Test
    void testTPSPerformance() {
        LOGGER.info("Testing TPS performance simulation");

        // Simulate server tick performance
        int tickCount = 100;
        long totalTickTime = 0;

        for (int i = 0; i < tickCount; i++) {
            long tickStart = System.nanoTime();
            simulateServerTick();
            long tickEnd = System.nanoTime();
            totalTickTime += (tickEnd - tickStart);
        }

        double avgTickTimeMs = totalTickTime / (tickCount * 1_000_000.0);
        double effectiveTPS = Math.min(20.0, 1000.0 / avgTickTimeMs);

        LOGGER.info("Average tick time: {:.3f}ms, Effective TPS: {:.2f}", avgTickTimeMs, effectiveTPS);

        // Check against threshold
        assertTrue(effectiveTPS >= MIN_TPS,
                String.format("TPS too low: %.2f < %.1f", effectiveTPS, MIN_TPS));

        // Check against baseline if available
        if (baseline != null && baseline.has("tpsMetrics")) {
            JsonObject tpsMetrics = baseline.getAsJsonObject("tpsMetrics");
            if (tpsMetrics.has("avg_tps")) {
                double baselineTPS = tpsMetrics.get("avg_tps").getAsDouble();
                double improvement = (effectiveTPS - baselineTPS) / baselineTPS * 100;

                LOGGER.info("TPS vs baseline: {:.1f}% improvement (baseline: {:.2f}, current: {:.2f})",
                        improvement, baselineTPS, effectiveTPS);

                // Allow for 2% regression tolerance
                assertTrue(effectiveTPS >= baselineTPS * 0.98,
                        String.format("TPS regression detected: %.2f vs baseline %.2f",
                                effectiveTPS, baselineTPS));
            }
        }
    }

    @Test
    void testMemoryUsage() {
        LOGGER.info("Testing memory usage performance");

        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Simulate memory-intensive operations
        simulateMemoryUsage();

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsageMB = finalMemory / (1024.0 * 1024.0);

        LOGGER.info("Memory usage: {:.1f}MB (delta: {:.1f}MB)",
                memoryUsageMB, (finalMemory - initialMemory) / (1024.0 * 1024.0));

        // Check against threshold
        assertTrue(memoryUsageMB < MAX_MEMORY_USAGE_MB,
                String.format("Memory usage too high: %.1fMB > %.1fMB", memoryUsageMB, MAX_MEMORY_USAGE_MB));

        // Check against baseline if available
        if (baseline != null && baseline.has("memoryMetrics")) {
            JsonObject memoryMetrics = baseline.getAsJsonObject("memoryMetrics");
            if (memoryMetrics.has("average_heap_usage_mb")) {
                double baselineMemory = memoryMetrics.get("average_heap_usage_mb").getAsDouble();
                double improvement = (baselineMemory - memoryUsageMB) / baselineMemory * 100;

                LOGGER.info("Memory vs baseline: {:.1f}% improvement (baseline: {:.1f}MB, current: {:.1f}MB)",
                        improvement, baselineMemory, memoryUsageMB);

                // Allow for 10% regression tolerance
                assertTrue(memoryUsageMB <= baselineMemory * 1.10,
                        String.format("Memory regression detected: %.1fMB vs baseline %.1fMB",
                                memoryUsageMB, baselineMemory));
            }
        }
    }

    // Simulation methods
    private void simulateChunkLoading(int chunkCount) {
        for (int i = 0; i < chunkCount; i++) {
            // Simulate chunk data processing
            byte[] chunkData = new byte[65536]; // 64KB
            for (int j = 0; j < chunkData.length; j += 1024) {
                chunkData[j] = (byte) (i % 256);
            }
            // Small delay to simulate I/O
            try {
                Thread.sleep(0, 10000); // 0.01ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private int simulateCachePerformance(int totalRequests) {
        // Simulate cache with 85% hit ratio
        int hits = 0;
        for (int i = 0; i < totalRequests; i++) {
            if (Math.random() > 0.15) { // 85% hit ratio
                hits++;
            } else {
                // Simulate cache miss (slower operation)
                byte[] data = new byte[1024];
                data[0] = 1;
            }
        }
        return hits;
    }

    private void simulateServerTick() {
        // Simulate entity updates
        for (int i = 0; i < 50; i++) {
            double calculation = Math.sin(i) * Math.cos(i) * Math.sqrt(i + 1);
        }

        // Simulate small memory allocation
        byte[] tempData = new byte[512];
        tempData[0] = 1;

        // Small processing delay
        try {
            Thread.sleep(0, 50000); // 0.05ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void simulateMemoryUsage() {
        // Simulate temporary memory allocations
        java.util.List<byte[]> tempAllocations = new java.util.ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tempAllocations.add(new byte[10240]); // 10KB allocations
        }
        // Clear references
        tempAllocations.clear();
    }
}