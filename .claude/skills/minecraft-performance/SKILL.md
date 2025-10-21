---
name: minecraft-performance
description: Performance optimization strategies for Minecraft 1.21.1 modding including tick budget management, entity optimization, memory efficiency, profiling techniques, and common performance pitfalls. Use when implementing performance-critical features or diagnosing lag issues.
allowed-tools: [Read, Grep, Glob, WebFetch, WebSearch]
---

# Minecraft Performance Optimization Skill

Performance optimization strategies and best practices for Minecraft 1.21.1 modding.

## Fundamental Performance Constraints

### Tick Budget
**Critical Rule**: Minecraft runs at 20 TPS (ticks per second)
- **Tick Duration**: 50ms maximum per tick
- **Performance Target**: Keep tick time under 45ms (10% safety margin)
- **Warning Threshold**: 47ms+ tick time causes "server running behind" warnings
- **Critical Threshold**: 50ms+ causes TPS drop and game slowdown

**Tick Time Breakdown** (typical server):
```
Total: 50ms budget
├── Entity Ticking: ~25-30ms (50-60%)
├── Block Updates: ~8-10ms (15-20%)
├── Chunk Operations: ~3-5ms (5-10%)
├── Networking: ~2-4ms (5-8%)
└── Other Systems: ~5-8ms (10-15%)
```

**Your Mod's Budget**:
- Single mod should use **< 1-2ms per tick** maximum
- Popular servers run 20-50+ mods
- Your 2ms becomes 40-100ms across all mods = server lag

### Memory Constraints
**Java Heap**: Minecraft typically runs with 2-8GB heap
- **Client**: Usually 4-6GB allocated
- **Server**: 2GB minimum, 8-16GB typical for large servers
- **Your Mod**: Should use < 50-100MB for typical features

**Garbage Collection Impact**:
- GC pauses freeze game (including rendering)
- Minor GC: 5-10ms pause
- Major GC: 50-500ms pause (causes visible stutter)
- **Goal**: Minimize object allocation to reduce GC pressure

## Performance Measurement

### Profiling Tools

#### 1. Spark Profiler (Recommended)
**Best for**: Production servers, detailed CPU profiling

```java
// Download: https://spark.lucko.me/
// Install as mod on client/server

// Commands:
/spark profiler start
/spark profiler stop
/spark profiler open

// Generates interactive HTML report showing:
// - Method call times
// - Tick time breakdown
// - Entity type performance
// - Plugin/mod performance
```

#### 2. Java Flight Recorder (JFR)
**Best for**: Memory profiling, GC analysis

```bash
# Start Minecraft with JFR
java -XX:+UnlockDiagnosticVMOptions \
     -XX:+DebugNonSafepoints \
     -XX:StartFlightRecording=filename=recording.jfr \
     -jar server.jar

# Analyze with JDK Mission Control
jmc recording.jfr
```

#### 3. Minecraft Debug Screen (F3)
**Best for**: Quick checks during development

```
F3 screen shows:
- Current TPS (target: 20.0)
- Tick time (ms) - should be < 50ms
- Memory usage (MB)
- Entity count
- Chunk statistics
```

#### 4. Manual Timing in Code
```java
// Measure method execution time
long startTime = System.nanoTime();
try {
    expensiveOperation();
} finally {
    long duration = System.nanoTime() - startTime;
    if (duration > 1_000_000) { // > 1ms
        LOGGER.warn("expensiveOperation took {}ms",
            duration / 1_000_000.0);
    }
}

// Measure average over multiple ticks
private long totalTime = 0;
private int tickCount = 0;

public void tick() {
    long start = System.nanoTime();
    doWork();
    totalTime += (System.nanoTime() - start);
    tickCount++;

    if (tickCount >= 100) {
        LOGGER.info("Average tick time: {}ms",
            (totalTime / tickCount) / 1_000_000.0);
        totalTime = 0;
        tickCount = 0;
    }
}
```

### Performance Benchmarking

**Before/After Comparison** (required for performance features):

