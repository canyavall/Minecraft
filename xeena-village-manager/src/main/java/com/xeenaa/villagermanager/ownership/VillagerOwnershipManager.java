package com.xeenaa.villagermanager.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central manager for all villager ownership data with thread-safe persistence.
 *
 * <p>This singleton manager handles:</p>
 * <ul>
 *   <li>Ownership binding and unbinding operations</li>
 *   <li>Ownership transfer between players</li>
 *   <li>Permission checks for various interactions</li>
 *   <li>Persistent storage using Minecraft's PersistentState system</li>
 * </ul>
 *
 * <p>Thread Safety: Uses ConcurrentHashMap for thread-safe concurrent access.
 * All public methods are thread-safe.</p>
 *
 * <p>Data is automatically saved when modified and loaded on world initialization.</p>
 *
 * @since 1.0.0
 */
public class VillagerOwnershipManager extends PersistentState {
    private static final String DATA_NAME = "xeenaa_villager_ownership";
    private static final String NBT_KEY_OWNERSHIPS = "Ownerships";
    private static final String NBT_KEY_VERSION = "DataVersion";
    private static final int CURRENT_VERSION = 1;

    private final Map<UUID, VillagerOwnership> ownerships = new ConcurrentHashMap<>();

    private static final PersistentState.Type<VillagerOwnershipManager> type = new PersistentState.Type<>(
        VillagerOwnershipManager::new,
        VillagerOwnershipManager::fromNbt,
        null
    );

    /**
     * Gets the ownership manager for a server world.
     *
     * @param world the server world
     * @return the ownership manager instance
     * @throws IllegalStateException if called on client side
     */
    public static VillagerOwnershipManager get(ServerWorld world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        PersistentStateManager stateManager = world.getPersistentStateManager();
        VillagerOwnershipManager manager = stateManager.getOrCreate(type, DATA_NAME);

        XeenaaVillagerManager.LOGGER.debug("Retrieved ownership manager for world {}", world.getRegistryKey().getValue());
        return manager;
    }

    /**
     * Creates a new ownership manager.
     */
    public VillagerOwnershipManager() {
        super();
        XeenaaVillagerManager.LOGGER.debug("Created new ownership manager");
    }

    /**
     * Binds a villager to a player, establishing ownership.
     *
     * <p>If the villager is already owned, this will fail silently.
     * Use {@link #transferOwnership} to change ownership.</p>
     *
     * @param villagerUUID the UUID of the villager to bind
     * @param owner the player who will own the villager
     * @throws IllegalArgumentException if villagerUUID or owner is null
     */
    public void bindVillager(UUID villagerUUID, ServerPlayerEntity owner) {
        if (villagerUUID == null) {
            throw new IllegalArgumentException("Villager UUID cannot be null");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }

        // Check if already owned
        if (ownerships.containsKey(villagerUUID)) {
            VillagerOwnership existing = ownerships.get(villagerUUID);
            XeenaaVillagerManager.LOGGER.warn("Attempted to bind already owned villager {} (owner: {})",
                villagerUUID, existing.getOwnerName());
            return;
        }

        // Create ownership
        VillagerOwnership ownership = new VillagerOwnership(villagerUUID, owner);
        ownerships.put(villagerUUID, ownership);
        markDirty();

        XeenaaVillagerManager.LOGGER.info("Bound villager {} to player {} ({})",
            villagerUUID, owner.getUuid(), owner.getName().getString());
    }

    /**
     * Unbinds a villager, removing ownership.
     *
     * @param villagerUUID the UUID of the villager to unbind
     * @throws IllegalArgumentException if villagerUUID is null
     */
    public void unbindVillager(UUID villagerUUID) {
        if (villagerUUID == null) {
            throw new IllegalArgumentException("Villager UUID cannot be null");
        }

        VillagerOwnership removed = ownerships.remove(villagerUUID);
        if (removed != null) {
            markDirty();
            XeenaaVillagerManager.LOGGER.info("Unbound villager {} (was owned by {})",
                villagerUUID, removed.getOwnerName());
        } else {
            XeenaaVillagerManager.LOGGER.debug("Attempted to unbind non-owned villager {}", villagerUUID);
        }
    }

