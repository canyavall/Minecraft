/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.stats.Stats
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ExperienceOrb
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.BreathAirGoal
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MoveToBlockGoal
 *  net.minecraft.world.entity.ai.goal.PanicGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.Bucketable
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockReptileEgg;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.PlatypusAIDigForItems;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityPlatypus
extends Animal
implements ISemiAquatic,
ITargetsDroppedItems,
Bucketable {
    private static final EntityDataAccessor<Boolean> SENSING = SynchedEntityData.m_135353_(EntityPlatypus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SENSING_VISUAL = SynchedEntityData.m_135353_(EntityPlatypus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.m_135353_(EntityPlatypus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> FEDORA = SynchedEntityData.m_135353_(EntityPlatypus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.m_135353_(EntityPlatypus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.m_135353_(EntityPlatypus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float prevInWaterProgress;
    public float inWaterProgress;
    public float prevDigProgress;
    public float digProgress;
    public boolean superCharged = false;
    private boolean isLandNavigator;
    private int swimTimer = -1000;

    protected EntityPlatypus(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0f);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0f);
        this.switchNavigator(false);
    }

    public static boolean canPlatypusSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_8055_(pos.m_7495_()).m_204336_(AMTagRegistry.PLATYPUS_SPAWNS) && pos.m_123342_() < worldIn.m_5736_() + 4;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.platypusSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 10.0).m_22268_(Attributes.f_22277_, 16.0).m_22268_(Attributes.f_22279_, (double)0.2f);
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.PLATYPUS_BREEDABLES);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.PLATYPUS_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.PLATYPUS_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.PLATYPUS_HURT.get();
    }

    @Nonnull
    public ItemStack m_28282_() {
        ItemStack stack = new ItemStack((ItemLike)AMItemRegistry.PLATYPUS_BUCKET.get());
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
        compound.m_128365_("PlatypusData", (Tag)platTag);
    }

    public void m_142278_(@Nonnull CompoundTag compound) {
        if (compound.m_128441_("PlatypusData")) {
            this.m_7378_(compound.m_128469_("PlatypusData"));
        }
    }

    @Nonnull
    public InteractionResult m_6071_(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.m_41720_() == AMItemRegistry.FEDORA.get() && !this.hasFedora()) {
            if (!player.m_7500_()) {
                itemstack.m_41774_(1);
            }
            this.setFedora(true);
            return InteractionResult.m_19078_((boolean)this.m_9236_().f_46443_);
        }
        if (itemstack.m_204117_(AMTagRegistry.PLATYPUS_CHARGEABLES) && !this.isSensing()) {
            this.superCharged = itemstack.m_204117_(AMTagRegistry.PLATYPUS_SUPER_CHARGEABLES);
            if (!player.m_7500_()) {
                itemstack.m_41774_(1);
            }
            this.setSensing(true);
            return InteractionResult.m_19078_((boolean)this.m_9236_().f_46443_);
        }
        return Bucketable.m_148828_((Player)player, (InteractionHand)hand, (LivingEntity)this).orElse(super.m_6071_(player, hand));
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new BreathAirGoal((PathfinderMob)this));
        this.f_21345_.m_25352_(1, (Goal)new AnimalAIFindWater((PathfinderMob)this));
        this.f_21345_.m_25352_(1, (Goal)new AnimalAILeaveWater((PathfinderMob)this));
        this.f_21345_.m_25352_(2, (Goal)new MateGoal(this, 1.0));
        this.f_21345_.m_25352_(2, (Goal)new LayEggGoal(this, 1.0));
        this.f_21345_.m_25352_(2, (Goal)new BreedGoal((Animal)this, 0.8));
        this.f_21345_.m_25352_(3, (Goal)new PanicGoal((PathfinderMob)this, 1.1));
        this.f_21345_.m_25352_(3, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_204132_(AMTagRegistry.PLATYPUS_CHARGEABLES), false){

            public void m_8056_() {
                super.m_8056_();
                EntityPlatypus.this.setSensingVisual(true);
            }

            public boolean m_8036_() {
                return super.m_8036_() && !EntityPlatypus.this.isSensing();
            }

            public void m_8041_() {
                super.m_8041_();
                EntityPlatypus.this.setSensingVisual(false);
            }
        });
        this.f_21345_.m_25352_(5, (Goal)new TemptGoal((PathfinderMob)this, 1.1, Ingredient.m_204132_(AMTagRegistry.PLATYPUS_FOODSTUFFS), false){

            public boolean m_8036_() {
                return super.m_8036_() && !EntityPlatypus.this.isSensing();
            }
        });
        this.f_21345_.m_25352_(5, (Goal)new PlatypusAIDigForItems(this));
        this.f_21345_.m_25352_(6, (Goal)new SemiAquaticAIRandomSwimming(this, 1.0, 30));
        this.f_21345_.m_25352_(7, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0, 60));
        this.f_21345_.m_25352_(8, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21345_.m_25352_(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21346_.m_25352_(1, (Goal)new CreatureAITargetItems((PathfinderMob)this, false, false, 40, 15){

            @Override
            public boolean m_8036_() {
                return super.m_8036_() && !EntityPlatypus.this.isSensing();
            }

            @Override
            public boolean m_8045_() {
                return super.m_8045_() && !EntityPlatypus.this.isSensing();
            }
        });
    }

    public boolean m_6469_(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev && source.m_7640_() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity)source.m_7640_();
            entity.m_7292_(new MobEffectInstance(MobEffects.f_19614_, 100));
        }
        return prev;
    }

    public boolean isPerry() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("perry");
    }

    public int m_6062_() {
        return 4800;
    }

    protected int m_7305_(int currentAir) {
        return this.m_6062_();
    }

    public void spawnGroundEffects() {
        float radius = 0.3f;
        for (int i1 = 0; i1 < 3; ++i1) {
            double motionX = this.m_217043_().m_188583_() * 0.07;
            double motionY = this.m_217043_().m_188583_() * 0.07;
            double motionZ = this.m_217043_().m_188583_() * 0.07;
            float angle = (float)Math.PI / 180 * this.f_20883_ + (float)i1;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraY = 0.8f;
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos ground = this.m_20099_();
            BlockState state = this.m_9236_().m_8055_(ground);
            if (!state.m_280296_() || !this.m_9236_().f_46443_) continue;
            this.m_9236_().m_6493_((ParticleOptions)new BlockParticleOption(ParticleTypes.f_123794_, state), true, this.m_20185_() + extraX, (double)ground.m_123342_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
        }
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_20301_(this.m_6062_());
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean m_6063_() {
        return false;
    }

    public void m_7023_(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().m_82490_(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(DIGGING, (Object)false);
        this.f_19804_.m_135372_(SENSING, (Object)false);
        this.f_19804_.m_135372_(SENSING_VISUAL, (Object)false);
        this.f_19804_.m_135372_(FEDORA, (Object)false);
        this.f_19804_.m_135372_(FROM_BUCKET, (Object)false);
        this.f_19804_.m_135372_(HAS_EGG, (Object)false);
    }

    protected void m_5907_() {
        super.m_5907_();
        if (this.hasFedora()) {
            this.m_19998_((ItemLike)AMItemRegistry.FEDORA.get());
        }
    }

    public boolean isSensing() {
        return (Boolean)this.f_19804_.m_135370_(SENSING);
    }

    public void setSensing(boolean sensing) {
        this.f_19804_.m_135381_(SENSING, (Object)sensing);
    }

    public boolean isSensingVisual() {
        return (Boolean)this.f_19804_.m_135370_(SENSING_VISUAL);
    }

    public void setSensingVisual(boolean sensing) {
        this.f_19804_.m_135381_(SENSING_VISUAL, (Object)sensing);
    }

    public boolean hasFedora() {
        return (Boolean)this.f_19804_.m_135370_(FEDORA);
    }

    public void setFedora(boolean sensing) {
        this.f_19804_.m_135381_(FEDORA, (Object)sensing);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Fedora", this.hasFedora());
        compound.m_128379_("Sensing", this.isSensing());
        compound.m_128379_("FromBucket", this.m_27487_());
        compound.m_128379_("HasEgg", this.hasEgg());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFedora(compound.m_128471_("Fedora"));
        this.setSensing(compound.m_128471_("Sensing"));
        this.m_27497_(compound.m_128471_("FromBucket"));
        this.setHasEgg(compound.m_128471_("HasEgg"));
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

    public boolean m_8023_() {
        return super.m_8023_() || this.m_27487_() || this.m_8077_();
    }

    public boolean m_6785_(double dist) {
        return !this.m_27487_() && !this.m_8023_();
    }

    public void m_8119_() {
        boolean dig;
        super.m_8119_();
        this.prevInWaterProgress = this.inWaterProgress;
        this.prevDigProgress = this.digProgress;
        boolean bl = dig = this.isDigging() && this.m_20072_();
        if (dig && this.digProgress < 5.0f) {
            this.digProgress += 1.0f;
        }
        if (!dig && this.digProgress > 0.0f) {
            this.digProgress -= 1.0f;
        }
        if (this.m_20072_()) {
            if (this.inWaterProgress < 5.0f) {
                this.inWaterProgress += 1.0f;
            }
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else {
            if (this.inWaterProgress > 0.0f) {
                this.inWaterProgress -= 1.0f;
            }
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (this.m_20096_() && this.isDigging()) {
            this.spawnGroundEffects();
        }
        if (this.inWaterProgress > 0.0f) {
            this.m_274367_(1.0f);
        } else {
            this.m_274367_(0.6f);
        }
        if (!this.m_9236_().f_46443_) {
            this.swimTimer = this.m_20069_() ? ++this.swimTimer : --this.swimTimer;
        }
        if (this.m_6084_() && (this.isSensing() || this.isSensingVisual())) {
            for (int j = 0; j < 2; ++j) {
                float radius = this.m_20205_() * 0.65f;
                float angle = (float)Math.PI / 180 * this.f_20883_;
                double extraX = (double)(radius * (1.5f + this.f_19796_.m_188501_() * 0.3f) * Mth.m_14031_((float)((float)Math.PI + angle)) + (this.f_19796_.m_188501_() - 0.5f)) + this.m_20184_().f_82479_ * 2.0;
                double extraZ = (double)(radius * (1.5f + this.f_19796_.m_188501_() * 0.3f) * Mth.m_14089_((float)angle) + (this.f_19796_.m_188501_() - 0.5f)) + this.m_20184_().f_82481_ * 2.0;
                double actualX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
                double actualZ = radius * Mth.m_14089_((float)angle);
                double motX = actualX - extraX;
                double motZ = actualZ - extraZ;
                this.m_9236_().m_7106_((ParticleOptions)AMParticleRegistry.PLATYPUS_SENSE.get(), this.m_20185_() + extraX, (double)(this.m_20206_() * 0.3f) + this.m_20186_(), this.m_20189_() + extraZ, motX * (double)0.1f, 0.0, motZ * (double)0.1f);
            }
        }
    }

    public boolean isDigging() {
        return (Boolean)this.f_19804_.m_135370_(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.f_19804_.m_135381_(DIGGING, (Object)digging);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = new GroundPathNavigatorWide((Mob)this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink((PathfinderMob)this, 1.2f, 1.6f);
            this.f_21344_ = new SemiAquaticPathNavigator((Mob)this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public boolean shouldEnterWater() {
        return (this.m_21188_() != null || this.swimTimer <= -1000 || this.isSensing()) && !this.hasEgg();
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.swimTimer > 600 && !this.isSensing() || this.hasEgg();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isDigging();
    }

    @Override
    public int getWaterSearchRange() {
        return 10;
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return (AgeableMob)((EntityType)AMEntityRegistry.PLATYPUS.get()).m_20615_((Level)serverWorld);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return !this.isSensing() && stack.m_204117_(AMTagRegistry.PLATYPUS_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_146850_(GameEvent.f_157806_);
        this.m_5496_(SoundEvents.f_11788_, this.m_6121_(), this.m_6100_());
        if (e.m_32055_().m_204117_(AMTagRegistry.PLATYPUS_CHARGEABLES)) {
            this.superCharged = e.m_32055_().m_204117_(AMTagRegistry.PLATYPUS_SUPER_CHARGEABLES);
            this.setSensing(true);
        }
    }

    public boolean hasEgg() {
        return (Boolean)this.f_19804_.m_135370_(HAS_EGG);
    }

    private void setHasEgg(boolean hasEgg) {
        this.f_19804_.m_135381_(HAS_EGG, (Object)hasEgg);
    }

    static class MateGoal
    extends BreedGoal {
        private final EntityPlatypus platypus;

        MateGoal(EntityPlatypus platypus, double speedIn) {
            super((Animal)platypus, speedIn);
            this.platypus = platypus;
        }

        public boolean m_8036_() {
            return super.m_8036_() && !this.platypus.hasEgg();
        }

        protected void m_8026_() {
            ServerPlayer serverplayerentity = this.f_25113_.m_27592_();
            if (serverplayerentity == null && this.f_25115_.m_27592_() != null) {
                serverplayerentity = this.f_25115_.m_27592_();
            }
            if (serverplayerentity != null) {
                serverplayerentity.m_36220_(Stats.f_12937_);
                CriteriaTriggers.f_10581_.m_147278_(serverplayerentity, this.f_25113_, this.f_25115_, (AgeableMob)this.f_25113_);
            }
            this.platypus.setHasEgg(true);
            this.f_25113_.m_27594_();
            this.f_25115_.m_27594_();
            this.f_25113_.m_146762_(6000);
            this.f_25115_.m_146762_(6000);
            if (this.f_25114_.m_46469_().m_46207_(GameRules.f_46135_)) {
                RandomSource random = this.f_25113_.m_217043_();
                this.f_25114_.m_7967_((Entity)new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), random.m_188503_(7) + 1));
            }
        }
    }

    static class LayEggGoal
    extends MoveToBlockGoal {
        private final EntityPlatypus turtle;
        private int digTime;

        LayEggGoal(EntityPlatypus turtle, double speedIn) {
            super((PathfinderMob)turtle, speedIn, 16);
            this.turtle = turtle;
        }

        public void m_8041_() {
            this.digTime = 0;
        }

        public boolean m_8036_() {
            return this.turtle.hasEgg() && super.m_8036_();
        }

        public boolean m_8045_() {
            return super.m_8045_() && this.turtle.hasEgg();
        }

        public double m_8052_() {
            return (double)this.turtle.m_20205_() + 0.5;
        }

        public void m_8037_() {
            super.m_8037_();
            if (!this.turtle.m_20069_() && this.m_25625_()) {
                BlockPos blockpos = this.turtle.m_20183_();
                Level world = this.turtle.m_9236_();
                this.turtle.m_146850_(GameEvent.f_157797_);
                world.m_5594_(null, blockpos, SoundEvents.f_12486_, SoundSource.BLOCKS, 0.3f, 0.9f + world.f_46441_.m_188501_() * 0.2f);
                world.m_7731_(this.f_25602_.m_7494_(), (BlockState)((Block)AMBlockRegistry.PLATYPUS_EGG.get()).m_49966_().m_61124_((Property)BlockReptileEgg.EGGS, (Comparable)Integer.valueOf(this.turtle.f_19796_.m_188503_(3) + 1)), 3);
                this.turtle.setHasEgg(false);
                this.turtle.setDigging(false);
                this.turtle.m_27601_(600);
            }
        }

        protected boolean m_6465_(LevelReader worldIn, BlockPos pos) {
            return worldIn.m_46859_(pos.m_7494_()) && BlockReptileEgg.isProperHabitat((BlockGetter)worldIn, pos);
        }
    }
}

