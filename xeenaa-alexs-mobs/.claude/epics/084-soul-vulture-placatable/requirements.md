# Epic 84: Soul Vulture - Placatable Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Soul Vulture** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Placatable
**Biome(s)**: Soul Sand Valley (Nether)
**Key Features**: 3 flame variants + glow

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter soul vulture in soul sand valley (nether)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (placatable)
   - Placatable-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 3 flame variants + glow

### Game Mechanics

**Entity Type**: Placatable Animal
**Size**: TBD (check original model)
**Behavior**: placatable
**Health**: TBD
**Drops**: TBD
**Spawning**: Soul Sand Valley (Nether)

---

## Features & Functionality

### Core Features
- Withered remains of dead birds
- Circle giant skeletons
- Health-stealing

### Special Mechanics
- Health-sap assault: Steals health up to 5 times max
- Soul Heart drop: Brews Potion of Soulsteal (same health-sapping)
- Natural enemy of Piglins
- Prey to Bone Serpents

### Items & Interactions
- **Drops**: Soul Heart
- **Crafted**: Potion of Soulsteal

### AI Behaviors
- **info_0**: Circles skeletons
- **info_1**: Launches health-stealing attacks
- **info_2**: Attacks Piglins
- **info_3**: Flees from Bone Serpents

### Combat
- **info_0**: Health-stealing assault (5 times max)
- **info_1**: Attacks Piglins


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Soul Vulture entity is summonable (`/summon xeenaa-alexs-mobs:soul-vulture`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Placatable behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 3 flame variants + glow
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- SoulVultureEntity class
- SoulVultureModel class (GeckoLib model)
- SoulVultureRenderer class (GeckoLib renderer)
- Geometry file (soul-vulture.geo.json)
- Animation file (soul-vulture.animation.json)
- Texture file(s) (soul-vulture.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 3 flame variants + glow

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
2. **Special Features**: 3 flame variants + glow
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Soul Vulture cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `SoulVultureEntity.java`
- `SoulVultureModel.java`
- `SoulVultureRenderer.java`

### Resource Files
- `soul-vulture.geo.json`
- `soul-vulture.animation.json`
- `soul-vulture.png` (+ variants)
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
- [ ] AI behaves according to Placatable type
- [ ] Special features work: 3 flame variants + glow
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Placatable
- Complexity: Medium
- Special features: 3 flame variants + glow
- Biomes: Soul Sand Valley (Nether)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 106

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
