# Citadel Model Analysis: Alex's Mobs Fly

## Overview
Decompiled from Alex's Mobs 1.22.9 (Forge 1.20.1) using CFR 0.152.

**Source Files:**
- `ModelFly.java` - Model definition
- `EntityFly.java` - Entity behavior
- `RenderFly.java` - Renderer

**Citadel Framework:**
- Extends `AdvancedEntityModel<EntityFly>`
- Uses `AdvancedModelBox` for bones
- Texture: `alexsmobs:textures/entity/fly.png` (32x32)

---

## Bone Hierarchy

```
root (pivot: 0, 24, 0)
└── body (pivot: 0, -3, 0)
    ├── legs (pivot: 0, 2, -2)
    ├── left_wing (pivot: 1, -2, -1)
    ├── right_wing (pivot: -1, -2, -1)
    └── mouth (pivot: 0, 0, -3)
```

---

## Bone Details

### root
- **Type**: Container bone
- **Position**: (0, 24, 0)
- **Cubes**: None
- **Purpose**: Root transform anchor

### body
- **Parent**: root
- **Position**: (0, -3, 0) relative to root
- **Absolute Position**: (0, 21, 0)
- **Cube**:
  - From: (-2, -2, -3)
  - To: (2, 2, 3) [size: 4×4×6]
  - Texture Offset: (0, 0)
  - Inflation: 0.0
- **Purpose**: Main body (thorax + abdomen)

### legs
- **Parent**: body
- **Position**: (0, 2, -2) relative to body
- **Absolute Position**: (0, 23, -2)
- **Cube**:
  - From: (-1.5, 0, 0)
  - To: (1.5, 1, 5) [size: 3×1×5]
  - Texture Offset: (0, 11)
  - Inflation: 0.0
- **Purpose**: Six legs grouped together

### left_wing
- **Parent**: body
- **Position**: (1, -2, -1) relative to body
- **Absolute Position**: (1, 19, -1)
- **Cube**:
  - From: (0, 0, -1)
  - To: (4, 0, 2) [size: 4×0×3] **← FLAT (height=0)**
  - Texture Offset: (12, 11)
  - Inflation: 0.0
  - Mirror: false
- **Purpose**: Left wing (flat plane)
- **Animation**: Flaps on Z axis

### right_wing
- **Parent**: body
- **Position**: (-1, -2, -1) relative to body
- **Absolute Position**: (-1, 19, -1)
- **Cube**:
  - From: (-4, 0, -1)
  - To: (0, 0, 2) [size: 4×0×3] **← FLAT (height=0)**
  - Texture Offset: (12, 11)
  - Inflation: 0.0
  - Mirror: true
- **Purpose**: Right wing (flat plane, mirrored)
- **Animation**: Flaps on Z axis (opposite direction)

### mouth
- **Parent**: body
- **Position**: (0, 0, -3) relative to body
- **Absolute Position**: (0, 21, -3)
- **Cube**:
  - From: (0, 0, -1)
  - To: (0, 4, 1) [size: 0×4×2] **← FLAT (width=0)**
  - Texture Offset: (15, 16)
  - Inflation: 0.0
- **Purpose**: Proboscis/mouthpart (flat vertical plane)
- **Animation**: Idle wobble

---

## Texture Mapping

**Texture Size**: 32×32 pixels

### UV Map Layout:

```
(0,0) ───────────────────────── (32,0)
  │                               │
  │  [body: 0,0 → 14,10]         │
  │                               │
  │  [legs: 0,11 → 11,17]        │
  │  [wings: 12,11 → 18,14]      │
  │                               │
  │  [mouth: 15,16 → 17,22]      │
  │                               │
(0,32) ────────────────────── (32,32)
```

**Breakdown:**
- **body**: Offset (0, 0), uses 4×4×6 cube → ~14×10 texture area
- **legs**: Offset (0, 11), uses 3×1×5 cube → ~11×6 texture area
- **left_wing**: Offset (12, 11), uses 4×0×3 cube → ~6×3 texture area
- **right_wing**: Offset (12, 11), MIRRORED, same UV as left
- **mouth**: Offset (15, 16), uses 0×4×2 cube → ~2×6 texture area

---

## Animation System (Procedural)

Citadel uses **code-based procedural animations** in `setupAnim()` method.

### Animation Methods Used:
1. **walk()** - Creates walking/bobbing motion
2. **flap()** - Creates flapping motion
3. **swing()** - Creates swinging motion
4. **Direct rotation assignment** - For static poses

### Animation States:

#### IDLE (on ground, not moving):
```java
left_wing.rotateAngleZ = Maths.rad(-35.0);  // Fold left wing
right_wing.rotateAngleZ = Maths.rad(35.0);   // Fold right wing
```

