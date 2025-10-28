# Epic 14: Bald Eagle - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Bald Eagle** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Mountains, Grove
**Key Features**: Falconry system, hood equipment

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter bald eagle in mountains, grove
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Falconry system, hood equipment

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Mountains, Grove

---

## Features & Functionality

### Core Features
- Large raptors
- Attacks salmon and rabbits
- Circle and slash tactic against large creatures

### Taming
- **Method**: Feed Fish Oil
- **Benefits**: Stay/follow/wander, falconry system

### Special Mechanics
- Falconry Glove: Pick up and hold eagles
- Launch: Punch to launch, attacks targeted mob and returns
- Falconry Hood: Direct control of eagle (pilot the eagle)
- Hood allows 150 block range, sneak to stop control
- Remove hood with shears
- Hunts salmon from lakes
- Can be injured by prey

### Items & Interactions
- **Eats**: Fish Oil (taming), Fish (healing)
- **Equipment**: Falconry Glove, Falconry Hood

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Dives for salmon
- **info_2**: Attacks rabbits
- **info_3**: Circle and slash tactic
- **info_4**: Returns to glove after attack
- **info_5**: Can be piloted with hood

### Equipment & Gear
- Falconry Glove (for handling)
- Falconry Hood (for control)

### Breeding
- **Item**: Rotten Flesh

### Combat
- **info_0**: Talon slash attacks
- **info_1**: Diving attacks
- **info_2**: Commanded attacks (falconry)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Bald Eagle entity is summonable (`/summon xeenaa-alexs-mobs:bald-eagle`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Falconry system, hood equipment
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BaldEagleEntity class
- BaldEagleModel class (GeckoLib model)
- BaldEagleRenderer class (GeckoLib renderer)
- Geometry file (bald-eagle.geo.json)
- Animation file (bald-eagle.animation.json)
- Texture file(s) (bald-eagle.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Falconry system, hood equipment

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
2. **Special Features**: Falconry system, hood equipment
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Bald Eagle cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BaldEagleEntity.java`
- `BaldEagleModel.java`
- `BaldEagleRenderer.java`

### Resource Files
- `bald-eagle.geo.json`
- `bald-eagle.animation.json`
- `bald-eagle.png` (+ variants)
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
- [ ] Special features work: Falconry system, hood equipment
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Falconry system, hood equipment
- Biomes: Mountains, Grove

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 36

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
