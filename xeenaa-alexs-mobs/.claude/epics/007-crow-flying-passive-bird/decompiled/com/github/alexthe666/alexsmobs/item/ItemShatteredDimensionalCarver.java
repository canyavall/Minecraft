/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.client.extensions.common.IClientItemExtensions
 */
package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityVoidPortal;
import com.github.alexthe666.alexsmobs.item.ItemDimensionalCarver;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemShatteredDimensionalCarver
extends ItemDimensionalCarver {
    public ItemShatteredDimensionalCarver(Item.Properties props) {
        super(props);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)AlexsMobs.PROXY.getISTERProperties());
    }

    @Override
    public void onPortalOpen(Level worldIn, LivingEntity player, EntityVoidPortal portal, Direction dir) {
        portal.setAttachmentFacing(dir);
        portal.setShattered(true);
        portal.setLifespan(2000);
        portal.exitDimension = worldIn.m_46472_();
        BlockPos playerPos = player.m_20183_();
        if (dir == Direction.DOWN) {
            portal.setDestination(new BlockPos(playerPos.m_123341_(), worldIn.m_141937_() + 1, playerPos.m_123343_()));
        } else if (dir == Direction.UP) {
            portal.setDestination(new BlockPos(playerPos.m_123341_(), worldIn.m_151558_() - 1, playerPos.m_123343_()));
        } else {
            double worldBorderDistance = worldIn.m_6857_().m_61941_((double)playerPos.m_123341_(), (double)playerPos.m_123343_()) - 5.0;
            BlockPos millionPos = playerPos.m_5484_(dir.m_122424_(), (int)Math.min(worldBorderDistance, 1000000.0));
            portal.setDestination(millionPos);
        }
    }
}

