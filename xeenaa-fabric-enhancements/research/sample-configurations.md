# Sample Configurations for Minecraft Performance Testing

## 1. Spark Profiler Configuration Templates

### Basic Server Performance Testing
```hocon
# config/spark/config.conf
spark {
    # Web viewer configuration
    web-viewer {
        enabled = true
        port = 4567
        bind-address = "0.0.0.0"
    }

    # Profiler settings optimized for chunk loading analysis
    profiler {
        max-stack-depth = 300
        thread-grouper = "by-name"
        data-aggregator = "simple"

        # Optimize for chunk loading performance
        sample-interval = 4  # 4ms sampling (good for chunk operations)
        thread-dumper = "basic"
    }

    # Health monitoring for tick performance
    health {
        enabled = true
        tick-threshold = 50      # Alert if tick takes > 50ms
        memory-threshold = 0.85  # Alert at 85% memory usage
        cpu-threshold = 0.8      # Alert at 80% CPU usage
    }

    # Activity monitoring
    activity {
        enabled = true
        tick-reporting = true
        tick-threshold = 100     # Report ticks > 100ms
    }
}
```

### Advanced Chunk Loading Analysis
```hocon
# config/spark/chunk-analysis.conf
spark {
    profiler {
        max-stack-depth = 500    # Deeper stack for chunk generation
        sample-interval = 2      # Higher resolution for chunk ops

        # Thread targeting for chunk operations
        thread-whitelist = [
            "Server thread",
            "Chunk-Worker-*",
            "worldgen-*"
        ]

        # Memory allocation tracking
        allocation-tracking = true
        allocation-sample-rate = 524288  # Every 512KB
    }

    # Custom event tracking
    events {
        chunk-load = true
        chunk-unload = true
        chunk-generation = true
        lighting-calculation = true
    }
}
```

## 2. JMH Benchmarking Configurations

### Chunk Loading Performance Benchmark
```java
package com.xeenaa.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xmx4G", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200"})
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
public class ChunkPerformanceBenchmark {

    private ChunkManager chunkManager;
    private WorldGeneration worldGen;

    @Param({"16", "32", "64", "128"}) // Test different view distances
    private int viewDistance;

    @Param({"overworld", "nether", "end"}) // Test different dimensions
    private String dimension;

    @Setup(Level.Trial)
    public void setupTrial() {
        chunkManager = new OptimizedChunkManager();
        worldGen = new WorldGeneration(dimension);
        chunkManager.setViewDistance(viewDistance);
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        chunkManager.clearCache();
        System.gc(); // Ensure clean state between iterations
    }

    @Benchmark
    @Group("chunk_operations")
    public void chunkLoading() {
        ChunkPos pos = new ChunkPos(
            ThreadLocalRandom.current().nextInt(-100, 100),
            ThreadLocalRandom.current().nextInt(-100, 100)
        );
        chunkManager.loadChunk(pos);
    }

    @Benchmark
    @Group("chunk_operations")
    public void chunkGeneration() {
        ChunkPos pos = new ChunkPos(
            ThreadLocalRandom.current().nextInt(1000, 2000), // New terrain
            ThreadLocalRandom.current().nextInt(1000, 2000)
        );
        worldGen.generateChunk(pos);
    }

    @Benchmark
    public void massChunkLoading() {
        // Simulate player movement requiring multiple chunks
        ChunkPos center = new ChunkPos(0, 0);
        for (int x = -viewDistance; x <= viewDistance; x++) {
            for (int z = -viewDistance; z <= viewDistance; z++) {
                chunkManager.loadChunk(new ChunkPos(center.x + x, center.z + z));
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ChunkPerformanceBenchmark.class.getSimpleName())
                .result("results/chunk-performance-" + System.currentTimeMillis() + ".json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
}
```

### Memory Allocation Benchmark
```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
public class ChunkMemoryBenchmark {

    @Benchmark
    @Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void allocateChunkData() {
        // Benchmark chunk data structure allocation
        ChunkData data = new ChunkData();
        data.fillWithRandomBlocks();
        return data;
    }

    @Benchmark
    public void serializeChunkData() {
        ChunkData data = createTestChunk();
        return ChunkSerializer.serialize(data);
    }

    @Benchmark
    public void deserializeChunkData(Blackhole bh) {
        byte[] serialized = getPreSerializedChunk();
        ChunkData data = ChunkSerializer.deserialize(serialized);
        bh.consume(data);
    }
}
```

## 3. JFR Custom Profiling Configuration

