You will execute a two-phase workflow to add a new feature.

**PHASE 1: Project Scope Manager**

You are the project-scope-manager. A user wants to add a new feature:

"$ARGUMENTS"

Your tasks:
1. Read .claude/current_project.txt to determine the active project
2. Read {{project}}/.claude/project.md to understand current project state
3. Design this feature (include game mechanics if needed: progression, economy, balance)
4. Determine which epic/phase this feature belongs in (or create new epic)
5. Write the feature specification in {{project}}/.claude/project.md with:
   - Clear description
   - Game mechanics design (if applicable)
   - User experience
   - Success criteria
6. Create epic folder {{project}}/.claude/epics/{{epic_name}}/ if needed
7. Save complex game mechanics to {{project}}/.claude/game-design/ if applicable
8. Report completion to user

**PHASE 2: Tech Lead Coordinator**

After Phase 1, you become the tech-lead-coordinator.

Your tasks:
1. Read the updated {{project}}/.claude/project.md
2. Identify the new feature that was just added
3. Determine which epic this feature belongs to:
   - Read {{project}}/.claude/epics/CURRENT_EPIC.md to see active epic
   - Create new epic if needed (with enumeration like 04-feature-name)
4. Read the epic's tasks file ({{project}}/.claude/epics/{{epic_name}}/tasks.md)
5. Break the new feature into concrete, actionable tasks
6. **STOP and REQUEST USER PRIORITIZATION**: Ask user where these new tasks should be prioritized compared to existing tasks
7. After user confirms priority, create the tasks in the epic's tasks.md file with proper:
   - Task descriptions
   - Assigned agents
   - Goals and requirements
   - Guidelines/resources (reference .claude/standards/minecraft-modding-standards.md)
   - Acceptance criteria
   - Dependencies
8. Update {{project}}/.claude/epics/CURRENT_EPIC.md if new epic created
9. Report task creation complete

If no current project is set, ask user to run /set_project first.
Do NOT create tasks without first getting user prioritization approval.
