/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCapuchinMonkey;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

public class CapuchinAIRangedAttack
extends Goal {
    private final EntityCapuchinMonkey entity;
    private final double moveSpeedAmp;
    private final float maxAttackDistanceDef;
    private float maxAttackDistance;
    private int attackCooldown;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public CapuchinAIRangedAttack(EntityCapuchinMonkey mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = this.maxAttackDistanceDef = maxAttackDistanceIn * maxAttackDistanceIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setAttackCooldown(int attackCooldownIn) {
        this.attackCooldown = attackCooldownIn;
    }

    public boolean m_8036_() {
        return this.entity.getDartTarget() != null && this.shouldRange();
    }

    protected boolean shouldRange() {
        return this.entity.attackDecision;
    }

    public boolean m_8045_() {
        return this.entity.getDartTarget() != null && this.entity.getDartTarget().m_6084_() && (this.m_8036_() || !this.entity.m_21573_().m_26571_()) && this.shouldRange();
    }

    public void m_8056_() {
        super.m_8056_();
        this.maxAttackDistance = this.maxAttackDistanceDef * (float)(this.entity.hasDart() ? 2 : 1);
        this.entity.m_21561_(true);
    }

    public void m_8041_() {
        super.m_8041_();
        this.entity.m_21561_(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.setAnimation(EntityCapuchinMonkey.NO_ANIMATION);
        this.entity.m_5810_();
    }

    public void m_8037_() {
        Entity livingentity = this.entity.getDartTarget();
        if (livingentity != null && livingentity.m_6084_()) {
            boolean flag1;
            double d0 = this.entity.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
            boolean flag = this.entity.m_21574_().m_148306_(livingentity);
            boolean bl = flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }
            this.seeTime = flag ? ++this.seeTime : --this.seeTime;
            if (!(d0 > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.entity.m_21573_().m_26573_();
                ++this.strafingTime;
            } else {
                this.entity.m_21573_().m_5624_(livingentity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if ((double)this.entity.m_217043_().m_188501_() < 0.3) {
                    boolean bl2 = this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double)this.entity.m_217043_().m_188501_() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > (double)(this.maxAttackDistance * 0.75f)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.maxAttackDistance * 0.25f)) {
                    this.strafingBackwards = true;
                }
                this.entity.m_21566_().m_24988_(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
                this.entity.m_21391_(livingentity, 30.0f, 30.0f);
            } else {
                this.entity.m_21563_().m_24960_(livingentity, 30.0f, 30.0f);
            }
            if (!flag && this.seeTime < -60) {
                this.entity.m_5810_();
            } else if (flag) {
                this.entity.setAnimation(EntityCapuchinMonkey.ANIMATION_THROW);
                this.attackTime = this.attackCooldown;
            }
        }
    }
}

