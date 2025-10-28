/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.LeavesBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.level.pathfinder.WalkNodeEvaluator
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.message.MessageCrowMountPlayer;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class CrowAIFollowOwner
extends Goal {
    private final EntityCrow crow;
    private final LevelReader world;
    private final double followSpeed;
    private final PathNavigation navigator;
    private final float maxDist;
    private final float minDist;
    private final boolean teleportToLeaves;
    float circlingTime = 0.0f;
    float circleDistance = 1.0f;
    float yLevel = 2.0f;
    boolean clockwise = false;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;
    private int maxCircleTime;

    public CrowAIFollowOwner(EntityCrow p_i225711_1_, double p_i225711_2_, float p_i225711_4_, float p_i225711_5_, boolean p_i225711_6_) {
        this.crow = p_i225711_1_;
        this.world = p_i225711_1_.m_9236_();
        this.followSpeed = p_i225711_2_;
        this.navigator = p_i225711_1_.m_21573_();
        this.minDist = p_i225711_4_;
        this.maxDist = p_i225711_5_;
        this.teleportToLeaves = p_i225711_6_;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean m_8036_() {
        LivingEntity lvt_1_1_ = this.crow.m_269323_();
        if (lvt_1_1_ == null) {
            return false;
        }
        if (lvt_1_1_.m_5833_()) {
            return false;
        }
        if (this.crow.isSitting() || this.crow.m_20159_()) {
            return false;
        }
        if (this.crow.getCommand() != 1) {
            return false;
        }
        if (this.crow.m_20280_((Entity)lvt_1_1_) < (double)(this.minDist * this.minDist)) {
            return false;
        }
        this.owner = lvt_1_1_;
        return this.crow.m_5448_() == null || !this.crow.m_5448_().m_6084_();
    }

    public boolean m_8045_() {
        if (this.crow.isSitting()) {
            return false;
        }
        return this.crow.getCommand() == 1 && !this.crow.m_20159_() && (this.crow.m_5448_() == null || !this.crow.m_5448_().m_6084_());
    }

    public void m_8056_() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.crow.m_21439_(BlockPathTypes.WATER);
        this.crow.m_21441_(BlockPathTypes.WATER, 0.0f);
        this.clockwise = this.crow.m_217043_().m_188499_();
        this.yLevel = this.crow.m_217043_().m_188503_(1);
        this.circlingTime = 0.0f;
        this.maxCircleTime = 20 + this.crow.m_217043_().m_188503_(100);
        this.circleDistance = 1.0f + this.crow.m_217043_().m_188501_() * 2.0f;
    }

    public void m_8041_() {
        this.owner = null;
        this.navigator.m_26573_();
        this.circlingTime = 0.0f;
        this.crow.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    public void m_8037_() {
        this.crow.m_21563_().m_24960_((Entity)this.owner, 10.0f, (float)this.crow.m_8132_());
        if (!this.crow.m_21523_() && !this.crow.m_20159_()) {
            double dist = this.crow.m_20280_((Entity)this.owner);
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (dist >= 144.0 && !this.crow.aiItemFlag) {
                    this.crow.setFlying(true);
                    this.crow.m_21566_().m_6849_(this.owner.m_20185_(), this.owner.m_20186_() + (double)this.owner.m_20192_() + (double)0.2f, this.owner.m_20189_(), 1.0);
                    this.tryToTeleportNearEntity();
                    this.circlingTime = 0.0f;
                }
            }
            if (!this.crow.aiItemFlag) {
                if (this.crow.isFlying()) {
                    this.circlingTime += 1.0f;
                }
                if (this.circlingTime > (float)this.maxCircleTime && this.crow.getRidingCrows(this.owner) < 2) {
                    this.crow.m_21566_().m_6849_(this.owner.m_20185_(), this.owner.m_20186_() + (double)this.owner.m_20192_() + (double)0.2f, this.owner.m_20189_(), (double)0.7f);
                    if (this.crow.m_20270_((Entity)this.owner) < 2.0f) {
                        this.crow.m_7998_((Entity)this.owner, true);
                        if (!this.crow.m_9236_().f_46443_) {
                            AlexsMobs.sendMSGToAll(new MessageCrowMountPlayer(this.crow.m_19879_(), this.owner.m_19879_()));
                        }
                    }
                } else {
                    Vec3 circlePos = this.getVultureCirclePos(this.owner.m_20182_());
                    if (circlePos == null) {
                        circlePos = this.owner.m_20182_();
                    }
                    this.crow.setFlying(true);
                    this.crow.m_21566_().m_6849_(circlePos.m_7096_(), circlePos.m_7098_() + (double)this.owner.m_20192_() + (double)0.2f, circlePos.m_7094_(), (double)0.7f);
                }
            }
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

    private void tryToTeleportNearEntity() {
        BlockPos lvt_1_1_ = this.owner.m_20183_();
        for (int lvt_2_1_ = 0; lvt_2_1_ < 10; ++lvt_2_1_) {
            int lvt_3_1_ = this.getRandomNumber(-3, 3);
            int lvt_4_1_ = this.getRandomNumber(-1, 1);
            int lvt_5_1_ = this.getRandomNumber(-3, 3);
            boolean lvt_6_1_ = this.tryToTeleportToLocation(lvt_1_1_.m_123341_() + lvt_3_1_, lvt_1_1_.m_123342_() + lvt_4_1_, lvt_1_1_.m_123343_() + lvt_5_1_);
            if (!lvt_6_1_) continue;
            return;
        }
    }

    private boolean tryToTeleportToLocation(int p_226328_1_, int p_226328_2_, int p_226328_3_) {
        if (Math.abs((double)p_226328_1_ - this.owner.m_20185_()) < 2.0 && Math.abs((double)p_226328_3_ - this.owner.m_20189_()) < 2.0) {
            return false;
        }
        if (!this.isTeleportFriendlyBlock(new BlockPos(p_226328_1_, p_226328_2_, p_226328_3_))) {
            return false;
        }
        this.crow.m_7678_((double)p_226328_1_ + 0.5, p_226328_2_, (double)p_226328_3_ + 0.5, this.crow.m_146908_(), this.crow.m_146909_());
        this.navigator.m_26573_();
        return true;
    }

    private boolean isTeleportFriendlyBlock(BlockPos p_226329_1_) {
        BlockPathTypes lvt_2_1_ = WalkNodeEvaluator.m_77604_((BlockGetter)this.world, (BlockPos.MutableBlockPos)p_226329_1_.m_122032_());
        if (lvt_2_1_ != BlockPathTypes.WALKABLE) {
            return false;
        }
        BlockState lvt_3_1_ = this.world.m_8055_(p_226329_1_.m_7495_());
        if (!this.teleportToLeaves && lvt_3_1_.m_60734_() instanceof LeavesBlock) {
            return false;
        }
        BlockPos lvt_4_1_ = p_226329_1_.m_121996_((Vec3i)this.crow.m_20183_());
        return this.world.m_45756_((Entity)this.crow, this.crow.m_20191_().m_82338_(lvt_4_1_));
    }

    private int getRandomNumber(int p_226327_1_, int p_226327_2_) {
        return this.crow.m_217043_().m_188503_(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
    }
}

