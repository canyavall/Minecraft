# Epic 41: Fly - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Fly** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Multiple biomes
**Key Features**: Tiny pest, minimal AI

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter fly in multiple biomes
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Tiny pest, minimal AI

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Multiple biomes

---

## Features & Functionality

### Core Features
- Small insects
- Attracted to dead/dying things
- Chases zombies nearly to death

### Special Mechanics
- Transforms into Crimson Mosquito if in Nether
- Drops maggots on death

### Items & Interactions
- **Drops**: Maggots (chance)
- **Eats**: Rotten Flesh

### AI Behaviors
- **info_0**: Chases and bites zombies
- **info_1**: Attracted to undead

### Breeding
- **Item**: Rotten Flesh

### Combat
- **info_0**: Bites zombies


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Fly entity is summonable (`/summon xeenaa-alexs-mobs:fly`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Tiny pest, minimal AI
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- FlyEntity class
- FlyModel class (GeckoLib model)
- FlyRenderer class (GeckoLib renderer)
- Geometry file (fly.geo.json)
- Animation file (fly.animation.json)
- Texture file(s) (fly.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Tiny pest, minimal AI

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
2. **Special Features**: Tiny pest, minimal AI
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Fly cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `FlyEntity.java`
- `FlyModel.java`
- `FlyRenderer.java`

### Resource Files
- `fly.geo.json`
- `fly.animation.json`
- `fly.png` (+ variants)
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
- [ ] Special features work: Tiny pest, minimal AI
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Tiny pest, minimal AI
- Biomes: Multiple biomes

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 63

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
