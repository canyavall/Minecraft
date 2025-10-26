# Blockbench Setup Guide - Epic 03

**Last Updated**: 2025-10-26
**For**: xeenaa-alexs-mobs (Fabric 1.21.1)
**Epic**: 03-proof-of-concept (First Three Animals)

---

## Overview

This guide walks through setting up Blockbench with GeckoLib for creating 3D mob models and animations. Blockbench is a free, open-source 3D modeling tool designed for Minecraft, and GeckoLib is an animation library that makes it easy to port complex animated entities to Fabric.

**What You'll Learn**:
- How to install Blockbench and the GeckoLib plugin
- How to create reusable templates for common mob types
- How to export models and animations for GeckoLib
- Troubleshooting common issues

**Time Required**: 30-60 minutes for setup + template creation

---

## Part 1: Installation

### Step 1: Download Blockbench

1. Visit **https://www.blockbench.net/**
2. Click the **Download** button
3. Choose your operating system:
   - **Windows**: Download `.exe` installer
   - **Mac**: Download `.dmg` installer
   - **Linux**: Download `.AppImage` or use package manager
4. Run the installer and follow prompts
5. Launch Blockbench

**Verification**: Blockbench should open to a welcome screen with "New Project" options.

---

### Step 2: Install GeckoLib Plugin

The GeckoLib Animation Utils plugin adds export functionality specifically for GeckoLib models.

**Installation Steps**:
1. In Blockbench, go to **File** → **Plugins** → **Available**
2. In the search box, type: `GeckoLib`
3. Find "**GeckoLib Animation Utils**" in the list
4. Click the **Install** button
5. If prompted, click **Restart Blockbench** (or restart manually)

**Verification**:
- After restart, go to **File** → **Plugins** → **Installed**
- "GeckoLib Animation Utils" should appear in the list
- When creating a new project, you should see "GeckoLib Model" as an option

---

### Step 3: Familiarize Yourself with Blockbench

**Key Interface Elements**:
- **Outliner (left)**: Shows bone/cube hierarchy
- **Viewport (center)**: 3D view of your model
- **Properties (right)**: Cube/bone properties and transformations
- **Timeline (bottom)**: Animation keyframes

**Basic Controls**:
- **Rotate view**: Right-click + drag
- **Pan view**: Middle-click + drag (or Shift + right-click + drag)
- **Zoom**: Scroll wheel
- **Select cube**: Left-click
- **Add cube**: Click **Add Cube** button or press Ctrl+A

**Recommended**: Watch the official Blockbench tutorial (5 min): https://www.blockbench.net/quickstart

---

## Part 2: Creating Templates

Templates speed up mob creation by providing pre-built bone structures for common mob types.

### Template 1: Insect Template (insect_template.bbmodel)

**Purpose**: Base for 6-legged insects (Fly, Cockroach, beetles, ants)

**Creation Steps**:

1. **Create New GeckoLib Model**:
   - File → New → GeckoLib Model
   - Model Name: `insect_template`
   - Texture Size: 32x32 (can adjust per mob)

2. **Create Bone Structure**:
   ```
   root (group)
   └── body (cube: 4x3x3 pixels)
       ├── head (cube: 3x3x3 pixels)
       │   ├── antenna_left (cube: 1x3x1 pixels)
       │   └── antenna_right (cube: 1x3x1 pixels)
       ├── wings (group)
       │   ├── wing_left (cube: 6x4x1 pixels, transparent)
       │   └── wing_right (cube: 6x4x1 pixels, transparent)
       └── legs (group)
           ├── leg_front_left (cube: 1x3x1 pixels)
           ├── leg_front_right (cube: 1x3x1 pixels)
           ├── leg_middle_left (cube: 1x3x1 pixels)
           ├── leg_middle_right (cube: 1x3x1 pixels)
           ├── leg_back_left (cube: 1x3x1 pixels)
           └── leg_back_right (cube: 1x3x1 pixels)
   ```

