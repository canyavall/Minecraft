# Epic 81: Skreecher - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Skreecher** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Deep Dark
**Key Features**: Sculk-related, glow overlay

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter skreecher in deep dark
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Sculk-related, glow overlay

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Deep Dark

---

## Features & Functionality

### Core Features
- Hideously-mutated primate
- Clings to ceiling
- Gigantic glowing eyes
- Cannot fight directly

### Special Mechanics
- Sound attack: Howls and claps feet, awakens Warden beneath target
- Extremely limited health
- Projectile disables temporarily and makes it fall
- Hard to identify before it acts

### Items & Interactions
- **Drops**: Skreecher Soul
- **Crafted**: Sculk Boomer (activated by sculk sensor, damages mobs within 3 blocks, redstone disables, cooldown)

### AI Behaviors
- **info_0**: Clings to ceiling
- **info_1**: Creates loud racket
- **info_2**: Summons Warden
- **info_3**: Falls when hit by projectile

### Combat
- **info_0**: No direct combat
- **info_1**: Summons Warden
- **info_2**: Howling and clapping


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Skreecher entity is summonable (`/summon xeenaa-alexs-mobs:skreecher`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Sculk-related, glow overlay
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SkreecherEntity class
- SkreecherModel class (GeckoLib model)
- SkreecherRenderer class (GeckoLib renderer)
- Geometry file (skreecher.geo.json)
- Animation file (skreecher.animation.json)
- Texture file(s) (skreecher.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Sculk-related, glow overlay

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
2. **Special Features**: Sculk-related, glow overlay
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Skreecher cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SkreecherEntity.java`
- `SkreecherModel.java`
- `SkreecherRenderer.java`

### Resource Files
- `skreecher.geo.json`
- `skreecher.animation.json`
- `skreecher.png` (+ variants)
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
- [ ] AI behaves according to Hostile type
- [ ] Special features work: Sculk-related, glow overlay
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Sculk-related, glow overlay
- Biomes: Deep Dark

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 103

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
