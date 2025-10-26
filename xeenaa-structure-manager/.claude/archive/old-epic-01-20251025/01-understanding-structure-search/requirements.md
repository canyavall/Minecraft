# Epic 01: Understanding Structure Search Bottleneck

**Created**: 2025-10-25
**Status**: PLANNING
**Priority**: Critical
**Root Cause**: RC#1 - Structure Search Space Explosion (17x more placement checks per chunk)

---

## Business Value

**Problem Statement**:
Players with 13+ structure mods (569 total structures) experience severe performance issues: lag spikes, slow chunk generation, and rendering struggles. We need to understand WHY this happens before we can fix it.

**Value Delivered**:
This epic provides the **knowledge foundation** for all future optimization epics. By thoroughly understanding and measuring the bottleneck, we can make informed decisions about which optimizations will actually help players.

**Success Definition**:
We can confidently answer: "Which of the 8 root causes contributes MOST to performance problems?" and "What optimization strategy will give players the best performance improvement?"

---

## Overview

This is a **RESEARCH AND MEASUREMENT** epic, not an implementation epic. We will:

1. **Understand** how Minecraft's structure placement checking algorithm works
2. **Measure** the actual CPU cost with profiling tools (Spark)
3. **Compare** vanilla (34 structures) vs modded (569 structures) performance
4. **Document** findings to guide Epic 02's optimization strategy

**No code changes** will be made to fix performance yet - only measurement and analysis code.

---

## Features

### Feature 1: Algorithm Understanding
**Description**: Complete documentation of how Minecraft determines "should this structure place here?"

**Research Questions**:
- What is the exact sequence of checks for each structure?
- How does `RandomSpreadStructurePlacement` work?
- What mathematical calculations happen per check?
- Where does biome filtering fit in the pipeline?

**Success Criteria**:
- [ ] Research document explains placement algorithm step-by-step
- [ ] Code flow diagram showing check sequence
- [ ] Identification of expensive operations vs cheap checks
- [ ] Understanding of when checks can be skipped

**Deliverable**: `.claude/research/structure-placement-algorithm.md`

---

### Feature 2: CPU Cost Profiling
**Description**: Measure actual time spent in structure placement checks

**Profiling Approach**:
- Use Spark Profiler (already running during our test)
- Add custom timing instrumentation if needed
- Measure per-structure-check cost
- Identify which structures are most expensive

**Success Criteria**:
- [ ] Spark profiler report analyzed and documented
- [ ] Cost per check measured (microseconds or milliseconds)
- [ ] Top 10 most expensive structures identified
- [ ] Total time spent in placement checks per chunk calculated

**Deliverable**: `.claude/research/profiling-results.md` with concrete numbers

---

### Feature 3: Vanilla vs Modded Comparison
**Description**: Quantify the performance difference between 34 and 569 structures

**Comparison Metrics**:
- Chunk generation time (ms per chunk)
- STRUCTURE_START stage time (from research)
- Placement check count per chunk
- Memory usage during worldgen
- Placement rate (structures/sec)

**Test Scenarios**:
1. **Vanilla** - 34 structures only
2. **Lightly Modded** - ~100 structures (remove big mods)
3. **Heavily Modded** - 569 structures (current setup)

**Success Criteria**:
- [ ] Measured data for all 3 scenarios
- [ ] Clear comparison showing 34 vs 569 performance impact
- [ ] Identified which metric degrades most (time, memory, or rate)
- [ ] Percentage performance loss documented (e.g., "569 structures = 4x slower worldgen")

**Deliverable**: `.claude/research/vanilla-vs-modded-comparison.md`

---

### Feature 4: Optimization Strategy Identification
**Description**: Use gathered data to determine which Epic (02-08) should be tackled first

**Analysis Questions**:
- Is the bottleneck in STRUCTURE_START stage? (Epic 02)
- Is it jigsaw assembly O(n²) checks? (Epic 03)
- Is it excessive placement attempts? (Epic 04)
- Is it memory/GC pressure? (Epic 05)
- Is it biome checks? (Epic 06)

