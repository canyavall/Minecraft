# Quick Start Guide

**Xeenaa's Alex's Mobs (Fabric) - Fast Setup and Testing**

Get up and running with the framework in under 10 minutes.

---

## Prerequisites

Before you start, ensure you have:

- **Java 21** (JDK or JRE)
- **Git** (for cloning the repository)
- **4+ GB RAM** available for Minecraft
- **2+ GB disk space** for project and dependencies

**Check Java version**:
```bash
java -version
```
Expected output: `java version "21.x.x"`

---

## Step 1: Get the Project

**Clone the repository** (or navigate to existing project):
```bash
cd C:/Users/canya/Documents/projects/Minecraft/xeenaa-alexs-mobs
```

**Verify project structure**:
```bash
ls
```
Expected files: `build.gradle`, `gradle/`, `src/`, `docs/`

---

## Step 2: Build the Project

**Run the build**:
```bash
./gradlew build
```

**Wait for completion** (first build takes 1-3 minutes):
- Downloads dependencies (GeckoLib, Fabric API)
- Compiles Java code
- Generates resources
- Creates mod JAR file

**Success indicator**:
```
BUILD SUCCESSFUL in 2m 15s
```

**If build fails**, check:
- Java 21 is installed
- Internet connection (downloads dependencies)
- No syntax errors in code

---

## Step 3: Launch the Client

**Run Minecraft with the mod**:
```bash
./gradlew runClient
```

**Wait for launch** (30-120 seconds depending on hardware):
- Minecraft window opens
- Game loads with mod

**Success indicator**:
- Minecraft main menu appears
- No crash on startup

**If launch fails**, check:
- Sufficient RAM allocated (4+ GB)
- No conflicting mods
- Console logs for errors

---

## Step 4: Create Test World

**From main menu**:
1. Click "Singleplayer"
2. Click "Create New World"
3. Configure world:
   - **World Name**: "Alex's Mobs Test"
   - **Game Mode**: Creative
   - **Allow Cheats**: ON
   - **Difficulty**: Peaceful (or Easy)
4. Click "Create New World"

**Wait for world generation** (10-30 seconds)

**Success indicator**:
- World loads successfully
- Player spawns in world
- No crashes

---

## Step 5: Test the Test Entity

**Summon the test entity**:
1. Press `T` to open chat
2. Type: `/summon xeenaa-alexs-mobs:test_animal`
3. Press `Enter`

**Expected result**:
- Entity spawns in front of player
- Entity is visible (green cube or model)
- Entity moves and looks around

**Alternative method (spawn egg)**:
1. Press `E` to open creative inventory
2. Search: "test animal"
3. Find "Test Animal Spawn Egg"
4. Right-click on ground to spawn

---

## Step 6: Verify Framework Features

### Rendering
- **Look at entity**: Should see model and texture
- **Walk around entity**: Visible from all angles
- **Press F3+B**: Shows hitbox (white outline)

### Animations
- **Stand still**: Entity plays idle animation
- **Walk away**: Entity walks, plays walk animation

### AI Behavior
- **Wait 10 seconds**: Entity should wander around
- **Get close**: Entity should look at player

### Sounds
- **Wait for ambient sound**: Will be silent (no .ogg files yet)
- **Attack entity**: Will be silent (expected)
- **Kill entity**: Will be silent (expected)

**Note**: Missing sound warnings in console are EXPECTED and OK.

### Loot
- **Kill entity**: Drops 1-2 leather
- **Pick up loot**: Adds to inventory

---

## Step 7: Check Console Logs

**Close Minecraft**

**Open logs**:
```bash
cat logs/latest.log
```

**Search for errors**:
```bash
grep -i "error" logs/latest.log
```

**Expected warnings** (these are OK):
```
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.ambient
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.hurt
[WARN] Missing sound for event: xeenaa-alexs-mobs:entity.test_animal.death
```

**Unexpected errors** (these are NOT OK):
- [ERROR] messages
- Stack traces
- "Failed to register" errors

---

## Quick Test Checklist

Verify these work:

- [x] Project builds successfully
- [x] Game launches without crashes
- [x] Test entity summons (`/summon xeenaa-alexs-mobs:test_animal`)
- [x] Entity is visible (not invisible)
- [x] Entity has texture (not pink/black checkerboard)
- [x] Entity animates (idle and walk)
- [x] Entity has AI (wanders, looks at player)
- [x] Spawn egg works (creative inventory)
- [x] Entity drops loot when killed (1-2 leather)
- [x] F3+B shows hitbox
- [x] No critical errors in console

**All checked?** Framework is working! ✅

