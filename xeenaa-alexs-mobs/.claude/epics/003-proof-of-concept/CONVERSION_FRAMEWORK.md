# Reusable Citadel → GeckoLib Conversion Framework

**Date**: 2025-10-26
**Purpose**: Define reusable patterns and tools for converting 89 remaining mobs
**Status**: Design Complete (Post-Fly Success)

---

## Executive Summary

Based on our successful Fly conversion ("works perfect, nice" - user feedback), we've identified **reusable patterns** that can be automated for the remaining 89 mobs.

**Key Finding**: ~80% of conversion work is **mechanical and automatable**. Only ~20% requires manual tuning (complex animations, unique behaviors).

**Time Savings**: With automation, we can convert mobs in **45-60 minutes** instead of 3-5 hours (75-80% faster).

**Framework Components**:
1. **Decompilation Workflow** - Extract Citadel models/animations from JAR
2. **Math Translation Tools** - Convert procedural animations to keyframes
3. **Code Generators** - Auto-generate entity/model/renderer classes
4. **Asset Management** - Batch-copy textures/sounds/loot tables
5. **Testing Checklist** - Systematic validation process

---

## 1. What's Reusable from Fly Implementation

### Analysis of Fly Conversion

**Total Time**: ~55 minutes (Approach C - Citadel conversion)

**Breakdown**:
- Decompilation: 10 min
- Model geometry conversion: 15 min
- Animation conversion: 20 min
- Entity code creation: 5 min
- Registration code: 3 min
- Asset copying: 2 min

### Reusable Components Identified

#### 1.1 Model Conversion (100% Reusable)

**What Fly Taught Us**:
- Citadel's `addBox()` calls → GeckoLib JSON cubes (direct translation)
- Bone hierarchy from `ModelPart` parent-child relationships
- Pivot points from `setPivot()` calls
- Texture UV coordinates preserved exactly

**Reusable Pattern**:
```java
// Citadel code (decompiled):
ModelPart body = new ModelPart(this);
body.setPivot(0.0F, 20.0F, 0.0F);
body.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4);

// GeckoLib JSON (converted):
{
  "name": "body",
  "pivot": [0, 20, 0],
  "cubes": [{
    "origin": [-1.5, -1.5, -2.0],
    "size": [3, 3, 4],
    "uv": [0, 0]
  }]
}
```

**Automation Potential**: ✅ **100% automatable** (mechanical translation)

---

#### 1.2 Animation Conversion (90% Reusable)

**What Fly Taught Us**:
- Citadel's `flap()` → GeckoLib rotation keyframes (sine wave sampling)
- Citadel's `walk()` → GeckoLib rotation keyframes (X-axis oscillation)
- Citadel's `swing()` → GeckoLib rotation keyframes (Y-axis pendulum)
- State-based logic → GeckoLib animation controller

**Reusable Formulas**:

**Flap Animation**:
```
Citadel: flap(bone, speed=1.82, degree=0.8, invert=true, offset=0, time)
Formula: rotation_z = sin(time × speed + offset) × degree × 45.8° × (invert ? -1 : 1)

GeckoLib: Sample sine wave at keyframes
[0.0, 0.25, 0.5, 0.75, 1.0] → [-45°, 45°, -45°, 45°, -45°] (for left wing)
```

**Walk Animation**:
```
Citadel: walk(bone, speed=0.28, degree=0.16, invert=false, offset=1.0, time)
Formula: rotation_x = sin(time × speed + offset) × degree × 45.8°

GeckoLib: Sample sine wave on X-axis
[0.0, 0.25, 0.5, 0.75, 1.0] → [0°, 4°, 0°, -4°, 0°] (subtle bob)
```

**Automation Potential**: ✅ **90% automatable** (only complex custom animations need manual work)

---

#### 1.3 Entity Code (95% Reusable)

**What Fly Taught Us**:
- Base class selection: `FlyingAnimalEntity` vs `AquaticAnimalEntity` vs `BaseAnimalEntity`
- AI goal registration: Same goals used across many mobs
- Animation controller: Same pattern (dead → death, moving → movement, else → idle)
- Sound methods: Always same structure (getAmbientSound, getHurtSound, getDeathSound)

**Reusable Template**:
```java
public class {MOB_NAME}Entity extends {BASE_CLASS} implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Constructor (always same)
    public {MOB_NAME}Entity(EntityType<? extends {BASE_CLASS}> entityType, World world) {
        super(entityType, world);
    }

    // Attributes (parameterized)
    public static DefaultAttributeContainer.Builder createAttributes() {
        return {BASE_CLASS}.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, {HEALTH})
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, {SPEED})
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, {FOLLOW_RANGE});
    }

    // AI goals (from specification)
    @Override
    protected void registerCustomGoals() {
        {GENERATED_GOALS}
    }

    // Animation controller (always same pattern)
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "{MOB_NAME}Controller", 0, this::animationPredicate));
    }

    private PlayState animationPredicate(AnimationState<{MOB_NAME}Entity> state) {
        if (this.isDead()) {
            controller.setAnimation("death");
        } else if ({MOVEMENT_CONDITION}) {
            controller.setAnimation("{MOVEMENT_ANIM}");
        } else {
            controller.setAnimation("idle");
        }
    }

    // Sounds (always same pattern, parameterized)
    @Override
    protected SoundEvent getAmbientSound() { return ModSounds.{MOB_NAME_UPPER}_AMBIENT; }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return ModSounds.{MOB_NAME_UPPER}_HURT; }
    @Override
    protected SoundEvent getDeathSound() { return ModSounds.{MOB_NAME_UPPER}_DEATH; }

    // Breeding (most mobs not breedable, same pattern)
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.{MOB_NAME_UPPER}.create(world);
    }
    @Override
    public boolean isBreedingItem(ItemStack stack) { return false; }
}
```

