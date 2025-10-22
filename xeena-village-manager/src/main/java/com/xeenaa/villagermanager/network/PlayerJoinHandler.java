package com.xeenaa.villagermanager.network;

import com.xeenaa.villagermanager.data.GuardData;
import com.xeenaa.villagermanager.data.GuardDataManager;
import com.xeenaa.villagermanager.network.ownership.InitialOwnershipSyncPacket;
import com.xeenaa.villagermanager.ownership.VillagerOwnership;
import com.xeenaa.villagermanager.ownership.VillagerOwnershipManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles server-side events for player connections and syncs data to new clients.
 *
 * <p>This handler sends initial synchronization packets when a player joins:</p>
 * <ul>
 *   <li>Guard data (roles, configurations)</li>
 *   <li>Ownership data (villager owners, permissions)</li>
 * </ul>
 *
 * <p>Initial sync ensures clients have complete data after world restart,
 * preventing missing owner badges and incorrect button visibility.</p>
 */
public class PlayerJoinHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger("PlayerJoinHandler");

    /**
     * Registers the player join handler to sync all game data to new clients.
     */
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            LOGGER.info("=== PLAYER JOIN: {} ===", player.getName().getString());

            // Send initial guard data sync to the player
            sendInitialGuardDataSync(player);

            // Send initial ownership data sync to the player
            sendInitialOwnershipDataSync(player);

            LOGGER.info("=== PLAYER JOIN COMPLETE: {} ===", player.getName().getString());
        });

        LOGGER.info("Registered player join handler for initial data sync (guard + ownership)");
    }

    /**
     * Sends all existing guard data to a newly connected player.
     */
    private static void sendInitialGuardDataSync(ServerPlayerEntity player) {
        try {
            LOGGER.info("=== SENDING INITIAL GUARD DATA SYNC to player {} ===", player.getName().getString());

            ServerWorld world = player.getServerWorld();
            GuardDataManager manager = GuardDataManager.get(world);
            Map<UUID, GuardData> allGuardData = manager.getAllGuardData();

            LOGGER.info("Found {} guard data entries to sync", allGuardData.size());

            if (allGuardData.isEmpty()) {
                LOGGER.info("No guard data to sync - player {} will have empty cache", player.getName().getString());
                // Still send an empty sync packet to clear any potential stale cache
                allGuardData = Map.of();
            }

            // Create and send initial sync packet
            InitialGuardDataSyncPacket packet = new InitialGuardDataSyncPacket(allGuardData);
            ServerPlayNetworking.send(player, packet);

            LOGGER.info("Sent initial guard data sync to player {} with {} entries",
                       player.getName().getString(), allGuardData.size());
            LOGGER.info("=== INITIAL GUARD DATA SYNC COMPLETE ===");

        } catch (Exception e) {
            LOGGER.error("Failed to send initial guard data sync to player {}: {}",
                        player.getName().getString(), e.getMessage(), e);
        }
    }

    /**
     * Sends all existing ownership data to a newly connected player.
     *
     * <p>This method is called when a player joins the world/server. It retrieves
     * all ownership records from the server-side {@link VillagerOwnershipManager}
     * and sends them to the client via {@link InitialOwnershipSyncPacket}.</p>
     *
     * <p>The sync includes:</p>
     * <ul>
     *   <li>All villager UUIDs that have owners</li>
     *   <li>Owner UUIDs and cached display names</li>
     *   <li>Permission settings for each owned villager</li>
     * </ul>
     *
     * <p>After initial sync, individual updates are sent via
     * {@link com.xeenaa.villagermanager.network.ownership.OwnershipSyncPacket}
     * when ownership changes occur.</p>
     *
     * @param player the player who just joined
     */
    private static void sendInitialOwnershipDataSync(ServerPlayerEntity player) {
        try {
            LOGGER.info("=== SENDING INITIAL OWNERSHIP DATA SYNC to player {} ===", player.getName().getString());

            ServerWorld world = player.getServerWorld();
            VillagerOwnershipManager manager = VillagerOwnershipManager.get(world);
            Map<UUID, VillagerOwnership> allOwnerships = manager.getAllOwnerships();

            LOGGER.info("Found {} ownership entries to sync", allOwnerships.size());

            // Convert VillagerOwnership objects to network-safe OwnershipData records
            Map<UUID, InitialOwnershipSyncPacket.OwnershipData> ownershipDataMap = new HashMap<>();

            for (Map.Entry<UUID, VillagerOwnership> entry : allOwnerships.entrySet()) {
                UUID villagerUUID = entry.getKey();
                VillagerOwnership ownership = entry.getValue();

                InitialOwnershipSyncPacket.OwnershipData data = new InitialOwnershipSyncPacket.OwnershipData(
                    ownership.getOwnerUUID(),
                    ownership.getOwnerName(),
                    ownership.getPermissions().toNbt()
                );

                ownershipDataMap.put(villagerUUID, data);

                LOGGER.debug("Prepared ownership sync: villager={}, owner={} ({})",
                    villagerUUID, ownership.getOwnerUUID(), ownership.getOwnerName());
            }

            // Create and send initial sync packet
            InitialOwnershipSyncPacket packet = new InitialOwnershipSyncPacket(ownershipDataMap);
            ServerPlayNetworking.send(player, packet);

            LOGGER.info("Sent initial ownership data sync to player {} with {} entries",
                player.getName().getString(), ownershipDataMap.size());
            LOGGER.info("=== INITIAL OWNERSHIP DATA SYNC COMPLETE ===");

        } catch (Exception e) {
            LOGGER.error("Failed to send initial ownership data sync to player {}: {}",
                player.getName().getString(), e.getMessage(), e);
        }
    }
}