**Some unchecked?** See [Troubleshooting](#troubleshooting) below.

---

## Troubleshooting

### Build Fails

**Problem**: `./gradlew build` produces errors

**Solution**:
```bash
# Clean and rebuild
./gradlew clean build --stacktrace

# Check Java version
java -version  # Must be 21.x.x

# Clear Gradle cache if needed
rm -rf ~/.gradle/caches
./gradlew build
```

### Game Won't Launch

**Problem**: `./gradlew runClient` fails or crashes

**Solution**:
```bash
# Check logs
cat logs/latest.log

# Increase RAM allocation (in gradle.properties or run config)
-Xmx4G

# Try clean launch
./gradlew clean runClient
```

### Entity Won't Summon

**Problem**: `/summon xeenaa-alexs-mobs:test_animal` shows "Unknown entity"

**Solution**:
- Check mod loaded (Mods menu)
- Check console logs for registration errors
- Verify `ModEntities.initialize()` called
- Try tab-completion: `/summon xeenaa-alexs-mobs:` (should show test_animal)

### Entity is Invisible

**Problem**: Entity summons but is not visible

**Solution**:
- Check renderer registered (`AlexsMobsClient.java`)
- Verify `.geo.json` file exists
- Check console for rendering errors
- Try F3+A to reload chunks

### Entity Has No Texture (Pink/Black)

**Problem**: Entity visible but shows pink/black checkerboard

**Solution**:
- Verify texture file exists: `src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/test_animal.png`
- Check file extension is `.png` (not `.jpg`)
- Try F3+T to reload resources

### Animations Don't Play (T-Pose)

**Problem**: Entity renders but doesn't animate

**Solution**:
- Verify `.animation.json` file exists
- Check animation names match in entity class
- Verify `GeoEntity` interface implemented
- Check console for GeckoLib errors

### No Loot Drops

**Problem**: Entity dies but drops nothing

**Solution**:
- Verify loot table exists: `src/main/resources/data/xeenaa-alexs-mobs/loot_table/entities/test_animal.json`
- Check JSON is valid: `jq . [loot_table_file]`
- Verify file name matches entity name

---

## Next Steps

### After Basic Testing

**Full Testing**: See `TESTING_CHECKLIST.md` for comprehensive manual tests

**Framework Usage**: See `FRAMEWORK_USAGE.md` for guide to adding new entities

**Performance Testing**: After manual testing, proceed to TASK-012 for performance validation

### Adding Your First Animal

Ready to add a real animal mob? Follow these steps:

1. **Read FRAMEWORK_USAGE.md** - Complete guide to adding entities
2. **Choose an animal** - Start simple (passive, no complex behaviors)
3. **Create entity class** - Extend `BaseAnimalEntity`, implement `GeoEntity`
4. **Create model files** - Use Blockbench to create `.geo.json` and animations
5. **Add textures** - Create entity texture (64×64 or 128×128)
6. **Register entity** - Add to `ModEntities.java`
7. **Create renderer** - Add client-side rendering
8. **Test** - Follow TESTING_CHECKLIST.md

### Recommended First Animals

Easy to implement (good starting points):
- **Passive land animals**: Crow, Roadrunner, Terrapin
- **Simple behaviors**: Wander, flee from player, basic AI
- **No complex mechanics**: No special abilities or interactions

Avoid for first implementation:
- Flying animals (complex movement AI)
- Water animals (swimming mechanics)
- Hostile animals (combat AI)
- Animals with special abilities

---

## Common Commands

### Build Commands
```bash
# Full build
./gradlew build

# Clean build (removes old files)
./gradlew clean build

# Fast build (skip tests)
./gradlew build -x test
```

### Run Commands
```bash
# Launch client
./gradlew runClient

# Launch dedicated server
./gradlew runServer

# Generate Minecraft sources (for IDE)
./gradlew genSources
```

### In-Game Commands
```bash
# Summon test entity
/summon xeenaa-alexs-mobs:test_animal

# Teleport entity to player
/tp @e[type=xeenaa-alexs-mobs:test_animal,limit=1] @p

# Kill all test entities
/kill @e[type=xeenaa-alexs-mobs:test_animal]

# Change gamemode
/gamemode creative
/gamemode survival
```

### Debug Commands
```bash
# Show hitboxes
F3 + B

# Reload resources
F3 + T

# Show debug screen
F3

# Toggle fullscreen
F11
```

---

## Project Structure Quick Reference

```
xeenaa-alexs-mobs/
├── build.gradle                   # Gradle build configuration
├── src/
│   ├── main/
│   │   ├── java/com/canya/xeenaa_alexs_mobs/
│   │   │   ├── AlexsMobsMod.java  # Main mod initializer
│   │   │   ├── entity/
│   │   │   │   ├── BaseAnimalEntity.java      # Base class for animals
│   │   │   │   └── animal/
│   │   │   │       └── TestAnimalEntity.java  # Test entity
│   │   │   ├── client/
│   │   │   │   ├── AlexsMobsClient.java       # Client initializer
│   │   │   │   ├── model/
│   │   │   │   │   └── TestAnimalModel.java   # GeckoLib model
│   │   │   │   └── renderer/
│   │   │   │       └── TestAnimalRenderer.java # GeckoLib renderer
│   │   │   └── registry/
│   │   │       ├── ModEntities.java   # Entity registration
│   │   │       ├── ModItems.java      # Item registration
│   │   │       └── ModSounds.java     # Sound registration
│   │   └── resources/
│   │       ├── assets/xeenaa-alexs-mobs/
│   │       │   ├── geo/               # Model files (.geo.json)
│   │       │   ├── animations/        # Animation files (.animation.json)
│   │       │   ├── textures/entity/   # Entity textures (.png)
│   │       │   ├── sounds.json        # Sound definitions
│   │       │   └── lang/en_us.json    # Translations
│   │       └── data/xeenaa-alexs-mobs/
│   │           └── loot_table/entities/ # Entity loot tables
│   └── client/
│       └── [client-only code]
├── docs/
│   ├── QUICKSTART.md              # This file
│   ├── FRAMEWORK_USAGE.md         # Complete framework guide
│   └── TESTING_CHECKLIST.md       # Manual testing procedures
└── logs/
    └── latest.log                 # Game console logs
```

---

## Resource Locations Quick Reference

### Models (GeckoLib Geometry)
```
src/main/resources/assets/xeenaa-alexs-mobs/geo/[entity_name].geo.json
```

### Animations (GeckoLib Animations)
```
src/main/resources/assets/xeenaa-alexs-mobs/animations/[entity_name].animation.json
```

### Entity Textures
```
src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/[entity_name].png
```

### Sound Files
```
src/main/resources/assets/xeenaa-alexs-mobs/sounds/entity/[entity_name]/[sound_type].ogg
```

### Loot Tables
```
src/main/resources/data/xeenaa-alexs-mobs/loot_table/entities/[entity_name].json
```

---

## Useful Links

### Project Documentation
- **FRAMEWORK_USAGE.md**: Complete guide to adding entities
- **TESTING_CHECKLIST.md**: Manual testing procedures
- **project.md**: Project vision and technical stack
- **requirements.md**: Epic 01 business requirements

### External Resources
- **GeckoLib Wiki**: https://github.com/bernie-g/geckolib/wiki
- **Fabric Wiki**: https://fabricmc.net/wiki/
- **Blockbench**: https://www.blockbench.net/ (model editor)
- **Minecraft Wiki**: https://minecraft.wiki/

---

## Support

### If You Get Stuck

1. **Check logs**: `logs/latest.log` contains detailed error information
2. **Review documentation**: FRAMEWORK_USAGE.md has troubleshooting section
3. **Compare with working code**: TestAnimalEntity is a complete working example
4. **Search console errors**: Use `grep` to find specific error messages

### Common Questions

**Q: Why are sounds silent?**
A: Sound `.ogg` files are not yet implemented. Missing sound warnings are expected and OK.

**Q: Why is the test entity a simple cube?**
A: It's a minimal test model. Real animals will have detailed Blockbench models.

**Q: Can I edit the test entity?**
A: Yes! Use it as a template for learning. Create new entities instead of modifying it.

**Q: How do I add my own entity?**
A: Follow FRAMEWORK_USAGE.md - complete step-by-step guide included.

**Q: Why does build take so long the first time?**
A: First build downloads dependencies (GeckoLib, Fabric API). Subsequent builds are faster.

---

## Summary

**Minimum viable test** (5 minutes):
1. `./gradlew build` - Build succeeds ✅
2. `./gradlew runClient` - Game launches ✅
3. `/summon xeenaa-alexs-mobs:test_animal` - Entity spawns ✅
4. Entity is visible and animates ✅

**Framework working?** YES → Proceed to full testing (TESTING_CHECKLIST.md)

**Framework broken?** See [Troubleshooting](#troubleshooting) above

---

**Quick Start Version**: Epic 01 - TASK-011
**Last Updated**: 2025-10-26
**Framework Version**: 0.1.0

**Ready to dive deeper?** Read FRAMEWORK_USAGE.md next!
