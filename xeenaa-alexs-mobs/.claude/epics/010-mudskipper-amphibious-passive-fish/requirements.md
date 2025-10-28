# Epic 10: Mudskipper - Amphibious Passive Fish

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: High
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Mudskipper** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib. The Mudskipper is a unique amphibious fish that can survive both in water and on land, known for "walking" on its fins and climbing out of water onto beaches and mudflats.

---

## Business Value

### Player Experience Goals

1. **Unique Amphibious Behavior** - Mudskippers bridge aquatic and land environments
   - Players enjoy watching mudskippers leave water and walk on land
   - Creates dynamic beach/shore ecosystems
   - Demonstrates unusual biological adaptation

2. **Interactive Shoreline Content** - Adds life to beach and mangrove biomes
   - Makes shorelines more interesting to explore
   - Creates natural transition zones between water and land
   - Rewards coastal exploration

3. **Educational Value** - Players learn about amphibious fish
   - Real mudskippers can breathe air and "walk" on land
   - Demonstrates evolutionary adaptation
   - Showcases unique locomotion methods

### Game Mechanics

**Entity Type**: Amphibious Passive Fish
**Size**: Small (0.6×0.4×0.6 blocks)
**Behavior**: Passive, swims in water, walks on land, stays near shore
**Health**: 4 HP
**Drops**: None (or small fish item)
**Spawning**: Beaches, mangrove swamps, river edges

---

## Features & Functionality

### Core Features
- Fish that walk on land
- Equally at home on land and water

### Taming
- **Method**: Feed several Lobster Tails
- **Benefits**: Stay/follow/wander, combat support

### Special Mechanics
- Dominance displays: Raise fins and circle each other
- Combat: Spits mud balls inflicting slowness and damage
- Strafing: Can strafe in combat

### Items & Interactions
- **Eats**: Lobster Tails (taming, breeding), Insect larvae (breeding)

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Dominance displays
- **info_2**: Defends owner
- **info_3**: Strafes in combat

### Breeding
- **Item**: Insect larvae or Lobster Tails

