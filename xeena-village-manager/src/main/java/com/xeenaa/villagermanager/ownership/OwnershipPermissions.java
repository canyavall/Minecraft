package com.xeenaa.villagermanager.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Manages permission settings for villager interactions.
 *
 * <p>This class controls who can interact with owned villagers through:</p>
 * <ul>
 *   <li>Trade permissions (everyone, owner only, or whitelist)</li>
 *   <li>Player whitelist for selective access</li>
 *   <li>Future: Management delegation capabilities</li>
 * </ul>
 *
 * <p>Thread Safety: This class is not thread-safe. External synchronization
 * is required for concurrent access (handled by VillagerOwnershipManager).</p>
 *
 * @since 1.0.0
 */
public class OwnershipPermissions {
    private static final String NBT_KEY_TRADE_PERMISSION = "TradePermission";
    private static final String NBT_KEY_TRADE_WHITELIST = "TradeWhitelist";
    private static final String NBT_KEY_ALLOW_OTHERS_MANAGE = "AllowOthersToManage";

    private TradePermission tradePermission;
    private final Set<UUID> tradeWhitelist;
    private boolean allowOthersToManage;

    /**
     * Trade permission modes for owned villagers.
     */
    public enum TradePermission {
        /**
         * Anyone can trade with the villager.
         */
        EVERYONE("Everyone"),

        /**
         * Only the owner can trade with the villager.
         */
        OWNER_ONLY("Owner Only"),

        /**
         * Only whitelisted players (and the owner) can trade.
         */
        WHITELIST("Whitelist");

        private final String displayName;

        TradePermission(String displayName) {
            this.displayName = displayName;
        }

        /**
         * Gets the display name for UI presentation.
         *
         * @return the human-readable display name
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Parses a trade permission from a string name.
         *
         * @param name the permission name (case-insensitive)
         * @return the matching TradePermission, or EVERYONE if invalid
         */
        public static TradePermission fromString(String name) {
            if (name == null) {
                return EVERYONE;
            }

            for (TradePermission permission : values()) {
                if (permission.name().equalsIgnoreCase(name)) {
                    return permission;
                }
            }

            XeenaaVillagerManager.LOGGER.warn("Invalid trade permission: {}, defaulting to EVERYONE", name);
            return EVERYONE;
        }
    }

    /**
     * Creates default permissions with public trade access.
     */
    public OwnershipPermissions() {
        this.tradePermission = TradePermission.EVERYONE;
        this.tradeWhitelist = new HashSet<>();
        this.allowOthersToManage = false;

        XeenaaVillagerManager.LOGGER.debug("Created default ownership permissions");
    }

    /**
     * Creates permissions with specified settings.
     *
     * @param tradePermission the initial trade permission mode
     * @param allowOthersToManage whether to allow management delegation
     */
    public OwnershipPermissions(TradePermission tradePermission, boolean allowOthersToManage) {
        this.tradePermission = tradePermission != null ? tradePermission : TradePermission.EVERYONE;
        this.tradeWhitelist = new HashSet<>();
        this.allowOthersToManage = allowOthersToManage;

        XeenaaVillagerManager.LOGGER.debug("Created ownership permissions: trade={}, delegate={}",
            this.tradePermission, allowOthersToManage);
    }

    /**
     * Checks if a player can trade with the villager based on current permissions.
     *
     * <p>Trade access is granted if:</p>
     * <ul>
     *   <li>Permission is EVERYONE</li>
     *   <li>Player is the owner (always allowed)</li>
     *   <li>Permission is WHITELIST and player is whitelisted</li>
     * </ul>
     *
     * @param player the player attempting to trade
     * @param ownerUUID the UUID of the villager's owner
     * @return true if trading is allowed, false otherwise
     * @throws IllegalArgumentException if player or ownerUUID is null
     */
    public boolean canTrade(ServerPlayerEntity player, UUID ownerUUID) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (ownerUUID == null) {
            throw new IllegalArgumentException("Owner UUID cannot be null");
        }

        UUID playerUUID = player.getUuid();

        // Owner always has access
        if (playerUUID.equals(ownerUUID)) {
            XeenaaVillagerManager.LOGGER.debug("Trade allowed: player {} is owner", playerUUID);
            return true;
        }

        // Check permission mode
        boolean allowed = switch (tradePermission) {
            case EVERYONE -> {
                XeenaaVillagerManager.LOGGER.debug("Trade allowed: EVERYONE mode");
                yield true;
            }
            case OWNER_ONLY -> {
                XeenaaVillagerManager.LOGGER.debug("Trade denied: OWNER_ONLY mode for player {}", playerUUID);
                yield false;
            }
            case WHITELIST -> {
                boolean inWhitelist = tradeWhitelist.contains(playerUUID);
                XeenaaVillagerManager.LOGGER.debug("Trade {}: WHITELIST mode for player {}",
                    inWhitelist ? "allowed" : "denied", playerUUID);
                yield inWhitelist;
            }
        };

