# Epic 34: Dropbear - Hostile Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Dropbear** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Hostile
**Biome(s)**: Old Growth Taiga
**Key Features**: Ambush predator, glow eyes

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter dropbear in old growth taiga
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (hostile)
   - Hostile-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Ambush predator, glow eyes

### Game Mechanics

**Entity Type**: Hostile Animal
**Size**: TBD (check original model)
**Behavior**: hostile
**Health**: TBD
**Drops**: TBD
**Spawning**: Old Growth Taiga

---

## Features & Functionality

### Core Features
- Hostile demonic marsupial
- Hangs from Nether ceiling
- Ambush predator

### Special Mechanics
- Ceiling hanging: Waits for prey below
- Drop attack: Leaps and crushes target
- Leap back to ceiling after kill
- Huge jaws and sharp claws

### Items & Interactions
- **Drops**: Dropbear Claw
- **Crafted**: Flint and Steel (no iron needed), Clinging Potion (walk on ceilings)

### AI Behaviors
- **info_0**: Hangs upside down
- **info_1**: Waits for prey
- **info_2**: Drop attacks
- **info_3**: Returns to ceiling

### Combat
- **info_0**: Drop and crush attack
- **info_1**: Jaw attacks
- **info_2**: Claw attacks
- **info_3**: Ceiling mobility


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Dropbear entity is summonable (`/summon xeenaa-alexs-mobs:dropbear`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Hostile behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Ambush predator, glow eyes
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- DropbearEntity class
- DropbearModel class (GeckoLib model)
- DropbearRenderer class (GeckoLib renderer)
- Geometry file (dropbear.geo.json)
- Animation file (dropbear.animation.json)
- Texture file(s) (dropbear.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Ambush predator, glow eyes

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
2. **Special Features**: Ambush predator, glow eyes
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Dropbear cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `DropbearEntity.java`
- `DropbearModel.java`
- `DropbearRenderer.java`

### Resource Files
- `dropbear.geo.json`
- `dropbear.animation.json`
- `dropbear.png` (+ variants)
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
- [ ] Special features work: Ambush predator, glow eyes
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Hostile
- Complexity: Medium
- Special features: Ambush predator, glow eyes
- Biomes: Old Growth Taiga

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 56

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