3. **Set Pivot Points** (CRITICAL for animations):
   - **body**: Center of thorax (0, 0, 0)
   - **head**: Base of head where it connects to body
   - **wings**: Where wings attach to body (left/right sides)
   - **legs**: Where each leg connects to body (hip joint)

4. **Tips**:
   - Keep cubes simple (low poly count)
   - Use symmetry: Create right side, then mirror for left
   - Legs should be angled naturally (not straight down)
   - Wings can be slightly raised (ready-to-fly pose)

5. **Save**:
   - File → Save Project
   - Location: `docs/blockbench-templates/insect_template.bbmodel`

**Total Cubes**: ~12 cubes (1 body + 1 head + 2 antennae + 2 wings + 6 legs)

---

### Template 2: Fish Template (fish_template.bbmodel)

**Purpose**: Base for aquatic creatures (Triops, small fish, tadpoles, crustaceans)

**Creation Steps**:

1. **Create New GeckoLib Model**:
   - File → New → GeckoLib Model
   - Model Name: `fish_template`
   - Texture Size: 32x32

2. **Create Bone Structure**:
   ```
   root (group)
   └── body (group)
       ├── head (cube: 3x2x3 pixels)
       ├── body_segment_1 (cube: 3x2x4 pixels)
       │   └── body_segment_2 (cube: 2x2x3 pixels)
       │       └── tail (cube: 2x1x4 pixels, fan-shaped)
       ├── fins (group)
       │   ├── fin_left (cube: 1x3x2 pixels, pectoral)
       │   ├── fin_right (cube: 1x3x2 pixels, pectoral)
       │   └── fin_dorsal (cube: 1x2x3 pixels, top fin)
       └── legs_swimmerets (group, optional for crustaceans)
           ├── leg_1 through leg_6 (cubes: 1x2x1 pixels each)
   ```

3. **Set Pivot Points**:
   - **body segments**: Where each segment connects (for undulation)
   - **tail**: Base where tail connects to body
   - **fins**: Where fin attaches to body
   - **legs**: Attachment points on belly

4. **Tips**:
   - Body segments allow for swimming undulation (S-curve motion)
   - Tail should be fan-shaped or forked for swimming
   - Fins can be slightly angled (swimming pose)
   - For Triops: Add small legs underneath, tail is fan-shaped

5. **Save**:
   - Location: `docs/blockbench-templates/fish_template.bbmodel`

**Total Cubes**: ~10-16 cubes (depending on whether legs are added)

---

## Part 3: Testing GeckoLib Export

Before creating actual mob models, verify the export process works.

### Create Test Model

1. **Load a Template** (or create new):
   - File → Open → Select `insect_template.bbmodel`

2. **Add Simple Animation**:
   - Click **Animate** tab (bottom)
   - Create new animation: `idle`
   - Duration: 2 seconds, loop: ON
   - Add keyframe at 0s: body at Y=0
   - Add keyframe at 1s: body at Y=0.5 (gentle bob up)
   - Add keyframe at 2s: body at Y=0 (back to start)

3. **Export Model**:
   - File → Export → **Export GeckoLib Model**
   - Save as: `test_export.geo.json`
   - Location: Anywhere temporary (e.g., Desktop)

4. **Export Animations**:
   - Animation → **Export Animations**
   - Save as: `test_export.animation.json`
   - Location: Same as model file

5. **Verify Exports**:
   - Open both `.json` files in a text editor
   - Check they contain valid JSON (no syntax errors)
   - `.geo.json` should have `"format_version"`, `"minecraft:geometry"`, `"bones"`
   - `.animation.json` should have `"format_version"`, `"animations"`, keyframes

**Expected File Sizes**:
- `.geo.json`: 1-5 KB (depending on complexity)
- `.animation.json`: 0.5-2 KB (depending on animation count)

**If Export Fails**:
- Verify GeckoLib plugin is installed (File → Plugins → Installed)
- Restart Blockbench
- Check Blockbench console for errors (View → Console)

---

## Part 4: Workflow for Creating Mobs

Once setup is complete, here's the workflow for creating each mob:

### Step-by-Step Process

