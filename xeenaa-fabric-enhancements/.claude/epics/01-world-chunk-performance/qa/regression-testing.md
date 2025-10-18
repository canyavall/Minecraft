# World/Chunk Performance Enhancement Regression Testing

## Overview

This document defines comprehensive regression testing procedures for the World/Chunk Performance Enhancement epic (E001). The regression testing framework ensures that optimizations maintain their effectiveness over time and prevents performance degradation due to code changes, dependency updates, or environmental changes.

## Regression Testing Strategy

### Multi-Level Regression Prevention

#### 1. Immediate Regression Detection (Commit Level)
- **Trigger**: Every commit to optimization-related code
- **Duration**: 5-10 minutes
- **Scope**: Core performance functions and critical paths
- **Threshold**: >2% performance degradation triggers failure

#### 2. Comprehensive Regression Testing (Pull Request Level)
- **Trigger**: Pull requests affecting chunk loading, memory management, or caching
- **Duration**: 30-45 minutes
- **Scope**: Full performance test suite
- **Threshold**: >5% degradation in any metric triggers failure

#### 3. Extended Regression Validation (Release Level)
- **Trigger**: Pre-release candidates and major version updates
- **Duration**: 2-4 hours
- **Scope**: Complete performance validation including stress testing
- **Threshold**: Any degradation below target improvements triggers investigation

#### 4. Continuous Monitoring (Production Level)
- **Trigger**: Continuous monitoring in production environments
- **Duration**: 24/7 monitoring with weekly reports
- **Scope**: Real-world performance tracking
- **Threshold**: Trend analysis alerts for performance degradation

## CI/CD Integration Framework

### GitHub Actions Workflow Configuration

#### Performance Regression Detection Workflow
```yaml
# .github/workflows/performance-regression.yml
name: Performance Regression Testing

on:
  pull_request:
    paths:
      - 'src/main/java/chunk/**'
      - 'src/main/java/world/**'
      - 'src/main/java/cache/**'
      - 'src/main/java/performance/**'
  push:
    branches: [main, develop]
  schedule:
    - cron: '0 2 * * 1'  # Weekly Monday 2 AM

env:
  MAVEN_OPTS: -Xmx4g
  JAVA_VERSION: 21

jobs:
  performance-baseline:
    runs-on: ubuntu-latest
    timeout-minutes: 60

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 2  # Need previous commit for comparison

      - name: Setup Java
        uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'

      - name: Cache Maven Dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}

      - name: Build Project
        run: mvn clean compile test-compile

      - name: Setup Test Environment
        run: |
          # Download and setup Minecraft server
          wget https://piston-data.mojang.com/v1/objects/minecraft-server-1.21.1.jar

          # Create test configuration
          echo "enable-jmx-monitoring=true" > server.properties
          echo "level-seed=8091867987493326313" >> server.properties
          echo "spawn-protection=0" >> server.properties

      - name: Run Performance Benchmarks
        run: |
          # Run JMH benchmarks
          mvn exec:java -Dexec.mainClass="org.openjdk.jmh.Main" \
              -Dexec.args="-rf json -rff benchmark-results.json"

      - name: Compare with Baseline
        id: performance_comparison
        run: |
          # Download previous benchmark results
          if [ "${{ github.event_name }}" = "pull_request" ]; then
            # Compare with target branch
            git checkout ${{ github.base_ref }}
            mvn exec:java -Dexec.mainClass="org.openjdk.jmh.Main" \
                -Dexec.args="-rf json -rff baseline-results.json"
            git checkout ${{ github.sha }}
          else
            # Use stored baseline
            curl -o baseline-results.json \
                "https://api.github.com/repos/${{ github.repository }}/releases/latest/assets/baseline-results.json"
          fi

          # Run comparison analysis
          python3 .github/scripts/compare_performance.py \
              baseline-results.json benchmark-results.json > comparison-report.txt

          # Set output for subsequent steps
          if grep -q "REGRESSION DETECTED" comparison-report.txt; then
            echo "regression_detected=true" >> $GITHUB_OUTPUT
          else
            echo "regression_detected=false" >> $GITHUB_OUTPUT
          fi

      - name: Upload Benchmark Results
        uses: actions/upload-artifact@v3
        with:
          name: benchmark-results
          path: |
            benchmark-results.json
            baseline-results.json
            comparison-report.txt

      - name: Comment Performance Results
        if: github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            const fs = require('fs');
            const report = fs.readFileSync('comparison-report.txt', 'utf8');

            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: `## Performance Regression Analysis\n\`\`\`\n${report}\n\`\`\``
            });

      - name: Fail on Regression
        if: steps.performance_comparison.outputs.regression_detected == 'true'
        run: |
          echo "Performance regression detected! Check the comparison report."
          exit 1

  integration-performance-test:
    runs-on: ubuntu-latest
    timeout-minutes: 90
    needs: performance-baseline

    strategy:
      matrix:
        test_scenario: [single_player, multi_player, stress_test]

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Setup Test Environment
        run: |
          # Setup environment specific to test scenario
          case "${{ matrix.test_scenario }}" in
            "single_player")
              echo "max-players=1" >> server.properties
              ;;
            "multi_player")
              echo "max-players=20" >> server.properties
              ;;
            "stress_test")
              echo "max-players=50" >> server.properties
              echo "view-distance=16" >> server.properties
              ;;
          esac

      - name: Run Integration Performance Tests
        run: |
          # Start server with performance monitoring
          timeout 30m ./scripts/run_integration_performance_test.sh ${{ matrix.test_scenario }}

      - name: Analyze Integration Results
        run: |
          python3 .github/scripts/analyze_integration_performance.py \
              test_results/${{ matrix.test_scenario }}/

      - name: Upload Integration Results
        uses: actions/upload-artifact@v3
        with:
          name: integration-results-${{ matrix.test_scenario }}
          path: test_results/${{ matrix.test_scenario }}/
