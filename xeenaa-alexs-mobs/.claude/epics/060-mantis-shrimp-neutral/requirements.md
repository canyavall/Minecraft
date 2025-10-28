# Epic 60: Mantis Shrimp - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Mantis Shrimp** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Warm Ocean
**Key Features**: 4 random variants, powerful punch

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter mantis shrimp in warm ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 4 random variants, powerful punch

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Warm Ocean

---

## Features & Functionality

### Core Features
- Giant crustaceans
- Powerful fists
- Fast enough to burn prey
- Break through blocks

### Taming
- **Method**: Feed 10-30 Tropical Fish
- **Benefits**: Follow/sit/wander, block breaking, land survival

### Special Mechanics
- Combat: Fists launch enemies back, break blocks
- Water bucket feeding: Prevents drying (can live on land indefinitely)
- Block breaking: Give block while sneaking, shrimp seeks and breaks matching blocks
- Predator of: Tropical fish, squid, guardians, shulkers (favorite)
- Dries out after 5 minutes on land (untamed)

### Items & Interactions
- **Eats**: Tropical Fish (taming), Fish (healing), Lobster Tails (breeding)

### AI Behaviors
- **info_0**: Follow/sit/wander/break blocks modes
- **info_1**: Hunts specific prey
- **info_2**: Block seeking and breaking

### Equipment & Gear
- Holds block in fist (for breaking matching blocks)

### Breeding
- **Item**: Lobster Tails

### Combat
- **info_0**: Fast burning punches
- **info_1**: Launch enemies
- **info_2**: Break blocks


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Mantis Shrimp entity is summonable (`/summon xeenaa-alexs-mobs:mantis-shrimp`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 4 random variants, powerful punch
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MantisShrimpEntity class
- MantisShrimpModel class (GeckoLib model)
- MantisShrimpRenderer class (GeckoLib renderer)
- Geometry file (mantis-shrimp.geo.json)
- Animation file (mantis-shrimp.animation.json)
- Texture file(s) (mantis-shrimp.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 4 random variants, powerful punch

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
2. **Special Features**: 4 random variants, powerful punch
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Mantis Shrimp cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MantisShrimpEntity.java`
- `MantisShrimpModel.java`
- `MantisShrimpRenderer.java`

### Resource Files
- `mantis-shrimp.geo.json`
- `mantis-shrimp.animation.json`
- `mantis-shrimp.png` (+ variants)
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
- [ ] Special features work: 4 random variants, powerful punch
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: 4 random variants, powerful punch
- Biomes: Warm Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 82

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
