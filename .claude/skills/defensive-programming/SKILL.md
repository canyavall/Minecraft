---
name: defensive-programming
description: Regression testing strategies, test coverage requirements, and defensive coding to prevent breaking existing functionality when adding new features. Use when implementing new features, writing tests, or modifying existing code to ensure regressions don't occur.
allowed-tools: [Read, Write, Edit, Grep, Glob]
---

# Defensive Programming Skill

Strategies to prevent regression bugs and protect existing functionality when adding new features.

## The Regression Problem

**Common Scenario**:
1. Feature A works perfectly
2. Implement Feature B
3. Feature A breaks (regression!)
4. User discovers the bug
5. Create fix task, waste time debugging

**Root Cause**: Tests don't adequately protect existing functionality.

## Regression Prevention Strategy

### 1. Test-First Development

**CRITICAL RULE**: Before modifying existing code, ensure tests exist for current behavior.

#### Before Modifying Code
```java
// ❌ BAD - modify without tests
public void addNewFeature() {
    // Change existing logic
    existingMethod(); // Might break!
    newLogic();
}

// ✅ GOOD - test existing behavior first
@Test
public void testExistingBehaviorBeforeChanges() {
    // Document current behavior with test
    Guard guard = createGuard("Steve", GuardType.ARCHER);
    assertEquals(5, guard.getAttackPower());
    // Now safe to modify - test will catch regressions
}
```

#### Workflow
1. **Identify** code that will be modified
2. **Write test** for current behavior (if missing)
3. **Verify** test passes (proves current behavior works)
4. **Modify** code to add new feature
5. **Verify** old test still passes (no regression)
6. **Add** new test for new feature

### 2. Comprehensive Test Coverage

**Minimum Requirements**:

#### Feature-Level Tests
**Every feature must have tests covering**:
- Happy path (normal use case)
- Edge cases (boundary conditions)
- Error cases (invalid input, failures)
- Integration points (interactions with other features)

#### Example: Guard Hiring Feature
```java
public class GuardHiringTests {
    // Happy path
    @Test
    public void testHireGuardWithSufficientEmeralds() {
        // Player has 50 emeralds, can hire archer (costs 50)
    }

    // Edge case: Exact amount
    @Test
    public void testHireGuardWithExactAmount() {
        // Player has exactly 50 emeralds
    }

    // Edge case: Insufficient emeralds
    @Test
    public void testHireGuardWithInsufficientEmeralds() {
        // Player has 49 emeralds, hiring should fail
    }

    // Edge case: No emeralds
    @Test
    public void testHireGuardWithZeroEmeralds() {
        // Player has 0 emeralds
    }

    // Error case: Invalid guard type
    @Test
    public void testHireGuardWithInvalidType() {
        // Null or invalid guard type
    }

    // Integration: Multiple guards
    @Test
    public void testHireMultipleGuardsDeductsCorrectAmount() {
        // Hire archer (50e), then swordsman (75e)
        // Should have original - 125
    }

    // Integration: Server sync
    @Test
    public void testHiredGuardSyncsToClient() {
        // Verify guard appears on client after hiring
    }

    // Regression: Existing guards
    @Test
    public void testExistingGuardsUnaffectedByNewHire() {
        // Hire guard, verify existing guards still work
    }
}
```

### 3. Regression Test Suite

**Dedicated Regression Tests**: Tests specifically to prevent known bugs from reoccurring.

#### When a Bug is Fixed
1. **Write test** that reproduces the bug
2. **Verify test fails** (proves bug exists)
3. **Fix the bug**
4. **Verify test passes** (proves fix works)
5. **Keep test** in regression suite forever

#### Example: Regression Test
```java
/**
 * Regression test for bug where guards lost armor on server restart.
 * Fixed in TASK-004.1
 */
@Test
public void testGuardArmorPersistsAfterServerRestart() {
    // Create guard with diamond armor
    Guard guard = createGuard("Steve", GuardType.SWORDSMAN);
    guard.equipArmor(Items.DIAMOND_CHESTPLATE);

    // Save and reload (simulate server restart)
    CompoundTag nbt = new CompoundTag();
    guard.writeNbt(nbt);
    Guard loadedGuard = createGuard("Steve", GuardType.SWORDSMAN);
    loadedGuard.readNbt(nbt);

    // Verify armor persisted
    assertEquals(Items.DIAMOND_CHESTPLATE,
        loadedGuard.getEquippedStack(EquipmentSlot.CHEST).getItem());
}
```

### 4. Test Organization

