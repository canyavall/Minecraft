---
name: task-planning
description: Rules and templates for creating technical plan.md files from requirements. Use when creating implementation plans via /create_plan command or breaking down features into tasks.
allowed-tools: [Read, Write, Edit]
---

# Task Planning Skill

Guidelines and templates for creating technical plan.md files for epic implementation.

## Purpose of plan.md

Plan.md defines **HOW** to implement features technically, NOT **WHAT** business value they deliver.

### Audience
- **Primary**: Developers (implementation-agent, research-agent)
- **Secondary**: planning-agent for task orchestration

### Focus
- ✅ Technical implementation steps
- ✅ Code architecture and design
- ✅ Specific classes, methods, files
- ✅ Dependencies and blockers
- ❌ Business justification
- ❌ User experience descriptions
- ❌ Game mechanics rationale

## File Structure

### Location
```
{{project}}/.claude/epics/##-epic-name/plan.md
```

### Created By
- planning-agent via `/create_plan` command
- After user validates requirements.md

## Task Template

See `template.md` for full template. Key sections:

### 1. Header
```markdown
# Epic ##: [Epic Name] - Technical Tasks

**Created**: [Date]
**Status**: READY
**Requirements**: See requirements.md

## Task Overview

Total tasks: [X]
- TODO: [X]
- IN_PROGRESS: 0
- COMPLETED: 0
```

### 2. Individual Task Format
```markdown
## TASK-001: [Descriptive Technical Title]

**Status**: TODO
**Priority**: [Critical/High/Medium/Low]
**Assigned Agent**: implementation-agent
**Estimated Effort**: [Hours]
**Feature**: [Which feature from requirements.md this implements]

**Description**:
[Clear technical description of what needs to be implemented]

**Goal**:
[The specific technical outcome this task achieves]

**Requirements**:
- [ ] Specific technical requirement 1
- [ ] Specific technical requirement 2
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `{{project}}/.claude/project.md` - Project specifications
- `{{project}}/.claude/epics/##-epic-name/requirements.md` - Business context
- [Any research files or references]

**Acceptance Criteria**:
- [ ] Code compiles without errors
- [ ] Follows all coding standards
- [ ] User can manually test the feature
- [ ] [Feature-specific technical criteria]

**Dependencies**:
- [TASK-XXX format for dependent tasks]

**Blockers**:
- None (or list current blockers)

