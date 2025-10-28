# Epic 11: Alligator Snapping Turtle - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Alligator Snapping Turtle** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: Swamp
**Key Features**: Aquatic, bite attack when provoked

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter alligator snapping turtle in swamp
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Aquatic, bite attack when provoked

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: Swamp

---

## Features & Functionality

### Core Features
- Massive semi-aquatic reptile
- Sharp beaked mouth
- Surprisingly fast
- Moss growth builds up on shell underwater

### Special Mechanics
- Shearable when mossy: Chance to drop Spiked Scute
- Spiked Turtle Shell helmet: Knockback resistance, 15s extra underwater breathing, knocks back melee attacks
- Rarely moves

### Items & Interactions
- **Drops**: Spiked Scute (when sheared if mossy)
- **Crafted**: Spiked Turtle Shell helmet upgrade

### AI Behaviors
- **info_0**: Attacks anything near mouth
- **info_1**: Fast bite attacks
- **info_2**: Rarely moves
- **info_3**: Moss growth over days

### Breeding
- **Item**: Raw Cod

### Combat
- **info_0**: Bite attack
- **info_1**: Fast movement for turtle


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Alligator Snapping Turtle entity is summonable (`/summon xeenaa-alexs-mobs:alligator-snapping-turtle`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Aquatic, bite attack when provoked
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- AlligatorSnappingTurtleEntity class
- AlligatorSnappingTurtleModel class (GeckoLib model)
- AlligatorSnappingTurtleRenderer class (GeckoLib renderer)
- Geometry file (alligator-snapping-turtle.geo.json)
- Animation file (alligator-snapping-turtle.animation.json)
- Texture file(s) (alligator-snapping-turtle.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Aquatic, bite attack when provoked

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
2. **Special Features**: Aquatic, bite attack when provoked
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Alligator Snapping Turtle cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `AlligatorSnappingTurtleEntity.java`
- `AlligatorSnappingTurtleModel.java`
- `AlligatorSnappingTurtleRenderer.java`

### Resource Files
- `alligator-snapping-turtle.geo.json`
- `alligator-snapping-turtle.animation.json`
- `alligator-snapping-turtle.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Aquatic, bite attack when provoked
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Medium
- Special features: Aquatic, bite attack when provoked
- Biomes: Swamp

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 33

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
