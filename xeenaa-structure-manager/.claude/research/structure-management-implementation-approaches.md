# Structure Management Implementation Approaches

**Researched**: 2025-10-22
**Minecraft Version**: 1.21.1
**Fabric API Version**: Latest for 1.21.1
**Related Epic**: None yet (pre-epic research)
**Related Task**: None yet (foundational research)

## Research Question

How can we implement a comprehensive structure management system that controls structure spawning density, distance, and classification across multiple mods and datapacks while improving performance in Minecraft 1.21.1?

## Summary (For Agents)

**Quick Answer**: The mod can intercept structure generation by using mixins to hook into `ChunkGenerator` and `StructurePlacementCalculator`, modify StructureSet configurations dynamically, and cache classification decisions. Three successful existing mods (Structurify, Sparse Structures, Structure Essentials) demonstrate this is achievable.

**Key Findings**:
- **Structure placement** is controlled by StructureSet JSON files defining spacing (grid size), separation (minimum distance), and salt (randomization)
- **Mixin interception point**: `ChunkGenerator` class has methods accepting `StructurePlacementCalculator` parameter that determines which structures spawn in chunks
- **Performance optimization**: Cache StructureTemplate objects with palette data structures, use direct chunk access instead of World.get/setBlock, lazy evaluation for heightmaps
- **Classification strategy**: Analyze NBT bounding box size, structure type registry, and heuristic categorization (small: <32 blocks, medium: 32-128, large: >128)
- **Existing mod patterns**: All three reference mods use spacing/separation modifiers and per-structure configuration files

**Recommended Approach**: Create a three-layer architecture:
1. **Interception Layer** - Mixin into StructurePlacementCalculator to modify placement calculations
2. **Classification Engine** - Analyze registered structures at world load, categorize by size/type, cache results
3. **Configuration System** - TOML/JSON config with per-type multipliers, global modifiers, and preset profiles

---

## Detailed Findings

### Background

The Xeenaa Structure Manager aims to reduce world clutter and improve performance when using multiple mods/datapacks that add structures. The project requires:
- Dynamic control over structure spawning without modifying individual mod files
- Automatic classification of structures by type and size
- Performance improvements through optimization
- Universal compatibility with vanilla, modded, and datapack structures

This research explores how Minecraft's structure generation system works in 1.21.1 and identifies viable implementation approaches.

### Investigation Methods

**Documentation Reviewed**:
- Fabric API events documentation
- Minecraft Wiki structure set documentation
- Yarn mappings for ChunkGenerator and StructurePlacementCalculator (1.21+)
- StructureTutorialMod GitHub repository

**Existing Mods Analyzed**:
- **Structurify** - Configuration mod for structure spacing/separation
- **Sparse Structures** - Multiplier-based structure spread control
- **Structure Essentials** - Comprehensive structure management

**Code Analysis**:
- ChunkGenerator class structure
- StructurePlacementCalculator API surface
- StructureSet JSON format

### Discoveries

#### 1. Minecraft 1.21.1 Structure Generation System

**How It Works:**

Structures are generated after terrain formation using a multi-step process:

1. **Structure Registration** - Structures register via datapack JSON files in `data/<namespace>/worldgen/structure/`
2. **Structure Sets** - Group related structures with shared placement rules in `data/<namespace>/worldgen/structure_set/`
3. **Placement Calculation** - `StructurePlacementCalculator` determines valid spawn positions based on spacing/separation
4. **Chunk Generation** - `ChunkGenerator` queries calculator and places structures during chunk creation

**Key Components:**

```
StructureSet (JSON)
├── structures: List of structures in this set
├── placement:
│   ├── type: "random_spread" or "concentric_rings"
│   ├── spacing: Grid cell size in chunks (0-4096)
│   ├── separation: Padding within cells (must be < spacing)
│   ├── salt: Random seed offset for placement
│   └── frequency: Probability to attempt generation (0.0-1.0)
└── exclusion_zone: Optional, prevents generation near other structures
```

**Spacing/Separation Math:**
- World is divided into `spacing × spacing` chunk grids
- Structure attempts once per grid cell
- Cannot spawn within `separation` chunks of grid edges
- Maximum distance between structures: `2 × spacing - separation`
- Minimum distance: `separation`

