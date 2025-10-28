# Epic 97: Underminer - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Underminer** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Underground
**Key Features**: 2 variants + dwarf variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter underminer in underground
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 2 variants + dwarf variant

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Underground

---

## Features & Functionality

### Core Features
- Rare ghostly inhabitants
- Ethereal (walk through walls)
- Only damaged by magic

### Special Mechanics
- Appears after time spent in mineshaft
- Disappears if approached
- Mines blocks with ghostly pickaxe (cannot destroy, reveals hidden ores)
- Ore-specific mining: Drop ore block near Underminer, becomes visible and seeks/mines that ore
- Mistrust of newcomers
- Stalks halls for hundreds of years

### Items & Interactions
- **Drops**: Ghostly Pickaxe (when slain with magic)
- **Ghostly Pickaxe**: Stores 9 stacks when inventory full, returns items when space available, repairs with phantom membranes

### AI Behaviors
- **info_0**: Stalks mineshafts
- **info_1**: Mines blocks (reveals ores)
- **info_2**: Disappears when approached
- **info_3**: Seeks specific ore when given

### Equipment & Gear
- Ghostly Pickaxe

### Combat
- **info_0**: No threat (passive unless attacked)
- **info_1**: Only damaged by magic


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Underminer entity is summonable (`/summon xeenaa-alexs-mobs:underminer`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 2 variants + dwarf variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- UnderminerEntity class
- UnderminerModel class (GeckoLib model)
- UnderminerRenderer class (GeckoLib renderer)
- Geometry file (underminer.geo.json)
- Animation file (underminer.animation.json)
- Texture file(s) (underminer.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 2 variants + dwarf variant

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
2. **Special Features**: 2 variants + dwarf variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Underminer cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `UnderminerEntity.java`
- `UnderminerModel.java`
- `UnderminerRenderer.java`

### Resource Files
- `underminer.geo.json`
- `underminer.animation.json`
- `underminer.png` (+ variants)
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
- [ ] Special features work: 2 variants + dwarf variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: 2 variants + dwarf variant
- Biomes: Underground

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 119

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
