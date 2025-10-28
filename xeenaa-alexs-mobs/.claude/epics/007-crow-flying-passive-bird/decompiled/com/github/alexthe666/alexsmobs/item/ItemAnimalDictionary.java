/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.stats.Stats
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 */
package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityAnaconda;
import com.github.alexthe666.alexsmobs.entity.EntityAnacondaPart;
import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpentPart;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeBody;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeHead;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeTail;
import com.github.alexthe666.alexsmobs.entity.EntityMurmur;
import com.github.alexthe666.alexsmobs.entity.EntityMurmurHead;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWormPart;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemAnimalDictionary
extends Item {
    private boolean usedOnEntity = false;

    public ItemAnimalDictionary(Item.Properties properties) {
        super(properties);
    }

    public InteractionResult m_6880_(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        if (playerIn instanceof ServerPlayer) {
            ServerPlayer serverplayerentity = (ServerPlayer)playerIn;
            CriteriaTriggers.f_10592_.m_23682_(serverplayerentity, itemStackIn);
            serverplayerentity.m_36246_(Stats.f_12982_.m_12902_((Object)this));
        }
        if (playerIn.m_9236_().f_46443_ && target.m_20078_() != null && target.m_20078_().contains("alexsmobs:")) {
            this.usedOnEntity = true;
            String id = target.m_20078_().replace("alexsmobs:", "");
            if (target instanceof EntityBoneSerpent || target instanceof EntityBoneSerpentPart) {
                id = "bone_serpent";
            }
            if (target instanceof EntityCentipedeHead || target instanceof EntityCentipedeBody || target instanceof EntityCentipedeTail) {
                id = "cave_centipede";
            }
            if (target instanceof EntityVoidWorm || target instanceof EntityVoidWormPart) {
                id = "void_worm";
            }
            if (target instanceof EntityAnaconda || target instanceof EntityAnacondaPart) {
                id = "anaconda";
            }
            if (target instanceof EntityMurmur || target instanceof EntityMurmurHead) {
                id = "murmur";
            }
            AlexsMobs.PROXY.openBookGUI(itemStackIn, id);
        }
        return InteractionResult.CONSUME;
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.m_21120_(handIn);
        if (!this.usedOnEntity) {
            if (playerIn instanceof ServerPlayer) {
                ServerPlayer serverplayerentity = (ServerPlayer)playerIn;
                CriteriaTriggers.f_10592_.m_23682_(serverplayerentity, itemStackIn);
                serverplayerentity.m_36246_(Stats.f_12982_.m_12902_((Object)this));
            }
            if (worldIn.f_46443_) {
                AlexsMobs.PROXY.openBookGUI(itemStackIn);
            }
        }
        this.usedOnEntity = false;
        return new InteractionResultHolder(InteractionResult.PASS, (Object)itemStackIn);
    }

    public void m_7373_(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add((Component)Component.m_237115_((String)"item.alexsmobs.animal_dictionary.desc").m_130940_(ChatFormatting.GRAY));
    }
}

