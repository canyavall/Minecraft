# Epic 20: Bunfungus - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Bunfungus** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Mushroom Fields
**Key Features**: Sleeping, transforming states

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter bunfungus in mushroom fields
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Sleeping, transforming states

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Mushroom Fields

---

## Features & Functionality

### Core Features
- Gigantic mutated rabbits
- Natural defense of mushroom biome
- Created (not bred)

### Special Mechanics
- Creation: Feed rabbit several Mungal Spores
- Carrot feeding: Becomes completely passive + grants Regeneration & Strength
- Sleeps during day, hunts monsters at night
- Attacks all monsters on sight

### Items & Interactions
- **Eats**: Carrots (pacification + buffs)
- **Created with**: Mungal Spores

### AI Behaviors
- **info_0**: Attacks monsters
- **info_1**: Neutral to players (unless carrot fed)
- **info_2**: Sleeps during day
- **info_3**: Active at night
- **info_4**: Hops quickly

### Combat
- **info_0**: Large area damage
- **info_1**: Great damage output
- **info_2**: Fast hopping


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Bunfungus entity is summonable (`/summon xeenaa-alexs-mobs:bunfungus`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Sleeping, transforming states
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BunfungusEntity class
- BunfungusModel class (GeckoLib model)
- BunfungusRenderer class (GeckoLib renderer)
- Geometry file (bunfungus.geo.json)
- Animation file (bunfungus.animation.json)
- Texture file(s) (bunfungus.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Sleeping, transforming states

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
2. **Special Features**: Sleeping, transforming states
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Bunfungus cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BunfungusEntity.java`
- `BunfungusModel.java`
- `BunfungusRenderer.java`

### Resource Files
- `bunfungus.geo.json`
- `bunfungus.animation.json`
- `bunfungus.png` (+ variants)
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
- [ ] Special features work: Sleeping, transforming states
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Sleeping, transforming states
- Biomes: Mushroom Fields

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 42

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
