package com.canya.xeenaa_alexs_mobs.entity.animal;

import com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity;
import com.canya.xeenaa_alexs_mobs.registry.ModEntities;
import com.canya.xeenaa_alexs_mobs.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Test entity for validating the Alex's Mobs Fabric port framework.
 *
 * <p>This entity serves as a proof-of-concept implementation to validate:
 * <ul>
 *   <li>{@link BaseAnimalEntity} - Common animal AI goals and behavior</li>
 *   <li>GeckoLib integration - Animation system via {@link GeoEntity}</li>
 *   <li>Entity registration - Fabric's {@code FabricEntityTypeBuilder} pattern</li>
 *   <li>Attribute registration - {@code FabricDefaultAttributeRegistry}</li>
 * </ul>
 *
 * <p>This is a minimal implementation with placeholder values for testing purposes.
 * It does NOT include:
 * <ul>
 *   <li>Custom sounds (returns {@code null} for all sound methods)</li>
 *   <li>Spawn eggs (handled separately in TASK-008)</li>
 *   <li>Client rendering (handled in TASK-006)</li>
 *   <li>Complex AI behaviors (uses base behaviors only)</li>
 * </ul>
 *
 * <h2>Testing Instructions:</h2>
 * <pre>
 * 1. Build the mod with `./gradlew build`
 * 2. Run the client with `./gradlew runClient`
 * 3. Summon in-game with: /summon xeenaa-alexs-mobs:test_animal
 * 4. Verify entity appears, walks, and looks around
 * </pre>
 *
 * <h2>Animation System:</h2>
 * <p>This entity implements basic walk/idle animation states using GeckoLib:
 * <ul>
 *   <li><b>Idle</b>: Plays when entity is stationary</li>
 *   <li><b>Walk</b>: Plays when entity is moving</li>
 * </ul>
 *
 * <p><b>Note:</b> Animations reference placeholder animation names. Actual animations
 * will be defined in {@code test_animal.animation.json} (TASK-006).
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. All methods should be called from the
 * appropriate game thread (server or client) as managed by Minecraft's entity system.
 *
 * @author Canya
 * @see BaseAnimalEntity
 * @see GeoEntity
 * @see ModEntities#TEST_ANIMAL
 */
public class TestAnimalEntity extends BaseAnimalEntity implements GeoEntity {

    /**
     * GeckoLib animation cache for this entity instance.
     *
     * <p>This cache stores animation state and controllers for GeckoLib's animation system.
     * It is initialized once per entity instance using {@link GeckoLibUtil#createInstanceCache(Object)}.
     *
     * @see #getAnimatableInstanceCache()
     */
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /**
     * Constructs a new test animal entity.
     *
     * <p>This constructor is called by Minecraft's entity spawning system. It passes
     * the entity type and world to the parent {@link BaseAnimalEntity} constructor.
     *
     * @param entityType the entity type (should be {@link ModEntities#TEST_ANIMAL})
     * @param world the world this entity is spawning in (server or client)
     * @throws NullPointerException if entityType or world is null
     */
    public TestAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // ============================================
    // Entity Attributes
    // ============================================

