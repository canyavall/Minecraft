# Epic 91: Tasmanian Devil - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Tasmanian Devil** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Forest
**Key Features**: Aggressive scavenger, angry state

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter tasmanian devil in forest
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Aggressive scavenger, angry state

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Forest

---

## Features & Functionality

### Core Features
- Small diminutive marsupials
- Black and white coats
- Strange growls

### Special Mechanics
- Group behavior: Sit/bask in groups
- Pack hunting: All nearby devils join hunt (rabbits, chickens)
- Rotten Flesh feeding: Unleashes howl that scares away monsters temporarily

### Items & Interactions
- **Eats**: Rotten Flesh (howl), Any meat (breeding)

### AI Behaviors
- **info_0**: Sits/basks in groups
- **info_1**: Pack hunting
- **info_2**: Defends self despite size
- **info_3**: Howl scares monsters

### Breeding
- **Item**: Any meat

### Combat
- **info_0**: Defends self
- **info_1**: Pack attacks
- **info_2**: Howl (monster scaring)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Tasmanian Devil entity is summonable (`/summon xeenaa-alexs-mobs:tasmanian-devil`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Aggressive scavenger, angry state
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- TasmanianDevilEntity class
- TasmanianDevilModel class (GeckoLib model)
- TasmanianDevilRenderer class (GeckoLib renderer)
- Geometry file (tasmanian-devil.geo.json)
- Animation file (tasmanian-devil.animation.json)
- Texture file(s) (tasmanian-devil.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Aggressive scavenger, angry state

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
2. **Special Features**: Aggressive scavenger, angry state
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Tasmanian Devil cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `TasmanianDevilEntity.java`
- `TasmanianDevilModel.java`
- `TasmanianDevilRenderer.java`

### Resource Files
- `tasmanian-devil.geo.json`
- `tasmanian-devil.animation.json`
- `tasmanian-devil.png` (+ variants)
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
- [ ] AI behaves according to Neutral type
- [ ] Special features work: Aggressive scavenger, angry state
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Simple
- Special features: Aggressive scavenger, angry state
- Biomes: Forest

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 113

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
