# Epic 78: Seal - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Seal** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Cold Ocean, Frozen Ocean
**Key Features**: 2 arctic variants, 2 brown variants, baby/crying

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter seal in cold ocean, frozen ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 2 arctic variants, 2 brown variants, baby/crying

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Cold Ocean, Frozen Ocean

---

## Features & Functionality

### Core Features
- Mostly aquatic mammals
- Bask and sleep on sand/ice
- Extremely adept swimmers
- Prey to Orcas

### Special Mechanics
- Primitive trade: Feed 3 fish to basking seal on land
- Seal scrounge seabed for item to return
- Usually junk (kelp, sand) but can be rare (shark teeth, scutes, nautilus shells)
- Entire pod flees if one attacked (swim to water)

### Items & Interactions
- **Eats**: Fish (3 for trade)
- **Returns**: Kelp, sand, shark teeth, scutes, nautilus shells

### AI Behaviors
- **info_0**: Basks on land
- **info_1**: Fast swimmer
- **info_2**: Flees to water when attacked
- **info_3**: Pod behavior

### Breeding
- **Item**: Lobster Tails


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Seal entity is summonable (`/summon xeenaa-alexs-mobs:seal`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 2 arctic variants, 2 brown variants, baby/crying
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SealEntity class
- SealModel class (GeckoLib model)
- SealRenderer class (GeckoLib renderer)
- Geometry file (seal.geo.json)
- Animation file (seal.animation.json)
- Texture file(s) (seal.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 2 arctic variants, 2 brown variants, baby/crying

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
2. **Special Features**: 2 arctic variants, 2 brown variants, baby/crying
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Seal cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SealEntity.java`
- `SealModel.java`
- `SealRenderer.java`

### Resource Files
- `seal.geo.json`
- `seal.animation.json`
- `seal.png` (+ variants)
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
- [ ] AI behaves according to Passive type
- [ ] Special features work: 2 arctic variants, 2 brown variants, baby/crying
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Medium
- Special features: 2 arctic variants, 2 brown variants, baby/crying
- Biomes: Cold Ocean, Frozen Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 100

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
