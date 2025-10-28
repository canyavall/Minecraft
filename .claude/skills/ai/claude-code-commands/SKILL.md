---
name: claude-code-commands
description: Comprehensive guide for creating, configuring, and maintaining Claude Code slash commands with clear workflows, agent invocation, and validation patterns.
category: ai
tags: [ai, claude-code, commands, workflows, slash-commands]
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Claude Code Commands

Comprehensive guide for creating and maintaining Claude Code slash commands with proper agent invocation, workflow patterns, and user validation.

## Purpose

This skill provides patterns and best practices for:
- Creating new slash commands
- Defining clear command workflows
- Invoking correct agents from commands
- Including validation checkpoints
- Parsing user arguments
- Debugging command execution issues
- Maintaining command consistency

## When to Use

- Creating a new slash command for a workflow
- Fixing command invocation issues
- Debugging why wrong agent responds
- Improving command user experience
- Understanding command → agent → skill flow

## Command Architecture Principles

### 1. Commands Invoke Agents

**Commands are entry points that invoke specialized agents**:

```markdown
---
description: What this command does
---

You are the [agent-name] responsible for [specific task].

[Rest of command workflow]
```

**Why**: Commands don't have persistent identity. They expand to a prompt that tells Claude which agent to be.

**Key Point**: Command file must be comprehensive - it doesn't automatically merge with agent file.

### 2. Clear Agent Identity

**Always start command with agent identity**:

```markdown
# ✅ CORRECT
You are the **epic-agent** responsible for creating business requirements.

# ❌ WRONG
Create an epic with business requirements.
(Doesn't identify which agent)
```

**Why**: Claude needs to know which agent role to assume and which boundaries to respect.

### 3. Validation Checkpoints

**Commands must include user validation at key points**:

```markdown
### 3. Present Draft to User

Show the draft requirements.md to the user and ask:
"Does this match your vision? Should I proceed with creating the epic folder?"

**STOP and WAIT for user confirmation before continuing.**

### 4. Create Epic (After User Approval)

Only after user confirms, create the epic folder structure.
```

**Why**: Prevents wasted work, ensures user alignment, allows course correction.

## Command File Structure

### Frontmatter Fields

Command files support these frontmatter fields:

```yaml
---
description: One-line description of what the command does (required)
agent: agent-name                                     # Agent to invoke (recommended)
argument-hint: [arg1] [arg2]                         # Argument hints for autocomplete (optional)
allowed-tools: [Read, Write, Bash]                   # Tool restrictions (optional)
model: claude-sonnet-4-5                             # Specific model (optional)
---
```

**Field Details**:

| Field | Purpose | Example | Required |
|-------|---------|---------|----------|
| `description` | Brief command description shown in command list | `"Create a new epic with business requirements"` | ✅ Yes |
| `agent` | Agent name to invoke (matches `.claude/agents/*.md` file) | `epic-agent`, `planning-agent` | ⭐ Recommended |
| `argument-hint` | Autocomplete hints for arguments | `[epic-name]`, `[pr-number] [priority]` | ❌ Optional |
| `allowed-tools` | Tool access restrictions | `[Read, Write, Edit]` | ❌ Optional |
| `model` | Override default model | `claude-sonnet-4-5` | ❌ Optional |

**Best Practice**: Always include `agent` field to ensure proper agent identification and UI labeling.

### Complete Template

```markdown
---
description: One-line description of what command does
agent: agent-name
---

You are the **[agent-name]** responsible for [primary responsibility].

## User Request

"$ARGUMENTS"

## Your Task

[High-level description of what agent should accomplish]

## Context

**Current Project**: Read from `.claude/current_project.txt`
**Epic Context**: Read from `{{project}}/.claude/epics/CURRENT_EPIC.md`
**Skills to Follow**: [List of relevant skills]

## Execution Steps

### 1. [First Step - Usually Validation/Preparation]

- Sub-task 1
- Sub-task 2

**Output**: [What this produces]

### 2. [Second Step - Usually Main Work]

- Sub-task 1
- Sub-task 2

**Output**: [What this produces]

### 3. [Third Step - Usually User Validation]

Present [output] to user and ask:
"[Specific validation question]"

**STOP and WAIT for user confirmation before continuing.**

### 4. [Fourth Step - After Validation]

Only after user confirms:
- Do next action 1
- Do next action 2

**Output**: [Final result]

### 5. [Final Step - Report to User]

Report to user:
```
[Template of what to say]

