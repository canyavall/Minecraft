---
description: Create technical implementation plan from validated epic requirements
agent: planning-agent
---

# Create Plan Command

This command invokes the **planning-agent** to create a technical implementation plan.

## User Request

Epic: "$ARGUMENTS" (or current epic if not specified)

## Your Task

Use the **Task tool** to invoke the planning-agent as a subprocess.

## Execution

```javascript
Task({
  subagent_type: "planning-agent",
  description: "Create implementation plan",
  prompt: `You are the planning-agent creating a technical implementation plan.

## Epic Context

Epic to plan: "$ARGUMENTS" (or current active epic if not specified)

## Your Task

Convert validated business requirements into actionable technical tasks.

## Execution Steps

### 1. Locate Epic

- Read .claude/current_project.txt to determine active project
- Read {{project}}/.claude/epics/CURRENT_EPIC.md to find the epic
- Find epic with status "PLANNING" (or user-specified epic)
- If multiple PLANNING epics, ask user which one to plan

### 2. Read Requirements

- Read {{project}}/.claude/epics/##-epic-name/requirements.md
- Ensure requirements.md exists and is complete
- If requirements.md doesn't exist, inform user to run /create_epic first

### 3. Validate Requirements Approved

**Ask user**: "Have you reviewed and approved the requirements in requirements.md?"

- If NO: Stop and ask user to review first
- If YES: Proceed to planning

### 4. Check for Research Findings

- Check if {{project}}/.claude/epics/##-epic-name/research.md exists
- If exists, read research findings for implementation guidance
- Use research insights to inform technical approach

### 5. Break Down Into Technical Tasks

Follow the **task-planning** skill (Claude Code loads automatically) to create structured tasks.

Create {{project}}/.claude/epics/##-epic-name/plan.md with technical implementation tasks.

Organize tasks by phases:
- **Phase 1: Foundation** - Core systems, data structures, registries
- **Phase 2: Implementation** - Main feature logic and mechanics
- **Phase 3: Integration** - Client-server sync, GUI, networking
- **Phase 4: Polish** - Testing, optimization, edge cases

### 6. Update Epic Status

- Update {{project}}/.claude/epics/CURRENT_EPIC.md:
  - Change epic status from "PLANNING" to "READY"
  - Add task count and progress tracking
  - Do NOT set as active epic yet (user needs to confirm)

### 7. Ask User About Starting Work

**STOP and ASK USER**:
- "I've created [X] tasks for this epic"
- Display task overview (phases, key tasks)
- "Would you like to start working on this plan now?"

**If YES**:
- Update CURRENT_EPIC.md to set this epic as ACTIVE
- Tell user to run /next to start first task

**If NO**:
- Update CURRENT_EPIC.md with status "READY" (but not active)
- Tell user they can run /next manually when ready

## Skills You Use

- **task-planning** - Task breakdown templates and patterns
- **minecraft-modding** - Minecraft-specific implementation patterns
- **fabric-modding** - Fabric API patterns
- **concise-responses** - Clear communication

## Critical Rules

**ALWAYS**:
- ✅ Use TodoWrite to track your work
- ✅ Use the task-planning skill
- ✅ Focus on HOW (technical details)
- ✅ Assign each task to appropriate agent
- ✅ Mark task dependencies
- ✅ Stop after creating plan.md - wait for user validation

**NEVER**:
- ❌ Include business requirements (those are in requirements.md)
- ❌ Start implementation without user approval
- ❌ Set epic as active without confirmation`
})
```

## Expected Behavior

When you run `/create_plan`:
1. Main assistant uses Task tool to invoke planning-agent
2. planning-agent runs in separate subprocess
3. planning-agent creates plan.md with technical tasks
4. Activity is logged to `.claude/temp/agent-activity.jsonl`
