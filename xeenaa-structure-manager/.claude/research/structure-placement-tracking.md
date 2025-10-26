# Structure Placement Tracking and Verification Research

**Project**: Xeenaa Structure Manager
**Created**: 2025-10-24
**Minecraft Version**: 1.21.1
**Research Focus**: Tracking and verifying structure placements to confirm multiplier effectiveness

---

## Executive Summary

**Quick Answer**: Use a mixin injecting into `StructureStart.place()` or `ChunkGenerator.locateStructure()` to track actual structure placements. Store placement data (ID, position, bounding box) in a concurrent map, then analyze spacing post-generation or export to CSV/JSON for statistical verification.

**Recommended Approach**:
1. Mixin into `StructureStart.place()` to capture successful placements
2. Store `Identifier`, `ChunkPos`, `BlockBox` in `ConcurrentHashMap<Identifier, List<StructurePlacement>>`
3. Log placements at DEBUG level with coordinates
4. Provide `/xeenaa stats` command to show placement counts and average distances
5. Export to JSON for external analysis (optional)

**Performance Impact**: Minimal (< 1ms per structure placement, negligible during worldgen)

**Confidence Level**: High (80%+) - Based on Minecraft source code analysis and successful mod implementations

---

## 1. Structure Placement Pipeline in Minecraft 1.21.1

### 1.1 World Generation Flow

```
World Load
  ↓
ChunkGenerator.create()
  ↓
StructurePlacementCalculator.create() ← [WE INTERCEPT HERE IN EPIC 01]
  ↓
Chunk Generation (per chunk)
  ↓
StructureAccessor.getStructureStarts() ← [READ: Structure starts for chunk]
  ↓
StructureStart.place() ← [BEST TRACKING POINT: Actual placement]
  ↓
StructurePiece.generate() (multiple pieces per structure)
  ↓
Blocks placed in world
```

### 1.2 Key Classes and Their Roles

| Class | Purpose | Thread Safety | Access Level |
|-------|---------|---------------|--------------|
| `StructurePlacementCalculator` | Determines WHERE structures should attempt to spawn | Server thread | Static factory method |
| `StructureAccessor` | Provides access to structure starts in chunks | Server thread | Interface, chunk-scoped |
| `StructureStart` | Describes a structure instance being generated | Server thread | Public class |
| `StructurePiece` | Individual piece of a structure (e.g., village house) | Server thread | Public class |
| `ChunkGenerator` | Orchestrates chunk generation including structures | Server thread | Public class |

### 1.3 Structure Storage in Chunks

**NBT Storage**: Structures are stored in chunk NBT data under `structures` tag:

```java
// Chunk NBT structure (simplified)
{
  "structures": {
    "starts": {
      "minecraft:village_plains": {
        "ChunkX": 10,
        "ChunkZ": -5,
        "BB": [150, 64, -80, 200, 90, -30], // Bounding box
        "biome": "minecraft:plains",
        "Children": [ /* StructurePiece list */ ]
      }
    },
    "References": {
      "minecraft:village_plains": 4 // Number of chunks referencing this structure
    }
  }
}
```

**In-Memory Access**: Use `StructureAccessor` interface to query structure data from generated chunks.

---

## 2. Tracking Approaches (Comparison)

### Approach A: Mixin into StructureStart.place() ⭐ RECOMMENDED

**Injection Point**: `StructureStart.place()`

**What it captures**: Successfully placed structures (after validation, biome checks, etc.)

**Pros**:
- Captures only ACTUAL placements (not attempts)
- Access to full `StructureStart` object (bounding box, ChunkPos, Structure reference)
- Fires once per structure (not per piece)
- Clean injection point with clear semantics

**Cons**:
- Requires reflection or accessor to extract Structure identifier
- May not capture placement ATTEMPTS (only successes)

**Implementation**:
```java
@Mixin(StructureStart.class)
public abstract class StructureStartMixin {

    @Inject(
        method = "place",
        at = @At("HEAD")
    )
    private void trackStructurePlacement(
        StructureWorldAccess world,
        StructureAccessor structureAccessor,
        ChunkGenerator chunkGenerator,
        Random random,
        BlockBox chunkBox,
        ChunkPos chunkPos,
        CallbackInfo ci
    ) {
        // Extract structure identifier via accessor or reflection
        StructureStart self = (StructureStart) (Object) this;

        // Get bounding box
        BlockBox boundingBox = self.getBoundingBox();

        // Get chunk position
        ChunkPos structurePos = self.getPos();

        // Track placement
        PlacementTracker.record(structureId, structurePos, boundingBox);

        LOGGER.debug("Structure placed: {} at chunk {}",
            structureId, structurePos);
    }
}
```

