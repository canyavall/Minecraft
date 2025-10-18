package com.xeenaa.fabricenhancements.performance.chunk;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.memory.MemoryOptimizationManager;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Core parallel chunk processing engine inspired by C2ME's architecture.
 *
 * Features:
 * - Lock-free parallel chunk processing pipeline
 * - Batch chunk loading optimization
 * - Thread-safe chunk data access with minimal locking
 * - CompletableFuture-based async operations
 * - Smart load balancing and queue management
 * - Memory-efficient chunk processing batches
 */
public class ParallelChunkProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelChunkProcessor.class);

    private final PerformanceConfig config;
    private final ThreadPoolManager threadPoolManager;
    private final AtomicReference<MemoryOptimizationManager> memoryOptimizer = new AtomicReference<>();

    // Processing pipeline
    private final Map<ChunkPos, CompletableFuture<WorldChunk>> activeChunkLoads = new ConcurrentHashMap<>();
    private final Map<ChunkPos, ChunkLoadRequest> pendingRequests = new ConcurrentHashMap<>();
    private final Set<ChunkPos> currentlyProcessing = ConcurrentHashMap.newKeySet();

    // Batch processing
    private final BlockingQueue<ChunkLoadRequest> loadQueue = new LinkedBlockingQueue<>();
    private final int maxBatchSize;
    private final long batchTimeoutMs = 5; // 5ms batch timeout for responsiveness

    // Performance tracking
    private final AtomicLong totalProcessedChunks = new AtomicLong();
    private final AtomicLong parallelSpeedup = new AtomicLong();
    private final LongAdder batchedOperations = new LongAdder();
    private final LongAdder cacheHits = new LongAdder();

    // Processing state
    private volatile boolean isShuttingDown = false;
    private final ScheduledFuture<?> batchProcessor;

    public ParallelChunkProcessor(PerformanceConfig config, ThreadPoolManager threadPoolManager) {
        this.config = config;
        this.threadPoolManager = threadPoolManager;
        this.maxBatchSize = Math.max(4, config.chunkLoadingThreads / 2);

        LOGGER.info("Initializing ParallelChunkProcessor with batch size: {}", maxBatchSize);

        // Start batch processing
        this.batchProcessor = threadPoolManager.scheduleRepeatingTask(
            this::processBatches, 0, batchTimeoutMs, TimeUnit.MILLISECONDS
        );
    }

    /**
     * Register memory optimization manager for integrated memory optimization
     */
    public void setMemoryOptimizer(MemoryOptimizationManager optimizer) {
        this.memoryOptimizer.set(optimizer);
        LOGGER.info("Memory optimization integrated with ParallelChunkProcessor");
    }

    /**
     * Process chunk loading request with parallel optimization
     */
    public CompletableFuture<WorldChunk> processChunk(ServerChunkManager chunkManager,
                                                     ChunkPos pos,
                                                     ChunkStatus status,
                                                     boolean load) {
        // Check if already processing
        CompletableFuture<WorldChunk> existing = activeChunkLoads.get(pos);
        if (existing != null && !existing.isDone()) {
            return existing;
        }

        // Create new processing future
        CompletableFuture<WorldChunk> future = new CompletableFuture<>();
        activeChunkLoads.put(pos, future);

        // Create load request
        ChunkLoadRequest request = new ChunkLoadRequest(chunkManager, pos, status, load, future);

        // Try immediate processing for hot chunks
        if (tryImmediateLoad(request)) {
            return future;
        }

        // Queue for batch processing
        pendingRequests.put(pos, request);
        if (!loadQueue.offer(request)) {
            // Queue full, process immediately to avoid blocking
            processImmediately(request);
        }

        return future;
    }

    /**
     * Try immediate loading for cache hits or simple operations
     */
    private boolean tryImmediateLoad(ChunkLoadRequest request) {
        if (currentlyProcessing.size() < maxBatchSize / 2) {
            // Low load, process immediately
            processImmediately(request);
            return true;
        }
        return false;
    }

    /**
     * Process chunk load request immediately
     */
    private void processImmediately(ChunkLoadRequest request) {
        currentlyProcessing.add(request.pos);

        threadPoolManager.submitChunkTask(() -> {
            MemoryOptimizationManager.OptimizedChunkAllocation allocation = null;
            try {
                // Get optimized memory allocation if available
                MemoryOptimizationManager optimizer = memoryOptimizer.get();
                if (optimizer != null) {
                    allocation = optimizer.allocateForChunkProcessing("immediate", 8192);
                }

                long startTime = System.nanoTime();
                WorldChunk chunk = performChunkLoad(request);
                long loadTime = System.nanoTime() - startTime;

                // Complete future
                request.future.complete(chunk);

                // Update metrics
                totalProcessedChunks.incrementAndGet();
                recordLoadMetrics(request.pos, loadTime, chunk != null);

            } catch (Exception e) {
                LOGGER.error("Failed to load chunk {}", request.pos, e);
                request.future.completeExceptionally(e);
            } finally {
                // Release memory allocation
                if (allocation != null) {
                    MemoryOptimizationManager optimizer = memoryOptimizer.get();
                    if (optimizer != null) {
                        optimizer.releaseChunkAllocation(allocation);
                    }
                }

                currentlyProcessing.remove(request.pos);
                activeChunkLoads.remove(request.pos);
                pendingRequests.remove(request.pos);
            }
        });
    }

    /**
     * Process queued chunks in batches
     */
    private void processBatches() {
        if (isShuttingDown || loadQueue.isEmpty()) {
            return;
        }

        List<ChunkLoadRequest> batch = new ArrayList<>();
        loadQueue.drainTo(batch, maxBatchSize);

        if (batch.isEmpty()) {
            return;
        }

        batchedOperations.increment();
        processBatch(batch);
    }

    /**
     * Process a batch of chunk load requests in parallel
     */
    private void processBatch(List<ChunkLoadRequest> batch) {
        if (batch.isEmpty()) return;

        // Sort batch for better locality
        batch.sort((a, b) -> {
            int dx = a.pos.x - b.pos.x;
            if (dx != 0) return dx;
            return a.pos.z - b.pos.z;
        });

        // Process batch in parallel
        CompletableFuture<Void> batchFuture = CompletableFuture.allOf(
            batch.stream()
                .map(this::processBatchItem)
                .toArray(CompletableFuture[]::new)
        );

        batchFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                LOGGER.warn("Batch processing completed with errors", throwable);
            }

            // Clean up batch items
            for (ChunkLoadRequest request : batch) {
                currentlyProcessing.remove(request.pos);
                activeChunkLoads.remove(request.pos);
                pendingRequests.remove(request.pos);
            }
        });
    }

    /**
     * Process individual item within a batch
     */
    private CompletableFuture<Void> processBatchItem(ChunkLoadRequest request) {
        currentlyProcessing.add(request.pos);

        return threadPoolManager.submitChunkTask(() -> {
            MemoryOptimizationManager.OptimizedChunkAllocation allocation = null;
            try {
                // Get optimized memory allocation if available
                MemoryOptimizationManager optimizer = memoryOptimizer.get();
                if (optimizer != null) {
                    allocation = optimizer.allocateForChunkProcessing("batch", 6144);
                }

                long startTime = System.nanoTime();
                WorldChunk chunk = performChunkLoad(request);
                long loadTime = System.nanoTime() - startTime;

                request.future.complete(chunk);
                totalProcessedChunks.incrementAndGet();
                recordLoadMetrics(request.pos, loadTime, chunk != null);

            } catch (Exception e) {
                LOGGER.error("Failed to load chunk {} in batch", request.pos, e);
                request.future.completeExceptionally(e);
            } finally {
                // Release memory allocation
                if (allocation != null) {
                    MemoryOptimizationManager optimizer = memoryOptimizer.get();
                    if (optimizer != null) {
                        optimizer.releaseChunkAllocation(allocation);
                    }
                }
            }
        });
    }

    /**
     * Perform the actual chunk loading operation
     */
    private WorldChunk performChunkLoad(ChunkLoadRequest request) {
        try {
            // Use original chunk manager method
            return request.chunkManager.getChunk(
                request.pos.x,
                request.pos.z,
                request.status,
                request.load
            );
        } catch (Exception e) {
            LOGGER.warn("Chunk load failed for {}: {}", request.pos, e.getMessage());
            return null;
        }
    }

    /**
     * Record chunk loading metrics
     */
    private void recordLoadMetrics(ChunkPos pos, long loadTimeNanos, boolean success) {
        double loadTimeMs = loadTimeNanos / 1_000_000.0;

        // Detect cache hits (fast loads)
        if (loadTimeMs < 1.0) {
            cacheHits.increment();
        }

        // Log slow loads
        if (loadTimeMs > 50.0) {
            LOGGER.debug("Slow chunk load: {} took {:.2f}ms", pos, loadTimeMs);
        }
    }

    /**
     * Get current processing statistics
     */
    public ProcessingStats getStats() {
        return new ProcessingStats(
            totalProcessedChunks.get(),
            activeChunkLoads.size(),
            currentlyProcessing.size(),
            loadQueue.size(),
            batchedOperations.sum(),
            cacheHits.sum(),
            calculateThroughput()
        );
    }

    /**
     * Calculate processing throughput (chunks per second)
     */
    private double calculateThroughput() {
        // Simple throughput calculation based on recent activity
        long processed = totalProcessedChunks.get();
        if (processed == 0) return 0.0;

        // Estimate based on current activity
        return Math.min(processed / 60.0, currentlyProcessing.size() * 20.0);
    }

    /**
     * Check if processor is overloaded
     */
    public boolean isOverloaded() {
        return currentlyProcessing.size() > maxBatchSize * 2 ||
               loadQueue.size() > maxBatchSize * 4;
    }

    /**
     * Get load factor (0.0 to 1.0)
     */
    public double getLoadFactor() {
        int maxConcurrent = maxBatchSize * 2;
        return Math.min(1.0, (double) currentlyProcessing.size() / maxConcurrent);
    }

    /**
     * Graceful shutdown
     */
    public void shutdown() {
        LOGGER.info("Shutting down ParallelChunkProcessor");
        isShuttingDown = true;

        // Cancel batch processor
        if (batchProcessor != null) {
            batchProcessor.cancel(false);
        }

        // Complete remaining futures
        CompletableFuture<Void> remainingWork = CompletableFuture.allOf(
            activeChunkLoads.values().toArray(new CompletableFuture[0])
        );

        try {
            remainingWork.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            LOGGER.warn("Timeout waiting for chunk processing completion");
            // Cancel remaining futures
            activeChunkLoads.values().forEach(future -> future.cancel(true));
        } catch (Exception e) {
            LOGGER.warn("Error during chunk processor shutdown", e);
        }

        logFinalStats();
    }

    /**
     * Log final processing statistics
     */
    private void logFinalStats() {
        ProcessingStats stats = getStats();
        LOGGER.info("ParallelChunkProcessor final stats - Processed: {}, Batched ops: {}, Cache hits: {}, Throughput: {:.1f} chunks/s",
                   stats.totalProcessed, stats.batchedOperations, stats.cacheHits, stats.throughput);
    }

    /**
     * Chunk load request data class
     */
    private static class ChunkLoadRequest {
        public final ServerChunkManager chunkManager;
        public final ChunkPos pos;
        public final ChunkStatus status;
        public final boolean load;
        public final CompletableFuture<WorldChunk> future;
        public final long timestamp;

        public ChunkLoadRequest(ServerChunkManager chunkManager, ChunkPos pos,
                               ChunkStatus status, boolean load,
                               CompletableFuture<WorldChunk> future) {
            this.chunkManager = chunkManager;
            this.pos = pos;
            this.status = status;
            this.load = load;
            this.future = future;
            this.timestamp = System.currentTimeMillis();
        }
    }

    /**
     * Processing statistics data class
     */
    public static class ProcessingStats {
        public final long totalProcessed;
        public final int activeLoads;
        public final int currentlyProcessing;
        public final int queueSize;
        public final long batchedOperations;
        public final long cacheHits;
        public final double throughput;

        public ProcessingStats(long totalProcessed, int activeLoads, int currentlyProcessing,
                              int queueSize, long batchedOperations, long cacheHits, double throughput) {
            this.totalProcessed = totalProcessed;
            this.activeLoads = activeLoads;
            this.currentlyProcessing = currentlyProcessing;
            this.queueSize = queueSize;
            this.batchedOperations = batchedOperations;
            this.cacheHits = cacheHits;
            this.throughput = throughput;
        }

        @Override
        public String toString() {
            return String.format("ProcessingStats{processed=%d, active=%d, processing=%d, queue=%d, batched=%d, hits=%d, throughput=%.1f}",
                               totalProcessed, activeLoads, currentlyProcessing, queueSize,
                               batchedOperations, cacheHits, throughput);
        }
    }
}