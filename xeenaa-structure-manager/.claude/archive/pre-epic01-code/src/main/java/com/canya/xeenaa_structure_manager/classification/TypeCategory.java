package com.canya.xeenaa_structure_manager.classification;

/**
 * Categorizes structures by their behavioral type and gameplay purpose.
 * <p>
 * Type classification is the second dimension of the two-dimensional classification system
 * (SIZE + TYPE). Type determines the gameplay behavior and purpose of structures, which
 * influences spacing more accurately than size alone.
 * <p>
 * Type-based multipliers take priority over size-based multipliers in the configuration
 * priority system (Type &gt; Size &gt; Global).
 *
 * @see SizeCategory
 * @see StructureClassification
 * @since 1.0.0
 */
public enum TypeCategory {
    /**
     * Towns and settlements - inhabited areas with buildings and villagers.
     * <p>
     * Detection heuristics:
     * <ul>
     *   <li>Name contains: "village", "town", "settlement"</li>
     *   <li>Surface structures (Y &gt; 40)</li>
     *   <li>Multiple buildings clustered together</li>
     * </ul>
     * <p>
     * Examples: Plains villages, desert towns, taiga settlements
     * <p>
     * Default spacing multiplier: 2.0x (towns should be spread out)
     */
    TOWN,

    /**
     * Dungeons and underground combat structures.
     * <p>
     * Detection heuristics:
     * <ul>
     *   <li>Name contains: "dungeon", "crypt", "catacomb", "fortress", "stronghold", "keep", "castle"</li>
     *   <li>Underground placement (Y &lt; 40)</li>
     *   <li>Contains spawners or hostile mob features</li>
     * </ul>
     * <p>
     * Examples: Ancient cities, strongholds, dungeons, crypts
     * <p>
     * Default spacing multiplier: 1.8x (rarer than size alone suggests, but not extreme)
     */
    DUNGEON,

    /**
     * Temples, monuments, and above-ground landmarks.
     * <p>
     * Detection heuristics:
     * <ul>
     *   <li>Name contains: "temple", "monument", "pyramid"</li>
     *   <li>Surface or elevated structures</li>
     *   <li>Biome-specific placement</li>
     * </ul>
     * <p>
     * Examples: Desert temples, jungle temples, ocean monuments
     * <p>
     * Default spacing multiplier: 2.2x (quite rare, significant landmarks)
     */
    TEMPLE,

    /**
     * Mineshafts and linear mining structures.
     * <p>
     * Detection heuristics:
     * <ul>
     *   <li>Name contains: "mineshaft", "mine", "shaft"</li>
     *   <li>Linear/branching bounding box shape (aspect ratio &gt; 10)</li>
     *   <li>Underground placement</li>
     * </ul>
     * <p>
     * Examples: Abandoned mineshafts, mine tunnels
     * <p>
     * Default spacing multiplier: 1.0x (CRITICAL: mineshafts are large but must stay common)
     * <p>
     * Note: This is a critical edge case. Size-only classification would make mineshafts
     * extremely rare (2.5x from size.large), breaking vanilla balance. Type detection
     * ensures mineshafts use 1.0x multiplier regardless of physical size.
     */
    MINESHAFT,

    /**
     * Sky and aerial structures.
     * <p>
     * Detection heuristics:
     * <ul>
     *   <li>Name contains: "sky", "floating", "aerial", "airship"</li>
     *   <li>High altitude placement (Y &gt; 100)</li>
     * </ul>
     * <p>
     * Examples: Sky villages, floating islands, airships
     * <p>
     * Default spacing multiplier: 1.5x (moderate spacing for aerial structures)
     */
    SKY,

    /**
     * Ruins and scattered debris structures.
     * <p>
     * Detection heuristics:
     * <ul>
     *   <li>Name contains: "ruin", "remnant", "wreckage"</li>
     *   <li>Small to medium size typically</li>
     *   <li>Scattered placement pattern</li>
     * </ul>
     * <p>
     * Examples: Ocean ruins, nether ruins, ruined portals
     * <p>
     * Default spacing multiplier: 1.3x (scattered but findable)
     */
    RUIN,

    /**
     * Unknown structure type - fallback when type cannot be determined.
     * <p>
     * When a structure is classified as UNKNOWN, the configuration system falls back
     * to size-based multipliers. This ensures safe defaults for structures that don't
     * match any heuristic detection patterns.
     * <p>
     * Detection: Used when no other type category matches
     * <p>
     * Default behavior: Uses size-based multipliers instead of type-based
     */
    UNKNOWN
}