**Automation Potential**: ✅ **95% automatable** (only unique behaviors need manual coding)

---

#### 1.4 Model/Renderer Classes (100% Reusable)

**What Fly Taught Us**:
- Model class: Always same structure (3 methods returning resource identifiers)
- Renderer class: Always same structure (constructor registers model)

**Reusable Templates**:

**Model**:
```java
public class {MOB_NAME}Model extends GeoModel<{MOB_NAME}Entity> {
    @Override
    public Identifier getModelResource({MOB_NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/{MOB_NAME_LOWER}.geo.json");
    }

    @Override
    public Identifier getTextureResource({MOB_NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/{MOB_NAME_LOWER}/{MOB_NAME_LOWER}.png");
    }

    @Override
    public Identifier getAnimationResource({MOB_NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/{MOB_NAME_LOWER}.animation.json");
    }
}
```

**Renderer**:
```java
public class {MOB_NAME}Renderer extends GeoEntityRenderer<{MOB_NAME}Entity> {
    public {MOB_NAME}Renderer(EntityRendererFactory.Context context) {
        super(context, new {MOB_NAME}Model());
    }
}
```

**Automation Potential**: ✅ **100% automatable** (zero variation between mobs)

---

#### 1.5 Registration Code (100% Reusable)

**What Fly Taught Us**:
- Entity registration: Same pattern for all mobs
- Spawn egg registration: Same pattern (only colors change)
- Sound registration: Same pattern
- Renderer registration: Same pattern

**Reusable Patterns**:

**Entity (ModEntities.java)**:
```java
public static final EntityType<{MOB_NAME}Entity> {MOB_NAME_UPPER} = Registry.register(
    Registries.ENTITY_TYPE,
    Identifier.of("xeenaa-alexs-mobs", "{MOB_NAME_LOWER}"),
    EntityType.Builder.create({MOB_NAME}Entity::new, SpawnGroup.{SPAWN_GROUP})
        .dimensions({WIDTH}f, {HEIGHT}f)
        .maxTrackingRange(8)
        .build()
);
```

**Spawn Egg (ModItems.java)**:
```java
public static final Item {MOB_NAME_UPPER}_SPAWN_EGG = Registry.register(
    Registries.ITEM,
    Identifier.of("xeenaa-alexs-mobs", "{MOB_NAME_LOWER}_spawn_egg"),
    new SpawnEggItem(ModEntities.{MOB_NAME_UPPER}, {PRIMARY_COLOR}, {SECONDARY_COLOR}, new Item.Settings())
);
```

**Sounds (ModSounds.java)**:
```java
public static final SoundEvent {MOB_NAME_UPPER}_AMBIENT = registerSound("entity.{MOB_NAME_LOWER}.ambient");
public static final SoundEvent {MOB_NAME_UPPER}_HURT = registerSound("entity.{MOB_NAME_LOWER}.hurt");
public static final SoundEvent {MOB_NAME_UPPER}_DEATH = registerSound("entity.{MOB_NAME_LOWER}.death");
```

**Renderer (AlexsMobsClient.java)**:
```java
EntityRendererRegistry.register(ModEntities.{MOB_NAME_UPPER}, {MOB_NAME}Renderer::new);
```

**Automation Potential**: ✅ **100% automatable** (pure string replacement)

---

## 2. Conversion Workflow (Step-by-Step)

### Step 1: Decompile Citadel Model (5-10 min)

**Goal**: Extract model and animation code from Alex's Mobs JAR.

**Tools**:
- **JD-GUI**: Graphical decompiler (easy to use)
- **IntelliJ IDEA**: Built-in decompiler (if you have IntelliJ)
- **CFR**: Command-line decompiler (for batch processing)

**Process**:
1. Open `alexsmobs-1.22.9.jar` in decompiler
2. Navigate to `com/github/alexthe666/alexsmobs/entity/`
3. Find `Model{MobName}.java` (e.g., `ModelFly.java`, `ModelCockroach.java`)
4. Save decompiled source to `.claude/temp/decompiled/{MobName}/Model{MobName}.java`

**What to Extract**:
- Bone hierarchy (constructor)
- Cube definitions (`addBox()` calls)
- Animation logic (`setupAnim()` method)
- Texture dimensions (`texWidth`, `texHeight`)

**Automation Potential**: ⚠️ **Semi-automatable** (batch decompile all models at once)

---

### Step 2: Analyze Animation Code (5-10 min)

**Goal**: Understand what animations the mob has and how they work.

**Manual Analysis**:
Read `setupAnim()` method and document:

1. **Bones Animated**: Which bones rotate/translate?
2. **Animation Helpers Used**: `flap()`, `walk()`, `swing()`, `bob()`?
3. **Parameters**: Speed, degree, invert, offset values
4. **State Logic**: What conditions trigger different animations? (isOnGround, isSwimming, isDead)

**Example from ModelFly.java**:
```java
// Analysis Notes for Fly:
// - Bones animated: left_wing, right_wing, mouth, legs
// - States:
//   - On ground: Wings static at ±35°, legs swing
//   - Flying: Wings flap (speed=1.82, degree=0.8), legs walk
// - Animation helpers:
//   - flap(left_wing, 1.82, 0.8, true, 0, 0.2, time, 1.0)
//   - flap(right_wing, 1.82, 0.8, false, 0, 0.2, time, 1.0)
//   - walk(mouth, 0.28, 0.08, false, -1.0, 0.2, time, 1.0)
//   - walk(legs, 0.28, 0.16, false, 1.0, 0.2, time, 1.0)
```

**Output**: Text file `{MobName}_animation_analysis.txt`

