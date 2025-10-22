# Passive Guard Health Regeneration - Testing Guide

## Enhanced Logging Added

The regeneration system now has **very visible INFO-level logs** that will show up clearly in the game console/log files. All logs are prefixed with `[PASSIVE REGEN]` for easy filtering.

## What the Logs Will Show

### 1. Combat State Changes
```
[PASSIVE REGEN] Guard <uuid> entered COMBAT (has target: Zombie)
[PASSIVE REGEN] Guard <uuid> entered COMBAT (taking damage) | Health: 12.5/40.0
```

### 2. Out-of-Combat Countdown (every 10 seconds)
```
[PASSIVE REGEN] ⏱ Guard <uuid> out of combat for 10 seconds (need 50 more seconds) | Health: 12.5/40.0
[PASSIVE REGEN] ⏱ Guard <uuid> out of combat for 20 seconds (need 40 more seconds) | Health: 12.5/40.0
[PASSIVE REGEN] ⏱ Guard <uuid> out of combat for 30 seconds (need 30 more seconds) | Health: 12.5/40.0
```

### 3. Regeneration Start
```
[PASSIVE REGEN] ✓ Guard <uuid> STARTED REGENERATING | Health: 12.5/40.0 | Out of combat for: 60 seconds
```

### 4. Each Healing Tick (every 4 seconds)
```
[PASSIVE REGEN] ❤ Guard <uuid> healed +0.50 HP (Tier 0) | Health: 12.5/40.0 -> 13.0/40.0
[PASSIVE REGEN] ❤ Guard <uuid> healed +0.50 HP (Tier 0) | Health: 13.0/40.0 -> 13.5/40.0
[PASSIVE REGEN] ❤ Guard <uuid> healed +0.50 HP (Tier 0) | Health: 13.5/40.0 -> 14.0/40.0
```

### 5. Combat Interruption
```
[PASSIVE REGEN] ✗ Guard <uuid> STOPPED REGENERATING | Final Health: 15.0/40.0
[PASSIVE REGEN] Guard <uuid> entered COMBAT (has target: Spider)
```

### 6. Reaching Full Health
```
[PASSIVE REGEN] ★ Guard <uuid> reached FULL HEALTH via passive regeneration (Tier 0)
```

## Testing Steps

### Step 1: Launch the Game
```bash
cd xeena-village-manager
./gradlew runClient
```

### Step 2: Create a Guard
1. Spawn a villager
2. Open management GUI (Shift + Right-Click)
3. Change profession to Guard
4. (Optional) Promote to a higher tier to see faster regeneration

### Step 3: Damage the Guard
- Use a sword to hit the guard (reduce health to about 50%)
- OR spawn a zombie and let it attack the guard
- Watch for the combat log: `[PASSIVE REGEN] Guard <uuid> entered COMBAT`

### Step 4: Move to Safe Area
- Move the guard away from any threats
- Ensure no hostile mobs are nearby (detection range is 20 blocks)

### Step 5: Wait and Watch Logs
You'll see countdown logs every 10 seconds:
- 10s: "need 50 more seconds"
- 20s: "need 40 more seconds"
- 30s: "need 30 more seconds"
- 40s: "need 20 more seconds"
- 50s: "need 10 more seconds"
- 60s: "STARTED REGENERATING"

### Step 6: Observe Regeneration
Every 4 seconds you'll see:
```
[PASSIVE REGEN] ❤ Guard healed +X.XX HP
```

### Step 7: Test Combat Interruption
While guard is regenerating:
- Attack the guard OR
- Spawn a hostile mob nearby

You should see:
```
[PASSIVE REGEN] ✗ Guard STOPPED REGENERATING
[PASSIVE REGEN] Guard entered COMBAT
```

## Expected Regeneration Rates

| Tier | Rank | HP per 4 seconds | HP per second |
|------|------|------------------|---------------|
| 0 | Recruit | 0.50 HP | 0.125 HP/s |
| 1 | Guard/Marksman | 0.75 HP | 0.1875 HP/s |
| 2 | Sergeant | 1.00 HP | 0.25 HP/s |
| 3 | Lieutenant | 1.25 HP | 0.3125 HP/s |
| 4 | Captain/Master | 1.50 HP | 0.375 HP/s |

## Viewing Health Bar in Game

To see the guard's health visually:
1. **F3 + B** - Shows entity hitboxes (you can see if guard is taking damage)
2. **Damage numbers** - Hit the guard and watch the health decrease
3. **Look for the logs** - The logs show exact health values

## Where to Find Logs

**In-Game Console:**
- Press `/` to open chat
- Logs appear in the game's console window

**Log File:**
- `xeena-village-manager/run/logs/latest.log`
- Search for `[PASSIVE REGEN]` to filter relevant logs

## Quick Test Checklist

- [ ] Guard enters combat when attacked (log appears)
- [ ] Countdown shows every 10 seconds when guard is injured and out of combat
- [ ] Regeneration starts after exactly 60 seconds
- [ ] Healing ticks occur every 4 seconds
- [ ] Heal amount matches tier (Tier 0 = 0.50 HP, Tier 2 = 1.00 HP, Tier 4 = 1.50 HP)
- [ ] Regeneration stops immediately when guard enters combat
- [ ] Regeneration reaches full health (full health log appears)
- [ ] Guard can regenerate in all modes (STAND, FOLLOW, PATROL)

## Troubleshooting

**No logs appearing?**
- Make sure you're using a villager with Guard profession
- Check that guard is actually damaged (health < max)
- Verify no hostile mobs within 20 blocks

**Regeneration not starting?**
- Wait the full 60 seconds
- Check logs for countdown messages
- Ensure guard has no active target

**Logs hard to read?**
- Open `run/logs/latest.log` in a text editor
- Search for `[PASSIVE REGEN]`
- All regeneration logs are prefixed for easy filtering
