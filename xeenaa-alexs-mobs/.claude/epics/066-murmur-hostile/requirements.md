# Epic 66: Murmur - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Murmur** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Deep Dark
**Key Features**: Sculk-related, angry state

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter murmur in deep dark
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Sculk-related, angry state

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Deep Dark

---

## Features & Functionality

### Core Features
- Tortured undead soul
- Appears as harmless person
- Serpentine extending neck (50+ blocks)

### Special Mechanics
- Neck extension: Extends over 50 blocks
- Head targeting: Head takes half damage vs body
- Deep spawning: Even deeper than Cave Centipedes
- Cherry Grove spawning: Rare surface appearance

### Items & Interactions
- **Drops**: Red Wool, Elastic Tendon, Unsettling Kimono (rare)
- **Crafted**: Tendon Whip (medium-range, 3 mobs, 8+ blocks, auto-target), Repairs with Elastic Tendons
- **Unsettling Kimono**: +2 block placement distance, makes undead neutral (except Murmurs and Wither)

### AI Behaviors
- **info_0**: Extends neck to look
- **info_1**: Sweeps forward to bite
- **info_2**: Quick retreat
- **info_3**: Head takes reduced damage

### Combat
- **info_0**: Extended neck bite
- **info_1**: Quick sweep attacks
- **info_2**: Head damage reduction


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Murmur entity is summonable (`/summon xeenaa-alexs-mobs:murmur`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Sculk-related, angry state
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MurmurEntity class
- MurmurModel class (GeckoLib model)
- MurmurRenderer class (GeckoLib renderer)
- Geometry file (murmur.geo.json)
- Animation file (murmur.animation.json)
- Texture file(s) (murmur.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Sculk-related, angry state

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
1. **Complexity**: Medium - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Sculk-related, angry state
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Murmur cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MurmurEntity.java`
- `MurmurModel.java`
- `MurmurRenderer.java`

### Resource Files
- `murmur.geo.json`
- `murmur.animation.json`
- `murmur.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 3-5 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Hostile type
- [ ] Special features work: Sculk-related, angry state
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Sculk-related, angry state
- Biomes: Deep Dark

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 88

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
