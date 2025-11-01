---
name: claude-code-skills
description: Creating and maintaining Claude Code skills - model-invoked capabilities with trigger-based activation, progressive disclosure, and proper frontmatter configuration. Use when creating skills, debugging activation issues, or structuring skill content.
category: ai
tags: [ai, claude-code, skills, knowledge, organization]
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Claude Code Skills

Guide for creating and maintaining Claude Code skills as model-invoked capabilities that extend Claude's functionality.

## Core Principles

### 1. Model-Invoked Activation

**Skills are "model-invoked"** - Claude autonomously decides when to activate them based on relevance to the current task.

Skills activate based on metadata in frontmatter:

```yaml
---
name: skill-name                           # Required: lowercase, hyphens, max 64 chars
description: What it does AND when to use  # Required: includes trigger terms, max 1024 chars
allowed-tools: [Read, Write, Edit]         # Optional: tool restrictions
category: minecraft|java|process|ai        # Custom: our categorization (not official)
tags: [keywords, for, matching]            # Custom: our tagging system (not official)
---
```

**Official Fields** (Claude Code standard):
- `name` - Skill identifier (required)
- `description` - Capability + trigger terms (required, max 1024 chars)
- `allowed-tools` - Tool access restrictions (optional)

**Custom Fields** (our project extensions):
- `category` - Our organizational grouping
- `tags` - Our keyword matching system

**Key**: Model-invoked activation means Claude decides when to use skills based on the `description` field.

### 2. Progressive Disclosure Pattern

**Three-level loading** (official Claude Code pattern):

**Level 1 - Metadata** (loaded at startup):
- `name` and `description` from frontmatter
- Claude uses this to determine skill relevance
- Minimal token cost (~30-50 tokens per skill)

**Level 2 - Core Content** (loaded when skill activates):
- Full `SKILL.md` file loads into context
- Target: Under 400 lines for efficiency
- Quick reference patterns and essential guidance

**Level 3 - Supporting Files** (loaded on-demand):
- Claude navigates to referenced files when needed
- Examples: `examples/`, `reference.md`, `templates/`
- Use markdown links: `See examples/patterns.md for details`

**Structure**:
```
skill-name/
├── SKILL.md           # Core patterns (Level 2)
├── examples/          # Detailed examples (Level 3)
│   └── patterns.md
└── templates/         # Templates (Level 3)
```

**Pattern**: Metadata → Core → On-demand details

### 3. Single Responsibility

One skill = one knowledge domain

✅ `coding-standards` - Code style, naming, docs
❌ `development-guide` - Mixed topics (split these)

---

## Skill Creation Process

### Step 1: Design

**Before creating**:
1. Define clear scope (one domain)
2. Choose category folder
3. Identify which agents will use it
4. Check for existing similar skills

### Step 2: Create Structure

```bash
mkdir -p .claude/skills/[category]/[skill-name]
touch .claude/skills/[category]/[skill-name]/SKILL.md
```

**Categories**:
- `minecraft/` - Minecraft/modding patterns
- `java/` - Java language patterns
- `process/` - Workflow and process
- `ai/` - AI infrastructure

### Step 3: Write SKILL.md

**Template**:
```markdown
---
name: skill-name
description: What it does AND trigger terms users would mention. Use when [specific contexts].
allowed-tools: [Read, Write]         # Optional: tool restrictions
category: [category]                  # Custom field (our project)
tags: [keyword1, keyword2, keyword3]  # Custom field (our project)
---

# Skill Name

Brief purpose statement.

## When to Use

- Use case 1 with trigger terms
- Use case 2 with trigger terms

## Core Pattern

[Essential pattern with minimal example]

**See `examples/detailed.md` for more examples** (progressive disclosure)

## Common Anti-Patterns

- ❌ Don't do X
- ❌ Avoid Y

## References

- Related skill: `other-skill`
```

**Description Field Best Practices**:
- ✅ Include **trigger terms** users would mention
- ✅ Explain BOTH capability AND context
- ✅ Max 1024 characters
- ❌ Don't use generic terms like "helps with" or "manages"
- ✅ Example: "Extract text from PDFs, fill forms, merge documents. Use when working with PDF files or mentioning document extraction."

### Step 4: Add to Documentation

Update `.claude/CLAUDE.md` skills section:

```markdown
### [Category] Skills
- **skill-name** - One-line description
```

### Step 5: (Optional) Add Examples

If skill needs detailed examples:

```bash
mkdir .claude/skills/[category]/[skill-name]/examples
```

Create example files referenced from SKILL.md.

---

## Skill Organization

### By Category

```
.claude/skills/
├── minecraft/        # Minecraft-specific
│   ├── fabric-modding/
│   ├── minecraft-modding/
│   └── ui-ux-design/
├── java/             # Java language
│   ├── coding-standards/
│   └── java-development/
├── process/          # Workflows
│   ├── epic-requirements/
│   └── task-planning/
└── ai/               # AI infrastructure
    ├── ai-workflow/
    ├── claude-code-agents/
    ├── claude-code-commands/
    ├── claude-code-setup/
    └── claude-code-skills/
```

