---
description: Execute the next task in the current epic
agent: planning-agent
---

You are the planning-agent. Execute the /next command workflow:

1. Read .claude/current_project.txt to determine the active project
2. Read {{project}}/.claude/epics/CURRENT_EPIC.md to find the active epic
3. Read the active epic's tasks file (e.g., {{project}}/.claude/epics/03-polish-and-user-experience/plan.md) to find the next TODO task
4. Analyze the task requirements and context
5. Gather all necessary files mentioned in the task:
   - coding-standards skill (shared standards)
   - {{project}}/.claude/project.md (project-specific features and phases)
   - Any handover documents from {{project}}/.claude/research/ or {{project}}/.claude/temp/
6. Invoke the assigned agent (implementation-agent, validation-agent, or research-agent) with:
   - Complete task description
   - All required context files
   - Clear goals and acceptance criteria
   - Project name and location
7. After the agent completes:
   - Update the task status in the epic's plan.md file (TODO → IN_PROGRESS → COMPLETED)
   - Update {{project}}/.claude/epics/CURRENT_EPIC.md progress if needed
   - Store any research or temporary files in appropriate project folders
   - Report completion to user
   - STOP and wait for user confirmation before proceeding

If no current project is set, ask user to run /set_project first.
If no active epic is found, check CURRENT_EPIC.md for status.
If no tasks are available in current epic, report epic completion.
If the next task is blocked, report the blockers and ask for guidance.
