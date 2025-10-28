# GeckoLib Model Orientation Debugging Guide

**Date**: 2025-10-28
**Project**: xeenaa-alexs-mobs
**Entity**: Crow (Flying Passive Bird)
**Issue**: Model rendering with incorrect orientation

---

## Problem Summary

When implementing a GeckoLib-animated entity (Crow), the model rendered with multiple orientation issues:
1. Flying backwards (tail-forward instead of head-forward)
2. Horizontal flight orientation (laying flat)
3. Head/leg inversion (legs on top, head on bottom)
4. Incorrect rotation behavior (rolling instead of yawing)

## Root Cause Analysis

### Model Structure
The crow model (`crow.geo.json`) has the following characteristics:
- **Baked rotation**: Body bone has 57.5° X-rotation built into the model
- **Coordinate system**: Citadel-style coordinates (different from standard Minecraft)
- **Natural orientation**: Model lays flat/horizontal in its default state
- **Pivot points**: Body pivot at Y=21.9

### Key Discovery
```json
{
  "name": "body",
  "parent": "root",
  "pivot": [0, 21.9, 0],
  "rotation": [57.5, 0, 0],  // THIS is the flying angle!
  "cubes": [...]
}
```

This 57.5° X-rotation is the model's **intended flying pose**, not an error.

---

## Debugging Methodology

### Step 1: Add Comprehensive Logging

Add detailed logging to the renderer's `render()` method to track entity state:

```java
@Override
public void render(CrowEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                   VertexConsumerProvider bufferSource, int packedLight) {
    // Log every 10-20 ticks
    if (entity.age % 10 == 0) {
        System.out.println("=== ENTITY DEBUG ===");
        System.out.println("Position: X=" + entity.getX() + " Y=" + entity.getY() + " Z=" + entity.getZ());
        System.out.println("Pitch: " + entity.getPitch() + "° (up/down tilt)");
        System.out.println("Yaw: " + entity.getYaw() + "° (facing direction)");
        System.out.println("Render Yaw: " + entityYaw + "° (interpolated)");
        System.out.println("Body Yaw: " + entity.bodyYaw + "°");
        System.out.println("Head Yaw: " + entity.headYaw + "°");
        System.out.println("Velocity: X=" + entity.getVelocity().x + " Y=" + entity.getVelocity().y + " Z=" + entity.getVelocity().z);
        System.out.println("Flying: " + !entity.isOnGround());
    }
    // ... rest of render code
}
```

