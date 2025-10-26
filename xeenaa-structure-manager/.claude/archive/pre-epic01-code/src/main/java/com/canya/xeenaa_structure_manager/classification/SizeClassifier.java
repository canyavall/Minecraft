package com.canya.xeenaa_structure_manager.classification;

import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Classifies structures by SIZE based on bounding box volume analysis.
 * <p>
 * This classifier implements the SIZE dimension of the two-dimensional classification
 * system (SIZE + TYPE). It calculates the physical footprint of structures by analyzing
 * NBT template bounding boxes and categorizes them into SMALL, MEDIUM, or LARGE.
 * <p>
 * <b>Classification Strategy:</b>
 * <ol>
 *   <li><b>Primary Method</b>: NBT Template Analysis
 *     <ul>
 *       <li>Access StructureTemplateManager to retrieve structure NBT</li>
 *       <li>Extract bounding box (BlockBox) from template</li>
 *       <li>Calculate volume: width × height × depth</li>
 *       <li>Categorize using {@link SizeCategory#fromVolume(int)}</li>
 *     </ul>
 *   </li>
 *   <li><b>Fallback Method</b>: Heuristic Estimation
 *     <ul>
 *       <li>When NBT template is unavailable (datapacks, JSON-only structures)</li>
 *       <li>Parse structure identifier for known keywords</li>
 *       <li>Use conservative size estimates based on structure name</li>
 *       <li>Default to MEDIUM (10,000 blocks³) when uncertain</li>
 *       <li>Log WARNING for transparency</li>
 *     </ul>
 *   </li>
 * </ol>
 * <p>
 * <b>Heuristic Estimation Rules:</b>
 * <ul>
 *   <li><b>Large structures (> 32,768 blocks³)</b>: mansion, village, stronghold, ancient_city, fortress</li>
 *   <li><b>Medium structures (4,096 - 32,768 blocks³)</b>: temple, monument, pyramid, dungeon</li>
 *   <li><b>Small structures (< 4,096 blocks³)</b>: ruin, shrine, hut, house</li>
 *   <li><b>Default</b>: 10,000 blocks³ (MEDIUM) - conservative fallback</li>
 * </ul>
 * <p>
 * <b>Performance Considerations:</b>
 * <ul>
 *   <li>NBT template access is expensive - cache results in {@link ClassificationCache}</li>
 *   <li>Fallback estimation is fast (string operations only)</li>
 *   <li>Thread-safe for parallel classification during world load</li>
 * </ul>
 * <p>
 * <b>Error Handling:</b>
 * <ul>
 *   <li>Null template: Log WARNING, use fallback estimation</li>
 *   <li>Missing NBT data: Log DEBUG, use fallback estimation</li>
 *   <li>Invalid bounding box: Log WARN, default to MEDIUM</li>
 *   <li>Never crashes - always returns a valid SizeCategory</li>
 * </ul>
 *
 * @see SizeCategory
 * @see StructureClassification
 * @see ClassificationCache
 * @since 1.0.0
 */
public class SizeClassifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(SizeClassifier.class);

    /**
     * Manager for accessing structure NBT templates.
     * <p>
     * This is provided by the ServerWorld and grants access to structure templates
     * stored in data packs and mods.
     */
    private final StructureTemplateManager templateManager;

    /**
     * Creates a new SIZE classifier with the specified template manager.
     * <p>
     * The template manager should be obtained from ServerWorld:
     * <pre>
     * StructureTemplateManager manager = serverWorld.getStructureTemplateManager();
     * SizeClassifier classifier = new SizeClassifier(manager);
     * </pre>
     *
     * @param templateManager the structure template manager from ServerWorld
     * @throws IllegalArgumentException if templateManager is null
     */
    public SizeClassifier(StructureTemplateManager templateManager) {
        if (templateManager == null) {
            throw new IllegalArgumentException("StructureTemplateManager cannot be null");
        }
        this.templateManager = templateManager;
        LOGGER.debug("SizeClassifier initialized with StructureTemplateManager");
    }

    /**
     * Classifies a structure by SIZE based on bounding box volume.
     * <p>
     * This method attempts to calculate the precise volume using NBT template data.
     * If NBT is unavailable, it falls back to heuristic estimation based on the
     * structure identifier name.
     * <p>
     * <b>Algorithm:</b>
     * <ol>
     *   <li>Attempt to retrieve NBT template via StructureTemplateManager</li>
     *   <li>If found: Calculate volume from bounding box</li>
     *   <li>If not found: Estimate volume using name heuristics</li>
     *   <li>Categorize volume using {@link SizeCategory#fromVolume(int)}</li>
     * </ol>
     *
     * @param id the structure identifier (e.g., "minecraft:ancient_city")
     * @param structure the Structure object to classify
     * @return SIZE category (SMALL, MEDIUM, or LARGE)
     * @throws IllegalArgumentException if id or structure is null
     */
    public SizeCategory classify(Identifier id, Structure structure) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        if (structure == null) {
            throw new IllegalArgumentException("Structure cannot be null");
        }

        // Calculate volume using NBT template or fallback estimation
        int volume = calculateVolume(id, structure);

        // Convert volume to size category
        SizeCategory category = SizeCategory.fromVolume(volume);

        LOGGER.debug("Classified {} as {} ({} blocks³)", id, category, volume);

        return category;
    }

    /**
     * Gets the volume of a structure in blocks³.
     * <p>
     * This method exposes the volume calculation for use by {@link ClassificationSystem}
     * when creating {@link StructureClassification} records. It uses the same calculation
     * logic as {@link #classify(Identifier, Structure)}.
     * <p>
     * <b>Volume Calculation:</b>
     * <ul>
     *   <li>Attempts to calculate from NBT template (precise)</li>
     *   <li>Falls back to heuristic estimation if NBT unavailable</li>
     *   <li>Returns the same volume used for SIZE categorization</li>
     * </ul>
     *
     * @param id the structure identifier
     * @param structure the Structure object
     * @return the structure volume in blocks³
     * @throws IllegalArgumentException if id or structure is null
     */
    public int getVolume(Identifier id, Structure structure) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        if (structure == null) {
            throw new IllegalArgumentException("Structure cannot be null");
        }

        return calculateVolume(id, structure);
    }

    /**
     * Calculates structure volume using NBT template or fallback estimation.
     * <p>
     * This method first attempts to access the structure's NBT template to calculate
     * the precise bounding box volume. If NBT access fails (common for datapack-defined
     * or procedurally generated structures), it falls back to heuristic estimation.
     * <p>
     * <b>NBT Template Approach:</b>
     * <ul>
     *   <li>Attempt to load StructureTemplate via templateManager</li>
     *   <li>Extract bounding box from template</li>
     *   <li>Calculate: (width + 1) × (height + 1) × (depth + 1)</li>
     *   <li>Note: +1 accounts for inclusive bounding box coordinates</li>
     * </ul>
     * <p>
     * <b>Fallback Estimation:</b>
     * <ul>
     *   <li>Parse structure identifier for known keywords</li>
     *   <li>Use conservative estimates based on structure type</li>
     *   <li>Default to 10,000 blocks³ (MEDIUM) when uncertain</li>
     * </ul>
     *
     * @param id the structure identifier
     * @param structure the Structure object
     * @return calculated or estimated volume in blocks³
     */
    private int calculateVolume(Identifier id, Structure structure) {
        // Attempt to get NBT template
        Optional<StructureTemplate> templateOptional = getTemplate(id);

        if (templateOptional.isPresent()) {
            // NBT template available - calculate precise volume
            StructureTemplate template = templateOptional.get();
            Vec3i size = template.getSize();

            // Calculate volume from template dimensions
            // Note: Vec3i.getX/Y/Z returns dimension lengths (already calculated)
            int volume = size.getX() * size.getY() * size.getZ();

            if (volume > 0) {
                LOGGER.debug("Calculated volume for {} from NBT: {} blocks³", id, volume);
                return volume;
            } else {
                LOGGER.warn("Invalid NBT template size for {}: {}x{}x{}, using fallback estimation",
                    id, size.getX(), size.getY(), size.getZ());
                return estimateVolume(id);
            }
        } else {
            // NBT template unavailable - use fallback estimation
            LOGGER.debug("NBT template not available for {}, using fallback estimation", id);
            return estimateVolume(id);
        }
    }

    /**
     * Attempts to retrieve a StructureTemplate from the StructureTemplateManager.
     * <p>
     * This method wraps the template manager access in Optional handling to gracefully
     * deal with missing templates (common for datapack structures and procedurally
     * generated structures).
     * <p>
     * <b>Why templates might be unavailable:</b>
     * <ul>
     *   <li>Structure is defined purely in JSON (no NBT file)</li>
     *   <li>Structure is procedurally generated at runtime</li>
     *   <li>Datapack uses different template location</li>
     *   <li>Template file is corrupted or missing</li>
     * </ul>
     *
     * @param id the structure identifier
     * @return Optional containing the template if available, empty otherwise
     */
    private Optional<StructureTemplate> getTemplate(Identifier id) {
        try {
            // Attempt to retrieve template from manager
            // Note: getTemplate() may return null for structures without NBT templates
            Optional<StructureTemplate> template = templateManager.getTemplate(id);

            if (template.isPresent()) {
                LOGGER.trace("Successfully retrieved NBT template for {}", id);
            } else {
                LOGGER.trace("No NBT template found for {} (likely JSON-only or procedural)", id);
            }

            return template;
        } catch (Exception e) {
            // Handle any exceptions during template loading
            // This can occur with corrupted NBT files or access issues
            LOGGER.warn("Exception while accessing template for {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Estimates structure volume when NBT template is unavailable.
     * <p>
     * This fallback method uses heuristic-based estimation by parsing the structure
     * identifier for known keywords and returning conservative size estimates.
     * <p>
     * <b>Estimation Strategy:</b>
     * <ul>
     *   <li><b>Better to overestimate than underestimate</b> - avoids clutter</li>
     *   <li><b>Default to MEDIUM (10,000 blocks³)</b> - safe conservative fallback</li>
     *   <li><b>Log WARN for transparency</b> - users can see which structures are estimated</li>
     *   <li><b>Based on vanilla structures</b> - estimates aligned with known structures</li>
     * </ul>
     * <p>
     * <b>Heuristic Rules:</b>
     * <ul>
     *   <li><b>LARGE (> 32,768 blocks³):</b>
     *     <ul>
     *       <li>mansion: 100,000 blocks³</li>
     *       <li>village: 50,000 blocks³</li>
     *       <li>stronghold: 80,000 blocks³</li>
     *       <li>ancient_city: 120,000 blocks³</li>
     *       <li>fortress: 30,000 blocks³</li>
     *     </ul>
     *   </li>
     *   <li><b>MEDIUM (4,096 - 32,768 blocks³):</b>
     *     <ul>
     *       <li>temple: 20,000 blocks³</li>
     *       <li>monument: 25,000 blocks³</li>
     *       <li>pyramid: 18,000 blocks³</li>
     *       <li>dungeon: 15,000 blocks³</li>
     *     </ul>
     *   </li>
     *   <li><b>SMALL (< 4,096 blocks³):</b>
     *     <ul>
     *       <li>ruin: 2,000 blocks³</li>
     *       <li>shrine: 1,500 blocks³</li>
     *       <li>hut: 800 blocks³</li>
     *       <li>house: 1,200 blocks³</li>
     *     </ul>
     *   </li>
     *   <li><b>Default:</b> 10,000 blocks³ (MEDIUM) - conservative fallback</li>
     * </ul>
     *
     * @param id the structure identifier
     * @return estimated volume in blocks³
     */
    private int estimateVolume(Identifier id) {
        String name = id.getPath().toLowerCase();

        // Large structures (> 32,768 blocks³)
        if (name.contains("mansion")) {
            LOGGER.debug("Estimated {} as mansion: 100,000 blocks³", id);
            return 100_000;
        }
        if (name.contains("village")) {
            LOGGER.debug("Estimated {} as village: 50,000 blocks³", id);
            return 50_000;
        }
        if (name.contains("stronghold")) {
            LOGGER.debug("Estimated {} as stronghold: 80,000 blocks³", id);
            return 80_000;
        }
        if (name.contains("ancient_city")) {
            LOGGER.debug("Estimated {} as ancient city: 120,000 blocks³", id);
            return 120_000;
        }
        if (name.contains("fortress")) {
            LOGGER.debug("Estimated {} as fortress: 30,000 blocks³", id);
            return 30_000;
        }

        // Medium structures (4,096 - 32,768 blocks³)
        if (name.contains("temple")) {
            LOGGER.debug("Estimated {} as temple: 20,000 blocks³", id);
            return 20_000;
        }
        if (name.contains("monument")) {
            LOGGER.debug("Estimated {} as monument: 25,000 blocks³", id);
            return 25_000;
        }
        if (name.contains("pyramid")) {
            LOGGER.debug("Estimated {} as pyramid: 18,000 blocks³", id);
            return 18_000;
        }
        if (name.contains("dungeon")) {
            LOGGER.debug("Estimated {} as dungeon: 15,000 blocks³", id);
            return 15_000;
        }
        if (name.contains("outpost")) {
            LOGGER.debug("Estimated {} as outpost: 8,000 blocks³", id);
            return 8_000;
        }
        if (name.contains("shipwreck")) {
            LOGGER.debug("Estimated {} as shipwreck: 6,000 blocks³", id);
            return 6_000;
        }

        // Small structures (< 4,096 blocks³)
        if (name.contains("ruin")) {
            LOGGER.debug("Estimated {} as ruin: 2,000 blocks³", id);
            return 2_000;
        }
        if (name.contains("shrine")) {
            LOGGER.debug("Estimated {} as shrine: 1,500 blocks³", id);
            return 1_500;
        }
        if (name.contains("hut")) {
            LOGGER.debug("Estimated {} as hut: 800 blocks³", id);
            return 800;
        }
        if (name.contains("house")) {
            LOGGER.debug("Estimated {} as house: 1,200 blocks³", id);
            return 1_200;
        }

        // Default: Medium (conservative fallback)
        LOGGER.warn("No heuristic match for {}, defaulting to MEDIUM: 10,000 blocks³", id);
        return 10_000;
    }
}
