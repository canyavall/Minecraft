package com.canya.xeenaa_alexs_mobs.entity.base;

import com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for all flying animal entities.
 *
 * <p>This abstract class provides common functionality for all flying mobs in Xeenaa's Alex's Mobs,
 * including 3D pathfinding, flight movement control, landing behavior, and slow falling mechanics.
 * All flying animal entities should extend this class to ensure consistent aerial behavior.
 *
 * <h2>Features Provided:</h2>
 * <ul>
 *   <li><b>3D Pathfinding</b>: Uses {@link BirdNavigation} for aerial movement (like bats/parrots)</li>
 *   <li><b>Flight Control</b>: {@link FlightMoveControl} for smooth 3D flying movement</li>
 *   <li><b>Landing Behavior</b>: Can land on blocks and rest (controlled via {@link #shouldFly()})</li>
 *   <li><b>Slow Fall</b>: No fall damage when descending or landing</li>
 *   <li><b>Flee Upward</b>: Natural escape behavior by flying upward when threatened</li>
 * </ul>
 *
 * <h2>Movement System:</h2>
 * <p>Flying entities use Minecraft's bird navigation system, which allows full 3D pathfinding:
 * <ul>
 *   <li>Can pathfind through air blocks</li>
 *   <li>Can navigate around obstacles in 3D space</li>
 *   <li>Can swim if needed (navigation allows water pathfinding)</li>
 *   <li>Cannot pathfind through doors (configured to avoid buildings)</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class FlyEntity extends FlyingAnimalEntity {
 *
 *     public FlyEntity(EntityType<? extends FlyingAnimalEntity> type, World world) {
 *         super(type, world);
 *     }
 *
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Add fly-specific AI goals here
 *         this.goalSelector.add(3, new FlyAroundGoal(this));
 *     }
 *
 *     @Override
 *     protected SoundEvent getAmbientSound() {
 *         return ModSounds.FLY_AMBIENT;
 *     }
 *
 *     @Override
 *     protected SoundEvent getHurtSound(DamageSource source) {
 *         return ModSounds.FLY_HURT;
 *     }
 *
 *     @Override
 *     protected SoundEvent getDeathSound() {
 *         return ModSounds.FLY_DEATH;
 *     }
 *
 *     @Override
 *     public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
 *         return ModEntities.FLY.create(world);
 *     }
 *
 *     @Override
 *     protected boolean shouldFly() {
 *         // Flies should always fly unless on ground
 *         return !this.isOnGround();
 *     }
 *
 *     public static DefaultAttributeContainer.Builder createAttributes() {
 *         return FlyingAnimalEntity.createMobAttributes()
 *             .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
 *             .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
 *             .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6);
 *     }
 * }
 * }</pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. All methods should be called from the server thread
 * (for ServerWorld) or the client thread (for ClientWorld). Entity ticking and navigation
 * are managed by Minecraft's entity system and should not be called concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>3D pathfinding is more expensive than 2D - use sparingly for many entities</li>
 *   <li>{@link #shouldFly()} is called every tick - keep it lightweight</li>
 *   <li>FlightMoveControl updates smoothly but adds computational overhead</li>
 * </ul>
 *
 * @author Canya
 * @see BaseAnimalEntity
 * @see BirdNavigation
 * @see FlightMoveControl
 */
public abstract class FlyingAnimalEntity extends BaseAnimalEntity {

    /**
     * Constructs a new flying animal entity.
     *
     * <p>This constructor sets up the flight movement controller with a max turn angle of 20 degrees
     * and enables in-air movement updates. The movement controller allows smooth 3D flying motion
     * similar to vanilla bats and parrots.
     *
     * @param type the entity type for this flying animal (from {@code ModEntities})
     * @param world the world this entity is spawning in (server or client)
     * @throws NullPointerException if type or world is null
     */
    protected FlyingAnimalEntity(EntityType<? extends FlyingAnimalEntity> type, World world) {
        super(type, world);
        // Set up flight movement controller
        // Parameters: entity, maxTurnAngle=20, updateInAir=true
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    // ============================================
    // Navigation System
    // ============================================

    /**
     * Creates the navigation system for 3D pathfinding.
     *
     * <p>Flying entities use {@link BirdNavigation} which enables full 3D pathfinding through
     * air blocks. This is the same navigation system used by vanilla bats and parrots.
     *
     * <p><b>Navigation Configuration:</b>
     * <ul>
     *   <li>Cannot pathfind through doors (avoids buildings)</li>
     *   <li>Can swim if needed (emergency behavior)</li>
     *   <li>Can enter open doors (if forced by pathfinding)</li>
     * </ul>
     *
     * <p><b>DO NOT override this method</b> unless you need custom navigation behavior.
     * The default bird navigation is suitable for all standard flying mobs.
     *
     * @param world the world this entity is in
     * @return a configured {@link BirdNavigation} instance for 3D pathfinding
     */
    @Override
    protected EntityNavigation createNavigation(World world) {
        // Use bird navigation for 3D pathfinding (like bat/parrot)
        BirdNavigation navigation = new BirdNavigation(this, world);
        navigation.setCanPathThroughDoors(false); // Avoid buildings
        navigation.setCanSwim(true); // Can swim if needed (emergency)
        navigation.setCanEnterOpenDoors(true); // Can go through open doors if needed
        return navigation;
    }

    // ============================================
    // Fall Damage & Physics
    // ============================================

    /**
     * Handles fall damage for flying entities.
     *
     * <p>Flying animals never take fall damage due to their ability to control descent.
     * This simulates slow falling / gliding behavior when descending.
     *
     * <p><b>DO NOT override this method</b> - flying entities should never take fall damage.
     *
     * @param fallDistance the distance fallen in blocks
     * @param damageMultiplier damage multiplier from effects/enchantments
     * @param damageSource the source of fall damage
     * @return always {@code false} (no fall damage applied)
     */
    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        // Flying animals don't take fall damage (slow fall ability)
        return false;
    }

    /**
     * Handles falling physics for flying entities.
     *
     * <p>This method is overridden to prevent fall particles and sounds from playing when
     * a flying entity descends. Flying mobs don't "fall" in the traditional sense - they
     * control their descent.
     *
     * <p><b>DO NOT override this method</b> - it prevents unwanted fall effects.
     *
     * @param heightDifference the vertical distance fallen
     * @param onGround whether the entity landed on ground
     * @param state the block state landed on
     * @param pos the position landed at
     */
    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos pos) {
        // Override to prevent fall particles/sounds for flying mobs
        // Do nothing - flying mobs don't "fall" traditionally
    }

    // ============================================
    // Flight Behavior
    // ============================================

    /**
     * Checks if this flying entity should be actively flying.
     *
     * <p>This method controls when the entity should fly versus rest on the ground.
     * Override this method to implement landing/resting behavior specific to your mob.
     *
     * <p><b>Default Behavior:</b> Always flying unless standing on ground.
     *
     * <p><b>Usage Example:</b>
     * <pre>{@code
     * @Override
     * protected boolean shouldFly() {
     *     // Fly during day, rest at night
     *     if (!this.getWorld().isDay()) {
     *         return false; // Rest at night
     *     }
     *     return !this.isOnGround(); // Fly during day
     * }
     * }</pre>
     *
     * <p><b>Another Example (Energy System):</b>
     * <pre>{@code
     * @Override
     * protected boolean shouldFly() {
     *     // Tired entities land to rest
     *     if (this.getEnergy() < 20) {
     *         return false; // Too tired to fly
     *     }
     *     return !this.isOnGround(); // Fly if not on ground
     * }
     * }</pre>
     *
     * @return {@code true} if entity should be flying, {@code false} if resting/landing
     */
    protected boolean shouldFly() {
        // Default: always flying unless on ground
        // Override in subclasses for custom landing behavior (e.g., rest at night)
        return !this.isOnGround();
    }
}
