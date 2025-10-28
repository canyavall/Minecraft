# Epic 70: Raccoon - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Raccoon** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Forest, Taiga
**Key Features**: Item theft, bandana/rigby variants, glow eyes

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter raccoon in forest, taiga
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Item theft, bandana/rigby variants, glow eyes

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Forest, Taiga

---

## Features & Functionality

### Core Features
- Small mammals
- Glowing eyes at night
- Known thieves
- "Washing" behavior

### Taming
- **Method**: Give Egg near water (they wash it)
- **Benefits**: Sit/follow/wander, bandana decoration

### Special Mechanics
- Begs for food when seeing it held
- Steals from chests and barrels
- Washing behavior: Washes food near water before eating
- Bandana: Can wear carpet as bandana (remove with shears)
- Steals from villager inventories

### Items & Interactions
- **Eats**: Egg (taming near water), Bread (breeding)
- **Drops**: Raccoon Tail
- **Crafted**: Frontiersman's Cap (speed bonus when sneaking)

### AI Behaviors
- **info_0**: Sit/follow/wander modes
- **info_1**: Begs for food
- **info_2**: Steals from containers
- **info_3**: Steals from villagers
- **info_4**: Washing behavior
- **info_5**: Can be aggressive

### Equipment & Gear
- Carpet (bandana decoration)

### Breeding
- **Item**: Bread

### Combat
- **info_0**: Aggressive when provoked


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Raccoon entity is summonable (`/summon xeenaa-alexs-mobs:raccoon`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Item theft, bandana/rigby variants, glow eyes
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- RaccoonEntity class
- RaccoonModel class (GeckoLib model)
- RaccoonRenderer class (GeckoLib renderer)
- Geometry file (raccoon.geo.json)
- Animation file (raccoon.animation.json)
- Texture file(s) (raccoon.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Item theft, bandana/rigby variants, glow eyes

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
2. **Special Features**: Item theft, bandana/rigby variants, glow eyes
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Raccoon cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `RaccoonEntity.java`
- `RaccoonModel.java`
- `RaccoonRenderer.java`

### Resource Files
- `raccoon.geo.json`
- `raccoon.animation.json`
- `raccoon.png` (+ variants)
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
- [ ] Special features work: Item theft, bandana/rigby variants, glow eyes
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Item theft, bandana/rigby variants, glow eyes
- Biomes: Forest, Taiga

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 92

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
