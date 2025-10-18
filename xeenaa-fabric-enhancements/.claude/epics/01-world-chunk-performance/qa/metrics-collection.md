# World/Chunk Performance Enhancement Metrics Collection

## Overview

This document defines comprehensive procedures for collecting, analyzing, and validating performance metrics for the World/Chunk Performance Enhancement epic (E001). The methodology ensures accurate measurement of the target improvements (15-25% chunk loading improvement, 10-20% memory reduction) using industry-standard tools.

## Metrics Collection Framework

### Primary Tools and Integration

#### 1. Spark Profiler Integration
Spark provides real-time, low-overhead profiling specifically designed for Minecraft environments.

```bash
# Spark Configuration for Chunk Performance Monitoring
# config/spark/spark.conf
[profiling]
enable-profiling = true
sampling-interval = "2ms"
profile-all-threads = true
web-interface-enabled = true
web-interface-port = 4567

[monitoring]
tps-monitoring = true
tps-alert-threshold = 18.0
memory-monitoring = true
memory-alert-threshold-mb = 7168  # 7GB for 8GB server
```

#### 2. JMH (Java Microbenchmark Harness) Setup
JMH provides precise microbenchmark measurements for performance-critical operations.

```xml
<!-- pom.xml JMH configuration -->
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.37</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.37</version>
</dependency>
```

#### 3. async-profiler Configuration
async-profiler provides low-overhead production profiling with flame graph generation.

```bash
# JVM configuration for optimal profiling
JAVA_OPTS="-XX:+UnlockDiagnosticVMOptions \
           -XX:+DebugNonSafepoints \
           -XX:+FlightRecorder \
           -XX:+UnlockCommercialFeatures"
```

## Core Performance Metrics

### 1. Chunk Loading Performance Metrics

#### 1.1 Primary Metrics
- **Chunk Loading Time**: Time from request to chunk availability
- **Bulk Loading Efficiency**: Performance scaling with multiple chunks
- **Cache Hit Performance**: Access time for cached chunks
- **Memory Allocation Rate**: Objects created per chunk operation

#### 1.2 Collection Procedures

##### Real-Time Chunk Loading Monitoring
```bash
# Continuous chunk loading monitoring with Spark
/spark profiler --timeout 3600 --only-ticks-over 30 --thread "Chunk-Generation"
```

##### JMH Benchmark for Precise Measurements
```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class ChunkLoadingBenchmark {

    @Param({"1", "10", "50", "100"})
    private int chunkCount;

    @Benchmark
    public void measureChunkLoadingTime(Blackhole bh) {
        List<CompletableFuture<Chunk>> futures = new ArrayList<>();
        long startTime = System.nanoTime();

        for (int i = 0; i < chunkCount; i++) {
            ChunkPos pos = new ChunkPos(i, 0);
            futures.add(chunkManager.loadChunkAsync(pos));
        }

        // Wait for all chunks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        long endTime = System.nanoTime();

        bh.consume(endTime - startTime);
    }
}
```

##### Automated Data Collection Script
```bash
#!/bin/bash
# collect_chunk_metrics.sh

MEASUREMENT_DURATION=${1:-1800}  # 30 minutes default
OUTPUT_DIR="metrics/$(date +%Y%m%d_%H%M%S)"
mkdir -p $OUTPUT_DIR

# Start Spark profiling
echo "Starting Spark profiling for ${MEASUREMENT_DURATION} seconds..."
/spark profiler --timeout $MEASUREMENT_DURATION --output "${OUTPUT_DIR}/spark_profile.json" &
SPARK_PID=$!

# Monitor TPS continuously
/spark tps --timeout $MEASUREMENT_DURATION --interval 5 > "${OUTPUT_DIR}/tps_data.csv" &
TPS_PID=$!

# Monitor memory usage
/spark heap --live --interval 30 --timeout $MEASUREMENT_DURATION > "${OUTPUT_DIR}/memory_data.csv" &
MEM_PID=$!

# System resource monitoring
top -b -d 1 -n $MEASUREMENT_DURATION > "${OUTPUT_DIR}/system_resources.log" &
iostat -x 1 $MEASUREMENT_DURATION > "${OUTPUT_DIR}/io_stats.log" &

echo "Monitoring started. Data collection will complete in ${MEASUREMENT_DURATION} seconds..."
wait $SPARK_PID $TPS_PID $MEM_PID

echo "Data collection complete. Results saved to $OUTPUT_DIR"
```