### Minecraft-Optimized JFR Profile
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration version="2.0" label="Minecraft Performance" description="Optimized for Minecraft server/client analysis">

    <!-- CPU Profiling -->
    <event name="jdk.ExecutionSample">
        <setting name="enabled">true</setting>
        <setting name="period">10 ms</setting>
    </event>

    <!-- Method profiling for hot paths -->
    <event name="jdk.MethodSample">
        <setting name="enabled">true</setting>
        <setting name="period">10 ms</setting>
    </event>

    <!-- Garbage Collection -->
    <event name="jdk.GarbageCollection">
        <setting name="enabled">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>

    <event name="jdk.G1GarbageCollection">
        <setting name="enabled">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>

    <!-- Memory Allocation -->
    <event name="jdk.ObjectAllocationInNewTLAB">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
    </event>

    <event name="jdk.ObjectAllocationOutsideTLAB">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
    </event>

    <!-- Thread Analysis -->
    <event name="jdk.ThreadSleep">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>

    <event name="jdk.ThreadPark">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>

    <!-- I/O Operations (important for chunk loading) -->
    <event name="jdk.FileRead">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>

    <event name="jdk.FileWrite">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>

    <!-- System Performance -->
    <event name="jdk.CPULoad">
        <setting name="enabled">true</setting>
        <setting name="period">1000 ms</setting>
    </event>

    <event name="jdk.PhysicalMemory">
        <setting name="enabled">true</setting>
        <setting name="period">1000 ms</setting>
    </event>

    <!-- JIT Compilation -->
    <event name="jdk.Compilation">
        <setting name="enabled">true</setting>
        <setting name="threshold">100 ms</setting>
    </event>

    <!-- Class Loading -->
    <event name="jdk.ClassLoad">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">false</setting>
    </event>

    <!-- Custom Minecraft Events (if implemented) -->
    <event name="minecraft.ChunkLoad">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
    </event>

    <event name="minecraft.ChunkGeneration">
        <setting name="enabled">true</setting>
        <setting name="stackTrace">true</setting>
    </event>

    <event name="minecraft.TickDuration">
        <setting name="enabled">true</setting>
        <setting name="threshold">50 ms</setting>
    </event>
</configuration>
```

## 4. C2ME Configuration for Testing

### Maximum Performance Configuration
```toml
# config/c2me.toml
[general]
# Reduce log spam during testing
reduced_logging = true

[worldgen]
# Max threads for world generation (0 = auto-detect cores)
max_threads = 0
# Allow more aggressive multithreading
allow_unsafe_async_chunks = true
# Reduce world generation lag
reduce_worldgen_lag = true

[chunk_loading]
# Enable async chunk request processing
enable_async_chunk_request = true
# Optimize chunk loading threads
chunk_load_threads = 0  # Auto-detect optimal count
# Enable chunk loading optimizations
enable_chunk_loading_optimizations = true

[threading]
# Enable general threading improvements
enable_threading_optimizations = true
# Reduce contention on chunk access
reduce_chunk_lock_contention = true

[optimizations]
# Enable chunk ticking optimizations
optimize_chunk_ticking = true
# Reduce memory allocations
reduce_memory_usage = true
# Enable async light updates
enable_async_light_updates = true

[experimental]
# Enable experimental features for testing
enable_experimental_features = true
# Use faster data structures
use_optimized_collections = true

[debug]
# Enable detailed performance metrics
enable_detailed_metrics = true
# Log chunk loading times
log_chunk_times = true
```

### Conservative Testing Configuration
```toml
# config/c2me-conservative.toml
[general]
reduced_logging = false

[worldgen]
max_threads = 4  # Limited threads for stability
allow_unsafe_async_chunks = false
reduce_worldgen_lag = true

[chunk_loading]
enable_async_chunk_request = true
chunk_load_threads = 2  # Conservative thread count
enable_chunk_loading_optimizations = true

[threading]
enable_threading_optimizations = true
reduce_chunk_lock_contention = false  # Disabled for stability

[optimizations]
optimize_chunk_ticking = true
reduce_memory_usage = true
enable_async_light_updates = false  # Disabled for compatibility

[experimental]
enable_experimental_features = false
use_optimized_collections = false

[debug]
enable_detailed_metrics = true
log_chunk_times = true
```

## 5. JVM Performance Flags for Testing

### Production Performance Flags
```bash
#!/bin/bash
# production-jvm-flags.sh

MEMORY="-Xmx8G -Xms8G"
GC_FLAGS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1"
OPTIMIZATION_FLAGS="-XX:+UseStringDeduplication -XX:+UseFastUnorderedTimeStamps -XX:+UseAES -XX:+UseAESIntrinsics -XX:UseAVX=2 -XX:+UseStringConcatIntrinsics -XX:+UseFMA -XX:+UseLoopPredicate"
PROFILING_FLAGS="-XX:+FlightRecorder -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints"
STARTUP_FLAGS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -XX:+UseTransparentHugePages"

java $MEMORY $GC_FLAGS $OPTIMIZATION_FLAGS $PROFILING_FLAGS $STARTUP_FLAGS -jar minecraft-server.jar
```

### Development/Testing Flags
```bash
#!/bin/bash
# development-jvm-flags.sh

