# Xeenaa Fabric Enhancements - Operational Runbook

## Overview

This operational runbook provides comprehensive procedures for deploying, operating, monitoring, and troubleshooting the Xeenaa Fabric Enhancements performance optimization system.

## Table of Contents

1. [Deployment Procedures](#deployment-procedures)
2. [Monitoring and Alerting](#monitoring-and-alerting)
3. [Performance Management](#performance-management)
4. [Troubleshooting Guide](#troubleshooting-guide)
5. [Maintenance Procedures](#maintenance-procedures)
6. [Emergency Procedures](#emergency-procedures)
7. [Configuration Management](#configuration-management)
8. [Performance Tuning](#performance-tuning)

## Deployment Procedures

### Pre-Deployment Checklist

#### System Requirements Verification
- [ ] Java 21 or higher installed
- [ ] Minecraft 1.21.1 server/client
- [ ] Fabric Loader 0.16.5 or higher
- [ ] Fabric API 0.103.0 or higher
- [ ] Minimum 2GB heap memory (4GB recommended)
- [ ] CPU with 4+ cores recommended

#### Performance Baseline Collection
```bash
# Collect baseline performance data before deployment
./gradlew collectBaseline

# Run hardware tier detection
./gradlew detectHardwareTier

# Validate system compatibility
./gradlew validateEnvironment
```

#### Configuration Preparation
```bash
# Generate default configuration
cp config/xeenaa-fabric-enhancements.json.template config/xeenaa-fabric-enhancements.json

# Validate configuration
./gradlew validateConfig

# Test configuration load
./gradlew testConfig
```

### Deployment Steps

#### 1. Backup Current System
```bash
# Backup existing mods directory
cp -r mods mods.backup.$(date +%Y%m%d_%H%M%S)

# Backup existing configuration
cp -r config config.backup.$(date +%Y%m%d_%H%M%S)

# Backup world data (recommended)
cp -r saves saves.backup.$(date +%Y%m%d_%H%M%S)
```

#### 2. Deploy Mod Files
```bash
# Copy mod JAR to mods directory
cp xeenaa-fabric-enhancements-1.0.0.jar mods/

# Verify JAR integrity
sha256sum mods/xeenaa-fabric-enhancements-1.0.0.jar

# Check for dependency conflicts
./gradlew checkDependencies
```

#### 3. Configuration Setup
```bash
# Copy production configuration
cp config/environments/production.json config/xeenaa-fabric-enhancements.json

# Validate configuration syntax
./gradlew validateConfig

# Test configuration loading
java -cp mods/xeenaa-fabric-enhancements-1.0.0.jar \
  com.xeenaa.fabricenhancements.config.PerformanceConfig --validate
```

#### 4. System Startup
```bash
# Start server with enhanced monitoring
java -Xmx4g -Xms2g \
  -XX:+UseG1GC \
  -XX:+FlightRecorder \
  -XX:StartFlightRecording=duration=300s,filename=startup.jfr \
  -jar fabric-server-mc.1.21.1-loader.0.16.5-launcher.1.0.1.jar nogui

# Monitor startup logs
tail -f logs/latest.log | grep -E "(XeenaaFabricEnhancements|Performance|ERROR|WARN)"
```

#### 5. Post-Deployment Validation
```bash
# Verify mod loaded successfully
grep "XeenaaFabricEnhancements.*initialized" logs/latest.log

# Check performance monitoring active
curl -f http://localhost:9090/metrics | grep xeenaa_performance

# Validate performance improvements
./gradlew validateDeployment
```

### Rollback Procedures

#### Quick Rollback
```bash
# Stop server
kill -TERM $(cat minecraft.pid)

# Remove mod
rm mods/xeenaa-fabric-enhancements-1.0.0.jar

# Restore backup configuration
rm -rf config
mv config.backup.$(date +%Y%m%d_%H%M%S) config

# Restart server
./start.sh
```

#### Full System Restore
```bash
# Stop all services
systemctl stop minecraft-server

# Restore full backup
rm -rf mods config saves
mv mods.backup.$(date +%Y%m%d_%H%M%S) mods
mv config.backup.$(date +%Y%m%d_%H%M%S) config
mv saves.backup.$(date +%Y%m%d_%H%M%S) saves

# Restart services
systemctl start minecraft-server
```

## Monitoring and Alerting

### Key Performance Metrics

#### Primary Metrics
- **Chunk Load Time**: Target <50ms average
- **Memory Usage**: Monitor for >4GB sustained usage
- **TPS**: Alert if <19.5 for >30 seconds
- **Cache Hit Ratio**: Alert if <80%

#### Secondary Metrics
- **Thread Pool Utilization**: Monitor >90% sustained
- **GC Pressure**: Alert on >10% time in GC
- **Error Rate**: Alert on >1% optimization failures
- **Resource Leaks**: Monitor file handles, memory trends

### Monitoring Setup

#### Prometheus Configuration
```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'minecraft-performance'
    static_configs:
      - targets: ['localhost:9090']
    scrape_interval: 5s
    metrics_path: /metrics
```

#### Grafana Dashboard
```json
{
  "dashboard": {
    "title": "Xeenaa Performance Monitoring",
    "panels": [
      {
        "title": "Chunk Load Performance",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(xeenaa_chunk_load_duration_seconds_sum[5m]) / rate(xeenaa_chunk_load_duration_seconds_count[5m])",
            "legendFormat": "Average Load Time"
          }
        ]
      },
      {
        "title": "Memory Usage",
        "type": "graph",
        "targets": [
          {
            "expr": "xeenaa_memory_usage_bytes",
            "legendFormat": "Memory Usage"
          }
        ]
      }
    ]
  }
}
```

### Alert Configuration

#### Critical Alerts
```yaml
# alerts.yml
groups:
  - name: xeenaa_performance
    rules:
      - alert: ChunkLoadTimeHigh
        expr: rate(xeenaa_chunk_load_duration_seconds_sum[5m]) / rate(xeenaa_chunk_load_duration_seconds_count[5m]) > 0.05
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "Chunk load time exceeding 50ms threshold"

      - alert: TPSLow
        expr: minecraft_tps < 19.5
        for: 30s
        labels:
          severity: critical
        annotations:
          summary: "Server TPS below 19.5"

      - alert: MemoryLeakDetected
        expr: increase(xeenaa_memory_usage_bytes[10m]) > 104857600  # 100MB
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Potential memory leak detected"
```

#### Warning Alerts
```yaml
      - alert: CacheHitRatioLow
        expr: rate(xeenaa_cache_hits_total[5m]) / (rate(xeenaa_cache_hits_total[5m]) + rate(xeenaa_cache_misses_total[5m])) < 0.8
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Cache hit ratio below 80%"

      - alert: ThreadPoolUtilizationHigh
        expr: xeenaa_thread_pool_active_threads / xeenaa_thread_pool_max_threads > 0.9
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "Thread pool utilization above 90%"
```

### Log Monitoring

#### Key Log Patterns
```bash
# Performance degradation indicators
grep -E "(Performance.*degraded|Optimization.*failed|Fallback.*activated)" logs/latest.log

# Memory pressure indicators
grep -E "(OutOfMemoryError|GC.*pressure|Memory.*exhausted)" logs/latest.log

# Configuration issues
grep -E "(Configuration.*invalid|Config.*error|Setting.*ignored)" logs/latest.log
```

#### Log Analysis Commands
```bash
# Analyze performance trends
awk '/XeenaaPerformance/ {print $1, $2, $NF}' logs/latest.log | sort

# Count optimization failures
grep -c "Optimization failed" logs/latest.log

# Extract performance metrics
grep "Performance metric" logs/latest.log | sed 's/.*metric: //' | sort -n
```

## Performance Management

### Performance Baselines

#### Baseline Collection
```bash
# Collect comprehensive baseline
./gradlew performanceAnalysisPipeline

# Generate baseline report
./gradlew generateBaselineReport

# Store baseline for comparison
cp performance/baselines/latest-baseline.json performance/baselines/baseline-$(date +%Y%m%d).json
```

#### Baseline Comparison
```bash
# Compare current performance to baseline
./gradlew compareToBaseline

# Generate trend analysis
./gradlew analyzeTrends --days=30

# Detect performance regressions
./gradlew detectRegressions --threshold=5
```

### Performance Optimization

#### Automatic Optimization
```bash
# Enable automatic performance tuning
echo "enableAutoOptimization=true" >> config/xeenaa-fabric-enhancements.json

# Configure optimization aggressiveness
echo "optimizationLevel=BALANCED" >> config/xeenaa-fabric-enhancements.json

# Set optimization targets
echo "targetChunkLoadTime=45" >> config/xeenaa-fabric-enhancements.json
```

#### Manual Optimization
```bash
# Analyze current performance bottlenecks
./gradlew analyzeBottlenecks

# Generate optimization recommendations
./gradlew generateOptimizationPlan

# Apply recommended optimizations
./gradlew applyOptimizations --plan=latest
```

### Capacity Planning

#### Resource Utilization Analysis
```bash
# Generate resource utilization report
./gradlew generateResourceReport

# Analyze memory usage patterns
./gradlew analyzeMemoryPatterns --duration=24h

# Project resource requirements
./gradlew projectResourceNeeds --players=100
```

## Troubleshooting Guide

### Common Issues and Solutions

#### Issue: High Chunk Load Times
**Symptoms:**
- Chunk load times >100ms consistently
- Player complaints about world loading lag
- High disk I/O during chunk loading

**Diagnosis:**
```bash
# Check chunk load performance
grep "Chunk load time" logs/latest.log | tail -20

# Analyze thread pool utilization
curl -s http://localhost:9090/metrics | grep thread_pool

# Check disk I/O
iostat -x 1 5 | grep -E "(Device|sda)"
```

**Solutions:**
1. **Increase Thread Pool Size:**
   ```json
   {
     "chunkLoadingThreads": 8,
     "maxConcurrentChunkLoads": 32
   }
   ```

2. **Optimize Disk Performance:**
   ```bash
   # Check disk performance
   dd if=/dev/zero of=/tmp/testfile bs=1G count=1 oflag=direct

   # Consider SSD upgrade if write speed <100MB/s
   ```

3. **Increase Memory Allocation:**
   ```bash
   # Increase heap size
   java -Xmx8g -Xms4g [other_options]
   ```

#### Issue: Memory Leaks
**Symptoms:**
- Continuously increasing memory usage
- Frequent garbage collection
- OutOfMemoryError exceptions

**Diagnosis:**
```bash
# Monitor memory trends
grep -E "(Memory usage|GC time)" logs/latest.log | tail -50

# Generate heap dump
jcmd <minecraft_pid> GC.run_finalization
jcmd <minecraft_pid> VM.gc
jcmd <minecraft_pid> GC.dump /tmp/heap_dump.hprof

# Analyze memory allocation
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=memory_analysis.jfr
```

**Solutions:**
1. **Enable Memory Leak Detection:**
   ```json
   {
     "enableMemoryLeakDetection": true,
     "memoryLeakThreshold": 0.1
   }
   ```

2. **Increase GC Efficiency:**
   ```bash
   # Use G1GC with optimized settings
   -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=16m
   ```

3. **Force Cleanup:**
   ```bash
   # Trigger manual cleanup via API
   curl -X POST http://localhost:9090/admin/cleanup
   ```

#### Issue: Low TPS Performance
**Symptoms:**
- Server TPS consistently below 19.5
- Player lag and rubber-banding
- Delayed block updates

**Diagnosis:**
```bash
# Monitor TPS trends
grep "TPS:" logs/latest.log | tail -20

# Check CPU usage
top -p <minecraft_pid>

# Analyze tick performance
curl -s http://localhost:9090/metrics | grep tps
```

**Solutions:**
1. **Optimize Tick Performance:**
   ```json
   {
     "enableTickOptimization": true,
     "tickOptimizationLevel": "AGGRESSIVE"
   }
   ```

2. **Reduce Concurrent Operations:**
   ```json
   {
     "maxConcurrentChunkLoads": 8,
     "chunkLoadingThreads": 4
   }
   ```

3. **Profile Performance:**
   ```bash
   # Start Spark profiling session
   /spark profiler start --thread * --timeout 300
   ```

### Diagnostic Commands

#### Performance Analysis
```bash
# Generate comprehensive performance report
./gradlew generateDiagnosticReport

# Analyze recent performance trends
./gradlew analyzeTrends --hours=24

# Check system health
./gradlew healthCheck
```

#### Configuration Validation
```bash
# Validate current configuration
./gradlew validateCurrentConfig

# Check for configuration conflicts
./gradlew checkConfigConflicts

# Generate optimal configuration
./gradlew generateOptimalConfig
```

#### System Information
```bash
# Collect system information
./gradlew collectSystemInfo

# Generate environment report
./gradlew generateEnvironmentReport

# Check mod compatibility
./gradlew checkModCompatibility
```

## Maintenance Procedures

### Daily Maintenance

#### Health Checks
```bash
#!/bin/bash
# daily_health_check.sh

echo "$(date): Starting daily health check"

# Check server status
if ! pgrep -f "fabric-server" > /dev/null; then
    echo "ERROR: Minecraft server not running"
    exit 1
fi

# Check performance metrics
if ! curl -sf http://localhost:9090/metrics > /dev/null; then
    echo "WARNING: Metrics endpoint not responding"
fi

# Check log errors
error_count=$(grep -c "ERROR" logs/latest.log)
if [ $error_count -gt 10 ]; then
    echo "WARNING: High error count: $error_count"
fi

# Check memory usage
memory_usage=$(curl -s http://localhost:9090/metrics | grep "xeenaa_memory_usage" | awk '{print $2}')
if [ $(echo "$memory_usage > 4294967296" | bc) -eq 1 ]; then
    echo "WARNING: High memory usage: $(echo "scale=2; $memory_usage/1024/1024/1024" | bc)GB"
fi

echo "$(date): Health check completed"
```

#### Performance Monitoring
```bash
#!/bin/bash
# daily_performance_check.sh

# Collect performance metrics
./gradlew collectDailyMetrics

# Compare to baseline
./gradlew compareToBaseline --days=1

# Generate daily performance report
./gradlew generateDailyReport

# Archive old logs
find logs/ -name "*.log.*" -mtime +7 -delete
```

### Weekly Maintenance

#### Performance Analysis
```bash
#!/bin/bash
# weekly_maintenance.sh

echo "$(date): Starting weekly maintenance"

# Generate weekly performance report
./gradlew generateWeeklyReport

# Update performance baselines
./gradlew updateBaselines --weeks=1

# Analyze performance trends
./gradlew analyzeTrends --days=7

# Check for performance regressions
./gradlew detectRegressions --period=weekly

# Optimize configuration based on trends
./gradlew optimizeConfiguration --auto-apply=false

echo "$(date): Weekly maintenance completed"
```

#### Configuration Review
```bash
# Review configuration effectiveness
./gradlew reviewConfiguration --period=weekly

# Generate configuration recommendations
./gradlew generateConfigRecommendations

# Test alternative configurations
./gradlew testConfigVariations --top=3
```

### Monthly Maintenance

#### Comprehensive Analysis
```bash
#!/bin/bash
# monthly_maintenance.sh

echo "$(date): Starting monthly maintenance"

# Generate comprehensive performance analysis
./gradlew generateMonthlyAnalysis

# Update long-term baselines
./gradlew updateLongTermBaselines

# Review and update optimization strategies
./gradlew reviewOptimizationStrategies

# Check for software updates
./gradlew checkUpdates

# Plan capacity adjustments
./gradlew planCapacity --projection=3months

echo "$(date): Monthly maintenance completed"
```

## Emergency Procedures

### Performance Emergency Response

#### Emergency: Server Unresponsive
```bash
#!/bin/bash
# emergency_response.sh

echo "EMERGENCY: Server unresponsive at $(date)"

# 1. Immediate diagnosis
echo "Checking server status..."
ps aux | grep minecraft

echo "Checking memory usage..."
free -h

echo "Checking disk space..."
df -h

# 2. Attempt graceful recovery
echo "Attempting graceful shutdown..."
kill -TERM $(pgrep -f minecraft)
sleep 30

# 3. Force shutdown if necessary
if pgrep -f minecraft > /dev/null; then
    echo "Force killing server..."
    kill -9 $(pgrep -f minecraft)
fi

# 4. Disable optimizations for safe restart
cp config/xeenaa-fabric-enhancements.json config/xeenaa-fabric-enhancements.json.backup
jq '.enableChunkOptimizations = false | .enableMemoryOptimizations = false' \
    config/xeenaa-fabric-enhancements.json.backup > config/xeenaa-fabric-enhancements.json

# 5. Restart with minimal optimizations
echo "Restarting server with minimal optimizations..."
./start.sh --safe-mode

echo "Emergency response completed at $(date)"
```

#### Emergency: Memory Exhaustion
```bash
#!/bin/bash
# memory_emergency.sh

echo "MEMORY EMERGENCY: $(date)"

# Force garbage collection
jcmd $(pgrep -f minecraft) GC.run_finalization
jcmd $(pgrep -f minecraft) VM.gc

# Reduce memory pressure
curl -X POST http://localhost:9090/admin/emergency-cleanup

# Disable memory-intensive features
jq '.enableMemoryOptimizations = false | .chunkCacheSize = 100' \
    config/xeenaa-fabric-enhancements.json > config/temp.json
mv config/temp.json config/xeenaa-fabric-enhancements.json

# Restart configuration
curl -X POST http://localhost:9090/admin/reload-config

echo "Memory emergency procedures completed"
```

### Escalation Procedures

#### Level 1: Automatic Recovery
- Automatic fallback to vanilla chunk loading
- Disable non-critical optimizations
- Alert monitoring systems

#### Level 2: Manual Intervention
- Disable specific optimization modules
- Adjust configuration parameters
- Collect diagnostic information

#### Level 3: Emergency Shutdown
- Safe server shutdown
- Disable all optimizations
- Restart in safe mode
- Contact development team

### Recovery Procedures

#### Configuration Recovery
```bash
# Restore last known good configuration
cp config/backups/last-good-config.json config/xeenaa-fabric-enhancements.json

# Validate restored configuration
./gradlew validateConfig

# Test configuration in safe mode
./gradlew testConfig --safe-mode
```

#### Performance Recovery
```bash
# Reset performance optimizations
./gradlew resetOptimizations

# Restore baseline configuration
./gradlew restoreBaselineConfig

# Gradually re-enable optimizations
./gradlew enableOptimizations --gradual
```

## Configuration Management

### Configuration Files

#### Main Configuration: `config/xeenaa-fabric-enhancements.json`
```json
{
  "enableChunkOptimizations": true,
  "enableMemoryOptimizations": true,
  "enablePerformanceMonitoring": true,
  "chunkLoadingThreads": 4,
  "maxConcurrentChunkLoads": 16,
  "targetTPS": 20.0,
  "memoryPressureThreshold": 0.8,
  "cacheSize": 1000,
  "optimizationLevel": "BALANCED"
}
```

#### Environment-Specific Configurations

**Development:** `config/environments/development.json`
```json
{
  "enableDebugLogging": true,
  "enablePerformanceProfiling": true,
  "optimizationLevel": "CONSERVATIVE",
  "chunkLoadingThreads": 2
}
```

**Production:** `config/environments/production.json`
```json
{
  "enableDebugLogging": false,
  "enablePerformanceProfiling": false,
  "optimizationLevel": "AGGRESSIVE",
  "chunkLoadingThreads": 8,
  "enableAutoOptimization": true
}
```

### Configuration Management Commands

#### Configuration Validation
```bash
# Validate syntax
./gradlew validateConfigSyntax

# Validate semantics
./gradlew validateConfigSemantics

# Check environment compatibility
./gradlew validateConfigEnvironment --env=production
```

#### Configuration Deployment
```bash
# Deploy configuration safely
./gradlew deployConfig --validate --backup

# Roll back configuration
./gradlew rollbackConfig --to-version=previous

# Test configuration changes
./gradlew testConfigChanges --dry-run
```

#### Configuration Optimization
```bash
# Generate optimal configuration
./gradlew generateOptimalConfig --workload=current

# Apply performance-based recommendations
./gradlew applyConfigRecommendations --source=performance-analysis

# A/B test configurations
./gradlew abTestConfig --config-a=current --config-b=optimized
```

## Performance Tuning

### Tuning Guidelines

#### Memory Tuning
```bash
# Calculate optimal heap size
./gradlew calculateOptimalHeapSize

# Tune GC settings
./gradlew tuneGCSettings --workload=current

# Optimize memory pools
./gradlew optimizeMemoryPools --target=latency
```

#### CPU Tuning
```bash
# Optimize thread pool sizes
./gradlew optimizeThreadPools --cpu-cores=$(nproc)

# Tune concurrency levels
./gradlew tuneConcurrency --target=throughput

# Optimize CPU affinity
./gradlew setCPUAffinity --cores=0-7
```

#### I/O Tuning
```bash
# Optimize disk I/O
./gradlew optimizeDiskIO --storage-type=ssd

# Tune network settings
./gradlew tuneNetworkSettings --connection-type=gigabit

# Optimize chunk I/O patterns
./gradlew optimizeChunkIO --pattern=sequential
```

### Performance Testing

#### Load Testing
```bash
# Run load test with simulated players
./gradlew loadTest --players=100 --duration=30m

# Test specific scenarios
./gradlew scenarioTest --scenario=chunk-loading --intensity=high

# Stress test system limits
./gradlew stressTest --until-failure
```

#### Benchmark Comparison
```bash
# Compare against baseline
./gradlew benchmarkComparison --baseline=production

# Compare configurations
./gradlew compareConfigs --config-a=current --config-b=optimized

# Regression testing
./gradlew regressionTest --commits=10
```

---

**Document Version:** 1.0
**Last Updated:** 2025-01-XX
**Maintained By:** Xeenaa Fabric Enhancements Team
**Emergency Contact:** support@xeenaa.dev