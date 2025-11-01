# Hook Patterns - Detailed Examples

Complete implementations of common hook patterns with production-ready code.

---

## Pattern 1: Complete Activity Tracking System

Track ALL workflow activity: user commands, subprocess agents, completions with resource usage.

### Architecture

**Three-Hook System**:
1. **UserPromptSubmit** - Catches user-typed slash commands
2. **PreToolUse (Task + SlashCommand)** - Catches subprocess creation + Claude-invoked commands
3. **SubagentStop** - Tracks subprocess completions with resources used

### Implementation

**settings.json**:
```json
{
  "hooks": {
    "UserPromptSubmit": [{
      "hooks": [{
        "type": "command",
        "command": "node .claude/hooks/log-agent-activity.js"
      }]
    }],
    "PreToolUse": [
      {
        "matcher": "Task",
        "hooks": [{
          "type": "command",
          "command": "node .claude/hooks/log-agent-activity.js"
        }]
      },
      {
        "matcher": "SlashCommand",
        "hooks": [{
          "type": "command",
          "command": "node .claude/hooks/log-agent-activity.js"
        }]
      }
    ],
    "SubagentStop": [{
      "hooks": [{
        "type": "command",
        "command": "node .claude/hooks/log-agent-activity.js"
      }]
    }]
  }
}
```