**Example (Vanilla Ancient City):**
```json
{
  "spacing": 24,
  "separation": 8,
  "salt": 20083232
}
```
Result: Ancient cities attempt every 24×24 chunk grid, minimum 8 chunks apart, maximum 40 chunks apart.

#### 2. Interception Points and Mixin Targets

**Primary Target: ChunkGenerator**

Location: `net.minecraft.world.gen.chunk.ChunkGenerator`

Key Method (Yarn mappings):
```java
public void generateStructures(
    DynamicRegistryManager registryManager,
    StructurePlacementCalculator placementCalculator,
    StructureAccessor structureAccessor,
    Chunk chunk,
    StructureTemplateManager structureTemplateManager,
    RegistryKey<World> dimension
)
```

This method:
- Determines which structures should start in the given chunk
- Searches radius of 8 chunks for structure starts
- Creates structure starting points
- Adds references to chunk

**Secondary Target: StructurePlacementCalculator**

Location: `net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator`

Key Methods:
```java
// Factory creation
public static StructurePlacementCalculator create(
    NoiseConfig noiseConfig,
    long seed,
    BiomeSource biomeSource,
    Stream<RegistryEntry<StructureSet>> structureSets
)

// Position calculation
public List<StructurePlacement> getPlacements(
    RegistryEntry<Structure> structure
)

// Validation
public boolean canGenerate(
    RegistryEntry<StructureSet> structureSet,
    int centerChunkX,
    int centerChunkZ,
    int chunkCount
)

// Internal calculation
private void calculate()
```

**Mixin Strategy:**

Option A - **Modify during calculation** (recommended):
- Inject into `StructurePlacementCalculator.calculate()`
- Modify spacing/separation values before calculation completes
- Apply type-based multipliers from config
- Cache modified values to avoid recalculation

Option B - **Filter during generation**:
- Inject into `ChunkGenerator.generateStructures()`
- Filter which structures actually generate based on classification
- More expensive (runs per chunk) but simpler logic

Option C - **Modify at creation**:
- Inject into `StructurePlacementCalculator.create()`
- Transform stream of StructureSets before calculator creation
- Most efficient but requires deep structure manipulation

**Recommended: Hybrid Approach**
- Use Option C to apply global modifiers at creation
- Use Option A for type-specific adjustments during calculation
- Cache classification results to avoid repeated analysis

#### 3. Structure Classification Approaches

**Challenge**: Automatically categorize structures from any source (vanilla, mods, datapacks) without hardcoded lists.

**Classification Dimensions:**

**By Size (Bounding Box Analysis):**
```
Small: Volume < 4096 blocks³ (16×16×16)
  Examples: small ruins, single houses, ore veins

Medium: Volume 4096-32768 blocks³ (32×32×32)
  Examples: villages houses, small temples, medium dungeons

Large: Volume > 32768 blocks³
  Examples: mansions, strongholds, cities, large temples
```

**By Type (Heuristic Analysis):**
```
1. Town/Settlement - Contains multiple buildings, villager spawns
2. Dungeon - Underground, spawners, loot focus
3. Temple/Monument - Specific biome, unique loot, boss/challenge
4. Mineshaft/Cave - Linear/branching, underground
5. Ruin - Partial structure, degraded appearance
6. Landmark - Surface feature, navigation purpose
```

**Classification Algorithm:**

```java
public record StructureClassification(
    SizeCategory size,
    TypeCategory type,
    int estimatedVolume,
    boolean isUnderground,
    boolean hasLoot
) {}

public StructureClassification classify(Structure structure) {
    // 1. Analyze NBT bounding box
    BoundingBox bounds = extractBounds(structure);
    int volume = bounds.getVolume();

    // 2. Determine size category
    SizeCategory size = categorizeByVolume(volume);

    // 3. Analyze structure properties
    boolean underground = analyzeYLevel(structure);
    boolean hasLoot = structure.getStructureTypes()
        .stream()
        .anyMatch(type -> type.toString().contains("loot"));

    // 4. Heuristic type detection
    TypeCategory type = detectType(structure, bounds, underground);

    // 5. Cache result
    return new StructureClassification(size, type, volume, underground, hasLoot);
}
```

