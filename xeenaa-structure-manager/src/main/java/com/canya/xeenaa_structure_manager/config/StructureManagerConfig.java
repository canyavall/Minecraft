package com.canya.xeenaa_structure_manager.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration manager for Xeenaa Structure Manager.
 *
 * <p>Handles loading, validation, and access to mod configuration. Supports both
 * preset-based configuration (for ease of use) and custom spacing multipliers
 * (for advanced users).</p>
 *
 * <p>Configuration is loaded once at mod initialization and is read-only afterward
 * for thread safety.</p>
 *
 * @see PerformancePreset
 */
public class StructureManagerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager/Config");

    private static final String CONFIG_FILE_NAME = "xeenaa-structure-manager.toml";
    private static final double DEFAULT_MULTIPLIER = 2.0;
    private static final PerformancePreset DEFAULT_PRESET = PerformancePreset.BALANCED;

    private final double appliedSpacingMultiplier;
    private final PerformancePreset activePreset;
    private final boolean usingCustomMultiplier;

    /**
     * Constructs a config instance with validated values.
     *
     * @param appliedSpacingMultiplier the spacing multiplier to use (must be >= 1.0)
     * @param activePreset the preset that was selected (may be null if using custom)
     * @param usingCustomMultiplier true if a custom multiplier override was used
     * @throws IllegalArgumentException if appliedSpacingMultiplier < 1.0
     */
    private StructureManagerConfig(double appliedSpacingMultiplier, PerformancePreset activePreset, boolean usingCustomMultiplier) {
        if (appliedSpacingMultiplier < 1.0) {
            throw new IllegalArgumentException("Applied spacing multiplier must be >= 1.0");
        }
        this.appliedSpacingMultiplier = appliedSpacingMultiplier;
        this.activePreset = activePreset;
        this.usingCustomMultiplier = usingCustomMultiplier;
    }

    /**
     * Gets the applied spacing multiplier.
     *
     * <p>This is the multiplier that will be used for structure placement optimization.
     * It is always >= 1.0 after validation.</p>
     *
     * @return the applied spacing multiplier
     */
    public double getAppliedSpacingMultiplier() {
        return appliedSpacingMultiplier;
    }

    /**
     * Gets the active performance preset.
     *
     * @return the active preset, or null if using a custom multiplier
     */
    public PerformancePreset getActivePreset() {
        return activePreset;
    }

    /**
     * Checks if a custom multiplier override is being used.
     *
     * @return true if using custom multiplier, false if using preset
     */
    public boolean isUsingCustomMultiplier() {
        return usingCustomMultiplier;
    }

    /**
     * Loads the configuration from disk.
     *
     * <p>Loading process:</p>
     * <ol>
     *   <li>Check if config file exists, create default if not</li>
     *   <li>Parse TOML configuration</li>
     *   <li>Validate and apply preset or custom multiplier</li>
     *   <li>Log the applied configuration</li>
     * </ol>
     *
     * @return the loaded and validated configuration
     * @throws RuntimeException if config file cannot be created or loaded
     */
    public static StructureManagerConfig load() {
        LOGGER.info("Loading configuration...");

        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);

        // Create default config if it doesn't exist
        if (!Files.exists(configPath)) {
            createDefaultConfig(configPath);
        }

        // Load config from file
        try (CommentedFileConfig config = CommentedFileConfig.of(configPath)) {
            config.load();
            return parseAndValidate(config);
        } catch (Exception e) {
            LOGGER.error("Failed to load config file, using defaults", e);
            return new StructureManagerConfig(DEFAULT_MULTIPLIER, DEFAULT_PRESET, false);
        }
    }

    /**
     * Creates the default configuration file.
     *
     * @param configPath path to the config file
     * @throws RuntimeException if file creation fails
     */
    private static void createDefaultConfig(Path configPath) {
        LOGGER.info("Config file not found, creating default: {}", configPath);

        try {
            // Ensure parent directory exists
            Files.createDirectories(configPath.getParent());

            // Create default config content
            String defaultConfig = buildDefaultConfigContent();

            // Write to file
            Files.writeString(configPath, defaultConfig);

            LOGGER.info("Default config file created successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create default config file", e);
        }
    }

    /**
     * Builds the default configuration file content.
     *
     * @return the default config as a TOML string
     */
    private static String buildDefaultConfigContent() {
        return """
                # Xeenaa Structure Manager Configuration
                # https://github.com/canya/xeenaa-structure-manager

                [performance]
                # Performance preset: Controls structure spacing optimization
                # Available presets:
                #   - "Vanilla": No optimization (1.0x spacing) - baseline performance
                #   - "Balanced": 50% faster worldgen, good structure density (2.0x) [RECOMMENDED]
                #   - "Performance": 80% faster worldgen, sparse structures (3.0x)
                #   - "Ultra": 90% faster worldgen, very sparse structures (4.0x)
                #
                # Performance scaling is quadratic:
                #   2x spacing = 75% fewer placement attempts
                #   3x spacing = 89% fewer attempts
                #   4x spacing = 94% fewer attempts
                preset = "Balanced"

                # Advanced: Set custom spacing multiplier (overrides preset if uncommented)
                # Must be >= 1.0 (values < 1.0 will be rejected and default to 2.0)
                # spacing_multiplier = 2.5
                """;
    }

    /**
     * Parses and validates the configuration.
     *
     * <p>Validation rules:</p>
     * <ul>
     *   <li>If spacing_multiplier is set: Use it (if valid), override preset</li>
     *   <li>If only preset is set: Use preset's multiplier</li>
     *   <li>If spacing_multiplier &lt; 1.0: Reject with ERROR, use 2.0x default</li>
     *   <li>If spacing_multiplier == 1.0: Log INFO "using recommended default", apply 2.0x</li>
     *   <li>If invalid preset: Fall back to Balanced with WARNING</li>
     * </ul>
     *
     * @param config the loaded config file
     * @return validated configuration instance
     */
    private static StructureManagerConfig parseAndValidate(CommentedFileConfig config) {
        Config performanceSection = config.get("performance");

        if (performanceSection == null) {
            LOGGER.warn("Performance section missing from config, using defaults");
            return new StructureManagerConfig(DEFAULT_MULTIPLIER, DEFAULT_PRESET, false);
        }

        // Check if custom multiplier is set (takes precedence over preset)
        if (performanceSection.contains("spacing_multiplier")) {
            return parseCustomMultiplier(performanceSection);
        } else {
            return parsePreset(performanceSection);
        }
    }

    /**
     * Parses and validates a custom spacing multiplier.
     *
     * @param config the performance config section
     * @return validated configuration with custom multiplier
     */
    private static StructureManagerConfig parseCustomMultiplier(Config config) {
        double customMultiplier = config.getOrElse("spacing_multiplier", DEFAULT_MULTIPLIER);

        // Validate custom multiplier
        if (customMultiplier < 1.0) {
            LOGGER.error(
                "Invalid spacing_multiplier: {} (must be >= 1.0). " +
                "Applying default: {}x",
                customMultiplier,
                DEFAULT_MULTIPLIER
            );
            return new StructureManagerConfig(DEFAULT_MULTIPLIER, null, true);
        }

        if (customMultiplier == 1.0) {
            LOGGER.info(
                "spacing_multiplier set to 1.0 (no optimization). " +
                "Using recommended default: {}x for better performance",
                DEFAULT_MULTIPLIER
            );
            return new StructureManagerConfig(DEFAULT_MULTIPLIER, null, true);
        }

        LOGGER.info("Using custom spacing multiplier: {}x (overriding preset)", customMultiplier);
        return new StructureManagerConfig(customMultiplier, null, true);
    }

    /**
     * Parses and validates a performance preset.
     *
     * @param config the performance config section
     * @return validated configuration with preset multiplier
     */
    private static StructureManagerConfig parsePreset(Config config) {
        String presetName = config.getOrElse("preset", DEFAULT_PRESET.name());
        PerformancePreset preset = PerformancePreset.fromString(presetName);

        if (preset == null) {
            LOGGER.warn(
                "Invalid preset: '{}'. Valid presets: Vanilla, Balanced, Performance, Ultra. " +
                "Falling back to: {}",
                presetName,
                DEFAULT_PRESET.name()
            );
            preset = DEFAULT_PRESET;
        }

        LOGGER.info("Using preset: {} ({}x spacing multiplier)", preset.name(), preset.getSpacingMultiplier());
        LOGGER.info("  {}", preset.getDescription());

        return new StructureManagerConfig(preset.getSpacingMultiplier(), preset, false);
    }

    /**
     * Logs the final applied configuration.
     *
     * <p>Called after config is fully loaded and validated to provide clear
     * feedback to the user about what settings are active.</p>
     */
    public void logAppliedConfig() {
        LOGGER.info("Configuration loaded successfully");
        LOGGER.info("========================================");

        if (usingCustomMultiplier) {
            LOGGER.info("Mode: Custom spacing multiplier");
            LOGGER.info("Applied multiplier: {}x", appliedSpacingMultiplier);
        } else if (activePreset != null) {
            LOGGER.info("Mode: {} preset", activePreset.name());
            LOGGER.info("Applied multiplier: {}x", appliedSpacingMultiplier);
            LOGGER.info("Description: {}", activePreset.getDescription());
        }

        // Calculate and log performance impact
        double reductionPercent = (1.0 - (1.0 / (appliedSpacingMultiplier * appliedSpacingMultiplier))) * 100.0;
        LOGGER.info("Structures will spawn ~{}x further apart on average", appliedSpacingMultiplier);
        LOGGER.info("Estimated placement reduction: ~{}%", String.format("%.1f", reductionPercent));

        if (appliedSpacingMultiplier >= 2.0) {
            LOGGER.info("Expected worldgen improvement: Moderate to significant");
        } else {
            LOGGER.info("Expected worldgen improvement: Minimal");
        }

        LOGGER.info("========================================");
    }
}
