# Epic 007: Crow - Technical Implementation Plan

**Epic**: Crow - Flying Passive Bird
**Status**: IN PROGRESS (Bug fixes → Feature completion)
**Complexity**: Medium
**Total Tasks**: 16 (12 main tasks + 4 sub-tasks)

---

## Plan Overview

This epic has **partial implementation** with known bugs and missing features. The plan is organized into 3 phases:

**Phase 1**: Fix blocking bugs (texture, spawn egg)
**Phase 2**: Add taming and command modes
**Phase 3**: Implement special mechanics (shoulder perch, item gathering, containers)

**Current State**:
- ✅ Entity class (CrowEntity) exists
- ✅ Model (crow.geo.json) with 9 bones
- ✅ Animations (6 total: idle, walk, run, fly, sit, attack)
- ✅ Renderer (CrowRenderer)
- ✅ Registry entries (ModEntities, AlexsMobsClient)
- ❌ Texture not rendering (black crow)
- ❌ Spawn egg missing
- ❌ Taming system not implemented
- ❌ Command modes not implemented
- ❌ Special mechanics not implemented

---

## Phase 1: Critical Bug Fixes

### TASK-001: Fix Crow Texture Rendering

**Status**: COMPLETED
**Priority**: CRITICAL
**Assigned**: implementation-agent
**Estimated**: 1-2 hours
**Complexity**: Medium
**Completed**: 2025-10-28 - Fixed getTextureLocation() to return Identifier directly

**Description**:
Crow currently renders completely black in-game despite texture file existing. Need to diagnose and fix texture loading/rendering issue.

**Problem**:
- Crow appears completely black (no texture, not even eyes)
- Texture file exists at `src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png`
- Model renders correctly (shape visible)
- Other animals (Fly, Cockroach, Triops) use same pattern and work

**Investigation Required**:
1. Verify texture file format and dimensions (should be 32×32 or 64×64 PNG)
2. Check texture file isn't corrupted (re-extract from Alex's Mobs JAR if needed)
3. Verify CrowRenderer getTextureLocation() method returns correct path
4. Check crow.geo.json UV coordinates match texture layout
5. Test if GeckoLib is loading the texture resource

**Files to Check**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/CrowRenderer.java`
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/client/model/CrowModel.java`
- `xeenaa-alexs-mobs/src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png`
- `xeenaa-alexs-mobs/src/main/resources/assets/xeenaa-alexs-mobs/geo/crow.geo.json`

**Reference**:
- Working examples: FlyRenderer, CockroachRenderer, TriopsRenderer
- Original texture location: `xeenaa-alexs-mobs/.claude/epics/002-mob-catalog-asset-inventory/alexsmobs-extracted/assets/alexsmobs/textures/entity/crow.png`

**Acceptance Criteria**:
- [ ] Crow texture renders correctly in-game
- [ ] All body parts show proper colors (black feathers, eyes visible)
- [ ] Texture doesn't appear stretched or distorted
- [ ] Build completes without errors
- [ ] User confirms visual appearance matches expectations

**Deliverables**:
- Fixed texture file (if corrupted)
- Updated renderer (if path issue)
- Updated model/geo file (if UV mapping issue)

---

### TASK-001.1: Fix - Crow Flies Backwards (180° Orientation Wrong)

**Status**: COMPLETED
**Priority**: HIGH
**Assigned**: implementation-agent
**Estimated**: 30 minutes - 1 hour
**Complexity**: Simple
**Completed**: 2025-10-28 - Added 180° Y-axis rotation in CrowRenderer
**Related Task**: TASK-001 (Texture Rendering Fix)

**Description**:
Bug reported during testing: Crow flies tail-first instead of head-first. The entity orientation is 180 degrees backwards - the tail is where the head should be and vice versa.

**Bug Details**:
- **Symptoms**: Crow flies with its tail/butt facing forward instead of its head/beak
- **Expected behavior**: Crow should fly head-first (beak pointing in direction of travel)
- **Actual behavior**: Crow flies backwards (tail pointing in direction of travel)
- **Root cause**: Likely renderer transformation issue (180° rotation needed) or model pivot orientation

**Investigation Required**:
1. Check CrowRenderer.render() method for rotation transformations
2. Compare with working examples (FlyRenderer applies 180° flip with `rotateZ`)
3. Verify model pivot orientation in crow.geo.json
4. Test if adding 180° Y-axis rotation fixes orientation