    /**
     * Transfers ownership of a villager to a new owner.
     *
     * <p>This creates a new ownership record with updated timestamp while
     * preserving permission settings where appropriate.</p>
     *
     * @param villagerUUID the UUID of the villager to transfer
     * @param newOwner the new owner
     * @param world the server world (for player lookup if needed)
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalStateException if villager is not currently owned
     */
    public void transferOwnership(UUID villagerUUID, ServerPlayerEntity newOwner, ServerWorld world) {
        if (villagerUUID == null) {
            throw new IllegalArgumentException("Villager UUID cannot be null");
        }
        if (newOwner == null) {
            throw new IllegalArgumentException("New owner cannot be null");
        }
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        VillagerOwnership oldOwnership = ownerships.get(villagerUUID);
        if (oldOwnership == null) {
            throw new IllegalStateException("Cannot transfer ownership of unowned villager: " + villagerUUID);
        }

        // Create new ownership (resets timestamp, permissions)
        VillagerOwnership newOwnership = new VillagerOwnership(villagerUUID, newOwner);
        ownerships.put(villagerUUID, newOwnership);
        markDirty();

        XeenaaVillagerManager.LOGGER.info("Transferred villager {} from {} to {} ({})",
            villagerUUID, oldOwnership.getOwnerName(), newOwner.getUuid(), newOwner.getName().getString());
    }

    /**
     * Gets ownership data for a villager.
     *
     * @param villagerUUID the villager UUID
     * @return the ownership data, or null if not owned
     */
    public VillagerOwnership getOwnership(UUID villagerUUID) {
        if (villagerUUID == null) {
            return null;
        }
        return ownerships.get(villagerUUID);
    }

    /**
     * Checks if a villager has an owner.
     *
     * @param villagerUUID the villager UUID
     * @return true if the villager is owned, false otherwise
     */
    public boolean hasOwner(UUID villagerUUID) {
        if (villagerUUID == null) {
            return false;
        }
        return ownerships.containsKey(villagerUUID);
    }

    /**
     * Checks if a player is the owner of a villager.
     *
     * @param villagerUUID the villager UUID
     * @param player the player to check
     * @return true if the player owns the villager, false otherwise
     */
    public boolean isOwner(UUID villagerUUID, ServerPlayerEntity player) {
        if (villagerUUID == null || player == null) {
            return false;
        }

        VillagerOwnership ownership = ownerships.get(villagerUUID);
        if (ownership == null) {
            return false;
        }

        return ownership.isOwner(player);
    }

    /**
     * Checks if a player can manage a villager (change settings, equipment, etc.).
     *
     * <p>Management access is granted if:</p>
     * <ul>
     *   <li>Player is the owner, OR</li>
     *   <li>Villager is not owned</li>
     * </ul>
     *
     * @param villagerUUID the villager UUID
     * @param player the player to check
     * @return true if management is allowed, false otherwise
     */
    public boolean canManage(UUID villagerUUID, ServerPlayerEntity player) {
        if (villagerUUID == null || player == null) {
            XeenaaVillagerManager.LOGGER.debug("canManage: null parameter, denying");
            return false;
        }

        VillagerOwnership ownership = ownerships.get(villagerUUID);
        if (ownership == null) {
            // Unowned villagers can be managed by anyone
            XeenaaVillagerManager.LOGGER.debug("canManage: villager {} unowned, allowing", villagerUUID);
            return true;
        }

        boolean allowed = ownership.isOwner(player);
        XeenaaVillagerManager.LOGGER.debug("canManage: villager {}, player {}, allowed={}",
            villagerUUID, player.getUuid(), allowed);
        return allowed;
    }

    /**
     * Checks if a player can trade with a villager.
     *
     * <p>Trade access depends on ownership status and permission settings:</p>
     * <ul>
     *   <li>Unowned villagers: Anyone can trade</li>
     *   <li>Owned villagers: Checked against permission settings</li>
     * </ul>
     *
     * @param villagerUUID the villager UUID
     * @param player the player to check
     * @return true if trading is allowed, false otherwise
     */
    public boolean canTrade(UUID villagerUUID, ServerPlayerEntity player) {
        if (villagerUUID == null || player == null) {
            XeenaaVillagerManager.LOGGER.debug("canTrade: null parameter, denying");
            return false;
        }

        VillagerOwnership ownership = ownerships.get(villagerUUID);
        if (ownership == null) {
            // Unowned villagers can be traded with by anyone
            XeenaaVillagerManager.LOGGER.debug("canTrade: villager {} unowned, allowing", villagerUUID);
            return true;
        }

        // Check lock status first
        if (!ownership.canInteract(player)) {
            XeenaaVillagerManager.LOGGER.debug("canTrade: villager {} locked, denying for player {}",
                villagerUUID, player.getUuid());
            return false;
        }

        // Check trade permissions
        boolean allowed = ownership.getPermissions().canTrade(player, ownership.getOwnerUUID());
        XeenaaVillagerManager.LOGGER.debug("canTrade: villager {}, player {}, allowed={}",
            villagerUUID, player.getUuid(), allowed);
        return allowed;
    }

