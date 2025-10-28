# Epic 22: Caiman - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Caiman** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Swamp, Mangrove Swamp
**Key Features**: Aquatic predator

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter caiman in swamp, mangrove swamp
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Aquatic predator

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Swamp, Mangrove Swamp

---

## Features & Functionality

### Core Features
- Small crocodilians
- Hunts fish and small animals
- Vibrates at surface (dominance display)

### Taming
- **Method**: Hatch eggs around player (imprinting)
- **Benefits**: Defend owner, grab and hold targets

### Special Mechanics
- Egg laying: Breed with catfish, lay eggs on ground
- Imprinting: Hatchlings view nearby person as parent
- Grab attack: Holds targets still (owner can flee or exploit limited movement)

### Items & Interactions
- **Eats**: Catfish

### AI Behaviors
- **info_0**: Hunts fish
- **info_1**: Dominance displays (vibration)
- **info_2**: Grab and hold attack
- **info_3**: Defends owner

### Breeding
- **Item**: Catfish
- **Produces**: Eggs → Tamed hatchlings (if near player)

### Combat
- **info_0**: Grab and hold attack


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Caiman entity is summonable (`/summon xeenaa-alexs-mobs:caiman`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Aquatic predator
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CaimanEntity class
- CaimanModel class (GeckoLib model)
- CaimanRenderer class (GeckoLib renderer)
- Geometry file (caiman.geo.json)
- Animation file (caiman.animation.json)
- Texture file(s) (caiman.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Aquatic predator

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
2. **Special Features**: Aquatic predator
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Caiman cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CaimanEntity.java`
- `CaimanModel.java`
- `CaimanRenderer.java`

### Resource Files
- `caiman.geo.json`
- `caiman.animation.json`
- `caiman.png` (+ variants)
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
- [ ] Special features work: Aquatic predator
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Aquatic predator
- Biomes: Swamp, Mangrove Swamp

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 44

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
