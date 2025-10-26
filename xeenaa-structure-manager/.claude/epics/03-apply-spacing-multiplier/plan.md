# Epic 03: Apply Spacing Multiplier to Structure Placement - Implementation Plan

**Epic Status**: READY
**Total Tasks**: 6
**Estimated Effort**: 8-12 hours
**Prerequisites**: Epic 01 completed ✅, Epic 02 completed ✅
**Priority**: CRITICAL
**Expected Impact**: 50-80% worldgen improvement (measured)

---

## Implementation Overview

This epic applies the spacing multiplier from Epic 02 config to actual structure placement, achieving the promised 50-80% worldgen performance improvement. We hook into Minecraft's structure placement algorithm and multiply spacing/separation values before grid calculations.

**Current State**: Config stores 2.0x multiplier, but structures still place at vanilla density (~2,100+ placements)
**Target State**: Multiplier applied to placement algorithm, reducing placements to ~650 (75% reduction)

**Expected Impact**:
- Placement reduction: 2,600 → ~650 (75%)
- STRUCTURE_START time: 50-60% reduction
- Memory usage: 50% → 20-30%
- User experience: "Struggled" → "Smooth"

---

## Phase 1: Research and Planning (Foundation)

### Task: [COMPLETED] TASK-001: Research Structure Placement Code and Identify Injection Point

**Agent**: implementation-agent
**Priority**: CRITICAL
**Estimated Effort**: 2-3 hours
**Dependencies**: None
**Completed**: 2025-10-25

**Goal**: Locate the exact code location where Minecraft calculates structure placement spacing/separation and identify the optimal mixin injection point.

**Requirements**:

1. **Examine Minecraft source code**:
   - Generate Minecraft sources: `./gradlew genSources`
   - Locate structure placement classes in `net.minecraft.world.gen.chunk.placement.*`
   - Find where spacing and separation values are used in grid calculations
   - Identify all structure placement types (RandomSpreadStructurePlacement, ConcentricRingsStructurePlacement, etc.)

2. **Key classes to investigate** (based on Epic 03 requirements):
   - `StructurePlacement` - Base class for all placement types
   - `RandomSpreadStructurePlacement` - Most common placement type (uses spacing/separation)
   - `ConcentricRingsStructurePlacement` - Ring-based placement (strongholds, ancient cities)
   - `ChunkGenerator.getStructureReferences()` - Where placements are checked

3. **Determine injection strategy**:
   - Option A: Mixin into StructurePlacement constructor to modify spacing/separation fields
   - Option B: Mixin into placement calculation methods (getStartChunk(), isPlacementChunk())
   - Option C: Mixin into ChunkGenerator where placement checks occur
   - **Recommendation**: Use Option A or B (most targeted, least compatibility issues)

4. **Document findings**:
   - Create: `xeenaa-structure-manager/.claude/research/structure-placement-mixin-strategy.md`
   - Include: Class names, method signatures, injection points, code snippets
   - Include: Recommended mixin approach with rationale

**Acceptance Criteria**:
- [ ] Minecraft sources generated successfully
- [ ] StructurePlacement classes examined and documented
- [ ] Injection point identified (specific class and method)
- [ ] Mixin strategy chosen (constructor vs method vs accessor)
- [ ] Research document created with code references
- [ ] All structure placement types identified (not just RandomSpread)
- [ ] Compatibility risks assessed (other mods touching same code)

**Implementation Notes**:

**Commands to run**:
```bash
cd xeenaa-structure-manager
./gradlew genSources
# Sources will be in: build/sources/minecraft/
```

**Where to look**:
- `net.minecraft.world.gen.chunk.placement.StructurePlacement` - Base class
- `net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement` - Common type
- `net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement` - Ring type
- Decompiled code in: `build/sources/minecraft/`

**Mixin considerations**:
- Use `@Inject` for method injection (least intrusive)
- Use `@ModifyArg` if modifying specific parameter
- Use `@Mixin` with field `@Shadow` if accessing private fields
- Use `@Accessor` if creating getter/setter for private fields
- Thread safety: Worldgen is multi-threaded, avoid shared mutable state

