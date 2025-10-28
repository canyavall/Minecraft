# Epic 09: Blobfish - Aquatic Passive Fish

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: Medium
**Estimated Effort**: 2-4 days

---

## Overview

Port the **Blobfish** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib. The Blobfish is a deep-sea fish known for its unique gelatinous appearance and sad expression, found only in deep ocean biomes.

---

## Business Value

### Player Experience Goals

1. **Unique Visual Appeal** - Blobfish are instantly recognizable and memorable
   - Players enjoy the distinctive "melting" appearance
   - Sad/goofy expression creates emotional response
   - Unusual creature adds novelty to deep ocean exploration

2. **Deep Ocean Content** - Adds life to underutilized deep ocean biomes
   - Gives players reason to explore deep waters
   - Creates atmospheric underwater scenes
   - Rewards deep-sea diving

3. **Educational Value** - Players learn about deep-sea adaptation
   - Real blobfish look different at depth vs surface
   - Demonstrates pressure adaptation
   - Raises awareness of unusual sea life

### Game Mechanics

**Entity Type**: Passive Aquatic Fish
**Size**: Medium (0.8×0.6×0.8 blocks)
**Behavior**: Passive, slow swimming near ocean floor
**Health**: 6 HP
**Drops**: Blobfish slime (unique item)
**Spawning**: Deep ocean biomes (Y < 30)

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
- [ ] Model appears correctly with gelatinous droopy appearance
- [ ] Animations play smoothly (swim, float, death)
- [ ] AI works (swims slowly, stays near ocean floor, avoids players)
- [ ] Spawns naturally in deep ocean biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- BlobfishEntity class extending AquaticAnimalEntity
- BlobfishModel class (GeckoLib model)
- BlobfishRenderer class (GeckoLib renderer)
- Geometry file (blobfish.geo.json)
- Animation file (blobfish.animation.json)
- Texture file (blobfish.png - pinkish-tan, sad face)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Basic sound effects
- Blobfish slime item (unique drop)

### Out of Scope

- Pressure mechanic (appearance change at different depths)
- Special interactions with other mobs
- Breeding mechanics
- Bucket capture (can be added later)
- Surface behavior (different from deep water)
- Multiple variants

---

## User Stories

### Story 1: Deep Ocean Exploration
**As a** player exploring deep oceans
**I want** to encounter unique creatures like blobfish
**So that** deep-sea diving feels rewarding and interesting

**Acceptance Criteria**:
- [ ] Blobfish spawn only in deep ocean (Y < 30)
- [ ] Blobfish are noticeable but not intrusive
- [ ] Encountering a blobfish feels like a discovery

### Story 2: Memorable Appearance
**As a** player who finds a blobfish
**I want** it to look distinctive and memorable
**So that** I remember the encounter

**Acceptance Criteria**:
- [ ] Blobfish has recognizable sad/droopy appearance
- [ ] Model matches expectations from real-world photos
- [ ] Gelatinous texture is apparent
- [ ] Size is appropriate (not too small to see)

### Story 3: Authentic Behavior
**As a** player observing a blobfish
**I want** it to move like a slow deep-sea creature
**So that** it feels realistic for its environment

**Acceptance Criteria**:
- [ ] Swimming is slow and lazy
- [ ] Stays near ocean floor
- [ ] Avoids players gently (not panicked)
- [ ] Body appears soft and floppy during movement

---

## Performance Requirements

**Target Specifications**:
- Support 10+ blobfish in loaded chunks
- No noticeable FPS drop
- Render time: <0.2ms per blobfish
- Entity tick time: <0.1ms per blobfish
- Memory: <4KB per blobfish

---

## Risks & Mitigations

**Potential Risks**:
1. **Gelatinous appearance** - Difficult to convey "soft" texture
   - Mitigation: Use translucency, smooth animations, droopy model
2. **Deep ocean spawning** - May be hard to find/test
   - Mitigation: Add debug spawn command, test in creative
3. **Sink behavior** - AI may conflict with floating physics
   - Mitigation: Custom gravity modifier for ocean floor attraction

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Blobfish cataloged in mob inventory
- ✅ Epic 03: Conversion process established
- AquaticAnimalEntity base class (likely exists or needs creation)

**Nice to Have**:
- Underwater rendering improvements

---

## Deliverables

### Code Files
- `BlobfishEntity.java` - Entity logic, AI, animation controller
- `BlobfishModel.java` - GeckoLib model definition
- `BlobfishRenderer.java` - Renderer with underwater effects

### Resource Files
- `blobfish.geo.json` - Model geometry (gelatinous body)
- `blobfish.animation.json` - All animations
- `blobfish.png` - Texture (pinkish-tan, sad face)
- Sound files (gurgle, ambient)

### Items
- Blobfish slime item (drop)

### Documentation
- Conversion notes
- Spawn configuration
- AI behavior documentation

---

## Timeline

**Estimated Timeline**:
- Day 1: Convert model from Citadel to GeckoLib
- Day 2: Implement entity and aquatic AI
- Day 3: Create animations and test rendering
- Day 4: Add sounds, drops, spawn rules, validate

**Total Effort**: 2-4 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in deep ocean
- [ ] Model renders properly (gelatinous appearance)
- [ ] All animations play correctly
- [ ] Swims slowly near ocean floor
- [ ] Avoids players gently
- [ ] Sounds play appropriately
- [ ] Drops blobfish slime on death
- [ ] Size and scale correct
- [ ] Performance acceptable

**Automated Testing**: TBD (after manual validation)

---

## Future Enhancements

**Potential Improvements**:
1. Bucket capture (blobfish in a bucket item)
2. Pressure mechanic (looks different at surface)
3. Breeding with special food
4. Blobfish slime crafting recipes
5. Aquarium behavior (if placed in tank)
6. Rare variant (different color)

---

## Notes

**Key Considerations**:
- Blobfish are slow and passive - AI should reflect this
- Gelatinous texture is the key visual feature
- Deep ocean spawning must be configured correctly
- Sad expression should be apparent but not exaggerated

**References**:
- Study vanilla cod/salmon for basic swimming AI
- Review AquaticAnimalEntity implementation
- Check Epic 06 (Triops) for aquatic behavior patterns

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