**Decision Matrix**:
For each potential optimization:
- **Impact**: High/Medium/Low (based on profiling data)
- **Effort**: High/Medium/Low (estimated complexity)
- **Risk**: High/Medium/Low (chance of breaking things)
- **Priority**: Impact + Effort + Risk = Overall score

**Success Criteria**:
- [ ] Ranked list of optimizations by potential impact
- [ ] Clear recommendation: "Start with Epic XX because..."
- [ ] Data-backed justification for priority order
- [ ] Risk assessment for each approach

**Deliverable**: `.claude/research/optimization-strategy.md`

---

## Technical Considerations

### Existing Measurement Tools

**We already have** (from old epics - keeping these!):
- ✅ PlacementTracker - Records every structure placement
- ✅ `/xeenaa stats` - Shows statistics and placement rates
- ✅ Spark Profiler - CPU profiling tool (already used in test)
- ✅ Performance test logs - 2,600 placements documented

**We may need to add**:
- Custom timing instrumentation in placement code
- Memory profiling hooks
- Comparison world generation (vanilla vs modded)

### Research Resources

**Data Sources**:
- `.claude/temp/chatgpt_answer.md` - Expert analysis
- `.claude/temp/deepseek.md` - Technical breakdown
- `.claude/research/structure-performance-bottleneck.md` - Prior research
- `.claude/research/performance-test-results.md` - Test data
- `.claude/temp/ROOT-CAUSE-ANALYSIS.md` - 8 bottlenecks identified

**External Resources**:
- Minecraft source code (decompiled via Fabric)
- Spark profiler reports
- Community knowledge (mods like Structure Layout Optimizer)

---

## Dependencies

**Prerequisites**:
- Spark Profiler mod installed ✅
- PlacementTracker system functional ✅
- Root cause analysis complete ✅
- Performance test data collected ✅

**Blocks**:
- None - this is a research epic, fully independent

---

## Out of Scope

**NOT included in Epic 01**:
- ❌ Writing optimization code (that's Epic 02-08)
- ❌ Fixing config bugs (not needed for research)
- ❌ Building classification system (premature)
- ❌ Creating multiplier system (wait for data)
- ❌ User-facing features (research only)

**Focus**: Understand and measure, don't fix yet.

---

## Acceptance Criteria (Epic-Level)

Epic 01 is **COMPLETE** when:

- [] Complete algorithm documentation exists (Feature 1)
- [  ] Profiling data with concrete numbers documented (Feature 2)
- [] Vanilla vs modded comparison shows clear performance difference (Feature 3)
- [] Optimization strategy document recommends next epic with data justification (Feature 4)
- [] All research documents reviewed and validated by user
- [] Clear answer to: "What should we optimize first and why?"

**Validation Method**: User reviews research documents and confirms findings make sense

---

## Next Steps After Epic 01

**If findings show**:
- STRUCTURE_START is #1 bottleneck → Epic 02 next
- Jigsaw assembly is #1 bottleneck → Epic 03 next
- Placement rate is #1 bottleneck → Epic 04 next
- Multiple equal bottlenecks → Prioritize by effort/impact ratio

**Epic 02+ will**:
- Reference Epic 01 findings
- Target specific measured bottleneck
- Have clear success metrics from Epic 01 data

---

## Timeline Estimate

**Research Phase**: 2-4 hours
- Algorithm documentation: 1-2 hours
- Profiling analysis: 30-60 min
- Vanilla comparison testing: 1-2 hours

**Total Epic Effort**: 4-8 hours (no coding, pure research/measurement)

---

**Tags**: #research #measurement #understanding #root-cause-01
**Type**: Knowledge Epic (no code delivery)
**Next Epic Depends On**: Findings from this epic determine Epic 02-08 priority order
