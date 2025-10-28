# Epic 32: Crow - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Crow** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Plains, Forest, Taiga
**Key Features**: Tameable, item gathering, shoulder perch

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter crow in plains, forest, taiga
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Tameable, item gathering, shoulder perch

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Plains, Forest, Taiga

---

## Features & Functionality

### Core Features
- Noisy birds
- Damage crops (pest)
- Pick up and eat food
- Scared by carved pumpkins

### Taming
- **Method**: Toss Pumpkin Seeds
- **Benefits**: Stay/follow/wander/gather items, shoulder perching, extra damage to undead

### Special Mechanics
- Carved Pumpkin: Scares crows from fields
- Hay block: Tamed crow sitting on it regains health
- Item gathering: Flies around hay block, gathers items
- Container deposit: Matches item to item frame on container, deposits from that side
- Shoulder perching: Sits on owner's shoulders (follow mode), remove by sneaking
- Extra damage to undead

### Items & Interactions
- **Eats**: Pumpkin Seeds (taming), food items

### AI Behaviors
- **info_0**: Stay/wander/follow/gather modes
- **info_1**: Picks up food
- **info_2**: Flies around hay block
- **info_3**: Deposits items in containers (matching item frames)
- **info_4**: Shoulder perching
- **info_5**: Pecks attackers (low damage, doesn't aggro mobs)
- **info_6**: Extra damage to undead

### Combat
- **info_0**: Pecks attackers (low damage)
- **info_1**: Extra damage to undead


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Crow entity is summonable (`/summon xeenaa-alexs-mobs:crow`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Tameable, item gathering, shoulder perch
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CrowEntity class
- CrowModel class (GeckoLib model)
- CrowRenderer class (GeckoLib renderer)
- Geometry file (crow.geo.json)
- Animation file (crow.animation.json)
- Texture file(s) (crow.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Tameable, item gathering, shoulder perch

### Out of Scope

- Advanced breeding mechanics (unless required)
- Complex equipment systems (unless in features)
- Multiple biome variants (unless specified)

---

## Performance Requirements

**Target Specifications**:
- Support multiple entities in loaded chunks
- No noticeable FPS drop
- Render time: <0.2ms per entity
- Entity tick time: <0.15ms per entity
- Memory: <5KB per entity

---

## Risks & Mitigations

**Potential Risks**:
1. **Complexity**: Medium - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Tameable, item gathering, shoulder perch
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Crow cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CrowEntity.java`
- `CrowModel.java`
- `CrowRenderer.java`

### Resource Files
- `crow.geo.json`
- `crow.animation.json`
- `crow.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 3-5 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Passive type
- [ ] Special features work: Tameable, item gathering, shoulder perch
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Medium
- Special features: Tameable, item gathering, shoulder perch
- Biomes: Plains, Forest, Taiga

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 54

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
