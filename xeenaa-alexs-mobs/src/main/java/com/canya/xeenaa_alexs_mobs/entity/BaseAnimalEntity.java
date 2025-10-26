package com.canya.xeenaa_alexs_mobs.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all animal entities in Xeenaa's Alex's Mobs (Fabric).
 *
 * <p>This abstract class provides common functionality for all animal mobs in the Alex's Mobs port,
 * including standard AI goals, sound handling, and breeding mechanics. All concrete animal entities
 * should extend this class to ensure consistent behavior and reduce code duplication.
 *
 * <h2>Features Provided:</h2>
 * <ul>
 *   <li><b>Common AI Goals</b>: Swimming, danger escape, mating, wandering, looking at players</li>
 *   <li><b>Sound System</b>: Abstract methods for ambient, hurt, and death sounds</li>
 *   <li><b>Breeding Support</b>: Integration with Minecraft's breeding mechanics</li>
 *   <li><b>Extensibility</b>: Hook method for adding entity-specific AI goals</li>
 * </ul>
 *
 * <h2>AI Goal Priority Structure:</h2>
 * <pre>
 * Priority 0: {@link SwimGoal} - Always swim in water (highest priority)
 * Priority 1: {@link EscapeDangerGoal} - Flee from threats
 * Priority 2: {@link AnimalMateGoal} - Breed with nearby animals
 * Priority 5: {@link WanderAroundFarGoal} - Wander when idle
 * Priority 6: {@link LookAtEntityGoal} - Look at nearby players
 * Priority 7: {@link LookAroundGoal} - Look around randomly
 * </pre>
 *
 * <p><b>Custom goals should be added in {@link #registerCustomGoals()} with priorities that fit this structure.</b>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class AlligatorEntity extends BaseAnimalEntity {
 *
 *     public AlligatorEntity(EntityType<? extends AnimalEntity> entityType, World world) {
 *         super(entityType, world);
 *     }
 *
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Add alligator-specific AI goals here
 *         this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, false));
 *         this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
 *     }
 *
 *     @Override
 *     protected SoundEvent getAmbientSound() {
 *         return ModSounds.ALLIGATOR_AMBIENT;
 *     }
 *
 *     @Override
 *     protected SoundEvent getHurtSound(DamageSource source) {
 *         return ModSounds.ALLIGATOR_HURT;
 *     }
 *
 *     @Override
 *     protected SoundEvent getDeathSound() {
 *         return ModSounds.ALLIGATOR_DEATH;
 *     }
 *
 *     @Override
 *     public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
 *         return ModEntities.ALLIGATOR.create(world);
 *     }
 *
 *     public static DefaultAttributeContainer.Builder createAttributes() {
 *         return AnimalEntity.createMobAttributes()
 *             .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
 *             .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
 *             .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0);
 *     }
 * }
 * }</pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. All methods should be called from the server thread
 * (for ServerWorld) or the client thread (for ClientWorld). Entity ticking and AI goal execution
 * are managed by Minecraft's entity system and should not be called concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>AI goals are evaluated every tick - keep {@link #registerCustomGoals()} lightweight</li>
 *   <li>Avoid expensive computations in sound methods (called frequently)</li>
 *   <li>Use lazy initialization for complex state if possible</li>
 * </ul>
 *
 * @author Canya
 * @see AnimalEntity
 * @see Goal
 * @see PassiveEntity#createChild(ServerWorld, PassiveEntity)
 */
public abstract class BaseAnimalEntity extends AnimalEntity {

    /**
     * Constructs a new base animal entity.
     *
     * <p>This constructor should be called by all subclasses in their constructor.
     * The entity type determines spawn behavior, save data, and registry information.
     *
     * @param entityType the entity type for this animal (from {@code ModEntities})
     * @param world the world this entity is spawning in (server or client)
     * @throws NullPointerException if entityType or world is null
     */
    public BaseAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // ============================================
    // AI Goal System
    // ============================================

