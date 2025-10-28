# Epic 28: Cosmaw - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Cosmaw** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: The End
**Key Features**: Floating End dimension mob

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter cosmaw in the end
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Floating End dimension mob

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: The End

---

## Features & Functionality

### Core Features
- Enigmatic creatures
- Evaded classification (traits of fish, arthropods, molluscs)
- Large claw-mouth

### Taming
- **Method**: Feed multiple Cosmic Cod
- **Benefits**: Rescues falling owner from void

### Special Mechanics
- Void rescue: Catches falling owner with claw-mouth, returns to land
- Essential for End exploration
- Does NOT defend in combat

### Items & Interactions
- **Eats**: Cosmic Cod (taming), Chorus Fruit (healing)

### AI Behaviors
- **info_0**: Wanders outer End islands
- **info_1**: Rescues falling owner
- **info_2**: Does not fight

### Breeding
- **Item**: Cosmic Cod


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Cosmaw entity is summonable (`/summon xeenaa-alexs-mobs:cosmaw`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Floating End dimension mob
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CosmawEntity class
- CosmawModel class (GeckoLib model)
- CosmawRenderer class (GeckoLib renderer)
- Geometry file (cosmaw.geo.json)
- Animation file (cosmaw.animation.json)
- Texture file(s) (cosmaw.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Floating End dimension mob

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
2. **Special Features**: Floating End dimension mob
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Cosmaw cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CosmawEntity.java`
- `CosmawModel.java`
- `CosmawRenderer.java`

### Resource Files
- `cosmaw.geo.json`
- `cosmaw.animation.json`
- `cosmaw.png` (+ variants)
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
- [ ] Special features work: Floating End dimension mob
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Floating End dimension mob
- Biomes: The End

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 50

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
