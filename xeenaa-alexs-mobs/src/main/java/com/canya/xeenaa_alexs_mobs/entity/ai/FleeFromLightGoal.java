package com.canya.xeenaa_alexs_mobs.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

/**
 * AI goal for mobs to flee from bright areas and seek darkness.
 *
 * <p>This goal causes light-averse entities (like cockroaches, cave-dwelling creatures) to actively
 * avoid bright areas and pathfind toward darker locations. The mob checks its current light level
 * and, if too bright, searches for a darker area within range to flee to.
 *
 * <h2>Behavior:</h2>
 * <ul>
 *   <li><b>Light Detection</b>: Monitors light level at mob's current position</li>
 *   <li><b>Flee Threshold</b>: Triggers when light level exceeds configurable threshold (default 7)</li>
 *   <li><b>Dark Zone Search</b>: Finds nearby positions with lower light levels</li>
 *   <li><b>Pathfinding</b>: Navigates to the darkest area found within search range</li>
 *   <li><b>Priority Execution</b>: High-priority goal that interrupts idle behaviors</li>
 * </ul>
 *
 * <h2>Light Level Scale:</h2>
 * <pre>
 *  0 - Complete darkness (pitch black)
 *  7 - Spawn threshold for hostile mobs
 *  8 - Dim light (shadows)
 * 11 - Torch at 4 blocks distance
 * 14 - Torch (directly adjacent)
 * 15 - Maximum light (sunlight, lava, glowstone)
 * </pre>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * public class CockroachEntity extends BaseAnimalEntity {
 *     @Override
 *     protected void registerCustomGoals() {
 *         // Priority 1: Flee from bright areas (high priority, interrupts most behaviors)
 *         this.goalSelector.add(1, new FleeFromLightGoal(this, 1.2D, 7, 12.0D));
 *     }
 * }
 * }</pre>
 *
 * <h2>Implementation Details:</h2>
 * <p>This goal uses both block light (torches, lava, glowstone) and sky light (sunlight) to
 * determine total light level. The mob searches in a configurable radius for the darkest area
 * and pathfinds there when in bright conditions.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. AI goals are ticked on the server thread only.
 * Do not call methods from this class on multiple threads concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Light level checks are performed every tick - lightweight operation</li>
 *   <li>Dark zone search is expensive - only runs when fleeing is triggered</li>
 *   <li>Consider smaller search ranges for many light-averse entities</li>
 *   <li>Pathfinding to dark areas may be called frequently in bright biomes</li>
 * </ul>
 *
 * @author Canya
 * @see Goal
 * @see LightType
 * @see PathAwareEntity
 */
public class FleeFromLightGoal extends Goal {

    /**
     * Default light level threshold for fleeing.
     * Light levels above this value trigger flee behavior (7 = hostile mob spawn threshold).
     */
    private static final int DEFAULT_LIGHT_THRESHOLD = 7;

    /**
     * Default search radius for finding dark areas (in blocks).
     * Larger values increase pathfinding cost but allow finding more distant safety.
     */
    private static final double DEFAULT_SEARCH_RANGE = 10.0D;

    /**
     * Number of random positions to sample when searching for dark areas.
     * More samples increase accuracy but cost more performance.
     */
    private static final int SEARCH_ATTEMPTS = 15;

    /**
     * Cooldown between dark zone search attempts (in ticks).
     * Prevents spamming expensive searches when no dark area is found.
     */
    private static final int SEARCH_COOLDOWN = 20;

    /** The light-averse mob controlled by this goal. */
    private final PathAwareEntity mob;

    /** Movement speed multiplier when fleeing from light (0.0 to 1.0+). */
    private final double fleeSpeed;

    /** Maximum light level tolerated before fleeing (0-15 scale). */
    private final int lightThreshold;

    /** Maximum distance to search for dark areas (in blocks). */
    private final double searchRange;

    /** Current target position to flee toward, or null if no target. */
    @Nullable
    private Vec3d targetPosition;

    /** Cooldown timer until next dark zone search (in ticks). */
    private int searchCooldown;

    /**
     * Constructs a flee-from-light goal with default threshold and range.
     *
     * <p>This constructor uses default values suitable for most light-averse mobs:
     * <ul>
     *   <li><b>Light Threshold</b>: 7 (hostile mob spawn threshold)</li>
     *   <li><b>Search Range</b>: 10 blocks</li>
     * </ul>
     *
     * @param mob the light-averse mob that will flee (must not be null)
     * @param fleeSpeed movement speed multiplier when fleeing (typically 1.0 to 1.5 for panic)
     * @throws NullPointerException if mob is null
     * @throws IllegalArgumentException if fleeSpeed is negative
     */
    public FleeFromLightGoal(PathAwareEntity mob, double fleeSpeed) {
        this(mob, fleeSpeed, DEFAULT_LIGHT_THRESHOLD, DEFAULT_SEARCH_RANGE);
    }

