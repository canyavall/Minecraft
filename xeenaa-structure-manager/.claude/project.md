# Xeenaa Structure Manager

**Minecraft Version**: 1.21.1
**Mod Loader**: Fabric
**Java Version**: 21
**Created**: 2025-10-22
**Author**: Canya
**Type**: Performance Optimization Mod

## Project Vision

The Xeenaa Structure Manager is a **comprehensive performance optimization mod** designed to solve the critical performance bottleneck that occurs when players use multiple structure-adding mods together. When 10-15+ structure mods are loaded (creating 500+ structures), world generation becomes extremely slow due to exponential increases in placement checks, jigsaw assembly overhead, biome filtering, and synchronization bottlenecks.

**Core Philosophy**: Attack structure performance problems at **every level** - from algorithm optimization to caching strategies to smart filtering. This is not a single-fix mod, but a comprehensive optimization suite targeting all identified bottlenecks.

**What Makes This Different**: Unlike simple spacing multiplier mods, we implement **multiple complementary optimization techniques** to address the root causes of performance degradation, not just symptoms.

## Goals

**Primary Goal**: **Dramatically improve world generation performance** when many structure mods are loaded (target: 50-70% reduction in structure-related overhead)

**Secondary Goals**:
- **Optimize placement checks** - Reduce redundant calculations and cache results
- **Improve biome filtering** - Smart pre-filtering and efficient biome queries
- **Optimize jigsaw assembly** - Address O(n²) intersection check problems
- **Reduce STRUCTURE_START overhead** - Minimize synchronization blocking in worldgen pipeline
- **Cache structure data** - Avoid redundant registry lookups and calculations
- **Smart placement rate control** - Reduce unnecessary placement attempts
- **Maintain 100% compatibility** - Work transparently with all structure mods without requiring changes

## Performance Problems This Mod Solves

### Problem 1: Search Space Explosion
- **Vanilla**: 34 structures → ~34 placement checks per chunk
- **Modded**: 569 structures → ~569 placement checks per chunk (17x increase)
- **Our Solution**: Caching, smart filtering, early rejection optimization

### Problem 2: STRUCTURE_START Synchronization Bottleneck
- **Problem**: STRUCTURE_START worldgen stage blocks all downstream stages
- **Impact**: With 569 structures, STRUCTURE_START takes 40-50% of worldgen time (vs 10-20% vanilla)
- **Our Solution**: Asynchronous processing, batch optimization, pipeline improvements

### Problem 3: Jigsaw Assembly O(n²) Complexity
- **Problem**: Large jigsaw structures (50+ pieces) cause exponential intersection checks
- **Impact**: Villages, dungeons, and complex structures take 2-50ms each to generate
- **Our Solution**: Spatial partitioning, intelligent intersection checking, assembly caching

### Problem 4: Redundant Biome Filtering
- **Problem**: Biome checks repeated for every structure on every chunk
- **Impact**: Thousands of redundant biome lookups per chunk
- **Our Solution**: Biome pre-filtering, chunk-level caching, smart invalidation

### Problem 5: Excessive Placement Attempts
- **Problem**: Structures attempt placement even when conditions will never be met
- **Impact**: 4-8x more placement attempts than necessary
- **Our Solution**: Probability-based rejection, terrain pre-validation, smart rate limiting

### Problem 6: Memory Allocation Pressure
- **Problem**: Temporary object creation during placement checks causes GC overhead
- **Impact**: GC pauses during worldgen
- **Our Solution**: Object pooling, immutable caching, allocation reduction

### Problem 7: Registry Lookup Overhead
- **Problem**: Structure registry lookups on every check
- **Impact**: Hash map overhead multiplied by 569 structures
- **Our Solution**: Structure metadata caching, indexed lookups

### Problem 8: NBT Serialization Bottleneck
- **Problem**: Structure data serialization during chunk saves
- **Impact**: Disk I/O spikes during saves
- **Our Solution**: Lazy serialization, delta compression, batch writes

