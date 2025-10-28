# Performance Analysis Reference

**Universal knowledge has been migrated to centralized knowledge base.**

## Centralized Knowledge

See `.claude/knowledge/performance/profiling.md` for:
- Complete comparison of profiling tools (Spark, JProfiler, JMH, etc.)
- Fabric-specific performance mods (C2ME, Lithium, Sodium, ScalableLux)
- Tool recommendations for different use cases
- Performance impact assessments
- Profiling workflows and best practices

**Tags to search**: `performance`, `profiling`, `spark`, `jvm`, `benchmarking`, `tools`, `optimization`, `monitoring`

## Project-Specific Notes for xeenaa-fabric-enhancements

### Our Tool Stack

**Primary Tools**:
- Spark - Real-time performance monitoring
- JMH - Benchmarking optimizations
- Eclipse MAT - Memory leak detection

**Performance Mods for Testing**:
- Lithium - Baseline comparison
- C2ME - Chunk generation testing
- ScalableLux - Lighting comparison

### Project Requirements

**CRITICAL**: ALL optimizations must show measurable improvement
- Before/after benchmarks MANDATORY
- Use JMH for rigorous testing
- Document performance gains in research files

### Benchmarking Template

```java
@Benchmark
public void testOptimization() {
    // Optimization to test
}

@Benchmark
public void testBaseline() {
    // Vanilla/baseline code
}
```

### Performance Testing Checklist

- [ ] Baseline measurements with Spark
- [ ] JMH benchmark suite created
- [ ] Before/after comparison documented
- [ ] Memory impact measured
- [ ] Real-world testing in development client
- [ ] Comparison vs Lithium/C2ME (if applicable)

---

**Archived Research** (now in centralized knowledge):
- ~~performance-tools-comparison.md~~ → `.claude/knowledge/performance/profiling.md`
