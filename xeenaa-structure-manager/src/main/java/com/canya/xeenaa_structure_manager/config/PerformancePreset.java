package com.canya.xeenaa_structure_manager.config;

/**
 * Performance presets for structure placement optimization.
 *
 * <p>Each preset defines a spacing multiplier that controls how far apart structures
 * spawn. Higher multipliers result in sparser structure placement and faster worldgen.</p>
 *
 * <p>Performance scaling is quadratic: 2x spacing = 75% fewer placement attempts,
 * 3x spacing = 89% fewer attempts, etc.</p>
 *
 * @see StructureManagerConfig
 */
public enum PerformancePreset {
    /**
     * No optimization - baseline vanilla performance.
     *
     * <p>Structures spawn at their normal density. No performance improvement.</p>
     */
    VANILLA(1.0, "No optimization - baseline performance"),

    /**
     * Balanced optimization - recommended for most users.
     *
     * <p>Reduces structure placement attempts by ~75%, resulting in approximately
     * 50% faster worldgen while maintaining good structure density.</p>
     */
    BALANCED(2.0, "50% faster worldgen, good structure density [RECOMMENDED]"),

    /**
     * Performance-focused optimization.
     *
     * <p>Reduces structure placement attempts by ~89%, resulting in approximately
     * 80% faster worldgen. Structures will be noticeably sparser.</p>
     */
    PERFORMANCE(3.0, "80% faster worldgen, sparse structures"),

    /**
     * Ultra performance optimization.
     *
     * <p>Reduces structure placement attempts by ~94%, resulting in approximately
     * 90% faster worldgen. Structures will be very sparse.</p>
     */
    ULTRA(4.0, "90% faster worldgen, very sparse structures");

    private final double spacingMultiplier;
    private final String description;

    /**
     * Constructs a performance preset.
     *
     * @param spacingMultiplier the spacing multiplier for this preset (must be > 0)
     * @param description human-readable description of the preset's effect
     * @throws IllegalArgumentException if spacingMultiplier <= 0
     */
    PerformancePreset(double spacingMultiplier, String description) {
        if (spacingMultiplier <= 0) {
            throw new IllegalArgumentException("Spacing multiplier must be positive");
        }
        this.spacingMultiplier = spacingMultiplier;
        this.description = description;
    }

    /**
     * Gets the spacing multiplier for this preset.
     *
     * @return the spacing multiplier (always > 0)
     */
    public double getSpacingMultiplier() {
        return spacingMultiplier;
    }

    /**
     * Gets the human-readable description of this preset.
     *
     * @return the preset description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parses a preset name (case-insensitive) into a PerformancePreset.
     *
     * @param name the preset name (e.g., "Balanced", "balanced", "BALANCED")
     * @return the matching preset, or null if not found
     */
    public static PerformancePreset fromString(String name) {
        if (name == null) {
            return null;
        }

        for (PerformancePreset preset : values()) {
            if (preset.name().equalsIgnoreCase(name)) {
                return preset;
            }
        }

        return null;
    }
}
