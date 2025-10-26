package com.canya.xeenaa_structure_manager.classification;

/**
 * Categorizes structures by their physical size (bounding box volume).
 * <p>
 * Size classification is one dimension of the two-dimensional classification system
 * (SIZE + TYPE). Size measures the physical world footprint and is used to determine
 * structure spacing when type-based classification is unavailable or type is UNKNOWN.
 * <p>
 * Volume thresholds are based on cubic blocks (width × height × depth).
 *
 * @see TypeCategory
 * @see StructureClassification
 * @since 1.0.0
 */
public enum SizeCategory {
    /**
     * Small structures with volume less than 4,096 blocks³ (approximately 16×16×16).
     * <p>
     * Examples: Small ruins, single houses, ore veins, shrines, small decorative structures.
     * <p>
     * Default spacing multiplier: 1.2x
     */
    SMALL(0, 4_096),

    /**
     * Medium structures with volume between 4,096 and 32,768 blocks³ (approximately 16×16×16 to 32×32×32).
     * <p>
     * Examples: Village houses, small temples, medium dungeons, watchtowers.
     * <p>
     * Default spacing multiplier: 1.5x
     */
    MEDIUM(4_096, 32_768),

    /**
     * Large structures with volume greater than 32,768 blocks³ (approximately 32×32×32+).
     * <p>
     * Examples: Mansions, strongholds, ancient cities, large villages, mineshafts.
     * <p>
     * Default spacing multiplier: 2.5x
     * <p>
     * Note: Large structures may have type-based overrides (e.g., mineshafts use type.mineshaft = 1.0x
     * instead of size.large = 2.5x to prevent breaking vanilla balance).
     */
    LARGE(32_768, Integer.MAX_VALUE);

    private final int minVolume;
    private final int maxVolume;

    /**
     * Creates a size category with the specified volume range.
     *
     * @param minVolume the minimum volume in blocks³ (inclusive)
     * @param maxVolume the maximum volume in blocks³ (exclusive)
     */
    SizeCategory(int minVolume, int maxVolume) {
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
    }

    /**
     * Gets the minimum volume for this category.
     *
     * @return the minimum volume in blocks³ (inclusive)
     */
    public int getMinVolume() {
        return minVolume;
    }

    /**
     * Gets the maximum volume for this category.
     *
     * @return the maximum volume in blocks³ (exclusive)
     */
    public int getMaxVolume() {
        return maxVolume;
    }

    /**
     * Determines the size category for a given structure volume.
     * <p>
     * This method categorizes structures based on their bounding box volume:
     * <ul>
     *   <li>SMALL: 0 - 4,095 blocks³</li>
     *   <li>MEDIUM: 4,096 - 32,767 blocks³</li>
     *   <li>LARGE: 32,768+ blocks³</li>
     * </ul>
     *
     * @param volume the structure volume in blocks³
     * @return the appropriate size category for the given volume
     * @throws IllegalArgumentException if volume is negative
     */
    public static SizeCategory fromVolume(int volume) {
        if (volume < 0) {
            throw new IllegalArgumentException("Volume cannot be negative: " + volume);
        }

        for (SizeCategory category : values()) {
            if (volume >= category.minVolume && volume < category.maxVolume) {
                return category;
            }
        }

        // Fallback to LARGE for volumes exceeding the maximum range
        return LARGE;
    }

    /**
     * Checks if a given volume falls within this category's range.
     *
     * @param volume the volume to check in blocks³
     * @return true if the volume is within this category's range
     */
    public boolean contains(int volume) {
        return volume >= minVolume && volume < maxVolume;
    }
}
