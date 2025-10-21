---
name: java-development
description: Expert knowledge of Java 21 features including records, pattern matching, text blocks, sealed classes, virtual threads, and modern Java best practices. Use when writing Java code, refactoring, or answering Java-specific questions.
allowed-tools: [Read, Grep, Glob]
---

# Java 21 Development Skill

Expert knowledge for writing modern, clean Java 21 code following best practices.

## Java 21 Features

### Records (Java 14+)
**Use for**: Immutable data carriers

```java
// Instead of traditional class
public record GuardData(String name, int level, GuardType type) {
    // Compact constructor for validation
    public GuardData {
        if (level < 0) throw new IllegalArgumentException("Level must be >= 0");
    }

    // Custom methods allowed
    public boolean isMaxLevel() {
        return level >= 10;
    }
}
```

**Benefits**:
- Auto-generated: constructor, getters, equals(), hashCode(), toString()
- Immutable by default
- Concise and readable
- Perfect for DTOs, value objects, data transfer

### Pattern Matching for switch (Java 21)
**Use for**: Type checking and extraction

```java
// Pattern matching in switch
String format(Object obj) {
    return switch (obj) {
        case Integer i -> String.format("int %d", i);
        case String s -> String.format("String %s", s);
        case GuardData(var name, var level, _) ->
            String.format("Guard %s level %d", name, level);
        case null -> "null";
        default -> obj.toString();
    };
}

// Pattern matching with when clauses
String categorize(GuardData guard) {
    return switch (guard) {
        case GuardData g when g.level() >= 10 -> "Elite";
        case GuardData g when g.level() >= 5 -> "Veteran";
        case GuardData g -> "Novice";
    };
}
```

### Text Blocks (Java 15+)
**Use for**: Multi-line strings, JSON, SQL, HTML

```java
// Instead of concatenation
String json = """
    {
        "name": "%s",
        "level": %d,
        "type": "%s"
    }
    """.formatted(name, level, type);

// SQL queries
String query = """
    SELECT * FROM guards
    WHERE level >= ?
    AND type = ?
    ORDER BY level DESC
    """;
```

### Sealed Classes (Java 17+)
**Use for**: Restricted inheritance hierarchies

```java
public sealed interface GuardType
    permits Archer, Swordsman, Mage {
    int attackPower();
}

public final class Archer implements GuardType {
    public int attackPower() { return 5; }
}

public final class Swordsman implements GuardType {
    public int attackPower() { return 7; }
}

public final class Mage implements GuardType {
    public int attackPower() { return 6; }
}

// Exhaustive switch (compiler checks all cases)
int calculateDamage(GuardType type) {
    return switch (type) {
        case Archer a -> a.attackPower() * 2;
        case Swordsman s -> s.attackPower() * 3;
        case Mage m -> m.attackPower() + magicBonus;
        // No default needed - compiler ensures exhaustiveness
    };
}
```

### Virtual Threads (Java 21)
**Use for**: High-concurrency tasks (with caution in Minecraft)

```java
// Lightweight threads for I/O-bound tasks
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> loadDataFromDisk());
    executor.submit(() -> fetchFromNetwork());
}
```

**Warning**: Minecraft has specific threading rules. Virtual threads are useful for:
- Background data loading
- Database operations
- Network requests
- NOT for world modifications (use server.execute())

### Sequenced Collections (Java 21)
**Use for**: Order-aware collections

```java
List<Guard> guards = new ArrayList<>();
guards.addFirst(newGuard);      // Add to start
guards.addLast(anotherGuard);   // Add to end
Guard first = guards.getFirst(); // Get first
Guard last = guards.getLast();   // Get last
List<Guard> reversed = guards.reversed(); // Reversed view
```

## Modern Java Patterns

### Optional Instead of Null
```java
// ❌ Bad - null checks everywhere
public Guard findGuard(String name) {
    // returns null if not found
}

if (guard != null) {
    guard.attack();
}

// ✅ Good - Optional
public Optional<Guard> findGuard(String name) {
    return Optional.ofNullable(guardMap.get(name));
}

findGuard("Steve")
    .ifPresent(Guard::attack);

Guard guard = findGuard("Steve")
    .orElseThrow(() -> new GuardNotFoundException(name));
```

### Stream API for Collections
```java
// ❌ Bad - imperative loop
List<Guard> highLevelGuards = new ArrayList<>();
for (Guard guard : allGuards) {
    if (guard.level() >= 5) {
        highLevelGuards.add(guard);
    }
}

// ✅ Good - declarative stream
List<Guard> highLevelGuards = allGuards.stream()
    .filter(g -> g.level() >= 5)
    .toList();

// Complex example
int totalPower = guards.stream()
    .filter(g -> g.type() == GuardType.ARCHER)
    .mapToInt(Guard::attackPower)
    .sum();
```

