# Epic 100: Warped Mosco - Chief Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Warped Mosco** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Chief
**Biome(s)**: Warped Forest (Nether)
**Key Features**: Chief/mini-boss, glow overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter warped mosco in warped forest (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (chief)
   - Chief-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Chief/mini-boss, glow overlay

### Game Mechanics

**Entity Type**: Chief Animal
**Size**: TBD (check original model)
**Behavior**: chief
**Health**: TBD
**Drops**: TBD
**Spawning**: Warped Forest (Nether)

---

## Features & Functionality

### Core Features
- Extremely dangerous Crimson Mosquito mutation
- Massive body
- Enraged

### Special Mechanics
- Creation: Crimson Mosquito sucks blood of warped fungus creature → mutation
- Attacks any nearby creature as food
- Blood regeneration: Sucks blood to regain health
- Hemolymph Blaster: Upgraded Blood Sprayer (Warped Muscle + Hemolymph Sacs)

### Items & Interactions
- **Drops**: Warped Muscle, Hemolymph Sacs
- **Crafted**: Hemolymph Blaster (upgraded weapon)

### AI Behaviors
- **info_0**: Enraged
- **info_1**: Attacks all creatures
- **info_2**: Regenerates via blood

### Combat
- **info_0**: Aerial body slam
- **info_1**: Punching
- **info_2**: Smashing
- **info_3**: Blood sucking (regeneration)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Warped Mosco entity is summonable (`/summon xeenaa-alexs-mobs:warped-mosco`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Chief behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Chief/mini-boss, glow overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- WarpedMoscoEntity class
- WarpedMoscoModel class (GeckoLib model)
- WarpedMoscoRenderer class (GeckoLib renderer)
- Geometry file (warped-mosco.geo.json)
- Animation file (warped-mosco.animation.json)
- Texture file(s) (warped-mosco.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Chief/mini-boss, glow overlay

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
2. **Special Features**: Chief/mini-boss, glow overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Warped Mosco cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `WarpedMoscoEntity.java`
- `WarpedMoscoModel.java`
- `WarpedMoscoRenderer.java`

### Resource Files
- `warped-mosco.geo.json`
- `warped-mosco.animation.json`
- `warped-mosco.png` (+ variants)
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
- [ ] AI behaves according to Chief type
- [ ] Special features work: Chief/mini-boss, glow overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Chief
- Complexity: Very Complex
- Special features: Chief/mini-boss, glow overlay
- Biomes: Warped Forest (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 122

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
