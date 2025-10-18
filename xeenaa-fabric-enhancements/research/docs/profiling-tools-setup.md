# Comprehensive Profiling Tools Setup Guide

## Overview

This guide covers the setup and usage of three essential profiling tools for Minecraft performance analysis: Spark, async-profiler, and JProfiler. Each tool serves different purposes and provides unique insights into performance bottlenecks.

## Tool Comparison Matrix

| Tool | Type | Cost | Best For | Platform Support | Overhead |
|------|------|------|----------|------------------|----------|
| Spark | In-game Plugin | Free | Real-time server profiling | All | Very Low |
| async-profiler | JVM Profiler | Free | Production profiling | Linux/macOS/Windows | Low |
| JProfiler | Commercial | Paid | Deep development analysis | All | Medium |

---

## 1. Spark Profiler

### Overview
Spark is a performance profiler for Minecraft clients, servers, and proxies. It's designed to be used in production environments with minimal performance impact.

### Installation

#### For Fabric Servers/Clients
```bash
# Download latest version
wget https://ci.lucko.me/job/spark/lastSuccessfulBuild/artifact/spark-fabric/build/libs/spark-fabric-[version].jar

# Place in mods folder
cp spark-fabric-[version].jar .minecraft/mods/
```

#### Verification
```bash
# In-game or console command
/spark version
```

### Basic Usage Commands

#### Server Performance Monitoring
```bash
# Monitor TPS for 60 seconds
/spark tps --timeout 60

# CPU profiling for 5 minutes
/spark profiler --timeout 300

# Memory usage profiling
/spark heap

# Monitor specific event (e.g., chunk loading)
/spark profiler --only-ticks-over 50
```

#### Advanced Profiling
```bash
# Profile with flame graph output
/spark profiler --timeout 180 --thread *

# Profile memory allocations
/spark profiler --timeout 120 --alloc

# Profile with sampling interval
/spark profiler --timeout 300 --interval 4ms
```

### Web Interface
After running profiling commands, Spark generates web-based reports accessible via the provided URLs. These include:
- Interactive flame graphs
- Call tree analysis
- Timing breakdowns
- Memory allocation reports

### Configuration
```toml
# config/spark/spark.conf
# Basic Spark configuration

[general]
# Enable profiling in production
enable-profiling = true

# Web interface settings
web-interface-enabled = true
web-interface-port = 4567

[profiling]
# Default profiling duration (seconds)
default-timeout = 300

# Sampling interval
sampling-interval = "4ms"

# Thread filtering
profile-all-threads = false
```

---

## 2. async-profiler

### Overview
async-profiler is a low-overhead sampling profiler for Java applications that avoids safepoint bias and provides accurate performance insights.

### Installation

#### Download and Setup
```bash
# Download latest version
wget https://github.com/async-profiler/async-profiler/releases/latest/download/async-profiler-2.9-linux-x64.tar.gz

# Extract
tar -xzf async-profiler-2.9-linux-x64.tar.gz
cd async-profiler-2.9-linux-x64

# Set permissions (Linux only)
sudo sysctl kernel.perf_event_paranoid=1
sudo sysctl kernel.kptr_restrict=0
```

#### Windows Setup
```powershell
# Download Windows version
# https://github.com/async-profiler/async-profiler/releases/latest
# Extract to desired location
```

### JVM Configuration for Optimal Results
```bash
# Add to Minecraft JVM arguments
-XX:+UnlockDiagnosticVMOptions
-XX:+DebugNonSafepoints
-XX:+FlightRecorder
```

### Basic Usage

#### CPU Profiling
```bash
# Profile CPU for 30 seconds, output to SVG flame graph
./profiler.sh -d 30 -f cpu-profile.svg [MINECRAFT_PID]

# Profile specific threads only
./profiler.sh -d 60 -t -f cpu-threaded.svg [MINECRAFT_PID]

# Profile with higher sampling frequency
./profiler.sh -d 30 -i 1ms -f cpu-detailed.svg [MINECRAFT_PID]
```