Next steps:
- [What user should do next]
```

## Critical Rules

**ALWAYS**:
- ✅ Read project context from current_project.txt
- ✅ Validate with user before destructive operations
- ✅ Follow [skill-name] skill patterns
- ✅ Report clear next steps

**NEVER**:
- ❌ Skip user validation checkpoints
- ❌ Proceed without required context
- ❌ Create files without showing user first
- ❌ Modify files without reading them first

## Error Handling

**If [error condition]**:
[How to handle gracefully]

**If [another error condition]**:
[How to handle gracefully]
```

## Command Workflow Patterns

### Pattern 1: Create/Generate Command

**Purpose**: Creates new artifacts (files, folders, etc.)

**Structure**:
1. Validate prerequisites
2. Gather required information
3. Generate draft
4. Show draft to user (STOP)
5. After approval, create files
6. Report completion and next steps

**Example**: `/create_epic`

```markdown
---
description: Create a new epic with business requirements
agent: epic-agent
---

You are the **epic-agent** responsible for creating business requirements.

## User Request

"$ARGUMENTS"

## Your Task

Create a new epic with business requirements in requirements.md.

## Execution Steps

### 1. Validate Context

- Read current project from `.claude/current_project.txt`
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md`
- Determine next epic number

### 2. Gather Requirements

Ask user (if not provided in arguments):
- Epic name
- Epic scope
- Key features
- Success criteria

### 3. Draft Requirements

Create requirements.md following `epic-requirements` skill template.

### 4. Show Draft to User

Present the draft requirements.md and ask:
"Does this match your vision? Should I create the epic?"

**STOP and WAIT for user confirmation.**

### 5. Create Epic

After user confirms:
- Create `{{project}}/.claude/epics/##-epic-name/` folder
- Write `requirements.md`
- Update `CURRENT_EPIC.md`

### 6. Report Completion

Report to user:
```
Epic created successfully! ✅

Location: {{project}}/.claude/epics/##-epic-name/requirements.md

Next steps:
- Review requirements.md
- Run `/create_plan` to generate technical tasks
```
```

### Pattern 2: Execute/Run Command

**Purpose**: Executes work from existing artifacts

**Structure**:
1. Read context and requirements
2. Execute task
3. Report results
4. Ask user to validate
5. Mark complete (or handle issues)

**Example**: `/next`

```markdown
---
description: Execute next task from current epic
agent: planning-agent
---

You are the **planning-agent** responsible for orchestrating task execution.

## Your Task

Execute the next TODO task from the current epic.

## Execution Steps

### 1. Read Context

- Read current project from `.claude/current_project.txt`
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` for active epic
- Read `{{project}}/.claude/epics/##-name/tasks.md` for task list
- Find first TODO task

### 2. Determine Agent

Based on task's assigned agent:
- `implementation-agent`: Invoke for code implementation and testing tasks
- `research-agent`: Invoke for research tasks

### 3. Invoke Agent

Provide agent with:
- Full task details (goal, requirements, acceptance criteria)
- Project context
- Relevant research files

### 4. Report Results

After agent completes:
- Show what was accomplished
- Ask user: "Task complete. Does this look correct?"

**STOP and WAIT for user validation.**

### 5. Mark Complete

After user validates:
- Update task status to COMPLETED in tasks.md
- Report next task preview (if any)
```

### Pattern 3: Fix/Debug Command

**Purpose**: Creates corrective tasks

**Structure**:
1. Understand the problem
2. Identify related task
3. Create sub-task
4. Report sub-task creation

**Example**: `/fix`

```markdown
---
description: Create bug fix sub-task
agent: planning-agent
---

You are the **planning-agent** responsible for task management.

## User Request

"$ARGUMENTS"

## Your Task

Create a bug fix sub-task in the current epic's tasks.md.

## Execution Steps

### 1. Understand Problem

Parse user's bug description from arguments.

If not clear, ask:
- What is broken?
- What should happen instead?
- Which feature is affected?

