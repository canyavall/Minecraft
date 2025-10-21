---
name: coding-standards
description: Java and Minecraft modding coding standards including naming conventions, package organization, documentation requirements, code style, and architectural patterns. Use when writing any code to ensure consistency and quality.
allowed-tools: [Read, Write, Edit]
---

# Coding Standards Skill

Mandatory coding standards for Java and Minecraft Fabric mod development.

## Naming Conventions

### Java Naming Standards

| Element | Convention | Example | Requirements |
|---------|------------|---------|-------------|
| **Packages** | lowercase, dot-separated | `com.xeenaa.villagermanager.network` | No abbreviations, clear hierarchy |
| **Classes** | PascalCase, descriptive nouns | `ProfessionSelectionScreen` | Avoid abbreviations, be specific |
| **Interfaces** | PascalCase, capability-focused | `ProfessionProvider`, `Configurable` | Use -able, -er, or descriptive nouns |
| **Records** | PascalCase, data-focused | `ProfessionData`, `NetworkMessage` | Represent immutable data structures |
| **Methods** | camelCase, verb-based | `openProfessionGui()`, `validateInput()` | Start with verbs, be descriptive |
| **Fields** | camelCase | `professionManager`, `selectedVillager` | Descriptive, avoid Hungarian notation |
| **Constants** | UPPER_SNAKE_CASE | `MAX_PROFESSION_COUNT`, `MOD_ID` | Use for true constants only |
| **Enums** | PascalCase for type, UPPER_CASE for values | `GuiState.OPEN`, `Side.CLIENT` | Clear hierarchy |
| **Type Parameters** | Single uppercase letter | `<T>`, `<K, V>`, `<E>` | Standard generics conventions |
| **Mixin Classes** | Target class + "Mixin" suffix | `VillagerEntityMixin` | Clear target identification |

### Minecraft-Specific Naming

| Element | Convention | Example | Notes |
|---------|------------|---------|-------|
| **Mod ID** | lowercase, no underscores | `xeenaa_villager_manager` | Match fabric.mod.json |
| **Registry Entries** | lowercase, underscores | `guard_spawn_egg` | Match resource pack conventions |
| **Packet IDs** | lowercase, underscores | `guard_hire`, `sync_data` | Descriptive action names |
| **Screen Handlers** | PascalCase + "ScreenHandler" | `GuardHiringScreenHandler` | Server-side GUI logic |
| **Screens** | PascalCase + "Screen" | `GuardHiringScreen` | Client-side GUI rendering |
| **Data Components** | UPPER_SNAKE_CASE | `GUARD_TYPE`, `ATTACK_POWER` | Registered components |

## Package Organization

### Standard Package Structure

```
src/
├── main/java/com/<author>/<modid>/
│   ├── ModInitializer.java            # Main entry point (server/common)
│   ├── network/                       # Client-server communication
│   │   ├── packets/                   # Packet payload classes
│   │   └── ServerPacketHandler.java   # Server-side packet processing
│   ├── registry/                      # Item/Block/Entity registration
│   │   ├── ModItems.java              # Item registry
│   │   ├── ModBlocks.java             # Block registry
│   │   ├── ModEntities.java           # Entity registry
│   │   └── ModComponents.java         # Data component registry
│   ├── entity/                        # Custom entities
│   │   ├── GuardEntity.java           # Entity class
│   │   └── ai/                        # AI goals and behaviors
│   ├── block/                         # Custom blocks
│   │   └── entity/                    # Block entities
│   ├── item/                          # Custom items
│   ├── util/                          # Utility classes and helpers
│   │   ├── Constants.java             # Mod-wide constants
│   │   └── ValidationUtils.java       # Input validation
│   └── mixin/                         # Mixins (use sparingly)
│       └── common/                    # Server/common mixins only
│
├── client/java/com/<author>/<modid>/
│   ├── ModClientInitializer.java      # Client entry point
│   ├── client/
│   │   ├── gui/                       # Client-side GUI components
│   │   │   ├── screen/                # Screen classes
│   │   │   └── widget/                # Custom widgets
│   │   ├── render/                    # Entity/block renderers
│   │   │   └── entity/                # Entity renderers
│   │   └── util/                      # Client-only utilities
│   └── mixin/
│       └── client/                    # Client-only mixins
│
└── resources/
    ├── fabric.mod.json                # Mod metadata
    ├── <modid>.mixins.json            # Common mixin config
    ├── <modid>.client.mixins.json     # Client mixin config
    └── assets/<modid>/
        ├── lang/                      # Translations
        │   └── en_us.json
        ├── textures/                  # Textures
        │   ├── entity/
        │   ├── item/
        │   ├── block/
        │   └── gui/
        └── models/                    # Models
            ├── item/
            └── block/
```

