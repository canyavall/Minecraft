package com.xeenaa.villagermanager.display;

/**
 * Configuration option for controlling villager profession/rank display above heads.
 */
public enum VillagerDisplayMode {
    /**
     * Show profession and level/rank information for all villagers.
     * - Guards: Show rank name + tier stars
     * - Regular villagers: Show profession name + level stars
     */
    SHOW_ALL("show_all"),

    /**
     * Show rank information only for guards (default behavior).
     * - Guards: Show rank name + tier stars
     * - Regular villagers: No custom name display
     */
    GUARDS_ONLY("guards_only"),

    /**
     * Don't show any profession/rank information above villager heads.
     * - All villagers: No custom name display
     */
    NONE("none");

    private final String id;

    VillagerDisplayMode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Parse display mode from string ID (for config deserialization).
     */
    public static VillagerDisplayMode fromId(String id) {
        for (VillagerDisplayMode mode : values()) {
            if (mode.getId().equals(id)) {
                return mode;
            }
        }
        return GUARDS_ONLY; // Default fallback
    }
}
