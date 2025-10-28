/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDropBear
extends Monster
implements IAnimatedEntity {
    public static final Animation ANIMATION_BITE = Animation.create((int)9);
    public static final Animation ANIMATION_SWIPE_R = Animation.create((int)15);
    public static final Animation ANIMATION_SWIPE_L = Animation.create((int)15);
    public static final Animation ANIMATION_JUMPUP = Animation.create((int)20);
    private static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.m_135353_(EntityDropBear.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float prevUpsideDownProgress;
    public float upsideDownProgress;
    public boolean fallRotation;
    private int animationTick;
    private boolean jumpingUp;
    private Animation currentAnimation;
    private int upwardsFallingTicks;
    private boolean isUpsideDownNavigator;
    private boolean prevOnGround;

    protected EntityDropBear(EntityType type, Level world) {
        super(type, world);
        this.fallRotation = this.f_19796_.m_188499_();
        this.jumpingUp = false;
        this.upwardsFallingTicks = 0;
        this.prevOnGround = false;
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 22.0).m_22268_(Attributes.f_22277_, 20.0).m_22268_(Attributes.f_22278_, (double)0.7f).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, 0.25);
    }

    public static BlockPos getLowestPos(LevelAccessor world, BlockPos pos) {
        while (!world.m_8055_(pos).m_60783_((BlockGetter)world, pos, Direction.DOWN) && pos.m_123342_() < 320) {
            pos = pos.m_7494_();
        }
        return pos;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.dropbearSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.DROPBEAR_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.DROPBEAR_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.DROPBEAR_HURT.get();
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.f_19796_.m_188499_() ? ANIMATION_BITE : (this.f_19796_.m_188499_() ? ANIMATION_SWIPE_L : ANIMATION_SWIPE_R));
        }
        return true;
    }

    protected void m_8099_() {
        super.m_8099_();
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new AIDropMelee());
        this.f_21345_.m_25352_(2, (Goal)new AIUpsideDownWander());
        this.f_21345_.m_25352_(6, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21345_.m_25352_(7, (Goal)new LookAtPlayerGoal((Mob)this, LivingEntity.class, 30.0f));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[]{EntityDropBear.class}));
        this.f_21346_.m_25352_(2, (Goal)new EntityAINearestTarget3D((Mob)this, Player.class, true){

            @Override
            protected AABB m_7255_(double targetDistance) {
                AABB bb = this.f_26135_.m_20191_().m_82377_(targetDistance, targetDistance, targetDistance);
                return new AABB(bb.f_82288_, 0.0, bb.f_82290_, bb.f_82291_, 256.0, bb.f_82293_);
            }
        });
        this.f_21346_.m_25352_(2, (Goal)new EntityAINearestTarget3D((Mob)this, AbstractVillager.class, true){

            @Override
            protected AABB m_7255_(double targetDistance) {
                AABB bb = this.f_26135_.m_20191_().m_82377_(targetDistance, targetDistance, targetDistance);
                return new AABB(bb.f_82288_, 0.0, bb.f_82290_, bb.f_82291_, 256.0, bb.f_82293_);
            }
        });
    }

    public boolean m_6673_(DamageSource source) {
        return super.m_6673_(source) || source.m_269533_(DamageTypeTags.f_268549_) || source.m_276093_(DamageTypes.f_268612_);
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        super.m_7840_(y, onGroundIn, state, pos);
    }

    protected void m_21229_() {
        this.onLand();
        super.m_21229_();
    }

    private void switchNavigator(boolean rightsideUp) {
        if (rightsideUp) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = new GroundPathNavigatorWide((Mob)this, this.m_9236_());
            this.isUpsideDownNavigator = false;
        } else {
            this.f_21342_ = new FlightMoveController((Mob)this, 1.1f, false);
            this.f_21344_ = new DirectPathNavigator((Mob)this, this.m_9236_());
            this.isUpsideDownNavigator = true;
        }
    }

    public void m_8119_() {
        super.m_8119_();
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
        this.prevUpsideDownProgress = this.upsideDownProgress;
        if (this.isUpsideDown() && this.upsideDownProgress < 5.0f) {
            this.upsideDownProgress += 1.0f;
        }
        if (!this.isUpsideDown() && this.upsideDownProgress > 0.0f) {
            this.upsideDownProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            BlockPos abovePos = this.getPositionAbove();
            BlockState aboveState = this.m_9236_().m_8055_(abovePos);
            BlockState belowState = this.m_9236_().m_8055_(this.m_20099_());
            BlockPos worldHeight = this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING, this.m_20183_());
            boolean validAboveState = aboveState.m_60783_((BlockGetter)this.m_9236_(), abovePos, Direction.DOWN);
            boolean validBelowState = belowState.m_60783_((BlockGetter)this.m_9236_(), this.m_20099_(), Direction.UP);
            LivingEntity attackTarget = this.m_5448_();
            if (attackTarget != null && this.m_20270_((Entity)attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 1.0f && this.m_142582_((Entity)attackTarget)) {
                if (this.getAnimationTick() == 6) {
                    if (this.getAnimation() == ANIMATION_BITE) {
                        float yRotRad = this.m_146908_() * ((float)Math.PI / 180);
                        attackTarget.m_147240_(0.5, (double)Mth.m_14031_((float)yRotRad), (double)(-Mth.m_14089_((float)yRotRad)));
                        this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_());
                    }
                } else if (this.getAnimationTick() == 9) {
                    if (this.getAnimation() == ANIMATION_SWIPE_L) {
                        rot = this.m_146908_() + 90.0f;
                        float rotRad = rot * ((float)Math.PI / 180);
                        attackTarget.m_147240_(0.5, (double)Mth.m_14031_((float)rotRad), (double)(-Mth.m_14089_((float)rotRad)));
                        this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_());
                    } else if (this.getAnimation() == ANIMATION_SWIPE_R) {
                        rot = this.m_146908_() - 90.0f;
                        float rotRad = rot * ((float)Math.PI / 180);
                        attackTarget.m_147240_(0.5, (double)Mth.m_14031_((float)rotRad), (double)(-Mth.m_14089_((float)rotRad)));
                        this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_());
                    }
                }
            }
            if ((attackTarget == null || attackTarget != null && !attackTarget.m_6084_()) && this.f_19796_.m_188503_(300) == 0 && this.m_20096_() && !this.isUpsideDown() && this.m_20186_() + 2.0 < (double)worldHeight.m_123342_() && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_JUMPUP);
            }
            if (this.jumpingUp && this.m_20186_() > (double)worldHeight.m_123342_()) {
                this.jumpingUp = false;
            }
            if (this.m_20096_() && this.getAnimation() == ANIMATION_JUMPUP && this.getAnimationTick() > 10 || this.jumpingUp && this.getAnimation() == NO_ANIMATION) {
                this.m_20256_(this.m_20184_().m_82520_(0.0, 2.0, 0.0));
                this.jumpingUp = true;
            }
            if (this.isUpsideDown()) {
                this.jumpingUp = false;
                this.m_20242_(!this.m_20096_());
                float f = 0.91f;
                this.m_20256_(this.m_20184_().m_82542_((double)0.91f, 1.0, (double)0.91f));
                if (!this.f_19863_) {
                    if (this.m_20096_() || validBelowState || this.upwardsFallingTicks > 5) {
                        this.setUpsideDown(false);
                        this.upwardsFallingTicks = 0;
                    } else {
                        if (!validAboveState) {
                            ++this.upwardsFallingTicks;
                        }
                        this.m_20256_(this.m_20184_().m_82520_(0.0, (double)0.2f, 0.0));
                    }
                } else {
                    this.upwardsFallingTicks = 0;
                }
                if (this.f_19862_) {
                    this.upwardsFallingTicks = 0;
                    this.m_20256_(this.m_20184_().m_82520_(0.0, (double)-0.3f, 0.0));
                }
                if (this.m_5830_() && this.m_9236_().m_46859_(this.m_20099_())) {
                    this.m_6034_(this.m_20185_(), this.m_20186_() - 1.0, this.m_20189_());
                }
            } else {
                this.m_20242_(false);
                if (validAboveState) {
                    this.setUpsideDown(true);
                }
            }
            if (this.isUpsideDown()) {
                if (!this.isUpsideDownNavigator) {
                    this.switchNavigator(false);
                }
            } else if (this.isUpsideDownNavigator) {
                this.switchNavigator(true);
            }
        }
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(UPSIDE_DOWN, (Object)false);
    }

    public boolean isUpsideDown() {
        return (Boolean)this.f_19804_.m_135370_(UPSIDE_DOWN);
    }

    public void setUpsideDown(boolean upsideDown) {
        this.f_19804_.m_135381_(UPSIDE_DOWN, (Object)upsideDown);
    }

    protected BlockPos getPositionAbove() {
        return new BlockPos((int)this.m_20182_().f_82479_, (int)(this.m_20191_().f_82292_ + 0.5000001), (int)this.m_20182_().f_82481_);
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_BITE, ANIMATION_SWIPE_L, ANIMATION_SWIPE_R, ANIMATION_JUMPUP};
    }

    private boolean hasLineOfSightBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.m_82512_((Vec3i)destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
        return result.m_82425_().equals((Object)destinationBlock);
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos upperPos = this.getPositionAbove().m_7494_();
        BlockPos highest = EntityDropBear.getLowestPos(world, upperPos);
        this.m_6034_((float)highest.m_123341_() + 0.5f, highest.m_123342_(), (float)highest.m_123343_() + 0.5f);
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing((LevelAccessor)worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void onLand() {
        if (!this.m_9236_().f_46443_) {
            this.m_9236_().m_7605_((Entity)this, (byte)39);
            for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().m_82400_(2.5))) {
                if (this.m_7307_(entity) || entity instanceof EntityDropBear || entity == this) continue;
                entity.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), 2.0f + this.f_19796_.m_188501_() * 5.0f);
                this.launch(entity, true);
            }
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.m_20096_()) {
            double d0 = e.m_20185_() - this.m_20185_();
            double d1 = e.m_20189_() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = 0.5f;
            e.m_5997_(d0 / d2 * 0.5, huge ? 0.5 : (double)0.2f, d1 / d2 * 0.5);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 39) {
            this.spawnGroundEffects();
        } else {
            super.m_7822_(id);
        }
    }

    public void spawnGroundEffects() {
        float radius = 2.3f;
        if (this.m_9236_().f_46443_) {
            for (int i1 = 0; i1 < 20 + this.f_19796_.m_188503_(12); ++i1) {
                double motionX = this.m_217043_().m_188583_() * 0.07;
                double motionY = this.m_217043_().m_188583_() * 0.07;
                double motionZ = this.m_217043_().m_188583_() * 0.07;
                float angle = (float)Math.PI / 180 * this.f_20883_ + (float)i1;
                double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
                double extraY = 0.8f;
                double extraZ = radius * Mth.m_14089_((float)angle);
                BlockPos ground = this.getGroundPosition(new BlockPos(Mth.m_14107_((double)(this.m_20185_() + extraX)), (int)this.m_20186_(), Mth.m_14107_((double)(this.m_20189_() + extraZ))));
                BlockState state = this.m_9236_().m_8055_(ground);
                if (state.m_60795_()) continue;
                this.m_9236_().m_6493_((ParticleOptions)new BlockParticleOption(ParticleTypes.f_123794_, state), true, this.m_20185_() + extraX, (double)ground.m_123342_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
            }
        }
    }

    private BlockPos getGroundPosition(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int)this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > 2 && this.m_9236_().m_46859_(position) && this.m_9236_().m_6425_(position).m_76178_()) {
            position = position.m_7495_();
        }
        return position;
    }

    private class AIDropMelee
    extends Goal {
        private boolean prevOnGround = false;

        public AIDropMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            return EntityDropBear.this.m_5448_() != null;
        }

        public void m_8037_() {
            LivingEntity target = EntityDropBear.this.m_5448_();
            if (target != null) {
                double dist = EntityDropBear.this.m_20270_((Entity)target);
                if (EntityDropBear.this.isUpsideDown()) {
                    double d0 = EntityDropBear.this.m_20185_() - target.m_20185_();
                    double d2 = EntityDropBear.this.m_20189_() - target.m_20189_();
                    double xzDistSqr = d0 * d0 + d2 * d2;
                    BlockPos ceilingPos = new BlockPos((int)target.m_20185_(), (int)(EntityDropBear.this.m_20186_() - 3.0 - (double)EntityDropBear.this.f_19796_.m_188503_(3)), (int)target.m_20189_());
                    BlockPos lowestPos = EntityDropBear.getLowestPos((LevelAccessor)EntityDropBear.this.m_9236_(), ceilingPos);
                    EntityDropBear.this.m_21566_().m_6849_((double)((float)lowestPos.m_123341_() + 0.5f), (double)ceilingPos.m_123342_(), (double)((float)lowestPos.m_123343_() + 0.5f), 1.1);
                    if (xzDistSqr < 2.5) {
                        EntityDropBear.this.setUpsideDown(false);
                    }
                } else if (EntityDropBear.this.m_20096_()) {
                    EntityDropBear.this.m_21573_().m_5624_((Entity)target, 1.2);
                }
                if (dist < 3.0) {
                    EntityDropBear.this.m_7327_((Entity)target);
                }
            }
        }
    }

    class AIUpsideDownWander
    extends RandomStrollGoal {
        public AIUpsideDownWander() {
            super((PathfinderMob)EntityDropBear.this, 1.0, 50);
        }

        @Nullable
        protected Vec3 m_7037_() {
            if (EntityDropBear.this.isUpsideDown()) {
                for (int i = 0; i < 15; ++i) {
                    Random rand = new Random();
                    BlockPos randPos = EntityDropBear.this.m_20183_().m_7918_(rand.nextInt(16) - 8, -2, rand.nextInt(16) - 8);
                    BlockPos lowestPos = EntityDropBear.getLowestPos((LevelAccessor)EntityDropBear.this.m_9236_(), randPos);
                    if (!EntityDropBear.this.m_9236_().m_8055_(lowestPos).m_60783_((BlockGetter)EntityDropBear.this.m_9236_(), lowestPos, Direction.DOWN)) continue;
                    return Vec3.m_82512_((Vec3i)lowestPos);
                }
                return null;
            }
            return super.m_7037_();
        }

        public boolean m_8036_() {
            return super.m_8036_();
        }

        public boolean m_8045_() {
            if (EntityDropBear.this.isUpsideDown()) {
                double d2;
                double d0 = EntityDropBear.this.m_20185_() - this.f_25726_;
                double d4 = d0 * d0 + (d2 = EntityDropBear.this.m_20189_() - this.f_25728_) * d2;
                return d4 > 4.0;
            }
            return super.m_8045_();
        }

        public void m_8041_() {
            super.m_8041_();
            this.f_25726_ = 0.0;
            this.f_25727_ = 0.0;
            this.f_25728_ = 0.0;
        }

        public void m_8056_() {
            if (EntityDropBear.this.isUpsideDown()) {
                this.f_25725_.m_21566_().m_6849_(this.f_25726_, this.f_25727_, this.f_25728_, this.f_25729_ * (double)0.7f);
            } else {
                this.f_25725_.m_21573_().m_26519_(this.f_25726_, this.f_25727_, this.f_25728_, this.f_25729_);
            }
        }
    }
}

