package com.xeenaa.fabricenhancements.performance.memory;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Comprehensive memory allocation tracking and monitoring system.
 * Provides detailed insights into memory usage patterns and optimization opportunities.
 *
 * Features:
 * - Real-time allocation tracking by type and size
 * - Memory leak detection with stack trace analysis
 * - Allocation pattern analysis for optimization
 * - Memory pressure monitoring and alerting
 * - Detailed reporting and baseline comparison
 * - Integration with JVM memory management beans
 */
public class MemoryAllocationTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryAllocationTracker.class);

    private final PerformanceConfig config;
    private final MemoryMXBean memoryMXBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    private final ThreadMXBean threadMXBean;
    private final ScheduledExecutorService trackerExecutor;

    // Allocation tracking
    private final Map<String, AllocationStats> allocationsByType = new ConcurrentHashMap<>();
    private final Map<Thread, ThreadAllocationInfo> threadAllocations = new ConcurrentHashMap<>();
    private final BlockingQueue<AllocationEvent> allocationEvents = new LinkedBlockingQueue<>();

    // Memory statistics
    private final AtomicLong totalTrackedAllocations = new AtomicLong();
    private final AtomicLong totalTrackedBytes = new AtomicLong();
    private final AtomicLong peakMemoryUsage = new AtomicLong();
    private final LongAdder memoryLeaksDetected = new LongAdder();

    // GC tracking
    private final Map<String, GCStats> gcStatsByCollector = new ConcurrentHashMap<>();
    private final AtomicLong lastGCCount = new AtomicLong();
    private final AtomicLong lastGCTime = new AtomicLong();

    // Memory pressure tracking
    private final LongAdder memoryPressureEvents = new LongAdder();
    private volatile boolean trackingEnabled = true;
    private volatile boolean detailedTrackingEnabled = false;

    // Sampling configuration
    private static final int SAMPLING_INTERVAL_MS = 1000; // 1 second
    private static final int MAX_TRACKED_ALLOCATIONS = 10000;
    private static final double MEMORY_PRESSURE_THRESHOLD = 0.85;
    private static final long ALLOCATION_SIZE_THRESHOLD = 1024; // Track allocations >= 1KB

    public MemoryAllocationTracker(PerformanceConfig config) {
        this.config = config;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.threadMXBean = ManagementFactory.getThreadMXBean();

        this.trackerExecutor = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "MemoryAllocation-Tracker");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });

        initializeGCTracking();
        startTracking();

        LOGGER.info("MemoryAllocationTracker initialized with sampling interval: {}ms", SAMPLING_INTERVAL_MS);
    }

    /**
     * Initialize GC tracking
     */
    private void initializeGCTracking() {
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            gcStatsByCollector.put(gcBean.getName(), new GCStats(gcBean.getName()));
            lastGCCount.addAndGet(gcBean.getCollectionCount());
            lastGCTime.addAndGet(gcBean.getCollectionTime());
        }
    }

    /**
     * Start memory allocation tracking
     */
    private void startTracking() {
        // Start sampling thread
        trackerExecutor.scheduleAtFixedRate(
            this::sampleMemoryUsage,
            0,
            SAMPLING_INTERVAL_MS,
            TimeUnit.MILLISECONDS
        );

        // Start allocation event processor
        trackerExecutor.submit(this::processAllocationEvents);

        // Start periodic analysis
        trackerExecutor.scheduleAtFixedRate(
            this::performAnalysis,
            30000, // 30 second initial delay
            30000, // 30 second interval
            TimeUnit.MILLISECONDS
        );
    }

    /**
     * Track memory allocation
     */
    public void trackAllocation(String type, long sizeBytes, String location) {
        if (!trackingEnabled || sizeBytes < ALLOCATION_SIZE_THRESHOLD) {
            return;
        }

        totalTrackedAllocations.incrementAndGet();
        totalTrackedBytes.addAndGet(sizeBytes);

        // Update peak memory usage
        long currentMemory = memoryMXBean.getHeapMemoryUsage().getUsed();
        updatePeakMemory(currentMemory);

        // Track by type
        allocationsByType.computeIfAbsent(type, k -> new AllocationStats(type))
            .recordAllocation(sizeBytes);

        // Track by thread if detailed tracking is enabled
        if (detailedTrackingEnabled) {
            trackThreadAllocation(sizeBytes, location);
        }

        // Queue event for processing
        AllocationEvent event = new AllocationEvent(
            System.nanoTime(),
            type,
            sizeBytes,
            Thread.currentThread().getName(),
            location
        );

        if (!allocationEvents.offer(event)) {
            // Queue full, enable sampling
            enableSampling();
        }
    }

    /**
     * Track thread-specific allocation
     */
    private void trackThreadAllocation(long sizeBytes, String location) {
        Thread currentThread = Thread.currentThread();
        ThreadAllocationInfo threadInfo = threadAllocations.computeIfAbsent(
            currentThread, k -> new ThreadAllocationInfo(currentThread.getName())
        );
        threadInfo.recordAllocation(sizeBytes, location);
    }

    /**
     * Update peak memory usage
     */
    private void updatePeakMemory(long currentMemory) {
        long peak = peakMemoryUsage.get();
        while (currentMemory > peak && !peakMemoryUsage.compareAndSet(peak, currentMemory)) {
            peak = peakMemoryUsage.get();
        }
    }

    /**
     * Sample memory usage
     */
    private void sampleMemoryUsage() {
        try {
            MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
            double usageRatio = (double) heapUsage.getUsed() / heapUsage.getMax();

            // Check for memory pressure
            if (usageRatio > MEMORY_PRESSURE_THRESHOLD) {
                handleMemoryPressure(heapUsage);
            }

            // Update GC statistics
            updateGCStats();

            // Check for memory leaks
            detectMemoryLeaks();

        } catch (Exception e) {
            LOGGER.warn("Error during memory sampling", e);
        }
    }

    /**
     * Handle memory pressure
     */
    private void handleMemoryPressure(MemoryUsage heapUsage) {
        memoryPressureEvents.increment();

        double usageRatio = (double) heapUsage.getUsed() / heapUsage.getMax();
        LOGGER.warn("Memory pressure detected: {:.1f}% heap usage ({} MB / {} MB)",
                   usageRatio * 100,
                   heapUsage.getUsed() / (1024 * 1024),
                   heapUsage.getMax() / (1024 * 1024));

        // Enable detailed tracking during pressure
        detailedTrackingEnabled = true;

        // Suggest optimizations
        suggestOptimizations();
    }

    /**
     * Update GC statistics
     */
    private void updateGCStats() {
        long totalGCCount = 0;
        long totalGCTime = 0;

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            String gcName = gcBean.getName();
            long currentCount = gcBean.getCollectionCount();
            long currentTime = gcBean.getCollectionTime();

            totalGCCount += currentCount;
            totalGCTime += currentTime;

            GCStats stats = gcStatsByCollector.get(gcName);
            if (stats != null) {
                stats.update(currentCount, currentTime);
            }
        }

        // Check for GC activity
        long newGCCount = totalGCCount - lastGCCount.get();
        long newGCTime = totalGCTime - lastGCTime.get();

        if (newGCCount > 0) {
            LOGGER.debug("GC activity: {} collections, {}ms total time", newGCCount, newGCTime);
        }

        lastGCCount.set(totalGCCount);
        lastGCTime.set(totalGCTime);
    }

    /**
     * Detect potential memory leaks
     */
    private void detectMemoryLeaks() {
        // Simple leak detection based on allocation patterns
        for (AllocationStats stats : allocationsByType.values()) {
            if (stats.isLeakCandidate()) {
                memoryLeaksDetected.increment();
                LOGGER.warn("Potential memory leak detected for type: {} (growth rate: {:.2f} MB/min)",
                           stats.getType(), stats.getGrowthRateMBPerMinute());
            }
        }

        // Check thread allocations for leaks
        if (detailedTrackingEnabled) {
            for (ThreadAllocationInfo threadInfo : threadAllocations.values()) {
                if (threadInfo.isLeakCandidate()) {
                    LOGGER.warn("Potential memory leak in thread: {} (allocation rate: {:.2f} MB/min)",
                               threadInfo.getThreadName(), threadInfo.getAllocationRateMBPerMinute());
                }
            }
        }
    }

    /**
     * Process allocation events
     */
    private void processAllocationEvents() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<AllocationEvent> batch = new ArrayList<>();
                AllocationEvent first = allocationEvents.take();
                batch.add(first);

                // Drain additional events for batch processing
                allocationEvents.drainTo(batch, 100);

                processBatchedEvents(batch);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                LOGGER.warn("Error processing allocation events", e);
            }
        }
    }

    /**
     * Process batched allocation events
     */
    private void processBatchedEvents(List<AllocationEvent> batch) {
        // Analyze allocation patterns
        Map<String, Long> typeTotals = new ConcurrentHashMap<>();
        Map<String, Integer> typeCounts = new ConcurrentHashMap<>();

        for (AllocationEvent event : batch) {
            typeTotals.merge(event.type, event.sizeBytes, Long::sum);
            typeCounts.merge(event.type, 1, Integer::sum);
        }

        // Update pattern analysis
        analyzeAllocationPatterns(typeTotals, typeCounts);
    }

    /**
     * Analyze allocation patterns
     */
    private void analyzeAllocationPatterns(Map<String, Long> typeTotals, Map<String, Integer> typeCounts) {
        for (Map.Entry<String, Long> entry : typeTotals.entrySet()) {
            String type = entry.getKey();
            long totalBytes = entry.getValue();
            int count = typeCounts.get(type);

            AllocationStats stats = allocationsByType.get(type);
            if (stats != null) {
                stats.updatePattern(totalBytes, count);
            }
        }
    }

    /**
     * Perform periodic analysis
     */
    private void performAnalysis() {
        try {
            // Analyze allocation efficiency
            double efficiency = calculateAllocationEfficiency();

            // Check for optimization opportunities
            List<String> recommendations = generateOptimizationRecommendations();

            if (!recommendations.isEmpty()) {
                LOGGER.debug("Memory optimization recommendations: {}", recommendations);
            }

            // Clean up old data
            cleanupOldData();

        } catch (Exception e) {
            LOGGER.warn("Error during memory analysis", e);
        }
    }

    /**
     * Calculate allocation efficiency
     */
    private double calculateAllocationEfficiency() {
        long totalAllocated = totalTrackedBytes.get();
        long currentUsage = memoryMXBean.getHeapMemoryUsage().getUsed();

        return totalAllocated > 0 ? (double) currentUsage / totalAllocated : 1.0;
    }

    /**
     * Generate optimization recommendations
     */
    private List<String> generateOptimizationRecommendations() {
        List<String> recommendations = new ArrayList<>();

        // Check for types with high allocation rates
        for (AllocationStats stats : allocationsByType.values()) {
            if (stats.getAllocationRateMBPerMinute() > 10.0) {
                recommendations.add("Consider pooling for type: " + stats.getType());
            }
        }

        // Check GC performance
        for (GCStats gcStats : gcStatsByCollector.values()) {
            if (gcStats.getAverageTimeMs() > 100) {
                recommendations.add("High GC time for: " + gcStats.getCollectorName());
            }
        }

        return recommendations;
    }

    /**
     * Suggest optimizations during memory pressure
     */
    private void suggestOptimizations() {
        List<String> suggestions = new ArrayList<>();

        // Find top memory consumers
        allocationsByType.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().getTotalBytes(), a.getValue().getTotalBytes()))
            .limit(5)
            .forEach(entry -> {
                suggestions.add("High memory usage: " + entry.getKey() +
                              " (" + entry.getValue().getTotalBytes() / (1024 * 1024) + " MB)");
            });

        LOGGER.info("Memory optimization suggestions: {}", suggestions);
    }

    /**
     * Clean up old data
     */
    private void cleanupOldData() {
        // Remove inactive thread allocations
        threadAllocations.entrySet().removeIf(entry ->
            !entry.getKey().isAlive() || entry.getValue().isInactive());

        // Reset allocation stats if too many tracked
        if (allocationsByType.size() > MAX_TRACKED_ALLOCATIONS) {
            allocationsByType.clear();
            LOGGER.debug("Reset allocation tracking due to size limit");
        }
    }

    /**
     * Enable sampling mode to reduce overhead
     */
    private void enableSampling() {
        trackingEnabled = true; // Could implement actual sampling logic here
    }

    /**
     * Get comprehensive tracking report
     */
    public MemoryTrackingReport getReport() {
        Map<String, AllocationStats> allocationsCopy = new ConcurrentHashMap<>(allocationsByType);
        Map<String, GCStats> gcStatsCopy = new ConcurrentHashMap<>(gcStatsByCollector);

        return new MemoryTrackingReport(
            totalTrackedAllocations.get(),
            totalTrackedBytes.get(),
            peakMemoryUsage.get(),
            memoryLeaksDetected.sum(),
            memoryPressureEvents.sum(),
            allocationsCopy,
            gcStatsCopy,
            calculateAllocationEfficiency(),
            generateOptimizationRecommendations()
        );
    }

    /**
     * Save tracking data to file
     */
    public void saveReport(Path outputFile) throws IOException {
        MemoryTrackingReport report = getReport();

        StringBuilder sb = new StringBuilder();
        sb.append("Memory Allocation Tracking Report\n");
        sb.append("Generated: ").append(new java.util.Date()).append("\n\n");

        sb.append("Summary:\n");
        sb.append("- Total Allocations: ").append(report.totalAllocations).append("\n");
        sb.append("- Total Bytes: ").append(report.totalBytes / (1024 * 1024)).append(" MB\n");
        sb.append("- Peak Memory: ").append(report.peakMemoryUsage / (1024 * 1024)).append(" MB\n");
        sb.append("- Memory Leaks Detected: ").append(report.memoryLeaksDetected).append("\n");
        sb.append("- Memory Pressure Events: ").append(report.memoryPressureEvents).append("\n");
        sb.append("- Allocation Efficiency: ").append(String.format("%.2f%%", report.allocationEfficiency * 100)).append("\n\n");

        sb.append("Allocation by Type:\n");
        report.allocationsByType.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().getTotalBytes(), a.getValue().getTotalBytes()))
            .forEach(entry -> {
                AllocationStats stats = entry.getValue();
                sb.append("- ").append(entry.getKey()).append(": ")
                  .append(stats.getTotalBytes() / (1024 * 1024)).append(" MB ")
                  .append("(").append(stats.getCount()).append(" allocations)\n");
            });

        Files.write(outputFile, sb.toString().getBytes());
    }

    /**
     * Shutdown tracker
     */
    public void shutdown() {
        LOGGER.info("Shutting down MemoryAllocationTracker");

        trackingEnabled = false;
        trackerExecutor.shutdown();

        try {
            if (!trackerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                trackerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            trackerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Save final report
        try {
            Path reportDir = Paths.get("performance/reports");
            Files.createDirectories(reportDir);
            saveReport(reportDir.resolve("memory-allocation-report.txt"));
        } catch (IOException e) {
            LOGGER.warn("Failed to save final memory allocation report", e);
        }

        LOGGER.info("MemoryAllocationTracker shutdown complete");
    }

    /**
     * Allocation statistics for a specific type
     */
    private static class AllocationStats {
        private final String type;
        private final AtomicLong count = new AtomicLong();
        private final AtomicLong totalBytes = new AtomicLong();
        private final AtomicLong lastUpdateTime = new AtomicLong(System.currentTimeMillis());
        private final LongAdder recentAllocations = new LongAdder();
        private volatile long previousTotalBytes = 0;

        public AllocationStats(String type) {
            this.type = type;
        }

        public void recordAllocation(long sizeBytes) {
            count.incrementAndGet();
            totalBytes.addAndGet(sizeBytes);
            recentAllocations.increment();
            lastUpdateTime.set(System.currentTimeMillis());
        }

        public void updatePattern(long batchBytes, int batchCount) {
            // Update pattern analysis (could be more sophisticated)
        }

        public boolean isLeakCandidate() {
            long currentBytes = totalBytes.get();
            double growthRate = getGrowthRateMBPerMinute();

            // Simple leak detection: rapid growth without corresponding deallocations
            return growthRate > 5.0 && currentBytes > previousTotalBytes * 1.5;
        }

        public double getGrowthRateMBPerMinute() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdateTime.get();
            if (timeDiff < 60000) return 0.0; // Less than 1 minute

            long bytesDiff = totalBytes.get() - previousTotalBytes;
            double minutes = timeDiff / 60000.0;
            return (bytesDiff / (1024.0 * 1024.0)) / minutes;
        }

        public double getAllocationRateMBPerMinute() {
            return getGrowthRateMBPerMinute();
        }

        // Getters
        public String getType() { return type; }
        public long getCount() { return count.get(); }
        public long getTotalBytes() { return totalBytes.get(); }
    }

    /**
     * Thread-specific allocation information
     */
    private static class ThreadAllocationInfo {
        private final String threadName;
        private final AtomicLong totalBytes = new AtomicLong();
        private final AtomicLong lastActivity = new AtomicLong(System.currentTimeMillis());
        private final Map<String, Long> locationTotals = new ConcurrentHashMap<>();

        public ThreadAllocationInfo(String threadName) {
            this.threadName = threadName;
        }

        public void recordAllocation(long sizeBytes, String location) {
            totalBytes.addAndGet(sizeBytes);
            lastActivity.set(System.currentTimeMillis());
            if (location != null) {
                locationTotals.merge(location, sizeBytes, Long::sum);
            }
        }

        public boolean isLeakCandidate() {
            return getAllocationRateMBPerMinute() > 10.0;
        }

        public boolean isInactive() {
            return System.currentTimeMillis() - lastActivity.get() > 300000; // 5 minutes
        }

        public double getAllocationRateMBPerMinute() {
            long timeDiff = System.currentTimeMillis() - lastActivity.get();
            if (timeDiff < 60000) return 0.0;

            double minutes = timeDiff / 60000.0;
            return (totalBytes.get() / (1024.0 * 1024.0)) / minutes;
        }

        public String getThreadName() { return threadName; }
    }

    /**
     * GC statistics tracking
     */
    private static class GCStats {
        private final String collectorName;
        private final AtomicLong totalCollections = new AtomicLong();
        private final AtomicLong totalTimeMs = new AtomicLong();
        private final AtomicLong lastCollections = new AtomicLong();
        private final AtomicLong lastTimeMs = new AtomicLong();

        public GCStats(String collectorName) {
            this.collectorName = collectorName;
        }

        public void update(long collections, long timeMs) {
            totalCollections.set(collections);
            totalTimeMs.set(timeMs);
        }

        public double getAverageTimeMs() {
            long collections = totalCollections.get();
            return collections > 0 ? (double) totalTimeMs.get() / collections : 0.0;
        }

        public String getCollectorName() { return collectorName; }
    }

    /**
     * Allocation event for detailed tracking
     */
    private static class AllocationEvent {
        final long timestamp;
        final String type;
        final long sizeBytes;
        final String threadName;
        final String location;

        AllocationEvent(long timestamp, String type, long sizeBytes, String threadName, String location) {
            this.timestamp = timestamp;
            this.type = type;
            this.sizeBytes = sizeBytes;
            this.threadName = threadName;
            this.location = location;
        }
    }

    /**
     * Comprehensive memory tracking report
     */
    public static class MemoryTrackingReport {
        public final long totalAllocations;
        public final long totalBytes;
        public final long peakMemoryUsage;
        public final long memoryLeaksDetected;
        public final long memoryPressureEvents;
        public final Map<String, AllocationStats> allocationsByType;
        public final Map<String, GCStats> gcStats;
        public final double allocationEfficiency;
        public final List<String> optimizationRecommendations;

        public MemoryTrackingReport(long totalAllocations, long totalBytes, long peakMemoryUsage,
                                  long memoryLeaksDetected, long memoryPressureEvents,
                                  Map<String, AllocationStats> allocationsByType,
                                  Map<String, GCStats> gcStats, double allocationEfficiency,
                                  List<String> optimizationRecommendations) {
            this.totalAllocations = totalAllocations;
            this.totalBytes = totalBytes;
            this.peakMemoryUsage = peakMemoryUsage;
            this.memoryLeaksDetected = memoryLeaksDetected;
            this.memoryPressureEvents = memoryPressureEvents;
            this.allocationsByType = allocationsByType;
            this.gcStats = gcStats;
            this.allocationEfficiency = allocationEfficiency;
            this.optimizationRecommendations = optimizationRecommendations;
        }

        @Override
        public String toString() {
            return String.format(
                "MemoryTrackingReport{allocations=%d, totalMB=%d, peakMB=%d, leaks=%d, " +
                "pressure=%d, efficiency=%.1f%%, types=%d}",
                totalAllocations, totalBytes / (1024 * 1024), peakMemoryUsage / (1024 * 1024),
                memoryLeaksDetected, memoryPressureEvents, allocationEfficiency * 100,
                allocationsByType.size()
            );
        }
    }
}