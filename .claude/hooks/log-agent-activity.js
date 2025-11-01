#!/usr/bin/env node

/**
 * Agent Activity Logging System
 *
 * Multi-Hook Approach:
 * 1. UserPromptSubmit - Logs ALL user input (including slash commands)
 * 2. PreToolUse (Task) - Logs when subprocess agents START
 * 3. PreToolUse (SlashCommand) - Logs when Claude programmatically invokes commands
 * 4. SubagentStop - Logs when subprocess agents COMPLETE
 *
 * Logs ALL agent and command activity to .claude/tracker/agent-activity.jsonl
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
    console.error('[Agent Activity Hook Error]', error.message);
    process.exit(0); // Exit gracefully to not disrupt workflow
  }
});

/**
 * Main hook processing function
 */
function processHook(hookInput) {
  const { hook_event_name, cwd, session_id } = hookInput;

  if (hook_event_name === 'UserPromptSubmit') {
    handleUserPrompt(hookInput);
  } else if (hook_event_name === 'PreToolUse') {
    handleAgentStart(hookInput);
  } else if (hook_event_name === 'SubagentStop') {
    handleAgentStop(hookInput);
  }
}

/**
 * Handle user prompt submission (UserPromptSubmit hook)
 * Catches ALL user input including slash commands
 */
function handleUserPrompt(hookInput) {
  const { prompt, cwd, session_id } = hookInput;

  // Check if this is a slash command
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

      appendToActivityLog(cwd, logEntry);
      console.log(`[Agent Activity] COMMAND: /${commandName} -> ${commandAgent}`);
    }
  }
  // Note: We could also log non-command prompts if desired
}

/**
 * Handle agent/command start (PreToolUse on Task or SlashCommand tool)
 */
function handleAgentStart(hookInput) {
  const { tool_name, tool_input, cwd, session_id } = hookInput;

  // Handle both Task (subprocess agents) and SlashCommand (Claude-invoked commands)
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
      prompt_preview: truncateText(tool_input?.prompt || '', 200),
      epic: epic || 'none',
      task: task || 'none',
      model: tool_input?.model || 'default'
    };

    appendToActivityLog(cwd, logEntry);
    console.log(`[Agent Activity] START (subprocess): ${logEntry.agent} - ${logEntry.description}`);
  } else if (tool_name === 'SlashCommand') {
    // Claude programmatically invoking a slash command
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

    appendToActivityLog(cwd, logEntry);
    console.log(`[Agent Activity] COMMAND (Claude): /${commandName} -> ${commandAgent}`);
  }
}

/**
 * Handle agent completion (SubagentStop)
 */
function handleAgentStop(hookInput) {
  const { transcript_path, cwd, session_id } = hookInput;

  // Read and parse transcript
  const transcriptPath = expandPath(transcript_path);
  if (!fs.existsSync(transcriptPath)) {
    console.error('[Agent Activity] Transcript not found:', transcriptPath);
    return;
  }

  const transcript = readTranscript(transcriptPath);

  // Extract information
  const agentName = extractAgentName(transcript);
  const skills = extractSkillsUsed(agentName, transcript, cwd);
  const knowledge = extractKnowledgeUsed(transcript);
  const { epic, task } = extractContext(cwd);
  const toolsUsed = extractToolsUsed(transcript);
  const messageCount = countAgentMessages(transcript);
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
    duration_estimate_ms: duration
  };

  appendToActivityLog(cwd, logEntry);
  console.log(`[Agent Activity] STOP: ${logEntry.agent} - ${skills.length} skills, ${knowledge.length} knowledge, ${toolsUsed.length} tools`);
}

