# Citadel → GeckoLib Migration Strategy

**Date**: 2025-10-26
**Issue**: Alex's Mobs uses Citadel (code-based models), not GeckoLib (JSON models)
**Impact**: We cannot simply copy model files - they don't exist in JSON format!

## The Problem

**What we expected:**
- GeckoLib models: `.geo.json` files (Bedrock Edition JSON format)
- GeckoLib animations: `.animation.json` files
- Easy conversion: Copy → Convert format → Done

**What we actually have:**
- **Citadel models**: Defined in **Java code** (not JSON files)
- Model structure: Java classes extending `AdvancedEntityModel<T>`
- Animations: Java code using Citadel's animation system
- NO `.geo.json` or `.animation.json` files to extract

## Available Assets from Alex's Mobs

**✅ CAN use directly:**
1. **Textures** (405 entity textures) - `.png` files work in both systems
2. **Sounds** (575 sound files) - `.ogg` files work universally
3. **Loot tables** - JSON format, mostly compatible
4. **Translations** - JSON format, compatible

**❌ CANNOT use directly:**
1. **Models** - Java code in Citadel format, not GeckoLib JSON
2. **Animations** - Java code in Citadel format, not GeckoLib JSON
3. **Entity behavior code** - Forge-specific, needs Fabric conversion anyway

## Our Options

### Option 1: Decompile and Convert Citadel Models (Complex)

**Process:**
1. Decompile `ModelFly.class` from JAR
2. Analyze Citadel model structure (cubes, bones, pivot points)
3. Manually recreate in Blockbench OR write code to generate GeckoLib JSON
4. Repeat for 90+ mobs

**Pros:**
- Get exact geometry from original
- Accurate to original mod

**Cons:**
- Very time-consuming (decompile → analyze → recreate for each mob)
- Requires understanding Citadel API
- Legal gray area (decompiling, even with permission)
- Still need to create animations from scratch

**Time Estimate:** 10-15 hours per mob (still high!)

---

### Option 2: Use Textures + Simple Programmatic Models (FASTEST)

**Process:**
1. Copy textures directly (already have them!)
2. Generate simple cube-based GeckoLib models programmatically
3. Create basic animations (idle, walk, death)
4. Iterate and improve quality over time

**Pros:**
- ✅ **FAST**: 2-3 hours per mob (not 15-20h!)
- ✅ Legal and ethical (using only textures with permission)
- ✅ No Blockbench learning required
- ✅ Fully reproducible and version-controlled
- ✅ Good enough for proof-of-concept

**Cons:**
- Models won't match original quality initially
- Will need manual refinement later for production

**Time Estimate:** 2-3 hours per mob for Epic 03

---

### Option 3: Blockbench from Scratch (Learning Required)

**Process:**
1. Learn Blockbench (2-3 hours)
2. Study original textures for reference
3. Model each mob manually in Blockbench
4. Create animations manually
5. Export to GeckoLib format

**Pros:**
- Full control over quality
- Can match original if skilled enough
- Learn valuable 3D modeling skill

**Cons:**
- Steep learning curve
- Very time-consuming (15-20h per mob)
- Quality depends on artistic skill

**Time Estimate:** 15-20 hours per mob

---

### Option 4: Hire 3D Artist (Costs Money)

**Process:**
1. Provide textures to artist
2. Artist creates models in Blockbench
3. Review and iterate
4. Export to GeckoLib

**Pros:**
- Professional quality
- No learning curve
- Potentially faster than DIY

**Cons:**
- Costs money ($20-50 per mob × 90 mobs = $1,800-4,500!)
- Need to find and coordinate with artist
- Communication overhead

**Time Estimate:** Depends on artist availability

---

## Recommended Strategy for Epic 03

**Use Option 2: Programmatic Generation with Original Textures**

### Why This Works Best:

1. **Permission covers textures** ✅
   - Alexthe666 said we can use his code/assets
   - Textures are the most important visual asset
   - Textures + simple geometry = recognizable mobs

2. **Fast iteration** ✅
   - Complete Epic 03 in 1-2 weeks (not 3-4 weeks)
   - Prove GeckoLib workflow works
   - Get mobs in-game quickly for testing

3. **Improvement path** ✅
   - Start with simple models
   - Refine in later epics
   - Can always improve quality incrementally

4. **Educational value** ✅
   - Learn GeckoLib JSON format
   - Understand model structure
   - Build tools for future mobs

### Implementation Plan:

**For Fly (TASK-004):**
1. Copy `fly.png` texture from extracted assets ✅ (already have it!)
2. Generate simple `fly.geo.json` programmatically:
   - Body: 1 cube (4×3×3 pixels)
   - Head: 1 cube (3×3×3 pixels)
   - Wings: 2 cubes (6×4×1 pixels, transparent)
   - Legs: 6 cubes (1×3×1 pixels)
3. Generate `fly.animation.json` with 3 basic animations:
   - Idle: Gentle bob (wings at rest)
   - Fly: Wing flap (rapid rotation)
   - Death: Tumble fall (rotation + fall)
4. Copy sound files directly ✅
5. Test in-game

**Time:** 2-3 hours (not 15-20h!)

**Quality:**
- ✅ Looks like a fly (texture is original!)
- ✅ Animations work
- ✅ Good enough for Epic 03 proof-of-concept
- ⚠️ Geometry simpler than original (but functional)

---

## Long-Term Strategy (Epic 04+)

### Phase 1: Proof of Concept (Epic 03)
- Use programmatic generation + original textures
- 3 ultra-simple mobs (Fly, Cockroach, Triops)
- Validate workflow end-to-end

### Phase 2: Simple Mobs (Epic 04-06)
- Continue programmatic generation
- Gradually improve model quality
- Build library of reusable patterns

### Phase 3: Complex Mobs (Epic 07-10)
- Learn Blockbench for complex mobs
- OR hire artist for hero mobs (Elephant, Orca, etc.)
- Use programmatic base + manual refinement

### Phase 4: Polish (Epic 11-15)
- Revisit early mobs
- Improve model quality
- Match original quality where possible

---

## Action Plan for You

**Right Now:**
1. ✅ We have textures extracted
2. ✅ We have sounds extracted
3. ✅ We have loot tables
4. ❌ We need to generate models programmatically

**Next Step:**
I'll create a **model generator tool** that:
- Takes mob specifications (cube sizes, bone hierarchy)
- Generates `.geo.json` files automatically
- Generates `.animation.json` files automatically
- No Blockbench needed!

**For Fly specifically:**
- Copy `fly.png` texture → Our assets folder
- Copy sound files → Our assets folder
- Generate `fly.geo.json` (programmatic)
- Generate `fly.animation.json` (programmatic)
- Test in-game!

---

## Summary

**We CAN use Alex's Mobs assets**, but:
- ✅ Textures: Direct copy
- ✅ Sounds: Direct copy
- ✅ Loot tables: Direct copy
- ❌ Models: Must recreate (Citadel ≠ GeckoLib)
- ❌ Animations: Must recreate

**Best approach:**
- Use **original textures** (biggest visual impact!)
- **Generate models** programmatically (fast, good enough)
- **Improve later** (incremental quality increases)

**Time savings:**
- Old estimate: 15-20h per mob (Blockbench from scratch)
- New estimate: 2-3h per mob (programmatic + textures)
- **Savings: 80-85% faster!**

---

**Ready to proceed?** I can create the model generator and start with Fly assets right now!