    /**
     * Constructs a flee-from-light goal with custom threshold and range.
     *
     * <p>This constructor allows full control over light-fleeing behavior. The light threshold
     * determines how bright an area must be before the mob flees, and the search range controls
     * how far the mob looks for dark areas.
     *
     * <p><b>Typical Values:</b>
     * <ul>
     *   <li><b>Speed</b>: 1.0 to 1.2 for moderate flee, 1.3 to 1.5 for panic</li>
     *   <li><b>Threshold</b>: 4-6 for very light-sensitive, 7-9 for moderately light-averse</li>
     *   <li><b>Range</b>: 8-12 blocks for normal mobs, 15-20 for fast/mobile entities</li>
     * </ul>
     *
     * @param mob the light-averse mob that will flee (must not be null)
     * @param fleeSpeed movement speed multiplier when fleeing (typically 1.0 to 1.5)
     * @param lightThreshold maximum light level tolerated (0-15, recommend 7)
     * @param searchRange maximum distance to search for dark areas (in blocks)
     * @throws NullPointerException if mob is null
     * @throws IllegalArgumentException if fleeSpeed or searchRange is negative, or lightThreshold is not 0-15
     */
    public FleeFromLightGoal(PathAwareEntity mob, double fleeSpeed, int lightThreshold, double searchRange) {
        this.mob = Objects.requireNonNull(mob, "Mob cannot be null");
        if (fleeSpeed < 0.0D) {
            throw new IllegalArgumentException("Flee speed cannot be negative: " + fleeSpeed);
        }
        if (lightThreshold < 0 || lightThreshold > 15) {
            throw new IllegalArgumentException("Light threshold must be 0-15: " + lightThreshold);
        }
        if (searchRange < 0.0D) {
            throw new IllegalArgumentException("Search range cannot be negative: " + searchRange);
        }
        this.fleeSpeed = fleeSpeed;
        this.lightThreshold = lightThreshold;
        this.searchRange = searchRange;
        this.targetPosition = null;
        this.searchCooldown = 0;

        // This goal controls movement (high priority, interrupts idle behaviors)
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
     *   <li>The mob is in a bright area (light level above threshold)</li>
     *   <li>The search cooldown has expired (not recently failed to find darkness)</li>
     *   <li>A darker area can be found within search range</li>
     * </ul>
     *
     * @return {@code true} if the goal can start, {@code false} otherwise
     */
    @Override
    public boolean canStart() {
        // Don't start if cooldown is active (recently failed search)
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }

        // Check if current position is too bright
        if (!isInBrightArea()) {
            return false;
        }

        // Try to find a darker area to flee to
        return findDarkArea();
    }

    /**
     * Checks if this goal should continue executing.
     *
     * <p>The goal continues as long as:
     * <ul>
     *   <li>There is a target position set</li>
     *   <li>The mob is still in a bright area (hasn't reached darkness yet)</li>
     *   <li>The mob is actively navigating toward the target</li>
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

        // Stop if mob reached darkness (success!)
        if (!isInBrightArea()) {
            return false;
        }

        // Stop if mob is no longer navigating (reached target or gave up)
        return mob.getNavigation().isFollowingPath();
    }

    /**
     * Starts executing this goal.
     *
     * <p>Begins pathfinding toward the darker area. If pathfinding fails,
     * the target is cleared and a cooldown is set before retrying.
     */
    @Override
    public void start() {
        if (targetPosition != null) {
            // Navigate to the dark area at configured flee speed
            boolean pathfindSuccess = mob.getNavigation().startMovingTo(
                targetPosition.x,
                targetPosition.y,
                targetPosition.z,
                fleeSpeed
            );

            // Clear target and set cooldown if pathfinding failed
            if (!pathfindSuccess) {
                targetPosition = null;
                searchCooldown = SEARCH_COOLDOWN;
            }
        }
    }

    /**
     * Stops executing this goal.
     *
     * <p>Clears the current target. Navigation is automatically stopped by
     * Minecraft's goal system when the goal becomes inactive.
     */
    @Override
    public void stop() {
        targetPosition = null;
        // Navigation is automatically cleared by Minecraft's goal system
    }

    // ============================================
    // Light Detection Logic
    // ============================================

