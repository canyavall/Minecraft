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
│   │   ├── minecraft-developer.md
│   │   ├── minecraft-qa-specialist.md
│   │   ├── minecraft-researcher.md
│   │   ├── project-scope-manager.md
│   │   └── tech-lead-coordinator.md
│   ├── commands/                      # Global commands
│   │   ├── set_project.md            # Switch active project
│   │   ├── next.md                   # Execute next task
│   │   ├── fix.md                    # Fix bugs
│   │   ├── new_feat.md               # Add features
│   │   └── serve_client.md           # Launch client
│   ├── standards/                     # Shared coding standards
│   │   └── minecraft-modding-standards.md
│   ├── current_project.txt            # Active project tracker
│   └── CLAUDE.md                      # This file
│
├── <project>/                         # PROJECT-SPECIFIC
│   ├── .claude/
│   │   ├── project.md                # Features and specifications
│   │   ├── tasks.md                  # Task tracking
│   │   ├── research/                 # Project-specific research
│   │   ├── temp/                     # Temporary files
│   │   ├── epics/                    # Epic-based organization
│   │   └── game-design/              # Game mechanics (if applicable)
│   ├── src/                          # Source code
│   └── build.gradle                  # Build configuration
│
└── mod_template/                      # Template for new projects
```

## Working with Multiple Projects

### CRITICAL: Always Set Project Context First

Before working on ANY project, you MUST set the active project context:

```bash
/set_project <project_name>
```

**Available projects**:
- `xeena-village-manager`
- `xeenaa-fabric-enhancements`
- `xeenaa-soulbound`

All subsequent commands will target this project automatically.

### Project Context System

The active project is stored in `.claude/current_project.txt`. All commands and agents:
1. Read this file to determine the active project
2. Automatically use project-specific paths (`{{project}}/.claude/...`)
3. Reference shared resources from root `.claude/` when appropriate

### Command Flow

```
User runs: /set_project xeena-village-manager
→ Updates: .claude/current_project.txt

User runs: /next
→ Reads: .claude/current_project.txt (gets "xeena-village-manager")
→ Reads: xeena-village-manager/.claude/tasks.md
→ Reads: .claude/standards/minecraft-modding-standards.md (shared)
→ Executes task for xeena-village-manager
```

## Agent-Driven Development

### Specialized Agents (Shared Across All Projects)

#### 🔧 minecraft-developer
**Expertise**: Java development, Fabric API, Minecraft 1.21.1 modding
- **ONLY** agent that writes code
- Implements all features (architecture + implementation + UI/UX)
- Follows `.claude/standards/minecraft-modding-standards.md`
- Stores research in `{{project}}/.claude/research/`
- Uses `{{project}}/.claude/temp/` for temporary files

#### 📋 project-scope-manager
**Expertise**: Feature management, epic breakdown, task organization
- Manages `{{project}}/.claude/project.md`
- Creates epics in `{{project}}/.claude/epics/`
- Designs game mechanics when applicable
- Prioritizes features based on business value

#### 🧪 minecraft-qa-specialist
**Expertise**: Quality assurance, testing strategies
- Creates tests AFTER user manual validation
- Defines QA procedures in epic-specific folders
- Establishes acceptance criteria
- Validates implementations meet quality standards

#### 🔍 minecraft-researcher
**Expertise**: Mod research, performance analysis, documentation
- Researches optimization techniques and mod compatibility
- Downloads documentation to `{{project}}/.claude/research/`
- Provides technical recommendations
- Avoids redundant research by checking existing files

#### 👔 tech-lead-coordinator
**Expertise**: Task orchestration, workflow management
- Converts features into tasks
- Invokes appropriate agents for each task
- Updates task status in `{{project}}/.claude/tasks.md`
- Orchestrates multi-step workflows

### Agent Collaboration Pattern

```
/new_feat "Add guard armor customization"
│
├─→ project-scope-manager
│   ├─ Reads: {{project}}/.claude/project.md
│   ├─ Designs: Feature and game mechanics
│   └─ Updates: {{project}}/.claude/project.md
│
├─→ tech-lead-coordinator
│   ├─ Reads: Updated project.md
│   ├─ Creates: Tasks in {{project}}/.claude/tasks.md
│   └─ Asks: User for prioritization
│
└─→ /next (executed by user)
    ├─→ tech-lead-coordinator
    │   ├─ Reads: {{project}}/.claude/tasks.md
    │   └─ Invokes: minecraft-developer
    │
    └─→ minecraft-developer
        ├─ Reads: .claude/standards/minecraft-modding-standards.md
        ├─ Implements: Feature code
        ├─ Stores: Research in {{project}}/.claude/research/
        └─ Updates: Task status
```

## Development Workflows

### 1. Adding a New Feature

```bash
# 1. Set project context
/set_project xeena-village-manager

# 2. Request new feature
/new_feat "Add armor customization for guards"

# Phase 1: project-scope-manager defines feature
→ Updates: xeena-village-manager/.claude/project.md

# Phase 2: tech-lead-coordinator creates tasks
→ Creates: Tasks in xeena-village-manager/.claude/tasks.md
→ Asks: User for prioritization

# 3. Execute tasks
/next
→ Executes first task
→ Stops and waits for user confirmation

# 4. Continue until complete
/next (repeat for each task)
```

### 2. Fixing a Bug

```bash
# 1. Set project context
/set_project xeena-village-manager

