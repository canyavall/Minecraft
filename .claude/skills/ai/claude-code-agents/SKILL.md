---
name: claude-code-agents
description: Comprehensive guide for creating, configuring, and maintaining Claude Code agents with clear responsibility boundaries and skill integration.
category: ai
tags: [ai, claude-code, agents, workflow, configuration]
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Claude Code Agents

Comprehensive guide for creating and maintaining Claude Code agents with proper boundaries, skill integration, and workflow patterns.

## Purpose

This skill provides patterns and best practices for:
- Creating new specialized agents
- Defining clear agent boundaries
- Integrating skills into agent workflows
- Ensuring agent collaboration patterns
- Debugging agent invocation issues
- Maintaining agent consistency

## When to Use

- Creating a new agent for specialized tasks
- Fixing agent boundary violations
- Debugging agent invocation problems
- Reviewing agent configuration for consistency
- Understanding agent collaboration patterns

## Agent Architecture Principles

### 1. Single Responsibility Principle

**Each agent has ONE clearly defined primary responsibility**:

```markdown
## CRITICAL: Your ONLY Responsibility

**You ONLY manage**: [Specific, narrow scope]

**What you create**:
- Concrete output 1
- Concrete output 2

**You DO NOT**:
- ❌ Task X (other-agent does this)
- ❌ Task Y (another-agent does this)
- ❌ Task Z (yet-another-agent does this)
```

**Why**: Prevents agent confusion, ensures predictable behavior, makes debugging easier.

### 2. Skills-Based Knowledge

**Agents reference skills, never "load" them**:

```markdown
# ✅ CORRECT
## Skills You Use

Follow patterns from these skills (Claude Code loads automatically):
- `coding-standards` - Naming conventions, code organization
- `minecraft-modding` - Minecraft systems and best practices
- `fabric-modding` - Fabric API specific patterns

# ❌ WRONG
## Skills You Use

Load the `coding-standards` skill and apply its patterns.
```

**Why**: Skills auto-load based on frontmatter. "Load" language creates confusion and extra work.

### 3. Clear Handoff Protocols

**Agents know when to hand off to other agents**:

```markdown
## When to Hand Off

**If user requests [X]**:
"That's outside my scope. Use the `/command-name` command to invoke [other-agent]."

**If you need [Y]**:
"I need [other-agent] to handle [Y]. Please run `/command-name` first."
```

**Why**: Ensures users reach the correct agent, prevents scope creep.

## Agent File Structure

### Complete Template

```markdown
---
name: agent-name
description: One-line description of agent's purpose
model: sonnet
color: blue|green|red|purple|yellow
---

You are the **[Agent Name]** responsible for [primary responsibility].

## CRITICAL: Your ONLY Responsibility

**You ONLY manage**: [Specific scope]

**What you create**:
- Output type 1 (with examples)
- Output type 2 (with examples)

**You DO NOT**:
- ❌ Task outside scope (agent-name does this)
- ❌ Another task outside scope (other-agent-name does this)
- ❌ Yet another task (third-agent-name does this)

## Primary Responsibilities

### 1. [Responsibility Category]

[Detailed description of what this involves]

**Process**:
1. Step one
2. Step two
3. Step three

**Output**: [What gets created]

### 2. [Another Responsibility Category]

[Description]

**Process**:
1. Steps...

**Output**: [What gets created]

## Skills You Use

Follow patterns from these skills (Claude Code loads automatically):
- `skill-name` - What this skill provides
- `another-skill` - What this skill provides
- `third-skill` - What this skill provides

## Workflow Patterns

### Pattern 1: [Common Workflow Name]

**Trigger**: When [user does X] or [agent needs Y]

**Steps**:
1. [Step with validation]
2. [Step with output]
3. **STOP**: Ask user "[Specific question]"
4. [Continue after user approval]

**Output**: [What gets created]

### Pattern 2: [Another Workflow]

[Similar structure]

## Critical Rules

**ALWAYS**:
- ✅ Rule 1 with reasoning
- ✅ Rule 2 with reasoning
- ✅ Rule 3 with reasoning

**NEVER**:
- ❌ Anti-pattern 1 with reasoning
- ❌ Anti-pattern 2 with reasoning
- ❌ Anti-pattern 3 with reasoning

## When to Hand Off

**If user requests [X]**:
"That's outside my scope. Use the `/command-name` command to invoke [agent-name]."

**If you need [Y]**:
"I need [agent-name] to handle [Y]. Please run `/command-name` first."

## Quality Standards

[Agent-specific quality requirements]

## Communication Style

[How agent should communicate with users]

## Examples

### Example 1: [Scenario]

**User Request**: "[Example user input]"

**Agent Response**:
```
[Step-by-step example of how agent handles this]
```

**Output**: [What gets created]

### Example 2: [Another Scenario]

[Similar structure]
```

