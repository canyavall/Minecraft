# Knowledge Vault Optimization Guide

**Version**: 2.0
**Last Updated**: 2025-11-01
**Purpose**: Guide for using the enhanced knowledge infrastructure with automatic context loading

---

## Overview

The knowledge vault has been enhanced with three medium-term optimization strategies to improve knowledge discovery and automatic context loading:

1. **Enhanced Index Structure** - Better categorization and tag taxonomy
2. **Project Knowledge Cache** - Pre-computed project→knowledge mappings
3. **Context-Aware Task Preloading** - Automatic knowledge loading during workflows

These optimizations eliminate redundant knowledge searches and ensure agents always have relevant information.

---

## Feature 1: Enhanced Index Structure (v2.0)

### What Changed

The `knowledge-index.json` has been upgraded from v1.0 to v2.0 with:

**New Top-Level Sections**:
- `metadata`: Version tracking, update timestamps, entry counts
- `taxonomy`: Formal category definitions and organized tag groups

**Enhanced Entry Fields**:
- `category`: Primary category (minecraft, fabric, library, graphics, performance)
- `relevance.projects`: Which projects benefit from this knowledge
- `relevance.topics`: Semantic topic classifications
- `relevance.difficulty`: Content complexity (reference, intermediate, advanced)

### Structure Example

```json
{
  "version": "2.0",
  "metadata": {
    "last_updated": "2025-11-01",
    "total_entries": 5,
    "schema_version": "2.0"
  },
  "taxonomy": {
    "categories": [
      {
        "id": "minecraft",
        "name": "Minecraft Systems",
        "description": "Core Minecraft game systems and mechanics"
      }
    ],
    "tag_groups": {
      "entities": ["entities", "mobs", "ai", "pathfinding", "brain-system"],
      "graphics": ["rendering", "textures", "animations", "models"]
    }
  },
  "entries": [
    {
      "path": "minecraft/villagers.md",
      "title": "Minecraft Villager System",
      "category": "minecraft",
      "tags": ["minecraft", "villagers", "professions"],
      "relevance": {
        "projects": ["xeena-village-manager"],
        "topics": ["entities", "ai-systems"],
        "difficulty": "intermediate"
      }
    }
  ]
}
```

### Benefits

- **Faster Search**: Filter by category before searching tags
- **Better Discovery**: Tag groups help find related concepts
- **Project Filtering**: Quickly find project-relevant knowledge
- **Difficulty Levels**: Understand content complexity upfront

### Usage for Agents

**When searching knowledge**:
```
1. Read knowledge-index.json
2. Filter by category if known (e.g., "graphics" for rendering tasks)
3. Search tags within category
4. Check relevance.projects for project-specific knowledge
5. Note difficulty level for prioritization
```

**When adding knowledge**:
```
1. Assign appropriate category
2. Add 4-8 tags from taxonomy tag_groups
3. Specify relevant projects (if any)
4. List semantic topics
5. Set difficulty level
6. Update metadata.total_entries
```

---

## Feature 2: Project Knowledge Cache

### What It Is

**File**: `.claude/knowledge/project-cache.json`

A pre-computed mapping of each project to its most relevant knowledge entries, with priority levels indicating importance.

### Structure

```json
{
  "version": "1.0",
  "cache": {
    "xeenaa-alexs-mobs": {
      "description": "Port of Alex's Mobs from Forge to Fabric",
      "relevant_knowledge": [
        {
          "path": "libraries/porting-forge-to-fabric.md",
          "priority": "critical",
          "reason": "Core porting guide for Forge→Fabric migration"
        },
        {
          "path": "graphics/flying-entity-control.md",
          "priority": "high",
          "reason": "Flying entity implementation (crows, eagles, etc.)"
        },
        {
          "path": "fabric-api/registry.md",
          "priority": "medium",
          "reason": "Fabric-specific registration patterns"
        }
      ],
      "common_tags": ["porting", "entities", "geckolib", "animations"]
    }
  }
}
```

