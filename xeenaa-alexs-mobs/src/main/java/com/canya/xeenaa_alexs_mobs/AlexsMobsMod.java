package com.canya.xeenaa_alexs_mobs;

import com.canya.xeenaa_alexs_mobs.registry.ModEntities;
import com.canya.xeenaa_alexs_mobs.registry.ModItems;
import com.canya.xeenaa_alexs_mobs.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod initializer for Xeenaa's Alex's Mobs (Fabric).
 *
 * This is a Fabric port of Alex's Mobs by AlexModGuy, bringing the beloved
 * animal mobs from the original Forge/NeoForge mod to Fabric 1.21.1.
 *
 * @author Canya
 * @version 0.1.0
 */
public class AlexsMobsMod implements ModInitializer {
    public static final String MOD_ID = "xeenaa-alexs-mobs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Xeenaa's Alex's Mobs (Fabric)");
        LOGGER.info("Original mod by AlexModGuy - Fabric port by Canya");
        // Register sound events (must be before entities)
        ModSounds.initialize();
        LOGGER.info("Sound registration complete");

        // Register entities and their attributes
        ModEntities.initialize();
        LOGGER.info("Entity registration complete");

        // Register items and item groups
        ModItems.initialize();
        LOGGER.info("Item registration complete");
    }
}
