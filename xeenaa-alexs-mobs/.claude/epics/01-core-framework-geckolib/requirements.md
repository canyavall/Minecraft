# Epic 01: Core Framework & GeckoLib Integration

**Epic Number**: 01
**Status**: PLANNING
**Priority**: Critical (Foundational)
**Created**: 2025-10-25
**Target Completion**: Foundation for all future mob porting

---

## Epic Summary

Establish the foundational infrastructure for porting Alex's Mobs to Fabric 1.21.1. This epic creates the core systems needed for entity registration, model rendering, animation support, and resource loading. Success means we have a proven, working framework that all future animal mobs can be built upon.

**Business Value**: Without this foundation, no mobs can be ported. This epic unlocks the ability to bring beloved Alex's Mobs creatures to Fabric players.

---

## Problem Statement

### Current Situation

- Alex's Mobs exists only for Forge/NeoForge, leaving Fabric players without access to 89+ beloved animal mobs
- The original mod uses Citadel (Forge-only animation library) which is incompatible with Fabric
- No standardized framework exists for porting Forge entities to Fabric with complex animations
- Players on Fabric servers cannot experience the rich biodiversity and gameplay enhancements that Alex's Mobs provides

### Why This Matters

Players choose Fabric for:
- Better performance than Forge
- Compatibility with performance mods (Sodium, Lithium)
- Cleaner mod ecosystem
- Server-side optimization

They should not have to sacrifice Alex's Mobs to get these benefits.

### What Success Looks Like

- A developer can create a new animal entity in under 30 minutes using our framework
- GeckoLib provides smooth, complex animations matching the original mod's quality
- Entity registration is simple and consistent
- The framework supports all planned features: AI goals, custom behaviors, sounds, drops, interactions
- Performance meets or exceeds the original Forge version
- Code is clean, well-documented, and follows Fabric best practices

---

## Features

### Feature 1: GeckoLib Integration

**Description**:
Integrate GeckoLib animation library (Fabric-compatible) to replace Citadel (Forge-only). GeckoLib provides complex 3D keyframe animations, sound/particle effects, and event-driven animation system.

**User Experience**:
- Players see smooth, natural animal animations (walking, idle, attacking, eating)
- Animations match the original Alex's Mobs quality
- No performance degradation from animation system
- Animations sync correctly in multiplayer

**Success Criteria**:
- GeckoLib dependency added to build.gradle and compiles successfully
- Test entity can load a .geo.json model
- Test entity plays basic animations (idle, walk)
- Animations render smoothly in both singleplayer and multiplayer
- No console errors or warnings related to GeckoLib

**Why This Feature**:
Complex animal animations are core to Alex's Mobs' appeal. Simple Java models cannot replicate the quality of the original. GeckoLib is the only Fabric-compatible solution that provides equivalent animation capabilities.

---

### Feature 2: Entity Registration System

**Description**:
Create a centralized, consistent system for registering animal entities using Fabric's registration patterns. This system handles entity type registration, attribute registration, and spawn egg creation.

**User Experience**:
- New animals appear in the world naturally
- Animals can be summoned using /summon command
- Spawn eggs work correctly in creative mode
- F3 debug shows correct entity information

**Success Criteria**:
- ModEntities registry class exists with clear registration pattern
- Test entity registers successfully without errors
- Entity appears in /summon command autocomplete
- Spawn egg works in creative inventory
- F3+B shows correct hitbox and entity ID
- Attributes (health, speed, damage) apply correctly

**Why This Feature**:
Every single animal mob (89+) will use this system. A clean, consistent registration pattern prevents bugs, reduces development time, and makes the codebase maintainable.

---

### Feature 3: Client Renderer Registration

**Description**:
Establish the client-side rendering pipeline for animal entities using Fabric's EntityRendererRegistry. Integrates with GeckoLib for model/texture loading.

**User Experience**:
- Animals look correct in-game (models and textures)
- No missing texture (pink/black checkerboard) errors
- Animals render at correct scale and orientation
- Shadow size matches entity size
- Rendering performance is smooth (60+ FPS)

**Success Criteria**:
- Client initialization registers renderers correctly
- Test entity has visible model and texture
- Entity renders from all camera angles
- Shadow renders beneath entity
- No Z-fighting or rendering glitches
- ResourceLocation paths resolve correctly

**Why This Feature**:
Rendering is the player's first impression. If animals don't look right, the port fails regardless of how good the code is. Establishing this early ensures visual quality throughout development.

---

