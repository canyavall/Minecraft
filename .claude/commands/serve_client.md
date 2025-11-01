---
description: Launch Minecraft client with the mod (builds first if needed)
agent: planning-agent
---

You are the planning-agent launching the Minecraft client for testing.

## CRITICAL ASSUMPTION

**When the user invokes /serve_client, they want a NEW instance**. Even if a process exists in the background, assume they want to start fresh. Never tell them "it's already running" - if they invoke this command, they want it running NOW.

## Your Task

Launch a fresh Minecraft client with the mod. Always start clean.

## Execution Steps

### 1. Determine Active Project
Read `.claude/current_project.txt` to get the project name.

If no project is set, ask user to set project first.

###2. Kill Any Existing Processes (MANDATORY)

**Before launching, ALWAYS kill existing Gradle/Minecraft processes:**

```bash
# Windows: Kill any gradle or java processes
powershell -Command "Get-Process | Where-Object {\$_.ProcessName -like '*java*' -and \$_.CommandLine -like '*runClient*'} | Stop-Process -Force" 2>$null || true

# Also try killing gradle daemon
cd {{project}} && ./gradlew --stop 2>$null || true
```

This ensures we start fresh every time.

### 3. Navigate to Project Directory

```bash
cd {{project}}/
```

### 4. Check if Build Exists

Check if mod jar exists in `build/libs/` (excluding `-sources.jar` and `-javadoc.jar`).

If NOT found, run:
```bash
./gradlew build
```

Wait for build to complete before proceeding.

### 5. Launch Fresh Client Instance

```bash
./gradlew runClient
```

Run in background using `run_in_background` parameter so user can continue working.

### 6. Report to User

Tell user:
- "Launching fresh Minecraft client for {{project}}"
- "Building first..." (if build was needed)
- "Client starting in background - game window will appear shortly"
- "You can test the mod once Minecraft loads"

**NEVER say**:
- ❌ "Client is already running"
- ❌ "Process exists, skipping launch"
- ❌ "Use existing instance"

**DO say**:
- ✅ "Killed existing processes, starting fresh"
- ✅ "Launching new client instance"
- ✅ "Client is starting"

## Important Notes

- User invoked this command = they want Minecraft running NOW
- Don't check if processes exist - just kill and restart
- Always assume fresh start is desired
- Background processes may exist but be inaccessible - kill anyway
- If gradle daemon is locked, --stop will release it

## Example Output

```
Killed any existing Gradle/Minecraft processes.
Building mod...
Build complete.
Launching fresh Minecraft client for xeenaa-alexs-mobs.
Game window will appear shortly - you can test the crow once loaded!
```