**Files to Check**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/client/renderer/CrowRenderer.java` (render method lines 164-179)
- Compare with `FlyRenderer.java` (line 169: rotateZ 180°)

**Likely Solution**:
Add 180° rotation around Y-axis in CrowRenderer.render() method:
```java
// After scale and translate transformations
poseStack.multiply(new Quaternionf().rotateY((float) Math.PI));  // Rotate 180° around Y
```

**Goal**:
Fix crow orientation so it flies head-first in the direction of movement.

**Requirements**:
- [ ] Investigate which axis needs rotation (Y-axis most likely for forward/backward)
- [ ] Add rotation transformation to CrowRenderer.render()
- [ ] Test that crow flies head-first
- [ ] Ensure crow still renders at correct height and scale
- [ ] Verify no other orientations broken (walking, sitting, idle)

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `FlyRenderer.java` - Working example (uses rotateZ for upside-down flip)
- CrowRenderer already has scale (0.8f) and translate (0, -1.5, 0)

**Acceptance Criteria**:
- [ ] Crow flies with head/beak pointing forward
- [ ] Crow's tail faces backwards when flying
- [ ] Movement direction matches visual orientation
- [ ] No new visual glitches introduced
- [ ] User validates the fix in-game

**Dependencies**:
- TASK-001 must be completed (texture rendering - already done)

**Blockers**:
- None

**Notes**:
- This is a rendering orientation fix for TASK-001
- FlyRenderer uses rotateZ (upside-down), but crow needs rotateY (backwards)
- Quick fix - should take 30 minutes to 1 hour

---

### TASK-001.2: Fix - Crow Head/Body Pitch Wrong (Horizontal Instead of Vertical)

**Status**: COMPLETED
**Priority**: HIGH
**Assigned**: implementation-agent
**Estimated**: 30 minutes - 1 hour
**Complexity**: Simple
**Completed**: 2025-10-28 - Added 90° X-axis rotation in CrowRenderer
**Related Task**: TASK-001.1 (Backwards Orientation Fix)

**Description**:
Bug reported during testing: Crow's body is horizontal (laying flat) when it should be vertical (head up, legs down). The crow flies correctly forward now, but its body orientation is rotated 90 degrees wrong on the pitch axis.

**Bug Details**:
- **Symptoms**: Crow's head points horizontally (parallel to ground), legs point horizontally
- **Expected behavior**: Crow should fly upright (head up, body vertical, legs down)
- **Actual behavior**: Crow flies laying flat/horizontal like it's on its side
- **Root cause**: Missing pitch rotation (likely needs 90° X-axis rotation)

**Investigation Required**:
1. Check if model needs X-axis rotation (pitch) to stand upright
2. Compare with FlyRenderer which may have similar pitch adjustments
3. Verify if this is model-specific or renderer transformation issue
4. Test different pitch angles (90°, -90°, or different axis)

**Files to Check**:
- `CrowRenderer.java` (render method - add pitch rotation)
- `FlyRenderer.java` (check if it has pitch/roll rotations)
- `crow.geo.json` (verify model pivot orientation)

**Likely Solution**:
Add X-axis (pitch) rotation in CrowRenderer.render() method:
```java
// After Y-rotation for forward/backward orientation
poseStack.multiply(new Quaternionf().rotateX((float) Math.PI / 2));  // Rotate 90° on X for upright
// OR
poseStack.multiply(new Quaternionf().rotateX(-(float) Math.PI / 2));  // Try -90° if wrong direction
```

**Goal**:
Fix crow's body pitch so it flies upright (head up, legs down) like a normal bird.

**Requirements**:
- [ ] Investigate which axis needs rotation (X for pitch, Z for roll)
- [ ] Add pitch rotation to CrowRenderer.render()
- [ ] Test that crow stands/flies upright
- [ ] Ensure crow still flies forward (don't break TASK-001.1 fix)
- [ ] Verify head points up and legs point down

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `FlyRenderer.java` - Check for pitch/roll rotations
- Current rotations: scale (0.8f), translate (0, -1.5, 0), rotateY (180°)

**Acceptance Criteria**:
- [ ] Crow's head points upward when flying
- [ ] Crow's legs point downward when flying
- [ ] Crow looks like a normal bird (not laying flat)
- [ ] Forward orientation still works (from TASK-001.1)
- [ ] User validates the fix in-game

**Dependencies**:
- TASK-001.1 must be completed (forward orientation - already done)

**Blockers**:
- None

**Notes**:
- This is additional rotation fix after TASK-001.1
- May need to experiment with X-axis vs Z-axis rotation
- Likely 90° or -90° rotation needed

---

### TASK-001.3: Fix - Crow Spawns Under Terrain and Flies Underground

**Status**: COMPLETED
**Priority**: HIGH
**Assigned**: implementation-agent
**Estimated**: 1-2 hours
**Complexity**: Medium
**Completed**: 2025-10-28 - Fixed Y-translation from -1.5 to +0.3 (moved UP instead of DOWN)
**Related Task**: TASK-001 (Texture Rendering), TASK-001.2 (Pitch Fix)

**Description**:
Bug reported during testing: Crow sometimes spawns under terrain blocks and flies underground instead of above ground. This likely relates to the Y-translation adjustment in the renderer (-1.5 blocks) which may be affecting spawn height or hitbox positioning.

**Bug Details**:
- **Symptoms**: Crow spawns below ground level, crow flies through terrain
- **Expected behavior**: Crow spawns at ground level, crow flies above ground avoiding terrain
- **Actual behavior**: Crow appears underground, pathfinding allows underground flight
- **Root cause**: Likely Y-translation in renderer or entity spawn height issue

**Investigation Required**:
1. Check CrowEntity spawn height settings (createMobAttributes)
2. Verify renderer Y-translation doesn't affect hitbox/collision
3. Check if flying AI has proper terrain avoidance
4. Test spawn egg spawn height (should spawn on top of block, not inside)
5. Compare with working flying entities (FlyEntity)

**Files to Check**:
- `CrowRenderer.java` (line 176: translate(0, -1.5, 0) - visual only?)
- `CrowEntity.java` (spawn attributes, flying AI goals)
- Compare with `FlyEntity.java` (working flying entity)

**Likely Solutions**:
1. **Renderer issue**: Y-translation should only affect rendering, not hitbox
2. **Spawn height**: Ensure spawn egg spawns crow at correct Y position
3. **AI pathfinding**: Add terrain avoidance to flying AI
4. **Entity position**: Check if entity position needs adjustment separate from renderer

**Goal**:
Fix crow spawning and flying to ensure it stays above ground.

**Requirements**:
- [ ] Investigate root cause (renderer vs entity position vs AI)
- [ ] Fix spawn height if spawn egg places crow underground
- [ ] Ensure flying AI avoids terrain
- [ ] Verify renderer Y-translation doesn't affect hitbox
- [ ] Test in various terrain (flat, hills, caves)

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- `FlyEntity.java` - Working flying entity reference
- Renderer transformations should be visual only (not affect collision)

**Acceptance Criteria**:
- [ ] Crow spawns at correct ground level (on top of blocks)
- [ ] Crow flies above ground, not through terrain
- [ ] Flying pathfinding avoids underground areas
- [ ] Hitbox collision works correctly
- [ ] User validates crow no longer clips underground

**Dependencies**:
- TASK-001 and TASK-001.2 must be completed

**Blockers**:
- None

**Notes**:
- This is a critical bug affecting basic functionality
- May need to separate entity position from renderer visual offset
- Check if Y-translation in renderer affects anything besides visuals
- Flying AI may need terrain collision avoidance goals

---

### TASK-001.4: Fix - Crow Head Tilts Based on Movement (Dynamic Pitch Issue)

**Status**: COMPLETED
**Priority**: HIGH
**Assigned**: implementation-agent
**Estimated**: 1-2 hours
**Complexity**: Medium
**Completed**: 2025-10-28 - Added entity pitch counter to keep model upright
**Related Task**: TASK-001.2 (Pitch Fix)

**Description**:
Bug reported during testing: Crow's head orientation changes based on movement direction. When near ground, head points down. When flying up, head points diagonally ("1 o'clock" position). The static -90° X-rotation is being combined with the entity's dynamic pitch calculation.

**Bug Details**:
- **Symptoms**: Crow's head tilts down near ground, tilts diagonally when flying up
- **Expected behavior**: Crow should always fly upright with head pointing forward/up regardless of movement direction
- **Actual behavior**: Head orientation changes dynamically based on entity pitch
- **Root cause**: Entity's natural pitch (based on movement) combines with renderer's static -90° X-rotation

**Investigation Required**:
1. Check if entity has dynamic pitch calculation based on velocity
2. Compare with FlyEntity - does it have same issue?
3. Determine if we need to counter the entity's pitch in renderer
4. Check if we should disable entity pitch updates for flying birds

**Files to Check**:
- `CrowRenderer.java` (render method with rotations)
- `CrowEntity.java` (check for pitch calculations, setXRot/getXRot)
- `FlyEntity.java` (compare flying entity pitch behavior)

**Likely Solutions**:
1. **Counter entity pitch**: Apply opposite rotation to cancel entity's pitch
   ```java
   // Counter the entity's pitch so model stays upright
   float entityPitch = entity.getXRot(); // Entity's current pitch
   poseStack.multiply(new Quaternionf().rotateX(-entityPitch * ((float)Math.PI / 180F)));
   ```

2. **Disable entity pitch**: Override entity's pitch calculation to always be 0
   ```java
   // In CrowEntity.java
   @Override
   public void setXRot(float pitch) {
       super.setXRot(0); // Always keep level
   }
   ```

3. **Alternative rotation approach**: Use different rotation order or axis

**Goal**:
Fix crow's head orientation to stay upright regardless of movement direction.

**Requirements**:
- [ ] Investigate entity pitch calculation
- [ ] Test solution with different movement directions (up, down, level)
- [ ] Ensure crow always appears upright
- [ ] Verify fix doesn't affect other animations

**Guidelines and Resources**:
- `coding-standards skill` - Code standards
- Entity pitch: getXRot() returns degrees, needs conversion to radians
- FlyRenderer - check if it has similar pitch handling

**Acceptance Criteria**:
- [ ] Crow head stays forward/up when near ground
- [ ] Crow head stays forward/up when flying upward
- [ ] Crow head orientation is consistent regardless of movement
- [ ] No visual glitches during direction changes
- [ ] User validates crow orientation looks correct

**Dependencies**:
- TASK-001.2 must be completed (current pitch rotation)

**Blockers**:
- None

**Notes**:
- This is related to TASK-001.2 but requires dynamic pitch handling
- May need to counter entity's natural pitch in renderer
- Flying entities often have pitch based on velocity direction
- Static rotation isn't enough - need to handle dynamic pitch

---

### TASK-002: Create Crow Spawn Egg

**Status**: COMPLETED
**Priority**: CRITICAL
**Assigned**: implementation-agent
**Estimated**: 30 minutes
**Complexity**: Simple
**Completed**: 2025-10-28 - Added CROW_SPAWN_EGG to ModItems with creative tab and language entry

**Description**:
Add Crow spawn egg to creative inventory so users can summon crows without commands.

**Implementation Steps**:

1. **Add spawn egg to ModItems.java**:
   ```java
   public static final RegistryObject<SpawnEggItem> CROW_SPAWN_EGG = ITEMS.register("crow_spawn_egg",
       () -> new SpawnEggItem(ModEntities.CROW, 0x1F1F1F, 0x3D3D3D,
           new Item.Properties()));
   ```
   - Primary color: Dark gray/black (crow body)
   - Secondary color: Lighter gray (highlights)

2. **Register in creative tab**:
   - Add to ALEXS_MOBS_TAB item group
   - Follow pattern from FLY_SPAWN_EGG, COCKROACH_SPAWN_EGG, TRIOPS_SPAWN_EGG

3. **Create language entry** in `en_us.json`:
   ```json
   "item.xeenaa-alexs-mobs.crow_spawn_egg": "Crow Spawn Egg"
   ```

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/registry/ModItems.java`
- `xeenaa-alexs-mobs/src/main/resources/assets/xeenaa-alexs-mobs/lang/en_us.json`

