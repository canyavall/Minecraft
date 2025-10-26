# Performance Validation Guide

**Epic**: 01-core-framework-geckolib
**Version**: 1.0.0
**Last Updated**: 2025-10-26

---

## Table of Contents

1. [Introduction](#introduction)
2. [Performance Requirements](#performance-requirements)
3. [Test Environment Setup](#test-environment-setup)
4. [Test Procedures](#test-procedures)
5. [Interpreting Results](#interpreting-results)
6. [Performance Troubleshooting](#performance-troubleshooting)
7. [Baseline Expectations](#baseline-expectations)
8. [Appendix: Tool Reference](#appendix-tool-reference)

---

## Introduction

This guide provides comprehensive procedures for validating that the Alex's Mobs Fabric port framework meets performance requirements. Performance validation ensures that the entity framework, GeckoLib integration, and rendering systems operate efficiently before porting the full catalog of 89+ animal mobs.

### Why Performance Validation Matters

- **Scalability**: The framework will be used for 89+ animal entities across diverse biomes
- **Multiplayer**: Server performance (TPS) directly impacts player experience
- **Client Performance**: Rendering performance affects playability (FPS)
- **Early Detection**: Finding bottlenecks now prevents costly refactoring later

### When to Validate

- ✅ **After TASK-011**: Initial framework validation (current task)
- ✅ **After Each Epic**: Regression testing as features are added
- ✅ **Before Release**: Final validation before publishing mod
- ✅ **After Optimization**: Confirm improvements are measurable

---

## Performance Requirements

The framework must meet these targets (defined in Epic 01 requirements):

| Metric | Target | Critical Threshold | Notes |
|--------|--------|-------------------|-------|
| **Entity Tick Time** | < 0.05ms per entity | < 0.1ms per entity | AI goal evaluation per tick |
| **Render Time** | < 0.1ms per entity | < 0.2ms per entity | GeckoLib model rendering |
| **Server TPS** | 20 TPS stable | ≥ 18 TPS | With 50+ entities spawned |
| **Client FPS** | ≥ 60 FPS | ≥ 30 FPS | Mid-range hardware (RTX 2060, i5-9400) |
| **Memory Leaks** | None detected | < 50 MB/hour growth | Over 10-minute test |
| **Startup Time** | < 2 seconds added | < 5 seconds added | Mod initialization overhead |

**Success Criteria**: All metrics meet **Target** values. If any metric only meets **Critical Threshold**, optimization is recommended but not required for epic completion.

---

## Test Environment Setup

### Required Tools

#### 1. Spark Profiler (Recommended)

**Installation**:
```bash
# Download Spark from Modrinth
# https://modrinth.com/mod/spark

# Add to mods folder
cp spark-1.10.73-fabric.jar .minecraft/mods/
```

**Purpose**: CPU profiling, tick time measurement, entity profiling

**Why**: Most accurate entity tick time measurement available for Minecraft mods

#### 2. F3 Debug Screen (Built-in)

**Access**: Press `F3` in-game

**Purpose**: TPS, FPS, memory, chunk stats

**Why**: Always available, no installation required

#### 3. Java Flight Recorder (Advanced)

**Activation**:
```bash
# Add JVM argument when launching Minecraft
-XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=recording.jfr
```

**Purpose**: Deep JVM profiling, GC analysis, memory allocation

**Why**: Best for identifying memory leaks and GC pressure

#### 4. Test World Setup

**Configuration**:
- **World Type**: Flat (superflat)
- **Game Mode**: Creative
- **Difficulty**: Peaceful (prevents mob spawning interference)
- **Render Distance**: 12 chunks (standard)
- **Simulation Distance**: 8 chunks (default)

**Why**: Controlled environment eliminates variable factors (terrain generation, hostile mobs)

---

## Test Procedures

### Test 1: Entity Tick Time Profiling

**Goal**: Measure CPU time consumed by entity AI goals per tick

**Prerequisites**:
- Spark profiler installed
- Test world loaded
- No other entities nearby

**Procedure**:

1. **Spawn Test Entities**:
   ```
   /summon xeenaa-alexs-mobs:test_animal ~ ~ ~
   ```
   Repeat 50 times (use command block for automation)

2. **Start Profiler**:
   ```
   /spark profiler start --tick-time 60
   ```

3. **Wait 60 Seconds**: Let entities execute AI goals naturally

4. **Stop Profiler**:
   ```
   /spark profiler stop
   ```

5. **View Results**:
   ```
   /spark profiler open
   ```

**What to Look For**:
- Find `TestAnimalEntity.tick()` in profiler output
- Check percentage of total tick time consumed
- Calculate: `(total_tick_time / 50 entities) < 0.05ms`

**Success Criteria**:
- ✅ Individual entity tick time < 0.05ms per entity
- ✅ Entity AI does not appear in top 10 performance consumers

**Example Output**:
```
TestAnimalEntity.tick() - 2.3ms total (0.046ms per entity) ✅
Total tick: 45ms (95% unrelated to entities)
```

---

### Test 2: Rendering Performance (FPS Test)

**Goal**: Measure client-side rendering performance with multiple entities

**Prerequisites**:
- Test world loaded in **client mode**
- 50 test entities spawned (from Test 1)
- Standing in position where all entities are visible

**Procedure**:

1. **Position Camera**: Stand where 20+ entities are visible on screen

2. **Record Baseline FPS** (F3 screen):
   - Note FPS before looking at entities
   - Turn away from entities, record FPS

3. **Measure Entity FPS**:
   - Look directly at entity cluster
   - Record FPS for 30 seconds (watch for spikes/drops)
   - Note minimum, maximum, average FPS

4. **Test Different Angles**:
   - Repeat at different camera distances (5m, 10m, 20m)
   - Note FPS at each distance

**What to Look For**:
- FPS should remain ≥ 60 on mid-range hardware
- No stuttering or frame drops when entities animate
- FPS should not decrease significantly when looking at entities vs looking away

**Success Criteria**:
- ✅ Average FPS ≥ 60 (mid-range hardware)
- ✅ FPS drop < 10 FPS when looking at entity cluster vs away
- ✅ No stuttering during entity animations

**Example Data**:
```
Baseline (no entities visible): 120 FPS
Entity cluster (20 visible): 110 FPS ✅ (drop: 10 FPS)
Close-up (5m distance): 105 FPS ✅
```

---

### Test 3: Server TPS Stability Test

**Goal**: Verify server maintains 20 TPS with entity load

**Prerequisites**:
- Dedicated server OR singleplayer world
- 50 test entities spawned
- F3 debug screen open

**Procedure**:

1. **Check Baseline TPS** (F3 screen, "Server tick" line):
   - Before spawning entities: Should be exactly 20.0 TPS
   - If < 20.0, there's existing lag (fix before testing)

2. **Spawn 50 Entities**:
   ```
   /execute at @s run summon xeenaa-alexs-mobs:test_animal ~5 ~ ~5
   ```
   (Use command block to spawn 50 times)

3. **Monitor TPS for 5 Minutes**:
   - Watch F3 "Server tick" value
   - Record minimum TPS observed
   - Note any TPS drops

4. **Stress Test** (Optional):
   - Spawn 100 entities total
   - Monitor TPS degradation

**What to Look For**:
- TPS should remain at 20.0 consistently
- Acceptable: Occasional dips to 19.8 TPS (< 1% variance)
- Warning: TPS < 19.0 indicates performance issue

**Success Criteria**:
- ✅ TPS remains ≥ 19.8 with 50 entities
- ✅ TPS remains ≥ 18.0 with 100 entities (stress test)
- ✅ No sustained TPS drops over 5 minutes

**Example Output**:
```
Baseline: 20.0 TPS ✅
With 50 entities: 19.9 TPS ✅
With 100 entities: 19.2 TPS ✅
```

---

### Test 4: Memory Leak Detection

**Goal**: Ensure entity framework does not leak memory over time

**Prerequisites**:
- F3 debug screen OR Java Flight Recorder
- 50 test entities spawned
- 10-minute test duration

**Procedure**:

1. **Record Baseline Memory** (F3 screen, "Mem:" line):
   - Note used/allocated memory (e.g., 2048MB / 4096MB)
   - Force garbage collection: Minecraft usually does this automatically

2. **Spawn Entities and Wait**:
   - Spawn 50 test entities
   - Let game run for 10 minutes
   - **Do not** change chunks or reload resources

3. **Monitor Memory Growth**:
   - Check memory every 2 minutes
   - Record: Time, Used Memory, Allocated Memory

4. **Calculate Growth Rate**:
   ```
   Memory Growth = (Final Used - Initial Used) / Duration
   Acceptable: < 50 MB/hour
   ```

**What to Look For**:
- Memory usage should stabilize after initial spike (entity loading)
- Garbage collection should prevent sustained growth
- After 10 minutes, memory should not have grown significantly

**Success Criteria**:
- ✅ Memory growth < 50 MB over 10 minutes
- ✅ No out-of-memory errors
- ✅ GC frequency remains normal (< 10 GC events per minute)

**Example Data**:
```
T=0min:   2048 MB used ✅
T=2min:   2100 MB used (entity loading spike)
T=4min:   2095 MB used (GC cleaned up)
T=6min:   2098 MB used
T=8min:   2092 MB used (GC)
T=10min:  2100 MB used ✅ (52 MB growth - acceptable)
```

---

### Test 5: Resource Reload Performance (F3+T)

**Goal**: Verify resources reload efficiently without crashes

**Prerequisites**:
- Test world loaded
- Test entities spawned
- Resources in correct directories

**Procedure**:

1. **Initial Load**:
   - Launch game, load world
   - Record time to main menu (check logs)

2. **Trigger Resource Reload**:
   - Press `F3 + T` (reload resource packs)
   - Watch for errors in console
   - Time the reload duration (watch screen)

3. **Verify Entities Still Render**:
   - Check test entities still have textures
   - Verify animations still play
   - Check for pink/black checkerboard (missing texture)

4. **Check Console for Errors**:
   ```
   [ERROR] Could not load texture: ...
   [WARN] Missing animation: ...
   ```

**What to Look For**:
- Reload completes in < 10 seconds
- No error messages in console
- Entities continue to render correctly after reload

**Success Criteria**:
- ✅ Resource reload completes without errors
- ✅ Reload time < 10 seconds
- ✅ Entities render correctly post-reload
- ✅ No "missing texture" or "missing animation" warnings

**Example Output**:
```
[INFO] Reloading resource packs...
[INFO] Loaded 1 geo models
[INFO] Loaded 1 animation files
[INFO] Resource reload complete (3.2s) ✅
```

---

### Test 6: Entity Spawn and Despawn Stress Test

**Goal**: Test rapid entity creation/removal for memory leaks

**Prerequisites**:
- Command blocks set up
- Spark profiler (optional but recommended)

**Procedure**:

1. **Setup Command Block Chain**:
   ```
   Command Block 1: /summon xeenaa-alexs-mobs:test_animal ~ ~ ~
   Command Block 2 (after 5s delay): /kill @e[type=xeenaa-alexs-mobs:test_animal]
   ```

2. **Run Loop for 2 Minutes**:
   - Let command blocks spawn/kill entities repeatedly
   - Monitor memory and TPS

3. **Check for Issues**:
   - Memory should not grow continuously
   - TPS should remain stable
   - No entity ghosts (entities that don't despawn)

**What to Look For**:
- Memory returns to baseline after each kill
- No entities remain when `/kill` executes
- TPS remains stable during spawn/despawn cycle

**Success Criteria**:
- ✅ Memory stabilizes (no continuous growth)
- ✅ All entities despawn cleanly
- ✅ TPS remains ≥ 19.5 during stress test

---

## Interpreting Results

### Performance Tier Classification

| Tier | Description | Action Required |
|------|-------------|----------------|
| **Excellent** | All metrics exceed targets by 20%+ | None - framework is highly optimized |
| **Good** | All metrics meet target values | None - proceed with epic completion |
| **Acceptable** | Metrics meet critical thresholds | Document findings, consider optimization in future epic |
| **Poor** | Metrics fail critical thresholds | **BLOCK EPIC** - optimization required before proceeding |

### Common Performance Patterns

#### Pattern 1: High Entity Tick Time

**Symptoms**:
- Entity tick > 0.1ms per entity
- TPS drops proportionally with entity count

**Causes**:
- Expensive AI goal checks every tick
- Pathfinding calculations not cached
- Unnecessary world queries

**See**: `OPTIMIZATION_NOTES.md` - "AI Goal Optimization"

---

#### Pattern 2: Low FPS with Entities Visible

**Symptoms**:
- FPS drops significantly when looking at entities
- Rendering time > 0.2ms per entity

**Causes**:
- GeckoLib model complexity
- Too many vertices per model
- Inefficient texture sampling

**See**: `OPTIMIZATION_NOTES.md` - "Rendering Optimization"

---

#### Pattern 3: Memory Leak

**Symptoms**:
- Continuous memory growth over time
- Out-of-memory crashes after extended play
- Frequent garbage collection

**Causes**:
- Animation caches not released
- Entity references held after death
- Event listeners not unregistered

**See**: `OPTIMIZATION_NOTES.md` - "Memory Management"

---

## Performance Troubleshooting

### Issue: TPS < 18 with 50 Entities

**Diagnostic Steps**:

1. **Check Baseline TPS** (before spawning entities):
   - If already < 20, issue is NOT the mod

2. **Profile with Spark**:
   ```
   /spark profiler start --tick-time 60
   ```
   - Identify which AI goal is expensive

3. **Disable AI Goals** (code change):
   ```java
   @Override
   protected void initGoals() {
       // Comment out all goals except SwimGoal
       this.goalSelector.add(0, new SwimGoal(this));
   }
   ```
   - Retest TPS
   - If TPS improves, problem is AI goals

4. **Check for Infinite Loops**:
   - Review custom `registerCustomGoals()` implementations
   - Look for goals that never complete

**Solution Paths**:
- Reduce AI goal check frequency (use tick counters)
- Cache expensive calculations
- Optimize pathfinding (use navigation cache)

---

### Issue: FPS < 30 When Looking at Entities

**Diagnostic Steps**:

1. **Check Baseline FPS** (not looking at entities):
   - If already < 60, issue is hardware/graphics settings

2. **Reduce Render Distance**:
   - Set to 8 chunks
   - If FPS improves significantly, issue is entity count not complexity

3. **Simplify Model** (test):
   - Replace test_animal.geo.json with simple cube
   - If FPS improves, model is too complex

4. **Check Texture Size**:
   - Textures > 256x256 are inefficient for entities
   - Reduce to 64x64 or 128x128

**Solution Paths**:
- Simplify GeckoLib models (reduce vertex count)
- Implement level-of-detail (LOD) rendering
- Reduce texture resolution
- Cull entities outside view frustum

---

### Issue: Memory Leak Detected

**Diagnostic Steps**:

1. **Use Java Flight Recorder**:
   ```bash
   java -XX:+FlightRecorder -XX:StartFlightRecording=duration=300s,filename=recording.jfr
   ```

2. **Analyze Heap Dump**:
   - Look for accumulating `TestAnimalEntity` instances
   - Check if AnimatableInstanceCache grows unbounded

3. **Test Entity Despawn**:
   ```
   /summon xeenaa-alexs-mobs:test_animal
   /kill @e[type=xeenaa-alexs-mobs:test_animal]
   ```
   - Check memory before and after
   - Memory should decrease after `/kill`

**Solution Paths**:
- Ensure entity cleanup in `remove()` method
- Clear animation caches when entity despawns
- Remove event listeners on entity death
- Review GeckoLib cache management

---

## Baseline Expectations

Based on framework design and GeckoLib benchmarks:

### Expected Performance Metrics

| Test | Expected Result | Rationale |
|------|----------------|-----------|
| **Entity Tick Time** | 0.02-0.04ms per entity | Simple AI goals (no pathfinding cache yet) |
| **Render Time** | 0.05-0.08ms per entity | Simple cube model (minimal vertices) |
| **TPS (50 entities)** | 19.8-20.0 TPS | Minimal AI overhead |
| **TPS (100 entities)** | 19.0-19.5 TPS | Linear scaling |
| **FPS (mid-range)** | 80-120 FPS | Simple models, low texture resolution |
| **Memory Growth** | < 30 MB / 10 min | GeckoLib caching stabilizes quickly |
| **Startup Time** | +1.0-1.5 seconds | GeckoLib initialization, registry setup |

### Comparison to Vanilla Entities

For reference, vanilla Minecraft entities:

| Entity Type | Tick Time | Notes |
|------------|-----------|-------|
| **Cow** | ~0.03ms | Simple passive mob |
| **Villager** | ~0.15ms | Complex AI (trading, pathfinding) |
| **Zombie** | ~0.08ms | Moderate AI (target selection, pathfinding) |

**Target**: Test entity should perform similar to vanilla **Cow** (simple passive mob).

---

## Appendix: Tool Reference

### Spark Profiler Commands

```bash
# Start profiling (60 second duration)
/spark profiler start --tick-time 60

# Stop profiling
/spark profiler stop

# Open web viewer
/spark profiler open

# Export profiling data
/spark profiler export

# Monitor TPS in real-time
/spark tps
```

### F3 Debug Screen Key Metrics

| Line | Metric | Meaning |
|------|--------|---------|
| **FPS** | Frames per second | Client rendering performance |
| **C** | Chunk sections rendered | How many chunks are visible |
| **E** | Entity count | Total entities in loaded chunks |
| **Mem** | Memory usage | `Used MB / Allocated MB` |
| **Server tick** | TPS | Server ticks per second (should be 20.0) |
| **ms ticks** | Tick duration | Milliseconds per tick (should be ~50ms) |

### Java Flight Recorder Analysis

**View Recording**:
```bash
jmc recording.jfr  # Opens Java Mission Control
```

**Key Views**:
- **Memory** → Heap usage over time
- **GC** → Garbage collection frequency/duration
- **Method Profiling** → Hot methods consuming CPU
- **Allocation** → Objects allocated per second

---

## Summary Checklist

Before marking TASK-012 complete, verify:

- [ ] All 6 test procedures documented clearly
- [ ] Performance targets are actionable and measurable
- [ ] Troubleshooting section covers common issues
- [ ] Tool setup instructions are complete
- [ ] Expected baselines are realistic
- [ ] Examples are concrete and helpful

**Next Steps**:
1. Run tests manually (user responsibility)
2. Record results in `PERFORMANCE_RESULTS.md` (optional)
3. If all tests pass, mark Epic 01 complete
4. Proceed to Epic 02 (Mob Catalog & Prioritization)

---

**Document Version**: 1.0.0
**Last Updated**: 2025-10-26
**Author**: implementation-agent (Canya)
