/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.Citadel
 *  com.github.alexthe666.citadel.server.entity.CitadelEntityData
 *  com.github.alexthe666.citadel.server.message.PropertiesMessage
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.item.ItemRainbowJelly;
import com.github.alexthe666.alexsmobs.misc.AMSimplexNoise;
import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RainbowUtil {
    private static final String RAINBOW_TYPE = "RainbowTypeAlexsMobs";

    public static void setRainbowType(LivingEntity fabulous, int type) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag((LivingEntity)fabulous);
        tag.m_128405_(RAINBOW_TYPE, type);
        CitadelEntityData.setCitadelTag((LivingEntity)fabulous, (CompoundTag)tag);
        if (!fabulous.m_9236_().f_46443_) {
            Citadel.sendMSGToAll((Object)new PropertiesMessage("CitadelPatreonConfig", tag, fabulous.m_19879_()));
        } else {
            Citadel.sendMSGToServer((Object)new PropertiesMessage("CitadelPatreonConfig", tag, fabulous.m_19879_()));
        }
    }

    public static int getRainbowType(LivingEntity entity) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag((LivingEntity)entity);
        if (lassoedTag.m_128441_(RAINBOW_TYPE)) {
            return lassoedTag.m_128451_(RAINBOW_TYPE);
        }
        return 0;
    }

    public static int getRainbowTypeFromStack(ItemStack stack) {
        String name = stack.m_41611_().getString().toLowerCase(Locale.ROOT);
        return ItemRainbowJelly.RainbowType.getFromString(name).ordinal() + 1;
    }

    public static int calculateGlassColor(BlockPos pos) {
        float f = (float)AMConfig.rainbowGlassFidelity;
        float f1 = (float)((AMSimplexNoise.noise(((float)pos.m_123341_() + f) / f, ((float)pos.m_123342_() + f) / f, ((float)pos.m_123343_() + f) / f) + 1.0) * 0.5);
        return Color.HSBtoRGB(f1, 1.0f, 1.0f);
    }
}