#### FLYING:
```java
// Left wing flaps on Z axis
flap(left_wing, speed=1.3, degree=0.8, invert=true, offset=0.0, weight=0.2, ageInTicks, 1.0)

// Right wing flaps on Z axis (opposite)
flap(right_wing, speed=1.3, degree=0.8, invert=false, offset=0.0, weight=0.2, ageInTicks, 1.0)

// Legs walk animation
walk(legs, speed=0.2, degree=0.2, invert=false, offset=1.0, weight=0.2, ageInTicks, 1.0)
```

#### MOUTH (constant idle):
```java
// Mouth wobbles vertically (X rotation)
walk(mouth, speed=0.28, degree=0.08, invert=false, offset=-1.0, weight=0.2, ageInTicks, 1.0)

// Mouth flaps slightly (Y rotation)
flap(mouth, speed=0.28, degree=0.04, invert=false, offset=-2.0, weight=0.0, ageInTicks, 1.0)
```

### Animation Parameters:
- **flySpeed**: 1.4
- **flyDegree**: 0.8
- **idleSpeed**: 1.4 (not actively used)
- **idleDegree**: 0.8 (not actively used)

---

## Key Observations

### 1. Flat Geometry
- **Wings**: Height dimension is 0 (4×0×3) - rendered as flat planes
- **Mouth**: Width dimension is 0 (0×4×2) - rendered as flat plane
- This is unusual but intentional for the fly's thin wings and proboscis

### 2. Coordinate System
- **Citadel Y-up**: Y is vertical axis
- **Root at Y=24**: Positions entity slightly above ground
- **Negative Y = down**: body at -3 means 3 units below root

### 3. Pivot Points Critical
- Wings pivot from base attachment point
- Legs pivot from top (where they attach to body)
- Mouth pivots from base (where it attaches to head)

### 4. Mirror Flag
- Right wing uses `mirror: true` to flip UV mapping
- Allows single texture for both wings

### 5. Animation System
- **No keyframe animations** - all procedural
- Uses Citadel's animation helper methods: `walk()`, `flap()`, `swing()`
- Animation timing based on `ageInTicks` (entity's lifetime in ticks)

---

## Conversion Challenges

### Challenge 1: Flat Geometry
**Problem**: GeckoLib may not handle 0-dimension cubes well
**Solution**: Use minimum thickness (0.01 or 0.1) instead of 0

### Challenge 2: Procedural Animations
**Problem**: GeckoLib uses keyframe animations, not procedural code
**Solution**:
- Option A: Create looping keyframe animations that mimic the procedural motion
- Option B: Keep animation code-based (acceptable for simple cases)
- Option C: Use GeckoLib's animation system with custom controllers

### Challenge 3: Coordinate System
**Problem**: Citadel vs GeckoLib may have different Y-up conventions
**Solution**: Test and adjust Y coordinates if model appears upside-down or offset

### Challenge 4: Pivot Points
**Problem**: Pivot point conventions may differ
**Solution**: Carefully translate pivot positions, test rotations

### Challenge 5: Animation Timing
**Problem**: Citadel's `walk()` and `flap()` methods don't exist in GeckoLib
**Solution**:
- Reverse-engineer the math behind these methods
- Create equivalent keyframe animations with sine/cosine curves

---

## Recommended Conversion Strategy

### Phase 1: Static Model (Geometry Only)
1. Convert bone hierarchy to GeckoLib format
2. Handle flat cubes by adding minimal thickness
3. Translate pivot points accurately
4. Map UV coordinates

### Phase 2: Test Static Model
1. Build and load in-game
2. Verify geometry appears correct
3. Verify texture maps correctly

### Phase 3: Add Animations
1. Create "idle" animation with mouth wobble
2. Create "fly" animation with wing flapping
3. Create "walk" animation with folded wings
4. Use sine wave keyframes to mimic procedural motion

### Phase 4: Test Animations
1. Verify smooth transitions
2. Verify correct rotation axes
3. Adjust timing/amplitude to match original

---

## Conversion Reference Values

### World Positions (for verification):
- Root: (0, 24, 0)
- Body: (0, 21, 0)
- Legs: (0, 23, -2)
- Left Wing: (1, 19, -1)
- Right Wing: (-1, 19, -1)
- Mouth: (0, 21, -3)

### Rotation Axes:
- **Wings**: Rotate on Z axis (roll)
- **Legs**: Rotate on X axis (pitch)
- **Mouth**: Rotate on X and Y axes (pitch and yaw)

### Animation Ranges (estimated):
- **Wing flap**: ±45° on Z axis (Citadel uses degree=0.8 radians = ±45.8°)
- **Wing fold**: ±35° on Z axis (static pose)
- **Mouth wobble**: ±4.6° on X axis (degree=0.08 radians)
- **Mouth flap**: ±2.3° on Y axis (degree=0.04 radians)

---

## Next Steps

1. Create `fly_citadel.geo.json` with accurate geometry
2. Handle flat cubes appropriately
3. Test static model in-game
4. Create basic animations
5. Iterate on animation timing/amplitude

**Time to convert**: Estimated 1-2 hours for geometry + UV, 2-3 hours for animations.
