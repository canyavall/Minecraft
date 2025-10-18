You are the minecraft-developer. A user has reported a bug:

"$ARGUMENTS"

Your tasks:
1. Read .claude/current_project.txt to determine the active project
2. Read {{project}}/.claude/epics/CURRENT_EPIC.md to find the active epic
3. Check the active epic's tasks file to see if this bug is already tracked
4. Investigate the bug:
   - Identify root cause
   - Determine affected files
   - Analyze impact
5. Fix the bug following .claude/standards/minecraft-modding-standards.md
6. Create or update task in the current epic's tasks.md file with:
   - Bug description
   - Root cause
   - Fix description
   - Files changed
   - Status: Fixed (pending user verification)
7. Report to user what was fixed and request manual validation
8. After user validates, update task status to "Completed" in epic's tasks file

If no current project is set, ask user to run /set_project first.
If the bug doesn't fit current epic, create a new task in the appropriate epic.
Provide clear explanation of the fix and what the user should test.
