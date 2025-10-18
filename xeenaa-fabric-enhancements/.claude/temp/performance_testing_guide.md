# Performance Testing Guide for Xeenaa Fabric Enhancements

## Overview
This guide provides a systematic approach to measure and validate performance improvements in your Minecraft Fabric mod.

## Phase 1: Baseline Measurement (BEFORE implementing optimizations)

### 1.1 Test Environment Setup
- **Minecraft Version**: 1.21.1
- **Java Version**: 21+
- **Hardware**: Document your system specs
- **World Type**: Create a consistent test world
- **Fabric Version**: 0.16.7 with Fabric API 0.116.0+1.21.1

### 1.2 Key Metrics to Measure

#### Server Performance
- **TPS (Ticks Per Second)**: Target 20 TPS
- **MSPT (Milliseconds Per Tick)**: Target <50ms
- **Memory Usage**: Heap utilization over time
- **Chunk Loading Time**: Time to load/generate chunks
- **GC Frequency**: Garbage collection events

#### Client Performance
- **FPS**: Frames per second
- **Frame Time**: Milliseconds per frame
- **Memory Usage**: Client-side heap usage
- **Chunk Rendering**: Time to render loaded chunks

### 1.3 Testing Scenarios

#### Scenario A: Chunk Loading Stress Test
1. Create new world
2. Fly in straight line for 5 minutes at speed 10
3. Measure: chunk load times, memory usage, TPS
4. Record: chunks loaded per second, memory allocation

#### Scenario B: Populated World Test
1. Load existing world with structures
2. Teleport to different biomes (10 locations)
3. Measure: world load times, memory usage
4. Record: average load time per teleport

#### Scenario C: Server Load Test
1. Run dedicated server
2. Multiple players (or simulate with bots)
3. Measure: TPS under load, memory growth
4. Record: performance degradation curve

## Phase 2: Measurement Tools

### 2.1 Built-in Tools
```bash
# In-game commands
/debug start    # Start profiling
/debug stop     # Stop and save profile

# JVM monitoring
-XX:+FlightRecorder
-XX:StartFlightRecording=duration=300s,filename=baseline.jfr
```

### 2.2 External Tools
- **Spark Profiler**: Install and configure
- **JProfiler/YourKit**: For detailed analysis
- **htop/Task Manager**: System resource monitoring

### 2.3 Custom Measurement Code
```java
// Add to mod for precise measurements
public class PerformanceProfiler {
    private static final int MEASUREMENT_INTERVAL = 20; // ticks
    private long lastMeasurement = System.nanoTime();

    public void measureChunkLoadTime(ChunkPos pos) {
        long start = System.nanoTime();
        // ... chunk loading code ...
        long duration = System.nanoTime() - start;
        recordMetric("chunk_load_time", duration);
    }
}
```

## Phase 3: Data Collection

### 3.1 Baseline Data Collection
Run each test scenario 5 times and collect:

#### Test Results Template
```
Test: Chunk Loading Stress Test - Run 1
Date: [DATE]
Duration: 5 minutes
Results:
- Average TPS: ___
- Min TPS: ___
- Average MSPT: ___
- Peak Memory: ___ MB
- Chunks loaded: ___
- Average chunk load time: ___ ms
- GC events: ___
```

### 3.2 Performance Targets
Based on community standards and your baseline:
- **TPS**: Maintain 20 TPS under normal load
- **Memory**: <20% increase from baseline
- **Chunk Loading**: 15-25% faster than vanilla
- **GC Pressure**: Reduce frequency by 10-30%

## Phase 4: Implementation and Re-testing

### 4.1 After Implementing Optimizations
1. Run identical test scenarios
2. Use same test world/conditions
3. Collect same metrics
4. Compare with baseline data

### 4.2 Statistical Validation
- Run tests multiple times (minimum 5 runs)
- Calculate average, standard deviation
- Use t-test for statistical significance
- Ensure consistent improvement across runs

## Phase 5: Reporting

### 5.1 Performance Report Template
```markdown
# Performance Improvement Report

## Summary
- **Optimization**: [Name of optimization]
- **Target Metric**: [Primary metric improved]
- **Improvement**: [X]% faster/[Y]% less memory

## Test Results
| Metric | Baseline | Optimized | Improvement |
|--------|----------|-----------|-------------|
| TPS    | 19.2     | 19.8      | +3.1%       |
| MSPT   | 47ms     | 41ms      | -12.8%      |
| Memory | 2.1GB    | 1.9GB     | -9.5%       |

## Validation
- Tests run: 5 times each
- Statistical significance: p < 0.05
- Consistent across scenarios: Yes/No
```

## Phase 6: Continuous Monitoring

### 6.1 Regression Detection
- Set up automated performance tests
- Monitor for performance regressions
- Alert when metrics deviate >5% from targets

### 6.2 Real-world Validation
- Deploy to test server
- Monitor player feedback
- Track long-term stability

## Tools and Commands

### Quick Performance Check
```bash
# Start Minecraft with performance monitoring
./gradlew runServer --args="--nogui --profiler spark"

# Generate performance report
/spark profiler --timeout 60 --thread *
```

### Memory Analysis
```bash
# JVM flags for memory analysis
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./heap_dumps/
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
```

### Automated Testing Script
```bash
#!/bin/bash
# Run performance test suite
echo "Starting performance tests..."
for i in {1..5}; do
    echo "Run $i/5"
    ./gradlew runServer --args="--test-scenario chunk-loading"
    sleep 30
done
echo "Tests complete. Analyze results in ./performance_reports/"
```

## Expected Results
Based on common Minecraft optimizations:
- **Chunk Loading**: 15-25% improvement
- **Memory Usage**: 10-20% reduction
- **TPS Stability**: More consistent 20 TPS
- **GC Pressure**: 20-30% fewer collections

Remember: Performance improvements should be measurable, reproducible, and statistically significant.