---
name: usage-tracking
description: Agent and skill activity tracking system using hooks and JSONL logs
category: ai
tags: [ai, tracking, logging, usage, analytics, hooks, reporting]
allowed-tools: [Read, Write, Edit, Bash, Grep, Glob]
---

# Usage Tracking System

Complete documentation for the agent and skill activity tracking system implemented via Claude Code hooks.

## Overview

**Purpose**: Automatically track which agents, skills, and knowledge documentation are being used during development work.

**Method**: Hook-based automatic tracking (no manual agent logging required)

**Benefits**:
- Monitor agent activity and workflow patterns
- Track which documentation is actually being consulted
- Identify documentation gaps and underutilized resources
- Analyze agent efficiency and tool usage
- Generate reports for retrospectives

---

## System Architecture

### Components

1. **Hooks** - Automatic event capture (`.claude/hooks/`)
2. **Automatic Log** - Structured JSONL data (`.claude/tracker/agent-activity.jsonl`)
3. **Manual Log** - Human-readable summaries (`.claude/tracker/logs.md`)
4. **Usage Command** - Report generation (`/show_usage`)
5. **Usage Agent** - Report analysis (`usage-tracking-agent`)

### How It Works

```
User runs command → Hook fires → Event logged → User views report
                       ↓
                  agent-activity.jsonl
                       ↓
              /show_usage command
                       ↓
            usage-tracking-agent
                       ↓
              Formatted report
```

---

## Hooks Configuration

### Active Hooks

Located in `.claude/settings.json`:

```json
{
  "hooks": {
    "PreToolUse": {
      "Task": {
        "command": "node .claude/hooks/log-agent-activity.js ${args}"
      },
      "SlashCommand": {
        "command": "node .claude/hooks/log-agent-activity.js ${args}"
      }
    },
    "SubagentStop": {
      "command": "node .claude/hooks/log-agent-activity.js ${args}"
    },
    "UserPromptSubmit": {
      "command": "node .claude/hooks/log-agent-activity.js ${args}"
    }
  }
}
```

### Hook Events

| Hook | When It Fires | What It Tracks |
|------|---------------|----------------|
| `UserPromptSubmit` | User types slash command | Command text, timestamp |
| `PreToolUse (SlashCommand)` | Claude invokes slash command | Command name, arguments |
| `PreToolUse (Task)` | Subprocess agent starts | Agent name, description, context |
| `SubagentStop` | Subprocess agent completes | Skills used, tools used, knowledge used, duration |

### Data Extraction Strategy (SubagentStop)

The hook extracts usage data using multiple strategies to ensure accurate tracking:

**Skills Used** (Multi-Strategy Approach):

Skills are loaded automatically by Claude Code based on agent frontmatter, so the hook reads the agent definition file to determine which skills were available to the agent.

1. **Strategy 1 - Agent Frontmatter**: Read `.claude/agents/[agent-name].md` and extract `skills: [...]` array from YAML frontmatter
2. **Strategy 2 - "Skills You Use" Section**: Parse "## Skills You Use" section and extract skill names from patterns like `- **skill-name**` or `- **`skill-name`**`
3. **Strategy 3 - Skill Mentions**: Find patterns like "Follow the **skill-name** skill" or "Use the **skill-name** skill" in agent content
4. **Strategy 4 - Fallback (Transcript)**: As a final fallback, check transcript for explicit Read tool calls to `.claude/skills/[category]/[skill-name]/SKILL.md` files

**Why Multiple Strategies?**
- Claude Code auto-loads skills based on frontmatter, so skills may not appear in the transcript
- Agents may reference skills in their prompt without explicitly reading the file
- Combining strategies ensures we capture what skills were AVAILABLE to the agent, not just what was READ

**Knowledge Used** (Transcript-Based):

Knowledge files are ALWAYS explicitly read by agents (not auto-loaded), so transcript analysis is reliable:

- Parse transcript for Read tool calls to `.claude/knowledge/[category]/[file].md` files
- Extract normalized paths (e.g., `minecraft/entity-rendering.md`)
- Sort and deduplicate knowledge references

**Tools Used** (Transcript-Based):

Tools are always explicitly invoked, making transcript analysis reliable:

