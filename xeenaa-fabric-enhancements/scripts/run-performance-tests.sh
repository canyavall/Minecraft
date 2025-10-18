#!/bin/bash
# Linux/macOS shell script for running comprehensive performance tests
# This script runs all performance testing components in sequence

set -e  # Exit on any error

echo "========================================"
echo "Xeenaa Fabric Enhancements Performance Test Suite"
echo "========================================"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR"

echo ""
echo "Current directory: $(pwd)"
echo "Timestamp: $(date)"

# Check if Gradle wrapper exists
if [ ! -f "./gradlew" ]; then
    echo "ERROR: Gradle wrapper not found. Please ensure gradlew exists in the project root."
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

echo ""
echo "========================================"
echo "Phase 1: Collecting Baseline Metrics"
echo "========================================"
echo "Running baseline collection..."
./gradlew collectBaseline
echo "Baseline collection completed successfully."

echo ""
echo "========================================"
echo "Phase 2: Running JMH Benchmarks"
echo "========================================"
echo "Running JMH performance benchmarks..."
./gradlew jmh
echo "JMH benchmarks completed successfully."

echo ""
echo "========================================"
echo "Phase 3: Running Unit Tests"
echo "========================================"
echo "Running unit tests..."
./gradlew test
echo "Unit tests completed successfully."

echo ""
echo "========================================"
echo "Phase 4: Running Performance Regression Tests"
echo "========================================"
echo "Running performance regression tests..."
./gradlew performanceTest
echo "Performance tests completed successfully."

echo ""
echo "========================================"
echo "Phase 5: Generating Performance Report"
echo "========================================"
echo "Generating comprehensive performance report..."
./gradlew generatePerformanceReport
echo "Performance report generated successfully."

echo ""
echo "========================================"
echo "Performance Test Suite Completed Successfully"
echo "========================================"
echo ""
echo "Results available in:"
echo "  - performance/reports/ (detailed reports)"
echo "  - build/reports/jmh/ (JMH benchmark results)"
echo "  - build/reports/tests/ (unit test results)"
echo ""
echo "Timestamp: $(date)"