# Multi-Project Minecraft Modding Workspace

This file provides guidance to Claude Code when working on the **Minecraft Multi-Project Workspace** containing multiple Minecraft 1.21.1 Fabric mods.

## Workspace Overview

This is a **multi-project workspace** for Minecraft Fabric mod development. All projects share common infrastructure (agents, commands, standards) while maintaining independent codebases and configurations.

### Active Projects
- **xeena-village-manager** - Villager management mod with GUI interface and guard ranking system
- **xeenaa-fabric-enhancements** - Performance-focused gameplay enhancements
- **xeenaa-soulbound** - Soulbound items functionality (planned)

### Technical Stack (All Projects)
- **Minecraft**: 1.21.1
- **Mod Loader**: Fabric
- **Java**: 21
- **Dependencies**: Fabric API
- **Build System**: Gradle with Fabric Loom

## Workspace Architecture

### Root Structure
```
workspace/
├── .claude/                           # SHARED infrastructure
│   ├── agents/                        # Common agent definitions
│   │   ├── implementation-agent.md
│   │   ├── validation-agent.md
│   │   ├── research-agent.md
│   │   ├── project-agent.md
│   │   ├── epic-agent.md
│   │   └── planning-agent.md
│   ├── commands/                      # Global commands
│   │   ├── create_project.md         # Create new project from template
│   │   ├── create_epic.md            # Create new epic with requirements
│   │   ├── create_plan.md            # Generate tasks from requirements
│   │   ├── add_task.md               # Add task to current epic
│   │   ├── fix.md                    # Create bug fix sub-task
│   │   ├── research.md               # Research and document findings
│   │   ├── next.md                   # Execute next task
│   │   └── serve_client.md           # Launch client
│   ├── skills/                        # Shared knowledge skills
│   │   ├── minecraft-modding/         # Minecraft core systems (loader-agnostic)
│   │   ├── fabric-modding/            # Fabric API specific
│   │   ├── minecraft-performance/     # Performance optimization strategies
│   │   ├── performance-testing/       # Performance measurement and validation
│   │   ├── coding-standards/          # Code standards and conventions
│   │   ├── ui-ux-design/              # GUI design and implementation
│   │   ├── java-development/          # Java 21 best practices
│   │   ├── research-methodology/      # Research process
│   │   ├── epic-requirements/         # requirements.md creation
│   │   ├── task-planning/             # tasks.md creation
│   │   ├── defensive-programming/     # Regression testing strategies
│   │   └── logging-strategy/          # Proactive logging guidelines
│   ├── current_project.txt            # Active project tracker
│   └── CLAUDE.md                      # This file
│
├── <project>/                         # PROJECT-SPECIFIC
│   ├── .claude/
│   │   ├── project.md                # Project overview
│   │   ├── research/                 # Project-specific research
│   │   ├── temp/                     # Temporary files
│   │   ├── ui/                       # UI mockups and specifications (if applicable)
│   │   ├── game-design/              # Game mechanics (if applicable)
│   │   └── epics/                    # Epic-based organization
│   │       ├── 01-epic-name/
│   │       │   ├── requirements.md   # Business requirements (from /create_epic)
│   │       │   └── tasks.md          # Technical tasks (from /create_plan)
│   │       ├── 02-another-epic/
│   │       │   ├── requirements.md
│   │       │   └── tasks.md
│   │       └── CURRENT_EPIC.md       # Epic tracking and status
│   ├── src/                          # Source code
│   └── build.gradle                  # Build configuration
│
└── mod_template/                      # Template for new projects
```

## Working with Multiple Projects

### CRITICAL: Always Set Project Context First

Before working on ANY project, you MUST set the active project context. The user will request this naturally:

**Natural language examples**:
- "Set project to xeena-village-manager"
- "Switch to xeenaa-fabric-enhancements"
- "Work on xeenaa-soulbound"

**Available projects**:
- `xeena-village-manager`
- `xeenaa-fabric-enhancements`
- `xeenaa-soulbound`

**When user requests project switch, you must**:
1. Validate the requested project name exists
2. Update `.claude/current_project.txt` with the project name (format: `CURRENT_PROJECT=<project_name>`)
3. Confirm the switch and show project status
4. All subsequent commands will target this project automatically

