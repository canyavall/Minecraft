/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.control.MoveControl$Operation
 *  net.minecraft.world.entity.ai.goal.AvoidEntityGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.util.LandRandomPos
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.registries.ForgeRegistries
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityLaviathan;
import com.github.alexthe666.alexsmobs.entity.EntityMosquitoSpit;
import com.github.alexthe666.alexsmobs.entity.EntityMungus;
import com.github.alexthe666.alexsmobs.entity.EntityTriops;
import com.github.alexthe666.alexsmobs.entity.EntityWarpedMosco;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoDismount;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoMountPlayer;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityCrimsonMosquito
extends Monster {
    public static final ResourceLocation FULL_LOOT = new ResourceLocation("alexsmobs", "entities/crimson_mosquito_full");
    public static final ResourceLocation FROM_FLY_LOOT = new ResourceLocation("alexsmobs", "entities/crimson_mosquito_fly");
    public static final ResourceLocation FROM_FLY_FULL_LOOT = new ResourceLocation("alexsmobs", "entities/crimson_mosquito_fly_full");
    protected static final EntityDimensions FLIGHT_SIZE = EntityDimensions.m_20398_((float)1.2f, (float)1.8f);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> BLOOD_LEVEL = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Boolean> SHRINKING = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> FROM_FLY = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Float> MOSQUITO_SCALE = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Boolean> SICK = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> LURING_LAVIATHAN = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> FLEEING_ENTITY = SynchedEntityData.m_135353_(EntityCrimsonMosquito.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final Predicate<LivingEntity> REPELLENT = mob -> mob.m_21023_((MobEffect)AMEffectRegistry.MOSQUITO_REPELLENT.get()) || mob instanceof EntityTriops;
    private static final Predicate<LivingEntity> NO_REPELLENT = mob -> !mob.m_21023_((MobEffect)AMEffectRegistry.MOSQUITO_REPELLENT.get());
    public float prevFlyProgress;
    public float flyProgress;
    public float prevShootProgress;
    public float shootProgress;
    public int shootingTicks;
    public int randomWingFlapTick = 0;
    private int flightTicks = 0;
    private int sickTicks = 0;
    private boolean prevFlying = false;
    private int spitCooldown = 0;
    private int loopSoundTick = 0;
    private int drinkTime = 0;
    public float prevMosquitoScale = 1.0f;
    private int repellentCheckTime = 0;
    private Vec3 fleePos = null;

    protected EntityCrimsonMosquito(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new MoveHelperController(this);
        this.m_21441_(BlockPathTypes.WATER, -1.0f);
        this.m_21441_(BlockPathTypes.LAVA, 0.0f);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0f);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0f);
    }

    public boolean hasLuringLaviathan() {
        return (Integer)this.f_19804_.m_135370_(LURING_LAVIATHAN) != -1;
    }

    public void onSpawnFromFly() {
        this.prevMosquitoScale = 0.2f;
        this.setShrink(false);
        this.setMosquitoScale(0.2f);
        this.setFromFly(true);
        for (int j = 0; j < 4; ++j) {
            this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123811_, this.m_20185_() + this.f_19796_.m_188500_() / 2.0, this.m_20227_(0.5), this.m_20189_() + this.f_19796_.m_188500_() / 2.0, this.f_19796_.m_188500_() * 0.5 + 0.5, 0.0, 0.0);
        }
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.MOSQUITO_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.MOSQUITO_DIE.get();
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.crimsonMosquitoSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 10.0).m_22268_(Attributes.f_22277_, 32.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 5.0).m_22268_(Attributes.f_22279_, 0.25);
    }

    @Nullable
    protected ResourceLocation m_7582_() {
        if (this.getBloodLevel() > 0) {
            return this.isFromFly() ? FROM_FLY_FULL_LOOT : FULL_LOOT;
        }
        return this.isFromFly() ? FROM_FLY_LOOT : super.m_7582_();
    }

    public boolean canRiderInteract() {
        return true;
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(2, (Goal)new FlyTowardsTarget(this));
        this.f_21345_.m_25352_(2, (Goal)new FlyAwayFromTarget(this));
        this.f_21345_.m_25352_(3, (Goal)new RandomFlyGoal(this));
        this.f_21345_.m_25352_(4, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 32.0f));
        this.f_21345_.m_25352_(5, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[]{EntityCrimsonMosquito.class, EntityWarpedMosco.class}));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<Player>((Mob)this, Player.class, 20, true, false, NO_REPELLENT));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<LivingEntity>((Mob)this, LivingEntity.class, 50, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.CRIMSON_MOSQUITO_TARGETS)));
        this.f_21345_.m_25352_(3, (Goal)new AvoidEntityGoal((PathfinderMob)this, EntityTriops.class, 16.0f, 1.3, 1.0));
    }

    public static boolean canMosquitoSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.m_7495_();
        boolean spawnBlock = worldIn.m_8055_(blockpos).m_60815_();
        return reason == MobSpawnType.SPAWNER || spawnBlock && worldIn.m_8055_(blockpos).m_60643_((BlockGetter)worldIn, blockpos, typeIn) && EntityCrimsonMosquito.m_219009_((ServerLevelAccessor)worldIn, (BlockPos)pos, (RandomSource)randomIn) && EntityCrimsonMosquito.m_217057_((EntityType)((EntityType)AMEntityRegistry.CRIMSON_MOSQUITO.get()), (LevelAccessor)worldIn, (MobSpawnType)reason, (BlockPos)pos, (RandomSource)randomIn);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128405_("FlightTicks", this.flightTicks);
        compound.m_128405_("SickTicks", this.sickTicks);
        compound.m_128350_("MosquitoScale", this.getMosquitoScale());
        compound.m_128379_("Flying", this.isFlying());
        compound.m_128379_("Shrinking", this.isShrinking());
        compound.m_128379_("IsFromFly", this.isFromFly());
        compound.m_128379_("Sick", this.isSick());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.flightTicks = compound.m_128451_("FlightTicks");
        this.sickTicks = compound.m_128451_("SickTicks");
        this.setMosquitoScale(compound.m_128457_("MosquitoScale"));
        this.setFlying(compound.m_128471_("Flying"));
        this.setShrink(compound.m_128471_("Shrinking"));
        this.setFromFly(compound.m_128471_("IsFromFly"));
        this.setSick(compound.m_128471_("Sick"));
    }

    private void spit(LivingEntity target) {
        if (this.isSick()) {
            return;
        }
        EntityMosquitoSpit llamaspitentity = new EntityMosquitoSpit(this.m_9236_(), this);
        double d0 = target.m_20185_() - this.m_20185_();
        double d1 = target.m_20227_(0.3333333333333333) - llamaspitentity.m_20186_();
        double d2 = target.m_20189_() - this.m_20189_();
        float f = Mth.m_14116_((float)((float)(d0 * d0 + d2 * d2))) * 0.2f;
        llamaspitentity.shoot(d0, d1 + (double)f, d2, 1.5f, 10.0f);
        if (!this.m_20067_()) {
            this.m_146850_(GameEvent.f_157778_);
            this.m_9236_().m_6263_(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.f_12098_, this.m_5720_(), 1.0f, 1.0f + (this.f_19796_.m_188501_() - this.f_19796_.m_188501_()) * 0.2f);
        }
        if (this.getBloodLevel() > 0) {
            this.setBloodLevel(this.getBloodLevel() - 1);
        }
        this.m_9236_().m_7967_((Entity)llamaspitentity);
    }

    public boolean m_6673_(DamageSource source) {
        return source.m_276093_(DamageTypes.f_268671_) || source.m_276093_(DamageTypes.f_268722_) || source.m_276093_(DamageTypes.f_268612_) || source.m_276093_(DamageTypes.f_268546_) || source.m_269533_(DamageTypeTags.f_268745_) || super.m_6673_(source);
    }

    public boolean m_6469_(DamageSource source, float amount) {
        if (source.m_7639_() != null && this.m_20201_() == source.m_7639_().m_20201_()) {
            return super.m_6469_(source, amount * 0.333f);
        }
        if (this.flightTicks < 0) {
            this.flightTicks = 0;
        }
        return super.m_6469_(source, amount);
    }

    public void m_6083_() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.m_6084_()) {
            this.m_8127_();
        } else {
            Entity mount;
            this.m_20334_(0.0, 0.0, 0.0);
            this.m_8119_();
            if (this.m_20159_() && (mount = this.m_20202_()) instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)mount;
                this.f_20883_ = livingEntity.f_20883_;
                this.m_146922_(livingEntity.m_146908_());
                this.f_20885_ = livingEntity.f_20885_;
                this.f_19859_ = livingEntity.f_20885_;
                float radius = 1.0f;
                float angle = (float)Math.PI / 180 * livingEntity.f_20883_;
                double extraX = 1.0f * Mth.m_14031_((float)((float)Math.PI + angle));
                double extraZ = 1.0f * Mth.m_14089_((float)angle);
                this.m_6034_(mount.m_20185_() + extraX, Math.max(mount.m_20186_() + (double)(mount.m_20192_() * 0.25f), mount.m_20186_()), mount.m_20189_() + extraZ);
                if (!mount.m_6084_() || mount instanceof Player && ((Player)mount).m_7500_()) {
                    this.m_6038_();
                }
                if (!this.m_9236_().f_46443_) {
                    if (this.drinkTime % 20 == 0 && this.m_6084_()) {
                        boolean mungus = AMConfig.warpedMoscoTransformation && mount instanceof EntityMungus && ((EntityMungus)mount).isWarpedMoscoReady();
                        if (mount.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), mungus ? 7.0f : 2.0f)) {
                            boolean sick;
                            if (mungus) {
                                ((EntityMungus)mount).disableExplosion();
                            }
                            if ((sick = this.isNonMungusWarpedTrigger(mount)) || mungus) {
                                if (!this.isSick()) {
                                    for (ServerPlayer serverplayerentity : this.m_9236_().m_45976_(ServerPlayer.class, this.m_20191_().m_82377_(40.0, 25.0, 40.0))) {
                                        AMAdvancementTriggerRegistry.MOSQUITO_SICK.trigger(serverplayerentity);
                                    }
                                }
                                this.setSick(true);
                                this.setFlying(false);
                                this.flightTicks = -150 - this.f_19796_.m_188503_(200);
                            }
                            this.m_146850_(GameEvent.f_157806_);
                            this.m_5496_(SoundEvents.f_11970_, this.m_6121_(), this.m_6100_());
                            this.setBloodLevel(this.getBloodLevel() + 1);
                            if (this.getBloodLevel() > 3) {
                                this.m_6038_();
                                AlexsMobs.sendMSGToAll(new MessageMosquitoDismount(this.m_19879_(), mount.m_19879_()));
                                this.setFlying(false);
                                this.flightTicks = -15;
                            }
                        }
                    }
                    if (this.drinkTime > 81) {
                        this.drinkTime = -20 - this.f_19796_.m_188503_(20);
                        this.m_6038_();
                        AlexsMobs.sendMSGToAll(new MessageMosquitoDismount(this.m_19879_(), mount.m_19879_()));
                        this.setFlying(false);
                        this.flightTicks = -15;
                    }
                }
            }
        }
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(FLYING, (Object)false);
        this.f_19804_.m_135372_(SHOOTING, (Object)false);
        this.f_19804_.m_135372_(SICK, (Object)false);
        this.f_19804_.m_135372_(BLOOD_LEVEL, (Object)0);
        this.f_19804_.m_135372_(SHRINKING, (Object)false);
        this.f_19804_.m_135372_(FROM_FLY, (Object)false);
        this.f_19804_.m_135372_(MOSQUITO_SCALE, (Object)Float.valueOf(1.0f));
        this.f_19804_.m_135372_(LURING_LAVIATHAN, (Object)-1);
        this.f_19804_.m_135372_(FLEEING_ENTITY, (Object)-1);
    }

    public boolean isFlying() {
        return (Boolean)this.f_19804_.m_135370_(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.m_135381_(FLYING, (Object)flying);
    }

    public void setupShooting() {
        this.f_19804_.m_135381_(SHOOTING, (Object)true);
        this.shootingTicks = 5;
    }

    public int getLuringLaviathan() {
        return (Integer)this.f_19804_.m_135370_(LURING_LAVIATHAN);
    }

    public void setLuringLaviathan(int lure) {
        this.f_19804_.m_135381_(LURING_LAVIATHAN, (Object)lure);
    }

    public int getFleeingEntityId() {
        return (Integer)this.f_19804_.m_135370_(FLEEING_ENTITY);
    }

    public void setFleeingEntityId(int lure) {
        this.f_19804_.m_135381_(FLEEING_ENTITY, (Object)lure);
    }

    public int getBloodLevel() {
        return Math.min((Integer)this.f_19804_.m_135370_(BLOOD_LEVEL), 4);
    }

    public void setBloodLevel(int bloodLevel) {
        this.f_19804_.m_135381_(BLOOD_LEVEL, (Object)bloodLevel);
    }

    public boolean isShrinking() {
        return (Boolean)this.f_19804_.m_135370_(SHRINKING);
    }

    public boolean isFromFly() {
        return (Boolean)this.f_19804_.m_135370_(FROM_FLY);
    }

    public void setShrink(boolean shrink) {
        this.f_19804_.m_135381_(SHRINKING, (Object)shrink);
    }

    public void setFromFly(boolean fromFly) {
        this.f_19804_.m_135381_(FROM_FLY, (Object)fromFly);
    }

    public float getMosquitoScale() {
        return ((Float)this.f_19804_.m_135370_(MOSQUITO_SCALE)).floatValue();
    }

    public void setMosquitoScale(float scale) {
        this.f_19804_.m_135381_(MOSQUITO_SCALE, (Object)Float.valueOf(scale));
    }

    public boolean isSick() {
        return (Boolean)this.f_19804_.m_135370_(SICK);
    }

    public void setSick(boolean shrink) {
        this.f_19804_.m_135381_(SICK, (Object)shrink);
    }

    public void m_8119_() {
        super.m_8119_();
        boolean shooting = (Boolean)this.f_19804_.m_135370_(SHOOTING);
        if (this.prevFlying != this.isFlying()) {
            this.m_6210_();
        }
        if (shooting) {
            if (this.shootProgress < 5.0f) {
                this.shootProgress += 1.0f;
            }
        } else if (this.shootProgress > 0.0f) {
            this.shootProgress -= 1.0f;
        }
        if (this.isFlying()) {
            if (this.flyProgress < 5.0f) {
                this.flyProgress += 1.0f;
            }
        } else if (this.flyProgress > 0.0f) {
            this.flyProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            if (this.m_20159_()) {
                this.setFlying(false);
            }
            if (this.isFlying()) {
                this.m_20242_(true);
            } else {
                this.m_20242_(false);
            }
            LivingEntity target = this.m_5448_();
            if (this.getFleeingEntityId() == -1) {
                if (target == null && this.f_19797_ - this.repellentCheckTime > 50) {
                    this.repellentCheckTime = this.f_19797_;
                    LivingEntity closestRepel = null;
                    for (LivingEntity entity : this.m_9236_().m_6443_(LivingEntity.class, this.m_20191_().m_82400_(30.0), REPELLENT)) {
                        if (closestRepel != null && !(entity.m_20270_((Entity)this) < closestRepel.m_20270_((Entity)this))) continue;
                        closestRepel = entity;
                    }
                    if (closestRepel != null) {
                        this.setFleeingEntityId(closestRepel.m_19879_());
                    }
                }
                if (target != null && REPELLENT.test(target) && this.m_20270_((Entity)target) < 20.0f) {
                    this.setFleeingEntityId(target.m_19879_());
                }
            } else {
                LivingEntity living;
                Entity fleeing = this.m_9236_().m_6815_(this.getFleeingEntityId());
                if (fleeing instanceof LivingEntity && REPELLENT.test(living = (LivingEntity)fleeing) && this.m_20270_((Entity)living) < 20.0f) {
                    this.m_6710_(null);
                    this.m_6703_(null);
                    if (this.m_20159_()) {
                        this.m_8127_();
                    }
                    if (this.fleePos == null || this.fleePos.m_82554_(this.m_20182_()) < 3.0 || this.f_19796_.m_188503_(40) == 0) {
                        Vec3 vec = LandRandomPos.m_148521_((PathfinderMob)this, (int)8, (int)4, (Vec3)fleeing.m_20182_());
                        if (vec != null) {
                            this.fleePos = vec;
                        }
                    } else {
                        this.setFlying(true);
                        this.f_21342_.m_6849_(this.fleePos.f_82479_, this.fleePos.f_82480_ + 1.0, this.fleePos.f_82481_, (double)1.2f);
                    }
                } else {
                    this.setFleeingEntityId(-1);
                }
            }
            if (this.hasLuringLaviathan()) {
                this.m_6710_(null);
                this.m_6703_(null);
                Entity entity = this.m_9236_().m_6815_(this.getLuringLaviathan());
                if (entity instanceof EntityLaviathan && ((EntityLaviathan)entity).isChilling()) {
                    Vec3 vec = ((EntityLaviathan)entity).getLureMosquitoPos();
                    this.setFlying(true);
                    this.m_21391_(entity, 10.0f, 10.0f);
                    this.m_21566_().m_6849_(vec.f_82479_, vec.f_82480_, vec.f_82481_, (double)0.7f);
                } else {
                    this.setLuringLaviathan(-1);
                }
            }
        }
        if (this.flyProgress == 0.0f && this.f_19796_.m_188503_(200) == 0) {
            this.randomWingFlapTick = 5 + this.f_19796_.m_188503_(15);
        }
        if (this.randomWingFlapTick > 0) {
            --this.randomWingFlapTick;
        }
        if (!this.m_9236_().f_46443_ && this.m_20096_() && !this.isFlying() && (this.flightTicks >= 0 && this.f_19796_.m_188503_(5) == 0 || this.m_5448_() != null)) {
            this.setFlying(true);
            this.m_20256_(this.m_20184_().m_82520_((double)((this.f_19796_.m_188501_() * 2.0f - 1.0f) * 0.2f), 0.5, (double)((this.f_19796_.m_188501_() * 2.0f - 1.0f) * 0.2f)));
            this.m_6853_(false);
            this.f_19812_ = true;
        }
        if (this.flightTicks < 0) {
            ++this.flightTicks;
        }
        if (!this.m_9236_().f_46443_ && this.isFlying()) {
            ++this.flightTicks;
            if (!(this.flightTicks <= 200 || this.m_5448_() != null && this.m_5448_().m_6084_())) {
                BlockPos above = this.getGroundPosition(this.m_20183_().m_7494_());
                if (this.m_9236_().m_6425_(above).m_76178_() && !this.m_9236_().m_8055_(above).m_60795_()) {
                    this.m_20184_().m_82520_(0.0, -0.2, 0.0);
                    if (this.m_20096_()) {
                        this.setFlying(false);
                        this.flightTicks = -150 - this.f_19796_.m_188503_(200);
                    }
                }
            }
        }
        this.prevMosquitoScale = this.getMosquitoScale();
        if (this.isShrinking()) {
            if (this.getMosquitoScale() > 0.4f) {
                this.setMosquitoScale(this.getMosquitoScale() - 0.1f);
            }
        } else if (this.getMosquitoScale() < 1.0f && !this.isSick()) {
            this.setMosquitoScale(this.getMosquitoScale() + 0.05f);
        }
        if (!this.m_9236_().f_46443_ && this.shootingTicks > 0) {
            --this.shootingTicks;
            if (this.shootingTicks == 0) {
                if (this.m_5448_() != null && this.getBloodLevel() > 0) {
                    this.spit(this.m_5448_());
                }
                this.f_19804_.m_135381_(SHOOTING, (Object)false);
            }
        }
        if (this.isFlying()) {
            if (this.loopSoundTick == 0) {
                this.m_146850_(GameEvent.f_223709_);
                this.m_5496_((SoundEvent)AMSoundRegistry.MOSQUITO_LOOP.get(), this.m_6121_(), this.m_6100_());
            }
            ++this.loopSoundTick;
            if (this.loopSoundTick > 100) {
                this.loopSoundTick = 0;
            }
        }
        if (this.m_20159_()) {
            if (this.drinkTime < 0) {
                this.drinkTime = 0;
            }
            ++this.drinkTime;
        } else {
            this.drinkTime = 0;
        }
        this.prevFlyProgress = this.flyProgress;
        this.prevShootProgress = this.shootProgress;
        this.prevFlying = this.isFlying();
        if (this.isSick()) {
            ++this.sickTicks;
            if (this.m_5448_() != null && !this.m_20159_()) {
                this.m_6710_(null);
            }
            if (this.sickTicks > 100) {
                this.setShrink(false);
                this.setMosquitoScale(this.getMosquitoScale() + 0.015f);
                if (this.sickTicks > 160) {
                    EntityWarpedMosco mosco = (EntityWarpedMosco)((EntityType)AMEntityRegistry.WARPED_MOSCO.get()).m_20615_(this.m_9236_());
                    mosco.m_20359_((Entity)this);
                    if (!this.m_9236_().f_46443_) {
                        mosco.m_6518_((ServerLevelAccessor)this.m_9236_(), this.m_9236_().m_6436_(this.m_20183_()), MobSpawnType.CONVERSION, null, null);
                    }
                    if (!this.m_9236_().f_46443_) {
                        this.m_9236_().m_7605_((Entity)this, (byte)79);
                        this.m_9236_().m_7967_((Entity)mosco);
                    }
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 79) {
            for (int i = 0; i < 27; ++i) {
                double d0 = this.f_19796_.m_188583_() * 0.02;
                double d1 = this.f_19796_.m_188583_() * 0.02;
                double d2 = this.f_19796_.m_188583_() * 0.02;
                this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123813_, this.m_20208_(1.6), this.m_20186_() + (double)(this.f_19796_.m_188501_() * 3.4f), this.m_20262_(1.6), d0, d1, d2);
            }
        } else {
            super.m_7822_(id);
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    public MobType m_6336_() {
        return MobType.f_21642_;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public EntityDimensions m_6972_(Pose poseIn) {
        return this.isFlying() ? FLIGHT_SIZE : super.m_6972_(poseIn);
    }

    public void m_7023_(Vec3 vec3d) {
        if (this.m_20096_() && !this.isFlying()) {
            if (this.m_21573_().m_26570_() != null) {
                this.m_21573_().m_26573_();
            }
            vec3d = Vec3.f_82478_;
        }
        super.m_7023_(vec3d);
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        InteractionResult type = super.m_6071_(player, hand);
        if (item == AMItemRegistry.WARPED_MIXTURE.get() && !this.isSick()) {
            this.m_19983_(item.getCraftingRemainingItem(itemstack));
            if (!player.m_7500_()) {
                itemstack.m_41774_(1);
            }
            this.setSick(true);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this)).m_6662_() != HitResult.Type.MISS;
    }

    private BlockPos getGroundPosition(BlockPos radialPos) {
        while (radialPos.m_123342_() > 1 && this.m_9236_().m_46859_(radialPos)) {
            radialPos = radialPos.m_7495_();
        }
        return radialPos;
    }

    public boolean isNonMungusWarpedTrigger(Entity entity) {
        ResourceLocation mobtype = ForgeRegistries.ENTITY_TYPES.getKey((Object)entity.m_6095_());
        return mobtype != null && !AMConfig.warpedMoscoMobTriggers.isEmpty() && AMConfig.warpedMoscoMobTriggers.contains(mobtype.toString());
    }

    static class MoveHelperController
    extends MoveControl {
        private final EntityCrimsonMosquito parentEntity;

        public MoveHelperController(EntityCrimsonMosquito sunbird) {
            super((Mob)sunbird);
            this.parentEntity = sunbird;
        }

        public void m_8126_() {
            if (this.f_24978_ >= 1.0 && this.parentEntity.isSick()) {
                this.f_24978_ = 0.35;
            }
            if (this.parentEntity.isFlying()) {
                if (this.f_24981_ == MoveControl.Operation.STRAFE) {
                    Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                    double d0 = vector3d.m_82553_();
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82520_(0.0, vector3d.m_82490_(this.f_24978_ * 0.05 / d0).m_7098_(), 0.0));
                    float f = (float)this.f_24974_.m_21133_(Attributes.f_22279_);
                    float f1 = (float)this.f_24978_ * f;
                    this.f_24979_ = 1.0f;
                    this.f_24980_ = 0.0f;
                    this.f_24974_.m_7910_(f1);
                    this.f_24974_.m_21564_(this.f_24979_);
                    this.f_24974_.m_21570_(this.f_24980_);
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                    Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                    double d0 = vector3d.m_82553_();
                    if (d0 < this.parentEntity.m_20191_().m_82309_()) {
                        this.f_24981_ = MoveControl.Operation.WAIT;
                        this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82490_(0.5));
                    } else {
                        this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82549_(vector3d.m_82490_(this.f_24978_ * 0.05 / d0)));
                        if (this.parentEntity.m_5448_() == null) {
                            Vec3 vector3d1 = this.parentEntity.m_20184_();
                            this.parentEntity.m_146922_(-((float)Mth.m_14136_((double)vector3d1.f_82479_, (double)vector3d1.f_82481_)) * 57.295776f);
                            this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                        } else {
                            double d2 = this.parentEntity.m_5448_().m_20185_() - this.parentEntity.m_20185_();
                            double d1 = this.parentEntity.m_5448_().m_20189_() - this.parentEntity.m_20189_();
                            this.parentEntity.m_146922_(-((float)Mth.m_14136_((double)d2, (double)d1)) * 57.295776f);
                            this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                        }
                    }
                }
            } else {
                this.f_24981_ = MoveControl.Operation.WAIT;
                this.f_24974_.m_7910_(0.0f);
                this.f_24974_.m_21564_(0.0f);
                this.f_24974_.m_21570_(0.0f);
            }
        }

        private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; ++i) {
                axisalignedbb = axisalignedbb.m_82383_(p_220673_1_);
                if (this.parentEntity.m_9236_().m_45756_((Entity)this.parentEntity, axisalignedbb)) continue;
                return false;
            }
            return true;
        }
    }

    public static class FlyTowardsTarget
    extends Goal {
        private final EntityCrimsonMosquito parentEntity;

        public FlyTowardsTarget(EntityCrimsonMosquito mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            if (!this.parentEntity.isFlying() || this.parentEntity.getBloodLevel() > 0 || this.parentEntity.drinkTime < 0 || this.parentEntity.getFleeingEntityId() != -1) {
                return false;
            }
            return !this.parentEntity.m_20159_() && this.parentEntity.m_5448_() != null && !this.isBittenByMosquito((Entity)this.parentEntity.m_5448_());
        }

        public boolean m_8045_() {
            return this.parentEntity.drinkTime >= 0 && this.parentEntity.getFleeingEntityId() == -1 && this.parentEntity.m_5448_() != null && !this.isBittenByMosquito((Entity)this.parentEntity.m_5448_()) && !this.parentEntity.f_19862_ && this.parentEntity.getBloodLevel() == 0 && this.parentEntity.isFlying() && this.parentEntity.m_21566_().m_24995_();
        }

        public boolean isBittenByMosquito(Entity entity) {
            for (Entity e : entity.m_20197_()) {
                if (!(e instanceof EntityCrimsonMosquito)) continue;
                return true;
            }
            return false;
        }

        public void m_8041_() {
        }

        public void m_8037_() {
            if (this.parentEntity.m_5448_() != null) {
                this.parentEntity.m_21566_().m_6849_(this.parentEntity.m_5448_().m_20185_(), this.parentEntity.m_5448_().m_20186_(), this.parentEntity.m_5448_().m_20189_(), 1.0);
                if (this.parentEntity.m_20191_().m_82377_((double)0.3f, (double)0.3f, (double)0.3f).m_82381_(this.parentEntity.m_5448_().m_20191_()) && !this.isBittenByMosquito((Entity)this.parentEntity.m_5448_()) && this.parentEntity.drinkTime == 0) {
                    this.parentEntity.m_7998_((Entity)this.parentEntity.m_5448_(), true);
                    if (!this.parentEntity.m_9236_().f_46443_) {
                        AlexsMobs.sendMSGToAll(new MessageMosquitoMountPlayer(this.parentEntity.m_19879_(), this.parentEntity.m_5448_().m_19879_()));
                    }
                }
            }
        }
    }

    public static class FlyAwayFromTarget
    extends Goal {
        private final EntityCrimsonMosquito parentEntity;
        private int spitCooldown = 0;
        private BlockPos shootPos = null;

        public FlyAwayFromTarget(EntityCrimsonMosquito mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            if (!this.parentEntity.isFlying() || this.parentEntity.getBloodLevel() <= 0 && this.parentEntity.drinkTime >= 0 || this.parentEntity.getFleeingEntityId() != -1) {
                return false;
            }
            if (!this.parentEntity.m_20159_() && this.parentEntity.m_5448_() != null) {
                this.shootPos = this.getBlockInTargetsViewMosquito(this.parentEntity.m_5448_());
                return true;
            }
            return false;
        }

        public boolean m_8045_() {
            return this.parentEntity.m_5448_() != null && (this.parentEntity.getBloodLevel() > 0 || this.parentEntity.drinkTime < 0) && this.parentEntity.isFlying() && !this.parentEntity.f_19862_;
        }

        public void m_8041_() {
            this.spitCooldown = 20;
        }

        public void m_8037_() {
            if (this.spitCooldown > 0) {
                --this.spitCooldown;
            }
            if (this.parentEntity.m_5448_() != null) {
                if (this.shootPos == null) {
                    this.shootPos = this.getBlockInTargetsViewMosquito(this.parentEntity.m_5448_());
                } else {
                    this.parentEntity.m_21566_().m_6849_((double)this.shootPos.m_123341_() + 0.5, (double)this.shootPos.m_123342_() + 0.5, (double)this.shootPos.m_123343_() + 0.5, 1.0);
                    this.parentEntity.m_21391_((Entity)this.parentEntity.m_5448_(), 30.0f, 30.0f);
                    if (this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)this.shootPos)) < 2.5) {
                        if (this.spitCooldown == 0 && this.parentEntity.getBloodLevel() > 0) {
                            this.parentEntity.setupShooting();
                            this.spitCooldown = 20;
                        }
                        this.shootPos = null;
                    }
                }
            }
        }

        public BlockPos getBlockInTargetsViewMosquito(LivingEntity target) {
            float radius = 4 + this.parentEntity.m_217043_().m_188503_(5);
            float angle = (float)Math.PI / 180 * (target.f_20885_ + 90.0f + (float)this.parentEntity.m_217043_().m_188503_(180));
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos ground = AMBlockPos.fromCoords(target.m_20185_() + extraX, target.m_20186_() + 1.0, target.m_20189_() + extraZ);
            if (this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)ground)) > 30.0 && !this.parentEntity.isTargetBlocked(Vec3.m_82512_((Vec3i)ground)) && this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)ground)) > 6.0) {
                return ground;
            }
            return this.parentEntity.m_20183_();
        }
    }

    static class RandomFlyGoal
    extends Goal {
        private final EntityCrimsonMosquito parentEntity;
        private BlockPos target = null;

        public RandomFlyGoal(EntityCrimsonMosquito mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            MoveControl movementcontroller = this.parentEntity.m_21566_();
            if (!this.parentEntity.isFlying() || this.parentEntity.m_5448_() != null || this.parentEntity.hasLuringLaviathan() || this.parentEntity.getFleeingEntityId() != -1) {
                return false;
            }
            if (!movementcontroller.m_24995_() || this.target == null) {
                this.target = this.getBlockInViewMosquito();
                if (this.target != null) {
                    this.parentEntity.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                }
                return true;
            }
            return false;
        }

        public boolean m_8045_() {
            return this.target != null && this.parentEntity.isFlying() && this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)this.target)) > 2.4 && this.parentEntity.m_21566_().m_24995_() && !this.parentEntity.f_19862_;
        }

        public void m_8041_() {
            this.target = null;
        }

        public void m_8037_() {
            if (this.target == null) {
                this.target = this.getBlockInViewMosquito();
            }
            if (this.target != null) {
                this.parentEntity.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)this.target)) < 2.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewMosquito() {
            float radius = 1 + this.parentEntity.m_217043_().m_188503_(5);
            float neg = this.parentEntity.m_217043_().m_188499_() ? 1.0f : -1.0f;
            float renderYawOffset = this.parentEntity.f_20883_;
            float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.parentEntity.m_217043_().m_188501_() * neg;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos radialPos = AMBlockPos.fromCoords(this.parentEntity.m_20185_() + extraX, this.parentEntity.m_20186_() + 2.0, this.parentEntity.m_20189_() + extraZ);
            BlockPos ground = this.parentEntity.getGroundPosition(radialPos);
            int up = this.parentEntity.isSick() ? 2 : 6;
            BlockPos newPos = ground.m_6630_(1 + this.parentEntity.m_217043_().m_188503_(up));
            if (!this.parentEntity.isTargetBlocked(Vec3.m_82512_((Vec3i)newPos)) && this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)newPos)) > 6.0) {
                return newPos;
            }
            return null;
        }
    }
}

