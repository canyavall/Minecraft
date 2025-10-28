/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;

public class BoneSerpentAIFindLava
extends Goal {
    private final EntityBoneSerpent creature;
    private BlockPos targetPos;

    public BoneSerpentAIFindLava(EntityBoneSerpent creature) {
        this.creature = creature;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean m_8036_() {
        if (!(this.creature.jumpCooldown != 0 && !this.creature.m_20096_() || this.creature.m_9236_().m_6425_(this.creature.m_20183_()).m_205070_(FluidTags.f_13131_) || this.creature.m_9236_().m_6425_(this.creature.m_20183_()).m_205070_(FluidTags.f_13132_))) {
            this.targetPos = this.generateTarget();
            return this.targetPos != null;
        }
        return false;
    }

    public void m_8056_() {
        if (this.targetPos != null) {
            this.creature.m_21573_().m_26519_((double)this.targetPos.m_123341_(), (double)this.targetPos.m_123342_(), (double)this.targetPos.m_123343_(), 0.5);
        }
    }

    public boolean m_8045_() {
        return !this.creature.m_21573_().m_26571_() && this.targetPos != null && !this.creature.m_9236_().m_6425_(this.creature.m_20183_()).m_205070_(FluidTags.f_13131_) && !this.creature.m_9236_().m_6425_(this.creature.m_20183_()).m_205070_(FluidTags.f_13132_);
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        RandomSource random = this.creature.m_217043_();
        int range = 16;
        for (int i = 0; i < 15; ++i) {
            BlockPos blockpos1 = this.creature.m_20183_().m_7918_(random.m_188503_(range) - range / 2, 3, random.m_188503_(range) - range / 2);
            while (this.creature.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.m_7495_();
            }
            if (!this.creature.m_9236_().m_6425_(blockpos1).m_205070_(FluidTags.f_13131_) && !this.creature.m_9236_().m_6425_(blockpos1).m_205070_(FluidTags.f_13132_)) continue;
            blockpos = blockpos1;
        }
        return blockpos;
    }
}

