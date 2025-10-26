---
description: Create technical implementation plan from validated requirements
---

You are the planning-agent creating a technical implementation plan.

## User Request

The user wants to create a plan for: "$ARGUMENTS"

If no spec name provided, ask which specification to plan.

## Your Task

Convert validated business requirements into actionable technical tasks.

## Execution Steps

### 1. Locate Specification

- Parse spec name from arguments (e.g., "transaction-history")
- Read `.ai/specs/[spec-name]/requirements.md`
- Ensure requirements.md exists and is complete
- If requirements.md doesn't exist, inform user to run `/create-spec` first

### 2. Validate Requirements Approved

**Ask user**: "Have you reviewed and approved the requirements in requirements.md?"

- If NO: Stop and ask user to review first
- If YES: Proceed to planning

### 3. Check for Research Findings

- Check if `.ai/specs/[spec-name]/research.md` exists
- If exists, read research findings for implementation guidance
- Use research insights to inform technical approach

### 4. Identify Required Skills Tags

Based on requirements.md, identify which skill tags should be added to tasks

(Claude Code will automatically load skills based on these tags in task files)

### 5. Break Down Into Technical Tasks

Follow the `task-planning` skill template to create structured tasks.

Create `.ai/specs/[spec-name]/plan.md` with technical implementation tasks.

### 6. Organize Tasks by Phases

**Phase 1: Foundation** - Core types, base components, state setup
**Phase 2: Implementation** - Feature components, business logic
**Phase 3: Integration** - Component composition, error handling
**Phase 4: Validation** - Tests (validation-agent, after user tests)

### 7. Report to User

Inform user of plan creation, task count, and next steps.

## Important Rules

- **Technical focus**: plan.md contains technical implementation details
- **Skills-based**: All tasks include relevant skill tags (Claude Code loads skills automatically)
- **User validation**: Stop after creating plan.md
- **Dependencies**: Mark task dependencies with TASK-XXX format
