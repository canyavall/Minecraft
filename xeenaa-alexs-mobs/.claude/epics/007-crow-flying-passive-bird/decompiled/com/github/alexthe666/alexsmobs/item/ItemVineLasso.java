/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.client.extensions.common.IClientItemExtensions
 */
package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityVineLasso;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemVineLasso
extends Item {
    public ItemVineLasso(Item.Properties props) {
        super(props);
    }

    public UseAnim m_6164_(ItemStack stack) {
        return UseAnim.BOW;
    }

    public static boolean isItemInUse(ItemStack stack) {
        return stack.m_41783_() != null && stack.m_41783_().m_128441_("Swinging") && stack.m_41783_().m_128471_("Swinging");
    }

    public void m_6883_(ItemStack stack, Level world, Entity entity, int i, boolean b) {
        if (entity instanceof LivingEntity) {
            if (stack.m_41783_() != null) {
                stack.m_41783_().m_128379_("Swinging", ((LivingEntity)entity).m_21211_() == stack && ((LivingEntity)entity).m_6117_());
            } else {
                stack.m_41751_(new CompoundTag());
            }
        }
    }

    public int m_8105_(ItemStack p_40680_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.m_21120_(p_40674_);
        p_40673_.m_6672_(p_40674_);
        return InteractionResultHolder.m_19090_((Object)itemstack);
    }

    public void m_5929_(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (count % 7 == 0) {
            livingEntityIn.m_146850_(GameEvent.f_223698_);
            livingEntityIn.m_5496_((SoundEvent)AMSoundRegistry.VINE_LASSO.get(), 1.0f, 1.0f + (livingEntityIn.m_217043_().m_188501_() - livingEntityIn.m_217043_().m_188501_()) * 0.2f);
        }
    }

    public void m_5551_(ItemStack stack, Level worldIn, LivingEntity livingEntityIn, int i) {
        if (!worldIn.f_46443_) {
            boolean left = false;
            if (livingEntityIn.m_7655_() == InteractionHand.OFF_HAND && livingEntityIn.m_5737_() == HumanoidArm.RIGHT || livingEntityIn.m_7655_() == InteractionHand.MAIN_HAND && livingEntityIn.m_5737_() == HumanoidArm.LEFT) {
                left = true;
            }
            int power = this.m_8105_(stack) - i;
            EntityVineLasso lasso = new EntityVineLasso(worldIn, livingEntityIn);
            Vec3 vector3d = livingEntityIn.m_20252_(1.0f);
            lasso.shoot(vector3d.m_7096_(), vector3d.m_7098_(), vector3d.m_7094_(), ItemVineLasso.getPowerForTime(power), 1.0f);
            if (!worldIn.f_46443_) {
                worldIn.m_7967_((Entity)lasso);
            }
            stack.m_41774_(1);
        }
    }

    public static float getPowerForTime(int p) {
        float f = (float)p / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)AlexsMobs.PROXY.getISTERProperties());
    }
}

