package com.xeenaa.fabricenhancements.performance.metrics;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import io.micrometer.core.instrument.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Enhanced metrics collector for chunk loading and management performance.
 *
 * Features:
 * - High-resolution chunk load time tracking
 * - Cache hit/miss ratio monitoring
 * - Slow chunk load detection and alerting
 * - Chunk generation time tracking
 * - Historical performance analysis
 * - Spark profiler integration support
 *
 * This collector provides comprehensive chunk performance monitoring
 * with minimal overhead, designed for production use.
 */
public class ChunkMetrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkMetrics.class);

    private final PerformanceConfig config;
    private final MeterRegistry meterRegistry;

    // Metrics
    private final Timer chunkLoadTimer;
    private final Counter chunkLoadCounter;
    private final Counter chunkCacheHitCounter;
    private final Counter chunkCacheMissCounter;
    private final Gauge loadedChunksGauge;
    private final DistributionSummary chunkLoadTimeDistribution;
    private final Timer chunkGenerationTimer;
    private final Counter slowChunkLoadCounter;
    private final Gauge avgChunkLoadTimeGauge;
    private final Gauge cacheHitRatioGauge;

    // Internal tracking
    private final ConcurrentHashMap<ChunkPos, Long> chunkLoadTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ChunkPos, ChunkLoadEvent> chunkLoadHistory = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<ChunkLoadEvent> recentSlowLoads = new ConcurrentLinkedQueue<>();
    private final LongAdder totalChunkLoads = new LongAdder();
    private final LongAdder cacheHits = new LongAdder();
    private final LongAdder cacheMisses = new LongAdder();
    private final LongAdder slowChunkLoads = new LongAdder();
    private final DoubleAdder totalLoadTime = new DoubleAdder();
    private final AtomicLong loadedChunkCount = new AtomicLong();

    // Performance thresholds for Spark integration
    private static final double SLOW_CHUNK_THRESHOLD_MS = 50.0;
    private static final double VERY_SLOW_CHUNK_THRESHOLD_MS = 200.0;
    private static final int MAX_SLOW_LOAD_HISTORY = 100;

    // Baseline collection
    private boolean collectingBaseline = false;
    private final LongAdder baselineChunkLoads = new LongAdder();
    private final DoubleAdder baselineLoadTime = new DoubleAdder();

    public ChunkMetrics(PerformanceConfig config) {
        this(config, null);
    }

    public ChunkMetrics(PerformanceConfig config, MeterRegistry meterRegistry) {
        this.config = config;
        this.meterRegistry = meterRegistry != null ? meterRegistry : Metrics.globalRegistry;

        // Initialize metrics
        this.chunkLoadTimer = Timer.builder("xeenaa.chunk.load_time")
                .description("Time taken to load chunks")
                .register(this.meterRegistry);

        this.chunkLoadCounter = Counter.builder("xeenaa.chunk.loads_total")
                .description("Total number of chunk loads")
                .register(this.meterRegistry);

        this.chunkCacheHitCounter = Counter.builder("xeenaa.chunk.cache_hits_total")
                .description("Total number of chunk cache hits")
                .register(this.meterRegistry);

        this.chunkCacheMissCounter = Counter.builder("xeenaa.chunk.cache_misses_total")
                .description("Total number of chunk cache misses")
                .register(this.meterRegistry);

        this.loadedChunksGauge = Gauge.builder("xeenaa.chunk.loaded_count")
                .description("Number of currently loaded chunks")
                .register(this.meterRegistry, this, ChunkMetrics::getLoadedChunkCountValue);

        this.chunkLoadTimeDistribution = DistributionSummary.builder("xeenaa.chunk.load_time_distribution")
                .description("Distribution of chunk load times")
                .baseUnit("milliseconds")
                .register(this.meterRegistry);

        this.chunkGenerationTimer = Timer.builder("xeenaa.chunk.generation_time")
                .description("Time taken to generate new chunks")
                .register(this.meterRegistry);

        this.slowChunkLoadCounter = Counter.builder("xeenaa.chunk.slow_loads_total")
                .description("Total number of slow chunk loads (>50ms)")
                .register(this.meterRegistry);

        this.avgChunkLoadTimeGauge = Gauge.builder("xeenaa.chunk.average_load_time")
                .description("Average chunk load time in milliseconds")
                .register(this.meterRegistry, this, ChunkMetrics::getAverageLoadTime);

        this.cacheHitRatioGauge = Gauge.builder("xeenaa.chunk.cache_hit_ratio")
                .description("Chunk cache hit ratio")
                .register(this.meterRegistry, this, ChunkMetrics::getCacheHitRatio);
    }

    /**
     * Collect chunk metrics from the server
     */
    public void collect(MinecraftServer server) {
        if (server == null) return;

        try {
            long totalLoaded = 0;

            // Count loaded chunks across all worlds
            for (ServerWorld world : server.getWorlds()) {
                int worldChunks = countLoadedChunks(world);
                totalLoaded += worldChunks;

                // Register world-specific gauge
                Gauge.builder("xeenaa.chunk.loaded_count_by_world")
                        .description("Loaded chunks by world")
                        .tag("world", world.getRegistryKey().getValue().toString())
                        .register(meterRegistry, () -> (double) countLoadedChunks(world));
            }

            loadedChunkCount.set(totalLoaded);

        } catch (Exception e) {
            LOGGER.warn("Error collecting chunk metrics", e);
        }
    }

    /**
     * Count loaded chunks in a world
     */
    private int countLoadedChunks(ServerWorld world) {
        try {
            return world.getChunkManager().getTotalChunksLoadedCount();
        } catch (Exception e) {
            // Fallback counting method
            return 0;
        }
    }

    /**
     * Record a chunk loading event
     */
    public void recordChunkLoad(ChunkPos pos, long startTimeNanos, boolean cacheHit) {
        long loadTimeNanos = System.nanoTime() - startTimeNanos;
        double loadTimeMs = loadTimeNanos / 1_000_000.0;

        // Record metrics
        chunkLoadTimer.record(loadTimeNanos, java.util.concurrent.TimeUnit.NANOSECONDS);
        chunkLoadCounter.increment();
        chunkLoadTimeDistribution.record(loadTimeMs);

        // Record cache performance
        if (cacheHit) {
            chunkCacheHitCounter.increment();
            cacheHits.increment();
        } else {
            chunkCacheMissCounter.increment();
            cacheMisses.increment();
        }

        // Update internal tracking
        chunkLoadTimes.put(pos, loadTimeNanos);
        totalChunkLoads.increment();
        totalLoadTime.add(loadTimeMs);

        // Baseline collection
        if (collectingBaseline) {
            baselineChunkLoads.increment();
            baselineLoadTime.add(loadTimeMs);
        }

        // Track and log slow chunk loads with enhanced details
        if (loadTimeMs > SLOW_CHUNK_THRESHOLD_MS) {
            slowChunkLoads.increment();
            slowChunkLoadCounter.increment();

            ChunkLoadEvent slowLoadEvent = new ChunkLoadEvent(pos, loadTimeMs, cacheHit, System.currentTimeMillis());

            // Keep recent slow loads for analysis
            recentSlowLoads.offer(slowLoadEvent);
            if (recentSlowLoads.size() > MAX_SLOW_LOAD_HISTORY) {
                recentSlowLoads.poll();
            }

            if (loadTimeMs > VERY_SLOW_CHUNK_THRESHOLD_MS) {
                LOGGER.warn("Very slow chunk load: {} took {:.2f}ms (cache hit: {}) - potential performance issue",
                           pos, loadTimeMs, cacheHit);
            } else {
                LOGGER.debug("Slow chunk load: {} took {:.2f}ms (cache hit: {})",
                           pos, loadTimeMs, cacheHit);
            }
        }

        // Store detailed load event for analysis
        chunkLoadHistory.put(pos, new ChunkLoadEvent(pos, loadTimeMs, cacheHit, System.currentTimeMillis()));
    }

    /**
     * Record chunk unload event
     */
    public void recordChunkUnload(ChunkPos pos) {
        chunkLoadTimes.remove(pos);
        chunkLoadHistory.remove(pos);

        Counter.builder("xeenaa.chunk.unloads_total")
                .description("Total number of chunk unloads")
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record chunk generation event for new chunks
     */
    public void recordChunkGeneration(ChunkPos pos, long generationTimeNanos) {
        double generationTimeMs = generationTimeNanos / 1_000_000.0;
        chunkGenerationTimer.record(generationTimeNanos, java.util.concurrent.TimeUnit.NANOSECONDS);

        if (generationTimeMs > 500) { // Log very slow chunk generation
            LOGGER.warn("Slow chunk generation: {} took {:.2f}ms", pos, generationTimeMs);
        }
    }

    /**
     * Get current cache hit ratio
     */
    public double getCacheHitRatio() {
        long totalRequests = cacheHits.sum() + cacheMisses.sum();
        if (totalRequests == 0) return 0.0;
        return (double) cacheHits.sum() / totalRequests;
    }

    /**
     * Get average chunk load time in milliseconds
     */
    public double getAverageLoadTime() {
        long loads = totalChunkLoads.sum();
        if (loads == 0) return 0.0;
        return totalLoadTime.sum() / loads;
    }

    /**
     * Get total number of chunk loads
     */
    public long getTotalChunkLoads() {
        return totalChunkLoads.sum();
    }

    /**
     * Get total number of slow chunk loads
     */
    public long getSlowChunkLoads() {
        return slowChunkLoads.sum();
    }

    /**
     * Get percentage of slow chunk loads
     */
    public double getSlowChunkLoadPercentage() {
        long total = getTotalChunkLoads();
        if (total == 0) return 0.0;
        return ((double) getSlowChunkLoads() / total) * 100.0;
    }

    /**
     * Get recent slow chunk loads for analysis
     */
    public List<ChunkLoadEvent> getRecentSlowLoads() {
        return new ArrayList<>(recentSlowLoads);
    }

    /**
     * Get chunk load statistics for a specific time window
     */
    public ChunkLoadStatistics getStatistics(int minutesBack) {
        long cutoffTime = System.currentTimeMillis() - (minutesBack * 60 * 1000);

        List<ChunkLoadEvent> recentEvents = chunkLoadHistory.values().stream()
                .filter(event -> event.timestamp > cutoffTime)
                .toList();

        if (recentEvents.isEmpty()) {
            return new ChunkLoadStatistics(0, 0.0, 0.0, 0.0, 0);
        }

        double totalTime = recentEvents.stream().mapToDouble(e -> e.loadTimeMs).sum();
        double avgTime = totalTime / recentEvents.size();
        double maxTime = recentEvents.stream().mapToDouble(e -> e.loadTimeMs).max().orElse(0.0);
        double minTime = recentEvents.stream().mapToDouble(e -> e.loadTimeMs).min().orElse(0.0);
        long cacheHitCount = recentEvents.stream().mapToLong(e -> e.cacheHit ? 1 : 0).sum();
        double hitRatio = (double) cacheHitCount / recentEvents.size();

        return new ChunkLoadStatistics(recentEvents.size(), avgTime, maxTime, minTime, hitRatio);
    }

    /**
     * Get currently loaded chunk count
     */
    public long getLoadedChunkCount() {
        return loadedChunkCount.get();
    }

    /**
     * Get loaded chunk count as double for Gauge
     */
    private double getLoadedChunkCountValue() {
        return loadedChunkCount.get();
    }

    /**
     * Start baseline collection
     */
    public void startBaselineCollection() {
        collectingBaseline = true;
        baselineChunkLoads.reset();
        baselineLoadTime.reset();
        LOGGER.info("Started chunk metrics baseline collection");
    }

    /**
     * Stop baseline collection and save results
     */
    public void stopBaselineCollection() {
        if (!collectingBaseline) return;

        collectingBaseline = false;

        try {
            // Calculate baseline metrics
            long loads = baselineChunkLoads.sum();
            double avgLoadTime = loads > 0 ? baselineLoadTime.sum() / loads : 0.0;
            double hitRatio = getCacheHitRatio();

            // Save baseline to file
            saveBaseline(loads, avgLoadTime, hitRatio);

            LOGGER.info("Chunk metrics baseline collection completed: {} loads, {:.2f}ms avg, {:.2f}% hit ratio",
                       loads, avgLoadTime, hitRatio * 100);

        } catch (Exception e) {
            LOGGER.error("Error saving chunk metrics baseline", e);
        }
    }

    /**
     * Save baseline metrics to file
     */
    private void saveBaseline(long loads, double avgLoadTime, double hitRatio) throws IOException {
        Path baselineDir = Paths.get("performance/baselines");
        Files.createDirectories(baselineDir);

        Path baselineFile = baselineDir.resolve("chunk-metrics-baseline.json");
        String baseline = String.format(
                "{\n" +
                "  \"timestamp\": %d,\n" +
                "  \"chunk_loads\": %d,\n" +
                "  \"average_load_time_ms\": %.3f,\n" +
                "  \"cache_hit_ratio\": %.3f\n" +
                "}",
                System.currentTimeMillis(),
                loads,
                avgLoadTime,
                hitRatio
        );

        Files.writeString(baselineFile, baseline);
    }

    /**
     * Generate chunk metrics report
     */
    public ChunkMetricsReport getReport() {
        return new ChunkMetricsReport(
                getTotalChunkLoads(),
                getLoadedChunkCount(),
                getAverageLoadTime(),
                getCacheHitRatio(),
                chunkLoadTimes.size(),
                getSlowChunkLoads(),
                getSlowChunkLoadPercentage()
        );
    }

    /**
     * Get detailed performance summary for Spark integration
     */
    public String getPerformanceSummary() {
        ChunkLoadStatistics recent1min = getStatistics(1);
        ChunkLoadStatistics recent5min = getStatistics(5);

        return String.format(
                "Chunk Performance Summary:\n" +
                "Total Loads: %d | Slow Loads: %d (%.2f%%)\n" +
                "Cache Hit Ratio: %.2f%% | Avg Load Time: %.2fms\n" +
                "Last 1min: %d loads, %.2fms avg\n" +
                "Last 5min: %d loads, %.2fms avg\n" +
                "Currently Loaded: %d chunks",
                getTotalChunkLoads(),
                getSlowChunkLoads(),
                getSlowChunkLoadPercentage(),
                getCacheHitRatio() * 100,
                getAverageLoadTime(),
                recent1min.loadCount,
                recent1min.avgLoadTime,
                recent5min.loadCount,
                recent5min.avgLoadTime,
                getLoadedChunkCount()
        );
    }

    /**
     * Chunk load event data class
     */
    public static class ChunkLoadEvent {
        public final ChunkPos pos;
        public final double loadTimeMs;
        public final boolean cacheHit;
        public final long timestamp;

        public ChunkLoadEvent(ChunkPos pos, double loadTimeMs, boolean cacheHit, long timestamp) {
            this.pos = pos;
            this.loadTimeMs = loadTimeMs;
            this.cacheHit = cacheHit;
            this.timestamp = timestamp;
        }
    }

    /**
     * Chunk load statistics data class
     */
    public static class ChunkLoadStatistics {
        public final int loadCount;
        public final double avgLoadTime;
        public final double maxLoadTime;
        public final double minLoadTime;
        public final double cacheHitRatio;

        public ChunkLoadStatistics(int loadCount, double avgLoadTime, double maxLoadTime,
                                 double minLoadTime, double cacheHitRatio) {
            this.loadCount = loadCount;
            this.avgLoadTime = avgLoadTime;
            this.maxLoadTime = maxLoadTime;
            this.minLoadTime = minLoadTime;
            this.cacheHitRatio = cacheHitRatio;
        }
    }

    /**
     * Enhanced chunk metrics report data class
     */
    public static class ChunkMetricsReport {
        public final long totalLoads;
        public final long currentlyLoaded;
        public final double averageLoadTimeMs;
        public final double cacheHitRatio;
        public final int trackedChunks;
        public final long slowLoads;
        public final double slowLoadPercentage;

        public ChunkMetricsReport(long totalLoads, long currentlyLoaded, double averageLoadTimeMs,
                                 double cacheHitRatio, int trackedChunks, long slowLoads, double slowLoadPercentage) {
            this.totalLoads = totalLoads;
            this.currentlyLoaded = currentlyLoaded;
            this.averageLoadTimeMs = averageLoadTimeMs;
            this.cacheHitRatio = cacheHitRatio;
            this.trackedChunks = trackedChunks;
            this.slowLoads = slowLoads;
            this.slowLoadPercentage = slowLoadPercentage;
        }

        @Override
        public String toString() {
            return String.format(
                    "ChunkMetrics{loads=%d, loaded=%d, avgTime=%.2fms, hitRatio=%.2f%%, tracked=%d, slow=%d (%.2f%%)}",
                    totalLoads, currentlyLoaded, averageLoadTimeMs, cacheHitRatio * 100, trackedChunks, slowLoads, slowLoadPercentage
            );
        }
    }
}