# Epic 62: Mimicube - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Mimicube** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: The End
**Key Features**: Mimic mechanics, outer overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter mimicube in the end
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Mimic mechanics, outer overlay

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: The End

---

## Features & Functionality

### Core Features
- Relatives of slimes/magma cubes
- Guard End Cities
- Powerful magic

### Special Mechanics
- Mimic attack: Copies attacker's weapons and armor
- Can learn: Shield, sword, bow, crossbow, trident, food
- Powerful if attacker uses strong equipment
- Mimicream crafting: 8 mimicream + damageable item = copied tool/equipment with enchantments (zero durability)
- Limitations on copying

### Items & Interactions
- **Drops**: Mimicream
- **Crafted**: Mimicked tools/equipment (zero durability, needs repair)

### AI Behaviors
- **info_0**: Guards End Cities
- **info_1**: Mimics attacker equipment
- **info_2**: Uses weapons/shields learned

### Equipment & Gear
- Mimics any equipment from attacker

### Combat
- **info_0**: Mimics attacker's equipment
- **info_1**: Uses shields, swords, bows, crossbows, tridents
- **info_2**: Difficult if powerful equipment used


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Mimicube entity is summonable (`/summon xeenaa-alexs-mobs:mimicube`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Mimic mechanics, outer overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MimicubeEntity class
- MimicubeModel class (GeckoLib model)
- MimicubeRenderer class (GeckoLib renderer)
- Geometry file (mimicube.geo.json)
- Animation file (mimicube.animation.json)
- Texture file(s) (mimicube.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Mimic mechanics, outer overlay

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
2. **Special Features**: Mimic mechanics, outer overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Mimicube cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MimicubeEntity.java`
- `MimicubeModel.java`
- `MimicubeRenderer.java`

### Resource Files
- `mimicube.geo.json`
- `mimicube.animation.json`
- `mimicube.png` (+ variants)
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
- [ ] AI behaves according to Hostile type
- [ ] Special features work: Mimic mechanics, outer overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Mimic mechanics, outer overlay
- Biomes: The End

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 84

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
