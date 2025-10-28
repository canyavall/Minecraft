# Epic 15: Banana Slug - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Banana Slug** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Forest (wet)
**Key Features**: 4 random color variants, slime trail

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter banana slug in forest (wet)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 4 random color variants, slime trail

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Forest (wet)

---

## Features & Functionality

### Core Features
- Sticks to sides of blocks
- Moves slowly through undergrowth
- Drops slime while moving

### Special Mechanics
- Drops Banana Slug Slime naturally while moving
- Slime Block converts water to crystallized mucus (draining mechanic)
- Mucus crystals decay when slime block is broken
- Requires silk touch to preserve crystallized mucus

### Items & Interactions
- **Drops**: Banana Slug Slime
- **Crafted**: Banana Slug Slime Block

### AI Behaviors
- **info_0**: Passive movement
- **info_1**: Sticks to blocks

### Breeding
- **Item**: Brown Mushrooms
- **info_1**: Multiple color variants (not all banana-yellow)

### Variants
- Various colors


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Banana Slug entity is summonable (`/summon xeenaa-alexs-mobs:banana-slug`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 4 random color variants, slime trail
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BananaSlugEntity class
- BananaSlugModel class (GeckoLib model)
- BananaSlugRenderer class (GeckoLib renderer)
- Geometry file (banana-slug.geo.json)
- Animation file (banana-slug.animation.json)
- Texture file(s) (banana-slug.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 4 random color variants, slime trail

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
2. **Special Features**: 4 random color variants, slime trail
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Banana Slug cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BananaSlugEntity.java`
- `BananaSlugModel.java`
- `BananaSlugRenderer.java`

### Resource Files
- `banana-slug.geo.json`
- `banana-slug.animation.json`
- `banana-slug.png` (+ variants)
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
- [ ] Special features work: 4 random color variants, slime trail
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: 4 random color variants, slime trail
- Biomes: Forest (wet)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 37

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
