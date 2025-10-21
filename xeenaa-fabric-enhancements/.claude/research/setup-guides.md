# Performance Tools Setup Guides

## 1. Spark Profiler Setup

### Installation for Fabric 1.21.1

1. **Download Spark**
   ```bash
   # Download from Modrinth (recommended)
   curl -L -o spark-fabric.jar "https://modrinth.com/mod/spark/version/latest"
   ```

2. **Installation**
   - Place `spark-fabric.jar` in your `mods/` folder
   - Requires Fabric API (ensure it's installed)
   - No configuration required - works out of the box

3. **Basic Commands**
   ```minecraft
   /spark profiler start --timeout 60
   /spark profiler stop
   /spark health
   /spark heapdump
   /spark heapsummary
   ```

4. **Advanced Profiling**
   ```minecraft
   # CPU profiling with allocation tracking
   /spark profiler start --alloc --timeout 120

   # Thread-specific profiling
   /spark profiler start --thread "*" --timeout 60

   # Memory allocation profiling
   /spark profiler start --alloc --timeout 30
   ```

### Spark Configuration

Create `config/spark/config.conf`:
```hocon
# Spark Configuration
spark {
    # Web viewer settings
    web-viewer {
        enabled = true
        port = 4567
    }

    # Profiler settings
    profiler {
        max-stack-depth = 300
        thread-grouper = "by-name"
        data-aggregator = "simple"
    }

    # Health monitoring
    health {
        enabled = true
        tick-threshold = 50
        memory-threshold = 0.9
    }
}
```

## 2. async-profiler Setup

### Installation

1. **Download async-profiler**
   ```bash
   # Linux x64
   wget https://github.com/async-profiler/async-profiler/releases/latest/download/async-profiler-3.0-linux-x64.tar.gz
   tar -xzf async-profiler-3.0-linux-x64.tar.gz

   # macOS
   wget https://github.com/async-profiler/async-profiler/releases/latest/download/async-profiler-3.0-macos.zip
   unzip async-profiler-3.0-macos.zip
   ```

2. **Basic Usage with Minecraft**
   ```bash
   # Find Minecraft Java process
   jps | grep minecraft

   # Profile for 60 seconds, generate flame graph
   ./profiler.sh -d 60 -f flamegraph.html <PID>

   # Memory allocation profiling
   ./profiler.sh -e alloc -d 30 -f alloc.html <PID>

   # Lock contention profiling
   ./profiler.sh -e lock -d 30 -f locks.html <PID>
   ```

3. **Integration with Spark**
   - Spark automatically uses async-profiler when available on Linux/macOS
   - No additional configuration needed
   - Provides better sampling accuracy than Java-based profiling

## 3. JMH Benchmarking Setup

### Maven Project Setup

1. **Create JMH Project**
   ```bash
   mvn archetype:generate \
     -DinteractiveMode=false \
     -DarchetypeGroupId=org.openjdk.jmh \
     -DarchetypeArtifactId=jmh-java-benchmark-archetype \
     -DgroupId=com.xeenaa.benchmarks \
     -DartifactId=chunk-performance-benchmarks \
     -Dversion=1.0
   ```

2. **Add to Existing Project**
   ```xml
   <dependencies>
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
   </dependencies>
   ```

3. **Sample Chunk Loading Benchmark**
   ```java
   @BenchmarkMode(Mode.AverageTime)
   @OutputTimeUnit(TimeUnit.MILLISECONDS)
   @State(Scope.Benchmark)
   @Fork(1)
   @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
   @Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
   public class ChunkLoadingBenchmark {

       private ChunkLoader chunkLoader;
       private ChunkPos testChunkPos;

       @Setup
       public void setup() {
           chunkLoader = new OptimizedChunkLoader();
           testChunkPos = new ChunkPos(0, 0);
       }

       @Benchmark
       public Chunk benchmarkChunkLoading() {
           return chunkLoader.loadChunk(testChunkPos);
       }

       @Benchmark
       @BenchmarkMode(Mode.Throughput)
       public void benchmarkChunkGeneration() {
           chunkLoader.generateChunk(testChunkPos);
       }
   }
   ```

4. **Running Benchmarks**
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="org.openjdk.jmh.Main"

   # With specific parameters
   mvn clean compile exec:java -Dexec.mainClass="org.openjdk.jmh.Main" \
     -Dexec.args="-i 5 -wi 3 -f 1 ChunkLoadingBenchmark"
   ```

## 4. Eclipse Memory Analyzer (MAT) Setup

### Installation

1. **Download and Install**
   - Download from: https://eclipse.dev/mat/downloads.php
   - Install as standalone application or Eclipse plugin

2. **Heap Dump Generation**
   ```bash
   # Using JVM flags (add to Minecraft startup)
   -XX:+HeapDumpOnOutOfMemoryError
   -XX:HeapDumpPath=/path/to/dumps/

   # Manual heap dump with jcmd
   jcmd <PID> GC.run_finalization
   jcmd <PID> VM.gc
   jcmd <PID> GC.run
   jcmd <PID> VM.classloader_stats

   # Using jmap
   jmap -dump:format=b,file=minecraft-heap.hprof <PID>
   ```

3. **Analysis Configuration**
   - Set memory limit: `MemoryAnalyzer.ini` → `-Xmx8g`
   - Enable DTFJ: Help → Install New Software → DTFJ
   - Configure reports: Window → Preferences → Memory Analyzer

## 5. JFR (Java Flight Recorder) Setup

### Command Line Setup

```bash
# Start Minecraft with JFR enabled
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=60s,filename=minecraft-profile.jfr \
     -jar minecraft-server.jar

# Start recording on running JVM
jcmd <PID> JFR.start duration=60s filename=minecraft-profile.jfr

# Stop recording
jcmd <PID> JFR.stop filename=minecraft-profile.jfr

# Check available recordings
jcmd <PID> JFR.check
```

### JFR Configuration Files

Create `custom-profile.jfc`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <event name="jdk.CPULoad">
        <setting name="enabled">true</setting>
        <setting name="period">1000 ms</setting>
    </event>
    <event name="jdk.GarbageCollection">
        <setting name="enabled">true</setting>
        <setting name="threshold">10 ms</setting>
    </event>
    <event name="jdk.ThreadAllocationStatistics">
        <setting name="enabled">true</setting>
        <setting name="period">10 s</setting>
    </event>
</configuration>
```

## 6. VisualVM Setup

### Installation and Configuration

1. **Download VisualVM**
   ```bash
   # Download from https://visualvm.github.io/
   # Or install via package manager
   brew install --cask visualvm  # macOS
   ```

2. **Connect to Minecraft**
   - Start Minecraft with JMX enabled:
     ```bash
     -Dcom.sun.management.jmxremote=true
     -Dcom.sun.management.jmxremote.port=9999
     -Dcom.sun.management.jmxremote.authenticate=false
     -Dcom.sun.management.jmxremote.ssl=false
     ```
   - In VisualVM: File → Add Remote Host → localhost:9999

3. **Profiling Configuration**
   - CPU Profiling: Filter to package `net.minecraft.*`, `com.xeenaa.*`
   - Memory Profiling: Track object allocations
   - Snapshots: Regular heap/CPU snapshots during gameplay

## 7. Performance Optimization Mods Setup

### C2ME Installation and Configuration

1. **Download and Install**
   ```bash
   # Download C2ME for Fabric 1.21.1
   curl -L -o c2me-fabric.jar "https://modrinth.com/mod/c2me-fabric/version/latest"
   ```

2. **Configuration**
   Create `config/c2me.toml`:
   ```toml
   [worldgen]
   max_threads = 0  # 0 = auto-detect CPU cores
   reduce_worldgen_lag = true

   [chunk_loading]
   enable_async_chunk_request = true
   chunk_load_threads = 0  # 0 = auto-detect

   [optimizations]
   optimize_chunk_ticking = true
   reduce_memory_usage = true
   ```

### Lithium + ScalableLux Setup

1. **Install Lithium**
   ```bash
   curl -L -o lithium-fabric.jar "https://modrinth.com/mod/lithium/version/latest"
   ```

2. **Install ScalableLux (Starlight replacement for 1.21+)**
   ```bash
   curl -L -o scalablelux-fabric.jar "https://modrinth.com/mod/scalablelux/version/latest"
   ```

3. **Configuration**
   No configuration required - both mods work automatically

## 8. Automated Setup Script

Create `setup-performance-tools.sh`:
```bash
#!/bin/bash

MODS_DIR="./mods"
CONFIG_DIR="./config"

# Create directories
mkdir -p "$MODS_DIR" "$CONFIG_DIR"

# Download performance mods
echo "Downloading performance mods..."
curl -L -o "$MODS_DIR/spark-fabric.jar" "https://modrinth.com/mod/spark/version/latest"
curl -L -o "$MODS_DIR/c2me-fabric.jar" "https://modrinth.com/mod/c2me-fabric/version/latest"
curl -L -o "$MODS_DIR/lithium-fabric.jar" "https://modrinth.com/mod/lithium/version/latest"
curl -L -o "$MODS_DIR/scalablelux-fabric.jar" "https://modrinth.com/mod/scalablelux/version/latest"

# Create basic configurations
mkdir -p "$CONFIG_DIR/spark"
cat > "$CONFIG_DIR/spark/config.conf" << 'EOF'
spark {
    web-viewer {
        enabled = true
        port = 4567
    }
    profiler {
        max-stack-depth = 300
        thread-grouper = "by-name"
    }
}
EOF

echo "Performance tools setup complete!"
echo "Start your server and use '/spark profiler start' to begin profiling"
```

Make executable and run:
```bash
chmod +x setup-performance-tools.sh
./setup-performance-tools.sh
```

## Troubleshooting

### Common Issues

1. **Spark not loading**
   - Ensure Fabric API is installed
   - Check mod compatibility versions
   - Verify Java version compatibility

2. **async-profiler permission denied**
   ```bash
   # Linux: Add user to perf group
   sudo usermod -a -G perf $USER
   # Or set kernel parameter
   echo 1 | sudo tee /proc/sys/kernel/perf_event_paranoid
   ```

3. **JMH benchmarks not running**
   - Ensure proper Maven setup
   - Check annotation processor configuration
   - Verify JMH version compatibility

4. **High memory usage during profiling**
   - Increase JVM heap size: `-Xmx8G`
   - Use shorter profiling periods
   - Configure tool-specific memory limits