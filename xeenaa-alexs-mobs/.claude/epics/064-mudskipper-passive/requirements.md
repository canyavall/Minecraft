# Epic 64: Mudskipper - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Mudskipper** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Mangrove Swamp
**Key Features**: Amphibious, spit projectile

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter mudskipper in mangrove swamp
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Amphibious, spit projectile

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Mangrove Swamp

---

## Features & Functionality

### Core Features
- Fish that walk on land
- Equally at home on land and water

### Taming
- **Method**: Feed several Lobster Tails
- **Benefits**: Stay/follow/wander, combat support

### Special Mechanics
- Dominance displays: Raise fins and circle each other
- Combat: Spits mud balls inflicting slowness and damage
- Strafing: Can strafe in combat

### Items & Interactions
- **Eats**: Lobster Tails (taming, breeding), Insect larvae (breeding)

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Dominance displays
- **info_2**: Defends owner
- **info_3**: Strafes in combat

### Breeding
- **Item**: Insect larvae or Lobster Tails

### Combat
- **info_0**: Mud ball projectiles (slowness + damage)
- **info_1**: Strafing movement


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Mudskipper entity is summonable (`/summon xeenaa-alexs-mobs:mudskipper`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Amphibious, spit projectile
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MudskipperEntity class
- MudskipperModel class (GeckoLib model)
- MudskipperRenderer class (GeckoLib renderer)
- Geometry file (mudskipper.geo.json)
- Animation file (mudskipper.animation.json)
- Texture file(s) (mudskipper.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Amphibious, spit projectile

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
2. **Special Features**: Amphibious, spit projectile
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Mudskipper cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MudskipperEntity.java`
- `MudskipperModel.java`
- `MudskipperRenderer.java`

### Resource Files
- `mudskipper.geo.json`
- `mudskipper.animation.json`
- `mudskipper.png` (+ variants)
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
- [ ] Special features work: Amphibious, spit projectile
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Amphibious, spit projectile
- Biomes: Mangrove Swamp

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 86

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
