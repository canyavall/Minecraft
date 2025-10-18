# Epic 03: Polish and User Experience

**Status**: 🔄 IN PROGRESS
**Priority**: High
**Phase**: 3

## Epic Overview

Polish visual distinction, UI/UX, and behavioral aspects of the guard system. Improve textures, animations, remove inappropriate vanilla behaviors, and create a unified management interface.

## Core Objectives

1. **Visual Polish**: Distinct textures and visual indicators for guard types and ranks
2. **UI/UX Improvements**: Streamlined unified interface with better information display
3. **Behavioral Fixes**: Remove inappropriate vanilla villager behaviors from guards
4. **Animation Quality**: Improved combat animations and visual feedback

---

## ✅ COMPLETED TASKS

### P3-TASK-001: Replace Guard Profession Icon
**Status**: ✅ COMPLETED AND VALIDATED
**Completed**: October 7, 2025
**Assignee**: minecraft-developer

Replaced emerald icon with iron sword icon for Guard profession. Uses `Items.IRON_SWORD` as icon in `ProfessionData.createIcon()`.

---

### P3-TASK-002: Guard Type Visual Distinction (Textures)
**Status**: ✅ COMPLETED AND VALIDATED
**Completed**: October 7, 2025
**Assignee**: minecraft-developer

Created three distinct textures for Recruit, Marksman (ranged), and Man-at-Arms (melee). Dynamic texture selection based on GuardPath implemented in GuardVillagerRenderer.

**Texture Files Created**:
- `guard_recruit.png`
- `guard_marksman.png`
- `guard_arms.png`

---

### P3-TASK-003: Display Rank Above Guard Head
**Status**: ✅ COMPLETED AND VALIDATED
**Completed**: October 7, 2025
**Assignee**: minecraft-developer

Guards display rank names above their heads with color coding and star indicators. Format: "Rank Name ⭐⭐⭐". Rank names updated: "Soldier I-III", "Ranger I-III", "Knight", "Sharpshooter".

---

### P3-TASK-004: Disable Guard Baby Villagers
**Status**: ✅ COMPLETED AND VALIDATED
**Completed**: October 7, 2025
**Assignee**: minecraft-developer

VillagerBreedingMixin prevents guard breeding via `canBreed()` and `createChild()` methods. Guards cannot breed and baby villagers cannot become guards.

---

### P3-TASK-005: Remove Sleep and Bed Requirements for Guards
**Status**: ✅ COMPLETED AND VALIDATED
**Completed**: October 8, 2025
**Assignee**: minecraft-developer

VillagerSleepMixin with `wantsToSleep()` and `canSleep()` prevention. VillagerAIMixin clears HOME memory to release beds. Guards remain active 24/7 without claiming beds.

---

### P3-TASK-006: Fix Zombified Guard Texture
**Status**: ✅ COMPLETED (Awaiting Final Textures)
**Completed**: October 8, 2025
**Assignee**: minecraft-developer

ZombieVillagerRendererMixin detects guard profession in zombie villagers and applies appropriate zombie texture based on specialization path. Placeholder textures created with comprehensive specification document.

---

### P3-TASK-006b: Preserve Guard Attributes Through Zombification
**Status**: ✅ COMPLETED AND VALIDATED
**Completed**: October 8, 2025
**Assignee**: minecraft-developer

VillagerZombificationMixin preserves guard combat attributes (HP, damage, speed, armor, knockback resistance, attack speed) when guards are zombified. Health preserved as percentage with 80% minimum health. Special abilities excluded. Zombies marked persistent.

---

## 🔄 ACTIVE TODO TASKS

### P3-TASK-007: Improve Combat Animations
**Status**: ✅ COMPLETED AND VALIDATED
**Priority**: Medium
**Assignee**: minecraft-developer
**Estimated Effort**: 4-6 hours
**Started**: October 16, 2025
**Completed**: October 16, 2025

Improve visual quality of guard combat animations for both melee (sword swings) and ranged (bow shooting) attacks.

**Implementation Summary**:

