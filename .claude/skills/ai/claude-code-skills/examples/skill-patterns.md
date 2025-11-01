# Skill Patterns and Examples

Detailed examples for creating, organizing, and maintaining Claude Code skills.

## Complete Skill Creation Walkthrough

### Example: Creating a `performance-profiling` Skill

#### Step 1: Design

**Scope**: Patterns for profiling and measuring performance in Minecraft mods

**Category**: `java/` (could be `minecraft/` but profiling is language-level)

**Who uses it**: implementation-agent when working on performance tasks

**Existing skills to check**:
- `performance-testing` - Related but different (testing vs profiling)
- `java-development` - General Java, not profiling-specific

**Decision**: Create new skill in `java/performance-profiling/`

#### Step 2: Create Structure

```bash
mkdir -p .claude/skills/java/performance-profiling
cd .claude/skills/java/performance-profiling
touch SKILL.md
```

#### Step 3: Write SKILL.md

```markdown
---
name: performance-profiling
description: Patterns for profiling and measuring Minecraft mod performance
category: java
tags: [java, performance, profiling, optimization, benchmarking]
allowed-tools: [Read, Write, Bash]
---

# Performance Profiling

Patterns for profiling and measuring performance in Minecraft mods.

## When to Use

- Investigating performance issues
- Measuring optimization impact
- Identifying bottlenecks
- Benchmarking before/after changes

## Core Pattern

### 1. Spark Profiler (Recommended)

**Setup**:
```bash
# Add Spark dependency to build.gradle
dependencies {
    include modImplementation("me.lucko:spark-api:0.1-SNAPSHOT")
}
```

**Usage in code**:
```java
// Profile a specific method
SparkAPI.profiler().start();
performExpensiveOperation();
SparkAPI.profiler().stop();
```

### 2. Manual Timing

**For quick measurements**:
```java
long start = System.nanoTime();
myMethod();
long duration = System.nanoTime() - start;
LOGGER.info("Operation took {}ms", duration / 1_000_000);
```

**See `examples/spark-integration.md` for detailed Spark setup**
**See `examples/profiling-patterns.md` for advanced techniques**

## Common Anti-Patterns

- ❌ Using `System.currentTimeMillis()` (low precision)
- ❌ Profiling without warmup (JIT not optimized yet)
- ❌ Not profiling in production-like environment
- ❌ Ignoring GC pauses in measurements

## References

- Related: `performance-testing` (testing performance)
- Related: `minecraft-performance-general` (optimization strategies)
```

#### Step 4: Document in CLAUDE.md

Add to `.claude/CLAUDE.md`:

```markdown
### Java Skills
- **performance-profiling** - Profiling and measuring mod performance
```

#### Step 5: Add Detailed Examples

Create `examples/spark-integration.md`:

```markdown
# Spark Profiler Integration

Complete guide for integrating Spark profiler into your mod...
[Detailed content here]
```

Create `examples/profiling-patterns.md`:

```markdown
# Advanced Profiling Patterns

Advanced techniques for performance measurement...
[Detailed content here]
```

---

## Before/After Refactoring Example

### Before: Monolithic Skill (800+ lines)

**Problem**: `development-practices` skill covered too much

```markdown
# Development Practices

## Code Standards
- Naming conventions
- Code organization
- Documentation

## Testing
- Unit tests
- Integration tests
- Mocking patterns

## Version Control
- Git workflows
- Commit messages
- Branch strategies

## Performance
- Profiling
- Optimization
- Benchmarking

[300 lines of code standards]
[200 lines of testing patterns]
[150 lines of git workflows]
[150 lines of performance]
```

**Issues**:
- Too long (800+ lines)
- Mixed responsibilities
- Hard to maintain
- Confusing to reference

### After: Split into Focused Skills

**Result**: 4 separate, focused skills

```
.claude/skills/java/
├── coding-standards/      # 250 lines
│   └── SKILL.md
├── testing-patterns/      # 180 lines
│   └── SKILL.md
├── performance-profiling/ # 120 lines
│   └── SKILL.md
└── git-workflow/          # 200 lines
    └── SKILL.md
```

**Benefits**:
- Each under 400 lines
- Clear single responsibility
- Easy to find and use
- Can be updated independently

---

## Multi-File Skill Organization

### Example: Large Skill with Examples

```
.claude/skills/minecraft/fabric-modding/
├── SKILL.md                    # 350 lines (core patterns)
├── examples/
│   ├── entity-creation.md      # Detailed entity examples
│   ├── custom-blocks.md        # Block registration patterns
│   ├── networking.md           # Client-server sync
│   └── rendering.md            # Custom rendering
└── templates/
    ├── entity-template.java    # Boilerplate code
    └── block-template.java
```