## Agent Color Coding

Use consistent colors for agent types:

| Color | Agent Type | Examples |
|-------|------------|----------|
| **blue** | Planning/Orchestration | planning-agent, project-agent |
| **green** | Specification/Requirements | epic-agent |
| **purple** | Meta/Infrastructure | ai-agent |
| **yellow** | Research/Investigation | research-agent |
| **(default)** | Implementation & Testing | implementation-agent |

**Why**: Visual distinction in Claude Code UI, easier to identify agent types.

## Common Agent Patterns

### Pattern 1: Specification Agent

**Purpose**: Creates business/user-facing documentation

**Characteristics**:
- Writes requirements, not implementation
- Business language, not technical jargon
- Focuses on "what" and "why", not "how"
- Validates with user before proceeding

**Example**: epic-agent

```markdown
## CRITICAL: Your ONLY Responsibility

**You ONLY create**: Business requirements in `requirements.md`

**You DO NOT**:
- ❌ Create technical tasks (planning-agent)
- ❌ Write code or tests (implementation-agent)
```

### Pattern 2: Planning Agent

**Purpose**: Converts requirements into actionable tasks

**Characteristics**:
- Technical task breakdown
- Estimates and priorities
- Agent assignments
- Dependencies and sequencing

**Example**: planning-agent

```markdown
## CRITICAL: Your ONLY Responsibility

**You ONLY create**: Technical tasks in `tasks.md`

**You DO NOT**:
- ❌ Write business requirements (epic-agent)
- ❌ Implement tasks or write tests (implementation-agent)
```

### Pattern 3: Implementation Agent

**Purpose**: Writes actual code

**Characteristics**:
- ONLY agent that modifies source code
- Uses ALL coding skills
- Creates research files when needed
- Implements features from tasks

**Example**: implementation-agent

```markdown
## CRITICAL: Your ONLY Responsibility

**You ONLY write**: Source code (Java, JSON, etc.)

**You DO NOT**:
- ❌ Create requirements (epic-agent)
- ❌ Create task plans (planning-agent)

**You ALSO write**: Tests after user manual validation confirms features work
```

### Pattern 4: Meta Agent

**Purpose**: Manages the AI infrastructure itself

**Characteristics**:
- Works ON the system, not THROUGH it
- Maintains agents, skills, commands
- Debugs workflow issues

**Example**: ai-agent

```markdown
## CRITICAL: Your ONLY Responsibility

**You ONLY manage**: AI infrastructure (agents, skills, commands)

**You DO NOT**:
- ❌ Write application code (implementation-agent)
- ❌ Create feature specs (epic-agent)
- ❌ Plan tasks (planning-agent)
```

## Agent Collaboration Patterns

### Sequential Handoff

```
User: /create_epic "New Feature"
  ↓
epic-agent: Creates requirements.md
  ↓
epic-agent: "Requirements ready. Run /create_plan to generate tasks."
  ↓
User: /create_plan
  ↓
planning-agent: Reads requirements.md
  ↓
planning-agent: Creates tasks.md
  ↓
planning-agent: "Tasks ready. Run /next to start implementation."
  ↓
User: /next
  ↓
planning-agent: Invokes implementation-agent
  ↓
implementation-agent: Implements first task
```

### Parallel Collaboration

```
planning-agent receives complex task
  ↓
planning-agent analyzes requirements
  ↓
  ├─→ research-agent: Investigates unknowns
  └─→ implementation-agent: Implements known parts and writes tests
  ↓
Results converge
  ↓
planning-agent: Coordinates completion
```

### Emergency Handoff

```
implementation-agent encounters unknown
  ↓
implementation-agent: "I don't know how X works. Use /research to investigate."
  ↓
User: /research "How does X work?"
  ↓
research-agent: Investigates and documents
  ↓
research-agent: "Research complete. Saved to .claude/research/. Run /next to continue."
  ↓
User: /next
  ↓
implementation-agent: Reads research file and continues
```

