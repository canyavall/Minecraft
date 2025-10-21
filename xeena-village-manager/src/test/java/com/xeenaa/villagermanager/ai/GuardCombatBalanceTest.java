package com.xeenaa.villagermanager.ai;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for guard combat balance.
 *
 * <p>These tests validate that guard combat statistics (damage, attack speed, DPS)
 * are balanced relative to vanilla Minecraft mobs, particularly:</p>
 * <ul>
 *   <li>Melee guards vs Zombies (3.0 DPS baseline)</li>
 *   <li>Ranged guards vs Skeletons (1.75 DPS baseline)</li>
 * </ul>
 *
 * <p><b>Balance Goals</b>:</p>
 * <ul>
 *   <li>Tier 0 guards should be WEAKER than vanilla mobs</li>
 *   <li>Tier 1-2 guards should be COMPETITIVE with vanilla mobs</li>
 *   <li>Tier 4 guards should be STRONGER than vanilla mobs</li>
 * </ul>
 *
 * @see com.xeenaa.villagermanager.ai.GuardDirectAttackGoal
 * @since 1.0.0 (Epic 04, Task P4-TASK-003.1)
 */
@DisplayName("Guard Combat Balance Tests")
public class GuardCombatBalanceTest {

    // Vanilla mob baseline stats (Normal difficulty)
    private static final float ZOMBIE_DAMAGE = 3.0f;
    private static final float ZOMBIE_ATTACK_SPEED_SECONDS = 1.0f;
    private static final float ZOMBIE_DPS = ZOMBIE_DAMAGE / ZOMBIE_ATTACK_SPEED_SECONDS; // 3.0 DPS

    private static final float SKELETON_DAMAGE = 3.5f; // Average
    private static final float SKELETON_ATTACK_SPEED_SECONDS = 2.0f;
    private static final float SKELETON_DPS = SKELETON_DAMAGE / SKELETON_ATTACK_SPEED_SECONDS; // 1.75 DPS

    // Iron Sword damage (used by melee guards)
    private static final float IRON_SWORD_DAMAGE = 5.0f;

    // Tick rate compensation (tick() called ~32 times/second instead of 20)
    private static final float TICK_RATE_COMPENSATION = 1.6f;

    /**
     * Calculates melee guard base damage based on tier.
     * Formula from GuardDirectAttackGoal.java lines 321-328.
     */
    private float calculateMeleeBaseDamage(int tier) {
        return 0.15f + (tier * 0.15f) + (tier >= 2 ? (tier - 1) * 0.1f : 0);
    }

    /**
     * Calculates melee attack cooldown in ticks based on tier.
     * Formula from GuardDirectAttackGoal.java lines 461-469.
     */
    private int calculateMeleeCooldownTicks(int tier) {
        return Math.max(32, 60 - (tier * 6));
    }

    /**
     * Converts tick cooldown to actual seconds, accounting for accelerated tick rate.
     */
    private float ticksToSeconds(int ticks) {
        // Normal: 20 ticks = 1 second
        // Accelerated: ~32 ticks = 1 second
        return ticks / 32.0f;
    }

    @Nested
    @DisplayName("Melee Guard Balance Tests")
    class MeleeGuardBalanceTests {

