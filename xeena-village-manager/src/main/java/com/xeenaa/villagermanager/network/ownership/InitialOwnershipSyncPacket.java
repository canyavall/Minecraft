package com.xeenaa.villagermanager.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.ownership.OwnershipPermissions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Server-to-Client packet for initial synchronization of all ownership data when a player joins.
 *
 * <p>This packet is sent once when a player connects to the server or joins a world,
 * ensuring the client-side ownership cache is populated with all existing ownership
 * data for proper GUI rendering and interaction checks.</p>
 *
 * <p>Lifecycle:</p>
 * <ul>
 *   <li>Sent by server on player join (via {@link com.xeenaa.villagermanager.network.PlayerJoinHandler})</li>
 *   <li>Received by client and processed to populate {@link com.xeenaa.villagermanager.client.network.ownership.ClientOwnershipCache}</li>
 *   <li>Contains ALL ownership data (no proximity limit like {@link OwnershipSyncPacket})</li>
 * </ul>
 *
 * <p>After initial sync, individual updates are sent via {@link OwnershipSyncPacket}
 * when ownership changes occur (bind, unbind, permission changes).</p>
 *
 * <p>Thread Safety: Immutable record, thread-safe by design.</p>
 *
 * @param ownerships map of villager UUID to ownership data (villagerUUID -> OwnershipData)
 * @since 1.0.0
 */
public record InitialOwnershipSyncPacket(
    Map<UUID, OwnershipData> ownerships
) implements CustomPayload {

    /**
     * Unique packet identifier for initial ownership synchronization.
     */
    public static final CustomPayload.Id<InitialOwnershipSyncPacket> ID =
        new CustomPayload.Id<>(Identifier.of(XeenaaVillagerManager.MOD_ID, "initial_ownership_sync"));

    /**
     * Packet codec for serialization/deserialization.
     *
     * <p>Encoding format:</p>
     * <ul>
     *   <li>VarInt: Number of ownership entries</li>
     *   <li>For each entry:
     *     <ul>
     *       <li>UUID: Villager UUID</li>
     *       <li>UUID: Owner UUID</li>
     *       <li>String: Owner name</li>
     *       <li>NBT: Permissions (nullable - boolean flag + compound)</li>
     *     </ul>
     *   </li>
     * </ul>
     */
    public static final PacketCodec<RegistryByteBuf, InitialOwnershipSyncPacket> CODEC =
        new PacketCodec<RegistryByteBuf, InitialOwnershipSyncPacket>() {
            @Override
            public InitialOwnershipSyncPacket decode(RegistryByteBuf buf) {
                XeenaaVillagerManager.LOGGER.info("=== DECODING INITIAL OWNERSHIP SYNC PACKET ===");

                Map<UUID, OwnershipData> ownerships = new HashMap<>();
                int count = buf.readVarInt();
                XeenaaVillagerManager.LOGGER.info("Reading {} ownership entries", count);

                for (int i = 0; i < count; i++) {
                    try {
                        // Read villager UUID
                        UUID villagerUUID = Uuids.PACKET_CODEC.decode(buf);

                        // Read owner UUID
                        UUID ownerUUID = Uuids.PACKET_CODEC.decode(buf);

                        // Read owner name
                        String ownerName = buf.readString();

                        // Read permissions (nullable)
                        NbtCompound permissionsNbt = null;
                        if (buf.readBoolean()) {
                            permissionsNbt = buf.readNbt();
                        }

                        OwnershipData data = new OwnershipData(ownerUUID, ownerName, permissionsNbt);
                        ownerships.put(villagerUUID, data);

                        XeenaaVillagerManager.LOGGER.debug("Decoded ownership entry {}: villager={}, owner={} ({})",
                            i, villagerUUID, ownerUUID, ownerName);

                    } catch (Exception e) {
                        XeenaaVillagerManager.LOGGER.error("Failed to decode ownership entry {}", i, e);
                    }
                }

                XeenaaVillagerManager.LOGGER.info("Successfully decoded initial ownership sync with {} entries",
                    ownerships.size());
                return new InitialOwnershipSyncPacket(ownerships);
            }

            @Override
            public void encode(RegistryByteBuf buf, InitialOwnershipSyncPacket packet) {
                XeenaaVillagerManager.LOGGER.info("=== ENCODING INITIAL OWNERSHIP SYNC PACKET ===");
                XeenaaVillagerManager.LOGGER.info("Encoding {} ownership entries", packet.ownerships.size());

                // Write count
                buf.writeVarInt(packet.ownerships.size());

                // Write each ownership entry
                for (Map.Entry<UUID, OwnershipData> entry : packet.ownerships.entrySet()) {
                    try {
                        UUID villagerUUID = entry.getKey();
                        OwnershipData data = entry.getValue();

                        // Write villager UUID
                        Uuids.PACKET_CODEC.encode(buf, villagerUUID);

                        // Write owner UUID
                        Uuids.PACKET_CODEC.encode(buf, data.ownerUUID());

                        // Write owner name
                        buf.writeString(data.ownerName());

                        // Write permissions (nullable)
                        buf.writeBoolean(data.permissionsNbt() != null);
                        if (data.permissionsNbt() != null) {
                            buf.writeNbt(data.permissionsNbt());
                        }

                        XeenaaVillagerManager.LOGGER.debug("Encoded ownership: villager={}, owner={} ({})",
                            villagerUUID, data.ownerUUID(), data.ownerName());

                    } catch (Exception e) {
                        XeenaaVillagerManager.LOGGER.error("Failed to encode ownership for villager {}",
                            entry.getKey(), e);
                    }
                }

                XeenaaVillagerManager.LOGGER.info("Successfully encoded initial ownership sync packet");
            }
        };

    /**
     * Checks if this packet contains any ownership data.
     *
     * @return true if ownerships map is empty, false otherwise
     */
    public boolean isEmpty() {
        return ownerships.isEmpty();
    }

    /**
     * Gets the number of ownership entries in this packet.
     *
     * @return the count of ownership records
     */
    public int getCount() {
        return ownerships.size();
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    /**
     * Ownership data record for network transmission.
     *
     * <p>Simplified version of {@link com.xeenaa.villagermanager.ownership.VillagerOwnership}
     * containing only data needed for client-side display and interaction checks.</p>
     *
     * @param ownerUUID the UUID of the owner player
     * @param ownerName the cached display name of the owner
     * @param permissionsNbt the permission settings as NBT (null for defaults)
     */
    public record OwnershipData(
        UUID ownerUUID,
        String ownerName,
        @Nullable NbtCompound permissionsNbt
    ) {
        /**
         * Gets the permissions, deserializing from NBT if present.
         *
         * @return the ownership permissions (defaults if NBT is null)
         */
        public OwnershipPermissions getPermissions() {
            if (permissionsNbt != null) {
                return OwnershipPermissions.fromNbt(permissionsNbt);
            }
            return new OwnershipPermissions();
        }
    }
}