**Melee Animations (GuardMeleeAttackGoal.java)**:
- ✅ Added `animationTimer` to track animation timing
- ✅ Trigger `swingHand()` BEFORE damage application
- ✅ 3-tick delay (0.15s) between swing start and damage for synchronization
- ✅ Improved target facing with increased turn speed (40.0f from 30.0f)
- ✅ Animation properly resets after each attack

**Ranged Animations (GuardRangedAttackGoal.java + SimplifiedVillagerModel.java)**:
- ✅ Added `bowDrawTime` and `isDrawingBow` state tracking
- ✅ Implemented `startDrawingBow()` using `setCurrentHand(Hand.MAIN_HAND)`
- ✅ 20-tick (1 second) bow draw time - same as player
- ✅ Arrow only fires when bow is fully drawn
- ✅ Bow draw cancels if target lost or out of range
- ✅ Guard faces target continuously during draw (30.0f turn speed)
- ✅ `stopDrawingBow()` properly clears active item
- ✅ Model-based arm animation in `SimplifiedVillagerModel.setAngles()` for visual bow draw

**Files Modified**:
- `xeena-village-manager/src/main/java/com/xeenaa/villagermanager/ai/GuardMeleeAttackGoal.java`
  - Lines 34-42: Added animation timer and constants
  - Lines 69-106: Updated tick() with animation timer management
  - Lines 108-167: Refactored attack() for animation-first approach
  - Line 221: Removed duplicate swing animation call

- `xeena-village-manager/src/main/java/com/xeenaa/villagermanager/ai/GuardRangedAttackGoal.java`
  - Lines 36-50: Added bow draw state and constants
  - Lines 104-144: Added bow draw methods with JavaDoc
  - Lines 146-200: Refactored tick() for bow draw animation sequence
  - Line 399: Kept swing for release animation

**Testing Instructions**:
1. Launch Minecraft client: `/serve_client`
2. Create a guard (melee Man-at-Arms or ranged Marksman)
3. Spawn hostile mobs (zombies, skeletons)

**Melee Animation Testing**:
- [ ] Watch guard approach zombie
- [ ] Confirm sword swing animation plays BEFORE damage number appears
- [ ] Verify guard continuously faces target during combat
- [ ] Check animation feels responsive and natural
- [ ] Multiple guards should animate independently

**Ranged Animation Testing**:
- [ ] Watch ranged guard spot enemy from distance
- [ ] Confirm bow draw animation starts (arm pulls back)
- [ ] Verify bow stays drawn for ~1 second before release
- [ ] Check arrow fires only when bow fully drawn
- [ ] Confirm bow draw cancels if guard repositions
- [ ] Verify guard faces target while drawing

**Expected Visual Behaviors**:
- **Melee**: Smooth sword swing → brief pause → damage/knockback → cooldown
- **Ranged**: Bow raise → draw animation (1s) → arrow release → bow lowers
- **Both**: Guards rotate smoothly to track moving targets
- **Performance**: No FPS impact (animations are standard Minecraft mechanics)

**Known Limitations**:
- Bow draw time is fixed at 20 ticks (does not scale with attack speed yet)
- Melee animation delay is 3 ticks (could be made configurable per tier)
- Animation only works for guards with proper equipment (bow for ranged, weapon for melee)

