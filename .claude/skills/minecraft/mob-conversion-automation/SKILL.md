---
tags: [minecraft, automation, citadel, geckolib, code-generation, tools]
activationPhrase: "mob conversion automation"
skillPriority: high
autoActivate: true
activationConditions:
  - contentMatch: "code generator|automation tool|CitadelModelParser|EntityCodeGenerator"
  - contentMatch: "decompile|batch conversion|template generation"
---

# Mob Conversion Automation Skill

**Purpose**: Provides code generator designs, automation tools, and templates to streamline Citadel → GeckoLib conversion for the remaining 89 mobs.

**Goal**: Reduce conversion time from 45-60 minutes to 10-15 minutes per mob through automation.

**Based on**: Validated Fly entity conversion framework (Epic 03, Approach C)

---

## Table of Contents

1. [Automation Strategy Overview](#automation-strategy-overview)
2. [Code Generator Designs](#code-generator-designs)
3. [Template System](#template-system)
4. [Batch Processing Tools](#batch-processing-tools)
5. [Manual vs Automated Tasks](#manual-vs-automated-tasks)
6. [Time Savings Analysis](#time-savings-analysis)
7. [Implementation Guide](#implementation-guide)

---

## Automation Strategy Overview

### What Can Be Automated (80%)

**Fully Automated** (requires no manual intervention):
1. ✅ **Decompilation**: Extract Java classes from JAR
2. ✅ **Model geometry conversion**: Parse Java → JSON bone hierarchy
3. ✅ **Entity class generation**: Create boilerplate entity code
4. ✅ **Model/renderer generation**: Create GeckoLib model and renderer classes
5. ✅ **Registration code**: Generate mod initialization snippets
6. ✅ **Asset copying**: Copy textures, sounds, loot tables

**Semi-Automated** (requires template customization):
7. ⚠️ **Animation conversion**: Convert Citadel helpers to keyframes (patterns recognizable)
8. ⚠️ **Spawn egg registration**: Generate item registration with texture paths

### What Requires Manual Work (20%)

**Cannot Be Automated** (unique per mob):
1. ❌ **Unique behaviors**: Special abilities, custom AI logic
2. ❌ **Complex animation timing**: Attack combos, state-specific animations
3. ❌ **Interaction logic**: Breeding, taming, riding mechanics
4. ❌ **Testing & tuning**: Movement speeds, animation timing, visual quality

---

## Code Generator Designs

### Tool 1: CitadelModelParser

**Purpose**: Parse decompiled Citadel model Java code to extract bone hierarchy and geometry.

**Input**: Decompiled `Model*.java` file
**Output**: GeckoLib `.geo.json` file

**Core Logic**:
```python
class CitadelModelParser:
    def parse(self, java_file_path):
        """Parse Citadel model to extract bone structure."""
        bones = []
        texture_dimensions = self.extract_texture_size(java_file_path)

        # Find all AdvancedModelBox instantiations
        bone_definitions = re.findall(
            r'this\.(\w+)\s*=\s*new\s+AdvancedModelBox\(this,\s*"([^"]+)"\);',
            file_content
        )

        for var_name, bone_name in bone_definitions:
            bone = {
                "name": bone_name,
                "pivot": self.extract_pivot(var_name),
                "rotation": self.extract_rotation(var_name),
                "cubes": self.extract_cubes(var_name),
                "parent": self.extract_parent(var_name)
            }
            bones.append(bone)

        return self.build_geckolib_geo_json(bones, texture_dimensions)

    def extract_pivot(self, var_name):
        """Extract setRotationPoint calls."""
        pattern = rf'{var_name}\.setRotationPoint\(([^,]+),\s*([^,]+),\s*([^\)]+)\)'
        match = re.search(pattern, self.content)
        if match:
            return [float(match.group(1)), float(match.group(2)), float(match.group(3))]
        return [0, 0, 0]

    def extract_cubes(self, var_name):
        """Extract addBox/setTextureOffset calls."""
        cubes = []
        # Find setTextureOffset
        texture_offset = re.search(
            rf'{var_name}\.setTextureOffset\(([^,]+),\s*([^\)]+)\)',
            self.content
        )

        # Find addBox
        box_pattern = rf'{var_name}\.addBox\(([^,]+),\s*([^,]+),\s*([^,]+),\s*([^,]+),\s*([^,]+),\s*([^\)]+)\)'
        for match in re.finditer(box_pattern, self.content):
            cube = {
                "origin": [float(match.group(1)), float(match.group(2)), float(match.group(3))],
                "size": [float(match.group(4)), float(match.group(5)), float(match.group(6))],
                "uv": [float(texture_offset.group(1)), float(texture_offset.group(2))] if texture_offset else [0, 0]
            }
            cubes.append(cube)

        return cubes

    def extract_parent(self, var_name):
        """Find addChild relationships."""
        parent_pattern = rf'(\w+)\.addChild\({var_name}\)'
        match = re.search(parent_pattern, self.content)
        if match:
            return match.group(1)  # Return parent variable name
        return None
```

**Example Usage**:
```bash
python citadel_model_parser.py --input decompiled/ModelFly.java --output geo/fly_citadel.geo.json
```

**Output**:
```json
{
  "format_version": "1.12.0",
  "minecraft:geometry": [{
    "description": {
      "identifier": "geometry.fly",
      "texture_width": 32,
      "texture_height": 32
    },
    "bones": [...]
  }]
}
```

---

### Tool 2: CitadelAnimationConverter

**Purpose**: Convert Citadel procedural animation helpers to GeckoLib keyframe JSON.

**Input**: Decompiled model with animation code (inside `render()` or `setAngles()`)
**Output**: GeckoLib `.animation.json` file

**Core Logic**:
```python
class CitadelAnimationConverter:
    def parse_animations(self, java_file_path):
        """Extract animation logic from Citadel model."""
        animations = {}

        # Recognize common patterns
        animations["idle"] = self.extract_idle_animation()
        animations["walk"] = self.extract_walk_animation()
        animations["fly"] = self.extract_fly_animation()
        animations["death"] = self.create_default_death_animation()

        return self.build_geckolib_animation_json(animations)

    def extract_fly_animation(self):
        """Recognize flap() helper pattern."""
        flap_calls = re.findall(
            r'this\.flap\((\w+),\s*([^,]+),\s*([^,]+),\s*(true|false)',
            self.content
        )

        bones = {}
        for bone_var, speed, degree, inverted in flap_calls:
            bone_name = self.resolve_bone_name(bone_var)
            angle = float(degree)

            # Generate keyframes for wing flapping
            bones[bone_name] = {
                "rotation": {
                    "0.0": [0, 0, angle if not inverted else -angle],
                    "0.25": [0, 0, -angle if not inverted else angle],
                    "0.5": [0, 0, angle if not inverted else -angle],
                    "0.75": [0, 0, -angle if not inverted else angle],
                    "1.0": [0, 0, angle if not inverted else -angle]
                }
            }

        return {
            "loop": True,
            "animation_length": 1.0,
            "bones": bones
        }

    def extract_walk_animation(self):
        """Recognize walk() helper pattern."""
        walk_calls = re.findall(
            r'this\.walk\((\w+),\s*([^,]+),\s*([^,]+),\s*(true|false)',
            self.content
        )

        bones = {}
        for bone_var, speed, degree, inverted in walk_calls:
            bone_name = self.resolve_bone_name(bone_var)
            angle = float(degree)

            # Generate keyframes for leg walking
            bones[bone_name] = {
                "rotation": {
                    "0.0": [angle if not inverted else -angle, 0, 0],
                    "0.5": [-angle if not inverted else angle, 0, 0],
                    "1.0": [angle if not inverted else -angle, 0, 0]
                }
            }

        return {
            "loop": True,
            "animation_length": 1.0,
            "bones": bones
        }
```

**Example Usage**:
```bash
python citadel_animation_converter.py --input decompiled/ModelFly.java --output animations/fly_citadel.animation.json
```

**Limitations**:
- ⚠️ Can recognize common patterns (`flap`, `walk`, `swing`, `bob`)
- ⚠️ Complex custom animations require manual tweaking
- ⚠️ Timing (`animation_length`) may need tuning based on speed testing

---

### Tool 3: EntityCodeGenerator

**Purpose**: Generate boilerplate entity class with GeckoLib integration.

**Input**: Mob name, type (flying/swimming/walking), attributes (health, speed)
**Output**: Complete `*Entity.java` file

**Template Variables**:
```python
ENTITY_TEMPLATE = """
package com.canya.xeenaa_alexs_mobs.entity.{category};

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.{BASE_CLASS};
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class {NAME}Entity extends {BASE_CLASS} implements GeoEntity {{
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public {NAME}Entity(EntityType<? extends {BASE_CLASS}> entityType, World world) {{
        super(entityType, world);
    }}

    public static DefaultAttributeContainer.Builder createAttributes() {{
        return {BASE_CLASS}.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, {HEALTH}D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, {SPEED}D)
            {FLYING_SPEED_LINE}
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, {FOLLOW_RANGE}D);
    }}

    @Override
    protected void initGoals() {{
        {GOALS}
    }}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {{
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }}

    private PlayState predicate(AnimationState<{NAME}Entity> state) {{
        {ANIMATION_LOGIC}
        return PlayState.CONTINUE;
    }}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {{
        return cache;
    }}
}}
"""

class EntityCodeGenerator:
    def generate(self, config):
        """Generate entity class from configuration."""
        base_class = self.get_base_class(config["type"])
        animation_logic = self.get_animation_logic(config["type"])
        goals = self.get_default_goals(config["type"])

        return ENTITY_TEMPLATE.format(
            category=config["category"],
            NAME=config["name"],
            BASE_CLASS=base_class,
            HEALTH=config["health"],
            SPEED=config["speed"],
            FLYING_SPEED_LINE=f'.add(EntityAttributes.GENERIC_FLYING_SPEED, {config.get("flying_speed", 0.6)}D)' if config["type"] == "flying" else "",
            FOLLOW_RANGE=config.get("follow_range", 16.0),
            GOALS=goals,
            ANIMATION_LOGIC=animation_logic
        )

    def get_base_class(self, mob_type):
        return {
            "flying": "FlyingAnimalEntity",
            "swimming": "FishEntity",
            "walking": "AnimalEntity",
            "hostile": "HostileEntity"
        }[mob_type]

    def get_animation_logic(self, mob_type):
        if mob_type == "flying":
            return '''
        if (this.isOnGround()) {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
        }
            '''
        elif mob_type == "swimming":
            return '''
        if (this.isSubmergedInWater()) {
            state.getController().setAnimation(RawAnimation.begin().then("swim", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
            '''
        else:
            return '''
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
            '''
```

**Example Usage**:
```bash
python entity_code_generator.py --config configs/fly.json --output src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/FlyEntity.java
```

**Config File** (`configs/fly.json`):
```json
{
  "name": "Fly",
  "category": "animal",
  "type": "flying",
  "health": 2.0,
  "speed": 0.6,
  "flying_speed": 0.8,
  "follow_range": 8.0
}
```

---

### Tool 4: ModelRendererGenerator

**Purpose**: Generate GeckoLib model and renderer classes.

**Input**: Mob name
**Output**: `*Model.java` and `*Renderer.java`

**Templates**:

**Model Template**:
```java
package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.{NAME}Entity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class {NAME}Model extends GeoModel<{NAME}Entity> {
    @Override
    public Identifier getModelResource({NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/{name}_citadel.geo.json");
    }

    @Override
    public Identifier getTextureResource({NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/{name}/{name}.png");
    }

    @Override
    public Identifier getAnimationResource({NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/{name}_citadel.animation.json");
    }
}
```

**Renderer Template**:
```java
package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.{NAME}Model;
import com.canya.xeenaa_alexs_mobs.entity.animal.{NAME}Entity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class {NAME}Renderer extends GeoEntityRenderer<{NAME}Entity> {
    public {NAME}Renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new {NAME}Model());
        this.shadowRadius = {SHADOW_RADIUS}f;
    }

    @Override
    public Identifier getTextureLocation({NAME}Entity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/{name}/{name}.png");
    }
}
```

**Usage**:
```bash
python model_renderer_generator.py --name Fly --shadow-radius 0.2
```

---

### Tool 5: RegistrationCodeGenerator

**Purpose**: Generate registration snippets for `ModInitializer`.

**Input**: Mob name, spawn egg colors
**Output**: Registration code snippets

**Generated Code**:
```java
// Entity Registration
public static final EntityType<FlyEntity> FLY = Registry.register(
    Registries.ENTITY_TYPE,
    Identifier.of("xeenaa-alexs-mobs", "fly"),
    EntityType.Builder.create(FlyEntity::new, SpawnGroup.CREATURE)
        .dimensions(0.3f, 0.3f)
        .build()
);

// Spawn Egg Registration
public static final Item FLY_SPAWN_EGG = Registry.register(
    Registries.ITEM,
    Identifier.of("xeenaa-alexs-mobs", "fly_spawn_egg"),
    new SpawnEggItem(ModEntities.FLY, 0x3B3B3B, 0x8B0000, new Item.Settings())
);

// Client Renderer Registration
EntityRendererRegistry.register(ModEntities.FLY, FlyRenderer::new);

// Attributes Registration
FabricDefaultAttributeRegistry.register(ModEntities.FLY, FlyEntity.createAttributes());
```

**Usage**:
```bash
python registration_code_generator.py --name Fly --primary-color 0x3B3B3B --secondary-color 0x8B0000 --dimensions 0.3 0.3
```

---

### Tool 6: AssetCopyScript

**Purpose**: Batch copy textures, sounds, loot tables from original mod JAR.

**Input**: Mob name list, source JAR path
**Output**: Copied assets in correct directory structure

**Script Logic**:
```python
import zipfile
import shutil
import os

class AssetCopyScript:
    def copy_assets(self, mob_name, source_jar, destination_dir):
        """Copy all assets for a mob from original JAR."""

        with zipfile.ZipFile(source_jar, 'r') as jar:
            # Copy textures
            texture_pattern = f"assets/alexsmobs/textures/entity/{mob_name.lower()}/"
            self.copy_matching_files(jar, texture_pattern,
                f"{destination_dir}/textures/entity/{mob_name.lower()}/")

            # Copy sounds
            sound_pattern = f"assets/alexsmobs/sounds/entity/{mob_name.lower()}/"
            self.copy_matching_files(jar, sound_pattern,
                f"{destination_dir}/sounds/entity/{mob_name.lower()}/")

            # Copy loot tables
            loot_pattern = f"data/alexsmobs/loot_tables/entities/{mob_name.lower()}.json"
            self.copy_matching_files(jar, loot_pattern,
                f"{destination_dir}/loot_tables/entities/")

    def copy_matching_files(self, jar, pattern, destination):
        """Extract files matching pattern from JAR."""
        for file_info in jar.filelist:
            if pattern in file_info.filename:
                jar.extract(file_info, destination)
```

**Usage**:
```bash
python asset_copy_script.py --mob Fly --source alexsmobs-1.22.9.jar --dest src/main/resources/assets/xeenaa-alexs-mobs/
```

---

## Template System

### Configuration-Driven Approach

**Mob Config Template** (`templates/mob_config.json`):
```json
{
  "name": "Fly",
  "category": "animal",
  "type": "flying",
  "attributes": {
    "health": 2.0,
    "speed": 0.6,
    "flying_speed": 0.8,
    "follow_range": 8.0
  },
  "dimensions": {
    "width": 0.3,
    "height": 0.3
  },
  "spawn_egg": {
    "primary_color": "0x3B3B3B",
    "secondary_color": "0x8B0000"
  },
  "renderer": {
    "shadow_radius": 0.2
  },
  "ai_goals": [
    "WanderAroundGoal",
    "LookAtEntityGoal",
    "LookAroundGoal"
  ],
  "animations": {
    "idle": "auto",
    "fly": "auto",
    "death": "default"
  }
}
```

**Usage**:
```bash
# Generate all files for a mob from config
python mob_generator.py --config templates/fly_config.json

# Outputs:
# - src/main/java/.../entity/animal/FlyEntity.java
# - src/main/java/.../client/model/FlyModel.java
# - src/main/java/.../client/renderer/FlyRenderer.java
# - src/main/resources/assets/.../geo/fly_citadel.geo.json
# - src/main/resources/assets/.../animations/fly_citadel.animation.json
# - Registration snippets (printed to console)
```

---

## Batch Processing Tools

### Batch Decompilation

**Script**: `batch_decompile.py`

**Purpose**: Decompile all 90 mob classes from Alex's Mobs JAR in one go.

```python
import subprocess
import os

MOBS = [
    "Fly", "Cockroach", "Triops", "Alligator", "Bear",
    # ... (all 90 mobs)
]

def batch_decompile(jar_path, output_dir):
    """Decompile all mob classes."""
    for mob in MOBS:
        classes = [
            f"com/github/alexthe666/alexsmobs/entity/Entity{mob}",
            f"com/github/alexthe666/alexsmobs/client/model/Model{mob}",
            f"com/github/alexthe666/alexsmobs/client/render/Render{mob}"
        ]

        for cls in classes:
            subprocess.run([
                "java", "-jar", "fernflower.jar",
                "-dgs=1",
                jar_path,
                f"{output_dir}/{mob}/"
            ])
```

**Usage**:
```bash
python batch_decompile.py --jar alexsmobs-1.22.9.jar --output .claude/temp/decompiled/
```

**Time Savings**: 90 mobs × 2 min each = 180 min → 5 minutes total

---

### Batch Conversion Pipeline

**Script**: `batch_convert.py`

**Purpose**: Run entire conversion pipeline for multiple mobs.

```python
def convert_mob_batch(mob_list, config):
    """Convert multiple mobs in sequence."""
    for mob_name in mob_list:
        print(f"Converting {mob_name}...")

        # Step 1: Parse model geometry
        run_tool("CitadelModelParser",
                 input=f"decompiled/Model{mob_name}.java",
                 output=f"geo/{mob_name.lower()}_citadel.geo.json")

        # Step 2: Convert animations
        run_tool("CitadelAnimationConverter",
                 input=f"decompiled/Model{mob_name}.java",
                 output=f"animations/{mob_name.lower()}_citadel.animation.json")

        # Step 3: Generate entity code
        run_tool("EntityCodeGenerator",
                 config=f"configs/{mob_name.lower()}.json",
                 output=f"src/.../entity/.../{ mob_name}Entity.java")

        # Step 4: Generate model/renderer
        run_tool("ModelRendererGenerator",
                 name=mob_name)

        # Step 5: Copy assets
        run_tool("AssetCopyScript",
                 mob=mob_name,
                 source="alexsmobs-1.22.9.jar")

        # Step 6: Generate registration code
        run_tool("RegistrationCodeGenerator",
                 name=mob_name)

        print(f"✅ {mob_name} converted successfully!")
```

**Usage**:
```bash
# Convert first 10 mobs
python batch_convert.py --mobs fly,cockroach,triops,alligator,bear,bison,blobfish,bobcat,bone_serpent,bunfungus

# Convert all 90 mobs (overnight job)
python batch_convert.py --all --config batch_config.json
```

---

## Manual vs Automated Tasks

### Per-Mob Workflow

**Phase 1: Automated (5-10 minutes)**
1. ✅ Run `batch_decompile.py` for mob
2. ✅ Run `CitadelModelParser` → generates `.geo.json`
3. ✅ Run `CitadelAnimationConverter` → generates `.animation.json`
4. ✅ Run `EntityCodeGenerator` → generates entity class
5. ✅ Run `ModelRendererGenerator` → generates model/renderer
6. ✅ Run `AssetCopyScript` → copies textures/sounds
7. ✅ Copy registration snippets into mod initializer

**Phase 2: Manual Review (3-5 minutes)**
8. ⚠️ Review generated animation keyframes
9. ⚠️ Check for unique behaviors in original entity code
10. ⚠️ Adjust animation timing if needed
11. ⚠️ Add custom AI goals if present

**Phase 3: Testing (5-10 minutes)**
12. ❌ Build mod
13. ❌ Spawn mob in-game
14. ❌ Verify visual quality
15. ❌ Test animations (idle, walk/fly, death)
16. ❌ Test AI behavior
17. ❌ Tune speed/animation timing

**Total Time**: 10-15 minutes (down from 45-60 minutes)

---

## Time Savings Analysis

### Original Manual Approach (Approach C)

**Per Mob**:
- Decompilation: 5 min
- Model geometry conversion: 15 min
- Animation conversion: 15 min
- Entity code: 10 min
- Testing: 10 min
- **Total**: ~55 min

**For 90 Mobs**:
- 90 × 55 min = **4,950 minutes (82.5 hours)**

### With Automation Tools

**Per Mob**:
- Run automation scripts: 5 min
- Manual review: 3 min
- Testing: 7 min
- **Total**: ~15 min

**For 90 Mobs**:
- 90 × 15 min = **1,350 minutes (22.5 hours)**

**Time Saved**: 82.5 - 22.5 = **60 hours (72% reduction)**

### Batch Processing Optimization

**One-Time Setup** (do once for all 90 mobs):
- Batch decompile all mobs: 10 min
- Configure mob configs: 30 min
- **Total**: 40 min

**Per Mob** (automated pipeline):
- Run `batch_convert.py`: 5 min (fully automated)
- Manual review: 3 min
- Testing: 7 min
- **Total**: ~15 min

**For 90 Mobs**:
- Setup: 40 min
- Conversion: 90 × 15 min = 1,350 min
- **Total**: **1,390 minutes (23.2 hours)**

**Time Saved**: 82.5 - 23.2 = **59.3 hours (72% reduction)**

---

## Implementation Guide

### Step 1: Set Up Automation Environment

**Requirements**:
- Python 3.8+
- FernFlower (Java decompiler)
- Alex's Mobs JAR file

**Directory Structure**:
```
tools/
├── citadel_model_parser.py
├── citadel_animation_converter.py
├── entity_code_generator.py
├── model_renderer_generator.py
├── registration_code_generator.py
├── asset_copy_script.py
├── batch_decompile.py
├── batch_convert.py
├── templates/
│   ├── entity_template.java
│   ├── model_template.java
│   ├── renderer_template.java
│   └── mob_config_template.json
└── configs/
    ├── fly.json
    ├── cockroach.json
    └── ... (90 total)
```

### Step 2: Create Tool Scripts

**Order of Development**:
1. Start with `CitadelModelParser` (most critical)
2. Then `CitadelAnimationConverter` (complex but reusable patterns)
3. Then `EntityCodeGenerator` (straightforward templates)
4. Then `ModelRendererGenerator` (simple templates)
5. Finally, batch processing scripts

**Testing Strategy**:
- Test each tool on Fly entity first (already validated)
- Compare output to existing working Fly files
- Once tool produces identical output, it's validated

### Step 3: Build Mob Configs

**Create config for each mob**:
```bash
# Generate blank configs for all 90 mobs
python generate_blank_configs.py --output configs/

# Fill in attributes by referencing decompiled entity code
# (Can be semi-automated by parsing entity attributes)
```

**Config Priority**:
1. Start with Epic 03 mobs: Fly (done), Cockroach, Triops
2. Then simple mobs (passive animals)
3. Then complex mobs (hostile, special abilities)

### Step 4: Validate Pipeline

**Test Conversion Pipeline**:
```bash
# Convert Cockroach (land animal - different from Fly)
python batch_convert.py --mobs cockroach

# Verify output:
# - Build mod
# - Test in-game
# - Compare to original Alex's Mobs cockroach

# If successful, pipeline is validated for land animals
```

**Expand to Other Mob Types**:
- Flying: Fly (validated)
- Land: Cockroach (next)
- Aquatic: Triops (next)
- Hostile: Test with simple hostile mob
- Special: Save complex mobs for last

### Step 5: Batch Process Remaining Mobs

**Overnight Batch Job**:
```bash
# Convert all 90 mobs in one run
nohup python batch_convert.py --all --config batch_config.json > conversion.log 2>&1 &

# Review log next morning
# Fix any errors manually
```

**Expected Issues**:
- 10-20% of mobs may have unique behaviors requiring manual fixes
- Animation timing may need tuning for some mobs
- Complex mobs (like Mimicube, Warped Toad) may need custom logic

---

## Quick Reference

### Tool Cheat Sheet

| Tool | Purpose | Input | Output | Time |
|------|---------|-------|--------|------|
| **CitadelModelParser** | Parse model geometry | Model*.java | .geo.json | 10s |
| **CitadelAnimationConverter** | Convert animations | Model*.java | .animation.json | 20s |
| **EntityCodeGenerator** | Generate entity class | mob_config.json | *Entity.java | 5s |
| **ModelRendererGenerator** | Generate model/renderer | mob name | *Model.java, *Renderer.java | 5s |
| **RegistrationCodeGenerator** | Generate registration | mob name | code snippets | 5s |
| **AssetCopyScript** | Copy assets | mob name, JAR | textures, sounds | 10s |

### Automation Workflow

```
1. Decompile (batch)        →  10 min for all 90 mobs
2. Generate configs         →  30 min (one-time setup)
3. Run batch_convert.py     →  Auto-generates all code/assets
4. Manual review per mob    →  3 min each
5. Test in-game per mob     →  7 min each
6. Fix issues as needed     →  Variable (20% of mobs)

Total: ~23 hours for 90 mobs (vs 82 hours manual)
```

### Error Handling

**Common Issues**:

| Issue | Cause | Fix |
|-------|-------|-----|
| **Missing bones** | Parser failed to find bone definition | Manually inspect Java code, adjust regex |
| **Wrong parent hierarchy** | Parent detection failed | Manually set parent in .geo.json |
| **Animations not working** | Animation name mismatch | Check entity controller vs JSON names |
| **Texture missing** | Asset copy failed | Manually copy texture from JAR |
| **Mob doesn't spawn** | Registration code incomplete | Check entity registration |

---

## Next Steps

1. **Validate Fly Tools**: Run tools on Fly entity, compare output to existing validated files
2. **Build Core Tools**: Implement CitadelModelParser and CitadelAnimationConverter
3. **Test on Cockroach**: Convert second mob to validate pipeline
4. **Expand to Triops**: Test aquatic mob conversion
5. **Batch Convert Remaining**: Once pipeline validated, convert all 90 mobs

**Expected Timeline**:
- Tool development: 2-3 days
- Pipeline validation: 1 day
- Batch conversion: 1-2 days
- Bug fixes: 1-2 days
- **Total**: 5-8 days to complete all 90 mobs

---

**Related Skills**:
- `citadel-to-geckolib-conversion` - Manual conversion workflow
- `geckolib-animation-patterns` - Animation design patterns

**Success Metrics**:
- ✅ 70-80% of mobs convert without manual intervention
- ✅ Conversion time reduced from 55 min to 15 min per mob
- ✅ Visual quality matches original Alex's Mobs
- ✅ All animations work correctly after generation
