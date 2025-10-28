/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.stats.Stats
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.PickaxeItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.item;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ItemGhostlyPickaxe
extends PickaxeItem {
    public ItemGhostlyPickaxe(Item.Properties props) {
        super((Tier)Tiers.IRON, 1, -2.8f, props);
    }

    public static boolean shouldStoreInGhost(LivingEntity player, ItemStack stack) {
        return player instanceof Player && ((Player)player).m_150109_().m_36062_() == -1;
    }

    public float m_8102_(ItemStack stack, BlockState blockState) {
        return blockState.m_204336_(BlockTags.f_144282_) ? 20.0f : 1.0f;
    }

    public boolean m_6813_(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (ItemGhostlyPickaxe.shouldStoreInGhost(user, stack)) {
            if (user instanceof Player) {
                Player player = (Player)user;
                player.m_36246_(Stats.f_12949_.m_12902_((Object)state.m_60734_()));
                player.m_36399_(0.005f);
            }
            if (!level.f_46443_) {
                BlockEntity blockentity = state.m_155947_() ? level.m_7702_(pos) : null;
                Block.m_49874_((BlockState)state, (ServerLevel)((ServerLevel)level), (BlockPos)pos, (BlockEntity)blockentity, (Entity)user, (ItemStack)stack).forEach(item -> ItemGhostlyPickaxe.putItemInGhostInventoryOrDrop(user, stack, item));
                state.m_222967_((ServerLevel)level, pos, stack, true);
                int fortuneLevel = EnchantmentHelper.m_44843_((Enchantment)Enchantments.f_44987_, (ItemStack)stack);
                int silkTouchLevel = EnchantmentHelper.m_44843_((Enchantment)Enchantments.f_44985_, (ItemStack)stack);
                int exp = state.getExpDrop((LevelReader)((ServerLevel)level), level.f_46441_, pos, fortuneLevel, silkTouchLevel);
                if (exp > 0) {
                    state.m_60734_().m_49805_((ServerLevel)level, pos, exp);
                }
            }
        }
        return super.m_6813_(stack, level, state, pos, user);
    }

    private static void putItemInGhostInventoryOrDrop(LivingEntity user, ItemStack pickaxe, ItemStack item) {
        CompoundTag compoundtag = pickaxe.m_41784_();
        SimpleContainer container = new SimpleContainer(9);
        if (compoundtag.m_128441_("Items")) {
            container.m_7797_(compoundtag.m_128437_("Items", 10));
        }
        if (user instanceof Player) {
            Player player = (Player)user;
            if (player.m_150109_().m_36054_(item)) {
                return;
            }
            if (container.m_19183_(item)) {
                ItemStack leftover = container.m_19173_(item);
                compoundtag.m_128365_("Items", (Tag)container.m_7927_());
                pickaxe.m_41751_(compoundtag);
                item = leftover;
            }
        }
        if (!item.m_41619_()) {
            user.m_19983_(item);
        }
    }

    public void m_6883_(ItemStack stack, Level level, Entity entity, int i, boolean offhand) {
        super.m_6883_(stack, level, entity, i, offhand);
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (player.f_19797_ % 3 == 0) {
                CompoundTag compoundtag = stack.m_41784_();
                SimpleContainer container = new SimpleContainer(9);
                boolean flag = false;
                if (compoundtag.m_128441_("Items")) {
                    container.m_7797_(compoundtag.m_128437_("Items", 10));
                }
                for (int slot = 0; slot < container.m_6643_(); ++slot) {
                    ItemStack stackAt = container.m_8020_(slot);
                    if (stackAt.m_41619_() || !player.m_36356_(stackAt)) continue;
                    container.m_7407_(slot, stack.m_41613_());
                    flag = true;
                    break;
                }
                if (flag) {
                    compoundtag.m_128365_("Items", (Tag)container.m_7927_());
                    stack.m_41751_(compoundtag);
                }
            }
        }
    }

    public boolean m_6832_(ItemStack pickaxe, ItemStack stack) {
        return stack.m_150930_(Items.f_42714_);
    }

    public void m_7373_(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_7373_(stack, worldIn, tooltip, flagIn);
        CompoundTag compoundtag = stack.m_41783_();
        if (compoundtag != null && compoundtag.m_128425_("Items", 9)) {
            SimpleContainer container = new SimpleContainer(9);
            container.m_7797_(compoundtag.m_128437_("Items", 10));
            int i = 0;
            int j = 0;
            for (int slot = 0; slot < container.m_6643_(); ++slot) {
                ItemStack itemstack = container.m_8020_(slot);
                if (itemstack.m_41619_()) continue;
                ++j;
                if (i > 4) continue;
                ++i;
                MutableComponent mutablecomponent = itemstack.m_41786_().m_6881_();
                mutablecomponent.m_130946_(" x").m_130946_(String.valueOf(itemstack.m_41613_()));
                tooltip.add((Component)mutablecomponent.m_130940_(ChatFormatting.DARK_AQUA));
            }
            if (j - i > 0) {
                tooltip.add((Component)Component.m_237110_((String)"container.shulkerBox.more", (Object[])new Object[]{j - i}).m_130944_(new ChatFormatting[]{ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC}));
            }
        }
    }

    private void dropAllContents(Level level, Vec3 vec3, ItemStack pickaxe) {
        CompoundTag compoundtag = pickaxe.m_41783_();
        if (compoundtag != null && compoundtag.m_128425_("Items", 9)) {
            SimpleContainer container = new SimpleContainer(9);
            container.m_7797_(compoundtag.m_128437_("Items", 10));
            for (int slot = 0; slot < container.m_6643_(); ++slot) {
                ItemEntity itemEntity;
                ItemStack itemstack = container.m_8020_(slot);
                if (itemstack.m_41619_() || !level.m_7967_((Entity)(itemEntity = new ItemEntity(level, vec3.f_82479_, vec3.f_82480_, vec3.f_82481_, itemstack.m_41777_())))) continue;
                container.m_7407_(slot, itemstack.m_41613_());
            }
            compoundtag.m_128365_("Items", (Tag)container.m_7927_());
            pickaxe.m_41751_(compoundtag);
        }
    }

    public void m_142023_(ItemEntity itemEntity) {
        this.dropAllContents(itemEntity.m_9236_(), itemEntity.m_20182_(), itemEntity.m_32055_());
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        int i = super.damageItem(stack, amount, entity, onBroken);
        if (i + stack.m_41773_() >= stack.m_41776_() && entity != null) {
            this.dropAllContents(entity.m_9236_(), entity.m_20182_(), stack);
        }
        return i;
    }

    public int getMaxDamage(ItemStack stack) {
        return 700;
    }
}

