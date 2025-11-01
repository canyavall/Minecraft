# Multi-Project Minecraft Modding Workspace

This workspace contains multiple Minecraft 1.21.1 Fabric mods sharing common infrastructure (agents, commands, skills) while maintaining independent codebases.

---

## CRITICAL: Intent Recognition

**ALWAYS determine user intent BEFORE taking action:**

### Informational Requests (Answer ONLY - No Actions)

User wants **information/explanation** when they say:
- "How does X work?"
- "What is Y?"
- "Explain Z"
- "Tell me about..."
- "Can you describe..."
- "Why does..."
- "Show me..."
- "What's the difference between..."

**Response**: Provide clear, concise explanation. **DO NOT** use tools, create files, or modify code unless explicitly requested.

### Action Requests (Use Tools - Take Actions)

User wants **implementation/changes** when they say:
- "Implement X"
- "Write Y"
- "Create Z"
- "Fix the bug"
- "Add feature"
- "Update the code"
- "Modify..."
- "Build..."
- "Set up..."

**Response**: Use appropriate tools, create/modify files, take requested actions.

### Ambiguous Requests (ASK for Clarification)

If unclear whether user wants information or action:
- **ASK**: "Would you like me to [action] or just explain how to do it?"
- **DO NOT** assume - always clarify first

**Example**:
- User: "I need a guard system"
- Assistant: "Would you like me to implement a guard system, or would you like me to explain how to approach building one?"

---

## CRITICAL: Automatic Command/Agent Invocation

**You can automatically invoke commands without explicit slash commands** when user intent is clear.

### Workflow Command Triggers

Detect these patterns and **automatically use the SlashCommand tool**:

| User Says | Intent | Auto-Invoke |
|-----------|--------|-------------|
| "execute the next task" | Start next task | `/next` |
| "work on the next task" | Start next task | `/next` |
| "continue with tasks" | Start next task | `/next` |
| "let's move to the next one" | Start next task | `/next` |
| "create/add a new epic [name]" | New epic | `/create_epic [name]` |
| "start new epic called [name]" | New epic | `/create_epic [name]` |
| "create implementation plan" | Generate tasks | `/create_plan` |
| "generate tasks for this epic" | Generate tasks | `/create_plan` |
| "add task: [description]" | Add task | `/add_task [description]` |
| "new task: [description]" | Add task | `/add_task [description]` |
| "there's a bug: [description]" | Bug fix | `/fix [description]` |
| "fix this issue: [description]" | Bug fix | `/fix [description]` |
| "research [topic]" | Research | `/research [topic]` |
| "investigate [topic]" | Research | `/research [topic]` |
| "build and launch" | Test in game | `/serve_client` |
| "run the client" | Test in game | `/serve_client` |
| "create new project [name]" | New project | `/create_project [name]` |

### Agent-Specific Triggers

Detect these patterns and **automatically use the SlashCommand tool** to invoke specialized agents:

| User Says | Intent | Auto-Invoke |
|-----------|--------|-------------|
| "we need to change/fix the agent..." | AI infrastructure | `/ai [description]` |
| "the command isn't working..." | AI infrastructure | `/ai [description]` |
| "skill needs updating..." | AI infrastructure | `/ai [description]` |
| "workflow is broken..." | AI infrastructure | `/ai [description]` |
| "agent boundaries issue..." | AI infrastructure | `/ai [description]` |

### How to Auto-Invoke

**When pattern matches:**
1. Recognize the intent
2. Extract parameters (if any)
3. Use SlashCommand tool with appropriate command
4. Let the command's agent handle execution

**Example Flow:**
```
User: "let's work on the next task"

You (internal):
- Pattern match: "work on the next task" → /next
- Action: Use SlashCommand tool

SlashCommand("/next")
→ planning-agent activates
→ Reads next TODO task
→ Invokes appropriate agent
→ Reports result
```

### Important Rules

**ALWAYS auto-invoke when:**
- ✅ Intent is **unambiguous** (clear action requested)
- ✅ Pattern **strongly matches** command purpose
- ✅ User language indicates they want action NOW

**NEVER auto-invoke when:**
- ❌ User is asking "how to" or "what is" (information only)
- ❌ Pattern is weak or ambiguous
- ❌ User says "should I" or "could you" (asking permission)

**Examples:**

✅ **Auto-invoke**:
- "execute the next task" → `/next`
- "there's a bug in the guard spawning" → `/fix "bug in guard spawning"`
- "research how Fabric networking works" → `/research "Fabric networking"`
- "the ai-agent needs fixing" → `/ai "agent needs fixing"`

❌ **Don't auto-invoke**:
- "how do I run the next task?" → Explain how to use `/next`
- "should we fix this bug?" → Ask if they want you to create fix task
- "what does /research do?" → Explain the research command

### Context-Aware Invocation

Consider workflow context:

**If user says:** "add this to the plan"
- **Context**: Currently reviewing epic requirements
- **Action**: Auto-invoke `/add_task` with inferred description

