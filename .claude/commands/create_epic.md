You are the epic-agent creating a new epic with business requirements.

## Your Task

A user wants to create a new epic:

"$ARGUMENTS"

## Execution Steps

### 1. Read Project Context
- Read `.claude/current_project.txt` to determine the active project
- Read `{{project}}/.claude/project.md` to understand current project state
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` to see existing epics

### 2. Validate Epic Creation
- Determine if this truly needs a NEW epic or fits in existing epic
- If fits in existing epic, inform user and suggest using that epic instead
- If new epic is justified, proceed to next step

### 3. Create Epic Structure
- Determine next epic number (e.g., if last is 04, create 05)
- Create epic folder: `{{project}}/.claude/epics/##-epic-name/`
  - Use kebab-case for epic name (e.g., `05-village-defense-system`)
  - Ensure number is zero-padded (01, 02, etc.)

### 4. Design Business Requirements
Create `{{project}}/.claude/epics/##-epic-name/requirements.md` with:

```markdown
# Epic ##: [Epic Name]

**Created**: [Date]
**Status**: PLANNING
**Priority**: [Critical/High/Medium/Low]

## Business Value

[What value does this epic deliver to users/players?]

## Overview

[High-level description of what this epic encompasses]

## Features

### Feature 1: [Feature Name]
**Description**: [What this feature does]
**User Experience**: [How players interact with it]
**Success Criteria**:
- [ ] Criterion 1
- [ ] Criterion 2

[Repeat for each feature in epic]

## Game Mechanics (if applicable)

### Progression System
[How players progress/unlock features]

### Economy/Balance
[Costs, rewards, balancing considerations]

### Combat/Interaction Systems
[How systems interact with gameplay]

## Technical Considerations

[Any known technical constraints or requirements]

## Dependencies

- [Other epics or systems this depends on]

## Out of Scope

[What this epic explicitly does NOT include]

## Acceptance Criteria (Epic-Level)

- [ ] All features deliver stated business value
- [ ] User experience is intuitive and polished
- [ ] Performance impact is acceptable
- [ ] Compatible with existing systems

```

### 5. Update Epic Tracking
- Update `{{project}}/.claude/epics/CURRENT_EPIC.md`:
  - Add new epic to the overview table with status "PLANNING"
  - Do NOT set as active epic yet (user needs to validate first)

### 6. Report to User

Inform user:
- Epic created: `{{project}}/.claude/epics/##-epic-name/`
- Requirements file: `{{project}}/.claude/epics/##-epic-name/requirements.md`
- **Next step**: User should review and validate requirements.md
- **After validation**: Run `/create_plan` to generate technical tasks

## Important Rules

- **Business-focused**: requirements.md should be readable by non-technical stakeholders
- **No technical tasks**: Do NOT create tasks.md - that's done by `/create_plan`
- **User validation**: Stop after creating requirements.md and wait for user approval
- **Game design**: Include game mechanics design (progression, economy, balance) when applicable
- **Epic numbering**: Always use zero-padded numbers (01, 02, 03, etc.)

If no current project is set, ask user to set project first.
