# Flying Entity Control in Minecraft with GeckoLib

**Last Updated**: 2025-11-01
**Minecraft Version**: 1.21.1 (applicable to 1.18+)
**Platform**: Fabric / Forge / Both
**Tags**: flying-entities, geckolib, entity-rendering, rotation, animation, 3d-transforms, coordinate-system, pitch, yaw, bones

## Overview

Flying entities in Minecraft require careful control of 3D positioning, rotation (yaw, pitch), and animation to create believable aerial movement. Unlike ground-based entities, flying entities must handle:

- **3D coordinate transformations** for smooth aerial movement
- **Multiple rotation axes** (yaw for direction, pitch for vertical tilt)
- **Renderer vs entity rotation** separation to avoid "clock pivoting"
- **GeckoLib bone-based animation** for articulated body parts

This knowledge document provides comprehensive guidance on implementing flying entities with proper rotation, translation, and GeckoLib integration, based on real-world challenges encountered when porting Alex's Mobs flying entities (crows, parrots) to Fabric.

---

## Minecraft's Coordinate System and Rotation

### Coordinate Axes

Minecraft uses a **right-handed coordinate system**:

- **X-axis**: West (-) to East (+)
- **Y-axis**: Down (-) to Up (+)
- **Z-axis**: North (-) to South (+)

### Rotation Concepts

Entities have **two primary rotation values** (both in degrees):

#### 1. Yaw (Y-rotation)

- **Definition**: Left/right rotation around the Y-axis (vertical)
- **Range**: -180° to +180° (wraps at boundaries)
- **Direction Convention**:
  - 0° = South (positive Z)
  - -90° = East (positive X)
  - +90° = West (negative X)
  - ±180° = North (negative Z)
- **Usage**: Determines which horizontal direction entity faces
- **Increases**: Clockwise when viewed from above

#### 2. Pitch (X-rotation)

- **Definition**: Up/down tilt around the X-axis (horizontal)
- **Range**: -90° to +90° (clamped, does not wrap)
- **Direction Convention**:
  - 0° = Horizontal
  - -90° = Looking straight up
  - +90° = Looking straight down
- **Usage**: Determines vertical tilt (important for flying entities)
- **Note**: Vanilla Minecraft entities typically **only apply pitch to the head**, not the body

#### 3. Roll (Z-rotation) - NOT NATIVE

- **Important**: Vanilla Minecraft entities **do not natively support roll** rotation
- Roll must be implemented via custom renderer code or bone rotations in models
- Mods like "Do a Barrel Roll" add roll for elytra flight with custom implementation

### Entity vs Renderer Rotation

**Critical Distinction**:

| Rotation Type | Where It's Set | Purpose | Controlled By |
|---------------|----------------|---------|---------------|
| **Entity Rotation** | `entity.setYaw()` / `entity.setPitch()` | Logical direction entity faces | AI, movement, player input |
| **Renderer Rotation** | `poseStack.mulPose()` | Visual orientation adjustments | Renderer class (client-only) |
| **Bone Rotation** | GeckoLib animations | Articulated body parts | Animation files + controller |

**Best Practice**: Use **entity rotation** for direction, **bone rotation** for body articulation, and **avoid renderer rotations** for orientation.

---

## The Rotation Pivot Problem

### Understanding Pivot Points

When you apply a rotation transformation in the renderer (`poseStack.mulPose()`), the rotation happens **around the model's root pivot point**.

**Example**: Crow Model
- Root pivot: `[0, 24, 0]` (at feet level in GeckoLib units)
- Body pivot: `[0, 21.9, 0]` (slightly above feet)
- Head pivot: `[0, 17, 0.5]` (attached to body)

### The "Clock Pivoting" Problem

**Problem**: When you rotate the entire model in the renderer, it rotates around the root pivot (feet), not the entity's center.

**Visual Result**:
```
Normal Rotation (Bone-Based):          Clock Rotation (Renderer-Based):

    🐦 → 🐦                                  🐦
     ↓    ↓                                   ↓
    🐦 → 🐦                                    🐦
                                              ↙
  (Head turns naturally)                 🐦 ← (Rotates around feet like clock hand)
```

**Why This Happens**:
1. `poseStack.mulPose(Axis.YP.rotationDegrees(180))` rotates **entire model** as rigid object
2. Rotation axis = root pivot at feet
3. Head, body, tail all rotate together around feet pivot
4. Result: Entity spins like clock hand instead of turning head naturally

