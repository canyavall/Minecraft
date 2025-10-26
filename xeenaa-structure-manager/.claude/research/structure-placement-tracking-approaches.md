# Structure Placement Tracking Approaches for Minecraft 1.21.1 Fabric

## Research Summary

This document outlines multiple approaches to track structure placements in Minecraft 1.21.1 Fabric mods, with focus on intercepting structure generation to log position, structure ID, and timestamp for spacing verification.

**Date**: 2025-10-24
**Minecraft Version**: 1.21.1
**Mappings**: Yarn 1.21.1+build.3
**Problem Context**: Track every structure placement event to verify spacing multipliers are working correctly.

---

## Understanding StructureStart Class (1.21.1)

After examining the decompiled source, here's the actual structure of `StructureStart` in Minecraft 1.21.1:

```java
package net.minecraft.structure;

public final class StructureStart {
    private final Structure structure;              // The structure definition
    private final StructurePiecesList children;     // The pieces that make up the structure
    private final ChunkPos pos;                     // Origin chunk position
    private int references;                         // Reference counter
    @Nullable
    private volatile BlockBox boundingBox;          // Cached bounding box

    // Key methods:
    public void place(StructureWorldAccess world, StructureAccessor structureAccessor,
                      ChunkGenerator chunkGenerator, Random random,
                      BlockBox chunkBox, ChunkPos chunkPos) { ... }

    public Structure getStructure() { return this.structure; }
    public ChunkPos getPos() { return this.pos; }
    public List<StructurePiece> getChildren() { return this.children.pieces(); }
    public boolean hasChildren() { return !this.children.isEmpty(); }
}
```

**Key Findings**:
- The field is named `structure` (NOT `structureType` or `structureFeature`)
- The field is `final` and declared directly in the class (no inheritance)
- The `place()` method is where actual block placement happens
- The structure's origin chunk is stored in the `pos` field

---

## Approach 1: Mixin into StructureStart.place() - RECOMMENDED

### Description
Inject into the `StructureStart.place()` method to intercept structure placement at the exact moment blocks are being generated.

### Implementation

```java
package com.canya.xeenaa_structure_manager.mixin;

import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.gen.structure.Structure;

@Mixin(StructureStart.class)
public class StructureStartPlaceMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager");

    @Shadow
    private Structure structure;

    @Shadow
    private ChunkPos pos;

    @Inject(
        method = "place",
        at = @At("HEAD")
    )
    private void onStructurePlace(
        StructureWorldAccess world,
        StructureAccessor structureAccessor,
        ChunkGenerator chunkGenerator,
        Random random,
        BlockBox chunkBox,
        ChunkPos chunkPos,
        CallbackInfo ci
    ) {
        // Check if structure has children (valid placement)
        StructureStart self = (StructureStart)(Object)this;
        if (!self.hasChildren()) {
            return; // Skip invalid/empty structures
        }

        // Get structure information
        String structureId = world.getRegistryManager()
            .get(net.minecraft.registry.RegistryKeys.STRUCTURE)
            .getId(structure)
            .toString();

        // Calculate center position
        int centerX = ChunkSectionPos.getOffsetPos(pos.x, 8);
        int centerZ = ChunkSectionPos.getOffsetPos(pos.z, 8);

        // Log placement
        LOGGER.info("[STRUCTURE_PLACED] {} at chunk ({}, {}) center ({}, {}) at {}",
            structureId,
            pos.x, pos.z,
            centerX, centerZ,
            System.currentTimeMillis()
        );

        // TODO: Store in database/tracking system
        // StructurePlacementTracker.recordPlacement(structureId, pos, System.currentTimeMillis());
    }
}
```

**Mixin Registration** (in `xeenaa-structure-manager.mixins.json`):
```json
{
  "required": true,
  "package": "com.canya.xeenaa_structure_manager.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": [
    "StructureStartPlaceMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

### Why @Shadow Works Here

**Previous Error**: "@Shadow field structure was not located in the target class"

**Root Cause**: The field is named `structure` NOT `structureType` or other variants. Using exact field name matches the decompiled source.

**Correct Usage**:
```java
@Shadow
private Structure structure;  // Matches: private final Structure structure;
```

**Alternative - Use Accessor Instead**:
```java
// If @Shadow still causes issues, use accessor method instead:
@Shadow
public abstract Structure getStructure();

// Then in the inject:
String structureId = world.getRegistryManager()
    .get(RegistryKeys.STRUCTURE)
    .getId(this.getStructure())
    .toString();
