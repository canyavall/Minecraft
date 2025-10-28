# Epic 48: Gorilla - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Gorilla** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Jungle, Bamboo Jungle
**Key Features**: Silverback leader, special variants (DK/Funky)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter gorilla in jungle, bamboo jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Silverback leader, special variants (DK/Funky)

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle, Bamboo Jungle

---

## Features & Functionality

### Core Features
- Massive apes
- Peaceful protectors of jungle
- Group hierarchy with Silverback leader

### Special Mechanics
- Banana feeding: Become trustful and friendly
- Silverback: Leader, 2-3x stronger, pounds chest when anxious
- Baby gorillas ride on parent backs
- Commands: Sit/wander (after enough bananas)
- Defend befriended players

### Items & Interactions
- **Eats**: Bananas (befriending)
- **Bananas from**: Breaking jungle leaves

### AI Behaviors
- **info_0**: Sit/wander modes (befriended)
- **info_1**: Defends friends
- **info_2**: Foraging
- **info_3**: Baby riding behavior
- **info_4**: Chest pounding (Silverback)

### Variants
- Silverback (leader)
- Regular

### Combat
- **info_0**: Massive damage
- **info_1**: Silverback 2-3x stronger


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Gorilla entity is summonable (`/summon xeenaa-alexs-mobs:gorilla`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Silverback leader, special variants (DK/Funky)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- GorillaEntity class
- GorillaModel class (GeckoLib model)
- GorillaRenderer class (GeckoLib renderer)
- Geometry file (gorilla.geo.json)
- Animation file (gorilla.animation.json)
- Texture file(s) (gorilla.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Silverback leader, special variants (DK/Funky)

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
2. **Special Features**: Silverback leader, special variants (DK/Funky)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Gorilla cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `GorillaEntity.java`
- `GorillaModel.java`
- `GorillaRenderer.java`

### Resource Files
- `gorilla.geo.json`
- `gorilla.animation.json`
- `gorilla.png` (+ variants)
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
- [ ] Special features work: Silverback leader, special variants (DK/Funky)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Silverback leader, special variants (DK/Funky)
- Biomes: Jungle, Bamboo Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 70

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
