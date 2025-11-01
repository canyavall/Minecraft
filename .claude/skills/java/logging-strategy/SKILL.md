---
name: logging-strategy
description: Proactive logging strategies including when to log, what to log, log levels, and structured logging. Use when implementing features to add defensive logging automatically, not waiting for bugs to occur.
allowed-tools: [Read, Write, Edit, Grep, Glob]
---

# Logging Strategy Skill

Proactive logging strategies to add comprehensive logging during implementation, not after bugs occur.

## CRITICAL RULE: Log During Implementation

**❌ Reactive Logging** (add logging after bug occurs):
- Implement feature without logging
- Bug occurs → add logging to debug
- **Problem**: Should have had logging from the start!

**✅ Proactive Logging** (add logging during implementation):
```java
public void hireGuard(PlayerEntity player, GuardType type) {
    LOGGER.info("Player {} attempting to hire {} guard", player.getName().getString(), type);

    int cost = getGuardCost(type);
    LOGGER.debug("Guard cost calculated: {} emeralds", cost);

    if (!hasEmeralds(player, cost)) {
        LOGGER.warn("Player {} has insufficient emeralds (need {}, has {})",
            player.getName().getString(), cost, countEmeralds(player));
        throw new InsufficientFundsException();
    }

    deductEmeralds(player, cost);
    Guard guard = spawnGuard(player, type);
    LOGGER.info("Successfully hired {} guard for player {} (ID: {})",
        type, player.getName().getString(), guard.getUuid());
}
```

## Log Levels (SLF4J/Log4j)

### ERROR - Critical failures
**When**: Unrecoverable errors, data corruption, crashes prevented
```java
LOGGER.error("Failed to load guard data from disk", exception);
LOGGER.error("Cannot sync guard data to client {}", playerName, exception);
```

### WARN - Recoverable issues, unexpected states
**When**: Degraded functionality, fallback used, suspicious behavior
```java
LOGGER.warn("Guard {} has invalid data, using defaults", guardId);
LOGGER.warn("Player {} attempted unauthorized action", playerName);
```

### INFO - Important state changes, lifecycle events
**When**: Feature usage, major operations, state transitions
```java
LOGGER.info("Guard {} hired by player {}", guardType, playerName);
LOGGER.info("Guard {} promoted to level {}", guardId, newLevel);
```

### DEBUG - Detailed operation flow
**When**: Development, troubleshooting, detailed tracking
```java
LOGGER.debug("Calculating guard cost for type {}", type);
LOGGER.debug("Player {} has {} emeralds", playerName, emeraldCount);
```

### TRACE - Very detailed execution
**When**: Deep debugging (rarely used, performance impact)
```java
LOGGER.trace("Entering method hireGuard with player={}, type={}", player, type);
```

## When to Add Logging

### 1. Entry Points (Always)
Every public method that external code calls:
```java
public void hireGuard(PlayerEntity player, GuardType type) {
    LOGGER.info("Hiring guard: player={}, type={}", player.getName(), type);
    // ... implementation
}
```

### 2. Error Conditions (Always)
Every exception, validation failure, error state:
```java
if (!hasPermission(player, Permission.HIRE_GUARD)) {
    LOGGER.warn("Player {} attempted to hire guard without permission", player.getName());
    throw new PermissionException();
}

try {
    saveGuardData(guard);
} catch (IOException e) {
    LOGGER.error("Failed to save guard data for guard {}", guard.getUuid(), e);
    throw new GuardSaveException("Cannot save guard", e);
}
```

### 3. State Changes (Always)
Every significant state transition:
```java
public void setGuardLevel(Guard guard, int newLevel) {
    int oldLevel = guard.getLevel();
    guard.setLevel(newLevel);
    LOGGER.info("Guard {} level changed: {} -> {}", guard.getUuid(), oldLevel, newLevel);
}
```

### 4. External Interactions (Always)
Network packets, file I/O, database:
```java
public void sendGuardHirePacket(ServerPlayerEntity player, Guard guard) {
    LOGGER.debug("Sending guard hire packet to player {}: guardId={}",
        player.getName().getString(), guard.getUuid());
    ServerPlayNetworking.send(player, new GuardHirePayload(guard.getUuid()));
}
```