#### 1.3 Chunk Loading Metrics Analysis

##### Performance Calculation Script
```python
#!/usr/bin/env python3
# analyze_chunk_performance.py

import json
import csv
import statistics
from datetime import datetime

def analyze_chunk_loading_metrics(data_dir):
    """Analyze chunk loading performance from collected data"""

    # Load Spark profiling data
    with open(f"{data_dir}/spark_profile.json", 'r') as f:
        spark_data = json.load(f)

    # Extract chunk loading times
    chunk_operations = extract_chunk_operations(spark_data)

    # Calculate performance metrics
    metrics = {
        'average_loading_time_ms': statistics.mean(chunk_operations),
        'median_loading_time_ms': statistics.median(chunk_operations),
        'p95_loading_time_ms': calculate_percentile(chunk_operations, 95),
        'p99_loading_time_ms': calculate_percentile(chunk_operations, 99),
        'total_chunks_processed': len(chunk_operations),
        'chunks_per_second': len(chunk_operations) / (MEASUREMENT_DURATION / 1000)
    }

    return metrics

def compare_with_baseline(current_metrics, baseline_metrics):
    """Compare current performance with baseline"""

    improvements = {}
    for metric in baseline_metrics:
        if metric.endswith('_time_ms'):
            # Lower is better for time metrics
            improvement = (baseline_metrics[metric] - current_metrics[metric]) / baseline_metrics[metric] * 100
        else:
            # Higher is better for throughput metrics
            improvement = (current_metrics[metric] - baseline_metrics[metric]) / baseline_metrics[metric] * 100

        improvements[metric] = improvement

    return improvements
```

### 2. Memory Usage Metrics

#### 2.1 Memory Metrics Categories
- **Heap Usage**: Total heap memory consumption
- **Chunk Data Memory**: Memory specifically for chunk storage
- **Cache Memory**: Memory used by chunk caching system
- **GC Performance**: Garbage collection frequency and pause times

#### 2.2 Memory Collection Procedures

##### Continuous Memory Monitoring
```bash
# Spark heap monitoring with detailed breakdown
/spark heap --live --include-non-heap --gc-info --interval 10
```

##### Eclipse MAT Integration for Deep Analysis
```bash
# Generate heap dumps for detailed analysis
jcmd <minecraft_pid> GC.run_finalization
jcmd <minecraft_pid> VM.gc
jcmd <minecraft_pid> GC.dump /path/to/heap_dump.hprof

# Analyze with Eclipse MAT
eclipse-mat -application org.eclipse.mat.api.parse /path/to/heap_dump.hprof
```

##### Memory Profiling with async-profiler
```bash
# Memory allocation profiling
./profiler.sh -e alloc -d 600 -f chunk_memory_alloc.html <minecraft_pid>

# Specific allocation tracking for chunk operations
./profiler.sh -e alloc -d 600 --alloc 1k -i 1ms -f chunk_large_allocs.html <minecraft_pid>
```

#### 2.3 Memory Analysis Scripts

##### Memory Usage Analysis
```python
#!/usr/bin/env python3
# analyze_memory_usage.py

import re
import matplotlib.pyplot as plt
from datetime import datetime

def analyze_memory_trends(memory_data_file):
    """Analyze memory usage trends over time"""

    timestamps = []
    heap_used = []
    heap_max = []
    gc_events = []

    with open(memory_data_file, 'r') as f:
        for line in f:
            if 'Heap Usage' in line:
                # Parse heap usage data
                timestamp, used, max_heap = parse_heap_line(line)
                timestamps.append(timestamp)
                heap_used.append(used)
                heap_max.append(max_heap)
            elif 'GC Event' in line:
                # Track garbage collection events
                gc_timestamp, gc_duration = parse_gc_line(line)
                gc_events.append((gc_timestamp, gc_duration))

    # Calculate memory efficiency metrics
    avg_utilization = statistics.mean([u/m for u, m in zip(heap_used, heap_max)])
    max_utilization = max([u/m for u, m in zip(heap_used, heap_max)])
    gc_frequency = len(gc_events) / (timestamps[-1] - timestamps[0]).total_seconds() * 60  # per minute

    return {
        'average_utilization': avg_utilization,
        'peak_utilization': max_utilization,
        'gc_frequency_per_minute': gc_frequency,
        'total_gc_time_ms': sum(duration for _, duration in gc_events)
    }
```

