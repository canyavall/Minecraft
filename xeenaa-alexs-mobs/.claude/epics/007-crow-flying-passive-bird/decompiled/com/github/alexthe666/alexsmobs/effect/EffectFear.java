/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 */
package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectFear
extends MobEffect {
    protected EffectFear() {
        super(MobEffectCategory.NEUTRAL, 0x7474F7);
        this.m_19472_(Attributes.f_22279_, "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    public void m_6742_(LivingEntity entity, int amplifier) {
        if (entity.m_20184_().f_82480_ > 0.0 && !entity.m_20072_()) {
            entity.m_20256_(entity.m_20184_().m_82542_(1.0, 0.0, 1.0));
        }
    }

    public boolean m_6584_(int duration, int amplifier) {
        return duration > 0;
    }

    public String m_19481_() {
        return "alexsmobs.potion.fear";
    }
}

