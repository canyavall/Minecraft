# ModelCrow Analysis

## Decompilation Summary

Successfully decompiled `ModelCrow.java` from Alex's Mobs JAR (version 1.22.9) using CFR decompiler v0.152.

**Source Location:** `C:\Users\canya\Documents\projects\Minecraft\xeenaa-alexs-mobs\.claude\epics\07-crow-flying-passive-bird\decompiled\com\github\alexthe666\alexsmobs\client\model\ModelCrow.java`

## Model Structure Overview

### Texture Information
- **Texture Width:** 32 pixels
- **Texture Height:** 32 pixels

### Model Hierarchy (9 Bones Total)

```
root
└── body (rotX: 1.0036 rad ≈ 57.5°)
    ├── leg_left (rotX: 0.5672 rad ≈ 32.5°)
    ├── leg_right (rotX: 0.5672 rad ≈ 32.5°)
    ├── wing_left (rotX: 0.0436 rad ≈ 2.5°)
    ├── wing_right (rotX: 0.0436 rad ≈ 2.5°)
    ├── tail (rotX: -0.1309 rad ≈ -7.5°)
    └── head (rotX: -0.7418 rad ≈ -42.5°)
        └── beak
```

### Bone Details

| Bone | Position (x, y, z) | Rotation (x, y, z) | Box Dimensions | Texture Offset |
|------|-------------------|-------------------|----------------|----------------|
| **root** | (0, 24, 0) | - | - | - |
| **body** | (0, -2.1, 0) | (1.0036, 0, 0) | 3×5×3 | (0, 0) |
| **leg_left** | (0.9, 0, 0) | (0.5672, 0, 0) | 1×2×3 | (0, 17) |
| **leg_right** | (-0.9, 0, 0) | (0.5672, 0, 0) | 1×2×3 (mirrored) | (0, 17) |
| **wing_left** | (1.5, -4.9, 1.7) | (0.0436, 0, 0) | 1×6×3 | (13, 13) |
| **wing_right** | (-1.5, -4.9, 1.7) | (0.0436, 0, 0) | 1×6×3 (mirrored) | (13, 13) |
| **tail** | (0, -0.1, 3.0) | (-0.1309, 0, 0) | 3×4×2 | (13, 0) |
| **head** | (0, -4.8, 1.7) | (-0.7418, 0, 0) | 3×4×3 | (0, 9) |
| **beak** | (0, -1.4, -1.9) | (0, 0, 0) | 1×2×3 | (13, 7) |

## Key Animation Systems

### Animation Variables
The model uses Citadel's AdvancedEntityModel system with the following animation parameters:

1. **Flying Animation** (`flyProgress`)
   - Interpolated between `prevFlyProgress` and current `flyProgress`
   - Controls wing flapping, body tilt, and leg tucking
   - Wing flap speed: 0.8, degree: 0.2 (multiplied by 5 for intensity)

2. **Sitting Animation** (`sitProgress`)
   - Body rotation: -25° forward tilt
   - Legs and head adjust 25° to compensate

3. **Running Animation** (`runProgress`)
   - Calculated as `max(0, limbSwingAmount * 5 - flyProgress)`
   - Body tilts forward 15°
   - Different from walking animation

4. **Attack Animation** (`attackProgress`)
   - Head tilts forward 60°
   - Body assists with 25° tilt
   - Legs brace with -25° rotation

5. **Walking Animation** (default ground locomotion)
   - Speed: 1.2, degree: 0.78
   - Leg swing magnitude: 1.85x base
   - Head bob: 0.4x base

6. **Idle Animation**
   - Speed: 0.1, degree: 0.1
   - Subtle head and tail movements

### Animation Methods Used
- `progressRotationPrev()` - Smooth rotation transitions
- `progressPositionPrev()` - Smooth position transitions
- `swing()` - Wing flapping motion
- `bob()` - Vertical body movement
- `walk()` - Cyclic walking animations
- `flap()` - Tail movement

## Flying Mechanics

### Wing Animation During Flight
When `flyProgress > 0`:
```java
// Wings rotate to horizontal position (-90°) and spread (±90° Y rotation)
wing_right: rotX=-90°, rotY=90°
wing_left:  rotX=-90°, rotY=-90°

// Wings positioned forward and up
position offset: (0, 2, 1)

// Active flapping
swing amplitude: flapDegree * 5.0 (1.0 total)
flap rate: 0.8 (quite fast)
```

### Body Posture During Flight
- Body tilts upward 20°
- Head counter-tilts down 15° (for forward vision)
- Legs tuck up 55° (streamlining)
- Vertical bobbing at half flap rate (0.4)

## Notable Features

### Head Scaling
The model applies a 0.9x scale to the head normally, but when `young` (baby crow), the head scales to 1.45x creating the characteristic "big head" baby appearance.

### Mirrored Parts
- Right leg is a mirrored version of left leg
- Right wing is a mirrored version of left wing
- Uses `true` parameter in `addBox()` to enable mirroring

### Citadel Integration
This model extends `AdvancedEntityModel<EntityCrow>` from the Citadel library, which provides:
- `AdvancedModelBox` for complex bone manipulation
- Animation helper methods (`swing`, `bob`, `walk`, `flap`)
- Progress-based interpolation methods
- Pose system (`updateDefaultPose()`, `resetToDefaultPose()`)

## Conversion Considerations for GeckoLib

### Challenges
1. **Citadel Dependencies**: Methods like `progressRotationPrev()`, `swing()`, `bob()` are Citadel-specific
2. **Custom Animation System**: Not using vanilla Minecraft keyframes
3. **Progress Variables**: Entity has `flyProgress`, `sitProgress`, `attackProgress` that drive animations
4. **Dynamic Calculations**: Animations computed procedurally, not keyframe-based

### Recommended Approach
1. **Option A - Pure Conversion**: Create GeckoLib animations with keyframes that replicate the procedural motion
2. **Option B - Hybrid**: Use Blockbench to create base animations, add procedural code in custom AnimationController
3. **Option C - Full Procedural**: Implement the same math using GeckoLib's bone API without keyframe animations

### Animation States to Recreate
- `idle` - Subtle head/tail movement
- `walk` - Ground locomotion with leg swing
- `run` - Faster variant with body tilt
- `fly` - Wing flapping with body adjustment
- `sit` - Resting posture
- `attack` - Pecking/biting motion

## Files Generated
1. `ModelCrow.java` - Full decompiled source (167 lines)
2. `ModelCrow_ANALYSIS.md` - This analysis document

## Next Steps
1. Review EntityCrow.java to understand `flyProgress` and other animation drivers
2. Decide on GeckoLib conversion strategy (keyframe vs procedural)
3. Create initial Blockbench model matching bone structure
4. Implement animation system in GeckoLib
