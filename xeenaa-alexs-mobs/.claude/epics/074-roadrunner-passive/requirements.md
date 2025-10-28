# Epic 74: Roadrunner - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Roadrunner** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Desert, Badlands
**Key Features**: Fast movement, meep variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter roadrunner in desert, badlands
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Fast movement, meep variant

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert, Badlands

---

## Features & Functionality

### Core Features
- Medium-sized bird
- Extremely quick and nimble
- Natural predator of Rattlesnakes

### Special Mechanics
- Feather crafting: Boots allowing fast running on sand

### Items & Interactions
- **Drops**: Roadrunner Feathers
- **Eats**: Rattlesnakes
- **Crafted**: Roadrunner Boots (fast sand running)

### AI Behaviors
- **info_0**: Extremely fast
- **info_1**: Hunts Rattlesnakes

### Combat
- **info_0**: Attacks Rattlesnakes


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Roadrunner entity is summonable (`/summon xeenaa-alexs-mobs:roadrunner`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Fast movement, meep variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- RoadrunnerEntity class
- RoadrunnerModel class (GeckoLib model)
- RoadrunnerRenderer class (GeckoLib renderer)
- Geometry file (roadrunner.geo.json)
- Animation file (roadrunner.animation.json)
- Texture file(s) (roadrunner.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Fast movement, meep variant

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
1. **Complexity**: Simple - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Fast movement, meep variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Roadrunner cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `RoadrunnerEntity.java`
- `RoadrunnerModel.java`
- `RoadrunnerRenderer.java`

### Resource Files
- `roadrunner.geo.json`
- `roadrunner.animation.json`
- `roadrunner.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 2-3 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Passive type
- [ ] Special features work: Fast movement, meep variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Fast movement, meep variant
- Biomes: Desert, Badlands

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 96

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
