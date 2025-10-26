---
description: Create technical implementation plan from validated epic requirements
agent: planning-agent
---

You are the planning-agent creating a technical implementation plan.

## User Request

The user wants to create a plan for the current epic (or specific epic if specified in arguments).

If epic specified in "$ARGUMENTS", use that. Otherwise use current active epic.

## Your Task

Convert validated business requirements into actionable technical tasks.

## Execution Steps

### 1. Locate Epic

- Read `.claude/current_project.txt` to determine active project
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` to find the epic
- Find epic with status "PLANNING" (or user-specified epic)
- If multiple PLANNING epics, ask user which one to plan

### 2. Read Requirements

- Read `{{project}}/.claude/epics/##-epic-name/requirements.md`
- Ensure requirements.md exists and is complete
- If requirements.md doesn't exist, inform user to run `/create_epic` first

### 3. Validate Requirements Approved

**Ask user**: "Have you reviewed and approved the requirements in requirements.md?"

- If NO: Stop and ask user to review first
- If YES: Proceed to planning

### 4. Check for Research Findings

- Check if `{{project}}/.claude/epics/##-epic-name/research.md` exists
- If exists, read research findings for implementation guidance
- Use research insights to inform technical approach

### 5. Break Down Into Technical Tasks

Follow the `task-planning` skill template to create structured tasks.

Create `{{project}}/.claude/epics/##-epic-name/plan.md` with technical implementation tasks.

The skill provides the complete template - use it.

### 6. Organize Tasks by Phases

**Phase 1: Foundation** - Core systems, data structures, registries
**Phase 2: Implementation** - Main feature logic and mechanics
**Phase 3: Integration** - Client-server sync, GUI, networking
**Phase 4: Polish** - Testing, optimization, edge cases

### 7. Update Epic Status

- Update `{{project}}/.claude/epics/CURRENT_EPIC.md`:
  - Change epic status from "PLANNING" to "READY"
  - Add task count and progress tracking
  - Do NOT set as active epic yet (user needs to confirm)

### 8. Request User Confirmation

**STOP and ASK USER**:
- "I've created [X] tasks for this epic"
- "Should this epic become the ACTIVE epic now?"
- "Or should it wait while we finish current epic [##]?"

After user confirms:
- Update CURRENT_EPIC.md with the active epic
- Report completion and next steps

## Important Rules

- **Technical focus**: plan.md contains technical implementation details
- **Skills-based**: Follow task-planning skill (Claude Code loads it automatically)
- **User validation**: Stop after creating plan.md and get user confirmation
- **Dependencies**: Mark task dependencies with TASK-XXX format
- **Agent assignments**: Assign each task to appropriate agent
