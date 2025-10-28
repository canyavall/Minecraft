# Epic 56: Laviathan - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Laviathan** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Lava Ocean (Nether)
**Key Features**: Huge lava creature, obsidian variant, gear/helmet

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter laviathan in lava ocean (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Huge lava creature, obsidian variant, gear/helmet

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Lava Ocean (Nether)

---

## Features & Functionality

### Core Features
- Gigantic magmatic reptiles
- Covered in Blackstone
- Completely passive

### Special Mechanics
- Rideable: Requires Straddlite Saddle + Straddlite Tack
- 4 passengers on saddle
- Tack for control with long reins
- Equipment reduces fleeing/submerging
- Predator of Crimson Mosquitoes: Breaches surface, emits smoke, lures insects
- Water exposure: "Solidifies" into stronger obsidian form
- Less likely to flee when equipped

### Items & Interactions
- **Eats**: Crimson Mosquito Larva (breeding), Magma Cream (healing)

### AI Behaviors
- **info_0**: Passive
- **info_1**: Flees if injured
- **info_2**: Submerges in lava
- **info_3**: Breaches to lure mosquitoes
- **info_4**: Emits smoke

### Equipment & Gear
- Straddlite Saddle (4 passengers)
- Straddlite Tack (control)

### Breeding
- **Item**: Crimson Mosquito Larva

### Variants
- Normal (lava)
- Obsidian form (water-exposed, stronger)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Laviathan entity is summonable (`/summon xeenaa-alexs-mobs:laviathan`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Huge lava creature, obsidian variant, gear/helmet
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- LaviathanEntity class
- LaviathanModel class (GeckoLib model)
- LaviathanRenderer class (GeckoLib renderer)
- Geometry file (laviathan.geo.json)
- Animation file (laviathan.animation.json)
- Texture file(s) (laviathan.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Huge lava creature, obsidian variant, gear/helmet

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
2. **Special Features**: Huge lava creature, obsidian variant, gear/helmet
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Laviathan cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `LaviathanEntity.java`
- `LaviathanModel.java`
- `LaviathanRenderer.java`

### Resource Files
- `laviathan.geo.json`
- `laviathan.animation.json`
- `laviathan.png` (+ variants)
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
- [ ] AI behaves according to Passive type
- [ ] Special features work: Huge lava creature, obsidian variant, gear/helmet
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Complex
- Special features: Huge lava creature, obsidian variant, gear/helmet
- Biomes: Lava Ocean (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 78

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
