# Epic 18: Blue Jay - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Blue Jay** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Forest, Birch Forest
**Key Features**: Flying passive, shiny variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter blue jay in forest, birch forest
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Flying passive, shiny variant

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Forest, Birch Forest

---

## Features & Functionality

### Core Features
- Loud calls
- Picks up and eats dropped food
- Raises feather crest when making noise
- Relative of Crow

### Special Mechanics
- Glow Berry charming: Follow player when fed Glow Berries
- Raccoon bonding: If Raccoon also fed Glow Berries during charm, they bond
- Bonded Blue Jay perches on Raccoon's back
- Joins Raccoon in combat
- Can unbond by feeding Glow Berries again
- Seed feeding: Chirps song revealing location of nearby monsters
- Useful for finding hidden caves/structures

### Items & Interactions
- **Eats**: Dropped food, Glow Berries, Seeds
- **Drops**: None specified

### AI Behaviors
- **info_0**: Picks up dropped food
- **info_1**: Follows when charmed
- **info_2**: Perches on Raccoon
- **info_3**: Joins combat with bonded Raccoon
- **info_4**: Reveals monsters when fed seeds

### Breeding
- **Item**: Maggots or insect larvae

### Combat
- **info_0**: Fights alongside bonded Raccoon


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Blue Jay entity is summonable (`/summon xeenaa-alexs-mobs:blue-jay`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Flying passive, shiny variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BlueJayEntity class
- BlueJayModel class (GeckoLib model)
- BlueJayRenderer class (GeckoLib renderer)
- Geometry file (blue-jay.geo.json)
- Animation file (blue-jay.animation.json)
- Texture file(s) (blue-jay.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Flying passive, shiny variant

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
2. **Special Features**: Flying passive, shiny variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Blue Jay cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BlueJayEntity.java`
- `BlueJayModel.java`
- `BlueJayRenderer.java`

### Resource Files
- `blue-jay.geo.json`
- `blue-jay.animation.json`
- `blue-jay.png` (+ variants)
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
- [ ] Special features work: Flying passive, shiny variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Flying passive, shiny variant
- Biomes: Forest, Birch Forest

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 40

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
