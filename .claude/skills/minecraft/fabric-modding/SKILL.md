---
name: fabric-modding
description: Expert knowledge of Fabric API for Minecraft 1.21.1 including Fabric events, mixins, networking, registries, and mod lifecycle. Use when implementing Fabric-specific features, working with Fabric API, or answering Fabric loader questions.
allowed-tools: [Read, Grep, Glob, WebFetch, WebSearch]
---

# Fabric API Modding Skill

Expert knowledge for developing Minecraft mods using Fabric API (Fabric Loader + Fabric API) for Minecraft 1.21.1.

## Fabric Mod Lifecycle

### Mod Initializer
**Purpose**: Server-side and common initialization

```java
public class MyMod implements ModInitializer {
    public static final String MOD_ID = "mymod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing " + MOD_ID);

        // Register items, blocks, entities
        ModRegistry.initialize();

        // Register server-side events
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);

        // Register networking handlers
        ModNetworking.registerReceivers();
    }

    private void onServerStart(MinecraftServer server) {
        LOGGER.info("Server started!");
    }
}
```

**fabric.mod.json entry**:
```json
{
  "entrypoints": {
    "main": ["com.example.mymod.MyMod"]
  }
}
```

### Client Initializer
**Purpose**: Client-only initialization (rendering, screens, keybindings)

```java
public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register entity renderers
        EntityRendererRegistry.register(ModEntities.GUARD, GuardRenderer::new);

        // Register screen handlers
        HandledScreens.register(ModScreenHandlers.GUARD_HIRING, GuardHiringScreen::new);

        // Register client-side events
        ClientPlayConnectionEvents.JOIN.register(this::onClientJoin);

        // Register keybindings
        KeyBindingHelper.registerKeyBinding(ModKeybindings.OPEN_GUARD_GUI);
    }

    private void onClientJoin(ClientPlayNetworkHandler handler,
                               PacketSender sender,
                               MinecraftClient client) {
        // Client joined server
    }
}
```

**fabric.mod.json entry**:
```json
{
  "entrypoints": {
    "client": ["com.example.mymod.MyModClient"]
  }
}
```

### Dedicated Server Initializer
**Purpose**: Dedicated server-only initialization (rare, usually not needed)

```java
public class MyModServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        // Server-only initialization
    }
}
```

## Fabric Registry System

### Registries Class
All vanilla registries available via `Registries`:

```java
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRegistry {
    // Items
    public static final Item GUARD_SPAWN_EGG = register(
        Registries.ITEM,
        "guard_spawn_egg",
        new SpawnEggItem(ModEntities.GUARD, 0x8B4513, 0xD2691E,
            new Item.Settings())
    );

    // Blocks
    public static final Block GUARD_POST = register(
        Registries.BLOCK,
        "guard_post",
        new GuardPostBlock(AbstractBlock.Settings.create())
    );

    // Block Entities
    public static final BlockEntityType<GuardPostBlockEntity> GUARD_POST_BE = register(
        Registries.BLOCK_ENTITY_TYPE,
        "guard_post",
        BlockEntityType.Builder.create(
            GuardPostBlockEntity::new,
            GUARD_POST
        ).build()
    );

    // Entities
    public static final EntityType<GuardEntity> GUARD = register(
        Registries.ENTITY_TYPE,
        "guard",
        EntityType.Builder.create(GuardEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.6f, 1.95f)
            .build()
    );

    // Screen Handlers
    public static final ScreenHandlerType<GuardHiringScreenHandler> GUARD_HIRING = register(
        Registries.SCREEN_HANDLER,
        "guard_hiring",
        new ScreenHandlerType<>(GuardHiringScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );

    private static <T> T register(Registry<T> registry, String name, T entry) {
        return Registry.register(registry, Identifier.of(MOD_ID, name), entry);
    }

    public static void initialize() {
        // Static initialization triggers all registrations
    }
}
```

### Registry Pattern
**Best Practice**: Use static initialization with explicit initialize() call

```java
// In ModInitializer
@Override
public void onInitialize() {
    ModRegistry.initialize(); // Triggers static initialization
}
```

## Fabric Events System

### Prefer Events Over Mixins
**Rule**: Always use Fabric API events when available. Only use mixins when no event exists.

### Common Fabric Events

