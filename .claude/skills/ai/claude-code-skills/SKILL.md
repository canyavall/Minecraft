---
name: claude-code-skills
description: Comprehensive guide for creating, organizing, and maintaining Claude Code skills with proper structure, categorization, and integration patterns.
category: ai
tags: [ai, claude-code, skills, knowledge, configuration]
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Claude Code Skills

Comprehensive guide for creating and maintaining Claude Code skills as reusable knowledge packages with proper structure and auto-loading behavior.

## Purpose

This skill provides patterns and best practices for:
- Creating new skills with proper frontmatter
- Organizing skills by category
- Keeping skills concise and maintainable
- Integrating skills with agents and commands
- Debugging skill loading issues
- Managing skill dependencies

## When to Use

- Creating a new skill for reusable knowledge
- Refactoring large agent prompts into skills
- Fixing skill loading or integration issues
- Organizing knowledge across multiple skills
- Understanding skill auto-loading behavior

## Skills Architecture Principles

### 1. Auto-Loading via Frontmatter

**Skills load automatically based on frontmatter metadata**:

```yaml
---
name: skill-name
description: Clear description of what this skill covers
category: minecraft|java|process|ai
tags: [relevant, keywords, for, matching]
allowed-tools: [Read, Write, Edit, Grep, Glob]
---
```

**Why**: Claude Code reads frontmatter to determine when to load skills. No manual "loading" needed.

**Key Fields**:
- `name`: Unique identifier (lowercase-with-hyphens)
- `description`: One-line summary (used for matching)
- `category`: Organizing folder (optional but recommended)
- `tags`: Keywords for relevance matching
- `allowed-tools`: Tools skill can use (optional, for tool-using skills)

### 2. Concise Core with Optional Details

**Keep SKILL.md under 400 lines**:

```
.claude/skills/category/skill-name/
├── SKILL.md              # Core patterns (< 400 lines)
├── examples/             # Detailed examples (optional)
│   ├── pattern-1.md
│   ├── pattern-2.md
│   └── advanced.md
└── templates/            # Templates (optional)
    └── template.md
```

**Why**: Keeps context size manageable, loads faster, easier to maintain.

**Pattern**:
```markdown
# SKILL.md (core)

## Pattern Overview
[Quick reference with key points]

**See `examples/detailed-pattern.md` for complete examples**

## Common Anti-Patterns
[What NOT to do]
```

### 3. Single Responsibility Per Skill

**Each skill covers ONE knowledge domain**:

```markdown
# ✅ CORRECT: coding-standards
- Naming conventions
- Code organization
- Documentation standards
(All related to code style)

# ❌ WRONG: development-guide
- Naming conventions
- Git workflows
- Performance testing
- UI design
(Too broad, split into multiple skills)
```

**Why**: Makes skills easier to find, maintain, and update. Prevents duplicate knowledge.

## Skill File Structure

### Complete Template

```markdown
---
name: skill-name
description: One-line description of skill's knowledge domain
category: minecraft|java|process|ai
tags: [keyword1, keyword2, keyword3]
allowed-tools: [Read, Write, Edit, Grep, Glob]
---

# Skill Title

Brief description of what this skill covers and when to use it.

## Purpose

- What knowledge this provides
- What problems it solves
- What patterns it documents

## When to Use

- Use case 1
- Use case 2
- Use case 3

## [Core Content Section 1]

### Pattern Name

**Context**: When to use this pattern

**Implementation**:
```language
code example
```

**Why**: Explanation of reasoning

**Common Mistakes**:
- ❌ Mistake 1
- ❌ Mistake 2

**See `examples/detailed-pattern-name.md` for advanced usage**

## [Core Content Section 2]

[Similar structure]

## Best Practices

**✅ DO**:
- Guideline 1 with reasoning
- Guideline 2 with reasoning

**❌ DON'T**:
- Anti-pattern 1 with reasoning
- Anti-pattern 2 with reasoning

## Quick Reference

[Condensed cheat-sheet format for common patterns]

## Related Skills

- `related-skill-1` - What it covers
- `related-skill-2` - What it covers

## References

- External documentation
- Relevant files in codebase
- Examples directory
```

## Skill Categories

Organize skills by domain:

### minecraft/

