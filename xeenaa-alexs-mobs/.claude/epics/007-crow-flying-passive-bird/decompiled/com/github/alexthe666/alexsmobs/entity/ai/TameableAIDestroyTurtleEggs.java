/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.goal.RemoveBlockGoal
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Blocks
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class TameableAIDestroyTurtleEggs
extends RemoveBlockGoal {
    public TameableAIDestroyTurtleEggs(TamableAnimal creatureIn, double speed, int yMax) {
        super(Blocks.f_50578_, (PathfinderMob)creatureIn, speed, yMax);
        this.f_25600_ = 800;
    }

    public boolean m_8036_() {
        return !((TamableAnimal)this.f_25598_).m_21824_() && super.m_8036_();
    }

    public boolean m_8045_() {
        return !((TamableAnimal)this.f_25598_).m_21824_() && super.m_8045_();
    }

    public void m_7659_(LevelAccessor worldIn, BlockPos pos) {
        worldIn.m_5594_(null, pos, SoundEvents.f_12604_, SoundSource.HOSTILE, 0.5f, 0.9f + this.f_25598_.m_217043_().m_188501_() * 0.2f);
    }

    protected int m_6099_(PathfinderMob mob) {
        return TameableAIDestroyTurtleEggs.m_186073_((int)(800 + mob.m_217043_().m_188503_(800)));
    }

    public void m_5777_(Level worldIn, BlockPos pos) {
        worldIn.m_5594_(null, pos, SoundEvents.f_12533_, SoundSource.BLOCKS, 0.7f, 0.9f + worldIn.f_46441_.m_188501_() * 0.2f);
    }

    public double m_8052_() {
        return 1.14;
    }
}

