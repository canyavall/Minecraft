# Epic 55: Komodo Dragon - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Komodo Dragon** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Sparse Jungle, Badlands
**Key Features**: Rideable, saddle + maid variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter komodo dragon in sparse jungle, badlands
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Rideable, saddle + maid variant

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Sparse Jungle, Badlands

---

## Features & Functionality

### Core Features
- Largest lizards
- See everything as food
- Cannibalistic
- Poisonous bite

### Taming
- **Method**: Feed multiple entire stacks of Rotten Flesh
- **Benefits**: Rideable (saddleable), stay/follow/wander

### Special Mechanics
- Wrestling: Fight each other for dominance, stand on hind legs
- Eat entire stacks: Consumes full stacks from players
- Saddle: Can be ridden, surprisingly quick mount
- Komodo Dragon Spit: Occasional drip
- Spit uses: Extend Poison Resistance duration, alternative to Poisonous Essence, craft slimeball (3 spit)

### Items & Interactions
- **Drops**: Komodo Dragon Spit
- **Eats**: Rotten Flesh (taming, entire stacks)
- **Crafted**: Slimeball (3 spit), Poison Resistance extension, Poisonous Essence alternative

### AI Behaviors
- **info_0**: Stay/follow/wander modes
- **info_1**: Attacks almost everything
- **info_2**: Cannibalistic (attacks weakened Komodos, babies)
- **info_3**: Wrestling displays

### Equipment & Gear
- Saddle

### Combat
- **info_0**: Poisonous bite
- **info_1**: Savage attacks


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Komodo Dragon entity is summonable (`/summon xeenaa-alexs-mobs:komodo-dragon`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Rideable, saddle + maid variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- KomodoDragonEntity class
- KomodoDragonModel class (GeckoLib model)
- KomodoDragonRenderer class (GeckoLib renderer)
- Geometry file (komodo-dragon.geo.json)
- Animation file (komodo-dragon.animation.json)
- Texture file(s) (komodo-dragon.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Rideable, saddle + maid variant

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
2. **Special Features**: Rideable, saddle + maid variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Komodo Dragon cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `KomodoDragonEntity.java`
- `KomodoDragonModel.java`
- `KomodoDragonRenderer.java`

### Resource Files
- `komodo-dragon.geo.json`
- `komodo-dragon.animation.json`
- `komodo-dragon.png` (+ variants)
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
- [ ] AI behaves according to Placatable type
- [ ] Special features work: Rideable, saddle + maid variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Complex
- Special features: Rideable, saddle + maid variant
- Biomes: Sparse Jungle, Badlands

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 77

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
