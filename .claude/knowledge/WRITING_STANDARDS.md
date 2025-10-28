# Knowledge Base Writing Standards

**Purpose**: Ensure consistency, quality, and usability of all knowledge base files.

**Audience**: research-agent, other agents, and users creating/updating knowledge files.

---

## Core Principles

1. **Universal, Not Project-Specific** - Knowledge must apply across multiple projects
2. **Factual, Not Opiniated** - Document what IS, not what SHOULD BE (unless best practices)
3. **Reproducible** - Include enough detail for others to apply the knowledge
4. **Searchable** - Use clear headings, consistent terminology, proper tags
5. **Maintainable** - Include version info, dates, sources for future updates

---

## File Structure

### Required Frontmatter

Every knowledge file must start with:

```markdown
# [Clear, Descriptive Title]

**Last Updated**: YYYY-MM-DD
**Minecraft Version**: X.X.X (or version range)
**Platform**: Fabric / Forge / Both / Platform-agnostic
**Tags**: tag1, tag2, tag3, tag4 (match index tags)

## Overview

[1-3 paragraph summary of what this knowledge covers and why it's useful]

---
```

### Required Sections

**Minimum sections** (adapt as needed):

1. **Overview** - What this knowledge covers (required)
2. **Main Content** - Core information (multiple sections as needed)
3. **Version Compatibility** - Which versions this applies to
4. **Primary Sources** - Where information came from
5. **Related Knowledge** - Links to other knowledge files

### Optional Sections (Use When Relevant)

- **Common Issues & Solutions** - Troubleshooting guide
- **Examples** - Code examples, use cases
- **Best Practices** - Recommended approaches
- **Performance Considerations** - Optimization notes
- **Security Considerations** - Security implications
- **Migration Guide** - How to update between versions
- **API Reference** - Method signatures, parameters
- **Testing** - How to verify/test the knowledge
- **Debugging** - How to debug issues

---

## Content Standards

### Level of Detail

**Goldilocks Principle**: Not too little, not too much, just right.

**Too Little** ❌:
```markdown
## Villagers
Villagers have AI and can claim beds.
```

**Too Much** ❌:
```markdown
## Villagers
[50 pages of exhaustive documentation including every internal detail,
private methods, implementation specifics that change between versions]
```

**Just Right** ✅:
```markdown
## Villager Bed Claiming

Villagers claim beds through brain memory system (MemoryModuleType.HOME).

### Claiming Requirements
1. Within 48-block radius
2. Actively pathfinding
3. Bed not already claimed

### Preventing Claiming
Override wantsToSleep() to return false:
[code example]
```

**Guidelines**:
- Include enough detail to **use** the knowledge
- Focus on **public APIs** and **documented behavior**
- Mention internal details **only if necessary** for understanding
- Provide **working examples** for complex concepts
- Link to **external docs** for exhaustive details

### Structure & Organization

**Use clear hierarchy**:

```markdown
## Part 1: Main Topic
### Subsection
#### Detail Level
```

**Maximum 4 heading levels**: `#`, `##`, `###`, `####`

**Break into parts** if knowledge is large:
```markdown
## Part 1: Basic Concepts
## Part 2: Advanced Usage
## Part 3: Troubleshooting
```

**Use horizontal rules** to separate major sections:
```markdown
---
```

### Code Examples

**Always include**:
- Language identifier in code blocks
- Comments explaining non-obvious code
- Complete, runnable examples (not fragments)
- Both "wrong" and "right" examples when teaching patterns

**Format**:
```markdown
### Example: Entity Registration

**Forge**:
```java
// Forge deferred registration
public static final DeferredRegister<EntityType<?>> ENTITIES =
    DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
\```

**Fabric**:
```java
// Fabric direct registration
public static final EntityType<CustomEntity> CUSTOM = Registry.register(
    Registries.ENTITY_TYPE,
    Identifier.of("modid", "custom"),
    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CustomEntity::new)
        .dimensions(EntityDimensions.fixed(1.0f, 2.0f))
        .build()
);
\```
```

### Comparison Tables

**Use tables** for comparing options:

```markdown
| Tool | CPU Overhead | Memory Overhead | Production Safe |
|------|--------------|-----------------|-----------------|
| Spark | <1% | <50MB | ✅ Yes |
| JProfiler | 2-5% | 100-500MB | ⚠️ Limited |
```