### Naming Conventions

✅ **Good names**:
- `fabric-modding` - Clear scope
- `epic-requirements` - Specific purpose
- `coding-standards` - Well-defined

❌ **Bad names**:
- `helpers` - Too vague
- `utils` - Unclear scope
- `stuff` - Not descriptive

---

## Integration with Agents

### Referencing Skills

**In agent/command prompts**:

```markdown
## Skills You Use

Follow these skills (model-invoked based on relevance):
- **coding-standards** - Code style and naming
- **fabric-modding** - Fabric API patterns
```

**❌ WRONG**:
```markdown
Load the coding-standards skill and follow it.
```

**✅ CORRECT**:
```markdown
Follow the coding-standards skill (Claude activates when relevant).
```

### How Skills Activate

**Model-Invoked Activation Process**:

1. **At startup**: Claude reads all SKILL.md frontmatter (name + description)
2. **During task**: Claude matches task requirements to skill descriptions
3. **Autonomous decision**: Claude decides which skills to activate
4. **Progressive loading**: Loads SKILL.md when relevant, then supporting files on-demand

**Key Distinction**:
- **Skills** = Model-invoked (Claude decides based on relevance)
- **Commands** = User-invoked (user types `/command` explicitly)

**Agents don't "load" skills - Claude autonomously activates skills based on task relevance**.

---

## Common Issues

### Issue: Skill Not Being Used

**Symptom**: Agent doesn't follow patterns

**Causes**:
- Frontmatter missing or incorrect
- Tags don't match context
- Agent prompt says "load skill" (wrong!)

**Fix**:
1. Verify frontmatter has `name`, `description`, `tags`
2. Update tags to match use cases
3. Change prompt to "Follow [skill] (auto-loaded)"

### Issue: Skill Too Long

**Symptom**: SKILL.md over 400 lines

**Fix**: Apply progressive loading
1. Keep core patterns in SKILL.md
2. Move detailed examples to `examples/`
3. Add references: "See `examples/X.md` for details"

### Issue: Duplicate Knowledge

**Symptom**: Similar content in multiple skills

**Fix**: Consolidate or reference
- Merge into one skill if closely related
- Or have one skill reference another
- Avoid copy-paste between skills

---

## Best Practices

### For SKILL.md Content

**✅ DO**:
- Focus on essential patterns
- Include anti-patterns (what NOT to do)
- Reference examples instead of embedding
- Keep under 400 lines
- Use clear section headers

**❌ DON'T**:
- Include every possible example
- Duplicate content from other skills
- Use vague descriptions
- Forget frontmatter

### For Frontmatter

**✅ DO**:
- Use descriptive, searchable tags
- Write clear one-line description
- Match category to folder structure
- Include relevant allowed-tools

**❌ DON'T**:
- Use generic tags (e.g., "general")
- Write multi-line descriptions
- Mismatch category and folder
- List tools skill doesn't use

### For Organization

**✅ DO**:
- Group related skills in same category
- Use consistent naming (kebab-case)
- Create examples/ when needed
- Document in CLAUDE.md

**❌ DON'T**:
- Mix categories (one skill, one category)
- Use inconsistent naming
- Embed all examples in SKILL.md
- Skip CLAUDE.md documentation

---

## Quality Checklist

Before finalizing a skill:

- [ ] Frontmatter complete (name, description, category, tags)
- [ ] SKILL.md under 400 lines
- [ ] Clear purpose and scope
- [ ] Examples referenced (not embedded if long)
- [ ] No duplicate content from other skills
- [ ] Documented in `.claude/CLAUDE.md`
- [ ] Category folder matches frontmatter
- [ ] Agents reference skill correctly (no "load" language)

---

## Maintenance

### When to Split a Skill

Split if:
- Over 400 lines and can't condense
- Covers multiple distinct domains
- Has independent use cases

**Example**: Split `development-guide` into:
- `coding-standards` (code style)
- `testing-patterns` (testing)
- `git-workflow` (version control)

### When to Merge Skills

Merge if:
- Multiple small skills on same topic
- Heavily dependent on each other
- Always used together

**Example**: Merge `naming-conventions` + `code-organization` → `coding-standards`

### Updating Skills

**When updating**:
1. Keep frontmatter current
2. Update CLAUDE.md if description changes
3. Maintain backward compatibility
4. Update references in agent prompts

---

## Examples

**See `examples/skill-patterns.md`** for:
- Complete skill creation walkthrough
- Before/after refactoring examples
- Multi-file skill organization
- Advanced integration patterns

---

## References

**Related Skills**:
- **claude-code-setup** - Overall infrastructure
- **claude-code-agents** - Agent patterns
- **claude-code-commands** - Command patterns
- **ai-workflow** - How skills integrate with workflows

**Documentation**: `.claude/CLAUDE.md` - Complete skills list

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used claude-code-skills
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used claude-code-skills`

This helps track which skills are actively consulted and identifies documentation gaps.