```java
public class PerformanceBenchmark {
    /**
     * Benchmark: Guard pathfinding optimization
     * Test: 100 guards finding paths to random targets
     * Environment: Superflat world, 10 chunk radius
     */
    @Test
    public void benchmarkGuardPathfinding() {
        // Setup
        List<GuardEntity> guards = createGuards(100);
        List<BlockPos> targets = generateRandomTargets(100);

        // Baseline (before optimization)
        long baselineTime = measurePathfinding(guards, targets);

        // Apply optimization
        enablePathfindingCache();

        // Optimized
        long optimizedTime = measurePathfinding(guards, targets);

        // Results
        double improvement = (baselineTime - optimizedTime) / (double) baselineTime * 100;
        LOGGER.info("Pathfinding improvement: {:.1f}% faster", improvement);
        LOGGER.info("Baseline: {}ms, Optimized: {}ms",
            baselineTime / 1_000_000, optimizedTime / 1_000_000);

        // Assert improvement
        assertTrue(optimizedTime < baselineTime * 0.8, // 20%+ improvement
            "Optimization should improve performance by at least 20%");
    }
}
```

## Entity Performance Optimization

### Entity Count Management

**Problem**: 1000 entities ticking = ~30ms per tick

```java
// ❌ BAD - spawn unlimited entities
public void hireGuard(PlayerEntity player, GuardType type) {
    spawnGuard(player, type); // No limit!
}

// ✅ GOOD - limit entity count
public class GuardManager {
    private static final int MAX_GUARDS_PER_PLAYER = 10;
    private static final int MAX_GUARDS_GLOBAL = 200;

    public boolean canHireGuard(PlayerEntity player) {
        int playerGuards = countGuardsForPlayer(player);
        if (playerGuards >= MAX_GUARDS_PER_PLAYER) {
            return false;
        }

        int totalGuards = countAllGuards(player.getWorld());
        if (totalGuards >= MAX_GUARDS_GLOBAL) {
            return false;
        }

        return true;
    }

    private int countGuardsForPlayer(PlayerEntity player) {
        // Cache this result - don't iterate every time!
        return guardRegistry.getGuardsForPlayer(player.getUuid()).size();
    }
}
```

### Entity Tick Optimization

**Tick Throttling**: Not all entities need to tick every tick

```java
// ✅ GOOD - tick throttling
public class GuardEntity extends PathAwareEntity {
    private int tickCounter = 0;

    @Override
    public void tick() {
        super.tick();
        tickCounter++;

        // Expensive AI updates every 10 ticks (0.5s) instead of every tick
        if (tickCounter % 10 == 0) {
            updateExpensiveAI();
        }

        // Very expensive operations every 100 ticks (5s)
        if (tickCounter % 100 == 0) {
            recheckAllNearbyTargets();
        }
    }

    private void updateExpensiveAI() {
        // Pathfinding recalculation
        // Target selection
        // Behavior state machine
    }
}
```

**Distance-Based Optimization**: Entities far from players can tick less

```java
public class GuardEntity extends PathAwareEntity {
    @Override
    public void tick() {
        super.tick();

        ServerPlayerEntity nearestPlayer = getNearestPlayer();
        if (nearestPlayer == null) {
            // No players nearby - minimal ticking
            tickIdle();
            return;
        }

        double distance = squaredDistanceTo(nearestPlayer);

        if (distance > 64 * 64) { // > 64 blocks
            // Far away - tick every 20 ticks (1s)
            if (age % 20 == 0) {
                tickDistant();
            }
        } else if (distance > 32 * 32) { // > 32 blocks
            // Medium distance - tick every 10 ticks (0.5s)
            if (age % 10 == 0) {
                tickMedium();
            }
        } else {
            // Close - full tick rate
            tickNearby();
        }
    }
}
```

### Pathfinding Optimization

**Problem**: Pathfinding is extremely expensive (A* algorithm)

```java
// ❌ BAD - recalculate path every tick
public void tick() {
    if (hasTarget()) {
        navigation.startMovingTo(target, 1.0); // Recalculates every tick!
    }
}

// ✅ GOOD - cache paths, recalculate only when needed
public class GuardEntity extends PathAwareEntity {
    private BlockPos lastTargetPos = null;
    private int ticksSincePathRecalc = 0;

    @Override
    public void tick() {
        super.tick();

        if (!hasTarget()) return;

        BlockPos currentTargetPos = target.getBlockPos();
        ticksSincePathRecalc++;

        // Recalculate only when:
        // 1. Target moved significantly (> 4 blocks)
        // 2. Path blocked (navigation failed)
        // 3. 100 ticks elapsed (5 seconds)
        boolean targetMoved = lastTargetPos == null ||
            lastTargetPos.getSquaredDistance(currentTargetPos) > 16;
        boolean pathBlocked = navigation.isIdle();
        boolean timeout = ticksSincePathRecalc > 100;

        if (targetMoved || pathBlocked || timeout) {
            navigation.startMovingTo(currentTargetPos, 1.0);
            lastTargetPos = currentTargetPos;
            ticksSincePathRecalc = 0;
        }
    }
}
```