**Code Example (WRONG APPROACH)**:
```java
@Override
public void render(CrowEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    poseStack.push();

    // ❌ BAD: Rotates entire model around feet pivot
    poseStack.mulPose(Axis.YP.rotationDegrees(180)); // Fix backwards flight
    poseStack.mulPose(Axis.XP.rotationDegrees(-57.5)); // Fix belly-laying pose
    // Result: Crow spins around feet like clock hand

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

### Correct Approaches

#### Approach 1: Use Entity Rotation (Preferred)

Let entity's `yaw` and `pitch` handle direction:

```java
// In entity AI or movement code (server-side)
entity.setYaw(desiredYaw); // Sets direction entity faces
entity.setPitch(desiredPitch); // Sets vertical tilt (flying entities only)
```

**Why This Works**:
- GeckoLib renderer respects entity yaw automatically
- Bones rotate naturally with animation system
- No "clock pivoting" because entity rotation is logical, not visual

#### Approach 2: Fix Model Orientation (If Model Is Wrong)

If model has incorrect default orientation, edit the model file:

```json
// crow.geo.json
{
  "name": "body",
  "pivot": [0, 21.9, 0],
  "rotation": [57.5, 0, 0], // ← Adjust this if body tilt is wrong
  "cubes": [...]
}
```

**When to Use**:
- Model consistently faces wrong direction in all contexts
- Model's body orientation doesn't match intended pose
- Change affects all instances universally

#### Approach 3: Dynamic Bone Rotation (Advanced)

Use GeckoLib's bone API to adjust bones dynamically during rendering:

```java
@Override
public void render(CrowEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    // Get specific bone and adjust rotation
    GeoBone bodyBone = model.getBone("body").orElse(null);
    if (bodyBone != null) {
        // Adjust pitch based on entity's vertical velocity
        float pitchAdjust = (float)entity.getVelocity().y * 20.0f;
        bodyBone.setRotX(bodyBone.getRotX() + (float)Math.toRadians(pitchAdjust));
    }

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
}
```

**When to Use**:
- Need dynamic body adjustments based on flight state
- Want pitch to reflect climbing/diving
- Animation files don't provide needed variation

---

## PoseStack Transformations (Scale and Translate Only)

### What PoseStack Is For

`PoseStack` (formerly `MatrixStack` in older versions) is a **transformation matrix stack** for positioning, scaling, and rotating rendered objects.

**Correct Usage**:
- ✅ **Scaling**: Adjust model size (`poseStack.scale()`)
- ✅ **Translation**: Adjust Y-position to prevent clipping (`poseStack.translate()`)
- ❌ **Rotation**: Avoid for entity orientation (use entity yaw/pitch instead)

### Scaling Example

```java
@Override
public void render(CrowEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    poseStack.push();

    // ✅ GOOD: Scale model to appropriate size
    poseStack.scale(0.8f, 0.8f, 0.8f); // 80% of original size

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

### Translation Example

```java
@Override
public void render(FlyEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    poseStack.push();

    // ✅ GOOD: Adjust Y-position to fix model clipping or floating
    poseStack.scale(0.25f, 0.25f, 0.25f); // Small flying insect
    poseStack.translate(0.0, 0.5, 0.0); // Lift model up 0.5 blocks

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

### Rotation Pivot Technique (If Rotation Is Necessary)

If you **must** use renderer rotation, use translate-rotate-translate pattern to rotate around center:

```java
// Only use if entity rotation and bone rotation cannot solve the problem
poseStack.push();

// 1. Translate pivot point to origin
poseStack.translate(0, -modelHeight/2, 0); // Move pivot to model center

// 2. Apply rotation (now rotates around center, not feet)
poseStack.mulPose(Axis.YP.rotationDegrees(180));

// 3. Translate back
poseStack.translate(0, modelHeight/2, 0);

super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
poseStack.pop();
```

**Warning**: This is a last resort. Prefer entity rotation or model fixes.

---

## GeckoLib Bone System and Animation

### Bone Hierarchy and Inheritance

GeckoLib models use a **parent-child bone hierarchy** where:

- **Groups are bones** (the only animatable elements)
- **Pivots are joints** (rotation points)
- **Cubes are flesh** (visual geometry, must be inside groups)

**Key Rule**: Child bones **inherit parent rotation**.

**Example Hierarchy**:
```
Root (pivot: [0, 24, 0])
 └─ Body (pivot: [0, 21.9, 0], rotation: [57.5°, 0, 0])
     ├─ Head (pivot: [0, 17, 0.5])
     │   ├─ Beak
     │   └─ Eyes
     ├─ Left Wing (pivot: [-3, 21, 1])
     ├─ Right Wing (pivot: [3, 21, 1])
     └─ Tail (pivot: [0, 21.8, 3], rotation: [-7.5°, 0, 0])
```

**Rotation Inheritance**: If body rotates 57.5° around X-axis:
- Head **also** rotates 57.5° (inherited)
- Wings **also** rotate 57.5° (inherited)
- Tail **adds** its own -7.5° rotation **on top of** inherited 57.5°

**Animation Challenge**: When animating child bones, you're "fighting" parent rotations. If body is tilted 57.5° forward, animating head to look up requires accounting for that tilt.

### Animation Controller Patterns for Flying Entities

#### Basic Flight Animation

```java
@Override
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, "controller", 5, this::animationPredicate));
}