**Minecraft-specific knowledge**:
- `minecraft-modding/` - Core Minecraft systems (loader-agnostic)
- `fabric-modding/` - Fabric API specific patterns
- `minecraft-performance/` - Performance optimization
- `structure-classification/` - Structure filtering strategies
- `ui-ux-design/` - GUI design for Minecraft

**When to create here**: Knowledge specific to Minecraft game systems

### java/

**Java language and best practices**:
- `java-development/` - Java 21 features and patterns
- `coding-standards/` - Naming, organization, documentation
- `defensive-programming/` - Testing and regression prevention
- `logging-strategy/` - Logging best practices
- `performance-testing/` - Benchmarking and profiling

**When to create here**: General Java knowledge, not Minecraft-specific

### process/

**Workflow and methodology**:
- `research-methodology/` - How to research unknowns
- `epic-requirements/` - requirements.md creation
- `task-planning/` - tasks.md creation

**When to create here**: Process guidance, workflow patterns

### ai/

**Claude Code infrastructure**:
- `claude-code-setup/` - General AI infrastructure
- `claude-code-agents/` - Agent creation and maintenance
- `claude-code-skills/` - Skills creation (this skill)
- `claude-code-commands/` - Command creation and workflows

**When to create here**: Meta-knowledge about the AI system itself

## Skill Size Management

### When to Split a Skill

**Indicators skill is too large**:
- SKILL.md exceeds 400 lines
- Covers multiple distinct domains
- Has many unrelated sections
- Takes long time to load/read

**How to split**:

```markdown
# Before (too large)
.claude/skills/java/development-guide/
└── SKILL.md (800 lines)
    - Naming conventions
    - Code organization
    - Documentation standards
    - Testing patterns
    - Logging strategy
    - Performance optimization

# After (properly split)
.claude/skills/java/
├── coding-standards/
│   └── SKILL.md (200 lines - naming, organization, docs)
├── defensive-programming/
│   └── SKILL.md (250 lines - testing patterns)
├── logging-strategy/
│   └── SKILL.md (150 lines - logging)
└── performance-testing/
    └── SKILL.md (200 lines - performance)
```

### When to Use examples/

**Move content to examples/ when**:
- Pattern needs extensive code examples
- Multiple variations of same pattern
- Advanced/edge case scenarios
- Historical context or migration guides

**Keep in SKILL.md**:
- Core patterns and principles
- Quick reference
- Common use cases
- Essential best practices

**Example structure**:

```markdown
# SKILL.md
## Authentication Pattern

**Basic Usage**:
```java
@Override
public boolean authenticate(User user) {
    return validateCredentials(user);
}
```

**See `examples/auth-patterns.md` for**:
- OAuth integration
- JWT token handling
- Multi-factor authentication
- Session management

# examples/auth-patterns.md
[Detailed implementations of all authentication patterns]
```

## Skill Integration Patterns

### Pattern 1: Agent References Skill

**In agent file**:

```markdown
## Skills You Use

Follow patterns from these skills (Claude Code loads automatically):
- `coding-standards` - Naming conventions and code organization
- `minecraft-modding` - Minecraft system knowledge
- `fabric-modding` - Fabric API specific patterns
```

**NOT**:
```markdown
# ❌ WRONG
Load the `coding-standards` skill and follow its patterns.
```

**Why**: Skills auto-load. Agent just needs to reference them.

### Pattern 2: Command References Skill

**In command file**:

```markdown
## Your Task

Create requirements.md following the `epic-requirements` skill patterns.

## Execution Steps

### 1. Structure Requirements

Follow the `epic-requirements` skill template structure.
```

**Why**: Command tells agent which skill to follow, skill provides the details.

### Pattern 3: Skill References Other Skills

**In SKILL.md**:

```markdown
## Related Skills

This skill works together with:
- `coding-standards` - For naming conventions used in logging
- `defensive-programming` - For testing logged behavior
- `performance-testing` - For measuring logging overhead

**Integration Example**:
When implementing a feature, apply:
1. `coding-standards` for method naming
2. `logging-strategy` (this skill) for observability
3. `defensive-programming` for test coverage
```

**Why**: Makes skill relationships explicit, helps users understand the knowledge graph.

### Pattern 4: Skill Family

**For related but distinct skills**:

```
.claude/skills/minecraft/minecraft-performance/
├── general/
│   └── SKILL.md              # General performance optimization
├── structure/
│   └── SKILL.md              # Structure-specific optimization
└── research/
    └── SKILL.md              # Knowledge retention from research
```

