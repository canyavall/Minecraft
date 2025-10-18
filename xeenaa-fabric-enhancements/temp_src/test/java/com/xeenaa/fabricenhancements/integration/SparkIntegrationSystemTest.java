package com.xeenaa.fabricenhancements.integration;

import com.xeenaa.fabricenhancements.config.PerformanceConfig;
import com.xeenaa.fabricenhancements.performance.PerformanceManager;
import com.xeenaa.fabricenhancements.performance.profiling.SparkIntegration;
import com.xeenaa.fabricenhancements.performance.metrics.MetricsCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive system integration test for Spark Profiler Integration.
 *
 * This test validates the complete implementation including:
 * - Configuration loading and validation
 * - Spark integration initialization
 * - Real-time monitoring functionality
 * - Performance metrics collection
 * - Report generation
 * - Resource cleanup
 *
 * The test ensures all components work together as specified in T001.1.
 */
public class SparkIntegrationSystemTest {

    private PerformanceConfig config;
    private PerformanceManager performanceManager;
    private SparkIntegration sparkIntegration;
    private MetricsCollector metricsCollector;

    @BeforeEach
    void setUp() {
        // Use development configuration for comprehensive testing
        config = PerformanceConfig.getDevelopmentConfig();

        // Validate Spark-specific settings are correct
        config.validateSparkSettings();

        performanceManager = new PerformanceManager(config);
        sparkIntegration = new SparkIntegration();
        metricsCollector = new MetricsCollector(config);
    }

    @Test
    @DisplayName("Complete System Integration Test")
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void testCompleteSystemIntegration() {
        try {
            // Phase 1: Initialization
            validateInitialization();

            // Phase 2: Configuration Validation
            validateConfiguration();

            // Phase 3: Real-time Monitoring
            validateRealTimeMonitoring();

            // Phase 4: Performance Metrics
            validatePerformanceMetrics();

            // Phase 5: Alert System
            validateAlertSystem();

            // Phase 6: Report Generation
            validateReportGeneration();

            // Phase 7: Performance Impact
            validatePerformanceImpact();

            System.out.println("✓ Complete system integration test passed successfully");

        } finally {
            // Phase 8: Cleanup
            performCleanup();
        }
    }

    private void validateInitialization() {
        System.out.println("Testing initialization...");

        // Initialize Spark integration
        sparkIntegration.initialize(null); // Mock server for testing
        assertTrue(sparkIntegration.isInitialized(), "Spark integration should be initialized");

        // Initialize performance manager
        performanceManager.initialize(null);

        // Initialize metrics collector
        metricsCollector.initialize(null);
        metricsCollector.startCollection();
        assertTrue(metricsCollector.isCollecting(), "Metrics collection should be active");

        System.out.println("✓ Initialization successful");
    }

    private void validateConfiguration() {
        System.out.println("Testing configuration validation...");

        // Validate Spark-specific settings
        assertEquals(2, config.sparkSamplingIntervalMs, "Sampling interval should be 2ms");
        assertEquals(18.0, config.tpsAlertThreshold, "TPS alert threshold should be 18.0");
        assertEquals(7168L, config.memoryAlertThresholdMB, "Memory alert threshold should be 7168MB");
        assertTrue(config.sparkWebInterfaceEnabled, "Web interface should be enabled");
        assertEquals(4567, config.sparkWebInterfacePort, "Web interface port should be 4567");

        // Validate web interface
        assertTrue(sparkIntegration.isWebInterfaceEnabled(), "Web interface should be enabled");
        assertEquals(4567, sparkIntegration.getWebInterfacePort(), "Web interface port should match config");

        // Validate performance thresholds
        assertTrue(config.maxPerformanceOverheadPercent <= 1.0, "Performance overhead limit should be ≤1%");

        System.out.println("✓ Configuration validation successful");
    }

