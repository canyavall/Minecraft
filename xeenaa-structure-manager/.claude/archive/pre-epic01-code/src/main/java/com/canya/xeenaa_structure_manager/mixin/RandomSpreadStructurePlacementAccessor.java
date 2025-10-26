package com.canya.xeenaa_structure_manager.mixin;

import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Mixin accessor for RandomSpreadStructurePlacement to access and modify private fields.
 * <p>
 * This accessor interface provides read and write access to the spacing and separation
 * fields of RandomSpreadStructurePlacement, allowing us to modify placement parameters
 * in-place rather than creating new instances.
 * <p>
 * Mixin accessors are the proper way to access private fields in Minecraft classes
 * without using reflection, providing compile-time safety and better performance.
 * <p>
 * <strong>Approach:</strong> Instead of creating new instances (which requires knowing
 * all constructor parameters), we directly modify the existing object's fields via
 * mutable accessors.
 *
 * @see RandomSpreadStructurePlacement
 * @see StructurePlacementCalculatorMixin
 * @since 1.0.0
 */
@Mixin(RandomSpreadStructurePlacement.class)
public interface RandomSpreadStructurePlacementAccessor {

    /**
     * Gets the spacing value (average chunks between structure attempts).
     *
     * @return the spacing value in chunks
     */
    @Accessor
    int getSpacing();

    /**
     * Sets the spacing value (average chunks between structure attempts).
     *
     * @param spacing the new spacing value in chunks
     */
    @Mutable
    @Accessor
    void setSpacing(int spacing);

    /**
     * Gets the separation value (minimum chunks between structures of this type).
     *
     * @return the separation value in chunks
     */
    @Accessor
    int getSeparation();

    /**
     * Sets the separation value (minimum chunks between structures of this type).
     *
     * @param separation the new separation value in chunks
     */
    @Mutable
    @Accessor
    void setSeparation(int separation);
}