/**
 * Read and parse JSONL transcript file
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
  // Look for Task tool calls with subagent_type
  for (let i = transcript.length - 1; i >= 0; i--) {
    const entry = transcript[i];

    if (entry.content && Array.isArray(entry.content)) {
      for (const block of entry.content) {
        if (block.type === 'tool_use' && block.name === 'Task' && block.input) {
          if (block.input.subagent_type) {
            return block.input.subagent_type;
          }
        }
      }
    }
  }

  // Look for agent identification patterns
  for (let i = transcript.length - 1; i >= 0; i--) {
    const entry = transcript[i];
    if (entry.role === 'assistant' && entry.content) {
      const content = getTextContent(entry.content);

      // Match patterns like "I AM AGENT implementation-agent"
      const agentMatch = content.match(/I AM AGENT ([a-z-]+)/i);
      if (agentMatch) {
        return agentMatch[1];
      }

      // Match agent name patterns
      const simpleMatch = content.match(/(implementation|research|planning|epic|project|ai|usage-tracking|general-purpose|Explore|Plan)-agent/i);
      if (simpleMatch) {
        return simpleMatch[0].toLowerCase();
      }
    }
  }

  return 'main-assistant'; // Default for main conversation
}

/**
 * Extract skills used by agent
 *
 * Strategy:
 * 1. Read agent definition file (.claude/agents/[agent-name].md)
 * 2. Extract skills from frontmatter OR from "Skills You Use" section
 * 3. Fallback: Check Read tool calls for explicit skill file reads
 *
 * Skills are loaded automatically by Claude Code based on agent frontmatter,
 * so we need to read the agent config, not just the transcript.
 */
