# Epic 79: Shoebill - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Shoebill** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Swamp
**Key Features**: Static bird

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter shoebill in swamp
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Static bird

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Swamp

---

## Features & Functionality

### Core Features
- Strange stork-like birds
- Intimidating appearance but timid
- Large strange beaks

### Special Mechanics
- Fishing behavior: Catches favorite food, produces bycatch
- Crocodile Egg feeding: Increases "lure" effect
- Terrapin Egg feeding: Increases "luck" effect
- Attacks baby Crocodiles and Turtles
- Takes wing when attacked

### Items & Interactions
- **Eats**: Crocodile Eggs (lure), Terrapin Eggs (luck)
- **Catches**: Fish (with enhanced drops)

### AI Behaviors
- **info_0**: Timid (flees when attacked)
- **info_1**: Fishing behavior
- **info_2**: Shakes beak
- **info_3**: Hunts baby aquatic creatures

### Combat
- **info_0**: Attacks baby Crocodiles/Turtles only
- **info_1**: Flees from threats


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Shoebill entity is summonable (`/summon xeenaa-alexs-mobs:shoebill`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Static bird
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- ShoebillEntity class
- ShoebillModel class (GeckoLib model)
- ShoebillRenderer class (GeckoLib renderer)
- Geometry file (shoebill.geo.json)
- Animation file (shoebill.animation.json)
- Texture file(s) (shoebill.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Static bird

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
2. **Special Features**: Static bird
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Shoebill cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `ShoebillEntity.java`
- `ShoebillModel.java`
- `ShoebillRenderer.java`

### Resource Files
- `shoebill.geo.json`
- `shoebill.animation.json`
- `shoebill.png` (+ variants)
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
- [ ] Special features work: Static bird
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Static bird
- Biomes: Swamp

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 101

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