**Automation Potential**: ❌ **Manual** (requires understanding code logic)

---

### Step 3: Convert Model Geometry (10-15 min)

**Goal**: Create `.geo.json` file from Citadel model code.

**Option A: Manual in Blockbench** (higher quality):
1. Create new GeckoLib model in Blockbench
2. Recreate bone hierarchy from decompiled code
3. Add cubes based on `addBox()` calls
4. Set pivot points from `setPivot()` calls
5. Export as `.geo.json`

**Option B: Programmatic** (faster, lower initial quality):
1. Parse decompiled Java code
2. Extract `addBox()` and `setPivot()` calls
3. Generate `.geo.json` programmatically
4. Refine later if needed

**For 89 Mobs**: **Option B** recommended (speed over perfection initially)

**Pseudo-Code for Parser**:
```java
public class CitadelModelParser {
    public GeoJson parseModel(Path modelJavaFile) {
        String code = Files.readString(modelJavaFile);

        // Extract bones
        List<Bone> bones = extractBones(code);

        // For each bone, extract cubes
        for (Bone bone : bones) {
            extractCubes(code, bone);
            extractPivot(code, bone);
        }

        // Convert to GeckoLib JSON
        return convertToGeoJson(bones);
    }

    private List<Bone> extractBones(String code) {
        // Find "ModelPart {name} = new ModelPart(this);"
        // Extract bone names
    }

    private void extractCubes(String code, Bone bone) {
        // Find "{bone}.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4);"
        // Extract origin and size
    }

    private void extractPivot(String code, Bone bone) {
        // Find "{bone}.setPivot(0.0F, 20.0F, 0.0F);"
        // Extract pivot point
    }
}
```

**Output**: `assets/xeenaa-alexs-mobs/geo/{mob_name}.geo.json`

**Automation Potential**: ✅ **90% automatable** (only complex models need manual refinement)

---

### Step 4: Convert Animations (15-20 min)

**Goal**: Create `.animation.json` file from Citadel animation code.

**Process**:
1. Read animation helper calls from `setupAnim()` (from Step 2 analysis)
2. For each helper call, convert to GeckoLib keyframes
3. Group animations by state (idle, fly, walk, death)
4. Generate JSON

**Conversion Tool**:
```java
public class CitadelAnimationConverter {

    /**
     * Converts Citadel flap() to GeckoLib rotation keyframes
     */
    public JsonObject convertFlap(String boneName, float speed, float degree,
                                   boolean invert, float offset, float animLength) {
        JsonObject bone = new JsonObject();
        JsonObject rotation = new JsonObject();

        // Sample sine wave at keyframes
        for (float t = 0.0f; t <= animLength; t += animLength / 4) {
            float angle = (float) Math.sin(t * speed * Math.PI * 2 + offset) * degree * 45.8f;
            if (invert) angle *= -1;

            JsonArray rot = new JsonArray();
            rot.add(0);  // X-axis (no rotation)
            rot.add(0);  // Y-axis (no rotation)
            rot.add(angle);  // Z-axis (wing flap)

            rotation.add(String.format("%.2f", t), rot);
        }

        bone.add("rotation", rotation);
        return bone;
    }

    /**
     * Converts Citadel walk() to GeckoLib rotation keyframes
     */
    public JsonObject convertWalk(String boneName, float speed, float degree,
                                   boolean invert, float offset, float animLength) {
        // Similar to flap, but rotate on X-axis (pitch)
    }

    /**
     * Converts Citadel swing() to GeckoLib rotation keyframes
     */
    public JsonObject convertSwing(String boneName, float speed, float degree,
                                    boolean invert, float offset, float animLength) {
        // Similar to flap, but rotate on Y-axis (yaw)
    }

    /**
     * Generates complete animation JSON from analysis
     */
    public JsonObject generateAnimationJson(AnimationAnalysis analysis) {
        JsonObject root = new JsonObject();
        root.addProperty("format_version", "1.8.0");

        JsonObject animations = new JsonObject();

        // Generate "idle" animation
        JsonObject idle = new JsonObject();
        idle.addProperty("loop", true);
        idle.addProperty("animation_length", 2.0);
        idle.add("bones", generateIdleBones(analysis));
        animations.add("idle", idle);

        // Generate "fly" animation (if flying mob)
        if (analysis.hasFlying()) {
            JsonObject fly = new JsonObject();
            fly.addProperty("loop", true);
            fly.addProperty("animation_length", 1.0);
            fly.add("bones", generateFlyBones(analysis));
            animations.add("fly", fly);
        }

        // Generate "death" animation
        JsonObject death = new JsonObject();
        death.addProperty("loop", false);
        death.addProperty("animation_length", 1.5);
        death.add("bones", generateDeathBones(analysis));
        animations.add("death", death);

        root.add("animations", animations);
        return root;
    }
}
```

**Usage**:
```java
// From Step 2 analysis
AnimationAnalysis flyAnalysis = new AnimationAnalysis()
    .addFlap("left_wing", 1.82f, 0.8f, true, 0.0f, 0.2f)
    .addFlap("right_wing", 1.82f, 0.8f, false, 0.0f, 0.2f)
    .addWalk("mouth", 0.28f, 0.08f, false, -1.0f, 0.2f)
    .addWalk("legs", 0.28f, 0.16f, false, 1.0f, 0.2f);

// Convert to GeckoLib
JsonObject animJson = converter.generateAnimationJson(flyAnalysis);

// Write to file
Files.writeString(
    Path.of("assets/.../animations/fly.animation.json"),
    new GsonBuilder().setPrettyPrinting().create().toJson(animJson)
);
```

**Output**: `assets/xeenaa-alexs-mobs/animations/{mob_name}.animation.json`

**Automation Potential**: ✅ **90% automatable** (only complex custom animations need manual work)

