# Epic 03: Proof of Concept - First Three Animals - Implementation Plan

**Epic**: 03-proof-of-concept
**Status**: READY
**Created**: 2025-10-26
**Total Tasks**: 18
**Estimated Effort**: 120-180 hours (1-2 weeks)

---

## Task Overview

| Phase | Tasks | Status |
|-------|-------|--------|
| Foundation | TASK-001 to TASK-003 | TODO (3 tasks) |
| Mob 1: Fly | TASK-004 to TASK-007 | TODO (4 tasks) |
| Mob 2: Cockroach | TASK-008 to TASK-011 | TODO (4 tasks) |
| Mob 3: Triops | TASK-012 to TASK-015 | TODO (4 tasks) |
| Validation & Documentation | TASK-016 to TASK-018 | TODO (3 tasks) |

---

## Phase 1: Foundation (3 tasks)

### TASK-001: Set Up Development Environment & Blockbench

**Agent**: implementation-agent
**Estimated Time**: 2-3 hours
**Priority**: Critical (blocks all asset creation)
**Dependencies**: None

**Goal**: Set up Blockbench with GeckoLib plugin and create base templates

**Requirements**:
1. **Install Blockbench** (if not already installed):
   - Download from https://www.blockbench.net/
   - Install GeckoLib plugin (File → Plugins → Available → GeckoLib Animation Utils)
   - Configure export settings for GeckoLib models

2. **Create Blockbench templates**:
   - **insect_template.bbmodel**: 6-legged insect base (for Fly, Cockroach, beetles)
     - Body: 1 cube (thorax)
     - Head: 1 cube with antennae
     - Wings: 2 cubes (left/right) - for flying insects
     - Legs: 6 cubes (3 pairs)
     - Total: ~10 cubes
   - **fish_template.bbmodel**: Aquatic creature base (for Triops, small fish)
     - Body: 2-3 cubes (head, body, tail)
     - Fins: 2-4 cubes
     - Tail: 1 cube
     - Total: ~7 cubes
   - Save templates to `docs/blockbench-templates/`

3. **Test GeckoLib export**:
   - Create simple test model (cube with animation)
   - Export .geo.json and .animation.json
   - Verify files are valid JSON
   - Place in project assets and test load (optional quick validation)

4. **Document Blockbench workflow**:
   - Create `docs/epic-03/BLOCKBENCH_SETUP.md` with:
     - Installation instructions
     - Plugin setup
     - Export process
     - Common issues and fixes

**Acceptance Criteria**:
- ✅ Blockbench installed with GeckoLib plugin
- ✅ Two templates created (insect_template.bbmodel, fish_template.bbmodel)
- ✅ Templates saved to `docs/blockbench-templates/`
- ✅ GeckoLib export tested and working
- ✅ BLOCKBENCH_SETUP.md created

**References**:
- Requirements: Asset Creation Workflow
- Requirements: Deliverable #3 (Blockbench Templates)

---

### TASK-002: Create Base Entity Classes (FlyingAnimalEntity, AquaticAnimalEntity)

**Agent**: implementation-agent
**Estimated Time**: 3-4 hours
**Priority**: Critical (blocks mob implementation)
**Dependencies**: None (Epic 01 already has BaseAnimalEntity)

**Goal**: Create reusable base classes for flying and aquatic mobs

**Requirements**:

1. **Create FlyingAnimalEntity.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/FlyingAnimalEntity.java`
   - Extends `BaseAnimalEntity` (from Epic 01)
   - Features:
     - 3D pathfinding navigation (like bat/parrot)
     - Flying movement controller
     - Landing behavior (can land on blocks, rest)
     - Flee upward when threatened
     - Fall slowly when not flying (no fall damage)
   - Constructor: `protected FlyingAnimalEntity(EntityType<? extends FlyingAnimalEntity> type, World world)`

2. **Create AquaticAnimalEntity.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/AquaticAnimalEntity.java`
   - Extends `BaseAnimalEntity` (from Epic 01)
   - Features:
     - Swimming navigation (like fish)
     - Water-only survival (die outside water after 30s)
     - Float on death (like fish)
     - Swim in random directions
     - Avoid land (pathfind back to water)
   - Constructor: `protected AquaticAnimalEntity(EntityType<? extends AquaticAnimalEntity> type, World world)`

3. **Code Standards**:
   - Comprehensive Javadoc (class and method level)
   - Input validation (null checks)
   - Thread safety (if applicable)
   - Follow coding-standards skill (Java 21 features)
   - Proper error handling

4. **Testing Approach**:
   - Manual testing when implementing first mob (Fly, Triops)
   - Verify no compilation errors
   - Verify classes are abstract (cannot be instantiated directly)

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/FlyingAnimalEntity.java`
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/AquaticAnimalEntity.java`

**Acceptance Criteria**:
- ✅ FlyingAnimalEntity.java created with 3D pathfinding support
- ✅ AquaticAnimalEntity.java created with swimming support
- ✅ Both extend BaseAnimalEntity
- ✅ Comprehensive Javadoc
- ✅ No compilation errors
- ✅ Ready to be extended by Fly and Triops entities

**References**:
- Requirements: Code Patterns (Reusable Templates)
- Requirements: Deliverable #2 (Reusable Base Classes)

---

### TASK-003: Create AI Goal Templates

**Agent**: implementation-agent
**Estimated Time**: 2-3 hours
**Priority**: High
**Dependencies**: TASK-002 (base entity classes)

**Goal**: Create reusable AI goal classes for common behaviors

**Requirements**:

