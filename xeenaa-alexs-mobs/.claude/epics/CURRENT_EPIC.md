# Current Epic Status - Xeenaa's Alex's Mobs (Fabric)

**Last Updated**: 2025-10-27

## Epic Overview

| Epic # | Name | Status | Progress | Priority |
|--------|------|--------|----------|----------|
| 001 | Core Framework & GeckoLib Integration | COMPLETE | 12/12 tasks | Critical |
| 002 | Mob Catalog & Asset Inventory | COMPLETE | 8/8 tasks | Critical |
| 003 | Proof of Concept (Mixed) | CLOSED | Chaotic Success | Critical |
| 004 | Fly - Flying Passive Insect | COMPLETE | Retroactive | Baseline |
| 005 | Cockroach - Ground Passive Insect | COMPLETE | Retroactive | Baseline |
| 006 | Triops - Aquatic Passive Crustacean | COMPLETE | Retroactive | Baseline |
| 007 | Crow - Flying Passive Bird | ACTIVE | 1/12 tasks | Medium |

---

## Summary

**Epics 001-002**: Foundation complete (framework, cataloging)
**Epic 003**: Closed as successful but chaotic proof-of-concept
**Epics 004-006**: Retroactive documentation for three completed animals
**Epic 007**: ACTIVE - Fixing bugs and adding features (12 tasks)
**Epics 008-100**: Requirements created, awaiting implementation

---

## Epic 004: Fly ✅ COMPLETE

**Animal**: Flying passive insect
**Completed**: 2025-10-26
**Delivered**:
- FlyEntity with flying AI
- Model with 12 bones (body, head, wings, legs, antennae)
- Animations: idle, fly, death
- Rendering adjustments: scale 0.25×, Y-offset +0.5, flip 180°

---

## Epic 005: Cockroach ✅ COMPLETE

**Animal**: Ground passive insect
**Completed**: 2025-10-26
**Delivered**:
- CockroachEntity with ground movement AI
- Model with 8 bones (body, head, 6 legs, 2 antennae)
- Animations: walk, idle
- Rendering adjustments: scale 0.63×, Y-offset -1.3

---

## Epic 006: Triops ✅ COMPLETE

**Animal**: Aquatic passive crustacean
**Completed**: 2025-10-27
**Delivered**:
- TriopsEntity with swim AI (FishEntity base)
- Model with 10 bones (body, antennae, legs, tail segments, tail flippers)
- Animations: swim, idle
- Rendering adjustments: Y-offset -1.3
- **Key fix**: Animation naming corrected to full format

---

## Epic 007: Crow 🔄 ACTIVE

**Animal**: Flying Passive Bird (Tameable)
**Status**: Bug fixes → Feature completion
**Progress**: 6/16 tasks complete (38%)

**Current State**:
- ✅ Entity, model, animations, renderer implemented
- ✅ TASK-001: Texture rendering fixed (getTextureLocation direct return)
- ✅ TASK-002: Spawn egg created (dark gray/black colors, in creative tab)
- ✅ TASK-001.1: Crow orientation fixed (180° Y-rotation applied)
- ✅ TASK-001.2: Crow body pitch fixed (-90° X-rotation for vertical orientation)
- ✅ TASK-001.3: Crow spawning/underground flight fixed (Y-translation changed from -1.5 to +0.3)
- ✅ TASK-001.4: Dynamic pitch fixed (counters entity pitch to keep model upright)
- ❌ Spawn egg texture missing (reported but not yet task-tracked)
- ❌ Missing taming system (Pumpkin Seeds)
- ❌ Missing command modes (Stay/Wander/Follow/Gather)
- ❌ Missing shoulder perching
- ❌ Missing item gathering automation
- ❌ Missing pest behaviors (crop damage, pumpkin fleeing)

**Plan**: `xeenaa-alexs-mobs/.claude/epics/007-crow-flying-passive-bird/plan.md`

**Last Completed**: TASK-001.4 - Fix Dynamic Pitch ✅
**Next Task**: TASK-003 - Validate Basic Crow Functionality (manual testing required)

---

## Status: Epic 007 In Progress

**3 animals completed**: Fly (004), Cockroach (005), Triops (006)
**1 animal active**: Crow (007) - 12 tasks (bug fixes + features)
**Conversion process validated**: Citadel→GeckoLib workflow proven

---

**All epics properly organized with dedicated folders, requirements, and plans** ✅
