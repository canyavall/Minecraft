# AI Workflow Examples

Detailed flow diagrams and real-world examples of AI workflows in action.

## Complete Feature Implementation Flow

### Day 1: Epic Creation

```
User: /create_epic "Guard Patrol System"

Main Assistant:
  → Uses Task tool
  → Task({
      subagent_type: "epic-agent",
      description: "Create epic: Guard Patrol System",
      prompt: "You are the epic-agent..."
    })

epic-agent subprocess:
  → Reads project context
  → Asks user for requirements details
  → Creates requirements.md
  → Shows summary
  → Asks: "Create implementation plan?"

User: "yes"

epic-agent:
  → Tells user to run /create_plan
  → STOPS

Activity logged:
  - agent_start (epic-agent, session_id: abc123)
  - agent_stop (skills: [epic-requirements], tools: [Read, Write])
```

### Day 2: Planning

```
User: /create_plan

Main Assistant:
  → Uses Task tool
  → Task({
      subagent_type: "planning-agent",
      description: "Create implementation plan",
      prompt: "You are the planning-agent..."
    })

planning-agent subprocess:
  → Reads requirements.md
  → Asks user: "Reviewed and approved requirements?"
  → User: "yes"
  → Creates plan.md with 8 tasks
  → Shows task overview
  → Asks: "Start working on this plan?"

User: "yes"

planning-agent:
  → Sets epic as ACTIVE
  → Tells user to run /next
  → STOPS

Activity logged:
  - agent_start (planning-agent, session_id: def456)
  - agent_stop (skills: [task-planning], tools: [Read, Write, Edit])
```

### Days 3-7: Implementation

```
User: /next

Main Assistant:
  → Uses Task tool
  → Task({
      subagent_type: "planning-agent",
      description: "Execute next task",
      prompt: "You are the planning-agent..."
    })

planning-agent subprocess:
  → Reads plan.md
  → Finds TASK-001: "Create Guard entity class"
  → Assigned to: implementation-agent

  → Uses Task tool (NESTED!)
  → Task({
      subagent_type: "implementation-agent",
      description: "TASK-001: Create Guard entity",
      prompt: "You are the implementation-agent..."
    })

    implementation-agent subprocess (nested):
      → Reads coding-standards skill
      → Reads requirements.md
      → Creates GuardEntity.java
      → Creates tests
      → Shows completed files
      → STOPS

  → Returns to planning-agent

planning-agent:
  → Updates TASK-001 status to COMPLETED
  → Updates CURRENT_EPIC.md progress
  → Reports to user: "TASK-001 complete"
  → STOPS

User: [Tests the guard entity in-game]
User: /next

[Repeat for TASK-002, TASK-003, etc.]

Activity logged:
  - agent_start (planning-agent, session_id: ghi789)
  - agent_start (implementation-agent, session_id: jkl012, nested!)
  - agent_stop (implementation-agent, tools: [Read, Write, Bash])
  - agent_stop (planning-agent, tools: [Read, Edit])
```

## Research → Implementation Flow

### Phase 1: Research

```
User: /research "Fabric entity goal system"

Main Assistant:
  → Task(research-agent, ...)

research-agent subprocess:
  → Searches knowledge base (knowledge-index.json)
  → Not found
  → WebSearch for Fabric documentation
  → Reads Fabric API docs
  → Creates research document
  → Saves to knowledge/fabric/entity-goals.md
  → Updates knowledge-index.json
  → Returns summary to user

Activity logged:
  - agent_start (research-agent)
  - agent_stop (skills: [research-methodology],
                knowledge_used: [],
                tools: [WebSearch, WebFetch, Write])
```

### Phase 2: Apply Research

```
User: /create_epic "Custom Entity Goals"

epic-agent subprocess:
  → References research findings
  → Creates requirements.md citing research
  → STOPS

User: /create_plan

planning-agent subprocess:
  → Reads requirements.md
  → Reads knowledge/fabric/entity-goals.md
  → Creates plan.md using researched patterns
  → STOPS

User: /next

planning-agent → implementation-agent (nested):
  → Reads knowledge/fabric/entity-goals.md
  → Implements using researched approach
  → STOPS

Activity logged for implementation-agent:
  - knowledge_used: ["fabric/entity-goals.md"]
  - Skills: [fabric-modding, coding-standards]
```