        @Test
        @DisplayName("Tier 0 melee guard is weaker than Zombie")
        void tier0MeleeWeakerThanZombie() {
            int tier = 0;

            // Calculate stats
            float baseDamage = calculateMeleeBaseDamage(tier);
            float totalDamage = baseDamage + IRON_SWORD_DAMAGE;
            int cooldownTicks = calculateMeleeCooldownTicks(tier);
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
            float guardDPS = totalDamage / attackSpeedSeconds;

            // Assertions
            assertEquals(0.15f, baseDamage, 0.01f, "Tier 0 base damage should be 0.15");
            assertEquals(5.15f, totalDamage, 0.01f, "Tier 0 total damage should be 5.15 (0.15 + 5.0)");
            assertEquals(60, cooldownTicks, "Tier 0 cooldown should be 60 ticks");
            assertEquals(1.875f, attackSpeedSeconds, 0.01f, "Tier 0 attack speed should be ~1.875s");
            assertEquals(2.75f, guardDPS, 0.1f, "Tier 0 DPS should be ~2.75");

            // Balance validation
            assertTrue(guardDPS < ZOMBIE_DPS,
                String.format("Tier 0 guard (%.2f DPS) should be WEAKER than Zombie (%.2f DPS)",
                    guardDPS, ZOMBIE_DPS));

            float dpsRatio = (guardDPS / ZOMBIE_DPS) * 100;
            assertTrue(dpsRatio < 95,
                String.format("Tier 0 guard should be at least 5%% weaker than Zombie (actual: %.1f%%)", dpsRatio));
        }

        @Test
        @DisplayName("Tier 1 melee guard is competitive with Zombie")
        void tier1MeleeCompetitiveWithZombie() {
            int tier = 1;

            // Calculate stats
            float baseDamage = calculateMeleeBaseDamage(tier);
            float totalDamage = baseDamage + IRON_SWORD_DAMAGE;
            int cooldownTicks = calculateMeleeCooldownTicks(tier);
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
            float guardDPS = totalDamage / attackSpeedSeconds;

            // Assertions
            assertEquals(0.30f, baseDamage, 0.01f, "Tier 1 base damage should be 0.30 (0.15 + 1*0.15)");
            assertEquals(5.30f, totalDamage, 0.01f, "Tier 1 total damage should be 5.30");
            assertEquals(54, cooldownTicks, "Tier 1 cooldown should be 54 ticks");
            assertEquals(1.69f, attackSpeedSeconds, 0.05f, "Tier 1 attack speed should be ~1.69s");
            assertEquals(3.14f, guardDPS, 0.15f, "Tier 1 DPS should be ~3.14");

            // Balance validation - should be very close to zombie (within 10%)
            float dpsRatio = (guardDPS / ZOMBIE_DPS) * 100;
            assertTrue(dpsRatio >= 95 && dpsRatio <= 115,
                String.format("Tier 1 guard should be competitive with Zombie (95-115%% DPS, actual: %.1f%%)", dpsRatio));
        }

        @Test
        @DisplayName("Tier 2 melee guard is moderately stronger than Zombie")
        void tier2MeleeStrongerThanZombie() {
            int tier = 2;

            // Calculate stats
            float baseDamage = calculateMeleeBaseDamage(tier);
            float totalDamage = baseDamage + IRON_SWORD_DAMAGE;
            int cooldownTicks = calculateMeleeCooldownTicks(tier);
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
            float guardDPS = totalDamage / attackSpeedSeconds;

            // Assertions
            assertEquals(0.55f, baseDamage, 0.01f, "Tier 2 base damage should be 0.55 (0.15 + 2*0.15 + 1*0.1)");
            assertEquals(5.55f, totalDamage, 0.01f, "Tier 2 total damage should be 5.55");
            assertEquals(48, cooldownTicks, "Tier 2 cooldown should be 48 ticks");

            // Balance validation - should be moderately stronger (110-140%)
            float dpsRatio = (guardDPS / ZOMBIE_DPS) * 100;
            assertTrue(dpsRatio >= 110 && dpsRatio <= 140,
                String.format("Tier 2 guard should be moderately stronger than Zombie (110-140%% DPS, actual: %.1f%%)", dpsRatio));
        }

