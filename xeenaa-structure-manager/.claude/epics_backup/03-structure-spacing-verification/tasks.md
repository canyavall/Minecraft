# Epic 03: Structure Spacing Verification & Integration - Technical Tasks

**Created**: 2025-10-24
**Status**: READY
**Requirements**: See requirements.md

## Task Overview

Total tasks: 6
- TODO: 3
- IN_PROGRESS: 0
- COMPLETED: 3

**Task Phases**:
- **Phase 1: Foundation** (TASK-001) - Lazy classification implementation
- **Phase 2: Integration** (TASK-002 to TASK-003) - Connect classification to multiplier system
- **Phase 3: Verification** (TASK-004 to TASK-005) - Verify multipliers work correctly
- **Phase 4: Polish** (TASK-006) - Performance validation and optimization

---

## TASK-001: Implement Lazy Classification System
**Status**: COMPLETED ✅
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 3-4 hours
**Feature**: Feature 1 - Lazy Classification System

**Description**:
Replace upfront classification (569 structures at world load) with on-demand lazy classification that only classifies structures when they're first placed during world generation.

**Goal**:
Eliminate the 7+ minute world load hang caused by synchronous NBT loading while maintaining full classification functionality.

**Requirements**:
- [ ] Remove `ClassificationSystem.classifyAll()` call from `StructureManagerMod.scanStructures()`
- [ ] Add lazy classification trigger in `StructurePlacementCalculatorMixin.transformStructureSets()`
- [ ] Check `ClassificationCache` first, only classify on cache miss
- [ ] Ensure thread-safe cache access (already implemented with ConcurrentHashMap)
- [ ] Log classification events at INFO level: "Classified {id} as {SIZE} {TYPE}"
- [ ] Follows coding-standards skill
- [ ] Follows minecraft-performance skill (lazy loading pattern)

**Guidelines and Resources**:
- `coding-standards skill` - Thread safety, logging
- `minecraft-performance skill` - Lazy loading patterns
- `minecraft-modding skill` - Thread safety in world generation
- `structure-classification skill` - Classification system architecture
- `xeenaa-structure-manager/.claude/epics/03-structure-spacing-verification/requirements.md` - Feature 1 requirements
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/StructureManagerMod.java:160` - Line to modify
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/mixin/StructurePlacementCalculatorMixin.java` - Where to add classification call

**Implementation Notes**:
```java
// In StructureManagerMod.scanStructures()
// REMOVE or comment out line 160:
// initializeClassificationSystem(world, structureRegistry);

// In StructurePlacementCalculatorMixin.transformStructureSets()
// ADD lazy classification before looking up from cache:
private static Stream<RegistryEntry<StructureSet>> transformStructureSets(...) {
    return stream.map(entry -> {
        StructureSet set = entry.value();

        for (StructureReference ref : set.structures()) {
            Identifier id = extractStructureId(ref);

            // Lazy classification: Check cache first
            StructureClassification classification =
                ClassificationSystem.getInstance().getCache().get(id);

            if (classification == null) {
                // Cache miss - classify now
                classification = ClassificationSystem.getInstance()
                    .classifyStructure(id, ref.structure());

                LOGGER.info("Classified {} as {} {}",
                    id, classification.size(), classification.type());
            }

            // Now use classification for multiplier lookup...
        }

        return entry;
    });
}
```

**Acceptance Criteria**:
- [ ] World loads in <10 seconds with 569 structures (no classification happens)
- [ ] First placement of each structure triggers classification
- [ ] Subsequent placements use cached results (no re-classification)
- [ ] Console logs: "Classified minecraft:village_plains as LARGE TOWN" on first placement
- [ ] No threading issues or exceptions during world generation
- [ ] Cache hit rate >95% after exploring for 10 minutes
- [ ] Code compiles without errors
- [ ] Follows all coding standards

**Dependencies**:
- None (foundation task, uses existing ClassificationSystem)

**Blockers**:
- None

