# Epic 03: Apply Spacing Multiplier to Structure Placement - Requirements

**Epic Status**: PLANNING
**Priority**: CRITICAL
**Target Release**: MVP (Minimum Viable Product)
**Epic Type**: Core Optimization Implementation
**Estimated Effort**: 8-12 hours
**Expected Impact**: 50-80% worldgen improvement (measured)

---

## Executive Summary

Epic 02 created the config system that stores spacing multipliers (2.0x), but the multiplier is **not yet applied** to actual structure placement. This epic implements the core optimization: **modifying Minecraft's structure placement algorithm** to multiply spacing/separation values by the configured multiplier, reducing placement attempts by 75% and achieving the promised 50-80% worldgen improvement.

**The Gap**: Config system works perfectly (2.0x stored), but structures still place at vanilla density (2,100+ placements observed in testing).

**The Solution**: Hook into STRUCTURE_START stage, multiply spacing calculations by config value, reduce placement attempts from ~2,600 to ~650 (75% reduction).

**Why This Matters**: This is THE performance optimization. Epic 02 prepared the config, Epic 03 delivers the actual speedup.

---

## Business Value

### Player Experience Impact

**Current State** (after Epic 02):
- Config system: ✅ Working
- Preset selection: ✅ Easy
- Multiplier stored: ✅ 2.0x
- **Actual performance**: ❌ No improvement (multiplier not applied)
- **Placements**: ~2,100+ structures (same as before)
- **RAM usage**: 50% (slightly better, but not dramatic)

**Target State** (after Epic 03):
- Config system: ✅ Working
- Preset selection: ✅ Easy
- Multiplier stored: ✅ 2.0x
- **Multiplier applied**: ✅ Structures 2x further apart
- **Placements**: ~650 structures (75% reduction)
- **RAM usage**: 20-30% (dramatic improvement)
- **Worldgen speed**: 50-80% faster (measured)
- **User experience**: "Struggled" → "Smooth"

### Target Audience

1. **All users** - Everyone who installed the mod expects the performance improvement
2. **Performance-focused players** - Want the 50-80% speedup promised in description
3. **Server owners** - Need actual optimization, not just config UI
4. **Modpack creators** - Testing performance mods, need real results

### Success Metrics

**Performance Targets** (from Epic 01 research):
- Structure placements: 75% reduction (2,600 → 650)
- STRUCTURE_START time: 50-60% reduction (measured with Spark)
- Memory usage: 40-50% reduction (r² = 0.98 with placement rate)
- Chunk generation time: 30-40% faster overall
- User perception: "Worldgen feels smooth"

**Validation** (before completing epic):
- Automated: Spark profiler shows 50%+ improvement
- Manual: User confirms worldgen feels faster
- Statistical: 3 test runs, 95% confidence interval

---

## Feature 1: Spacing Multiplier Application

### Description

Modify Minecraft's structure placement algorithm to multiply spacing and separation values by the configured multiplier. This reduces the number of chunks where structures attempt placement, cutting placements by 75% with a 2.0x multiplier.

**How It Works**:

1. **Current behavior** (vanilla/modded):
   - Structure has `spacing = 32` and `separation = 8`
   - Every chunk in 32-chunk grid gets placement attempt
   - With 569 structures, ~569 checks per chunk
   - Result: 2,600+ placements, slow worldgen

2. **With 2.0x multiplier applied**:
   - Multiply spacing: `32 * 2.0 = 64`
   - Multiply separation: `8 * 2.0 = 16`
   - Every chunk in 64-chunk grid gets placement attempt (75% fewer)
   - With 569 structures, ~142 checks per chunk (75% reduction)
   - Result: ~650 placements, fast worldgen

**Implementation approach**: Mixin into `net.minecraft.world.gen.chunk.placement.StructurePlacement` or similar class to modify spacing/separation calculations.

### User Experience

**What users experience**:
- Select "Balanced" preset in config (2.0x multiplier)
- Launch game, create new world
- **Worldgen is noticeably faster** (50-80% improvement)
- Structures spawn ~2x further apart (expected trade-off)
- No crashes, no conflicts, transparent optimization

**What users DON'T need to do**:
- No manual configuration beyond preset selection
- No need to restart world (applies to new chunks)
- No need to understand technical details
- No compatibility issues with other mods

### Success Criteria

**Functional**:
- [ ] Spacing multiplier applied to all structures
- [ ] Separation multiplier applied to all structures
- [ ] Config value used (2.0x for Balanced preset)
- [ ] Works with vanilla structures
- [ ] Works with modded structures (all 569 tested)
- [ ] No crashes or errors
- [ ] Compatible with structure mods (no conflicts)

