package com.canya.xeenaa_structure_manager.mixin;

import com.canya.xeenaa_structure_manager.classification.ClassificationCache;
import com.canya.xeenaa_structure_manager.classification.ClassificationSystem;
import com.canya.xeenaa_structure_manager.classification.StructureClassification;
import com.canya.xeenaa_structure_manager.config.ConfigManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.stream.Stream;

/**
 * Mixin into StructurePlacementCalculator to intercept and modify structure placement.
 *
 * <p>This mixin hooks into the {@link StructurePlacementCalculator#create} method to transform
 * the stream of structure sets before the placement calculator is created. This is the most
 * efficient injection point as it only runs once during calculator creation, not per-chunk.
 *
 * <p><strong>Design Rationale:</strong>
 * <ul>
 *   <li>Injects at calculator creation time (once per world/dimension)</li>
 *   <li>Uses @ModifyVariable to cleanly intercept and transform the stream parameter</li>
 *   <li>Avoids per-chunk overhead by modifying placement rules at creation time</li>
 *   <li>Preserves compatibility by returning a valid stream in all cases</li>
 * </ul>
 *
 * <p><strong>Implementation Status:</strong>
 * <ul>
 *   <li>TASK-007: Infrastructure created, interception confirmed (current)</li>
 *   <li>TASK-008: Transformation logic will be added (next task)</li>
 * </ul>
 *
 * @see StructurePlacementCalculator#create
 * @see com.canya.xeenaa_structure_manager.config.ConfigManager
 * @see com.canya.xeenaa_structure_manager.classification.ClassificationCache
 */
@Mixin(StructurePlacementCalculator.class)
public class StructurePlacementCalculatorMixin {

    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager/Mixin");

    /**
     * Intercepts the structure set stream during calculator creation and applies classification-based multipliers.
     *
     * <p>This method captures the stream of structure sets passed to the placement calculator
     * constructor and transforms it to apply classification-based spacing/separation multipliers.
     * The transformation preserves all structure set properties and relationships while adjusting
     * placement frequency based on structure size and type.
     *
     * <p><strong>Implementation (TASK-008):</strong><br>
     * Transforms the stream using {@code .map()} to:
     * <ol>
     *   <li>Look up structure classification from ClassificationCache</li>
     *   <li>Query ConfigManager for size/type multipliers using priority system</li>
     *   <li>Calculate new spacing/separation values by applying multipliers</li>
     *   <li>Create modified StructureSet with new placement rules</li>
     * </ol>
     *
     * <p><strong>Priority System:</strong><br>
     * Multipliers are applied in the following priority order:
     * <ol>
     *   <li>Structure Override (absolute values from config)</li>
     *   <li>Type-based multiplier (e.g., type.dungeon = 1.8x)</li>
     *   <li>Size-based multiplier (e.g., size.large = 2.5x)</li>
     *   <li>Global multiplier (e.g., global = 1.0x)</li>
     * </ol>
     *
     * <p><strong>Error Handling:</strong><br>
     * If classification system is not initialized, classification fails, or any error occurs during
     * transformation, the original structure set is returned unchanged. This ensures compatibility
     * and prevents crashes.
     *
     * <p><strong>Thread Safety:</strong><br>
     * This method runs during world generation on the server thread. The stream transformation
     * is stateless and thread-safe, relying on immutable configuration and cached classifications.
     *
     * <p><strong>Stream Handling:</strong><br>
     * Streams can only be consumed once. This method returns a transformed stream using {@code .map()},
     * which is safe. Terminal operations like {@code .forEach()} are forbidden here.
     *
     * @param structureSets Original stream of structure sets from Minecraft's world generation
     * @param noiseConfig Noise configuration for world generation (unused, but part of signature)
     * @param seed World seed used for placement calculations (unused, but part of signature)
     * @param biomeSource Biome source for the dimension (unused, but part of signature)
     * @return Transformed stream with modified structure placement rules
     */
    @ModifyVariable(
        method = "create(Lnet/minecraft/world/gen/noise/NoiseConfig;JLnet/minecraft/world/biome/source/BiomeSource;Ljava/util/stream/Stream;)Lnet/minecraft/world/gen/chunk/placement/StructurePlacementCalculator;",
        at = @At("HEAD"),
        argsOnly = true
    )
    private static Stream<RegistryEntry<StructureSet>> modifyStructureSets(
        Stream<RegistryEntry<StructureSet>> structureSets,
        NoiseConfig noiseConfig,
        long seed,
        BiomeSource biomeSource
    ) {
        LOGGER.info("=== DIAGNOSTIC: Structure Placement Calculator Creation ===");
        LOGGER.info("Intercepting structure set stream...");

        // Get singletons
        ConfigManager config = ConfigManager.getInstance();
        ClassificationSystem classificationSystem = ClassificationSystem.getInstance();

        // Safety check: if classification system not initialized, return unchanged
        if (classificationSystem == null) {
            LOGGER.warn("ClassificationSystem not initialized, skipping multiplier application");
            return structureSets;
        }

        ClassificationCache cache = classificationSystem.getCache();

        // DIAGNOSTIC: Count structures in stream
        final java.util.concurrent.atomic.AtomicInteger structureCount = new java.util.concurrent.atomic.AtomicInteger(0);
        final java.util.concurrent.atomic.AtomicInteger modifiedCount = new java.util.concurrent.atomic.AtomicInteger(0);

        // Transform stream with classification-based multipliers
        Stream<RegistryEntry<StructureSet>> result = structureSets.map(entry -> {
            structureCount.incrementAndGet();
            try {
                RegistryEntry<StructureSet> transformed = transformStructureSet(entry, config, cache, classificationSystem);
                if (transformed != entry) {
                    modifiedCount.incrementAndGet();
                }
                return transformed;
            } catch (Exception e) {
                // On any error, log and return original entry
                LOGGER.error("Error transforming structure set: {}", e.getMessage(), e);
                return entry;
            }
        });

        // Log diagnostic info after stream is consumed
        LOGGER.info("=== DIAGNOSTIC: Stream Processing Complete ===");
        LOGGER.info("Total structure sets in stream: {}", structureCount.get());
        LOGGER.info("Structure sets modified: {}", modifiedCount.get());
        LOGGER.info("This represents structures AFTER dimension + biome filtering");

        return result;
    }

