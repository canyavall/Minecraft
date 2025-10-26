# Epic 01: Understanding Structure Search Bottleneck - Tasks

**Epic**: Understanding Structure Search Bottleneck
**Status**: READY
**Created**: 2025-10-25
**Root Cause**: RC#1 - Structure Search Space Explosion

---

## Task Overview

**Total Tasks**: 8
**Status Breakdown**:
- TODO: 7
- IN_PROGRESS: 0
- COMPLETED: 1

**Progress**: 12.5% (1/8 completed)

---

## Phase 1: Research & Understanding (Foundation)

### TASK-001: [COMPLETED] Document Structure Placement Algorithm

**Agent**: research-agent
**Priority**: Critical
**Estimated Effort**: 2-3 hours
**Feature**: Feature 1 - Algorithm Understanding

**Goal**:
Create comprehensive documentation explaining how Minecraft determines "should this structure place here?" by analyzing the placement check pipeline.

**Requirements**:
- [ ] Read and analyze Minecraft source code for `RandomSpreadStructurePlacement`
- [ ] Review existing research files (chatgpt_answer.md, deepseek.md)
- [ ] Document the complete placement check sequence step-by-step
- [ ] Identify expensive operations vs cheap checks in the pipeline
- [ ] Create code flow diagram showing check sequence
- [ ] Explain when checks can be skipped or short-circuited
- [ ] Document biome filtering integration in placement pipeline

**Guidelines**:
- Follow research-methodology skill for research process
- Reference minecraft-modding skill for understanding vanilla systems
- Use minecraft-performance-structure skill for structure-specific knowledge
- Store all findings in `research/task-001/` folder within this epic
- You can paste research from other AIs into this folder for comparison

**Acceptance Criteria**:
- [ ] Research document clearly explains placement algorithm for non-experts
- [ ] Code flow diagram visually shows check sequence
- [ ] Expensive vs cheap operations are identified and justified
- [ ] Document includes code references with line numbers
- [ ] Explains how spacing/separation parameters affect checks
- [ ] User can read document and understand "why 569 structures = slow"

**Resources**:
- Existing research: `.claude/temp/chatgpt_answer.md`
- Existing research: `.claude/temp/deepseek.md`
- Existing research: `.claude/research/structure-performance-bottleneck.md`
- Minecraft source: Decompiled via Fabric (use Read tool on generated sources)

**Dependencies**: None (foundation task)

**Deliverable**: `research/task-001/structure-placement-algorithm.md` (primary research document)

**Research Folder**: `research/task-001/`
- Main findings: `structure-placement-algorithm.md`
- External research: You can paste research from ChatGPT, DeepSeek, etc. here
- Code samples: Any extracted code snippets
- Diagrams: Flow diagrams, visualizations

**Notes**:
- This is pure research, no code changes needed
- Focus on understanding, not optimizing
- Document should guide Epic 02-08 decisions
- User may paste research from other AIs into task-001 folder

---

### TASK-002: [TODO] Analyze Existing Profiling Data

**Agent**: research-agent
**Priority**: Critical
**Estimated Effort**: 1-2 hours
**Feature**: Feature 2 - CPU Cost Profiling

**Goal**:
Extract and document performance metrics from existing Spark profiler reports to understand actual CPU cost of structure placement checks.

**Requirements**:
- [ ] Locate Spark profiler report from previous testing session
- [ ] Identify time spent in structure placement methods
- [ ] Extract per-method timing data (% of total time, milliseconds)
- [ ] Identify top 10 most expensive methods in placement pipeline
- [ ] Calculate total time spent in STRUCTURE_START stage
- [ ] Compare time distribution across different placement phases
- [ ] Document which structures appear most frequently in profiler

**Guidelines**:
- Follow performance-testing skill for profiling analysis
- Use minecraft-performance-general skill for understanding profiler output
- Cross-reference with ROOT-CAUSE-ANALYSIS.md findings
- Store findings in `research/task-002/` folder within this epic
- You can paste profiling data from other tools into this folder

