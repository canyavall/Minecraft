package com.canya.xeenaa_structure_manager;

import com.canya.xeenaa_structure_manager.classification.ClassificationSystem;
import com.canya.xeenaa_structure_manager.command.XeenaaCommand;
import com.canya.xeenaa_structure_manager.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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

        initializeConfig();
        registerWorldLoadHook();
        registerCommands();

        LOGGER.info("Xeenaa Structure Manager has been initialized!");
    }

    /**
     * Initializes the configuration system and generates config file if needed.
     * <p>
     * This method calls ConfigManager.getInstance() to trigger lazy initialization,
     * which will:
     * <ul>
     *   <li>Create the config directory if it doesn't exist</li>
     *   <li>Generate default config file with Balanced preset</li>
     *   <li>Load configuration values into memory</li>
     * </ul>
     * <p>
     * This is called early during mod initialization to ensure the config file
     * is available for users to edit before world generation begins.
     */
    private void initializeConfig() {
        ConfigManager config = ConfigManager.getInstance();
        LOGGER.info("Configuration system initialized");
    }

    /**
     * Registers all mod commands with the Fabric command system.
     * <p>
     * Currently registers:
     * <ul>
     *   <li>/xeenaa stats - Display placement statistics</li>
     *   <li>/xeenaa clear - Clear placement tracking data</li>
     * </ul>
     */
    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(XeenaaCommand::register);
        LOGGER.info("Registered mod commands");
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

        // Log detailed breakdown by mod (always show top contributors)
        LOGGER.info("Structure breakdown by mod:");
        MOD_STRUCTURE_COUNTS.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> LOGGER.info("  {}: {} structures",
                entry.getKey(), entry.getValue()));

        // DIAGNOSTIC: Analyze potential duplicates by grouping similar names
        LOGGER.info("=== DIAGNOSTIC: Potential Duplicate Analysis ===");
        analyzePotentialDuplicates();

        // Log individual structures at DEBUG level
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("All discovered structures:");
            STRUCTURE_REGISTRY.keySet().stream()
                .sorted()
                .forEach(id -> LOGGER.debug("  - {}", id));
        }

        // Initialize classification system (singleton instance)
        // Two modes available: upfront classification (all at once) or lazy classification (on-demand)
        ClassificationSystem classificationSystem = new ClassificationSystem(world);

        // PERFORMANCE TEST: Enable both modes to measure real-world performance difference
        boolean enableUpfrontClassification = true; // Set to false to use lazy-only mode

        if (enableUpfrontClassification) {
            LOGGER.info("=== PERFORMANCE TEST: Starting upfront classification of {} structures ===", totalStructures);
            long classifyStartTime = System.currentTimeMillis();

            // Run upfront classification
            classificationSystem.classifyAll(structureRegistry);

            long classifyEndTime = System.currentTimeMillis();
            long classifyDuration = classifyEndTime - classifyStartTime;

            LOGGER.info("=== UPFRONT CLASSIFICATION COMPLETE: {} structures classified in {}ms ({} seconds) ===",
                totalStructures, classifyDuration, String.format("%.2f", classifyDuration / 1000.0));
            LOGGER.info("=== Average: {}ms per structure ===", String.format("%.2f", (double) classifyDuration / totalStructures));
        } else {
            LOGGER.info("=== Lazy classification mode enabled (structures classified on-demand during placement) ===");
        }

        // DIAGNOSTIC: Analyze memory consumption after classification
        analyzeMemoryConsumption();
    }

    /**
     * Initializes the classification system and classifies all structures.
     * <p>
     * This method creates the ClassificationSystem singleton and runs classification
     * on all discovered structures. Classification results are cached for use by the
     * mixin that applies multipliers.
     * <p>
     * <b>Performance Reporting:</b>
     * <ul>
     *   <li>Logs performance summary header before classification</li>
     *   <li>Measures classification time and memory usage</li>
     *   <li>Verifies performance targets are met</li>
     *   <li>Logs completion summary</li>
     * </ul>
     *
     * @param world the server world providing template manager access
     * @param structureRegistry the registry containing all structures to classify
     */
    private void initializeClassificationSystem(ServerWorld world, Registry<Structure> structureRegistry) {
        try {
            LOGGER.info("=== Xeenaa Structure Manager - Performance Report ===");

            // Create classification system (sets singleton instance)
            ClassificationSystem classificationSystem = new ClassificationSystem(world);

            // Classify all structures (includes performance logging)
            classificationSystem.classifyAll(structureRegistry);

            LOGGER.info("=== Xeenaa Structure Manager - Initialization Complete ===");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize classification system", e);
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

    /**
     * DIAGNOSTIC: Analyzes potential duplicate structures by grouping similar names.
     * <p>
     * This method groups structures by normalized base names to identify potential
     * duplicates across mods (e.g., multiple desert temples, village variants, etc.).
     * <p>
     * Provides data to answer: "Do we really have 5 desert temples?"
     */
    private void analyzePotentialDuplicates() {
        // Group structures by normalized name
        Map<String, java.util.List<Identifier>> groupedStructures = new java.util.HashMap<>();

        for (Identifier id : STRUCTURE_REGISTRY.keySet()) {
            String normalizedName = normalizeStructureName(id.getPath());
            groupedStructures.computeIfAbsent(normalizedName, k -> new java.util.ArrayList<>()).add(id);
        }

        // Find groups with multiple structures (potential duplicates)
        int totalDuplicateGroups = 0;
        int totalDuplicateStructures = 0;

        LOGGER.info("Analyzing {} unique structure types...", groupedStructures.size());

        for (Map.Entry<String, java.util.List<Identifier>> entry : groupedStructures.entrySet()) {
            java.util.List<Identifier> variants = entry.getValue();
            if (variants.size() > 1) {
                totalDuplicateGroups++;
                totalDuplicateStructures += variants.size();

                // Log groups with 3+ variants (significant duplication)
                if (variants.size() >= 3) {
                    LOGGER.info("  '{}': {} variants from {} mod(s)",
                        entry.getKey(),
                        variants.size(),
                        variants.stream().map(Identifier::getNamespace).distinct().count());

                    // Show first 5 variants
                    variants.stream().limit(5).forEach(variant ->
                        LOGGER.info("    - {}", variant));

                    if (variants.size() > 5) {
                        LOGGER.info("    ... and {} more", variants.size() - 5);
                    }
                }
            }
        }

        LOGGER.info("Duplicate Analysis Summary:");
        LOGGER.info("  Total structure groups: {}", groupedStructures.size());
        LOGGER.info("  Groups with duplicates: {} ({} structures total)",
            totalDuplicateGroups, totalDuplicateStructures);
        LOGGER.info("  Potential savings: {} structures could be deduplicated",
            totalDuplicateStructures - totalDuplicateGroups);
    }

    /**
     * Normalizes a structure name to group similar variants together.
     * <p>
     * Examples:
     * - "desert_pyramid_1", "desert_pyramid_large" → "desert_pyramid"
     * - "village_plains", "village_desert" → "village"
     * - "mineshaft_mesa", "mineshaft_taiga" → "mineshaft"
     *
     * @param path the structure path from identifier
     * @return normalized base name
     */
    private String normalizeStructureName(String path) {
        String normalized = path.toLowerCase();

        // Remove common suffixes
        normalized = normalized.replaceAll("_(\\d+|small|medium|large|huge|variant|alt|new|old)$", "");

        // Remove biome suffixes
        normalized = normalized.replaceAll("_(plains|desert|savanna|taiga|snowy|jungle|swamp|ocean|nether|end|crimson|warped|soul|basalt|ice|icy|stone|birch|dark_forest|mushroom|mangrove|grassy)$", "");

        // Remove directional/size descriptors
        normalized = normalized.replaceAll("_(north|south|east|west|upper|lower|top|bottom)$", "");

        return normalized;
    }

    /**
     * DIAGNOSTIC: Logs detailed memory consumption for structures.
     * <p>
     * Measures and reports memory usage of:
     * - Structure registry (Identifier + Structure objects)
     * - Classification cache
     * - Placement tracking
     * <p>
     * Provides data to answer: "How much memory are structures consuming?"
     */
    private void analyzeMemoryConsumption() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        LOGGER.info("=== DIAGNOSTIC: Memory Consumption Analysis ===");
        LOGGER.info("JVM Memory:");
        LOGGER.info("  Used: {} MB / {} MB (max: {} MB)",
            usedMemory / 1_048_576,
            totalMemory / 1_048_576,
            maxMemory / 1_048_576);

        // Estimate structure registry size
        int structureCount = STRUCTURE_REGISTRY.size();
        // Rough estimate: Identifier (~100 bytes) + Structure object (~200 bytes) per entry
        long estimatedRegistrySize = structureCount * 300L;

        LOGGER.info("Structure Registry:");
        LOGGER.info("  Structures: {}", structureCount);
        LOGGER.info("  Estimated memory: ~{} KB ({} bytes per structure)",
            estimatedRegistrySize / 1024,
            300);

        // Classification cache analysis (if available)
        ClassificationSystem classificationSystem = ClassificationSystem.getInstance();
        if (classificationSystem != null) {
            int cacheSize = classificationSystem.getCache().size();
            // Estimate: StructureClassification record (~150 bytes per entry)
            long estimatedCacheSize = cacheSize * 150L;

            LOGGER.info("Classification Cache:");
            LOGGER.info("  Cached entries: {}", cacheSize);
            LOGGER.info("  Estimated memory: ~{} KB ({} bytes per entry)",
                estimatedCacheSize / 1024,
                150);
        }

        LOGGER.info("Total Structure System Overhead:");
        LOGGER.info("  Estimated: ~{} KB (~{}% of used memory)",
            (estimatedRegistrySize) / 1024,
            String.format("%.2f", (estimatedRegistrySize * 100.0) / usedMemory));
    }
}