    /**
     * Checks if the mob's current position is too bright (above threshold).
     *
     * <p>This method measures the total light level at the mob's position, combining:
     * <ul>
     *   <li><b>Block Light</b>: Light from torches, lava, glowstone, etc.</li>
     *   <li><b>Sky Light</b>: Sunlight (adjusted for time of day and weather)</li>
     * </ul>
     *
     * <p>The maximum of these two values is compared against the configured threshold.
     *
     * @return {@code true} if light level exceeds threshold, {@code false} if dark enough
     */
    private boolean isInBrightArea() {
        World world = mob.getWorld();
        BlockPos pos = mob.getBlockPos();

        // Get light levels (0-15 scale)
        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        int skyLight = world.getLightLevel(LightType.SKY, pos);

        // Total light is maximum of block light and sky light
        int totalLight = Math.max(blockLight, skyLight);

        // Too bright if above threshold
        return totalLight > lightThreshold;
    }

    /**
     * Gets the total light level at a specific position.
     *
     * <p>This is a helper method used during dark area searches. It returns the
     * maximum of block light and sky light at the given position.
     *
     * @param world the world to check
     * @param pos the position to check
     * @return total light level (0-15)
     */
    private int getLightLevel(World world, BlockPos pos) {
        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        int skyLight = world.getLightLevel(LightType.SKY, pos);
        return Math.max(blockLight, skyLight);
    }

    // ============================================
    // Dark Zone Search Logic
    // ============================================

    /**
     * Attempts to find a darker area within search range.
     *
     * <p>This method samples multiple random positions around the mob and selects
     * the darkest valid location found. The search process:
     * <ol>
     *   <li>Samples {@link #SEARCH_ATTEMPTS} random positions within {@link #searchRange}</li>
     *   <li>Validates each position (not solid block, within world bounds)</li>
     *   <li>Measures light level at each valid position</li>
     *   <li>Selects the darkest position found (must be darker than current)</li>
     * </ol>
     *
     * <p>If no darker area is found, a cooldown is set before retrying to prevent
     * spamming expensive searches.
     *
     * @return {@code true} if a darker area was found, {@code false} otherwise
     */
    private boolean findDarkArea() {
        World world = mob.getWorld();
        BlockPos currentPos = mob.getBlockPos();
        int currentLight = getLightLevel(world, currentPos);

        BlockPos bestPosition = null;
        int lowestLight = currentLight; // Must be darker than current position

        // Sample random positions to find the darkest one
        for (int attempt = 0; attempt < SEARCH_ATTEMPTS; attempt++) {
            // Random offset within search range
            double offsetX = (mob.getRandom().nextDouble() * 2.0D - 1.0D) * searchRange;
            double offsetY = (mob.getRandom().nextDouble() * 2.0D - 1.0D) * searchRange;
            double offsetZ = (mob.getRandom().nextDouble() * 2.0D - 1.0D) * searchRange;

            BlockPos testPos = currentPos.add((int) offsetX, (int) offsetY, (int) offsetZ);

            // Validate position
            if (!isValidPosition(world, testPos)) {
                continue;
            }

            // Check light level
            int lightLevel = getLightLevel(world, testPos);
            if (lightLevel < lowestLight) {
                lowestLight = lightLevel;
                bestPosition = testPos;
            }
        }

        // If we found a darker area, set it as target
        if (bestPosition != null) {
            this.targetPosition = Vec3d.ofCenter(bestPosition);
            return true;
        }

        // No darker area found - set cooldown to avoid spamming searches
        searchCooldown = SEARCH_COOLDOWN;
        return false;
    }

    /**
     * Validates whether a position is suitable for fleeing to.
     *
     * <p>A position is valid if:
     * <ul>
     *   <li>It is within world height bounds (not void, not above build limit)</li>
     *   <li>The block at that position is not solid (mob can occupy the space)</li>
     *   <li>There is a solid block below (mob won't fall into void)</li>
     * </ul>
     *
     * @param world the world to check
     * @param pos the position to validate
     * @return {@code true} if the position is valid, {@code false} otherwise
     */
    private boolean isValidPosition(World world, BlockPos pos) {
        // Check world height bounds
        if (!world.isInBuildLimit(pos)) {
            return false;
        }

        // Check if position is inside a solid block (mob can't occupy)
        if (world.getBlockState(pos).isSolidBlock(world, pos)) {
            return false;
        }

        // Check if there's a solid block below (mob won't fall into void)
        BlockPos below = pos.down();
        return world.getBlockState(below).isSolidBlock(world, below);
    }
}
