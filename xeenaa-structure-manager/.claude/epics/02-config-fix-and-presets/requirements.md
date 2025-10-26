# Epic 02: Config Fix and Performance Presets - Requirements

**Epic Status**: PLANNING
**Priority**: CRITICAL
**Target Release**: MVP (Minimum Viable Product)
**Epic Type**: Bug Fix + UX Enhancement
**Estimated Effort**: <6 hours
**Expected Impact**: 50-80% worldgen performance improvement

---

## Executive Summary

Epic 01 research identified a **critical config validation bug** that prevents spacing multipliers from being applied, causing a 100x explosion in structure placement attempts (2,600 vs 200-400 expected). This epic fixes the bug and adds user-friendly performance presets to make the optimization accessible to all users.

**The Problem**: Config validation rejects `spacing_multiplier = 1.0` as invalid, logs "using defaults," but never actually applies the default multiplier. Result: All 569 structures spawn at vanilla spacing with no optimization applied.

**The Solution**: Fix the validation logic to properly apply 2.0x as the default multiplier, and add 4 performance presets (Vanilla/Balanced/Performance/Ultra) so users can easily choose their desired performance vs density trade-off.

**Why This Matters**: This is the **highest ROI optimization** in the entire Epic 01 catalog (ROI score: 10.0). Fixing this bug delivers 50-80% improvement in under 6 hours of work.

---

## Business Value

### Player Experience Impact

**Before Epic 02** (config bug active):
- World generation: "Computer struggles to render chunks"
- Chunk generation time: 125-200ms per chunk
- Structure placements: 2,600 in 8 minutes (overwhelming)
- Memory usage: 1.6-4.8 GB volatile swings
- GC pauses: 100-200ms every 30-60 seconds
- **User experience**: Unplayable with 500+ structures

**After Epic 02** (bug fixed + presets):
- World generation: "Smooth and responsive"
- Chunk generation time: 80-120ms per chunk (36-40% faster)
- Structure placements: 1,300 in 8 minutes (50% reduction)
- Memory usage: 0.9-2.5 GB stabilized
- GC pauses: 20-40ms every 2-3 minutes (80% reduction)
- **User experience**: Playable and enjoyable

### Target Audience

1. **Modpack Players** (primary): Using 10-15+ structure mods, experiencing slow worldgen
2. **Server Owners** (secondary): Need better worldgen performance for multiplayer
3. **Content Creators** (tertiary): Recording/streaming, need smooth gameplay
4. **Casual Users** (bonus): Anyone who installed "just a few" structure mods and noticed lag

### Success Metrics

**Performance Metrics** (measured via Spark profiler):
- STRUCTURE_START time: 50-100ms → 25-50ms (50% reduction)
- Worldgen time per chunk: 125-200ms → 80-120ms (36-40% reduction)
- Structure placements per 8 min: 2,600 → 1,300 (50% reduction)
- Memory allocation rate: 20 MB/min → 10 MB/min (50% reduction)
- GC frequency: 8-12/min → 2-3/min (75% reduction)

**User Experience Metrics**:
- Config difficulty: "Confusing" → "One-click presets"
- Setup time: 5-10 minutes (reading docs) → 30 seconds (select preset)
- Performance improvement awareness: "Is it working?" → "Clear metrics shown"

**Acceptance Criteria** (must pass to complete epic):
- [ ] Config bug fixed (1.0 → 2.0x default applied)
- [ ] 40-60% improvement measured (Spark profiler validation)
- [ ] 4 presets implemented (Vanilla/Balanced/Performance/Ultra)
- [ ] Preset selection tested by user
- [ ] Performance metrics visible in logs
- [ ] No structures broken or missing (100% compatibility)

---

## Feature 1: Fix Config Validation Bug

### Description

The config validation logic currently treats `spacing_multiplier = 1.0` as invalid and logs "using defaults," but fails to actually apply the default multiplier value. This causes all 569 structures to spawn at vanilla spacing (1.0x) with zero optimization.

**Root Cause**:
```java
// Current (BROKEN):
if (spacingMultiplier <= 1.0) {
    LOGGER.warn("Invalid spacing_multiplier {}, using defaults", spacingMultiplier);
    spacingMultiplier = 1.0; // Bug: Sets to 1.0 instead of recommended default!
}
```

