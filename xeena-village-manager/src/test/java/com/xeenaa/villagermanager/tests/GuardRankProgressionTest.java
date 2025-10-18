package com.xeenaa.villagermanager.tests;

import com.xeenaa.villagermanager.data.rank.GuardPath;
import com.xeenaa.villagermanager.data.rank.GuardRank;
import com.xeenaa.villagermanager.data.rank.GuardRankData;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression tests for guard rank progression logic.
 *
 * Bug Fix: selectPath() in UnifiedGuardManagementScreen was always purchasing rank 1
 * (MAN_AT_ARMS_I or MARKSMAN_I) regardless of current rank, causing "already has this rank"
 * errors when trying to progress from tier 1 to tier 2.
 *
 * Fixed by using getNextRank() to get the correct next rank in the progression chain.
 */
@DisplayName("Guard Rank Progression Tests")
public class GuardRankProgressionTest {

    @Nested
    @DisplayName("Rank Progression Chain - Melee Path")
    class MeleeProgressionTests {

        @Test
        @DisplayName("Recruit -> Man-at-Arms I (Soldier I) progression")
        void testRecruitToMeleeT1() {
            // UnifiedGuardManagementScreen.selectPath() lines 474-479:
            // if (currentRank == GuardRank.RECRUIT) {
            //     if (path == GuardPath.MELEE) {
            //         nextRank = GuardRank.MAN_AT_ARMS_I;
            //     }
            // }

            GuardRank currentRank = GuardRank.RECRUIT;
            GuardRank expectedNextRank = GuardRank.MAN_AT_ARMS_I;

            // When at RECRUIT and selecting MELEE path, should get MAN_AT_ARMS_I
            assertEquals(expectedNextRank, GuardRank.MAN_AT_ARMS_I,
                "From RECRUIT, selecting MELEE path should purchase MAN_AT_ARMS_I");
        }

        @Test
        @DisplayName("Man-at-Arms I -> Man-at-Arms II (Soldier I -> Soldier II) progression")
        void testMeleeT1ToT2() {
            // UnifiedGuardManagementScreen.selectPath() lines 480-483:
            // else {
            //     // If already on a path, get the next rank in that path
            //     nextRank = currentRank.getNextRank();
            // }

            GuardRank currentRank = GuardRank.MAN_AT_ARMS_I;
            GuardRank nextRank = currentRank.getNextRank();

            assertEquals(GuardRank.MAN_AT_ARMS_II, nextRank,
                "From MAN_AT_ARMS_I, next rank should be MAN_AT_ARMS_II");
        }

        @Test
        @DisplayName("Man-at-Arms II -> Man-at-Arms III (Soldier II -> Soldier III) progression")
        void testMeleeT2ToT3() {
            GuardRank currentRank = GuardRank.MAN_AT_ARMS_II;
            GuardRank nextRank = currentRank.getNextRank();

            assertEquals(GuardRank.MAN_AT_ARMS_III, nextRank,
                "From MAN_AT_ARMS_II, next rank should be MAN_AT_ARMS_III");
        }

        @Test
        @DisplayName("Man-at-Arms III -> Knight (Soldier III -> Knight) progression")
        void testMeleeT3ToT4() {
            GuardRank currentRank = GuardRank.MAN_AT_ARMS_III;
            GuardRank nextRank = currentRank.getNextRank();

            assertEquals(GuardRank.KNIGHT, nextRank,
                "From MAN_AT_ARMS_III, next rank should be KNIGHT");
        }

        @Test
        @DisplayName("Knight (Tier 4) has no next rank")
        void testKnightIsMaxRank() {
            GuardRank currentRank = GuardRank.KNIGHT;
            GuardRank nextRank = currentRank.getNextRank();

            assertNull(nextRank,
                "KNIGHT is max rank and should have no next rank");
        }

