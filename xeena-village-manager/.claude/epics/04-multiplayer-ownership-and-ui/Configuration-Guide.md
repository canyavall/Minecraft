# Xeenaa Villager Manager - Configuration Guide

**Version**: 1.0.0
**Last Updated**: October 19, 2025
**Config File**: `config/xeenaa-villager-manager.json`

---

## Overview

This guide explains all configuration options for the Xeenaa Villager Manager mod. The mod uses a JSON configuration file that is automatically created on first launch.

---

## Configuration File Location

```
<minecraft_directory>/config/xeenaa-villager-manager.json
```

**Common locations**:
- **Windows**: `%APPDATA%\.minecraft\config\xeenaa-villager-manager.json`
- **Linux**: `~/.minecraft/config/xeenaa-villager-manager.json`
- **macOS**: `~/Library/Application Support/minecraft/config/xeenaa-villager-manager.json`

---

## Default Configuration

```json
{
  "blacklisted_professions": [
    "minecraft:nitwit"
  ],
  "villager_display_mode": "show_all"
}
```

---

## Configuration Settings

### 1. Blacklisted Professions

**Setting**: `blacklisted_professions`
**Type**: Array of Strings
**Default**: `["minecraft:nitwit"]`
**Restart Required**: No

**Description**:
List of profession IDs that cannot be assigned to villagers through the profession selection GUI. Blacklisted professions will not appear in the available profession list.

**Format**:
Each profession ID must follow the format: `"namespace:profession_id"`

**Examples**:
```json
{
  "blacklisted_professions": [
    "minecraft:nitwit",
    "minecraft:weaponsmith",
    "minecraft:armorer"
  ]
}
```

**Vanilla Profession IDs**:
- `minecraft:none` - Unemployed
- `minecraft:nitwit` - Nitwit
- `minecraft:armorer` - Armorer
- `minecraft:butcher` - Butcher
- `minecraft:cartographer` - Cartographer
- `minecraft:cleric` - Cleric
- `minecraft:farmer` - Farmer
- `minecraft:fisherman` - Fisherman
- `minecraft:fletcher` - Fletcher
- `minecraft:leatherworker` - Leatherworker
- `minecraft:librarian` - Librarian
- `minecraft:mason` - Mason (Stone Mason)
- `minecraft:shepherd` - Shepherd
- `minecraft:toolsmith` - Toolsmith
- `minecraft:weaponsmith` - Weaponsmith

**Mod Professions**:
- `xeenaa_villager_manager:guard` - Guard

**Use Cases**:
- Prevent assigning overpowered professions on your server
- Restrict certain professions for balance reasons
- Create themed villages with limited profession options

---

### 2. Villager Display Mode

**Setting**: `villager_display_mode`
**Type**: String (enum)
**Default**: `"show_all"`
**Restart Required**: No

**Description**:
Controls whether display names appear above villagers showing their profession/level or guard rank/tier.

**Options**:

#### `"none"` - No Display Names
- No custom names shown above any villagers
- Villagers appear as vanilla (no overhead text)
- Best for minimalist or vanilla-like gameplay

#### `"guards_only"` - Guards Only
- Only guards show rank and tier display above their heads
- Regular villagers have no custom names
- Format: **"Knight ⭐⭐⭐⭐"** (rank name + tier stars)
- Best for emphasizing guards while keeping villagers vanilla

#### `"show_all"` - Show All (Default)
- All villagers show appropriate displays:
  - **Guards**: Rank name + tier stars (e.g., "Knight ⭐⭐⭐⭐")
  - **Regular villagers**: Profession + level stars (e.g., "Librarian ★★★★")
- Unemployed and nitwit villagers never show displays
- Best for full information visibility

**Display Format Examples**:

**Guards** (when displayed):
- Tier 0 (Recruit): "Recruit" (no stars)
- Tier 1 (Sentinel): "Sentinel ⭐"
- Tier 2 (Defender): "Defender ⭐⭐"
- Tier 3 (Veteran): "Veteran ⭐⭐⭐"
- Tier 4 (Knight/Sharpshooter): "Knight ⭐⭐⭐⭐"

**Regular Villagers** (when displayed):
- Level 1 (Novice): "Farmer" (no stars)
- Level 2 (Apprentice): "Farmer ★"
- Level 3 (Journeyman): "Farmer ★★"
- Level 4 (Expert): "Farmer ★★★"
- Level 5 (Master): "Farmer ★★★★"

**Color Coding**:
- Display names are color-coded by level/tier
- Colors progress from white (low) → green → yellow → gold → teal (high)
- Tier 4 Knights/Sharpshooters have unique teal/lavender colors

**Example Configurations**:

```json
{
  "villager_display_mode": "none"
}
```

```json
{
  "villager_display_mode": "guards_only"
}
```

```json
{
  "villager_display_mode": "show_all"
}
```

---

## Guard-Specific Settings

**Important**: Guard behavior settings (modes, patrol radius, detection range) are **NOT** stored in the global config file. Instead, they are stored **per-guard** in the world save data.

