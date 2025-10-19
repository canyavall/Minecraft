# P4-TASK-001: Guard Modes Manual Test Plan

**Task**: Test all guard modes (STAND, FOLLOW, PATROL)
**Priority**: High
**Estimated Time**: 2-3 hours
**Date Created**: October 19, 2025

---

## Test Overview

This document provides a comprehensive manual testing plan for the three guard movement modes:
- **STAND**: Guard stands still at current location
- **FOLLOW**: Guard follows players or villagers
- **PATROL**: Guard patrols around their guard post workstation

### System Architecture Summary

Guard modes are implemented as AI goals with priorities:
- Priority 5: GuardFollowVillagerGoal (FOLLOW mode)
- Priority 7: GuardPatrolGoal (PATROL mode)
- Priority 8: GuardStandGoal (STAND mode)

Only ONE mode goal runs at a time based on the current `guardMode` setting in GuardData.

---

## Prerequisites

### Before Testing:
1. Launch Minecraft client in creative mode
2. Prepare test environment:
   - Flat area for testing movement
   - At least 2 villagers to convert to guards
   - Guard Post workstation block
   - Test hostile mobs (zombies) for combat interruption testing
3. Enable debug logging (already present via System.out.println)

### Test Setup:
```
Test Area Layout:
- 50x50 flat area (stone or grass blocks)
- Guard Post placed at center (0, 0 relative)
- Marker blocks at 5, 10, 15, 20 block distances from center
- Spawn villagers and convert to guards
```

---

## Test Suite 1: STAND Mode

### Test 1.1: Basic STAND Behavior
**Objective**: Verify guard stops all movement and stays in place

**Steps**:
1. Convert a villager to guard profession
2. Open Guard Management UI
3. Set guard mode to STAND
4. Note guard's current position
5. Wait 2 minutes (2400 ticks)

**Expected Results**:
- ✅ Guard stops moving immediately
- ✅ Guard remains at original position
- ✅ Guard can rotate head/body for looking
- ✅ Navigation is continuously cancelled

**Debug Log Validation**:
```
Expected logs:
- "GUARD AI: Added GuardStandGoal"
- GuardStandGoal.canStart() returns true
- GuardStandGoal.tick() continuously calls guard.getNavigation().stop()
```

**Pass Criteria**: Guard does not move more than 0.5 blocks from start position

---

### Test 1.2: STAND Mode During Combat
**Objective**: Verify guard can still attack while in STAND mode

**Steps**:
1. Set guard to STAND mode
2. Spawn a zombie near the guard (within 10 blocks)
3. Observe guard behavior

**Expected Results**:
- ✅ Guard rotates to face zombie
- ✅ Guard attacks zombie (melee or ranged)
- ✅ Guard does NOT chase zombie
- ✅ Guard returns to STAND position after combat

**Pass Criteria**: Guard remains within 2 blocks of STAND position even during combat

---

### Test 1.3: STAND to Other Mode Switching
**Objective**: Verify guard transitions out of STAND mode correctly

**Steps**:
1. Set guard to STAND mode
2. Wait 30 seconds
3. Change mode to FOLLOW
4. Observe guard behavior

**Expected Results**:
- ✅ GuardStandGoal.shouldContinue() returns false
- ✅ GuardFollowVillagerGoal.canStart() returns true
- ✅ Guard starts following player immediately
- ✅ No lingering navigation cancellation

**Debug Log Validation**:
```
Expected logs:
- "GUARD FOLLOW: canStart() called for guard [UUID]"
- "GUARD FOLLOW: Guard [UUID] mode is FOLLOW"
- "GUARD FOLLOW: Found follow target: [Player Name]"
```

**Pass Criteria**: Guard switches modes within 1 second (20 ticks)

---

## Test Suite 2: FOLLOW Mode

### Test 2.1: Basic FOLLOW Behavior
**Objective**: Verify guard follows nearest player

**Steps**:
1. Set guard mode to FOLLOW
2. Stand 15 blocks away from guard
3. Walk slowly in a circle (20 block radius)
4. Observe guard following behavior

**Expected Results**:
- ✅ Guard follows player within 3-12 block distance
- ✅ Guard path updates every 0.5 seconds (10 ticks)
- ✅ Guard speeds up if distance > 12 blocks
- ✅ Guard stops if distance < 3 blocks

