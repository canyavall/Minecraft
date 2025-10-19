# Ranged Guard Stats Analysis
**Task**: P4-TASK-003.1
**Date**: October 19, 2025
**Status**: Completed

---

## Overview

This document compares the combat statistics of Xeenaa Villager Manager's ranged guards (Marksman/Sharpshooter) with vanilla Minecraft bow-using hostile mobs (Skeleton and Pillager).

---

## Comparison Table

| Attribute | Guard (Tier 0) | Guard (Tier 4) | Skeleton | Pillager |
|-----------|---------------|---------------|----------|----------|
| **Attack Range** | 4-16 blocks | 4-16 blocks | ~16 blocks | 8 blocks (shoot)<br>16 blocks (pursue) |
| **Preferred Range** | 8-15 blocks | 8-15 blocks | Variable | 8 blocks |
| **Base Damage (Normal)** | 2.5 | 2.5 | 3-4 | 4 |
| **Max Damage (Normal)** | 2.5 | 3.75 (Precision) | 4 | 4 |
| **Attack Speed** | 30 ticks (1.5s) | 18 ticks (0.9s) | 1-3 seconds<br>(adaptive) | 60 ticks (3s) |
| **Accuracy (Normal)** | 14 (lower is better) | 6 | 6 | Unknown |
| **Bow Draw Time** | 20 ticks (1s) | 20 ticks (1s) | Instant | N/A (crossbow) |
| **Special Abilities** | None | Double Shot (20%)<br>Multishot (15%)<br>Slowing Arrow (15%)<br>Precision Shot (40%) | None | None |
| **Health** | Varies by tier | Varies by tier | 20 HP (10♥) | 24 HP (12♥) |

---

## Detailed Analysis

### 1. Attack Range

**Guard Behavior**:
- **Effective Range**: 4-16 blocks
- **Preferred Range**: 8-15 blocks (optimal positioning)
- **Danger Zone**: < 5 blocks (triggers emergency retreat)
- **Kiting Range**: < 8 blocks (backs away while shooting)

**Skeleton Behavior**:
- **Activation Range**: 16 blocks
- **Shooting Range**: Variable, can shoot from long range
- **Retreat Range**: < 4 blocks (runs away when player gets too close)

**Pillager Behavior**:
- **Activation Range**: 16 blocks (starts loading crossbow)
- **Shooting Range**: 8 blocks maximum
- **No Retreat**: Pillagers don't back away, they shoot at 8 blocks

**Comparison**:
- ✅ Guards have **better tactical positioning** than Pillagers (can shoot from farther range)
- ✅ Guards **match Skeleton range** (16 block effective range)
- ✅ Guards have **superior kiting behavior** (maintain distance, retreat when needed)

**Code Reference**: `GuardRangedAttackGoal.java:42-45` (distance constants), `GuardDirectAttackGoal.java:198-211` (kiting logic)

---

### 2. Damage Output

**Guard Damage Breakdown**:
```java
// Base damage (GuardDirectAttackGoal.java:343)
arrow.setDamage(2.0 + (difficulty * 0.5));
// Normal difficulty: 2.5 damage

// Precision Shot ability (GuardSpecialAbilities.java:324)
projectile.setDamage(projectile.getDamage() * 1.5); // +50% damage
// Precision Shot: 3.75 damage
```

**Damage Comparison (Normal Difficulty)**:

| Mob Type | Base Damage | Max Damage | Notes |
|----------|------------|------------|-------|
| Guard (Tier 0-2) | 2.5 | 2.5 | Standard arrows |
| Guard (Tier 3+) | 2.5 | 3.75 | 40% chance Precision Shot |
| Guard (Tier 4) | 2.5 | 3.75 | + special effects (slow, multishot) |
| Skeleton | 3.0 | 4.0 | Range-based variation |
| Pillager | 4.0 | 4.0 | Fixed crossbow damage |

**Analysis**:
- ❌ **Lower base damage** than both Skeleton (3-4) and Pillager (4)
- ⚠️ **Tier 3+ Precision Shot** brings max damage to 3.75 (closer to vanilla)
- ✅ **Compensated by special abilities** at Tier 4 (slowing arrows, multishot, double shot)
- ✅ **All arrows apply Slowness I** for 3 seconds (35% speed reduction)

**Code Reference**: `GuardDirectAttackGoal.java:343`, `GuardSpecialAbilities.java:324`, `GuardRangedAttackGoal.java:391-398` (slowness effect)

---

### 3. Attack Speed / Fire Rate

**Guard Attack Speed**:
```java
// Base cooldown: 30 ticks (1.5 seconds)
// Tier scaling (GuardRangedAttackGoal.java:433-435)
private int getAttackCooldown(int tier) {
    return Math.max(15, BASE_ATTACK_COOLDOWN - (tier * 3));
}
```