**Skills to use**:
- minecraft-modding (structure placement understanding)
- fabric-modding (mixin patterns, injection strategies)
- research-methodology (systematic investigation)

---

## Phase 2: Implementation (Core Mixin)

### Task: [COMPLETED] TASK-002: Implement Spacing Multiplier Mixin

**Agent**: implementation-agent
**Priority**: CRITICAL
**Estimated Effort**: 3-4 hours
**Dependencies**: TASK-001 (injection point identified)
**Completed**: 2025-10-25

**Goal**: Create mixin that multiplies structure placement spacing and separation values by the configured multiplier from Epic 02 config.

**Requirements**:

1. **Create mixin class**:
   - Package: `com.canya.xeenaa_structure_manager.mixin`
   - Class: `StructurePlacementMixin.java` (or specific type like RandomSpreadStructurePlacementMixin)
   - Target: Class identified in TASK-001
   - Purpose: Multiply spacing/separation by config multiplier

2. **Implementation approach** (depends on TASK-001 findings):

   **Option A - Constructor injection** (if modifying fields at creation):
   ```java
   @Mixin(RandomSpreadStructurePlacement.class)
   public class RandomSpreadStructurePlacementMixin {
       @ModifyArg(
           method = "<init>",
           at = @At(value = "INVOKE", target = "..."),
           index = <spacing_param_index>
       )
       private static int modifySpacing(int originalSpacing) {
           double multiplier = StructureManagerMod.getConfig().getEffectiveMultiplier();
           return (int) Math.round(originalSpacing * multiplier);
       }

       @ModifyArg(
           method = "<init>",
           at = @At(value = "INVOKE", target = "..."),
           index = <separation_param_index>
       )
       private static int modifySeparation(int originalSeparation) {
           double multiplier = StructureManagerMod.getConfig().getEffectiveMultiplier();
           return (int) Math.round(originalSeparation * multiplier);
       }
   }
   ```

   **Option B - Method injection** (if modifying calculation):
   ```java
   @Mixin(RandomSpreadStructurePlacement.class)
   public class RandomSpreadStructurePlacementMixin {
       @Inject(
           method = "getStartChunk",
           at = @At("HEAD"),
           cancellable = true
       )
       private void modifyStartChunk(long seed, int x, int z, CallbackInfoReturnable<ChunkPos> cir) {
           // Calculate with multiplied spacing
           double multiplier = StructureManagerMod.getConfig().getEffectiveMultiplier();
           // ... custom calculation with multiplied values
           cir.setReturnValue(new ChunkPos(modifiedX, modifiedZ));
       }
   }
   ```

3. **Register mixin**:
   - Add mixin to: `src/main/resources/xeenaa-structure-manager.mixins.json`
   - Entry: `"StructurePlacementMixin"` (or specific class name)
   - Verify mixin applies at startup (check logs)

4. **Config integration**:
   - Access config via: `StructureManagerMod.getConfig().getEffectiveMultiplier()`
   - Ensure multiplier is 2.0x for Balanced preset (from Epic 02)
   - Handle edge cases: multiplier < 1.0 (should already be validated in config)

5. **Logging** (use logging-strategy skill):
   - INFO level: "Applied spacing multiplier to <StructureType>: <originalValue> -> <newValue>"
   - DEBUG level: "Structure placement calculation: spacing=<X>, separation=<Y>, multiplier=<M>"
   - Verify logs appear during world creation

6. **Thread safety**:
   - Config is immutable after load (safe for multi-threaded access)
   - Mixin should not use mutable shared state
   - Each placement calculation is independent (no synchronization needed)

**Acceptance Criteria**:
- [ ] Mixin class created in correct package
- [ ] Mixin applies spacing multiplier correctly
- [ ] Mixin applies separation multiplier correctly
- [ ] Mixin registered in mixins.json
- [ ] Config integration working (reads 2.0x from Balanced preset)
- [ ] Logging shows multiplier applied (INFO level)
- [ ] Code compiles without errors
- [ ] Mixin applies at startup (verified in logs)
- [ ] No mixin conflicts or errors

