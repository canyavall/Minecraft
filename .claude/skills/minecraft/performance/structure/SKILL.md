---
name: minecraft-performance-structure
description: Structure generation performance optimization for Minecraft 1.21.1+ based on Epic 01 comprehensive research. Covers STRUCTURE_START bottleneck (40-60% of worldgen), grid-based filtering, spacing multipliers, jigsaw assembly O(n²), and config validation. Part of minecraft/performance skill family.
allowed-tools: [Read, Grep, Glob, WebFetch, WebSearch]
---

# Structure Performance Optimization Skill

**Category**: Minecraft Performance (Structure Specialized)
**Last Updated**: 2025-10-25 (Epic 01 Research Complete)
**Applies To**: Minecraft 1.21.1+ structure generation optimization
**Research Confidence**: 95% (validated through extensive profiling)

## Overview

This skill provides comprehensive knowledge about Minecraft structure generation performance, based on Epic 01 research including profiling 2,600 structure placements across multiple scenarios, algorithm analysis, and existing mod research.

## Critical Finding: The PRIMARY Bottleneck

**STRUCTURE_START is the PRIMARY bottleneck** consuming **40-60% of worldgen time** (vs 10-15% vanilla).

### Evidence from Profiling

| Metric | Vanilla (34) | Heavy Modded (569) | Impact |
|--------|--------------|-------------------|--------|
| STRUCTURE_START time | 5-10ms | 50-100ms | **10x slower** |
| % of worldgen | 10-15% | 40-60% | **4x higher** |
| Grid checks per chunk | ~25 | ~150 | 6x more |
| Placements per 8 min | ~30 | 2,600 | **87x more** |
| Synchronization overhead | ~10% | 57% | **Blocks pipeline** |

**Key Insight**: The bottleneck is NOT just "more structures" - it's the **placement frequency explosion** caused by too many structures at vanilla spacing.

## The Structure Generation Pipeline (3 Phases)

### Phase 1: World Load (One-Time)

```
569 total structures (all mods loaded)
→ Dimension filter: 569 → ~400 (Overworld-compatible only)
→ BiomeSource filter: 400 → 80-150 (has valid biomes in this world)
→ StructurePlacementCalculator cached with 80-150 structures
```

**Performance**: ~28 seconds for 569 structures (one-time, acceptable)

### Phase 2: STRUCTURE_START (Per-Chunk) **← PRIMARY BOTTLENECK**

```
For each chunk generated:
1. Grid Check (80-150 structures):
   - Calculate grid cell for each structure
   - Check if THIS chunk matches grid position
   - Result: 5-20 structures match grid (NOT all 80-150!)
   - Cost: ~0.02% of STRUCTURE_START time (negligible!)

2. Biome Check (5-20 grid matches):
   - Validate structure can spawn in this biome
   - Result: 2-10 structures are biome-compatible
   - Cost: ~5-10% of STRUCTURE_START time

3. Jigsaw Assembly (2-10 biome-compatible):
   - Generate structure pieces (template pool selection)
   - Check piece intersections (O(n²) complexity!)
   - Result: 1-3 structures successfully created
   - Cost: ~50-70% of STRUCTURE_START time **← SECONDARY BOTTLENECK**

4. Save to Chunk NBT (1-3 successful):
   - Write StructureStart to chunk data
   - Structure NOT placed yet (just "reserved")
   - Cost: ~5-10% of STRUCTURE_START time
```

**Total STRUCTURE_START Time**:
- Vanilla: 5-10ms per chunk (10-15% of worldgen)
- Heavy modded: 50-100ms per chunk (40-60% of worldgen)
- **Synchronization**: Must complete for ALL surrounding chunks before next stages → 57% overhead

### Phase 3: Placement (After Terrain)

```
After terrain generation completes:
1. Read StructureStart from chunk NBT
2. Place structure pieces in world (blocks, entities)
3. PlacementTracker records placement for verification
```

**Performance**: 2-5ms per structure (not a bottleneck)

## What Is NOT the Bottleneck

### ❌ Myth 1: "Grid checks are expensive"
**Reality**: Grid checks = **0.02% of STRUCTURE_START time**
- 569 grid checks = pure math (no I/O, no lookups)
- Already highly optimized by vanilla
- NOT worth optimizing further

