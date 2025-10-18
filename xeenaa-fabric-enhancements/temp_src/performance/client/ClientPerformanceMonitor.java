package com.xeenaa.fabricenhancements.performance.client;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Client-side performance monitoring for chunk loading and rendering.
 * Tracks client-specific performance metrics and rendering performance.
 */
@Environment(EnvType.CLIENT)
public class ClientPerformanceMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPerformanceMonitor.class);

    private final PerformanceConfig config;
    private final ConcurrentHashMap<ChunkPos, Long> clientChunkLoadTimes = new ConcurrentHashMap<>();
    private final LongAdder clientChunkLoads = new LongAdder();
    private final LongAdder renderFrames = new LongAdder();

    private long lastFrameTime = System.nanoTime();
    private double currentFPS = 60.0;
    private long lastTickTime = System.nanoTime();

    public ClientPerformanceMonitor(PerformanceConfig config) {
        this.config = config;
        LOGGER.debug("Client performance monitor initialized");
    }

    /**
     * Called on each client tick
     */
    public void onClientTick(MinecraftClient client) {
        long currentTime = System.nanoTime();
        long tickDuration = currentTime - lastTickTime;
        lastTickTime = currentTime;

        // Update render performance metrics
        updateRenderMetrics(client);

        // Monitor world-specific performance
        if (client.world != null) {
            monitorWorldPerformance(client.world);
        }

        // Check for performance issues
        checkClientPerformance(tickDuration);
    }

    /**
     * Update rendering performance metrics
     */
    private void updateRenderMetrics(MinecraftClient client) {
        long currentTime = System.nanoTime();
        long frameTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        renderFrames.increment();

        // Calculate FPS (simplified)
        if (frameTime > 0) {
            double frameFPS = 1_000_000_000.0 / frameTime;
            // Simple moving average
            currentFPS = (currentFPS * 0.9) + (frameFPS * 0.1);
        }

        // Log performance issues
        if (currentFPS < 30.0) {
            LOGGER.debug("Low FPS detected: {:.1f}", currentFPS);
        }
    }

    /**
     * Monitor world-specific performance on client
     */
    private void monitorWorldPerformance(ClientWorld world) {
        try {
            // Monitor chunk loading on client side
            ChunkPos playerChunk = new ChunkPos(world.getPlayers().iterator().next().getBlockPos());

            // Check if chunk is newly loaded
            if (!clientChunkLoadTimes.containsKey(playerChunk)) {
                recordClientChunkLoad(playerChunk);
            }

            // Clean up old chunk tracking (keep only recent chunks)
            if (clientChunkLoadTimes.size() > 100) {
                cleanupOldChunkTracking();
            }

        } catch (Exception e) {
            LOGGER.debug("Error monitoring client world performance", e);
        }
    }

    /**
     * Record a client-side chunk load
     */
    private void recordClientChunkLoad(ChunkPos pos) {
        long loadTime = System.nanoTime();
        clientChunkLoadTimes.put(pos, loadTime);
        clientChunkLoads.increment();

        LOGGER.trace("Client chunk loaded: {} (total: {})", pos, clientChunkLoads.sum());
    }

    /**
     * Clean up old chunk tracking data
     */
    private void cleanupOldChunkTracking() {
        long currentTime = System.nanoTime();
        long oldThreshold = currentTime - (60_000_000_000L); // 60 seconds

        clientChunkLoadTimes.entrySet().removeIf(entry ->
                entry.getValue() < oldThreshold
        );
    }

    /**
     * Check client performance and log warnings
     */
    private void checkClientPerformance(long tickDuration) {
        double tickMs = tickDuration / 1_000_000.0;

        // Check for client lag
        if (tickMs > 100.0) { // More than 100ms client tick
            LOGGER.debug("Client lag detected: tick took {:.2f}ms", tickMs);
        }

        // Check FPS
        if (currentFPS < config.targetTPS && renderFrames.sum() % 100 == 0) {
            LOGGER.debug("Low FPS: {:.1f} (render frames: {})", currentFPS, renderFrames.sum());
        }
    }

    /**
     * Get current client FPS
     */
    public double getCurrentFPS() {
        return currentFPS;
    }

    /**
     * Get total client chunk loads
     */
    public long getClientChunkLoads() {
        return clientChunkLoads.sum();
    }

    /**
     * Get total render frames
     */
    public long getRenderFrames() {
        return renderFrames.sum();
    }

    /**
     * Get currently tracked chunks
     */
    public int getTrackedChunksCount() {
        return clientChunkLoadTimes.size();
    }
}