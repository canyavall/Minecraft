package com.xeenaa.villagermanager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.display.VillagerDisplayMode;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration system for Xeenaa Villager Manager mod.
 *
 * <p>This config file controls mod-wide settings that affect all villagers.
 * Individual guard behavior settings (modes, patrol radius, etc.) are stored
 * per-guard in {@link com.xeenaa.villagermanager.data.GuardData}.</p>
 *
 * <p><b>Configuration File Location:</b> {@code config/xeenaa-villager-manager.json}</p>
 *
 * <p><b>Settings Summary:</b></p>
 * <ul>
 *   <li><b>blacklisted_professions</b>: List of profession IDs that cannot be assigned</li>
 *   <li><b>villager_display_mode</b>: Controls display names above villagers</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class ModConfig {
    private static final String CONFIG_FILE_NAME = "xeenaa-villager-manager.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static ModConfig INSTANCE;

    // ========================================
    // Configuration Fields
    // ========================================

    /**
     * List of profession IDs that are blacklisted and cannot be assigned to villagers.
     *
     * <p><b>Format:</b> {@code "namespace:profession_id"} (e.g., {@code "minecraft:nitwit"})</p>
     *
     * <p><b>Default:</b> {@code ["minecraft:nitwit"]}</p>
     *
     * <p><b>Usage:</b> When opening the profession selection GUI, blacklisted professions
     * will not appear in the available options.</p>
     *
     * <p><b>Restart Required:</b> No - changes apply immediately when GUI is opened</p>
     */
    public List<String> blacklisted_professions = new ArrayList<>();

    /**
     * Controls display names shown above villagers.
     *
     * <p><b>Options:</b></p>
     * <ul>
     *   <li><b>none</b> - No display names shown</li>
     *   <li><b>guards_only</b> - Only guards show rank/tier displays</li>
     *   <li><b>show_all</b> - All villagers show profession/level (guards show rank)</li>
     * </ul>
     *
     * <p><b>Default:</b> {@code "show_all"}</p>
     *
     * <p><b>Example Display Formats:</b></p>
     * <ul>
     *   <li>Regular villagers: "Librarian ★★★★" (profession + level stars)</li>
     *   <li>Guards: "Knight ⭐⭐⭐⭐" (rank + tier stars)</li>
     * </ul>
     *
     * <p><b>Restart Required:</b> No - changes apply to newly loaded/spawned villagers</p>
     */
    public String villager_display_mode = "show_all";

    // ========================================
    // Constructor & Initialization
    // ========================================

    /**
     * Private constructor for singleton pattern.
     * Initializes default configuration values.
     */
    private ModConfig() {
        // Default blacklist - exclude nitwit profession by default
        blacklisted_professions.add("minecraft:nitwit");
    }

    /**
     * Gets the singleton configuration instance.
     * Automatically loads from file if not already loaded.
     *
     * @return the mod configuration instance
     */
    public static ModConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = loadConfig();
        }
        return INSTANCE;
    }

    // ========================================
    // Blacklist Management
    // ========================================

    /**
     * Checks if a profession is blacklisted and cannot be assigned.
     *
     * @param professionId the profession identifier to check
     * @return {@code true} if the profession is blacklisted, {@code false} otherwise
     */
    public boolean isProfessionBlacklisted(Identifier professionId) {
        return blacklisted_professions.contains(professionId.toString());
    }

    /**
     * Gets an immutable copy of all blacklisted profession identifiers.
     *
     * @return list of blacklisted profession IDs (format: "namespace:id")
     */
    public List<String> getBlacklistedProfessions() {
        return new ArrayList<>(blacklisted_professions);
    }

    /**
     * Adds a profession to the blacklist and saves the configuration.
     * If the profession is already blacklisted, no action is taken.
     *
     * @param professionId the profession ID to blacklist (format: "namespace:id")
     */
    public void addBlacklistedProfession(String professionId) {
        if (!blacklisted_professions.contains(professionId)) {
            blacklisted_professions.add(professionId);
            saveConfig();
        }
    }

    /**
     * Removes a profession from the blacklist and saves the configuration.
     * If the profession is not blacklisted, no action is taken.
     *
     * @param professionId the profession ID to un-blacklist (format: "namespace:id")
     */
    public void removeBlacklistedProfession(String professionId) {
        if (blacklisted_professions.remove(professionId)) {
            saveConfig();
        }
    }

    // ========================================
    // Display Mode Management
    // ========================================

    /**
     * Gets the current villager display mode configuration.
     *
     * @return the display mode enum value
     * @see VillagerDisplayMode
     */
    public VillagerDisplayMode getVillagerDisplayMode() {
        return VillagerDisplayMode.fromId(villager_display_mode);
    }

    /**
     * Sets the villager display mode and saves the configuration.
     *
     * @param mode the new display mode to set
     * @see VillagerDisplayMode
     */
    public void setVillagerDisplayMode(VillagerDisplayMode mode) {
        this.villager_display_mode = mode.getId();
        saveConfig();
    }

    // ========================================
    // Persistence (Load/Save)
    // ========================================

    /**
     * Loads configuration from file.
     * If the file doesn't exist, creates a new default configuration.
     * If loading fails, returns default configuration and logs error.
     *
     * @return the loaded (or default) configuration
     */
    private static ModConfig loadConfig() {
        Path configPath = getConfigPath();

        if (!Files.exists(configPath)) {
            XeenaaVillagerManager.LOGGER.info("Config file not found, creating default configuration");
            ModConfig defaultConfig = new ModConfig();
            defaultConfig.saveConfig();
            return defaultConfig;
        }

        try {
            String json = Files.readString(configPath);
            ModConfig config = GSON.fromJson(json, ModConfig.class);
            XeenaaVillagerManager.LOGGER.info("Loaded configuration with {} blacklisted professions",
                config.blacklisted_professions.size());
            return config;
        } catch (IOException e) {
            XeenaaVillagerManager.LOGGER.error("Failed to load config file, using defaults", e);
            return new ModConfig();
        }
    }

    /**
     * Saves the current configuration to file.
     * Creates the config directory if it doesn't exist.
     * Logs error if save fails but does not throw exception.
     */
    public void saveConfig() {
        Path configPath = getConfigPath();

        try {
            // Ensure config directory exists
            Files.createDirectories(configPath.getParent());

            String json = GSON.toJson(this);
            Files.writeString(configPath, json);
            XeenaaVillagerManager.LOGGER.info("Saved configuration to {}", configPath);
        } catch (IOException e) {
            XeenaaVillagerManager.LOGGER.error("Failed to save config file", e);
        }
    }

    /**
     * Gets the path to the configuration file.
     *
     * @return path to {@code config/xeenaa-villager-manager.json}
     */
    private static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);
    }

    /**
     * Reloads configuration from file, discarding current in-memory values.
     * Useful for applying changes made by external editors.
     */
    public static void reload() {
        INSTANCE = loadConfig();
        XeenaaVillagerManager.LOGGER.info("Configuration reloaded");
    }
}