**Acceptance Criteria**:
- [ ] Document contains concrete numbers (ms, %, time per check)
- [ ] Top 10 most expensive methods identified with justification
- [ ] Total STRUCTURE_START time calculated and documented
- [ ] Profiling data clearly shows bottleneck locations
- [ ] Data presented in readable format (tables, charts if applicable)
- [ ] Findings reference specific Spark report sections

**Resources**:
- Previous Spark profiler reports (if saved)
- Existing research: `.claude/temp/ROOT-CAUSE-ANALYSIS.md`
- Existing research: `.claude/research/performance-test-results.md`
- Spark documentation for interpreting reports

**Dependencies**: None (can run in parallel with TASK-001)

**Deliverable**: `research/task-002/profiling-results.md` (primary analysis document)

**Research Folder**: `research/task-002/`
- Main findings: `profiling-results.md`
- Spark reports: Raw profiler output files
- External analysis: Paste profiling analysis from other AIs
- Charts/graphs: Performance visualizations

**Notes**:
- If Spark report not saved, may need to re-run profiling session
- Focus on worldgen-specific profiling, not tick profiling
- Document baseline before any optimizations
- User may paste profiling data from external tools

---

## Phase 2: Measurement & Testing

### TASK-003: [TODO] Set Up Vanilla Comparison Test Environment

**Agent**: implementation-agent
**Priority**: High
**Estimated Effort**: 1-2 hours
**Feature**: Feature 3 - Vanilla vs Modded Comparison

**Goal**:
Create three test world configurations (vanilla 34, lightly modded ~100, heavily modded 569) to enable performance comparison.

**Requirements**:
- [ ] Document current mod list with structure counts
- [ ] Create test scenario 1: Vanilla only (34 structures)
- [ ] Create test scenario 2: Lightly modded (~100 structures)
- [ ] Create test scenario 3: Heavily modded (569 structures - current)
- [ ] Ensure consistent world seed across all scenarios
- [ ] Ensure consistent Minecraft settings (render distance, etc.)
- [ ] Document test procedure for consistent results
- [ ] Verify PlacementTracker works in all scenarios

**Guidelines**:
- Follow performance-testing skill for test scenario creation
- Use defensive-programming skill for validation
- Document test setup in `research/task-003/` folder within this epic
- No optimization code - only measurement setup

**Acceptance Criteria**:
- [ ] Three distinct test scenarios documented
- [ ] World seed and settings documented for reproducibility
- [ ] Structure count verified for each scenario (use `/xeenaa stats`)
- [ ] Test procedure clearly written for user execution
- [ ] PlacementTracker confirmed functional in all scenarios
- [ ] User can execute tests by following documented procedure

**Resources**:
- Existing tracking: `PlacementTracker.java`
- Existing command: `XeenaaCommand.java` (for `/xeenaa stats`)
- Existing research: `.claude/temp/structure-mods-test-list.md`

**Dependencies**: None (setup task)

**Deliverable**: `research/task-003/test-setup.md` (test environment documentation)

**Research Folder**: `research/task-003/`
- Test setup: `test-setup.md`
- Mod lists: Documentation of which mods for each scenario
- Screenshots: Visual confirmation of test setup
- External configs: Paste mod configurations from other sources

**Notes**:
- User will manually run tests, not automated
- Ensure consistent testing conditions for valid comparison
- Document which mods to disable for each scenario

---

### TASK-004: [TODO] Execute Vanilla vs Modded Performance Tests

**Agent**: research-agent
**Priority**: High
**Estimated Effort**: 2-3 hours
**Feature**: Feature 3 - Vanilla vs Modded Comparison

**Goal**:
Run performance tests across all three scenarios and collect comprehensive metrics for comparison.

**Requirements**:
- [ ] Execute test for Scenario 1 (vanilla 34 structures)
- [ ] Execute test for Scenario 2 (lightly modded ~100 structures)
- [ ] Execute test for Scenario 3 (heavily modded 569 structures)
- [ ] Collect metrics: chunk generation time, placement rate, structure count
- [ ] Run Spark profiler for each scenario
- [ ] Document MSPT (milliseconds per tick) for each scenario
- [ ] Measure memory usage during worldgen for each scenario
- [ ] Record total structures placed per minute for each scenario

