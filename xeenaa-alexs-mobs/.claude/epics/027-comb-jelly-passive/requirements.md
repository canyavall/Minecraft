# Epic 27: Comb Jelly - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Comb Jelly** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Frozen Ocean
**Key Features**: 3 color variants + glow overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter comb jelly in frozen ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 3 color variants + glow overlay

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Frozen Ocean

---

## Features & Functionality

### Core Features
- Gelatinous creature
- Flashing prismatic colors
- Drifts in water
- Simple intelligence

### Special Mechanics
- Can be bucketed
- Rainbow Jelly: Can color almost any mob with flashing rainbow pattern
- Sponge removes rainbow effect
- Eating jelly applies effect to player
- Rainbow Glass: Crafted with 8 glass, changes color by position, emits light

### Items & Interactions
- **Drops**: Rainbow Jelly
- **Crafted**: Rainbow Glass

### AI Behaviors
- **info_0**: Drifts passively
- **info_1**: Sifts microscopic prey

### Variants
- Multiple colors


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Comb Jelly entity is summonable (`/summon xeenaa-alexs-mobs:comb-jelly`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 3 color variants + glow overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CombJellyEntity class
- CombJellyModel class (GeckoLib model)
- CombJellyRenderer class (GeckoLib renderer)
- Geometry file (comb-jelly.geo.json)
- Animation file (comb-jelly.animation.json)
- Texture file(s) (comb-jelly.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 3 color variants + glow overlay

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
2. **Special Features**: 3 color variants + glow overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Comb Jelly cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CombJellyEntity.java`
- `CombJellyModel.java`
- `CombJellyRenderer.java`

### Resource Files
- `comb-jelly.geo.json`
- `comb-jelly.animation.json`
- `comb-jelly.png` (+ variants)
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
- [ ] Special features work: 3 color variants + glow overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: 3 color variants + glow overlay
- Biomes: Frozen Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 49

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
