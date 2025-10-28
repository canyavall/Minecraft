# Epic 86: Straddler - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Straddler** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Basalt Deltas (Nether)
**Key Features**: Lava strider

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter straddler in basalt deltas (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Lava strider

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Basalt Deltas (Nether)

---

## Features & Functionality

### Core Features
- Giant relatives of Strider
- Aggressive unlike Striders
- Hardy on land, water, lava
- Netted hair

### Special Mechanics
- Stradpole launch: Shoots Stradpoles from hair at targets
- Hardier than Striders
- Intruder aggression

### Items & Interactions
- **Drops**: Basalt, Straddlite (rare)
- **Crafted**: Straddleboard (floats in lava, sinks in water, dyable, fast but risky, can snap if hits ceiling/wall hard)

### AI Behaviors
- **info_0**: Aggressive to intruders
- **info_1**: Launches Stradpoles
- **info_2**: Hardy in multiple environments

### Combat
- **info_0**: Stradpole projectiles
- **info_1**: Decent damage


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Straddler entity is summonable (`/summon xeenaa-alexs-mobs:straddler`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Lava strider
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- StraddlerEntity class
- StraddlerModel class (GeckoLib model)
- StraddlerRenderer class (GeckoLib renderer)
- Geometry file (straddler.geo.json)
- Animation file (straddler.animation.json)
- Texture file(s) (straddler.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Lava strider

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
2. **Special Features**: Lava strider
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Straddler cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `StraddlerEntity.java`
- `StraddlerModel.java`
- `StraddlerRenderer.java`

### Resource Files
- `straddler.geo.json`
- `straddler.animation.json`
- `straddler.png` (+ variants)
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
- [ ] Special features work: Lava strider
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Lava strider
- Biomes: Basalt Deltas (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 108

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
