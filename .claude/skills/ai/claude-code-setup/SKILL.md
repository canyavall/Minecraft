---
name: claude-code-setup
description: Guidelines for configuring and maintaining Claude Code infrastructure including agents, commands, and skills system.
category: ai
tags: [ai, claude-code, agents, commands, skills, workflow]
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Claude Code Setup

Guidelines for maintaining Claude Code infrastructure: agents, commands, and skills.

## Purpose

- Creating and maintaining agents
- Writing effective commands
- Organizing skills
- Debugging workflow issues
- Ensuring consistency

## Architecture

1. **Agents** (`.claude/agents/*.md`) - Specialized roles, reference skills
2. **Commands** (`.claude/commands/*.md`) - User workflows, invoke agents
3. **Skills** (`.claude/skills/**/*`) - Knowledge packages, auto-loaded
4. **Project Instructions** (`.claude/CLAUDE.md`) - Overview, workflows

## Agent Configuration

### Structure

```markdown
---
name: agent-name
description: Brief purpose
model: sonnet
color: blue|green|red|purple|yellow
---

You are the [Agent Name] responsible for [primary purpose].

## CRITICAL: Your ONLY Responsibility

**You ONLY manage**: [Clear scope]

**What you create**:
- Specific outputs

**You DO NOT**:
- ❌ Thing 1 (other-agent does this)
- ❌ Thing 2 (another-agent does this)

## Primary Responsibilities

[Detailed responsibilities]

## Skills You Use

[List skills - Claude Code loads automatically]

## Critical Rules

**ALWAYS**: ✅ Best practices
**NEVER**: ❌ Anti-patterns
```

### Best Practices

- **Clear Boundaries**: ONE primary responsibility, comprehensive DO NOT list
- **Skills**: List skills used, never "load manually", state auto-loading
- **Consistent Structure**: Same template, uniform formatting
- **Color Coding**: blue (planning), green (spec), red (validation), purple (meta), yellow (research)

## Command Configuration

### Structure

```markdown
---
description: What command does
---

You are the [agent-name] [doing what].

## User Request

"$ARGUMENTS"

## Your Task

[High-level task]

## Execution Steps

### 1. Step Name
- Sub-task 1
- Sub-task 2

### N. Report to User
[What to tell user]

## Critical Rules

**DO NOT**:
- ❌ Skip user validation
- ❌ Proceed without approval
```

### Best Practices

- **Agent Identity**: Start with "You are the [agent-name]"
- **Validation**: Include approval checkpoints
- **Skills**: Use "Follow [skill] patterns", NOT "Load [skill]"
- **Clear Steps**: Numbered, actionable steps

## Skills Configuration

### Structure

```markdown
---
name: skill-name
description: What skill covers
category: core|libraries|quality|process|ai
tags: [relevant, tags]
---

# Skill Title

Brief description

## When to Use

- Specific use case 1
- Specific use case 2

## [Core Content - max 300 lines]

**See `examples/*.md` for detailed patterns**

## References

- Related skills
- Examples
- External docs
```

### Best Practices

- **Max 300 Lines**: Keep SKILL.md concise, move details to examples/
- **Progressive Loading**: Quick reference in SKILL.md, depth in examples/
- **Clear Metadata**: Accurate tags and description
- **No Duplication**: Reference other skills, don't repeat content

## File Locations

```
.claude/
├── agents/              # Agent configurations
│   ├── spec-agent.md
│   ├── planning-agent.md
│   ├── implementation-agent.md
│   └── validation-agent.md
├── commands/            # User commands
│   ├── create-spec.md
│   ├── create-plan.md
│   └── start-plan.md
├── skills/              # Knowledge packages
│   ├── core/           # Essential patterns
│   ├── libraries/      # Library usage
│   ├── quality/        # Testing patterns
│   ├── process/        # Workflows
│   └── ai/             # Meta skills
└── CLAUDE.md           # Project overview
```

## Common Patterns

### Agent Handoff

```markdown
## When to Hand Off

If the user requests [X], respond:
"That's outside my scope. Use the /[command] command to invoke [other-agent]."
```

### Validation Checkpoint

```markdown
## Step N: Show to User

Present [output] to user and ask: "Does this look correct? Should I proceed?"

**WAIT for user confirmation before continuing.**
```

### Skills Reference

```markdown
## Skills You Use

Follow patterns from these skills (Claude Code loads automatically):
- `skill-name` - Purpose
- `skill-name` - Purpose
```

## Anti-Patterns to Avoid

### ❌ Manual Skills Loading

```markdown
# Wrong
Load the `skill-name` skill and follow its patterns.

# Correct
Follow the `skill-name` skill patterns.
```

### ❌ Vague Boundaries

```markdown
# Wrong
You create specifications.

# Correct
You ONLY create requirements.md files from ticket.md or user prompts.
You DO NOT create plans (planning-agent), write code (implementation-agent), or write tests (validation-agent).
```

### ❌ Missing Validation

```markdown
# Wrong
3. Create the plan
4. Start implementation

# Correct
3. Create the plan
4. Show plan to user and wait for approval
5. Only after approval, start implementation
```

## Debugging Workflow Issues

See `workflow-debugging` skill for comprehensive debugging guidance.

Quick checklist:
- [ ] Command clearly identifies agent?
- [ ] No "load skill" language?
- [ ] Agent boundaries clear?
- [ ] Validation checkpoints present?
- [ ] Skills referenced correctly?

## Creating New Agents

1. Copy template from existing agent
2. Define ONLY Responsibility (very specific)
3. List comprehensive DO NOTs
4. Reference skills (auto-loaded)
5. Add color coding
6. Test with sample command

## Creating New Commands

1. Start with agent identity
2. Parse user arguments clearly
3. Define execution steps
4. Add validation checkpoints
5. Use "Follow skill patterns" language
6. Test workflow end-to-end

## Creating New Skills

1. Choose appropriate category folder in .claude/skills/
2. Write concise SKILL.md with proper frontmatter (name, description, allowed-tools)
3. Move large details to examples/ if needed
4. Update .claude/CLAUDE.md to document the new skill
5. Update skills-tags-mapping.json (for Copilot integration)

## Verification Checklist

After making changes:

- [ ] Read changed files to verify
- [ ] Check related files for consistency
- [ ] Test mental workflow walkthrough
- [ ] Update documentation if needed
- [ ] Run `/ai-test` to verify (if available)

## References

- Related skills: `workflow-debugging`, `copilot-setup`
- Examples: `examples/agent-templates.md`, `examples/command-templates.md`
- Documentation: `.claude/CLAUDE.md` (complete skills and agent documentation)
- Claude Code docs: https://docs.claude.com/en/docs/claude-code