### Package Responsibility Rules

| Package | Purpose | Side | Dependencies Allowed |
|---------|---------|------|---------------------|
| `root` | Entry points and core initialization | Both | Fabric API, Minecraft common |
| `network` | Client-server communication | Both | Fabric Networking |
| `registry` | Item/block/entity registration | Both | Minecraft registries |
| `entity` | Custom entity implementations | Both | Minecraft entity system |
| `block` | Custom block implementations | Both | Minecraft block system |
| `item` | Custom item implementations | Both | Minecraft item system |
| `util` | Shared utilities | Both | Minimal external deps |
| `client.gui` | User interface components | Client | Minecraft client, rendering |
| `client.render` | Custom renderers | Client | Client-side rendering APIs |
| `client.util` | Client-specific utilities | Client | Client-side APIs only |
| `mixin.common` | Server/common game logic mixins | Both | Target classes only |
| `mixin.client` | Client-side mixins | Client | Client target classes only |

**Rules**:
- ✅ **DO** organize by feature when packages grow large (e.g., `entity/guard/`, `entity/villager/`)
- ✅ **DO** keep related classes together
- ❌ **DON'T** mix client and server code in same package
- ❌ **DON'T** create deep package hierarchies unnecessarily (max 4-5 levels)

## Code Style and Formatting

### General Formatting

```java
// ✅ GOOD - clear, formatted, readable
public class GuardEntity extends PathAwareEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardEntity.class);
    private static final int MAX_LEVEL = 10;

    private final GuardType type;
    private int level;

    public GuardEntity(EntityType<? extends GuardEntity> entityType, World world) {
        super(entityType, world);
        this.level = 1;
    }

    public void setLevel(int level) {
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Level must be between 1 and " + MAX_LEVEL);
        }
        this.level = level;
    }
}

// ❌ BAD - inconsistent, unreadable
public class GuardEntity extends PathAwareEntity{
private int level;private final GuardType type;
public GuardEntity(EntityType<? extends GuardEntity> entityType,World world){super(entityType,world);this.level=1;}
public void setLevel(int level){this.level=level;}
}
```

### Formatting Rules

**Indentation**:
- Use 4 spaces (not tabs)
- Consistent indentation for all blocks
- Align chained method calls

**Braces**:
- Opening brace on same line (Kernighan & Ritchie style)
- Always use braces, even for single-line if/for/while

**Line Length**:
- Maximum 120 characters per line
- Break long lines at logical points

**Blank Lines**:
- One blank line between methods
- Two blank lines between class sections
- Blank line after class declaration

**Imports**:
- Group imports: Java SE → Minecraft → Fabric → Mod
- No wildcard imports (except in very large groups)
- Remove unused imports

## Documentation Standards

### Javadoc Requirements

**All public classes, methods, and fields MUST have Javadoc**:

```java
/**
 * Represents a custom guard entity that protects villages and players.
 * <p>
 * Guards can be hired, upgraded, and equipped with armor and weapons.
 * They follow AI goals to patrol, defend, and attack hostile mobs.
 *
 * @see GuardType
 * @see GuardManager
 * @since 1.0.0
 */
public class GuardEntity extends PathAwareEntity {

    /**
     * Maximum level a guard can reach through upgrades.
     */
    private static final int MAX_LEVEL = 10;

    /**
     * Sets the guard's level, affecting stats and capabilities.
     * <p>
     * Higher levels grant increased health, damage, and special abilities.
     *
     * @param level the new level (must be between 1 and {@value MAX_LEVEL})
     * @throws IllegalArgumentException if level is out of valid range
     */
    public void setLevel(int level) {
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalArgumentException(
                "Level must be between 1 and " + MAX_LEVEL + ", got: " + level
            );
        }
        this.level = level;
        recalculateStats();
    }

    /**
     * Calculates the guard's attack damage based on level and equipment.
     * <p>
     * Base damage is determined by guard type, then modified by:
     * <ul>
     *   <li>Level bonus: {@code level * 0.5}</li>
     *   <li>Equipment bonus: weapon damage + enchantments</li>
     *   <li>Buff effects: temporary status effects</li>
     * </ul>
     *
     * @return the total attack damage value
     * @see #recalculateStats()
     */
    public float calculateAttackDamage() {
        float base = type.getBaseDamage();
        float levelBonus = level * 0.5f;
        float equipmentBonus = getEquipmentBonus();
        return base + levelBonus + equipmentBonus;
    }
}
```

