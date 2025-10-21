# Guard Combat Balance - In-Game Testing Guide
**Task**: P4-TASK-003.1 Manual Testing
**Date**: October 19, 2025

---

## Quick Start Testing

### 1. Launch Minecraft Client

The client is already building/running in background. If you need to launch fresh:
```bash
cd xeena-village-manager
./gradlew runClient
```

### 2. Create Test World

1. Create new **Creative** world
2. Enable **cheats** (for commands)
3. Set difficulty to **Normal** (for standard damage values)

---

## Test Scenarios

### Test 1: Ranged Guard (Tier 0) vs Skeleton

**Goal**: Tier 0 Recruit should be **slightly weaker** than Skeleton (89% DPS)

**Setup**:
```
/summon minecraft:villager ~ ~ ~ {VillagerData:{profession:"xeenaa_villager_manager:guard"}}
```

**Equip the guard** (right-click with bow + arrows):
1. Give guard a **bow** (drag from your inventory)
2. Give guard **arrows** (at least 64)

**Spawn opponent**:
```
/summon minecraft:skeleton ~5 ~ ~
```

**What to observe**:
- ⏱️ **Attack Speed**: Recruit fires every **2.25 seconds**, Skeleton ~2.0s
- 💥 **Damage**: Both deal ~3.5 damage per arrow
- 🎯 **Outcome**: Should be **close fight**, Skeleton slight advantage

**Expected Result**: Skeleton wins 60-70% of the time

---

### Test 2: Ranged Guard (Tier 4) vs Skeleton

**Goal**: Tier 4 Sharpshooter should be **much stronger** (171% DPS + special abilities)

**Setup**:
```
# Create Tier 4 guard (requires NBT data - see Test Setup Helper below)
```

**Equip with bow + arrows**

**Spawn opponent**:
```
/summon minecraft:skeleton ~5 ~ ~
```

**What to observe**:
- ⏱️ **Attack Speed**: Sharpshooter fires every **1.5 seconds**, Skeleton ~2.0s
- 💥 **Damage**: Sharpshooter 4.5, Skeleton 3.5
- ✨ **Special Abilities**: Watch for Double Shot (2 arrows), Multishot (3 arrows)
- 🎯 **Outcome**: Guard should **dominate**

**Expected Result**: Sharpshooter wins 90%+ of the time

---

### Test 3: Melee Guard (Tier 0) vs Zombie

**Goal**: Tier 0 Recruit should be **weaker** than Zombie when unarmed, competitive with weapon

**Setup**:
```
/summon minecraft:villager ~ ~ ~ {VillagerData:{profession:"xeenaa_villager_manager:guard"}}
```

**Equip with Iron Sword**:
1. Give guard **iron_sword**

**Spawn opponent**:
```
/summon minecraft:zombie ~3 ~ ~
```

**What to observe**:
- ⏱️ **Attack Speed**: Both attack every **1.0 second** (20 ticks)
- 💥 **Damage**: Guard with Iron Sword = 6.5, Zombie = 3.0
- 🎯 **Outcome**: Guard should win due to weapon advantage

**Expected Result**:
- **Without weapon**: Zombie wins
- **With Iron Sword**: Guard wins (justified by player investment)

---

### Test 4: Melee Guard (Tier 4) vs Zombie

**Goal**: Tier 4 Knight should be **very strong** (118% Vindicator DPS)

**Setup**: Tier 4 Knight with Iron Sword

**Spawn multiple zombies**:
```
/summon minecraft:zombie ~3 ~ ~
/summon minecraft:zombie ~-3 ~ ~
/summon minecraft:zombie ~ ~ ~3
```

**What to observe**:
- ⏱️ **Attack Speed**: Knight attacks every **0.65 seconds**, Zombies 1.0s
- 💥 **Damage**: 10.0 per hit
- ✨ **Special Abilities**: Area damage, Slowness II debuff (30% chance)
- 🎯 **Outcome**: Knight should handle multiple zombies

**Expected Result**: Knight wins against 2-3 zombies simultaneously

---

