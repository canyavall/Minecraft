# Epic 13: Anteater - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Anteater** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Jungle, Savanna
**Key Features**: Attacks leaf cutter ants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter anteater in jungle, savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Attacks leaf cutter ants

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle, Savanna

---

## Features & Functionality

### Core Features
- Insectivorous mammals
- Large claws
- Extremely long tongue

### Special Mechanics
- Rears up when attacked, slashes with claws (great damage)
- Preys on Leafcutter Ants
- Tears at Anthills to find ants
- Uncovers: Dirt, rooted dirt, pupae, tubers, fungus from anthills
- Scoops ants with tongue
- Baby Anteaters ride on parent's back

### Items & Interactions
- **Eats**: Leafcutter Ants, Leafcutter Ant Pupae
- **Uncovers**: Dirt, rooted dirt, pupae, tubers, fungus

### AI Behaviors
- **info_0**: Seeks Leafcutter Ants
- **info_1**: Attacks Anthills
- **info_2**: Eats ants with tongue
- **info_3**: Baby riding behavior
- **info_4**: Defensive when attacked

### Breeding
- **Item**: Leafcutter Ant Pupae

### Combat
- **info_0**: Rear up and slash with claws


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Anteater entity is summonable (`/summon xeenaa-alexs-mobs:anteater`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Attacks leaf cutter ants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- AnteaterEntity class
- AnteaterModel class (GeckoLib model)
- AnteaterRenderer class (GeckoLib renderer)
- Geometry file (anteater.geo.json)
- Animation file (anteater.animation.json)
- Texture file(s) (anteater.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Attacks leaf cutter ants

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
2. **Special Features**: Attacks leaf cutter ants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Anteater cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `AnteaterEntity.java`
- `AnteaterModel.java`
- `AnteaterRenderer.java`

### Resource Files
- `anteater.geo.json`
- `anteater.animation.json`
- `anteater.png` (+ variants)
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
- [ ] Special features work: Attacks leaf cutter ants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Attacks leaf cutter ants
- Biomes: Jungle, Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 35

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
