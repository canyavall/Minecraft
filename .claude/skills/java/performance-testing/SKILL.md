---
name: performance-testing
description: Performance testing methodology for Minecraft mods including baseline measurement, profiling tools, test scenarios, statistical validation, and reporting. Use when validating performance optimizations or diagnosing performance issues.
allowed-tools: [Read, Write, Bash]
---

# Performance Testing Skill

Systematic methodology for measuring, validating, and reporting performance improvements in Minecraft mods.

## Testing Methodology Overview

### Golden Rule
**"Measure twice, optimize once"** - Always confirm optimizations work with real data, not just theory.

### Testing Phases

1. **Baseline Measurement** - Measure current performance before changes
2. **Implementation** - Make optimization changes
3. **Re-testing** - Measure performance with identical conditions
4. **Statistical Validation** - Verify improvements are significant and reproducible
5. **Reporting** - Document results with evidence
6. **Continuous Monitoring** - Track long-term stability

## Phase 1: Baseline Measurement

### Test Environment Setup

**Hardware Documentation**:
- CPU: [model and speed]
- RAM: [amount and speed]
- Storage: [SSD/HDD type]
- GPU: [model] (for client testing)

**Software Configuration**:
- Minecraft Version: 1.21.1
- Fabric Loader: [version]
- Fabric API: [version]
- Java Version: 21+
- JVM Arguments: [document any custom flags]

**World Setup**:
- Create dedicated test world (seed-based for reproducibility)
- Document world type (flat, normal, amplified)
- Keep world size consistent across tests
- Use same world for all baseline and optimized tests

### Key Metrics to Measure

#### Server Performance Metrics

**TPS (Ticks Per Second)**:
- Target: 20 TPS
- Measurement: Server tick counter
- Warning: < 19 TPS indicates issues
- Critical: < 15 TPS unacceptable

**MSPT (Milliseconds Per Tick)**:
- Target: < 50ms
- Comfortable: < 45ms (10% safety margin)
- Warning: > 47ms
- Critical: > 50ms (causes TPS drop)

**Memory Usage**:
- Heap utilization over time
- Peak memory during stress tests
- Memory growth rate (leak detection)
- GC pause frequency and duration

**Chunk Operations**:
- Chunk load time (generation vs. loading)
- Chunks loaded per second
- Memory per chunk
- Chunk unload time

**GC (Garbage Collection)**:
- GC events per minute
- GC pause duration
- Minor vs. Major GC ratio
- Total GC time percentage

#### Client Performance Metrics

**FPS (Frames Per Second)**:
- Average FPS
- Minimum FPS (1% low)
- Frame time consistency

**Chunk Rendering**:
- Chunk render time
- Render distance impact
- Culling efficiency

**Memory Usage**:
- Client heap utilization
- Texture memory usage

### Test Scenarios

#### Scenario A: Chunk Loading Stress Test
**Purpose**: Measure chunk loading performance under high load

**Procedure**:
1. Create new world with specific seed
2. Set flying speed to 10 (fastest creative flight)
3. Fly in straight line for 5 minutes
4. Do not change direction or speed

**Metrics to Record**:
- Chunks loaded per second
- Average chunk load time
- Peak chunk load time
- Memory usage growth
- TPS during flight
- GC events during test

**Success Criteria**:
- Maintain 19+ TPS throughout
- Chunk load time < 20ms average
- No memory leaks (stable usage)

#### Scenario B: Existing World Loading
**Purpose**: Measure world load and chunk retrieval performance

**Procedure**:
1. Use pre-generated test world (1000+ chunks)
2. Teleport to 20 predefined locations
3. Wait 10 seconds at each location
4. Measure load time and memory

**Locations** (diverse biomes):
- Plains, Forest, Desert, Taiga, Swamp
- Mountains, Ocean, River, Jungle, Savanna
- Ice Spikes, Mesa, Mushroom, Nether, End
- Village, Stronghold, Mineshaft, Ocean Monument, End City

**Metrics to Record**:
- World initial load time
- Per-location teleport time
- Memory usage per location
- Chunk cache hit rate

#### Scenario C: Server Stress Test
**Purpose**: Measure performance under multiplayer load

**Procedure**:
1. Run dedicated server
2. Simulate 5-10 concurrent players (bots or actual players)
3. Each player performs chunk loading activities
4. Run for 30 minutes minimum

**Metrics to Record**:
- TPS under load
- MSPT distribution
- Memory growth rate
- Network bandwidth usage
- Player connection quality

**Success Criteria**:
- Maintain 19+ TPS with all players active
- No server timeouts
- Stable memory (no leaks)

