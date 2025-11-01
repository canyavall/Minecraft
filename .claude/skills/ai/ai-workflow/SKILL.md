---
name: ai-workflow
description: Understanding AI workflows - how agents, commands, skills, and users interact via subprocess invocations
category: ai
tags: [ai, workflow, agents, commands, skills, subprocess, task-tool, automation]
allowed-tools: [Read, Write, Edit, Grep, Glob]
---

# AI Workflow

Understanding how AI flows work: how agents, commands, skills, and users interact through subprocess-based workflows.

## Recent Changes (2025-11-01)

**Activity Tracking Architecture**: Implemented **complete tracking system** with proper hook events

- ✅ User slash commands tracked via **UserPromptSubmit** hook
- ✅ Claude-invoked commands tracked via **SlashCommand** tool hook
- ✅ Subprocess agents tracked via **Task** tool hook
- ✅ Subprocess completions tracked via **SubagentStop** hook
- ✅ Complete visibility into all workflow activity
- ✅ Unified activity log (`.claude/temp/agent-activity.jsonl`)

**Key Discovery**: User-typed slash commands (like `/ai`) are **not tools** - they're text expansion. The `UserPromptSubmit` hook catches them before Claude processes the input.

**How it works**:
- User types `/command` → **UserPromptSubmit** hook fires
- Claude invokes `/command` programmatically → **SlashCommand** tool fires
- Commands may invoke subprocesses → **Task** tool fires
- All levels tracked independently with different event types

---

## Core Architecture

### The Four Pillars

```
USER → COMMANDS → AGENTS (subprocess) → SKILLS
         ↓           ↓                      ↑
     Task tool   Isolated session    Auto-loaded
```

1. **Users** - Initiate workflows, validate work, make decisions
2. **Commands** - Entry points using Task tool to invoke agents
3. **Agents** - Subprocess workers executing specialized tasks
4. **Skills** - Auto-loaded knowledge packages

### Execution Models

**Two execution models exist**:

1. **In-Conversation Execution**:
   - Slash commands expand into prompts
   - Main assistant executes in current conversation
   - Tracked via Skill tool hook
   - Event type: `command_start`

2. **Subprocess Execution**:
   - Task tool creates isolated subprocess
   - Separate agent session with own context
   - Tracked via Task tool hook + SubagentStop
   - Event types: `agent_start`, `agent_stop`

**Commands can optionally invoke subprocesses**:
```javascript
// Command expands, then may use Task tool
Task({
  subagent_type: "agent-name",
  description: "Brief task description",
  prompt: `[Full agent prompt with all context]`
})
```

---

## Invocation Methods

### 1. Explicit Commands

User types slash command: `/next`, `/create_epic "name"`, `/research "topic"`

**Flow**:
```
User: /command
→ Skill tool expands .claude/commands/command.md
→ Main assistant uses Task tool
→ Agent subprocess starts
→ Agent executes and returns
→ Activity logged
```

### 2. Auto-Detection

Claude Code recognizes intent and auto-invokes command.

**Patterns**:
- "work on the next task" → `/next`
- "there's a bug in X" → `/fix "bug in X"`
- "research how X works" → `/research "X"`

**Rules**:
- ✅ Auto-invoke when intent is unambiguous
- ❓ Ask user when ambiguous
- ℹ️ Just answer when informational ("how does X work?")

### 3. Nested Subprocess (Advanced)

Agents can invoke other agents using Task tool.

**Example - `/next` command**:
```
User: /next
→ Task(planning-agent)
  → planning-agent subprocess
    → Task(implementation-agent)  # Nested!
      → implementation-agent subprocess
      → Returns to planning-agent
    → planning-agent updates plan.md
  → Returns to main
```

Both levels tracked independently!

---

## Execution Patterns

### Pattern: Epic Creation (Multi-Stage)

```
/create_epic → epic-agent (subprocess)
  → Creates requirements.md
  → Asks: "Create plan?"
  → User: "yes"
  → User runs: /create_plan

/create_plan → planning-agent (subprocess)
  → Creates plan.md
  → Asks: "Start work?"
  → User: "yes"
  → User runs: /next

/next → planning-agent (subprocess)
  → Invokes implementation-agent (nested subprocess)
  → Task completed
  → Waits for user validation
```

### Pattern: Task Execution (Sequential)

```
1. User: /next
2. Task tool → planning-agent subprocess
3. planning-agent → Task tool → worker agent (nested)
4. Worker agent completes task
5. planning-agent updates plan.md
6. STOP - wait for user validation
7. Repeat: /next for next task
```

### Pattern: Bug Fix (Branching)

```
1. User: /fix "bug description"
2. Task tool → planning-agent subprocess
3. planning-agent asks: "Related or standalone?"
4. Creates task (sub-task or main task)
5. User: /next to execute fix
```

### Pattern: Research (Knowledge Gathering)

```
1. User: /research "topic"
2. Task tool → research-agent subprocess
3. research-agent checks existing research
4. Conducts new research if needed
5. Documents in .claude/knowledge/ or {{project}}/.claude/research/
6. Returns summary to user
```