- Parse transcript for `tool_use` blocks
- Extract `name` field from each tool use
- Track all Claude Code tools: Read, Write, Edit, Bash, Grep, Glob, Task, SlashCommand, etc.
- Sort and deduplicate tool names

---

## Dual Logging System

### 1. Automatic Log (Hook-Based)

**Location**: `.claude/tracker/agent-activity.jsonl`

**Format**: JSON Lines (one JSON object per line)

**Logged by**: Hooks (automatic, no agent intervention needed)

### Event Types

#### 1. Agent Start Event

Logged when a subprocess agent begins work:

```json
{
  "event": "agent_start",
  "timestamp": "2025-11-01T10:30:00Z",
  "session_id": "abc123",
  "agent": "implementation-agent",
  "type": "subprocess",
  "description": "Implement guard spawning system",
  "prompt_preview": "You are the implementation-agent...",
  "epic": "001-guard-system",
  "task": "TASK-001",
  "model": "sonnet"
}
```

#### 2. Agent Stop Event

Logged when a subprocess agent completes:

```json
{
  "event": "agent_stop",
  "timestamp": "2025-11-01T10:35:00Z",
  "session_id": "abc123",
  "agent": "implementation-agent",
  "epic": "001-guard-system",
  "task": "TASK-001",
  "skills_used": ["fabric-modding", "defensive-programming"],
  "knowledge_used": ["minecraft/entity-rendering.md"],
  "tools_used": ["Read", "Write", "Edit", "Grep"],
  "message_count": 5,
  "duration_estimate_ms": 300000
}
```

**Field Details**:
- `skills_used`: Array of skill names (e.g., `"fabric-modding"`, not full paths) extracted from agent definition
- `knowledge_used`: Array of knowledge file paths (e.g., `"minecraft/entity-rendering.md"`) from transcript reads
- `tools_used`: Array of tool names (e.g., `"Read"`, `"Write"`) from transcript tool_use blocks
- `message_count`: Number of assistant messages in transcript
- `duration_estimate_ms`: Estimated duration from first to last message timestamp

#### 3. Command Invoked Event

Logged when a slash command is invoked (either user-typed or Claude-invoked):

```json
{
  "event": "command_invoked",
  "timestamp": "2025-11-01T10:30:00Z",
  "session_id": "abc123",
  "command": "next",
  "agent": "planning-agent",
  "type": "user-typed",
  "arguments": "",
  "epic": "001-guard-system",
  "task": "TASK-001"
}
```

**Field Details**:
- `type`: Either `"user-typed"` (from UserPromptSubmit hook) or `"claude-invoked"` (from SlashCommand tool)
- `command`: Command name without the slash (e.g., `"next"`, not `"/next"`)
- `agent`: Agent responsible for executing the command (mapped from command name)
- `arguments`: Arguments passed to the command (everything after command name)

### 2. Manual Logs (Agent-Written)

There are TWO manual logs written by agents:

#### A. Activity Log

**Location**: `.claude/tracker/logs.md`

**Format**: Markdown with structured entries

**Purpose**: Human-readable activity summaries written by agents after completing work

**Format**:
```markdown
- [YYYY-MM-DD] [agent-name] - [task description] - Skills: [list] - Tools: [list]
```

**Example**:
```markdown
- 2025-11-01 ai-agent - Cleaned up tracking system documentation - Skills: usage-tracking - Tools: Read, Write, Edit, Bash
```

**Instructions for Agents**:
1. Add entry at TOP of the list after completing work
2. Keep only last 20 entries
3. Use exact format shown above
4. Be concise but specific

#### B. Skill Usage Log

**Location**: `.claude/tracker/skill.md`

**Format**: One line per skill usage

**Purpose**: Track which skills are actively consulted during work

**Format**:
```
[YYYY-MM-DD HH:MM:SS] [agent-name] used [skill-name]
```

**Example**:
```
[2025-11-01 15:30:00] implementation-agent used fabric-modding
[2025-11-01 15:32:15] research-agent used research-methodology
```

**Instructions for Agents**:
1. Append one line when using a skill (not after, but WHEN)
2. Keep only last 50 entries
3. Use ISO timestamp format (UTC)
4. Agent name and skill name from frontmatter
5. This is automatically instructed in every SKILL.md file

### Why Dual Logging?