**Performance** (measured with Epic 01 test procedure):
- [ ] Placement count: 2,600 → ~650 (75% reduction)
- [ ] STRUCTURE_START time: 50-60% reduction
- [ ] Chunk generation time: 30-40% faster
- [ ] Memory usage: 40-50% reduction
- [ ] Statistical validation: 3 test runs, consistent results

**User validation**:
- [ ] User confirms worldgen feels faster
- [ ] User confirms structures are further apart (expected)
- [ ] User confirms no crashes or issues
- [ ] User approves epic for completion

### Implementation Notes

**Technical approach**:
1. Research Minecraft's structure placement code (use Epic 01 algorithm research)
2. Identify injection point (likely `StructurePlacement` class or spacing calculation)
3. Create mixin to multiply spacing/separation by config value
4. Ensure multiplier applied before grid calculations
5. Test with vanilla and modded structures
6. Validate with Epic 01 test procedure

**Challenges**:
- Finding correct injection point (Minecraft code may have changed)
- Ensuring multiplier applies to ALL structure types
- Avoiding conflicts with other structure mods
- Maintaining save compatibility (structures already placed unaffected)

**Epic 01 research available**:
- `placement-algorithm.md` - Complete algorithm documentation
- `code-references.md` - Exact classes and methods
- `test-procedure.md` - Reproducible testing methodology

---

## Feature 2: Validation and Testing

### Description

Validate that the spacing multiplier application achieves the expected 50-80% improvement through automated profiling and manual user testing.

**Testing phases**:
1. **Unit testing**: Verify spacing calculation modified correctly
2. **Integration testing**: Test with vanilla structures
3. **Compatibility testing**: Test with 15+ structure mods (Epic 01 mod list)
4. **Performance testing**: Measure with Spark profiler (Epic 01 methodology)
5. **Statistical validation**: 3 test runs, 95% confidence interval
6. **User validation**: User confirms improvement in real gameplay

### User Experience

**User testing checklist**:
1. Build mod with Epic 03 changes
2. Launch Minecraft, check logs (multiplier applied)
3. Create new world (seed: -1234567890 for consistency)
4. Observe worldgen speed (should feel noticeably faster)
5. Check RAM usage (should be 20-30%, not 50%+)
6. Play for 10 minutes, explore
7. Confirm structures spawn correctly (further apart, but functional)
8. Report any issues or concerns

**What indicates success**:
- Worldgen "feels smooth" (subjective but important)
- Chunks load quickly during exploration
- No lag spikes during worldgen
- Structures are further apart (expected, not a bug)
- No crashes or errors

### Success Criteria

**Automated testing**:
- [ ] Spark profiler shows 50-60% STRUCTURE_START reduction
- [ ] Placement count: ~650 (75% fewer than baseline 2,600)
- [ ] Memory usage: 20-30% peak (vs 50% current, 80% pre-Epic 02)
- [ ] 3 test runs completed, results consistent
- [ ] Statistical validation: Mean improvement within 5% of expected

**User validation**:
- [ ] User confirms worldgen feels faster
- [ ] User confirms no crashes or errors
- [ ] User confirms structures spawn correctly
- [ ] User approves performance improvement
- [ ] User ready to proceed to Epic 04 (existing mod compatibility)

**Compatibility**:
- [ ] Works with 15+ structure mods from Epic 01 testing
- [ ] No conflicts detected
- [ ] Vanilla structures unaffected
- [ ] Modded structures work correctly

### Implementation Notes

**Use Epic 01 test procedure**:
- Seed: -1234567890
- Render distance: 12 chunks
- Duration: 8 minutes worldgen
- Profiling: Spark profiler for CPU timing
- Metrics: Placement count, STRUCTURE_START time, memory

**Baseline comparison**:
- Scenario 1 (Vanilla): 200-400 placements, 10-15% STRUCTURE_START time
- Scenario 2 (Modded Bug): 2,600 placements, 50-100ms STRUCTURE_START
- **Scenario 3 (Epic 03)**: ~650 placements, 20-40ms STRUCTURE_START
- **Target**: 50-80% improvement vs Scenario 2

---

## Out of Scope (Future Epics)

- ❌ **Per-structure multipliers** (Epic 05+, after user feedback)
- ❌ **Structure classification system** (Epic 06+, conditional on need)
- ❌ **Advanced placement strategies** (Epic 07+, if needed)
- ❌ **GUI for configuration** (Epic 08+, if requested)
- ❌ **Existing mod compatibility docs** (Epic 04, after Epic 03 validation)

**Rationale**: Epic 03 focuses ONLY on applying the spacing multiplier. Additional features come after we validate this core optimization works.

---

## Technical Constraints

