package com.example.helloworld.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("helloworld-client");
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello World Client is initializing!");
        
        // Add client-side initialization here
        // For example: keybindings, rendering, client-side event handlers
        
        LOGGER.info("Hello World Client has been initialized!");
    }
}