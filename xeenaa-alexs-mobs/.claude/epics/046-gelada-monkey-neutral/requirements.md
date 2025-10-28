# Epic 46: Gelada Monkey - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Gelada Monkey** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Mountain biomes
**Key Features**: Leader variant, angry states

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter gelada monkey in mountain biomes
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Leader variant, angry states

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Mountain biomes

---

## Features & Functionality

### Core Features
- Large monkeys
- Eat almost exclusively grass
- Group hierarchy with leader (large mane)

### Special Mechanics
- Grooming behavior
- Leader fights (spectacular, not seriously hurt)
- Wheat feeding: Entice them to destroy grass and tall grass in area (land clearing)
- Flip lips to reveal gigantic teeth
- Stand on hind legs showing crimson chest (rare)

### Items & Interactions
- **Eats**: Wheat (grass clearing), Dead Bushes (breeding)

### AI Behaviors
- **info_0**: Grooming
- **info_1**: Fiercely territorial
- **info_2**: Leader hierarchy
- **info_3**: Grass clearing when fed wheat

### Breeding
- **Item**: Dead Bushes

### Variants
- Leader (large mane)
- Regular

### Combat
- **info_0**: Territorial defense
- **info_1**: Leader fights (display)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Gelada Monkey entity is summonable (`/summon xeenaa-alexs-mobs:gelada-monkey`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Leader variant, angry states
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- GeladaMonkeyEntity class
- GeladaMonkeyModel class (GeckoLib model)
- GeladaMonkeyRenderer class (GeckoLib renderer)
- Geometry file (gelada-monkey.geo.json)
- Animation file (gelada-monkey.animation.json)
- Texture file(s) (gelada-monkey.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Leader variant, angry states

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
2. **Special Features**: Leader variant, angry states
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Gelada Monkey cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `GeladaMonkeyEntity.java`
- `GeladaMonkeyModel.java`
- `GeladaMonkeyRenderer.java`

### Resource Files
- `gelada-monkey.geo.json`
- `gelada-monkey.animation.json`
- `gelada-monkey.png` (+ variants)
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
- [ ] Special features work: Leader variant, angry states
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Leader variant, angry states
- Biomes: Mountain biomes

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 68

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
