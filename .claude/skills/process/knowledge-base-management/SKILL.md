---
name: knowledge-base-management
description: Guidelines for creating, updating, and migrating research to the centralized knowledge base
allowed-tools: [Read, Write, Edit, Grep, Glob]
---

# Knowledge Base Management Skill

**Purpose**: Ensure all agents working with the knowledge base follow consistent standards for creating, updating, and organizing universal research.

**When this skill applies**: Any time you're working with `.claude/knowledge/` files, migrating research, or documenting universal findings.

---

## Quick Reference

### Before Working with Knowledge Base

**ALWAYS ask these questions**:
1. Is this knowledge **universal** (applies to multiple projects)?
   - YES → Knowledge base
   - NO → Project research folder
2. Does similar knowledge already exist?
   - Check `knowledge-index.json` first
   - Search by tags
   - Update existing instead of creating duplicate
3. Are you following WRITING_STANDARDS.md?
   - Required frontmatter
   - Proper structure
   - Quality checklist

---

## Part 1: Knowledge Base Structure

### Directory Organization

```
.claude/knowledge/
├── knowledge-index.json         # Master index (ALWAYS update)
├── WRITING_STANDARDS.md        # Content standards (this skill summarizes it)
├── MIGRATION_GUIDE.md          # Migration procedures
├── README.md                   # Overview
│
├── minecraft/                  # Core Minecraft systems
├── fabric-api/                # Fabric API specifics
├── libraries/                 # Third-party libraries
├── graphics/                  # Visual systems
└── performance/               # Optimization knowledge
```

### Universal vs Project-Specific Decision Tree

**Universal knowledge** → `.claude/knowledge/`
- Fabric API documentation
- Minecraft system mechanics
- Library usage guides (GeckoLib, Cloth Config, etc.)
- Performance profiling tools
- Porting patterns (Forge → Fabric)
- General best practices

**Project-specific** → `<project>/.claude/research/`
- Design decisions for THIS mod
- Project-unique implementations
- Testing results for THIS mod
- Configuration choices
- Implementation notes with project context

**Rule of thumb**: If 2+ projects would benefit → universal knowledge

---

## Part 2: Writing Standards (Summary)

**Full details**: Read `.claude/knowledge/WRITING_STANDARDS.md`

### Required File Structure

Every knowledge file must have:

```markdown
# [Clear Title]

**Last Updated**: YYYY-MM-DD
**Minecraft Version**: X.X.X (or range)
**Platform**: Fabric/Forge/Both/Agnostic
**Tags**: tag1, tag2, tag3, tag4

## Overview
[1-3 paragraph summary]

---

## [Main Content Sections]

---

## Version Compatibility
[Which versions this applies to]

## Primary Sources
[Citations and references]

## Related Knowledge
[Links to other knowledge files]
```

### Content Quality Standards

**Level of Detail** - "Goldilocks Principle":
- ❌ Too little: Vague, unusable
- ❌ Too much: 1000+ lines, exhaustive internals
- ✅ Just right: Public APIs, usage examples, key concepts (200-800 lines)

**Code Examples**:
- Must be complete and runnable
- Must include comments
- Show both wrong ❌ and right ✅ approaches when teaching

**Version Info**:
- Always specify Minecraft version
- Note Fabric/Forge/library versions if relevant
- Indicate version ranges where applicable

**Sources**:
- Cite primary sources (documentation, source code, research)
- Document research methodology
- Include confidence levels for findings

### Tag Guidelines

**Use 4-8 tags per entry**:
- 1-2 category tags: `minecraft`, `fabric`, `performance`, `graphics`
- 2-4 specific tags: `entities`, `profiling`, `animations`, `registry`
- 1-2 technology tags: `geckolib`, `spark`, `mixins`

**Format**: All lowercase, hyphenated for multi-word: `brain-system`, `world-generation`

### Quality Checklist

Before submitting knowledge:
- [ ] Universal (not project-specific)
- [ ] Factually accurate (verified sources)
- [ ] Sufficient detail to use the knowledge
- [ ] Complete code examples
- [ ] Required frontmatter present
- [ ] Proper structure (heading hierarchy)
- [ ] Consistent terminology
- [ ] Sources cited
- [ ] Entry added to knowledge-index.json
- [ ] Related knowledge linked

---

## Part 3: Knowledge Index Integration

### Always Update knowledge-index.json

**Every time you create/update knowledge**, update the index:

```json
{
  "path": "category/filename.md",
  "title": "Clear Descriptive Title",
  "tags": ["tag1", "tag2", "tag3", "tag4"],
  "date_updated": "YYYY-MM-DD",
  "summary": "One or two sentence description",
  "related": ["other/file.md", "another/file.md"]
}
```