**Guidelines**:
- Follow performance-testing skill for measurement methodology
- Use consistent test duration (e.g., 10 minutes of worldgen per scenario)
- Document exact test conditions (date, time, system specs)
- Store raw data and analysis in `research/task-004/` folder within this epic

**Acceptance Criteria**:
- [ ] All three scenarios tested with complete data
- [ ] Metrics table comparing 34 vs 100 vs 569 structures
- [ ] Percentage performance degradation calculated (e.g., "569 = 4x slower")
- [ ] Spark profiler data collected for each scenario
- [ ] Clear identification of which metric degrades most (time, memory, or rate)
- [ ] Statistical validity confirmed (multiple test runs if needed)

**Resources**:
- Test setup from TASK-003
- Spark Profiler mod
- PlacementTracker for structure count
- Performance-testing skill for methodology

**Dependencies**:
- TASK-003 (test environment setup must be complete)

**Deliverable**: `research/task-004/performance-comparison.md` (comprehensive test results)

**Research Folder**: `research/task-004/`
- Main findings: `performance-comparison.md`
- Raw test data: CSV/JSON files with metrics
- Spark reports: Profiler output for each scenario
- External benchmarks: Paste performance data from other users/AIs
- Screenshots: Evidence of test execution

**Notes**:
- User will execute tests manually, agent documents results
- May require multiple test runs for statistical validity
- Save Spark reports for each scenario

---

### TASK-005: [TODO] Add Custom Timing Instrumentation (If Needed)

**Agent**: implementation-agent
**Priority**: Medium
**Estimated Effort**: 2-3 hours
**Feature**: Feature 2 - CPU Cost Profiling

**Goal**:
If Spark profiler data is insufficient, add custom timing hooks to measure per-structure-check cost precisely.

**Requirements**:
- [ ] Determine if Spark data from TASK-002/TASK-004 is sufficient
- [ ] If insufficient, identify placement check methods to instrument
- [ ] Add timing measurements using `System.nanoTime()` or similar
- [ ] Log timing data with structure name and check type
- [ ] Calculate average cost per placement check
- [ ] Identify structures with highest per-check cost
- [ ] Store timing results in structured format (CSV or JSON)
- [ ] Ensure instrumentation can be disabled for production

**Guidelines**:
- Follow coding-standards skill for instrumentation code
- Use logging-strategy skill for timing log messages
- Follow minecraft-modding skill for mixin placement
- Use defensive-programming skill for safe instrumentation
- Instrument via mixins, not direct code changes

**Acceptance Criteria**:
- [ ] Timing instrumentation added only if Spark insufficient
- [ ] Per-check cost measured in microseconds or nanoseconds
- [ ] Top 10 most expensive structures by check cost identified
- [ ] Instrumentation does not affect performance (negligible overhead)
- [ ] Timing data stored in `research/task-005/` folder
- [ ] Instrumentation can be toggled on/off via config or flag

**Resources**:
- Existing mixins in `src/main/java/com/canya/xeenaa_structure_manager/mixin/`
- Profiling results from TASK-002
- Minecraft source code for placement methods

**Dependencies**:
- TASK-002 (determine if Spark data sufficient)
- TASK-004 (may provide additional profiling data)

**Deliverable**: `research/task-005/instrumentation.md` (timing code documentation)

**Research Folder**: `research/task-005/`
- Instrumentation design: `instrumentation.md`
- Timing results: Raw timing data files
- Code snippets: Mixin code examples
- External tools: Paste timing data from JFR, async-profiler, etc.

**Notes**:
- **Conditional task**: Only execute if TASK-002/TASK-004 profiling insufficient
- This is measurement code, not optimization code
- Remove or disable before Epic 02

---

## Phase 3: Analysis & Strategy

### TASK-006: [TODO] Analyze Root Cause Contribution

**Agent**: research-agent
**Priority**: Critical
**Estimated Effort**: 2-3 hours
**Feature**: Feature 4 - Optimization Strategy Identification

