# Performance Profiling and Analysis Tools

**Last Updated**: 2025-10-27
**Minecraft Versions**: 1.17.1 - 1.21.x
**Platform**: Fabric (with some cross-loader tools)
**Tags**: performance, profiling, spark, jvm, benchmarking, tools, optimization, monitoring

## Overview

Comprehensive comparison of performance analysis tools for Minecraft mod development, focusing on Fabric compatibility and production suitability.

---

## Tool Comparison Matrix

| Tool | Type | Platform Support | MC 1.21.1 Compatible | Performance Overhead | License |
|------|------|------------------|----------------------|---------------------|---------|
| **Spark** | Profiler | All | ✅ Yes | Minimal (production safe) | GPL v3 |
| **async-profiler** | Native Profiler | Linux/macOS | ✅ Yes (via Spark) | Ultra-low | Apache 2.0 |
| **JProfiler** | Commercial Profiler | All | ✅ Yes | Low-Medium | Commercial |
| **JFR** | Built-in Profiler | All | ✅ Yes | Very Low | Oracle Binary |
| **VisualVM** | Free Profiler | All | ✅ Yes | Medium | GPL v2 |
| **Eclipse MAT** | Memory Analyzer | All | ✅ Yes | N/A (heap analysis) | EPL |
| **JConsole** | JVM Monitor | All | ✅ Yes | Low | Oracle Binary |
| **JMH** | Benchmarking | All | ✅ Yes | N/A (testing only) | GPL v2 |

---

## Fabric-Specific Performance Mods

| Tool | Purpose | Compatibility | Key Features | Impact |
|------|---------|---------------|--------------|--------|
| **C2ME** | Chunk Loading | 1.17.1 - 1.21.x | Parallel chunk generation, 70% worldgen speedup | High |
| **Lithium** | General Optimization | Current versions | Game logic optimization, server/client | High |
| **Starlight** | Lighting Engine | 1.17.x - 1.20.4 | Complete lighting rewrite (archived) | High |
| **ScalableLux** | Lighting Engine | 1.21+ | Starlight fork for newer versions | High |
| **Sodium** | Rendering | 1.16.1 - 1.21.x | Graphics engine replacement | Very High |

---

## Metrics Capability Matrix

| Tool | CPU Profiling | Memory Analysis | Chunk Loading | Tick Performance | GC Monitoring | Thread Analysis |
|------|---------------|-----------------|---------------|------------------|---------------|-----------------|
| **Spark** | ✅ Excellent | ✅ Good | ✅ Good | ✅ Excellent | ✅ Good | ✅ Excellent |
| **async-profiler** | ✅ Excellent | ✅ Excellent | ✅ Good | ✅ Good | ✅ Good | ✅ Excellent |
| **JProfiler** | ✅ Excellent | ✅ Excellent | ✅ Limited | ✅ Good | ✅ Excellent | ✅ Excellent |
| **JFR** | ✅ Good | ✅ Good | ✅ Limited | ✅ Good | ✅ Excellent | ✅ Good |
| **VisualVM** | ✅ Good | ✅ Good | ✅ Limited | ✅ Limited | ✅ Good | ✅ Good |
| **Eclipse MAT** | ❌ No | ✅ Excellent | ❌ No | ❌ No | ❌ No | ❌ No |
| **JConsole** | ✅ Limited | ✅ Good | ❌ No | ✅ Limited | ✅ Good | ✅ Limited |

---

## Ease of Integration

| Tool | Setup Complexity | IDE Integration | Automation Support | Learning Curve |
|------|-----------------|------------------|-------------------|----------------|
| **Spark** | Very Easy | N/A (in-game) | Good | Low |
| **async-profiler** | Easy | Limited | Excellent | Medium |
| **JProfiler** | Easy | Excellent | Good | Medium |
| **JFR** | Easy | Good | Good | Medium |
| **VisualVM** | Easy | Limited | Limited | Low |
| **Eclipse MAT** | Easy | Excellent | Limited | High |
| **JConsole** | Very Easy | Limited | Limited | Low |
| **JMH** | Medium | Good | Excellent | High |

