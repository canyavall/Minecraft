# Epic 59: Maned Wolf - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Maned Wolf** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Savanna
**Key Features**: Ender variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter maned wolf in savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Ender variant

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
- Strange canine (neither wolf nor fox)
- Primarily frugivorous
- Known as "Skunk Wolf"

### Special Mechanics
- Apple feeding: Makes wolf shake fur and release smell
- Smell attracts other animals (herding livestock)
- Smell increases fungus production in Leafcutter anthills

### Items & Interactions
- **Eats**: Apples

### AI Behaviors
- **info_0**: Passive movement
- **info_1**: Shakes when fed apples
- **info_2**: Emits attraction smell

### Breeding
- **Item**: Rabbit or Chicken meat


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Maned Wolf entity is summonable (`/summon xeenaa-alexs-mobs:maned-wolf`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Ender variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- ManedWolfEntity class
- ManedWolfModel class (GeckoLib model)
- ManedWolfRenderer class (GeckoLib renderer)
- Geometry file (maned-wolf.geo.json)
- Animation file (maned-wolf.animation.json)
- Texture file(s) (maned-wolf.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Ender variant

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
2. **Special Features**: Ender variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Maned Wolf cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `ManedWolfEntity.java`
- `ManedWolfModel.java`
- `ManedWolfRenderer.java`

### Resource Files
- `maned-wolf.geo.json`
- `maned-wolf.animation.json`
- `maned-wolf.png` (+ variants)
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
- [ ] Special features work: Ender variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Ender variant
- Biomes: Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 81

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
