/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.item.FallingBlockEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Fallable
 *  net.minecraft.world.level.block.PointedDripstoneBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.level.pathfinder.PathFinder
 *  net.minecraft.world.level.pathfinder.WalkNodeEvaluator
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexthe666.alexsmobs.entity.ai.MovementControllerCustomCollisions;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityRockyRoller
extends Monster
implements ICustomCollisions {
    private static final EntityDataAccessor<Boolean> ROLLING = SynchedEntityData.m_135353_(EntityRockyRoller.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.m_135353_(EntityRockyRoller.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float rollProgress;
    public float prevRollProgress;
    public int rollCounter = 0;
    public float clientRoll = 0.0f;
    private int maxRollTime = 50;
    private Vec3 rollDelta;
    private float rollYRot;
    private int rollCooldown = 0;
    private int earthquakeCooldown = 0;

    protected EntityRockyRoller(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);
        this.f_21364_ = 8;
        this.f_21342_ = new MovementControllerCustomCollisions((Mob)this);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.rockyRollerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean checkRockyRollerSpawnRules(EntityType<? extends Monster> animal, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && EntityRockyRoller.m_219009_((ServerLevelAccessor)worldIn, (BlockPos)pos, (RandomSource)random) && (worldIn.m_8055_(pos.m_7495_()).m_204336_(AMTagRegistry.ROCKY_ROLLER_SPAWNS) || worldIn.m_8055_(pos.m_7495_()).m_280296_());
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 10.0).m_22268_(Attributes.f_22284_, 20.0).m_22268_(Attributes.f_22277_, 20.0).m_22268_(Attributes.f_22278_, (double)0.7f).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, 0.25);
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new Navigator((Mob)this, worldIn);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new AIMelee());
        this.f_21345_.m_25352_(2, (Goal)new AIRollIdle(this));
        this.f_21345_.m_25352_(3, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8));
        this.f_21345_.m_25352_(4, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.f_21345_.m_25352_(5, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false, true));
        this.f_21346_.m_25352_(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, false, true));
        this.f_21346_.m_25352_(3, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(ANGRY, (Object)false);
        this.f_19804_.m_135372_(ROLLING, (Object)false);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.ROCKY_ROLLER_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.ROCKY_ROLLER_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.ROCKY_ROLLER_HURT.get();
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevRollProgress = this.rollProgress;
        if (this.isRolling()) {
            if (this.rollProgress < 5.0f) {
                this.rollProgress += 1.0f;
            }
        } else if (this.rollProgress > 0.0f) {
            this.rollProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            this.setAngry(this.m_5448_() != null && this.m_5448_().m_6084_() && this.m_20280_((Entity)this.m_5448_()) < 400.0);
        }
        if (this.isRolling() && this.rollCooldown <= 0) {
            this.handleRoll();
            if (this.isAngry() && this.m_6084_()) {
                for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().m_82400_((double)0.3f))) {
                    if (this.m_7307_(entity) || entity == this) continue;
                    entity.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (this.isTarget(entity) ? 5.0f : 2.0f) + this.f_19796_.m_188501_() * 2.0f);
                    this.launch(entity, this.isTarget(entity));
                    if (!this.isTarget(entity)) continue;
                    this.maxRollTime = this.rollCounter + 10;
                }
            }
            if (this.rollCounter > 2 && !this.isMoving() || !this.m_6084_()) {
                this.setRolling(false);
            }
            this.m_274367_(1.0f);
        } else {
            this.m_274367_(0.66f);
            this.rollCounter = 0;
        }
        if (this.rollCooldown > 0) {
            --this.rollCooldown;
        }
        if (this.earthquakeCooldown > 0) {
            --this.earthquakeCooldown;
        }
    }

    private boolean isMoving() {
        return this.m_20184_().m_82556_() > 0.02;
    }

    private void earthquake() {
        boolean flag = false;
        List list = this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().m_82377_(6.0, 8.0, 6.0));
        for (LivingEntity e : list) {
            if (e instanceof EntityRockyRoller || !e.m_6084_()) continue;
            e.m_7292_(new MobEffectInstance((MobEffect)AMEffectRegistry.EARTHQUAKE.get(), 20, 0, false, false, true));
            flag = true;
        }
        if (!this.m_9236_().m_45527_(this.m_20183_()) && this.m_9236_().m_46469_().m_46207_(GameRules.f_46132_)) {
            BlockPos ceil = this.m_20183_().m_7918_(0, 2, 0);
            while (!(this.m_9236_().m_8055_(ceil).m_280296_() && this.m_9236_().m_8055_(ceil).m_60734_() != Blocks.f_152588_ || ceil.m_123342_() >= this.m_9236_().m_151558_())) {
                ceil = ceil.m_7494_();
            }
            int i = 2 + this.f_19796_.m_188503_(2);
            int j = 2 + this.f_19796_.m_188503_(2);
            int k = 2 + this.f_19796_.m_188503_(2);
            float f = (float)(i + j + k) * 0.333f + 0.5f;
            double fTimesF = f * f;
            for (BlockPos blockpos1 : BlockPos.m_121940_((BlockPos)ceil.m_7918_(-i, -j, -k), (BlockPos)ceil.m_7918_(i, j, k))) {
                if (!(blockpos1.m_123331_((Vec3i)ceil) <= fTimesF) || !(this.m_9236_().m_8055_(blockpos1).m_60734_() instanceof Fallable)) continue;
                if (this.isHangingDripstone(blockpos1)) {
                    while (this.isHangingDripstone(blockpos1.m_7494_()) && blockpos1.m_123342_() < this.m_9236_().m_151558_()) {
                        blockpos1 = blockpos1.m_7494_();
                    }
                    if (this.isHangingDripstone(blockpos1)) {
                        Vec3 vec3 = Vec3.m_82539_((Vec3i)blockpos1);
                        FallingBlockEntity fallingblockentity = FallingBlockEntity.m_201971_((Level)this.m_9236_(), (BlockPos)new BlockPos((int)vec3.f_82479_, (int)vec3.f_82480_, (int)vec3.f_82481_), (BlockState)this.m_9236_().m_8055_(blockpos1));
                        this.m_9236_().m_46961_(blockpos1, false);
                        this.m_9236_().m_7967_((Entity)fallingblockentity);
                    }
                } else {
                    this.m_9236_().m_186460_(blockpos1, this.m_9236_().m_8055_(blockpos1).m_60734_(), 2);
                }
                flag = true;
            }
        }
        if (flag) {
            this.m_146850_(GameEvent.f_223709_);
            this.m_5496_((SoundEvent)AMSoundRegistry.ROCKY_ROLLER_EARTHQUAKE.get(), this.m_6121_(), this.m_6100_());
        }
    }

    private boolean isHangingDripstone(BlockPos pos) {
        return this.m_9236_().m_8055_(pos).m_60734_() instanceof PointedDripstoneBlock && this.m_9236_().m_8055_(pos).m_61143_((Property)PointedDripstoneBlock.f_154009_) == Direction.DOWN;
    }

    private boolean isTarget(Entity entity) {
        return this.m_5448_() != null && this.m_5448_().m_7306_(entity);
    }

    public boolean isRolling() {
        return (Boolean)this.f_19804_.m_135370_(ROLLING);
    }

    public void setRolling(boolean rolling) {
        this.f_19804_.m_135381_(ROLLING, (Object)rolling);
    }

    public boolean isAngry() {
        return (Boolean)this.f_19804_.m_135370_(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.f_19804_.m_135381_(ANGRY, (Object)angry);
    }

    private void handleRoll() {
        ++this.rollCounter;
        if (!this.m_9236_().f_46443_) {
            if (this.f_19862_ && this.earthquakeCooldown == 0 & this.isAngry()) {
                this.earthquakeCooldown = this.maxRollTime;
                this.earthquake();
            }
            if (this.rollCounter > this.maxRollTime) {
                this.setRolling(false);
                this.rollCooldown = 10 + this.f_19796_.m_188503_(10);
                this.rollCounter = 0;
                this.m_20256_(Vec3.f_82478_);
            } else {
                Vec3 vec3 = this.m_20184_();
                if (this.rollCounter == 1) {
                    float f = this.m_146908_() * ((float)Math.PI / 180);
                    float f1 = this.m_6162_() ? 0.2f : 0.35f;
                    this.rollYRot = this.m_146908_();
                    this.rollDelta = new Vec3(vec3.f_82479_ + (double)(-Mth.m_14031_((float)f) * f1), 0.0, vec3.f_82481_ + (double)(Mth.m_14089_((float)f) * f1));
                    this.m_20256_(this.rollDelta.m_82520_(0.0, 0.27, 0.0));
                } else {
                    this.m_146922_(this.rollYRot);
                    this.m_5616_(this.rollYRot);
                    this.m_5618_(this.rollYRot);
                    this.m_20334_(this.rollDelta.f_82479_, vec3.f_82480_, this.rollDelta.f_82481_);
                }
            }
        }
    }

    private void rollFor(int time) {
        if (this.rollCooldown == 0) {
            this.maxRollTime = time;
            this.earthquakeCooldown = 0;
            this.setRolling(true);
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.m_20096_()) {
            double d0 = e.m_20185_() - this.m_20185_();
            double d1 = e.m_20189_() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = huge ? 1.0f : 0.35f;
            e.m_5997_(d0 / d2 * (double)f, huge ? 0.5 : (double)0.2f, d1 / d2 * (double)f);
        }
    }

    public boolean m_6673_(DamageSource source) {
        return source.equals("fallingStalactite") || super.m_6673_(source);
    }

    public int m_6056_() {
        return super.m_6056_() * 2;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean m_5829_() {
        return this.m_6084_();
    }

    public void m_7334_(Entity entity) {
        entity.m_20256_(entity.m_20184_().m_82549_(this.m_20184_()));
    }

    public boolean canPassThrough(BlockPos blockPos, BlockState blockstate, VoxelShape voxelShape) {
        return blockstate.m_60734_() instanceof PointedDripstoneBlock;
    }

    public boolean m_20039_(BlockPos pos, BlockState blockstate) {
        return !(blockstate.m_60734_() instanceof PointedDripstoneBlock) && super.m_20039_(pos, blockstate);
    }

    public Vec3 m_20272_(Vec3 vec3) {
        return ICustomCollisions.getAllowedMovementForEntity((Entity)this, (Vec3)vec3);
    }

    public boolean m_6469_(DamageSource dmg, float amount) {
        LivingEntity livingentity;
        Entity entity;
        if (!(this.isMoving() || dmg.m_276093_(DamageTypes.f_268515_) || !((entity = dmg.m_7640_()) instanceof LivingEntity) || (livingentity = (LivingEntity)entity) instanceof EntityRockyRoller || dmg.m_276093_(DamageTypes.f_268565_) || livingentity.f_19864_)) {
            livingentity.m_6469_(this.m_269291_().m_269374_((Entity)this), 2.0f);
        }
        return super.m_6469_(dmg, amount);
    }

    static class Navigator
    extends AdvancedPathNavigateNoTeleport {
        public Navigator(Mob mob, Level world) {
            super(mob, world, true);
        }

        protected PathFinder m_5532_(int i) {
            this.f_26508_ = new RockyRollerNodeEvaluator();
            return new PathFinder(this.f_26508_, i);
        }
    }

    private class AIMelee
    extends Goal {
        private BlockPos rollFromPos = null;
        private int rollTimeout = 0;

        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            return EntityRockyRoller.this.m_5448_() != null && EntityRockyRoller.this.m_5448_().m_6084_() && !EntityRockyRoller.this.isRolling();
        }

        public void m_8037_() {
            LivingEntity enemy = EntityRockyRoller.this.m_5448_();
            double d0 = this.validRollDistance(enemy);
            double distToEnemySqr = EntityRockyRoller.this.m_20270_((Entity)enemy);
            if (this.rollFromPos == null || enemy.m_20275_((double)((float)this.rollFromPos.m_123341_() + 0.5f), (double)((float)this.rollFromPos.m_123342_() + 0.5f), (double)this.rollFromPos.m_123343_() + 0.5) > 60.0 || !this.canEntitySeePosition(enemy, this.rollFromPos)) {
                this.rollFromPos = this.getRollAtPosition((Entity)enemy);
            }
            EntityRockyRoller.this.m_21391_((Entity)enemy, 100.0f, 5.0f);
            if (this.rollTimeout < 40 && this.rollFromPos != null && distToEnemySqr <= d0 && EntityRockyRoller.this.m_20275_((float)this.rollFromPos.m_123341_() + 0.5f, (float)this.rollFromPos.m_123342_() + 0.5f, (double)this.rollFromPos.m_123343_() + 0.5) > 2.25) {
                EntityRockyRoller.this.m_21573_().m_26519_((double)((float)this.rollFromPos.m_123341_() + 0.5f), (double)((float)this.rollFromPos.m_123342_() + 0.5f), (double)((float)this.rollFromPos.m_123343_() + 0.5f), 1.6);
                ++this.rollTimeout;
            } else {
                double d1 = enemy.m_20185_() - EntityRockyRoller.this.m_20185_();
                double d2 = enemy.m_20189_() - EntityRockyRoller.this.m_20189_();
                float f = (float)(Mth.m_14136_((double)d2, (double)d1) * 57.2957763671875) - 90.0f;
                EntityRockyRoller.this.m_146922_(f);
                EntityRockyRoller.this.f_20883_ = f;
                EntityRockyRoller.this.rollFor(30 + EntityRockyRoller.this.f_19796_.m_188503_(40));
            }
        }

        public void m_8041_() {
            super.m_8041_();
            this.rollTimeout = 0;
        }

        protected double validRollDistance(LivingEntity attackTarget) {
            return 3.0f + attackTarget.m_20205_();
        }

        private boolean canEntitySeePosition(LivingEntity entity, BlockPos destinationBlock) {
            Vec3 Vector3d = new Vec3(entity.m_20185_(), entity.m_20186_() + 0.5, entity.m_20189_());
            Vec3 blockVec = Vec3.m_82512_((Vec3i)destinationBlock);
            BlockHitResult result = entity.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)entity));
            return result != null && (result.m_82425_().equals((Object)destinationBlock) || entity.m_9236_().m_8055_(result.m_82425_()).m_60734_() == Blocks.f_152588_);
        }

        public BlockPos getRollAtPosition(Entity target) {
            float radius = (float)(EntityRockyRoller.this.m_217043_().m_188503_(2) + 6) + target.m_20205_();
            int orbit = EntityRockyRoller.this.m_217043_().m_188503_(360);
            float angle = (float)Math.PI / 180 * (float)orbit;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos circlePos = new BlockPos((int)(target.m_20185_() + extraX), (int)target.m_20188_(), (int)(target.m_20189_() + extraZ));
            while (!EntityRockyRoller.this.m_9236_().m_8055_(circlePos).m_60795_() && circlePos.m_123342_() < EntityRockyRoller.this.m_9236_().m_151558_()) {
                circlePos = circlePos.m_7494_();
            }
            while (!EntityRockyRoller.this.m_9236_().m_8055_(circlePos.m_7495_()).m_60634_((BlockGetter)EntityRockyRoller.this.m_9236_(), circlePos.m_7495_(), (Entity)EntityRockyRoller.this) && circlePos.m_123342_() > 1) {
                circlePos = circlePos.m_7495_();
            }
            if (EntityRockyRoller.this.m_21692_(circlePos) > -1.0f) {
                return circlePos;
            }
            return null;
        }
    }

    class AIRollIdle
    extends Goal {
        EntityRockyRoller rockyRoller;

        public AIRollIdle(EntityRockyRoller p_29328_) {
            this.rockyRoller = p_29328_;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        public boolean m_8036_() {
            if (this.rockyRoller.m_20096_()) {
                if (this.rockyRoller.isRolling() || this.rockyRoller.rollCooldown > 0 || this.rockyRoller.m_5448_() != null && this.rockyRoller.m_5448_().m_6084_()) {
                    return false;
                }
                float f = this.rockyRoller.m_146908_() * ((float)Math.PI / 180);
                int i = 0;
                int j = 0;
                float f1 = -Mth.m_14031_((float)f);
                float f2 = Mth.m_14089_((float)f);
                if ((double)Math.abs(f1) > 0.5) {
                    i = (int)((float)i + f1 / Math.abs(f1));
                }
                if ((double)Math.abs(f2) > 0.5) {
                    j = (int)((float)j + f2 / Math.abs(f2));
                }
                return this.rockyRoller.m_9236_().m_8055_(this.rockyRoller.m_20183_().m_7918_(i, -1, j)).m_60795_();
            }
            return false;
        }

        public boolean m_8045_() {
            return false;
        }

        public void m_8056_() {
            this.rockyRoller.rollFor(30 + EntityRockyRoller.this.f_19796_.m_188503_(30));
        }

        public boolean m_6767_() {
            return false;
        }
    }

    static class RockyRollerNodeEvaluator
    extends WalkNodeEvaluator {
        RockyRollerNodeEvaluator() {
        }

        protected BlockPathTypes m_264405_(BlockGetter level, BlockPos pos, BlockPathTypes typeIn) {
            return level.m_8055_(pos).m_60734_() instanceof PointedDripstoneBlock ? BlockPathTypes.OPEN : super.m_264405_(level, pos, typeIn);
        }
    }
}