---

### Step 5: Generate Entity Code (2-5 min)

**Goal**: Create `{MobName}Entity.java` from template.

**Process**:
1. Define mob specification (name, base class, attributes, AI goals)
2. Apply template with string replacement
3. Write to file

**Tool**:
```java
public class EntityCodeGenerator {

    private static final String TEMPLATE = """
    package com.canya.xeenaa_alexs_mobs.entity.animal;

    import {IMPORTS}

    public class {MOB_NAME}Entity extends {BASE_CLASS} implements GeoEntity {

        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public {MOB_NAME}Entity(EntityType<? extends {BASE_CLASS}> entityType, World world) {
            super(entityType, world);
        }

        public static DefaultAttributeContainer.Builder createAttributes() {
            return {BASE_CLASS}.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, {HEALTH}D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, {SPEED}D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, {FLYING_SPEED}D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, {FOLLOW_RANGE}D);
        }

        @Override
        protected void registerCustomGoals() {
            {GOALS}
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(this, "{MOB_NAME}Controller", 0, this::animationPredicate));
        }

        private PlayState animationPredicate(AnimationState<{MOB_NAME}Entity> state) {
            AnimationController<{MOB_NAME}Entity> controller = state.getController();

            if (this.isDead()) {
                controller.setAnimation(RawAnimation.begin().then("death", Animation.LoopType.PLAY_ONCE));
                return PlayState.CONTINUE;
            }

            if ({MOVEMENT_CONDITION}) {
                controller.setAnimation(RawAnimation.begin().then("{MOVEMENT_ANIM}", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }

        @Nullable
        @Override
        public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
            return ModEntities.{MOB_NAME_UPPER}.create(world);
        }

        @Override
        public boolean isBreedingItem(ItemStack stack) {
            return false;
        }

        @Nullable
        @Override
        protected SoundEvent getAmbientSound() {
            return ModSounds.{MOB_NAME_UPPER}_AMBIENT;
        }

        @Nullable
        @Override
        protected SoundEvent getHurtSound(DamageSource source) {
            return ModSounds.{MOB_NAME_UPPER}_HURT;
        }

        @Nullable
        @Override
        protected SoundEvent getDeathSound() {
            return ModSounds.{MOB_NAME_UPPER}_DEATH;
        }
    }
    """;

    public void generate(MobSpec spec) {
        String code = TEMPLATE
            .replace("{MOB_NAME}", spec.name)
            .replace("{MOB_NAME_UPPER}", spec.name.toUpperCase())
            .replace("{BASE_CLASS}", spec.baseClass)
            .replace("{HEALTH}", String.valueOf(spec.health))
            .replace("{SPEED}", String.valueOf(spec.speed))
            .replace("{FLYING_SPEED}", String.valueOf(spec.flyingSpeed))
            .replace("{FOLLOW_RANGE}", String.valueOf(spec.followRange))
            .replace("{GOALS}", generateGoalsCode(spec.goals))
            .replace("{MOVEMENT_CONDITION}", spec.movementCondition)
            .replace("{MOVEMENT_ANIM}", spec.movementAnimation)
            .replace("{IMPORTS}", generateImports(spec));

        Path output = Path.of("src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/" + spec.name + "Entity.java");
        Files.writeString(output, code);
    }

    private String generateGoalsCode(List<Goal> goals) {
        StringBuilder sb = new StringBuilder();
        for (Goal goal : goals) {
            sb.append(String.format(
                "this.goalSelector.add(%d, new %s(%s));\n",
                goal.priority, goal.className, goal.arguments
            ));
        }
        return sb.toString();
    }
}
```

**Usage**:
```java
MobSpec fly = new MobSpec(
    name: "Fly",
    baseClass: "FlyingAnimalEntity",
    health: 2.0,
    speed: 0.6,
    flyingSpeed: 0.8,
    followRange: 8.0,
    goals: List.of(
        new Goal(0, "WanderInAirGoal", "this, 0.3D, 7.0D"),
        new Goal(1, "LandOnBlockGoal", "this"),
        new Goal(2, "FleeEntityGoal<PlayerEntity>", "this, PlayerEntity.class, 4.0F, 0.4D, 0.4D")
    ),
    movementCondition: "!this.isOnGround() && state.isMoving()",
    movementAnimation: "fly"
);

EntityCodeGenerator generator = new EntityCodeGenerator();
generator.generate(fly);
```

**Output**: `src/main/java/.../entity/animal/FlyEntity.java`

**Automation Potential**: ✅ **95% automatable** (only unique behaviors need manual additions)

---

### Step 6: Generate Model/Renderer Code (1-2 min)

**Goal**: Create `{MobName}Model.java` and `{MobName}Renderer.java`.

**Process**: Pure template with string replacement (100% mechanical).

