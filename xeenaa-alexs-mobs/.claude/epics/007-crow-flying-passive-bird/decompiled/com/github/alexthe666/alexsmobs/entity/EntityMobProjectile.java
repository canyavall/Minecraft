/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.network.NetworkHooks
 */
package com.github.alexthe666.alexsmobs.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityMobProjectile
extends Entity {
    private UUID ownerUUID;
    private int ownerNetworkId;
    private boolean leftOwner;

    public EntityMobProjectile(EntityType type, Level level) {
        super(type, level);
    }

    public EntityMobProjectile(EntityType type, Level worldIn, Mob shooter) {
        this(type, worldIn);
        this.setShooter((Entity)shooter);
    }

    protected Vec3 calcOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.m_82496_(xRot * ((float)Math.PI / 180)).m_82524_(-yRot * ((float)Math.PI / 180));
    }

    protected static float lerpRotation(float f, float f1) {
        while (f1 - f < -180.0f) {
            f -= 360.0f;
        }
        while (f1 - f >= 180.0f) {
            f += 360.0f;
        }
        return Mth.m_14179_((float)0.2f, (float)f, (float)f1);
    }

    public Packet<ClientGamePacketListener> m_5654_() {
        return NetworkHooks.getEntitySpawningPacket((Entity)this);
    }

    protected void m_8097_() {
    }

    public void m_8119_() {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        this.doBehavior();
        super.m_8119_();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.m_278158_((Entity)this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.m_6662_() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        double d0 = this.m_20185_() + vector3d.f_82479_;
        double d1 = this.m_20186_() + vector3d.f_82480_;
        double d2 = this.m_20189_() + vector3d.f_82481_;
        this.updateRotation();
        if (this.m_5830_() && (!this.m_20069_() || this.removeInWater())) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else if (this.m_20072_() && this.removeInWater()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            this.m_20256_(vector3d.m_82490_((double)0.99f));
            this.m_6034_(d0, d1, d2);
        }
    }

    protected boolean removeInWater() {
        return true;
    }

    public abstract void doBehavior();

    protected void onEntityHit(EntityHitResult result) {
        Entity entity = this.getShooter();
        if (entity instanceof LivingEntity) {
            boolean bl = result.m_82443_().m_6469_(this.m_269291_().m_269299_((Entity)this, (LivingEntity)entity), this.getDamage());
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    protected abstract float getDamage();

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().m_8055_(p_230299_1_.m_82425_());
        if (!this.m_9236_().f_46443_) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Nullable
    public Entity getShooter() {
        if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            return ((ServerLevel)this.m_9236_()).m_8791_(this.ownerUUID);
        }
        return this.ownerNetworkId != 0 ? this.m_9236_().m_6815_(this.ownerNetworkId) : null;
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.m_20148_();
            this.ownerNetworkId = entityIn.m_19879_();
        }
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

    protected boolean canHitEntity(Entity inQuestion) {
        if (!inQuestion.m_5833_() && inQuestion.m_6084_() && inQuestion.m_6087_()) {
            Entity entity = this.getShooter();
            return !(entity != null && !this.leftOwner && entity.m_20365_(inQuestion) || entity != null && inQuestion != null && this.isSameTeam(entity, inQuestion));
        }
        return false;
    }

    public boolean isSameTeam(Entity shooter, Entity entity) {
        TamableAnimal tamableAnimal;
        if (shooter instanceof TamableAnimal && (tamableAnimal = (TamableAnimal)shooter).m_21824_()) {
            TamableAnimal alsoTameable;
            if (entity instanceof TamableAnimal && (alsoTameable = (TamableAnimal)entity).m_21824_() && alsoTameable.m_21805_() != null && tamableAnimal.m_21805_() != null && tamableAnimal.m_21805_().equals(alsoTameable.m_21805_())) {
                return true;
            }
            return tamableAnimal.m_21805_() != null && tamableAnimal.m_21805_().equals(entity.m_20148_()) || shooter.m_7307_(entity);
        }
        return shooter.m_7307_(entity);
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.m_14116_((float)((float)vector3d.m_165924_()));
        this.m_146926_(EntityMobProjectile.lerpRotation(this.f_19860_, (float)(Mth.m_14136_((double)vector3d.f_82480_, (double)f) * 57.2957763671875)));
        this.m_146922_(EntityMobProjectile.lerpRotation(this.f_19859_, (float)(Mth.m_14136_((double)vector3d.f_82479_, (double)vector3d.f_82481_) * 57.2957763671875)));
    }
}

