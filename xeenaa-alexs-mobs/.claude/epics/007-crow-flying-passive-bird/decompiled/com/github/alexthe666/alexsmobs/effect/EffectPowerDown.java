/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeMap
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.gameevent.GameEvent
 */
package com.github.alexthe666.alexsmobs.effect;

import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.gameevent.GameEvent;

public class EffectPowerDown
extends MobEffect {
    private int lastDuration = -1;
    private int firstDuration = -1;

    protected EffectPowerDown() {
        super(MobEffectCategory.NEUTRAL, 0);
        this.m_19472_(Attributes.f_22279_, "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    public void m_6742_(LivingEntity entity, int amplifier) {
        if (entity.m_20184_().f_82480_ > 0.0 && !entity.m_20072_()) {
            entity.m_20256_(entity.m_20184_().m_82542_(1.0, 0.0, 1.0));
        }
        if (this.firstDuration == this.lastDuration) {
            entity.m_5496_((SoundEvent)AMSoundRegistry.APRIL_FOOLS_POWER_OUTAGE.get(), 1.5f, 1.0f);
            entity.m_146850_(GameEvent.f_223709_);
        }
    }

    public int getActiveTime() {
        return this.firstDuration - this.lastDuration;
    }

    public boolean m_6584_(int duration, int amplifier) {
        this.lastDuration = duration;
        if (duration <= 0) {
            this.lastDuration = -1;
            this.firstDuration = -1;
        }
        if (this.firstDuration == -1) {
            this.firstDuration = duration;
        }
        return duration > 0;
    }

    public void m_6386_(LivingEntity entity, AttributeMap map, int i) {
        this.lastDuration = -1;
        this.firstDuration = -1;
        super.m_6386_(entity, map, i);
    }

    public void m_6385_(LivingEntity entity, AttributeMap map, int i) {
        this.lastDuration = -1;
        this.firstDuration = -1;
        super.m_6385_(entity, map, i);
    }

    public String m_19481_() {
        return "alexsmobs.potion.power_down";
    }
}

