package com.canya.xeenaa_structure_manager.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side initialization for Xeenaa Structure Manager.
 * <p>
 * This class handles client-only functionality:
 * <ul>
 *   <li>Future: Configuration GUI</li>
 *   <li>Future: Visual feedback for structure detection</li>
 * </ul>
 * <p>
 * Currently, this class exists to satisfy the client entrypoint requirement
 * in fabric.mod.json, but doesn't perform any client-specific initialization.
 *
 * @since 1.0.0
 */
public class StructureManagerClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("xeenaa-structure-manager-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Xeenaa Structure Manager Client is initializing!");

        // TODO: Add client-side initialization
        // - Configuration GUI
        // - Visual feedback for structure detection

        LOGGER.info("Xeenaa Structure Manager Client has been initialized!");
    }
}
