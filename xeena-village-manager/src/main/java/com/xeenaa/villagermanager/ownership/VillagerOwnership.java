package com.xeenaa.villagermanager.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

/**
 * Represents ownership data for a single villager.
 *
 * <p>This class encapsulates all ownership-related information including:</p>
 * <ul>
 *   <li>Owner identification (UUID and cached name)</li>
 *   <li>Binding timestamp for record-keeping</li>
 *   <li>Permission settings for interactions</li>
 *   <li>Lock status to prevent unauthorized access</li>
 * </ul>
 *
 * <p>Thread Safety: This class is not thread-safe. External synchronization
 * is required for concurrent access (handled by VillagerOwnershipManager).</p>
 *
 * @since 1.0.0
 */
public class VillagerOwnership {
    private static final String NBT_KEY_VILLAGER_UUID = "VillagerUUID";
    private static final String NBT_KEY_OWNER_UUID = "OwnerUUID";
    private static final String NBT_KEY_OWNER_NAME = "OwnerName";
    private static final String NBT_KEY_BIND_TIMESTAMP = "BindTimestamp";
    private static final String NBT_KEY_PERMISSIONS = "Permissions";
    private static final String NBT_KEY_LOCKED = "Locked";
    private static final String NBT_KEY_VERSION = "DataVersion";
    private static final int CURRENT_VERSION = 1;

    private final UUID villagerUUID;
    private final UUID ownerUUID;
    private final String ownerName;
    private final long bindTimestamp;
    private OwnershipPermissions permissions;
    private boolean locked;

    /**
     * Creates ownership data for a villager bound to a player.
     *
     * @param villagerUUID the UUID of the villager being owned
     * @param owner the player who will own the villager
     * @throws IllegalArgumentException if villagerUUID or owner is null
     */
    public VillagerOwnership(UUID villagerUUID, ServerPlayerEntity owner) {
        if (villagerUUID == null) {
            throw new IllegalArgumentException("Villager UUID cannot be null");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Owner player cannot be null");
        }

        this.villagerUUID = villagerUUID;
        this.ownerUUID = owner.getUuid();
        this.ownerName = owner.getName().getString();
        this.bindTimestamp = System.currentTimeMillis();
        this.permissions = new OwnershipPermissions();
        this.locked = false;

        XeenaaVillagerManager.LOGGER.info("Created ownership: villager={}, owner={} ({})",
            villagerUUID, ownerUUID, ownerName);
    }

    /**
     * Private constructor for deserialization.
     *
     * @param villagerUUID the villager UUID
     * @param ownerUUID the owner UUID
     * @param ownerName the cached owner name
     * @param bindTimestamp the original bind timestamp
     * @param permissions the permission settings
     * @param locked the lock status
     */
    private VillagerOwnership(UUID villagerUUID, UUID ownerUUID, String ownerName,
                             long bindTimestamp, OwnershipPermissions permissions, boolean locked) {
        this.villagerUUID = villagerUUID;
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.bindTimestamp = bindTimestamp;
        this.permissions = permissions;
        this.locked = locked;
    }

    /**
     * Checks if a player is the owner of this villager.
     *
     * @param player the player to check
     * @return true if the player is the owner, false otherwise
     * @throws IllegalArgumentException if player is null
     */
    public boolean isOwner(ServerPlayerEntity player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        boolean result = ownerUUID.equals(player.getUuid());
        XeenaaVillagerManager.LOGGER.debug("Ownership check: player={}, owner={}, match={}",
            player.getUuid(), ownerUUID, result);
        return result;
    }

    /**
     * Checks if a player can interact with this villager based on lock status and ownership.
     *
     * <p>Interaction is allowed if:</p>
     * <ul>
     *   <li>Villager is not locked, OR</li>
     *   <li>Player is the owner</li>
     * </ul>
     *
     * <p>Note: This method only checks general access. Specific interactions
     * (like trading) may have additional permission requirements.</p>
     *
     * @param player the player attempting to interact
     * @return true if interaction is allowed, false otherwise
     * @throws IllegalArgumentException if player is null
     */
    public boolean canInteract(ServerPlayerEntity player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        // Owner always has access
        if (isOwner(player)) {
            XeenaaVillagerManager.LOGGER.debug("Interaction allowed: player {} is owner", player.getUuid());
            return true;
        }

        // If locked, only owner can interact
        if (locked) {
            XeenaaVillagerManager.LOGGER.debug("Interaction denied: villager {} is locked", villagerUUID);
            return false;
        }

        // Not locked, allow interaction (specific permissions checked elsewhere)
        XeenaaVillagerManager.LOGGER.debug("Interaction allowed: villager {} not locked", villagerUUID);
        return true;
    }

    /**
     * Gets the villager's UUID.
     *
     * @return the villager UUID
     */
    public UUID getVillagerUUID() {
        return villagerUUID;
    }

    /**
     * Gets the owner's UUID.
     *
     * @return the owner UUID
     */
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    /**
     * Gets the cached owner name.
     *
     * <p>Note: This is cached at bind time and may become outdated if
     * the player changes their name.</p>
     *
     * @return the owner's display name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Gets the timestamp when ownership was established.
     *
     * @return milliseconds since epoch when villager was bound
     */
    public long getBindTimestamp() {
        return bindTimestamp;
    }