### Index Update Workflow

1. **Create/update knowledge file** following WRITING_STANDARDS
2. **Add or update entry** in knowledge-index.json
3. **Verify JSON syntax** (no trailing commas, proper quotes)
4. **Test discoverability**: Can you find it by searching tags?

---

## Part 4: Migration Procedures

**Full details**: Read `.claude/knowledge/MIGRATION_GUIDE.md`

### When to Migrate Existing Research

**Scan project research folders** for universal knowledge:
```bash
# Check each project
ls <project>/.claude/research/
```

**Ask for each file**:
1. Does this apply to multiple mods? → Migrate
2. Is this Minecraft/Fabric/library documentation? → Migrate
3. Is this project-specific context? → Keep in project

### Migration Steps

**For each universal knowledge item**:

1. **Choose category**: minecraft/, fabric-api/, libraries/, graphics/, performance/
2. **Create knowledge file** (or merge with existing)
3. **Follow WRITING_STANDARDS.md**
4. **Update knowledge-index.json**
5. **Create project reference file**:

```markdown
# [Topic] Reference

**Universal knowledge migrated to centralized knowledge base.**

## Centralized Knowledge

See `.claude/knowledge/category/filename.md` for:
- [What it covers]
- [Key topics]

**Tags to search**: `tag1`, `tag2`, `tag3`

## Project-Specific Notes

[Project-specific context that stays here]
```

6. **Archive or remove** original project research file (optional)

---

## Part 5: Workflow Patterns

### Creating New Knowledge

**Workflow**:
1. **Search index first**: Does this knowledge already exist?
2. **Determine category**: Which folder does it belong in?
3. **Read WRITING_STANDARDS.md**: Refresh on requirements
4. **Create knowledge file**: Follow required structure
5. **Validate with checklist**: Ensure quality
6. **Update index**: Add entry with tags
7. **Link related knowledge**: Cross-reference

### Updating Existing Knowledge

**Workflow**:
1. **Read current knowledge file**
2. **Update content**: Add new information, fix errors
3. **Update "Last Updated" date**
4. **Add changelog note** (if significant change)
5. **Update index entry**: New date, revised summary if needed
6. **Verify related links**: Still accurate?

### Searching Knowledge Base

**Workflow**:
1. **Read knowledge-index.json**
2. **Search for tags**: Use grep or manual search
   ```bash
   grep -i "villagers" .claude/knowledge/knowledge-index.json
   ```
3. **Read matching knowledge files**
4. **Use information** or update if needed

---

## Part 6: Agent-Specific Guidance

### For research-agent

**Your workflow with knowledge base**:

1. **ALWAYS search knowledge base FIRST**:
   - Read knowledge-index.json
   - Search for relevant tags
   - Review existing knowledge

2. **Identify gaps**: What's missing or unclear?

3. **Conduct research** only for gaps

4. **Decide storage location**:
   - Universal → `.claude/knowledge/` + update index
   - Project-specific → `<project>/.claude/research/`

5. **Follow WRITING_STANDARDS.md** for knowledge files

6. **Update index** with proper tags

### For implementation-agent

**Your usage of knowledge base**:

1. **Check knowledge base** before implementing unfamiliar systems
2. **Reference knowledge files** in code comments when applicable
3. **Suggest knowledge updates** if you discover gaps during implementation

### For other agents

**Your usage of knowledge base**:

1. **Search knowledge base** for unknown systems/APIs
2. **Reference knowledge** instead of re-researching
3. **Suggest updates** when finding outdated information

---

## Part 7: Common Patterns

### Pattern: Creating Knowledge from Research

**Scenario**: You've researched something and need to document it.

**Steps**:
1. Determine if universal or project-specific
2. If universal:
   - Choose category folder
   - Create knowledge file with WRITING_STANDARDS structure
   - Update knowledge-index.json
3. If project-specific:
   - Create in `<project>/.claude/research/`
   - Reference centralized knowledge if applicable

### Pattern: Consolidating Duplicate Research

**Scenario**: Multiple projects have similar research.

**Steps**:
1. Identify common knowledge across projects
2. Create centralized knowledge file
3. Update knowledge-index.json
4. Create reference files in each project
5. Archive or remove project-specific duplicates

### Pattern: Deprecating Old Knowledge

**Scenario**: Knowledge becomes outdated (new Minecraft version, API changes).

**Steps**:
1. **Don't delete** - mark as deprecated
2. Update title: `# Old Approach (Deprecated in X.XX)`
3. Add deprecation notice at top
4. Link to current knowledge
5. Keep for historical reference
6. Update index with deprecation note

