# Installing Spark Profiler for Performance Testing

Spark is the most accurate tool for measuring Minecraft performance improvements.

## Installation Steps

### 1. Download Spark
- Go to: https://spark.lucko.me/download
- Download Spark for Fabric 1.21.1
- Place in your `mods/` folder

### 2. Install in Test Environment
```bash
# Create test server directory
mkdir minecraft_test_server
cd minecraft_test_server

# Download Fabric server
# Get from: https://fabricmc.net/use/server/

# Copy your mod + Spark
cp xeenaa-fabric-enhancements-*.jar mods/
cp spark-fabric-*.jar mods/
```

### 3. Basic Usage
```bash
# Start profiling
/spark profiler start

# Run your performance test (chunk loading, world gen, etc.)
# ... wait 60-300 seconds ...

# Stop and get results
/spark profiler stop
/spark profiler info
```

### 4. Advanced Profiling
```bash
# CPU profiling for 2 minutes
/spark profiler --timeout 120 --thread *

# Memory profiling
/spark heap --include-non-loaded-classes

# Method-specific profiling
/spark profiler --timeout 60 --only-ticks-over 50
```

## Interpreting Results

### Key Metrics to Watch
- **Tick duration**: Should be <50ms
- **Method call frequency**: Look for hotspots
- **Memory allocation**: Check for memory leaks
- **Thread usage**: Ensure efficient threading

### Before/After Comparison
1. Run Spark profiling with vanilla/baseline
2. Save the profile links
3. Implement your optimizations
4. Run Spark profiling again
5. Compare the flame graphs

### Example Improvement Indicators
- Reduced time in chunk loading methods
- Lower memory allocation rates
- Fewer garbage collection events
- More efficient method call patterns

## Automated Testing with Spark
You can integrate Spark into your automated testing:

```bash
# Start server with Spark auto-profiling
java -jar server.jar --spark-auto-profile --spark-timeout 300
```