# Minecraft Villager System

**Minecraft Version**: 1.21.1 (mechanics from 1.14+)
**Last Updated**: 2025-10-27
**Tags**: minecraft, villagers, professions, textures, ai, pathfinding, brain-system, beds, poi

## Overview

Complete documentation of Minecraft's villager system including profession textures, brain AI, bed claiming mechanics, and the Point of Interest (POI) system.

---

## Part 1: Villager Profession Texture System

### How Profession Textures Work

#### Automatic Texture Loading

Minecraft's resource pack system automatically searches for profession textures using this pattern:
```
assets/{mod_namespace}/textures/entity/villager/profession/{profession_id}.png
```

When a villager is assigned a profession, the game:
1. Gets the profession's identifier (e.g., `modnamespace:profession_name`)
2. Constructs the texture path automatically
3. Loads the texture - **no code registration needed**

#### Texture File Requirements

**File Specifications**:
- Resolution: 64x64 pixels
- Format: PNG with transparency support
- Content: Villager profession outfit/appearance
- Path: Must match profession ID exactly (case-sensitive on some systems)

#### Optional Metadata File

**Path**: `{texture_path}.mcmeta`
**Purpose**: Defines hat rendering properties

```json
{
  "villager": {
    "hat": "full"
  }
}
```

#### What Causes Purple/Violet Villagers

Purple/violet appearance indicates Minecraft's "missing texture" fallback. This happens when:
- The texture file doesn't exist at the expected path
- The texture file is corrupted or invalid format
- The file name doesn't match the profession ID exactly

#### Version History

- **Old Fabric Versions**: Required `VillagerProfessionBuilder` for registration
- **Minecraft 1.21.1**: `VillagerProfessionBuilder` is deprecated
- **Current System**: Uses direct `VillagerProfession` constructor + automatic texture loading

#### Modern Fabric Implementation

No client-side registration code needed:
1. Create profession with proper identifier
2. Register with Minecraft registry
3. Place texture at correct path
4. Texture system handles the rest automatically

#### Debugging Profession Textures

1. Check resource pack structure is correct
2. Ensure file names match profession IDs exactly
3. Verify PNG format and dimensions (64x64)
4. Look for resource loading errors in game logs
5. Test with F3+T (resource pack reload)

---

## Part 2: Villager Brain & Bed Claiming System

### Brain Memory System

Villagers use Minecraft's brain AI system introduced in 1.14 (Village & Pillage update).

#### Key Memory Modules

**MemoryModuleType.HOME**
- Type: `MemoryModuleType<GlobalPos>`
- Purpose: Stores location of claimed bed
- Package: `net.minecraft.entity.ai.brain.MemoryModuleType`
- Data Format: `GlobalPos` (dimension + BlockPos)

**Other Important Memories**:
- `minecraft:home` - Bed location (GlobalPos)
- `minecraft:job_site` - Workstation location (GlobalPos)
- `minecraft:meeting_point` - Bell location (GlobalPos)

#### Inspecting Villager Memory (Debug)

```bash
# View all brain memories for nearest villager
/data get entity @e[type=minecraft:villager,limit=1,sort=nearest] Brain.memories

# Check HOME memory specifically
/data get entity @e[type=minecraft:villager,limit=1,sort=nearest] Brain.memories."minecraft:home"
```

### Bed Claiming Process

#### Claiming Requirements (All Must Be True)

1. **Distance**: Within 48-block sphere of bed (measured from pillow block)
2. **Pathfinding State**: Villager must be actively pathfinding
3. **Availability**: Bed not already claimed by another villager
4. **Time Independent**: Can claim at any time (day or night)
5. **Line of Sight Independent**: Does not require visual contact

#### Bed Location Definition

- Bed position defined by **pillow block** coordinates
- After claiming, TWO-WAY binding occurs:
  - Villager remembers bed coordinates (in `MemoryModuleType.HOME`)
  - Bed block remembers which villager claimed it

#### Brain Tasks Involved in Claiming

**FindPointOfInterestTask**
- Package: `net.minecraft.entity.ai.brain.task`
- Purpose: Searches for unclaimed points of interest (beds, workstations)
- Search Radius: 48 blocks
- Behavior: When detected, villager pathfinds to claim it

**SleepTask**
- Package: `net.minecraft.entity.ai.brain.task`
- Extends: `MultiTickTask`
- Purpose: Manages sleep behavior AND bed claiming
- Active Period: Tick 12000-23999 (night time)
- Key Point: Handles BOTH claiming AND sleeping

**JumpInBedTask**
- Package: `net.minecraft.entity.ai.brain.task`
- Extends: `MultiTickTask`
- Purpose: Handles physical action of getting into bed

### Bed Forgetting/Unclaiming Process

Villagers forget their bed under two conditions:

#### Condition 1: Bed Removal (Proximity Check)
- Distance: Within ~2 blocks of remembered bed location
- Trigger: Villager attempts to sleep
- Check: Bed no longer exists at pillow coordinates
- Note: Rotating or moving bed DOES trigger forgetting

