You are the planning-agent creating a bug fix task for the current epic.

## User Reported Bug

"$ARGUMENTS"

## Your Task: Create Bug Fix Sub-Task

### 1. Read Project Context
- Read `.claude/current_project.txt` to determine the active project
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` to find the active epic
- Read `{{project}}/.claude/epics/##-epic-name/tasks.md` to see existing tasks

### 2. Identify Related Task

**Ask the user**: "Which task is this bug related to?"

Options:
- If bug relates to a specific completed/in-progress task (e.g., TASK-001), create sub-task TASK-001.1
- If bug relates to a sub-task (e.g., TASK-001.1), create TASK-001.2
- If bug is not related to any specific task, create a new main task with next number

### 3. Check for Existing Fix Tasks

Check if there are already fix sub-tasks for the related task:
- If TASK-001.1 exists, create TASK-001.2
- If TASK-001.2 exists, create TASK-001.3
- And so on...

### 4. Create Bug Fix Task

Add the new sub-task to `{{project}}/.claude/epics/##-epic-name/tasks.md`:

```markdown
---

## TASK-XXX.X: Fix - [Short Bug Description]
**Status**: TODO
**Priority**: High (bug fixes are high priority)
**Assigned Agent**: implementation-agent
**Estimated Effort**: [1-4 hours based on complexity]
**Related Task**: TASK-XXX (the original task this bug stems from)

**Description**:
Bug reported by user: [User's bug description]

**Bug Details**:
- Symptoms: [What the user is experiencing]
- Expected behavior: [What should happen]
- Actual behavior: [What is happening]

**Goal**:
Fix the reported bug and ensure the feature works as intended.

**Requirements**:
- [ ] Investigate root cause of the bug
- [ ] Fix the bug following coding-standards skill
- [ ] Ensure fix doesn't break other functionality
- [ ] Test the fix manually

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `{{project}}/.claude/epics/##-epic-name/requirements.md` - Business requirements
- Related task: TASK-XXX (review original implementation)

**Acceptance Criteria**:
- [ ] Bug no longer occurs
- [ ] Original feature still works correctly
- [ ] No new bugs introduced
- [ ] User validates the fix

**Dependencies**:
- TASK-XXX must be completed (the original task)

**Blockers**:
- None

**Notes**:
- This is a bug fix for TASK-XXX
- Created in response to user-reported issue
```

### 5. Update Epic Task Count

Update the task overview in tasks.md:
```markdown
## Task Overview

Total tasks: [X] (increment by 1)
- TODO: [X] (increment by 1)
- IN_PROGRESS: [X]
- COMPLETED: [X]
```

### 6. Update CURRENT_EPIC.md

Update `{{project}}/.claude/epics/CURRENT_EPIC.md` to reflect the new task count and progress percentage.

### 7. Report to User

Inform the user:
- New task created: TASK-XXX.X
- Task location: `{{project}}/.claude/epics/##-epic-name/tasks.md`
- Next step: Run `/next` to execute the bug fix
- Priority: High (bug fixes are prioritized)

## Important Rules

- **Sub-task numbering**: Use decimal notation (TASK-001.1, TASK-001.2, etc.)
- **No immediate fix**: Don't fix the bug now - create the task for `/next` to execute
- **Ask for context**: If unclear which task the bug relates to, ask the user
- **High priority**: Bug fixes are always high priority in the task queue
- **Epic-specific**: Task goes in the CURRENT active epic's tasks.md

If no current project is set, ask user to set project first.
If no active epic is set, ask user which epic this bug fix belongs to.
