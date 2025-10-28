/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
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
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.FollowParentGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.monster.AbstractSkeleton
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.Pillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.IHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityEmu
extends Animal
implements IAnimatedEntity,
IHerdPanic {
    public static final Animation ANIMATION_DODGE_LEFT = Animation.create((int)10);
    public static final Animation ANIMATION_DODGE_RIGHT = Animation.create((int)10);
    public static final Animation ANIMATION_PECK_GROUND = Animation.create((int)25);
    public static final Animation ANIMATION_SCRATCH = Animation.create((int)20);
    public static final Animation ANIMATION_PUZZLED = Animation.create((int)30);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.m_135353_(EntityEmu.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private int animationTick;
    private Animation currentAnimation;
    private int revengeCooldown = 0;
    private boolean emuAttackedDirectly = false;
    public int timeUntilNextEgg = this.f_19796_.m_188503_(6000) + 6000;

    protected EntityEmu(EntityType type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 20.0).m_22268_(Attributes.f_22279_, (double)0.35f).m_22268_(Attributes.f_22281_, 3.0);
    }

    public static <T extends Mob> boolean canEmuSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.m_7495_()).m_204336_(AMTagRegistry.EMU_SPAWNS);
        return spawnBlock && worldIn.m_45524_(pos, 0) > 8;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.emuSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.EMU_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.EMU_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.EMU_HURT.get();
    }

    public int getVariant() {
        return (Integer)this.f_19804_.m_135370_(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.m_135381_(VARIANT, (Object)variant);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(VARIANT, (Object)0);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.3, true){

            protected double m_6639_(LivingEntity attackTarget) {
                return super.m_6639_(attackTarget) + 2.5;
            }

            public boolean m_8036_() {
                return super.m_8036_() && EntityEmu.this.revengeCooldown <= 0;
            }

            public boolean m_8045_() {
                return super.m_8045_() && EntityEmu.this.revengeCooldown <= 0;
            }
        });
        this.f_21345_.m_25352_(2, (Goal)new AnimalAIHerdPanic((PathfinderMob)this, 1.5));
        this.f_21345_.m_25352_(3, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(4, (Goal)new FollowParentGoal((Animal)this, 1.1));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.1, Ingredient.m_204132_(AMTagRegistry.EMU_BREEDABLES), false));
        this.f_21345_.m_25352_(5, (Goal)new AnimalAIWanderRanged((PathfinderMob)this, 110, 1.0, 10, 7));
        this.f_21345_.m_25352_(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 15.0f));
        this.f_21345_.m_25352_(7, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal());
        if (AMConfig.emuTargetSkeletons) {
            this.f_21346_.m_25352_(2, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractSkeleton.class, false));
            this.f_21346_.m_25352_(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Pillager.class, false));
        }
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.EMU_BREEDABLES);
    }

    public boolean m_6779_(LivingEntity target) {
        return !this.m_6162_() && super.m_6779_(target);
    }

    public boolean m_6469_(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev) {
            int fleeTime;
            double range = 15.0;
            this.revengeCooldown = fleeTime = 100 + this.m_217043_().m_188503_(5);
            List list = this.m_9236_().m_45976_(this.getClass(), this.m_20191_().m_82377_(range, range / 2.0, range));
            for (EntityEmu emu : list) {
                emu.revengeCooldown = fleeTime;
                if (!emu.m_6162_() || this.f_19796_.m_188503_(2) != 0) continue;
                emu.emuAttackedDirectly = this.m_21188_() != null;
                emu.revengeCooldown = emu.emuAttackedDirectly ? 10 + this.m_217043_().m_188503_(30) : fleeTime;
            }
            this.emuAttackedDirectly = this.m_21188_() != null;
            this.revengeCooldown = this.emuAttackedDirectly ? 10 + this.m_217043_().m_188503_(30) : this.revengeCooldown;
        }
        return prev;
    }

    public void m_7023_(Vec3 travelVector) {
        this.m_7910_((float)this.m_21133_(Attributes.f_22279_) * (this.getAnimation() == ANIMATION_PECK_GROUND || this.getAnimation() == ANIMATION_PUZZLED ? 0.15f : 1.0f) * (this.m_20077_() ? 0.2f : 1.0f));
        super.m_7023_(travelVector);
    }

    public void m_8119_() {
        super.m_8119_();
        if (!this.m_9236_().f_46443_) {
            if (this.m_21188_() == null && this.m_5448_() == null && this.m_20184_().m_82556_() < 0.03 && this.m_217043_().m_188503_(190) == 0 && this.getAnimation() == NO_ANIMATION) {
                if (this.m_217043_().m_188503_(3) == 0) {
                    this.setAnimation(ANIMATION_PUZZLED);
                } else if (this.m_20096_()) {
                    this.setAnimation(ANIMATION_PECK_GROUND);
                }
            }
            if (this.revengeCooldown > 0) {
                --this.revengeCooldown;
            }
            if (this.revengeCooldown <= 0 && this.m_21188_() != null && !this.emuAttackedDirectly) {
                this.m_6703_(null);
                this.revengeCooldown = 0;
            }
            LivingEntity target = this.m_5448_();
            if (this.m_6084_() && target != null && this.getAnimation() == ANIMATION_SCRATCH && this.m_20270_((Entity)target) < 4.0f && (this.getAnimationTick() == 8 || this.getAnimationTick() == 15)) {
                float f1 = this.m_146908_() * ((float)Math.PI / 180);
                this.m_20256_(this.m_20184_().m_82520_((double)(-Mth.m_14031_((float)f1) * 0.02f), 0.0, (double)(Mth.m_14089_((float)f1) * 0.02f)));
                target.m_147240_((double)0.4f, target.m_20185_() - this.m_20185_(), target.m_20189_() - this.m_20189_());
                target.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_());
            }
        }
        if (!this.m_9236_().f_46443_ && this.m_6084_() && !this.m_6162_() && --this.timeUntilNextEgg <= 0) {
            this.m_5496_(SoundEvents.f_11752_, 1.0f, (this.f_19796_.m_188501_() - this.f_19796_.m_188501_()) * 0.2f + 1.0f);
            this.m_19998_((ItemLike)AMItemRegistry.EMU_EGG.get());
            this.timeUntilNextEgg = this.f_19796_.m_188503_(6000) + 6000;
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_DODGE_LEFT, ANIMATION_DODGE_RIGHT, ANIMATION_PECK_GROUND, ANIMATION_SCRATCH, ANIMATION_PUZZLED};
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityEmu emu = (EntityEmu)((EntityType)AMEntityRegistry.EMU.get()).m_20615_((Level)serverWorld);
        emu.setVariant(this.getVariant());
        return emu;
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SCRATCH);
        }
        return true;
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setVariant(compound.m_128451_("Variant"));
        if (compound.m_128441_("EggLayTime")) {
            this.timeUntilNextEgg = compound.m_128451_("EggLayTime");
        }
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128405_("Variant", this.getVariant());
        compound.m_128405_("EggLayTime", this.timeUntilNextEgg);
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (this.f_19796_.m_188503_(200) == 0) {
            this.setVariant(2);
        } else if (this.f_19796_.m_188503_(3) == 0) {
            this.setVariant(1);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void onPanic() {
    }

    @Override
    public boolean canPanic() {
        return true;
    }

    class HurtByTargetGoal
    extends net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal {
        public HurtByTargetGoal() {
            super((PathfinderMob)EntityEmu.this, new Class[0]);
        }

        public void m_8056_() {
            if (EntityEmu.this.m_6162_() || !EntityEmu.this.emuAttackedDirectly) {
                this.m_26047_();
                this.m_8041_();
            } else {
                super.m_8056_();
            }
        }

        protected void m_5766_(Mob mobIn, LivingEntity targetIn) {
            if (mobIn instanceof EntityEmu && !mobIn.m_6162_() && !EntityEmu.this.emuAttackedDirectly && ((EntityEmu)mobIn).revengeCooldown <= 0) {
                super.m_5766_(mobIn, targetIn);
            }
        }
    }
}

