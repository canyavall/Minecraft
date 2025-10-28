# Epic 68: Platypus - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Platypus** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: River, Swamp
**Key Features**: Aquatic, fedora/perry variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter platypus in river, swamp
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Aquatic, fedora/perry variants

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: River, Swamp

---

## Features & Functionality

### Core Features
- Unique mammal (combination of animals)
- Poisonous spines on feet
- Electric sensitivity (weak smell/hearing/sight)

### Special Mechanics
- Redstone attraction: Naturally attracted when held
- Redstone feeding: Recharges electric sensitivity, digs up items from clay
- Redstone block feeding: Greatly increases items dug up
- Digs up: Clay balls, maggots
- Can be bucketed

### Items & Interactions
- **Eats**: Redstone (charging), Redstone Block (supercharge), Lobster Tails (breeding)
- **Finds**: Clay balls, maggots

### AI Behaviors
- **info_0**: Attracted to redstone
- **info_1**: Digs in clay blocks
- **info_2**: Hunts with electricity

### Breeding
- **Item**: Lobster Tails

### Combat
- **info_0**: Poisonous spines (defense)
- **info_1**: Inflicts poison on attackers


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Platypus entity is summonable (`/summon xeenaa-alexs-mobs:platypus`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Aquatic, fedora/perry variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- PlatypusEntity class
- PlatypusModel class (GeckoLib model)
- PlatypusRenderer class (GeckoLib renderer)
- Geometry file (platypus.geo.json)
- Animation file (platypus.animation.json)
- Texture file(s) (platypus.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Aquatic, fedora/perry variants

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
2. **Special Features**: Aquatic, fedora/perry variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Platypus cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `PlatypusEntity.java`
- `PlatypusModel.java`
- `PlatypusRenderer.java`

### Resource Files
- `platypus.geo.json`
- `platypus.animation.json`
- `platypus.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Aquatic, fedora/perry variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Simple
- Special features: Aquatic, fedora/perry variants
- Biomes: River, Swamp

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 90

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
