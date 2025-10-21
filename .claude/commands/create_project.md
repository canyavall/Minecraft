You are the project-agent creating a new Minecraft mod project from the template.

## User Requested Project

"$ARGUMENTS"

## Your Task: Create Complete Project from Template

### 1. Validate Project Name

- Check if project already exists in workspace
- Validate naming convention (kebab-case, lowercase)
- Ensure name is descriptive and follows pattern: `xeenaa-[feature-area]`

**If invalid name**: Suggest corrections and ask user to confirm

**Existing projects** (check these):
- xeena-village-manager
- xeenaa-fabric-enhancements
- xeenaa-soulbound

### 2. Verify Template Exists

Check that `mod_template/` exists at workspace root:
- `/Users/miguelperello/Documents/projects/Minecraft/mod_template/`

If template doesn't exist, report error and ask user to create template first.

### 3. Parse Project Details

From the project name, determine:
- **Folder name**: kebab-case (e.g., `xeenaa-economy-system`)
- **Mod ID**: Same as folder name (e.g., `xeenaa-economy-system`)
- **Display name**: Title case (e.g., "Xeenaa Economy System")
- **Package name**: lowercase, underscores (e.g., `xeenaa_economy_system`)

**Ask user to confirm**:
- Mod ID: `xeenaa-economy-system`
- Display Name: "Xeenaa Economy System"
- Author: [Ask user for author name]
- Description: [Ask user for short description]

### 4. Copy Template Structure

Copy `mod_template/` to new project folder:

```bash
cp -r mod_template/ {{project_name}}/
```

**Folder location**: `/Users/miguelperello/Documents/projects/Minecraft/{{project_name}}/`

### 5. Update Template Placeholders

Replace placeholders in these files:

**gradle.properties**:
- `{{MOD_ID}}` → actual mod ID
- `{{MOD_NAME}}` → display name
- `{{VERSION}}` → "0.1.0"
- `{{AUTHOR}}` → author name

**fabric.mod.json**:
- `{{MOD_ID}}` → actual mod ID
- `{{MOD_NAME}}` → display name
- `{{DESCRIPTION}}` → user-provided description
- `{{AUTHOR}}` → author name
- `{{VERSION}}` → "0.1.0"

**build.gradle**:
- Update if any project-specific build config needed
- Usually template is fine as-is

**README.md**:
- `{{MOD_NAME}}` → display name
- `{{DESCRIPTION}}` → user-provided description

**Package structure** in `src/main/java/`:
- Rename package from template to: `com/{{author}}/{{package_name}}/`

### 6. Create .claude/ Infrastructure

Create complete `.claude/` folder structure:

```
{{project}}/.claude/
├── project.md           # You create this (see template below)
├── research/            # Empty folder
├── temp/                # Empty folder
├── game-design/         # Empty folder
└── epics/               # Epic folder structure
    └── CURRENT_EPIC.md  # Initial tracking file
```

**Create empty folders**:
- `{{project}}/.claude/research/`
- `{{project}}/.claude/temp/`
- `{{project}}/.claude/game-design/`
- `{{project}}/.claude/epics/`

### 7. Create project.md

Create `{{project}}/.claude/project.md`:

```markdown
# {{Display Name}}

**Minecraft Version**: 1.21.1
**Mod Loader**: Fabric
**Java Version**: 21
**Created**: {{Current Date}}
**Author**: {{Author Name}}

## Project Vision

{{Ask user: "What is the vision for this mod? What problem does it solve or experience does it create?"}}

## Goals

{{Ask user: "What are the main goals? (3-5 bullet points)"}}

## Technical Stack

### Dependencies
- **Fabric API**: Latest for 1.21.1
- **Fabric Loader**: Latest
- **Java**: 21

### Build System
- **Gradle**: 8.x
- **Fabric Loom**: Latest

## Architecture Overview

### Package Structure
```
src/main/java/com/{{author}}/{{package_name}}/
├── config/          # Configuration
├── registry/        # Registration
└── [feature-packages as needed]
```

### Core Systems

{{Default to: "To be defined as epics are created"}}

## Development Guidelines

### Code Standards
- Follow `coding-standards skill`
- Java 21 modern features (records, pattern matching, text blocks)
- Fabric events over mixins when possible
- Client-server separation

### Epic Organization
- Each epic represents a major feature area
- Epic requirements define WHAT (business value)
- Epic tasks define HOW (technical implementation)
- User validates requirements before task creation

## Project Scope

### In Scope

{{Ask user: "What major feature areas are IN SCOPE for this project?"}}

### Out of Scope

{{Ask user: "What should explicitly be OUT OF SCOPE?"}}

## Performance Targets

{{If performance-focused mod, ask user for targets. Otherwise: "Standard Minecraft mod performance expectations"}}

## Compatibility

- **Minecraft Versions**: 1.21.1
- **Mod Compatibility**: {{Ask user if there are specific mods to support/avoid}}
- **Server/Client**: {{Ask user: "Both / Server-only / Client-only?"}}

## Epic Status

No epics created yet. Run `/create_epic "Epic Name"` to add your first epic.
```

