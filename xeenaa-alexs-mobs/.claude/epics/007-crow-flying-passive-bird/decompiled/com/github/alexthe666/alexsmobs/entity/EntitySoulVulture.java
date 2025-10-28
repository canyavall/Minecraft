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
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
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
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.animal.FlyingAnimal
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.piglin.AbstractPiglin
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntitySoulVulture
extends Monster
implements FlyingAnimal {
    public static final ResourceLocation SOUL_LOOT = new ResourceLocation("alexsmobs", "entities/soul_vulture_heart");
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.m_135353_(EntitySoulVulture.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> TACKLING = SynchedEntityData.m_135353_(EntitySoulVulture.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Optional<BlockPos>> PERCH_POS = SynchedEntityData.m_135353_(EntitySoulVulture.class, (EntityDataSerializer)EntityDataSerializers.f_135039_);
    private static final EntityDataAccessor<Integer> SOUL_LEVEL = SynchedEntityData.m_135353_(EntitySoulVulture.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    public float prevFlyProgress;
    public float flyProgress;
    public float prevTackleProgress;
    public float tackleProgress;
    private boolean isLandNavigator;
    private int perchSearchCooldown = 0;
    private int landingCooldown = 0;
    private int tackleCooldown = 0;

    protected EntitySoulVulture(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.switchNavigator(true);
    }

    public MobType m_6336_() {
        return MobType.f_21641_;
    }

    @Nullable
    protected ResourceLocation m_7582_() {
        return this.hasSoulHeart() ? SOUL_LOOT : super.m_7582_();
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.soulVultureSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canVultureSpawn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.m_7495_();
        boolean spawnBlock = worldIn.m_8055_(blockpos).m_204336_(AMTagRegistry.SOUL_VULTURE_SPAWNS);
        return reason == MobSpawnType.SPAWNER || spawnBlock && EntitySoulVulture.m_217057_((EntityType)((EntityType)AMEntityRegistry.SOUL_VULTURE.get()), (LevelAccessor)worldIn, (MobSpawnType)reason, (BlockPos)pos, (RandomSource)randomIn);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.SOUL_VULTURE_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.SOUL_VULTURE_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.SOUL_VULTURE_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 12.0).m_22268_(Attributes.f_22277_, 18.0).m_22268_(Attributes.f_22281_, 4.0).m_22268_(Attributes.f_22279_, 0.25);
    }

    public boolean isPerchBlock(BlockPos pos, BlockState state) {
        return this.m_9236_().m_46859_(pos.m_7494_()) && this.m_9236_().m_46859_(pos.m_6630_(2)) && state.m_204336_(AMTagRegistry.SOUL_VULTURE_PERCHES);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(1, (Goal)new AICirclePerch(this));
        this.f_21345_.m_25352_(2, (Goal)new AIFlyRandom(this));
        this.f_21345_.m_25352_(3, (Goal)new AITackleMelee(this));
        this.f_21345_.m_25352_(4, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 20.0f));
        this.f_21345_.m_25352_(5, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[]{EntitySoulVulture.class}));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<Player>((Mob)this, Player.class, true));
        this.f_21346_.m_25352_(2, new EntityAINearestTarget3D<AbstractPiglin>((Mob)this, AbstractPiglin.class, true));
        this.f_21346_.m_25352_(3, new EntityAINearestTarget3D<AbstractVillager>((Mob)this, AbstractVillager.class, true));
    }

    public int m_5792_() {
        return 1;
    }

    public boolean m_7296_(int sizeIn) {
        return true;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = new GroundPathNavigatorWide((Mob)this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new MoveHelper(this);
            this.f_21344_ = new DirectPathNavigator((Mob)this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(FLYING, (Object)false);
        this.f_19804_.m_135372_(TACKLING, (Object)false);
        this.f_19804_.m_135372_(PERCH_POS, Optional.empty());
        this.f_19804_.m_135372_(SOUL_LEVEL, (Object)0);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Flying", this.m_29443_());
        if (this.getPerchPos() != null) {
            compound.m_128405_("PerchX", this.getPerchPos().m_123341_());
            compound.m_128405_("PerchY", this.getPerchPos().m_123342_());
            compound.m_128405_("PerchZ", this.getPerchPos().m_123343_());
        }
        compound.m_128405_("SoulLevel", this.getSoulLevel());
        compound.m_128405_("LandingCooldown", this.landingCooldown);
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFlying(compound.m_128471_("Flying"));
        this.setSoulLevel(compound.m_128451_("SoulLevel"));
        this.landingCooldown = compound.m_128451_("LandingCooldown");
        if (compound.m_128441_("PerchX") && compound.m_128441_("PerchY") && compound.m_128441_("PerchZ")) {
            this.setPerchPos(new BlockPos(compound.m_128451_("PerchX"), compound.m_128451_("PerchY"), compound.m_128451_("PerchZ")));
        }
    }

    public boolean m_29443_() {
        return (Boolean)this.f_19804_.m_135370_(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.m_135381_(FLYING, (Object)flying);
    }

    public boolean isTackling() {
        return (Boolean)this.f_19804_.m_135370_(TACKLING);
    }

    public void setTackling(boolean tackling) {
        this.f_19804_.m_135381_(TACKLING, (Object)tackling);
    }

    public BlockPos getPerchPos() {
        return ((Optional)this.f_19804_.m_135370_(PERCH_POS)).orElse(null);
    }

    public void setPerchPos(BlockPos pos) {
        this.f_19804_.m_135381_(PERCH_POS, Optional.ofNullable(pos));
    }

    public int getSoulLevel() {
        return (Integer)this.f_19804_.m_135370_(SOUL_LEVEL);
    }

    public void setSoulLevel(int tackling) {
        this.f_19804_.m_135381_(SOUL_LEVEL, (Object)tackling);
    }

    public void m_8119_() {
        boolean flying;
        super.m_8119_();
        this.prevTackleProgress = this.tackleProgress;
        this.prevFlyProgress = this.flyProgress;
        if (!this.m_9236_().f_46443_) {
            if (this.perchSearchCooldown > 0) {
                --this.perchSearchCooldown;
            }
            if (this.m_5448_() != null && this.m_5448_().m_6084_()) {
                this.setPerchPos(this.m_5448_().m_20183_().m_6630_(7));
            } else if (this.getPerchPos() != null && !this.isPerchBlock(this.getPerchPos(), this.m_9236_().m_8055_(this.getPerchPos()))) {
                this.setPerchPos(null);
            }
            if (this.getPerchPos() == null && this.perchSearchCooldown == 0) {
                this.perchSearchCooldown = 20 + this.f_19796_.m_188503_(20);
                this.setPerchPos(this.findNewPerchPos());
            }
            if (!this.m_29443_() && this.landingCooldown == 0 && (this.getPerchPos() == null || this.shouldLeavePerch(this.getPerchPos()))) {
                this.setFlying(true);
            }
            if (!this.m_29443_() && this.m_5448_() != null) {
                this.setFlying(true);
            }
            if (this.landingCooldown > 0 && this.m_29443_() && this.m_20096_() && this.m_5448_() == null) {
                this.setFlying(false);
            }
        }
        if (flying = this.m_29443_()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (this.flyProgress < 5.0f) {
                this.flyProgress += 1.0f;
            }
        } else {
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.flyProgress > 0.0f) {
                this.flyProgress -= 1.0f;
            }
        }
        if (this.isTackling()) {
            if (this.tackleProgress < 5.0f) {
                this.tackleProgress += 1.0f;
            }
        } else if (this.tackleProgress > 0.0f) {
            this.tackleProgress -= 1.0f;
        }
        if (this.landingCooldown > 0) {
            --this.landingCooldown;
        }
        if (this.tackleCooldown > 0) {
            --this.tackleCooldown;
        }
        if (this.m_29443_()) {
            this.m_20242_(true);
        } else {
            this.m_20242_(false);
        }
        if (this.m_9236_().f_46443_ && this.hasSoulHeart()) {
            float radius = 0.25f + this.f_19796_.m_188501_() * 1.0f;
            float fly = this.flyProgress * 0.2f;
            float wingSpread = 15.0f + 65.0f * fly + (float)this.f_19796_.m_188503_(5);
            float angle = (float)Math.PI / 180 * ((float)(this.f_19796_.m_188499_() ? -1 : 1) * (wingSpread + 180.0f) + this.f_20883_);
            float angleMotion = (float)Math.PI / 180 * this.f_20883_;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            double mov = this.m_20184_().m_82553_();
            double extraXMotion = -mov * (double)Mth.m_14031_((float)((float)(Math.PI + (double)angleMotion)));
            double extraZMotion = -mov * (double)Mth.m_14089_((float)angleMotion);
            double yRandom = 0.2f + this.f_19796_.m_188501_() * 0.3f;
            this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123745_, this.m_20185_() + extraX, this.m_20186_() + yRandom, this.m_20189_() + extraZ, extraXMotion, (double)(this.f_19796_.m_188501_() * 0.1f), extraZMotion);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 68) {
            for (int i = 0; i < 6 + this.f_19796_.m_188503_(3); ++i) {
                double d2 = this.f_19796_.m_188583_() * 0.02;
                double d0 = this.f_19796_.m_188583_() * 0.02;
                double d1 = this.f_19796_.m_188583_() * 0.02;
                this.m_9236_().m_7106_((ParticleOptions)ParticleTypes.f_123746_, this.m_20185_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, this.m_20186_() + (double)(this.m_20206_() * 0.5f) + (double)(this.f_19796_.m_188501_() * this.m_20206_() * 0.5f), this.m_20189_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, d0, d1, d2);
            }
        } else {
            super.m_7822_(id);
        }
    }

    public BlockPos findNewPerchPos() {
        BlockState beneathState = this.m_9236_().m_8055_(this.m_20099_());
        if (this.isPerchBlock(this.m_20099_(), beneathState)) {
            return this.m_20099_();
        }
        BlockPos blockpos = null;
        Random random = new Random();
        int range = 14;
        for (int i = 0; i < 15; ++i) {
            BlockPos blockpos1 = this.m_20183_().m_7918_(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
            while (this.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.m_7495_();
            }
            if (!this.isPerchBlock(blockpos1, this.m_9236_().m_8055_(blockpos1))) continue;
            blockpos = blockpos1;
        }
        return blockpos;
    }

    private boolean shouldLeavePerch(BlockPos perchPos) {
        return this.m_20238_(Vec3.m_82512_((Vec3i)perchPos)) > 13.0 || this.landingCooldown == 0;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean shouldSwoop() {
        return this.m_5448_() != null && this.tackleCooldown == 0;
    }

    public boolean hasSoulHeart() {
        return this.getSoulLevel() > 2;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this)).m_6662_() != HitResult.Type.MISS;
    }

    class AICirclePerch
    extends Goal {
        private final EntitySoulVulture vulture;
        float speed = 1.0f;
        float circlingTime = 0.0f;
        float circleDistance = 5.0f;
        float maxCirclingTime = 80.0f;
        boolean clockwise = false;
        private BlockPos targetPos;
        private int yLevel = 1;

        public AICirclePerch(EntitySoulVulture vulture) {
            this.vulture = vulture;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            return !this.vulture.shouldSwoop() && this.vulture.m_29443_() && this.vulture.getPerchPos() != null;
        }

        public void m_8056_() {
            this.circlingTime = 0.0f;
            this.speed = 0.8f + EntitySoulVulture.this.f_19796_.m_188501_() * 0.4f;
            this.yLevel = this.vulture.f_19796_.m_188503_(3);
            this.maxCirclingTime = 360 + this.vulture.f_19796_.m_188503_(80);
            this.circleDistance = 5.0f + this.vulture.f_19796_.m_188501_() * 5.0f;
            this.clockwise = this.vulture.f_19796_.m_188499_();
        }

        public void m_8041_() {
            this.circlingTime = 0.0f;
            this.speed = 0.8f + EntitySoulVulture.this.f_19796_.m_188501_() * 0.4f;
            this.yLevel = this.vulture.f_19796_.m_188503_(3);
            this.maxCirclingTime = 360 + this.vulture.f_19796_.m_188503_(80);
            this.circleDistance = 5.0f + this.vulture.f_19796_.m_188501_() * 5.0f;
            this.clockwise = this.vulture.f_19796_.m_188499_();
            this.vulture.tackleCooldown = 0;
        }

        public void m_8037_() {
            BlockPos encircle = this.vulture.getPerchPos();
            double localSpeed = this.speed;
            if (this.vulture.m_5448_() != null) {
                localSpeed *= 1.55;
            }
            if (encircle != null) {
                this.circlingTime += 1.0f;
                if (this.circlingTime > 360.0f) {
                    this.vulture.m_21566_().m_6849_((double)encircle.m_123341_() + 0.5, (double)encircle.m_123342_() + 1.1, (double)encircle.m_123343_() + 0.5, localSpeed);
                    if (this.vulture.f_19863_ || this.vulture.m_20275_((double)encircle.m_123341_() + 0.5, (double)encircle.m_123342_() + 1.1, (double)encircle.m_123343_() + 0.5) < 1.0) {
                        this.vulture.setFlying(false);
                        this.vulture.m_20256_(Vec3.f_82478_);
                        this.vulture.landingCooldown = 400 + EntitySoulVulture.this.f_19796_.m_188503_(1200);
                        this.m_8041_();
                    }
                } else {
                    BlockPos circlePos = this.getVultureCirclePos(encircle);
                    if (circlePos != null) {
                        this.vulture.m_21566_().m_6849_((double)circlePos.m_123341_() + 0.5, (double)circlePos.m_123342_() + 0.5, (double)circlePos.m_123343_() + 0.5, localSpeed);
                    }
                }
            }
        }

        public boolean m_8045_() {
            return this.m_8036_();
        }

        public BlockPos getVultureCirclePos(BlockPos target) {
            float angle = 0.05235988f * (this.clockwise ? -this.circlingTime : this.circlingTime);
            double extraX = this.circleDistance * Mth.m_14031_((float)angle);
            double extraZ = this.circleDistance * Mth.m_14089_((float)angle);
            BlockPos pos = new BlockPos((int)((double)target.m_123341_() + extraX), target.m_123342_() + 1 + this.yLevel, (int)((double)target.m_123343_() + extraZ));
            if (this.vulture.m_9236_().m_46859_(pos)) {
                return pos;
            }
            return null;
        }
    }

    private class AIFlyRandom
    extends Goal {
        private final EntitySoulVulture vulture;
        private BlockPos target = null;

        public AIFlyRandom(EntitySoulVulture vulture) {
            this.vulture = vulture;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            if (this.vulture.getPerchPos() != null || this.vulture.shouldSwoop()) {
                return false;
            }
            MoveControl movementcontroller = this.vulture.m_21566_();
            if (!movementcontroller.m_24995_() || this.target == null) {
                this.target = this.getBlockInViewVulture();
                if (this.target != null) {
                    this.vulture.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                }
                return true;
            }
            return false;
        }

        public boolean m_8045_() {
            if (this.vulture.getPerchPos() != null || this.vulture.shouldSwoop()) {
                return false;
            }
            return this.target != null && this.vulture.m_20238_(Vec3.m_82512_((Vec3i)this.target)) > 2.4 && this.vulture.m_21566_().m_24995_() && !this.vulture.f_19862_;
        }

        public void m_8041_() {
            this.target = null;
        }

        public void m_8037_() {
            if (this.target == null) {
                this.target = this.getBlockInViewVulture();
            }
            if (this.target != null) {
                this.vulture.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                if (this.vulture.m_20238_(Vec3.m_82512_((Vec3i)this.target)) < 2.5) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewVulture() {
            float radius = -9.45f - (float)this.vulture.m_217043_().m_188503_(10);
            float neg = this.vulture.m_217043_().m_188499_() ? 1.0f : -1.0f;
            float renderYawOffset = this.vulture.f_20883_;
            float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.vulture.m_217043_().m_188501_() * neg;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos radialPos = new BlockPos((int)(this.vulture.m_20185_() + extraX), (int)this.vulture.m_20186_(), (int)(this.vulture.m_20189_() + extraZ));
            while (EntitySoulVulture.this.m_9236_().m_46859_(radialPos) && radialPos.m_123342_() > 2) {
                radialPos = radialPos.m_7495_();
            }
            BlockPos newPos = radialPos.m_6630_(this.vulture.m_20186_() - (double)radialPos.m_123342_() > 16.0 ? 4 : this.vulture.m_217043_().m_188503_(5) + 5);
            if (!this.vulture.isTargetBlocked(Vec3.m_82512_((Vec3i)newPos)) && this.vulture.m_20238_(Vec3.m_82512_((Vec3i)newPos)) > 6.0) {
                return newPos;
            }
            return null;
        }
    }

    private class AITackleMelee
    extends Goal {
        private final EntitySoulVulture vulture;

        public AITackleMelee(EntitySoulVulture vulture) {
            this.vulture = vulture;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            if (this.vulture.m_5448_() != null && this.vulture.shouldSwoop()) {
                this.vulture.setFlying(true);
                return true;
            }
            return false;
        }

        public void m_8041_() {
            this.vulture.setTackling(false);
        }

        public void m_8037_() {
            if (this.vulture.m_29443_()) {
                this.vulture.setTackling(true);
            } else {
                this.vulture.setTackling(false);
            }
            if (this.vulture.m_5448_() != null) {
                this.vulture.m_21566_().m_6849_(this.vulture.m_5448_().m_20185_(), this.vulture.m_5448_().m_20186_() + (double)this.vulture.m_5448_().m_20192_(), this.vulture.m_5448_().m_20189_(), 2.0);
                double d0 = this.vulture.m_20185_() - this.vulture.m_5448_().m_20185_();
                double d2 = this.vulture.m_20189_() - this.vulture.m_5448_().m_20189_();
                float f = (float)(Mth.m_14136_((double)d2, (double)d0) * 57.2957763671875) - 90.0f;
                this.vulture.m_146922_(f);
                this.vulture.f_20883_ = this.vulture.m_146908_();
                if (this.vulture.m_20191_().m_82377_((double)0.3f, (double)0.3f, (double)0.3f).m_82381_(this.vulture.m_5448_().m_20191_()) && this.vulture.tackleCooldown == 0) {
                    EntitySoulVulture.this.tackleCooldown = 100 + EntitySoulVulture.this.f_19796_.m_188503_(200);
                    float dmg = (float)this.vulture.m_21051_(Attributes.f_22281_).m_22135_();
                    if (this.vulture.m_5448_().m_6469_(this.vulture.m_269291_().m_269333_((LivingEntity)this.vulture), dmg) && this.vulture.m_21223_() < this.vulture.m_21233_() - dmg && this.vulture.getSoulLevel() < 5) {
                        this.vulture.setSoulLevel(this.vulture.getSoulLevel() + 1);
                        this.vulture.m_5634_(dmg);
                        this.vulture.m_9236_().m_7605_((Entity)this.vulture, (byte)68);
                    }
                    this.m_8041_();
                }
            }
        }
    }

    static class MoveHelper
    extends MoveControl {
        private final EntitySoulVulture parentEntity;

        public MoveHelper(EntitySoulVulture bird) {
            super((Mob)bird);
            this.parentEntity = bird;
        }

        public void m_8126_() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.m_82553_();
                if (d5 < 0.3) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82490_(0.5));
                } else {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82549_(vector3d.m_82490_(this.f_24978_ * 0.05 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float)Mth.m_14136_((double)vector3d1.f_82479_, (double)vector3d1.f_82481_)) * 57.295776f);
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
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
}