1. **Create WanderInAirGoal.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/WanderInAirGoal.java`
   - For flying mobs (Fly, future birds)
   - Random 3D movement (x, y, z coordinates)
   - Avoid ground (prefer y > 2 blocks)
   - Speed parameter (configurable)

2. **Create FleeFromLightGoal.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/FleeFromLightGoal.java`
   - For light-averse mobs (Cockroach, cave creatures)
   - Pathfind to dark areas (light level < 7)
   - Flee from torches/sunlight
   - Speed parameter (configurable)

3. **Create BurrowInBlockGoal.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/BurrowInBlockGoal.java`
   - For burrowing mobs (Triops, moles)
   - Burrow into sand/gravel when idle
   - Re-emerge after delay
   - Configurable block types

4. **Code Standards**:
   - Extend vanilla `Goal` class
   - Use goal priority system
   - Comprehensive Javadoc
   - Null safety
   - Performance-conscious (avoid expensive operations in tick())

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/WanderInAirGoal.java`
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/FleeFromLightGoal.java`
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/BurrowInBlockGoal.java`

**Acceptance Criteria**:
- ✅ All 3 AI goal classes created
- ✅ Extend vanilla Goal class properly
- ✅ Configurable parameters (speed, block types, etc.)
- ✅ Comprehensive Javadoc
- ✅ No compilation errors
- ✅ Ready to be used by mobs

**References**:
- Requirements: Code Patterns (AI Goal Templates)
- Requirements: Game Mechanics (Mob Behaviors)

---

## Phase 2: Mob 1 - Fly (4 tasks)

### TASK-004: Create Fly Model & Animations in Blockbench

**Agent**: implementation-agent
**Estimated Time**: 15-20 hours
**Priority**: Critical
**Dependencies**: TASK-001 (Blockbench setup)

**Goal**: Create GeckoLib model and animations for Fly in Blockbench

**Requirements**:

1. **Research**:
   - Watch Alex's Mobs Fly footage (YouTube, wiki)
   - Study real fly anatomy for reference
   - Note: Keep it SIMPLE (this is ultra-simple mob)

2. **Create Model** (use insect_template.bbmodel as base):
   - **Body parts**:
     - Thorax: 1 cube (4x3x3 pixels)
     - Head: 1 cube (3x3x3 pixels) with compound eyes
     - Wings: 2 cubes (transparent, 6x4x1 pixels each)
     - Legs: 6 cubes (1x3x1 pixels each, 3 pairs)
     - Antennae: 2 small cubes (optional)
   - **Total**: ~12 cubes (simple geometry)
   - **Size**: Tiny (0.3 x 0.2 x 0.3 blocks in-game)
   - **Bone hierarchy**:
     - root → body → head, wings, legs
   - **Pivot points**: Set correctly for animations

3. **Create Animations**:
   - **idle** (2 second loop):
     - Wings at rest (folded or slightly open)
     - Subtle body bob (0.5 pixel up/down)
     - Antennae twitch
   - **fly** (1 second loop):
     - Wings flap rapidly (30-40 flaps/sec visual illusion)
     - Body slight tilt forward
     - Legs tucked close to body
   - **death** (1.5 second play-once):
     - Fall from sky (rotate 180°)
     - Wings stop moving
     - Spin slightly

4. **Create Texture**:
   - Resolution: 16x16 or 32x32 (keep simple)
   - Colors: Black/grey fly body, transparent wings
   - Eyes: Red compound eyes
   - Save as `fly.png`

5. **Export**:
   - Export model: `fly.geo.json` → `assets/xeenaa-alexs-mobs/geo/`
   - Export animations: `fly.animation.json` → `assets/xeenaa-alexs-mobs/animations/`
   - Export texture: `fly.png` → `assets/xeenaa-alexs-mobs/textures/entity/`

6. **Track Time**:
   - Start timer when starting model
   - Record: Model creation (X hours), Animation (Y hours), Texture (Z hours)
   - Save to `docs/epic-03/EFFORT_TRACKING.md`

**Deliverable**:
- `assets/xeenaa-alexs-mobs/geo/fly.geo.json`
- `assets/xeenaa-alexs-mobs/animations/fly.animation.json`
- `assets/xeenaa-alexs-mobs/textures/entity/fly.png`
- Time tracking entry in EFFORT_TRACKING.md

**Acceptance Criteria**:
- ✅ Fly model created (simple, ~12 cubes)
- ✅ 3 animations created (idle, fly, death)
- ✅ Texture created (16x16 or 32x32)
- ✅ All files exported to correct locations
- ✅ Files are valid JSON (no syntax errors)
- ✅ Time tracked and documented

**References**:
- Requirements: Game Mechanics (Fly behavior)
- Requirements: Asset Creation Workflow

---

### TASK-005: Implement Fly Entity Class & AI

**Agent**: implementation-agent
**Estimated Time**: 8-12 hours
**Priority**: Critical
**Dependencies**: TASK-002 (FlyingAnimalEntity), TASK-003 (AI goals), TASK-004 (Fly model)

**Goal**: Implement FlyEntity.java with AI behaviors

**Requirements**:

1. **Create FlyEntity.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/FlyEntity.java`
   - Extends `FlyingAnimalEntity`
   - Size: 0.3 x 0.2 x 0.3 blocks
   - Health: 2 HP (1 heart)
   - Speed: 0.3 (flying speed)

2. **Implement AI Goals** (4 goals):
   - Priority 0: `WanderInAirGoal` - Fly randomly in 3D space
   - Priority 1: `LandOnBlockGoal` - Occasionally land on blocks (rest 2-10 sec)
   - Priority 2: `FleeEntityGoal<PlayerEntity>` - Flee from players within 4 blocks
   - Priority 3: `LookAroundGoal` - Look at nearby entities when landed

3. **Implement GeckoLib Integration**:
   - Implement `GeoEntity` interface
   - Override animation controller methods
   - Animation logic:
     - Play "fly" when in air and moving
     - Play "idle" when landed
     - Play "death" on death

4. **Implement Attributes**:
   - Max health: 2.0
   - Flying speed: 0.3
   - Follow range: 8.0

5. **Code Standards**:
   - Comprehensive Javadoc
   - Null safety
   - Follow coding-standards skill

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/FlyEntity.java`

