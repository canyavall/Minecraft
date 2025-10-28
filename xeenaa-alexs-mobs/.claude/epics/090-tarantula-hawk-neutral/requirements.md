# Epic 90: Tarantula Hawk - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Tarantula Hawk** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Desert, Badlands
**Key Features**: Nether variant, baby, angry states

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter tarantula hawk in desert, badlands
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Nether variant, baby, angry states

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert, Badlands

---

## Features & Functionality

### Core Features
- Giant parasitoid wasps (not spider or bird)
- Favorite prey: Spiders

### Taming
- **Method**: Feed 15-25 Spider Eyes
- **Benefits**: Formidable defense, anti-arthropod combat

### Special Mechanics
- Debilitating Sting: Paralyzes arthropods, damages non-arthropods to half health
- Hunting: Stings spider, returns to drag and bury underground
- Erratic flight pattern
- Grubs: After breeding, seek spiders to paralyze, baby grub emerges from buried spider
- Nether breeding: Yellow-orange color pattern

### Items & Interactions
- **Eats**: Spider Eyes (taming), Fermented Spider Eyes (breeding), Flowers (healing)
- **Drops**: Tattered Tarantula Hawk Wing
- **Crafted**: Tarantula Hawk Wing (9 tattered), Tarantula Hawk Elytra (upgrade with +3 armor, more durability, enchantable), Banner Pattern

### AI Behaviors
- **info_0**: Swoops to sting spiders
- **info_1**: Drags paralyzed prey
- **info_2**: Buries spiders underground
- **info_3**: Erratic flight
- **info_4**: Defends owner (tamed)

### Breeding
- **Item**: Fermented Spider Eyes
- **Produces**: Grubs from buried spiders

### Variants
- Nether variant (yellow-orange)

### Combat
- **info_0**: Debilitating Sting (paralyzes arthropods)
- **info_1**: Damages non-arthropods to half health
- **info_2**: Erratic flight (hard to hit)
- **info_3**: Frequent stinging


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Tarantula Hawk entity is summonable (`/summon xeenaa-alexs-mobs:tarantula-hawk`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Nether variant, baby, angry states
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- TarantulaHawkEntity class
- TarantulaHawkModel class (GeckoLib model)
- TarantulaHawkRenderer class (GeckoLib renderer)
- Geometry file (tarantula-hawk.geo.json)
- Animation file (tarantula-hawk.animation.json)
- Texture file(s) (tarantula-hawk.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Nether variant, baby, angry states

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
2. **Special Features**: Nether variant, baby, angry states
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Tarantula Hawk cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `TarantulaHawkEntity.java`
- `TarantulaHawkModel.java`
- `TarantulaHawkRenderer.java`

### Resource Files
- `tarantula-hawk.geo.json`
- `tarantula-hawk.animation.json`
- `tarantula-hawk.png` (+ variants)
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
- [ ] Special features work: Nether variant, baby, angry states
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Nether variant, baby, angry states
- Biomes: Desert, Badlands

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 112

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
