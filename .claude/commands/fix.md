---
description: Create a bug fix task in the current epic
agent: planning-agent
---

You are the planning-agent creating a bug fix task for the current epic.

## User Reported Issue

"$ARGUMENTS"

## Your Task: Create Fix Task

### 1. Read Project Context
- Read `.claude/current_project.txt` to determine the active project
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` to find the active epic
- Read `{{project}}/.claude/epics/##-epic-name/plan.md` to see existing tasks
- Read `{{project}}/.claude/epics/##-epic-name/requirements.md` for context

### 2. Determine Task Type

**ASK USER**: "Is this issue related to an existing task, or is it a standalone issue?"

**Option A - Related to Existing Task** (creates sub-task):
- Ask: "Which task number?" (e.g., TASK-001)
- Check for existing sub-tasks (TASK-001.1, TASK-001.2, etc.)
- Create next available sub-task number (e.g., TASK-001.3)
- Sub-task format: `TASK-XXX.Y: Fix - [Short Description]`

**Option B - Standalone Issue** (creates new main task):
- Find highest task number in plan.md
- Create next sequential main task (e.g., if last is TASK-014, create TASK-015)
- Main task format: `TASK-XXX: Fix - [Short Description]`

### 3. Create Fix Task

Based on user's choice from step 2:

Add the new task to `{{project}}/.claude/epics/##-epic-name/plan.md`:

**For Sub-tasks (Option A)**:
```markdown
---

## TASK-XXX.Y: Fix - [Short Issue Description]
**Status**: TODO
**Priority**: High (fixes are high priority)
**Assigned Agent**: implementation-agent
**Estimated Effort**: [1-4 hours based on complexity]
**Related Task**: TASK-XXX (parent task)

**For Main tasks (Option B)**:
```markdown
---

## TASK-XXX: Fix - [Short Issue Description]
**Status**: TODO
**Priority**: High (fixes are high priority)
**Assigned Agent**: implementation-agent
**Estimated Effort**: [1-4 hours based on complexity]
**Feature**: [Which feature from requirements.md this relates to]

**Description**:
Issue reported by user: [User's issue description]

**Issue Details**:
- Symptoms: [What the user is experiencing]
- Expected behavior: [What should happen]
- Actual behavior: [What is happening]
- Error messages: [Any error messages or logs]

**Goal**:
Fix the reported issue and ensure the feature works as intended.

**Requirements**:
- [ ] Investigate root cause of the issue
- [ ] Analyze logs and stack traces if applicable
- [ ] Fix the issue following coding-standards skill
- [ ] Ensure fix doesn't break other functionality
- [ ] Test the fix manually

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `{{project}}/.claude/epics/##-epic-name/requirements.md` - Business requirements
- Related task: TASK-XXX (review original implementation)

**Acceptance Criteria**:
- [ ] Issue no longer occurs
- [ ] Feature works as expected
- [ ] No new issues introduced
- [ ] User validates the fix

**Dependencies**:
- [For sub-tasks: TASK-XXX must be completed]
- [For main tasks: List any dependencies or write "None"]

**Blockers**:
- None

**Notes**:
- [For sub-tasks: This is a fix for TASK-XXX]
- [Any additional context or notes]
```

### 4. Update Epic Task Count

Update the task overview in plan.md:
```markdown
## Task Overview

Total tasks: [X] (increment by 1)
- TODO: [X] (increment by 1)
- IN_PROGRESS: [X]
- COMPLETED: [X]
```

### 5. Update CURRENT_EPIC.md

Update `{{project}}/.claude/epics/CURRENT_EPIC.md` to reflect the new task count and progress percentage.

### 6. Report to User

Inform the user:
- New task created: TASK-XXX[.Y] (with number)
- Task type: [Sub-task of TASK-XXX] or [Standalone fix task]
- Task location: `{{project}}/.claude/epics/##-epic-name/plan.md`
- Next step: Run `/next` to execute the fix
- Priority: High (fixes are prioritized)

## Important Rules

- **Flexible task creation**: Can create sub-tasks (TASK-XXX.Y) OR main tasks (TASK-XXX)
- **Ask user first**: Always ask if issue is related to existing task or standalone
- **Sub-task numbering**: Use decimal notation (TASK-001.1, TASK-001.2, etc.)
- **Main task numbering**: Use sequential numbers (TASK-015, TASK-016, etc.)
- **No immediate fix**: Don't fix the issue now - create the task for `/next` to execute
- **High priority**: Fixes are always high priority in the task queue
- **Epic-specific**: Task goes in the CURRENT active epic's plan.md

If no current project is set, ask user to set project first.
If no active epic is set, ask user which epic this bug fix belongs to.
