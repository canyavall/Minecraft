package com.xeenaa.fabricenhancements.performance.metrics;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import io.micrometer.core.instrument.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Metrics collector for general server performance and state.
 * Tracks player counts, entity counts, and world statistics.
 */
public class ServerMetrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMetrics.class);

    private final PerformanceConfig config;
    private final MeterRegistry meterRegistry;

    // Metrics
    private final Gauge playerCountGauge;
    private final Gauge entityCountGauge;
    private final Gauge loadedChunksGauge;
    private final Counter playerJoinCounter;
    private final Counter playerLeaveCounter;

    // Current values
    private int currentPlayerCount = 0;
    private int currentEntityCount = 0;
    private int currentLoadedChunks = 0;

    public ServerMetrics(PerformanceConfig config) {
        this(config, null);
    }

    public ServerMetrics(PerformanceConfig config, MeterRegistry meterRegistry) {
        this.config = config;
        this.meterRegistry = meterRegistry != null ? meterRegistry : Metrics.globalRegistry;

        // Initialize metrics
        this.playerCountGauge = Gauge.builder("xeenaa.server.players")
                .description("Number of connected players")
                .register(this.meterRegistry, this, ServerMetrics::getPlayerCount);

        this.entityCountGauge = Gauge.builder("xeenaa.server.entities")
                .description("Total number of entities across all worlds")
                .register(this.meterRegistry, this, ServerMetrics::getEntityCount);

        this.loadedChunksGauge = Gauge.builder("xeenaa.server.loaded_chunks")
                .description("Total number of loaded chunks across all worlds")
                .register(this.meterRegistry, this, ServerMetrics::getLoadedChunkCount);

        this.playerJoinCounter = Counter.builder("xeenaa.server.player_joins_total")
                .description("Total number of player joins")
                .register(this.meterRegistry);

        this.playerLeaveCounter = Counter.builder("xeenaa.server.player_leaves_total")
                .description("Total number of player leaves")
                .register(this.meterRegistry);
    }

    /**
     * Collect server metrics
     */
    public void collect(MinecraftServer server) {
        if (server == null) return;

        try {
            // Update player count
            currentPlayerCount = server.getCurrentPlayerCount();

            // Count entities and chunks across all worlds
            int totalEntities = 0;
            int totalChunks = 0;

            for (ServerWorld world : server.getWorlds()) {
                totalEntities += world.iterateEntities().spliterator().getExactSizeIfKnown();
                totalChunks += world.getChunkManager().getTotalChunksLoadedCount();

                // Register world-specific metrics
                registerWorldMetrics(world);
            }

            currentEntityCount = totalEntities;
            currentLoadedChunks = totalChunks;

        } catch (Exception e) {
            LOGGER.warn("Error collecting server metrics", e);
        }
    }

    /**
     * Register world-specific metrics
     */
    private void registerWorldMetrics(ServerWorld world) {
        String worldName = world.getRegistryKey().getValue().toString();

        // World-specific entity count
        Gauge.builder("xeenaa.server.world_entities")
                .description("Number of entities in specific world")
                .tag("world", worldName)
                .register(meterRegistry, world, w -> (double) w.iterateEntities().spliterator().getExactSizeIfKnown());

        // World-specific loaded chunks
        Gauge.builder("xeenaa.server.world_chunks")
                .description("Number of loaded chunks in specific world")
                .tag("world", worldName)
                .register(meterRegistry, world, w -> (double) w.getChunkManager().getTotalChunksLoadedCount());
    }

    /**
     * Called on each server tick for real-time updates
     */
    public void onServerTick(MinecraftServer server) {
        // Real-time metrics that need frequent updates can be handled here
        // For now, the main collection happens in collect()
    }

    /**
     * Record a player join event
     */
    public void recordPlayerJoin() {
        playerJoinCounter.increment();
    }

    /**
     * Record a player leave event
     */
    public void recordPlayerLeave() {
        playerLeaveCounter.increment();
    }

    /**
     * Get current player count
     */
    public double getPlayerCount() {
        return currentPlayerCount;
    }

    /**
     * Get current entity count
     */
    public double getEntityCount() {
        return currentEntityCount;
    }

    /**
     * Get current loaded chunk count
     */
    public double getLoadedChunkCount() {
        return currentLoadedChunks;
    }
}