---
name: geckolib-animation-patterns
tags: [minecraft, geckolib, animation, mob, entity]
activationPhrase: "geckolib animation patterns"
skillPriority: medium
autoActivate: true
activationConditions:
  - filePattern: "**/*.animation.json"
  - filePattern: "**/client/model/*.java"
  - contentMatch: "AnimatableModel|AnimationController|AnimationState"
---


# GeckoLib Animation Patterns Skill

**Purpose**: Provides animation-specific patterns for GeckoLib, including keyframe creation, animation state management, and converting Citadel procedural helpers to JSON keyframes.

**Based on**: Validated Fly entity conversion (Epic 03, Approach C)

---

## Table of Contents

1. [GeckoLib Animation Fundamentals](#geckolib-animation-fundamentals)
2. [Animation File Structure](#animation-file-structure)
3. [Keyframe Creation Strategies](#keyframe-creation-strategies)
4. [Converting Citadel Procedural Animations](#converting-citadel-procedural-animations)
5. [Animation Controllers](#animation-controllers)
6. [Common Animation Patterns](#common-animation-patterns)
7. [Animation State Management](#animation-state-management)
8. [Critical Issues and Fixes](#critical-issues-and-fixes)
9. [Performance Considerations](#performance-considerations)

---

## GeckoLib Animation Fundamentals

### Architecture Overview

**GeckoLib Animation System**:
- **Format**: Bedrock Edition JSON (v1.8.0 or v1.12.0)
- **Keyframe-based**: Artist-friendly timeline animations
- **Bone-driven**: Animations modify bone rotations, positions, scales
- **State-driven**: Animation controllers manage state transitions

**Key Differences from Citadel**:

| Aspect | Citadel (Forge) | GeckoLib (Fabric) |
|--------|-----------------|-------------------|
| **Definition** | Java code | JSON files |
| **Style** | Procedural (math-based) | Keyframe (timeline) |
| **Animation** | `flap()`, `walk()`, `swing()` helpers | Keyframe sequences |
| **Timing** | Code-controlled | JSON animation_length |
| **Interpolation** | Manual | Automatic (linear/bezier) |

### File Locations

```
assets/xeenaa-alexs-mobs/
├── geo/
│   └── [mob]_citadel.geo.json       # Model geometry
├── animations/
│   └── [mob]_citadel.animation.json # Animation keyframes
└── textures/entity/[mob]/
    └── [mob].png                     # Texture
```

---

## Animation File Structure

### JSON Schema (v1.8.0)

**Template**:
```json
{
  "format_version": "1.8.0",
  "animations": {
    "animation_name": {
      "loop": true,
      "animation_length": 2.0,
      "bones": {
        "bone_name": {
          "rotation": {
            "0.0": [x, y, z],
            "1.0": [x, y, z],
            "2.0": [x, y, z]
          },
          "position": {
            "0.0": [x, y, z]
          },
          "scale": {
            "0.0": [x, y, z]
          }
        }
      }
    }
  }
}
```

### Key Properties

**Animation Properties**:
- `loop`: Boolean - whether animation repeats
- `animation_length`: Float - total duration in seconds
- `override_previous_animation`: Boolean - force override instead of blend

**Keyframe Properties**:
- `rotation`: `[pitch, yaw, roll]` in degrees
- `position`: `[x, y, z]` in pixels (1 block = 16 pixels)
- `scale`: `[x, y, z]` multipliers (1.0 = normal)

**Timing**:
- Keyframe times in seconds (e.g., `"0.0"`, `"0.5"`, `"1.0"`)
- GeckoLib interpolates automatically between keyframes
- More keyframes = smoother but larger file

---

## Keyframe Creation Strategies

### 1. Oscillating Motion (Wings, Tails)

**Pattern**: A → B → A (back and forth)

**Example - Fly Wing Flapping**:
```json
"left_wing": {
  "rotation": {
    "0.0": [0, 0, 45],    // Up position
    "0.25": [0, 0, -45],  // Down position
    "0.5": [0, 0, 45],    // Up position
    "0.75": [0, 0, -45],  // Down position
    "1.0": [0, 0, 45]     // Up position (loop start)
  }
}
```

**When to Use**:
- Flying (wings)
- Swimming (fins, tail)
- Hovering (body bobbing)
- Idle breathing (chest expansion)

**Tips**:
- First and last keyframes should match for seamless looping
- Use 4+ keyframes for smooth motion (0.0, 0.25, 0.5, 0.75, 1.0)
- Larger angle differences = faster/more dramatic motion

### 2. Cyclic Motion (Walking, Running)

**Pattern**: A → B → C → D → A (repeating cycle)

**Example - Quadruped Walking**:
```json
"front_left_leg": {
  "rotation": {
    "0.0": [30, 0, 0],   // Forward
    "0.25": [0, 0, 0],   // Neutral
    "0.5": [-30, 0, 0],  // Backward
    "0.75": [0, 0, 0],   // Neutral
    "1.0": [30, 0, 0]    // Forward (loop)
  }
}
```

**When to Use**:
- Walking/running (legs)
- Climbing (limb alternation)
- Crawling (body segments)

**Tips**:
- Opposite legs should be out of phase (front_left at 0.0 = back_right at 0.5)
- Add vertical bobbing to body for realism
- Faster animation_length = faster movement

### 3. Single-Shot Actions (Attacks, Jumps)

**Pattern**: A → B → C (no loop)

**Example - Attack Lunge**:
```json
"attack": {
  "loop": false,
  "animation_length": 0.5,
  "bones": {
    "body": {
      "position": {
        "0.0": [0, 0, 0],     // Start position
        "0.2": [0, 0, -4],    // Wind-up
        "0.3": [0, 0, 8],     // Lunge forward
        "0.5": [0, 0, 0]      // Return
      },
      "rotation": {
        "0.0": [0, 0, 0],
        "0.2": [10, 0, 0],    // Tilt back
        "0.3": [-15, 0, 0],   // Tilt forward
        "0.5": [0, 0, 0]
      }
    }
  }
}
```

**When to Use**:
- Attacks
- Death animations
- Special abilities
- Eating/drinking

**Tips**:
- Set `loop: false`
- Use animation controllers to trigger once
- Add anticipation (wind-up) before main action
- Include recovery frames at the end

### 4. Idle Subtle Motion

**Pattern**: Very slow A → B → A with small angles

**Example - Fly Idle Mouth Movement**:
```json
"idle": {
  "loop": true,
  "animation_length": 2.0,
  "bones": {
    "mouth": {
      "rotation": {
        "0.0": [0, 0, 0],
        "0.5": [4.6, 2.3, 0],   // Small twitch
        "1.0": [0, 0, 0],
        "1.5": [-4.6, -2.3, 0], // Small twitch opposite
        "2.0": [0, 0, 0]
      }
    }
  }
}
```

**When to Use**:
- Idle animations (breathing, twitching)
- Ambience (ears flicking, eyes blinking)

**Tips**:
- Use small angles (< 10 degrees)
- Slow timing (2-4 seconds)
- Adds life without being distracting

---

## Converting Citadel Procedural Animations

### Citadel Helper Methods

**Common Patterns in Decompiled Code**:

```java
// Citadel's TabulaModelHelper methods (from decompiled code)
public static void flap(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
public static void walk(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
public static void swing(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
public static void bob(AdvancedModelBox box, float speed, float degree, boolean bounce, float f, float f1)
```

### Conversion Strategies

#### 1. `flap()` → Wing Animation

**Citadel Code**:
```java
this.flap(leftWing, speed, degree, false, 0F, 0F, f, f1);
this.flap(rightWing, speed, degree, true, 0F, 0F, f, f1);  // inverted
```

**GeckoLib JSON**:
```json
"fly": {
  "loop": true,
  "animation_length": 1.0,
  "bones": {
    "left_wing": {
      "rotation": {
        "0.0": [0, 0, 45],
        "0.5": [0, 0, -45],
        "1.0": [0, 0, 45]
      }
    },
    "right_wing": {
      "rotation": {
        "0.0": [0, 0, -45],  // Opposite of left_wing
        "0.5": [0, 0, 45],
        "1.0": [0, 0, -45]
      }
    }
  }
}
```

**Conversion Logic**:
- `speed` → `animation_length` (inverse relationship: higher speed = shorter length)
- `degree` → max angle in keyframes (e.g., degree=45 → `[0, 0, 45]`)
- `invert` → opposite angles for left/right symmetry

#### 2. `walk()` → Leg Animation

**Citadel Code**:
```java
this.walk(frontLeftLeg, speed, degree, false, 0F, 0F, f, f1);
this.walk(frontRightLeg, speed, degree, true, 0F, 0F, f, f1);
this.walk(backLeftLeg, speed, degree, true, 0F, 0F, f, f1);
this.walk(backRightLeg, speed, degree, false, 0F, 0F, f, f1);
```

**GeckoLib JSON**:
```json
"walk": {
  "loop": true,
  "animation_length": 1.0,
  "bones": {
    "front_left_leg": {
      "rotation": {
        "0.0": [30, 0, 0],
        "0.5": [-30, 0, 0],
        "1.0": [30, 0, 0]
      }
    },
    "front_right_leg": {
      "rotation": {
        "0.0": [-30, 0, 0],  // Opposite phase
        "0.5": [30, 0, 0],
        "1.0": [-30, 0, 0]
      }
    },
    "back_left_leg": {
      "rotation": {
        "0.0": [-30, 0, 0],  // Same phase as front_right
        "0.5": [30, 0, 0],
        "1.0": [-30, 0, 0]
      }
    },
    "back_right_leg": {
      "rotation": {
        "0.0": [30, 0, 0],   // Same phase as front_left
        "0.5": [-30, 0, 0],
        "1.0": [30, 0, 0]
      }
    }
  }
}
```

#### 3. `swing()` → Tail/Appendage Animation

**Citadel Code**:
```java
this.swing(tail, speed, degree, false, 0F, 0F, f, f1);
```

**GeckoLib JSON**:
```json
"walk": {
  "loop": true,
  "animation_length": 1.0,
  "bones": {
    "tail": {
      "rotation": {
        "0.0": [0, -20, 0],   // Swing left
        "0.5": [0, 20, 0],    // Swing right
        "1.0": [0, -20, 0]    // Swing left
      }
    }
  }
}
```

---

## Animation Controllers

### Java Entity Integration

**Entity Must Implement**:
```java
public class FlyEntity extends FlyingAnimalEntity implements GeoEntity {
    private final AnimatablInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<FlyEntity> state) {
        if (this.isOnGround()) {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
```

### Animation Name Matching (CRITICAL)

**MUST MATCH EXACTLY**:

❌ **WRONG** (this caused the Fly wing animation bug):
```json
// In animation JSON
"animations": {
  "animation.fly.idle": { ... },
  "animation.fly.fly": { ... }
}
```

```java
// In entity code
state.getController().setAnimation(RawAnimation.begin().then("idle", ...));
```
**Mismatch!** Code calls "idle", JSON defines "animation.fly.idle"

✅ **CORRECT**:
```json
// In animation JSON
"animations": {
  "idle": { ... },
  "fly": { ... },
  "death": { ... }
}
```

```java
// In entity code
state.getController().setAnimation(RawAnimation.begin().then("idle", ...));
```
**Match!** Both use "idle"

### State Machine Patterns

#### Pattern 1: Simple Movement States

```java
private PlayState predicate(AnimationState<MobEntity> state) {
    if (this.isDead()) {
        state.getController().setAnimation(RawAnimation.begin().then("death", Animation.LoopType.PLAY_ONCE));
    } else if (this.isAttacking()) {
        state.getController().setAnimation(RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE));
    } else if (state.isMoving()) {
        state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
    } else {
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
    }
    return PlayState.CONTINUE;
}
```

#### Pattern 2: Flying/Swimming States

```java
private PlayState predicate(AnimationState<FlyingEntity> state) {
    if (this.isOnGround()) {
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
    } else if (this.getVelocity().horizontalLengthSquared() > 0.01) {
        state.getController().setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
    } else {
        state.getController().setAnimation(RawAnimation.begin().then("hover", Animation.LoopType.LOOP));
    }
    return PlayState.CONTINUE;
}
```

#### Pattern 3: Multiple Controllers

```java
@Override
public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    controllers.add(new AnimationController<>(this, "attacks", 0, this::attackPredicate));
}

private PlayState movementPredicate(AnimationState<MobEntity> state) {
    // Handle idle/walk/run animations
}

private PlayState attackPredicate(AnimationState<MobEntity> state) {
    // Handle attack animations separately
}
```

---

## Common Animation Patterns

### Flying Creatures

**Required Animations**: `idle`, `fly`, `death`

**Idle** (on ground):
```json
"idle": {
  "loop": true,
  "animation_length": 2.0,
  "bones": {
    "wings": {
      "rotation": {
        "0.0": [0, 0, -35],  // Folded
        "2.0": [0, 0, -35]
      }
    },
    "body": {
      "position": {
        "0.0": [0, 0, 0],
        "1.0": [0, 0.5, 0],  // Slight breathing bob
        "2.0": [0, 0, 0]
      }
    }
  }
}
```

**Fly** (in air):
```json
"fly": {
  "loop": true,
  "animation_length": 0.5,  // Fast wing flapping
  "bones": {
    "left_wing": {
      "rotation": {
        "0.0": [0, 0, 45],
        "0.25": [0, 0, -45],
        "0.5": [0, 0, 45]
      }
    },
    "right_wing": {
      "rotation": {
        "0.0": [0, 0, -45],
        "0.25": [0, 0, 45],
        "0.5": [0, 0, -45]
      }
    }
  }
}
```

### Swimming Creatures

**Required Animations**: `idle`, `swim`, `death`

**Swim**:
```json
"swim": {
  "loop": true,
  "animation_length": 1.0,
  "bones": {
    "tail": {
      "rotation": {
        "0.0": [0, -20, 0],
        "0.5": [0, 20, 0],
        "1.0": [0, -20, 0]
      }
    },
    "fins": {
      "rotation": {
        "0.0": [0, 0, -15],
        "0.5": [0, 0, 15],
        "1.0": [0, 0, -15]
      }
    }
  }
}
```

### Walking Creatures

**Required Animations**: `idle`, `walk`, `run` (optional), `death`

**Walk**:
```json
"walk": {
  "loop": true,
  "animation_length": 1.0,
  "bones": {
    "front_left_leg": {
      "rotation": {
        "0.0": [30, 0, 0],
        "0.5": [-30, 0, 0],
        "1.0": [30, 0, 0]
      }
    },
    "front_right_leg": {
      "rotation": {
        "0.0": [-30, 0, 0],
        "0.5": [30, 0, 0],
        "1.0": [-30, 0, 0]
      }
    }
  }
}
```

---

## Animation State Management

### Velocity-Based States

```java
private PlayState predicate(AnimationState<MobEntity> state) {
    double velocity = this.getVelocity().horizontalLengthSquared();

    if (velocity > 0.06) {  // Running threshold
        state.getController().setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
    } else if (velocity > 0.01) {  // Walking threshold
        state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
    } else {
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
    }

    return PlayState.CONTINUE;
}
```

### Behavior-Based States

```java
private PlayState predicate(AnimationState<MobEntity> state) {
    // Check entity behavior state
    if (this.isAttacking()) {
        state.getController().setAnimation(RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE));
        return PlayState.CONTINUE;
    }

    if (this.isSleeping()) {
        state.getController().setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    // Default to movement state
    return movementAnimation(state);
}
```

### Transition Smoothing

```java
// Add transition ticks for smooth blending
controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
//                                                             ^ transition ticks
```

---

## Critical Issues and Fixes

### Issue 1: Animations Not Playing

**Symptoms**:
- Entity renders but doesn't animate
- Wings/limbs remain static
- No errors in logs

**Root Cause**: Animation name mismatch between JSON and Java code

**Fix**:
1. Check animation names in JSON file
2. Check animation names in entity `predicate()` method
3. Ensure EXACT match (case-sensitive)

**Example from Fly Bug**:
```json
// WRONG
"animations": {
  "animation.fly.idle": { ... }
}
```

```java
// Entity code calls
state.getController().setAnimation(RawAnimation.begin().then("idle", ...));
```

**Solution**: Rename JSON to `"idle": { ... }`

### Issue 2: Jittery/Stuttering Animations

**Cause**: Too few keyframes or incorrect loop timing

**Fix**:
- Add more keyframes (minimum 4 for smooth loops)
- Ensure first and last keyframes match
- Check `animation_length` is appropriate for motion speed

### Issue 3: Animations Too Fast/Slow

**Cause**: Incorrect `animation_length` value

**Fix**:
```json
// Too fast
"fly": {
  "animation_length": 0.2  // Wings flap unrealistically fast
}

// Better
"fly": {
  "animation_length": 0.5  // More realistic wing speed
}
```

### Issue 4: Limbs Not Moving

**Cause**: Bone names in animation JSON don't match model geometry

**Fix**:
1. Check bone names in `.geo.json` file
2. Ensure animation JSON uses identical names
3. Case-sensitive match required

---

## Performance Considerations

### Optimization Tips

1. **Minimize Keyframes**: Use only as many as needed for smooth motion
2. **Reuse Animations**: Share animations between similar mobs when possible
3. **Avoid Complex Math**: GeckoLib interpolates automatically - don't over-complicate
4. **Use Loop Efficiently**: Set `loop: true` for repeating animations to avoid re-triggering

### File Size Management

**Typical Sizes**:
- Simple mob (3 animations): 2-4 KB
- Complex mob (10+ animations): 10-20 KB
- Very detailed (attack variants, emotes): 30-50 KB

**When to Split Files**:
- If animation file exceeds 50 KB, consider splitting into multiple files
- Separate attack animations from movement animations
- Use multiple animation controllers

---

## Quick Reference

### Animation Name Checklist

✅ **Before Testing**:
- [ ] Animation names in JSON match entity code EXACTLY
- [ ] No "animation.mob." prefix unless code also uses it
- [ ] Case-sensitive match verified
- [ ] All referenced animations exist in JSON

### Common Animation Timings

| Animation Type | Recommended Length | Keyframes |
|----------------|-------------------|-----------|
| **Fast wing flap** | 0.3 - 0.5s | 4-6 |
| **Slow wing flap** | 1.0 - 2.0s | 4-6 |
| **Walking** | 0.8 - 1.2s | 4-8 |
| **Running** | 0.4 - 0.6s | 4-6 |
| **Idle breathing** | 2.0 - 4.0s | 4-8 |
| **Attack** | 0.3 - 0.8s | 6-10 |
| **Death** | 1.0 - 2.0s | 8-12 |

### Rotation Axis Guide

- **Pitch (X)**: Forward/backward tilt (nodding yes)
- **Yaw (Y)**: Left/right rotation (shaking head no)
- **Roll (Z)**: Side-to-side tilt (ear to shoulder)

**Wing Flapping** typically uses **Roll (Z)**: `[0, 0, 45]`
**Leg Walking** typically uses **Pitch (X)**: `[30, 0, 0]`
**Tail Swinging** typically uses **Yaw (Y)**: `[0, 20, 0]`

---

## Validated Example: Fly Entity

**Success Metrics**:
- ✅ Visual quality: 8/10 ("quite accurate")
- ✅ Wings animate correctly after fix
- ✅ Speed feels natural after tuning
- ✅ Spawn egg renders correctly

**Files**:
- `fly_citadel.geo.json` (6 bones)
- `fly_citadel.animation.json` (3 animations: idle, fly, death)
- `FlyEntity.java` (animation controller)
- `FlyModel.java` (GeckoLib model)

**Key Lessons**:
1. Animation name mismatch was root cause of static wings
2. Simple animations (4 keyframes) work well for small mobs
3. Speed tuning is critical for realism
4. GeckoLib's automatic interpolation handles smooth motion

---

**Next Skill**: See `mob-conversion-automation` for code generator designs and automation tools.

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used geckolib-animation-patterns
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used geckolib-animation-patterns`

This helps track which skills are actively consulted and identifies documentation gaps.