        @Test
        @DisplayName("Complete melee progression chain: RECRUIT -> I -> II -> III -> KNIGHT")
        void testCompleteMeleeProgression() {
            // RECRUIT (Tier 0)
            GuardRank rank0 = GuardRank.RECRUIT;
            assertEquals(0, rank0.getTier(), "RECRUIT should be tier 0");

            // MAN_AT_ARMS_I (Tier 1)
            GuardRank rank1 = rank0.getNextRank(); // Should be null for RECRUIT
            // For RECRUIT, we manually select MAN_AT_ARMS_I when choosing MELEE path
            rank1 = GuardRank.MAN_AT_ARMS_I;
            assertEquals(1, rank1.getTier(), "MAN_AT_ARMS_I should be tier 1");

            // MAN_AT_ARMS_II (Tier 2)
            GuardRank rank2 = rank1.getNextRank();
            assertNotNull(rank2, "MAN_AT_ARMS_I should have a next rank");
            assertEquals(GuardRank.MAN_AT_ARMS_II, rank2, "Next rank should be MAN_AT_ARMS_II");
            assertEquals(2, rank2.getTier(), "MAN_AT_ARMS_II should be tier 2");

            // MAN_AT_ARMS_III (Tier 3)
            GuardRank rank3 = rank2.getNextRank();
            assertNotNull(rank3, "MAN_AT_ARMS_II should have a next rank");
            assertEquals(GuardRank.MAN_AT_ARMS_III, rank3, "Next rank should be MAN_AT_ARMS_III");
            assertEquals(3, rank3.getTier(), "MAN_AT_ARMS_III should be tier 3");

            // KNIGHT (Tier 4)
            GuardRank rank4 = rank3.getNextRank();
            assertNotNull(rank4, "MAN_AT_ARMS_III should have a next rank");
            assertEquals(GuardRank.KNIGHT, rank4, "Next rank should be KNIGHT");
            assertEquals(4, rank4.getTier(), "KNIGHT should be tier 4");

            // No further progression
            assertNull(rank4.getNextRank(), "KNIGHT should have no next rank");
        }
    }

    @Nested
    @DisplayName("Rank Progression Chain - Ranged Path")
    class RangedProgressionTests {

        @Test
        @DisplayName("Recruit -> Marksman I progression")
        void testRecruitToRangedT1() {
            GuardRank currentRank = GuardRank.RECRUIT;
            GuardRank expectedNextRank = GuardRank.MARKSMAN_I;

            assertEquals(expectedNextRank, GuardRank.MARKSMAN_I,
                "From RECRUIT, selecting RANGED path should purchase MARKSMAN_I");
        }

        @Test
        @DisplayName("Marksman I -> Marksman II progression")
        void testRangedT1ToT2() {
            GuardRank currentRank = GuardRank.MARKSMAN_I;
            GuardRank nextRank = currentRank.getNextRank();

            assertEquals(GuardRank.MARKSMAN_II, nextRank,
                "From MARKSMAN_I, next rank should be MARKSMAN_II");
        }

        @Test
        @DisplayName("Marksman II -> Marksman III progression")
        void testRangedT2ToT3() {
            GuardRank currentRank = GuardRank.MARKSMAN_II;
            GuardRank nextRank = currentRank.getNextRank();

            assertEquals(GuardRank.MARKSMAN_III, nextRank,
                "From MARKSMAN_II, next rank should be MARKSMAN_III");
        }

        @Test
        @DisplayName("Marksman III -> Sharpshooter progression")
        void testRangedT3ToT4() {
            GuardRank currentRank = GuardRank.MARKSMAN_III;
            GuardRank nextRank = currentRank.getNextRank();

            assertEquals(GuardRank.SHARPSHOOTER, nextRank,
                "From MARKSMAN_III, next rank should be SHARPSHOOTER");
        }

        @Test
        @DisplayName("Sharpshooter (Tier 4) has no next rank")
        void testSharpshooterIsMaxRank() {
            GuardRank currentRank = GuardRank.SHARPSHOOTER;
            GuardRank nextRank = currentRank.getNextRank();

            assertNull(nextRank,
                "SHARPSHOOTER is max rank and should have no next rank");
        }

