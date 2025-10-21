---
name: minecraft-modding
description: General Minecraft 1.21.1 game mechanics and systems knowledge including entities, ticking, world structure, client-server architecture, and data storage. Loader-agnostic knowledge applicable to any mod loader. Use when understanding how Minecraft systems work fundamentally.
allowed-tools: [Read, Grep, Glob, WebFetch, WebSearch]
---

# Minecraft 1.21.1 Core Systems

General knowledge about how Minecraft works internally. This is loader-agnostic and applies to Fabric, Forge, NeoForge, etc.

## Game Loop and Ticking

### Tick System
**Fundamental Concept**: Minecraft runs at 20 ticks per second (TPS)
- **Tick Duration**: 50ms per tick (1000ms / 20 ticks)
- **Tick Budget**: Must complete all tick logic within 50ms
- **Server Tick**: Processes game logic (entities, blocks, redstone)
- **Client Tick**: Updates client state, animations, particles

### What Happens Each Tick
1. **World Tick**: Process all loaded chunks
2. **Entity Tick**: Update all loaded entities
3. **Block Entity Tick**: Update tile entities (furnaces, chests, etc.)
4. **Random Tick**: Random block updates (crop growth, ice melting)
5. **Scheduled Tick**: Queued block updates (redstone, water flow)

### Performance Impact
- ❌ **Never** iterate all entities every tick (O(n) per tick is expensive)
- ✅ **Use** entity selectors or targeted queries
- ❌ **Never** perform heavy calculations in tick methods
- ✅ **Cache** expensive calculations, update periodically
- ❌ **Never** modify collections while iterating
- ✅ **Use** concurrent collections or copy-on-write patterns

## Entity System

### Entity Hierarchy
```
Entity (base class)
├── LivingEntity
│   ├── PlayerEntity
│   ├── MobEntity
│   │   ├── PathAwareEntity
│   │   │   ├── AnimalEntity
│   │   │   ├── HostileEntity
│   │   │   └── WaterCreatureEntity
│   │   └── FlyingEntity
│   └── ArmorStandEntity
├── ProjectileEntity
│   ├── ArrowEntity
│   ├── FireballEntity
│   └── ThrownItemEntity
├── VehicleEntity
│   ├── BoatEntity
│   └── MinecartEntity
└── ItemEntity
```

### Entity Lifecycle
1. **Creation**: Entity spawned in world
2. **Loading**: Entity loaded from disk (NBT/components)
3. **Ticking**: Entity updated every game tick
4. **Tracking**: Client receives entity updates
5. **Unloading**: Entity chunk unloaded, entity serialized
6. **Removal**: Entity marked for removal, cleaned up

### Entity AI (Goal System)
**Mob AI**: Uses Goal-based system
- **Goals**: Individual behaviors (AttackGoal, FleeGoal, FollowGoal)
- **Goal Selector**: Manages multiple goals with priorities
- **Target Selector**: Handles target selection for hostile mobs
- **Priority**: Lower number = higher priority

**Common Goals**:
- `LookAtEntityGoal` - Look at nearby entities
- `WanderAroundGoal` - Random wandering
- `MeleeAttackGoal` - Melee combat
- `RangedAttackGoal` - Ranged combat
- `FleeEntityGoal` - Run from threats
- `FollowTargetGoal` - Follow specific entities

### Pathfinding
**Navigation System**:
- `PathNavigator` - Handles pathfinding for entities
- `PathNodeType` - Defines traversable terrain types
- **A* Algorithm**: Finds optimal path from A to B
- **Cost Calculation**: Different terrain has different costs
- **Path Recalculation**: Happens when target moves or path blocked

**Performance Considerations**:
- Pathfinding is expensive (A* algorithm)
- Limit pathfinding distance
- Cache paths when possible
- Throttle path recalculation frequency

## Data Storage (1.21.1)

### Data Components (New in 1.21)
**Major Change**: Replaced NBT for item and entity data

**Why Components?**:
- Type-safe (no string keys like NBT)
- Better performance
- Codec-based serialization
- Cleaner API