**Success Criteria**: ✅ Captures actual placements, ✅ Access to bounding box, ✅ Clean injection

---

### Approach B: Query StructureAccessor After Generation

**Injection Point**: After chunk generation completes

**What it captures**: All structure starts present in generated chunks

**Pros**:
- No need to track during generation
- Can query at any time post-generation
- Access to chunk NBT data
- No performance impact during worldgen

**Cons**:
- Requires iterating through all loaded chunks (expensive)
- Doesn't capture placement timing
- Difficult to distinguish "just generated" vs "loaded from disk"
- No access to placement attempts (only successes)

**Implementation**:
```java
// Query all structures in a region
ServerWorld world = ...;
StructureAccessor accessor = world.getStructureAccessor();

for (int chunkX = minX; chunkX <= maxX; chunkX++) {
    for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
        ChunkPos pos = new ChunkPos(chunkX, chunkZ);

        // Get structure starts for this chunk
        var structureStarts = accessor.getStructureStarts(
            SectionPos.from(pos, 0),
            structure
        );

        for (StructureStart start : structureStarts) {
            // Process structure
        }
    }
}
```

**Success Criteria**: ⚠️ Works but expensive, ⚠️ No timing info, ❌ Not ideal for verification

---

### Approach C: Mixin into ChunkGenerator.locateStructure()

**Injection Point**: `ChunkGenerator.locateStructure()`

**What it captures**: Structure location queries (e.g., from `/locate` command)

**Pros**:
- Shows when players/systems search for structures
- Access to search parameters and results
- Can verify structures are findable

**Cons**:
- Only fires when `/locate` is used (not automatic)
- Doesn't capture actual generation
- Not suitable for automatic verification

**Success Criteria**: ❌ Wrong use case for verification

---

### Approach D: Fabric API Events (if available)

**Fabric API Status**: ❌ **No built-in structure generation events in Fabric API 1.21.1**

**Research Finding**: Fabric API provides `BiomeModifications` for adding structures to biomes, but **no callbacks for structure placement**. Custom events would require creating our own event system with mixins.

**Recommendation**: Not worth the complexity for Epic 01. Use Approach A (mixin directly).

---

## 3. Data Collection Strategy

### 3.1 What Data to Capture

**Minimum Required**:
- `Identifier structureId` - Structure identifier (e.g., "minecraft:village_plains")
- `ChunkPos chunkPos` - Originating chunk position
- `BlockBox boundingBox` - 3D bounding box (for distance calculation)

**Optional (for advanced analysis)**:
- `long timestamp` - When structure was placed (System.currentTimeMillis())
- `Identifier biome` - Biome where structure spawned
- `boolean successful` - Placement succeeded or failed (if tracking attempts)
- `int references` - Number of chunks referencing this structure

**Data Structure**:
```java
public record PlacedStructure(
    Identifier structureId,
    ChunkPos chunkPos,
    BlockBox boundingBox,
    long timestamp
) {
    // Calculate center position
    public BlockPos getCenter() {
        return new BlockPos(
            (boundingBox.getMinX() + boundingBox.getMaxX()) / 2,
            (boundingBox.getMinY() + boundingBox.getMaxY()) / 2,
            (boundingBox.getMinZ() + boundingBox.getMaxZ()) / 2
        );
    }

    // Calculate distance to another structure
    public double distanceTo(PlacedStructure other) {
        BlockPos thisCenter = this.getCenter();
        BlockPos otherCenter = other.getCenter();
        return Math.sqrt(thisCenter.getSquaredDistance(otherCenter));
    }
}
```

### 3.2 Storage System

