---
name: claude-code-hooks
description: Comprehensive guide for configuring, creating, and debugging Claude Code hooks for workflow automation, validation, logging, and custom integrations
category: ai
tags: [ai, claude-code, hooks, automation, validation, logging, events]
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Claude Code Hooks

Complete guide for working with Claude Code hooks - event-driven automation that executes at key workflow lifecycle points.

## Purpose

This skill provides patterns and best practices for:
- Understanding the 9 hook events and when they fire
- Configuring hooks in settings.json
- Creating custom hook scripts
- Debugging hook execution issues
- Implementing common use cases (logging, validation, formatting)
- Securing hook implementations

## When to Use

- Setting up workflow automation (formatting, linting, logging)
- Creating activity tracking systems
- Validating or blocking potentially dangerous operations
- Integrating external tools with Claude Code
- Debugging why hooks aren't firing
- Understanding hook execution order

---

## Hook Events Overview

Claude Code provides **9 hook events** that fire at different lifecycle points:

| Event | Fires When | Can Block? | Primary Use Cases |
|-------|------------|------------|-------------------|
| **UserPromptSubmit** | User submits prompt (BEFORE Claude processes) | No | Log commands, track activity |
| **PreToolUse** | Before tool execution | **Yes** | Validate params, block operations, log tools |
| **PostToolUse** | After tool completes | No | Auto-format, run linters |
| **SubagentStop** | Subprocess completes | No | Track resources, log duration |
| **Stop** | Claude finishes responding | No | Session cleanup, notifications |
| **Notification** | Claude sends notification | No | Custom notifications |
| **SessionStart** | Session starts/resumes | No | Initialize state, setup logging |
| **SessionEnd** | Session terminates | No | Cleanup, save state |
| **PreCompact** | Before context reduction | No | Backup state |

### Key Hook Details

**UserPromptSubmit**:
- Input: `{ prompt, cwd, session_id, hook_event_name }`
- Use: Detect slash commands (prompt starts with `/`)

**PreToolUse** (most powerful):
- Input: `{ tool_name, tool_input, cwd, session_id }`
- Exit 0 = allow, exit 1 = block
- Supports matchers to filter by tool name

**PostToolUse**:
- Input: `{ tool_name, tool_input, tool_output, cwd, session_id }`
- Use: Process results, run post-operations

**SubagentStop**:
- Input: `{ transcript_path, cwd, session_id }`
- Use: Parse transcript JSONL to extract usage data
- Extract skills used (from agent definition file + transcript)
- Extract knowledge used (from transcript Read tool calls)
- Extract tools used (from transcript tool_use blocks)
- See usage-tracking skill for detailed extraction strategies

---

## Hook Configuration

### Settings.json Structure

Hooks are configured in `.claude/settings.json`:

```json
{
  "hooks": {
    "EventName": [
      {
        "matcher": "pattern",
        "hooks": [
          {
            "type": "command",
            "command": "executable script"
          }
        ]
      }
    ]
  }
}
```

### Hook Types

**1. Without Matcher** (applies to all events):
```json
"UserPromptSubmit": [
  {
    "hooks": [
      {
        "type": "command",
        "command": "node .claude/hooks/log-user-input.js"
      }
    ]
  }
]
```

**2. With Matcher** (filters by tool name):
```json
"PreToolUse": [
  {
    "matcher": "Bash",
    "hooks": [
      {
        "type": "command",
        "command": "node .claude/hooks/log-bash-commands.js"
      }
    ]
  },
  {
    "matcher": "Edit",
    "hooks": [
      {
        "type": "command",
        "command": "python .claude/hooks/protect-files.py"
      }
    ]
  }
]
```

**3. Wildcard Matcher** (matches all):
```json
"PreToolUse": [
  {
    "matcher": "*",
    "hooks": [
      {
        "type": "command",
        "command": "node .claude/hooks/log-all-tools.js"
      }
    ]
  }
]
```

### Matcher Patterns

**Available for PreToolUse/PostToolUse only**:
- Exact match: `"matcher": "Bash"` → only Bash tool
- Wildcard: `"matcher": "*"` → all tools
- Tool names: `Read`, `Write`, `Edit`, `Bash`, `Task`, `SlashCommand`, `Grep`, `Glob`

**Other events don't use matchers** (apply to all occurrences).

---

## Creating Hook Scripts

### Input Format

Hooks receive JSON via **stdin**:

```javascript
// Node.js example
let inputData = '';
process.stdin.on('data', (chunk) => {
  inputData += chunk;
});

process.stdin.on('end', () => {
  const hookInput = JSON.parse(inputData);
  // hookInput contains: hook_event_name, cwd, session_id, tool_name, tool_input, etc.
});
```

### Common Input Fields

**All hooks**:
- `hook_event_name` - Event that fired (e.g., "PreToolUse")
- `cwd` - Current working directory
- `session_id` - Unique session identifier

**PreToolUse/PostToolUse**:
- `tool_name` - Name of tool being used
- `tool_input` - Tool parameters (object)
- `tool_output` - Tool result (PostToolUse only)

**SubagentStop**:
- `transcript_path` - Path to agent transcript JSONL file

**UserPromptSubmit**:
- `prompt` - Full user prompt text

### Exit Codes (PreToolUse Only)

PreToolUse hooks can **block tool execution**:

```javascript
// Allow tool to execute
process.exit(0);

// Block tool execution
console.error('Operation blocked: editing .env files not allowed');
process.exit(1);
```

**All other hooks**: Exit code ignored, always exit 0

### Error Handling

Hooks should handle errors gracefully:

```javascript
try {
  // Hook logic
  process.exit(0);
} catch (error) {
  console.error('[Hook Error]', error.message);
  process.exit(0); // Don't disrupt workflow
}
```

