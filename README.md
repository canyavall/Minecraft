# Minecraft Multi-Project Workspace

This workspace contains multiple Minecraft Fabric mods for version 1.21.1, organized with a shared agent and command infrastructure.

## Projects

### Active Projects
- **xeena-village-manager** (in progress) - Villager management mod with GUI interface
- **xeenaa-fabric-enhancements** (in progress) - Performance-focused gameplay enhancements

### Planned Projects
- **xeenaa-soulbound** (not started) - Soulbound items functionality

### Template
- **mod_template** - Base template for new Fabric 1.21.1 mods

## Workspace Structure

```
├── .claude/                           # Shared workspace configuration
│   ├── agents/                        # Common agent definitions (shared across all projects)
│   │   ├── minecraft-developer.md
│   │   ├── minecraft-qa-specialist.md
│   │   ├── minecraft-researcher.md
│   │   ├── project-scope-manager.md
│   │   └── tech-lead-coordinator.md
│   ├── commands/                      # Global commands
│   │   ├── set_project.md            # Switch active project context
│   │   ├── next.md                   # Execute next task
│   │   ├── fix.md                    # Fix bugs
│   │   ├── new_feat.md               # Add new features
│   │   └── serve_client.md           # Launch Minecraft client
│   ├── standards/                     # Shared coding standards
│   │   └── minecraft-modding-standards.md
│   ├── templates/                     # Project templates
│   └── current_project.txt            # Currently active project
│
├── <project_name>/                    # Individual project folder
│   ├── .claude/                       # Project-specific configuration
│   │   ├── project.md                # Project description and features
│   │   ├── tasks.md                  # Task tracking (replaces bugs.md and issuestracker)
│   │   ├── research/                 # Project-specific research findings
│   │   ├── temp/                     # Temporary agent-generated files
│   │   └── epics/                    # Epic-based organization
│   │       └── <epic_name>/
│   │           └── tasks.md          # Epic-specific tasks
│   ├── src/                          # Mod source code
│   └── build.gradle                  # Build configuration
│
└── README.md                          # This file
```

## Workflow

### 1. Set Active Project
Before working on any project, set the active context:

```bash
/set_project xeena-village-manager
```

Available projects:
- `xeena-village-manager`
- `xeenaa-fabric-enhancements`
- `xeenaa-soulbound`

### 2. Common Commands

All commands automatically target the currently active project:

- **/next** - Execute the next TODO task from the active project's tasks.md
- **/fix <description>** - Fix a bug in the active project
- **/new_feat <description>** - Add a new feature to the active project
- **/serve_client** - Build and launch Minecraft client for the active project

### 3. Agent-Driven Development

The workspace uses specialized agents:

- **minecraft-developer** - ONLY agent that writes code
- **minecraft-qa-specialist** - Creates automated tests after user validation
- **minecraft-researcher** - Researches mods and optimization techniques
- **project-scope-manager** - Manages features and creates epics
- **tech-lead-coordinator** - Breaks features into tasks and orchestrates agents

### 4. Task Management

Bugs and features are tracked in tasks within:
- `<project>/.claude/tasks.md` - Main project tasks
- `<project>/.claude/epics/<epic_name>/tasks.md` - Epic-specific tasks

**No separate changelog or issuestracker** - all tracking is done through tasks.

## Shared Resources

### Standards
All projects follow the shared coding standards defined in:
`.claude/standards/minecraft-modding-standards.md`

### Agents
Agents are defined once in `.claude/agents/` and used across all projects. They automatically adapt to the active project context.

### Research
Each project maintains its own research in `<project>/.claude/research/` to avoid redundant work and maintain project-specific knowledge.

## Creating a New Project

1. Create project folder from template:
   ```bash
   cp -r mod_template new-project-name
   ```

2. Create project structure:
   ```bash
   mkdir -p new-project-name/.claude/{research,temp,epics}
   ```

3. Create `project.md`:
   ```bash
   # Copy and customize project.md template
   ```

4. Update `.claude/current_project.txt` to include the new project

5. Set as active project:
   ```bash
   /set_project new-project-name
   ```

## Development Environment

### Requirements
- Java 21
- Gradle 8.x
- Minecraft 1.21.1
- Fabric Loader
- Fabric API

### Build Commands
From within a project directory:
```bash
./gradlew build          # Build the mod
./gradlew runClient      # Run Minecraft client
./gradlew runServer      # Run dedicated server
./gradlew genSources     # Generate Minecraft sources
```

## Key Principles

1. **Project Isolation** - Each mod is independent with its own source and configuration
2. **Shared Infrastructure** - Commands, agents, and standards are shared across projects
3. **Task-Based Tracking** - All work is tracked through tasks, not separate changelogs or issue trackers
4. **Agent Orchestration** - Specialized agents handle specific responsibilities
5. **Context Awareness** - Commands automatically work with the active project

## Migration Notes

This workspace was reorganized from project-specific configurations to a shared infrastructure:

- Agents moved from individual projects to `.claude/agents/`
- Commands moved from individual projects to `.claude/commands/`
- Standards consolidated into `.claude/standards/`
- Changelogs removed (replaced by task tracking)
- Issuestracker removed (bugs tracked in tasks)
- Project context managed via `/set_project` command

---

**Current Active Project**: xeena-village-manager

Use `/set_project <project_name>` to change the active project context.
