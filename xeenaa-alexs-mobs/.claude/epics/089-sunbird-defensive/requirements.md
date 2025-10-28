# Epic 89: Sunbird - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Sunbird** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: Mountains (peak)
**Key Features**: Fire-based, 3 variants (base/glow/shine)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter sunbird in mountains (peak)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Fire-based, 3 variants (base/glow/shine)

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: Mountains (peak)

---

## Features & Functionality

### Core Features
- Mythical crane
- Completely passive
- Holy creature

### Special Mechanics
- Blessing: Grants easier elytra flight, accelerated float up/down
- Curse: If attacked, attacker falls swiftly (often fatal)
- Ignites undead nearby (acts like sun)
- Immune to fire, heat, lava

### AI Behaviors
- **info_0**: Soars above mountains
- **info_1**: Grants blessing
- **info_2**: Applies curse if attacked
- **info_3**: Ignites undead

### Combat
- **info_0**: Curse (fall damage to attackers)
- **info_1**: Ignites undead passively


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Sunbird entity is summonable (`/summon xeenaa-alexs-mobs:sunbird`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Fire-based, 3 variants (base/glow/shine)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SunbirdEntity class
- SunbirdModel class (GeckoLib model)
- SunbirdRenderer class (GeckoLib renderer)
- Geometry file (sunbird.geo.json)
- Animation file (sunbird.animation.json)
- Texture file(s) (sunbird.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Fire-based, 3 variants (base/glow/shine)

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
2. **Special Features**: Fire-based, 3 variants (base/glow/shine)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Sunbird cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SunbirdEntity.java`
- `SunbirdModel.java`
- `SunbirdRenderer.java`

### Resource Files
- `sunbird.geo.json`
- `sunbird.animation.json`
- `sunbird.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Fire-based, 3 variants (base/glow/shine)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Medium
- Special features: Fire-based, 3 variants (base/glow/shine)
- Biomes: Mountains (peak)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 111

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
