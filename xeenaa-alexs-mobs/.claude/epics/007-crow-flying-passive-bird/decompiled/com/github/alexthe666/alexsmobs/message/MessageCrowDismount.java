/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraftforge.fml.LogicalSide
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageCrowDismount {
    public int rider;
    public int mount;

    public MessageCrowDismount(int rider, int mount) {
        this.rider = rider;
        this.mount = mount;
    }

    public MessageCrowDismount() {
    }

    public static MessageCrowDismount read(FriendlyByteBuf buf) {
        return new MessageCrowDismount(buf.readInt(), buf.readInt());
    }

    public static void write(MessageCrowDismount message, FriendlyByteBuf buf) {
        buf.writeInt(message.rider);
        buf.writeInt(message.mount);
    }

    public static class Handler {
        public static void handle(MessageCrowDismount message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> {
                ServerPlayer player = ((NetworkEvent.Context)context.get()).getSender();
                if (((NetworkEvent.Context)context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = AlexsMobs.PROXY.getClientSidePlayer();
                }
                if (player != null && player.m_9236_() != null) {
                    Entity entity = player.m_9236_().m_6815_(message.rider);
                    Entity mountEntity = player.m_9236_().m_6815_(message.mount);
                    if (entity instanceof EntityCrow && mountEntity != null) {
                        entity.m_8127_();
                    }
                }
            });
        }
    }
}

