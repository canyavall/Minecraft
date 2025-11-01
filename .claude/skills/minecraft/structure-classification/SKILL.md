---
name: structure-classification
description: Minecraft structure classification strategies including two-dimensional classification (size + type), heuristic detection methods, and configuration priority systems. Use when implementing structure management, auto-classification, or structure-related features.
tools:
  - read
  - grep
  - glob
---

# Structure Classification Skill

This skill provides expert knowledge on classifying Minecraft structures for automated management systems. It covers two-dimensional classification, heuristic detection, priority systems, and best practices learned from competitive analysis.

## Core Concepts

### Why Classification Matters

**Problem**: Modpacks can have 200+ structures from different mods. Managing them individually is impossible.

**Solution**: Automatic classification into categories enables intelligent defaults without manual configuration.

**Competitive Advantage**: All existing structure management mods (Sparse Structures, Structurify, Structure Essentials) require manual configuration. Automatic classification is a game-changer.

## Two-Dimensional Classification System

### The Problem with Single-Dimension Classification

**Size-only classification FAILS for edge cases**:

```java
// Size-only approach (WRONG)
if (structure.getVolume() > 32768) {
    applySpacing(2.5x);  // Large spacing
}

// Problem: Mineshafts are LARGE but should be COMMON
// Problem: Large dungeon vs large town need different treatment
```

**Type-only classification FAILS for physical impact**:

```java
// Type-only approach (ALSO WRONG)
if (structure.getType() == "dungeon") {
    applySpacing(1.8x);
}

// Problem: Small dungeon vs large dungeon have different impact
// Problem: Ignores actual world footprint
```

### The Solution: Two Independent Dimensions

**Dimension 1: SIZE** (Physical Footprint)
- Measures: Bounding box volume in blocks³
- Purpose: Capture physical world impact
- Categories: Small, Medium, Large

**Dimension 2: TYPE** (Behavioral Category)
- Measures: Structure purpose/behavior
- Purpose: Capture gameplay intent
- Categories: Town, Dungeon, Temple, Mineshaft, Sky, Ruin, Unknown

**Both dimensions are INDEPENDENT and both matter**:

```java
public record StructureClassification(
    SizeCategory size,      // Physical impact
    TypeCategory type,      // Gameplay behavior
    int volume,             // Raw measurement
    boolean underground,    // Location context
    Identifier id          // For debugging
) {}
```

## Size Classification (Dimension 1)

### Size Categories and Thresholds

```java
public enum SizeCategory {
    SMALL(0, 4_096),           // < 16×16×16
    MEDIUM(4_096, 32_768),     // 16×16×16 to 32×32×32
    LARGE(32_768, Integer.MAX_VALUE);  // > 32×32×32

    private final int minVolume;
    private final int maxVolume;

    public static SizeCategory fromVolume(int volume) {
        for (SizeCategory cat : values()) {
            if (volume >= cat.minVolume && volume < cat.maxVolume) {
                return cat;
            }
        }
        return LARGE;
    }
}
```

### Bounding Box Volume Calculation

```java
public int calculateVolume(Structure structure) {
    // 1. Try to get NBT template
    StructureTemplate template = getTemplate(structure);
    if (template != null) {
        BlockBox bounds = template.getBoundingBox();
        int width = bounds.getBlockCountX();
        int height = bounds.getBlockCountY();
        int depth = bounds.getBlockCountZ();
        return width * height * depth;
    }

    // 2. Fallback: Estimate from structure type
    return estimateVolumeFromType(structure);
}

private int estimateVolumeFromType(Structure structure) {
    String name = structure.getId().getPath();

    // Heuristic estimates when NBT unavailable
    if (name.contains("mansion")) return 100_000;  // Large
    if (name.contains("village")) return 50_000;   // Large
    if (name.contains("temple")) return 20_000;    // Medium
    if (name.contains("ruin")) return 2_000;       // Small

    return 10_000;  // Default to Medium
}
```

### Size Classification Best Practices

