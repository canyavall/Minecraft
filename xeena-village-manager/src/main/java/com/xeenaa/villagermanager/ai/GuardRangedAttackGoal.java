package com.xeenaa.villagermanager.ai;

import com.xeenaa.villagermanager.data.GuardData;
import com.xeenaa.villagermanager.data.GuardDataManager;
import com.xeenaa.villagermanager.data.rank.GuardRankData;
import com.xeenaa.villagermanager.ai.GuardSpecialAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.EnumSet;
import java.util.Optional;

/**
 * Ranged attack goal for guard villagers with specialized behavior patterns.
 * Implements glass cannon specialization with distance maintenance and kiting tactics.
 */
public class GuardRangedAttackGoal extends Goal {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardRangedAttackGoal.class);
    private final VillagerEntity guard;
    private int attackCooldown = 0;
    private int repositionCooldown = 0;
    private int bowDrawTime = 0; // Tracks bow draw progress
    private boolean isDrawingBow = false;
    private BlockPos lastHighGroundPos = null;
    private GuardSpecialAbilities specialAbilities;

    // Distance management constants
    private static final double PREFERRED_MIN_DISTANCE = 8.0;
    private static final double PREFERRED_MAX_DISTANCE = 15.0;
    private static final double DANGER_DISTANCE = 5.0;
    private static final double HIGH_GROUND_SEARCH_RADIUS = 10.0;

    // Cooldowns (in ticks)
    private static final int BASE_ATTACK_COOLDOWN = 45; // 2.25 seconds (balanced with Skeleton)
    private static final int REPOSITION_COOLDOWN = 60; // 3 seconds
    private static final int BOW_DRAW_TIME = 20; // 1 second for full draw (same as player)

    private final double moveSpeed;
    private final int attackInterval;
    private final float maxShootRange;
    private int attackTime = -1;
    private int seeTime;

    public GuardRangedAttackGoal(VillagerEntity guard, double speed, int attackInterval, float maxShootRange) {
        this.guard = guard;
        this.moveSpeed = speed;
        this.attackInterval = attackInterval;
        this.maxShootRange = maxShootRange;
        this.specialAbilities = GuardSpecialAbilities.get(guard);
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!isGuard()) {
            return false;
        }

        GuardData guardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
        if (guardData == null) {
            return false;
        }

        // Only ranged guards use this goal
        GuardRankData rankData = guardData.getRankData();
        if (!isRangedSpecialization(rankData)) {
            return false;
        }

        // Check if guard has bow and arrows
        ItemStack mainHand = guard.getEquippedStack(net.minecraft.entity.EquipmentSlot.MAINHAND);
        if (!(mainHand.getItem() instanceof BowItem)) {
            return false;
        }

        LivingEntity target = guard.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart() || !guard.getNavigation().isIdle();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        lastHighGroundPos = null;
        attackTime = -1;
        seeTime = 0;
        stopDrawingBow();
    }

    /**
     * Starts the bow drawing animation
     */
    private void startDrawingBow() {
        if (!isDrawingBow) {
            // Set current hand to trigger the animation
            guard.setCurrentHand(Hand.MAIN_HAND);
            isDrawingBow = true;
            bowDrawTime = 0;
            LOGGER.info("[RANGED ANIMATION] Guard {} started drawing bow",
                guard.getUuid().toString().substring(0, 8));
        }
        // Keep the bow draw animation active by ensuring the guard is using the item
        // This must be called every tick while drawing
        if (isDrawingBow && guard.getActiveItem().isEmpty()) {
            guard.setCurrentHand(Hand.MAIN_HAND);
        }
    }

    /**
     * Stops the bow drawing animation
     */
    private void stopDrawingBow() {
        if (isDrawingBow) {
            guard.clearActiveItem();
            isDrawingBow = false;
            bowDrawTime = 0;
            LOGGER.info("[RANGED ANIMATION] Guard {} stopped drawing bow",
                guard.getUuid().toString().substring(0, 8));
        }
    }

    /**
     * Checks if the bow is fully drawn
     */
    private boolean isBowFullyDrawn() {
        return bowDrawTime >= BOW_DRAW_TIME;
    }

    @Override
    public void tick() {
        LivingEntity target = guard.getTarget();
        if (target == null) {
            stopDrawingBow();
            return;
        }

        // Update cooldowns
        if (attackCooldown > 0) attackCooldown--;
        if (repositionCooldown > 0) repositionCooldown--;

        double distanceToTarget = guard.squaredDistanceTo(target);
        double actualDistance = Math.sqrt(distanceToTarget);
        boolean canSeeTarget = guard.getVisibilityCache().canSee(target);

        if (canSeeTarget) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
            stopDrawingBow(); // Cancel bow draw if target not visible
        }

        // Always look at target while drawing or attacking
        guard.getLookControl().lookAt(target, 30.0f, 30.0f);

        // Handle bow drawing animation and attack sequence
        if (canSeeTarget && canAttack(actualDistance)) {
            // Start drawing bow if not already drawing
            if (!isDrawingBow) {
                startDrawingBow();
            }

            // Increment draw time while aiming
            if (isDrawingBow) {
                bowDrawTime++;
            }

            // Fire when fully drawn
            if (isBowFullyDrawn()) {
                performRangedAttack(target, actualDistance);
                stopDrawingBow();
                this.attackTime = this.attackInterval;
            }
        } else {
            // Not in attack position - cancel bow draw and reposition
            stopDrawingBow();
            // Handle basic ranged positioning
            handleBasicRangedPositioning(target, actualDistance);
        }

        if (this.attackTime > 0) {
            --this.attackTime;
        }
    }

    /**
     * Handles basic ranged positioning
     */
    private void handleBasicRangedPositioning(LivingEntity target, double distance) {
        // Emergency retreat if too close
        if (distance <= DANGER_DISTANCE) {
            performEmergencyRetreat(target);
            return;
        }

        // Only reposition if cooldown is finished
        if (repositionCooldown > 0) {
            return;
        }
        // Seek high ground if available and not in danger
        if (repositionCooldown <= 0 && distance > DANGER_DISTANCE) {
            BlockPos highGround = findHighGround(target);
            if (highGround != null && !highGround.equals(lastHighGroundPos)) {
                guard.getNavigation().startMovingTo(highGround.getX(), highGround.getY(), highGround.getZ(), moveSpeed);
                lastHighGroundPos = highGround;
                repositionCooldown = REPOSITION_COOLDOWN;
                return;
            }
        }

        // Maintain optimal distance
        if (distance < PREFERRED_MIN_DISTANCE) {
            // Too close - kite away
            performKiting(target);
        } else if (distance > PREFERRED_MAX_DISTANCE) {
            // Too far - move closer but maintain range
            Vec3d targetPos = target.getPos();
            Vec3d guardPos = guard.getPos();
            Vec3d direction = targetPos.subtract(guardPos).normalize();
            Vec3d moveTarget = targetPos.subtract(direction.multiply(PREFERRED_MIN_DISTANCE + 2.0));

            guard.getNavigation().startMovingTo(moveTarget.x, moveTarget.y, moveTarget.z, moveSpeed * 0.8);
        }
    }

    /**
     * Performs emergency retreat when enemy gets too close
     */
    private void performEmergencyRetreat(LivingEntity target) {
        Vec3d guardPos = guard.getPos();
        Vec3d targetPos = target.getPos();
        Vec3d retreatDirection = guardPos.subtract(targetPos).normalize();
        Vec3d retreatTarget = guardPos.add(retreatDirection.multiply(8.0));

        guard.getNavigation().startMovingTo(retreatTarget.x, retreatTarget.y, retreatTarget.z, moveSpeed * 1.2);
        repositionCooldown = REPOSITION_COOLDOWN / 2; // Shorter cooldown for emergency moves
    }

    /**
     * Performs kiting behavior - moving away while maintaining line of sight
     */
    private void performKiting(LivingEntity target) {
        if (repositionCooldown > 0) return;

        Vec3d guardPos = guard.getPos();
        Vec3d targetPos = target.getPos();
        Vec3d kiteDirection = guardPos.subtract(targetPos).normalize();

        // Add some perpendicular movement for unpredictability
        Vec3d perpendicular = new Vec3d(-kiteDirection.z, 0, kiteDirection.x);
        if (guard.getRandom().nextBoolean()) {
            perpendicular = perpendicular.multiply(-1);
        }

        Vec3d kiteTarget = guardPos.add(kiteDirection.multiply(6.0)).add(perpendicular.multiply(2.0));
        guard.getNavigation().startMovingTo(kiteTarget.x, kiteTarget.y, kiteTarget.z, moveSpeed);

        repositionCooldown = REPOSITION_COOLDOWN;
    }

    /**
     * Finds high ground positions for tactical advantage
     */
    private BlockPos findHighGround(LivingEntity target) {
        BlockPos guardPos = guard.getBlockPos();
        BlockPos targetPos = target.getBlockPos();
        int guardY = guardPos.getY();

        // Search for positions 2-4 blocks higher within radius
        for (int y = guardY + 2; y <= guardY + 4; y++) {
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos checkPos = guardPos.add(x, y - guardY, z);

                    // Check if position is valid and provides line of sight to target
                    if (isValidHighGroundPosition(checkPos, targetPos)) {
                        double distance = Math.sqrt(checkPos.getSquaredDistance(targetPos));
                        if (distance >= PREFERRED_MIN_DISTANCE && distance <= PREFERRED_MAX_DISTANCE) {
                            return checkPos;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Checks if a position is valid for high ground positioning
     */
    private boolean isValidHighGroundPosition(BlockPos pos, BlockPos targetPos) {
        // Check if position is safe to stand on
        if (!guard.getWorld().getBlockState(pos).isAir() ||
            !guard.getWorld().getBlockState(pos.up()).isAir()) {
            return false;
        }

        if (guard.getWorld().getBlockState(pos.down()).isAir()) {
            return false;
        }

        // Check line of sight to target
        Vec3d start = Vec3d.ofCenter(pos);
        Vec3d end = Vec3d.ofCenter(targetPos);
        return guard.getWorld().raycast(new net.minecraft.world.RaycastContext(
            start, end,
            net.minecraft.world.RaycastContext.ShapeType.COLLIDER,
            net.minecraft.world.RaycastContext.FluidHandling.NONE,
            guard
        )).getType() == net.minecraft.util.hit.HitResult.Type.MISS;
    }

    /**
     * Checks if the guard can attack based on distance and cooldowns
     */
    private boolean canAttack(double distance) {
        return attackCooldown <= 0 &&
               distance >= 4.0 &&
               distance <= PREFERRED_MAX_DISTANCE &&
               guard.canSee(guard.getTarget());
    }

    /**
     * Performs ranged attack with rank-based special abilities
     */
    private void performRangedAttack(LivingEntity target, double distance) {
        GuardData guardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
        if (guardData == null) return;

        GuardRankData rankData = guardData.getRankData();
        int tier = rankData.getCurrentTier();

        // Try to use special abilities based on rank
        boolean usedSpecialAttack = false;

        if (tier >= 4) {
            float random = guard.getRandom().nextFloat();
            if (random < 0.2f) {
                usedSpecialAttack = specialAbilities.useAbility(GuardSpecialAbilities.AbilityType.DOUBLE_SHOT, target);
            } else if (random < 0.35f) {
                usedSpecialAttack = specialAbilities.useAbility(GuardSpecialAbilities.AbilityType.MULTISHOT, target);
            } else if (random < 0.5f) {
                usedSpecialAttack = specialAbilities.useAbility(GuardSpecialAbilities.AbilityType.SLOWING_ARROW, target);
            }
        }

        if (!usedSpecialAttack && tier >= 3 && guard.getRandom().nextFloat() < 0.4f) {
            usedSpecialAttack = specialAbilities.useAbility(GuardSpecialAbilities.AbilityType.PRECISION_SHOT, target);
        }

        if (!usedSpecialAttack) {
            performBasicRangedAttack(target, tier);
        }

        attackCooldown = getAttackCooldown(tier);
    }

    /**
     * Performs basic ranged attack with accuracy based on tier
     */
    private void performBasicRangedAttack(LivingEntity target, int tier) {
        GuardData guardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
        String rankName = guardData != null ? guardData.getRankData().getCurrentRank().getDisplayName() : "UNKNOWN";

        // Create arrow entity
        ArrowEntity arrow = new ArrowEntity(guard.getWorld(), guard, new ItemStack(Items.ARROW), null);

        // Apply slowness effect ONLY at Tier 3+ (balanced progression)
        if (tier >= 3) {
            arrow.addEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                net.minecraft.entity.effect.StatusEffects.SLOWNESS,
                40, // 2 seconds (reduced from 3)
                0,  // Amplifier 0 = Slowness I (35% speed reduction)
                false,
                false
            ));
        }

        // Improved accuracy for higher tiers
        float accuracy = Math.max(1.0f, 14 - tier * 2);
        float velocity = 1.6f;

        // Bow draw animation is handled in tick() method, just trigger release
        guard.swingHand(Hand.MAIN_HAND);

        // Calculate aim with accuracy bonus
        double deltaX = target.getX() - guard.getX();
        double deltaY = target.getBodyY(0.3333333333333333) - arrow.getY();
        double deltaZ = target.getZ() - guard.getZ();
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        arrow.setVelocity(deltaX, deltaY + horizontalDistance * 0.20000000298023224, deltaZ, velocity, accuracy);

        guard.getWorld().spawnEntity(arrow);
        guard.getWorld().playSound(null, guard.getX(), guard.getY(), guard.getZ(),
            SoundEvents.ENTITY_ARROW_SHOOT, guard.getSoundCategory(), 1.0f, 1.0f);

        // LOG RANGED COMBAT EVENT
        LOGGER.info("[RANGED COMBAT] Guard {} (Rank: {}, Tier: {}) shot slowness arrow at {} | Distance: {:.2f} blocks | Accuracy: {:.2f} | Velocity: {:.2f}",
            guard.getUuid().toString().substring(0, 8),
            rankName,
            tier,
            target.getName().getString(),
            horizontalDistance,
            accuracy,
            velocity);
    }

    /**
     * Gets attack cooldown based on tier
     * Balanced progression: Tier 0 slightly slower than Skeleton, Tier 4 matches Skeleton close-range
     */
    private int getAttackCooldown(int tier) {
        // Tier 0: 45 ticks (2.25s) - slightly slower than Skeleton average (2.0s)
        // Tier 1: 42 ticks (2.1s)
        // Tier 2: 38 ticks (1.9s) - approaching Skeleton speed
        // Tier 3: 34 ticks (1.7s)
        // Tier 4: 30 ticks (1.5s) - matches Skeleton close-range speed
        return Math.max(30, BASE_ATTACK_COOLDOWN - (tier * 3)); // Faster attacks at higher tiers
    }

    /**
     * Checks if the guard uses ranged specialization
     */
    private boolean isRangedSpecialization(GuardRankData rankData) {
        if (rankData.getChosenPath() != null) {
            return rankData.getChosenPath().getId().equals("ranged");
        }

        // Check current rank for specialization
        String rankId = rankData.getCurrentRank().getId();
        return rankId.startsWith("marksman_");
    }

    private boolean isGuard() {
        return guard.getVillagerData().getProfession().id().equals("guard");
    }
}