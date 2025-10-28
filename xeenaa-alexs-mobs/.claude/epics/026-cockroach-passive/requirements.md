# Epic 26: Cockroach - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Cockroach** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Underground, Buildings
**Key Features**: Small pest, simple AI

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter cockroach in underground, buildings
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Small pest, simple AI

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Underground, Buildings

---

## Features & Functionality

### Core Features
- Foul insects
- Avoid light
- Prolific breeders
- Nuisance pest

### Special Mechanics
- Light avoidance: Scatter from torches
- Vulnerable to predators in light
- Ootheca: Egg cases with multiple nymphs
- Bread/Sugar feeding: Makes them comfortable
- Eat dropped food (dangerous in caves)
- Fondness for Maracas
- Beheaded form: Can lose head but function normally
- Prey to Cave Centipedes

### Items & Interactions
- **Eats**: Bread, Sugar (comfort), dropped food
- **Drops**: Ootheca (egg cases), Cockroach Wing Fragments
- **Crafted**: Cockroach Wing (9 fragments), Bug Pheromones Potion (undetected by hostile arthropods)

### AI Behaviors
- **info_0**: Avoids light
- **info_1**: Scatters from torches
- **info_2**: Eats dropped food
- **info_3**: Prolific breeding
- **info_4**: Functions beheaded

### Breeding
- **Item**: Sugar
- **Produces**: Ootheca (egg cases)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Cockroach entity is summonable (`/summon xeenaa-alexs-mobs:cockroach`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Small pest, simple AI
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CockroachEntity class
- CockroachModel class (GeckoLib model)
- CockroachRenderer class (GeckoLib renderer)
- Geometry file (cockroach.geo.json)
- Animation file (cockroach.animation.json)
- Texture file(s) (cockroach.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Small pest, simple AI

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
2. **Special Features**: Small pest, simple AI
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Cockroach cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CockroachEntity.java`
- `CockroachModel.java`
- `CockroachRenderer.java`

### Resource Files
- `cockroach.geo.json`
- `cockroach.animation.json`
- `cockroach.png` (+ variants)
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
- [ ] Special features work: Small pest, simple AI
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Small pest, simple AI
- Biomes: Underground, Buildings

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 48

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
