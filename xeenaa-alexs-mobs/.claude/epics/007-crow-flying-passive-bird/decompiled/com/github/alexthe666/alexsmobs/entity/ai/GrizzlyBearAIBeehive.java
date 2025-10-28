/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  net.minecraft.commands.arguments.EntityAnchorArgument$Anchor
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.MoveToBlockGoal
 *  net.minecraft.world.entity.animal.Bee
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.BeehiveBlock
 *  net.minecraft.world.level.block.entity.BeehiveBlockEntity
 *  net.minecraft.world.level.block.entity.BeehiveBlockEntity$BeeReleaseStatus
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.event.ForgeEventFactory
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class GrizzlyBearAIBeehive
extends MoveToBlockGoal {
    private final EntityGrizzlyBear bear;
    private int idleAtHiveTime = 0;
    private boolean isAboveDestinationBear;

    public GrizzlyBearAIBeehive(EntityGrizzlyBear bear) {
        super((PathfinderMob)bear, 1.0, 32, 8);
        this.bear = bear;
    }

    public boolean m_8036_() {
        return !this.bear.m_6162_() && super.m_8036_();
    }

    public void m_8041_() {
        this.idleAtHiveTime = 0;
    }

    public double m_8052_() {
        return 2.0;
    }

    public void m_8037_() {
        super.m_8037_();
        BlockPos blockpos = this.m_6669_();
        if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.m_8052_())) {
            this.isAboveDestinationBear = false;
            ++this.f_25601_;
            if (this.m_8064_()) {
                this.f_25598_.m_21573_().m_26519_((double)blockpos.m_123341_() + 0.5, (double)blockpos.m_123342_(), (double)blockpos.m_123343_() + 0.5, this.f_25599_);
            }
        } else {
            this.isAboveDestinationBear = true;
            --this.f_25601_;
        }
        if (this.m_25625_() && Math.abs(this.bear.m_20186_() - (double)this.f_25602_.m_123342_()) <= 3.0) {
            this.bear.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double)this.f_25602_.m_123341_() + 0.5, (double)this.f_25602_.m_123342_(), (double)this.f_25602_.m_123343_() + 0.5));
            if (this.bear.m_20186_() + 2.0 < (double)this.f_25602_.m_123342_()) {
                this.bear.setAnimation(EntityGrizzlyBear.ANIMATION_MAUL);
                this.bear.maxStandTime = 60;
                this.bear.setStanding(true);
            } else if (this.bear.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.bear.setAnimation(this.bear.m_217043_().m_188499_() ? EntityGrizzlyBear.ANIMATION_SWIPE_L : EntityGrizzlyBear.ANIMATION_SWIPE_R);
            }
            if (this.idleAtHiveTime >= 20) {
                this.eatHive();
            } else {
                ++this.idleAtHiveTime;
            }
        }
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_((Vec3i)AMBlockPos.fromCoords(positionVec.m_7096_(), blockpos.m_123342_(), positionVec.m_7094_())) < distance * distance;
    }

    protected boolean m_25625_() {
        return this.isAboveDestinationBear;
    }

    private void eatHive() {
        BlockState blockstate;
        if (ForgeEventFactory.getMobGriefingEvent((Level)this.bear.m_9236_(), (Entity)this.bear) && (blockstate = this.bear.m_9236_().m_8055_(this.f_25602_)).m_204336_(AMTagRegistry.GRIZZLY_BEEHIVE) && this.bear.m_9236_().m_7702_(this.f_25602_) instanceof BeehiveBlockEntity) {
            RandomSource rand = this.bear.m_217043_();
            BeehiveBlockEntity beehivetileentity = (BeehiveBlockEntity)this.bear.m_9236_().m_7702_(this.f_25602_);
            beehivetileentity.m_58748_(null, blockstate, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            this.bear.m_9236_().m_46717_(this.f_25602_, blockstate.m_60734_());
            ItemStack stack = new ItemStack((ItemLike)Items.f_42784_);
            int level = 0;
            if (blockstate.m_60734_() instanceof BeehiveBlock) {
                level = (Integer)blockstate.m_61143_((Property)BeehiveBlock.f_49564_);
            }
            for (int i = 0; i < level; ++i) {
                ItemEntity itementity = new ItemEntity(this.bear.m_9236_(), (double)((float)this.f_25602_.m_123341_() + rand.m_188501_()), (double)((float)this.f_25602_.m_123342_() + rand.m_188501_()), (double)((float)this.f_25602_.m_123343_() + rand.m_188501_()), stack);
                itementity.m_32060_();
                this.bear.m_9236_().m_7967_((Entity)itementity);
            }
            this.bear.m_9236_().m_46961_(this.f_25602_, false);
            if (blockstate.m_60734_() instanceof BeehiveBlock) {
                this.bear.m_9236_().m_46597_(this.f_25602_, (BlockState)blockstate.m_61124_((Property)BeehiveBlock.f_49564_, (Comparable)Integer.valueOf(0)));
            }
            double d0 = 15.0;
            for (Bee bee : this.bear.m_9236_().m_45976_(Bee.class, new AABB((double)this.f_25602_.m_123341_() - d0, (double)this.f_25602_.m_123342_() - d0, (double)this.f_25602_.m_123343_() - d0, (double)this.f_25602_.m_123341_() + d0, (double)this.f_25602_.m_123342_() + d0, (double)this.f_25602_.m_123343_() + d0))) {
                bee.m_7870_(100);
                bee.m_6710_((LivingEntity)this.bear);
                bee.m_27915_(400);
            }
            this.m_8041_();
        }
    }

    protected boolean m_6465_(LevelReader worldIn, BlockPos pos) {
        if (worldIn.m_8055_(pos).m_204336_(AMTagRegistry.GRIZZLY_BEEHIVE) && worldIn.m_7702_(pos) instanceof BeehiveBlockEntity && worldIn.m_8055_(pos).m_60734_() instanceof BeehiveBlock) {
            int i = (Integer)worldIn.m_8055_(pos).m_61143_((Property)BeehiveBlock.f_49564_);
            return i > 0;
        }
        return false;
    }
}

