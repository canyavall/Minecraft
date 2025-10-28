/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.fml.LogicalSide
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.inventory.MenuTransmutationTable;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageTransmuteFromMenu {
    private int playerId;
    private int choice;

    public MessageTransmuteFromMenu(int playerId, int choice) {
        this.playerId = playerId;
        this.choice = choice;
    }

    public MessageTransmuteFromMenu() {
    }

    public static MessageTransmuteFromMenu read(FriendlyByteBuf buf) {
        return new MessageTransmuteFromMenu(buf.readInt(), buf.readInt());
    }

    public static void write(MessageTransmuteFromMenu message, FriendlyByteBuf buf) {
        buf.writeInt(message.playerId);
        buf.writeInt(message.choice);
    }

    public static class Handler {
        public static void handle(MessageTransmuteFromMenu message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> {
                ServerPlayer player = ((NetworkEvent.Context)context.get()).getSender();
                if (((NetworkEvent.Context)context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = AlexsMobs.PROXY.getClientSidePlayer();
                }
                if (player.m_19879_() == message.playerId && player.f_36096_ instanceof MenuTransmutationTable) {
                    MenuTransmutationTable table = (MenuTransmutationTable)player.f_36096_;
                    table.transmute((Player)player, message.choice);
                }
            });
        }
    }
}