private PlayState animationPredicate(AnimationState<FlyingEntity> state) {
    AnimationController<FlyingEntity> controller = state.getController();

    // Flying: entity is airborne
    if (!this.isOnGround()) {
        controller.setAnimation(RawAnimation.begin()
            .thenLoop("animation.entity.fly"));
        return PlayState.CONTINUE;
    }

    // Idle: entity is grounded and not moving
    if (state.getController().getAnimatable().getVelocity().horizontalLength() < 0.01) {
        controller.setAnimation(RawAnimation.begin()
            .thenLoop("animation.entity.idle"));
        return PlayState.CONTINUE;
    }

    // Walk: entity is grounded and moving
    controller.setAnimation(RawAnimation.begin()
        .thenLoop("animation.entity.walk"));
    return PlayState.CONTINUE;
}
```

#### Advanced: Multiple Controllers

For complex entities with concurrent animations (e.g., flying + head tracking):

```java
@Override
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    // Body/flight controller
    controllers.add(new AnimationController<>(this, "movement", 5, this::movementPredicate));

    // Head tracking controller (independent)
    controllers.add(new AnimationController<>(this, "head", 0, this::headPredicate));
}

private PlayState headPredicate(AnimationState<FlyingEntity> state) {
    // Head tracks nearest player
    // This controller runs concurrently with movement controller
    // Later controllers have priority for bone conflicts
    return PlayState.CONTINUE;
}
```

**Transition Length**: The second parameter (`5` in examples) is transition time in ticks:
- `0` = Instant snap (no blending)
- `5` = Smooth 0.25-second blend (recommended)
- `10` = Slow 0.5-second blend

---

## Entity Rotation Management (LivingEntity)

### Key Rotation Fields

Minecraft's `LivingEntity` uses **four rotation variables**:

| Field | Purpose | Updated By |
|-------|---------|------------|
| `yaw` | Entity's horizontal direction | Movement, AI |
| `pitch` | Entity's vertical tilt | Movement, AI (flying entities) |
| `headYaw` | Head's horizontal rotation | `EntityLookHelper` (AI) |
| `renderYawOffset` | Body rendering direction | `EntityBodyHelper` (rendering) |

### Rotation Helpers

#### EntityLookHelper (Server-Side AI)

Controls head rotation to look at targets:

```java
// In custom AI goal
LookHelper lookHelper = entity.getLookHelper();
lookHelper.lookAt(targetEntity); // Head tracks target
```

#### EntityBodyHelper (Rendering)

Gradually rotates body to match head direction when stationary:

```java
// Automatic - no code needed
// Body slowly turns to match head when not moving
// Body instantly aligns with movement direction when moving
```

### Smooth Rotation (Interpolation)

Minecraft interpolates rotation between ticks for smooth visuals:

```java
// In LivingEntity
public void updateTrackedHeadRotation(float yaw, int interpolationSteps) {
    // Smoothly interpolates head yaw over multiple frames
}

