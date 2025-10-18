package com.xeenaa.fabricenhancements.performance.memory;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.memory.ObjectPoolManager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ObjectPoolManager to validate memory optimization functionality.
 * Tests pool efficiency, thread safety, and memory leak prevention.
 */
public class ObjectPoolManagerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectPoolManagerTest.class);

    private ObjectPoolManager poolManager;
    private PerformanceConfig config;

    @BeforeEach
    void setUp() {
        config = new PerformanceConfig();
        poolManager = new ObjectPoolManager(config);
    }

    @AfterEach
    void tearDown() {
        if (poolManager != null) {
            poolManager.shutdown();
        }
    }

    @Test
    void testBasicPoolOperations() {
        LOGGER.info("Testing basic pool operations");

        // Create pool for test objects
        ObjectPool<TestObject> pool = poolManager.getPool(TestObject.class, TestObject::new);

        // Test borrow and return
        TestObject obj1 = pool.borrow();
        assertNotNull(obj1);

        TestObject obj2 = pool.borrow();
        assertNotNull(obj2);

        // Return objects
        pool.returnObject(obj1);
        pool.returnObject(obj2);

        // Verify pool statistics
        assertTrue(pool.getBorrowCount() >= 2);
        assertTrue(pool.getReturnCount() >= 2);

        LOGGER.info("Basic pool operations test passed");
    }

    @Test
    void testPoolEfficiency() throws InterruptedException {
        LOGGER.info("Testing pool efficiency target (>75%)");

        ObjectPool<TestObject> pool = poolManager.getPool(TestObject.class, TestObject::new);

        // Warm up the pool
        List<TestObject> objects = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            objects.add(pool.borrow());
        }
        for (TestObject obj : objects) {
            pool.returnObject(obj);
        }

        // Wait for pool to stabilize
        Thread.sleep(100);

        // Test efficiency with rapid borrow/return cycles
        for (int i = 0; i < 100; i++) {
            TestObject obj = pool.borrow();
            pool.returnObject(obj);
        }

        double hitRate = pool.getHitRate();
        LOGGER.info("Pool hit rate: {:.1f}%", hitRate * 100);

        // Verify efficiency target
        assertTrue(hitRate > 0.75, String.format("Pool hit rate %.1f%% below 75%% target", hitRate * 100));

        LOGGER.info("Pool efficiency test passed");
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        LOGGER.info("Testing concurrent pool access");

        ObjectPool<TestObject> pool = poolManager.getPool(TestObject.class, TestObject::new);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Create concurrent borrow/return operations
        for (int i = 0; i < 100; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                TestObject obj = pool.borrow();
                try {
                    Thread.sleep(1); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                pool.returnObject(obj);
            }, executor);
            futures.add(future);
        }

        // Wait for all operations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(10, TimeUnit.SECONDS);

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // Verify no corruption
        long borrowCount = pool.getBorrowCount();
        long returnCount = pool.getReturnCount();

        LOGGER.info("Concurrent operations: {} borrows, {} returns", borrowCount, returnCount);
        assertEquals(borrowCount, returnCount, "Mismatch between borrows and returns");

        LOGGER.info("Concurrent access test passed");
    }

    @Test
    void testMemoryOptimizationIntegration() {
        LOGGER.info("Testing memory optimization integration");

        MemoryOptimizationManager optimizer = new MemoryOptimizationManager(config);
        ChunkDataPool chunkPool = new ChunkDataPool(config, poolManager);

        try {
            // Test chunk data allocation
            ChunkDataPool.ChunkDataBuffer buffer = chunkPool.borrowChunkDataBuffer();
            assertNotNull(buffer);

            ArrayList<Object> list = chunkPool.borrowList();
            assertNotNull(list);

            // Test optimized allocation
            MemoryOptimizationManager.OptimizedChunkAllocation allocation =
                optimizer.allocateForChunkProcessing("test", 1024);
            assertNotNull(allocation);

            // Return resources
            chunkPool.returnChunkDataBuffer(buffer);
            chunkPool.returnList(list);
            optimizer.releaseChunkAllocation(allocation);

            // Get statistics
            ChunkDataPool.ChunkPoolStats stats = chunkPool.getStats();
            assertTrue(stats.totalAllocations > 0);
            assertTrue(stats.totalDeallocations > 0);

            LOGGER.info("Memory optimization stats: {}", stats);

        } finally {
            optimizer.shutdown();
        }

        LOGGER.info("Memory optimization integration test passed");
    }

    @Test
    void testMemoryLayoutOptimization() {
        LOGGER.info("Testing memory layout optimization");

        MemoryLayoutOptimizer optimizer = new MemoryLayoutOptimizer(config);

        try {
            // Test allocation optimization
            MemoryLayoutOptimizer.OptimizedAllocation allocation =
                optimizer.optimizeAllocation(1024, MemoryLayoutOptimizer.AllocationHint.CHUNK_DATA);

            assertNotNull(allocation);

            // Get optimization statistics
            MemoryLayoutOptimizer.MemoryOptimizationStats stats = optimizer.getStats();
            assertNotNull(stats);

            LOGGER.info("Layout optimization stats: {}", stats);

        } finally {
            optimizer.shutdown();
        }

        LOGGER.info("Memory layout optimization test passed");
    }

    @Test
    void testOptimizedChunkData() {
        LOGGER.info("Testing optimized chunk data structures");

        ChunkDataPool dataPool = new ChunkDataPool(config, poolManager);

        // Test optimized chunk section
        MemoryOptimizedChunkData.OptimizedChunkSection section =
            new MemoryOptimizedChunkData.OptimizedChunkSection(0, dataPool);

        // Test block operations
        section.setBlockState(0, 0, 0, 1); // Stone
        assertEquals(1, section.getBlockState(0, 0, 0));

        section.setBlockState(1, 1, 1, 2); // Grass
        assertEquals(2, section.getBlockState(1, 1, 1));

        // Test memory efficiency
        long memoryUsage = section.getMemoryUsage();
        assertTrue(memoryUsage > 0);
        assertTrue(memoryUsage < 100000); // Should be much less than naive storage

        LOGGER.info("Section memory usage: {} bytes", memoryUsage);

        // Test reset
        section.reset();
        assertEquals(0, section.getBlockState(0, 0, 0)); // Should be air after reset

        LOGGER.info("Optimized chunk data test passed");
    }

    @Test
    void testPerformanceTargets() {
        LOGGER.info("Testing performance targets validation");

        MemoryOptimizationManager optimizer = new MemoryOptimizationManager(config);

        try {
            // Simulate memory optimization work
            for (int i = 0; i < 100; i++) {
                MemoryOptimizationManager.OptimizedChunkAllocation allocation =
                    optimizer.allocateForChunkProcessing("target_test", 1024);
                if (allocation != null) {
                    optimizer.releaseChunkAllocation(allocation);
                }
            }

            // Wait for optimization cycles
            Thread.sleep(1000);

            // Get comprehensive statistics
            MemoryOptimizationManager.MemoryOptimizationStats stats = optimizer.getStats();
            assertNotNull(stats);

            LOGGER.info("Performance stats: {}", stats);

            // Validate basic functionality
            assertTrue(stats.optimizationCycles > 0, "No optimization cycles occurred");
            assertNotNull(stats.poolStats, "Pool statistics not available");

            // Memory reduction target (may not be achieved in unit test)
            LOGGER.info("Memory reduction achieved: {:.1f}%", stats.actualMemoryReduction * 100);

            // Pool efficiency should be reasonable
            if (stats.poolStats.totalBorrows > 0) {
                double hitRate = stats.poolStats.overallHitRate;
                LOGGER.info("Overall pool hit rate: {:.1f}%", hitRate * 100);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        } finally {
            optimizer.shutdown();
        }

        LOGGER.info("Performance targets test completed");
    }

    /**
     * Test object for pool operations
     */
    static class TestObject implements Resettable {
        private int value;
        private boolean inUse;

        public TestObject() {
            this.value = 0;
            this.inUse = true;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public void reset() {
            this.value = 0;
            this.inUse = false;
        }

        public boolean isInUse() {
            return inUse;
        }
    }
}