    /**
     * Initializes AI goals for this entity.
     *
     * <p>This method sets up common AI behaviors that all animals should exhibit, such as
     * swimming in water, fleeing from danger, and wandering when idle. After registering
     * common goals, it calls {@link #registerCustomGoals()} to allow subclasses to add
     * entity-specific behaviors.
     *
     * <p><b>Priority Guidelines:</b>
     * <ul>
     *   <li>0-1: Critical survival (swim, panic, flee)</li>
     *   <li>2-4: Active behaviors (attack, mate, follow)</li>
     *   <li>5-7: Idle behaviors (wander, look)</li>
     *   <li>8+: Low-priority or situational behaviors</li>
     * </ul>
     *
     * <p><b>DO NOT override this method.</b> Override {@link #registerCustomGoals()} instead
     * to add custom AI goals while preserving common behaviors.
     *
     * @see #registerCustomGoals()
     */
    @Override
    protected void initGoals() {
        // Priority 0: Swimming is critical - prevents drowning
        this.goalSelector.add(0, new SwimGoal(this));

        // Priority 1: Escape from danger (fire, lava, explosions)
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5D));

        // Priority 2: Mating behavior for breeding mechanics
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));

        // Priority 5: Wander around when idle (far range for natural movement)
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));

        // Priority 6: Look at nearby players (adds life-like behavior)
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));

        // Priority 7: Look around randomly when idle
        this.goalSelector.add(7, new LookAroundGoal(this));

        // Allow subclasses to register entity-specific goals
        registerCustomGoals();
    }

    /**
     * Hook method for subclasses to register custom AI goals.
     *
     * <p>Override this method to add entity-specific behaviors such as:
     * <ul>
     *   <li>Attack goals (melee, ranged)</li>
     *   <li>Target selection (hostile, prey, predator)</li>
     *   <li>Special behaviors (dig, climb, fly)</li>
     *   <li>Following or fleeing from specific entities</li>
     * </ul>
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * @Override
     * protected void registerCustomGoals() {
     *     // Priority 3: Attack nearby chickens
     *     this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false));
     *     this.targetSelector.add(1, new ActiveTargetGoal<>(this, ChickenEntity.class, true));
     *
     *     // Priority 4: Follow players holding specific items
     *     this.goalSelector.add(4, new TemptGoal(this, 1.1D, TEMPT_INGREDIENT, false));
     * }
     * }</pre>
     *
     * <p>This method is called AFTER common goals are registered in {@link #initGoals()}.
     * The default implementation does nothing.
     */
    protected void registerCustomGoals() {
        // Override in subclasses to add entity-specific AI goals
        // Default: No custom goals
    }

    // ============================================
    // Sound System
    // ============================================

    /**
     * Gets the ambient sound event for this entity.
     *
     * <p>This sound plays periodically when the entity is alive and idle. It should represent
     * the natural sounds the animal makes (e.g., bird chirping, alligator growling).
     *
     * <p><b>Performance Note:</b> This method is called frequently. Return a constant
     * {@link SoundEvent} - do NOT perform expensive computations here.
     *
     * @return the sound event to play when ambient, or {@code null} for no sound
     */
    @Nullable
    @Override
    protected abstract SoundEvent getAmbientSound();

    /**
     * Gets the hurt sound event for this entity.
     *
     * <p>This sound plays when the entity takes damage. It should convey pain or distress
     * appropriate to the animal type.
     *
     * <p>The {@link DamageSource} parameter allows different sounds for different damage types
     * (e.g., fire damage vs melee attack), but most entities use the same hurt sound regardless.
     *
     * <p><b>Performance Note:</b> This method is called frequently during combat. Return a
     * constant {@link SoundEvent} - do NOT perform expensive computations here.
     *
     * @param source the source of the damage (fire, player attack, fall, etc.)
     * @return the sound event to play when hurt, or {@code null} for no sound
     */
    @Nullable
    @Override
    protected abstract SoundEvent getHurtSound(DamageSource source);

    /**
     * Gets the death sound event for this entity.
     *
     * <p>This sound plays when the entity dies (health reaches zero). It should be distinct
     * from the hurt sound and convey finality.
     *
     * <p><b>Performance Note:</b> This method is called once per death. Return a constant
     * {@link SoundEvent} - do NOT perform expensive computations here.
     *
     * @return the sound event to play when dying, or {@code null} for no sound
     */
    @Nullable
    @Override
    protected abstract SoundEvent getDeathSound();

    // ============================================
    // Breeding System
    // ============================================

    /**
     * Creates a baby entity of this type for breeding mechanics.
     *
     * <p>This method is called when two animals of the same type breed (triggered by
     * {@link AnimalMateGoal}). The baby entity should be of the same type as the parents.
     *
     * <p><b>Implementation Pattern:</b>
     * <pre>{@code
     * @Override
     * public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
     *     return ModEntities.ALLIGATOR.create(world);
     * }
     * }</pre>
     *
     * <p><b>Thread Safety:</b> This method is only called on the server thread (ServerWorld).
     * It will never be called on the client.
     *
     * @param world the server world where the baby will spawn
     * @param entity the other parent entity (same type as this entity)
     * @return a new baby entity, or {@code null} if breeding should fail
     * @throws IllegalArgumentException if world is not a ServerWorld
     */
    @Nullable
    @Override
    public abstract PassiveEntity createChild(ServerWorld world, PassiveEntity entity);

    // ============================================
    // Helper Methods
    // ============================================

    /**
     * Checks if this entity is currently in a dangerous situation.
     *
     * <p>This helper method can be used by custom AI goals to determine if the entity
     * should prioritize escape behaviors. An entity is considered in danger if:
     * <ul>
     *   <li>It is on fire</li>
     *   <li>It is in lava</li>
     *   <li>It has a target (being attacked)</li>
     * </ul>
     *
     * <p><b>Usage Example:</b>
     * <pre>{@code
     * if (isInDanger()) {
     *     // Cancel non-critical behaviors
     *     return false;
     * }
     * }</pre>
     *
     * @return {@code true} if the entity is in immediate danger, {@code false} otherwise
     */
    protected boolean isInDanger() {
        return this.isOnFire() || this.isInLava() || this.getTarget() != null;
    }

    /**
     * Checks if this entity is idle (not executing any active behaviors).
     *
     * <p>This helper method can be used by custom AI goals to determine if the entity
     * is available for low-priority behaviors. An entity is considered idle if:
     * <ul>
     *   <li>It has no attack target</li>
     *   <li>It is not in love mode (breeding)</li>
     *   <li>It is not in danger</li>
     * </ul>
     *
     * <p><b>Usage Example:</b>
     * <pre>{@code
     * if (isIdle()) {
     *     // Execute low-priority behaviors (play, explore, etc.)
     * }
     * }</pre>
     *
     * @return {@code true} if the entity is idle, {@code false} if engaged in active behavior
     */
    protected boolean isIdle() {
        return this.getTarget() == null && !this.isInLove() && !isInDanger();
    }

}