### 5. Performance-Critical Paths (Add Timing)
Expensive operations:
```java
public void calculateGuardPaths() {
    long startTime = System.currentTimeMillis();
    LOGGER.debug("Calculating paths for {} guards", guards.size());

    for (Guard guard : guards) {
        guard.recalculatePath();
    }

    long duration = System.currentTimeMillis() - startTime;
    LOGGER.info("Path calculation complete: {} guards in {}ms", guards.size(), duration);

    if (duration > 50) {
        LOGGER.warn("Path calculation took {}ms (> 50ms tick budget!)", duration);
    }
}
```

### 6. Business Logic Decisions (Log Why)
Important decisions, branching logic:
```java
public GuardType selectGuardType(PlayerEntity player) {
    int emeralds = countEmeralds(player);

    if (emeralds >= 100) {
        LOGGER.debug("Player {} has {} emeralds, suggesting MAGE", playerName, emeralds);
        return GuardType.MAGE;
    } else if (emeralds >= 75) {
        LOGGER.debug("Player {} has {} emeralds, suggesting SWORDSMAN", playerName, emeralds);
        return GuardType.SWORDSMAN;
    } else {
        LOGGER.debug("Player {} has {} emeralds, suggesting ARCHER", playerName, emeralds);
        return GuardType.ARCHER;
    }
}
```

### 7. Data Transformations (Log Before/After)
Conversions, calculations:
```java
public int calculateUpgradeCost(int currentLevel, int targetLevel) {
    LOGGER.debug("Calculating upgrade cost: currentLevel={}, targetLevel={}",
        currentLevel, targetLevel);

    int cost = 0;
    for (int level = currentLevel + 1; level <= targetLevel; level++) {
        cost += level * 25;
    }

    LOGGER.debug("Total upgrade cost: {} emeralds ({} -> {})", cost, currentLevel, targetLevel);
    return cost;
}
```

## Structured Logging Best Practices

### Use Parameterized Messages
```java
// ❌ BAD - string concatenation
LOGGER.info("Player " + player + " hired " + type + " guard");

// ✅ GOOD - parameterized (better performance, safer)
LOGGER.info("Player {} hired {} guard", player, type);
```

**Why**: No string concat if logging disabled, automatic null handling, easier to parse

### Include Context
```java
// ❌ BAD - vague, no context
LOGGER.error("Failed to save");

// ✅ GOOD - specific, includes IDs and context
LOGGER.error("Failed to save guard data: guardId={}, playerId={}",
    guard.getUuid(), player.getUuid(), exception);
```

### Log IDs, Not Objects
```java
// ❌ BAD - logs object toString (unreliable)
LOGGER.info("Processing guard: {}", guard);

// ✅ GOOD - logs specific identifiable properties
LOGGER.info("Processing guard: id={}, type={}, level={}",
    guard.getUuid(), guard.getType(), guard.getLevel());
```

## Common Logging Patterns

### Pattern 1: Method Boundary Logging
```java
public Guard hireGuard(PlayerEntity player, GuardType type) {
    LOGGER.info("hireGuard called: player={}, type={}", player.getName(), type);

    try {
        Guard guard = createAndSpawnGuard(player, type);
        LOGGER.info("hireGuard success: guardId={}", guard.getUuid());
        return guard;
    } catch (Exception e) {
        LOGGER.error("hireGuard failed: player={}, type={}", player.getName(), type, e);
        throw e;
    }
}
```

### Pattern 2: State Transition Logging
```java
public void changeGuardState(Guard guard, GuardState newState) {
    GuardState oldState = guard.getState();
    LOGGER.info("Guard state transition: id={}, {} -> {}", guard.getUuid(), oldState, newState);
    guard.setState(newState);
}
```

### Pattern 3: Loop/Batch Logging
```java
public void processGuards(List<Guard> guards) {
    LOGGER.info("Processing {} guards", guards.size());

    int processed = 0, errors = 0;
    for (Guard guard : guards) {
        try {
            processGuard(guard);
            processed++;
        } catch (Exception e) {
            errors++;
            LOGGER.warn("Failed to process guard {}", guard.getUuid(), e);
        }
    }

    LOGGER.info("Guard processing complete: success={}, errors={}, total={}",
        processed, errors, guards.size());
}
```

