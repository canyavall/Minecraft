# Complete Installation Guide for Performance Mods

## Prerequisites

### System Requirements
- **Java**: Java 21 or newer (Java 22 recommended for some mods)
- **Memory**: At least 4GB RAM allocated to Minecraft (8GB+ recommended)
- **Graphics**: OpenGL 4.5 compatible graphics card for Sodium
- **CPU**: Multi-core processor recommended for C2ME benefits

### Base Installation
1. **Minecraft**: Version 1.21.1
2. **Fabric Loader**: Latest stable version (0.16.0+)
3. **Fabric API**: Latest version for 1.21.1

## Step-by-Step Installation

### Phase 1: Base Setup

#### 1. Install Fabric Loader
1. Download Fabric Installer from https://fabricmc.net/use/installer/
2. Run the installer and select Minecraft 1.21.1
3. Install Fabric Loader (latest stable version)
4. Launch Minecraft once to create the Fabric profile

#### 2. Install Fabric API
1. Download Fabric API for 1.21.1 from:
   - Modrinth: https://modrinth.com/mod/fabric-api
   - CurseForge: https://www.curseforge.com/minecraft/mc-mods/fabric-api
2. Place the .jar file in your `.minecraft/mods` folder

### Phase 2: Core Performance Mods (Install in this order)

#### 3. FerriteCore (Memory Optimization)
```
Version: Latest for 1.21.1
Download: Modrinth/CurseForge
Purpose: Reduces memory usage by 30-50%
Install: Place ferrite-core-[version].jar in mods folder
```
**Why first**: Memory optimizations should be loaded early to affect other mods

#### 4. Lithium (General Performance)
```
Version: 0.14.2 for Fabric 1.21.1
Download: https://modrinth.com/mod/lithium
File: lithium-fabric-[version].jar
Configuration: Optional config file for selective optimizations
```
**Features enabled by default**:
- AI pathfinding optimizations
- Physics engine improvements
- Entity processing optimizations
- Block tick optimizations

#### 5. C2ME (Chunk Processing)
```
Version: 0.3.0+alpha.0.293+1.21.1
Download: https://modrinth.com/mod/c2me-fabric
File: c2me-fabric-mc1.21.1-[version].jar
Configuration: config/c2me.toml (created after first run)
```
**Important**: This is an alpha version - backup your worlds!

### Phase 3: Specialized Performance Mods

#### 6. ScalableLux (Lighting Optimization)
```
Version: Latest for 1.21+
Download: https://modrinth.com/mod/scalablelux
Purpose: Modern lighting engine optimization
Replaces: Phosphor (don't use both)
```

#### 7. Sodium (Client-Side Rendering)
```
Version: 0.6.1 for Fabric 1.21.1
Download: https://modrinth.com/mod/sodium
File: sodium-fabric-[version].jar
Conflicts: OptiFine (choose one or the other)
```
**Client-side only**: Install on client, not server

#### 8. ModernFix (Performance Fixes)
```
Version: Latest for 1.21.1
Download: https://modrinth.com/mod/modernfix
Purpose: Collection of performance fixes and patches
Configuration: Extensive config file with toggleable fixes
```

### Phase 4: Profiling and Analysis Tools

#### 9. Spark (Performance Profiling)
```
Version: Latest Fabric plugin
Download: https://www.spigotmc.org/resources/spark.57242/
File: spark-[version]-fabric.jar
Usage: In-game commands for profiling
```

## Configuration Files

### C2ME Configuration (config/c2me.toml)
```toml
[clientSideConfig]
# Optimize client-side chunk processing
optimizeAsyncChunkRequest = true
optimizeChunkAccess = true

[generalOptimizations]
# Core optimizations
optimizeChunkTicking = true
optimizeEntityTicking = true
optimizeBlockEntityTicking = true

[threadedWorldGen]
# Threaded world generation settings
enabled = true
threadCount = 4  # Adjust based on CPU cores
```

### Lithium Configuration (config/lithium.properties)
```properties
# Enable all optimizations (default)
mixin.ai.pathing=true
mixin.block.hopper=true
mixin.entity.collisions=true
mixin.chunk.palette=true
mixin.world.block_entity_ticking=true
```

