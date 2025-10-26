# Comprehensive Structure Mods Test List

**Purpose**: Test Xeenaa Structure Manager with a diverse set of structure-adding mods
**Target**: 15-20 mods adding 500-1000+ structures total
**Minecraft Version**: 1.21.1
**Mod Loader**: Fabric

---

## Priority 1: Large Structure Packs (Core Testing)

These mods add many structures and are essential for comprehensive testing.

### 1. When Dungeons Arise
**Structures Added**: ~50-60 large structures (castles, towers, keeps, fortresses)
**Download**: https://modrinth.com/mod/when-dungeons-arise
**Why**: Most popular structure mod, huge variety, excellent for testing LARGE + TYPE classification
**Expected Classification**: LARGE, DUNGEON/RUIN/SKY

### 2. YUNG's Better Mineshafts
**Structures Added**: Multiple mineshaft variants
**Download**: https://modrinth.com/mod/yungs-better-mineshafts
**Why**: Tests MINESHAFT type classification (critical edge case - must stay 1.0x)
**Expected Classification**: LARGE, MINESHAFT

### 3. YUNG's Better Strongholds
**Structures Added**: Enhanced stronghold structures
**Download**: https://modrinth.com/mod/yungs-better-strongholds
**Why**: Tests stronghold classification, concentric ring placement
**Expected Classification**: LARGE, DUNGEON

### 4. Repurposed Structures
**Structures Added**: 25+ structure variants
**Download**: https://modrinth.com/mod/repurposed-structures-fabric
**Why**: Repurposes vanilla structures, good for vanilla compatibility testing
**Expected Classification**: Mix of SMALL/MEDIUM/LARGE, various types

### 5. ChoiceTheorem's Overhauled Village
**Structures Added**: 30+ village variants for all biomes
**Download**: https://modrinth.com/mod/ct-overhaul-village
**Why**: Tests TOWN classification extensively, critical for 2.0x multiplier verification
**Expected Classification**: LARGE, TOWN

---

## Priority 2: Medium Structure Packs (Diversity Testing)

### 6. Dungeons and Taverns
**Structures Added**: 20+ dungeons and taverns
**Download**: https://modrinth.com/mod/dungeons-and-taverns
**Why**: Mix of SMALL (taverns) and MEDIUM (dungeons), good for size classification
**Expected Classification**: SMALL/MEDIUM, DUNGEON/TOWN

### 7. Sky Villages
**Structures Added**: Villages in the sky
**Download**: https://modrinth.com/mod/sky-villages
**Why**: Tests SKY type classification
**Expected Classification**: LARGE, SKY

### 8. Underground Villages
**Structures Added**: Villages underground
**Download**: https://modrinth.com/mod/underground-villages
**Why**: Tests underground detection, village classification
**Expected Classification**: LARGE, TOWN (underground=true)

### 9. Structory
**Structures Added**: 15-20 decorative structures
**Download**: https://modrinth.com/mod/structory
**Why**: Mix of small decorative structures, tests SMALL classification
**Expected Classification**: SMALL/MEDIUM, RUIN

### 10. Tidal Towns
**Structures Added**: Ocean villages
**Download**: https://modrinth.com/mod/tidal-towns
**Why**: Tests water-based structure classification
**Expected Classification**: LARGE, TOWN

---

## Priority 3: Specialized Structure Packs (Edge Cases)

### 11. Additional Structures
**Structures Added**: 10+ small structures
**Download**: https://modrinth.com/mod/additional-structures
**Why**: Small shrines, camps, ruins - tests SMALL + RUIN classification
**Expected Classification**: SMALL, RUIN

### 12. Formations Overworld
**Structures Added**: Natural formations (not traditional structures)
**Download**: https://modrinth.com/mod/formations
**Why**: Tests edge case - natural formations vs structures
**Expected Classification**: SMALL/MEDIUM, UNKNOWN

### 13. Villages and Pillages
**Structures Added**: Enhanced pillager outposts
**Download**: https://modrinth.com/mod/villages-and-pillages
**Why**: Tests DUNGEON classification for hostile structures
**Expected Classification**: MEDIUM, DUNGEON