    private void validateRealTimeMonitoring() {
        System.out.println("Testing real-time monitoring...");

        // Test TPS monitoring
        double initialTPS = sparkIntegration.getCurrentTPS();
        assertTrue(initialTPS >= 0.0 && initialTPS <= 20.0, "TPS should be in valid range");

        // Simulate chunk load events
        for (int i = 0; i < 100; i++) {
            long loadTime = (long) (Math.random() * 100_000_000); // 0-100ms
            boolean cacheHit = Math.random() < 0.8;

            sparkIntegration.recordChunkLoadEvent(i % 10, i % 10, loadTime);
            sparkIntegration.recordCacheHit(cacheHit);
        }

        // Verify metrics are being tracked
        assertTrue(sparkIntegration.getTotalChunkLoads() == 100, "Should have recorded 100 chunk loads");

        double cacheHitRatio = sparkIntegration.getCacheHitRatio();
        assertTrue(cacheHitRatio > 0.5 && cacheHitRatio < 1.0, "Cache hit ratio should be reasonable");

        System.out.println("✓ Real-time monitoring successful");
    }

    private void validatePerformanceMetrics() {
        System.out.println("Testing performance metrics collection...");

        // Test custom metrics collection
        long initialLoads = sparkIntegration.getTotalChunkLoads();

        // Simulate more chunk operations with various patterns
        simulateChunkOperations(200);

        long finalLoads = sparkIntegration.getTotalChunkLoads();
        assertEquals(200, finalLoads - initialLoads, "Should have recorded exactly 200 additional loads");

        // Test metrics summary
        String summary = sparkIntegration.getPerformanceMetricsSummary();
        assertNotNull(summary, "Performance summary should not be null");
        assertFalse(summary.isEmpty(), "Performance summary should not be empty");

        // Verify summary contains key metrics
        assertTrue(summary.contains("TPS"), "Summary should contain TPS information");
        assertTrue(summary.contains("Memory"), "Summary should contain memory information");
        assertTrue(summary.contains("Chunk"), "Summary should contain chunk information");

        System.out.println("✓ Performance metrics validation successful");
    }

    private void validateAlertSystem() {
        System.out.println("Testing alert system...");

        // Test low TPS detection (simulated by recording many slow operations)
        for (int i = 0; i < 50; i++) {
            // Simulate very slow chunk loads that would trigger TPS alerts
            sparkIntegration.recordChunkLoadEvent(i, i, 500_000_000L); // 500ms loads
        }

        // Test memory monitoring (the integration should handle this internally)
        double memoryUsage = getCurrentMemoryUsageMB();
        assertTrue(memoryUsage > 0, "Memory usage should be positive");

        // Test performance report generation with alerts
        sparkIntegration.generateSparkReport(); // Should log any alerts

        System.out.println("✓ Alert system validation successful");
    }

    private void validateReportGeneration() {
        System.out.println("Testing report generation...");

        // Generate Spark-specific report
        sparkIntegration.generateSparkReport();

        // Test performance manager report integration
        if (performanceManager.isSparkIntegrationAvailable()) {
            String comprehensiveSummary = performanceManager.getPerformanceSummary();
            assertNotNull(comprehensiveSummary, "Comprehensive summary should not be null");
            assertTrue(comprehensiveSummary.contains("Spark"), "Summary should contain Spark data");
        }

        // Verify configuration files exist
        assertTrue(Files.exists(Paths.get("src/main/resources/spark-config.yml")),
                  "Spark YAML config should exist");
        assertTrue(Files.exists(Paths.get("src/main/resources/spark.properties")),
                  "Spark properties config should exist");
        assertTrue(Files.exists(Paths.get("src/main/resources/spark-integration.json")),
                  "Spark JSON config should exist");

        System.out.println("✓ Report generation validation successful");
    }