**Expected Behavior**:
- If user sets `spacing_multiplier < 1.0`: Reject as invalid, apply 2.0x default
- If user sets `spacing_multiplier = 1.0`: Treat as "use recommended default", apply 2.0x
- If user sets `spacing_multiplier > 1.0`: Use their value as-is

### User Experience

**Current (Broken)**:
1. User installs mod
2. Sees config file with `spacing_multiplier = 1.0`
3. Thinks "1.0 means use default, that's fine"
4. Plays game, worldgen is still slow
5. Confused: "Is the mod even working?"

**After Fix**:
1. User installs mod
2. Sees config file with `spacing_multiplier = 2.0` (or leaves at default)
3. Plays game, worldgen is smooth
4. Sees logs: "Using recommended spacing multiplier: 2.0x"
5. Happy: "Mod is working, worldgen feels great!"

### Success Criteria

- [ ] Validation logic fixed (< 1.0 = invalid, = 1.0 = use 2.0x default, > 1.0 = use value)
- [ ] Default config value changed to 2.0 (not 1.0)
- [ ] Log message clearly states applied multiplier ("Using spacing multiplier: 2.0x")
- [ ] Config bug diagnostic error removed (no longer triggers)
- [ ] Tested: Config with 1.0 → applies 2.0x
- [ ] Tested: Config with 0.5 → rejects, applies 2.0x default
- [ ] Tested: Config with 3.0 → applies 3.0x as-is

### Implementation Notes

**Files to modify**:
- Config validation class (wherever spacing_multiplier is validated)
- Default config file (change default from 1.0 to 2.0)
- Logging statements (clarify what multiplier is actually applied)

**Expected effort**: <2 hours
**Risk level**: Zero (config fix only, no gameplay changes)
**Testing**: Manual testing + automated config parsing test

---

## Feature 2: Performance Presets System

### Description

Add 4 pre-configured performance presets that users can select with a single config option. Each preset represents a different performance vs structure density trade-off, making it easy for users to choose the right balance for their hardware and preferences.

**Presets**:
1. **Vanilla** (1.0x): No optimization, baseline performance (for comparison/testing)
2. **Balanced** (2.0x): 50% improvement, good structure density (RECOMMENDED for most users)
3. **Performance** (3.0x): 80% improvement, sparse structures (for lower-end hardware)
4. **Ultra** (4.0x): 90% improvement, very sparse structures (maximum performance)

### User Experience

**Config file** (simple and clear):
```toml
[performance]
# Performance preset: Controls structure spacing optimization
# - "Vanilla": No optimization (1.0x spacing)
# - "Balanced": 50% faster worldgen, good structure density (2.0x) [RECOMMENDED]
# - "Performance": 80% faster worldgen, sparse structures (3.0x)
# - "Ultra": 90% faster worldgen, very sparse structures (4.0x)
preset = "Balanced"

# Advanced: Set custom multiplier (overrides preset if uncommented)
# spacing_multiplier = 2.5
```

**User flow**:
1. User opens config file
2. Sees clear descriptions of each preset with % improvement
3. Changes `preset = "Balanced"` to `preset = "Performance"`
4. Saves file, restarts game
5. Logs show: "Using Performance preset (3.0x spacing multiplier)"
6. Worldgen is 80% faster

### Success Criteria

- [ ] 4 presets implemented (Vanilla/Balanced/Performance/Ultra)
- [ ] Preset selection via config (`preset = "Balanced"`)
- [ ] Custom multiplier override option (`spacing_multiplier = 2.5` overrides preset)
- [ ] Clear config comments explaining each preset
- [ ] Log message shows selected preset and applied multiplier
- [ ] Tested: Each preset applies correct multiplier
- [ ] Tested: Custom multiplier overrides preset
- [ ] Tested: Invalid preset falls back to Balanced with warning

### Preset Specifications

| Preset | Multiplier | Expected Improvement | Use Case |
|--------|------------|---------------------|----------|
| **Vanilla** | 1.0x | 0% (baseline) | Testing, comparison, "I want all structures" |
| **Balanced** | 2.0x | 50% worldgen speedup | Recommended for most users, good balance |
| **Performance** | 3.0x | 80% worldgen speedup | Lower-end hardware, prioritize performance |
| **Ultra** | 4.0x | 90% worldgen speedup | Maximum performance, exploration-focused |

