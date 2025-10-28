---
name: logging-strategy
description: Proactive logging strategies including when to log, what to log, log levels, and structured logging. Use when implementing features to add defensive logging automatically, not waiting for bugs to occur.
allowed-tools: [Read, Write, Edit, Grep, Glob]
---

# Logging Strategy Skill

Proactive logging strategies to add comprehensive logging during implementation, not after bugs occur.

## The Logging Problem

**Common Scenario**:
1. Implement feature without logging
2. Bug occurs in production
3. User: "Add logging so we can debug"
4. Developer adds logging
5. Bug happens again - now we can debug
6. **Problem**: Should have had logging from the start!

**Root Cause**: Logging added reactively (after bugs) instead of proactively (during implementation).

## Proactive Logging Strategy

### CRITICAL RULE: Log During Implementation

**❌ Reactive Logging** (add logging after bug):
```java
public void hireGuard(PlayerEntity player, GuardType type) {
    int cost = getGuardCost(type);
    deductEmeralds(player, cost);
    spawnGuard(player, type);
    // Bug occurs - no logging to debug!
}

// After bug discovered:
// "Can you add logging?"
```

**✅ Proactive Logging** (add logging during implementation):
```java
public void hireGuard(PlayerEntity player, GuardType type) {
    LOGGER.info("Player {} attempting to hire {} guard",
        player.getName().getString(), type);

    int cost = getGuardCost(type);
    LOGGER.debug("Guard cost calculated: {} emeralds", cost);

    if (!hasEmeralds(player, cost)) {
        LOGGER.warn("Player {} has insufficient emeralds (need {}, has {})",
            player.getName().getString(), cost, countEmeralds(player));
        throw new InsufficientFundsException();
    }

    deductEmeralds(player, cost);
    LOGGER.debug("Deducted {} emeralds from {}", cost, player.getName().getString());

    Guard guard = spawnGuard(player, type);
    LOGGER.info("Successfully hired {} guard for player {} (ID: {})",
        type, player.getName().getString(), guard.getUuid());
}
```

## Log Levels

### SLF4J/Log4j Levels (Standard in Minecraft Mods)

#### ERROR - Critical failures that prevent functionality
**When**: Unrecoverable errors, data corruption, crashes prevented
```java
LOGGER.error("Failed to load guard data from disk", exception);
LOGGER.error("Critical: Guard registry corrupted, {} guards lost", count);
LOGGER.error("Cannot sync guard data to client {}", playerName, exception);
```

**Characteristics**:
- Something is broken and needs immediate attention
- Functionality is impaired or lost
- Usually includes exception/stack trace
- Admin should investigate immediately

#### WARN - Recoverable issues, unexpected states
**When**: Degraded functionality, fallback used, suspicious behavior
```java
LOGGER.warn("Guard {} has invalid data, using defaults", guardId);
LOGGER.warn("Player {} attempted unauthorized guard action", playerName);
LOGGER.warn("Guard pathfinding failed, retrying with fallback");
LOGGER.warn("Emerald transaction validation failed, rolling back");
```

**Characteristics**:
- Something unusual happened but functionality continues
- Potential issue that might become error
- Fallback or recovery mechanism used
- Should be investigated but not urgent

#### INFO - Important state changes, lifecycle events
**When**: Feature usage, major operations, state transitions
```java
LOGGER.info("Guard {} hired by player {}", guardType, playerName);
LOGGER.info("Guard {} promoted to level {}", guardId, newLevel);
LOGGER.info("Village defense activated, {} guards deployed", count);
LOGGER.info("Guard data saved for {} guards", guardCount);
```

**Characteristics**:
- Documents important events
- Useful for understanding system behavior
- Enables audit trail
- Production-friendly (not too verbose)

#### DEBUG - Detailed operation flow, intermediate states
**When**: Development, troubleshooting, detailed flow tracking
```java
LOGGER.debug("Calculating guard cost for type {}", type);
LOGGER.debug("Player {} has {} emeralds", playerName, emeraldCount);
LOGGER.debug("Guard AI tick: state={}, target={}", state, targetId);
LOGGER.debug("Pathfinding: path length={}, cost={}", pathLength, cost);
```

