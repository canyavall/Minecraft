/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.control.MoveControl$Operation
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class EtherealMoveController
extends MoveControl {
    private final Mob parentEntity;
    private final float speedGeneral;

    public EtherealMoveController(Mob parentEntity, float speedGeneral) {
        super(parentEntity);
        this.parentEntity = parentEntity;
        this.speedGeneral = speedGeneral;
    }

    public void m_8126_() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
            double d0 = vector3d.m_82553_();
            this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82549_(vector3d.m_82490_(this.f_24978_ * (double)this.speedGeneral * 0.025 / d0)));
            double yAdd = this.f_24976_ - this.parentEntity.m_20186_();
            if (d0 > (double)this.parentEntity.m_20205_()) {
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82520_(0.0, (double)this.parentEntity.m_6113_() * (double)this.speedGeneral * Mth.m_14008_((double)yAdd, (double)-1.0, (double)1.0) * (double)0.6f, 0.0));
                Vec3 vector3d1 = this.parentEntity.m_20184_();
                this.parentEntity.m_146922_(-((float)Mth.m_14136_((double)vector3d1.f_82479_, (double)vector3d1.f_82481_)) * 57.295776f);
                this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
            }
        } else if (this.f_24981_ == MoveControl.Operation.STRAFE || this.f_24981_ == MoveControl.Operation.JUMPING) {
            this.f_24981_ = MoveControl.Operation.WAIT;
        }
    }
}

