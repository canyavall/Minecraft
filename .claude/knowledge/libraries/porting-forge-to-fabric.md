# Porting Mods from Forge/NeoForge to Fabric

**Last Updated**: 2025-10-27
**Minecraft Version**: 1.20.1 → 1.21.1 (general patterns apply to other versions)
**Tags**: fabric, forge, neoforge, porting, geckolib, entities, animation, registry, migration

## Overview

Comprehensive guide for porting mods from Forge/NeoForge to Fabric, covering entity systems, registration patterns, animation libraries, and common pitfalls.

---

## Part 1: Key Architectural Differences

### Mod Loading

**Forge/NeoForge**:
```java
@Mod("modid")
public class MyMod {
    public MyMod() {
        // Initialization in constructor
        ModLoadingContext.get().registerConfig(...);
    }
}
```

**Fabric**:
```java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Initialization in method
    }
}
```

**Fabric Client**:
```java
@Environment(EnvType.CLIENT)
public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Client-only initialization
    }
}
```

### Entry Points

**Forge**: Uses `@Mod` annotation and event bus
**Fabric**: Uses `fabric.mod.json` entry points

```json
{
  "entrypoints": {
    "main": ["com.example.MyMod"],
    "client": ["com.example.MyModClient"]
  }
}
```

---

## Part 2: Entity Registration

### Basic Entity Registration

**Forge/NeoForge**:
```java
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<CustomEntity>> CUSTOM =
        ENTITIES.register("custom", () ->
            EntityType.Builder.of(CustomEntity::new, MobCategory.CREATURE)
                .sized(1.0f, 2.0f)
                .build("custom")
        );
}
```

**Fabric**:
```java
public class ModEntities {
    public static final EntityType<CustomEntity> CUSTOM = register(
        "custom",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CustomEntity::new)
            .dimensions(EntityDimensions.fixed(1.0f, 2.0f))
            .build()
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("modid", name),
            type
        );
    }
}
```

### Attribute Registration

**Forge/NeoForge**:
```java
@SubscribeEvent
public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.CUSTOM.get(), CustomEntity.createAttributes().build());
}
```

**Fabric**:
```java
public static void initialize() {
    FabricDefaultAttributeRegistry.register(
        ModEntities.CUSTOM,
        CustomEntity.createAttributes()
    );
}
```

### Client Renderer Registration

**Forge/NeoForge**:
```java
@SubscribeEvent
public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(ModEntities.CUSTOM.get(), CustomRenderer::new);
}
```

**Fabric**:
```java
@Environment(EnvType.CLIENT)
public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CUSTOM, CustomRenderer::new);
    }
}
```

---

## Part 3: Animation Systems

### Citadel → GeckoLib Migration

**Challenge**: Forge mods often use Citadel (Forge-only) for animations
**Solution**: Port to GeckoLib (cross-loader compatible)

#### Citadel (Forge)

```java
// Uses .tbl (Tabula) models
public class CustomEntity extends TamableAnimal implements IAnimatedEntity {
    private Animation animation;
    private final AnimationController controller;
}
```

#### GeckoLib (Fabric)

```java
public class CustomEntity extends TamableEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private PlayState predicate(AnimationState<CustomEntity> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("walk"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        }
        return PlayState.CONTINUE;
    }
}
```

#### Model Conversion Workflow

1. **Extract .tbl model** from Forge mod JAR
2. **Open in Blockbench**
3. **Export as GeckoLib**:
   - File → Export → GeckoLib Animated Model
   - Generates: `entity.geo.json` (model), `entity.animation.json` (animations)
4. **Place in resources**:
   ```
   assets/modid/geo/entity.geo.json
   assets/modid/animations/entity.animation.json
   ```
5. **Create GeoModel class**:
```java
public class CustomEntityModel extends GeoModel<CustomEntity> {
    @Override
    public Identifier getModelResource(CustomEntity entity) {
        return Identifier.of("modid", "geo/custom_entity.geo.json");
    }

    @Override
    public Identifier getTextureResource(CustomEntity entity) {
        return Identifier.of("modid", "textures/entity/custom_entity.png");
    }

    @Override
    public Identifier getAnimationResource(CustomEntity entity) {
        return Identifier.of("modid", "animations/custom_entity.animation.json");
    }
}
```

6. **Create GeoRenderer**:
```java
public class CustomEntityRenderer extends GeoEntityRenderer<CustomEntity> {
    public CustomEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CustomEntityModel());
    }
}
```

#### Dependencies

**Add to build.gradle**:
```gradle
repositories {
    maven { url 'https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/' }
}

dependencies {
    modImplementation 'software.bernie.geckolib:geckolib-fabric-1.21:4.5.8'
}
```