**Component Types**:
- Registered like items/blocks
- Uses `Codec<T>` for serialization
- Attached to items, entities, block entities

**Example Concept**:
```
Instead of:
nbt.putString("guard_type", "archer")

Now:
entity.set(GUARD_TYPE_COMPONENT, GuardType.ARCHER)
```

**Built-in Components**:
- Item components: enchantments, durability, custom name, lore
- Entity components: health, position, rotation, custom data

### NBT (Legacy, Still Used)
**Still Used For**:
- World save data
- Structure templates
- Some mod data storage
- Backward compatibility

**NBT Structure**:
- `CompoundTag` - Key-value map
- `ListTag` - Ordered list
- Primitive types: int, float, string, etc.

## Client-Server Architecture

### Fundamental Separation
**Two Sides**:
- **Logical Server**: Game logic, world state, authoritative
- **Logical Client**: Rendering, input, prediction

**Physical Deployment**:
- **Dedicated Server**: Only runs server side (no client code)
- **Integrated Server**: Client hosts server internally (singleplayer)
- **Client**: Connects to server (multiplayer)

### Thread Safety
**Critical Rules**:
- ❌ **Server Thread**: Only server code runs here
- ❌ **Client Thread**: Only client code runs here
- ❌ **Netty Thread**: Network packets arrive here (NOT game thread!)
- ✅ **Always** schedule world modifications on correct thread
- ✅ **Never** access client classes from server (crashes dedicated server)

### World.isClient
**Check Logical Side**:
```
if (world.isClient) {
    // Client side - rendering, particles, sounds
} else {
    // Server side - game logic, spawning, damage
}
```

**Common Pattern**:
- Client: Play particles, sounds, update UI
- Server: Modify world, spawn entities, change state
- Both: Must sync via network packets

### Data Synchronization
**What Needs Syncing**:
- Entity positions and rotations
- Entity data (health, equipment, custom data)
- Block states and updates
- Container inventories
- Player actions and interactions

**Sync Frequency**:
- Entity position: Every tick (tracked entities only)
- Entity data: When changed (data watchers)
- Block updates: When modified
- Inventories: When opened and modified

## World Structure

### Dimension System
**Three Default Dimensions**:
- Overworld (`minecraft:overworld`)
- Nether (`minecraft:the_nether`)
- End (`minecraft:the_end`)

**Dimension Properties**:
- Height range (y-level min/max)
- Light level behavior
- Biome distribution
- Generator settings

### Chunk System
**Chunk**: 16x384x16 block region (1.21.1 height)
- **Chunk Status**: Different loading stages (empty → full → ticking)
- **Chunk Tickets**: Keep chunks loaded
- **View Distance**: How far players load chunks
- **Simulation Distance**: How far chunks tick

**Chunk Loading Levels**:
1. **Border**: Chunk exists but doesn't tick
2. **Ticking**: Chunk fully active, entities tick
3. **Entity Ticking**: Only entities tick, blocks don't

**Performance Impact**:
- Only load necessary chunks
- Avoid forcing chunk loads
- Be mindful of chunk boundaries
- Use chunk events for chunk-dependent logic

### Block System
**Block vs BlockState**:
- **Block**: The type (e.g., `Blocks.STONE`)
- **BlockState**: Specific variant with properties (e.g., `stone[variant=granite]`)

**Block Properties**:
- Hardness, blast resistance
- Light emission
- Collision shape
- Sound type

**Block Entities** (Tile Entities):
- Blocks with extra data (chests, furnaces, signs)
- Tick every game tick (expensive!)
- Store inventory, custom data

## Resource System

### Assets vs Data
**Assets** (`assets/[modid]/`): Client-side visual resources
- `textures/` - PNG images for blocks, items, entities
- `models/` - JSON model definitions
- `lang/` - Translations (en_us.json)
- `sounds/` - Sound files and definitions
- `blockstates/` - Block state → model mappings

**Data** (`data/[modid]/`): Server-side game data
- `recipes/` - Crafting recipes (JSON)
- `loot_tables/` - Loot definitions
- `tags/` - Item/block tags for grouping
- `advancements/` - Achievement definitions
- `structures/` - Structure templates