### Feature 4: Base Entity Classes

**Description**:
Create abstract base classes that all animal entities inherit from. These classes provide common functionality: AI goal management, attribute definitions, sound handling, and Fabric-Forge compatibility patterns.

**User Experience**:
- Animals behave naturally (swim in water, avoid lava, pathfind around obstacles)
- Animals make appropriate sounds (idle, hurt, death)
- Animals respond to environment (panic when attacked, follow parent)
- Behavior is consistent across all ported animals

**Success Criteria**:
- BaseAnimalEntity abstract class exists
- Provides standard AI goal setup (swim, panic, wander, look)
- Handles sound registration pattern
- Implements breeding interface (if applicable)
- Test entity inherits and functions correctly
- Code is well-documented with Javadoc

**Why This Feature**:
With 89+ animals to port, shared code prevents duplication and ensures consistency. If every animal reimplements basic behaviors, bugs multiply and maintenance becomes impossible.

---

### Feature 5: Resource Loading System

**Description**:
Establish the pattern for loading and organizing resources: models (.geo.json), textures (.png), animations (.animation.json), sounds (.ogg), and loot tables (.json).

**User Experience**:
- Animals have correct textures (no missing textures)
- Sound effects play correctly and at appropriate volume
- Loot drops work when animals are killed
- Resources reload correctly when using F3+T

**Success Criteria**:
- Directory structure follows Fabric conventions
- ResourceLocation paths are consistent and documented
- Test entity loads all resources without errors
- F3+T reload works without crashes
- Sound system initializes correctly
- Loot tables generate when entity dies

**Why This Feature**:
Proper resource organization prevents "missing texture" bugs, makes assets easy to find, and ensures compatibility with resource packs. Getting this right early prevents having to reorganize 89+ mob files later.

---

## Out of Scope

The following are explicitly NOT part of this epic:

- Porting actual animal mobs (framework only; mob porting starts in Epic 03)
- Item system (mob drops and craftable items in later epics)
- Spawn system (biome-based spawning is separate)
- Multiplayer synchronization beyond basic entity sync
- Config system (not needed for framework validation)
- Performance optimization (framework must work first)
- Animal Dictionary (in-game documentation is later)

---

## User Stories

### Story 1: Developer Creating New Entity
**As a** developer working on this port
**I want** a simple, consistent pattern for adding new animals
**So that** I can port mobs quickly without reinventing the wheel each time

**Acceptance Criteria**:
- Registration requires < 10 lines of boilerplate code
- Pattern is documented with code examples
- New entity compiles and runs on first try

---

### Story 2: Player Experiencing Animals
**As a** Fabric player
**I want** animals to look and move naturally
**So that** the world feels alive and immersive

**Acceptance Criteria**:
- Animals have smooth animations (not jerky)
- Textures are crisp and correct
- Animals do not flicker or glitch
- Performance is smooth (60+ FPS on mid-range hardware)

---

### Story 3: Multiplayer Server Admin
**As a** server administrator
**I want** animals to work correctly for all players
**So that** everyone has the same gameplay experience

**Acceptance Criteria**:
- Animals render correctly for all connected clients
- Animations sync across players
- No server-side errors or crashes
- TPS remains stable (20 TPS) with test entities

---

## Technical Considerations

### Dependencies

**GeckoLib for Fabric 1.21**:
- Version: 5.0+ (supports Fabric 1.21.x)
- Integration: Add to build.gradle, include in mod
- Purpose: Animation system replacement for Citadel

**Fabric API**:
- Already included in project
- Used for: Entity registration, rendering, events

### Performance Requirements

- Entity tick time: < 0.05ms per entity
- Render time: < 0.1ms per entity at 60 FPS
- Memory: Efficient model caching (no leaks)
- Startup time: < 2 seconds added to game launch

---

## Success Metrics

### Must Have

- GeckoLib compiles and loads without errors
- Test entity renders with model and texture
- Test entity plays basic animations (idle, walk)
- Entity registration pattern established
- Client renderer registration works
- Base entity class provides reusable functionality
- Resources load from correct directories

---

## Epic Completion Criteria

This epic is **COMPLETE** when:

1. All 5 features are implemented and working
2. Manual testing passes all test cases
3. User validates framework is ready for mob porting
4. Code is committed and documented

After completion, run `/create_epic "Mob Catalog & Prioritization"` to begin Epic 02.

---

**Next Step**: User should review this requirements document, make any changes, then run `/create_plan` to generate technical implementation tasks.