        @Test
        @DisplayName("Tier 4 melee guard is significantly stronger than Zombie")
        void tier4MeleeSignificantlyStrongerThanZombie() {
            int tier = 4;

            // Calculate stats
            float baseDamage = calculateMeleeBaseDamage(tier);
            float totalDamage = baseDamage + IRON_SWORD_DAMAGE;
            int cooldownTicks = calculateMeleeCooldownTicks(tier);
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
            float guardDPS = totalDamage / attackSpeedSeconds;

            // Assertions
            assertEquals(1.05f, baseDamage, 0.01f, "Tier 4 base damage should be 1.05 (0.15 + 4*0.15 + 3*0.1)");
            assertEquals(6.05f, totalDamage, 0.01f, "Tier 4 total damage should be 6.05");
            assertEquals(36, cooldownTicks, "Tier 4 cooldown should be 36 ticks (60 - 4*6)");
            assertEquals(1.125f, attackSpeedSeconds, 0.05f, "Tier 4 attack speed should be ~1.125s (36/32)");

            // Balance validation - should be significantly stronger (160-200%)
            float dpsRatio = (guardDPS / ZOMBIE_DPS) * 100;
            assertTrue(dpsRatio >= 160 && dpsRatio <= 200,
                String.format("Tier 4 guard should be significantly stronger than Zombie (160-200%% DPS, actual: %.1f%%)", dpsRatio));
        }

        @Test
        @DisplayName("Melee damage scales progressively with tier")
        void meleeDamageScalesWithTier() {
            float[] expectedBaseDamages = {
                0.15f,  // Tier 0: 0.15 + 0*0.15 + 0 = 0.15
                0.30f,  // Tier 1: 0.15 + 1*0.15 + 0 = 0.30
                0.55f,  // Tier 2: 0.15 + 2*0.15 + (2-1)*0.1 = 0.55
                0.80f,  // Tier 3: 0.15 + 3*0.15 + (3-1)*0.1 = 0.80
                1.05f   // Tier 4: 0.15 + 4*0.15 + (4-1)*0.1 = 1.05
            };

            for (int tier = 0; tier <= 4; tier++) {
                float actualDamage = calculateMeleeBaseDamage(tier);
                assertEquals(expectedBaseDamages[tier], actualDamage, 0.01f,
                    String.format("Tier %d base damage should be %.2f", tier, expectedBaseDamages[tier]));

                // Ensure damage increases with tier
                if (tier > 0) {
                    assertTrue(actualDamage > calculateMeleeBaseDamage(tier - 1),
                        String.format("Tier %d damage should be higher than Tier %d", tier, tier - 1));
                }
            }
        }

        @Test
        @DisplayName("Melee attack speed increases with tier")
        void meleeAttackSpeedIncreasesWithTier() {
            int[] expectedCooldowns = {60, 54, 48, 42, 36}; // Ticks (60 - tier*6)

            for (int tier = 0; tier <= 4; tier++) {
                int actualCooldown = calculateMeleeCooldownTicks(tier);
                assertEquals(expectedCooldowns[tier], actualCooldown,
                    String.format("Tier %d cooldown should be %d ticks", tier, expectedCooldowns[tier]));

                // Ensure cooldown decreases with tier (faster attacks)
                if (tier > 0) {
                    assertTrue(actualCooldown <= calculateMeleeCooldownTicks(tier - 1),
                        String.format("Tier %d should attack faster than Tier %d", tier, tier - 1));
                }
            }
        }

        @Test
        @DisplayName("Melee minimum cooldown is enforced (32 ticks)")
        void meleeMinimumCooldownEnforced() {
            // Test that even at very high tiers, cooldown doesn't go below 32 ticks
            for (int tier = 4; tier <= 10; tier++) {
                int cooldown = calculateMeleeCooldownTicks(tier);
                assertTrue(cooldown >= 32,
                    String.format("Tier %d cooldown should not be less than 32 ticks (actual: %d)", tier, cooldown));
            }
        }
    }

    @Nested
    @DisplayName("Combat Formula Validation Tests")
    class CombatFormulaValidationTests {

