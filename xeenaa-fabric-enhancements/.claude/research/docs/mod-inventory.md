# Performance Mods Inventory

## Overview
This document provides a comprehensive inventory of key performance mods for Minecraft Fabric 1.21.1, including download information, features, and optimization details.

## Core Performance Mods

### 1. C2ME (Concurrent Chunk Management Engine)
- **Current Version**: 0.3.0+alpha.0.293+1.21.1
- **Download Sources**:
  - Modrinth: https://modrinth.com/mod/c2me-fabric
  - CurseForge: https://www.curseforge.com/minecraft/mc-mods/c2me
- **Purpose**: Improves chunk generation, I/O, and loading performance using multiple CPU cores
- **Key Features**:
  - Parallel chunk processing
  - Optimized I/O operations
  - Multi-threaded chunk generation
- **Dependencies**: Fabric API
- **Recommended Companions**: Lithium, ScalableLux
- **Performance Impact**: 30-50% faster chunk loading on multi-core systems
- **Configuration**: Available via config file for thread counts and optimization levels

### 2. Lithium
- **Current Version**: Latest for Fabric 1.21.1
- **Download Sources**:
  - Modrinth: https://modrinth.com/mod/lithium
  - CurseForge: https://www.curseforge.com/minecraft/mc-mods/lithium
- **Purpose**: General-purpose optimization mod focusing on game logic improvements
- **Key Features**:
  - AI pathfinding optimizations
  - Physics engine improvements
  - Entity processing optimizations
  - Block and fluid tick optimizations
- **Dependencies**: Fabric API
- **Performance Impact**: 15-25% overall performance improvement
- **Configuration**: Modular configuration system allowing selective optimizations

### 3. Sodium
- **Current Version**: Latest for Fabric 1.21.1
- **Download Sources**:
  - Modrinth: https://modrinth.com/mod/sodium
  - CurseForge: https://www.curseforge.com/minecraft/mc-mods/sodium
- **Purpose**: Client-side rendering optimization
- **Key Features**:
  - Modern OpenGL rendering pipeline
  - Chunk rendering optimizations
  - Reduced CPU overhead for rendering
  - Advanced graphics options
- **Dependencies**: Fabric API
- **Performance Impact**: 50-200% FPS improvement on most systems
- **Configuration**: In-game options menu with detailed graphics settings

### 4. ScalableLux
- **Current Version**: Latest for 1.21+
- **Download Sources**:
  - Modrinth: https://modrinth.com/mod/scalablelux
- **Purpose**: Modern lighting engine optimization
- **Key Features**:
  - Scalable light computation
  - Optimized light propagation algorithms
  - Reduced lighting-related lag
  - Better performance with large builds
- **Dependencies**: Fabric API
- **Performance Impact**: 20-40% improvement in lighting-heavy scenarios
- **Configuration**: Automatic optimization with optional manual tuning

### 5. FerriteCore
- **Current Version**: Latest for Fabric 1.21.1
- **Download Sources**:
  - Modrinth: https://modrinth.com/mod/ferrite-core
  - CurseForge: https://www.curseforge.com/minecraft/mc-mods/ferritecore-fabric
- **Purpose**: Memory usage optimization
- **Key Features**:
  - Reduces memory usage by optimizing data structures
  - Faster startup times
  - Lower RAM requirements
  - JVM garbage collection improvements
- **Dependencies**: Fabric API
- **Performance Impact**: 30-50% memory usage reduction
- **Configuration**: Mostly automatic with optional advanced settings

### 6. ModernFix
- **Current Version**: Latest for Fabric 1.21.1
- **Download Sources**:
  - Modrinth: https://modrinth.com/mod/modernfix
  - CurseForge: https://www.curseforge.com/minecraft/mc-mods/modernfix
- **Purpose**: Collection of performance fixes and optimizations
- **Key Features**:
  - Startup time improvements
  - Memory leak fixes
  - Various performance patches
  - Compatibility improvements
- **Dependencies**: Fabric API
- **Performance Impact**: 15-30% overall improvement, significant startup time reduction
- **Configuration**: Extensive config file with toggleable fixes

## Performance Analysis Tools

### 1. Spark
- **Current Version**: Latest Fabric plugin
- **Download Sources**:
  - SpigotMC: https://www.spigotmc.org/resources/spark.57242/
  - GitHub: https://github.com/lucko/spark
- **Purpose**: Advanced performance profiling and monitoring
- **Key Features**:
  - CPU profiling with flame graphs
  - Memory usage analysis
  - TPS monitoring
  - Real-time performance metrics
- **Usage**: Command-based profiling with web interface
- **Output Formats**: Flame graphs, detailed reports, exportable data

### 2. JProfiler
- **Type**: Commercial Java profiling tool
- **Purpose**: Comprehensive JVM profiling
- **Key Features**:
  - CPU profiling
  - Memory analysis
  - Thread analysis
  - Database profiling
- **Setup**: Requires license, integrates with Minecraft launcher
- **Output**: Detailed GUI-based analysis tools

### 3. async-profiler
- **Type**: Open-source Java profiling tool
- **Purpose**: Low-overhead profiling for production environments
- **Key Features**:
  - CPU and allocation profiling
  - Flame graph generation
  - Low overhead
  - Java and native code profiling
- **Setup**: Download binary, attach to Minecraft process
- **Output**: Flame graphs and detailed reports

## Compatibility Matrix

| Mod | Minecraft Version | Fabric Loader | Java Version | Conflicts |
|-----|------------------|---------------|--------------|-----------|
| C2ME | 1.21.1 | Latest | Java 21+ | None known |
| Lithium | 1.21.1 | Latest | Java 21+ | None known |
| Sodium | 1.21.1 | Latest | Java 21+ | OptiFine |
| ScalableLux | 1.21+ | Latest | Java 21+ | Phosphor |
| FerriteCore | 1.21.1 | Latest | Java 21+ | None known |
| ModernFix | 1.21.1 | Latest | Java 21+ | None known |

## Performance Stack Recommendations

### Server-Side Stack
1. C2ME (chunk processing)
2. Lithium (general optimizations)
3. ScalableLux (lighting)
4. FerriteCore (memory)
5. ModernFix (fixes)

### Client-Side Stack
1. Sodium (rendering)
2. Lithium (general optimizations)
3. FerriteCore (memory)
4. ModernFix (fixes)

### Development/Testing Stack
- All performance mods above
- Spark for profiling
- async-profiler for detailed analysis
- JProfiler for comprehensive debugging

## Installation Order
1. Fabric Loader
2. Fabric API
3. FerriteCore (first for memory optimizations)
4. Lithium (general optimizations)
5. C2ME (chunk processing)
6. ScalableLux (lighting)
7. Sodium (client-side rendering)
8. ModernFix (final fixes and patches)
9. Spark (profiling tool)

## Known Issues and Considerations
- C2ME is in alpha/beta, may have stability issues
- Sodium conflicts with OptiFine
- Some mods may require Java 22 instead of Java 21
- Regular backups recommended when using alpha/beta mods
- Performance gains vary by hardware configuration