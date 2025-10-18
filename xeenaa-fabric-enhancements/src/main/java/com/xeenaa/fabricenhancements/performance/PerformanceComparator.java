package com.xeenaa.fabricenhancements.performance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

/**
 * Compares performance results before and after optimizations
 */
public class PerformanceComparator {
    private static final Logger LOGGER = LoggerFactory.getLogger("PerformanceComparator");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path RESULTS_DIR = Paths.get("performance_results");

    /**
     * Save performance test results
     */
    public static void saveResults(String testName, PerformanceResults results) {
        try {
            Files.createDirectories(RESULTS_DIR);

            String timestamp = Instant.now().toString().replace(":", "-");
            String filename = String.format("%s_%s.json", testName, timestamp);
            Path filePath = RESULTS_DIR.resolve(filename);

            String json = GSON.toJson(results);
            Files.writeString(filePath, json);

            LOGGER.info("Performance results saved to: {}", filePath);
        } catch (IOException e) {
            LOGGER.error("Failed to save performance results", e);
        }
    }

    /**
     * Load performance results from file
     */
    public static PerformanceResults loadResults(Path filePath) {
        try {
            String json = Files.readString(filePath);
            return GSON.fromJson(json, PerformanceResults.class);
        } catch (IOException e) {
            LOGGER.error("Failed to load performance results from {}", filePath, e);
            return null;
        }
    }

    /**
     * Compare two performance result sets
     */
    public static ComparisonReport compare(PerformanceResults baseline, PerformanceResults optimized) {
        ComparisonReport report = new ComparisonReport();
        report.baselineId = baseline.testId;
        report.optimizedId = optimized.testId;
        report.comparisonTime = Instant.now().toString();

        // Compare chunk loading performance
        if (baseline.chunkLoadTimeMs > 0 && optimized.chunkLoadTimeMs > 0) {
            double improvement = ((baseline.chunkLoadTimeMs - optimized.chunkLoadTimeMs) / baseline.chunkLoadTimeMs) * 100;
            report.chunkLoadImprovement = improvement;
            report.chunkLoadSignificant = Math.abs(improvement) > 5.0; // 5% threshold
        }

        // Compare memory usage
        if (baseline.memoryUsageMB > 0 && optimized.memoryUsageMB > 0) {
            double improvement = ((baseline.memoryUsageMB - optimized.memoryUsageMB) / baseline.memoryUsageMB) * 100;
            report.memoryImprovement = improvement;
            report.memorySignificant = Math.abs(improvement) > 5.0;
        }

        // Compare TPS
        if (baseline.averageTPS > 0 && optimized.averageTPS > 0) {
            double improvement = ((optimized.averageTPS - baseline.averageTPS) / baseline.averageTPS) * 100;
            report.tpsImprovement = improvement;
            report.tpsSignificant = Math.abs(improvement) > 2.0; // 2% threshold for TPS
        }

        // Overall assessment
        report.overallImprovement = calculateOverallImprovement(report);
        report.targetsMet = checkTargetsMet(report);

        return report;
    }

