# Epic 85: Spectre - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Spectre** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: The End
**Key Features**: Spectral entity, bone/glint/glow overlays

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter spectre in the end
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Spectral entity, bone/glint/glow overlays

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: The End

---

## Features & Functionality

### Core Features
- Distant relative of Phantom
- Completely passive
- Spectral (cannot be attacked normally)

### Special Mechanics
- Immune to normal attacks (only magical damage)
- Soul Heart interest: Follows player holding Soul Heart
- Lead attachment: Use lead to attach, Spectre pulls player along
- Void navigation: Quick way to explore End, find new islands
- Detach: Sneak or interact with lead

### AI Behaviors
- **info_0**: Floats in void
- **info_1**: Follows Soul Heart holders
- **info_2**: Pulls leashed players
- **info_3**: Passive

### Equipment & Gear
- Lead (for pulling)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Spectre entity is summonable (`/summon xeenaa-alexs-mobs:spectre`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Spectral entity, bone/glint/glow overlays
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SpectreEntity class
- SpectreModel class (GeckoLib model)
- SpectreRenderer class (GeckoLib renderer)
- Geometry file (spectre.geo.json)
- Animation file (spectre.animation.json)
- Texture file(s) (spectre.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Spectral entity, bone/glint/glow overlays

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
2. **Special Features**: Spectral entity, bone/glint/glow overlays
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Spectre cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SpectreEntity.java`
- `SpectreModel.java`
- `SpectreRenderer.java`

### Resource Files
- `spectre.geo.json`
- `spectre.animation.json`
- `spectre.png` (+ variants)
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
- [ ] AI behaves according to Passive type
- [ ] Special features work: Spectral entity, bone/glint/glow overlays
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Medium
- Special features: Spectral entity, bone/glint/glow overlays
- Biomes: The End

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 107

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
