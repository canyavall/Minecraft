package com.canya.xeenaa_alexs_mobs.registry;

import com.canya.xeenaa_alexs_mobs.entity.animal.BlobfishEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.CockroachEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.CrowEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.HummingbirdEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.MudskipperEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity;
import com.canya.xeenaa_alexs_mobs.entity.animal.TriopsEntity;
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

    /**
     * Fly entity - tiny flying pest insect.
     *
     * <p>This entity represents a common housefly, serving as a small ambient creature
     * and demonstrating:
     * <ul>
     *   <li>Flying AI and movement patterns</li>
     *   <li>Tiny hitbox implementation (0.3 × 0.2 blocks)</li>
     *   <li>Realistic fly behavior (wandering, landing, taking off)</li>
     *   <li>Low-impact ambient entities</li>
     * </ul>
     *
     * <p><b>Dimensions:</b> 0.3 blocks wide × 0.2 blocks tall (tiny - smaller than a baby chicken)
     *
     * <p><b>Spawn Group:</b> CREATURE (ambient passive creature)
     *
     * <p><b>Registry Name:</b> {@code xeenaa-alexs-mobs:fly}
     *
     * <p><b>Summon Command:</b> {@code /summon xeenaa-alexs-mobs:fly}
     *
     * <p><b>Characteristics:</b>
     * <ul>
     *   <li>Health: 2 HP (dies in one hit from most sources)</li>
     *   <li>Movement: 0.5 blocks/tick flying speed (fast and erratic)</li>
     *   <li>Sounds: Buzzing ambient, squish hurt/death sounds</li>
     *   <li>Drops: 10% chance of 1 string (fly carcass material)</li>
     * </ul>
     *
     * @see FlyEntity
     */
    public static final EntityType<FlyEntity> FLY = register(
        "fly",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FlyEntity::new)
            .dimensions(EntityDimensions.fixed(0.3f, 0.2f))
            .build()
    );

    /**
     * Cockroach entity - small ground-dwelling insect (Epic 03).
     */
    public static final EntityType<CockroachEntity> COCKROACH = register(
        "cockroach",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CockroachEntity::new)
            .dimensions(EntityDimensions.fixed(0.4f, 0.3f))
            .build()
    );

    /**
     * Triops entity - small aquatic creature (Epic 03).
     */
    public static final EntityType<TriopsEntity> TRIOPS = register(
        "triops",
        FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, TriopsEntity::new)
            .dimensions(EntityDimensions.fixed(0.35f, 0.25f))
            .build()
    );

    /**
     * Hummingbird entity - tiny flying bird (Epic 03).
     */
    public static final EntityType<HummingbirdEntity> HUMMINGBIRD = register(
        "hummingbird",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HummingbirdEntity::new)
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .build()
    );

    /**
     * Mudskipper entity - amphibious fish (Epic 03).
     */
    public static final EntityType<MudskipperEntity> MUDSKIPPER = register(
        "mudskipper",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MudskipperEntity::new)
            .dimensions(EntityDimensions.fixed(0.4f, 0.3f))
            .build()
    );

    /**
     * Blobfish entity - deep water fish (Epic 03).
     */
    public static final EntityType<BlobfishEntity> BLOBFISH = register(
        "blobfish",
        FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, BlobfishEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.4f))
            .build()
    );

    /**
     * Crow entity - intelligent corvid bird with flying and ground movement (Epic 03).
     *
     * <p>The crow is a medium-sized bird entity that exhibits both aerial and ground-based behavior.
     * Unlike purely flying entities, crows can walk on the ground, land to rest, and take flight
     * when needed. They are intelligent birds that can be tempted with seeds.
     *
     * <p><b>Dimensions:</b> 0.5 blocks wide × 0.7 blocks tall (medium bird)
     *
     * <p><b>Spawn Group:</b> CREATURE (passive flying animal)
     *
     * <p><b>Registry Name:</b> {@code xeenaa-alexs-mobs:crow}
     *
     * <p><b>Summon Command:</b> {@code /summon xeenaa-alexs-mobs:crow}
     *
     * <p><b>Characteristics:</b>
     * <ul>
     *   <li>Health: 6 HP (3 hearts)</li>
     *   <li>Ground Speed: 0.25 blocks/tick (standard passive mob)</li>
     *   <li>Flying Speed: 0.4 blocks/tick (moderate aerial movement)</li>
     *   <li>Behavior: Ground walking, flying, landing, fleeing from threats</li>
     *   <li>Breeding: Tempted by seeds (wheat, beetroot, melon, pumpkin)</li>
     * </ul>
     *
     * @see CrowEntity
     */
    public static final EntityType<CrowEntity> CROW = register(
        "crow",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.7f))
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
        FabricDefaultAttributeRegistry.register(FLY, FlyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(COCKROACH, CockroachEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TRIOPS, TriopsEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HUMMINGBIRD, HummingbirdEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MUDSKIPPER, MudskipperEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BLOBFISH, BlobfishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CROW, CrowEntity.createAttributes());

        // Future attribute registrations will be added here as entities are implemented
        // This ensures entities have proper attributes (health, speed, armor, etc.)
    }
}
