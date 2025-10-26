package com.canya.xeenaa_alexs_mobs.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

/**
 * AI goal for flying mobs to occasionally land on blocks and rest.
 *
 * <p>This goal enables flying entities (like flies, birds, butterflies) to periodically
 * land on solid blocks and remain stationary for a brief period before resuming flight.
 * This creates more realistic and varied flying behavior compared to constant aerial movement.
 *
 * <h2>Behavior:</h2>
 * <ul>
 *   <li><b>Target Selection</b>: Finds nearby solid blocks below the mob to land on</li>
 *   <li><b>Landing</b>: Navigates to ground and stops movement when landed</li>
 *   <li><b>Rest Duration</b>: Stays landed for 2-10 seconds (randomized)</li>
 *   <li><b>Resumption</b>: Returns to flight after rest period expires</li>
 *   <li><b>Interrupt</b>: Can be interrupted by higher-priority goals (flee, etc.)</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class FlyEntity extends FlyingAnimalEntity {
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Priority 1: Occasionally land on blocks (rest for 2-10 seconds)
 *         this.goalSelector.add(1, new LandOnBlockGoal(this));
 *     }
 * }
 * }</pre>
 *
 * <h2>Implementation Details:</h2>
 * <p>This goal works by:
 * <ol>
 *   <li>Checking if enough time has passed since last landing attempt (cooldown)</li>
 *   <li>Finding the highest solid block below the mob (up to 10 blocks down)</li>
 *   <li>Navigating to that block position using the mob's navigation system</li>
 *   <li>Setting a rest timer when successfully landed on ground</li>
 *   <li>Clearing navigation and resuming other goals when rest completes</li>
 * </ol>
 *
 * <p><b>Interaction with Other Goals:</b> This goal should have lower priority than
 * critical goals (flee, escape danger) so it can be interrupted when needed. However,
 * it should have higher priority than idle wandering to occasionally override wandering.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. AI goals are ticked on the server thread only.
 * Do not call methods from this class on multiple threads concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>{@link #canStart()} includes block checks - uses cooldown to limit frequency</li>
 *   <li>{@link #tick()} is called every game tick while active - kept lightweight</li>
 *   <li>Block position calculations use simple math operations (minimal overhead)</li>
 *   <li>Cooldown prevents constant landing attempts (reduces pathfinding overhead)</li>
 * </ul>
 *
 * @author Canya
 * @see Goal
 * @see net.minecraft.entity.ai.pathing.BirdNavigation
 * @see com.canya.xeenaa_alexs_mobs.entity.base.FlyingAnimalEntity
 */
public class LandOnBlockGoal extends Goal {

    /**
     * Minimum rest duration when landed on a block (in ticks).
     * Corresponds to approximately 2 seconds (40 ticks).
     */
    private static final int MIN_REST_DURATION = 40; // 2 seconds

    /**
     * Maximum rest duration when landed on a block (in ticks).
     * Corresponds to approximately 10 seconds (200 ticks).
     */
    private static final int MAX_REST_DURATION = 200; // 10 seconds

    /**
     * Minimum cooldown between landing attempts (in ticks).
     * Corresponds to approximately 5 seconds (100 ticks).
     */
    private static final int MIN_LANDING_COOLDOWN = 100; // 5 seconds

    /**
     * Maximum cooldown between landing attempts (in ticks).
     * Corresponds to approximately 15 seconds (300 ticks).
     */
    private static final int MAX_LANDING_COOLDOWN = 300; // 15 seconds

    /**
     * Maximum distance to check below the mob for landing surfaces (in blocks).
     */
    private static final int MAX_LANDING_SEARCH_DEPTH = 10;

    /** The flying mob controlled by this goal. */
    private final MobEntity mob;

    /** Current landing position target, or null if no landing in progress. */
    @Nullable
    private BlockPos landingTarget;

    /** Remaining time to rest on the ground (in ticks), or 0 if not resting. */
    private int restTimer;

    /** Cooldown timer until next landing attempt (in ticks). */
    private int landingCooldown;