**Reference**:
- Working examples: FLY_SPAWN_EGG, COCKROACH_SPAWN_EGG, TRIOPS_SPAWN_EGG in ModItems.java

**Acceptance Criteria**:
- [ ] Spawn egg appears in creative inventory (ALEXS_MOBS_TAB)
- [ ] Spawn egg has correct colors (dark gray + lighter gray)
- [ ] Right-clicking spawn egg spawns Crow entity
- [ ] Spawned crow is functional (renders, moves, AI works)
- [ ] Language entry displays correctly

**Deliverables**:
- Updated ModItems.java with CROW_SPAWN_EGG
- Updated en_us.json with spawn egg name

---

### TASK-003: Validate Basic Crow Functionality

**Status**: TODO
**Priority**: HIGH
**Assigned**: User (manual validation)
**Estimated**: 30 minutes
**Complexity**: Simple
**Dependencies**: TASK-001, TASK-002

**Description**:
User manual testing to validate crow renders correctly, spawns properly, and basic AI works before proceeding to advanced features.

**Test Scenarios**:

1. **Spawning**:
   - [ ] Use spawn egg in creative mode
   - [ ] Crow appears at cursor location
   - [ ] Crow doesn't immediately despawn

2. **Rendering**:
   - [ ] Crow texture visible (not black)
   - [ ] Eyes, beak, feathers show correct colors
   - [ ] Model proportions look correct
   - [ ] No Z-fighting or flickering