```

#### Performance Comparison Script
```python
#!/usr/bin/env python3
# .github/scripts/compare_performance.py

import json
import sys
import statistics
from typing import Dict, List, Tuple

class PerformanceComparer:
    def __init__(self, baseline_file: str, current_file: str):
        self.baseline_data = self._load_benchmark_data(baseline_file)
        self.current_data = self._load_benchmark_data(current_file)

        # Performance thresholds
        self.regression_thresholds = {
            'chunk_loading_time': 5.0,  # 5% degradation threshold
            'memory_usage': 10.0,       # 10% increase threshold
            'cache_hit_ratio': 5.0,     # 5% decrease threshold
            'tps_stability': 2.0        # 2% decrease threshold
        }

    def _load_benchmark_data(self, file_path: str) -> Dict:
        """Load and parse JMH benchmark results"""
        with open(file_path, 'r') as f:
            data = json.load(f)

        # Parse JMH results into standardized format
        parsed_results = {}
        for benchmark in data:
            benchmark_name = benchmark['benchmark']
            primary_metric = benchmark['primaryMetric']

            parsed_results[benchmark_name] = {
                'score': primary_metric['score'],
                'error': primary_metric['scoreError'],
                'unit': primary_metric['scoreUnit'],
                'confidence_interval': primary_metric['scoreConfidence']
            }

        return parsed_results

    def compare_benchmarks(self) -> Dict[str, any]:
        """Compare current results with baseline"""
        comparison_results = {
            'regressions': [],
            'improvements': [],
            'stable': [],
            'missing_benchmarks': [],
            'new_benchmarks': []
        }

        # Check for missing benchmarks
        baseline_benchmarks = set(self.baseline_data.keys())
        current_benchmarks = set(self.current_data.keys())

        comparison_results['missing_benchmarks'] = list(baseline_benchmarks - current_benchmarks)
        comparison_results['new_benchmarks'] = list(current_benchmarks - baseline_benchmarks)

        # Compare common benchmarks
        common_benchmarks = baseline_benchmarks & current_benchmarks

        for benchmark in common_benchmarks:
            baseline_score = self.baseline_data[benchmark]['score']
            current_score = self.current_data[benchmark]['score']

            # Calculate percentage change
            percent_change = ((current_score - baseline_score) / baseline_score) * 100

            result = {
                'benchmark': benchmark,
                'baseline_score': baseline_score,
                'current_score': current_score,
                'percent_change': percent_change,
                'is_improvement': self._is_improvement(benchmark, percent_change),
                'is_regression': self._is_regression(benchmark, percent_change)
            }

            if result['is_regression']:
                comparison_results['regressions'].append(result)
            elif result['is_improvement']:
                comparison_results['improvements'].append(result)
            else:
                comparison_results['stable'].append(result)

        return comparison_results

    def _is_improvement(self, benchmark: str, percent_change: float) -> bool:
        """Determine if change represents improvement"""
        # For time-based metrics, lower is better
        if 'time' in benchmark.lower() or 'duration' in benchmark.lower():
            return percent_change < -2.0  # 2% improvement threshold
        # For throughput metrics, higher is better
        else:
            return percent_change > 2.0

    def _is_regression(self, benchmark: str, percent_change: float) -> bool:
        """Determine if change represents regression"""
        threshold = self._get_regression_threshold(benchmark)

        # For time-based metrics, higher is worse
        if 'time' in benchmark.lower() or 'duration' in benchmark.lower():
            return percent_change > threshold
        # For throughput metrics, lower is worse
        else:
            return percent_change < -threshold

    def _get_regression_threshold(self, benchmark: str) -> float:
        """Get regression threshold for specific benchmark"""
        for pattern, threshold in self.regression_thresholds.items():
            if pattern in benchmark.lower():
                return threshold
        return 5.0  # Default 5% threshold

    def generate_report(self, comparison_results: Dict) -> str:
        """Generate human-readable performance report"""
        report_lines = []

        # Header
        report_lines.append("=== PERFORMANCE REGRESSION ANALYSIS ===")
        report_lines.append("")

        # Summary
        regressions = comparison_results['regressions']
        improvements = comparison_results['improvements']
        stable = comparison_results['stable']

        report_lines.append(f"📊 SUMMARY:")
        report_lines.append(f"  • Regressions: {len(regressions)}")
        report_lines.append(f"  • Improvements: {len(improvements)}")
        report_lines.append(f"  • Stable: {len(stable)}")
        report_lines.append("")

        # Regressions (critical)
        if regressions:
            report_lines.append("🚨 REGRESSIONS DETECTED:")
            for regression in regressions:
                report_lines.append(
                    f"  • {regression['benchmark']}: "
                    f"{regression['percent_change']:+.2f}% "
                    f"({regression['baseline_score']:.3f} → {regression['current_score']:.3f})"
                )
            report_lines.append("")

        # Improvements (positive)
        if improvements:
            report_lines.append("✅ IMPROVEMENTS:")
            for improvement in improvements:
                report_lines.append(
                    f"  • {improvement['benchmark']}: "
                    f"{improvement['percent_change']:+.2f}% "
                    f"({improvement['baseline_score']:.3f} → {improvement['current_score']:.3f})"
                )
            report_lines.append("")

        # Missing benchmarks
        if comparison_results['missing_benchmarks']:
            report_lines.append("⚠️  MISSING BENCHMARKS:")
            for benchmark in comparison_results['missing_benchmarks']:
                report_lines.append(f"  • {benchmark}")
            report_lines.append("")

        # Result determination
        if regressions:
            report_lines.append("❌ RESULT: REGRESSION DETECTED")
            report_lines.append("Action Required: Review performance optimizations")
        else:
            report_lines.append("✅ RESULT: NO REGRESSIONS DETECTED")
            report_lines.append("Performance is stable or improved")

        return "\n".join(report_lines)