    /**
     * Constructs a land-on-block goal.
     *
     * <p>This goal will cause the flying mob to occasionally land on nearby solid blocks
     * and rest for a randomized duration before resuming flight.
     *
     * @param mob the flying mob that will land (must not be null)
     * @throws NullPointerException if mob is null
     */
    public LandOnBlockGoal(MobEntity mob) {
        this.mob = Objects.requireNonNull(mob, "Mob cannot be null");
        this.landingTarget = null;
        this.restTimer = 0;
        this.landingCooldown = getRandomLandingCooldown(); // Start with random cooldown

        // This goal controls movement only (not looking direction)
        this.setControls(EnumSet.of(Control.MOVE));
    }

    // ============================================
    // Goal Lifecycle Methods
    // ============================================

    /**
     * Checks if this goal can start executing.
     *
     * <p>This goal can start if:
     * <ul>
     *   <li>The mob is not on the ground (currently flying)</li>
     *   <li>The landing cooldown has expired (enough time since last landing)</li>
     *   <li>The mob is not being controlled by a player</li>
     *   <li>A valid landing position can be found below the mob</li>
     * </ul>
     *
     * @return {@code true} if the goal can start, {@code false} otherwise
     */
    @Override
    public boolean canStart() {
        // Don't land if already on ground
        if (mob.isOnGround()) {
            return false;
        }

        // Don't land if cooldown is active
        if (landingCooldown > 0) {
            return false;
        }

        // Don't land if being controlled
        if (mob.hasControllingPassenger()) {
            return false;
        }

        // Try to find a landing position below
        return findLandingPosition();
    }

    /**
     * Checks if this goal should continue executing.
     *
     * <p>The goal continues if:
     * <ul>
     *   <li>A landing target is set (goal is active)</li>
     *   <li>Either navigating to target OR resting on ground</li>
     *   <li>The mob is not being controlled</li>
     * </ul>
     *
     * @return {@code true} if the goal should continue, {@code false} to stop
     */
    @Override
    public boolean shouldContinue() {
        // Stop if landing target was cleared
        if (landingTarget == null) {
            return false;
        }

        // Stop if being controlled
        if (mob.hasControllingPassenger()) {
            return false;
        }

        // Continue if still navigating to landing position
        if (mob.getNavigation().isFollowingPath()) {
            return true;
        }

        // Continue if resting on ground (rest timer active)
        return restTimer > 0;
    }

    /**
     * Starts executing this goal.
     *
     * <p>Begins pathfinding toward the landing position. If the mob is already on the
     * ground when this starts, immediately begin resting. Otherwise, navigate to the
     * landing target.
     */
    @Override
    public void start() {
        if (landingTarget == null) {
            return;
        }

        // If already on ground, start resting immediately
        if (mob.isOnGround()) {
            startResting();
            return;
        }

        // Navigate to the landing position
        boolean pathfindSuccess = mob.getNavigation().startMovingTo(
            landingTarget.getX() + 0.5D, // Center of block
            landingTarget.getY() + 1.0D,  // On top of block
            landingTarget.getZ() + 0.5D,
            1.0D // Normal speed
        );

        // Clear target if pathfinding failed
        if (!pathfindSuccess) {
            landingTarget = null;
            setLandingCooldown();
        }
    }

    /**
     * Stops executing this goal.
     *
     * <p>Clears the landing target, rest timer, and sets a cooldown before the next
     * landing attempt. This creates natural spacing between landing behaviors.
     */
    @Override
    public void stop() {
        landingTarget = null;
        restTimer = 0;
        setLandingCooldown();
        // Navigation is automatically cleared by Minecraft's goal system
    }

    /**
     * Ticks this goal every game tick while it's executing.
     *
     * <p>Handles state transitions:
     * <ul>
     *   <li>Decrements rest timer if resting on ground</li>
     *   <li>Decrements cooldown timer if not active</li>
     *   <li>Starts resting when mob lands (on ground + not resting)</li>
     * </ul>
     *
     * <p><b>Performance Note:</b> This method is called every tick (20 times per second).
     * All operations are simple field accesses and decrements, so overhead is minimal.
     */
    @Override
    public void tick() {
        // Decrement cooldown timer if active
        if (landingCooldown > 0) {
            landingCooldown--;
        }

        // If resting, decrement rest timer
        if (restTimer > 0) {
            restTimer--;
            return;
        }

        // If mob landed and not yet resting, start resting
        if (mob.isOnGround() && landingTarget != null && restTimer == 0) {
            startResting();
        }
    }