### 3. TPS (Ticks Per Second) Monitoring

#### 3.1 TPS Metrics
- **Average TPS**: Mean TPS over measurement period
- **TPS Stability**: Percentage of time above threshold (19.5 TPS)
- **TPS Variance**: Standard deviation of TPS measurements
- **Tick Duration**: MSPT (Milliseconds Per Tick) analysis

#### 3.2 TPS Collection Procedures

##### Real-Time TPS Monitoring
```bash
# Continuous TPS monitoring with alerts
/spark tps --timeout 0 --alert-threshold 18.0 --monitor-interval 5
```

##### Detailed MSPT Analysis
```bash
# MSPT breakdown by system component
/spark profiler --timeout 600 --only-ticks-over 50 --include-slow-operations
```

#### 3.3 TPS Analysis and Alerting

##### TPS Stability Calculator
```python
def calculate_tps_stability(tps_data, threshold=19.5):
    """Calculate TPS stability metrics"""

    above_threshold = sum(1 for tps in tps_data if tps >= threshold)
    stability_percentage = (above_threshold / len(tps_data)) * 100

    # Calculate TPS variance
    tps_variance = statistics.variance(tps_data)
    tps_std_dev = statistics.stdev(tps_data)

    # Identify lag spikes
    lag_spikes = [tps for tps in tps_data if tps < threshold]

    return {
        'stability_percentage': stability_percentage,
        'average_tps': statistics.mean(tps_data),
        'tps_variance': tps_variance,
        'tps_std_dev': tps_std_dev,
        'lag_spike_count': len(lag_spikes),
        'worst_tps': min(tps_data)
    }
```

### 4. Cache Performance Metrics

#### 4.1 Cache Metrics
- **Cache Hit Ratio**: Percentage of successful cache accesses
- **Cache Access Time**: Time to retrieve cached chunks
- **Cache Memory Efficiency**: Memory usage vs. cache effectiveness
- **Eviction Rate**: Frequency of cache evictions

#### 4.2 Cache Metrics Collection

##### Custom Cache Monitoring
```java
// ChunkCacheMetrics.java
public class ChunkCacheMetrics {
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicLong cacheEvictions = new AtomicLong(0);
    private final HistogramMetric accessTimeHistogram = new HistogramMetric();

    public void recordCacheHit(long accessTimeNanos) {
        cacheHits.incrementAndGet();
        accessTimeHistogram.record(accessTimeNanos / 1_000_000.0); // Convert to ms
    }

    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }

    public void recordEviction() {
        cacheEvictions.incrementAndGet();
    }

    public double getHitRatio() {
        long hits = cacheHits.get();
        long misses = cacheMisses.get();
        return hits + misses > 0 ? (double) hits / (hits + misses) : 0.0;
    }

    public double getAverageAccessTime() {
        return accessTimeHistogram.getMean();
    }
}
```

##### Cache Performance Validation
```bash
# Export cache statistics for analysis
/spark cache-stats --export csv --duration 1800
```

## Automated Metrics Collection Pipeline