**Example**:
```markdown
# Old Entity Registration (Pre-1.19)

**Status**: ⚠️ DEPRECATED - Use for Minecraft 1.18 and earlier only

For Minecraft 1.19+, see `.claude/knowledge/fabric-api/registry.md`

[old content preserved]
```

---

## Part 8: Maintenance

### Regular Knowledge Base Health Checks

**Quarterly review**:
- [ ] Check for outdated version information
- [ ] Verify external links aren't broken
- [ ] Update deprecated APIs or patterns
- [ ] Consolidate overlapping knowledge
- [ ] Improve poorly-written knowledge
- [ ] **Check file sizes** - split files over 500 lines

### Knowledge File Size Management

**CRITICAL**: Keep knowledge files concise and focused.

**Size Guidelines**:
- **Target**: 200-400 lines per file
- **Maximum**: 500 lines (split if exceeded)
- **Exception**: Up to 800 lines if truly cohesive topic

**Why file size matters**:
- Easier to find specific information
- Faster to load and read
- Better for context-limited searches
- Clearer topic separation
- More maintainable

### When to Split Files

**Split immediately if**:
- File exceeds 500 lines
- File covers multiple distinct subtopics
- File has "Part 1, Part 2, Part 3" structure
- Content is loosely related rather than tightly integrated

**Keep together if**:
- Topics are tightly integrated and cross-reference constantly
- Splitting would create excessive cross-referencing
- Total size is under 500 lines

### How to Split Knowledge Files

**Workflow for research-agent**:

#### Step 1: Analyze Current File

Read the file and identify:
- Natural topic boundaries (current "Parts" or major sections)
- Size of each topic
- Cross-references between topics
- Potential file names

**Example analysis**:
```
minecraft/villagers.md (297 lines)
- Part 1: Profession Textures (~90 lines)
- Part 2: Brain & Bed Claiming (~200 lines)

Decision: Split into 2 files
→ villagers-professions.md (profession system, textures)
→ villagers-brain-ai.md (brain system, bed claiming, POI)
```

#### Step 2: Plan File Structure

Determine new file names following pattern: `topic-subtopic.md`

**Naming patterns**:
```
villagers.md → villagers-professions.md, villagers-brain-ai.md
entities.md → entities-registration.md, entities-ai.md, entities-rendering.md
porting.md → porting-entities.md, porting-items.md, porting-networking.md
```

**Guidelines**:
- Use descriptive subtopic names
- Keep consistent with existing naming
- Use hyphens for multi-word names

#### Step 3: Create New Files

For each new file:

1. **Copy relevant content** from original
2. **Add proper frontmatter**:
   ```markdown
   # [Topic - Subtopic]

   **Minecraft Version**: X.X.X
   **Last Updated**: YYYY-MM-DD
   **Tags**: [same category tags], [subtopic-specific tags]

   ## Overview
   [What this specific file covers]
   ```

3. **Add cross-references** to other split files:
   ```markdown
   ## Related Knowledge

   **Other villager topics**:
   - `.claude/knowledge/minecraft/villagers-professions.md` - Profession textures
   - `.claude/knowledge/minecraft/villagers-brain-ai.md` - Brain AI and bed claiming
   ```

4. **Update internal links** if content references other parts

#### Step 4: Update Knowledge Index

For **each new file**, add entry to `knowledge-index.json`:

```json
{
  "path": "minecraft/villagers-professions.md",
  "title": "Villager Profession System",
  "tags": ["minecraft", "villagers", "professions", "textures"],
  "date_updated": "YYYY-MM-DD",
  "summary": "Villager profession registration and texture system",
  "related": ["minecraft/villagers-brain-ai.md"]
}
```

**Remove** old entry for original file.

#### Step 5: Update Project References

Check if project research files reference the old file:

```bash
grep -r "minecraft/villagers.md" */\.claude/research/
```

Update references to point to appropriate new file.

#### Step 6: Delete Original File

Once all new files are created and indexed:
```bash
rm .claude/knowledge/minecraft/villagers.md
```

### File Split Decision Matrix

| File Size | Distinct Topics | Action |
|-----------|----------------|--------|
| < 300 lines | Any | ✅ Keep as-is |
| 300-500 lines | 1 topic | ✅ Keep as-is |
| 300-500 lines | 2-3 topics | ⚠️ Consider splitting if topics are distinct |
| 500-800 lines | 1 topic | ⚠️ Consider splitting if natural sections exist |
| 500-800 lines | 2+ topics | ❌ **Must split** |
| > 800 lines | Any | ❌ **Must split** immediately |

### Example Split Workflows

#### Example 1: villagers.md (297 lines, 2 topics)

**Current structure**:
- Part 1: Profession Textures (~90 lines)
- Part 2: Brain AI & Bed Claiming (~200 lines)