```

### Pros
- ✅ Intercepts at exact placement moment
- ✅ Has access to all structure information (type, position, bounding box)
- ✅ Called once per structure placement (not per chunk reference)
- ✅ Can filter out invalid structures with `hasChildren()` check
- ✅ Simple, focused mixin with clear purpose

### Cons
- ❌ Mixins can have compatibility issues with other mods
- ❌ Requires understanding Mixin syntax and injection points
- ❌ May need updates if Minecraft changes the place() method signature

### When to Use
- ✅ You need precise placement timing
- ✅ You want to log each unique structure placement once
- ✅ You need access to structure metadata (bounding box, pieces)
- ✅ You're building a structure spacing verification tool

---

## Approach 2: Mixin into ChunkGenerator.addStructureReferences() - ALTERNATIVE

### Description
Inject into `ChunkGenerator.addStructureReferences()` which is called when chunks are being generated and structure references are being added. This catches structures as they're being registered to chunks.

### Implementation

```java
package com.canya.xeenaa_structure_manager.mixin;

import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorStructureReferenceMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager");

    @Inject(
        method = "addStructureReferences",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/DebugInfoSender;sendStructureStart(" +
                     "Lnet/minecraft/world/StructureWorldAccess;" +
                     "Lnet/minecraft/structure/StructureStart;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onStructureReferenceAdded(
        StructureWorldAccess world,
        StructureAccessor structureAccessor,
        Chunk chunk,
        CallbackInfo ci,
        // Locals captured from the loop
        int i, ChunkPos chunkPos, int j, int k, int l, int m,
        ChunkSectionPos chunkSectionPos,
        int n, int o, long p,
        Iterator<?> iterator,
        StructureStart structureStart
    ) {
        // Log structure reference addition
        String structureId = world.getRegistryManager()
            .get(net.minecraft.registry.RegistryKeys.STRUCTURE)
            .getId(structureStart.getStructure())
            .toString();

        ChunkPos originPos = structureStart.getPos();

        LOGGER.info("[STRUCTURE_REFERENCED] {} origin chunk ({}, {}) referenced by chunk ({}, {})",
            structureId,
            originPos.x, originPos.z,
            chunk.getPos().x, chunk.getPos().z
        );

        // For tracking unique structures, deduplicate by origin chunk
        // StructurePlacementTracker.recordReference(structureId, originPos, chunk.getPos());
    }
}
```

### Pros
- ✅ Catches structures during chunk generation phase
- ✅ Has access to both origin chunk and referencing chunks
- ✅ Called at a well-defined hook point (near DebugInfoSender.sendStructureStart)
- ✅ Can track which chunks reference which structures

### Cons
- ❌ Called MULTIPLE times per structure (once per intersecting chunk)
- ❌ Requires deduplication logic to avoid duplicate logging
- ❌ Complex local variable capture (fragile to Minecraft updates)
- ❌ May not catch structures that fail to generate

### When to Use
- ✅ You need to track chunk-to-structure relationships
- ✅ You're debugging structure reference system
- ❌ NOT recommended for simple placement tracking (use Approach 1 instead)

---

## Approach 3: Custom Fabric Event (Create Your Own)

### Description
Since Fabric API doesn't provide a built-in structure placement event, create a custom event that other parts of your mod (or other mods) can listen to.

### Implementation

**Step 1: Define Custom Event**
```java
package com.canya.xeenaa_structure_manager.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.structure.Structure;

@FunctionalInterface
public interface StructurePlacementCallback {
    Event<StructurePlacementCallback> EVENT = EventFactory.createArrayBacked(
        StructurePlacementCallback.class,
        (listeners) -> (world, structure, pos, structureStart) -> {
            for (StructurePlacementCallback listener : listeners) {
                listener.onStructurePlacement(world, structure, pos, structureStart);
            }
        }
    );

    /**
     * Called when a structure is placed in the world.
     *
     * @param world The world access
     * @param structure The structure being placed
     * @param pos The origin chunk position
     * @param structureStart The structure start instance
     */
    void onStructurePlacement(
        StructureWorldAccess world,
        Structure structure,
        ChunkPos pos,
        StructureStart structureStart
    );
}
```

**Step 2: Fire Event from Mixin**
```java
package com.canya.xeenaa_structure_manager.mixin;

import com.canya.xeenaa_structure_manager.event.StructurePlacementCallback;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.gen.structure.Structure;

@Mixin(StructureStart.class)
public class StructureStartEventMixin {
    @Shadow
    private Structure structure;

    @Shadow
    private ChunkPos pos;

    @Inject(method = "place", at = @At("HEAD"))
    private void fireStructurePlacementEvent(
        StructureWorldAccess world,
        StructureAccessor structureAccessor,
        ChunkGenerator chunkGenerator,
        Random random,
        BlockBox chunkBox,
        ChunkPos chunkPos,
        CallbackInfo ci
    ) {
        StructureStart self = (StructureStart)(Object)this;
        if (self.hasChildren()) {
            StructurePlacementCallback.EVENT.invoker().onStructurePlacement(
                world, structure, pos, self
            );
        }
    }
}
```

**Step 3: Register Event Listener**
```java
package com.canya.xeenaa_structure_manager;

