---
name: epic-research-organization
description: Epic-specific research organization pattern. Research files are stored in task-specific folders within each epic ({{project}}/.claude/epics/##-epic-name/research/task-XXX/). Supports pasting external research from other AIs.
allowed-tools: [Write, Read, Glob]
---

# Epic Research Organization Skill

Epic-specific research storage pattern ensuring clean organization and support for external research integration.

## Core Principle

**Research is organized by EPIC and TASK, not globally.**

```
OLD (deprecated):
.claude/research/some-research.md  ❌ Global, mixed epics

NEW (current):
.claude/epics/01-epic-name/research/task-001/algorithm-research.md  ✅ Epic + task specific
```

---

## Folder Structure

### Epic Research Folder

Each epic has a `research/` folder with task-specific subfolders:

```
{{project}}/.claude/epics/##-epic-name/
├── requirements.md           # Business requirements
├── tasks.md                  # Technical tasks
└── research/                 # Epic research
    ├── README.md             # Organization documentation
    ├── task-001/             # Research for TASK-001
    │   ├── main-research.md  # Primary deliverable
    │   ├── chatgpt-analysis.md   # External research (pasted)
    │   ├── diagrams/         # Supporting visuals
    │   └── data/             # Raw data files
    ├── task-002/             # Research for TASK-002
    │   └── profiling-results.md
    └── task-008/             # Research for TASK-008 (final summary)
        └── epic-summary.md
```

### Task Folder Contents

Each `task-XXX/` folder contains:

**Primary Deliverable**:
- The main research document specified in `tasks.md`
- Named according to task deliverable (e.g., `algorithm-research.md`)

**Supporting Files** (optional):
- Diagrams, charts, visualizations
- Code snippets, examples
- Raw data files (CSV, JSON)
- Work-in-progress notes

**External Research** (user-pasted):
- Research from ChatGPT, DeepSeek, Gemini, etc.
- Clearly labeled (e.g., `chatgpt-analysis.md`, `deepseek-breakdown.md`)
- User comparison notes

---

## Creating Research Files

### As research-agent or implementation-agent

When completing a task that requires research:

**1. Identify the correct folder**:
```
Current epic: Epic 01 - Understanding Structure Search
Current task: TASK-001 - Document Structure Placement Algorithm
Research folder: .claude/epics/01-understanding-structure-search/research/task-001/
```

**2. Create primary deliverable**:
```
File: .claude/epics/01-understanding-structure-search/research/task-001/structure-placement-algorithm.md
Content: Your research findings
```

**3. Add supporting files if needed**:
```
File: .claude/epics/01-understanding-structure-search/research/task-001/code-flow-diagram.png
File: .claude/epics/01-understanding-structure-search/research/task-001/minecraft-source-excerpts.java
```

**4. Reference in task completion**:
```markdown
## TASK-001: [COMPLETED] Document Structure Placement Algorithm

**Deliverable**: research/task-001/structure-placement-algorithm.md ✅

Supporting files:
- research/task-001/code-flow-diagram.png
- research/task-001/minecraft-source-excerpts.java
```

### File Naming Conventions

**Primary Deliverables**:
- Use descriptive names from `tasks.md`
- Examples: `algorithm-research.md`, `profiling-results.md`, `decision-matrix.md`

**Supporting Files**:
- Clear, descriptive names
- Examples: `code-flow-diagram.png`, `spark-report.txt`, `test-data.csv`

**External Research** (user-pasted):
- Prefix with source: `chatgpt-`, `deepseek-`, `gemini-`
- Examples: `chatgpt-algorithm-explanation.md`, `deepseek-performance-analysis.md`

---

## User Workflow (Pasting External Research)

Users can paste research from other AIs into task folders for comparison:

### Example: Comparing Multiple Sources

**User asks ChatGPT**: "How does Minecraft structure placement work?"

**User copies response** to:
```
.claude/epics/01-understanding-structure-search/research/task-001/chatgpt-algorithm-explanation.md
```

**User asks DeepSeek**: Same question

**User copies response** to:
```
.claude/epics/01-understanding-structure-search/research/task-001/deepseek-algorithm-breakdown.md
```

**User creates comparison**:
```
.claude/epics/01-understanding-structure-search/research/task-001/my-synthesis.md

Content:
- Claude's approach: Focused on code flow
- ChatGPT's approach: High-level algorithm
- DeepSeek's approach: Performance focus
- My conclusion: Combine all three perspectives
```

### Benefits

✅ **Multiple perspectives** - Compare Claude's research with external sources
✅ **Organized comparison** - All related research in one task folder
✅ **Clear attribution** - Easy to see which AI provided which insights
✅ **Flexible** - User can organize however they prefer within task folder

---

## Referencing Research

### Within Same Epic

**From tasks.md**:
```markdown
**Resources**:
- Algorithm research: `research/task-001/structure-placement-algorithm.md`
- Profiling data: `research/task-002/profiling-results.md`
```

**From another task's research**:
```markdown
See profiling data in ../task-002/profiling-results.md for performance metrics.
```

### Across Epics

**From Epic 02 referencing Epic 01**:
```markdown
Based on Epic 01 research findings:
See: .claude/epics/01-understanding-structure-search/research/task-006/root-cause-analysis.md
```

### From Global Research (Legacy)

**Old global research** can still be referenced:
```markdown
Previous research (pre-Epic 01):
See: .claude/research/structure-performance-bottleneck.md
```

---

## Epic README Template

Each epic's `research/` folder should have a `README.md`:

```markdown
# Epic [##]: [Epic Name] - Research Organization

**Epic**: [Epic Name]
**Research Folder Structure**: Task-based organization

---

## Folder Structure

research/
├── README.md (this file)
├── task-001/  # [Task Title]
├── task-002/  # [Task Title]
└── task-XXX/  # [Task Title]

---

## Task Folder Contents

### task-001/ - [Task Title]
**Primary Deliverable**: `[filename].md`

**Can Also Contain**:
- [List expected supporting files]
- External research from other AIs
- [Specific notes for this task]

[Repeat for each task]

---

**Last Updated**: [Date]
**Epic**: [##] - [Epic Name]
```

---

## Migration from Global Research

### Old Pattern (Deprecated)
```
.claude/research/
├── structure-placement-tracking.md
├── profiling-results.md
└── epic-01-summary.md
```

**Problems**:
- Mixed research from different epics
- Hard to know which epic a file belongs to
- Difficult to archive completed epics
- No support for task-specific organization

### New Pattern (Current)
```
.claude/epics/01-understanding-structure-search/research/
├── task-001/structure-placement-algorithm.md
├── task-002/profiling-results.md
└── task-008/epic-01-summary.md
```

**Benefits**:
- Epic-specific isolation
- Clear task-to-research mapping
- Easy to archive entire epic
- Task folders support external research

### Handling Legacy Research

**Keep old global research**:
- Don't delete `.claude/research/` folder
- Reference it when needed
- NEW research goes to epic folders

**Optional migration**:
- User can move relevant old research to epic folders
- Not required, just keep referencing old location

---

## Quality Checklist

Before marking a task complete:

- [ ] Primary deliverable created in `research/task-XXX/`
- [ ] File named according to `tasks.md` deliverable
- [ ] Supporting files organized in same folder
- [ ] Research is well-structured and readable
- [ ] References to other research use relative paths
- [ ] Task marked complete in `tasks.md` with deliverable path

---

## Common Patterns

### Research Task (research-agent)

```markdown
TASK-001: Document Algorithm

Steps:
1. Create research/task-001/ folder
2. Investigate algorithm
3. Write research/task-001/algorithm-research.md
4. Add diagrams to research/task-001/
5. Update tasks.md: "Deliverable: research/task-001/algorithm-research.md ✅"
```

### Implementation Task with Research (implementation-agent)

```markdown
TASK-005: Add Custom Timing Instrumentation

Steps:
1. Design instrumentation approach
2. Create research/task-005/instrumentation-design.md
3. Implement code
4. Document results in research/task-005/timing-results.md
5. Update tasks.md with both deliverables
```

### User Pasting External Research

```markdown
User workflow:
1. Ask ChatGPT for analysis
2. Copy response to research/task-001/chatgpt-analysis.md
3. Ask DeepSeek for comparison
4. Copy response to research/task-001/deepseek-analysis.md
5. Create research/task-001/comparison-notes.md
```

---

## Anti-Patterns (Don't Do This)

❌ **Global research files**:
```
.claude/research/epic-01-algorithm.md  # Wrong location
```

✅ **Epic-specific files**:
```
.claude/epics/01-epic/research/task-001/algorithm.md  # Correct
```

---

❌ **Mixed epic research**:
```
.claude/epics/01-epic/research/
├── epic-01-stuff.md
├── epic-02-stuff.md  # Wrong epic!
```

✅ **Separate epic research**:
```
.claude/epics/01-epic/research/task-001/stuff.md
.claude/epics/02-epic/research/task-001/stuff.md
```

---

❌ **Unclear file names**:
```
research/task-001/notes.md  # What kind of notes?
research/task-001/data.csv  # What data?
```

✅ **Descriptive file names**:
```
research/task-001/algorithm-research.md
research/task-001/placement-check-profiling-data.csv
```

---

❌ **No task folders**:
```
research/all-research.md  # Everything mixed together
```

✅ **Task-specific folders**:
```
research/task-001/algorithm.md
research/task-002/profiling.md
```

---

## Benefits Summary

### For Claude Code

✅ **Clear organization** - Easy to find research by task
✅ **Epic isolation** - No mixing between epics
✅ **Automatic context** - Task number tells you what the research is about
✅ **Clean archives** - Archive entire epic folder when done

### For Users

✅ **Paste external research** - Natural place for ChatGPT/DeepSeek responses
✅ **Side-by-side comparison** - Multiple sources in same task folder
✅ **Flexible organization** - Organize within task folder however you want
✅ **Easy navigation** - Task number maps directly to tasks.md

### For Projects

✅ **Scalable** - Works for 1 epic or 100 epics
✅ **Portable** - Epic folder contains everything for that epic
✅ **Maintainable** - Clear structure, easy to understand
✅ **Collaborative** - Multiple people can contribute research to same task

---

## Examples

### Example 1: Research Epic (Epic 01)

```
.claude/epics/01-understanding-structure-search/research/
├── README.md
├── task-001/  # Algorithm Understanding
│   ├── structure-placement-algorithm.md
│   ├── chatgpt-algorithm-explanation.md  (user pasted)
│   ├── code-flow-diagram.png
│   └── minecraft-source-excerpts.java
├── task-002/  # Profiling Analysis
│   ├── profiling-results.md
│   ├── spark-report-vanilla.txt
│   ├── spark-report-modded.txt
│   └── performance-charts.png
└── task-008/  # Final Summary
    ├── epic-01-summary.md
    └── executive-summary-slides.pdf
```

### Example 2: Implementation Epic (Epic 02)

```
.claude/epics/02-structure-start-optimization/research/
├── README.md
├── task-001/  # Research STRUCTURE_START bottleneck
│   ├── structure-start-analysis.md
│   ├── vanilla-code-review.md
│   └── optimization-approaches.md
├── task-005/  # Performance validation
│   ├── before-optimization-profile.txt
│   ├── after-optimization-profile.txt
│   └── performance-comparison.md
└── task-012/  # Final validation
    └── epic-02-results.md
```

---

**Last Updated**: 2025-10-25
**Pattern Version**: 1.0
**Applies To**: All projects using epic-based workflow

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used epic-research-organization
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used epic-research-organization`

This helps track which skills are actively consulted and identifies documentation gaps.