#### File Structure
```
src/test/java/
├── integration/          # Integration tests (multiple systems)
│   ├── GuardCombatIntegrationTest.java
│   └── VillageDefenseIntegrationTest.java
├── regression/           # Regression tests (prevent known bugs)
│   ├── GuardArmorPersistenceTest.java
│   └── GuardHiringDesyncTest.java
└── unit/                 # Unit tests (single class/method)
    ├── GuardEntityTest.java
    └── GuardRegistryTest.java
```

#### Test Naming Convention
```java
// Pattern: test[Action][Condition][ExpectedResult]
@Test
public void testHireGuard_WithSufficientEmeralds_Success() { }

@Test
public void testHireGuard_WithInsufficientEmeralds_Fails() { }

@Test
public void testGuardAttack_WhenTargetDies_StopsAttacking() { }
```

### 5. Change Impact Analysis

**Before Modifying Code**: Ask these questions:

#### What Could Break?
- What features use this code?
- What classes depend on this?
- What assumptions might change?
- What edge cases could be affected?

#### Example Analysis
```java
// Modifying this method:
public int getGuardCost(GuardType type) {
    return switch (type) {
        case ARCHER -> 50;
        case SWORDSMAN -> 75;
        case MAGE -> 100;
    };
}

// Impact analysis:
// 1. Guard hiring system uses this (test hiring)
// 2. Economy balance depends on these values (test economy)
// 3. Upgrade system might use this (test upgrades)
// 4. Admin commands might reference this (test commands)

// Action: Add tests for all these before changing!
```

### 6. Integration Testing

**Test Feature Interactions**: New features often break old ones through interactions.

#### Critical Integration Points
```java
@Test
public void testNewFeatureDoesNotBreakExisting() {
    // Setup: Create existing feature state
    Guard existingGuard = createAndHireGuard("Steve", GuardType.ARCHER);

    // Action: Use new feature
    upgradeGuard(existingGuard, 2);

    // Verify: Existing functionality still works
    assertTrue(existingGuard.canAttack());
    assertEquals(GuardType.ARCHER, existingGuard.getType());
    assertNotNull(existingGuard.getOwner());
}
```

### 7. Test Coverage Requirements

**Minimum Coverage Targets**:
- **Critical paths**: 100% (hiring, combat, persistence)
- **Business logic**: 90%+ (costs, upgrades, balancing)
- **Integration points**: 80%+ (networking, GUI, data sync)
- **Utility code**: 70%+ (helpers, formatters)

**Coverage Tools**:
- JaCoCo for Java coverage
- IDE coverage runners
- Gradle test reports

### 8. Continuous Testing

**Test Frequency**:
- ✅ **Before committing**: Run all tests
- ✅ **After modifying**: Run affected tests
- ✅ **Before implementing new feature**: Run regression suite
- ✅ **After bug fix**: Run regression suite

**Automation**:
```gradle
// In build.gradle
test {
    // Fail build if coverage drops
    finalizedBy jacocoTestReport

    // Run all tests
    useJUnitPlatform()
}

jacocoTestReport {
    // Generate coverage report
    reports {
        xml.enabled true
        html.enabled true
    }
}
```

## Defensive Coding Patterns

### Input Validation
```java
// ✅ GOOD - validate everything
public void hireGuard(PlayerEntity player, GuardType type) {
    // Validate inputs
    if (player == null) {
        throw new IllegalArgumentException("Player cannot be null");
    }
    if (type == null) {
        throw new IllegalArgumentException("GuardType cannot be null");
    }

    // Defensive checks
    if (player.getWorld().isClient()) {
        throw new IllegalStateException("Cannot hire guard on client");
    }

    // Business logic
    int cost = getGuardCost(type);
    if (!hasEmeralds(player, cost)) {
        throw new InsufficientFundsException("Need " + cost + " emeralds");
    }

    // Execute
    deductEmeralds(player, cost);
    spawnGuard(player, type);
}
```

### Null Safety
```java
// ❌ BAD - assume non-null
public void attackTarget(LivingEntity target) {
    target.damage(getDamage()); // NPE if target is null!
}

// ✅ GOOD - defensive null checks
public void attackTarget(LivingEntity target) {
    if (target == null) {
        LOGGER.warn("Attempted to attack null target");
        return;
    }
    if (target.isRemoved()) {
        LOGGER.debug("Target already removed, skipping attack");
        return;
    }
    target.damage(getDamage());
}
```

### Immutability
```java
// ❌ BAD - mutable state can break
public class GuardStats {
    public int level;
    public int damage;
}

// ✅ GOOD - immutable prevents modification bugs
public record GuardStats(int level, int damage) {
    public GuardStats {
        if (level < 0 || level > 10) {
            throw new IllegalArgumentException("Level must be 0-10");
        }
    }

    public GuardStats withLevel(int newLevel) {
        return new GuardStats(newLevel, this.damage);
    }
}
```

