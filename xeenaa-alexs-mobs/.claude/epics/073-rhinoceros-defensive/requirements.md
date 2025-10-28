# Epic 73: Rhinoceros - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Rhinoceros** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: Savanna
**Key Features**: Charge attack, potion overlay, angry

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter rhinoceros in savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Charge attack, potion overlay, angry

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: Savanna

---

## Features & Functionality

### Core Features
- Large armored mammals
- Large horns
- Strongly dislike pillagers

### Special Mechanics
- Wheat pacification: Feeding wheat makes them friendly
- Potion application: Use potion on horn, applies effect to attacked creatures (15-20 attacks)
- Defend befriended players
- Roam villages for safety
- Seek out and attack pillagers

### AI Behaviors
- **info_0**: Initially skittish
- **info_1**: Pacified with wheat
- **info_2**: Defends befriended players
- **info_3**: Roams villages
- **info_4**: Hunts pillagers

### Equipment & Gear
- Horn (potion application)

### Breeding
- **Item**: Tall Grass or Dead Bushes

### Combat
- **info_0**: Horn attacks (large damage)
- **info_1**: Fast attacks
- **info_2**: Potion effects on hit


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Rhinoceros entity is summonable (`/summon xeenaa-alexs-mobs:rhinoceros`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Charge attack, potion overlay, angry
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- RhinocerosEntity class
- RhinocerosModel class (GeckoLib model)
- RhinocerosRenderer class (GeckoLib renderer)
- Geometry file (rhinoceros.geo.json)
- Animation file (rhinoceros.animation.json)
- Texture file(s) (rhinoceros.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Charge attack, potion overlay, angry

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
1. **Complexity**: Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Charge attack, potion overlay, angry
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Rhinoceros cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `RhinocerosEntity.java`
- `RhinocerosModel.java`
- `RhinocerosRenderer.java`

### Resource Files
- `rhinoceros.geo.json`
- `rhinoceros.animation.json`
- `rhinoceros.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 5-7 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Charge attack, potion overlay, angry
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Complex
- Special features: Charge attack, potion overlay, angry
- Biomes: Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 95

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