1. **Research**:
   - Watch videos of the mob in Alex's Mobs original mod
   - Study real animal anatomy (Google Images)
   - Note key features (size, proportions, movement style)

2. **Model Creation**:
   - Load appropriate template (insect or fish)
   - Modify cubes to match mob anatomy
   - Adjust pivot points as needed
   - Keep geometry simple (~10-15 cubes for simple mobs)

3. **Texturing**:
   - Use Paint tab to UV map
   - Export texture template (File → Export → Export UV Mapping)
   - Paint in external editor (Aseprite, GIMP, Paint.NET)
   - Import texture back (Paint → Import Texture)

4. **Animation**:
   - Create 3 animations for each mob:
     - **idle**: 2 second loop, subtle movement
     - **walk/fly/swim**: 1-2 second loop, movement cycle
     - **death**: 1-2 second play-once, fall/flip

5. **Export for GeckoLib**:
   - Export model: `<mob_name>.geo.json` → `assets/xeenaa-alexs-mobs/geo/`
   - Export animations: `<mob_name>.animation.json` → `assets/xeenaa-alexs-mobs/animations/`
   - Export texture: `<mob_name>.png` → `assets/xeenaa-alexs-mobs/textures/entity/`

6. **Integration** (in code):
   - Create entity class (e.g., `FlyEntity.java`)
   - Create model class (e.g., `FlyModel.java`)
   - Create renderer class (e.g., `FlyRenderer.java`)
   - Register entity in `ModEntities.java`

---

## Part 5: Best Practices

### Modeling Tips