def main():
    if len(sys.argv) != 3:
        print("Usage: python compare_performance.py <baseline.json> <current.json>")
        sys.exit(1)

    baseline_file = sys.argv[1]
    current_file = sys.argv[2]

    comparer = PerformanceComparer(baseline_file, current_file)
    comparison_results = comparer.compare_benchmarks()
    report = comparer.generate_report(comparison_results)

    print(report)

    # Exit with error code if regressions detected
    if comparison_results['regressions']:
        sys.exit(1)

if __name__ == "__main__":
    main()
```

### Integration Test Script
```bash
#!/bin/bash
# scripts/run_integration_performance_test.sh

set -e

TEST_SCENARIO=${1:-"single_player"}
OUTPUT_DIR="test_results/${TEST_SCENARIO}"
DURATION=1800  # 30 minutes

mkdir -p "$OUTPUT_DIR"

echo "Starting integration performance test: $TEST_SCENARIO"

# Start Minecraft server with monitoring
java -Xms4G -Xmx4G -XX:+UseG1GC \
     -javaagent:async-profiler.jar=start,event=cpu,file=${OUTPUT_DIR}/cpu_profile.html \
     -jar minecraft-server.jar nogui &
SERVER_PID=$!

# Wait for server startup
sleep 60

# Start Spark monitoring
echo "spark profiler --timeout $DURATION --output ${OUTPUT_DIR}/spark_profile.json" | nc localhost 25575 &
echo "spark tps --timeout $DURATION --export csv --output ${OUTPUT_DIR}/tps_data.csv" | nc localhost 25575 &
echo "spark heap --interval 10 --timeout $DURATION --output ${OUTPUT_DIR}/memory_data.csv" | nc localhost 25575 &

