# Epic 71: Rain Frog - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Rain Frog** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Desert (rain only)
**Key Features**: 3 random variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter rain frog in desert (rain only)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 3 random variants

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert (rain only)

---

## Features & Functionality

### Core Features
- Small frogs
- Cannot jump or swim well
- No tadpoles

### Special Mechanics
- Burrows in sand
- Puffs up and squeaks when threatened
- Shovel on burrowing frog prevents despawning
- Associated with rain (belief they bring rain if pleased)
- Enjoys music discs

### Items & Interactions
- **Eats**: Small insects, Maggots

### AI Behaviors
- **info_0**: Burrows in sand
- **info_1**: Defensive puffing
- **info_2**: Cannot jump/swim

### Breeding
- **Item**: Maggots


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Rain Frog entity is summonable (`/summon xeenaa-alexs-mobs:rain-frog`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 3 random variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- RainFrogEntity class
- RainFrogModel class (GeckoLib model)
- RainFrogRenderer class (GeckoLib renderer)
- Geometry file (rain-frog.geo.json)
- Animation file (rain-frog.animation.json)
- Texture file(s) (rain-frog.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 3 random variants

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
2. **Special Features**: 3 random variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Rain Frog cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `RainFrogEntity.java`
- `RainFrogModel.java`
- `RainFrogRenderer.java`

### Resource Files
- `rain-frog.geo.json`
- `rain-frog.animation.json`
- `rain-frog.png` (+ variants)
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
- [ ] Special features work: 3 random variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: 3 random variants
- Biomes: Desert (rain only)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 93

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
