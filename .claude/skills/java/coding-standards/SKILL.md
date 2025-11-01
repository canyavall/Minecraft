---
name: coding-standards
description: Java and Minecraft modding coding standards including naming conventions, package organization, documentation requirements, code style, and architectural patterns. Use when writing any code to ensure consistency and quality.
allowed-tools: [Read, Write, Edit]
---


# Coding Standards Skill

Mandatory coding standards for Java and Minecraft Fabric mod development.

## Naming Conventions

### Java Naming Standards

| Element | Convention | Example |
|---------|------------|---------|
| **Packages** | lowercase, dot-separated | `com.xeenaa.villagermanager.network` |
| **Classes** | PascalCase, descriptive nouns | `ProfessionSelectionScreen` |
| **Interfaces** | PascalCase, capability-focused | `ProfessionProvider`, `Configurable` |
| **Records** | PascalCase, data-focused | `ProfessionData`, `NetworkMessage` |
| **Methods** | camelCase, verb-based | `openProfessionGui()`, `validateInput()` |
| **Fields** | camelCase | `professionManager`, `selectedVillager` |
| **Constants** | UPPER_SNAKE_CASE | `MAX_PROFESSION_COUNT`, `MOD_ID` |
| **Enums** | PascalCase for type, UPPER for values | `GuiState.OPEN` |
| **Type Parameters** | Single uppercase letter | `<T>`, `<K, V>`, `<E>` |
| **Mixin Classes** | Target + "Mixin" suffix | `VillagerEntityMixin` |

### Minecraft-Specific Naming

| Element | Convention | Example |
|---------|------------|---------|
| **Mod ID** | lowercase, no underscores | `xeenaa_villager_manager` |
| **Registry Entries** | lowercase, underscores | `guard_spawn_egg` |
| **Packet IDs** | lowercase, underscores | `guard_hire`, `sync_data` |
| **Screen Handlers** | PascalCase + "ScreenHandler" | `GuardHiringScreenHandler` |
| **Screens** | PascalCase + "Screen" | `GuardHiringScreen` |
| **Data Components** | UPPER_SNAKE_CASE | `GUARD_TYPE`, `ATTACK_POWER` |

## Package Organization

### Standard Package Structure

```
src/
├── main/java/com/<author>/<modid>/
│   ├── ModInitializer.java            # Main entry point
│   ├── network/                       # Client-server communication
│   ├── registry/                      # Item/Block/Entity registration
│   ├── entity/                        # Custom entities
│   ├── block/                         # Custom blocks
│   ├── item/                          # Custom items
│   ├── util/                          # Utilities
│   └── mixin/common/                  # Server/common mixins
│
├── client/java/com/<author>/<modid>/
│   ├── ModClientInitializer.java      # Client entry point
│   ├── client/gui/                    # GUI components
│   ├── client/render/                 # Entity/block renderers
│   └── mixin/client/                  # Client-only mixins
│
└── resources/
    ├── fabric.mod.json
    ├── <modid>.mixins.json
    └── assets/<modid>/
```

**See `examples/package-organization.md` for detailed structure and rules**

### Package Responsibility

| Package | Purpose | Side |
|---------|---------|------|
| `root` | Entry points and core | Both |
| `network` | Client-server communication | Both |
| `registry` | Registration | Both |
| `entity/block/item` | Game objects | Both |
| `client.gui` | User interface | Client |
| `client.render` | Renderers | Client |
| `mixin.common` | Common mixins | Both |
| `mixin.client` | Client mixins | Client |

**Rules**:
- ✅ Organize by feature when packages grow large
- ❌ Never mix client and server code in same package

## Code Style

### Formatting Rules

- **Indentation**: 4 spaces (not tabs)
- **Braces**: K&R style (opening on same line), always use braces
- **Line Length**: Max 120 characters
- **Blank Lines**: One between methods, two between sections
- **Imports**: Group Java → Minecraft → Fabric → Mod, no wildcards

**See `examples/code-style.md` for detailed examples**

## Documentation Standards

### Javadoc Requirements

**All public classes, methods, and fields MUST have Javadoc**:

```java
/**
 * Brief one-sentence summary.
 * <p>
 * Detailed description in paragraph.
 *
 * @param name parameter description
 * @return return value description
 * @throws IllegalArgumentException if invalid input
 * @see RelatedClass
 * @since 1.0.0
 */
```

**Class Javadoc**: Summary + detailed description + `@see` + `@since`

**Method Javadoc**: Summary + description + `@param` + `@return` + `@throws`

**Field Javadoc**: Brief description + units/range if applicable

**See `examples/javadoc-examples.md` for complete examples**

## Mixin Standards

### When to Use Mixins

**✅ USE when**:
- No Fabric API event exists
- Modifying vanilla behavior not exposed
- Performance-critical modifications

**❌ DON'T use when**:
- Fabric API event is available
- Can use composition or inheritance
- Modifying third-party mod code

### Mixin Rules