**Recommendation to users** (in config):
- Most users: **Balanced** (great performance, still plenty of structures)
- Potato PCs: **Performance** (prioritize smooth gameplay)
- Exploration-focused: **Ultra** (fast worldgen, structures still findable but rare)
- Modpack testing: **Vanilla** (compare with/without optimization)

### Implementation Notes

**Files to create/modify**:
- Config class: Add `preset` enum field
- Config file: Add preset selection with comments
- Preset mapping: Map preset names to multiplier values
- Validation: Validate preset name, fall back to Balanced if invalid
- Logging: Show preset name and applied multiplier

**Expected effort**: 2-4 hours
**Risk level**: Low (config enhancement, no complex logic)
**Testing**: Manual testing of all 4 presets + custom override

---

## Feature 3: Performance Metrics Logging

### Description

Add clear, user-visible logging that shows:
1. Which preset/multiplier is active
2. How many structures were modified
3. Confirmation that optimization is working
4. Performance improvement estimates

This addresses the "Is the mod even working?" confusion and builds user confidence.

### User Experience

**Game logs** (on world load):
```
[Xeenaa Structure Manager] ═══════════════════════════════════════
[Xeenaa Structure Manager] Config: Using "Balanced" preset (2.0x spacing multiplier)
[Xeenaa Structure Manager] Modified 147 structure sets (from 569 total structures)
[Xeenaa Structure Manager] Expected improvement: 50% faster worldgen
[Xeenaa Structure Manager] Structures will spawn ~2x further apart on average
[Xeenaa Structure Manager] ═══════════════════════════════════════
```

**During worldgen** (DEBUG level, optional):
```
[Xeenaa Structure Manager] Performance: 45 structures placed, avg spacing 128 chunks, 5.2 placements/sec
[Xeenaa Structure Manager] STRUCTURE_START avg: 32ms (vs 65ms baseline, 51% improvement)
```

### Success Criteria

- [ ] Startup log shows preset and multiplier (INFO level)
- [ ] Startup log shows structure count and expected improvement (INFO level)
- [ ] Optional performance metrics during worldgen (DEBUG level)
- [ ] Error log if config bug detected (CRITICAL - should never trigger after fix)
- [ ] Logs are clear and non-technical (users can understand)
- [ ] Tested: Logs appear correctly for all presets

### Implementation Notes

**Logging points**:
1. **World load** (after StructurePlacementCalculator creation): Show preset, count, expected improvement
2. **Periodic during worldgen** (every 100 chunks, DEBUG level): Show placement stats
3. **Config bug detection** (should be removed after fix, but keep as safety net): Critical error if 1.0x detected

**Expected effort**: 1-2 hours
**Risk level**: Zero (logging only, no gameplay impact)
**Testing**: Manual verification of log messages

---

## Out of Scope (Future Epics)

The following are explicitly **NOT** included in Epic 02:

- ❌ **Per-structure spacing overrides** (Epic 04-05, conditional on user feedback)
- ❌ **Structure classification system** (Epic 04-05, for advanced users)
- ❌ **GUI configuration tool** (Epic 06+, nice-to-have)
- ❌ **Jigsaw optimization** (Covered by recommending Structure Layout Optimizer in Epic 03)
- ❌ **Memory optimization** (Achieved indirectly by reducing placement rate)
- ❌ **Biome pre-filter** (Epic 07+, low ROI per Epic 01 analysis)
- ❌ **Async STRUCTURE_START** (Research-only, vanilla limitation)

**Rationale**: Epic 02 focuses on the **highest ROI** fix (config bug) + **best UX** feature (presets). Other optimizations deferred until Epic 02 is validated.

---

## Technical Constraints

### Performance Requirements

- **Minimum improvement**: 40% worldgen speedup (Balanced preset)
- **Maximum improvement**: 90% worldgen speedup (Ultra preset)
- **Compatibility**: 100% with all structure mods (no breaking changes)
- **Memory overhead**: <1 MB (negligible for preset system)
- **Config load time**: <50ms (negligible impact on startup)

### Compatibility Requirements

- **Minecraft version**: 1.21.1
- **Fabric API**: Latest stable
- **Structure mods**: Universal compatibility (works with ANY mod adding structures)
- **Multiplayer**: Full server support (optimization applies server-side)
- **Datapacks**: Compatible with datapack-added structures

