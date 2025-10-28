# Epic 44: Froststalker - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Froststalker** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Ice Spikes, Frozen Peaks
**Key Features**: Ice projectiles, spike variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter froststalker in ice spikes, frozen peaks
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Ice projectiles, spike variants

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
- Ultimate pack predator
- Favorite prey: Tusklin
- Ice spike defense

### Special Mechanics
- Pack hunting: Upright posture, circles prey, group attacks
- Ice shards: Shake off when injured (great damage)
- Regain spikes in water
- Freeze water beneath feet
- Froststalker Horn helmet: Tricks them into thinking wearer is Froststalker
- Wearing helmet: Can breed (2 porkchops) but will turn on wearer if attacked
- Warm biomes: Weakness + ice spikes melt

### Items & Interactions
- **Drops**: Froststalker Horn
- **Crafted**: Froststalker Horn Helmet (disguise)

### AI Behaviors
- **info_0**: Pack hunting
- **info_1**: Circles prey
- **info_2**: Upright posture when hunting
- **info_3**: Shakes ice shards when injured
- **info_4**: Freezes water
- **info_5**: Weakness in warm biomes

### Breeding
- **Item**: 2 Porkchops (only while wearing horn helmet)
- **Warning**: Will attack if provoked

### Combat
- **info_0**: Pack tactics
- **info_1**: Ice shard shake
- **info_2**: Leaping, slashing, biting
- **info_3**: Freezes water (unsafe battleground)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Froststalker entity is summonable (`/summon xeenaa-alexs-mobs:froststalker`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Ice projectiles, spike variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- FroststalkerEntity class
- FroststalkerModel class (GeckoLib model)
- FroststalkerRenderer class (GeckoLib renderer)
- Geometry file (froststalker.geo.json)
- Animation file (froststalker.animation.json)
- Texture file(s) (froststalker.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Ice projectiles, spike variants

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
2. **Special Features**: Ice projectiles, spike variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Froststalker cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `FroststalkerEntity.java`
- `FroststalkerModel.java`
- `FroststalkerRenderer.java`

### Resource Files
- `froststalker.geo.json`
- `froststalker.animation.json`
- `froststalker.png` (+ variants)
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
- [ ] Special features work: Ice projectiles, spike variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Medium
- Special features: Ice projectiles, spike variants
- Biomes: Ice Spikes, Frozen Peaks

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 66

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
