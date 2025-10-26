# Epic 03: Structure Spacing Verification and Integration

**Created**: 2025-10-24
**Priority**: Critical
**Status**: READY
**Dependencies**: Epic 01 (Classification System), Placement Tracking (Informal)

## Executive Summary

Enable and verify the complete structure management system by re-activating the classification engine, implementing lazy classification to avoid performance issues, and using our placement tracking system to verify that multipliers are correctly applied to structure spacing.

**Business Value**: Transform the mod from observation-only to actively improving structure spacing, reducing world clutter while maintaining performance.

**User Experience**: Install mod → Load world → Structures automatically spaced further apart → Use `/xeenaa stats` to verify improvements

---

## Background

### Current State
- ✅ **Epic 01 Complete**: Classification system and multiplier application fully implemented
- ✅ **Placement Tracking Complete**: Real-time tracking with performance monitoring
- ❌ **Classification Disabled**: Line 160 in StructureManagerMod.java comments out `initializeClassificationSystem()`
- ❌ **No Active Improvement**: Mod only tracks placements, doesn't modify spacing

### Problem
Epic 01's classification system loads 569 NBT files synchronously during world load, causing 7+ minute hangs. This made testing impossible, so classification was disabled.

### Solution
Implement **lazy classification** - only classify structures when they're actually placed, eliminating the bulk loading issue while maintaining full functionality.

---

## Feature 1: Lazy Classification System

**Description**: Replace upfront classification (all 569 structures at world load) with on-demand classification (only when a structure is actually placed).

**Business Requirements**:
- World loads in <10 seconds (same as vanilla)
- First placement of each structure type triggers classification
- Subsequent placements use cached results
- No performance regression during gameplay

**User Experience**:
1. Install mod and load world
2. World loads quickly (no 7-minute hang)
3. As player explores, structures are classified on-demand
4. Console shows: "Classified minecraft:village_plains as LARGE TOWN"
5. Subsequent villages use cached classification

**Technical Approach**:
- Remove `ClassificationSystem.classifyAll()` call from world load
- Add classification trigger in `StructurePlacementCalculatorMixin`
- Check cache first, classify on miss
- Thread-safe cache access during world generation

**Success Criteria**:
- World loads in <10 seconds with 569 structures
- No classification happens until structures are actually placed
- Cache hit rate >95% after exploring for 10 minutes
- No threading issues or race conditions

---

## Feature 2: Multiplier Application Verification

**Description**: Use the placement tracking system to verify that config multipliers are correctly applied to structure spacing.

**Business Requirements**:
- Users can visually verify structures are further apart
- `/xeenaa stats` command shows before/after spacing analysis
- Console logs show which multipliers were applied
- Statistical analysis proves multipliers work

**User Experience**:
1. Load world with mod enabled (Balanced preset, default multipliers)
2. Play for 10 minutes, exploring and generating chunks
3. Run `/xeenaa stats` command
4. See report: "Villages: Avg spacing 32.4 chunks (expected 28-36 for 2.0x multiplier)"
5. Verify multipliers are working correctly

**Technical Approach**:
- Enhance `PlacementTracker.logDetailedStatistics()` to show expected vs actual spacing
- Calculate expected spacing from config multipliers
- Compare actual spacing (from placement tracking) to expected
- Flag discrepancies for investigation

**Success Criteria**:
- Villages with 2.0x multiplier show ~2x vanilla spacing
- Mineshafts with 1.0x multiplier show vanilla spacing
- `/xeenaa stats` output is clear and actionable
- Users can see the mod is working

---

## Feature 3: Config Integration Testing

**Description**: Verify that all three config presets (Sparse, Balanced, Abundant) work correctly and produce measurably different spacing.

**Business Requirements**:
- Sparse preset produces most spacing (structures rare)
- Balanced preset is default (structures less common than vanilla)
- Abundant preset produces least spacing (structures more common)
- Users can switch presets and see immediate effect

**User Experience**:
1. Test Sparse preset (spacing_multiplier = 2.5x globally)
2. Generate world, note spacing in logs
3. Delete world, switch to Abundant preset (0.8x globally)
4. Generate world, note spacing is ~3x closer (2.5/0.8 = 3.125x difference)
5. Confirm presets work as advertised

**Technical Approach**:
- Test each preset with `/xeenaa stats` analysis
- Compare spacing statistics across presets
- Verify multiplier compounding works correctly
- Document expected spacing for each preset

**Success Criteria**:
- Sparse preset: Avg spacing >2x vanilla
- Balanced preset: Avg spacing 1.5x vanilla
- Abundant preset: Avg spacing <1x vanilla
- Switching presets changes actual spacing

---

## Feature 4: Performance Validation

**Description**: Ensure lazy classification and multiplier application don't impact world generation performance.

**Business Requirements**:
- World generation FPS: No regression vs vanilla
- Memory usage: <50MB for classification cache
- CPU usage: Classification <5ms per structure
- No lag spikes during exploration

**User Experience**:
1. Install mod
2. Play normally for 30 minutes
3. Monitor F3 debug screen (no FPS drops)
4. Check logs for performance warnings
5. Mod is invisible performance-wise

**Technical Approach**:
- Add timing logs for each classification event
- Monitor memory usage in PlacementTracker
- Measure classification time per structure
- Use Spark Profiler for CPU analysis

**Success Criteria**:
- Classification: <5ms per structure
- Memory: <1KB per cache entry (<569KB total)
- No FPS regression during world generation
- No performance warnings in logs

---

## Non-Goals (Future Epics)

- Advanced classification (Y-level detection, shape analysis)
- Structure-specific overrides UI
- Dimension-specific multipliers (Nether/End)
- Biome-aware spacing adjustments
- Automated testing suite

---

## Technical Constraints

- Must work with 569 structures from 13 mods
- Thread-safe (world generation is multi-threaded)
- Lazy classification must not block world gen
- Placement tracking must have minimal overhead

---

## Success Metrics

**Epic 03 is complete when**:
1. ✅ World loads in <10 seconds (vs 7+ minutes with upfront classification)
2. ✅ Classification happens on-demand during gameplay
3. ✅ `/xeenaa stats` shows multipliers are correctly applied
4. ✅ Villages are measurably further apart (spacing analysis proves it)
5. ✅ All three presets produce different spacing
6. ✅ No performance regression during gameplay
7. ✅ User can see the mod is working (logs + stats command)

---

## Dependencies

- ✅ Epic 01: Classification and multiplier system (implemented)
- ✅ Placement Tracking: Real-time tracking system (implemented)
- ⏳ Lazy classification implementation (new)
- ⏳ Verification tooling (enhancements to stats command)

---

## Estimated Effort

**Total**: 8-12 hours

- Feature 1 (Lazy Classification): 3-4 hours
- Feature 2 (Multiplier Verification): 2-3 hours
- Feature 3 (Config Testing): 2-3 hours
- Feature 4 (Performance Validation): 1-2 hours

---

## Risks and Mitigation

| Risk | Probability | Impact | Mitigation |
|------|------------|--------|------------|
| Lazy classification too slow | Medium | High | Implement async classification queue |
| Cache miss rate too high | Low | Medium | Pre-classify common structures |
| Multipliers not applying | Low | Critical | Add debug logging to mixin |
| Threading issues | Medium | High | Comprehensive thread safety testing |

---

## Approval

**Ready for planning when**:
- User validates these requirements
- User confirms this is the right scope for Epic 03
- User approves lazy classification approach

Run `/create_plan` to generate technical tasks from these requirements.
