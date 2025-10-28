# Epic 08: Hummingbird - Flying Passive Bird

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: Medium
**Priority**: High
**Estimated Effort**: 3-5 days

---

## Overview

Port the **Hummingbird** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib. The Hummingbird is a tiny flying passive bird known for its rapid wing movement, hovering ability, and attraction to flowers.

---

## Business Value

### Player Experience Goals

1. **Aesthetic Beauty** - Hummingbirds add vibrant color and grace to the world
   - Players enjoy watching hummingbirds hover near flowers
   - Colorful plumage creates visual interest
   - Elegant flight patterns are mesmerizing

2. **Nature Interaction** - Hummingbirds interact with the environment
   - Attracted to flowers (especially red ones)
   - Feed on nectar (potential mechanic)
   - Create ecosystem connections

3. **Educational Value** - Players learn about hummingbird biology
   - Rapid wing flapping (up to 80 beats per second in real life)
   - Hovering and backward flight capabilities
   - Relationship with flowers (pollination)

### Game Mechanics

**Entity Type**: Passive Flying Bird
**Size**: Tiny (0.4×0.3×0.4 blocks)
**Behavior**: Passive, hovers near flowers, fast flight
**Health**: 2 HP (fragile)
**Drops**: Feather (rare)
**Spawning**: Flower forests, jungle edges, plains with flowers

---

## Features & Functionality

### Core Features
- Smallest birds
- Extremely fast flight
- Intense pollinators (better than bees)

### Special Mechanics
- Pollination: More effective than bees
- Hummingbird Feeders: Crafted with Water Bottles + Sugar
- Feeders keep hummingbirds in area and boost pollination

### Items & Interactions
- **Eats**: Flowers (via pollination)
- **Interacts with**: Hummingbird Feeders

### AI Behaviors
- **info_0**: Fast flight
- **info_1**: Intense pollination
- **info_2**: Stays near feeders

### Breeding
- **Item**: Flowers


## Success Criteria

**Epic is COMPLETE when:**

- [ ] Hummingbird entity is summonable (`/summon xeenaa-alexs-mobs:hummingbird`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly (hover, fly, perch, feed, death)
- [ ] AI works (flies around, hovers at flowers, perches occasionally)
- [ ] Size and positioning look correct
- [ ] Performance is acceptable (multiple hummingbirds cause no lag)
- [ ] Sounds play appropriately
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- HummingbirdEntity class extending FlyingAnimalEntity
- HummingbirdModel class (GeckoLib model)
- HummingbirdRenderer class (GeckoLib renderer)
- Geometry file (hummingbird.geo.json)
- Animation file (hummingbird.animation.json)
- Texture file(s) (hummingbird.png, variants optional)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Basic sound effects

### Out of Scope

- Advanced flower pollination mechanics
- Breeding mechanics
- Nectar collection items
- Territory/nesting behavior
- Migration patterns
- Multiple color variants (can be added later)

---

## User Stories

### Story 1: Garden Wildlife
**As a** player building a garden
**I want** to see hummingbirds visiting my flowers
**So that** my garden feels alive and rewarding

**Acceptance Criteria**:
- [ ] Hummingbirds spawn near flowers
- [ ] Hummingbirds are attracted to flower blocks
- [ ] Hummingbirds hover at flowers in feeding animation

### Story 2: Realistic Flight Behavior
**As a** player observing wildlife
**I want** hummingbirds to hover and fly like real hummingbirds
**So that** they feel authentic and unique

**Acceptance Criteria**:
- [ ] Hummingbirds can hover in place
- [ ] Wings flap rapidly during hover
- [ ] Flight is fast and agile
- [ ] Can move backward or sideways

### Story 3: Beautiful Visuals
**As a** player exploring
**I want** hummingbirds to be visually striking
**So that** they stand out as special creatures

**Acceptance Criteria**:
- [ ] Colorful, vibrant texture
- [ ] Smooth animations
- [ ] Graceful movement patterns
- [ ] Appropriate tiny size

---

## Performance Requirements

**Target Specifications**:
- Support 20+ hummingbirds in loaded chunks
- No noticeable FPS drop
- Render time: <0.15ms per hummingbird
- Entity tick time: <0.08ms per hummingbird
- Memory: <3KB per hummingbird

---

## Risks & Mitigations

**Potential Risks**:
1. **Hover AI complexity** - May need custom goal for stationary hovering
   - Mitigation: Study vanilla bee hovering behavior
2. **Wing animation speed** - Very fast flapping may look choppy
   - Mitigation: Use high frame rate animation or blur effect
3. **Flower detection** - Finding nearby flowers efficiently
   - Mitigation: Use cached block scanning with cooldown

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: Hummingbird cataloged in mob inventory
- ✅ Epic 03: Conversion process established
- ✅ Epic 04: Flying AI patterns from Fly

**Nice to Have**:
- FlyingAnimalEntity base class improvements

---

## Deliverables

### Code Files
- `HummingbirdEntity.java` - Entity logic, AI, animation controller
- `HummingbirdModel.java` - GeckoLib model definition
- `HummingbirdRenderer.java` - Renderer with transformations

### Resource Files
- `hummingbird.geo.json` - Model geometry
- `hummingbird.animation.json` - All animations
- `hummingbird.png` - Texture (colorful plumage)
- Sound files (chirp, hum, land)

### Documentation
- Conversion notes (if applicable)
- Animation specifications
- AI behavior documentation

---

## Timeline

**Estimated Timeline**:
- Day 1: Convert model from Citadel to GeckoLib
- Day 2: Implement entity and basic AI
- Day 3: Create animations and test rendering
- Day 4: Implement hover/flower attraction AI
- Day 5: Add sounds, polish, and validate

**Total Effort**: 3-5 days

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] Hovers at flowers
- [ ] Perches on blocks
- [ ] Flies smoothly
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

**Automated Testing**: TBD (after manual validation)

---

## Future Enhancements

**Potential Improvements**:
1. Multiple color variants (ruby-throated, emerald, violet)
2. Nectar collection mechanic (player can harvest)
3. Breeding with sugar water
4. Pollination effects (bonus crop growth)
5. Territorial behavior near feeders
6. Migration/seasonal appearance

---

## Notes

**Key Considerations**:
- Hovering is the signature behavior - must look good
- Wing flapping speed is critical to authenticity
- Attraction to flowers should feel natural, not forced
- Size should be slightly larger than fly but still tiny

**References**:
- Study vanilla bee AI for flower interaction
- Review Epic 04 (Fly) for flying AI patterns
- Check Epic 07 (Crow) for bird-specific behaviors

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