### Try-with-Resources
```java
// ✅ Good - auto-close resources
try (var reader = Files.newBufferedReader(path);
     var writer = Files.newBufferedWriter(outputPath)) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line);
    }
}
// reader and writer auto-closed
```

### Factory Methods for Collections
```java
// Immutable collections
List<String> names = List.of("Steve", "Alex", "Creeper");
Set<Integer> ids = Set.of(1, 2, 3, 4, 5);
Map<String, Integer> levels = Map.of(
    "Steve", 10,
    "Alex", 8
);

// Mutable collections (if needed)
List<String> mutableNames = new ArrayList<>(names);
```

## Best Practices

### Naming Conventions
- **Classes**: `PascalCase` (e.g., `GuardManager`)
- **Methods**: `camelCase` (e.g., `attackEnemy()`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `MAX_GUARD_LEVEL`)
- **Packages**: `lowercase` (e.g., `com.xeenaa.guards`)
- **Interfaces**: Avoid "I" prefix (use `Guard` not `IGuard`)

### Method Design
- **Small methods**: 10-20 lines max
- **Single responsibility**: One clear purpose
- **Clear names**: Method name describes what it does
- **Avoid side effects**: Pure functions when possible

### SOLID Principles

**Single Responsibility**:
```java
// ❌ Bad - class does too much
class Guard {
    void attack() { }
    void defend() { }
    void saveToDatabase() { } // Different responsibility!
}

// ✅ Good - separate concerns
class Guard {
    void attack() { }
    void defend() { }
}

class GuardRepository {
    void save(Guard guard) { }
}
```

**Open/Closed**:
```java
// ✅ Good - open for extension, closed for modification
interface AttackStrategy {
    int calculateDamage(Guard guard);
}

class MeleeAttackStrategy implements AttackStrategy { }
class RangedAttackStrategy implements AttackStrategy { }
```

**Dependency Injection**:
```java
// ✅ Good - inject dependencies
class GuardManager {
    private final GuardRepository repository;

    public GuardManager(GuardRepository repository) {
        this.repository = repository;
    }
}
```

### Error Handling
```java
// ✅ Good - specific exceptions
public Guard createGuard(String name) throws InvalidGuardNameException {
    if (name == null || name.isBlank()) {
        throw new InvalidGuardNameException("Name cannot be empty");
    }
    return new Guard(name);
}

// ✅ Good - validation
public void setLevel(int level) {
    if (level < 0 || level > MAX_LEVEL) {
        throw new IllegalArgumentException(
            "Level must be between 0 and " + MAX_LEVEL
        );
    }
    this.level = level;
}
```

### Documentation
```java
/**
 * Manages guard entities in the village defense system.
 *
 * <p>Guards can be hired, promoted, and assigned to patrol zones.
 * Each guard has a level, type, and equipment set.
 *
 * @see Guard
 * @see GuardType
 * @since 1.0.0
 */
public class GuardManager {

    /**
     * Creates a new guard with the specified name and type.
     *
     * @param name the guard's display name, must not be blank
     * @param type the guard's combat specialization
     * @return the newly created guard
     * @throws IllegalArgumentException if name is blank
     */
    public Guard createGuard(String name, GuardType type) {
        // implementation
    }
}
```

## Common Anti-Patterns to Avoid

### ❌ Primitive Obsession
```java
// Bad - using primitives everywhere
void processGuard(String name, int level, int type, int x, int y, int z)

// Good - use objects
void processGuard(Guard guard, Position position)
```

### ❌ God Objects
```java
// Bad - one class does everything
class GameManager {
    void handleGuards() { }
    void handleVillagers() { }
    void handleEconomy() { }
    void handleCombat() { }
    void handleGUI() { }
}

// Good - separate responsibilities
class GuardManager { }
class VillagerManager { }
class EconomyManager { }
```

### ❌ Magic Numbers
```java
// Bad
if (guard.level() > 10) { }

// Good
static final int MAX_GUARD_LEVEL = 10;
if (guard.level() > MAX_GUARD_LEVEL) { }
```

### ❌ Returning Null
```java
// Bad
public Guard findGuard(String name) {
    return map.get(name); // might be null
}

// Good
public Optional<Guard> findGuard(String name) {
    return Optional.ofNullable(map.get(name));
}
```

## When to Use This Skill

Use this skill when:
- Writing new Java code
- Refactoring existing code
- Reviewing code for Java best practices
- Questions about Java 21 features
- Questions about "What's the modern Java way to do X?"
- Implementing design patterns in Java
- Optimizing Java code performance