### Testing Requirements

**Before Epic 02 completion, must test**:
1. **Config bug fix**: Verify 1.0 → 2.0x default applied correctly
2. **All 4 presets**: Test Vanilla/Balanced/Performance/Ultra
3. **Custom override**: Test spacing_multiplier overrides preset
4. **Performance validation**: Run Scenario 2 (bug) vs Scenario 3 (fixed) from Epic 01 test procedure
5. **Compatibility**: Test with 3+ structure mod combinations
6. **Multiplayer**: Test on dedicated server (optimization applies correctly)

**Acceptance criteria** (from Epic 01 test procedure):
- 40-60% improvement measured (Scenario 3 vs Scenario 2)
- Variance within 20% of expected
- 3 test runs for statistical validity

---

## Dependencies

### Prerequisites

- [x] **Epic 01 completed**: Research and decision matrix available
- [x] **Test procedure defined**: Epic 01 TASK-008 provides reproducible methodology
- [x] **Baseline measurements**: Epic 01 TASK-007 provides comparison data

### External Dependencies

- None (config-only changes, no external libraries required)

### Epic 02 Blocking Epic 03+

**Epic 03** (Recommend Existing Mods):
- Depends on Epic 02 validation (ensure our optimization works before recommending combos)

**Epic 04-05** (Advanced Features):
- Conditional on Epic 02 user feedback (do users need per-structure control?)

---

## Success Criteria (Epic-Level)

Epic 02 is considered **COMPLETE** when:

1. **Config bug fixed** and validated (< 1.0 invalid, = 1.0 uses 2.0x, > 1.0 uses value)
2. **4 presets implemented** and tested (Vanilla/Balanced/Performance/Ultra)
3. **Custom multiplier override** works correctly
4. **Performance improvement measured**: 40-60% worldgen speedup (Balanced preset, Scenario 3 vs 2)
5. **Statistical validation**: 3 test runs, consistent results, 95% confidence
6. **Compatibility validated**: 3+ structure mod combinations tested
7. **User validation**: User manually tests preset selection and worldgen performance
8. **Logs confirmed**: Clear metrics showing preset and improvement

**User experience goal**: Users should be able to install the mod, select a preset, and immediately feel the performance improvement without needing to understand technical details.

---

## Risk Assessment

### Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Config bug fix doesn't work | Low | High | Thorough testing of validation logic, unit tests |
| Presets don't match expected improvement | Low | Medium | Reference Epic 01 performance math, validate with testing |
| Users confused by preset choices | Low | Low | Clear config comments, recommend Balanced for most users |
| Compatibility issue with specific mod | Low | Medium | Test with diverse mod combinations, monitor user reports |

**Overall risk level**: **LOW** - Config-only changes with clear testing methodology

---

## Timeline Estimate

| Task | Estimated Time |
|------|----------------|
| Fix config validation bug | 1-2 hours |
| Implement preset system | 2-3 hours |
| Add performance logging | 1 hour |
| Testing (all scenarios) | 1-2 hours |
| **Total** | **5-8 hours** |

**Target completion**: 1-2 days (including user validation)

---

## References

- **Epic 01 Executive Summary**: Complete research findings and recommendation
- **Epic 01 Decision Matrix**: ROI scoring (Config Fix = 10.0, Presets = 4.5)
- **Epic 01 Test Procedure**: Reproducible testing methodology (TASK-008)
- **Epic 01 Baseline Data**: Performance measurements (TASK-007)
- **minecraft-performance-structure skill**: Updated with Epic 01 findings (includes config bug details)

---

## Questions for User Validation

Before proceeding with Epic 02 implementation, please confirm:

1. **Priority**: Is fixing the config bug + adding presets the right Epic 02 focus? (Alternative: We could do other optimizations first)
2. **Presets**: Are 4 presets (Vanilla/Balanced/Performance/Ultra) sufficient? Should we add more granularity?
3. **Default**: Should the default preset be "Balanced" (2.0x)? Or "Vanilla" (1.0x) for conservative users?
4. **Logging**: Is INFO-level startup logging acceptable? Or should we make it quieter (only log on custom config)?

**Recommendation**: Proceed as planned - Epic 02 has the highest ROI (10.0) and delivers immediate user value.

---

**Status**: PLANNING - Awaiting user validation before running `/create_plan`