**If user says:** "this needs research"
- **Context**: Currently discussing unknown system
- **Action**: Auto-invoke `/research` with topic from conversation

### Confirmation for Destructive Actions

For potentially disruptive commands, **ask first**:
- Creating new epics (changes project structure)
- Creating new projects (major operation)

**Example:**
```
User: "create new epic for economy system"

You: "I'll create a new epic called 'Economy System'. This will:
- Create {{project}}/.claude/epics/[##]-economy-system/
- Add requirements.md file
- Update CURRENT_EPIC.md

Proceed? (yes/no)"

[If yes] → Use SlashCommand("/create_epic Economy System")
```

---

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
│   ├── skills/                    # Shared workflow patterns
│   ├── knowledge/                 # Centralized research (NEW!)
│   │   ├── knowledge-index.json   # Tag-based index
│   │   ├── minecraft/             # Core Minecraft systems
│   │   ├── fabric-api/            # Fabric API documentation
│   │   ├── libraries/             # Third-party libraries
│   │   ├── graphics/              # Visual systems
│   │   └── performance/           # Optimization knowledge
│   └── current_project.txt        # Active project tracker
│
├── <project>/                     # PROJECT-SPECIFIC
│   ├── .claude/
│   │   ├── project.md
│   │   ├── research/              # Project-specific research only
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
| **implementation-agent** | **ONLY agent that writes code, tests, and debugs** |
| **research-agent** | Research unknowns, document findings |
| **ai-agent** | Maintain AI infrastructure (agents/skills/commands) |

**Agent files**: `.claude/agents/<agent-name>.md`

## Knowledge Base (Centralized Research)

**NEW**: Cross-project research stored in `.claude/knowledge/` to eliminate duplication.

### Two-Tier Knowledge System

**1. Centralized Knowledge Base** (`.claude/knowledge/`)
- Universal research shared across ALL projects
- Enhanced tag-based discovery via `knowledge-index.json` v2.0
- Project knowledge cache for automatic context loading
- Categories: minecraft/, fabric-api/, libraries/, graphics/, performance/
- Examples: Fabric networking, GeckoLib setup, entity rendering, profiling

**2. Project-Specific Research** (`<project>/.claude/research/`)
- Project-unique context and decisions
- Implementation notes specific to THIS mod
- References centralized knowledge when applicable

### Enhanced Knowledge Infrastructure

**NEW v2.0 Features**:

1. **Enhanced Index Structure** (`knowledge-index.json`)
   - Taxonomy section with formal categories and tag groups
   - Entry categorization (minecraft, fabric, library, graphics, performance)
   - Relevance metadata: projects, topics, difficulty levels
   - Better searchability and discoverability

2. **Project Knowledge Cache** (`project-cache.json`)
   - Pre-computed mappings: project → relevant knowledge
   - Priority levels: critical, high, medium, low
   - Automatic context loading during `/next` command
   - Eliminates redundant knowledge searches

3. **Context-Aware Task Preloading**
   - planning-agent automatically loads project-relevant knowledge
   - Task-specific knowledge matching by tags and keywords
   - Knowledge context passed to worker agents
   - Ensures agents have right information before starting

### Using the Knowledge Base

**Agents (especially research-agent):**
1. ALWAYS search `knowledge-index.json` FIRST by tags (now with enhanced taxonomy)
2. Check `project-cache.json` for project-relevant knowledge
3. Read existing knowledge to avoid duplicate research
4. Add universal findings to knowledge base
5. Update index AND project cache with new knowledge
6. **Follow WRITING_STANDARDS.md** when creating/updating knowledge

**planning-agent (automatic context loading):**
1. Read `project-cache.json` during `/next` command
2. Load all "critical" and "high" priority knowledge for active project
3. Match task keywords to knowledge tags for task-specific additions
4. Pass knowledge context to implementation-agent or research-agent

**Documentation**:
- `.claude/knowledge/WRITING_STANDARDS.md` - How to write knowledge files
- `.claude/knowledge/MIGRATION_GUIDE.md` - How to migrate research
- `.claude/knowledge/README.md` - Knowledge base overview
- `.claude/knowledge/knowledge-index.json` - Enhanced searchable index
- `.claude/knowledge/project-cache.json` - Project→knowledge mappings

## Skills (Shared Workflow Patterns)

Skills provide specialized workflow patterns that activate automatically based on context.

**NOTE**: Skills are for HOW to do things. Knowledge base is for WHAT things are.

### Minecraft Skills
- **minecraft-modding** - Core Minecraft systems (loader-agnostic)
- **fabric-modding** - Fabric API specific patterns
- **flying-entity-control** - Flying entity rotation, animation, and aerial movement with GeckoLib
- **geckolib-animation-patterns** - GeckoLib animation keyframes and state management
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
- **concise-responses** - Provide short answers first, elaborate only when requested
- **research-methodology** - Research process and documentation
- **knowledge-base-management** - Knowledge base writing standards and migration
- **epic-requirements** - requirements.md creation
- **task-planning** - plan.md creation
- **multi-project-workflow** - Multi-project patterns and troubleshooting