## Debugging Agent Issues

### Issue: Agent Doing Too Much

**Symptoms**: Agent crosses boundaries into other agents' responsibilities

**Diagnosis**:
1. Read agent's ONLY Responsibility section
2. Check if DO NOT section is comprehensive
3. Look for vague language ("help with", "assist", "support")

**Fix**:
```markdown
# Before (vague)
You help users with their development workflow.

# After (specific)
You ONLY create technical task breakdowns in tasks.md.
You DO NOT write code, create requirements, or run tests.
```

### Issue: Agent Not Using Skills

**Symptoms**: Agent makes mistakes that skills would prevent, or tries to "load" skills

**Diagnosis**:
1. Check if agent prompt uses "load" language
2. Verify skills are listed in "Skills You Use" section
3. Check if skill frontmatter is correct

**Fix**:
```markdown
# Before (wrong)
Load the `coding-standards` skill and follow its patterns.

# After (correct)
Follow the `coding-standards` skill patterns (Claude Code loads automatically).
```

### Issue: Agent Doesn't Know When to Hand Off

**Symptoms**: Agent tries to do things outside its scope, confuses users

**Diagnosis**:
1. Check if "When to Hand Off" section exists
2. Look for clear guidance on what to tell users

**Fix**:
```markdown
## When to Hand Off

**If user requests feature implementation**:
"I only create requirements. Use the `/create_plan` command to generate tasks, then `/next` to implement."

**If user reports a bug**:
"Use the `/fix` command to create a bug fix task."
```

### Issue: Unclear Agent Purpose

**Symptoms**: Users unsure when to use this agent, agent invoked incorrectly

**Diagnosis**:
1. Read agent description in frontmatter
2. Check if ONLY Responsibility is clear
3. Verify examples are provided

**Fix**:
1. Rewrite description to be ultra-specific
2. Add concrete examples of inputs/outputs
3. Add "When to Use This Agent" section

## Agent Creation Checklist

When creating a new agent:

- [ ] **Frontmatter Complete**
  - [ ] Unique `name` (matches filename)
  - [ ] Clear `description` (one line)
  - [ ] Appropriate `color` coding
  - [ ] Model specified (`sonnet`)

- [ ] **Responsibility Boundaries Clear**
  - [ ] ONLY Responsibility section exists
  - [ ] "You ONLY manage" is specific
  - [ ] "What you create" has concrete examples
  - [ ] DO NOT section lists all out-of-scope tasks
  - [ ] Each DO NOT references correct agent

- [ ] **Skills Integration**
  - [ ] All relevant skills listed
  - [ ] No "load" language used
  - [ ] Uses "Claude Code loads automatically" phrasing

- [ ] **Workflow Patterns Defined**
  - [ ] Common workflows documented
  - [ ] Validation checkpoints included
  - [ ] User communication specified

- [ ] **Handoff Protocols**
  - [ ] "When to Hand Off" section exists
  - [ ] Specific commands referenced
  - [ ] Clear user guidance provided

- [ ] **Examples Provided**
  - [ ] At least 2 realistic scenarios
  - [ ] Shows input → process → output
  - [ ] Demonstrates proper behavior

- [ ] **Documentation Updated**
  - [ ] Added to `.claude/CLAUDE.md` agents section
  - [ ] Color coding matches other agents
  - [ ] Collaboration patterns documented

## Best Practices

### ✅ DO

- **Be ultra-specific** about responsibility boundaries
- **List every out-of-scope task** in DO NOT section
- **Reference skills** with "Claude Code loads automatically"
- **Include validation checkpoints** in workflows
- **Provide concrete examples** of inputs and outputs
- **Use consistent structure** across all agents
- **Test mental walkthrough** of common workflows

### ❌ DON'T

- **Use vague language** like "help with" or "assist"
- **Allow scope creep** - keep responsibilities narrow
- **Tell agents to "load" skills** - they auto-load
- **Skip the DO NOT section** - it's critical
- **Forget color coding** - use consistent colors
- **Create agents without clear handoff** protocols
- **Skip documentation updates** in CLAUDE.md

## References

- Related skills: `claude-code-skills`, `claude-code-commands`, `claude-code-setup`
- Documentation: `.claude/CLAUDE.md` (Agent-Driven Development section)
- Examples: See existing agents in `.claude/agents/`