**Notes**:
- ClassificationSystem.classifyStructure() method needs to be created (currently only classifyAll() exists)
- This is synchronous lazy loading - classification happens on first access, blocking that structure's placement
- Thread-safe because ConcurrentHashMap.computeIfAbsent() is atomic
- Performance impact: <5ms per structure (acceptable for first placement)

---

## TASK-002: Add ClassificationSystem.classifyStructure() Method
**Status**: COMPLETED ✅
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 1-2 hours
**Feature**: Feature 1 - Lazy Classification System

**Description**:
Extract the single-structure classification logic from `classifyAll()` into a new `classifyStructure()` method that can be called for lazy classification.

**Goal**:
Provide a public method to classify a single structure on-demand, with automatic cache storage.

**Requirements**:
- [ ] Create `public StructureClassification classifyStructure(Identifier id, Structure structure)` method
- [ ] Method should: classify SIZE, classify TYPE, create StructureClassification record
- [ ] Automatically store result in cache (cache.put())
- [ ] Return the classification
- [ ] Thread-safe implementation
- [ ] Comprehensive Javadoc
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `coding-standards skill` - Method design, documentation
- `java-development skill` - Method extraction patterns
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/classification/ClassificationSystem.java` - File to modify

**Implementation Notes**:
```java
/**
 * Classifies a single structure and stores the result in the cache.
 * <p>
 * This method is thread-safe and can be called from multiple world generation
 * threads simultaneously. If the structure is already classified, this method
 * will return the cached result without re-classification.
 *
 * @param id the structure identifier
 * @param structure the structure to classify
 * @return the classification result
 */
public StructureClassification classifyStructure(Identifier id, Structure structure) {
    // Check cache first (avoid redundant classification)
    StructureClassification cached = cache.get(id);
    if (cached != null) {
        return cached;
    }

    try {
        // Classify SIZE
        SizeCategory size = sizeClassifier.classify(id, structure);
        int volume = sizeClassifier.getVolume(id, structure);

        // Classify TYPE
        TypeCategory type = typeClassifier.classify(id, structure);
        boolean underground = typeClassifier.isUnderground(id, structure);

        // Create classification record
        StructureClassification classification =
            new StructureClassification(size, type, volume, underground, id);

        // Store in cache
        cache.put(id, classification);

        return classification;

    } catch (Exception e) {
        LOGGER.error("Failed to classify structure {}: {}", id, e.getMessage(), e);

        // Return safe fallback
        return new StructureClassification(
            SizeCategory.MEDIUM,
            TypeCategory.UNKNOWN,
            10_000,
            false,
            id
        );
    }
}
```

**Acceptance Criteria**:
- [ ] Method successfully classifies structures on-demand
- [ ] Results are automatically cached
- [ ] Thread-safe (no race conditions)
- [ ] Error handling with fallback classification
- [ ] Comprehensive Javadoc
- [ ] Code compiles without errors
- [ ] Follows all coding standards

**Dependencies**:
- TASK-001 (needed for lazy classification integration)

**Blockers**:
- None

**Notes**:
- Extract logic from existing classifyAll() method
- Cache check prevents redundant classification
- Fallback ensures system never crashes from classification errors

---

## TASK-003: Enable ConfigManager and Verify Multiplier Integration
**Status**: COMPLETED ✅
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 1-2 hours
**Feature**: Feature 1 - Lazy Classification System (Integration)

**Description**:
Verify that ConfigManager is properly initialized and that the multiplier application system (TASK-008 from Epic 01) is ready to use the lazy classification results.

**Goal**:
Ensure the complete pipeline works: lazy classification → cache lookup → config multiplier query → spacing modification.

**Requirements**:
- [ ] Verify ConfigManager.getInstance() is called during mod initialization
- [ ] Verify config file is generated with default values
- [ ] Verify StructurePlacementCalculatorMixin queries ConfigManager correctly
- [ ] Test priority system: Structure > Type > Size > Global
- [ ] Add debug logging for multiplier application
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `coding-standards skill` - Integration testing
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/config/ConfigManager.java` - Config system
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/mixin/StructurePlacementCalculatorMixin.java` - Multiplier application

**Implementation Notes**:
```java
// In StructurePlacementCalculatorMixin (should already exist from Epic 01):
double multiplier = ConfigManager.getInstance().getSpacingMultiplier(
    id,
    classification.size(),
    classification.type()
);