---

## Performance Impact Assessment

| Tool | CPU Overhead | Memory Overhead | Network Impact | Production Suitable |
|------|--------------|-----------------|----------------|-------------------|
| **Spark** | <1% | <50MB | Minimal | ✅ Yes |
| **async-profiler** | <0.5% | <20MB | None | ✅ Yes |
| **JProfiler** | 2-5% | 100-500MB | Low | ⚠️ Limited |
| **JFR** | <1% | <100MB | None | ✅ Yes |
| **VisualVM** | 5-10% | 200-1GB | Medium | ❌ No |
| **Eclipse MAT** | N/A | N/A | None | ✅ Offline only |
| **JConsole** | 1-2% | <50MB | Low | ⚠️ Limited |

---

## Recommended Tool Combinations

### For Development Workflow
1. **Primary**: Spark + C2ME + Lithium + ScalableLux
2. **Memory Issues**: Eclipse MAT + JProfiler
3. **Benchmarking**: JMH + async-profiler
4. **Production Monitoring**: Spark + JFR

### For Chunk Performance Analysis
1. **Real-time**: Spark profiler
2. **Development**: C2ME with debug logging
3. **Benchmarking**: JMH with custom chunk loading tests
4. **Memory**: Eclipse MAT for heap analysis

### For Client-Side Performance
1. **Rendering**: Sodium + Spark
2. **General**: Lithium + Spark
3. **Memory**: VisualVM + Eclipse MAT

### For Server Performance
1. **Primary**: Spark + Lithium
2. **Chunk Gen**: C2ME + Spark
3. **Memory Leaks**: Eclipse MAT
4. **Continuous Monitoring**: JFR + JConsole

---

## Tool-Specific Details

### Spark (Recommended Primary Tool)

**Why Spark?**
- Designed specifically for Minecraft
- Minimal performance impact (<1% overhead)
- Comprehensive metrics for server/client
- Active development and community support
- Excellent for chunk loading analysis
- In-game commands for profiling
- Web viewer for analysis

**Installation**:
```bash
# Add to mods folder
spark-fabric-1.21.jar
```

**Key Commands**:
```
/spark profiler start
/spark profiler stop
/spark health
/spark tps
```

**Best For**:
- Real-time performance monitoring
- Identifying tick lag sources
- Thread analysis
- Production environments
- Quick diagnostics

### async-profiler (For Deep CPU Analysis)

**Why async-profiler?**
- Ultra-low overhead (<0.5%)
- Native profiling (no JVM overhead)
- Flame graphs
- Production-safe
- Works through Spark integration

**Best For**:
- Deep CPU hotspot analysis
- Production profiling
- Minimal impact profiling
- Native code analysis

### JProfiler (For Development)

**Why JProfiler?**
- Comprehensive commercial tool
- Excellent IDE integration
- Memory leak detection
- Thread analysis
- Call tree analysis

**Best For**:
- Development environment
- Complex memory issues
- Detailed thread analysis
- Not for production

### JFR (Java Flight Recorder)

**Why JFR?**
- Built into JDK
- Very low overhead
- Continuous recording
- Production-safe
- No dependencies

**Activation**:
```bash
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr
```

**Best For**:
- Long-running production profiling
- JVM-level metrics
- GC analysis
- Continuous monitoring

### Eclipse MAT (For Memory Issues)

**Why Eclipse MAT?**
- Industry standard for heap analysis
- Excellent leak detection
- Visual memory analysis
- Dominator trees
- Retained heap calculation

**Best For**:
- Memory leak investigation
- OutOfMemoryError analysis
- Object retention analysis
- Post-mortem heap dumps

### JMH (For Benchmarking)

**Why JMH?**
- Gold standard for microbenchmarking
- Statistical rigor
- JIT warmup handling
- Prevents optimization tricks
- Reproducible results

