---
name: concise-responses
description: Provide short, actionable answers first, then offer detailed reports if needed. Prevents overwhelming users with verbose responses.
allowed-tools: [Read, Write, Edit]
---

# Concise Responses Skill

**Purpose**: Answer questions efficiently - provide key information first, elaborate only when requested.

**When this applies**: Analysis questions, investigations, recommendations, codebase queries.

---

## Core Principle

**Answer first, elaborate only if requested.**

Users value their time. Give them what they need to make a decision, not everything you know.

---

## Response Format

### For Analysis/Investigation Questions

**Initial Response** (≤30 lines):

```markdown
## Quick Answer

[Direct answer in 3-5 bullet points]

## Action Items

[What can be done - numbered list, max 5 items]

---

**Want more details?**
- Say "extend" for full analysis
- Say "report" for comprehensive documentation (saved to .claude/temp/)
```

### After User Requests Extension

**If user says "extend"**:
- Expand inline with more details (50-100 lines)
- Still keep it focused and structured

**If user says "report"**:
- Create comprehensive document in `.claude/temp/[topic]-report.md`
- Keep response short, just tell user where the file is

---

## Examples

### Bad (What NOT to do) ❌

```
User: "Which entities need GeckoLib animations?"
Assistant: [Creates 300-line analysis of all 89 entities with complete implementation details, model formats, animation controllers, and code examples]
```

**Problem**: User just needed a quick list to prioritize work.

### Good (What to do) ✅

```
User: "Which entities need GeckoLib animations?"
Assistant:

## Entities Needing GeckoLib

**Found 12 complex entities requiring GeckoLib**:

1. **Boss Mobs** (3 entities)
   - Void Worm: Complex multi-segment animation
   - Bone Serpent: Particle effects + movement
   - Laviathan: Advanced combat animations

2. **Flying Entities** (4 entities)
   - Bone Serpent, Laviathan, Straddler, Flutter
   - Need flight animation + combat states

3. **Complex Behaviors** (5 entities)
   - Mimicube (transformation animations)
   - Mimic Octopus (disguise animations)
   - Farseer (portal animations)

**Recommended Priority**:
1. Start with simple land mobs (basic walk/idle)
2. Add flying entities (more complex)
3. Tackle boss mobs last (most complex)

---

**Want more details?**
- Say "extend" for animation complexity breakdown per entity
- Say "report" for complete entity-by-entity analysis
```

---

## When to Use This Skill

### Always use for:
- ✅ Analysis questions ("what files...", "which components...", "how many...")
- ✅ Investigation requests ("check if...", "find...", "look for...")
- ✅ Recommendations ("should we...", "can we remove...", "what's best...")
- ✅ Codebase queries ("where is...", "which classes...", "how does...")
- ✅ Research questions ("what's the difference...", "how does X work...")

### Don't use for:
- ❌ Implementation requests (code changes need context)
- ❌ Complex multi-step tasks (needs detailed planning)
- ❌ User explicitly asks for detailed report
- ❌ Epic/requirements creation (follow epic-requirements skill)
- ❌ Task planning (follow task-planning skill)

---

## File Storage Rules

**Initial response**: NO file created (answer inline)

**If user requests "report"**:
- Create file in `.claude/temp/[descriptive-name].md`
- Use kebab-case naming
- Keep filename descriptive but short
- Tell user file location

**Example**:
```markdown
✅ Detailed report created: `.claude/temp/geckolib-entities-analysis.md`
```

---

## Length Guidelines

**Initial response**: ≤30 lines (quick answer + action items)
**Extended response**: 50-100 lines (inline, more details)
**Full report**: Any length (file in .claude/temp/)

---

## Domain-Specific Examples

### Minecraft Modding

**Question**: "Which villager professions need custom textures?"

**Good Response**:
```markdown
## Custom Textures Needed

**Found 3 custom professions**:

1. **Guard** (`profession/guard.png`)
   - Status: Missing
   - Required: 64x64 PNG
   - Location: assets/xeenaa_villager_manager/textures/entity/villager/profession/

2. **Custom professions from other mods** (check config)

**Action**:
```bash
# Check existing textures
ls src/main/resources/assets/*/textures/entity/villager/profession/
```

---

**Want more details?** Say "extend" for texture creation guide
```

### Performance Investigation

**Question**: "What's causing the lag spikes?"

**Good Response**:
```markdown
## Lag Spike Analysis

**Root cause**: Entity ticking in chunk generation

**Top 3 bottlenecks**:
1. **Pathfinding** - 45% of tick time (entities in unloaded chunks)
2. **Structure placement** - 30% of tick time (too many placement attempts)
3. **Block entity updates** - 15% of tick time (inefficient tile entity logic)

**Quick fixes**:
1. Limit pathfinding range for custom entities
2. Reduce structure placement attempts (already in progress)
3. Optimize block entity tick frequency

---

**Want more details?** Say "report" for full profiling data
```

### Code Structure

**Question**: "Where is the entity registration code?"

**Good Response**:
```markdown
## Entity Registration Location

**Found in**: `src/main/java/com/modid/registry/ModEntities.java`

**Key patterns**:
- Entity type registration: Lines 15-45
- Attribute registration: Lines 50-80
- Spawn placement: Lines 85-120

**Related files**:
- Client renderer: `ModEntityRenderers.java`
- Spawn rules: `BiomeModifications.java`

**To add new entity**:
1. Register EntityType in ModEntities
2. Register attributes in FabricDefaultAttributeRegistry
3. Register renderer in client initializer
```

---

## Key Principles

1. **Respect user's time**: Short answer first
2. **Actionable**: Focus on what to do, not exhaustive why (unless asked)
3. **Progressive disclosure**: Offer more, don't force it
4. **File when detailed**: Long analysis → file, not inline
5. **Structured**: Use clear sections, bullet points, code blocks

---

## When NOT to Be Concise

**These requests need full responses immediately**:

1. **Implementation requests**: "Implement guard attack behavior"
   - Needs full context, code changes, testing

2. **Epic creation**: "/create_epic villager trading"
   - Needs complete requirements (follow epic-requirements skill)

3. **Task planning**: "/create_plan"
   - Needs complete task breakdown (follow task-planning skill)

4. **User explicitly requests details**: "Give me a complete analysis of..."
   - Honor the request

5. **Research documentation**: "/research how villager AI works"
   - Needs comprehensive documentation (follow research-methodology + knowledge-base-management)

---

## Integration with Other Skills

This skill **complements**:
- **research-methodology**: Quick answers from knowledge base vs full research
- **knowledge-base-management**: Quick reference vs comprehensive documentation
- **epic-requirements**: Quick feature overview vs full requirements
- **task-planning**: Quick task summary vs full plan

This skill **does not replace**:
- Epic/requirements creation (those need full detail)
- Task planning (needs complete breakdown)
- Implementation (code needs context)
- Research documentation (knowledge needs comprehensive detail)

---

## Summary

**Default behavior**: Short, actionable, offer extension
**User control**: "extend" for more inline, "report" for file
**File storage**: Only when user requests comprehensive analysis
**Length limits**: 30 lines initial, 50-100 extended, unlimited in files

Remember: Most users just need the answer and next steps. Give them that first.