**log-agent-activity.js** (complete implementation):
```javascript
#!/usr/bin/env node

/**
 * Complete Activity Tracking System
 *
 * Logs:
 * - User-typed slash commands (UserPromptSubmit)
 * - Claude-invoked commands (PreToolUse SlashCommand)
 * - Subprocess agent starts (PreToolUse Task)
 * - Subprocess completions with resources (SubagentStop)
 */

const fs = require('fs');
const path = require('path');

// Read hook input from stdin
let inputData = '';
process.stdin.on('data', (chunk) => {
  inputData += chunk;
});

process.stdin.on('end', () => {
  try {
    const hookInput = JSON.parse(inputData);
    processHook(hookInput);
  } catch (error) {
    console.error('[Hook Error]', error.message);
    process.exit(0); // Don't disrupt workflow
  }
});

/**
 * Route to appropriate handler based on hook event
 */
function processHook(hookInput) {
  const { hook_event_name } = hookInput;

  if (hook_event_name === 'UserPromptSubmit') {
    handleUserPrompt(hookInput);
  } else if (hook_event_name === 'PreToolUse') {
    handleAgentStart(hookInput);
  } else if (hook_event_name === 'SubagentStop') {
    handleAgentStop(hookInput);
  }
}

/**
 * Handle user prompt submission
 * Detects slash commands from user input
 */
function handleUserPrompt(hookInput) {
  const { prompt, cwd, session_id } = hookInput;

  if (prompt && prompt.trim().startsWith('/')) {
    const { epic, task } = extractContext(cwd);
    const commandMatch = prompt.match(/^\/([a-z_]+)(?:\s+(.*))?$/i);

    if (commandMatch) {
      const commandName = commandMatch[1];
      const commandArgs = commandMatch[2] || '';
      const commandAgent = mapCommandToAgent(commandName);

      const logEntry = {
        event: 'command_invoked',
        timestamp: new Date().toISOString(),
        session_id: session_id,
        command: commandName,
        agent: commandAgent,
        type: 'user-typed',
        arguments: commandArgs,
        epic: epic || 'none',
        task: task || 'none'
      };

      appendToLog(cwd, logEntry);
      console.log(`[Activity] USER COMMAND: /${commandName} -> ${commandAgent}`);
    }
  }
}

/**
 * Handle agent/subprocess start
 * Processes both Task tool (subprocess) and SlashCommand tool (Claude-invoked)
 */
function handleAgentStart(hookInput) {
  const { tool_name, tool_input, cwd, session_id } = hookInput;

  if (tool_name === 'Task') {
    // Subprocess agent invocation
    const { epic, task } = extractContext(cwd);

    const logEntry = {
      event: 'agent_start',
      timestamp: new Date().toISOString(),
      session_id: session_id,
      agent: tool_input?.subagent_type || 'unknown',
      type: 'subprocess',
      description: tool_input?.description || 'No description',
      epic: epic || 'none',
      task: task || 'none',
      model: tool_input?.model || 'default'
    };

    appendToLog(cwd, logEntry);
    console.log(`[Activity] AGENT START: ${logEntry.agent} - ${logEntry.description}`);
  } else if (tool_name === 'SlashCommand') {
    // Claude programmatically invoking a command
    const { epic, task } = extractContext(cwd);
    const commandName = extractCommandName(tool_input?.command || '');
    const commandAgent = mapCommandToAgent(commandName);

    const logEntry = {
      event: 'command_invoked',
      timestamp: new Date().toISOString(),
      session_id: session_id,
      command: commandName,
      agent: commandAgent,
      type: 'claude-invoked',
      arguments: extractCommandArguments(tool_input?.command || ''),
      epic: epic || 'none',
      task: task || 'none'
    };

    appendToLog(cwd, logEntry);
    console.log(`[Activity] CLAUDE COMMAND: /${commandName} -> ${commandAgent}`);
  }
}

/**
 * Handle subprocess completion
 * Extracts resources used from transcript
 */
function handleAgentStop(hookInput) {
  const { transcript_path, cwd, session_id } = hookInput;

  const transcriptPath = expandPath(transcript_path);
  if (!fs.existsSync(transcriptPath)) {
    console.error('[Activity] Transcript not found:', transcriptPath);
    return;
  }

  const transcript = readTranscript(transcriptPath);
  const agentName = extractAgentName(transcript);
  const skills = extractFileReferences(transcript, 'skills');
  const knowledge = extractFileReferences(transcript, 'knowledge');
  const { epic, task } = extractContext(cwd);
  const toolsUsed = extractToolsUsed(transcript);
  const messageCount = countMessages(transcript);
  const duration = estimateDuration(transcript);

  const logEntry = {
    event: 'agent_stop',
    timestamp: new Date().toISOString(),
    session_id: session_id,
    agent: agentName || 'unknown',
    epic: epic || 'none',
    task: task || 'none',
    skills_used: skills,
    knowledge_used: knowledge,
    tools_used: toolsUsed,
    message_count: messageCount,
    duration_ms: duration
  };

  appendToLog(cwd, logEntry);
  console.log(`[Activity] AGENT STOP: ${logEntry.agent} - ${messageCount} messages, ${toolsUsed.length} tools`);
}

/**
 * Read JSONL transcript file
 */
function readTranscript(transcriptPath) {
  const content = fs.readFileSync(transcriptPath, 'utf8');
  const lines = content.trim().split('\n').filter(line => line.trim());
  return lines.map(line => {
    try {
      return JSON.parse(line);
    } catch (e) {
      return null;
    }
  }).filter(Boolean);
}

/**
 * Extract agent name from transcript
 */
function extractAgentName(transcript) {
  // Look for Task tool with subagent_type
  for (let i = transcript.length - 1; i >= 0; i--) {
    const entry = transcript[i];
    if (entry.content && Array.isArray(entry.content)) {
      for (const block of entry.content) {
        if (block.type === 'tool_use' && block.name === 'Task' && block.input?.subagent_type) {
          return block.input.subagent_type;
        }
      }
    }
  }

  return 'main-assistant';
}

/**
 * Extract file references from transcript
 *
 * NOTE: This is a simplified example showing transcript-based extraction.
 * The actual implementation uses a multi-strategy approach:
 *
 * Skills:
 *   1. Read agent definition file for frontmatter skills array
 *   2. Parse "Skills You Use" section in agent file
 *   3. Find skill mentions in agent content
 *   4. Fallback to transcript Read tool calls
 *   → Returns skill NAMES only (e.g., "fabric-modding")
 *
 * Knowledge:
 *   - Parse transcript for Read tool calls to .claude/knowledge/ files
 *   → Returns file PATHS (e.g., "minecraft/entity-rendering.md")
 *
 * See .claude/hooks/log-agent-activity.js for complete implementation.
 */
function extractFileReferences(transcript, type) {
  const references = new Set();

  // For knowledge: Check Read tool calls in transcript
  for (const entry of transcript) {
    if (entry.content && Array.isArray(entry.content)) {
      for (const block of entry.content) {
        if (block.type === 'tool_use' && block.name === 'Read' && block.input?.file_path) {
          const filePath = block.input.file_path;

          if (type === 'knowledge') {
            const match = filePath.match(/\.claude[/\\]knowledge[/\\](.+\.md)/);
            if (match) {
              references.add(match[1].replace(/\\/g, '/'));
            }
          }
        }
      }
    }
  }

  return Array.from(references).sort();
}

/**
 * Extract tools used from transcript
 */
function extractToolsUsed(transcript) {
  const tools = new Set();
  for (const entry of transcript) {
    if (entry.content && Array.isArray(entry.content)) {
      for (const block of entry.content) {
        if (block.type === 'tool_use' && block.name) {
          tools.add(block.name);
        }
      }
    }
  }
  return Array.from(tools).sort();
}

/**
 * Count assistant messages in transcript
 */
function countMessages(transcript) {
  return transcript.filter(entry => entry.role === 'assistant').length;
}

/**
 * Estimate duration from transcript timestamps
 */
function estimateDuration(transcript) {
  if (transcript.length < 2) return 0;

  const timestamps = transcript
    .filter(entry => entry.timestamp)
    .map(entry => new Date(entry.timestamp).getTime())
    .filter(t => !isNaN(t));

  if (timestamps.length < 2) return 0;
  return Math.max(...timestamps) - Math.min(...timestamps);
}

/**
 * Extract epic and task context from CURRENT_EPIC.md
 */
function extractContext(cwd) {
  const context = { epic: null, task: null };

  try {
    const possiblePaths = [
      path.join(cwd, '.claude/epics/CURRENT_EPIC.md'),
      path.join(cwd, 'xeena-village-manager/.claude/epics/CURRENT_EPIC.md'),
      path.join(cwd, 'xeenaa-alexs-mobs/.claude/epics/CURRENT_EPIC.md')
    ];

    for (const epicPath of possiblePaths) {
      if (fs.existsSync(epicPath)) {
        const content = fs.readFileSync(epicPath, 'utf8');

        const epicMatch = content.match(/epic:\s*([0-9]{3}-[a-z-]+)/i);
        if (epicMatch) context.epic = epicMatch[1];

        const taskMatch = content.match(/current.*task:\s*(TASK-[0-9]+(?:\.[0-9]+)?)/i);
        if (taskMatch) context.task = taskMatch[1];

        break;
      }
    }
  } catch (error) {
    // Silently fail - context optional
  }

  return context;
}

/**
 * Get text content from message content
 */
function getTextContent(content) {
  if (typeof content === 'string') return content;
  if (Array.isArray(content)) {
    return content
      .filter(b => b.type === 'text')
      .map(b => b.text)
      .join(' ');
  }
  return '';
}

/**
 * Append log entry to activity log file
 */
function appendToLog(cwd, logEntry) {
  try {
    const logPath = path.join(cwd, '.claude/temp/agent-activity.jsonl');
    const logDir = path.dirname(logPath);

    if (!fs.existsSync(logDir)) {
      fs.mkdirSync(logDir, { recursive: true });
    }

    fs.appendFileSync(logPath, JSON.stringify(logEntry) + '\n', 'utf8');
  } catch (error) {
    console.error('[Activity Hook Error]', error.message);
  }
}

/**
 * Expand ~ in file paths
 */
function expandPath(filePath) {
  if (filePath.startsWith('~')) {
    return path.join(process.env.HOME || process.env.USERPROFILE, filePath.slice(1));
  }
  return filePath;
}

/**
 * Extract command name from slash command string
 */
function extractCommandName(commandString) {
  const match = commandString.match(/^\/([a-z_]+)/i);
  return match ? match[1] : 'unknown';
}

/**
 * Extract command arguments from slash command string
 */
function extractCommandArguments(commandString) {
  const match = commandString.match(/^\/[a-z_]+\s+(.+)/i);
  return match ? match[1] : '';
}

/**
 * Map command name to responsible agent
 */
function mapCommandToAgent(commandName) {
  const mapping = {
    'ai': 'ai-agent',
    'create_epic': 'epic-agent',
    'create_plan': 'planning-agent',
    'fix': 'planning-agent',
    'next': 'planning-agent',
    'research': 'research-agent',
    'create_project': 'project-agent',
    'show_usage': 'usage-tracking-agent',
    'serve_client': 'implementation-agent'
  };

  return mapping[commandName] || 'unknown-agent';
}
```