**Automatic logs** (hooks):
- Capture ALL agent activity (even when agents forget)
- Structured data for analysis and reporting
- Track tools, message counts, duration
- Require no agent awareness

**Manual logs** (agent-written):
- Activity log: High-level summaries of completed work
- Skill log: Validates that skills are actually consulted
- Human-readable for quick review
- Confirms agents are following skill guidelines

**Use both because**:
- Hooks can't detect if agents READ and FOLLOW skills
- Agents can log INTENT (using a skill) vs PRESENCE (skill in prompt)
- Manual skill logs catch gaps in documentation usage
- Combination provides complete picture of workflow

---

## Usage Command

### `/show_usage` Command

**Purpose**: Generate reports from activity logs

**Invokes**: `usage-tracking-agent`

**Filters** (optional):
- Time period: `last day`, `last week`, `last month`, `all time`
- Specific agent: `agent implementation-agent`
- Specific epic: `epic 001-guard-system`
- Specific task: `task TASK-001`

**Examples**:
```bash
/show_usage                              # Full report (all time)
/show_usage epic 001-guard-system        # Filter by epic
/show_usage agent implementation-agent   # Filter by agent
/show_usage last week                    # Last 7 days
```

---

## Report Format

### Sample Report Structure

```markdown
# Agent Activity & Usage Report
Generated: 2025-11-01T15:30:00Z
Period: All time

## Activity Summary
- Total agent invocations: 15
- Unique agents used: 3
- Custom agents: implementation-agent, planning-agent, research-agent
- Default agents: Explore
- Total tools used: 45

## Agent Activity Timeline
### 2025-11-01T14:30:00Z - implementation-agent
- **Task**: Implement guard spawning (TASK-001)
- **Epic**: 001-guard-system
- **Duration**: 300000 ms (5 min)
- **Tools**: Read, Write, Edit, Grep
- **Messages**: 5
- **Skills**: fabric-modding/SKILL.md, defensive-programming/SKILL.md
- **Knowledge**: minecraft/entity-rendering.md

## Skills Usage
1. `fabric-modding` - 10 references
   - Used by: implementation-agent, research-agent
   - Recent tasks: TASK-001, TASK-005, TASK-012

2. `defensive-programming` - 8 references
   - Used by: implementation-agent
   - Recent tasks: TASK-003, TASK-007, TASK-011

## Knowledge Usage
1. `minecraft/entity-rendering.md` - 6 references
   - Used by: implementation-agent, research-agent
   - Recent tasks: TASK-002, TASK-008

2. `fabric-api/networking-basics.md` - 4 references
   - Used by: implementation-agent
   - Recent tasks: TASK-004, TASK-009

## Tools Usage
1. Read - 25 uses
   - Agents: implementation-agent, research-agent, planning-agent

2. Write - 15 uses
   - Agents: implementation-agent

3. Edit - 12 uses
   - Agents: implementation-agent

## Per-Agent Breakdown
### implementation-agent (10 invocations)
- **Type**: Custom
- **Skills**: fabric-modding (10), defensive-programming (8)
- **Knowledge**: minecraft/entity-rendering.md (6), fabric-api/networking-basics.md (4)
- **Tools**: Read (20), Write (15), Edit (12), Grep (8)
- **Average messages**: 5
- **Average duration**: 300000 ms

## Per-Epic Breakdown
### Epic 001: Guard System
- **Agents invoked**: implementation-agent (5), research-agent (2), planning-agent (3)
- **Skills**: fabric-modding (5), defensive-programming (3)
- **Knowledge**: minecraft/entity-rendering.md (4), minecraft/villagers.md (2)
- **Total invocations**: 10

## Insights
- Most active agent: implementation-agent (10 invocations)
- Most referenced skill: fabric-modding (10 times)
- Most referenced knowledge: minecraft/entity-rendering.md (6 times)
- Most used tool: Read (25 times)
- Underutilized documentation: minecraft/villagers.md (1 reference)
```

---

## Maintenance

### Log Rotation

The log file grows over time. Archive periodically:

```bash
# Archive logs (monthly)
mv .claude/tracker/agent-activity.jsonl .claude/tracker/agent-activity-2025-11.jsonl
touch .claude/tracker/agent-activity.jsonl
```