### ModernFix Configuration (config/modernfix-mixins.properties)
```properties
# Startup optimizations
mixin.perf.faster_texture_stitching=true
mixin.perf.faster_resource_loading=true
mixin.perf.launch_parallelization=true

# Runtime optimizations
mixin.perf.dynamic_entity_renderers=true
mixin.perf.faster_item_rendering=true
```

## JVM Arguments for Optimal Performance

### Recommended JVM Arguments
```bash
-Xms4G -Xmx8G
-XX:+UseG1GC
-XX:+ParallelRefProcEnabled
-XX:MaxGCPauseMillis=200
-XX:+UnlockExperimentalVMOptions
-XX:+DisableExplicitGC
-XX:+AlwaysPreTouch
-XX:G1HeapWastePercent=5
-XX:G1MixedGCCountTarget=4
-XX:G1MixedGCLiveThresholdPercent=90
-XX:G1RSetUpdatingPauseTimePercent=5
-XX:SurvivorRatio=32
-XX:+PerfDisableSharedMem
-XX:MaxTenuringThreshold=1
```

### Memory Allocation Guidelines
- **4GB RAM total**: -Xms2G -Xmx3G
- **8GB RAM total**: -Xms4G -Xmx6G
- **16GB RAM total**: -Xms6G -Xmx12G
- **32GB+ RAM total**: -Xms8G -Xmx16G

## Verification Steps

### 1. Startup Verification
1. Launch Minecraft with the Fabric profile
2. Check the mods list (Mod Menu recommended)
3. Verify all mods loaded without errors
4. Check latest.log for any warnings

### 2. Performance Testing
1. Create a new test world
2. Use F3 to monitor performance metrics
3. Run `/spark tps` command for TPS monitoring
4. Test chunk loading performance with long-distance travel

### 3. Configuration Verification
```bash
# Check if config files were created
.minecraft/config/c2me.toml
.minecraft/config/lithium.properties
.minecraft/config/modernfix-mixins.properties
```

## Troubleshooting Installation

### Common Issues

#### 1. Mod Conflicts
**Symptoms**: Crashes on startup, missing textures
**Solution**: Check for conflicting mods:
- OptiFine + Sodium = Remove OptiFine
- Phosphor + ScalableLux = Remove Phosphor
- Multiple performance mods with overlapping features

#### 2. Memory Issues
**Symptoms**: OutOfMemoryError, frequent GC pauses
**Solution**:
- Increase Xmx value in JVM arguments
- Add FerriteCore if not already installed
- Remove unnecessary resource packs

#### 3. Java Version Issues
**Symptoms**: "Unsupported Java version" errors
**Solution**:
- Ensure Java 21+ is installed
- Some mods may require Java 22
- Update Minecraft launcher Java settings

#### 4. Performance Regression
**Symptoms**: Worse performance than vanilla
**Solution**:
1. Test with one mod at a time
2. Check mod-specific configurations
3. Verify hardware compatibility (especially for Sodium)
4. Monitor with Spark to identify bottlenecks

## Installation Verification Checklist

- [ ] Fabric Loader installed and working
- [ ] Fabric API present in mods folder
- [ ] All performance mods present in mods folder
- [ ] No conflicting mods (OptiFine removed if using Sodium)
- [ ] Config files generated after first launch
- [ ] JVM arguments configured properly
- [ ] Test world loads without errors
- [ ] Performance metrics show improvements
- [ ] Spark profiling tools functional

## Post-Installation Optimization

### 1. Baseline Performance Measurement
```bash
# Use Spark to establish baseline metrics
/spark tps --timeout 60
/spark profiler --timeout 300
```

### 2. Incremental Testing
1. Test each mod's impact individually
2. Document performance gains/losses
3. Adjust configurations based on hardware
4. Create backup profiles for different use cases

### 3. Regular Maintenance
- Update mods monthly or when Minecraft updates
- Monitor for new performance mods
- Backup configurations before updates
- Document any custom settings or modifications