## Bug Fix Flow

### User Reports Issue

```
User: /fix "Guards are walking through walls"

Main Assistant:
  → Task(planning-agent, ...)

planning-agent subprocess:
  → Reads plan.md
  → Asks: "Related to existing task or standalone?"

User: "Related to TASK-003 (Guard AI)"

planning-agent:
  → Checks TASK-003 for existing sub-tasks
  → Creates TASK-003.1: Fix - Guards walking through walls
  → Adds to plan.md under TASK-003
  → High priority
  → Assigned to implementation-agent
  → Reports: "Created TASK-003.1. Run /next to fix."
  → STOPS

Activity logged:
  - agent_start (planning-agent)
  - agent_stop (tools: [Read, Edit])
```

### Fix Execution

```
User: /next

planning-agent subprocess:
  → Finds TASK-003.1 (next TODO, high priority)

  → Task(implementation-agent, ...)

    implementation-agent subprocess:
      → Reads TASK-003.1 description
      → Reads GuardAI.java
      → Identifies collision detection issue
      → Fixes pathfinding logic
      → Tests fix
      → Updates code
      → STOPS

planning-agent:
  → Marks TASK-003.1 as COMPLETED
  → STOPS

User: [Tests in-game - guards no longer walk through walls]
User: /next [continues with next task]
```

## Multi-Project Switch

### Morning: Village Manager

```
User: "Set project to xeena-village-manager"

Main Assistant:
  → Reads .claude/current_project.txt
  → Writes: CURRENT_PROJECT=xeena-village-manager
  → Confirms: "Switched to xeena-village-manager"

User: /next

Task(planning-agent, ...):
  → Reads xeena-village-manager/.claude/epics/CURRENT_EPIC.md
  → Reads xeena-village-manager/.claude/epics/003-guards/plan.md
  → Executes next task for village manager project
  → STOPS
```

### Afternoon: Fabric Enhancements

```
User: "Switch to xeenaa-fabric-enhancements"

Main Assistant:
  → Writes: CURRENT_PROJECT=xeenaa-fabric-enhancements
  → Confirms: "Switched to xeenaa-fabric-enhancements"

User: /create_epic "Chunk Loading Optimization"

Task(epic-agent, ...):
  → Creates xeenaa-fabric-enhancements/.claude/epics/002-chunk-loading/
  → Creates requirements.md
  → STOPS

Both projects maintain:
  - Separate epics/
  - Separate research/
  - Separate plan.md files
  - Independent activity tracking
```

## Troubleshooting Flows

### Activity Not Tracked (RESOLVED 2025-11-01)

**Problem Discovery**:
```
User: /ai "create skill"

Main Assistant:
  → SlashCommand expands .claude/commands/ai.md
  → Works IN CONVERSATION (no subprocess)
  → ❌ No Task tool used
  → ❌ PreToolUse (Task) hook doesn't fire
  → ❌ No activity logged
```

**Root Cause**:
- Commands expand to prompts (not subprocess invocations)
- Only Task tool hooks were configured
- Slash commands use Skill tool (not SlashCommand)
- Skill tool invocations were invisible to tracking system

**Solution Implemented**:
```
User: /ai "create skill"

Main Assistant:
  → Uses Skill tool
  → ✅ PreToolUse (Skill) hook fires
  → ✅ Logs command_start event:
      - command: "ai"
      - agent: "ai-agent"
      - type: "in-conversation"
      - arguments: "create skill"
  → Expands .claude/commands/ai.md
  → Executes in conversation
  → Activity logged to .claude/temp/agent-activity.jsonl

User: /show_usage
  → ✅ Shows ai command activity!
```

**If command uses Task tool (optional)**:
```
User: /ai "create skill"
  → Skill tool fires (command_start logged)
  → Command may invoke: Task({subagent_type: "ai-agent", ...})
  → Task tool fires (agent_start logged)
  → ai-agent subprocess runs
  → SubagentStop fires (agent_stop logged)
  → BOTH command AND subprocess tracked!
```

### Skills Not Being Used

**Issue**:
```
User: /create_epic "Feature"

epic-agent subprocess:
  → Searches for "epic-requirements skill"
  → Tries to manually load it
  → ❌ Wrong!
```