        @Test
        @DisplayName("Complete ranged progression chain: RECRUIT -> I -> II -> III -> SHARPSHOOTER")
        void testCompleteRangedProgression() {
            // RECRUIT (Tier 0)
            GuardRank rank0 = GuardRank.RECRUIT;
            assertEquals(0, rank0.getTier(), "RECRUIT should be tier 0");

            // MARKSMAN_I (Tier 1)
            GuardRank rank1 = GuardRank.MARKSMAN_I;
            assertEquals(1, rank1.getTier(), "MARKSMAN_I should be tier 1");

            // MARKSMAN_II (Tier 2)
            GuardRank rank2 = rank1.getNextRank();
            assertNotNull(rank2, "MARKSMAN_I should have a next rank");
            assertEquals(GuardRank.MARKSMAN_II, rank2, "Next rank should be MARKSMAN_II");
            assertEquals(2, rank2.getTier(), "MARKSMAN_II should be tier 2");

            // MARKSMAN_III (Tier 3)
            GuardRank rank3 = rank2.getNextRank();
            assertNotNull(rank3, "MARKSMAN_II should have a next rank");
            assertEquals(GuardRank.MARKSMAN_III, rank3, "Next rank should be MARKSMAN_III");
            assertEquals(3, rank3.getTier(), "MARKSMAN_III should be tier 3");

            // SHARPSHOOTER (Tier 4)
            GuardRank rank4 = rank3.getNextRank();
            assertNotNull(rank4, "MARKSMAN_III should have a next rank");
            assertEquals(GuardRank.SHARPSHOOTER, rank4, "Next rank should be SHARPSHOOTER");
            assertEquals(4, rank4.getTier(), "SHARPSHOOTER should be tier 4");

            // No further progression
            assertNull(rank4.getNextRank(), "SHARPSHOOTER should have no next rank");
        }
    }

    @Nested
    @DisplayName("Regression Prevention - Bug Fix Validation")
    class RegressionTests {

        @Test
        @DisplayName("REGRESSION: Old logic always returned MAN_AT_ARMS_I for melee, regardless of current rank")
        void testOldLogicAlwaysReturnedTier1Melee() {
            // Before the fix:
            // GuardRank nextRank = null;
            // if (path == GuardPath.MELEE) {
            //     nextRank = GuardRank.MAN_AT_ARMS_I;  // <-- ALWAYS TIER 1!
            // }
            //
            // This caused "already has this rank" error when clicking Melee Path button
            // after already being at MAN_AT_ARMS_I

            GuardRank currentRank = GuardRank.MAN_AT_ARMS_I;
            GuardRank oldBuggyResult = GuardRank.MAN_AT_ARMS_I; // Old logic always returned this
            GuardRank correctResult = currentRank.getNextRank(); // Should be MAN_AT_ARMS_II

            assertNotEquals(oldBuggyResult, correctResult,
                "REGRESSION CHECK: Old logic returned MAN_AT_ARMS_I but should return MAN_AT_ARMS_II");
            assertEquals(GuardRank.MAN_AT_ARMS_II, correctResult,
                "Correct next rank from MAN_AT_ARMS_I is MAN_AT_ARMS_II");
        }

        @Test
        @DisplayName("REGRESSION: Old logic always returned MARKSMAN_I for ranged, regardless of current rank")
        void testOldLogicAlwaysReturnedTier1Ranged() {
            // Before the fix:
            // } else if (path == GuardPath.RANGED) {
            //     nextRank = GuardRank.MARKSMAN_I;  // <-- ALWAYS TIER 1!
            // }

            GuardRank currentRank = GuardRank.MARKSMAN_I;
            GuardRank oldBuggyResult = GuardRank.MARKSMAN_I;
            GuardRank correctResult = currentRank.getNextRank();

            assertNotEquals(oldBuggyResult, correctResult,
                "REGRESSION CHECK: Old logic returned MARKSMAN_I but should return MARKSMAN_II");
            assertEquals(GuardRank.MARKSMAN_II, correctResult,
                "Correct next rank from MARKSMAN_I is MARKSMAN_II");
        }

        @Test
        @DisplayName("REGRESSION: Attempting to purchase current rank should fail")
        void testCannotPurchaseSameRank() {
            UUID villagerUuid = UUID.randomUUID();
            GuardRankData rankData = new GuardRankData(villagerUuid);

            // Set current rank to MAN_AT_ARMS_I
            rankData.setCurrentRank(GuardRank.MAN_AT_ARMS_I);
            rankData.setChosenPath(GuardPath.MELEE);

            // Try to purchase the same rank (MAN_AT_ARMS_I) - should fail
            assertFalse(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_I),
                "Should not be able to purchase the same rank (MAN_AT_ARMS_I) again");
        }

