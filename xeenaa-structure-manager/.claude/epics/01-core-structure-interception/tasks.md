# Epic 01: Core Structure Interception & Auto-Classification - Technical Tasks

**Created**: 2025-10-22
**Status**: READY
**Requirements**: See requirements.md

## Task Overview

Total tasks: 9
- TODO: 8
- IN_PROGRESS: 0
- COMPLETED: 1

**Task Phases**:
- **Phase 1: Foundation** (TASK-001 to TASK-003) - Registry scanning, data structures, caching
- **Phase 2: Classification** (TASK-004 to TASK-005) - SIZE and TYPE detection engines
- **Phase 3: Configuration** (TASK-006) - TOML config system with presets
- **Phase 4: Integration** (TASK-007 to TASK-008) - Mixin interception and multiplier application
- **Phase 5: Polish** (TASK-009) - Performance optimization and benchmarking

---

## TASK-001: Structure Registry Scanner and World Load Hook
**Status**: COMPLETED
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 3-4 hours
**Feature**: Feature 1 - Structure Registry Scanner

**Description**:
Create the foundational system that scans all registered structures when a world loads. This is the entry point for the entire classification system.

**Goal**:
Discover and catalog ALL structures from vanilla, mods, and datapacks by hooking into world load events and querying the structure registry.

**Requirements**:
- [x] Hook into `ServerWorldEvents.LOAD` (Fabric API event)
- [x] Access `DynamicRegistryManager.get(RegistryKeys.STRUCTURE)`
- [x] Only scan on Overworld dimension (skip Nether/End)
- [x] Iterate through all registered structures
- [x] Log total structure count to console
- [x] Collect structure IDs and references for classification
- [x] Follows coding-standards skill

**Guidelines and Resources**:
- `coding-standards skill` - Java 21 patterns, naming conventions
- `fabric-modding skill` - Fabric events system
- `minecraft-modding skill` - Registry access patterns
- `structure-classification skill` - Registry scanning best practices
- `xeenaa-structure-manager/.claude/epics/01-core-structure-interception/requirements.md` - Feature 1 requirements
- `xeenaa-structure-manager/.claude/research/structure-management-implementation-approaches.md` - Registry access details

**Implementation Notes**:
```java
// Entry point in StructureManagerMod.java
@Override
public void onInitialize() {
    ServerWorldEvents.LOAD.register((server, world) -> {
        if (world.getRegistryKey() == World.OVERWORLD) {
            scanStructures(world);
        }
    });
}

private void scanStructures(ServerWorld world) {
    DynamicRegistryManager registryManager = world.getRegistryManager();
    Registry<Structure> structures = registryManager.get(RegistryKeys.STRUCTURE);

    LOGGER.info("Scanning structures...");
    int count = structures.getEntrySet().size();
    LOGGER.info("Discovered {} structures", count);

    // Pass to classification system (TASK-002)
}
```

**Acceptance Criteria**:
- [x] Code compiles without errors
- [x] Event fires on world load (Overworld only)
- [x] Successfully accesses structure registry
- [x] Console logs: "Xeenaa: Discovered X structures from Y mods"
- [x] Count includes vanilla + mods + datapacks
- [x] Completes in <1 second for 1000 structures
- [x] Follows all coding standards

**Dependencies**:
- None (foundation task)

**Blockers**:
- None

**Notes**:
- This task establishes the entry point for the entire system
- Must work with datapacks (loaded at world time, not mod init time)
- Overworld-only scope for Epic 01 (Nether/End in future)

---

## TASK-002: Classification Data Structures and Caching System
**Status**: TODO
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 3-4 hours
**Feature**: Feature 2 - Two-Dimensional Auto-Classification

**Description**:
Create the core data structures for storing two-dimensional classification results (SIZE + TYPE) and implement a thread-safe caching system.

**Goal**:
Define Java records and enums for SIZE/TYPE categories, and create a ConcurrentHashMap-based cache that stores classification results permanently.