#### Scenario D: Long-term Stability
**Purpose**: Detect memory leaks and performance degradation

**Procedure**:
1. Run server for 24+ hours
2. Periodic player activity simulation
3. Monitor metrics every hour

**Metrics to Record**:
- Memory usage trend
- Performance degradation over time
- Leak detection (heap growth)
- Error logs

**Success Criteria**:
- Flat memory usage (no linear growth)
- Stable TPS throughout 24 hours
- No crashes or errors

### Data Collection Template

**File**: `baseline_results.txt`

```
=== BASELINE PERFORMANCE ===
Date: [YYYY-MM-DD HH:MM]
Test Run: [1/5]
Scenario: [Chunk Loading Stress Test]
Duration: [5 minutes]

Environment:
- CPU: [model]
- RAM: [amount]
- World: [seed, type]
- Java: [version]

Results:
- Average TPS: ___
- Minimum TPS: ___
- Average MSPT: ___ ms
- Peak Memory: ___ MB
- Total Memory Growth: ___ MB
- Chunks Loaded: ___
- Average Chunk Load Time: ___ ms
- Peak Chunk Load Time: ___ ms
- GC Events: ___
- Total GC Time: ___ ms

Observations:
[Any unusual behavior, lag spikes, errors]
```

## Phase 2: Profiling Tools

### Tool 1: Spark Profiler (Recommended)

**Installation**:
1. Download Spark for Fabric 1.21.1 from https://spark.lucko.me/download
2. Place `spark-fabric-[version].jar` in `mods/` folder
3. Start server/client

**Basic Usage**:
```bash
# Start profiling
/spark profiler start

# Run your test scenario (5+ minutes)

# Stop and generate report
/spark profiler stop

# Open web interface (shows URL in chat)
/spark profiler open
```

**Advanced Options**:
```bash
# Profile for specific duration
/spark profiler --timeout 300

# Profile specific threads
/spark profiler --thread Server-Worker-*

# Only profile specific methods
/spark profiler --only-ticks-over 50

# Export results
/spark profiler export
```

**Web Interface**:
- Flame graphs showing hottest code paths
- Method call percentages
- Thread activity analysis
- Export to share with others

### Tool 2: Java Flight Recorder (JFR)

**Enable JFR**:
```bash
# Start Minecraft with JFR
java -XX:+UnlockDiagnosticVMOptions \
     -XX:+DebugNonSafepoints \
     -XX:StartFlightRecording=duration=300s,filename=baseline.jfr \
     -jar server.jar
```

**Analyze with Mission Control**:
```bash
# Open JFR file
jmc baseline.jfr
```

**Key Views**:
- Method Profiling (CPU hotspots)
- Memory (allocations, GC)
- Thread Activity (contention, waits)
- I/O Operations

### Tool 3: Minecraft Debug Profiler

**Usage**:
```bash
# In-game
/debug start
# ... run test ...
/debug stop

# Results saved to: debug/profile-results-YYYY-MM-DD_HH.MM.SS.txt
```

**Limitations**:
- Basic profiling only
- No detailed method breakdown
- Good for quick checks

### Tool 4: Custom Performance Monitoring

**Implement in your mod**:
```java
public class PerformanceProfiler {
    private static final Logger LOGGER = LoggerFactory.getLogger("PerformanceProfiler");
    private final Map<String, LongSummaryStatistics> metrics = new ConcurrentHashMap<>();

    public void startMeasurement(String operation) {
        measurements.put(operation, System.nanoTime());
    }

    public void endMeasurement(String operation) {
        Long start = measurements.remove(operation);
        if (start != null) {
            long duration = System.nanoTime() - start;
            metrics.computeIfAbsent(operation, k -> new LongSummaryStatistics())
                   .accept(duration);
        }
    }

    public void logSummary() {
        metrics.forEach((operation, stats) -> {
            LOGGER.info("{}: avg={}ms, max={}ms, count={}",
                operation,
                stats.getAverage() / 1_000_000.0,
                stats.getMax() / 1_000_000.0,
                stats.getCount()
            );
        });
    }
}

// Usage
profiler.startMeasurement("chunk_load");
// ... chunk loading code ...
profiler.endMeasurement("chunk_load");
```

### Tool 5: System Monitoring

**htop/Task Manager**:
- Monitor CPU usage per core
- Track memory usage
- Identify process bottlenecks

**JVM Monitoring Flags**:
```bash
# GC logging
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-Xloggc:gc.log

# Memory dumps on OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./heap_dumps/

# JMX for remote monitoring
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.authenticate=false
```

