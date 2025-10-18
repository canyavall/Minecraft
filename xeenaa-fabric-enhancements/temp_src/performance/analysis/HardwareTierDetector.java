package com.xeenaa.fabricenhancements.performance.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

/**
 * Hardware tier detection system for automatic hardware performance categorization.
 * Provides comprehensive hardware profiling to categorize systems into performance tiers
 * for baseline establishment and performance target adjustment.
 */
public class HardwareTierDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareTierDetector.class);

    // Performance benchmarks for tier classification
    private static final long CPU_BENCHMARK_DURATION_MS = 5000; // 5 seconds
    private static final int MEMORY_BENCHMARK_ITERATIONS = 1000;
    private static final int IO_BENCHMARK_ITERATIONS = 100;

    // Hardware tier thresholds
    private static final long HIGH_END_CPU_SCORE = 8000;
    private static final long MID_RANGE_CPU_SCORE = 4000;
    private static final long HIGH_END_MEMORY_MB = 16384; // 16GB
    private static final long MID_RANGE_MEMORY_MB = 8192;  // 8GB
    private static final double HIGH_END_IO_SCORE = 800.0; // MB/s
    private static final double MID_RANGE_IO_SCORE = 400.0; // MB/s

    private final MemoryMXBean memoryMXBean;
    private final OperatingSystemMXBean osMXBean;

    public HardwareTierDetector() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.osMXBean = ManagementFactory.getOperatingSystemMXBean();
    }

    /**
     * Detect and classify hardware tier based on comprehensive benchmarking
     */
    public HardwareTier detectHardwareTier() {
        LOGGER.info("Starting comprehensive hardware tier detection...");

        try {
            HardwareProfile profile = new HardwareProfile();

            // Gather system information
            gatherSystemInfo(profile);

            // Perform CPU benchmarking
            benchmarkCPU(profile);

            // Perform memory benchmarking
            benchmarkMemory(profile);

            // Perform I/O benchmarking
            benchmarkIO(profile);

            // Calculate overall hardware tier
            HardwareTier tier = calculateHardwareTier(profile);

            LOGGER.info("Hardware tier detection completed: {}", tier.name);
            LOGGER.info("CPU Score: {}, Memory: {}MB, I/O Score: {:.1f}MB/s",
                       profile.cpuScore, profile.totalMemoryMB, profile.ioScore);

            return tier;

        } catch (Exception e) {
            LOGGER.error("Error during hardware tier detection", e);
            // Return conservative estimate on error
            return createFallbackTier();
        }
    }

    /**
     * Gather comprehensive system information
     */
    private void gatherSystemInfo(HardwareProfile profile) {
        LOGGER.debug("Gathering system information...");

        // Basic system properties
        profile.osName = System.getProperty("os.name");
        profile.osVersion = System.getProperty("os.version");
        profile.osArch = System.getProperty("os.arch");
        profile.javaVersion = System.getProperty("java.version");
        profile.jvmName = System.getProperty("java.vm.name");

        // CPU information
        profile.availableProcessors = Runtime.getRuntime().availableProcessors();

        // Memory information
        profile.totalMemoryMB = memoryMXBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
        profile.maxHeapMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);

        // Try to get system memory (requires platform-specific MXBean)
        try {
            if (osMXBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunMXBean =
                    (com.sun.management.OperatingSystemMXBean) osMXBean;
                profile.systemMemoryMB = sunMXBean.getTotalPhysicalMemorySize() / (1024 * 1024);
            }
        } catch (Exception e) {
            LOGGER.debug("Could not access system memory information: {}", e.getMessage());
            profile.systemMemoryMB = profile.maxHeapMemoryMB * 4; // Estimate
        }

        // Disk space information
        File currentDir = new File(".");
        profile.totalDiskSpaceGB = currentDir.getTotalSpace() / (1024 * 1024 * 1024);
        profile.freeDiskSpaceGB = currentDir.getFreeSpace() / (1024 * 1024 * 1024);

        LOGGER.debug("System info gathered: {} cores, {}MB heap, {}MB system memory",
                    profile.availableProcessors, profile.maxHeapMemoryMB, profile.systemMemoryMB);
    }

    /**
     * Benchmark CPU performance using computational workloads
     */
    private void benchmarkCPU(HardwareProfile profile) {
        LOGGER.debug("Starting CPU benchmark...");

        long startTime = System.currentTimeMillis();
        AtomicLong operations = new AtomicLong(0);

        // Multi-threaded CPU benchmark
        ExecutorService executor = Executors.newFixedThreadPool(profile.availableProcessors);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < profile.availableProcessors; i++) {
            Future<?> future = executor.submit(() -> {
                long threadOperations = 0;
                long endTime = startTime + CPU_BENCHMARK_DURATION_MS;

                while (System.currentTimeMillis() < endTime) {
                    // Perform CPU-intensive calculations
                    double result = 0;
                    for (int j = 0; j < 1000; j++) {
                        result += Math.sqrt(Math.sin(j) * Math.cos(j)) * Math.log(j + 1);
                    }

                    // Prevent optimization
                    if (result < 0) {
                        System.currentTimeMillis();
                    }

                    threadOperations++;
                }

                operations.addAndGet(threadOperations);
            });
            futures.add(future);
        }

        // Wait for all threads to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                LOGGER.warn("CPU benchmark thread failed", e);
            }
        }

        executor.shutdown();

        long totalTime = System.currentTimeMillis() - startTime;
        profile.cpuScore = operations.get() * 1000 / totalTime; // Operations per second
        profile.cpuBenchmarkDurationMs = totalTime;

        LOGGER.debug("CPU benchmark completed: {} ops/sec in {}ms", profile.cpuScore, totalTime);
    }

    /**
     * Benchmark memory performance using allocation and access patterns
     */
    private void benchmarkMemory(HardwareProfile profile) {
        LOGGER.debug("Starting memory benchmark...");

        long startTime = System.nanoTime();

        // Memory allocation benchmark
        List<byte[]> allocations = new ArrayList<>();
        long allocationStartTime = System.nanoTime();

        for (int i = 0; i < MEMORY_BENCHMARK_ITERATIONS; i++) {
            byte[] data = new byte[1024 * 1024]; // 1MB allocations
            // Fill with data to ensure actual allocation
            for (int j = 0; j < data.length; j += 4096) {
                data[j] = (byte) i;
            }
            allocations.add(data);
        }

        long allocationTime = System.nanoTime() - allocationStartTime;
        profile.memoryAllocationScore = (double) MEMORY_BENCHMARK_ITERATIONS / (allocationTime / 1_000_000_000.0);

        // Memory access benchmark
        long accessStartTime = System.nanoTime();
        long checksum = 0;

        for (byte[] allocation : allocations) {
            for (int i = 0; i < allocation.length; i += 1024) {
                checksum += allocation[i];
            }
        }

        long accessTime = System.nanoTime() - accessStartTime;
        long totalBytesAccessed = (long) allocations.size() * allocations.get(0).length / 1024;
        profile.memoryAccessScore = (double) totalBytesAccessed / (accessTime / 1_000_000_000.0);

        // Cleanup
        allocations.clear();
        System.gc();

        long totalTime = (System.nanoTime() - startTime) / 1_000_000;
        profile.memoryBenchmarkDurationMs = totalTime;

        LOGGER.debug("Memory benchmark completed: alloc={:.1f} ops/sec, access={:.1f} KB/sec in {}ms",
                    profile.memoryAllocationScore, profile.memoryAccessScore, totalTime);
    }

    /**
     * Benchmark I/O performance using file operations
     */
    private void benchmarkIO(HardwareProfile profile) {
        LOGGER.debug("Starting I/O benchmark...");

        long startTime = System.nanoTime();

        try {
            // Create temporary directory for testing
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "xeenaa-io-benchmark");
            tempDir.mkdirs();

            // Sequential write benchmark
            long writeStartTime = System.nanoTime();
            long totalBytesWritten = 0;

            for (int i = 0; i < IO_BENCHMARK_ITERATIONS; i++) {
                File testFile = new File(tempDir, "test_" + i + ".dat");
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(testFile)) {
                    byte[] data = new byte[1024 * 1024]; // 1MB
                    java.util.Arrays.fill(data, (byte) i);
                    fos.write(data);
                    fos.flush();
                    totalBytesWritten += data.length;
                }
            }

            long writeTime = System.nanoTime() - writeStartTime;
            profile.ioWriteScore = (double) totalBytesWritten / (1024 * 1024) / (writeTime / 1_000_000_000.0);

            // Sequential read benchmark
            long readStartTime = System.nanoTime();
            long totalBytesRead = 0;

            for (int i = 0; i < IO_BENCHMARK_ITERATIONS; i++) {
                File testFile = new File(tempDir, "test_" + i + ".dat");
                try (java.io.FileInputStream fis = new java.io.FileInputStream(testFile)) {
                    byte[] buffer = new byte[1024 * 1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        totalBytesRead += bytesRead;
                    }
                }
            }

            long readTime = System.nanoTime() - readStartTime;
            profile.ioReadScore = (double) totalBytesRead / (1024 * 1024) / (readTime / 1_000_000_000.0);

            // Calculate overall I/O score (harmonic mean of read/write)
            profile.ioScore = 2.0 / (1.0 / profile.ioWriteScore + 1.0 / profile.ioReadScore);

            // Cleanup test files
            for (int i = 0; i < IO_BENCHMARK_ITERATIONS; i++) {
                new File(tempDir, "test_" + i + ".dat").delete();
            }
            tempDir.delete();

        } catch (Exception e) {
            LOGGER.warn("I/O benchmark failed, using conservative estimate", e);
            profile.ioScore = 100.0; // Conservative estimate
            profile.ioWriteScore = 100.0;
            profile.ioReadScore = 100.0;
        }

        long totalTime = (System.nanoTime() - startTime) / 1_000_000;
        profile.ioBenchmarkDurationMs = totalTime;

        LOGGER.debug("I/O benchmark completed: write={:.1f} MB/s, read={:.1f} MB/s, overall={:.1f} MB/s in {}ms",
                    profile.ioWriteScore, profile.ioReadScore, profile.ioScore, totalTime);
    }

    /**
     * Calculate overall hardware tier based on benchmark results
     */
    private HardwareTier calculateHardwareTier(HardwareProfile profile) {
        LOGGER.debug("Calculating hardware tier...");

        // Score each component
        TierLevel cpuTier = classifyCPUTier(profile.cpuScore);
        TierLevel memoryTier = classifyMemoryTier(profile.systemMemoryMB);
        TierLevel ioTier = classifyIOTier(profile.ioScore);

        LOGGER.debug("Component tiers - CPU: {}, Memory: {}, I/O: {}", cpuTier, memoryTier, ioTier);

        // Calculate weighted score
        int cpuWeight = 40;  // CPU is most important for Minecraft
        int memoryWeight = 35; // Memory is critical for chunk loading
        int ioWeight = 25;   // I/O affects world loading

        double weightedScore = (cpuTier.ordinal() * cpuWeight +
                               memoryTier.ordinal() * memoryWeight +
                               ioTier.ordinal() * ioWeight) / 100.0;

        // Determine overall tier
        TierLevel overallTier;
        if (weightedScore >= 2.5) {
            overallTier = TierLevel.HIGH_END;
        } else if (weightedScore >= 1.5) {
            overallTier = TierLevel.MID_RANGE;
        } else if (weightedScore >= 0.5) {
            overallTier = TierLevel.LOW_END;
        } else {
            overallTier = TierLevel.MINIMAL;
        }

        // Create hardware tier with performance targets
        return createHardwareTier(overallTier, profile, cpuTier, memoryTier, ioTier);
    }

    private TierLevel classifyCPUTier(long cpuScore) {
        if (cpuScore >= HIGH_END_CPU_SCORE) {
            return TierLevel.HIGH_END;
        } else if (cpuScore >= MID_RANGE_CPU_SCORE) {
            return TierLevel.MID_RANGE;
        } else if (cpuScore >= MID_RANGE_CPU_SCORE / 2) {
            return TierLevel.LOW_END;
        } else {
            return TierLevel.MINIMAL;
        }
    }

    private TierLevel classifyMemoryTier(long memoryMB) {
        if (memoryMB >= HIGH_END_MEMORY_MB) {
            return TierLevel.HIGH_END;
        } else if (memoryMB >= MID_RANGE_MEMORY_MB) {
            return TierLevel.MID_RANGE;
        } else if (memoryMB >= MID_RANGE_MEMORY_MB / 2) {
            return TierLevel.LOW_END;
        } else {
            return TierLevel.MINIMAL;
        }
    }

    private TierLevel classifyIOTier(double ioScore) {
        if (ioScore >= HIGH_END_IO_SCORE) {
            return TierLevel.HIGH_END;
        } else if (ioScore >= MID_RANGE_IO_SCORE) {
            return TierLevel.MID_RANGE;
        } else if (ioScore >= MID_RANGE_IO_SCORE / 2) {
            return TierLevel.LOW_END;
        } else {
            return TierLevel.MINIMAL;
        }
    }

    private HardwareTier createHardwareTier(TierLevel overallTier, HardwareProfile profile,
                                          TierLevel cpuTier, TierLevel memoryTier, TierLevel ioTier) {

        // Define performance targets based on tier
        PerformanceTargets targets = new PerformanceTargets();

        switch (overallTier) {
            case HIGH_END:
                targets.chunkLoadingImprovementTarget = 0.25; // 25% improvement
                targets.memoryReductionTarget = 0.20; // 20% reduction
                targets.expectedTPS = 20.0;
                targets.expectedRenderDistance = 16;
                break;
            case MID_RANGE:
                targets.chunkLoadingImprovementTarget = 0.20; // 20% improvement
                targets.memoryReductionTarget = 0.15; // 15% reduction
                targets.expectedTPS = 18.0;
                targets.expectedRenderDistance = 12;
                break;
            case LOW_END:
                targets.chunkLoadingImprovementTarget = 0.15; // 15% improvement
                targets.memoryReductionTarget = 0.10; // 10% reduction
                targets.expectedTPS = 15.0;
                targets.expectedRenderDistance = 8;
                break;
            case MINIMAL:
                targets.chunkLoadingImprovementTarget = 0.10; // 10% improvement
                targets.memoryReductionTarget = 0.05; // 5% reduction
                targets.expectedTPS = 12.0;
                targets.expectedRenderDistance = 6;
                break;
        }

        return new HardwareTier(overallTier, profile, cpuTier, memoryTier, ioTier, targets);
    }

    private HardwareTier createFallbackTier() {
        LOGGER.warn("Creating fallback hardware tier due to detection failure");

        HardwareProfile fallbackProfile = new HardwareProfile();
        fallbackProfile.availableProcessors = Runtime.getRuntime().availableProcessors();
        fallbackProfile.maxHeapMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        fallbackProfile.cpuScore = MID_RANGE_CPU_SCORE / 2; // Conservative estimate
        fallbackProfile.ioScore = MID_RANGE_IO_SCORE / 2;

        PerformanceTargets conservativeTargets = new PerformanceTargets();
        conservativeTargets.chunkLoadingImprovementTarget = 0.10;
        conservativeTargets.memoryReductionTarget = 0.05;
        conservativeTargets.expectedTPS = 15.0;
        conservativeTargets.expectedRenderDistance = 8;

        return new HardwareTier(TierLevel.LOW_END, fallbackProfile,
                               TierLevel.LOW_END, TierLevel.LOW_END, TierLevel.LOW_END,
                               conservativeTargets);
    }

    // Data classes

    public enum TierLevel {
        MINIMAL("Minimal", "Entry-level hardware with basic performance"),
        LOW_END("Low-End", "Budget hardware with modest performance"),
        MID_RANGE("Mid-Range", "Mainstream hardware with good performance"),
        HIGH_END("High-End", "Premium hardware with excellent performance");

        public final String displayName;
        public final String description;

        TierLevel(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
    }

    public static class HardwareProfile {
        // System information
        public String osName;
        public String osVersion;
        public String osArch;
        public String javaVersion;
        public String jvmName;

        // Hardware specifications
        public int availableProcessors;
        public long totalMemoryMB;
        public long maxHeapMemoryMB;
        public long systemMemoryMB;
        public long totalDiskSpaceGB;
        public long freeDiskSpaceGB;

        // Performance scores
        public long cpuScore;
        public double memoryAllocationScore;
        public double memoryAccessScore;
        public double ioScore;
        public double ioWriteScore;
        public double ioReadScore;

        // Benchmark durations
        public long cpuBenchmarkDurationMs;
        public long memoryBenchmarkDurationMs;
        public long ioBenchmarkDurationMs;

        public String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static class PerformanceTargets {
        public double chunkLoadingImprovementTarget;
        public double memoryReductionTarget;
        public double expectedTPS;
        public int expectedRenderDistance;
    }

    public static class HardwareTier {
        public final TierLevel overallTier;
        public final HardwareProfile profile;
        public final TierLevel cpuTier;
        public final TierLevel memoryTier;
        public final TierLevel ioTier;
        public final PerformanceTargets targets;
        public final String name;
        public final String description;
        public final String timestamp;

        // Calculated properties
        public final Map<String, Object> recommendations;

        public HardwareTier(TierLevel overallTier, HardwareProfile profile,
                           TierLevel cpuTier, TierLevel memoryTier, TierLevel ioTier,
                           PerformanceTargets targets) {
            this.overallTier = overallTier;
            this.profile = profile;
            this.cpuTier = cpuTier;
            this.memoryTier = memoryTier;
            this.ioTier = ioTier;
            this.targets = targets;
            this.name = overallTier.displayName;
            this.description = overallTier.description;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Generate recommendations
            this.recommendations = generateRecommendations();
        }

        private Map<String, Object> generateRecommendations() {
            Map<String, Object> recs = new HashMap<>();

            // JVM recommendations
            recs.put("recommendedHeapSize", Math.min(profile.systemMemoryMB / 2, 8192));
            recs.put("recommendedGC", cpuTier.ordinal() >= 2 ? "G1GC" : "ParallelGC");

            // Performance recommendations
            recs.put("recommendedRenderDistance", targets.expectedRenderDistance);
            recs.put("recommendedChunkLoadingThreads", Math.min(profile.availableProcessors / 2, 4));

            // Optimization priorities
            List<String> priorities = new ArrayList<>();
            if (cpuTier.ordinal() < 2) priorities.add("CPU optimization");
            if (memoryTier.ordinal() < 2) priorities.add("Memory optimization");
            if (ioTier.ordinal() < 2) priorities.add("I/O optimization");
            recs.put("optimizationPriorities", priorities);

            return recs;
        }
    }
}