**Thread-Safe Concurrent Storage**:
```java
public class PlacementTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger("PlacementTracker");

    // Map: Structure ID -> List of placements
    private static final ConcurrentHashMap<Identifier, CopyOnWriteArrayList<PlacedStructure>>
        PLACEMENTS = new ConcurrentHashMap<>();

    public static void record(Identifier id, ChunkPos pos, BlockBox box) {
        PlacedStructure placement = new PlacedStructure(
            id,
            pos,
            box,
            System.currentTimeMillis()
        );

        PLACEMENTS.computeIfAbsent(id, k -> new CopyOnWriteArrayList<>())
                  .add(placement);

        LOGGER.debug("Tracked: {} at chunk {} (total: {})",
            id, pos, PLACEMENTS.get(id).size());
    }

    public static List<PlacedStructure> getAll(Identifier id) {
        return PLACEMENTS.getOrDefault(id, new CopyOnWriteArrayList<>());
    }

    public static int getCount(Identifier id) {
        return PLACEMENTS.getOrDefault(id, new CopyOnWriteArrayList<>()).size();
    }

    public static void clear() {
        PLACEMENTS.clear();
    }
}
```

**Why CopyOnWriteArrayList**: Structure generation is bursty (many placements during worldgen, rare afterwards). CopyOnWriteArrayList optimizes for reads over writes, perfect for this use case.

**Memory Estimate**: ~200 bytes per placement × 1000 structures = ~200 KB (negligible)

---

## 4. Distance Calculation and Verification

### 4.1 Distance Metrics

**Chunk Distance** (simplest):
```java
public static int chunkDistance(ChunkPos a, ChunkPos b) {
    int dx = Math.abs(a.x - b.x);
    int dz = Math.abs(a.z - b.z);
    return Math.max(dx, dz); // Chebyshev distance (chunk grid)
}
```

**Block Distance** (more accurate):
```java
public static double blockDistance(BlockPos a, BlockPos b) {
    return Math.sqrt(a.getSquaredDistance(b));
}
```

**Minecraft Spacing Distance** (what spacing/separation use):
```java
// Minecraft uses chunk distance for structure placement
// spacing = max distance between structure attempts
// separation = min distance between structure attempts
```

### 4.2 Statistical Analysis

**Average Spacing Calculation**:
```java
public static double calculateAverageSpacing(Identifier structureId) {
    List<PlacedStructure> placements = PlacementTracker.getAll(structureId);

    if (placements.size() < 2) {
        return 0.0; // Need at least 2 placements
    }

    // Calculate pairwise distances
    List<Double> distances = new ArrayList<>();

    for (int i = 0; i < placements.size(); i++) {
        for (int j = i + 1; j < placements.size(); j++) {
            double distance = placements.get(i).distanceTo(placements.get(j));
            distances.add(distance);
        }
    }

    // Average distance
    double sum = distances.stream().mapToDouble(d -> d).sum();
    return sum / distances.size();
}
```

**Minimum Distance** (verify separation):
```java
public static double calculateMinimumSpacing(Identifier structureId) {
    List<PlacedStructure> placements = PlacementTracker.getAll(structureId);

    if (placements.size() < 2) {
        return Double.MAX_VALUE;
    }

    double minDistance = Double.MAX_VALUE;

    for (int i = 0; i < placements.size(); i++) {
        for (int j = i + 1; j < placements.size(); j++) {
            double distance = placements.get(i).distanceTo(placements.get(j));
            minDistance = Math.min(minDistance, distance);
        }
    }

    return minDistance;
}
```

**Expected vs Actual Comparison**:
```java
public static void verifySpacing(Identifier structureId) {
    // Get vanilla spacing from config
    ConfigManager config = ConfigManager.getInstance();
    int vanillaSpacing = getVanillaSpacing(structureId); // From Minecraft data
    double multiplier = config.getSpacingMultiplier(...);

    double expectedSpacing = vanillaSpacing * multiplier * 16; // Convert chunks to blocks
    double actualSpacing = calculateAverageSpacing(structureId);

    double variance = Math.abs(expectedSpacing - actualSpacing) / expectedSpacing;

    LOGGER.info("Verification for {}: expected {:.1f} blocks, actual {:.1f} blocks (variance: {:.1f}%)",
        structureId, expectedSpacing, actualSpacing, variance * 100);

    if (variance < 0.2) { // Within 20%
        LOGGER.info("✅ PASS: Spacing within acceptable variance");
    } else {
        LOGGER.warn("⚠️ WARN: Spacing variance exceeds 20%");
    }
}
```

---

## 5. Logging and Observability

### 5.1 Logging Strategy

**During Generation** (DEBUG level):
```java
LOGGER.debug("Structure placed: {} at chunk {} (BB: {})",
    structureId, chunkPos, boundingBox);
```

**After Generation** (INFO level):
```java
LOGGER.info("Structure placement summary:");
LOGGER.info("  minecraft:village_plains: 47 placements, avg spacing 1250 blocks");
LOGGER.info("  minecraft:ancient_city: 3 placements, avg spacing 8400 blocks");
```

