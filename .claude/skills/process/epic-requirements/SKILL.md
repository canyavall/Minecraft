---
name: epic-requirements
description: Rules and templates for creating epic requirements.md files. Use when creating new epics via /create_epic command or when defining business requirements for features.
allowed-tools: [Read, Write, Edit]
---

# Epic Requirements Creation Skill

Guidelines and templates for creating business-focused requirements.md files for epics.

## Purpose of requirements.md

Requirements.md defines **WHAT** features deliver and **WHY** they matter, NOT **HOW** to implement them.

### Audience
- **Primary**: Product owners, game designers, stakeholders
- **Secondary**: Technical team for understanding business value

### Focus
- ✅ Business value and user experience
- ✅ Game mechanics and balance
- ✅ Success criteria and metrics
- ❌ Technical implementation details
- ❌ Code architecture
- ❌ Specific classes or methods

## File Structure

### Location
```
{{project}}/.claude/epics/##-epic-name/requirements.md
```

### Naming Convention
- **Epic Number**: Zero-padded (01, 02, 03, etc.)
- **Epic Name**: kebab-case, descriptive
- Examples:
  - `01-guard-ranking-system/requirements.md`
  - `05-village-defense-towers/requirements.md`

## Requirements Template

See `template.md` for full template. Key sections:

### 1. Header
```markdown
# Epic ##: [Epic Name]

**Created**: [Date]
**Status**: PLANNING
**Priority**: [Critical/High/Medium/Low]
```

