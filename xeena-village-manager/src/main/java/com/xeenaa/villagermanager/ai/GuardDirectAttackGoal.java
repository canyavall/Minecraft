package com.xeenaa.villagermanager.ai;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.ai.performance.GuardAIScheduler;
import com.xeenaa.villagermanager.config.GuardBehaviorConfig;
import com.xeenaa.villagermanager.data.GuardData;
import com.xeenaa.villagermanager.data.GuardDataManager;
import com.xeenaa.villagermanager.util.CombatEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;

/**
 * Simple, direct attack goal that makes guards attack nearby hostile mobs.
 * Bypasses vanilla targeting systems entirely.
 *
 * <p>This goal respects guard behavior configuration including detection range.
 * Guards attack all visible hostile mobs within their detection range.</p>
 */
public class GuardDirectAttackGoal extends Goal {
    private final VillagerEntity guard;
    private LivingEntity target;
    private int attackCooldown = 0;
    private int targetSearchCooldown = 0;

    // Cached configuration values
    private GuardBehaviorConfig cachedConfig;
    private int configRefreshCounter = 0;
    private static final int CONFIG_REFRESH_INTERVAL = 100; // Refresh every 5 seconds

    public GuardDirectAttackGoal(VillagerEntity guard) {
        this.guard = guard;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        refreshConfiguration();
    }

    /**
     * Refreshes the cached configuration from guard data.
     * Only works on server side - goals only execute on server anyway.
     */
    private void refreshConfiguration() {
        // Goals only execute on server side, but Goal objects exist on both sides
        // Only access GuardDataManager on server to avoid crashes
        if (guard.getWorld() != null && !guard.getWorld().isClient() && guard.getWorld() instanceof ServerWorld world) {
            GuardDataManager manager = GuardDataManager.get(world);
            if (manager != null) {
                GuardData guardData = manager.getGuardData(guard.getUuid());
                if (guardData != null) {
                    GuardBehaviorConfig newConfig = guardData.getBehaviorConfig();
                    // Log if configuration changed
                    if (cachedConfig == null || !cachedConfig.equals(newConfig)) {
                        XeenaaVillagerManager.LOGGER.info("Guard {} configuration updated - Detection: {}, GuardMode: {}",
                            guard.getUuid(),
                            newConfig.detectionRange(),
                            newConfig.guardMode().getDisplayName());
                    }
                    cachedConfig = newConfig;
                    return;
                }
            }
        }
        // Fallback to default configuration
        if (cachedConfig == null || !cachedConfig.equals(GuardBehaviorConfig.DEFAULT)) {
            XeenaaVillagerManager.LOGGER.debug("Guard {} using default configuration", guard.getUuid());
        }
        cachedConfig = GuardBehaviorConfig.DEFAULT;
    }

    /**
     * Gets the current detection range from configuration.
     */
    private double getDetectionRange() {
        // Periodically refresh configuration to pick up changes
        if (++configRefreshCounter >= CONFIG_REFRESH_INTERVAL) {
            configRefreshCounter = 0;
            refreshConfiguration();
        }

        if (cachedConfig != null) {
            return cachedConfig.detectionRange();
        }
        return 20.0; // Default fallback
    }

    /**
     * Checks if the guard should engage a hostile.
     * Guards always attack visible hostile entities within their detection range.
     */
    private boolean shouldEngageHostile(HostileEntity hostile) {
        // Attack all visible hostile mobs within detection range
        return guard.canSee(hostile);
    }