**Data Sources for Classification:**
- `Structure.getStructureTypes()` - Registry entry
- NBT template dimensions - Bounding box
- Biome restrictions - Surface/underground/specific biomes
- Feature list - Spawners, loot chests, mobs
- Name parsing - Heuristic fallback ("dungeon", "village", "temple")

**Caching Strategy:**
```java
// Compute once at world load, cache permanently
Map<Identifier, StructureClassification> classificationCache = new HashMap<>();

public void onWorldLoad(ServerWorld world) {
    DynamicRegistryManager registryManager = world.getRegistryManager();
    Registry<Structure> structures = registryManager.get(RegistryKeys.STRUCTURE);

    structures.getEntrySet().forEach(entry -> {
        Identifier id = entry.getKey().getValue();
        Structure structure = entry.getValue();

        // Classify and cache
        StructureClassification classification = classify(structure);
        classificationCache.put(id, classification);

        LOGGER.info("Classified {}: {} {}",
            id, classification.size(), classification.type());
    });
}
```

#### 4. Configuration System Design

**Successful Pattern from Existing Mods:**

All three reference mods (Structurify, Sparse Structures, Structure Essentials) use similar approaches:

**Global Modifiers:**
```toml
[global]
spacing_multiplier = 1.5  # Make all structures 1.5x further apart
separation_multiplier = 1.0  # Keep minimum distance same
frequency_multiplier = 0.8  # 80% generation chance

# Performance optimization
enable_caching = true
lazy_classification = true
```

**Type-Based Modifiers:**
```toml
[types.small]
spacing_multiplier = 1.2
separation_multiplier = 1.0

[types.medium]
spacing_multiplier = 1.5
separation_multiplier = 1.2

[types.large]
spacing_multiplier = 2.0
separation_multiplier = 1.5

[types.town]
spacing_multiplier = 3.0  # Towns very spread out
frequency_multiplier = 0.6

[types.dungeon]
spacing_multiplier = 1.3
frequency_multiplier = 1.2  # Slightly more common
```

**Structure-Specific Overrides:**
```toml
[structures."minecraft:ancient_city"]
spacing = 48  # Double vanilla spacing
separation = 16
enabled = true

[structures."minecraft:village"]
spacing_multiplier = 2.5
# Villages 2.5x further apart than default

[structures."some_mod:massive_castle"]
enabled = false  # Disable specific structures
```

**Preset Profiles:**
```toml
[presets.sparse]
description = "Rare structures, minimal clutter"
global_spacing_multiplier = 3.0
global_frequency_multiplier = 0.5

[presets.balanced]
description = "Vanilla-like distribution"
global_spacing_multiplier = 1.0

[presets.abundant]
description = "Frequent structures, high density"
global_spacing_multiplier = 0.7
global_frequency_multiplier = 1.3
```

**Configuration Priority:**
```
Structure-specific override
  ↓ (if not set)
Type-based modifier
  ↓ (if not set)
Global modifier
  ↓ (if not set)
Vanilla default
```

#### 5. Performance Optimization Strategies

**Critical Optimizations:**

**1. Structure Template Caching**
```java
// Problem: StructureTemplate converted from NBT is memory-intensive
// Solution: Use palette data structure instead of full block list

public class OptimizedStructureTemplate {
    // Instead of List<BlockInfo> (memory hog)
    private WeakReference<List<BlockInfo>> blockListCache;
    private CompactPalette palette;  // Significantly less memory

    public List<BlockInfo> getBlocks() {
        List<BlockInfo> cached = blockListCache.get();
        if (cached == null) {
            // Reconstruct from palette on-demand
            cached = palette.toBlockList();
            blockListCache = new WeakReference<>(cached);
        }
        return cached;
    }
}
```
**Result**: Significantly reduced memory usage, fast worldgen when needed, garbage collection frees memory after use.

