# Epic 77: Seagull - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Seagull** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Beach, Ocean
**Key Features**: Flying scavenger, wingull variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter seagull in beach, ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Flying scavenger, wingull variant

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Beach, Ocean

---

## Features & Functionality

### Core Features
- Medium-sized birds
- Steals food items from hand and hotbar
- Picks up dropped food

### Special Mechanics
- Steals items every 1-3 minutes
- Treasure finding: With buried treasure map in off-hand, toss Lobster Tail
- Seagull that eats it sits directly above buried treasure

### Items & Interactions
- **Eats**: Any food items, Lobster Tails

### AI Behaviors
- **info_0**: Steals food
- **info_1**: Picks up dropped food
- **info_2**: Sits above buried treasure (when Lobster Tail fed)

### Breeding
- **Item**: Raw Cod


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Seagull entity is summonable (`/summon xeenaa-alexs-mobs:seagull`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Flying scavenger, wingull variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SeagullEntity class
- SeagullModel class (GeckoLib model)
- SeagullRenderer class (GeckoLib renderer)
- Geometry file (seagull.geo.json)
- Animation file (seagull.animation.json)
- Texture file(s) (seagull.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Flying scavenger, wingull variant

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
2. **Special Features**: Flying scavenger, wingull variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Seagull cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SeagullEntity.java`
- `SeagullModel.java`
- `SeagullRenderer.java`

### Resource Files
- `seagull.geo.json`
- `seagull.animation.json`
- `seagull.png` (+ variants)
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
- [ ] Special features work: Flying scavenger, wingull variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Flying scavenger, wingull variant
- Biomes: Beach, Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 99

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