public void updateTrackedPositionAndAngles(double x, double y, double z,
                                           float yaw, float pitch,
                                           int interpolationSteps,
                                           boolean interpolate) {
    // Smoothly interpolates position and rotation
}
```

**Key Insight**: Rotation changes are interpolated over `interpolationSteps` ticks to avoid jerky movement.

### Flying Entity Pitch Control

For flying entities to tilt dynamically based on vertical movement:

```java
@Override
public void tick() {
    super.tick();

    // Adjust pitch based on vertical velocity
    double verticalVelocity = this.getVelocity().y;

    // Pitch calculation: upward velocity = negative pitch (looking up)
    float desiredPitch = (float)(-verticalVelocity * 30.0); // Scale factor
    desiredPitch = MathHelper.clamp(desiredPitch, -45, 45); // Clamp to reasonable range

    // Smoothly interpolate to desired pitch
    float currentPitch = this.getPitch();
    float newPitch = MathHelper.lerp(0.1f, currentPitch, desiredPitch);
    this.setPitch(newPitch);
}
```

**Result**: Entity naturally tilts up when ascending, down when descending.

---

## Common Issues and Solutions

### Issue 1: Entity Flies Backwards

**Symptom**: Flying entity moves forward but faces backward.

**Root Cause**: Model's default orientation doesn't match entity's forward direction (yaw 0° = south).

**Solutions**:

1. **Preferred**: Fix model orientation in `model.geo.json`:
   ```json
   {
     "name": "body",
     "rotation": [0, 180, 0] // Rotate body 180° on Y-axis
   }
   ```

2. **Alternative**: Adjust entity spawn yaw:
   ```java
   @Override
   public void initialize(...) {
       this.setYaw(this.getYaw() + 180); // Offset by 180°
   }
   ```

### Issue 2: Entity Rotates Around Feet (Clock Pivoting)

**Symptom**: When entity turns, it spins around feet like clock hand instead of turning head naturally.

**Root Cause**: Using renderer rotations (`poseStack.mulPose()`) instead of entity rotation or bone animation.

**Solution**: Remove renderer rotations and use entity yaw:
```java
// ❌ WRONG
poseStack.mulPose(Axis.YP.rotationDegrees(180));

