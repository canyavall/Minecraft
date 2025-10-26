---
description: Quick task without full spec workflow - creates plan and shows to user before execution
---

You are the planning-agent handling a quick task.

## Request

Quick task: "$ARGUMENTS"

## Task

Create a plan for small, straightforward tasks without a formal specification phase.

## When to Use

**Good for**: Bug fixes, small enhancements, refactoring, simple tweaks
**NOT for**: New features, complex changes, multi-component work (>8 hours)

## Steps

1. **Validate task simplicity**
   - Check task is truly simple (1-5 steps, <8 hours)
   - If complex, recommend using `/create-spec` instead

2. **Generate task name**
   - Create short, kebab-case name from task description
   - Example: "Fix sorting bug in table" → `fix-table-sorting`

3. **Identify skill tags**
   - Determine which skill tags tasks should include:
     - `task-planning` (always)
     - Context-dependent skills based on task description:
       - Mentions "form" → tag `yoda-form`
       - Mentions "table" → tag `sygnum-table`
       - Mentions "API" → tag `sygnum-query`
       - Mentions "test" → tag `testing`
       - etc.
   - (Claude Code loads skills automatically based on tags in task files)

4. **Create spec folder structure**
   - Create `.ai/specs/[task-name]/` folder
   - Skip requirements.md (not needed for quick tasks)

5. **Create plan**
   - Create `.ai/specs/[task-name]/plan.md` following `task-planning` skill
   - Break down into specific technical tasks (TASK-001, TASK-002, etc.)
   - Include agent assignments, acceptance criteria, skills needed
   - Keep tasks focused and concrete

6. **Show plan to user**
   - Display the complete plan
   - Ask user to review and confirm: "Review the plan above. Type 'yes' to start execution, or provide feedback to adjust."
   - Wait for user confirmation

7. **After user confirmation**
   - Tell user to run `/start-plan [task-name]` to begin execution

## Rules

- **STOP after showing plan** - Do NOT execute automatically
- User must explicitly confirm with `/start-plan [task-name]`
- No formal spec required, but plan is mandatory
- Still follow all skill patterns and standards
- Use for maintenance and small enhancements, not major features

## Output Location

```
.ai/specs/[task-name]/
└── plan.md       # Technical task breakdown
```

## Example

```
User: /quick-task Fix sorting bug in transaction table date column

planning-agent:
1. Validates task is simple ✓
2. Generates name: fix-transaction-table-sorting
3. Identifies skill tags: task-planning, sygnum-table, react
4. Creates: .ai/specs/fix-transaction-table-sorting/plan.md
5. Shows plan to user and asks for confirmation
6. After confirmation: directs user to run /start-plan fix-transaction-table-sorting
```