---

## Common Patterns

Quick reference for common hook use cases. **See `examples/hook-patterns.md` for complete implementations**.

### Pattern 1: Activity Logging
**Use case**: Track all slash commands, subprocess agents, and completions

**Hooks**: UserPromptSubmit + PreToolUse (Task, SlashCommand) + SubagentStop

**Output**: `.claude/tracker/agent-activity.jsonl` with event types, timestamps, resources used

**Data Extraction**:
- Commands: Detected from UserPromptSubmit (user-typed) and PreToolUse SlashCommand (Claude-invoked)
- Agent starts: Detected from PreToolUse Task with agent name and description
- Agent stops: Extract skills (from agent definition), knowledge (from transcript reads), tools (from transcript)

**See**: usage-tracking skill for complete implementation details and extraction strategies

### Pattern 2: File Protection
**Use case**: Block edits to sensitive files (.env, package-lock.json, .git/)

**Hooks**: PreToolUse (Edit, Write) with exit code 1 to block

**Output**: Console error message explaining why operation blocked

### Pattern 3: Auto-Formatting
**Use case**: Automatically format files after edits (prettier, black, gofmt)

**Hooks**: PostToolUse (Edit) runs appropriate formatter based on file extension

**Output**: Formatted files, console log showing which files were formatted

### Pattern 4: Command Logging
**Use case**: Log all Bash commands with descriptions and results

**Hooks**: PreToolUse (Bash) logs command start, PostToolUse (Bash) logs exit code

**Output**: `.claude/temp/bash-commands.jsonl` with timestamps and statuses

---

## Debugging Hooks

### Common Issues

**Hook not firing**:
- Check settings.json registration (case-sensitive event names)
- Verify matcher pattern matches tool name exactly
- Test script: `echo '{"test":"data"}' | node script.js`
- Check permissions: `chmod +x script.sh`

**Hook blocking workflow** (PreToolUse):
- Check exit codes (0 = allow, non-zero = block)
- Add error logging to identify blocking conditions
- Ensure intentional blocking only

**Hook script errors**:
- Test with sample JSON input first
- Wrap logic in try-catch
- Always exit 0 unless blocking intentionally
- Log errors to stderr

**Multiple hooks conflict**:
- Order hooks intentionally (array order matters)
- Use separate log files per hook
- Coordinate file access if needed

---

## Best Practices

### For Hook Configuration

**✅ DO**:
- Use descriptive script names (log-activity.js, protect-files.py)
- Place scripts in `.claude/hooks/` directory
- Use specific matchers when possible (not always `*`)
- Test hooks in development before deploying
- Document hook purpose in comments

**❌ DON'T**:
- Use wildcards unnecessarily (performance impact)
- Block operations without clear error messages
- Forget error handling in scripts
- Hardcode paths (use `cwd` from hook input)

### For Hook Scripts

**✅ DO**:
- Read all stdin before processing
- Parse JSON carefully with error handling
- Exit gracefully (0) unless intentionally blocking
- Log errors to stderr
- Use absolute paths or paths relative to `cwd`

**❌ DON'T**:
- Assume stdin is immediately available
- Exit non-zero accidentally (blocks PreToolUse)
- Write to stdout unnecessarily (pollutes console)
- Use long-running operations (blocks workflow)

### For Security

**✅ DO**:
- Review hook scripts before enabling (they execute arbitrary code)
- Use PreToolUse to validate/block dangerous operations
- Sanitize file paths in hook scripts
- Use user-level settings (~/.claude/) for personal hooks
- Use project-level settings (.claude/) for team hooks

**❌ DON'T**:
- Execute untrusted hook scripts
- Allow hooks to modify critical infrastructure
- Skip validation in PreToolUse hooks
- Expose sensitive data in logs

---

## Hook Locations

### User-Level Hooks
**Location**: `~/.claude/settings.json`

**Purpose**: Hooks that apply to ALL projects

**Examples**:
- Personal preferences
- Universal file protection
- Global command logging
- Cross-project activity tracking

### Project-Level Hooks
**Location**: `<project>/.claude/settings.json`

**Purpose**: Hooks specific to THIS project

**Examples**:
- Project-specific linting
- Team formatting standards
- Project activity tracking
- Integration with project tools

**Note**: Project hooks are git-committable and shared with team.

---

## Integration with Workflow

### How Hooks Fit Into AI Workflow

```
User types /command
  ↓
UserPromptSubmit hook fires → Log command
  ↓
Command expands to prompt
  ↓
Agent uses Tool (e.g., Task)
  ↓
PreToolUse hook fires → Validate operation
  ↓
Tool executes
  ↓
PostToolUse hook fires → Auto-format files
  ↓
(if subprocess) SubagentStop hook fires → Log completion
  ↓
Stop hook fires → Session cleanup
```

### Hook + Agent Collaboration

**Hooks handle**:
- Validation before operations
- Logging and tracking
- Automation after operations
- Integration with external tools

**Agents handle**:
- Understanding user intent
- Choosing correct tools
- Implementing features
- Following skills

**They complement each other**: Hooks provide deterministic control, agents provide intelligent decision-making.

---

## Complete Examples

**See `examples/hook-patterns.md`** for production-ready implementations:
- Multi-hook activity tracking system (complete code)
- File protection with clear error messages
- Auto-formatting for multiple languages
- Command logging with PreToolUse + PostToolUse
- Session management patterns

---

## References

**Related**: claude-code-setup, claude-code-commands, ai-workflow
**Docs**: https://docs.claude.com/en/docs/claude-code/hooks-guide

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used claude-code-hooks
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used claude-code-hooks`

This helps track which skills are actively consulted and identifies documentation gaps.
