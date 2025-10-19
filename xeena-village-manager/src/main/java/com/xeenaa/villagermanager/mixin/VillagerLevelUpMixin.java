package com.xeenaa.villagermanager.mixin;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.config.ModConfig;
import com.xeenaa.villagermanager.display.VillagerDisplayNameManager;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to update villager display names when their data changes (level up, profession change).
 *
 * <p>This mixin intercepts the {@code setVillagerData()} method to detect when a villager's
 * level or profession changes, and updates their display name accordingly based on the
 * configured display mode.</p>
 *
 * <p>This ensures that when villagers level up through trading, their display name
 * (e.g., "Librarian ★★★") updates immediately to reflect the new level.</p>
 *
 * @since 1.0.0
 */
@Mixin(VillagerEntity.class)
public abstract class VillagerLevelUpMixin {

    @Shadow
    public abstract VillagerData getVillagerData();

    @Unique
    private VillagerData xeenaa$previousData = null;

    /**
     * Captures the villager data BEFORE it changes.
     */
    @Inject(method = "setVillagerData", at = @At("HEAD"))
    private void captureOldData(VillagerData villagerData, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        if (!villager.getWorld().isClient()) {
            // Store the current data before it gets changed
            xeenaa$previousData = this.getVillagerData();

            XeenaaVillagerManager.LOGGER.info(
                "[VillagerLevelUpMixin] BEFORE setVillagerData for {}: profession={}, level={}",
                villager.getUuid(),
                xeenaa$previousData.getProfession().id(),
                xeenaa$previousData.getLevel()
            );
        }
    }

    /**
     * Intercepts villager data changes to update display name when level or profession changes.
     *
     * <p>This method is called whenever a villager's data is updated, including:
     * <ul>
     *   <li>Level changes from trading (level 1 → 5)</li>
     *   <li>Profession changes (farmer → librarian)</li>
     *   <li>Biome type changes</li>
     * </ul>
     *
     * <p>The display name is updated based on {@link VillagerDisplayNameManager} configuration:
     * <ul>
     *   <li>GUARDS_ONLY: Only guards show rank display</li>
     *   <li>SHOW_ALL: All villagers show profession + level</li>
     *   <li>NONE: No villagers show display</li>
     * </ul>
     *
     * @param villagerData the new villager data being set
     * @param ci callback info for the mixin injection
     */
    @Inject(method = "setVillagerData", at = @At("TAIL"))
    private void onVillagerDataChanged(VillagerData villagerData, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;

        // Only update display on server side to avoid client-side desync
        if (!villager.getWorld().isClient() && xeenaa$previousData != null) {
            VillagerData oldData = xeenaa$previousData;
            VillagerData newData = villagerData;

            XeenaaVillagerManager.LOGGER.info(
                "[VillagerLevelUpMixin] AFTER setVillagerData for {}: profession={}, level={}",
                villager.getUuid(),
                newData.getProfession().id(),
                newData.getLevel()
            );

            // Check if level or profession changed
            boolean levelChanged = oldData.getLevel() != newData.getLevel();
            boolean professionChanged = !oldData.getProfession().equals(newData.getProfession());

            XeenaaVillagerManager.LOGGER.info(
                "[VillagerLevelUpMixin] Change detection: levelChanged={}, professionChanged={}",
                levelChanged,
                professionChanged
            );

            if (levelChanged || professionChanged) {
                XeenaaVillagerManager.LOGGER.info(
                    "[VillagerLevelUpMixin] Villager data CHANGED for {}: level {} -> {}, profession {} -> {}",
                    villager.getUuid(),
                    oldData.getLevel(),
                    newData.getLevel(),
                    oldData.getProfession().id(),
                    newData.getProfession().id()
                );

                // Log current display mode
                var displayMode = ModConfig.getInstance().getVillagerDisplayMode();
                XeenaaVillagerManager.LOGGER.info(
                    "[VillagerLevelUpMixin] Current display mode: {}",
                    displayMode
                );

                // Update display name based on current configuration
                VillagerDisplayNameManager.updateVillagerDisplay(villager);

                XeenaaVillagerManager.LOGGER.info(
                    "[VillagerLevelUpMixin] Updated display for villager {}",
                    villager.getUuid()
                );
            } else {
                XeenaaVillagerManager.LOGGER.info(
                    "[VillagerLevelUpMixin] No changes detected for villager {}",
                    villager.getUuid()
                );
            }

            // Clear the previous data
            xeenaa$previousData = null;
        }
    }
}