    /**
     * Generate a formatted comparison report
     */
    public static String generateReport(ComparisonReport comparison) {
        StringBuilder report = new StringBuilder();

        report.append("=".repeat(60)).append("\n");
        report.append("PERFORMANCE COMPARISON REPORT\n");
        report.append("=".repeat(60)).append("\n");
        report.append(String.format("Baseline: %s\n", comparison.baselineId));
        report.append(String.format("Optimized: %s\n", comparison.optimizedId));
        report.append(String.format("Generated: %s\n", comparison.comparisonTime));
        report.append("\n");

        // Chunk Loading Results
        report.append("CHUNK LOADING PERFORMANCE:\n");
        report.append("-".repeat(30)).append("\n");
        if (comparison.chunkLoadImprovement != 0) {
            String status = comparison.chunkLoadSignificant ? "SIGNIFICANT" : "Not significant";
            String direction = comparison.chunkLoadImprovement > 0 ? "IMPROVEMENT" : "REGRESSION";
            report.append(String.format("Change: %.2f%% %s (%s)\n",
                Math.abs(comparison.chunkLoadImprovement), direction, status));
        } else {
            report.append("No data available\n");
        }
        report.append("\n");

        // Memory Usage Results
        report.append("MEMORY USAGE:\n");
        report.append("-".repeat(30)).append("\n");
        if (comparison.memoryImprovement != 0) {
            String status = comparison.memorySignificant ? "SIGNIFICANT" : "Not significant";
            String direction = comparison.memoryImprovement > 0 ? "REDUCTION" : "INCREASE";
            report.append(String.format("Change: %.2f%% %s (%s)\n",
                Math.abs(comparison.memoryImprovement), direction, status));
        } else {
            report.append("No data available\n");
        }
        report.append("\n");

        // TPS Results
        report.append("TPS PERFORMANCE:\n");
        report.append("-".repeat(30)).append("\n");
        if (comparison.tpsImprovement != 0) {
            String status = comparison.tpsSignificant ? "SIGNIFICANT" : "Not significant";
            String direction = comparison.tpsImprovement > 0 ? "IMPROVEMENT" : "REGRESSION";
            report.append(String.format("Change: %.2f%% %s (%s)\n",
                Math.abs(comparison.tpsImprovement), direction, status));
        } else {
            report.append("No data available\n");
        }
        report.append("\n");

        // Overall Assessment
        report.append("OVERALL ASSESSMENT:\n");
        report.append("-".repeat(30)).append("\n");
        report.append(String.format("Overall Improvement: %.2f%%\n", comparison.overallImprovement));
        report.append(String.format("Performance Targets Met: %s\n", comparison.targetsMet ? "YES" : "NO"));

        if (comparison.overallImprovement > 10) {
            report.append("🎉 EXCELLENT performance improvement!\n");
        } else if (comparison.overallImprovement > 5) {
            report.append("✅ Good performance improvement\n");
        } else if (comparison.overallImprovement > 0) {
            report.append("⚠️ Minor improvement - consider further optimization\n");
        } else {
            report.append("❌ Performance regression detected - review changes\n");
        }

        report.append("=".repeat(60)).append("\n");

        return report.toString();
    }

    private static double calculateOverallImprovement(ComparisonReport report) {
        List<Double> improvements = new ArrayList<>();

        if (report.chunkLoadImprovement != 0) improvements.add(report.chunkLoadImprovement);
        if (report.memoryImprovement != 0) improvements.add(report.memoryImprovement);
        if (report.tpsImprovement != 0) improvements.add(report.tpsImprovement);

        if (improvements.isEmpty()) return 0.0;

        return improvements.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private static boolean checkTargetsMet(ComparisonReport report) {
        boolean chunkTarget = report.chunkLoadImprovement >= 15.0; // 15% improvement target
        boolean memoryTarget = report.memoryImprovement >= 10.0;   // 10% reduction target
        boolean tpsTarget = report.tpsImprovement >= 0.0;          // No TPS regression

        return chunkTarget || memoryTarget || (tpsTarget && report.overallImprovement > 5.0);
    }

    /**
     * Performance test results container
     */
    public static class PerformanceResults {
        public String testId;
        public String timestamp;
        public String testType;
        public double chunkLoadTimeMs;
        public double memoryUsageMB;
        public double averageTPS;
        public double minTPS;
        public int totalChunksLoaded;
        public Map<String, Double> customMetrics = new HashMap<>();

        public PerformanceResults(String testId, String testType) {
            this.testId = testId;
            this.testType = testType;
            this.timestamp = Instant.now().toString();
        }
    }

    /**
     * Comparison report container
     */
    public static class ComparisonReport {
        public String baselineId;
        public String optimizedId;
        public String comparisonTime;
        public double chunkLoadImprovement;
        public boolean chunkLoadSignificant;
        public double memoryImprovement;
        public boolean memorySignificant;
        public double tpsImprovement;
        public boolean tpsSignificant;
        public double overallImprovement;
        public boolean targetsMet;
    }
}