---

## Pattern 2: File Protection System

Prevent accidental edits to sensitive files with clear error messages.

### Implementation

**settings.json**:
```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Edit",
        "hooks": [{
          "type": "command",
          "command": "python .claude/hooks/protect-files.py"
        }]
      },
      {
        "matcher": "Write",
        "hooks": [{
          "type": "command",
          "command": "python .claude/hooks/protect-files.py"
        }]
      }
    ]
  }
}
```

**protect-files.py**:
```python
#!/usr/bin/env python3

import sys
import json
import os

def main():
    try:
        # Read hook input
        hook_input = json.load(sys.stdin)
        file_path = hook_input.get('tool_input', {}).get('file_path', '')

        # Protected file patterns
        protected_patterns = [
            '.env',
            '.env.local',
            '.env.production',
            'package-lock.json',
            'yarn.lock',
            '.git/',
            'node_modules/',
            'credentials.json',
            'secrets.yaml'
        ]

        # Check if file matches protected pattern
        for pattern in protected_patterns:
            if pattern in file_path:
                print(f"ERROR: Cannot modify protected file: {file_path}", file=sys.stderr)
                print(f"Pattern matched: {pattern}", file=sys.stderr)
                print("If you need to modify this file, disable the file protection hook.", file=sys.stderr)
                sys.exit(1)  # Block operation

        # Allow operation
        sys.exit(0)

    except Exception as e:
        print(f"Hook error: {e}", file=sys.stderr)
        sys.exit(0)  # Don't block on error

if __name__ == '__main__':
    main()
```