    @Override
    public boolean canStart() {
        // Search for targets every 10 ticks (0.5 seconds)
        if (targetSearchCooldown > 0) {
            targetSearchCooldown--;
            return this.target != null && this.target.isAlive();
        }

        targetSearchCooldown = 10;

        // Get detection range from configuration
        double detectionRange = getDetectionRange();

        // Find nearest hostile entity within configured range
        Box searchBox = guard.getBoundingBox().expand(detectionRange);

        // Find hostiles based on configuration
        List<HostileEntity> hostiles = guard.getWorld().getEntitiesByClass(
            HostileEntity.class,
            searchBox,
            entity -> entity.isAlive() && shouldEngageHostile(entity)
        );

        if (!hostiles.isEmpty()) {
            // Target the closest hostile
            this.target = hostiles.stream()
                .min((e1, e2) -> Double.compare(
                    guard.squaredDistanceTo(e1),
                    guard.squaredDistanceTo(e2)
                ))
                .orElse(null);

            if (this.target != null) {
                return true;
            }
        }

        this.target = null;
        return false;
    }

    @Override
    public boolean shouldContinue() {
        double detectionRange = getDetectionRange();
        double maxDistanceSquared = detectionRange * detectionRange;

        return this.target != null &&
               this.target.isAlive() &&
               guard.squaredDistanceTo(this.target) < maxDistanceSquared;
    }

    @Override
    public void start() {
        guard.setTarget(this.target);

        // Count how many GuardDirectAttackGoal instances are active on this guard
        try {
            java.lang.reflect.Field goalSelectorField = net.minecraft.entity.mob.MobEntity.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);
            net.minecraft.entity.ai.goal.GoalSelector goalSelector = (net.minecraft.entity.ai.goal.GoalSelector) goalSelectorField.get(guard);

            long attackGoalCount = goalSelector.getGoals().stream()
                .filter(goal -> goal.getGoal() instanceof GuardDirectAttackGoal)
                .count();

            if (attackGoalCount > 1) {
                XeenaaVillagerManager.LOGGER.warn("[DUPLICATE GOALS] Guard {} has {} GuardDirectAttackGoal instances! Should only have 1!",
                    guard.getUuid(), attackGoalCount);
            } else {
                XeenaaVillagerManager.LOGGER.info("[COMBAT START] Guard {} starting combat with {} GuardDirectAttackGoal instances",
                    guard.getUuid(), attackGoalCount);
            }
        } catch (Exception e) {
            XeenaaVillagerManager.LOGGER.error("Failed to count GuardDirectAttackGoal instances", e);
        }