### Project Context System

The active project is stored in `.claude/current_project.txt`. All commands and agents:
1. Read this file to determine the active project
2. Automatically use project-specific paths (`{{project}}/.claude/...`)
3. Reference shared resources from root `.claude/` when appropriate

### Command Flow

```
User says: "Set project to xeena-village-manager"
→ Claude validates project exists
→ Claude updates: .claude/current_project.txt
→ Claude confirms: "Switched to xeena-village-manager"

User runs: /next
→ Reads: .claude/current_project.txt (gets "xeena-village-manager")
→ Reads: xeena-village-manager/.claude/tasks.md
→ Uses: coding-standards skill (automatically activated)
→ Executes task for xeena-village-manager
```

## Agent-Driven Development

### Specialized Agents (Shared Across All Projects)

#### 🏗️ project-agent
**Expertise**: Project creation, project.md maintenance, workspace setup
- Creates new projects from mod_template
- Writes project.md (vision, technical stack, architecture)
- Sets up .claude/ infrastructure for new projects
- Maintains project-level scope and documentation

#### 🔧 implementation-agent
**Expertise**: Java development, Fabric API, Minecraft 1.21.1 modding
- **ONLY** agent that writes code
- Implements all features (architecture + implementation + UI/UX)
- Uses coding-standards, minecraft-modding, fabric-modding, and other relevant skills
- Stores research in `{{project}}/.claude/research/`
- Uses `{{project}}/.claude/temp/` for temporary files

#### 📋 epic-agent
**Expertise**: Epic creation, business requirements, game mechanics design
- Creates epic folder structure (`{{project}}/.claude/epics/##-name/`)
- Writes requirements.md (business-focused, stakeholder-friendly)
- Designs game mechanics when applicable (progression, economy, combat)
- Updates CURRENT_EPIC.md with epic tracking

#### 🧪 validation-agent
**Expertise**: Quality assurance, testing strategies
- Creates tests AFTER user manual validation
- Defines QA procedures in epic-specific folders
- Establishes acceptance criteria
- Validates implementations meet quality standards

#### 🔍 research-agent
**Expertise**: Mod research, performance analysis, documentation
- Researches optimization techniques and mod compatibility
- Downloads documentation to `{{project}}/.claude/research/`
- Provides technical recommendations
- Avoids redundant research by checking existing files

#### 👔 planning-agent
**Expertise**: Task orchestration, workflow management
- Converts features into tasks
- Invokes appropriate agents for each task
- Updates task status in `{{project}}/.claude/tasks.md`
- Orchestrates multi-step workflows

### Agent Collaboration Pattern

```
STEP 1: /create_epic "Guard Armor Customization"
│
└─→ epic-agent
    ├─ Reads: {{project}}/.claude/epics/CURRENT_EPIC.md
    ├─ Creates: {{project}}/.claude/epics/05-guard-armor/requirements.md
    ├─ Designs: Business requirements and game mechanics
    └─ Waits: User validation of requirements.md

USER VALIDATES requirements.md

STEP 2: /create_plan
│
└─→ planning-agent
    ├─ Reads: {{project}}/.claude/epics/05-guard-armor/requirements.md
    ├─ Uses: task-planning skill
    ├─ Creates: {{project}}/.claude/epics/05-guard-armor/tasks.md
    └─ Asks: User to confirm epic activation

STEP 3: /next (executed by user)
│
├─→ planning-agent
│   ├─ Reads: {{project}}/.claude/epics/05-guard-armor/tasks.md
│   └─ Invokes: implementation-agent
│
└─→ implementation-agent
    ├─ Uses: coding-standards, minecraft-modding, fabric-modding skills
    ├─ Reads: {{project}}/.claude/epics/05-guard-armor/requirements.md
    ├─ Implements: Feature code
    ├─ Stores: Research in {{project}}/.claude/research/
    └─ Updates: Task status
```

## Skills System (Shared Knowledge)

Skills provide specialized knowledge that agents automatically use when relevant. Unlike agents (which perform actions) and commands (which users invoke), skills are **model-invoked** based on context.

