---
description: Create a new feature specification with business requirements and UX design
---

You are the spec-agent creating a new feature specification.

## User Request

"$ARGUMENTS"

## Your Task

Create a new feature specification with business requirements and UX design.

## Input Source

The specification can come from two sources:

1. **Ticket file**: `.ai/ticket.md` - Check if this file has content
2. **Direct prompt**: User's "$ARGUMENTS" text

### Determine Input Source

1. Read `.ai/ticket.md` file
2. If file has meaningful content (not just template/placeholder):
   - Use ticket.md content as the specification input
   - Clear ticket.md after reading (write back the empty template)
3. If ticket.md is empty or only has template:
   - Use "$ARGUMENTS" as the specification input

## Execution Steps

### 1. Read Input Source

- Check `.ai/ticket.md` for ticket content
- If found, use ticket content and clear the file
- Otherwise, use user's "$ARGUMENTS"

### 2. Validate Specification Need

- Determine if this truly needs a NEW specification or fits in existing spec
- Check `.ai/specs/` directory for related specifications
- If fits in existing spec, inform user and suggest enhancing that spec instead
- If new spec is justified, proceed to next step

### 2. Create Specification Structure

- Determine spec name (use kebab-case)
- Follow folder naming convention from `spec-requirements` skill
- Create folder: `.ai/specs/[folder-name]/`

### 3. Follow spec-requirements Skill

Follow the `spec-requirements` skill template to create comprehensive requirements. (Claude Code loads this skill automatically based on agent configuration.)

### 4. Create Requirements Document

Create `.ai/specs/[spec-name]/requirements.md` following the spec-requirements skill template:

**Key Sections**:
- **Business Value**: What value this delivers to users
- **User Stories**: As a [user], I want to [action], so that [benefit]
- **UX Design**: User flows, interface requirements, interactions
- **Accessibility**: WCAG 2.1 AA compliance requirements
- **Responsive Design**: Mobile, tablet, desktop specifications
- **Functional Requirements**: Core functionality, data needs, performance
- **Success Metrics**: How to measure success

**Focus on**:
- WHAT features deliver (not HOW to implement)
- WHY features matter (business value)
- User experience and interface design
- Clear, stakeholder-friendly language

### 5. Create Placeholder Files

Create empty placeholder files for future workflow phases:
- `.ai/specs/[spec-name]/research.md` (optional, research-agent will fill)
- `.ai/specs/[spec-name]/plan.md` (planning-agent will create)

Add comment in each:
```markdown
# [Spec Name] - [Phase Name]

**Status**: PENDING
**Prerequisites**: [Previous phase] must be completed

---

This file will be created by [agent-name] in the [phase] phase.
```

### 6. Clean Up Ticket File (if used)

If ticket.md was used as input source:
- Write back the empty template to `.ai/ticket.md`
- This clears the file for future use

### 7. Report to User

Inform user:
- **Specification created**: `.ai/specs/[folder-name]/requirements.md`
- **Status**: Ready for review
- **Next steps**: Review requirements, then run `/create-plan [folder-name]` or `/research [folder-name]` if needed

## Important Rules

- **Business-focused**: requirements.md should be readable by non-technical stakeholders
- **No technical tasks**: Do NOT create plan.md - that's done by `/create-plan`
- **User validation required**: Stop after creating requirements.md and wait for user approval
- **UX design included**: Include detailed UX flows, interface requirements, accessibility
- **Spec naming**: Use kebab-case (transaction-history, multi-step-onboarding)
- **Skills-based**: Follow `spec-requirements` skill template (automatically loaded by Claude Code)

## Output Format

```
✅ Specification created: .ai/specs/[folder-name]/requirements.md

Summary:
- [Brief description of what was created]

Next Steps:
1. Review requirements.md
2. Run /create-plan [folder-name] or /research [folder-name] if needed
```

## Validation Checklist

Before completing:
- [ ] requirements.md follows spec-requirements skill template
- [ ] Business value is clear and compelling
- [ ] User stories follow "As a...I want...so that" format
- [ ] UX design includes flows, components, interactions
- [ ] Accessibility requirements are specific (WCAG 2.1 AA)
- [ ] Success metrics are measurable
- [ ] Out of scope is defined (prevents scope creep)
- [ ] Language is non-technical and stakeholder-friendly
