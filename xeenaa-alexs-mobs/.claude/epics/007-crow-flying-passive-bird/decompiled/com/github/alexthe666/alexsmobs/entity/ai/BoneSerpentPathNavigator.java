/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Util
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.network.protocol.game.DebugPackets
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.pathfinder.Path
 *  net.minecraft.world.level.pathfinder.PathFinder
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentNodeProcessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BoneSerpentPathNavigator
extends PathNavigation {
    public BoneSerpentPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected PathFinder m_5532_(int p_179679_1_) {
        this.f_26508_ = new BoneSerpentNodeProcessor();
        return new PathFinder(this.f_26508_, p_179679_1_);
    }

    protected boolean m_7632_() {
        return true;
    }

    protected Vec3 m_7475_() {
        return new Vec3(this.f_26494_.m_20185_(), this.f_26494_.m_20227_(0.5), this.f_26494_.m_20189_());
    }

    public void m_7638_() {
        ++this.f_26498_;
        if (this.f_26506_) {
            this.m_26569_();
        }
        if (!this.m_26571_()) {
            if (this.m_7632_()) {
                this.m_7636_();
            } else if (this.f_26496_ != null && !this.f_26496_.m_77392_()) {
                Vec3 vector3d = this.f_26496_.m_77380_((Entity)this.f_26494_);
                if (Mth.m_14107_((double)this.f_26494_.m_20185_()) == Mth.m_14107_((double)vector3d.f_82479_) && Mth.m_14107_((double)this.f_26494_.m_20186_()) == Mth.m_14107_((double)vector3d.f_82480_) && Mth.m_14107_((double)this.f_26494_.m_20189_()) == Mth.m_14107_((double)vector3d.f_82481_)) {
                    this.f_26496_.m_77374_();
                }
            }
            DebugPackets.m_133703_((Level)this.f_26495_, (Mob)this.f_26494_, (Path)this.f_26496_, (float)this.f_26505_);
            if (!this.m_26571_()) {
                Vec3 vector3d1 = this.f_26496_.m_77380_((Entity)this.f_26494_);
                this.f_26494_.m_21566_().m_6849_(vector3d1.f_82479_, vector3d1.f_82480_, vector3d1.f_82481_, this.f_26497_);
            }
        }
    }

    protected void m_7636_() {
        if (this.f_26496_ != null) {
            Vec3 vector3d = this.m_7475_();
            float f = this.f_26494_.m_20205_();
            float f1 = 3.0f;
            Vec3 vector3d1 = this.f_26494_.m_20184_();
            if (Math.abs(vector3d1.f_82479_) > 0.2 || Math.abs(vector3d1.f_82481_) > 0.2) {
                f1 = (float)((double)f1 * vector3d1.m_82553_() * 6.0);
            }
            int i = 6;
            Vec3 vector3d2 = Vec3.m_82539_((Vec3i)this.f_26496_.m_77400_());
            if (Math.abs(this.f_26494_.m_20185_() - vector3d2.f_82479_) < (double)f1 && Math.abs(this.f_26494_.m_20189_() - vector3d2.f_82481_) < (double)f1 && Math.abs(this.f_26494_.m_20186_() - vector3d2.f_82480_) < (double)(f1 * 2.0f)) {
                this.f_26496_.m_77374_();
            }
            for (int j = Math.min(this.f_26496_.m_77399_() + 6, this.f_26496_.m_77398_() - 1); j > this.f_26496_.m_77399_(); --j) {
                vector3d2 = this.f_26496_.m_77382_((Entity)this.f_26494_, j);
                if (vector3d2.m_82557_(vector3d) > 36.0 || !this.canMoveDirectly(vector3d, vector3d2, 0, 0, 0)) continue;
                this.f_26496_.m_77393_(j);
                break;
            }
            this.m_6481_(vector3d);
        }
    }

    protected void m_6481_(Vec3 positionVec3) {
        if (this.f_26498_ - this.f_26499_ > 100) {
            if (positionVec3.m_82557_(this.f_26500_) < 2.25) {
                this.m_26573_();
            }
            this.f_26499_ = this.f_26498_;
            this.f_26500_ = positionVec3;
        }
        if (this.f_26496_ != null && !this.f_26496_.m_77392_()) {
            BlockPos vector3i = this.f_26496_.m_77400_();
            if (vector3i.equals((Object)this.f_26501_)) {
                this.f_26502_ += Util.m_137550_() - this.f_26503_;
            } else {
                this.f_26501_ = vector3i;
                double d0 = positionVec3.m_82554_(Vec3.m_82512_((Vec3i)this.f_26501_));
                double d = this.f_26504_ = this.f_26494_.m_6113_() > 0.0f ? d0 / (double)this.f_26494_.m_6113_() * 100.0 : 0.0;
            }
            if (this.f_26504_ > 0.0 && (double)this.f_26502_ > this.f_26504_ * 2.0) {
                this.f_26501_ = Vec3i.f_123288_;
                this.f_26502_ = 0L;
                this.f_26504_ = 0.0;
                this.m_26573_();
            }
            this.f_26503_ = Util.m_137550_();
        }
    }

    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
        Vec3 vector3d = new Vec3(posVec32.f_82479_, posVec32.f_82480_ + (double)this.f_26494_.m_20206_() * 0.5, posVec32.f_82481_);
        return this.f_26495_.m_45547_(new ClipContext(posVec31, vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this.f_26494_)).m_6662_() == HitResult.Type.MISS;
    }

    public boolean m_6342_(BlockPos pos) {
        return !this.f_26495_.m_8055_(pos).m_60804_((BlockGetter)this.f_26495_, pos);
    }

    public void m_7008_(boolean canSwim) {
    }
}

