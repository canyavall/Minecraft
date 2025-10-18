# Comprehensive Troubleshooting Guide

## Overview

This guide addresses common issues encountered when using performance mods with Minecraft Fabric 1.21.1. It provides systematic troubleshooting steps, solutions, and preventive measures.

## Quick Diagnosis Checklist

Before diving into specific issues, run through this quick checklist:

- [ ] Java version is 21+ (some mods require Java 22)
- [ ] Fabric Loader is latest stable version
- [ ] Fabric API is installed and up to date
- [ ] No conflicting mods (OptiFine with Sodium, Phosphor with ScalableLux)
- [ ] Sufficient memory allocated (4GB minimum, 8GB+ recommended)
- [ ] Check latest.log for error messages
- [ ] Verify mod versions are compatible with Minecraft 1.21.1

## Common Installation Issues

### Issue 1: Mods Not Loading

#### Symptoms
- Mods don't appear in mod list
- No performance improvements observed
- Missing mod features

#### Causes and Solutions

**Cause: Incorrect mod loader**
```
Problem: Forge mods in Fabric mods folder
Solution: Verify all mods are Fabric versions
Check: File names should contain "fabric" not "forge"
```

**Cause: Wrong Minecraft version**
```
Problem: 1.20.x mods in 1.21.1 installation
Solution: Download specific 1.21.1 compatible versions
Check: Mod description pages for version compatibility
```

**Cause: Missing dependencies**
```
Problem: Fabric API not installed
Solution: Download and install Fabric API for 1.21.1
Check: Some mods require additional dependencies
```

#### Diagnostic Steps
```bash
# Check mod loading in latest.log
grep -i "loading.*mod" logs/latest.log

# Verify Fabric loader version
grep -i "fabric.*loader" logs/latest.log

# Check for dependency errors
grep -i "dependency" logs/latest.log
```

### Issue 2: Server Won't Start

#### Symptoms
- Server crashes during startup
- "Incompatible mod set" errors
- OutOfMemoryError during loading

#### Common Causes

**Java Version Issues**
```
Error: "Unsupported major.minor version"
Solution: Update to Java 21 or newer
Command: java -version
Expected: openjdk version "21.0.x" or higher
```

**Memory Allocation Problems**
```
Error: "OutOfMemoryError" during startup
Solution: Increase heap size
JVM Args: -Xms4G -Xmx8G (adjust based on available RAM)
Check: Ensure system has enough free memory
```

**Mod Conflicts**
```
Error: "Mod X conflicts with Mod Y"
Solution: Remove conflicting mods
Common conflicts:
- OptiFine + Sodium
- Phosphor + ScalableLux
- Multiple chunk optimization mods
```

#### Diagnostic Steps
```bash
# Check startup errors
tail -n 100 logs/latest.log | grep -i "error"

# Memory usage during startup
jstat -gc [PID] 1s 60s

# Check for mod conflicts
grep -i "conflict" logs/latest.log
```

### Issue 3: Performance Degradation

#### Symptoms
- Lower FPS than vanilla
- Increased lag or stuttering
- Higher memory usage
- Poor server TPS

#### Troubleshooting Steps

**Step 1: Baseline Comparison**
```bash
# Test vanilla performance first
# Remove all mods, test for 30 minutes
/spark tps --timeout 1800

# Add mods one by one
# Test each addition for performance impact
```

**Step 2: Identify Problematic Mods**
```bash
# Profile with each mod individually
/spark profiler --timeout 300

# Check for excessive resource usage
top -p [MINECRAFT_PID]
```

**Step 3: Configuration Issues**
```bash
# Check mod configurations
ls -la config/
# Review config files for performance settings
# Reset to defaults if needed
```

## Mod-Specific Issues

### C2ME (Concurrent Chunk Management Engine)

#### Issue: World Corruption
**Symptoms**: Chunks missing, world holes, data loss
**Cause**: Alpha/beta software instability
**Solution**:
```bash
# Immediate action
1. Stop server immediately
2. Restore from backup
3. Remove C2ME temporarily
4. Update to latest C2ME version
5. Test in isolated environment first
```

**Prevention**:
```bash
# Enable more frequent backups
# Create hourly backups during C2ME usage
#!/bin/bash
while true; do
    cp -r world world_backup_$(date +%Y%m%d_%H%M%S)
    sleep 3600
done
```

#### Issue: High CPU Usage
**Symptoms**: 100% CPU usage, system slowdown
**Cause**: Too many chunk processing threads
**Solution**:
```toml
# config/c2me.toml
[threadedWorldGen]
enabled = true
threadCount = 4  # Reduce from default (usually CPU cores - 2)

[clientSideConfig]
optimizeAsyncChunkRequest = true
asyncChunkRequestThreadCount = 2  # Limit concurrent requests
```

### Lithium