MEMORY="-Xmx4G -Xms2G"
GC_FLAGS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200"
DEBUG_FLAGS="-XX:+UnlockDiagnosticVMOptions -XX:+LogVMOutput -XX:+TraceClassLoading -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
PROFILING_FLAGS="-XX:+FlightRecorder -XX:StartFlightRecording=duration=300s,filename=minecraft-test.jfr,settings=profile"
JMX_FLAGS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

java $MEMORY $GC_FLAGS $DEBUG_FLAGS $PROFILING_FLAGS $JMX_FLAGS -jar minecraft-server.jar
```

## 6. Test Automation Scripts

### Automated Performance Test Runner
```bash
#!/bin/bash
# performance-test-runner.sh

TEST_DURATION=300  # 5 minutes
RESULTS_DIR="./performance-results/$(date +%Y%m%d_%H%M%S)"
SERVER_JAR="minecraft-server.jar"

mkdir -p "$RESULTS_DIR"

echo "Starting Minecraft performance testing suite..."

# Function to run a single test
run_test() {
    local test_name=$1
    local config_file=$2
    local jvm_flags=$3

    echo "Running test: $test_name"

    # Start server with specific configuration
    java $jvm_flags -Dconfig.file="$config_file" -jar "$SERVER_JAR" &
    SERVER_PID=$!

    # Wait for server startup
    sleep 30

    # Start profiling
    ./spark-cli.sh profiler start --timeout $TEST_DURATION --output "$RESULTS_DIR/${test_name}-spark.json"

    # Generate synthetic load
    ./chunk-load-generator.sh --duration $TEST_DURATION --chunks 1000

    # Wait for test completion
    sleep $TEST_DURATION

    # Stop server
    kill $SERVER_PID
    wait $SERVER_PID 2>/dev/null

    echo "Test $test_name completed"
}

# Run test suite
run_test "baseline" "config/baseline.conf" "-Xmx4G -XX:+UseG1GC"
run_test "optimized" "config/optimized.conf" "-Xmx4G -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions"
run_test "c2me_enabled" "config/c2me.conf" "-Xmx4G -XX:+UseG1GC"

echo "Performance testing completed. Results in: $RESULTS_DIR"
```

### Chunk Loading Test Generator
```bash
#!/bin/bash
# chunk-load-generator.sh

DURATION=300
CHUNKS=1000
SERVER_ADDRESS="localhost:25565"

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --duration)
            DURATION="$2"
            shift 2
            ;;
        --chunks)
            CHUNKS="$2"
            shift 2
            ;;
        --server)
            SERVER_ADDRESS="$2"
            shift 2
            ;;
        *)
            echo "Unknown argument: $1"
            exit 1
            ;;
    esac
done

# Generate chunk loading commands
python3 << EOF
import time
import random
import socket

def send_chunk_load_commands():
    for i in range($CHUNKS):
        x = random.randint(-1000, 1000)
        z = random.randint(-1000, 1000)

        # Simulate player movement to trigger chunk loading
        print(f"Loading chunk at {x}, {z}")
        time.sleep($DURATION / $CHUNKS)

send_chunk_load_commands()
EOF
```

## 7. Configuration Validation

### Validation Script
```bash
#!/bin/bash
# validate-config.sh

CONFIG_DIR="./config"
ERRORS=0

echo "Validating performance configurations..."

# Check Spark configuration
if [ -f "$CONFIG_DIR/spark/config.conf" ]; then
    echo "✓ Spark configuration found"
    # Validate HOCON syntax
    if ! java -cp spark.jar com.typesafe.config.ConfigFactory.parseFile("$CONFIG_DIR/spark/config.conf") >/dev/null 2>&1; then
        echo "✗ Spark configuration syntax error"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo "✗ Spark configuration missing"
    ERRORS=$((ERRORS + 1))
fi

# Check C2ME configuration
if [ -f "$CONFIG_DIR/c2me.toml" ]; then
    echo "✓ C2ME configuration found"
    # Basic TOML validation
    if ! python3 -c "import toml; toml.load('$CONFIG_DIR/c2me.toml')" 2>/dev/null; then
        echo "✗ C2ME configuration syntax error"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo "✗ C2ME configuration missing"
    ERRORS=$((ERRORS + 1))
fi

# Check JFR profile
if [ -f "$CONFIG_DIR/minecraft-performance.jfc" ]; then
    echo "✓ JFR profile found"
    # Validate XML
    if ! xmllint --noout "$CONFIG_DIR/minecraft-performance.jfc" 2>/dev/null; then
        echo "✗ JFR profile syntax error"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo "✗ JFR profile missing"
    ERRORS=$((ERRORS + 1))
fi

if [ $ERRORS -eq 0 ]; then
    echo "All configurations valid!"
    exit 0
else
    echo "Found $ERRORS configuration errors"
    exit 1
fi
```

This comprehensive configuration suite provides ready-to-use templates for all major performance testing scenarios, from basic monitoring to advanced benchmarking and automated testing workflows.