# Performance Analysis Tools Comparison Matrix

## Tool Overview

| Tool | Type | Platform Support | Minecraft 1.21.1 Compatible | Performance Overhead | License |
|------|------|------------------|------------------------------|---------------------|---------|
| **Spark** | Profiler | All | ✅ Yes | Minimal (production safe) | GPL v3 |
| **async-profiler** | Native Profiler | Linux/macOS | ✅ Yes (via Spark) | Ultra-low | Apache 2.0 |
| **JProfiler** | Commercial Profiler | All | ✅ Yes | Low-Medium | Commercial |
| **JFR** | Built-in Profiler | All | ✅ Yes | Very Low | Oracle Binary |
| **VisualVM** | Free Profiler | All | ✅ Yes | Medium | GPL v2 |
| **Eclipse MAT** | Memory Analyzer | All | ✅ Yes | N/A (heap analysis) | EPL |
| **JConsole** | JVM Monitor | All | ✅ Yes | Low | Oracle Binary |
| **JMH** | Benchmarking | All | ✅ Yes | N/A (testing only) | GPL v2 |

## Fabric-Specific Tools

| Tool | Purpose | Compatibility | Key Features |
|------|---------|---------------|---------------|
| **C2ME** | Chunk Loading | 1.17.1 - 1.21.x | Parallel chunk generation, 70% worldgen speedup |
| **Lithium** | General Optimization | Current versions | Game logic optimization, server/client |
| **Starlight** | Lighting Engine | 1.17.x - 1.20.4 | Complete lighting rewrite (archived) |
| **ScalableLux** | Lighting Engine | 1.21+ | Starlight fork for newer versions |
| **Sodium** | Rendering | 1.16.1 - 1.21.x | Graphics engine replacement |

## Metrics Capability Matrix

| Tool | CPU Profiling | Memory Analysis | Chunk Loading | Tick Performance | GC Monitoring | Thread Analysis |
|------|---------------|-----------------|---------------|------------------|---------------|-----------------|
| **Spark** | ✅ Excellent | ✅ Good | ✅ Good | ✅ Excellent | ✅ Good | ✅ Excellent |
| **async-profiler** | ✅ Excellent | ✅ Excellent | ✅ Good | ✅ Good | ✅ Good | ✅ Excellent |
| **JProfiler** | ✅ Excellent | ✅ Excellent | ✅ Limited | ✅ Good | ✅ Excellent | ✅ Excellent |
| **JFR** | ✅ Good | ✅ Good | ✅ Limited | ✅ Good | ✅ Excellent | ✅ Good |
| **VisualVM** | ✅ Good | ✅ Good | ✅ Limited | ✅ Limited | ✅ Good | ✅ Good |
| **Eclipse MAT** | ❌ No | ✅ Excellent | ❌ No | ❌ No | ❌ No | ❌ No |
| **JConsole** | ✅ Limited | ✅ Good | ❌ No | ✅ Limited | ✅ Good | ✅ Limited |

## Ease of Integration

| Tool | Setup Complexity | IDE Integration | Automation Support | Learning Curve |
|------|-----------------|------------------|-------------------|----------------|
| **Spark** | Very Easy | N/A | Good | Low |
| **async-profiler** | Easy | Limited | Excellent | Medium |
| **JProfiler** | Easy | Excellent | Good | Medium |
| **JFR** | Easy | Good | Good | Medium |
| **VisualVM** | Easy | Limited | Limited | Low |
| **Eclipse MAT** | Easy | Excellent | Limited | High |
| **JConsole** | Very Easy | Limited | Limited | Low |
| **JMH** | Medium | Good | Excellent | High |

## Recommended Tool Combinations

### For Development Workflow
1. **Primary**: Spark + C2ME + Lithium + ScalableLux
2. **Memory Issues**: Eclipse MAT + JProfiler
3. **Benchmarking**: JMH + async-profiler
4. **Production Monitoring**: Spark + JFR

### For Chunk Performance Analysis
1. **Real-time**: Spark profiler
2. **Development**: C2ME with debug logging
3. **Benchmarking**: JMH with custom chunk loading tests
4. **Memory**: Eclipse MAT for heap analysis

### For Client-Side Performance
1. **Rendering**: Sodium + Spark
2. **General**: Lithium + Spark
3. **Memory**: VisualVM + Eclipse MAT

## Performance Impact Assessment

| Tool | CPU Overhead | Memory Overhead | Network Impact | Production Suitable |
|------|--------------|-----------------|----------------|-------------------|
| **Spark** | <1% | <50MB | Minimal | ✅ Yes |
| **async-profiler** | <0.5% | <20MB | None | ✅ Yes |
| **JProfiler** | 2-5% | 100-500MB | Low | ⚠️ Limited |
| **JFR** | <1% | <100MB | None | ✅ Yes |
| **VisualVM** | 5-10% | 200-1GB | Medium | ❌ No |
| **Eclipse MAT** | N/A | N/A | None | ✅ Offline only |
| **JConsole** | 1-2% | <50MB | Low | ⚠️ Limited |

## Key Findings Summary

### Best Overall Choice: Spark
- Designed specifically for Minecraft
- Minimal performance impact
- Comprehensive metrics for server/client
- Active development and community support
- Excellent for chunk loading analysis

### Best for Deep Analysis: async-profiler + JProfiler
- async-profiler for production profiling
- JProfiler for detailed development analysis
- Complementary strengths in different scenarios

### Best for Memory Issues: Eclipse MAT
- Industry standard for heap dump analysis
- Excellent leak detection capabilities
- Essential for memory optimization work

### Best for Benchmarking: JMH
- Gold standard for Java microbenchmarking
- Critical for comparing optimization approaches
- Required for rigorous performance testing