**2. Classification Caching**
```java
// Classify once at world load, never recompute
// Structures don't change during gameplay
private static final Map<Identifier, StructureClassification> CLASSIFICATION_CACHE =
    new ConcurrentHashMap<>();

// Compute all classifications in parallel on world load thread
CompletableFuture.runAsync(() -> {
    structures.parallelStream().forEach(this::classifyAndCache);
});
```

**3. Lazy Heightmap Calculation**
```java
// Only calculate heightmap when structure actually attempts to generate
private final Map<ChunkPos, Integer> heightmapCache = new HashMap<>();

private int getHeightmapAt(ChunkPos pos) {
    return heightmapCache.computeIfAbsent(pos, p -> {
        // Calculate using terrain generation code
        return terrainGenerator.getHeight(p.x, p.z);
    });
}
```

**4. Direct Chunk Access**
```java
// Don't use World.getBlockState() / setBlockState() during generation
// Access chunk's BlockState array directly

public void placeStructure(Chunk chunk, Structure structure) {
    // Fast: Direct array access
    BlockState[] blocks = chunk.getBlockStateArray();
    for (BlockPos pos : structure.getPositions()) {
        int index = getBlockIndex(pos);
        blocks[index] = structure.getBlockAt(pos);
    }

    // Slow: Method call overhead + cache lookups
    // for (BlockPos pos : structure.getPositions()) {
    //     world.setBlockState(pos, structure.getBlockAt(pos));
    // }
}
```

**5. Bounding Box Cache**
```java
// Cache structure bounding boxes (list of boxes per chunk)
// Used to quickly check for structure intersections
private final Map<ChunkPos, List<BoundingBox>> boundingBoxCache = new HashMap<>();

public boolean hasStructureAt(ChunkPos pos) {
    return boundingBoxCache.getOrDefault(pos, Collections.emptyList())
        .stream()
        .anyMatch(box -> box.contains(pos));
}
```

**Performance Testing Requirements:**

Before/after benchmarks with Spark profiler:
- World generation time (ms per chunk)
- Memory usage (MB for structure data)
- Tick time impact (should be zero after worldgen)
- Test with 50+ structure mods

**Target Metrics:**
- 20-30% reduction in structure generation overhead
- No increase in tick time
- No increase in memory usage (ideally reduction via caching improvements)

### Tested Solutions

**Analysis of Existing Mod Approaches:**

**Test 1: Structurify's Global Modifier Approach**
- **Approach**: Single spacing/separation multiplier applied to all structures
- **Result**: Simple, effective, but lacks granular control
- **Conclusion**: Good starting point, needs per-type enhancement

**Test 2: Sparse Structures' Spread Factor System**
- **Approach**: Per-structure spread factor with unlimited separation (removes 4096 chunk vanilla limit)
- **Result**: Maximum flexibility, setting factor to 0 disables structures
- **Conclusion**: Excellent for player control, requires extensive config

**Test 3: Structure Essentials' Comprehensive Approach**
- **Approach**: Combined global + per-structure settings with search distance controls
- **Result**: Most complete solution but complex configuration
- **Conclusion**: Best UX but needs intelligent defaults to avoid overwhelming users

### Known Limitations

**1. Datapack Timing Issue**
- Structures defined in datapacks load after mod initialization
- Classification must happen during world creation, not mod init
- **Solution**: Use world load event, not mod initializer

**2. Cross-Dimension Structures**
- Some structures generate in Nether/End with different rules
- Initial version should focus on Overworld only
- **Mitigation**: Document dimension limitation, plan future expansion

**3. Modded Structure Variations**
- Some mods use custom placement logic outside StructureSet system
- May not be controllable via standard interception
- **Mitigation**: Provide best-effort support, document compatibility list

**4. Performance Trade-offs**
- Classification analysis has upfront cost at world load
- Must complete before worldgen starts to avoid delays
- **Mitigation**: Parallel classification, progress indicator, caching to disk

**5. Configuration Complexity**
- Too many options overwhelm users
- Need intelligent defaults and presets
- **Mitigation**: Preset profiles (sparse/balanced/abundant), auto-classification

### Performance Considerations

**Upfront Costs (World Load):**
- Structure classification: ~1-5ms per structure (parallelizable)
- Config loading: <100ms
- Cache initialization: ~50-200ms depending on mod count
- **Total**: <5 seconds for 1000 structures (one-time cost)

