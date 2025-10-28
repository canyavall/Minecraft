# Citadel to GeckoLib Conversion Process

## Current Status: Missing Step Identified

### What We Have ✅
1. **Alex's Mobs JAR** - Original Forge mod
   - Location: `.claude/epics/02-mob-catalog-asset-inventory/alexsmobs-1.22.9.jar`

2. **Extracted JAR Contents** - Unzipped the JAR
   - Location: `.claude/epics/02-mob-catalog-asset-inventory/alexsmobs-extracted/`
   - Contains:
     - ✅ Textures (PNG files) - `assets/alexsmobs/textures/entity/`
     - ✅ Sounds (OGG files) - `assets/alexsmobs/sounds/`
     - ✅ Compiled classes (`.class` files) - `com/github/alexthe666/alexsmobs/client/model/`
     - ❌ **NOT decompiled Java source code**

3. **Our Current Implementation**
   - ✅ Entity classes (Java) - Using Fabric patterns
   - ✅ Textures - Extracted from Alex's Mobs (correct)
   - ❌ **Models** - Placeholder cube geometry (WRONG)
   - ❌ **Animations** - Placeholder keyframes (WRONG)

### The Problem
We're using **placeholder GeckoLib models** instead of the real Citadel models.
- The textures are correct (extracted from JAR)
- The 3D geometry is wrong (manually created placeholders)
- Result: Texture draped over wrong shape

---

## Complete Conversion Process

### Step 1: Decompile Citadel Model Classes ⚠️ MISSING
**Input:** `ModelCockroach.class` (compiled bytecode)
**Output:** `ModelCockroach.java` (readable source code)

**Tool:** Java decompiler (CFR, Fernflower, JD-CLI)

**Command Example:**
```bash
# Using CFR decompiler
java -jar cfr.jar ModelCockroach.class --outputdir ./decompiled/
```

**What This Gives Us:**
```java
// Example of what we'll see in ModelCockroach.java
public class ModelCockroach extends AdvancedEntityModel<EntityCockroach> {
    private final AdvancedModelBox body;
    private final AdvancedModelBox head;
    private final AdvancedModelBox leg1;
    // ... etc

    public ModelCockroach() {
        this.texWidth = 64;
        this.texHeight = 32;

        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.addBox(-2.0F, -2.0F, -3.0F, 4, 3, 6);

        this.leg1 = new AdvancedModelBox(this);
        this.leg1.setRotationPoint(1.5F, 0.0F, -2.0F);
        this.body.addChild(this.leg1);
        this.leg1.addBox(0.0F, 0.0F, -0.5F, 3, 1, 1);
        // ... more bones and cubes
    }
}
```

---

### Step 2: Parse Citadel Model to Extract Geometry
**Input:** `ModelCockroach.java` (decompiled source)
**Output:** Structured bone/cube data

**Tool:** `citadel_model_parser.py` (we created this)

**What It Extracts:**
- Bone hierarchy (parent-child relationships)
- Rotation points (pivot points for each bone)
- Cube definitions (position, size, UV coordinates)
- Texture dimensions

**Example Output:**
```json
{
  "bones": [
    {
      "name": "body",
      "parent": "root",
      "pivot": [0, 20, 0],
      "cubes": [
        {
          "origin": [-2, -2, -3],
          "size": [4, 3, 6],
          "uv": [0, 0]
        }
      ]
    },
    {
      "name": "leg1",
      "parent": "body",
      "pivot": [1.5, 0, -2],
      "cubes": [...]
    }
  ]
}
```

---

### Step 3: Convert to GeckoLib Format
**Input:** Parsed bone/cube data
**Output:** GeckoLib `.geo.json` file

**Tool:** `citadel_model_parser.py` (continuation of step 2)

**GeckoLib Format (Bedrock/Minecraft format):**
```json
{
  "format_version": "1.12.0",
  "minecraft:geometry": [{
    "description": {
      "identifier": "geometry.cockroach",
      "texture_width": 64,
      "texture_height": 32
    },
    "bones": [
      {
        "name": "body",
        "pivot": [0, 20, 0],
        "cubes": [
          {
            "origin": [-2, 18, -3],
            "size": [4, 3, 6],
            "uv": [0, 0]
          }
        ]
      }
    ]
  }]
}
```

**Output Location:** `src/main/resources/assets/xeenaa-alexs-mobs/geo/cockroach.geo.json`

---

### Step 4: Extract Animation Data (Similar Process)
**Input:** Decompiled model or animation classes
**Output:** GeckoLib `.animation.json`

**Citadel Animation Code:**
```java
// In model or entity class
public void setupAnim(...) {
    this.leg1.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    this.leg2.yRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    // ... procedural animations
}
```

**Convert to GeckoLib Keyframes:**
```json
{
  "format_version": "1.8.0",
  "animations": {
    "walk": {
      "loop": true,
      "animation_length": 1.0,
      "bones": {
        "leg1": {
          "rotation": {
            "0.0": [0, -80, 0],
            "0.5": [0, 80, 0],
            "1.0": [0, -80, 0]
          }
        }
      }
    }
  }
}
```

---

## Why We're Not Done Yet

### Current Files (WRONG):
```
src/main/resources/assets/xeenaa-alexs-mobs/
├── geo/
│   ├── cockroach_citadel.geo.json  ❌ PLACEHOLDER (manual cubes)
│   └── triops_citadel.geo.json     ❌ PLACEHOLDER (manual cubes)
├── animations/
│   ├── cockroach_citadel.animation.json  ❌ PLACEHOLDER (basic keyframes)
│   └── triops_citadel.animation.json     ❌ PLACEHOLDER (basic keyframes)
└── textures/entity/
    ├── cockroach/cockroach.png  ✅ REAL (extracted from JAR)
    └── triops/triops.png        ✅ REAL (extracted from JAR)
```

### What We Need:
1. **Decompile** the `.class` files to get Java source
2. **Parse** the Citadel model code to extract geometry
3. **Convert** to GeckoLib JSON format
4. **Replace** our placeholder files with real geometry

---

## Next Action: Decompile ONE Model

**Test Case:** `ModelFly.class` (simplest mob)

**Why Fly First:**
- Simplest geometry (tiny insect)
- Already has working texture
- Fast to validate if process works

**Steps:**
1. Decompile `ModelFly.class` → `ModelFly.java`
2. Read the Java code to understand Citadel structure
3. Manually convert ONE bone to GeckoLib format to test
4. If it works, decide: automate or manually convert key mobs

**Decision Point After Test:**
- If conversion is straightforward → Automate with Python tool
- If complex edge cases → Manually convert Epic 03 mobs (6 small mobs)
- Then decide on approach for remaining 84 mobs

---

## Tools Available

### Decompilers:
1. **CFR** - Modern, good output
2. **Fernflower** - IntelliJ's decompiler
3. **JD-CLI** - Command line Java decompiler

### Our Automation:
- `citadel_model_parser.py` - Parses Java → GeckoLib
- `citadel_animation_converter.py` - Converts animations
- `batch_convert.py` - Orchestrates full pipeline

---

## Success Criteria

**After fixing models, we should see:**
1. Cockroach with proper segmented body and 6 legs
2. Triops with correct shell shape and tail
3. Animations that match the original Alex's Mobs
4. No visual glitches or UV mapping issues

**Current vs Expected:**
- Current: Boxy placeholder with real texture stretched wrong
- Expected: Detailed model matching original Alex's Mobs exactly
