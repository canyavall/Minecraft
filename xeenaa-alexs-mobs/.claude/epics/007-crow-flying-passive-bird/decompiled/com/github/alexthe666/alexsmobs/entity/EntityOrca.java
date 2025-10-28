/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.control.MoveControl$Operation
 *  net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl
 *  net.minecraft.world.entity.ai.goal.BreathAirGoal
 *  net.minecraft.world.entity.ai.goal.FollowBoatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomSwimmingGoal
 *  net.minecraft.world.entity.ai.goal.TryFindWaterGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.monster.Drowned
 *  net.minecraft.world.entity.monster.Guardian
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCachalotWhale;
import com.github.alexthe666.alexsmobs.entity.OrcaAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.OrcaAIJump;
import com.github.alexthe666.alexsmobs.entity.ai.OrcaAIMeleeJump;
import com.github.alexthe666.alexsmobs.entity.ai.SwimmerJumpPathNavigator;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityOrca
extends TamableAnimal
implements IAnimatedEntity {
    public static final Animation ANIMATION_BITE = Animation.create((int)8);
    public static final Animation ANIMATION_TAILSWING = Animation.create((int)20);
    private static final EntityDataAccessor<Integer> MOISTNESS = SynchedEntityData.m_135353_(EntityOrca.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final TargetingConditions PLAYER_PREDICATE = TargetingConditions.m_148353_().m_26883_(24.0).m_148355_();
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.m_135353_(EntityOrca.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    public int jumpCooldown;
    private int animationTick;
    private Animation currentAnimation;
    private int blockBreakCounter;
    public static final Predicate<LivingEntity> TARGET_BABY = animal -> animal.m_6162_();

    protected EntityOrca(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0f);
        this.f_21342_ = new MoveHelperController(this);
        this.f_21365_ = new SmoothSwimmingLookControl((Mob)this, 10);
    }

    public boolean m_6785_(double distanceToClosestPlayer) {
        return !this.m_21824_();
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.orcaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 60.0).m_22268_(Attributes.f_22277_, 64.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 10.0).m_22268_(Attributes.f_22278_, (double)0.7f).m_22268_(Attributes.f_22279_, (double)1.35f);
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new SwimmerJumpPathNavigator((Mob)this, worldIn);
    }

    public int getMoistness() {
        return (Integer)this.f_19804_.m_135370_(MOISTNESS);
    }

    public void setMoistness(int p_211137_1_) {
        this.f_19804_.m_135381_(MOISTNESS, (Object)p_211137_1_);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(MOISTNESS, (Object)2400);
        this.f_19804_.m_135372_(VARIANT, (Object)0);
    }

    public int getVariant() {
        return (Integer)this.f_19804_.m_135370_(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.m_135381_(VARIANT, (Object)variant);
    }

    public int determineVariant(BlockPos coords) {
        if (coords == null) {
            return 0;
        }
        if (coords.m_123343_() < 0) {
            return coords.m_123341_() < 0 ? 1 : 0;
        }
        return coords.m_123341_() < 0 ? 3 : 2;
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.ORCA_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.ORCA_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.ORCA_DIE.get();
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new BreathAirGoal((PathfinderMob)this));
        this.f_21345_.m_25352_(1, (Goal)new TryFindWaterGoal((PathfinderMob)this));
        this.f_21345_.m_25352_(2, (Goal)new SwimWithPlayerGoal(this, 4.0));
        this.f_21345_.m_25352_(4, (Goal)new RandomSwimmingGoal((PathfinderMob)this, 1.0, 10));
        this.f_21345_.m_25352_(4, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21345_.m_25352_(5, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21345_.m_25352_(5, (Goal)new OrcaAIJump(this, 10));
        this.f_21345_.m_25352_(6, (Goal)new OrcaAIMeleeJump(this));
        this.f_21345_.m_25352_(6, (Goal)new OrcaAIMelee(this, 1.2f, true));
        this.f_21345_.m_25352_(8, (Goal)new FollowBoatGoal((PathfinderMob)this));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]).m_26044_(new Class[0]));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<EntityCachalotWhale>((Mob)this, EntityCachalotWhale.class, 25, false, false, TARGET_BABY));
        this.f_21346_.m_25352_(3, new EntityAINearestTarget3D<LivingEntity>((Mob)this, LivingEntity.class, 200, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.ORCA_TARGETS)));
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_BITE, ANIMATION_TAILSWING};
    }

    public void m_7023_(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
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

    public void m_8024_() {
        super.m_8024_();
        this.breakBlock();
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            return;
        }
        boolean flag = false;
        if (!this.m_9236_().f_46443_ && this.blockBreakCounter == 0) {
            for (int a = (int)Math.round(this.m_20191_().f_82288_); a <= (int)Math.round(this.m_20191_().f_82291_); ++a) {
                for (int b = (int)Math.round(this.m_20191_().f_82289_) - 1; b <= (int)Math.round(this.m_20191_().f_82292_) + 1 && b <= 127; ++b) {
                    for (int c = (int)Math.round(this.m_20191_().f_82290_); c <= (int)Math.round(this.m_20191_().f_82293_); ++c) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = this.m_9236_().m_8055_(pos);
                        FluidState fluidState = this.m_9236_().m_6425_(pos);
                        Block block = state.m_60734_();
                        if (state.m_60795_() || state.m_60808_((BlockGetter)this.m_9236_(), pos).m_83281_() || !state.m_204336_(AMTagRegistry.ORCA_BREAKABLES) || !fluidState.m_76178_() || block == Blocks.f_50016_) continue;
                        this.m_20256_(this.m_20184_().m_82542_((double)0.6f, 1.0, (double)0.6f));
                        flag = true;
                        if (state.m_204336_(BlockTags.f_13047_)) {
                            this.m_9236_().m_46961_(pos, false);
                            this.m_9236_().m_46597_(pos, Blocks.f_49990_.m_49966_());
                            continue;
                        }
                        this.m_9236_().m_46961_(pos, true);
                    }
                }
            }
        }
        if (flag) {
            this.blockBreakCounter = 20;
        }
    }

    public void m_8119_() {
        super.m_8119_();
        if (this.jumpCooldown > 0) {
            --this.jumpCooldown;
            float f2 = (float)(-((double)((float)this.m_20184_().f_82480_) * 57.2957763671875));
            this.m_146926_(f2);
        }
        if (this.m_21525_()) {
            this.m_20301_(this.m_6062_());
        } else {
            if (this.m_20071_()) {
                this.setMoistness(2400);
            } else {
                this.setMoistness(this.getMoistness() - 1);
                if (this.getMoistness() <= 0) {
                    this.m_6469_(this.m_269291_().m_269483_(), 1.0f);
                }
                if (this.m_20096_()) {
                    this.m_20256_(this.m_20184_().m_82520_((double)((this.f_19796_.m_188501_() * 2.0f - 1.0f) * 0.2f), 0.5, (double)((this.f_19796_.m_188501_() * 2.0f - 1.0f) * 0.2f)));
                    this.m_146922_(this.f_19796_.m_188501_() * 360.0f);
                    this.m_6853_(false);
                    this.f_19812_ = true;
                }
            }
            if (this.m_9236_().f_46443_ && this.m_20069_() && this.m_20184_().m_82556_() > 0.03) {
                Vec3 vector3d = this.m_20252_(0.0f);
                float yRotRad = this.m_146908_() * ((float)Math.PI / 180);
                float f = Mth.m_14089_((float)yRotRad) * 0.9f;
                float f1 = Mth.m_14031_((float)yRotRad) * 0.9f;
                float f2 = 1.2f - this.f_19796_.m_188501_() * 0.7f;
                for (int i = 0; i < 2; ++i) {
                    this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123776_, this.m_20185_() - vector3d.f_82479_ * (double)f2 + (double)f, this.m_20186_() - vector3d.f_82480_, this.m_20189_() - vector3d.f_82481_ * (double)f2 + (double)f1, 0.0, 0.0, 0.0);
                    this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123776_, this.m_20185_() - vector3d.f_82479_ * (double)f2 - (double)f, this.m_20186_() - vector3d.f_82480_, this.m_20189_() - vector3d.f_82481_ * (double)f2 - (double)f1, 0.0, 0.0, 0.0);
                }
            }
        }
        LivingEntity attackTarget = this.m_5448_();
        if (attackTarget != null && this.m_20270_((Entity)attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 2.0f) {
            float damage;
            if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 4) {
                boolean flag;
                damage = (int)this.m_21133_(Attributes.f_22281_);
                if (attackTarget instanceof Drowned || attackTarget instanceof Guardian) {
                    damage *= 2.0f;
                }
                if (flag = attackTarget.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), damage)) {
                    this.m_19970_((LivingEntity)this, (Entity)attackTarget);
                    this.m_5496_(SoundEvents.f_11801_, 1.0f, 1.0f);
                }
            }
            if (this.getAnimation() == ANIMATION_TAILSWING && this.getAnimationTick() == 6) {
                boolean flag;
                damage = (int)this.m_21133_(Attributes.f_22281_);
                if (attackTarget instanceof Drowned || attackTarget instanceof Guardian) {
                    damage *= 2.0f;
                }
                if (flag = attackTarget.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), damage)) {
                    this.m_19970_((LivingEntity)this, (Entity)attackTarget);
                    this.m_5496_(SoundEvents.f_11801_, 1.0f, 1.0f);
                }
                float yRotRad = this.m_146908_() * ((float)Math.PI / 180);
                attackTarget.m_147240_(1.0, (double)Mth.m_14031_((float)yRotRad), (double)(-Mth.m_14089_((float)yRotRad)));
                float knockbackResist = (float)Mth.m_14008_((double)(1.0 - this.m_21133_(Attributes.f_22278_)), (double)0.0, (double)1.0);
                this.m_5448_().m_20256_(this.m_5448_().m_20184_().m_82520_(0.0, (double)(knockbackResist * 0.4f), 0.0));
            }
        }
        if (attackTarget != null && attackTarget instanceof Player && attackTarget.m_21023_((MobEffect)AMEffectRegistry.ORCAS_MIGHT.get())) {
            attackTarget.m_21195_((MobEffect)AMEffectRegistry.ORCAS_MIGHT.get());
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.m_20072_() && this.f_19796_.m_188499_()) {
            this.setAnimation(ANIMATION_TAILSWING);
        } else {
            this.setAnimation(ANIMATION_BITE);
        }
        return true;
    }

    public int m_6062_() {
        return 4800;
    }

    protected int m_7305_(int currentAir) {
        return this.m_6062_();
    }

    protected float m_6431_(Pose poseIn, EntityDimensions sizeIn) {
        return 1.0f;
    }

    public int m_8132_() {
        return 1;
    }

    public int m_8085_() {
        return 1;
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_41720_() == Items.f_42527_;
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob p_241840_2_) {
        return (AgeableMob)((EntityType)AMEntityRegistry.ORCA.get()).m_20615_((Level)serverWorld);
    }

    public boolean shouldUseJumpAttack(LivingEntity attackTarget) {
        if (attackTarget.m_20069_()) {
            BlockPos up = attackTarget.m_20183_().m_7494_();
            return this.m_9236_().m_6425_(up.m_7494_()).m_76178_() && this.m_9236_().m_6425_(up.m_6630_(2)).m_76178_() && this.jumpCooldown == 0;
        }
        return this.jumpCooldown == 0;
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_20301_(this.m_6062_());
        this.setVariant(this.determineVariant(this.m_20183_()));
        this.m_146926_(0.0f);
        this.setMoistness(2400);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean m_6040_() {
        return false;
    }

    public void m_6075_() {
        int i = this.m_20146_();
        super.m_6075_();
        this.updateAir(i);
    }

    public boolean m_6063_() {
        return false;
    }

    public MobType m_6336_() {
        return MobType.f_21644_;
    }

    public boolean m_6914_(LevelReader worldIn) {
        return worldIn.m_45784_((Entity)this);
    }

    protected void updateAir(int p_209207_1_) {
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128405_("Moistness", this.getMoistness());
        compound.m_128405_("Variant", this.getVariant());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setMoistness(compound.m_128451_("Moistness"));
        this.setVariant(compound.m_128451_("Variant"));
    }

    public void onJumpHit(LivingEntity entityIn) {
        boolean flag = entityIn.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)((int)this.m_21133_(Attributes.f_22281_)));
        if (flag) {
            this.m_19970_((LivingEntity)this, (Entity)entityIn);
            this.m_5496_(SoundEvents.f_11801_, 1.0f, 1.0f);
        }
    }

    public static boolean canOrcaSpawn(EntityType<EntityOrca> p_223364_0_, LevelAccessor p_223364_1_, MobSpawnType reason, BlockPos p_223364_3_, RandomSource p_223364_4_) {
        if (p_223364_3_.m_123342_() > 45 && p_223364_3_.m_123342_() < p_223364_1_.m_5736_()) {
            return p_223364_1_.m_6425_(p_223364_3_).m_205070_(FluidTags.f_13131_);
        }
        return false;
    }

    static class MoveHelperController
    extends MoveControl {
        private final EntityOrca dolphin;

        public MoveHelperController(EntityOrca dolphinIn) {
            super((Mob)dolphinIn);
            this.dolphin = dolphinIn;
        }

        public void m_8126_() {
            if (this.dolphin.m_20069_()) {
                this.dolphin.m_20256_(this.dolphin.m_20184_().m_82520_(0.0, 0.005, 0.0));
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.dolphin.m_21573_().m_26571_()) {
                double d2;
                double d1;
                double d0 = this.f_24975_ - this.dolphin.m_20185_();
                double d3 = d0 * d0 + (d1 = this.f_24976_ - this.dolphin.m_20186_()) * d1 + (d2 = this.f_24977_ - this.dolphin.m_20189_()) * d2;
                if (d3 < 2.500000277905201E-7) {
                    this.f_24974_.m_21564_(0.0f);
                } else {
                    float f = (float)(Mth.m_14136_((double)d2, (double)d0) * 57.2957763671875) - 90.0f;
                    this.dolphin.m_146922_(this.m_24991_(this.dolphin.m_146908_(), f, 10.0f));
                    this.dolphin.f_20883_ = this.dolphin.m_146908_();
                    this.dolphin.f_20885_ = this.dolphin.m_146908_();
                    float f1 = (float)(this.f_24978_ * this.dolphin.m_21133_(Attributes.f_22279_));
                    if (this.dolphin.m_20069_()) {
                        this.dolphin.m_7910_(f1 * 0.02f);
                        float f2 = -((float)(Mth.m_14136_((double)d1, (double)Mth.m_14116_((float)((float)(d0 * d0 + d2 * d2)))) * 57.2957763671875));
                        f2 = Mth.m_14036_((float)Mth.m_14177_((float)f2), (float)-85.0f, (float)85.0f);
                        this.dolphin.m_146926_(this.m_24991_(this.dolphin.m_146909_(), f2, 5.0f));
                        float xRotRad = this.dolphin.m_146909_() * ((float)Math.PI / 180);
                        float f3 = Mth.m_14089_((float)xRotRad);
                        float f4 = Mth.m_14031_((float)xRotRad);
                        this.dolphin.f_20902_ = f3 * f1;
                        this.dolphin.f_20901_ = -f4 * f1;
                    } else {
                        this.dolphin.m_7910_(f1 * 0.1f);
                    }
                }
            } else {
                this.dolphin.m_7910_(0.0f);
                this.dolphin.m_21570_(0.0f);
                this.dolphin.m_21567_(0.0f);
                this.dolphin.m_21564_(0.0f);
            }
        }
    }

    static class SwimWithPlayerGoal
    extends Goal {
        private final EntityOrca dolphin;
        private final double speed;
        private Player targetPlayer;

        SwimWithPlayerGoal(EntityOrca dolphinIn, double speedIn) {
            this.dolphin = dolphinIn;
            this.speed = speedIn;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            this.targetPlayer = this.dolphin.m_9236_().m_45946_(PLAYER_PREDICATE, (LivingEntity)this.dolphin);
            if (this.targetPlayer == null) {
                return false;
            }
            return this.targetPlayer.m_6069_() && this.dolphin.m_5448_() != this.targetPlayer;
        }

        public boolean m_8045_() {
            return this.targetPlayer != null && this.dolphin.m_5448_() != this.targetPlayer && this.targetPlayer.m_6069_() && this.dolphin.m_20280_((Entity)this.targetPlayer) < 256.0;
        }

        public void m_8056_() {
        }

        public void m_8041_() {
            this.targetPlayer = null;
            this.dolphin.m_21573_().m_26573_();
        }

        public void m_8037_() {
            this.dolphin.m_21563_().m_24960_((Entity)this.targetPlayer, (float)(this.dolphin.m_8085_() + 20), (float)this.dolphin.m_8132_());
            if (this.dolphin.m_20280_((Entity)this.targetPlayer) < 10.0) {
                this.dolphin.m_21573_().m_26573_();
            } else {
                this.dolphin.m_21573_().m_5624_((Entity)this.targetPlayer, this.speed);
            }
            if (this.targetPlayer.m_6069_() && this.targetPlayer.m_9236_().f_46441_.m_188503_(6) == 0) {
                this.targetPlayer.m_7292_(new MobEffectInstance((MobEffect)AMEffectRegistry.ORCAS_MIGHT.get(), 1000));
            }
        }
    }
}