**Guidelines**:
- Use checkmarks (✅), warnings (⚠️), crosses (❌) for quick scanning
- Keep table cells concise
- Align columns for readability

### Lists & Bullets

**Use ordered lists** for sequential steps:
```markdown
1. First step
2. Second step
3. Third step
```

**Use unordered lists** for non-sequential items:
```markdown
- Requirement A
- Requirement B
- Requirement C
```

**Use checkboxes** for checklists:
```markdown
- [ ] Task not done
- [x] Task completed
```

---

## Writing Style

### Tone

- **Technical but Accessible** - Explain concepts clearly without oversimplifying
- **Authoritative** - State facts confidently (based on verified sources)
- **Neutral** - Avoid bias, present options objectively
- **Practical** - Focus on what developers need to know

### Voice

- **Active voice preferred**: "The system loads textures" > "Textures are loaded by the system"
- **Second person for instructions**: "You can override this method" (not "One can override")
- **Third person for descriptions**: "Villagers claim beds" (not "Villagers will claim beds")

### Terminology

**Use consistent terms**:
- ✅ "Minecraft 1.21.1" (not "MC 1.21.1" or "Minecraft version 1.21.1")
- ✅ "Fabric API" (not "Fabric" when referring to the API specifically)
- ✅ "brain AI system" (not "brain system", "AI system", "brain-AI")
- ✅ "method" (not "function" for Java methods)
- ✅ "package" (not "namespace" for Java packages)

**Define acronyms** on first use:
```markdown
Point of Interest (POI) system
```

### Formatting

**Bold** for emphasis:
```markdown
**Important**: This will overwrite existing data.
```

**Italic** for terms/references:
```markdown
The *VillagerEntity* class handles...
```

**Code** for identifiers:
```markdown
The `wantsToSleep()` method returns...
```

**Blockquotes** for important notes:
```markdown
> **Note**: This behavior changed in Minecraft 1.21.
```

---

## Version Management

### Version Specificity

**Always specify**:
- Minecraft version(s) the knowledge applies to
- Fabric/Forge version if relevant
- Library versions if applicable

**Format**:
```markdown
**Minecraft Version**: 1.21.1
**Fabric API**: 0.100.0+
**GeckoLib**: 4.5.8
```

**Version ranges**:
```markdown
**Minecraft Version**: 1.14 - 1.21.1 (mechanics unchanged)
```

### Deprecation

**Mark deprecated information**:
```markdown
## Old Approach (Deprecated in 1.21)

~~Use VillagerProfessionBuilder for registration~~

**Note**: VillagerProfessionBuilder is deprecated in Minecraft 1.21.1.
Use direct VillagerProfession constructor instead.

## Current Approach (1.21+)

Use direct registration:
[code example]
```

### Updates

**When updating knowledge**:
1. Change "Last Updated" date
2. Add note about what changed (if significant)
3. Update related entries in knowledge-index.json
4. Keep deprecated information with clear warnings

**Update note format**:
```markdown
**Last Updated**: 2025-10-27

**Changelog**:
- 2025-10-27: Added GeckoLib 4.6 support
- 2025-09-15: Updated for Minecraft 1.21.1
- 2025-08-01: Initial creation
```

---

## Source Attribution

### Required Citations

**Always cite sources**:

```markdown
## Primary Sources

- [Fabric Wiki - Entity Registration](https://fabricmc.net/wiki/tutorial:entity)
- Yarn Mappings (FabricMC): https://maven.fabricmc.net/docs/
- Minecraft Source Code (decompiled via Fabric Loom)
```

**For research findings**:
```markdown
## Research Methodology

- Analyzed Minecraft 1.21.1 source code (Yarn mappings)
- Tested behavior in development environment
- Confirmed with Fabric Wiki documentation
- Cross-referenced with community findings
```

### Confidence Levels

**Indicate certainty** for research findings:

```markdown
**Confidence Level**: High (verified in source code)
**Confidence Level**: Medium (observed behavior, not confirmed in source)
**Confidence Level**: Low (community reports, not verified)
```

---

## Knowledge Index Integration

### When Adding Knowledge

