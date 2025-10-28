/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGeladaMonkey;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

public class GeladaAIGroom
extends Goal {
    private final EntityGeladaMonkey monkey;
    private int groomTime = 0;
    private int groomCooldown = 220;
    private EntityGeladaMonkey beingGroomed;

    public GeladaAIGroom(EntityGeladaMonkey monkey) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.monkey = monkey;
    }

    public boolean m_8036_() {
        if (this.groomCooldown > 0) {
            --this.groomCooldown;
            return false;
        }
        this.groomCooldown = 200 + this.monkey.m_217043_().m_188503_(1000);
        EntityGeladaMonkey nearestMonkey = null;
        for (EntityGeladaMonkey entity : this.monkey.m_9236_().m_45976_(EntityGeladaMonkey.class, this.monkey.m_20191_().m_82400_(15.0))) {
            if (entity.m_19879_() == this.monkey.m_19879_() || !this.monkey.canBeGroomed() || nearestMonkey != null && !(this.monkey.m_20270_((Entity)nearestMonkey) > this.monkey.m_20270_((Entity)entity))) continue;
            nearestMonkey = entity;
        }
        this.beingGroomed = nearestMonkey;
        return this.beingGroomed != null;
    }

    public boolean m_8045_() {
        return this.beingGroomed != null && this.beingGroomed.m_6084_() && !this.beingGroomed.shouldStopBeingGroomed() && this.groomTime < 200 && (this.beingGroomed.groomerID == -1 || this.beingGroomed.groomerID == this.monkey.m_19879_());
    }

    public void m_8041_() {
        this.groomTime = 0;
        this.monkey.isGrooming = false;
        if (this.beingGroomed != null) {
            this.beingGroomed.groomerID = -1;
        }
        this.beingGroomed = null;
    }

    public void m_8037_() {
        double dist = this.monkey.m_20270_((Entity)this.beingGroomed);
        if (dist < (double)(this.monkey.m_20205_() + 0.5f)) {
            this.monkey.isGrooming = true;
            this.beingGroomed.groomerID = this.monkey.m_19879_();
            this.monkey.setSitting(true);
            ++this.groomTime;
            if (this.groomTime % 50 == 0) {
                this.monkey.m_5634_(1.0f);
            }
            if (this.monkey.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.monkey.setAnimation(EntityGeladaMonkey.ANIMATION_GROOM);
            }
            this.monkey.m_21573_().m_26573_();
            this.monkey.m_21391_((Entity)this.beingGroomed, 360.0f, 360.0f);
        } else {
            this.monkey.isGrooming = false;
            this.beingGroomed.groomerID = -1;
            this.monkey.setSitting(false);
            this.monkey.m_21573_().m_5624_((Entity)this.beingGroomed, 1.0);
        }
    }
}