**✅ DO**:
- Calculate from NBT bounding box when available
- Cache calculated volumes (structures don't change)
- Use conservative estimates for unknown structures
- Log when using fallback estimates

**❌ DON'T**:
- Hardcode structure names
- Assume all structures have NBT templates
- Recalculate volume on every access
- Fail silently when NBT unavailable

## Type Classification (Dimension 2)

### Type Categories

```java
public enum TypeCategory {
    TOWN,       // Villages, settlements, inhabited areas
    DUNGEON,    // Underground combat/loot structures
    TEMPLE,     // Monuments, pyramids, above-ground landmarks
    MINESHAFT,  // Linear mining structures
    SKY,        // Aerial/floating structures
    RUIN,       // Scattered debris/abandoned structures
    UNKNOWN     // Fallback when type cannot be determined
}
```

### Heuristic Detection Methods

**Method 1: Name-Based Detection (Primary - 80% Accuracy)**

```java
public TypeCategory detectTypeFromName(Identifier id) {
    String name = id.getPath().toLowerCase();

    // Town/Settlement keywords
    if (name.contains("village") ||
        name.contains("town") ||
        name.contains("settlement")) {
        return TypeCategory.TOWN;
    }

    // Dungeon keywords
    if (name.contains("dungeon") ||
        name.contains("crypt") ||
        name.contains("catacomb") ||
        name.contains("fortress") ||
        name.contains("stronghold") ||
        name.contains("keep") ||
        name.contains("castle")) {
        return TypeCategory.DUNGEON;
    }

    // Temple keywords
    if (name.contains("temple") ||
        name.contains("pyramid") ||
        name.contains("monument")) {
        return TypeCategory.TEMPLE;
    }

    // Mineshaft keywords
    if (name.contains("mineshaft") ||
        name.contains("mine") ||
        name.contains("shaft")) {
        return TypeCategory.MINESHAFT;
    }

    // Sky keywords
    if (name.contains("sky") ||
        name.contains("floating") ||
        name.contains("aerial") ||
        name.contains("airship")) {
        return TypeCategory.SKY;
    }

    // Ruin keywords
    if (name.contains("ruin") ||
        name.contains("remnant") ||
        name.contains("wreckage")) {
        return TypeCategory.RUIN;
    }

    return TypeCategory.UNKNOWN;
}
```

**Method 2: Y-Level Analysis (Secondary)**

```java
public TypeCategory detectTypeFromYLevel(Structure structure) {
    // Analyze structure's Y-level constraints
    int minY = structure.getMinY();
    int maxY = structure.getMaxY();
    int avgY = (minY + maxY) / 2;

    // Sky structures (high altitude)
    if (avgY > 100) {
        return TypeCategory.SKY;
    }

    // Underground structures (below sea level)
    if (avgY < 40) {
        return TypeCategory.DUNGEON;
    }

    // Surface structures
    return TypeCategory.UNKNOWN;  // Can't determine from Y alone
}
```

**Method 3: Bounding Box Shape Analysis (Tertiary)**

```java
public TypeCategory detectTypeFromShape(BlockBox bounds) {
    int width = bounds.getBlockCountX();
    int height = bounds.getBlockCountY();
    int depth = bounds.getBlockCountZ();

    // Mineshafts are LINEAR (long in one dimension)
    int max = Math.max(width, Math.max(height, depth));
    int min = Math.min(width, Math.min(height, depth));

    double aspectRatio = (double) max / min;

    // Very elongated structure = likely mineshaft
    if (aspectRatio > 10) {
        return TypeCategory.MINESHAFT;
    }

    return TypeCategory.UNKNOWN;
}
```

**Combined Detection Algorithm**:

```java
public TypeCategory detectType(Identifier id, Structure structure) {
    // 1. Name-based detection (most reliable)
    TypeCategory fromName = detectTypeFromName(id);
    if (fromName != TypeCategory.UNKNOWN) {
        return fromName;
    }

    // 2. Y-level analysis (helps with underground vs aerial)
    TypeCategory fromY = detectTypeFromYLevel(structure);
    if (fromY != TypeCategory.UNKNOWN) {
        return fromY;
    }

    // 3. Shape analysis (detects linear structures)
    BlockBox bounds = getBoundingBox(structure);
    if (bounds != null) {
        TypeCategory fromShape = detectTypeFromShape(bounds);
        if (fromShape != TypeCategory.UNKNOWN) {
            return fromShape;
        }
    }

    // 4. Fallback
    return TypeCategory.UNKNOWN;
}
```

### Type Detection Best Practices

**✅ DO**:
- Use multiple detection methods (name → Y-level → shape)
- Accept 80% accuracy as "good enough" for MVP
- Log detected types for user transparency
- Provide TypeCategory.UNKNOWN fallback
- Allow manual overrides in config

**❌ DON'T**:
- Try for 100% accuracy (diminishing returns)
- Use complex machine learning (overkill for MVP)
- Fail when type cannot be determined
- Hardcode structure lists (defeats auto-classification)
- Ignore edge cases (mineshafts, sky structures)

## Configuration Priority System

### Priority Hierarchy

```
Manual Structure Override (config: structures."mod:name")
  ↓ (if not set)
Type-Based Multiplier (config: type.dungeon)
  ↓ (if not set or Type=Unknown)
Size-Based Multiplier (config: size.large)
  ↓ (if not set)
Global Multiplier (config: global)
  ↓
Vanilla Default (1.0x)
```

### Implementation Example

```java
public double getSpacingMultiplier(
    Identifier structureId,
    SizeCategory size,
    TypeCategory type
) {
    // 1. Check structure-specific override
    StructureOverride override = config.getStructureOverride(structureId);
    if (override != null && override.spacingMultiplier != null) {
        return override.spacingMultiplier;
    }

    // 2. Check type-based multiplier
    if (type != TypeCategory.UNKNOWN) {
        Double typeMultiplier = config.getTypeMultiplier(type);
        if (typeMultiplier != null) {
            return typeMultiplier * config.globalMultiplier;
        }
    }

    // 3. Check size-based multiplier
    Double sizeMultiplier = config.getSizeMultiplier(size);
    if (sizeMultiplier != null) {
        return sizeMultiplier * config.globalMultiplier;
    }

    // 4. Use global multiplier
    return config.globalMultiplier;
}
```

### Configuration File Structure

```toml
[preset]
active = "balanced"

[global]
spacing_multiplier = 1.0
separation_multiplier = 1.0

# SIZE-based defaults
[size.small]
spacing_multiplier = 1.2

[size.medium]
spacing_multiplier = 1.5

[size.large]
spacing_multiplier = 2.5

# TYPE-based overrides (take priority over size)
[type.town]
spacing_multiplier = 2.0

[type.dungeon]
spacing_multiplier = 1.8

[type.temple]
spacing_multiplier = 2.2

[type.mineshaft]
spacing_multiplier = 1.0  # CRITICAL: Don't touch mineshafts!

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

## Real-World Classification Examples

### Example 1: Ancient City

```java
Identifier: "minecraft:ancient_city"
Bounding Box: ~500,000 blocks³
Y-Level: -52 to -30 (deep underground)

// Classification:
SIZE: LARGE (500,000 > 32,768)
TYPE: DUNGEON (name contains "city", Y < 0)

// Config application:
size.large = 2.5x    // Would apply this...
type.dungeon = 1.8x  // ...but TYPE overrides!

// Final multiplier: 1.8x (not 2.5x)
// Why: Dungeons should be rarer than size alone suggests,
//      but not as rare as full large spacing
```

### Example 2: Plains Village

```java
Identifier: "minecraft:village_plains"
Bounding Box: ~50,000 blocks³
Y-Level: 64-80 (surface)

// Classification:
SIZE: LARGE (50,000 > 32,768)
TYPE: TOWN (name contains "village")

// Config application:
size.large = 2.5x
type.town = 2.0x  // TYPE overrides

// Final multiplier: 2.0x
// Why: Towns should be spread out but not extreme
```

### Example 3: Abandoned Mineshaft

```java
Identifier: "minecraft:mineshaft"
Bounding Box: ~100,000 blocks³ (long and linear)
Y-Level: 10-60 (underground)

// Classification:
SIZE: LARGE (100,000 > 32,768)
TYPE: MINESHAFT (name contains "mineshaft")

// Config application:
size.large = 2.5x
type.mineshaft = 1.0x  // TYPE overrides - CRITICAL!

// Final multiplier: 1.0x (NO CHANGE)
// Why: Mineshafts are linear/branching, already balanced
//      Size-based spacing would BREAK them
```

### Example 4: Sky Village (Modded)

```java
Identifier: "some_mod:sky_village"
Bounding Box: ~20,000 blocks³
Y-Level: 120-150 (aerial)

// Classification:
SIZE: MEDIUM (20,000 in range)
TYPE: SKY (Y > 100, name contains "sky")

// Config application:
size.medium = 1.5x
type.sky = 1.5x  // TYPE overrides (same value)

// Final multiplier: 1.5x
// Why: Aerial structures need moderate spacing
```

### Example 5: Small Ruin

```java
Identifier: "ruins_mod:small_ruin"
Bounding Box: ~2,000 blocks³
Y-Level: 63-70 (surface)

// Classification:
SIZE: SMALL (2,000 < 4,096)
TYPE: RUIN (name contains "ruin")

// Config application:
size.small = 1.2x
type.ruin = 1.3x  // TYPE overrides

// Final multiplier: 1.3x
// Why: Ruins should be scattered but slightly more spread than size
```

### Example 6: Unknown Mod Structure

```java
Identifier: "unknown_mod:mystery_castle"
Bounding Box: ~40,000 blocks³
Y-Level: 64-90
Name: No matching keywords

// Classification:
SIZE: LARGE (40,000 > 32,768)
TYPE: UNKNOWN (no detection matches)

// Config application:
size.large = 2.5x  // Used because TYPE=UNKNOWN
type.unknown = not configured

// Final multiplier: 2.5x
// Why: Fallback to size when type cannot be determined
//      Safe default for unknown structures
```

## Performance Considerations

### Caching Strategy

```java
// Classification is EXPENSIVE - cache results!
private final Map<Identifier, StructureClassification> cache =
    new ConcurrentHashMap<>();

public StructureClassification classify(Identifier id, Structure structure) {
    // Check cache first
    return cache.computeIfAbsent(id, key -> {
        // Expensive operations only if not cached
        SizeCategory size = classifySize(structure);
        TypeCategory type = detectType(id, structure);
        int volume = calculateVolume(structure);

        return new StructureClassification(size, type, volume,
            isUnderground(structure), id);
    });
}
```

**Cache Best Practices**:
- Use `ConcurrentHashMap` for thread-safety (world load is multi-threaded)
- Cache at world load, never invalidate (structures don't change)
- Consider serializing cache to disk (avoid reclassification on reload)

### Parallel Classification

```java
// Classify all structures in parallel for performance
public void classifyAllStructures(Registry<Structure> registry) {
    List<StructureClassification> results =
        registry.getEntrySet()
            .parallelStream()  // Use all CPU cores
            .map(entry -> {
                Identifier id = entry.getKey().getValue();
                Structure structure = entry.getValue();
                return classify(id, structure);
            })
            .toList();

    LOGGER.info("Classified {} structures in parallel", results.size());
}
```

**Performance Targets**:
- Classification: < 5 seconds for 1000 structures
- Parallel processing: Use all available CPU cores
- Memory: < 10MB for classification cache (1KB per structure × 1000)

## Common Pitfalls and Solutions

### Pitfall 1: Mineshafts Get Broken

**Problem**:
```java
// Size-only classification
if (structure.getVolume() > 32768) {
    spacing = 2.5x;  // Mineshafts become super rare!
}
```

**Solution**:
```java
// Type detection saves the day
if (type == TypeCategory.MINESHAFT) {
    spacing = 1.0x;  // Keep vanilla balance
}
```

### Pitfall 2: Not Handling Unknown Types

**Problem**:
```java
// Crashes when type cannot be determined
if (type == TypeCategory.UNKNOWN) {
    throw new RuntimeException("Unknown type!");  // BAD!
}
```

**Solution**:
```java
// Graceful fallback to size
if (type == TypeCategory.UNKNOWN) {
    return getSizeMultiplier(size);  // Safe default
}
```

### Pitfall 3: Hardcoding Structure Names

**Problem**:
```java
// Defeats the purpose of auto-classification
if (id.equals("minecraft:village")) { ... }
if (id.equals("yung:dungeon1")) { ... }
// ... 200 more hardcoded cases
```

**Solution**:
```java
// Use heuristics, not hardcoding
String name = id.getPath();
if (name.contains("village")) { ... }  // Works for ANY mod
```

### Pitfall 4: Synchronous Classification Blocking World Load

**Problem**:
```java
// World hangs for 10 seconds during load
structures.forEach(s -> classify(s));  // Blocking!
```

**Solution**:
```java
// Async parallel classification
CompletableFuture.runAsync(() -> {
    structures.parallelStream().forEach(this::classify);
});
```

### Pitfall 5: Not Logging Classifications

**Problem**:
```java
// Silent classification - user has no idea what happened
classify(structure);
```

**Solution**:
```java
// Transparent logging
StructureClassification result = classify(structure);
LOGGER.info("Classified {}: {} {} ({}m³)",
    id, result.size(), result.type(), result.volume());
```

## Testing Classification Accuracy

### Validation Against Known Structures

```java
@Test
public void testVanillaStructureClassification() {
    // Verify vanilla structures classified correctly

    // Villages should be LARGE + TOWN
    var village = classify(id("minecraft:village_plains"));
    assertEquals(SizeCategory.LARGE, village.size());
    assertEquals(TypeCategory.TOWN, village.type());

    // Ancient City should be LARGE + DUNGEON
    var city = classify(id("minecraft:ancient_city"));
    assertEquals(SizeCategory.LARGE, city.size());
    assertEquals(TypeCategory.DUNGEON, city.type());

    // Mineshaft should be LARGE + MINESHAFT
    var mineshaft = classify(id("minecraft:mineshaft"));
    assertEquals(SizeCategory.LARGE, mineshaft.size());
    assertEquals(TypeCategory.MINESHAFT, mineshaft.type());
}
```

### Measure Heuristic Accuracy

```java
// Test with 100 known structures
int correct = 0;
int total = 0;

for (var entry : knownStructures.entrySet()) {
    Identifier id = entry.getKey();
    ExpectedClassification expected = entry.getValue();

    StructureClassification actual = classify(id);

    if (actual.type() == expected.type()) {
        correct++;
    }
    total++;
}

double accuracy = (double) correct / total * 100;
LOGGER.info("Type detection accuracy: {}%", accuracy);
// Target: 80%+ for MVP
```

## When to Use This Skill

**Use structure-classification skill when**:
- Implementing auto-classification systems
- Designing structure management configs
- Creating structure-aware features
- Optimizing structure generation
- Building structure analysis tools

**This skill provides**:
- Two-dimensional classification approach (SIZE + TYPE)
- Heuristic detection algorithms
- Priority system design
- Configuration best practices
- Real-world examples and edge cases
- Performance optimization strategies

## References

**Related Research**:
- `.claude/research/structure-management-implementation-approaches.md` - Full technical research
- `.claude/research/competitive-analysis-and-innovation.md` - Why two dimensions matter

**Related Epics**:
- Epic 01: Core Structure Interception & Auto-Classification

**Minecraft Systems**:
- `Registry<Structure>` - Structure registration
- `StructurePlacementCalculator` - Placement logic
- `StructureTemplate` - NBT template access
- `BlockBox` - Bounding box calculations

---

**Key Takeaway**: Single-dimension classification (size-only OR type-only) breaks edge cases. Two independent dimensions (SIZE + TYPE) with a priority system (Type > Size > Global) handles all structure varieties correctly.

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used structure-classification
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used structure-classification`

This helps track which skills are actively consulted and identifies documentation gaps.