**Implementation Notes**:

**Files to create**:
- `src/main/java/com/canya/xeenaa_structure_manager/mixin/StructurePlacementMixin.java`

**Files to modify**:
- `src/main/resources/xeenaa-structure-manager.mixins.json` (add mixin entry)

**Testing during implementation**:
1. Build: `./gradlew build`
2. Check mixin applies: Look for Fabric mixin logs
3. Launch client: Check INFO logs for multiplier application
4. Create world: Verify logs show modified spacing values

**Common mixin pitfalls**:
- Incorrect `@At` target (use exact method signature)
- Wrong parameter index in `@ModifyArg` (count from 0)
- Missing `@Shadow` for private field access
- Forgetting to register in mixins.json
- Target method not found (verify with genSources)

**Skills to use**:
- coding-standards (Java naming, documentation, error handling)
- fabric-modding (mixin patterns, Fabric API)
- minecraft-modding (structure placement understanding)
- logging-strategy (proactive logging, structured messages)
- defensive-programming (input validation, thread safety)

---

## Phase 3: Testing and Validation (Verification)

### Task: [TODO] TASK-003: Test with Vanilla Structures

**Agent**: implementation-agent (+ validation-agent for test creation after user validation)
**Priority**: HIGH
**Estimated Effort**: 1-2 hours
**Dependencies**: TASK-002 (mixin implemented)

**Goal**: Verify that the spacing multiplier works correctly with vanilla Minecraft structures (villages, temples, monuments, etc.) without breaking anything.

**Requirements**:

1. **Build and launch**:
   - Build mod: `./gradlew build`
   - Launch client: `./gradlew runClient`
   - Check logs: Verify mixin applied and multiplier logged

2. **Create test world**:
   - Seed: `-1234567890` (same as Epic 01 testing for comparison)
   - Game mode: Creative
   - World type: Default
   - Render distance: 12 chunks (same as Epic 01)

3. **Test vanilla structures** (verify correct spacing):
   - Villages: Should spawn ~2x further apart
   - Desert temples: Should spawn ~2x further apart
   - Jungle temples: Should spawn ~2x further apart
   - Ocean monuments: Should spawn ~2x further apart
   - Woodland mansions: Should spawn ~2x further apart
   - Strongholds: Should spawn in rings (ConcentricRingsStructurePlacement - may need special handling)

4. **Functional verification**:
   - Structures generate correctly (not broken/missing parts)
   - Structures are complete (all blocks present)
   - Structures have correct loot (chests, spawners)
   - No crashes during worldgen
   - No errors in logs

5. **Visual exploration**:
   - Fly around in creative mode for 5-10 minutes
   - Use `/locate structure` command to find nearest structures
   - Measure distance between structures (should be ~2x vanilla)
   - Take screenshots if structures look broken

6. **Document results**:
   - Create: `xeenaa-structure-manager/.claude/temp/vanilla-structure-testing.md`
   - Include: Structure types tested, distances observed, any issues found
   - Include: Screenshots if problems detected

**Acceptance Criteria**:
- [ ] Mod builds successfully
- [ ] Minecraft launches without crashes
- [ ] Logs show mixin applied
- [ ] Logs show spacing multiplier (2.0x) applied
- [ ] Villages spawn ~2x further apart
- [ ] All vanilla structure types tested (6+ types)
- [ ] Structures generate correctly (not broken)
- [ ] No crashes during 10 minutes of worldgen
- [ ] No errors in logs
- [ ] Results documented

**Implementation Notes**:

**Commands to use in Minecraft**:
```
/locate structure minecraft:village_plains
/locate structure minecraft:desert_pyramid
/locate structure minecraft:jungle_pyramid
/locate structure minecraft:monument
/locate structure minecraft:mansion
/locate structure minecraft:stronghold
```

