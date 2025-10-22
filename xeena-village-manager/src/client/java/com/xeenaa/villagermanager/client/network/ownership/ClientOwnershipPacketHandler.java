package com.xeenaa.villagermanager.client.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.network.ownership.InitialOwnershipSyncPacket;
import com.xeenaa.villagermanager.network.ownership.OwnershipDeniedPacket;
import com.xeenaa.villagermanager.network.ownership.OwnershipSyncPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;

/**
 * Client-side handler for ownership-related network packets.
 *
 * <p>This class processes ownership synchronization and denial packets from
 * the server, updating the client-side ownership cache and providing user
 * feedback.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Process initial ownership sync on world join</li>
 *   <li>Update client-side ownership cache with server data</li>
 *   <li>Refresh management GUI if currently open</li>
 *   <li>Display error messages for denied operations</li>
 *   <li>Show toast notifications for ownership changes (optional)</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class ClientOwnershipPacketHandler {

    /**
     * Handles initial ownership synchronization packet from the server.
     *
     * <p>This packet is received once when the player joins a world/server,
     * containing ALL ownership data. It populates the client-side ownership
     * cache to ensure proper display of ownership badges and buttons after
     * world restart.</p>
     *
     * <p>The handler:</p>
     * <ul>
     *   <li>Clears existing client-side ownership cache</li>
     *   <li>Populates cache with all ownership entries from server</li>
     *   <li>Logs sync completion with entry count</li>
     * </ul>
     *
     * @param packet the initial ownership sync packet
     * @param context the client networking context
     */
    public static void handleInitialSync(InitialOwnershipSyncPacket packet, ClientPlayNetworking.Context context) {
        // Execute on client thread for thread safety
        MinecraftClient client = context.client();
        client.execute(() -> {
            try {
                XeenaaVillagerManager.LOGGER.info("=== RECEIVED INITIAL OWNERSHIP SYNC ===");
                XeenaaVillagerManager.LOGGER.info("Processing {} ownership entries from server", packet.getCount());

                // Get client-side ownership cache
                ClientOwnershipCache cache = ClientOwnershipCache.getInstance();

                // Clear existing cache to ensure clean state
                cache.clearAll();
                XeenaaVillagerManager.LOGGER.debug("Cleared existing client ownership cache");

                // Process each ownership entry
                int processed = 0;
                for (Map.Entry<UUID, InitialOwnershipSyncPacket.OwnershipData> entry : packet.ownerships().entrySet()) {
                    try {
                        UUID villagerUUID = entry.getKey();
                        InitialOwnershipSyncPacket.OwnershipData data = entry.getValue();

                        // Update cache with ownership data
                        cache.updateOwnership(
                            villagerUUID,
                            data.ownerUUID(),
                            data.ownerName(),
                            data.permissionsNbt()
                        );

                        processed++;

                        XeenaaVillagerManager.LOGGER.debug("Cached ownership: villager={}, owner={} ({})",
                            villagerUUID, data.ownerUUID(), data.ownerName());

                    } catch (Exception e) {
                        XeenaaVillagerManager.LOGGER.error("Error processing ownership entry for villager {}",
                            entry.getKey(), e);
                    }
                }

                XeenaaVillagerManager.LOGGER.info("Successfully processed {} ownership entries (cache size: {})",
                    processed, cache.getCacheSize());
                XeenaaVillagerManager.LOGGER.info("=== INITIAL OWNERSHIP SYNC COMPLETE ===");

            } catch (Exception e) {
                XeenaaVillagerManager.LOGGER.error("Error handling initial ownership sync packet", e);
            }
        });
    }

    /**
     * Handles ownership synchronization packets from the server.
     *
     * <p>This packet is received when:</p>
     * <ul>
     *   <li>A nearby villager is claimed or released</li>
     *   <li>Ownership permissions are updated</li>
     *   <li>The player opens the management GUI for an owned villager</li>
     * </ul>
     *
     * <p>The handler updates the client-side ownership cache and refreshes
     * any open management GUI for the affected villager.</p>
     *
     * @param packet the ownership sync packet
     * @param context the client networking context
     */
    public static void handleSync(OwnershipSyncPacket packet, ClientPlayNetworking.Context context) {
        // Execute on client thread for thread safety
        MinecraftClient client = context.client();
        client.execute(() -> {
            try {
                XeenaaVillagerManager.LOGGER.debug("Received ownership sync for villager {}: owner={}",
                    packet.villagerUUID(),
                    packet.isOwned() ? packet.ownerName() : "none");

                // Update client-side ownership cache
                ClientOwnershipCache cache = ClientOwnershipCache.getInstance();

                if (packet.isOwned()) {
                    // Store ownership data
                    cache.updateOwnership(
                        packet.villagerUUID(),
                        packet.ownerUUID(),
                        packet.ownerName(),
                        packet.permissionsNbt()
                    );

                    XeenaaVillagerManager.LOGGER.info("Updated client ownership cache: villager {} now owned by {}",
                        packet.villagerUUID(), packet.ownerName());
                } else {
                    // Remove ownership data (villager is unowned)
                    cache.removeOwnership(packet.villagerUUID());

                    XeenaaVillagerManager.LOGGER.info("Updated client ownership cache: villager {} is now unowned",
                        packet.villagerUUID());
                }

                // Refresh GUI if currently viewing this villager
                refreshGuiIfOpen(client, packet.villagerUUID());

            } catch (Exception e) {
                XeenaaVillagerManager.LOGGER.error("Error handling ownership sync packet", e);
            }
        });
    }

    /**
     * Handles ownership denied packets from the server.
     *
     * <p>This packet is received when the server rejects a bind or unbind
     * request due to validation failures. The handler displays an appropriate
     * error message to the player.</p>
     *
     * @param packet the ownership denied packet
     * @param context the client networking context
     */
    public static void handleDenied(OwnershipDeniedPacket packet, ClientPlayNetworking.Context context) {
        // Execute on client thread for thread safety
        MinecraftClient client = context.client();
        client.execute(() -> {
            try {
                XeenaaVillagerManager.LOGGER.warn("Ownership operation denied by server: {}", packet.reason());

                // Display error message to player
                if (client.player != null) {
                    String errorMessage = packet.getUserMessage();
                    client.player.sendMessage(
                        Text.literal("❌ " + errorMessage)
                            .styled(style -> style.withColor(0xFF0000)),
                        false
                    );
                }

            } catch (Exception e) {
                XeenaaVillagerManager.LOGGER.error("Error handling ownership denied packet", e);
            }
        });
    }

    /**
     * Refreshes the management GUI if currently viewing the specified villager.
     *
     * <p>This method checks if the player has the management screen open for
     * the villager whose ownership just changed. If so, it triggers a refresh
     * by closing and reopening the screen.</p>
     *
     * @param client the Minecraft client
     * @param villagerUUID the UUID of the villager whose ownership changed
     */
    private static void refreshGuiIfOpen(MinecraftClient client, java.util.UUID villagerUUID) {
        if (client.currentScreen instanceof com.xeenaa.villagermanager.client.gui.UnifiedGuardManagementScreen) {
            // Refresh by calling init() which is public
            // This will rebuild the UI with updated ownership data
            XeenaaVillagerManager.LOGGER.debug("Management GUI is open, triggering refresh");
            client.currentScreen.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        }
    }
}
