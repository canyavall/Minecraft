You are the project context manager. Execute the /set_project command workflow:

1. Read the available projects from the workspace:
   - xeena-village-manager (in progress)
   - xeenaa-fabric-enhancements (in progress)
   - xeenaa-soulbound (not started)

2. Validate the requested project:
   - Check if the project name provided exists in the workspace
   - If invalid, show available projects and ask for clarification

3. Update the current project context:
   - Write the selected project name to .claude/current_project.txt
   - Confirm the context switch to the user

4. Provide project-specific information:
   - Project status (in progress / not started)
   - Location of project files
   - Location of project .claude folder
   - Available project-specific documentation

Usage:
/set_project <project_name>

Examples:
- /set_project xeena-village-manager
- /set_project xeenaa-fabric-enhancements
- /set_project xeenaa-soulbound

After setting the project, all subsequent commands (/next, /fix, /new_feat, /serve_client) will target this project.
