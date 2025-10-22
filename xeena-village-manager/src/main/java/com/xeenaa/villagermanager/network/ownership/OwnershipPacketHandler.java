package com.xeenaa.villagermanager.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.ownership.VillagerOwnership;
import com.xeenaa.villagermanager.ownership.VillagerOwnershipManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side handler for ownership-related network packets.
 *
 * <p>This class processes bind and unbind requests from clients, performs
 * comprehensive validation, and broadcasts ownership changes to nearby players.</p>
 *
 * <p>Security Features:</p>
 * <ul>
 *   <li>Distance validation: Players must be within 10 blocks</li>
 *   <li>Rate limiting: 2 second cooldown between operations</li>
 *   <li>Ownership validation: Prevents duplicate binding and unauthorized unbinding</li>
 *   <li>Entity validation: Ensures target is a valid, loaded villager</li>
 * </ul>
 *
 * <p>Thread Safety: Uses ConcurrentHashMap for cooldown tracking, safe for
 * concurrent server ticks.</p>
 *
 * @since 1.0.0
 */
public class OwnershipPacketHandler {

    /**
     * Cooldown duration in milliseconds (2 seconds).
     */
    private static final long COOLDOWN_MS = 2000;

    /**
     * Maximum distance in blocks for ownership operations (10 blocks).
     */
    private static final double MAX_DISTANCE = 10.0;

    /**
     * Squared maximum distance for efficient distance checks (100 blocks²).
     */
    private static final double MAX_DISTANCE_SQUARED = MAX_DISTANCE * MAX_DISTANCE;

    /**
     * Broadcast radius in blocks for ownership sync packets (32 blocks).
     */
    private static final double BROADCAST_RADIUS = 32.0;

    /**
     * Squared broadcast radius for efficient distance checks (1024 blocks²).
     */
    private static final double BROADCAST_RADIUS_SQUARED = BROADCAST_RADIUS * BROADCAST_RADIUS;

    /**
     * Cooldown tracker for bind operations per player.
     * Key: Player UUID, Value: Timestamp of last bind attempt
     */
    private static final Map<UUID, Long> bindCooldowns = new ConcurrentHashMap<>();

    /**
     * Cooldown tracker for unbind operations per player.
     * Key: Player UUID, Value: Timestamp of last unbind attempt
     */
    private static final Map<UUID, Long> unbindCooldowns = new ConcurrentHashMap<>();

    /**
     * Handles a bind villager request from a client.
     *
     * <p>Validation steps:</p>
     * <ol>
     *   <li>Check rate limiting (2 second cooldown)</li>
     *   <li>Find villager entity by ID</li>
     *   <li>Validate entity is a villager and loaded</li>
     *   <li>Check player distance (10 block limit)</li>
     *   <li>Verify villager is not already owned</li>
     *   <li>Create ownership binding</li>
     *   <li>Broadcast to nearby players</li>
     * </ol>
     *
     * @param packet the bind request packet
     * @param context the server networking context
     */
    public static void handleBind(BindVillagerPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        // Execute on server thread for thread safety
        player.getServer().execute(() -> {
            try {
                XeenaaVillagerManager.LOGGER.info("Processing bind request from player {} for villager entity {}",
                    player.getName().getString(), packet.villagerEntityId());

                // Rate limiting check
                if (!checkCooldown(player, bindCooldowns, "bind")) {
                    sendDenied(player, "rate_limited");
                    return;
                }

                // Find villager entity
                ServerWorld world = (ServerWorld) player.getWorld();
                Entity entity = world.getEntityById(packet.villagerEntityId());

                if (entity == null) {
                    XeenaaVillagerManager.LOGGER.warn("Bind failed: entity {} not found",
                        packet.villagerEntityId());
                    sendDenied(player, "entity_not_found");
                    return;
                }

                if (!(entity instanceof VillagerEntity villager)) {
                    XeenaaVillagerManager.LOGGER.warn("Bind failed: entity {} is not a villager",
                        packet.villagerEntityId());
                    sendDenied(player, "invalid_entity");
                    return;
                }

                // Distance validation
                if (!checkDistance(player, villager)) {
                    XeenaaVillagerManager.LOGGER.warn("Bind failed: player {} too far from villager {} (distance: {} blocks)",
                        player.getName().getString(), villager.getUuid(),
                        Math.sqrt(player.squaredDistanceTo(villager)));
                    sendDenied(player, "too_far");
                    return;
                }

                // Ownership validation
                VillagerOwnershipManager ownershipManager = VillagerOwnershipManager.get(world);
                if (ownershipManager.hasOwner(villager.getUuid())) {
                    VillagerOwnership existing = ownershipManager.getOwnership(villager.getUuid());
                    XeenaaVillagerManager.LOGGER.warn("Bind failed: villager {} already owned by {}",
                        villager.getUuid(), existing != null ? existing.getOwnerName() : "unknown");
                    sendDenied(player, "already_owned");
                    return;
                }

                // Bind the villager
                ownershipManager.bindVillager(villager.getUuid(), player);

                // Update cooldown
                bindCooldowns.put(player.getUuid(), System.currentTimeMillis());

                // Send success feedback to player
                player.sendMessage(
                    Text.literal("Successfully claimed villager!")
                        .styled(style -> style.withColor(0x00FF00)),
                    false
                );

                // Broadcast ownership sync to nearby players
                VillagerOwnership ownership = ownershipManager.getOwnership(villager.getUuid());
                broadcastOwnershipSync(world, villager, ownership);

                XeenaaVillagerManager.LOGGER.info("Successfully bound villager {} to player {} ({})",
                    villager.getUuid(), player.getUuid(), player.getName().getString());

            } catch (Exception e) {
                XeenaaVillagerManager.LOGGER.error("Error processing bind packet", e);
                sendDenied(player, "internal_error");
            }
        });
    }

