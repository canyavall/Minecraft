package com.xeenaa.fabricenhancements.performance.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Benchmark validation system for ensuring reliable and repeatable measurements.
 * Provides comprehensive validation of benchmark environments, statistical significance
 * testing, and measurement reliability verification to ensure accurate performance data.
 */
public class BenchmarkValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkValidator.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Validation thresholds
    private static final double MAX_COEFFICIENT_VARIATION = 0.20; // 20% CV threshold
    private static final double MIN_STATISTICAL_POWER = 0.80; // 80% power threshold
    private static final int MIN_SAMPLE_SIZE = 30;
    private static final double MAX_SYSTEM_LOAD_THRESHOLD = 0.80; // 80% CPU usage
    private static final long MIN_AVAILABLE_MEMORY_MB = 1024; // 1GB minimum
    private static final double MAX_GC_PRESSURE_THRESHOLD = 0.10; // 10% GC time ratio

    // System monitoring
    private final MemoryMXBean memoryMXBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    private final ThreadMXBean threadMXBean;
    private final RuntimeMXBean runtimeMXBean;

    // Validation state
    private ValidationEnvironment environment;
    private final List<ValidationResult> validationHistory;
    private final Map<String, BenchmarkReliability> reliabilityScores;

    public BenchmarkValidator() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
        this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        this.validationHistory = new ArrayList<>();
        this.reliabilityScores = new HashMap<>();
    }

    /**
     * Validate benchmark environment and data quality comprehensively
     */
    public ValidationResult validateBenchmark(String benchmarkName, List<Double> measurements,
                                            Map<String, Object> benchmarkMetadata) {
        LOGGER.info("Validating benchmark: {}", benchmarkName);

        ValidationResult result = new ValidationResult(benchmarkName);

        try {
            // Step 1: Validate environment conditions
            validateEnvironment(result);

            // Step 2: Validate data quality and statistical properties
            validateDataQuality(measurements, result);

            // Step 3: Validate measurement reliability
            validateMeasurementReliability(measurements, result);

            // Step 4: Validate statistical significance
            validateStatisticalSignificance(measurements, result);

            // Step 5: Validate repeatability
            validateRepeatability(benchmarkName, measurements, result);

            // Step 6: Calculate overall validation score
            calculateOverallValidationScore(result);

            // Step 7: Store reliability metrics
            updateReliabilityScores(benchmarkName, result);

            // Step 8: Generate recommendations
            generateRecommendations(result);

            validationHistory.add(result);

            LOGGER.info("Validation completed for {}: overall score {:.2f}, valid: {}",
                       benchmarkName, result.overallScore, result.isValid);

            return result;

        } catch (Exception e) {
            LOGGER.error("Benchmark validation failed for " + benchmarkName, e);
            result.isValid = false;
            result.validationErrors.add("Validation process failed: " + e.getMessage());
            return result;
        }
    }

    /**
     * Validate the current system environment for benchmarking
     */
    private void validateEnvironment(ValidationResult result) {
        LOGGER.debug("Validating environment conditions...");

        environment = captureEnvironmentState();

        // CPU load validation
        double cpuLoad = environment.systemCpuLoad;
        if (cpuLoad > MAX_SYSTEM_LOAD_THRESHOLD) {
            result.environmentIssues.add(String.format("High CPU load: %.1f%% (threshold: %.1f%%)",
                cpuLoad * 100, MAX_SYSTEM_LOAD_THRESHOLD * 100));
            result.environmentScore -= 0.25;
        }

        // Memory validation
        long availableMemoryMB = environment.availableMemoryMB;
        if (availableMemoryMB < MIN_AVAILABLE_MEMORY_MB) {
            result.environmentIssues.add(String.format("Low available memory: %d MB (minimum: %d MB)",
                availableMemoryMB, MIN_AVAILABLE_MEMORY_MB));
            result.environmentScore -= 0.25;
        }

        // GC pressure validation
        double gcPressure = environment.gcTimeRatio;
        if (gcPressure > MAX_GC_PRESSURE_THRESHOLD) {
            result.environmentIssues.add(String.format("High GC pressure: %.1f%% (threshold: %.1f%%)",
                gcPressure * 100, MAX_GC_PRESSURE_THRESHOLD * 100));
            result.environmentScore -= 0.20;
        }

        // JVM stability validation
        if (environment.uptime < TimeUnit.MINUTES.toMillis(5)) {
            result.environmentIssues.add("JVM uptime too short (< 5 minutes) for stable benchmarking");
            result.environmentScore -= 0.10;
        }

        // Thread contention validation
        if (environment.blockedThreadCount > 0) {
            result.environmentIssues.add(String.format("Thread contention detected: %d blocked threads",
                environment.blockedThreadCount));
            result.environmentScore -= 0.15;
        }

        LOGGER.debug("Environment validation completed: score {:.2f}, issues: {}",
                    result.environmentScore, result.environmentIssues.size());
    }

    /**
     * Validate data quality and statistical properties
     */
    private void validateDataQuality(List<Double> measurements, ValidationResult result) {
        LOGGER.debug("Validating data quality...");

        if (measurements == null || measurements.isEmpty()) {
            result.dataQualityIssues.add("No measurements provided");
            result.dataQualityScore = 0.0;
            return;
        }

        // Remove invalid values
        List<Double> validMeasurements = measurements.stream()
            .filter(val -> val != null && !Double.isNaN(val) && Double.isFinite(val) && val >= 0)
            .toList();

        if (validMeasurements.size() != measurements.size()) {
            int invalidCount = measurements.size() - validMeasurements.size();
            result.dataQualityIssues.add(String.format("Removed %d invalid measurements", invalidCount));
            result.dataQualityScore -= 0.10;
        }

        if (validMeasurements.isEmpty()) {
            result.dataQualityIssues.add("No valid measurements after filtering");
            result.dataQualityScore = 0.0;
            return;
        }

        // Sample size validation
        if (validMeasurements.size() < MIN_SAMPLE_SIZE) {
            result.dataQualityIssues.add(String.format("Sample size too small: %d (minimum: %d)",
                validMeasurements.size(), MIN_SAMPLE_SIZE));
            result.dataQualityScore -= 0.30;
        }

        // Statistical analysis
        StatisticalAnalysis.StatisticalReport stats =
            StatisticalAnalysis.analyzeBenchmarkResults(validMeasurements, "validation");

        // Variability validation
        if (stats.coefficientOfVariation > MAX_COEFFICIENT_VARIATION) {
            result.dataQualityIssues.add(String.format("High variability: CV=%.3f (threshold: %.3f)",
                stats.coefficientOfVariation, MAX_COEFFICIENT_VARIATION));
            result.dataQualityScore -= 0.25;
        }

        // Outlier validation
        if (stats.outlierPercentage > 10.0) {
            result.dataQualityIssues.add(String.format("High outlier percentage: %.1f%%",
                stats.outlierPercentage));
            result.dataQualityScore -= 0.15;
        }

        // Data distribution validation
        if (!stats.isNormallyDistributed) {
            result.dataQualityIssues.add("Data is not normally distributed");
            result.dataQualityScore -= 0.10;
        }

        result.statisticalReport = stats;

        LOGGER.debug("Data quality validation completed: score {:.2f}, issues: {}",
                    result.dataQualityScore, result.dataQualityIssues.size());
    }

    /**
     * Validate measurement reliability and consistency
     */
    private void validateMeasurementReliability(List<Double> measurements, ValidationResult result) {
        LOGGER.debug("Validating measurement reliability...");

        if (measurements.size() < 10) {
            result.reliabilityIssues.add("Insufficient data for reliability analysis");
            result.reliabilityScore = 0.5;
            return;
        }

        // Split data into chunks for reliability analysis
        int chunkSize = Math.max(5, measurements.size() / 5);
        List<List<Double>> chunks = new ArrayList<>();

        for (int i = 0; i < measurements.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, measurements.size());
            chunks.add(measurements.subList(i, endIndex));
        }

        if (chunks.size() < 2) {
            result.reliabilityIssues.add("Insufficient chunks for reliability analysis");
            result.reliabilityScore = 0.5;
            return;
        }

        // Calculate means for each chunk
        List<Double> chunkMeans = chunks.stream()
            .mapToDouble(chunk -> chunk.stream().mapToDouble(Double::doubleValue).average().orElse(0.0))
            .boxed()
            .toList();

        // Calculate inter-chunk variability
        double overallMean = measurements.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double chunkVariance = chunkMeans.stream()
            .mapToDouble(mean -> Math.pow(mean - overallMean, 2))
            .average()
            .orElse(0.0);

        double chunkStdDev = Math.sqrt(chunkVariance);
        double chunkCV = overallMean > 0 ? chunkStdDev / overallMean : 0.0;

        // Reliability assessment
        if (chunkCV > 0.15) { // 15% threshold for chunk-to-chunk variation
            result.reliabilityIssues.add(String.format("High chunk-to-chunk variation: CV=%.3f", chunkCV));
            result.reliabilityScore -= 0.30;
        }

        // Trend analysis (checking for systematic drift)
        double trendSlope = calculateTrendSlope(measurements);
        double trendSignificance = Math.abs(trendSlope) / overallMean;

        if (trendSignificance > 0.05) { // 5% trend significance threshold
            result.reliabilityIssues.add(String.format("Significant measurement trend detected: %.3f%% per sample",
                trendSignificance * 100));
            result.reliabilityScore -= 0.25;
        }

        // Stability assessment (first half vs second half)
        int midpoint = measurements.size() / 2;
        List<Double> firstHalf = measurements.subList(0, midpoint);
        List<Double> secondHalf = measurements.subList(midpoint, measurements.size());

        double firstMean = firstHalf.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double secondMean = secondHalf.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stabilityDiff = Math.abs(firstMean - secondMean) / Math.max(firstMean, secondMean);

        if (stabilityDiff > 0.10) { // 10% stability threshold
            result.reliabilityIssues.add(String.format("Unstable measurements: %.1f%% difference between halves",
                stabilityDiff * 100));
            result.reliabilityScore -= 0.20;
        }

        result.reliabilityMetrics.put("chunk_cv", chunkCV);
        result.reliabilityMetrics.put("trend_significance", trendSignificance);
        result.reliabilityMetrics.put("stability_difference", stabilityDiff);

        LOGGER.debug("Reliability validation completed: score {:.2f}, issues: {}",
                    result.reliabilityScore, result.reliabilityIssues.size());
    }

    /**
     * Validate statistical significance of measurements
     */
    private void validateStatisticalSignificance(List<Double> measurements, ValidationResult result) {
        LOGGER.debug("Validating statistical significance...");

        if (result.statisticalReport == null) {
            result.significanceIssues.add("No statistical report available");
            result.significanceScore = 0.0;
            return;
        }

        StatisticalAnalysis.StatisticalReport stats = result.statisticalReport;

        // Statistical power validation
        if (stats.statisticalPower < MIN_STATISTICAL_POWER) {
            result.significanceIssues.add(String.format("Low statistical power: %.2f (minimum: %.2f)",
                stats.statisticalPower, MIN_STATISTICAL_POWER));
            result.significanceScore -= 0.30;
        }

        // Sample size adequacy
        if (!stats.isAdequateSampleSize) {
            result.significanceIssues.add(String.format("Inadequate sample size: %d (recommended: %d)",
                stats.sampleSize, stats.recommendedSampleSize));
            result.significanceScore -= 0.25;
        }

        // Confidence interval validation
        if (stats.relativeMarginOfError > 0.15) { // 15% relative margin of error threshold
            result.significanceIssues.add(String.format("Wide confidence interval: %.1f%% relative margin",
                stats.relativeMarginOfError * 100));
            result.significanceScore -= 0.20;
        }

        // Effect size validation
        if (stats.effectSize < 0.2) { // Small effect size threshold
            result.significanceIssues.add(String.format("Small effect size: %.3f", stats.effectSize));
            result.significanceScore -= 0.15;
        }

        // Overall statistical significance
        if (!stats.isStatisticallySignificant) {
            result.significanceIssues.add("Results are not statistically significant");
            result.significanceScore -= 0.40;
        }

        LOGGER.debug("Significance validation completed: score {:.2f}, issues: {}",
                    result.significanceScore, result.significanceIssues.size());
    }

    /**
     * Validate repeatability by comparing with historical data
     */
    private void validateRepeatability(String benchmarkName, List<Double> measurements, ValidationResult result) {
        LOGGER.debug("Validating repeatability...");

        BenchmarkReliability reliability = reliabilityScores.get(benchmarkName);
        if (reliability == null) {
            result.repeatabilityIssues.add("No historical data for repeatability comparison");
            result.repeatabilityScore = 0.8; // Neutral score for first run
            return;
        }

        double currentMean = measurements.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double historicalMean = reliability.averageMean;
        double expectedVariation = reliability.averageStandardDeviation;

        // Check if current mean is within expected range
        double deviationFromHistory = Math.abs(currentMean - historicalMean);
        double normalizedDeviation = expectedVariation > 0 ? deviationFromHistory / expectedVariation : 0.0;

        if (normalizedDeviation > 2.0) { // More than 2 standard deviations from historical mean
            result.repeatabilityIssues.add(String.format("Mean deviates significantly from history: %.2f std devs",
                normalizedDeviation));
            result.repeatabilityScore -= 0.30;
        }

        // Check consistency of variability
        double currentCV = result.statisticalReport.coefficientOfVariation;
        double historicalCV = reliability.averageCV;
        double cvDifference = Math.abs(currentCV - historicalCV);

        if (cvDifference > 0.05) { // 5% CV difference threshold
            result.repeatabilityIssues.add(String.format("Variability differs from history: current CV %.3f vs historical %.3f",
                currentCV, historicalCV));
            result.repeatabilityScore -= 0.20;
        }

        result.repeatabilityMetrics.put("historical_mean", historicalMean);
        result.repeatabilityMetrics.put("deviation_from_history", normalizedDeviation);
        result.repeatabilityMetrics.put("cv_difference", cvDifference);

        LOGGER.debug("Repeatability validation completed: score {:.2f}, issues: {}",
                    result.repeatabilityScore, result.repeatabilityIssues.size());
    }

    /**
     * Calculate overall validation score based on component scores
     */
    private void calculateOverallValidationScore(ValidationResult result) {
        // Weighted average of component scores
        double environmentWeight = 0.20;
        double dataQualityWeight = 0.30;
        double reliabilityWeight = 0.25;
        double significanceWeight = 0.15;
        double repeatabilityWeight = 0.10;

        result.overallScore = (result.environmentScore * environmentWeight +
                              result.dataQualityScore * dataQualityWeight +
                              result.reliabilityScore * reliabilityWeight +
                              result.significanceScore * significanceWeight +
                              result.repeatabilityScore * repeatabilityWeight);

        // Determine overall validity
        result.isValid = result.overallScore >= 0.7 && // Minimum 70% score
                        result.environmentScore >= 0.5 && // Minimum environment quality
                        result.dataQualityScore >= 0.6 && // Minimum data quality
                        result.reliabilityScore >= 0.6;   // Minimum reliability

        LOGGER.debug("Overall validation score calculated: {:.2f}, valid: {}", result.overallScore, result.isValid);
    }

    /**
     * Update reliability scores for historical tracking
     */
    private void updateReliabilityScores(String benchmarkName, ValidationResult result) {
        BenchmarkReliability reliability = reliabilityScores.computeIfAbsent(benchmarkName,
            k -> new BenchmarkReliability(benchmarkName));

        if (result.statisticalReport != null) {
            reliability.addMeasurement(
                result.statisticalReport.mean,
                result.statisticalReport.standardDeviation,
                result.statisticalReport.coefficientOfVariation,
                result.overallScore
            );
        }
    }

    /**
     * Generate recommendations for improving benchmark quality
     */
    private void generateRecommendations(ValidationResult result) {
        List<String> recommendations = new ArrayList<>();

        // Environment recommendations
        if (result.environmentScore < 0.8) {
            recommendations.add("Reduce system load before running benchmarks");
            recommendations.add("Ensure adequate free memory (> 1GB)");
            recommendations.add("Allow JVM warmup time (> 5 minutes)");
        }

        // Data quality recommendations
        if (result.dataQualityScore < 0.8) {
            if (result.statisticalReport != null && result.statisticalReport.sampleSize < MIN_SAMPLE_SIZE) {
                recommendations.add("Increase sample size to at least " + MIN_SAMPLE_SIZE);
            }
            if (result.statisticalReport != null && result.statisticalReport.coefficientOfVariation > MAX_COEFFICIENT_VARIATION) {
                recommendations.add("Reduce measurement variability through environment control");
            }
        }

        // Reliability recommendations
        if (result.reliabilityScore < 0.8) {
            recommendations.add("Implement longer warmup periods");
            recommendations.add("Use more consistent measurement intervals");
            recommendations.add("Monitor for external interference during benchmarking");
        }

        // Significance recommendations
        if (result.significanceScore < 0.8) {
            recommendations.add("Increase sample size for better statistical power");
            recommendations.add("Use more controlled experimental conditions");
        }

        result.recommendations = recommendations;
    }

    /**
     * Capture current environment state for validation
     */
    private ValidationEnvironment captureEnvironmentState() {
        ValidationEnvironment env = new ValidationEnvironment();

        // CPU and system load
        try {
            if (ManagementFactory.getOperatingSystemMXBean() instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean osBean =
                    (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                env.systemCpuLoad = osBean.getSystemCpuLoad();
                env.processCpuLoad = osBean.getProcessCpuLoad();
            }
        } catch (Exception e) {
            LOGGER.debug("Could not get CPU load information: {}", e.getMessage());
        }

        // Memory information
        env.availableMemoryMB = memoryMXBean.getHeapMemoryUsage().getMax() -
                               memoryMXBean.getHeapMemoryUsage().getUsed();
        env.availableMemoryMB /= (1024 * 1024);

        // GC information
        long totalGcTime = gcBeans.stream().mapToLong(GarbageCollectorMXBean::getCollectionTime).sum();
        env.uptime = runtimeMXBean.getUptime();
        env.gcTimeRatio = env.uptime > 0 ? (double) totalGcTime / env.uptime : 0.0;

        // Thread information
        env.threadCount = threadMXBean.getThreadCount();
        env.blockedThreadCount = 0; // Would need thread dump analysis for accurate count

        return env;
    }

    /**
     * Calculate trend slope using linear regression
     */
    private double calculateTrendSlope(List<Double> measurements) {
        int n = measurements.size();
        if (n < 2) return 0.0;

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i;
            double y = measurements.get(i);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double denominator = n * sumX2 - sumX * sumX;
        return denominator != 0 ? (n * sumXY - sumX * sumY) / denominator : 0.0;
    }

    /**
     * Save validation results to file
     */
    public void saveValidationResults(ValidationResult result) throws IOException {
        Path validationDir = Paths.get("performance/validation");
        Files.createDirectories(validationDir);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path resultFile = validationDir.resolve(String.format("validation-%s-%s.json",
            result.benchmarkName.replaceAll("[^a-zA-Z0-9]", "_"), timestamp));

        String json = GSON.toJson(result);
        Files.writeString(resultFile, json);

        LOGGER.info("Validation results saved to: {}", resultFile);
    }

    /**
     * Get validation history
     */
    public List<ValidationResult> getValidationHistory() {
        return new ArrayList<>(validationHistory);
    }

    /**
     * Get reliability scores for all benchmarks
     */
    public Map<String, BenchmarkReliability> getReliabilityScores() {
        return new HashMap<>(reliabilityScores);
    }

    // Data classes

    public static class ValidationResult {
        public final String benchmarkName;
        public final String timestamp;

        // Validation scores (0.0 to 1.0)
        public double environmentScore = 1.0;
        public double dataQualityScore = 1.0;
        public double reliabilityScore = 1.0;
        public double significanceScore = 1.0;
        public double repeatabilityScore = 1.0;
        public double overallScore = 0.0;

        // Overall validity
        public boolean isValid = false;

        // Issues found during validation
        public final List<String> environmentIssues = new ArrayList<>();
        public final List<String> dataQualityIssues = new ArrayList<>();
        public final List<String> reliabilityIssues = new ArrayList<>();
        public final List<String> significanceIssues = new ArrayList<>();
        public final List<String> repeatabilityIssues = new ArrayList<>();
        public final List<String> validationErrors = new ArrayList<>();

        // Metrics and analysis results
        public StatisticalAnalysis.StatisticalReport statisticalReport;
        public final Map<String, Object> reliabilityMetrics = new HashMap<>();
        public final Map<String, Object> repeatabilityMetrics = new HashMap<>();

        // Recommendations
        public List<String> recommendations = new ArrayList<>();

        public ValidationResult(String benchmarkName) {
            this.benchmarkName = benchmarkName;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    private static class ValidationEnvironment {
        public double systemCpuLoad = 0.0;
        public double processCpuLoad = 0.0;
        public long availableMemoryMB = 0;
        public long uptime = 0;
        public double gcTimeRatio = 0.0;
        public int threadCount = 0;
        public int blockedThreadCount = 0;
    }

    private static class BenchmarkReliability {
        public final String benchmarkName;
        public final List<Double> means = new ArrayList<>();
        public final List<Double> standardDeviations = new ArrayList<>();
        public final List<Double> cvs = new ArrayList<>();
        public final List<Double> validationScores = new ArrayList<>();

        public double averageMean = 0.0;
        public double averageStandardDeviation = 0.0;
        public double averageCV = 0.0;
        public double averageValidationScore = 0.0;
        public int measurementCount = 0;

        public BenchmarkReliability(String benchmarkName) {
            this.benchmarkName = benchmarkName;
        }

        public void addMeasurement(double mean, double stdDev, double cv, double validationScore) {
            means.add(mean);
            standardDeviations.add(stdDev);
            cvs.add(cv);
            validationScores.add(validationScore);
            measurementCount++;

            // Update averages
            averageMean = means.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageStandardDeviation = standardDeviations.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageCV = cvs.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageValidationScore = validationScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
    }
}