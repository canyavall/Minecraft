# Epic 96: Tusklin - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Tusklin** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Ice Spikes, Frozen Peaks
**Key Features**: Rideable, saddle + hooves overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter tusklin in ice spikes, frozen peaks
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rideable, saddle + hooves overlay

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Ice Spikes, Frozen Peaks

---

## Features & Functionality

### Core Features
- Long-lost ancestor of Hoglin
- Hostile to intruders
- Large tusks

### Special Mechanics
- Saddle: Can be ridden (bucks off after short time)
- Brown Mushroom: Pacifies temporarily
- Young dig up brown mushrooms from dirt
- Hogshoes: Rumored Piglin equipment (enchantable like boots)
- Won't pursue far from battle point

### Items & Interactions
- **Eats**: Brown Mushroom (pacification), Red Mushroom (breeding)
- **Equipment**: Saddle, Hogshoes (rumored)

### AI Behaviors
- **info_0**: Attacks intruders
- **info_1**: Won't pursue far
- **info_2**: Digs up mushrooms (young)
- **info_3**: Bucks off rider

### Equipment & Gear
- Saddle
- Hogshoes (enchantable)

### Breeding
- **Item**: Red Mushroom

### Combat
- **info_0**: Gore with tusks
- **info_1**: Fling into air


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Tusklin entity is summonable (`/summon xeenaa-alexs-mobs:tusklin`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rideable, saddle + hooves overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- TusklinEntity class
- TusklinModel class (GeckoLib model)
- TusklinRenderer class (GeckoLib renderer)
- Geometry file (tusklin.geo.json)
- Animation file (tusklin.animation.json)
- Texture file(s) (tusklin.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rideable, saddle + hooves overlay

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
2. **Special Features**: Rideable, saddle + hooves overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Tusklin cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `TusklinEntity.java`
- `TusklinModel.java`
- `TusklinRenderer.java`

### Resource Files
- `tusklin.geo.json`
- `tusklin.animation.json`
- `tusklin.png` (+ variants)
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
- [ ] AI behaves according to Placatable type
- [ ] Special features work: Rideable, saddle + hooves overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Complex
- Special features: Rideable, saddle + hooves overlay
- Biomes: Ice Spikes, Frozen Peaks

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 118

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
