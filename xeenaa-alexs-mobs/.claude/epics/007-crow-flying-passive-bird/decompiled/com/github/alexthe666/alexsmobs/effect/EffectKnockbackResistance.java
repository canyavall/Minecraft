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

public class EffectKnockbackResistance
extends MobEffect {
    public EffectKnockbackResistance() {
        super(MobEffectCategory.BENEFICIAL, 8803127);
        this.m_19472_(Attributes.f_22278_, "03C3C89D-7037-4B42-869F-B146BCB64D2F", 0.5, AttributeModifier.Operation.ADDITION);
    }

    public void m_6742_(LivingEntity LivingEntityIn, int amplifier) {
    }

    public boolean m_6584_(int duration, int amplifier) {
        return duration > 0;
    }

    public String m_19481_() {
        return "alexsmobs.potion.knockback_resistance";
    }
}

