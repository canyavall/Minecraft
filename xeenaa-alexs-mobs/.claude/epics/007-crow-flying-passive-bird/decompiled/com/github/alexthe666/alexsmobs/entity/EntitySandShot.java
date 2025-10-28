/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.network.NetworkHooks
 *  net.minecraftforge.network.PlayMessages$SpawnEntity
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityGuster;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntitySandShot
extends Entity {
    private UUID ownerUUID;
    private int ownerNetworkId;
    private boolean leftOwner;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.m_135353_(EntitySandShot.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);

    public EntitySandShot(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntitySandShot(Level worldIn, EntityGuster p_i47273_2_) {
        this((EntityType)AMEntityRegistry.SAND_SHOT.get(), worldIn);
        this.setShooter((Entity)p_i47273_2_);
        this.m_6034_(p_i47273_2_.m_20185_() - (double)(p_i47273_2_.m_20205_() + 1.0f) * 0.35 * (double)Mth.m_14031_((float)(p_i47273_2_.f_20883_ * ((float)Math.PI / 180))), p_i47273_2_.m_20188_() + (double)0.2f, p_i47273_2_.m_20189_() + (double)(p_i47273_2_.m_20205_() + 1.0f) * 0.35 * (double)Mth.m_14089_((float)(p_i47273_2_.f_20883_ * ((float)Math.PI / 180))));
    }

    public EntitySandShot(Level worldIn, LivingEntity p_i47273_2_, boolean right) {
        this((EntityType)AMEntityRegistry.SAND_SHOT.get(), worldIn);
        this.setShooter((Entity)p_i47273_2_);
        float rot = p_i47273_2_.f_20885_ + (float)(right ? 60 : -60);
        this.m_6034_(p_i47273_2_.m_20185_() - (double)p_i47273_2_.m_20205_() * 0.5 * (double)Mth.m_14031_((float)(rot * ((float)Math.PI / 180))), p_i47273_2_.m_20188_() - (double)0.2f, p_i47273_2_.m_20189_() + (double)p_i47273_2_.m_20205_() * 0.5 * (double)Mth.m_14089_((float)(rot * ((float)Math.PI / 180))));
    }

    @OnlyIn(value=Dist.CLIENT)
    public EntitySandShot(Level worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this((EntityType)AMEntityRegistry.SAND_SHOT.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20334_(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public EntitySandShot(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this((EntityType)AMEntityRegistry.SAND_SHOT.get(), world);
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

    public int getVariant() {
        return (Integer)this.f_19804_.m_135370_(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.m_135381_(VARIANT, (Object)variant);
    }

    public Packet<ClientGamePacketListener> m_5654_() {
        return NetworkHooks.getEntitySpawningPacket((Entity)this);
    }

    public void m_8119_() {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        ParticleOptions type = this.getVariant() == 2 ? (ParticleOptions)AMParticleRegistry.GUSTER_SAND_SHOT_SOUL.get() : (this.getVariant() == 1 ? (ParticleOptions)AMParticleRegistry.GUSTER_SAND_SHOT_RED.get() : (ParticleOptions)AMParticleRegistry.GUSTER_SAND_SHOT.get());
        for (int i = 0; i < 3 + this.f_19796_.m_188503_(6); ++i) {
            double d0 = 0.1 + 0.3 * (double)i;
            this.m_9236_().m_7106_(type, this.m_20185_() + (double)(0.25f * (this.f_19796_.m_188501_() - 0.5f)), this.m_20186_() + (double)(0.25f * (this.f_19796_.m_188501_() - 0.5f)), this.m_20189_() + (double)(0.25f * (this.f_19796_.m_188501_() - 0.5f)), this.m_20184_().f_82479_ * d0, this.m_20184_().f_82480_, this.m_20184_().f_82481_ * d0);
        }
        super.m_8119_();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.m_278158_((Entity)this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.m_6662_() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        this.updateRotation();
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_)) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else if (this.m_20072_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            double d0 = this.m_20185_() + vector3d.f_82479_;
            double d1 = this.m_20186_() + vector3d.f_82480_;
            double d2 = this.m_20189_() + vector3d.f_82481_;
            this.m_20256_(vector3d.m_82490_((double)0.99f));
            this.m_20256_(this.m_20184_().m_82520_(0.0, (double)-0.03f, 0.0));
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().m_82520_(0.0, (double)-0.03f, 0.0));
            }
            this.m_6034_(d0, d1, d2);
        }
    }

    protected void onEntityHit(EntityHitResult p_213868_1_) {
        Entity entity = this.getSandShooter();
        if (entity instanceof LivingEntity) {
            p_213868_1_.m_82443_().m_6469_(this.m_269291_().m_269299_((Entity)this, (LivingEntity)entity), 2.5f);
        }
        if (entity instanceof Player && p_213868_1_.m_82443_() instanceof LivingEntity) {
            ((LivingEntity)p_213868_1_.m_82443_()).m_7292_(new MobEffectInstance(MobEffects.f_19610_, 100, 0, true, false));
        }
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().m_8055_(p_230299_1_.m_82425_());
        if (!this.m_9236_().f_46443_) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    protected void m_8097_() {
        this.f_19804_.m_135372_(VARIANT, (Object)0);
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.m_20148_();
            this.ownerNetworkId = entityIn.m_19879_();
        }
    }

    @Nullable
    public Entity getSandShooter() {
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
    }

    protected void m_7378_(CompoundTag compound) {
        if (compound.m_128403_("Owner")) {
            this.ownerUUID = compound.m_128342_("Owner");
        }
        this.leftOwner = compound.m_128471_("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getSandShooter();
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
        this.m_20256_(vector3d);
        float f = Mth.m_14116_((float)((float)vector3d.m_165925_()));
        this.m_146922_((float)(Mth.m_14136_((double)vector3d.f_82479_, (double)vector3d.f_82481_) * 57.2957763671875));
        this.m_146926_((float)(Mth.m_14136_((double)vector3d.f_82480_, (double)f) * 57.2957763671875));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -Mth.m_14031_((float)(p_234612_3_ * ((float)Math.PI / 180))) * Mth.m_14089_((float)(p_234612_2_ * ((float)Math.PI / 180)));
        float f1 = -Mth.m_14031_((float)((p_234612_2_ + p_234612_4_) * ((float)Math.PI / 180)));
        float f2 = Mth.m_14089_((float)(p_234612_3_ * ((float)Math.PI / 180))) * Mth.m_14089_((float)(p_234612_2_ * ((float)Math.PI / 180)));
        this.shoot(f, f1, f2, p_234612_5_, p_234612_6_);
        Vec3 vector3d = p_234612_1_.m_20184_();
        this.m_20256_(this.m_20184_().m_82520_(vector3d.f_82479_, p_234612_1_.m_20096_() ? 0.0 : vector3d.f_82480_, vector3d.f_82481_));
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.m_6662_();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult)result);
        }
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
            Entity entity = this.getSandShooter();
            return entity == null || this.leftOwner || !entity.m_20365_(p_230298_1_);
        }
        return false;
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.m_14116_((float)((float)vector3d.m_165925_()));
        this.m_146926_(EntitySandShot.lerpRotation(this.f_19860_, (float)(Mth.m_14136_((double)vector3d.f_82480_, (double)f) * 57.2957763671875)));
        this.m_146922_(EntitySandShot.lerpRotation(this.f_19859_, (float)(Mth.m_14136_((double)vector3d.f_82479_, (double)vector3d.f_82481_) * 57.2957763671875)));
    }
}

