# Epic 93: Tiger - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Tiger** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Jungle, Bamboo Jungle
**Key Features**: White variant, sleeping/angry, glow eyes

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter tiger in jungle, bamboo jungle
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: White variant, sleeping/angry, glow eyes

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Jungle, Bamboo Jungle

---

## Features & Functionality

### Core Features
- Big cats
- Excellent hunters
- Extremely strong and fast
- Masters of stealth

### Special Mechanics
- Tiger's Blessing: Feed meat (chicken, porkchops preferred), grants temporary neutral status + tigers aid in combat
- Blessing duration: Few minutes, must continue feeding
- Lost if tiger attacked
- Invisibility: Nearly invisible when stalking from far
- Fear effect: Freezes prey for few seconds
- Protective parents

### Items & Interactions
- **Eats**: Chicken, Porkchops (preferred), other fresh meats

### AI Behaviors
- **info_0**: Wary of outsiders
- **info_1**: Attacks when approached
- **info_2**: Stealth stalking (nearly invisible)
- **info_3**: Fear effect on appearance
- **info_4**: Aids blessed players in combat
- **info_5**: Protective of young

### Breeding
- **Item**: Acacia Blossoms
- **Rare**: White tiger kittens (chance)

### Variants
- White tigers (rare breeding result)

### Combat
- **info_0**: Leap attacks (from many blocks away)
- **info_1**: Claw slashing
- **info_2**: Extremely fast
- **info_3**: Stealth attacks
- **info_4**: Fear effect


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Tiger entity is summonable (`/summon xeenaa-alexs-mobs:tiger`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: White variant, sleeping/angry, glow eyes
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- TigerEntity class
- TigerModel class (GeckoLib model)
- TigerRenderer class (GeckoLib renderer)
- Geometry file (tiger.geo.json)
- Animation file (tiger.animation.json)
- Texture file(s) (tiger.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: White variant, sleeping/angry, glow eyes

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
2. **Special Features**: White variant, sleeping/angry, glow eyes
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Tiger cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `TigerEntity.java`
- `TigerModel.java`
- `TigerRenderer.java`

### Resource Files
- `tiger.geo.json`
- `tiger.animation.json`
- `tiger.png` (+ variants)
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
- [ ] Special features work: White variant, sleeping/angry, glow eyes
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Complex
- Special features: White variant, sleeping/angry, glow eyes
- Biomes: Jungle, Bamboo Jungle

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 115

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