        return allowed;
    }

    /**
     * Adds a player to the trade whitelist.
     *
     * <p>Note: This only affects behavior when permission mode is WHITELIST.</p>
     *
     * @param playerUUID the UUID of the player to whitelist
     * @throws IllegalArgumentException if playerUUID is null
     */
    public void addToWhitelist(UUID playerUUID) {
        if (playerUUID == null) {
            throw new IllegalArgumentException("Player UUID cannot be null");
        }

        if (tradeWhitelist.add(playerUUID)) {
            XeenaaVillagerManager.LOGGER.info("Added player {} to trade whitelist", playerUUID);
        } else {
            XeenaaVillagerManager.LOGGER.debug("Player {} already in trade whitelist", playerUUID);
        }
    }

    /**
     * Removes a player from the trade whitelist.
     *
     * @param playerUUID the UUID of the player to remove
     * @throws IllegalArgumentException if playerUUID is null
     */
    public void removeFromWhitelist(UUID playerUUID) {
        if (playerUUID == null) {
            throw new IllegalArgumentException("Player UUID cannot be null");
        }

        if (tradeWhitelist.remove(playerUUID)) {
            XeenaaVillagerManager.LOGGER.info("Removed player {} from trade whitelist", playerUUID);
        } else {
            XeenaaVillagerManager.LOGGER.debug("Player {} not in trade whitelist", playerUUID);
        }
    }

    /**
     * Checks if a player is in the trade whitelist.
     *
     * @param playerUUID the UUID to check
     * @return true if the player is whitelisted, false otherwise
     */
    public boolean isWhitelisted(UUID playerUUID) {
        if (playerUUID == null) {
            return false;
        }
        return tradeWhitelist.contains(playerUUID);
    }

    /**
     * Clears all players from the trade whitelist.
     */
    public void clearWhitelist() {
        int size = tradeWhitelist.size();
        tradeWhitelist.clear();
        XeenaaVillagerManager.LOGGER.info("Cleared {} players from trade whitelist", size);
    }

    /**
     * Gets an immutable copy of the trade whitelist.
     *
     * @return a new set containing all whitelisted player UUIDs
     */
    public Set<UUID> getWhitelist() {
        return new HashSet<>(tradeWhitelist);
    }

    /**
     * Gets the current trade permission mode.
     *
     * @return the trade permission mode
     */
    public TradePermission getTradePermission() {
        return tradePermission;
    }

    /**
     * Sets the trade permission mode.
     *
     * @param tradePermission the new permission mode
     * @throws IllegalArgumentException if tradePermission is null
     */
    public void setTradePermission(TradePermission tradePermission) {
        if (tradePermission == null) {
            throw new IllegalArgumentException("Trade permission cannot be null");
        }

        TradePermission old = this.tradePermission;
        this.tradePermission = tradePermission;

        XeenaaVillagerManager.LOGGER.info("Changed trade permission from {} to {}", old, tradePermission);
    }

    /**
     * Checks if management delegation is allowed.
     *
     * @return true if others can manage this villager, false otherwise
     */
    public boolean isAllowOthersToManage() {
        return allowOthersToManage;
    }

    /**
     * Sets whether management delegation is allowed.
     *
     * @param allowOthersToManage true to allow delegation, false otherwise
     */
    public void setAllowOthersToManage(boolean allowOthersToManage) {
        this.allowOthersToManage = allowOthersToManage;
        XeenaaVillagerManager.LOGGER.debug("Set allowOthersToManage to {}", allowOthersToManage);
    }

    /**
     * Serializes permissions to NBT format.
     *
     * @return NBT compound containing all permission data
     */
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        // Save trade permission
        nbt.putString(NBT_KEY_TRADE_PERMISSION, tradePermission.name());

        // Save whitelist
        NbtList whitelistNbt = new NbtList();
        for (UUID uuid : tradeWhitelist) {
            whitelistNbt.add(NbtString.of(uuid.toString()));
        }
        nbt.put(NBT_KEY_TRADE_WHITELIST, whitelistNbt);

        // Save management delegation
        nbt.putBoolean(NBT_KEY_ALLOW_OTHERS_MANAGE, allowOthersToManage);

        XeenaaVillagerManager.LOGGER.debug("Serialized permissions: mode={}, whitelist_size={}",
            tradePermission, tradeWhitelist.size());

        return nbt;
    }

    /**
     * Deserializes permissions from NBT format.
     *
     * @param nbt the NBT compound to read from
     * @return a new OwnershipPermissions instance with loaded data
     * @throws IllegalArgumentException if nbt is null
     */
    public static OwnershipPermissions fromNbt(NbtCompound nbt) {
        if (nbt == null) {
            throw new IllegalArgumentException("NBT compound cannot be null");
        }

        OwnershipPermissions permissions = new OwnershipPermissions();

        // Load trade permission
        if (nbt.contains(NBT_KEY_TRADE_PERMISSION, NbtElement.STRING_TYPE)) {
            String permissionName = nbt.getString(NBT_KEY_TRADE_PERMISSION);
            permissions.tradePermission = TradePermission.fromString(permissionName);
        }

        // Load whitelist
        if (nbt.contains(NBT_KEY_TRADE_WHITELIST, NbtElement.LIST_TYPE)) {
            NbtList whitelistNbt = nbt.getList(NBT_KEY_TRADE_WHITELIST, NbtElement.STRING_TYPE);
            for (int i = 0; i < whitelistNbt.size(); i++) {
                try {
                    UUID uuid = UUID.fromString(whitelistNbt.getString(i));
                    permissions.tradeWhitelist.add(uuid);
                } catch (IllegalArgumentException e) {
                    XeenaaVillagerManager.LOGGER.error("Failed to parse whitelisted UUID: {}",
                        whitelistNbt.getString(i), e);
                }
            }
        }

        // Load management delegation
        if (nbt.contains(NBT_KEY_ALLOW_OTHERS_MANAGE, NbtElement.BYTE_TYPE)) {
            permissions.allowOthersToManage = nbt.getBoolean(NBT_KEY_ALLOW_OTHERS_MANAGE);
        }

        XeenaaVillagerManager.LOGGER.debug("Deserialized permissions: mode={}, whitelist_size={}",
            permissions.tradePermission, permissions.tradeWhitelist.size());

        return permissions;
    }

    @Override
    public String toString() {
        return "OwnershipPermissions{" +
            "tradePermission=" + tradePermission +
            ", whitelistSize=" + tradeWhitelist.size() +
            ", allowOthersToManage=" + allowOthersToManage +
            '}';
    }
}