### 8. Create Initial CURRENT_EPIC.md

Create `{{project}}/.claude/epics/CURRENT_EPIC.md`:

```markdown
# Current Epic Status - {{Display Name}}

**Last Updated**: {{Current Date}}

## Epic Overview

| Epic # | Name | Status | Progress | Priority |
|--------|------|--------|----------|----------|
| - | No epics yet | - | - | - |

---

## 🎯 Currently Active Epic

No active epic. Run `/create_epic "Epic Name"` to create your first epic.

---

## Epic History

None yet. This file will track epic progress as you create them.
```

### 9. Update Workspace Documentation

Update workspace root files:

**Add to `.claude/current_project.txt`**:
```
CURRENT_PROJECT={{project_name}}
```

**Optionally update root README.md** with new project entry if README exists.

### 10. Set as Active Project

Make the new project the active context:
- Write to `.claude/current_project.txt`: `CURRENT_PROJECT={{project_name}}`

### 11. Report to User

Provide comprehensive summary:

```
✅ Project Created: {{Display Name}}

📁 Location: {{full_path}}

📄 Files Created:
- ✅ Project structure from mod_template
- ✅ .claude/project.md (project vision and architecture)
- ✅ .claude/epics/CURRENT_EPIC.md (epic tracking)
- ✅ .claude/research/ (for research-agent)
- ✅ .claude/temp/ (for implementation-agent)
- ✅ .claude/game-design/ (for epic-agent)

⚙️  Configuration:
- Mod ID: {{mod_id}}
- Display Name: {{display_name}}
- Author: {{author}}
- Minecraft: 1.21.1 (Fabric)

📋 Next Steps:
1. Review and customize {{project}}/.claude/project.md
2. Run /create_epic "Your First Epic" to add features
3. Run ./gradlew build to verify project compiles

🎯 Active Project: {{project_name}} (set as current context)
```

## Important Rules

### Template Processing
- **Replace ALL placeholders**: No {{}} should remain
- **Update package structure**: Rename Java packages correctly
- **Validate files**: Ensure gradle.properties and fabric.mod.json are valid

### User Interaction
- **Ask for input**: Get author, description, vision, scope
- **Confirm details**: Show mod ID, display name before creating
- **Report thoroughly**: Clear summary of what was created

### Quality Checks
Before reporting success, verify:
- [ ] All placeholders replaced
- [ ] .claude/ structure complete
- [ ] project.md exists and is populated
- [ ] CURRENT_EPIC.md exists
- [ ] current_project.txt updated
- [ ] Folder naming follows conventions

### Project Activation
- **Always set as active**: New project becomes current context
- **Update tracking**: current_project.txt reflects new project
- **Ready to use**: User can immediately run /create_epic

## Error Handling

**If project exists**:
- Report: "Project {{name}} already exists"
- Ask: "Did you mean to work on existing project? Or choose different name?"

**If template missing**:
- Report: "mod_template not found"
- Suggest: "Create mod_template first or specify template location"

**If invalid name**:
- Report: "Project name should be kebab-case"
- Suggest: Corrected version
- Ask: "Use {{suggested_name}} instead?"

## Validation Checklist

Before completion:
- [ ] Project folder created from template
- [ ] All placeholders replaced in gradle.properties
- [ ] All placeholders replaced in fabric.mod.json
- [ ] Package structure renamed correctly
- [ ] .claude/ folder structure complete
- [ ] project.md created and populated
- [ ] CURRENT_EPIC.md initialized
- [ ] current_project.txt updated
- [ ] Project set as active context
- [ ] User can run /create_epic next

After successful creation, the user should be able to:
1. Run `./gradlew build` and have it compile
2. Run `/create_epic` to add their first epic
3. Start development immediately