### Combat
- **info_0**: Mud ball projectiles (slowness + damage)
- **info_1**: Strafing movement


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Mudskipper entity is summonable (`/summon xeenaa-alexs-mobs:mudskipper`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly (swim, walk, jump, transitions)
- [ ] AI works in BOTH water and land environments
- [ ] Mudskipper can pathfind from water to land and back
- [ ] Stays near shore (doesn't swim to deep ocean or walk inland)
- [ ] Breathing mechanic works (returns to water periodically)
- [ ] Size and positioning look correct in both environments
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MudskipperEntity class with **dual environment logic**
- Custom pathfinding for amphibious movement
- MudskipperModel class (GeckoLib model)
- MudskipperRenderer class (GeckoLib renderer)
- Geometry file (mudskipper.geo.json)
- Animation file (mudskipper.animation.json)
- Texture file (mudskipper.png)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Breathing/moisture mechanic

### Out of Scope

- Breeding mechanics
- Taming/domestication
- Climbing vertical surfaces (just beaches)
- Burrowing in mud
- Special interactions with other mobs
- Multiple variants

---

## User Stories

### Story 1: Amphibious Discovery
**As a** player exploring a beach
**I want** to see mudskippers leaving the water and walking on sand
**So that** I feel surprised and delighted by unique behavior

**Acceptance Criteria**:
- [ ] Mudskippers spawn near shorelines
- [ ] Mudskippers swim in shallow water
- [ ] Mudskippers climb onto beaches
- [ ] Mudskippers walk/hop on land
- [ ] Transition between water and land looks smooth

### Story 2: Realistic Amphibious Behavior
**As a** player observing a mudskipper
**I want** it to behave like a real amphibious fish
**So that** it feels authentic and educational

**Acceptance Criteria**:
- [ ] Mudskipper stays near shore (doesn't swim deep)
- [ ] Mudskipper doesn't wander far inland
- [ ] Mudskipper returns to water periodically
- [ ] Walking animation uses fins realistically
- [ ] Flees to water when threatened

### Story 3: Dynamic Shoreline Ecosystem
**As a** player building near water
**I want** mudskippers to add life to beaches and shores
**So that** coastal areas feel more alive

**Acceptance Criteria**:
- [ ] Multiple mudskippers can coexist
- [ ] Mudskippers interact naturally with environment
- [ ] Behavior changes based on location (water vs land)
- [ ] Creates interesting scenes (fish out of water)

---

## Performance Requirements

**Target Specifications**:
- Support 15+ mudskippers in loaded chunks
- No noticeable FPS drop
- Render time: <0.2ms per mudskipper
- Entity tick time: <0.12ms per mudskipper (complex AI)
- Memory: <5KB per mudskipper
- Pathfinding must be efficient (amphibious is expensive)

---

## Risks & Mitigations

**Potential Risks** (HIGH COMPLEXITY):

1. **Amphibious pathfinding** - Very complex to implement correctly
   - Mitigation: Study vanilla dolphin/turtle AI, may need custom pathfinding
2. **Animation transitions** - Water→land and land→water must be smooth
   - Mitigation: Create dedicated transition animations, use GeckoLib states
3. **AI state management** - Switching between water and land behaviors
   - Mitigation: Use clear state machine, separate goal sets
4. **Breathing mechanic** - Balancing time on land vs water
   - Mitigation: Use timer system, visual cues (panting animation)
5. **Performance** - Dual environment AI is more expensive
   - Mitigation: Optimize pathfinding, limit spawn numbers, efficient goals

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Mudskipper cataloged in mob inventory
- ✅ Epic 03: Conversion process established
- AquaticAnimalEntity base class (may need extension for amphibious)
- Custom pathfinding system for amphibious movement

**Nice to Have**:
- Study Epic 06 (Triops) for aquatic AI
- Research vanilla turtle/dolphin pathfinding

---

## Deliverables

### Code Files
- `MudskipperEntity.java` - Complex dual-environment AI
- `MudskipperModel.java` - GeckoLib model definition
- `MudskipperRenderer.java` - Renderer with state-based transforms
- Custom pathfinding goals (if needed)

### Resource Files
- `mudskipper.geo.json` - Model geometry
- `mudskipper.animation.json` - All animations (many!)
- `mudskipper.png` - Texture (brown/gray patterns)
- Sound files (splash, flop, breathe)

### Documentation
- Amphibious AI architecture
- Pathfinding system documentation
- Animation state machine
- Breathing mechanic specification

---

## Timeline

**Estimated Timeline** (HIGH COMPLEXITY):
- Day 1-2: Convert model, create all animations
- Day 3: Implement basic entity and water AI
- Day 4: Implement land AI and movement
- Day 5: Create amphibious pathfinding system
- Day 6: Implement breathing mechanic and transitions
- Day 7: Add sounds, polish, optimize, validate

**Total Effort**: 5-7 days (most complex animal so far)

---

## Validation & Testing

**Manual Testing Required** (EXTENSIVE):
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally near shorelines
- [ ] Model renders properly
- [ ] Swim animation works in water
- [ ] Walk animation works on land
- [ ] Jump animation works on land
- [ ] Transitions between water/land are smooth
- [ ] Stays near shore (doesn't swim deep)
- [ ] Stays near water (doesn't walk inland)
- [ ] Returns to water periodically
- [ ] Flees to water when threatened
- [ ] Sounds play appropriately
- [ ] Size and scale correct in both environments
- [ ] Performance acceptable
- [ ] Pathfinding doesn't glitch
- [ ] No stuck-in-blocks issues

**Automated Testing**: TBD (after manual validation)

---

## Future Enhancements

**Potential Improvements**:
1. Burrowing in mud/sand
2. Climbing on logs or small structures
3. Breeding mechanics
4. Territory defense (aggressive to other mudskippers)
5. Feeding on small insects (if on land)
6. Variants (different patterns/colors)
7. Aquarium compatibility

---

## Notes

**Key Considerations**:
- **This is the most complex animal due to amphibious nature**
- Pathfinding in two environments is challenging
- Animation state machine must be robust
- Breathing mechanic must be balanced (fun, not annoying)
- Performance optimization is critical
- May require significant research and iteration

**References**:
- Study vanilla turtle AI (amphibious)
- Study vanilla dolphin AI (water pathfinding)
- Review Epic 06 (Triops) for aquatic AI patterns
- Research custom pathfinding implementations

**Development Strategy**:
1. Start with water-only implementation
2. Add land-only implementation
3. Combine and add transitions
4. Add breathing mechanic last
5. Extensive testing at each stage

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START** (HIGH COMPLEXITY - Plan carefully!)
