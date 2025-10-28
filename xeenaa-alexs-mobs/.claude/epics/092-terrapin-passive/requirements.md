# Epic 92: Terrapin - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Terrapin** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Swamp, River
**Key Features**: 6 base colors + 6 shell patterns + 4 skin patterns

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter terrapin in swamp, river
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 6 base colors + 6 shell patterns + 4 skin patterns

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Swamp, River

---

## Features & Functionality

### Core Features
- Small river turtles
- Completely passive
- Relies on shell for protection

### Special Mechanics
- Can be bucketed
- Jump mechanics: Recedes into shell when jumped on
- Second jump: Spins in direction of jumper, ricochets off walls
- Colliding with mob deals damage (mob may become aggressive)
- Vast array of patterns and colors

### AI Behaviors
- **info_0**: Passive swimming
- **info_1**: Shell mechanics (jump interactions)

### Breeding
- **Item**: Seagrass
- **Produces**: 1-4 eggs
- **Genetics**: Direct cross or mutation to random color
- **info_3**: Colors can be parent's or average of both

### Variants
- Vast array of patterns and colors
- Many unnatural colors
- Mutation mechanic


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Terrapin entity is summonable (`/summon xeenaa-alexs-mobs:terrapin`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 6 base colors + 6 shell patterns + 4 skin patterns
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- TerrapinEntity class
- TerrapinModel class (GeckoLib model)
- TerrapinRenderer class (GeckoLib renderer)
- Geometry file (terrapin.geo.json)
- Animation file (terrapin.animation.json)
- Texture file(s) (terrapin.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 6 base colors + 6 shell patterns + 4 skin patterns

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
2. **Special Features**: 6 base colors + 6 shell patterns + 4 skin patterns
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Terrapin cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `TerrapinEntity.java`
- `TerrapinModel.java`
- `TerrapinRenderer.java`

### Resource Files
- `terrapin.geo.json`
- `terrapin.animation.json`
- `terrapin.png` (+ variants)
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
- [ ] AI behaves according to Passive type
- [ ] Special features work: 6 base colors + 6 shell patterns + 4 skin patterns
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Complex
- Special features: 6 base colors + 6 shell patterns + 4 skin patterns
- Biomes: Swamp, River

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 114

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
