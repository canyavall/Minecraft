---
description: Create a new epic with business requirements and game mechanics design
agent: epic-agent
---

You are the epic-agent creating a new epic.

## User Request

"$ARGUMENTS"

## Your Task

Create a new epic with comprehensive business requirements and game mechanics design (for Minecraft mods).

## Execution Steps

### 1. Read Project Context

- Read `.claude/current_project.txt` to determine the active project
- If no project set, inform user they need to set a project first and stop
- Read `{{project}}/.claude/project.md` to understand current project
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` to see existing epics

### 2. Validate Epic Need

- Determine if this truly needs a NEW epic or fits in existing epic
- If fits in existing epic, inform user and suggest using that epic instead
- If new epic is justified, proceed

### 3. Create Epic Structure

- Determine next epic number (e.g., if last is 04, create 05)
- Create epic folder: `{{project}}/.claude/epics/##-epic-name/`
  - Use kebab-case for epic name (e.g., `05-village-defense-system`)
  - Ensure number is zero-padded (01, 02, etc.)

### 4. Create Requirements Document

Follow the `epic-requirements` skill to create `{{project}}/.claude/epics/##-epic-name/requirements.md`.

The skill provides the complete template and examples - use it.

### 5. Create Placeholder for Plan

Create empty placeholder: `{{project}}/.claude/epics/##-epic-name/plan.md`

Add comment:
```markdown
# Epic ##: [Epic Name] - Implementation Plan

**Status**: PENDING
**Prerequisites**: requirements.md must be validated by user

---

This file will be created by planning-agent when user runs `/create_plan`.
```

### 6. Update Epic Tracking

- Update `{{project}}/.claude/epics/CURRENT_EPIC.md`:
  - Add new epic to overview table with status "PLANNING"
  - Do NOT set as active epic yet (user needs to validate first)

### 7. Report to User

Inform user:
- Epic created: `{{project}}/.claude/epics/##-epic-name/`
- Requirements file: `{{project}}/.claude/epics/##-epic-name/requirements.md`
- Display a brief summary of what's in requirements.md (key features, goals)

### 8. Ask User About Next Steps

**ASK USER**: "Would you like me to generate the implementation plan (plan.md) for this epic now?"

**If YES**:
- Automatically invoke `/create_plan` command
- Continue workflow seamlessly

**If NO**:
- Stop here
- Tell user they can run `/create_plan` manually when ready
- Remind user to review requirements.md first

## Important Rules

- **Business-focused**: requirements.md uses business language (follow epic-requirements skill)
- **No technical tasks**: Do NOT create plan.md content - that's done by `/create_plan`
- **User validation**: Stop after creating requirements.md and wait for user approval
- **Epic numbering**: Always use zero-padded numbers (01, 02, 03, etc.)
- **Follow skill**: Use the epic-requirements skill for structure and templates