### 2. Identify Parent Task

Ask user: "Which task is this bug related to?"

Read `{{project}}/.claude/epics/##-name/tasks.md` to verify task exists.

### 3. Create Sub-Task

Determine next sub-task number (e.g., TASK-004.1, TASK-004.2).

Create sub-task following `task-planning` skill patterns:
- Title: "Fix: [bug description]"
- Goal: Fix the specific issue
- Parent: TASK-XXX
- Priority: Based on severity

### 4. Add to tasks.md

Insert sub-task after parent task in tasks.md.

### 5. Report Creation

Report to user:
```
Bug fix task created! ✅

Task: TASK-XXX.Y - Fix: [description]
Parent: TASK-XXX
Priority: [priority]

Run `/next` to execute the fix.
```
```

### Pattern 4: Research/Investigate Command

**Purpose**: Investigates unknowns and documents findings

**Structure**:
1. Understand research question
2. Check existing research
3. Conduct investigation
4. Document findings
5. Provide summary

**Example**: `/research`

```markdown
---
description: Research technical question and document findings
agent: research-agent
---

You are the **research-agent** responsible for investigating unknowns.

## User Request

"$ARGUMENTS"

## Your Task

Research the question and document findings in .claude/research/.

## Execution Steps

### 1. Parse Question

Extract research question from arguments.

If not clear, ask: "What specifically do you want to research?"

### 2. Check Existing Research

Search `{{project}}/.claude/research/` for existing files on topic.

If found, ask: "Found existing research. Should I update it or create new?"

### 3. Conduct Research

Follow `research-methodology` skill process:
1. Official documentation
2. Source code analysis
3. Successful mod examples
4. Community resources

### 4. Document Findings

Create `{{project}}/.claude/research/[topic].md` following template:
- Executive summary
- Key findings
- Implementation recommendations
- Code examples
- References

### 5. Provide Summary

Report to user:
```
Research complete! ✅

Quick Answer: [One-line answer]

Key Findings:
- Finding 1
- Finding 2
- Finding 3

Recommended Approach: [Specific recommendation]

Full research: {{project}}/.claude/research/[topic].md
```
```

## Command Argument Handling

### Simple Arguments

**For commands with single argument**:

```markdown
## User Request

"$ARGUMENTS"

## Parse Arguments

Extract [argument name] from user request.

**Example**: "/create_epic Add Guard Towers"
→ Epic name: "Add Guard Towers"
```

### Multiple Arguments

**For commands with multiple arguments**:

```markdown
## User Request

"$ARGUMENTS"

## Parse Arguments

**Format**: /command <arg1> <arg2> [optional]

Parse:
- `arg1`: First required argument
- `arg2`: Second required argument
- `optional`: Optional third argument (default: [value])

If arguments missing, ask user for clarification.
```

### Optional Arguments

**For commands with optional arguments**:

```markdown
## Parse Arguments

**Required**: [required arg]
**Optional**: [optional arg] (defaults to [value])

If required argument missing, ask user.
If optional argument missing, use default.
```

### Complex Arguments

**For commands with structured arguments**:

```markdown
## Parse Arguments

**Format**: /command --flag1 value1 --flag2 value2

Parse flags:
- `--flag1`: Description (required)
- `--flag2`: Description (optional, default: value)

If user provides free-form text instead, interpret intelligently.
```

## Skills Integration in Commands

### Reference Skills, Don't Load

```markdown
# ✅ CORRECT
## Your Task

Create requirements.md following the `epic-requirements` skill patterns.

## Execution Steps

### 2. Structure Requirements

Follow the `epic-requirements` skill template structure.

# ❌ WRONG
## Your Task

Load the `epic-requirements` skill and create requirements.md.

## Execution Steps

### 1. Load Skill

Load the `epic-requirements` skill.

### 2. Create Requirements

Use the loaded skill to create requirements.md.
```

**Why**: Skills auto-load. Commands just reference them.

### Multiple Skills

**When command uses multiple skills**:

```markdown
## Skills to Follow

This command uses patterns from:
- `epic-requirements` - requirements.md structure
- `research-methodology` - For gathering information
- `coding-standards` - For example code snippets

