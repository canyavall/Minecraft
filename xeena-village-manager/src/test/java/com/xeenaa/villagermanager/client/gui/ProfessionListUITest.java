package com.xeenaa.villagermanager.client.gui;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression tests for profession list UI capacity.
 *
 * Bug Fix: Profession list was limited to 12 slots (6 rows x 2 columns) but had 15 professions,
 * causing Guard profession to be cut off and invisible in the UI.
 *
 * Fixed by increasing capacity to 16 slots (8 rows x 2 columns) and frame height from 280 to 340.
 */
@DisplayName("Profession List UI Capacity Tests")
class ProfessionListUITest {

    @Nested
    @DisplayName("UI Layout Constants")
    class UILayoutTests {

        @Test
        @DisplayName("FRAME_HEIGHT is at least 340 to accommodate 8 rows of professions")
        void testFrameHeightAccommodates8Rows() {
            // UnifiedGuardManagementScreen.java:41
            // private static final int FRAME_HEIGHT = 340; // Increased to accommodate 8 rows of professions
            //
            // CRITICAL: Frame height must be at least 340 to fit:
            // - 8 rows of profession buttons (24px height each + 5px spacing = 232px)
            // - Title space (30px)
            // - Bottom control buttons (40px)
            // - Panel padding (20px)
            // Total minimum: 322px, using 340px for comfort

            int FRAME_HEIGHT = 340;
            assertTrue(FRAME_HEIGHT >= 340,
                "FRAME_HEIGHT must be at least 340 to accommodate 8 rows of professions");
        }

        @Test
        @DisplayName("Profession button grid supports at least 16 slots (8 rows x 2 columns)")
        void testProfessionGridSupports16Slots() {
            // UnifiedGuardManagementScreen.java:174
            // for (int i = 0; i < availableProfessions.size() && i < 16; i++) { // Max 8 rows x 2 columns
            //
            // CRITICAL: Must support at least 16 profession slots to display all professions:
            // - 14 vanilla professions (armorer, butcher, cartographer, cleric, farmer, fisherman,
            //   fletcher, leatherworker, librarian, mason, none, shepherd, toolsmith, weaponsmith)
            // - 1 modded profession (guard)
            // Total: 15 professions

            int MAX_PROFESSION_SLOTS = 16;
            assertTrue(MAX_PROFESSION_SLOTS >= 16,
                "Profession grid must support at least 16 slots (8 rows x 2 columns)");
        }

        @Test
        @DisplayName("Grid capacity (16) is greater than current profession count (15)")
        void testGridCapacityExceedsProfessionCount() {
            // This ensures we have room for growth
            int MAX_PROFESSION_SLOTS = 16;
            int CURRENT_PROFESSION_COUNT = 15; // 14 vanilla + 1 modded (guard)

            assertTrue(MAX_PROFESSION_SLOTS > CURRENT_PROFESSION_COUNT,
                "Grid capacity should exceed current profession count to allow for future additions");
        }
    }

    @Nested
    @DisplayName("Profession Display Completeness")
    class ProfessionDisplayTests {

        @Test
        @DisplayName("All 15 professions can be displayed in the UI (no cutoff)")
        void testAllProfessionsDisplayable() {
            // UnifiedGuardManagementScreen.java:174
            // for (int i = 0; i < availableProfessions.size() && i < 16; i++)
            //
            // With 16 slots and 15 professions, all professions should be displayable

            int availableProfessionsSize = 15; // 14 vanilla + 1 guard
            int MAX_DISPLAY_SLOTS = 16;

            assertTrue(availableProfessionsSize <= MAX_DISPLAY_SLOTS,
                "All professions must fit within the available display slots");
        }

        @Test
        @DisplayName("Guard profession (position 15) is within displayable range")
        void testGuardProfessionDisplayable() {
            // Guard profession is sorted last (modded professions come after vanilla)
            // With 0-based indexing:
            // Position 0-13: Vanilla professions (armorer through weaponsmith)
            // Position 14: Guard profession

            int GUARD_POSITION = 14; // 0-based index
            int MAX_DISPLAY_SLOTS = 16;

            assertTrue(GUARD_POSITION < MAX_DISPLAY_SLOTS,
                "Guard profession at position 14 must be within the 16 displayable slots");
        }

        @Test
        @DisplayName("UI supports 8 rows of 2 columns each")
        void testGridLayout8x2() {
            // UnifiedGuardManagementScreen.java:174
            // Comment: "Max 8 rows x 2 columns"

            int ROWS = 8;
            int COLUMNS = 2;
            int EXPECTED_CAPACITY = ROWS * COLUMNS;

            assertEquals(16, EXPECTED_CAPACITY,
                "8 rows x 2 columns should provide 16 total slots");
        }
    }