### 14. Graveyard Mod
**Structures Added**: Graveyards and crypts
**Download**: https://modrinth.com/mod/the-graveyard-fabric
**Why**: Tests TEMPLE/DUNGEON classification for graveyards
**Expected Classification**: MEDIUM, TEMPLE/DUNGEON (underground variants)

---

## Priority 4: Biome Mods with Structures (Optional)

These add structures as part of biome generation.

### 15. Regions Unexplored
**Structures Added**: Various biome-specific structures
**Download**: https://modrinth.com/mod/regions-unexplored
**Why**: Tests compatibility with biome mods, various structure types
**Expected Classification**: Mix of all types

### 16. Terralith (Optional)
**Structures Added**: Some structures with biomes
**Download**: https://modrinth.com/mod/terralith
**Why**: Popular biome mod, tests biome compatibility
**Note**: May add a lot of worldgen changes, use cautiously

---

## Alternative/Backup Mods

If any of the above are unavailable or incompatible:

### Backup Options
- **Awesome Dungeons**: https://modrinth.com/mod/awesome-dungeon
- **Dungeon Now Loading**: https://modrinth.com/mod/dungeonnowloading
- **Philip's Ruins**: https://modrinth.com/mod/philips-ruins
- **Towers of the Wild Reworked**: https://modrinth.com/mod/towers-of-the-wild-reworked
- **MES - Moog's End Structures**: https://modrinth.com/mod/mes-moogs-end-structures (Nether/End - skip for Epic 01)

---

## Required Dependencies

Some structure mods require additional libraries. Check each mod's page for dependencies.

**Common Dependencies**:
- **Fabric API**: https://modrinth.com/mod/fabric-api (Required - you already have this)
- **YUNG's API**: https://modrinth.com/mod/yungs-api (Required for YUNG's mods)
- **Cloth Config API**: https://modrinth.com/mod/cloth-config (Required by some mods)
- **Collective**: https://modrinth.com/mod/collective (Required by some mods)

---

## Installation Instructions

### Using Modrinth Launcher (Recommended)
1. Create new Fabric 1.21.1 instance in Modrinth launcher
2. Click "Add content" → Search for each mod
3. Install directly from launcher
4. Dependencies auto-install

### Manual Installation
1. Download `.jar` files from Modrinth links above
2. Place in `.minecraft/mods/` folder
3. Download any required dependencies
4. Ensure all mods are for Minecraft 1.21.1 + Fabric

### Testing Instance Setup
```
.minecraft/
├── mods/
│   ├── fabric-api-0.110.0+1.21.1.jar
│   ├── xeenaa-structure-manager-1.0.0.jar  (your mod)
│   ├── when-dungeons-arise-2.1.5.jar
│   ├── yungs-better-mineshafts-4.0.4.jar
│   ├── yungs-api-4.0.6.jar
│   ├── repurposed-structures-7.1.15.jar
│   ├── ct-overhaul-village-3.4.6.jar
│   └── ... (other structure mods)
└── config/
    └── xeenaa-structure-manager.toml  (auto-generated)
```

---

## Expected Testing Results

### Structure Discovery
```
Expected total: 500-1000 structures
Expected mods: 15-20 namespaces
Expected breakdown:
- minecraft: ~30 structures
- dungeons_arise: ~50 structures
- bettermineshafts: ~10 structures
- repurposed_structures: ~25 structures
- ...
```

### Classification Breakdown
```
Expected SIZE distribution:
- SMALL: 20-30% (shrines, camps, small ruins)
- MEDIUM: 40-50% (dungeons, temples, outposts)
- LARGE: 20-30% (villages, castles, mansions)

Expected TYPE distribution:
- TOWN: ~15-20% (villages, towns)
- DUNGEON: ~30-40% (dungeons, keeps, fortresses)
- TEMPLE: ~10-15% (temples, shrines)
- MINESHAFT: ~5% (mineshaft variants)
- SKY: ~5% (sky villages, floating structures)
- RUIN: ~10-15% (ruins, camps, small structures)
- UNKNOWN: ~15-20% (unclassified structures)
```

