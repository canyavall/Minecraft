# Epic 03: Proof of Concept - First Three Animals

**Epic Number**: 03
**Epic Name**: Proof of Concept - First Three Animals
**Status**: PLANNING
**Created**: 2025-10-26
**Priority**: Critical
**Estimated Effort**: Small (120-180 hours over 1-2 weeks)

---

## Executive Summary

After establishing the GeckoLib framework (Epic 01) and cataloging all 90+ animals (Epic 02), we now validate the entire porting workflow by implementing the three simplest animals from Alex's Mobs: **Fly**, **Cockroach**, and **Triops**. This proof of concept validates our GeckoLib-based approach, establishes reusable patterns, and builds confidence before tackling the remaining 87 animals.

**Business Value**: De-risks the 14-18 month porting roadmap by validating core assumptions early. Success proves the framework works; blockers discovered now are cheaper to fix than after porting 30+ mobs.

---

## Problem Statement

### Current Situation

We have:
- ✅ **Epic 01 Complete**: GeckoLib framework with test_animal entity
- ✅ **Epic 02 Complete**: 90 animals cataloged with 13-epic roadmap

However:
- ❌ **No real animals ported yet** - Only test_animal (proof of framework, not real mob)
- ❌ **GeckoLib unproven with actual Alex's Mobs animals** - Citadel → GeckoLib conversion untested
- ❌ **Asset recreation workflow undefined** - No Blockbench templates, no texture pipeline
- ❌ **Porting time estimates unvalidated** - Epic 02 estimates (40-60h per simple mob) are theoretical
- ❌ **AI behavior patterns not established** - Each mob will need custom AI goals

### Why This Matters

**Without proof of concept:**
- We might discover GeckoLib limitations after porting 10+ mobs (costly rework)
- Asset recreation might take 3x longer than estimated (roadmap invalidated)
- Unknown blockers could delay the 60-75 week timeline
- No reusable templates means reinventing the wheel for each of 87 remaining mobs

**With proof of concept:**
- ✅ Validate GeckoLib can handle real Alex's Mobs animals
- ✅ Establish asset creation pipeline (Blockbench → GeckoLib → Fabric)
- ✅ Create reusable templates (quadruped, flying, aquatic base classes)
- ✅ Measure actual porting time vs. estimates
- ✅ Identify and fix blockers early when scope is minimal

---

## Goals & Success Criteria

### Primary Goals

1. **Port 3 ultra-simple animals** - Fly, Cockroach, Triops (flying, land, aquatic coverage)
2. **Validate GeckoLib workflow** - Prove Citadel → GeckoLib conversion works end-to-end
3. **Establish asset pipeline** - Create Blockbench templates, texture workflow, sound integration
4. **Measure actual effort** - Track hours to validate Epic 02 estimates (40-60h per simple mob)
5. **Create reusable patterns** - Base entity classes, AI goal templates, animation templates

### Success Criteria

**Epic is COMPLETE when:**

- ✅ **All 3 animals fully functional**:
  - Spawn naturally in correct biomes (or via spawn egg)
  - AI behaviors work (wander, flee, basic interactions)
  - Animations play correctly (idle, walk/fly/swim, death)
  - Textures render properly (no z-fighting, correct resolution)
  - Sounds play on correct events (ambient, hurt, death)
  - Loot drops on death (following vanilla loot table system)
  - No crashes or critical bugs

- ✅ **GeckoLib validation passed**:
  - All 3 movement types work (flying, land, aquatic)
  - Animations sync with AI behaviors
  - Models render at correct scale and orientation
  - No performance issues with 10+ entities spawned

- ✅ **Reusable templates created**:
  - Blockbench templates for common body types
  - Base entity classes (FlyingAnimalEntity, LandAnimalEntity, AquaticAnimalEntity)
  - AI goal templates (WanderGoal, FleeGoal, BreedGoal patterns)
  - Resource structure (textures, models, animations, sounds, loot)

- ✅ **Effort tracking complete**:
  - Actual hours tracked per mob (model, animation, code, testing)
  - Compare actuals vs. Epic 02 estimates
  - Adjust roadmap if needed