**Characteristics**:
- Detailed execution flow
- Intermediate values and calculations
- Usually disabled in production
- Enables step-by-step debugging

#### TRACE - Very detailed, granular execution
**When**: Deep debugging, performance profiling (rarely used)
```java
LOGGER.trace("Entering method hireGuard with player={}, type={}", player, type);
LOGGER.trace("Loop iteration {}: processing guard {}", i, guardId);
LOGGER.trace("Cache hit for guard type {}", type);
```

**Characteristics**:
- Extremely detailed
- Performance impact
- Usually disabled even in development
- Only for deep debugging sessions

## When to Add Logging

### 1. Entry Points (Always Log)
**Every public method that external code calls**:
```java
public void hireGuard(PlayerEntity player, GuardType type) {
    LOGGER.info("Hiring guard: player={}, type={}", player.getName(), type);
    // ... implementation
}

public void upgradeGuard(Guard guard, int newLevel) {
    LOGGER.info("Upgrading guard: id={}, currentLevel={}, newLevel={}",
        guard.getUuid(), guard.getLevel(), newLevel);
    // ... implementation
}
```

**Why**: Entry points are where features begin - need to know they were called.

### 2. Error Conditions (Always Log)
**Every exception, validation failure, error state**:
```java
if (!hasPermission(player, Permission.HIRE_GUARD)) {
    LOGGER.warn("Player {} attempted to hire guard without permission",
        player.getName().getString());
    throw new PermissionException();
}

try {
    saveGuardData(guard);
} catch (IOException e) {
    LOGGER.error("Failed to save guard data for guard {}",
        guard.getUuid(), e);
    throw new GuardSaveException("Cannot save guard", e);
}
```

**Why**: Need to know when and why things fail.

### 3. State Changes (Always Log)
**Every significant state transition**:
```java
public void setGuardLevel(Guard guard, int newLevel) {
    int oldLevel = guard.getLevel();
    guard.setLevel(newLevel);
    LOGGER.info("Guard {} level changed: {} -> {}",
        guard.getUuid(), oldLevel, newLevel);
}

public void assignGuardToPost(Guard guard, BlockPos post) {
    LOGGER.info("Guard {} assigned to post at {}",
        guard.getUuid(), post);
    guard.setAssignedPost(post);
}
```

**Why**: State changes are critical to understanding system behavior.

### 4. External Interactions (Always Log)
**Network packets, file I/O, database, external systems**:
```java
public void sendGuardHirePacket(ServerPlayerEntity player, Guard guard) {
    LOGGER.debug("Sending guard hire packet to player {}: guardId={}",
        player.getName().getString(), guard.getUuid());
    ServerPlayNetworking.send(player, new GuardHirePayload(guard.getUuid()));
}

public void loadGuardData(UUID guardId) {
    LOGGER.debug("Loading guard data from disk: guardId={}", guardId);
    try {
        GuardData data = diskStorage.read(guardId);
        LOGGER.info("Loaded guard data: guardId={}, level={}",
            guardId, data.level());
    } catch (IOException e) {
        LOGGER.error("Failed to load guard data for {}", guardId, e);
    }
}
```

**Why**: External interactions are common failure points.

### 5. Performance-Critical Paths (Add Timing)
**Expensive operations, loops, pathfinding**:
```java
public void calculateGuardPaths() {
    long startTime = System.currentTimeMillis();
    int guardCount = guards.size();

    LOGGER.debug("Calculating paths for {} guards", guardCount);

    for (Guard guard : guards) {
        guard.recalculatePath();
    }

    long duration = System.currentTimeMillis() - startTime;
    LOGGER.info("Path calculation complete: {} guards in {}ms ({} ms/guard)",
        guardCount, duration, duration / guardCount);

    if (duration > 50) {
        LOGGER.warn("Path calculation took {}ms (> 50ms tick budget!)", duration);
    }
}
```

**Why**: Performance issues need measurements to diagnose.

