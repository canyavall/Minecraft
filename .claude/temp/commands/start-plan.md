---
description: Start executing the implementation plan
---

You are the planning-agent starting implementation.

## Task

Start plan for: "$ARGUMENTS"

## Steps

### 1. Locate and Analyze Plan

- Read `.ai/specs/[spec-name]/plan.md`
- Count total TODO tasks
- Identify which agents are assigned to tasks
- Group consecutive tasks by agent

### 2. Ask User for Plan Approval

Ask user if the plan is approved and ready to execute.

### 3. Determine Execution Mode

Analyze the plan and ask user to choose execution mode:

**Option A: Stop After Each Task**
- Execute one task at a time
- Stop after each task for user review
- User runs `/next` to continue

**Option B: Continuous Execution (Same Agent)**
- Execute multiple consecutive tasks assigned to the same agent
- Stop only when:
  - Agent changes (e.g., implementation-agent → validation-agent)
  - All tasks completed
  - Error occurs

**Prompt**:
```
This plan has [N] tasks:
- [X] tasks assigned to implementation-agent
- [Y] tasks assigned to validation-agent
- [Z] tasks assigned to research-agent

Execution modes:
A) Stop after each task (manual control, slower)
B) Continuous execution - run consecutive tasks with same agent, stop when agent changes (faster)

Which execution mode would you like? [A/B]
```

### 4. Execute Based on Mode

#### Mode A: Stop After Each Task
1. Find first TODO task
2. Update task status to IN_PROGRESS
3. Launch assigned agent
4. After completion, update task to COMPLETED
5. Report completion and STOP
6. Wait for user to run `/next`

#### Mode B: Continuous Execution
1. Find first TODO task
2. Note the assigned agent
3. Execute task:
   - Update task to IN_PROGRESS
   - Launch assigned agent
   - After completion, update to COMPLETED
4. Check next task:
   - If next task has SAME agent → Continue to step 3
   - If next task has DIFFERENT agent → STOP and inform user
   - If no more tasks → STOP, plan complete
5. Report progress after each task

### 5. Progress Reporting

After each task completion, report:
- Task completed: [task name]
- Progress: [X] of [N] tasks completed
- Next action:
  - Mode A: "Run `/next` to continue"
  - Mode B (same agent): "Continuing to next task..."
  - Mode B (agent change): "Next task requires [different-agent]. Run `/next` to continue."
  - Plan complete: "All tasks completed!"

## Rules

### Mode A Rules
- Execute ONLY one task at a time
- STOP after each task completion
- Wait for user to run `/next`
- Provides maximum control

### Mode B Rules
- Execute consecutive tasks with same agent
- STOP when agent changes
- STOP when all tasks complete
- STOP on errors
- Provides faster execution for sequential work

### General Rules
- Always ask user for execution mode preference
- Track which mode is active throughout plan
- Provide clear progress updates
- Update task statuses in plan.md
- Pass complete context to agents (requirements, research, skills)
- Handle errors gracefully

## Context Provided to Agents

When launching an agent for a task, provide:
- `.ai/specs/[spec-name]/requirements.md` (what to build)
- `.ai/specs/[spec-name]/research.md` (if exists - how to build)
- Current task from plan.md (what to do now)
- Relevant skills (loaded automatically by agent)

## Error Handling

If a task fails:
1. Mark task as FAILED in plan.md
2. Report error to user
3. STOP execution (regardless of mode)
4. Suggest: Fix issue, then run `/next` to retry

## Example Output

### Mode A (Stop After Each)
```
✅ Task 1 completed: Set up project structure

Progress: 1 of 20 tasks completed
Mode: Manual (stop after each task)

Next: Run `/next` to start Task 2: Create base components
```

### Mode B (Continuous)
```
✅ Task 1 completed: Set up project structure
✅ Task 2 completed: Create base components
✅ Task 3 completed: Implement form validation

Progress: 3 of 20 tasks completed
Mode: Continuous (implementation-agent)

⚠️ Next task requires validation-agent (different agent)

Run `/next` to continue with validation tasks.
```