**Verification Results** (INFO level):
```java
LOGGER.info("Spacing verification:");
LOGGER.info("  ✅ minecraft:village_plains: expected 1200, actual 1250 (variance: 4.2%)");
LOGGER.info("  ✅ minecraft:ancient_city: expected 8000, actual 8400 (variance: 5.0%)");
```

### 5.2 Debug Command

**Create `/xeenaa stats` command**:
```java
public class StructureStatsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("xeenaa")
                .then(literal("stats")
                    .executes(ctx -> {
                        printStatistics(ctx.getSource());
                        return 1;
                    })
                    .then(argument("structure", IdentifierArgumentType.identifier())
                        .executes(ctx -> {
                            Identifier id = IdentifierArgumentType.getIdentifier(ctx, "structure");
                            printStructureDetails(ctx.getSource(), id);
                            return 1;
                        })
                    )
                )
        );
    }

    private static void printStatistics(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("=== Structure Placement Statistics ==="), false);

        for (Identifier id : PlacementTracker.getAllStructures()) {
            int count = PlacementTracker.getCount(id);
            double avgSpacing = PlacementTracker.calculateAverageSpacing(id);

            source.sendFeedback(() -> Text.literal(
                String.format("%s: %d placements, avg %.1f blocks apart",
                    id, count, avgSpacing)
            ), false);
        }
    }
}
```

---

## 6. Export for External Analysis

### 6.1 JSON Export Format

```json
{
  "minecraft:village_plains": [
    {
      "chunkPos": {"x": 10, "z": -5},
      "boundingBox": {"minX": 150, "minY": 64, "minZ": -80, "maxX": 200, "maxY": 90, "maxZ": -30},
      "center": {"x": 175, "y": 77, "z": -55},
      "timestamp": 1729800000000
    },
    {
      "chunkPos": {"x": 50, "z": 12},
      "boundingBox": {"minX": 800, "minY": 64, "minZ": 192, "maxX": 850, "maxY": 90, "maxZ": 242},
      "center": {"x": 825, "y": 77, "z": 217},
      "timestamp": 1729800001234
    }
  ],
  "minecraft:ancient_city": [
    {
      "chunkPos": {"x": -20, "z": -30},
      "boundingBox": {"minX": -320, "minY": -40, "minZ": -480, "maxX": -200, "maxY": -10, "maxZ": -360},
      "center": {"x": -260, "y": -25, "z": -420},
      "timestamp": 1729800002345
    }
  ]
}
```

### 6.2 Export Command

```java
public class ExportCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("xeenaa")
                .then(literal("export")
                    .executes(ctx -> {
                        exportToJson(ctx.getSource());
                        return 1;
                    })
                )
        );
    }

    private static void exportToJson(ServerCommandSource source) {
        Path exportPath = Paths.get("config/xeenaa-structure-placements.json");

        try {
            JsonObject root = new JsonObject();

            for (Identifier id : PlacementTracker.getAllStructures()) {
                JsonArray placements = new JsonArray();

                for (PlacedStructure p : PlacementTracker.getAll(id)) {
                    JsonObject placement = new JsonObject();

                    // Add chunk pos
                    JsonObject chunkPos = new JsonObject();
                    chunkPos.addProperty("x", p.chunkPos().x);
                    chunkPos.addProperty("z", p.chunkPos().z);
                    placement.add("chunkPos", chunkPos);

                    // Add bounding box
                    BlockBox bb = p.boundingBox();
                    JsonObject boundingBox = new JsonObject();
                    boundingBox.addProperty("minX", bb.getMinX());
                    boundingBox.addProperty("minY", bb.getMinY());
                    boundingBox.addProperty("minZ", bb.getMinZ());
                    boundingBox.addProperty("maxX", bb.getMaxX());
                    boundingBox.addProperty("maxY", bb.getMaxY());
                    boundingBox.addProperty("maxZ", bb.getMaxZ());
                    placement.add("boundingBox", boundingBox);

                    // Add center
                    BlockPos center = p.getCenter();
                    JsonObject centerPos = new JsonObject();
                    centerPos.addProperty("x", center.getX());
                    centerPos.addProperty("y", center.getY());
                    centerPos.addProperty("z", center.getZ());
                    placement.add("center", centerPos);

                    // Add timestamp
                    placement.addProperty("timestamp", p.timestamp());

                    placements.add(placement);
                }

                root.add(id.toString(), placements);
            }

            // Write to file
            Files.writeString(exportPath, new GsonBuilder().setPrettyPrinting().create().toJson(root));

            source.sendFeedback(() -> Text.literal(
                "Exported structure placements to " + exportPath
            ), false);

        } catch (IOException e) {
            source.sendError(Text.literal("Failed to export: " + e.getMessage()));
        }
    }
}
```

