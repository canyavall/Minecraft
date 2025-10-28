/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ItemParticleOption
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
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.JumpControl
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.control.MoveControl$Operation
 *  net.minecraft.world.entity.ai.goal.AvoidEntityGoal
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.PanicGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.Cat
 *  net.minecraft.world.entity.animal.Ocelot
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.Path
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityRattlesnake;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.JerboaAIBeg;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityJerboa
extends Animal {
    private static final EntityDataAccessor<Boolean> JUMP_ACTIVE = SynchedEntityData.m_135353_(EntityJerboa.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.m_135353_(EntityJerboa.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.m_135353_(EntityJerboa.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> BEFRIENDED = SynchedEntityData.m_135353_(EntityJerboa.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float jumpProgress;
    public float prevJumpProgress;
    public float reboundProgress;
    public float prevReboundProgress;
    public float begProgress;
    public float prevBegProgress;
    public float sleepProgress;
    public float prevSleepProgress;
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;

    protected EntityJerboa(EntityType<? extends Animal> jerboa, Level lvl) {
        super(jerboa, lvl);
        this.f_21342_ = new MoveHelperController(this);
        this.f_21343_ = new JumpHelperController(this);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 4.0).m_22268_(Attributes.f_22279_, (double)0.45f);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(JUMP_ACTIVE, (Object)false);
        this.f_19804_.m_135372_(BEGGING, (Object)false);
        this.f_19804_.m_135372_(SLEEPING, (Object)false);
        this.f_19804_.m_135372_(BEFRIENDED, (Object)false);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new JerboaAIBeg(this, 1.0));
        this.f_21345_.m_25352_(2, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(3, (Goal)new AvoidEntityGoal((PathfinderMob)this, Player.class, 5.0f, 1.3, 1.0){

            public boolean m_8036_() {
                return !EntityJerboa.this.isBefriended() && super.m_8036_();
            }
        });
        this.f_21345_.m_25352_(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, Cat.class, 9.0f, 1.3, 1.0));
        this.f_21345_.m_25352_(5, (Goal)new AvoidEntityGoal((PathfinderMob)this, Ocelot.class, 9.0f, 1.3, 1.0));
        this.f_21345_.m_25352_(6, (Goal)new AvoidEntityGoal((PathfinderMob)this, EntityRattlesnake.class, 9.0f, 1.3, 1.0));
        this.f_21345_.m_25352_(7, (Goal)new PanicGoal((PathfinderMob)this, 1.1));
        this.f_21345_.m_25352_(8, (Goal)new AnimalAIWanderRanged((PathfinderMob)this, 20, 1.0, 10, 7));
        this.f_21345_.m_25352_(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0f));
        this.f_21345_.m_25352_(10, (Goal)new RandomLookAroundGoal((Mob)this));
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setBefriended(compound.m_128471_("Befriended"));
        this.setSleeping(compound.m_128471_("Sleeping"));
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Befriended", this.isBefriended());
        compound.m_128379_("Sleeping", this.m_5803_());
    }

    public boolean m_6785_(double distanceToClosestPlayer) {
        return !this.m_8023_();
    }

    public boolean m_8023_() {
        return super.m_8023_() || this.isBefriended();
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.JERBOA_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.JERBOA_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.JERBOA_HURT.get();
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevJumpProgress = this.jumpProgress;
        this.prevReboundProgress = this.reboundProgress;
        this.prevSleepProgress = this.sleepProgress;
        this.prevBegProgress = this.begProgress;
        if (!this.m_9236_().f_46443_) {
            this.f_19804_.m_135381_(JUMP_ACTIVE, (Object)(!this.m_20096_() ? 1 : 0));
        }
        if (((Boolean)this.f_19804_.m_135370_(JUMP_ACTIVE)).booleanValue()) {
            if (this.jumpProgress < 5.0f) {
                this.jumpProgress += 1.0f;
                if (this.reboundProgress > 0.0f) {
                    this.reboundProgress -= 1.0f;
                }
            }
            if (this.jumpProgress >= 5.0f && this.reboundProgress < 5.0f) {
                this.reboundProgress += 1.0f;
            }
        } else {
            if (this.reboundProgress > 0.0f) {
                this.reboundProgress = Math.max(this.reboundProgress - 1.0f, 0.0f);
            }
            if (this.jumpProgress > 0.0f) {
                this.jumpProgress = Math.max(this.jumpProgress - 1.0f, 0.0f);
            }
        }
        if (this.isBegging()) {
            if (this.begProgress < 5.0f) {
                this.begProgress += 1.0f;
            }
        } else if (this.begProgress > 0.0f) {
            this.begProgress -= 1.0f;
        }
        if (this.m_5803_()) {
            if (this.sleepProgress < 5.0f) {
                this.sleepProgress += 1.0f;
            }
        } else if (this.sleepProgress > 0.0f) {
            this.sleepProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            if (this.m_9236_().m_46461_() && this.m_21188_() == null && !this.isBegging()) {
                if (this.f_19797_ % 10 == 0 && this.m_217043_().m_188503_(750) == 0) {
                    this.setSleeping(true);
                }
            } else if (this.m_5803_()) {
                this.setSleeping(false);
            }
        }
    }

    public boolean isBegging() {
        return (Boolean)this.f_19804_.m_135370_(BEGGING);
    }

    public void setBegging(boolean begging) {
        this.f_19804_.m_135381_(BEGGING, (Object)begging);
    }

    public boolean m_5803_() {
        return (Boolean)this.f_19804_.m_135370_(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.f_19804_.m_135381_(SLEEPING, (Object)sleeping);
    }

    public boolean isBefriended() {
        return (Boolean)this.f_19804_.m_135370_(BEFRIENDED);
    }

    public void setBefriended(boolean befriended) {
        this.f_19804_.m_135381_(BEFRIENDED, (Object)befriended);
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if ((itemstack.m_204117_(AMTagRegistry.JERBOA_BEGS_FOR) || this.m_6898_(itemstack)) && (this.m_21223_() < this.m_21233_() || !this.isBefriended())) {
            if (!player.m_7500_()) {
                itemstack.m_41774_(1);
            }
            this.setBefriended(true);
            this.m_5634_(4.0f);
            return InteractionResult.SUCCESS;
        }
        InteractionResult type = super.m_6071_(player, hand);
        if (type != InteractionResult.SUCCESS && !this.m_6898_(itemstack) && itemstack.m_204117_(AMTagRegistry.JERBOA_BEGS_FOR)) {
            this.setSleeping(false);
            this.m_146850_(GameEvent.f_223708_);
            this.m_5496_(SoundEvents.f_12190_, this.m_6100_(), this.m_6121_());
            for (int i = 0; i < 6 + this.f_19796_.m_188503_(3); ++i) {
                double d2 = this.f_19796_.m_188583_() * 0.02;
                double d0 = this.f_19796_.m_188583_() * 0.02;
                double d1 = this.f_19796_.m_188583_() * 0.02;
                this.m_9236_().m_7106_((ParticleOptions)new ItemParticleOption(ParticleTypes.f_123752_, itemstack), this.m_20185_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, this.m_20186_() + (double)(this.m_20206_() * 0.5f) + (double)(this.f_19796_.m_188501_() * this.m_20206_() * 0.5f), this.m_20189_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, d0, d1, d2);
            }
            if (this.f_19796_.m_188501_() <= 0.3f) {
                player.m_7292_(new MobEffectInstance((MobEffect)AMEffectRegistry.FLEET_FOOTED.get(), 12000));
            }
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean m_6469_(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev) {
            LivingEntity hurter;
            this.setSleeping(false);
            if (source.m_7639_() != null && source.m_7639_() instanceof LivingEntity && (hurter = (LivingEntity)source.m_7639_()).m_21023_((MobEffect)AMEffectRegistry.FLEET_FOOTED.get())) {
                hurter.m_21195_((MobEffect)AMEffectRegistry.FLEET_FOOTED.get());
            }
            return prev;
        }
        return prev;
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.JERBOA_BREEDABLES);
    }

    public boolean shouldMove() {
        return !this.m_5803_();
    }

    public float getJumpCompletion(float partialTicks) {
        return this.jumpDuration == 0 ? 0.0f : ((float)this.jumpTicks + partialTicks) / (float)this.jumpDuration;
    }

    protected float m_6118_() {
        return this.f_19862_ ? super.m_6118_() + 0.2f : 0.25f + this.f_19796_.m_188501_() * 0.15f;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public static boolean isValidLightLevel(ServerLevelAccessor p_223323_0_, BlockPos p_223323_1_, RandomSource p_223323_2_) {
        int light = p_223323_0_.m_46803_(p_223323_1_);
        return light <= 4;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.jerboaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canMonsterSpawnInLight(EntityType<? extends EntityJerboa> p_223325_0_, ServerLevelAccessor p_223325_1_, MobSpawnType p_223325_2_, BlockPos p_223325_3_, RandomSource p_223325_4_) {
        return EntityJerboa.isValidLightLevel(p_223325_1_, p_223325_3_, p_223325_4_) && EntityJerboa.m_217057_(p_223325_0_, (LevelAccessor)p_223325_1_, (MobSpawnType)p_223325_2_, (BlockPos)p_223325_3_, (RandomSource)p_223325_4_);
    }

    public static <T extends Mob> boolean canJerboaSpawn(EntityType<EntityJerboa> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_45527_(pos.m_7494_()) && EntityJerboa.canMonsterSpawnInLight(entityType, iServerWorld, reason, pos, random);
    }

    protected void m_6135_() {
        double d1;
        super.m_6135_();
        double d0 = this.f_21342_.m_24999_();
        if (!(d0 > 0.0) || (d1 = this.m_20184_().m_165924_()) < 0.01) {
            // empty if block
        }
        if (!this.m_9236_().f_46443_) {
            this.m_9236_().m_7605_((Entity)this, (byte)1);
        }
    }

    public void setMovementSpeed(double newSpeed) {
        this.m_21573_().m_26517_(newSpeed);
        this.f_21342_.m_6849_(this.f_21342_.m_25000_(), this.f_21342_.m_25001_(), this.f_21342_.m_25002_(), newSpeed);
    }

    public void startJumping() {
        this.m_6862_(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    private void checkLandingDelay() {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }

    private void calculateRotationYaw(double x, double z) {
        this.m_146922_((float)(Mth.m_14136_((double)(z - this.m_20189_()), (double)(x - this.m_20185_())) * 57.2957763671875) - 90.0f);
    }

    private void enableJumpControl() {
        if (this.f_21343_ instanceof JumpHelperController) {
            ((JumpHelperController)this.f_21343_).setCanJump(true);
        }
    }

    private void disableJumpControl() {
        if (this.f_21343_ instanceof JumpHelperController) {
            ((JumpHelperController)this.f_21343_).setCanJump(false);
        }
    }

    private void updateMoveTypeDuration() {
        this.currentMoveTypeDuration = this.f_21342_.m_24999_() < 2.2 ? 2 : 1;
    }

    public void m_8024_() {
        super.m_8024_();
        if (this.currentMoveTypeDuration > 0) {
            --this.currentMoveTypeDuration;
        }
        if (this.m_20096_() && this.shouldMove()) {
            LivingEntity livingentity;
            if (!this.wasOnGround) {
                this.m_6862_(false);
                this.checkLandingDelay();
            }
            if (this.currentMoveTypeDuration == 0 && (livingentity = this.m_5448_()) != null && this.m_20280_((Entity)livingentity) < 16.0) {
                this.calculateRotationYaw(livingentity.m_20185_(), livingentity.m_20189_());
                this.f_21342_.m_6849_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_(), this.f_21342_.m_24999_());
                this.startJumping();
                this.wasOnGround = true;
            }
            if (this.f_21343_ instanceof JumpHelperController) {
                JumpHelperController rabbitController = (JumpHelperController)this.f_21343_;
                if (!rabbitController.getIsJumping()) {
                    if (this.f_21342_.m_24995_() && this.currentMoveTypeDuration == 0) {
                        Path path = this.f_21344_.m_26570_();
                        Vec3 vector3d = new Vec3(this.f_21342_.m_25000_(), this.f_21342_.m_25001_(), this.f_21342_.m_25002_());
                        if (path != null && !path.m_77392_()) {
                            vector3d = path.m_77380_((Entity)this);
                        }
                        this.calculateRotationYaw(vector3d.f_82479_, vector3d.f_82481_);
                        this.startJumping();
                    }
                } else if (!rabbitController.canJump()) {
                    this.enableJumpControl();
                }
            }
        } else if (!this.shouldMove()) {
            this.m_6862_(false);
            this.checkLandingDelay();
        }
        this.wasOnGround = this.m_20096_();
    }

    public void m_8107_() {
        super.m_8107_();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.m_6862_(false);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 1) {
            this.m_20076_();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.m_7822_(id);
        }
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel p_146743_, AgeableMob p_146744_) {
        EntityJerboa boa = (EntityJerboa)((EntityType)AMEntityRegistry.JERBOA.get()).m_20615_((Level)p_146743_);
        boa.setBefriended(true);
        return boa;
    }

    public boolean hasJumper() {
        return this.f_21343_ instanceof JumpHelperController;
    }

    static class MoveHelperController
    extends MoveControl {
        private final EntityJerboa jerboa;
        private double nextJumpSpeed;

        public MoveHelperController(EntityJerboa jerboa) {
            super((Mob)jerboa);
            this.jerboa = jerboa;
        }

        public void m_8126_() {
            if (this.jerboa.hasJumper() && this.jerboa.m_20096_() && !this.jerboa.f_20899_ && !((JumpHelperController)this.jerboa.f_21343_).getIsJumping()) {
                this.jerboa.setMovementSpeed(0.0);
            } else if (this.m_24995_()) {
                this.jerboa.setMovementSpeed(this.nextJumpSpeed);
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                this.f_24981_ = MoveControl.Operation.WAIT;
                Vec3 vector3d = new Vec3(this.f_24975_ - this.jerboa.m_20185_(), this.f_24976_ - this.jerboa.m_20186_(), this.f_24977_ - this.jerboa.m_20189_());
                double d0 = vector3d.m_82553_();
                this.jerboa.m_20256_(this.jerboa.m_20184_().m_82549_(vector3d.m_82490_(this.f_24978_ * 1.0 * 0.05 / d0)));
            }
            super.m_8126_();
        }

        public void m_6849_(double x, double y, double z, double speedIn) {
            if (this.jerboa.m_20069_()) {
                speedIn = 1.5;
            }
            super.m_6849_(x, y, z, speedIn);
            if (speedIn > 0.0) {
                this.nextJumpSpeed = speedIn;
            }
        }
    }

    public static class JumpHelperController
    extends JumpControl {
        private final EntityJerboa jerboa;
        private boolean canJump;

        public JumpHelperController(EntityJerboa jerboa) {
            super((Mob)jerboa);
            this.jerboa = jerboa;
        }

        public boolean getIsJumping() {
            return this.f_24897_;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean canJumpIn) {
            this.canJump = canJumpIn;
        }

        public void m_8124_() {
            if (this.f_24897_) {
                this.jerboa.startJumping();
                this.f_24897_ = false;
            }
        }
    }
}

