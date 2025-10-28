/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.FollowBoatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomSwimmingGoal
 *  net.minecraft.world.entity.ai.goal.TryFindWaterGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation
 *  net.minecraft.world.entity.animal.AbstractSchoolingFish
 *  net.minecraft.world.entity.animal.Bucketable
 *  net.minecraft.world.entity.animal.Squid
 *  net.minecraft.world.entity.animal.WaterAnimal
 *  net.minecraft.world.entity.monster.Drowned
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityBlobfish;
import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAISwimBottom;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityFrilledShark
extends WaterAnimal
implements IAnimatedEntity,
Bucketable {
    public static final Animation ANIMATION_ATTACK = Animation.create((int)17);
    private static final EntityDataAccessor<Boolean> DEPRESSURIZED = SynchedEntityData.m_135353_(EntityFrilledShark.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.m_135353_(EntityFrilledShark.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float prevOnLandProgress;
    public float onLandProgress;
    private int animationTick;
    private Animation currentAnimation;

    protected EntityFrilledShark(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new AquaticMoveController((PathfinderMob)this, 1.0f);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 20.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 3.0).m_22268_(Attributes.f_22279_, (double)0.2f);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(DEPRESSURIZED, (Object)false);
        this.f_19804_.m_135372_(FROM_BUCKET, (Object)false);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(1, (Goal)new TryFindWaterGoal((PathfinderMob)this));
        this.f_21345_.m_25352_(2, (Goal)new AIMelee());
        this.f_21345_.m_25352_(3, (Goal)new AnimalAISwimBottom((PathfinderMob)this, 0.8f, 7));
        this.f_21345_.m_25352_(4, (Goal)new RandomSwimmingGoal((PathfinderMob)this, (double)0.8f, 3));
        this.f_21345_.m_25352_(5, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21345_.m_25352_(6, (Goal)new FollowBoatGoal((PathfinderMob)this));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<Squid>((Mob)this, Squid.class, 40, false, true, null));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<EntityMimicOctopus>((Mob)this, EntityMimicOctopus.class, 70, false, true, null));
        this.f_21346_.m_25352_(3, new EntityAINearestTarget3D<AbstractSchoolingFish>((Mob)this, AbstractSchoolingFish.class, 100, false, true, null));
        this.f_21346_.m_25352_(4, new EntityAINearestTarget3D<EntityBlobfish>((Mob)this, EntityBlobfish.class, 70, false, true, null));
        this.f_21346_.m_25352_(5, new EntityAINearestTarget3D<Drowned>((Mob)this, Drowned.class, 4, false, true, null));
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.frilledSharkSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canFrilledSharkSpawn(EntityType<EntityFrilledShark> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_46801_(pos) && iServerWorld.m_46801_(pos.m_7494_());
    }

    public boolean m_27487_() {
        return (Boolean)this.f_19804_.m_135370_(FROM_BUCKET);
    }

    public void m_27497_(boolean p_203706_1_) {
        this.f_19804_.m_135381_(FROM_BUCKET, (Object)p_203706_1_);
    }

    @Nonnull
    public SoundEvent m_142623_() {
        return SoundEvents.f_11782_;
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("FromBucket", this.m_27487_());
        compound.m_128379_("Depressurized", this.isDepressurized());
    }

    public boolean m_8023_() {
        return super.m_8023_() || this.m_27487_();
    }

    public boolean m_6785_(double p_213397_1_) {
        return !this.m_27487_() && !this.m_8077_();
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.m_27497_(compound.m_128471_("FromBucket"));
        this.setDepressurized(compound.m_128471_("Depressurized"));
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).m_76178_() && down.m_123342_() > 1) {
            down = down.m_7495_();
        }
        this.m_6034_((float)down.m_123341_() + 0.5f, down.m_123342_() + 1, (float)down.m_123343_() + 0.5f);
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing((LevelAccessor)worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean m_6914_(LevelReader worldIn) {
        return worldIn.m_45784_((Entity)this);
    }

    public boolean isDepressurized() {
        return (Boolean)this.f_19804_.m_135370_(DEPRESSURIZED);
    }

    public void setDepressurized(boolean depressurized) {
        this.f_19804_.m_135381_(DEPRESSURIZED, (Object)depressurized);
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new WaterBoundPathNavigation((Mob)this, worldIn);
    }

    protected SoundEvent m_5592_() {
        return SoundEvents.f_11759_;
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return SoundEvents.f_11761_;
    }

    @Nonnull
    public ItemStack m_28282_() {
        ItemStack stack = new ItemStack((ItemLike)AMItemRegistry.FRILLED_SHARK_BUCKET.get());
        if (this.m_8077_()) {
            stack.m_41714_(this.m_7770_());
        }
        return stack;
    }

    public void m_6872_(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.m_41714_(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.m_7380_(platTag);
        CompoundTag compound = bucket.m_41784_();
        compound.m_128365_("FrilledSharkData", (Tag)platTag);
    }

    public void m_142278_(@Nonnull CompoundTag compound) {
        if (compound.m_128441_("FrilledSharkData")) {
            this.m_7378_(compound.m_128469_("FrilledSharkData"));
        }
    }

    @Nonnull
    protected InteractionResult m_6071_(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return Bucketable.m_148828_((Player)player, (InteractionHand)hand, (LivingEntity)this).orElse(super.m_6071_(player, hand));
    }

    public void m_7023_(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().m_82542_(0.9, 0.6, 0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().m_82520_(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    public void m_267651_(boolean flying) {
        float f1 = (float)Mth.m_184648_((double)(this.m_20185_() - this.f_19854_), (double)(this.m_20186_() - this.f_19855_), (double)(this.m_20189_() - this.f_19856_));
        float f2 = Math.min(f1 * 8.0f, 1.0f);
        this.f_267362_.m_267566_(f2, 0.4f);
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        if (!this.m_20069_() && this.onLandProgress < 5.0f) {
            this.onLandProgress += 1.0f;
        }
        if (this.m_20069_() && this.onLandProgress > 0.0f) {
            this.onLandProgress -= 1.0f;
        }
        if (this.m_20069_()) {
            this.m_20256_(this.m_20184_().m_82542_(1.0, 0.8, 1.0));
        }
        boolean clear = this.hasClearance();
        if (this.isDepressurized() && clear) {
            this.setDepressurized(false);
        }
        if (!this.isDepressurized() && !clear) {
            this.setDepressurized(true);
        }
        if (!this.m_9236_().f_46443_ && this.m_5448_() != null && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 12) {
            float f1 = this.m_146908_() * ((float)Math.PI / 180);
            this.m_20256_(this.m_20184_().m_82520_((double)(-Mth.m_14031_((float)f1) * 0.06f), 0.0, (double)(Mth.m_14089_((float)f1) * 0.06f)));
            if (this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_())) {
                this.m_5448_().m_7292_(new MobEffectInstance((MobEffect)AMEffectRegistry.EXSANGUINATION.get(), 60, 2));
                if (this.f_19796_.m_188503_(15) == 0 && this.m_5448_() instanceof Squid) {
                    this.m_19998_((ItemLike)AMItemRegistry.SERRATED_SHARK_TOOTH.get());
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    public boolean m_6469_(DamageSource source, float amount) {
        if (source.m_7639_() instanceof Drowned) {
            amount *= 0.5f;
        }
        return super.m_6469_(source, amount);
    }

    private boolean hasClearance() {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (int l1 = 0; l1 < 10; ++l1) {
            BlockState blockstate = this.m_9236_().m_8055_((BlockPos)blockpos$mutable.m_122169_(this.m_20185_(), this.m_20186_() + (double)l1, this.m_20189_()));
            if (blockstate.m_60819_().m_205070_(FluidTags.f_13131_)) continue;
            return false;
        }
        return true;
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public boolean isKaiju() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("kamata kun") || s.toLowerCase().contains("kamata-kun"));
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_ATTACK};
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 68) {
            double d2 = this.f_19796_.m_188583_() * 0.1;
            double d0 = this.f_19796_.m_188583_() * 0.1;
            double d1 = this.f_19796_.m_188583_() * 0.1;
            float radius = this.m_20205_() * 0.8f;
            float angle = (float)Math.PI / 180 * this.f_20883_;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            double x = this.m_20185_() + extraX + d0;
            double y = this.m_20186_() + (double)(this.m_20206_() * 0.15f) + d1;
            double z = this.m_20189_() + extraZ + d2;
            this.m_9236_().m_7106_((ParticleOptions)AMParticleRegistry.TEETH_GLINT.get(), x, y, z, this.m_20184_().f_82479_, this.m_20184_().f_82480_, this.m_20184_().f_82481_);
        } else {
            super.m_7822_(id);
        }
    }

    private class AIMelee
    extends Goal {
        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            return EntityFrilledShark.this.m_5448_() != null && EntityFrilledShark.this.m_5448_().m_6084_();
        }

        public void m_8037_() {
            LivingEntity target = EntityFrilledShark.this.m_5448_();
            double speed = 1.0;
            if (EntityFrilledShark.this.m_20270_((Entity)target) < 10.0f) {
                if ((double)EntityFrilledShark.this.m_20270_((Entity)target) < 1.9) {
                    EntityFrilledShark.this.m_7327_((Entity)target);
                    speed = 0.8f;
                } else {
                    speed = 0.6f;
                    EntityFrilledShark.this.m_21391_((Entity)target, 70.0f, 70.0f);
                    if (target instanceof Squid) {
                        Vec3 mouth = EntityFrilledShark.this.m_20182_();
                        float squidSpeed = 0.07f;
                        ((Squid)target).m_29958_((float)(mouth.f_82479_ - target.m_20185_()) * squidSpeed, (float)(mouth.f_82480_ - target.m_20188_()) * squidSpeed, (float)(mouth.f_82481_ - target.m_20189_()) * squidSpeed);
                        EntityFrilledShark.this.m_9236_().m_7605_((Entity)EntityFrilledShark.this, (byte)68);
                    }
                }
            }
            if (target instanceof Drowned || target instanceof Player) {
                speed = 1.0;
            }
            EntityFrilledShark.this.m_21573_().m_5624_((Entity)target, speed);
        }
    }
}

