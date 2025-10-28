# Epic 94: Toucan - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Toucan** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Jungle
**Key Features**: 4 random variants + 2 special (gold/sam)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter toucan in jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 4 random variants + 2 special (gold/sam)

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle

---

## Features & Functionality

### Core Features
- Colorful birds with massive beaks
- Affinity for fruits

### Special Mechanics
- Sapling placement: Fed fruit → places corresponding sapling
- Apple → Oak sapling, Banana → Jungle sapling
- Golden Apple: Transforms to golden toucan, faster sapling placement
- Enchanted Golden Apple: Permanent mode, no constant feeding needed

### Items & Interactions
- **Eats**: Fruits (apples, bananas, golden apples, enchanted golden apples)

### AI Behaviors
- **info_0**: Perches on trees
- **info_1**: Places saplings when fed

### Breeding
- **Item**: Eggs

### Variants
- Variety of dazzling colors


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Toucan entity is summonable (`/summon xeenaa-alexs-mobs:toucan`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 4 random variants + 2 special (gold/sam)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- ToucanEntity class
- ToucanModel class (GeckoLib model)
- ToucanRenderer class (GeckoLib renderer)
- Geometry file (toucan.geo.json)
- Animation file (toucan.animation.json)
- Texture file(s) (toucan.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 4 random variants + 2 special (gold/sam)

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
2. **Special Features**: 4 random variants + 2 special (gold/sam)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Toucan cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `ToucanEntity.java`
- `ToucanModel.java`
- `ToucanRenderer.java`

### Resource Files
- `toucan.geo.json`
- `toucan.animation.json`
- `toucan.png` (+ variants)
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
- [ ] Special features work: 4 random variants + 2 special (gold/sam)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: 4 random variants + 2 special (gold/sam)
- Biomes: Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 116

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