**Best For**:
- Comparing optimization approaches
- Algorithm performance testing
- Before/after measurements
- Rigorous performance validation

---

## Profiling Workflow

### Step 1: Identify Performance Problem

Use lightweight tools first:
```bash
/spark tps          # Check TPS
/spark health       # Check thread health
```

### Step 2: Profile with Spark

```bash
/spark profiler start
# Wait 30-60 seconds during lag
/spark profiler stop
# Analyze flame graph
```

### Step 3: Deep Dive (If Needed)

**For CPU issues**: async-profiler via Spark
**For memory issues**: Take heap dump, analyze with Eclipse MAT
**For specific code**: JProfiler in development

### Step 4: Benchmark Solutions

Use JMH to validate optimizations:
```java
@Benchmark
public void testOptimization() {
    // Code to benchmark
}
```

### Step 5: Verify in Production

Monitor with Spark + JFR for ongoing validation

---

## Common Performance Bottlenecks

### Chunk Generation
- **Tool**: Spark + C2ME debug logs
- **Look For**: World generation threads, structure placement
- **Solution**: Parallel generation, caching, optimization mods

### Tick Performance
- **Tool**: Spark tick profiler
- **Look For**: Entity ticks, block entity ticks, scheduled ticks
- **Solution**: Reduce entity count, optimize logic, batch operations

### Memory Leaks
- **Tool**: Eclipse MAT
- **Look For**: Growing collections, retained objects, strong references
- **Solution**: Proper cleanup, weak references, lifecycle management

### Rendering
- **Tool**: Sodium + Spark
- **Look For**: Draw calls, shader compilation, vertex uploads
- **Solution**: Batching, culling, LOD, Sodium optimizations

### Thread Contention
- **Tool**: Spark thread analyzer
- **Look For**: Lock contention, thread waiting, synchronization
- **Solution**: Reduce locking, async operations, lock-free data structures

---

## Best Practices

### During Development
1. Profile early and often
2. Use Spark as default profiler
3. Benchmark optimizations with JMH
4. Check memory with Eclipse MAT periodically
5. Keep performance mods (Lithium, C2ME) installed

### Before Release
1. Full profiling session with Spark
2. Memory leak check with heap dumps
3. Load testing with multiple players
4. Chunk generation testing
5. Performance comparison vs baseline

### In Production
1. Enable Spark for diagnostics
2. Use JFR for continuous monitoring
3. Monitor logs for warnings
4. Regular heap dump analysis
5. Player feedback on lag

---

## Integration with Mod Development

### Gradle Setup for JMH

```gradle
dependencies {
    testImplementation 'org.openjdk.jmh:jmh-core:1.36'
    testAnnotationProcessor 'org.openjdk.jmh:jmh-generator-annprocess:1.36'
}
```

### Spark as Development Dependency

```gradle
modImplementation "me.lucko:spark-fabric:1.10.53"
```

### Performance Testing Framework

1. **Unit Tests**: Standard JUnit tests
2. **Benchmarks**: JMH for critical paths
3. **Integration Tests**: Spark in dev environment
4. **Load Tests**: Spark on test server

---

## Key Findings Summary

### Best Overall Choice: Spark
- Minecraft-specific design
- Minimal performance impact
- Comprehensive server/client metrics
- Active development
- Production-safe

### Best for Deep Analysis: async-profiler + JProfiler
- async-profiler for production
- JProfiler for development
- Complementary strengths

### Best for Memory: Eclipse MAT
- Industry standard
- Excellent leak detection
- Essential for memory work

### Best for Benchmarking: JMH
- Statistical rigor
- Reproducible results
- Required for optimization validation

---

## Related Knowledge

- `.claude/knowledge/performance/threading.md` - Thread optimization
- `.claude/knowledge/performance/benchmarking.md` - Benchmark methodologies
- `.claude/knowledge/minecraft/world-generation.md` - Chunk generation specifics