### Error Recovery
```java
// ✅ GOOD - handle failures gracefully
public void loadGuardData() {
    try {
        GuardData data = readFromDisk();
        applyGuardData(data);
    } catch (IOException e) {
        LOGGER.error("Failed to load guard data", e);
        // Fallback to defaults
        applyDefaultGuardData();
    } catch (Exception e) {
        LOGGER.error("Unexpected error loading guards", e);
        // Don't crash - recover gracefully
        notifyAdminOfError(e);
    }
}
```

## Testing Workflow Integration

### For implementation-agent

**When implementing new feature**:
1. ✅ **Before coding**: Write tests for existing code that will be modified
2. ✅ **During coding**: Write tests for new feature incrementally
3. ✅ **After coding**: Run all tests, ensure none broke
4. ✅ **Request user validation**: User tests manually
5. ⏸️ **STOP**: Wait for validation-agent to write comprehensive test suite

**When fixing bug**:
1. ✅ **Write test** that reproduces bug
2. ✅ **Verify test fails** (proves bug exists)
3. ✅ **Fix bug**
4. ✅ **Verify test passes** (proves fix works)
5. ✅ **Add to regression suite**
6. ✅ **Run all tests** (ensure fix didn't break anything)

### For validation-agent

**When writing test suite** (AFTER user validates manually):

1. ✅ **Read existing tests**: Understand what's already tested
2. ✅ **Identify gaps**: What's not tested?
3. ✅ **Write regression tests**: Protect existing features
4. ✅ **Write integration tests**: Test feature interactions
5. ✅ **Write edge case tests**: Boundary conditions
6. ✅ **Write error case tests**: Invalid inputs, failures
7. ✅ **Organize tests**: unit/, integration/, regression/
8. ✅ **Document coverage**: What's tested, what gaps remain

## Test Quality Standards

### Good Test Characteristics

**FIRST Principles**:
- **F**ast - Runs quickly (milliseconds, not seconds)
- **I**ndependent - No dependencies on other tests
- **R**epeatable - Same result every time
- **S**elf-validating - Clear pass/fail (no manual inspection)
- **T**imely - Written at right time (before/during implementation)

### Test Assertions
```java
// ❌ BAD - vague assertions
@Test
public void testGuardHiring() {
    assertTrue(result);  // What does this prove?
}

// ✅ GOOD - specific, descriptive assertions
@Test
public void testGuardHiring_WithSufficientEmeralds_CreatesGuardAndDeductsEmeralds() {
    // Arrange
    PlayerEntity player = createPlayerWithEmeralds(50);

    // Act
    Guard guard = hireGuard(player, GuardType.ARCHER);

    // Assert
    assertNotNull(guard, "Guard should be created");
    assertEquals(GuardType.ARCHER, guard.getType(),
        "Guard should be Archer type");
    assertEquals(0, countEmeralds(player),
        "Emeralds should be deducted (50 - 50 = 0)");
    assertEquals(player, guard.getOwner(),
        "Guard owner should be hiring player");
}
```

### Test Data Setup
```java
// ✅ GOOD - clear test data builders
public class GuardTestFixtures {
    public static Guard createDefaultGuard() {
        return createGuard("TestGuard", GuardType.ARCHER, 1);
    }

    public static Guard createGuard(String name, GuardType type, int level) {
        Guard guard = new Guard(EntityType.GUARD, testWorld);
        guard.setCustomName(Text.of(name));
        guard.setGuardType(type);
        guard.setLevel(level);
        return guard;
    }

    public static PlayerEntity createPlayerWithEmeralds(int count) {
        PlayerEntity player = createTestPlayer();
        player.getInventory().insertStack(
            new ItemStack(Items.EMERALD, count)
        );
        return player;
    }
}
```

## When to Use This Skill

Use this skill when:
- **Implementing new features** - Ensure tests exist before modifying code
- **Modifying existing code** - Prevent regressions
- **Fixing bugs** - Write regression test to prevent recurrence
- **validation-agent writing tests** - Comprehensive test strategy
- **Code reviews** - Check for adequate test coverage
- **Questions about testing strategy**
- **Preventing "Feature B breaks Feature A" scenarios**
- **Ensuring code quality and reliability**

## Key Principles

1. **Test Existing Before Modifying**: Always write tests for current behavior before changing it
2. **Regression Suite is Sacred**: Every fixed bug gets a test that stays forever
3. **Integration Matters**: Test how features interact, not just in isolation
4. **Coverage is Minimum**: 100% coverage doesn't mean bug-free, but <80% means high risk
5. **Tests Are Documentation**: Good tests show how code should work
6. **Fast Tests Run Often**: Slow tests don't get run, fast tests protect continuously
7. **Defensive Coding**: Validate inputs, handle errors, check assumptions
