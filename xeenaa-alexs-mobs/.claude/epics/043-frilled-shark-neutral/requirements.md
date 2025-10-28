# Epic 43: Frilled Shark - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Frilled Shark** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Deep Ocean
**Key Features**: Depressurized variant, kaiju variant

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter frilled shark in deep ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Depressurized variant, kaiju variant

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Deep Ocean

---

## Features & Functionality

### Core Features
- Lethargic living fossils
- Hunt blobfish and squid
- Reflective teeth (lure prey)

### Special Mechanics
- Luring: Uses reflective teeth in low-light to lure squid
- Exsanguination effect: Serrated teeth quickly drain health
- Teeth shedding: Occasional when attacking squid
- Can be bucketed
- Depressurized: Discolored and slower under 10 blocks water

### Items & Interactions
- **Drops**: Serrated Shark Teeth (shed)
- **Crafted**: Shield of the Deep (more durable, inflicts Exsanguination when blocking)

### AI Behaviors
- **info_0**: Lethargic hunting
- **info_1**: Lures prey with teeth
- **info_2**: Exsanguination attacks

### Variants
- Normal (deep)
- Depressurized (pale, slow, shallow)

### Combat
- **info_0**: Serrated teeth
- **info_1**: Exsanguination effect


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Frilled Shark entity is summonable (`/summon xeenaa-alexs-mobs:frilled-shark`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Depressurized variant, kaiju variant
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- FrilledSharkEntity class
- FrilledSharkModel class (GeckoLib model)
- FrilledSharkRenderer class (GeckoLib renderer)
- Geometry file (frilled-shark.geo.json)
- Animation file (frilled-shark.animation.json)
- Texture file(s) (frilled-shark.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Depressurized variant, kaiju variant

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
2. **Special Features**: Depressurized variant, kaiju variant
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Frilled Shark cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `FrilledSharkEntity.java`
- `FrilledSharkModel.java`
- `FrilledSharkRenderer.java`

### Resource Files
- `frilled-shark.geo.json`
- `frilled-shark.animation.json`
- `frilled-shark.png` (+ variants)
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
- [ ] Special features work: Depressurized variant, kaiju variant
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Medium
- Special features: Depressurized variant, kaiju variant
- Biomes: Deep Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 65

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
