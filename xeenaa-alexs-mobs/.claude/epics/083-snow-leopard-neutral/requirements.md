# Epic 83: Snow Leopard - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Snow Leopard** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Mountains, Snowy Slopes
**Key Features**: Stealth predator, sleeping state

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter snow leopard in mountains, snowy slopes
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Stealth predator, sleeping state

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Mountains, Snowy Slopes

---

## Features & Functionality

### Core Features
- Big cats
- Voracious hunters
- Not aggressive to humans
- Stalk sheep (favorite prey)

### Special Mechanics
- Stealth: Arch back, sneak up on prey
- Leap attack: Devastating
- Looting effect: Kills often yield extra drops (high looting level)
- Defend themselves and offspring

### Items & Interactions
- **Eats**: Moose Ribs (breeding)

### AI Behaviors
- **info_0**: Sits/sleeps on snowy peaks
- **info_1**: Stalks prey
- **info_2**: Arches back while sneaking
- **info_3**: Leap attacks
- **info_4**: Extra drops from kills

### Breeding
- **Item**: Moose Ribs

### Combat
- **info_0**: Stealth stalking
- **info_1**: Devastating leap attack
- **info_2**: Claw attacks
- **info_3**: Not aggressive to humans
- **info_4**: Defends self/offspring


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Snow Leopard entity is summonable (`/summon xeenaa-alexs-mobs:snow-leopard`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Stealth predator, sleeping state
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SnowLeopardEntity class
- SnowLeopardModel class (GeckoLib model)
- SnowLeopardRenderer class (GeckoLib renderer)
- Geometry file (snow-leopard.geo.json)
- Animation file (snow-leopard.animation.json)
- Texture file(s) (snow-leopard.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Stealth predator, sleeping state

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
2. **Special Features**: Stealth predator, sleeping state
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Snow Leopard cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SnowLeopardEntity.java`
- `SnowLeopardModel.java`
- `SnowLeopardRenderer.java`

### Resource Files
- `snow-leopard.geo.json`
- `snow-leopard.animation.json`
- `snow-leopard.png` (+ variants)
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
- [ ] Special features work: Stealth predator, sleeping state
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Stealth predator, sleeping state
- Biomes: Mountains, Snowy Slopes

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 105

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
