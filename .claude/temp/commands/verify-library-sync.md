---
description: Verify library code is in sync with skills and README documentation
tags: [project]
---

You are verifying that a library's code, skill file, and README are in sync.

## Task

The user has modified code in a library under `libs/` and wants to verify that:
1. Library README is up-to-date with code changes
2. Skill file reflects current API and patterns
3. Examples in skill work with current code
4. Skills mapping is accurate

## Instructions

### Step 1: Identify Library

**Parse user input**:
- User may provide library name: `/verify-library-sync yoda-form`
- Or ask to verify current changes: `/verify-library-sync` (detect from git status)

**Identify the library**:
```
Library path: libs/[library-name]/
Skill path: .claude/skills/libraries/[library-name]/
README path: libs/[library-name]/README.md
```

**Verify library has a skill**:
- Check if `.claude/skills/libraries/[library-name]/` exists
- If no skill exists, inform user and exit (nothing to sync)

### Step 2: Analyze Recent Changes

**Check git changes** (if library name not provided):
```bash
git status
git diff libs/[library-name]/
```

**Identify change type**:
- API changes (exports, function signatures, component props)
- Behavior changes (same API, different behavior)
- New features (new exports, components, hooks)
- Breaking changes (removed/changed existing API)
- Bug fixes (no API change)
- Refactoring (internal only)

### Step 3: Compare Library Code with Skill

**Read key files**:
1. Library's main exports: `libs/[library-name]/src/index.ts`
2. Skill file: `.claude/skills/libraries/[library-name]/SKILL.md`
3. README: `libs/[library-name]/README.md`

**Check for discrepancies**:
- [ ] Are exported functions/hooks/components documented in skill?
- [ ] Do code examples in skill match current API?
- [ ] Are function signatures in README accurate?
- [ ] Do props/parameters in examples match current types?
- [ ] Are new features mentioned in skill and README?
- [ ] Are deprecated features removed from docs?

### Step 4: Verify Examples

**Read examples directory**:
`.claude/skills/libraries/[library-name]/examples/`

**Check each example**:
- [ ] Imports match current exports
- [ ] Component props match current API
- [ ] Hook parameters match current signature
- [ ] Code patterns follow current best practices
- [ ] Examples demonstrate new features (if any)

### Step 5: Check Skills Mapping

**Read**: `.github/skills-tags-mapping.json`

**Verify library entry**:
- [ ] Description matches skill frontmatter
- [ ] Tags include key concepts
- [ ] Path is correct

### Step 6: Generate Report

Create verification report:

```markdown
# Library-Skill Sync Report

**Library**: [library-name]
**Date**: [timestamp]
**Changes Detected**: [API/Behavior/New Feature/etc.]

## Sync Status

### README vs Code
- ✅/❌ API documentation matches code
- ✅/❌ Examples use current API
- ✅/❌ New features documented
- ✅/❌ Deprecated features removed

### Skill vs Code
- ✅/❌ Core patterns documented
- ✅/❌ Code examples work
- ✅/❌ Hook/function signatures match
- ✅/❌ Component props match

### Examples
- ✅/❌ All examples use current API
- ✅/❌ Imports are correct
- ✅/❌ New features have examples

### Skills Mapping
- ✅/❌ Description accurate
- ✅/❌ Tags complete
- ✅/❌ Path correct

## Issues Found

[List any discrepancies or outdated content]

## Recommendations

[What needs to be updated to bring docs in sync]

## Overall Status

✅ **IN SYNC** - All documentation matches current code
⚠️  **NEEDS UPDATES** - [X] items need updating
❌ **OUT OF SYNC** - Major discrepancies found
```

### Step 7: Provide Actionable Guidance

If issues found:
1. List specific files to update
2. Provide exact sections to modify
3. Suggest example updates
4. Reference `.ai/library-sync-checklist.md` for guidance

If everything in sync:
1. Confirm all documentation is accurate
2. No action needed

## Important Notes

**What to check**:
- API signatures (functions, hooks, components)
- Props and parameters
- Import paths
- Code examples
- Usage patterns
- New features
- Deprecated features

**What NOT to check**:
- Internal implementation details (not in public API)
- Private functions (not exported)
- Dev dependencies
- Build configuration

**Focus on**:
- Public API surface
- Developer-facing documentation
- AI agent guidance accuracy

## Libraries with Skills

Check these 12 libraries:
- yoda-form
- sygnum-table
- sygnum-query
- sygnum-store
- suil
- sygnum-themes
- sygnum-testing
- sygnum-access
- sygnum-csv
- sygnum-stepper
- sygnum-charts
- sygnum-idp

## Exit Conditions

**Exit with success** if:
- Library has no skill (inform user, no sync needed)
- Everything is in sync
- Issues identified with clear action items

**Exit with error** if:
- Library doesn't exist
- Cannot read required files
- Git errors prevent analysis

## Output Format

Always provide:
1. **Summary** - Quick status (in sync / needs updates / out of sync)
2. **Details** - Specific findings for each section
3. **Action Items** - What to update (if anything)
4. **Reference** - Link to `.ai/library-sync-checklist.md` for detailed guidance

---

**Remember**: This verification helps ensure AI agents have accurate library guidance!