### Available Skills

#### 🎮 minecraft-modding
**Knowledge**: General Minecraft 1.21.1 game mechanics and systems (loader-agnostic)
**Activates When**: Understanding how Minecraft works fundamentally, performance questions, vanilla mechanics
**Contains**:
- Game loop and ticking system
- Entity system and AI goals
- Data components (1.21.1 vanilla feature)
- Client-server architecture concepts
- World/chunk structure
- Performance optimization strategies
- Resource system (assets vs data)
- Common pitfalls and best practices

#### 🔧 fabric-modding
**Knowledge**: Fabric API and Fabric Loader specific implementation
**Activates When**: Working with Fabric API, Fabric events, mixins, Fabric-specific patterns
**Contains**:
- Fabric mod lifecycle (ModInitializer, ClientModInitializer)
- Fabric events system (prefer events over mixins)
- Fabric registry patterns
- Fabric networking (CustomPayload, PacketCodec)
- Fabric GUI system (ScreenHandler, HandledScreen)
- Fabric mixins (when to use, injection points)
- Client-server separation patterns
- Fabric resources and documentation

#### ⚡ minecraft-performance
**Knowledge**: Performance optimization strategies, profiling, tick budget management
**Activates When**: Implementing performance-critical features, diagnosing lag, optimization questions
**Contains**:
- Tick budget management (50ms constraint, allocation strategies)
- Entity optimization (tick throttling, distance-based updates, pathfinding caching)
- Memory optimization (object pooling, caching strategies, GC pressure reduction)
- Network optimization (packet size reduction, batching, delta updates)
- Chunk and world performance (chunk loading, spatial partitioning)
- Profiling tools (Spark, JFR, F3 debug screen)
- Common performance pitfalls (O(n²) algorithms, excessive serialization, world queries)
- Performance testing requirements (before/after benchmarks)
- CPU optimization (algorithm complexity, lazy evaluation)
- Performance budget guidelines

#### 📊 performance-testing
**Knowledge**: Performance measurement and validation methodology for Minecraft mods
**Activates When**: Validating optimizations, measuring baseline performance, creating test plans, reporting results
**Contains**:
- Testing methodology (6-phase process: baseline → implement → retest → validate → report → monitor)
- Profiling tools (Spark Profiler, Java Flight Recorder, Minecraft debug, custom monitoring)
- Test scenarios (chunk loading stress, world loading, server stress, long-term stability)
- Key metrics (TPS, MSPT, memory, chunk operations, GC frequency)
- Performance targets (improvement ranges, success criteria, statistical requirements)
- Data collection templates (baseline results, test configuration, observations)
- Statistical validation (t-tests, confidence intervals, reproducibility)
- Reporting templates (executive summary, results tables, statistical analysis)
- Regression detection (automated tests, performance thresholds, alerts)
- Real-world validation (beta testing, player feedback, production monitoring)

#### 📏 coding-standards
**Knowledge**: Java and Minecraft modding coding standards, naming conventions, architecture patterns
**Activates When**: Writing any code, creating classes/methods, organizing packages, documenting code
**Contains**:
- Naming conventions (PascalCase, camelCase, UPPER_SNAKE_CASE for all Java elements)
- Package organization (standard structure, responsibility rules)
- Code style and formatting (indentation, braces, line length)
- Documentation standards (Javadoc requirements for all public APIs)
- Mixin standards and restrictions (when to use, naming, organization)
- Client-server separation rules (package separation, environment checking)
- Architecture patterns (singleton registry, factory, builder)
- Error handling standards (input validation, exception types, fail fast)
- Version control standards (commit messages, branch naming)
- Mod compatibility guidelines (defensive coding, deprecation)
- Security and validation (never trust client input)
- Standards compliance checklist