**Frontmatter**:
```yaml
# general/SKILL.md
---
name: minecraft-performance-general
description: General Minecraft performance optimization strategies
tags: [performance, optimization, general]
---

# structure/SKILL.md
---
name: minecraft-performance-structure
description: Structure generation performance optimization
tags: [performance, optimization, structures, worldgen]
---

# research/SKILL.md
---
name: minecraft-performance-research
description: Knowledge retention system for performance research findings
tags: [performance, research, knowledge-retention]
---
```

**Why**: Related knowledge grouped by folder, distinct skills for specific contexts.

## Debugging Skill Issues

### Issue: Skill Not Loading

**Symptoms**: Agent doesn't follow skill patterns, makes mistakes skill would prevent

**Diagnosis**:
1. Check frontmatter exists and is valid YAML
2. Verify `name` field matches expected skill name
3. Ensure `description` clearly describes skill purpose
4. Check file is in correct location (`.claude/skills/[category]/[skill-name]/SKILL.md`)

**Fix**:
```yaml
# ❌ WRONG (missing frontmatter)
# Coding Standards

# ✅ CORRECT
---
name: coding-standards
description: Java and Minecraft modding code standards
category: java
tags: [code, standards, style, naming]
---

# Coding Standards
```

### Issue: Skill Referenced but Not Applied

**Symptoms**: Agent mentions skill but doesn't follow it

**Diagnosis**:
1. Check if agent uses "load" language (wrong)
2. Verify skill content is clear and actionable
3. Check if skill is too long (>400 lines)

**Fix**:

```markdown
# In agent/command
# ❌ WRONG
Load the `coding-standards` skill

# ✅ CORRECT
Follow the `coding-standards` skill patterns

# In skill
# ❌ WRONG (vague)
Follow best practices for naming

# ✅ CORRECT (specific)
**Class Names**: PascalCase (e.g., `VillagerManager`)
**Methods**: camelCase (e.g., `findNearbyVillagers`)
**Constants**: UPPER_SNAKE_CASE (e.g., `MAX_DISTANCE`)
```

### Issue: Duplicate Knowledge Across Skills

**Symptoms**: Same information in multiple skills, inconsistencies

**Diagnosis**:
1. Search for duplicate patterns across skills
2. Identify which skill should own the knowledge
3. Check if skills reference each other

**Fix**:
```markdown
# coding-standards/SKILL.md (owns naming conventions)
## Naming Conventions

**Class Names**: PascalCase
**Methods**: camelCase
**Constants**: UPPER_SNAKE_CASE

# logging-strategy/SKILL.md (references, doesn't duplicate)
## Logger Naming

Follow `coding-standards` skill naming conventions for logger names:
```java
private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class);
```
```

### Issue: Skill Too Broad

**Symptoms**: Skill covers many unrelated topics, hard to maintain

**Diagnosis**:
1. Count distinct knowledge domains in skill
2. Check if skill name is generic (e.g., "development", "best-practices")
3. See if different sections have different audiences

**Fix**: Split into focused skills (see "When to Split a Skill")

## Skill Creation Checklist

When creating a new skill:

- [ ] **Planning**
  - [ ] Identified specific knowledge domain
  - [ ] Checked for existing skills covering topic
  - [ ] Determined appropriate category folder
  - [ ] Verified skill won't exceed 400 lines

- [ ] **Frontmatter**
  - [ ] Unique `name` (lowercase-with-hyphens)
  - [ ] Clear `description` (one line)
  - [ ] Appropriate `category`
  - [ ] Relevant `tags` for matching
  - [ ] `allowed-tools` if skill uses tools