**Key Insights from Logs**:
- Entity `Pitch` was always **0.00°** (entity doesn't tilt dynamically)
- Body Yaw and Head Yaw rotate independently from entity Yaw
- This means the 57.5° baked rotation is static, not dynamic

### Step 2: Test Natural Orientation

Before applying any transformations, test with **ONLY scaling**:

```java
poseStack.scale(0.8f, 0.8f, 0.8f);
// NO rotations - see natural state
```

**Result**: Crow lays flat/horizontal, flies backwards, legs where head should be.

### Step 3: Systematic Rotation Testing

Test single-axis rotations one at a time:

| Rotation | Result | Status |
|----------|--------|--------|
| None | Horizontal, backwards, legs on top | ❌ |
| X +90° | Vertical but weird orientation | ❌ |
| X -90° | Vertical but different weird orientation | ❌ |
| X 180° | Laying on back | ❌ |
| Y 180° | **Forward flight, correct turning, legs on top** | ⚠️ Partial |
| Z +90° | Sideways, underground | ❌ |
| Z -90° | Sideways, underground | ❌ |
| Z 180° | Laying on back | ❌ |

**Key Discovery**: Y-axis 180° fixed the backwards flying and preserved correct yaw rotation!

### Step 4: Combination Testing

After finding Y-axis 180° works for direction, test combinations:

| Combination | Result | Status |
|-------------|--------|--------|
| Y 180° + X 180° | Laying on back | ❌ |
| Y 180° + Z 180° | Laying on back | ❌ |
| Y 180° + X +90° | Rolling instead of turning | ❌ |
| Y 180° + X -90° | Underground, weird rotation | ❌ |
| Y 180° + Z +90° | Underground, lateral movement | ❌ |
| Y 180° + Z -90° | Underground, lateral movement | ❌ |

**Current Status**: Y-axis 180° ONLY gives us:
- ✅ Forward flight (not backwards)
- ✅ Correct yaw rotation (turns properly)
- ❌ Legs on top, head on bottom (still inverted)

---

## Common Pitfalls

### 1. Rotation Axis Confusion
**Problem**: Applying X-axis rotation changes which axis the entity rotates around for yaw, causing "rolling" instead of "turning".

**Solution**: Avoid X-axis rotations if entity needs to preserve natural yaw behavior.

### 2. Combining 180° Rotations
**Problem**: Two 180° rotations often cancel each other out or produce unexpected results.

**Example**: Y 180° + Z 180° makes the crow lay on its back (worse than starting position).

### 3. Underground Spawning/Flying
**Problem**: Some rotation combinations cause the model to shift downward, making it fly underground.

**Attempted Fix**: Added Y-translation (+0.3), but this didn't solve the root rotation issue.

### 4. Dynamic Pitch Counter-rotation
**Problem**: Attempted to counter entity pitch with opposite renderer rotation.

```java
float entityPitch = entity.getPitch();
poseStack.multiply(new Quaternionf().rotateX(-entityPitch * ((float) Math.PI / 180F)));
```

**Result**: Failed - entity pitch is always 0° for this mob, and adding dynamic rotations caused spinning.

---

## Working Solution (Partial)

### Current Code
```java
@Override
public void render(CrowEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                   VertexConsumerProvider bufferSource, int packedLight) {
    poseStack.push();

    // Scale to appropriate size
    poseStack.scale(0.8f, 0.8f, 0.8f);

    // Y-axis 180° fixes backwards flying (CONFIRMED WORKING)
    // Issue: Legs on top, head on bottom (needs additional rotation)
    poseStack.multiply(new Quaternionf().rotateY((float) Math.PI));

    super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    poseStack.pop();
}
```

### Status
- ✅ Flies forward correctly
- ✅ Turns left/right properly (yaw works)
- ⚠️ **REMAINING ISSUE**: Legs on top, head on bottom

---

## Lessons Learned

### 1. Check Model Files First
Before adding renderer transformations, inspect the model JSON files:
- Look for baked rotations in bone definitions
- Check pivot points
- Understand the model's natural orientation

### 2. Log Entity State Extensively
Comprehensive logging reveals:
- Whether entity pitch/yaw actually changes
- If body/head rotate independently
- Movement patterns and velocity vectors

### 3. Test Incrementally
- Start with NO rotations (baseline)
- Test single-axis rotations one at a time
- Only combine rotations after understanding each axis
- Document what works and what doesn't

### 4. Understand Rotation Order Matters
Rotation order affects the final result:
```java
// These produce DIFFERENT results:
poseStack.multiply(rotateY(180)).multiply(rotateX(90));
poseStack.multiply(rotateX(90)).multiply(rotateY(180));
```

### 5. Preserve Entity Yaw Behavior
Avoid X-axis or Z-axis rotations if possible - they change which axis the entity rotates around, breaking natural turning behavior.

---

## Next Steps to Resolve

### Option A: Model File Adjustment (Recommended)
Instead of fighting the model with renderer rotations, **fix the model directly**:

1. Export the model from Blockbench
2. Rotate bones in Blockbench to correct orientation
3. Re-export and test
4. Keep renderer transformations minimal (Y 180° only)

**Pros**: Clean solution, no weird rotation combinations
**Cons**: Requires model editing

### Option B: Find Correct Rotation Combination
Continue testing rotation combinations, possibly with smaller angles (45°, 30°, etc.)

**Pros**: No model file changes
**Cons**: May never find perfect combination, maintains complexity

### Option C: Custom Rotation Logic
Implement custom rotation handling in the renderer with bone-specific transformations

**Pros**: Maximum control
**Cons**: Complex, fragile, hard to maintain

---

## Rotation Testing Matrix (Reference)

| X-axis | Y-axis | Z-axis | Forward? | Upright? | Yaw OK? | Notes |
|--------|--------|--------|----------|----------|---------|-------|
| 0° | 0° | 0° | ❌ | ❌ | ✅ | Backwards, flat, legs on top |
| 0° | 180° | 0° | ✅ | ❌ | ✅ | **Best so far** - legs on top |
| 90° | 0° | 0° | ❌ | ⚠️ | ❌ | Vertical but wrong |
| -90° | 0° | 0° | ❌ | ⚠️ | ❌ | Vertical but wrong |
| 180° | 0° | 0° | ❌ | ❌ | ✅ | On back |
| 0° | 0° | 90° | ❌ | ❌ | ❌ | Sideways |
| 0° | 0° | -90° | ❌ | ❌ | ❌ | Sideways |
| 0° | 0° | 180° | ❌ | ❌ | ✅ | On back |
| 180° | 180° | 0° | ✅ | ❌ | ✅ | On back |
| 0° | 180° | 180° | ✅ | ❌ | ✅ | On back |
| 90° | 180° | 0° | ⚠️ | ⚠️ | ❌ | Rolling instead of turning |
| -90° | 180° | 0° | ❌ | ❌ | ❌ | Underground, weird |
| 0° | 180° | 90° | ❌ | ❌ | ❌ | Underground, sideways |
| 0° | 180° | -90° | ❌ | ❌ | ❌ | Underground, sideways |

---

## Debug Checklist for Future Entities

When encountering GeckoLib orientation issues:

- [ ] Read model JSON files to understand baked rotations
- [ ] Add comprehensive logging to renderer
- [ ] Test with NO rotations first (baseline)
- [ ] Log entity pitch/yaw values during flight
- [ ] Test each axis rotation independently (±90°, ±180°)
- [ ] Document results in a matrix
- [ ] Only test combinations after understanding single-axis effects
- [ ] Check if entity pitch actually changes (many flying mobs have pitch=0)
- [ ] Consider fixing the model instead of fighting it with rotations
- [ ] Test that yaw rotation (turning) still works correctly after transforms

---

## References

- **Model File**: `src/main/resources/assets/xeenaa-alexs-mobs/geo/crow.geo.json`
- **Renderer**: `src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/CrowRenderer.java`
- **Entity**: `src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java`
- **Working Example**: `FlyRenderer.java` (uses Z 180° for upside-down flip)

## Conclusion

GeckoLib model orientation issues are complex due to:
1. Baked rotations in model files
2. Different coordinate systems (Citadel vs standard Minecraft)
3. Interaction between entity yaw and renderer transformations
4. Order-dependent rotation combinations

**Current Achievement**: Y-axis 180° rotation fixes backwards flying while preserving correct turning behavior. Only remaining issue is head/leg inversion.

**Recommended Next Step**: Edit the model file in Blockbench to flip the orientation directly, rather than trying to fix it with renderer rotations.
