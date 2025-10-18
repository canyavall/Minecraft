package com.xeenaa.fabricenhancements;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Xeenaa Fabric Enhancements.
 * Focuses on world and chunk performance optimizations with comprehensive monitoring.
 */
public class XeenaaFabricEnhancements implements ModInitializer {
    public static final String MOD_ID = "xeenaa-fabric-enhancements";
    public static final String MOD_NAME = "Xeenaa Fabric Enhancements";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {} - Performance Enhancement Suite", MOD_NAME);
        LOGGER.info("{} initialization complete", MOD_NAME);
    }
}