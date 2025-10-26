package com.canya.xeenaa_structure_manager.mixin;

import com.canya.xeenaa_structure_manager.tracking.PlacementTracker;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin into {@link StructureStart#place} to track structure placements.
 * <p>
 * This mixin intercepts structure placement at the exact moment blocks are being generated,
 * allowing us to log structure ID, position, and timestamp for spacing verification.
 *
 * @see PlacementTracker
 * @see StructureStart#place
 * @since 1.0.0
 */
@Mixin(StructureStart.class)
public class StructureStartPlaceMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager/PlacementTracking");

    @Shadow
    @Final
    private Structure structure;

    @Shadow
    @Final
    private ChunkPos pos;

    /**
     * Intercepts structure placement to record the event.
     * <p>
     * This method is called at the HEAD of the place() method, before any blocks are placed.
     * It extracts structure information and records the placement in the PlacementTracker.
     *
     * @param world the world where structure is being placed
     * @param structureAccessor the structure accessor
     * @param chunkGenerator the chunk generator
     * @param random the random instance
     * @param chunkBox the bounding box of the current chunk
     * @param chunkPos the position of the current chunk being generated
     * @param ci callback info from Mixin
     */
    @Inject(
        method = "place",
        at = @At("HEAD")
    )
    private void onStructurePlace(
        StructureWorldAccess world,
        StructureAccessor structureAccessor,
        ChunkGenerator chunkGenerator,
        Random random,
        BlockBox chunkBox,
        ChunkPos chunkPos,
        CallbackInfo ci
    ) {
        try {
            // Check if structure has children (valid placement)
            StructureStart self = (StructureStart)(Object)this;
            if (!self.hasChildren()) {
                return; // Skip invalid/empty structures
            }

            // Get structure identifier from registry
            Identifier structureId = world.getRegistryManager()
                .get(RegistryKeys.STRUCTURE)
                .getId(structure);

            if (structureId == null) {
                LOGGER.warn("Could not extract structure ID from registry, skipping tracking");
                return;
            }

            // Get actual structure position from bounding box
            BlockBox boundingBox = self.getBoundingBox();
            int structureY = boundingBox != null ? boundingBox.getMinY() : 64;

            // Calculate center position (chunk center in blocks)
            int centerX = ChunkSectionPos.getOffsetPos(pos.x, 8);
            int centerZ = ChunkSectionPos.getOffsetPos(pos.z, 8);

            // Record the placement in tracker with actual Y position
            BlockPos blockPos = new BlockPos(centerX, structureY, centerZ);
            PlacementTracker.getInstance().recordPlacement(structureId, blockPos);

            // Log at DEBUG level for detailed tracking
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Tracked placement: {} at chunk ({}, {}), center ({}, {})",
                    structureId, pos.x, pos.z, centerX, centerZ);
            }

        } catch (Exception e) {
            // Log error but don't crash - tracking is non-critical
            LOGGER.error("Failed to track structure placement: {}", e.getMessage(), e);
        }
    }
}