    /**
     * Gets the permission settings for this ownership.
     *
     * @return the ownership permissions
     */
    public OwnershipPermissions getPermissions() {
        return permissions;
    }

    /**
     * Sets new permission settings.
     *
     * @param permissions the new permissions
     * @throws IllegalArgumentException if permissions is null
     */
    public void setPermissions(OwnershipPermissions permissions) {
        if (permissions == null) {
            throw new IllegalArgumentException("Permissions cannot be null");
        }

        this.permissions = permissions;
        XeenaaVillagerManager.LOGGER.debug("Updated permissions for villager {}: {}",
            villagerUUID, permissions);
    }

    /**
     * Checks if the villager is locked.
     *
     * <p>When locked, only the owner can interact with the villager.</p>
     *
     * @return true if locked, false otherwise
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the lock status of the villager.
     *
     * @param locked true to lock, false to unlock
     */
    public void setLocked(boolean locked) {
        boolean wasLocked = this.locked;
        this.locked = locked;

        if (wasLocked != locked) {
            XeenaaVillagerManager.LOGGER.info("Villager {} lock status changed: {} -> {}",
                villagerUUID, wasLocked, locked);
        }
    }

    /**
     * Serializes ownership data to NBT format.
     *
     * @return NBT compound containing all ownership data
     */
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        // Version for future compatibility
        nbt.putInt(NBT_KEY_VERSION, CURRENT_VERSION);

        // Core data
        nbt.putUuid(NBT_KEY_VILLAGER_UUID, villagerUUID);
        nbt.putUuid(NBT_KEY_OWNER_UUID, ownerUUID);
        nbt.putString(NBT_KEY_OWNER_NAME, ownerName);
        nbt.putLong(NBT_KEY_BIND_TIMESTAMP, bindTimestamp);
        nbt.putBoolean(NBT_KEY_LOCKED, locked);

        // Permissions
        nbt.put(NBT_KEY_PERMISSIONS, permissions.toNbt());

        XeenaaVillagerManager.LOGGER.debug("Serialized ownership for villager {}: owner={}, locked={}",
            villagerUUID, ownerUUID, locked);

        return nbt;
    }

    /**
     * Deserializes ownership data from NBT format.
     *
     * @param nbt the NBT compound to read from
     * @return a new VillagerOwnership instance with loaded data
     * @throws IllegalArgumentException if nbt is null or missing required fields
     */
    public static VillagerOwnership fromNbt(NbtCompound nbt) {
        if (nbt == null) {
            throw new IllegalArgumentException("NBT compound cannot be null");
        }

        // Check version
        int version = nbt.getInt(NBT_KEY_VERSION);
        if (version > CURRENT_VERSION) {
            XeenaaVillagerManager.LOGGER.warn("Loading ownership data from newer version: {} > {}",
                version, CURRENT_VERSION);
        }

        // Validate required fields
        if (!nbt.containsUuid(NBT_KEY_VILLAGER_UUID)) {
            throw new IllegalArgumentException("Missing required field: " + NBT_KEY_VILLAGER_UUID);
        }
        if (!nbt.containsUuid(NBT_KEY_OWNER_UUID)) {
            throw new IllegalArgumentException("Missing required field: " + NBT_KEY_OWNER_UUID);
        }
        if (!nbt.contains(NBT_KEY_OWNER_NAME, NbtElement.STRING_TYPE)) {
            throw new IllegalArgumentException("Missing required field: " + NBT_KEY_OWNER_NAME);
        }
        if (!nbt.contains(NBT_KEY_BIND_TIMESTAMP, NbtElement.LONG_TYPE)) {
            throw new IllegalArgumentException("Missing required field: " + NBT_KEY_BIND_TIMESTAMP);
        }

        // Load core data
        UUID villagerUUID = nbt.getUuid(NBT_KEY_VILLAGER_UUID);
        UUID ownerUUID = nbt.getUuid(NBT_KEY_OWNER_UUID);
        String ownerName = nbt.getString(NBT_KEY_OWNER_NAME);
        long bindTimestamp = nbt.getLong(NBT_KEY_BIND_TIMESTAMP);
        boolean locked = nbt.getBoolean(NBT_KEY_LOCKED);

        // Load permissions
        OwnershipPermissions permissions;
        if (nbt.contains(NBT_KEY_PERMISSIONS, NbtElement.COMPOUND_TYPE)) {
            permissions = OwnershipPermissions.fromNbt(nbt.getCompound(NBT_KEY_PERMISSIONS));
        } else {
            XeenaaVillagerManager.LOGGER.warn("Missing permissions for villager {}, using defaults", villagerUUID);
            permissions = new OwnershipPermissions();
        }

        XeenaaVillagerManager.LOGGER.debug("Deserialized ownership for villager {}: owner={}, locked={}",
            villagerUUID, ownerUUID, locked);

        return new VillagerOwnership(villagerUUID, ownerUUID, ownerName, bindTimestamp, permissions, locked);
    }

    @Override
    public String toString() {
        return "VillagerOwnership{" +
            "villagerUUID=" + villagerUUID +
            ", ownerUUID=" + ownerUUID +
            ", ownerName='" + ownerName + '\'' +
            ", bindTimestamp=" + bindTimestamp +
            ", locked=" + locked +
            ", permissions=" + permissions +
            '}';
    }
}
