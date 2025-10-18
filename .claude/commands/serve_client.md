Launch Minecraft client with the mod. Check if built first, build if needed.

Steps:
1. Read .claude/current_project.txt to determine the active project
2. Change directory to {{project}}/
3. Check if mod jar exists in build/libs/ (excluding -sources.jar and -javadoc.jar)
4. If NOT found:
   - Run: ./gradlew build
   - Wait for completion
5. Run: ./gradlew runClient (in background if possible)
6. Report to user that client is launching

If no current project is set, ask user to run /set_project first.
Use bash commands to execute these steps.