**Pathfinding Limits**:
```java
public class GuardEntity extends PathAwareEntity {
    @Override
    protected void initGoals() {
        // Limit pathfinding distance
        this.goalSelector.add(1, new MeleeAttackGoal(
            this,
            1.0,
            false
        ) {
            @Override
            protected double getSquaredMaxAttackDistance(LivingEntity entity) {
                // Don't pathfind beyond 32 blocks
                return 32.0 * 32.0;
            }
        });
    }
}
```

### Entity Removal and Cleanup

```java
// ✅ GOOD - remove entities when not needed
public class GuardEntity extends PathAwareEntity {
    private static final int MAX_IDLE_TIME = 6000; // 5 minutes
    private int idleTicks = 0;

    @Override
    public void tick() {
        super.tick();

        if (getOwner() == null) {
            // Owner offline - become idle
            idleTicks++;
            if (idleTicks > MAX_IDLE_TIME) {
                LOGGER.debug("Removing guard {} - owner offline too long",
                    getUuid());
                discard(); // Remove entity
            }
        } else {
            idleTicks = 0;
        }
    }
}
```

## Memory Optimization

### Object Allocation Reduction

**Problem**: Excessive object allocation causes GC pressure

```java
// ❌ BAD - creates new object every tick
public void tick() {
    List<GuardEntity> nearbyGuards = new ArrayList<>(); // Allocates every tick!
    for (Entity entity : getWorld().getOtherEntities(this, getBoundingBox().expand(10))) {
        if (entity instanceof GuardEntity guard) {
            nearbyGuards.add(guard);
        }
    }
}

// ✅ GOOD - reuse list
public class GuardEntity extends PathAwareEntity {
    private final List<GuardEntity> nearbyGuardsCache = new ArrayList<>();
    private int cacheAge = 0;

    public List<GuardEntity> getNearbyGuards() {
        // Recalculate every 20 ticks (1s)
        if (cacheAge++ > 20) {
            nearbyGuardsCache.clear();
            for (Entity entity : getWorld().getOtherEntities(this, getBoundingBox().expand(10))) {
                if (entity instanceof GuardEntity guard) {
                    nearbyGuardsCache.add(guard);
                }
            }
            cacheAge = 0;
        }
        return nearbyGuardsCache;
    }
}
```

### Caching Strategies

**Computed Value Caching**:
```java
public class GuardEntity extends PathAwareEntity {
    private float cachedAttackDamage = -1;
    private int cachedAttackDamageTick = -1;

    public float getAttackDamage() {
        // Cache attack damage calculation (involves equipment, level, buffs)
        if (cachedAttackDamageTick != age) {
            cachedAttackDamage = calculateAttackDamage();
            cachedAttackDamageTick = age;
        }
        return cachedAttackDamage;
    }

    private float calculateAttackDamage() {
        // Expensive calculation
        float base = getBaseAttackDamage();
        float equipment = getEquipmentBonus();
        float level = getLevelBonus();
        return base + equipment + level;
    }

    public void onEquipmentChanged() {
        // Invalidate cache when equipment changes
        cachedAttackDamageTick = -1;
    }
}
```

**Query Result Caching**:
```java
public class GuardRegistry {
    private final Map<UUID, List<Guard>> playerGuardsCache = new HashMap<>();
    private final Map<UUID, Integer> cacheTimestamps = new HashMap<>();
    private static final int CACHE_DURATION = 20; // 1 second

    public List<Guard> getGuardsForPlayer(UUID playerUuid) {
        int currentTick = getCurrentTick();
        Integer lastUpdate = cacheTimestamps.get(playerUuid);

        // Cache hit - within 1 second
        if (lastUpdate != null && currentTick - lastUpdate < CACHE_DURATION) {
            return playerGuardsCache.get(playerUuid);
        }

        // Cache miss - recalculate
        List<Guard> guards = recalculateGuardsForPlayer(playerUuid);
        playerGuardsCache.put(playerUuid, guards);
        cacheTimestamps.put(playerUuid, currentTick);
        return guards;
    }

    public void invalidateCacheForPlayer(UUID playerUuid) {
        cacheTimestamps.remove(playerUuid);
    }
}
```