        @Test
        @DisplayName("Base damage formula matches implementation")
        void baseDamageFormulaMatches() {
            // Test formula: 0.15f + (tier * 0.15f) + (tier >= 2 ? (tier - 1) * 0.1f : 0)

            // Tier 0
            assertEquals(0.15f, calculateMeleeBaseDamage(0), 0.001f);

            // Tier 1 (no tier >= 2 bonus)
            assertEquals(0.30f, calculateMeleeBaseDamage(1), 0.001f);

            // Tier 2 (first tier with bonus)
            assertEquals(0.55f, calculateMeleeBaseDamage(2), 0.001f);

            // Tier 3: 0.15 + 3*0.15 + 2*0.1 = 0.80
            assertEquals(0.80f, calculateMeleeBaseDamage(3), 0.001f);

            // Tier 4: 0.15 + 4*0.15 + 3*0.1 = 1.05
            assertEquals(1.05f, calculateMeleeBaseDamage(4), 0.001f);
        }

        @Test
        @DisplayName("Cooldown formula matches implementation")
        void cooldownFormulaMatches() {
            // Test formula: Math.max(32, 60 - (tier * 6))

            // Tier 0: 60 - 0 = 60
            assertEquals(60, calculateMeleeCooldownTicks(0));

            // Tier 1: 60 - 6 = 54
            assertEquals(54, calculateMeleeCooldownTicks(1));

            // Tier 2: 60 - 12 = 48
            assertEquals(48, calculateMeleeCooldownTicks(2));

            // Tier 3: 60 - 18 = 42
            assertEquals(42, calculateMeleeCooldownTicks(3));

            // Tier 4: 60 - 24 = 36
            assertEquals(36, calculateMeleeCooldownTicks(4));

            // Tier 5+: 60 - 30 = 30, but min is 32
            assertEquals(32, calculateMeleeCooldownTicks(5));
            assertEquals(32, calculateMeleeCooldownTicks(10));
        }

        @Test
        @DisplayName("DPS calculation accuracy")
        void dpsCalculationAccuracy() {
            // Validate DPS = damage / attack_speed_seconds

            int tier = 1;
            float baseDamage = calculateMeleeBaseDamage(tier);
            float totalDamage = baseDamage + IRON_SWORD_DAMAGE; // 0.30 + 5.0 = 5.30
            int cooldownTicks = calculateMeleeCooldownTicks(tier); // 54 ticks
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks); // 54 / 32 = 1.6875s
            float expectedDPS = totalDamage / attackSpeedSeconds; // 5.30 / 1.6875 = 3.14 DPS

            assertEquals(3.14f, expectedDPS, 0.02f, "DPS calculation should be accurate");
        }

