# Epic 47: Giant Squid - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Giant Squid** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Deep Ocean
**Key Features**: Huge size, 2 color variants, depressurized

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter giant squid in deep ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Huge size, 2 color variants, depressurized

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Deep Ocean

---

## Features & Functionality

### Core Features
- Large invertebrates
- Largest eye in animal kingdom
- Prey to Cachalot Whales

### Special Mechanics
- Lightning transformation: Normal squid struck by lightning → unique blue Giant Squid
- Lost Tentacles: Dropped during battle with Cachalot Whale
- Grappling Squok crafting: Shoots 4 tentacles, latches surfaces, pulls player
- Depressurized: Pale under 10 blocks water

### Items & Interactions
- **Drops**: Ink Sacs, Lost Tentacles (vs Cachalot)
- **Crafted**: Grappling Squok (4-tentacle grappling hook)

### AI Behaviors
- **info_0**: Neutral to most
- **info_1**: Hostile to guardians and fish
- **info_2**: Flees from Cachalot Whales

### Variants
- Normal
- Blue (lightning-struck)
- Depressurized (pale)

### Combat
- **info_0**: Attacks guardians/fish
- **info_1**: Loses tentacles vs Cachalot


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Giant Squid entity is summonable (`/summon xeenaa-alexs-mobs:giant-squid`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Huge size, 2 color variants, depressurized
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- GiantSquidEntity class
- GiantSquidModel class (GeckoLib model)
- GiantSquidRenderer class (GeckoLib renderer)
- Geometry file (giant-squid.geo.json)
- Animation file (giant-squid.animation.json)
- Texture file(s) (giant-squid.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Huge size, 2 color variants, depressurized

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
2. **Special Features**: Huge size, 2 color variants, depressurized
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Giant Squid cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `GiantSquidEntity.java`
- `GiantSquidModel.java`
- `GiantSquidRenderer.java`

### Resource Files
- `giant-squid.geo.json`
- `giant-squid.animation.json`
- `giant-squid.png` (+ variants)
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
- [ ] Special features work: Huge size, 2 color variants, depressurized
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Huge size, 2 color variants, depressurized
- Biomes: Deep Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 69

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