**Requirements**:
- [ ] Create `SizeCategory` enum (SMALL, MEDIUM, LARGE)
- [ ] Create `TypeCategory` enum (TOWN, DUNGEON, TEMPLE, MINESHAFT, SKY, RUIN, UNKNOWN)
- [ ] Create `StructureClassification` record (size, type, volume, underground, id)
- [ ] Implement thread-safe cache (ConcurrentHashMap)
- [ ] Provide cache lookup methods
- [ ] Add cache statistics logging
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `coding-standards skill` - Java 21 records, enums
- `java-development skill` - Records and sealed classes
- `structure-classification skill` - Two-dimensional classification system
- `minecraft-performance skill` - Caching strategies
- Research: `structure-management-implementation-approaches.md` - Classification cache design

**Implementation Notes**:
```java
package com.canya.xeenaa_structure_manager.classification;

public enum SizeCategory {
    SMALL(0, 4_096),
    MEDIUM(4_096, 32_768),
    LARGE(32_768, Integer.MAX_VALUE);

    private final int minVolume;
    private final int maxVolume;

    // ... implementation
}

public enum TypeCategory {
    TOWN, DUNGEON, TEMPLE, MINESHAFT, SKY, RUIN, UNKNOWN
}

public record StructureClassification(
    SizeCategory size,
    TypeCategory type,
    int volume,
    boolean underground,
    Identifier id
) {}

public class ClassificationCache {
    private final Map<Identifier, StructureClassification> cache =
        new ConcurrentHashMap<>();

    public StructureClassification get(Identifier id) {
        return cache.get(id);
    }

    public void put(Identifier id, StructureClassification classification) {
        cache.put(id, classification);
    }
}
```

**Acceptance Criteria**:
- [ ] All enums and records compile
- [ ] Records use Java 21 features
- [ ] Cache is thread-safe (ConcurrentHashMap)
- [ ] Cache methods are well-documented
- [ ] Logging shows cache hit/miss statistics
- [ ] Memory-efficient (< 1KB per entry)
- [ ] Follows all coding standards

**Dependencies**:
- TASK-001 (provides structure IDs)

**Blockers**:
- None

