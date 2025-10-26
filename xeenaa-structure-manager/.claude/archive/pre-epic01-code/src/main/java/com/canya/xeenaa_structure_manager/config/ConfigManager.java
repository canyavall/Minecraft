package com.canya.xeenaa_structure_manager.config;

import com.canya.xeenaa_structure_manager.classification.SizeCategory;
import com.canya.xeenaa_structure_manager.classification.TypeCategory;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages TOML-based configuration for structure spacing and separation multipliers.
 * <p>
 * This singleton class provides a hierarchical configuration system with the following priority order:
 * <pre>
 * Structure Override → Type → Size → Global (preset-modified)
 * </pre>
 * <p>
 * <b>Configuration Structure:</b>
 * <ul>
 *   <li><b>Preset</b>: High-level profiles (sparse, balanced, abundant) that modify global multipliers</li>
 *   <li><b>Global</b>: Base spacing/separation multipliers applied to all structures</li>
 *   <li><b>Size</b>: Multipliers based on structure volume (small, medium, large)</li>
 *   <li><b>Type</b>: Multipliers based on structure behavior (town, dungeon, temple, etc.)</li>
 *   <li><b>Structure</b>: Absolute overrides for specific structures</li>
 * </ul>
 * <p>
 * <b>Configuration File:</b> {@code config/xeenaa-structure-manager.toml}
 * <p>
 * <b>Thread Safety:</b> This class is thread-safe. Configuration values are cached in final
 * immutable collections after loading, allowing safe concurrent access.
 * <p>
 * <b>Hot-Reload:</b> Call {@link #reload()} to reload configuration from disk without restarting.
 *
 * @see PresetType
 * @see StructureConfig
 * @see SizeCategory
 * @see TypeCategory
 * @since 1.0.0
 */
public final class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager/Config");

    // Configuration file constants
    private static final String CONFIG_FILENAME = "xeenaa-structure-manager.toml";
    private static final Path CONFIG_PATH = Paths.get("config", CONFIG_FILENAME);

    // Validation constants
    private static final double MIN_MULTIPLIER = 0.1;
    private static final double MAX_MULTIPLIER = 10.0;
    private static final double DEFAULT_GLOBAL_SPACING = 1.0;
    private static final double DEFAULT_GLOBAL_SEPARATION = 1.0;

    // Singleton instance
    private static volatile ConfigManager INSTANCE;

    // Configuration state (immutable after loading for thread safety)
    private volatile PresetType activePreset;
    private volatile double globalSpacingMultiplier;
    private volatile double globalSeparationMultiplier;
    private volatile Map<SizeCategory, Double> sizeMultipliers;
    private volatile Map<TypeCategory, Double> typeMultipliers;
    private volatile Map<Identifier, StructureConfig> structureOverrides;

    /**
     * Gets the singleton instance of ConfigManager.
     * <p>
     * This method uses double-checked locking for thread-safe lazy initialization.
     * The first call will load or generate the configuration file.
     *
     * @return the singleton ConfigManager instance
     */
    public static ConfigManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ConfigManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConfigManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Private constructor for singleton pattern.
     * <p>
     * Initializes the configuration system by:
     * <ol>
     *   <li>Creating the config directory if it doesn't exist</li>
     *   <li>Generating default config file if it doesn't exist</li>
     *   <li>Loading configuration from disk</li>
     * </ol>
     */
    private ConfigManager() {
        LOGGER.info("Initializing configuration system...");

        try {
            // Ensure config directory exists
            Path configDir = CONFIG_PATH.getParent();
            if (configDir != null && !Files.exists(configDir)) {
                Files.createDirectories(configDir);
                LOGGER.info("Created config directory: {}", configDir);
            }

            // Generate default config if it doesn't exist
            if (!Files.exists(CONFIG_PATH)) {
                LOGGER.info("Config file not found, generating default configuration...");
                generateDefaultConfig();
            }

            // Load configuration
            loadConfig();

            LOGGER.info("Configuration system initialized successfully");
        } catch (IOException e) {
            LOGGER.error("Failed to initialize configuration system", e);
            // Use safe defaults
            applyDefaults();
        }
    }

    /**
     * Loads configuration from the TOML file.
     * <p>
     * This method parses the TOML file, validates all values, and caches them in
     * immutable collections for fast thread-safe access.
     * <p>
     * Invalid values are logged as warnings and replaced with safe defaults.
     */
    private synchronized void loadConfig() {
        LOGGER.info("Loading configuration from: {}", CONFIG_PATH);

        try (CommentedFileConfig config = CommentedFileConfig.of(CONFIG_PATH)) {
            config.load();

            // Load preset
            String presetKey = config.getOrElse("preset.active", "balanced");
            activePreset = PresetType.fromConfigKey(presetKey);
            LOGGER.info("Active preset: {}", activePreset.getConfigKey());

            // Load global multipliers (before preset application)
            double baseGlobalSpacing = validateMultiplier(
                    config.getOrElse("global.spacing_multiplier", DEFAULT_GLOBAL_SPACING),
                    "global.spacing_multiplier",
                    DEFAULT_GLOBAL_SPACING
            );
            double baseGlobalSeparation = validateMultiplier(
                    config.getOrElse("global.separation_multiplier", DEFAULT_GLOBAL_SEPARATION),
                    "global.separation_multiplier",
                    DEFAULT_GLOBAL_SEPARATION
            );

            // Apply preset multipliers to global values
            globalSpacingMultiplier = baseGlobalSpacing * activePreset.getSpacingMultiplier();
            globalSeparationMultiplier = baseGlobalSeparation * activePreset.getSeparationMultiplier();

            LOGGER.info("Global spacing: {}x, separation: {}x",
                    String.format("%.2f", globalSpacingMultiplier),
                    String.format("%.2f", globalSeparationMultiplier));

            // Validate spacing > separation
            if (globalSpacingMultiplier <= globalSeparationMultiplier) {
                LOGGER.warn("Global spacing ({}) must be greater than separation ({}), using defaults",
                        globalSpacingMultiplier, globalSeparationMultiplier);
                globalSpacingMultiplier = DEFAULT_GLOBAL_SPACING;
                globalSeparationMultiplier = DEFAULT_GLOBAL_SEPARATION;
            }

            // Load size-based multipliers
            loadSizeMultipliers(config);

            // Load type-based multipliers
            loadTypeMultipliers(config);

            // Load structure-specific overrides
            loadStructureOverrides(config);

            LOGGER.info("Configuration loaded successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration, using defaults", e);
            applyDefaults();
        }
    }

    /**
     * Loads size-based multipliers from the configuration.
     * <p>
     * Size multipliers are applied to structures based on their volume category.
     * These values are multiplied by the global multiplier to get the final value.
     *
     * @param config the configuration object
     */
    private void loadSizeMultipliers(CommentedFileConfig config) {
        Map<SizeCategory, Double> multipliers = new EnumMap<>(SizeCategory.class);

        multipliers.put(SizeCategory.SMALL, validateMultiplier(
                config.getOrElse("size.small.spacing_multiplier", 1.2),
                "size.small.spacing_multiplier",
                1.2
        ));
        multipliers.put(SizeCategory.MEDIUM, validateMultiplier(
                config.getOrElse("size.medium.spacing_multiplier", 1.5),
                "size.medium.spacing_multiplier",
                1.5
        ));
        multipliers.put(SizeCategory.LARGE, validateMultiplier(
                config.getOrElse("size.large.spacing_multiplier", 2.5),
                "size.large.spacing_multiplier",
                2.5
        ));

        sizeMultipliers = Map.copyOf(multipliers);

        LOGGER.info("Size multipliers: small={}x, medium={}x, large={}x",
                String.format("%.2f", multipliers.get(SizeCategory.SMALL)),
                String.format("%.2f", multipliers.get(SizeCategory.MEDIUM)),
                String.format("%.2f", multipliers.get(SizeCategory.LARGE)));
    }

    /**
     * Loads type-based multipliers from the configuration.
     * <p>
     * Type multipliers are applied to structures based on their behavioral category.
     * These values take priority over size multipliers and are multiplied by the global multiplier.
     *
     * @param config the configuration object
     */
    private void loadTypeMultipliers(CommentedFileConfig config) {
        Map<TypeCategory, Double> multipliers = new EnumMap<>(TypeCategory.class);

        multipliers.put(TypeCategory.TOWN, validateMultiplier(
                config.getOrElse("type.town.spacing_multiplier", 2.0),
                "type.town.spacing_multiplier",
                2.0
        ));
        multipliers.put(TypeCategory.DUNGEON, validateMultiplier(
                config.getOrElse("type.dungeon.spacing_multiplier", 1.8),
                "type.dungeon.spacing_multiplier",
                1.8
        ));
        multipliers.put(TypeCategory.TEMPLE, validateMultiplier(
                config.getOrElse("type.temple.spacing_multiplier", 2.2),
                "type.temple.spacing_multiplier",
                2.2
        ));
        multipliers.put(TypeCategory.MINESHAFT, validateMultiplier(
                config.getOrElse("type.mineshaft.spacing_multiplier", 1.0),
                "type.mineshaft.spacing_multiplier",
                1.0
        ));
        multipliers.put(TypeCategory.SKY, validateMultiplier(
                config.getOrElse("type.sky.spacing_multiplier", 1.5),
                "type.sky.spacing_multiplier",
                1.5
        ));
        multipliers.put(TypeCategory.RUIN, validateMultiplier(
                config.getOrElse("type.ruin.spacing_multiplier", 1.3),
                "type.ruin.spacing_multiplier",
                1.3
        ));

        typeMultipliers = Map.copyOf(multipliers);

        LOGGER.info("Type multipliers: town={}x, dungeon={}x, temple={}x, mineshaft={}x, sky={}x, ruin={}x",
                String.format("%.2f", multipliers.get(TypeCategory.TOWN)),
                String.format("%.2f", multipliers.get(TypeCategory.DUNGEON)),
                String.format("%.2f", multipliers.get(TypeCategory.TEMPLE)),
                String.format("%.2f", multipliers.get(TypeCategory.MINESHAFT)),
                String.format("%.2f", multipliers.get(TypeCategory.SKY)),
                String.format("%.2f", multipliers.get(TypeCategory.RUIN)));
    }

    /**
     * Loads structure-specific overrides from the configuration.
     * <p>
     * Structure overrides have the highest priority and provide absolute control
     * over individual structures' spawning behavior.
     *
     * @param config the configuration object
     */
    private void loadStructureOverrides(CommentedFileConfig config) {
        Map<Identifier, StructureConfig> overrides = new HashMap<>();

        // Check if structures section exists
        CommentedConfig structuresSection = config.get("structures");
        if (structuresSection != null) {
            for (CommentedConfig.Entry entry : structuresSection.entrySet()) {
                String structureId = entry.getKey();
                try {
                    Identifier id = Identifier.of(structureId);
                    CommentedConfig structureConfig = entry.getValue();

                    Integer spacing = structureConfig.get("spacing");
                    Integer separation = structureConfig.get("separation");
                    Boolean enabled = structureConfig.getOrElse("enabled", true);

                    StructureConfig override = new StructureConfig(spacing, separation, enabled);
                    overrides.put(id, override);

                    LOGGER.info("Loaded structure override: {} (spacing={}, separation={}, enabled={})",
                            structureId,
                            spacing != null ? spacing : "default",
                            separation != null ? separation : "default",
                            enabled);
                } catch (Exception e) {
                    LOGGER.warn("Failed to load structure override for '{}': {}", structureId, e.getMessage());
                }
            }
        }

        structureOverrides = Map.copyOf(overrides);
        LOGGER.info("Loaded {} structure override(s)", overrides.size());
    }

    /**
     * Validates a multiplier value and returns a safe default if invalid.
     * <p>
     * Valid multipliers must be in the range (MIN_MULTIPLIER, MAX_MULTIPLIER).
     * Invalid values are logged as warnings.
     *
     * @param value the value to validate
     * @param key the configuration key (for logging)
     * @param defaultValue the default value to use if validation fails
     * @return the validated value or the default value
     */
    private double validateMultiplier(Object value, String key, double defaultValue) {
        try {
            double multiplier = ((Number) value).doubleValue();
            if (multiplier < MIN_MULTIPLIER || multiplier > MAX_MULTIPLIER) {
                LOGGER.warn("Multiplier '{}' = {} is out of range [{}, {}], using default: {}",
                        key, multiplier, MIN_MULTIPLIER, MAX_MULTIPLIER, defaultValue);
                return defaultValue;
            }
            return multiplier;
        } catch (Exception e) {
            LOGGER.warn("Invalid multiplier value for '{}': {}, using default: {}",
                    key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Applies default configuration values.
     * <p>
     * This is used as a fallback when configuration loading fails.
     */
    private void applyDefaults() {
        activePreset = PresetType.BALANCED;
        globalSpacingMultiplier = DEFAULT_GLOBAL_SPACING;
        globalSeparationMultiplier = DEFAULT_GLOBAL_SEPARATION;

        Map<SizeCategory, Double> defaultSize = new EnumMap<>(SizeCategory.class);
        defaultSize.put(SizeCategory.SMALL, 1.2);
        defaultSize.put(SizeCategory.MEDIUM, 1.5);
        defaultSize.put(SizeCategory.LARGE, 2.5);
        sizeMultipliers = Map.copyOf(defaultSize);

        Map<TypeCategory, Double> defaultType = new EnumMap<>(TypeCategory.class);
        defaultType.put(TypeCategory.TOWN, 2.0);
        defaultType.put(TypeCategory.DUNGEON, 1.8);
        defaultType.put(TypeCategory.TEMPLE, 2.2);
        defaultType.put(TypeCategory.MINESHAFT, 1.0);
        defaultType.put(TypeCategory.SKY, 1.5);
        defaultType.put(TypeCategory.RUIN, 1.3);
        typeMultipliers = Map.copyOf(defaultType);

        structureOverrides = Map.of();

        LOGGER.info("Applied default configuration values");
    }

    /**
     * Generates the default TOML configuration file.
     * <p>
     * Creates a well-commented configuration file with balanced preset as default.
     *
     * @throws IOException if file writing fails
     */
    private void generateDefaultConfig() throws IOException {
        String defaultConfig = """
                # Xeenaa Structure Manager Configuration
                # This file controls how structures spawn in your world

                [preset]
                # Active preset: "sparse", "balanced", or "abundant"
                # - sparse: Structures 2x further apart (explorer friendly)
                # - balanced: Default vanilla-like density (recommended)
                # - abundant: More structures for builders
                active = "balanced"

                [global]
                # Global multipliers apply to all structures (base level)
                spacing_multiplier = 1.0
                separation_multiplier = 1.0

                # SIZE-based multipliers (override global)
                # Small: < 4,096 blocks³ (ruins, shrines, small houses)
                [size.small]
                spacing_multiplier = 1.2

                # Medium: 4,096 - 32,768 blocks³ (temples, dungeons, medium structures)
                [size.medium]
                spacing_multiplier = 1.5

                # Large: > 32,768 blocks³ (villages, mansions, strongholds)
                [size.large]
                spacing_multiplier = 2.5

                # TYPE-based multipliers (override size when specified)
                # These handle edge cases like mineshafts and dungeons

                [type.town]
                spacing_multiplier = 2.0

                [type.dungeon]
                spacing_multiplier = 1.8

                [type.temple]
                spacing_multiplier = 2.2

                [type.mineshaft]
                spacing_multiplier = 1.0  # CRITICAL: Don't modify mineshafts!

                [type.sky]
                spacing_multiplier = 1.5

                [type.ruin]
                spacing_multiplier = 1.3

                # Structure-specific overrides (highest priority)
                # Example: Override ancient city spacing
                # [structures."minecraft:ancient_city"]
                # spacing = 48
                # separation = 16
                # enabled = true
                """;

        Files.writeString(CONFIG_PATH, defaultConfig);
        LOGGER.info("Generated default config file: {}", CONFIG_PATH);
    }

    /**
     * Gets the spacing multiplier for a structure using the priority system.
     * <p>
     * <b>Priority Order:</b>
     * <ol>
     *   <li>Structure-specific override (absolute value, not multiplier)</li>
     *   <li>Type-based multiplier (if type is not UNKNOWN)</li>
     *   <li>Size-based multiplier</li>
     *   <li>Global multiplier (preset-modified)</li>
     * </ol>
     * <p>
     * <b>Important:</b> Size and type multipliers are already compounded with the global
     * multiplier during configuration loading, so this method returns the final value.
     *
     * @param structureId the structure identifier
     * @param size the size category of the structure
     * @param type the type category of the structure
     * @return the final spacing multiplier to apply
     */
    public double getSpacingMultiplier(Identifier structureId, SizeCategory size, TypeCategory type) {
        // Priority 1: Structure-specific override (highest)
        StructureConfig override = structureOverrides.get(structureId);
        if (override != null && override.hasSpacingOverride()) {
            // Structure overrides are absolute values, not multipliers
            // Return the spacing value directly as a "multiplier" (it will be used as-is)
            return override.spacing();
        }

        // Priority 2: Type-based multiplier (overrides size)
        if (type != TypeCategory.UNKNOWN && typeMultipliers.containsKey(type)) {
            return typeMultipliers.get(type) * globalSpacingMultiplier;
        }

        // Priority 3: Size-based multiplier
        if (sizeMultipliers.containsKey(size)) {
            return sizeMultipliers.get(size) * globalSpacingMultiplier;
        }

        // Priority 4: Global multiplier (fallback)
        return globalSpacingMultiplier;
    }

    /**
     * Gets the separation multiplier for a structure using the priority system.
     * <p>
     * <b>Priority Order:</b>
     * <ol>
     *   <li>Structure-specific override (absolute value, not multiplier)</li>
     *   <li>Type-based multiplier (if type is not UNKNOWN)</li>
     *   <li>Size-based multiplier</li>
     *   <li>Global multiplier (preset-modified)</li>
     * </ol>
     * <p>
     * <b>Note:</b> Currently, type and size multipliers only apply to spacing.
     * Separation always uses the global separation multiplier unless there's a
     * structure-specific override. This is by design to maintain consistent
     * minimum distances between structures of the same type.
     *
     * @param structureId the structure identifier
     * @param size the size category of the structure (currently unused for separation)
     * @param type the type category of the structure (currently unused for separation)
     * @return the final separation multiplier to apply
     */
    public double getSeparationMultiplier(Identifier structureId, SizeCategory size, TypeCategory type) {
        // Priority 1: Structure-specific override (highest)
        StructureConfig override = structureOverrides.get(structureId);
        if (override != null && override.hasSeparationOverride()) {
            // Structure overrides are absolute values, not multipliers
            return override.separation();
        }

        // For now, separation uses global multiplier only
        // Future enhancement: Could add type/size-based separation multipliers
        return globalSeparationMultiplier;
    }

    /**
     * Checks if a structure is enabled for world generation.
     * <p>
     * Structures are enabled by default unless explicitly disabled via structure override.
     *
     * @param structureId the structure identifier
     * @return true if the structure is enabled, false otherwise
     */
    public boolean isStructureEnabled(Identifier structureId) {
        StructureConfig override = structureOverrides.get(structureId);
        if (override != null) {
            return override.enabled();
        }
        return true; // Default: all structures enabled
    }

    /**
     * Reloads configuration from disk.
     * <p>
     * This method allows hot-reloading configuration changes without restarting
     * the game or server. Changes take effect immediately for new structure generation.
     * <p>
     * <b>Thread Safety:</b> This method is synchronized to prevent concurrent reloads.
     * Configuration values are atomically updated via volatile fields.
     */
    public synchronized void reload() {
        LOGGER.info("Reloading configuration...");
        try {
            loadConfig();
            LOGGER.info("Configuration reloaded successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to reload configuration, keeping existing values", e);
        }
    }

    /**
     * Gets the currently active preset.
     *
     * @return the active preset type
     */
    public PresetType getActivePreset() {
        return activePreset;
    }

    /**
     * Gets the global spacing multiplier (after preset application).
     *
     * @return the global spacing multiplier
     */
    public double getGlobalSpacingMultiplier() {
        return globalSpacingMultiplier;
    }

    /**
     * Gets the global separation multiplier (after preset application).
     *
     * @return the global separation multiplier
     */
    public double getGlobalSeparationMultiplier() {
        return globalSeparationMultiplier;
    }
}