**Add to fabric.mod.json**:
```json
{
  "depends": {
    "geckolib": ">=4.5.0"
  }
}
```

---

## Part 4: AI Goals & Behavior

### Goal Registration

**Forge/NeoForge**:
```java
@Override
protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
}
```

**Fabric**:
```java
@Override
protected void initGoals() {
    this.goalSelector.add(0, new SwimGoal(this));
    this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false));
}
```

**Key Differences**:
- Forge: `addGoal()` → Fabric: `add()`
- Forge: `FloatGoal` → Fabric: `SwimGoal`
- Forge: `registerGoals()` → Fabric: `initGoals()`

### Brain System (Villagers, Piglins, etc.)

**Mostly identical** - Vanilla brain system works the same on both loaders

**Forge/NeoForge & Fabric**:
```java
@Override
protected Brain<?> makeBrain(Dynamic<?> dynamic) {
    return BrainProvider.makeBrain(this, dynamic);
}

@Override
protected void customServerAiStep() {
    super.customServerAiStep();
    this.brain.tick((ServerLevel)this.level, this);
}
```

---

## Part 5: Items & Blocks

### Item Registration

**Forge/NeoForge**:
```java
public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

public static final RegistryObject<Item> CUSTOM_ITEM =
    ITEMS.register("custom_item", () -> new Item(new Item.Properties()));
```

**Fabric**:
```java
public static final Item CUSTOM_ITEM = register(
    "custom_item",
    new Item(new Item.Settings())
);

private static Item register(String name, Item item) {
    return Registry.register(Registries.ITEM, Identifier.of("modid", name), item);
}
```

### Block Registration

**Forge/NeoForge**:
```java
public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

public static final RegistryObject<Block> CUSTOM_BLOCK =
    BLOCKS.register("custom_block", () ->
        new Block(BlockBehaviour.Properties.of().strength(3.0f))
    );
```

**Fabric**:
```java
public static final Block CUSTOM_BLOCK = register(
    "custom_block",
    new Block(AbstractBlock.Settings.create().strength(3.0f))
);

private static Block register(String name, Block block) {
    return Registry.register(Registries.BLOCK, Identifier.of("modid", name), block);
}
```

---

## Part 6: Networking

### Packet System

**Forge/NeoForge**:
```java
SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
    new ResourceLocation(MODID, "main"),
    () -> PROTOCOL_VERSION,
    PROTOCOL_VERSION::equals,
    PROTOCOL_VERSION::equals
);

CHANNEL.registerMessage(0, CustomPacket.class,
    CustomPacket::encode,
    CustomPacket::decode,
    CustomPacket::handle
);
```

**Fabric**:
```java
public class ModNetworking {
    public static final Identifier CUSTOM_PACKET = Identifier.of("modid", "custom");

    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(CUSTOM_PACKET, (server, player, handler, buf, responseSender) -> {
            // Read packet data
            int value = buf.readInt();

            // Execute on server thread
            server.execute(() -> {
                // Handle packet
            });
        });
    }
}
```

**Sending Packets**:

**Forge**: `CHANNEL.sendToServer(new CustomPacket())`
**Fabric**: `ClientPlayNetworking.send(ModNetworking.CUSTOM_PACKET, buf)`

---

## Part 7: Configuration

### Config Files

**Forge**:
```java
public class ModConfig {
    public static ForgeConfigSpec.IntValue spawnWeight;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        spawnWeight = builder.defineInRange("spawnWeight", 10, 1, 100);
        SPEC = builder.build();
    }
}
```

**Fabric** (using Cloth Config):
```java
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int spawnWeight = 10;
}
```

**Fabric** (using AutoConfig):
```gradle
dependencies {
    modImplementation "me.shedaniel.cloth:cloth-config-fabric:11.1.106"
}
```

---

## Part 8: Common Pitfalls

### 1. Class Names Differ

| Forge/NeoForge | Fabric |
|----------------|--------|
| `FloatGoal` | `SwimGoal` |
| `MobCategory` | `SpawnGroup` |
| `ResourceLocation` | `Identifier` |
| `addGoal()` | `add()` |
| `registerGoals()` | `initGoals()` |

### 2. Event System

**Forge**: Uses event bus (`@SubscribeEvent`)
**Fabric**: Uses callbacks

**Forge**:
```java
@SubscribeEvent
public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    // Handle
}
```

**Fabric**:
```java
ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
    // Handle
});
```

### 3. Mixins

**Forge**: Optional (has access transformers, event bus)
**Fabric**: Primary modification method

**Learning curve**: Fabric requires mixin knowledge

### 4. Registry Timing

**Forge**: Deferred registration (lazy)
**Fabric**: Immediate registration

**Impact**: Fabric requires careful ordering of registration

---

## Part 9: What Can Be Kept Unchanged