**Tool**:
```java
public class ModelRendererGenerator {

    public void generate(String mobName) {
        generateModel(mobName);
        generateRenderer(mobName);
    }

    private void generateModel(String mobName) {
        String code = """
        package com.canya.xeenaa_alexs_mobs.client.model;

        import com.canya.xeenaa_alexs_mobs.entity.animal.{MOB_NAME}Entity;
        import net.minecraft.util.Identifier;
        import software.bernie.geckolib.model.GeoModel;

        public class {MOB_NAME}Model extends GeoModel<{MOB_NAME}Entity> {
            @Override
            public Identifier getModelResource({MOB_NAME}Entity entity) {
                return Identifier.of("xeenaa-alexs-mobs", "geo/{MOB_NAME_LOWER}.geo.json");
            }

            @Override
            public Identifier getTextureResource({MOB_NAME}Entity entity) {
                return Identifier.of("xeenaa-alexs-mobs", "textures/entity/{MOB_NAME_LOWER}/{MOB_NAME_LOWER}.png");
            }

            @Override
            public Identifier getAnimationResource({MOB_NAME}Entity entity) {
                return Identifier.of("xeenaa-alexs-mobs", "animations/{MOB_NAME_LOWER}.animation.json");
            }
        }
        """.replace("{MOB_NAME}", mobName).replace("{MOB_NAME_LOWER}", mobName.toLowerCase());

        Path output = Path.of("src/main/java/com/canya/xeenaa_alexs_mobs/client/model/" + mobName + "Model.java");
        Files.writeString(output, code);
    }

    private void generateRenderer(String mobName) {
        String code = """
        package com.canya.xeenaa_alexs_mobs.client.renderer;

        import com.canya.xeenaa_alexs_mobs.client.model.{MOB_NAME}Model;
        import com.canya.xeenaa_alexs_mobs.entity.animal.{MOB_NAME}Entity;
        import net.minecraft.client.render.entity.EntityRendererFactory;
        import software.bernie.geckolib.renderer.GeoEntityRenderer;

        public class {MOB_NAME}Renderer extends GeoEntityRenderer<{MOB_NAME}Entity> {
            public {MOB_NAME}Renderer(EntityRendererFactory.Context context) {
                super(context, new {MOB_NAME}Model());
            }
        }
        """.replace("{MOB_NAME}", mobName);

        Path output = Path.of("src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/" + mobName + "Renderer.java");
        Files.writeString(output, code);
    }
}
```

**Output**:
- `src/main/java/.../client/model/{MobName}Model.java`
- `src/main/java/.../client/renderer/{MobName}Renderer.java`

**Automation Potential**: ✅ **100% automatable** (zero variation)

---

### Step 7: Generate Registration Code (1-2 min)

**Goal**: Generate code snippets to add to `ModEntities.java`, `ModItems.java`, `ModSounds.java`, `AlexsMobsClient.java`.

**Process**: Template-based string generation.

**Tool**:
```java
public class RegistrationCodeGenerator {

    public RegistrationSnippets generate(MobSpec spec) {
        return new RegistrationSnippets(
            entityRegistration: generateEntityRegistration(spec),
            spawnEggRegistration: generateSpawnEggRegistration(spec),
            soundRegistrations: generateSoundRegistrations(spec),
            rendererRegistration: generateRendererRegistration(spec)
        );
    }

    private String generateEntityRegistration(MobSpec spec) {
        return """
        public static final EntityType<{MOB_NAME}Entity> {MOB_NAME_UPPER} = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("xeenaa-alexs-mobs", "{MOB_NAME_LOWER}"),
            EntityType.Builder.create({MOB_NAME}Entity::new, SpawnGroup.{SPAWN_GROUP})
                .dimensions({WIDTH}f, {HEIGHT}f)
                .maxTrackingRange(8)
                .build()
        );
        """.replace("{MOB_NAME}", spec.name)
           .replace("{MOB_NAME_UPPER}", spec.name.toUpperCase())
           .replace("{MOB_NAME_LOWER}", spec.name.toLowerCase())
           .replace("{SPAWN_GROUP}", spec.spawnGroup)
           .replace("{WIDTH}", String.valueOf(spec.width))
           .replace("{HEIGHT}", String.valueOf(spec.height));
    }

    private String generateSpawnEggRegistration(MobSpec spec) {
        return """
        public static final Item {MOB_NAME_UPPER}_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of("xeenaa-alexs-mobs", "{MOB_NAME_LOWER}_spawn_egg"),
            new SpawnEggItem(ModEntities.{MOB_NAME_UPPER}, {PRIMARY_COLOR}, {SECONDARY_COLOR}, new Item.Settings())
        );
        """.replace("{MOB_NAME_UPPER}", spec.name.toUpperCase())
           .replace("{MOB_NAME_LOWER}", spec.name.toLowerCase())
           .replace("{PRIMARY_COLOR}", spec.eggPrimaryColor)
           .replace("{SECONDARY_COLOR}", spec.eggSecondaryColor);
    }

    private String generateSoundRegistrations(MobSpec spec) {
        String upper = spec.name.toUpperCase();
        String lower = spec.name.toLowerCase();

        return """
        public static final SoundEvent {UPPER}_AMBIENT = registerSound("entity.{LOWER}.ambient");
        public static final SoundEvent {UPPER}_HURT = registerSound("entity.{LOWER}.hurt");
        public static final SoundEvent {UPPER}_DEATH = registerSound("entity.{LOWER}.death");
        """.replace("{UPPER}", upper).replace("{LOWER}", lower);
    }

    private String generateRendererRegistration(MobSpec spec) {
        return "EntityRendererRegistry.register(ModEntities.{UPPER}, {NAME}Renderer::new);"
            .replace("{UPPER}", spec.name.toUpperCase())
            .replace("{NAME}", spec.name);
    }
}
```

**Usage**:
```java
RegistrationCodeGenerator gen = new RegistrationCodeGenerator();
RegistrationSnippets snippets = gen.generate(fly);

System.out.println("=== Add to ModEntities.java ===");
System.out.println(snippets.entityRegistration);

System.out.println("\n=== Add to ModItems.java ===");
System.out.println(snippets.spawnEggRegistration);

System.out.println("\n=== Add to ModSounds.java ===");
System.out.println(snippets.soundRegistrations);

System.out.println("\n=== Add to AlexsMobsClient.java ===");
System.out.println(snippets.rendererRegistration);
```

**Output**: Code snippets to copy-paste into registry files.

**Automation Potential**: ✅ **100% automatable** (can even auto-insert into files if desired)

---

### Step 8: Copy Assets (5-10 min)

**Goal**: Copy textures, sounds, loot tables from Alex's Mobs to our mod.