## Technical Stack

### Dependencies
- **Fabric API**: Latest for 1.21.1
- **Fabric Loader**: Latest (0.16.7+)
- **Java**: 21

### Build System
- **Gradle**: 8.x
- **Fabric Loom**: Latest

### Profiling Tools
- **Spark Profiler**: CPU profiling during development
- **Java Flight Recorder (JFR)**: Memory and GC profiling
- **Custom instrumentation**: Per-optimization measurements

## Architecture Overview

### Package Structure
```
src/main/java/com/canya/xeenaa_structure_manager/
├── cache/           # Caching systems (biome cache, placement cache, metadata cache)
├── optimization/    # Core optimization implementations
│   ├── placement/   # Placement check optimizations
│   ├── jigsaw/      # Jigsaw assembly optimizations
│   ├── biome/       # Biome filtering optimizations
│   └── pipeline/    # STRUCTURE_START pipeline optimizations
├── config/          # Configuration system (toggle optimizations, debug mode)
├── tracking/        # Performance tracking and metrics
├── profiling/       # Custom profiling instrumentation
└── mixin/           # Mixins for hooking into structure generation
```

### Core Optimization Systems

**To be defined through epics based on research findings**

Potential systems (will be validated through Epic 01 research):
1. **Placement Cache System** - Cache placement eligibility decisions
2. **Biome Pre-Filter** - Smart biome-based filtering before expensive checks
3. **Jigsaw Spatial Partitioner** - Optimize intersection checks with spatial data structures
4. **Pipeline Batch Processor** - Reduce STRUCTURE_START synchronization overhead
5. **Metadata Cache** - Pre-compute and cache structure metadata
6. **Probability Gate** - Early rejection based on placement probability
7. **Memory Pool Manager** - Object pooling for temporary allocations
8. **Lazy NBT Serializer** - Reduce serialization overhead

## Development Guidelines

### Code Standards
- Follow `coding-standards` skill
- Java 21 modern features (records, pattern matching, text blocks)
- Fabric events over mixins when possible
- Client-server separation (metrics on client, optimization on server)

### Performance Standards
- **Every optimization must be measured** - Before/after profiling required
- **Minimum improvement threshold** - 5% improvement to justify complexity
- **Regression testing** - Ensure optimizations don't break compatibility
- **Toggle-able** - All optimizations can be disabled via config for debugging

### Epic Organization
- **Epic 01**: Research and measurement (understand ALL bottlenecks)
- **Epic 02-08+**: Each targets a specific bottleneck with proven optimization
- Epic requirements define WHAT (business value, performance target)
- Epic tasks define HOW (technical implementation, measurement)
- User validates requirements AND measurements before moving forward

## Project Scope

### In Scope

**Performance Optimizations** (Primary):
- ✅ Placement check optimization (caching, early rejection)
- ✅ Biome filtering optimization (pre-filtering, caching)
- ✅ Jigsaw assembly optimization (spatial partitioning, algorithm improvements)
- ✅ STRUCTURE_START pipeline optimization (async, batching)
- ✅ Memory optimization (object pooling, allocation reduction)
- ✅ Registry lookup optimization (metadata caching)
- ✅ NBT serialization optimization (lazy writes, compression)

**Supporting Features**:
- ✅ Performance tracking and metrics (measure improvements)
- ✅ Configuration system (toggle optimizations, adjust parameters)
- ✅ Debug mode (detailed logging for troubleshooting)
- ✅ Compatibility layer (work with all structure mods)
- ✅ Profiling instrumentation (measure specific bottlenecks)

**Optional Features** (If time permits):
- Structure classification for smarter optimization
- User-facing performance dashboard
- Per-structure optimization profiles

### Out of Scope

- ❌ **User-facing structure control** - Not a structure management/customization mod
- ❌ **Spacing multipliers only** - Too simplistic, we're doing comprehensive optimization
- ❌ **Custom structure generation** - Only optimize existing structures
- ❌ **Terrain modification** - Only structure generation optimization
- ❌ **Biome changes** - Structures stay in their intended biomes
- ❌ **Structure editing** - Only optimize placement and generation