**Notes**:
- [Implementation decisions]
- [Technical considerations]
```

## Task Numbering

### Main Tasks
- Sequential: TASK-001, TASK-002, TASK-003, etc.
- Three digits, zero-padded
- Increment by 1

### Sub-Tasks (Bug Fixes)
- Related to main task: TASK-001.1, TASK-001.2, etc.
- Used for bug fixes or follow-up work
- Created by `/fix` command

### Example Numbering
```
TASK-001: Implement GuardEntity class
TASK-002: Add guard registry system
TASK-002.1: Fix registry desync on server restart (bug fix)
TASK-003: Create guard hiring GUI
TASK-003.1: Fix GUI crash when no emeralds (bug fix)
TASK-003.2: Fix button positioning on smaller screens (bug fix)
TASK-004: Implement guard combat AI
```

## Task Status Values

- **TODO**: Not started yet
- **IN_PROGRESS**: Currently being worked on
- **COMPLETED**: Finished and validated by user

## Task Phases

Organize tasks into logical phases:

### Phase 1: Foundation
**Purpose**: Core systems, data structures, registries

**Tasks Include**:
- Entity/block registration
- Data component definitions
- Registry setup
- Basic initialization

**Example**:
```markdown
## TASK-001: Create GuardEntity class
**Phase**: Foundation
- Define GuardEntity extending VillagerEntity
- Register entity type in Registries
- Set up basic entity properties
```

### Phase 2: Implementation
**Purpose**: Main feature logic and mechanics

**Tasks Include**:
- Business logic implementation
- Game mechanics code
- Feature-specific algorithms
- Data management

**Example**:
```markdown
## TASK-005: Implement guard combat AI
**Phase**: Implementation
- Create custom AttackGoal
- Implement target selection logic
- Add pathfinding for combat
```

### Phase 3: Integration
**Purpose**: Client-server sync, GUI, networking

**Tasks Include**:
- Packet creation and handling
- GUI implementation
- Client-server synchronization
- Network communication

**Example**:
```markdown
## TASK-009: Create guard hiring GUI
**Phase**: Integration
- Extend HandledScreen for guard hiring
- Implement ScreenHandler for inventory
- Add networking for hire action
```

### Phase 4: Polish
**Purpose**: Testing, optimization, edge cases

**Tasks Include**:
- Performance optimization
- Edge case handling
- Visual polish
- Sound effects
- Automated testing (after user validation)

**Example**:
```markdown
## TASK-013: Optimize guard pathfinding
**Phase**: Polish
- Profile guard pathfinding performance
- Implement caching for path calculations
- Add configurable tick rate
```

## Agent Assignment

### implementation-agent
**Assign For**:
- All code implementation
- System architecture
- GUI/UX implementation
- Bug fixes
- Refactoring

**Example Task**:
```markdown
**Assigned Agent**: implementation-agent
**Description**: Create GuardEntity class extending VillagerEntity with
custom data components for guard type, level, and equipment.
```

### research-agent
**Assign For**:
- Investigation of unknown systems
- Performance research
- Compatibility analysis
- Best practice research

**Example Task**:
```markdown
**Assigned Agent**: research-agent
**Description**: Research Fabric's entity AI system to determine best
approach for guard combat behaviors in 1.21.1.
```

### Testing Tasks (implementation-agent)
**Testing is now handled by implementation-agent**:
- Write automated tests after user manual validation
- Create test strategy and test cases
- Implement quality assurance procedures

**Example Task**:
```markdown
**Assigned Agent**: implementation-agent
**Description**: Write automated tests for guard hiring system after
user has manually validated it works correctly. Include unit tests
for entity creation, integration tests for hiring mechanics, and
edge case coverage for invalid inputs.
```

## Writing Technical Tasks

### Be Specific About Code

**✅ Good**:
```markdown
## TASK-001: Create GuardEntity class

**Description**:
Create GuardEntity class extending VillagerEntity. Implement custom data
components for:
- GuardType (Archer/Swordsman/Mage) using enum codec
- Level (0-10) using integer codec
- Equipment (ItemStack list) using list codec

Register entity type in ModRegistry with spawn egg.
```

**❌ Bad**:
```markdown
## TASK-001: Make guards

**Description**:
Create the guard system.
```

### Include Technical Constraints

**✅ Good**:
```markdown
**Requirements**:
- [ ] Must be thread-safe for server tick execution
- [ ] Must sync guard data to client via packets
- [ ] Must follow singleton registry pattern from standards.md
- [ ] Must handle null cases for missing components
```

**❌ Bad**:
```markdown
**Requirements**:
- [ ] Make it work
- [ ] No bugs
```

### Define Clear Acceptance Criteria

**✅ Good**:
```markdown
**Acceptance Criteria**:
- [ ] GuardEntity spawns with correct model and texture
- [ ] Guard data components persist through server restart
- [ ] Guard type changes reflect on client without reconnect
- [ ] Code compiles with zero warnings
- [ ] All public methods have Javadoc
```

**❌ Bad**:
```markdown
**Acceptance Criteria**:
- [ ] Works correctly
- [ ] Tested
```

### Specify Dependencies

**✅ Good**:
```markdown
**Dependencies**:
- TASK-001: GuardEntity class (needed for registry)
- TASK-002: Data components (needed for guard state)

