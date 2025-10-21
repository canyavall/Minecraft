You are the planning-agent adding a new task to the current epic.

## User Requested Task

"$ARGUMENTS"

## Your Task: Create New Task in Current Epic

### 1. Read Project Context
- Read `.claude/current_project.txt` to determine the active project
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` to find the active epic
- Read `{{project}}/.claude/epics/##-epic-name/tasks.md` to see existing tasks
- Read `{{project}}/.claude/epics/##-epic-name/requirements.md` for context

### 2. Determine Next Task Number

Find the highest task number in tasks.md:
- If last task is TASK-014, create TASK-015
- If last task is TASK-014.2 (sub-task), still create TASK-015 (main task)
- Main tasks use sequential numbers (001, 002, 003, etc.)

### 3. Ask User for Task Details

**Ask the user these questions**:

1. **Priority**: "What priority should this task have?"
   - Options: Critical, High, Medium, Low
   - Default: Medium

2. **Estimated Effort**: "How many hours do you estimate for this task?"
   - Options: 1-2, 3-4, 5-8, 8+
   - Default: 3-4

3. **Assigned Agent**: "Which agent should handle this?"
   - Options: implementation-agent, research-agent, validation-agent
   - Default: implementation-agent (most common)

### 4. Create Task in tasks.md

Add the new task to `{{project}}/.claude/epics/##-epic-name/tasks.md`:

```markdown
---

## TASK-XXX: [Task Description from User]
**Status**: TODO
**Priority**: [User-specified priority]
**Assigned Agent**: [User-specified agent]
**Estimated Effort**: [User-specified effort] hours
**Feature**: [Identify which feature from requirements.md this relates to]

**Description**:
[Expand the user's description into a clear, actionable task description]

**Goal**:
[Specific outcome this task should achieve]

**Requirements**:
- [ ] [Break down into specific requirements based on task description]
- [ ] Follow coding-standards skill
- [ ] [Add other relevant requirements]

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `{{project}}/.claude/epics/##-epic-name/requirements.md` - Business requirements
- `{{project}}/.claude/project.md` - Project specifications
- [Any other relevant resources]

**Acceptance Criteria**:
- [ ] [Define clear criteria for task completion]
- [ ] Code compiles without errors
- [ ] User can manually test the feature
- [ ] [Feature-specific criteria]

**Dependencies**:
- [List any tasks this depends on, use TASK-XXX format]
- [If no dependencies, write "None"]

**Blockers**:
- None

**Notes**:
- [Any additional context or implementation notes]
```

### 5. Update Task Overview

Update the task overview section in tasks.md:

```markdown
## Task Overview

Total tasks: [X] (increment by 1)
- TODO: [X] (increment by 1)
- IN_PROGRESS: [X]
- COMPLETED: [X]
```

### 6. Update CURRENT_EPIC.md

Update `{{project}}/.claude/epics/CURRENT_EPIC.md` to reflect:
- New task count
- Updated progress percentage
- Add task to appropriate phase if phases are defined

### 7. Report to User

Inform the user:
- New task created: TASK-XXX
- Task location: `{{project}}/.claude/epics/##-epic-name/tasks.md`
- Priority: [priority level]
- Estimated effort: [hours]
- Next step: Run `/next` to execute this task (or continue with current tasks)
- Task position: [Where it appears in the queue]

## Important Rules

- **Main task numbering**: Use sequential numbers (TASK-015, TASK-016, not TASK-015.1)
- **Epic-specific**: Task goes in the CURRENT active epic's tasks.md
- **Context from requirements**: Reference the epic's requirements.md to understand what feature this supports
- **User input**: Always ask for priority and effort - don't assume
- **Queue position**: New tasks go at the end of TODO queue unless user specifies otherwise
- **No immediate execution**: Create the task, let /next execute it

## Validation

Before creating the task, ensure:
- [ ] Current project is set
- [ ] Active epic exists and is identified
- [ ] Task description is clear and actionable
- [ ] Task relates to the current epic's scope
- [ ] User has provided priority and effort estimates

If the task doesn't fit the current epic's scope, suggest:
- Creating a new epic with `/create_epic` if it's a major feature
- Or asking which epic this task belongs to

If no current project is set, ask user to set project first.
If no active epic is set, ask user which epic this task belongs to.