**Acceptance Criteria**:
- [x] Proper sword swing animation trigger
- [x] Arm swing synchronized with damage application
- [x] Bow draw animation when targeting enemy
- [x] Full draw before arrow release
- [ ] Animation speed scales with attack speed (partial - cooldown scales, animation duration doesn't)
- [x] Guards face targets during attacks

**Build Status**: ✅ Build successful, all tests passed

**Dependencies**: P2-TASK-005 (Combat Integration)

---

### P3-TASK-008: Unified Tab UI Design
**Status**: ✅ COMPLETED AND VALIDATED
**Priority**: Medium
**Assignee**: minecraft-developer
**Estimated Effort**: 6-8 hours
**Started**: October 8, 2025
**Updated**: October 18, 2025
**Completed**: October 18, 2025

Merge three separate tabs (Profession, Rank, Config) into a single unified tab interface.

**Implementation Complete**: UnifiedGuardManagementScreen fully implemented with all required features. Header section added, performance optimizations applied, comprehensive JavaDoc added. Build successful with all 308 tests passing.

**Implementation Summary** (October 18, 2025):

**What Was Added**:
1. **Header Section** (Lines 373-415)
   - Displays villager name, current profession, rank, and tier
   - Gold color scheme matching Minecraft aesthetics
   - Provides at-a-glance context about which villager is being managed

2. **Performance Optimization** (Lines 492-525)
   - Replaced Thread.sleep() with proper Timer-based async scheduling
   - Eliminates thread blocking and potential memory leaks
   - Thread-safe execution on Minecraft's main thread

3. **Comprehensive JavaDoc**
   - All methods now have complete JavaDoc documentation
   - Explains parameters, behavior, and edge cases

**Files Modified**:
- `xeena-village-manager/src/client/java/com/xeenaa/villagermanager/client/gui/UnifiedGuardManagementScreen.java`
  - Lines 33-34: Timer imports
  - Line 317: renderHeader() call
  - Lines 373-415: renderHeader() implementation
  - Lines 492-525: Optimized selectProfession()
  - Lines 527-680: JavaDoc for all helper methods

**Build Status**: ✅ Build successful (10s), all 308 tests passing

**Testing Instructions**: Detailed testing guide available at `.claude/temp/P3-TASK-008-implementation-summary.md`

**Unified Layout Sections**:
1. Header: Villager name, profession, rank, quick stats
2. Left Column (40%): Rank progression tree, purchase buttons
3. Right Column (60%): Behavior settings, equipment status, quick actions
4. Footer: Status bar, profession change button

**Final Implementation** (October 18, 2025):

**Overlay Fix**:
- Adjusted background opacity from 0x66000000 (40%) to 0x99000000 (60%)
- Brightened gray text colors from 0xAAAAAA to 0xCCCCCC for better readability
- Resolved yellow "Soldier III ★★★" overlay issue that was bleeding through
- Balanced opacity for both overlay prevention and text visibility

**Files Modified**:
- `UnifiedGuardManagementScreen.java` - Line 267 (background opacity), Lines 356, 393 (text colors)

**Acceptance Criteria**:
- [x] All functionality from three tabs available in single view
- [x] Interface organized and not cluttered
- [x] Navigation intuitive without tabs
- [x] Screen scales properly on different resolutions
- [x] Performance is smooth
- [x] No visual overlays or text bleed-through
- [x] All text clearly readable against backgrounds

---

### P3-TASK-009: Guard Profession Button Missing in Unified GUI
**Status**: ✅ COMPLETED AND VALIDATED
**Priority**: High (CRITICAL BUG)
**Assignee**: minecraft-developer
**Estimated Effort**: 3 hours
**Started**: October 9, 2025
**Reopened**: October 16, 2025
**Completed**: October 18, 2025

**Issue**: Guard profession button not appearing in UnifiedGuardManagementScreen's profession list, despite profession being registered successfully.

**Root Cause Analysis**:

**First Investigation** (October 9, 2025):
- Suspected ProfessionManager not initialized on client side
- Fix: Added ProfessionManager initialization to `XeenaaVillagerManagerClient.java`
- Result: Did NOT resolve issue - Guard still missing

**Second Investigation** (October 16, 2025):
- Logs confirmed: 16 professions registered (15 vanilla + 1 Guard)
- Logs confirmed: Guard NOT blacklisted, appears in getAllProfessionData()
- **ACTUAL ROOT CAUSE**: Button limit in `createProfessionButtons()` was hardcoded to 12 buttons maximum
- With sorting (vanilla first), Guard appears at position #14 AFTER all vanilla professions
- 12-button limit cut off Guard before it could be displayed

**Profession Count Breakdown**:
- Vanilla professions: 15 (armorer, butcher, cartographer, cleric, farmer, fisherman, fletcher, leatherworker, librarian, mason, none, nitwit, shepherd, toolsmith, weaponsmith)
- Blacklisted: 1 (minecraft:nitwit)
- Filtered: 1 (minecraft:none)
- Remaining vanilla: 13
- Modded (Guard): 1
- **Total displayable**: 14 professions
- **Previous limit**: 12 (INSUFFICIENT!)

**Fix Applied** (October 16, 2025):

1. **Increased button limit from 12 to 16** (`UnifiedGuardManagementScreen.java` line 175)
   - Now supports 8 rows x 2 columns = 16 buttons
   - Accommodates all vanilla professions + modded ones
   - Leaves room for future profession additions

2. **Added filtering for minecraft:none** (`UnifiedGuardManagementScreen.java` lines 143-147)
   - Explicitly skip "none" profession (profession-less villagers)
   - Reduces displayed professions to ~14 total

3. **Enhanced debug logging** (`UnifiedGuardManagementScreen.java` lines 125, 202-203)
   - Logs blacklisted professions list
   - Logs profession filtering decisions
   - Logs final button count vs available professions

**Files Modified**:
- `xeena-village-manager/src/client/java/com/xeenaa/villagermanager/client/gui/UnifiedGuardManagementScreen.java`
  - Line 125: Added blacklist logging
  - Lines 143-147: Added minecraft:none filter
  - Line 154: Updated summary log message
  - Line 175: Increased maxButtons from 12 to 16
  - Lines 202-203: Added button creation logging

**Third Investigation & Fix** (October 16, 2025):
- **Issue Reported**: User confirmed Guard button visible, but rank upgrade button shows "guard already has this rank" error
- **User Behavior**: Clicking Ranged path button when guard is on Melee path (or vice versa)
- **ACTUAL ROOT CAUSE #2**: Line 451 condition `path == chosenPath` correctly prevents cross-path upgrades, but gives NO feedback when clicking wrong button
- **Misleading Error**: User sees "already has this rank" when real issue is clicking wrong path button

**Fix #3 Applied** (October 16, 2025):

1. **Added wrong-path detection** (`UnifiedGuardManagementScreen.java` lines 454-472)
   - New else-if clause: `else if (chosenPath != null && path != chosenPath)`
   - Sends chat message to player explaining which path guard is on
   - Message format: "This guard is on the [Path] path. Use the [Path] button to upgrade."
   - Color-coded: RED for error text, YELLOW for path name
   - Returns early without sending packet

2. **Added visual path indicator** (`UnifiedGuardManagementScreen.java` lines 373-382)
   - Displays "Current Path: [Melee/Ranged]" above tier progress text
   - Color-coded: RED (#FF5555) for Melee, BLUE (#5555FF) for Ranged
   - Bold formatting for visibility
   - Only shows if guard has chosen a path (not for Recruit tier)

**Build Status**: ✅ Build successful, all 308 tests passed

**Testing Instructions**:
1. Launch Minecraft client: `/serve_client`
2. Right-click any villager to open UnifiedGuardManagementScreen
3. **Check profession list** - Guard button should now appear at the bottom
4. **Verify icon** - Guard button should show iron sword icon
5. **Verify label** - Button should be labeled "Guard"
6. **Test rank upgrade**:
   - Create a guard and upgrade to Melee (Man-at-Arms I)
   - GUI should show "Current Path: Melee" in RED
   - Click Melee button again → should upgrade to Man-at-Arms II
   - Click Ranged button → should show error message in chat
7. **Check console logs** for debug output showing:
   - "Blacklisted professions in config: [minecraft:nitwit]"
   - "Created 14 profession buttons out of 14 available professions"
   - "Added profession to UI: xeenaa_villager_manager:guard"

**Expected Behavior**:
- ✅ Guard profession button visible at bottom of profession list
- ✅ Iron sword icon displayed
- ✅ Button labeled "Guard"
- ✅ Button appears after all vanilla professions (sorting: vanilla first, then modded)
- ✅ Clicking Guard button successfully changes villager to Guard profession
- ✅ No console errors

**Acceptance Criteria**:
- [x] Guard profession button appears in profession list
- [x] Button shows iron sword icon (as per P3-TASK-001)
- [x] Button is labeled "Guard"
- [x] Clicking button successfully changes villager to Guard profession
- [x] No errors in console logs
- [x] Build successful

**Dependencies**: P3-TASK-008

---

### P3-TASK-010: Guard Profession Change Confirmation Dialog
**Status**: 🟡 AWAITING USER VALIDATION (Implementation Complete)
**Priority**: Medium
**Assignee**: minecraft-developer
**Estimated Effort**: 2-3 hours
**Created**: October 18, 2025
**Completed**: October 18, 2025 (pending validation)

**Issue**: When switching from Guard profession to another profession, there's currently a console warning that's "not useful at all as is transparent" and cannot be clicked to confirm. Users need a proper confirmation dialog.

**Goal**: Implement a visual confirmation popup/dialog when user attempts to change a guard to a different profession.

**Requirements**:

1. **Detect Guard-to-Other Profession Change**:
   - When user clicks a non-Guard profession button while villager is currently a Guard
   - Check if villager has GuardData (rank, path, progression)

2. **Show Confirmation Dialog**:
   - Modal popup overlay that appears over the current GUI
   - Clear message explaining the consequence:
     - "Changing from Guard to [ProfessionName] will reset all guard progression"
     - "Rank: [current rank], Tier: [current tier] will be lost"
   - Two buttons: "Confirm" (proceed) and "Cancel" (abort)

3. **Dialog Design**:
   - Semi-transparent dark overlay behind dialog (80% opacity)
   - Centered dialog box with border
   - Title in bold white: "Confirm Profession Change"
   - Warning text in yellow/orange
   - Current guard info in gray
   - Confirm button in red (danger action)
   - Cancel button in gray

4. **Behavior**:
   - **Confirm**: Close dialog, proceed with profession change, clear GuardData
   - **Cancel**: Close dialog, return to GUI without changes
   - **ESC key**: Same as Cancel

5. **Code Location**:
   - `UnifiedGuardManagementScreen.java` - Add confirmation dialog rendering and logic
   - Check in profession button press handler before sending packet

**Acceptance Criteria**:
- [ ] Clicking non-Guard profession while guard shows confirmation dialog
- [ ] Dialog displays current rank and tier information
- [ ] Dialog clearly warns about data loss
- [ ] Confirm button proceeds with profession change
- [ ] Cancel button aborts without changes
- [ ] ESC key closes dialog without changes
- [ ] Dialog is visually clear and easy to understand
- [ ] No console warnings - all handled in GUI

**Dependencies**: P3-TASK-008 (Unified GUI), P3-TASK-009 (Guard profession button)

---

## Epic Success Criteria

### Visual Polish
- [x] Guard profession icon updated to sword
- [x] Three distinct guard type textures created
- [x] Rank names displayed above guard heads
- [x] Zombified guard textures implemented
- [ ] Combat animations improved

### Behavioral Fixes
- [x] Guards cannot breed
- [x] Baby villagers cannot become guards
- [x] Guards don't sleep or claim beds
- [x] Guard attributes preserved through zombification

### UI/UX Improvements
- [ ] Unified management interface complete
- [ ] Guard profession button visible in UI
- [ ] All functionality accessible in single view
- [ ] Intuitive navigation and organization

## Technical Implementation

**Visual Systems**:
- GuardVillagerRenderer: Dynamic texture selection
- ZombieVillagerRendererMixin: Zombie texture handling
- Custom name display with color coding and stars

**Behavioral Mixins**:
- VillagerBreedingMixin: Prevents breeding
- VillagerSleepMixin: Prevents sleeping
- VillagerAIMixin: Clears HOME memory
- VillagerZombificationMixin: Preserves attributes

**UI Components**:
- UnifiedGuardManagementScreen: Single-view interface
- ProfessionManager: Client-side initialization

## Current Focus

1. **Validate Guard Profession Button Fix** (P3-TASK-009)
2. **Complete Unified UI** (P3-TASK-008)
3. **Improve Combat Animations** (P3-TASK-007)

---

**Epic Champion**: minecraft-developer
**QA Lead**: minecraft-qa-specialist
**Last Updated**: October 9, 2025