---

## Pattern 3: Auto-Formatting System

Automatically format files after edits using appropriate formatters.

### Implementation

**settings.json**:
```json
{
  "hooks": {
    "PostToolUse": [{
      "matcher": "Edit",
      "hooks": [{
        "type": "command",
        "command": "bash .claude/hooks/auto-format.sh"
      }]
    }]
  }
}
```

**auto-format.sh**:
```bash
#!/bin/bash

# Read hook input
INPUT=$(cat)

# Extract file path and check if formatting succeeded
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')

if [ -z "$FILE_PATH" ] || [ ! -f "$FILE_PATH" ]; then
  exit 0
fi

# Format based on file extension
case "$FILE_PATH" in
  *.js|*.jsx|*.ts|*.tsx)
    if command -v prettier >/dev/null 2>&1; then
      prettier --write "$FILE_PATH" 2>/dev/null
      echo "[Auto-Format] Formatted JavaScript/TypeScript file: $FILE_PATH"
    fi
    ;;
  *.py)
    if command -v black >/dev/null 2>&1; then
      black "$FILE_PATH" 2>/dev/null
      echo "[Auto-Format] Formatted Python file: $FILE_PATH"
    fi
    ;;
  *.java)
    if command -v google-java-format >/dev/null 2>&1; then
      google-java-format -i "$FILE_PATH" 2>/dev/null
      echo "[Auto-Format] Formatted Java file: $FILE_PATH"
    fi
    ;;
  *.go)
    if command -v gofmt >/dev/null 2>&1; then
      gofmt -w "$FILE_PATH" 2>/dev/null
      echo "[Auto-Format] Formatted Go file: $FILE_PATH"
    fi
    ;;
esac

exit 0
```

---

## Pattern 4: Comprehensive Command Logging

Log all Bash commands with timestamp, description, and output status.

### Implementation

**settings.json**:
```json
{
  "hooks": {
    "PreToolUse": [{
      "matcher": "Bash",
      "hooks": [{
        "type": "command",
        "command": "node .claude/hooks/log-bash-commands.js"
      }]
    }],
    "PostToolUse": [{
      "matcher": "Bash",
      "hooks": [{
        "type": "command",
        "command": "node .claude/hooks/log-bash-results.js"
      }]
    }]
  }
}
```