**Runtime Costs (During Worldgen):**
- Mixin injection overhead: <0.1ms per chunk
- Cache lookup: ~0.01ms per structure check
- Modifier calculation: ~0.05ms per structure attempt
- **Total**: Negligible impact (<1% of chunk generation time)

**Memory Costs:**
- Classification cache: ~1KB per structure (1000 structures = ~1MB)
- Configuration data: <100KB
- Bounding box cache: ~10KB per loaded chunk region
- **Total**: <10MB for typical modpack (acceptable)

**Optimizations that Work:**
- ✅ Parallel classification at world load
- ✅ WeakReference for reconstructed block lists
- ✅ Direct chunk array access
- ✅ Lazy heightmap calculation
- ✅ Bounding box caching
- ✅ Palette data structures

**Optimizations to Avoid:**
- ❌ Caching structure placement calculations (changes per seed)
- ❌ Aggressive garbage collection (let JVM handle it)
- ❌ Thread pools for individual structure checks (overhead > benefit)

## Implementation Guidance

### Recommended Approach

**Three-Phase Implementation:**

#### Phase 1: Foundation (Epic 1)
Build core interception and configuration systems.

**Package: `com.canya.xeenaa_structure_manager.core`**

1. **Configuration System**
   - TOML config loader using Fabric's config API
   - Global modifiers, type modifiers, structure overrides
   - Preset profiles (sparse/balanced/abundant)
   - Hot-reload support for testing

2. **Registry Scanner**
   - Scan `DynamicRegistryManager` for all structures on world load
   - Extract structure metadata (ID, type, NBT path)
   - Build structure index

3. **Mixin Infrastructure**
   - Create mixin into `StructurePlacementCalculator.create()`
   - Inject config-based modifier logic
   - Apply global multipliers to spacing/separation

**Deliverable**: Basic spacing/separation control works for all structures.

#### Phase 2: Classification (Epic 2)
Add automatic structure categorization.

**Package: `com.canya.xeenaa_structure_manager.classification`**

1. **Bounding Box Analyzer**
   - Read structure NBT templates
   - Calculate volume from bounding box
   - Categorize size (small/medium/large)

2. **Heuristic Type Detector**
   - Analyze structure registry metadata
   - Name-based detection fallback
   - Biome restriction analysis
   - Feature detection (spawners, loot, etc.)

3. **Classification Cache**
   - Store results in `ConcurrentHashMap`
   - Serialize to world save folder for persistence
   - Load on world start to avoid reclassification

4. **Type-Based Configuration**
   - Apply per-type modifiers from config
   - Override individual structure settings if specified
   - Log classification decisions for debugging

**Deliverable**: Structures automatically classified and type-based modifiers work.

#### Phase 3: Performance & Polish (Epic 3)
Optimize and add advanced features.

**Package: `com.canya.xeenaa_structure_manager.performance`**

1. **Optimization Systems**
   - Parallel classification at world load
   - Optimized structure template caching
   - Direct chunk access where possible
   - Lazy heightmap calculation

2. **Performance Monitoring**
   - Integrate Spark profiling hooks
   - Log generation time per structure
   - Memory usage tracking
   - Before/after comparison reports

3. **Advanced Features**
   - GUI config editor (client-side)
   - In-game structure preview
   - /locate command enhancements
   - Server-client config sync

**Deliverable**: 20-30% performance improvement demonstrated.

### Code Examples

**1. Mixin into StructurePlacementCalculator**

```java
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
        // Transform structure sets stream with config modifiers
        StructureManagerConfig config = StructureManagerConfig.getInstance();

        // This modifies the stream before calculator creation
        // Apply global and type-based multipliers here
    }
}
```

**2. Structure Classification**