**Attack Speed by Tier**:
- Tier 0: 30 ticks (1.5 seconds) = 0.67 arrows/second
- Tier 1: 27 ticks (1.35 seconds) = 0.74 arrows/second
- Tier 2: 24 ticks (1.2 seconds) = 0.83 arrows/second
- Tier 3: 21 ticks (1.05 seconds) = 0.95 arrows/second
- Tier 4: 18 ticks (0.9 seconds) = 1.11 arrows/second

**Vanilla Mob Fire Rates**:
- **Skeleton**: Variable (1-3 seconds) based on distance
  - Close range: ~1 second (1.0 arrows/second)
  - Long range: ~3 seconds (0.33 arrows/second)
- **Pillager**: Fixed 3 seconds (0.33 arrows/second)

**Analysis**:
- ✅ **Tier 0 guards match Skeleton close-range** fire rate (1.5s vs 1.0s)
- ✅ **Tier 4 guards FASTER than Skeleton** at close range (0.9s vs 1.0s)
- ✅ **All tiers MUCH FASTER than Pillager** (3 seconds)
- ✅ **Consistent fire rate** regardless of distance (no slowdown at long range like Skeleton)

**Code Reference**: `GuardRangedAttackGoal.java:48,433-435`

---

### 4. Accuracy

**Guard Accuracy Formula**:
```java
// GuardRangedAttackGoal.java:401
float accuracy = Math.max(1.0f, 14 - tier * 2);
```

**Accuracy by Tier** (lower = more accurate):
- Tier 0: 14
- Tier 1: 12
- Tier 2: 10
- Tier 3: 8
- Tier 4: 6

**Vanilla Mob Accuracy** (error value):
- **Skeleton**:
  - Easy: 10
  - Normal: 6
  - Hard: 2
- **Pillager**: Unknown (likely similar to Skeleton)

**Analysis**:
- ❌ **Tier 0 guards LESS accurate** than Skeleton on Normal (14 vs 6)
- ✅ **Tier 4 guards EQUAL accuracy** to Skeleton on Normal (6)
- ⚠️ **Never reach Hard difficulty accuracy** (minimum 6, not 2)

**Code Reference**: `GuardRangedAttackGoal.java:401`

---

### 5. Special Abilities (Tier 4 Only)

**Ability Activation Chances** (GuardRangedAttackGoal.java:359-378):

| Ability | Chance | Effect |
|---------|--------|--------|
| **Double Shot** | 20% | Fires two arrows simultaneously at separate targets |
| **Multishot** | 15% | Fires three arrows in a spread pattern |
| **Slowing Arrow** | 15% | Enhanced slow effect (already applied to all arrows) |
| **Precision Shot** (Tier 3+) | 40% | 50% more damage (2.5 → 3.75) |

**Base Effect (All Tiers)**:
- All arrows apply **Slowness I** for 3 seconds (35% speed reduction)

**Vanilla Comparison**:
- Skeletons and Pillagers have **NO special abilities**
- Guards have **significant tactical advantage** at higher tiers

**Analysis**:
- ✅ **Major advantage over vanilla mobs** with special abilities
- ✅ **Slowness effect on all arrows** makes guards strong at crowd control
- ✅ **Tier 4 has 90% chance** to use some special ability (20% + 15% + 15% + 40%)
- ✅ **Multishot and Double Shot** provide AoE damage vanilla mobs cannot match

**Code Reference**: `GuardRangedAttackGoal.java:359-378`, `GuardRangedAttackGoal.java:391-398` (slowness)

---

### 6. Tactical Behavior

**Guard AI Tactics** (GuardRangedAttackGoal.java):
1. **Distance Maintenance**:
   - Emergency retreat if enemy < 5 blocks (line 213-258)
   - Kiting behavior if enemy < 8 blocks (line 264-281)
   - Advance if enemy > 15 blocks (line 237-245)

2. **High Ground Seeking**:
   - Searches for positions 2-4 blocks higher (line 286-309)
   - Validates line of sight before moving (line 314-334)
   - Repositions every 3 seconds (60 tick cooldown) (line 49)

3. **Bow Draw Animation**:
   - Full 1-second draw time like players (line 50)
   - Shows visual aiming before firing (line 116-130)
   - Cancels draw if target lost (line 172)

**Vanilla Mob Tactics**:
- **Skeleton**: Simple retreat when player < 4 blocks, chase when far
- **Pillager**: No retreat, stands and shoots, crossbow can break

**Analysis**:
- ✅ **Far superior tactical AI** compared to vanilla mobs
- ✅ **High ground seeking** gives positional advantage
- ✅ **Kiting behavior** keeps guards alive longer
- ✅ **Realistic bow mechanics** with draw animation

**Code Reference**: `GuardRangedAttackGoal.java:211-334`

---

## Balance Assessment

### Strengths (Compared to Vanilla Mobs)

1. ✅ **Superior Tactical AI**
   - Kiting, retreating, high ground seeking
   - Better survival and positioning than Skeleton/Pillager

2. ✅ **Faster Attack Speed** (Tier 4)
   - 0.9 seconds vs Skeleton 1-3s, Pillager 3s
   - Higher DPS through fire rate

