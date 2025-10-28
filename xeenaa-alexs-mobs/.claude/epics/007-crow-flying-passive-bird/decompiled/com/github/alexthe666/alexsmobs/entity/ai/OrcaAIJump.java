/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.ai.goal.JumpGoal
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityOrca;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class OrcaAIJump
extends JumpGoal {
    private static final int[] JUMP_DISTANCES = new int[]{0, 1, 4, 5, 6, 7, 10};
    private final EntityOrca dolphin;
    private final int interval;
    private boolean inWater;

    public OrcaAIJump(EntityOrca dolphin, int p_i50329_2_) {
        this.dolphin = dolphin;
        this.interval = p_i50329_2_;
    }

    public boolean m_8036_() {
        if (this.dolphin.m_217043_().m_188503_(this.interval) != 0 || this.dolphin.m_5448_() != null || this.dolphin.jumpCooldown != 0) {
            return false;
        }
        Direction direction = this.dolphin.m_6374_();
        int i = direction.m_122429_();
        int j = direction.m_122431_();
        BlockPos blockpos = this.dolphin.m_20183_();
        for (int k : JUMP_DISTANCES) {
            if (this.canJumpTo(blockpos, i, j, k) && this.isAirAbove(blockpos, i, j, k)) continue;
            return false;
        }
        return true;
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.m_7918_(dx * scale, 0, dz * scale);
        return this.dolphin.m_9236_().m_6425_(blockpos).m_205070_(FluidTags.f_13131_) && !this.dolphin.m_9236_().m_8055_(blockpos).m_280555_();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.dolphin.m_9236_().m_8055_(pos.m_7918_(dx * scale, 1, dz * scale)).m_60795_() && this.dolphin.m_9236_().m_8055_(pos.m_7918_(dx * scale, 2, dz * scale)).m_60795_();
    }

    public boolean m_8045_() {
        double d0 = this.dolphin.m_20184_().f_82480_;
        return !(this.dolphin.jumpCooldown <= 0 || d0 * d0 < (double)0.03f && this.dolphin.m_146909_() != 0.0f && Math.abs(this.dolphin.m_146909_()) < 10.0f && this.dolphin.m_20069_() || this.dolphin.m_20096_());
    }

    public boolean m_6767_() {
        return false;
    }

    public void m_8056_() {
        Direction direction = this.dolphin.m_6374_();
        float up = 0.7f + this.dolphin.m_217043_().m_188501_() * 0.8f;
        this.dolphin.m_20256_(this.dolphin.m_20184_().m_82520_((double)direction.m_122429_() * 0.6, (double)up, (double)direction.m_122431_() * 0.6));
        this.dolphin.m_21573_().m_26573_();
        this.dolphin.jumpCooldown = this.dolphin.m_217043_().m_188503_(256) + 256;
    }

    public void m_8041_() {
        this.dolphin.m_146926_(0.0f);
    }

    public void m_8037_() {
        boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.dolphin.m_9236_().m_6425_(this.dolphin.m_20183_());
            this.inWater = fluidstate.m_205070_(FluidTags.f_13131_);
        }
        if (this.inWater && !flag) {
            this.dolphin.m_5496_(SoundEvents.f_11805_, 1.0f, 1.0f);
        }
        Vec3 vector3d = this.dolphin.m_20184_();
        if (vector3d.f_82480_ * vector3d.f_82480_ < (double)0.1f && this.dolphin.m_146909_() != 0.0f) {
            this.dolphin.m_146926_(Mth.m_14189_((float)this.dolphin.m_146909_(), (float)0.0f, (float)0.2f));
        } else {
            double d0 = Math.sqrt(vector3d.m_165925_());
            double d1 = Math.signum(-vector3d.f_82480_) * Math.acos(d0 / vector3d.m_82553_()) * 57.2957763671875;
            this.dolphin.m_146926_((float)d1);
        }
    }
}