### Log Cleanup

Start fresh:

```bash
# Clear all usage data
rm .claude/tracker/agent-activity.jsonl
touch .claude/tracker/agent-activity.jsonl
```

### Validation

Ensure log entries are valid JSON:

```bash
# Validate JSONL format
cat .claude/tracker/agent-activity.jsonl | jq empty
```

---

## Verifying Logging Works

### How to Check if Tracking is Active

1. **Check hook configuration** - Verify `.claude/settings.json` has hooks configured
2. **Run an agent** - Execute `/next` or any command that invokes an agent
3. **Check log file** - Verify `.claude/tracker/agent-activity.jsonl` exists and has recent entries
4. **Look for console output** - Hook script logs to console: `[Agent Activity] START/STOP/COMMAND: ...`

### Sample Console Output

When hooks fire correctly, you'll see messages like:

```
[Agent Activity] COMMAND: /next -> planning-agent
[Agent Activity] START (subprocess): implementation-agent - Implement guard spawning
[Agent Activity] STOP: implementation-agent - 2 skills, 1 knowledge, 4 tools
```

### Debugging Missing Data

**Problem**: skills_used array is empty

**Solutions**:
1. Check agent definition file has frontmatter with `skills: [...]`
2. Ensure agent prompt has "## Skills You Use" section
3. Verify skill names match actual skill folders (e.g., `fabric-modding`, not `fabric-modding/SKILL.md`)
4. Review hook script extraction logic in `extractSkillsUsed()` function

**Problem**: knowledge_used array is empty

**Solutions**:
1. Verify agent actually used Read tool to read knowledge files
2. Check knowledge files are in `.claude/knowledge/` directory
3. Ensure file paths match pattern: `.claude/knowledge/[category]/[file].md`
4. Knowledge is NOT auto-loaded, so agents must explicitly read files

**Problem**: tools_used array is empty

**Solutions**:
1. Verify agent actually invoked tools (Read, Write, Edit, etc.)
2. Check transcript file exists and is valid JSONL
3. Review hook script extraction logic in `extractToolsUsed()` function

---

## Best Practices

### For Agents (Ensuring Proper Tracking)

**✅ DO**:
- Define skills in frontmatter: `skills: [fabric-modding, defensive-programming]`
- Reference skills in "## Skills You Use" section with clear formatting: `- **fabric-modding** - Description`
- Explicitly read knowledge files when you need them: Use Read tool on `.claude/knowledge/...`
- Use meaningful agent names matching your agent file (e.g., `implementation-agent`)

**❌ DON'T**:
- Assume skills are tracked without frontmatter or section documentation
- Manually load skills (skills auto-load based on frontmatter)
- Forget to read knowledge files when referencing them
- Use generic agent names that don't help identify your role

### For Hook Maintenance

1. **Keep hooks simple**: Minimal logic, fast execution
2. **Handle errors gracefully**: Don't break workflow if logging fails
3. **Test hook changes**: Verify events still fire correctly
4. **Monitor hook output**: Check for errors in console

### For Report Analysis

1. **Run reports regularly**: Weekly or after major milestones
2. **Look for patterns**: Identify workflow bottlenecks
3. **Find documentation gaps**: Create missing knowledge files
4. **Validate agent behavior**: Ensure agents reference documentation
5. **Archive old logs**: Prevent log file bloat

### For Documentation

1. **Reference files explicitly**: Agents should mention skill/knowledge paths
2. **Use full paths**: Include `.claude/skills/` or `.claude/knowledge/` prefix
3. **Cite sources**: Explain which documentation informed decisions
4. **Be specific**: Reference exact files, not just categories

**Example**: "Following the `fabric-modding/SKILL.md` guidelines, I'll use Fabric API networking..."

---

## Troubleshooting

### No Log File

**Symptom**: `/show_usage` reports "No usage data found"

**Causes**:
- Hooks haven't fired yet
- Hook script failed to execute
- Node.js not installed

**Solutions**:
1. Run some tasks with `/next` to generate data
2. Check `.claude/settings.json` has hook configuration
3. Verify Node.js installed: `node --version`
4. Check hook script permissions
5. Look for hook errors in console output

### Malformed Entries

**Symptom**: Report skips some entries or shows parsing errors