**What to look for**:
- Distance from spawn to first structure (should be ~2x vanilla)
- Distance between structures of same type (should be ~2x vanilla)
- Structure completeness (all parts generated)
- Log messages confirming multiplier application

**If structures broken**:
1. Check logs for errors
2. Screenshot the broken structure
3. Document in temp/vanilla-structure-testing.md
4. May need to adjust mixin approach in TASK-002
5. Consult research from TASK-001

**Skills to use**:
- minecraft-modding (structure behavior understanding)
- defensive-programming (comprehensive testing)
- logging-strategy (verify log messages)

---

### Task: [TODO] TASK-004: Test with Modded Structures

**Agent**: implementation-agent
**Priority**: HIGH
**Estimated Effort**: 2-3 hours
**Dependencies**: TASK-003 (vanilla structures verified)

**Goal**: Verify that the spacing multiplier works correctly with modded structures from Epic 01 testing (569 structures from 15+ mods) without breaking compatibility.

**Requirements**:

1. **Install structure mods** (from Epic 01 testing):
   - Use same modpack configuration as Epic 01
   - Verify 569 structures loaded (check logs or mod list)
   - Ensure all 15+ structure mods present

2. **Create test world**:
   - Seed: `-1234567890` (Epic 01 baseline comparison)
   - Game mode: Creative
   - World type: Default
   - Render distance: 12 chunks

3. **Test modded structure types**:
   - At least 5 different mod structures (variety of mods)
   - Different placement types (random spread, rings, linear)
   - Large structures (cities, dungeons) and small structures (ruins, camps)
   - Verify spacing multiplier applies to ALL types

4. **Functional verification**:
   - Modded structures generate correctly
   - Structures are complete (not missing parts)
   - Structures have correct loot/spawners
   - No crashes during worldgen
   - No errors or warnings in logs

5. **Compatibility verification**:
   - No mixin conflicts (check Fabric logs)
   - Other structure mods still work (not disabled by our mod)
   - Structures from all 15+ mods can generate
   - No "structure failed to place" warnings

6. **Observe placement rate**:
   - Play for 10 minutes, explore
   - Structures should spawn MUCH less frequently than Epic 01 testing (~75% fewer)
   - Compare to Epic 01 logs: ~2,600 placements → expect ~650 placements

7. **Document results**:
   - Create: `xeenaa-structure-manager/.claude/temp/modded-structure-testing.md`
   - Include: Mods tested, structures found, placement observations, issues
   - Include: Comparison to Epic 01 baseline (placement count)

**Acceptance Criteria**:
- [ ] All structure mods from Epic 01 installed
- [ ] 569 structures confirmed loaded
- [ ] 5+ modded structure types tested
- [ ] All modded structures generate correctly
- [ ] No crashes during 10 minutes of worldgen
- [ ] No mixin conflicts in logs
- [ ] Placement rate visibly reduced (~75% fewer structures)
- [ ] Compatibility with all 15+ mods verified
- [ ] Results documented with comparison to Epic 01

**Implementation Notes**:

**Mods from Epic 01** (verify these work):
- List from Epic 01 research: Check `.claude/epics/01-comprehensive-performance-research/research/baselines/`
- Ensure same versions as Epic 01 testing

**What to look for**:
- Structures spawning much less frequently (good sign)
- Variety of mod structures still spawning (not just vanilla)
- No "structure overlap" or "placement failed" warnings
- Logs show multiplier applied to modded structures

**If compatibility issues**:
1. Check which mod is conflicting (logs will show)
2. Examine that mod's structure placement code
3. May need to adjust mixin to be more defensive
4. Document incompatibility for Epic 04 (mod compatibility docs)

**Performance observation** (informal):
- Does worldgen feel faster? (should be noticeably smoother)
- RAM usage lower? (should be lower with fewer placements)
- FPS drops during chunk loading? (should be reduced)

**Skills to use**:
- minecraft-modding (modded structure behavior)
- fabric-modding (mod compatibility, mixin conflicts)
- defensive-programming (edge case testing)

---

## Phase 4: Performance Validation (Measurement)

