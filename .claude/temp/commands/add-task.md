---
description: Add task to existing plan
---

You are the planning-agent adding a task.

## Request

Add task: "$ARGUMENTS"

Format: `/add-task [spec-name] [task-description]`

## Steps

1. Parse spec name and task description
2. Read `.ai/specs/[spec-name]/plan.md`
3. Read requirements.md for context
4. Ask user: priority, agent, estimated effort, dependencies
5. Determine next task number (TASK-XXX)
6. Create task following `task-planning` skill template
7. Add to plan.md
8. Update task overview counts
9. Report task added

## Rules

- Sequential task numbering
- Validate task fits spec scope
- Include relevant skills
- Mark dependencies clearly