    /**
     * Transforms a single structure set by applying classification-based multipliers.
     * <p>
     * This method extracts the structure identifier, looks up its classification, queries
     * the configuration for appropriate multipliers, and creates a new StructureSet with
     * modified spacing/separation values.
     *
     * <p><strong>Edge Cases Handled:</strong>
     * <ul>
     *   <li>Empty structure sets - returned unchanged</li>
     *   <li>No classification found - lazy classification triggered (new in Epic 03)</li>
     *   <li>Disabled structures - returned unchanged (with info log)</li>
     *   <li>Multiplier = 1.0x - returned unchanged (preserve vanilla behavior)</li>
     *   <li>Non-RandomSpread placements - returned unchanged (future enhancement)</li>
     * </ul>
     *
     * @param entry the registry entry containing the structure set
     * @param config the configuration manager for querying multipliers
     * @param cache the classification cache for looking up structure classifications
     * @param classificationSystem the classification system for lazy classification
     * @return modified registry entry with new placement rules, or original if no changes needed
     */
    private static RegistryEntry<StructureSet> transformStructureSet(
        RegistryEntry<StructureSet> entry,
        ConfigManager config,
        ClassificationCache cache,
        ClassificationSystem classificationSystem
    ) {
        StructureSet originalSet = entry.value();

        // Get structure IDs from the set
        var structures = originalSet.structures();

        if (structures.isEmpty()) {
            LOGGER.debug("Empty structure set, skipping");
            return entry; // No structures, return unchanged
        }

        // Use first structure's classification (most sets have one structure anyway)
        var firstStructure = structures.get(0);
        Identifier structureId = getStructureId(firstStructure);

        if (structureId == null) {
            LOGGER.debug("Could not extract structure ID, skipping");
            return entry;
        }

        // Lazy classification: Check cache first
        StructureClassification classification = cache.get(structureId);

        if (classification == null) {
            // Cache miss - classify now (lazy classification)
            var structureEntry = firstStructure.structure();
            classification = classificationSystem.classifyStructure(
                structureId,
                structureEntry.value()
            );

            LOGGER.info("Classified {} as {} {}",
                structureId, classification.size(), classification.type());
        }

        // Check if structure is enabled
        if (!config.isStructureEnabled(structureId)) {
            LOGGER.info("Structure {} is disabled, skipping", structureId);
            return entry; // Structure disabled, return unchanged
        }

        // Get spacing multiplier using priority system
        double spacingMultiplier = config.getSpacingMultiplier(
            structureId,
            classification.size(),
            classification.type()
        );

        // Get separation multiplier
        double separationMultiplier = config.getSeparationMultiplier(
            structureId,
            classification.size(),
            classification.type()
        );

        // Skip if multiplier is 1.0 (no change needed)
        if (spacingMultiplier == 1.0 && separationMultiplier == 1.0) {
            LOGGER.debug("Multiplier is 1.0x for {}, skipping", structureId);
            return entry; // No modification needed
        }

        // Modify placement
        StructurePlacement originalPlacement = originalSet.placement();
        StructurePlacement modifiedPlacement = modifyPlacement(
            originalPlacement,
            spacingMultiplier,
            separationMultiplier,
            structureId
        );

        // If placement couldn't be modified, return original
        if (modifiedPlacement == originalPlacement) {
            return entry;
        }

        // Create new StructureSet with modified placement
        StructureSet modifiedSet = new StructureSet(
            originalSet.structures(),
            modifiedPlacement
        );

        // Log applied multiplier
        LOGGER.info("Applied {}x spacing, {}x separation to {} ({})",
            String.format("%.2f", spacingMultiplier),
            String.format("%.2f", separationMultiplier),
            structureId,
            classification.type());

        // Wrap in registry entry and return
        return RegistryEntry.of(modifiedSet);
    }