## Phase 3: Performance Targets

### Target Improvement Ranges

**Based on successful Minecraft optimization mods**:

| Optimization Type | Minimum | Good | Excellent |
|------------------|---------|------|-----------|
| Chunk Loading | 10% faster | 20% faster | 30%+ faster |
| Memory Usage | 5% reduction | 15% reduction | 25%+ reduction |
| TPS Stability | Fewer spikes | 19+ TPS consistent | 19.8+ TPS always |
| GC Frequency | 10% less | 25% less | 40%+ less |

### Success Criteria

#### Minimum Acceptable Improvement
- **Any single metric**: > 5% improvement
- **Primary target**: > 10% improvement
- **Statistical significance**: p < 0.05 (95% confidence)
- **Reproducibility**: Consistent across 5+ test runs

#### No-Regression Requirements
- **Compatibility**: No conflicts with major mods
- **Stability**: Zero crashes or data corruption
- **Functionality**: All vanilla features work correctly
- **Side effects**: No other metrics become worse

### Statistical Requirements

**Sample Size**: Minimum 5 test runs per scenario

**Confidence Level**: 95% confidence interval

**Reproducibility**: Results must be consistent (low variance)

**Significance Test**: Use t-test to compare baseline vs. optimized
```python
# Example statistical validation
from scipy import stats

baseline = [19.2, 19.1, 19.3, 19.0, 19.2]  # 5 TPS measurements
optimized = [19.7, 19.8, 19.6, 19.7, 19.8]

t_stat, p_value = stats.ttest_ind(baseline, optimized)
if p_value < 0.05:
    print("Improvement is statistically significant!")
```

## Phase 4: Testing Workflow

### Step-by-Step Process

**1. Run Baseline Tests** (minimum 5 runs)
```bash
# Test 1
./run_baseline_test.sh > baseline_run1.txt

# Test 2 (after restarting server)
./run_baseline_test.sh > baseline_run2.txt

# ... continue for 5 runs
```

**2. Calculate Baseline Statistics**
```
Average TPS: [mean ± std dev]
Average Chunk Load Time: [mean ± std dev]
Memory Usage: [mean ± std dev]
```

**3. Implement Optimization**
- Make ONE optimization change at a time
- Document exactly what was changed
- Build and test compilation

**4. Run Optimized Tests** (minimum 5 runs)
- Use IDENTICAL test conditions
- Same world, same scenarios
- Same hardware state (reboot if needed)

**5. Calculate Improvement**
```
Improvement = ((Optimized - Baseline) / Baseline) * 100%

Example:
Baseline chunk load: 25ms
Optimized chunk load: 20ms
Improvement: ((20 - 25) / 25) * 100% = -20% (20% faster)
```

**6. Validate Statistical Significance**
- Run t-test or similar
- Check p-value < 0.05
- Verify confidence intervals don't overlap

**7. Test for Regressions**
- Run vanilla gameplay tests
- Check for new bugs
- Verify mod compatibility

## Phase 5: Reporting

### Performance Report Template

**File**: `performance_report_[optimization_name].md`