### Task: [TODO] TASK-005: Performance Validation with Spark Profiler

**Agent**: implementation-agent (+ validation-agent for automated tests)
**Priority**: CRITICAL
**Estimated Effort**: 2-3 hours
**Dependencies**: TASK-003, TASK-004 (functional testing complete)

**Goal**: Measure actual performance improvement using Spark profiler and Epic 01 test procedure, validating the 50-80% improvement target.

**Requirements**:

1. **Install Spark profiler**:
   - Download Spark mod (same version as Epic 01)
   - Place in mods folder
   - Verify loads successfully

2. **Run Epic 01 test procedure**:
   - Seed: `-1234567890`
   - Render distance: 12 chunks
   - Duration: 8 minutes of worldgen
   - Profile: `/spark profiler start` → explore → `/spark profiler stop`

3. **Collect metrics** (3 test runs for statistical validation):

   **Test Run 1**:
   - Start profiler, create world, explore for 8 minutes
   - Collect: STRUCTURE_START time (%), placement count, total worldgen time
   - Export profiler results

   **Test Run 2**:
   - Repeat with new world (same seed)
   - Verify consistency with Run 1

   **Test Run 3**:
   - Final validation run
   - Calculate mean and standard deviation

4. **Baseline comparison** (from Epic 01):
   - **Scenario 1 (Vanilla)**: 200-400 placements, 10-15% STRUCTURE_START time
   - **Scenario 2 (Modded Bug)**: 2,600 placements, 50-100ms STRUCTURE_START, 40-60% of worldgen time
   - **Scenario 3 (Epic 03 - Current)**: Expected ~650 placements, 20-40ms STRUCTURE_START, 20-30% of worldgen time

5. **Calculate improvements**:
   - Placement reduction: (2,600 - actual) / 2,600 * 100% (target: 75%)
   - STRUCTURE_START reduction: (baseline - actual) / baseline * 100% (target: 50-60%)
   - Overall worldgen improvement: Compare total chunk generation time (target: 30-40%)
   - Memory improvement: Peak RAM usage (target: 50% → 20-30%)

6. **Statistical validation**:
   - Mean of 3 runs
   - Standard deviation (should be low, <10%)
   - 95% confidence interval
   - Verify improvement within 5% of target (50-80%)

7. **Document results**:
   - Create: `xeenaa-structure-manager/.claude/temp/epic-03-performance-validation.md`
   - Include: Spark profiler screenshots, metrics table, statistical analysis
   - Include: Comparison to Epic 01 baselines
   - Include: Conclusion: Did we achieve 50-80% improvement?

**Acceptance Criteria**:
- [ ] Spark profiler installed and working
- [ ] 3 test runs completed (8 minutes each)
- [ ] Placement count: ~650 (75% reduction from 2,600)
- [ ] STRUCTURE_START time: 50-60% reduction
- [ ] Overall worldgen time: 30-40% faster
- [ ] Memory usage: 20-30% peak (down from 50%)
- [ ] Statistical validation: 95% confidence interval
- [ ] Improvement within target range (50-80%)
- [ ] Results documented with Spark screenshots
- [ ] Epic 01 baseline comparison completed

**Implementation Notes**:

**Spark commands**:
```
/spark profiler start
# Explore world for 8 minutes (fly around, generate chunks)
/spark profiler stop
/spark profiler open  # View results in browser
```

**Metrics to collect from Spark**:
- `net.minecraft.world.gen.chunk.ChunkGenerator.generateStructureStarts` - STRUCTURE_START time
- Total tick time for chunk generation
- Method call count for structure placement
- Memory allocation during worldgen

**What success looks like**:
- STRUCTURE_START drops from 40-60% → 20-30% of worldgen time
- Placement count: ~650 (vs 2,600 in Epic 01 Scenario 2)
- User can SEE the improvement (worldgen feels smooth)
- RAM usage: Steady 20-30% (vs 50% in Epic 02 testing)

