# Epic 29: Cosmic Cod - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Cosmic Cod** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: The End
**Key Features**: Swimming fish variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter cosmic cod in the end
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Swimming fish variant

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: The End

---

## Features & Functionality

### Core Features
- Hydrophobic (damaged by water)
- Float in void instead of water
- School behavior
- Prey to Cosmaws

### Special Mechanics
- Can be bucketed (empty bucket)
- School teleportation: If one attacked, entire school teleports away
- Can place Cod in Enderiophage Capsid to transform

### Items & Interactions
- **Used for**: Taming Cosmaws

### AI Behaviors
- **info_0**: Floats in void
- **info_1**: School behavior
- **info_2**: Teleports when threatened


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Cosmic Cod entity is summonable (`/summon xeenaa-alexs-mobs:cosmic-cod`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Swimming fish variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CosmicCodEntity class
- CosmicCodModel class (GeckoLib model)
- CosmicCodRenderer class (GeckoLib renderer)
- Geometry file (cosmic-cod.geo.json)
- Animation file (cosmic-cod.animation.json)
- Texture file(s) (cosmic-cod.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Swimming fish variant

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
2. **Special Features**: Swimming fish variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Cosmic Cod cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CosmicCodEntity.java`
- `CosmicCodModel.java`
- `CosmicCodRenderer.java`

### Resource Files
- `cosmic-cod.geo.json`
- `cosmic-cod.animation.json`
- `cosmic-cod.png` (+ variants)
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
- [ ] Special features work: Swimming fish variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Swimming fish variant
- Biomes: The End

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 51

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
