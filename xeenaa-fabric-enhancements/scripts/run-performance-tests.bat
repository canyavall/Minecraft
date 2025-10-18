@echo off
REM Windows batch script for running comprehensive performance tests
REM This script runs all performance testing components in sequence

echo ========================================
echo Xeenaa Fabric Enhancements Performance Test Suite
echo ========================================

set SCRIPT_DIR=%~dp0
set PROJECT_DIR=%SCRIPT_DIR%\..
cd /d "%PROJECT_DIR%"

echo.
echo Current directory: %CD%
echo Timestamp: %DATE% %TIME%

REM Check if Gradle wrapper exists
if not exist gradlew.bat (
    echo ERROR: Gradle wrapper not found. Please ensure gradlew.bat exists in the project root.
    exit /b 1
)

echo.
echo ========================================
echo Phase 1: Collecting Baseline Metrics
echo ========================================
echo Running baseline collection...
call gradlew.bat collectBaseline
if errorlevel 1 (
    echo ERROR: Baseline collection failed
    exit /b 1
)
echo Baseline collection completed successfully.

echo.
echo ========================================
echo Phase 2: Running JMH Benchmarks
echo ========================================
echo Running JMH performance benchmarks...
call gradlew.bat jmh
if errorlevel 1 (
    echo ERROR: JMH benchmarks failed
    exit /b 1
)
echo JMH benchmarks completed successfully.

echo.
echo ========================================
echo Phase 3: Running Unit Tests
echo ========================================
echo Running unit tests...
call gradlew.bat test
if errorlevel 1 (
    echo ERROR: Unit tests failed
    exit /b 1
)
echo Unit tests completed successfully.

echo.
echo ========================================
echo Phase 4: Running Performance Regression Tests
echo ========================================
echo Running performance regression tests...
call gradlew.bat performanceTest
if errorlevel 1 (
    echo ERROR: Performance tests failed
    exit /b 1
)
echo Performance tests completed successfully.

echo.
echo ========================================
echo Phase 5: Generating Performance Report
echo ========================================
echo Generating comprehensive performance report...
call gradlew.bat generatePerformanceReport
if errorlevel 1 (
    echo ERROR: Report generation failed
    exit /b 1
)
echo Performance report generated successfully.

echo.
echo ========================================
echo Performance Test Suite Completed Successfully
echo ========================================
echo.
echo Results available in:
echo   - performance/reports/ (detailed reports)
echo   - build/reports/jmh/ (JMH benchmark results)
echo   - build/reports/tests/ (unit test results)
echo.
echo Timestamp: %DATE% %TIME%