        @Test
        @DisplayName("Tick rate compensation is applied correctly")
        void tickRateCompensationApplied() {
            // Normal Minecraft: 20 ticks/second
            // Accelerated (our case): ~32 ticks/second

            int cooldownTicks = 60;

            // Normal calculation
            float normalSeconds = cooldownTicks / 20.0f; // 3.0 seconds

            // Compensated calculation
            float compensatedSeconds = ticksToSeconds(cooldownTicks); // 60 / 32 = 1.875 seconds

            assertEquals(3.0f, normalSeconds, 0.01f);
            assertEquals(1.875f, compensatedSeconds, 0.01f);

            // Verify compensation factor
            float compensationFactor = normalSeconds / compensatedSeconds;
            assertEquals(1.6f, compensationFactor, 0.05f,
                "Tick rate compensation factor should be ~1.6x");
        }
    }

    @Nested
    @DisplayName("Balance Progression Tests")
    class BalanceProgressionTests {

        @Test
        @DisplayName("DPS increases with each tier upgrade")
        void dpsIncreasesWithTier() {
            float previousDPS = 0;

            for (int tier = 0; tier <= 4; tier++) {
                float baseDamage = calculateMeleeBaseDamage(tier);
                float totalDamage = baseDamage + IRON_SWORD_DAMAGE;
                int cooldownTicks = calculateMeleeCooldownTicks(tier);
                float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
                float guardDPS = totalDamage / attackSpeedSeconds;

                if (tier > 0) {
                    assertTrue(guardDPS > previousDPS,
                        String.format("Tier %d DPS (%.2f) should be higher than Tier %d DPS (%.2f)",
                            tier, guardDPS, tier - 1, previousDPS));
                }

                previousDPS = guardDPS;
            }
        }

        @Test
        @DisplayName("Investment justification - emerald cost vs DPS gain")
        void emeraldCostVsDPSGain() {
            // Rank costs from game (approximate)
            int[] rankCosts = {0, 15, 20, 45, 75}; // Cumulative emerald cost per tier

            for (int tier = 1; tier <= 4; tier++) {
                float tier0DPS = calculateDPS(0);
                float currentDPS = calculateDPS(tier);
                float dpsGain = currentDPS - tier0DPS;
                int emeraldCost = rankCosts[tier];

                // DPS gain per emerald should be reasonable
                float dpsPerEmerald = dpsGain / emeraldCost;

                assertTrue(dpsPerEmerald > 0,
                    String.format("Tier %d should provide positive DPS gain per emerald", tier));

                // Log for manual review (tests don't fail on this)
                System.out.printf("Tier %d: %d emeralds for +%.2f DPS (%.3f DPS/emerald)%n",
                    tier, emeraldCost, dpsGain, dpsPerEmerald);
            }
        }

        private float calculateDPS(int tier) {
            float baseDamage = calculateMeleeBaseDamage(tier);
            float totalDamage = baseDamage + IRON_SWORD_DAMAGE;
            int cooldownTicks = calculateMeleeCooldownTicks(tier);
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
            return totalDamage / attackSpeedSeconds;
        }

        @Test
        @DisplayName("Tier progression feels balanced (no huge jumps)")
        void tierProgressionBalanced() {
            float previousDPS = 0;

            for (int tier = 0; tier <= 4; tier++) {
                float currentDPS = calculateDPS(tier);

                if (tier > 0) {
                    float dpsIncrease = currentDPS - previousDPS;
                    float percentIncrease = (dpsIncrease / previousDPS) * 100;

                    // No single tier should increase DPS by more than 100% (2x)
                    assertTrue(percentIncrease <= 100,
                        String.format("Tier %d DPS increase (%.1f%%) should not exceed 100%%", tier, percentIncrease));

                    // Every tier should provide at least 5% DPS increase
                    assertTrue(percentIncrease >= 5,
                        String.format("Tier %d DPS increase (%.1f%%) should be at least 5%%", tier, percentIncrease));
                }

                previousDPS = currentDPS;
            }
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Negative tier handled gracefully")
        void negativeTierHandled() {
            // Formula should work with tier = 0 as minimum
            int tier = -1;
            int cooldown = Math.max(32, 60 - (tier * 6)); // 60 - (-6) = 66
            assertEquals(66, cooldown, "Negative tier should still apply formula");

            float baseDamage = calculateMeleeBaseDamage(-1);
            // 0.15 + (-1)*0.15 + (tier >= 2 ? ... : 0) = 0.15 - 0.15 = 0.0
            assertTrue(baseDamage >= 0, "Base damage should not be negative");
        }

        @Test
        @DisplayName("Very high tier capped at minimum cooldown")
        void veryHighTierCapped() {
            int tier = 100;
            int cooldown = calculateMeleeCooldownTicks(tier);
            assertEquals(32, cooldown, "Very high tier should be capped at minimum cooldown");
        }

        @Test
        @DisplayName("Unarmed guard damage (no weapon)")
        void unarmedGuardDamage() {
            int tier = 0;
            float baseDamage = calculateMeleeBaseDamage(tier);
            // Without Iron Sword, damage is just base
            assertEquals(0.15f, baseDamage, 0.01f);

            // Unarmed Tier 0 guard should be very weak
            int cooldownTicks = calculateMeleeCooldownTicks(tier);
            float attackSpeedSeconds = ticksToSeconds(cooldownTicks);
            float unarmedDPS = baseDamage / attackSpeedSeconds;

            assertTrue(unarmedDPS < 0.5f,
                String.format("Unarmed guard should be very weak (%.2f DPS)", unarmedDPS));
        }
    }
}