#### Issue: AI Behavior Changes
**Symptoms**: Mobs acting differently, pathfinding issues
**Cause**: Aggressive AI optimizations
**Solution**:
```properties
# config/lithium.properties
# Disable specific AI optimizations
mixin.ai.pathing=false
mixin.ai.nearby_entity_tracking=false

# Keep other optimizations
mixin.block.hopper=true
mixin.entity.collisions=true
```

#### Issue: Redstone Timing Issues
**Symptoms**: Redstone contraptions breaking, timing changes
**Cause**: Block update optimizations
**Solution**:
```properties
# Disable redstone-affecting optimizations
mixin.block.redstone=false
mixin.world.block_entity_ticking=false

# Monitor for specific issues
# Test contraptions individually
```

### Sodium

#### Issue: Graphics Artifacts
**Symptoms**: Missing textures, flickering, graphical glitches
**Cause**: GPU driver compatibility issues
**Solution**:
```bash
# Update graphics drivers first
# NVIDIA: Download latest from nvidia.com
# AMD: Download latest from amd.com
# Intel: Update through Windows Update

# Try compatibility mode
# Video Settings → Advanced → Use OpenGL 3.3 (compatibility)
```

#### Issue: Crashes with Shaders
**Symptoms**: Game crashes when enabling shaders
**Cause**: Sodium conflicts with OptiFine shaders
**Solution**:
```bash
# Remove OptiFine completely
# Install Iris for shader support with Sodium
# Download: https://modrinth.com/mod/iris

# Compatible shader packs:
# - BSL Shaders
# - Complementary Shaders
# - Sildur's Vibrant Shaders
```

### FerriteCore

#### Issue: Increased Memory Usage
**Symptoms**: Higher memory usage than expected
**Cause**: Profiling overhead or misconfiguration
**Solution**:
```bash
# Check if profiling is enabled
grep -i "profile" config/ferritecore.properties

# Disable debug features
debug.enableMemoryProfiling=false
debug.logMemoryUsage=false
```

### ScalableLux

#### Issue: Lighting Glitches
**Symptoms**: Dark spots, incorrect lighting, visual artifacts
**Cause**: Lighting engine conflicts or bugs
**Solution**:
```bash
# Remove conflicting lighting mods
# Check for Phosphor and remove it
rm mods/phosphor*.jar

# Reset lighting in affected areas
# Use /fill command to update light levels
/fill ~ ~ ~ ~16 ~16 ~16 air replace air
```

### ModernFix

#### Issue: Startup Problems
**Symptoms**: Longer startup times, loading failures
**Cause**: Incompatible optimizations
**Solution**:
```properties
# config/modernfix-mixins.properties
# Disable problematic optimizations
mixin.perf.launch_parallelization=false
mixin.perf.faster_texture_stitching=false

# Enable one by one to identify issues
```

## Performance Debugging

### Systematic Performance Analysis

#### Step 1: Establish Baseline
```bash
# Vanilla server test (30 minutes)
java -Xms4G -Xmx8G -XX:+UseG1GC -jar server.jar nogui

# Measure baseline metrics
/spark tps --timeout 1800
/spark profiler --timeout 1800
```

#### Step 2: Individual Mod Testing
```bash
# Test each mod individually
for mod in lithium sodium ferritecore c2me scalablelux modernfix; do
    echo "Testing $mod..."
    # Install only this mod + Fabric API
    # Run 30-minute test
    # Record performance metrics
    # Remove mod before next test
done
```

#### Step 3: Cumulative Testing
```bash
# Add mods in order of importance/stability
# 1. FerriteCore (memory optimization)
# 2. Lithium (general performance)
# 3. ModernFix (startup and fixes)
# 4. ScalableLux (lighting)
# 5. Sodium (client-side only)
# 6. C2ME (last, most experimental)
```

### Profiling Commands

#### Server Performance
```bash
# TPS monitoring
/spark tps --timeout 300

# CPU profiling
/spark profiler --timeout 600 --thread *

# Memory analysis
/spark heap

# Specific thread analysis
/spark profiler --timeout 300 --only-ticks-over 50
```

#### System Resource Monitoring
```bash
# CPU and memory usage
top -b -n 60 -d 1 | grep java

# Disk I/O
iostat -x 1 60

# Network usage
netstat -i

# JVM garbage collection
jstat -gc [PID] 1s 60s
```

## Configuration Optimization

### JVM Tuning for Problematic Scenarios

#### High Memory Usage
```bash
# Optimized JVM arguments for memory efficiency
-Xms4G -Xmx6G
-XX:+UseG1GC
-XX:G1HeapWastePercent=5
-XX:G1MixedGCCountTarget=4
-XX:+UnlockExperimentalVMOptions
-XX:G1MixedGCLiveThresholdPercent=90
```

#### High CPU Usage
```bash
# CPU optimization arguments
-XX:+UseG1GC
-XX:+ParallelRefProcEnabled
-XX:MaxGCPauseMillis=200
-XX:+UnlockExperimentalVMOptions
-XX:+DisableExplicitGC
```

