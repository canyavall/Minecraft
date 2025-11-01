---
description: Execute the next task in the current epic
agent: planning-agent
---

# Next Command

This command invokes the **planning-agent** to execute the next task.

## Your Task

Use the **Task tool** to invoke the planning-agent as a subprocess.

## Execution

```javascript
Task({
  subagent_type: "planning-agent",
  description: "Execute next task",
  prompt: `You are the planning-agent. Execute the /next command workflow:

## Your Process

### 1. Read Project Context

- Read .claude/current_project.txt to determine the active project
- Read {{project}}/.claude/epics/CURRENT_EPIC.md to find the active epic
- Read the active epic's plan.md to find the next TODO task

### 2. Find Next Task

- Scan plan.md for first task with status "TODO"
- If no TODO tasks found, report epic completion
- Read task details (description, requirements, agent assignment)

### 3. Gather Context

**CRITICAL: Use Context-Aware Knowledge Preloading**

#### Step 3.1: Load Project Knowledge Cache

Read `.claude/knowledge/project-cache.json` and find the active project's entry.
This pre-computes which knowledge files are relevant to the project.

For each knowledge entry with priority "critical" or "high":
- Note the knowledge path
- Prepare to reference it when invoking the worker agent

#### Step 3.2: Load Task-Specific Knowledge

Based on task description, identify additional relevant knowledge:
- Search `.claude/knowledge/knowledge-index.json` by tags
- Match task keywords to knowledge tags
- Add any highly relevant knowledge not in project cache

**Example**: Task mentions "flying entity animation"
→ Search index for tags: flying-entities, animations, geckolib
→ Find: graphics/flying-entity-control.md
→ Add to context

#### Step 3.3: Collect Traditional Context Files

- coding-standards skill (shared standards)
- {{project}}/.claude/project.md (project specifications)
- {{project}}/.claude/epics/##-epic-name/requirements.md (business requirements)
- Any research files from {{project}}/.claude/research/
- Any handover documents from {{project}}/.claude/temp/

### 4. Invoke Assigned Agent

Use the Task tool to invoke the assigned agent (implementation-agent or research-agent):

\`\`\`javascript
Task({
  subagent_type: "[implementation-agent or research-agent]",
  description: "[Task description]",
  prompt: \`[Full task context with all requirements and files]\`
})
\`\`\`

Provide the agent with:
- Complete task description
- All required context files
- Clear goals and acceptance criteria
- Project name and location
- **Relevant knowledge references** (from Steps 3.1 and 3.2)

**Knowledge Context Format**:
```
## Relevant Knowledge Base Entries

The following knowledge files are relevant to this task:

**Critical Priority**:
- .claude/knowledge/[path] - [reason why relevant]

**High Priority**:
- .claude/knowledge/[path] - [reason why relevant]

**Task-Specific**:
- .claude/knowledge/[path] - [matched tags: tag1, tag2]

Read these knowledge files BEFORE starting implementation.
```

### 5. After Agent Completes

- Update task status in plan.md (TODO → COMPLETED)
- Update {{project}}/.claude/epics/CURRENT_EPIC.md progress
- Store any research or temporary files in appropriate folders
- Report completion to user

### 6. Stop and Wait

**STOP and wait for user confirmation before proceeding to next task.**

Tell user: "Task TASK-XXX is complete. Run /next when ready for the next task."

## Critical Rules

**ALWAYS**:
- ✅ Use TodoWrite to track your work
- ✅ Use Task tool to invoke implementation-agent or research-agent
- ✅ Update task status after completion
- ✅ STOP after each task - wait for user validation
- ✅ Provide complete context to invoked agent

**NEVER**:
- ❌ Auto-proceed to next task without user confirmation
- ❌ Skip validation checkpoints
- ❌ Work on multiple tasks simultaneously

## Error Handling

If no current project is set, ask user to set project first.
If no active epic is found, check CURRENT_EPIC.md for status.
If no tasks are available, report epic completion.
If next task is blocked, report blockers and ask for guidance.`
})
```

## Expected Behavior

When you run `/next`:
1. Main assistant uses Task tool to invoke planning-agent
2. planning-agent subprocess reads plan.md
3. planning-agent uses Task tool to invoke implementation-agent or research-agent (nested subprocess!)
4. Worker agent completes task
5. planning-agent updates plan.md
6. Activity is logged for both planning-agent AND worker agent
