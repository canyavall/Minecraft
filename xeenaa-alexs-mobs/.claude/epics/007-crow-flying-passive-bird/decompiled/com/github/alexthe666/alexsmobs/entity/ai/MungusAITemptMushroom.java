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
 *  net.minecraft.world.item.ItemStack
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMungus;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MungusAITemptMushroom
extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.m_148353_().m_26883_(10.0).m_148355_();
    private final TargetingConditions targetingConditions;
    protected final EntityMungus mob;
    private final double speedModifier;
    private double px;
    private double py;
    private double pz;
    private int calmDown;
    private double pRotX;
    private double pRotY;
    protected Player player;

    public MungusAITemptMushroom(EntityMungus p_25939_, double p_25940_) {
        this.mob = p_25939_;
        this.speedModifier = p_25940_;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.m_148354_();
    }

    public boolean m_8036_() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        }
        this.player = this.mob.m_9236_().m_45946_(this.targetingConditions, (LivingEntity)this.mob);
        if (this.player != null) {
            return this.shouldFollow(this.player.m_21205_()) || this.shouldFollow(this.player.m_21206_());
        }
        return false;
    }

    public boolean m_8045_() {
        return this.m_8036_();
    }

    public void m_8056_() {
        this.px = this.player.m_20185_();
        this.py = this.player.m_20186_();
        this.pz = this.player.m_20189_();
    }

    public void m_8041_() {
        this.player = null;
        this.mob.m_21573_().m_26573_();
    }

    public void m_8037_() {
        this.mob.m_21563_().m_24960_((Entity)this.player, (float)(this.mob.m_8085_() + 20), (float)this.mob.m_8132_());
        if (this.mob.m_20280_((Entity)this.player) < 6.25) {
            this.mob.m_21573_().m_26573_();
        } else {
            this.mob.m_21573_().m_5624_((Entity)this.player, this.speedModifier);
        }
    }

    protected boolean shouldFollow(ItemStack stack) {
        return this.mob.shouldFollowMushroom(stack) || stack.m_41720_() == AMItemRegistry.MUNGAL_SPORES.get();
    }
}

