# Epic 03 Strategy Change - Asset Extraction Workflow

**Date**: 2025-10-26
**Reason**: Alexthe666 granted permission to use original Alex's Mobs assets
**Impact**: MAJOR - Changes entire development workflow

## What Changed

### OLD Strategy (Before Permission)
❌ Create all assets from scratch:
1. Learn Blockbench
2. Model each mob manually (15-20h per mob)
3. Create animations in Blockbench
4. Draw textures in image editor
5. Source sounds from Freesound.org

**Problems:**
- Very time-consuming (15-20h per mob × 90 mobs = 1,350-1,800 hours!)
- Quality depends on artist skill (learning curve)
- Assets won't match original look
- Requires Blockbench expertise

### NEW Strategy (With Permission)
✅ Extract and convert original assets:
1. Extract assets from Alex's Mobs 1.22.9 JAR
2. Convert Citadel models to GeckoLib format
3. Copy textures and sounds (no conversion needed)
4. Adapt Forge entity code to Fabric patterns
5. Test and validate

**Benefits:**
- Much faster (6-10h per mob × 90 mobs = 540-900 hours - 60% time savings!)
- Professional quality (original artist's work)
- Authentic look matching original mod
- No Blockbench learning required

## Technical Changes Needed

### 1. Asset Extraction
**What to extract from Alex's Mobs 1.22.9 JAR:**
- Models: `.json` geometry files (Citadel format)
- Textures: `.png` files (can use directly)
- Animations: `.json` animation files (Citadel format)
- Sounds: `.ogg` files (can use directly)
- Code: Entity classes (for reference, need Fabric conversion)

**Tools needed:**
- JAR extractor (7-Zip, WinRAR, or `jar xf` command)
- Text editor for viewing JSON
- Optional: Decompiler for viewing original code

### 2. Format Conversion

**Citadel vs GeckoLib:**
Both use similar JSON-based formats, but differences exist:

| Feature | Citadel (Forge) | GeckoLib (Fabric/Forge) |
|---------|-----------------|-------------------------|
| Model format | Custom JSON | Bedrock Edition JSON |
| Animation format | Custom JSON | Bedrock Edition JSON |
| Bone hierarchy | Similar | Similar |
| Texture mapping | UV coordinates | UV coordinates |
| File structure | Different | Different |

**Conversion Options:**

**Option A: Manual Conversion**
- Open Citadel .json in text editor
- Reformat to GeckoLib/Bedrock format
- Validate in Blockbench (optional)
- Pros: Full control, no tools needed
- Cons: Time-consuming, error-prone

**Option B: Automated Converter**
- Write Java/Python script to convert JSON
- Parse Citadel format → Output GeckoLib format
- Batch process all 90+ mobs
- Pros: Fast, reusable, less errors
- Cons: Need to understand both formats

**Option C: Use Blockbench as Intermediary**
- Import Citadel model into Blockbench (if supported)
- Export as GeckoLib format
- Pros: Visual verification, easier
- Cons: Still manual per-mob, requires Blockbench

**Option D: Check if GeckoLib supports Citadel directly**
- Research if GeckoLib can load Citadel models
- Might not need conversion at all
- Pros: No work needed!
- Cons: Unlikely, but worth checking

### 3. Code Adaptation (Forge → Fabric)

**Already planned, no change:**
- Entity classes: Forge → Fabric API patterns
- Registration: Forge Registry → Fabric Registry
- Rendering: Forge RenderType → Fabric RenderLayer
- AI Goals: Mostly compatible, minor changes

**Now easier with reference:**
- Can see original Forge code
- Understand intended behavior
- Copy logic, adapt syntax

## Impact on Epic 03 Tasks

### Tasks That Change:

**TASK-001: Set Up Blockbench**
- ~~OLD: Learn Blockbench, create templates~~
- **NEW: Extract assets from JAR, analyze formats**
- Time: Still 2-3 hours

**TASK-004: Create Fly Model**
- ~~OLD: Model in Blockbench (15-20h)~~
- **NEW: Extract + convert Fly assets (2-3h)**
- Massive time savings!

**TASK-008: Create Cockroach Model**
- ~~OLD: Model in Blockbench (12-18h)~~
- **NEW: Extract + convert Cockroach assets (2-3h)**

**TASK-012: Create Triops Model**
- ~~OLD: Model in Blockbench (12-18h)~~
- **NEW: Extract + convert Triops assets (2-3h)**

### New Estimated Timeline:

| Phase | OLD Estimate | NEW Estimate | Savings |
|-------|--------------|--------------|---------|
| Phase 1 (Foundation) | 7-10h | 7-10h | 0h (no change) |
| Phase 2 (Fly) | 29-41h | 16-24h | **13-17h saved** |
| Phase 3 (Cockroach) | 23-36h | 13-21h | **10-15h saved** |
| Phase 4 (Triops) | 23-36h | 13-21h | **10-15h saved** |
| Phase 5 (Validation) | 12-19h | 12-19h | 0h (no change) |
| **TOTAL** | **94-142h** | **61-95h** | **33-47h saved (35-45%)** |

Epic 03 now takes **1.5-2 weeks** instead of **2-3 weeks**!

## Immediate Action Items

1. **Extract Assets**:
   - Download Alex's Mobs 1.22.9 JAR
   - Extract to temporary directory
   - Organize by asset type (models, textures, sounds)

2. **Analyze Formats**:
   - Open Citadel model JSON for Fly
   - Compare to GeckoLib format
   - Identify conversion mapping

3. **Choose Conversion Strategy**:
   - Decide: Manual, Automated, Blockbench, or Direct use?
   - Test with 1 mob (Fly) first
   - If successful, apply to all 90+ mobs

4. **Update Plan**:
   - Revise TASK-001, TASK-004, TASK-008, TASK-012
   - Update time estimates
   - Adjust CURRENT_EPIC.md

5. **Credit Original Author**:
   - Add attribution to mod description
   - Credit in-game (if requested)
   - Link to original mod

## Risks and Mitigations

**Risk 1: Citadel → GeckoLib conversion is complex**
- Mitigation: Start with simplest mob (Fly), learn from first conversion
- Fallback: Use Blockbench as intermediary (visual tool)

**Risk 2: Animations don't convert properly**
- Mitigation: Test thoroughly, fix broken animations manually
- Fallback: Recreate simple animations (idle, walk, death) if needed

**Risk 3: Textures don't map correctly**
- Mitigation: Verify UV coordinates match
- Fallback: Re-export from Blockbench with corrected UVs

**Risk 4: Sounds are incompatible format**
- Mitigation: Sounds are .ogg (standard), should work directly
- Fallback: Re-encode with Audacity/FFmpeg

## Next Steps

**Right now:**
1. Extract Alex's Mobs 1.22.9 assets
2. Analyze Fly model format (Citadel JSON)
3. Research Citadel → GeckoLib conversion
4. Test conversion with Fly (proof of concept)

**Then:**
5. Update Epic 03 plan with new workflow
6. Continue with TASK-001 (now asset extraction, not Blockbench learning)
7. Complete Epic 03 faster with original assets

---

**Summary**: With permission granted, we can save **33-47 hours** on Epic 03 alone by using original assets instead of creating from scratch. This changes everything!

**Status**: Strategy updated, ready to extract assets and analyze conversion requirements.
