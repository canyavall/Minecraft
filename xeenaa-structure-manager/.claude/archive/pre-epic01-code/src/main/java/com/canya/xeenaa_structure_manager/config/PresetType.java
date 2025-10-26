package com.canya.xeenaa_structure_manager.config;

/**
 * Preset configurations for structure spacing and separation multipliers.
 * <p>
 * Presets provide quick configuration profiles for different gameplay styles:
 * <ul>
 *   <li><b>SPARSE</b>: Structures 2x further apart for exploration-focused gameplay</li>
 *   <li><b>BALANCED</b>: Default vanilla-like density (recommended)</li>
 *   <li><b>ABUNDANT</b>: More structures closer together for builder-focused gameplay</li>
 * </ul>
 * <p>
 * Presets are applied as multipliers to the global configuration values, which then
 * cascade through the priority system (Structure &gt; Type &gt; Size &gt; Global).
 *
 * @see ConfigManager
 * @since 1.0.0
 */
public enum PresetType {
    /**
     * Sparse preset - structures 2x further apart for exploration-focused gameplay.
     * <p>
     * Global multipliers:
     * <ul>
     *   <li>Spacing: 2.0x</li>
     *   <li>Separation: 1.5x</li>
     * </ul>
     * <p>
     * Effect on size multipliers (compounded):
     * <ul>
     *   <li>Small: 2.4x (1.2 × 2.0)</li>
     *   <li>Medium: 3.0x (1.5 × 2.0)</li>
     *   <li>Large: 5.0x (2.5 × 2.0)</li>
     * </ul>
     * <p>
     * Best for: Players who enjoy exploration and want structures to feel rare and rewarding.
     */
    SPARSE("sparse", 2.0, 1.5),

    /**
     * Balanced preset - default vanilla-like structure density (recommended).
     * <p>
     * Global multipliers:
     * <ul>
     *   <li>Spacing: 1.0x (no change)</li>
     *   <li>Separation: 1.0x (no change)</li>
     * </ul>
     * <p>
     * Effect on size multipliers (unchanged):
     * <ul>
     *   <li>Small: 1.2x</li>
     *   <li>Medium: 1.5x</li>
     *   <li>Large: 2.5x</li>
     * </ul>
     * <p>
     * Best for: Default gameplay experience similar to vanilla Minecraft.
     */
    BALANCED("balanced", 1.0, 1.0),

    /**
     * Abundant preset - more structures closer together for builder-focused gameplay.
     * <p>
     * Global multipliers:
     * <ul>
     *   <li>Spacing: 0.8x</li>
     *   <li>Separation: 0.9x</li>
     * </ul>
     * <p>
     * Effect on size multipliers (reduced):
     * <ul>
     *   <li>Small: 0.96x (1.2 × 0.8)</li>
     *   <li>Medium: 1.2x (1.5 × 0.8)</li>
     *   <li>Large: 2.0x (2.5 × 0.8)</li>
     * </ul>
     * <p>
     * Best for: Builders who want more structures to discover and build around.
     */
    ABUNDANT("abundant", 0.8, 0.9);

    private final String configKey;
    private final double spacingMultiplier;
    private final double separationMultiplier;

    /**
     * Creates a preset type with the specified multipliers.
     *
     * @param configKey the configuration key used in TOML files
     * @param spacingMultiplier the global spacing multiplier for this preset
     * @param separationMultiplier the global separation multiplier for this preset
     */
    PresetType(String configKey, double spacingMultiplier, double separationMultiplier) {
        this.configKey = configKey;
        this.spacingMultiplier = spacingMultiplier;
        this.separationMultiplier = separationMultiplier;
    }

    /**
     * Gets the configuration key for this preset.
     * <p>
     * This key is used in the TOML configuration file to identify the active preset.
     *
     * @return the lowercase preset name (e.g., "sparse", "balanced", "abundant")
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * Gets the global spacing multiplier for this preset.
     * <p>
     * This multiplier is applied to all size-based and type-based multipliers,
     * creating a cascading effect throughout the configuration system.
     *
     * @return the spacing multiplier (e.g., 2.0 for sparse, 1.0 for balanced, 0.8 for abundant)
     */
    public double getSpacingMultiplier() {
        return spacingMultiplier;
    }

    /**
     * Gets the global separation multiplier for this preset.
     * <p>
     * Separation defines the minimum distance between structures of the same type.
     * It must always be less than spacing to ensure valid structure placement.
     *
     * @return the separation multiplier (e.g., 1.5 for sparse, 1.0 for balanced, 0.9 for abundant)
     */
    public double getSeparationMultiplier() {
        return separationMultiplier;
    }

    /**
     * Finds a preset by its configuration key.
     * <p>
     * This method performs case-insensitive matching to provide a forgiving
     * configuration experience (e.g., "Sparse", "SPARSE", and "sparse" all work).
     *
     * @param configKey the configuration key to match (case-insensitive)
     * @return the matching preset type, or {@link #BALANCED} if no match is found
     */
    public static PresetType fromConfigKey(String configKey) {
        if (configKey == null || configKey.isBlank()) {
            return BALANCED;
        }

        String normalized = configKey.trim().toLowerCase();
        for (PresetType preset : values()) {
            if (preset.configKey.equals(normalized)) {
                return preset;
            }
        }

        // Return default preset if no match found
        return BALANCED;
    }
}