**Update knowledge-index.json**:

```json
{
  "path": "category/filename.md",
  "title": "Clear Descriptive Title",
  "tags": ["tag1", "tag2", "tag3", "tag4"],
  "date_updated": "YYYY-MM-DD",
  "summary": "One or two sentence summary of what this covers",
  "related": ["other/file.md", "another/file.md"]
}
```

### Tag Guidelines

**Use 4-8 tags per entry**:
- 1-2 **category tags** (minecraft, fabric, performance, etc.)
- 2-4 **specific tags** (entities, profiling, animations, etc.)
- 1-2 **technology tags** (geckolib, spark, mixins, etc.)

**Tag format**:
- All lowercase
- Hyphenated for multi-word tags: `brain-system`, `world-generation`
- Consistent across knowledge base

**Common tags**:
- **Categories**: `minecraft`, `fabric`, `forge`, `performance`, `graphics`
- **Systems**: `entities`, `blocks`, `items`, `villagers`, `rendering`
- **Tools**: `spark`, `geckolib`, `mixins`, `gradle`
- **Concepts**: `profiling`, `optimization`, `animation`, `registry`, `networking`

### Related Knowledge Links

**Link related knowledge files**:

```markdown
## Related Knowledge

- `.claude/knowledge/fabric-api/registry.md` - Fabric registration patterns
- `.claude/knowledge/minecraft/entities.md` - General entity systems
- `.claude/knowledge/graphics/animations.md` - Animation libraries
```

**Principles**:
- Use relative paths from knowledge base root
- Link to knowledge that provides context or related info
- Maximum 5 related links (most relevant only)

---

## Quality Checklist

**Before submitting knowledge file**:

### Content Quality
- [ ] Applies to multiple projects (not project-specific)
- [ ] Factually accurate (verified from sources)
- [ ] Sufficient detail to apply the knowledge
- [ ] Code examples are complete and runnable
- [ ] No outdated or deprecated information (or clearly marked)

### Structure Quality
- [ ] Required frontmatter present
- [ ] Clear heading hierarchy (max 4 levels)
- [ ] Proper sections (Overview, Version Compatibility, Sources, Related)
- [ ] Logical organization and flow

### Writing Quality
- [ ] Technical but accessible tone
- [ ] Active voice where appropriate
- [ ] Consistent terminology
- [ ] Proper formatting (bold, italic, code, blockquotes)
- [ ] No spelling or grammar errors

### Index Integration
- [ ] Entry added to knowledge-index.json
- [ ] 4-8 relevant tags included
- [ ] Accurate summary (1-2 sentences)
- [ ] Related knowledge files linked

### Maintenance Info
- [ ] Last Updated date present
- [ ] Version specificity clear
- [ ] Primary sources cited
- [ ] Confidence level noted (for research)

---

## File Size Guidelines

**CRITICAL**: Keep knowledge files concise and focused.

**Size Limits**:
- **Target**: 200-400 lines per file
- **Maximum**: 500 lines (split if exceeded)
- **Exception**: Up to 800 lines ONLY if truly cohesive single topic

**Why file size matters**:
- Easier to find specific information
- Faster to load and read
- Better for context-limited searches
- Clearer topic separation
- More maintainable

**When to split immediately**:
- ❌ File exceeds 500 lines
- ❌ File covers multiple distinct subtopics
- ❌ File has "Part 1, Part 2, Part 3" structure
- ❌ Content is loosely related rather than tightly integrated

**Split naming pattern**: `topic-subtopic.md`

**Example splits**:
```
villagers.md (297 lines, 2 topics)
→ villagers-professions.md (profession system, textures)
→ villagers-brain-ai.md (brain AI, bed claiming, POI)

porting-forge-to-fabric.md (646 lines, many topics)
→ porting-overview.md (architecture, mod loading)
→ porting-entities.md (entity registration, attributes, AI)
→ porting-animations.md (Citadel to GeckoLib)
→ porting-items-blocks.md (items, blocks, networking)
```

**See knowledge-base-management skill** for complete splitting workflow.

---

## Examples

### Good Knowledge File Structure ✅