#### Large Player Count
```bash
# High-load server optimization
-Xms8G -Xmx12G
-XX:+UseG1GC
-XX:G1HeapRegionSize=32m
-XX:G1NewSizePercent=20
-XX:G1ReservePercent=20
-XX:MaxGCPauseMillis=50
```

### Mod Configuration Templates

#### Conservative Configuration (Stability Focus)
```toml
# C2ME - Conservative settings
[threadedWorldGen]
enabled = true
threadCount = 2  # Half of CPU cores

[generalOptimizations]
optimizeChunkTicking = false  # Disable for stability
optimizeEntityTicking = true
```

```properties
# Lithium - Safe optimizations only
mixin.ai.pathing=true
mixin.block.hopper=true
mixin.entity.collisions=true
mixin.chunk.palette=true
# Disable potentially problematic optimizations
mixin.ai.nearby_entity_tracking=false
mixin.world.explosions=false
```

#### Performance Configuration (Speed Focus)
```toml
# C2ME - Aggressive settings
[threadedWorldGen]
enabled = true
threadCount = 6  # CPU cores - 2

[generalOptimizations]
optimizeChunkTicking = true
optimizeEntityTicking = true
optimizeBlockEntityTicking = true
```

```properties
# Lithium - All optimizations enabled
mixin.ai.pathing=true
mixin.ai.nearby_entity_tracking=true
mixin.block.hopper=true
mixin.entity.collisions=true
mixin.chunk.palette=true
mixin.world.block_entity_ticking=true
mixin.world.explosions=true
```

## Emergency Procedures

### Server Won't Start
```bash
# Emergency recovery steps
1. Copy logs/latest.log to safe location
2. Remove all mods except Fabric API
3. Test if server starts
4. If yes: Add mods one by one
5. If no: Check Java version and memory allocation
```

### World Corruption
```bash
# Immediate response
1. Stop server immediately: kill [PID]
2. Don't restart until backup restored
3. Copy corrupted world to separate folder
4. Restore from most recent backup
5. Identify last change before corruption
```

### Performance Crisis
```bash
# Performance emergency response
1. Monitor resource usage: top, htop, Task Manager
2. Identify problematic process/thread
3. If server TPS < 15: Restart immediately
4. Remove recently added mods
5. Test with vanilla to confirm hardware capability
```

### Data Recovery
```bash
# Backup and recovery procedures
# Automated backup script
#!/bin/bash
BACKUP_DIR="/backups/minecraft"
WORLD_DIR="/server/world"
DATE=$(date +%Y%m%d_%H%M%S)

# Create backup
tar -czf "$BACKUP_DIR/world_backup_$DATE.tar.gz" "$WORLD_DIR"

# Keep only last 48 backups (48 hours if hourly)
ls -t "$BACKUP_DIR"/world_backup_*.tar.gz | tail -n +49 | xargs rm -f
```

## Monitoring and Prevention

### Automated Health Checks
```bash
#!/bin/bash
# health-check.sh - Run every 5 minutes via cron

TPS=$(spark tps --timeout 60 | grep "Average" | awk '{print $3}')
MEMORY_USAGE=$(ps -p [PID] -o %mem --no-headers)

# Alert conditions
if (( $(echo "$TPS < 18.0" | bc -l) )); then
    echo "WARNING: TPS dropped to $TPS" | mail -s "Server Performance Alert" admin@domain.com
fi

if (( $(echo "$MEMORY_USAGE > 90.0" | bc -l) )); then
    echo "WARNING: Memory usage at $MEMORY_USAGE%" | mail -s "Memory Alert" admin@domain.com
fi
```

### Log Monitoring
```bash
# Monitor for common error patterns
tail -f logs/latest.log | grep -E "(ERROR|WARN|Exception|OutOfMemory)"

# Set up log rotation
# Add to /etc/logrotate.d/minecraft
/path/to/minecraft/logs/*.log {
    daily
    rotate 30
    compress
    missingok
    notifempty
    create 0644 minecraft minecraft
}
```

### Performance Trending
```bash
# Daily performance report
#!/bin/bash
DATE=$(date +%Y-%m-%d)
REPORT_FILE="performance_report_$DATE.txt"

echo "Daily Performance Report - $DATE" > $REPORT_FILE
echo "=================================" >> $REPORT_FILE

# TPS average over last 24 hours
spark tps --timeout 86400 >> $REPORT_FILE

# Memory usage trend
echo "Memory Usage:" >> $REPORT_FILE
cat /var/log/minecraft/memory.log | tail -n 144 >> $REPORT_FILE

# Send report
mail -s "Daily Performance Report" admin@domain.com < $REPORT_FILE
```

This comprehensive troubleshooting guide provides systematic approaches to identify, diagnose, and resolve common issues with performance mods, ensuring stable and optimized Minecraft server operations.