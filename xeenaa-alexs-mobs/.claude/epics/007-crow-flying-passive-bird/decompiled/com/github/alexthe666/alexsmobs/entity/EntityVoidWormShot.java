/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.common.ToolActions
 *  net.minecraftforge.network.NetworkHooks
 *  net.minecraftforge.network.PlayMessages$SpawnEntity
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWormPart;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityVoidWormShot
extends Entity {
    private UUID ownerUUID;
    private int ownerNetworkId;
    private boolean leftOwner;
    private static final EntityDataAccessor<Float> STOP_HOMING_PROGRESS = SynchedEntityData.m_135353_(EntityVoidWormShot.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    public float prevStopHomingProgress = 0.0f;
    public static final float HOME_FOR = 40.0f;

    public EntityVoidWormShot(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntityVoidWormShot(Level worldIn, EntityVoidWorm p_i47273_2_) {
        this((EntityType)AMEntityRegistry.VOID_WORM_SHOT.get(), worldIn);
        this.setShooter((Entity)p_i47273_2_);
        this.m_6034_(p_i47273_2_.m_20185_() - (double)(p_i47273_2_.m_20205_() + 1.0f) * 0.35 * (double)Mth.m_14031_((float)(p_i47273_2_.f_20883_ * ((float)Math.PI / 180))), p_i47273_2_.m_20186_() + 1.0, p_i47273_2_.m_20189_() + (double)(p_i47273_2_.m_20205_() + 1.0f) * 0.35 * (double)Mth.m_14089_((float)(p_i47273_2_.f_20883_ * ((float)Math.PI / 180))));
    }

    public EntityVoidWormShot(Level worldIn, LivingEntity p_i47273_2_, boolean right) {
        this((EntityType)AMEntityRegistry.VOID_WORM_SHOT.get(), worldIn);
        this.setShooter((Entity)p_i47273_2_);
        float rot = p_i47273_2_.f_20885_ + (float)(right ? 60 : -60);
        this.m_6034_(p_i47273_2_.m_20185_() - (double)p_i47273_2_.m_20205_() * (double)0.9f * (double)Mth.m_14031_((float)(rot * ((float)Math.PI / 180))), p_i47273_2_.m_20186_() + 1.0, p_i47273_2_.m_20189_() + (double)p_i47273_2_.m_20205_() * 0.9 * (double)Mth.m_14089_((float)(rot * ((float)Math.PI / 180))));
    }

    @OnlyIn(value=Dist.CLIENT)
    public EntityVoidWormShot(Level worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this((EntityType)AMEntityRegistry.VOID_WORM_SHOT.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20334_(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public EntityVoidWormShot(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this((EntityType)AMEntityRegistry.VOID_WORM_SHOT.get(), world);
    }

    protected static float lerpRotation(float p_234614_0_, float p_234614_1_) {
        while (p_234614_1_ - p_234614_0_ < -180.0f) {
            p_234614_0_ -= 360.0f;
        }
        while (p_234614_1_ - p_234614_0_ >= 180.0f) {
            p_234614_0_ += 360.0f;
        }
        return Mth.m_14179_((float)0.2f, (float)p_234614_0_, (float)p_234614_1_);
    }

    public Packet<ClientGamePacketListener> m_5654_() {
        return NetworkHooks.getEntitySpawningPacket((Entity)this);
    }

    public void m_8119_() {
        this.prevStopHomingProgress = this.getStopHomingProgress();
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        if (this.f_19797_ > 400) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.f_19797_ > 40) {
            Entity entity = this.getShooter();
            float stopHomingProgress = this.getStopHomingProgress();
            if (stopHomingProgress < 40.0f) {
                this.setStopHomingProgress(stopHomingProgress += 1.0f);
            }
            float homeScale = 1.0f - stopHomingProgress / 40.0f;
            if (entity instanceof Mob && ((Mob)entity).m_5448_() != null && homeScale > 0.0f) {
                LivingEntity target = ((Mob)entity).m_5448_();
                if (target == null) {
                    this.m_6074_();
                }
                double d0 = target.m_20185_() - this.m_20185_();
                double d1 = target.m_20188_() - this.m_20186_();
                double d2 = target.m_20189_() - this.m_20189_();
                Vec3 vec = new Vec3(d0, d1, d2).m_82541_().m_82490_((double)(Math.max(homeScale, 0.5f) * 1.2f));
                this.m_20256_(vec);
            } else {
                this.m_20256_(this.m_20184_().m_82520_(0.0, -0.09, 0.0));
            }
        }
        super.m_8119_();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.m_278158_((Entity)this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.m_6662_() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        double d0 = this.m_20185_() + vector3d.f_82479_;
        double d1 = this.m_20186_() + vector3d.f_82480_;
        double d2 = this.m_20189_() + vector3d.f_82481_;
        this.m_20242_(true);
        this.updateRotation();
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_)) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else if (this.m_20072_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            this.m_20256_(vector3d.m_82490_((double)0.99f));
            this.m_6034_(d0, d1, d2);
        }
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        Player player;
        boolean b;
        Entity entity = this.getShooter();
        if (entity instanceof LivingEntity && !(p_213868_1_.m_82443_() instanceof EntityVoidWorm) && !(p_213868_1_.m_82443_() instanceof EntityVoidWormPart) && (b = this.wormAttack(p_213868_1_.m_82443_(), this.m_269291_().m_269299_((Entity)this, (LivingEntity)entity), (float)(AMConfig.voidWormDamageModifier * 4.0))) && p_213868_1_.m_82443_() instanceof Player && (player = (Player)p_213868_1_.m_82443_()).m_21211_().canPerformAction(ToolActions.SHIELD_BLOCK)) {
            player.m_36384_(true);
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    private boolean wormAttack(Entity entity, DamageSource source, float dmg) {
        return entity.m_6469_(source, dmg);
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().m_8055_(p_230299_1_.m_82425_());
        if (!this.m_9236_().f_46443_) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    protected void m_8097_() {
        this.f_19804_.m_135372_(STOP_HOMING_PROGRESS, (Object)Float.valueOf(0.0f));
    }

    public float getStopHomingProgress() {
        return ((Float)this.f_19804_.m_135370_(STOP_HOMING_PROGRESS)).floatValue();
    }

    public void setStopHomingProgress(float progress) {
        this.f_19804_.m_135381_(STOP_HOMING_PROGRESS, (Object)Float.valueOf(progress));
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.m_20148_();
            this.ownerNetworkId = entityIn.m_19879_();
        }
    }

    @Nullable
    public Entity getShooter() {
        if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            return ((ServerLevel)this.m_9236_()).m_8791_(this.ownerUUID);
        }
        return this.ownerNetworkId != 0 ? this.m_9236_().m_6815_(this.ownerNetworkId) : null;
    }

    protected void m_7380_(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.m_128362_("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            compound.m_128379_("LeftOwner", true);
        }
        compound.m_128350_("HomeTime", this.getStopHomingProgress());
    }

    protected void m_7378_(CompoundTag compound) {
        if (compound.m_128403_("Owner")) {
            this.ownerUUID = compound.m_128342_("Owner");
        }
        this.setStopHomingProgress(compound.m_128457_("HomeTime"));
        this.leftOwner = compound.m_128471_("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getShooter();
        if (entity != null) {
            for (Entity entity1 : this.m_9236_().m_6249_((Entity)this, this.m_20191_().m_82369_(this.m_20184_()).m_82400_(1.0), p_234613_0_ -> !p_234613_0_.m_5833_() && p_234613_0_.m_6087_())) {
                if (entity1.m_20201_() != entity.m_20201_()) continue;
                return false;
            }
        }
        return true;
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = new Vec3(x, y, z).m_82541_().m_82520_(this.f_19796_.m_188583_() * (double)0.0075f * (double)inaccuracy, this.f_19796_.m_188583_() * (double)0.0075f * (double)inaccuracy, this.f_19796_.m_188583_() * (double)0.0075f * (double)inaccuracy).m_82490_((double)velocity);
        this.m_20256_(this.m_20184_().m_82549_(vector3d));
        float f = Mth.m_14116_((float)((float)vector3d.m_165925_()));
        this.m_146922_((float)(Mth.m_14136_((double)vector3d.f_82479_, (double)vector3d.f_82481_) * 57.2957763671875));
        this.m_146926_((float)(Mth.m_14136_((double)vector3d.f_82480_, (double)f) * 57.2957763671875));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.m_6662_();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult)result);
        }
        this.m_146850_(GameEvent.f_223707_);
        this.m_5496_(SoundEvents.f_11983_, 1.0f, 0.5f);
        Entity entity = this.getShooter();
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_6001_(double x, double y, double z) {
        this.m_20334_(x, y, z);
        if (this.f_19860_ == 0.0f && this.f_19859_ == 0.0f) {
            float f = Mth.m_14116_((float)((float)(x * x + z * z)));
            this.m_146926_((float)(Mth.m_14136_((double)y, (double)f) * 57.2957763671875));
            this.m_146922_((float)(Mth.m_14136_((double)x, (double)z) * 57.2957763671875));
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        if (!p_230298_1_.m_5833_() && p_230298_1_.m_6084_() && p_230298_1_.m_6087_()) {
            Entity entity = this.getShooter();
            return (entity == null || this.leftOwner || !entity.m_20365_(p_230298_1_)) && !(p_230298_1_ instanceof EntityVoidWormShot) && !(p_230298_1_ instanceof EntityVoidWormPart);
        }
        return false;
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.m_14116_((float)((float)vector3d.m_165924_()));
        this.m_146926_(EntityVoidWormShot.lerpRotation(this.f_19860_, (float)(Mth.m_14136_((double)vector3d.f_82480_, (double)f) * 57.2957763671875)));
        this.m_146922_(EntityVoidWormShot.lerpRotation(this.f_19859_, (float)(Mth.m_14136_((double)vector3d.f_82479_, (double)vector3d.f_82481_) * 57.2957763671875)));
    }
}