### Efficient Data Structures

```java
// ❌ BAD - linear search through list
private List<GuardEntity> allGuards = new ArrayList<>();

public GuardEntity findGuardByUuid(UUID uuid) {
    for (GuardEntity guard : allGuards) { // O(n) lookup!
        if (guard.getUuid().equals(uuid)) {
            return guard;
        }
    }
    return null;
}

// ✅ GOOD - use HashMap for O(1) lookup
private Map<UUID, GuardEntity> guardsByUuid = new HashMap<>();

public GuardEntity findGuardByUuid(UUID uuid) {
    return guardsByUuid.get(uuid); // O(1) lookup
}
```

**Choose Right Collection**:
- `HashMap<K,V>` - Fast key lookup (O(1))
- `ArrayList<T>` - Fast index access, iteration
- `LinkedList<T>` - Fast insertion/removal (avoid for iteration)
- `HashSet<T>` - Fast contains check (O(1))
- `EnumMap<K,V>` - Fastest for enum keys
- `it.unimi.dsi.fastutil.*` - Primitive collections (no boxing overhead)

## Chunk and World Performance

### Chunk Loading Optimization

```java
// ❌ BAD - force load chunks unnecessarily
public void spawnGuard(ServerWorld world, BlockPos pos) {
    world.setChunkForced(pos.getX() >> 4, pos.getZ() >> 4, true); // Keeps chunk loaded!
    world.spawnEntity(new GuardEntity(world));
}

// ✅ GOOD - only load chunks when necessary
public void spawnGuard(ServerWorld world, BlockPos pos) {
    // Check if chunk is already loaded
    if (!world.isChunkLoaded(pos)) {
        LOGGER.warn("Cannot spawn guard at {} - chunk not loaded", pos);
        return;
    }

    world.spawnEntity(new GuardEntity(world));
    // Don't force chunk to stay loaded
}
```

### Chunk Ticket Management

```java
// ✅ GOOD - use chunk tickets responsibly
public class GuardPostBlockEntity extends BlockEntity {
    private ChunkPos loadedChunk = null;

    public void setActive(boolean active) {
        ServerWorld world = (ServerWorld) getWorld();
        if (world == null) return;

        if (active && loadedChunk == null) {
            // Add ticket to keep chunk loaded
            ChunkPos pos = new ChunkPos(getPos());
            world.getChunkManager().addTicket(
                ChunkTicketType.START,
                pos,
                2, // Load radius (2 chunks)
                pos
            );
            loadedChunk = pos;
            LOGGER.debug("Added chunk ticket at {}", pos);
        } else if (!active && loadedChunk != null) {
            // Remove ticket when no longer needed
            world.getChunkManager().removeTicket(
                ChunkTicketType.START,
                loadedChunk,
                2,
                loadedChunk
            );
            loadedChunk = null;
            LOGGER.debug("Removed chunk ticket at {}", loadedChunk);
        }
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        // Clean up ticket when block entity removed
        setActive(false);
    }
}
```

### Block Entity Ticking

```java
// ❌ BAD - tick unnecessarily
public class GuardPostBlockEntity extends BlockEntity implements BlockEntityTicker {
    @Override
    public void tick() {
        // Ticks every tick even when idle!
        checkForNearbyGuards();
    }
}

// ✅ GOOD - only tick when needed
public class GuardPostBlockEntity extends BlockEntity {
    private boolean needsTicking = false;

    public void setNeedsTicking(boolean needs) {
        if (this.needsTicking != needs) {
            this.needsTicking = needs;
            markDirty();
            // Update block entity ticker state
            if (world != null) {
                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
            }
        }
    }

    @Nullable
    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world, BlockState state, BlockEntityType<T> type) {
        // Only return ticker if block entity needs ticking
        GuardPostBlockEntity be = (GuardPostBlockEntity) world.getBlockEntity(pos);
        if (be != null && be.needsTicking) {
            return GuardPostBlockEntity::tick;
        }
        return null; // Don't tick
    }
}
```

