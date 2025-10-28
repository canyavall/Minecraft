# Epic 58: Lobster - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Lobster** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Ocean (Cold)
**Key Features**: 6 color variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter lobster in ocean (cold)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 6 color variants

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Ocean (Cold)

---

## Features & Functionality

### Core Features
- Small aquatic crustaceans
- Pinch aggressors but don't pursue

### Special Mechanics
- Can be bucketed

### Items & Interactions
- **Drops**: Lobster Tails (cooked food)

### AI Behaviors
- **info_0**: Defends self by pinching
- **info_1**: Does not pursue

### Variants
- Multiple colors (red most common, rarer colors exist)

### Combat
- **info_0**: Pinch attack (defensive only)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Lobster entity is summonable (`/summon xeenaa-alexs-mobs:lobster`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 6 color variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- LobsterEntity class
- LobsterModel class (GeckoLib model)
- LobsterRenderer class (GeckoLib renderer)
- Geometry file (lobster.geo.json)
- Animation file (lobster.animation.json)
- Texture file(s) (lobster.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 6 color variants

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
2. **Special Features**: 6 color variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Lobster cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `LobsterEntity.java`
- `LobsterModel.java`
- `LobsterRenderer.java`

### Resource Files
- `lobster.geo.json`
- `lobster.animation.json`
- `lobster.png` (+ variants)
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
- [ ] AI behaves according to Neutral type
- [ ] Special features work: 6 color variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: 6 color variants
- Biomes: Ocean (Cold)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 80

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