## Execution Steps

### 2. Draft Requirements

Follow `epic-requirements` skill template.

If unknowns encountered, use `research-methodology` skill to investigate.

Use `coding-standards` skill when showing code examples.
```

## Debugging Command Issues

### Issue: Wrong Agent Responds

**Symptoms**: User runs `/command` but general assistant responds or wrong agent

**Diagnosis**:
1. Check if command starts with "You are the [agent-name]"
2. Verify agent name matches file in `.claude/agents/`
3. Ensure command file is complete (doesn't rely on agent file merging)

**Fix**:
```markdown
# Before (missing agent identity)
---
description: Create an epic
---

Create a new epic with business requirements.

# After (clear agent identity)
---
description: Create a new epic with business requirements
agent: epic-agent
---

You are the **epic-agent** responsible for creating business requirements.

## Your Task

Create a new epic with business requirements in requirements.md.

[Complete workflow...]
```

### Issue: User Validation Skipped

**Symptoms**: Command creates files without showing user, proceeds without approval

**Diagnosis**:
1. Check if validation checkpoints exist
2. Look for "STOP and WAIT" language
3. Verify user questions are clear

**Fix**:
```markdown
# Before (no validation)
### 3. Create Epic

Create epic folder and requirements.md.

# After (with validation)
### 3. Show Draft to User

Present the draft requirements.md and ask:
"Does this match your vision? Should I create the epic?"

**STOP and WAIT for user confirmation before continuing.**

### 4. Create Epic (After Approval)

Only after user confirms, create epic folder and requirements.md.
```

### Issue: Missing Context

**Symptoms**: Command fails because it doesn't read required files

**Diagnosis**:
1. Check if command reads `current_project.txt`
2. Verify epic context is loaded
3. Look for required file reads in steps

**Fix**:
```markdown
# Before (missing context)
### 1. Create Task

Create new task in tasks.md.

# After (with context)
### 1. Read Context

- Read current project from `.claude/current_project.txt`
- Read `{{project}}/.claude/epics/CURRENT_EPIC.md` for active epic
- Read `{{project}}/.claude/epics/##-name/tasks.md` for task list
- Determine next task number

### 2. Create Task

Create new task with proper numbering in tasks.md.
```

### Issue: Unclear Next Steps

**Symptoms**: Command completes but user doesn't know what to do next

**Diagnosis**:
1. Check if final step reports to user
2. Verify next steps are mentioned
3. Look for specific command suggestions

**Fix**:
```markdown
# Before (unclear)
### 5. Report Completion

Epic created successfully.

# After (clear next steps)
### 5. Report Completion

