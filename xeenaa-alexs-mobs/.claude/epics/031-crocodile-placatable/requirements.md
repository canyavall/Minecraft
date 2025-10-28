# Epic 31: Crocodile - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Crocodile** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Swamp, River
**Key Features**: 2 variants, crown equipment

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter crocodile in swamp, river
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 2 variants, crown equipment

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Swamp, River

---

## Features & Functionality

### Core Features
- Massive reptiles
- Lunge and pull underwater
- Devastating death roll

### Taming
- **Method**: Imprint eggs (hatch near player)
- **Benefits**: Defends birth area, can be made to stay and bask

### Special Mechanics
- Lunge attack can be blocked (stuns crocodile)
- Breeding: Come ashore to lay eggs
- Eggs: Parents become aggressive if damaged
- Imprinted babies defend birth area from monsters
- Stay/bask command available

### Items & Interactions
- **Drops**: Crocodile Scutes (on adult or death)
- **Eats**: Rotten Flesh (breeding)
- **Crafted**: Crocodile Chestplate (increased swim speed), Animal Dictionary copy

### AI Behaviors
- **info_0**: Lunge attack
- **info_1**: Pull underwater
- **info_2**: Death roll
- **info_3**: Lay eggs on shore
- **info_4**: Defend eggs
- **info_5**: Defends birth area (imprinted)

### Breeding
- **Item**: Rotten Flesh
- **Produces**: Eggs on shore → Imprinted babies

### Combat
- **info_0**: Lunge attack (blockable)
- **info_1**: Pull underwater
- **info_2**: Death roll


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Crocodile entity is summonable (`/summon xeenaa-alexs-mobs:crocodile`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 2 variants, crown equipment
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CrocodileEntity class
- CrocodileModel class (GeckoLib model)
- CrocodileRenderer class (GeckoLib renderer)
- Geometry file (crocodile.geo.json)
- Animation file (crocodile.animation.json)
- Texture file(s) (crocodile.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 2 variants, crown equipment

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
1. **Complexity**: Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: 2 variants, crown equipment
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Crocodile cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CrocodileEntity.java`
- `CrocodileModel.java`
- `CrocodileRenderer.java`

### Resource Files
- `crocodile.geo.json`
- `crocodile.animation.json`
- `crocodile.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 5-7 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Placatable type
- [ ] Special features work: 2 variants, crown equipment
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Complex
- Special features: 2 variants, crown equipment
- Biomes: Swamp, River

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 53

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
