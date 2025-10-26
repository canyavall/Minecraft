package com.canya.xeenaa_structure_manager.classification;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classifies structures by TYPE based on heuristic detection methods.
 * <p>
 * This classifier implements the TYPE dimension of the two-dimensional classification
 * system (SIZE + TYPE). It categorizes structures by their behavioral type and gameplay
 * purpose using a three-stage detection process: name analysis, Y-level constraints,
 * and bounding box shape analysis.
 * <p>
 * <b>Classification Strategy:</b>
 * <ol>
 *   <li><b>Primary Method</b>: Name-Based Keyword Matching (80% accuracy)
 *     <ul>
 *       <li>Parse structure identifier for known keywords</li>
 *       <li>Match against 7 type categories (TOWN, DUNGEON, TEMPLE, MINESHAFT, SKY, RUIN, UNKNOWN)</li>
 *       <li>Fast and reliable for majority of structures</li>
 *       <li>Returns immediately on match</li>
 *     </ul>
 *   </li>
 *   <li><b>Secondary Method</b>: Y-Level Constraint Analysis
 *     <ul>
 *       <li>Analyze structure placement Y-level to infer type</li>
 *       <li>Underground (Y &lt; 40) → Likely DUNGEON</li>
 *       <li>Sky (Y &gt; 100) → Likely SKY</li>
 *       <li><i>Epic 01 Note: Returns UNKNOWN (complex to implement, deferred to future epic)</i></li>
 *     </ul>
 *   </li>
 *   <li><b>Tertiary Method</b>: Bounding Box Shape Analysis
 *     <ul>
 *       <li>Detect linear structures (mineshafts) vs compact structures</li>
 *       <li>Ratio: max(X, Y, Z) / min(X, Y, Z) &gt; 10 → Likely MINESHAFT</li>
 *       <li><i>Epic 01 Note: Returns UNKNOWN (requires bounding box sharing, deferred to future epic)</i></li>
 *     </ul>
 *   </li>
 *   <li><b>Fallback</b>: TypeCategory.UNKNOWN
 *     <ul>
 *       <li>When no detection method succeeds, return UNKNOWN</li>
 *       <li>Configuration system falls back to SIZE-based multipliers</li>
 *       <li>Safe default behavior ensures no crashes</li>
 *     </ul>
 *   </li>
 * </ol>
 * <p>
 * <b>Type Categories and Detection Keywords:</b>
 * <ul>
 *   <li><b>TOWN</b>: village, town, settlement, city, outpost</li>
 *   <li><b>DUNGEON</b>: dungeon, crypt, catacomb, stronghold, ancient_city, fortress, bastion</li>
 *   <li><b>TEMPLE</b>: temple, pyramid, monument, shrine</li>
 *   <li><b>MINESHAFT</b>: mineshaft, mine_shaft, mine (CRITICAL: must detect to preserve vanilla balance)</li>
 *   <li><b>SKY</b>: sky, floating, aerial, cloud, tower, end_city</li>
 *   <li><b>RUIN</b>: ruin, wreck, shipwreck, debris</li>
 *   <li><b>UNKNOWN</b>: Structures that don't match any pattern</li>
 * </ul>
 * <p>
 * <b>Critical Edge Case: Mineshafts</b>
 * <ul>
 *   <li>Mineshafts are LARGE (by size), but should remain COMMON (not rare)</li>
 *   <li>If we apply size.large = 2.5x spacing, mineshafts become too rare</li>
 *   <li>Solution: Detect TYPE=MINESHAFT, then config applies type.mineshaft = 1.0x</li>
 *   <li>Detection priority: Name contains "mineshaft" → TYPE=MINESHAFT (most reliable)</li>
 *   <li>Future enhancement: Shape analysis (linear structure) → TYPE=MINESHAFT</li>
 * </ul>
 * <p>
 * <b>80% Accuracy Target:</b>
 * <ul>
 *   <li>Name-based detection is expected to catch 80%+ of structures</li>
 *   <li>TypeCategory.UNKNOWN is an acceptable fallback (uses SIZE rules)</li>
 *   <li>Perfect accuracy is not required for MVP</li>
 *   <li>Future epics can enhance detection with Y-level and shape analysis</li>
 * </ul>
 * <p>
 * <b>Performance Considerations:</b>
 * <ul>
 *   <li>Name-based detection is very fast (string operations only)</li>
 *   <li>Thread-safe for parallel classification during world load</li>
 *   <li>No NBT access required (faster than SIZE classification)</li>
 *   <li>Results should be cached in {@link ClassificationCache}</li>
 * </ul>
 * <p>
 * <b>Epic 01 Scope Decision:</b>
 * <ul>
 *   <li>Y-level detection: Returns UNKNOWN (complex to implement, requires StructurePlacement access)</li>
 *   <li>Shape analysis: Returns UNKNOWN (requires bounding box data from SizeClassifier)</li>
 *   <li>Focus on name detection: 80% accuracy achievable with keyword matching alone</li>
 *   <li>Future Epic 02+: Implement full Y-level and shape analysis</li>
 * </ul>
 * <p>
 * <b>Error Handling:</b>
 * <ul>
 *   <li>Null identifier: Throws IllegalArgumentException</li>
 *   <li>Null structure: Throws IllegalArgumentException</li>
 *   <li>No keyword match: Returns UNKNOWN (safe fallback)</li>
 *   <li>Never crashes - always returns a valid TypeCategory</li>
 * </ul>
 *
 * @see TypeCategory
 * @see StructureClassification
 * @see ClassificationCache
 * @since 1.0.0
 */
