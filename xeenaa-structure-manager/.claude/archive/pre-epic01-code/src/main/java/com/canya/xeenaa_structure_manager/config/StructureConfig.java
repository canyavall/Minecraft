package com.canya.xeenaa_structure_manager.config;

/**
 * Immutable configuration for a specific structure override.
 * <p>
 * Structure-specific overrides represent the highest priority in the configuration
 * system, allowing fine-grained control over individual structures. When a structure
 * has an override, its settings take precedence over type, size, and global multipliers.
 * <p>
 * This record uses Java 21's record feature to provide a concise, immutable data holder
 * for structure configuration values.
 * <p>
 * <b>Priority System:</b>
 * <pre>
 * Structure Override (highest) → Type → Size → Global (lowest)
 * </pre>
 * <p>
 * <b>Example Configuration (TOML):</b>
 * <pre>
 * [structures."minecraft:ancient_city"]
 * spacing = 48
 * separation = 16
 * enabled = true
 * </pre>
 * <p>
 * <b>Null Handling:</b> Fields can be null to allow partial overrides. For example,
 * a structure might override only {@code enabled} while leaving spacing/separation
 * to be determined by type/size/global multipliers.
 *
 * @param spacing the absolute spacing value (not a multiplier), or null to use priority system
 * @param separation the absolute separation value (not a multiplier), or null to use priority system
 * @param enabled whether this structure is enabled for world generation
 * @see ConfigManager
 * @since 1.0.0
 */
public record StructureConfig(
        Integer spacing,
        Integer separation,
        boolean enabled
) {
    /**
     * Creates a structure configuration with validation.
     * <p>
     * Validation rules:
     * <ul>
     *   <li>If both spacing and separation are non-null, spacing must be greater than separation</li>
     *   <li>Spacing and separation must be positive if non-null</li>
     * </ul>
     *
     * @param spacing the absolute spacing value, or null
     * @param separation the absolute separation value, or null
     * @param enabled whether the structure is enabled
     * @throws IllegalArgumentException if spacing ≤ separation or if either value is non-positive
     */
    public StructureConfig {
        // Validate spacing and separation
        if (spacing != null && spacing <= 0) {
            throw new IllegalArgumentException("Spacing must be positive: " + spacing);
        }
        if (separation != null && separation <= 0) {
            throw new IllegalArgumentException("Separation must be positive: " + separation);
        }
        if (spacing != null && separation != null && spacing <= separation) {
            throw new IllegalArgumentException(
                    "Spacing (" + spacing + ") must be greater than separation (" + separation + ")"
            );
        }
    }

    /**
     * Checks if this configuration has a spacing override.
     *
     * @return true if spacing is explicitly set, false if it should use the priority system
     */
    public boolean hasSpacingOverride() {
        return spacing != null;
    }

    /**
     * Checks if this configuration has a separation override.
     *
     * @return true if separation is explicitly set, false if it should use the priority system
     */
    public boolean hasSeparationOverride() {
        return separation != null;
    }

    /**
     * Creates a structure configuration with only enabled/disabled set.
     * <p>
     * This is useful for structures where you only want to control whether they spawn,
     * but let the type/size/global multipliers handle spacing calculations.
     *
     * @param enabled whether the structure is enabled
     * @return a structure configuration with no spacing/separation overrides
     */
    public static StructureConfig enabledOnly(boolean enabled) {
        return new StructureConfig(null, null, enabled);
    }

    /**
     * Creates a fully-specified structure configuration.
     * <p>
     * This is useful for structures that need complete control over their spawning behavior.
     *
     * @param spacing the absolute spacing value
     * @param separation the absolute separation value
     * @param enabled whether the structure is enabled
     * @return a fully-configured structure override
     * @throws IllegalArgumentException if spacing ≤ separation or if either value is non-positive
     */
    public static StructureConfig of(int spacing, int separation, boolean enabled) {
        return new StructureConfig(spacing, separation, enabled);
    }
}