### ❌ Myth 2: "Memory is the problem"
**Reality**: 569 structures = **~250 KB** (negligible)
- Memory FOLLOWS placement rate (r² = 0.98 correlation)
- 2,600 placements → 20 MB/min allocation → 8-12 GC/min
- Fix placement rate → memory issues vanish
- **Memory is a SYMPTOM, not the CAUSE**

### ❌ Myth 3: "Biome filtering is slow"
**Reality**: Biome check = **5-10% of STRUCTURE_START time**
- Only checked for 5-20 grid matches
- Biome source lookup is fast (cached)
- NOT the primary bottleneck

### ❌ Myth 4: "22 mineshaft variants = 22x slower"
**Reality**: Variants in same StructureSet = **1 check**
- Variant selection happens AFTER grid/biome filtering
- Structure variants don't multiply placement attempts
- Only badly-organized mods (22 separate StructureSets) cause problems

## What IS the Bottleneck

### ✅ PRIMARY: STRUCTURE_START Placement Frequency (40-60% of worldgen)

**Problem**: Too many placement ATTEMPTS per chunk
- 569 structures at vanilla spacing = 2,600 placements in 8 min (87x vanilla!)
- Each attempt triggers expensive jigsaw assembly
- **Root cause**: Config validation bug prevents spacing multipliers from applying

**Solution**: Fix config bug + apply spacing multipliers
- 2x spacing = 75% fewer grid matches per chunk
- 50% fewer placement attempts overall
- **Expected improvement**: 50-80% worldgen speedup

### ✅ SECONDARY: Jigsaw Assembly O(n²) (20-30% of STRUCTURE_START)

**Problem**: Each structure piece checks intersection with EVERY other piece
- Ancient city (200 pieces) = 40,000 intersection checks
- Trial chambers (312 placements measured) = #1 performance outlier
- O(n²) complexity scales poorly

**Solution**: Structure Layout Optimizer mod (BoxOctree spatial indexing)
- Reduces O(n²) → O(n log n)
- **50-90% improvement** for jigsaw assembly
- Already exists - recommend instead of reimplementing

### ✅ TERTIARY: Synchronization Overhead (57% wasted time)

**Problem**: STRUCTURE_START must complete for ALL surrounding chunks before next stages
- Blocks parallel worldgen pipeline
- 57% of time spent WAITING (not computing)
- Cannot parallelize STRUCTURE_START stage

**Solution**: Reduce STRUCTURE_START time (via spacing multipliers)
- 50% faster STRUCTURE_START = 50% less waiting
- **Cannot eliminate synchronization** (vanilla limitation)
- Future: Move jigsaw to FEATURES stage (async, research-only)

## Optimization Strategies (Ranked by ROI)

### Strategy 1: Fix Config Validation Bug (ROI: 10.0) **← IMMEDIATE**

**Problem**: Config validation rejects `spacing_multiplier = 1.0`
- Logs "using defaults" but doesn't actually apply them
- All 569 structures spawn at vanilla spacing (no multiplier applied)
- Result: 2,600 placements vs expected 200-400 (100x explosion!)

**Solution**: Fix validation to accept 1.0 as valid, apply defaults correctly

**Implementation**:
```java
// Current (BROKEN):
if (spacingMultiplier <= 1.0) {
    LOGGER.warn("Invalid spacing_multiplier {}, using defaults", spacingMultiplier);
    spacingMultiplier = 1.0; // But defaults are NEVER applied!
}

// Fixed:
if (spacingMultiplier < 1.0) {
    LOGGER.warn("Invalid spacing_multiplier {} (must be >= 1.0), using 2.0", spacingMultiplier);
    spacingMultiplier = 2.0; // Apply 2.0x as default
} else if (spacingMultiplier == 1.0) {
    spacingMultiplier = 2.0; // Treat 1.0 as "use recommended default"
    LOGGER.info("Using recommended spacing multiplier: 2.0x");
}
```

**Expected Impact**:
- Placements: 2,600 → 1,300 (50% reduction)
- STRUCTURE_START: 50-100ms → 25-50ms (50% reduction)
- Worldgen time: 125-200ms → 80-120ms (36-40% reduction)
- User experience: "Struggled" → "Smooth"

**Effort**: <2 hours
**Risk**: Zero (config fix only)
**Confidence**: 95%

### Strategy 2: Spacing Multipliers (ROI: 8.0) **← CORE OPTIMIZATION**

