---
name: minecraft-performance-research
description: Knowledge retention system for performance research findings. Provides guidelines for when and how to capture research insights into skills, preventing knowledge loss. Part of the minecraft/performance skill family.
allowed-tools: [Read, Write, Edit, Glob, Grep]
---

# Performance Research Knowledge Retention Skill

**Category**: Minecraft Performance (Knowledge Management)
**Last Updated**: 2025-10-25
**Purpose**: Capture research findings into reusable knowledge

## The Knowledge Loss Problem

**Scenario**:
```
1. Do performance research → Create .claude/research/findings.md
2. Use findings to solve immediate problem
3. Move to next problem
4. 2 months later: Same type of problem, no memory of findings
5. Re-research same topic = wasted time
```

**Solution**: Systematically capture research into skills when appropriate.

## When to Update Skills from Research

### ✅ Update Skill When:

1. **Discovered a Pattern**
   - Found a general solution applicable to multiple cases
   - Example: "Grid-based filtering reduces checks by 50%"

2. **Debunked a Misconception**
   - Research proves common belief is wrong
   - Example: "Memory is NOT the bottleneck for 569 structures"

3. **Identified New Best Practice**
   - Found better way to do something
   - Example: "Always use Spark Profiler before optimizing"

4. **Documented Performance Budget**
   - Measured concrete thresholds
   - Example: "STRUCTURE_START should be < 20ms per chunk"

5. **Created Reusable Template**
   - Pattern that applies to similar problems
   - Example: "Spacing multiplier calculation formula"

### ❌ Don't Update Skill When:

1. **One-Time Specific Fix**
   - Solution only applies to one bug
   - Keep in research/ but don't promote to skill

2. **Experimental Findings**
   - Not yet validated or proven
   - Wait for confirmation before adding to skill

3. **Project-Specific Knowledge**
   - Only relevant to current project
   - Keep in project `.claude/research/`

4. **Incomplete Understanding**
   - Still investigating, don't have full picture
   - Complete research first

## How to Update Skills from Research

### Process:

```
STEP 1: Complete Research
├─ Investigate problem thoroughly
├─ Create research document in .claude/research/
└─ Validate findings (test, measure, verify)

STEP 2: Identify Reusable Knowledge
├─ What patterns did you discover?
├─ What assumptions did you debunk?
├─ What best practices emerged?
└─ What can future-you use?

STEP 3: Determine Target Skill
├─ minecraft-performance-general: General optimization patterns
├─ minecraft-performance-structure: Structure-specific patterns
├─ New skill: If topic warrants dedicated skill
└─ Consider: Does this fit existing skills or need new one?

STEP 4: Update Skill
├─ Add new section for findings
├─ Include code examples if applicable
├─ Reference research document
├─ Update "Last Updated" date
└─ Add to "Key Takeaways" section

STEP 5: Cross-Reference
├─ Link research document to updated skill
├─ Link skill to research document
└─ Update related skills if needed
```

### Template for Skill Updates:

```markdown
## [New Section Title Based on Research]

### Research Finding: [Brief Summary]

**Source**: `.claude/research/[research-file].md`
**Date**: YYYY-MM-DD
**Key Insight**: [One sentence summary]

**Detailed Explanation**:
[Explain the finding in detail]

**Example**:
```java
// Code example demonstrating the finding
```

**When to Apply**:
- Scenario 1
- Scenario 2

**Validation**:
- How to verify this works
- Expected results
```

## Skill Update Decision Matrix

| Research Type | Target Skill | Add When |
|--------------|--------------|----------|
| **Entity optimization pattern** | minecraft-performance-general | Pattern applies to > 1 entity type |
| **Worldgen bottleneck** | minecraft-performance-structure | Affects structure generation |
| **Profiling methodology** | minecraft-performance-general | Reusable measurement technique |
| **Memory optimization** | minecraft-performance-general | General memory reduction pattern |
| **Structure placement math** | minecraft-performance-structure | Calculation method for placement |
| **Performance budget** | Both skills | Threshold applies to category |
| **Bug-specific fix** | NONE | Keep in research/ only |
| **New optimization category** | Create new skill | Topic is large enough (>5KB content) |

## Real Example: Structure Performance Research

**Research Files** (xeenaa-structure-manager):
```
.claude/research/
├── structure-performance-bottleneck.md
├── biome-structure-filtering.md
├── structure-start-stage-analysis.md
├── structure-variant-selection.md
└── structure-placement-tracking.md
```

