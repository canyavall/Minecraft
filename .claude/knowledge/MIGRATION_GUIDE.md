# Knowledge Base Migration Guide

## Purpose

This guide helps migrate existing research from project-specific folders to the centralized knowledge base.

## Migration Decision Tree

For each research file, ask:

### Is this knowledge UNIVERSAL?
- ✅ YES if it applies to ALL mods (Fabric API, Minecraft internals, libraries)
- ❌ NO if it's specific to ONE project (design decisions, project context)

### Universal Knowledge → `.claude/knowledge/`
Examples:
- How Fabric networking works
- GeckoLib animation setup
- Minecraft entity rendering pipeline
- Villager trading mechanics
- Performance profiling techniques

### Project-Specific → `<project>/.claude/research/`
Examples:
- Why we chose X architecture for THIS mod
- THIS mod's villager GUI implementation notes
- Testing results for THIS mod's features
- THIS mod's compatibility decisions

## Migration Process

### 1. Identify Universal Knowledge

Scan project research folders:
```bash
# Check each project
ls xeena-village-manager/.claude/research/
ls xeenaa-fabric-enhancements/.claude/research/
ls xeenaa-structure-manager/.claude/research/
```

Look for research that:
- Documents Minecraft/Fabric systems
- Explains third-party libraries
- Contains reusable patterns
- Has no project-specific context

### 2. Choose Category

Map to knowledge base categories:

| If research covers... | Place in... |
|----------------------|-------------|
| Minecraft core systems | `minecraft/` |
| Fabric API features | `fabric-api/` |
| Third-party libraries | `libraries/` |
| Graphics/rendering | `graphics/` |
| Performance/optimization | `performance/` |

### 3. Migrate File

```bash
# Example: Moving GeckoLib research
# From: xeena-village-manager/.claude/research/geckolib-setup.md
# To: .claude/knowledge/libraries/geckolib.md

# If file exists, merge content
# If new, copy and adapt
```

### 4. Update Index

Add entry to `.claude/knowledge/knowledge-index.json`:

```json
{
  "path": "libraries/geckolib.md",
  "title": "GeckoLib Animation Library",
  "tags": ["geckolib", "animations", "models", "blockbench"],
  "date_updated": "2025-10-27",
  "summary": "Complete guide to GeckoLib animation system for Minecraft 1.21.1",
  "related": ["graphics/animations.md", "minecraft/entities.md"]
}
```

### 5. Leave Project-Specific Notes

If research had project context, create minimal project file:

`xeena-village-manager/.claude/research/villager-animations.md`:
```markdown
# Villager Animations

See centralized GeckoLib knowledge: `.claude/knowledge/libraries/geckolib.md`

## Project-Specific Notes
- We use idle, walk, and combat animations
- Animation controller in VillagerGuardEntity.java:123
- Custom animation: "salute" for rank changes
```

## Example Migrations

### Example 1: Fabric Networking

**Original**: `xeena-village-manager/.claude/research/networking-setup.md`
- Contains: How Fabric networking works, packet registration, client-server communication
- **Decision**: Universal → migrate to `fabric-api/networking.md`
- **Reason**: Applies to ALL mods using Fabric networking

**Project file remains**: Project-specific packet list and implementation notes

### Example 2: Villager AI Research

**Original**: `xeena-village-manager/.claude/research/villager-behavior.md`
- Contains: Mix of vanilla villager mechanics + project guard behavior
- **Decision**: Split!
  - Vanilla mechanics → `minecraft/villagers.md`
  - Guard-specific → stays in project research

### Example 3: Performance Profiling

**Original**: `xeenaa-fabric-enhancements/.claude/research/profiling-methods.md`
- Contains: How to use JVM profilers, interpret results, find bottlenecks
- **Decision**: Universal → migrate to `performance/profiling.md`
- **Reason**: Profiling techniques apply to ALL projects

**Project file remains**: This project's specific profiling results

## Batch Migration Command

```bash
# Create all category directories
mkdir -p .claude/knowledge/{minecraft,fabric-api,libraries,graphics,performance}

# Then manually review and migrate universal content
# NO automated script - requires human judgment
```

## Ongoing Workflow

### When conducting new research:

**BEFORE creating research file, decide:**

1. **Check existing knowledge**: Search `knowledge-index.json` for related topics
2. **Is this universal?**
   - YES → Create/update in `.claude/knowledge/` + update index
   - NO → Create in `<project>/.claude/research/`
3. **Reference centralized knowledge**: Link to knowledge base from project files

### When reading project research:

**If you find universal content in project research:**
1. Extract it to appropriate `.claude/knowledge/` category
2. Update `knowledge-index.json`
3. Replace project content with reference + project-specific notes

## Benefits

- ✅ Research once, use everywhere
- ✅ No duplicate investigations across projects
- ✅ Tag-based discovery (no hundreds of skills)
- ✅ Clear separation: knowledge vs. project context
- ✅ Easy to keep up-to-date (one source of truth)

## Questions?

If unsure whether knowledge is universal:
- Would another mod benefit from this?
- Does it document a system/API/library?
- Is it reusable without project context?

**If YES to any → migrate to knowledge base**
