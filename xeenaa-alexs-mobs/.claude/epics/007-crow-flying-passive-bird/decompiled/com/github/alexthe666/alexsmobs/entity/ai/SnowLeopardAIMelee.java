/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.Vec3i
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.ai.util.LandRandomPos
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.level.pathfinder.WalkNodeEvaluator
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntitySnowLeopard;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class SnowLeopardAIMelee
extends Goal {
    private final EntitySnowLeopard leopard;
    private LivingEntity target;
    private boolean secondPartOfLeap = false;
    private Vec3 leapPos = null;
    private int jumpCooldown = 0;
    private boolean stalk = false;

    public SnowLeopardAIMelee(EntitySnowLeopard snowLeopard) {
        this.leopard = snowLeopard;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Nullable
    private static BlockPos getRandomDelta(RandomSource p_226343_0_, int p_226343_1_, int p_226343_2_, int p_226343_3_, @Nullable Vec3 p_226343_4_, double p_226343_5_) {
        if (p_226343_4_ != null && p_226343_5_ < Math.PI) {
            double lvt_7_2_ = Mth.m_14136_((double)p_226343_4_.f_82481_, (double)p_226343_4_.f_82479_) - 1.5707963705062866;
            double lvt_9_2_ = lvt_7_2_ + (double)(2.0f * p_226343_0_.m_188501_() - 1.0f) * p_226343_5_;
            double lvt_11_1_ = Math.sqrt(p_226343_0_.m_188500_()) * (double)Mth.f_13994_ * (double)p_226343_1_;
            double lvt_13_1_ = -lvt_11_1_ * Math.sin(lvt_9_2_);
            double lvt_15_1_ = lvt_11_1_ * Math.cos(lvt_9_2_);
            if (Math.abs(lvt_13_1_) <= (double)p_226343_1_ && Math.abs(lvt_15_1_) <= (double)p_226343_1_) {
                int lvt_17_1_ = p_226343_0_.m_188503_(2 * p_226343_2_ + 1) - p_226343_2_ + p_226343_3_;
                return AMBlockPos.fromCoords(lvt_13_1_, lvt_17_1_, lvt_15_1_);
            }
            return null;
        }
        int lvt_7_1_ = p_226343_0_.m_188503_(2 * p_226343_1_ + 1) - p_226343_1_;
        int lvt_8_1_ = p_226343_0_.m_188503_(2 * p_226343_2_ + 1) - p_226343_2_ + p_226343_3_;
        int lvt_9_1_ = p_226343_0_.m_188503_(2 * p_226343_1_ + 1) - p_226343_1_;
        return new BlockPos(lvt_7_1_, lvt_8_1_, lvt_9_1_);
    }

    public static BlockPos moveUpToAboveSolid(BlockPos p_226342_0_, int p_226342_1_, int p_226342_2_, Predicate<BlockPos> p_226342_3_) {
        BlockPos lvt_6_1_;
        if (p_226342_1_ < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + p_226342_1_ + ", expected >= 0");
        }
        if (!p_226342_3_.test(p_226342_0_)) {
            return p_226342_0_;
        }
        BlockPos lvt_4_1_ = p_226342_0_.m_7494_();
        while (lvt_4_1_.m_123342_() < p_226342_2_ && p_226342_3_.test(lvt_4_1_)) {
            lvt_4_1_ = lvt_4_1_.m_7494_();
        }
        BlockPos lvt_5_1_ = lvt_4_1_;
        while (lvt_5_1_.m_123342_() < p_226342_2_ && lvt_5_1_.m_123342_() - lvt_4_1_.m_123342_() < p_226342_1_ && !p_226342_3_.test(lvt_6_1_ = lvt_5_1_.m_7494_())) {
            lvt_5_1_ = lvt_6_1_;
        }
        return lvt_5_1_;
    }

    public boolean m_8036_() {
        return this.leopard.m_5448_() != null && !this.leopard.m_5803_() && !this.leopard.isSitting() && (this.leopard.m_5448_().m_6084_() || this.leopard.m_5448_() instanceof Player) && !this.leopard.m_6162_();
    }

    public void m_8056_() {
        this.target = this.leopard.m_5448_();
        this.stalk = this.target instanceof Player && this.leopard.m_21188_() != null && this.leopard.m_21188_() == this.target ? this.leopard.m_20270_((Entity)this.target) > 10.0f : this.leopard.m_20270_((Entity)this.target) > 4.0f;
        this.secondPartOfLeap = false;
    }

    public void m_8041_() {
        this.secondPartOfLeap = false;
        this.stalk = false;
        this.leapPos = null;
        this.jumpCooldown = 0;
        this.leopard.setTackling(false);
        this.leopard.setSlSneaking(false);
    }

    public void m_8037_() {
        if (this.jumpCooldown > 0) {
            --this.jumpCooldown;
        }
        if (this.stalk) {
            if (this.secondPartOfLeap) {
                this.leopard.setTackling(!this.leopard.m_20096_());
                this.leopard.m_21391_((Entity)this.target, 180.0f, 10.0f);
                this.leopard.f_20883_ = this.leopard.m_146908_();
                if (this.leopard.m_20270_((Entity)this.target) < 3.0f && this.leopard.m_142582_((Entity)this.target)) {
                    this.target.m_6469_(this.leopard.m_269291_().m_269333_((LivingEntity)this.leopard), (float)(this.leopard.m_21051_(Attributes.f_22281_).m_22135_() * 2.5));
                    this.stalk = false;
                    this.secondPartOfLeap = false;
                } else if (this.leopard.m_20096_() && this.jumpCooldown == 0) {
                    this.leopard.setSlSneaking(false);
                    this.jumpCooldown = 10 + this.leopard.m_217043_().m_188503_(10);
                    Vec3 vector3d = this.leopard.m_20184_();
                    Vec3 vector3d1 = new Vec3(this.target.m_20185_() - this.leopard.m_20185_(), 0.0, this.target.m_20189_() - this.leopard.m_20189_());
                    if (vector3d1.m_82556_() > 1.0E-7) {
                        vector3d1 = vector3d1.m_82541_().m_82490_(0.9).m_82549_(vector3d.m_82490_(0.8));
                    }
                    this.leopard.m_20334_(vector3d1.f_82479_, vector3d1.f_82480_ + (double)0.6f, vector3d1.f_82481_);
                }
            } else if (this.leapPos == null || this.target.m_20238_(this.leapPos) > 250.0) {
                Vec3 vector3d1 = this.calculateFarPoint(50.0);
                this.leapPos = vector3d1 != null ? vector3d1 : LandRandomPos.m_148492_((PathfinderMob)this.leopard, (int)10, (int)10, (Vec3)this.target.m_20182_());
            } else {
                this.leopard.setSlSneaking(true);
                this.leopard.m_21573_().m_26519_(this.leapPos.f_82479_, this.leapPos.f_82480_, this.leapPos.f_82481_, 1.0);
                if (this.leopard.m_20275_(this.leapPos.f_82479_, this.leapPos.f_82480_, this.leapPos.f_82481_) < 9.0 && this.leopard.m_142582_((Entity)this.target)) {
                    this.secondPartOfLeap = true;
                    this.leopard.m_21573_().m_26573_();
                }
            }
        } else {
            this.leopard.setSlSneaking(false);
            this.leopard.m_21573_().m_5624_((Entity)this.target, 1.0);
            if (this.leopard.m_20270_((Entity)this.target) < 3.0f) {
                if (this.leopard.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.leopard.setAnimation(this.leopard.m_217043_().m_188499_() ? EntitySnowLeopard.ANIMATION_ATTACK_R : EntitySnowLeopard.ANIMATION_ATTACK_L);
                } else if (this.leopard.getAnimationTick() == 5) {
                    this.leopard.m_7327_((Entity)this.target);
                }
            }
        }
    }

    private Vec3 calculateFarPoint(double dist) {
        Vec3 highest = null;
        for (int i = 0; i < 10; ++i) {
            Vec3 vector3d1 = this.calculateVantagePoint(this.target, 8, 3, 1, this.target.m_20182_().m_82492_(this.leopard.m_20185_(), this.leopard.m_20186_(), this.leopard.m_20189_()), false, 1.5707963705062866, arg_0 -> ((EntitySnowLeopard)this.leopard).m_21692_(arg_0), false, 0, 0, true);
            if (vector3d1 == null || !(this.target.m_20238_(vector3d1) > dist) || highest != null && !(highest.m_7098_() < vector3d1.f_82480_)) continue;
            highest = vector3d1;
        }
        return highest;
    }

    @Nullable
    private Vec3 calculateVantagePoint(LivingEntity creature, int xz, int y, int p_226339_3_, @Nullable Vec3 p_226339_4_, boolean p_226339_5_, double p_226339_6_, ToDoubleFunction<BlockPos> p_226339_8_, boolean p_226339_9_, int p_226339_10_, int p_226339_11_, boolean p_226339_12_) {
        PathNavigation lvt_13_1_ = this.leopard.m_21573_();
        RandomSource lvt_14_1_ = creature.m_217043_();
        boolean lvt_15_2_ = this.leopard.m_21536_() ? this.leopard.m_21534_().m_203195_((Position)creature.m_20182_(), (double)(this.leopard.m_21535_() + (float)xz) + 1.0) : false;
        boolean lvt_16_1_ = false;
        double lvt_17_1_ = Double.NEGATIVE_INFINITY;
        BlockPos lvt_19_1_ = creature.m_20183_();
        for (int lvt_20_1_ = 0; lvt_20_1_ < 10; ++lvt_20_1_) {
            double lvt_27_1_;
            BlockPathTypes lvt_26_1_;
            BlockPos lvt_25_2_;
            BlockPos lvt_21_1_ = SnowLeopardAIMelee.getRandomDelta(lvt_14_1_, xz, y, p_226339_3_, p_226339_4_, p_226339_6_);
            if (lvt_21_1_ == null) continue;
            int lvt_22_1_ = lvt_21_1_.m_123341_();
            int lvt_23_1_ = lvt_21_1_.m_123342_();
            int lvt_24_1_ = lvt_21_1_.m_123343_();
            if (this.leopard.m_21536_() && xz > 1) {
                lvt_25_2_ = this.leopard.m_21534_();
                lvt_22_1_ = creature.m_20185_() > (double)lvt_25_2_.m_123341_() ? (lvt_22_1_ -= lvt_14_1_.m_188503_(xz / 2)) : (lvt_22_1_ += lvt_14_1_.m_188503_(xz / 2));
                lvt_24_1_ = creature.m_20189_() > (double)lvt_25_2_.m_123343_() ? (lvt_24_1_ -= lvt_14_1_.m_188503_(xz / 2)) : (lvt_24_1_ += lvt_14_1_.m_188503_(xz / 2));
            }
            if ((lvt_25_2_ = AMBlockPos.fromCoords((double)lvt_22_1_ + creature.m_20185_(), (double)lvt_23_1_ + creature.m_20186_(), (double)lvt_24_1_ + creature.m_20189_())).m_123342_() < 0 || lvt_25_2_.m_123342_() > creature.m_9236_().m_151558_() || lvt_15_2_ && !this.leopard.m_21444_(lvt_25_2_) || p_226339_12_ && !lvt_13_1_.m_6342_(lvt_25_2_)) continue;
            if (p_226339_9_) {
                lvt_25_2_ = SnowLeopardAIMelee.moveUpToAboveSolid(lvt_25_2_, lvt_14_1_.m_188503_(p_226339_10_ + 1) + p_226339_11_, creature.m_9236_().m_151558_(), p_226341_1_ -> creature.m_9236_().m_8055_(p_226341_1_).m_280296_());
            }
            if (!p_226339_5_ && creature.m_9236_().m_6425_(lvt_25_2_).m_205070_(FluidTags.f_13131_) || this.leopard.m_21439_(lvt_26_1_ = WalkNodeEvaluator.m_77604_((BlockGetter)creature.m_9236_(), (BlockPos.MutableBlockPos)lvt_25_2_.m_122032_())) != 0.0f || !((lvt_27_1_ = p_226339_8_.applyAsDouble(lvt_25_2_)) > lvt_17_1_)) continue;
            lvt_17_1_ = lvt_27_1_;
            lvt_19_1_ = lvt_25_2_;
            lvt_16_1_ = true;
        }
        if (lvt_16_1_) {
            return Vec3.m_82539_((Vec3i)lvt_19_1_);
        }
        return null;
    }
}