### Test 5: Attack Speed Timing Test

**Goal**: Verify exact attack speeds match the formulas

**Setup**: Create guards of each tier with stopwatch/counter

**Ranged Guards (with bow)**:
| Tier | Expected Cooldown | How to Verify |
|------|-------------------|---------------|
| 0 | 2.25s (45 ticks) | Count "1-Mississippi, 2-Mississippi, quarter" |
| 1 | 2.1s (42 ticks) | Slightly faster than Tier 0 |
| 2 | 1.9s (38 ticks) | Just under 2 seconds |
| 4 | 1.5s (30 ticks) | "1-Mississippi, half" |

**Melee Guards (with sword)**:
| Tier | Expected Cooldown | How to Verify |
|------|-------------------|---------------|
| 0 | 1.0s (20 ticks) | "1-Mississippi" exactly |
| 2 | 0.85s (17 ticks) | Slightly faster than 1 second |
| 4 | 0.65s (13 ticks) | About 2 attacks per "1-Mississippi" |

**Method**:
1. Place guard near training dummy (armor stand or zombie with regeneration)
2. Count attacks over 10 seconds
3. Divide 10 by number of attacks = cooldown

**Check logs** at `logs/latest.log`:
```
[RANGED COMBAT] Guard ... shot slowness arrow at ...
[MELEE COMBAT] Guard ... attacked ...
```

---

## Test Setup Helpers

### Quick Tier 4 Sharpshooter Setup

**Option 1: Use GUI** (Recommended)
1. Summon guard villager
2. Right-click to open GUI
3. Navigate to Rank Management
4. Purchase ranks: Recruit → Ranger I → Ranger II → Ranger III → Sharpshooter
   - Costs: 15 + 20 + 45 + 75 = **155 emeralds total**
5. Equip with bow + arrows

**Option 2: Creative Mode Shortcut**
```
# Give yourself emeralds
/give @p minecraft:emerald 200

# Follow Option 1 steps above
```

### Quick Tier 4 Knight Setup

Same as above, but choose:
- Recruit → Soldier I → Soldier II → Soldier III → Knight
- Equip with Iron Sword instead of bow

### Testing Arena Setup

**Build a testing arena**:
```
/fill ~-10 ~ ~-10 ~10 ~5 ~10 minecraft:air
/fill ~-10 ~-1 ~-10 ~10 ~-1 ~10 minecraft:stone
/fill ~-10 ~ ~-10 ~-10 ~5 ~10 minecraft:glass
/fill ~10 ~ ~-10 ~10 ~5 ~10 minecraft:glass
/fill ~-10 ~ ~-10 ~10 ~5 ~-10 minecraft:glass
/fill ~-10 ~ ~10 ~10 ~5 ~10 minecraft:glass
```

This creates a 20x20 enclosed arena.

### Spawn Mobs with Health Bars

**Skeleton with visible health**:
```
/summon minecraft:skeleton ~5 ~ ~ {CustomName:'{"text":"Test Skeleton"}',CustomNameVisible:1b}
```

**Zombie with visible health**:
```
/summon minecraft:zombie ~3 ~ ~ {CustomName:'{"text":"Test Zombie"}',CustomNameVisible:1b}
```

---

## What to Check in Logs

Monitor `logs/latest.log` for combat events:

**Ranged Combat Logs**:
```
[RANGED COMBAT] Guard a1b2c3d4 (Rank: Recruit, Tier: 0) shot slowness arrow at Skeleton | Distance: 12.45 blocks | Accuracy: 14.00 | Velocity: 1.60
```

**Melee Combat Logs**:
```
[MELEE COMBAT] Guard a1b2c3d4 (Rank: Recruit, Tier: 0) attacked Zombie with Iron Sword | Base Dmg: 0.50 + Weapon: 6.00 = Final: 6.50 | Hit: true
```

**Attack Cooldowns** (check these match expected values):
- Ranged Tier 0: Should see attacks ~45 ticks (2.25s) apart
- Melee Tier 0: Should see attacks ~20 ticks (1.0s) apart

---