**Blockers**:
- Waiting for Fabric API 0.95.0 for component bug fix
```

**❌ Bad**:
```markdown
**Dependencies**:
- Other stuff needs to be done first
```

## Quality Checklist

Before finalizing plan.md:

- [ ] **All features** from requirements.md have corresponding tasks
- [ ] **Task numbers** are sequential and correct
- [ ] **Dependencies** explicitly marked with TASK-XXX references
- [ ] **Agents assigned** appropriately
- [ ] **Effort estimated** realistically (hours)
- [ ] **Acceptance criteria** are specific and testable
- [ ] **Technical details** included (classes, methods, files)
- [ ] **Standards referenced** (coding-standards skill)
- [ ] **Phases organized** logically (Foundation → Implementation → Integration → Polish)
- [ ] **No business justification** (that's in requirements.md)

## Task Organization

### Logical Ordering
Tasks should be ordered by:
1. **Dependencies first**: Foundation before implementation
2. **Risk reduction**: High-risk/unknown tasks early
3. **Value delivery**: Complete features before starting new ones
4. **Incremental testing**: Testable milestones

### Example Task Order
```markdown
TASK-001: Create GuardEntity class (Foundation)
TASK-002: Register guard data components (Foundation)
TASK-003: Implement guard registry (Foundation)
TASK-004: Add guard AI goals (Implementation)
TASK-005: Implement combat behavior (Implementation)
TASK-006: Create hiring GUI screen (Integration)
TASK-007: Add network packets for hiring (Integration)
TASK-008: Sync guard data to client (Integration)
TASK-009: Optimize pathfinding (Polish)
TASK-010: Add visual effects (Polish)
```

## Common Mistakes

### ❌ Too Vague
```markdown
## TASK-001: Add guards
**Description**: Implement guard system
```

**✅ Fixed**:
```markdown
## TASK-001: Create GuardEntity class
**Description**: Create GuardEntity class extending VillagerEntity.
Implement custom AI goals for attacking hostile mobs and following players.
Register entity type in ModRegistry with identifier "guard".
Include spawn egg item with correct texture.
```

### ❌ Business Language in Technical Task
```markdown
## TASK-001: Allow players to hire guards
**Description**: Players should be able to right-click villagers and
pay emeralds to hire them as guards for village defense.
```

**✅ Fixed**:
```markdown
## TASK-001: Implement guard hiring interaction
**Description**: Add right-click interaction handler on VillagerEntity.
Check if player has sufficient emeralds (50 for Archer type).
If yes: deduct emeralds, convert VillagerEntity to GuardEntity,
sync to client via S2C packet.
If no: send failure message to player.
```

### ❌ Missing Acceptance Criteria
```markdown
**Acceptance Criteria**:
- [ ] Done
```

**✅ Fixed**:
```markdown
**Acceptance Criteria**:
- [ ] Right-clicking villager opens hiring GUI
- [ ] GUI shows all 3 guard types with costs
- [ ] Hiring deducts correct emerald amount
- [ ] Guard spawns with correct type and equipment
- [ ] Transaction syncs to all online players
- [ ] Code follows coding-standards skill
```

## Integration with Workflow

### planning-agent Creates plan.md
1. User validates requirements.md
2. User runs `/create_plan`
3. planning-agent reads requirements.md
4. planning-agent creates plan.md following this skill
5. planning-agent asks user to activate epic

### During Execution
1. User runs `/next`
2. planning-agent reads next TODO task
3. planning-agent invokes assigned agent
4. Agent completes task
5. planning-agent updates task status

### Task Updates
- Status changes: TODO → IN_PROGRESS → COMPLETED
- Blockers added/removed as discovered
- Notes added during implementation
- Sub-tasks created via `/fix` command

## Task vs Requirements

| Aspect | requirements.md | plan.md |
|--------|-----------------|----------|
| **Focus** | What & Why | How |
| **Audience** | Stakeholders | Developers |
| **Language** | Business | Technical |
| **Details** | User experience | Code implementation |
| **Success** | Player value | Technical completion |
| **Created By** | epic-agent | planning-agent |
| **Example** | "Players hire guards for 50 emeralds" | "Implement GuardHiringHandler with emerald transaction" |

## When to Use This Skill

Use this skill when:
- Creating tasks via `/create_plan` command
- planning-agent breaks down requirements into implementation
- Adding new task via `/add_task` command
- Creating bug fix sub-task via `/fix` command
- Reviewing task structure and quality
- Questions about "How should I structure tasks?"
- Ensuring tasks are technical and actionable