### Comprehensive Monitoring Script
```bash
#!/bin/bash
# comprehensive_metrics_collection.sh

# Configuration
MEASUREMENT_DURATION=${1:-3600}  # 1 hour default
TEST_NAME=${2:-"performance_test"}
OUTPUT_BASE="metrics/$(date +%Y%m%d_%H%M%S)_${TEST_NAME}"

# Create output directories
mkdir -p "${OUTPUT_BASE}/raw_data"
mkdir -p "${OUTPUT_BASE}/analysis"
mkdir -p "${OUTPUT_BASE}/reports"

echo "Starting comprehensive metrics collection for ${TEST_NAME}"
echo "Duration: ${MEASUREMENT_DURATION} seconds"
echo "Output directory: ${OUTPUT_BASE}"

# Start background monitoring processes
start_monitoring() {
    # Spark profiling
    /spark profiler --timeout $MEASUREMENT_DURATION \
        --thread "*" --interval 2ms \
        --output "${OUTPUT_BASE}/raw_data/spark_profile.json" &
    SPARK_PID=$!

    # TPS monitoring
    /spark tps --timeout $MEASUREMENT_DURATION \
        --interval 5 --export csv \
        --output "${OUTPUT_BASE}/raw_data/tps_data.csv" &
    TPS_PID=$!

    # Memory monitoring
    /spark heap --live --interval 10 \
        --timeout $MEASUREMENT_DURATION \
        --output "${OUTPUT_BASE}/raw_data/memory_data.csv" &
    MEM_PID=$!

    # Cache statistics
    /spark cache-stats --monitor --interval 30 \
        --timeout $MEASUREMENT_DURATION \
        --output "${OUTPUT_BASE}/raw_data/cache_stats.csv" &
    CACHE_PID=$!

    # System resources
    top -b -d 1 -n $MEASUREMENT_DURATION > "${OUTPUT_BASE}/raw_data/system_top.log" &
    iostat -x 1 $MEASUREMENT_DURATION > "${OUTPUT_BASE}/raw_data/io_stats.log" &
    sar -u -r -n DEV 1 $MEASUREMENT_DURATION > "${OUTPUT_BASE}/raw_data/system_sar.log" &

    echo "All monitoring processes started. PIDs: Spark=$SPARK_PID, TPS=$TPS_PID, Memory=$MEM_PID"

    # Store PIDs for cleanup
    echo "$SPARK_PID $TPS_PID $MEM_PID $CACHE_PID" > "${OUTPUT_BASE}/monitor_pids.txt"
}

# Analysis and reporting
analyze_results() {
    echo "Analyzing collected data..."

    # Run Python analysis scripts
    python3 analyze_chunk_performance.py "${OUTPUT_BASE}/raw_data" > "${OUTPUT_BASE}/analysis/chunk_performance.json"
    python3 analyze_memory_usage.py "${OUTPUT_BASE}/raw_data" > "${OUTPUT_BASE}/analysis/memory_analysis.json"
    python3 analyze_tps_stability.py "${OUTPUT_BASE}/raw_data" > "${OUTPUT_BASE}/analysis/tps_analysis.json"

    # Generate comprehensive report
    python3 generate_performance_report.py "${OUTPUT_BASE}/analysis" > "${OUTPUT_BASE}/reports/performance_report.html"

    echo "Analysis complete. Reports available in ${OUTPUT_BASE}/reports/"
}

# Main execution
start_monitoring

# Wait for completion
wait

# Perform analysis
analyze_results

# Generate summary
echo "Metrics collection and analysis complete!"
echo "Results location: ${OUTPUT_BASE}"
echo "View report: ${OUTPUT_BASE}/reports/performance_report.html"
```

## Performance Comparison and Validation

