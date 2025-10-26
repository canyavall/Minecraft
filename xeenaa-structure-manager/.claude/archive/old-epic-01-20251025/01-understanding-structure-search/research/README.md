# Epic 01: Research Organization

**Epic**: Understanding Structure Search Bottleneck
**Research Folder Structure**: Task-based organization

---

## Folder Structure

Each task has its own research folder:

```
research/
├── README.md (this file)
├── task-001/  # Algorithm Understanding
├── task-002/  # Profiling Analysis
├── task-003/  # Test Environment Setup
├── task-004/  # Performance Comparison Testing
├── task-005/  # Custom Timing Instrumentation (conditional)
├── task-006/  # Root Cause Analysis
├── task-007/  # Optimization Decision Matrix
└── task-008/  # Final Research Report
```

---

## How to Use

### For Claude Code (implementation-agent/research-agent)

When completing a task:
1. Create your primary research document in the corresponding `task-XXX/` folder
2. Name it according to the deliverable specified in `tasks.md`
3. Add supporting files (charts, data, code snippets) to the same folder

**Example** (TASK-001):
```
research/task-001/
├── structure-placement-algorithm.md  # Primary deliverable
├── code-flow-diagram.png             # Supporting visual
├── minecraft-source-excerpts.java    # Code snippets
└── notes.md                          # Working notes
```

### For User (pasting external research)

You can paste research from other AIs (ChatGPT, DeepSeek, Gemini, etc.) into the appropriate task folder:

**Example** (TASK-001):
```
research/task-001/
├── structure-placement-algorithm.md  # Claude's research
├── chatgpt-analysis.md               # ChatGPT's research (pasted by user)
├── deepseek-breakdown.md             # DeepSeek's research (pasted by user)
└── comparison-notes.md               # User's notes comparing all sources
```

---

## Task Folder Contents

### task-001/ - Algorithm Understanding
**Primary Deliverable**: `structure-placement-algorithm.md`

**Can Also Contain**:
- External research from other AIs
- Code flow diagrams
- Minecraft source code excerpts
- Algorithm pseudocode
- Notes and observations

---

### task-002/ - Profiling Analysis
**Primary Deliverable**: `profiling-results.md`

**Can Also Contain**:
- Spark profiler reports (raw output)
- External profiling analysis
- Performance charts/graphs
- Method timing tables
- CPU flamegraphs

---

### task-003/ - Test Environment Setup
**Primary Deliverable**: `test-setup.md`

**Can Also Contain**:
- Mod lists for each scenario
- Configuration files
- Screenshots of test setup
- External test configurations
- World seed documentation

---

### task-004/ - Performance Comparison Testing
**Primary Deliverable**: `performance-comparison.md`

**Can Also Contain**:
- Raw test data (CSV/JSON)
- Spark reports for each scenario
- Performance charts
- External benchmark data
- Screenshots of test execution
- Statistical analysis

---

### task-005/ - Custom Timing Instrumentation (Conditional)
**Primary Deliverable**: `instrumentation.md`

**Can Also Contain**:
- Mixin code snippets
- Timing data files
- JFR/async-profiler reports
- External timing tools data
- Instrumentation design diagrams

**Note**: Only execute this task if TASK-002/TASK-004 profiling is insufficient

---

### task-006/ - Root Cause Analysis
**Primary Deliverable**: `root-cause-analysis.md`

**Can Also Contain**:
- Data tables (contribution percentages)
- Bottleneck analysis charts
- External root cause analysis
- Supporting evidence links
- Interaction diagrams (RC#1 amplifies RC#2, etc.)

---

### task-007/ - Optimization Decision Matrix
**Primary Deliverable**: `decision-matrix.md`

**Can Also Contain**:
- Impact/Effort/Risk tables
- Priority ranking spreadsheets
- External optimization recommendations
- Justification documents
- Trade-off analysis

---

### task-008/ - Final Research Report
**Primary Deliverable**: `epic-01-summary.md`

**Can Also Contain**:
- Presentation slides
- One-page executive summary
- External AI summaries (for comparison)
- Quick reference cheat sheets
- Stakeholder communication materials

---

## Benefits of This Organization

### ✅ **Clean Separation**
- Each task's research is isolated
- Easy to find specific research
- No confusion about which task produced which document

### ✅ **External Research Integration**
- User can paste research from ChatGPT, DeepSeek, etc.
- Multiple sources can be compared side-by-side
- Claude's research + external research in same folder

### ✅ **Flexible Content**
- Not limited to single file per task
- Can add supporting files (diagrams, data, code)
- Natural place for work-in-progress notes

### ✅ **Epic-Specific**
- Research stays with the epic
- Epic 02 will have its own `research/task-XXX/` structure
- Easy to archive entire epic when done

---

## Example Workflow

### Claude Code Completes TASK-001

1. **Creates primary document**:
   ```
   research/task-001/structure-placement-algorithm.md
   ```

2. **Adds supporting files**:
   ```
   research/task-001/code-flow-diagram.png
   research/task-001/minecraft-source-excerpts.java
   ```

3. **Marks task complete** in `tasks.md`

---

### User Pastes External Research

1. **User asks ChatGPT**: "Explain how Minecraft structure placement works"

2. **User copies response** to:
   ```
   research/task-001/chatgpt-algorithm-explanation.md
   ```

3. **User compares** Claude's research with ChatGPT's research

4. **User creates synthesis**:
   ```
   research/task-001/combined-analysis.md
   ```

---

## Best Practices

### For Claude Code
- ✅ Use descriptive filenames
- ✅ Include "last updated" date in documents
- ✅ Reference other task folders when needed (e.g., "See task-002/profiling-results.md")
- ✅ Keep primary deliverable clearly named
- ❌ Don't duplicate research across multiple task folders

### For Users
- ✅ Clearly label external research (e.g., "chatgpt-", "deepseek-")
- ✅ Include date and source when pasting external research
- ✅ Add comparison notes if using multiple sources
- ✅ Feel free to reorganize within task folders
- ❌ Don't delete Claude's primary deliverable

---

## Migration from Old System

**Old system**: All research in `.claude/research/` (project-global)

**New system**: Epic-specific `research/task-XXX/` folders

**Benefits**:
- Epic 01 research doesn't mix with Epic 02 research
- Easier to archive completed epics
- Clear task-to-research mapping
- Better support for pasting external research

**Old global research** (`.claude/research/`) can still be referenced when needed, but new research goes in epic folders.

---

**Last Updated**: 2025-10-25
**Epic**: 01 - Understanding Structure Search Bottleneck