    /**
     * Handles an unbind villager request from a client.
     *
     * <p>Validation steps:</p>
     * <ol>
     *   <li>Check rate limiting (2 second cooldown)</li>
     *   <li>Find villager entity by ID</li>
     *   <li>Validate entity is a villager and loaded</li>
     *   <li>Check player distance (10 block limit)</li>
     *   <li>Verify player is the owner</li>
     *   <li>Remove ownership binding</li>
     *   <li>Broadcast to nearby players</li>
     * </ol>
     *
     * @param packet the unbind request packet
     * @param context the server networking context
     */
    public static void handleUnbind(UnbindVillagerPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        // Execute on server thread for thread safety
        player.getServer().execute(() -> {
            try {
                XeenaaVillagerManager.LOGGER.info("Processing unbind request from player {} for villager entity {}",
                    player.getName().getString(), packet.villagerEntityId());

                // Rate limiting check
                if (!checkCooldown(player, unbindCooldowns, "unbind")) {
                    sendDenied(player, "rate_limited");
                    return;
                }

                // Find villager entity
                ServerWorld world = (ServerWorld) player.getWorld();
                Entity entity = world.getEntityById(packet.villagerEntityId());

                if (entity == null) {
                    XeenaaVillagerManager.LOGGER.warn("Unbind failed: entity {} not found",
                        packet.villagerEntityId());
                    sendDenied(player, "entity_not_found");
                    return;
                }

                if (!(entity instanceof VillagerEntity villager)) {
                    XeenaaVillagerManager.LOGGER.warn("Unbind failed: entity {} is not a villager",
                        packet.villagerEntityId());
                    sendDenied(player, "invalid_entity");
                    return;
                }

                // Distance validation
                if (!checkDistance(player, villager)) {
                    XeenaaVillagerManager.LOGGER.warn("Unbind failed: player {} too far from villager {} (distance: {} blocks)",
                        player.getName().getString(), villager.getUuid(),
                        Math.sqrt(player.squaredDistanceTo(villager)));
                    sendDenied(player, "too_far");
                    return;
                }

                // Ownership validation
                VillagerOwnershipManager ownershipManager = VillagerOwnershipManager.get(world);
                if (!ownershipManager.isOwner(villager.getUuid(), player)) {
                    XeenaaVillagerManager.LOGGER.warn("Unbind failed: player {} is not owner of villager {}",
                        player.getName().getString(), villager.getUuid());
                    sendDenied(player, "not_owner");
                    return;
                }

                // Unbind the villager
                ownershipManager.unbindVillager(villager.getUuid());

                // Update cooldown
                unbindCooldowns.put(player.getUuid(), System.currentTimeMillis());

                // Send success feedback to player
                player.sendMessage(
                    Text.literal("Released ownership of villager")
                        .styled(style -> style.withColor(0xFFAA00)),
                    false
                );

                // Broadcast ownership sync (null owner = unowned)
                broadcastOwnershipSync(world, villager, null);

                XeenaaVillagerManager.LOGGER.info("Successfully unbound villager {} (was owned by {})",
                    villager.getUuid(), player.getName().getString());

            } catch (Exception e) {
                XeenaaVillagerManager.LOGGER.error("Error processing unbind packet", e);
                sendDenied(player, "internal_error");
            }
        });
    }