**Manual Process**:
1. Find texture: `alexsmobs-1.22.9.jar/assets/alexsmobs/textures/entity/{mob}.png`
2. Copy to: `assets/xeenaa-alexs-mobs/textures/entity/{mob}/{mob}.png`
3. Find sounds: `alexsmobs-1.22.9.jar/assets/alexsmobs/sounds/entity/{mob}/*.ogg`
4. Copy to: `assets/xeenaa-alexs-mobs/sounds/entity/{mob}/*.ogg`
5. Find loot table: `alexsmobs-1.22.9.jar/data/alexsmobs/loot_tables/entities/{mob}.json`
6. Copy to: `data/xeenaa-alexs-mobs/loot_tables/entities/{mob}.json`

**Semi-Automated Process**:
```bash
#!/bin/bash
# Script to batch-copy assets for a mob

MOB_NAME="$1"
MOB_LOWER=$(echo "$MOB_NAME" | tr '[:upper:]' '[:lower:]')

# Extract texture
unzip -p alexsmobs-1.22.9.jar "assets/alexsmobs/textures/entity/${MOB_LOWER}.png" \
  > "assets/xeenaa-alexs-mobs/textures/entity/${MOB_LOWER}/${MOB_LOWER}.png"

# Extract sounds
for sound in ambient hurt death; do
  unzip -p alexsmobs-1.22.9.jar "assets/alexsmobs/sounds/entity/${MOB_LOWER}/${sound}.ogg" \
    > "assets/xeenaa-alexs-mobs/sounds/entity/${MOB_LOWER}/${sound}.ogg" 2>/dev/null
done

# Extract loot table
unzip -p alexsmobs-1.22.9.jar "data/alexsmobs/loot_tables/entities/${MOB_LOWER}.json" \
  > "data/xeenaa-alexs-mobs/loot_tables/entities/${MOB_LOWER}.json" 2>/dev/null

echo "Assets copied for $MOB_NAME"
```

**Usage**:
```bash
./copy_assets.sh Fly
./copy_assets.sh Cockroach
./copy_assets.sh Triops
```

**Automation Potential**: ✅ **90% automatable** (only missing/renamed assets need manual finding)

---

### Step 9: Test and Validate (10-15 min)

**Goal**: Verify mob works correctly in-game.

**Testing Checklist**:

**Visual**:
- [ ] Model geometry appears correct (not corrupted)
- [ ] Texture maps correctly (no missing texture pink/black)
- [ ] Size is appropriate (matches original or specifications)
- [ ] No Z-fighting or visual glitches

**Animations**:
- [ ] Idle animation plays when stationary
- [ ] Movement animation plays when moving
- [ ] Death animation plays on death
- [ ] Animations loop correctly
- [ ] Animation speed feels natural
- [ ] Transitions are smooth