### Javadoc Guidelines

**Class Javadoc Must Include**:
- Brief one-sentence summary
- Detailed description in `<p>` paragraph
- `@see` references to related classes
- `@since` version when class was added

**Method Javadoc Must Include**:
- Brief one-sentence summary
- Detailed description of behavior
- `@param` for each parameter with description
- `@return` for non-void methods with description
- `@throws` for each checked exception with conditions

**Field Javadoc Must Include**:
- Brief description of purpose
- Units if applicable (e.g., "in milliseconds", "in blocks")
- Valid range if applicable

**Internal/Private Members**:
- Use single-line `//` comments for brief explanations
- Use Javadoc if logic is complex or non-obvious

## Mixin Standards and Restrictions

### When to Use Mixins

**✅ USE mixins when**:
- No Fabric API event exists for the hook point
- Modifying vanilla behavior not exposed through API
- Performance-critical modifications (avoid event overhead)

**❌ DON'T use mixins when**:
- Fabric API event is available (use event instead!)
- Can be achieved through composition or inheritance
- Modifying third-party mod code (compatibility issues)

### Mixin Naming and Organization

```java
// ✅ GOOD - clear target and purpose
@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

    @Inject(
        method = "interactMob",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        // Implementation
    }
}

// ❌ BAD - unclear purpose, poor naming
@Mixin(VillagerEntity.class)
public class MyMixin {
    @Inject(method = "interactMob", at = @At("HEAD"))
    private void inject(PlayerEntity p, Hand h, CallbackInfoReturnable<ActionResult> c) {
        // What does this do?
    }
}
```

### Mixin Rules

**Naming**:
- Class name: `<TargetClass>Mixin`
- Method name: `on<OriginalMethodName>` or descriptive action

**Organization**:
- Common/server mixins in `mixin/common/`
- Client mixins in `mixin/client/`
- Separate mixin config files for client and common

**Best Practices**:
- Use `@Inject` over `@Overwrite` (compatibility)
- Minimize injection points (performance)
- Always document WHY mixin is needed
- Use `cancellable = true` only when necessary
- Prefer `@At("HEAD")` or `@At("RETURN")` over complex injection points

## Client-Server Separation

### Critical Rules

**❌ NEVER import client classes in common code**:
```java
// ❌ CATASTROPHIC - crashes dedicated server
import net.minecraft.client.MinecraftClient;

public class MyMod implements ModInitializer {
    public void onInitialize() {
        MinecraftClient.getInstance(); // CRASH!
    }
}
```

**✅ ALWAYS separate client and server code**:
```java
// ✅ GOOD - client code in client initializer
public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MinecraftClient.getInstance(); // Safe here
    }
}
```

### Package Separation

```
src/
├── main/java/           # Common and server code (NO client imports!)
└── client/java/         # Client-only code (can import client classes)
```

**Rules**:
- ✅ **DO** use `world.isClient` to check logical side
- ✅ **DO** use `@Environment(EnvType.CLIENT)` for client-only classes/methods
- ❌ **DON'T** import client classes in main source set
- ❌ **DON'T** reference client-only classes from common code

## Architecture Patterns

### Singleton Registry Pattern

**Use for**: Managing collections of custom entities/items/blocks

```java
public class GuardRegistry {
    private static GuardRegistry instance;
    private final Map<UUID, GuardEntity> guards = new ConcurrentHashMap<>();

    private GuardRegistry() {
        // Private constructor
    }

    public static GuardRegistry getInstance() {
        if (instance == null) {
            instance = new GuardRegistry();
        }
        return instance;
    }

    public void registerGuard(GuardEntity guard) {
        guards.put(guard.getUuid(), guard);
    }

    public GuardEntity getGuard(UUID uuid) {
        return guards.get(uuid);
    }

    // Thread-safe for server environment
    public List<GuardEntity> getAllGuards() {
        return new ArrayList<>(guards.values());
    }
}
```

