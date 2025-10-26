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
 * AI goal for flying mobs to wander randomly in 3D space.
 *
 * <p>This goal enables flying entities (like flies, bats, birds) to move freely through the air
 * by selecting random target positions in 3D space. Unlike ground-based wandering goals that only
 * consider X/Z coordinates, this goal includes Y-axis movement for true aerial navigation.
 *
 * <h2>Behavior:</h2>
 * <ul>
 *   <li><b>Random Target Selection</b>: Picks random positions in 3D space within a configurable range</li>
 *   <li><b>Minimum Height</b>: Prefers staying above ground (default 2 blocks minimum)</li>
 *   <li><b>Block Avoidance</b>: Validates targets to avoid solid blocks and invalid positions</li>
 *   <li><b>Periodic Updates</b>: Selects new target every 20-40 ticks (1-2 seconds)</li>
 *   <li><b>Speed Control</b>: Configurable movement speed during wandering</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class FlyEntity extends FlyingAnimalEntity {
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Priority 3: Wander in air when idle (speed = 0.6, range = 7 blocks)
 *         this.goalSelector.add(3, new WanderInAirGoal(this, 0.6D, 7.0D));
 *     }
 * }
 * }</pre>
 *
 * <h2>Implementation Details:</h2>
 * <p>This goal uses Minecraft's navigation system to pathfind to the target position.
 * Flying mobs should use {@link net.minecraft.entity.ai.pathing.BirdNavigation} for proper
 * 3D pathfinding support. The goal automatically retries if pathfinding fails.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. AI goals are ticked on the server thread only.
 * Do not call methods from this class on multiple threads concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>{@link #tick()} is called every game tick - keep operations lightweight</li>
 *   <li>Target selection uses random calculations - acceptable overhead for idle behavior</li>
 *   <li>Block validation checks are performed only when selecting new targets</li>
 *   <li>Consider using higher cooldown values (longer delays) for many flying entities</li>
 * </ul>
 *
 * @author Canya
 * @see Goal
 * @see net.minecraft.entity.ai.pathing.BirdNavigation
 * @see net.minecraft.entity.ai.control.FlightMoveControl
 */
public class WanderInAirGoal extends Goal {

    /**
     * Default minimum height above ground level (in blocks).
     * Flying mobs prefer to stay at least this high to avoid ground obstacles.
     */
    private static final int DEFAULT_MIN_HEIGHT = 2;

    /**
     * Minimum cooldown between target selections (in ticks).
     * Corresponds to approximately 1 second (20 ticks).
     */
    private static final int MIN_COOLDOWN = 20;

    /**
     * Maximum cooldown between target selections (in ticks).
     * Corresponds to approximately 2 seconds (40 ticks).
     */
    private static final int MAX_COOLDOWN = 40;

    /** The flying mob controlled by this goal. */
    private final MobEntity mob;

    /** Movement speed multiplier when wandering (0.0 to 1.0+). */
    private final double speed;

    /** Maximum distance from current position to select targets (in blocks). */
    private final double wanderRange;

    /** Current target position to navigate toward, or null if no target. */
    @Nullable
    private Vec3d targetPosition;

    /** Cooldown timer until next target selection (in ticks). */
    private int cooldownTimer;

    /**
     * Constructs a wander-in-air goal with default range.
     *
     * <p>This constructor uses a default wander range of 10 blocks, suitable for most
     * flying mobs. For more control over wandering distance, use
     * {@link #WanderInAirGoal(MobEntity, double, double)}.
     *
     * @param mob the flying mob that will wander (must not be null)
     * @param speed movement speed multiplier (typically 0.4 to 1.0)
     * @throws NullPointerException if mob is null
     * @throws IllegalArgumentException if speed is negative
     */
    public WanderInAirGoal(MobEntity mob, double speed) {
        this(mob, speed, 10.0D);
    }

    /**
     * Constructs a wander-in-air goal with custom range.
     *
     * <p>This constructor allows full control over wandering behavior. The wander range
     * determines how far from the current position the mob can select random targets.
     * Larger ranges create more exploratory behavior, while smaller ranges keep the mob
     * in a tighter area.
     *
     * <p><b>Typical Values:</b>
     * <ul>
     *   <li><b>Speed</b>: 0.4 to 0.8 for slow wandering, 1.0+ for fast movement</li>
     *   <li><b>Range</b>: 5-7 blocks for confined areas, 10-15 for open exploration</li>
     * </ul>
     *
     * @param mob the flying mob that will wander (must not be null)
     * @param speed movement speed multiplier (typically 0.4 to 1.0)
     * @param wanderRange maximum distance to select targets from current position (in blocks)
     * @throws NullPointerException if mob is null
     * @throws IllegalArgumentException if speed or wanderRange is negative
     */
    public WanderInAirGoal(MobEntity mob, double speed, double wanderRange) {
        this.mob = Objects.requireNonNull(mob, "Mob cannot be null");
        if (speed < 0.0D) {
            throw new IllegalArgumentException("Speed cannot be negative: " + speed);
        }
        if (wanderRange < 0.0D) {
            throw new IllegalArgumentException("Wander range cannot be negative: " + wanderRange);
        }
        this.speed = speed;
        this.wanderRange = wanderRange;
        this.targetPosition = null;
        this.cooldownTimer = 0;

        // This goal controls both movement and looking direction
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    // ============================================
    // Goal Lifecycle Methods
    // ============================================

    /**
     * Checks if this goal can start executing.
     *
     * <p>This goal can start if:
     * <ul>
     *   <li>The mob has no navigation target (not busy pathfinding)</li>
     *   <li>The mob is not being controlled by a player (e.g., leash, vehicle)</li>
     *   <li>The cooldown timer has expired (ready for new target)</li>
     * </ul>
     *
     * @return {@code true} if the goal can start, {@code false} otherwise
     */
    @Override
    public boolean canStart() {
        // Don't start if mob is busy pathfinding to another goal's target
        if (mob.getNavigation().isFollowingPath()) {
            return false;
        }

        // Don't start if mob is being controlled (leash, vehicle, etc.)
        if (mob.hasControllingPassenger()) {
            return false;
        }

        // Don't start if cooldown is active (recently selected a target)
        if (cooldownTimer > 0) {
            return false;
        }

        // Try to find a valid target position
        return findNewTarget();
    }

    /**
     * Checks if this goal should continue executing.
     *
     * <p>The goal continues as long as:
     * <ul>
     *   <li>There is a target position set</li>
     *   <li>The mob is actively navigating toward that target</li>
     *   <li>The mob is not being controlled by a player</li>
     * </ul>
     *
     * @return {@code true} if the goal should continue, {@code false} to stop
     */
    @Override
    public boolean shouldContinue() {
        // Stop if target was cleared
        if (targetPosition == null) {
            return false;
        }

        // Stop if mob is no longer navigating (reached target or gave up)
        if (!mob.getNavigation().isFollowingPath()) {
            return false;
        }

        // Stop if mob is being controlled
        return !mob.hasControllingPassenger();
    }

    /**
     * Starts executing this goal.
     *
     * <p>Begins pathfinding toward the selected target position. If pathfinding fails,
     * the target is cleared and the goal will retry on the next canStart() check.
     */
    @Override
    public void start() {
        if (targetPosition != null) {
            // Navigate to the target position at configured speed
            boolean pathfindSuccess = mob.getNavigation().startMovingTo(
                targetPosition.x,
                targetPosition.y,
                targetPosition.z,
                speed
            );

            // Clear target if pathfinding failed (invalid position or unreachable)
            if (!pathfindSuccess) {
                targetPosition = null;
                setCooldown();
            }
        }
    }

    /**
     * Stops executing this goal.
     *
     * <p>Clears the current target and sets a cooldown before the next target can be selected.
     * This prevents the mob from immediately selecting a new target and creates more natural
     * wandering behavior with pauses.
     */
    @Override
    public void stop() {
        targetPosition = null;
        setCooldown();
        // Navigation is automatically cleared by Minecraft's goal system
    }

    /**
     * Ticks this goal every game tick while it's executing.
     *
     * <p>Decrements the cooldown timer if active. This method is called every tick (20 times
     * per second), so it must be lightweight to avoid performance issues.
     *
     * <p><b>Performance Note:</b> This method does minimal work intentionally. Target selection
     * and pathfinding only occur in {@link #canStart()} and {@link #start()}.
     */
    @Override
    public void tick() {
        // Decrement cooldown timer (handled here for simplicity)
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }
    }

    // ============================================
    // Target Selection Logic
    // ============================================

    /**
     * Attempts to find a new valid target position for wandering.
     *
     * <p>This method tries up to 10 times to find a suitable position by:
     * <ol>
     *   <li>Selecting a random offset within the wander range</li>
     *   <li>Adjusting Y coordinate to prefer staying above ground</li>
     *   <li>Validating the target is not inside a solid block</li>
     *   <li>Checking the position is within world bounds</li>
     * </ol>
     *
     * <p>If no valid target is found after 10 attempts, the method returns false and
     * a cooldown is set before retrying.
     *
     * @return {@code true} if a valid target was found, {@code false} otherwise
     */
    private boolean findNewTarget() {
        World world = mob.getWorld();
        Vec3d currentPos = mob.getPos();

        // Try up to 10 times to find a valid target
        for (int attempt = 0; attempt < 10; attempt++) {
            // Random offset in 3D space within wander range
            double offsetX = (mob.getRandom().nextDouble() * 2.0D - 1.0D) * wanderRange;
            double offsetY = (mob.getRandom().nextDouble() * 2.0D - 1.0D) * wanderRange;
            double offsetZ = (mob.getRandom().nextDouble() * 2.0D - 1.0D) * wanderRange;

            // Calculate target position
            Vec3d target = currentPos.add(offsetX, offsetY, offsetZ);

            // Ensure target is above minimum height
            BlockPos groundPos = BlockPos.ofFloored(target.x, target.y - 1, target.z);
            int groundHeight = world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING,
                groundPos.getX(), groundPos.getZ());
            if (target.y < groundHeight + DEFAULT_MIN_HEIGHT) {
                target = new Vec3d(target.x, groundHeight + DEFAULT_MIN_HEIGHT, target.z);
            }

            // Validate target position
            if (isValidTarget(target)) {
                this.targetPosition = target;
                return true;
            }
        }

        // Failed to find valid target after 10 attempts - set cooldown
        setCooldown();
        return false;
    }

    /**
     * Validates whether a target position is suitable for wandering.
     *
     * <p>A position is valid if:
     * <ul>
     *   <li>It is within world height bounds (not void, not above build limit)</li>
     *   <li>The block at that position is not solid (mob can occupy the space)</li>
     * </ul>
     *
     * @param target the target position to validate
     * @return {@code true} if the position is valid, {@code false} otherwise
     */
    private boolean isValidTarget(Vec3d target) {
        World world = mob.getWorld();
        BlockPos targetPos = BlockPos.ofFloored(target);

        // Check world height bounds
        if (!world.isInBuildLimit(targetPos)) {
            return false;
        }

        // Check if position is inside a solid block
        return !world.getBlockState(targetPos).isSolidBlock(world, targetPos);
    }

    /**
     * Sets a random cooldown before the next target selection attempt.
     *
     * <p>The cooldown is randomized between {@link #MIN_COOLDOWN} and {@link #MAX_COOLDOWN}
     * to create varied, natural-looking wandering behavior. This prevents the mob from
     * constantly picking new targets and creates brief pauses in movement.
     */
    private void setCooldown() {
        this.cooldownTimer = MIN_COOLDOWN + mob.getRandom().nextInt(MAX_COOLDOWN - MIN_COOLDOWN + 1);
    }
}