---

## 7. How Competitors Do It

### 7.1 Sparse Structures Mod

**Approach**: Multiplies spacing/separation globally

**Verification**: None built-in (relies on user observation)

**Testing**: Manual testing by kshrubb reported on mod page

**Takeaway**: No automated verification - users manually explore and verify structures are spread out

### 7.2 Structurify Mod

**Approach**: Per-structure configuration with salt and frequency control

**Verification**: Uses flatness checks and biome checks (pre-generation validation)

**Testing**: Reports compatibility issues with mods that alter generation heavily

**Takeaway**: Focus on PREVENTING bad placements rather than TRACKING successful ones

**Key Insight**: Neither competitor implements automated verification - they rely on manual testing and user feedback!

### 7.3 What We Can Learn

1. **Manual verification is industry standard** - Users explore worlds and report issues
2. **Pre-generation validation** (biome checks, flatness) can prevent bad placements
3. **Statistical analysis** would be INNOVATIVE - neither competitor does this
4. **Debug commands** for placement statistics would be UNIQUE differentiator

---

## 8. Performance Impact Analysis

### 8.1 Mixin Overhead

**StructureStart.place() frequency**:
- Called once per structure placement
- Typical world: 100-500 structures generated during initial spawn area (10-chunk radius)
- Worldgen session: 1000-5000 structures (exploring, pregeneration)

**Tracking overhead per placement**:
- Create `PlacedStructure` record: ~50ns (trivial)
- `ConcurrentHashMap.computeIfAbsent()`: ~100ns (fast)
- `CopyOnWriteArrayList.add()`: ~50ns (write operation)
- Logging (DEBUG, disabled in production): 0ns
- **Total**: ~200ns = 0.0002ms per structure

**Impact on worldgen**: 5000 structures × 0.0002ms = **1ms total overhead** (negligible)

### 8.2 Memory Impact

**Per-structure memory**:
- `PlacedStructure` record: ~100 bytes (3 references + primitives)
- `CopyOnWriteArrayList` overhead: ~50 bytes
- **Total**: ~150 bytes per placement

**1000 structures**: 150 KB (0.15 MB)
**10000 structures**: 1.5 MB (large world, heavy modpack)

**Conclusion**: Memory impact negligible (<2MB worst case)

### 8.3 Statistical Calculation Overhead

**Average spacing calculation** (O(n²) pairwise distances):
- 100 structures: 4,950 distance calculations
- Each calculation: ~100ns (Math.sqrt)
- **Total**: 495 microseconds = 0.5ms

**Minimum spacing calculation** (O(n²)):
- Same complexity, similar performance

**Conclusion**: Statistical calculations are fast, can run on-demand

### 8.4 Export Overhead

**JSON serialization**:
- 1000 structures × ~200 bytes JSON each = ~200 KB file
- Serialization time: ~10ms
- Write to disk: ~50ms
- **Total**: ~60ms (one-time operation, user-triggered)

**Conclusion**: Export is cheap, no impact on gameplay

---

## 9. Implementation Recommendations

### 9.1 Epic 02 Scope (Verification & Testing)

**Recommended Deliverables**:

1. **PlacementTracker System**
   - `PlacedStructure` record
   - `ConcurrentHashMap` storage
   - Thread-safe tracking methods

2. **StructureStartMixin**
   - Inject into `StructureStart.place()`
   - Extract structure identifier
   - Record placement data
   - DEBUG logging

3. **Statistical Analysis**
   - Average spacing calculation
   - Minimum spacing calculation
   - Expected vs actual comparison
   - Variance reporting

4. **Debug Commands**
   - `/xeenaa stats` - Show all structure counts and spacing
   - `/xeenaa stats <structure>` - Detailed structure info
   - `/xeenaa export` - Export to JSON

5. **Verification Logging**
   - INFO level summary after worldgen
   - PASS/WARN indicators based on variance
   - Clear, actionable output

