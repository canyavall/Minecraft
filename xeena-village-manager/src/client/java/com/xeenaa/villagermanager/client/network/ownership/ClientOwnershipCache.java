package com.xeenaa.villagermanager.client.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.ownership.OwnershipPermissions;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side cache for villager ownership data.
 *
 * <p>This singleton cache stores ownership information received from the server
 * via {@link com.xeenaa.villagermanager.network.ownership.OwnershipSyncPacket}.
 * It provides quick lookups for GUI rendering and interaction checks.</p>
 *
 * <p>Cache Lifecycle:</p>
 * <ul>
 *   <li>Updated when receiving ownership sync packets from server</li>
 *   <li>Cleared when disconnecting from a server</li>
 *   <li>Automatically cleaned up for unloaded villagers (optional)</li>
 * </ul>
 *
 * <p>Thread Safety: Uses ConcurrentHashMap for safe concurrent access
 * from network and render threads.</p>
 *
 * @since 1.0.0
 */
public class ClientOwnershipCache {

    /**
     * Singleton instance.
     */
    private static final ClientOwnershipCache INSTANCE = new ClientOwnershipCache();

    /**
     * Cache storage mapping villager UUIDs to ownership data.
     */
    private final Map<UUID, CachedOwnership> ownershipCache = new ConcurrentHashMap<>();

    /**
     * Private constructor for singleton pattern.
     */
    private ClientOwnershipCache() {
        XeenaaVillagerManager.LOGGER.debug("Initialized ClientOwnershipCache");
    }

    /**
     * Gets the singleton instance.
     *
     * @return the cache instance
     */
    public static ClientOwnershipCache getInstance() {
        return INSTANCE;
    }

    /**
     * Updates ownership data for a villager.
     *
     * @param villagerUUID the villager UUID
     * @param ownerUUID the owner's UUID
     * @param ownerName the owner's display name
     * @param permissionsNbt the permissions as NBT (null for defaults)
     */
    public void updateOwnership(UUID villagerUUID, UUID ownerUUID, String ownerName, @Nullable NbtCompound permissionsNbt) {
        if (villagerUUID == null || ownerUUID == null || ownerName == null) {
            XeenaaVillagerManager.LOGGER.warn("Cannot update ownership with null parameters");
            return;
        }

        OwnershipPermissions permissions;
        if (permissionsNbt != null) {
            permissions = OwnershipPermissions.fromNbt(permissionsNbt);
        } else {
            permissions = new OwnershipPermissions();
        }

        CachedOwnership cached = new CachedOwnership(ownerUUID, ownerName, permissions);
        ownershipCache.put(villagerUUID, cached);

        XeenaaVillagerManager.LOGGER.debug("Updated ownership cache: villager {} -> owner {} ({})",
            villagerUUID, ownerUUID, ownerName);
    }

    /**
     * Removes ownership data for a villager (marks as unowned).
     *
     * @param villagerUUID the villager UUID
     */
    public void removeOwnership(UUID villagerUUID) {
        if (villagerUUID == null) {
            return;
        }

        ownershipCache.remove(villagerUUID);

        XeenaaVillagerManager.LOGGER.debug("Removed ownership cache entry for villager {}", villagerUUID);
    }

    /**
     * Gets ownership data for a villager.
     *
     * @param villagerUUID the villager UUID
     * @return the cached ownership data, or null if unowned or not cached
     */
    @Nullable
    public CachedOwnership getOwnership(UUID villagerUUID) {
        if (villagerUUID == null) {
            return null;
        }

        return ownershipCache.get(villagerUUID);
    }

    /**
     * Checks if a villager has an owner.
     *
     * @param villagerUUID the villager UUID
     * @return true if owned, false if unowned or not cached
     */
    public boolean hasOwner(UUID villagerUUID) {
        return getOwnership(villagerUUID) != null;
    }

    /**
     * Gets the owner's display name for a villager.
     *
     * @param villagerUUID the villager UUID
     * @return the owner's name, or null if unowned
     */
    @Nullable
    public String getOwnerName(UUID villagerUUID) {
        CachedOwnership ownership = getOwnership(villagerUUID);
        return ownership != null ? ownership.ownerName() : null;
    }

    /**
     * Gets the owner's UUID for a villager.
     *
     * @param villagerUUID the villager UUID
     * @return the owner's UUID, or null if unowned
     */
    @Nullable
    public UUID getOwnerUUID(UUID villagerUUID) {
        CachedOwnership ownership = getOwnership(villagerUUID);
        return ownership != null ? ownership.ownerUUID() : null;
    }

    /**
     * Clears all cached ownership data.
     *
     * <p>This should be called when disconnecting from a server to prevent
     * stale data from persisting.</p>
     */
    public void clearAll() {
        int size = ownershipCache.size();
        ownershipCache.clear();

        XeenaaVillagerManager.LOGGER.info("Cleared {} cached ownership entries", size);
    }

    /**
     * Gets the total number of cached ownership entries.
     *
     * @return the cache size
     */
    public int getCacheSize() {
        return ownershipCache.size();
    }

    /**
     * Cached ownership data record.
     *
     * @param ownerUUID the owner's UUID
     * @param ownerName the owner's display name
     * @param permissions the ownership permissions
     */
    public record CachedOwnership(
        UUID ownerUUID,
        String ownerName,
        OwnershipPermissions permissions
    ) {}
}
