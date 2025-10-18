package com.xeenaa.fabricenhancements.performance.metrics;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Central metrics collection system using Micrometer.
 * Collects and aggregates performance metrics from all subsystems.
 */
public class MetricsCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollector.class);

    private final PerformanceConfig config;
    private final MeterRegistry meterRegistry;
    private final ScheduledExecutorService scheduler;

    private ChunkMetrics chunkMetrics;
    private MemoryMetrics memoryMetrics;
    private TPSMetrics tpsMetrics;
    private ServerMetrics serverMetrics;

    private MinecraftServer server;
    private final AtomicBoolean collecting = new AtomicBoolean(false);

    public MetricsCollector(PerformanceConfig config) {
        this.config = config;

        // Initialize meter registry (Prometheus for production, Simple for development)
        if (config.enableSparkIntegration) {
            this.meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        } else {
            this.meterRegistry = new SimpleMeterRegistry();
        }

        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    /**
     * Initialize metrics collector with server instance
     */
    public void initialize(MinecraftServer server) {
        this.server = server;

        // Initialize individual metrics collectors
        this.chunkMetrics = new ChunkMetrics(config, meterRegistry);
        this.memoryMetrics = new MemoryMetrics(config, meterRegistry);
        this.tpsMetrics = new TPSMetrics(config, meterRegistry);
        this.serverMetrics = new ServerMetrics(config, meterRegistry);

        LOGGER.info("Metrics collector initialized with {} registry",
                   meterRegistry.getClass().getSimpleName());
    }

    /**
     * Start metrics collection
     */
    public void startCollection() {
        if (collecting.getAndSet(true)) {
            LOGGER.warn("Metrics collection already started");
            return;
        }

        LOGGER.info("Starting metrics collection with interval: {}ms", config.metricsCollectionInterval);

        // Schedule metrics collection
        scheduler.scheduleAtFixedRate(
                this::collectAllMetrics,
                0,
                config.metricsCollectionInterval,
                TimeUnit.MILLISECONDS
        );

        // Schedule metrics reporting
        scheduler.scheduleAtFixedRate(
                this::reportMetrics,
                30,
                30,
                TimeUnit.SECONDS
        );
    }

    /**
     * Stop metrics collection
     */
    public void stopCollection() {
        if (!collecting.getAndSet(false)) {
            LOGGER.warn("Metrics collection not started");
            return;
        }

        LOGGER.info("Stopping metrics collection");

        // Final metrics collection
        collectAllMetrics();
        reportMetrics();

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
    }

    /**
     * Called on each server tick for real-time metrics
     */
    public void onServerTick(MinecraftServer server) {
        if (!collecting.get() || this.server == null) return;

        try {
            // Update real-time metrics
            tpsMetrics.onServerTick(server);
            serverMetrics.onServerTick(server);

        } catch (Exception e) {
            LOGGER.warn("Error collecting tick metrics", e);
        }
    }

    /**
     * Collect all metrics from subsystems
     */
    private void collectAllMetrics() {
        if (server == null) return;

        try {
            long startTime = System.nanoTime();

            // Collect metrics from all subsystems
            chunkMetrics.collect(server);
            memoryMetrics.collect();
            tpsMetrics.collect(server);
            serverMetrics.collect(server);

            long collectionTime = System.nanoTime() - startTime;

            // Track collection performance
            meterRegistry.timer("xeenaa.metrics.collection_time")
                    .record(collectionTime, TimeUnit.NANOSECONDS);

            LOGGER.trace("Metrics collection completed in {:.2f}ms",
                        collectionTime / 1_000_000.0);

        } catch (Exception e) {
            LOGGER.error("Error during metrics collection", e);
        }
    }

    /**
     * Report current metrics to log
     */
    private void reportMetrics() {
        try {
            LOGGER.info("=== Performance Metrics Report ===");

            if (tpsMetrics != null) {
                LOGGER.info("TPS: Current={:.2f}, Average={:.2f}, Min={:.2f}",
                           tpsMetrics.getCurrentTPS(),
                           tpsMetrics.getAverageTPS(),
                           tpsMetrics.getMinTPS());
            }

            if (chunkMetrics != null) {
                LOGGER.info("Chunks: Loaded={}, Cache Hit Ratio={:.2f}%, Avg Load Time={:.2f}ms",
                           chunkMetrics.getLoadedChunkCount(),
                           chunkMetrics.getCacheHitRatio() * 100,
                           chunkMetrics.getAverageLoadTime());
            }

            if (memoryMetrics != null) {
                LOGGER.info("Memory: Used={}MB, Free={}MB, Allocated={}MB",
                           memoryMetrics.getCurrentMemoryUsageMB(),
                           memoryMetrics.getFreeMemoryMB(),
                           memoryMetrics.getAllocatedMemoryMB());
            }

            if (serverMetrics != null) {
                LOGGER.info("Server: Players={}, Entities={}, Loaded Chunks={}",
                           serverMetrics.getPlayerCount(),
                           serverMetrics.getEntityCount(),
                           serverMetrics.getLoadedChunkCount());
            }

            LOGGER.info("=== End Report ===");

        } catch (Exception e) {
            LOGGER.error("Error generating metrics report", e);
        }
    }

    /**
     * Get metrics registry for external integrations
     */
    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
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
     * Get server metrics instance
     */
    public ServerMetrics getServerMetrics() {
        return serverMetrics;
    }

    /**
     * Export metrics in Prometheus format (if using Prometheus registry)
     */
    public String exportPrometheusMetrics() {
        if (meterRegistry instanceof PrometheusMeterRegistry) {
            return ((PrometheusMeterRegistry) meterRegistry).scrape();
        }
        return "Prometheus metrics not available";
    }

    /**
     * Check if metrics collection is active
     */
    public boolean isCollecting() {
        return collecting.get();
    }
}