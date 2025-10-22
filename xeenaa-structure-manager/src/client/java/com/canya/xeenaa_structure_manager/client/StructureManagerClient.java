package com.canya.xeenaa_structure_manager.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