### Factory Pattern for Entities

**Use for**: Creating entities with complex initialization

```java
public class GuardFactory {
    public static GuardEntity createGuard(World world, GuardType type, int level) {
        GuardEntity guard = new GuardEntity(ModEntities.GUARD, world);
        guard.setGuardType(type);
        guard.setLevel(level);
        guard.initializeEquipment();
        guard.initializeAI();
        return guard;
    }

    public static GuardEntity createDefaultGuard(World world) {
        return createGuard(world, GuardType.SWORDSMAN, 1);
    }
}
```

### Builder Pattern for Complex Objects

**Use for**: Objects with many optional parameters

```java
public record GuardStats(
    int level,
    float health,
    float damage,
    float defense,
    float speed
) {
    public static class Builder {
        private int level = 1;
        private float health = 20.0f;
        private float damage = 5.0f;
        private float defense = 0.0f;
        private float speed = 1.0f;

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder health(float health) {
            this.health = health;
            return this;
        }

        public GuardStats build() {
            return new GuardStats(level, health, damage, defense, speed);
        }
    }
}

// Usage
GuardStats stats = new GuardStats.Builder()
    .level(5)
    .health(50.0f)
    .damage(10.0f)
    .build();
```

## Error Handling Standards

### Input Validation

**All public methods MUST validate inputs**:

```java
// ✅ GOOD - validates inputs, clear error messages
public void hireGuard(PlayerEntity player, GuardType type, int cost) {
    if (player == null) {
        throw new IllegalArgumentException("Player cannot be null");
    }
    if (type == null) {
        throw new IllegalArgumentException("GuardType cannot be null");
    }
    if (cost < 0) {
        throw new IllegalArgumentException("Cost cannot be negative: " + cost);
    }
    if (world.isClient) {
        throw new IllegalStateException("Cannot hire guard on client side");
    }

    // Implementation
}

// ❌ BAD - no validation, unclear failures
public void hireGuard(PlayerEntity player, GuardType type, int cost) {
    player.getInventory().removeStack(cost); // NPE if player is null!
}
```

### Exception Handling

**Use specific exception types**:

```java
// ✅ GOOD - specific exceptions
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(int required, int available) {
        super(String.format("Insufficient funds: need %d, have %d", required, available));
    }
}

// Usage
if (emeralds < cost) {
    throw new InsufficientFundsException(cost, emeralds);
}
```

### Fail Fast Principle

```java
// ✅ GOOD - fail fast with clear error
public void setLevel(int level) {
    if (level < 1 || level > MAX_LEVEL) {
        throw new IllegalArgumentException(
            "Level must be between 1 and " + MAX_LEVEL + ", got: " + level
        );
    }
    this.level = level;
}

// ❌ BAD - silent failure
public void setLevel(int level) {
    if (level >= 1 && level <= MAX_LEVEL) {
        this.level = level;
    }
    // What if level is invalid? Silently ignored!
}
```

## Version Control Standards

### Commit Messages

**Format**: `<type>: <description>`

**Types**:
- `feat` - New feature
- `fix` - Bug fix
- `refactor` - Code refactoring (no behavior change)
- `docs` - Documentation changes
- `test` - Adding/updating tests
- `perf` - Performance improvements
- `style` - Code style changes (formatting)
- `chore` - Build/tooling changes

**Examples**:
```
feat: add guard armor customization GUI
fix: guards losing equipment on server restart
refactor: extract guard hiring logic to service
docs: update README with installation instructions
perf: cache guard pathfinding results
```

### Branch Naming

**Format**: `<type>/<description>`

**Examples**:
```
feat/guard-armor-system
fix/guard-persistence-bug
refactor/networking-layer
```

## Mod Compatibility Guidelines

### Defensive Coding for Compatibility

**Check for mod presence before integration**:

```java
// ✅ GOOD - conditional integration
if (FabricLoader.getInstance().isModLoaded("some_mod")) {
    // Use Some Mod's API
    SomeModIntegration.register();
}

// ❌ BAD - assumes mod is present
SomeModIntegration.register(); // ClassNotFoundException if mod not installed!
```

### Avoid Breaking Changes

**Use `@Deprecated` instead of removing methods**:

