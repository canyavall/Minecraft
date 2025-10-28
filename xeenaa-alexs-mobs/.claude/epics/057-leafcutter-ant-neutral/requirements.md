# Epic 57: Leafcutter Ant - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Leafcutter Ant** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Jungle
**Key Features**: Queen variant, leaf carrying (3 variants), angry

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter leafcutter ant in jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Queen variant, leaf carrying (3 variants), angry

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle

---

## Features & Functionality

### Core Features
- Diminutive insects
- Massive colonies (up to 20 ants)
- Two forms: Workers and Queens

### Special Mechanics
- Workers: Harvest leaves, seen outside anthill
- Queen: Stomping anthill summons, much larger/stronger, one per colony
- Repopulation: Queen present + 5 leaves = new worker (max 10 ants)
- Without queen: Cannot repopulate
- Leafcutter Ant Chambers: Underground network, converts leaves to fungi
- Gongylidia farming: Ants farm and eat this fungi
- Chamber sunlight: Converts to dirt
- Chamber spreading: Spreads to adjacent dirt when harvested
- Fungi harvesting: Interact with fungal chamber, requires Shroomlight nearby or ants become hostile
- Feeding: Gongylidia heals, boosts colony (to queen), pacifies area
- Leafcutter Ant Pupa: Rare chamber drop, creates new colony on dirt/grass

### Items & Interactions
- **Harvests**: Leaves
- **Produces**: Gongylidia (fungi)
- **Drops**: Leafcutter Ant Pupa (rare from chambers)
- **Eats**: Gongylidia

### AI Behaviors
- **info_0**: Workers harvest leaves
- **info_1**: Queen commands colony
- **info_2**: Converts leaves to fungi
- **info_3**: Farming behavior
- **info_4**: Hostile without Shroomlight (when harvesting)
- **info_5**: Pacified by feeding

### Breeding
- **Natural**: Queen + 5 leaves = new worker
- **Colony**: Leafcutter Ant Pupa on dirt/grass

### Variants
- Workers
- Queen (larger, stronger)

### Combat
- **info_0**: Queen much stronger
- **info_1**: Colony attacks without Shroomlight


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Leafcutter Ant entity is summonable (`/summon xeenaa-alexs-mobs:leafcutter-ant`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Queen variant, leaf carrying (3 variants), angry
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- LeafcutterAntEntity class
- LeafcutterAntModel class (GeckoLib model)
- LeafcutterAntRenderer class (GeckoLib renderer)
- Geometry file (leafcutter-ant.geo.json)
- Animation file (leafcutter-ant.animation.json)
- Texture file(s) (leafcutter-ant.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Queen variant, leaf carrying (3 variants), angry

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
2. **Special Features**: Queen variant, leaf carrying (3 variants), angry
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Leafcutter Ant cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `LeafcutterAntEntity.java`
- `LeafcutterAntModel.java`
- `LeafcutterAntRenderer.java`

### Resource Files
- `leafcutter-ant.geo.json`
- `leafcutter-ant.animation.json`
- `leafcutter-ant.png` (+ variants)
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
- [ ] Special features work: Queen variant, leaf carrying (3 variants), angry
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Queen variant, leaf carrying (3 variants), angry
- Biomes: Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 79

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