**Acceptance Criteria**:
- ✅ FlyEntity.java created
- ✅ Extends FlyingAnimalEntity
- ✅ 4 AI goals implemented with correct priorities
- ✅ GeckoLib integration complete
- ✅ Attributes set correctly
- ✅ No compilation errors

**References**:
- Requirements: Game Mechanics (Fly - AI Goals)
- Epic 01: BaseAnimalEntity patterns

---

### TASK-006: Create Fly Model & Renderer Classes

**Agent**: implementation-agent
**Estimated Time**: 2-3 hours
**Priority**: Critical
**Dependencies**: TASK-005 (FlyEntity)

**Goal**: Create GeckoLib model and renderer classes for Fly

**Requirements**:

1. **Create FlyModel.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/FlyModel.java`
   - Extends `GeoModel<FlyEntity>`
   - Override methods:
     - `getModelResource()` → `xeenaa-alexs-mobs:geo/fly.geo.json`
     - `getTextureResource()` → `xeenaa-alexs-mobs:textures/entity/fly.png`
     - `getAnimationResource()` → `xeenaa-alexs-mobs:animations/fly.animation.json`

2. **Create FlyRenderer.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/FlyRenderer.java`
   - Extends `GeoEntityRenderer<FlyEntity>`
   - Constructor: Register model
   - Override `getRenderType()` if needed (for transparency)