## Network Performance

### Packet Size Optimization

```java
// ❌ BAD - send entire entity data every update
public record GuardSyncPayload(
    UUID guardUuid,
    String name,
    GuardType type,
    int level,
    float health,
    float maxHealth,
    int experience,
    ItemStack helmet,
    ItemStack chestplate,
    ItemStack leggings,
    ItemStack boots,
    ItemStack mainHand
    // ... 50+ more fields
) implements CustomPayload {
    // Huge packet (500+ bytes)
}

// ✅ GOOD - send only changed data
public record GuardDeltaSyncPayload(
    UUID guardUuid,
    @Nullable String name,        // Only if changed
    @Nullable Integer level,      // Only if changed
    @Nullable Float health        // Only if changed
    // etc.
) implements CustomPayload {
    // Small packet (< 50 bytes typically)
}
```

### Packet Frequency Reduction

```java
// ❌ BAD - send packet every tick
public void tick() {
    if (!world.isClient) {
        syncToClients(); // 20 packets/second per guard!
    }
}

// ✅ GOOD - send only when changed
public class GuardEntity extends PathAwareEntity {
    private float lastSyncedHealth = -1;
    private int lastSyncedLevel = -1;
    private int ticksSinceSync = 0;

    @Override
    public void tick() {
        super.tick();

        if (world.isClient) return;

        boolean healthChanged = Math.abs(getHealth() - lastSyncedHealth) > 0.5f;
        boolean levelChanged = getLevel() != lastSyncedLevel;
        boolean timeout = ticksSinceSync++ > 100; // Force sync every 5s

        if (healthChanged || levelChanged || timeout) {
            syncToClients();
            lastSyncedHealth = getHealth();
            lastSyncedLevel = getLevel();
            ticksSinceSync = 0;
        }
    }
}
```

### Bulk Operations

```java
// ❌ BAD - send individual packets
public void syncAllGuards() {
    for (GuardEntity guard : getAllGuards()) {
        ServerPlayNetworking.send(player, new GuardSyncPayload(guard));
        // 100 guards = 100 packets!
    }
}

// ✅ GOOD - batch into single packet
public record BulkGuardSyncPayload(
    List<GuardData> guards
) implements CustomPayload {
    // 100 guards in 1 packet
}

public void syncAllGuards() {
    List<GuardData> guardData = getAllGuards().stream()
        .map(GuardData::fromEntity)
        .toList();
    ServerPlayNetworking.send(player, new BulkGuardSyncPayload(guardData));
}
```

## CPU Optimization

### Algorithm Complexity

```java
// ❌ BAD - O(n²) complexity
public List<GuardEntity> findNearbyGuards(GuardEntity guard) {
    List<GuardEntity> result = new ArrayList<>();
    List<GuardEntity> allGuards = getAllGuards(); // n guards

    for (GuardEntity other : allGuards) { // n iterations
        for (GuardEntity check : allGuards) { // n iterations = n²
            if (guard.squaredDistanceTo(check) < 100) {
                result.add(check);
            }
        }
    }
    return result;
}

// ✅ GOOD - O(n) complexity with spatial partitioning
public class GuardSpatialIndex {
    private Map<ChunkPos, List<GuardEntity>> guardsByChunk = new HashMap<>();

    public List<GuardEntity> findNearbyGuards(GuardEntity guard, double radius) {
        List<GuardEntity> result = new ArrayList<>();
        ChunkPos centerChunk = guard.getChunkPos();
        int chunkRadius = (int) Math.ceil(radius / 16.0);

        // Only check guards in nearby chunks
        for (int x = -chunkRadius; x <= chunkRadius; x++) {
            for (int z = -chunkRadius; z <= chunkRadius; z++) {
                ChunkPos chunkPos = new ChunkPos(
                    centerChunk.x + x,
                    centerChunk.z + z
                );
                List<GuardEntity> guards = guardsByChunk.get(chunkPos);
                if (guards != null) {
                    for (GuardEntity other : guards) {
                        if (guard.squaredDistanceTo(other) < radius * radius) {
                            result.add(other);
                        }
                    }
                }
            }
        }
        return result;
    }
}
```

### Lazy Evaluation

