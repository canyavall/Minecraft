# Epic 16: Bison - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Bison** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Plains, Meadow
**Key Features**: Rideable, shearable, baby variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter bison in plains, meadow
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rideable, shearable, baby variants

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Plains, Meadow

---

## Features & Functionality

### Core Features
- Hoofed mammals
- Quick to anger
- Charge at intruders and other bison
- Fling attackers into air

### Special Mechanics
- Shearable: Obtain Bison Fur
- Bison Fur Block: Makes carpets, crafts with boots for powdered snow resistance
- Sheared bison eat grass to regrow coat
- Clear snow layers while moving
- Snow coating in snowy weather: Remove with shovel or place snow layer on them
- Snow melts in water/rain/hot biomes

### Items & Interactions
- **Drops**: Beef, Bison Fur (when sheared)
- **Crafted**: Bison Fur Block, Boots upgrade

### AI Behaviors
- **info_0**: Aggressive when approached
- **info_1**: Charge attacks
- **info_2**: Young are defenseless
- **info_3**: Adult bison clear snow
- **info_4**: Eat grass (to regrow fur)

### Breeding
- **Item**: Wheat

### Combat
- **info_0**: Charge attack
- **info_1**: Fling into air


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Bison entity is summonable (`/summon xeenaa-alexs-mobs:bison`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rideable, shearable, baby variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BisonEntity class
- BisonModel class (GeckoLib model)
- BisonRenderer class (GeckoLib renderer)
- Geometry file (bison.geo.json)
- Animation file (bison.animation.json)
- Texture file(s) (bison.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rideable, shearable, baby variants

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
2. **Special Features**: Rideable, shearable, baby variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Bison cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BisonEntity.java`
- `BisonModel.java`
- `BisonRenderer.java`

### Resource Files
- `bison.geo.json`
- `bison.animation.json`
- `bison.png` (+ variants)
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
- [ ] Special features work: Rideable, shearable, baby variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Rideable, shearable, baby variants
- Biomes: Plains, Meadow

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 38

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