3. **Register Renderer**:
   - Update `AlexsMobsClient.java`:
     - Add renderer registration in `onInitializeClient()`:
       ```java
       EntityRendererRegistry.register(ModEntities.FLY, FlyRenderer::new);
       ```

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/FlyModel.java`
- `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/FlyRenderer.java`
- Updated `AlexsMobsClient.java`

**Acceptance Criteria**:
- ✅ FlyModel.java created
- ✅ FlyRenderer.java created
- ✅ Renderer registered in AlexsMobsClient
- ✅ No compilation errors

**References**:
- Epic 01: TestAnimalModel and TestAnimalRenderer patterns

---

### TASK-007: Register Fly Entity, Add Sounds & Loot

**Agent**: implementation-agent
**Estimated Time**: 4-6 hours
**Priority**: Critical
**Dependencies**: TASK-005 (FlyEntity), TASK-006 (FlyRenderer)

**Goal**: Register Fly entity and add sounds/loot table

**Requirements**:

1. **Register Entity in ModEntities.java**:
   - Add entity type:
     ```java
     public static final EntityType<FlyEntity> FLY = Registry.register(
         Registries.ENTITY_TYPE,
         Identifier.of("xeenaa-alexs-mobs", "fly"),
         EntityType.Builder.create(FlyEntity::new, SpawnGroup.CREATURE)
             .dimensions(0.3f, 0.2f)
             .maxTrackingRange(8)
             .build()
     );
     ```
   - Add to entity list for registration

2. **Create Spawn Egg in ModItems.java**:
   - Add spawn egg item:
     ```java
     public static final Item FLY_SPAWN_EGG = Registry.register(
         Registries.ITEM,
         Identifier.of("xeenaa-alexs-mobs", "fly_spawn_egg"),
         new SpawnEggItem(ModEntities.FLY, 0x404040, 0x8B0000, new Item.Settings())
     );
     ```
   - Add to creative tab

3. **Add Sounds**:
   - Source 3 royalty-free sounds (Freesound.org):
     - `fly_ambient.ogg` (buzzing sound, ~1-2 sec)
     - `fly_hurt.ogg` (squish sound, <1 sec)
     - `fly_death.ogg` (squish sound, <1 sec)
   - Place in `assets/xeenaa-alexs-mobs/sounds/entity/fly/`
   - Register in `ModSounds.java`:
     ```java
     public static final SoundEvent FLY_AMBIENT = registerSound("entity.fly.ambient");
     public static final SoundEvent FLY_HURT = registerSound("entity.fly.hurt");
     public static final SoundEvent FLY_DEATH = registerSound("entity.fly.death");
     ```
   - Update `sounds.json`:
     ```json
     {
       "entity.fly.ambient": {
         "sounds": ["xeenaa-alexs-mobs:entity/fly/fly_ambient"]
       },
       "entity.fly.hurt": {
         "sounds": ["xeenaa-alexs-mobs:entity/fly/fly_hurt"]
       },
       "entity.fly.death": {
         "sounds": ["xeenaa-alexs-mobs:entity/fly/fly_death"]
       }
     }
     ```
   - Hook sounds in FlyEntity:
     - Override `getAmbientSound()` → FLY_AMBIENT
     - Override `getHurtSound()` → FLY_HURT
     - Override `getDeathSound()` → FLY_DEATH

4. **Create Loot Table**:
   - Create `data/xeenaa-alexs-mobs/loot_tables/entities/fly.json`:
     ```json
     {
       "type": "minecraft:entity",
       "pools": [
         {
           "rolls": 1,
           "entries": [
             {
               "type": "minecraft:item",
               "name": "minecraft:string",
               "functions": [
                 {
                   "function": "minecraft:set_count",
                   "count": 1
                 }
               ],
               "conditions": [
                 {
                   "condition": "minecraft:random_chance",
                   "chance": 0.1
                 }
               ]
             }
           ]
         }
       ]
     }
     ```
   - 10% chance to drop 1 string

5. **Translation**:
   - Add to `assets/xeenaa-alexs-mobs/lang/en_us.json`:
     ```json
     {
       "entity.xeenaa-alexs-mobs.fly": "Fly",
       "item.xeenaa-alexs-mobs.fly_spawn_egg": "Fly Spawn Egg"
     }
     ```

**Deliverable**:
- Updated `ModEntities.java`
- Updated `ModItems.java`
- Updated `ModSounds.java`
- 3 sound files in `assets/xeenaa-alexs-mobs/sounds/entity/fly/`
- Updated `sounds.json`
- `data/xeenaa-alexs-mobs/loot_tables/entities/fly.json`
- Updated `en_us.json`

**Acceptance Criteria**:
- ✅ Fly entity registered in ModEntities
- ✅ Fly spawn egg created and registered
- ✅ 3 sounds sourced and registered
- ✅ Loot table created (10% string drop)
- ✅ Translations added
- ✅ No compilation errors
- ✅ Build succeeds (`./gradlew build`)

**References**:
- Epic 01: test_animal registration patterns
- Requirements: Game Mechanics (Fly - Loot/Sounds)

---

## Phase 3: Mob 2 - Cockroach (4 tasks)

### TASK-008: Create Cockroach Model & Animations in Blockbench

**Agent**: implementation-agent
**Estimated Time**: 12-18 hours
**Priority**: High
**Dependencies**: TASK-001 (Blockbench setup)

**Goal**: Create GeckoLib model and animations for Cockroach in Blockbench

**Requirements**:

1. **Research**:
   - Watch Alex's Mobs Cockroach footage
   - Study real cockroach anatomy
   - Keep SIMPLE (ultra-simple mob)

2. **Create Model** (use insect_template.bbmodel as base):
   - **Body parts**:
     - Body: 2 cubes (head 3x2x2, abdomen 4x2x3)
     - Legs: 6 cubes (1x2x1 pixels each, 3 pairs)
     - Antennae: 2 small cubes (1x3x1 pixels)
   - **Total**: ~10 cubes
   - **Size**: Tiny (0.4 x 0.2 x 0.4 blocks in-game)
   - **Bone hierarchy**: root → body → head, abdomen, legs, antennae
   - **Pivot points**: Set for scuttling animation

3. **Create Animations**:
   - **idle** (2 second loop):
     - Antennae twitch
     - Slight body sway
   - **walk** (0.5 second loop):
     - Legs scuttle rapidly (fast movement)
     - Body low to ground
   - **death** (1 second play-once):
     - Flip on back (rotate 180°)
     - Legs curl inward

4. **Create Texture**:
   - Resolution: 16x16 or 32x32
   - Colors: Brown cockroach
   - Save as `cockroach.png`

5. **Export**:
   - Export model: `cockroach.geo.json` → `assets/xeenaa-alexs-mobs/geo/`
   - Export animations: `cockroach.animation.json` → `assets/xeenaa-alexs-mobs/animations/`
   - Export texture: `cockroach.png` → `assets/xeenaa-alexs-mobs/textures/entity/`

6. **Track Time**:
   - Record hours in EFFORT_TRACKING.md

**Deliverable**:
- `assets/xeenaa-alexs-mobs/geo/cockroach.geo.json`
- `assets/xeenaa-alexs-mobs/animations/cockroach.animation.json`
- `assets/xeenaa-alexs-mobs/textures/entity/cockroach.png`
- Time tracking entry

**Acceptance Criteria**:
- ✅ Cockroach model created (~10 cubes)
- ✅ 3 animations created (idle, walk, death)
- ✅ Texture created
- ✅ All files exported to correct locations
- ✅ Valid JSON
- ✅ Time tracked

**References**:
- Requirements: Game Mechanics (Cockroach behavior)
- TASK-004 patterns (Fly model creation)

---

### TASK-009: Implement Cockroach Entity Class & AI

**Agent**: implementation-agent
**Estimated Time**: 6-10 hours
**Priority**: High
**Dependencies**: TASK-003 (AI goals), TASK-008 (Cockroach model)

**Goal**: Implement CockroachEntity.java with AI behaviors

**Requirements**:

1. **Create CockroachEntity.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CockroachEntity.java`
   - Extends `BaseAnimalEntity` (land mob, not flying)
   - Size: 0.4 x 0.2 x 0.4 blocks
   - Health: 3 HP (1.5 hearts)
   - Speed: 0.35 (fast scuttling)

2. **Implement AI Goals** (3 goals):
   - Priority 0: `WanderAroundGoal` - Scurry in random directions
   - Priority 1: `FleeEntityGoal<PlayerEntity>` - Run from players within 5 blocks (fast speed 0.5)
   - Priority 2: `FleeFromLightGoal` - Prefer dark areas (light level < 7)

3. **Implement GeckoLib Integration**:
   - Animation logic:
     - Play "walk" when moving
     - Play "idle" when stationary
     - Play "death" on death

