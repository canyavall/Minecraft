/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class CrowAIMelee
extends Goal {
    private final EntityCrow crow;
    float circlingTime = 0.0f;
    float circleDistance = 1.0f;
    float yLevel = 2.0f;
    boolean clockwise = false;
    private int maxCircleTime;

    public CrowAIMelee(EntityCrow crow) {
        this.crow = crow;
    }

    public boolean m_8036_() {
        return this.crow.m_5448_() != null && !this.crow.isSitting() && this.crow.getCommand() != 3;
    }

    public void m_8056_() {
        this.clockwise = this.crow.m_217043_().m_188499_();
        this.yLevel = this.crow.m_217043_().m_188503_(2);
        this.circlingTime = 0.0f;
        this.maxCircleTime = 20 + this.crow.m_217043_().m_188503_(100);
        this.circleDistance = 1.0f + this.crow.m_217043_().m_188501_() * 3.0f;
    }

    public void m_8041_() {
        this.clockwise = this.crow.m_217043_().m_188499_();
        this.yLevel = this.crow.m_217043_().m_188503_(2);
        this.circlingTime = 0.0f;
        this.maxCircleTime = 20 + this.crow.m_217043_().m_188503_(100);
        this.circleDistance = 1.0f + this.crow.m_217043_().m_188501_() * 3.0f;
        if (this.crow.m_20096_()) {
            this.crow.setFlying(false);
        }
    }

    public void m_8037_() {
        LivingEntity target = this.crow.m_5448_();
        if (target != null) {
            if (this.circlingTime > (float)this.maxCircleTime) {
                this.crow.m_21566_().m_6849_(target.m_20185_(), target.m_20186_() + (double)(target.m_20192_() / 2.0f), target.m_20189_(), (double)1.3f);
                if (this.crow.m_20270_((Entity)target) < 2.0f) {
                    this.crow.peck();
                    if (target.m_6336_() == MobType.f_21641_) {
                        target.m_6469_(target.m_269291_().m_269264_(), 4.0f);
                    } else {
                        target.m_6469_(target.m_269291_().m_269264_(), 1.0f);
                    }
                    this.m_8041_();
                }
            } else {
                Vec3 circlePos = this.getVultureCirclePos(target.m_20182_());
                if (circlePos == null) {
                    circlePos = target.m_20182_();
                }
                this.crow.setFlying(true);
                this.crow.m_21566_().m_6849_(circlePos.m_7096_(), circlePos.m_7098_() + (double)target.m_20192_() + (double)0.2f, circlePos.m_7094_(), 1.0);
            }
        }
        if (this.crow.isFlying()) {
            this.circlingTime += 1.0f;
        }
    }

    public Vec3 getVultureCirclePos(Vec3 target) {
        float angle = 0.13962634f * (this.clockwise ? -this.circlingTime : this.circlingTime);
        double extraX = this.circleDistance * Mth.m_14031_((float)angle);
        double extraZ = this.circleDistance * Mth.m_14089_((float)angle);
        Vec3 pos = new Vec3(target.m_7096_() + extraX, target.m_7098_() + (double)this.yLevel, target.m_7094_() + extraZ);
        if (this.crow.m_9236_().m_46859_(AMBlockPos.fromVec3(pos))) {
            return pos;
        }
        return null;
    }
}

