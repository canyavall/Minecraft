---
name: flying-entity-control
tags: [minecraft, geckolib, flying-entities, rotation, entity-rendering, animation, 3d-transforms]
activationPhrase: "flying entity control"
skillPriority: high
autoActivate: true
activationConditions:
  - filePattern: "**/entity/*Flying*.java"
  - filePattern: "**/entity/*Bird*.java"
  - filePattern: "**/renderer/*Flying*.java"
  - contentMatch: "FlyingAnimalEntity|flying.*entity|aerial.*movement"
---

# Flying Entity Control Skill

**Purpose**: Guide implementation of flying entities with proper rotation handling, animation control, and GeckoLib integration for aerial movement in Minecraft.

**Based on**: Comprehensive research documented in `.claude/knowledge/graphics/flying-entity-control.md` (crow entity case study, Epic 007)

---

## For Agents: Quick Summary

Flying entities require careful 3D rotation management to avoid common pitfalls:

**CRITICAL RULES**:
1. **Use entity rotation** (`entity.setYaw()`, `entity.setPitch()`) for direction - NOT renderer rotations
2. **Use PoseStack ONLY for scale/translate** - avoid `poseStack.mulPose()` for orientation
3. **Use GeckoLib bones** for articulated movement (wings, tail, head)
4. **Avoid "clock pivoting"** - renderer rotations cause entity to spin around feet instead of turning naturally

**COMMON ISSUE**: "Entity flies backwards/rotates around feet"
**ROOT CAUSE**: Using renderer rotations instead of entity rotation or model fixes
**FIX**: Remove `poseStack.mulPose()` calls, use entity yaw/pitch instead

**For detailed information**: See `.claude/knowledge/graphics/flying-entity-control.md`

---

## Table of Contents