4. **Implement Attributes**:
   - Max health: 3.0
   - Movement speed: 0.35
   - Follow range: 6.0

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CockroachEntity.java`

**Acceptance Criteria**:
- ✅ CockroachEntity.java created
- ✅ Extends BaseAnimalEntity
- ✅ 3 AI goals implemented
- ✅ GeckoLib integration complete
- ✅ No compilation errors

**References**:
- Requirements: Game Mechanics (Cockroach - AI Goals)
- TASK-005 patterns (FlyEntity)

---

### TASK-010: Create Cockroach Model & Renderer Classes

**Agent**: implementation-agent
**Estimated Time**: 2-3 hours
**Priority**: High
**Dependencies**: TASK-009 (CockroachEntity)

**Goal**: Create GeckoLib model and renderer classes for Cockroach

**Requirements**:

1. **Create CockroachModel.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/CockroachModel.java`
   - Extends `GeoModel<CockroachEntity>`
   - Point to cockroach.geo.json, cockroach.png, cockroach.animation.json

2. **Create CockroachRenderer.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/CockroachRenderer.java`
   - Extends `GeoEntityRenderer<CockroachEntity>`

3. **Register Renderer**:
   - Update `AlexsMobsClient.java`

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/CockroachModel.java`
- `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/CockroachRenderer.java`
- Updated `AlexsMobsClient.java`

**Acceptance Criteria**:
- ✅ CockroachModel.java created
- ✅ CockroachRenderer.java created
- ✅ Renderer registered
- ✅ No compilation errors

**References**:
- TASK-006 patterns (FlyModel/Renderer)

---

### TASK-011: Register Cockroach Entity, Add Sounds & Loot

**Agent**: implementation-agent
**Estimated Time**: 3-5 hours
**Priority**: High
**Dependencies**: TASK-009 (CockroachEntity), TASK-010 (CockroachRenderer)

**Goal**: Register Cockroach entity and add sounds/loot table

**Requirements**:

1. **Register Entity in ModEntities.java**:
   - Add COCKROACH entity type (0.4f x 0.2f dimensions)

2. **Create Spawn Egg in ModItems.java**:
   - Colors: 0x5C4033 (brown), 0x2B1B0F (dark brown)

3. **Add Sounds**:
   - Source 2 sounds:
     - `cockroach_scuttle.ogg` (quiet scuttling, ambient)
     - `cockroach_squish.ogg` (death sound)
   - Place in `assets/xeenaa-alexs-mobs/sounds/entity/cockroach/`
   - Register in ModSounds.java
   - Update sounds.json
   - Hook in CockroachEntity

4. **Create Loot Table**:
   - `data/xeenaa-alexs-mobs/loot_tables/entities/cockroach.json`
   - No drops (too small)

5. **Translation**:
   - Add to en_us.json

**Deliverable**:
- Updated ModEntities.java
- Updated ModItems.java
- Updated ModSounds.java
- 2 sound files
- Updated sounds.json
- Loot table (empty)
- Updated en_us.json

**Acceptance Criteria**:
- ✅ Cockroach entity registered
- ✅ Spawn egg created
- ✅ 2 sounds added
- ✅ Loot table created
- ✅ Translations added
- ✅ Build succeeds

**References**:
- TASK-007 patterns (Fly registration)

---

## Phase 4: Mob 3 - Triops (4 tasks)

### TASK-012: Create Triops Model & Animations in Blockbench

**Agent**: implementation-agent
**Estimated Time**: 12-18 hours
**Priority**: High
**Dependencies**: TASK-001 (Blockbench setup)

**Goal**: Create GeckoLib model and animations for Triops in Blockbench

**Requirements**:

1. **Research**:
   - Watch Alex's Mobs Triops footage
   - Study prehistoric shrimp anatomy
   - Keep SIMPLE

2. **Create Model** (use fish_template.bbmodel as base):
   - **Body parts**:
     - Head: 1 cube (3x2x3)
     - Body: 2 cubes (segments)
     - Tail: 1 cube (2x1x4 fan-shaped)
     - Legs/swimmerets: 6 small cubes (underneath)
   - **Total**: ~10 cubes
   - **Size**: Small (0.5 x 0.3 x 0.5 blocks in-game)
   - **Bone hierarchy**: root → body → head, segments, tail, legs

3. **Create Animations**:
   - **idle** (2 second loop):
     - Gentle wiggle
     - Legs paddle slowly
   - **swim** (1 second loop):
     - Tail undulates
     - Legs paddle rapidly
     - Body slight S-curve
   - **death** (1.5 second play-once):
     - Float to surface
     - Flip on back

4. **Create Texture**:
   - Resolution: 16x16 or 32x32
   - Colors: Tan/brown prehistoric shrimp
   - Save as `triops.png`

5. **Export**:
   - Export model: `triops.geo.json`
   - Export animations: `triops.animation.json`
   - Export texture: `triops.png`

6. **Track Time**:
   - Record hours in EFFORT_TRACKING.md

**Deliverable**:
- `assets/xeenaa-alexs-mobs/geo/triops.geo.json`
- `assets/xeenaa-alexs-mobs/animations/triops.animation.json`
- `assets/xeenaa-alexs-mobs/textures/entity/triops.png`
- Time tracking entry

**Acceptance Criteria**:
- ✅ Triops model created (~10 cubes)
- ✅ 3 animations created
- ✅ Texture created
- ✅ All files exported
- ✅ Valid JSON
- ✅ Time tracked

**References**:
- Requirements: Game Mechanics (Triops behavior)

---

### TASK-013: Implement Triops Entity Class & AI

**Agent**: implementation-agent
**Estimated Time**: 6-10 hours
**Priority**: High
**Dependencies**: TASK-002 (AquaticAnimalEntity), TASK-003 (AI goals), TASK-012 (Triops model)

**Goal**: Implement TriopsEntity.java with AI behaviors

**Requirements**:

