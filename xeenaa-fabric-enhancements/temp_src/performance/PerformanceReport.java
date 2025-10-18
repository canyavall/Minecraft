package com.xeenaa.fabricenhancements.performance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xeenaa.fabricenhancements.performance.metrics.ChunkMetrics;
import com.xeenaa.fabricenhancements.performance.metrics.MemoryMetrics;
import com.xeenaa.fabricenhancements.performance.metrics.TPSMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enhanced comprehensive performance report generation with Spark integration.
 *
 * This class aggregates metrics from all subsystems including:
 * - TPS metrics with Spark real-time monitoring
 * - Chunk performance metrics with detailed analysis
 * - Memory metrics with alert thresholds
 * - Spark profiler integration data
 * - Environment information for context
 *
 * Reports are generated in JSON format with human-readable summaries,
 * providing comprehensive performance analysis for both development
 * and production environments.
 */
public class PerformanceReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceReport.class);
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private final ReportData reportData;

    private PerformanceReport(ReportData reportData) {
        this.reportData = reportData;
    }

    /**
     * Save the report to file
     */
    public void saveToFile() throws IOException {
        Path reportsDir = Paths.get("performance/reports");
        Files.createDirectories(reportsDir);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path reportFile = reportsDir.resolve("performance-report-" + timestamp + ".json");

        String jsonReport = GSON.toJson(reportData);
        Files.writeString(reportFile, jsonReport);

        // Also save as latest report
        Path latestReport = reportsDir.resolve("latest-performance-report.json");
        Files.writeString(latestReport, jsonReport);

        LOGGER.info("Performance report saved to: {}", reportFile);
    }

    /**
     * Generate summary text report
     */
    public String generateSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Performance Report Summary ===\n");
        summary.append("Generated: ").append(reportData.timestamp).append("\n");
        summary.append("Duration: ").append(reportData.durationMinutes).append(" minutes\n\n");

        // TPS Summary
        if (reportData.tpsMetrics != null) {
            summary.append("TPS Performance:\n");
            summary.append("  Current: ").append(String.format("%.2f", reportData.tpsMetrics.currentTPS)).append("\n");
            summary.append("  Average: ").append(String.format("%.2f", reportData.tpsMetrics.averageTPS)).append("\n");
            summary.append("  Minimum: ").append(String.format("%.2f", reportData.tpsMetrics.minTPS)).append("\n");
            summary.append("  Stability: ").append(String.format("%.3f", reportData.tpsMetrics.stability)).append("\n");
            summary.append("  Below Threshold: ").append(reportData.tpsMetrics.belowThreshold ? "YES" : "NO").append("\n\n");
        }

        // Chunk Summary
        if (reportData.chunkMetrics != null) {
            summary.append("Chunk Performance:\n");
            summary.append("  Total Loads: ").append(reportData.chunkMetrics.totalLoads).append("\n");
            summary.append("  Currently Loaded: ").append(reportData.chunkMetrics.currentlyLoaded).append("\n");
            summary.append("  Average Load Time: ").append(String.format("%.2f ms", reportData.chunkMetrics.averageLoadTimeMs)).append("\n");
            summary.append("  Cache Hit Ratio: ").append(String.format("%.2f%%", reportData.chunkMetrics.cacheHitRatio * 100)).append("\n\n");
        }

        // Memory Summary
        if (reportData.memoryMetrics != null) {
            summary.append("Memory Performance:\n");
            summary.append("  Current Usage: ").append(String.format("%.1f MB", reportData.memoryMetrics.currentUsageMB)).append("\n");
            summary.append("  Max Memory: ").append(String.format("%.1f MB", reportData.memoryMetrics.maxMemoryMB)).append("\n");
            summary.append("  Memory Utilization: ").append(String.format("%.1f%%",
                    (reportData.memoryMetrics.currentUsageMB / reportData.memoryMetrics.maxMemoryMB) * 100)).append("\n");
            summary.append("  GC Collections: ").append(reportData.memoryMetrics.totalGCCollections).append("\n\n");
        }

        // Spark Integration Summary
        if (reportData.sparkMetrics != null && !reportData.sparkMetrics.isEmpty()) {
            summary.append("Spark Integration:\n");
            summary.append(reportData.sparkMetrics).append("\n\n");
        }

        // Performance Analysis
        summary.append("Performance Analysis:\n");
        summary.append(generatePerformanceAnalysis());

        return summary.toString();
    }

    /**
     * Generate performance analysis with recommendations
     */
    private String generatePerformanceAnalysis() {
        StringBuilder analysis = new StringBuilder();

        // TPS Analysis
        if (reportData.tpsMetrics != null) {
            if (reportData.tpsMetrics.currentTPS < 18.0) {
                analysis.append("  ⚠ WARNING: TPS is significantly below target (").append(String.format("%.2f", reportData.tpsMetrics.currentTPS)).append(" < 20.0)\n");
            } else if (reportData.tpsMetrics.currentTPS < 19.5) {
                analysis.append("  ⚠ NOTICE: TPS is below optimal level (").append(String.format("%.2f", reportData.tpsMetrics.currentTPS)).append(" < 19.5)\n");
            } else {
                analysis.append("  ✓ TPS is healthy (").append(String.format("%.2f", reportData.tpsMetrics.currentTPS)).append(")\n");
            }

            if (reportData.tpsMetrics.stability > 2.0) {
                analysis.append("  ⚠ TPS stability is poor (variance: ").append(String.format("%.3f", reportData.tpsMetrics.stability)).append(")\n");
            }
        }

        // Chunk Analysis
        if (reportData.chunkMetrics != null) {
            if (reportData.chunkMetrics.averageLoadTimeMs > 50.0) {
                analysis.append("  ⚠ Chunk loading is slow (").append(String.format("%.2f ms", reportData.chunkMetrics.averageLoadTimeMs)).append(" > 50ms)\n");
            } else {
                analysis.append("  ✓ Chunk loading performance is good (").append(String.format("%.2f ms", reportData.chunkMetrics.averageLoadTimeMs)).append(")\n");
            }

            if (reportData.chunkMetrics.cacheHitRatio < 0.8) {
                analysis.append("  ⚠ Chunk cache hit ratio is low (").append(String.format("%.2f%%", reportData.chunkMetrics.cacheHitRatio * 100)).append(" < 80%)\n");
            } else {
                analysis.append("  ✓ Chunk cache performance is good (").append(String.format("%.2f%%", reportData.chunkMetrics.cacheHitRatio * 100)).append(")\n");
            }
        }

        // Memory Analysis
        if (reportData.memoryMetrics != null) {
            double memoryUtilization = (reportData.memoryMetrics.currentUsageMB / reportData.memoryMetrics.maxMemoryMB) * 100;
            if (memoryUtilization > 85.0) {
                analysis.append("  ⚠ High memory utilization (").append(String.format("%.1f%%", memoryUtilization)).append(")\n");
            } else if (memoryUtilization > 70.0) {
                analysis.append("  ⚠ Elevated memory utilization (").append(String.format("%.1f%%", memoryUtilization)).append(")\n");
            } else {
                analysis.append("  ✓ Memory utilization is healthy (").append(String.format("%.1f%%", memoryUtilization)).append(")\n");
            }
        }

        return analysis.toString();
    }

    /**
     * Builder pattern for creating performance reports
     */
    public static class Builder {
        private final ReportData reportData = new ReportData();

        public Builder() {
            reportData.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            reportData.version = "1.0.0";
        }

        public Builder withDuration(long durationMinutes) {
            reportData.durationMinutes = durationMinutes;
            return this;
        }

        public Builder withTPSMetrics(TPSMetrics.TPSMetricsReport tpsMetrics) {
            reportData.tpsMetrics = tpsMetrics;
            return this;
        }

        public Builder withChunkMetrics(ChunkMetrics.ChunkMetricsReport chunkMetrics) {
            reportData.chunkMetrics = chunkMetrics;
            return this;
        }

        public Builder withMemoryMetrics(MemoryMetrics.MemoryMetricsReport memoryMetrics) {
            reportData.memoryMetrics = memoryMetrics;
            return this;
        }

        public Builder withEnvironmentInfo(EnvironmentInfo environmentInfo) {
            reportData.environmentInfo = environmentInfo;
            return this;
        }

        public Builder withSparkMetrics(String sparkMetrics) {
            reportData.sparkMetrics = sparkMetrics;
            return this;
        }

        public PerformanceReport build() {
            // Set environment info if not provided
            if (reportData.environmentInfo == null) {
                reportData.environmentInfo = EnvironmentInfo.collect();
            }

            return new PerformanceReport(reportData);
        }
    }

    /**
     * Report data structure
     */
    private static class ReportData {
        String timestamp;
        String version;
        long durationMinutes;
        TPSMetrics.TPSMetricsReport tpsMetrics;
        ChunkMetrics.ChunkMetricsReport chunkMetrics;
        MemoryMetrics.MemoryMetricsReport memoryMetrics;
        EnvironmentInfo environmentInfo;
        String sparkMetrics;
    }

    /**
     * Environment information for context
     */
    public static class EnvironmentInfo {
        public final String javaVersion;
        public final String osName;
        public final String osVersion;
        public final int availableProcessors;
        public final long maxMemoryMB;
        public final String minecraftVersion;

        private EnvironmentInfo(String javaVersion, String osName, String osVersion,
                               int availableProcessors, long maxMemoryMB, String minecraftVersion) {
            this.javaVersion = javaVersion;
            this.osName = osName;
            this.osVersion = osVersion;
            this.availableProcessors = availableProcessors;
            this.maxMemoryMB = maxMemoryMB;
            this.minecraftVersion = minecraftVersion;
        }

        public static EnvironmentInfo collect() {
            Runtime runtime = Runtime.getRuntime();
            return new EnvironmentInfo(
                    System.getProperty("java.version"),
                    System.getProperty("os.name"),
                    System.getProperty("os.version"),
                    runtime.availableProcessors(),
                    runtime.maxMemory() / (1024 * 1024),
                    "1.21.1" // Could be dynamically detected
            );
        }
    }
}