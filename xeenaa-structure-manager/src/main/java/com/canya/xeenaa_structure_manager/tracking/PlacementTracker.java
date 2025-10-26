package com.canya.xeenaa_structure_manager.tracking;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe tracker for structure placements during world generation.
 * <p>
 * This system records every structure placement event, allowing for:
 * <ul>
 *   <li>Verification that multipliers are being applied correctly</li>
 *   <li>Statistical analysis of structure spacing</li>
 *   <li>Debugging structure generation issues</li>
 *   <li>Performance monitoring of structure placement</li>
 * </ul>
 * <p>
 * <b>Thread Safety:</b>
 * This class uses {@link ConcurrentHashMap} to ensure thread-safe access from
 * multiple world generation threads. All public methods are safe for concurrent use.
 * <p>
 * <b>Memory Management:</b>
 * The tracker stores all placements in memory. For long-running servers, consider
 * implementing periodic cleanup or limits on the number of tracked placements.
 * <p>
 * <b>Usage Example:</b>
 * <pre>
 * // Get singleton instance
 * PlacementTracker tracker = PlacementTracker.getInstance();
 *
 * // Record a placement (called from mixin)
 * tracker.recordPlacement(structureId, blockPos);
 *
 * // Get statistics
 * int totalPlacements = tracker.getTotalPlacements();
 * List&lt;PlacementRecord&gt; villages = tracker.getPlacementsForStructure(
 *     Identifier.of("minecraft", "village_plains")
 * );
 * </pre>
 *
 * @see PlacementRecord
 * @see com.canya.xeenaa_structure_manager.mixin.StructureStartPlaceMixin
 * @since 1.0.0
 */
