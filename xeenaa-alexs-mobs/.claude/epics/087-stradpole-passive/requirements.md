# Epic 87: Stradpole - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Stradpole** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Basalt Deltas (Nether)
**Key Features**: Baby form of Straddler

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter stradpole in basalt deltas (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Baby form of Straddler

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Basalt Deltas (Nether)

---

## Features & Functionality

### Core Features
- Immature Straddler larvae
- Launched as projectiles
- Found in lava pools

### Special Mechanics
- Can be bucketed (lava bucket)
- Feeding Crimson Mosquito Larva: Chance to grow into hostile adult Straddler
- Equally at home in water and lava
- Exotic aquarium addition

### Items & Interactions
- **Eats**: Crimson Mosquito Larva (growth)

### AI Behaviors
- **info_0**: Lives in lava/water
- **info_1**: Launched by parents
- **info_2**: Can grow to adult

### Combat
- **info_0**: Used as projectile by adults


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Stradpole entity is summonable (`/summon xeenaa-alexs-mobs:stradpole`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Baby form of Straddler
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- StradpoleEntity class
- StradpoleModel class (GeckoLib model)
- StradpoleRenderer class (GeckoLib renderer)
- Geometry file (stradpole.geo.json)
- Animation file (stradpole.animation.json)
- Texture file(s) (stradpole.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Baby form of Straddler

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
2. **Special Features**: Baby form of Straddler
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Stradpole cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `StradpoleEntity.java`
- `StradpoleModel.java`
- `StradpoleRenderer.java`

### Resource Files
- `stradpole.geo.json`
- `stradpole.animation.json`
- `stradpole.png` (+ variants)
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
- [ ] Special features work: Baby form of Straddler
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Baby form of Straddler
- Biomes: Basalt Deltas (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 109

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
