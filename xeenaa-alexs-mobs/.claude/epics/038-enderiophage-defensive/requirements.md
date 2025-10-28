# Epic 38: Enderiophage - Defensive Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Enderiophage** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Defensive
**Biome(s)**: The End, Nether, Overworld
**Key Features**: 3 dimension variants with glow

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter enderiophage in the end, nether, overworld
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (defensive)
   - Defensive-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: 3 dimension variants with glow

### Game Mechanics

**Entity Type**: Defensive Animal
**Size**: TBD (check original model)
**Behavior**: defensive
**Health**: TBD
**Drops**: TBD
**Spawning**: The End, Nether, Overworld

---

## Features & Functionality

### Core Features
- Monstrous biochemical automaton
- Created to eliminate Endergrades
- Requires Eye of Ender to attack

### Special Mechanics
- Ender Flu effect: 10 minutes, host erupts with new phages and dies
- Cure: Chorus fruit or milk
- Eye shattering: After inflicting Ender Flu, eye shatters
- Eye stealing: Latches onto Enderman, inflicts blindness, steals eye
- Cannot attack without eye
- Capsid drop: Holds entire stack, items float up to capsid/inventory above
- Enderiophage Rocket: Capsid + iron nuggets + endstone = Elytra fuel
- Construction: Eye of Ender on Capsid above vertical End Rod = transforms to Enderiophage
- Dimension-based materials

### Items & Interactions
- **Drops**: Capsid
- **Crafted**: Enderiophage Rocket (Elytra fuel), Cosmic Cod (Cod in Capsid)

### AI Behaviors
- **info_0**: Hostile to Endergrades
- **info_1**: Hostile to Endermen
- **info_2**: Inflicts Ender Flu
- **info_3**: Latches to steal eyes
- **info_4**: Seeks sight of Endermen after eye loss

### Variants
- Materials vary by dimension

### Combat
- **info_0**: Ender Flu infliction
- **info_1**: Eye stealing from Endermen
- **info_2**: Requires eye to attack


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Enderiophage entity is summonable (`/summon xeenaa-alexs-mobs:enderiophage`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Defensive behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: 3 dimension variants with glow
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- EnderiophageEntity class
- EnderiophageModel class (GeckoLib model)
- EnderiophageRenderer class (GeckoLib renderer)
- Geometry file (enderiophage.geo.json)
- Animation file (enderiophage.animation.json)
- Texture file(s) (enderiophage.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: 3 dimension variants with glow

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
2. **Special Features**: 3 dimension variants with glow
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Enderiophage cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `EnderiophageEntity.java`
- `EnderiophageModel.java`
- `EnderiophageRenderer.java`

### Resource Files
- `enderiophage.geo.json`
- `enderiophage.animation.json`
- `enderiophage.png` (+ variants)
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
- [ ] AI behaves according to Defensive type
- [ ] Special features work: 3 dimension variants with glow
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Defensive
- Complexity: Medium
- Special features: 3 dimension variants with glow
- Biomes: The End, Nether, Overworld

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 60

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