**log-bash-commands.js** (PreToolUse):
```javascript
#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

let inputData = '';
process.stdin.on('data', (chunk) => { inputData += chunk; });

process.stdin.on('end', () => {
  try {
    const hook = JSON.parse(inputData);
    const { command, description } = hook.tool_input;
    const timestamp = new Date().toISOString();

    const logEntry = {
      timestamp,
      session_id: hook.session_id,
      command,
      description: description || 'No description',
      status: 'started'
    };

    const logPath = path.join(hook.cwd, '.claude/temp/bash-commands.jsonl');
    fs.appendFileSync(logPath, JSON.stringify(logEntry) + '\n');

    console.log(`[Bash Log] ${description || command}`);
  } catch (error) {
    console.error('[Bash Hook Error]', error.message);
  }

  process.exit(0);
});
```

**log-bash-results.js** (PostToolUse):
```javascript
#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

let inputData = '';
process.stdin.on('data', (chunk) => { inputData += chunk; });

process.stdin.on('end', () => {
  try {
    const hook = JSON.parse(inputData);
    const { command, description } = hook.tool_input;
    const { stdout, stderr, exit_code } = hook.tool_output || {};
    const timestamp = new Date().toISOString();

    const logEntry = {
      timestamp,
      session_id: hook.session_id,
      command,
      description: description || 'No description',
      status: exit_code === 0 ? 'success' : 'failed',
      exit_code,
      output_length: (stdout || '').length + (stderr || '').length
    };

    const logPath = path.join(hook.cwd, '.claude/temp/bash-commands.jsonl');
    fs.appendFileSync(logPath, JSON.stringify(logEntry) + '\n');

  } catch (error) {
    console.error('[Bash Hook Error]', error.message);
  }

  process.exit(0);
});
```

---

## Pattern 5: Session Management

Initialize and cleanup session state using SessionStart and SessionEnd hooks.

### Implementation

**settings.json**:
```json
{
  "hooks": {
    "SessionStart": [{
      "hooks": [{
        "type": "command",
        "command": "bash .claude/hooks/session-start.sh"
      }]
    }],
    "SessionEnd": [{
      "hooks": [{
        "type": "command",
        "command": "bash .claude/hooks/session-end.sh"
      }]
    }]
  }
}
```

**session-start.sh**:
```bash
#!/bin/bash

INPUT=$(cat)
SESSION_ID=$(echo "$INPUT" | jq -r '.session_id')
CWD=$(echo "$INPUT" | jq -r '.cwd')
TIMESTAMP=$(date -Iseconds)

# Create session log directory
mkdir -p "$CWD/.claude/temp/sessions"

# Initialize session log
SESSION_FILE="$CWD/.claude/temp/sessions/$SESSION_ID.json"
cat > "$SESSION_FILE" <<EOF
{
  "session_id": "$SESSION_ID",
  "start_time": "$TIMESTAMP",
  "cwd": "$CWD"
}
EOF

echo "[Session] Started: $SESSION_ID"

exit 0
```

**session-end.sh**:
```bash
#!/bin/bash

INPUT=$(cat)
SESSION_ID=$(echo "$INPUT" | jq -r '.session_id')
CWD=$(echo "$INPUT" | jq -r '.cwd')
TIMESTAMP=$(date -Iseconds)

SESSION_FILE="$CWD/.claude/temp/sessions/$SESSION_ID.json"

if [ -f "$SESSION_FILE" ]; then
  # Update session file with end time
  jq --arg end "$TIMESTAMP" '. + {end_time: $end}' "$SESSION_FILE" > "$SESSION_FILE.tmp"
  mv "$SESSION_FILE.tmp" "$SESSION_FILE"

  echo "[Session] Ended: $SESSION_ID"
fi

exit 0
```

---

## Pattern 6: Custom Notification System

Integrate external notification services when Claude needs user input.

### Implementation

**settings.json**:
```json
{
  "hooks": {
    "Notification": [{
      "hooks": [{
        "type": "command",
        "command": "python .claude/hooks/notify.py"
      }]
    }]
  }
}
```

