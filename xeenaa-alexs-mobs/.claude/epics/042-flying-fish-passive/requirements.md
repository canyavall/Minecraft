# Epic 42: Flying Fish - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Flying Fish** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Ocean (Warm)
**Key Features**: 3 random variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter flying fish in ocean (warm)
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
**Spawning**: Ocean (Warm)

---

## Features & Functionality

### Core Features
- Leap out of water and glide
- Prey to dolphins and seals

### Special Mechanics
- Can be bucketed
- Crafting: 2 Flying Fish + 2 String = boots enabling water leaping
- Boots improved if wearer has elytra

### Items & Interactions
- **Drops**: Flying Fish (itself)
- **Crafted**: Flying Fish Boots

### AI Behaviors
- **info_0**: Leaps and glides
- **info_1**: Flees from predators

### Variants
- Few color variations


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Flying Fish entity is summonable (`/summon xeenaa-alexs-mobs:flying-fish`)
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

- FlyingFishEntity class
- FlyingFishModel class (GeckoLib model)
- FlyingFishRenderer class (GeckoLib renderer)
- Geometry file (flying-fish.geo.json)
- Animation file (flying-fish.animation.json)
- Texture file(s) (flying-fish.png + variants)
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
- ✅ Epic 02: Flying Fish cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `FlyingFishEntity.java`
- `FlyingFishModel.java`
- `FlyingFishRenderer.java`

### Resource Files
- `flying-fish.geo.json`
- `flying-fish.animation.json`
- `flying-fish.png` (+ variants)
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
- Biomes: Ocean (Warm)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 64

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