**Causes**:
- Invalid JSON in log file
- Incomplete event data
- Hook script bugs

**Solutions**:
1. Validate log file: `cat .claude/temp/agent-activity.jsonl | jq empty`
2. Find invalid lines: `cat .claude/temp/agent-activity.jsonl | while read line; do echo "$line" | jq empty 2>&1 || echo "Invalid: $line"; done`
3. Check hook script for bugs
4. Remove malformed lines manually

### Missing Skills/Knowledge Paths

**Symptom**: Report shows empty skills/knowledge arrays

**Causes**:
- Agents not consulting documentation
- Hook parsing logic missing references
- File paths not in expected format

**Solutions**:
1. Verify agents reference documentation with full paths
2. Check hook script parsing logic
3. Review agent transcripts to see if files were mentioned

### Hooks Not Firing

**Symptom**: No events logged after running commands

**Causes**:
- Hook configuration incorrect
- Hook script has errors
- Node.js not found
- File permissions issues

**Solutions**:
1. Check `.claude/settings.json` syntax
2. Run hook script manually to test: `node .claude/hooks/log-agent-activity.js`
3. Check Node.js path in system
4. Make hook script executable: `chmod +x .claude/hooks/log-agent-activity.js`

---

## Implementation Files

### Directory Structure

```
.claude/
├── hooks/
│   └── log-agent-activity.js           # Hook script (automatic logging)
├── tracker/
│   ├── agent-activity.jsonl            # Automatic log (hook-based, JSONL)
│   └── logs.md                         # Manual log (agent-written, markdown)
├── commands/
│   └── show_usage.md                   # /show_usage command
├── agents/
│   └── usage-tracking-agent.md         # Report generation agent
├── skills/ai/usage-tracking/
│   └── SKILL.md                        # This file
└── settings.json                        # Hook configuration
```

### Key Files

| File | Purpose | Status |
|------|---------|--------|
| `hooks/log-agent-activity.js` | Hook script (automatic logging) | Active ✅ |
| `tracker/agent-activity.jsonl` | Automatic log (hook-based, structured) | Active ✅ |
| `tracker/logs.md` | Manual log (activity summaries) | Active ✅ |
| `tracker/skill.md` | Manual log (skill usage tracking) | Active ✅ |
| `commands/show_usage.md` | Report command | Active ✅ |
| `agents/usage-tracking-agent.md` | Report analyzer | Active ✅ |
| `skills/ai/usage-tracking/SKILL.md` | This documentation | Active ✅ |

---

## Migration Notes

### From Old System

**What changed** (2025-11-01):
- ✅ Unified hook script (`log-agent-activity.js`) handles all events
- ✅ Single log file (`agent-activity.jsonl`) with multiple event types
- ✅ Complete visibility: commands, subprocesses, completions
- ❌ Removed separate hook scripts per event type
- ❌ Removed manual agent logging instructions

**What stayed the same**:
- `/show_usage` command invocation
- `usage-tracking-agent` for report generation
- JSONL log format (extended with new event types)

---

## Key Principles

1. **Automatic** - Hooks capture events without agent intervention
2. **Universal** - Works for ALL agents (custom + default)
3. **Maintainable** - Single hook script, single log file
4. **Passive** - Doesn't interfere with workflows
5. **Transparent** - Agents don't need to know about logging
6. **Actionable** - Reports provide insights for improvement

---

## Future Enhancements

Potential improvements:
- **Dashboard**: Visual analytics UI
- **Trend analysis**: Usage patterns over time
- **Alerts**: Notify when documentation underutilized
- **Auto-recommendations**: Suggest docs based on task type
- **Coverage reports**: Which docs never used
- **Performance metrics**: Agent execution time trends

---

## References

**Related Skills**:
- **ai-workflow** - How agents, commands, and skills interact
- **claude-code-hooks** - Hook configuration and patterns
- **claude-code-agents** - Agent creation and boundaries

**Documentation**:
- `.claude/hooks/README.md` - Hook system overview
- `.claude/commands/show_usage.md` - Command specification
- `.claude/agents/usage-tracking-agent.md` - Agent responsibilities

---

**Last Updated**: 2025-11-01

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used usage-tracking
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used usage-tracking`

This helps track which skills are actively consulted and identifies documentation gaps.
