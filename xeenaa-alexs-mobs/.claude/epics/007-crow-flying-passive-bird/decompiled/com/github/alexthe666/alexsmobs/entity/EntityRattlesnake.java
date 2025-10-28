/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.FollowParentGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.Rabbit
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityJerboa;
import com.github.alexthe666.alexsmobs.entity.EntityRoadrunner;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityRattlesnake
extends Animal
implements IAnimatedEntity {
    public float prevCurlProgress;
    public float curlProgress;
    public int randomToungeTick = 0;
    public int maxCurlTime = 75;
    private int curlTime = 0;
    private static final EntityDataAccessor<Boolean> RATTLING = SynchedEntityData.m_135353_(EntityRattlesnake.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> CURLED = SynchedEntityData.m_135353_(EntityRattlesnake.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final Predicate<LivingEntity> WARNABLE_PREDICATE = mob -> mob instanceof Player && !((Player)mob).m_7500_() && !mob.m_5833_() || mob instanceof EntityRoadrunner;
    private static final Predicate<LivingEntity> TARGETABLE_PREDICATE = mob -> mob instanceof Player && !((Player)mob).m_7500_() && !mob.m_5833_() || mob instanceof EntityRoadrunner;
    private int animationTick;
    private Animation currentAnimation;
    public static final Animation ANIMATION_BITE = Animation.create((int)20);
    private int loopSoundTick = 0;

    protected EntityRattlesnake(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.2, true));
        this.f_21345_.m_25352_(2, (Goal)new WarnPredatorsGoal());
        this.f_21345_.m_25352_(2, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(4, (Goal)new FollowParentGoal((Animal)this, 1.1));
        this.f_21345_.m_25352_(5, (Goal)new AnimalAIWanderRanged((PathfinderMob)this, 60, 1.0, 7, 7));
        this.f_21345_.m_25352_(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 15.0f));
        this.f_21345_.m_25352_(7, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Rabbit.class, 15, true, true, null));
        this.f_21346_.m_25352_(2, (Goal)new NearestAttackableTargetGoal((Mob)this, EntityJerboa.class, 15, true, true, null));
        this.f_21346_.m_25352_(3, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.f_21346_.m_25352_(4, (Goal)new ShortDistanceTarget());
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.RATTLESNAKE_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.RATTLESNAKE_HURT.get();
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.rattlesnakeSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public boolean m_7327_(Entity entityIn) {
        this.setAnimation(ANIMATION_BITE);
        return true;
    }

    public boolean m_7301_(MobEffectInstance potioneffectIn) {
        if (potioneffectIn.m_19544_() == MobEffects.f_19614_) {
            return false;
        }
        return super.m_7301_(potioneffectIn);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(CURLED, (Object)false);
        this.f_19804_.m_135372_(RATTLING, (Object)false);
    }

    public boolean isCurled() {
        return (Boolean)this.f_19804_.m_135370_(CURLED);
    }

    public void setCurled(boolean curled) {
        this.f_19804_.m_135381_(CURLED, (Object)curled);
    }

    public boolean isRattling() {
        return (Boolean)this.f_19804_.m_135370_(RATTLING);
    }

    public void setRattling(boolean rattling) {
        this.f_19804_.m_135381_(RATTLING, (Object)rattling);
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevCurlProgress = this.curlProgress;
        if (this.isCurled()) {
            if (this.curlProgress < 5.0f) {
                this.curlProgress += 0.5f;
            }
        } else if (this.curlProgress > 0.0f) {
            this.curlProgress -= 1.0f;
        }
        if (this.randomToungeTick == 0 && this.f_19796_.m_188503_(15) == 0) {
            this.randomToungeTick = 10 + this.f_19796_.m_188503_(20);
        }
        if (this.randomToungeTick > 0) {
            --this.randomToungeTick;
        }
        if (this.isCurled() && !this.isRattling() && ++this.curlTime > this.maxCurlTime) {
            this.setCurled(false);
            this.curlTime = 0;
            this.maxCurlTime = 75 + this.f_19796_.m_188503_(50);
        }
        LivingEntity target = this.m_5448_();
        if (!this.m_9236_().f_46443_) {
            if (this.isCurled() && target != null && target.m_6084_()) {
                this.setCurled(false);
            }
            if (this.isRattling() && target == null) {
                this.setCurled(true);
            }
            if (!this.isCurled() && this.m_5448_() == null && this.f_19796_.m_188503_(500) == 0) {
                this.maxCurlTime = 300 + this.f_19796_.m_188503_(250);
                this.setCurled(true);
            }
        }
        if (this.getAnimation() == ANIMATION_BITE) {
            if (this.getAnimationTick() == 4) {
                this.m_5496_((SoundEvent)AMSoundRegistry.RATTLESNAKE_ATTACK.get(), this.m_6121_(), this.m_6100_());
            }
            if (this.getAnimationTick() == 8 && target != null && (double)this.m_20270_((Entity)target) < 2.0) {
                boolean meepMeep = target instanceof EntityRoadrunner;
                int f = this.m_6162_() ? 2 : 1;
                target.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), meepMeep ? 1.0f : (float)f * (float)this.m_21051_(Attributes.f_22281_).m_22115_());
                if (!meepMeep) {
                    target.m_7292_(new MobEffectInstance(MobEffects.f_19614_, 300, f * 2));
                }
            }
        }
        if (this.isRattling()) {
            if (this.loopSoundTick == 0) {
                this.m_146850_(GameEvent.f_223709_);
                this.m_5496_((SoundEvent)AMSoundRegistry.RATTLESNAKE_LOOP.get(), this.m_6121_() * 0.5f, this.m_6100_());
            }
            ++this.loopSoundTick;
            if (this.loopSoundTick > 50) {
                this.loopSoundTick = 0;
            }
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    public void m_7023_(Vec3 vec3d) {
        if (this.m_20096_() && this.isCurled()) {
            if (this.m_21573_().m_26570_() != null) {
                this.m_21573_().m_26573_();
            }
            vec3d = Vec3.f_82478_;
        }
        super.m_7023_(vec3d);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 8.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, (double)0.28f);
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_41720_().m_41472_() && stack.m_41720_().m_41473_() != null && stack.m_41720_().m_41473_().m_38746_();
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return (AgeableMob)((EntityType)AMEntityRegistry.RATTLESNAKE.get()).m_20615_((Level)p_241840_1_);
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_BITE};
    }

    public static boolean canRattlesnakeSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.m_7495_()).m_204336_(AMTagRegistry.RATTLESNAKE_SPAWNS);
        return spawnBlock && worldIn.m_45524_(pos, 0) > 8;
    }

    class WarnPredatorsGoal
    extends Goal {
        int executionChance = 20;
        Entity target = null;

        WarnPredatorsGoal() {
        }

        public boolean m_8036_() {
            if (EntityRattlesnake.this.m_217043_().m_188503_(this.executionChance) == 0) {
                double dist = 5.0;
                List list = EntityRattlesnake.this.m_9236_().m_6443_(LivingEntity.class, EntityRattlesnake.this.m_20191_().m_82377_(5.0, 5.0, 5.0), WARNABLE_PREDICATE);
                double d0 = Double.MAX_VALUE;
                Entity possibleTarget = null;
                for (Entity entity : list) {
                    double d1 = EntityRattlesnake.this.m_20280_(entity);
                    if (d1 > d0) continue;
                    d0 = d1;
                    possibleTarget = entity;
                }
                this.target = possibleTarget;
                return !list.isEmpty();
            }
            return false;
        }

        public boolean m_8045_() {
            return this.target != null && (double)EntityRattlesnake.this.m_20270_(this.target) < 5.0 && EntityRattlesnake.this.m_5448_() == null;
        }

        public void m_8041_() {
            this.target = null;
            EntityRattlesnake.this.setRattling(false);
        }

        public void m_8037_() {
            EntityRattlesnake.this.setRattling(true);
            EntityRattlesnake.this.setCurled(true);
            EntityRattlesnake.this.curlTime = 0;
            EntityRattlesnake.this.m_21563_().m_24960_(this.target, 30.0f, 30.0f);
        }
    }

    class ShortDistanceTarget
    extends NearestAttackableTargetGoal<Player> {
        public ShortDistanceTarget() {
            super((Mob)EntityRattlesnake.this, Player.class, 3, true, true, TARGETABLE_PREDICATE);
        }

        public boolean m_8036_() {
            if (EntityRattlesnake.this.m_6162_()) {
                return false;
            }
            return super.m_8036_();
        }

        public void m_8056_() {
            super.m_8056_();
            EntityRattlesnake.this.setCurled(false);
            EntityRattlesnake.this.setRattling(true);
        }

        protected double m_7623_() {
            return 2.0;
        }
    }
}