#### Memory Allocation Profiling
```bash
# Profile memory allocations
./profiler.sh -e alloc -d 60 -f memory-alloc.svg [MINECRAFT_PID]

# Profile allocations over 1KB only
./profiler.sh -e alloc -d 60 --alloc 1k -f memory-large.svg [MINECRAFT_PID]
```

#### Lock Contention Analysis
```bash
# Profile lock contention
./profiler.sh -e lock -d 120 -f lock-contention.svg [MINECRAFT_PID]
```

### Advanced Profiling Scenarios

#### Continuous Profiling
```bash
#!/bin/bash
# continuous-profile.sh
MINECRAFT_PID=$1
DURATION=${2:-300}

while true; do
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    ./profiler.sh -d $DURATION -f "profile_${TIMESTAMP}.svg" $MINECRAFT_PID
    sleep $((DURATION + 60))  # 1-minute gap between profiles
done
```

#### Server Startup Profiling
```bash
# Profile server startup process
./profiler.sh -e cpu,alloc -d 120 -f startup-profile.svg [MINECRAFT_PID]
```

### Output Formats
```bash
# SVG flame graph (default)
./profiler.sh -d 30 -f output.svg [PID]

# HTML interactive flame graph
./profiler.sh -d 30 -f output.html [PID]

# JFR (Java Flight Recorder) format
./profiler.sh -d 30 -f output.jfr [PID]

# Collapsed stack traces
./profiler.sh -d 30 -o collapsed [PID] > stacks.txt
```

---

## 3. JProfiler

### Overview
JProfiler is a commercial Java profiler that provides comprehensive analysis capabilities with an intuitive GUI. It's particularly powerful for development and detailed analysis.

### Installation and Licensing
1. Download from https://www.ej-technologies.com/jprofiler/download
2. Install for your platform (Windows/macOS/Linux)
3. Obtain license (commercial) or use 10-day trial
4. Launch JProfiler application

### Minecraft Integration Setup

#### Method 1: Attach to Running Process
1. Start Minecraft normally
2. In JProfiler: Session → Attach to Running JVM
3. Select Minecraft process from list
4. Choose profiling options

#### Method 2: JVM Startup Integration
```bash
# Add to Minecraft JVM arguments
-agentpath:[JPROFILER_HOME]/bin/[PLATFORM]/libjprofilerti.so=port=8849
# Windows: -agentpath:C:\Program Files\jprofiler15\bin\windows-x64\jprofilerti.dll=port=8849
```

### Key Profiling Views

#### 1. CPU Profiling
- **Call Tree**: Shows method call hierarchy and timing
- **Hot Spots**: Identifies methods consuming most CPU time
- **Thread Views**: Analyzes thread behavior and synchronization

#### 2. Memory Analysis
- **Heap Snapshot**: Complete memory state analysis
- **Memory Leaks**: Automated leak detection
- **Allocation Recording**: Track object creation patterns

#### 3. Thread Analysis
- **Thread Monitor**: Real-time thread state visualization
- **Deadlock Detection**: Automatic deadlock identification
- **Synchronization Analysis**: Lock contention analysis

### Minecraft-Specific Profiling Strategies

#### Server Performance Analysis
```java
// Configure JProfiler for server analysis
Session settings:
- CPU profiling: Sampling mode (low overhead)
- Memory: Allocation recording enabled
- Threads: Monitor all server threads
- Filters: Focus on net.minecraft.* packages
```

#### Client Performance Analysis
```java
// Client-specific configuration
Focus areas:
- Rendering pipeline (OpenGL calls)
- World loading and chunk generation
- Entity processing and AI
- Resource loading and caching
```

### Remote Profiling Setup

#### SSH-based Remote Profiling
1. Configure SSH access to remote server
2. In JProfiler: Session → Integration → Remote
3. Enter SSH credentials and server details
4. JProfiler handles remote agent deployment automatically