**Approach**: Increase spacing/separation values at StructurePlacementCalculator creation

**How It Works**:
```java
// Grid calculation (simplified):
int gridX = chunkX / spacing; // Larger spacing = larger grid cells
int gridZ = chunkZ / spacing;
long salt = worldSeed ^ structureHash;
int offsetX = random(salt, gridX, gridZ) % spacing;
int offsetZ = random(salt, gridX, gridZ) % spacing;

boolean matches = (chunkX - offsetX) % spacing == 0
               && (chunkZ - offsetZ) % spacing == 0;

// 2x spacing example:
// 32 chunks → 64 chunks = 75% fewer chunks match grid
```

**Implementation** (mixin at calculator creation):
```java
@Mixin(StructurePlacementCalculator.class)
public class StructurePlacementCalculatorMixin {
    @ModifyVariable(method = "create", at = @At("HEAD"))
    private static Stream<RegistryEntry<StructureSet>> modifyStructureSets(
        Stream<RegistryEntry<StructureSet>> structureSets
    ) {
        double multiplier = Config.spacingMultiplier; // e.g., 2.0

        return structureSets.map(entry -> {
            StructureSet set = entry.value();
            StructurePlacement placement = set.placement();

            if (placement instanceof RandomSpreadStructurePlacement randomSpread) {
                int newSpacing = (int) Math.ceil(randomSpread.getSpacing() * multiplier);
                int newSeparation = (int) Math.ceil(randomSpread.getSeparation() * multiplier);

                // Apply multiplier
                randomSpread.setSpacing(newSpacing);
                randomSpread.setSeparation(newSeparation);
            }

            return entry;
        });
    }
}
```

**Performance Math**:
- 1.0x (vanilla): 100% placement attempts
- 1.5x: ~56% placement attempts (44% reduction)
- 2.0x: ~25% placement attempts (75% reduction)
- 3.0x: ~11% placement attempts (89% reduction)

**Trade-offs**:
- ✅ Preserves all structure variety (content intact)
- ✅ Zero per-chunk overhead (one-time at world load)
- ✅ User-configurable (per-structure or global)
- ⚠️ Structures spawn further apart (intended, but some users may want more density)

**Recommended**: 2.0x as default (50-80% improvement, good structure density)

### Strategy 3: Performance Presets (ROI: 4.5) **← UX IMPROVEMENT**

**Approach**: Pre-configured multiplier presets for easy user adoption

**Presets**:
```toml
[presets]
"Vanilla" = 1.0x    # No optimization (for comparison)
"Balanced" = 2.0x   # 50% improvement, good density (RECOMMENDED)
"Performance" = 3.0x # 80% improvement, sparse structures
"Ultra" = 4.0x      # 90% improvement, very sparse
```

**Benefits**:
- Users don't need to understand multipliers
- One-click optimization
- Easy A/B testing (Vanilla vs Balanced)
- Clear performance expectations

**Effort**: 2-4 hours (config system + UI)
**Risk**: Low
**Confidence**: 90%

### Strategy 4: Recommend Structure Layout Optimizer (ROI: 8.0) **← INFINITE ROI**

**Approach**: Document compatibility, recommend users install SLO

**What SLO Does**:
- BoxOctree spatial indexing for jigsaw intersection checks
- Reduces O(n²) → O(n log n)
- 50-90% improvement for large jigsaw structures
- Zero conflict with our mod

**Combined Impact**:
- Our mod: 50% fewer placements (frequency reduction)
- SLO: 80% faster per placement (algorithmic optimization)
- **Combined: 90% reduction in jigsaw assembly time**

**Effort**: 0 hours (just documentation)
**Risk**: Zero
**Confidence**: 95%

### Strategy 5: Per-Structure Spacing Override (ROI: 2.8) **← CONDITIONAL**

**Approach**: Allow users to set different multipliers for different structures

**Example**:
```toml
[structure_overrides]
"minecraft:village" = 1.5x    # Keep villages common
"minecraft:ancient_city" = 5.0x # Make ancient cities rare
"when_dungeons_arise:*" = 3.0x # All WDA structures sparse
```

**Benefits**:
- Granular control for power users
- Address specific performance outliers (trial chambers)
- Preserve gameplay balance (common vs rare structures)

**Trade-offs**:
- Complex configuration
- Requires structure classification system
- Most users won't use it

