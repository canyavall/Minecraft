# Epic 40: Flutter - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Flutter** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Lush Caves
**Key Features**: Butterfly-like, glow overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter flutter in lush caves
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Butterfly-like, glow overlay

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Lush Caves

---

## Features & Functionality

### Core Features
- Small plantoid creatures
- Float by flapping giant flower
- Interested in flowers

### Taming
- **Method**: Feed 4-6 different flowers
- **info_1**: Must all be different varieties
- **Benefits**: Stay/follow/wander commands, combat support

### Special Mechanics
- Flower pot armor: Can equip flower pot for additional protection
- Can pick up Flutter in pot by sneaking
- Breeding near azalea: Causes azalea bushes/leaves to bloom
- Combat: Floats up and shoots homing pollen balls

### Items & Interactions
- **Eats**: Flowers
- **Drops**: Spore Blossom
- **Healed with**: Flowers

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Floats up in combat
- **info_2**: Shoots homing pollen balls
- **info_3**: Avoids harm by floating

### Equipment & Gear
- Flower pot (armor)

### Breeding
- **Item**: Bonemeal

### Combat
- **info_0**: Homing pollen ball projectiles


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Flutter entity is summonable (`/summon xeenaa-alexs-mobs:flutter`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Butterfly-like, glow overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- FlutterEntity class
- FlutterModel class (GeckoLib model)
- FlutterRenderer class (GeckoLib renderer)
- Geometry file (flutter.geo.json)
- Animation file (flutter.animation.json)
- Texture file(s) (flutter.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Butterfly-like, glow overlay

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
2. **Special Features**: Butterfly-like, glow overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Flutter cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `FlutterEntity.java`
- `FlutterModel.java`
- `FlutterRenderer.java`

### Resource Files
- `flutter.geo.json`
- `flutter.animation.json`
- `flutter.png` (+ variants)
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
- [ ] Special features work: Butterfly-like, glow overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Butterfly-like, glow overlay
- Biomes: Lush Caves

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 62

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