#### Manual Remote Agent
```bash
# On remote server
java -agentpath:/path/to/jprofiler/bin/linux-x64/libjprofilerti.so=port=8849,nowait \
     -jar minecraft-server.jar

# Connect from JProfiler client
# Session → Attach to remote JVM → host:8849
```

### Best Practices

#### Sampling vs. Exact Profiling
- **Sampling**: Use for production and initial analysis (low overhead)
- **Exact**: Use for development and detailed method analysis (higher overhead)

#### Filter Configuration
```java
// Recommended filters for Minecraft profiling
Include filters:
- net.minecraft.**
- mod.packagename.**  // Your mod packages
- org.spongepowered.**  // If using Sponge

Exclude filters:
- java.**
- sun.**
- javax.**
- com.mojang.authlib.**  // Unless debugging auth issues
```

#### Snapshot Analysis
1. Take snapshots at regular intervals
2. Compare snapshots to identify trends
3. Export data for long-term analysis
4. Use snapshot comparison for A/B testing

---

## Profiling Workflow Recommendations

### 1. Initial Performance Assessment
```bash
# Step 1: Quick TPS check with Spark
/spark tps --timeout 60

# Step 2: Basic CPU profiling
/spark profiler --timeout 300

# Step 3: If issues found, deeper analysis with async-profiler
./profiler.sh -d 600 -f detailed-analysis.svg [PID]
```

### 2. Memory Issue Investigation
```bash
# Step 1: Check memory usage trends
/spark heap

# Step 2: Profile allocations
./profiler.sh -e alloc -d 300 -f memory-profile.svg [PID]

# Step 3: Detailed analysis with JProfiler
# Use memory snapshots and leak detection
```

### 3. Performance Optimization Cycle
1. **Baseline**: Establish current performance metrics
2. **Profile**: Identify bottlenecks using appropriate tool
3. **Optimize**: Make targeted improvements
4. **Verify**: Re-profile to confirm improvements
5. **Document**: Record changes and performance gains

### 4. Production Monitoring
```bash
# Automated monitoring script
#!/bin/bash
# monitor-performance.sh

# Daily TPS monitoring
/spark tps --timeout 300 > daily-tps-$(date +%Y%m%d).log

# Weekly detailed profiling
if [ $(date +%u) -eq 1 ]; then  # Monday
    ./profiler.sh -d 900 -f weekly-profile-$(date +%Y%m%d).svg [PID]
fi

# Alert on performance degradation
CURRENT_TPS=$(spark tps --timeout 60 | grep "Average" | awk '{print $3}')
if (( $(echo "$CURRENT_TPS < 18.0" | bc -l) )); then
    echo "Performance alert: TPS dropped to $CURRENT_TPS" | mail -s "Server Performance Alert" admin@domain.com
fi
```

## Tool Selection Guidelines

### Use Spark When:
- Real-time monitoring needed
- Minimal performance impact required
- Quick performance checks
- Integrated Minecraft analysis preferred

### Use async-profiler When:
- Production environment profiling
- Need flame graphs for visualization
- Low-overhead continuous monitoring
- Open-source solution preferred

### Use JProfiler When:
- Detailed development analysis needed
- GUI-based analysis preferred
- Commercial support required
- Complex memory leak investigation
- Thread synchronization issues

## Integration with CI/CD

### Automated Performance Testing
```yaml
# .github/workflows/performance-test.yml
name: Performance Testing
on: [pull_request]

jobs:
  performance:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Setup Java
        uses: actions/setup-java@v2
        with:
          java-version: '21'
      - name: Build and Test
        run: ./gradlew build
      - name: Performance Baseline
        run: |
          java -jar server.jar &
          SERVER_PID=$!
          sleep 30
          ./async-profiler/profiler.sh -d 120 -f ci-profile.svg $SERVER_PID
          kill $SERVER_PID
      - name: Upload Profile
        uses: actions/upload-artifact@v2
        with:
          name: performance-profile
          path: ci-profile.svg
```

This comprehensive setup guide provides everything needed to implement effective performance profiling for Minecraft Fabric environments using all three tools in combination.