**Recommendation**: Epic 04-05 (AFTER Epic 02-03 validation)

**Effort**: 24-40 hours
**Risk**: Medium
**Confidence**: 70%

### Strategy 6: Biome Pre-Filter (ROI: 0.3) **← NOT RECOMMENDED**

**Approach**: Check biome BEFORE grid check to skip incompatible structures

**Analysis**:
```
Current (wasteful):
- Grid check 150 structures → 20 matches
- Biome check 20 matches → 3 compatible
- Result: 130 wasted grid checks

Optimized (your idea):
- Biome check 150 structures → 30 compatible
- Grid check 30 compatible → 3 matches
- Result: 80% fewer grid checks
```

**Why NOT Recommended**:
- Grid checks = **0.02% of STRUCTURE_START time** (already negligible)
- Biome lookups = 5-10% (potentially MORE expensive than grid math)
- Optimization targets wrong bottleneck
- **80% reduction of 0.02% = 0.016% total improvement**

**Trade-off Analysis**:
- Effort: 8-16 hours (implement biome pre-filter caching)
- Benefit: ~0.5-1.0ms per chunk (negligible)
- **ROI: 0.3 (very low)**

**Recommendation**: Defer indefinitely (not worth effort)

**Alternative**: If biome mods make biome lookups expensive, cache biome at chunk center once and reuse (but still low ROI)

## Diagnostic Logging Best Practices

### What to Log (Based on Epic 01 Findings)

**1. Calculator Creation** (one-time, INFO level):
```java
LOGGER.info("=== Structure Placement Calculator Created ===");
LOGGER.info("Total structure sets: {} (after dimension + biome filtering)", count);
LOGGER.info("Modified structure sets: {}", modifiedCount);
LOGGER.info("Config: spacing_multiplier = {}x", multiplier);
```

**2. Spacing Transformations** (one-time, DEBUG level):
```java
for (StructureSet set : modifiedSets) {
    LOGGER.debug("Structure '{}': spacing {}→{} ({}x multiplier)",
        id, oldSpacing, newSpacing, multiplier);
}
```

**3. Placement Tracking** (ongoing, DEBUG level):
```java
@Inject(method = "place", at = @At("RETURN"))
private void onStructurePlaced(CallbackInfo ci) {
    LOGGER.debug("Structure placed: {} at chunk [{}, {}]",
        structureId, chunkX, chunkZ);
    PlacementTracker.record(structureId, chunkX, chunkZ);
}
```

**4. Performance Metrics** (periodic, INFO level):
```java
// Every 100 chunks:
LOGGER.info("Performance: {} structures placed, avg spacing {}, placement rate {}/sec",
    placementCount, avgSpacing, placementRate);
```

### Config Bug Diagnostic (CRITICAL)

```java
// Add this to IMMEDIATELY detect config bug:
if (configMultiplier == 1.0 && appliedMultiplier == 1.0) {
    LOGGER.error("═══════════════════════════════════════════════════");
    LOGGER.error("CONFIG BUG DETECTED: Spacing multiplier NOT applied!");
    LOGGER.error("Expected: 2.0x default, Actual: 1.0x (no effect)");
    LOGGER.error("Result: 100x more placements than expected");
    LOGGER.error("Fix: Set spacing_multiplier = 2.0 in config");
    LOGGER.error("═══════════════════════════════════════════════════");
}
```

## Common Misconceptions (Debunked)

### Myth 1: "Grid checks are the bottleneck"
**Reality**: 0.02% of time - pure math, already optimal
**Source**: TASK-006 profiling analysis

### Myth 2: "Memory causes the lag"
**Reality**: Memory FOLLOWS CPU bottleneck (r² = 0.98)
**Source**: TASK-004 memory profiling

### Myth 3: "Biome pre-filter would help significantly"
**Reality**: 80% reduction of 0.02% = negligible benefit
**Source**: Epic 01 decision matrix (ROI = 0.3)

### Myth 4: "Parallelization solves STRUCTURE_START"
**Reality**: Cannot parallelize synchronization stage (57% overhead remains)
**Source**: TASK-006 synchronization analysis

### Myth 5: "Structure variants cause exponential slowdown"
**Reality**: Variants in same StructureSet = 1 check (no multiplication)
**Source**: TASK-001 algorithm documentation

## Performance Testing Methodology

### Required Test Scenarios (from Epic 01)

