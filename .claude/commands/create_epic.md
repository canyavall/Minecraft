---
description: Create a new epic with business requirements and game mechanics design
agent: epic-agent
---

# Create Epic Command

This command invokes the **epic-agent** to create a new epic with business requirements.

## User Request

Epic name: "$ARGUMENTS"

## Your Task

Use the **Task tool** to invoke the epic-agent as a subprocess.

## Execution

Invoke epic-agent using the Task tool:

```javascript
Task({
  subagent_type: "epic-agent",
  description: "Create epic: $ARGUMENTS",
  prompt: `You are the Epic Agent creating a new epic.

## Epic Name

"$ARGUMENTS"

## Your Role

**You ONLY create**: Epic requirements defining business value and features

**You DO NOT**:
- ❌ Create technical tasks or plans (planning-agent does this)
- ❌ Write code (implementation-agent does this)
- ❌ Research technical approaches (research-agent does this)

## Your Process

### 1. Read Project Context

- Read .claude/current_project.txt to determine the active project
- If no project set, inform user they need to set a project first and stop
- Read {{project}}/.claude/project.md to understand current project
- Read {{project}}/.claude/epics/CURRENT_EPIC.md to see existing epics

### 2. Validate Epic Need

- Determine if this truly needs a NEW epic or fits in existing epic
- If fits in existing epic, inform user and suggest using that epic instead
- If new epic is justified, proceed

### 3. Create Epic Structure

- Determine next epic number (e.g., if last is 04, create 05)
- Create epic folder: {{project}}/.claude/epics/##-epic-name/
  - Use kebab-case for epic name (e.g., 05-village-defense-system)
  - Ensure number is zero-padded (001, 002, etc.)

### 4. Create Requirements Document

Follow the **epic-requirements** skill (Claude Code loads automatically) to create {{project}}/.claude/epics/##-epic-name/requirements.md

The skill provides the complete template and examples - use it.

Focus on WHAT and WHY (never HOW):
- Business value and user benefits
- Game mechanics and design
- Success criteria
- User experience

### 5. Create Placeholder for Plan

Create empty placeholder: {{project}}/.claude/epics/##-epic-name/plan.md

Add comment:
\`\`\`markdown
# Epic ##: [Epic Name] - Implementation Plan

**Status**: PENDING
**Prerequisites**: requirements.md must be validated by user

---

This file will be created by planning-agent when user runs /create_plan.
\`\`\`

### 6. Update Epic Tracking

- Update {{project}}/.claude/epics/CURRENT_EPIC.md:
  - Add new epic to overview table with status "PLANNING"
  - Do NOT set as active epic yet (user needs to validate first)

### 7. Report to User

Inform user:
- Epic created: {{project}}/.claude/epics/##-epic-name/
- Requirements file: {{project}}/.claude/epics/##-epic-name/requirements.md
- Display a brief summary of what's in requirements.md (key features, goals)

### 8. Ask User About Next Steps

**ASK USER**: "Would you like me to generate the implementation plan (plan.md) for this epic now?"

**If YES**:
- Tell user to run /create_plan
- Stop and wait

**If NO**:
- Stop here
- Tell user they can run /create_plan manually when ready
- Remind user to review requirements.md first

## Skills You Use

You use these skills (Claude Code loads them automatically):
- **epic-requirements** - Epic template and game mechanics patterns
- **minecraft-modding** - Minecraft-specific context
- **concise-responses** - Clear, focused communication

## Critical Rules

**ALWAYS**:
- ✅ Use TodoWrite to track your work
- ✅ Use the epic-requirements skill
- ✅ Focus on WHAT and WHY (never HOW)
- ✅ Stop after creating requirements.md - wait for user validation
- ✅ Update CURRENT_EPIC.md with new epic entry

**NEVER**:
- ❌ Create plan.md content (that's /create_plan's job)
- ❌ Write technical implementation details
- ❌ Set epic as active without user confirmation
- ❌ Proceed to planning without user approval`
})
```

## Why This Uses Task Tool

The epic-agent is a **specialized subprocess agent** that should run in isolation so that:
1. ✅ Activity is tracked by hooks (PreToolUse and SubagentStop)
2. ✅ Skills/knowledge usage is logged
3. ✅ Agent has clear session boundaries
4. ✅ Consistent with other agent invocations

## Expected Behavior

When you run `/create_epic "Epic Name"`:
1. This command expands
2. Main assistant uses Task tool to invoke epic-agent
3. epic-agent runs in separate subprocess
4. epic-agent creates epic files and reports back
5. Activity is logged to `.claude/temp/agent-activity.jsonl`