#### 🎨 ui-ux-design
**Knowledge**: GUI design and implementation for Minecraft screens, HandledScreen patterns, layout specifications
**Activates When**: Designing custom GUIs, implementing screens, creating UI mockups, positioning widgets
**Contains**:
- UI folder system (mockups, specifications, source files)
- Minecraft GUI constraints (HandledScreen, texture coordinates, standard dimensions)
- Layout specification template (exact dimensions, ASCII diagrams, component positions)
- UI behavior specification template (workflows, states, networking, accessibility)
- Minecraft GUI best practices (vanilla aesthetics, grid layouts, responsive positioning)
- Widget implementation patterns (buttons, custom widgets, tabs, scrollable lists)
- Texture atlas mapping (256×256 layout, U/V coordinates)
- Performance optimization (render culling, cached calculations)
- Integration with epic/implementation workflow

#### ☕ java-development
**Knowledge**: Java 21 features, records, pattern matching, sealed classes, modern best practices
**Activates When**: Writing Java code, refactoring, Java-specific questions
**Contains**:
- Records and pattern matching
- Text blocks and sealed classes
- Stream API and Optional
- Modern Java patterns
- SOLID principles
- Error handling best practices
- Common anti-patterns to avoid

#### 🔍 research-methodology
**Knowledge**: Systematic research process, documentation, investigation methods
**Activates When**: Researching unknowns, via `/research` command, investigating how systems work
**Contains**:
- Research process (5 steps)
- Research methods priority (docs → code → mods → community)
- Research document template
- Quality standards
- Executive summary format
- When to research vs implement

#### 📋 epic-requirements
**Knowledge**: Business requirements creation, requirements.md structure, templates
**Activates When**: Creating epics via `/create_epic`, defining business requirements
**Contains**:
- Requirements.md template
- Business language guidelines
- Feature structure (description, UX, success criteria)
- Game mechanics specification
- Quality checklist
- Business vs technical separation

#### 📝 task-planning
**Knowledge**: Technical task creation, tasks.md structure, templates
**Activates When**: Creating tasks via `/create_plan`, breaking down features
**Contains**:
- Tasks.md template
- Task numbering system (main vs sub-tasks)
- Task phases (Foundation → Implementation → Integration → Polish)
- Agent assignment guidelines
- Technical specification guidelines
- Task vs requirements separation

#### 🛡️ defensive-programming
**Knowledge**: Regression testing strategies, test coverage, defensive coding
**Activates When**: Implementing features, writing tests, preventing regressions
**Contains**:
- Test-first development workflow (test existing before modifying)
- Regression test suite organization (unit/, integration/, regression/)
- Comprehensive test coverage requirements (100% critical paths, 90% business logic)
- Change impact analysis process
- Integration testing patterns (test feature interactions)
- Test quality standards (FIRST principles)
- Defensive coding patterns (input validation, null safety, immutability)
- Test organization and naming conventions

#### 📊 logging-strategy
**Knowledge**: Proactive logging strategies, structured logging, log levels
**Activates When**: Implementing features, debugging, adding observability
**Contains**:
- Proactive vs reactive logging approach
- SLF4J log levels (ERROR, WARN, INFO, DEBUG, TRACE) with guidelines
- 7 critical logging points (entry, errors, state changes, external calls, performance, business logic, data transforms)
- Structured logging patterns (parameterized messages, context inclusion)
- Method boundary logging
- Performance measurement logging
- Logging checklist for every feature
- Log message best practices

### How Skills Work

**Automatic Activation**:
- Claude reads skill descriptions
- Matches user request to relevant skills
- Applies skill knowledge automatically
- No explicit invocation needed

**Example Flow**:
```
User: "Create epic for guard towers"

Claude recognizes: Creating epic
→ Activates: epic-requirements skill
→ Uses: Template and guidelines from skill
→ Creates: requirements.md following skill rules

User: "How do I sync entity data to client?"

Claude recognizes: Minecraft networking question
→ Activates: minecraft-modding skill
→ Provides: Networking guidance from skill
→ References: Packet patterns and thread safety rules
```

### Skills Location

All skills stored in: `.claude/skills/`

Each skill directory contains:
- `SKILL.md` - Main skill instructions (required)
- `template.md` - Templates for creation (optional)
- `examples/` - Example files (optional)

### Skills vs Agents vs Commands

