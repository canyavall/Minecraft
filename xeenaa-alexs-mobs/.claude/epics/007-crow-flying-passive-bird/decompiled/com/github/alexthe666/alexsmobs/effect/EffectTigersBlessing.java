/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 */
package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectTigersBlessing
extends MobEffect {
    protected EffectTigersBlessing() {
        super(MobEffectCategory.BENEFICIAL, 16766814);
    }

    public void m_6742_(LivingEntity entity, int amplifier) {
    }

    public boolean m_6584_(int duration, int amplifier) {
        return duration > 0;
    }

    public String m_19481_() {
        return "alexsmobs.potion.tigers_blessing";
    }
}