### Minecraft Version
- **Target**: 1.21.1
- **Fabric API**: Latest stable
- **Java**: 21

### Code Quality
- Follow `coding-standards` skill
- Comprehensive Javadoc
- Input validation
- Error handling
- Thread safety (worldgen is multi-threaded)

### Performance Requirements
- **No regressions**: Must not make anything slower
- **Minimal overhead**: Multiplication is O(1), negligible cost
- **Memory safe**: No memory leaks or excessive allocations
- **Compatible**: Must work with all structure mods

### Testing Requirements
- **Automated**: Spark profiler, 3 test runs
- **Manual**: User validation
- **Statistical**: 95% confidence interval
- **Compatibility**: Test with 15+ structure mods

---

## Dependencies

### Prerequisites

- [x] **Epic 01 completed**: Research identified spacing multiplier as optimal strategy
- [x] **Epic 02 completed**: Config system stores multiplier value
- [ ] **Epic 02 validated**: User confirms config system works (DONE - user tested in-game)

### External Dependencies

- Minecraft 1.21.1 (stable)
- Fabric API (latest)
- Spark profiler (for validation)

### Research Dependencies

All research already available from Epic 01:
- `placement-algorithm.md` - Algorithm documentation
- `code-references.md` - Exact classes/methods
- `test-procedure.md` - Testing methodology
- `decision-matrix.md` - Why spacing multiplier (ROI 10.0)

---

## Success Criteria (Epic-Level)

Epic 03 is considered **COMPLETE** when:

1. **Spacing multiplier applied** to all structure placement calculations
2. **Performance validated**: 50-80% improvement measured (Spark profiler)
3. **Placement reduction validated**: 2,600 → ~650 placements
4. **Memory improvement validated**: 50% → 20-30% RAM usage
5. **Compatibility validated**: Works with 15+ structure mods
6. **User validated**: User confirms worldgen feels faster
7. **No regressions**: No crashes, errors, or compatibility issues
8. **Ready for Epic 04**: User approves moving to mod compatibility docs

**Definition of Done**:
- All acceptance criteria met
- All tests passed (automated + manual)
- User approves performance improvement
- Code reviewed and merged
- Ready to document compatibility with existing mods (Epic 04)

---

## Risk Assessment

### Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Can't find correct injection point | Low | High | Use Epic 01 code references, test thoroughly |
| Multiplier doesn't apply to all structures | Medium | High | Test with 15+ mods, verify all placement types |
| Performance improvement less than expected | Low | Medium | Epic 01 math verified, 95% confident in target |
| Compatibility issues with other mods | Low | Medium | Use defensive coding, test with common mods |
| Save incompatibility (existing worlds break) | Very Low | High | Only affects new chunks, existing structures safe |

**Overall risk level**: **LOW** - Epic 01 research validated approach, config system ready, clear implementation path

---

## Timeline Estimate

| Task | Estimated Time |
|------|----------------|
| Research injection point (use Epic 01 docs) | 1-2 hours |
| Implement spacing multiplier mixin | 2-3 hours |
| Test with vanilla structures | 1 hour |
| Test with modded structures (15+ mods) | 2-3 hours |
| Performance validation (Spark profiler) | 1-2 hours |
| User validation and feedback | 1 hour |
| Bug fixes and polish | 1-2 hours |
| **Total** | **9-14 hours** |

**Target completion**: 2-3 days (including user validation)

---

## References

- **Epic 01 EXECUTIVE-SUMMARY.md**: PRIMARY BOTTLENECK = STRUCTURE_START (40-60%)
- **Epic 01 decision-matrix.md**: Spacing multiplier = ROI 10.0 (highest)
- **Epic 01 placement-algorithm.md**: Complete algorithm documentation
- **Epic 01 code-references.md**: Exact classes and methods to hook
- **Epic 01 test-procedure.md**: Reproducible testing methodology
- **Epic 02 plan.md**: Config system implementation (multiplier stored)
- **minecraft-performance-structure skill**: Performance optimization patterns

---

## Questions for User Validation

Before proceeding with Epic 03 implementation, please confirm:

1. **Timing**: Ready to implement now? (Epic 02 validated ✅)
2. **Scope**: Is 50-80% improvement sufficient? Or target higher?
3. **Trade-off**: Comfortable with structures 2x further apart? (Balanced preset)
4. **Testing**: Should we test with all 15+ structure mods? Or subset?
5. **Release**: Complete Epic 03 before Epic 04 (mod compatibility docs)? (Recommended: Yes)

**Recommendation**: Proceed with Epic 03 implementation now. Config system validated, research complete, clear path forward.

---

**Status**: PLANNING - Awaiting user approval to proceed with `/create_plan`
