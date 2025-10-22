package com.xeenaa.villagermanager.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Client-to-Server packet requesting to unbind (release) a villager.
 *
 * <p>When a player clicks the "Release Ownership" button in the management GUI
 * and confirms the action, this packet is sent to the server to release ownership.</p>
 *
 * <p>Server-side validation includes:</p>
 * <ul>
 *   <li>Villager exists and is loaded</li>
 *   <li>Player is the current owner</li>
 *   <li>Player is within 10 blocks of villager</li>
 *   <li>Rate limiting (max 1 unbind per 2 seconds per player)</li>
 * </ul>
 *
 * @param villagerEntityId the entity ID of the villager to unbind
 * @since 1.0.0
 */
public record UnbindVillagerPacket(int villagerEntityId) implements CustomPayload {

    /**
     * Unique packet identifier for unbind villager requests.
     */
    public static final CustomPayload.Id<UnbindVillagerPacket> ID =
        new CustomPayload.Id<>(Identifier.of(XeenaaVillagerManager.MOD_ID, "unbind_villager"));

    /**
     * Packet codec for serialization/deserialization.
     * Uses variable-length integer encoding for efficient network transmission.
     */
    public static final PacketCodec<RegistryByteBuf, UnbindVillagerPacket> CODEC =
        PacketCodec.tuple(
            PacketCodecs.VAR_INT, UnbindVillagerPacket::villagerEntityId,
            UnbindVillagerPacket::new
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