```java
/**
 * @deprecated Use {@link #hireGuard(PlayerEntity, GuardType, int)} instead.
 * This method will be removed in version 2.0.0.
 */
@Deprecated(since = "1.5.0", forRemoval = true)
public void hireGuard(PlayerEntity player, GuardType type) {
    hireGuard(player, type, getGuardCost(type));
}
```

## Security and Validation

### Never Trust Client Input

**Always validate on server**:

```java
// Server-side packet handler
private static void handleGuardHire(GuardHirePayload payload, ServerPlayNetworking.Context context) {
    context.server().execute(() -> {
        ServerPlayerEntity player = context.player();

        // ✅ VALIDATE everything from client
        if (payload.villagerId() < 0) {
            LOGGER.warn("Invalid villager ID from client: {}", payload.villagerId());
            return;
        }

        if (payload.type() == null) {
            LOGGER.warn("Null guard type from client");
            return;
        }

        // Verify player has permission
        if (!canHireGuard(player)) {
            LOGGER.warn("Player {} attempted to hire guard without permission", player.getName());
            return;
        }

        // Verify player has funds
        int cost = getGuardCost(payload.type());
        if (!hasEmeralds(player, cost)) {
            LOGGER.warn("Player {} has insufficient funds for guard", player.getName());
            return;
        }

        // Execute action
        hireGuard(player, payload.type());
    });
}
```

### Range Validation

```java
// ✅ GOOD - validate ranges
public void dealDamage(LivingEntity target, float amount) {
    if (amount < 0) {
        LOGGER.warn("Attempted negative damage: {}", amount);
        return;
    }
    if (amount > 1000) {
        LOGGER.warn("Suspiciously high damage: {}", amount);
        amount = 1000; // Cap to reasonable maximum
    }
    target.damage(getDamageSources().mob(this), amount);
}
```

## Standards Compliance Checklist

Before committing code, verify:

### Naming
- [ ] Classes use PascalCase
- [ ] Methods use camelCase, start with verbs
- [ ] Constants use UPPER_SNAKE_CASE
- [ ] Packages use lowercase, no abbreviations
- [ ] Mixins named `<Target>Mixin`

### Organization
- [ ] Client code in `client/` source set
- [ ] No client imports in `main/` source set
- [ ] Packages organized by feature/responsibility
- [ ] Mixin configs separated (client vs common)

### Documentation
- [ ] All public classes have Javadoc
- [ ] All public methods have Javadoc with `@param`, `@return`, `@throws`
- [ ] Complex logic has explanatory comments
- [ ] README updated if user-facing changes

### Code Quality
- [ ] Inputs validated in public methods
- [ ] Specific exception types used
- [ ] No wildcard imports
- [ ] No unused imports
- [ ] Consistent formatting (4 spaces, K&R braces)
- [ ] Line length < 120 characters

### Minecraft-Specific
- [ ] `world.isClient` checks for side-specific logic
- [ ] Fabric events used instead of mixins (when available)
- [ ] Mod ID matches fabric.mod.json
- [ ] Registry entries use lowercase_snake_case

### Performance
- [ ] No expensive operations in tick methods
- [ ] Collections pre-sized when size known
- [ ] Resources closed with try-with-resources
- [ ] Caching used for expensive calculations

### Security
- [ ] All client input validated on server
- [ ] Range checks for numeric inputs
- [ ] Null checks for all parameters
- [ ] Permission checks for sensitive operations

## When to Use This Skill

Use this skill when:
- Writing any Java code for Minecraft mods
- Creating new classes, methods, or packages
- Reviewing code for standards compliance
- Naming anything (classes, methods, variables, files)
- Organizing project structure
- Writing documentation
- Creating mixins
- Handling errors and validation
- Ensuring client-server separation
- Questions about "How should I name X?"
- Questions about "Where should this class go?"
- Questions about "What documentation do I need?"

## Key Principles

1. **Consistency**: Follow conventions everywhere, no exceptions
2. **Clarity**: Code should be self-documenting with clear names
3. **Safety**: Validate all inputs, fail fast with clear errors
4. **Separation**: Keep client and server code strictly separated
5. **Documentation**: All public APIs must have comprehensive Javadoc
6. **Compatibility**: Design for mod compatibility from the start
7. **Performance**: Be mindful of tick budget and memory usage
8. **Security**: Never trust client input, always validate on server