// ✅ RIGHT
// In entity class or AI
entity.setYaw(desiredYaw);
```

### Issue 3: Entity Lays on Belly While Flying

**Symptom**: Flying entity looks horizontal (like lying down) instead of upright.

**Root Cause**: Model's body bone has excessive forward tilt (X-rotation).

**Solutions**:

1. **Check if intentional**: Many flying creatures (birds, insects) DO fly with horizontal bodies when cruising. Compare to real-world references.

2. **Adjust model**: Reduce body X-rotation in `model.geo.json`:
   ```json
   {
     "name": "body",
     "rotation": [30, 0, 0] // Reduce from 57.5° to 30°
   }
   ```

3. **Dynamic adjustment**: Use bone API to adjust pitch during flight:
   ```java
   GeoBone bodyBone = model.getBone("body").orElse(null);
   if (bodyBone != null && !entity.isOnGround()) {
       // Tilt body up 20° when flying
       bodyBone.setRotX(bodyBone.getRotX() + (float)Math.toRadians(20));
   }
   ```

### Issue 4: Entity Tilts Left/Right Unexpectedly

**Symptom**: Flying entity is not level horizontally; appears to tilt to one side.

**Root Cause**: Model has unintended Z-rotation (roll) on body or root bone.

**Solution**: Check model for Z-rotation values:
```json
{
  "name": "body",
  "rotation": [57.5, 0, 0] // Z should be 0 for level flight
}
```

**Alternative**: Counter with renderer rotation (last resort):
```java
poseStack.mulPose(Axis.ZP.rotationDegrees(-angleToCounter));
```

### Issue 5: Animation Looks Wrong When Flying Up/Down

**Symptom**: Flight animation doesn't adapt to climbing or diving.

**Root Cause**: Pitch changes affect visual appearance, but animation stays the same.

**Solution**: Use animation state to vary animations based on pitch:
```java
private PlayState animationPredicate(AnimationState<FlyingEntity> state) {
    float pitch = this.getPitch();

    if (!this.isOnGround()) {
        if (pitch < -20) {
            // Ascending steeply - use climb animation
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.entity.fly_climb"));
        } else if (pitch > 20) {
            // Descending steeply - use dive animation
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.entity.fly_dive"));
        } else {
            // Level flight - use normal fly animation
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.entity.fly"));
        }
        return PlayState.CONTINUE;
    }
    // ... other states
}
```

---

## Best Practices

### DO:

1. **Use entity rotation for direction**:
   - Set `entity.setYaw()` for turning
   - Set `entity.setPitch()` for vertical tilt (flying entities)

2. **Use GeckoLib bones for articulation**:
   - Head turning
   - Wing flapping
   - Tail movement
   - Body bending

3. **Use PoseStack for scale and position**:
   - `poseStack.scale()` to resize model
   - `poseStack.translate()` to fix Y-position clipping

4. **Test with F3+B hitbox overlay**:
   - Verify entity actually spawns
   - Check if hitbox matches visual model

5. **Use smooth interpolation**:
   - Gradually change pitch/yaw over multiple ticks
   - Use `MathHelper.lerp()` for smooth transitions

### DON'T:

1. **Don't use renderer rotations for orientation**:
   - Causes "clock pivoting" around root pivot
   - Breaks natural bone animation

2. **Don't fight GeckoLib's bone system**:
   - If bones rotate naturally with entity yaw, don't override
   - Let animation controllers handle bone state

3. **Don't assume roll support**:
   - Vanilla Minecraft doesn't support roll
   - Requires custom implementation if needed

4. **Don't over-rotate in model file**:
   - Keep body rotations reasonable (<45° for most cases)
   - Test rotation impact on AI pathfinding

5. **Don't skip version testing**:
   - Rotation handling changed in 1.17+ (MatrixStack → PoseStack)
   - Test on target Minecraft version

---

## Implementation Checklist

When implementing a flying entity:

- [ ] **Entity Class**:
  - [ ] Extends appropriate base (FlyingAnimalEntity, PathAwareEntity, etc.)
  - [ ] Implements GeoEntity
  - [ ] Has flight AI goals (WanderInAirGoal, LandOnBlockGoal, etc.)
  - [ ] Updates pitch based on vertical velocity (if dynamic pitch desired)

- [ ] **Model (Blockbench → GeckoLib)**:
  - [ ] All cubes inside groups (bones)
  - [ ] Root pivot at feet or appropriate base point
  - [ ] Body rotation reasonable (<45° X-rotation typical)
  - [ ] No unintended Z-rotation (roll) on main bones
  - [ ] Exported as GeckoLib model

- [ ] **Animations**:
  - [ ] Idle animation (subtle movements)
  - [ ] Fly animation (wing flapping, body posture)
  - [ ] Walk animation (if entity can land)
  - [ ] Animation controller checks `isOnGround()` for state switching

- [ ] **Renderer**:
  - [ ] Extends `GeoEntityRenderer<YourEntity>`
  - [ ] Passes model to super constructor
  - [ ] Uses `poseStack.scale()` for size adjustments
  - [ ] Uses `poseStack.translate()` for Y-position fixes
  - [ ] Avoids `poseStack.mulPose()` for orientation
  - [ ] Registered in client initializer

- [ ] **Testing**:
  - [ ] Entity spawns correctly (F3+B to check hitbox)
  - [ ] Flies in correct direction (not backwards)
  - [ ] Turns naturally (not clock pivoting)
  - [ ] Body orientation looks correct (not laying on belly)
  - [ ] Animation plays correctly when flying/walking
  - [ ] No Z-fighting or texture flickering

---

## Version Compatibility

### Minecraft 1.18+

- `PoseStack` is standard (replaced `MatrixStack`)
- `EntityRendererProvider.Context` for renderer constructor
- Modern entity attribute system
- GeckoLib 3.1+ recommended

### Minecraft 1.17

- Transitional version (`MatrixStack` → `PoseStack`)
- Some methods still use old names

### Minecraft 1.16 and Earlier

- Uses `MatrixStack` instead of `PoseStack`
- Older GeckoLib versions (3.0.x)
- Different entity registration patterns

**Migration Note**: Most concepts remain the same; method names differ.

---

## Performance Considerations

### Flight AI Overhead

Flying entities use **3D pathfinding**, which is more expensive than ground pathfinding:

**Mitigation Strategies**:
1. **Landing behavior**: Allow entity to land periodically (reduces pathfinding)
2. **Pathfinding range**: Limit wander range (`WanderInAirGoal(entity, speed, 10.0)`)
3. **Update frequency**: Don't recalculate path every tick

### Animation Performance

GeckoLib animation controllers run **every tick** (20 times/second):

**Best Practices**:
1. Keep animation predicates lightweight (simple state checks)
2. Avoid complex calculations in animation logic
3. Cache frequently accessed values
4. Use transition lengths to reduce state change frequency

### Rotation Interpolation

Smooth rotation interpolation happens **client-side every frame**:

**Impact**: Minimal - interpolation is highly optimized by Minecraft

**Tip**: Let vanilla interpolation handle smoothness; don't override unless necessary.

---

## Primary Sources

1. **Minecraft Wiki - Rotation**: https://minecraft.wiki/w/Rotation
2. **GeckoLib Wiki - Entity Animations**: https://github.com/bernie-g/geckolib/wiki/Entity-Animations
3. **GeckoLib Wiki - Blockbench Models**: https://github.com/bernie-g/geckolib/wiki/Making-Your-Models-(Blockbench)
4. **Grey's Minecraft Modding Blog - Entity Rotations**: http://greyminecraftcoder.blogspot.com/2015/07/entity-rotations-and-animation.html
5. **Fabric Documentation - Entity Rendering**: https://fabricmc.net/wiki/tutorial:entity
6. **PoseStack Gist**: https://gist.github.com/FortiBrine/1cf1bb7a3cd5820b35967657ce76f435

---

## Related Knowledge

- `minecraft/entities.md` - General entity system
- `fabric-api/registry.md` - Entity registration
- `graphics/animations.md` - Animation systems (general)
- `libraries/porting-forge-to-fabric.md` - Alex's Mobs porting (includes flying entities)
- `minecraft/alexs-mobs-reference.md` - Catalog of Alex's Mobs creatures

---

## Real-World Case Study: Crow Entity (Alex's Mobs Port)

### Problem

Crow entity (flying passive bird) had rotation issues when ported from Forge to Fabric:
- Flew backwards
- Rotated around feet ("clock pivoting") when turning
- Appeared to lay on belly while flying
- Had unexpected left tilt

### Attempted Solutions (Failed)

1. **Renderer Y-rotation (180°)**: Fixed backwards flight but caused clock pivoting
2. **Combined rotations (Y 180° + X -57.5°)**: Stood up more but tilted left
3. **Added Z-rotation (30°)**: Made it worse - crow went upside down
4. **Model rotation changes**: Didn't help and broke AI pathfinding

### Final Solution

**Reverted all renderer rotations and accepted model design**:
- Removed all `poseStack.mulPose()` calls
- Let entity yaw control direction naturally
- Accepted body's 57.5° forward tilt as intentional (realistic flying pose)
- Used only `poseStack.scale(0.8f)` for size adjustment

**Key Lesson**: The "laying on belly" pose was **intentional** - real birds DO fly with horizontal bodies when cruising. Fighting the model's natural design with renderer rotations broke natural rotation behavior.

### Code (Final Working Version)

```java
@Override
public void render(CrowEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    poseStack.push();

    // ONLY scale - no rotations
    poseStack.scale(0.8f, 0.8f, 0.8f);

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

**Result**: Crow flies naturally, turns smoothly, and body orientation looks correct.

---

## Debugging Tips

### Issue: Can't see entity at all

1. Check entity spawns: `/summon namespace:entity_name ~ ~ ~`
2. Enable hitbox overlay: F3+B
3. Check console for texture errors
4. Verify renderer registration in client initializer

### Issue: Entity faces wrong direction

1. Test with NO renderer rotations first
2. Check model's default body rotation in geo.json
3. Verify entity yaw is updating correctly (add logging)
4. Compare to vanilla flying entity (bat, parrot)

### Issue: Rotation looks jerky

1. Check if interpolation is disabled
2. Ensure tick rate is stable (not lagging server)
3. Use smooth transitions in animation controller (transition length > 0)
4. Use `MathHelper.lerp()` when manually updating rotation

### Issue: Animation doesn't match movement

1. Verify animation controller checks correct state (`isOnGround()`, velocity)
2. Check animation names match files exactly
3. Test animations in Blockbench preview
4. Add logging to animation predicate to see which animation plays

### Debugging Code Template

```java
@Override
public void render(FlyingEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    // Debug logging (remove in production)
    if (entity.age % 20 == 0) { // Every second
        System.out.println("=== ENTITY DEBUG ===");
        System.out.println("Position: " + entity.getPos());
        System.out.println("Yaw: " + entity.getYaw());
        System.out.println("Pitch: " + entity.getPitch());
        System.out.println("OnGround: " + entity.isOnGround());
        System.out.println("Velocity: " + entity.getVelocity());
    }

    poseStack.push();
    // Your transformations
    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

---

**End of Document**
