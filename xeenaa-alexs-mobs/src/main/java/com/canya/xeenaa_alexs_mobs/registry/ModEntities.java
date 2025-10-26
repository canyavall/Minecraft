package com.canya.xeenaa_alexs_mobs.registry;

import com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry for all custom entities in Alex's Mobs port.
 *
 * <p>This class provides a centralized registration system for entities using Fabric patterns.
 * All entity types should be registered here using the {@link #register(String, EntityType)} method.
 *
 * <p>Entity attributes must be registered separately in the {@link #initialize()} method using
 * {@code FabricDefaultAttributeRegistry.register()}.
 *
 * <p><b>Usage Example:</b>
 * <pre>{@code
 * public static final EntityType<AlligatorEntity> ALLIGATOR = register(
 *     "alligator",
 *     FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AlligatorEntity::new)
 *         .dimensions(EntityDimensions.fixed(1.5f, 0.8f))
 *         .build()
 * );
 * }</pre>
 *
 * <p>Then in {@link #initialize()}, register attributes:
 * <pre>{@code
 * FabricDefaultAttributeRegistry.register(ALLIGATOR, AlligatorEntity.createAttributes());
 * }</pre>
 *
 * @author Canya
 * @see net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry
 * @see net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder
 */
public class ModEntities {

    // ============================================
    // Entity Type Definitions
    // ============================================

    /**
     * Test animal entity for framework validation.
     *
     * <p>This entity serves as a proof-of-concept to validate:
     * <ul>
     *   <li>Entity registration with Fabric</li>
     *   <li>GeckoLib animation integration</li>
     *   <li>BaseAnimalEntity common behavior</li>
     *   <li>Attribute registration</li>
     * </ul>
     *
     * <p><b>Dimensions:</b> 0.6 blocks wide × 0.8 blocks tall (similar to sheep size)
     *
     * <p><b>Spawn Group:</b> CREATURE (passive animal)
     *
     * <p><b>Registry Name:</b> {@code xeenaa-alexs-mobs:test_animal}
     *
     * <p><b>Summon Command:</b> {@code /summon xeenaa-alexs-mobs:test_animal}
     *
     * @see TestAnimalEntity
     */
    public static final EntityType<TestAnimalEntity> TEST_ANIMAL = register(
        "test_animal",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TestAnimalEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 0.8f))
            .build()
    );

    // Future entities will be added here following the pattern above
    // Examples: ALLIGATOR, ANACONDA, ANTEATER, BALD_EAGLE, etc.

    // ============================================
    // Registration Methods
    // ============================================

    /**
     * Registers an entity type with Minecraft's entity registry.
     *
     * <p>This method uses Fabric's registry system to register entity types
     * with the mod's namespace. All entities are registered under the
     * "xeenaa-alexs-mobs" namespace.
     *
     * @param name the registry name for this entity (e.g., "alligator")
     * @param type the entity type to register
     * @param <T> the entity class type extending {@link Entity}
     * @return the registered entity type (for chaining or field assignment)
     */
    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("xeenaa-alexs-mobs", name), type);
    }

    /**
     * Initializes all entity registrations and registers entity attributes.
     *
     * <p>This method must be called during mod initialization to ensure all entities
     * have their attributes properly registered with Fabric. Without this call,
     * entities will not have attributes like health, movement speed, armor, etc.
     *
     * <p><b>Call this from {@code AlexsMobsMod.onInitialize()}.</b>
     *
     * <p>Entity attributes are registered using Fabric's
     * {@code FabricDefaultAttributeRegistry.register()} method. Each entity
     * should provide a static {@code createAttributes()} method that returns
     * an {@link net.minecraft.entity.attribute.DefaultAttributeContainer.Builder}.
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * FabricDefaultAttributeRegistry.register(ALLIGATOR, AlligatorEntity.createAttributes());
     * }</pre>
     */
    public static void initialize() {
        // Register entity attributes here
        FabricDefaultAttributeRegistry.register(TEST_ANIMAL, TestAnimalEntity.createAttributes());

        // Future attribute registrations will be added here as entities are implemented
        // This ensures entities have proper attributes (health, speed, armor, etc.)
    }
}
