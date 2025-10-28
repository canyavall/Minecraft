# Epic 17: Blobfish - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Blobfish** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Deep Ocean
**Key Features**: Pressure variants (depressurized)

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter blobfish in deep ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Pressure variants (depressurized)

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Deep Ocean

---

## Features & Functionality

### Core Features
- Gelatinous fish
- Grey and compressed naturally
- Pink pile of sludge in low pressure

### Special Mechanics
- Pressure-dependent: Pink sludge under 10 blocks of water
- Fish Oil crafting: 4 blobfish + bottle = Fish Oil
- Fish Oil effect: Limited levitation in water or rain
- Can be bucketed
- Slimeball application: Allows land survival (display purposes)

### Items & Interactions
- **Drops**: Blobfish (itself)
- **Crafted**: Fish Oil (4 blobfish + bottle)
- **Uses**: Slimeball (land survival)

### AI Behaviors
- **info_0**: Passive swimming
- **info_1**: Pressure-based appearance change

### Variants
- Grey compressed (deep water)
- Pink sludge (shallow water)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Blobfish entity is summonable (`/summon xeenaa-alexs-mobs:blobfish`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Pressure variants (depressurized)
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BlobfishEntity class
- BlobfishModel class (GeckoLib model)
- BlobfishRenderer class (GeckoLib renderer)
- Geometry file (blobfish.geo.json)
- Animation file (blobfish.animation.json)
- Texture file(s) (blobfish.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Pressure variants (depressurized)

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
1. **Complexity**: Simple - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Pressure variants (depressurized)
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Blobfish cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `BlobfishEntity.java`
- `BlobfishModel.java`
- `BlobfishRenderer.java`

### Resource Files
- `blobfish.geo.json`
- `blobfish.animation.json`
- `blobfish.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 2-3 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Passive type
- [ ] Special features work: Pressure variants (depressurized)
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Pressure variants (depressurized)
- Biomes: Deep Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 39

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
