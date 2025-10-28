# Epic 67: Orca - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Orca** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Cold/Frozen Ocean
**Key Features**: 4 directional variants (NE/NW/SE/SW)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter orca in cold/frozen ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 4 directional variants (NE/NW/SE/SW)

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Cold/Frozen Ocean

---

## Features & Functionality

### Core Features
- Large dolphin-like cetaceans
- Hunt polar bears
- Kill fish with tail slaps
- Pod behavior

### Special Mechanics
- Orca's Might effect: Swimming with pod grants increased attack speed
- Pod variants: South, North, East, West (different appearance)
- Hurt one → entire pod attacks
- Hunt Drowned and Guardians
- Young Cachalot predation

### AI Behaviors
- **info_0**: Bobs in/out of water
- **info_1**: Attacks polar bears
- **info_2**: Tail slap fish schools
- **info_3**: Pod behavior
- **info_4**: Defends pod members
- **info_5**: Grants Orca's Might to swimmers

### Variants
- South pod
- North pod
- East pod
- West pod

### Combat
- **info_0**: Attacks polar bears
- **info_1**: Tail slaps
- **info_2**: Pod attacks
- **info_3**: Hunts Drowned/Guardians
- **info_4**: Preys on young Cachalots


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Orca entity is summonable (`/summon xeenaa-alexs-mobs:orca`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 4 directional variants (NE/NW/SE/SW)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- OrcaEntity class
- OrcaModel class (GeckoLib model)
- OrcaRenderer class (GeckoLib renderer)
- Geometry file (orca.geo.json)
- Animation file (orca.animation.json)
- Texture file(s) (orca.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 4 directional variants (NE/NW/SE/SW)

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
1. **Complexity**: Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: 4 directional variants (NE/NW/SE/SW)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Orca cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `OrcaEntity.java`
- `OrcaModel.java`
- `OrcaRenderer.java`

### Resource Files
- `orca.geo.json`
- `orca.animation.json`
- `orca.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 5-7 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Neutral type
- [ ] Special features work: 4 directional variants (NE/NW/SE/SW)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: 4 directional variants (NE/NW/SE/SW)
- Biomes: Cold/Frozen Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 89

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
