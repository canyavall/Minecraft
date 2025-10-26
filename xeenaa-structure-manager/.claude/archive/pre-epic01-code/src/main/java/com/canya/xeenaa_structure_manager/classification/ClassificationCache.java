package com.canya.xeenaa_structure_manager.classification;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe cache for structure classification results.
 * <p>
 * Classification is an expensive operation involving NBT analysis, heuristic detection,
 * and bounding box calculations. This cache stores results permanently, as structures
 * do not change at runtime.
 * <p>
 * Thread safety is critical because world loading is multi-threaded in Minecraft 1.21.1.
 * This cache uses {@link ConcurrentHashMap} to ensure safe concurrent access during
 * parallel classification.
 * <p>
 * Memory efficiency: Each classification entry is approximately 1KB (Identifier + enum values
 * + primitives), so 1000 structures consume ~1MB of memory - acceptable overhead.
 *
 * @see StructureClassification
 * @since 1.0.0
 */
public class ClassificationCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassificationCache.class);

    /**
     * Thread-safe cache mapping structure identifiers to their classifications.
     * <p>
     * ConcurrentHashMap provides:
     * <ul>
     *   <li>O(1) lookup performance</li>
     *   <li>Thread-safe concurrent reads and writes</li>
     *   <li>No locking required for reads</li>
     * </ul>
     */
    private final Map<Identifier, StructureClassification> cache = new ConcurrentHashMap<>();

    /**
     * Retrieves the classification for a structure.
     *
     * @param id the structure identifier
     * @return the cached classification, or null if not yet classified
     * @throws IllegalArgumentException if id is null
     */
    public StructureClassification get(Identifier id) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        return cache.get(id);
    }

    /**
     * Stores a classification result in the cache.
     * <p>
     * This method is thread-safe and can be called concurrently from multiple threads
     * during parallel classification.
     *
     * @param id the structure identifier
     * @param classification the classification result to cache
     * @throws IllegalArgumentException if id or classification is null
     */
    public void put(Identifier id, StructureClassification classification) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        if (classification == null) {
            throw new IllegalArgumentException("Classification cannot be null");
        }

        cache.put(id, classification);
        LOGGER.debug("Cached classification for {}: {}", id, classification.toCompactString());
    }

    /**
     * Gets the total number of cached classifications.
     *
     * @return the number of structures in the cache
     */
    public int size() {
        return cache.size();
    }

    /**
     * Checks if the cache contains a classification for the given structure.
     *
     * @param id the structure identifier to check
     * @return true if a classification exists for this structure
     * @throws IllegalArgumentException if id is null
     */
    public boolean contains(Identifier id) {
        if (id == null) {
            throw new IllegalArgumentException("Structure identifier cannot be null");
        }
        return cache.containsKey(id);
    }

    /**
     * Clears all cached classifications.
     * <p>
     * This should only be called when reloading world data or during testing.
     * Under normal operation, classifications are cached permanently.
     */
    public void clear() {
        int previousSize = cache.size();
        cache.clear();
        LOGGER.info("Cleared classification cache ({} entries)", previousSize);
    }

    /**
     * Gets statistics on size category distribution.
     * <p>
     * Returns a map showing how many structures fall into each size category:
     * <ul>
     *   <li>SMALL: count of small structures</li>
     *   <li>MEDIUM: count of medium structures</li>
     *   <li>LARGE: count of large structures</li>
     * </ul>
     *
     * @return unmodifiable map of size categories to structure counts
     */
    public Map<SizeCategory, Long> getSizeStatistics() {
        Map<SizeCategory, Long> stats = cache.values().stream()
            .collect(Collectors.groupingBy(
                StructureClassification::size,
                Collectors.counting()
            ));

        // Ensure all categories are present (even with 0 count)
        for (SizeCategory category : SizeCategory.values()) {
            stats.putIfAbsent(category, 0L);
        }

        return Collections.unmodifiableMap(stats);
    }

    /**
     * Gets statistics on type category distribution.
     * <p>
     * Returns a map showing how many structures fall into each type category:
     * <ul>
     *   <li>TOWN: count of town structures</li>
     *   <li>DUNGEON: count of dungeon structures</li>
     *   <li>TEMPLE: count of temple structures</li>
     *   <li>MINESHAFT: count of mineshaft structures</li>
     *   <li>SKY: count of sky structures</li>
     *   <li>RUIN: count of ruin structures</li>
     *   <li>UNKNOWN: count of unclassified structures</li>
     * </ul>
     *
     * @return unmodifiable map of type categories to structure counts
     */
    public Map<TypeCategory, Long> getTypeStatistics() {
        Map<TypeCategory, Long> stats = cache.values().stream()
            .collect(Collectors.groupingBy(
                StructureClassification::type,
                Collectors.counting()
            ));

        // Ensure all categories are present (even with 0 count)
        for (TypeCategory category : TypeCategory.values()) {
            stats.putIfAbsent(category, 0L);
        }

        return Collections.unmodifiableMap(stats);
    }

    /**
     * Gets the count of underground vs surface structures.
     *
     * @return array where [0] is surface count and [1] is underground count
     */
    public long[] getUndergroundStatistics() {
        long underground = cache.values().stream()
            .filter(StructureClassification::underground)
            .count();
        long surface = cache.size() - underground;

        return new long[] { surface, underground };
    }

    /**
     * Logs detailed cache statistics to the console.
     * <p>
     * This method provides transparency to users about how structures were classified.
     * Example output:
     * <pre>
     * Classification Statistics:
     *   Total structures: 247
     *   Size distribution: 89 small, 112 medium, 46 large
     *   Type distribution: 67 dungeons, 34 towns, 12 temples, 8 mineshafts, 126 other
     *   Location: 178 surface, 69 underground
     * </pre>
     */
    public void logStatistics() {
        Map<SizeCategory, Long> sizeStats = getSizeStatistics();
        Map<TypeCategory, Long> typeStats = getTypeStatistics();
        long[] undergroundStats = getUndergroundStatistics();

        LOGGER.info("Classification Statistics:");
        LOGGER.info("  Total structures: {}", size());
        LOGGER.info("  Size distribution: {} small, {} medium, {} large",
            sizeStats.get(SizeCategory.SMALL),
            sizeStats.get(SizeCategory.MEDIUM),
            sizeStats.get(SizeCategory.LARGE)
        );
        LOGGER.info("  Type distribution: {} dungeons, {} towns, {} temples, {} mineshafts, {} sky, {} ruins, {} unknown",
            typeStats.get(TypeCategory.DUNGEON),
            typeStats.get(TypeCategory.TOWN),
            typeStats.get(TypeCategory.TEMPLE),
            typeStats.get(TypeCategory.MINESHAFT),
            typeStats.get(TypeCategory.SKY),
            typeStats.get(TypeCategory.RUIN),
            typeStats.get(TypeCategory.UNKNOWN)
        );
        LOGGER.info("  Location: {} surface, {} underground",
            undergroundStats[0],
            undergroundStats[1]
        );
    }

    /**
     * Gets an unmodifiable view of all cached classifications.
     * <p>
     * This is useful for batch processing or configuration application.
     *
     * @return unmodifiable map of all cached classifications
     */
    public Map<Identifier, StructureClassification> getAll() {
        return Collections.unmodifiableMap(cache);
    }
}
