package com.canya.xeenaa_structure_manager;

import com.canya.xeenaa_structure_manager.command.XeenaaCommand;
import com.canya.xeenaa_structure_manager.config.StructureManagerConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Xeenaa Structure Manager.
 *
 * <p><b>Epic 02 - Config System:</b>
 * This mod now includes a configuration system with performance presets for structure
 * placement optimization.
 *
 * <p><b>Active Systems:</b>
 * <ul>
 *   <li>StructureManagerConfig - Configuration with preset support</li>
 *   <li>PlacementTracker - Records every structure placement for analysis</li>
 *   <li>/xeenaa stats - Command to view placement statistics</li>
 *   <li>/xeenaa clear - Command to clear tracking data</li>
 *   <li>StructureStartPlaceMixin - Hooks structure placement events</li>
 * </ul>
 *
 * <p><b>Configuration:</b>
 * Config file location: {@code config/xeenaa-structure-manager.toml}<br>
 * Default preset: Balanced (2.0x spacing multiplier)<br>
 * Available presets: Vanilla, Balanced, Performance, Ultra
 *
 * @author Canya
 * @version Epic 02 (Config System)
 * @since 2025-10-25
 */
public class StructureManagerMod implements ModInitializer {
    public static final String MOD_ID = "xeenaa-structure-manager";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static StructureManagerConfig config;

    @Override
    public void onInitialize() {
        LOGGER.info("=== Xeenaa Structure Manager - Initializing ===");

        // Load configuration first (before any other systems)
        loadConfiguration();

        // Register commands
        registerCommands();

        LOGGER.info("=== Xeenaa Structure Manager initialized ===");
        LOGGER.info("Use '/xeenaa stats' to view placement statistics");
        LOGGER.info("Use '/xeenaa clear' to reset tracking data");
    }

    /**
     * Loads and validates the mod configuration.
     * <p>
     * This method loads the configuration from disk, validates all settings,
     * and logs the applied configuration for user visibility.
     * <p>
     * Configuration is loaded once at initialization and is read-only afterward.
     */
    private void loadConfiguration() {
        try {
            config = StructureManagerConfig.load();
            config.logAppliedConfig();

            // Log spacing multiplier summary (Epic 03 - one-time INFO log)
            LOGGER.info("Structure placement spacing multiplier: {}x (preset: {})",
                config.getAppliedSpacingMultiplier(),
                config.getActivePreset() != null ? config.getActivePreset() : "CUSTOM");
        } catch (Exception e) {
            LOGGER.error("Critical error loading configuration", e);
            throw new RuntimeException("Failed to initialize Xeenaa Structure Manager", e);
        }
    }

    /**
     * Registers all mod commands with the Fabric command system.
     * <p>
     * Currently registers:
     * <ul>
     *   <li>/xeenaa stats - Display placement statistics</li>
     *   <li>/xeenaa clear - Clear placement tracking data</li>
     * </ul>
     */
    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(XeenaaCommand::register);
        LOGGER.info("Registered /xeenaa commands");
    }

    /**
     * Gets the mod configuration.
     * <p>
     * The configuration is loaded once at initialization and is read-only afterward.
     * This method is thread-safe as the config instance is immutable after initialization.
     *
     * @return the mod configuration
     * @throws IllegalStateException if called before initialization
     */
    public static StructureManagerConfig getConfig() {
        if (config == null) {
            throw new IllegalStateException("Config accessed before initialization");
        }
        return config;
    }
}
