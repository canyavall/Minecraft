# Epic 24: Catfish - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Catfish** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: River, Swamp
**Key Features**: 3 size variants (small/medium/large)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter catfish in river, swamp
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 3 size variants (small/medium/large)

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: River, Swamp

---

## Features & Functionality

### Core Features
- Lethargic bottom feeders
- Three sizes: Small, Medium, Large
- Not picky eaters

### Special Mechanics
- Vacuum items (Small/Medium): Small stores 3 stacks, Medium stores 9 stacks
- Eat mobs (Large): Eats mobs shorter than a block (1 at a time)
- Regurgitation: Injured or encounters sea pickle → spits out contents
- Hate sea pickles
- Sea Lantern luring: Attracted to flashing light
- Can be bucketed (contents preserved)
- Used for item/mob transport
- Won't despawn if ate items or bucketed

### Items & Interactions
- **Eats**: Items (Small/Medium), Mobs shorter than block (Large)
- **Repelled by**: Sea Pickles
- **Attracted to**: Sea Lanterns

### AI Behaviors
- **info_0**: Bottom feeding
- **info_1**: Vacuum behavior (items/mobs)
- **info_2**: Luring to sea lanterns
- **info_3**: Regurgitation (injured/pickle)
- **info_4**: Poor vision

### Variants
- Small (3 stacks)
- Medium (9 stacks)
- Large (1 mob, swamps only)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Catfish entity is summonable (`/summon xeenaa-alexs-mobs:catfish`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 3 size variants (small/medium/large)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CatfishEntity class
- CatfishModel class (GeckoLib model)
- CatfishRenderer class (GeckoLib renderer)
- Geometry file (catfish.geo.json)
- Animation file (catfish.animation.json)
- Texture file(s) (catfish.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 3 size variants (small/medium/large)

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
2. **Special Features**: 3 size variants (small/medium/large)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Catfish cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CatfishEntity.java`
- `CatfishModel.java`
- `CatfishRenderer.java`

### Resource Files
- `catfish.geo.json`
- `catfish.animation.json`
- `catfish.png` (+ variants)
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
- [ ] Special features work: 3 size variants (small/medium/large)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Medium
- Special features: 3 size variants (small/medium/large)
- Biomes: River, Swamp

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 46

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
