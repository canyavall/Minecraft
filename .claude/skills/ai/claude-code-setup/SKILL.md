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

## File Locations & Configuration Files (2025)

### Required Configuration Files

```
.claude/
├── settings.json              # ✅ Team-shared settings (version controlled)
├── settings.local.json        # ✅ Personal settings (gitignored)
├── CLAUDE.md                  # ✅ Project instructions and overview
├── current_project.txt        # Project-specific: Active project tracker
├── agents/                    # Agent configurations
│   ├── epic-agent.md
│   ├── planning-agent.md
│   ├── implementation-agent.md
│   ├── research-agent.md
│   ├── project-agent.md
│   └── ai-agent.md
├── commands/                  # User slash commands
│   ├── create_epic.md
│   ├── create_plan.md
│   ├── next.md
│   ├── fix.md
│   ├── research.md
│   ├── serve_client.md
│   ├── create_project.md
│   └── ai.md
├── skills/                    # Knowledge packages (auto-loaded)
│   ├── minecraft/            # Domain-specific
│   ├── java/                 # Language-specific
│   ├── process/              # Workflow patterns
│   └── ai/                   # Meta skills
└── knowledge/                 # Centralized research (optional)
    ├── knowledge-index.json
    ├── README.md
    └── [research files]

# Optional files
.mcp.json                      # ⚠️ Model Context Protocol config (if using MCP servers)
```

### Configuration File Purposes

**settings.json** (Team-shared):
- Permissions (allow/deny/ask)
- Model selection
- Tool behavior
- Environment variables
- Plugin configuration
- Checked into source control

**settings.local.json** (Personal):
- Personal overrides
- Local development settings
- Automatically gitignored by Claude Code
- NOT checked into source control

**.mcp.json** (Optional):
- Model Context Protocol server configuration
- Extends Claude Code with external tools
- Project-level MCP servers

**CLAUDE.md**:
- Project overview and instructions
- Agent responsibilities
- Workflow patterns
- Command documentation

### What's New in 2025

**Configuration Changes**:
- ✅ `settings.json` and `settings.local.json` are now the standard (replacing older `config.json`)
- ✅ `.mcp.json` added for Model Context Protocol server configuration
- ✅ Plugin marketplace support via `enabledPlugins` in settings.json
- ✅ Agent skills system with YAML frontmatter
- ✅ Hierarchical settings: user (`~/.claude/settings.json`) → project (`.claude/settings.json`) → local (`.claude/settings.local.json`)

**Deprecated/Legacy**:
- ❌ `config.json` - Use `settings.json` instead
- ❌ `~/.claude.json` - Use `~/.claude/settings.json` instead (though some docs mention both for compatibility)

### Configuration Best Practices

**1. Settings Hierarchy**:
```
User settings (~/.claude/settings.json)
  ↓ (overridden by)
Project settings (.claude/settings.json)
  ↓ (overridden by)
Local settings (.claude/settings.local.json)
```

**2. What Goes Where**:

**settings.json** (team-shared):
- Baseline permissions everyone needs
- Project-wide tool access patterns
- Standard model configurations
- Shared environment variables

**settings.local.json** (personal):
- Personal API keys
- Local development overrides
- Experimental settings
- Machine-specific paths

**3. Security Best Practices**:
- ALWAYS add `.claude/settings.local.json` to `.gitignore`
- NEVER commit API keys or secrets
- Use `permissions.deny` to block sensitive files (`.env`, `credentials.json`, etc.)

**4. MCP Server Configuration** (Optional):
- Use `.mcp.json` for project-level MCP servers
- Place in project root (same level as `.claude/`)
- Version control if team needs same MCP servers

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
You DO NOT create plans (planning-agent) or write code and tests (implementation-agent).
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