### 6. Business Logic Decisions (Log Why)
**Important decisions, branching logic**:
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

**Why**: Understanding why system made a decision helps debug logic errors.

### 7. Data Transformations (Log Before/After)
**Conversions, calculations, data processing**:
```java
public int calculateUpgradeCost(int currentLevel, int targetLevel) {
    LOGGER.debug("Calculating upgrade cost: currentLevel={}, targetLevel={}",
        currentLevel, targetLevel);

    int cost = 0;
    for (int level = currentLevel + 1; level <= targetLevel; level++) {
        int levelCost = level * 25;
        cost += levelCost;
        LOGGER.trace("Level {} cost: {}, total: {}", level, levelCost, cost);
    }

    LOGGER.debug("Total upgrade cost: {} emeralds ({} -> {})",
        cost, currentLevel, targetLevel);
    return cost;
}
```

**Why**: Calculations can be wrong - logging shows the math.

## Structured Logging

### Use Parameterized Messages
```java
// ❌ BAD - string concatenation
LOGGER.info("Player " + player + " hired " + type + " guard");

// ✅ GOOD - parameterized (better performance, safer)
LOGGER.info("Player {} hired {} guard", player, type);

// ✅ GOOD - multiple parameters
LOGGER.info("Guard hired: player={}, type={}, cost={}, level={}",
    playerName, type, cost, level);
```

**Why**:
- No string concat if logging disabled (performance)
- Automatic null handling
- Easier to parse logs
- Consistent formatting

### Include Context
```java
// ❌ BAD - vague, no context
LOGGER.error("Failed to save");

// ✅ GOOD - specific, includes IDs and context
LOGGER.error("Failed to save guard data: guardId={}, playerId={}",
    guard.getUuid(), player.getUuid(), exception);

// ✅ GOOD - actionable information
LOGGER.warn("Emerald transaction failed: player={}, amount={}, balance={}, reason={}",
    playerName, amount, currentBalance, reason);
```

**Why**: Context-free logs are useless for debugging.

### Log IDs, Not Objects
```java
// ❌ BAD - logs object toString (unreliable)
LOGGER.info("Processing guard: {}", guard);

// ✅ GOOD - logs specific identifiable properties
LOGGER.info("Processing guard: id={}, type={}, level={}",
    guard.getUuid(), guard.getType(), guard.getLevel());
```

**Why**: Object toString might change, break, or be unhelpful.

## Logging Patterns

### Pattern 1: Method Boundary Logging
```java
public Guard hireGuard(PlayerEntity player, GuardType type) {
    LOGGER.info("hireGuard called: player={}, type={}", player.getName(), type);

    try {
        // ... implementation
        Guard guard = createAndSpawnGuard(player, type);

        LOGGER.info("hireGuard success: guardId={}", guard.getUuid());
        return guard;
    } catch (Exception e) {
        LOGGER.error("hireGuard failed: player={}, type={}",
            player.getName(), type, e);
        throw e;
    }
}
```

### Pattern 2: State Transition Logging
```java
public void changeGuardState(Guard guard, GuardState newState) {
    GuardState oldState = guard.getState();

    LOGGER.info("Guard state transition: id={}, {} -> {}",
        guard.getUuid(), oldState, newState);

    guard.setState(newState);

    // Log side effects
    if (newState == GuardState.COMBAT) {
        LOGGER.debug("Guard {} entering combat mode, target={}",
            guard.getUuid(), guard.getTarget());
    }
}
```

### Pattern 3: Loop/Batch Logging
```java
public void processGuards(List<Guard> guards) {
    LOGGER.info("Processing {} guards", guards.size());

    int processed = 0;
    int errors = 0;

    for (Guard guard : guards) {
        try {
            processGuard(guard);
            processed++;

            if (processed % 100 == 0) {
                LOGGER.debug("Progress: {}/{} guards processed", processed, guards.size());
            }
        } catch (Exception e) {
            errors++;
            LOGGER.warn("Failed to process guard {}", guard.getUuid(), e);
        }
    }

    LOGGER.info("Guard processing complete: success={}, errors={}, total={}",
        processed, errors, guards.size());
}
```