    /**
     * Creates default attributes for the test animal entity.
     *
     * <p>This method defines the entity's base stats such as health and movement speed.
     * These values are placeholder values for testing and can be adjusted as needed.
     *
     * <p><b>Attribute Values:</b>
     * <ul>
     *   <li><b>Max Health</b>: 10.0 HP (5 hearts)</li>
     *   <li><b>Movement Speed</b>: 0.25 (standard passive mob speed)</li>
     * </ul>
     *
     * <p>This method is called during entity registration in {@link ModEntities#initialize()}.
     *
     * @return an attribute container builder with configured default attributes
     * @see net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    // ============================================
    // GeckoLib Animation System
    // ============================================

    /**
     * Registers animation controllers for this entity.
     *
     * <p>This method sets up the GeckoLib animation system by registering controllers
     * that determine which animations play based on entity state (idle, walking, etc.).
     *
     * <p>The single controller registered here handles both idle and walking animations,
     * switching between them based on movement state via {@link #predicate(AnimationState)}.
     *
     * @param controllers the controller registrar provided by GeckoLib
     * @see #predicate(AnimationState)
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    /**
     * Animation predicate that determines which animation to play.
     *
     * <p>This method is called every tick by the GeckoLib animation system to determine
     * the current animation state. It checks if the entity is moving and switches between
     * walk and idle animations accordingly.
     *
     * <p><b>Animation States:</b>
     * <ul>
     *   <li><b>Moving</b>: Plays "walk" animation in loop</li>
     *   <li><b>Stationary</b>: Plays "idle" animation in loop</li>
     * </ul>
     *
     * <p><b>Note:</b> Animation names ("walk", "idle") are placeholders. Actual animations
     * must be defined in the model's animation JSON file (created in TASK-006).
     *
     * @param state the current animation state provided by GeckoLib
     * @return {@link PlayState#CONTINUE} to continue playing the current animation
     */
    private PlayState predicate(AnimationState<TestAnimalEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    /**
     * Gets the GeckoLib animation cache for this entity instance.
     *
     * <p>This method is required by the {@link GeoEntity} interface and provides
     * access to the animation cache that stores animation state and controllers.
     *
     * @return the animation instance cache for this entity
     * @see #cache
     */
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ============================================
    // Breeding System
    // ============================================

    /**
     * Creates a baby test animal entity for breeding mechanics.
     *
     * <p>This method is called when two test animals breed (triggered by {@link net.minecraft.entity.ai.goal.AnimalMateGoal}).
     * It creates a new test animal entity of the same type.
     *
     * <p><b>Implementation Note:</b> This is a test entity, so breeding mechanics are minimal.
     * Real entities in Alex's Mobs may have more complex breeding logic (traits, variants, etc.).
     *
     * @param world the server world where the baby will spawn
     * @param entity the other parent entity (should also be a TestAnimalEntity)
     * @return a new baby TestAnimalEntity, or {@code null} if entity creation fails
     * @throws IllegalArgumentException if world is not a ServerWorld
     */
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.TEST_ANIMAL.create(world);
    }

    /**
     * Checks if the given item stack can be used to breed this test animal.
     *
     * <p><b>Test Implementation:</b> Returns {@code false} for all items because this is a
     * test entity without breeding mechanics. Real entities should check for specific breeding
     * items (e.g., wheat for cows, carrots for pigs).
     *
     * <p><b>Example for real entities:</b>
     * <pre>{@code
     * @Override
     * public boolean isBreedingItem(ItemStack stack) {
     *     return stack.isOf(Items.WHEAT);
     * }
     * }</pre>
     *
     * @param stack the item stack to check
     * @return {@code false} (test entity cannot be bred with items)
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false; // Test entity has no breeding items
    }

    // ============================================
    // Sound System
    // ============================================

    /**
     * Gets the ambient sound event for this test entity.
     *
     * <p><b>Test Implementation:</b> Returns {@code null} because this is a test entity
     * without sound assets. Real entities should return appropriate {@link SoundEvent}
     * references from a ModSounds registry.
     *
     * @return {@code null} (no ambient sound for test entity)
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.TEST_ANIMAL_AMBIENT;
    }

    /**
     * Gets the hurt sound event for this test entity.
     *
     * <p><b>Test Implementation:</b> Returns {@code null} because this is a test entity
     * without sound assets. Real entities should return appropriate {@link SoundEvent}
     * references from a ModSounds registry.
     *
     * @param source the source of the damage (ignored in test implementation)
     * @return {@code null} (no hurt sound for test entity)
     */
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.TEST_ANIMAL_HURT;
    }

    /**
     * Gets the death sound event for this test entity.
     *
     * <p><b>Test Implementation:</b> Returns {@code null} because this is a test entity
     * without sound assets. Real entities should return appropriate {@link SoundEvent}
     * references from a ModSounds registry.
     *
     * @return {@code null} (no death sound for test entity)
     */
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.TEST_ANIMAL_DEATH;
    }
}
