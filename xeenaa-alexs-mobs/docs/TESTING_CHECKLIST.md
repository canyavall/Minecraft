# Testing Checklist

**Xeenaa's Alex's Mobs (Fabric) - Manual Testing Guide**

This document provides comprehensive manual testing procedures for the framework established in Epic 01. The user will perform these tests to validate the framework before proceeding to performance testing (TASK-012).

---

## Table of Contents

1. [Pre-Test Setup](#pre-test-setup)
2. [Test Cases](#test-cases)
3. [Test Results Template](#test-results-template)
4. [How to Interpret Results](#how-to-interpret-results)
5. [What to Do If Tests Fail](#what-to-do-if-tests-fail)
6. [Performance Testing Guidelines](#performance-testing-guidelines)

---

## Pre-Test Setup

### Prerequisites

Before running tests, ensure:

1. **Project builds successfully**
   ```bash
   cd C:/Users/canya/Documents/projects/Minecraft/xeenaa-alexs-mobs
   ./gradlew clean build
   ```
   - Expected: `BUILD SUCCESSFUL` message
   - No compilation errors or warnings

2. **Dependencies resolved**
   - GeckoLib 5.0+ included
   - Fabric API available
   - All dependencies downloaded

3. **Clean logs directory**
   ```bash
   rm logs/latest.log  # Clear old logs for fresh test run
   ```

4. **Test environment**
   - Minecraft 1.21.1 installed
   - Java 21 runtime available
   - Sufficient disk space (2+ GB)
   - Sufficient RAM (4+ GB allocated to Minecraft)

### Launch Configuration

Run the client:
```bash
./gradlew runClient
```

**Expected Launch Time**: 30-120 seconds (depends on hardware)

**Success Indicators**:
- Minecraft main menu appears
- No crash during startup
- Mod appears in mod list (Mods menu)

### Test World Setup

1. **Create new test world**:
   - World Type: Default
   - Game Mode: Creative
   - Difficulty: Peaceful (for initial tests)
   - Cheats: Enabled

2. **Load world**:
   - Wait for world generation to complete
   - Verify no crashes during world load

3. **Open console**:
   - Press F3 to show debug screen
   - Monitor TPS (should be 20.0)
   - Monitor FPS (should be smooth, 60+)

---

## Test Cases

### Test 1: Build Success

**Objective**: Verify project compiles without errors

**Steps**:
1. Run `./gradlew clean build`
2. Wait for build to complete
3. Check console output

**Expected Results**:
- ✅ Build completes successfully
- ✅ No compilation errors
- ✅ No warnings in build output
- ✅ JAR file created in `build/libs/`

**Pass Criteria**: All expected results met

**Log Location**: Build output in terminal

---

### Test 2: Mod Loading

**Objective**: Verify mod loads during game startup

**Steps**:
1. Run `./gradlew runClient`
2. Wait for game to start
3. Open "Mods" menu from main menu
4. Search for "Xeenaa's Alex's Mobs"

**Expected Results**:
- ✅ Game launches without crashes
- ✅ Mod appears in mod list
- ✅ Mod version shows correctly
- ✅ Mod description visible

**Pass Criteria**: All expected results met

**Log Location**: `logs/latest.log` (check for mod initialization messages)

**Log Keywords to Search**:
```
Initializing Alex's Mobs
Registering sound events
```

---

### Test 3: Entity Registration

**Objective**: Verify test entity is registered and summonable

**Steps**:
1. Create/load test world (Creative mode)
2. Open chat (T key)
3. Type `/summon xeenaa-alexs-mobs:`
4. Check autocomplete suggestions
5. Type `/summon xeenaa-alexs-mobs:test_animal`
6. Execute command

**Expected Results**:
- ✅ `test_animal` appears in autocomplete
- ✅ Command executes without error
- ✅ Entity spawns at player location
- ✅ Entity is visible (not invisible)
- ✅ Console shows no errors

**Pass Criteria**: All expected results met

**Failure Indicators**:
- ❌ "Unknown entity" error
- ❌ Entity spawns but is invisible
- ❌ Command fails with error

**Log Location**: `logs/latest.log`

---

### Test 4: Entity Rendering

**Objective**: Verify entity renders with correct model and texture

**Steps**:
1. Summon test_animal entity
2. Walk around entity to view from all angles
3. Press F3+B to show hitbox
4. Observe entity appearance

**Expected Results**:
- ✅ Entity has visible model (cube or custom geometry)
- ✅ Entity has texture applied (green color, no pink/black checkerboard)
- ✅ Entity renders from all camera angles
- ✅ Shadow renders beneath entity
- ✅ Hitbox shows correct dimensions (F3+B)
- ✅ No Z-fighting or rendering glitches

**Pass Criteria**: All expected results met

**Failure Indicators**:
- ❌ Entity is invisible
- ❌ Pink/black checkerboard (missing texture)
- ❌ No shadow
- ❌ Flickering or glitching

**Log Location**: `logs/latest.log` (search for "texture" or "renderer")

---

### Test 5: Entity Animations

**Objective**: Verify GeckoLib animations play correctly

**Steps**:
1. Summon test_animal entity
2. Stand still and observe entity (idle animation)
3. Wait 5 seconds
4. Walk away from entity and observe (walk animation)
5. Return and observe entity become idle again

**Expected Results**:
- ✅ Idle animation plays when entity is stationary
- ✅ Walk animation plays when entity is moving
- ✅ Animation transitions are smooth
- ✅ No T-pose or frozen animations
- ✅ Animation loops correctly (doesn't stop abruptly)

**Pass Criteria**: At least idle and walk animations work

**Known Limitation**:
- Animations are placeholders (simple cube model)
- Full animation quality will be tested with real animal models

**Failure Indicators**:
- ❌ Entity stuck in T-pose
- ❌ No animation plays
- ❌ Animation stutters or freezes

**Log Location**: `logs/latest.log` (search for "animation" or "GeckoLib")

---

### Test 6: Entity AI Behavior

**Objective**: Verify entity has correct AI goals from BaseAnimalEntity

**Steps**:
1. Summon test_animal entity in open area
2. Switch to Survival mode (`/gamemode survival`)
3. Step back and observe for 30 seconds
4. Place water nearby and observe entity
5. Attack entity and observe response

**Expected Results**:
- ✅ Entity wanders around (WanderAroundFarGoal)
- ✅ Entity looks at player when nearby (LookAtEntityGoal)
- ✅ Entity looks around randomly (LookAroundGoal)
- ✅ Entity swims in water (SwimGoal)
- ✅ Entity flees when damaged (EscapeDangerGoal)

**Pass Criteria**: At least wandering and looking behaviors work

**Failure Indicators**:
- ❌ Entity stands completely still
- ❌ Entity doesn't react to player
- ❌ Entity drowns in water (not swimming)

**Log Location**: In-game observation (no specific logs)

---

### Test 7: Spawn Egg

**Objective**: Verify spawn egg works in creative inventory

**Steps**:
1. Open creative inventory (E key)
2. Search for "test animal"
3. Find "Test Animal Spawn Egg"
4. Observe egg colors
5. Take spawn egg into inventory
6. Right-click on ground to use spawn egg

**Expected Results**:
- ✅ Spawn egg appears in creative inventory
- ✅ Spawn egg has correct name ("Test Animal Spawn Egg")
- ✅ Spawn egg has green colors (primary: 0x76AF50, secondary: 0x4B6B32)
- ✅ Spawn egg is in "Xeenaa's Alex's Mobs" creative tab
- ✅ Right-clicking spawn egg spawns entity
- ✅ Spawned entity is same as summoned entity

**Pass Criteria**: All expected results met

**Failure Indicators**:
- ❌ Spawn egg missing from creative inventory
- ❌ Wrong colors (default white egg)
- ❌ Spawn egg doesn't spawn entity

**Log Location**: `logs/latest.log` (search for "spawn_egg")

---

### Test 8: Entity Hitbox

**Objective**: Verify entity has correct hitbox dimensions

**Steps**:
1. Summon test_animal entity
2. Press F3+B to show hitboxes
3. Observe entity hitbox outline
4. Compare to entity visual size

**Expected Results**:
- ✅ Hitbox is visible (white outline)
- ✅ Hitbox dimensions match entity registration (0.6 × 0.8 blocks)
- ✅ Hitbox follows entity movement
- ✅ Hitbox doesn't clip through blocks

**Pass Criteria**: Hitbox visible and correct size

**Reference**: ModEntities.java defines dimensions as `EntityDimensions.fixed(0.6f, 0.8f)`

**Failure Indicators**:
- ❌ No hitbox visible
- ❌ Hitbox wrong size (too large/small)

**Log Location**: In-game observation

---

### Test 9: Sound Events

**Objective**: Verify sound events attempt to play (even without .ogg files)

**Steps**:
1. Summon test_animal entity
2. Wait for ambient sound (may take 30-60 seconds)
3. Attack entity to trigger hurt sound
4. Kill entity to trigger death sound
5. Check console logs

**Expected Results**:
- ✅ No crashes when sounds attempt to play
- ✅ Console shows "missing sound" warnings (expected - no .ogg files)
- ✅ Game continues normally despite missing sounds
- ✅ Sound events registered (check logs)

**Pass Criteria**: Sound events registered, no crashes

**Known Limitation**:
- Actual .ogg sound files NOT implemented
- Missing sound warnings are EXPECTED
- Sounds will be silent (no audio)

**Expected Warnings**:
```
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.ambient
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.hurt
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.death
```

**Failure Indicators**:
- ❌ Game crashes when sound attempts to play
- ❌ Sound events not registered

**Log Location**: `logs/latest.log` (search for "sound" or "Missing sound")

---

### Test 10: Entity Loot Table

**Objective**: Verify entity drops loot when killed

**Steps**:
1. Summon test_animal entity
2. Kill entity (attack until health reaches zero)
3. Observe drops on ground
4. Pick up drops
5. Check inventory

**Expected Results**:
- ✅ Entity dies when health reaches zero
- ✅ Entity plays death animation (if implemented)
- ✅ Loot drops on ground (leather)
- ✅ Loot amount: 1-2 leather (random)
- ✅ Loot can be picked up

**Pass Criteria**: Entity drops 1-2 leather

**Looting Enchantment Test** (Optional):
1. Use sword with Looting III
2. Kill entity
3. Verify increased drops (up to +1 per looting level)

**Failure Indicators**:
- ❌ No loot drops
- ❌ Wrong loot type
- ❌ Loot cannot be picked up

**Log Location**: In-game observation

---

### Test 11: Resource Reload (F3+T)

**Objective**: Verify resources reload without crashes

**Steps**:
1. Summon test_animal entity
2. Press F3+T to reload resources
3. Wait for reload to complete
4. Observe entity after reload
5. Check console logs

**Expected Results**:
- ✅ Resource reload completes without crash
- ✅ Entity still renders correctly after reload
- ✅ Textures reload correctly
- ✅ Animations still work
- ✅ No errors in console

**Pass Criteria**: Reload succeeds, entity still works

**Failure Indicators**:
- ❌ Game crashes during reload
- ❌ Entity becomes invisible after reload
- ❌ Errors in console

**Log Location**: `logs/latest.log` (search for "Reloading" or "resource")

---

### Test 12: Console Error Check

**Objective**: Verify no critical errors in console

**Steps**:
1. After running all tests, close game
2. Open `logs/latest.log`
3. Search for error keywords
4. Review any errors found

**Expected Results**:
- ✅ No CRITICAL or ERROR level messages
- ✅ Expected WARN messages only (missing sounds)
- ✅ No stack traces or exceptions
- ✅ Mod initialization messages present

**Pass Criteria**: No unexpected errors

**Expected Warnings** (these are OK):
```
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.ambient
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.hurt
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.death
```

**Failure Indicators**:
- ❌ [ERROR] messages
- ❌ Stack traces (exception dumps)
- ❌ "Failed to register" errors

**Search Commands**:
```bash
# Search for errors
grep -i "error" logs/latest.log

# Search for exceptions
grep -i "exception" logs/latest.log

# Search for critical issues
grep -i "critical" logs/latest.log
```

**Log Location**: `logs/latest.log`

---

### Test 13: Multiplayer Compatibility (Optional)

**Objective**: Verify entity works on dedicated server

**Note**: This test is OPTIONAL for framework validation. Required for full release.

**Steps**:
1. Run `./gradlew runServer`
2. Wait for server to start
3. Connect from Minecraft client
4. Summon test_animal entity
5. Verify entity renders for connected player
6. Check server console for errors

**Expected Results**:
- ✅ Server starts without errors
- ✅ Entity spawns on server
- ✅ Entity visible to all connected players
- ✅ Animations sync across players
- ✅ No server-side errors

**Pass Criteria**: Entity works in multiplayer

**Known Limitations**:
- Full multiplayer testing in later epics
- Basic synchronization tested here

**Failure Indicators**:
- ❌ Server crashes
- ❌ Entity invisible to players
- ❌ Animations desynchronized

**Log Location**: Server console and `logs/latest.log`

---

## Test Results Template

Use this template to record test results:

```
=== Epic 01 Framework Testing Results ===
Date: [YYYY-MM-DD]
Tester: [Your Name]
Environment: Windows/Linux/Mac, Java 21, Minecraft 1.21.1

Test 1: Build Success
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 2: Mod Loading
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 3: Entity Registration
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 4: Entity Rendering
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 5: Entity Animations
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 6: Entity AI Behavior
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 7: Spawn Egg
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 8: Entity Hitbox
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 9: Sound Events
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 10: Entity Loot Table
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 11: Resource Reload (F3+T)
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 12: Console Error Check
  Status: [ ] PASS  [ ] FAIL
  Notes:

Test 13: Multiplayer Compatibility (Optional)
  Status: [ ] PASS  [ ] FAIL  [ ] SKIPPED
  Notes:

=== Overall Results ===
Total Tests: 13 (12 required, 1 optional)
Passed: [X/12]
Failed: [X/12]
Skipped: [X/1]

Framework Ready: [ ] YES  [ ] NO

Additional Notes:


Logs Attached: [ ] YES  [ ] NO
```

---

## How to Interpret Results

### All Tests Pass

**Interpretation**: Framework is working correctly ✅

**Next Steps**:
1. Mark TASK-011 as COMPLETED
2. Proceed to TASK-012 (Performance Validation)
3. Prepare for Epic 02 (Mob Catalog & Asset Inventory)

### Minor Issues (1-2 tests fail, non-critical)

**Interpretation**: Framework mostly working, minor fixes needed ⚠️

**Examples of Minor Issues**:
- Spawn egg has wrong colors (cosmetic)
- Loot table drops wrong amount (configuration)
- Missing sound warnings (expected)

**Next Steps**:
1. Document issues in TASK-011 notes
2. Create fix tasks if needed
3. Proceed to TASK-012 if core functionality works

### Major Issues (3+ tests fail, core functionality broken)

**Interpretation**: Framework has critical problems ❌

**Examples of Major Issues**:
- Entity won't summon (registration broken)
- Entity is invisible (rendering broken)
- Game crashes (critical bug)

**Next Steps**:
1. DO NOT proceed to TASK-012
2. Create bug fix sub-tasks
3. Debug and fix issues
4. Re-run tests

### Critical Failure (Game won't launch)

**Interpretation**: Fundamental problem, immediate fix required 🚨

**Next Steps**:
1. Check build logs for compilation errors
2. Check `logs/latest.log` for crash reports
3. Verify dependencies (GeckoLib, Fabric API)
4. Consult FRAMEWORK_USAGE.md troubleshooting section

---

## What to Do If Tests Fail

### Step 1: Identify the Failure

1. Note which test failed
2. Record exact error message or symptom
3. Check console logs for errors
4. Take screenshots if visual issue

### Step 2: Check Common Causes

Refer to **FRAMEWORK_USAGE.md - Troubleshooting** section for:
- Entity won't summon
- Entity is invisible
- Missing texture
- Animations don't play
- Sounds don't play
- AI doesn't work
- Spawn egg doesn't work
- Build fails
- Loot doesn't drop

### Step 3: Debug Steps

1. **Check logs first**: `logs/latest.log` contains most error information
2. **Verify files exist**: Ensure all resource files are present
3. **Check registration**: Verify entity, sounds, items registered
4. **Compare with working code**: Reference TestAnimalEntity

### Step 4: Fix and Re-Test

1. Make fix based on debugging
2. Rebuild project: `./gradlew clean build`
3. Re-run failed test
4. If still failing, try next debugging approach

### Step 5: Document Issue

If you cannot fix:
1. Document exact steps to reproduce
2. Include error messages and logs
3. Note what debugging steps you tried
4. Create bug report for future fix

---

## Performance Testing Guidelines

**Note**: Performance testing is TASK-012. This section provides overview only.

### Performance Metrics to Track

After manual testing passes, TASK-012 will measure:

1. **Entity Tick Time**:
   - Target: < 0.05ms per entity
   - Tool: Spark profiler or F3 debug

2. **Rendering Performance**:
   - Target: < 0.1ms render time per entity
   - Tool: FPS counter, F3 debug

3. **Memory Usage**:
   - Target: No memory leaks
   - Tool: F3 memory graph, JVisualVM

4. **TPS Stability**:
   - Target: 20 TPS with 50+ entities
   - Tool: F3 debug screen

5. **Startup Time**:
   - Target: < 2 seconds added to game launch
   - Tool: Log timestamps

### Performance Test Scenarios

TASK-012 will include:
1. Spawn 50 test entities
2. Measure TPS over 5 minutes
3. Measure FPS with entities on-screen
4. Profile entity tick time
5. Monitor memory for leaks
6. Test F3+T reload performance

### Expected Performance

Based on requirements.md:
- Entity tick time: < 0.05ms per entity ✅
- Render time: < 0.1ms per entity ✅
- Memory: Efficient model caching (no leaks) ✅
- TPS: 20.0 stable with 50+ entities ✅
- FPS: 60+ on mid-range hardware ✅

---

## Known Issues and Limitations

### Expected Warnings

These are NOT failures:
- Missing sound warnings (no .ogg files yet)
- "Placeholder" texture notices (test entity only)

### Test Entity Limitations

- Simple cube model (real animals will have detailed models)
- Placeholder animations (simple idle/walk)
- No breeding items (breeding not fully implemented)
- Silent (sound files not added)

### Framework Status

**Implemented** (Epic 01):
- ✅ Entity registration system
- ✅ GeckoLib integration
- ✅ Client rendering
- ✅ Base animal entity
- ✅ Sound event registration
- ✅ Spawn egg system
- ✅ Loot table system

**Not Implemented** (Future Epics):
- ❌ Actual animal mob implementations
- ❌ Biome-based spawning
- ❌ Config system
- ❌ Item system (drops, equipment)
- ❌ Animal Dictionary (in-game guide)

---

## Summary Checklist

Before marking TASK-011 as complete, verify:

- [ ] All 12 required tests passed
- [ ] Test results documented
- [ ] Logs reviewed for errors
- [ ] Known issues documented
- [ ] Framework ready for performance testing (TASK-012)

**If all checkboxes marked**: Framework validation COMPLETE ✅

**If any checkboxes unmarked**: Review failures and fix issues

---

**Testing Version**: Epic 01 - TASK-011
**Last Updated**: 2025-10-26
**Framework Version**: 0.1.0