### Priority Levels

| Level | Meaning | When to Use |
|-------|---------|-------------|
| **critical** | Must read for ANY work on this project | Core project concepts, essential APIs |
| **high** | Highly relevant, read for MOST tasks | Common patterns, frequently used systems |
| **medium** | Useful reference, read when applicable | Optional patterns, helpful background |
| **low** | Background information, optional | Nice-to-know, rarely needed |

### Benefits

- **Instant Context**: No need to search index every time
- **Prioritized Loading**: Load critical knowledge first
- **Consistent Context**: All agents get same baseline knowledge
- **Performance**: Single cache read vs. multiple index searches

### Maintenance Workflow

**When to Update**:
1. New project created → Add project entry
2. New knowledge added → Add to relevant project(s)
3. Project scope changes → Adjust priorities
4. Knowledge becomes outdated → Remove or downgrade priority

**How to Update**:

**Add New Project** (during `/create_project`):
```json
"new-project-name": {
  "description": "From project.md vision",
  "relevant_knowledge": [],
  "common_tags": []
}
```

**Add Knowledge to Project** (when creating universal knowledge):
```json
{
  "path": "category/new-knowledge.md",
  "priority": "high",
  "reason": "Why this matters to THIS project"
}
```

**Adjust Priorities** (as project evolves):
- Track which knowledge gets used most
- Upgrade frequently referenced entries to "high"
- Downgrade rarely used entries to "medium" or "low"
- Remove irrelevant entries entirely

---

## Feature 3: Context-Aware Task Preloading

### What It Is

Automatic loading of relevant knowledge during task execution (`/next` command), eliminating manual knowledge searches.

### How It Works

**Step 1: Project Cache Loading** (planning-agent)

When `/next` is called:
```
1. Read .claude/knowledge/project-cache.json
2. Find active project's entry
3. Collect all "critical" and "high" priority knowledge paths
4. Note the reason for each knowledge entry
```

**Step 2: Task-Specific Matching** (planning-agent)

Based on task description:
```
1. Extract keywords from task (e.g., "flying entity", "animation", "GeckoLib")
2. Search knowledge-index.json for matching tags
3. Find knowledge entries with overlapping tags
4. Add highly relevant entries not in project cache
5. Record which tags matched
```

**Step 3: Context Assembly** (planning-agent)

Prepare knowledge context for worker agent:
```
## Relevant Knowledge Base Entries

**Critical Priority**:
- .claude/knowledge/[path] - [reason from cache]

**High Priority**:
- .claude/knowledge/[path] - [reason from cache]

**Task-Specific** (matched tags: tag1, tag2):
- .claude/knowledge/[path] - [matched tags]

Read these knowledge files BEFORE starting implementation.
```

**Step 4: Knowledge Handoff** (planning-agent → worker agent)

When invoking implementation-agent or research-agent:
```javascript
Task({
  subagent_type: "implementation-agent",
  prompt: `
    [Task description]

    ${knowledgeContext}

    [Rest of task context]
  `
})
```

**Step 5: Agent Reads Knowledge** (worker agent)

Worker agent receives prompt with knowledge context:
```
1. See "Relevant Knowledge Base Entries" section
2. Read all critical and high priority files first
3. Skim task-specific entries if needed
4. Proceed with implementation using knowledge
```

### Benefits

- **Automatic Discovery**: No manual knowledge searching
- **Consistent Context**: Every task gets relevant knowledge
- **Time Savings**: Agents don't waste time searching
- **Better Quality**: Agents have right information upfront
- **Context Limits**: Only load relevant knowledge, not entire vault

### Configuration

**Enable/Disable** (in `/next` command):

Currently **ENABLED by default**. To disable, remove Steps 3.1 and 3.2 from `.claude/commands/next.md`.

**Adjust Thresholds**:

Edit `/next` command to change which priorities are auto-loaded:
```markdown
For each knowledge entry with priority "critical" or "high":
```

Change to `"critical"` only for minimal context, or add `"medium"` for comprehensive context.

---

## Workflow Integration

### For research-agent

**When Creating New Knowledge**:

1. Research and document as usual
2. Add entry to `knowledge-index.json` with enhanced fields:
   ```json
   {
     "category": "graphics",
     "relevance": {
       "projects": ["xeenaa-alexs-mobs"],
       "topics": ["flying-entities", "animations"],
       "difficulty": "advanced"
     }
   }
   ```
3. Update `project-cache.json` for relevant projects:
   ```json
   {
     "path": "graphics/flying-entity-control.md",
     "priority": "high",
     "reason": "Essential for flying mob implementation"
   }
   ```

### For planning-agent

**During `/next` Command**:

1. **Load project cache** (Step 3.1):
   ```
   Read .claude/knowledge/project-cache.json
   Find ${activeProject} entry
   Collect critical + high priority paths
   ```

2. **Match task keywords** (Step 3.2):
   ```
   Parse task description for keywords
   Search knowledge-index.json tags
   Find matching entries
   ```

3. **Build knowledge context** (Step 3.3):
   ```
   Format as "Relevant Knowledge Base Entries"
   List by priority
   Include reasons and matched tags
   ```

4. **Pass to worker agent** (Step 4):
   ```javascript
   Task({
     prompt: `${knowledgeContext}\n\n${taskContext}`
   })
   ```

### For implementation-agent

**When Starting Task**:

1. **Read knowledge context** (in task prompt):
   ```
   Look for "Relevant Knowledge Base Entries" section
   ```

2. **Load critical knowledge FIRST**:
   ```
   Read all critical priority files completely
   ```

3. **Load high priority knowledge**:
   ```
   Read high priority files relevant to task
   ```

4. **Skim task-specific knowledge**:
   ```
   Scan task-specific entries if task mentions related concepts
   ```

5. **Proceed with implementation**:
   ```
   Use knowledge to inform implementation decisions
   Reference knowledge files in code comments when applicable
   ```

---

## Maintenance and Best Practices

### Index Maintenance

**Quarterly Review**:
- Update `metadata.last_updated` timestamp
- Verify `metadata.total_entries` count
- Check that all entries have enhanced fields
- Ensure taxonomy tag_groups are comprehensive
- Remove outdated entries or mark as deprecated

**When Adding Entry**:
- Follow enhanced schema exactly
- Include all required fields (category, relevance)
- Use tags from taxonomy tag_groups
- Update metadata.total_entries
- Cross-reference related knowledge

### Cache Maintenance

**When Creating Project**:
- Add project entry to cache immediately
- Include description from project.md
- Start with empty relevant_knowledge array
- List expected common_tags based on project scope

**When Adding Knowledge**:
- Determine which projects benefit
- Assign appropriate priority level
- Write clear reason explaining relevance
- Update all applicable project entries

**Periodic Review** (monthly):
- Track which knowledge is actually used
- Adjust priorities based on usage
- Remove entries that proved irrelevant
- Add missing entries discovered during work

**Cache Validation**:
```bash
# Verify all paths exist
jq -r '.cache[].relevant_knowledge[].path' project-cache.json | \
  while read path; do
    test -f ".claude/knowledge/$path" || echo "Missing: $path"
  done

# Verify all projects exist
jq -r '.cache | keys[]' project-cache.json | \
  while read project; do
    test -d "$project" || echo "Unknown project: $project"
  done
```

### Context Preloading Tuning

**Monitor Performance**:
- Track how often task-specific matching finds useful knowledge
- Measure context size (knowledge entries loaded per task)
- Collect feedback on whether agents had right information

**Adjust Thresholds**:
- If agents miss knowledge → Add more to cache or lower priority threshold
- If agents overloaded → Increase threshold to critical-only or adjust priorities
- If task matching too noisy → Refine tag taxonomy or matching algorithm

