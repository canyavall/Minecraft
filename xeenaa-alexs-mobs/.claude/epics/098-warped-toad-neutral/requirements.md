# Epic 98: Warped Toad - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Warped Toad** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Warped Forest (Nether)
**Key Features**: 3 variants (base/glow/pepe) with blink states

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter warped toad in warped forest (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 3 variants (base/glow/pepe) with blink states

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Warped Forest (Nether)

---

## Features & Functionality

### Core Features
- Giant rare amphibian
- Covered in glowing shroomlight fungus
- Skilled swimmer (lava and water)

### Taming
- **Method**: Feed multiple Crimson Mosquito Larva
- **Benefits**: Wander/stay/follow, defense against Nether insects

### Special Mechanics
- Shroomlight drops on death (chance)
- Tongue attack: Extremely fast, catches Flies and Crimson Mosquitoes
- Crimson Mosquito Larva: Crafted with Maggot + Crimson Mosquito Proboscis

### Items & Interactions
- **Drops**: Shroomlight (chance)
- **Eats**: Crimson Mosquito Larva (taming, breeding), Maggots (healing), Flies, Crimson Mosquitoes
- **Crafted**: Crimson Mosquito Larva (Maggot + Proboscis)

### AI Behaviors
- **info_0**: Wander/stay/follow modes
- **info_1**: Hops in forests
- **info_2**: Swims in lava/water
- **info_3**: Fast tongue attacks
- **info_4**: Defends tamer

### Breeding
- **Item**: Crimson Mosquito Larva

### Combat
- **info_0**: Fast tongue shooting
- **info_1**: Catches flying insects


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Warped Toad entity is summonable (`/summon xeenaa-alexs-mobs:warped-toad`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 3 variants (base/glow/pepe) with blink states
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- WarpedToadEntity class
- WarpedToadModel class (GeckoLib model)
- WarpedToadRenderer class (GeckoLib renderer)
- Geometry file (warped-toad.geo.json)
- Animation file (warped-toad.animation.json)
- Texture file(s) (warped-toad.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 3 variants (base/glow/pepe) with blink states

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
2. **Special Features**: 3 variants (base/glow/pepe) with blink states
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Warped Toad cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `WarpedToadEntity.java`
- `WarpedToadModel.java`
- `WarpedToadRenderer.java`

### Resource Files
- `warped-toad.geo.json`
- `warped-toad.animation.json`
- `warped-toad.png` (+ variants)
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
- [ ] Special features work: 3 variants (base/glow/pepe) with blink states
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: 3 variants (base/glow/pepe) with blink states
- Biomes: Warped Forest (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 120

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