if (multiplier != 1.0) {
    applyMultiplier(set, multiplier);
    LOGGER.info("Applied {}x spacing to {} (SIZE={}, TYPE={})",
        multiplier, id, classification.size(), classification.type());
}
```

**Acceptance Criteria**:
- [ ] Config file auto-generates on first run at `config/xeenaa-structure-manager.toml`
- [ ] ConfigManager returns correct multipliers based on priority
- [ ] Mineshafts get 1.0x multiplier (type.mineshaft = 1.0)
- [ ] Villages get 2.0x multiplier (type.town = 2.0 in Balanced preset)
- [ ] Console logs show which multipliers are applied
- [ ] Code compiles without errors
- [ ] Follows all coding standards

**Dependencies**:
- TASK-001 (lazy classification must work first)
- TASK-002 (classifyStructure method must exist)

**Blockers**:
- None

**Notes**:
- This task verifies existing Epic 01 code works with lazy classification
- Most code should already exist from TASK-008 of Epic 01
- Focus on integration testing and debug logging

---

## TASK-004: Enhance /xeenaa stats for Multiplier Verification
**Status**: TODO
**Priority**: High
**Assigned Agent**: implementation-agent
**Estimated Effort**: 2-3 hours
**Feature**: Feature 2 - Multiplier Application Verification

**Description**:
Enhance the `/xeenaa stats` command to show expected vs actual spacing analysis, proving that config multipliers are correctly applied to structure spacing.

**Goal**:
Provide users with clear evidence that the mod is working correctly by comparing actual spacing (from PlacementTracker) to expected spacing (from config multipliers).

**Requirements**:
- [ ] Calculate expected spacing from vanilla baseline × config multiplier
- [ ] Compare actual spacing (from PlacementTracker) to expected spacing
- [ ] Show results in `/xeenaa stats` output
- [ ] Flag discrepancies >20% for investigation
- [ ] Include per-structure-type analysis
- [ ] Follows coding-standards skill
- [ ] Follows logging-strategy skill (clear, actionable output)

**Guidelines and Resources**:
- `coding-standards skill` - Algorithm design
- `logging-strategy skill` - User-facing output
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/tracking/PlacementTracker.java` - Placement data source
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/config/ConfigManager.java` - Multiplier source

**Implementation Notes**:
```java
// In PlacementTracker.logDetailedStatistics():
// Add multiplier verification section