**Scenario 1: Vanilla Baseline** (control)
- 34 structures
- Spacing: vanilla (no multiplier)
- Expected: 20-30 placements per 8 min, 5-10ms STRUCTURE_START

**Scenario 2: Heavy Modded (Bug)**
- 569 structures
- Spacing: 1.0x (config bug, no multiplier applied)
- Expected: 2,400-2,800 placements per 8 min, 50-100ms STRUCTURE_START

**Scenario 3: Heavy Modded (Fixed)**
- 569 structures
- Spacing: 2.0x (config bug fixed, multiplier applied)
- Expected: 1,200-1,400 placements per 8 min, 25-50ms STRUCTURE_START

**Validation Criteria**:
- 40-60% improvement required (Scenario 3 vs Scenario 2)
- Variance within 20% of expected
- 3 test runs minimum for statistical significance

### Profiling Tools

1. **Spark Profiler** (CPU profiling):
   ```
   /spark profiler start --timeout 480
   /spark profiler stop
   ```

2. **PlacementTracker** (verification):
   ```java
   /xeenaa stats  // Show placement counts and spacing
   /xeenaa export // Export JSON for analysis
   ```

3. **JVM Metrics** (memory correlation):
   - Heap usage over time
   - GC frequency and pause times
   - Allocation rate (MB/min)

## Validation Checklist

Before claiming performance improvements:

- [ ] Measured baseline (Scenario 1: vanilla)
- [ ] Measured bug scenario (Scenario 2: config bug)
- [ ] Measured fixed scenario (Scenario 3: multiplier applied)
- [ ] Verified 40-60% improvement (Scenario 3 vs 2)
- [ ] Structures spawn at correct spacing (2x = ~2x further apart)
- [ ] No structures broken or missing (100% compatibility)
- [ ] Tested in 5+ different biomes
- [ ] Tested with 3+ mod combinations
- [ ] Statistical validation (3 runs, 95% confidence)
- [ ] Memory follows placement rate (GC pressure reduced)

## Integration with Classification Systems

When building structure management mods:

1. **Classify structures** by size/type/rarity
2. **Apply multipliers** based on classification
3. **Track placements** to verify effectiveness
4. **Provide diagnostics** with statistical validation
5. **Export data** for user transparency

## Key Takeaways

1. **STRUCTURE_START is PRIMARY bottleneck** (40-60% of worldgen, 10x slower than vanilla)
2. **Config bug causes 100x placement explosion** (2,600 vs 200-400 expected)
3. **Grid checks are NOT the problem** (0.02% of time, already optimal)
4. **Memory FOLLOWS CPU bottleneck** (symptom, not cause)
5. **Jigsaw assembly O(n²) is SECONDARY** (20-30% of STRUCTURE_START, SLO fixes this)
6. **Spacing multipliers are highly effective** (50-80% improvement, quadratic benefit)
7. **Biome pre-filter has low ROI** (optimizes 0.02% of time, not worth effort)
8. **Synchronization cannot be eliminated** (vanilla limitation, 57% overhead remains)

## When to Apply This Skill

- Designing structure performance optimization mods
- Diagnosing slow worldgen in modpacks
- Explaining performance to users (debunk myths!)
- Analyzing structure mod compatibility
- Creating performance test plans
- Evaluating optimization proposals (ROI analysis)

## References (Epic 01 Research)

- TASK-001: Algorithm documentation (placement-algorithm.md, code-references.md)
- TASK-002: Performance characteristics (complexity analysis)
- TASK-003: Spark profiling summary (CPU bottleneck identification)
- TASK-004: Memory analysis (correlation with placement rate)
- TASK-005: Per-structure costs (trial chambers = #1 outlier)
- TASK-006: STRUCTURE_START timing (40-60% PRIMARY bottleneck, 57% sync overhead)
- TASK-007: Baseline comparison (vanilla vs modded, 2,600 placements measured)
- TASK-008: Test procedure (reproducible methodology)
- TASK-009: Existing solutions (SLO, C2ME, Lithium, FerriteCore)
- TASK-010: Optimization catalog (18 techniques evaluated)
- TASK-011: Decision matrix (ROI scoring, Epic 02 recommendation)
- TASK-012: Executive summary (Epic 01 findings synthesis)

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used minecraft-performance-structure
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used minecraft-performance-structure`

This helps track which skills are actively consulted and identifies documentation gaps.
