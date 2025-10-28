# Epic 50: Guster - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Guster** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Desert, Badlands
**Key Features**: Sandstorm, 3 variants + glow overlays

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter guster in desert, badlands
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Sandstorm, 3 variants + glow overlays

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert, Badlands

---

## Features & Functionality

### Core Features
- Vengeful desert spirits
- Manifest during storms
- Formed from local sand

### Special Mechanics
- Throw targets high into air
- Shoot sand stream
- Lift and toss dropped items
- Resistant to projectiles
- Sand type variants: Soul sand or red sand appearances

### Items & Interactions
- **Drops**: Sand, Guster Eye
- **Crafted**: Gustmaker (redstone-powered gust block), Pocket of Sand (weapon)

### AI Behaviors
- **info_0**: Manifests in storms
- **info_1**: Throws enemies
- **info_2**: Shoots sand
- **info_3**: Lifts items
- **info_4**: Projectile resistance

### Variants
- Soul sand appearance
- Red sand appearance

### Combat
- **info_0**: Throw into air
- **info_1**: Sand stream shooting
- **info_2**: Resistant to projectiles


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Guster entity is summonable (`/summon xeenaa-alexs-mobs:guster`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Sandstorm, 3 variants + glow overlays
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- GusterEntity class
- GusterModel class (GeckoLib model)
- GusterRenderer class (GeckoLib renderer)
- Geometry file (guster.geo.json)
- Animation file (guster.animation.json)
- Texture file(s) (guster.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Sandstorm, 3 variants + glow overlays

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
2. **Special Features**: Sandstorm, 3 variants + glow overlays
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Guster cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `GusterEntity.java`
- `GusterModel.java`
- `GusterRenderer.java`

### Resource Files
- `guster.geo.json`
- `guster.animation.json`
- `guster.png` (+ variants)
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
- [ ] AI behaves according to Hostile type
- [ ] Special features work: Sandstorm, 3 variants + glow overlays
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Sandstorm, 3 variants + glow overlays
- Biomes: Desert, Badlands

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 72

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
