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
 *  net.minecraft.network.chat.Component
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
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.AgeableMob$AgeableMobGroupData
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Ingredient$TagValue
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.common.Tags$Items
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityTossedItem;
import com.github.alexthe666.alexsmobs.entity.IFollower;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.CapuchinAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.CapuchinAIRangedAttack;
import com.github.alexthe666.alexsmobs.entity.ai.CapuchinAITargetBalloons;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwner;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public class EntityCapuchinMonkey
extends TamableAnimal
implements IAnimatedEntity,
IFollower,
ITargetsDroppedItems {
    public static final Animation ANIMATION_THROW = Animation.create((int)12);
    public static final Animation ANIMATION_HEADTILT = Animation.create((int)15);
    public static final Animation ANIMATION_SCRATCH = Animation.create((int)20);
    protected static final EntityDataAccessor<Boolean> DART = SynchedEntityData.m_135353_(EntityCapuchinMonkey.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.m_135353_(EntityCapuchinMonkey.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.m_135353_(EntityCapuchinMonkey.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.m_135353_(EntityCapuchinMonkey.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> DART_TARGET = SynchedEntityData.m_135353_(EntityCapuchinMonkey.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    public float prevSitProgress;
    public float sitProgress;
    public boolean forcedSit = false;
    public boolean attackDecision = false;
    private int animationTick;
    private Animation currentAnimation;
    private int sittingTime = 0;
    private int maxSitTime = 75;
    private boolean hasSlowed = false;
    private int rideCooldown = 0;
    private Ingredient temptItems = null;

    protected EntityCapuchinMonkey(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0f);
    }

    public static boolean isTameableFood(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.CAPUCHIN_MONKEY_TAMEABLES);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 10.0).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, (double)0.4f);
    }

    public static <T extends Mob> boolean canCapuchinSpawn(EntityType<EntityCapuchinMonkey> gorilla, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.m_7495_());
        return (blockstate.m_204336_(AMTagRegistry.CAPUCHIN_MONKEY_SPAWNS) || blockstate.m_60713_(Blocks.f_50016_)) && worldIn.m_45524_(p_223317_3_, 0) > 8;
    }

    public int m_5792_() {
        return 8;
    }

    public boolean m_7296_(int sizeIn) {
        return false;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.capuchinMonkeySpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public Ingredient getAllFoods() {
        if (this.temptItems == null) {
            this.temptItems = Ingredient.m_43938_(Stream.of(new Ingredient.TagValue(AMTagRegistry.CAPUCHIN_MONKEY_BREEDABLES), new Ingredient.TagValue(AMTagRegistry.CAPUCHIN_MONKEY_FOODSTUFFS)));
        }
        return this.temptItems;
    }

    public boolean m_6469_(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        }
        Entity entity = source.m_7639_();
        if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
            amount = (amount + 1.0f) / 4.0f;
        }
        return super.m_6469_(source, amount);
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(1, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(2, (Goal)new SitWhenOrderedToGoal((TamableAnimal)this));
        this.f_21345_.m_25352_(3, (Goal)new CapuchinAIMelee(this, 1.0, true));
        this.f_21345_.m_25352_(3, (Goal)new CapuchinAIRangedAttack(this, 1.0, 20, 15.0f));
        this.f_21345_.m_25352_(6, (Goal)new TameableAIFollowOwner(this, 1.0, 10.0f, 2.0f, false));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.1, Ingredient.m_204132_(AMTagRegistry.CAPUCHIN_MONKEY_TAMEABLES), true){

            public void m_8037_() {
                super.m_8037_();
                if (this.f_25924_.m_20280_((Entity)this.f_25925_) < 6.25 && this.f_25924_.m_217043_().m_188503_(14) == 0) {
                    ((EntityCapuchinMonkey)this.f_25924_).setAnimation(ANIMATION_HEADTILT);
                }
            }
        });
        this.f_21345_.m_25352_(7, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0, 60));
        this.f_21345_.m_25352_(10, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.f_21345_.m_25352_(10, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new CreatureAITargetItems((PathfinderMob)this, false));
        this.f_21346_.m_25352_(2, (Goal)new OwnerHurtByTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(3, (Goal)new OwnerHurtTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(4, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[]{EntityCapuchinMonkey.class, EntityTossedItem.class}).m_26044_(new Class[0]));
        this.f_21346_.m_25352_(5, (Goal)new CapuchinAITargetBalloons(this, true));
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.CAPUCHIN_MONKEY_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.CAPUCHIN_MONKEY_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.CAPUCHIN_MONKEY_HURT.get();
    }

    public boolean m_7307_(Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal)entityIn).m_21830_(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.m_7307_(entityIn);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("MonkeySitting", this.isSitting());
        compound.m_128379_("HasDart", this.hasDart());
        compound.m_128379_("ForcedToSit", this.forcedSit);
        compound.m_128405_("Command", this.getCommand());
        compound.m_128405_("Variant", this.getVariant());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.m_21839_(compound.m_128471_("MonkeySitting"));
        this.forcedSit = compound.m_128471_("ForcedToSit");
        this.setCommand(compound.m_128451_("Command"));
        this.setDart(compound.m_128471_("HasDart"));
        this.setVariant(compound.m_128451_("Variant"));
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 10.0f) {
                this.sitProgress += 1.0f;
            }
        } else if (this.sitProgress > 0.0f) {
            this.sitProgress -= 1.0f;
        }
        if (!this.forcedSit && this.isSitting() && ++this.sittingTime > this.maxSitTime) {
            this.m_21839_(false);
            this.sittingTime = 0;
            this.maxSitTime = 75 + this.f_19796_.m_188503_(50);
        }
        if (!this.m_9236_().f_46443_ && this.getAnimation() == NO_ANIMATION && !this.isSitting() && this.getCommand() != 1 && this.f_19796_.m_188503_(1500) == 0) {
            this.maxSitTime = 300 + this.f_19796_.m_188503_(250);
            this.m_21839_(true);
        }
        this.m_274367_(2.0f);
        if (!this.forcedSit && this.isSitting() && (this.getDartTarget() != null || this.getCommand() == 1)) {
            this.m_21839_(false);
        }
        if (!this.m_9236_().f_46443_) {
            if (this.m_5448_() != null && this.getAnimation() == ANIMATION_SCRATCH && this.getAnimationTick() == 10) {
                float f1 = this.m_146908_() * ((float)Math.PI / 180);
                this.m_20256_(this.m_20184_().m_82520_((double)(-Mth.m_14031_((float)f1) * 0.3f), 0.0, (double)(Mth.m_14089_((float)f1) * 0.3f)));
                this.m_5448_().m_147240_(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_());
                this.setAttackDecision((Entity)this.m_5448_());
            }
            if (this.getDartTarget() != null && this.getDartTarget().m_6084_() && this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() == 5) {
                Vec3 vector3d = this.getDartTarget().m_20184_();
                double d0 = this.getDartTarget().m_20185_() + vector3d.f_82479_ - this.m_20185_();
                double d1 = this.getDartTarget().m_20188_() - (double)1.1f - this.m_20186_();
                double d2 = this.getDartTarget().m_20189_() + vector3d.f_82481_ - this.m_20189_();
                float f = Mth.m_14116_((float)((float)(d0 * d0 + d2 * d2)));
                EntityTossedItem tossedItem = new EntityTossedItem(this.m_9236_(), (LivingEntity)this);
                tossedItem.setDart(this.hasDart());
                tossedItem.m_146926_(tossedItem.m_146909_() - 20.0f);
                tossedItem.m_6686_(d0, d1 + (double)(f * 0.2f), d2, this.hasDart() ? 1.15f : 0.75f, 8.0f);
                if (!this.m_20067_()) {
                    this.m_9236_().m_6263_(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.f_12553_, this.m_5720_(), 1.0f, 0.8f + this.f_19796_.m_188501_() * 0.4f);
                    this.m_146850_(GameEvent.f_157778_);
                }
                this.m_9236_().m_7967_((Entity)tossedItem);
                this.setAttackDecision(this.getDartTarget());
            }
        }
        if (this.rideCooldown > 0) {
            --this.rideCooldown;
        }
        if (!this.m_9236_().f_46443_ && this.getAnimation() == NO_ANIMATION && this.m_217043_().m_188503_(300) == 0) {
            this.setAnimation(ANIMATION_HEADTILT);
        }
        if (!this.m_9236_().f_46443_ && this.isSitting()) {
            this.m_21573_().m_26573_();
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SCRATCH);
        }
        return true;
    }

    public void m_7023_(Vec3 vec3d) {
        if (this.isSitting()) {
            if (this.m_21573_().m_26570_() != null) {
                this.m_21573_().m_26573_();
            }
            vec3d = Vec3.f_82478_;
        }
        super.m_7023_(vec3d);
    }

    protected void m_5907_() {
        super.m_5907_();
        if (this.hasDart()) {
            this.m_19998_((ItemLike)AMItemRegistry.ANCIENT_DART.get());
        }
    }

    public void m_6083_() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.m_6084_()) {
            this.m_8127_();
        } else if (this.m_21824_() && entity instanceof LivingEntity && this.m_21830_((LivingEntity)entity)) {
            Entity mount;
            this.m_20334_(0.0, 0.0, 0.0);
            this.m_8119_();
            if (this.m_20159_() && (mount = this.m_20202_()) instanceof Player) {
                Player player = (Player)mount;
                this.f_20883_ = player.f_20883_;
                this.m_146922_(player.m_146908_());
                this.f_20885_ = player.f_20885_;
                this.f_19859_ = player.f_20885_;
                float radius = 0.0f;
                float angle = (float)Math.PI / 180 * (((LivingEntity)mount).f_20883_ - 180.0f);
                double extraX = 0.0f * Mth.m_14031_((float)((float)Math.PI + angle));
                double extraZ = 0.0f * Mth.m_14089_((float)angle);
                this.m_6034_(mount.m_20185_() + extraX, Math.max(mount.m_20186_() + (double)mount.m_20206_() + 0.1, mount.m_20186_()), mount.m_20189_() + extraZ);
                this.attackDecision = true;
                if (!mount.m_6084_() || this.rideCooldown == 0 && mount.m_6144_()) {
                    this.m_6038_();
                    this.attackDecision = false;
                }
            }
        } else {
            super.m_6083_();
        }
    }

    public void setAttackDecision(Entity target) {
        this.attackDecision = target instanceof Monster || this.hasDart() ? true : !this.attackDecision;
    }

    public int getCommand() {
        return (Integer)this.f_19804_.m_135370_(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.m_135381_(COMMAND, (Object)command);
    }

    public boolean isSitting() {
        return (Boolean)this.f_19804_.m_135370_(SITTING);
    }

    public void m_21839_(boolean sit) {
        this.f_19804_.m_135381_(SITTING, (Object)sit);
    }

    public boolean hasDartTarget() {
        return (Integer)this.f_19804_.m_135370_(DART_TARGET) != -1 && this.hasDart();
    }

    public void setDartTarget(Entity entity) {
        this.f_19804_.m_135381_(DART_TARGET, (Object)(entity == null ? -1 : entity.m_19879_()));
        if (entity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity)entity;
            this.m_6710_(target);
        }
    }

    @Nullable
    public Entity getDartTarget() {
        if (!this.hasDartTarget()) {
            return this.m_5448_();
        }
        Entity entity = this.m_9236_().m_6815_(((Integer)this.f_19804_.m_135370_(DART_TARGET)).intValue());
        if (entity == null || !entity.m_6084_()) {
            return this.m_5448_();
        }
        return entity;
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(COMMAND, (Object)0);
        this.f_19804_.m_135372_(DART_TARGET, (Object)-1);
        this.f_19804_.m_135372_(SITTING, (Object)false);
        this.f_19804_.m_135372_(DART, (Object)false);
        this.f_19804_.m_135372_(VARIANT, (Object)0);
    }

    public boolean hasDart() {
        return (Boolean)this.f_19804_.m_135370_(DART);
    }

    public void setDart(boolean dart) {
        this.f_19804_.m_135381_(DART, (Object)dart);
    }

    public int getVariant() {
        return (Integer)this.f_19804_.m_135370_(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.m_135381_(VARIANT, (Object)variant);
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        EntityCapuchinMonkey monkey = (EntityCapuchinMonkey)((EntityType)AMEntityRegistry.CAPUCHIN_MONKEY.get()).m_20615_((Level)p_241840_1_);
        monkey.setVariant(this.getVariant());
        return monkey;
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public boolean m_6673_(DamageSource source) {
        return source.m_276093_(DamageTypes.f_268612_) || super.m_6673_(source);
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (EntityCapuchinMonkey.isTameableFood(itemstack)) {
            if (!this.m_21824_()) {
                this.m_142075_(player, hand, itemstack);
                if (this.m_217043_().m_188503_(5) == 0) {
                    this.m_21828_(player);
                    this.m_9236_().m_7605_((Entity)this, (byte)7);
                } else {
                    this.m_9236_().m_7605_((Entity)this, (byte)6);
                }
                return InteractionResult.SUCCESS;
            }
            if (this.m_21824_() && this.getAllFoods().test(itemstack) && !this.m_6898_(itemstack) && this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_11788_, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0f);
                return InteractionResult.SUCCESS;
            }
        }
        InteractionResult interactionresult = itemstack.m_41647_(player, (LivingEntity)this, hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_((LivingEntity)player) && !this.m_6898_(itemstack) && !EntityCapuchinMonkey.isTameableFood(itemstack) && !this.getAllFoods().test(itemstack)) {
            boolean sit;
            if (!this.hasDart() && itemstack.m_41720_() == AMItemRegistry.ANCIENT_DART.get()) {
                this.setDart(true);
                this.m_142075_(player, hand, itemstack);
                return InteractionResult.CONSUME;
            }
            if (this.hasDart() && itemstack.m_204117_(Tags.Items.SHEARS)) {
                this.setDart(false);
                itemstack.m_41622_(1, (LivingEntity)this, p_233654_0_ -> {});
                return InteractionResult.SUCCESS;
            }
            if (player.m_6144_() && player.m_20197_().isEmpty()) {
                this.m_20329_((Entity)player);
                this.rideCooldown = 20;
                return InteractionResult.SUCCESS;
            }
            this.setCommand(this.getCommand() + 1);
            if (this.getCommand() == 3) {
                this.setCommand(0);
            }
            player.m_5661_((Component)Component.m_237110_((String)("entity.alexsmobs.all.command_" + this.getCommand()), (Object[])new Object[]{this.m_7755_()}), true);
            boolean bl = sit = this.getCommand() == 2;
            if (sit) {
                this.forcedSit = true;
                this.m_21839_(true);
            } else {
                this.forcedSit = false;
                this.m_21839_(false);
            }
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_THROW, ANIMATION_SCRATCH};
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return this.getAllFoods().test(stack) || EntityCapuchinMonkey.isTameableFood(stack);
    }

    public boolean m_6898_(ItemStack stack) {
        Item item = stack.m_41720_();
        return this.m_21824_() && stack.m_204117_(AMTagRegistry.CAPUCHIN_MONKEY_BREEDABLES);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_5634_(5.0f);
        this.m_146850_(GameEvent.f_157806_);
        this.m_5496_(SoundEvents.f_11788_, this.m_6121_(), this.m_6100_());
        if (e.m_32055_().m_204117_(AMTagRegistry.BANANAS) && this.m_217043_().m_188503_(4) == 0) {
            this.m_19983_(new ItemStack((ItemLike)AMBlockRegistry.BANANA_PEEL.get()));
        }
        Entity itemThrower = e.m_19749_();
        if (e.m_32055_().m_204117_(AMTagRegistry.CAPUCHIN_MONKEY_TAMEABLES) && itemThrower != null && !this.m_21824_()) {
            if (this.m_217043_().m_188503_(5) == 0) {
                this.m_7105_(true);
                this.m_21816_(itemThrower.m_20148_());
                this.m_9236_().m_7605_((Entity)this, (byte)7);
            } else {
                this.m_9236_().m_7605_((Entity)this, (byte)6);
            }
        }
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor world, DifficultyInstance diff, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        int i;
        if (data instanceof CapuchinGroupData) {
            i = ((CapuchinGroupData)((Object)data)).variant;
        } else {
            i = this.f_19796_.m_188503_(4);
            data = new CapuchinGroupData(i);
        }
        this.setVariant(i);
        return super.m_6518_(world, diff, spawnType, data, tag);
    }

    public static class CapuchinGroupData
    extends AgeableMob.AgeableMobGroupData {
        public final int variant;

        CapuchinGroupData(int variant) {
            super(true);
            this.variant = variant;
        }
    }
}