**notify.py**:
```python
#!/usr/bin/env python3

import sys
import json
import subprocess
import platform

def main():
    try:
        hook_input = json.load(sys.stdin)
        message = hook_input.get('notification_text', 'Claude Code needs your attention')

        # Send notification based on platform
        if platform.system() == 'Darwin':  # macOS
            subprocess.run([
                'osascript', '-e',
                f'display notification "{message}" with title "Claude Code"'
            ])
        elif platform.system() == 'Linux':
            subprocess.run(['notify-send', 'Claude Code', message])
        elif platform.system() == 'Windows':
            # Use Windows toast notification
            subprocess.run([
                'powershell', '-Command',
                f'[Windows.UI.Notifications.ToastNotificationManager, Windows.UI.Notifications, ContentType = WindowsRuntime] | Out-Null; $template = [Windows.UI.Notifications.ToastNotificationManager]::GetTemplateContent([Windows.UI.Notifications.ToastTemplateType]::ToastText02); $template.GetElementsByTagName("text")[0].AppendChild($template.CreateTextNode("Claude Code")); $template.GetElementsByTagName("text")[1].AppendChild($template.CreateTextNode("{message}")); [Windows.UI.Notifications.ToastNotificationManager]::CreateToastNotifier("Claude Code").Show($template);'
            ])

        print(f"[Notify] Sent notification: {message}")

    except Exception as e:
        print(f"Notification error: {e}", file=sys.stderr)

    sys.exit(0)

if __name__ == '__main__':
    main()
```

---

## Testing Hooks

### Testing Individual Hooks

Create sample hook input and test scripts directly:

```bash
# Test activity tracking hook
echo '{
  "hook_event_name": "UserPromptSubmit",
  "prompt": "/ai test",
  "cwd": "/path/to/project",
  "session_id": "test-session"
}' | node .claude/hooks/log-agent-activity.js

# Test file protection hook
echo '{
  "hook_event_name": "PreToolUse",
  "tool_name": "Edit",
  "tool_input": {
    "file_path": ".env"
  },
  "cwd": "/path/to/project",
  "session_id": "test-session"
}' | python .claude/hooks/protect-files.py

# Should exit with code 1 (blocked)
echo $?
```

### Integration Testing

Test hooks in actual Claude Code workflow:

1. Enable hook in settings.json
2. Perform triggering action (e.g., edit file, run command)
3. Check console output for hook messages
4. Verify log files contain expected entries
5. Test blocking behavior (for PreToolUse hooks)

### Debugging Failed Hooks

If hook isn't working:

1. **Test script standalone**: `echo '{"test":"data"}' | script.js`
2. **Check permissions**: `chmod +x script.sh`
3. **Verify JSON parsing**: Add logging to script
4. **Check exit codes**: Ensure script exits 0 (or 1 for blocking)
5. **Review settings.json**: Verify hook registration syntax

---

## Production Deployment

### Checklist

Before deploying hooks to production:

- [ ] Test hook script standalone with sample JSON
- [ ] Verify hook doesn't block workflow unintentionally
- [ ] Add error handling (try-catch, graceful failures)
- [ ] Test with actual Claude Code workflow
- [ ] Document hook purpose and configuration
- [ ] Add hook to version control (git commit)
- [ ] Share with team (if project-level hook)
- [ ] Monitor initial deployment for issues

### Monitoring

Monitor hook performance:

```bash
# Check hook execution frequency
grep -c "\[Activity\]" .claude/temp/agent-activity.jsonl

# Check for hook errors
grep -i "error" .claude/temp/*.log

# Monitor hook performance (if timing added to hooks)
jq -s 'map(.duration_ms) | add / length' .claude/temp/agent-activity.jsonl
```

### Maintenance

Regular hook maintenance:

1. **Review logs periodically** - Ensure hooks working correctly
2. **Update scripts** - Keep pace with Claude Code updates
3. **Clean old logs** - Prevent unbounded log growth
4. **Optimize performance** - Profile slow hooks
5. **Update documentation** - Keep team informed of changes
