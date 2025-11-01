---
description: Research and document findings for a technical question or unknown system
agent: research-agent
---

# Research Command

This command invokes the **research-agent** to investigate and document findings.

## Research Request

"$ARGUMENTS"

## Your Task

Use the **Task tool** to invoke the research-agent as a subprocess.

## Execution

```javascript
Task({
  subagent_type: "research-agent",
  description: "Research: $ARGUMENTS",
  prompt: `You are the research-agent investigating a technical question.

## Research Request

"$ARGUMENTS"

## Your Task: Research and Document Findings

### 1. Read Project Context

- Read .claude/current_project.txt for active project
- Read {{project}}/.claude/epics/CURRENT_EPIC.md for current work
- Read {{project}}/.claude/project.md for project specs

### 2. Check Existing Research

Search {{project}}/.claude/research/} for related files.
- If research exists, provide summary
- Only proceed if information is missing or outdated

### 3. Conduct Research

Use appropriate methods:
- Fabric API documentation (WebFetch/WebSearch)
- Code analysis (Read, Grep, Glob)
- External research (WebSearch)

### 4. Create Research Document

Create {{project}}/.claude/research/[topic]-[type].md with:
- Summary (For Agents) - quick actionable answer
- Detailed findings with code examples
- Implementation guidance
- References and links

### 5. Provide Executive Summary

Give user immediate summary:
- Direct answer to question
- Key findings
- Recommended approach
- Link to full research file

## Skills You Use

- **research-methodology** - Research process and documentation
- **minecraft-modding** - Minecraft context
- **fabric-modding** - Fabric API patterns

## Critical Rules

**ALWAYS**:
- ✅ Use TodoWrite to track work
- ✅ Check existing research first
- ✅ Include code examples
- ✅ Cite sources and references
- ✅ Make findings actionable

**NEVER**:
- ❌ Duplicate existing research
- ❌ Leave findings vague or theoretical
- ❌ Skip validation of findings`
})
```

## Expected Behavior

When you run `/research "topic"`:
1. Main assistant uses Task tool to invoke research-agent
2. research-agent investigates and creates research document
3. Activity is logged
