package com.canya.xeenaa_structure_manager.classification;

import net.minecraft.util.Identifier;

/**
 * Represents the two-dimensional classification of a Minecraft structure.
 * <p>
 * This record captures both the SIZE (physical footprint) and TYPE (behavioral category)
 * of a structure, along with supporting metadata for debugging and configuration.
 * <p>
 * The two-dimensional approach solves edge cases that single-dimension classification
 * cannot handle:
 * <ul>
 *   <li><b>Size alone fails</b>: Mineshafts are large but should stay common (not 2.5x spacing)</li>
 *   <li><b>Type alone fails</b>: Large dungeons vs small dungeons have different physical impact</li>
 *   <li><b>Combined approach works</b>: Handles all structure varieties correctly</li>
 * </ul>
 * <p>
 * Configuration priority system:
 * <pre>
 * Structure Override (manual config)
 *   ↓ (if not set)
 * Type-based multiplier (e.g., type.dungeon = 1.8x)
 *   ↓ (if not set or Type=UNKNOWN)
 * Size-based multiplier (e.g., size.large = 2.5x)
 *   ↓ (if not set)
 * Global multiplier (e.g., global = 1.0x)
 * </pre>
 *
 * @param size the size category based on bounding box volume
 * @param type the behavioral type category based on heuristic detection
 * @param volume the raw bounding box volume in blocks³
 * @param underground whether the structure generates primarily underground (Y &lt; 40)
 * @param id the structure identifier for debugging and config lookups
 * @see SizeCategory
 * @see TypeCategory
 * @since 1.0.0
 */
public record StructureClassification(
    SizeCategory size,
    TypeCategory type,
    int volume,
    boolean underground,
    Identifier id
) {
    /**
     * Creates a structure classification with validation.
     *
     * @param size the size category based on bounding box volume
     * @param type the behavioral type category based on heuristic detection
     * @param volume the raw bounding box volume in blocks³
     * @param underground whether the structure generates primarily underground
     * @param id the structure identifier
     * @throws IllegalArgumentException if size is null, type is null, volume is negative, or id is null
     */
    public StructureClassification {
        if (size == null) {
            throw new IllegalArgumentException("Size category cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type category cannot be null");
        }
        if (volume < 0) {
            throw new IllegalArgumentException("Volume cannot be negative: " + volume);
        }
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
    }

    /**
     * Returns a human-readable string representation of this classification.
     * <p>
     * Format: "StructureId: SIZE TYPE (volume m³, [underground|surface])"
     * <p>
     * Example: "minecraft:ancient_city: LARGE DUNGEON (500000 m³, underground)"
     *
     * @return formatted classification string
     */
    @Override
    public String toString() {
        return String.format("%s: %s %s (%d m³, %s)",
            id,
            size,
            type,
            volume,
            underground ? "underground" : "surface"
        );
    }

    /**
     * Creates a compact summary string for logging large batches of classifications.
     * <p>
     * Format: "SIZE/TYPE"
     * <p>
     * Example: "LARGE/DUNGEON"
     *
     * @return compact classification summary
     */
    public String toCompactString() {
        return size + "/" + type;
    }
}
