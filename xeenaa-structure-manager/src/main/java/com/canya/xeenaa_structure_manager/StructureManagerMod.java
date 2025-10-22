package com.canya.xeenaa_structure_manager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main mod class for Xeenaa Structure Manager.
 *
 * This mod provides intelligent structure spawning management by:
 * - Automatically scanning and cataloging all registered structures
 * - Classifying structures by size and type
 * - Applying configurable density controls
 * - Improving world generation performance
 *
 * @author Canya
 * @version 1.0.0
 * @since 2025-10-22
 */
public class StructureManagerMod implements ModInitializer {
    public static final String MOD_ID = "xeenaa-structure-manager";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Cache of discovered structures mapped by their identifier.
     * Thread-safe to support parallel classification in future tasks.
     */
    private static final Map<Identifier, Structure> STRUCTURE_REGISTRY = new ConcurrentHashMap<>();

    /**
     * Tracks which mods contribute structures.
     * Key: mod namespace, Value: count of structures from that mod
     */
    private static final Map<String, Integer> MOD_STRUCTURE_COUNTS = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("Xeenaa Structure Manager is initializing!");

        registerWorldLoadHook();

        LOGGER.info("Xeenaa Structure Manager has been initialized!");
    }

    /**
     * Registers the world load event hook to scan structures when a world loads.
     * Only scans the Overworld dimension as per Epic 01 scope.
     */
    private void registerWorldLoadHook() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            // Only scan structures for the Overworld dimension
            if (world.getRegistryKey() == World.OVERWORLD) {
                LOGGER.info("World loaded, scanning structures...");
                scanStructures(world);
            }
        });
    }

    /**
     * Scans all registered structures from the world's dynamic registry.
     * This discovers structures from:
     * - Vanilla Minecraft
     * - Fabric mods
     * - Datapacks
     *
     * Results are stored in STRUCTURE_REGISTRY for classification in TASK-002.
     *
     * @param world The server world to scan structures from
     */
    private void scanStructures(ServerWorld world) {
        long startTime = System.currentTimeMillis();

        // Access the dynamic registry manager to get all registered structures
        DynamicRegistryManager registryManager = world.getRegistryManager();
        Registry<Structure> structureRegistry = registryManager.get(RegistryKeys.STRUCTURE);

        // Clear previous registry data (in case of world reload)
        STRUCTURE_REGISTRY.clear();
        MOD_STRUCTURE_COUNTS.clear();

        // Iterate through all registered structures
        Set<RegistryEntry.Reference<Structure>> entries = structureRegistry.streamEntries().collect(
            java.util.stream.Collectors.toSet()
        );

        for (RegistryEntry.Reference<Structure> entry : entries) {
            RegistryKey<Structure> key = entry.registryKey();
            Identifier id = key.getValue();
            Structure structure = entry.value();

            // Store structure for future classification
            STRUCTURE_REGISTRY.put(id, structure);

            // Track which mod contributed this structure
            String namespace = id.getNamespace();
            MOD_STRUCTURE_COUNTS.merge(namespace, 1, Integer::sum);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Log discovery results
        int totalStructures = STRUCTURE_REGISTRY.size();
        int modCount = MOD_STRUCTURE_COUNTS.size();

        LOGGER.info("Xeenaa: Discovered {} structures from {} mods in {}ms",
            totalStructures, modCount, duration);

        // Log detailed breakdown by mod
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Structure breakdown by mod:");
            MOD_STRUCTURE_COUNTS.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> LOGGER.debug("  {}: {} structures",
                    entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Returns the discovered structure registry.
     * This will be used by the classification engine in TASK-002.
     *
     * @return Unmodifiable map of structure identifiers to Structure instances
     */
    public static Map<Identifier, Structure> getStructureRegistry() {
        return Map.copyOf(STRUCTURE_REGISTRY);
    }

    /**
     * Returns the count of structures discovered from each mod.
     *
     * @return Unmodifiable map of mod namespaces to structure counts
     */
    public static Map<String, Integer> getModStructureCounts() {
        return Map.copyOf(MOD_STRUCTURE_COUNTS);
    }
}