    /**
     * Checks if a player is on cooldown for an operation.
     *
     * <p>Cooldown is 2 seconds from the last operation attempt.
     * Old cooldown entries are automatically cleaned up.</p>
     *
     * @param player the player to check
     * @param cooldownMap the cooldown map to check against
     * @param operation the operation name (for logging)
     * @return true if not on cooldown, false if rate limited
     */
    private static boolean checkCooldown(ServerPlayerEntity player, Map<UUID, Long> cooldownMap, String operation) {
        long currentTime = System.currentTimeMillis();
        UUID playerUUID = player.getUuid();

        if (cooldownMap.containsKey(playerUUID)) {
            long lastAttempt = cooldownMap.get(playerUUID);
            long timeSinceLastAttempt = currentTime - lastAttempt;

            if (timeSinceLastAttempt < COOLDOWN_MS) {
                long remainingCooldown = COOLDOWN_MS - timeSinceLastAttempt;
                XeenaaVillagerManager.LOGGER.debug("Player {} on {} cooldown: {}ms remaining",
                    player.getName().getString(), operation, remainingCooldown);
                return false;
            }

            // Clean up old cooldown entry
            cooldownMap.remove(playerUUID);
        }

        return true;
    }

    /**
     * Checks if a player is within range of a villager.
     *
     * @param player the player
     * @param villager the villager
     * @return true if within 10 blocks, false otherwise
     */
    private static boolean checkDistance(ServerPlayerEntity player, VillagerEntity villager) {
        double distanceSquared = player.squaredDistanceTo(villager);
        boolean inRange = distanceSquared <= MAX_DISTANCE_SQUARED;

        XeenaaVillagerManager.LOGGER.debug("Distance check: player {} to villager {}: {} blocks (limit: {} blocks), in range: {}",
            player.getName().getString(), villager.getUuid(),
            Math.sqrt(distanceSquared), MAX_DISTANCE, inRange);

        return inRange;
    }

    /**
     * Sends an ownership denied packet to a player.
     *
     * @param player the player to notify
     * @param reason the denial reason code
     */
    private static void sendDenied(ServerPlayerEntity player, String reason) {
        OwnershipDeniedPacket packet = new OwnershipDeniedPacket(reason);
        ServerPlayNetworking.send(player, packet);

        XeenaaVillagerManager.LOGGER.debug("Sent ownership denied to player {}: {}",
            player.getName().getString(), reason);
    }

    /**
     * Broadcasts ownership sync to all nearby players.
     *
     * <p>Broadcast radius is 32 blocks. All players within range receive
     * the updated ownership information.</p>
     *
     * @param world the server world
     * @param villager the villager whose ownership changed
     * @param ownership the new ownership data (null for unowned)
     */
    private static void broadcastOwnershipSync(ServerWorld world, VillagerEntity villager, VillagerOwnership ownership) {
        UUID villagerUUID = villager.getUuid();
        UUID ownerUUID = ownership != null ? ownership.getOwnerUUID() : null;
        String ownerName = ownership != null ? ownership.getOwnerName() : null;
        NbtCompound permissionsNbt = ownership != null ? ownership.getPermissions().toNbt() : null;

        OwnershipSyncPacket syncPacket = new OwnershipSyncPacket(
            villagerUUID,
            ownerUUID,
            ownerName,
            permissionsNbt
        );

        // Send to all players within 32 blocks
        int sentCount = 0;
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.squaredDistanceTo(villager) < BROADCAST_RADIUS_SQUARED) {
                ServerPlayNetworking.send(player, syncPacket);
                sentCount++;
            }
        }

        XeenaaVillagerManager.LOGGER.info("Broadcast ownership sync for villager {} to {} nearby players (owner: {})",
            villagerUUID, sentCount, ownerName != null ? ownerName : "none");
    }

    /**
     * Cleans up old cooldown entries for players who have logged out.
     *
     * <p>This method should be called periodically (e.g., every 5 minutes)
     * to prevent memory leaks from accumulating cooldown entries.</p>
     *
     * @param connectedPlayerUUIDs set of currently connected player UUIDs
     * @return the number of entries cleaned up
     */
    public static int cleanupCooldowns(java.util.Set<UUID> connectedPlayerUUIDs) {
        int cleaned = 0;

        cleaned += bindCooldowns.keySet().removeIf(uuid -> !connectedPlayerUUIDs.contains(uuid)) ? 1 : 0;
        cleaned += unbindCooldowns.keySet().removeIf(uuid -> !connectedPlayerUUIDs.contains(uuid)) ? 1 : 0;

        if (cleaned > 0) {
            XeenaaVillagerManager.LOGGER.debug("Cleaned up {} stale cooldown entries", cleaned);
        }

        return cleaned;
    }
}
