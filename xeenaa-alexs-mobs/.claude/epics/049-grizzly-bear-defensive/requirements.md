# Epic 49: Grizzly Bear - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Grizzly Bear** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: Forest, Taiga
**Key Features**: Tameable, rideable, honey interaction, cubs

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter grizzly bear in forest, taiga
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Tameable, rideable, honey interaction, cubs

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: Forest, Taiga

---

## Features & Functionality

### Core Features
- Large omnivorous mammals
- Often with 1-2 cubs
- Raid beehives for honey

### Taming
- **Method**: Feed salmon after eating honey
- **Benefits**: War mount

### Special Mechanics
- Honey sating: Can be approached safely after eating honey
- Taming sequence: Feed salmon after honey sating
- War mount: Can be ridden in combat
- Raids beehives
- Hair shedding: Occasional
- Grizzly Bear Hair + Strength Potion = Knockback Resistance Potion
- Snow coating: In snowy weather, remove with shovel or place snow layer

### Items & Interactions
- **Drops**: Grizzly Bear Hair (shed)
- **Eats**: Honey (from beehives), Salmon (taming)
- **Crafted**: Knockback Resistance Potion

### AI Behaviors
- **info_0**: Neutral at distance
- **info_1**: Hostile when approached
- **info_2**: Raids beehives
- **info_3**: Protective of cubs
- **info_4**: Sated after eating honey

### Combat
- **info_0**: Standard attacks
- **info_1**: War mount capability


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Grizzly Bear entity is summonable (`/summon xeenaa-alexs-mobs:grizzly-bear`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Tameable, rideable, honey interaction, cubs
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- GrizzlyBearEntity class
- GrizzlyBearModel class (GeckoLib model)
- GrizzlyBearRenderer class (GeckoLib renderer)
- Geometry file (grizzly-bear.geo.json)
- Animation file (grizzly-bear.animation.json)
- Texture file(s) (grizzly-bear.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Tameable, rideable, honey interaction, cubs

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
2. **Special Features**: Tameable, rideable, honey interaction, cubs
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Grizzly Bear cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `GrizzlyBearEntity.java`
- `GrizzlyBearModel.java`
- `GrizzlyBearRenderer.java`

### Resource Files
- `grizzly-bear.geo.json`
- `grizzly-bear.animation.json`
- `grizzly-bear.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: Tameable, rideable, honey interaction, cubs
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Complex
- Special features: Tameable, rideable, honey interaction, cubs
- Biomes: Forest, Taiga

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 71

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
