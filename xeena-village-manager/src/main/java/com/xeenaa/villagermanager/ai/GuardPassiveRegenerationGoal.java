package com.xeenaa.villagermanager.ai;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.data.GuardData;
import com.xeenaa.villagermanager.data.GuardDataManager;
import com.xeenaa.villagermanager.profession.ModProfessions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.EnumSet;

/**
 * AI goal that provides passive health regeneration for guards after being out of combat.
 * This goal complements the {@link GuardRetreatGoal} by providing long-term sustainability
 * for guards that avoid critical damage.
 *
 * <h2>Mechanics</h2>
 * <ul>
 *   <li><b>Combat Detection:</b> Tracks when guard enters/exits combat based on targeting and damage</li>
 *   <li><b>Out-of-Combat Timer:</b> Guard must be out of combat for 60 seconds (1200 ticks)</li>
 *   <li><b>Regeneration Interval:</b> Heals every 4 seconds (80 ticks) - slower than vanilla player</li>
 *   <li><b>Tier Scaling:</b> Higher tier guards regenerate faster</li>
 * </ul>
 *
 * <h2>Regeneration Rates</h2>
 * <ul>
 *   <li><b>Tier 0 (Recruit):</b> 0.5 HP every 4s = 0.125 HP/s</li>
 *   <li><b>Tier 1:</b> 0.75 HP every 4s = 0.1875 HP/s</li>
 *   <li><b>Tier 2 (Sergeant):</b> 1.0 HP every 4s = 0.25 HP/s</li>
 *   <li><b>Tier 3 (Lieutenant):</b> 1.25 HP every 4s = 0.3125 HP/s</li>
 *   <li><b>Tier 4 (Captain/Master):</b> 1.5 HP every 4s = 0.375 HP/s</li>
 * </ul>
 *
 * <h2>Combat State Tracking</h2>
 * <p>Combat state is tracked per-goal instance. A guard is considered "in combat" when:</p>
 * <ul>
 *   <li>Guard has a target ({@code getTarget() != null})</li>
 *   <li>Guard is taking damage (tracked via last damage tick)</li>
 * </ul>
 *
 * <h2>Integration with Other Goals</h2>
 * <ul>
 *   <li><b>GuardRetreatGoal:</b> Activates at low health (&lt;20%), provides faster emergency healing</li>
 *   <li><b>GuardPassiveRegenerationGoal:</b> Activates after extended peace, provides slow maintenance healing</li>
 *   <li>Both goals can coexist - retreat provides emergency healing, passive provides long-term sustainability</li>
 * </ul>
 *
 * @see GuardRetreatGoal
 * @since 1.0.0
 */
public class GuardPassiveRegenerationGoal extends Goal {
    private final VillagerEntity guard;

    // Combat tracking
    private int lastCombatTick = -1;
    private int lastDamageTick = -1;

    // Regeneration timing
    private int ticksSinceLastRegen = 0;

    // Constants
    private static final int OUT_OF_COMBAT_DELAY = 1200; // 60 seconds (60 * 20 ticks)
    private static final int REGENERATION_INTERVAL = 80; // 4 seconds (4 * 20 ticks)

    /**
     * Creates a new passive regeneration goal for the specified guard.
     *
     * @param guard The guard villager entity
     * @throws IllegalArgumentException if guard is null
     */
    public GuardPassiveRegenerationGoal(VillagerEntity guard) {
        if (guard == null) {
            throw new IllegalArgumentException("Guard cannot be null");
        }

        this.guard = guard;
        this.setControls(EnumSet.noneOf(Control.class)); // Does not control movement or looking

        XeenaaVillagerManager.LOGGER.debug("Created GuardPassiveRegenerationGoal for guard {}",
            guard.getUuid());
    }

    /**
     * Checks if this goal can start executing.
     * Regeneration starts when:
     * <ul>
     *   <li>Guard is a valid guard profession</li>
     *   <li>Guard is not at full health</li>
     *   <li>Guard has been out of combat for at least 60 seconds</li>
     * </ul>
     *
     * @return true if regeneration should activate
     */
    @Override
    public boolean canStart() {
        // Only guards can regenerate via this goal
        if (!isGuard()) {
            return false;
        }

        // Don't regenerate if already at full health
        if (guard.getHealth() >= guard.getMaxHealth()) {
            return false;
        }

        // Check if guard has been out of combat long enough
        if (!hasBeenOutOfCombatLongEnough()) {
            // Log countdown every 10 seconds (200 ticks) when guard is injured but not ready to regen
            int ticksSinceCombat = getTicksSinceLastCombat();
            if (guard.getHealth() < guard.getMaxHealth() && ticksSinceCombat > 0 && ticksSinceCombat % 200 == 0) {
                int secondsRemaining = (OUT_OF_COMBAT_DELAY - ticksSinceCombat) / 20;
                XeenaaVillagerManager.LOGGER.info(
                    "[PASSIVE REGEN] ⏱ Guard {} out of combat for {} seconds (need {} more seconds) | Health: {}/{}",
                    guard.getUuid(),
                    ticksSinceCombat / 20,
                    secondsRemaining,
                    String.format("%.1f", guard.getHealth()),
                    String.format("%.1f", guard.getMaxHealth())
                );
            }
            return false;
        }

        return true;
    }

