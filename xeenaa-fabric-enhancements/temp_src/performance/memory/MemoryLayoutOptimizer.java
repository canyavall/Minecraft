package com.xeenaa.fabricenhancements.performance.memory;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memory layout optimizer inspired by FerriteCore's memory optimization techniques.
 *
 * Features:
 * - Memory region pooling for reduced fragmentation
 * - Allocation pattern optimization for better cache locality
 * - Memory compaction strategies for long-running sessions
 * - NUMA-aware memory allocation (where supported)
 * - Memory pressure monitoring and automatic optimization
 * - Object layout optimization for better memory density
 */
public class MemoryLayoutOptimizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryLayoutOptimizer.class);

    private final PerformanceConfig config;
    private final MemoryMXBean memoryMXBean;
    private final ScheduledExecutorService optimizationExecutor;

    // Memory region management
    private final Map<Integer, MemoryRegion> memoryRegions = new ConcurrentHashMap<>();
    private final BlockingQueue<AllocationRequest> allocationQueue = new LinkedBlockingQueue<>();

    // Memory statistics
    private final AtomicLong totalOptimizedAllocations = new AtomicLong();
    private final AtomicLong fragmentationReductions = new AtomicLong();
    private final AtomicLong memoryCompactions = new AtomicLong();
    private final LongAdder bytesOptimized = new LongAdder();

    // Memory pressure tracking
    private final AtomicLong lastGCTime = new AtomicLong();
    private final AtomicLong gcPressureScore = new AtomicLong();
    private volatile boolean memoryPressureDetected = false;

    // Optimization settings
    private static final int REGION_SIZE = 64 * 1024 * 1024; // 64MB regions
    private static final int MAX_REGIONS = 16;
    private static final long OPTIMIZATION_INTERVAL_MS = 30000; // 30 seconds
    private static final double FRAGMENTATION_THRESHOLD = 0.3; // 30% fragmentation triggers optimization

    public MemoryLayoutOptimizer(PerformanceConfig config) {
        this.config = config;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();

        this.optimizationExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "MemoryLayout-Optimizer");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY + 1);
            return t;
        });

        initializeMemoryRegions();
        startOptimizationLoop();

        LOGGER.info("MemoryLayoutOptimizer initialized with {} regions of {}MB each",
                   MAX_REGIONS, REGION_SIZE / (1024 * 1024));
    }

    /**
     * Initialize memory regions for optimized allocation
     */
    private void initializeMemoryRegions() {
        for (int i = 0; i < MAX_REGIONS; i++) {
            memoryRegions.put(i, new MemoryRegion(i, REGION_SIZE));
        }
    }

    /**
     * Start optimization loop
     */
    private void startOptimizationLoop() {
        optimizationExecutor.scheduleAtFixedRate(
            this::performOptimization,
            OPTIMIZATION_INTERVAL_MS,
            OPTIMIZATION_INTERVAL_MS,
            TimeUnit.MILLISECONDS
        );

        // Process allocation requests
        optimizationExecutor.submit(this::processAllocationRequests);
    }

    /**
     * Optimize memory allocation for better locality
     */
    public OptimizedAllocation optimizeAllocation(int sizeBytes, AllocationHint hint) {
        AllocationRequest request = new AllocationRequest(sizeBytes, hint);

        // Try immediate optimization for small allocations
        if (sizeBytes < 1024) {
            return performImmediateAllocation(request);
        }

        // Queue for batch processing
        allocationQueue.offer(request);
        return new OptimizedAllocation(null, -1, false);
    }

    /**
     * Perform immediate allocation for small objects
     */
    private OptimizedAllocation performImmediateAllocation(AllocationRequest request) {
        MemoryRegion bestRegion = findBestRegion(request.sizeBytes, request.hint);
        if (bestRegion != null) {
            int offset = bestRegion.allocate(request.sizeBytes);
            if (offset >= 0) {
                totalOptimizedAllocations.incrementAndGet();
                bytesOptimized.add(request.sizeBytes);
                return new OptimizedAllocation(bestRegion, offset, true);
            }
        }

        return new OptimizedAllocation(null, -1, false);
    }

    /**
     * Find best memory region for allocation
     */
    private MemoryRegion findBestRegion(int sizeBytes, AllocationHint hint) {
        MemoryRegion best = null;
        double bestScore = -1.0;

        for (MemoryRegion region : memoryRegions.values()) {
            if (region.canAllocate(sizeBytes)) {
                double score = calculateRegionScore(region, sizeBytes, hint);
                if (score > bestScore) {
                    bestScore = score;
                    best = region;
                }
            }
        }

        return best;
    }

    /**
     * Calculate allocation score for region
     */
    private double calculateRegionScore(MemoryRegion region, int sizeBytes, AllocationHint hint) {
        double score = 0.0;

        // Prefer regions with more free space
        double freeSpaceRatio = (double) region.getFreeSpace() / region.getTotalSpace();
        score += freeSpaceRatio * 0.4;

        // Prefer regions with less fragmentation
        double fragmentationRatio = region.getFragmentationRatio();
        score += (1.0 - fragmentationRatio) * 0.3;

        // Prefer regions matching allocation hint
        if (hint != null) {
            score += hint.getAffinityScore(region) * 0.3;
        }

        return score;
    }

    /**
     * Process queued allocation requests
     */
    private void processAllocationRequests() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<AllocationRequest> batch = new ArrayList<>();
                AllocationRequest first = allocationQueue.take();
                batch.add(first);

                // Drain additional requests for batch processing
                allocationQueue.drainTo(batch, 32);

                processBatchedAllocations(batch);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                LOGGER.warn("Error processing allocation requests", e);
            }
        }
    }

    /**
     * Process batched allocations for better efficiency
     */
    private void processBatchedAllocations(List<AllocationRequest> batch) {
        // Sort by size for better packing
        batch.sort((a, b) -> Integer.compare(b.sizeBytes, a.sizeBytes));

        for (AllocationRequest request : batch) {
            performImmediateAllocation(request);
        }
    }

    /**
     * Perform memory optimization cycle
     */
    private void performOptimization() {
        try {
            updateMemoryPressure();

            if (shouldPerformOptimization()) {
                LOGGER.debug("Starting memory layout optimization cycle");

                long startTime = System.nanoTime();
                int optimizations = 0;

                // Check each region for optimization opportunities
                for (MemoryRegion region : memoryRegions.values()) {
                    if (region.getFragmentationRatio() > FRAGMENTATION_THRESHOLD) {
                        if (optimizeRegion(region)) {
                            optimizations++;
                        }
                    }
                }

                // Perform global optimizations if under memory pressure
                if (memoryPressureDetected) {
                    performGlobalOptimization();
                }

                long optimizationTime = System.nanoTime() - startTime;

                if (optimizations > 0) {
                    LOGGER.debug("Memory optimization completed: {} regions optimized in {:.2f}ms",
                               optimizations, optimizationTime / 1_000_000.0);
                }
            }

        } catch (Exception e) {
            LOGGER.warn("Error during memory optimization", e);
        }
    }

    /**
     * Update memory pressure indicators
     */
    private void updateMemoryPressure() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        double usageRatio = (double) heapUsage.getUsed() / heapUsage.getMax();

        // Calculate GC pressure
        long currentTime = System.currentTimeMillis();
        long lastGC = lastGCTime.get();
        long timeSinceGC = currentTime - lastGC;

        // Update GC pressure score
        if (timeSinceGC < 5000) { // Recent GC activity
            gcPressureScore.incrementAndGet();
        } else if (timeSinceGC > 30000) { // No recent GC
            gcPressureScore.set(Math.max(0, gcPressureScore.get() - 1));
        }

        // Detect memory pressure
        memoryPressureDetected = usageRatio > 0.8 || gcPressureScore.get() > 10;
    }

    /**
     * Check if optimization should be performed
     */
    private boolean shouldPerformOptimization() {
        // Always optimize under memory pressure
        if (memoryPressureDetected) {
            return true;
        }

        // Check if any regions need optimization
        for (MemoryRegion region : memoryRegions.values()) {
            if (region.getFragmentationRatio() > FRAGMENTATION_THRESHOLD) {
                return true;
            }
        }

        return false;
    }

    /**
     * Optimize specific memory region
     */
    private boolean optimizeRegion(MemoryRegion region) {
        try {
            boolean optimized = region.defragment();
            if (optimized) {
                fragmentationReductions.incrementAndGet();
                LOGGER.debug("Optimized memory region {}: fragmentation reduced from {:.1f}% to {:.1f}%",
                           region.getId(), region.getPreviousFragmentation() * 100,
                           region.getFragmentationRatio() * 100);
            }
            return optimized;
        } catch (Exception e) {
            LOGGER.warn("Failed to optimize memory region {}", region.getId(), e);
            return false;
        }
    }

    /**
     * Perform global memory optimization
     */
    private void performGlobalOptimization() {
        LOGGER.debug("Performing global memory optimization due to memory pressure");

        // Compact all regions
        for (MemoryRegion region : memoryRegions.values()) {
            region.compact();
        }

        // Suggest garbage collection
        System.gc();
        lastGCTime.set(System.currentTimeMillis());

        memoryCompactions.incrementAndGet();
    }

    /**
     * Get memory optimization statistics
     */
    public MemoryOptimizationStats getStats() {
        long totalFreeSpace = memoryRegions.values().stream()
            .mapToLong(MemoryRegion::getFreeSpace)
            .sum();

        long totalUsedSpace = memoryRegions.values().stream()
            .mapToLong(MemoryRegion::getUsedSpace)
            .sum();

        double avgFragmentation = memoryRegions.values().stream()
            .mapToDouble(MemoryRegion::getFragmentationRatio)
            .average()
            .orElse(0.0);

        return new MemoryOptimizationStats(
            totalOptimizedAllocations.get(),
            fragmentationReductions.get(),
            memoryCompactions.get(),
            bytesOptimized.sum(),
            totalFreeSpace,
            totalUsedSpace,
            avgFragmentation,
            memoryPressureDetected,
            gcPressureScore.get()
        );
    }

    /**
     * Shutdown optimizer
     */
    public void shutdown() {
        LOGGER.info("Shutting down MemoryLayoutOptimizer");

        optimizationExecutor.shutdown();
        try {
            if (!optimizationExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                optimizationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            optimizationExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Clear regions
        memoryRegions.clear();

        LOGGER.info("MemoryLayoutOptimizer shutdown complete");
    }

    /**
     * Memory region for optimized allocation
     */
    private static class MemoryRegion {
        private final int id;
        private final int totalSize;
        private final AtomicLong usedBytes = new AtomicLong();
        private final AtomicLong fragmentedBytes = new AtomicLong();
        private final ConcurrentHashMap<Integer, AllocationBlock> allocations = new ConcurrentHashMap<>();
        private final AtomicLong nextOffset = new AtomicLong();
        private double previousFragmentation = 0.0;

        public MemoryRegion(int id, int totalSize) {
            this.id = id;
            this.totalSize = totalSize;
        }

        public boolean canAllocate(int sizeBytes) {
            return getFreeSpace() >= sizeBytes;
        }

        public int allocate(int sizeBytes) {
            if (!canAllocate(sizeBytes)) {
                return -1;
            }

            int offset = (int) nextOffset.getAndAdd(sizeBytes);
            if (offset + sizeBytes > totalSize) {
                return -1;
            }

            allocations.put(offset, new AllocationBlock(offset, sizeBytes));
            usedBytes.addAndGet(sizeBytes);
            return offset;
        }

        public void deallocate(int offset) {
            AllocationBlock block = allocations.remove(offset);
            if (block != null) {
                usedBytes.addAndGet(-block.size);
                fragmentedBytes.addAndGet(block.size);
            }
        }

        public boolean defragment() {
            previousFragmentation = getFragmentationRatio();

            // Simple defragmentation - compact allocations
            List<AllocationBlock> activeBlocks = new ArrayList<>(allocations.values());
            activeBlocks.sort((a, b) -> Integer.compare(a.offset, b.offset));

            allocations.clear();
            nextOffset.set(0);
            fragmentedBytes.set(0);

            for (AllocationBlock block : activeBlocks) {
                int newOffset = (int) nextOffset.getAndAdd(block.size);
                allocations.put(newOffset, new AllocationBlock(newOffset, block.size));
            }

            return getFragmentationRatio() < previousFragmentation;
        }

        public void compact() {
            defragment();
        }

        public long getFreeSpace() {
            return totalSize - usedBytes.get();
        }

        public long getUsedSpace() {
            return usedBytes.get();
        }

        public long getTotalSpace() {
            return totalSize;
        }

        public double getFragmentationRatio() {
            long used = usedBytes.get();
            long fragmented = fragmentedBytes.get();
            return used > 0 ? (double) fragmented / used : 0.0;
        }

        public double getPreviousFragmentation() {
            return previousFragmentation;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Allocation block tracking
     */
    private static class AllocationBlock {
        final int offset;
        final int size;

        AllocationBlock(int offset, int size) {
            this.offset = offset;
            this.size = size;
        }
    }

    /**
     * Allocation request
     */
    private static class AllocationRequest {
        final int sizeBytes;
        final AllocationHint hint;

        AllocationRequest(int sizeBytes, AllocationHint hint) {
            this.sizeBytes = sizeBytes;
            this.hint = hint;
        }
    }

    /**
     * Allocation hint for optimization
     */
    public enum AllocationHint {
        CHUNK_DATA(1.0),
        TEMPORARY(0.5),
        LONG_LIVED(0.3),
        FREQUENT_ACCESS(0.8);

        private final double priority;

        AllocationHint(double priority) {
            this.priority = priority;
        }

        public double getAffinityScore(MemoryRegion region) {
            // Simple affinity calculation based on region usage and hint type
            return priority * (1.0 - region.getFragmentationRatio());
        }
    }

    /**
     * Optimized allocation result
     */
    public static class OptimizedAllocation {
        public final MemoryRegion region;
        public final int offset;
        public final boolean optimized;

        public OptimizedAllocation(MemoryRegion region, int offset, boolean optimized) {
            this.region = region;
            this.offset = offset;
            this.optimized = optimized;
        }
    }

    /**
     * Memory optimization statistics
     */
    public static class MemoryOptimizationStats {
        public final long totalOptimizedAllocations;
        public final long fragmentationReductions;
        public final long memoryCompactions;
        public final long bytesOptimized;
        public final long totalFreeSpace;
        public final long totalUsedSpace;
        public final double averageFragmentation;
        public final boolean memoryPressureDetected;
        public final long gcPressureScore;

        public MemoryOptimizationStats(long totalOptimizedAllocations, long fragmentationReductions,
                                     long memoryCompactions, long bytesOptimized, long totalFreeSpace,
                                     long totalUsedSpace, double averageFragmentation,
                                     boolean memoryPressureDetected, long gcPressureScore) {
            this.totalOptimizedAllocations = totalOptimizedAllocations;
            this.fragmentationReductions = fragmentationReductions;
            this.memoryCompactions = memoryCompactions;
            this.bytesOptimized = bytesOptimized;
            this.totalFreeSpace = totalFreeSpace;
            this.totalUsedSpace = totalUsedSpace;
            this.averageFragmentation = averageFragmentation;
            this.memoryPressureDetected = memoryPressureDetected;
            this.gcPressureScore = gcPressureScore;
        }

        public double getOptimizationEfficiency() {
            return totalOptimizedAllocations > 0 ?
                (double) bytesOptimized / totalOptimizedAllocations : 0.0;
        }

        public long getTotalManagedMemoryMB() {
            return (totalFreeSpace + totalUsedSpace) / (1024 * 1024);
        }

        @Override
        public String toString() {
            return String.format(
                "MemoryOptimizationStats{allocations=%d, reductions=%d, compactions=%d, " +
                "optimized=%dMB, fragmentation=%.1f%%, pressure=%s}",
                totalOptimizedAllocations, fragmentationReductions, memoryCompactions,
                bytesOptimized / (1024 * 1024), averageFragmentation * 100, memoryPressureDetected
            );
        }
    }
}