**Debug Log Validation**:
```
Expected logs:
- "GUARD FOLLOW: Found nearest player [Player Name]"
- "GUARD FOLLOW: Guard [UUID] mode is FOLLOW"
- Path recalculation logs every 10 ticks
```

**Pass Criteria**: Guard maintains 3-12 block distance 90% of the time

---

### Test 2.2: FOLLOW Mode - Specific Player Target
**Objective**: Verify guard follows specific player when set

**Steps**:
1. Open Guard Management UI
2. Set specific player as follow target (if implemented)
3. Have another player stand closer to guard
4. Observe which player guard follows

**Expected Results**:
- ✅ Guard prioritizes specified follow target
- ✅ Guard ignores other nearby players
- ✅ Guard falls back to nearest player if target not found

**Debug Log Validation**:
```
Expected logs:
- "GUARD FOLLOW: Found specific player [Target Name] at distance [X]"
```

**Pass Criteria**: Guard follows correct player 100% of the time

---

### Test 2.3: FOLLOW Mode - Villager Fallback
**Objective**: Verify guard follows villagers when no players nearby

**Steps**:
1. Set guard to FOLLOW mode
2. Move player > 20 blocks away (out of range)
3. Place regular villager near guard (within 12 blocks)
4. Observe guard behavior

**Expected Results**:
- ✅ Guard switches to following villager
- ✅ Guard prioritizes villagers in danger (attacked by mobs)
- ✅ Guard does NOT follow other guards

**Debug Log Validation**:
```
Expected logs:
- findFollowTarget() selects villager when no player found
```

**Pass Criteria**: Guard follows villager when no player available

---

### Test 2.4: FOLLOW Mode During Combat
**Objective**: Verify guard stops following to engage in combat

**Steps**:
1. Set guard to FOLLOW mode
2. Guard is actively following player
3. Spawn zombie between guard and player
4. Observe behavior

**Expected Results**:
- ✅ Guard stops following immediately
- ✅ Guard engages zombie in combat
- ✅ Guard resumes following after zombie defeated
- ✅ shouldContinue() returns false when guard.getTarget() != null

**Pass Criteria**: Guard prioritizes combat over following

---

### Test 2.5: FOLLOW Mode - Maximum Distance
**Objective**: Verify guard stops following if player too far away

**Steps**:
1. Set guard to FOLLOW mode
2. Walk slowly away from guard
3. Measure distance when guard stops following
4. Observe behavior at 48+ blocks

**Expected Results**:
- ✅ Guard stops following at ~48 blocks (MAX_DISTANCE * 4)
- ✅ GuardFollowVillagerGoal.shouldContinue() returns false
- ✅ Guard stands idle or switches to patrol

**Pass Criteria**: Guard stops following at expected distance threshold

---

## Test Suite 3: PATROL Mode

### Test 3.1: Basic PATROL Behavior
**Objective**: Verify guard patrols around guard post

**Steps**:
1. Place Guard Post block at coordinates (0, 0, 0)
2. Set villager to Guard profession (workstation should link)
3. Set guard mode to PATROL
4. Observe for 5 minutes

**Expected Results**:
- ✅ Guard identifies Guard Post as patrol center
- ✅ Guard picks random patrol targets within radius (default 16 blocks)
- ✅ Guard moves to target, pauses (cooldown), picks new target
- ✅ Patrol targets are at least 4 blocks apart (MIN_PATROL_DISTANCE)

**Debug Log Validation**:
```
Expected logs:
- findGuardPostWorkstation() finds the Guard Post
- patrolCenter is set to Guard Post position
- findBasicPatrolTarget() returns valid positions
```

**Pass Criteria**: Guard stays within 16 block radius of Guard Post

---

### Test 3.2: PATROL Radius Bounds
**Objective**: Verify guard respects patrol radius limits

**Steps**:
1. Set guard to PATROL mode
2. Mark blocks at 16, 17, 18 block distances from Guard Post
3. Observe patrol targets over 10 minutes
4. Record all positions guard visits

**Expected Results**:
- ✅ All patrol targets within 16 block radius
- ✅ No targets beyond radius boundary
- ✅ Distribution appears random within circle