    private void validatePerformanceImpact() {
        System.out.println("Testing performance impact...");

        // Measure overhead of Spark integration
        long startTime = System.nanoTime();

        // Perform operations that would be typical during gameplay
        for (int i = 0; i < 1000; i++) {
            sparkIntegration.recordChunkLoadEvent(i % 100, i % 100,
                                                (long) (Math.random() * 50_000_000));
            sparkIntegration.recordCacheHit(Math.random() < 0.8);
            sparkIntegration.getCurrentTPS();
        }

        long endTime = System.nanoTime();
        double operationTimeMs = (endTime - startTime) / 1_000_000.0;
        double avgTimePerOp = operationTimeMs / 1000.0;

        System.out.printf("Performance impact: %.3f ms for 1000 operations (%.3f ms/op)%n",
                         operationTimeMs, avgTimePerOp);

        // Each operation should take less than 0.01ms on average
        assertTrue(avgTimePerOp < 0.01, "Operations should be very fast: " + avgTimePerOp + "ms/op");

        // Validate memory usage is reasonable
        long memoryUsage = getCurrentMemoryUsageMB();
        assertTrue(memoryUsage < 1000, "Memory usage should be reasonable: " + memoryUsage + "MB");

        System.out.println("✓ Performance impact validation successful");
    }

    private void performCleanup() {
        System.out.println("Performing cleanup...");

        try {
            if (metricsCollector != null && metricsCollector.isCollecting()) {
                metricsCollector.stopCollection();
            }

            if (sparkIntegration != null && sparkIntegration.isInitialized()) {
                sparkIntegration.shutdown();
            }

            if (performanceManager != null) {
                performanceManager.shutdown();
            }

            System.out.println("✓ Cleanup completed successfully");

        } catch (Exception e) {
            System.err.println("Warning: Error during cleanup: " + e.getMessage());
        }
    }

    // Helper methods

    private void simulateChunkOperations(int count) {
        for (int i = 0; i < count; i++) {
            int x = i % 50;
            int z = i % 50;

            // Vary load times to simulate different scenarios
            long loadTime;
            boolean cacheHit;

            if (i % 5 == 0) {
                // Simulate cache miss with longer load time
                loadTime = (long) (50_000_000 + Math.random() * 100_000_000); // 50-150ms
                cacheHit = false;
            } else {
                // Simulate cache hit with short load time
                loadTime = (long) (Math.random() * 5_000_000); // 0-5ms
                cacheHit = true;
            }

            sparkIntegration.recordChunkLoadEvent(x, z, loadTime);
            sparkIntegration.recordCacheHit(cacheHit);
        }
    }

    private double getCurrentMemoryUsageMB() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return usedMemory / (1024.0 * 1024.0);
    }

    @Test
    @DisplayName("Configuration File Validation")
    void testConfigurationFiles() {
        System.out.println("Testing configuration files...");

        // Verify all configuration files exist and are readable
        Path[] configFiles = {
            Paths.get("src/main/resources/spark-config.yml"),
            Paths.get("src/main/resources/spark.properties"),
            Paths.get("src/main/resources/spark-integration.json")
        };

        for (Path configFile : configFiles) {
            assertTrue(Files.exists(configFile), "Config file should exist: " + configFile);
            assertTrue(Files.isReadable(configFile), "Config file should be readable: " + configFile);

            try {
                long size = Files.size(configFile);
                assertTrue(size > 100, "Config file should not be empty: " + configFile);
            } catch (Exception e) {
                fail("Error reading config file: " + configFile + " - " + e.getMessage());
            }
        }

        System.out.println("✓ Configuration files validation successful");
    }

    @Test
    @DisplayName("Production vs Development Configuration")
    void testProductionConfiguration() {
        System.out.println("Testing production configuration...");

        PerformanceConfig prodConfig = PerformanceConfig.getProductionConfig();
        PerformanceConfig devConfig = PerformanceConfig.getDevelopmentConfig();

        // Production should have more conservative settings
        assertTrue(prodConfig.sparkSamplingIntervalMs >= devConfig.sparkSamplingIntervalMs,
                  "Production sampling should be same or less frequent");
        assertTrue(prodConfig.metricsCollectionInterval >= devConfig.metricsCollectionInterval,
                  "Production metrics collection should be same or less frequent");

        // Both should validate successfully
        assertTrue(prodConfig.validateSparkSettings(), "Production config should be valid");
        assertTrue(devConfig.validateSparkSettings(), "Development config should be valid");

        System.out.println("✓ Production configuration validation successful");
    }
}