        // Notify scheduler that guard entered combat for increased update frequency
        if (guard.getWorld() instanceof ServerWorld serverWorld) {
            GuardAIScheduler scheduler = GuardAIScheduler.get(serverWorld);
            scheduler.markCombatActive(guard);
        }
    }

    @Override
    public void stop() {
        this.target = null;
        guard.setTarget(null);
        guard.getNavigation().stop();

        // Notify scheduler that guard left combat for reduced update frequency
        if (guard.getWorld() instanceof ServerWorld serverWorld) {
            GuardAIScheduler scheduler = GuardAIScheduler.get(serverWorld);
            scheduler.markCombatInactive(guard);
        }
    }

    // Debug counter for logging tick() calls
    private int tickCounter = 0;

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }

        // Update cooldowns
        if (attackCooldown > 0) {
            attackCooldown--;
            // Log cooldown decrement every 5 ticks to track if tick() is actually being called
            if (tickCounter % 5 == 0) {
                XeenaaVillagerManager.LOGGER.info("[TICK DEBUG] Cooldown: {} -> {} (decrementing)",
                    attackCooldown + 1, attackCooldown);
            }
        }
        tickCounter++;

        // Look at target
        guard.getLookControl().lookAt(target, 30.0f, 30.0f);

        double distanceToTarget = guard.squaredDistanceTo(target);
        double actualDistance = Math.sqrt(distanceToTarget);

        // Check if guard has a bow (ranged guard)
        net.minecraft.item.ItemStack weapon = guard.getEquippedStack(net.minecraft.entity.EquipmentSlot.MAINHAND);
        boolean isRanged = weapon.getItem() instanceof net.minecraft.item.BowItem;

        if (isRanged) {
            // Ranged combat - keep distance and shoot
            if (actualDistance < 8.0) {
                // Too close, back away
                net.minecraft.util.math.Vec3d guardPos = guard.getPos();
                net.minecraft.util.math.Vec3d targetPos = target.getPos();
                net.minecraft.util.math.Vec3d awayDirection = guardPos.subtract(targetPos).normalize();
                net.minecraft.util.math.Vec3d backupTarget = guardPos.add(awayDirection.multiply(2.0));
                guard.getNavigation().startMovingTo(backupTarget.x, backupTarget.y, backupTarget.z, 0.8);
            } else if (actualDistance > 12.0) {
                // Too far, move closer
                guard.getNavigation().startMovingTo(target, 0.8);
            } else {
                // Good distance, stop and shoot
                guard.getNavigation().stop();
            }

            // Draw bow when aiming (within shooting range)
            if (actualDistance >= 4.0 && actualDistance <= 16.0) {
                if (!guard.isUsingItem() && attackCooldown > 20) {
                    // Start drawing bow (show draw animation while in cooldown)
                    guard.setCurrentHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }

            // Shoot if in range and cooldown ready
            if (actualDistance >= 4.0 && actualDistance <= 16.0 && attackCooldown <= 0) {
                performRangedAttack();
                // Tier-based ranged attack cooldown (balanced with Skeleton)
                GuardData rangedGuardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
                int rangedTier = rangedGuardData != null ? rangedGuardData.getRankData().getCurrentTier() : 0;
                attackCooldown = getRangedAttackCooldown(rangedTier);
            }
        } else {
            // Melee combat - close distance and attack
            if (actualDistance > 2.0) {
                guard.getNavigation().startMovingTo(target, 1.0);
            } else {
                guard.getNavigation().stop();
            }

            // Attack if in range and cooldown is ready
            if (actualDistance <= 3.0 && attackCooldown <= 0) {
                performMeleeAttack();
                // Tier-based melee attack cooldown (balanced with Zombie)
                GuardData meleeGuardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
                int meleeTier = meleeGuardData != null ? meleeGuardData.getRankData().getCurrentTier() : 0;
                int newCooldown = getMeleeAttackCooldown(meleeTier);
                attackCooldown = newCooldown;
                XeenaaVillagerManager.LOGGER.info("[MELEE COOLDOWN SET] Tier {} - Set cooldown to {} ticks ({} seconds)",
                    meleeTier, newCooldown, newCooldown / 20.0);
            }
        }
    }

    private void performMeleeAttack() {
        if (this.target == null) {
            return;
        }

        // Get guard tier for logging
        GuardData logGuardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
        int guardTier = logGuardData != null ? logGuardData.getRankData().getCurrentTier() : 0;

        // Log melee attack with timing info
        XeenaaVillagerManager.LOGGER.info("[MELEE ATTACK] Guard Tier {} attacking {} | Guard Health: {}/{} | Target Health: {}/{} | Cooldown was: {} ticks",
            guardTier,
            target.getName().getString(),
            guard.getHealth(),
            guard.getMaxHealth(),
            target.getHealth(),
            target.getMaxHealth(),
            attackCooldown);

        // Swing hand animation - must be synced to clients via EntityAnimationS2CPacket
        guard.swingHand(net.minecraft.util.Hand.MAIN_HAND);

        // Also send animation packet to all tracking clients
        if (!guard.getWorld().isClient()) {
            var packet = new net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket(guard,
                net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket.SWING_MAIN_HAND);
            ((net.minecraft.server.world.ServerWorld) guard.getWorld()).getChunkManager()
                .sendToNearbyPlayers(guard, packet);
        }

        // Visual effect: Weapon swing particles (2 particles for performance)
        CombatEffects.spawnMeleeSwingParticles(guard.getWorld(), guard);

        // Audio effect: Weapon swing sound
        CombatEffects.playMeleeSwingSound(guard.getWorld(), guard.getPos(), guard.getSoundCategory());

        // Get guard tier for damage scaling
        GuardData damageGuardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
        int damageTier = damageGuardData != null ? damageGuardData.getRankData().getCurrentTier() : 0;

        // Tier-based base melee damage (WITHOUT weapon)
        // Goal: Make Tier 0-1 LOSE to zombies in 1v1
        // Tier 0: 0.15 base (with Iron Sword 5.0 = 5.15 total, DPS = 2.94)
        // Tier 1: 0.25 base (5.25 total, DPS = 3.28)
        // Tier 2: 0.5 base (5.5 total, DPS = 3.93)
        // Tier 3: 0.75 base (5.75 total, DPS = 4.79)
        // Tier 4: 1.2 base (6.2 total, DPS = 6.2)
        float baseDamage = 0.15f + (damageTier * 0.15f) + (damageTier >= 2 ? (damageTier - 1) * 0.1f : 0);

        // Add weapon damage
        net.minecraft.item.ItemStack weapon = guard.getEquippedStack(net.minecraft.entity.EquipmentSlot.MAINHAND);
        if (weapon.getItem() instanceof net.minecraft.item.SwordItem || weapon.getItem() instanceof net.minecraft.item.ToolItem) {
            double weaponDamage = weapon.getOrDefault(
                net.minecraft.component.DataComponentTypes.ATTRIBUTE_MODIFIERS,
                net.minecraft.component.type.AttributeModifiersComponent.DEFAULT
            ).modifiers().stream()
                .filter(entry -> entry.attribute().equals(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE))
                .mapToDouble(entry -> entry.modifier().value())
                .sum();
            baseDamage += (float) weaponDamage;
        }

        // Deal damage
        net.minecraft.entity.damage.DamageSource damageSource = guard.getDamageSources().mobAttack(guard);
        boolean damaged = target.damage(damageSource, baseDamage);

        // Log damage dealt
        XeenaaVillagerManager.LOGGER.info("[MELEE DAMAGE] Dealt {} damage to {} | Hit: {} | Target remaining health: {}/{}",
            baseDamage,
            target.getName().getString(),
            damaged,
            target.getHealth(),
            target.getMaxHealth());

        if (damaged) {
            // Apply knockback
            target.takeKnockback(0.4,
                guard.getX() - target.getX(),
                guard.getZ() - target.getZ());

            // Visual effect: Hit impact particles (4 particles)
            CombatEffects.spawnHitImpactParticles(guard.getWorld(), target, false);

            // Audio effect: Hit impact sound
            CombatEffects.playMeleeHitSound(guard.getWorld(), target.getPos(), guard.getSoundCategory(), false);
        }
    }

    private void performRangedAttack() {
        if (this.target == null) {
            return;
        }

        // Set guard to "using item" state for bow draw animation
        guard.setCurrentHand(net.minecraft.util.Hand.MAIN_HAND);

        // Visual effect: Arrow trail particles (3 particles for performance)
        CombatEffects.spawnArrowTrailParticles(guard.getWorld(), guard, target);

        // Calculate arrow spawn position (from guard's eye level, offset for hand position)
        // Villagers are 1.95 blocks tall, eye height is at ~1.62 blocks
        double arrowX = guard.getX();
        double arrowY = guard.getEyeY() - 0.1; // Slightly below eye level (bow hand height)
        double arrowZ = guard.getZ();

        // Offset arrow spawn to the side (right hand) based on guard's yaw
        float yaw = guard.getYaw() * ((float)Math.PI / 180f);
        double offsetX = -Math.sin(yaw) * 0.3; // 0.3 blocks to the right
        double offsetZ = Math.cos(yaw) * 0.3;

        arrowX += offsetX;
        arrowZ += offsetZ;

        // Create arrow at proper position
        net.minecraft.entity.projectile.ArrowEntity arrow =
            new net.minecraft.entity.projectile.ArrowEntity(guard.getWorld(), arrowX, arrowY, arrowZ,
                guard.getEquippedStack(net.minecraft.entity.EquipmentSlot.MAINHAND).copy(), null);

        arrow.setOwner(guard);

        // Calculate trajectory from arrow's spawn position to target
        double dx = target.getX() - arrowX;
        double dy = target.getBodyY(0.3333333333333333) - arrowY;
        double dz = target.getZ() - arrowZ;
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        // Get tier for velocity and damage calculations
        GuardData arrowGuardData = GuardDataManager.get(guard.getWorld()).getGuardData(guard.getUuid());
        int arrowTier = arrowGuardData != null ? arrowGuardData.getRankData().getCurrentTier() : 0;

        // Set velocity with tier-based scaling
        // Tier 0: 2.2 velocity, Tier 1: 2.4, Tier 2: 2.6, Tier 3: 2.8, Tier 4: 3.0
        float velocity = 2.2f + (arrowTier * 0.2f);
        arrow.setVelocity(dx, dy + horizontalDistance * 0.20000000298023224, dz, velocity, 14.0f - (guard.getWorld().getDifficulty().getId() * 4.0f));

        // Set damage with tier-based scaling for balance
        // Reduced base damage to compensate for superior AI and tactics
        // Tier 0 Normal: 2.5 (significantly lower than Skeleton 3.5)
        // Tier 1 Normal: 2.8
        // Tier 4 Normal: 3.7 (approaching Pillager 4.0)
        double baseDamage = 2.0 + (arrowTier * 0.3); // Tier scaling: 2.0 → 3.2
        double difficultyBonus = guard.getWorld().getDifficulty().getId() * 0.5;
        arrow.setDamage(baseDamage + difficultyBonus);

        // Audio effect: Bow shoot sound
        guard.getWorld().playSound(null, guard.getX(), guard.getY(), guard.getZ(),
            net.minecraft.sound.SoundEvents.ENTITY_ARROW_SHOOT,
            guard.getSoundCategory(), 1.0f, 1.0f / (guard.getRandom().nextFloat() * 0.4f + 0.8f));

        // Spawn arrow
        guard.getWorld().spawnEntity(arrow);

        // Clear "using item" state after a short delay (bow release animation)
        // This happens automatically when the item use finishes, but we clear it manually
        guard.clearActiveItem();
    }

    /**
     * Gets the ranged attack cooldown based on guard tier.
     * Balanced against Pillager (3.5s) and Skeleton (2.0s).
     *
     * @param tier The guard's current tier (0-4)
     * @return Attack cooldown in ticks
     */
    private int getRangedAttackCooldown(int tier) {
        // Tier 0: 70 ticks (3.5s) - matches Pillager speed
        // Tier 1: 65 ticks (3.25s) - still slower than Pillager
        // Tier 2: 55 ticks (2.75s) - faster than Pillager
        // Tier 3: 45 ticks (2.25s) - approaching Skeleton speed
        // Tier 4: 40 ticks (2.0s) - matches Skeleton speed
        return Math.max(40, 70 - (tier * 7));
    }

    /**
     * Gets the melee attack cooldown based on guard tier.
     * Balanced against Zombie (1.0s attack speed).
     *
     * @param tier The guard's current tier (0-4)
     * @return Attack cooldown in ticks
     */
    private int getMeleeAttackCooldown(int tier) {
        // COMPENSATING for accelerated tick rate (~32 ticks/second instead of 20)
        // Tier 0: 60 ticks (~1.875s actual) - significantly slower than Zombie (1.0s)
        // Tier 1: 54 ticks (~1.69s actual) - still much slower than Zombie
        // Tier 2: 48 ticks (~1.5s actual) - slower than Zombie
        // Tier 3: 40 ticks (~1.25s actual) - approaching Zombie speed
        // Tier 4: 32 ticks (~1.0s actual) - matches Zombie
        return Math.max(32, 60 - (tier * 6));
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}