**Goal**:
Determine which of the 8 identified root causes contributes MOST to performance problems using gathered data.

**Requirements**:
- [ ] Review all 8 root causes from ROOT-CAUSE-ANALYSIS.md
- [ ] Map profiling data to each root cause
- [ ] Calculate percentage contribution of each root cause to total overhead
- [ ] Identify primary bottleneck (highest % contribution)
- [ ] Identify secondary bottlenecks (significant but lower %)
- [ ] Assess interactions between root causes (e.g., RC#1 amplifies RC#2)
- [ ] Document findings with supporting data and charts

**Guidelines**:
- Follow research-methodology skill for analysis process
- Use performance-testing skill for interpreting metrics
- Reference minecraft-performance-structure skill for context
- Store analysis in `research/task-006/` folder within this epic

**Acceptance Criteria**:
- [ ] All 8 root causes analyzed with data-backed assessment
- [ ] Primary bottleneck clearly identified with percentage contribution
- [ ] Root causes ranked by impact (High/Medium/Low)
- [ ] Root cause interactions documented (if any)
- [ ] Supporting data referenced from profiling and testing tasks
- [ ] User can read analysis and understand "fix this first"

**Resources**:
- Root cause list: `.claude/temp/ROOT-CAUSE-ANALYSIS.md`
- Profiling data: from TASK-002 and TASK-005
- Comparison data: from TASK-004
- Algorithm understanding: from TASK-001

**Dependencies**:
- TASK-001 (algorithm understanding)
- TASK-002 (profiling analysis)
- TASK-004 (comparison testing)
- TASK-005 (custom instrumentation, if applicable)

**Deliverable**: `research/task-006/root-cause-analysis.md` (primary bottleneck identification)

**Research Folder**: `research/task-006/`
- Main analysis: `root-cause-analysis.md`
- Data tables: Contribution percentages by root cause
- Charts: Visual representation of bottleneck analysis
- External research: Paste root cause analysis from other AIs
- Supporting evidence: Links to profiling data from other tasks

**Notes**:
- This determines Epic 02-08 priority order
- Focus on data-driven conclusions, not assumptions
- Be prepared to challenge initial hypotheses

---

### TASK-007: [TODO] Create Optimization Decision Matrix

**Agent**: research-agent
**Priority**: Critical
**Estimated Effort**: 1-2 hours
**Feature**: Feature 4 - Optimization Strategy Identification

**Goal**:
Evaluate each potential optimization (Epic 02-08) using Impact/Effort/Risk matrix to determine recommended priority order.

**Requirements**:
- [ ] Create decision matrix for Epic 02-08
- [ ] For each epic, assess Impact (High/Medium/Low) based on profiling data
- [ ] For each epic, estimate Effort (High/Medium/Low) based on complexity
- [ ] For each epic, evaluate Risk (High/Medium/Low) based on mod compatibility
- [ ] Calculate overall priority score (Impact + Effort + Risk)
- [ ] Rank epics by priority score
- [ ] Provide clear recommendation: "Start with Epic XX because..."
- [ ] Justify recommendation with data from TASK-006

**Guidelines**:
- Follow research-methodology skill for decision-making process
- Use performance-testing skill for impact assessment
- Reference minecraft-modding skill for risk assessment
- Document matrix in `research/task-007/` folder within this epic

**Acceptance Criteria**:
- [ ] Decision matrix includes all epics (02-08)
- [ ] Impact scores justified with profiling data
- [ ] Effort estimates realistic and documented
- [ ] Risk assessments consider mod compatibility and Minecraft version changes
- [ ] Clear priority ranking with #1 recommendation
- [ ] Recommendation includes data-backed justification (not intuition)
- [ ] User can confidently proceed with recommended epic

**Resources**:
- Epic roadmap: `.claude/epics/CURRENT_EPIC.md`
- Root cause analysis: from TASK-006
- Profiling data: from TASK-002/TASK-005
- Comparison data: from TASK-004

**Dependencies**:
- TASK-006 (root cause contribution analysis)

**Deliverable**: `research/task-007/decision-matrix.md` (epic priority recommendations)

**Research Folder**: `research/task-007/`
- Decision matrix: `decision-matrix.md`
- Impact/Effort/Risk tables: Detailed scoring
- Priority ranking: Final epic order recommendation
- External opinions: Paste optimization recommendations from other AIs
- Justifications: Data-backed reasoning for each score

**Notes**:
- This is the key deliverable guiding Epic 02+ order
- Be objective, let data drive recommendations
- Consider both quick wins and high-impact long-term fixes

---

### TASK-008: [TODO] Synthesize Final Research Report

**Agent**: research-agent
**Priority**: High
**Estimated Effort**: 1-2 hours
**Feature**: All Features - Epic Summary

**Goal**:
Create executive summary synthesizing all Epic 01 findings for user review and Epic 02+ planning.

**Requirements**:
- [ ] Summarize algorithm understanding from TASK-001
- [ ] Summarize profiling findings from TASK-002/TASK-005
- [ ] Summarize comparison results from TASK-004
- [ ] Include optimization strategy from TASK-006/TASK-007
- [ ] Answer all 5 epic questions from requirements.md
- [ ] Provide clear recommendation for next steps
- [ ] Include references to all detailed research documents
- [ ] Create executive summary (1-2 pages max)

**Guidelines**:
- Follow research-methodology skill for executive summary format
- Keep summary concise and actionable
- Use clear language for non-technical readers
- Store summary in `research/task-008/` folder within this epic

**Acceptance Criteria**:
- [ ] Executive summary answers all 5 epic questions
- [ ] Clear answer to "What should we optimize first and why?"
- [ ] Concrete numbers from profiling and testing included
- [ ] Recommendation justified with data references
- [ ] Summary is 1-2 pages (not overwhelming)
- [ ] User can read summary and approve Epic 02 direction
- [ ] All detailed research documents linked for deep dives

**Resources**:
- All research documents from TASK-001 through TASK-007
- Epic requirements: `.claude/epics/01-understanding-structure-search/requirements.md`

**Dependencies**:
- TASK-001 (algorithm documentation)
- TASK-002 (profiling analysis)
- TASK-004 (comparison testing)
- TASK-006 (root cause analysis)
- TASK-007 (decision matrix)

**Deliverable**: `research/task-008/epic-01-summary.md` (final comprehensive report)

**Research Folder**: `research/task-008/`
- Executive summary: `epic-01-summary.md`
- Presentation slides: Visual summary for stakeholders
- Quick reference: One-page cheat sheet of key findings
- External summaries: Paste summaries from other AIs for comparison

**Notes**:
- This is the final deliverable for Epic 01
- User validates this before Epic 02 begins
- Should clearly guide Epic 02-08 implementation

---

## Task Dependencies Diagram

```
PHASE 1: Research & Understanding
TASK-001 (Algorithm Docs) ─┐
TASK-002 (Profiling Analysis) ─┤
                                ├─→ TASK-006 (Root Cause Analysis) ─→ TASK-007 (Decision Matrix) ─→ TASK-008 (Final Report)
PHASE 2: Measurement            │
TASK-003 (Test Setup) ─→ TASK-004 (Run Tests) ─┤
TASK-005 (Custom Timing - conditional) ────────┘
```

**Critical Path**: TASK-001 → TASK-003 → TASK-004 → TASK-006 → TASK-007 → TASK-008

---

## Notes

**Epic Philosophy**: Research first, optimize later. This epic ONLY gathers data and understanding.

**No Code Delivery**: Only research documents, measurement tools, and analysis. No performance fixes yet.

**User Validation**: User will manually execute tests (TASK-004) and validate research findings before Epic 02.

**Conditional Task**: TASK-005 only executes if Spark profiling insufficient.

**Next Epic**: Determined by TASK-007 decision matrix. Expected: Epic 02 (STRUCTURE_START) or Epic 04 (Placement Rate).

---

**Ready to execute?** Run `/next` to start TASK-001!
