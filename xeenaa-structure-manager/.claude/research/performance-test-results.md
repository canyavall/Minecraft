# Performance Test Results - Epic 03 Validation

**Date**: 2025-10-25
**Test Type**: Live gameplay with 569 structures from 13 mods
**Duration**: ~8 minutes of exploration
**Result**: **MULTIPLIERS ARE NOT BEING APPLIED** ❌

## Test Environment

**Structure Mods Installed**: 13
- additionalstructures: 198 structures (35%)
- mvs: 129 structures (23%)
- repurposed_structures: 107 structures (19%)
- dungeons_arise: 39 structures
- mss: 35 structures
- minecraft: 34 structures (vanilla)
- Others: 27 structures

**Total Structures**: 569 (17x vanilla)

**Current Config**:
```toml
[global_structure_settings]
spacing_multiplier = 1.0
separation_multiplier = 1.0

Size multipliers: small=1.2x, medium=1.5x, large=2.5x
Type multipliers: town=2.0x, dungeon=1.8x, temple=2.2x
```

## Critical Findings

### 1. Config Validation Failure

**Log Evidence**:
```log
[14:04:32] [Render thread/WARN] Global spacing (1.0) must be greater than separation (1.0), using defaults
```

**Problem**: Config validation rejected `spacing_multiplier = 1.0` because it equals `separation_multiplier = 1.0`, but then **failed to apply default values**. Structures are spawning at **vanilla rates** with 569 registered structures.

**Impact**: Multipliers completely non-functional.

### 2. Massive Structure Density

**Placement Statistics** (8 minutes of gameplay):
- **Total placements**: 2,600+ structures
- **Placement rate**: 10-87 placements/sec (fluctuating)
- **Memory usage**: 1,667 MB - 4,860 MB (highly variable)

**Extreme Examples**:

**Mineshaft Spam**:
```log
repurposed_structures:mineshaft_icy #1-51 placed in seconds
Many at the SAME chunk: (76, 24), (71, 24)
Minimum distance: 51 blocks apart (3.2 chunks)
```

**Trial Chambers Explosion**:
```log
minecraft:trial_chambers #1-312 placements
Average distance: ~304 blocks (19 chunks)
```

**Underground Bunkers**:
```log
underground_bunkers:underground_bunker #1-240 placements
```

**Betterdungeons**:
```log
betterdungeons:small_dungeon #1-93 placements
Minimum distance: 8.1 chunks (129 blocks)
```

### 3. Structures Overlapping

**Evidence of multiple structures at identical chunk positions**:
```log
[14:15:39] Placed repurposed_structures:mineshaft_icy #24 at chunk (76, 24)
[14:15:39] Placed repurposed_structures:mineshaft_icy #25 at chunk (71, 24)
[14:15:39] Placed repurposed_structures:mineshaft_icy #32 at chunk (76, 24)
[14:15:39] Placed repurposed_structures:mineshaft_icy #33 at chunk (71, 24)
[14:15:39] Placed repurposed_structures:mineshaft_icy #35 at chunk (76, 24)
[14:15:39] Placed repurposed_structures:mineshaft_icy #36 at chunk (71, 24)
```

Multiple mineshafts spawning at **the exact same chunk coordinates** within milliseconds.

### 4. Performance Impact

**Observed symptoms** (user reported):
- Computer struggled to render
- Visible lag during exploration
- Frame drops from excessive structure generation

**Memory pressure**:
- Peak: 4,860 MB / 6,216 MB (78% usage)
- Low: 1,667 MB / 6,216 MB (27% usage)
- **High variability** indicates GC pressure from rapid structure spawning

**Placement rate volatility**:
- Best: 87.6 placements/sec
- Worst: 10.4 placements/sec
- **8x variance** indicates worldgen thread contention

## Root Cause Analysis

### Issue #1: Config Defaults Not Applied

**Code Path**:
```
ConfigManager loads config
  ↓
Validation checks: spacing > separation
  ↓
FAILS: spacing (1.0) == separation (1.0)
  ↓
Logs warning: "using defaults"
  ↓
BUG: No defaults actually applied!
  ↓
Result: spacing_multiplier = 1.0, separation_multiplier = 1.0
```

**Impact**: All 569 structures spawn at vanilla rates (no spacing increase).

### Issue #2: Multiplier System Not Active

**Tasks TASK-001 to TASK-003 are marked COMPLETED** ✅, but the test proves they're **not working**:

1. **TASK-001**: Lazy classification implemented, but...
2. **TASK-002**: `classifyStructure()` method exists, but...
3. **TASK-003**: ConfigManager integration... **NOT WORKING**

**Evidence**:
- No log entries showing "Applied 2.0x spacing to village"
- No classification logs showing "Classified minecraft:village_plains as LARGE TOWN"
- Structures spawning at vanilla density despite 569 total structures

### Issue #3: Epic 03 Tasks Incomplete

**Tasks marked COMPLETED prematurely**:
- TASK-001: Code exists but not proven functional
- TASK-002: Method exists but not called correctly
- TASK-003: Integration broken (config defaults issue)

**Tasks remaining**:
- TASK-004: `/xeenaa stats` multiplier verification - **NEEDED NOW**
- TASK-005: Preset testing - blocked
- TASK-006: Performance validation - blocked

## Required Fixes

### Fix #1: Config Default Application (CRITICAL)

**Problem**: When validation fails, defaults aren't applied.

**Fix**: Update ConfigManager to apply sensible defaults when validation fails:
```java
if (spacing <= separation) {
    LOGGER.warn("Global spacing ({}) must be greater than separation ({}), applying defaults",
        spacing, separation);

    // APPLY DEFAULTS
    this.globalSpacingMultiplier = 1.5;  // Balanced default
    this.globalSeparationMultiplier = 1.0;
}
```

### Fix #2: Verify Multiplier Integration

**Need to verify**:
1. `StructurePlacementCalculatorMixin` calls `ConfigManager.getSpacingMultiplier()`
2. Classification results are passed to config query
3. Multipliers are actually applied to structure placement calculations

**Add debug logging**:
```java
LOGGER.info("Structure: {}, Classification: {} {}, Multiplier: {}x",
    id, size, type, multiplier);
```

### Fix #3: Complete TASK-004 (Enhanced Stats)

**Priority**: CRITICAL

**Why**: We need to **prove** multipliers work with concrete data. `/xeenaa stats` should show:
```
=== Multiplier Verification ===
minecraft:village_plains: Multiplier=2.0x, Expected=64 chunks, Actual=128 chunks ✓ PASS
repurposed_structures:mineshaft_icy: Multiplier=1.0x, Expected=32 chunks, Actual=5 chunks ⚠ FAIL
```

## Impact on Epic 03 Goals

**Epic Goal**: "Make the mod actually improve structure spacing (not just observe)"

**Current Status**: **FAILING** ❌

- ❌ Multipliers not applied
- ❌ Structures spawning at vanilla density with 17x vanilla count
- ❌ Massive visual clutter and performance issues
- ✅ Placement tracking works (proven by logs)
- ✅ Classification system exists (but not proven functional)

**Remaining Work**:
1. Fix config default application (30 min)
2. Verify multiplier integration (1 hour)
3. Complete TASK-004 (enhanced stats) (2-3 hours)
4. Test with fixed config (1 hour)
5. Complete TASK-005 and TASK-006 (3-4 hours)

**Total**: ~8-10 hours to complete Epic 03

## Recommendations

### Immediate Actions

1. **Fix config defaults** - Highest priority
2. **Add debug logging** to multiplier application
3. **Run /next** to implement TASK-004 (enhanced stats)
4. **Re-test** with fixed config and verify spacing improves

### Testing Protocol

1. Fix config system
2. Create new world
3. Explore for 5 minutes
4. Run `/xeenaa stats`
5. Verify:
   - Villages are 2x farther apart
   - Mineshafts maintain vanilla spacing
   - Large structures are 2.5x farther apart

### Success Criteria

**Epic 03 passes when**:
- ✅ Config defaults applied correctly
- ✅ Multipliers visible in logs ("Applied 2.0x to village")
- ✅ `/xeenaa stats` shows expected vs actual matching
- ✅ Structures measurably farther apart (visual gameplay test)
- ✅ Performance acceptable (no lag from 569 structures)

---

**Conclusion**: The performance test was **extremely valuable**. It revealed our mod isn't working yet - multipliers are completely non-functional due to config validation bug. This is exactly what Epic 03 was designed to fix. We now have concrete evidence of what needs to be fixed and a clear path forward.

**Next Step**: Run `/next` to execute TASK-004, but FIRST fix the config defaults issue.

---

**Tags**: #performance-test #epic-03 #multipliers-broken #config-bug #569-structures
**Status**: Test complete - critical bugs identified
**Action Required**: Fix config defaults, then continue Epic 03 tasks