**Measurement**:
- Use F3 debug screen to track guard coordinates
- Calculate distance from Guard Post
- Log: `distance = sqrt((x - centerX)^2 + (z - centerZ)^2)`

**Pass Criteria**: 100% of patrol targets within configured radius

---

### Test 3.3: PATROL Without Guard Post
**Objective**: Verify guard patrols around current position if no Guard Post found

**Steps**:
1. Create guard without placing Guard Post
2. Set mode to PATROL
3. Note starting position
4. Observe patrol behavior

**Expected Results**:
- ✅ Guard uses current position as patrol center
- ✅ Guard patrols within radius from start position
- ✅ Fallback behavior works correctly

**Debug Log Validation**:
```
Expected logs:
- findGuardPostWorkstation() returns null
- patrolCenter set to guard.getBlockPos()
```

**Pass Criteria**: Guard patrols around starting position when no workstation found

---

### Test 3.4: PATROL Path Recalculation
**Objective**: Verify guard finds new targets correctly

**Steps**:
1. Set guard to PATROL
2. Block guard's current path with walls
3. Observe behavior when navigation fails

**Expected Results**:
- ✅ Guard detects failed navigation
- ✅ Guard picks new patrol target
- ✅ Guard doesn't get stuck in loop
- ✅ Cooldown system prevents spam

**Debug Log Validation**:
```
Expected logs in tick():
- "!guard.getNavigation().isFollowingPath()" detected
- currentTarget reset and new target selected
```

**Pass Criteria**: Guard recovers from blocked paths within 10 seconds

---

### Test 3.5: PATROL Mode During Combat
**Objective**: Verify guard stops patrolling to engage enemies

**Steps**:
1. Set guard to PATROL mode
2. Guard is actively patrolling
3. Spawn zombie within detection range
4. Observe behavior

**Expected Results**:
- ✅ Guard stops patrolling immediately (canStart returns false)
- ✅ Guard engages zombie in combat
- ✅ Guard resumes patrolling after combat
- ✅ Patrol cooldown applies before next patrol

**Pass Criteria**: Combat takes priority over patrol

---

### Test 3.6: PATROL Surface Finding
**Objective**: Verify guard finds valid surface positions for patrol targets

**Steps**:
1. Create varied terrain around Guard Post:
   - Hills
   - Holes/pits
   - Water
   - Walls
2. Set guard to PATROL
3. Observe patrol target selection over 10 minutes

**Expected Results**:
- ✅ Guard finds surface level correctly (findSurface method)
- ✅ Guard avoids invalid positions (solid blocks, water)
- ✅ Guard checks for 2 blocks of clearance above surface
- ✅ Guard navigates terrain successfully

**Pass Criteria**: 90%+ of patrol targets are navigable surface positions

---

## Test Suite 4: Mode Switching

### Test 4.1: Rapid Mode Switching
**Objective**: Verify system handles quick mode changes without issues

**Steps**:
1. Set guard to STAND
2. Wait 5 seconds
3. Change to FOLLOW
4. Wait 5 seconds
5. Change to PATROL
6. Wait 5 seconds
7. Change back to STAND
8. Repeat 5 times

**Expected Results**:
- ✅ No crashes or errors
- ✅ Guard responds to each mode change
- ✅ Previous goal properly stops (shouldContinue returns false)
- ✅ New goal starts correctly (canStart returns true)
- ✅ No "stuck" states or lingering behavior

**Pass Criteria**: Guard switches modes correctly 100% of the time

---

### Test 4.2: Mode Change During Combat
**Objective**: Verify mode switching works even when guard is fighting

**Steps**:
1. Set guard to FOLLOW mode
2. Spawn zombie to engage guard in combat
3. While guard is fighting, change mode to STAND
4. Observe behavior

**Expected Results**:
- ✅ Mode change applies immediately
- ✅ Guard continues combat (combat has higher priority)
- ✅ After combat ends, guard respects new mode (STAND)

**Pass Criteria**: Mode change persists through combat

---

### Test 4.3: Server Restart Persistence
**Objective**: Verify guard mode persists through server restart

**Steps**:
1. Set 3 guards to different modes (STAND, FOLLOW, PATROL)
2. Note their UUIDs and modes
3. Save and close world
4. Reopen world
5. Check guard modes

