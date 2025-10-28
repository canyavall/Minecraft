/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 */
package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectExsanguination
extends MobEffect {
    private int lastDuration = -1;

    protected EffectExsanguination() {
        super(MobEffectCategory.HARMFUL, 15552849);
    }

    public void m_6742_(LivingEntity entity, int amplifier) {
        entity.m_6469_(entity.m_269291_().m_269425_(), (float)Math.min(amplifier + 1, Math.round((float)this.lastDuration / 20.0f)));
        for (int i = 0; i < 3; ++i) {
            entity.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123798_, entity.m_20208_(1.0), entity.m_20187_(), entity.m_20262_(1.0), 0.0, 0.0, 0.0);
        }
    }

    public boolean m_6584_(int duration, int amplifier) {
        this.lastDuration = duration;
        return duration > 0 && duration % 20 == 0;
    }

    public String m_19481_() {
        return "alexsmobs.potion.exsanguination";
    }
}