**Decision**: Split into 2 files (topics are distinct)

**New files**:
1. `villagers-professions.md` - Profession system, texture loading, registration
2. `villagers-brain-ai.md` - Brain system, memory, bed claiming, POI

**Tags**:
- `villagers-professions.md`: minecraft, villagers, professions, textures, registry
- `villagers-brain-ai.md`: minecraft, villagers, ai, brain-system, pathfinding, beds, poi

#### Example 2: porting-forge-to-fabric.md (646 lines, many topics)

**Current structure**:
- Architectural differences
- Entity registration
- Animation systems (Citadel → GeckoLib)
- AI goals
- Items & blocks
- Networking
- Configuration
- Common issues

**Decision**: Split into 4 files (topics are distinct and large)

**New files**:
1. `porting-overview.md` - Architecture, mod loading, entry points
2. `porting-entities.md` - Entity registration, attributes, rendering, AI
3. `porting-animations.md` - Citadel to GeckoLib migration, model conversion
4. `porting-items-blocks.md` - Item/block registration, networking, config

#### Example 3: profiling.md (378 lines, 1 cohesive topic)

**Current structure**:
- Tool comparison
- Metrics capabilities
- Integration
- Workflows

**Decision**: Keep as-is (under 500 lines, cohesive topic, all sections related)

### Splitting Checklist

When splitting a knowledge file:

- [ ] File exceeds 500 lines or has distinct subtopics
- [ ] New file names follow `topic-subtopic.md` pattern
- [ ] Each new file has complete frontmatter
- [ ] Each new file has Overview section
- [ ] Cross-references added between related files
- [ ] All new files added to knowledge-index.json
- [ ] Old entry removed from knowledge-index.json
- [ ] Project research references updated
- [ ] Original file deleted
- [ ] No broken internal links

---

## Part 9: Quality Assurance

### Before Committing Knowledge

**Run through checklist**:
- [ ] Frontmatter complete (version, tags, date)
- [ ] Overview section clear and concise
- [ ] Code examples are complete and tested
- [ ] Sources cited
- [ ] Related knowledge linked
- [ ] Index updated
- [ ] Tags discoverable (4-8 tags)
- [ ] No spelling/grammar errors
- [ ] Consistent terminology
- [ ] Proper markdown formatting

### Knowledge Review Criteria

**Good knowledge file characteristics**:
- ✅ Universal and reusable
- ✅ Well-structured with clear hierarchy
- ✅ Complete code examples
- ✅ Properly tagged and indexed
- ✅ Version-specific and dated
- ✅ Well-sourced
- ✅ Practical and usable

**Poor knowledge file characteristics**:
- ❌ Project-specific context mixed in
- ❌ Incomplete or broken examples
- ❌ No version information
- ❌ No sources cited
- ❌ Poorly organized
- ❌ Too vague or too detailed

---

## Part 10: Integration with Existing Docs

### Relationship to Other Documentation

**Knowledge Base** (`.claude/knowledge/`):
- Universal research
- Cross-project facts
- API documentation
- System mechanics

**Skills** (`.claude/skills/`):
- HOW to do things
- Workflows and processes
- Standards and conventions
- This skill is an example!

**Project Research** (`<project>/.claude/research/`):
- Project-specific findings
- Design decisions
- Implementation notes
- References to centralized knowledge

### When to Use Each

**Use Knowledge Base** when:
- Documenting Minecraft/Fabric systems
- Explaining third-party libraries
- Recording universal patterns
- Creating reusable references

**Use Skills** when:
- Defining workflows
- Establishing processes
- Setting standards
- Guiding agent behavior

**Use Project Research** when:
- Recording project decisions
- Documenting project-specific implementations
- Noting project testing results
- Storing project context

---

## Summary Checklist

**When working with knowledge base**:

1. ✅ Search `knowledge-index.json` FIRST
2. ✅ Determine if knowledge is universal or project-specific
3. ✅ Follow WRITING_STANDARDS.md for structure
4. ✅ Include required frontmatter (version, tags, date)
5. ✅ Provide complete, runnable code examples
6. ✅ Cite sources and research methodology
7. ✅ Update knowledge-index.json with entry
8. ✅ Add 4-8 discoverable tags
9. ✅ Link related knowledge files
10. ✅ Validate with quality checklist

**Remember**: Knowledge base is for **WHAT things are**, Skills are for **HOW to do things**.

---

## Quick Links

- Full Writing Standards: `.claude/knowledge/WRITING_STANDARDS.md`
- Migration Guide: `.claude/knowledge/MIGRATION_GUIDE.md`
- Knowledge Base README: `.claude/knowledge/README.md`
- Knowledge Index: `.claude/knowledge/knowledge-index.json`
