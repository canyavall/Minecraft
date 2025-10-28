# Epic 95: Triops - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Simple
**Priority**: Medium
**Estimated Effort**: 2-3 days

---

## Overview

Port the **Triops** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Desert (water)
**Key Features**: Small aquatic crustacean

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter triops in desert (water)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Small aquatic crustacean

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Desert (water)

---

## Features & Functionality

### Core Features
- Small shrimp-like crustaceans
- Three compound eyes
- Very primitive and hardy
- Consumes mosquito larvae
- Terrifies mosquitoes

### Special Mechanics
- Can be bucketed
- Mosquito repellent: Eggs can craft Mosquito Repellent Stew
- Stew makes Crimson Mosquitoes avoid player

### Items & Interactions
- **Eats**: Mosquito larvae, Carrots
- **Drops**: Triops Eggs
- **Crafted**: Mosquito Repellent Stew

### AI Behaviors
- **info_0**: Hunts mosquito larvae
- **info_1**: Mates when both fed carrots
- **info_2**: Lays eggs on water

### Breeding
- **Method**: Both Triops fed carrots, they find mate
- **Produces**: Eggs that hatch if given time
- **info_2**: Eggs can be broken for stew

### Combat
- **info_0**: Terrifies mosquitoes (passive)


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Triops entity is summonable (`/summon xeenaa-alexs-mobs:triops`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Small aquatic crustacean
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- TriopsEntity class
- TriopsModel class (GeckoLib model)
- TriopsRenderer class (GeckoLib renderer)
- Geometry file (triops.geo.json)
- Animation file (triops.animation.json)
- Texture file(s) (triops.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Small aquatic crustacean

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
2. **Special Features**: Small aquatic crustacean
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Triops cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `TriopsEntity.java`
- `TriopsModel.java`
- `TriopsRenderer.java`

### Resource Files
- `triops.geo.json`
- `triops.animation.json`
- `triops.png` (+ variants)
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
- [ ] Special features work: Small aquatic crustacean
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Simple
- Special features: Small aquatic crustacean
- Biomes: Desert (water)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 117

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
