# Epic 69: Potoo - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Potoo** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Jungle
**Key Features**: Camouflage bird

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter potoo in jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Camouflage bird

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle

---

## Features & Functionality

### Core Features
- Lazy birds
- Camouflage as tree parts
- Massive eyes with scaling pupils

### Special Mechanics
- Perches on leaves/logs and stays there
- Monster detection: Pupils dilate in darkness, shrieks when monsters can spawn
- Hunts small flying insects
- Can be handled with Falconry Glove

### Items & Interactions
- **Eats**: Small flying insects

### AI Behaviors
- **info_0**: Sleeps during day
- **info_1**: More active at night
- **info_2**: Perches and rarely leaves
- **info_3**: Shrieks when monsters can spawn
- **info_4**: Hunts insects

### Breeding
- **Item**: Maggots


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Potoo entity is summonable (`/summon xeenaa-alexs-mobs:potoo`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Camouflage bird
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- PotooEntity class
- PotooModel class (GeckoLib model)
- PotooRenderer class (GeckoLib renderer)
- Geometry file (potoo.geo.json)
- Animation file (potoo.animation.json)
- Texture file(s) (potoo.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Camouflage bird

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
2. **Special Features**: Camouflage bird
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Potoo cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `PotooEntity.java`
- `PotooModel.java`
- `PotooRenderer.java`

### Resource Files
- `potoo.geo.json`
- `potoo.animation.json`
- `potoo.png` (+ variants)
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
- [ ] Special features work: Camouflage bird
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Camouflage bird
- Biomes: Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 91

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
