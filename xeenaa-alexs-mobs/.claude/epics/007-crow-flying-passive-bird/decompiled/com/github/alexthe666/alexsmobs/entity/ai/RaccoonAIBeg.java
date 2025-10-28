/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.player.Player
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class RaccoonAIBeg
extends Goal {
    private static final TargetingConditions ENTITY_PREDICATE = TargetingConditions.m_148353_().m_26883_(32.0);
    protected final EntityRaccoon raccoon;
    private final double speed;
    protected Player closestPlayer;
    private int delayTemptCounter;
    private boolean isRunning;

    public RaccoonAIBeg(EntityRaccoon raccoon, double speed) {
        this.raccoon = raccoon;
        this.speed = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean m_8036_() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        }
        if (!this.raccoon.m_21205_().m_41619_()) {
            return false;
        }
        this.closestPlayer = this.raccoon.m_9236_().m_45946_(ENTITY_PREDICATE, (LivingEntity)this.raccoon);
        if (this.closestPlayer == null) {
            return false;
        }
        boolean food = EntityRaccoon.isRaccoonFood(this.closestPlayer.m_21205_()) || EntityRaccoon.isRaccoonFood(this.closestPlayer.m_21206_());
        return food;
    }

    public boolean m_8045_() {
        return this.raccoon.m_21205_().m_41619_() && this.m_8036_();
    }

    public void m_8056_() {
        this.isRunning = true;
    }

    public void m_8041_() {
        this.closestPlayer = null;
        this.raccoon.m_21573_().m_26573_();
        this.delayTemptCounter = 100;
        this.raccoon.setBegging(false);
        this.isRunning = false;
    }

    public void m_8037_() {
        this.raccoon.m_21563_().m_24960_((Entity)this.closestPlayer, (float)(this.raccoon.m_8085_() + 20), (float)this.raccoon.m_8132_());
        if (this.raccoon.m_20280_((Entity)this.closestPlayer) < 12.0) {
            this.raccoon.m_21573_().m_26573_();
            this.raccoon.setBegging(true);
        } else {
            this.raccoon.m_21573_().m_5624_((Entity)this.closestPlayer, this.speed);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}

