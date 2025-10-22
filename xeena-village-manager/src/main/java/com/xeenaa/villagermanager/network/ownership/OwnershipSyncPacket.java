package com.xeenaa.villagermanager.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Server-to-Client packet syncing ownership data for a villager.
 *
 * <p>This packet is broadcast to nearby players (32 block radius) when:</p>
 * <ul>
 *   <li>A villager is claimed (bound) by a player</li>
 *   <li>A villager is released (unbound) by its owner</li>
 *   <li>Ownership permissions are updated</li>
 *   <li>A player opens the management GUI for an owned villager</li>
 * </ul>
 *
 * <p>Clients use this data to update their local ownership cache and refresh
 * the management GUI if currently viewing the villager.</p>
 *
 * <p>Nullable fields:</p>
 * <ul>
 *   <li>{@code ownerUUID} - null indicates villager is unowned</li>
 *   <li>{@code ownerName} - null indicates villager is unowned</li>
 *   <li>{@code permissionsNbt} - null uses default permissions</li>
 * </ul>
 *
 * @param villagerUUID the UUID of the villager being synced
 * @param ownerUUID the UUID of the owner, or null if unowned
 * @param ownerName the display name of the owner, or null if unowned
 * @param permissionsNbt the permission settings as NBT, or null for defaults
 * @since 1.0.0
 */
public record OwnershipSyncPacket(
    UUID villagerUUID,
    @Nullable UUID ownerUUID,
    @Nullable String ownerName,
    @Nullable NbtCompound permissionsNbt
) implements CustomPayload {

    /**
     * Unique packet identifier for ownership synchronization.
     */
    public static final CustomPayload.Id<OwnershipSyncPacket> ID =
        new CustomPayload.Id<>(Identifier.of(XeenaaVillagerManager.MOD_ID, "ownership_sync"));

    /**
     * Packet codec for serialization/deserialization.
     *
     * <p>Handles nullable fields properly using custom encoding/decoding.</p>
     */
    public static final PacketCodec<RegistryByteBuf, OwnershipSyncPacket> CODEC =
        new PacketCodec<RegistryByteBuf, OwnershipSyncPacket>() {
            @Override
            public OwnershipSyncPacket decode(RegistryByteBuf buf) {
                // Villager UUID (required)
                UUID villagerUUID = Uuids.PACKET_CODEC.decode(buf);

                // Owner UUID (nullable - boolean flag + value)
                UUID ownerUUID = buf.readBoolean() ? Uuids.PACKET_CODEC.decode(buf) : null;

                // Owner name (nullable - boolean flag + value)
                String ownerName = buf.readBoolean() ? buf.readString() : null;

                // Permissions NBT (nullable - boolean flag + value)
                NbtCompound permissionsNbt = buf.readBoolean() ? buf.readNbt() : null;

                return new OwnershipSyncPacket(villagerUUID, ownerUUID, ownerName, permissionsNbt);
            }

            @Override
            public void encode(RegistryByteBuf buf, OwnershipSyncPacket packet) {
                // Villager UUID (required)
                Uuids.PACKET_CODEC.encode(buf, packet.villagerUUID());

                // Owner UUID (nullable - boolean flag + value)
                buf.writeBoolean(packet.ownerUUID() != null);
                if (packet.ownerUUID() != null) {
                    Uuids.PACKET_CODEC.encode(buf, packet.ownerUUID());
                }

                // Owner name (nullable - boolean flag + value)
                buf.writeBoolean(packet.ownerName() != null);
                if (packet.ownerName() != null) {
                    buf.writeString(packet.ownerName());
                }

                // Permissions NBT (nullable - boolean flag + value)
                buf.writeBoolean(packet.permissionsNbt() != null);
                if (packet.permissionsNbt() != null) {
                    buf.writeNbt(packet.permissionsNbt());
                }
            }
        };

    /**
     * Checks if this packet represents an owned villager.
     *
     * @return true if ownerUUID is non-null, false otherwise
     */
    public boolean isOwned() {
        return ownerUUID != null;
    }

    /**
     * Checks if this packet represents an unowned villager.
     *
     * @return true if ownerUUID is null, false otherwise
     */
    public boolean isUnowned() {
        return ownerUUID == null;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