- ✅ **Documentation created**:
  - Porting workflow guide (how to port next animal)
  - Blockbench tutorial (how to create GeckoLib models)
  - Lessons learned document (what worked, what didn't)

### Out of Scope

- ❌ Advanced AI (taming, riding, inventory) - Deferred to complex mobs
- ❌ Special features (item pickup, transformations) - Deferred to medium/complex mobs
- ❌ Texture variants (baby, biome-specific colors) - Just 1 texture per mob for now
- ❌ Multiple animations beyond basics - Just idle, walk/fly/swim, death
- ❌ Item drops beyond loot tables - No custom items/ethical drops yet

---

## User Stories

### Story 1: Player Discovers First Animals

**As a** Minecraft player using the Fabric mod
**I want** to encounter realistic animals (flies, cockroaches, triops) in the world
**So that** the game world feels more alive and diverse

**Acceptance Criteria**:
- Animals spawn naturally in appropriate biomes (or can be spawned via eggs)
- Animals have realistic behaviors:
  - **Fly**: Flies around randomly, lands occasionally, flees from players
  - **Cockroach**: Scurries on ground, hides in dark places, runs from light/players
  - **Triops**: Swims in water, burrows in sand/gravel, passive behavior
- Animals make ambient sounds occasionally
- Animals drop simple loot when killed (leather, raw food, etc.)

**Game Experience**:
- **Immersion**: Animals behave like real creatures, not static decorations
- **Discovery**: Players notice animals in different environments (air, land, water)
- **Interaction**: Players can observe, hunt, or avoid animals based on preference

---

### Story 2: Developer Ports Next Animal

**As a** developer porting the 87 remaining animals
**I want** established templates and workflows
**So that** I can port animals efficiently without reinventing patterns

**Acceptance Criteria**:
- Blockbench templates exist for common body types (quadruped, biped, fish, insect)
- Base entity classes handle common behaviors (movement, AI, sounds)
- Asset structure is documented (where to put textures, models, animations, sounds)
- Porting guide explains step-by-step process (research → model → animate → code → test)

**Developer Experience**:
- **Efficiency**: Can start porting new animal in <30 minutes using templates
- **Consistency**: All animals follow same patterns (code style, resource structure)
- **Quality**: Templates enforce best practices (performance, null safety, comments)

---

### Story 3: Validator Confirms GeckoLib Works

**As a** project validator (user or QA tester)
**I want** proof that GeckoLib can handle Alex's Mobs animals
**So that** I have confidence the 14-18 month roadmap is achievable

**Acceptance Criteria**:
- All 3 movement types work (flying, land, aquatic)
- No crashes or critical bugs with GeckoLib rendering
- Performance is acceptable (60+ FPS with 20+ animals spawned)
- Animations are smooth (no jittering, no frame skipping)

**Validation Experience**:
- **Confidence**: Clear evidence GeckoLib is viable solution
- **Risk Mitigation**: Blockers discovered early, not after 6 months
- **Go/No-Go Decision**: Can confidently proceed to Epic 04+ or pivot if needed

---

## Game Mechanics & Design

### Mob Behaviors (Simplified for Proof of Concept)

#### 1. Fly (Flying Passive)

**AI Goals** (4 total - ultra-simple):
1. **WanderGoal**: Fly randomly in 3D space (like bat/parrot)
2. **LandGoal**: Occasionally land on blocks, rest for 2-10 seconds
3. **FleeGoal**: Flee from players within 4 blocks
4. **LookAroundGoal**: Look at nearby entities when landed

**Animations** (3 total):
- `idle` - Wings at rest (when landed)
- `fly` - Wing flapping (continuous loop)
- `death` - Fall from sky, spin

**Textures**: 1 texture (simple insect)
**Sounds**: ambient_fly.ogg, hurt.ogg, death.ogg
**Loot**: Nothing (or 1 string with 10% chance)
**Spawn**: Any biome, light level > 7

**Size**: Tiny (0.3 x 0.2 x 0.3 blocks)

---

#### 2. Cockroach (Land Passive)

**AI Goals** (3 total - ultra-simple):
1. **WanderGoal**: Scurry on ground in random directions
2. **FleeGoal**: Run from players within 5 blocks
3. **AvoidLightGoal**: Prefer dark areas (light level < 7)

**Animations** (3 total):
- `idle` - Antennae twitch
- `walk` - Legs scuttle (fast movement)
- `death` - Flip on back, legs curl

**Textures**: 1 texture (brown cockroach)
**Sounds**: scuttle.ogg, squish.ogg (death)
**Loot**: Nothing (too small)
**Spawn**: Dark places, caves, under trees

**Size**: Tiny (0.4 x 0.2 x 0.4 blocks)

---

#### 3. Triops (Aquatic Passive)

**AI Goals** (3 total - ultra-simple):
1. **SwimGoal**: Swim in water (like fish)
2. **BurrowGoal**: Burrow in sand/gravel when stationary
3. **AvoidPlayersGoal**: Flee from players within 3 blocks

**Animations** (3 total):
- `idle` - Gentle wiggle
- `swim` - Tail undulates, legs paddle
- `death` - Float to surface

**Textures**: 1 texture (prehistoric shrimp)
**Sounds**: splash.ogg, hurt.ogg
**Loot**: 1 raw cod with 30% chance (or nothing)
**Spawn**: Rivers, swamps, warm oceans

**Size**: Small (0.5 x 0.3 x 0.5 blocks)

---

### Why These 3 Mobs?

**Movement Type Coverage**:
- ✅ **Flying** (Fly) - Validates 3D pathfinding, aerial AI
- ✅ **Land** (Cockroach) - Validates ground movement, standard AI
- ✅ **Aquatic** (Triops) - Validates swimming, underwater rendering

**Complexity Justification**:
- ✅ **Ultra-simple AI** - 3-4 goals each (minimal testing surface)
- ✅ **Minimal animations** - 3 animations each (fast to create in Blockbench)
- ✅ **No special features** - No taming, riding, inventory, transformations
- ✅ **Tiny models** - Small hitboxes, simple geometry (easy to model)
- ✅ **Low risk** - If these fail, we discover issues early; if these succeed, we prove viability

**Strategic Value**:
- ✅ **Fast iteration** - 40-60 hours per mob = 120-180 hours total (1-2 weeks)
- ✅ **Workflow validation** - Tests entire pipeline from research → playable mob
- ✅ **Template creation** - Each mob creates reusable patterns for similar mobs
- ✅ **Effort calibration** - Actual time tracked to validate Epic 02 estimates

---

## Technical Considerations

### GeckoLib Integration

**Requirements**:
1. Create 3 `.geo.json` models in Blockbench
2. Create 3 `.animation.json` files (3 animations × 3 mobs = 9 total animations)
3. Create 3 entity classes extending `BaseAnimalEntity` (from Epic 01)
4. Create 3 model classes extending `GeoModel<T>`
5. Create 3 renderer classes extending `GeoEntityRenderer<T>`
6. Register all 3 entities in `ModEntities.java`
7. Register all 3 renderers in `AlexsMobsClient.java`

**Validation Points**:
- Models load without errors
- Animations play at correct speed and loop properly
- Textures apply correctly (no UV mapping issues)
- Entities render at correct scale and orientation
- No z-fighting or transparency issues

---

### Asset Creation Workflow

**Blockbench Pipeline**:
1. Research original Alex's Mobs design (reference screenshots/videos)
2. Create GeckoLib model in Blockbench
   - Use appropriate body type template (insect, quadruped, fish)
   - Keep geometry simple (5-15 cubes per mob)
   - Use proper bone hierarchy (parent-child relationships)
3. Create animations in Blockbench
   - Idle: Subtle movement (2-4 second loop)
   - Walk/Fly/Swim: Movement cycle (1-2 second loop)
   - Death: 1-2 second play-once animation
4. Export `.geo.json` and `.animation.json` to `assets/xeenaa-alexs-mobs/geo/` and `.../animations/`
5. Create texture in image editor (16x16 or 32x32 simple texture)
6. Export `.png` to `assets/xeenaa-alexs-mobs/textures/entity/`

**Sound Integration**:
- Source royalty-free sounds (Freesound.org, or record custom)
- Convert to `.ogg` format (Audacity or ffmpeg)
- Place in `assets/xeenaa-alexs-mobs/sounds/`
- Register in `ModSounds.java`
- Hook to entity events (ambient, hurt, death)

**Loot Tables**:
- Create JSON in `data/xeenaa-alexs-mobs/loot_tables/entities/`
- Use Minecraft 1.21.1 syntax (validated from Epic 01)
- Keep simple (1-2 items max, basic drop chances)

---

### Code Patterns (Reusable Templates)

**Base Entity Classes** (created in this epic):

```java
// FlyingAnimalEntity.java - For all flying mobs
public abstract class FlyingAnimalEntity extends BaseAnimalEntity {
    // 3D pathfinding navigation
    // Landing behavior
    // Flee-upward AI
}

// LandAnimalEntity.java - For all land mobs (already exists as BaseAnimalEntity)
public abstract class LandAnimalEntity extends BaseAnimalEntity {
    // Ground pathfinding
    // Standard wander/flee
}

// AquaticAnimalEntity.java - For all swimming mobs
public abstract class AquaticAnimalEntity extends BaseAnimalEntity {
    // Swimming navigation
    // Water-only survival
    // Float on death
}
```

**AI Goal Templates**:
- `WanderInAirGoal` - For flying mobs (reusable for birds)
- `FleeFromLightGoal` - For cockroach (reusable for cave mobs)
- `BurrowInSandGoal` - For triops (reusable for burrowing animals)

**Renderer Templates**:
- Standard `GeoEntityRenderer` setup (already established in Epic 01)
- Texture layering pattern (for future mobs with variants)

---

### Performance Targets

**Entity Tick Performance**:
- Each mob should tick in < 0.05ms (same as Epic 01 baseline)
- 20 mobs spawned = < 1ms total tick time
- No pathfinding lag (flying mobs especially critical)

**Rendering Performance**:
- Each mob should render in < 0.1ms (Epic 01 baseline)
- 20 mobs visible = < 2ms total render time
- 60+ FPS maintained on mid-range hardware

**Memory Usage**:
- < 5 KB per entity instance
- Model/texture caching works (no redundant loading)

---

## Deliverables

### 1. Three Fully Functional Mobs

**Fly** (`FlyEntity.java`):
- ✅ Entity class with 4 AI goals
- ✅ GeckoLib model with 3 animations
- ✅ Texture (16x16 or 32x32)
- ✅ 3 sound files
- ✅ Loot table (minimal)
- ✅ Spawn egg
- ✅ Registered in ModEntities

**Cockroach** (`CockroachEntity.java`):
- ✅ Entity class with 3 AI goals
- ✅ GeckoLib model with 3 animations
- ✅ Texture (16x16 or 32x32)
- ✅ 2 sound files
- ✅ Loot table (none)
- ✅ Spawn egg
- ✅ Registered in ModEntities

**Triops** (`TriopsEntity.java`):
- ✅ Entity class with 3 AI goals
- ✅ GeckoLib model with 3 animations
- ✅ Texture (16x16 or 32x32)
- ✅ 2 sound files
- ✅ Loot table (minimal)
- ✅ Spawn egg
- ✅ Registered in ModEntities

---

### 2. Reusable Base Classes

**Location**: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/`

1. **FlyingAnimalEntity.java** - Base for all flying mobs (10 future mobs)
2. **AquaticAnimalEntity.java** - Base for all swimming mobs (15 future mobs)
3. *(LandAnimalEntity = BaseAnimalEntity already exists from Epic 01)*

---

### 3. Blockbench Templates

**Location**: `docs/blockbench-templates/`

1. **insect_template.bbmodel** - For small 6-legged creatures (fly, cockroach, beetles)
2. **fish_template.bbmodel** - For aquatic creatures (triops, small fish)
3. **bird_template.bbmodel** - For flying creatures (will be useful in Epic 04+)

---

### 4. Documentation

**Location**: `docs/epic-03/`

1. **PORTING_WORKFLOW.md** - Step-by-step guide to port next animal
2. **BLOCKBENCH_GUIDE.md** - How to create GeckoLib models and animations
3. **LESSONS_LEARNED.md** - What worked, what didn't, improvements for Epic 04+
4. **EFFORT_TRACKING.md** - Actual hours per mob vs. estimates

---

### 5. Effort Tracking Spreadsheet

**Format**: Markdown table in `EFFORT_TRACKING.md`

| Mob | Model (h) | Animations (h) | Code (h) | Testing (h) | Total (h) | Estimate (h) | Variance |
|-----|-----------|----------------|----------|-------------|-----------|--------------|----------|
| Fly | [actual] | [actual] | [actual] | [actual] | [actual] | 40-60 | [%] |
| Cockroach | [actual] | [actual] | [actual] | [actual] | [actual] | 40-60 | [%] |
| Triops | [actual] | [actual] | [actual] | [actual] | [actual] | 40-60 | [%] |

**Use**: Adjust Epic 04+ estimates based on actuals

---

## Testing Strategy

### Manual Testing Checklist (Per Mob)

**Spawn Testing**:
- [ ] Mob spawns via spawn egg (creative mode)
- [ ] Mob appears at correct size and orientation
- [ ] Texture renders correctly (no missing textures)
- [ ] No crashes on spawn

**AI Testing**:
- [ ] Mob wanders naturally (not stuck or spinning)
- [ ] Mob flees from player when approached
- [ ] Mob executes special behaviors (landing, burrowing, etc.)
- [ ] Pathfinding works (doesn't get stuck in walls/terrain)

**Animation Testing**:
- [ ] Idle animation plays when stationary
- [ ] Movement animation plays when moving
- [ ] Death animation plays on death
- [ ] Animations loop correctly (no freezing)
- [ ] Animation speed matches movement speed

**Sound Testing**:
- [ ] Ambient sound plays occasionally (every 30-60s)
- [ ] Hurt sound plays when damaged
- [ ] Death sound plays on death
- [ ] Volume is appropriate (not too loud/quiet)

**Loot Testing**:
- [ ] Loot drops on death (if applicable)
- [ ] Loot matches loot table definition
- [ ] No duplicate drops or errors

**Performance Testing**:
- [ ] Spawn 20 mobs - no FPS drop
- [ ] Spawn 50 mobs - acceptable performance (30+ FPS)
- [ ] No memory leaks after 10 minutes
- [ ] No console errors/warnings

---

### Automated Testing (If Time Permits)

**Unit Tests** (low priority for proof of concept):
- Test entity registration
- Test AI goal initialization
- Test model/renderer registration

**Integration Tests** (manual for now):
- All 3 mobs work simultaneously
- Mobs interact with vanilla entities (no conflicts)
- Mobs work in multiplayer (server-client sync)

---

## Dependencies

### Prerequisites

- ✅ **Epic 01 Complete** - GeckoLib framework exists
- ✅ **Epic 02 Complete** - Mobs researched and cataloged
- ✅ **Blockbench Installed** - With GeckoLib plugin
- ✅ **Sound Editing Tool** - Audacity or ffmpeg (for .ogg conversion)
- ✅ **Image Editor** - For textures (GIMP, Aseprite, Paint.NET, or Photoshop)

### External Dependencies

**Mod Dependencies** (already in Epic 01):
- Fabric API (latest for 1.21.1)
- GeckoLib 4.x (Fabric version)

**Development Tools**:
- Blockbench (free, open-source model editor)
- GeckoLib Blockbench plugin (for .geo.json export)

**Asset Sources**:
- Freesound.org (royalty-free sounds)
- Original Alex's Mobs (reference only, NOT extracting assets)

---

## Risks & Mitigation

### Risk 1: GeckoLib Limitations

**Risk**: GeckoLib cannot replicate Citadel animations (especially flying mobs)
**Probability**: Low (15%) - GeckoLib is mature and widely used
**Impact**: High - Would invalidate entire 14-18 month roadmap
**Mitigation**:
- Test flying mob (Fly) first - highest risk
- If blockers found, evaluate alternatives (custom animation system, Animated Java)
- Epic 03 is specifically designed to catch this early
**Contingency**: Pivot to alternative animation library or defer flying mobs

---

### Risk 2: Asset Creation Time Underestimated

**Risk**: Creating models/animations takes 2-3x longer than estimated (40-60h → 120-180h per mob)
**Probability**: Medium (30%) - First time using Blockbench for GeckoLib
**Impact**: Medium - Would extend 14-18 month roadmap to 24-36 months
**Mitigation**:
- Track actual time meticulously
- Create templates to speed up future mobs
- Improve Blockbench skills through practice (learning curve)
**Contingency**: Adjust Epic 04+ estimates, reduce scope (defer complex mobs), or recruit asset creators

---

### Risk 3: Sound Licensing Issues

**Risk**: Cannot find/create suitable royalty-free sounds
**Probability**: Low (10%) - Many free sound libraries exist
**Impact**: Low - Mobs work without sounds (just less immersive)
**Mitigation**:
- Use Freesound.org (Creative Commons sounds)
- Record custom sounds if needed (foley)
- Simplify sound design (fewer sounds per mob)
**Contingency**: Ship without sounds initially, add in Epic 04+

---

### Risk 4: AI Behavior Bugs

**Risk**: Mobs exhibit broken AI (spinning, getting stuck, crashing)
**Probability**: Medium (25%) - Custom AI goals often have edge cases
**Impact**: Medium - Delays testing, requires debugging
**Mitigation**:
- Start with simplest AI (wander, flee only)
- Test frequently (spawn mob every 30 min of dev time)
- Use vanilla AI goals where possible (less custom code)
**Contingency**: Simplify AI further if needed (remove problematic goals)

---

### Risk 5: Performance Issues

**Risk**: Mobs cause FPS drops or lag with 20+ spawned
**Probability**: Low (10%) - These are ultra-simple mobs
**Impact**: Medium - Would require optimization work
**Mitigation**:
- Profile early and often (F3 debug screen)
- Use simple models (< 20 cubes per mob)
- Optimize pathfinding (reduce tick rate for distant mobs)
**Contingency**: Reduce model complexity, simplify animations, cap spawn counts

---

## Performance Considerations

**Entity Tick Budget**:
- Target: < 0.05ms per entity (20 entities = 1ms total)
- Measure: F3 debug screen, profiler (if available)
- Optimize: Reduce pathfinding frequency, simplify AI goals

**Rendering Budget**:
- Target: < 0.1ms per entity (20 entities = 2ms total)
- Measure: FPS counter, render profiler
- Optimize: LOD (reduce detail for distant mobs), frustum culling

**Memory Budget**:
- Target: < 5 KB per entity instance
- Measure: JVM memory profiler
- Optimize: Avoid unnecessary object allocation, reuse AI goal instances

---

## User Validation Required

Before proceeding to `/create_plan`, user should review:

1. **Mob Selection**:
   - Confirm Fly, Cockroach, Triops are the right choices
   - Suggest alternatives if preferred (other simple mobs from Epic 02)

2. **Scope Validation**:
   - Approve simplified behaviors (no advanced AI for proof of concept)
   - Confirm 3 animations per mob is sufficient (not full animation set)
   - Approve basic loot tables (no custom items yet)

3. **Timeline Expectations**:
   - 120-180 hours total (1-2 weeks) is acceptable
   - Epic 04 can start immediately after Epic 03 (no breaks)

4. **Documentation Priority**:
   - Confirm need for Blockbench guide and porting workflow
   - Suggest any additional documentation (video tutorials, etc.)

5. **Success Criteria**:
   - Any adjustments to acceptance criteria
   - Performance targets realistic

---

## Next Steps (After User Approval)

1. User reviews and validates this requirements document
2. User runs `/create_plan` to generate technical tasks
3. Planning-agent creates `plan.md` with 15-20 tasks
4. User runs `/next` to begin executing tasks
5. Implementation-agent ports mobs one at a time (Fly → Cockroach → Triops)
6. User validates each mob in-game before marking task complete
7. Epic marked complete, proceed to Epic 04 (Simple Passive Land & Flying - 8 mobs)

---

## Notes

- This epic is **HIGH RISK, HIGH VALUE** - De-risks entire 14-18 month roadmap
- **Fast iteration** is key - Don't overengineer, just prove it works
- **Template creation** is critical - Saves 100+ hours in Epic 04+
- **Effort tracking** validates roadmap - Be honest about time spent
- **Blockers found early** are GOOD - Better now than after Epic 10

---

**Status**: PLANNING - Awaiting user validation before creating plan.md

**Estimated Timeline**:
- Planning: 2-4 hours (creating plan.md)
- Implementation: 120-180 hours (porting 3 mobs)
- **Total**: 1-2 weeks

**Next Milestone**: Epic 04 (8 simple mobs) - Builds on Epic 03 templates
