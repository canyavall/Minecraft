# Epic 51: Hammerhead Shark - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Hammerhead Shark** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Warm Ocean
**Key Features**: Aquatic predator

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter hammerhead shark in warm ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Aquatic predator

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Warm Ocean

---

## Features & Functionality

### Core Features
- Large cartilaginous fish
- Predatory scavengers
- Target creatures below half health

### Special Mechanics
- Circle prey before attacking (scoping attack angle)
- Drops teeth occasionally when attacking

### Items & Interactions
- **Drops**: Hammerhead Teeth
- **Eats**: Tropical fish, squid, injured creatures
- **Crafted**: Hammerhead Arrows (better underwater travel, extra damage to aquatic)

### AI Behaviors
- **info_0**: Circles prey
- **info_1**: Scavenger behavior (targets half health)
- **info_2**: Loves tropical fish and squid

### Combat
- **info_0**: Circling attack pattern
- **info_1**: Targets weakened prey


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Hammerhead Shark entity is summonable (`/summon xeenaa-alexs-mobs:hammerhead-shark`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Aquatic predator
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- HammerheadSharkEntity class
- HammerheadSharkModel class (GeckoLib model)
- HammerheadSharkRenderer class (GeckoLib renderer)
- Geometry file (hammerhead-shark.geo.json)
- Animation file (hammerhead-shark.animation.json)
- Texture file(s) (hammerhead-shark.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Aquatic predator

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
2. **Special Features**: Aquatic predator
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Hammerhead Shark cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `HammerheadSharkEntity.java`
- `HammerheadSharkModel.java`
- `HammerheadSharkRenderer.java`

### Resource Files
- `hammerhead-shark.geo.json`
- `hammerhead-shark.animation.json`
- `hammerhead-shark.png` (+ variants)
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
- [ ] AI behaves according to Placatable type
- [ ] Special features work: Aquatic predator
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Medium
- Special features: Aquatic predator
- Biomes: Warm Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 73

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