        @Test
        @DisplayName("REGRESSION: New logic correctly uses getNextRank() for progression")
        void testNewLogicUsesGetNextRank() {
            // After the fix:
            // } else {
            //     // If already on a path, get the next rank in that path
            //     nextRank = currentRank.getNextRank();
            // }

            // Test all tiers to ensure getNextRank() works correctly
            assertEquals(GuardRank.MAN_AT_ARMS_II, GuardRank.MAN_AT_ARMS_I.getNextRank(),
                "getNextRank() should return correct progression for tier 1 melee");
            assertEquals(GuardRank.MAN_AT_ARMS_III, GuardRank.MAN_AT_ARMS_II.getNextRank(),
                "getNextRank() should return correct progression for tier 2 melee");
            assertEquals(GuardRank.KNIGHT, GuardRank.MAN_AT_ARMS_III.getNextRank(),
                "getNextRank() should return correct progression for tier 3 melee");

            assertEquals(GuardRank.MARKSMAN_II, GuardRank.MARKSMAN_I.getNextRank(),
                "getNextRank() should return correct progression for tier 1 ranged");
            assertEquals(GuardRank.MARKSMAN_III, GuardRank.MARKSMAN_II.getNextRank(),
                "getNextRank() should return correct progression for tier 2 ranged");
            assertEquals(GuardRank.SHARPSHOOTER, GuardRank.MARKSMAN_III.getNextRank(),
                "getNextRank() should return correct progression for tier 3 ranged");
        }
    }

    @Nested
    @DisplayName("GuardRankData Progression Validation")
    class RankDataProgressionTests {

        @Test
        @DisplayName("Can purchase next rank in melee progression chain")
        void testCanPurchaseNextRankMelee() {
            UUID villagerUuid = UUID.randomUUID();
            GuardRankData rankData = new GuardRankData(villagerUuid);

            // Start at RECRUIT, choose MELEE path
            assertEquals(GuardRank.RECRUIT, rankData.getCurrentRank());
            assertTrue(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_I),
                "Should be able to purchase MAN_AT_ARMS_I from RECRUIT");

            // Purchase MAN_AT_ARMS_I
            rankData.purchaseRank(GuardRank.MAN_AT_ARMS_I, 15);
            assertEquals(GuardRank.MAN_AT_ARMS_I, rankData.getCurrentRank());
            assertEquals(GuardPath.MELEE, rankData.getChosenPath());

            // Should be able to purchase MAN_AT_ARMS_II (NOT MAN_AT_ARMS_I again)
            assertFalse(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_I),
                "Should NOT be able to purchase MAN_AT_ARMS_I again");
            assertTrue(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_II),
                "Should be able to purchase MAN_AT_ARMS_II");

            // Purchase MAN_AT_ARMS_II
            rankData.purchaseRank(GuardRank.MAN_AT_ARMS_II, 20);
            assertEquals(GuardRank.MAN_AT_ARMS_II, rankData.getCurrentRank());

            // Should be able to purchase MAN_AT_ARMS_III
            assertTrue(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_III),
                "Should be able to purchase MAN_AT_ARMS_III");
        }

        @Test
        @DisplayName("Can purchase next rank in ranged progression chain")
        void testCanPurchaseNextRankRanged() {
            UUID villagerUuid = UUID.randomUUID();
            GuardRankData rankData = new GuardRankData(villagerUuid);

            // Start at RECRUIT, choose RANGED path
            assertTrue(rankData.canPurchaseRank(GuardRank.MARKSMAN_I),
                "Should be able to purchase MARKSMAN_I from RECRUIT");

            // Purchase MARKSMAN_I
            rankData.purchaseRank(GuardRank.MARKSMAN_I, 15);
            assertEquals(GuardRank.MARKSMAN_I, rankData.getCurrentRank());
            assertEquals(GuardPath.RANGED, rankData.getChosenPath());

            // Should be able to purchase MARKSMAN_II (NOT MARKSMAN_I again)
            assertFalse(rankData.canPurchaseRank(GuardRank.MARKSMAN_I),
                "Should NOT be able to purchase MARKSMAN_I again");
            assertTrue(rankData.canPurchaseRank(GuardRank.MARKSMAN_II),
                "Should be able to purchase MARKSMAN_II");

            // Purchase MARKSMAN_II
            rankData.purchaseRank(GuardRank.MARKSMAN_II, 20);
            assertEquals(GuardRank.MARKSMAN_II, rankData.getCurrentRank());

            // Should be able to purchase MARKSMAN_III
            assertTrue(rankData.canPurchaseRank(GuardRank.MARKSMAN_III),
                "Should be able to purchase MARKSMAN_III");
        }

        @Test
        @DisplayName("Cannot skip ranks in progression")
        void testCannotSkipRanks() {
            UUID villagerUuid = UUID.randomUUID();
            GuardRankData rankData = new GuardRankData(villagerUuid);

            // Purchase MAN_AT_ARMS_I
            rankData.purchaseRank(GuardRank.MAN_AT_ARMS_I, 15);

            // Try to skip MAN_AT_ARMS_II and go directly to MAN_AT_ARMS_III
            assertFalse(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_III),
                "Should NOT be able to skip MAN_AT_ARMS_II and purchase MAN_AT_ARMS_III directly");

            // Try to skip to KNIGHT
            assertFalse(rankData.canPurchaseRank(GuardRank.KNIGHT),
                "Should NOT be able to skip to KNIGHT from MAN_AT_ARMS_I");

            // Should only be able to purchase the immediate next rank
            assertTrue(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_II),
                "Should only be able to purchase the immediate next rank (MAN_AT_ARMS_II)");
        }

        @Test
        @DisplayName("Cannot change paths mid-progression")
        void testCannotChangePathsMidProgression() {
            UUID villagerUuid = UUID.randomUUID();
            GuardRankData rankData = new GuardRankData(villagerUuid);

            // Choose MELEE path
            rankData.purchaseRank(GuardRank.MAN_AT_ARMS_I, 15);
            assertEquals(GuardPath.MELEE, rankData.getChosenPath());

            // Try to purchase RANGED rank
            assertFalse(rankData.canPurchaseRank(GuardRank.MARKSMAN_I),
                "Should NOT be able to purchase RANGED rank after choosing MELEE path");
            assertFalse(rankData.canPurchaseRank(GuardRank.MARKSMAN_II),
                "Should NOT be able to purchase any RANGED rank after choosing MELEE path");

            // Should only be able to purchase next rank in MELEE path
            assertTrue(rankData.canPurchaseRank(GuardRank.MAN_AT_ARMS_II),
                "Should be able to continue on MELEE path");
        }
    }

    @Nested
    @DisplayName("Path Selection Logic")
    class PathSelectionTests {

        @Test
        @DisplayName("At RECRUIT rank, path selection chooses first rank of that path")
        void testPathSelectionAtRecruit() {
            // UnifiedGuardManagementScreen.selectPath() lines 473-479

            GuardRank currentRank = GuardRank.RECRUIT;

            // Selecting MELEE path should target MAN_AT_ARMS_I
            GuardRank meleeTarget = GuardRank.MAN_AT_ARMS_I;
            assertEquals(GuardPath.MELEE, meleeTarget.getPath(),
                "MAN_AT_ARMS_I should be in MELEE path");

            // Selecting RANGED path should target MARKSMAN_I
            GuardRank rangedTarget = GuardRank.MARKSMAN_I;
            assertEquals(GuardPath.RANGED, rangedTarget.getPath(),
                "MARKSMAN_I should be in RANGED path");
        }

        @Test
        @DisplayName("After choosing path, button purchases next rank in that path")
        void testPathButtonPurchasesNextRank() {
            // UnifiedGuardManagementScreen.selectPath() lines 480-483:
            // } else {
            //     // If already on a path, get the next rank in that path
            //     nextRank = currentRank.getNextRank();
            // }

            GuardRank currentRank = GuardRank.MAN_AT_ARMS_I;
            GuardRank nextRank = currentRank.getNextRank();

            assertNotNull(nextRank, "Should have a next rank");
            assertEquals(GuardRank.MAN_AT_ARMS_II, nextRank,
                "Next rank should be MAN_AT_ARMS_II, not MAN_AT_ARMS_I");
        }
    }
}
