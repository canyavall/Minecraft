# Framework Usage Guide

**Xeenaa's Alex's Mobs (Fabric) - Developer Documentation**

This guide explains how to use the framework established in Epic 01 to add new animal entities to the mod. The framework provides standardized patterns for entity registration, rendering, animation, sounds, and loot.

---

## Table of Contents

1. [Framework Overview](#framework-overview)
2. [Architecture](#architecture)
3. [Adding a New Animal Entity](#adding-a-new-animal-entity)
4. [Component Reference](#component-reference)
5. [Best Practices](#best-practices)
6. [Troubleshooting](#troubleshooting)

---

## Framework Overview

### What is the Framework?

The framework is a collection of base classes, registries, and patterns that provide:

- **Entity Registration System**: Centralized registration with Fabric (ModEntities)
- **Base Animal Entity**: Common AI goals and behaviors (BaseAnimalEntity)
- **GeckoLib Integration**: Complex 3D animations via GeckoLib
- **Client Rendering**: Model, texture, and animation loading (GeoModel, GeoEntityRenderer)
- **Sound System**: Centralized sound event registration (ModSounds)
- **Item System**: Spawn eggs and other items (ModItems)
- **Resource Organization**: Standardized directory structure

### Key Technologies

- **Minecraft**: 1.21.1
- **Fabric API**: Modern mod loader
- **GeckoLib 5.0+**: Animation system (replaces Forge-only Citadel)
- **Java 21**: Modern Java features (records, pattern matching, text blocks)

### Design Principles

- **DRY (Don't Repeat Yourself)**: Common functionality in base classes
- **Consistency**: All entities follow the same patterns
- **Separation of Concerns**: Clear boundaries (entity logic, rendering, sounds, items)
- **Extensibility**: Easy to add new entities without modifying framework

---

## Architecture

### Package Structure

```
com.canya.xeenaa_alexs_mobs/
├── AlexsMobsMod.java              # Main mod initializer (server + client)
├── client/
│   ├── AlexsMobsClient.java       # Client initializer (client only)
│   ├── model/
│   │   └── TestAnimalModel.java   # GeckoLib model (resource locations)
│   └── renderer/
│       └── TestAnimalRenderer.java # GeckoLib renderer (model + context)
├── entity/
│   ├── BaseAnimalEntity.java      # Abstract base for all animals
│   └── animal/
│       └── TestAnimalEntity.java  # Concrete entity implementation
└── registry/
    ├── ModEntities.java           # Entity type registration
    ├── ModItems.java              # Item registration (spawn eggs)
    └── ModSounds.java             # Sound event registration
```

### Resource Structure

```
resources/
├── assets/xeenaa-alexs-mobs/
│   ├── geo/                       # GeckoLib models (.geo.json)
│   ├── animations/                # Animation files (.animation.json)
│   ├── textures/
│   │   ├── entity/               # Entity textures (.png)
│   │   └── item/                 # Item textures (.png)
│   ├── sounds/                   # Sound files (.ogg)
│   ├── sounds.json               # Sound event definitions
│   └── lang/
│       └── en_us.json            # Translations
└── data/xeenaa-alexs-mobs/
    └── loot_table/
        └── entities/             # Entity loot tables (.json)
```

### Component Interaction Flow

```
1. Mod Initialization (AlexsMobsMod.onInitialize)
   → ModSounds.initialize()     # Register sound events
   → ModEntities.initialize()   # Register entities + attributes
   → ModItems.initialize()      # Register items + creative tab

2. Client Initialization (AlexsMobsClient.onInitializeClient)
   → EntityRendererRegistry.register() # Register renderers

3. Entity Spawning (In-Game)
   → EntityType.create(world)   # Create entity instance
   → BaseAnimalEntity.initGoals() # Set up AI goals
   → GeoEntity.registerControllers() # Set up animations

4. Entity Rendering (Client)
   → TestAnimalRenderer.render()
   → TestAnimalModel.getModelResource()
   → TestAnimalModel.getTextureResource()
   → TestAnimalModel.getAnimationResource()
   → GeckoLib loads and renders model
```

---

## Adding a New Animal Entity

Follow these steps to add a new animal (e.g., "Grizzly Bear"):

### Step 1: Create the Entity Class

**File**: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/GrizzlyBearEntity.java`

```java
package com.canya.xeenaa_alexs_mobs.entity.animal;

import com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity;
import com.canya.xeenaa_alexs_mobs.registry.ModEntities;
import com.canya.xeenaa_alexs_mobs.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Grizzly Bear entity from Alex's Mobs.
 *
 * <p>A powerful bear that can be hostile when provoked.
 *
 * @author Canya
 * @see BaseAnimalEntity
 * @see GeoEntity
 */
public class GrizzlyBearEntity extends BaseAnimalEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GrizzlyBearEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // ============================================
    // Entity Attributes
    // ============================================

    /**
     * Creates default attributes for the grizzly bear.
     *
     * @return attribute builder with configured stats
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)      // 20 hearts
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)   // Faster than most animals
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D);   // Strong attack
    }

    // ============================================
    // Custom AI Goals
    // ============================================

    @Override
    protected void registerCustomGoals() {
        // Priority 3: Attack nearby targets
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false));

        // Priority 1: Target players who attack
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    // ============================================
    // GeckoLib Animation System
    // ============================================

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<GrizzlyBearEntity> state) {
        if (this.isAttacking()) {
            state.getController().setAnimation(
                RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE)
            );
        } else if (state.isMoving()) {
            state.getController().setAnimation(
                RawAnimation.begin().then("walk", Animation.LoopType.LOOP)
            );
        } else {
            state.getController().setAnimation(
                RawAnimation.begin().then("idle", Animation.LoopType.LOOP)
            );
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ============================================
    // Breeding System
    // ============================================

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.GRIZZLY_BEAR.create(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.SALMON);  // Can breed with salmon
    }

    // ============================================
    // Sound System
    // ============================================

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.GRIZZLY_BEAR_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.GRIZZLY_BEAR_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GRIZZLY_BEAR_DEATH;
    }
}
```

### Step 2: Register the Entity Type

**File**: `ModEntities.java`

Add the entity type registration:

```java
public static final EntityType<GrizzlyBearEntity> GRIZZLY_BEAR = register(
    "grizzly_bear",
    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, GrizzlyBearEntity::new)
        .dimensions(EntityDimensions.fixed(1.5f, 1.4f))  // Width × Height
        .build()
);
```

Then register attributes in `initialize()`:

```java
public static void initialize() {
    FabricDefaultAttributeRegistry.register(TEST_ANIMAL, TestAnimalEntity.createAttributes());
    FabricDefaultAttributeRegistry.register(GRIZZLY_BEAR, GrizzlyBearEntity.createAttributes());
}
```

### Step 3: Create the Client Model

**File**: `src/main/java/com/canya/xeenaa_alexs_mobs/client/model/GrizzlyBearModel.java`

```java
package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.GrizzlyBearEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model for the Grizzly Bear entity.
 *
 * @author Canya
 * @see GrizzlyBearEntity
 */
@Environment(EnvType.CLIENT)
public class GrizzlyBearModel extends GeoModel<GrizzlyBearEntity> {

    @Override
    public Identifier getModelResource(GrizzlyBearEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/grizzly_bear.geo.json");
    }

    @Override
    public Identifier getTextureResource(GrizzlyBearEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/grizzly_bear.png");
    }

    @Override
    public Identifier getAnimationResource(GrizzlyBearEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/grizzly_bear.animation.json");
    }
}
```

### Step 4: Create the Client Renderer

**File**: `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/GrizzlyBearRenderer.java`

```java
package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.GrizzlyBearModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.GrizzlyBearEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for the Grizzly Bear entity.
 *
 * @author Canya
 * @see GrizzlyBearEntity
 * @see GrizzlyBearModel
 */
@Environment(EnvType.CLIENT)
public class GrizzlyBearRenderer extends GeoEntityRenderer<GrizzlyBearEntity> {

    public GrizzlyBearRenderer(EntityRendererFactory.Context context) {
        super(context, new GrizzlyBearModel());
        this.shadowRadius = 0.8f;  // Shadow size
    }

    @Override
    public Identifier getTextureLocation(GrizzlyBearEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/grizzly_bear.png");
    }
}
```

### Step 5: Register the Renderer

**File**: `AlexsMobsClient.java`

Add renderer registration in `registerEntityRenderers()`:

```java
private void registerEntityRenderers() {
    EntityRendererRegistry.register(ModEntities.TEST_ANIMAL, TestAnimalRenderer::new);
    EntityRendererRegistry.register(ModEntities.GRIZZLY_BEAR, GrizzlyBearRenderer::new);
}
```

### Step 6: Register Sound Events

**File**: `ModSounds.java`

Add sound event registrations:

```java
public static final SoundEvent GRIZZLY_BEAR_AMBIENT = register("entity.grizzly_bear.ambient");
public static final SoundEvent GRIZZLY_BEAR_HURT = register("entity.grizzly_bear.hurt");
public static final SoundEvent GRIZZLY_BEAR_DEATH = register("entity.grizzly_bear.death");
```

Update `getRegisteredSoundCount()`:

```java
private static int getRegisteredSoundCount() {
    return 6;  // 3 for test_animal + 3 for grizzly_bear
}
```

### Step 7: Create Spawn Egg

**File**: `ModItems.java`

Add spawn egg registration:

```java
public static final Item GRIZZLY_BEAR_SPAWN_EGG = register(
    "grizzly_bear_spawn_egg",
    new SpawnEggItem(ModEntities.GRIZZLY_BEAR, 0x5C4033, 0x3D2A21,
        new Item.Settings())
);
```

Add to creative tab in `initialize()`:

```java
.entries((context, entries) -> {
    entries.add(TEST_ANIMAL_SPAWN_EGG);
    entries.add(GRIZZLY_BEAR_SPAWN_EGG);
})
```

### Step 8: Add Resources

Create these resource files:

#### Geometry Model
**File**: `src/main/resources/assets/xeenaa-alexs-mobs/geo/grizzly_bear.geo.json`
- Export from Blockbench (Bedrock Edition model format)

#### Texture
**File**: `src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/grizzly_bear.png`
- Entity texture (e.g., 64×64 or 128×128 pixels)

#### Animations
**File**: `src/main/resources/assets/xeenaa-alexs-mobs/animations/grizzly_bear.animation.json`
- Export from Blockbench (animations: idle, walk, attack)

#### Sound Definitions
**File**: `src/main/resources/assets/xeenaa-alexs-mobs/sounds.json`

Add entries:

```json
{
  "entity.grizzly_bear.ambient": {
    "sounds": [
      "xeenaa-alexs-mobs:entity/grizzly_bear/ambient1"
    ],
    "subtitle": "subtitles.entity.grizzly_bear.ambient"
  },
  "entity.grizzly_bear.hurt": {
    "sounds": [
      "xeenaa-alexs-mobs:entity/grizzly_bear/hurt1"
    ],
    "subtitle": "subtitles.entity.grizzly_bear.hurt"
  },
  "entity.grizzly_bear.death": {
    "sounds": [
      "xeenaa-alexs-mobs:entity/grizzly_bear/death1"
    ],
    "subtitle": "subtitles.entity.grizzly_bear.death"
  }
}
```

#### Sound Files
**Files**: `src/main/resources/assets/xeenaa-alexs-mobs/sounds/entity/grizzly_bear/`
- `ambient1.ogg`
- `hurt1.ogg`
- `death1.ogg`

#### Translations
**File**: `src/main/resources/assets/xeenaa-alexs-mobs/lang/en_us.json`

Add entries:

```json
{
  "entity.xeenaa-alexs-mobs.grizzly_bear": "Grizzly Bear",
  "item.xeenaa-alexs-mobs.grizzly_bear_spawn_egg": "Grizzly Bear Spawn Egg",
  "subtitles.entity.grizzly_bear.ambient": "Grizzly Bear growls",
  "subtitles.entity.grizzly_bear.hurt": "Grizzly Bear hurts",
  "subtitles.entity.grizzly_bear.death": "Grizzly Bear dies"
}
```

#### Loot Table
**File**: `src/main/resources/data/xeenaa-alexs-mobs/loot_table/entities/grizzly_bear.json`

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
                "min": 2,
                "max": 4
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

### Step 9: Build and Test

```bash
./gradlew build
./gradlew runClient
```

In-game tests:
1. `/summon xeenaa-alexs-mobs:grizzly_bear`
2. Use spawn egg from creative inventory
3. Verify rendering, animations, AI behavior
4. Test sounds (ambient, hurt, death)
5. Kill entity and verify loot drops

---

## Component Reference

### BaseAnimalEntity

**Purpose**: Abstract base class providing common animal behaviors.

**Provided Features**:
- Standard AI goals (swim, escape danger, mate, wander, look)
- Sound system (abstract methods for ambient, hurt, death)
- Breeding support (createChild method)
- Helper methods (isInDanger, isIdle)

**How to Extend**:
1. Extend `BaseAnimalEntity`
2. Implement `GeoEntity` for GeckoLib animations
3. Override `registerCustomGoals()` for entity-specific AI
4. Implement abstract sound methods
5. Implement `createChild()` for breeding
6. Add `createAttributes()` static method

**Example**:
```java
public class MyAnimalEntity extends BaseAnimalEntity implements GeoEntity {
    @Override
    protected void registerCustomGoals() {
        // Add custom AI goals here (priority 3-4 recommended)
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MY_ANIMAL_AMBIENT;
    }

    // ... implement other abstract methods
}
```

### GeoEntity (GeckoLib Interface)

**Purpose**: Enables GeckoLib animations on entities.

**Required Methods**:
- `registerControllers()`: Register animation controllers
- `getAnimatableInstanceCache()`: Provide animation cache

**Animation Controller Pattern**:
```java
private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

@Override
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
}

private PlayState predicate(AnimationState<MyEntity> state) {
    if (state.isMoving()) {
        state.getController().setAnimation(
            RawAnimation.begin().then("walk", Animation.LoopType.LOOP)
        );
    } else {
        state.getController().setAnimation(
            RawAnimation.begin().then("idle", Animation.LoopType.LOOP)
        );
    }
    return PlayState.CONTINUE;
}

@Override
public AnimatableInstanceCache getAnimatableInstanceCache() {
    return cache;
}
```

**Animation States**:
- `state.isMoving()`: Entity is walking/running
- `this.isAttacking()`: Entity is performing attack
- Custom conditions: Check entity state fields

**Animation Types**:
- `Animation.LoopType.LOOP`: Repeating animation (idle, walk)
- `Animation.LoopType.PLAY_ONCE`: Single playback (attack, eat)
- `Animation.LoopType.HOLD_ON_LAST_FRAME`: Play once and hold (death)

### GeoModel

**Purpose**: Provides resource locations for entity models, textures, and animations.

**Required Methods**:
- `getModelResource()`: Returns path to `.geo.json`
- `getTextureResource()`: Returns path to texture `.png`
- `getAnimationResource()`: Returns path to `.animation.json`

**Naming Conventions**:
```
Model:      geo/[entity_name].geo.json
Texture:    textures/entity/[entity_name].png
Animations: animations/[entity_name].animation.json
```

**Variant Support**:
```java
@Override
public Identifier getTextureResource(MyEntity entity) {
    if (entity.getVariant() == Variant.ALBINO) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/my_entity_albino.png");
    }
    return Identifier.of("xeenaa-alexs-mobs", "textures/entity/my_entity.png");
}
```

### GeoEntityRenderer

**Purpose**: Renders the entity using GeckoLib.

**Key Configuration**:
- `shadowRadius`: Shadow size (0.5 = small, 1.0 = large)
- Model instance passed to constructor
- Texture location from model

**Example**:
```java
public class MyAnimalRenderer extends GeoEntityRenderer<MyAnimalEntity> {
    public MyAnimalRenderer(EntityRendererFactory.Context context) {
        super(context, new MyAnimalModel());
        this.shadowRadius = 0.7f;  // Adjust based on entity size
    }

    @Override
    public Identifier getTextureLocation(MyAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/my_animal.png");
    }
}
```

### ModEntities Registry

**Purpose**: Centralized entity type registration.

**Pattern**:
```java
public static final EntityType<MyEntity> MY_ENTITY = register(
    "my_entity",
    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MyEntity::new)
        .dimensions(EntityDimensions.fixed(1.0f, 1.0f))  // Width × Height
        .build()
);
```

**Spawn Groups**:
- `CREATURE`: Passive animals (most Alex's Mobs entities)
- `MONSTER`: Hostile mobs
- `AMBIENT`: Bats, flies
- `WATER_CREATURE`: Fish, dolphins

**Dimensions**:
- Width: Entity hitbox width (blocks)
- Height: Entity hitbox height (blocks)
- Example: Sheep = 0.9 × 1.3, Cow = 0.9 × 1.4

### ModSounds Registry

**Purpose**: Centralized sound event registration.

**Naming Convention**:
```
entity.[entity_name].[sound_type]
```

**Sound Types**:
- `ambient`: Idle/periodic sounds
- `hurt`: Damage sounds
- `death`: Death sounds
- `step`: Footstep sounds (optional)
- `attack`: Attack sounds (optional)

**Example**:
```java
public static final SoundEvent MY_ENTITY_AMBIENT = register("entity.my_entity.ambient");
public static final SoundEvent MY_ENTITY_HURT = register("entity.my_entity.hurt");
public static final SoundEvent MY_ENTITY_DEATH = register("entity.my_entity.death");
```

### ModItems Registry

**Purpose**: Centralized item registration (spawn eggs, drops, equipment).

**Spawn Egg Pattern**:
```java
public static final Item MY_ENTITY_SPAWN_EGG = register(
    "my_entity_spawn_egg",
    new SpawnEggItem(ModEntities.MY_ENTITY, 0xPRIMARY, 0xSECONDARY,
        new Item.Settings())
);
```

**Color Selection**:
- Primary: Base color of the egg
- Secondary: Spots color
- Use hex colors (e.g., `0xFF5733`)

---

## Best Practices

### Code Organization

1. **One entity per file**: Keep entity classes focused and manageable
2. **Package structure**: Follow established patterns (`entity.animal`, `client.model`, `client.renderer`)
3. **Naming conventions**: Use entity name consistently across all files
4. **Documentation**: Add Javadoc to all public methods and classes

### Performance Optimization

1. **AI Goals**: Keep priority 0-2 for critical behaviors, 3-7 for common behaviors
2. **Animation Predicates**: Avoid expensive computations in predicate methods (called every tick)
3. **Sound Methods**: Return constant SoundEvent references (no computation)
4. **Caching**: Use `AnimatableInstanceCache` (already handled by framework)

### Resource Management

1. **Texture Size**: Use appropriate resolution (64×64 or 128×128 for most entities)
2. **Sound Format**: Use .ogg format for all sounds (Minecraft requirement)
3. **Model Complexity**: Balance detail with performance (GeckoLib handles well, but keep reasonable)
4. **Animation Efficiency**: Avoid excessive keyframes

### Testing Strategy

1. **Build first**: Always build before testing (`./gradlew build`)
2. **Console monitoring**: Watch for errors, warnings, missing resources
3. **In-game tests**:
   - Summon entity with `/summon` command
   - Verify rendering and animations
   - Test AI behaviors (wander, attack, flee)
   - Verify sounds (ambient, hurt, death)
   - Test spawn egg
   - Verify loot drops
4. **Multiplayer testing**: Test on dedicated server if possible

### Version Control

1. **Commit frequently**: Small, focused commits
2. **Test before commit**: Ensure build succeeds and game launches
3. **Descriptive messages**: Explain what entity/feature was added

---

## Troubleshooting

### Entity Won't Summon

**Symptoms**: `/summon xeenaa-alexs-mobs:my_entity` fails or shows "Unknown entity"

**Causes & Solutions**:
1. **Not registered**: Check `ModEntities.java` has entity type defined
2. **Attributes not registered**: Check `ModEntities.initialize()` calls `FabricDefaultAttributeRegistry.register()`
3. **Mod not loaded**: Check `fabric.mod.json` entrypoints are correct
4. **Wrong namespace**: Entity ID must match mod ID (`xeenaa-alexs-mobs`)

**Debug Steps**:
```bash
# Check logs for registration errors
grep "Registering entity" logs/latest.log

# Verify entity type exists
/summon xeenaa-alexs-mobs:   # Tab completion should show entities
```

### Entity is Invisible (Missing Model)

**Symptoms**: Entity exists but is invisible, no model renders

**Causes & Solutions**:
1. **Renderer not registered**: Check `AlexsMobsClient.registerEntityRenderers()` has renderer
2. **Model file missing**: Verify `.geo.json` file exists at correct path
3. **Wrong resource path**: Check `getModelResource()` returns correct Identifier
4. **Client-only code on server**: Ensure renderer is in client package with `@Environment(EnvType.CLIENT)`

**Debug Steps**:
```bash
# Check logs for rendering errors
grep "renderer" logs/latest.log | grep -i error

# Verify resource files exist
ls src/main/resources/assets/xeenaa-alexs-mobs/geo/
```

### Entity Has Missing Texture (Pink/Black Checkerboard)

**Symptoms**: Entity model renders but shows pink/black checkerboard pattern

**Causes & Solutions**:
1. **Texture file missing**: Verify `.png` file exists at correct path
2. **Wrong texture path**: Check `getTextureResource()` returns correct Identifier
3. **File extension wrong**: Must be `.png` not `.jpg`
4. **Texture too large**: Minecraft limits texture size (keep under 1024×1024)

**Debug Steps**:
```bash
# Check logs for texture loading errors
grep "texture" logs/latest.log | grep -i "missing\|error"

# Verify texture file exists
ls src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/
```

### Animations Don't Play

**Symptoms**: Entity renders but stays in T-pose or doesn't animate

**Causes & Solutions**:
1. **Animation file missing**: Verify `.animation.json` exists
2. **Wrong animation names**: Animation names in predicate must match animation file
3. **Controller not registered**: Check `registerControllers()` is implemented
4. **GeoEntity not implemented**: Entity must implement `GeoEntity` interface

**Debug Steps**:
```java
// Add debug logging to predicate
private PlayState predicate(AnimationState<MyEntity> state) {
    LOGGER.info("Animation state: moving={}", state.isMoving());
    // ... rest of predicate
}
```

### Sounds Don't Play

**Symptoms**: Entity is silent (no ambient, hurt, or death sounds)

**Causes & Solutions**:
1. **Sound files missing**: `.ogg` files don't exist (expected for test entity)
2. **Sound events not registered**: Check `ModSounds.java` has sound events
3. **sounds.json missing entries**: Check sound definitions exist
4. **Wrong sound path**: Verify paths in sounds.json match actual files

**Expected Behavior**:
- Missing `.ogg` files produce warnings but don't crash
- Sound events still work (just silent until files added)

**Debug Steps**:
```bash
# Check logs for sound errors
grep "sound" logs/latest.log | grep -i "missing\|error"

# Verify sounds.json is valid JSON
cat src/main/resources/assets/xeenaa-alexs-mobs/sounds.json | jq .
```

### Entity AI Doesn't Work

**Symptoms**: Entity stands still, doesn't wander, doesn't react to player

**Causes & Solutions**:
1. **Attributes not registered**: Entity needs movement speed attribute
2. **Goals registered with wrong priority**: High-priority goals block lower ones
3. **BaseAnimalEntity not extended**: Use `BaseAnimalEntity` for common AI
4. **Goal conflicts**: Multiple goals competing (check priorities)

**Debug Steps**:
```java
// Add debug logging to registerCustomGoals
@Override
protected void registerCustomGoals() {
    LOGGER.info("Registering custom goals for {}", this.getType().toString());
    // ... register goals
}
```

### Spawn Egg Doesn't Work

**Symptoms**: Spawn egg exists but doesn't spawn entity or has wrong colors

**Causes & Solutions**:
1. **Entity type wrong**: Check spawn egg references correct `ModEntities.MY_ENTITY`
2. **Not added to creative tab**: Check `ModItems.initialize()` adds to entries
3. **Wrong colors**: Colors are hex values (e.g., `0xRRGGBB`)

**Debug Steps**:
```bash
# Verify spawn egg is registered
grep "spawn_egg" logs/latest.log
```

### Build Fails

**Symptoms**: `./gradlew build` produces errors

**Common Errors**:
1. **Import errors**: Missing import statements
2. **Type mismatches**: Check generics (e.g., `EntityType<MyEntity>`)
3. **Missing methods**: Ensure all abstract methods implemented
4. **Syntax errors**: Check for missing semicolons, braces

**Debug Steps**:
```bash
# Build with detailed error output
./gradlew build --stacktrace

# Check for compilation errors
./gradlew compileJava
```

### F3+T Resource Reload Crashes

**Symptoms**: Game crashes when reloading resources with F3+T

**Causes & Solutions**:
1. **Invalid JSON**: Check `.geo.json` and `.animation.json` are valid
2. **Resource path errors**: Verify all paths in model class are correct
3. **Missing resources**: Ensure all referenced files exist

**Debug Steps**:
```bash
# Validate JSON files
jq . src/main/resources/assets/xeenaa-alexs-mobs/geo/my_entity.geo.json
jq . src/main/resources/assets/xeenaa-alexs-mobs/animations/my_entity.animation.json
```

### Loot Doesn't Drop

**Symptoms**: Entity dies but drops nothing

**Causes & Solutions**:
1. **Loot table missing**: Create loot table JSON file
2. **Wrong loot table path**: Must be `data/xeenaa-alexs-mobs/loot_table/entities/[entity_name].json`
3. **Invalid JSON**: Check loot table JSON is valid

**Debug Steps**:
```bash
# Verify loot table exists
ls src/main/resources/data/xeenaa-alexs-mobs/loot_table/entities/

# Validate loot table JSON
jq . src/main/resources/data/xeenaa-alexs-mobs/loot_table/entities/my_entity.json
```

---

## Additional Resources

### External Documentation

- **GeckoLib Wiki**: https://github.com/bernie-g/geckolib/wiki
- **Fabric Wiki**: https://fabricmc.net/wiki/
- **Minecraft Wiki (Entity)**: https://minecraft.wiki/w/Entity
- **Blockbench**: https://www.blockbench.net/ (for creating models)

### Project Documentation

- **QUICKSTART.md**: Fast setup and testing guide
- **TESTING_CHECKLIST.md**: Manual testing procedures
- **project.md**: Project vision and technical stack
- **requirements.md**: Epic 01 business requirements

### Code Examples

- **TestAnimalEntity**: Simple example entity
- **BaseAnimalEntity**: Common AI goals and patterns
- **ModEntities**: Registration pattern examples

---

**Questions or Issues?**

If you encounter issues not covered here:
1. Check logs: `logs/latest.log`
2. Verify resource files exist
3. Compare with TestAnimalEntity (working example)
4. Check GeckoLib and Fabric documentation

**Framework Version**: 0.1.0 (Epic 01)
**Last Updated**: 2025-10-26