```java
// ❌ BAD - calculate everything upfront
public GuardStats getGuardStats(GuardEntity guard) {
    int attackPower = calculateAttackPower(guard);      // Expensive
    int defense = calculateDefense(guard);              // Expensive
    int speed = calculateSpeed(guard);                  // Expensive
    int luck = calculateLuck(guard);                    // Expensive
    return new GuardStats(attackPower, defense, speed, luck);
}

// ✅ GOOD - calculate only when accessed
public class GuardStats {
    private final GuardEntity guard;
    private Integer cachedAttackPower = null;
    private Integer cachedDefense = null;

    public int getAttackPower() {
        if (cachedAttackPower == null) {
            cachedAttackPower = calculateAttackPower(guard);
        }
        return cachedAttackPower;
    }

    public int getDefense() {
        if (cachedDefense == null) {
            cachedDefense = calculateDefense(guard);
        }
        return cachedDefense;
    }

    public void invalidate() {
        cachedAttackPower = null;
        cachedDefense = null;
    }
}
```

### Avoid String Operations

```java
// ❌ BAD - string concatenation in loops
public String formatGuardList(List<GuardEntity> guards) {
    String result = "";
    for (GuardEntity guard : guards) {
        result += guard.getName() + ", "; // Creates new String each iteration!
    }
    return result;
}

// ✅ GOOD - use StringBuilder
public String formatGuardList(List<GuardEntity> guards) {
    StringBuilder sb = new StringBuilder();
    for (GuardEntity guard : guards) {
        sb.append(guard.getName()).append(", ");
    }
    return sb.toString();
}

// ✅ EVEN BETTER - use Collectors
public String formatGuardList(List<GuardEntity> guards) {
    return guards.stream()
        .map(GuardEntity::getName)
        .collect(Collectors.joining(", "));
}
```

## Common Performance Pitfalls

### 1. World.getEntitiesByClass() in Tick
```java
// ❌ CATASTROPHIC - iterates ALL entities every tick
public void tick() {
    List<GuardEntity> guards = world.getEntitiesByClass(
        GuardEntity.class,
        getBoundingBox().expand(50),
        guard -> true
    ); // Checks every entity in world!
}

// ✅ SOLUTION - use spatial queries or maintain registry
public class GuardManager {
    private final Map<ChunkPos, Set<GuardEntity>> guardsByChunk = new HashMap<>();

    public List<GuardEntity> getNearbyGuards(BlockPos pos, double radius) {
        // Only check guards in relevant chunks
        return guardsByChunk.getOrDefault(new ChunkPos(pos), Set.of())
            .stream()
            .filter(g -> g.getPos().isWithinDistance(pos, radius))
            .toList();
    }
}
```

### 2. Excessive NBT/Component Serialization
```java
// ❌ BAD - serialize every tick
public void tick() {
    CompoundTag nbt = new CompoundTag();
    writeNbt(nbt); // Serializes entire entity state!
    syncToClients(nbt);
}

// ✅ GOOD - serialize only on save/load
public void tick() {
    // Don't serialize in tick!
}

@Override
public void writeNbt(CompoundTag nbt) {
    // Only called when saving to disk or syncing to client
    super.writeNbt(nbt);
}
```

### 3. Synchronous File I/O
```java
// ❌ CATASTROPHIC - blocks game thread
public void saveGuardData() {
    File file = new File("guards.json");
    Files.writeString(file.toPath(), toJson()); // BLOCKS for milliseconds!
}

// ✅ SOLUTION - use async I/O
public void saveGuardData() {
    CompletableFuture.runAsync(() -> {
        try {
            File file = new File("guards.json");
            Files.writeString(file.toPath(), toJson());
        } catch (IOException e) {
            LOGGER.error("Failed to save guard data", e);
        }
    });
}
```

### 4. Unnecessary Data Watchers
```java
// ❌ BAD - syncs every tick
@Override
protected void initDataTracker(DataTracker.Builder builder) {
    builder.add(ATTACK_POWER, 5); // Synced to client every change
}

public void tick() {
    // Recalculate every tick - syncs to client every tick!
    dataTracker.set(ATTACK_POWER, calculateAttackPower());
}

// ✅ GOOD - only sync when actually changed
private int lastAttackPower = -1;

public void tick() {
    int currentAttackPower = calculateAttackPower();
    if (currentAttackPower != lastAttackPower) {
        dataTracker.set(ATTACK_POWER, currentAttackPower);
        lastAttackPower = currentAttackPower;
    }
}
```