**Skill Update** (from these research files):
- Created: `minecraft-performance-structure` skill
- Captured: Grid-based filtering concept
- Debunked: "Memory is the bottleneck" myth
- Documented: Spacing multiplier strategy
- Added: Performance testing methodology

**Result**: Future mods don't need to re-research structure performance basics.

## Knowledge Retention Checklist

After completing performance research, ask:

- [ ] Did I discover a general pattern? → Update skill
- [ ] Did I debunk a misconception? → Add to skill myths section
- [ ] Did I measure performance thresholds? → Add to skill budgets
- [ ] Did I create reusable code pattern? → Add example to skill
- [ ] Does this apply to multiple scenarios? → Update skill
- [ ] Is this a one-time fix? → Keep in research only
- [ ] Should this be a new skill? → Create if >5KB content

## Maintaining Skills Over Time

### Regular Skill Reviews:

**Monthly** (or after major research):
1. Review recent research documents
2. Identify patterns across multiple research files
3. Update skills with consolidated knowledge
4. Archive outdated findings

**Quarterly**:
1. Audit skills for accuracy
2. Update examples for new Minecraft versions
3. Remove obsolete patterns
4. Merge similar sections

**Yearly**:
1. Major skills restructuring if needed
2. Split large skills (>50KB)
3. Archive deprecated content
4. Update version references

## Anti-Pattern: Skill Bloat

**Problem**: Adding every research finding to skills makes them unwieldy.

**Solution**: Quality over quantity
- Only add findings used > 3 times
- Keep skills focused on one topic
- Use cross-references instead of duplication
- Create new skills when topics diverge

**Example of Bloat**:
```markdown
## How to Fix Guard Armor Not Saving (Don't do this)
## How to Fix Villager Trades Broken (Don't do this)
## How to Fix Entity Duplication Bug (Don't do this)
```

**Better Approach**:
```markdown
## Entity Data Persistence Patterns (Do this)
- General NBT serialization best practices
- Common persistence pitfalls
- Validation checklist
```

## Integration with Research Workflow

### When using `/research` command:

```
User: /research "Why do 569 structures cause lag?"

→ research-agent investigates
→ Creates: .claude/research/structure-lag-investigation.md
→ ALSO checks: Should this update a skill?

→ If yes: Suggests skill update
→ If no: Marks as project-specific knowledge

→ Returns: "Research complete. Findings added to
           minecraft-performance-structure skill under
           'Grid-Based Filtering' section."
```

## Skill Update Template

When updating a skill from research:

```markdown
---
name: skill-name
description: [Updated description if needed]
allowed-tools: [...]
---

# Skill Title

[Existing content...]

---

## NEW: [Research Finding Title]

**Added**: YYYY-MM-DD (Research: `path/to/research.md`)

**Finding**: [One sentence summary]

**Explanation**: [Detailed explanation]

**Example**: [Code example if applicable]

**When to Use**: [Scenarios where this applies]

**References**:
- Research: `.claude/research/file.md`
- Related: [Related skill or section]

---

[Continue with existing content...]
```

## Key Principles

1. **Research is Ephemeral, Skills are Permanent**
   - Research documents solve immediate problems
   - Skills capture reusable knowledge

2. **Promote Knowledge Upwards**
   - Project research → Skill (if general)
   - One-time fix → Research only
   - Pattern → Skill update

3. **Cross-Reference Everything**
   - Skills point to research for details
   - Research points to skills for context
   - Related skills link to each other

4. **Quality Control**
   - Only validated findings go into skills
   - Test before documenting
   - Update if proven wrong

5. **Avoid Duplication**
   - One canonical source per concept
   - Use links instead of copying
   - Merge similar sections

## Success Metrics

Good knowledge retention system shows:
- ✅ Decreasing research time for similar problems
- ✅ Skills referenced in agent outputs
- ✅ New contributors can find answers in skills
- ✅ Research documents cite skills as "already known"
- ✅ Fewer duplicate research investigations

## When to Apply This Skill

Use this skill:
- After completing performance research
- When updating skills based on findings
- When deciding if research → skill
- When reviewing knowledge retention
- When restructuring skills
- When onboarding new contributors

## Related Skills

- **research-methodology**: How to conduct research
- **minecraft-performance-general**: General optimization patterns
- **minecraft-performance-structure**: Structure-specific patterns

---

**Remember**: Research solves today's problem. Skills prevent tomorrow's re-research.

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used minecraft-performance-research
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used minecraft-performance-research`

This helps track which skills are actively consulted and identifies documentation gaps.