    /**
     * Checks if this goal should continue executing.
     * Stops when:
     * <ul>
     *   <li>Guard reaches full health</li>
     *   <li>Guard enters combat</li>
     * </ul>
     *
     * @return true if regeneration should continue
     */
    @Override
    public boolean shouldContinue() {
        // Stop if guard reached full health
        if (guard.getHealth() >= guard.getMaxHealth()) {
            XeenaaVillagerManager.LOGGER.debug("Guard {} stopped regeneration - full health reached",
                guard.getUuid());
            return false;
        }

        // Stop if guard entered combat
        if (isInCombat()) {
            XeenaaVillagerManager.LOGGER.info("Guard {} stopped regeneration - entered combat",
                guard.getUuid());
            return false;
        }

        return true;
    }

    /**
     * Called when the goal starts executing.
     * Logs the start of regeneration and resets the regeneration timer.
     */
    @Override
    public void start() {
        ticksSinceLastRegen = 0;

        XeenaaVillagerManager.LOGGER.info(
            "[PASSIVE REGEN] ✓ Guard {} STARTED REGENERATING | Health: {}/{} | Out of combat for: {} seconds",
            guard.getUuid(),
            String.format("%.1f", guard.getHealth()),
            String.format("%.1f", guard.getMaxHealth()),
            getTicksSinceLastCombat() / 20
        );
    }

    /**
     * Called when the goal stops executing.
     * Logs completion and final health state.
     */
    @Override
    public void stop() {
        XeenaaVillagerManager.LOGGER.info(
            "[PASSIVE REGEN] ✗ Guard {} STOPPED REGENERATING | Final Health: {}/{}",
            guard.getUuid(),
            String.format("%.1f", guard.getHealth()),
            String.format("%.1f", guard.getMaxHealth())
        );
    }

    /**
     * Called every tick while the goal is active.
     * Handles regeneration timing and health restoration.
     */
    @Override
    public void tick() {
        // Update combat tracking every tick
        updateCombatState();

        // Increment regeneration timer
        ticksSinceLastRegen++;

        // Regenerate health every REGENERATION_INTERVAL ticks
        if (ticksSinceLastRegen >= REGENERATION_INTERVAL) {
            regenerateHealth();
            ticksSinceLastRegen = 0;
        }
    }