**SKILL.md** (condensed):
```markdown
## Entity Creation

**Basic pattern**:
```java
public class CustomEntity extends MobEntity {
    // Core entity structure
}
```

**See `examples/entity-creation.md` for complete examples**:
- Living entities
- Projectile entities
- Custom AI goals
- Data components

## Custom Blocks

**Basic pattern**:
```java
public class CustomBlock extends Block {
    // Core block structure
}
```

**See `examples/custom-blocks.md` for complete examples**
```

**examples/entity-creation.md** (detailed):
```markdown
# Entity Creation Examples

## Living Entities

Complete example of a living entity with AI:

```java
public class GuardEntity extends PathAwareEntity {
    // [100 lines of complete implementation]
}
```

## Projectile Entities

[Another 100 lines]

## Custom AI Goals

[Another 100 lines]
```

**Benefits**:
- SKILL.md stays under 400 lines
- Complete examples still available
- Easy to find specific patterns
- Can add more examples without bloating core file

---

## Advanced Integration Patterns

### Pattern 1: Skill Composition

**Skills can reference each other**:

**In `fabric-modding/SKILL.md`**:
```markdown
## Code Quality

When implementing Fabric features, follow:
- **coding-standards** skill (Java conventions)
- **defensive-programming** skill (validation, error handling)
- **logging-strategy** skill (proactive logging)

These skills are auto-loaded - reference them in your code.
```

**Why**: Compose complex guidance from focused skills

### Pattern 2: Conditional Guidance

**Skill provides different patterns based on context**:

```markdown
## Entity Rendering

### For Client-Side Mods
[Client-specific patterns]

### For Server-Side Mods
Server-side entities don't need renderers.
**See `ui-ux-design` skill for server-side UI patterns**.

### For Universal Mods (Client + Server)
[Universal patterns with client/server separation]
```

**Why**: One skill serves multiple contexts

### Pattern 3: Progressive Disclosure

**SKILL.md**: Basic pattern + reference

