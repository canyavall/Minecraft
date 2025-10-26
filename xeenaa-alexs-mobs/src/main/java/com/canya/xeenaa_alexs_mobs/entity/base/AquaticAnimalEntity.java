package com.canya.xeenaa_alexs_mobs.entity.base;

import com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.world.World;

/**
 * Base class for all aquatic animal entities.
 *
 * <p>This abstract class provides common functionality for all swimming mobs in Xeenaa's Alex's Mobs,
 * including water-based pathfinding, water survival requirements, breathing mechanics, and floating
 * on death. All aquatic animal entities should extend this class to ensure consistent swimming behavior.
 *
 * <h2>Features Provided:</h2>
 * <ul>
 *   <li><b>Swim Navigation</b>: Uses {@link SwimNavigation} for underwater pathfinding (like fish)</li>
 *   <li><b>Water Survival</b>: Dies outside water after configurable time (default 30 seconds)</li>
 *   <li><b>Float on Death</b>: Corpse floats to surface like vanilla fish</li>
 *   <li><b>Water Currents</b>: Not pushed by water flow (can hold position)</li>
 * </ul>
 *
 * <h2>Air Supply System:</h2>
 * <p>Aquatic entities have a limited air supply when outside water:
 * <ul>
 *   <li><b>In water</b>: Air supply constantly replenished</li>
 *   <li><b>Out of water</b>: Air supply depletes at 1 tick per tick (20 ticks/second)</li>
 *   <li><b>Air depleted</b>: Entity takes {@code dryOut} damage (1.0 per tick)</li>
 *   <li><b>Default survival time</b>: 600 ticks (30 seconds)</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class TriopsEntity extends AquaticAnimalEntity {
 *
 *     public TriopsEntity(EntityType<? extends AquaticAnimalEntity> type, World world) {
 *         super(type, world);
 *     }
 *
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Add triops-specific AI goals here
 *         this.goalSelector.add(3, new SwimAroundGoal(this, 1.0, 10));
 *     }
 *
 *     @Override
 *     protected SoundEvent getAmbientSound() {
 *         return ModSounds.TRIOPS_AMBIENT;
 *     }
 *
 *     @Override
 *     protected SoundEvent getHurtSound(DamageSource source) {
 *         return ModSounds.TRIOPS_HURT;
 *     }
 *
 *     @Override
 *     protected SoundEvent getDeathSound() {
 *         return ModSounds.TRIOPS_DEATH;
 *     }
 *
 *     @Override
 *     public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
 *         return ModEntities.TRIOPS.create(world);
 *     }
 *
 *     @Override
 *     protected int getMaxAirSupply() {
 *         // Triops can survive 1 minute out of water (1200 ticks)
 *         return 1200;
 *     }
 *
 *     public static DefaultAttributeContainer.Builder createAttributes() {
 *         return AquaticAnimalEntity.createMobAttributes()
 *             .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0)
 *             .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4);
 *     }
 * }
 * }</pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. All methods should be called from the server thread
 * (for ServerWorld) or the client thread (for ClientWorld). Entity ticking and air supply
 * management are handled by Minecraft's entity system and should not be called concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>{@link #tick()} runs every game tick (20 times/second) - keep air supply logic lightweight</li>
 *   <li>Water checks ({@link #isInsideWaterOrBubbleColumn()}) are optimized by Minecraft</li>
 *   <li>Avoid expensive computations in {@link #getMaxAirSupply()} - return a constant</li>
 * </ul>
 *
 * @author Canya
 * @see BaseAnimalEntity
 * @see SwimNavigation
 */
public abstract class AquaticAnimalEntity extends BaseAnimalEntity {

    /**
     * Current air supply in ticks (600 ticks = 30 seconds).
     *
     * <p>This value decreases by 1 each tick when the entity is outside water.
     * When it reaches 0, the entity starts taking {@code dryOut} damage.
     */
    private int airSupply = 600;

    /**
     * Constructs a new aquatic animal entity.
     *
     * <p>This constructor initializes the entity with full air supply (600 ticks).
     * The air supply will deplete when the entity is outside water and replenish
     * when the entity returns to water.
     *
     * @param type the entity type for this aquatic animal (from {@code ModEntities})
     * @param world the world this entity is spawning in (server or client)
     * @throws NullPointerException if type or world is null
     */
    protected AquaticAnimalEntity(EntityType<? extends AquaticAnimalEntity> type, World world) {
        super(type, world);
    }

    // ============================================
    // Navigation System
    // ============================================

