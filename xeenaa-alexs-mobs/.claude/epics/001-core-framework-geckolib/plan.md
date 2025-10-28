# Epic 01: Core Framework & GeckoLib Integration - Implementation Plan

**Epic**: 01-core-framework-geckolib
**Status**: READY
**Created**: 2025-10-25
**Total Tasks**: 12
**Estimated Effort**: 15-20 hours

---

## Task Overview

| Phase | Tasks | Status |
|-------|-------|--------|
| Foundation | TASK-001 to TASK-003 | COMPLETED |
| Implementation | TASK-004 to TASK-008 | TODO |
| Integration | TASK-009 to TASK-010 | TODO |
| Validation | TASK-011 to TASK-012 | TODO |

---

## Phase 1: Foundation (3 tasks)

### TASK-001: [COMPLETED] Add GeckoLib Dependency

**Agent**: implementation-agent
**Estimated Time**: 30 minutes
**Priority**: Critical (blocks all other tasks)
**Dependencies**: None

**Goal**: Configure build.gradle to include GeckoLib 5.0+ for Fabric 1.21.x

**Requirements**:
1. Add GeckoLib to dependencies in build.gradle:
   - `modImplementation "software.bernie.geckolib:geckolib-fabric-1.21:5.0+"`
   - `include "software.bernie.geckolib:geckolib-fabric-1.21:5.0+"` (JiJ - Jar in Jar)
2. Refresh Gradle dependencies
3. Verify no version conflicts
4. Build project successfully

**Acceptance Criteria**:
- Gradle build succeeds without errors
- GeckoLib appears in dependency tree
- No version conflict warnings in console
- Project compiles cleanly

**References**:
- Research: `.claude/research/alexs-mobs-porting-strategy.md` (GeckoLib section)
- GeckoLib Modrinth: https://modrinth.com/mod/geckolib