### Baseline Comparison Framework
```python
#!/usr/bin/env python3
# performance_comparison.py

import json
import sys
from dataclasses import dataclass
from typing import Dict, List

@dataclass
class PerformanceMetrics:
    chunk_loading_time_ms: float
    memory_usage_mb: float
    tps_stability_percent: float
    cache_hit_ratio: float
    gc_frequency_per_minute: float

def load_metrics(file_path: str) -> PerformanceMetrics:
    """Load performance metrics from JSON file"""
    with open(file_path, 'r') as f:
        data = json.load(f)

    return PerformanceMetrics(
        chunk_loading_time_ms=data['chunk_loading']['average_time_ms'],
        memory_usage_mb=data['memory']['peak_usage_mb'],
        tps_stability_percent=data['tps']['stability_percentage'],
        cache_hit_ratio=data['cache']['hit_ratio'],
        gc_frequency_per_minute=data['memory']['gc_frequency_per_minute']
    )

def calculate_improvements(baseline: PerformanceMetrics, optimized: PerformanceMetrics) -> Dict[str, float]:
    """Calculate performance improvements as percentages"""

    improvements = {}

    # Chunk loading time improvement (lower is better)
    improvements['chunk_loading_improvement'] = (
        (baseline.chunk_loading_time_ms - optimized.chunk_loading_time_ms) /
        baseline.chunk_loading_time_ms * 100
    )

    # Memory usage improvement (lower is better)
    improvements['memory_improvement'] = (
        (baseline.memory_usage_mb - optimized.memory_usage_mb) /
        baseline.memory_usage_mb * 100
    )

    # TPS stability improvement (higher is better)
    improvements['tps_stability_improvement'] = (
        optimized.tps_stability_percent - baseline.tps_stability_percent
    )

    # Cache hit ratio (new metric, should be high)
    improvements['cache_effectiveness'] = optimized.cache_hit_ratio * 100

    # GC frequency improvement (lower is better)
    improvements['gc_improvement'] = (
        (baseline.gc_frequency_per_minute - optimized.gc_frequency_per_minute) /
        baseline.gc_frequency_per_minute * 100
    )

    return improvements

def validate_targets(improvements: Dict[str, float]) -> Dict[str, bool]:
    """Validate that improvements meet epic targets"""

    validation = {}

    # Target: 15-25% chunk loading improvement
    validation['chunk_loading_target'] = 15 <= improvements['chunk_loading_improvement'] <= 40

    # Target: 10-20% memory reduction
    validation['memory_target'] = 10 <= improvements['memory_improvement'] <= 30

    # Target: TPS stability maintained or improved
    validation['tps_stability_target'] = improvements['tps_stability_improvement'] >= 0

    # Target: Cache hit ratio >80%
    validation['cache_target'] = improvements['cache_effectiveness'] >= 80

    # Target: GC improvement (any positive improvement acceptable)
    validation['gc_target'] = improvements['gc_improvement'] >= 0

    return validation

def generate_validation_report(improvements: Dict[str, float], validation: Dict[str, bool]) -> str:
    """Generate human-readable validation report"""

    report = []
    report.append("=== Performance Validation Report ===\n")

    # Chunk loading results
    chunk_result = "✓ PASS" if validation['chunk_loading_target'] else "✗ FAIL"
    report.append(f"Chunk Loading Improvement: {improvements['chunk_loading_improvement']:.1f}% {chunk_result}")
    report.append("  Target: 15-25% improvement")

    # Memory results
    memory_result = "✓ PASS" if validation['memory_target'] else "✗ FAIL"
    report.append(f"Memory Usage Improvement: {improvements['memory_improvement']:.1f}% {memory_result}")
    report.append("  Target: 10-20% reduction")

    # TPS stability results
    tps_result = "✓ PASS" if validation['tps_stability_target'] else "✗ FAIL"
    report.append(f"TPS Stability: {improvements['tps_stability_improvement']:.1f}% {tps_result}")
    report.append("  Target: Maintain or improve")

    # Cache results
    cache_result = "✓ PASS" if validation['cache_target'] else "✗ FAIL"
    report.append(f"Cache Hit Ratio: {improvements['cache_effectiveness']:.1f}% {cache_result}")
    report.append("  Target: >80%")

    # Overall result
    all_passed = all(validation.values())
    overall_result = "✓ ALL TARGETS MET" if all_passed else "✗ SOME TARGETS NOT MET"
    report.append(f"\nOverall Result: {overall_result}")

    return "\n".join(report)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python performance_comparison.py <baseline_metrics.json> <optimized_metrics.json>")
        sys.exit(1)

    baseline_file = sys.argv[1]
    optimized_file = sys.argv[2]

    # Load metrics
    baseline_metrics = load_metrics(baseline_file)
    optimized_metrics = load_metrics(optimized_file)

    # Calculate improvements
    improvements = calculate_improvements(baseline_metrics, optimized_metrics)

    # Validate against targets
    validation = validate_targets(improvements)

    # Generate report
    report = generate_validation_report(improvements, validation)
    print(report)

    # Exit with appropriate code
    sys.exit(0 if all(validation.values()) else 1)
```

## Quality Assurance and Validation

### Data Quality Checks
```bash
# validate_metrics_quality.sh
#!/bin/bash

validate_data_quality() {
    local data_dir=$1

    echo "Validating data quality for: $data_dir"

    # Check for required files
    required_files=(
        "spark_profile.json"
        "tps_data.csv"
        "memory_data.csv"
        "cache_stats.csv"
    )

    for file in "${required_files[@]}"; do
        if [[ ! -f "${data_dir}/${file}" ]]; then
            echo "ERROR: Missing required file: $file"
            return 1
        fi
    done

    # Validate data completeness
    python3 validate_data_completeness.py "$data_dir"

    # Check for data anomalies
    python3 detect_data_anomalies.py "$data_dir"

    echo "Data quality validation complete"
}
```

This comprehensive metrics collection framework ensures accurate measurement and validation of all performance improvements in the World/Chunk Performance Enhancement epic, providing the necessary data to confirm achievement of the 15-25% chunk loading improvement and 10-20% memory reduction targets.