# Epic 12: Anaconda - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Anaconda** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Swamp, Jungle
**Key Features**: Constriction mechanic, color variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter anaconda in swamp, jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Constriction mechanic, color variants

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Swamp, Jungle

---

## Features & Functionality

### Core Features
- Gigantic snakes
- At home on land and water
- Massive appetite

### Special Mechanics
- Ambush predator: Wraps around prey and suffocates
- Swallowed prey drops no items
- Digests slowly after eating
- Shedding: Becomes discolored after eating, sheds skin
- Anaconda Skin: Creates banner pattern or crafts Vine Lasso
- Vine Lasso: Captures any mob (including hostiles), can't tie to fences

### Items & Interactions
- **Drops**: Anaconda Skin
- **Eats**: Chicken (for pacification)
- **Crafted**: Vine Lasso, Banner Pattern

### AI Behaviors
- **info_0**: Ambush attacks
- **info_1**: Wraps and suffocates prey
- **info_2**: Digestion period
- **info_3**: Shedding behavior

### Breeding
- **Item**: Chicken
- **Note**: Must be pacified first

### Variants
- Green or Yellow

### Combat
- **info_0**: Wrap and suffocate attack
- **info_1**: Swallowed prey drops nothing


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Anaconda entity is summonable (`/summon xeenaa-alexs-mobs:anaconda`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Constriction mechanic, color variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- AnacondaEntity class
- AnacondaModel class (GeckoLib model)
- AnacondaRenderer class (GeckoLib renderer)
- Geometry file (anaconda.geo.json)
- Animation file (anaconda.animation.json)
- Texture file(s) (anaconda.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Constriction mechanic, color variants

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
2. **Special Features**: Constriction mechanic, color variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Anaconda cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `AnacondaEntity.java`
- `AnacondaModel.java`
- `AnacondaRenderer.java`

### Resource Files
- `anaconda.geo.json`
- `anaconda.animation.json`
- `anaconda.png` (+ variants)
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
- [ ] Special features work: Constriction mechanic, color variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Medium
- Special features: Constriction mechanic, color variants
- Biomes: Swamp, Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 34

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
