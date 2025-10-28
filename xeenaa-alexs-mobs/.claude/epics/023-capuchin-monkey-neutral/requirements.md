# Epic 23: Capuchin Monkey - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Capuchin Monkey** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Jungle
**Key Features**: Item interactions, 4 random variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter capuchin monkey in jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Item interactions, 4 random variants

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle

---

## Features & Functionality

### Core Features
- Small primates
- Multiple colors by group
- Fondness for bananas

### Taming
- **Method**: Feed Bananas
- **Benefits**: Stay/follow/wander, shoulder riding, ranged + melee attacks

### Special Mechanics
- Shoulder riding: Can attack from shoulder, throws stones
- Combat: Uses ranged and melee attacks
- Ancient Dart: Upgrades ranged attack with piercing (found in Jungle Temple chests)
- Banana Peel drops: Can craft "Sopa De Macaco" ("Uma Delicia")

### Items & Interactions
- **Eats**: Bananas (taming), Eggs (healing), Maggots (breeding)
- **Drops**: Banana Peels (occasional)
- **Equipment**: Ancient Dart (ranged upgrade)
- **Crafted**: Sopa De Macaco

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Shoulder riding
- **info_2**: Ranged (stone throwing) + melee attacks
- **info_3**: Attacks from shoulder

### Equipment & Gear
- Ancient Dart (ranged weapon upgrade)

### Breeding
- **Item**: Maggots

### Variants
- Many colors depending on group

### Combat
- **info_0**: Stone throwing (ranged)
- **info_1**: Melee attacks
- **info_2**: Piercing darts (with Ancient Dart)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Capuchin Monkey entity is summonable (`/summon xeenaa-alexs-mobs:capuchin-monkey`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Item interactions, 4 random variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CapuchinMonkeyEntity class
- CapuchinMonkeyModel class (GeckoLib model)
- CapuchinMonkeyRenderer class (GeckoLib renderer)
- Geometry file (capuchin-monkey.geo.json)
- Animation file (capuchin-monkey.animation.json)
- Texture file(s) (capuchin-monkey.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Item interactions, 4 random variants

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
2. **Special Features**: Item interactions, 4 random variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Capuchin Monkey cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CapuchinMonkeyEntity.java`
- `CapuchinMonkeyModel.java`
- `CapuchinMonkeyRenderer.java`

### Resource Files
- `capuchin-monkey.geo.json`
- `capuchin-monkey.animation.json`
- `capuchin-monkey.png` (+ variants)
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
- [ ] Special features work: Item interactions, 4 random variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Complex
- Special features: Item interactions, 4 random variants
- Biomes: Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 45

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