### Pattern 4: Performance Measurement
```java
public void performExpensiveOperation() {
    long startTime = System.nanoTime();

    try {
        int itemsProcessed = doWork();
        long durationMs = (System.nanoTime() - startTime) / 1_000_000;

        LOGGER.info("Expensive operation complete: duration={}ms, items={}", durationMs, itemsProcessed);

        if (durationMs > 50) {
            LOGGER.warn("Operation exceeded tick budget: {}ms > 50ms", durationMs);
        }
    } catch (Exception e) {
        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.error("Expensive operation failed after {}ms", durationMs, e);
        throw e;
    }
}
```

## Logger Setup

### Standard Logger Declaration
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuardManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardManager.class);
    // ... methods use LOGGER
}
```

### Named Loggers for Subsystems
```java
// Class-based logger (standard)
private static final Logger LOGGER = LoggerFactory.getLogger(GuardManager.class);

// Named logger for specific subsystems
private static final Logger NETWORK_LOGGER = LoggerFactory.getLogger("mymod.network");
private static final Logger COMBAT_LOGGER = LoggerFactory.getLogger("mymod.combat");
```

## Common Mistakes to Avoid

### ❌ Missing Exception in Error Logs
```java
// BAD - exception not logged
catch (IOException e) {
    LOGGER.error("Failed to save guard");  // Where's the stack trace?!
}

// GOOD - exception included
catch (IOException e) {
    LOGGER.error("Failed to save guard data", e);
}
```

### ❌ Excessive DEBUG Logging in Hot Paths
```java
// BAD - debug logs in hot path (called every tick)
public void tick() {
    LOGGER.debug("Tick!");  // Spam!
    for (Guard guard : guards) {
        LOGGER.debug("Guard {} ticking", guard);  // More spam!
        guard.tick();
    }
}

// GOOD - info/warn only for frequent operations
public void tick() {
    for (Guard guard : guards) {
        guard.tick();  // No logging
    }

    if (guards.size() > 100) {
        LOGGER.warn("High guard count: {}", guards.size());  // Only problems
    }
}
```

### ❌ Not Checking isDebugEnabled for Expensive Operations
```java
// BAD - expensive string building even if debug disabled
LOGGER.debug("Guard state: " + buildExpensiveDebugString(guard));

// GOOD - check before expensive operations
if (LOGGER.isDebugEnabled()) {
    LOGGER.debug("Guard state: {}", buildExpensiveDebugString(guard));
}
```

## Logging Checklist for Implementation

When implementing a new feature, ensure logging for:

- [ ] **Entry point** - Feature invocation logged at INFO level
- [ ] **Input validation failures** - Logged at WARN with invalid values
- [ ] **State changes** - All significant state transitions at INFO
- [ ] **External calls** - Network/disk/API calls at DEBUG
- [ ] **Error conditions** - All exceptions at ERROR with stack trace
- [ ] **Success outcomes** - Operation completion at INFO
- [ ] **Performance measurements** - For expensive operations
- [ ] **Business logic decisions** - Why system chose path A vs B
- [ ] **Data transformations** - Input and output of calculations

## Integration with implementation-agent

**During implementation**:
1. ✅ Add logger declaration to every class
2. ✅ Log at entry points (public methods)
3. ✅ Log all error conditions with exceptions
4. ✅ Log state changes
5. ✅ Log external interactions
6. ✅ Add performance measurements
7. ✅ Use appropriate log levels

**DO NOT wait for**:
- User to request logging
- Bug to occur
- Testing phase to add logging

## Key Principles

1. **Log Proactively**: Add logging during implementation, not after bugs
2. **Log Entry Points**: Every feature invocation should be logged
3. **Log Errors Always**: Never silently catch exceptions
4. **Log State Changes**: Track how system evolves
5. **Context is Critical**: Include IDs, values, state - not just "failed"
6. **Right Level**: ERROR for failures, WARN for unexpected, INFO for events, DEBUG for flow
7. **Performance Aware**: Don't log in hot paths, check isDebugEnabled for expensive operations
8. **Structured Format**: Use parameterized messages, log IDs not objects

---

## Usage Tracking

**When using this skill**, append one line to `.claude/tracker/skill.md`:

```
[YYYY-MM-DD HH:MM:SS] [your-agent-name] used logging-strategy
```

**Example**: `[2025-11-01 15:30:00] implementation-agent used logging-strategy`

This helps track which skills are actively consulted and identifies documentation gaps.
