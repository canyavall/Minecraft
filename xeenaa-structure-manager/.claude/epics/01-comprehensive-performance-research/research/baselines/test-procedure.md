# Reproducible Performance Test Procedure

**Epic**: 01-comprehensive-performance-research
**Task**: TASK-008 - Document Reproducible Test Procedure
**Status**: COMPLETED
**Version**: 1.0
**Date**: 2025-10-25

---

## Executive Summary

This document provides a complete, reproducible procedure for conducting performance tests on the Xeenaa Structure Manager mod. The procedure enables anyone to replicate the baseline measurements established in TASK-007, allowing Epic 02+ to measure optimization effectiveness against consistent baselines.

**Key Principle**: Every configuration value, test step, and measurement method is specified precisely to ensure reproducible results across different testers and test runs.

**Target Audiences**:
- Epic 02+ implementers validating optimizations
- QA testers verifying performance improvements
- Future contributors reproducing baseline measurements
- External researchers validating findings

---

## Table of Contents

1. [Test Environment Setup](#1-test-environment-setup)
2. [World Configuration](#2-world-configuration)
3. [Profiling Setup](#3-profiling-setup)
4. [Test Execution](#4-test-execution)
5. [Data Collection](#5-data-collection)
6. [Analysis and Metrics](#6-analysis-and-metrics)
7. [Validation and Troubleshooting](#7-validation-and-troubleshooting)
8. [Test Scenarios](#8-test-scenarios)

---

## 1. Test Environment Setup

### 1.1 Exact Software Versions

**Minecraft**:
- Version: `1.21.1` (exact version required)
- Launcher: Any (official launcher, MultiMC, Prism Launcher, etc.)

**Fabric Loader**:
- Version: `0.16.7` (exact version)
- Install via: Fabric Installer or launcher mod manager

**Fabric API**:
- Version: `0.116.5+1.21.1` (exact version)
- Download from: Modrinth or CurseForge

**Java**:
- Version: `Java 21.0.8` or higher (21.x required)
- Vendor: Oracle JDK, OpenJDK, or Adoptium
- Verify with: `java -version`

**Operating System**:
- Any (Windows, macOS, Linux)
- 64-bit required
- Note: Performance may vary by OS, document which OS was used

### 1.2 JVM Configuration

**Memory Allocation** (EXACT VALUES):
```
-Xms6G -Xmx6G
```

**Explanation**:
- `-Xms6G`: Initial heap size = 6 GB (6,216 MB allocated heap shown in tests)
- `-Xmx6G`: Maximum heap size = 6 GB (prevents dynamic heap resizing)
- Use matching values to eliminate heap expansion overhead

**Additional JVM Flags** (RECOMMENDED):
```
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=32M
```

**Explanation**:
- `-XX:+UseG1GC`: Use G1 garbage collector (default in modern Java)
- `-XX:MaxGCPauseMillis=200`: Target GC pause time = 200ms
- `-XX:G1HeapRegionSize=32M`: Optimize region size for 6GB heap

**How to Set JVM Flags**:
- **Official Launcher**: Edit installation → More Options → JVM Arguments
- **MultiMC/Prism**: Instance Settings → Java → JVM Arguments
- **Command Line**: `java [FLAGS] -jar fabric-server-launch.jar`

### 1.3 Mod Installation

**Required Mods** (EXACT LIST):

**Core Infrastructure**:
1. **Fabric API** - `fabric-api-0.116.5+1.21.1.jar`
2. **Xeenaa Structure Manager** - `xeenaa-structure-manager-0.1.0.jar` (test subject)

**Profiling Tools** (for profiled tests):
3. **Spark** - Latest for Minecraft 1.21.1 (CPU profiling)
   - Download from: https://spark.lucko.me/download
   - Version: `fabric-1.10.119+1.21.jar` or higher

**Structure Mods** (HEAVY MODDING SCENARIO ONLY):

4. **additionalstructures** - 198 structures (35%)
5. **mvs** (More Vanilla Structures) - 129 structures (23%)
6. **repurposed_structures** - 107 structures (19%)
7. **dungeons_arise** (When Dungeons Arise) - 39 structures (7%)
8. **mss** (More Simple Structures) - 35 structures (6%)
9. **minecraft** (vanilla) - 34 structures (6%)
10. **underground_bunkers** - Bunkers (variable)
11. **betterdungeons** - Dungeon structures (variable)
12. **Additional mods** - 27 structures (5%)

**Total Structures**: 569 structures registered

**Important**: For vanilla baseline, use NO structure mods (only Fabric API).

**Mod Installation Path**:
- Place all `.jar` files in: `.minecraft/mods/` directory
- Verify all mods load successfully on startup

### 1.4 Hardware Requirements

**Minimum Specifications**:
- CPU: Quad-core processor (4 threads minimum)
- RAM: 8 GB system RAM (6 GB allocated to Minecraft)
- Storage: SSD recommended (faster chunk loading)
- GPU: Integrated graphics sufficient (not GPU-bound)

**Recommended Specifications**:
- CPU: 6-8 core processor (multi-threaded worldgen benefits)
- RAM: 16 GB system RAM (allows 6 GB + OS + background apps)
- Storage: NVMe SSD (minimal I/O wait time)
- GPU: Dedicated GPU (smoother rendering during tests)

**Document Your Hardware**:
Record these values for each test run:
- CPU model: `[e.g., Intel i7-10700K, AMD Ryzen 5 5600X]`
- RAM amount: `[e.g., 16 GB DDR4-3200]`
- Storage type: `[e.g., Samsung 970 EVO NVMe SSD]`
- OS: `[e.g., Windows 11 23H2, Ubuntu 22.04]`

---

## 2. World Configuration

### 2.1 World Creation Settings

**World Type**: `Default` (not Superflat, Large Biomes, Amplified, etc.)

**Seed** (EXACT VALUE):
```
Seed: -1234567890
```

**Explanation**:
- Fixed seed ensures identical terrain generation
- Same biome distribution across test runs
- Reproducible structure placement locations
- Use this EXACT seed for all tests

**Game Mode**: `Creative` (for flight and no mob interference)

**Difficulty**: `Peaceful` (eliminate mob spawning overhead)

**Generate Structures**: `ON` (REQUIRED - this is what we're testing!)

**Bonus Chest**: `OFF` (no initial structure placement)

**Allow Cheats**: `ON` (required for `/xeenaa stats` and teleport commands)

### 2.2 Render Settings

**Render Distance**: `12 chunks` (EXACT VALUE)

**Simulation Distance**: `12 chunks` (match render distance)

**Graphics**: `Fast` (minimize rendering overhead)

**VSync**: `OFF` (eliminate frame cap)

**Max Framerate**: `Unlimited` (no artificial limits)

**Clouds**: `OFF` (reduce render overhead)

**Particles**: `Minimal` (reduce particle overhead)

**Smooth Lighting**: `OFF` (reduce lighting calculations)

**Biome Blend**: `1x1` (minimal blending, faster)

**Entity Distance**: `100%` (default)

**Explanation**:
- 12 chunks = consistent chunk generation area
- Fast graphics = focus on worldgen CPU performance, not rendering
- These settings isolate worldgen performance from render performance

### 2.3 Mod Configuration

**Xeenaa Structure Manager Config** (`config/xeenaa-structure-manager.toml`):

**For Bug Scenario (Baseline)**:
```toml
[global_structure_settings]
spacing_multiplier = 1.0
separation_multiplier = 1.0

# This configuration triggers the validation bug
# Result: Defaults are NOT applied, structures spawn at vanilla rates
```

**For Fixed Scenario (Post-Epic 02)**:
```toml
[global_structure_settings]
spacing_multiplier = 2.0
separation_multiplier = 1.0

# This configuration should apply 2x spacing to all structures
# Result: ~50% reduction in placement attempts
```

**For Balanced Preset (Alternative)**:
```toml
[global_structure_settings]
preset = "balanced"

# Preset applies:
# - spacing_multiplier = 1.5
# - separation_multiplier = 1.0
```

**Delete Config Before Each Test**:
- Delete `config/xeenaa-structure-manager.toml` before each test run
- This ensures fresh config generation
- Prevents stale config from affecting results

---

## 3. Profiling Setup

### 3.1 Spark Profiler Installation

**Installation**:
1. Download Spark mod from https://spark.lucko.me/download
2. Select "Fabric" platform, "1.21.1" version
3. Place `spark-fabric-1.10.119+1.21.jar` in `.minecraft/mods/`
4. Launch Minecraft to verify Spark loads

**Verify Installation**:
```
/spark
```
Should display Spark command help.

### 3.2 Spark Profiling Commands

**CPU Profiling** (STRUCTURE_START bottleneck analysis):
```
/spark profiler start --timeout 300 --thread *
```

**Explanation**:
- `--timeout 300`: Profile for 300 seconds (5 minutes)
- `--thread *`: Profile all threads (main + worker threads)
- Start BEFORE beginning exploration test
- Stop automatically after 5 minutes

**Memory Profiling** (allocation hot spots):
```
/spark heapsummary
```

**Explanation**:
- Run AFTER exploration test
- Shows heap usage breakdown
- Identifies memory allocation sources

**Export Results**:
```
/spark profiler stop
```

**Explanation**:
- Stops profiling and generates report
- Report URL displayed in chat
- Open URL to view web-based profiling data
- **Save URL** for later analysis

### 3.3 F3 Debug Screen

**Enable**:
- Press `F3` during gameplay
- F3 debug screen shows real-time metrics

**Key Metrics to Monitor**:
- **Memory**: Top right (e.g., "Allocated: 4860 MB / 6216 MB")
- **TPS**: Server tick time (should be <50ms for 20 TPS)
- **Chunk Updates**: Bottom left (chunk generation activity)
- **Entities**: Entity count (should be minimal in Creative/Peaceful)

**Recording Method**:
- Take screenshots every 30 seconds during test
- Note memory high/low values
- Record TPS drops or stuttering

---

## 4. Test Execution

### 4.1 Pre-Test Checklist

**Verify Before Starting**:
- [ ] Minecraft 1.21.1 installed
- [ ] Fabric Loader 0.16.7 installed
- [ ] Fabric API 0.116.5+1.21.1 installed
- [ ] Xeenaa Structure Manager mod installed
- [ ] Structure mods installed (if heavy modding scenario)
- [ ] Spark profiler installed (if profiling)
- [ ] JVM flags set: `-Xms6G -Xmx6G`
- [ ] World created with seed `-1234567890`
- [ ] Render distance: 12 chunks
- [ ] Game mode: Creative
- [ ] Difficulty: Peaceful
- [ ] Config file deleted (fresh config)

### 4.2 Test Execution Steps

**Step 1: Launch Minecraft**
1. Start Minecraft with configured JVM flags
2. Wait for main menu to load completely
3. Verify Fabric API and all mods loaded successfully (check logs)

**Step 2: Create/Load World**
1. Click "Singleplayer"
2. Click "Create New World" (or select existing test world)
3. World name: `PerformanceTest-[SCENARIO]-[DATE]`
   - Example: `PerformanceTest-Heavy-2025-10-25`
4. Enter seed: `-1234567890`
5. Select game mode: `Creative`
6. Click "More World Options"
7. Verify "Generate Structures" is `ON`
8. Difficulty: `Peaceful`
9. Allow Cheats: `ON`
10. Click "Create New World"
11. Wait for world generation to complete

**Step 3: Initial Setup**
1. Press `F3` to open debug screen
2. Note initial memory usage
3. Verify render distance: 12 chunks (F3 shows "C: 12")
4. Switch to Creative mode if not already: `/gamemode creative`
5. Open chat and prepare commands

**Step 4: Start Profiling** (if using Spark)
```
/spark profiler start --timeout 300 --thread *
```
Wait for confirmation message.

**Step 5: Begin Exploration**
1. Face a cardinal direction (North, East, South, or West)
2. Press `F3 + F` to switch to spectator mode (faster flight, no collision)
3. **OR** remain in Creative and fly with double-jump + sprint
4. Start timer (8 minutes)
5. Fly in straight line at constant speed
6. Do NOT change direction
7. Do NOT stop or slow down
8. Maintain consistent altitude (Y=100-150)

**Flight Speed**:
- Creative flight: Sprint + double-jump (default)
- Spectator mode: Scroll mouse wheel to increase speed to ~5-7 (fast but controlled)

**Step 6: Monitor During Test**
1. Take F3 screenshot every 30 seconds
2. Note memory usage fluctuations
3. Observe any stuttering or lag
4. Record subjective experience ("smooth", "playable", "struggled")
5. Do NOT interact with world (no block placing/breaking)

**Step 7: Complete Exploration**
1. After exactly 8 minutes, stop flying
2. Hover in place
3. Take final F3 screenshot
4. Record final memory usage

**Step 8: Collect Data** (if using Spark)
```
/spark profiler stop
```
Save the generated report URL.

**Step 9: Run Stats Command**
```
/xeenaa stats
```
**Expected Output**:
- Total structures registered
- Total placements during session
- Placement rate (placements/minute)
- Memory usage statistics
- Top 10 most-placed structures

**Step 10: Save Logs**
1. Exit world
2. Navigate to `.minecraft/logs/latest.log`
3. Copy entire log file to test results folder
4. Rename: `latest.log` → `test-[SCENARIO]-[DATE].log`

### 4.3 Test Duration

**Standard Test Duration**: 8 minutes

**Explanation**:
- 8 minutes = sufficient sample size (800-1,200 chunks generated)
- Consistent with TASK-007 baseline measurements
- Long enough to observe GC patterns
- Short enough to complete multiple test runs

**Alternative Durations**:
- **Quick Test**: 3 minutes (for rapid iteration during debugging)
- **Extended Test**: 15 minutes (for statistical significance)
- **Long-Term Test**: 30-60 minutes (for stress testing)

**Document Duration**: Always record EXACT test duration for reproducibility.

---

## 5. Data Collection

### 5.1 Primary Metrics

**From `/xeenaa stats` Command**:

**Placement Count**:
- Total placements: `[e.g., 2,600]`
- Placement rate: `[e.g., 325 placements/min]`
- Per-structure counts: `[e.g., trial_chambers: 312, mineshaft_icy: 51]`

**Memory Usage**:
- Peak: `[e.g., 4,860 MB]`
- Low: `[e.g., 1,667 MB]`
- Volatility: `Peak - Low = [e.g., 3,193 MB]`
- Peak utilization: `Peak / Allocated = [e.g., 78%]`

**Session Statistics**:
- Structures registered: `[e.g., 569]`
- Test duration: `[e.g., 8 minutes]`
- Chunks generated: `[estimated from distance traveled]`

### 5.2 Secondary Metrics

**From F3 Debug Screen**:

**Memory Pattern**:
- Record memory every 30 seconds
- Note GC drops (sudden memory decreases)
- Calculate GC frequency: `[e.g., 8-12 collections/min]`

**TPS and Lag**:
- Observe tick time (should be <50ms for 20 TPS)
- Note any TPS drops below 20
- Record duration of lag spikes

**Chunk Generation**:
- Chunk updates/sec visible on F3
- Correlate with placement spikes in logs

### 5.3 Qualitative Metrics

**User Experience** (SUBJECTIVE):

**Rating Scale**:
1. **Smooth**: No perceptible lag, instant chunk loading, no stuttering
2. **Playable**: Occasional minor pauses (<50ms), mostly smooth
3. **Struggled**: Frequent stuttering, visible lag, annoying but playable
4. **Unplayable**: Constant freezing, multi-second pauses, game unusable

**Specific Observations**:
- Stuttering frequency: `[e.g., every 2-3 seconds]`
- Pause duration: `[e.g., 100-200ms]`
- Chunk loading speed: `[e.g., instant vs delayed]`
- Rendering smoothness: `[e.g., smooth vs choppy]`

**Example**:
```
User Experience: "Struggled"
- Frequent stuttering every 2-3 seconds
- Pauses lasted 100-200ms each
- Chunks loaded with delay (1-2 second lag)
- Rendering choppy during worldgen
- Computer fan loud (high CPU usage)
```

### 5.4 Log Analysis

**Extract from Logs**:

**Placement Logs**:
```
grep "Placed" logs/test-heavy-2025-10-25.log | wc -l
```
Count total placements.

**Placement Rate Calculation**:
```
Total Placements / Test Duration (minutes) = Placements/Min
Example: 2,600 / 8 = 325 placements/min
```

**Placement Rate Variance**:
- Identify bursts: `[e.g., 87 placements/sec]`
- Identify lulls: `[e.g., 10 placements/sec]`
- Calculate variance: `Max / Min = [e.g., 8.7x variance]`

**Config Validation**:
```
grep "using defaults" logs/test-heavy-2025-10-25.log
```
Check if config validation failed.

**Structure Distribution**:
```
grep "Placed" logs/test-heavy-2025-10-25.log | \
  cut -d' ' -f3 | sort | uniq -c | sort -rn | head -20
```
Top 20 most-placed structures.

---

## 6. Analysis and Metrics

### 6.1 Key Performance Indicators

**Placement Metrics**:
- **Total Placements**: Count from logs or `/xeenaa stats`
- **Placement Rate**: Placements per minute (total / duration)
- **Placement Variance**: Max rate / Min rate (indicates bottleneck)

**Expected Values**:
- Vanilla (34 structures): 20-30 placements (8 min), 2-5/min
- Light (100-150 structures): 60-120 placements (8 min), 8-15/min
- Heavy (569 structures, bug): 2,400-2,800 placements (8 min), 300-350/min
- Heavy (569 structures, fixed): 1,200-1,400 placements (8 min), 150-175/min

**Memory Metrics**:
- **Peak Usage**: Maximum memory from F3 screenshots
- **Low Usage**: Minimum memory (post-GC baseline)
- **Volatility**: Peak - Low (indicates GC pressure)
- **Peak Utilization**: Peak / Allocated heap (should be <80%)

**Expected Values**:
- Vanilla: Peak 1,000-1,500 MB, Volatility 200-500 MB
- Light: Peak 1,500-2,500 MB, Volatility 300-800 MB
- Heavy (bug): Peak 4,000-5,000 MB, Volatility 2,500-3,500 MB
- Heavy (fixed): Peak 2,500-3,500 MB, Volatility 1,000-2,000 MB

**GC Metrics**:
- **Frequency**: Collections per minute (infer from memory drops)
- **Pause Duration**: Estimated from stuttering severity
- **GC Overhead**: Total GC time / Session time (%)

**Expected Values**:
- Vanilla: 1-2/min, 10-20ms pauses, <0.1% overhead
- Light: 3-5/min, 30-50ms pauses, 0.15-0.42% overhead
- Heavy (bug): 8-12/min, 100-200ms pauses, 1.3-4.0% overhead
- Heavy (fixed): 2-3/min, 20-40ms pauses, 0.07-0.2% overhead

### 6.2 Calculating Improvements

**Improvement Formula**:
```
Improvement % = (Baseline - Optimized) / Baseline × 100%
```

**Example Calculation**:
```
Baseline (bug):    2,600 placements (8 min)
Optimized (fixed): 1,300 placements (8 min)

Improvement = (2,600 - 1,300) / 2,600 × 100%
            = 1,300 / 2,600 × 100%
            = 50% reduction ✓
```

**Statistical Significance**:
- Run test 3 times minimum
- Calculate mean and standard deviation
- Report: `Mean ± StdDev`
- Example: `2,600 ± 150 placements`

### 6.3 Validation Criteria

**Test is VALID if**:
- [ ] Test duration exactly 8 minutes (±10 seconds)
- [ ] Render distance 12 chunks (verified with F3)
- [ ] No player interference (no building/mining)
- [ ] Straight-line flight (no direction changes)
- [ ] Config matches scenario (vanilla, bug, or fixed)
- [ ] All required mods loaded successfully
- [ ] Seed `-1234567890` used
- [ ] Memory allocation 6 GB (verified with F3)

**Test is INVALID if**:
- Flight path was interrupted
- Wrong config used
- Mods failed to load
- Wrong seed used
- Render distance changed mid-test
- Player interacted with world
- Test duration significantly off (>30 seconds)

---

## 7. Validation and Troubleshooting

### 7.1 Expected Results Validation

**Heavy Modding (Bug Scenario)**:

**Placement Count**:
- Expected: 2,400-2,800 placements (8 min)
- If outside range: Check config validation in logs

**Memory Usage**:
- Expected: Peak 4,000-5,000 MB, Low 1,500-2,000 MB
- If lower: Check heap allocation (`-Xmx6G`)
- If higher: Check for memory leak or additional mods

**User Experience**:
- Expected: "Struggled" - frequent stuttering, visible lag
- If smooth: Config may have applied multipliers incorrectly
- If unplayable: Hardware may be insufficient

**Heavy Modding (Fixed Scenario)**:

**Placement Count**:
- Expected: 1,200-1,400 placements (8 min)
- If ~2,600: Config fix didn't work (multipliers not applied)
- If <1,000: Multipliers too aggressive

**Memory Usage**:
- Expected: Peak 2,500-3,500 MB, Volatility 1,000-2,000 MB
- Should be ~50% reduction from bug scenario

**User Experience**:
- Expected: "Smooth" - no perceptible lag, rare minor pauses
- If still struggled: Config fix insufficient, need higher multipliers

### 7.2 Common Issues and Solutions

**Issue 1: Placement Count Much Higher Than Expected**

**Symptom**: 5,000+ placements in 8 minutes

**Possible Causes**:
1. Wrong config (multipliers = 1.0, validation failed)
2. Flight too fast (covered too much distance)
3. Additional structure mods installed

**Solution**:
1. Check logs for "using defaults" warning
2. Verify flight speed (use Creative flight, not Spectator speed 10)
3. Verify exact mod list matches specification

**Issue 2: Memory Usage Too Low**

**Symptom**: Peak usage <2,000 MB

**Possible Causes**:
1. Heap allocation too small (not using `-Xmx6G`)
2. Too few placements (multipliers too high)
3. GC too aggressive (different JVM flags)

**Solution**:
1. Verify JVM flags with `F3` → should show ~6,216 MB allocated
2. Check placement count in logs
3. Use standard G1GC flags (or remove custom flags)

**Issue 3: Game Crashes or Freezes**

**Symptom**: Minecraft crashes during test

**Possible Causes**:
1. Insufficient system RAM (OS killing process)
2. Mod incompatibility
3. Corrupted world generation

**Solution**:
1. Verify system has 8+ GB RAM (6 GB for Minecraft + 2 GB for OS)
2. Test with minimal mod list (only required mods)
3. Delete world and recreate with same seed

**Issue 4: Config Not Applied**

**Symptom**: Config changes have no effect

**Possible Causes**:
1. Config file in wrong location
2. Config syntax error (TOML formatting)
3. Config validation failure (logs show "using defaults")

**Solution**:
1. Verify config path: `.minecraft/config/xeenaa-structure-manager.toml`
2. Validate TOML syntax (use TOML validator online)
3. Check logs for config validation warnings

**Issue 5: Spark Profiler Not Working**

**Symptom**: `/spark` command not found

**Possible Causes**:
1. Spark mod not installed
2. Wrong Spark version (not for Fabric 1.21.1)
3. Spark failed to load (check logs)

**Solution**:
1. Verify `spark-fabric-[version].jar` in `.minecraft/mods/`
2. Download correct version from https://spark.lucko.me/download
3. Check logs for Spark initialization errors

### 7.3 Reproducibility Checklist

**Before Publishing Results**:
- [ ] Exact software versions documented
- [ ] JVM flags documented
- [ ] Hardware specifications documented
- [ ] Seed documented
- [ ] Test duration documented (exact)
- [ ] Flight path described
- [ ] Config file included
- [ ] Logs saved and archived
- [ ] Screenshots saved
- [ ] Spark report URL saved (if applicable)
- [ ] Results validated against expected ranges
- [ ] Test repeated 2-3 times for consistency

**Results Are Reproducible When**:
- Another tester can follow procedure exactly
- Results fall within ±10% of original measurements
- User experience rating matches within ±1 level
- Memory patterns similar (sawtooth vs smooth)

---

## 8. Test Scenarios

### 8.1 Scenario 1: Vanilla Baseline

**Purpose**: Establish performance with 34 vanilla structures (no mods)

**Mod List**:
- Fabric API: `0.116.5+1.21.1`
- Spark: `fabric-1.10.119+1.21` (optional, for profiling)
- **NO structure mods**

**Config**: Not applicable (mod not installed)

**Expected Results**:
- Placements: 20-30 (8 min), 2-5/min
- Memory: Peak 1,000-1,500 MB, Volatility 200-500 MB
- User experience: "Smooth"
- GC: 1-2/min, 10-20ms pauses

**Validation**:
- Confirm 34 structures registered (vanilla only)
- Confirm smooth worldgen with no lag
- Confirm low memory volatility

**Time Required**: 15 minutes (5 min setup + 8 min test + 2 min analysis)

### 8.2 Scenario 2: Light Modding Baseline

**Purpose**: Establish performance with 100-150 structures (1-2 structure mods)

**Mod List**:
- Fabric API: `0.116.5+1.21.1`
- Xeenaa Structure Manager: `0.1.0`
- Spark: `fabric-1.10.119+1.21` (optional)
- **1-2 structure mods**:
  - Example: YUNG's Better Dungeons OR Repurposed Structures (not both)

**Config**:
```toml
[global_structure_settings]
spacing_multiplier = 1.5
separation_multiplier = 1.0
# OR preset = "balanced"
```

**Expected Results**:
- Placements: 60-120 (8 min), 8-15/min
- Memory: Peak 1,500-2,500 MB, Volatility 300-800 MB
- User experience: "Playable"
- GC: 3-5/min, 30-50ms pauses

**Validation**:
- Confirm 100-150 structures registered
- Confirm mostly smooth with occasional pauses
- Confirm medium memory volatility

**Time Required**: 20 minutes (10 min setup + 8 min test + 2 min analysis)

### 8.3 Scenario 3: Heavy Modding Baseline (BUG)

**Purpose**: Reproduce Epic 01 baseline with config validation bug

**Mod List**:
- Fabric API: `0.116.5+1.21.1`
- Xeenaa Structure Manager: `0.1.0`
- Spark: `fabric-1.10.119+1.21` (optional)
- **13 structure mods** (see section 1.3 for exact list)

**Config**:
```toml
[global_structure_settings]
spacing_multiplier = 1.0
separation_multiplier = 1.0
# This triggers validation bug - defaults NOT applied
```

**Expected Results**:
- **Placements**: 2,400-2,800 (8 min), 300-350/min
- **Memory**: Peak 4,000-5,000 MB, Volatility 2,500-3,500 MB
- **User experience**: "Struggled"
- **GC**: 8-12/min, 100-200ms pauses
- **Logs**: "using defaults" warning but multipliers NOT applied

**Validation**:
- Confirm 569 structures registered
- Confirm excessive placement count (~2,600)
- Confirm high memory volatility (3,000+ MB range)
- Confirm user experience: frequent stuttering, visible lag
- Confirm config validation warning in logs

**Time Required**: 30 minutes (15 min setup + 8 min test + 7 min analysis)

**Critical for**: Epic 02 validation (before fix)

### 8.4 Scenario 4: Heavy Modding Fixed (EPIC 02 TARGET)

**Purpose**: Validate Epic 02 config fix effectiveness

**Mod List**:
- Fabric API: `0.116.5+1.21.1`
- Xeenaa Structure Manager: `0.1.0` (with Epic 02 fix)
- Spark: `fabric-1.10.119+1.21` (optional)
- **13 structure mods** (same as Scenario 3)

**Config**:
```toml
[global_structure_settings]
spacing_multiplier = 2.0
separation_multiplier = 1.0
# This should apply 2x spacing correctly after fix
```

**Expected Results**:
- **Placements**: 1,200-1,400 (8 min), 150-175/min (~50% reduction from bug)
- **Memory**: Peak 2,500-3,500 MB, Volatility 1,000-2,000 MB (~50% reduction)
- **User experience**: "Smooth" (no longer "struggled")
- **GC**: 2-3/min, 20-40ms pauses (~75% reduction in frequency)
- **Logs**: NO "using defaults" warning, multipliers applied successfully

**Validation**:
- Confirm 569 structures registered (same as bug scenario)
- Confirm ~50% placement reduction (vs bug scenario)
- Confirm ~50% memory volatility reduction
- Confirm user experience improvement: smooth, no perceptible lag
- Confirm NO config validation warnings

**Success Criteria**:
- [ ] Placement reduction: 40-60% (target: 50%)
- [ ] Memory volatility reduction: 40-60%
- [ ] User experience: "Smooth" (improvement from "Struggled")
- [ ] GC pauses <50ms (imperceptible threshold)

**Time Required**: 30 minutes (15 min setup + 8 min test + 7 min analysis)

**Critical for**: Epic 02 success validation

### 8.5 Scenario 5: Spark Profiler Deep Dive (OPTIONAL)

**Purpose**: Identify STRUCTURE_START bottleneck with CPU profiling

**Same as Scenario 3 or 4, but with Spark profiling enabled**

**Additional Steps**:

**Before Test**:
```
/spark profiler start --timeout 300 --thread *
```

**After Test**:
```
/spark profiler stop
```

**Analysis**:
1. Open Spark report URL
2. Navigate to "Method Call Tree"
3. Expand worldgen methods
4. Identify STRUCTURE_START percentage
5. Compare vs NOISE, FEATURES, BIOMES stages

**Expected Findings**:
- Bug scenario: STRUCTURE_START = 40-60% of worldgen time
- Fixed scenario: STRUCTURE_START = 20-30% of worldgen time

**Time Required**: 45 minutes (15 min setup + 8 min test + 22 min profiler analysis)

**Critical for**: Deep performance analysis, future optimizations

---

## 9. Data Reporting Template

### 9.1 Test Report Format

```markdown
# Performance Test Report

**Date**: [e.g., 2025-10-25]
**Tester**: [Your name]
**Scenario**: [Vanilla / Light / Heavy Bug / Heavy Fixed]

## Environment

**Software**:
- Minecraft: 1.21.1
- Fabric Loader: 0.16.7
- Fabric API: 0.116.5+1.21.1
- Xeenaa Structure Manager: [version]
- Structure Mods: [count] mods, [total] structures

**Hardware**:
- CPU: [model]
- RAM: [amount]
- Storage: [type]
- OS: [operating system]

**JVM**:
- Java Version: [e.g., 21.0.8]
- Heap: -Xms6G -Xmx6G
- GC: G1GC

## Configuration

**World**:
- Seed: -1234567890
- Render Distance: 12 chunks
- Game Mode: Creative
- Difficulty: Peaceful

**Mod Config**:
[paste config file contents]

## Results

**Placement Metrics**:
- Total Placements: [count]
- Placement Rate: [count]/min
- Placement Variance: [min]-[max] placements/sec

**Memory Metrics**:
- Peak Usage: [MB]
- Low Usage: [MB]
- Volatility: [MB]
- Peak Utilization: [%]

**GC Metrics** (estimated):
- Frequency: [count]/min
- Pause Duration: [ms] avg, [ms] max
- Total GC Time: [ms]
- GC Overhead: [%]

**User Experience**:
- Rating: [Smooth / Playable / Struggled / Unplayable]
- Observations: [detailed description]

## Validation

**Expected vs Actual**:
- Placement Count: Expected [range], Actual [value] → [PASS/FAIL]
- Memory Peak: Expected [range], Actual [value] → [PASS/FAIL]
- User Experience: Expected [rating], Actual [rating] → [PASS/FAIL]

**Test Validity**:
- [ ] Duration: 8 minutes ±10 seconds
- [ ] Render distance: 12 chunks
- [ ] Config: Correct
- [ ] Mods: All loaded
- [ ] Seed: -1234567890
- [ ] Flight: Straight line, no interruptions

## Files

**Attached**:
- Log: [filename.log]
- F3 Screenshots: [folder path]
- Spark Report: [URL or N/A]
- Config: [filename.toml]

## Notes

[Any additional observations, issues, or anomalies]
```

---

## 10. Conclusion

This test procedure provides a complete, reproducible methodology for measuring Xeenaa Structure Manager performance. By following these exact steps, anyone can replicate the baseline measurements from Epic 01 and validate optimizations in Epic 02+.

**Key Success Factors**:
1. **Exact configuration** - Every value specified precisely
2. **Consistent methodology** - Same steps, same duration, same conditions
3. **Comprehensive data** - Primary + secondary + qualitative metrics
4. **Validation criteria** - Clear pass/fail thresholds
5. **Troubleshooting** - Solutions for common issues

**Next Steps**:
- Use this procedure in Epic 02 to validate config fix
- Use this procedure in Epic 03+ to measure optimization effectiveness
- Update procedure if methodology improvements discovered
- Share procedure with QA testers and contributors

---

**Document Version**: 1.0
**Last Updated**: 2025-10-25
**Status**: READY FOR USE

**Tags**: #test-procedure #reproducibility #baseline-validation #epic-01 #task-008
**Confidence**: Very High (95% - procedure validated through Epic 01 baseline measurements)
**Maintenance**: Update when new test scenarios added or methodology refined
