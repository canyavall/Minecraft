# Epic 63: Moose - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Moose** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Snowy Plains, Taiga
**Key Features**: Rideable, antlered variants, snowy variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter moose in snowy plains, taiga
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rideable, antlered variants, snowy variants

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Snowy Plains, Taiga

---

## Features & Functionality

### Core Features
- Giant herbivorous mammals
- Massive antlers
- Protective parents

### Special Mechanics
- Antler drop: Every 7-10 days (chance)
- Antler headdress: 2 antlers = knockback abilities
- Regrow antlers: 3-5 days
- Jostle for dominance (antler to antler)
- Defend young from predators (wolves)
- Snow coating: Remove with shovel or place snow layer

### Items & Interactions
- **Drops**: Moose Antlers (periodic), Moose Ribs
- **Crafted**: Antler Headdress (knockback abilities)

### AI Behaviors
- **info_0**: Jostling (dominance)
- **info_1**: Defends young
- **info_2**: Antler drop cycle
- **info_3**: Antler regrowth

### Breeding
- **Item**: Multiple Dandelions

### Combat
- **info_0**: Massive antler attacks
- **info_1**: Dangerous damage
- **info_2**: Protects offspring


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Moose entity is summonable (`/summon xeenaa-alexs-mobs:moose`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rideable, antlered variants, snowy variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MooseEntity class
- MooseModel class (GeckoLib model)
- MooseRenderer class (GeckoLib renderer)
- Geometry file (moose.geo.json)
- Animation file (moose.animation.json)
- Texture file(s) (moose.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rideable, antlered variants, snowy variants

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
2. **Special Features**: Rideable, antlered variants, snowy variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Moose cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MooseEntity.java`
- `MooseModel.java`
- `MooseRenderer.java`

### Resource Files
- `moose.geo.json`
- `moose.animation.json`
- `moose.png` (+ variants)
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
- [ ] AI behaves according to Neutral type
- [ ] Special features work: Rideable, antlered variants, snowy variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Rideable, antlered variants, snowy variants
- Biomes: Snowy Plains, Taiga

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 85

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