### Multiplier Verification
```
Expected multipliers applied:
- Villages (TOWN): 2.0x spacing
- Dungeons (DUNGEON): 1.8x spacing
- Mineshafts (MINESHAFT): 1.0x (NOT MODIFIED - critical!)
- Large structures (SIZE=LARGE): 2.5x spacing (if no type override)
- Small structures (SIZE=SMALL): 1.2x spacing
```

### Performance Targets
```
Classification time: <5 seconds for 1000 structures
Memory usage: <10 MB for cache
Structures/second: >150-200 structures/sec
```

---

## Testing Phases

### Phase 1: Core Testing (5-7 mods)
Install mods 1-7 from Priority 1 & 2
**Goal**: Verify basic functionality, classification, multipliers
**Time**: 30 minutes

### Phase 2: Comprehensive Testing (15-20 mods)
Install all Priority 1, 2, 3 mods
**Goal**: Stress test with many structures, verify edge cases
**Time**: 1 hour

### Phase 3: Performance Testing
Same as Phase 2, but focus on:
- Classification timing
- Memory usage
- World generation lag
- Spark profiler integration
**Time**: 1-2 hours

---

## Verification Checklist

After installing mods and creating a world, verify:

### Console Logs
- [ ] All mods' structures discovered
- [ ] Structure breakdown shows correct counts
- [ ] Classification completes in <5s
- [ ] Memory usage <10 MB
- [ ] Mineshafts show 1.0x multiplier (not modified)
- [ ] Villages show 2.0x multiplier
- [ ] Dungeons show 1.8x multiplier
- [ ] No ERROR messages
- [ ] No WARN messages about classification failures

### In-Game Verification
- [ ] World generates successfully
- [ ] Structures spawn (check with /locate)
- [ ] Villages feel further apart than vanilla
- [ ] Mineshafts still common (1.0x working)
- [ ] Large dungeons are rarer (2.0x-2.5x working)
- [ ] No crashes or lag

### Configuration
- [ ] Config file auto-generated at `config/xeenaa-structure-manager.toml`
- [ ] Can edit config and reload world
- [ ] Preset changes work (try "sparse" preset)
- [ ] Structure overrides work (disable a structure)

---

## Troubleshooting

### "No structures discovered from mod X"
- Check mod is for 1.21.1
- Check mod is Fabric (not Forge)
- Check dependencies are installed
- Check mod actually adds structures (some are biome-only)

### "Classification failed for structure X"
- Check console for WARN messages
- Structure may lack NBT template
- May be datapack-only structure
- Check structure-classification skill for edge cases

### "Multiplier not applied to structure X"
- Check if structure has classification (DEBUG log)
- Check if multiplier is 1.0x (no change needed)
- Check config file has correct multipliers
- Check priority system (type overrides size)

### Performance Issues
- Too many structure mods (>30)
- Reduce to 15-20 for testing
- Check Spark profiler for bottlenecks
- Review parallel processing logs

---

## Next Steps After Testing

### If Everything Works
1. Document successful test results
2. Take screenshots of console output
3. Record video of structures spawning
4. Create Epic 02 for structure placement tracking
5. Test with 50+ structure mods (stress test)

### If Issues Found
1. Document errors in `.claude/temp/test-results.md`
2. Use `/research` or `/fix` to address issues
3. Add edge case handling if needed
4. Re-test after fixes

---

## Download Script (PowerShell)

```powershell
# Create mods folder
New-Item -ItemType Directory -Force -Path ".\test-instance\mods"

# Download using Modrinth API or manual download
# (User would need to download manually from links above)

Write-Host "Please download mods from Modrinth links above"
Write-Host "Place all .jar files in: .\test-instance\mods\"
```

---

## Summary

**Recommended Mod Count**: 15-20 mods
**Expected Structures**: 500-1000 total
**Testing Time**: 2-3 hours (installation + testing + verification)
**Performance Target**: All PASS (classification <5s, memory <10MB)

**Key Mods for Testing**:
1. When Dungeons Arise (LARGE + DUNGEON)
2. YUNG's Better Mineshafts (MINESHAFT edge case)
3. ChoiceTheorem's Overhauled Village (TOWN classification)
4. Repurposed Structures (variety testing)
5. Sky Villages (SKY type testing)

Start with these 5 mods, then expand to the full list.