3. ✅ **Special Abilities** (Tier 3+)
   - Precision Shot, Multishot, Double Shot
   - Crowd control with Slowness I on all arrows
   - No vanilla equivalent

4. ✅ **Consistent Performance**
   - Attack speed doesn't slow at long range
   - Predictable behavior patterns

### Weaknesses (Compared to Vanilla Mobs)

1. ❌ **Lower Base Damage**
   - 2.5 damage vs Skeleton 3-4, Pillager 4
   - 37.5% less damage than Pillager
   - 16-37.5% less than Skeleton

2. ❌ **Lower Starting Accuracy** (Tier 0-2)
   - Tier 0 has accuracy 14 vs Skeleton 6
   - Takes investment to reach vanilla accuracy

3. ⚠️ **Requires Tier Progression**
   - Full power only at Tier 3-4
   - Low-tier guards weaker than vanilla mobs

---

## Recommendations

### Option 1: Keep Current Balance (Recommended)
**Rationale**: Guards are intentionally **weaker early, stronger late**
- Low-tier guards (0-2): Weaker than vanilla (lower damage, lower accuracy)
- High-tier guards (3-4): Stronger than vanilla (special abilities compensate)
- **Fits RPG progression**: Guards need investment to outperform vanilla mobs

**No changes needed** - current balance encourages player investment in guards.

---

### Option 2: Buff Base Damage
**If guards feel too weak in early/mid game**:

```java
// Current (GuardDirectAttackGoal.java:343)
arrow.setDamage(2.0 + (difficulty * 0.5));

// Suggested buff
arrow.setDamage(2.5 + (difficulty * 0.5) + (tier * 0.25));
// Tier 0: 3.0 (Normal) - closer to Skeleton
// Tier 4: 4.0 (Normal) - matches Pillager
```

**Pros**: Makes early guards more competitive
**Cons**: May make high-tier guards overpowered with special abilities

---

### Option 3: Improve Starting Accuracy
**If Tier 0-2 feel too inaccurate**:

```java
// Current (GuardRangedAttackGoal.java:401)
float accuracy = Math.max(1.0f, 14 - tier * 2);

// Suggested improvement
float accuracy = Math.max(1.0f, 10 - tier * 1.5);
// Tier 0: 10 (matches Skeleton Easy)
// Tier 4: 4 (better than Skeleton Normal)
```

**Pros**: Early guards land more shots
**Cons**: May make aiming too easy across all tiers

---

### Option 4: Add Tier-Based Damage Scaling (Balanced)
**Gradual power curve**:

```java
// Suggested scaling formula
double tierBonus = tier * 0.15; // +0.15 damage per tier
arrow.setDamage((2.0 + (difficulty * 0.5)) * (1.0 + tierBonus));

// Results (Normal difficulty):
// Tier 0: 2.5 damage (current)
// Tier 1: 2.875 damage
// Tier 2: 3.25 damage
// Tier 3: 3.625 damage
// Tier 4: 4.0 damage (matches Pillager)
```

**Pros**:
- Smooth progression curve
- Tier 4 matches vanilla without being overpowered
- Special abilities remain valuable

**Cons**: None significant

---

## Conclusion

**Current Balance Summary**:
- **Tier 0-2 Guards**: Weaker than vanilla mobs (lower damage, lower accuracy)
- **Tier 3-4 Guards**: Stronger than vanilla mobs (special abilities, faster attacks)
- **Tactical AI**: Guards significantly outperform vanilla in positioning and survival

**Final Assessment**:
The current balance appears **intentionally designed** with a progression curve. Guards require **investment** (time, resources) to reach full power, which fits the mod's RPG mechanics.

**Recommended Action**:
✅ **Keep current balance** unless player feedback indicates early guards feel too weak. The special abilities at higher tiers provide sufficient power fantasy and reward for player investment.

If changes are needed, **Option 4 (Tier-Based Damage Scaling)** provides the most balanced improvement without disrupting the existing progression curve.

---

## Technical Reference

### Key Files Analyzed:
- `GuardRangedAttackGoal.java` - Main ranged combat AI
- `GuardDirectAttackGoal.java` - Simplified ranged/melee handling
- `GuardSpecialAbilities.java` - Tier 3+ special attacks
- `GuardBehaviorConfig.java` - Detection range configuration

### Stat Constants:
- Attack range: 4-16 blocks (preferred 8-15)
- Base damage: 2.0 + (difficulty * 0.5)
- Base cooldown: 30 ticks - (tier * 3)
- Accuracy: 14 - (tier * 2), minimum 1
- Bow draw time: 20 ticks (1 second)
- Slowness effect: Level I, 60 ticks (3 seconds)

### Vanilla Mob Sources:
- Minecraft Wiki (minecraft.wiki/w/Skeleton)
- Minecraft Wiki (minecraft.wiki/w/Pillager)
- Data accurate for Minecraft 1.21