| Aspect | Skills | Agents | Commands |
|--------|--------|--------|----------|
| **Invocation** | Automatic (model) | Invoked by planning-agent | User types /command |
| **Purpose** | Provide knowledge | Perform actions | Trigger workflows |
| **Examples** | java-development | implementation-agent | /create_epic |
| **Contains** | Guidelines, templates | Responsibilities, process | Steps, agent invocation |
| **Tool Access** | Restricted via frontmatter | All tools available | Depends on agent |

### Using Skills Effectively

**Agents reference skills**:
- epic-agent uses epic-requirements skill when creating requirements.md
- planning-agent uses task-planning skill when creating tasks.md
- implementation-agent uses coding-standards, minecraft-modding, fabric-modding, minecraft-performance, performance-testing, ui-ux-design, java-development, defensive-programming, and logging-strategy skills when coding
- validation-agent uses defensive-programming, logging-strategy, and performance-testing skills when creating tests
- research-agent uses research-methodology skill when investigating

**All knowledge consolidated in skills**:
- **coding-standards**: Code rules (naming, patterns, architecture, documentation)
- **minecraft-modding**: How Minecraft systems work (entities, ticking, world structure)
- **fabric-modding**: Fabric API knowledge (events, networking, registries)
- **minecraft-performance**: Performance optimization (profiling, caching, tick budget)
- **performance-testing**: Performance measurement and validation (baselines, profiling tools, statistical analysis)
- **ui-ux-design**: GUI design (HandledScreen, layouts, mockups, specifications)
- **defensive-programming**: Testing strategies and regression prevention
- **logging-strategy**: Proactive logging and observability

**Benefits**:
- ✅ Consistent output format (templates ensure uniformity)
- ✅ Best practices applied automatically
- ✅ Reduces agent context size (knowledge externalized)
- ✅ Easy to update (change skill file, all agents benefit)
- ✅ Discoverable (Claude finds relevant skills)

## Command Decision Guide

**When should I use which command?**

| Situation | Command | Creates |
|-----------|---------|---------|
| Creating a brand new mod project | `/create_project <name>` | Complete project from template |
| Starting a major new feature area | `/create_epic <name>` | Epic folder with requirements.md |
| Epic requirements validated, ready for tasks | `/create_plan` | tasks.md with technical breakdown |
| Need to add a task to current epic | `/add_task <description>` | TASK-015 (new main task) |
| Found a bug in existing task | `/fix <description>` | TASK-001.1 (sub-task) |
| Don't know how to implement something | `/research <question>` | Research file in .claude/research/ |
| Ready to work on next task | `/next` | Executes next TODO task |
| Want to test in Minecraft | `/serve_client` | Launches game |

**Task Numbering**:
- Main tasks: `TASK-001`, `TASK-002`, `TASK-015` (sequential)
- Bug fixes: `TASK-001.1`, `TASK-001.2` (sub-tasks of main task)

## Development Workflows

### 1. Creating a New Epic and Adding Features

```bash
# 1. Set project context
User: "Set project to xeena-village-manager"

# 2. Create epic with business requirements
/create_epic "Guard Armor Customization System"

→ epic-agent creates:
  - xeena-village-manager/.claude/epics/05-guard-armor/requirements.md
  - Business requirements, game mechanics, success criteria
→ Stops and waits for user validation

# 3. User reviews and validates requirements.md
User: [Reviews the requirements file, makes edits if needed]

# 4. Generate technical implementation plan
/create_plan

→ planning-agent creates:
  - xeena-village-manager/.claude/epics/05-guard-armor/tasks.md
  - Technical tasks broken down from requirements
→ Asks: Should this become the active epic?

# 5. Execute tasks
/next
→ Executes first task from epic
→ Stops and waits for user confirmation

# 6. Continue until epic complete
/next (repeat for each task)
```

### 2. Fixing a Bug

```bash
# 1. Set project context
User: "Set project to xeena-village-manager"

# 2. Report bug and create fix task
/fix "Guards lose armor on server restart"

→ planning-agent asks: "Which task is this bug related to?"
User: "TASK-004" (or describes the related feature)

→ planning-agent creates:
  - xeena-village-manager/.claude/epics/04-multiplayer/tasks.md
  - New sub-task: TASK-004.1 (bug fix task)
→ Reports: "Bug fix task created, run /next to execute"

# 3. Execute the fix
/next

→ Reads: TASK-004.1 from epic tasks
→ implementation-agent investigates and fixes bug
→ Reports fix and requests user validation

# 4. Validate fix manually
User: [Tests the fix in-game]

# 5. Continue with next task
/next (marks TASK-004.1 as completed, proceeds to next task)
```

