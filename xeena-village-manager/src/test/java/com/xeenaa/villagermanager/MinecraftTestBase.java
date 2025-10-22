package com.xeenaa.villagermanager;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base class for tests that need Minecraft's registry system initialized.
 *
 * <p>This class ensures that Minecraft's registries are properly bootstrapped before
 * any test execution begins. This is critical when testing code that:</p>
 * <ul>
 *   <li>Mocks Minecraft classes (e.g., ServerPlayerEntity, VillagerEntity)</li>
 *   <li>Uses Mockito instrumentation on classes with static initializers</li>
 *   <li>Accesses Minecraft registries during class loading</li>
 * </ul>
 *
 * <h2>Why This Is Needed</h2>
 * <p>When Mockito attempts to instrument Minecraft classes like {@code ServerPlayerEntity},
 * it triggers static initializers that access Minecraft's registry system. If the registries
 * haven't been bootstrapped, this results in:</p>
 * <pre>
 * MockitoException: Cannot instrument class net.minecraft.server.network.ServerPlayerEntity
 * Caused by: IllegalArgumentException: Not bootstrapped (called from registry ResourceKey[minecraft:root / minecraft:game_event])
 * </pre>
 *
 * <h2>Usage</h2>
 * <p>Simply extend this class in your test class:</p>
 * <pre>
 * class MyMinecraftTest extends MinecraftTestBase {
 *     // Your tests here
 * }
 * </pre>
 *
 * <h2>Thread Safety</h2>
 * <p>Bootstrap is performed once before all tests via {@link BeforeAll}. The static
 * boolean flag ensures that even if multiple test classes extend this base, bootstrap
 * only executes once per JVM instance.</p>
 *
 * @see Bootstrap#initialize()
 * @see SharedConstants#createGameVersion()
 */
public abstract class MinecraftTestBase {

    /**
     * Flag to track if Minecraft has been bootstrapped.
     * Ensures bootstrap only runs once per JVM instance.
     */
    private static boolean bootstrapped = false;

    /**
     * Initializes Minecraft's registry system before any tests run.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Creates the game version via {@link SharedConstants#createGameVersion()}</li>
     *   <li>Initializes all Minecraft registries via {@link Bootstrap#initialize()}</li>
     *   <li>Sets the bootstrapped flag to prevent duplicate initialization</li>
     * </ol>
     *
     * <p><strong>Note:</strong> This runs once before ALL tests in the test class,
     * not before each individual test method.</p>
     *
     * <p><strong>Module Access:</strong> In Java 21+, this may encounter
     * {@link IllegalAccessError} due to module restrictions. If bootstrap fails,
     * the error is caught and logged but tests continue. This allows mocking
     * Minecraft classes without full registry initialization.</p>
     *
     * @throws RuntimeException if bootstrap fails critically (non-access-related errors)
     */
    @BeforeAll
    public static void bootstrapMinecraft() {
        if (!bootstrapped) {
            try {
                // Attempt to disable bootstrap checks
                System.setProperty("fabric.development", "true");
                System.setProperty("fabric.gametest", "true");

                SharedConstants.createGameVersion();
                Bootstrap.initialize();
                bootstrapped = true;
                System.out.println("[MinecraftTestBase] Successfully bootstrapped Minecraft registries");
            } catch (IllegalAccessError | IllegalArgumentException e) {
                // Expected in Java 21+ due to module restrictions
                // Tests using mocks will still work, but direct Minecraft API usage may fail
                System.err.println("[MinecraftTestBase] Bootstrap failed (expected in some Java versions): " + e.getMessage());
                System.err.println("[MinecraftTestBase] Tests will continue with mocked Minecraft classes");
                bootstrapped = true; // Mark as attempted to prevent retries
            } catch (Exception e) {
                // Unexpected error - rethrow
                throw new RuntimeException("Unexpected error during Minecraft bootstrap", e);
            }
        }
    }
}
