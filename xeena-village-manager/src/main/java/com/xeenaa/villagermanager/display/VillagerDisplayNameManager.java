package com.xeenaa.villagermanager.display;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.config.ModConfig;
import com.xeenaa.villagermanager.data.GuardData;
import com.xeenaa.villagermanager.data.GuardDataManager;
import com.xeenaa.villagermanager.data.rank.GuardRank;
import com.xeenaa.villagermanager.profession.ModProfessions;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

/**
 * Centralized manager for all villager display name logic.
 * Handles both guard rank displays and regular villager profession/level displays.
 */
public class VillagerDisplayNameManager {

    /**
     * Updates the display name for a villager based on the current configuration.
     * This method checks the config and determines whether to show display info.
     *
     * @param villager the villager entity to update
     */
    public static void updateVillagerDisplay(VillagerEntity villager) {
        VillagerDisplayMode mode = ModConfig.getInstance().getVillagerDisplayMode();
        boolean isGuardVillager = isGuard(villager);

        XeenaaVillagerManager.LOGGER.info(
            "[VillagerDisplayNameManager] updateVillagerDisplay called for {}: mode={}, isGuard={}",
            villager.getUuid(),
            mode,
            isGuardVillager
        );

        switch (mode) {
            case NONE:
                XeenaaVillagerManager.LOGGER.info("[VillagerDisplayNameManager] Mode is NONE, clearing display");
                clearVillagerDisplay(villager);
                break;

            case GUARDS_ONLY:
                if (isGuardVillager) {
                    XeenaaVillagerManager.LOGGER.info("[VillagerDisplayNameManager] Mode is GUARDS_ONLY, updating guard display");
                    updateGuardDisplay(villager);
                } else {
                    XeenaaVillagerManager.LOGGER.info("[VillagerDisplayNameManager] Mode is GUARDS_ONLY, clearing non-guard display");
                    clearVillagerDisplay(villager);
                }
                break;

            case SHOW_ALL:
                if (isGuardVillager) {
                    XeenaaVillagerManager.LOGGER.info("[VillagerDisplayNameManager] Mode is SHOW_ALL, updating guard display");
                    updateGuardDisplay(villager);
                } else {
                    XeenaaVillagerManager.LOGGER.info("[VillagerDisplayNameManager] Mode is SHOW_ALL, updating regular villager display");
                    updateRegularVillagerDisplay(villager);
                }
                break;
        }
    }

    /**
     * Clears the custom name display for a villager.
     *
     * @param villager the villager entity
     */
    public static void clearVillagerDisplay(VillagerEntity villager) {
        villager.setCustomName(null);
        villager.setCustomNameVisible(false);
    }

    /**
     * Checks if a villager should have a display name based on current config.
     *
     * @param villager the villager entity
     * @return true if display should be shown
     */
    public static boolean shouldShowDisplay(VillagerEntity villager) {
        VillagerDisplayMode mode = ModConfig.getInstance().getVillagerDisplayMode();

        return switch (mode) {
            case NONE -> false;
            case GUARDS_ONLY -> isGuard(villager);
            case SHOW_ALL -> true;
        };
    }

    /**
     * Updates the display for a guard villager (rank + tier stars).
     *
     * @param villager the guard villager entity
     */
    private static void updateGuardDisplay(VillagerEntity villager) {
        World world = villager.getWorld();
        if (world == null) return;

        GuardDataManager guardManager = GuardDataManager.get(world);
        GuardData guardData = guardManager.getGuardData(villager.getUuid());

        if (guardData == null) {
            XeenaaVillagerManager.LOGGER.warn("Guard villager {} has no GuardData", villager.getUuid());
            return;
        }

        GuardRank rank = guardData.getRankData().getCurrentRank();
        Text displayName = createGuardDisplayName(rank);

        villager.setCustomName(displayName);
        villager.setCustomNameVisible(true);

        XeenaaVillagerManager.LOGGER.debug("Updated guard display for {} to: {}",
            villager.getUuid(), displayName.getString());
    }

    /**
     * Updates the display for a regular villager (profession + level stars).
     *
     * @param villager the regular villager entity
     */
    private static void updateRegularVillagerDisplay(VillagerEntity villager) {
        VillagerData data = villager.getVillagerData();
        VillagerProfession profession = data.getProfession();
        int level = data.getLevel();

        XeenaaVillagerManager.LOGGER.info(
            "[VillagerDisplayNameManager] updateRegularVillagerDisplay called for {}: profession={}, level={}",
            villager.getUuid(),
            profession.id(),
            level
        );

        // Don't show display for nitwits or unemployed
        if (profession == net.minecraft.village.VillagerProfession.NONE ||
            profession == net.minecraft.village.VillagerProfession.NITWIT) {
            XeenaaVillagerManager.LOGGER.info(
                "[VillagerDisplayNameManager] Clearing display for NONE/NITWIT villager {}",
                villager.getUuid()
            );
            clearVillagerDisplay(villager);
            return;
        }

        Text displayName = createRegularVillagerDisplayName(profession, level);

        villager.setCustomName(displayName);
        villager.setCustomNameVisible(true);

        XeenaaVillagerManager.LOGGER.info(
            "[VillagerDisplayNameManager] Updated villager display for {} to: '{}'",
            villager.getUuid(),
            displayName.getString()
        );
    }

