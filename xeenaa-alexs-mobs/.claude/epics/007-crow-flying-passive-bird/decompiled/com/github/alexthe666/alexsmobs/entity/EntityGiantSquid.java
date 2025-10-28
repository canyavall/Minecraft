/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Vec3i
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
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.TryFindWaterGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation
 *  net.minecraft.world.entity.animal.WaterAnimal
 *  net.minecraft.world.entity.monster.Guardian
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.entity.PartEntity
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCachalotWhale;
import com.github.alexthe666.alexsmobs.entity.EntityGiantSquidPart;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;

public class EntityGiantSquid
extends WaterAnimal {
    private static final EntityDataAccessor<Float> SQUID_PITCH = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Float> DEPRESSURIZATION = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Boolean> OVERRIDE_BODYROT = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> GRABBING = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> CAPTURED = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> BLUE = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> GRAB_ENTITY = SynchedEntityData.m_135353_(EntityGiantSquid.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    public final EntityGiantSquidPart mantlePart1;
    public final EntityGiantSquidPart mantlePart2;
    public final EntityGiantSquidPart mantlePart3;
    public final EntityGiantSquidPart tentaclesPart1;
    public final EntityGiantSquidPart tentaclesPart2;
    public final EntityGiantSquidPart tentaclesPart3;
    public final EntityGiantSquidPart tentaclesPart4;
    public final EntityGiantSquidPart tentaclesPart5;
    public final EntityGiantSquidPart tentaclesPart6;
    public final EntityGiantSquidPart mantleCollisionPart;
    public final EntityGiantSquidPart[] allParts;
    public final float[][] ringBuffer = new float[64][2];
    public int ringBufferIndex = -1;
    public float prevSquidPitch;
    public float prevDepressurization;
    public float grabProgress;
    public float prevGrabProgress;
    public float dryProgress;
    public float prevDryProgress;
    public float capturedProgress;
    public float prevCapturedProgress;
    public int humTick = 0;
    private int holdTime;
    private int resetCapturedStateIn;

    protected EntityGiantSquid(EntityType type, Level level) {
        super(type, level);
        this.m_21441_(BlockPathTypes.WATER, 0.0f);
        this.mantlePart1 = new EntityGiantSquidPart(this, 0.9f, 0.9f);
        this.mantlePart2 = new EntityGiantSquidPart(this, 1.2f, 1.2f);
        this.mantlePart3 = new EntityGiantSquidPart(this, 0.45f, 0.45f);
        this.tentaclesPart1 = new EntityGiantSquidPart(this, 0.9f, 0.9f);
        this.tentaclesPart2 = new EntityGiantSquidPart(this, 1.0f, 1.0f);
        this.tentaclesPart3 = new EntityGiantSquidPart(this, 1.2f, 1.2f);
        this.tentaclesPart4 = new EntityGiantSquidPart(this, 1.2f, 1.2f);
        this.tentaclesPart5 = new EntityGiantSquidPart(this, 1.2f, 1.2f);
        this.tentaclesPart6 = new EntityGiantSquidPart(this, 1.2f, 1.2f);
        this.mantleCollisionPart = new EntityGiantSquidPart(this, 2.9f, 2.9f, true);
        this.allParts = new EntityGiantSquidPart[]{this.mantlePart1, this.mantlePart2, this.mantlePart3, this.mantleCollisionPart, this.tentaclesPart1, this.tentaclesPart2, this.tentaclesPart3, this.tentaclesPart4, this.tentaclesPart5, this.tentaclesPart6};
        this.f_21365_ = new SmoothSwimmingLookControl((Mob)this, 4);
        this.f_21342_ = new AquaticMoveController((PathfinderMob)this, 1.2f, 5.0f);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.giantSquidSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canGiantSquidSpawn(EntityType<EntityGiantSquid> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_46801_(pos) && iServerWorld.m_46801_(pos.m_7494_());
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing((LevelAccessor)worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).m_76178_() && down.m_123342_() > 1) {
            down = down.m_7495_();
        }
        this.m_6034_((float)down.m_123341_() + 0.5f, down.m_123342_() + 3 + this.f_19796_.m_188503_(3), (float)down.m_123343_() + 0.5f);
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.GIANT_SQUID_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.GIANT_SQUID_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 38.0).m_22268_(Attributes.f_22281_, 8.0).m_22268_(Attributes.f_22279_, 0.25);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(SQUID_PITCH, (Object)Float.valueOf(0.0f));
        this.f_19804_.m_135372_(OVERRIDE_BODYROT, (Object)false);
        this.f_19804_.m_135372_(DEPRESSURIZATION, (Object)Float.valueOf(0.0f));
        this.f_19804_.m_135372_(GRABBING, (Object)false);
        this.f_19804_.m_135372_(CAPTURED, (Object)false);
        this.f_19804_.m_135372_(BLUE, (Object)false);
        this.f_19804_.m_135372_(GRAB_ENTITY, (Object)-1);
    }

    @Nullable
    public Entity getGrabbedEntity() {
        if (!this.m_9236_().f_46443_ || (Integer)this.f_19804_.m_135370_(GRAB_ENTITY) == -1) {
            return this.m_5448_();
        }
        return this.m_9236_().m_6815_(((Integer)this.f_19804_.m_135370_(GRAB_ENTITY)).intValue());
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        return super.m_6071_(player, hand);
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new WaterBoundPathNavigation((Mob)this, worldIn);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(1, (Goal)new TryFindWaterGoal((PathfinderMob)this));
        this.f_21345_.m_25352_(1, (Goal)new AIAvoidWhales());
        this.f_21345_.m_25352_(2, (Goal)new AIMelee());
        this.f_21345_.m_25352_(3, (Goal)new AIDeepwaterSwimming());
        this.f_21345_.m_25352_(5, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[]{EntityCachalotWhale.class}));
        this.f_21346_.m_25352_(2, (Goal)new EntityAINearestTarget3D((Mob)this, Guardian.class, 20, true, true, null){

            public boolean m_8036_() {
                return super.m_8036_();
            }
        });
        this.f_21346_.m_25352_(3, (Goal)new EntityAINearestTarget3D((Mob)this, LivingEntity.class, 70, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.GIANT_SQUID_TARGETS)){

            public boolean m_8036_() {
                return !EntityGiantSquid.this.m_20072_() && !EntityGiantSquid.this.isCaptured() && super.m_8036_();
            }
        });
    }

    public void m_8107_() {
        super.m_8107_();
        if (!this.m_21525_()) {
            if (this.ringBufferIndex < 0) {
                for (int i = 0; i < this.ringBuffer.length; ++i) {
                    this.ringBuffer[i][0] = 180.0f + this.m_146908_();
                    this.ringBuffer[i][1] = this.getSquidPitch();
                }
            }
            ++this.ringBufferIndex;
            if (this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = this.f_20883_;
            this.ringBuffer[this.ringBufferIndex][1] = this.getSquidPitch();
        }
    }

    public void m_8119_() {
        super.m_8119_();
        if (this.f_19797_ % 100 == 0) {
            this.m_5634_(2.0f);
        }
        float f = Mth.m_14177_((float)(180.0f + this.m_146908_()));
        this.f_20883_ = this.m_21376_(this.f_20883_, f, 180.0f);
        this.prevSquidPitch = this.getSquidPitch();
        this.prevDepressurization = this.getDepressurization();
        this.prevDryProgress = this.dryProgress;
        this.prevGrabProgress = this.grabProgress;
        this.prevCapturedProgress = this.capturedProgress;
        if (!this.m_20069_() && this.dryProgress < 5.0f) {
            this.dryProgress += 1.0f;
        }
        if (this.m_20069_() && this.dryProgress > 0.0f) {
            this.dryProgress -= 1.0f;
        }
        if (this.isGrabbing()) {
            if (this.grabProgress < 5.0f) {
                this.grabProgress += 0.25f;
            }
        } else if (this.grabProgress > 0.0f) {
            this.grabProgress -= 0.25f;
        }
        if (this.isCaptured()) {
            if (this.capturedProgress < 5.0f) {
                this.capturedProgress += 0.5f;
            }
        } else if (this.capturedProgress > 0.0f) {
            this.capturedProgress -= 0.5f;
        }
        if (this.isGrabbing()) {
            Entity target = this.getGrabbedEntity();
            if (!this.m_9236_().f_46443_ && target != null) {
                this.f_19804_.m_135381_(GRAB_ENTITY, (Object)target.m_19879_());
                if (this.holdTime % 20 == 0 && this.holdTime > 30) {
                    target.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)(3 + this.f_19796_.m_188503_(5)));
                }
            }
            if (target != null && target.m_6084_()) {
                this.m_146926_(0.0f);
                float invert = 1.0f - this.grabProgress * 0.2f;
                Vec3 extraVec = new Vec3(0.0, 0.0, (double)(2.0f + invert * 7.0f)).m_82496_(-this.m_146909_() * ((float)Math.PI / 180)).m_82524_(-this.f_20883_ * ((float)Math.PI / 180));
                Vec3 minus = new Vec3(this.m_20185_() + extraVec.f_82479_ - target.m_20185_(), this.m_20186_() + extraVec.f_82480_ - target.m_20186_(), this.m_20189_() + extraVec.f_82481_ - target.m_20189_());
                target.m_20256_(minus);
            }
            ++this.holdTime;
            if (this.holdTime > 1000) {
                this.holdTime = 0;
                this.setGrabbing(false);
            }
        } else {
            this.holdTime = 0;
        }
        if (!this.m_21525_()) {
            Vec3[] avector3d = new Vec3[this.allParts.length];
            for (int j = 0; j < this.allParts.length; ++j) {
                this.allParts[j].collideWithNearbyEntities();
                avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
            }
            float pitch = this.m_146909_() * ((float)Math.PI / 180) * 0.8f;
            this.mantleCollisionPart.m_6034_(this.m_20185_(), this.m_20186_() - (double)((this.mantleCollisionPart.m_20206_() - this.m_20192_()) * 0.5f * (1.0f - this.dryProgress * 0.2f)), this.m_20189_());
            this.setPartPositionFromBuffer(this.mantlePart1, pitch, 0.9f, 0);
            this.setPartPositionFromBuffer(this.mantlePart2, pitch, 1.6f, 0);
            this.setPartPositionFromBuffer(this.mantlePart3, pitch, 2.45f, 0);
            this.setPartPositionFromBuffer(this.tentaclesPart1, pitch, -0.8f, 0);
            this.setPartPositionFromBuffer(this.tentaclesPart2, pitch, -1.5f, 0);
            this.setPartPositionFromBuffer(this.tentaclesPart3, pitch, -2.3f, 5);
            this.setPartPositionFromBuffer(this.tentaclesPart4, pitch, -3.4f, 10);
            this.setPartPositionFromBuffer(this.tentaclesPart5, pitch, -5.4f, 15);
            this.setPartPositionFromBuffer(this.tentaclesPart6, pitch, -7.4f, 20);
            if (this.m_20072_()) {
                if (this.mantleCollisionPart.scale != 1.0f) {
                    this.mantleCollisionPart.scale = 1.0f;
                    this.mantleCollisionPart.m_6210_();
                }
            } else if (this.mantleCollisionPart.scale != 0.25f) {
                this.mantleCollisionPart.scale = 0.25f;
                this.mantleCollisionPart.m_6210_();
            }
            for (int l = 0; l < this.allParts.length; ++l) {
                this.allParts[l].f_19854_ = avector3d[l].f_82479_;
                this.allParts[l].f_19855_ = avector3d[l].f_82480_;
                this.allParts[l].f_19856_ = avector3d[l].f_82481_;
                this.allParts[l].f_19790_ = avector3d[l].f_82479_;
                this.allParts[l].f_19791_ = avector3d[l].f_82480_;
                this.allParts[l].f_19792_ = avector3d[l].f_82481_;
            }
            this.m_20242_(this.m_20069_());
        }
        if (!this.m_9236_().f_46443_) {
            if (this.getSquidPitch() > 0.0f) {
                float decrease = Math.min(2.0f, this.getSquidPitch());
                this.decrementSquidPitch(decrease);
            }
            if (this.getSquidPitch() < 0.0f) {
                float decrease = Math.min(2.0f, -this.getSquidPitch());
                this.incrementSquidPitch(decrease);
            }
            if (this.m_20072_()) {
                float dist = (float)this.m_20184_().m_7098_() * 45.0f;
                if (((Boolean)this.f_19804_.m_135370_(OVERRIDE_BODYROT)).booleanValue()) {
                    this.decrementSquidPitch(dist);
                } else {
                    this.incrementSquidPitch(dist);
                }
            }
            if (!this.m_20096_() && this.m_204036_(FluidTags.f_13131_) < (double)this.m_20206_()) {
                this.m_20256_(this.m_20184_().m_82520_(0.0, (double)-0.1f, 0.0));
            }
            float pressure = this.getDepressureLevel();
            if (this.getDepressurization() < pressure) {
                this.setDepressurization(this.getDepressurization() + 0.1f);
            }
            if (this.getDepressurization() > pressure) {
                this.setDepressurization(this.getDepressurization() - 0.1f);
            }
        }
        if (this.isHumming()) {
            if (this.humTick % 20 == 0) {
                this.m_5496_((SoundEvent)AMSoundRegistry.GIANT_SQUID_GAMES.get(), this.m_6121_(), 1.0f);
                this.humTick = 0;
            }
            ++this.humTick;
        }
        if (!this.m_9236_().f_46443_) {
            if (this.resetCapturedStateIn > 0) {
                --this.resetCapturedStateIn;
            } else {
                this.setCaptured(false);
            }
        }
    }

    private boolean isHumming() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("squid games!!") || AlexsMobs.isAprilFools();
    }

    public float getRingBuffer(int bufferOffset, float partialTicks, boolean pitch) {
        int i = this.ringBufferIndex - bufferOffset & 0x3F;
        int j = this.ringBufferIndex - bufferOffset - 1 & 0x3F;
        int k = pitch ? 1 : 0;
        float prevBuffer = this.ringBuffer[j][k];
        float buffer = this.ringBuffer[i][k];
        float end = prevBuffer + (buffer - prevBuffer) * partialTicks;
        return this.m_21376_(prevBuffer, end, 10.0f);
    }

    private void setPartPosition(EntityGiantSquidPart part, double offsetX, double offsetY, double offsetZ, float offsetScale) {
        part.m_6034_(this.m_20185_() + offsetX * (double)offsetScale * (double)part.scale, this.m_20186_() + offsetY * (double)offsetScale * (double)part.scale, this.m_20189_() + offsetZ * (double)offsetScale * (double)part.scale);
    }

    private void setPartPositionFromBuffer(EntityGiantSquidPart part, float pitch, float offsetScale, int ringBufferOffset) {
        float f2 = Mth.m_14031_((float)(this.getRingBuffer(ringBufferOffset, 1.0f, false) * ((float)Math.PI / 180))) * (1.0f - Math.abs(this.m_146909_() / 90.0f));
        float f3 = Mth.m_14089_((float)(this.getRingBuffer(ringBufferOffset, 1.0f, false) * ((float)Math.PI / 180))) * (1.0f - Math.abs(this.m_146909_() / 90.0f));
        this.setPartPosition(part, f2, pitch, -f3, offsetScale);
    }

    public int m_8132_() {
        return 1;
    }

    public int m_8085_() {
        return 3;
    }

    public void m_7023_(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            if (((Boolean)this.f_19804_.m_135370_(OVERRIDE_BODYROT)).booleanValue()) {
                travelVector = new Vec3(travelVector.f_82479_, travelVector.f_82480_, -travelVector.f_82481_);
            }
            this.m_19920_(this.m_6113_(), travelVector);
            double d = this.m_5448_() == null ? 0.6 : 0.9;
            this.m_20256_(this.m_20184_().m_82542_(0.9, d, 0.9));
            this.m_6478_(MoverType.SELF, this.m_20184_());
        } else {
            super.m_7023_(travelVector);
        }
    }

    public boolean m_6040_() {
        return true;
    }

    public boolean m_6063_() {
        return false;
    }

    public MobType m_6336_() {
        return MobType.f_21644_;
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setBlue(compound.m_128471_("Blue"));
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Blue", this.isBlue());
    }

    public boolean m_6914_(LevelReader worldIn) {
        return worldIn.m_45784_((Entity)this);
    }

    public float getDepressurization() {
        return Mth.m_14036_((float)((Float)this.f_19804_.m_135370_(DEPRESSURIZATION)).floatValue(), (float)0.0f, (float)1.0f);
    }

    public void setDepressurization(float depressurization) {
        this.f_19804_.m_135381_(DEPRESSURIZATION, (Object)Float.valueOf(depressurization));
    }

    public float getSquidPitch() {
        return Mth.m_14036_((float)((Float)this.f_19804_.m_135370_(SQUID_PITCH)).floatValue(), (float)-90.0f, (float)90.0f);
    }

    public void setSquidPitch(float pitch) {
        this.f_19804_.m_135381_(SQUID_PITCH, (Object)Float.valueOf(pitch));
    }

    public void incrementSquidPitch(float pitch) {
        this.f_19804_.m_135381_(SQUID_PITCH, (Object)Float.valueOf(this.getSquidPitch() + pitch));
    }

    public void decrementSquidPitch(float pitch) {
        this.f_19804_.m_135381_(SQUID_PITCH, (Object)Float.valueOf(this.getSquidPitch() - pitch));
    }

    public boolean isGrabbing() {
        return (Boolean)this.f_19804_.m_135370_(GRABBING);
    }

    public void setGrabbing(boolean running) {
        this.f_19804_.m_135381_(GRABBING, (Object)running);
    }

    public boolean isCaptured() {
        return (Boolean)this.f_19804_.m_135370_(CAPTURED);
    }

    public void setCaptured(boolean running) {
        this.f_19804_.m_135381_(CAPTURED, (Object)running);
    }

    public boolean isBlue() {
        return (Boolean)this.f_19804_.m_135370_(BLUE);
    }

    public void setBlue(boolean t) {
        this.f_19804_.m_135381_(BLUE, (Object)t);
    }

    public void m_7334_(Entity entity) {
        if (!this.isCaptured()) {
            super.m_7334_(entity);
        }
    }

    public void m_267651_(boolean flying) {
        float f1 = (float)Mth.m_184648_((double)(this.m_20185_() - this.f_19854_), (double)(this.m_20186_() - this.f_19855_), (double)(this.m_20189_() - this.f_19856_));
        float f2 = Math.min(f1 * 8.0f, 1.0f);
        this.f_267362_.m_267566_(f2, 0.4f);
    }

    public boolean m_5829_() {
        return this.m_6084_();
    }

    public Vec3 m_20272_(Vec3 movement) {
        boolean flag3;
        if (this.m_146899_() || !this.m_20072_()) {
            return super.m_20272_(movement);
        }
        AABB aabb = this.mantleCollisionPart.m_20191_();
        List list = this.m_9236_().m_183134_((Entity)this, aabb.m_82369_(movement));
        Vec3 vec3 = movement.m_82556_() == 0.0 ? movement : EntityGiantSquid.m_198894_((Entity)this, (Vec3)movement, (AABB)aabb, (Level)this.m_9236_(), (List)list);
        boolean flag = movement.f_82479_ != vec3.f_82479_;
        boolean flag1 = movement.f_82480_ != vec3.f_82480_;
        boolean flag2 = movement.f_82481_ != vec3.f_82481_;
        boolean bl = flag3 = this.m_20096_() || flag1 && movement.f_82480_ < 0.0;
        if (this.getStepHeight() > 0.0f && flag3 && (flag || flag2)) {
            Vec3 vec33;
            Vec3 vec31 = EntityGiantSquid.m_198894_((Entity)this, (Vec3)new Vec3(movement.f_82479_, (double)this.getStepHeight(), movement.f_82481_), (AABB)aabb, (Level)this.m_9236_(), (List)list);
            Vec3 vec32 = EntityGiantSquid.m_198894_((Entity)this, (Vec3)new Vec3(0.0, (double)this.getStepHeight(), 0.0), (AABB)aabb.m_82363_(movement.f_82479_, 0.0, movement.f_82481_), (Level)this.m_9236_(), (List)list);
            if (vec32.f_82480_ < (double)this.getStepHeight() && (vec33 = EntityGiantSquid.m_198894_((Entity)this, (Vec3)new Vec3(movement.f_82479_, 0.0, movement.f_82481_), (AABB)aabb.m_82383_(vec32), (Level)this.m_9236_(), (List)list).m_82549_(vec32)).m_165925_() > vec31.m_165925_()) {
                vec31 = vec33;
            }
            if (vec31.m_165925_() > vec3.m_165925_()) {
                return vec31.m_82549_(EntityGiantSquid.m_198894_((Entity)this, (Vec3)new Vec3(0.0, -vec31.f_82480_ + movement.f_82480_, 0.0), (AABB)aabb.m_82383_(vec31), (Level)this.m_9236_(), (List)list));
            }
        }
        return vec3;
    }

    public float m_146909_() {
        return this.getSquidPitch();
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    public boolean attackEntityPartFrom(EntityGiantSquidPart part, DamageSource source, float amount) {
        return this.m_6469_(source, amount);
    }

    public boolean m_6673_(DamageSource source) {
        return source.m_276093_(DamageTypes.f_268612_) || super.m_6673_(source);
    }

    public void directPitch(double d0, double d1, double d2, double d3) {
        boolean shift = (Boolean)this.f_19804_.m_135370_(OVERRIDE_BODYROT);
        float add = shift ? 90.0f : -90.0f;
        float f = (float)(Mth.m_14136_((double)d2, (double)d0) * 57.2957763671875) + add;
        this.m_146922_(this.m_21376_(this.m_146908_(), f, shift ? 10.0f : 5.0f));
    }

    public float m_5686_(float partialTick) {
        return this.prevSquidPitch + (this.getSquidPitch() - this.prevSquidPitch) * partialTick;
    }

    public float m_5675_(float partialTick) {
        return partialTick == 1.0f ? this.f_20883_ : Mth.m_14179_((float)partialTick, (float)this.f_20884_, (float)this.f_20883_);
    }

    protected float m_21376_(float in, float target, float maxShift) {
        float f1;
        float f = Mth.m_14177_((float)(target - in));
        if (f > maxShift) {
            f = maxShift;
        }
        if (f < -maxShift) {
            f = -maxShift;
        }
        if ((f1 = in + f) < 0.0f) {
            f1 += 360.0f;
        } else if (f1 > 360.0f) {
            f1 -= 360.0f;
        }
        return f1;
    }

    private float getDepressureLevel() {
        BlockState blockstate;
        int waterLevelAbove;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (waterLevelAbove = 0; waterLevelAbove < 10 && ((blockstate = this.m_9236_().m_8055_((BlockPos)blockpos$mutable.m_122169_(this.m_20185_(), this.m_20186_() + (double)waterLevelAbove, this.m_20189_()))).m_60819_().m_205070_(FluidTags.f_13131_) || blockstate.m_280296_()); ++waterLevelAbove) {
        }
        return 1.0f - (float)waterLevelAbove / 10.0f;
    }

    private boolean canFitAt(BlockPos pos) {
        return true;
    }

    public boolean tickCaptured(EntityCachalotWhale whale) {
        this.resetCapturedStateIn = 25;
        if (this.f_19796_.m_188503_(13) == 0) {
            this.spawnInk();
            whale.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), 4 + this.f_19796_.m_188503_(4));
            if (this.f_19796_.m_188501_() <= 0.3f) {
                this.setCaptured(false);
                if (this.f_19796_.m_188501_() < 0.2f) {
                    this.m_19998_((ItemLike)AMItemRegistry.LOST_TENTACLE.get());
                }
                return true;
            }
        }
        this.setCaptured(true);
        this.setSquidPitch(0.0f);
        return false;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        super.m_7822_(id);
    }

    public boolean m_6469_(DamageSource src, float f) {
        if (super.m_6469_(src, f) && this.m_21188_() != null && !this.isCaptured() && this.f_19796_.m_188499_()) {
            this.spawnInk();
            return true;
        }
        return false;
    }

    private void spawnInk() {
        this.m_146850_(GameEvent.f_223708_);
        this.m_5496_(SoundEvents.f_12441_, this.m_6121_(), 0.5f * this.m_6100_());
        if (!this.m_9236_().f_46443_) {
            Vec3 inkDirection = new Vec3(0.0, 0.0, (double)1.2f).m_82496_(-this.m_146909_() * ((float)Math.PI / 180)).m_82524_(-this.f_20883_ * ((float)Math.PI / 180));
            Vec3 vec3 = this.m_20182_().m_82549_(inkDirection);
            for (int i = 0; i < 30; ++i) {
                Vec3 vec32 = inkDirection.m_82520_((double)(this.f_19796_.m_188501_() - 0.5f), (double)(this.f_19796_.m_188501_() - 0.5f), (double)(this.f_19796_.m_188501_() - 0.5f)).m_82490_(0.8 + (double)(this.f_19796_.m_188501_() * 2.0f));
                ((ServerLevel)this.m_9236_()).m_8767_((ParticleOptions)ParticleTypes.f_123765_, vec3.f_82479_, vec3.f_82480_ + 0.5, vec3.f_82481_, 0, vec32.f_82479_, vec32.f_82480_, vec32.f_82481_, (double)0.1f);
            }
        }
    }

    private class AIAvoidWhales
    extends Goal {
        private EntityCachalotWhale whale;
        private Vec3 moveTo;
        private int runDelay;

        public AIAvoidWhales() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            if (EntityGiantSquid.this.m_20072_() && !EntityGiantSquid.this.f_19862_ && !EntityGiantSquid.this.isCaptured() && this.runDelay-- <= 0) {
                EntityCachalotWhale closest = null;
                float dist = 50.0f;
                for (EntityCachalotWhale dude : EntityGiantSquid.this.m_9236_().m_45976_(EntityCachalotWhale.class, EntityGiantSquid.this.m_20191_().m_82400_((double)dist))) {
                    if (closest != null && !(dude.m_20270_((Entity)EntityGiantSquid.this) < closest.m_20270_((Entity)EntityGiantSquid.this))) continue;
                    closest = dude;
                }
                if (closest != null) {
                    this.whale = closest;
                    return true;
                }
                this.runDelay = 50 + EntityGiantSquid.this.f_19796_.m_188503_(50);
            }
            return false;
        }

        public boolean m_8045_() {
            return this.whale != null && this.whale.m_6084_() && !EntityGiantSquid.this.f_19862_ && EntityGiantSquid.this.m_20270_((Entity)this.whale) < 60.0f;
        }

        public void m_8037_() {
            if (this.whale != null && this.whale.m_6084_()) {
                double dist = EntityGiantSquid.this.m_20270_((Entity)this.whale);
                Vec3 vec = EntityGiantSquid.this.m_20182_().m_82546_(this.whale.m_20182_()).m_82541_();
                Vec3 vec2 = EntityGiantSquid.this.m_20182_().m_82549_(vec.m_82490_((double)(12 + EntityGiantSquid.this.f_19796_.m_188503_(5))));
                EntityGiantSquid.this.m_21573_().m_26519_(vec2.f_82479_, vec2.f_82480_, vec2.f_82481_, dist < 20.0 ? (double)1.9f : (double)1.3f);
            }
        }

        public void m_8041_() {
            this.whale = null;
            this.moveTo = null;
        }
    }

    private class AIMelee
    extends Goal {
        private AIMelee() {
        }

        public boolean m_8036_() {
            return EntityGiantSquid.this.m_20072_() && EntityGiantSquid.this.m_5448_() != null && EntityGiantSquid.this.m_5448_().m_6084_();
        }

        public void m_8037_() {
            EntityGiantSquid squid = EntityGiantSquid.this;
            LivingEntity target = EntityGiantSquid.this.m_5448_();
            double dist = squid.m_20270_((Entity)target);
            if (squid.m_142582_((Entity)target) && dist < 7.0) {
                squid.setGrabbing(true);
            } else {
                Vec3 moveBodyTo = target.m_20182_();
                squid.m_21573_().m_26519_(moveBodyTo.f_82479_, moveBodyTo.f_82480_, moveBodyTo.f_82481_, 1.0);
            }
            if (dist < 14.0) {
                squid.f_19804_.m_135381_(OVERRIDE_BODYROT, (Object)true);
            } else {
                squid.f_19804_.m_135381_(OVERRIDE_BODYROT, (Object)false);
            }
        }

        public void m_8041_() {
            EntityGiantSquid.this.f_19804_.m_135381_(OVERRIDE_BODYROT, (Object)false);
            EntityGiantSquid.this.setGrabbing(false);
        }
    }

    private class AIDeepwaterSwimming
    extends Goal {
        private BlockPos moveTo;

        public AIDeepwaterSwimming() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            BlockPos found;
            if (EntityGiantSquid.this.m_20160_() || EntityGiantSquid.this.m_5448_() != null && !EntityGiantSquid.this.isGrabbing() || !EntityGiantSquid.this.m_20069_() && !EntityGiantSquid.this.m_20077_()) {
                return false;
            }
            if ((EntityGiantSquid.this.m_21573_().m_26571_() || EntityGiantSquid.this.m_217043_().m_188503_(30) == 0) && (found = this.findTargetPos()) != null) {
                this.moveTo = found;
                return true;
            }
            return false;
        }

        private BlockPos findTargetPos() {
            RandomSource r = EntityGiantSquid.this.m_217043_();
            for (int i = 0; i < 15; ++i) {
                BlockPos pos = EntityGiantSquid.this.m_20183_().m_7918_(r.m_188503_(16) - 8, r.m_188503_(32) - 16, r.m_188503_(16) - 8);
                if (!EntityGiantSquid.this.m_9236_().m_46801_(pos) || !EntityGiantSquid.this.canFitAt(pos)) continue;
                return this.getDeeperTarget(pos);
            }
            return null;
        }

        private BlockPos getDeeperTarget(BlockPos waterAtPos) {
            BlockPos surface = new BlockPos((Vec3i)waterAtPos);
            BlockPos seafloor = new BlockPos((Vec3i)waterAtPos);
            while (EntityGiantSquid.this.m_9236_().m_46801_(surface) && surface.m_123342_() < 320) {
                surface = surface.m_7494_();
            }
            while (EntityGiantSquid.this.m_9236_().m_46801_(seafloor) && seafloor.m_123342_() > -64) {
                seafloor = seafloor.m_7495_();
            }
            int distance = surface.m_123342_() - seafloor.m_123342_();
            if (distance < 10) {
                return waterAtPos;
            }
            int i = (int)((double)distance * 0.4);
            return seafloor.m_6630_(1 + EntityGiantSquid.this.m_217043_().m_188503_(i));
        }

        public void m_8056_() {
            EntityGiantSquid.this.m_21573_().m_26519_((double)((float)this.moveTo.m_123341_() + 0.5f), (double)((float)this.moveTo.m_123342_() + 0.5f), (double)((float)this.moveTo.m_123343_() + 0.5f), 1.0);
        }

        public boolean m_8045_() {
            return false;
        }
    }
}