1. **Create TriopsEntity.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/TriopsEntity.java`
   - Extends `AquaticAnimalEntity`
   - Size: 0.5 x 0.3 x 0.5 blocks
   - Health: 4 HP (2 hearts)
   - Speed: 0.4 (swimming)

2. **Implement AI Goals** (3 goals):
   - Priority 0: `SwimAroundGoal` - Swim in random directions (like fish)
   - Priority 1: `BurrowInBlockGoal` - Burrow in sand/gravel when idle
   - Priority 2: `FleeEntityGoal<PlayerEntity>` - Flee from players within 3 blocks

3. **Implement GeckoLib Integration**:
   - Animation logic:
     - Play "swim" when moving in water
     - Play "idle" when stationary
     - Play "death" on death

4. **Implement Attributes**:
   - Max health: 4.0
   - Movement speed: 0.4
   - Follow range: 6.0

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/TriopsEntity.java`

**Acceptance Criteria**:
- ✅ TriopsEntity.java created
- ✅ Extends AquaticAnimalEntity
- ✅ 3 AI goals implemented
- ✅ GeckoLib integration complete
- ✅ No compilation errors

**References**:
- Requirements: Game Mechanics (Triops - AI Goals)

---

### TASK-014: Create Triops Model & Renderer Classes

**Agent**: implementation-agent
**Estimated Time**: 2-3 hours
**Priority**: High
**Dependencies**: TASK-013 (TriopsEntity)

**Goal**: Create GeckoLib model and renderer classes for Triops

**Requirements**:

1. **Create TriopsModel.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/TriopsModel.java`
   - Extends `GeoModel<TriopsEntity>`

2. **Create TriopsRenderer.java**:
   - Location: `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/TriopsRenderer.java`
   - Extends `GeoEntityRenderer<TriopsEntity>`

3. **Register Renderer**:
   - Update `AlexsMobsClient.java`

**Deliverable**:
- `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/TriopsModel.java`
- `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/TriopsRenderer.java`
- Updated `AlexsMobsClient.java`

**Acceptance Criteria**:
- ✅ TriopsModel.java created
- ✅ TriopsRenderer.java created
- ✅ Renderer registered
- ✅ No compilation errors

**References**:
- TASK-006 patterns

---

### TASK-015: Register Triops Entity, Add Sounds & Loot

**Agent**: implementation-agent
**Estimated Time**: 3-5 hours
**Priority**: High
**Dependencies**: TASK-013 (TriopsEntity), TASK-014 (TriopsRenderer)

**Goal**: Register Triops entity and add sounds/loot table

**Requirements**:

1. **Register Entity in ModEntities.java**:
   - Add TRIOPS entity type (0.5f x 0.3f dimensions)
   - SpawnGroup: WATER_CREATURE

2. **Create Spawn Egg in ModItems.java**:
   - Colors: 0xC2B280 (tan), 0x8B7355 (brown)

3. **Add Sounds**:
   - Source 2 sounds:
     - `triops_splash.ogg` (ambient/swimming)
     - `triops_hurt.ogg` (hurt sound)
   - Place in `assets/xeenaa-alexs-mobs/sounds/entity/triops/`
   - Register in ModSounds.java
   - Update sounds.json
   - Hook in TriopsEntity

4. **Create Loot Table**:
   - `data/xeenaa-alexs-mobs/loot_tables/entities/triops.json`
   - 30% chance to drop 1 raw cod

5. **Translation**:
   - Add to en_us.json

**Deliverable**:
- Updated ModEntities.java
- Updated ModItems.java
- Updated ModSounds.java
- 2 sound files
- Updated sounds.json
- Loot table (30% raw cod)
- Updated en_us.json

**Acceptance Criteria**:
- ✅ Triops entity registered
- ✅ Spawn egg created
- ✅ 2 sounds added
- ✅ Loot table created
- ✅ Translations added
- ✅ Build succeeds

**References**:
- TASK-007 patterns

---

## Phase 5: Validation & Documentation (3 tasks)

### TASK-016: Manual Testing & Validation

**Agent**: implementation-agent (with user validation)
**Estimated Time**: 6-10 hours
**Priority**: Critical
**Dependencies**: TASK-007 (Fly complete), TASK-011 (Cockroach complete), TASK-015 (Triops complete)

**Goal**: Test all 3 mobs in-game and validate they work correctly

**Requirements**:

1. **Build and Launch**:
   - Run `./gradlew build` (ensure build succeeds)
   - Run `./gradlew runClient` (launch Minecraft)

2. **Test Each Mob** (Fly, Cockroach, Triops):
   - **Spawn Testing**:
     - [ ] Spawn via spawn egg (creative mode)
     - [ ] Mob appears at correct size
     - [ ] Texture renders correctly (no missing texture)
     - [ ] No crashes on spawn
   - **AI Testing**:
     - [ ] Mob wanders naturally (not stuck)
     - [ ] Mob flees from player when approached
     - [ ] Special behaviors work (flying, burrowing, swimming)
     - [ ] Pathfinding works (doesn't get stuck in walls)
   - **Animation Testing**:
     - [ ] Idle animation plays when stationary
     - [ ] Movement animation plays when moving
     - [ ] Death animation plays on death
     - [ ] Animations loop correctly
     - [ ] Animation speed matches movement
   - **Sound Testing**:
     - [ ] Ambient sound plays occasionally
     - [ ] Hurt sound plays when damaged
     - [ ] Death sound plays on death
     - [ ] Volume is appropriate
   - **Loot Testing**:
     - [ ] Loot drops on death (if applicable)
     - [ ] Loot matches loot table
   - **Performance Testing**:
     - [ ] Spawn 20 mobs - no FPS drop
     - [ ] Spawn 50 mobs - acceptable performance (30+ FPS)
     - [ ] No console errors/warnings

3. **Document Issues**:
   - Create `docs/epic-03/TESTING_RESULTS.md`
   - List all bugs found
   - List all issues to fix

4. **Fix Critical Bugs**:
   - Fix any crashes or critical issues
   - Re-test after fixes
   - Document fixes

**Deliverable**:
- `docs/epic-03/TESTING_RESULTS.md`
- Bug fixes (if needed)

**Acceptance Criteria**:
- ✅ All 3 mobs tested against checklist
- ✅ All mobs work correctly (spawn, AI, animations, sounds, loot)
- ✅ No critical bugs
- ✅ Performance acceptable
- ✅ Testing results documented

**References**:
- Requirements: Testing Strategy (Manual Testing Checklist)

**User Validation Required**: User must manually test all 3 mobs in-game before marking this task complete.

---

### TASK-017: Create Documentation & Lessons Learned

**Agent**: implementation-agent
**Estimated Time**: 4-6 hours
**Priority**: High
**Dependencies**: TASK-016 (Testing complete)

**Goal**: Create comprehensive documentation for Epic 03

**Requirements**:

1. **Create PORTING_WORKFLOW.md**:
   - Location: `docs/epic-03/PORTING_WORKFLOW.md`
   - Content:
     - Step-by-step guide to port next animal
     - Research phase (watch videos, study anatomy)
     - Modeling phase (Blockbench creation)
     - Animation phase (create animations)
     - Coding phase (entity class, AI, renderer)
     - Registration phase (ModEntities, sounds, loot)
     - Testing phase (manual testing checklist)
   - Include: Time estimates per phase
   - Include: Common pitfalls and how to avoid

2. **Create BLOCKBENCH_GUIDE.md**:
   - Location: `docs/epic-03/BLOCKBENCH_GUIDE.md`
   - Content:
     - How to create GeckoLib models (with screenshots)
     - How to create animations
     - How to export .geo.json and .animation.json
     - Troubleshooting common issues
     - Best practices (bone hierarchy, pivot points, animation loops)

3. **Create LESSONS_LEARNED.md**:
   - Location: `docs/epic-03/LESSONS_LEARNED.md`
   - Content:
     - What worked well (successes)
     - What didn't work (failures)
     - Unexpected challenges
     - GeckoLib limitations discovered
     - Asset creation insights
     - Code patterns that worked
     - Improvements for Epic 04+

4. **Update EFFORT_TRACKING.md**:
   - Location: `docs/epic-03/EFFORT_TRACKING.md`
   - Content:
     - Final effort summary table (see requirements.md)
     - Per-mob breakdown (model, animations, code, testing hours)
     - Total hours vs. Epic 02 estimates
     - Variance analysis (+/- %)
     - Adjusted estimates for Epic 04

**Deliverable**:
- `docs/epic-03/PORTING_WORKFLOW.md`
- `docs/epic-03/BLOCKBENCH_GUIDE.md`
- `docs/epic-03/LESSONS_LEARNED.md`
- `docs/epic-03/EFFORT_TRACKING.md`

**Acceptance Criteria**:
- ✅ All 4 documentation files created
- ✅ PORTING_WORKFLOW.md is actionable (can port next mob following it)
- ✅ BLOCKBENCH_GUIDE.md has screenshots/examples
- ✅ LESSONS_LEARNED.md is honest and detailed
- ✅ EFFORT_TRACKING.md has final hours with variance analysis

**References**:
- Requirements: Deliverable #4 (Documentation)
- Requirements: Deliverable #5 (Effort Tracking)

---

### TASK-018: Final Validation & Epic Completion

**Agent**: implementation-agent (with user final approval)
**Estimated Time**: 2-3 hours
**Priority**: Critical
**Dependencies**: All previous tasks (TASK-001 to TASK-017)

**Goal**: Final validation that all success criteria are met and epic is complete

**Requirements**:

1. **Success Criteria Review**:
   - [ ] ✅ **All 3 animals fully functional** (Fly, Cockroach, Triops work in-game)
   - [ ] ✅ **GeckoLib validation passed** (all 3 movement types work - flying, land, aquatic)
   - [ ] ✅ **Reusable templates created**:
     - [ ] Blockbench templates (insect_template.bbmodel, fish_template.bbmodel)
     - [ ] Base entity classes (FlyingAnimalEntity.java, AquaticAnimalEntity.java)
     - [ ] AI goal templates (WanderInAirGoal, FleeFromLightGoal, BurrowInBlockGoal)
   - [ ] ✅ **Effort tracking complete** (EFFORT_TRACKING.md finalized)
   - [ ] ✅ **Documentation created** (all 4 docs exist and are comprehensive)

2. **Code Quality Check**:
   - [ ] All code has comprehensive Javadoc
   - [ ] No compilation warnings
   - [ ] Build succeeds (`./gradlew build`)
   - [ ] No critical bugs

3. **Performance Validation**:
   - [ ] Spawn 20 mobs - no performance issues
   - [ ] Check F3 debug - entity tick < 0.05ms per mob
   - [ ] Render performance acceptable

4. **Update Epic Tracking**:
   - Update `CURRENT_EPIC.md`:
     - Change Epic 03 status to "COMPLETE"
     - Update progress to 18/18 tasks
     - Add completion date
     - Update deliverables section

5. **Create Epic Summary**:
   - Create `docs/epic-03/EPIC_SUMMARY.md` with:
     - Total hours spent (actual)
     - Total hours estimated (120-180h)
     - Variance analysis
     - Key achievements
     - Blockers discovered (if any)
     - Recommendations for Epic 04

**Deliverable**:
- Updated `CURRENT_EPIC.md`
- `docs/epic-03/EPIC_SUMMARY.md`
- Final validation checklist completed

**Acceptance Criteria**:
- ✅ All success criteria met
- ✅ All 18 tasks marked COMPLETED
- ✅ Epic tracking updated
- ✅ Epic summary created
- ✅ User approves epic completion

**User Validation Required**: User must review and approve that epic is complete before marking TASK-018 done.

**References**:
- Requirements: Success Criteria

---

## Task Execution Order

**Critical Path** (must be done in order):
1. TASK-001 (Blockbench setup) → Blocks all model tasks
2. TASK-002 (Base classes) → TASK-003 (AI goals) → All entity tasks
3. TASK-004 (Fly model) → TASK-005 (Fly entity) → TASK-006 (Fly renderer) → TASK-007 (Fly registration)
4. TASK-008 (Cockroach model) → TASK-009 (Cockroach entity) → TASK-010 (Cockroach renderer) → TASK-011 (Cockroach registration)
5. TASK-012 (Triops model) → TASK-013 (Triops entity) → TASK-014 (Triops renderer) → TASK-015 (Triops registration)
6. TASK-007, TASK-011, TASK-015 → TASK-016 (Testing)
7. TASK-016 → TASK-017 (Documentation) → TASK-018 (Final validation)

**Recommended Order**:
- Week 1: TASK-001 to TASK-007 (Foundation + Fly complete)
- Week 2: TASK-008 to TASK-015 (Cockroach + Triops complete)
- Week 2: TASK-016 to TASK-018 (Testing + Documentation + Validation)

**Parallel Opportunities**:
- TASK-004 (Fly model) and TASK-002 (Base classes) can be done in parallel
- TASK-008 (Cockroach model) can start while TASK-005 to TASK-007 are in progress
- TASK-012 (Triops model) can start while TASK-009 to TASK-011 are in progress

---

## Success Criteria (Epic Completion)

This epic is **COMPLETE** when:

- ✅ All 18 tasks marked COMPLETED
- ✅ All 3 animals fully functional (Fly, Cockroach, Triops work in-game)
- ✅ GeckoLib validation passed (all 3 movement types work)
- ✅ Reusable templates created and documented
- ✅ Effort tracking finalized with variance analysis
- ✅ All documentation created (4 docs)
- ✅ User manually tests and approves all 3 mobs
- ✅ No critical bugs or blockers
- ✅ Build succeeds and mod loads without errors

---

## Risk Mitigation

**If GeckoLib blockers found**:
- Document the blocker in LESSONS_LEARNED.md
- Research alternatives (Animated Java, custom system)
- Consult with user on pivot strategy
- Mark epic as "BLOCKED" and create research task

**If effort exceeds estimates significantly (>2x)**:
- Update EFFORT_TRACKING.md immediately
- Adjust Epic 04+ estimates in porting roadmap
- Consider recruiting asset creators
- Discuss scope reduction with user

**If performance issues found**:
- Profile using F3 debug and profiler
- Optimize AI goals (reduce tick frequency)
- Simplify models/animations if needed
- Document optimizations in LESSONS_LEARNED.md

---

## Deliverables Summary

**Location**: `xeenaa-alexs-mobs/`

### Code (27 new files):
1. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/FlyingAnimalEntity.java`
2. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/base/AquaticAnimalEntity.java`
3. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/WanderInAirGoal.java`
4. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/FleeFromLightGoal.java`
5. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/BurrowInBlockGoal.java`
6. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/FlyEntity.java`
7. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CockroachEntity.java`
8. `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/TriopsEntity.java`
9. `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/FlyModel.java`
10. `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/CockroachModel.java`
11. `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/TriopsModel.java`
12. `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/FlyRenderer.java`
13. `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/CockroachRenderer.java`
14. `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/TriopsRenderer.java`
15. Updated: `ModEntities.java`, `ModItems.java`, `ModSounds.java`, `AlexsMobsClient.java`