**Notes**:
- Use Java 21 records (immutable, concise)
- ConcurrentHashMap for thread safety during parallel classification
- Cache never invalidates (structures don't change at runtime)

---

## TASK-003: SIZE Classification Engine (Bounding Box Analysis)
**Status**: TODO
**Priority**: High
**Assigned Agent**: implementation-agent
**Estimated Effort**: 4-5 hours
**Feature**: Feature 2 - Two-Dimensional Auto-Classification (SIZE dimension)

**Description**:
Implement the SIZE dimension classifier that calculates bounding box volume from NBT templates and categorizes structures as Small, Medium, or Large.

**Goal**:
Given a Structure, calculate its 3D volume and assign it to a SIZE category. Handle NBT unavailability gracefully with fallback estimates.

**Requirements**:
- [ ] Access StructureTemplateManager
- [ ] Extract NBT bounding box (BlockBox)
- [ ] Calculate volume (width × height × depth)
- [ ] Categorize using SizeCategory thresholds
- [ ] Fallback estimation when NBT unavailable
- [ ] Cache results to avoid recomputation
- [ ] Follows coding-standards skill
- [ ] Follows structure-classification skill

**Guidelines and Resources**:
- `structure-classification skill` - SIZE classification methods
- `minecraft-modding skill` - NBT template access
- `coding-standards skill` - Error handling
- Research: `structure-management-implementation-approaches.md` - Bounding box calculation

**Implementation Notes**:
```java
package com.canya.xeenaa_structure_manager.classification;

public class SizeClassifier {
    private final StructureTemplateManager templateManager;

    public SizeCategory classify(Identifier id, Structure structure) {
        int volume = calculateVolume(id, structure);
        return SizeCategory.fromVolume(volume);
    }

    private int calculateVolume(Identifier id, Structure structure) {
        // 1. Try NBT template
        StructureTemplate template = getTemplate(id);
        if (template != null) {
            BlockBox bounds = template.getBoundingBox();
            return bounds.getBlockCountX() *
                   bounds.getBlockCountY() *
                   bounds.getBlockCountZ();
        }

        // 2. Fallback estimation
        LOGGER.warn("NBT unavailable for {}, using estimate", id);
        return estimateVolume(id);
    }

    private int estimateVolume(Identifier id) {
        String name = id.getPath();
        // Heuristic estimates
        if (name.contains("mansion")) return 100_000;  // Large
        if (name.contains("village")) return 50_000;   // Large
        if (name.contains("temple")) return 20_000;    // Medium
        if (name.contains("ruin")) return 2_000;       // Small
        return 10_000;  // Default Medium
    }
}
```

**Acceptance Criteria**:
- [ ] Successfully accesses NBT templates when available
- [ ] Correctly calculates 3D volume
- [ ] Assigns correct SIZE categories
- [ ] Handles missing NBT gracefully (no crashes)
- [ ] Logs warnings when using fallback estimates
- [ ] Vanilla structures classified correctly (test against known list)
- [ ] Results are cached
- [ ] Follows all coding standards

**Dependencies**:
- TASK-002 (needs SizeCategory enum and cache)

**Blockers**:
- None

**Notes**:
- Not all structures have NBT templates (some are JSON-only)
- Fallback estimates should be conservative
- Test with vanilla structures first (known sizes)

---

## TASK-004: TYPE Classification Engine (Heuristic Detection)
**Status**: TODO
**Priority**: High
**Assigned Agent**: implementation-agent
**Estimated Effort**: 5-6 hours
**Feature**: Feature 2 - Two-Dimensional Auto-Classification (TYPE dimension)

**Description**:
Implement the TYPE dimension classifier using heuristic detection (name analysis, Y-level constraints, bounding box shape) to categorize structures by behavioral type.

**Goal**:
Classify structures into Town, Dungeon, Temple, Mineshaft, Sky, Ruin, or Unknown using 3 detection methods in priority order.

**Requirements**:
- [ ] Name-based keyword matching (primary, 80% accuracy target)
- [ ] Y-level constraint analysis (secondary)
- [ ] Bounding box shape analysis for linear structures (tertiary)
- [ ] Handle all TypeCategory values
- [ ] Provide TypeCategory.UNKNOWN fallback
- [ ] Log detected type for transparency
- [ ] Follows coding-standards skill
- [ ] Follows structure-classification skill

**Guidelines and Resources**:
- `structure-classification skill` - TYPE classification heuristics
- `minecraft-modding skill` - Structure metadata access
- `coding-standards skill` - Pattern matching
- Research: `structure-management-implementation-approaches.md` - Type detection methods

**Implementation Notes**:
```java
package com.canya.xeenaa_structure_manager.classification;

public class TypeClassifier {

    public TypeCategory classify(Identifier id, Structure structure) {
        // 1. Name-based detection (primary)
        TypeCategory fromName = detectFromName(id);
        if (fromName != TypeCategory.UNKNOWN) {
            return fromName;
        }

        // 2. Y-level analysis (secondary)
        TypeCategory fromY = detectFromYLevel(structure);
        if (fromY != TypeCategory.UNKNOWN) {
            return fromY;
        }

        // 3. Shape analysis (tertiary)
        TypeCategory fromShape = detectFromShape(structure);
        if (fromShape != TypeCategory.UNKNOWN) {
            return fromShape;
        }

        // 4. Fallback
        return TypeCategory.UNKNOWN;
    }

    private TypeCategory detectFromName(Identifier id) {
        String name = id.getPath().toLowerCase();

        if (name.contains("village") || name.contains("town"))
            return TypeCategory.TOWN;
        if (name.contains("dungeon") || name.contains("crypt"))
            return TypeCategory.DUNGEON;
        if (name.contains("temple") || name.contains("pyramid"))
            return TypeCategory.TEMPLE;
        if (name.contains("mineshaft") || name.contains("mine"))
            return TypeCategory.MINESHAFT;
        if (name.contains("sky") || name.contains("floating"))
            return TypeCategory.SKY;
        if (name.contains("ruin"))
            return TypeCategory.RUIN;

        return TypeCategory.UNKNOWN;
    }

    // ... Y-level and shape detection
}
```

**Acceptance Criteria**:
- [ ] Name-based detection works for common keywords
- [ ] Vanilla structures classified correctly (80%+ accuracy)
- [ ] Mineshafts detected correctly (TYPE=MINESHAFT)
- [ ] Sky structures detected (Y > 100)
- [ ] Underground structures detected (Y < 40)
- [ ] Unknown types handled gracefully
- [ ] Console logs detected types
- [ ] Follows all coding standards

**Dependencies**:
- TASK-002 (needs TypeCategory enum)
- TASK-003 (may use bounding box from SIZE classification)

**Blockers**:
- None

**Notes**:
- 80% accuracy is acceptable for MVP (not 100%)
- NAME detection is most reliable (use first)
- TypeCategory.UNKNOWN is safe fallback (uses SIZE rules)
- Test with popular structure mods (YUNG's, When Dungeons Arise)

---

## TASK-005: Integrated Classification System with Parallel Processing
**Status**: TODO
**Priority**: High
**Assigned Agent**: implementation-agent
**Estimated Effort**: 4-5 hours
**Feature**: Feature 2 - Two-Dimensional Auto-Classification

**Description**:
Integrate SIZE and TYPE classifiers into a unified system with parallel processing and comprehensive logging. This orchestrates TASK-003 and TASK-004.

**Goal**:
Provide a single `classifyAll()` method that runs SIZE and TYPE classification in parallel, caches results, and logs comprehensive statistics.

**Requirements**:
- [ ] Orchestrate SizeClassifier and TypeClassifier
- [ ] Parallel classification using parallelStream()
- [ ] Populate ClassificationCache with results
- [ ] Log comprehensive statistics (counts by size/type)
- [ ] Complete in <5 seconds for 1000 structures
- [ ] Handle exceptions gracefully
- [ ] Follows coding-standards skill
- [ ] Follows structure-classification skill

**Guidelines and Resources**:
- `structure-classification skill` - Parallel classification
- `minecraft-performance skill` - Parallel processing
- `coding-standards skill` - Error handling
- Research: `structure-management-implementation-approaches.md` - Performance optimization

**Implementation Notes**:
```java
package com.canya.xeenaa_structure_manager.classification;

public class ClassificationSystem {
    private final SizeClassifier sizeClassifier;
    private final TypeClassifier typeClassifier;
    private final ClassificationCache cache;

    public void classifyAll(Registry<Structure> structures) {
        LOGGER.info("Classifying structures...");
        long startTime = System.currentTimeMillis();

        // Parallel classification
        List<StructureClassification> results =
            structures.getEntrySet()
                .parallelStream()
                .map(entry -> {
                    Identifier id = entry.getKey().getValue();
                    Structure structure = entry.getValue();
                    return classify(id, structure);
                })
                .toList();

        // Cache results
        results.forEach(c -> cache.put(c.id(), c));

        // Log statistics
        long duration = System.currentTimeMillis() - startTime;
        logStatistics(results, duration);
    }

    private StructureClassification classify(Identifier id, Structure s) {
        SizeCategory size = sizeClassifier.classify(id, s);
        TypeCategory type = typeClassifier.classify(id, s);
        int volume = /* from SIZE classifier */;
        boolean underground = /* from TYPE classifier */;

        return new StructureClassification(size, type, volume, underground, id);
    }

    private void logStatistics(List<StructureClassification> results, long duration) {
        int small = (int) results.stream().filter(c -> c.size() == SizeCategory.SMALL).count();
        int medium = (int) results.stream().filter(c -> c.size() == SizeCategory.MEDIUM).count();
        int large = (int) results.stream().filter(c -> c.size() == SizeCategory.LARGE).count();

        int towns = (int) results.stream().filter(c -> c.type() == TypeCategory.TOWN).count();
        // ... other type counts

        LOGGER.info("Classified {} structures in {}ms", results.size(), duration);
        LOGGER.info("Size: {} small, {} medium, {} large", small, medium, large);
        LOGGER.info("Type: {} towns, {} dungeons, {} temples, {} mineshafts, ...", ...);
    }
}
```

**Acceptance Criteria**:
- [ ] Parallel classification completes in <5 seconds for 1000 structures
- [ ] Console shows: "Classified X structures: Y small, Z medium, W large | A towns, B dungeons..."
- [ ] All results cached in ClassificationCache
- [ ] No crashes on classification errors
- [ ] Statistics match actual results
- [ ] User can see what mod classified as what
- [ ] Follows all coding standards

**Dependencies**:
- TASK-002 (cache and data structures)
- TASK-003 (SIZE classifier)
- TASK-004 (TYPE classifier)

**Blockers**:
- None

**Notes**:
- This is the "public API" of the classification system
- Called from TASK-001's world load hook
- Parallel processing essential for performance with many mods

---

## TASK-006: Configuration System with TOML and Presets
**Status**: TODO
**Priority**: High
**Assigned Agent**: implementation-agent
**Estimated Effort**: 5-6 hours
**Feature**: Feature 3 - Configuration System with Presets

**Description**:
Create a TOML-based configuration system with three presets (Sparse, Balanced, Abundant) and two-dimensional multipliers (size.X and type.Y sections) with priority system.

**Goal**:
Auto-generate `config/xeenaa-structure-manager.toml` with working presets and provide methods to query multipliers using the priority system (Type > Size > Global).

**Requirements**:
- [ ] TOML config file generation
- [ ] Three presets: Sparse, Balanced (default), Abundant
- [ ] Global spacing/separation multipliers
- [ ] SIZE-based multipliers (size.small, size.medium, size.large)
- [ ] TYPE-based multipliers (type.town, type.dungeon, etc.)
- [ ] Structure-specific overrides
- [ ] Priority system: Structure > Type > Size > Global
- [ ] Config validation with safe defaults
- [ ] Hot-reload support (no world restart needed)
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `structure-classification skill` - Configuration priority system
- `coding-standards skill` - Configuration patterns
- Research: `structure-management-implementation-approaches.md` - Config structure

**Implementation Notes**:
```toml
# Auto-generated config file
[preset]
active = "balanced"

[global]
spacing_multiplier = 1.0
separation_multiplier = 1.0

# SIZE-based multipliers
[size.small]
spacing_multiplier = 1.2

[size.medium]
spacing_multiplier = 1.5

[size.large]
spacing_multiplier = 2.5

# TYPE-based multipliers (override size)
[type.town]
spacing_multiplier = 2.0

[type.dungeon]
spacing_multiplier = 1.8

[type.temple]
spacing_multiplier = 2.2

[type.mineshaft]
spacing_multiplier = 1.0  # CRITICAL: Don't modify mineshafts!

[type.sky]
spacing_multiplier = 1.5

[type.ruin]
spacing_multiplier = 1.3

# Manual overrides (highest priority)
[structures."minecraft:ancient_city"]
spacing = 48
separation = 16
enabled = true
```

```java
public class ConfigManager {
    public double getSpacingMultiplier(
        Identifier structureId,
        SizeCategory size,
        TypeCategory type
    ) {
        // Priority: Structure > Type > Size > Global
        if (hasStructureOverride(structureId)) {
            return getStructureSpacing(structureId);
        }

        if (type != TypeCategory.UNKNOWN && hasTypeMultiplier(type)) {
            return getTypeMultiplier(type) * globalMultiplier;
        }

        if (hasSizeMultiplier(size)) {
            return getSizeMultiplier(size) * globalMultiplier;
        }

        return globalMultiplier;
    }
}
```

**Acceptance Criteria**:
- [ ] Config file auto-generates on first run
- [ ] Presets work correctly (Sparse, Balanced, Abundant)
- [ ] SIZE multipliers apply correctly
- [ ] TYPE multipliers override SIZE correctly
- [ ] Structure overrides have highest priority
- [ ] Invalid values use safe defaults with warnings
- [ ] Config changes apply without world restart
- [ ] TOML parsing handles errors gracefully
- [ ] Follows all coding standards

**Dependencies**:
- TASK-002 (needs SizeCategory and TypeCategory)

**Blockers**:
- None

**Notes**:
- Use a TOML library (research which one Fabric mods use)
- Priority system is critical for correct behavior
- type.mineshaft = 1.0x is ESSENTIAL (don't break mineshafts!)

---

## TASK-007: Structure Placement Mixin Interception
**Status**: TODO
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 6-7 hours
**Feature**: Feature 4 - Structure Placement Interception

**Description**:
Create a mixin that injects into StructurePlacementCalculator.create() to intercept and modify structure placement configurations before they're used for world generation.

**Goal**:
Hook into Minecraft's structure generation pipeline and modify spacing/separation values in the StructureSet stream based on our configuration and classification.

**Requirements**:
- [ ] Mixin into StructurePlacementCalculator.create()
- [ ] Inject at @HEAD or suitable injection point
- [ ] Access Stream<RegistryEntry<StructureSet>>
- [ ] Modify spacing/separation for each StructureSet
- [ ] Preserve structure set relationships (exclusion zones)
- [ ] Don't break other mods' structure generation
- [ ] Follows coding-standards skill
- [ ] Follows fabric-modding skill (mixin best practices)

**Guidelines and Resources**:
- `fabric-modding skill` - Mixin patterns
- `coding-standards skill` - Mixin standards
- `structure-classification skill` - Mixin interception strategies
- Research: `structure-management-implementation-approaches.md` - Mixin injection points

**Implementation Notes**:
```java
package com.canya.xeenaa_structure_manager.mixin;

@Mixin(StructurePlacementCalculator.class)
public class StructurePlacementCalculatorMixin {

    @Inject(
        method = "create(Lnet/minecraft/world/gen/NoiseConfig;JLnet/minecraft/world/biome/source/BiomeSource;Ljava/util/stream/Stream;)Lnet/minecraft/world/gen/chunk/placement/StructurePlacementCalculator;",
        at = @At("HEAD")
    )
    private static void modifyStructureSets(
        NoiseConfig noiseConfig,
        long seed,
        BiomeSource biomeSource,
        Stream<RegistryEntry<StructureSet>> structureSets,
        CallbackInfoReturnable<StructurePlacementCalculator> cir
    ) {
        // Transform structure sets with our multipliers
        // This is the critical interception point!
    }
}
```

**Acceptance Criteria**:
- [ ] Mixin successfully injects without crashes
- [ ] Mixin fires during world generation
- [ ] Can access StructureSet stream
- [ ] Doesn't break vanilla structure generation
- [ ] Compatible with other structure mods
- [ ] Logs when mixin applies modifications
- [ ] Minimal injection surface (clean mixin)
- [ ] Follows all coding standards

**Dependencies**:
- TASK-006 (needs ConfigManager)
- TASK-005 (needs ClassificationSystem)

**Blockers**:
- None

**Notes**:
- This is the most critical technical task
- Mixin must be minimal and safe (don't break other mods)
- Test with vanilla first, then mod structures
- May need to use Accessor or Invoker for private methods

---

## TASK-008: Apply Classification-Based Multipliers to Structure Generation
**Status**: TODO
**Priority**: Critical
**Assigned Agent**: implementation-agent
**Estimated Effort**: 5-6 hours
**Feature**: Feature 4 - Structure Placement Interception

**Description**:
Implement the logic that applies config multipliers to StructureSet spacing/separation values based on classification results, using the priority system.

**Goal**:
For each structure, look up its SIZE and TYPE classification, query config for appropriate multiplier, and modify the StructureSet accordingly.

**Requirements**:
- [ ] Look up StructureClassification from cache
- [ ] Query ConfigManager with priority system
- [ ] Calculate new spacing/separation values
- [ ] Modify StructureSet in stream
- [ ] Log applied multipliers for transparency
- [ ] Handle unknown structures gracefully
- [ ] Preserve vanilla spacing when multiplier = 1.0x
- [ ] Follows coding-standards skill
- [ ] Follows structure-classification skill

**Guidelines and Resources**:
- `structure-classification skill` - Priority system and multiplier application
- `coding-standards skill` - Algorithm design
- Research: `structure-management-implementation-approaches.md` - Spacing calculation

**Implementation Notes**:
```java
private static Stream<RegistryEntry<StructureSet>> transformStructureSets(
    Stream<RegistryEntry<StructureSet>> stream
) {
    return stream.map(entry -> {
        StructureSet set = entry.value();

        // For each structure in the set
        for (StructureReference ref : set.structures()) {
            Identifier id = ref.structure().getKey().get().getValue();

            // Get classification
            StructureClassification classification =
                ClassificationSystem.getCache().get(id);

            if (classification == null) {
                LOGGER.warn("No classification for {}, skipping", id);
                continue;
            }

            // Get multiplier using priority system
            double multiplier = ConfigManager.getSpacingMultiplier(
                id,
                classification.size(),
                classification.type()
            );

            // Apply to StructureSet
            if (multiplier != 1.0) {
                applyMultiplier(set, multiplier);
                LOGGER.debug("Applied {}x spacing to {}", multiplier, id);
            }
        }

        return entry;
    });
}

private static void applyMultiplier(StructureSet set, double multiplier) {
    // Modify spacing and separation
    int newSpacing = (int) (set.placement().spacing() * multiplier);
    int newSeparation = (int) (set.placement().separation() * multiplier);

    // Ensure separation < spacing
    if (newSeparation >= newSpacing) {
        newSeparation = Math.max(1, newSpacing - 1);
    }

    // Update StructureSet (may need accessor)
}
```

**Acceptance Criteria**:
- [ ] Classification cache lookups work
- [ ] Config priority system applies correctly
- [ ] Mineshafts get 1.0x (not modified)
- [ ] Large dungeons get type.dungeon (1.8x), not size.large (2.5x)
- [ ] Unknown structures get size-based multipliers
- [ ] Console logs applied multipliers
- [ ] World generates with modified spacing
- [ ] User can verify structures are further/closer apart
- [ ] Follows all coding standards

**Dependencies**:
- TASK-007 (mixin injection point)
- TASK-005 (classification cache)
- TASK-006 (config and priority system)

**Blockers**:
- None

**Notes**:
- This is where classification and config come together
- Priority system must work correctly (test with known structures)
- Edge case: mineshafts MUST get 1.0x (verify in testing)

---

## TASK-009: Performance Optimization and Benchmarking
**Status**: TODO
**Priority**: Medium
**Assigned Agent**: implementation-agent
**Estimated Effort**: 3-4 hours
**Feature**: Feature 5 - Basic Performance Optimization

**Description**:
Optimize the classification and interception systems for performance, and add benchmarking to measure actual improvement vs vanilla.

**Goal**:
Ensure classification completes in <5 seconds for 1000 structures, add timing logs, and verify no world generation performance regression.

**Requirements**:
- [ ] Verify parallel classification works efficiently
- [ ] Add timing logs for classification phase
- [ ] Add timing logs for structure generation
- [ ] Measure memory usage of classification cache
- [ ] Ensure ConcurrentHashMap used correctly
- [ ] Add performance statistics to console
- [ ] Follows minecraft-performance skill
- [ ] Follows coding-standards skill

**Guidelines and Resources**:
- `minecraft-performance skill` - Caching and optimization
- `structure-classification skill` - Performance considerations
- Research: `structure-management-implementation-approaches.md` - Performance strategies

**Implementation Notes**:
```java
// Benchmark classification
long startTime = System.currentTimeMillis();
classificationSystem.classifyAll(structures);
long duration = System.currentTimeMillis() - startTime;

LOGGER.info("Classification completed in {}ms ({} structures/sec)",
    duration, (count * 1000L) / duration);

// Measure cache memory
long memoryUsed = cache.size() * 1024; // ~1KB per entry
LOGGER.info("Classification cache: {} entries, ~{} MB",
    cache.size(), memoryUsed / (1024 * 1024));

// Structure generation benchmark (compare before/after)
// May need Spark profiler integration for accurate measurement
```

**Acceptance Criteria**:
- [ ] Classification completes in <5 seconds for 1000 structures
- [ ] Console shows timing statistics
- [ ] Memory usage logged (<10MB for cache)
- [ ] No world generation performance regression
- [ ] Parallel processing utilizes multiple cores
- [ ] Cache access is O(1) (ConcurrentHashMap)
- [ ] Benchmarking logs are helpful and clear
- [ ] Follows all coding standards

**Dependencies**:
- TASK-005 (classification system to benchmark)
- TASK-008 (structure generation to measure)

**Blockers**:
- None

**Notes**:
- Epic 01 goal: No regression (improvement is bonus)
- Epic 04 will target 20-30% improvement with advanced optimization
- Focus on ensuring current implementation is not slow
- Simple timing logs sufficient for Epic 01 (Spark integration in Epic 04)

---

## Task Dependencies Graph

```
TASK-001 (Registry Scanner)
  ↓
TASK-002 (Data Structures & Cache)
  ↓
├─→ TASK-003 (SIZE Classifier)
│     ↓
└─→ TASK-004 (TYPE Classifier)
      ↓
    TASK-005 (Integrated Classification)
      ↓
    TASK-006 (Configuration System)
      ↓
    TASK-007 (Mixin Interception)
      ↓
    TASK-008 (Apply Multipliers)
      ↓
    TASK-009 (Performance & Benchmarking)
```

**Critical Path**: TASK-001 → TASK-002 → TASK-003/004 → TASK-005 → TASK-006 → TASK-007 → TASK-008 → TASK-009

**Parallel Opportunities**: TASK-003 and TASK-004 can be developed in parallel after TASK-002

---

## Success Metrics

**Epic 01 is complete when**:
1. ✅ User installs mod → Loads world → Structures automatically managed
2. ✅ Console shows: "Classified X structures: Y small, Z medium, W large | A towns, B dungeons..."
3. ✅ Mineshafts remain common (type.mineshaft = 1.0x works)
4. ✅ Large dungeons get 1.8x spacing (type override works)
5. ✅ Config presets work (switch preset → reload world → spacing changes)
6. ✅ Classification <5 seconds for 1000 structures
7. ✅ No crashes, no errors, works with popular structure mods
8. ✅ User can verify structures are further/closer apart

**When to move to Epic 02**:
- All 9 tasks completed
- User manually validates: structures are correctly spaced
- Works with 50+ structure mods without issues
- Performance is acceptable (no regression)

---

## Notes

**Skills Referenced**:
- `coding-standards skill` - All tasks
- `structure-classification skill` - TASK-002 through TASK-008
- `fabric-modding skill` - TASK-001, TASK-007
- `minecraft-modding skill` - TASK-001, TASK-003
- `java-development skill` - TASK-002
- `minecraft-performance skill` - TASK-005, TASK-009

**Research Referenced**:
- `structure-management-implementation-approaches.md` - Technical details
- `competitive-analysis-and-innovation.md` - Why two dimensions matter

**Testing Strategy**:
- Each task should compile and run without errors
- User manually tests features after implementation
- validation-agent creates automated tests AFTER user validation (future epic)
- Focus on working code first, automated tests later

**Risk Mitigation**:
- Mixin may conflict with other mods → Test with popular structure mods early
- Classification may be slow → Parallel processing from start
- Type detection may be inaccurate → 80% accuracy acceptable, fallback to SIZE
- Config may be confusing → Clear comments in TOML, good documentation

**Estimated Total Effort**: 39-47 hours (approximately 1-2 weeks for implementation-agent)