**Fix in agent.md**:
```markdown
# WRONG:
Load the epic-requirements skill and follow its patterns.

# CORRECT:
Follow the epic-requirements skill (Claude Code loads automatically).
```

**Result**:
```
epic-agent subprocess:
  → References epic-requirements skill
  → Skill auto-loaded by Claude Code
  → ✅ Follows patterns correctly
  → Activity log shows: skills_used: ["epic-requirements/SKILL.md"]
```

## Activity Tracking Visualization

### View in /show_usage

After running `/create_epic`, `/create_plan`, `/next` sequence:

```
# Agent Activity & Usage Report
Generated: 2025-11-01
Period: Last 24 hours

## Activity Summary
- Total agent invocations: 3
- Unique agents used: 3
- Custom agents: epic-agent, planning-agent, implementation-agent

## Agent Activity Timeline

### 2025-11-01 10:30:00 - epic-agent
- **Task**: Create epic: Guard System
- **Epic**: 007-guard-system
- **Duration**: 180000 ms
- **Tools**: Read, Write, Edit
- **Skills**: epic-requirements, minecraft-modding
- **Knowledge**: None

### 2025-11-01 10:35:00 - planning-agent
- **Task**: Create implementation plan
- **Epic**: 007-guard-system
- **Duration**: 240000 ms
- **Tools**: Read, Write, Edit
- **Skills**: task-planning, fabric-modding
- **Knowledge**: None

### 2025-11-01 10:42:00 - planning-agent
- **Task**: Execute next task
- **Epic**: 007-guard-system
- **Duration**: 420000 ms (includes nested subprocess!)
- **Tools**: Read, Edit, Task
- **Skills**: None (orchestrator role)

### 2025-11-01 10:42:15 - implementation-agent (nested)
- **Task**: TASK-001: Create Guard entity
- **Epic**: 007-guard-system
- **Duration**: 380000 ms
- **Tools**: Read, Write, Bash, Edit
- **Skills**: coding-standards, fabric-modding, java-development
- **Knowledge**: fabric/entities.md

## Insights
- Most active agent: planning-agent (2 invocations)
- Most used skill: fabric-modding (3 times)
- Longest task: TASK-001 implementation (380000 ms)
- Nested subprocess detected: planning-agent → implementation-agent
```

## Changelog

### 2025-11-01: Activity Tracking Architecture

**Problem Discovered**: Commands ran in-conversation but weren't being tracked
- ❌ No activity logs for slash commands
- ❌ Only Task tool (subprocess) invocations were tracked
- ❌ `/show_usage` showed no data despite commands working
- ❌ Incomplete visibility into workflow

**Root Cause Analysis**:
- Slash commands expand into prompts (not subprocess invocations)
- Commands execute in main conversation thread
- Only PreToolUse (Task) hook was configured
- Slash commands use **Skill tool** (discovered via testing)
- Skill tool invocations were invisible

**Solution Implemented**: Hybrid tracking approach
- ✅ Added PreToolUse (Skill) hook to `.claude/settings.json`
- ✅ Updated logging script to handle both tool types (Task + Skill)
- ✅ `command_start` events for slash commands (Skill tool, in-conversation)
- ✅ `agent_start`/`agent_stop` events for subprocesses (Task tool)
- ✅ Command-to-agent mapping for attribution
- ✅ Complete workflow visibility

**Architecture Clarification**:
- Commands are NOT subprocesses (they expand to prompts via Skill tool)
- Commands MAY invoke subprocesses (via Task tool)
- Both levels now tracked independently
- Different event types distinguish execution models

**Key Discovery**: Slash commands use the `Skill` tool (not `SlashCommand`), which is why hooks must match on "Skill"

**Files Modified**:
- `.claude/settings.json` - Added Skill hook matcher
- `.claude/hooks/log-agent-activity.js` - Updated to handle both tools (Task + Skill)
- `.claude/skills/ai/ai-workflow/SKILL.md` - Updated documentation
- `.claude/skills/ai/ai-workflow/examples/workflow-examples.md` - Updated flows

**Documentation Updated**: ai-workflow skill now correctly documents hybrid tracking with Skill tool
