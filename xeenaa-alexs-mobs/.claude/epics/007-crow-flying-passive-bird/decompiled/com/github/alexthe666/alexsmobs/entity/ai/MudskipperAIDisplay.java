/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMudskipper;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MudskipperAIDisplay
extends Goal {
    private static final TargetingConditions JOSTLE_PREDICATE = TargetingConditions.m_148353_().m_26883_(16.0).m_148355_();
    protected EntityMudskipper partner;
    private EntityMudskipper mudskipper;
    private Level world;
    private float angle;
    private Vec3 center = null;

    public MudskipperAIDisplay(EntityMudskipper mudskipper) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        this.mudskipper = mudskipper;
        this.world = mudskipper.m_9236_();
    }

    public boolean m_8036_() {
        if (this.mudskipper.isDisplaying() || this.mudskipper.shouldFollow() || this.mudskipper.m_21827_() || this.mudskipper.m_27593_() || this.mudskipper.m_20160_() || this.mudskipper.m_20159_() || this.mudskipper.m_6162_() || this.mudskipper.m_5448_() != null || this.mudskipper.m_20096_() || this.mudskipper.displayCooldown > 0) {
            return false;
        }
        if (this.mudskipper.instantlyTriggerDisplayAI || this.mudskipper.m_217043_().m_188503_(30) == 0) {
            this.mudskipper.instantlyTriggerDisplayAI = false;
            if (this.mudskipper.getDisplayingPartner() instanceof EntityMudskipper) {
                this.partner = (EntityMudskipper)this.mudskipper.getDisplayingPartner();
                return this.partner.displayCooldown == 0;
            }
            EntityMudskipper possiblePartner = this.getNearbyMudskipper();
            if (possiblePartner != null) {
                this.mudskipper.setDisplayingPartner((Entity)possiblePartner);
                possiblePartner.setDisplayingPartner((Entity)this.mudskipper);
                this.partner = possiblePartner;
                this.partner.instantlyTriggerDisplayAI = true;
                return true;
            }
        }
        return false;
    }

    public void m_8056_() {
        this.mudskipper.displayTimer = 0;
        this.angle = 0.0f;
        this.setDisplayDirection(this.mudskipper.m_217043_().m_188499_());
    }

    public void setDisplayDirection(boolean dir) {
        this.mudskipper.displayDirection = dir;
        this.partner.displayDirection = !dir;
    }

    public void m_8041_() {
        this.center = null;
        this.mudskipper.setDisplaying(false);
        this.mudskipper.setDisplayingPartner(null);
        this.mudskipper.displayTimer = 0;
        this.angle = 0.0f;
        this.mudskipper.m_21573_().m_26573_();
        if (this.partner != null) {
            this.partner.setDisplaying(false);
            this.partner.setDisplayingPartner(null);
            this.partner.displayTimer = 0;
            this.partner = null;
        }
    }

    public void m_8037_() {
        if (this.partner != null) {
            if (this.center == null || this.mudskipper.m_217043_().m_188503_(100) == 0) {
                this.center = new Vec3((this.mudskipper.m_20185_() + this.partner.m_20185_()) / 2.0, (this.mudskipper.m_20186_() + this.partner.m_20186_()) / 2.0, (this.mudskipper.m_20189_() + this.partner.m_20189_()) / 2.0);
            }
            this.mudskipper.setDisplaying(true);
            float x = (float)(this.mudskipper.m_20185_() - this.partner.m_20185_());
            float y = Math.abs((float)(this.mudskipper.m_20186_() - this.partner.m_20186_()));
            float z = (float)(this.mudskipper.m_20189_() - this.partner.m_20189_());
            double distXZ = Math.sqrt(x * x + z * z);
            if (distXZ > 3.0) {
                this.mudskipper.m_21573_().m_5624_((Entity)this.partner, 1.0);
            } else {
                float speed = this.mudskipper.m_217043_().m_188501_() * 0.5f + 0.8f;
                if (this.mudskipper.displayDirection) {
                    if (this.angle < 180.0f) {
                        this.angle += 10.0f;
                    } else {
                        this.mudskipper.displayDirection = false;
                    }
                }
                if (!this.mudskipper.displayDirection) {
                    if (this.angle > -180.0f) {
                        this.angle -= 10.0f;
                    } else {
                        this.mudskipper.displayDirection = true;
                    }
                }
                if (distXZ < (double)0.8f && this.mudskipper.m_20096_() && this.partner.m_20096_()) {
                    this.mudskipper.m_21391_((Entity)this.partner, 360.0f, 360.0f);
                    this.setDisplayDirection(!this.mudskipper.displayDirection);
                    this.mudskipper.openMouth(10 + this.mudskipper.m_217043_().m_188503_(20));
                    this.partner.m_20256_(this.partner.m_20184_().m_82520_((double)(0.2f * this.mudskipper.m_217043_().m_188501_()), (double)0.35f, (double)(0.2f * this.mudskipper.m_217043_().m_188501_())));
                }
                Vec3 circle = this.getCirclingPosOf(this.center, 1.5f + this.mudskipper.m_217043_().m_188501_());
                Vec3 dirVec = circle.m_82546_(this.mudskipper.m_20182_());
                float headAngle = -((float)(Mth.m_14136_((double)dirVec.f_82479_, (double)dirVec.f_82481_) * 57.2957763671875));
                this.mudskipper.m_21573_().m_26519_(circle.f_82479_, circle.f_82480_, circle.f_82481_, (double)speed);
                this.mudskipper.m_146922_(headAngle);
                this.mudskipper.f_20885_ = headAngle;
                this.mudskipper.f_20883_ = headAngle;
                this.mudskipper.nextDisplayAngleFromServer = this.angle;
                ++this.mudskipper.displayTimer;
                ++this.partner.displayTimer;
                if (this.mudskipper.displayTimer > 400 || y > 2.0f) {
                    this.mudskipper.m_21573_().m_26573_();
                    this.partner.m_21573_().m_26573_();
                    this.mudskipper.f_19812_ = true;
                    this.mudskipper.displayTimer = 0;
                    this.partner.displayTimer = 0;
                    this.mudskipper.displayCooldown = 200 + this.mudskipper.m_217043_().m_188503_(200);
                    this.partner.displayTimer = 0;
                    this.partner.displayCooldown = 200 + this.partner.m_217043_().m_188503_(200);
                    this.m_8041_();
                }
            }
        }
    }

    public Vec3 getCirclingPosOf(Vec3 center, double circleDistance) {
        float cir = (float)Math.PI / 180 * this.angle;
        double extraX = circleDistance * (double)Mth.m_14031_((float)cir);
        double extraZ = circleDistance * (double)Mth.m_14089_((float)cir);
        return center.m_82520_(extraX, 0.0, extraZ);
    }

    public boolean m_8045_() {
        return !this.mudskipper.m_6162_() && !this.mudskipper.shouldFollow() && !this.mudskipper.m_21827_() && !this.mudskipper.m_27593_() && !this.mudskipper.m_20160_() && this.mudskipper.m_5448_() == null && this.partner != null && this.partner.m_6084_() && this.mudskipper.displayCooldown == 0 && this.partner.displayCooldown == 0;
    }

    @Nullable
    private EntityMudskipper getNearbyMudskipper() {
        List skippers = this.world.m_45971_(EntityMudskipper.class, JOSTLE_PREDICATE, (LivingEntity)this.mudskipper, this.mudskipper.m_20191_().m_82400_(16.0));
        double lvt_2_1_ = Double.MAX_VALUE;
        EntityMudskipper lvt_4_1_ = null;
        for (EntityMudskipper lvt_6_1_ : skippers) {
            if (!this.mudskipper.canDisplayWith(lvt_6_1_) || !(this.mudskipper.m_20280_((Entity)lvt_6_1_) < lvt_2_1_)) continue;
            lvt_4_1_ = lvt_6_1_;
            lvt_2_1_ = this.mudskipper.m_20280_((Entity)lvt_6_1_);
        }
        return lvt_4_1_;
    }
}