**Naming**: `<TargetClass>Mixin`

**Organization**:
- Common mixins in `mixin/common/`
- Client mixins in `mixin/client/`
- Separate config files

**Best Practices**:
- Use `@Inject` over `@Overwrite`
- Minimize injection points
- Document WHY mixin is needed
- Prefer `@At("HEAD")` or `@At("RETURN")`

**See `examples/mixin-examples.md` for complete patterns**

## Client-Server Separation

### Critical Rules

**❌ NEVER import client classes in common code** - crashes dedicated servers!

**✅ ALWAYS separate**:
- Client code in `client/` source set
- No client imports in `main/` source set
- Use `world.isClient` for side checks
- Use `@Environment(EnvType.CLIENT)` annotation

**See `examples/client-server-separation.md` for examples**

## Architecture Patterns

Common patterns for Minecraft modding:

- **Singleton Registry**: Managing collections (guards, items, etc.)
- **Factory Pattern**: Creating entities with complex initialization
- **Builder Pattern**: Objects with many optional parameters

**See `examples/architecture-patterns.md` for complete implementations**

## Error Handling

### Input Validation

**All public methods MUST validate inputs**:

```java
public void setLevel(int level) {
    if (level < 1 || level > MAX_LEVEL) {
        throw new IllegalArgumentException(
            "Level must be between 1 and " + MAX_LEVEL + ", got: " + level
        );
    }
    this.level = level;
}
```

**Principles**:
- Validate all inputs
- Use specific exception types
- Fail fast with clear error messages
- Never silently ignore invalid inputs

**See `examples/error-handling.md` for patterns**

## Security and Validation

### Never Trust Client Input

**Always validate on server**:

```java
private static void handlePacket(Payload payload, Context context) {
    context.server().execute(() -> {
        // ✅ VALIDATE everything from client
        if (payload.id() < 0) {
            LOGGER.warn("Invalid ID from client: {}", payload.id());
            return;
        }

        // Verify permissions
        if (!hasPermission(context.player())) {
            LOGGER.warn("Unauthorized action");
            return;
        }

        // Execute action
    });
}
```

**See `examples/security-validation.md` for complete patterns**

## Version Control

### Commit Messages

**Format**: `<type>: <description>`

**Types**: `feat`, `fix`, `refactor`, `docs`, `test`, `perf`, `style`, `chore`

**Examples**:
```
feat: add guard armor customization GUI
fix: guards losing equipment on server restart
refactor: extract guard hiring logic to service
```

**See `examples/version-control.md` for guidelines**

## Standards Compliance Checklist

Before committing code:

### Naming
- [ ] Classes use PascalCase
- [ ] Methods use camelCase, start with verbs
- [ ] Constants use UPPER_SNAKE_CASE
- [ ] Mixins named `<Target>Mixin`

### Organization
- [ ] Client code in `client/` source set
- [ ] No client imports in `main/` source set
- [ ] Mixin configs separated (client vs common)

### Documentation
- [ ] All public classes have Javadoc
- [ ] All public methods have `@param`, `@return`, `@throws`
- [ ] Complex logic has comments

### Code Quality
- [ ] Inputs validated in public methods
- [ ] Specific exception types used
- [ ] No wildcard imports
- [ ] Consistent formatting (4 spaces, K&R braces)
- [ ] Line length < 120 characters

### Minecraft-Specific
- [ ] `world.isClient` checks for side logic
- [ ] Fabric events used instead of mixins (when available)
- [ ] Mod ID matches fabric.mod.json
- [ ] Registry entries use lowercase_snake_case

### Security
- [ ] All client input validated on server
- [ ] Range checks for numeric inputs
- [ ] Null checks for parameters
- [ ] Permission checks for sensitive operations

## When to Use This Skill

- Writing any Java code for Minecraft mods
- Creating new classes, methods, or packages
- Reviewing code for standards compliance
- Naming anything (classes, methods, variables)
- Organizing project structure
- Writing documentation
- Creating mixins
- Handling errors and validation
- Ensuring client-server separation

## Key Principles

1. **Consistency**: Follow conventions everywhere
2. **Clarity**: Self-documenting code with clear names
3. **Safety**: Validate inputs, fail fast
4. **Separation**: Strict client/server separation
5. **Documentation**: Comprehensive Javadoc for public APIs
6. **Security**: Never trust client input

## Examples

**Detailed examples available in**:
- `examples/package-organization.md` - Complete package structure
- `examples/code-style.md` - Formatting and style examples
- `examples/javadoc-examples.md` - Documentation examples
- `examples/mixin-examples.md` - Mixin patterns
- `examples/client-server-separation.md` - Side separation
- `examples/architecture-patterns.md` - Design patterns
- `examples/error-handling.md` - Validation and exceptions
- `examples/security-validation.md` - Server-side validation
- `examples/version-control.md` - Git conventions

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used coding-standards
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used coding-standards`

This helps track which skills are actively consulted and identifies documentation gaps.