**Guard Settings Stored Per-Guard**:
- Guard Mode (STAND / FOLLOW / PATROL)
- Patrol Radius (blocks)
- Detection Range (blocks)
- Follow Target Player
- Profession Lock

These settings are configured through the **Guard Management GUI** and are saved individually for each guard villager.

**Why Per-Guard?**
This allows you to have different guards with different behaviors (e.g., some guards on patrol, others following specific players).

---

## Editing the Configuration

### Method 1: Edit the JSON File Directly

1. Close Minecraft (or the server)
2. Navigate to the config file location
3. Open `xeenaa-villager-manager.json` with a text editor
4. Make your changes
5. Save the file
6. Restart Minecraft/server

**Important**: Ensure valid JSON syntax. Use a JSON validator if unsure.

### Method 2: In-Game (Future Feature)

A configuration GUI is planned for a future update to edit settings in-game.

---

## Configuration Validation

The mod automatically validates configuration on load:

- **Invalid profession IDs**: Logged as warnings, ignored
- **Invalid display mode**: Falls back to `"show_all"` (default)
- **Missing config file**: Creates default configuration automatically
- **Corrupted JSON**: Falls back to default configuration, logs error

**Check logs** at `logs/latest.log` for configuration issues.

---

## Troubleshooting

### Config Changes Not Applied

**Problem**: Changed config but settings haven't updated in-game.

**Solutions**:
1. **Restart required**: While most settings don't require restart, fully restart the game/server to ensure changes apply
2. **Check file location**: Ensure you're editing the correct config file (see locations above)
3. **Validate JSON**: Use a JSON validator to check for syntax errors
4. **Check logs**: Look for error messages in `logs/latest.log`

### Professions Still Appearing Despite Blacklist

**Problem**: Blacklisted professions still show in GUI.

**Solutions**:
1. **Check format**: Ensure profession ID follows `"namespace:id"` format
2. **Case sensitive**: IDs are case-sensitive (use lowercase)
3. **Restart GUI**: Close and reopen the profession selection screen
4. **Verify blacklist**: Check config file shows your changes

### Display Names Not Showing

**Problem**: Villager display names not appearing.

**Solutions**:
1. **Check mode**: Ensure `villager_display_mode` is set to `"show_all"` or `"guards_only"`
2. **Unemployed/nitwit**: These never show displays (by design)
3. **New villagers**: Display applies to newly spawned/loaded villagers
4. **Restart world**: Exit and reload the world

---

## Migration from Old Config Versions

If you had an older version with guard-related settings in the config file, those settings have been **removed** in version 1.0.0.

**Removed Settings** (now per-guard):
- `guard_settings.enabled`
- `guard_settings.max_guards_per_village`
- `guard_settings.guard_range`
- `guard_settings.patrol_radius`
- `guard_settings.experience_gain_rate`
- `guard_settings.default_behavior.*`

**Migration**: When you update, the mod will automatically:
1. Load your old config
2. Preserve `blacklisted_professions` and `villager_display_mode`
3. Ignore removed settings (no data loss)
4. Save a clean config file

**No action required** - migration is automatic!

---

## FAQ

### Q: Can I disable the guard profession entirely?
**A**: No, but you can add `"xeenaa_villager_manager:guard"` to `blacklisted_professions` to prevent assigning it through the GUI.

### Q: Can I change guard patrol radius globally?
**A**: No, patrol radius is set per-guard through the Guard Management GUI. This allows different guards to have different patrol areas.

### Q: Will changing the display mode affect existing villagers?
**A**: Changes apply to all villagers when chunks are loaded/reloaded. Exit and rejoin the world to see changes.

### Q: Can I have custom display names for villagers?
**A**: Not through this config. The display mode controls automatic profession/rank displays only.

### Q: How do I reset to default configuration?
**A**: Delete `config/xeenaa-villager-manager.json` and the mod will create a new default config on next launch.

---

## Advanced: API Usage

Developers can access configuration programmatically:

```java
// Get config instance
ModConfig config = ModConfig.getInstance();

// Check if profession is blacklisted
boolean isBlacklisted = config.isProfessionBlacklisted(
    Identifier.of("minecraft", "nitwit")
);

// Get display mode
VillagerDisplayMode mode = config.getVillagerDisplayMode();

// Set display mode (saves automatically)
config.setVillagerDisplayMode(VillagerDisplayMode.GUARDS_ONLY);

// Add profession to blacklist (saves automatically)
config.addBlacklistedProfession("minecraft:weaponsmith");

// Remove from blacklist (saves automatically)
config.removeBlacklistedProfession("minecraft:weaponsmith");

// Manual reload from disk
ModConfig.reload();
```

---

## Support

If you encounter issues:
1. Check this guide first
2. Review `logs/latest.log` for errors
3. Report bugs at: [GitHub Issues](https://github.com/yourusername/xeenaa-villager-manager/issues)

---

**Happy Villager Managing!**
