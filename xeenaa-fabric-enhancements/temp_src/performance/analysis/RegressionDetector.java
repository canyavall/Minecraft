package com.xeenaa.fabricenhancements.performance.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Automated performance regression detection system.
 * Provides comprehensive regression analysis by comparing current performance measurements
 * with historical baselines, using statistical significance testing and trend analysis
 * to identify performance regressions and improvements with high confidence.
 */
public class RegressionDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegressionDetector.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Regression detection thresholds
    private static final double DEFAULT_REGRESSION_THRESHOLD = 0.05; // 5% performance degradation
    private static final double DEFAULT_IMPROVEMENT_THRESHOLD = 0.05; // 5% performance improvement
    private static final double STATISTICAL_SIGNIFICANCE_THRESHOLD = 0.05; // 5% p-value
    private static final int MIN_HISTORICAL_SAMPLES = 5;
    private static final int MAX_HISTORICAL_SAMPLES = 50;

    // Analysis configuration
    private final double regressionThreshold;
    private final double improvementThreshold;
    private final double significanceThreshold;

    // Historical data management
    private final Map<String, List<PerformanceMeasurement>> historicalData;
    private final Map<String, BaselineStatistics> baselineStatistics;
    private final List<RegressionReport> regressionHistory;

    public RegressionDetector() {
        this(DEFAULT_REGRESSION_THRESHOLD, DEFAULT_IMPROVEMENT_THRESHOLD, STATISTICAL_SIGNIFICANCE_THRESHOLD);
    }

    public RegressionDetector(double regressionThreshold, double improvementThreshold, double significanceThreshold) {
        this.regressionThreshold = regressionThreshold;
        this.improvementThreshold = improvementThreshold;
        this.significanceThreshold = significanceThreshold;
        this.historicalData = new HashMap<>();
        this.baselineStatistics = new HashMap<>();
        this.regressionHistory = new ArrayList<>();

        // Load existing historical data
        loadHistoricalData();
    }

    /**
     * Detect regressions by comparing current measurements with historical baselines
     */
    public RegressionReport detectRegressions(String benchmarkName, List<Double> currentMeasurements,
                                            Map<String, Object> metadata) {
        LOGGER.info(\"Detecting regressions for benchmark: {}\", benchmarkName);

        RegressionReport report = new RegressionReport(benchmarkName, metadata);

        try {
            // Step 1: Validate input data
            if (!validateInputData(currentMeasurements, report)) {
                return report;
            }

            // Step 2: Get historical baseline
            BaselineStatistics baseline = getOrCreateBaseline(benchmarkName, currentMeasurements);
            report.baseline = baseline;

            // Step 3: Perform statistical analysis on current data
            StatisticalAnalysis.StatisticalReport currentStats =
                StatisticalAnalysis.analyzeBenchmarkResults(currentMeasurements, benchmarkName + \"_current\");
            report.currentStatistics = currentStats;

            // Step 4: Compare with baseline using statistical tests
            performRegressionAnalysis(baseline, currentStats, report);

            // Step 5: Perform trend analysis
            performTrendAnalysis(benchmarkName, currentMeasurements, report);

            // Step 6: Validate statistical significance
            validateStatisticalSignificance(baseline, currentStats, report);

            // Step 7: Generate actionable recommendations
            generateRecommendations(report);

            // Step 8: Update historical data
            updateHistoricalData(benchmarkName, currentMeasurements, currentStats);

            // Step 9: Save report
            saveRegressionReport(report);

            regressionHistory.add(report);

            LOGGER.info(\"Regression detection completed for {}: regression={}, improvement={}, significant={}\",
                       benchmarkName, report.isRegression, report.isImprovement, report.isStatisticallySignificant);

            return report;

        } catch (Exception e) {
            LOGGER.error(\"Regression detection failed for \" + benchmarkName, e);
            report.detectionErrors.add(\"Regression detection failed: \" + e.getMessage());
            return report;
        }
    }

    /**
     * Validate input data quality
     */
    private boolean validateInputData(List<Double> measurements, RegressionReport report) {
        if (measurements == null || measurements.isEmpty()) {
            report.detectionErrors.add(\"No measurements provided\");
            return false;
        }

        // Filter out invalid measurements
        List<Double> validMeasurements = measurements.stream()
            .filter(val -> val != null && !Double.isNaN(val) && Double.isFinite(val) && val >= 0)
            .toList();

        if (validMeasurements.size() != measurements.size()) {
            int invalidCount = measurements.size() - validMeasurements.size();
            report.validationWarnings.add(String.format(\"Removed %d invalid measurements\", invalidCount));
        }

        if (validMeasurements.size() < 3) {
            report.detectionErrors.add(\"Insufficient valid measurements for regression analysis\");
            return false;
        }

        return true;
    }

    /**
     * Get existing baseline or create new one from current measurements
     */
    private BaselineStatistics getOrCreateBaseline(String benchmarkName, List<Double> currentMeasurements) {
        BaselineStatistics baseline = baselineStatistics.get(benchmarkName);

        if (baseline == null) {
            // Create initial baseline from current measurements
            baseline = createInitialBaseline(benchmarkName, currentMeasurements);
            baselineStatistics.put(benchmarkName, baseline);
            LOGGER.info(\"Created initial baseline for benchmark: {}\", benchmarkName);
        } else {
            LOGGER.debug(\"Using existing baseline for benchmark: {} (samples: {})\",
                        benchmarkName, baseline.sampleCount);
        }

        return baseline;
    }

    /**
     * Create initial baseline statistics from first measurements
     */
    private BaselineStatistics createInitialBaseline(String benchmarkName, List<Double> measurements) {
        StatisticalAnalysis.StatisticalReport stats =
            StatisticalAnalysis.analyzeBenchmarkResults(measurements, benchmarkName + \"_baseline\");

        BaselineStatistics baseline = new BaselineStatistics();
        baseline.benchmarkName = benchmarkName;
        baseline.mean = stats.mean;
        baseline.standardDeviation = stats.standardDeviation;
        baseline.coefficientOfVariation = stats.coefficientOfVariation;
        baseline.sampleCount = stats.sampleSize;
        baseline.lastUpdated = LocalDateTime.now();
        baseline.measurements = new ArrayList<>(measurements);

        return baseline;
    }

    /**
     * Perform comprehensive regression analysis
     */
    private void performRegressionAnalysis(BaselineStatistics baseline,
                                         StatisticalAnalysis.StatisticalReport currentStats,
                                         RegressionReport report) {
        // Calculate performance change
        double percentChange = ((currentStats.mean - baseline.mean) / baseline.mean) * 100;
        double absoluteChange = currentStats.mean - baseline.mean;

        report.percentChange = percentChange;
        report.absoluteChange = absoluteChange;

        // Determine regression/improvement status
        report.isRegression = percentChange > (regressionThreshold * 100);
        report.isImprovement = percentChange < -(improvementThreshold * 100);

        // Calculate effect size (Cohen's d)
        double pooledStdDev = Math.sqrt(((baseline.sampleCount - 1) * Math.pow(baseline.standardDeviation, 2) +
                                        (currentStats.sampleSize - 1) * Math.pow(currentStats.standardDeviation, 2)) /
                                       (baseline.sampleCount + currentStats.sampleSize - 2));

        report.effectSize = pooledStdDev > 0 ? absoluteChange / pooledStdDev : 0.0;

        // Classify effect size magnitude
        if (Math.abs(report.effectSize) < 0.2) {
            report.effectSizeMagnitude = \"Small\";
        } else if (Math.abs(report.effectSize) < 0.5) {
            report.effectSizeMagnitude = \"Medium\";
        } else {
            report.effectSizeMagnitude = \"Large\";
        }

        LOGGER.debug(\"Regression analysis: change={:.2f}%, effect_size={:.3f} ({})\",
                    percentChange, report.effectSize, report.effectSizeMagnitude);
    }

    /**
     * Perform trend analysis using historical data
     */
    private void performTrendAnalysis(String benchmarkName, List<Double> currentMeasurements,
                                    RegressionReport report) {
        List<PerformanceMeasurement> history = historicalData.get(benchmarkName);

        if (history == null || history.size() < 3) {
            report.trendWarnings.add(\"Insufficient historical data for trend analysis\");
            return;
        }

        // Sort by timestamp and get recent measurements
        List<PerformanceMeasurement> recentHistory = history.stream()
            .sorted(Comparator.comparing(m -> m.timestamp))
            .collect(Collectors.toList());

        int lookbackPeriod = Math.min(10, recentHistory.size());
        List<PerformanceMeasurement> trendData = recentHistory.subList(
            Math.max(0, recentHistory.size() - lookbackPeriod), recentHistory.size());

        // Calculate trend using linear regression
        double[] timePoints = new double[trendData.size()];
        double[] means = new double[trendData.size()];

        for (int i = 0; i < trendData.size(); i++) {
            timePoints[i] = i;
            means[i] = trendData.get(i).mean;
        }

        double trendSlope = calculateLinearRegressionSlope(timePoints, means);
        report.trendSlope = trendSlope;

        // Classify trend
        double trendThreshold = baseline.mean * 0.01; // 1% of baseline mean
        if (Math.abs(trendSlope) < trendThreshold) {
            report.trendDirection = \"Stable\";
        } else if (trendSlope > 0) {
            report.trendDirection = \"Degrading\";
        } else {
            report.trendDirection = \"Improving\";
        }

        // Calculate trend significance
        double trendSignificance = Math.abs(trendSlope) / baseline.mean;
        report.trendSignificance = trendSignificance;

        if (trendSignificance > 0.02) { // 2% trend significance
            if (trendSlope > 0) {
                report.trendWarnings.add(String.format(\"Degrading trend detected: %.2f%% per measurement\",
                    trendSignificance * 100));
            } else {
                report.trendWarnings.add(String.format(\"Improving trend detected: %.2f%% per measurement\",
                    trendSignificance * 100));
            }
        }

        LOGGER.debug(\"Trend analysis: direction={}, slope={:.3f}, significance={:.3f}\",
                    report.trendDirection, trendSlope, trendSignificance);
    }

    /**
     * Validate statistical significance using multiple tests
     */
    private void validateStatisticalSignificance(BaselineStatistics baseline,
                                                StatisticalAnalysis.StatisticalReport currentStats,
                                                RegressionReport report) {
        // Welch's t-test for unequal variances
        double tStatistic = calculateWelchTTest(baseline, currentStats);
        report.tStatistic = tStatistic;

        // Approximate p-value (simplified calculation)
        double pValue = calculateApproximatePValue(Math.abs(tStatistic), currentStats.sampleSize + baseline.sampleCount - 2);
        report.pValue = pValue;

        // Statistical significance determination
        report.isStatisticallySignificant = pValue < significanceThreshold;

        // Confidence interval for the difference
        double standardError = Math.sqrt(
            Math.pow(baseline.standardDeviation, 2) / baseline.sampleCount +
            Math.pow(currentStats.standardDeviation, 2) / currentStats.sampleSize
        );

        double criticalValue = 1.96; // Approximate for 95% confidence
        double marginOfError = criticalValue * standardError;

        report.confidenceIntervalLower = report.absoluteChange - marginOfError;
        report.confidenceIntervalUpper = report.absoluteChange + marginOfError;

        // Power analysis
        report.statisticalPower = calculateStatisticalPower(report.effectSize, currentStats.sampleSize, significanceThreshold);

        LOGGER.debug(\"Statistical significance: t={:.3f}, p={:.4f}, significant={}, power={:.3f}\",
                    tStatistic, pValue, report.isStatisticallySignificant, report.statisticalPower);
    }

    /**
     * Generate actionable recommendations based on analysis
     */
    private void generateRecommendations(RegressionReport report) {
        List<String> recommendations = new ArrayList<>();

        // Regression-specific recommendations
        if (report.isRegression && report.isStatisticallySignificant) {
            recommendations.add(\"Performance regression detected - investigate recent changes\");
            recommendations.add(\"Profile the application to identify performance hotspots\");

            if (report.effectSize > 0.8) {
                recommendations.add(\"Large effect size detected - this is a significant regression requiring immediate attention\");
            }

            if (report.trendDirection.equals(\"Degrading\")) {
                recommendations.add(\"Degrading trend observed - monitor closely for continued deterioration\");
            }
        }

        // Improvement-specific recommendations
        if (report.isImprovement && report.isStatisticallySignificant) {
            recommendations.add(\"Performance improvement detected - document and validate changes\");
            recommendations.add(\"Consider if improvement trades off other metrics (memory, stability)\");

            if (report.effectSize < -0.8) {
                recommendations.add(\"Large performance improvement - verify accuracy and sustainability\");
            }
        }

        // Statistical validity recommendations
        if (!report.isStatisticallySignificant && (report.isRegression || report.isImprovement)) {
            recommendations.add(\"Changes detected but not statistically significant - collect more data\");
            recommendations.add(\"Consider increasing sample size for better statistical power\");
        }

        if (report.statisticalPower < 0.8) {
            recommendations.add(String.format(\"Low statistical power (%.2f) - increase sample size for reliable detection\",
                report.statisticalPower));
        }

        // Variability recommendations
        if (report.currentStatistics.coefficientOfVariation > 0.15) {
            recommendations.add(\"High measurement variability - investigate environmental factors\");
            recommendations.add(\"Consider longer warmup periods or more controlled test conditions\");
        }

        // Trend recommendations
        if (report.trendSignificance > 0.05) {
            recommendations.add(\"Significant trend detected - monitor for systematic changes\");
        }

        report.recommendations = recommendations;
    }

    /**
     * Update historical data with new measurements
     */
    private void updateHistoricalData(String benchmarkName, List<Double> measurements,
                                    StatisticalAnalysis.StatisticalReport stats) {
        PerformanceMeasurement measurement = new PerformanceMeasurement();
        measurement.benchmarkName = benchmarkName;
        measurement.timestamp = LocalDateTime.now();
        measurement.mean = stats.mean;
        measurement.standardDeviation = stats.standardDeviation;
        measurement.coefficientOfVariation = stats.coefficientOfVariation;
        measurement.sampleSize = stats.sampleSize;
        measurement.measurements = new ArrayList<>(measurements);

        // Add to historical data
        historicalData.computeIfAbsent(benchmarkName, k -> new ArrayList<>()).add(measurement);

        // Limit historical data size
        List<PerformanceMeasurement> history = historicalData.get(benchmarkName);
        if (history.size() > MAX_HISTORICAL_SAMPLES) {
            history.removeIf(m -> history.indexOf(m) < history.size() - MAX_HISTORICAL_SAMPLES);
        }

        // Update baseline statistics with exponential moving average
        BaselineStatistics baseline = baselineStatistics.get(benchmarkName);
        if (baseline != null && history.size() >= MIN_HISTORICAL_SAMPLES) {
            updateBaselineStatistics(baseline, stats);
        }

        // Save updated historical data
        saveHistoricalData();
    }

    /**
     * Update baseline statistics using exponential moving average
     */
    private void updateBaselineStatistics(BaselineStatistics baseline, StatisticalAnalysis.StatisticalReport newStats) {
        double alpha = 0.1; // Smoothing factor for exponential moving average

        baseline.mean = (1 - alpha) * baseline.mean + alpha * newStats.mean;
        baseline.standardDeviation = (1 - alpha) * baseline.standardDeviation + alpha * newStats.standardDeviation;
        baseline.coefficientOfVariation = (1 - alpha) * baseline.coefficientOfVariation + alpha * newStats.coefficientOfVariation;
        baseline.sampleCount += newStats.sampleSize;
        baseline.lastUpdated = LocalDateTime.now();
    }

    /**
     * Calculate Welch's t-test statistic for unequal variances
     */
    private double calculateWelchTTest(BaselineStatistics baseline, StatisticalAnalysis.StatisticalReport current) {
        double mean1 = baseline.mean;
        double mean2 = current.mean;
        double var1 = Math.pow(baseline.standardDeviation, 2);
        double var2 = Math.pow(current.standardDeviation, 2);
        double n1 = baseline.sampleCount;
        double n2 = current.sampleSize;

        double denominator = Math.sqrt(var1 / n1 + var2 / n2);
        return denominator != 0 ? (mean2 - mean1) / denominator : 0.0;
    }

    /**
     * Calculate approximate p-value using t-distribution approximation
     */
    private double calculateApproximatePValue(double tStatistic, double degreesOfFreedom) {
        // Simplified p-value calculation (would use proper statistical library in production)
        if (degreesOfFreedom > 30) {
            // Use normal approximation for large samples
            return 2 * (1 - approximateNormalCDF(tStatistic));
        } else {
            // Conservative estimate for small samples
            if (tStatistic > 2.0) return 0.05;
            if (tStatistic > 1.96) return 0.1;
            if (tStatistic > 1.645) return 0.2;
            return 0.5;
        }
    }

    /**
     * Approximate normal CDF (cumulative distribution function)
     */
    private double approximateNormalCDF(double x) {
        // Simplified normal CDF approximation
        return 0.5 + 0.5 * Math.tanh(x * 0.7);
    }

    /**
     * Calculate statistical power for detecting performance changes
     */
    private double calculateStatisticalPower(double effectSize, int sampleSize, double alpha) {
        // Simplified power calculation
        double z_alpha = 1.96; // For 95% confidence
        double z_beta = Math.abs(effectSize) * Math.sqrt(sampleSize / 2.0) - z_alpha;

        return approximateNormalCDF(z_beta);
    }

    /**
     * Calculate linear regression slope
     */
    private double calculateLinearRegressionSlope(double[] x, double[] y) {
        int n = x.length;
        if (n != y.length || n < 2) return 0.0;

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
        }

        double denominator = n * sumX2 - sumX * sumX;
        return denominator != 0 ? (n * sumXY - sumX * sumY) / denominator : 0.0;
    }

    /**
     * Load historical data from files
     */
    private void loadHistoricalData() {
        try {
            Path dataDir = Paths.get(\"performance/regression-data\");
            if (!Files.exists(dataDir)) {
                return;
            }

            // Load historical measurements
            Path measurementsFile = dataDir.resolve(\"historical-measurements.json\");
            if (Files.exists(measurementsFile)) {
                String json = Files.readString(measurementsFile);
                Map<String, List<PerformanceMeasurement>> data = GSON.fromJson(json,
                    new TypeToken<Map<String, List<PerformanceMeasurement>>>(){}.getType());
                if (data != null) {
                    historicalData.putAll(data);
                }
            }

            // Load baseline statistics
            Path baselinesFile = dataDir.resolve(\"baseline-statistics.json\");
            if (Files.exists(baselinesFile)) {
                String json = Files.readString(baselinesFile);
                Map<String, BaselineStatistics> data = GSON.fromJson(json,
                    new TypeToken<Map<String, BaselineStatistics>>(){}.getType());
                if (data != null) {
                    baselineStatistics.putAll(data);
                }
            }

            LOGGER.info(\"Loaded historical data for {} benchmarks\", historicalData.size());

        } catch (Exception e) {
            LOGGER.warn(\"Failed to load historical regression data\", e);
        }
    }

    /**
     * Save historical data to files
     */
    private void saveHistoricalData() {
        try {
            Path dataDir = Paths.get(\"performance/regression-data\");
            Files.createDirectories(dataDir);

            // Save historical measurements
            Path measurementsFile = dataDir.resolve(\"historical-measurements.json\");
            String measurementsJson = GSON.toJson(historicalData);
            Files.writeString(measurementsFile, measurementsJson);

            // Save baseline statistics
            Path baselinesFile = dataDir.resolve(\"baseline-statistics.json\");
            String baselinesJson = GSON.toJson(baselineStatistics);
            Files.writeString(baselinesFile, baselinesJson);

        } catch (Exception e) {
            LOGGER.error(\"Failed to save historical regression data\", e);
        }
    }

    /**
     * Save regression report
     */
    private void saveRegressionReport(RegressionReport report) {
        try {
            Path reportsDir = Paths.get(\"performance/reports/regression\");
            Files.createDirectories(reportsDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd_HH-mm-ss\"));
            String filename = String.format(\"regression-%s-%s.json\",
                report.benchmarkName.replaceAll(\"[^a-zA-Z0-9]\", \"_\"), timestamp);

            Path reportFile = reportsDir.resolve(filename);
            String json = GSON.toJson(report);
            Files.writeString(reportFile, json);

            // Save as latest for this benchmark
            Path latestFile = reportsDir.resolve(String.format(\"latest-%s.json\",
                report.benchmarkName.replaceAll(\"[^a-zA-Z0-9]\", \"_\")));
            Files.writeString(latestFile, json);

        } catch (Exception e) {
            LOGGER.error(\"Failed to save regression report\", e);
        }
    }

    /**
     * Get regression analysis summary for all benchmarks
     */
    public Map<String, RegressionSummary> getRegressionSummary() {
        Map<String, RegressionSummary> summary = new HashMap<>();

        for (RegressionReport report : regressionHistory) {
            summary.put(report.benchmarkName, new RegressionSummary(
                report.benchmarkName,
                report.isRegression,
                report.isImprovement,
                report.isStatisticallySignificant,
                report.percentChange,
                report.effectSize,
                report.trendDirection,
                report.timestamp
            ));
        }

        return summary;
    }

    // Data classes

    public static class RegressionReport {
        public final String benchmarkName;
        public final String timestamp;
        public final Map<String, Object> metadata;

        // Statistical analysis results
        public StatisticalAnalysis.StatisticalReport currentStatistics;
        public BaselineStatistics baseline;

        // Regression analysis
        public double percentChange = 0.0;
        public double absoluteChange = 0.0;
        public boolean isRegression = false;
        public boolean isImprovement = false;
        public boolean isStatisticallySignificant = false;

        // Effect size analysis
        public double effectSize = 0.0;
        public String effectSizeMagnitude = \"Unknown\";

        // Statistical tests
        public double tStatistic = 0.0;
        public double pValue = 1.0;
        public double statisticalPower = 0.0;
        public double confidenceIntervalLower = 0.0;
        public double confidenceIntervalUpper = 0.0;

        // Trend analysis
        public double trendSlope = 0.0;
        public String trendDirection = \"Unknown\";
        public double trendSignificance = 0.0;

        // Issues and recommendations
        public final List<String> detectionErrors = new ArrayList<>();
        public final List<String> validationWarnings = new ArrayList<>();
        public final List<String> trendWarnings = new ArrayList<>();
        public List<String> recommendations = new ArrayList<>();

        public RegressionReport(String benchmarkName, Map<String, Object> metadata) {
            this.benchmarkName = benchmarkName;
            this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\"));
        }
    }

    public static class BaselineStatistics {
        public String benchmarkName;
        public double mean;
        public double standardDeviation;
        public double coefficientOfVariation;
        public int sampleCount;
        public LocalDateTime lastUpdated;
        public List<Double> measurements;
    }

    public static class PerformanceMeasurement {
        public String benchmarkName;
        public LocalDateTime timestamp;
        public double mean;
        public double standardDeviation;
        public double coefficientOfVariation;
        public int sampleSize;
        public List<Double> measurements;
    }

    public static class RegressionSummary {
        public final String benchmarkName;
        public final boolean isRegression;
        public final boolean isImprovement;
        public final boolean isStatisticallySignificant;
        public final double percentChange;
        public final double effectSize;
        public final String trendDirection;
        public final String timestamp;

        public RegressionSummary(String benchmarkName, boolean isRegression, boolean isImprovement,
                               boolean isStatisticallySignificant, double percentChange, double effectSize,
                               String trendDirection, String timestamp) {
            this.benchmarkName = benchmarkName;
            this.isRegression = isRegression;
            this.isImprovement = isImprovement;
            this.isStatisticallySignificant = isStatisticallySignificant;
            this.percentChange = percentChange;
            this.effectSize = effectSize;
            this.trendDirection = trendDirection;
            this.timestamp = timestamp;
        }
    }
}