#### Lifecycle Events
```java
import net.fabricmc.fabric.api.event.lifecycle.v1.*;

// Server lifecycle
ServerLifecycleEvents.SERVER_STARTING.register(server -> {
    // Server is starting
});

ServerLifecycleEvents.SERVER_STARTED.register(server -> {
    // Server has started
});

ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
    // Server is stopping
});

// World events
ServerWorldEvents.LOAD.register((server, world) -> {
    // World loaded
});

ServerWorldEvents.UNLOAD.register((server, world) -> {
    // World unloaded
});

// Entity events
ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
    // Entity loaded into world
});
```

#### Player Events
```java
import net.fabricmc.fabric.api.entity.event.v1.*;

// Player attack
AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
    if (entity instanceof GuardEntity guard) {
        // Player attacked a guard
        return ActionResult.FAIL; // Cancel attack
    }
    return ActionResult.PASS; // Allow attack
});

// Player use entity (right-click)
UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
    if (entity instanceof VillagerEntity && !world.isClient) {
        // Open hiring GUI
        player.openHandledScreen(new GuardHiringScreenHandlerFactory());
        return ActionResult.SUCCESS;
    }
    return ActionResult.PASS;
});
```

#### Block Events
```java
import net.fabricmc.fabric.api.event.player.*;

// Block break
PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
    if (state.getBlock() instanceof GuardPostBlock) {
        // Prevent breaking guard post if guards are assigned
        return false; // Cancel break
    }
    return true; // Allow break
});
```

#### Item Events
```java
import net.fabricmc.fabric.api.event.player.*;

// Item use
UseItemCallback.EVENT.register((player, world, hand) -> {
    ItemStack stack = player.getStackInHand(hand);
    if (stack.getItem() == ModItems.GUARD_WHISTLE) {
        // Use guard whistle
        return TypedActionResult.success(stack);
    }
    return TypedActionResult.pass(stack);
});
```

#### Client Events
```java
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;

// Client tick
ClientTickEvents.END_CLIENT_TICK.register(client -> {
    // Called every client tick
});

// Render events
WorldRenderEvents.AFTER_ENTITIES.register(context -> {
    // Render after entities
});

// Connection events
ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
    // Joined server/world
});

ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
    // Disconnected
});
```

## Fabric Networking

### Packet Registration

#### Define Payload
```java
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record GuardHirePayload(int villagerId, GuardType type) implements CustomPayload {
    public static final Id<GuardHirePayload> ID =
        new Id<>(Identifier.of(MOD_ID, "guard_hire"));

    public static final PacketCodec<RegistryByteBuf, GuardHirePayload> CODEC =
        PacketCodec.tuple(
            PacketCodecs.VAR_INT, GuardHirePayload::villagerId,
            GuardType.PACKET_CODEC, GuardHirePayload::type,
            GuardHirePayload::new
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
```

#### Register Packets
```java
import net.fabricmc.fabric.api.networking.v1.*;

public class ModNetworking {
    public static void registerReceivers() {
        // Server receives from client
        PayloadTypeRegistry.playC2S().register(
            GuardHirePayload.ID,
            GuardHirePayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
            GuardHirePayload.ID,
            ModNetworking::handleGuardHire
        );

        // Client receives from server
        PayloadTypeRegistry.playS2C().register(
            GuardDataSyncPayload.ID,
            GuardDataSyncPayload.CODEC
        );
    }

    private static void handleGuardHire(GuardHirePayload payload,
                                        ServerPlayNetworking.Context context) {
        // IMPORTANT: Use server.execute() for world modifications
        context.server().execute(() -> {
            ServerPlayerEntity player = context.player();
            // Find villager and hire as guard
            // ...
        });
    }
}
```

#### Send Packets
```java
// Client to Server
ServerPlayNetworking.send(new GuardHirePayload(villagerId, GuardType.ARCHER));

// Server to Client
ServerPlayNetworking.send(player, new GuardDataSyncPayload(guardId, data));

// Server to all players tracking entity
for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
    ServerPlayNetworking.send(player, payload);
}
```

### Thread Safety Rules
- ❌ **NEVER** modify world directly in packet handler
- ✅ **ALWAYS** use `server.execute(() -> { ... })` for world changes
- ✅ Client packets arrive on netty thread, not game thread
- ✅ Server packets arrive on netty thread, not server thread

