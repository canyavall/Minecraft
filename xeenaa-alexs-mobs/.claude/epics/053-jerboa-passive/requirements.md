# Epic 53: Jerboa - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Jerboa** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Desert
**Key Features**: Small rodent, sleeping state

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter jerboa in desert
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Small rodent, sleeping state

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert

---

## Features & Functionality

### Core Features
- Small nocturnal mammals
- Very long back legs
- Sleeps during day, bounces at night
- Prey to rattlesnakes, foxes, cats

### Special Mechanics
- Begs for seeds
- Giving seeds grants Fleet-Footed effect (speed boost when running+jumping)
- Fed Jerboa won't despawn

### Items & Interactions
- **Eats**: Seeds, Maggots
- **Drops**: None specified

### AI Behaviors
- **info_0**: Sleeps during day
- **info_1**: Bounces at night
- **info_2**: Begs for seeds
- **info_3**: Shy/flees from predators

### Breeding
- **Item**: Maggots or small insects


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Jerboa entity is summonable (`/summon xeenaa-alexs-mobs:jerboa`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Small rodent, sleeping state
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- JerboaEntity class
- JerboaModel class (GeckoLib model)
- JerboaRenderer class (GeckoLib renderer)
- Geometry file (jerboa.geo.json)
- Animation file (jerboa.animation.json)
- Texture file(s) (jerboa.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Small rodent, sleeping state

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
2. **Special Features**: Small rodent, sleeping state
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Jerboa cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `JerboaEntity.java`
- `JerboaModel.java`
- `JerboaRenderer.java`

### Resource Files
- `jerboa.geo.json`
- `jerboa.animation.json`
- `jerboa.png` (+ variants)
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
- [ ] Special features work: Small rodent, sleeping state
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Small rodent, sleeping state
- Biomes: Desert

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 75

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