## Success Criteria

### ✅ Ranged Balance is Correct If:

1. **Tier 0 Recruit** loses to Skeleton in 1v1 (or very close)
2. **Tier 1 Ranger** is competitive with Skeleton (50/50 fights)
3. **Tier 4 Sharpshooter** dominates Skeleton easily
4. **Attack speeds** match expected values (2.25s → 1.5s)
5. **Damage scaling** increases with tier (3.5 → 4.5)

### ✅ Melee Balance is Correct If:

1. **Tier 0 Recruit** (unarmed) loses to Zombie
2. **Tier 0 Recruit** (Iron Sword) beats Zombie (weapon advantage)
3. **Tier 4 Knight** can handle 2-3 Zombies simultaneously
4. **Attack speeds** match expected values (1.0s → 0.65s)
5. **Special abilities** activate at Tier 4 (area damage, slowness)

---

## Red Flags to Watch For

### ❌ Balance is BROKEN If:

1. **Tier 0 easily dominates Skeleton** - Attack speed too fast
2. **Tier 4 loses to Skeleton** - Damage too low
3. **Guards attack multiple times per second** - Cooldown formula broken
4. **Arrows do 2.5 damage** - Tier scaling not working
5. **Melee guards attack faster than 0.65s at Tier 4** - Formula not applied

---

## Advanced Testing

### DPS Verification Test

**Setup**:
1. Spawn zombie with **Regeneration 255**:
```
/summon minecraft:zombie ~ ~ ~ {ActiveEffects:[{Id:10,Amplifier:255,Duration:999999}]}
```

2. Spawn guard of specific tier with weapon
3. Record damage over 30 seconds
4. Calculate DPS = Total Damage / 30

**Expected DPS** (Normal difficulty):

**Ranged**:
- Tier 0: ~1.56 DPS
- Tier 4: ~3.00 DPS

**Melee** (with Iron Sword):
- Tier 0: ~6.5 DPS
- Tier 4: ~15.4 DPS

### Multi-Mob Stress Test

**Tier 4 Sharpshooter vs Multiple Skeletons**:
```
/summon minecraft:skeleton ~5 ~ ~
/summon minecraft:skeleton ~-5 ~ ~
/summon minecraft:skeleton ~ ~ ~5
/summon minecraft:skeleton ~ ~ ~-5
```

**Expected**: Sharpshooter wins with special abilities (Multishot, Double Shot)

**Tier 4 Knight vs Multiple Zombies**:
```
# Spawn 5 zombies in a circle
```

**Expected**: Knight wins with area damage and CC abilities

---

## Reporting Issues

If balance feels wrong, report:

1. **Tier tested**: (0, 1, 2, 3, or 4)
2. **Equipment**: (weapon type)
3. **Opponent**: (mob type)
4. **Result**: (who won, by how much)
5. **Expected vs Actual**: What should have happened
6. **Log snippet**: Copy combat logs if available

**Example Report**:
```
Tier: 0 Recruit (Ranged)
Equipment: Bow + Arrows
Opponent: Skeleton
Result: Guard won easily (took only 3 damage)
Expected: Should be close fight or lose
Log: [Shows guard attacking every 1.5s instead of 2.25s]
```

---

## Quick Reference: Expected Outcomes

| Matchup | Expected Winner | Margin |
|---------|----------------|--------|
| Recruit (Ranged, Tier 0) vs Skeleton | Skeleton | Slight |
| Ranger I (Tier 1) vs Skeleton | 50/50 | Even |
| Sharpshooter (Tier 4) vs Skeleton | Guard | Dominant |
| Recruit (Melee, unarmed) vs Zombie | Zombie | Clear |
| Recruit (Iron Sword) vs Zombie | Guard | Moderate |
| Knight (Tier 4) vs Zombie | Guard | Dominant |
| Knight (Tier 4) vs 3 Zombies | Guard | Hard fight |
| Sharpshooter (Tier 4) vs 3 Skeletons | Guard | Moderate |

---

**Ready to test? Launch the game and start with Test 1!** 🎮