    /**
     * Updates the combat state tracking for the guard.
     * Checks for target and recent damage to determine combat status.
     */
    private void updateCombatState() {
        if (guard.getWorld().isClient()) {
            return; // Only track on server side
        }

        if (!(guard.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        int currentTick = serverWorld.getServer().getTicks();
        boolean wasInCombat = (lastCombatTick >= 0 && (currentTick - lastCombatTick) < 60);

        // Check if guard has a target or was recently damaged
        if (guard.getTarget() != null && guard.getTarget().isAlive()) {
            if (!wasInCombat) {
                XeenaaVillagerManager.LOGGER.info(
                    "[PASSIVE REGEN] Guard {} entered COMBAT (has target: {})",
                    guard.getUuid(),
                    guard.getTarget().getName().getString()
                );
            }
            lastCombatTick = currentTick;
        }

        // Track damage (hurtTime is > 0 for 10 ticks after taking damage)
        if (guard.hurtTime > 0) {
            if (!wasInCombat) {
                XeenaaVillagerManager.LOGGER.info(
                    "[PASSIVE REGEN] Guard {} entered COMBAT (taking damage) | Health: {}/{}",
                    guard.getUuid(),
                    String.format("%.1f", guard.getHealth()),
                    String.format("%.1f", guard.getMaxHealth())
                );
            }
            lastDamageTick = currentTick;
            lastCombatTick = currentTick;
        }
    }

    /**
     * Checks if the guard is currently in combat.
     * A guard is in combat if they have a target or were recently damaged (within 3 seconds).
     *
     * @return true if guard is in combat
     */
    private boolean isInCombat() {
        if (guard.getWorld().isClient()) {
            return false;
        }

        if (!(guard.getWorld() instanceof ServerWorld serverWorld)) {
            return false;
        }

        int currentTick = serverWorld.getServer().getTicks();

        // Check for active target
        if (guard.getTarget() != null && guard.getTarget().isAlive()) {
            return true;
        }

        // Check for recent damage (within 3 seconds = 60 ticks)
        if (lastDamageTick >= 0 && (currentTick - lastDamageTick) < 60) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the guard has been out of combat for the required duration.
     *
     * @return true if guard has been peaceful for at least OUT_OF_COMBAT_DELAY ticks
     */
    private boolean hasBeenOutOfCombatLongEnough() {
        if (isInCombat()) {
            return false;
        }

        int ticksSinceLastCombat = getTicksSinceLastCombat();
        return ticksSinceLastCombat >= OUT_OF_COMBAT_DELAY;
    }

    /**
     * Gets the number of ticks since the guard was last in combat.
     *
     * @return ticks since last combat, or Integer.MAX_VALUE if never in combat
     */
    private int getTicksSinceLastCombat() {
        if (guard.getWorld().isClient()) {
            return 0;
        }

        if (!(guard.getWorld() instanceof ServerWorld serverWorld)) {
            return 0;
        }

        if (lastCombatTick < 0) {
            // Never been in combat, assume safe for regeneration
            return Integer.MAX_VALUE;
        }

        int currentTick = serverWorld.getServer().getTicks();
        return currentTick - lastCombatTick;
    }

    /**
     * Regenerates health for the guard based on their current tier.
     * Higher tier guards regenerate more health per interval.
     */
    private void regenerateHealth() {
        if (guard.getWorld().isClient()) {
            return; // Only regenerate on server side
        }

        if (!(guard.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        // Get guard data for tier information
        GuardData guardData = GuardDataManager.get(serverWorld).getGuardData(guard.getUuid());
        if (guardData == null) {
            XeenaaVillagerManager.LOGGER.warn(
                "Guard {} has no GuardData - cannot determine tier for regeneration",
                guard.getUuid()
            );
            return;
        }

        // Calculate tier-scaled regeneration amount
        int tier = guardData.getRankData().getCurrentTier();
        float healAmount = calculateHealAmount(tier);

        // Store health before healing for logging
        float healthBefore = guard.getHealth();
        float maxHealth = guard.getMaxHealth();

        // Apply healing (capped at max health)
        float newHealth = Math.min(healthBefore + healAmount, maxHealth);
        guard.setHealth(newHealth);

        // Log regeneration tick at INFO level for visibility
        XeenaaVillagerManager.LOGGER.info(
            "[PASSIVE REGEN] ❤ Guard {} healed +{} HP (Tier {}) | Health: {}/{} -> {}/{}",
            guard.getUuid(),
            String.format("%.2f", healAmount),
            tier,
            String.format("%.1f", healthBefore),
            String.format("%.1f", maxHealth),
            String.format("%.1f", newHealth),
            String.format("%.1f", maxHealth)
        );

        // Log when guard reaches full health
        if (newHealth >= maxHealth && healthBefore < maxHealth) {
            XeenaaVillagerManager.LOGGER.info(
                "[PASSIVE REGEN] ★ Guard {} reached FULL HEALTH via passive regeneration (Tier {})",
                guard.getUuid(),
                tier
            );
        }
    }

    /**
     * Calculates the heal amount based on guard tier.
     * Formula: 0.5 + (tier × 0.25) HP per interval
     *
     * @param tier The guard's current tier (0-4)
     * @return heal amount in HP
     */
    private float calculateHealAmount(int tier) {
        // Tier 0: 0.5 HP per interval (0.125 HP/s)
        // Tier 1: 0.75 HP per interval (0.1875 HP/s)
        // Tier 2: 1.0 HP per interval (0.25 HP/s)
        // Tier 3: 1.25 HP per interval (0.3125 HP/s)
        // Tier 4: 1.5 HP per interval (0.375 HP/s)
        return 0.5f + (tier * 0.25f);
    }

    /**
     * Checks if this villager is a guard.
     *
     * @return true if the villager has the Guard profession
     */
    private boolean isGuard() {
        if (guard.getVillagerData() == null) {
            return false;
        }

        return guard.getVillagerData().getProfession() == ModProfessions.GUARD;
    }

    /**
     * Gets the guard's current tier for external access (debugging/monitoring).
     *
     * @return current tier, or 0 if guard data not available
     */
    public int getCurrentTier() {
        if (guard.getWorld().isClient() || !(guard.getWorld() instanceof ServerWorld serverWorld)) {
            return 0;
        }

        GuardData guardData = GuardDataManager.get(serverWorld).getGuardData(guard.getUuid());
        if (guardData == null) {
            return 0;
        }

        return guardData.getRankData().getCurrentTier();
    }

    /**
     * Gets the time remaining until regeneration starts (for debugging/monitoring).
     *
     * @return ticks remaining until regeneration can start, or 0 if already eligible
     */
    public int getTicksUntilRegeneration() {
        if (isInCombat()) {
            return OUT_OF_COMBAT_DELAY;
        }

        int ticksSinceLastCombat = getTicksSinceLastCombat();
        return Math.max(0, OUT_OF_COMBAT_DELAY - ticksSinceLastCombat);
    }
}
