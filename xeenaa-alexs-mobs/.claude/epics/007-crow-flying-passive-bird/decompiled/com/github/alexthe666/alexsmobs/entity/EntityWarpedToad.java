/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
 *  net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
 *  net.minecraft.world.entity.ai.navigation.GroundPathNavigation
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.LeavesBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.level.pathfinder.WalkNodeEvaluator
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito;
import com.github.alexthe666.alexsmobs.entity.IFollower;
import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeapRandomly;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.WarpedToadAIRandomSwimming;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class EntityWarpedToad
extends TamableAnimal
implements ITargetsDroppedItems,
IFollower,
ISemiAquatic {
    private static final EntityDataAccessor<Float> TONGUE_LENGTH = SynchedEntityData.m_135353_(EntityWarpedToad.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Boolean> TONGUE_OUT = SynchedEntityData.m_135353_(EntityWarpedToad.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.m_135353_(EntityWarpedToad.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.m_135353_(EntityWarpedToad.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Boolean> JUMP_ACTIVE = SynchedEntityData.m_135353_(EntityWarpedToad.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float blinkProgress;
    public float prevBlinkProgress;
    public float attackProgress;
    public float prevAttackProgress;
    public float sitProgress;
    public float prevSitProgress;
    public float swimProgress;
    public float prevSwimProgress;
    public float jumpProgress;
    public float prevJumpProgress;
    public float reboundProgress;
    public float prevReboundProgress;
    private boolean isLandNavigator;
    private int currentMoveTypeDuration;
    private int swimTimer = -100;

    protected EntityWarpedToad(EntityType entityType, Level world) {
        super(entityType, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0f);
        this.m_21441_(BlockPathTypes.LAVA, 0.0f);
        this.switchNavigator(false);
    }

    public boolean isBased() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("pepe");
    }

    public static boolean canWarpedToadSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.m_7495_();
        boolean spawnBlock = worldIn.m_6425_(blockpos).m_205070_(FluidTags.f_13132_) || worldIn.m_8055_(blockpos).m_60815_();
        return reason == MobSpawnType.SPAWNER || spawnBlock;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 30.0).m_22268_(Attributes.f_22277_, 32.0).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22278_, 0.25).m_22268_(Attributes.f_22279_, (double)0.2f);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.WARPED_TOAD_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.WARPED_TOAD_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.WARPED_TOAD_HURT.get();
    }

    public boolean m_6040_() {
        return true;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.warpedToadSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public int m_5792_() {
        return 5;
    }

    public boolean m_7296_(int sizeIn) {
        return false;
    }

    public boolean m_6914_(LevelReader worldIn) {
        return worldIn.m_45784_((Entity)this);
    }

    public boolean m_6469_(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        }
        Entity entity = source.m_7639_();
        this.m_21839_(false);
        if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
            amount = (amount + 1.0f) / 3.0f;
        }
        return super.m_6469_(source, amount);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("ToadSitting", this.m_21827_());
        compound.m_128405_("Command", this.getCommand());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.m_21839_(compound.m_128471_("ToadSitting"));
        this.setCommand(compound.m_128451_("Command"));
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new SitWhenOrderedToGoal((TamableAnimal)this));
        this.f_21345_.m_25352_(1, (Goal)new TongueAttack(this));
        this.f_21345_.m_25352_(2, (Goal)new FollowOwner(this, 1.3, 4.0f, 2.0f, false));
        this.f_21345_.m_25352_(3, (Goal)new AnimalAIFindWater((PathfinderMob)this));
        this.f_21345_.m_25352_(3, (Goal)new AnimalAILeaveWater((PathfinderMob)this));
        this.f_21345_.m_25352_(3, (Goal)new BreedGoal((Animal)this, 0.8));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_204132_(AMTagRegistry.WARPED_TOAD_FOODSTUFFS), false));
        this.f_21345_.m_25352_(5, (Goal)new WarpedToadAIRandomSwimming(this, 1.0, 7));
        this.f_21345_.m_25352_(6, (Goal)new AnimalAILeapRandomly((PathfinderMob)this, 50, 7){

            @Override
            public boolean m_8036_() {
                return super.m_8036_() && !EntityWarpedToad.this.m_21827_();
            }
        });
        this.f_21345_.m_25352_(7, (Goal)new AnimalAIWanderRanged((PathfinderMob)this, 60, 1.0, 5, 4));
        this.f_21345_.m_25352_(10, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0f));
        this.f_21345_.m_25352_(11, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new CreatureAITargetItems((PathfinderMob)this, false));
        this.f_21346_.m_25352_(2, (Goal)new OwnerHurtByTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(3, (Goal)new OwnerHurtTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(4, new EntityAINearestTarget3D<LivingEntity>((Mob)this, LivingEntity.class, 50, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.WARPED_TOAD_TARGETS)));
        this.f_21346_.m_25352_(5, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
    }

    public void m_7023_(Vec3 travelVector) {
        if (this.m_21827_()) {
            if (this.m_21573_().m_26570_() != null) {
                this.m_21573_().m_26573_();
            }
            travelVector = Vec3.f_82478_;
            super.m_7023_(travelVector);
        } else if (this.m_21515_() && (this.m_20069_() || this.m_20077_())) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().m_82490_(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().m_82520_(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    protected float m_6118_() {
        return 0.5f;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void m_8024_() {
        super.m_8024_();
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.WARPED_TOAD_BREEDABLES) && this.m_21824_();
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && itemstack.m_204117_(AMTagRegistry.WARPED_TOAD_TAMEABLES)) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.f_157806_);
            this.m_5496_(SoundEvents.f_12465_, this.m_6121_(), this.m_6100_());
            if (this.m_217043_().m_188503_(3) == 0) {
                this.m_21828_(player);
                this.m_9236_().m_7605_((Entity)this, (byte)7);
            } else {
                this.m_9236_().m_7605_((Entity)this, (byte)6);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.m_21824_() && itemstack.m_204117_(AMTagRegistry.WARPED_TOAD_FOODSTUFFS)) {
            if (this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_12465_, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0f);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        InteractionResult interactionresult = itemstack.m_41647_(player, (LivingEntity)this, hand);
        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_((LivingEntity)player) && !this.m_6898_(itemstack)) {
            boolean sit;
            this.setCommand(this.getCommand() + 1);
            if (this.getCommand() == 3) {
                this.setCommand(0);
            }
            player.m_5661_((Component)Component.m_237110_((String)("entity.alexsmobs.all.command_" + this.getCommand()), (Object[])new Object[]{this.m_7755_()}), true);
            boolean bl = sit = this.getCommand() == 2;
            if (sit) {
                this.m_21839_(true);
                return InteractionResult.SUCCESS;
            }
            this.m_21839_(false);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean m_7307_(Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal)entityIn).m_21830_(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.m_7307_(entityIn);
    }

    public boolean m_5843_() {
        return false;
    }

    private void calculateRotationYaw(double x, double z) {
        this.m_146922_((float)(Mth.m_14136_((double)(z - this.m_20189_()), (double)(x - this.m_20185_())) * 57.2957763671875) - 90.0f);
    }

    public void m_8107_() {
        super.m_8107_();
        if (this.m_6162_() && this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        if (!this.m_9236_().f_46443_) {
            if (this.m_20069_() || this.m_20077_()) {
                if (this.swimTimer < 0) {
                    this.swimTimer = 0;
                }
                ++this.swimTimer;
            } else {
                if (this.swimTimer > 0) {
                    this.swimTimer = 0;
                }
                --this.swimTimer;
            }
        }
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = this.m_6037_(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AquaticMoveController((PathfinderMob)this, 1.2f);
            this.f_21344_ = new BoneSerpentPathNavigator((Mob)this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(TONGUE_LENGTH, (Object)Float.valueOf(1.0f));
        this.f_19804_.m_135372_(TONGUE_OUT, (Object)false);
        this.f_19804_.m_135372_(COMMAND, (Object)0);
        this.f_19804_.m_135372_(SITTING, (Object)false);
        this.f_19804_.m_135372_(JUMP_ACTIVE, (Object)false);
    }

    public int getCommand() {
        return (Integer)this.f_19804_.m_135370_(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.m_135381_(COMMAND, (Object)command);
    }

    public boolean m_21827_() {
        return (Boolean)this.f_19804_.m_135370_(SITTING);
    }

    public void m_21839_(boolean sit) {
        this.f_19804_.m_135381_(SITTING, (Object)sit);
    }

    public void m_8119_() {
        LivingEntity entityIn;
        boolean isTongueOut;
        boolean isTechnicalBlinking;
        super.m_8119_();
        this.prevBlinkProgress = this.blinkProgress;
        this.prevAttackProgress = this.attackProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevSwimProgress = this.swimProgress;
        this.prevJumpProgress = this.jumpProgress;
        this.prevReboundProgress = this.reboundProgress;
        this.m_274367_(1.0f);
        boolean bl = isTechnicalBlinking = this.f_19797_ % 50 > 42;
        if (isTechnicalBlinking) {
            if (this.blinkProgress < 5.0f) {
                this.blinkProgress += 1.0f;
            }
        } else if (this.blinkProgress > 0.0f) {
            this.blinkProgress -= 1.0f;
        }
        if ((isTongueOut = this.isTongueOut()) && this.attackProgress < 5.0f) {
            this.attackProgress += 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            this.f_19804_.m_135381_(JUMP_ACTIVE, (Object)(!this.m_20096_() ? 1 : 0));
        }
        if (((Boolean)this.f_19804_.m_135370_(JUMP_ACTIVE)).booleanValue() && !this.m_20072_()) {
            this.f_20883_ = this.m_146908_();
            this.f_20885_ = this.m_146908_();
            if (this.jumpProgress < 5.0f) {
                this.jumpProgress += 0.5f;
                if (this.reboundProgress > 0.0f) {
                    this.reboundProgress -= 1.0f;
                }
            }
            if (this.jumpProgress >= 5.0f && this.reboundProgress < 5.0f) {
                this.reboundProgress += 0.5f;
            }
        } else {
            if (this.reboundProgress > 0.0f) {
                this.reboundProgress = Math.max(this.reboundProgress - 1.0f, 0.0f);
            }
            if (this.jumpProgress > 0.0f) {
                this.jumpProgress = Math.max(this.jumpProgress - 1.0f, 0.0f);
            }
        }
        if ((entityIn = this.m_5448_()) != null && this.attackProgress > 0.0f) {
            if (this.isTongueOut()) {
                double d0 = entityIn.m_20185_() - this.m_20185_();
                double d2 = entityIn.m_20189_() - this.m_20189_();
                double d1 = entityIn.m_20188_() - this.m_20188_();
                double d3 = Mth.m_14116_((float)((float)(d0 * d0 + d2 * d2)));
                float f = (float)(Mth.m_14136_((double)d2, (double)d0) * 57.2957763671875) - 90.0f;
                float f1 = (float)(-(Mth.m_14136_((double)d1, (double)d3) * 57.2957763671875));
                this.m_146926_(f1);
                this.m_146922_(f);
                this.f_20883_ = this.m_146908_();
                this.f_20885_ = this.m_146908_();
            } else {
                if (entityIn instanceof EntityCrimsonMosquito) {
                    ((EntityCrimsonMosquito)entityIn).setShrink(true);
                }
                this.m_146926_(0.0f);
                float radius = this.attackProgress * 0.2f * 1.2f * (this.getTongueLength() - this.getTongueLength() * 0.4f);
                float angle = (float)Math.PI / 180 * this.f_20883_;
                double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
                double extraZ = radius * Mth.m_14089_((float)angle);
                double yHelp = entityIn.m_20206_();
                Vec3 minus = new Vec3(this.m_20185_() + extraX - this.m_5448_().m_20185_(), (double)this.m_20192_() - yHelp - this.m_5448_().m_20186_(), this.m_20189_() + extraZ - this.m_5448_().m_20189_());
                this.m_5448_().m_20256_(minus);
                if (this.attackProgress == 0.5f) {
                    float damage = (float)this.m_21051_(Attributes.f_22281_).m_22135_();
                    if (entityIn instanceof EntityCrimsonMosquito) {
                        damage = Float.MAX_VALUE;
                    }
                    entityIn.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), damage);
                }
            }
        }
        if (!this.m_9236_().f_46443_ && this.attackProgress == 5.0f && isTongueOut) {
            this.setTongueOut(false);
            this.attackProgress = 4.0f;
        }
        if (this.attackProgress > 0.0f && !this.isTongueOut()) {
            this.attackProgress -= 0.5f;
        }
        if (this.m_21827_()) {
            if (this.sitProgress < 5.0f) {
                this.sitProgress += 1.0f;
            }
        } else if (this.sitProgress > 0.0f) {
            this.sitProgress -= 1.0f;
        }
        if (this.shouldSwim()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (this.swimProgress < 5.0f) {
                this.swimProgress += 1.0f;
            }
        } else {
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.swimProgress > 0.0f) {
                this.swimProgress -= 1.0f;
            }
        }
    }

    public boolean shouldSwim() {
        return this.m_20069_() || this.m_20077_();
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.WARPED_TOAD_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_5634_(5.0f);
    }

    public boolean isBlinking() {
        return this.blinkProgress > 1.0f || this.blinkProgress < -1.0f || this.attackProgress > 1.0f;
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return (AgeableMob)((EntityType)AMEntityRegistry.WARPED_TOAD.get()).m_20615_((Level)serverWorld);
    }

    public float getTongueLength() {
        return ((Float)this.f_19804_.m_135370_(TONGUE_LENGTH)).floatValue();
    }

    public void setTongueLength(float length) {
        this.f_19804_.m_135381_(TONGUE_LENGTH, (Object)Float.valueOf(length));
    }

    public boolean m_6063_() {
        return false;
    }

    @Override
    public boolean shouldEnterWater() {
        return this.swimTimer < -200 && !this.m_21827_() && this.getCommand() != 1;
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.swimTimer > 600 && !this.m_21827_() && this.getCommand() != 1;
    }

    @Override
    public boolean shouldStopMoving() {
        return this.m_21827_();
    }

    private boolean isTongueOut() {
        return (Boolean)this.f_19804_.m_135370_(TONGUE_OUT);
    }

    private void setTongueOut(boolean out) {
        this.f_19804_.m_135381_(TONGUE_OUT, (Object)out);
    }

    @Override
    public int getWaterSearchRange() {
        return 8;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    public static class TongueAttack
    extends Goal {
        private final EntityWarpedToad parentEntity;
        private int spitCooldown = 0;
        private BlockPos shootPos = null;

        public TongueAttack(EntityWarpedToad toad) {
            this.parentEntity = toad;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_20197_().isEmpty();
        }

        public boolean m_8045_() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_20197_().isEmpty();
        }

        public void m_8041_() {
            this.spitCooldown = 20;
            this.parentEntity.m_21573_().m_26573_();
        }

        public void m_8037_() {
            LivingEntity entityIn;
            if (this.spitCooldown > 0) {
                --this.spitCooldown;
            }
            if ((entityIn = this.parentEntity.m_5448_()) != null) {
                double dist = this.parentEntity.m_20270_((Entity)entityIn);
                if (dist < 8.0 && this.parentEntity.m_142582_((Entity)entityIn) && !this.parentEntity.isTongueOut() && this.parentEntity.attackProgress == 0.0f && this.spitCooldown == 0) {
                    this.parentEntity.setTongueLength((float)Math.max(1.0, dist + 2.0));
                    this.spitCooldown = 10;
                    this.parentEntity.setTongueOut(true);
                }
                this.parentEntity.m_21573_().m_5624_((Entity)entityIn, (double)1.4f);
            }
        }
    }

    public static class FollowOwner
    extends Goal {
        private final EntityWarpedToad tameable;
        private final LevelReader world;
        private final double followSpeed;
        private final float maxDist;
        private final float minDist;
        private final boolean teleportToLeaves;
        private LivingEntity owner;
        private int timeToRecalcPath;
        private float oldWaterCost;

        public FollowOwner(EntityWarpedToad p_i225711_1_, double p_i225711_2_, float p_i225711_4_, float p_i225711_5_, boolean p_i225711_6_) {
            this.tameable = p_i225711_1_;
            this.world = p_i225711_1_.m_9236_();
            this.followSpeed = p_i225711_2_;
            this.minDist = p_i225711_4_;
            this.maxDist = p_i225711_5_;
            this.teleportToLeaves = p_i225711_6_;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(p_i225711_1_.m_21573_() instanceof GroundPathNavigation) && !(p_i225711_1_.m_21573_() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean m_8036_() {
            LivingEntity lvt_1_1_ = this.tameable.m_269323_();
            if (lvt_1_1_ == null) {
                return false;
            }
            if (lvt_1_1_.m_5833_()) {
                return false;
            }
            if (this.tameable.m_21827_() || this.tameable.getCommand() != 1) {
                return false;
            }
            if (this.tameable.m_20280_((Entity)lvt_1_1_) < (double)(this.minDist * this.minDist)) {
                return false;
            }
            this.owner = lvt_1_1_;
            return true;
        }

        public boolean m_8045_() {
            if (this.tameable.m_21573_().m_26571_()) {
                return false;
            }
            if (this.tameable.m_21827_() || this.tameable.getCommand() != 1) {
                return false;
            }
            return this.tameable.m_20280_((Entity)this.owner) > (double)(this.maxDist * this.maxDist);
        }

        public void m_8056_() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.tameable.m_21439_(BlockPathTypes.WATER);
            this.tameable.m_21441_(BlockPathTypes.WATER, 0.0f);
        }

        public void m_8041_() {
            this.owner = null;
            this.tameable.m_21573_().m_26573_();
            this.tameable.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
        }

        public void m_8037_() {
            this.tameable.m_21563_().m_24960_((Entity)this.owner, 10.0f, (float)this.tameable.m_8132_());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.tameable.m_21523_() && !this.tameable.m_20159_()) {
                    if (this.tameable.m_20280_((Entity)this.owner) >= 144.0) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.tameable.m_21573_().m_5624_((Entity)this.owner, this.followSpeed);
                    }
                }
            }
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
            this.tameable.m_7678_((double)p_226328_1_ + 0.5, p_226328_2_, (double)p_226328_3_ + 0.5, this.tameable.m_146908_(), this.tameable.m_146909_());
            this.tameable.m_21573_().m_26573_();
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
            BlockPos lvt_4_1_ = p_226329_1_.m_121996_((Vec3i)this.tameable.m_20183_());
            return this.world.m_45756_((Entity)this.tameable, this.tameable.m_20191_().m_82338_(lvt_4_1_));
        }

        private int getRandomNumber(int p_226327_1_, int p_226327_2_) {
            return this.tameable.m_217043_().m_188503_(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
        }
    }
}