### 3. Adding a Task to Existing Epic

```bash
# 1. Set project context
User: "Set project to xeena-village-manager"

# 2. Realize you need a new feature mid-epic
# Epic 04 has TASK-001 through TASK-014
# You need: "Add permission system for guard management"

/add_task "Add permission system for guard management"

→ planning-agent reads current epic (Epic 04)
→ Determines next task number: TASK-015
→ Asks: "What priority? (Critical/High/Medium/Low)"
User: "High"

→ Asks: "Estimated effort in hours? (1-2/3-4/5-8/8+)"
User: "5-8"

→ Asks: "Which agent? (implementation-agent/research-agent/validation-agent)"
User: "implementation-agent" (or just press enter for default)

→ Creates TASK-015 in epic's tasks.md
→ Updates task count
→ Reports: "Task TASK-015 created, run /next when ready"

# 3. Continue with current work or execute new task
/next (will execute tasks in order, including new TASK-015)
```

### 4. Testing in Minecraft

```bash
# 1. Set project context
User: "Set project to xeena-village-manager"

# 2. Launch client
/serve_client

→ Changes to: xeena-village-manager/ directory
→ Runs: ./gradlew build (if needed)
→ Runs: ./gradlew runClient
```

### 5. Switching Between Projects

```bash
# Working on village manager
User: "Set project to xeena-village-manager"
Claude: [Updates .claude/current_project.txt]
User: /next

# Switch to fabric enhancements
User: "Switch to xeenaa-fabric-enhancements"
Claude: [Updates .claude/current_project.txt]
User: /next

# All commands now target xeenaa-fabric-enhancements
```

### 6. Researching Unknown Systems

```bash
# 1. Set project context
User: "Set project to xeena-village-manager"

# 2. Encounter unknown during task execution
# You're working on TASK-008: "Add custom villager textures"
# You don't know how Fabric handles dynamic texture switching

/research "How does Fabric/Minecraft handle dynamic entity texture switching for villagers?"

→ research-agent checks existing research first
→ Searches: xeena-village-manager/.claude/research/
→ No existing research found

→ Conducts research:
  - Reads Fabric API documentation
  - Searches for VillagerEntityRenderer patterns
  - Checks successful mod implementations
  - Tests texture resource location system

→ Creates: xeena-village-manager/.claude/research/villager-texture-switching.md

→ Provides immediate summary:
  "Research complete! ✅

  Quick Answer: Use VillagerEntityRenderer.getTexture() override
  with dynamic ResourceLocation based on villager profession data.

  Key Findings:
  - Textures: assets/[modid]/textures/entity/villager/
  - Register: EntityRendererRegistry.register() in client init
  - Dynamic: Override getTexture() method

  Recommended: Create custom renderer, read GuardData component,
  return appropriate texture ResourceLocation.

  Full research saved to: .claude/research/villager-texture-switching.md"

# 3. implementation-agent can now reference this research
/next (continues with TASK-008, reads research file for implementation guidance)
```

### 7. Creating a New Project

```bash
# Create brand new mod project from template
/create_project "Xeenaa Economy System"

→ project-agent validates name
→ Asks user:
  - Author name?
  - Short description?
  - Project vision?
  - Scope (what's in/out)?

→ Copies mod_template to xeenaa-economy-system/
→ Updates placeholders (mod ID, display name, author, etc.)
→ Creates .claude/ infrastructure:
  - project.md (vision, technical stack)
  - research/ (empty)
  - temp/ (empty)
  - game-design/ (empty)
  - epics/CURRENT_EPIC.md (initial state)

→ Sets as active project
→ Reports: "Project created! Review project.md, then run /create_epic"

# Now you can create first epic
/create_epic "Core Economy System"
```

## File Organization and Responsibilities

### Shared Resources (Root `.claude/`)