**AI/Behavior**:
- [ ] Spawns successfully via spawn egg
- [ ] Wanders/moves naturally (not stuck)
- [ ] Pathfinding works (doesn't get stuck in walls)
- [ ] Special behaviors work (flying, swimming, burrowing, etc.)
- [ ] Flees from player when approached (if applicable)
- [ ] Interacts with environment correctly

**Sounds**:
- [ ] Ambient sound plays occasionally
- [ ] Hurt sound plays when damaged
- [ ] Death sound plays on death
- [ ] Volume is appropriate (not too loud/quiet)

**Mechanics**:
- [ ] Health value correct (dies in expected hits)
- [ ] Loot drops on death (if applicable)
- [ ] No console errors or warnings

**Performance**:
- [ ] Spawn 20 mobs - acceptable FPS
- [ ] No memory leaks
- [ ] No lag spikes

**If all pass**: ✅ Mob complete, move to next mob
**If issues found**: Debug and fix, retest

**Automation Potential**: ❌ **Manual** (requires human evaluation)

---

## 3. Mob Taxonomy (Grouping for Efficiency)

### Purpose

Group 90 mobs by **type** to identify shared patterns and optimize conversion order.

### Taxonomy Categories

#### Category A: Flying Animals (Priority: High)

**Base Class**: `FlyingAnimalEntity`
**AI Goals**: `WanderInAirGoal`, `LandOnBlockGoal`, `FleeEntityGoal`
**Animation Pattern**: Wings flap when flying, fold when landed

**Mobs** (13):
1. Fly ✅ (DONE)
2. Hummingbird
3. Blue Jay
4. Toucan
5. Potoo
6. Parrot variant
7. Crow
8. Roadrunner (partial flying)
9. Seagull
10. Warped Toad (jumps/glides)
11. Mimicube (floating)
12. Stradpole (tadpole flight?)
13. Gelada Monkey (climbing, not flying)

**Conversion Strategy**:
- Use Fly as template
- Most will have similar wing flap animations
- Vary speed and degree parameters
- Some have ground behaviors (roadrunner, crow)

**Estimated Time**: 45-60 min per mob × 12 = **9-12 hours**

---

#### Category B: Aquatic Animals (Priority: High)

**Base Class**: `AquaticAnimalEntity`
**AI Goals**: `SwimAroundGoal`, `FleeEntityGoal`, optional `BurrowInBlockGoal`
**Animation Pattern**: Fins oscillate, tail swings

**Mobs** (15):
1. Triops (planned in Epic 03)
2. Lobster
3. Mudskipper (amphibious)
4. Platypus
5. Comb Jelly
6. Devil's Hole Pupfish
7. Frilled Shark
8. Giant Squid
9. Hammerhead Shark
10. Mimic Octopus
11. Mantis Shrimp
12. Blobfish
13. Catfish
14. Cachalot Whale
15. Orca

**Conversion Strategy**:
- Use Triops as template (once complete)
- Most have tail + fin animations
- Vary swimming speeds (whale slow, squid fast)
- Some have special abilities (octopus mimicry, whale diving)

**Estimated Time**: 45-60 min per mob × 15 = **11-15 hours**

---

#### Category C: Land Passive Animals (Priority: Medium)

**Base Class**: `BaseAnimalEntity`
**AI Goals**: `WanderAroundGoal`, `FleeEntityGoal`, `LookAroundGoal`
**Animation Pattern**: Legs walk, body bobs

**Mobs** (20):
1. Cockroach (planned in Epic 03)
2. Jerboa
3. Banana Slug
4. Gazelle
5. Capuchin Monkey
6. Kangaroo
7. Elephant
8. Terrapin
9. Bison
10. Mungus
11. Moose
12. Rhinoceros
13. Sugar Glider
14. Snow Leopard
15. Tasmanian Devil
16. Anteater
17. Sunbird
18. Cosmaw
19. Froststalker (wolf-like)
20. Laviathan (lava creature)

**Conversion Strategy**:
- Use Cockroach as template for insects
- Quadrupeds (4 legs): Most common pattern
- Bipeds (2 legs): Kangaroo, monkey, jerboa
- Vary leg count and animation speed

**Estimated Time**: 45-60 min per mob × 20 = **15-20 hours**

---

#### Category D: Aggressive/Hostile Animals (Priority: Medium)

**Base Class**: `BaseAnimalEntity` (with attack goals)
**AI Goals**: `WanderAroundGoal`, `MeleeAttackGoal`, `LookAtEntityGoal`
**Animation Pattern**: Walk + attack animation

**Mobs** (12):
1. Crocodile
2. Komodo Dragon
3. Alligator Snapping Turtle
4. Grizzly Bear
5. Tiger
6. Gorilla
7. Warped Mosco
8. Cave Centipede
9. Bone Serpent
10. Crimson Mosquito
11. Endergrade
12. Void Worm (boss)

**Conversion Strategy**:
- Similar to passive land animals + attack animation
- Some have special attacks (poison, knockback)
- Boss mobs (Void Worm) need extra attention

**Estimated Time**: 60-90 min per mob × 12 = **12-18 hours**

---

#### Category E: Complex/Unique Animals (Priority: Low)

**Base Class**: Varies (may need custom base classes)
**AI Goals**: Custom/unique behaviors
**Animation Pattern**: Complex multi-state animations

**Mobs** (10):
1. Mantis (multi-limbed)
2. Straddler (tall, spider-like)
3. Rattlesnake (snake slither)
4. Anaconda (large snake)
5. Flutter (flying insect with complex wings)
6. Spectre (ghost-like floating)
7. Soul Vulture (flying + hovering)
8. Skreecher (sound-based attack)
9. Tusklin (pig-like with tusks)
10. Underminer (digging behavior)

**Conversion Strategy**:
- Leave for last (Epic 07+)
- Require manual modeling in Blockbench
- Complex animations need keyframe authoring
- May need custom AI goals

**Estimated Time**: 90-120 min per mob × 10 = **15-20 hours**

---

#### Category F: Ambient/Decorative (Priority: Medium)

**Base Class**: `BaseAnimalEntity` (minimal AI)
**AI Goals**: `WanderAroundGoal` or static
**Animation Pattern**: Idle animation only (or minimal movement)

**Mobs** (10):
1. Flutter
2. Leafcutter Ant
3. Cosmic Cod
4. Rainbow Jelly
5. Seagull (ambient bird)
6. Terrapin (slow turtle)
7. Bunfungus (mushroom creature)
8. Enderiophage (ambient parasite)
9. Guster (sandstorm creature)
10. Mudskipper (beach ambient)

**Conversion Strategy**:
- Simplest category (minimal AI, simple animations)
- Use for practice/testing
- Good for filling out mod quickly

**Estimated Time**: 30-45 min per mob × 10 = **5-7.5 hours**

---

### Taxonomy Summary

| Category | Count | Base Class | Complexity | Estimated Time |
|----------|-------|-----------|------------|----------------|
| **A: Flying** | 13 | FlyingAnimalEntity | Medium | 9-12 hours |
| **B: Aquatic** | 15 | AquaticAnimalEntity | Medium | 11-15 hours |
| **C: Land Passive** | 20 | BaseAnimalEntity | Low-Medium | 15-20 hours |
| **D: Aggressive** | 12 | BaseAnimalEntity | Medium-High | 12-18 hours |
| **E: Complex** | 10 | Custom | High | 15-20 hours |
| **F: Ambient** | 10 | BaseAnimalEntity | Low | 5-7.5 hours |
| **TOTAL** | **80** | - | - | **67-92.5 hours** |

**Note**: Original plan was 90 mobs, but some may be duplicates or variants. Adjust as needed.

---

### Recommended Conversion Order

**Epic 03** (Proof of Concept): 3 mobs
1. Fly (flying) ✅ DONE
2. Cockroach (land)
3. Triops (aquatic)

**Epic 04-06** (Simple Mobs): 30-40 mobs
- Start with Category F (ambient/decorative) - easiest
- Then Category A (flying) - use Fly template
- Then Category B (aquatic) - use Triops template
- Then Category C (land passive) - use Cockroach template

**Epic 07-10** (Complex Mobs): 30-40 mobs
- Category D (aggressive) - add attack animations
- Category E (complex) - manual Blockbench work

**Epic 11-15** (Polish): Quality improvements
- Revisit early mobs with improved tools
- Manual refinements in Blockbench
- Performance optimization

---

## 4. Tool Implementation Roadmap

### Phase 1: Epic 03 (Manual Process Documentation)

**Goal**: Document manual workflow with 3 mobs (Fly, Cockroach, Triops).

**Deliverables**:
- ✅ Fly conversion complete (validated by user)
- Cockroach conversion (land animal template)
- Triops conversion (aquatic animal template)
- Document pain points and automation opportunities

**Time**: 3-5 hours (already mostly done)

---

### Phase 2: Epic 04 (Build Basic Automation Tools)

**Goal**: Automate repetitive tasks (code generation, registration).

**Tools to Build**:
1. **EntityCodeGenerator**: Template-based entity class generation
2. **ModelRendererGenerator**: Template-based model/renderer generation
3. **RegistrationCodeGenerator**: Registration snippet generation
4. **AssetCopyScript**: Bash script to batch-copy assets

**Deliverables**:
- 4 tools implemented (Java or Python)
- Test on 5-8 simple mobs (Category F - ambient)
- Measure time savings vs manual process

**Time**: 5-10 hours (tool development) + 3-6 hours (testing on 5-8 mobs) = **8-16 hours total**

---

### Phase 3: Epic 05-06 (Build Advanced Conversion Tools)

**Goal**: Automate model/animation conversion.

**Tools to Build**:
1. **CitadelModelParser**: Parse decompiled Java → extract bone hierarchy and cubes
2. **CitadelAnimationConverter**: Convert procedural animations to keyframes
3. **GeoJsonGenerator**: Generate `.geo.json` from parsed model
4. **AnimationJsonGenerator**: Generate `.animation.json` from converted animations

**Deliverables**:
- 4 advanced tools implemented
- Test on 15-20 mobs (Categories A, B, C)
- Achieve 45-60 min per mob target

**Time**: 10-15 hours (tool development) + 12-20 hours (testing on 15-20 mobs) = **22-35 hours total**

---

### Phase 4: Epic 07-10 (Refinement and Complex Mobs)

**Goal**: Handle edge cases and complex mobs.

**Focus**:
- Manual Blockbench modeling for complex mobs (Category E)
- Refine automated tools based on real-world usage
- Add support for custom animations (attack, special abilities)

**Deliverables**:
- All remaining mobs converted (Categories D, E)
- Tools mature and reliable
- Documentation of patterns and edge cases

**Time**: 30-40 hours (20-30 mobs + tool refinements)

---

### Phase 5: Epic 11-15 (Polish and Optimization)

**Goal**: Improve visual quality and performance.

**Focus**:
- Revisit early mobs with Blockbench (improve geometry)
- Refine animations (smoother keyframes, better timing)
- Performance profiling and optimization
- User feedback and iteration

**Time**: 20-30 hours (quality improvements)

---

### Total Framework Development Time

**Tool Development**: 15-25 hours
**Testing/Validation**: 15-26 hours
**Mob Conversions**: 67-92.5 hours
**Polish/Refinement**: 20-30 hours

**Grand Total**: **117-173.5 hours** for all 90 mobs + framework

**Without Framework**: **270-450 hours** (3-5h per mob × 90)

**Time Saved**: **153-276.5 hours** (56-61% reduction!)

---

## 5. Success Metrics

### Conversion Speed

**Target**: 45-60 minutes per mob (with automation)
**Baseline**: 3-5 hours per mob (manual Blockbench)

**Metric**: Average conversion time over 10 mobs
**Success Criteria**: ≤ 60 min average

---

### Visual Quality

**Target**: "Looks accurate" or "quite accurate" (user feedback from Fly)
**Baseline**: Original Alex's Mobs quality

**Metric**: User subjective rating (1-10 scale)
**Success Criteria**: ≥ 7/10 average

---

### Animation Smoothness

**Target**: Animations play correctly and smoothly
**Baseline**: Citadel's procedural animations

**Metric**: User feedback + technical validation (no stuttering)
**Success Criteria**: All animations play without errors

---

### Code Quality

**Target**: Generated code follows coding standards
**Baseline**: Hand-written code from Epic 01

**Metric**: Code review checklist (Javadoc, null safety, etc.)
**Success Criteria**: 100% compliance with coding-standards skill

---

### Performance

**Target**: 20 mobs on screen = stable 60 FPS
**Baseline**: Vanilla Minecraft passive mob performance

**Metric**: F3 debug screen FPS measurement
**Success Criteria**: ≥ 60 FPS with 20 mobs

---

## 6. Conclusion

### What We've Learned

**From Fly Conversion**:
- Citadel → GeckoLib conversion is viable (user approved)
- ~80% of work is mechanical and automatable
- Visual quality is acceptable with direct conversion
- Animation system works (once name matching fixed)

**From Citadel Research**:
- Citadel exists to solve code duplication (library abstraction)
- Procedural animations = performance, keyframes = artist workflow
- Mathematical primitives (`flap`, `walk`, `swing`) are translatable
- State-driven logic maps to animation controllers

---

### Our Framework Benefits

**Time Savings**: 45-60 min per mob vs 3-5 hours (75-80% faster)
**Consistency**: All mobs follow same architecture (no human errors)
**Scalability**: Tools work for all 90 mobs (reusable investment)
**Quality**: Direct conversion preserves original look and feel

---

### Next Steps

1. **Complete Epic 03**: Convert Cockroach and Triops (validate templates)
2. **Build Basic Tools** (Epic 04): Code generators, asset copy script
3. **Build Advanced Tools** (Epic 05-06): Model/animation converters
4. **Batch Convert** (Epic 07-10): Use tools to convert all remaining mobs
5. **Polish** (Epic 11-15): Refine quality and performance

---

**Total Estimated Time**: **117-173.5 hours** for all 90 mobs + framework

**With user approval**: "works perfect, nice" - we have proof the approach works!

---

**END OF CONVERSION FRAMEWORK**

**Ready to convert the next 89 mobs!**
