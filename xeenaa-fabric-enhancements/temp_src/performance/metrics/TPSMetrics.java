package com.xeenaa.fabricenhancements.performance.metrics;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import io.micrometer.core.instrument.*;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * Metrics collector for server TPS (Ticks Per Second) performance.
 * Tracks tick times, TPS stability, and performance degradation.
 */
public class TPSMetrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(TPSMetrics.class);
    private static final long TICK_TIME_TARGET = 50_000_000L; // 50ms in nanoseconds (20 TPS)

    private final PerformanceConfig config;
    private final MeterRegistry meterRegistry;

    // Metrics
    private final Timer tickTimer;
    private final Gauge tpsGauge;
    private final Gauge averageTPSGauge;
    private final Counter lagSpikeCounter;
    private final DistributionSummary tickTimeDistribution;

    // TPS tracking
    private final Queue<Long> tickTimes = new ArrayDeque<>();
    private final Queue<Double> tpsHistory = new ArrayDeque<>();
    private long lastTickTime = System.nanoTime();
    private double currentTPS = 20.0;
    private double minTPS = 20.0;
    private double maxTPS = 20.0;
    private long totalTicks = 0;

    // Baseline collection
    private boolean collectingBaseline = false;
    private final DoubleAdder baselineTPS = new DoubleAdder();
    private final DoubleAdder baselineTickTime = new DoubleAdder();
    private int baselineSamples = 0;

    public TPSMetrics(PerformanceConfig config) {
        this(config, null);
    }

    public TPSMetrics(PerformanceConfig config, MeterRegistry meterRegistry) {
        this.config = config;
        this.meterRegistry = meterRegistry != null ? meterRegistry : Metrics.globalRegistry;

        // Initialize metrics
        this.tickTimer = Timer.builder("xeenaa.tps.tick_time")
                .description("Server tick execution time")
                .register(this.meterRegistry);

        this.tpsGauge = Gauge.builder("xeenaa.tps.current")
                .description("Current server TPS")
                .register(this.meterRegistry, this, TPSMetrics::getCurrentTPS);

        this.averageTPSGauge = Gauge.builder("xeenaa.tps.average")
                .description("Average server TPS over time window")
                .register(this.meterRegistry, this, TPSMetrics::getAverageTPS);

        this.lagSpikeCounter = Counter.builder("xeenaa.tps.lag_spikes_total")
                .description("Total number of lag spikes detected")
                .register(this.meterRegistry);

        this.tickTimeDistribution = DistributionSummary.builder("xeenaa.tps.tick_time_distribution")
                .description("Distribution of tick execution times")
                .baseUnit("milliseconds")
                .register(this.meterRegistry);
    }

    /**
     * Called on each server tick to record timing
     */
    public void onServerTick(MinecraftServer server) {
        long currentTime = System.nanoTime();
        long tickDuration = currentTime - lastTickTime;
        lastTickTime = currentTime;

        recordTick(tickDuration);
    }

    /**
     * Record a server tick with its duration
     */
    public void recordTick(long tickTimeNanos) {
        totalTicks++;
        double tickTimeMs = tickTimeNanos / 1_000_000.0;

        // Record tick time
        tickTimer.record(tickTimeNanos, java.util.concurrent.TimeUnit.NANOSECONDS);
        tickTimeDistribution.record(tickTimeMs);

        // Update tick times queue (keep last N ticks for TPS calculation)
        tickTimes.offer(tickTimeNanos);
        if (tickTimes.size() > config.tpsAveragingWindow) {
            tickTimes.poll();
        }

        // Calculate current TPS
        updateTPS();

        // Check for lag spikes
        checkLagSpike(tickTimeMs);

        // Baseline collection
        if (collectingBaseline) {
            baselineTPS.add(currentTPS);
            baselineTickTime.add(tickTimeMs);
            baselineSamples++;
        }
    }

    /**
     * Update TPS calculation based on recent tick times
     */
    private void updateTPS() {
        if (tickTimes.isEmpty()) return;

        // Calculate average tick time over the window
        long totalTickTime = tickTimes.stream().mapToLong(Long::longValue).sum();
        double averageTickTime = totalTickTime / (double) tickTimes.size();

        // Convert to TPS (target is 50ms per tick = 20 TPS)
        currentTPS = Math.min(20.0, 1_000_000_000.0 / averageTickTime);

        // Update TPS history
        tpsHistory.offer(currentTPS);
        if (tpsHistory.size() > config.tpsAveragingWindow) {
            tpsHistory.poll();
        }

        // Update min/max TPS
        if (currentTPS < minTPS) minTPS = currentTPS;
        if (currentTPS > maxTPS) maxTPS = currentTPS;
    }

    /**
     * Check for lag spikes and record them
     */
    private void checkLagSpike(double tickTimeMs) {
        // Consider a lag spike if tick takes more than 150ms (3x normal)
        if (tickTimeMs > 150.0) {
            lagSpikeCounter.increment();
            LOGGER.warn("Lag spike detected: tick took {:.2f}ms (TPS: {:.2f})", tickTimeMs, currentTPS);
        } else if (tickTimeMs > 100.0) {
            LOGGER.debug("Slow tick: {:.2f}ms (TPS: {:.2f})", tickTimeMs, currentTPS);
        }
    }

    /**
     * Collect TPS metrics (called periodically)
     */
    public void collect(MinecraftServer server) {
        // Additional server-specific metrics can be collected here
        if (server != null) {
            // Record server-specific TPS metrics
            Gauge.builder("xeenaa.tps.server_running")
                    .description("Server running status")
                    .register(meterRegistry, server, s -> s.isRunning() ? 1.0 : 0.0);
        }
    }

    /**
     * Get current TPS
     */
    public double getCurrentTPS() {
        return currentTPS;
    }

    /**
     * Get average TPS over the time window
     */
    public double getAverageTPS() {
        if (tpsHistory.isEmpty()) return currentTPS;
        return tpsHistory.stream().mapToDouble(Double::doubleValue).average().orElse(currentTPS);
    }

    /**
     * Get minimum recorded TPS
     */
    public double getMinTPS() {
        return minTPS;
    }

    /**
     * Get maximum recorded TPS
     */
    public double getMaxTPS() {
        return maxTPS;
    }

    /**
     * Get total number of ticks processed
     */
    public long getTotalTicks() {
        return totalTicks;
    }

    /**
     * Check if TPS is below threshold
     */
    public boolean isBelowThreshold() {
        return currentTPS < config.minimumAcceptableTPS;
    }

    /**
     * Get TPS stability (lower values indicate more stable TPS)
     */
    public double getTPSStability() {
        if (tpsHistory.size() < 2) return 0.0;

        double average = getAverageTPS();
        double variance = tpsHistory.stream()
                .mapToDouble(tps -> Math.pow(tps - average, 2))
                .average()
                .orElse(0.0);

        return Math.sqrt(variance); // Standard deviation
    }

    /**
     * Start baseline collection
     */
    public void startBaselineCollection() {
        collectingBaseline = true;
        baselineTPS.reset();
        baselineTickTime.reset();
        baselineSamples = 0;
        LOGGER.info("Started TPS metrics baseline collection");
    }

    /**
     * Stop baseline collection and save results
     */
    public void stopBaselineCollection() {
        if (!collectingBaseline) return;

        collectingBaseline = false;

        try {
            // Calculate baseline metrics
            double avgTPS = baselineSamples > 0 ? baselineTPS.sum() / baselineSamples : 0.0;
            double avgTickTime = baselineSamples > 0 ? baselineTickTime.sum() / baselineSamples : 0.0;
            double stability = getTPSStability();

            // Save baseline to file
            saveBaseline(avgTPS, avgTickTime, stability);

            LOGGER.info("TPS metrics baseline collection completed: {:.2f} avg TPS, {:.2f}ms avg tick time, {:.3f} stability",
                       avgTPS, avgTickTime, stability);

        } catch (Exception e) {
            LOGGER.error("Error saving TPS metrics baseline", e);
        }
    }

    /**
     * Save baseline metrics to file
     */
    private void saveBaseline(double avgTPS, double avgTickTime, double stability) throws IOException {
        Path baselineDir = Paths.get("performance/baselines");
        Files.createDirectories(baselineDir);

        Path baselineFile = baselineDir.resolve("tps-metrics-baseline.json");
        String baseline = String.format(
                "{\n" +
                "  \"timestamp\": %d,\n" +
                "  \"average_tps\": %.3f,\n" +
                "  \"target_tps\": %.1f,\n" +
                "  \"average_tick_time_ms\": %.3f,\n" +
                "  \"target_tick_time_ms\": %.1f,\n" +
                "  \"tps_stability\": %.3f,\n" +
                "  \"min_tps\": %.3f,\n" +
                "  \"max_tps\": %.3f,\n" +
                "  \"total_ticks\": %d,\n" +
                "  \"samples\": %d\n" +
                "}",
                System.currentTimeMillis(),
                avgTPS,
                config.targetTPS,
                avgTickTime,
                50.0, // 50ms target tick time
                stability,
                minTPS,
                maxTPS,
                totalTicks,
                baselineSamples
        );

        Files.writeString(baselineFile, baseline);
    }

    /**
     * Generate TPS metrics report
     */
    public TPSMetricsReport getReport() {
        return new TPSMetricsReport(
                getCurrentTPS(),
                getAverageTPS(),
                getMinTPS(),
                getMaxTPS(),
                getTPSStability(),
                getTotalTicks(),
                isBelowThreshold()
        );
    }

    /**
     * TPS metrics report data class
     */
    public static class TPSMetricsReport {
        public final double currentTPS;
        public final double averageTPS;
        public final double minTPS;
        public final double maxTPS;
        public final double stability;
        public final long totalTicks;
        public final boolean belowThreshold;

        public TPSMetricsReport(double currentTPS, double averageTPS, double minTPS, double maxTPS,
                               double stability, long totalTicks, boolean belowThreshold) {
            this.currentTPS = currentTPS;
            this.averageTPS = averageTPS;
            this.minTPS = minTPS;
            this.maxTPS = maxTPS;
            this.stability = stability;
            this.totalTicks = totalTicks;
            this.belowThreshold = belowThreshold;
        }

        @Override
        public String toString() {
            return String.format(
                    "TPSMetrics{current=%.2f, avg=%.2f, min=%.2f, max=%.2f, stability=%.3f, ticks=%d, belowThreshold=%s}",
                    currentTPS, averageTPS, minTPS, maxTPS, stability, totalTicks, belowThreshold
            );
        }
    }
}