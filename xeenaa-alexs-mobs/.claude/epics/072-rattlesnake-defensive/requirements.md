# Epic 72: Rattlesnake - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Rattlesnake** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: Desert, Badlands
**Key Features**: Venomous bite

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter rattlesnake in desert, badlands
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Venomous bite

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert, Badlands

---

## Features & Functionality

### Core Features
- Medium reptile
- Loud rattle warning
- Poisonous bite

### Special Mechanics
- Rattle warning: Loud sound when predator near
- Hostile if approached during rattle
- Sworn enemy of Roadrunners

### Items & Interactions
- **Drops**: Rattlesnake Rattle
- **Crafted**: Poisonous Essence (Rattle + Poison Potion), Poison Resistance (Poisonous Essence + Cave Centipede Leg)

### AI Behaviors
- **info_0**: Heard before seen
- **info_1**: Rattles at threats
- **info_2**: Hostile if approached during rattle
- **info_3**: Defends self

### Breeding
- **Item**: Any meat

### Combat
- **info_0**: Poisonous bite


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Rattlesnake entity is summonable (`/summon xeenaa-alexs-mobs:rattlesnake`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Venomous bite
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- RattlesnakeEntity class
- RattlesnakeModel class (GeckoLib model)
- RattlesnakeRenderer class (GeckoLib renderer)
- Geometry file (rattlesnake.geo.json)
- Animation file (rattlesnake.animation.json)
- Texture file(s) (rattlesnake.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Venomous bite

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
2. **Special Features**: Venomous bite
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Rattlesnake cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `RattlesnakeEntity.java`
- `RattlesnakeModel.java`
- `RattlesnakeRenderer.java`

### Resource Files
- `rattlesnake.geo.json`
- `rattlesnake.animation.json`
- `rattlesnake.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Venomous bite
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Simple
- Special features: Venomous bite
- Biomes: Desert, Badlands

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 94

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