```markdown
# Performance Improvement Report: [Optimization Name]

## Executive Summary
- **Optimization**: [Brief description]
- **Primary Metric**: [Metric improved]
- **Improvement**: [X]% faster / [Y]% less memory
- **Statistical Significance**: p = [p-value]
- **Recommendation**: ✅ Deploy / ⚠️ Needs work / ❌ Reject

## Test Configuration
- **Date**: [YYYY-MM-DD]
- **Mod Version**: [version]
- **Minecraft**: 1.21.1
- **Java**: 21
- **Hardware**: [specs]
- **Test Runs**: 5 baseline + 5 optimized

## Results Summary

| Metric | Baseline (mean ± σ) | Optimized (mean ± σ) | Improvement | Target Met? |
|--------|---------------------|----------------------|-------------|-------------|
| Chunk Load Time | 25.3ms ± 2.1ms | 20.1ms ± 1.8ms | -20.5% | ✅ |
| Memory Usage | 2.1GB ± 0.1GB | 1.9GB ± 0.1GB | -9.5% | ✅ |
| Average TPS | 19.2 ± 0.3 | 19.7 ± 0.2 | +2.6% | ✅ |
| GC Events/min | 12 ± 2 | 9 ± 1 | -25.0% | ✅ |

## Statistical Analysis
- **Sample Size**: n=5 for each condition
- **Confidence Level**: 95%
- **T-test p-value**: 0.003 (< 0.05, statistically significant)
- **Effect Size**: Cohen's d = 2.1 (large effect)

## Detailed Results

### Baseline Performance
```
Run 1: TPS=19.2, Chunk Load=25.1ms, Memory=2.1GB
Run 2: TPS=19.1, Chunk Load=26.3ms, Memory=2.0GB
Run 3: TPS=19.3, Chunk Load=24.8ms, Memory=2.2GB
Run 4: TPS=19.0, Chunk Load=25.9ms, Memory=2.1GB
Run 5: TPS=19.2, Chunk Load=24.5ms, Memory=2.1GB
```

### Optimized Performance
```
Run 1: TPS=19.7, Chunk Load=19.8ms, Memory=1.9GB
Run 2: TPS=19.8, Chunk Load=20.5ms, Memory=1.8GB
Run 3: TPS=19.6, Chunk Load=20.3ms, Memory=2.0GB
Run 4: TPS=19.7, Chunk Load=19.9ms, Memory=1.9GB
Run 5: TPS=19.8, Chunk Load=20.0ms, Memory=1.9GB
```

## Implementation Details
**What was changed**:
[Detailed description of the optimization]

**Code location**:
- File: `[path]`
- Lines: [line numbers]

**Approach**:
[Explanation of why this optimization works]

## Validation
- ✅ No crashes or errors
- ✅ All vanilla features work
- ✅ Compatible with Fabric API
- ✅ No mod conflicts detected
- ✅ 24-hour stability test passed

## Trade-offs
**Benefits**:
- [List benefits]

**Costs**:
- [Any downsides, increased complexity, etc.]

## Recommendations
**Deployment**: ✅ Ready for production

**Next Steps**:
1. Merge optimization to main branch
2. Add to release notes
3. Monitor user feedback
4. Consider additional optimizations

## Appendices

### Spark Profiler Results
[Link to Spark flame graphs]

### JFR Analysis
[Key findings from Java Flight Recorder]

### Test Logs
[Links to full test logs]
```

## Phase 6: Continuous Monitoring

### Regression Detection

**Automated Performance Tests**:
```bash
#!/bin/bash
# Run nightly performance tests

# Build mod
./gradlew build

# Run test suite
./gradlew test

# Run performance benchmarks
./scripts/performance_benchmark.sh

# Compare with baseline
./scripts/compare_to_baseline.sh

# Alert if > 5% regression
if [ $REGRESSION -gt 5 ]; then
    echo "ALERT: Performance regression detected!"
    # Send notification
fi
```

**Performance Thresholds**:
- TPS: Alert if < 19.0
- MSPT: Alert if > 47ms
- Memory: Alert if > 10% increase
- Chunk load: Alert if > 5% slower

### Real-World Validation

**Beta Testing**:
1. Deploy to test server with real players
2. Monitor metrics for 1 week
3. Collect player feedback
4. Verify no issues in production

**Metrics to Track**:
- Player-reported lag
- Server crashes
- TPS stability over time
- Memory usage trends
- Mod conflicts

## Quick Reference: Commands

### Spark Profiler
```bash
/spark profiler start              # Start profiling
/spark profiler stop               # Stop and save
/spark profiler --timeout 300      # Profile for 5 minutes
/spark profiler open               # Open web interface
```

### Minecraft Debug
```bash
/debug start                       # Start debug profiler
/debug stop                        # Stop and save
```

### Server Performance
```bash
/forge tps                         # Check TPS (if Forge)
/perf start                        # Some mods provide this
```

### JVM Monitoring
```bash
# GC logs
-Xloggc:gc.log

# Heap dump
jmap -dump:live,format=b,file=heap.bin <pid>

# Thread dump
jstack <pid> > threads.txt
```

## When to Use This Skill

Use this skill when:
- Validating performance optimizations
- Measuring baseline performance before changes
- Creating performance test plans
- Reporting optimization results
- Diagnosing performance issues
- Setting up profiling infrastructure
- Comparing before/after performance
- Questions about "How do I measure X?"
- Questions about "Is my optimization working?"
- Questions about "What tools should I use to profile?"

## Key Principles

1. **Measure First** - Always baseline before optimizing
2. **Control Variables** - Same conditions for baseline and optimized tests
3. **Statistical Rigor** - Multiple runs, confidence intervals, significance tests
4. **Document Everything** - Record all configuration and results
5. **One Change at a Time** - Test optimizations individually
6. **Real-World Testing** - Synthetic benchmarks + real gameplay
7. **No Premature Optimization** - Profile first, optimize bottlenecks only
8. **Reproducibility** - Results must be consistent across runs
9. **No Regressions** - Verify nothing breaks
10. **Share Results** - Help the community with transparent reporting
