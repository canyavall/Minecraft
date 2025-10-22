package com.xeenaa.villagermanager.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Client-to-Server packet requesting to bind (claim) a villager.
 *
 * <p>When a player clicks the "Claim Villager" button in the management GUI,
 * this packet is sent to the server to request ownership of the villager.</p>
 *
 * <p>Server-side validation includes:</p>
 * <ul>
 *   <li>Villager exists and is loaded</li>
 *   <li>Player is within 10 blocks of villager</li>
 *   <li>Villager is not already owned</li>
 *   <li>Rate limiting (max 1 bind per 2 seconds per player)</li>
 * </ul>
 *
 * @param villagerEntityId the entity ID of the villager to bind
 * @since 1.0.0
 */
public record BindVillagerPacket(int villagerEntityId) implements CustomPayload {

    /**
     * Unique packet identifier for bind villager requests.
     */
    public static final CustomPayload.Id<BindVillagerPacket> ID =
        new CustomPayload.Id<>(Identifier.of(XeenaaVillagerManager.MOD_ID, "bind_villager"));

    /**
     * Packet codec for serialization/deserialization.
     * Uses variable-length integer encoding for efficient network transmission.
     */
    public static final PacketCodec<RegistryByteBuf, BindVillagerPacket> CODEC =
        PacketCodec.tuple(
            PacketCodecs.VAR_INT, BindVillagerPacket::villagerEntityId,
            BindVillagerPacket::new
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
