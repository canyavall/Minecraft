# Epic 99: Void Worm - Boss Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Void Worm** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Boss
**Biome(s)**: The End
**Key Features**: Boss mob, multi-part, portal mechanics

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter void worm in the end
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (boss)
   - Boss-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Boss mob, multi-part, portal mechanics

### Game Mechanics

**Entity Type**: Boss Animal
**Size**: TBD (check original model)
**Behavior**: boss
**Health**: TBD
**Drops**: TBD
**Spawning**: The End

---

## Features & Functionality

### Core Features
- Ancient creature
- Interdimensional parasite
- Boss fight
- Tunnel through reality

### Special Mechanics
- Summoning: Toss Mysterious Worm into End's void
- Mysterious Worm: Place Crimson Mosquito Larva in Enderiophage Capsid
- Splitting: Damaged body segment stops glowing, destroyed = splits in two (half health, slower)
- Can spawn many worms if not careful
- Dimensional Carver: Eye + mandibles + netherite = portal to spawn (20 uses)
- Shattered Dimensional Carver: Dimensional Carver in Capsid = portal 1 million blocks

### Items & Interactions
- **Drops**: Void Worm Eye, Void Worm Mandibles (x2)
- **Crafted**: Void Worm Beak (trophy, bites with redstone), Dimensional Carver (spawn portal, 20 uses), Shattered Dimensional Carver

### AI Behaviors
- **info_0**: Dwells in void
- **info_1**: Tunnels through reality
- **info_2**: Splits when damaged

### Combat
- **info_0**: Homing crystals (up to 4)
- **info_1**: Direct bite attacks
- **info_2**: Portal tearing (reach hidden prey)
- **info_3**: Splitting (when segments destroyed)
- **info_4**: Head damage only (body segments split worm)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Void Worm entity is summonable (`/summon xeenaa-alexs-mobs:void-worm`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Boss behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Boss mob, multi-part, portal mechanics
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- VoidWormEntity class
- VoidWormModel class (GeckoLib model)
- VoidWormRenderer class (GeckoLib renderer)
- Geometry file (void-worm.geo.json)
- Animation file (void-worm.animation.json)
- Texture file(s) (void-worm.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Boss mob, multi-part, portal mechanics

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
1. **Complexity**: Very Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Boss mob, multi-part, portal mechanics
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Void Worm cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `VoidWormEntity.java`
- `VoidWormModel.java`
- `VoidWormRenderer.java`

### Resource Files
- `void-worm.geo.json`
- `void-worm.animation.json`
- `void-worm.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 7-14 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Boss type
- [ ] Special features work: Boss mob, multi-part, portal mechanics
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Boss
- Complexity: Very Complex
- Special features: Boss mob, multi-part, portal mechanics
- Biomes: The End

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 121

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