    /**
     * Gets all ownership data for debugging or administrative purposes.
     *
     * <p>Warning: This returns a copy of the internal map. Modifications
     * to the returned map will not affect the manager.</p>
     *
     * @return a copy of all ownership data
     */
    public Map<UUID, VillagerOwnership> getAllOwnerships() {
        return new HashMap<>(ownerships);
    }

    /**
     * Clears all ownership data.
     *
     * <p>Warning: This is primarily for testing purposes.
     * Use with caution in production environments.</p>
     */
    public void clearAll() {
        int size = ownerships.size();
        ownerships.clear();
        markDirty();
        XeenaaVillagerManager.LOGGER.warn("Cleared all {} ownership records", size);
    }

    /**
     * Gets the total number of owned villagers.
     *
     * @return the count of ownership records
     */
    public int getOwnershipCount() {
        return ownerships.size();
    }

    /**
     * Serializes all ownership data to NBT.
     *
     * @param nbt the NBT compound to write to
     * @param registries the registry wrapper for lookups
     * @return the NBT compound with ownership data
     */
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        // Write version
        nbt.putInt(NBT_KEY_VERSION, CURRENT_VERSION);

        // Write all ownerships
        NbtList ownershipsList = new NbtList();
        for (VillagerOwnership ownership : ownerships.values()) {
            try {
                ownershipsList.add(ownership.toNbt());
            } catch (Exception e) {
                XeenaaVillagerManager.LOGGER.error("Failed to serialize ownership for villager {}",
                    ownership.getVillagerUUID(), e);
            }
        }
        nbt.put(NBT_KEY_OWNERSHIPS, ownershipsList);

        XeenaaVillagerManager.LOGGER.info("Saved {} ownership records", ownerships.size());
        return nbt;
    }

    /**
     * Deserializes ownership data from NBT.
     *
     * @param nbt the NBT compound to read from
     * @param registries the registry wrapper for lookups
     * @return a new ownership manager with loaded data
     */
    public static VillagerOwnershipManager fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        VillagerOwnershipManager manager = new VillagerOwnershipManager();

        // Check version
        int version = nbt.getInt(NBT_KEY_VERSION);
        if (version > CURRENT_VERSION) {
            XeenaaVillagerManager.LOGGER.warn("Loading ownership data from newer version: {} > {}",
                version, CURRENT_VERSION);
        }

        // Load all ownerships
        if (nbt.contains(NBT_KEY_OWNERSHIPS, NbtElement.LIST_TYPE)) {
            NbtList ownershipsList = nbt.getList(NBT_KEY_OWNERSHIPS, NbtElement.COMPOUND_TYPE);
            int loaded = 0;
            int failed = 0;

            for (int i = 0; i < ownershipsList.size(); i++) {
                try {
                    NbtCompound ownershipNbt = ownershipsList.getCompound(i);
                    VillagerOwnership ownership = VillagerOwnership.fromNbt(ownershipNbt);
                    manager.ownerships.put(ownership.getVillagerUUID(), ownership);
                    loaded++;
                } catch (Exception e) {
                    XeenaaVillagerManager.LOGGER.error("Failed to load ownership entry {}", i, e);
                    failed++;
                }
            }

            XeenaaVillagerManager.LOGGER.info("Loaded {} ownership records ({} failed)",
                loaded, failed);
        } else {
            XeenaaVillagerManager.LOGGER.info("No ownership data found, starting fresh");
        }

        return manager;
    }

    /**
     * Cleans up ownership records for villagers that no longer exist.
     *
     * <p>This should be called periodically (e.g., on world load or during
     * scheduled maintenance) to prevent data accumulation.</p>
     *
     * @param world the server world to check entities in
     * @return the number of records removed
     */
    public int cleanupInvalidEntries(ServerWorld world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        Set<UUID> validVillagerUUIDs = new HashSet<>();

        // Collect all valid villager UUIDs
        world.iterateEntities().forEach(entity -> {
            if (entity instanceof net.minecraft.entity.passive.VillagerEntity && entity.isAlive()) {
                validVillagerUUIDs.add(entity.getUuid());
            }
        });

        // Remove ownership records for non-existent villagers
        int removed = 0;
        Iterator<Map.Entry<UUID, VillagerOwnership>> iterator = ownerships.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, VillagerOwnership> entry = iterator.next();
            if (!validVillagerUUIDs.contains(entry.getKey())) {
                iterator.remove();
                removed++;
                XeenaaVillagerManager.LOGGER.debug("Removed stale ownership record for villager {}",
                    entry.getKey());
            }
        }

        if (removed > 0) {
            markDirty();
            XeenaaVillagerManager.LOGGER.info("Cleaned up {} invalid ownership records", removed);
        }

        return removed;
    }
}