```markdown
# Minecraft Entity Registration

**Last Updated**: 2025-10-27
**Minecraft Version**: 1.21.1
**Platform**: Fabric
**Tags**: minecraft, fabric, entities, registry

## Overview

Complete guide to registering custom entities in Fabric, including entity types,
attributes, and client-side rendering.

---

## Part 1: Basic Entity Registration

### Entity Type Registration

Register entity type using FabricEntityTypeBuilder:

[code example]

### Attribute Registration

Register entity attributes:

[code example]

---

## Part 2: Client-Side Registration

[content]

---

## Version Compatibility

- Minecraft 1.21.1 (current patterns)
- Fabric API 0.100.0+
- Pattern stable since Fabric 1.19+

## Primary Sources

- Fabric Wiki: https://fabricmc.net/wiki/tutorial:entity
- Yarn Mappings: https://maven.fabricmc.net/docs/

## Related Knowledge

- `.claude/knowledge/fabric-api/networking.md` - Entity networking
- `.claude/knowledge/minecraft/entities.md` - Entity behavior
```

### Poor Knowledge File Structure ❌

```markdown
# Entities

entities are things in minecraft

## how to register

use registry

## code

code goes here

## end
```

**Problems**:
- No frontmatter (version, tags, dates)
- Too vague and incomplete
- Poor structure and organization
- No sources or related knowledge
- Not useful as reference

---

## Common Mistakes to Avoid

### ❌ Project-Specific Content in Knowledge Base

**Wrong**:
```markdown
# Guard Villager Implementation

Our guard villagers have these settings:
- Health: 30
- Attack: 5
```

**Right** (in project research):
```markdown
# Guard Villager Settings (Project-Specific)

See `.claude/knowledge/minecraft/villagers.md` for villager system.

Our guard configuration:
- Health: 30
- Attack: 5
```

### ❌ Opinion as Fact

**Wrong**:
```markdown
Spark is the best profiler and you should always use it.
```

**Right**:
```markdown
Spark is recommended for Minecraft profiling due to minimal overhead (<1%)
and Minecraft-specific features. See comparison table for alternatives.
```

### ❌ Incomplete Code Examples

**Wrong**:
```java
// Register entity
Registry.register(...)
```

**Right**:
```java
// Register custom entity
public static final EntityType<CustomEntity> CUSTOM = Registry.register(
    Registries.ENTITY_TYPE,
    Identifier.of("modid", "custom"),
    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CustomEntity::new)
        .dimensions(EntityDimensions.fixed(1.0f, 2.0f))
        .build()
);
```

### ❌ Missing Version Info

**Wrong**:
```markdown
# Entity Registration

Use FabricEntityTypeBuilder.
```

**Right**:
```markdown
# Entity Registration

**Minecraft Version**: 1.21.1
**Fabric API**: 0.100.0+

Use FabricEntityTypeBuilder for entity registration in Fabric 1.19+.
```

### ❌ No Sources

**Wrong**:
```markdown
Villagers claim beds through brain system.
```

**Right**:
```markdown
Villagers claim beds through brain system (MemoryModuleType.HOME).

**Primary Sources**:
- Minecraft source code (Yarn mappings)
- Tested in Minecraft 1.21.1 development environment
```

---

## Maintenance

### Regular Updates

**Check knowledge base quarterly** for:
- Outdated version information
- Deprecated APIs or patterns
- New Minecraft/Fabric releases
- Broken external links

### Archiving Outdated Knowledge

**When knowledge becomes obsolete**:
1. Don't delete - mark as deprecated
2. Indicate which version it applies to
3. Link to current knowledge
4. Keep for historical reference

**Example**:
```markdown
# Old Entity Registration (Pre-1.19)

**Status**: ⚠️ DEPRECATED - Use for Minecraft 1.18 and earlier only

For Minecraft 1.19+, see `.claude/knowledge/fabric-api/registry.md`

[old content preserved]
```

---

## Summary

**Good knowledge files are**:
1. ✅ Universal (not project-specific)
2. ✅ Well-structured (clear hierarchy)
3. ✅ Properly tagged (searchable)
4. ✅ Version-specific (clear applicability)
5. ✅ Well-sourced (citable)
6. ✅ Practical (usable examples)
7. ✅ Maintained (updated regularly)

**Use this guide** when creating or updating knowledge base files to ensure consistency and quality across all knowledge.
