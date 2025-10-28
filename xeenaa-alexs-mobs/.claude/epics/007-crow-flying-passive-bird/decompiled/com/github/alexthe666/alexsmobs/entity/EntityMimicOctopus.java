/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
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
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
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
 *  net.minecraft.world.entity.ai.util.DefaultRandomPos
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.Bucketable
 *  net.minecraft.world.entity.animal.Pufferfish
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.Guardian
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Ingredient$TagValue
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Explosion
 *  net.minecraft.world.level.Explosion$BlockInteraction
 *  net.minecraft.world.level.ExplosionDamageCalculator
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.IFollower;
import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwnerWater;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityMimicOctopus
extends TamableAnimal
implements ISemiAquatic,
IFollower,
Bucketable {
    private static final EntityDataAccessor<Boolean> STOP_CHANGE = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> UPGRADED = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> MIMIC_ORDINAL = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> PREV_MIMIC_ORDINAL = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> MOISTNESS = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Optional<BlockState>> MIMICKED_BLOCK = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_268618_);
    private static final EntityDataAccessor<Optional<BlockState>> PREV_MIMICKED_BLOCK = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_268618_);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> LAST_SCARED_MOB_ID = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> UPGRADED_LASER_ENTITY_ID = SynchedEntityData.m_135353_(EntityMimicOctopus.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    public MimicState localMimicState = MimicState.OVERLAY;
    public float transProgress = 0.0f;
    public float prevTransProgress = 0.0f;
    public float colorShiftProgress = 0.0f;
    public float prevColorShiftProgress = 0.0f;
    public float groundProgress = 5.0f;
    public float prevGroundProgress = 0.0f;
    public float sitProgress = 0.0f;
    public float prevSitProgress = 0.0f;
    private boolean isLandNavigator;
    private int moistureAttackTime = 0;
    private int camoCooldown = 120 + this.f_19796_.m_188503_(1200);
    private int mimicCooldown = 0;
    private int stopMimicCooldown = -1;
    private int fishFeedings;
    private int mimicreamFeedings;
    private int exclaimTime = 0;
    private BlockState localMimic;
    private LivingEntity laserTargetEntity;
    private int guardianLaserTime;

    protected EntityMimicOctopus(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0f);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0f);
        this.switchNavigator(false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 16.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, (double)0.2f);
    }

    public static boolean canMimicOctopusSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockPos downPos = pos;
        while (downPos.m_123342_() > 1 && !worldIn.m_6425_(downPos).m_76178_()) {
            downPos = downPos.m_7495_();
        }
        boolean spawnBlock = worldIn.m_8055_(downPos).m_204336_(AMTagRegistry.MIMIC_OCTOPUS_SPAWNS);
        return spawnBlock && downPos.m_123342_() < worldIn.m_5736_() + 1;
    }

    public static MimicState getStateForItem(ItemStack stack) {
        if (stack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_CREEPER_ITEMS)) {
            return MimicState.CREEPER;
        }
        if (stack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_GUARDIAN_ITEMS)) {
            return MimicState.GUARDIAN;
        }
        if (stack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_PUFFERFISH_ITEMS)) {
            return MimicState.PUFFERFISH;
        }
        return null;
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.MIMIC_OCTOPUS_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.MIMIC_OCTOPUS_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.MIMIC_OCTOPUS_HURT.get();
    }

    public boolean m_6914_(LevelReader worldIn) {
        return worldIn.m_45784_((Entity)this);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mimicOctopusSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.f_19804_.m_135381_(PREV_MIMIC_ORDINAL, (Object)0);
        this.setMimickedBlock(null);
        this.setMimicState(MimicState.OVERLAY);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.f_19804_.m_135381_(MIMIC_ORDINAL, (Object)compound.m_128451_("MimicState"));
        this.setUpgraded(compound.m_128471_("Upgraded"));
        this.m_21839_(compound.m_128471_("Sitting"));
        this.setStopChange(compound.m_128471_("StopChange"));
        this.setCommand(compound.m_128451_("OctoCommand"));
        this.setMoistness(compound.m_128451_("Moistness"));
        this.m_27497_(compound.m_128471_("FromBucket"));
        BlockState blockstate = null;
        if (compound.m_128425_("MimickedBlockState", 10) && (blockstate = NbtUtils.m_247651_((HolderGetter)this.m_9236_().m_246945_(Registries.f_256747_), (CompoundTag)compound.m_128469_("MimickedBlockState"))).m_60795_()) {
            blockstate = null;
        }
        this.setMimickedBlock(blockstate);
        this.camoCooldown = compound.m_128451_("CamoCooldown");
        this.mimicCooldown = compound.m_128451_("MimicCooldown");
        this.stopMimicCooldown = compound.m_128451_("StopMimicCooldown");
        this.fishFeedings = compound.m_128451_("FishFeedings");
        this.mimicreamFeedings = compound.m_128451_("MimicreamFeedings");
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128405_("MimicState", this.getMimicState().ordinal());
        compound.m_128379_("Upgraded", this.isUpgraded());
        compound.m_128379_("Sitting", this.isSitting());
        compound.m_128405_("OctoCommand", this.getCommand());
        compound.m_128405_("Moistness", this.getMoistness());
        compound.m_128379_("FromBucket", this.m_27487_());
        compound.m_128379_("StopChange", this.isStopChange());
        BlockState blockstate = this.getMimickedBlock();
        if (blockstate != null) {
            compound.m_128365_("MimickedBlockState", (Tag)NbtUtils.m_129202_((BlockState)blockstate));
        }
        compound.m_128405_("CamoCooldown", this.camoCooldown);
        compound.m_128405_("MimicCooldown", this.mimicCooldown);
        compound.m_128405_("StopMimicCooldown", this.stopMimicCooldown);
        compound.m_128405_("FishFeedings", this.fishFeedings);
        compound.m_128405_("MimicreamFeedings", this.mimicreamFeedings);
    }

    @Nonnull
    public ItemStack m_28282_() {
        ItemStack stack = new ItemStack((ItemLike)AMItemRegistry.MIMIC_OCTOPUS_BUCKET.get());
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
        compound.m_128365_("MimicOctopusData", (Tag)platTag);
    }

    public void m_142278_(@Nonnull CompoundTag compound) {
        if (compound.m_128441_("MimicOctopusData")) {
            this.m_7378_(compound.m_128469_("MimicOctopusData"));
        }
        this.setMoistness(60000);
    }

    protected float m_6118_() {
        return super.m_6118_() * (this.m_20072_() ? 1.3f : 1.0f);
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
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

    public boolean m_6063_() {
        return false;
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new AIAttack());
        this.f_21345_.m_25352_(1, (Goal)new SitWhenOrderedToGoal((TamableAnimal)this));
        this.f_21345_.m_25352_(2, (Goal)new TameableAIFollowOwnerWater(this, 1.3, 4.0f, 2.0f, false));
        this.f_21345_.m_25352_(3, (Goal)new AnimalAIFindWater((PathfinderMob)this));
        this.f_21345_.m_25352_(3, (Goal)new AnimalAILeaveWater((PathfinderMob)this));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_43938_(Stream.of(new Ingredient.TagValue(AMTagRegistry.MIMIC_OCTOPUS_BREEDABLES), new Ingredient.TagValue(AMTagRegistry.MIMIC_OCTOPUS_TAMEABLES))), false){

            public void m_8037_() {
                EntityMimicOctopus.this.setMimickedBlock(null);
                super.m_8037_();
                EntityMimicOctopus.this.camoCooldown = 40;
                EntityMimicOctopus.this.stopMimicCooldown = 40;
            }
        });
        this.f_21345_.m_25352_(5, (Goal)new AIFlee());
        this.f_21345_.m_25352_(7, (Goal)new BreedGoal((Animal)this, 0.8));
        this.f_21345_.m_25352_(8, (Goal)new AIMimicNearbyMobs());
        this.f_21345_.m_25352_(9, (Goal)new BreedGoal((Animal)this, 0.8));
        this.f_21345_.m_25352_(10, (Goal)new AISwim());
        this.f_21345_.m_25352_(11, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21345_.m_25352_(11, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21346_.m_25352_(1, (Goal)new OwnerHurtByTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(2, (Goal)new OwnerHurtTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(3, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]){

            public boolean m_8036_() {
                return EntityMimicOctopus.this.m_21824_() && super.m_8036_();
            }
        });
    }

    public boolean m_6898_(ItemStack stack) {
        Item item = stack.m_41720_();
        return this.m_21824_() && stack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_BREEDABLES);
    }

    public boolean isActiveCamo() {
        return this.getMimicState() == MimicState.OVERLAY && this.getMimickedBlock() != null;
    }

    public double m_20968_(@Nullable Entity lookingEntity) {
        if (this.isActiveCamo()) {
            return super.m_20968_(lookingEntity) * (double)0.1f;
        }
        return super.m_20968_(lookingEntity);
    }

    @Nonnull
    public InteractionResult m_6071_(@Nonnull Player player, @Nonnull InteractionHand hand) {
        InteractionResult interactionresult;
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        MimicState readState = EntityMimicOctopus.getStateForItem(itemstack);
        InteractionResult type = super.m_6071_(player, hand);
        if (readState != null && this.m_21824_()) {
            if (this.mimicCooldown == 0) {
                this.setMimicState(readState);
                this.mimicCooldown = 20;
                this.camoCooldown = this.stopMimicCooldown = this.isUpgraded() ? 120 : 1200;
                this.setMimickedBlock(null);
            }
            return InteractionResult.SUCCESS;
        }
        boolean tame = this.m_21824_();
        if (tame && itemstack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_TOGGLES_MIMIC)) {
            this.setStopChange(!this.isStopChange());
            if (this.isStopChange()) {
                this.makeEatingParticles(itemstack);
            } else {
                this.m_9236_().m_7605_((Entity)this, (byte)6);
                this.mimicEnvironment();
            }
            return InteractionResult.SUCCESS;
        }
        if (!tame && itemstack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_TAMEABLES)) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.f_157806_);
            this.m_5496_(SoundEvents.f_11803_, this.m_6121_(), this.m_6100_());
            ++this.fishFeedings;
            if (this.getMimicState() == MimicState.OVERLAY && this.getMimickedBlock() == null) {
                if (this.fishFeedings > 5 && this.m_217043_().m_188503_(2) == 0 || this.fishFeedings > 8) {
                    this.m_21828_(player);
                    this.m_9236_().m_7605_((Entity)this, (byte)7);
                } else {
                    this.m_9236_().m_7605_((Entity)this, (byte)6);
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (tame && itemstack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_TAMEABLES)) {
            if (this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_11803_, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0f);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        if (tame) {
            Optional result = Bucketable.m_148828_((Player)player, (InteractionHand)hand, (LivingEntity)this);
            if (result.isPresent()) {
                return (InteractionResult)result.get();
            }
            if (itemstack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_MOISTURIZES) && this.getMoistness() < 24000) {
                this.setMoistness(48000);
                this.makeEatingParticles(itemstack);
                this.m_142075_(player, hand, itemstack);
                return InteractionResult.SUCCESS;
            }
            if (!this.isUpgraded() && itemstack.m_204117_(AMTagRegistry.MIMIC_OCTOPUS_ATTACK_FOODS)) {
                ++this.mimicreamFeedings;
                if (this.mimicreamFeedings > 5 || this.mimicreamFeedings > 2 && this.f_19796_.m_188503_(2) == 0) {
                    this.m_9236_().m_7605_((Entity)this, (byte)46);
                    this.setUpgraded(true);
                    this.setMimicState(MimicState.MIMICUBE);
                    this.setStopChange(false);
                    this.setMimickedBlock(null);
                    this.stopMimicCooldown = 40;
                }
                this.makeEatingParticles(itemstack);
                this.m_142075_(player, hand, itemstack);
                return InteractionResult.SUCCESS;
            }
        }
        if ((interactionresult = itemstack.m_41647_(player, (LivingEntity)this, hand)) != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_((LivingEntity)player)) {
            if (player.m_6144_()) {
                if (this.m_21205_().m_41619_()) {
                    ItemStack cop = itemstack.m_41777_();
                    cop.m_41764_(1);
                    this.m_21008_(InteractionHand.MAIN_HAND, cop);
                    itemstack.m_41774_(1);
                    return InteractionResult.SUCCESS;
                }
                this.m_19983_(this.m_21205_().m_41777_());
                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.f_41583_);
                return InteractionResult.SUCCESS;
            }
            if (!this.m_6898_(itemstack)) {
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
        }
        return type;
    }

    public int getCommand() {
        return (Integer)this.f_19804_.m_135370_(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.m_135381_(COMMAND, (Object)command);
    }

    private void makeEatingParticles(ItemStack item) {
        for (int i = 0; i < 6 + this.f_19796_.m_188503_(3); ++i) {
            double d2 = this.f_19796_.m_188583_() * 0.02;
            double d0 = this.f_19796_.m_188583_() * 0.02;
            double d1 = this.f_19796_.m_188583_() * 0.02;
            this.m_9236_().m_7106_((ParticleOptions)new ItemParticleOption(ParticleTypes.f_123752_, item), this.m_20185_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, this.m_20186_() + (double)(this.m_20206_() * 0.5f) + (double)(this.f_19796_.m_188501_() * this.m_20206_() * 0.5f), this.m_20189_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, d0, d1, d2);
        }
    }

    public void m_267651_(boolean flying) {
        float f1 = (float)Mth.m_184648_((double)(this.m_20185_() - this.f_19854_), (double)(this.m_20186_() - this.f_19855_), (double)(this.m_20189_() - this.f_19856_));
        float f2 = Math.min(f1 * (this.groundProgress < 2.5f ? 4.0f : 8.0f), 1.0f);
        this.f_267362_.m_267566_(f2, 0.4f);
    }

    public boolean m_6040_() {
        return true;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = new GroundPathNavigatorWide((Mob)this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink((PathfinderMob)this, 1.3f, 1.0f);
            this.f_21344_ = new SemiAquaticPathNavigator((Mob)this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    public void m_8119_() {
        double d1;
        double d0;
        super.m_8119_();
        if (this.localMimic != this.getPrevMimickedBlock()) {
            this.localMimic = this.getPrevMimickedBlock();
            this.colorShiftProgress = 0.0f;
        }
        if (this.localMimicState != this.getPrevMimicState()) {
            this.localMimicState = this.getPrevMimicState();
            this.transProgress = 0.0f;
        }
        if (this.m_20069_()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else if (!this.isLandNavigator) {
            this.switchNavigator(true);
        }
        BlockPos pos = AMBlockPos.fromCoords(this.m_20185_(), this.m_20188_() - 1.0, this.m_20189_());
        boolean ground = this.m_9236_().m_8055_(pos).m_60783_((BlockGetter)this.m_9236_(), pos, Direction.UP) && this.getMimicState() != MimicState.GUARDIAN || !this.m_20072_() || this.isSitting();
        this.prevTransProgress = this.transProgress;
        this.prevColorShiftProgress = this.colorShiftProgress;
        this.prevGroundProgress = this.groundProgress;
        this.prevSitProgress = this.sitProgress;
        if (this.getPrevMimicState() != this.getMimicState() && this.transProgress < 5.0f) {
            this.transProgress += 0.25f;
        }
        if (this.getPrevMimicState() == this.getMimicState() && this.transProgress > 0.0f) {
            this.transProgress -= 0.25f;
        }
        if (this.getPrevMimickedBlock() != this.getMimickedBlock() && this.colorShiftProgress < 5.0f) {
            this.colorShiftProgress += 0.25f;
        }
        if (this.getPrevMimickedBlock() == this.getMimickedBlock() && this.colorShiftProgress > 0.0f) {
            this.colorShiftProgress -= 0.25f;
        }
        if (ground && this.groundProgress < 5.0f) {
            this.groundProgress += 0.5f;
        }
        if (!ground && this.groundProgress > 0.0f) {
            this.groundProgress -= 0.5f;
        }
        if (this.isSitting() && this.sitProgress < 5.0f) {
            this.sitProgress += 0.5f;
        }
        if (!this.isSitting() && this.sitProgress > 0.0f) {
            this.sitProgress -= 0.5f;
        }
        if (this.m_20072_()) {
            float f2 = (float)(-((double)((float)this.m_20184_().f_82480_ * 3.0f) * 57.2957763671875));
            this.m_146926_(f2);
        }
        if (this.camoCooldown > 0) {
            --this.camoCooldown;
        }
        if (this.mimicCooldown > 0) {
            --this.mimicCooldown;
        }
        if (this.stopMimicCooldown > 0) {
            --this.stopMimicCooldown;
        }
        if (this.m_21525_()) {
            this.m_20301_(this.m_6062_());
        } else if (this.m_20071_() || this.m_21205_().m_41720_() == Items.f_42447_) {
            this.setMoistness(60000);
        } else {
            this.setMoistness(this.getMoistness() - 1);
            if (this.getMoistness() <= 0 && this.moistureAttackTime-- <= 0) {
                this.m_21839_(false);
                this.m_6469_(this.m_269291_().m_269483_(), this.f_19796_.m_188503_(2) == 0 ? 1.0f : 0.0f);
                this.moistureAttackTime = 20;
            }
        }
        if (this.camoCooldown <= 0 && this.f_19796_.m_188503_(300) == 0) {
            this.mimicEnvironment();
            this.camoCooldown = this.m_217043_().m_188503_(2200) + 200;
        }
        if (!(this.getMimicState() == MimicState.OVERLAY && this.getMimickedBlock() == null || this.stopMimicCooldown != 0 || this.isStopChange())) {
            this.setMimicState(MimicState.OVERLAY);
            this.setMimickedBlock(null);
            this.stopMimicCooldown = -1;
        }
        if (this.m_9236_().f_46443_ && this.exclaimTime > 0) {
            Entity e;
            --this.exclaimTime;
            if (this.exclaimTime == 0 && (e = this.m_9236_().m_6815_(((Integer)this.f_19804_.m_135370_(LAST_SCARED_MOB_ID)).intValue())) != null && this.transProgress >= 5.0f) {
                double d2 = this.f_19796_.m_188583_() * 0.1;
                d0 = this.f_19796_.m_188583_() * 0.1;
                d1 = this.f_19796_.m_188583_() * 0.1;
                this.m_9236_().m_7106_((ParticleOptions)AMParticleRegistry.SHOCKED.get(), e.m_20185_(), e.m_20188_() + (double)(e.m_20206_() * 0.15f) + (double)(this.f_19796_.m_188501_() * e.m_20206_() * 0.15f), e.m_20189_(), d0, d1, d2);
            }
        }
        if (this.hasGuardianLaser()) {
            LivingEntity livingentity;
            if (this.guardianLaserTime < 30) {
                ++this.guardianLaserTime;
            }
            if ((livingentity = this.getGuardianLaser()) != null && this.m_20072_()) {
                this.m_21563_().m_24960_((Entity)livingentity, 90.0f, 90.0f);
                this.m_21563_().m_8128_();
                double d5 = this.getLaserAttackAnimationScale(0.0f);
                d0 = livingentity.m_20185_() - this.m_20185_();
                d1 = livingentity.m_20227_(0.5) - this.m_20188_();
                double d2 = livingentity.m_20189_() - this.m_20189_();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d0 /= d3;
                d1 /= d3;
                d2 /= d3;
                double d4 = this.f_19796_.m_188500_();
                while (d4 < d3) {
                    this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123795_, this.m_20185_() + d0 * (d4 += 1.8 - d5 + this.f_19796_.m_188500_() * (1.7 - d5)), this.m_20188_() + d1 * d4, this.m_20189_() + d2 * d4, 0.0, 0.0, 0.0);
                }
                if (this.guardianLaserTime == 30) {
                    livingentity.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), 5.0f);
                    this.guardianLaserTime = 0;
                    this.f_19804_.m_135381_(UPGRADED_LASER_ENTITY_ID, (Object)-1);
                }
            }
        }
        if (!this.m_9236_().f_46443_ && this.f_19797_ % 40 == 0) {
            this.m_5634_(2.0f);
        }
    }

    public float getLaserAttackAnimationScale(float p_175477_1_) {
        return ((float)this.guardianLaserTime + p_175477_1_) / 30.0f;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 68) {
            if (this.exclaimTime == 0) {
                this.exclaimTime = 20;
            }
        } else if (id == 69) {
            this.creeperExplode();
        } else {
            super.m_7822_(id);
        }
    }

    public void mimicEnvironment() {
        if (!this.isStopChange()) {
            BlockPos down = this.getPositionDown();
            if (!this.m_9236_().m_46859_(down)) {
                this.setMimicState(MimicState.OVERLAY);
                this.setMimickedBlock(this.m_9236_().m_8055_(down));
            }
            this.stopMimicCooldown = this.m_217043_().m_188503_(2200);
        }
    }

    public int getMoistness() {
        return (Integer)this.f_19804_.m_135370_(MOISTNESS);
    }

    public void setMoistness(int p_211137_1_) {
        this.f_19804_.m_135381_(MOISTNESS, (Object)p_211137_1_);
    }

    private BlockPos getPositionDown() {
        BlockPos pos = AMBlockPos.fromCoords(this.m_20185_(), this.m_20188_(), this.m_20189_());
        while (pos.m_123342_() > 1 && (this.m_9236_().m_46859_(pos) || this.m_9236_().m_46801_(pos))) {
            pos = pos.m_7495_();
        }
        return pos;
    }

    public void m_7023_(Vec3 travelVector) {
        if (this.isSitting()) {
            if (this.m_21573_().m_26570_() != null) {
                this.m_21573_().m_26573_();
            }
            travelVector = Vec3.f_82478_;
            super.m_7023_(travelVector);
            return;
        }
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().m_82490_(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    public boolean isSitting() {
        return (Boolean)this.f_19804_.m_135370_(SITTING);
    }

    public void m_21839_(boolean sit) {
        this.f_19804_.m_135381_(SITTING, (Object)sit);
    }

    public boolean m_27487_() {
        return (Boolean)this.f_19804_.m_135370_(FROM_BUCKET);
    }

    public void m_27497_(boolean sit) {
        this.f_19804_.m_135381_(FROM_BUCKET, (Object)sit);
    }

    @Nonnull
    public SoundEvent m_142623_() {
        return SoundEvents.f_11782_;
    }

    public boolean isUpgraded() {
        return (Boolean)this.f_19804_.m_135370_(FROM_BUCKET);
    }

    public void setUpgraded(boolean sit) {
        this.f_19804_.m_135381_(FROM_BUCKET, (Object)sit);
    }

    public boolean isStopChange() {
        return (Boolean)this.f_19804_.m_135370_(STOP_CHANGE);
    }

    public void setStopChange(boolean sit) {
        this.f_19804_.m_135381_(STOP_CHANGE, (Object)sit);
    }

    public boolean hasGuardianLaser() {
        return (Integer)this.f_19804_.m_135370_(UPGRADED_LASER_ENTITY_ID) != -1 && this.isUpgraded() && this.m_20072_();
    }

    @Nullable
    public LivingEntity getGuardianLaser() {
        if (!this.hasGuardianLaser()) {
            return null;
        }
        if (this.m_9236_().f_46443_) {
            if (this.laserTargetEntity != null) {
                return this.laserTargetEntity;
            }
            Entity lvt_1_1_ = this.m_9236_().m_6815_(((Integer)this.f_19804_.m_135370_(UPGRADED_LASER_ENTITY_ID)).intValue());
            if (lvt_1_1_ instanceof LivingEntity) {
                this.laserTargetEntity = (LivingEntity)lvt_1_1_;
                return this.laserTargetEntity;
            }
            return null;
        }
        return this.m_5448_();
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return (AgeableMob)((EntityType)AMEntityRegistry.MIMIC_OCTOPUS.get()).m_20615_((Level)serverWorld);
    }

    public boolean m_8023_() {
        return super.m_8023_() || this.m_27487_() || this.m_21824_();
    }

    public boolean m_6785_(double distanceToClosestPlayer) {
        return !this.m_21824_() && !this.m_27487_();
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(MIMIC_ORDINAL, (Object)0);
        this.f_19804_.m_135372_(PREV_MIMIC_ORDINAL, (Object)-1);
        this.f_19804_.m_135372_(MOISTNESS, (Object)60000);
        this.f_19804_.m_135372_(MIMICKED_BLOCK, Optional.empty());
        this.f_19804_.m_135372_(PREV_MIMICKED_BLOCK, Optional.empty());
        this.f_19804_.m_135372_(SITTING, (Object)false);
        this.f_19804_.m_135372_(COMMAND, (Object)0);
        this.f_19804_.m_135372_(LAST_SCARED_MOB_ID, (Object)-1);
        this.f_19804_.m_135372_(FROM_BUCKET, (Object)false);
        this.f_19804_.m_135372_(UPGRADED, (Object)false);
        this.f_19804_.m_135372_(STOP_CHANGE, (Object)false);
        this.f_19804_.m_135372_(UPGRADED_LASER_ENTITY_ID, (Object)-1);
    }

    public MimicState getMimicState() {
        return MimicState.values()[Mth.m_14045_((int)((Integer)this.f_19804_.m_135370_(MIMIC_ORDINAL)), (int)0, (int)4)];
    }

    public void setMimicState(MimicState state) {
        if (this.getMimicState() != state) {
            this.f_19804_.m_135381_(PREV_MIMIC_ORDINAL, (Object)((Integer)this.f_19804_.m_135370_(MIMIC_ORDINAL)));
        }
        this.f_19804_.m_135381_(MIMIC_ORDINAL, (Object)state.ordinal());
    }

    public MimicState getPrevMimicState() {
        if ((Integer)this.f_19804_.m_135370_(PREV_MIMIC_ORDINAL) == -1) {
            return null;
        }
        return MimicState.values()[Mth.m_14045_((int)((Integer)this.f_19804_.m_135370_(PREV_MIMIC_ORDINAL)), (int)0, (int)4)];
    }

    @Nullable
    public BlockState getMimickedBlock() {
        return ((Optional)this.f_19804_.m_135370_(MIMICKED_BLOCK)).orElse(null);
    }

    public void setMimickedBlock(@Nullable BlockState state) {
        if (this.getMimickedBlock() != state) {
            this.f_19804_.m_135381_(PREV_MIMICKED_BLOCK, Optional.ofNullable(this.getMimickedBlock()));
        }
        this.f_19804_.m_135381_(MIMICKED_BLOCK, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getPrevMimickedBlock() {
        return ((Optional)this.f_19804_.m_135370_(PREV_MIMICKED_BLOCK)).orElse(null);
    }

    protected void updateAir(int p_209207_1_) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(p_209207_1_ - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().m_269483_(), 2.0f);
            }
        } else {
            this.m_20301_(1200);
        }
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.isSitting() && (this.m_5448_() == null || this.m_5448_().m_20072_());
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.m_5448_() != null && !this.m_5448_().m_20072_();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isSitting();
    }

    @Override
    public int getWaterSearchRange() {
        return 16;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this)).m_6662_() != HitResult.Type.MISS;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = -9.45f - (float)this.m_217043_().m_188503_(24) - radiusAdd;
        float neg = this.m_217043_().m_188499_() ? 1.0f : -1.0f;
        float renderYawOffset = this.f_20883_;
        float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.m_217043_().m_188501_() * neg;
        double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
        double extraZ = radius * Mth.m_14089_((float)angle);
        BlockPos radialPos = new BlockPos((int)(fleePos.m_7096_() + extraX), 0, (int)(fleePos.m_7094_() + extraZ));
        BlockPos ground = this.getOctopusGround(radialPos);
        return ground != null ? Vec3.m_82512_((Vec3i)ground) : null;
    }

    private BlockPos getOctopusGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int)this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > 2 && this.m_9236_().m_6425_(position).m_205070_(FluidTags.f_13131_)) {
            position = position.m_7495_();
        }
        return position;
    }

    public void m_7350_(EntityDataAccessor<?> key) {
        super.m_7350_(key);
        if (UPGRADED_LASER_ENTITY_ID.equals(key)) {
            this.guardianLaserTime = 0;
            this.laserTargetEntity = null;
        }
    }

    private void creeperExplode() {
        Explosion explosion = new Explosion(this.m_9236_(), (Entity)this, this.m_269291_().m_269333_((LivingEntity)this), (ExplosionDamageCalculator)null, this.m_20185_(), this.m_20186_(), this.m_20189_(), 1.0f + this.f_19796_.m_188501_(), false, Explosion.BlockInteraction.KEEP);
        explosion.m_46061_();
        explosion.m_46075_(true);
    }

    public static enum MimicState {
        OVERLAY,
        CREEPER,
        GUARDIAN,
        PUFFERFISH,
        MIMICUBE;

    }

    private class AIAttack
    extends Goal {
        private int executionCooldown = 0;
        private int scareMobTime = 0;
        private Vec3 fleePosition = null;

        public AIAttack() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            if (this.executionCooldown > 0) {
                EntityMimicOctopus.this.f_19804_.m_135381_(UPGRADED_LASER_ENTITY_ID, (Object)-1);
                --this.executionCooldown;
            }
            if (EntityMimicOctopus.this.isStopChange() && EntityMimicOctopus.this.getMimicState() == MimicState.OVERLAY) {
                return false;
            }
            return this.executionCooldown == 0 && EntityMimicOctopus.this.m_21824_() && EntityMimicOctopus.this.m_5448_() != null && EntityMimicOctopus.this.m_5448_().m_6084_();
        }

        public void m_8041_() {
            this.fleePosition = null;
            this.scareMobTime = 0;
            this.executionCooldown = 100 + EntityMimicOctopus.this.f_19796_.m_188503_(200);
            if (EntityMimicOctopus.this.isUpgraded()) {
                this.executionCooldown = 30;
            } else {
                EntityMimicOctopus.this.m_6703_(null);
                EntityMimicOctopus.this.m_6710_(null);
            }
            if (EntityMimicOctopus.this.stopMimicCooldown <= 0) {
                EntityMimicOctopus.this.mimicEnvironment();
            }
            EntityMimicOctopus.this.f_19804_.m_135381_(UPGRADED_LASER_ENTITY_ID, (Object)-1);
        }

        public Vec3 generateFleePosition(LivingEntity fleer) {
            for (int i = 0; i < 15; ++i) {
                BlockPos pos = fleer.m_20183_().m_7918_(EntityMimicOctopus.this.f_19796_.m_188503_(32) - 16, EntityMimicOctopus.this.f_19796_.m_188503_(16), EntityMimicOctopus.this.f_19796_.m_188503_(32) - 16);
                while (fleer.m_9236_().m_46859_(pos) && pos.m_123342_() > 1) {
                    pos = pos.m_7495_();
                }
                if (fleer instanceof PathfinderMob) {
                    if (!(((PathfinderMob)fleer).m_21692_(pos) >= 0.0f)) continue;
                    return Vec3.m_82512_((Vec3i)pos);
                }
                return Vec3.m_82512_((Vec3i)pos);
            }
            return null;
        }

        public void m_8037_() {
            LivingEntity target = EntityMimicOctopus.this.m_5448_();
            if (target != null) {
                if (this.scareMobTime > 0) {
                    if (this.fleePosition == null || target.m_20238_(this.fleePosition) < (double)(target.m_20205_() * target.m_20205_() * 2.0f)) {
                        this.fleePosition = this.generateFleePosition(target);
                    }
                    if (target instanceof Mob && this.fleePosition != null) {
                        ((Mob)target).m_21573_().m_26519_(this.fleePosition.f_82479_, this.fleePosition.f_82480_, this.fleePosition.f_82481_, 1.5);
                        ((Mob)target).m_21566_().m_6849_(this.fleePosition.f_82479_, this.fleePosition.f_82480_, this.fleePosition.f_82481_, 1.5);
                        ((Mob)target).m_6710_(null);
                    }
                    EntityMimicOctopus.this.camoCooldown = Math.max(EntityMimicOctopus.this.camoCooldown, 20);
                    EntityMimicOctopus.this.stopMimicCooldown = Math.max(EntityMimicOctopus.this.stopMimicCooldown, 20);
                    --this.scareMobTime;
                    if (this.scareMobTime == 0) {
                        this.m_8041_();
                        return;
                    }
                }
                double dist = EntityMimicOctopus.this.m_20270_((Entity)target);
                boolean move = true;
                if (dist < 7.0 && EntityMimicOctopus.this.m_142582_((Entity)target) && EntityMimicOctopus.this.getMimicState() == MimicState.GUARDIAN && EntityMimicOctopus.this.isUpgraded()) {
                    EntityMimicOctopus.this.f_19804_.m_135381_(UPGRADED_LASER_ENTITY_ID, (Object)target.m_19879_());
                    move = false;
                }
                if (dist < 3.0) {
                    EntityMimicOctopus.this.f_19804_.m_135381_(LAST_SCARED_MOB_ID, (Object)target.m_19879_());
                    if (move) {
                        move = EntityMimicOctopus.this.isUpgraded() && dist > 2.0;
                    }
                    EntityMimicOctopus.this.m_21573_().m_26573_();
                    if (!EntityMimicOctopus.this.isStopChange()) {
                        EntityMimicOctopus.this.setMimickedBlock(null);
                        MimicState prev = EntityMimicOctopus.this.getMimicState();
                        if (EntityMimicOctopus.this.m_20072_()) {
                            if (prev != MimicState.GUARDIAN && prev != MimicState.PUFFERFISH) {
                                if (EntityMimicOctopus.this.f_19796_.m_188499_()) {
                                    EntityMimicOctopus.this.setMimicState(MimicState.GUARDIAN);
                                } else {
                                    EntityMimicOctopus.this.setMimicState(MimicState.PUFFERFISH);
                                }
                            }
                        } else {
                            EntityMimicOctopus.this.setMimicState(MimicState.CREEPER);
                        }
                    }
                    if (EntityMimicOctopus.this.getMimicState() != MimicState.OVERLAY) {
                        EntityMimicOctopus.this.mimicCooldown = 40;
                        EntityMimicOctopus.this.stopMimicCooldown = Math.max(EntityMimicOctopus.this.stopMimicCooldown, 60);
                    }
                    if (EntityMimicOctopus.this.isUpgraded() && EntityMimicOctopus.this.transProgress >= 5.0f) {
                        if (EntityMimicOctopus.this.getMimicState() == MimicState.PUFFERFISH && EntityMimicOctopus.this.m_20191_().m_82363_(2.0, 1.3, 2.0).m_82381_(target.m_20191_())) {
                            target.m_6469_(EntityMimicOctopus.this.m_269291_().m_269333_((LivingEntity)EntityMimicOctopus.this), 4.0f);
                            target.m_7292_(new MobEffectInstance(MobEffects.f_19614_, 400, 2));
                        }
                        if (EntityMimicOctopus.this.getMimicState() == MimicState.GUARDIAN) {
                            if (EntityMimicOctopus.this.m_20191_().m_82363_(1.0, 1.0, 1.0).m_82381_(target.m_20191_())) {
                                target.m_6469_(EntityMimicOctopus.this.m_269291_().m_269333_((LivingEntity)EntityMimicOctopus.this), 1.0f);
                            }
                            EntityMimicOctopus.this.f_19804_.m_135381_(UPGRADED_LASER_ENTITY_ID, (Object)target.m_19879_());
                        }
                        if (EntityMimicOctopus.this.getMimicState() == MimicState.CREEPER) {
                            EntityMimicOctopus.this.creeperExplode();
                            EntityMimicOctopus.this.m_9236_().m_7605_((Entity)EntityMimicOctopus.this, (byte)69);
                            this.executionCooldown = 300;
                        }
                    }
                    if (this.scareMobTime == 0) {
                        EntityMimicOctopus.this.m_9236_().m_7605_((Entity)EntityMimicOctopus.this, (byte)68);
                        this.scareMobTime = 60 + EntityMimicOctopus.this.f_19796_.m_188503_(60);
                    }
                }
                if (move) {
                    EntityMimicOctopus.this.m_21391_((Entity)target, 30.0f, 30.0f);
                    EntityMimicOctopus.this.m_21573_().m_5624_((Entity)target, (double)1.2f);
                }
            }
        }
    }

    private class AIFlee
    extends Goal {
        protected final EntitySorter theNearestAttackableTargetSorter;
        protected final Predicate<? super Entity> targetEntitySelector;
        protected int executionChance = 8;
        protected boolean mustUpdate;
        private Entity targetEntity;
        private Vec3 flightTarget = null;
        private int cooldown = 0;

        AIFlee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntitySorter((Entity)EntityMimicOctopus.this);
            this.targetEntitySelector = new Predicate<Entity>(){

                public boolean apply(@Nullable Entity e) {
                    return e.m_6084_() && e.m_6095_().m_204039_(AMTagRegistry.MIMIC_OCTOPUS_FEARS) || e instanceof Player && !((Player)e).m_7500_();
                }
            };
        }

        public boolean m_8036_() {
            List list;
            if (EntityMimicOctopus.this.m_20159_() || EntityMimicOctopus.this.m_20160_() || EntityMimicOctopus.this.m_21824_()) {
                return false;
            }
            if (!this.mustUpdate) {
                long worldTime = EntityMimicOctopus.this.m_9236_().m_46467_() % 10L;
                if (EntityMimicOctopus.this.m_21216_() >= 100 && worldTime != 0L) {
                    return false;
                }
                if (EntityMimicOctopus.this.m_217043_().m_188503_(this.executionChance) != 0 && worldTime != 0L) {
                    return false;
                }
            }
            if ((list = EntityMimicOctopus.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector)).isEmpty()) {
                return false;
            }
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.targetEntity = (Entity)list.get(0);
            this.mustUpdate = false;
            return true;
        }

        public boolean m_8045_() {
            return this.targetEntity != null && !EntityMimicOctopus.this.m_21824_() && EntityMimicOctopus.this.m_20270_(this.targetEntity) < 20.0f;
        }

        public void m_8041_() {
            this.flightTarget = null;
            this.targetEntity = null;
            EntityMimicOctopus.this.setMimicState(MimicState.OVERLAY);
            EntityMimicOctopus.this.setMimickedBlock(null);
        }

        public void m_8037_() {
            if (this.cooldown > 0) {
                --this.cooldown;
            }
            if (!EntityMimicOctopus.this.isActiveCamo()) {
                EntityMimicOctopus.this.mimicEnvironment();
            }
            if (this.flightTarget != null) {
                EntityMimicOctopus.this.m_21573_().m_26519_(this.flightTarget.f_82479_, this.flightTarget.f_82480_, this.flightTarget.f_82481_, (double)1.2f);
                if (this.cooldown == 0 && EntityMimicOctopus.this.isTargetBlocked(this.flightTarget)) {
                    this.cooldown = 30;
                    this.flightTarget = null;
                }
            }
            if (this.targetEntity != null) {
                Vec3 vec;
                if ((this.flightTarget == null || this.flightTarget != null && EntityMimicOctopus.this.m_20238_(this.flightTarget) < 6.0) && (vec = DefaultRandomPos.m_148407_((PathfinderMob)EntityMimicOctopus.this, (int)16, (int)7, (Vec3)this.targetEntity.m_20182_())) != null) {
                    this.flightTarget = vec;
                }
                if (EntityMimicOctopus.this.m_20270_(this.targetEntity) > 20.0f) {
                    this.m_8041_();
                }
            }
        }

        protected double getTargetDistance() {
            return 10.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntityMimicOctopus.this.m_20185_(), EntityMimicOctopus.this.m_20186_() + 0.5, EntityMimicOctopus.this.m_20189_());
            AABB aabb = new AABB(-targetDistance, -targetDistance, -targetDistance, targetDistance, targetDistance, targetDistance);
            return aabb.m_82383_(renderCenter);
        }
    }

    private class AIMimicNearbyMobs
    extends Goal {
        protected final EntitySorter theNearestAttackableTargetSorter;
        protected final Predicate<? super Entity> targetEntitySelector;
        protected int executionChance = 30;
        protected boolean mustUpdate;
        private Entity targetEntity;
        private Vec3 flightTarget = null;
        private int cooldown = 0;

        AIMimicNearbyMobs() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntitySorter((Entity)EntityMimicOctopus.this);
            this.targetEntitySelector = new Predicate<Entity>(){

                public boolean apply(@Nullable Entity e) {
                    return e.m_6084_() && (e instanceof Creeper || e instanceof Guardian || e instanceof Pufferfish);
                }
            };
        }

        public boolean m_8036_() {
            List list;
            if (EntityMimicOctopus.this.m_20159_() || EntityMimicOctopus.this.m_20160_() || EntityMimicOctopus.this.getMimicState() != MimicState.OVERLAY || EntityMimicOctopus.this.mimicCooldown > 0) {
                return false;
            }
            if (!this.mustUpdate) {
                long worldTime = EntityMimicOctopus.this.m_9236_().m_46467_() % 10L;
                if (EntityMimicOctopus.this.m_21216_() >= 100 && worldTime != 0L) {
                    return false;
                }
                if (EntityMimicOctopus.this.m_217043_().m_188503_(this.executionChance) != 0 && worldTime != 0L) {
                    return false;
                }
            }
            if ((list = EntityMimicOctopus.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector)).isEmpty()) {
                return false;
            }
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.targetEntity = (Entity)list.get(0);
            this.mustUpdate = false;
            return true;
        }

        public boolean m_8045_() {
            return this.targetEntity != null && EntityMimicOctopus.this.m_20270_(this.targetEntity) < 10.0f && EntityMimicOctopus.this.getMimicState() == MimicState.OVERLAY;
        }

        public void m_8041_() {
            EntityMimicOctopus.this.m_21573_().m_26573_();
            this.flightTarget = null;
            this.targetEntity = null;
        }

        public void m_8037_() {
            if (this.cooldown > 0) {
                --this.cooldown;
            }
            if (this.targetEntity != null) {
                EntityMimicOctopus.this.m_21573_().m_5624_(this.targetEntity, (double)1.2f);
                if (EntityMimicOctopus.this.m_20270_(this.targetEntity) > 20.0f) {
                    this.m_8041_();
                    EntityMimicOctopus.this.setMimicState(MimicState.OVERLAY);
                    EntityMimicOctopus.this.setMimickedBlock(null);
                } else if (EntityMimicOctopus.this.m_20270_(this.targetEntity) < 5.0f && EntityMimicOctopus.this.m_142582_(this.targetEntity)) {
                    int i;
                    EntityMimicOctopus.this.stopMimicCooldown = i = 1200;
                    EntityMimicOctopus.this.camoCooldown = i + 40;
                    EntityMimicOctopus.this.mimicCooldown = 40;
                    if (this.targetEntity instanceof Creeper) {
                        EntityMimicOctopus.this.setMimicState(MimicState.CREEPER);
                    } else if (this.targetEntity instanceof Guardian) {
                        EntityMimicOctopus.this.setMimicState(MimicState.GUARDIAN);
                    } else if (this.targetEntity instanceof Pufferfish) {
                        EntityMimicOctopus.this.setMimicState(MimicState.PUFFERFISH);
                    } else {
                        EntityMimicOctopus.this.setMimicState(MimicState.OVERLAY);
                        EntityMimicOctopus.this.setMimickedBlock(null);
                    }
                    this.m_8041_();
                }
            }
        }

        protected double getTargetDistance() {
            return 10.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntityMimicOctopus.this.m_20185_(), EntityMimicOctopus.this.m_20186_() + 0.5, EntityMimicOctopus.this.m_20189_());
            AABB aabb = new AABB(-targetDistance, -targetDistance, -targetDistance, targetDistance, targetDistance, targetDistance);
            return aabb.m_82383_(renderCenter);
        }
    }

    private class AISwim
    extends SemiAquaticAIRandomSwimming {
        public AISwim() {
            super((Animal)EntityMimicOctopus.this, 1.0, 35);
        }

        @Override
        protected Vec3 findSurfaceTarget(PathfinderMob creature, int i, int i1) {
            if (creature.m_217043_().m_188503_(5) == 0) {
                return super.findSurfaceTarget(creature, i, i1);
            }
            BlockPos downPos = creature.m_20183_();
            while (creature.m_9236_().m_6425_(downPos).m_205070_(FluidTags.f_13131_) || creature.m_9236_().m_6425_(downPos).m_205070_(FluidTags.f_13132_)) {
                downPos = downPos.m_7495_();
            }
            if (EntityMimicOctopus.this.m_9236_().m_8055_(downPos).m_60815_() && EntityMimicOctopus.this.m_9236_().m_8055_(downPos).m_60734_() != Blocks.f_50450_) {
                return new Vec3((double)((float)downPos.m_123341_() + 0.5f), (double)downPos.m_123342_(), (double)((float)downPos.m_123343_() + 0.5f));
            }
            return null;
        }
    }

    public record EntitySorter(Entity theEntity) implements Comparator<Entity>
    {
        @Override
        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.m_20280_(p_compare_1_);
            double d1 = this.theEntity.m_20280_(p_compare_2_);
            return Double.compare(d0, d1);
        }
    }
}