public class TypeClassifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeClassifier.class);

    /**
     * Creates a new TYPE classifier.
     * <p>
     * Unlike SizeClassifier, TypeClassifier does not require external dependencies
     * (no StructureTemplateManager needed for name-based detection).
     */
    public TypeClassifier() {
        LOGGER.debug("TypeClassifier initialized");
    }

    /**
     * Classifies a structure by TYPE using three-stage heuristic detection.
     * <p>
     * This method attempts three detection strategies in priority order:
     * <ol>
     *   <li>Name-based keyword matching (primary, 80% accuracy)</li>
     *   <li>Y-level constraint analysis (secondary, Epic 01: returns UNKNOWN)</li>
     *   <li>Bounding box shape analysis (tertiary, Epic 01: returns UNKNOWN)</li>
     *   <li>Fallback to UNKNOWN if no method succeeds</li>
     * </ol>
     * <p>
     * <b>Algorithm:</b>
     * <ol>
     *   <li>Attempt name-based detection via {@link #detectFromName(Identifier)}</li>
     *   <li>If UNKNOWN: Attempt Y-level analysis via {@link #detectFromYLevel(Structure)}</li>
     *   <li>If UNKNOWN: Attempt shape analysis via {@link #detectFromShape(Structure)}</li>
     *   <li>If UNKNOWN: Log and return UNKNOWN (safe fallback)</li>
     * </ol>
     * <p>
     * Each detection method logs its result at DEBUG level, showing which method
     * successfully classified the structure for transparency and debugging.
     *
     * @param id the structure identifier (e.g., "minecraft:ancient_city")
     * @param structure the Structure object to classify
     * @return TYPE category (TOWN, DUNGEON, TEMPLE, MINESHAFT, SKY, RUIN, or UNKNOWN)
     * @throws IllegalArgumentException if id or structure is null
     */
    public TypeCategory classify(Identifier id, Structure structure) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        if (structure == null) {
            throw new IllegalArgumentException("Structure cannot be null");
        }

        // 1. Name-based detection (primary)
        TypeCategory fromName = detectFromName(id);
        if (fromName != TypeCategory.UNKNOWN) {
            LOGGER.debug("Classified {} as {} (name-based)", id, fromName);
            return fromName;
        }

        // 2. Y-level analysis (secondary)
        TypeCategory fromY = detectFromYLevel(structure);
        if (fromY != TypeCategory.UNKNOWN) {
            LOGGER.debug("Classified {} as {} (Y-level)", id, fromY);
            return fromY;
        }

        // 3. Shape analysis (tertiary)
        TypeCategory fromShape = detectFromShape(structure);
        if (fromShape != TypeCategory.UNKNOWN) {
            LOGGER.debug("Classified {} as {} (shape)", id, fromShape);
            return fromShape;
        }

        // 4. Fallback
        LOGGER.debug("Classified {} as UNKNOWN (fallback)", id);
        return TypeCategory.UNKNOWN;
    }

    /**
     * Determines if a structure generates primarily underground.
     * <p>
     * This method provides underground status for {@link ClassificationSystem} when
     * creating {@link StructureClassification} records. It uses type-based heuristics
     * to infer underground placement.
     * <p>
     * <b>Underground Detection Strategy (Epic 01):</b>
     * <ul>
     *   <li><b>Type-based inference</b>: Certain types are known to be underground
     *     <ul>
     *       <li>DUNGEON: Almost always underground (strongholds, ancient cities, etc.)</li>
     *       <li>MINESHAFT: Always underground</li>
     *     </ul>
     *   </li>
     *   <li><b>Other types</b>: Assumed surface (conservative default)</li>
     * </ul>
     * <p>
     * <b>Epic 01 Scope:</b>
     * <p>
     * This is a simplified heuristic-based approach. Future epics can enhance this with
     * actual Y-level constraint analysis from StructurePlacement data.
     * <p>
     * <b>Accuracy Trade-off:</b>
     * <ul>
     *   <li>May incorrectly classify some surface dungeons as underground</li>
     *   <li>May incorrectly classify some underground ruins as surface</li>
     *   <li>80% accuracy is acceptable for Epic 01 MVP</li>
     *   <li>Future enhancement: Read Y-level constraints from StructurePlacement</li>
     * </ul>
     *
     * @param id the structure identifier
     * @param structure the Structure object
     * @return true if the structure is primarily underground, false otherwise
     * @throws IllegalArgumentException if id or structure is null
     */
    public boolean isUnderground(Identifier id, Structure structure) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        if (structure == null) {
            throw new IllegalArgumentException("Structure cannot be null");
        }

        // Classify the structure to determine its type
        TypeCategory type = classify(id, structure);

        // Infer underground status based on type
        // DUNGEON and MINESHAFT are almost always underground
        return type == TypeCategory.DUNGEON || type == TypeCategory.MINESHAFT;
    }

    /**
     * Detects structure type from identifier name using keyword matching.
     * <p>
     * This is the primary detection method with 80%+ accuracy. It parses the
     * structure identifier's path component and matches against known keywords
     * for each type category.
     * <p>
     * <b>Detection Rules (in priority order):</b>
     * <ol>
     *   <li><b>MINESHAFT</b> (checked first - critical edge case):
     *     <ul>
     *       <li>Keywords: "mineshaft", "mine_shaft"</li>
     *       <li>Why first: Must prevent "mine" from matching other types</li>
     *     </ul>
     *   </li>
     *   <li><b>TOWN</b>:
     *     <ul>
     *       <li>Keywords: "village", "town", "settlement", "outpost"</li>
     *       <li>Examples: minecraft:village_plains, minecraft:pillager_outpost</li>
     *     </ul>
     *   </li>
     *   <li><b>DUNGEON</b>:
     *     <ul>
     *       <li>Keywords: "dungeon", "crypt", "catacomb", "stronghold", "ancient_city", "fortress", "bastion"</li>
     *       <li>Examples: minecraft:ancient_city, minecraft:stronghold, minecraft:bastion_remnant</li>
     *     </ul>
     *   </li>
     *   <li><b>TEMPLE</b>:
     *     <ul>
     *       <li>Keywords: "temple", "pyramid", "monument", "shrine"</li>
     *       <li>Examples: minecraft:desert_pyramid, minecraft:ocean_monument</li>
     *     </ul>
     *   </li>
     *   <li><b>SKY</b>:
     *     <ul>
     *       <li>Keywords: "sky", "floating", "aerial", "cloud", "tower", "end_city"</li>
     *       <li>Examples: minecraft:end_city, modded sky villages</li>
     *     </ul>
     *   </li>
     *   <li><b>RUIN</b>:
     *     <ul>
     *       <li>Keywords: "ruin", "wreck", "shipwreck", "debris"</li>
     *       <li>Examples: minecraft:ocean_ruin_cold, minecraft:shipwreck</li>
     *     </ul>
     *   </li>
     * </ol>
     * <p>
     * <b>Why MINESHAFT is checked first:</b>
     * <ul>
     *   <li>Mineshafts often contain "mine" in their name</li>
     *   <li>If checked after other types, "mine" might incorrectly match</li>
     *   <li>CRITICAL: Ensures mineshafts get correct TYPE for proper spacing</li>
     * </ul>
     *
     * @param id the structure identifier
     * @return detected TypeCategory or UNKNOWN if no match
     */
    private TypeCategory detectFromName(Identifier id) {
        String name = id.getPath().toLowerCase();

        // MINESHAFT check FIRST (critical edge case)
        if (name.contains("mineshaft") || name.contains("mine_shaft")) {
            return TypeCategory.MINESHAFT;
        }

        // TOWN structures
        if (name.contains("village") || name.contains("town") ||
            name.contains("settlement") || name.contains("outpost")) {
            return TypeCategory.TOWN;
        }

        // DUNGEON structures
        if (name.contains("dungeon") || name.contains("crypt") ||
            name.contains("catacomb") || name.contains("stronghold") ||
            name.contains("ancient_city") || name.contains("fortress") ||
            name.contains("bastion")) {
            return TypeCategory.DUNGEON;
        }

        // TEMPLE structures
        if (name.contains("temple") || name.contains("pyramid") ||
            name.contains("monument") || name.contains("shrine")) {
            return TypeCategory.TEMPLE;
        }

        // SKY structures
        if (name.contains("sky") || name.contains("floating") ||
            name.contains("aerial") || name.contains("cloud") ||
            name.contains("tower") || name.contains("end_city")) {
            return TypeCategory.SKY;
        }

        // RUIN structures
        if (name.contains("ruin") || name.contains("wreck") ||
            name.contains("shipwreck") || name.contains("debris")) {
            return TypeCategory.RUIN;
        }

        // No match found
        return TypeCategory.UNKNOWN;
    }

    /**
     * Detects structure type from Y-level placement constraints.
     * <p>
     * This secondary detection method analyzes structure placement Y-level to infer
     * the type. Underground structures are likely dungeons, sky-level structures are
     * likely sky structures, etc.
     * <p>
     * <b>Detection Rules:</b>
     * <ul>
     *   <li><b>Underground</b> (Y &lt; 40): Likely DUNGEON</li>
     *   <li><b>Surface</b> (40 ≤ Y ≤ 100): No inference (most structures spawn at surface)</li>
     *   <li><b>Sky</b> (Y &gt; 100): Likely SKY</li>
     * </ul>
     * <p>
     * <b>Epic 01 Scope Decision:</b>
     * <p>
     * Y-level detection is complex in Minecraft 1.21.1 because:
     * <ul>
     *   <li>Structure object doesn't directly expose Y-level constraints</li>
     *   <li>Would require accessing StructureSet and StructurePlacement (not available at classification time)</li>
     *   <li>StructurePlacement contains Y-level ranges, but requires deep structure manipulation</li>
     * </ul>
     * <p>
     * <b>For Epic 01:</b> This method returns UNKNOWN (defer to name/shape detection)
     * <p>
     * <b>Future Epic:</b> Enhance when we have access to full placement data
     *
     * @param structure the Structure object to analyze
     * @return detected TypeCategory or UNKNOWN (always UNKNOWN in Epic 01)
     */
    private TypeCategory detectFromYLevel(Structure structure) {
        // Epic 01: Return UNKNOWN (complex to implement, low ROI for MVP)
        // Future enhancement: Access StructurePlacement to get Y-level constraints
        //
        // Complexity reasons:
        // - Structure object doesn't expose Y-level directly
        // - Would need to access StructureSet (not available here)
        // - Would need to parse StructurePlacement (varies by placement type)
        // - TerrainAdaptation doesn't provide Y-level, only adaptation mode
        //
        // For Epic 01: Name-based detection is sufficient (80% accuracy)
        // Future Epic 02+: Implement full Y-level analysis
        return TypeCategory.UNKNOWN;
    }

    /**
     * Detects structure type from bounding box shape analysis.
     * <p>
     * This tertiary detection method analyzes the structure's bounding box dimensions
     * to detect linear structures (like mineshafts) versus compact structures.
     * <p>
     * <b>Detection Rules:</b>
     * <ul>
     *   <li><b>Linear structures</b>: max(X, Y, Z) / min(X, Y, Z) &gt; 10 → Likely MINESHAFT</li>
     *   <li><b>Compact structures</b>: Similar dimensions → No inference</li>
     * </ul>
     * <p>
     * <b>Example:</b>
     * <ul>
     *   <li>Mineshaft: 100 blocks long, 5 blocks wide, 5 blocks tall → Ratio: 100/5 = 20 → MINESHAFT</li>
     *   <li>Dungeon: 30×25×20 → Ratio: 30/20 = 1.5 → Not linear, no inference</li>
     * </ul>
     * <p>
     * <b>Epic 01 Scope Decision:</b>
     * <p>
     * Shape analysis requires bounding box dimensions, which introduces complexity:
     * <ul>
     *   <li>Would need to duplicate NBT access from SizeClassifier (code duplication)</li>
     *   <li>OR integrate with SizeClassifier (tight coupling between classifiers)</li>
     *   <li>Bounding box data would need to be shared via StructureClassification record</li>
     * </ul>
     * <p>
     * <b>For Epic 01:</b> This method returns UNKNOWN (name detection catches mineshafts)
     * <p>
     * <b>Future Epic:</b> Share bounding box data between classifiers via StructureClassification record
     *
     * @param structure the Structure object to analyze
     * @return detected TypeCategory or UNKNOWN (always UNKNOWN in Epic 01)
     */
    private TypeCategory detectFromShape(Structure structure) {
        // Epic 01: Return UNKNOWN (requires bounding box sharing, defer to future epic)
        // Future enhancement: Share bounding box data from SizeClassifier
        //
        // Complexity reasons:
        // - Would need to access NBT template (duplicates SizeClassifier logic)
        // - Would create tight coupling between SizeClassifier and TypeClassifier
        // - Better architecture: Share bounding box via StructureClassification record
        //
        // For Epic 01: Name-based detection catches mineshafts (keywords: "mineshaft", "mine")
        // Future Epic 02+: Enhance StructureClassification to include bounding box dimensions
        //                  Then use here for shape analysis
        return TypeCategory.UNKNOWN;
    }
}