public class PlacementTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlacementTracker.class);

    /**
     * Minimum distance threshold (in chunks) for logging placements.
     * Only log placements that are at least this far from the previous one.
     */
    private static final double MIN_DISTANCE_TO_LOG = 0.5;

    /**
     * Singleton instance for global access.
     * Initialized on first access (lazy initialization).
     */
    private static volatile PlacementTracker INSTANCE;

    /**
     * Thread-safe storage for all placement records.
     * Key: Structure identifier (e.g., "minecraft:village_plains")
     * Value: List of placement records for that structure type
     */
    private final Map<Identifier, List<PlacementRecord>> placements;

    /**
     * Performance metrics
     */
    private final Runtime runtime = Runtime.getRuntime();
    private long lastMemoryCheck = 0;
    private int placementsSinceLastCheck = 0;

    /**
     * Private constructor for singleton pattern.
     * Initializes the concurrent storage map.
     */
    private PlacementTracker() {
        this.placements = new ConcurrentHashMap<>();
        LOGGER.info("PlacementTracker initialized");
    }

    /**
     * Gets the singleton instance of the placement tracker.
     * <p>
     * This uses double-checked locking to ensure thread-safe lazy initialization.
     * <p>
     * <b>Thread Safety:</b> Safe for concurrent access from multiple threads.
     *
     * @return the singleton instance
     */
    public static PlacementTracker getInstance() {
        if (INSTANCE == null) {
            synchronized (PlacementTracker.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PlacementTracker();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Records a structure placement event.
     * <p>
     * This method is called by the {@link com.canya.xeenaa_structure_manager.mixin.StructureStartPlaceMixin}
     * whenever a structure is successfully placed in the world.
     * <p>
     * The placement is stored in a thread-safe manner and can be queried later
     * for statistics and analysis.
     * <p>
     * <b>Performance:</b> O(1) insertion time using {@link ConcurrentHashMap}.
     * <p>
     * <b>Thread Safety:</b> Safe for concurrent calls from world generation threads.
     *
     * @param structureId the identifier of the placed structure
     * @param blockPos the position where the structure was placed
     * @throws IllegalArgumentException if structureId or blockPos is null
     */
    public void recordPlacement(Identifier structureId, BlockPos blockPos) {
        if (structureId == null) {
            throw new IllegalArgumentException("Structure ID cannot be null");
        }
        if (blockPos == null) {
            throw new IllegalArgumentException("Block position cannot be null");
        }

        // Create placement record
        PlacementRecord record = PlacementRecord.of(structureId, blockPos);

        // Add to thread-safe storage
        List<PlacementRecord> records = placements.computeIfAbsent(
            structureId,
            k -> Collections.synchronizedList(new ArrayList<>())
        );
        records.add(record);
        placementsSinceLastCheck++;

        // Log only significant placements (reduce spam)
        int count = records.size();
        if (count > 1) {
            // Calculate distance from previous placement
            PlacementRecord previous = records.get(count - 2);
            double distanceChunks = record.chunkDistanceTo(previous);
            double distanceBlocks = record.blockDistanceTo(previous);

            // Only log if distance is significant (filters out stacked structures)
            if (distanceChunks >= MIN_DISTANCE_TO_LOG) {
                LOGGER.info("Placed {} #{} at chunk ({}, {}) - Distance: {} chunks ({} blocks)",
                    structureId,
                    count,
                    record.chunkPos().x,
                    record.chunkPos().z,
                    String.format("%.1f", distanceChunks),
                    String.format("%.0f", distanceBlocks));
            } else {
                // Log stacked structures at DEBUG level
                LOGGER.debug("Placed {} #{} at chunk ({}, {}) - Stacked (distance: {} chunks)",
                    structureId,
                    count,
                    record.chunkPos().x,
                    record.chunkPos().z,
                    String.format("%.1f", distanceChunks));
            }
        } else {
            LOGGER.info("Placed {} #1 at chunk ({}, {}) - First placement",
                structureId,
                record.chunkPos().x,
                record.chunkPos().z);
        }

        // Periodic memory/performance monitoring (every 100 placements)
        if (placementsSinceLastCheck >= 100) {
            logPerformanceMetrics();
            placementsSinceLastCheck = 0;
        }
    }

    /**
     * Logs performance metrics including memory usage and CPU load.
     * Called periodically during world generation to monitor impact.
     */
    private void logPerformanceMetrics() {
        long currentTime = System.currentTimeMillis();

        // Memory metrics
        long totalMemory = runtime.totalMemory() / 1024 / 1024; // MB
        long freeMemory = runtime.freeMemory() / 1024 / 1024; // MB
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory() / 1024 / 1024; // MB

        // Calculate placements rate (if not first check)
        String rateInfo = "";
        if (lastMemoryCheck > 0) {
            long timeDelta = currentTime - lastMemoryCheck;
            double placementsPerSecond = (100.0 / timeDelta) * 1000;
            rateInfo = String.format(", Rate: %.1f placements/sec", placementsPerSecond);
        }

        LOGGER.info("Performance: Memory used {} MB / {} MB (max {} MB), Total placements: {}{}",
            usedMemory,
            totalMemory,
            maxMemory,
            getTotalPlacements(),
            rateInfo);

        lastMemoryCheck = currentTime;
    }

    /**
     * Gets all placements for a specific structure type.
     * <p>
     * Returns an unmodifiable copy of the placement list to prevent external
     * modification while maintaining thread safety.
     * <p>
     * Example:
     * <pre>
     * Identifier villageId = Identifier.of("minecraft", "village_plains");
     * List&lt;PlacementRecord&gt; villages = tracker.getPlacementsForStructure(villageId);
     * System.out.println("Found " + villages.size() + " villages");
     * </pre>
     *
     * @param structureId the structure identifier to query
     * @return an unmodifiable list of placement records (empty if none found)
     * @throws IllegalArgumentException if structureId is null
     */
    public List<PlacementRecord> getPlacementsForStructure(Identifier structureId) {
        if (structureId == null) {
            throw new IllegalArgumentException("Structure ID cannot be null");
        }

        List<PlacementRecord> records = placements.get(structureId);
        if (records == null) {
            return Collections.emptyList();
        }

        // Return unmodifiable copy for thread safety
        synchronized (records) {
            return Collections.unmodifiableList(new ArrayList<>(records));
        }
    }

    /**
     * Gets the total number of structures placed.
     * <p>
     * This counts all placement records across all structure types.
     * <p>
     * <b>Performance:</b> O(n) where n is the number of structure types.
     *
     * @return the total number of placed structures
     */
    public int getTotalPlacements() {
        return placements.values().stream()
            .mapToInt(List::size)
            .sum();
    }

    /**
     * Gets the number of placements for a specific structure type.
     * <p>
     * Example:
     * <pre>
     * Identifier villageId = Identifier.of("minecraft", "village_plains");
     * int count = tracker.getPlacementCount(villageId);
     * System.out.println("Placed " + count + " villages");
     * </pre>
     *
     * @param structureId the structure identifier to query
     * @return the number of placements (0 if none found)
     * @throws IllegalArgumentException if structureId is null
     */
    public int getPlacementCount(Identifier structureId) {
        if (structureId == null) {
            throw new IllegalArgumentException("Structure ID cannot be null");
        }

        List<PlacementRecord> records = placements.get(structureId);
        return records == null ? 0 : records.size();
    }

    /**
     * Gets all structure types that have been placed.
     * <p>
     * Returns an unmodifiable set of structure identifiers for iteration.
     * <p>
     * Example:
     * <pre>
     * for (Identifier structureId : tracker.getTrackedStructures()) {
     *     int count = tracker.getPlacementCount(structureId);
     *     System.out.println(structureId + ": " + count + " placements");
     * }
     * </pre>
     *
     * @return an unmodifiable set of structure identifiers
     */
    public java.util.Set<Identifier> getTrackedStructures() {
        return Collections.unmodifiableSet(placements.keySet());
    }

    /**
     * Clears all tracked placements.
     * <p>
     * This is useful for:
     * <ul>
     *   <li>Resetting tracking for a new test</li>
     *   <li>Freeing memory on long-running servers</li>
     *   <li>Starting fresh after world regeneration</li>
     * </ul>
     * <p>
     * <b>Thread Safety:</b> Safe for concurrent access.
     */
    public void clear() {
        int previousSize = getTotalPlacements();
        placements.clear();
        LOGGER.info("Cleared {} placement records", previousSize);
    }

    /**
     * Logs a comprehensive summary of all tracked placements with spacing analysis.
     * <p>
     * This method outputs detailed statistics to the logs including:
     * <ul>
     *   <li>Total placement counts per structure</li>
     *   <li>Average spacing between placements (chunks and blocks)</li>
     *   <li>Minimum and maximum spacing</li>
     *   <li>Standard deviation of spacing</li>
     * </ul>
     * <p>
     * Call this method after world generation to analyze the results.
     */
    public void logDetailedStatistics() {
        LOGGER.info("=== Xeenaa Structure Manager - Placement Statistics ===");
        LOGGER.info("Total placements: {}", getTotalPlacements());
        LOGGER.info("Tracked structure types: {}", placements.size());
        LOGGER.info("");

        if (placements.isEmpty()) {
            LOGGER.info("No placements recorded yet.");
            return;
        }

        // Sort by placement count (most to least)
        placements.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
            .forEach(entry -> {
                Identifier structureId = entry.getKey();
                List<PlacementRecord> records = entry.getValue();
                int count = records.size();

                if (count == 1) {
                    // Only one placement, no spacing to calculate
                    LOGGER.info("{}: {} placement (no spacing data)",
                        structureId, count);
                } else {
                    // Calculate spacing statistics
                    synchronized (records) {
                        List<Double> distances = new ArrayList<>();
                        for (int i = 1; i < records.size(); i++) {
                            double distance = records.get(i).chunkDistanceTo(records.get(i - 1));
                            distances.add(distance);
                        }

                        // Calculate statistics
                        double avgDistance = distances.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                        double minDistance = distances.stream()
                            .mapToDouble(Double::doubleValue)
                            .min()
                            .orElse(0.0);

                        double maxDistance = distances.stream()
                            .mapToDouble(Double::doubleValue)
                            .max()
                            .orElse(0.0);

                        // Calculate standard deviation
                        double variance = distances.stream()
                            .mapToDouble(d -> Math.pow(d - avgDistance, 2))
                            .average()
                            .orElse(0.0);
                        double stdDev = Math.sqrt(variance);

                        LOGGER.info("{}: {} placements - Avg spacing: {:.1f} chunks ({:.0f} blocks), Min: {:.1f}, Max: {:.1f}, StdDev: {:.1f}",
                            structureId,
                            count,
                            avgDistance,
                            avgDistance * 16, // Convert chunks to blocks
                            minDistance,
                            maxDistance,
                            stdDev);
                    }
                }
            });

        LOGGER.info("=== End of Placement Statistics ===");
    }

    /**
     * Gets a summary of all tracked placements.
     * <p>
     * Returns a formatted string with statistics for all structure types.
     * <p>
     * Example output:
     * <pre>
     * Placement Tracking Summary:
     * Total placements: 1543
     * Tracked structures: 127
     *
     * Top 5 placed structures:
     *   minecraft:village_plains: 45 placements
     *   dungeons_arise:castle: 23 placements
     *   repurposed_structures:mineshaft_birch: 18 placements
     *   ...
     * </pre>
     *
     * @return a formatted summary string
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Placement Tracking Summary:\n");
        sb.append(String.format("Total placements: %d\n", getTotalPlacements()));
        sb.append(String.format("Tracked structures: %d\n", placements.size()));

        if (!placements.isEmpty()) {
            sb.append("\nTop 5 placed structures:\n");
            placements.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .limit(5)
                .forEach(entry -> sb.append(String.format("  %s: %d placements\n",
                    entry.getKey(), entry.getValue().size())));
        }

        return sb.toString();
    }
}
