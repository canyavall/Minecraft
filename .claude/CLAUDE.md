# Multi-Project Minecraft Modding Workspace

This workspace contains multiple Minecraft 1.21.1 Fabric mods sharing common infrastructure (agents, commands, skills) while maintaining independent codebases.

## Active Projects

- **xeena-village-manager** - Villager management mod with GUI and guard system
- **xeenaa-fabric-enhancements** - Performance optimizations
- **xeenaa-structure-manager** - Structure generation performance mod
- **xeenaa-soulbound** - Soulbound items (planned)

**Technical Stack**: Minecraft 1.21.1, Fabric, Java 21, Gradle with Loom

## Workspace Structure

```
workspace/
├── .claude/                       # SHARED infrastructure
│   ├── agents/                    # Agent definitions
│   ├── commands/                  # Global commands
│   ├── skills/                    # Shared knowledge
│   └── current_project.txt        # Active project tracker
│
├── <project>/                     # PROJECT-SPECIFIC
│   ├── .claude/
│   │   ├── project.md
│   │   ├── research/
│   │   ├── epics/
│   │   │   ├── ##-name/
│   │   │   │   ├── requirements.md    # Business requirements
│   │   │   │   └── plan.md           # Technical tasks
│   │   │   └── CURRENT_EPIC.md       # Epic tracking
│   │   └── temp/
│   └── src/                       # Source code
```

## CRITICAL: Project Context

**Before ANY work, set the active project**:

User says: "Set project to xeena-village-manager"
→ Validate project exists
→ Update `.claude/current_project.txt` with `CURRENT_PROJECT=<project_name>`
→ Confirm switch

All commands automatically target the active project.

## Agents (Specialized Workers)

| Agent | Responsibility |
|-------|----------------|
| **project-agent** | Create projects from template, maintain project.md |
| **epic-agent** | Create business requirements (requirements.md) |
| **planning-agent** | Create technical tasks (plan.md), orchestrate workflows |
| **implementation-agent** | **ONLY agent that writes code** |
| **research-agent** | Research unknowns, document findings |
| **validation-agent** | Create tests AFTER user manual validation |
| **ai-agent** | Maintain AI infrastructure (agents/skills/commands) |

**Agent files**: `.claude/agents/<agent-name>.md`

## Skills (Shared Knowledge)

Skills provide specialized knowledge that activates automatically based on context.

### Minecraft Skills
- **minecraft-modding** - Core Minecraft systems (loader-agnostic)
- **fabric-modding** - Fabric API specific patterns
- **minecraft-performance-general** - General optimization strategies
- **minecraft-performance-structure** - Structure generation optimization
- **minecraft-performance-research** - Knowledge retention from research
- **structure-classification** - Structure classification strategies
- **ui-ux-design** - GUI design and implementation

### Java Skills
- **java-development** - Java 21 features and best practices
- **coding-standards** - Code standards, naming, architecture
- **defensive-programming** - Testing and regression prevention
- **logging-strategy** - Proactive logging guidelines
- **performance-testing** - Performance measurement and validation

### Process Skills
- **research-methodology** - Research process and documentation
- **epic-requirements** - requirements.md creation
- **task-planning** - plan.md creation
- **multi-project-workflow** - Multi-project patterns and troubleshooting

### AI Skills
- **claude-code-setup** - AI infrastructure and configuration
- **claude-code-agents** - Agent creation and boundaries
- **claude-code-skills** - Skills creation and organization
- **claude-code-commands** - Command workflows and patterns

**Skill files**: `.claude/skills/<category>/<skill-name>/SKILL.md`

**How skills work**: Claude Code loads them automatically based on frontmatter. Agents reference them (don't manually load).

## Commands (User-Triggered Workflows)

| Command | Purpose |
|---------|---------|
| `/create_project <name>` | Create new mod from template |
| `/create_epic <name>` | Create business requirements |
| `/create_plan` | Generate technical tasks from requirements |
| `/add_task <desc>` | Add task to current epic |
| `/fix <desc>` | Create bug fix sub-task |
| `/research <question>` | Research and document findings |
| `/next` | Execute next task |
| `/serve_client` | Build and launch Minecraft |
| `/ai <desc>` | Debug/fix AI infrastructure |

**Command files**: `.claude/commands/<command-name>.md`

**Note**: Project switching done via natural language ("Set project to <name>"), not slash commands.

## Workflow Patterns

All workflow patterns documented in **multi-project-workflow** skill. Common patterns:

1. **Create Epic**: `/create_epic` → validate requirements → `/create_plan` → `/next`
2. **Execute Tasks**: `/next` → agent works → STOP → user validates → `/next`
3. **Fix Bug**: `/fix` → creates sub-task → `/next`
4. **Research**: `/research` → document findings → use in implementation
5. **Test**: `/serve_client` → manual testing → create automated tests

See **multi-project-workflow** skill for detailed patterns and troubleshooting.

## Task Management

- **Epic requirements**: Business value, user experience (requirements.md)
- **Epic tasks**: Technical implementation (plan.md)
- **Epic status**: Progress tracking (CURRENT_EPIC.md)
- **No separate trackers**: No changelog.md, bugs.md, or issuestracker.md

**Task numbering**:
- Main tasks: `TASK-001`, `TASK-002`, `TASK-015`
- Sub-tasks: `TASK-001.1`, `TASK-001.2`

## Code Quality Standards

All projects follow **coding-standards** skill (activates automatically):
- Java 21 modern features
- Fabric API best practices
- Comprehensive Javadoc
- Input validation
- Proper error handling
- Thread safety

## Development Commands

```bash
# Build commands (from project directory)
./gradlew build          # Build mod
./gradlew runClient      # Run Minecraft client
./gradlew test           # Run tests

# Workflow commands (project-aware)
/create_epic <name>      # Start new feature
/create_plan             # Generate tasks
/next                    # Execute next task
/serve_client            # Test in Minecraft
/ai <desc>               # Fix AI infrastructure
```

## Project-Specific Guidelines

### xeena-village-manager
- Focus: Villager management GUI, guard ranking system
- Patterns: Client-server networking, registry singleton

### xeenaa-fabric-enhancements
- Focus: Performance optimizations
- Requirement: ALL features must show measurable improvement
- Testing: Before/after benchmarks MANDATORY

### xeenaa-structure-manager
- Focus: Structure generation performance
- Philosophy: Measure twice, cut once
- Epic 01: Research ALL bottlenecks
- Epic 02+: Implement optimizations

### xeenaa-soulbound
- Status: Not started
- Setup: Use mod_template as base

## Key Principles

1. **Shared Infrastructure** - Agents, commands, skills used everywhere
2. **Project Isolation** - Each mod independent
3. **Context Awareness** - Commands respect active project
4. **Single Source of Truth** - plan.md only tracking
5. **Skills-Based Knowledge** - Auto-activated when relevant
6. **Agent Specialization** - Clear non-overlapping responsibilities
7. **User Validation** - Manual testing before automated tests

---

**Remember**: ALWAYS set project context FIRST. When in doubt, check `.claude/current_project.txt`.

**For detailed workflows, patterns, and troubleshooting**: See **multi-project-workflow** skill.