**examples/**: Intermediate patterns

**examples/advanced/**: Complex patterns

```
skill-name/
├── SKILL.md              # Basics
├── examples/
│   ├── intermediate.md   # More complex
│   └── advanced/
│       ├── optimization.md
│       └── edge-cases.md
```

**In SKILL.md**:
```markdown
## Basic Pattern

[Simple example]

**Need more?**
- `examples/intermediate.md` - Complex scenarios
- `examples/advanced/optimization.md` - Performance optimization
```

**Why**: Readers get what they need without information overload

---

## Skill Naming Patterns

### Good Naming Examples

| Name | Why It's Good | What It Covers |
|------|---------------|----------------|
| `fabric-modding` | Specific framework | Fabric API patterns |
| `epic-requirements` | Clear purpose | Writing requirements.md |
| `coding-standards` | Well-established term | Code style, naming |
| `performance-profiling` | Action-oriented | Profiling techniques |
| `ui-ux-design` | Domain-specific | UI/UX patterns |

### Bad Naming Examples

| Name | Why It's Bad | Better Alternative |
|------|--------------|-------------------|
| `helpers` | Too vague | Specific: `string-utils`, `collection-helpers` |
| `stuff` | Meaningless | Describe what it contains |
| `general` | Not specific | Split into focused skills |
| `best-practices` | Too broad | `coding-standards`, `testing-patterns` |
| `tips-and-tricks` | Informal | `optimization-techniques` |

---

## Frontmatter Examples

### Good Frontmatter

```yaml
---
name: fabric-modding
description: Fabric API patterns for Minecraft 1.21.1 modding
category: minecraft
tags: [minecraft, fabric, modding, api, 1.21.1]
allowed-tools: [Read, Write, Grep, Bash]
---
```

**Why it's good**:
- Clear, specific description
- Relevant tags (Minecraft, Fabric, version)
- Appropriate category
- Lists tools actually used

### Poor Frontmatter

```yaml
---
name: modding
description: Modding stuff
tags: [general]
---
```

**Issues**:
- Name too generic ("modding" could be any game)
- Description vague ("stuff")
- Tags not useful ("general")
- Missing category and allowed-tools

### Fixed Version

```yaml
---
name: minecraft-modding
description: Core Minecraft modding patterns (loader-agnostic)
category: minecraft
tags: [minecraft, modding, entities, blocks, items]
allowed-tools: [Read, Write]
---
```

---

## Common Refactoring Scenarios

### Scenario 1: Skill Too Long

**Before** (650 lines):
```markdown
# Testing Patterns

## Unit Testing
[150 lines]

## Integration Testing
[200 lines]

## Mocking
[150 lines]

## Test Data Builders
[150 lines]
```

**After** (main: 280 lines, examples: 400 lines):

**SKILL.md** (280 lines):
```markdown
# Testing Patterns

## Unit Testing
[Core pattern - 50 lines]
**See `examples/unit-testing.md` for complete examples**

## Integration Testing
[Core pattern - 60 lines]
**See `examples/integration-testing.md`**

## Mocking
[Core pattern - 50 lines]
**See `examples/mocking.md`**

## Test Data Builders
[Core pattern - 60 lines]
**See `examples/test-builders.md`**
```

**examples/** (400 total lines across 4 files)

### Scenario 2: Duplicate Content

**Before**: Same patterns in multiple skills

```
coding-standards/SKILL.md:
  - Naming conventions [100 lines]
  - Error handling [50 lines]

defensive-programming/SKILL.md:
  - Input validation [80 lines]
  - Error handling [50 lines] ← DUPLICATE

logging-strategy/SKILL.md:
  - Log levels [60 lines]
  - Error handling [30 lines] ← DUPLICATE
```

**After**: Centralize and reference

```
coding-standards/SKILL.md:
  - Naming conventions [100 lines]
  - Error handling [50 lines] ← CANONICAL VERSION

defensive-programming/SKILL.md:
  - Input validation [80 lines]
  - See `coding-standards` for error handling ← REFERENCE

logging-strategy/SKILL.md:
  - Log levels [60 lines]
  - See `coding-standards` for error handling ← REFERENCE
```

---

## Integration with Agents

### Example 1: Agent References Multiple Skills

**In `implementation-agent.md`**:
```markdown
## Skills You Use

Follow these skills (Claude Code loads them automatically):
- **coding-standards** - Code style, naming, architecture
- **fabric-modding** - Fabric API patterns
- **defensive-programming** - Validation, error handling
- **logging-strategy** - Proactive logging
- **performance-testing** - Performance validation

When implementing features, apply all relevant skill patterns.
```

### Example 2: Command References Specific Skill

**In `/create_epic` command**:
```markdown
You are the epic-agent creating a new epic.

Follow the **epic-requirements** skill (Claude Code loads automatically) for:
- Epic structure template
- Game mechanics patterns
- Requirements sections

The skill provides complete templates - use them.
```

### Example 3: Skill References Other Skills

**In `fabric-modding/SKILL.md`**:
```markdown
## Code Quality Standards

When writing Fabric mod code:

1. **Follow `coding-standards` skill** (auto-loaded):
   - Java naming conventions
   - Package organization
   - Javadoc requirements

2. **Follow `defensive-programming` skill** (auto-loaded):
   - Null checks
   - Input validation
   - Error handling

3. **Follow Fabric-specific patterns** (below):
   - Event registration
   - Registry patterns
   - Client/server separation
```

---

## Quality Checklist Examples

### Before Finalizing a Skill

```
skill-name/SKILL.md

Checklist:
✅ Frontmatter complete
   - name: "performance-profiling"
   - description: "Patterns for profiling..."
   - category: "java"
   - tags: [java, performance, profiling]
   - allowed-tools: [Read, Write, Bash]

✅ Under 400 lines (current: 285 lines)

✅ Clear purpose
   "Profiling and measuring Minecraft mod performance"

✅ Examples referenced, not embedded
   - "See examples/spark-integration.md"
   - "See examples/profiling-patterns.md"

✅ No duplicate content
   - Checked: No overlap with performance-testing
   - Different focus: profiling vs testing

✅ Documented in CLAUDE.md
   Added to Java Skills section

✅ Category matches folder
   Category: "java"
   Path: .claude/skills/java/performance-profiling/

✅ Agents reference correctly
   implementation-agent: "Follow performance-profiling skill"
   (No "load" language)
```

---

## Summary

**Key Takeaways**:

1. **Keep SKILL.md under 400 lines** - Use examples/ for details
2. **One responsibility per skill** - Split if too broad
3. **Clear, specific naming** - Avoid vague names
4. **Good frontmatter** - Enables auto-loading
5. **Reference, don't duplicate** - Link to other skills
6. **Progressive disclosure** - Basic → intermediate → advanced
7. **Document in CLAUDE.md** - Make discoverable
8. **Agents reference, not load** - Skills auto-load

**Progressive Loading Pattern**:
```
SKILL.md (< 400 lines):
  - Purpose and scope
  - Core patterns (minimal examples)
  - References to examples/
  - Common anti-patterns

examples/ (unlimited):
  - Complete implementations
  - Advanced patterns
  - Edge cases
  - Templates
```

This keeps skills maintainable, fast to load, and easy to use!