### Assets (15 new files):
16-18. 3 `.geo.json` models (fly, cockroach, triops)
19-21. 3 `.animation.json` files
22-24. 3 `.png` textures
25-31. 7 `.ogg` sound files (3 fly + 2 cockroach + 2 triops)
32-34. 3 loot tables
35. Updated `sounds.json`
36. Updated `en_us.json`

### Templates (2 files):
37. `docs/blockbench-templates/insect_template.bbmodel`
38. `docs/blockbench-templates/fish_template.bbmodel`

### Documentation (6 files):
39. `docs/epic-03/BLOCKBENCH_SETUP.md`
40. `docs/epic-03/PORTING_WORKFLOW.md`
41. `docs/epic-03/BLOCKBENCH_GUIDE.md`
42. `docs/epic-03/LESSONS_LEARNED.md`
43. `docs/epic-03/EFFORT_TRACKING.md`
44. `docs/epic-03/EPIC_SUMMARY.md`
45. `docs/epic-03/TESTING_RESULTS.md`

**Total**: 45 files created/updated

---

## Next Epic

After completing this epic, proceed to:

**Epic 04: Simple Passive Land & Flying Animals** (8 mobs)
- **Mobs**: Blue Jay, Hummingbird, Toucan, Banana Slug, Jerboa, Mudskipper, Potoo, Shoebill
- **Goal**: Build on Epic 03 templates to port 8 simple animals efficiently
- **Effort**: S-M (200-240 hours, 2-3 weeks)
- **Templates**: Use FlyingAnimalEntity, insect_template, bird_template from Epic 03

---

**Ready to start?** Run `/next` to begin with TASK-001!
