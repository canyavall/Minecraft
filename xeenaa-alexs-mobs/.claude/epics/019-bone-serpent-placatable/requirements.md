# Epic 19: Bone Serpent - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Bone Serpent** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Lava Ocean (Nether)
**Key Features**: Multi-part entity (head/mid/tail)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter bone serpent in lava ocean (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Multi-part entity (head/mid/tail)

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Lava Ocean (Nether)

---

## Features & Functionality

### Core Features
- Giant snake-like undead
- Dwells in deep lava
- Jumps in and out of lava

### Special Mechanics
- Mortal enemy of Wither Skeletons
- Crush attack: Leaps from lava, crushes prey beneath
- Explodes into bone piles on death
- Potion of Lava Vision: Bone Serpent Tooth + lava bottle = see through lava

### Items & Interactions
- **Drops**: Bones, Bone Serpent Tooth
- **Crafted**: Potion of Lava Vision (see through lava like water)

### AI Behaviors
- **info_0**: Hostile to explorers and Wither Skeletons
- **info_1**: Jumps from lava
- **info_2**: Crush attacks
- **info_3**: Disappears into lava

### Combat
- **info_0**: Leap and crush attack
- **info_1**: Hostile to Wither Skeletons


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Bone Serpent entity is summonable (`/summon xeenaa-alexs-mobs:bone-serpent`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Multi-part entity (head/mid/tail)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BoneSerpentEntity class
- BoneSerpentModel class (GeckoLib model)
- BoneSerpentRenderer class (GeckoLib renderer)
- Geometry file (bone-serpent.geo.json)
- Animation file (bone-serpent.animation.json)
- Texture file(s) (bone-serpent.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Multi-part entity (head/mid/tail)

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
1. **Complexity**: Very Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Multi-part entity (head/mid/tail)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Bone Serpent cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BoneSerpentEntity.java`
- `BoneSerpentModel.java`
- `BoneSerpentRenderer.java`

### Resource Files
- `bone-serpent.geo.json`
- `bone-serpent.animation.json`
- `bone-serpent.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 7-14 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Placatable type
- [ ] Special features work: Multi-part entity (head/mid/tail)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Very Complex
- Special features: Multi-part entity (head/mid/tail)
- Biomes: Lava Ocean (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 41

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