#### Condition 2: Distance-Based Forgetting
- Distance: 150+ blocks away (taxicab/Manhattan distance)
- State Requirement: Villager is pathfinding
- Trigger: Villager attempts to sleep
- Formula: `|x1 - x2| + |y1 - y2| + |z1 - z2| >= 150`

### Related Brain Tasks

**VillagerTaskListProvider**
- Package: `net.minecraft.entity.ai.brain.task`
- Key Method: `createRestTasks(VillagerProfession profession, float speed)`
- Returns: `ImmutableList` of task pairs for rest behavior
- Purpose: Assembles all sleep/rest-related tasks

**Additional Sleep-Related Tasks**:
- `WakeUpTask` - Handles waking up logic
- `WalkHomeTask` - Pathfinding to claimed bed
- `HideInHomeTask` - Taking shelter behavior
- `GoToPointOfInterestTask` - Generic POI navigation
- `GoToRememberedPositionTask` - Navigate to stored memory location

### Point of Interest (POI) System

**PointOfInterestType.HOME**
- Block Type: Bed blocks
- Registry: Beds registered as `HOME` type POI
- Claiming Mechanic: Nearly identical to workstation claiming
- Storage: POI data stored at chunk level

**Important**: The `VillagerEntity.sleep(BlockPos pos)` method is called AFTER claiming, not during.

**Claiming Flow**:
```
1. FindPointOfInterestTask finds unclaimed bed
2. Villager pathfinds to bed (provisional claim established)
3. SleepTask stores bed location in MemoryModuleType.HOME
4. Bed block records villager UUID as claimant
5. Later: sleep() method called to physically enter bed
```

### Preventing Bed Claiming (For Custom Villager Types)

#### Key Methods to Override

**VillagerEntity Methods**:
```java
public boolean wantsToSleep()  // Checked before sleep tasks
protected boolean canSleep()   // Secondary check
public void sleep(BlockPos pos)
public void wakeUp()
public Brain<VillagerEntity> getBrain()
```

**Brain Methods (Generic)**:
```java
public <U> void remember(MemoryModuleType<U> type, U value)
public <U> void remember(MemoryModuleType<U> type, U value, long expiry)
public <U> void forget(MemoryModuleType<U> type)
public <U> Optional<U> getOptionalMemory(MemoryModuleType<U> type)
```

#### Recommended Approach

To prevent custom villagers from claiming beds:

**Step 1**: Override `wantsToSleep()` to return false
```java
@Inject(method = "wantsToSleep", at = @At("HEAD"), cancellable = true)
private void preventSleep(CallbackInfoReturnable<Boolean> cir) {
    // Check if custom villager type
    // If yes: cir.setReturnValue(false);
}
```

**Step 2**: Clear HOME memory when villager becomes custom type
```java
villager.getBrain().forget(MemoryModuleType.HOME);
```

**Step 3**: Override `canSleep()` as backup
```java
@Inject(method = "canSleep", at = @At("HEAD"), cancellable = true)
private void preventSleep(CallbackInfoReturnable<Boolean> cir) {
    // Check if custom villager type
    // If yes: cir.setReturnValue(false);
}
```

#### Why This Works

1. `wantsToSleep()` is checked by `SleepTask` before attempting to claim/sleep
2. Returning `false` prevents entire sleep task chain from executing
3. No claiming = bed stays available for other villagers
4. Clearing existing HOME memory releases already-claimed beds
5. `canSleep()` provides defense-in-depth

### Physical Prevention (Not Recommended)

You can prevent claiming by blocking pathfinding:
- Surround villager with solid blocks at foot or eye level
- Requires 6-8 blocks (glass works)
- **Problem**: Breaks villager mobility - not suitable for mobile custom villagers

### Testing & Verification

**Test Cases**:
1. Custom villager should not claim new beds
2. Existing claimed beds should be released when villager becomes custom type
3. Other villagers should successfully claim previously unavailable beds
4. Custom villagers should remain active during night (tick 12000-23999)
5. Memory check should show no HOME memory

**Debug Commands**:
```bash
# Check villager's brain memories
/data get entity @e[type=minecraft:villager,limit=1,sort=nearest] Brain.memories."minecraft:home"

# Check bed claim status
/data get block <x> <y> <z>
```

---

## Version Compatibility

- Mechanics confirmed for Minecraft 1.14 through 1.21.1
- Brain system introduced in 1.14 (Village & Pillage update)
- Texture loading pattern stable since 1.14
- Yarn mappings available for all modern versions

## Primary Sources

- Yarn Mappings (FabricMC): https://maven.fabricmc.net/docs/
- Villager Mechanics Analysis: https://gist.github.com/orlp/db1ca6dbb82727c4a939c95694a52b81
- Brain System: `net.minecraft.entity.ai.brain` package
- Resource Pack Documentation: Minecraft Wiki

## Related Knowledge

- `.claude/knowledge/fabric-api/registry.md` - Entity and block registration
- `.claude/knowledge/minecraft/entities.md` - General entity systems
