# Epic 82: Skunk - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Skunk** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: Forest, Plains
**Key Features**: Spray defense mechanism

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter skunk in forest, plains
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Spray defense mechanism

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: Forest, Plains

---

## Features & Functionality

### Core Features
- Small mammals
- Very skittish
- Noxious spray defense

### Special Mechanics
- Defensive spray: Inflicts nausea when chased/hurt
- Lingering spray: Clings to surfaces, can be bottled
- Stink in a Bottle: Fuel for Stink Ray
- Stink Ray: Distracts mobs, causes infighting, scares passives (5 uses per bottle)
- Potion spray: If inflicted with potion effect while spraying, creates lingering cloud
- Only adults can spray

### Items & Interactions
- **Drops**: Stink in a Bottle (from lingering spray)
- **Crafted**: Stink Ray

### AI Behaviors
- **info_0**: Skittish
- **info_1**: Flees from predators
- **info_2**: Sprays when cornered/hurt

### Breeding
- **Item**: Sweet Berries

### Combat
- **info_0**: Nausea spray (defense)
- **info_1**: Lingering spray clouds
- **info_2**: Potion effect clouds (if inflicted)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Skunk entity is summonable (`/summon xeenaa-alexs-mobs:skunk`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Spray defense mechanism
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SkunkEntity class
- SkunkModel class (GeckoLib model)
- SkunkRenderer class (GeckoLib renderer)
- Geometry file (skunk.geo.json)
- Animation file (skunk.animation.json)
- Texture file(s) (skunk.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Spray defense mechanism

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
2. **Special Features**: Spray defense mechanism
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Skunk cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SkunkEntity.java`
- `SkunkModel.java`
- `SkunkRenderer.java`

### Resource Files
- `skunk.geo.json`
- `skunk.animation.json`
- `skunk.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Spray defense mechanism
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Simple
- Special features: Spray defense mechanism
- Biomes: Forest, Plains

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 104

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
