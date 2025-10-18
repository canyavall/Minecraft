package com.xeenaa.fabricenhancements.performance;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple performance profiler for measuring key metrics
 */
public class SimplePerformanceProfiler {
    private static final Logger LOGGER = LoggerFactory.getLogger("PerformanceProfiler");
    private static final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    // Performance counters
    private static final AtomicLong chunkLoadsTotal = new AtomicLong(0);
    private static final AtomicLong chunkLoadTimeTotal = new AtomicLong(0);
    private static final ConcurrentHashMap<String, Long> measurements = new ConcurrentHashMap<>();

    // Timers for different operations
    private static final Timer chunkLoadTimer = Timer.builder("chunk_load_time")
            .description("Time taken to load chunks")
            .register(meterRegistry);

    private static final Timer worldGenTimer = Timer.builder("world_gen_time")
            .description("Time taken for world generation")
            .register(meterRegistry);

    /**
     * Start timing an operation
     */
    public static Timer.Sample startTiming(String operation) {
        return Timer.start(meterRegistry);
    }

    /**
     * Record chunk load timing
     */
    public static void recordChunkLoad(long durationNanos) {
        chunkLoadsTotal.incrementAndGet();
        chunkLoadTimeTotal.addAndGet(durationNanos);
        chunkLoadTimer.record(durationNanos, java.util.concurrent.TimeUnit.NANOSECONDS);

        LOGGER.debug("Chunk load took {} ns", durationNanos);
    }

    /**
     * Record world generation timing
     */
    public static void recordWorldGeneration(long durationNanos) {
        worldGenTimer.record(durationNanos, java.util.concurrent.TimeUnit.NANOSECONDS);
        LOGGER.debug("World generation took {} ns", durationNanos);
    }

    /**
     * Record a custom measurement
     */
    public static void recordMeasurement(String metric, long value) {
        measurements.put(metric + "_" + System.currentTimeMillis(), value);
        LOGGER.debug("Recorded {}: {}", metric, value);
    }

    /**
     * Get performance statistics
     */
    public static PerformanceStats getStats() {
        long totalChunks = chunkLoadsTotal.get();
        long totalTime = chunkLoadTimeTotal.get();

        return new PerformanceStats(
            totalChunks,
            totalTime,
            totalChunks > 0 ? totalTime / totalChunks : 0,
            measurements.size()
        );
    }

    /**
     * Print performance report
     */
    public static void printReport() {
        PerformanceStats stats = getStats();

        LOGGER.info("=== Performance Report ===");
        LOGGER.info("Total chunks loaded: {}", stats.totalChunks);
        LOGGER.info("Total load time: {} ms", stats.totalLoadTime / 1_000_000);
        LOGGER.info("Average load time per chunk: {} ms", stats.averageLoadTime / 1_000_000);
        LOGGER.info("Total measurements: {}", stats.totalMeasurements);
        LOGGER.info("========================");
    }

    /**
     * Reset all counters
     */
    public static void reset() {
        chunkLoadsTotal.set(0);
        chunkLoadTimeTotal.set(0);
        measurements.clear();
        LOGGER.info("Performance counters reset");
    }

    /**
     * Performance statistics container
     */
    public static class PerformanceStats {
        public final long totalChunks;
        public final long totalLoadTime;
        public final long averageLoadTime;
        public final int totalMeasurements;

        public PerformanceStats(long totalChunks, long totalLoadTime, long averageLoadTime, int totalMeasurements) {
            this.totalChunks = totalChunks;
            this.totalLoadTime = totalLoadTime;
            this.averageLoadTime = averageLoadTime;
            this.totalMeasurements = totalMeasurements;
        }
    }
}