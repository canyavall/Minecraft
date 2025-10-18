package com.xeenaa.fabricenhancements.performance;

import com.xeenaa.fabricenhancements.performance.analysis.HardwareTierDetector;
import com.xeenaa.fabricenhancements.performance.analysis.StatisticalAnalysis;
import com.xeenaa.fabricenhancements.performance.analysis.BenchmarkValidator;
import com.xeenaa.fabricenhancements.performance.analysis.RegressionDetector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comprehensive integration tests for the enhanced benchmarking framework.
 * Tests the complete end-to-end functionality including hardware tier detection,
 * baseline collection, statistical analysis, benchmark validation, and regression detection.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Benchmarking Framework Integration Tests")
public class BenchmarkingFrameworkIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingFrameworkIntegrationTest.class);

    private HardwareTierDetector hardwareDetector;
    private BaselineCollector baselineCollector;
    private BenchmarkValidator benchmarkValidator;
    private RegressionDetector regressionDetector;

    // Test data
    private List<Double> sampleMeasurements;
    private List<Double> baselineMeasurements;
    private Map<String, Object> testMetadata;

    @BeforeEach
    void setUp() {
        LOGGER.info("Setting up benchmarking framework integration test");

        // Initialize framework components
        hardwareDetector = new HardwareTierDetector();
        baselineCollector = new BaselineCollector();
        benchmarkValidator = new BenchmarkValidator();
        regressionDetector = new RegressionDetector();

        // Generate test data
        generateTestMeasurements();

        // Create test metadata
        testMetadata = new HashMap<>();
        testMetadata.put("test_name", "integration_test");
        testMetadata.put("environment", "junit");
        testMetadata.put("version", "1.0.0");
    }

    @AfterEach
    void tearDown() {
        LOGGER.info("Cleaning up benchmarking framework integration test");
        // Cleanup is handled by individual components
    }

    @Test
    @Order(1)
    @DisplayName("Hardware Tier Detection Integration")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testHardwareTierDetection() {
        LOGGER.info("Testing hardware tier detection integration");

        // Act
        HardwareTierDetector.HardwareTier tier = hardwareDetector.detectHardwareTier();

        // Assert
        assertThat(tier).isNotNull();
        assertThat(tier.name).isNotBlank();
        assertThat(tier.overallTier).isNotNull();
        assertThat(tier.profile).isNotNull();
        assertThat(tier.targets).isNotNull();

        // Validate tier components
        assertThat(tier.cpuTier).isNotNull();
        assertThat(tier.memoryTier).isNotNull();
        assertThat(tier.ioTier).isNotNull();

        // Validate performance targets
        assertThat(tier.targets.chunkLoadingImprovementTarget).isBetween(0.0, 1.0);
        assertThat(tier.targets.memoryReductionTarget).isBetween(0.0, 1.0);
        assertThat(tier.targets.expectedTPS).isPositive();
        assertThat(tier.targets.expectedRenderDistance).isPositive();

        // Validate profile data
        assertThat(tier.profile.availableProcessors).isPositive();
        assertThat(tier.profile.cpuScore).isPositive();
        assertThat(tier.profile.ioScore).isPositive();
        assertThat(tier.profile.maxHeapMemoryMB).isPositive();

        LOGGER.info("Hardware tier detection successful: {} ({} CPU, {} Memory, {} I/O)",
                   tier.name, tier.cpuTier.displayName, tier.memoryTier.displayName, tier.ioTier.displayName);
    }

    @Test
    @Order(2)
    @DisplayName("Baseline Collection Integration")
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testBaselineCollection() {
        LOGGER.info("Testing baseline collection integration");

        // Act
        BaselineCollector.EnhancedBaselineMetrics baseline = baselineCollector.collectBaseline();

        // Assert
        assertThat(baseline).isNotNull();
        assertThat(baseline.timestamp).isNotBlank();
        assertThat(baseline.hardwareTier).isNotNull();

        // Validate system information
        assertThat(baseline.systemInfo).isNotEmpty();
        assertThat(baseline.systemInfo).containsKeys(
            "java_version", "os_name", "available_processors",
            "hardware_tier", "target_chunk_improvement", "target_memory_reduction"
        );

        // Validate metrics collections
        assertThat(baseline.memoryMetrics).isNotEmpty();
        assertThat(baseline.chunkMetrics).isNotEmpty();
        assertThat(baseline.tpsMetrics).isNotEmpty();

        // Validate statistical analysis was performed
        assertThat(baseline.statisticalAnalysis).isNotNull();
        assertThat(baseline.targetValidation).isNotNull();

        // Validate memory metrics
        assertThat(baseline.memoryMetrics).containsKeys(
            "allocation_samples", "avg_allocation_time_ms", "allocation_std_dev_ms"
        );

        // Validate chunk metrics
        assertThat(baseline.chunkMetrics).containsKeys(
            "single_threaded_samples", "multi_threaded_samples", "cache_hit_ratio"
        );

        // Validate TPS metrics
        assertThat(baseline.tpsMetrics).containsKeys(
            "single_threaded_avg_tps", "multi_threaded_avg_tps", "target_tps"
        );

        LOGGER.info("Baseline collection successful: {} metrics collected for {} tier",
                   baseline.systemInfo.size() + baseline.memoryMetrics.size() +
                   baseline.chunkMetrics.size() + baseline.tpsMetrics.size(),
                   baseline.hardwareTier.name);
    }

    @Test
    @Order(3)
    @DisplayName("Statistical Analysis Integration")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStatisticalAnalysis() {
        LOGGER.info("Testing statistical analysis integration");

        // Act
        StatisticalAnalysis.StatisticalReport report =
            StatisticalAnalysis.analyzeBenchmarkResults(sampleMeasurements, "integration_test");

        // Assert
        assertThat(report).isNotNull();
        assertThat(report.benchmarkName).isEqualTo("integration_test");
        assertThat(report.timestamp).isNotBlank();

        // Validate descriptive statistics
        assertThat(report.sampleSize).isEqualTo(sampleMeasurements.size());
        assertThat(report.mean).isPositive();
        assertThat(report.standardDeviation).isNotNegative();
        assertThat(report.coefficientOfVariation).isNotNegative();

        // Validate percentiles
        assertThat(report.median).isPositive();
        assertThat(report.q1).isPositive();
        assertThat(report.q3).isPositive();
        assertThat(report.q1).isLessThanOrEqualTo(report.median);
        assertThat(report.median).isLessThanOrEqualTo(report.q3);

        // Validate statistical properties
        assertThat(report.skewness).isFinite();
        assertThat(report.kurtosis).isFinite();

        // Validate confidence intervals
        assertThat(report.confidenceLevel).isBetween(0.0, 1.0);
        assertThat(report.confidenceIntervalLower).isLessThanOrEqualTo(report.mean);
        assertThat(report.confidenceIntervalUpper).isGreaterThanOrEqualTo(report.mean);

        // Validate data quality assessment
        assertThat(report.dataQuality).isNotNull();
        assertThat(report.outlierPercentage).isBetween(0.0, 100.0);

        LOGGER.info("Statistical analysis successful: mean={:.3f}, cv={:.3f}, quality={}",
                   report.mean, report.coefficientOfVariation, report.dataQuality.name());
    }

    @Test
    @Order(4)
    @DisplayName("Benchmark Validation Integration")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testBenchmarkValidation() {
        LOGGER.info("Testing benchmark validation integration");

        // Act
        BenchmarkValidator.ValidationResult result =
            benchmarkValidator.validateBenchmark("integration_test", sampleMeasurements, testMetadata);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.benchmarkName).isEqualTo("integration_test");
        assertThat(result.timestamp).isNotBlank();

        // Validate component scores
        assertThat(result.environmentScore).isBetween(0.0, 1.0);
        assertThat(result.dataQualityScore).isBetween(0.0, 1.0);
        assertThat(result.reliabilityScore).isBetween(0.0, 1.0);
        assertThat(result.significanceScore).isBetween(0.0, 1.0);
        assertThat(result.repeatabilityScore).isBetween(0.0, 1.0);
        assertThat(result.overallScore).isBetween(0.0, 1.0);

        // Validate statistical report
        assertThat(result.statisticalReport).isNotNull();

        // Validate recommendations
        assertThat(result.recommendations).isNotNull();

        // Validate metrics
        assertThat(result.reliabilityMetrics).isNotNull();
        assertThat(result.repeatabilityMetrics).isNotNull();

        LOGGER.info("Benchmark validation successful: overall score={:.2f}, valid={}",
                   result.overallScore, result.isValid);
    }

    @Test
    @Order(5)
    @DisplayName("Regression Detection Integration")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRegressionDetection() {
        LOGGER.info("Testing regression detection integration");

        // First, establish baseline
        RegressionDetector.RegressionReport baselineReport =
            regressionDetector.detectRegressions("integration_test_baseline", baselineMeasurements, testMetadata);

        // Then test with current measurements (slight regression)
        List<Double> regressedMeasurements = sampleMeasurements.stream()
            .map(val -> val * 1.08) // 8% performance degradation
            .toList();

        // Act
        RegressionDetector.RegressionReport report =
            regressionDetector.detectRegressions("integration_test_baseline", regressedMeasurements, testMetadata);

        // Assert
        assertThat(report).isNotNull();
        assertThat(report.benchmarkName).isEqualTo("integration_test_baseline");
        assertThat(report.timestamp).isNotBlank();

        // Validate regression analysis
        assertThat(report.percentChange).isPositive(); // Should detect degradation
        assertThat(report.absoluteChange).isPositive();

        // Validate statistical properties
        assertThat(report.currentStatistics).isNotNull();
        assertThat(report.baseline).isNotNull();
        assertThat(report.effectSize).isFinite();
        assertThat(report.effectSizeMagnitude).isNotBlank();

        // Validate statistical tests
        assertThat(report.tStatistic).isFinite();
        assertThat(report.pValue).isBetween(0.0, 1.0);
        assertThat(report.statisticalPower).isBetween(0.0, 1.0);

        // Validate trend analysis
        assertThat(report.trendDirection).isNotBlank();
        assertThat(report.trendSlope).isFinite();
        assertThat(report.trendSignificance).isNotNegative();

        // Validate recommendations
        assertThat(report.recommendations).isNotNull();

        LOGGER.info("Regression detection successful: change={:.2f}%, regression={}, improvement={}",
                   report.percentChange, report.isRegression, report.isImprovement);
    }

    @Test
    @Order(6)
    @DisplayName("End-to-End Framework Integration")
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void testEndToEndFrameworkIntegration() {
        LOGGER.info("Testing end-to-end framework integration");

        // Step 1: Hardware tier detection
        HardwareTierDetector.HardwareTier tier = hardwareDetector.detectHardwareTier();
        assertThat(tier).isNotNull();

        // Step 2: Baseline collection with hardware context
        BaselineCollector.EnhancedBaselineMetrics baseline = baselineCollector.collectBaseline();
        assertThat(baseline).isNotNull();
        assertThat(baseline.hardwareTier).isNotNull();

        // Step 3: Statistical analysis of baseline data
        Map<String, StatisticalAnalysis.StatisticalReport> reports = baselineCollector.getStatisticalReports();
        assertThat(reports).isNotEmpty();

        // Step 4: Validate baseline measurements
        for (Map.Entry<String, StatisticalAnalysis.StatisticalReport> entry : reports.entrySet()) {
            StatisticalAnalysis.StatisticalReport report = entry.getValue();
            List<Double> measurements = generateSampleData(report.mean, report.standardDeviation, 50);

            BenchmarkValidator.ValidationResult validation =
                benchmarkValidator.validateBenchmark(entry.getKey(), measurements, testMetadata);

            assertThat(validation).isNotNull();
            assertThat(validation.overallScore).isGreaterThan(0.0);
        }

        // Step 5: Regression detection
        RegressionDetector.RegressionReport regressionReport =
            regressionDetector.detectRegressions("end_to_end_test", sampleMeasurements, testMetadata);
        assertThat(regressionReport).isNotNull();

        // Step 6: Verify data persistence and retrieval
        Map<String, RegressionDetector.RegressionSummary> summary = regressionDetector.getRegressionSummary();
        assertThat(summary).isNotEmpty();

        // Validate integration consistency
        assertThat(tier.targets.chunkLoadingImprovementTarget).isPositive();
        assertThat(baseline.targetValidation).isNotNull();

        LOGGER.info("End-to-end framework integration successful: {} tier, {} baseline metrics, {} reports",
                   tier.name, baseline.systemInfo.size() + baseline.memoryMetrics.size() +
                   baseline.chunkMetrics.size() + baseline.tpsMetrics.size(), reports.size());
    }

    @Test
    @DisplayName("Performance Target Validation")
    void testPerformanceTargetValidation() {
        LOGGER.info("Testing performance target validation");

        // Create performance data that should meet targets
        List<Double> optimizedMeasurements = generateOptimizedMeasurements();

        // Analyze with statistical framework
        StatisticalAnalysis.StatisticalReport report =
            StatisticalAnalysis.analyzeBenchmarkResults(optimizedMeasurements, "target_validation_test");

        // Validate statistical quality
        assertThat(report.isStatisticallySignificant).isTrue();
        assertThat(report.coefficientOfVariation).isLessThan(0.20); // < 20% CV target
        assertThat(report.statisticalPower).isGreaterThan(0.80); // > 80% power target

        // Test performance targets
        assertThat(report.meetsChunkLoadingTarget || report.meetsMemoryTarget).isTrue();

        LOGGER.info("Performance target validation successful: CV={:.3f}, power={:.3f}",
                   report.coefficientOfVariation, report.statisticalPower);
    }

    @Test
    @DisplayName("Framework Resilience and Error Handling")
    void testFrameworkResilience() {
        LOGGER.info("Testing framework resilience and error handling");

        // Test with invalid/edge case data
        List<Double> invalidData = List.of(Double.NaN, Double.POSITIVE_INFINITY, -1.0, null);
        List<Double> emptyData = new ArrayList<>();
        List<Double> singleValue = List.of(42.0);

        // Statistical analysis should handle invalid data gracefully
        assertThrows(IllegalArgumentException.class, () ->
            StatisticalAnalysis.analyzeBenchmarkResults(null, "test"));

        assertThrows(IllegalArgumentException.class, () ->
            StatisticalAnalysis.analyzeBenchmarkResults(emptyData, "test"));

        // Benchmark validation should handle edge cases
        BenchmarkValidator.ValidationResult result1 =
            benchmarkValidator.validateBenchmark("resilience_test", singleValue, testMetadata);
        assertThat(result1).isNotNull();
        assertThat(result1.isValid).isFalse(); // Should fail due to insufficient data

        // Regression detection should handle insufficient data
        RegressionDetector.RegressionReport result2 =
            regressionDetector.detectRegressions("resilience_test", singleValue, testMetadata);
        assertThat(result2).isNotNull();
        assertThat(result2.detectionErrors).isNotEmpty();

        LOGGER.info("Framework resilience testing successful: proper error handling verified");
    }

    @Test
    @EnabledIf("java.lang.System#getProperty('performance.integration.file-system') != null")
    @DisplayName("File System Integration")
    void testFileSystemIntegration() throws Exception {
        LOGGER.info("Testing file system integration");

        // Test baseline persistence
        BaselineCollector.EnhancedBaselineMetrics baseline = baselineCollector.collectBaseline();

        // Check if baseline files were created
        Path baselineDir = Paths.get("performance/baselines");
        if (Files.exists(baselineDir)) {
            assertThat(Files.list(baselineDir)).isNotEmpty();
        }

        // Test regression data persistence
        regressionDetector.detectRegressions("file_system_test", sampleMeasurements, testMetadata);

        Path regressionDir = Paths.get("performance/regression-data");
        if (Files.exists(regressionDir)) {
            assertThat(Files.exists(regressionDir.resolve("historical-measurements.json")) ||
                      Files.exists(regressionDir.resolve("baseline-statistics.json"))).isTrue();
        }

        LOGGER.info("File system integration successful: data persistence verified");
    }

    @Test
    @DisplayName("Concurrent Framework Operations")
    void testConcurrentFrameworkOperations() throws Exception {
        LOGGER.info("Testing concurrent framework operations");

        // Test concurrent statistical analysis
        List<java.util.concurrent.CompletableFuture<StatisticalAnalysis.StatisticalReport>> futures =
            new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final int index = i;
            java.util.concurrent.CompletableFuture<StatisticalAnalysis.StatisticalReport> future =
                java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                    List<Double> data = generateSampleData(100.0 + index * 10, 15.0, 100);
                    return StatisticalAnalysis.analyzeBenchmarkResults(data, "concurrent_test_" + index);
                });
            futures.add(future);
        }

        // Wait for all to complete
        java.util.concurrent.CompletableFuture.allOf(futures.toArray(new java.util.concurrent.CompletableFuture[0]))
            .get(30, TimeUnit.SECONDS);

        // Validate all completed successfully
        for (java.util.concurrent.CompletableFuture<StatisticalAnalysis.StatisticalReport> future : futures) {
            StatisticalAnalysis.StatisticalReport report = future.get();
            assertThat(report).isNotNull();
            assertThat(report.isStatisticallySignificant).isTrue();
        }

        LOGGER.info("Concurrent framework operations successful: {} analyses completed", futures.size());
    }

    // Helper methods

    private void generateTestMeasurements() {
        // Generate realistic sample measurements with some variability
        sampleMeasurements = generateSampleData(100.0, 15.0, 100);

        // Generate baseline measurements (slightly different for comparison)
        baselineMeasurements = generateSampleData(95.0, 12.0, 100);
    }

    private List<Double> generateSampleData(double mean, double stdDev, int size) {
        List<Double> data = new ArrayList<>();
        java.util.Random random = new java.util.Random(12345); // Fixed seed for reproducibility

        for (int i = 0; i < size; i++) {
            // Generate normally distributed data
            double value = mean + stdDev * random.nextGaussian();
            // Ensure positive values (representing time measurements)
            data.add(Math.max(value, 1.0));
        }

        return data;
    }

    private List<Double> generateOptimizedMeasurements() {
        // Generate measurements that should meet performance targets
        // Lower mean (better performance) with low variability
        return generateSampleData(75.0, 8.0, 150); // 25% improvement with good consistency
    }
}