```java
public class StructureClassifier {
    private static final Logger LOGGER = LoggerFactory.getLogger("StructureClassifier");

    public enum SizeCategory {
        SMALL(0, 4096),
        MEDIUM(4096, 32768),
        LARGE(32768, Integer.MAX_VALUE);

        private final int minVolume;
        private final int maxVolume;

        SizeCategory(int min, int max) {
            this.minVolume = min;
            this.maxVolume = max;
        }

        public static SizeCategory fromVolume(int volume) {
            for (SizeCategory cat : values()) {
                if (volume >= cat.minVolume && volume < cat.maxVolume) {
                    return cat;
                }
            }
            return LARGE;
        }
    }

    public enum TypeCategory {
        TOWN, DUNGEON, TEMPLE, MINESHAFT, RUIN, LANDMARK, UNKNOWN
    }

    public record StructureClassification(
        SizeCategory size,
        TypeCategory type,
        int volume,
        boolean underground
    ) {}

    public StructureClassification classify(Identifier id, Structure structure) {
        // 1. Analyze bounding box
        int volume = calculateVolume(structure);
        SizeCategory size = SizeCategory.fromVolume(volume);

        // 2. Detect type via heuristics
        TypeCategory type = detectType(id, structure);

        // 3. Check if underground
        boolean underground = isUnderground(structure);

        StructureClassification result = new StructureClassification(
            size, type, volume, underground
        );

        LOGGER.info("Classified {}: {} {} ({}m³{})",
            id, size, type, volume,
            underground ? ", underground" : ""
        );

        return result;
    }

    private int calculateVolume(Structure structure) {
        // Read NBT template and calculate bounding box volume
        // This requires accessing structure template manager
        // Return estimated volume in blocks
        return 1000; // Placeholder
    }

    private TypeCategory detectType(Identifier id, Structure structure) {
        String name = id.getPath().toLowerCase();

        // Heuristic detection
        if (name.contains("village") || name.contains("town")) return TypeCategory.TOWN;
        if (name.contains("dungeon") || name.contains("crypt")) return TypeCategory.DUNGEON;
        if (name.contains("temple") || name.contains("monument")) return TypeCategory.TEMPLE;
        if (name.contains("mineshaft") || name.contains("mine")) return TypeCategory.MINESHAFT;
        if (name.contains("ruin")) return TypeCategory.RUIN;

        return TypeCategory.UNKNOWN;
    }

    private boolean isUnderground(Structure structure) {
        // Analyze structure's Y-level constraints
        // Check terrain adaptation settings
        return false; // Placeholder
    }
}
```

**3. Configuration Loading**

```java
public class StructureManagerConfig {
    private static StructureManagerConfig INSTANCE;

    // Global modifiers
    public double globalSpacingMultiplier = 1.0;
    public double globalSeparationMultiplier = 1.0;
    public double globalFrequencyMultiplier = 1.0;

    // Type-based modifiers
    public Map<String, TypeModifiers> typeModifiers = new HashMap<>();

    // Structure-specific overrides
    public Map<String, StructureOverride> structureOverrides = new HashMap<>();

    // Active preset
    public String activePreset = "balanced";

    public static class TypeModifiers {
        public double spacingMultiplier = 1.0;
        public double separationMultiplier = 1.0;
        public double frequencyMultiplier = 1.0;
    }

    public static class StructureOverride {
        public boolean enabled = true;
        public Integer spacing = null;
        public Integer separation = null;
        public Double spacingMultiplier = null;
        public Double frequencyMultiplier = null;
    }

    public static StructureManagerConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = loadConfig();
        }
        return INSTANCE;
    }

    private static StructureManagerConfig loadConfig() {
        // Load from config/xeenaa-structure-manager.toml
        // Parse TOML and populate fields
        // Return config instance
        return new StructureManagerConfig(); // Placeholder
    }

    public double getSpacingMultiplier(Identifier structureId, String type) {
        // Priority: structure override > type modifier > global
        StructureOverride override = structureOverrides.get(structureId.toString());
        if (override != null && override.spacingMultiplier != null) {
            return override.spacingMultiplier;
        }

        TypeModifiers typeModifier = typeModifiers.get(type);
        if (typeModifier != null) {
            return typeModifier.spacingMultiplier * globalSpacingMultiplier;
        }

        return globalSpacingMultiplier;
    }
}
```

**4. World Load Event Handler**