## Fabric Mixins

### When to Use Mixins
**Only when**: No Fabric API event exists for your use case

**Prefer Events** because:
- Better mod compatibility
- Official API support
- Less fragile (no internal class dependencies)
- Easier to debug

### Mixin Basics

#### Mixin Class
```java
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        VillagerEntity self = (VillagerEntity) (Object) this;

        if (!self.getWorld().isClient && player.isSneaking()) {
            // Open hiring GUI
            player.openHandledScreen(new GuardHiringScreenHandlerFactory(self));
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
```

#### Mixin JSON Configuration
**File**: `src/main/resources/mymod.mixins.json`

```json
{
  "required": true,
  "package": "com.example.mymod.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": [
    "VillagerEntityMixin"
  ],
  "client": [
    "ClientOnlyMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

#### Register Mixins in fabric.mod.json
```json
{
  "mixins": [
    "mymod.mixins.json"
  ]
}
```

### Common Injection Points
- `@At("HEAD")` - Start of method
- `@At("TAIL")` - End of method (before return)
- `@At("RETURN")` - At every return statement
- `@At("INVOKE", target = "...")` - Before specific method call

## Fabric GUI System

### Screen Handler (Server Logic)
```java
public class GuardHiringScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public GuardHiringScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public GuardHiringScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.GUARD_HIRING, syncId);
        this.inventory = inventory;

        // Add inventory slots
        this.addSlot(new Slot(inventory, 0, 80, 35));

        // Add player inventory slots
        // ... (standard 9x4 inventory grid)
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
```

### Screen (Client Rendering)
```java
@Environment(EnvType.CLIENT)
public class GuardHiringScreen extends HandledScreen<GuardHiringScreenHandler> {
    private static final Identifier TEXTURE =
        Identifier.of(MOD_ID, "textures/gui/guard_hiring.png");

    public GuardHiringScreen(GuardHiringScreenHandler handler,
                             PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
```

### Register Screen
```java
// In ClientModInitializer
HandledScreens.register(ModScreenHandlers.GUARD_HIRING, GuardHiringScreen::new);
```

## Fabric-Specific Patterns

### Client-Server Separation
```java
// ❌ BAD - imports client class in common code
import net.minecraft.client.MinecraftClient;

public class MyMod {
    public void doSomething() {
        MinecraftClient.getInstance(); // CRASH on dedicated server!
    }
}

// ✅ GOOD - separate client code
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Common code only
    }
}

public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Client code here
        MinecraftClient.getInstance(); // Safe here
    }
}
```

### Environment Checking
```java
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// Method-level annotation
@Environment(EnvType.CLIENT)
public void clientOnlyMethod() {
    // Client code
}

// Class-level annotation
@Environment(EnvType.CLIENT)
public class ClientOnlyClass {
    // Entire class is client-only
}

// Runtime check
if (world.isClient) {
    // Client side
} else {
    // Server side
}
```

## Fabric Resources

### Official Documentation
- **Fabric Wiki**: https://fabricmc.net/wiki/
- **Fabric API Docs**: https://maven.fabricmc.net/docs/fabric-api-[version]/
- **Yarn Mappings**: https://github.com/FabricMC/yarn

### Development Tools
- **Fabric Loom**: Gradle plugin for Fabric mod development
- **Fabric API**: Core library with events and utilities
- **Fabric Loader**: Mod loader itself

### Useful Links
- **Fabric Discord**: Real-time community support
- **Fabric GitHub**: Source code and issues
- **Mod Template**: https://github.com/FabricMC/fabric-example-mod

## When to Use This Skill

Use this skill when:
- Working with Fabric API systems (events, networking, lifecycle)
- Implementing Fabric-specific patterns (ModInitializer, ClientModInitializer)
- Registering items/blocks/entities via Fabric registries
- Creating custom packets with Fabric networking
- Using Fabric events vs writing mixins
- Setting up client-server separation
- Questions about "How do I do X with Fabric API?"
- Questions about "What's the Fabric way to implement Y?"
- Debugging Fabric-specific issues (event timing, mixin conflicts)

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used fabric-modding
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used fabric-modding`

This helps track which skills are actively consulted and identifies documentation gaps.
