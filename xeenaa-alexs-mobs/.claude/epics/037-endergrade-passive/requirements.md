# Epic 37: Endergrade - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Endergrade** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: The End
**Key Features**: Rideable with saddle

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter endergrade in the end
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rideable with saddle

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: The End

---

## Features & Functionality

### Core Features
- Giant relative of microscopic Tardigrade
- Floats through air
- Eats chorus fruit

### Special Mechanics
- Saddleable: Can be ridden
- Chorus on a Stick: Controls ridden Endergrade (like carrot on stick)
- Very slow mount
- Useful for evading Shulker attacks
- Reaching distant heights

### Items & Interactions
- **Eats**: Chorus Fruit (luring, breeding)

### AI Behaviors
- **info_0**: Floats gently
- **info_1**: Eats chorus fruit
- **info_2**: Lives at heights

### Equipment & Gear
- Saddle
- Chorus on a Stick (control)

### Breeding
- **Item**: Chorus Fruit


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Endergrade entity is summonable (`/summon xeenaa-alexs-mobs:endergrade`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rideable with saddle
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- EndergradeEntity class
- EndergradeModel class (GeckoLib model)
- EndergradeRenderer class (GeckoLib renderer)
- Geometry file (endergrade.geo.json)
- Animation file (endergrade.animation.json)
- Texture file(s) (endergrade.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rideable with saddle

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
2. **Special Features**: Rideable with saddle
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Endergrade cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `EndergradeEntity.java`
- `EndergradeModel.java`
- `EndergradeRenderer.java`

### Resource Files
- `endergrade.geo.json`
- `endergrade.animation.json`
- `endergrade.png` (+ variants)
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
- [ ] Special features work: Rideable with saddle
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Rideable with saddle
- Biomes: The End

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 59

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