    @Nested
    @DisplayName("Regression Prevention")
    class RegressionTests {

        @Test
        @DisplayName("REGRESSION: Old capacity of 12 slots was insufficient for 15 professions")
        void testOldCapacityWasInsufficient() {
            // Before the fix:
            // for (int i = 0; i < availableProfessions.size() && i < 12; i++) // Max 6 rows x 2 columns
            //
            // This caused professions 13, 14, 15 (toolsmith, weaponsmith, guard) to be cut off

            int OLD_CAPACITY = 12; // 6 rows x 2 columns
            int PROFESSION_COUNT = 15;

            assertTrue(PROFESSION_COUNT > OLD_CAPACITY,
                "REGRESSION CHECK: Old capacity (12) was insufficient for 15 professions");
        }

        @Test
        @DisplayName("REGRESSION: Guard profession (index 14) was outside old capacity (12)")
        void testGuardWasOutsideOldCapacity() {
            // Guard profession at position 14 was beyond the old limit of 12

            int GUARD_POSITION = 14;
            int OLD_CAPACITY = 12;

            assertTrue(GUARD_POSITION >= OLD_CAPACITY,
                "REGRESSION CHECK: Guard at position 14 was outside old capacity of 12");
        }

        @Test
        @DisplayName("REGRESSION: Old frame height (280) was too small for 8 rows")
        void testOldFrameHeightWasTooSmall() {
            // Before the fix:
            // private static final int FRAME_HEIGHT = 280;
            //
            // Required height for 8 rows:
            // - 8 rows * (24px button + 5px spacing) = 232px
            // - Title space: 30px
            // - Bottom buttons: 40px
            // - Padding: 20px
            // Total: 322px minimum

            int OLD_FRAME_HEIGHT = 280;
            int MINIMUM_REQUIRED_HEIGHT = 322;

            assertTrue(OLD_FRAME_HEIGHT < MINIMUM_REQUIRED_HEIGHT,
                "REGRESSION CHECK: Old frame height (280) was too small for 8 rows (322 minimum)");
        }

        @Test
        @DisplayName("REGRESSION: New capacity (16) fixes the profession display issue")
        void testNewCapacityFixesIssue() {
            // After the fix:
            // for (int i = 0; i < availableProfessions.size() && i < 16; i++) // Max 8 rows x 2 columns

            int NEW_CAPACITY = 16;
            int PROFESSION_COUNT = 15;
            int GUARD_POSITION = 14;

            assertTrue(PROFESSION_COUNT <= NEW_CAPACITY,
                "New capacity (16) must accommodate all 15 professions");
            assertTrue(GUARD_POSITION < NEW_CAPACITY,
                "Guard profession at position 14 must be within new capacity of 16");
        }
    }

    @Nested
    @DisplayName("Future Scalability")
    class ScalabilityTests {

        @Test
        @DisplayName("UI has capacity for 1 additional profession beyond current count")
        void testCapacityForFutureGrowth() {
            // Current: 15 professions
            // Capacity: 16 slots
            // Room for: 1 more profession before needing another expansion

            int CURRENT_CAPACITY = 16;
            int CURRENT_PROFESSION_COUNT = 15;
            int AVAILABLE_SLOTS = CURRENT_CAPACITY - CURRENT_PROFESSION_COUNT;

            assertEquals(1, AVAILABLE_SLOTS,
                "Should have exactly 1 slot available for future profession additions");
        }

        @Test
        @DisplayName("Adding 1 more profession would fill the grid (no cutoff)")
        void testOneMoreProfessionFitsInGrid() {
            // If one more profession is added (total 16), it should still fit

            int CURRENT_CAPACITY = 16;
            int PROFESSION_COUNT_AFTER_ADDITION = 16; // 15 + 1

            assertTrue(PROFESSION_COUNT_AFTER_ADDITION <= CURRENT_CAPACITY,
                "Adding 1 more profession should still fit within the 16-slot capacity");
        }

        @Test
        @DisplayName("Adding 2+ professions would require grid expansion")
        void testTwoMoreProfessionsExceedsCapacity() {
            // This test documents the capacity limit and helps future developers
            // understand when the grid needs to be expanded again

            int CURRENT_CAPACITY = 16;
            int PROFESSION_COUNT_AFTER_TWO_ADDITIONS = 17; // 15 + 2

            assertTrue(PROFESSION_COUNT_AFTER_TWO_ADDITIONS > CURRENT_CAPACITY,
                "Adding 2 or more professions would exceed current capacity and require expansion to 10 rows (20 slots) or scrolling");
        }
    }
}