**Status Values**:
- `PLANNING` - Requirements being defined (user hasn't validated)
- `READY` - Requirements validated, tasks can be created
- `IN_PROGRESS` - Tasks being executed
- `COMPLETED` - All tasks done and validated

### 2. Business Value
```markdown
## Business Value

[What value does this epic deliver to users/players?]
```

**What to Include**:
- Player benefit (what new capability they get)
- Problem being solved
- Enhancement to gameplay experience
- Why this matters to users

**Example**:
> This epic delivers a progression system for guards, allowing players to
> develop stronger defenses over time. Players can strategically invest
> emeralds to create elite guard units, adding depth to village defense
> and providing long-term goals.

### 3. Overview
```markdown
## Overview

[High-level description of what this epic encompasses]
```

**What to Include**:
- Scope of the epic
- Major components
- How it fits with existing features

### 4. Features
```markdown
## Features

### Feature 1: [Feature Name]
**Description**: [What this feature does]

**User Experience**: [How players interact with it]

**Success Criteria**:
- [ ] Criterion 1
- [ ] Criterion 2
- [ ] Criterion 3
```

**Feature Guidelines**:
- Each feature should be distinct capability
- Describe from player's perspective
- Include concrete success criteria
- Make success measurable

**User Experience Section**:
- Step-by-step player interaction
- What they see, click, do
- Expected feedback and responses
- Edge cases from user perspective

### 5. Game Mechanics (if applicable)
```markdown
## Game Mechanics

### Progression System
[How players progress/unlock features]

### Economy/Balance
[Costs, rewards, balancing considerations]

### Combat/Interaction Systems
[How systems interact with gameplay]
```

**When to Include**:
- Game has progression elements
- Economy/resource management involved
- Combat or balance considerations
- Social/multiplayer interactions

**Be Specific**:
- ✅ "Archer guard costs 50 emeralds, level 1 → 2 costs 25 emeralds"
- ❌ "Guards cost emeralds to hire and upgrade"
- ✅ "Arrow towers deal 4 HP damage with 16 block range"
- ❌ "Towers do damage to enemies in range"

### 6. Technical Considerations
```markdown
## Technical Considerations

[Known technical constraints or requirements]
```

**What to Include**:
- Performance constraints (if known)
- Compatibility requirements
- Data storage needs (if impacts design)
- Client vs server considerations (if affects UX)

### 7. Dependencies
```markdown
## Dependencies

- [Other epics or systems this depends on]
```

**Types of Dependencies**:
- Previous epics that must be completed
- Existing systems that must be in place
- External mods or APIs required

### 8. Out of Scope
```markdown
## Out of Scope

[What this epic explicitly does NOT include]
```

**Why This Matters**:
- Sets clear boundaries
- Prevents scope creep
- Manages expectations
- Helps prioritization

**Examples**:
- ❌ "AI improvements" → ✅ "Advanced pathfinding (deferred to Epic 06)"
- ❌ "Not doing PvP" → ✅ "PvP guard combat (out of scope for MVP)"

### 9. Acceptance Criteria (Epic-Level)
```markdown
## Acceptance Criteria (Epic-Level)

- [ ] All features deliver stated business value
- [ ] User experience is intuitive
- [ ] Performance is acceptable
- [ ] Compatible with existing systems
- [ ] [Epic-specific criteria]
```

**Epic-Level vs Task-Level**:
- **Epic-Level**: Overall epic success (goes in requirements.md)
- **Task-Level**: Specific implementation success (goes in tasks.md)

## Writing Guidelines

### Use Business Language

**✅ Good**:
> Players can hire guards by right-clicking a villager and paying emeralds.
> Guards automatically defend the village from hostile mobs.

**❌ Bad**:
> Implement GuardEntity class with AI goals for attacking HostileMobEntity
> within 16 block range using PathNavigator.

### Be Specific with Numbers

**✅ Good**:
> - Archer guard: 50 emeralds to hire, 4 HP damage, 16 block range
> - Upgrade to level 2: 25 emeralds, +1 HP damage

**❌ Bad**:
> - Guards cost emeralds
> - Upgrades improve damage

### Describe User Experience

**✅ Good**:
> 1. Player right-clicks villager
> 2. GUI opens showing guard types and costs
> 3. Player clicks "Hire Archer" button
> 4. If player has 50 emeralds, guard spawns and emeralds deducted
> 5. If insufficient emeralds, red text shows "Need 50 emeralds"

**❌ Bad**:
> Player hires guard through GUI

### Include Success Metrics

**✅ Good**:
> - [ ] Players can hire all 3 guard types
> - [ ] Guard upgrade costs scale correctly (L1→L2: 25e, L2→L3: 50e)
> - [ ] Guards defend village within 32 block radius

**❌ Bad**:
> - [ ] Guards work
> - [ ] Upgrades implemented

## Quality Checklist

Before finalizing requirements.md:

- [ ] **Business Value** clearly states player benefit
- [ ] **Features** described from player perspective
- [ ] **User Experience** shows step-by-step interaction
- [ ] **Game Mechanics** include specific numbers/values
- [ ] **Success Criteria** are measurable
- [ ] **Out of Scope** sets clear boundaries
- [ ] **No technical implementation** details (classes, methods, code)
- [ ] **Language** accessible to non-developers
- [ ] **All placeholders** replaced with actual content

## Examples

See `examples/` folder for:
- `guard-ranking-system.md` - Progression/economy example
- `village-defense-towers.md` - Combat/placement example
- `chunk-loading-optimization.md` - Performance epic example

## Common Mistakes

### ❌ Too Technical
```markdown
## Features

### Feature 1: GuardEntity Implementation
**Description**: Create GuardEntity extending VillagerEntity with custom
AI goals and pathfinding using AttackGoal and DefendVillageGoal.
```

**✅ Fixed**:
```markdown
### Feature 1: Hired Guards
**Description**: Players can hire guards who automatically defend the
village from hostile mobs like zombies and skeletons.
```

### ❌ Vague Success Criteria
```markdown
**Success Criteria**:
- [ ] Guards work properly
- [ ] Players like the feature
```

**✅ Fixed**:
```markdown
**Success Criteria**:
- [ ] Guards engage hostile mobs within 32 blocks of village center
- [ ] Players can hire 3 different guard types (Archer, Swordsman, Mage)
- [ ] Guard upgrade costs match design: L1→L2: 25e, L2→L3: 50e, L3→L4: 100e
```

### ❌ Missing Game Mechanics
```markdown
## Features
### Feature 1: Tower Defense
**Description**: Players can build towers.
```

**✅ Fixed**:
```markdown
### Feature 1: Tower Defense

**Description**: Players can craft and place defensive towers that
automatically attack hostile mobs.

**User Experience**:
1. Player crafts Arrow Tower (50 emeralds + materials)
2. Player places tower near village
3. Tower automatically targets hostile mobs within 16 blocks
4. Tower fires arrows dealing 4 HP damage every 2 seconds
5. Tower can be upgraded to Level 2 (+8 block range, +2 HP damage)

**Game Mechanics**:
- **Arrow Tower**: 16 block range, 4 HP damage, 2 second cooldown
- **Fireball Tower**: 24 block range, 6 HP damage + AOE, 4 second cooldown
- **Upgrade Cost**: Level 2: 100 emeralds, Level 3: 200 emeralds
- **Max Towers**: 5 per village (prevents spam)
```

## Integration with Workflow

### epic-agent Creates requirements.md
1. User runs `/create_epic "Epic Name"`
2. epic-agent creates `##-epic-name/requirements.md`
3. epic-agent follows this skill's template
4. User validates requirements.md

### After Validation
1. User runs `/create_plan`
2. planning-agent reads requirements.md
3. planning-agent creates tasks.md (technical implementation)

### Separation of Concerns
- **requirements.md** (this skill): Business, UX, game design
- **tasks.md** (task-planning skill): Technical, implementation, code

## When to Use This Skill

Use this skill when:
- Creating new epic via `/create_epic` command
- epic-agent is invoked to define business requirements
- Reviewing or updating epic requirements
- Questions about "How should I structure requirements?"
- Ensuring requirements are business-focused, not technical

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used epic-requirements
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used epic-requirements`

This helps track which skills are actively consulted and identifies documentation gaps.