    // ============================================
    // Landing Logic
    // ============================================

    /**
     * Attempts to find a valid landing position below the mob.
     *
     * <p>This method searches downward from the mob's current position up to
     * {@link #MAX_LANDING_SEARCH_DEPTH} blocks to find the highest solid block.
     * If a valid landing surface is found, it is stored in {@link #landingTarget}.
     *
     * <p><b>Search Algorithm:</b>
     * <ol>
     *   <li>Start at mob's current position</li>
     *   <li>Check each block position downward (up to 10 blocks)</li>
     *   <li>Use world heightmap as fallback if no block found</li>
     *   <li>Validate landing position is solid and safe</li>
     * </ol>
     *
     * @return {@code true} if a valid landing position was found, {@code false} otherwise
     */
    private boolean findLandingPosition() {
        World world = mob.getWorld();
        BlockPos currentPos = mob.getBlockPos();

        // Search downward for solid ground
        BlockPos searchPos = currentPos;
        for (int i = 0; i < MAX_LANDING_SEARCH_DEPTH; i++) {
            searchPos = searchPos.down();

            // Check if this position is a solid block
            if (world.getBlockState(searchPos).isSolidBlock(world, searchPos)) {
                // Found a valid landing position
                this.landingTarget = searchPos;
                return true;
            }
        }

        // Fallback: Use world heightmap to find ground level
        BlockPos heightmapPos = new BlockPos(
            currentPos.getX(),
            world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING, currentPos.getX(), currentPos.getZ()),
            currentPos.getZ()
        );

        // Validate heightmap position is within reasonable range
        if (Math.abs(heightmapPos.getY() - currentPos.getY()) <= MAX_LANDING_SEARCH_DEPTH) {
            BlockPos groundPos = heightmapPos.down(); // Land on top of the heightmap block
            if (isValidLandingPosition(groundPos)) {
                this.landingTarget = groundPos;
                return true;
            }
        }

        // No valid landing position found
        setLandingCooldown(); // Set cooldown before retrying
        return false;
    }

    /**
     * Validates whether a block position is suitable for landing.
     *
     * <p>A position is valid for landing if:
     * <ul>
     *   <li>The block itself is solid (can stand on it)</li>
     *   <li>The block above is air or passable (mob can fit)</li>
     * </ul>
     *
     * @param pos the block position to validate
     * @return {@code true} if the position is valid for landing, {@code false} otherwise
     */
    private boolean isValidLandingPosition(BlockPos pos) {
        World world = mob.getWorld();

        // Check if landing block is solid
        if (!world.getBlockState(pos).isSolidBlock(world, pos)) {
            return false;
        }

        // Check if block above is passable (mob can fit)
        BlockPos above = pos.up();
        return !world.getBlockState(above).isSolidBlock(world, above);
    }

    /**
     * Starts the resting phase after successfully landing.
     *
     * <p>Sets a randomized rest timer between {@link #MIN_REST_DURATION} and
     * {@link #MAX_REST_DURATION}. During this time, the mob remains stationary
     * on the ground.
     */
    private void startResting() {
        // Stop any navigation (stay in place)
        mob.getNavigation().stop();

        // Set random rest duration
        this.restTimer = MIN_REST_DURATION + mob.getRandom().nextInt(MAX_REST_DURATION - MIN_REST_DURATION + 1);
    }

    /**
     * Sets a random cooldown before the next landing attempt.
     *
     * <p>The cooldown is randomized between {@link #MIN_LANDING_COOLDOWN} and
     * {@link #MAX_LANDING_COOLDOWN} to create varied landing behavior. This prevents
     * the mob from constantly attempting to land and creates more natural flight patterns.
     */
    private void setLandingCooldown() {
        this.landingCooldown = getRandomLandingCooldown();
    }

    /**
     * Generates a random landing cooldown value.
     *
     * <p>Cooldown is between {@link #MIN_LANDING_COOLDOWN} and {@link #MAX_LANDING_COOLDOWN}.
     *
     * @return a random cooldown value in ticks
     */
    private int getRandomLandingCooldown() {
        return MIN_LANDING_COOLDOWN + mob.getRandom().nextInt(MAX_LANDING_COOLDOWN - MIN_LANDING_COOLDOWN + 1);
    }
}
