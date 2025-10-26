# TASK-001 Manual Completion Checklist

**Task**: Set Up Development Environment & Blockbench
**Estimated Time**: 2-3 hours
**Status**: IN PROGRESS

---

## Instructions

This task requires manual work in the Blockbench GUI. Follow the steps below and check off each item as you complete it.

**Reference**: See `BLOCKBENCH_SETUP.md` for detailed instructions.

---

## Checklist

### Part 1: Installation

- [ ] **Download Blockbench**
  - Visit: https://www.blockbench.net/
  - Download for your OS
  - Install application

- [ ] **Install GeckoLib Plugin**
  - Open Blockbench
  - File → Plugins → Available
  - Search for "GeckoLib Animation Utils"
  - Click Install
  - Restart Blockbench

- [ ] **Verify Plugin Installed**
  - File → Plugins → Installed
  - "GeckoLib Animation Utils" appears in list
  - File → New shows "GeckoLib Model" option

---

### Part 2: Create insect_template.bbmodel

- [ ] **Create New GeckoLib Model**
  - File → New → GeckoLib Model
  - Model Name: `insect_template`
  - Texture Size: 32x32

- [ ] **Create Bone Structure** (12 cubes total):
  - [ ] root (group)
  - [ ] body (cube: 4x3x3)
  - [ ] head (cube: 3x3x3)
  - [ ] antenna_left (cube: 1x3x1)
  - [ ] antenna_right (cube: 1x3x1)
  - [ ] wing_left (cube: 6x4x1, transparent)
  - [ ] wing_right (cube: 6x4x1, transparent)
  - [ ] leg_front_left (cube: 1x3x1)
  - [ ] leg_front_right (cube: 1x3x1)
  - [ ] leg_middle_left (cube: 1x3x1)
  - [ ] leg_middle_right (cube: 1x3x1)
  - [ ] leg_back_left (cube: 1x3x1)
  - [ ] leg_back_right (cube: 1x3x1)

- [ ] **Set Pivot Points**
  - [ ] body pivot: center (0, 0, 0)
  - [ ] head pivot: base where connects to body
  - [ ] wing pivots: where attach to body sides
  - [ ] leg pivots: where each leg connects to body

- [ ] **Save Template**
  - File → Save Project
  - Location: `docs/blockbench-templates/insect_template.bbmodel`
  - Verify file saved successfully

---

### Part 3: Create fish_template.bbmodel

- [ ] **Create New GeckoLib Model**
  - File → New → GeckoLib Model
  - Model Name: `fish_template`
  - Texture Size: 32x32

- [ ] **Create Bone Structure** (10-16 cubes total):
  - [ ] root (group)
  - [ ] body (group)
  - [ ] head (cube: 3x2x3)
  - [ ] body_segment_1 (cube: 3x2x4)
  - [ ] body_segment_2 (cube: 2x2x3)
  - [ ] tail (cube: 2x1x4, fan-shaped)
  - [ ] fin_left (cube: 1x3x2, pectoral)
  - [ ] fin_right (cube: 1x3x2, pectoral)
  - [ ] fin_dorsal (cube: 1x2x3, top fin)
  - [ ] OPTIONAL: leg_1 through leg_6 (for crustaceans like Triops)

- [ ] **Set Pivot Points**
  - [ ] body segment pivots: where segments connect
  - [ ] tail pivot: base where connects to body
  - [ ] fin pivots: where attach to body
  - [ ] leg pivots (if added): attachment on belly

- [ ] **Save Template**
  - File → Save Project
  - Location: `docs/blockbench-templates/fish_template.bbmodel`
  - Verify file saved successfully

---

### Part 4: Test GeckoLib Export

- [ ] **Create Test Model**
  - Open `insect_template.bbmodel` (or any template)
  - Add simple idle animation:
    - Create animation: `idle`, 2 seconds, loop ON
    - Keyframe 0s: body Y=0
    - Keyframe 1s: body Y=0.5 (gentle bob)
    - Keyframe 2s: body Y=0

- [ ] **Export Model**
  - File → Export → Export GeckoLib Model
  - Save as: `test_export.geo.json` (anywhere temporary)

- [ ] **Export Animations**
  - Animation → Export Animations
  - Save as: `test_export.animation.json` (same location)

- [ ] **Verify Exports**
  - [ ] Open `test_export.geo.json` in text editor
  - [ ] Verify valid JSON (no syntax errors)
  - [ ] Contains: `"format_version"`, `"minecraft:geometry"`, `"bones"`
  - [ ] Open `test_export.animation.json` in text editor
  - [ ] Verify valid JSON
  - [ ] Contains: `"format_version"`, `"animations"`, keyframes

- [ ] **Clean Up Test Files**
  - Delete `test_export.geo.json` and `test_export.animation.json`
  - These were just for testing, not needed

---

### Part 5: Verification

- [ ] **All Files Exist**:
  - [ ] `docs/blockbench-templates/insect_template.bbmodel` exists
  - [ ] `docs/blockbench-templates/fish_template.bbmodel` exists
  - [ ] `docs/epic-03/BLOCKBENCH_SETUP.md` exists (auto-created)
  - [ ] `docs/blockbench-templates/README.md` exists (auto-created)

- [ ] **GeckoLib Export Tested**:
  - [ ] Can export .geo.json files
  - [ ] Can export .animation.json files
  - [ ] Files are valid JSON

- [ ] **Ready for Next Task**:
  - [ ] Blockbench is installed and configured
  - [ ] Templates are created and saved
  - [ ] Export workflow is understood
  - [ ] Can proceed to TASK-004 (Create Fly model)

---

## Time Tracking

**Start Time**: ___________
**End Time**: ___________
**Total Time**: ___________ hours

**Breakdown**:
- Installation: _____ minutes
- insect_template creation: _____ minutes
- fish_template creation: _____ minutes
- Export testing: _____ minutes
- Total: _____ hours

**Notes/Issues**:
(Record any issues encountered or deviations from the guide)

---

## Completion

When ALL checkboxes above are checked:

1. ✅ Mark this checklist as complete
2. ✅ Update TASK-001 status in plan.md to COMPLETED
3. ✅ Record time spent in EFFORT_TRACKING.md (to be created later)
4. ✅ Proceed to TASK-002 (Create Base Entity Classes)

---

**TASK-001 Status**: IN PROGRESS → Awaiting your manual completion

Once you've completed all checkboxes, run `/next` again to proceed to TASK-002.
