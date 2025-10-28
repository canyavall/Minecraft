# Epic 52: Hummingbird - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Hummingbird** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Jungle, Forest
**Key Features**: Tiny flying, 3 random variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter hummingbird in jungle, forest
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Tiny flying, 3 random variants

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle, Forest

---

## Features & Functionality

### Core Features
- Smallest birds
- Extremely fast flight
- Intense pollinators (better than bees)

### Special Mechanics
- Pollination: More effective than bees
- Hummingbird Feeders: Crafted with Water Bottles + Sugar
- Feeders keep hummingbirds in area and boost pollination

### Items & Interactions
- **Eats**: Flowers (via pollination)
- **Interacts with**: Hummingbird Feeders

### AI Behaviors
- **info_0**: Fast flight
- **info_1**: Intense pollination
- **info_2**: Stays near feeders

### Breeding
- **Item**: Flowers


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Hummingbird entity is summonable (`/summon xeenaa-alexs-mobs:hummingbird`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Tiny flying, 3 random variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- HummingbirdEntity class
- HummingbirdModel class (GeckoLib model)
- HummingbirdRenderer class (GeckoLib renderer)
- Geometry file (hummingbird.geo.json)
- Animation file (hummingbird.animation.json)
- Texture file(s) (hummingbird.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Tiny flying, 3 random variants

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
2. **Special Features**: Tiny flying, 3 random variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Hummingbird cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `HummingbirdEntity.java`
- `HummingbirdModel.java`
- `HummingbirdRenderer.java`

### Resource Files
- `hummingbird.geo.json`
- `hummingbird.animation.json`
- `hummingbird.png` (+ variants)
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
- [ ] Special features work: Tiny flying, 3 random variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Tiny flying, 3 random variants
- Biomes: Jungle, Forest

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 74

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