**Optimize Tag Matching**:
- Improve tag_groups to cluster related concepts
- Add synonyms to tags (e.g., "animations" + "animation" + "animated")
- Weight tags by specificity (specific tags match before general tags)

---

## Troubleshooting

### Issue: Agent didn't have relevant knowledge

**Symptoms**: Agent asks about system already documented in knowledge base

**Diagnosis**:
1. Check if knowledge exists in index
2. Check if knowledge in project cache
3. Check priority level (is it "low"?)
4. Check if task keywords match knowledge tags

**Solutions**:
- Add knowledge to project cache if missing
- Increase priority if too low
- Add more tags to knowledge entry
- Update common_tags in project cache

### Issue: Agent overloaded with too much knowledge

**Symptoms**: Agent takes long time to read context, mentions context limits

**Diagnosis**:
1. Check how many knowledge entries loaded
2. Check if many "medium" priority entries
3. Check if task-specific matching too broad

**Solutions**:
- Downgrade non-essential knowledge to "low"
- Remove irrelevant entries from cache
- Refine tag matching to be more specific
- Increase threshold to critical+high only

### Issue: Project cache out of sync with index

**Symptoms**: Cache references non-existent knowledge paths

**Diagnosis**:
```bash
# Check for broken paths
jq -r '.cache[].relevant_knowledge[].path' project-cache.json | \
  while read path; do
    test -f ".claude/knowledge/$path" || echo "Broken: $path"
  done
```

**Solutions**:
1. Find broken entries in cache
2. Check if knowledge was renamed/moved
3. Update cache paths to match current index
4. Remove entries if knowledge was deleted

### Issue: Task-specific matching not finding knowledge

**Symptoms**: Agent doesn't get knowledge that clearly matches task

**Diagnosis**:
1. Check task keywords
2. Check knowledge entry tags
3. Look for synonym mismatches (e.g., "animate" vs "animation")

**Solutions**:
- Add more tags to knowledge entry
- Expand tag_groups in taxonomy
- Use broader tags in knowledge entries
- Add task keyword as tag to knowledge

---

## Performance Metrics

### Measuring Success

**Knowledge Discovery Rate**:
- Before: Agents manually search index for every task
- After: Agents automatically receive relevant knowledge 95%+ of time

**Context Load Time**:
- Before: 2-3 manual Read operations to find knowledge
- After: 1 cache read + 0-1 task-specific search

**Knowledge Relevance**:
- Target: 80%+ of loaded knowledge used during task
- Measure: Track which knowledge files agents actually reference

**Cache Hit Rate**:
- Target: 90%+ of tasks satisfied by project cache alone
- Measure: Track how often task-specific matching needed

### Optimization Goals

**Short-term** (1-2 months):
- All projects have cache entries
- All knowledge has enhanced index fields
- Context preloading works reliably

**Medium-term** (3-6 months):
- Cache priorities tuned based on actual usage
- Tag taxonomy expanded with new concepts
- Task-specific matching accuracy improved

**Long-term** (6+ months):
- Automatic cache updates when knowledge added
- Intelligent priority recommendations
- Usage analytics for knowledge discovery patterns

---

## Migration from v1.0

If you have existing knowledge base in v1.0 format:

### Step 1: Upgrade Index

1. Add metadata section
2. Add taxonomy section with categories and tag_groups
3. For each entry, add:
   - `category` field
   - `relevance` object with projects, topics, difficulty

### Step 2: Create Project Cache

1. Create `project-cache.json`
2. Add entry for each project
3. Review knowledge index and assign to projects
4. Set initial priorities (start conservative with "medium")

### Step 3: Update Commands

1. Update `/next` command with Steps 3.1 and 3.2
2. Update planning-agent to read project cache

### Step 4: Update Skills