| File/Folder | Purpose | Managed By |
|------------|---------|------------|
| `agents/` | Agent definitions | Manual/Setup |
| `commands/` | Global commands | Manual/Setup |
| `standards/` | Coding standards | Manual/team-lead-coordinator |
| `current_project.txt` | Active project tracker | Natural language (Claude updates) |
| `CLAUDE.md` | This file | Manual/Documentation |

### Project-Specific (Each `{{project}}/.claude/`)

| File/Folder | Purpose | Managed By |
|------------|---------|------------|
| `project.md` | Project overview and vision | project-agent |
| `research/` | Research findings | research-agent (via /research) |
| `temp/` | Temporary files | implementation-agent |
| `game-design/` | Game mechanics | epic-agent |
| `epics/CURRENT_EPIC.md` | Epic tracking and status | planning-agent |
| `epics/##-name/requirements.md` | Business requirements | epic-agent (via /create_epic) |
| `epics/##-name/tasks.md` | Technical implementation tasks | planning-agent (via /create_plan) |

## Task Management System

### Task Tracking
All work is organized in epic-based tasks:
- **Epic requirements**: `{{project}}/.claude/epics/##-epic-name/requirements.md` (business-oriented, created by `/create_epic`)
- **Epic tasks**: `{{project}}/.claude/epics/##-epic-name/tasks.md` (technical implementation, created by `/create_plan`)
- **Epic status**: `{{project}}/.claude/epics/CURRENT_EPIC.md` (tracks all epics and current active epic)

### Task Structure
```markdown
## Task: [TODO/IN_PROGRESS/COMPLETED] Task Title

**Agent**: implementation-agent
**Goal**: Clear description of what needs to be done

**Requirements**:
- Specific requirement 1
- Specific requirement 2

**Acceptance Criteria**:
- Criteria 1
- Criteria 2

**Notes**:
- Implementation details
- Bug fixes
- Research references
```

### No Separate Changelogs or Issue Trackers
- ❌ **No `changelog.md`** - History tracked in task status changes
- ❌ **No `bugs.md`** - Bugs tracked as tasks
- ❌ **No `issuestracker.md`** - All issues tracked in tasks
- ✅ **Single source of truth**: `tasks.md`

## Standards and Code Quality

### Shared Coding Standards
All projects MUST follow: **coding-standards skill** (automatically activated when writing code)

Key standards:
- Java 21 modern features (records, pattern matching, text blocks)
- Fabric API best practices (prefer events over mixins)
- Client-server separation
- Performance-first mindset
- Comprehensive error handling
- Thread safety considerations

### Code Quality Requirements
- All public methods have Javadoc
- Input validation for all public methods
- Proper exception handling
- Performance impact documented
- Tests written (by validation-agent after user validation)

### Before Every Implementation
1. Apply coding-standards skill (naming, organization, documentation)
2. Review `{{project}}/.claude/project.md`
3. Check `{{project}}/.claude/research/` for relevant findings
4. Understand task requirements and acceptance criteria

## Development Commands

### Build Commands (run from project directory)
```bash
./gradlew build          # Build the mod
./gradlew runClient      # Run Minecraft client
./gradlew runServer      # Run dedicated server
./gradlew genSources     # Generate Minecraft sources
./gradlew test           # Run tests
```

### Workflow Commands (project-aware)
```bash
/create_project <name>   # Create new project from template
/create_epic <name>      # Create new epic with business requirements
/create_plan             # Generate technical tasks from validated requirements
/add_task <description>  # Add new task to current epic (e.g., TASK-015)
/fix <description>       # Create bug fix sub-task (e.g., TASK-001.1)
/research <question>     # Research technical question, save findings
/next                    # Execute next task
/serve_client            # Build and launch client
```

**Note**: Project switching is done via natural language (e.g., "Set project to xeena-village-manager")

## Project-Specific Guidelines

### xeena-village-manager
- Focus: Villager management GUI and guard system
- Key patterns: Client-server networking, registry singleton
- Special considerations: Guard ranking system, emerald economy
- Research location: `xeena-village-manager/.claude/research/`