1. [Core Principles](#core-principles)
2. [Rotation Strategy](#rotation-strategy)
3. [GeckoLib Integration](#geckolib-integration)
4. [Common Issues Quick Reference](#common-issues-quick-reference)
5. [Implementation Checklist](#implementation-checklist)
6. [Reference](#reference)

---

## Core Principles

### The Three Rotation Systems

Flying entities have **three independent rotation systems**:

| System | Where Set | Purpose | Example |
|--------|-----------|---------|---------|
| **Entity Rotation** | `entity.setYaw()`, `entity.setPitch()` | Logical direction (AI, physics) | Turning, tilting up/down |
| **Renderer Rotation** | `poseStack.mulPose()` | Visual adjustments (client-only) | ❌ AVOID for orientation |
| **Bone Rotation** | GeckoLib animations, bone API | Articulated body parts | Wing flapping, head turning |

**Key Rule**: Use **entity rotation** for direction, **bones** for articulation, **avoid renderer rotations** for orientation.

### Minecraft's Coordinate System

- **X-axis**: West (-) to East (+)
- **Y-axis**: Down (-) to Up (+)
- **Z-axis**: North (-) to South (+)

**Yaw** (horizontal direction):
- 0° = South (+Z)
- -90° = East (+X)
- +90° = West (-X)
- ±180° = North (-Z)

**Pitch** (vertical tilt):
- 0° = Horizontal
- -90° = Straight up
- +90° = Straight down

**Roll** (banking): NOT natively supported - must implement via bones or renderer

---

## Rotation Strategy

### ✅ DO: Use Entity Rotation

**For direction changes**:
```java
// In entity tick() or AI
entity.setYaw(desiredYaw);      // Horizontal direction
entity.setPitch(desiredPitch);  // Vertical tilt (flying entities)
```

**For dynamic pitch based on movement**:
```java
@Override
public void tick() {
    super.tick();

    // Adjust pitch based on vertical velocity
    double verticalVelocity = this.getVelocity().y;
    float desiredPitch = (float)(-verticalVelocity * 30.0); // Scale factor
    desiredPitch = MathHelper.clamp(desiredPitch, -45, 45); // Reasonable range

    // Smooth interpolation
    float currentPitch = this.getPitch();
    float newPitch = MathHelper.lerp(0.1f, currentPitch, desiredPitch);
    this.setPitch(newPitch);
}
```

### ✅ DO: Use PoseStack for Scale/Translate

**For size adjustments**:
```java
@Override
public void render(FlyingEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    poseStack.push();

    // ✅ GOOD: Scale model
    poseStack.scale(0.8f, 0.8f, 0.8f);

    // ✅ GOOD: Fix Y-position clipping
    poseStack.translate(0.0, 0.5, 0.0);

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

### ❌ DON'T: Use Renderer Rotations for Orientation

**This causes "clock pivoting"** (entity spins around feet):
```java
// ❌ BAD: Causes rotation around root pivot (feet)
poseStack.mulPose(Axis.YP.rotationDegrees(180)); // Fix backwards flight
poseStack.mulPose(Axis.XP.rotationDegrees(-57.5)); // Fix belly-laying pose
// Result: Entity rotates like clock hand instead of turning naturally
```

**Why it fails**:
- Rotates **entire model** as rigid object around root pivot (feet)
- Breaks natural bone animation hierarchy
- Head, body, tail all rotate together around wrong pivot point

### Alternative: Fix Model Orientation (If Model Is Wrong)

**If model consistently faces wrong direction**, edit model file:
```json
// crow.geo.json
{
  "name": "body",
  "pivot": [0, 21.9, 0],
  "rotation": [57.5, 0, 0], // Adjust body default rotation
  "cubes": [...]
}
```

---

## GeckoLib Integration

### Animation Controller Pattern

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

### Dynamic Bone Adjustment (Advanced)

**For pitch-based body tilt during flight**:
```java
@Override
public void render(FlyingEntity entity, float entityYaw, float partialTick,
                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
                   int packedLight) {
    // Get specific bone and adjust rotation dynamically
    GeoBone bodyBone = model.getBone("body").orElse(null);
    if (bodyBone != null && !entity.isOnGround()) {
        // Adjust pitch based on entity's vertical velocity
        float pitchAdjust = (float)entity.getVelocity().y * 20.0f;
        bodyBone.setRotX(bodyBone.getRotX() + (float)Math.toRadians(pitchAdjust));
    }

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
}
```

### Bone Hierarchy and Inheritance

**Critical Understanding**:
- Child bones **inherit parent rotation**
- If body rotates 57.5° forward, head/wings/tail also rotate 57.5° (inherited)
- Animation adjustments are **additive** on top of inherited rotation

**Example Hierarchy**:
```
Root (pivot: [0, 24, 0])
 └─ Body (pivot: [0, 21.9, 0], rotation: [57.5°, 0, 0])
     ├─ Head (inherits body's 57.5° tilt)
     ├─ Left Wing (inherits body's 57.5° tilt)
     ├─ Right Wing (inherits body's 57.5° tilt)
     └─ Tail (inherits body's 57.5° tilt, adds own rotation)
```

---

## Common Issues Quick Reference

### Issue 1: Entity Flies Backwards

**Symptom**: Entity moves forward but faces backward

**Root Cause**: Model's default orientation doesn't match entity forward direction (yaw 0° = south)

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
       this.setYaw(this.getYaw() + 180);
   }
   ```

### Issue 2: Entity Rotates Around Feet (Clock Pivoting)

**Symptom**: Entity spins around feet like clock hand instead of turning naturally

**Root Cause**: Using renderer rotations (`poseStack.mulPose()`)

**Solution**: Remove renderer rotations, use entity yaw:
```java
// ❌ WRONG
poseStack.mulPose(Axis.YP.rotationDegrees(180));

// ✅ RIGHT
entity.setYaw(desiredYaw); // In entity class or AI
```

### Issue 3: Entity Lays on Belly While Flying

**Symptom**: Flying entity looks horizontal (lying down) instead of upright

**Root Cause**: Model's body bone has forward tilt (often intentional for realistic flight)

**Solutions**:
1. **Check if intentional**: Many birds/insects DO fly with horizontal bodies
2. **Adjust model**: Reduce body X-rotation in `model.geo.json`:
   ```json
   {
     "name": "body",
     "rotation": [30, 0, 0] // Reduce from 57.5° to 30°
   }
   ```

3. **Dynamic adjustment**: Use bone API to adjust pitch during flight (see GeckoLib Integration above)

### Issue 4: Entity Tilts Left/Right Unexpectedly

**Symptom**: Flying entity not level horizontally; appears to tilt to one side

**Root Cause**: Model has unintended Z-rotation (roll) on body or root bone

**Solution**: Check model for Z-rotation:
```json
{
  "name": "body",
  "rotation": [57.5, 0, 0] // Z should be 0 for level flight
}
```

### Issue 5: Animation Looks Wrong When Flying Up/Down

**Symptom**: Flight animation doesn't adapt to climbing or diving

**Solution**: Use animation state to vary animations based on pitch:
```java
private PlayState animationPredicate(AnimationState<FlyingEntity> state) {
    float pitch = this.getPitch();

    if (!this.isOnGround()) {
        if (pitch < -20) {
            // Ascending - use climb animation
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.entity.fly_climb"));
        } else if (pitch > 20) {
            // Descending - use dive animation
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.entity.fly_dive"));
        } else {
            // Level flight
            controller.setAnimation(RawAnimation.begin().thenLoop("animation.entity.fly"));
        }
        return PlayState.CONTINUE;
    }
    // ... other states
}
```

---

## Implementation Checklist

When implementing a flying entity, verify:

**Entity Class**:
- [ ] Extends appropriate base (FlyingAnimalEntity, PathAwareEntity, etc.)
- [ ] Implements GeoEntity
- [ ] Has flight AI goals (WanderInAirGoal, LandOnBlockGoal, etc.)
- [ ] Updates pitch based on vertical velocity (if dynamic pitch desired)

**Model (Blockbench → GeckoLib)**:
- [ ] All cubes inside groups (bones)
- [ ] Root pivot at feet or appropriate base point
- [ ] Body rotation reasonable (<45° X-rotation typical)
- [ ] No unintended Z-rotation (roll) on main bones
- [ ] Exported as GeckoLib model

**Animations**:
- [ ] Idle animation (subtle movements)
- [ ] Fly animation (wing flapping, body posture)
- [ ] Walk animation (if entity can land)
- [ ] Animation controller checks `isOnGround()` for state switching

**Renderer**:
- [ ] Extends `GeoEntityRenderer<YourEntity>`
- [ ] Passes model to super constructor
- [ ] Uses `poseStack.scale()` for size adjustments
- [ ] Uses `poseStack.translate()` for Y-position fixes
- [ ] **Avoids `poseStack.mulPose()` for orientation**
- [ ] Registered in client initializer

**Testing**:
- [ ] Entity spawns correctly (F3+B to check hitbox)
- [ ] Flies in correct direction (not backwards)
- [ ] Turns naturally (not clock pivoting)
- [ ] Body orientation looks correct (not laying on belly unless intentional)
- [ ] Animation plays correctly when flying/walking
- [ ] No Z-fighting or texture flickering

---

## Best Practices Summary

### DO:
1. Use entity rotation (`setYaw()`, `setPitch()`) for direction
2. Use GeckoLib bones for articulated movement
3. Use PoseStack for scale and Y-position adjustments
4. Test with F3+B hitbox overlay
5. Use smooth interpolation for rotation changes (`MathHelper.lerp()`)

### DON'T:
1. Use renderer rotations (`poseStack.mulPose()`) for entity orientation
2. Fight GeckoLib's bone inheritance system
3. Assume vanilla Minecraft supports roll rotation (it doesn't)
4. Over-rotate in model file (keep body < 45° for most cases)
5. Skip version testing (rotation handling changed in 1.17+)

---

## Reference

**For detailed information**:
- `.claude/knowledge/graphics/flying-entity-control.md` - Comprehensive research document
  - Minecraft coordinate system details
  - Rotation pivot problem deep dive
  - PoseStack transformation patterns
  - GeckoLib bone system architecture
  - Real-world case study (crow entity)
  - Debugging tips and templates
  - Performance considerations

**Related Skills**:
- `geckolib-animation-patterns` - Animation keyframes and state management
- `fabric-modding` - Fabric API patterns
- `minecraft-modding` - Core Minecraft systems

**Primary Sources** (referenced in knowledge base):
- Minecraft Wiki - Rotation
- GeckoLib Wiki - Entity Animations
- GeckoLib Wiki - Blockbench Models
- Fabric Documentation - Entity Rendering

---

## Real-World Validation

**Case Study**: Crow Entity (Epic 007)

**Problem**: Crow flew backwards, rotated around feet, appeared to lay on belly

**Attempted Fixes** (all failed):
- Renderer Y-rotation (180°) - caused clock pivoting
- Combined rotations (Y 180° + X -57.5°) - tilted left
- Added Z-rotation (30°) - went upside down

**Final Solution**:
- Removed ALL renderer rotations
- Let entity yaw control direction naturally
- Accepted body's 57.5° forward tilt as intentional (realistic bird flight pose)
- Used only `poseStack.scale(0.8f)` for size

**Result**: Natural flight, smooth turning, correct orientation

**Key Lesson**: The "laying on belly" pose was **intentional design** - real birds fly with horizontal bodies. Fighting model design with renderer rotations breaks natural rotation behavior.

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used flying-entity-control
```

**Example**: `[2025-11-01 16:45:00] implementation-agent used flying-entity-control`

This helps track which skills are actively consulted and identifies documentation gaps.
