package com.example.helloworld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldMod implements ModInitializer {
    public static final String MOD_ID = "helloworld";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Create a simple item as an example
    public static final Item HELLO_ITEM = new Item(new Item.Settings());
    
    @Override
    public void onInitialize() {
        LOGGER.info("Hello World Mod is initializing!");
        
        // Register the hello item
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "hello_item"), HELLO_ITEM);
        
        // Add item to creative menu
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(HELLO_ITEM);
        });
        
        LOGGER.info("Hello World Mod has been initialized!");
    }
}