### Assets (Usually 100% Compatible)

- ✅ Textures (.png files)
- ✅ Sounds (.ogg files)
- ✅ Models (JSON models - may need path updates)
- ✅ Loot tables (JSON - update paths)
- ✅ Recipes (JSON - update paths)
- ✅ Lang files (JSON - usually compatible)

### Code (Often Reusable)

- ✅ Entity logic (AI, behavior, attributes)
- ✅ Item behavior (mostly vanilla APIs)
- ✅ Block behavior (mostly vanilla APIs)
- ✅ Utility classes
- ✅ Data structures
- ✅ Algorithm implementations

### Must Change

- ❌ Registration code (completely different)
- ❌ Mod initialization (different entry point)
- ❌ Event handling (different system)
- ❌ Networking (different packet system)
- ❌ Config system (different libraries)
- ❌ Animation system (if using Forge-specific libraries)

---

## Part 10: Recommended Porting Strategy

### Phase 1: Infrastructure (Week 1)

1. Create Fabric mod structure (`fabric.mod.json`, entry points)
2. Set up GeckoLib dependency (if needed)
3. Create registration framework (entities, items, blocks)
4. Set up client initialization

### Phase 2: Core Entities (Week 2-4)

1. Port 3-5 simple entities first
2. Validate complete workflow
3. Document patterns discovered
4. Refine process

### Phase 3: Incremental Porting (Ongoing)

1. Port entities in logical groups
2. Test each group thoroughly
3. Fix bugs as discovered
4. Update documentation

### Phase 4: Items & Systems (Week N)

1. Port all items
2. Port recipes and loot tables
3. Implement special mechanics
4. Port configuration

### Phase 5: Polish (Final Week)

1. Performance testing
2. Compatibility testing
3. Bug fixes
4. Documentation

---

## Part 11: Testing Checklist

### Entity Testing

- [ ] Entity spawns correctly
- [ ] Entity renders with correct model/texture
- [ ] Animations play correctly
- [ ] AI goals function as expected
- [ ] Combat/interaction works
- [ ] Entity persists across saves
- [ ] No console errors

### Item Testing

- [ ] Item appears in creative menu
- [ ] Item has correct texture
- [ ] Item functionality works
- [ ] Recipes craft correctly
- [ ] Loot tables drop correctly

### Performance Testing

- [ ] No lag spikes from entities
- [ ] Memory usage reasonable
- [ ] Chunk loading smooth
- [ ] Rendering performant

### Compatibility Testing

- [ ] Works with Fabric API
- [ ] Compatible with common mods (Sodium, Lithium)
- [ ] No conflicts with other entity mods
- [ ] Multiplayer-safe

---

## Part 12: Resources

### Documentation

- [Fabric Wiki](https://fabricmc.net/wiki/) - Official Fabric documentation
- [GeckoLib Wiki](https://github.com/bernie-g/geckolib/wiki) - GeckoLib animation docs
- [Yarn Mappings](https://maven.fabricmc.net/docs/) - Fabric mappings reference

### Tools

- [Blockbench](https://www.blockbench.net/) - Model editor with GeckoLib support
- [Fabric Loom](https://github.com/FabricMC/fabric-loom) - Gradle plugin for Fabric
- [Mixin Gradle Plugin](https://github.com/SpongePowered/Mixin) - Mixin support

### Example Ports

- BluSpring's Alex's Mobs Fabric Port (1.19.2) - Architecture reference
- Other successful Forge→Fabric ports on GitHub

---

## Part 13: Common Issues & Solutions

### Issue: Missing Textures

**Cause**: Path differences between Forge and Fabric
**Solution**: Verify `assets/modid/` structure, check identifiers

### Issue: Entity Not Rendering

**Cause**: Renderer not registered on client
**Solution**: Ensure `EntityRendererRegistry.register()` in client initializer

### Issue: Animations Not Playing

**Cause**: GeckoLib not set up correctly
**Solution**: Verify `GeoEntity` interface, check animation controller

### Issue: Crashes on Startup

**Cause**: Registration order, missing dependencies
**Solution**: Check `fabric.mod.json` dependencies, verify registration order

### Issue: Multiplayer Desync

**Cause**: Networking issues, client/server separation
**Solution**: Use `@Environment(EnvType.CLIENT)`, proper packet handling

---

## Related Knowledge

- `.claude/knowledge/fabric-api/registry.md` - Fabric registration patterns
- `.claude/knowledge/fabric-api/networking.md` - Fabric packet system
- `.claude/knowledge/fabric-api/mixins.md` - Mixin usage guide
- `.claude/knowledge/minecraft/entities.md` - Entity system overview
- `.claude/knowledge/graphics/animations.md` - Animation systems comparison
