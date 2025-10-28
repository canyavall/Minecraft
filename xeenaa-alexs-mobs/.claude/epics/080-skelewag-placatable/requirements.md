# Epic 80: Skelewag - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Skelewag** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Shipwreck, Ocean
**Key Features**: 2 pirate variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter skelewag in shipwreck, ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 2 pirate variants

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Shipwreck, Ocean

---

## Features & Functionality

### Core Features
- Undead remains of fish
- Patrol dark waters
- Highly aggressive

### Special Mechanics
- Slash skull back and forth
- Stab forwards (large damage)
- Quick and unpredictable movement
- Drowned jockeys: Drowned ride Skelewags (more deadly)
- Tattered flag: Some have shulker and crossbones flag

### Items & Interactions
- **Drops**: Fish Bones, Skelewag Skull
- **Crafted**: Strange Fish Finder (Fish Bones), Bone Meal
- **Skelewag Skull**: Used like sword (quick attacks, low damage), blocks/parries like shield

### AI Behaviors
- **info_0**: Patrols sunken ships
- **info_1**: Charges at dolphins/intruders
- **info_2**: Quick unpredictable movement

### Equipment & Gear
- Skelewag Skull (weapon/shield)

### Variants
- Regular
- With tattered flag

### Combat
- **info_0**: Slash attacks
- **info_1**: Stab attacks
- **info_2**: Quick movement
- **info_3**: Drowned jockey (mounted)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Skelewag entity is summonable (`/summon xeenaa-alexs-mobs:skelewag`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 2 pirate variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SkelewagEntity class
- SkelewagModel class (GeckoLib model)
- SkelewagRenderer class (GeckoLib renderer)
- Geometry file (skelewag.geo.json)
- Animation file (skelewag.animation.json)
- Texture file(s) (skelewag.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 2 pirate variants

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
2. **Special Features**: 2 pirate variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Skelewag cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SkelewagEntity.java`
- `SkelewagModel.java`
- `SkelewagRenderer.java`

### Resource Files
- `skelewag.geo.json`
- `skelewag.animation.json`
- `skelewag.png` (+ variants)
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
- [ ] AI behaves according to Placatable type
- [ ] Special features work: 2 pirate variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Medium
- Special features: 2 pirate variants
- Biomes: Shipwreck, Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 102

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