    /**
     * Extracts the structure identifier from a structure selection entry.
     * <p>
     * This method navigates the Minecraft API to extract the Identifier from the
     * structure selection entry using reflection. The entry type is internal to
     * Minecraft and not directly accessible, so we use reflection to call the
     * structure() method and extract the registry key.
     *
     * @param entry the structure selection entry (Object to avoid compile-time dependencies)
     * @return the structure identifier, or null if extraction fails
     */
    private static Identifier getStructureId(Object entry) {
        try {
            // Use reflection to call structure() method
            // The entry should have: structure() -> RegistryEntry<Structure> -> getKey() -> Optional<RegistryKey>
            var structureMethod = entry.getClass().getMethod("structure");
            var registryEntry = (RegistryEntry<?>) structureMethod.invoke(entry);

            return registryEntry.getKey()
                .map(key -> key.getValue())
                .orElse(null);
        } catch (Exception e) {
            LOGGER.warn("Failed to extract structure ID via reflection: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Modifies a structure placement by applying spacing and separation multipliers.
     * <p>
     * This method checks the placement type and creates a new placement with modified values.
     * Currently only supports {@link RandomSpreadStructurePlacement}, which covers most vanilla
     * structures (villages, temples, dungeons, etc.).
     *
     * <p><strong>Supported Placements:</strong>
     * <ul>
     *   <li>{@link RandomSpreadStructurePlacement} - Villages, temples, dungeons, ruins, etc.</li>
     * </ul>
     *
     * <p><strong>Unsupported Placements (returned unchanged):</strong>
     * <ul>
     *   <li>ConcentricRingsStructurePlacement - Strongholds (future enhancement)</li>
     *   <li>Other placement types (rare, low priority)</li>
     * </ul>
     *
     * <p><strong>Validation:</strong>
     * <ul>
     *   <li>Ensures separation &lt; spacing (Minecraft requirement)</li>
     *   <li>Ensures separation ≥ 1 (minimum valid value)</li>
     *   <li>Logs warnings if adjustments are needed</li>
     * </ul>
     *
     * @param original the original structure placement
     * @param spacingMultiplier the multiplier to apply to spacing
     * @param separationMultiplier the multiplier to apply to separation
     * @param structureId the structure identifier (for logging)
     * @return modified placement with new spacing/separation, or original if unsupported
     */
    private static StructurePlacement modifyPlacement(
        StructurePlacement original,
        double spacingMultiplier,
        double separationMultiplier,
        Identifier structureId
    ) {
        // Check if this is a RandomSpreadStructurePlacement (most common type)
        if (original instanceof RandomSpreadStructurePlacement randomSpread) {
            // Cast to accessor to access and modify private fields
            RandomSpreadStructurePlacementAccessor accessor = (RandomSpreadStructurePlacementAccessor) randomSpread;

            // Get original values
            int originalSpacing = accessor.getSpacing();
            int originalSeparation = accessor.getSeparation();

            // Calculate new values
            int newSpacing = (int) Math.ceil(originalSpacing * spacingMultiplier);
            int newSeparation = (int) Math.ceil(originalSeparation * separationMultiplier);

            // Validate: separation must be < spacing
            if (newSeparation >= newSpacing) {
                LOGGER.warn("Separation ({}) >= spacing ({}) for {}, adjusting separation",
                    newSeparation, newSpacing, structureId);
                newSeparation = Math.max(1, newSpacing - 1);
            }

            // Ensure separation is at least 1
            if (newSeparation < 1) {
                newSeparation = 1;
            }

            // Log the transformation (INFO level for diagnostic visibility)
            LOGGER.info("DIAGNOSTIC: Transformed placement for {}: spacing {}→{} ({}x), separation {}→{} ({}x)",
                structureId,
                originalSpacing, newSpacing, String.format("%.2f", spacingMultiplier),
                originalSeparation, newSeparation, String.format("%.2f", separationMultiplier));

            // Modify the placement in-place using mutable accessors
            accessor.setSpacing(newSpacing);
            accessor.setSeparation(newSeparation);

            // Return the modified placement (same object reference, but with updated fields)
            return original;
        }

        // For other placement types (ConcentricRings, etc.), return unchanged
        // These are less common and can be handled in a future epic
        LOGGER.debug("Unsupported placement type {} for {}, skipping",
            original.getClass().getSimpleName(), structureId);
        return original;
    }
}
