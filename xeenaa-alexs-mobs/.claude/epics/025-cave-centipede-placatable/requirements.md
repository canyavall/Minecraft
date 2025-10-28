# Epic 25: Cave Centipede - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Cave Centipede** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Caves
**Key Features**: Multi-segment entity, glow overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter cave centipede in caves
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Multi-segment entity, glow overlay

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Caves

---

## Features & Functionality

### Core Features
- Rare subterranean arthropod
- Giant and cumbersome
- Poisonous bite
- Slow but dangerous

### Special Mechanics
- Preys on Cockroaches
- Seeks them from far away

### Items & Interactions
- **Drops**: Cave Centipede Legs
- **Crafted**: Cave Centipede Leggings (wall climbing + increased defense), Poison Resistance Potion (brew with Poisonous Essence or Komodo Dragon Spit)

### AI Behaviors
- **info_0**: Seeks cockroaches
- **info_1**: Slow movement
- **info_2**: Poisonous attacks

### Combat
- **info_0**: Poisonous bite
- **info_1**: Dangerous damage
- **info_2**: Slow attacks


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Cave Centipede entity is summonable (`/summon xeenaa-alexs-mobs:cave-centipede`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Multi-segment entity, glow overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CaveCentipedeEntity class
- CaveCentipedeModel class (GeckoLib model)
- CaveCentipedeRenderer class (GeckoLib renderer)
- Geometry file (cave-centipede.geo.json)
- Animation file (cave-centipede.animation.json)
- Texture file(s) (cave-centipede.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Multi-segment entity, glow overlay

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
1. **Complexity**: Very Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Multi-segment entity, glow overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Cave Centipede cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CaveCentipedeEntity.java`
- `CaveCentipedeModel.java`
- `CaveCentipedeRenderer.java`

### Resource Files
- `cave-centipede.geo.json`
- `cave-centipede.animation.json`
- `cave-centipede.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 7-14 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Placatable type
- [ ] Special features work: Multi-segment entity, glow overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Very Complex
- Special features: Multi-segment entity, glow overlay
- Biomes: Caves

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 47

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