LOGGER.info("=== Multiplier Verification ===\");

for (Identifier structureId : getTrackedStructures()) {
    List<PlacementRecord> records = getPlacementsForStructure(structureId);

    if (records.size() < 3) {
        continue; // Need at least 3 placements for meaningful analysis
    }

    // Get classification
    StructureClassification classification =
        ClassificationSystem.getInstance().getCache().get(structureId);

    if (classification == null) {
        continue; // Not yet classified
    }

    // Get configured multiplier
    double configMultiplier = ConfigManager.getInstance().getSpacingMultiplier(
        structureId,
        classification.size(),
        classification.type()
    );

    // Calculate actual spacing
    double actualSpacing = calculateAverageSpacing(records);

    // Assume vanilla baseline (varies by structure, use estimates)
    double vanillaSpacing = estimateVanillaSpacing(structureId);
    double expectedSpacing = vanillaSpacing * configMultiplier;

    // Calculate deviation
    double deviation = Math.abs(actualSpacing - expectedSpacing) / expectedSpacing;

    String status = deviation < 0.2 ? "✓ PASS" : "⚠ WARN";

    LOGGER.info("{}: Multiplier={}x, Expected={} chunks, Actual={} chunks, Deviation={}% {}",
        structureId,
        String.format("%.1f", configMultiplier),
        String.format("%.1f", expectedSpacing),
        String.format("%.1f", actualSpacing),
        String.format("%.0f", deviation * 100),
        status);
}
```

**Acceptance Criteria**:
- [ ] `/xeenaa stats` shows expected vs actual spacing for each structure type
- [ ] Villages with 2.0x multiplier show ~2x vanilla spacing
- [ ] Mineshafts with 1.0x multiplier show vanilla spacing
- [ ] Discrepancies >20% are flagged with ⚠ WARN
- [ ] Output is clear and actionable
- [ ] Works with at least 3 placements per structure type
- [ ] Code compiles without errors
- [ ] Follows all coding standards

**Dependencies**:
- TASK-003 (multiplier system must be working)

**Blockers**:
- None

**Notes**:
- Vanilla spacing baselines are estimates (exact values vary by structure)
- 20% deviation threshold is reasonable (world gen has RNG)
- May need to research vanilla spacing values for common structures

---

## TASK-005: Config Preset Testing and Documentation
**Status**: TODO
**Priority**: Medium
**Assigned Agent**: implementation-agent
**Estimated Effort**: 2-3 hours
**Feature**: Feature 3 - Config Integration Testing

**Description**:
Test all three config presets (Sparse, Balanced, Abundant) to verify they produce measurably different spacing, and document expected spacing ranges.

**Goal**:
Verify preset system works correctly and create reference documentation for users showing what to expect from each preset.

**Requirements**:
- [ ] Test Sparse preset (spacing_multiplier = 2.5x globally)
- [ ] Test Balanced preset (spacing_multiplier = 1.0x globally, type overrides apply)
- [ ] Test Abundant preset (spacing_multiplier = 0.8x globally)
- [ ] Generate worlds with each preset and collect spacing data
- [ ] Verify multiplier compounding works (global × type/size)
- [ ] Document expected spacing ranges for each preset
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `coding-standards skill` - Documentation standards
- `xeenaa-structure-manager/config/xeenaa-structure-manager.toml` - Config file
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/config/PresetType.java` - Preset definitions

**Implementation Notes**:
Testing procedure:
1. Set preset to "sparse" in config
2. Create new world, explore for 10 minutes
3. Run `/xeenaa stats` and record spacing data
4. Delete world
5. Repeat for "balanced" and "abundant" presets
6. Compare results

**Acceptance Criteria**:
- [ ] Sparse preset: Avg spacing >2x vanilla for most structures
- [ ] Balanced preset: Avg spacing ~1.5x vanilla (type overrides vary)
- [ ] Abundant preset: Avg spacing <1x vanilla for most structures
- [ ] Switching presets produces measurably different spacing (>30% difference)
- [ ] Multiplier compounding works correctly (global × type multiplier)
- [ ] Documentation created in `.claude/epics/03-structure-spacing-verification/preset-results.md`
- [ ] Results match expectations from requirements.md
- [ ] Follows all coding standards

**Dependencies**:
- TASK-004 (enhanced stats command for analysis)

**Blockers**:
- None

**Notes**:
- This is primarily a testing and documentation task
- User validation is critical (manual gameplay testing)
- Results will inform preset tuning in future epics

---

## TASK-006: Performance Validation and Optimization
**Status**: TODO
**Priority**: Medium
**Assigned Agent**: implementation-agent
**Estimated Effort**: 1-2 hours
**Feature**: Feature 4 - Performance Validation

**Description**:
Validate that lazy classification and multiplier application don't impact world generation performance compared to vanilla Minecraft.

**Goal**:
Prove the mod has no negative performance impact and meets all performance targets from requirements.md.

**Requirements**:
- [ ] Add timing logs for each classification event
- [ ] Measure memory usage of classification cache
- [ ] Verify classification time <5ms per structure
- [ ] Verify cache memory <1KB per entry (<569KB total)
- [ ] Monitor world generation FPS (no regression)
- [ ] Use Spark Profiler for CPU analysis if available
- [ ] Follows minecraft-performance skill
- [ ] Follows performance-testing skill

**Guidelines and Resources**:
- `minecraft-performance skill` - Performance optimization
- `performance-testing skill` - Performance measurement methodology
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/classification/ClassificationSystem.java` - Add timing logs
- `xeenaa-structure-manager/src/main/java/com/canya/xeenaa_structure_manager/tracking/PlacementTracker.java` - Already has performance monitoring

**Implementation Notes**:
```java
// In ClassificationSystem.classifyStructure():
long startTime = System.nanoTime();

// ... classification logic ...

long duration = System.nanoTime() - startTime;
double durationMs = duration / 1_000_000.0;

if (durationMs > 5.0) {
    LOGGER.warn("Classification took {}ms for {} (target <5ms)",
        String.format("%.2f", durationMs), id);
} else {
    LOGGER.debug("Classification took {}ms for {}",
        String.format("%.2f", durationMs), id);
}
```

**Acceptance Criteria**:
- [ ] Classification: <5ms per structure (logged and verified)
- [ ] Memory: <1KB per cache entry (<569KB total, verified via cache.size() × 1024)
- [ ] No world generation FPS regression (compare F3 debug screen before/after)
- [ ] No lag spikes during exploration
- [ ] No performance warnings in logs
- [ ] PlacementTracker shows healthy memory usage (<50MB additional)
- [ ] Spark Profiler shows <1% CPU time spent in classification (if tested)
- [ ] Code compiles without errors
- [ ] Follows all coding standards

**Dependencies**:
- TASK-001 (lazy classification must be implemented)
- TASK-003 (multiplier system must be working)

**Blockers**:
- None

**Notes**:
- Most performance monitoring already exists in PlacementTracker
- Focus on classification timing and cache memory
- Performance testing should be done with 569 structures from 13 mods

---

## Task Dependencies Graph

```
TASK-001 (Lazy Classification)
  ↓
TASK-002 (classifyStructure Method)
  ↓
TASK-003 (Enable Config & Verify Integration)
  ↓
├─→ TASK-004 (Enhance /xeenaa stats)
│     ↓
│   TASK-005 (Preset Testing)
│
└─→ TASK-006 (Performance Validation)
```

**Critical Path**: TASK-001 → TASK-002 → TASK-003 → TASK-004 → TASK-005

**Parallel Opportunities**: TASK-006 can be done in parallel with TASK-005 after TASK-003

---

## Success Metrics

**Epic 03 is complete when**:
1. ✅ World loads in <10 seconds (vs 7+ minutes with upfront classification)
2. ✅ Classification happens on-demand during gameplay
3. ✅ `/xeenaa stats` shows multipliers are correctly applied (expected vs actual)
4. ✅ Villages are measurably further apart (spacing analysis proves it)
5. ✅ All three presets produce different spacing (documented in preset-results.md)
6. ✅ No performance regression during gameplay (<5ms classification, <569KB memory)
7. ✅ User can see the mod is working (logs + stats command + gameplay)

**When to move to Epic 04**:
- All 6 tasks completed
- User manually validates: structures are correctly spaced in-game
- Performance metrics meet targets
- Config presets work as expected

---

## Notes

**Skills Referenced**:
- `coding-standards skill` - All tasks
- `minecraft-performance skill` - TASK-001, TASK-006
- `performance-testing skill` - TASK-006
- `logging-strategy skill` - TASK-004
- `structure-classification skill` - TASK-001, TASK-002
- `java-development skill` - TASK-002

**Research Referenced**:
- None required (all systems already implemented in Epic 01)

**Testing Strategy**:
- Each task should compile and run without errors
- User manually tests features after implementation
- validation-agent creates automated tests AFTER user validation (future epic)
- Focus on working code first, automated tests later

**Risk Mitigation**:
- Lazy classification may be slow → Add timing logs to detect
- Cache miss rate may be high → Monitor in testing
- Multipliers may not apply → Enhanced debug logging in TASK-003
- Threading issues → Already mitigated with ConcurrentHashMap

**Estimated Total Effort**: 11-16 hours (slightly higher than initial 8-12 hour estimate due to task breakdown)
