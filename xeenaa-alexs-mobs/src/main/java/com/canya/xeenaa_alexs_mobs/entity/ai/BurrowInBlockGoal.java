package com.canya.xeenaa_alexs_mobs.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * AI goal for mobs to burrow into specific blocks (sand, gravel) when idle.
 *
 * <p>This goal enables entities (like triops, sandfish, crabs) to hide within certain block types
 * by pathfinding to a burrowable block and entering a "burrowed" state. While burrowed, the mob
 * typically becomes invisible/invulnerable and remains hidden for a duration before re-emerging.
 *
 * <h2>Behavior:</h2>
 * <ul>
 *   <li><b>Block Search</b>: Finds nearby burrowable blocks (configurable types like sand/gravel)</li>
 *   <li><b>Pathfinding</b>: Navigates to the nearest suitable block</li>
 *   <li><b>Burrow State</b>: Enters "burrowed" mode (invisible/invulnerable)</li>
 *   <li><b>Duration</b>: Stays burrowed for configurable time (default 5-10 seconds)</li>
 *   <li><b>Re-emergence</b>: Returns to normal state after duration expires</li>
 *   <li><b>Safety-Based</b>: Only burrows when not in danger, breeding, or otherwise engaged</li>
 * </ul>
 *
 * <h2>Burrowed State:</h2>
 * <p>When burrowed, the mob is expected to have:
 * <ul>
 *   <li><b>Visual Effect</b>: Invisible flag set (entity rendering hidden)</li>
 *   <li><b>Protection</b>: Invulnerable flag set (no damage taken)</li>
 *   <li><b>Collision</b>: No collisions with other entities</li>
 *   <li><b>AI</b>: Most other goals should not execute while burrowed</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class TriopsEntity extends BaseAnimalEntity {
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Priority 4: Burrow in sand when idle (5-10 second duration)
 *         this.goalSelector.add(4, new BurrowInBlockGoal(
 *             this,
 *             0.8D,
 *             List.of(Blocks.SAND, Blocks.RED_SAND),
 *             100,  // 5 seconds min
 *             200   // 10 seconds max
 *         ));
 *     }
 * }
 * }</pre>
 *
 * <h2>Implementation Details:</h2>
 * <p>This goal manages state transitions between surface and burrowed modes. The entity
 * should override {@link #onBurrow()} and {@link #onEmerge()} to handle visual/gameplay
 * effects like invisibility, invulnerability, and particle effects.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. AI goals are ticked on the server thread only.
 * Do not call methods from this class on multiple threads concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Block search is performed only when starting the goal - not every tick</li>
 *   <li>Burrowing mobs consume minimal performance while hidden (no rendering)</li>
 *   <li>Consider limiting number of nearby burrowable blocks to prevent expensive searches</li>
 *   <li>Duration timer is lightweight (simple integer decrement per tick)</li>
 * </ul>
 *
 * @author Canya
 * @see Goal
 * @see PathAwareEntity
 */
public class BurrowInBlockGoal extends Goal {

    /**
     * Default minimum burrow duration (in ticks).
     * Corresponds to approximately 5 seconds (100 ticks).
     */
    private static final int DEFAULT_MIN_DURATION = 100;

    /**
     * Default maximum burrow duration (in ticks).
     * Corresponds to approximately 10 seconds (200 ticks).
     */
    private static final int DEFAULT_MAX_DURATION = 200;

    /**
     * Search radius for finding burrowable blocks (in blocks).
     * Larger values increase search cost but allow finding more distant hiding spots.
     */
    private static final int SEARCH_RADIUS = 8;

    /**
     * Cooldown between burrow attempts (in ticks).
     * Prevents spamming burrow searches when no suitable block is found.
     */
    private static final int BURROW_COOLDOWN = 100;

    /**
     * States for the burrowing state machine.
     */
    private enum BurrowState {
        /** Not burrowed, searching for a burrowable block. */
        SEARCHING,
        /** Pathfinding to a burrowable block. */
        APPROACHING,
        /** Currently burrowed in a block. */
        BURROWED,
        /** Emerging from burrow. */
        EMERGING
    }

    /** The mob controlled by this goal. */
    private final PathAwareEntity mob;

    /** Movement speed multiplier when approaching burrow location (0.0 to 1.0+). */
    private final double speed;

    /** List of block types the mob can burrow into (e.g., sand, gravel). */
    private final List<Block> burrowableBlocks;

    /** Minimum duration to stay burrowed (in ticks). */
    private final int minBurrowDuration;

    /** Maximum duration to stay burrowed (in ticks). */
    private final int maxBurrowDuration;

    /** Current state of the burrowing state machine. */
    private BurrowState currentState;

    /** Target position to burrow into, or null if no target. */
    @Nullable
    private BlockPos targetBlock;

    /** Remaining time to stay burrowed (in ticks), or 0 if not burrowed. */
    private int burrowTimer;

    /** Cooldown timer until next burrow attempt (in ticks). */
    private int cooldownTimer;

    /**
     * Constructs a burrow-in-block goal with default duration.
     *
     * <p>This constructor uses default burrow duration values:
     * <ul>
     *   <li><b>Min Duration</b>: 100 ticks (5 seconds)</li>
     *   <li><b>Max Duration</b>: 200 ticks (10 seconds)</li>
     * </ul>
     *
     * @param mob the mob that will burrow (must not be null)
     * @param speed movement speed multiplier when approaching burrow (typically 0.6 to 1.0)
     * @param burrowableBlocks list of block types to burrow into (must not be null or empty)
     * @throws NullPointerException if mob or burrowableBlocks is null
     * @throws IllegalArgumentException if speed is negative or burrowableBlocks is empty
     */
    public BurrowInBlockGoal(PathAwareEntity mob, double speed, List<Block> burrowableBlocks) {
        this(mob, speed, burrowableBlocks, DEFAULT_MIN_DURATION, DEFAULT_MAX_DURATION);
    }

    /**
     * Constructs a burrow-in-block goal with custom duration.
     *
     * <p>This constructor allows full control over burrowing behavior. The burrow duration
     * is randomized between minBurrowDuration and maxBurrowDuration each time the mob burrows,
     * creating varied, natural-looking hiding behavior.
     *
     * <p><b>Typical Values:</b>
     * <ul>
     *   <li><b>Speed</b>: 0.6 to 0.8 for slow, cautious approach</li>
     *   <li><b>Min Duration</b>: 60-100 ticks (3-5 seconds) for brief hiding</li>
     *   <li><b>Max Duration</b>: 200-400 ticks (10-20 seconds) for extended hiding</li>
     * </ul>
     *
     * @param mob the mob that will burrow (must not be null)
     * @param speed movement speed multiplier when approaching burrow (typically 0.6 to 1.0)
     * @param burrowableBlocks list of block types to burrow into (must not be null or empty)
     * @param minBurrowDuration minimum time to stay burrowed in ticks
     * @param maxBurrowDuration maximum time to stay burrowed in ticks
     * @throws NullPointerException if mob or burrowableBlocks is null
     * @throws IllegalArgumentException if speed is negative, burrowableBlocks is empty,
     *         or duration values are invalid
     */
    public BurrowInBlockGoal(PathAwareEntity mob, double speed, List<Block> burrowableBlocks,
                             int minBurrowDuration, int maxBurrowDuration) {
        this.mob = Objects.requireNonNull(mob, "Mob cannot be null");
        if (speed < 0.0D) {
            throw new IllegalArgumentException("Speed cannot be negative: " + speed);
        }
        Objects.requireNonNull(burrowableBlocks, "Burrowable blocks cannot be null");
        if (burrowableBlocks.isEmpty()) {
            throw new IllegalArgumentException("Burrowable blocks list cannot be empty");
        }
        if (minBurrowDuration < 1) {
            throw new IllegalArgumentException("Min burrow duration must be at least 1: " + minBurrowDuration);
        }
        if (maxBurrowDuration < minBurrowDuration) {
            throw new IllegalArgumentException("Max burrow duration must be >= min duration: " +
                maxBurrowDuration + " < " + minBurrowDuration);
        }

        this.speed = speed;
        this.burrowableBlocks = List.copyOf(burrowableBlocks); // Immutable copy
        this.minBurrowDuration = minBurrowDuration;
        this.maxBurrowDuration = maxBurrowDuration;
        this.currentState = BurrowState.SEARCHING;
        this.targetBlock = null;
        this.burrowTimer = 0;
        this.cooldownTimer = 0;

        // This goal controls movement and target selection
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
     *   <li>The mob is idle (not in danger, not breeding, no target)</li>
     *   <li>The cooldown timer has expired (not recently attempted burrow)</li>
     *   <li>A burrowable block can be found within search range</li>
     * </ul>
     *
     * @return {@code true} if the goal can start, {@code false} otherwise
     */
    @Override
    public boolean canStart() {
        // Don't start if cooldown is active
        if (cooldownTimer > 0) {
            cooldownTimer--;
            return false;
        }

        // Don't start if mob is busy or in danger
        if (!isSafeToburrow()) {
            return false;
        }

        // Try to find a burrowable block
        return findBurrowableBlock();
    }

    /**
     * Checks if this goal should continue executing.
     *
     * <p>The goal continues through the full burrow cycle:
     * <ol>
     *   <li><b>APPROACHING</b>: Until mob reaches target block</li>
     *   <li><b>BURROWED</b>: Until burrow timer expires</li>
     *   <li><b>EMERGING</b>: Brief transition (1 tick)</li>
     * </ol>
     *
     * <p>The goal stops if the mob is no longer safe (danger detected) or if pathfinding fails.
     *
     * @return {@code true} if the goal should continue, {@code false} to stop
     */
    @Override
    public boolean shouldContinue() {
        // Stop if no longer safe (danger detected)
        if (!isSafeToburrow()) {
            return false;
        }

        // Continue based on current state
        return switch (currentState) {
            case SEARCHING -> false; // Shouldn't be in this state during execution
            case APPROACHING -> mob.getNavigation().isFollowingPath();
            case BURROWED -> burrowTimer > 0;
            case EMERGING -> false; // Transition state (1 tick only)
        };
    }

    /**
     * Starts executing this goal.
     *
     * <p>Begins pathfinding toward the burrowable block. If pathfinding fails,
     * the goal is abandoned and a cooldown is set.
     */
    @Override
    public void start() {
        if (targetBlock != null) {
            currentState = BurrowState.APPROACHING;

            // Navigate to the burrowable block
            boolean pathfindSuccess = mob.getNavigation().startMovingTo(
                targetBlock.getX() + 0.5,
                targetBlock.getY(),
                targetBlock.getZ() + 0.5,
                speed
            );

            // Abandon if pathfinding failed
            if (!pathfindSuccess) {
                targetBlock = null;
                currentState = BurrowState.SEARCHING;
                cooldownTimer = BURROW_COOLDOWN;
            }
        }
    }

    /**
     * Stops executing this goal.
     *
     * <p>If the mob is burrowed when stopped (e.g., due to danger), it immediately emerges.
     * Otherwise, the goal simply resets its state.
     */
    @Override
    public void stop() {
        // If burrowed, force emergence
        if (currentState == BurrowState.BURROWED) {
            emerge();
        }

        // Reset state
        targetBlock = null;
        currentState = BurrowState.SEARCHING;
        burrowTimer = 0;
        mob.getNavigation().stop();
    }

    /**
     * Ticks this goal every game tick while it's executing.
     *
     * <p>This method manages state transitions:
     * <ul>
     *   <li><b>APPROACHING</b>: Check if reached target, transition to BURROWED</li>
     *   <li><b>BURROWED</b>: Decrement timer, transition to EMERGING when expired</li>
     *   <li><b>EMERGING</b>: Complete emergence, end goal</li>
     * </ul>
     */
    @Override
    public void tick() {
        switch (currentState) {
            case APPROACHING -> tickApproaching();
            case BURROWED -> tickBurrowed();
            case EMERGING -> tickEmerging();
        }
    }

    // ============================================
    // State Machine Ticking
    // ============================================

    /**
     * Ticks the APPROACHING state.
     *
     * <p>Checks if the mob has reached the target block. If so, transitions to BURROWED state.
     */
    private void tickApproaching() {
        if (targetBlock == null) {
            return;
        }

        // Check if mob reached the target block (within 1.5 blocks)
        if (mob.getBlockPos().isWithinDistance(targetBlock, 1.5)) {
            burrow();
        }
    }

    /**
     * Ticks the BURROWED state.
     *
     * <p>Decrements the burrow timer. When it reaches zero, transitions to EMERGING state.
     */
    private void tickBurrowed() {
        burrowTimer--;

        // Time to emerge
        if (burrowTimer <= 0) {
            currentState = BurrowState.EMERGING;
        }
    }

    /**
     * Ticks the EMERGING state.
     *
     * <p>Completes the emergence process. This is a brief transition state.
     */
    private void tickEmerging() {
        emerge();
        // Goal will end on next tick (shouldContinue returns false for EMERGING)
    }

    // ============================================
    // State Transition Methods
    // ============================================

    /**
     * Transitions the mob into the BURROWED state.
     *
     * <p>This method:
     * <ol>
     *   <li>Stops navigation</li>
     *   <li>Sets the burrowed state and timer</li>
     *   <li>Applies visual/gameplay effects (invisibility, invulnerability)</li>
     *   <li>Calls {@link #onBurrow()} for entity-specific behavior</li>
     * </ol>
     */
    private void burrow() {
        // Stop moving
        mob.getNavigation().stop();

        // Transition to burrowed state
        currentState = BurrowState.BURROWED;

        // Set random burrow duration
        burrowTimer = minBurrowDuration +
            mob.getRandom().nextInt(maxBurrowDuration - minBurrowDuration + 1);

        // Apply burrowed effects
        mob.setInvisible(true);
        mob.setInvulnerable(true);

        // Entity-specific burrow behavior (particles, sounds, etc.)
        onBurrow();
    }

    /**
     * Transitions the mob out of the BURROWED state.
     *
     * <p>This method:
     * <ol>
     *   <li>Removes visual/gameplay effects (visibility restored, vulnerability restored)</li>
     *   <li>Resets state back to SEARCHING</li>
     *   <li>Calls {@link #onEmerge()} for entity-specific behavior</li>
     * </ol>
     */
    private void emerge() {
        // Remove burrowed effects
        mob.setInvisible(false);
        mob.setInvulnerable(false);

        // Transition to searching state (goal will end)
        currentState = BurrowState.SEARCHING;

        // Entity-specific emerge behavior (particles, sounds, etc.)
        onEmerge();
    }

    // ============================================
    // Block Search Logic
    // ============================================

    /**
     * Attempts to find a nearby burrowable block within search range.
     *
     * <p>This method scans a cubic region around the mob, looking for blocks that match
     * the configured burrowable block types. It selects the nearest valid block found.
     *
     * <p>If no suitable block is found, a cooldown is set before retrying to prevent
     * spamming expensive searches.
     *
     * @return {@code true} if a burrowable block was found, {@code false} otherwise
     */
    private boolean findBurrowableBlock() {
        World world = mob.getWorld();
        BlockPos mobPos = mob.getBlockPos();

        BlockPos nearestBlock = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        // Search in a cubic region around the mob
        for (BlockPos pos : BlockPos.iterateOutwards(mobPos, SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS)) {
            BlockState state = world.getBlockState(pos);

            // Check if this is a burrowable block
            if (!isBurrowableBlock(state.getBlock())) {
                continue;
            }

            // Check if position is valid (accessible)
            if (!isValidBurrowPosition(world, pos)) {
                continue;
            }

            // Track nearest block
            double distanceSq = pos.getSquaredDistance(mobPos);
            if (distanceSq < nearestDistanceSq) {
                nearestDistanceSq = distanceSq;
                nearestBlock = pos;
            }
        }

        // If we found a burrowable block, set it as target
        if (nearestBlock != null) {
            this.targetBlock = nearestBlock;
            return true;
        }

        // No burrowable block found - set cooldown
        cooldownTimer = BURROW_COOLDOWN;
        return false;
    }

    /**
     * Checks if a block type is burrowable.
     *
     * @param block the block to check
     * @return {@code true} if the block is in the burrowable blocks list
     */
    private boolean isBurrowableBlock(Block block) {
        return burrowableBlocks.contains(block);
    }

    /**
     * Validates whether a position is suitable for burrowing.
     *
     * <p>A position is valid if:
     * <ul>
     *   <li>The block above is air (mob can enter from above)</li>
     *   <li>The block is within world bounds</li>
     * </ul>
     *
     * @param world the world to check
     * @param pos the position to validate
     * @return {@code true} if the position is valid for burrowing
     */
    private boolean isValidBurrowPosition(World world, BlockPos pos) {
        // Check if position is within world bounds
        if (!world.isInBuildLimit(pos)) {
            return false;
        }

        // Check if block above is air (mob can enter from above)
        BlockPos above = pos.up();
        return world.getBlockState(above).isAir();
    }

    // ============================================
    // Safety Checks
    // ============================================

    /**
     * Checks if it's safe for the mob to burrow.
     *
     * <p>It's safe to burrow if:
     * <ul>
     *   <li>The mob is not in danger (not on fire, not in lava, no attacker)</li>
     *   <li>The mob is not breeding (not in love mode)</li>
     *   <li>The mob has no attack target</li>
     * </ul>
     *
     * @return {@code true} if safe to burrow, {@code false} if the mob is busy or endangered
     */
    private boolean isSafeToburrow() {
        // Not safe if in danger
        if (mob.isOnFire() || mob.isInLava() || mob.getAttacker() != null) {
            return false;
        }

        // Not safe if breeding (check if mob is AnimalEntity)
        if (mob instanceof net.minecraft.entity.passive.AnimalEntity animal && animal.isInLove()) {
            return false;
        }

        // Not safe if has target
        return mob.getTarget() == null;
    }

    // ============================================
    // Extension Hooks
    // ============================================

    /**
     * Called when the mob enters the burrowed state.
     *
     * <p>Override this method in subclasses to add entity-specific effects:
     * <ul>
     *   <li>Play burrow sound</li>
     *   <li>Spawn burrow particles (sand/gravel falling)</li>
     *   <li>Set custom entity data/flags</li>
     * </ul>
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * @Override
     * protected void onBurrow() {
     *     mob.playSound(ModSounds.TRIOPS_BURROW, 1.0F, 1.0F);
     *     ((ServerWorld) mob.getWorld()).spawnParticles(
     *         ParticleTypes.BLOCK,
     *         mob.getX(), mob.getY(), mob.getZ(),
     *         10, 0.5, 0.5, 0.5, 0.1
     *     );
     * }
     * }</pre>
     *
     * <p>The default implementation does nothing.
     */
    protected void onBurrow() {
        // Override in entity-specific goals for custom behavior
    }

    /**
     * Called when the mob emerges from the burrowed state.
     *
     * <p>Override this method in subclasses to add entity-specific effects:
     * <ul>
     *   <li>Play emerge sound</li>
     *   <li>Spawn emerge particles (sand/gravel flying up)</li>
     *   <li>Reset custom entity data/flags</li>
     * </ul>
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * @Override
     * protected void onEmerge() {
     *     mob.playSound(ModSounds.TRIOPS_EMERGE, 1.0F, 1.0F);
     *     ((ServerWorld) mob.getWorld()).spawnParticles(
     *         ParticleTypes.BLOCK,
     *         mob.getX(), mob.getY(), mob.getZ(),
     *         15, 0.5, 0.5, 0.5, 0.2
     *     );
     * }
     * }</pre>
     *
     * <p>The default implementation does nothing.
     */
    protected void onEmerge() {
        // Override in entity-specific goals for custom behavior
    }
}