Report to user:
```
Epic created successfully! ✅

Location: {{project}}/.claude/epics/05-epic-name/requirements.md

Next steps:
- Review and edit requirements.md if needed
- Run `/create_plan` to generate technical tasks
- After tasks created, run `/next` to start implementation
```
```

## Command Creation Checklist

When creating a new command:

- [ ] **Frontmatter**
  - [ ] Clear `description` (one line, user-facing)
  - [ ] Filename matches command name (`/command` → `command.md`)

- [ ] **Agent Identity**
  - [ ] Starts with "You are the [agent-name]"
  - [ ] Agent name matches existing agent file
  - [ ] Agent responsibility is clear

- [ ] **Task Definition**
  - [ ] "Your Task" section explains goal
  - [ ] Context section lists required reads
  - [ ] Skills section lists relevant skills

- [ ] **Argument Handling**
  - [ ] "User Request" captures $ARGUMENTS
  - [ ] Arguments parsed in first step
  - [ ] Missing arguments trigger user questions

- [ ] **Workflow Steps**
  - [ ] Steps numbered clearly (### 1., ### 2., etc.)
  - [ ] Each step has clear sub-tasks
  - [ ] Each step specifies output
  - [ ] Validation checkpoints included

- [ ] **User Validation**
  - [ ] Shows drafts before creating files
  - [ ] Uses "STOP and WAIT" language
  - [ ] Asks specific validation questions
  - [ ] Only proceeds after approval

- [ ] **Skills Integration**
  - [ ] Skills referenced (not "loaded")
  - [ ] Uses "Follow [skill] patterns" language
  - [ ] No manual skill loading steps

- [ ] **Error Handling**
  - [ ] Handles missing context gracefully
  - [ ] Provides helpful error messages
  - [ ] Suggests how to fix issues

- [ ] **Completion Reporting**
  - [ ] Final step reports to user
  - [ ] Shows what was created/modified
  - [ ] Lists specific next steps
  - [ ] Suggests relevant commands

- [ ] **Documentation**
  - [ ] Added to `.claude/CLAUDE.md` if significant
  - [ ] Usage examples provided
  - [ ] Related commands mentioned

## Best Practices

### ✅ DO

- **Start with agent identity** ("You are the X-agent")
- **Include validation checkpoints** before destructive operations
- **Provide clear next steps** after completion
- **Read context files** (current_project.txt, CURRENT_EPIC.md)
- **Reference skills** with "Follow [skill] patterns"
- **Handle missing arguments** gracefully
- **Use "STOP and WAIT"** language for validation
- **Show drafts to user** before creating files
- **Number steps clearly** for sequential workflows
- **Test mental walkthrough** of entire workflow

### ❌ DON'T

- **Skip agent identity** at the start
- **Assume context** - always read required files
- **Skip validation** before creating files
- **Use "load skill" language** - skills auto-load
- **Leave user hanging** - always suggest next steps
- **Create files blindly** - show drafts first
- **Use vague steps** - be specific and actionable
- **Forget error handling** - handle edge cases
- **Make steps too large** - break into sub-tasks
- **Skip documentation** if command is non-trivial

## Command Naming Conventions

### Action-Based Names

**Format**: `/<verb>_<noun>`

**Examples**:
- `/create_epic` - Creates a new epic
- `/create_plan` - Creates a task plan
- `/add_task` - Adds a task
- `/fix` - Creates a fix sub-task
- `/next` - Executes next task

### Consistency

**Use consistent verbs**:
- `create_*` - Creates new artifacts
- `add_*` - Adds to existing artifacts
- `run_*` - Executes something
- `serve_*` - Launches a service
- `*` (noun only) - For common actions (fix, next, research)

## Advanced Patterns

### Conditional Workflows

**For commands with different paths based on context**:

```markdown
### 2. Determine Workflow

**If epic exists**:
- Add to existing epic
- Update CURRENT_EPIC.md

**If no epic exists**:
- Ask user: "No active epic. Create one with `/create_epic` first?"
- Exit gracefully

**If multiple epics TODO**:
- Ask user: "Which epic? (Current: [epic-name])"
- Switch context if needed
```

### Multi-Agent Commands

**For commands that coordinate multiple agents**:

```markdown
### 3. Orchestrate Agents

**For research tasks**:
Invoke research-agent with [context]

**For implementation tasks**:
Invoke implementation-agent with [context]

**For testing tasks**:
Invoke implementation-agent with [context] (testing phase after user validation)

Coordinate results and report to user.
```

### Parameterized Commands

**For commands with many optional parameters**:

```markdown
## Parse Arguments

**Format**: /command [name] [--priority high|medium|low] [--agent agent-name] [--hours estimate]

**Defaults**:
- priority: medium
- agent: implementation-agent
- hours: 3-4

Parse intelligently:
- If user provides structured flags, use them
- If user provides free-form text, interpret
- Ask for clarification only if ambiguous
```

## Quality Standards

### Workflow Clarity

- Steps are numbered and sequential
- Each step has clear inputs and outputs
- Validation points are obvious
- Error paths are handled

### User Experience

- Clear guidance throughout process
- Validation at appropriate points
- Helpful next steps after completion
- Graceful error messages

### Agent Integration

- Clear agent identity
- Skills referenced correctly
- Handoffs documented
- Boundaries respected

### Maintainability

- Consistent structure with other commands
- Clear comments for complex logic
- Easy to update and extend
- Well-documented in CLAUDE.md

## References

- Related skills: `claude-code-agents`, `claude-code-skills`, `claude-code-setup`
- Documentation: `.claude/CLAUDE.md` (Commands section)
- Examples: See existing commands in `.claude/commands/`
- Claude Code docs: https://docs.claude.com/en/docs/claude-code