### AI Skills
- **ai-workflow** - How AI flows work: agents, commands, skills, and user interaction patterns
- **claude-code-setup** - AI infrastructure and configuration
- **claude-code-agents** - Agent creation and boundaries
- **claude-code-skills** - Skills creation and organization
- **claude-code-commands** - Command workflows and patterns
- **claude-code-hooks** - Hooks configuration, automation, validation, and debugging

**Skill files**: `.claude/skills/<category>/<skill-name>/SKILL.md`

**How skills work**: Claude Code loads them automatically based on frontmatter. Agents reference them (don't manually load).

## Commands (User-Triggered Workflows)

| Command | Purpose | Agent | Natural Language Examples |
|---------|---------|-------|---------------------------|
| `/create_project <name>` | Create new mod from template | project-agent | "create new project MyMod" |
| `/create_epic <name>` | Create business requirements | epic-agent | "start new epic Guard System" |
| `/create_plan` | Generate technical tasks from requirements | planning-agent | "create implementation plan" |
| `/fix <desc>` | Create fix task (sub-task or main task) | planning-agent | "there's a bug in spawning" |
| `/research <question>` | Research and document findings | research-agent | "research Fabric networking" |
| `/next` | Execute next task | planning-agent | "work on the next task" |
| `/serve_client` | Build and launch Minecraft | planning-agent | "build and launch" |
| `/ai <desc>` | Debug/fix AI infrastructure | ai-agent | "the agent needs fixing" |

**Command files**: `.claude/commands/<command-name>.md`

**Invocation Methods**:
1. **Explicit**: Use slash command syntax `/next`
2. **Automatic**: Use natural language "work on the next task" (see Automatic Command Invocation section above)
3. **Natural Language**: Project switching "Set project to <name>"

## Workflow Patterns

All workflow patterns documented in **multi-project-workflow** skill. Common patterns:

### Epic Creation Workflow (Interactive)
1. **Create Epic**: User runs `/create_epic "Epic Name"`
2. **Epic Agent**: Creates requirements.md with business requirements
3. **Ask User**: "Would you like me to generate the implementation plan now?"
   - **If YES**: Automatically runs `/create_plan` → continues to step 4
   - **If NO**: Stops, user reviews requirements.md
4. **Planning Agent**: Creates plan.md with technical tasks
5. **Ask User**: "Would you like to start working on this plan now?"
   - **If YES**: Sets epic as ACTIVE, runs `/next` → starts first task
   - **If NO**: Stops, user can run `/next` manually later

### Task Execution Workflow
1. **Execute Next**: User runs `/next` (or auto-invoked from epic creation)
2. **Planning Agent**: Reads next TODO task from plan.md
3. **Agent Works**: Invokes assigned agent (implementation/research)
4. **STOP**: Agent completes, waits for user validation
5. **User Validates**: User tests/reviews the work
6. **Repeat**: User runs `/next` for next task

### Fix Issue Workflow
1. **Report Issue**: User runs `/fix "description of issue"`
2. **Planning Agent Asks**: "Is this related to an existing task or standalone?"
   - **Related**: Creates sub-task (TASK-XXX.Y)
   - **Standalone**: Creates new main task (TASK-XXX)
3. **Next**: User runs `/next` to execute the fix

### Research Workflow
1. **Research**: User runs `/research "question"`
2. **Research Agent**: Investigates and documents findings
3. **Integration**: Use findings in implementation tasks

### Testing Workflow
1. **Manual Test**: User runs `/serve_client` → tests in Minecraft
2. **Validate**: User confirms features work correctly
3. **Automated Tests**: implementation-agent writes test suite after validation

See **multi-project-workflow** skill for detailed patterns and troubleshooting.

## Task Management

- **Epic requirements**: Business value, user experience (requirements.md)
- **Epic tasks**: Technical implementation (plan.md)
- **Epic status**: Progress tracking (CURRENT_EPIC.md)
- **No separate trackers**: No changelog.md, bugs.md, or issuestracker.md

**Task numbering**:
- Main tasks: `TASK-001`, `TASK-002`, `TASK-015`
- Sub-tasks: `TASK-001.1`, `TASK-001.2`

---

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

1. **Shared Infrastructure** - Agents, commands, skills, knowledge used everywhere
2. **Project Isolation** - Each mod independent
3. **Context Awareness** - Commands respect active project
4. **Single Source of Truth** - plan.md for tasks, knowledge base for research
5. **Skills-Based Patterns** - Workflows auto-activated when relevant
6. **Centralized Knowledge** - Universal research in knowledge base, not duplicated
7. **Agent Specialization** - Clear non-overlapping responsibilities
8. **User Validation** - Manual testing before automated tests

---

**Remember**: ALWAYS set project context FIRST. When in doubt, check `.claude/current_project.txt`.

**For detailed workflows, patterns, and troubleshooting**: See **multi-project-workflow** skill.
