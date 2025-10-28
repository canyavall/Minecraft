# Epic 21: Cachalot Whale - Neutral Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Very Complex
**Priority**: Medium
**Estimated Effort**: 7-14 days

---

## Overview

Port the **Cachalot Whale** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: Neutral
**Biome(s)**: Ocean (Deep)
**Key Features**: Huge size, albino variant, echolocation

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter cachalot whale in ocean (deep)
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category (neutral)
   - Neutral-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: Huge size, albino variant, echolocation

### Game Mechanics

**Entity Type**: Neutral Animal
**Size**: TBD (check original model)
**Behavior**: neutral
**Health**: TBD
**Drops**: TBD
**Spawning**: Ocean (Deep)

---

## Features & Functionality

### Core Features
- Massive mammals
- Hunt squid (favorite prey)
- Sharp teeth
- Can break ice and boats

### Special Mechanics
- Echolocation: Sends sound to locate squid
- Ramming: Opens mouth full of teeth, charges prey
- Teeth drop: Occasional when hunting
- Ice breaking: Can break ice while moving
- Boat breaking: Breaks boats and wood when charging
- All-white variant: Much stronger (rare)
- Beach stranding: Spawn during thunderstorms, helpless
- Rescue reward: Push to water without hurting → Ambergris reward
- Orca threat: Young Cachalots preyed by Orcas, mothers defend
- Echolocator crafting: Reveals nearby caves (limited uses)
- Endolocator: Echolocator + 4 ender pearls = reveals End Portal near stronghold

### Items & Interactions
- **Drops**: Cachalot Whale Teeth, Ambergris (rescue reward)
- **Crafted**: Echolocator (reveals caves), Endolocator (finds End Portal)
- **Fuel**: Ambergris smelts 64 items
- **Trade**: Ambergris to fishermen villagers

### AI Behaviors
- **info_0**: Echolocation hunting
- **info_1**: Charges prey
- **info_2**: Breaks ice/boats
- **info_3**: Beach stranding
- **info_4**: Mothers defend young

### Variants
- Normal
- All-white (much stronger, rare)

### Combat
- **info_0**: Ram attack
- **info_1**: Sharp teeth
- **info_2**: Charges
- **info_3**: Breaks boats


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Cachalot Whale entity is summonable (`/summon xeenaa-alexs-mobs:cachalot-whale`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to Neutral behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: Huge size, albino variant, echolocation
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- CachalotWhaleEntity class
- CachalotWhaleModel class (GeckoLib model)
- CachalotWhaleRenderer class (GeckoLib renderer)
- Geometry file (cachalot-whale.geo.json)
- Animation file (cachalot-whale.animation.json)
- Texture file(s) (cachalot-whale.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: Huge size, albino variant, echolocation

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
1. **Complexity**: Very Complex - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: Huge size, albino variant, echolocation
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Cachalot Whale cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `CachalotWhaleEntity.java`
- `CachalotWhaleModel.java`
- `CachalotWhaleRenderer.java`

### Resource Files
- `cachalot-whale.geo.json`
- `cachalot-whale.animation.json`
- `cachalot-whale.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: 7-14 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to Neutral type
- [ ] Special features work: Huge size, albino variant, echolocation
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: Neutral
- Complexity: Very Complex
- Special features: Huge size, albino variant, echolocation
- Biomes: Ocean (Deep)

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line 43

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