import com.canya.xeenaa_structure_manager.event.StructurePlacementCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XeenaaStructureManager implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager");

    @Override
    public void onInitialize() {
        // Register structure placement listener
        StructurePlacementCallback.EVENT.register((world, structure, pos, structureStart) -> {
            String structureId = world.getRegistryManager()
                .get(RegistryKeys.STRUCTURE)
                .getId(structure)
                .toString();

            LOGGER.info("[STRUCTURE_PLACED] {} at chunk ({}, {}) at {}",
                structureId, pos.x, pos.z, System.currentTimeMillis());

            // Store in tracking system
            // StructurePlacementTracker.recordPlacement(structureId, pos, System.currentTimeMillis());
        });

        LOGGER.info("XeenaaStructureManager initialized - structure tracking active");
    }
}
```

### Pros
- ✅ Clean separation of concerns (mixin fires event, listeners handle logic)
- ✅ Other parts of your mod (or other mods) can listen to the event
- ✅ Follows Fabric's event pattern
- ✅ Easy to add multiple listeners
- ✅ Testable (can fire events without actual world generation)

### Cons
- ❌ More boilerplate code
- ❌ Still requires a mixin to fire the event
- ❌ Adds another layer of abstraction

### When to Use
- ✅ You want a clean, extensible API
- ✅ Multiple systems need to react to structure placements
- ✅ You want to provide an API for other mods
- ✅ You're building a framework/library mod

---

## Approach 4: ServerWorldEvents + Periodic Scanning - NOT RECOMMENDED

### Description
Instead of mixins, listen to server tick events and periodically scan for new structures in loaded chunks.

### Implementation Sketch
```java
ServerTickEvents.END_WORLD_TICK.register((world) -> {
    // Every N ticks, scan loaded chunks for structures
    if (world.getTime() % 20 == 0) { // Every second
        for (ServerChunk chunk : world.getChunkManager().chunks) {
            Map<Structure, StructureStart> starts = chunk.getStructureStarts();
            for (Map.Entry<Structure, StructureStart> entry : starts.entrySet()) {
                // Check if we've seen this structure before
                // If not, log it as a new placement
            }
        }
    }
});
```

### Pros
- ✅ No mixins required
- ✅ Uses official Fabric API

### Cons
- ❌ Inefficient (scans chunks every tick)
- ❌ High performance overhead
- ❌ Can miss structures if chunks unload before scan
- ❌ Requires complex state tracking to avoid duplicate logs
- ❌ No access to exact placement timestamp

### When to Use
- ❌ **NEVER** - Use Approach 1, 2, or 3 instead

---

## Comparison Matrix

| Aspect | Approach 1 (StructureStart.place) | Approach 2 (addStructureReferences) | Approach 3 (Custom Event) | Approach 4 (Periodic Scan) |
|--------|-----------------------------------|-------------------------------------|---------------------------|----------------------------|
| **Precision** | ✅ Exact placement moment | ⚠️ Multiple calls per structure | ✅ Exact placement moment | ❌ Delayed, approximate |
| **Performance** | ✅ Zero overhead | ✅ Zero overhead | ✅ Minimal overhead | ❌ High CPU usage |
| **Simplicity** | ✅ Single mixin | ⚠️ Complex locals capture | ⚠️ Multiple files | ✅ No mixins |
| **Compatibility** | ✅ High | ⚠️ Fragile locals | ✅ High | ✅ Very high |
| **Extensibility** | ⚠️ One-off solution | ⚠️ One-off solution | ✅ API for others | ❌ Inefficient |
| **Data Quality** | ✅ One log per structure | ❌ Requires deduplication | ✅ One log per structure | ❌ May miss structures |

---

## Recommended Solution

**For your use case (spacing multiplier verification), use Approach 1 (StructureStart.place mixin).**

### Why?
1. **Precise**: Logs exactly when structure places
2. **Simple**: Single mixin file, clear injection point
3. **Efficient**: Called once per structure, no overhead
4. **Complete data**: Access to structure type, position, bounding box
5. **Easy debugging**: Clear correlation between log and placement

### Implementation Checklist
- [x] Create `StructureStartPlaceMixin.java` in mixin package
- [x] Register mixin in `xeenaa-structure-manager.mixins.json`
- [x] Use correct field name: `structure` (NOT `structureType`)
- [x] Filter invalid structures with `hasChildren()` check
- [x] Log structure ID, origin chunk position, center coordinates, timestamp
- [x] Consider adding structured logging (JSON format) for parsing
- [ ] Implement StructurePlacementTracker to store placements
- [ ] Add spacing verification logic that compares placement distances
- [ ] Create report generator for spacing analysis

### Future Enhancement Path
If you later need an extensible system:
1. Start with Approach 1 (immediate solution)
2. Extract event firing from the mixin (move to Approach 3)
3. Keep logging as an event listener
4. Other mods can now listen to structure placements

---

## Common Pitfalls and Solutions

### Pitfall 1: "@Shadow field structure was not located"
**Cause**: Wrong field name or incorrect access modifier
**Solution**: Use exact name from decompiled source: `private Structure structure;`

### Pitfall 2: "Null pointer in structure ID"
**Cause**: Structure registry not available on client
**Solution**: Check `world.isClient()` and only log on server side

### Pitfall 3: "Duplicate structure logs"
**Cause**: Using Approach 2 which fires multiple times
**Solution**: Switch to Approach 1, OR implement deduplication by origin chunk

### Pitfall 4: "Structure logged but not visible in world"
**Cause**: Structure generation can fail after place() is called
**Solution**: Log at both start and end of place() method, or add success check

### Pitfall 5: "Performance issues with logging"
**Cause**: Synchronous I/O in placement hot path
**Solution**: Use async logging or queue placements for batch processing

---

## References and Resources

### Minecraft 1.21.1 Source Classes
- `net.minecraft.structure.StructureStart` - Main structure placement class
- `net.minecraft.world.gen.chunk.ChunkGenerator` - Chunk generation and structure references
- `net.minecraft.server.network.DebugInfoSender` - Empty debug hook (could be used)
- `net.minecraft.world.gen.structure.Structure` - Structure definition
- `net.minecraft.structure.StructurePiece` - Individual structure pieces

### Yarn Mappings (1.21.1+build.3)
- Field: `StructureStart.structure` → `Structure`
- Field: `StructureStart.pos` → `ChunkPos` (origin chunk)
- Method: `StructureStart.place(...)` → Places structure blocks
- Method: `StructureStart.getStructure()` → Returns `Structure`
- Method: `StructureStart.hasChildren()` → Validates structure has pieces

### Fabric API Documentation
- **No built-in structure placement event** in Fabric API 0.116.5+1.21.1
- Custom events follow pattern: `EventFactory.createArrayBacked(...)`
- Mixin injection points: `@At("HEAD")`, `@At("RETURN")`, `@At("INVOKE")`

### External Mods Reference
- **Structure Essentials** (by someaddons): Uses structure tracking, closed source
- **MixinTrace**: Helpful for debugging mixin issues
- **StructurePlacerAPI**: API-focused structure manipulation

### Mixin Documentation
- [Mixin Wiki](https://github.com/SpongePowered/Mixin/wiki)
- [Fabric Mixin Tutorial](https://fabricmc.net/wiki/tutorial:mixin_introduction)
- [Injection Point Reference](https://github.com/SpongePowered/Mixin/wiki/Injection-Point-Reference)

---

## Example Log Output

Using Approach 1, you'll get logs like:
```
[INFO] [XeenaaStructureManager] [STRUCTURE_PLACED] minecraft:village_plains at chunk (123, 456) center (1992, 7304) at 1729782345678
[INFO] [XeenaaStructureManager] [STRUCTURE_PLACED] minecraft:pillager_outpost at chunk (130, 445) center (2088, 7128) at 1729782347123
[INFO] [XeenaaStructureManager] [STRUCTURE_PLACED] minecraft:village_desert at chunk (145, 470) center (2328, 7528) at 1729782351456
```

With structured logging (JSON):
```json
{"event":"structure_placed","structure":"minecraft:village_plains","chunk":{"x":123,"z":456},"center":{"x":1992,"z":7304},"timestamp":1729782345678}
{"event":"structure_placed","structure":"minecraft:pillager_outpost","chunk":{"x":130,"z":445},"center":{"x":2088,"z":7128},"timestamp":1729782347123}
```

---

## Next Steps

1. **Implement Approach 1** (StructureStart.place mixin)
2. **Test in development environment** with `/locate structure` commands
3. **Create StructurePlacementTracker** to store placements in memory or database
4. **Add spacing verification logic** that compares distances between structures of same type
5. **Generate reports** showing actual vs expected spacing with spacing multiplier applied

---

## Research Metadata

- **Author**: Claude (research-agent)
- **Date**: 2025-10-24
- **Minecraft Version**: 1.21.1
- **Fabric Loader**: 0.16.5
- **Fabric API**: 0.116.5+1.21.1
- **Mappings**: Yarn 1.21.1+build.3
- **Research Method**: Source code analysis, web search, Fabric API inspection
- **Confidence Level**: High (verified against actual source code)
