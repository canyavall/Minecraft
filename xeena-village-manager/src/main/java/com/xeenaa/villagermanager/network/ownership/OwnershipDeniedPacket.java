package com.xeenaa.villagermanager.network.ownership;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Server-to-Client packet indicating an ownership operation was denied.
 *
 * <p>This packet is sent when the server rejects a bind or unbind request
 * due to validation failures. It provides a specific reason code that the
 * client can use to display an appropriate error message.</p>
 *
 * <p>Common reason codes:</p>
 * <ul>
 *   <li>{@code "already_owned"} - Villager is already owned by another player</li>
 *   <li>{@code "not_owner"} - Player is not the owner (for unbind requests)</li>
 *   <li>{@code "too_far"} - Player is more than 10 blocks away</li>
 *   <li>{@code "rate_limited"} - Player is on cooldown (2 second limit)</li>
 *   <li>{@code "entity_not_found"} - Villager entity doesn't exist or isn't loaded</li>
 *   <li>{@code "invalid_entity"} - Entity exists but is not a villager</li>
 * </ul>
 *
 * @param reason a code identifying why the operation was denied
 * @since 1.0.0
 */
public record OwnershipDeniedPacket(String reason) implements CustomPayload {

    /**
     * Unique packet identifier for ownership denial notifications.
     */
    public static final CustomPayload.Id<OwnershipDeniedPacket> ID =
        new CustomPayload.Id<>(Identifier.of(XeenaaVillagerManager.MOD_ID, "ownership_denied"));

    /**
     * Packet codec for serialization/deserialization.
     * Uses standard string encoding with UTF-8 charset.
     */
    public static final PacketCodec<RegistryByteBuf, OwnershipDeniedPacket> CODEC =
        PacketCodec.tuple(
            PacketCodecs.STRING, OwnershipDeniedPacket::reason,
            OwnershipDeniedPacket::new
        );

    /**
     * Gets a user-friendly error message for the reason code.
     *
     * @return a formatted error message suitable for display
     */
    public String getUserMessage() {
        return switch (reason) {
            case "already_owned" -> "This villager is already owned by another player";
            case "not_owner" -> "You are not the owner of this villager";
            case "too_far" -> "You must be within 10 blocks of the villager";
            case "rate_limited" -> "Please wait before trying again";
            case "entity_not_found" -> "Villager not found or not loaded";
            case "invalid_entity" -> "Target is not a valid villager";
            default -> "Operation failed: " + reason;
        };
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