3. **Animations** (check all 6):
   - [ ] Idle: Standing still animation plays
   - [ ] Walk: Walking on ground animation plays
   - [ ] Run: Running animation plays when moving fast
   - [ ] Fly: Flying animation plays when airborne
   - [ ] Sit: Sitting animation (if triggered)
   - [ ] Attack: Attack animation (if crow attacks)

4. **Basic AI**:
   - [ ] Crow flies around randomly
   - [ ] Crow lands on blocks occasionally
   - [ ] Crow flees when player approaches
   - [ ] Crow picks up dropped food items (toss seeds/bread)
   - [ ] Crow doesn't get stuck in blocks
   - [ ] Crow pathfinding works (doesn't fly into walls constantly)

5. **Performance**:
   - [ ] No FPS drops when crow spawns
   - [ ] Multiple crows (5-10) don't cause lag
   - [ ] No console errors related to crow

**Validation Method**:
- User spawns crows in test world
- Observes behavior for 5-10 minutes
- Reports any visual glitches, AI bugs, or performance issues

**Acceptance Criteria**:
- [ ] All rendering tests pass
- [ ] All animation tests pass
- [ ] Basic AI behaviors work
- [ ] No blocking bugs identified
- [ ] User approves visual appearance

**Deliverables**:
- User validation report (bugs found or approval to proceed)

---

## Phase 2: Taming & Command System

### TASK-004: Implement Taming System (Pumpkin Seeds)

**Status**: TODO
**Priority**: HIGH
**Assigned**: implementation-agent
**Estimated**: 2-3 hours
**Complexity**: Medium
**Dependencies**: TASK-003 (user validation)

**Description**:
Implement taming system where player tosses Pumpkin Seeds to tame wild crow. Extends TamableAnimal base class.

**Implementation Requirements**:

1. **Change base class** (if not already TamableAnimal):
   - CrowEntity should extend `TamableAnimal` (not just `Animal`)
   - Provides built-in taming mechanics, owner tracking, sitting

2. **Taming interaction**:
   - Detect Pumpkin Seeds item (Items.PUMPKIN_SEEDS)
   - Taming chance: ~33% per seed (vanilla wolf-like)
   - Play hearts particles on success
   - Play smoke particles on failure
   - Consume seed on attempt

3. **Override mobInteract()**:
   ```java
   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
       ItemStack itemstack = player.getItemInHand(hand);

       if (itemstack.is(Items.PUMPKIN_SEEDS) && !this.isTamed()) {
           // Taming logic
           if (!player.getAbilities().instabuild) {
               itemstack.shrink(1);
           }

           if (!this.level().isClientSide) {
               if (this.random.nextInt(3) == 0) {
                   this.tame(player);
                   this.level().broadcastEntityEvent(this, (byte)7); // hearts
                   return InteractionResult.SUCCESS;
               } else {
                   this.level().broadcastEntityEvent(this, (byte)6); // smoke
                   return InteractionResult.CONSUME;
               }
           }
           return InteractionResult.sidedSuccess(this.level().isClientSide);
       }

       return super.mobInteract(player, hand);
   }
   ```

4. **NBT data persistence**:
   - Owner UUID saved/loaded
   - Tamed state saved/loaded
   - Command mode saved/loaded (for next task)

5. **Visual feedback**:
   - Tamed crows look at owner
   - Follow owner (basic follow goal)
   - Name tag support

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java`

**Reference**:
- Vanilla wolf taming: `net.minecraft.world.entity.animal.Wolf`
- Vanilla parrot taming: `net.minecraft.world.entity.animal.Parrot`
- Decompiled EntityCrow.java taming logic

**Acceptance Criteria**:
- [ ] Tossing Pumpkin Seeds to wild crow attempts taming
- [ ] Taming succeeds with ~33% chance
- [ ] Hearts particles play on success
- [ ] Smoke particles play on failure
- [ ] Seed is consumed on attempt
- [ ] Tamed crow follows owner
- [ ] Tamed crow persists through save/load
- [ ] Owner UUID saved correctly

**Deliverables**:
- Updated CrowEntity.java with taming system

---

### TASK-005: Implement Command Mode System (4 Modes)

**Status**: TODO
**Priority**: HIGH
**Assigned**: implementation-agent
**Estimated**: 3-4 hours
**Complexity**: Medium
**Dependencies**: TASK-004

**Description**:
Implement 4 command modes that tamed crows can be switched between: Stay, Wander, Follow, Gather Items.

**Command Modes**:

1. **Stay** (Mode 0):
   - Crow sits on hay block (or any block if no hay nearby)
   - Regenerates health slowly when on hay block
   - Won't move unless hurt
   - SitWhenOrderedToGoal active

2. **Wander** (Mode 1):
   - Free roaming behavior
   - Flies around randomly
   - Picks up food items
   - Won't follow owner
   - RandomStrollGoal + PickupFoodGoal active

3. **Follow** (Mode 2):
   - Flies around owner
   - Attempts to perch on shoulders after time
   - FollowOwnerGoal active
   - ShoulderPerchGoal active (TASK-007)

4. **Gather Items** (Mode 3):
   - Flies around hay block center
   - Picks up items within radius
   - Deposits items in containers with matching item frames
   - GatherItemsGoal active (TASK-008)

**Implementation Steps**:

1. **Add command data field**:
   ```java
   private static final EntityDataAccessor<Integer> COMMAND =
       SynchedEntityData.defineId(CrowEntity.class, EntityDataSerializers.INT);

   public int getCommand() {
       return this.entityData.get(COMMAND);
   }

   public void setCommand(int command) {
       this.entityData.set(COMMAND, command);
   }
   ```

2. **Cycle command mode interaction**:
   - Shift + right-click (empty hand) on tamed crow cycles modes
   - Order: Stay → Wander → Follow → Gather → Stay
   - Display mode change message to player
   - Play click sound

3. **Update AI goals** based on command:
   ```java
   @Override
   public void tick() {
       super.tick();

       if (this.isTamed()) {
           int command = this.getCommand();

           // Clear existing goals
           this.goalSelector.removeGoal(sitGoal);
           this.goalSelector.removeGoal(wanderGoal);
           this.goalSelector.removeGoal(followGoal);
           this.goalSelector.removeGoal(gatherGoal);

           // Add goal based on command
           switch (command) {
               case 0: // Stay
                   this.goalSelector.addGoal(1, sitGoal);
                   break;
               case 1: // Wander
                   this.goalSelector.addGoal(3, wanderGoal);
                   break;
               case 2: // Follow
                   this.goalSelector.addGoal(2, followGoal);
                   break;
               case 3: // Gather
                   this.goalSelector.addGoal(2, gatherGoal);
                   break;
           }
       }
   }
   ```

4. **Hay block healing** (Stay mode):
   - Check if standing on hay block
   - Heal 0.5 HP every 2 seconds
   - Particles on heal

5. **NBT persistence**:
   - Save/load command mode
   - Restore goals on world load

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java`

**Reference**:
- Decompiled EntityCrow.java command system
- Vanilla wolf sit/stand toggle

**Acceptance Criteria**:
- [ ] Shift + right-click cycles command modes
- [ ] Mode change message displays to player
- [ ] Stay mode: Crow sits and doesn't move
- [ ] Stay mode: Crow heals on hay block
- [ ] Wander mode: Crow roams freely
- [ ] Follow mode: Crow follows owner
- [ ] Gather mode: Crow flies around hay block (gathering in TASK-008)
- [ ] Command mode persists through save/load
- [ ] All modes work without errors

**Deliverables**:
- Updated CrowEntity.java with command system
- 4 functional command modes

---

### TASK-006: Add Crop Damage & Pumpkin Fleeing Behaviors

**Status**: TODO
**Priority**: MEDIUM
**Assigned**: implementation-agent
**Estimated**: 2 hours
**Complexity**: Medium
**Dependencies**: TASK-003

**Description**:
Wild (untamed) crows damage crops and flee from carved pumpkins. These are pest behaviors that discourage wild crows.

**Feature 1: Crop Damage**:

1. **Create CrowCircleCropsGoal**:
   - Wild crows circle over crop fields
   - Random chance to damage crop when flying over
   - Damages crops: Wheat, Carrots, Potatoes, Beetroot
   - Sets crop age back 1 stage
   - Only active when crow is wild (not tamed)

2. **Implementation**:
   ```java
   public class CrowCircleCropsGoal extends Goal {
       private final CrowEntity crow;
       private BlockPos targetCrop;

       @Override
       public boolean canUse() {
           if (crow.isTamed()) return false;
           // Find nearby crop blocks
           // Random chance to target crop
           return true;
       }

       @Override
       public void tick() {
           // Fly toward crop
           // Random chance to damage crop
           if (crow.random.nextInt(100) < 5) {
               // Reduce crop age by 1
           }
       }
   }
   ```

**Feature 2: Carved Pumpkin Fleeing**:

1. **Create CrowFleePumpkinGoal**:
   - Detect carved pumpkins within radius (16 blocks)
   - Flee away from pumpkin location
   - Fly upward and away
   - Only active when crow is wild

2. **Implementation**:
   ```java
   public class CrowFleePumpkinGoal extends Goal {
       private final CrowEntity crow;
       private BlockPos pumpkinPos;

       @Override
       public boolean canUse() {
           if (crow.isTamed()) return false;
           // Scan for carved pumpkins
           pumpkinPos = findNearbyPumpkin();
           return pumpkinPos != null;
       }

       @Override
       public void tick() {
           // Calculate flee direction (away from pumpkin)
           // Fly upward and away
       }
   }
   ```

**Files to Create**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/CrowCircleCropsGoal.java`
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/CrowFleePumpkinGoal.java`

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java` (add goals)

**Reference**:
- Decompiled CrowAICircleCrops.java
- Vanilla FleeGoal patterns

**Acceptance Criteria**:
- [ ] Wild crows circle over crop fields
- [ ] Wild crows occasionally damage crops (reduce age)
- [ ] Wild crows flee from carved pumpkins
- [ ] Tamed crows don't damage crops
- [ ] Tamed crows don't flee from pumpkins
- [ ] Behaviors disable when crow is tamed

**Deliverables**:
- CrowCircleCropsGoal.java
- CrowFleePumpkinGoal.java
- Updated CrowEntity.java with pest goals

---

## Phase 3: Special Mechanics

### TASK-007: Implement Shoulder Perching (Follow Mode)

**Status**: TODO
**Priority**: MEDIUM
**Assigned**: implementation-agent
**Estimated**: 3-4 hours
**Complexity**: High
**Dependencies**: TASK-005

**Description**:
When crow is in Follow mode, it attempts to perch on owner's shoulders after flying around for a while. Player can remove by sneaking.

**Implementation Requirements**:

1. **Shoulder entity system** (Vanilla parrot pattern):
   - Use vanilla shoulder entity NBT system
   - `Player.setShoulderEntityLeft()` / `setShoulderEntityRight()`
   - Crow disappears from world when on shoulder
   - Respawns when player sneaks

2. **Perching attempt logic**:
   - Only in Follow mode (command == 2)
   - After following for 10-20 seconds
   - When close to owner (< 2 blocks)
   - When owner is on ground (not flying/swimming/riding)
   - Random shoulder selection (left or right)

3. **Implementation**:
   ```java
   public class CrowShoulderPerchGoal extends Goal {
       private final CrowEntity crow;
       private final Player owner;
       private int perchAttemptTime = 0;

       @Override
       public boolean canUse() {
           if (crow.getCommand() != 2) return false; // Not in follow mode
           if (owner == null) return false;
           if (!owner.onGround()) return false;
           if (crow.distanceTo(owner) > 2.0) return false;
           if (perchAttemptTime++ < 200) return false; // Wait 10 seconds
           return true;
       }

       @Override
       public void start() {
           // Save crow to shoulder NBT
           CompoundTag crowTag = new CompoundTag();
           crow.saveWithoutId(crowTag);

           // Choose random shoulder
           if (owner.getShoulderEntityLeft().isEmpty()) {
               owner.setShoulderEntityLeft(crowTag);
           } else if (owner.getShoulderEntityRight().isEmpty()) {
               owner.setShoulderEntityRight(crowTag);
           }

           // Remove crow from world
           crow.discard();
       }
   }
   ```

4. **Dismount on sneak**:
   - Vanilla handles this automatically
   - Crow respawns at player location when player sneaks
   - Reset perch attempt timer

5. **Rendering on shoulder**:
   - Render small crow model on player shoulder
   - Use smaller scale (0.5×)
   - Rotate to face forward

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java`

**Files to Create** (if needed):
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/CrowShoulderPerchGoal.java`

**Reference**:
- Vanilla parrot shoulder mechanics: `net.minecraft.world.entity.animal.Parrot`
- Decompiled MessageCrowMountPlayer.java, MessageCrowDismount.java

**Acceptance Criteria**:
- [ ] Crow in Follow mode attempts to perch after 10+ seconds
- [ ] Crow disappears from world when on shoulder
- [ ] Crow renders on player's shoulder (left or right)
- [ ] Player sneaking dismounts crow
- [ ] Crow respawns at player location on dismount
- [ ] Shoulder crow persists through world reload
- [ ] No crashes or duplication bugs

**Deliverables**:
- Shoulder perching system
- Updated CrowEntity.java with perch goal

---

### TASK-008: Implement Item Gathering System (Gather Mode)

**Status**: TODO
**Priority**: MEDIUM
**Assigned**: implementation-agent
**Estimated**: 4-6 hours
**Complexity**: High
**Dependencies**: TASK-005

**Description**:
When crow is in Gather mode, it flies around hay block, picks up items within radius, and deposits them in nearby containers that have matching item frames.

**System Components**:

**Part 1: Hay Block Center Point**:
- Crow finds nearest hay block when entering Gather mode
- Stores hay block position as gathering center
- Flies around hay block in circular pattern
- Re-scans for hay block if original is broken

**Part 2: Item Pickup**:
- Scan for ItemEntity drops within radius (10 blocks from hay block)
- Pick up any item (not just food)
- Hold item in beak (rendering layer)
- CrowEntity implements ITargetsDroppedItems interface

**Part 3: Container Deposit System**:

1. **Find containers with item frames**:
   - Scan for BlockEntity containers (chest, furnace, barrel, etc.)
   - Check each side (6 directions) for item frame
   - Match held item to item in frame

2. **Deposit logic**:
   ```java
   private boolean depositItem(ItemStack heldItem) {
       // Find nearest hay block
       BlockPos hayPos = findNearestHayBlock();

       // Scan for containers near hay block
       for (BlockPos containerPos : scanContainers(hayPos, 16)) {
           // Check each side for item frame
           for (Direction dir : Direction.values()) {
               BlockPos framePos = containerPos.relative(dir);
               ItemFrame frame = findItemFrame(framePos);

               if (frame != null && matchesItem(frame.getItem(), heldItem)) {
                   // Deposit from this side
                   BlockEntity be = level.getBlockEntity(containerPos);
                   if (be instanceof Container) {
                       // Use Fabric item API to insert
                       ItemStack remainder = insertItem(be, heldItem, dir.getOpposite());
                       if (remainder.isEmpty()) {
                           return true; // Successfully deposited
                       }
                   }
               }
           }
       }
       return false; // No matching container
   }
   ```

**Part 4: AI Goal**:
```java
public class CrowGatherItemsGoal extends Goal {
    private final CrowEntity crow;
    private BlockPos hayBlockPos;
    private ItemEntity targetItem;
    private BlockPos depositPos;
    private int gatherPhase = 0; // 0=find hay, 1=find item, 2=pickup, 3=deposit

    @Override
    public boolean canUse() {
        return crow.getCommand() == 3; // Gather mode
    }

    @Override
    public void tick() {
        switch (gatherPhase) {
            case 0: // Find hay block
                hayBlockPos = findNearestHayBlock();
                if (hayBlockPos != null) gatherPhase = 1;
                break;
            case 1: // Find item
                targetItem = findNearestItem(hayBlockPos, 10);
                if (targetItem != null) gatherPhase = 2;
                break;
            case 2: // Pick up item
                if (crow.distanceTo(targetItem) < 1.0) {
                    crow.pickupItem(targetItem);
                    gatherPhase = 3;
                }
                break;
            case 3: // Deposit item
                if (depositItem(crow.getHeldItem())) {
                    crow.clearHeldItem();
                    gatherPhase = 1; // Start over
                }
                break;
        }
    }
}
```

**Files to Create**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/ai/CrowGatherItemsGoal.java`
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/client/render/layer/LayerCrowItem.java` (item rendering)

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java`

**Reference**:
- Decompiled EntityCrow.java gathering logic
- Decompiled LayerCrowItem.java rendering
- Fabric Transfer API for item insertion

**Acceptance Criteria**:
- [ ] Crow in Gather mode flies around hay block
- [ ] Crow picks up items within 10 blocks of hay block
- [ ] Crow holds item in beak (visible)
- [ ] Crow finds containers with item frames
- [ ] Crow deposits item if frame matches held item
- [ ] Deposit works from correct side (frame direction)
- [ ] Crow continues gathering after deposit
- [ ] No item duplication bugs
- [ ] Works with various containers (chest, barrel, furnace, hopper)

**Deliverables**:
- CrowGatherItemsGoal.java
- LayerCrowItem.java (item rendering)
- Updated CrowEntity.java with gathering system

---

### TASK-009: Add Combat Features (Defense & Undead Bonus)

**Status**: TODO
**Priority**: LOW
**Assigned**: implementation-agent
**Estimated**: 1-2 hours
**Complexity**: Simple
**Dependencies**: TASK-004

**Description**:
Tamed crows defend their owner by pecking attackers. Pecks deal low damage (won't aggro mobs) but deal extra damage to undead.

**Implementation**:

1. **Owner defense goals** (vanilla pattern):
   ```java
   // In CrowEntity.registerGoals()
   this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
   this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
   ```

2. **Peck attack**:
   - Melee attack goal
   - Damage: 1.0 (half heart)
   - Attack speed: Fast (1.0 second cooldown)
   - Low enough not to make target aggressive

3. **Undead damage bonus**:
   ```java
   @Override
   public boolean doHurtTarget(Entity target) {
       float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);

       // Extra damage to undead
       if (target instanceof LivingEntity living) {
           if (living.getMobType() == MobType.UNDEAD) {
               damage += 2.0F; // Total 3.0 damage to undead
           }
       }

       return target.hurt(this.damageSources().mobAttack(this), damage);
   }
   ```

4. **Attack animation**:
   - Play "attack" animation during peck
   - Particle effects on hit
   - Sound effect (crow caw)

**Files to Modify**:
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/CrowEntity.java`

**Reference**:
- Vanilla wolf defense behavior
- Decompiled CrowAIMelee.java

**Acceptance Criteria**:
- [ ] Tamed crow attacks owner's attacker
- [ ] Peck deals 1.0 damage to normal mobs
- [ ] Peck deals 3.0 damage to undead mobs
- [ ] Attack doesn't make target aggressive toward player
- [ ] Attack animation plays during peck
- [ ] Sound plays on attack

**Deliverables**:
- Updated CrowEntity.java with combat goals

---

### TASK-010: Add Sounds (Ambient, Hurt, Death)

**Status**: TODO
**Priority**: LOW
**Assigned**: implementation-agent
**Estimated**: 1 hour
**Complexity**: Simple
**Dependencies**: TASK-001

**Description**:
Add crow sounds for ambient cawing, hurt sounds, and death sound.

**Sound Files Available**:
- `crow_hurt_0.ogg`, `crow_hurt_1.ogg` (extracted from Alex's Mobs)
- Need to create/find ambient and death sounds

**Implementation**:

1. **Copy sound files**:
   - From: `xeenaa-alexs-mobs/.claude/epics/002-mob-catalog-asset-inventory/alexsmobs-extracted/assets/alexsmobs/sounds/`
   - To: `xeenaa-alexs-mobs/src/main/resources/assets/xeenaa-alexs-mobs/sounds/entity/crow/`

2. **Register sounds** in sounds.json:
   ```json
   {
     "crow.ambient": {
       "sounds": [
         "xeenaa-alexs-mobs:entity/crow/crow_ambient_0",
         "xeenaa-alexs-mobs:entity/crow/crow_ambient_1"
       ],
       "subtitle": "Crow caws"
     },
     "crow.hurt": {
       "sounds": [
         "xeenaa-alexs-mobs:entity/crow/crow_hurt_0",
         "xeenaa-alexs-mobs:entity/crow/crow_hurt_1"
       ],
       "subtitle": "Crow hurts"
     },
     "crow.death": {
       "sounds": [
         "xeenaa-alexs-mobs:entity/crow/crow_death"
       ],
       "subtitle": "Crow dies"
     }
   }
   ```

3. **Override sound methods** in CrowEntity:
   ```java
   @Override
   protected SoundEvent getAmbientSound() {
       return ModSounds.CROW_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
       return ModSounds.CROW_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
       return ModSounds.CROW_DEATH;
   }
   ```

4. **Register in ModSounds.java**:
   ```java
   public static final RegistryObject<SoundEvent> CROW_AMBIENT =
       SOUNDS.register("crow.ambient", () -> SoundEvent.createVariableRangeEvent(...));
   ```

**Files to Create/Modify**:
- `xeenaa-alexs-mobs/src/main/resources/assets/xeenaa-alexs-mobs/sounds.json`
- `xeenaa-alexs-mobs/src/main/java/com/canya/xeenaa_alexs_mobs/registry/ModSounds.java`
- Sound files in `src/main/resources/assets/xeenaa-alexs-mobs/sounds/entity/crow/`

**Acceptance Criteria**:
- [ ] Crow makes ambient cawing sounds periodically
- [ ] Crow makes hurt sound when damaged
- [ ] Crow makes death sound when killed
- [ ] Sounds are audible and appropriate volume
- [ ] No console errors about missing sounds

**Deliverables**:
- Crow sound files
- Updated sounds.json
- Updated ModSounds.java
- Updated CrowEntity.java

---

### TASK-011: User Manual Validation (All Features)

**Status**: TODO
**Priority**: CRITICAL
**Assigned**: User
**Estimated**: 1-2 hours
**Complexity**: Simple
**Dependencies**: TASK-001 through TASK-010

**Description**:
Comprehensive user testing of all crow features to validate implementation before considering epic complete.

**Test Checklist**:

**Basic Functionality**:
- [ ] Crow spawns from egg
- [ ] Crow texture renders correctly
- [ ] All 6 animations work (idle, walk, run, fly, sit, attack)
- [ ] Crow AI is functional (flying, landing, pathfinding)

**Taming System**:
- [ ] Toss Pumpkin Seeds to wild crow
- [ ] Taming succeeds with ~33% chance
- [ ] Hearts particles on success
- [ ] Smoke particles on failure
- [ ] Tamed crow follows owner

**Command Modes**:
- [ ] Shift + right-click cycles modes
- [ ] Mode 0 (Stay): Crow sits on blocks
- [ ] Mode 0 (Stay): Crow heals on hay block
- [ ] Mode 1 (Wander): Crow roams freely, picks up food
- [ ] Mode 2 (Follow): Crow follows owner
- [ ] Mode 3 (Gather): Crow flies around hay block

**Shoulder Perching** (Follow Mode):
- [ ] Crow perches on shoulder after following
- [ ] Crow renders on shoulder correctly
- [ ] Sneaking dismounts crow
- [ ] Crow respawns at player location

**Item Gathering** (Gather Mode):
- [ ] Place hay block in area
- [ ] Drop items near hay block
- [ ] Crow picks up items
- [ ] Place chest with item frame
- [ ] Put matching item in frame
- [ ] Crow deposits item in chest from frame side
- [ ] Crow continues gathering more items

**Pest Behaviors** (Wild Only):
- [ ] Wild crow damages crops when flying over
- [ ] Wild crow flees from carved pumpkins
- [ ] Tamed crow doesn't damage crops
- [ ] Tamed crow doesn't flee from pumpkins

**Combat**:
- [ ] Tamed crow defends owner when attacked
- [ ] Crow pecks attacker
- [ ] Low damage to normal mobs
- [ ] Extra damage to zombies/skeletons (undead)

**Sounds**:
- [ ] Ambient cawing plays periodically
- [ ] Hurt sound when damaged
- [ ] Death sound when killed

**Persistence**:
- [ ] Tamed crow persists through save/load
- [ ] Command mode persists
- [ ] Owner UUID persists
- [ ] Shoulder crow persists (when on shoulder during save)

**Performance & Bugs**:
- [ ] No FPS drops with multiple crows
- [ ] No console errors
- [ ] No item duplication bugs
- [ ] No pathfinding getting stuck
- [ ] No texture glitches

**Acceptance Criteria**:
- [ ] All test scenarios pass
- [ ] User approves crow for release
- [ ] No blocking bugs identified

**Deliverables**:
- User validation report
- Bug list (if any issues found)

---

### TASK-012: Create Automated Tests (Post-Validation)

**Status**: TODO
**Priority**: LOW
**Assigned**: implementation-agent
**Estimated**: 2-3 hours
**Complexity**: Medium
**Dependencies**: TASK-011 (user validation complete)

**Description**:
Create automated tests to prevent regression of crow features in future updates. Only done AFTER user manual validation is complete.

**Test Coverage**:

1. **Entity Tests**:
   - Crow spawns correctly
   - Crow has correct attributes (health, speed, size)
   - Crow AI goals registered

2. **Taming Tests**:
   - Pumpkin Seeds tame crow
   - Taming sets owner UUID
   - Tamed crow follows owner

3. **Command Mode Tests**:
   - Cycling commands works
   - Each mode activates correct goals
   - Command persists through NBT

4. **Item Gathering Tests**:
   - Crow picks up items
   - Crow finds containers
   - Crow deposits items correctly
   - No item duplication

5. **Combat Tests**:
   - Crow attacks owner's attacker
   - Damage correct (1.0 normal, 3.0 undead)

**Files to Create**:
- `xeenaa-alexs-mobs/src/test/java/com/canya/xeenaa_alexs_mobs/entity/CrowEntityTest.java`
- `xeenaa-alexs-mobs/src/test/java/com/canya/xeenaa_alexs_mobs/entity/CrowTamingTest.java`
- `xeenaa-alexs-mobs/src/test/java/com/canya/xeenaa_alexs_mobs/entity/CrowGatheringTest.java`

**Acceptance Criteria**:
- [ ] All tests pass
- [ ] Tests cover critical features
- [ ] Tests prevent regression

**Deliverables**:
- Automated test suite for crow

---

## Task Summary

| Phase | Tasks | Status | Estimated Time |
|-------|-------|--------|----------------|
| Phase 1: Bug Fixes | 3 | TODO | 2-3 hours |
| Phase 2: Taming & Commands | 4 | TODO | 9-11 hours |
| Phase 3: Special Mechanics | 5 | TODO | 11-15 hours |
| **TOTAL** | **12** | **TODO** | **22-29 hours** |

---

## Dependencies Graph

```
TASK-001 (Fix Texture) ─┬─> TASK-003 (User Validation) ─> TASK-004 (Taming) ─┬─> TASK-005 (Commands) ─┬─> TASK-007 (Shoulder Perch)
                         │                                                      │                        └─> TASK-008 (Item Gathering)
TASK-002 (Spawn Egg) ────┘                                                      └─> TASK-009 (Combat)

TASK-006 (Pest Behaviors) ────────────────────────────────────────────────────────> (No dependencies)

TASK-010 (Sounds) ────────────> TASK-001 (Fix Texture)

TASK-011 (User Validation) ───> ALL PREVIOUS TASKS ───> TASK-012 (Automated Tests)
```

---

## Notes

**Current State**: Epic 007 has partial implementation from previous work. This plan focuses on fixing bugs and completing missing features.

**Epic 032 (Duplicate)**: There's also an Epic 032 for Crow with updated requirements. After completing Epic 007, consider merging/deleting Epic 032 to avoid confusion.

**Complexity Estimate**: Medium - Most code exists, need bug fixes + feature additions. Item gathering system is most complex part.

**Next Epic Recommendation**: After Crow, consider simple animals (Banana Slug, Jerboa, Rain Frog) before tackling complex ones (Elephant, Bald Eagle, Leafcutter Ant).

---

**Created**: 2025-10-27
**Status**: 📋 READY FOR EXECUTION