## Performance Targets

This is a **performance-first mod**. Every optimization must be measured and validated:

### Minimum Viable Product (MVP)
- **Target**: 30-40% reduction in structure-related worldgen overhead
- **Scenario**: 500+ structures from 10-15 structure mods
- **Measurement**: Spark profiler showing STRUCTURE_START time reduction

### Ideal Performance
- **Target**: 50-70% reduction in structure-related worldgen overhead
- **Scenario**: 500+ structures from 10-15 structure mods
- **Measurement**: Spark profiler + JFR showing comprehensive improvements

### Success Metrics
- **STRUCTURE_START time**: 50%+ reduction
- **Chunk generation time**: 30%+ reduction overall
- **Memory usage**: No increase (or 10%+ reduction)
- **GC pressure**: 20%+ reduction in allocations
- **TPS impact**: Minimal to zero during worldgen
- **Compatibility**: 100% compatibility with all tested structure mods

### Testing Requirements
- **Baseline**: Profile vanilla (34 structures)
- **Comparison**: Profile modded without our mod (569 structures)
- **Optimized**: Profile modded with our mod (569 structures)
- **Statistical validity**: Multiple test runs, consistent results
- **Real-world testing**: Server with 10+ players generating new chunks

## Compatibility

- **Minecraft Versions**: 1.21.1 (may expand to other versions later)
- **Mod Compatibility**: Universal compatibility with any mod/datapack adding structures via standard Minecraft systems
- **Server/Client**: Server-side optimization (no client required, but metrics visible on client)
- **Multiplayer**: Full server support, optimizations benefit all players

## Special Considerations

### Performance-First Design Principles
Every system must be designed with performance in mind:
- **Measure first** - Profile before optimizing
- **Cache aggressively** - Avoid redundant calculations
- **Lazy evaluation** - Compute only when needed
- **Efficient data structures** - Choose structures based on access patterns
- **Minimal allocations** - Reduce GC pressure
- **Early rejection** - Fail fast when possible
- **Spatial partitioning** - Use spatial data structures for geometric queries
- **Batch processing** - Reduce overhead through batching

### Research-Driven Development
- **Epic 01 is critical** - Understand ALL bottlenecks before fixing
- **Validate assumptions** - Every optimization must be measured
- **Learn from others** - Research existing performance mods
- **Document findings** - Update skills with performance insights
- **Iterate based on data** - Let profiling guide priorities

### Compatibility Strategy
The mod must work transparently with:
- Vanilla Minecraft structures
- Fabric mods using standard registries
- Datapacks using standard structure definitions
- Legacy mod structures
- Future structure mods (forward compatibility)

**Approach**: Hook at the right abstraction level (Minecraft's structure placement pipeline, not mod-specific code)

## Epic Status

**Current**: Starting fresh with comprehensive research approach

**Next Steps**:
1. Run `/create_epic` to create Epic 01 with proper scope
2. Epic 01 will research ALL bottlenecks and optimization opportunities
3. Epic 02+ will implement optimizations based on Epic 01 findings

## Research Investment

**Time already invested** (valuable assets):
- ✅ Structure placement algorithm understanding (TASK-001 research)
- ✅ Root cause analysis (8 bottlenecks identified)
- ✅ External research (ChatGPT, DeepSeek analysis)
- ✅ Performance test data (baseline measurements)
- ✅ Existing mods research (Structure Layout Optimizer, etc.)

**This research guides our epic creation and implementation strategy.**

## Notes

- This is a **comprehensive optimization mod**, not a simple config mod
- We target **multiple bottlenecks simultaneously** for maximum impact
- Every optimization must be **measured and validated**
- We learn from **existing performance mods** and improve upon them
- All performance insights are **documented in Claude Code skills** for future projects
- This mod is **research-driven** - we understand before we optimize