    /**
     * Creates the navigation system for underwater pathfinding.
     *
     * <p>Aquatic entities use {@link SwimNavigation} which enables pathfinding through water.
     * This is the same navigation system used by vanilla fish (cod, salmon, tropical fish).
     *
     * <p><b>Navigation Behavior:</b>
     * <ul>
     *   <li>Prefers water blocks for pathfinding</li>
     *   <li>Avoids land when possible</li>
     *   <li>Can pathfind through flowing water</li>
     * </ul>
     *
     * <p><b>DO NOT override this method</b> unless you need custom navigation behavior.
     * The default swim navigation is suitable for all standard aquatic mobs.
     *
     * @param world the world this entity is in
     * @return a configured {@link SwimNavigation} instance for underwater pathfinding
     */
    @Override
    protected EntityNavigation createNavigation(World world) {
        // Use swim navigation for underwater pathfinding (like fish)
        return new SwimNavigation(this, world);
    }

    // ============================================
    // Aquatic Properties
    // ============================================

    /**
     * Checks if this entity should be pushed by water currents.
     *
     * <p>Aquatic animals are not pushed by water currents, allowing them to maintain their
     * position and swim against the flow. This is consistent with vanilla fish behavior.
     *
     * <p><b>DO NOT override this method</b> unless you want water to push your mob around.
     *
     * @return always {@code false} (aquatic animals control their position in water)
     */
    @Override
    public boolean isPushedByFluids() {
        // Not pushed by water - allows entities to control their position
        // This is the same as vanilla fish behavior
        return false;
    }

    // ============================================
    // Air Supply System
    // ============================================

    /**
     * Updates the entity's air supply when outside water.
     *
     * <p>This method is called every game tick (20 times per second). It handles the air supply
     * depletion system:
     * <ul>
     *   <li><b>In water</b>: Reset air supply to maximum (entity is safe)</li>
     *   <li><b>Out of water</b>: Decrease air supply by 1 per tick</li>
     *   <li><b>Air depleted</b>: Deal 1.0 damage per tick from {@code dryOut} damage source</li>
     * </ul>
     *
     * <p><b>Performance Note:</b> This runs every tick on the server side only. The client
     * does not handle air supply logic.
     *
     * <p><b>DO NOT override this method</b> unless you need custom air supply behavior.
     * To customize survival time, override {@link #getMaxAirSupply()} instead.
     */
    @Override
    public void tick() {
        super.tick();

        // Only run air supply logic on server side
        if (!this.getWorld().isClient) {
            // Handle air supply when out of water
            if (this.isInsideWaterOrBubbleColumn()) {
                // In water: reset air supply to maximum (safe)
                airSupply = getMaxAirSupply();
            } else {
                // Out of water: countdown to death
                airSupply--;
                if (airSupply <= 0) {
                    // Die after air supply depleted (default 30 seconds)
                    // Use dryOut damage source (same as guardians/fish out of water)
                    this.damage(this.getDamageSources().dryOut(), 1.0F);
                }
            }
        }
    }

    /**
     * Gets the maximum time (in ticks) this entity can survive outside water.
     *
     * <p>Override this method to customize how long your aquatic mob can survive on land.
     * The default value is 600 ticks (30 seconds at 20 ticks per second).
     *
     * <p><b>Example Values:</b>
     * <ul>
     *   <li>600 ticks = 30 seconds (default - like tropical fish)</li>
     *   <li>1200 ticks = 1 minute (hardier aquatic mobs)</li>
     *   <li>6000 ticks = 5 minutes (amphibious mobs)</li>
     * </ul>
     *
     * <p><b>Performance Note:</b> Return a constant value - this may be called frequently.
     * Avoid expensive computations.
     *
     * <p><b>Usage Example:</b>
     * <pre>{@code
     * @Override
     * protected int getMaxAirSupply() {
     *     return 1200; // Can survive 1 minute out of water
     * }
     * }</pre>
     *
     * @return maximum ticks this entity can survive outside water (default 600)
     */
    protected int getMaxAirSupply() {
        return 600; // Default: 30 seconds (600 ticks at 20 ticks/second)
    }

    // ============================================
    // Death Behavior
    // ============================================

    /**
     * Handles post-death physics for aquatic entities.
     *
     * <p>When an aquatic entity dies in water, its corpse floats to the surface similar to
     * vanilla fish. This is achieved by applying a small upward velocity each tick while
     * the entity is in water and not on the ground.
     *
     * <p><b>Floating Behavior:</b>
     * <ul>
     *   <li>Applies upward velocity of 0.02 per tick</li>
     *   <li>Only activates when in water and not on ground</li>
     *   <li>Stops floating once entity reaches surface or lands on block</li>
     * </ul>
     *
     * <p><b>DO NOT override this method</b> unless you want custom death physics.
     * The default floating behavior matches vanilla fish.
     */
    @Override
    protected void updatePostDeath() {
        // Call parent to handle standard death physics
        super.updatePostDeath();

        // Float to surface on death (like vanilla fish)
        // Only apply upward velocity if in water and not on ground
        if (!this.isOnGround() && this.isTouchingWater()) {
            // Apply small upward velocity to float to surface
            this.setVelocity(this.getVelocity().add(0.0, 0.02, 0.0));
        }
    }
}
