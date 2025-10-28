# Epic 65: Mungus - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Mungus** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Mushroom Fields
**Key Features**: Sack equipment, shoes, special beam

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter mungus in mushroom fields
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Sack equipment, shoes, special beam

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Mushroom Fields

---

## Features & Functionality

### Core Features
- Strange fungal creatures
- Relatives of Guardian and Strider
- Mycelium coat with mushrooms

### Special Mechanics
- Mushroom feeding: More of same mushroom → more mushrooms grow on Mungus
- Guardian beam: Fires at nearby same-type mushrooms, grows them
- Giant mushroom: Over mycelium, mushroom may become giant
- Exhausts Mungus, lowers mushroom count
- Explosion: Slain with 5 mushrooms → violent explosion + biome conversion
- Biome conversion: Red/Brown → Mushroom Fields, Crimson → Crimson Forest, Warped → Warped Forest
- Poisonous Potato: Undoes biome conversion

### Items & Interactions
- **Eats**: Mushrooms (any type), Mungal Spores (breeding), Poisonous Potato (undo conversion)
- **Drops**: Mungal Spores (every few days)

### AI Behaviors
- **info_0**: Passive
- **info_1**: Fires beams at mushrooms
- **info_2**: Grows mushrooms
- **info_3**: Explodes (5 mushrooms on back)

### Breeding
- **Item**: Mungal Spores


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Mungus entity is summonable (`/summon xeenaa-alexs-mobs:mungus`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Sack equipment, shoes, special beam
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MungusEntity class
- MungusModel class (GeckoLib model)
- MungusRenderer class (GeckoLib renderer)
- Geometry file (mungus.geo.json)
- Animation file (mungus.animation.json)
- Texture file(s) (mungus.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Sack equipment, shoes, special beam

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
2. **Special Features**: Sack equipment, shoes, special beam
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Mungus cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MungusEntity.java`
- `MungusModel.java`
- `MungusRenderer.java`

### Resource Files
- `mungus.geo.json`
- `mungus.animation.json`
- `mungus.png` (+ variants)
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
- [ ] AI behaves according to Passive type
- [ ] Special features work: Sack equipment, shoes, special beam
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Complex
- Special features: Sack equipment, shoes, special beam
- Biomes: Mushroom Fields

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 87

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