**Notes**:
- Use "include" directive to bundle GeckoLib (users won't need separate download)
- Version 5.0+ confirmed to support Fabric 1.21.x
- Check for latest 5.x version on Modrinth

---

### TASK-002: [COMPLETED] Create Entity Registration System

**Agent**: implementation-agent
**Estimated Time**: 2 hours
**Priority**: Critical
**Dependencies**: TASK-001 (GeckoLib dependency)

**Goal**: Create ModEntities registry class with Fabric registration patterns

**Requirements**:
1. Create `registry/ModEntities.java` class
2. Implement static registration helper method:
   ```java
   private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type)
   ```
3. Add `public static void initialize()` method for attribute registration
4. Call `ModEntities.initialize()` from main mod initializer
5. Add comprehensive Javadoc explaining registration pattern

**Implementation Pattern**:
```java
public class ModEntities {
    // Example entity type (will be replaced with test entity in TASK-004)
    public static final EntityType<TestAnimalEntity> TEST_ANIMAL = register(
        "test_animal",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TestAnimalEntity::new)
            .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
            .build()
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("xeenaa-alexs-mobs", name), type);
    }

    public static void initialize() {
        // Register entity attributes
        FabricDefaultAttributeRegistry.register(TEST_ANIMAL,
            TestAnimalEntity.createAttributes());
    }
}
```

**Acceptance Criteria**:
- ModEntities class exists in registry/ package
- Registration pattern is consistent and documented
- initialize() method called from main mod class
- Test entity (from TASK-004) registers without errors
- Code follows coding-standards skill

**References**:
- Research: `.claude/research/alexs-mobs-porting-strategy.md` (Entity Registration Pattern)
- Fabric Wiki: https://wiki.fabricmc.net/tutorial:entity

---

### TASK-003: [COMPLETED] Set Up Client Renderer Registration

**Agent**: implementation-agent
**Estimated Time**: 1.5 hours
**Priority**: Critical
**Dependencies**: TASK-001 (GeckoLib)

**Goal**: Create client initialization system for entity renderer registration

**Requirements**:
1. Verify `client/AlexsMobsClient.java` exists (created during project setup)
2. Implement `onInitializeClient()` method
3. Add `@Environment(EnvType.CLIENT)` annotation
4. Create helper method for batch renderer registration
5. Document renderer registration pattern

**Implementation Pattern**:
```java
@Environment(EnvType.CLIENT)
public class AlexsMobsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("xeenaa-alexs-mobs-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Alex's Mobs client");
        registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        // Example (will add test entity in TASK-006)
        EntityRendererRegistry.register(ModEntities.TEST_ANIMAL, TestAnimalRenderer::new);
    }
}
```

**Acceptance Criteria**:
- Client initializer properly annotated with @Environment
- Renderer registration method exists
- Test entity renderer (from TASK-006) registers successfully
- No "missing renderer" warnings in logs
- Code follows client-server separation pattern

**References**:
- Research: `.claude/research/alexs-mobs-porting-strategy.md` (Client Rendering Registration)
- Fabric API: EntityRendererRegistry documentation

---

## Phase 2: Implementation (5 tasks)

### TASK-004: [COMPLETED] Create Base Animal Entity Class

**Agent**: implementation-agent
**Estimated Time**: 3 hours
**Priority**: High
**Dependencies**: TASK-002 (Entity registration system)

**Goal**: Create abstract BaseAnimalEntity class with common functionality for all animals

**Requirements**:
1. Create `entity/BaseAnimalEntity.java` extending `AnimalEntity`
2. Implement abstract methods for customization:
   - `getSoundEvent()` for custom sounds
   - `createChild()` for breeding
3. Provide default `initGoals()` with common AI:
   - SwimGoal (priority 0)
   - EscapeDangerGoal (priority 1)
   - AnimalMateGoal (priority 2)
   - WanderAroundFarGoal (priority 5)
   - LookAtEntityGoal (priority 6)
   - LookAroundGoal (priority 7)
4. Add helper methods for common patterns
5. Comprehensive Javadoc with usage examples

**Implementation Structure**:
```java
/**
 * Base class for all animal entities in Alex's Mobs port.
 * Provides common AI goals, sound handling, and breeding interface.
 */
public abstract class BaseAnimalEntity extends AnimalEntity {

    public BaseAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        // Common AI goals all animals should have
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        // Subclasses can override to add custom goals
        registerCustomGoals();
    }

    /**
     * Override this to add entity-specific AI goals.
     */
    protected void registerCustomGoals() {
        // Override in subclasses
    }

    // Abstract methods for customization
    protected abstract SoundEvent getAmbientSound();
    protected abstract SoundEvent getHurtSound(DamageSource source);
    protected abstract SoundEvent getDeathSound();
}
```

**Acceptance Criteria**:
- BaseAnimalEntity class exists and compiles
- Provides all common AI goals
- Well-documented with Javadoc
- Test entity (TASK-005) successfully extends it
- No code duplication for common functionality

**References**:
- Research: `.claude/research/alexs-mobs-porting-strategy.md` (AI Goals Migration)
- Requirements: Feature 4 (Base Entity Classes)

---

### TASK-005: [COMPLETED] Create Test Animal Entity

**Agent**: implementation-agent
**Estimated Time**: 2 hours
**Priority**: High
**Dependencies**: TASK-004 (Base entity class)

**Goal**: Create a simple test entity to validate the framework

**Requirements**:
1. Create `entity/animal/TestAnimalEntity.java` extending `BaseAnimalEntity`
2. Implement required abstract methods
3. Define entity attributes (health, speed, etc.)
4. Implement GeoEntity interface for GeckoLib support
5. Create placeholder AnimationController
6. Register in ModEntities
7. Create spawn egg in ModItems

**Implementation**:
```java
public class TestAnimalEntity extends BaseAnimalEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TestAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<TestAnimalEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.TEST_ANIMAL.create(world);
    }

    // Sound methods...
}
```

**Acceptance Criteria**:
- Entity compiles and runs
- Can be summoned with /summon xeenaa-alexs-mobs:test_animal
- F3+B shows correct hitbox
- Entity has basic AI (wander, look around)
- Spawn egg works in creative menu

**References**:
- Research: `.claude/research/alexs-mobs-porting-strategy.md` (Complete Entity Example)

---

### TASK-006: [COMPLETED] Create Test Entity Model and Renderer

**Agent**: implementation-agent
**Estimated Time**: 2.5 hours
**Priority**: High
**Dependencies**: TASK-005 (Test entity)

**Goal**: Create GeckoLib model, renderer, and simple test model file

**Requirements**:
1. Create `client/model/TestAnimalModel.java` extending `GeoModel<TestAnimalEntity>`
2. Create `client/renderer/TestAnimalRenderer.java` extending `GeoEntityRenderer`
3. Create simple cube model in Blockbench:
   - Export as .geo.json to `resources/assets/xeenaa-alexs-mobs/geo/test_animal.geo.json`
   - Create basic idle and walk animations
   - Export animations to `resources/assets/xeenaa-alexs-mobs/animations/test_animal.animation.json`
4. Create placeholder texture (simple colored square)
5. Register renderer in AlexsMobsClient

**Model Implementation**:
```java
public class TestAnimalModel extends GeoModel<TestAnimalEntity> {
    @Override
    public Identifier getModelResource(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/test_animal.geo.json");
    }

    @Override
    public Identifier getTextureResource(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal.png");
    }

    @Override
    public Identifier getAnimationResource(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/test_animal.animation.json");
    }
}
```

**Renderer Implementation**:
```java
public class TestAnimalRenderer extends GeoEntityRenderer<TestAnimalEntity> {
    public TestAnimalRenderer(EntityRendererFactory.Context context) {
        super(context, new TestAnimalModel());
    }

    @Override
    public Identifier getTextureLocation(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal.png");
    }
}
```

**Acceptance Criteria**:
- Entity renders in-game with model visible
- Animations play (idle and walk)
- Texture applies correctly (no pink/black checkerboard)
- Shadow renders beneath entity
- No console errors about missing resources
- Entity renders from all camera angles

**References**:
- Requirements: Feature 3 (Client Renderer Registration)
- GeckoLib Wiki: https://github.com/bernie-g/geckolib/wiki

**Notes**:
- Use Blockbench to create simple cube model (1x1x1 block as placeholder)
- Simple texture: 16x16 colored square is sufficient for testing

---

### TASK-007: [COMPLETED] Establish Resource Directory Structure

**Agent**: implementation-agent
**Estimated Time**: 1 hour
**Priority**: Medium
**Dependencies**: TASK-006 (Test entity rendering)

**Goal**: Create organized directory structure for all mod resources

**Requirements**:
1. Create complete directory structure:
```
resources/
├── assets/xeenaa-alexs-mobs/
│   ├── geo/                    # GeckoLib models (.geo.json)
│   ├── animations/             # Animation files (.animation.json)
│   ├── textures/
│   │   ├── entity/            # Entity textures
│   │   └── item/              # Item textures
│   ├── sounds/                # Sound files (.ogg)
│   ├── lang/                  # Translations
│   │   └── en_us.json
│   └── icon.png               # Mod icon
└── data/xeenaa-alexs-mobs/
    ├── loot_table/
    │   └── entities/          # Entity loot tables
    ├── recipe/                # Crafting recipes
    └── tags/                  # Tags
```

2. Create README.md in each major directory explaining what goes there
3. Add test_animal resources to validate structure
4. Document ResourceLocation path conventions

**Acceptance Criteria**:
- All directories exist
- README files explain directory purposes
- Path conventions documented
- Test entity resources load from correct locations
- F3+T resource reload works without errors

**References**:
- Requirements: Feature 5 (Resource Loading System)

---

### TASK-008: [COMPLETED] Create ModItems Registry and Test Spawn Egg

**Agent**: implementation-agent
**Estimated Time**: 1.5 hours
**Priority**: Medium
**Dependencies**: TASK-005 (Test entity)

**Goal**: Create item registration system and spawn egg for test entity

**Requirements**:
1. Create `registry/ModItems.java` class
2. Implement item registration pattern matching entity registration
3. Create spawn egg for test animal:
   - Use vanilla SpawnEggItem
   - Choose egg colors (primary and secondary)
4. Add to creative mode item group
5. Create language file entry (en_us.json)

**Implementation**:
```java
public class ModItems {
    public static final Item TEST_ANIMAL_SPAWN_EGG = register(
        "test_animal_spawn_egg",
        new SpawnEggItem(ModEntities.TEST_ANIMAL, 0x4B5C3A, 0x2A3420,
            new Item.Settings())
    );

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM,
            Identifier.of("xeenaa-alexs-mobs", name), item);
    }

    public static void initialize() {
        // Called from main mod initializer
    }
}
```

**Acceptance Criteria**:
- Spawn egg appears in creative inventory
- Spawn egg spawns test animal when used
- Spawn egg has correct colors
- Translation shows correct name
- Follows same pattern as entity registration

**References**:
- Requirements: Feature 2 (Entity Registration System) - spawn egg creation

---

## Phase 3: Integration (2 tasks)

### TASK-009: [COMPLETED] Create ModSounds Registry and Test Sound

**Agent**: implementation-agent
**Estimated Time**: 1.5 hours
**Priority**: Low
**Dependencies**: TASK-007 (Resource structure)

**Goal**: Create sound registration system and add test sound to test entity

**Requirements**:
1. Create `registry/ModSounds.java` class
2. Register test sound event
3. Add placeholder sound file (can use vanilla sound temporarily)
4. Create sounds.json in assets
5. Update test entity to use registered sound
6. Test sound plays in-game

**Implementation**:
```java
public class ModSounds {
    public static final SoundEvent TEST_ANIMAL_AMBIENT = register("entity.test_animal.ambient");
    public static final SoundEvent TEST_ANIMAL_HURT = register("entity.test_animal.hurt");
    public static final SoundEvent TEST_ANIMAL_DEATH = register("entity.test_animal.death");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of("xeenaa-alexs-mobs", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize() {
        // Called from main mod initializer
    }
}
```

**Acceptance Criteria**:
- Sound events registered without errors
- sounds.json properly formatted
- Test entity plays sounds when idle, hurt, killed
- No "missing sound" warnings in logs

**References**:
- Requirements: Feature 5 (Resource Loading System) - sounds

---

### TASK-010: [COMPLETED] Create Test Loot Table

**Agent**: implementation-agent
**Estimated Time**: 45 minutes
**Priority**: Low
**Dependencies**: TASK-008 (Items registry)

**Goal**: Create loot table for test entity

**Requirements**:
1. Create loot table JSON: `data/xeenaa-alexs-mobs/loot_table/entities/test_animal.json`
2. Add simple drop (e.g., 1-2 leather)
3. Validate JSON format
4. Test entity drops items when killed

**Implementation** (test_animal.json):
```json
{
  "type": "minecraft:entity",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "minecraft:leather",
          "functions": [
            {
              "function": "minecraft:set_count",
              "count": {
                "min": 1,
                "max": 2
              }
            },
            {
              "function": "minecraft:looting_enchant",
              "count": {
                "min": 0,
                "max": 1
              }
            }
          ]
        }
      ]
    }
  ]
}
```

**Acceptance Criteria**:
- Loot table JSON is valid
- Entity drops leather when killed
- Looting enchantment increases drops
- Loot loads without errors

**References**:
- Requirements: Feature 5 (Resource Loading System) - loot tables
- Minecraft Wiki: Loot table format

---

## Phase 4: Validation (2 tasks)

### TASK-011: [COMPLETED] Manual Testing and Documentation

**Agent**: implementation-agent
**Estimated Time**: 2 hours
**Priority**: Critical
**Dependencies**: All previous tasks

**Goal**: Thoroughly test framework and document usage patterns

**Requirements**:
1. Execute all manual test cases from requirements.md:
   - GeckoLib integration tests
   - Entity registration tests
   - Client rendering tests
   - Base class functionality tests
   - Resource loading tests
2. Document any issues found
3. Create developer guide: `docs/FRAMEWORK_USAGE.md`
4. Include code examples for creating new entities
5. Document troubleshooting common issues

**Test Checklist**:
- [ ] Build succeeds without errors
- [ ] Game launches with mod loaded
- [ ] Test entity summons via /summon command
- [ ] Test entity renders with model and texture
- [ ] Animations play (idle, walk)
- [ ] Entity has correct AI behavior
- [ ] Spawn egg works in creative mode
- [ ] F3+B shows correct hitbox
- [ ] Sounds play correctly
- [ ] Loot drops when entity killed
- [ ] F3+T resource reload works
- [ ] No errors in console
- [ ] Multiplayer test (if possible)

**Acceptance Criteria**:
- All test cases pass
- Issues documented (create TASK-012 if fixes needed)
- Developer guide created with clear examples
- Framework ready for use in future epics

**References**:
- Requirements: Testing Strategy section
- Requirements: Success Metrics

---

### TASK-012: [COMPLETED] Performance Validation and Optimization

**Agent**: implementation-agent
**Estimated Time**: 1.5 hours
**Priority**: Medium
**Dependencies**: TASK-011 (Manual testing)

**Goal**: Validate framework meets performance requirements

**Requirements**:
1. Profile entity tick time (Spark profiler or F3 debug)
2. Profile rendering performance (FPS with multiple entities)
3. Check memory usage (F3 memory graph)
4. Validate against requirements:
   - Entity tick < 0.05ms per entity
   - Render time < 0.1ms per entity
   - No memory leaks
5. Document baseline performance metrics
6. Optimize if needed

**Performance Tests**:
1. Spawn 50 test entities
2. Measure TPS (should stay at 20)
3. Measure FPS (should be 60+ on mid-range hardware)
4. Check entity tick time in profiler
5. Monitor memory over 10 minutes
6. Test F3+T reload performance

**Acceptance Criteria**:
- TPS remains stable (20) with 50+ entities
- FPS remains smooth (60+) with entities visible
- Entity tick time < 0.05ms per entity
- No memory leaks detected
- Performance metrics documented

**References**:
- Requirements: Performance Requirements section
- Research: `.claude/research/alexs-mobs-porting-strategy.md` (Performance Considerations)

---

## Task Execution Order

**Critical Path** (must be done in order):
1. TASK-001 (GeckoLib dependency) → Blocks all
2. TASK-002 (Entity registration) → TASK-004, TASK-005
3. TASK-003 (Client registration) → TASK-006
4. TASK-004 (Base entity) → TASK-005
5. TASK-005 (Test entity) → TASK-006, TASK-008, TASK-010
6. TASK-006 (Model/renderer) → TASK-011
7. TASK-011 (Testing) → TASK-012

**Can be done in parallel**:
- TASK-007 (Resource structure) - independent
- TASK-008 (Items/spawn egg) - after TASK-005
- TASK-009 (Sounds) - after TASK-007
- TASK-010 (Loot table) - after TASK-008

**Recommended Order**:
Week 1: TASK-001, TASK-002, TASK-003, TASK-004
Week 2: TASK-005, TASK-006, TASK-007, TASK-008
Week 3: TASK-009, TASK-010, TASK-011, TASK-012

---

## Success Criteria (Epic Completion)

This epic is **COMPLETE** when:

- ✅ All 12 tasks are marked COMPLETED
- ✅ Test entity works end-to-end (summon, render, animate, AI, sounds, loot)
- ✅ Framework is documented and ready for use
- ✅ Performance meets requirements
- ✅ User validates framework is ready for mob porting
- ✅ No blocking bugs or console errors

---

## Next Epic

After completing this epic, proceed to:

**Epic 02: Mob Catalog & Asset Inventory**
- Download Alex's Mobs 1.22.9 JAR
- Extract and catalog all 89+ animal mobs
- Create prioritization list (simple → complex)
- Document model, texture, sound requirements per mob
- Plan mob groups for future porting epics

---

**Ready to start?** Run `/next` to begin with TASK-001!
