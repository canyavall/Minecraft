# Epic 36: Emu - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Emu** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Badlands, Savanna
**Key Features**: Baby variants, 3 color types

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter emu in badlands, savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Baby variants, 3 color types

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
- Large flightless birds
- Dodge projectiles
- Great speed and numbers
- Historic war legend

### Special Mechanics
- Projectile dodging: Uncanny ability to dodge
- Attack skeletons (dislikes bows)
- Eggs: Large blue eggs, can be cooked
- Emu Feathers: Craft leggings allowing wearer to dodge projectiles
- Banner pattern from feathers

### Items & Interactions
- **Drops**: Emu Feathers, Emu Eggs
- **Crafted**: Emu Leggings (dodge projectiles), Banner Pattern

### AI Behaviors
- **info_0**: Flees when attacked initially
- **info_1**: Some return to attack
- **info_2**: Large claw attacks
- **info_3**: Dodges projectiles
- **info_4**: Attacks skeletons

### Breeding
- **Item**: Wheat
- **info_1**: Color patterns through breeding

### Variants
- Few color patterns

### Combat
- **info_0**: Rip and tear with large claws
- **info_1**: Dodges projectiles


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Emu entity is summonable (`/summon xeenaa-alexs-mobs:emu`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Baby variants, 3 color types
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- EmuEntity class
- EmuModel class (GeckoLib model)
- EmuRenderer class (GeckoLib renderer)
- Geometry file (emu.geo.json)
- Animation file (emu.animation.json)
- Texture file(s) (emu.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Baby variants, 3 color types

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
2. **Special Features**: Baby variants, 3 color types
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Emu cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `EmuEntity.java`
- `EmuModel.java`
- `EmuRenderer.java`

### Resource Files
- `emu.geo.json`
- `emu.animation.json`
- `emu.png` (+ variants)
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
- [ ] Special features work: Baby variants, 3 color types
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Baby variants, 3 color types
- Biomes: Badlands, Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 58

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