# Run scenario-specific tests
case "$TEST_SCENARIO" in
    "single_player")
        run_single_player_test "$OUTPUT_DIR"
        ;;
    "multi_player")
        run_multi_player_test "$OUTPUT_DIR"
        ;;
    "stress_test")
        run_stress_test "$OUTPUT_DIR"
        ;;
esac

# Wait for test completion
wait

# Stop server
kill $SERVER_PID 2>/dev/null || true

echo "Integration test completed: $TEST_SCENARIO"
```

## Continuous Performance Monitoring

### Production Performance Monitoring

#### Real-Time Performance Dashboard
```python
#!/usr/bin/env python3
# monitoring/performance_dashboard.py

import time
import json
import logging
from datetime import datetime, timedelta
from typing import Dict, List
import requests

class PerformanceMonitor:
    def __init__(self, config: Dict):
        self.config = config
        self.baseline_metrics = self._load_baseline_metrics()
        self.alert_thresholds = config['alert_thresholds']

    def collect_current_metrics(self) -> Dict:
        """Collect current performance metrics from running server"""
        metrics = {}

        # Collect from Spark API
        spark_response = requests.get(f"{self.config['spark_api_url']}/metrics")
        metrics['spark'] = spark_response.json()

        # Collect from custom monitoring endpoints
        custom_response = requests.get(f"{self.config['server_url']}/performance/metrics")
        metrics['custom'] = custom_response.json()

        return metrics

    def analyze_performance_trends(self, historical_data: List[Dict]) -> Dict:
        """Analyze performance trends over time"""
        if len(historical_data) < 10:
            return {'status': 'insufficient_data'}

        # Calculate trend analysis
        chunk_loading_times = [d['chunk_loading']['average_ms'] for d in historical_data]
        memory_usage = [d['memory']['heap_used_mb'] for d in historical_data]
        tps_values = [d['tps']['average'] for d in historical_data]

        trends = {
            'chunk_loading_trend': self._calculate_trend(chunk_loading_times),
            'memory_trend': self._calculate_trend(memory_usage),
            'tps_trend': self._calculate_trend(tps_values),
            'timestamp': datetime.now().isoformat()
        }

        return trends

    def detect_regressions(self, current_metrics: Dict) -> List[Dict]:
        """Detect performance regressions against baseline"""
        regressions = []

        # Compare chunk loading performance
        current_chunk_time = current_metrics['chunk_loading']['average_ms']
        baseline_chunk_time = self.baseline_metrics['chunk_loading']['average_ms']

        if current_chunk_time > baseline_chunk_time * 1.1:  # 10% threshold
            regressions.append({
                'metric': 'chunk_loading_time',
                'current': current_chunk_time,
                'baseline': baseline_chunk_time,
                'degradation_percent': ((current_chunk_time - baseline_chunk_time) / baseline_chunk_time) * 100,
                'severity': 'high' if current_chunk_time > baseline_chunk_time * 1.2 else 'medium'
            })

        # Compare memory usage
        current_memory = current_metrics['memory']['peak_mb']
        baseline_memory = self.baseline_metrics['memory']['peak_mb']

        if current_memory > baseline_memory * 1.15:  # 15% threshold
            regressions.append({
                'metric': 'memory_usage',
                'current': current_memory,
                'baseline': baseline_memory,
                'degradation_percent': ((current_memory - baseline_memory) / baseline_memory) * 100,
                'severity': 'high' if current_memory > baseline_memory * 1.25 else 'medium'
            })

        return regressions

    def send_alerts(self, regressions: List[Dict]):
        """Send alerts for detected regressions"""
        if not regressions:
            return

        alert_message = self._format_alert_message(regressions)

        # Send to configured alert channels
        for channel in self.config['alert_channels']:
            if channel['type'] == 'slack':
                self._send_slack_alert(channel, alert_message)
            elif channel['type'] == 'email':
                self._send_email_alert(channel, alert_message)
            elif channel['type'] == 'webhook':
                self._send_webhook_alert(channel, alert_message)

    def _calculate_trend(self, values: List[float]) -> str:
        """Calculate trend direction from time series data"""
        if len(values) < 5:
            return 'unknown'

        # Simple linear trend calculation
        recent_avg = sum(values[-5:]) / 5
        historical_avg = sum(values[:-5]) / (len(values) - 5)

        change_percent = ((recent_avg - historical_avg) / historical_avg) * 100

        if change_percent > 5:
            return 'deteriorating'
        elif change_percent < -5:
            return 'improving'
        else:
            return 'stable'

    def run_continuous_monitoring(self):
        """Main monitoring loop"""
        logging.info("Starting continuous performance monitoring")

        while True:
            try:
                # Collect current metrics
                current_metrics = self.collect_current_metrics()

                # Store metrics for historical analysis
                self._store_metrics(current_metrics)

                # Detect regressions
                regressions = self.detect_regressions(current_metrics)

                if regressions:
                    logging.warning(f"Performance regressions detected: {len(regressions)}")
                    self.send_alerts(regressions)

                # Sleep until next monitoring cycle
                time.sleep(self.config['monitoring_interval_seconds'])

            except Exception as e:
                logging.error(f"Monitoring error: {e}")
                time.sleep(60)  # Wait before retry
