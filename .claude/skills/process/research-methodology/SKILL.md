---
name: research-methodology
description: Systematic approach to researching unknown problems, documenting findings, and creating research files in epic-specific folders ({{project}}/.claude/epics/##-epic-name/research/task-XXX/). Use when investigating how systems work, analyzing other mods, or researching technical solutions.
allowed-tools: [WebFetch, WebSearch, Read, Grep, Glob, Write]
---

# Research Methodology Skill

Systematic approach for the research-agent to investigate unknown problems and document findings.

**IMPORTANT**: For research organization and file storage patterns, follow the **epic-research-organization** skill (Claude Code loads it automatically). This skill focuses on the PROCESS of research, not WHERE to store it.

## Research Process

### 1. Define Research Question

Start with a clear, specific question:
- ✅ "How does Fabric handle dynamic entity texture switching for villagers?"
- ✅ "What is the packet structure for syncing custom entity data in 1.21.1?"
- ❌ "How do entities work?" (too broad)
- ❌ "Fix the rendering" (not a research question)

### 2. Check Existing Research

**ALWAYS check first** before starting new research:

```bash
# Check epic-specific research folders
ls {{project}}/.claude/epics/##-epic-name/research/task-*/
grep -r "texture" {{project}}/.claude/epics/##-epic-name/research/

# Check legacy global research (older projects)
ls {{project}}/.claude/research/ | grep -i "texture"
grep -r "entity.*texture" {{project}}/.claude/research/
```

**If research exists**:
- Read it and provide summary to user
- Only extend if information is outdated or incomplete

**If research doesn't exist**:
- Proceed with new research
- Store in epic-specific folder (see epic-research-organization skill)

### 3. Research Methods

Use these methods in order of reliability:

#### A. Official Documentation (Highest Reliability)
- **Fabric Wiki**: https://fabricmc.net/wiki/
- **Fabric API JavaDocs**: Check via IDE or genSources
- **Minecraft Wiki**: https://minecraft.wiki/ for vanilla mechanics

```bash
# Use WebFetch for documentation
WebFetch("https://fabricmc.net/wiki/tutorial:entity")
```

#### B. Source Code Analysis (Very Reliable)
- **Yarn Mappings**: Use `/gradlew genSources` to browse decompiled Minecraft code
- **Fabric API Source**: Check implementation in Fabric API
- **Project Code**: Search existing codebase patterns

```bash
# Search codebase for patterns
grep -r "EntityRenderer" src/
grep -r "getTexture" src/
```

#### C. Successful Mod Implementations (Reliable)
- **Open Source Mods**: Check how successful mods solve similar problems
- **GitHub Search**: Search for pattern implementations
- **Mod Examples**: Look at well-maintained mods

```bash
# Search for mod examples
WebSearch("Fabric 1.21.1 custom entity texture github")
```

#### D. Community Resources (Moderate Reliability)
- **Stack Overflow**: For specific technical questions
- **Reddit /r/fabricmc**: Community discussions
- **Fabric Discord**: Real-time help (check pins)

#### E. Experimental Testing (Validate Uncertain Findings)
- Create minimal test case
- Document what you tried
- Record observations
- Validate assumptions

### 4. Document Findings

**Storage Location**: Follow **epic-research-organization** skill for WHERE to store research.

**Quick Reference**:
- Epic-specific: `{{project}}/.claude/epics/##-epic-name/research/task-XXX/[filename].md`
- Legacy global: `{{project}}/.claude/research/[filename].md` (deprecated, use epic folders for new research)

**Filename Format**:
- Use kebab-case: `villager-texture-system.md`
- Be specific: `guard-pathfinding-optimization.md`
- Searchable: Include key terms others would search for

## Research Document Template

```markdown
# [Research Topic]

**Researched**: [Date]
**Minecraft Version**: 1.21.1
**Fabric API Version**: [Version if relevant]
**Related Epic**: [Epic ##] - [Epic Name]
**Related Task**: [TASK-XXX if applicable]

## Research Question

[Clear statement of what needed to be discovered]

## Summary (For Agents)

**Quick Answer**: [One-paragraph summary that directly answers the question]

**Key Findings**:
- [Finding 1 - actionable information]
- [Finding 2 - actionable information]
- [Finding 3 - actionable information]

**Recommended Approach**: [Specific implementation recommendation in 2-3 sentences]

---

## Detailed Findings

### Background

[Context and why this research was needed]

### Investigation Methods

[What was researched: documentation, code analysis, testing, etc.]

### Discoveries

#### [Topic Area 1]

[Detailed findings with code examples, links, or observations]

```java
// Code examples demonstrating findings
public class Example {
    // Annotated code showing the pattern
}
```

#### [Topic Area 2]

[More detailed information]

### Tested Solutions

[If experimental testing was done, document results]

**Test 1**: [Description]
- **Approach**: [What was tried]
- **Result**: [What happened]
- **Conclusion**: [What was learned]

**Test 2**: [Description]
- **Approach**: [What was tried]
- **Result**: [What happened]
- **Conclusion**: [What was learned]

### Known Limitations

[Any constraints, edge cases, or gotchas discovered]
- Limitation 1
- Limitation 2

### Performance Considerations

[If applicable, document performance implications]
- Performance consideration 1
- Performance consideration 2

## Implementation Guidance

### Recommended Approach

[Step-by-step guidance for implementing based on findings]

1. [Step 1 with specific details]
2. [Step 2 with specific details]
3. [Step 3 with specific details]

### Code Examples

```java
// Practical code examples showing recommended implementation
public class RecommendedPattern {
    public void implementation() {
        // Well-commented example code
    }
}
```

### Pitfalls to Avoid

- **Pitfall 1**: [Description and how to avoid]
- **Pitfall 2**: [Description and how to avoid]
- **Pitfall 3**: [Description and how to avoid]

## References

- [Link to Fabric documentation]
- [Link to relevant GitHub issues/PRs]
- [Link to mod examples]
- [File paths in codebase]: `src/path/to/relevant/file.java`

## Related Research

[Links to other research files in .claude/research/ that relate to this topic]
- [Related research file 1]
- [Related research file 2]

## Questions for Further Research

[Any unanswered questions or areas needing deeper investigation]
- Question 1
- Question 2

---

**Research Status**: ✅ Complete / ⏸️ Partial / 🔄 Ongoing
**Confidence Level**: High / Medium / Low
**Last Updated**: [Date]
```

## Quality Standards

### Research Must Be:

**Reproducible**:
- Include steps to reproduce findings
- Link to exact documentation versions
- Specify tool versions used

**Versioned**:
- Always note Minecraft version (1.21.1)
- Specify Fabric API version if relevant
- Note Java version if it matters

**Cited**:
- Link to all sources
- Credit mod authors if using their patterns
- Include file paths for code references

**Practical**:
- Focus on actionable information
- Include "what to do" not just "what we learned"
- Provide implementation guidance

**Well-Organized**:
- Use clear headings
- Include code examples
- Separate summary from details

## Executive Summary for User

After saving research, provide immediate summary:

```
Research complete! ✅

Findings saved to: {{project}}/.claude/epics/##-epic-name/research/task-XXX/[filename].md

Quick Answer: [Direct answer to their question]

Key Findings:
- [Finding 1]
- [Finding 2]
- [Finding 3]

Recommended Approach: [2-3 sentence implementation recommendation]

Full details available in research file for reference by implementation-agent.
```

## Research Index Management

If `{{project}}/.claude/research/INDEX.md` exists, add entry:

```markdown
- **[Topic]** (`filename.md`) - [One-line description] - [Date]
```

If INDEX.md doesn't exist, optionally create it:

```markdown
# Research Index

Catalog of all research conducted for this project.

## Research Files

- **[Topic 1]** (`topic-1.md`) - [Description] - 2025-01-15
- **[Topic 2]** (`topic-2.md`) - [Description] - 2025-01-16
```

## When to Research vs Implement

### ✅ Research When:
- You don't know how a system works
- Multiple implementation approaches exist
- Need to find best practices
- Investigating a new Fabric API feature
- Analyzing how other mods solve a problem
- Performance optimization requires data

### ❌ Don't Research When:
- Standard implementation is known
- Following established patterns from standards.md
- Simple feature with clear approach
- Time-sensitive bug fix (implement, then research if needed)

## Avoiding Redundant Research

### Before Starting:
1. Check `{{project}}/.claude/research/` for existing files
2. Read INDEX.md if it exists
3. Search for keywords in research folder
4. Check related epics for research references

### When Updating:
- Update existing file instead of creating new one
- Add "Last Updated" date
- Increment version if major changes
- Note what changed in a comment

## Research Types

### **Investigative Research**
Purpose: Understand how something works
Example: "How does Fabric handle entity rendering?"

### **Solution Research**
Purpose: Find best way to implement a feature
Example: "Best pattern for custom villager professions?"

### **Performance Research**
Purpose: Optimize existing implementation
Example: "How to reduce entity tick overhead?"

### **Compatibility Research**
Purpose: Ensure mod compatibility
Example: "How do other mods handle guard AI?"

### **Troubleshooting Research**
Purpose: Debug mysterious behavior
Example: "Why does texture not update on client?"

## When to Use This Skill

Use this skill when:
- User asks "How does X work?"
- User requests "Research how to implement Y"
- Encountering unknown unknowns during implementation
- Investigating performance issues
- Analyzing other mod implementations
- Creating technical documentation
- The command `/research` is invoked
- implementation-agent needs information before coding

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used research-methodology
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used research-methodology`

This helps track which skills are actively consulted and identifies documentation gaps.