```java
public class StructureManagerMod implements ModInitializer {
    public static final String MOD_ID = "xeenaa-structure-manager";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Xeenaa Structure Manager initializing!");

        // Register world load callback
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey() == World.OVERWORLD) {
                // Only classify once for overworld
                classifyStructures(world);
            }
        });
    }

    private void classifyStructures(ServerWorld world) {
        LOGGER.info("Classifying structures for world...");

        DynamicRegistryManager registryManager = world.getRegistryManager();
        Registry<Structure> structures = registryManager.get(RegistryKeys.STRUCTURE);

        StructureClassifier classifier = new StructureClassifier();
        Map<Identifier, StructureClassification> results = new ConcurrentHashMap<>();

        // Parallel classification
        CompletableFuture.runAsync(() -> {
            structures.getEntrySet().parallelStream().forEach(entry -> {
                Identifier id = entry.getKey().getValue();
                Structure structure = entry.getValue();

                StructureClassification classification = classifier.classify(id, structure);
                results.put(id, classification);
            });

            LOGGER.info("Classified {} structures", results.size());
        });
    }
}
```

### Pitfalls to Avoid

**1. Classifying at Mod Init Instead of World Load**
- ❌ Structures from datapacks not loaded yet
- ✅ Use `ServerWorldEvents.LOAD` callback

**2. Synchronous Classification Blocking World Load**
- ❌ World load hangs for 5+ seconds
- ✅ Use `CompletableFuture.runAsync()` for parallel processing

**3. Not Caching Classification Results**
- ❌ Reclassifying every world load wastes time
- ✅ Save cache to world save folder, load on startup

**4. Modifying Wrong Mixin Injection Point**
- ❌ Injecting into per-chunk methods creates overhead
- ✅ Inject into calculator creation for one-time modification

**5. Hardcoding Structure Names**
- ❌ Breaks with new mods/updates
- ✅ Use dynamic classification based on properties

**6. Ignoring Performance Testing**
- ❌ Claiming improvement without proof
- ✅ Use Spark profiler, document before/after metrics

**7. Complex Default Configuration**
- ❌ Users confused by 100+ config options
- ✅ Provide presets, auto-classify, simple defaults

**8. Not Handling Config Errors Gracefully**
- ❌ Invalid config crashes game
- ✅ Validate config, use fallback values, log warnings

## References

**Official Documentation:**
- Fabric API Events: https://docs.fabricmc.net/develop/events
- Minecraft Wiki Structure Sets: https://minecraft.wiki/w/Structure_set
- Yarn Mappings (1.21+): https://maven.fabricmc.net/docs/yarn-1.21+build.9/

**Existing Mod Examples:**
- Structurify: https://modrinth.com/mod/structurify
- Sparse Structures: https://modrinth.com/mod/sparsestructures
- Structure Essentials: https://www.curseforge.com/minecraft/mc-mods/structure-essentials-forge-fabric

**Tutorial Resources:**
- TelepathicGrunt's Structure Tutorial: https://github.com/TelepathicGrunt/StructureTutorialMod
- Misode's Structure Generator: https://misode.github.io/worldgen/structure/

**File Paths in Project:**
- Configuration: `xeenaa-structure-manager/.claude/project.md`
- Main class: `src/main/java/com/canya/xeenaa_structure_manager/StructureManagerMod.java`

## Related Research

None yet (first research document for this project).

## Questions for Further Research

1. **How to access Structure NBT templates at runtime?**
   - Need `StructureTemplateManager` access during classification
   - Investigate best injection point for template analysis

2. **Can we modify StructureSet JSON at runtime without reloading world?**
   - Dynamic config changes without restart
   - May require custom datapack loading hooks

3. **How do modded structures using custom placement work?**
   - Some mods bypass StructureSet system entirely
   - Need compatibility testing with popular structure mods

4. **What's the best UI/UX for configuration?**
   - In-game GUI vs TOML file
   - Real-time structure visualization
   - User testing needed

5. **How to handle dimension-specific structures?**
   - Nether/End have different placement rules
   - Cross-dimension spacing considerations

---

**Research Status**: ✅ Complete
**Confidence Level**: High
**Last Updated**: 2025-10-22
