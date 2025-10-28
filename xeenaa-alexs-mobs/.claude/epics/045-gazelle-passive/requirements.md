# Epic 45: Gazelle - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Gazelle** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Savanna
**Key Features**: Herd animal, fleeing behavior

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter gazelle in savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Herd animal, fleeing behavior

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Savanna

---

## Features & Functionality

### Core Features
- Small, timid mammals
- Forage in large herds
- Extremely fast runners

### Special Mechanics
- Entire herd flees when one is attacked
- Horns can brew Speed III Potion (from Speed II)

### Items & Interactions
- **Drops**: Gazelle Horns (rare), Mutton

### AI Behaviors
- **info_0**: Foraging
- **info_1**: Herd behavior
- **info_2**: Flee from threats
- **info_3**: Very fast movement


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Gazelle entity is summonable (`/summon xeenaa-alexs-mobs:gazelle`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Herd animal, fleeing behavior
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- GazelleEntity class
- GazelleModel class (GeckoLib model)
- GazelleRenderer class (GeckoLib renderer)
- Geometry file (gazelle.geo.json)
- Animation file (gazelle.animation.json)
- Texture file(s) (gazelle.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Herd animal, fleeing behavior

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
2. **Special Features**: Herd animal, fleeing behavior
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Gazelle cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `GazelleEntity.java`
- `GazelleModel.java`
- `GazelleRenderer.java`

### Resource Files
- `gazelle.geo.json`
- `gazelle.animation.json`
- `gazelle.png` (+ variants)
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
- [ ] Special features work: Herd animal, fleeing behavior
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Herd animal, fleeing behavior
- Biomes: Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 67

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
