# Epic 30: Crimson Mosquito - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Crimson Mosquito** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Crimson Forest (Nether)
**Key Features**: Blood variants, fly state

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter crimson mosquito in crimson forest (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Blood variants, fly state

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Crimson Forest (Nether)

---

## Features & Functionality

### Core Features
- Hideous mutant flying insect
- Transformation from Fly
- Voracious

### Special Mechanics
- Fly transformation: Garden fly in Nether → Crimson Mosquito
- Attack sequence: Lunges, latches, sucks blood → inflates → spits blood back at target
- Blood Sprayer crafting: Proboscis + Blood Sac = weapon using Mosquito Spit (area damage)
- Blood Sac ammunition

### Items & Interactions
- **Drops**: Blood Sac (when inflated), Crimson Mosquito Proboscis
- **Crafted**: Blood Sprayer (weapon), Crimson Mosquito Larva (Maggot + Proboscis)

### AI Behaviors
- **info_0**: Seeks warm-blooded prey
- **info_1**: Lunge attack
- **info_2**: Latches and sucks
- **info_3**: Inflates with blood
- **info_4**: Spits blood back

### Combat
- **info_0**: Lunge and latch
- **info_1**: Blood sucking
- **info_2**: Blood spitting (area damage)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Crimson Mosquito entity is summonable (`/summon xeenaa-alexs-mobs:crimson-mosquito`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Blood variants, fly state
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CrimsonMosquitoEntity class
- CrimsonMosquitoModel class (GeckoLib model)
- CrimsonMosquitoRenderer class (GeckoLib renderer)
- Geometry file (crimson-mosquito.geo.json)
- Animation file (crimson-mosquito.animation.json)
- Texture file(s) (crimson-mosquito.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Blood variants, fly state

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
2. **Special Features**: Blood variants, fly state
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Crimson Mosquito cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CrimsonMosquitoEntity.java`
- `CrimsonMosquitoModel.java`
- `CrimsonMosquitoRenderer.java`

### Resource Files
- `crimson-mosquito.geo.json`
- `crimson-mosquito.animation.json`
- `crimson-mosquito.png` (+ variants)
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
- [ ] AI behaves according to Placatable type
- [ ] Special features work: Blood variants, fly state
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Medium
- Special features: Blood variants, fly state
- Biomes: Crimson Forest (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 52

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
