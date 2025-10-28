# Epic 39: Farseer - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Farseer** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Ancient City
**Key Features**: Portal mechanics, transmutation table

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter farseer in ancient city
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Portal mechanics, transmutation table

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Ancient City

---

## Features & Functionality

### Core Features
- From beyond dimensions
- Extremely rare
- Tear portals at world borders

### Special Mechanics
- Portal tearing: Appears at world border when prey near
- Shattered Dimensional Carver: Place Dimensional Carver in Capsid, creates portal 1 million blocks away
- Transmutation Table: Farseer Arm + obsidian + nether star
- Transmutation: Transforms entire stacks for XP cost
- Three options appear per item
- Item frequency affects future options
- Loses knowledge on destruction, explosion + nether star

### Items & Interactions
- **Drops**: Farseer Arms
- **Crafted**: Transmutation Table (stack transformation), Shattered Dimensional Carver

### AI Behaviors
- **info_0**: Tears portals at world border
- **info_1**: Extremely hostile
- **info_2**: Surprises prey

### Combat
- **info_0**: Quick claws
- **info_1**: Devastating beam from eye
- **info_2**: Dissolves opponents into static
- **info_3**: Pierces armor and enchantments


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Farseer entity is summonable (`/summon xeenaa-alexs-mobs:farseer`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Portal mechanics, transmutation table
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- FarseerEntity class
- FarseerModel class (GeckoLib model)
- FarseerRenderer class (GeckoLib renderer)
- Geometry file (farseer.geo.json)
- Animation file (farseer.animation.json)
- Texture file(s) (farseer.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Portal mechanics, transmutation table

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
2. **Special Features**: Portal mechanics, transmutation table
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Farseer cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `FarseerEntity.java`
- `FarseerModel.java`
- `FarseerRenderer.java`

### Resource Files
- `farseer.geo.json`
- `farseer.animation.json`
- `farseer.png` (+ variants)
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
- [ ] AI behaves according to Hostile type
- [ ] Special features work: Portal mechanics, transmutation table
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Very Complex
- Special features: Portal mechanics, transmutation table
- Biomes: Ancient City

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 61

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
