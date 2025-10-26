---
name: multi-project-workflow
description: Multi-project workspace workflows, common patterns, and troubleshooting
category: process
allowed-tools: [Read, Write, Edit, Bash, Glob]
---

# Multi-Project Workflow Skill

This skill provides workflow patterns for working with multiple Minecraft mod projects in a shared workspace.

## Project Context Management

### Setting Project Context

Before ANY work, set the active project:

**User says**:
- "Set project to xeena-village-manager"
- "Switch to xeenaa-fabric-enhancements"
- "Work on xeenaa-soulbound"

**You must**:
1. Validate project name exists
2. Update `.claude/current_project.txt` with format: `CURRENT_PROJECT=<project_name>`
3. Confirm switch and show project status

### Project Context System

- Active project stored in `.claude/current_project.txt`
- All commands read this file to determine target project
- Use `{{project}}/` path placeholders in prompts
- Commands automatically target active project

## Common Workflow Patterns

### Pattern 1: Create New Epic

```
User: /create_epic "Feature Name"
→ epic-agent creates requirements.md
→ STOP and wait for user validation
→ User validates requirements
→ User runs /create_plan
→ planning-agent creates plan.md
→ User runs /next to begin tasks
```

### Pattern 2: Execute Tasks

```
User: /next
→ planning-agent reads plan.md
→ Invokes appropriate agent (implementation/research/validation)
→ Agent completes task
→ STOP and wait for user confirmation
→ User runs /next for next task
```

### Pattern 3: Fix Bug

```
User: /fix "Bug description"
→ planning-agent asks which task it's related to
→ Creates sub-task (e.g., TASK-004.1)
→ User runs /next to execute fix
```

### Pattern 4: Add Task to Epic

```
User: /add_task "New task description"
→ planning-agent determines next task number
→ Asks priority, effort, agent
→ Creates task in plan.md
→ User runs /next when ready
```

### Pattern 5: Research Unknown

```
User: /research "How does X work?"
→ research-agent checks existing research
→ Conducts research if not found
→ Creates research document
→ Provides immediate summary
```

### Pattern 6: Test in Minecraft

```
User: /serve_client
→ Build mod if needed
→ Launch Minecraft client
→ User tests manually
```

### Pattern 7: Switch Projects

```
User: "Switch to project-name"
→ Validate project exists
→ Update .claude/current_project.txt
→ Confirm switch
→ All subsequent /commands target new project
```

### Pattern 8: Create New Project

```
User: /create_project "Project Name"
→ project-agent validates name
→ Asks for author, description, vision
→ Copies template
→ Creates .claude/ infrastructure
→ Sets as active project
→ User runs /create_epic for first epic
```

## Troubleshooting

### "No current project set" Error

**Cause**: `.claude/current_project.txt` is empty or missing
**Solution**: Ask user which project, then update file with `CURRENT_PROJECT=<name>`

### Agent Can't Find Files

**Cause**: Project context not set or wrong paths
**Solution**:
1. Verify `.claude/current_project.txt` has correct project
2. Ensure using `{{project}}/` path placeholders
3. Check file exists in correct project folder

### Conflicting Coding Styles

**Cause**: Not applying coding-standards skill consistently
**Solution**: coding-standards skill activates automatically for all code

### Lost Task History

**Cause**: Looking for changelog.md or bugs.md
**Solution**: All history is in `{{project}}/.claude/epics/##-name/plan.md`

### Commands Target Wrong Project

**Cause**: Forgot to set project context
**Solution**: ALWAYS check/set project context FIRST before any work

## Multi-Project Best Practices

### ✅ DO
- Set project context before starting work
- Keep project research in project-specific folders
- Apply shared skills consistently
- Use consistent task structure across projects
- Document project-specific patterns in project.md

### ❌ DON'T
- Work on multiple projects simultaneously
- Duplicate standards or agents across projects
- Create changelog or issuestracker files (use plan.md)
- Hard-code project names in agents or commands
- Skip manual testing before automated tests

## Command Decision Guide

| Situation | Command | Result |
|-----------|---------|--------|
| Create new project | `/create_project <name>` | Complete project from template |
| Start new feature | `/create_epic <name>` | Epic folder with requirements.md |
| Ready for tasks | `/create_plan` | plan.md with technical tasks |
| Add task to epic | `/add_task <desc>` | New main task (TASK-015) |
| Fix bug | `/fix <desc>` | Sub-task (TASK-001.1) |
| Research unknown | `/research <question>` | Research document |
| Execute next task | `/next` | Run next TODO task |
| Test in game | `/serve_client` | Launch Minecraft |
| AI infrastructure | `/ai <desc>` | Debug/fix agents/skills/commands |

## Task Numbering

- Main tasks: `TASK-001`, `TASK-002`, `TASK-015` (sequential)
- Bug fixes: `TASK-001.1`, `TASK-001.2` (sub-tasks)

## File Organization

### Shared Resources (Root `.claude/`)
- `agents/` - Agent definitions
- `commands/` - Global commands
- `skills/` - Shared knowledge skills
- `current_project.txt` - Active project tracker
- `CLAUDE.md` - Main project instructions

### Project-Specific (`{{project}}/.claude/`)
- `project.md` - Project overview
- `research/` - Research findings
- `temp/` - Temporary files
- `epics/CURRENT_EPIC.md` - Epic tracking
- `epics/##-name/requirements.md` - Business requirements
- `epics/##-name/plan.md` - Technical tasks

## Key Principles

1. **Shared Infrastructure** - Agents, commands, skills used everywhere
2. **Project Isolation** - Each mod independent
3. **Context Awareness** - Commands respect active project
4. **Single Source of Truth** - plan.md is only tracking
5. **Skills-Based Knowledge** - Skills activated automatically
6. **Agent Specialization** - Clear non-overlapping responsibilities
7. **User Validation** - Manual testing before automated tests