function extractSkillsUsed(agentName, transcript, cwd) {
  const skills = new Set();

  // Try to read agent definition file
  const agentFilePath = path.join(cwd, '.claude', 'agents', `${agentName}.md`);

  if (fs.existsSync(agentFilePath)) {
    try {
      const agentContent = fs.readFileSync(agentFilePath, 'utf8');

      // Strategy 1: Extract from frontmatter (skills: [...])
      const frontmatterMatch = agentContent.match(/^---\s*\n([\s\S]*?)\n---/);
      if (frontmatterMatch) {
        const frontmatter = frontmatterMatch[1];
        const skillsMatch = frontmatter.match(/skills:\s*\[(.*?)\]/s);
        if (skillsMatch) {
          // Parse array of skills
          const skillsList = skillsMatch[1].split(',').map(s => s.trim().replace(/['"]/g, ''));
          skillsList.forEach(skill => {
            if (skill) skills.add(skill);
          });
        }
      }

      // Strategy 2: Extract from "Skills You Use" section
      const skillsSectionMatch = agentContent.match(/##?\s*Skills You Use[\s\S]*?(?=\n##|$)/i);
      if (skillsSectionMatch) {
        const section = skillsSectionMatch[0];
        // Match patterns like: - **`skill-name`** or - **skill-name** - Description
        const skillPatternMatches = section.matchAll(/[-*]\s*\*\*`?([a-z0-9-]+)`?\*\*/g);
        for (const match of skillPatternMatches) {
          skills.add(match[1]);
        }
      }

      // Strategy 3: Look for skill mentions in content (e.g., "Follow the **skill-name** skill")
      const skillMentions = agentContent.matchAll(/(?:follow|use|reference|consult)(?:s)?\s+(?:the\s+)?`?\*\*([a-z0-9-]+)\*\*`?\s+skill/gi);
      for (const match of skillMentions) {
        const skillName = match[1].toLowerCase();
        // Validate it's a real skill name pattern (hyphenated)
        if (skillName.includes('-')) {
          skills.add(skillName);
        }
      }
    } catch (error) {
      // Silently fail - agent file might not exist or be readable
    }
  }

  // Strategy 4: Fallback - Check transcript for explicit Read tool calls to skill files
  for (const entry of transcript) {
    if (entry.content && Array.isArray(entry.content)) {
      for (const block of entry.content) {
        if (block.type === 'tool_use' && block.name === 'Read' && block.input && block.input.file_path) {
          const filePath = block.input.file_path;
          const skillMatch = filePath.match(/\.claude[/\\]skills[/\\]([^/\\]+[/\\][^/\\]+)[/\\]SKILL\.md/);
          if (skillMatch) {
            const skillPath = skillMatch[1].replace(/\\/g, '/');
            const skillName = skillPath.split('/').pop(); // Get skill-name from category/skill-name
            skills.add(skillName);
          }
        }
      }
    }
  }

  return Array.from(skills).sort();
}

/**
 * Extract knowledge files used from transcript
 *
 * Strategy:
 * - Look for Read tool calls to .claude/knowledge/*\/*.md files
 * - Knowledge is always explicitly read by agents (not auto-loaded like skills)
 */
function extractKnowledgeUsed(transcript) {
  const references = new Set();

  for (const entry of transcript) {
    if (entry.content && Array.isArray(entry.content)) {
      for (const block of entry.content) {
        if (block.type === 'tool_use' && block.name === 'Read' && block.input && block.input.file_path) {
          const filePath = block.input.file_path;
          const knowledgeMatch = filePath.match(/\.claude[/\\]knowledge[/\\](.+\.md)/);
          if (knowledgeMatch) {
            const normalizedPath = knowledgeMatch[1].replace(/\\/g, '/');
            references.add(normalizedPath);
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
 * Count agent messages in transcript
 */
function countAgentMessages(transcript) {
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
 * Extract epic and task context
 */
function extractContext(cwd) {
  const context = { epic: null, task: null };

  try {
    const possiblePaths = [
      path.join(cwd, '.claude/epics/CURRENT_EPIC.md'),
      path.join(cwd, 'xeena-village-manager/.claude/epics/CURRENT_EPIC.md'),
      path.join(cwd, 'xeenaa-alexs-mobs/.claude/epics/CURRENT_EPIC.md'),
      path.join(cwd, 'xeenaa-fabric-enhancements/.claude/epics/CURRENT_EPIC.md'),
      path.join(cwd, 'xeenaa-structure-manager/.claude/epics/CURRENT_EPIC.md'),
    ];

    for (const epicPath of possiblePaths) {
      if (fs.existsSync(epicPath)) {
        const content = fs.readFileSync(epicPath, 'utf8');

        const epicMatch = content.match(/epic:\s*([0-9]{3}-[a-z-]+)/i) ||
                         content.match(/Epic\s+([0-9]{3})/i);
        if (epicMatch) {
          context.epic = epicMatch[1];
        }

        const taskMatch = content.match(/current.*task:\s*(TASK-[0-9]+(?:\.[0-9]+)?)/i) ||
                         content.match(/\[ \]\s*(TASK-[0-9]+(?:\.[0-9]+)?)/);
        if (taskMatch) {
          context.task = taskMatch[1];
        }

        break;
      }
    }
  } catch (error) {
    // Silently fail - context is optional
  }

  return context;
}

/**
 * Get text content from message content
 */
function getTextContent(content) {
  if (typeof content === 'string') {
    return content;
  }
  if (Array.isArray(content)) {
    return content
      .filter(b => b.type === 'text')
      .map(b => b.text)
      .join(' ');
  }
  return '';
}

/**
 * Truncate text to specified length
 */
function truncateText(text, maxLength) {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
}

/**
 * Append activity entry to log file
 */
function appendToActivityLog(cwd, logEntry) {
  try {
    const logPath = path.join(cwd, '.claude/tracker/agent-activity.jsonl');

    // Ensure directory exists
    const logDir = path.dirname(logPath);
    if (!fs.existsSync(logDir)) {
      fs.mkdirSync(logDir, { recursive: true });
    }

    // Append as JSON line
    const jsonLine = JSON.stringify(logEntry) + '\n';
    fs.appendFileSync(logPath, jsonLine, 'utf8');
  } catch (error) {
    console.error('[Agent Activity Hook Error] Failed to write log:', error.message);
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
 * Example: "/ai fix the skill" -> "ai"
 */
function extractCommandName(commandString) {
  const match = commandString.match(/^\/([a-z_]+)/i);
  return match ? match[1] : 'unknown';
}

/**
 * Extract command arguments from slash command string
 * Example: "/ai fix the skill" -> "fix the skill"
 */
function extractCommandArguments(commandString) {
  const match = commandString.match(/^\/[a-z_]+\s+(.+)/i);
  return match ? match[1] : '';
}

/**
 * Map command name to agent responsible for execution
 */
function mapCommandToAgent(commandName) {
  const commandToAgent = {
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

  return commandToAgent[commandName] || 'unknown-agent';
}
