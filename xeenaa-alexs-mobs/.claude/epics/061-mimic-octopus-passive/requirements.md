# Epic 61: Mimic Octopus - Passive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Complex
**Priority**: Medium
**Estimated Effort**: 5-7 days

---

## Overview

Port the **Mimic Octopus** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Passive
**Biome(s)**: Warm Ocean
**Key Features**: Mimicry system (5 forms), overlay effects

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter mimic octopus in warm ocean
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (passive)
   - Passive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Mimicry system (5 forms), overlay effects

### Game Mechanics

**Entity Type**: Passive Animal
**Size**: TBD (check original model)
**Behavior**: passive
**Health**: TBD
**Drops**: TBD
**Spawning**: Warm Ocean

---

## Features & Functionality

### Core Features
- Curious cephalopod
- Rapid regeneration
- Shape-shifting and color changing

### Taming
- **Method**: Feed 5-8 Lobster Tails (not while camouflaged)
- **Benefits**: Scares attackers, land following, form control, combat mimicry

### Special Mechanics
- Camouflage: Matches any block, almost invisible
- Mimics: Pufferfish, Creepers, Guardians (when nearby)
- Form control: Ink sac toggles random form changes
- Memory system: Prismarine shard = Guardian, Pufferfish drop = Pufferfish, Creeper drop = Creeper
- Slimeball: Prevents drying on land (every few days)
- Mimicream upgrade: 5-8 mimicream = combat abilities (Guardian beams, Pufferfish poison, Creeper explosions)
- Can be bucketed

### Items & Interactions
- **Eats**: Lobster Tails (taming), Tropical Fish (breeding)
- **Uses**: Slimeball (land survival), Ink Sac (toggle forms), Prismarine Shard/Pufferfish/Creeper drops (memory)
- **Upgrade**: Mimicream (combat abilities)

### AI Behaviors
- **info_0**: Sit/wander/follow modes
- **info_1**: Camouflage as blocks
- **info_2**: Mimics nearby creatures
- **info_3**: Scares attackers (tamed)
- **info_4**: Can follow on land (with slimeball)

### Breeding
- **Item**: Tropical Fish

### Variants
- Can mimic: Blocks, Pufferfish, Creepers, Guardians
- Guardian: Beam attacks
- Pufferfish: Poison stabs
- Creeper: Explosions


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Mimic Octopus entity is summonable (`/summon xeenaa-alexs-mobs:mimic-octopus`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Passive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Mimicry system (5 forms), overlay effects
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- MimicOctopusEntity class
- MimicOctopusModel class (GeckoLib model)
- MimicOctopusRenderer class (GeckoLib renderer)
- Geometry file (mimic-octopus.geo.json)
- Animation file (mimic-octopus.animation.json)
- Texture file(s) (mimic-octopus.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Mimicry system (5 forms), overlay effects

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
2. **Special Features**: Mimicry system (5 forms), overlay effects
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Mimic Octopus cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `MimicOctopusEntity.java`
- `MimicOctopusModel.java`
- `MimicOctopusRenderer.java`

### Resource Files
- `mimic-octopus.geo.json`
- `mimic-octopus.animation.json`
- `mimic-octopus.png` (+ variants)
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
- [ ] AI behaves according to Passive type
- [ ] Special features work: Mimicry system (5 forms), overlay effects
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Passive
- Complexity: Complex
- Special features: Mimicry system (5 forms), overlay effects
- Biomes: Warm Ocean

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 83

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