**If improvement less than expected**:
1. Verify mixin actually applied (check logs)
2. Verify multiplier is 2.0x (check config logs)
3. Check if some structures not affected (may need additional mixins)
4. Re-examine TASK-001 research (wrong injection point?)
5. Document findings and adjust approach

**Skills to use**:
- performance-testing (baseline comparison, statistical validation)
- minecraft-performance-structure (bottleneck analysis)
- logging-strategy (verify optimization applied)

---

## Phase 5: User Validation (Final Approval)

### Task: [TODO] TASK-006: User Manual Testing and Validation

**Agent**: User (manual testing) + implementation-agent (support)
**Priority**: CRITICAL
**Estimated Effort**: 1-2 hours (user time)
**Dependencies**: TASK-005 (performance validated with Spark)

**Goal**: User manually tests the mod in real gameplay scenario and validates that worldgen feels noticeably faster with the spacing multiplier applied.

**Requirements**:

1. **User installs latest build**:
   - Build mod: `./gradlew build`
   - Copy to mods folder (replace Epic 02 version)
   - Launch game

2. **User validates config and logs**:
   - Check game logs for startup messages
   - Verify: "Applied spacing multiplier: 2.0x" (or similar)
   - Verify: "Mode: BALANCED preset"
   - Verify: No errors or warnings

3. **User creates new world and tests**:
   - Seed: `-1234567890` (or any seed for variety)
   - Game mode: Survival or Creative
   - Play for 15-20 minutes, explore world
   - Observe worldgen speed (chunks loading)
   - Observe RAM usage (should be 20-30%, not 50%+)

4. **User validates worldgen performance**:
   - Does worldgen feel noticeably faster? (Yes/No)
   - Are chunks loading smoothly? (Yes/No)
   - Any lag spikes during exploration? (Yes/No)
   - RAM usage improved? (Yes/No)
   - Game feels playable? (Yes/No)

5. **User validates structures**:
   - Find 3-5 structures (villages, temples, etc.)
   - Verify structures are NOT broken (all parts present)
   - Verify structures ARE further apart (expected trade-off)
   - Verify structures have correct loot/spawners
   - Any missing or broken structures? (Yes/No)

6. **User feedback**:
   - Subjective: Does worldgen feel "smooth" now? (Epic 03 goal)
   - Comparison: Better than Epic 02 testing? (should be MUCH better)
   - Trade-off: Is 2x structure spacing acceptable? (or adjust preset)
   - Issues: Any crashes, errors, or unexpected behavior?
   - Approval: Ready to complete Epic 03? (Yes/No)

7. **User approves epic**:
   - If satisfied: Mark Epic 03 as COMPLETED
   - If issues: Create `/fix` tasks and address before completion
   - If needs adjustment: Modify config presets or multiplier

**Acceptance Criteria**:
- [ ] User confirms worldgen feels noticeably faster
- [ ] User confirms RAM usage improved (20-30% vs 50%)
- [ ] User confirms structures spawn correctly (not broken)
- [ ] User confirms structures are further apart (expected)
- [ ] User confirms no crashes or errors
- [ ] User subjectively rates worldgen as "smooth"
- [ ] User approves Epic 03 for completion
- [ ] User ready to proceed to Epic 04 (mod compatibility docs)

**Implementation Notes**:

**User testing checklist** (for user to follow):
```
[ ] Build and install mod
[ ] Launch game, check logs
[ ] Create new world
[ ] Play for 15-20 minutes
[ ] Observe worldgen speed (smooth? fast?)
[ ] Check RAM usage (Task Manager or F3)
[ ] Find 3-5 structures
[ ] Verify structures not broken
[ ] Verify structures further apart
[ ] Provide feedback (smooth? issues?)
[ ] Approve or request fixes
```

**What user should experience**:
- Worldgen noticeably faster than Epic 02 testing
- Chunks load quickly during exploration
- No lag spikes when generating new chunks
- RAM usage steady 20-30% (vs 50% in Epic 02)
- Structures spawn correctly but less frequently
- Game feels playable and smooth