    /**
     * Creates the display name for a guard (same logic as GuardData.createRankDisplayName).
     *
     * @param rank the guard's current rank
     * @return formatted Text component with rank name, stars, and color
     */
    private static Text createGuardDisplayName(GuardRank rank) {
        String rankName = rank.getDisplayName();
        int tier = rank.getTier();

        // Create display format: "Rank Name ⭐⭐⭐⭐"
        String displayText;
        if (tier > 0) {
            String stars = "⭐".repeat(tier);
            displayText = rankName + " " + stars;
        } else {
            // Tier 0 (Recruit) - no stars
            displayText = rankName;
        }

        // Get color based on tier and path
        int color = getGuardRankColor(rank);

        // Build formatted text with color
        return Text.literal(displayText)
            .styled(style -> style.withColor(TextColor.fromRgb(color)));
    }

    /**
     * Creates the display name for a regular villager.
     * Format: "[Profession] ★★★★★" (level stars)
     *
     * @param profession the villager's profession
     * @param level the villager's level (1-5)
     * @return formatted Text component with profession name, stars, and color
     */
    private static Text createRegularVillagerDisplayName(VillagerProfession profession, int level) {
        // Get profession name from registry
        Identifier professionId = Registries.VILLAGER_PROFESSION.getId(profession);
        String professionName = profession.id(); // Use simple ID for now (e.g., "farmer")

        // Capitalize first letter
        if (professionName != null && !professionName.isEmpty()) {
            professionName = professionName.substring(0, 1).toUpperCase() + professionName.substring(1);
        } else {
            professionName = "Villager";
        }

        // Create stars based on level (1-5)
        // Level 1 (Novice) = no stars, Level 2-5 = 1-4 stars
        String stars = level > 1 ? " " + "★".repeat(Math.min(level - 1, 4)) : "";
        String displayText = professionName + stars;

        // Get color based on level
        int color = getVillagerLevelColor(level);

        // Build formatted text with color
        return Text.literal(displayText)
            .styled(style -> style.withColor(TextColor.fromRgb(color)));
    }

    /**
     * Gets the RGB color code for a guard rank based on tier and path.
     * (Same logic as GuardData.getRankColor)
     */
    private static int getGuardRankColor(GuardRank rank) {
        int tier = rank.getTier();
        com.xeenaa.villagermanager.data.rank.GuardPath path = rank.getPath();

        return switch (tier) {
            case 0 -> 0xF0F0F0; // Tier 0 (Recruit): Soft White
            case 1 -> 0xB8B8B8; // Tier 1: Soft Gray
            case 2 -> 0xFFDD88; // Tier 2: Soft Yellow/Cream
            case 3 -> 0xDDA055; // Tier 3: Soft Gold/Bronze
            case 4 -> {
                // Tier 4: Soft Teal for Melee, Soft Lavender for Ranged
                if (path != null && path.getId().equals("ranged")) {
                    yield 0xCC88CC; // Soft Lavender for Sharpshooter
                } else {
                    yield 0x88CCCC; // Soft Teal for Knight
                }
            }
            default -> 0xF0F0F0; // Fallback to soft white
        };
    }

    /**
     * Gets the RGB color code for a regular villager based on level.
     * Color scheme:
     * - Level 1 (Novice): Soft White
     * - Level 2 (Apprentice): Soft Green
     * - Level 3 (Journeyman): Soft Yellow
     * - Level 4 (Expert): Soft Gold
     * - Level 5 (Master): Soft Teal
     */
    private static int getVillagerLevelColor(int level) {
        return switch (level) {
            case 1 -> 0xF0F0F0; // Novice: Soft White
            case 2 -> 0x88CC88; // Apprentice: Soft Green
            case 3 -> 0xFFDD88; // Journeyman: Soft Yellow/Cream
            case 4 -> 0xDDA055; // Expert: Soft Gold/Bronze
            case 5 -> 0x88CCCC; // Master: Soft Teal
            default -> 0xB8B8B8; // Fallback: Soft Gray
        };
    }

    /**
     * Checks if a villager is a guard.
     *
     * @param villager the villager entity
     * @return true if the villager is a guard
     */
    private static boolean isGuard(VillagerEntity villager) {
        return villager.getVillagerData().getProfession() == ModProfessions.GUARD;
    }
}
