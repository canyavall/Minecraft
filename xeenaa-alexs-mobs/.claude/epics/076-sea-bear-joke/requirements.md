# Epic 76: Sea Bear - Joke Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Sea Bear** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Joke
**Biome(s)**: Ocean (rare)
**Key Features**: Joke mob, special spawn conditions

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter sea bear in ocean (rare)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (joke)
   - Joke-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Joke mob, special spawn conditions

### Game Mechanics

**Entity Type**: Joke Animal
**Size**: TBD (check original model)
**Behavior**: joke
**Health**: TBD
**Drops**: TBD
**Spawning**: Ocean (rare)

---

## Features & Functionality

### Core Features
- Joke entry
- References SpongeBob SquarePants

### Special Mechanics
- Things to avoid: Don't play clarinet, wear sombrero in goofy fashion, clown shoes, hoop skirt, screech like chimpanzee
- Defense: Draw Anti-Sea Bear Circle in sand with stick (only defense)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Sea Bear entity is summonable (`/summon xeenaa-alexs-mobs:sea-bear`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Joke behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Joke mob, special spawn conditions
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SeaBearEntity class
- SeaBearModel class (GeckoLib model)
- SeaBearRenderer class (GeckoLib renderer)
- Geometry file (sea-bear.geo.json)
- Animation file (sea-bear.animation.json)
- Texture file(s) (sea-bear.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Joke mob, special spawn conditions

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
2. **Special Features**: Joke mob, special spawn conditions
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Sea Bear cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SeaBearEntity.java`
- `SeaBearModel.java`
- `SeaBearRenderer.java`

### Resource Files
- `sea-bear.geo.json`
- `sea-bear.animation.json`
- `sea-bear.png` (+ variants)
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
- [ ] AI behaves according to Joke type
- [ ] Special features work: Joke mob, special spawn conditions
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Joke
- Complexity: Medium
- Special features: Joke mob, special spawn conditions
- Biomes: Ocean (rare)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 98

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