### xeenaa-fabric-enhancements
- Focus: Performance optimizations
- Key requirement: ALL features must show measurable performance improvement
- Testing: Before/after benchmarks MANDATORY
- Epic structure: Performance targets defined per epic
- Research location: `xeenaa-fabric-enhancements/.claude/research/`

### xeenaa-soulbound
- Status: Not started
- Setup: Use mod_template as base
- Research location: `xeenaa-soulbound/.claude/research/`

## Creating a New Project

Use the `/create_project` command to create a complete project from template:

```bash
/create_project "Xeenaa New Mod"

→ project-agent validates name (must be kebab-case)
→ Asks for: author, description, vision, scope
→ Copies mod_template to xeenaa-new-mod/
→ Updates all placeholders (mod ID, display name, etc.)
→ Creates .claude/ infrastructure:
  - project.md
  - research/
  - temp/
  - game-design/
  - epics/CURRENT_EPIC.md
→ Sets as active project
→ Ready to run /create_epic
```

**Manual steps** (if you prefer not to use /create_project):

1. **Copy template**: `cp -r mod_template new-project-name`
2. **Create folders**: `mkdir -p new-project-name/.claude/{research,temp,epics,game-design}`
3. **Create project.md**: Customize project vision and technical stack
4. **Update placeholders**: In gradle.properties, fabric.mod.json
5. **Set as active**: "Set project to new-project-name"

## Best Practices for Multi-Project Work

### ✅ DO
- Always set the active project (via natural language) before starting work
- Keep project research in project-specific folders
- Apply shared skills (coding-standards, minecraft-modding, etc.) consistently
- Use consistent task structure across projects
- Document project-specific patterns in `project.md`

### ❌ DON'T
- Work on multiple projects simultaneously without switching context
- Duplicate standards or agents across projects
- Create changelog or issuestracker files (use tasks.md)
- Hard-code project names in agents or commands
- Skip manual testing before QA creates automated tests

## Agent Invocation Guidelines

### When to Use Each Agent

| Scenario | Agent | Reason |
|----------|-------|--------|
| Create new project | project-agent | Creates project from template, writes project.md |
| Create new epic | epic-agent | Creates epic requirements and game mechanics |
| Break feature into tasks | planning-agent | Creates task breakdowns |
| Implement code | implementation-agent | ONLY agent that writes code |
| Research optimization | research-agent | Downloads docs, analyzes mods |
| Define tests | validation-agent | Creates tests after user validation |

### Agent Context Requirements

When invoking an agent, always provide:
1. **Project name**: Which project is being worked on
2. **Project context**: Path to `{{project}}/.claude/project.md`
3. **Research**: Path to `{{project}}/.claude/research/`
4. **Task details**: Clear goals and acceptance criteria
5. **Skills**: Relevant skills automatically activated (coding-standards, minecraft-modding, etc.)

## Troubleshooting

### "No current project set" Error
**Solution**: Ask user which project to work on, then update `.claude/current_project.txt`

### Agent can't find files
**Cause**: Project context not set or wrong paths
**Solution**:
1. Verify `.claude/current_project.txt` has correct project
2. Ensure using `{{project}}/` path placeholders
3. Check file exists in correct project folder

### Conflicting coding styles between projects
**Cause**: Not consistently applying coding-standards skill
**Solution**: Ensure coding-standards skill is activated for all code (happens automatically)

### Lost task history
**Cause**: Looking for changelog.md or bugs.md
**Solution**: All history is in `{{project}}/.claude/tasks.md` - check task status changes

## Key Principles

1. **Shared Infrastructure** - Agents, commands, and skills defined once, used everywhere
2. **Project Isolation** - Each mod is independent with its own code and configuration
3. **Context Awareness** - All commands respect the active project setting
4. **Single Source of Truth** - Tasks.md is the only tracking mechanism
5. **Skills-Based Knowledge** - All code follows coding-standards skill automatically
6. **Agent Specialization** - Each agent has clear, non-overlapping responsibilities
7. **User Validation** - Manual testing before automated test creation

---

**Remember**: This is a multi-project workspace. ALWAYS ensure the project context is set (ask user if unclear, then update `.claude/current_project.txt`) before executing any other commands. When in doubt, check `.claude/current_project.txt` to see which project is active.
