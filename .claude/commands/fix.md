---
description: Create a bug fix task in the current epic
agent: planning-agent
---

# Fix Command

This command invokes the **planning-agent** to create a bug fix task.

## User Reported Issue

"$ARGUMENTS"

## Your Task

Use the **Task tool** to invoke the planning-agent as a subprocess.

## Execution

```javascript
Task({
  subagent_type: "planning-agent",
  description: "Create fix task",
  prompt: `You are the planning-agent creating a bug fix task.

## User Reported Issue

"$ARGUMENTS"

## Your Task: Create Fix Task

### 1. Read Project Context

- Read .claude/current_project.txt to determine the active project
- Read {{project}}/.claude/epics/CURRENT_EPIC.md to find the active epic  
- Read {{project}}/.claude/epics/##-epic-name/plan.md to see existing tasks

### 2. Determine Task Type

**ASK USER**: "Is this issue related to an existing task, or is it a standalone issue?"

**Option A - Related** (creates sub-task):
- Ask: "Which task number?" (e.g., TASK-001)
- Create next sub-task number (e.g., TASK-001.3)
- Format: TASK-XXX.Y: Fix - [Description]

**Option B - Standalone** (creates new main task):
- Find highest task number in plan.md
- Create next sequential main task
- Format: TASK-XXX: Fix - [Description]

### 3. Create Fix Task

Add task to {{project}}/.claude/epics/##-epic-name/plan.md with:
- High priority (fixes are urgent)
- Assigned to implementation-agent
- Complete description of issue
- Acceptance criteria

### 4. Update Epic

- Update task count in plan.md
- Update {{project}}/.claude/epics/CURRENT_EPIC.md progress

### 5. Report to User

- New task created: TASK-XXX[.Y]
- Next step: Run /next to execute the fix

## Critical Rules

**ALWAYS**:
- ✅ Use TodoWrite to track your work
- ✅ Ask if related or standalone before creating
- ✅ High priority for fix tasks
- ✅ Stop after creating task - don't execute it

**NEVER**:
- ❌ Fix the issue immediately (create task for /next)
- ❌ Assume task type without asking user`
})
```

## Expected Behavior

When you run `/fix "issue description"`:
1. Main assistant uses Task tool to invoke planning-agent
2. planning-agent creates fix task in plan.md
3. Activity is logged
