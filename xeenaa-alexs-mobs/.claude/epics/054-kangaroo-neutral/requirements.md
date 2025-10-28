# Epic 54: Kangaroo - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Kangaroo** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Badlands, Savanna
**Key Features**: Unique movement, pouch mechanic

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter kangaroo in badlands, savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Unique movement, pouch mechanic

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Badlands, Savanna

---

## Features & Functionality

### Core Features
- Large marsupials
- Hop-only locomotion
- Sometimes with joey in pouch
- Strict herbivores

### Taming
- **Method**: Feed 10-15 Carrots
- **Benefits**: Stay/follow/wander, pouch inventory access, equipment use, self-healing

### Special Mechanics
- Pouch inventory: Access by sneaking
- Melee weapon: Kangaroo uses weapon in pouch when attacking
- Equipment: Can equip helmet/chestplate from pouch
- Self-healing: Eats non-meat food in pouch when injured
- Kangaroo Burger: Cooked meat creates burger

### Items & Interactions
- **Drops**: Kangaroo Meat, Kangaroo Hide
- **Eats**: Carrots (taming), Grass, non-meat food (healing)
- **Crafted**: Kangaroo Burger, Leather, Banner Pattern

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Hop locomotion
- **info_2**: Feeds on grass
- **info_3**: Uses pouch weapons
- **info_4**: Self-heals with pouch food

### Equipment & Gear
- Pouch inventory
- Can equip helmet/chestplate
- Melee weapon from pouch

### Breeding
- **Item**: Dead Bush or Grass

### Combat
- **info_0**: Punch attacks
- **info_1**: Kick with both feet (devastating)
- **info_2**: Uses melee weapon from pouch


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Kangaroo entity is summonable (`/summon xeenaa-alexs-mobs:kangaroo`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Unique movement, pouch mechanic
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- KangarooEntity class
- KangarooModel class (GeckoLib model)
- KangarooRenderer class (GeckoLib renderer)
- Geometry file (kangaroo.geo.json)
- Animation file (kangaroo.animation.json)
- Texture file(s) (kangaroo.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Unique movement, pouch mechanic

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
2. **Special Features**: Unique movement, pouch mechanic
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Kangaroo cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `KangarooEntity.java`
- `KangarooModel.java`
- `KangarooRenderer.java`

### Resource Files
- `kangaroo.geo.json`
- `kangaroo.animation.json`
- `kangaroo.png` (+ variants)
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
- [ ] AI behaves according to Neutral type
- [ ] Special features work: Unique movement, pouch mechanic
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Unique movement, pouch mechanic
- Biomes: Badlands, Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 76

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