# 2. Report bug
/fix "Guards lose armor on server restart"

→ minecraft-developer investigates
→ Fixes bug following shared standards
→ Updates task in xeena-village-manager/.claude/tasks.md
→ Reports fix and requests user validation

# 3. Validate fix manually

# 4. Confirm completion
→ Developer marks task as completed
```

### 3. Testing in Minecraft

```bash
# 1. Set project context
/set_project xeena-village-manager

# 2. Launch client
/serve_client

→ Changes to: xeena-village-manager/ directory
→ Runs: ./gradlew build (if needed)
→ Runs: ./gradlew runClient
```

### 4. Switching Between Projects

```bash
# Working on village manager
/set_project xeena-village-manager
/next

# Switch to fabric enhancements
/set_project xeenaa-fabric-enhancements
/next

# All commands now target xeenaa-fabric-enhancements
```

## File Organization and Responsibilities

### Shared Resources (Root `.claude/`)

| File/Folder | Purpose | Managed By |
|------------|---------|------------|
| `agents/` | Agent definitions | Manual/Setup |
| `commands/` | Global commands | Manual/Setup |
| `standards/` | Coding standards | Manual/team-lead-coordinator |
| `current_project.txt` | Active project tracker | `/set_project` command |
| `CLAUDE.md` | This file | Manual/Documentation |

### Project-Specific (Each `{{project}}/.claude/`)

| File/Folder | Purpose | Managed By |
|------------|---------|------------|
| `project.md` | Features and phases | project-scope-manager |
| `tasks.md` | Task tracking | tech-lead-coordinator |
| `research/` | Research findings | minecraft-researcher |
| `temp/` | Temporary files | minecraft-developer |
| `epics/` | Epic organization | project-scope-manager |
| `game-design/` | Game mechanics | project-scope-manager |

## Task Management System

### Task Tracking
All work (features, bugs, improvements) is tracked in tasks:
- Main tasks: `{{project}}/.claude/tasks.md`
- Epic-specific: `{{project}}/.claude/epics/{{epic_name}}/tasks.md`

### Task Structure
```markdown
## Task: [TODO/IN_PROGRESS/COMPLETED] Task Title

**Agent**: minecraft-developer
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
All projects MUST follow: `.claude/standards/minecraft-modding-standards.md`

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
- Tests written (by minecraft-qa-specialist after user validation)

### Before Every Implementation
1. Read `.claude/standards/minecraft-modding-standards.md`
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
/set_project <name>      # Set active project
/next                    # Execute next task
/fix <description>       # Fix a bug
/new_feat <description>  # Add a feature
/serve_client            # Build and launch client
```

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

1. **Copy template**:
   ```bash
   cp -r mod_template new-project-name
   ```

2. **Create folder structure**:
   ```bash
   mkdir -p new-project-name/.claude/{research,temp,epics,game-design}
   ```

3. **Create project.md**:
   ```bash
   # Create and customize new-project-name/.claude/project.md
   ```

4. **Update workspace**:
   - Add to `.claude/current_project.txt` list
   - Update README.md

5. **Set as active**:
   ```bash
   /set_project new-project-name
   ```

## Best Practices for Multi-Project Work

### ✅ DO
- Always run `/set_project` before starting work
- Keep project research in project-specific folders
- Reference shared standards from root `.claude/standards/`
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
| Define new feature | project-scope-manager | Manages project.md and epics |
| Break feature into tasks | tech-lead-coordinator | Creates task breakdowns |
| Implement code | minecraft-developer | ONLY agent that writes code |
| Research optimization | minecraft-researcher | Downloads docs, analyzes mods |
| Define tests | minecraft-qa-specialist | Creates tests after user validation |

### Agent Context Requirements

When invoking an agent, always provide:
1. **Project name**: Which project is being worked on
2. **Shared standards**: Path to `.claude/standards/minecraft-modding-standards.md`
3. **Project context**: Path to `{{project}}/.claude/project.md`
4. **Research**: Path to `{{project}}/.claude/research/`
5. **Task details**: Clear goals and acceptance criteria

## Troubleshooting

### "No current project set" Error
**Solution**: Run `/set_project <project_name>` first

### Agent can't find files
**Cause**: Project context not set or wrong paths
**Solution**:
1. Verify `.claude/current_project.txt` has correct project
2. Ensure using `{{project}}/` path placeholders
3. Check file exists in correct project folder

### Conflicting standards between projects
**Cause**: Project-specific standards file created
**Solution**: Remove project-specific standards, use shared `.claude/standards/minecraft-modding-standards.md`

### Lost task history
**Cause**: Looking for changelog.md or bugs.md
**Solution**: All history is in `{{project}}/.claude/tasks.md` - check task status changes

## Key Principles

1. **Shared Infrastructure** - Agents, commands, and standards defined once, used everywhere
2. **Project Isolation** - Each mod is independent with its own code and configuration
3. **Context Awareness** - All commands respect the active project setting
4. **Single Source of Truth** - Tasks.md is the only tracking mechanism
5. **Standards Compliance** - All code follows shared minecraft-modding-standards.md
6. **Agent Specialization** - Each agent has clear, non-overlapping responsibilities
7. **User Validation** - Manual testing before automated test creation

---

**Remember**: This is a multi-project workspace. ALWAYS set the project context with `/set_project` before executing any other commands. When in doubt, check `.claude/current_project.txt` to see which project is active.
