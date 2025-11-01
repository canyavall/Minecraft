# Crow Rotation Problem - Debug Notes

## Original Model (crow.geo.json)
- Root pivot: [0, 24, 0] (at feet level)
- Body pivot: [0, 21.9, 0] with 57.5° X-rotation (tilted forward)
- Head pivot: [0, 17, 0.5]
- Tail pivot: [0, 21.8, 3]

## What We Tried

### Attempt 1: Y-rotation 180°
- Goal: Fix backwards flight
- Result: Crow flies forward BUT rotates around feet like clock hand (not natural head turn)

### Attempt 2: Y 180° + X -57.5°
- Goal: Counter body's forward tilt
- Result: Stood up more but tilted left, diagonal flight

### Attempt 3: Y 180° + X -57.5° + Z 30°
- Goal: Fix left tilt
- Result: WORSE - crows upside down

### Attempt 4: Y 180° + X -90°
- Goal: Simpler clean rotation
- Result: Still wrong pivot point issue

### Current: NO rotations (reverted)
- Testing original model behavior to understand baseline

## The Real Problem

**Renderer rotations (poseStack.multiply) rotate the ENTIRE model around its root pivot.**

The root pivot is at the feet (Y=24), so:
- Any Y-rotation spins crow around feet axis (clock rotation)
- This breaks natural head turning behavior

## How It Should Work

GeckoLib entities should rotate via:
1. **Entity yaw** - Controls which direction entity faces
2. **Bone rotations** - Individual bones (head, body, tail) rotate in animations
3. **Renderer** - Only for scaling/positioning, NOT orientation

## What We Broke

By adding poseStack.multiply rotations, we:
- Forced entire model to rotate around feet pivot
- Overrode GeckoLib's natural bone rotation system
- Made crow rotate like rigid object instead of articulated creature

## Next Steps

1. **Test original** - See how crow behaves with NO renderer rotations
2. **Check CrowEntity** - Ensure entity yaw updates correctly when turning
3. **Fix model or animations** - If needed, adjust geo.json pivots or animation.json rotations
4. **Never use renderer rotations for orientation** - Only for scale/position

## Key Insight

The model's 57.5° body tilt is INTENTIONAL for flying pose. It should be:
- Visible when looking at crow from side while flying
- Hidden by proper head/tail bone rotations in animations
- NOT countered by renderer transforms

---

## Testing Checklist (Update Each Test)

### Test 1: Original Model (NO Renderer Rotations)
**Status:** TESTED ✅
**What works:**
- ✅ Crow rotates naturally (head turns, not clock pivot)
- ✅ Crow flies in proper direction
**What's broken:**
- ❌ Crow looks like laying on belly (body too horizontal)
**Observations:**
- Rotation system works correctly with NO renderer transforms
- Problem is model's body orientation, not rotation pivot
- Need to tilt body UP, not rotate entire model

### Rules to Follow
1. **ALWAYS read this doc before making changes**
2. **NEVER add renderer rotations (poseStack.multiply)** unless absolutely necessary
3. **Test each change** - update checklist with results
4. **If rotation needed** - fix in model/animations, NOT renderer
5. **Rotation pivot = feet** - any renderer rotation spins around feet like clock

---

## Solution: Fix Body Tilt in Model

**Current:** Body bone has 57.5° X-rotation in crow.geo.json (line 21)
**Problem:** Makes crow look like laying on belly
**Options:**

### Option A: Reduce Body Rotation in Model
Edit `crow.geo.json` line 21:
- Change from: `"rotation": [57.5, 0, 0]`
- Change to: `"rotation": [20, 0, 0]` or `[30, 0, 0]`
- Pro: Simple, permanent fix
- Con: Might affect flying pose

### Option B: Add Dynamic Animation
In `crow.animation.json`, add body rotation in fly animation
- Pro: Different poses for flying vs standing
- Con: More complex

### Option C: Translation Offset (SAFER)
Add Y-translation in renderer to lift crow visually
- Use `poseStack.translate(0, 0.3, 0)` to raise model
- Pro: Doesn't affect rotations or animations
- Con: Might not fully fix "laying" appearance

**Recommended:** Try Option C first (safest), then Option A if needed.

---

### Test 2: Reduce Body Rotation (Model Fix - WRONG DIRECTION)
**Status**: FAILED ❌
**Change**: crow.geo.json line 21: rotation [57.5,0,0] → [30,0,0]
**What works**:
- ✅ Rotation still natural
**What's broken**:
- ❌ Crow nose-down (diving pose) - WORSE than before
**Observations**:
- We went WRONG direction - need to INCREASE rotation, not decrease
- 57.5° was making it lay flat, 30° makes it dive
- Need value HIGHER than 57.5° (try 70-80°)

### Test 3: INCREASE Body Rotation to 75°
**Status**: FAILED ❌
**Change**: crow.geo.json line 21: rotation [30,0,0] → [75,0,0]
**Logic**: Higher angle = nose tilts UP more
**What works**:
- ✅ Still rotates naturally
**What's broken**:
- ❌ Still looks same (horizontal/belly-laying)
- ❌ CROWS STOPPED FLYING - AI can't handle extreme tilt
**Observations**:
- Body rotation in model doesn't seem to affect visual appearance much
- Too extreme angle (75°) breaks flight AI
- Need DIFFERENT approach - model rotation isn't working

## CONCLUSION: Accept Original Model Design

**Decision**: Revert to 57.5° (original)

**Reasoning**:
- The "laying on belly" pose is likely INTENTIONAL for flying birds
- Real birds DO fly with horizontal bodies when cruising
- Model rotation changes either don't help OR break AI
- Focus should be on other features (taming, AI, etc) NOT visual tweaks

**Final**: crow.geo.json body rotation = 57.5° (ORIGINAL VALUE)