**Expected Results**:
- ✅ All guards retain their modes
- ✅ GuardData loaded correctly from NBT
- ✅ AI goals reinitialize with correct mode

**Pass Criteria**: 100% mode retention after restart

---

## Test Suite 5: Edge Cases

### Test 5.1: Guard Without GuardData
**Objective**: Verify graceful handling when GuardData is missing

**Steps**:
1. Force a scenario where guard exists but GuardData is missing (debug/testing)
2. Observe behavior

**Expected Results**:
- ✅ canStart() returns false for all mode goals
- ✅ No crashes or null pointer exceptions
- ✅ Guard behaves like regular villager

**Pass Criteria**: No crashes, safe fallback behavior

---

### Test 5.2: Conflicting Goals Priority
**Objective**: Verify goal priority system works correctly

**Steps**:
1. Create situation where multiple goals could run:
   - Guard in FOLLOW mode near player
   - Zombie nearby (combat)
   - Guard at low health (retreat)
2. Observe which goal wins

**Expected Results**:
- ✅ Priority 0: Direct attack (combat)
- ✅ Priority 2: Retreat (low health)
- ✅ Priority 5: Follow (movement)
- ✅ Higher priority goals interrupt lower priority

**Pass Criteria**: Correct priority order maintained

---

### Test 5.3: Pathfinding Failure
**Objective**: Verify system handles impossible navigation targets

**Steps**:
1. Set guard to PATROL
2. Surround guard with walls (no valid patrol targets)
3. Observe behavior over 2 minutes

**Expected Results**:
- ✅ Guard attempts to find valid targets
- ✅ After 10 failed attempts, returns null
- ✅ Cooldown system prevents constant retries
- ✅ No infinite loops or freezing

**Pass Criteria**: Guard handles failure gracefully, no crashes

---

## Test Execution Checklist

### Pre-Test:
- [ ] Build project: `./gradlew build`
- [ ] Launch client: `./gradlew runClient`
- [ ] Create test world (Creative, Flat)
- [ ] Prepare test area with markers
- [ ] Have console/logs visible for debugging

### During Test:
- [ ] Record observations for each test case
- [ ] Note actual behavior vs expected behavior
- [ ] Capture screenshots of issues
- [ ] Save debug logs for failing tests

### Post-Test:
- [ ] Summarize results (pass/fail for each test)
- [ ] Document any bugs found
- [ ] Note unexpected behavior
- [ ] Create follow-up tasks for issues

---

## Bug Reporting Template

If issues are found, use this template:

```markdown
### Bug: [Short Description]

**Test Case**: [e.g., Test 2.1: Basic FOLLOW Behavior]
**Severity**: [Critical / High / Medium / Low]
**Reproducibility**: [Always / Sometimes / Rare]

**Steps to Reproduce**:
1.
2.
3.

**Expected Behavior**:
-

**Actual Behavior**:
-

**Debug Logs**:
```
[paste relevant logs]
```

**Screenshots**:
[attach if applicable]

**Notes**:
[additional context]
```

---

## Success Criteria Summary

For P4-TASK-001 to be considered COMPLETE:

1. **STAND Mode**: 100% pass rate on Tests 1.1-1.3
2. **FOLLOW Mode**: 100% pass rate on Tests 2.1-2.5
3. **PATROL Mode**: 100% pass rate on Tests 3.1-3.6
4. **Mode Switching**: 100% pass rate on Tests 4.1-4.3
5. **Edge Cases**: 100% pass rate on Tests 5.1-5.3

**Overall**: At least 95% of all test cases must pass. Any critical bugs must be documented and fixed before moving to P4-TASK-002.

---

## Time Estimates

- Test Suite 1 (STAND): 20 minutes
- Test Suite 2 (FOLLOW): 30 minutes
- Test Suite 3 (PATROL): 45 minutes
- Test Suite 4 (Mode Switching): 20 minutes
- Test Suite 5 (Edge Cases): 15 minutes
- Bug documentation and summary: 20 minutes

**Total**: ~2.5 hours

---

## Next Steps After Testing

1. Complete test execution and document results
2. If bugs found: Create separate tasks for fixes
3. Update P4-TASK-001 status to COMPLETED
4. Proceed to P4-TASK-002: Test Patrol Radius System
