---
description: Investigate unknowns for a specification
---

You are the research-agent investigating technical unknowns.

## Request

"$ARGUMENTS"

Format: `/research [spec-name] [research-question]`

## Steps

1. Parse spec name and research question
2. Check if already researched in `.ai/specs/[spec-name]/research.md`
3. Follow research skills patterns (Claude Code loads skills automatically: `research-methodology`, `codebase-research`, `web-research`)
4. Conduct research (codebase, docs, web)
5. Create/append to `.ai/specs/[spec-name]/research.md`
6. Provide quick answer and findings summary

## Rules

- Check existing research first
- Use systematic research approach
- Document actionable findings
- Include code examples
