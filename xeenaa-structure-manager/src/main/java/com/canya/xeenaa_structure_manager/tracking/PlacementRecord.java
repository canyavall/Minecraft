package com.canya.xeenaa_structure_manager.tracking;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

/**
 * Immutable record of a structure placement event.
 * <p>
 * This record captures all relevant information about a structure placement:
 * <ul>
 *   <li>Structure identifier (e.g., "minecraft:village_plains")</li>
 *   <li>Position in the world (block coordinates)</li>
 *   <li>Chunk coordinates (for spacing calculations)</li>
 *   <li>Timestamp of placement (for analysis)</li>
 * </ul>
 * <p>
 * Records are created by the {@link PlacementTracker} when structures are placed
 * via the {@link com.canya.xeenaa_structure_manager.mixin.StructureStartPlaceMixin}.
 * <p>
 * Thread-safe: This class is immutable and safe for concurrent access.
 *
 * @param structureId the identifier of the placed structure
 * @param blockPos the block position where the structure was placed
 * @param chunkPos the chunk position (derived from blockPos)
 * @param timestamp the system time when the structure was placed (milliseconds)
 *
 * @see PlacementTracker
 * @since 1.0.0
 */
public record PlacementRecord(
    Identifier structureId,
    BlockPos blockPos,
    ChunkPos chunkPos,
    long timestamp
) {
    /**
     * Creates a new placement record with the current timestamp.
     * <p>
     * This is the primary constructor used by the placement tracking system.
     * The chunk position is automatically derived from the block position.
     * <p>
     * Example:
     * <pre>
     * Identifier id = Identifier.of("minecraft", "village_plains");
     * BlockPos pos = new BlockPos(100, 64, 200);
     * PlacementRecord record = PlacementRecord.of(id, pos);
     * </pre>
     *
     * @param structureId the identifier of the placed structure
     * @param blockPos the block position where the structure was placed
     * @return a new placement record with the current timestamp
     * @throws IllegalArgumentException if structureId or blockPos is null
     */
    public static PlacementRecord of(Identifier structureId, BlockPos blockPos) {
        if (structureId == null) {
            throw new IllegalArgumentException("Structure ID cannot be null");
        }
        if (blockPos == null) {
            throw new IllegalArgumentException("Block position cannot be null");
        }

        ChunkPos chunkPos = new ChunkPos(blockPos);
        long timestamp = System.currentTimeMillis();

        return new PlacementRecord(structureId, blockPos, chunkPos, timestamp);
    }

    /**
     * Calculates the distance in chunks to another placement record.
     * <p>
     * This is used for spacing analysis to determine how far apart structures
     * of the same type are placed.
     * <p>
     * The distance is calculated using Euclidean distance formula:
     * <pre>
     * distance = sqrt((x1 - x2)² + (z1 - z2)²)
     * </pre>
     *
     * @param other the other placement record to calculate distance to
     * @return the distance in chunks (as a double for precision)
     * @throws IllegalArgumentException if other is null
     */
    public double chunkDistanceTo(PlacementRecord other) {
        if (other == null) {
            throw new IllegalArgumentException("Other placement record cannot be null");
        }

        int dx = this.chunkPos.x - other.chunkPos.x;
        int dz = this.chunkPos.z - other.chunkPos.z;

        return Math.sqrt(dx * dx + dz * dz);
    }

    /**
     * Calculates the distance in blocks to another placement record.
     * <p>
     * This uses the actual block positions for more precise distance calculations.
     * <p>
     * Note: This only calculates XZ distance (horizontal), ignoring Y coordinate,
     * as structure spacing is typically measured horizontally.
     *
     * @param other the other placement record to calculate distance to
     * @return the distance in blocks (as a double for precision)
     * @throws IllegalArgumentException if other is null
     */
    public double blockDistanceTo(PlacementRecord other) {
        if (other == null) {
            throw new IllegalArgumentException("Other placement record cannot be null");
        }

        int dx = this.blockPos.getX() - other.blockPos.getX();
        int dz = this.blockPos.getZ() - other.blockPos.getZ();

        return Math.sqrt(dx * dx + dz * dz);
    }

    /**
     * Gets a human-readable representation of this placement record.
     * <p>
     * Example output:
     * <pre>
     * PlacementRecord[structureId=minecraft:village_plains, pos=(100, 64, 200), chunk=(6, 12)]
     * </pre>
     *
     * @return a string representation of this record
     */
    @Override
    public String toString() {
        return String.format(
            "PlacementRecord[structureId=%s, pos=(%d, %d, %d), chunk=(%d, %d)]",
            structureId,
            blockPos.getX(), blockPos.getY(), blockPos.getZ(),
            chunkPos.x, chunkPos.z
        );
    }
}