### 5. Large Collection Iteration
```java
// ❌ BAD - iterates entire collection
public boolean hasGuardNamed(String name) {
    for (GuardEntity guard : allGuards) { // 10,000 guards!
        if (guard.getName().equals(name)) {
            return true;
        }
    }
    return false;
}

// ✅ GOOD - use index
private Map<String, GuardEntity> guardsByName = new HashMap<>();

public boolean hasGuardNamed(String name) {
    return guardsByName.containsKey(name); // O(1)
}
```

## Performance Testing Requirements

### Before Optimization
1. **Profile** - Identify actual bottleneck (don't guess!)
2. **Benchmark** - Measure current performance
3. **Document** - Record baseline metrics

### After Optimization
1. **Re-profile** - Verify bottleneck addressed
2. **Re-benchmark** - Measure new performance
3. **Compare** - Calculate improvement percentage
4. **Validate** - Ensure functionality unchanged

### Benchmark Template
```java
/**
 * Performance Benchmark: [Feature Name]
 *
 * Test Environment:
 * - World: Superflat, 10 chunk render distance
 * - Entities: 100 guards, 50 villagers
 * - Server: Dedicated, 8GB RAM
 *
 * Baseline (before optimization):
 * - Average tick time: 15.2ms
 * - Peak tick time: 28.7ms
 * - Memory usage: 2.1GB
 *
 * Optimized (after caching):
 * - Average tick time: 8.3ms (-45%)
 * - Peak tick time: 12.1ms (-58%)
 * - Memory usage: 2.0GB (-5%)
 *
 * Conclusion: Caching reduced tick time by 45%, meeting performance target.
 */
@Test
public void benchmarkGuardTicking() {
    // Benchmark implementation
}
```

## Performance Budget Guidelines

### Tick Budget Allocation
- **Entity ticking**: < 0.5ms per 100 entities
- **Pathfinding**: < 1ms per 10 active pathfinds
- **Networking**: < 0.1ms per player
- **Block entities**: < 0.05ms per block entity

### Memory Budget
- **Entity data**: < 1KB per entity
- **Cached data**: < 10MB total
- **Registry data**: < 5MB total

### Network Budget
- **Entity sync**: < 100 bytes per entity per second
- **GUI data**: < 1KB per screen open
- **Bulk operations**: < 10KB per operation

## Profiling Workflow

### Step 1: Reproduce Performance Issue
```bash
# Setup test environment
/fill ~-50 ~ ~-50 ~50 ~ ~50 minecraft:stone
/summon guard ~ ~ ~ {Count:100}
/spark profiler start
# Wait 60 seconds
/spark profiler stop
/spark profiler open
```

### Step 2: Analyze Profile
- Identify top 5 methods by total time
- Check for unexpected O(n²) patterns
- Look for excessive GC (> 5% of time)
- Find hot paths (> 1ms per tick)

### Step 3: Optimize
- Apply appropriate optimization strategy
- Add performance logging
- Document change

### Step 4: Verify
- Re-run profiler
- Compare before/after metrics
- Ensure improvement meets target (20%+ minimum)

## When to Use This Skill

Use this skill when:
- Implementing performance-critical features (entities, ticking, pathfinding)
- Diagnosing lag or TPS drops
- Optimizing existing implementations
- Reviewing code for performance issues
- Setting performance budgets for features
- Creating benchmarks and performance tests
- Questions about "Why is X slow?"
- Questions about "How can I optimize Y?"
- Before implementing features with > 100 entities
- Before implementing features that tick frequently

## Key Principles

1. **Measure First**: Profile before optimizing (don't guess)
2. **Tick Budget**: Keep all operations under 1-2ms per tick
3. **Cache Aggressively**: Recompute only when data changes
4. **Batch Operations**: Group similar work together
5. **Lazy Evaluation**: Calculate only when needed
6. **Spatial Partitioning**: Use chunk-based data structures
7. **Network Sparingly**: Send only changes, not full state
8. **Test Rigorously**: Benchmark before/after with real workloads
9. **Document Impact**: Record performance metrics for all optimizations
10. **Memory Matters**: Reduce allocations to minimize GC pressure
