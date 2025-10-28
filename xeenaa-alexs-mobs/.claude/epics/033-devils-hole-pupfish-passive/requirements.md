# Epic 33: Devil's Hole Pupfish - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Devil's Hole Pupfish** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Desert (rare)
**Key Features**: Tiny rare fish

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter devil's hole pupfish in desert (rare)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Tiny rare fish

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert (rare)

---

## Features & Functionality

### Core Features
- World's rarest fish
- Only spawns in one specific chunk
- Requires Strange Fish Finder to locate

### Special Mechanics
- Chase each other (puppy-like behavior)
- Eats muck off submerged mossy blocks
- Can dislodge slime balls from mossy blocks
- Reproduces after feeding on moss
- Can be bucketed

### Items & Interactions
- **Drops**: Slime balls (from mossy blocks)
- **Crafted**: Strange Fish Finder (locates their chunk)

### AI Behaviors
- **info_0**: Chase behavior
- **info_1**: Feeds on mossy blocks
- **info_2**: Reproduces naturally

### Breeding
- **info_0**: Natural reproduction after moss feeding


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Devil's Hole Pupfish entity is summonable (`/summon xeenaa-alexs-mobs:devils-hole-pupfish`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Tiny rare fish
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- DevilsHolePupfishEntity class
- DevilsHolePupfishModel class (GeckoLib model)
- DevilsHolePupfishRenderer class (GeckoLib renderer)
- Geometry file (devils-hole-pupfish.geo.json)
- Animation file (devils-hole-pupfish.animation.json)
- Texture file(s) (devils-hole-pupfish.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Tiny rare fish

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
2. **Special Features**: Tiny rare fish
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Devil's Hole Pupfish cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `DevilsHolePupfishEntity.java`
- `DevilsHolePupfishModel.java`
- `DevilsHolePupfishRenderer.java`

### Resource Files
- `devils-hole-pupfish.geo.json`
- `devils-hole-pupfish.animation.json`
- `devils-hole-pupfish.png` (+ variants)
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
- [ ] Special features work: Tiny rare fish
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Tiny rare fish
- Biomes: Desert (rare)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 55

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
