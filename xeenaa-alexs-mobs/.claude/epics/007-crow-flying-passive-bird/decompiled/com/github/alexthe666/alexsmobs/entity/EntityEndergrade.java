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
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.control.MoveControl$Operation
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.FlyingAnimal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EndergradeAIBreakFlowers;
import com.github.alexthe666.alexsmobs.entity.ai.EndergradeAITargetItems;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityEndergrade
extends Animal
implements FlyingAnimal {
    private static final EntityDataAccessor<Integer> BITE_TICK = SynchedEntityData.m_135353_(EntityEndergrade.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.m_135353_(EntityEndergrade.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float tartigradePitch = 0.0f;
    public float prevTartigradePitch = 0.0f;
    public float biteProgress = 0.0f;
    public float prevBiteProgress = 0.0f;
    public boolean stopWandering = false;
    public boolean hasItemTarget = false;

    protected EntityEndergrade(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new MoveHelperController(this);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 20.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, (double)0.15f);
    }

    public static boolean canEndergradeSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return !worldIn.m_8055_(pos.m_7495_()).m_60795_();
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new DirectPathNavigator((Mob)this, worldIn);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Saddled", this.isSaddled());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setSaddled(compound.m_128471_("Saddled"));
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(BITE_TICK, (Object)0);
        this.f_19804_.m_135372_(SADDLED, (Object)false);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(1, (Goal)new EndergradeAIBreakFlowers(this));
        this.f_21345_.m_25352_(2, (Goal)new BreedGoal(this, 1.2){

            public void m_8056_() {
                super.m_8056_();
                EntityEndergrade.this.stopWandering = true;
            }

            public void m_8041_() {
                super.m_8041_();
                EntityEndergrade.this.stopWandering = false;
            }
        });
        this.f_21345_.m_25352_(3, (Goal)new TemptGoal((PathfinderMob)this, 1.1, Ingredient.m_204132_(AMTagRegistry.ENDERGRADE_BREEDABLES), false){

            public void m_8056_() {
                super.m_8056_();
                EntityEndergrade.this.stopWandering = true;
            }

            public void m_8041_() {
                super.m_8041_();
                EntityEndergrade.this.stopWandering = false;
            }
        });
        this.f_21345_.m_25352_(4, (Goal)new RandomFlyGoal(this));
        this.f_21345_.m_25352_(5, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0f));
        this.f_21345_.m_25352_(5, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new EndergradeAITargetItems(this, true));
    }

    @Nullable
    public LivingEntity m_6688_() {
        for (Entity passenger : this.m_20197_()) {
            Player player;
            if (!(passenger instanceof Player) || !(player = (Player)passenger).m_21205_().m_204117_(AMTagRegistry.ENDERGRADE_FOLLOWS) && !player.m_21206_().m_204117_(AMTagRegistry.ENDERGRADE_FOLLOWS)) continue;
            return player;
        }
        return null;
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.ENDERGRADE_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.ENDERGRADE_HURT.get();
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        if (item == Items.f_42450_ && !this.isSaddled()) {
            if (!player.m_7500_()) {
                itemstack.m_41774_(1);
            }
            this.setSaddled(true);
            return InteractionResult.SUCCESS;
        }
        if (itemstack.m_204117_(AMTagRegistry.ENDERGRADE_BREEDABLES) && this.m_21023_((MobEffect)AMEffectRegistry.ENDER_FLU.get())) {
            if (!player.m_7500_()) {
                itemstack.m_41774_(1);
            }
            this.m_5634_(8.0f);
            this.m_21195_((MobEffect)AMEffectRegistry.ENDER_FLU.get());
            return InteractionResult.SUCCESS;
        }
        InteractionResult type = super.m_6071_(player, hand);
        if (type != InteractionResult.SUCCESS && !this.m_6898_(itemstack) && !player.m_6144_() && this.isSaddled()) {
            player.m_20329_((Entity)this);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.ENDERGRADE_BREEDABLES);
    }

    public void m_19956_(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float radius = -0.25f;
            float angle = (float)Math.PI / 180 * this.f_20883_;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            passenger.m_6034_(this.m_20185_() + extraX, this.m_20186_() + this.m_6048_() + passenger.m_6049_(), this.m_20189_() + extraZ);
        }
    }

    public double m_6048_() {
        float f = Math.min(0.25f, this.f_267362_.m_267731_());
        float f1 = this.f_267362_.m_267756_();
        return (double)this.m_20206_() - 0.1 + (double)(0.12f * Mth.m_14089_((float)(f1 * 0.7f)) * 0.7f * f);
    }

    public boolean m_20068_() {
        return true;
    }

    public boolean isSaddled() {
        return (Boolean)this.f_19804_.m_135370_(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.f_19804_.m_135381_(SADDLED, (Object)saddled);
    }

    public void m_8119_() {
        int tick;
        float f2;
        super.m_8119_();
        this.prevTartigradePitch = this.tartigradePitch;
        this.prevBiteProgress = this.biteProgress;
        this.tartigradePitch = f2 = (float)(-((double)((float)this.m_20184_().f_82480_ * 3.0f) * 57.2957763671875));
        if (this.m_20184_().m_82556_() > (double)0.005f) {
            float angleMotion = (float)Math.PI / 180 * this.f_20883_;
            double extraXMotion = -0.2f * Mth.m_14031_((float)((float)(Math.PI + (double)angleMotion)));
            double extraZMotion = -0.2f * Mth.m_14089_((float)angleMotion);
            this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123810_, this.m_20208_(0.5), this.m_20186_() + 0.3, this.m_20262_(0.5), extraXMotion, 0.0, extraZMotion);
        }
        if ((tick = ((Integer)this.f_19804_.m_135370_(BITE_TICK)).intValue()) > 0) {
            this.f_19804_.m_135381_(BITE_TICK, (Object)(tick - 1));
            this.biteProgress += 1.0f;
        } else if (this.biteProgress > 0.0f) {
            this.biteProgress -= 1.0f;
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

    private BlockPos getGroundPosition(BlockPos radialPos) {
        while (radialPos.m_123342_() > 1 && this.m_9236_().m_46859_(radialPos)) {
            radialPos = radialPos.m_7495_();
        }
        if (radialPos.m_123342_() <= 1) {
            return new BlockPos(radialPos.m_123341_(), this.m_9236_().m_5736_(), radialPos.m_123343_());
        }
        return radialPos;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this)).m_6662_() != HitResult.Type.MISS;
    }

    public boolean canTargetItem(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.ENDERGRADE_FOODSTUFFS);
    }

    public void onGetItem(ItemEntity targetEntity) {
        this.m_146850_(GameEvent.f_157806_);
        this.m_5496_(SoundEvents.f_11788_, this.m_6121_(), this.m_6100_());
        this.m_5634_(5.0f);
    }

    public void bite() {
        this.f_19804_.m_135381_(BITE_TICK, (Object)5);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.endergradeSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return (AgeableMob)((EntityType)AMEntityRegistry.ENDERGRADE.get()).m_20615_((Level)p_241840_1_);
    }

    protected void m_5907_() {
        super.m_5907_();
        if (this.isSaddled() && !this.m_9236_().f_46443_) {
            this.m_19998_((ItemLike)Items.f_42450_);
        }
    }

    protected Vec3 m_274312_(Player player, Vec3 deltaIn) {
        if (player.f_20902_ != 0.0f) {
            this.m_6858_(true);
            Vec3 lookVec = player.m_20154_();
            if (player.f_20902_ < 0.0f) {
                lookVec = lookVec.m_82524_((float)Math.PI);
            }
            double y = lookVec.f_82480_ * (double)0.35f;
            return new Vec3((double)player.f_20900_, y, (double)player.f_20902_);
        }
        this.m_6858_(false);
        return Vec3.f_82478_;
    }

    protected void m_274498_(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (player.f_20902_ != 0.0f || player.f_20900_ != 0.0f) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25f);
            this.f_20883_ = this.f_20885_ = this.m_146908_();
            this.f_19859_ = this.f_20885_;
            this.m_274367_(1.0f);
            this.m_21573_().m_26573_();
            this.m_6710_(null);
            this.m_6858_(true);
        }
    }

    protected float m_245547_(Player rider) {
        return (float)(this.m_21133_(Attributes.f_22279_) * (double)(this.m_20096_() ? 0.2f : 0.8f));
    }

    public boolean m_29443_() {
        return true;
    }

    static class MoveHelperController
    extends MoveControl {
        private final EntityEndergrade parentEntity;

        public MoveHelperController(EntityEndergrade sunbird) {
            super((Mob)sunbird);
            this.parentEntity = sunbird;
        }

        public void m_8126_() {
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
                    double localSpeed = this.f_24978_;
                    if (this.parentEntity.m_20160_()) {
                        localSpeed *= 1.5;
                    }
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82549_(vector3d.m_82490_(localSpeed * 0.005 / d0)));
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

    static class RandomFlyGoal
    extends Goal {
        private final EntityEndergrade parentEntity;
        private BlockPos target = null;

        public RandomFlyGoal(EntityEndergrade mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            MoveControl movementcontroller = this.parentEntity.m_21566_();
            if (this.parentEntity.stopWandering || this.parentEntity.hasItemTarget) {
                return false;
            }
            if (!movementcontroller.m_24995_() || this.target == null) {
                this.target = this.getBlockInViewEndergrade();
                if (this.target != null) {
                    this.parentEntity.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                }
                return true;
            }
            return false;
        }

        public boolean m_8045_() {
            return this.target != null && !this.parentEntity.stopWandering && !this.parentEntity.hasItemTarget && this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)this.target)) > 2.4 && this.parentEntity.m_21566_().m_24995_() && !this.parentEntity.f_19862_;
        }

        public void m_8041_() {
            this.target = null;
        }

        public void m_8037_() {
            if (this.target == null) {
                this.target = this.getBlockInViewEndergrade();
            }
            if (this.target != null) {
                this.parentEntity.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)this.target)) < 2.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewEndergrade() {
            float radius = 1 + this.parentEntity.m_217043_().m_188503_(5);
            float neg = this.parentEntity.m_217043_().m_188499_() ? 1.0f : -1.0f;
            float renderYawOffset = this.parentEntity.f_20883_;
            float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.parentEntity.m_217043_().m_188501_() * neg;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos radialPos = AMBlockPos.fromCoords(this.parentEntity.m_20185_() + extraX, this.parentEntity.m_20186_() + 2.0, this.parentEntity.m_20189_() + extraZ);
            BlockPos ground = this.parentEntity.getGroundPosition(radialPos);
            BlockPos newPos = ground.m_6630_(1 + this.parentEntity.m_217043_().m_188503_(6));
            if (!this.parentEntity.isTargetBlocked(Vec3.m_82512_((Vec3i)newPos)) && this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)newPos)) > 6.0) {
                return newPos;
            }
            return null;
        }
    }
}

