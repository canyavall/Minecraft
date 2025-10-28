# Epic 35: Elephant - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Elephant** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Savanna
**Key Features**: Rideable, inventory system, 17 decor variants

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter elephant in savanna
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rideable, inventory system, 17 decor variants

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Savanna

---

## Features & Functionality

### Core Features
- Giant land mammals
- Three types: Calves, Tusked, Non-tusked
- Tusked elephants significantly stronger
- Defensive of herds

### Taming
- **Method**: Feed Acacia Blossoms (babies and non-tusked only)
- **info_1**: Cannot tame wild tusked elephants
- **Tamed tusked**: Must tame as calf
- **Benefits**: Rideable, large inventory, charge attack

### Special Mechanics
- Rideable by interaction
- Chest: Adds large inventory to back (access by sneaking)
- Carpet decoration: Any carpet, remove with shears
- Charge attack: Feed wheat while riding (tusked only), requires rest period
- Tear leaves for Acacia Blossoms (rare drop)
- Wandering Traders: Can spawn on trader elephants with chest loot

### Items & Interactions
- **Eats**: Acacia Blossoms (taming, breeding)
- **Drops**: Acacia Blossoms (from trees, rare)

### AI Behaviors
- **info_0**: Herd behavior
- **info_1**: Defensive of herd
- **info_2**: Trunk toss attacks
- **info_3**: Rear up and stomp
- **info_4**: Tear leaves from trees
- **info_5**: Charge attack (tusked)

### Equipment & Gear
- Chest (inventory)
- Carpet (decoration)

### Breeding
- **Item**: Acacia Blossoms

### Variants
- Calves, Tusked, Non-tusked
- Trader Elephants (with Wandering Traders)

### Combat
- **info_0**: Trunk toss
- **info_1**: Rear and stomp
- **info_2**: Charge attack (tusked, massive damage)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Elephant entity is summonable (`/summon xeenaa-alexs-mobs:elephant`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rideable, inventory system, 17 decor variants
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- ElephantEntity class
- ElephantModel class (GeckoLib model)
- ElephantRenderer class (GeckoLib renderer)
- Geometry file (elephant.geo.json)
- Animation file (elephant.animation.json)
- Texture file(s) (elephant.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rideable, inventory system, 17 decor variants

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
2. **Special Features**: Rideable, inventory system, 17 decor variants
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Elephant cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `ElephantEntity.java`
- `ElephantModel.java`
- `ElephantRenderer.java`

### Resource Files
- `elephant.geo.json`
- `elephant.animation.json`
- `elephant.png` (+ variants)
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
- [ ] AI behaves according to Neutral type
- [ ] Special features work: Rideable, inventory system, 17 decor variants
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Very Complex
- Special features: Rideable, inventory system, 17 decor variants
- Biomes: Savanna

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 57

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