**NOT in Epic 02**:
- Visual map generation (future epic)
- Integration with profiling tools like Spark (Epic 04)
- Advanced analytics dashboard (future epic)
- Real-time tracking UI (future epic)

### 9.2 Testing Strategy

**Phase 1: Vanilla Structures**
1. Generate fresh world
2. Explore 10-chunk radius (spawn area)
3. Run `/xeenaa stats`
4. Verify counts match expectations (villages ~5-10, ancient cities 0-1, etc.)
5. Check average spacing matches config multipliers

**Phase 2: Modded Structures**
1. Install 5-10 structure mods (YUNG's, When Dungeons Arise, etc.)
2. Generate fresh world
3. Pregen 20-chunk radius
4. Run `/xeenaa stats`
5. Verify all modded structures are tracked
6. Check spacing multipliers apply correctly

**Phase 3: Stress Test**
1. Install 50+ structure mods
2. Generate world
3. Pregen 50-chunk radius
4. Run `/xeenaa stats`
5. Verify performance (worldgen time, memory usage)
6. Check for crashes or errors

**Phase 4: Multiplier Verification**
1. Set `type.town` to 3.0x
2. Generate world
3. Run `/xeenaa stats minecraft:village_plains`
4. Verify average spacing is ~3x vanilla
5. Repeat for other structure types

### 9.3 Acceptance Criteria

✅ **Epic 02 is complete when**:
1. Tracking mixin successfully captures all structure placements
2. `/xeenaa stats` shows accurate counts and spacing for all structures
3. Verification logs show PASS for vanilla structures with default config
4. Variance is within 20% of expected spacing for 90%+ of structures
5. Works correctly with 50+ structure mods
6. Performance overhead is <5ms for 1000 structures
7. Memory usage is <5MB for 10,000 structures
8. Export command generates valid JSON
9. User can confirm multipliers are working by comparing worlds

---

## 10. Alternative: Using Minecraft's /locate Command Programmatically

### 10.1 How /locate Works Internally

Minecraft's `/locate structure <structure>` command uses `ChunkGenerator.locateStructure()`:

```java
// LocateCommand.java (simplified)
public static int execute(
    ServerCommandSource source,
    RegistryEntry.Reference<Structure> structure
) {
    ServerWorld world = source.getWorld();
    BlockPos sourcePos = BlockPos.ofFloored(source.getPosition());

    // Search for nearest structure
    Pair<BlockPos, RegistryEntry<Structure>> result =
        world.getChunkManager()
             .getChunkGenerator()
             .locateStructure(world, structure, sourcePos, 100, false);

    if (result != null) {
        BlockPos structurePos = result.getFirst();
        int distance = MathHelper.floor(getDistance(sourcePos.getX(), sourcePos.getZ(),
            structurePos.getX(), structurePos.getZ()));

        // Send message to chat
        source.sendFeedback(...);
    }

    return result != null ? 1 : 0;
}
```

**Key Method**: `ChunkGenerator.locateStructure()`

### 10.2 Using locateStructure() for Verification

**Approach**: Call `locateStructure()` programmatically from multiple positions to sample structure distribution

**Pros**:
- No mixin required
- Uses vanilla Minecraft logic
- Guaranteed to match `/locate` command

**Cons**:
- VERY EXPENSIVE (searches chunks, can cause lag)
- Only finds NEAREST structure (not all structures)
- Requires many calls from different positions to get good sample
- May trigger chunk generation (side effects)

**Conclusion**: ❌ Not suitable for automated verification (too expensive)

### 10.3 When to Use /locate Approach

**Good for**:
- Manual testing (player uses `/locate` to verify structures exist)
- Smoke testing (verify at least one instance of each structure spawns)
- User-driven verification (not automated)

**Example Test**:
```java
// Manual test: Verify villages spawn with 3.0x spacing
// 1. Use /locate structure minecraft:village_plains
// 2. Teleport to village
// 3. Use /locate again from village location
// 4. Measure distance between villages
// 5. Compare to expected (vanilla_spacing * 3.0)
```

---

## 11. Research References

### 11.1 Minecraft Source Code

- `net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator` - Structure placement logic
- `net.minecraft.structure.StructureStart` - Structure instance representation
- `net.minecraft.structure.StructurePiece` - Individual structure piece
- `net.minecraft.world.gen.StructureAccessor` - Interface for accessing structure data
- `net.minecraft.world.gen.chunk.ChunkGenerator` - Chunk generation orchestrator
- `net.minecraft.server.command.LocateCommand` - /locate command implementation

### 11.2 Fabric API Documentation

- Fabric API 0.110.0+1.21.1: No structure generation events
- BiomeModifications API: For adding structures, not tracking them
- Events documentation: Custom events require mixin-based implementation

### 11.3 Yarn Mappings

- Yarn 1.21.1+build.X - Deobfuscated Minecraft class names
- StructureStart: https://maven.fabricmc.net/docs/yarn-1.20.1-rc1+build.2/net/minecraft/structure/StructureStart.html
- ChunkGenerator: https://maven.fabricmc.net/docs/yarn-1.21.5+build.1/net/minecraft/world/gen/chunk/ChunkGenerator.html

### 11.4 Competitor Mods

- Sparse Structures: Global spacing multiplier, no verification
- Structurify: Per-structure config, pre-generation validation, no tracking
- Neither mod implements automated placement verification

### 11.5 Community Resources

- Minecraft Wiki: /locate command documentation
- Fabric Wiki: Events and callbacks tutorial
- SpigotMC Forums: StructureStart, StructureBoundingBox discussion

---

## 12. Open Questions and Future Research

### 12.1 Unanswered Questions

1. **How to extract Structure identifier from StructureStart?**
   - Possible: Use accessor mixin or reflection
   - Need to test: Which approach is cleaner?

2. **Does StructureStart.place() fire for failed placements?**
   - Hypothesis: Only fires for successful placements
   - Need to test: Generate world, count placements vs expected attempts

3. **How to handle structures spanning multiple chunks?**
   - Current approach: Track by origin ChunkPos (correct)
   - Verification: Bounding box may extend into neighboring chunks (expected)

4. **What is the actual variance in structure spacing due to randomness?**
   - Minecraft uses salt and random spread
   - Expected variance: 10-30% from average (need empirical data)

### 12.2 Future Epic Ideas

**Epic 03: Visual Verification Tools**
- Web-based map showing structure locations
- Color-coded by structure type
- Distance overlay showing spacing violations
- Export to image or HTML

**Epic 04: Advanced Performance Analysis**
- Integration with Spark Profiler
- Real-time structure generation metrics
- Comparison with vanilla worldgen performance
- Automated regression testing

**Epic 05: Smart Verification**
- Statistical distribution analysis (Poisson distribution expected)
- Outlier detection (structures too close/far)
- Automatic config recommendations
- Machine learning to optimize multipliers

---

## 13. Conclusion

### 13.1 Final Recommendation

**Best Approach for Epic 02**: Mixin into `StructureStart.place()` with concurrent tracking

**Implementation Steps**:
1. Create `PlacedStructure` record and `PlacementTracker` singleton
2. Create `StructureStartMixin` with `@Inject` at `place()` method HEAD
3. Extract structure identifier via accessor or reflection
4. Record placement data in concurrent map
5. Add `/xeenaa stats` command for querying data
6. Add verification logging (expected vs actual spacing)
7. Add optional `/xeenaa export` for JSON export

**Estimated Effort**: 4-6 hours (one task in Epic 02)

**Success Metrics**:
- ✅ All structure placements tracked
- ✅ Statistics match manual exploration
- ✅ Variance within 20% of expected
- ✅ Works with 50+ mods
- ✅ Performance overhead <5ms
- ✅ Memory usage <5MB

### 13.2 Why This Approach Wins

1. **Accuracy**: Captures actual placements, not attempts
2. **Performance**: Negligible overhead (<1ms for 5000 structures)
3. **Simplicity**: Single mixin, single storage class
4. **Flexibility**: Easy to extend with export, visualization
5. **Innovative**: Neither competitor implements this
6. **User-Friendly**: Debug commands provide immediate feedback

### 13.3 Risks and Mitigations

| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| Mixin fails to capture placements | Low | High | Test thoroughly with vanilla structures first |
| Structure identifier extraction fails | Medium | High | Use accessor mixin or fallback to toString() |
| Variance too high (>30%) | Medium | Medium | Document expected variance, adjust thresholds |
| Memory leak from unbounded growth | Low | Medium | Add clear() method, call on world unload |
| Performance regression | Very Low | High | Benchmark before/after, disable if needed |

---

**Research completed**: 2025-10-24
**Confidence level**: High (80%+)
**Ready for implementation**: ✅ Yes
**Next step**: Create Epic 02 tasks for verification system