1. **Keep It Simple**: Proof of concept mobs should be 10-15 cubes max
2. **Use Proper Hierarchy**: Parent-child relationships matter for animations
3. **Set Pivots First**: ALWAYS set pivot points before animating (can't change easily after)
4. **Save Often**: Blockbench can crash, especially with complex models
5. **Use Symmetry**: Create one side, mirror for other (Tools → Mirror → Mirror Model)

### Animation Tips

1. **Start with Idle**: Get idle animation working first (simplest)
2. **Loop Seamlessly**: First and last keyframes should match (smooth loop)
3. **Ease In/Out**: Use Bezier curves for smooth motion (right-click keyframe → Interpolation)
4. **Test in Blockbench**: Play animation in Blockbench before exporting (press Play button)
5. **Keep It Simple**: 2-4 keyframes per animation is often enough for simple mobs

### Export Checklist

Before exporting for use in mod:

- [ ] All cubes have proper UV mapping
- [ ] All pivot points are set correctly
- [ ] All animations loop smoothly (or play-once for death)
- [ ] Model is saved (File → Save Project)
- [ ] Exported files are named correctly (`<mob_name>.geo.json`, etc.)
- [ ] Files are placed in correct asset directories

---

## Part 6: Common Issues & Solutions

### Issue 1: GeckoLib Plugin Not Found

**Problem**: "GeckoLib Animation Utils" doesn't appear in Available Plugins list

**Solutions**:
- Check internet connection (Blockbench downloads plugin list online)
- Restart Blockbench
- Manually download plugin: https://github.com/JannisX11/blockbench-plugins (search for GeckoLib)
- Install manually: File → Plugins → Load Plugin from File

---

### Issue 2: Export Produces Invalid JSON

**Problem**: Exported `.geo.json` or `.animation.json` has syntax errors

**Solutions**:
- Verify GeckoLib plugin is installed (not vanilla export)
- Check for special characters in bone names (use only letters, numbers, underscores)
- Ensure all cubes are parented to a bone/group
- Update GeckoLib plugin to latest version (File → Plugins → Installed → Update)

---

### Issue 3: Animations Don't Play in Game

**Problem**: Model renders but animations don't play or are frozen

**Solutions**:
- Verify animation names match code (case-sensitive: `idle`, `walk`, `death`)
- Check animation is set to **loop** (for idle/walk) or **play-once** (for death)
- Ensure animation controller in entity code references correct animation names
- Verify `.animation.json` file is in correct location (`assets/.../animations/`)

---

### Issue 4: Model Renders at Wrong Scale

**Problem**: Mob is too big or too small in-game

**Solutions**:
- Check model scale in entity registration (`EntityType.Builder.dimensions(width, height)`)
- Verify Blockbench model isn't too large (should fit in ~16x16x16 grid for small mobs)
- Adjust renderer scale if needed (in `<MobName>Renderer.java`, override `getScaleModifier()`)

---

### Issue 5: Texture Not Rendering

**Problem**: Model shows up as white/magenta (missing texture)

**Solutions**:
- Verify texture file exists: `assets/xeenaa-alexs-mobs/textures/entity/<mob_name>.png`
- Check texture path in model class (`getTextureResource()` method)
- Ensure texture resolution matches model (16x16, 32x32, 64x64)
- Reload resources in-game (F3+T)

---

## Part 7: Resources & References

### Official Documentation

- **Blockbench Official Site**: https://www.blockbench.net/
- **Blockbench Wiki**: https://www.blockbench.net/wiki/
- **GeckoLib Documentation**: https://github.com/bernie-g/geckolib (check wiki)
- **GeckoLib Blockbench Plugin**: Search in Blockbench plugins

### Tutorials

- **Blockbench Quickstart**: https://www.blockbench.net/quickstart (5 min video)
- **GeckoLib Tutorial**: Search YouTube for "GeckoLib Minecraft tutorial"
- **Animation Basics**: Blockbench → Help → Animation Guide

### Community

- **Blockbench Discord**: Join for help and tips
- **GeckoLib Discord**: Community support for animation questions

---

## Part 8: Templates Created

### insect_template.bbmodel

**Location**: `docs/blockbench-templates/insect_template.bbmodel`

**Use For**:
- Fly (Epic 03)
- Cockroach (Epic 03)
- Beetles, ants, small bugs (future epics)

**Structure**: 12 cubes (body, head, 2 antennae, 2 wings, 6 legs)

**Key Features**:
- 6-legged structure (front, middle, back pairs)
- Wing bones for flying insects
- Antennae for insects
- Ready for walk/fly animations

---

### fish_template.bbmodel

**Location**: `docs/blockbench-templates/fish_template.bbmodel`

**Use For**:
- Triops (Epic 03)
- Small fish, tadpoles, crustaceans (future epics)

**Structure**: 10-16 cubes (head, body segments, tail, fins, optional legs)

**Key Features**:
- Segmented body for undulation (swimming S-curve motion)
- Fins for aquatic movement
- Optional legs for crustaceans (Triops, lobster)
- Ready for swim animations

---

## Part 9: Next Steps After Setup

Once Blockbench is set up and templates are created:

1. ✅ **Blockbench installed** with GeckoLib plugin
2. ✅ **Templates created** (insect_template.bbmodel, fish_template.bbmodel)
3. ✅ **Export tested** (verified .geo.json and .animation.json work)

**You're ready for**:
- **TASK-004**: Create Fly model and animations
- **TASK-008**: Create Cockroach model and animations
- **TASK-012**: Create Triops model and animations

**Time Estimate for Each Mob**:
- Model creation: 15-20 hours (first time, gets faster with practice)
- Animations: Included in model creation time
- Total per mob: 15-20 hours (decreases to 10-15h after experience)

---

## Completion Checklist

Mark off as you complete each step:

- [ ] Blockbench downloaded and installed
- [ ] GeckoLib Animation Utils plugin installed
- [ ] Familiarized with Blockbench interface (5 min quickstart)
- [ ] insect_template.bbmodel created and saved
- [ ] fish_template.bbmodel created and saved
- [ ] Test export performed (verified .geo.json and .animation.json are valid)
- [ ] All template files in `docs/blockbench-templates/` directory

**When all checked**: TASK-001 is COMPLETE! ✅

---

**Setup Complete!** You're ready to create mob models for Epic 03: Proof of Concept.

Next: Begin TASK-004 (Create Fly model) using `insect_template.bbmodel` as a starting point.