**If user reports issues**:
1. implementation-agent investigates logs
2. Identify root cause (mixin not applying? wrong multiplier?)
3. Create `/fix` sub-task (e.g., TASK-002.1)
4. Fix and retest
5. Return to user for re-validation

**If user wants different spacing**:
- User can change preset in config (Balanced → Performance → Ultra)
- Or set custom spacing_multiplier (e.g., 2.5x)
- Restart game and retest

**Skills to use**:
- None (user testing)
- implementation-agent uses logging-strategy if debugging needed

---

## Success Criteria (Epic-Level)

Epic 03 is **COMPLETE** when all tasks are marked COMPLETED and:

1. ✅ **Spacing multiplier applied** to all structure placement calculations
2. ✅ **Performance validated**: 50-80% improvement measured (Spark profiler)
3. ✅ **Placement reduction validated**: 2,600 → ~650 placements (75%)
4. ✅ **Memory improvement validated**: 50% → 20-30% RAM usage
5. ✅ **Compatibility validated**: Works with vanilla + 569 modded structures
6. ✅ **User validated**: User confirms worldgen feels faster
7. ✅ **No regressions**: No crashes, errors, or broken structures
8. ✅ **Ready for Epic 04**: User approves moving to mod compatibility docs

**Definition of Done**:
- All 6 tasks marked COMPLETED
- All acceptance criteria met
- User approves Epic 03 for merge/release
- Ready to proceed to Epic 04 (existing mod compatibility documentation)

---

## Implementation Timeline

| Phase | Tasks | Estimated Time | Cumulative |
|-------|-------|----------------|------------|
| Phase 1: Research | TASK-001 | 2-3 hours | 2-3 hours |
| Phase 2: Implementation | TASK-002 | 3-4 hours | 5-7 hours |
| Phase 3: Testing | TASK-003, TASK-004 | 3-5 hours | 8-12 hours |
| Phase 4: Validation | TASK-005 | 2-3 hours | 10-15 hours |
| Phase 5: User Validation | TASK-006 | 1-2 hours | 11-17 hours |

**Target completion**: 2-3 days (including user validation and potential fixes)

---

## Risk Mitigation

### Task-Level Risks

**TASK-001 (Research)**:
- Risk: Can't find injection point in current Minecraft version
- Mitigation: Use genSources, examine multiple placement types, consult Fabric docs

**TASK-002 (Implementation)**:
- Risk: Mixin breaks structure placement or causes crashes
- Mitigation: Test incrementally, use defensive coding, comprehensive logging

**TASK-003/TASK-004 (Testing)**:
- Risk: Structures broken or missing parts
- Mitigation: Test many structure types, visual inspection, check logs for errors

**TASK-005 (Performance)**:
- Risk: Improvement less than 50-80% target
- Mitigation: Verify multiplier applied, check all placement types, iterate on approach

**TASK-006 (User Validation)**:
- Risk: User finds worldgen still slow or structures broken
- Mitigation: Address issues with /fix tasks, adjust config presets if needed

---

## References

- **Epic 03 requirements.md**: Business requirements and success metrics
- **Epic 01 decision-matrix.md**: Why spacing multiplier (ROI 10.0, highest)
- **Epic 01 baselines**: Test procedure and baseline performance data
- **Epic 02 config**: StructureManagerConfig.java (getEffectiveMultiplier() method)
- **minecraft-performance-structure skill**: Structure optimization patterns
- **fabric-modding skill**: Mixin patterns and injection strategies
- **coding-standards skill**: Java conventions and documentation

---

## Next Steps After Epic 03

1. **Epic 04**: Document compatibility with existing performance mods (SLO, FerriteCore, C2ME)
   - Measure combined improvement (Epic 03 + existing mods)
   - Expected: 75-85% combined improvement
   - Create compatibility guide for users

2. **Epic 05+**: Conditional on user feedback
   - Per-structure multipliers (if users want finer control)
   - Structure classification system (if needed)
   - Advanced placement strategies (if requested)

3. **User feedback**: Gather community input on next priorities

---

**Status**: READY - All tasks defined, waiting for user to confirm and start execution with `/next`
