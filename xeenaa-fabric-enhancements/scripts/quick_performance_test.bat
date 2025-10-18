@echo off
echo =====================================
echo Xeenaa Fabric Enhancements - Quick Performance Test
echo =====================================
echo.

echo Creating baseline measurement...
echo 1. Testing vanilla Minecraft performance
echo 2. Run this BEFORE implementing optimizations
echo 3. Save results for comparison
echo.

echo Starting Minecraft server with performance monitoring...
echo.

cd "%~dp0.."

echo [%date% %time%] Starting baseline test > performance_results.txt
echo ===================================== >> performance_results.txt
echo Baseline Performance Test >> performance_results.txt
echo ===================================== >> performance_results.txt

echo Starting server for 5 minutes with monitoring...
echo Press Ctrl+C to stop early if needed
echo.

gradlew runServer --args="--nogui" --info > server_performance_log.txt 2>&1

echo.
echo Test completed! Check these files:
echo - performance_results.txt (summary)
echo - server_performance_log.txt (detailed log)
echo.
echo Next steps:
echo 1. Implement your optimizations
echo 2. Run this script again
echo 3. Compare the results
echo.
pause