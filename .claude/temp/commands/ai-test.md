---
description: Run AI infrastructure test suite to verify agents, commands, and skills
---

You are the ai-agent running the infrastructure test suite.

## User Request

"$ARGUMENTS"

## Your Task

Execute the AI infrastructure test suite to verify that agents, commands, and skills are working correctly.

## Test Overview

This test suite validates:
1. **spec-agent** creates proper requirements.md following spec-requirements skill
2. **planning-agent** creates proper plan.md following task-planning skill
3. **Skills auto-loading** - No manual Read/Glob on .claude/skills/
4. **Agent boundaries** - Each agent respects its scope
5. **Output quality** - Files follow skill templates

## Execution Steps

### 1. Prepare Test Environment

- Read `.ai/ai-tests/test-plan.md` for test strategy
- Read `.ai/ai-tests/test-data/test-ticket.md` for test feature
- Create timestamp for this test run
- Initialize test log

### 2. Copy Test Ticket to .ai/ticket.md

- Copy content from `.ai/ai-tests/test-data/test-ticket.md`
- Write to `.ai/ticket.md` to trigger spec-agent

### 3. Test Case 1: Spec-Agent

**Execute**: Invoke spec-agent to create specification

**What to invoke**:
```
Use Task tool with:
- subagent_type: "spec-agent"
- description: "Test spec-agent with counter component"
- prompt: "Create specification for counter component from .ai/ticket.md"
```

**Monitor**:
- Does spec-agent create `.ai/specs/counter/requirements.md`?
- Does it follow spec-requirements skill template?
- Does it mention "Following spec-requirements skill"?
- Are there any Read/Glob calls to .claude/skills/?
- Does it include business value, user stories, UX design?
- Is language non-technical and stakeholder-friendly?

**Record Results**:
- Files created: [list]
- Skills mentioned: [list]
- Manual loading detected: [yes/no]
- Template compliance: [yes/no]
- Quality score: [1-10]

### 4. Test Case 2: Planning-Agent

**Execute**: Invoke planning-agent to create implementation plan

**What to invoke**:
```
Use Task tool with:
- subagent_type: "planning-agent"
- description: "Test planning-agent plan creation"
- prompt: "Create implementation plan from .ai/specs/counter/requirements.md. User has approved the requirements."
```

**Monitor**:
- Does planning-agent create `.ai/specs/counter/plan.md`?
- Does it follow task-planning skill template?
- Does it mention "Following task-planning skill"?
- Are tasks numbered (TASK-001, TASK-002, etc.)?
- Does each task have Skills & Resources section?
- Are skills identified correctly (react, suil, standards)?
- Are there any Read/Glob calls to .claude/skills/?

**Record Results**:
- Files created: [list]
- Skills mentioned: [list]
- Task structure: [compliant/non-compliant]
- Manual loading detected: [yes/no]
- Quality score: [1-10]

### 5. Test Case 3: Verify Skills Auto-Loading

**Analyze**: Review all tool calls from Test Cases 1 and 2

**Check**:
- Count Read tool calls to `.claude/skills/` paths
- Count Glob tool calls with pattern matching skills
- Verify agents mentioned skills naturally
- Confirm outputs match skill patterns

**Record Results**:
- Read calls to skills: [count]
- Glob calls for skills: [count]
- Skills loading: [automatic/manual]
- Evidence of skill patterns: [list examples]

### 6. Test Case 4: Agent Boundaries

**Analyze**: Review agent behavior

**Check**:
- spec-agent only created requirements.md (not plan.md or code)
- planning-agent only created plan.md (not code)
- No agent crossed into another's territory
- Proper handoff between agents

**Record Results**:
- Boundary violations: [count]
- Examples: [list if any]
- Boundaries respected: [yes/no]

### 7. Generate Test Report

Create `.ai/ai-tests/test-results/test-report-[timestamp].md` with:

```markdown
# AI Infrastructure Test Report

**Test Run**: [timestamp]
**Test Feature**: Counter Component
**Duration**: [duration]

## Executive Summary

[PASS/FAIL] - [Summary sentence]

## Test Results

### Test 1: Spec-Agent
- Status: [PASS/FAIL]
- Files created: [list]
- Skills mentioned: [list]
- Manual loading: [yes/no]
- Template compliance: [yes/no]
- Quality: [score]/10
- Issues: [list or "None"]

### Test 2: Planning-Agent
- Status: [PASS/FAIL]
- Files created: [list]
- Skills mentioned: [list]
- Manual loading: [yes/no]
- Template compliance: [yes/no]
- Quality: [score]/10
- Issues: [list or "None"]

### Test 3: Skills Auto-Loading
- Status: [PASS/FAIL]
- Read calls to skills: [count]
- Glob calls to skills: [count]
- Skills loading: [automatic/manual]
- Issues: [list or "None"]

### Test 4: Agent Boundaries
- Status: [PASS/FAIL]
- Boundary violations: [count]
- All boundaries respected: [yes/no]
- Issues: [list or "None"]

## Overall Assessment

**Pass Rate**: [X/4 tests passed]

**Infrastructure Status**: [Working/Issues Found]

## Detailed Findings

### What Worked Well
[List successes]

### Issues Found
[List problems with severity]

### Skills Evidence
[Examples of skill patterns being followed]

## Recommendations

[List of improvements if needed, or "No changes needed"]

## Conclusion

[Overall assessment and next steps]
```

### 8. Clean Up Test Artifacts

**Clean spec directory**:
- Remove `.ai/specs/counter/` directory
- Keep test results in `.ai/ai-tests/test-results/`

**Reset ticket.md**:
- Clear `.ai/ticket.md` back to template

### 9. Report to User

**Provide summary**:
```
✅ AI Infrastructure Test Suite Complete

Test Feature: Counter Component
Duration: [X] seconds
Pass Rate: [X/4]

Results:
- spec-agent: [PASS/FAIL]
- planning-agent: [PASS/FAIL]
- Skills auto-loading: [PASS/FAIL]
- Agent boundaries: [PASS/FAIL]

Detailed report: .ai/ai-tests/test-results/test-report-[timestamp].md

[Overall assessment]

Next steps: [recommendations]
```

## Important Rules

- **Complete all steps** - Don't stop until test suite is done
- **Monitor tool usage** - Watch for Read/Glob on .claude/skills/
- **Record everything** - Capture all observations
- **Objective assessment** - Score based on actual behavior
- **Clean up after** - Remove test artifacts

## Success Criteria

**PASS Requirements**:
- spec-agent creates proper requirements.md
- planning-agent creates proper plan.md
- No manual skills loading (0 Read/Glob to .claude/skills/)
- All agent boundaries respected
- Outputs follow skill templates

**If ANY test fails**:
- Document the failure clearly
- Suggest specific fixes
- Mark overall status as ISSUES FOUND

## You Are Testing the System

This is a meta-level test of the AI infrastructure. Be thorough, objective, and precise in your assessment.