---

## Validation Checkpoints

**Critical checkpoints** (agents must STOP and wait):
1. After creating requirements.md (before planning)
2. After creating plan.md (before execution)
3. After each task completion (before next task)
4. Before destructive operations

**Checkpoint format**:
```markdown
**STOP and ASK USER**: "Question for user?"

**If YES**: [action]
**If NO**: [alternative action]

**WAIT for user response before continuing.**
```

---

## Activity Tracking

### How It Works

**Hooks** (`.claude/settings.json`):
- `PreToolUse (Skill)` → Logs command_start event (in-conversation)
- `PreToolUse (Task)` → Logs agent_start event (subprocess)
- `SubagentStop` → Logs agent_stop event (subprocess completion)

**Log file**: `.claude/temp/agent-activity.jsonl`

**What's tracked**:
- **All slash commands**: Command name, agent, arguments
- **Subprocess agents**: Agent name, session ID, description
- **Epic/task context**: Current epic and task
- **Tools/Skills/Knowledge**: Used during subprocess execution
- **Duration estimates**: For subprocess completions

**View activity**: `/show_usage`

### Log Events

**command_start** (slash command invocation):
```json
{
  "event": "command_start",
  "timestamp": "2025-11-01T...",
  "session_id": "abc123",
  "command": "ai",
  "agent": "ai-agent",
  "type": "in-conversation",
  "arguments": "fix the skill",
  "epic": "007-feature",
  "task": "TASK-001"
}
```

**agent_start** (subprocess invocation):
```json
{
  "event": "agent_start",
  "timestamp": "2025-11-01T...",
  "session_id": "abc123",
  "agent": "implementation-agent",
  "type": "subprocess",
  "description": "Implement feature X",
  "epic": "007-feature",
  "task": "TASK-001"
}
```

**agent_stop** (subprocess completion):
```json
{
  "event": "agent_stop",
  "session_id": "abc123",
  "agent": "implementation-agent",
  "skills_used": ["fabric-modding/SKILL.md"],
  "knowledge_used": ["minecraft/entities.md"],
  "tools_used": ["Read", "Write", "Edit"],
  "duration_estimate_ms": 300000
}
```

---

## Multi-Project Context

All workflows are **project-aware**:

1. Check `.claude/current_project.txt` before any work
2. Use `{{project}}/` in all file paths
3. Commands automatically target active project

**Switching projects**:
```
User: "Set project to xeena-village-manager"
→ Updates .claude/current_project.txt
→ All subsequent commands target new project
```

---

## Common Issues

### Issue: Activity Not Being Tracked (RESOLVED 2025-11-01)

**Symptom**: `/show_usage` showed no data for slash commands

**Root Cause**: Commands run in-conversation (not as subprocesses), so Task tool hooks didn't fire

**Solution Implemented**: Added Skill tool hook
- Now tracks both command invocations AND subprocess executions
- Hybrid approach: `command_start` events for slash commands (Skill tool), `agent_start`/`agent_stop` for subprocesses (Task tool)
- Complete visibility into all workflow activity

### Issue: Agent Doesn't Follow Skills

**Symptom**: Agent makes mistakes or searches for skills

**Cause**: Agent prompt says "load skill manually" (wrong!)

**Solution**: Change to "Follow [skill] (Claude Code loads automatically)"

### Issue: Wrong Agent Responds

**Symptom**: Different agent than expected

**Cause**: Command doesn't use Task tool with correct subagent_type

**Solution**: Verify command uses `Task({ subagent_type: "correct-agent", ... })`

---

## Best Practices

### For Commands
- ✅ Use Task tool for all agent invocations
- ✅ Provide complete context in prompt
- ✅ Include validation checkpoints
- ✅ Reference skills (don't load manually)

### For Agents
- ✅ Use Task tool to invoke other agents (nested subprocesses)
- ✅ Use TodoWrite to track work
- ✅ STOP at validation checkpoints
- ✅ Reference skills, don't load them

### For Workflows
- ✅ One task at a time (validate before next)
- ✅ Manual testing before automated tests
- ✅ User approval at key milestones
- ✅ Clear communication of progress

---

## Key Principles

1. **Subprocess Model** - All agents run in isolated subprocesses (via Task tool)
2. **Activity Tracking** - All invocations logged automatically
3. **Validation Required** - User approval at checkpoints
4. **Agent Boundaries** - Each agent has ONE responsibility
5. **Skills Auto-Load** - Never manually load skills
6. **Project Context** - Set project BEFORE any work
7. **Nested Subprocesses** - Agents can invoke other agents

---

## References

**Related Skills**:
- **claude-code-setup** - AI infrastructure configuration
- **claude-code-agents** - Agent creation patterns
- **claude-code-commands** - Command workflow patterns
- **multi-project-workflow** - Multi-project patterns

**Examples**: See `examples/workflow-examples.md` for detailed flow diagrams and use cases

**Documentation**: `.claude/CLAUDE.md` - Complete workflow documentation

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used ai-workflow
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used ai-workflow`

This helps track which skills are actively consulted and identifies documentation gaps.
