# Centralized Knowledge Base

## Purpose

This knowledge base centralizes **universal research** that applies across ALL mods in the workspace, eliminating duplicate investigations and providing tag-based discovery.

## Architecture

### Knowledge Base = WHAT things are
- API documentation (Fabric, libraries)
- System mechanics (Minecraft internals)
- Technical facts and findings
- Reusable patterns and implementations

### Skills = HOW to do things
- Workflows and processes
- Standards and conventions
- Agent instructions
- Development methodologies

## Structure

```
knowledge/
├── knowledge-index.json         # Master index with tags
├── WRITING_STANDARDS.md        # How to write knowledge files
├── MIGRATION_GUIDE.md          # How to migrate existing research
├── README.md                   # This file
│
├── minecraft/                  # Core Minecraft systems
│   ├── entities.md            # Entity system, AI, pathfinding
│   ├── rendering.md           # Client-side rendering
│   ├── world-generation.md    # World gen, chunks, structures
│   └── villagers.md           # Villager mechanics, trading
│
├── fabric-api/                # Fabric API specifics
│   ├── networking.md          # Packets, client-server
│   ├── events.md              # Event system, callbacks
│   ├── registry.md            # Registry system
│   └── mixins.md              # Mixin usage, injection
│
├── libraries/                 # Third-party libraries
│   ├── geckolib.md           # GeckoLib animations
│   ├── cloth-config.md       # Config screens
│   └── mod-menu.md           # Mod menu integration
│
├── graphics/                  # Visual systems
│   ├── textures.md           # Texture creation, formats
│   ├── animations.md         # Animation systems
│   ├── models.md             # 3D models, Blockbench
│   └── shaders.md            # Shaders, effects
│
└── performance/               # Optimization knowledge
    ├── profiling.md          # Profiling tools, techniques
    ├── threading.md          # Threading, async
    └── benchmarking.md       # Performance measurement
```

## Index Schema

`knowledge-index.json` contains entries with this structure:

```json
{
  "version": "1.0",
  "description": "Centralized knowledge base index",
  "entries": [
    {
      "path": "minecraft/entities.md",
      "title": "Minecraft Entity System",
      "tags": ["minecraft", "entities", "mobs", "ai", "pathfinding"],
      "date_updated": "2025-10-27",
      "summary": "Complete documentation of Minecraft's entity system",
      "related": ["fabric-api/registry.md", "graphics/animations.md"]
    }
  ]
}
```

### Fields:
- **path**: Relative path within knowledge/
- **title**: Human-readable title
- **tags**: Array of searchable keywords (lowercase)
- **date_updated**: Last modification date (YYYY-MM-DD)
- **summary**: 1-2 sentence description
- **related**: Array of related knowledge file paths

## Writing Standards

**CRITICAL**: All knowledge files must follow `.claude/knowledge/WRITING_STANDARDS.md`

Key standards include:
- Required frontmatter (version, tags, dates)
- Clear structure and organization
- Complete code examples
- Source attribution
- Quality checklist validation

See WRITING_STANDARDS.md for complete guidelines.

---

## Usage Workflow

### For research-agent

**ALWAYS start with knowledge base search:**

1. **Search existing knowledge**:
   ```
   Read knowledge-index.json
   Search for relevant tags
   Review existing knowledge files
   Identify what's already known
   ```

2. **Conduct new research** only for gaps

3. **Decide where to document**:
   - **Universal** (Fabric API, libraries, Minecraft systems) → `.claude/knowledge/`
   - **Project-specific** (design decisions, project context) → `<project>/.claude/research/`

4. **Update knowledge base**:
   - Create/update knowledge file in appropriate category
   - Add/update entry in `knowledge-index.json`
   - Add relevant tags for discoverability
   - Link related knowledge files

### For other agents

**Reference centralized knowledge:**

1. When encountering unknown systems, check knowledge base first
2. Reference knowledge files in implementation notes
3. Suggest knowledge base updates when discovering gaps

### For users

**Manually search when needed:**

```bash
# Search by tag (grep the index)
grep -i "geckolib" .claude/knowledge/knowledge-index.json

# Read specific knowledge
cat .claude/knowledge/libraries/geckolib.md
```

## Tag Guidelines

**Use consistent, lowercase tags:**

### Category Tags
- `minecraft` - Core Minecraft systems
- `fabric` - Fabric API features
- `library` - Third-party libraries
- `graphics` - Visual/rendering
- `performance` - Optimization

### System Tags
- `entities`, `blocks`, `items`, `villagers`
- `rendering`, `networking`, `events`
- `animations`, `textures`, `models`

### Technology Tags
- `geckolib`, `cloth-config`, `mod-menu`
- `mixins`, `registry`, `packets`
- `profiling`, `threading`, `benchmarking`

### Feature Tags
- `ai`, `pathfinding`, `trading`
- `gui`, `screens`, `config`
- `combat`, `inventory`, `world-gen`

**Principle**: Add 4-8 tags per entry, covering category + specific terms

## Migration Process

See `MIGRATION_GUIDE.md` for detailed migration instructions.

**Quick decision tree:**
- Does this apply to multiple mods? → Knowledge base
- Is this Minecraft/Fabric/library documentation? → Knowledge base
- Is this project-specific context? → Project research folder

## Benefits

- ✅ **No duplicate research** - Research once, use everywhere
- ✅ **Fast discovery** - Tag-based search instead of hundreds of skills
- ✅ **Clear separation** - Universal knowledge vs. project context
- ✅ **Easy updates** - One source of truth per topic
- ✅ **Scalable** - Can grow indefinitely without cluttering skills

## Examples

### Universal Knowledge → Knowledge Base
- How Fabric networking works
- GeckoLib animation setup
- Minecraft entity rendering pipeline
- Villager AI mechanics
- JVM profiling techniques

### Project-Specific → Project Research
- Why we chose X GUI design for THIS mod
- THIS mod's villager customizations
- Testing results for THIS mod's features
- THIS mod's configuration decisions

## Maintenance

**Keep knowledge current:**
- Update files when new versions change behavior
- Add new findings as research progresses
- Maintain links between related knowledge
- Archive outdated information with version notes

**Index hygiene:**
- Keep tags consistent
- Update date_updated on changes
- Maintain accurate summaries
- Verify related links aren't broken