- [ ] **Content Structure**
  - [ ] Purpose section explains scope
  - [ ] "When to Use" lists clear use cases
  - [ ] Core patterns documented
  - [ ] Best practices (DO/DON'T) included
  - [ ] Quick reference section

- [ ] **Examples**
  - [ ] Code examples are correct and tested
  - [ ] Shows both correct and incorrect patterns
  - [ ] Explains reasoning ("Why")
  - [ ] Large examples moved to `examples/`

- [ ] **Integration**
  - [ ] Related skills referenced
  - [ ] Shows how skill works with others
  - [ ] No duplicate content from other skills

- [ ] **Documentation**
  - [ ] Added to `.claude/CLAUDE.md` skills section
  - [ ] Category documented
  - [ ] Integration patterns explained

- [ ] **Testing**
  - [ ] Created sample agent/command that uses skill
  - [ ] Mental walkthrough of common use cases
  - [ ] Verified skill loads correctly

## Best Practices

### ✅ DO

- **Keep skills focused** on single knowledge domain
- **Use clear frontmatter** with accurate description and tags
- **Provide concrete examples** with code snippets
- **Explain reasoning** with "Why" sections
- **Reference other skills** instead of duplicating
- **Keep SKILL.md concise** (<400 lines)
- **Move details to examples/** for advanced patterns
- **Update regularly** as patterns evolve
- **Test mental walkthrough** of skill usage

### ❌ DON'T

- **Create overly broad skills** covering many domains
- **Duplicate knowledge** across multiple skills
- **Use vague language** - be specific and actionable
- **Exceed 400 lines** in SKILL.md (split instead)
- **Forget frontmatter** - required for auto-loading
- **Create skills for single use** - skills should be reusable
- **Skip documentation** in CLAUDE.md
- **Use "load" language** when referencing skills

## Skill Maintenance

### When to Update a Skill

**Update when**:
- New patterns discovered through research
- Anti-patterns identified in code reviews
- Framework/library updates change best practices
- User feedback indicates confusion
- Related skills change and affect this one

### Update Process

1. **Identify what changed**
   ```bash
   # Find all files that might need updates
   grep -r "old-pattern" .claude/skills/
   ```

2. **Update skill content**
   - Modify SKILL.md
   - Update examples if needed
   - Adjust related skills references

3. **Update documentation**
   - Update `.claude/CLAUDE.md` if skill scope changed
   - Update agent files if they reference the skill

4. **Test integration**
   - Mental walkthrough of agents using skill
   - Verify no conflicts with other skills
   - Check examples still work

### Deprecating Skills

**When to deprecate**:
- Knowledge is obsolete (old framework version)
- Merged into another skill
- Split into multiple focused skills

**Process**:
1. Add deprecation notice to SKILL.md
2. Reference replacement skill(s)
3. Update agents/commands to use new skill
4. After transition period, archive skill

```markdown
# DEPRECATED: old-skill

**This skill is deprecated. Use these instead**:
- `new-skill-1` - Covers X
- `new-skill-2` - Covers Y

[Original content for reference]
```

## Advanced Patterns

### Conditional Skill Application

**For skills with context-dependent patterns**:

```markdown
## Pattern Selection

**For client-side code**:
[Client-specific patterns]

**For server-side code**:
[Server-specific patterns]

**For shared code**:
[Patterns that work everywhere]
```

### Skill Templates

**For skills that provide templates**:

```
.claude/skills/process/epic-requirements/
├── SKILL.md              # Guidelines for requirements
└── template.md           # requirements.md template
```

**Reference in SKILL.md**:
```markdown
## Template Usage

Use the template in `template.md` as a starting point.

**Required Sections**:
- Epic Overview
- Features
- Success Criteria
```

### Skill Versioning

**For skills that evolve significantly**:

```
.claude/skills/minecraft/fabric-modding/
├── SKILL.md              # Current version (Fabric 1.21.1)
└── examples/
    ├── migration-1.20-to-1.21.md
    └── legacy-1.20-patterns.md
```

## Quality Standards

### Content Quality

- **Accurate**: All patterns tested and verified
- **Current**: Reflects latest framework versions
- **Complete**: Covers common use cases
- **Clear**: Easy to understand and follow
- **Concise**: No unnecessary information

### Structural Quality

- **Well-organized**: Logical flow of sections
- **Consistent**: Follows skill template
- **Scannable**: Clear headings and formatting
- **Referenced**: Links to related skills and examples

### Integration Quality

- **Discoverable**: Tags and description match use cases
- **Composable**: Works well with other skills
- **Non-duplicate**: No overlap with existing skills
- **Referenced**: Used by relevant agents/commands

## References

- Related skills: `claude-code-agents`, `claude-code-commands`, `claude-code-setup`
- Documentation: `.claude/CLAUDE.md` (Skills System section)
- Examples: See existing skills in `.claude/skills/`
- Claude Code docs: https://docs.claude.com/en/docs/claude-code
