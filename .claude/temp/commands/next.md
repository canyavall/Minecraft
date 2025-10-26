---
description: Continue with next task in plan
---

You are the planning-agent executing the next task.

## Task

Find and execute next TODO task from active plan, respecting execution mode.

## Steps

### 1. Locate Active Plan

- Find active spec with IN_PROGRESS or TODO tasks in `.ai/specs/`
- Load `plan.md`
- Check for execution mode metadata in plan (Mode A or Mode B)
- If no mode specified, default to Mode A (stop after each)

### 2. Check Execution Mode

**Mode A: Stop After Each Task**
- Execute exactly one task
- Stop after completion
- Wait for user to run `/next` again

**Mode B: Continuous Execution (Same Agent)**
- Execute tasks continuously while same agent
- Stop when agent changes
- Stop when all tasks complete
- Stop on errors

### 3. Find Next Task

1. Find first TODO task (check dependencies met)
2. Note which agent is assigned to this task
3. Update task status to IN_PROGRESS

### 4. Execute Task

1. Launch assigned agent with full context:
   - `.ai/specs/[spec-name]/requirements.md`
   - `.ai/specs/[spec-name]/research.md` (if exists)
   - Current task from plan.md
   - Relevant skills (loaded automatically)
2. After completion, update task to COMPLETED

### 5. Determine Next Action

#### If Mode A (Stop After Each):
- Report completion
- Show progress (X of N tasks)
- STOP
- Tell user to run `/next` for next task

#### If Mode B (Continuous):
1. Check if there are more TODO tasks
2. If yes, check next task's agent:
   - **Same agent as current task**: Continue to step 3 (execute next task)
   - **Different agent**: STOP and inform user which agent is needed next
3. If no more tasks: Report plan complete and STOP

### 6. Progress Reporting

After each task, report:
- Task completed: [task name]
- Progress: [X] of [N] tasks completed
- Execution mode: [A or B]
- Next action:
  - Mode A: "Run `/next` to continue"
  - Mode B (same agent): "Continuing to next task..."
  - Mode B (agent change): "Next task requires [agent-name]. Run `/next` to continue."
  - Plan complete: "All tasks completed! 🎉"

## Rules

### Mode Detection
- Check plan.md for execution mode metadata
- If not found, default to Mode A
- Execution mode set by `/start-plan` command
- Mode persists throughout plan execution

### Mode A Behavior
- Execute ONLY one task per `/next` invocation
- ALWAYS stop after task completion
- Require explicit `/next` for each task
- Maximum user control

### Mode B Behavior
- Execute multiple consecutive tasks with same agent
- Stop when:
  - Agent changes (e.g., implementation → validation)
  - No more tasks
  - Error occurs
- Report progress after each task
- Faster execution for sequential work

### General Rules
- Always check dependencies before executing task
- Update task statuses in plan.md
- Handle errors gracefully (mark as FAILED, stop, report)
- Provide clear progress updates
- If plan complete, congratulate user

## Error Handling

If a task fails:
1. Mark task as FAILED in plan.md
2. Add error details to plan.md
3. STOP execution (regardless of mode)
4. Report error to user with details
5. Suggest: Fix issue, then run `/next` to retry same task

## Example Outputs

### Mode A - Single Task Execution
```
✅ Task 4 completed: Implement user authentication

Progress: 4 of 20 tasks completed
Mode: Manual (stop after each task)

Next: Run `/next` to start Task 5: Add form validation
```

### Mode B - Continuous (Same Agent)
```
✅ Task 4 completed: Implement user authentication
✅ Task 5 completed: Add form validation
✅ Task 6 completed: Create dashboard layout

Progress: 6 of 20 tasks completed
Mode: Continuous (implementation-agent)

Continuing to next task: Task 7: Add data fetching...
```

### Mode B - Agent Change
```
✅ Task 10 completed: Complete UI implementation

Progress: 10 of 20 tasks completed
Mode: Continuous (implementation-agent)

⚠️ Next task (Task 11) requires validation-agent

The implementation phase is complete. Ready to start validation phase.

Run `/next` to begin validation tasks.
```

### Plan Complete
```
✅ Task 20 completed: Final integration testing

Progress: 20 of 20 tasks completed

🎉 All tasks completed! Plan execution finished.

Summary:
- Total tasks: 20
- Completed: 20
- Failed: 0

Next steps:
- Review implementation
- Run `/validate [spec-name]` to verify requirements
- Create pull request
```

## State Tracking

The command should track:
- Current execution mode (A or B)
- Last executed agent
- Tasks completed in current run (for Mode B)
- Total progress

Store this in plan.md metadata or track in memory between invocations.