### Pattern 4: Network Packet Logging
```java
public void handleGuardHirePacket(GuardHirePayload payload, ServerPlayNetworking.Context context) {
    ServerPlayerEntity player = context.player();

    LOGGER.debug("Received guard hire packet: player={}, guardType={}",
        player.getName().getString(), payload.type());

    context.server().execute(() -> {
        try {
            Guard guard = hireGuard(player, payload.type());

            LOGGER.info("Guard hire packet processed: player={}, guardId={}",
                player.getName().getString(), guard.getUuid());

            // Send confirmation
            ServerPlayNetworking.send(player, new GuardHireConfirmPayload(guard.getUuid()));
            LOGGER.debug("Sent hire confirmation to player {}", player.getName());

        } catch (Exception e) {
            LOGGER.error("Failed to process guard hire packet: player={}, type={}",
                player.getName().getString(), payload.type(), e);

            // Send error
            ServerPlayNetworking.send(player, new GuardHireErrorPayload(e.getMessage()));
        }
    });
}
```

### Pattern 5: Performance Measurement
```java
public void performExpensiveOperation() {
    long startTime = System.nanoTime();

    LOGGER.debug("Starting expensive operation");

    try {
        // ... expensive operation
        int itemsProcessed = doWork();

        long durationNs = System.nanoTime() - startTime;
        long durationMs = durationNs / 1_000_000;

        LOGGER.info("Expensive operation complete: duration={}ms, items={}",
            durationMs, itemsProcessed);

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

### Logger Naming Convention
```java
// ✅ GOOD - class-based logger (standard pattern)
private static final Logger LOGGER = LoggerFactory.getLogger(GuardManager.class);

// ✅ GOOD - named logger for specific subsystems
private static final Logger NETWORK_LOGGER = LoggerFactory.getLogger("mymod.network");
private static final Logger COMBAT_LOGGER = LoggerFactory.getLogger("mymod.combat");
```

## Common Logging Mistakes

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

### ❌ Logging Sensitive Data
```java
// BAD - logs password/token
LOGGER.info("Login: username={}, password={}", username, password);

// GOOD - don't log sensitive data
LOGGER.info("Login: username={}", username);
```

### ❌ Excessive DEBUG Logging
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
    // No logging in hot path
    for (Guard guard : guards) {
        guard.tick();
    }

    // Only log problems
    if (guards.size() > 100) {
        LOGGER.warn("High guard count: {}", guards.size());
    }
}
```

### ❌ Not Checking isDebugEnabled
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

## Integration with Agents

### implementation-agent Responsibilities

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
- implementation-agent to add logging during testing phase

### implementation-agent Testing Responsibilities

**When writing tests**:
1. ✅ Verify logging occurs at appropriate points
2. ✅ Test that errors are logged
3. ✅ Check log levels are appropriate
4. ❌ Don't write tests that depend on specific log messages (brittle)

## When to Use This Skill

Use this skill when:
- **Implementing new features** - Add logging during implementation
- **Fixing bugs** - Ensure logging exists to prevent next occurrence
- **Code reviews** - Check for adequate logging
- **implementation-agent writing code** - Proactive logging strategy
- **Debugging issues** - Wish you had logging? Add it now!
- **Performance optimization** - Add timing measurements
- **Questions about "what should I log?"**
- **Questions about "when should I log?"**

## Key Principles

1. **Log Proactively**: Add logging during implementation, not after bugs
2. **Log Entry Points**: Every feature invocation should be logged
3. **Log Errors Always**: Never silently catch exceptions
4. **Log State Changes**: Track how system evolves
5. **Context is Critical**: Include IDs, values, state - not just "failed"
6. **Right Level**: ERROR for failures, WARN for unexpected, INFO for events, DEBUG for flow
7. **Performance Aware**: Don't log in hot paths, check isDebugEnabled for expensive operations
8. **Structured Format**: Use parameterized messages, log IDs not objects