### Resource Loading Order
1. **Built-in**: Minecraft's default resources
2. **Mod Resources**: Each mod's resources
3. **Data Packs**: User-added data packs
4. **Resource Packs**: User-added resource packs (override textures/models)

## Performance Optimization

### Entity Performance
**Best Practices**:
- Limit entity count (cull inactive entities)
- Reduce entity tracking distance
- Optimize AI goal complexity
- Cache pathfinding results
- Throttle expensive operations (spread across ticks)

### Tick Budget Management
**50ms Per Tick Budget**:
- Entity ticking: ~60-70% of tick time
- Block updates: ~10-15%
- Chunk loading: ~5-10%
- Networking: ~5-10%
- Remaining: Other systems

**Optimization Strategies**:
- **Amortize**: Spread heavy work across multiple ticks
- **Cache**: Store expensive calculation results
- **Batch**: Group similar operations
- **Throttle**: Limit frequency of expensive operations
- **Lazy**: Only calculate when needed

### Chunk Loading Performance
**Minimize Chunk Loading**:
- Avoid unnecessary chunk tickets
- Don't force-load chunks unnecessarily
- Use chunk events to detect loading
- Cache chunk-dependent calculations

### Network Performance
**Reduce Packet Size**:
- Send only changed data
- Use compact data formats
- Batch multiple updates
- Throttle update frequency

**Reduce Packet Frequency**:
- Don't send every tick (too much!)
- Send on change only
- Batch updates when possible
- Use delta compression

## Common Pitfalls

### Thread Safety Issues
- ❌ Modifying world from wrong thread
- ❌ Accessing client classes on server
- ❌ Race conditions in async code
- ✅ Always schedule world modifications properly
- ✅ Use thread-safe collections

### Performance Issues
- ❌ Iterating all entities every tick
- ❌ Heavy calculations in tick methods
- ❌ Excessive packet sending
- ❌ Keeping chunks loaded unnecessarily
- ✅ Use targeted queries
- ✅ Cache expensive results
- ✅ Batch operations

### Data Synchronization
- ❌ Assuming client has server data
- ❌ Assuming server has client input
- ❌ Not syncing custom entity data
- ✅ Always sync state changes
- ✅ Use proper packets/data watchers

## Version-Specific Changes (1.21.1)

### Data Components
**Major Change**: Replaced NBT for items/entities
- Type-safe component system
- Codec-based serialization
- Better performance than NBT
- See vanilla `DataComponentTypes` for examples

### Height Changes
**World Height**: -64 to 320 (384 total)
- Deepslate generation below y=0
- Extended build height
- Affects chunk size

### Recipe System
**Updated Recipe Format**:
- Component-aware crafting
- New serialization format
- Better recipe book integration

### Entity Improvements
**AI Enhancements**:
- Improved pathfinding algorithm
- Better goal prioritization
- Enhanced navigation
- Component-based entity data

## Resources for Learning

### Official Documentation
- **Minecraft Wiki**: https://minecraft.wiki/ - Vanilla mechanics
- **Yarn Mappings**: Decompiled Minecraft code (use `/gradlew genSources`)
- **Minecraft Source**: Best reference for how systems actually work

### Understanding Systems
- **Read Vanilla Code**: Best way to learn patterns
- **Test in-game**: Observe behavior directly
- **Use Debug Tools**: F3 screen, debug mode
- **Profile Performance**: Understand performance impact

### Community Resources
- **Minecraft Wiki**: Comprehensive vanilla mechanics
- **YouTube Tutorials**: Visual learning for concepts
- **Reddit**: /r/Minecraft, /r/technicalminecraft
- **Stack Overflow**: Specific technical questions

## When to Use This Skill

Use this skill when:
- Understanding how Minecraft systems work fundamentally
- Designing game mechanics that interact with vanilla
- Debugging issues related to game logic (not loader-specific)
- Performance optimization and tick budgeting
- Client-server architecture questions
- Questions about "How does Minecraft handle X internally?"
- Questions about "Why does Y work this way?"
- Understanding vanilla mechanics before modding them
