/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.animal.Animal
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;

public class AnimalAIHurtByTargetNotBaby
extends HurtByTargetGoal {
    private final Animal animal;

    public AnimalAIHurtByTargetNotBaby(Animal creatureIn, Class<?> ... excludeReinforcementTypes) {
        super((PathfinderMob)creatureIn, (Class[])excludeReinforcementTypes);
        this.animal = creatureIn;
    }

    public void m_8056_() {
        super.m_8056_();
        if (this.animal.m_6162_()) {
            this.m_26047_();
            this.m_8041_();
        }
    }

    protected void m_5766_(Mob mobIn, LivingEntity targetIn) {
        if (!mobIn.m_6162_()) {
            super.m_5766_(mobIn, targetIn);
        }
    }
}