```

### Automated Performance Reporting

#### Weekly Performance Report Generation
```python
#!/usr/bin/env python3
# reporting/weekly_performance_report.py

import json
from datetime import datetime, timedelta
from jinja2 import Template

class WeeklyReportGenerator:
    def __init__(self):
        self.report_template = self._load_template()

    def generate_weekly_report(self, start_date: datetime) -> str:
        """Generate comprehensive weekly performance report"""

        end_date = start_date + timedelta(days=7)

        # Collect performance data for the week
        performance_data = self._collect_weekly_data(start_date, end_date)

        # Calculate performance metrics
        metrics_summary = self._calculate_weekly_metrics(performance_data)

        # Generate trend analysis
        trend_analysis = self._analyze_weekly_trends(performance_data)

        # Identify any regressions or improvements
        changes = self._identify_performance_changes(performance_data)

        # Generate report
        report_data = {
            'week_start': start_date.strftime('%Y-%m-%d'),
            'week_end': end_date.strftime('%Y-%m-%d'),
            'metrics_summary': metrics_summary,
            'trend_analysis': trend_analysis,
            'performance_changes': changes,
            'recommendations': self._generate_recommendations(metrics_summary, changes)
        }

        return self.report_template.render(**report_data)

    def _load_template(self) -> Template:
        """Load HTML report template"""
        template_content = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Weekly Performance Report - {{ week_start }} to {{ week_end }}</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 40px; }
                .metric { background: #f5f5f5; padding: 15px; margin: 10px 0; border-radius: 5px; }
                .improvement { border-left: 5px solid #4CAF50; }
                .regression { border-left: 5px solid #f44336; }
                .stable { border-left: 5px solid #2196F3; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                th { background-color: #f2f2f2; }
            </style>
        </head>
        <body>
            <h1>Weekly Performance Report</h1>
            <h2>Period: {{ week_start }} to {{ week_end }}</h2>

            <h3>Performance Metrics Summary</h3>
            <div class="metric">
                <h4>Chunk Loading Performance</h4>
                <p>Average Time: {{ metrics_summary.chunk_loading.average_ms }}ms</p>
                <p>95th Percentile: {{ metrics_summary.chunk_loading.p95_ms }}ms</p>
                <p>Change from Previous Week: {{ metrics_summary.chunk_loading.week_over_week_change }}%</p>
            </div>

            <div class="metric">
                <h4>Memory Usage</h4>
                <p>Average Heap Usage: {{ metrics_summary.memory.average_heap_mb }}MB</p>
                <p>Peak Memory: {{ metrics_summary.memory.peak_mb }}MB</p>
                <p>GC Frequency: {{ metrics_summary.memory.gc_per_hour }}/hour</p>
            </div>

            <div class="metric">
                <h4>TPS Performance</h4>
                <p>Average TPS: {{ metrics_summary.tps.average }}</p>
                <p>Time Above 19.5 TPS: {{ metrics_summary.tps.stability_percent }}%</p>
                <p>Worst 5-minute Average: {{ metrics_summary.tps.worst_5min }}</p>
            </div>

            <h3>Performance Changes</h3>
            {% for change in performance_changes %}
            <div class="metric {{ change.type }}">
                <h4>{{ change.metric }}</h4>
                <p>{{ change.description }}</p>
                <p>Impact: {{ change.impact_percent }}%</p>
            </div>
            {% endfor %}

            <h3>Recommendations</h3>
            <ul>
            {% for recommendation in recommendations %}
                <li>{{ recommendation }}</li>
            {% endfor %}
            </ul>
        </body>
        </html>
        """
        return Template(template_content)
```

## Performance Regression Alert System

### Alert Configuration
```yaml
# alerts/performance_alerts.yml
alert_rules:
  chunk_loading_regression:
    metric: "chunk_loading_time_ms"
    threshold_percent: 10
    severity: "high"
    notification_channels: ["slack", "email"]

  memory_usage_increase:
    metric: "memory_usage_mb"
    threshold_percent: 15
    severity: "medium"
    notification_channels: ["slack"]

  tps_degradation:
    metric: "tps_average"
    threshold_value: 18.0
    comparison: "less_than"
    severity: "critical"
    notification_channels: ["slack", "email", "pagerduty"]

  cache_efficiency_drop:
    metric: "cache_hit_ratio"
    threshold_percent: -10
    severity: "medium"
    notification_channels: ["slack"]

notification_channels:
  slack:
    webhook_url: "${SLACK_WEBHOOK_URL}"
    channel: "#performance-alerts"

  email:
    smtp_server: "smtp.company.com"
    recipients: ["qa-team@company.com", "devops@company.com"]

  pagerduty:
    integration_key: "${PAGERDUTY_INTEGRATION_KEY}"
```

### Regression Response Procedures

#### Automated Response Workflow
```bash
#!/bin/bash
# scripts/regression_response.sh

REGRESSION_TYPE=$1
SEVERITY=$2
METRIC_VALUE=$3
BASELINE_VALUE=$4

echo "Performance regression detected:"
echo "Type: $REGRESSION_TYPE"
echo "Severity: $SEVERITY"
echo "Current: $METRIC_VALUE"
echo "Baseline: $BASELINE_VALUE"

case "$SEVERITY" in
    "critical")
        # Critical regressions require immediate action
        echo "CRITICAL regression - initiating emergency response"

        # Create emergency issue
        gh issue create --title "CRITICAL: Performance Regression in $REGRESSION_TYPE" \
                       --body "Automated detection of critical performance regression" \
                       --label "critical,performance,regression"

        # Trigger emergency deployment rollback if in production
        if [ "$ENVIRONMENT" = "production" ]; then
            echo "Triggering automatic rollback..."
            gh workflow run emergency-rollback.yml
        fi
        ;;

    "high")
        # High severity requires quick investigation
        echo "HIGH severity regression - creating urgent issue"

        gh issue create --title "HIGH: Performance Regression in $REGRESSION_TYPE" \
                       --body "Performance degradation detected requiring investigation" \
                       --label "high-priority,performance,regression"
        ;;

    "medium")
        # Medium severity requires investigation within SLA
        echo "MEDIUM severity regression - creating tracking issue"

        gh issue create --title "Performance Regression in $REGRESSION_TYPE" \
                       --body "Performance degradation detected" \
                       --label "performance,regression"
        ;;
esac

# Update performance tracking dashboard
python3 scripts/update_performance_dashboard.py \
    --regression-type "$REGRESSION_TYPE" \
    --severity "$SEVERITY" \
    --current-value "$METRIC_VALUE" \
    --baseline-value "$BASELINE_VALUE"
```

## Performance Regression Recovery

### Regression Analysis Tools
```python
#!/usr/bin/env python3
# tools/regression_analyzer.py

import git
import json
from datetime import datetime, timedelta
from typing import List, Dict, Optional

class RegressionAnalyzer:
    def __init__(self, repo_path: str):
        self.repo = git.Repo(repo_path)

    def identify_regression_commit(self,
                                  regression_date: datetime,
                                  metric_name: str) -> Optional[str]:
        """Identify the commit that introduced a performance regression"""

        # Get commits around the regression date
        since_date = regression_date - timedelta(days=7)
        until_date = regression_date + timedelta(days=1)

        commits = list(self.repo.iter_commits(
            since=since_date.strftime('%Y-%m-%d'),
            until=until_date.strftime('%Y-%m-%d')
        ))

        # Analyze commits for performance-related changes
        suspect_commits = []

        for commit in commits:
            if self._is_performance_related(commit):
                performance_impact = self._analyze_commit_impact(commit, metric_name)
                suspect_commits.append({
                    'commit': commit.hexsha,
                    'date': commit.committed_datetime,
                    'message': commit.message,
                    'impact_score': performance_impact
                })

        # Return most likely regression commit
        if suspect_commits:
            suspect_commits.sort(key=lambda x: x['impact_score'], reverse=True)
            return suspect_commits[0]['commit']

        return None

    def _is_performance_related(self, commit) -> bool:
        """Check if commit contains performance-related changes"""
        performance_keywords = [
            'chunk', 'memory', 'cache', 'performance', 'optimization',
            'loading', 'allocation', 'gc', 'heap'
        ]

        # Check commit message
        message_lower = commit.message.lower()
        for keyword in performance_keywords:
            if keyword in message_lower:
                return True

        # Check modified files
        for item in commit.stats.files:
            if any(path in item for path in ['chunk', 'cache', 'memory', 'performance']):
                return True

        return False
```

This comprehensive regression testing framework ensures that the World/Chunk Performance Enhancement optimizations maintain their effectiveness over time and provides rapid detection and response to any performance degradations.