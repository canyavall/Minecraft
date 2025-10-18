package com.xeenaa.fabricenhancements.client;

import com.xeenaa.fabricenhancements.XeenaaFabricEnhancements;
import com.xeenaa.fabricenhancements.performance.client.ClientPerformanceMonitor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side initialization for performance monitoring.
 * Handles client-specific chunk loading and rendering performance tracking.
 */
@Environment(EnvType.CLIENT)
public class XeenaaFabricEnhancementsClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaFabricEnhancements-Client");

    private static ClientPerformanceMonitor clientMonitor;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing client-side performance monitoring");

        // Initialize client performance monitor
        clientMonitor = new ClientPerformanceMonitor(XeenaaFabricEnhancements.getConfig());

        // Register client tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                clientMonitor.onClientTick(client);
            }
        });

        LOGGER.info("Client-side performance monitoring initialized");
    }

    /**
     * Get the client performance monitor instance
     */
    public static ClientPerformanceMonitor getClientMonitor() {
        return clientMonitor;
    }
}