1. Update knowledge-base-management skill with new sections
2. Document new workflows for research-agent
3. Add cache maintenance procedures

### Step 5: Update Documentation

1. Update CLAUDE.md with new features
2. Create this OPTIMIZATION_GUIDE.md
3. Update README.md to reference new files

---

## Examples

### Example 1: Adding Flying Entity Knowledge

**Scenario**: Just researched flying entity control for Alex's Mobs project.

**Step 1: Add to index**:
```json
{
  "path": "graphics/flying-entity-control.md",
  "title": "Flying Entity Control in Minecraft with GeckoLib",
  "category": "graphics",
  "tags": ["flying-entities", "geckolib", "rotation", "animation"],
  "relevance": {
    "projects": ["xeenaa-alexs-mobs"],
    "topics": ["flying-entities", "rendering", "animations"],
    "difficulty": "advanced"
  }
}
```

**Step 2: Add to project cache**:
```json
{
  "path": "graphics/flying-entity-control.md",
  "priority": "high",
  "reason": "Essential for implementing flying mobs (crows, eagles, etc.)"
}
```

**Result**: Next time a task mentions "flying" or "crow", this knowledge auto-loads.

### Example 2: Planning Agent Context Loading

**Scenario**: User runs `/next` for task "Implement crow entity with flying behavior".

**planning-agent workflow**:

1. **Load project cache**:
   ```
   Read project-cache.json
   Find xeenaa-alexs-mobs entry
   Collect critical + high priority:
   - libraries/porting-forge-to-fabric.md (critical)
   - minecraft/alexs-mobs-reference.md (critical)
   - graphics/flying-entity-control.md (high)
   ```

2. **Match task keywords**:
   ```
   Task: "Implement crow entity with flying behavior"
   Keywords: crow, entity, flying, behavior

   Search index for tags: flying-entities, entities, ai, behavior
   Found match: graphics/flying-entity-control.md (already in cache)
   Found match: minecraft/entities.md (not in cache, add as task-specific)
   ```

3. **Build context**:
   ```markdown
   ## Relevant Knowledge Base Entries

   **Critical Priority**:
   - .claude/knowledge/libraries/porting-forge-to-fabric.md - Core porting guide
   - .claude/knowledge/minecraft/alexs-mobs-reference.md - Mob catalog

   **High Priority**:
   - .claude/knowledge/graphics/flying-entity-control.md - Flying entity patterns

   **Task-Specific** (matched tags: entities, ai):
   - .claude/knowledge/minecraft/entities.md - Entity system and AI
   ```

4. **Invoke implementation-agent** with this context.

**Result**: implementation-agent receives all relevant knowledge automatically.

---

## Future Enhancements

### Planned Features

**Short-term**:
- Automatic cache validation tool
- Usage tracking to measure knowledge relevance
- Cache diff tool to see what changed

**Medium-term**:
- Intelligent priority recommendations based on usage
- Automatic cache updates when knowledge added
- Knowledge versioning for Minecraft version compatibility

**Long-term**:
- Machine learning for tag matching
- Semantic search for knowledge discovery
- Automatic knowledge consolidation suggestions

---

## Summary

The knowledge vault optimization provides:

1. **Enhanced Index** - Better categorization, taxonomy, and metadata
2. **Project Cache** - Pre-computed project→knowledge mappings
3. **Auto-loading** - Context-aware knowledge preloading in workflows

**Benefits**:
- ✅ Faster knowledge discovery
- ✅ Automatic context loading
- ✅ Consistent agent knowledge
- ✅ Reduced manual searching
- ✅ Better task quality

**Next Steps**:
1. Ensure all knowledge has enhanced index fields
2. Populate project cache for all projects
3. Monitor context preloading effectiveness
4. Refine priorities based on actual usage

For detailed workflows, see **knowledge-base-management** skill.

---

**Questions?** See `.claude/knowledge/README.md` or consult the knowledge-base-management skill.
