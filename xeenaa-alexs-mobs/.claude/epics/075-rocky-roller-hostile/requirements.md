# Epic 75: Rocky Roller - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Rocky Roller** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Dripstone Caves
**Key Features**: Rolling attack, angry state

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter rocky roller in dripstone caves
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rolling attack, angry state

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Dripstone Caves

---

## Features & Functionality

### Core Features
- Giant aggressive monster
- Covered in pointed dripstone shell
- Rolls at high speeds

### Special Mechanics
- Rolling attack: Crushes intruders at high speed
- Low health but extreme armor
- High damage output

### Items & Interactions
- **Drops**: Tuff, Pointed Dripstone, Rocky Shell
- **Crafted**: Rocky Chestplate (roll while sprinting, quick travel, ram damage, decreased fall damage, sinks in water)

### AI Behaviors
- **info_0**: Aggressive to intruders
- **info_1**: High-speed rolling

### Combat
- **info_0**: High-speed rolling
- **info_1**: Crushing attacks
- **info_2**: Thick armor
- **info_3**: High damage


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Rocky Roller entity is summonable (`/summon xeenaa-alexs-mobs:rocky-roller`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rolling attack, angry state
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- RockyRollerEntity class
- RockyRollerModel class (GeckoLib model)
- RockyRollerRenderer class (GeckoLib renderer)
- Geometry file (rocky-roller.geo.json)
- Animation file (rocky-roller.animation.json)
- Texture file(s) (rocky-roller.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rolling attack, angry state

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
2. **Special Features**: Rolling attack, angry state
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Rocky Roller cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `RockyRollerEntity.java`
- `RockyRollerModel.java`
- `RockyRollerRenderer.java`

### Resource Files
- `rocky-roller.geo.json`
- `rocky-roller.animation.json`
- `rocky-roller.png` (+ variants)
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
- [ ] Special features work: Rolling attack, angry state
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Rolling attack, angry state
- Biomes: Dripstone Caves

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 97

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
