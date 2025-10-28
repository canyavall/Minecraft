/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.AgeableMob$AgeableMobGroupData
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.ai.util.LandRandomPos
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRideParent;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GorillaAIChargeLooker;
import com.github.alexthe666.alexsmobs.entity.ai.GorillaAIFollowCaravan;
import com.github.alexthe666.alexsmobs.entity.ai.GorillaAIForageLeaves;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAITempt;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityGorilla
extends TamableAnimal
implements IAnimatedEntity,
ITargetsDroppedItems {
    public static final Animation ANIMATION_BREAKBLOCK_R = Animation.create((int)20);
    public static final Animation ANIMATION_BREAKBLOCK_L = Animation.create((int)20);
    public static final Animation ANIMATION_POUNDCHEST = Animation.create((int)40);
    public static final Animation ANIMATION_ATTACK = Animation.create((int)20);
    protected static final EntityDimensions SILVERBACK_SIZE = EntityDimensions.m_20395_((float)1.35f, (float)1.95f);
    private static final EntityDataAccessor<Boolean> SILVERBACK = SynchedEntityData.m_135353_(EntityGorilla.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.m_135353_(EntityGorilla.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.m_135353_(EntityGorilla.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.m_135353_(EntityGorilla.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public int maxStandTime = 75;
    public float prevStandProgress;
    public float prevSitProgress;
    public float standProgress;
    public float sitProgress;
    public boolean forcedSit = false;
    private int animationTick;
    private Animation currentAnimation;
    private int standingTime = 0;
    private int eatingTime;
    @Nullable
    private EntityGorilla caravanHead;
    @Nullable
    private EntityGorilla caravanTail;
    private int sittingTime = 0;
    private int maxSitTime = 75;
    @Nullable
    private UUID bananaThrowerID = null;
    private boolean hasSilverbackAttributes = false;
    public int poundChestCooldown = 0;

    protected EntityGorilla(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, -1.0f);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0f);
        this.m_274367_(1.1f);
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new AdvancedPathNavigateNoTeleport((Mob)this, worldIn, false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 30.0).m_22268_(Attributes.f_22277_, 32.0).m_22268_(Attributes.f_22284_, 0.0).m_22268_(Attributes.f_22281_, 7.0).m_22268_(Attributes.f_22278_, 0.5).m_22268_(Attributes.f_22279_, 0.25);
    }

    public static boolean isTameableFood(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.BANANAS);
    }

    public static boolean canGorillaSpawn(EntityType<EntityGorilla> gorilla, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.m_7495_());
        return (blockstate.m_204336_(AMTagRegistry.GORILLA_SPAWNS) || blockstate.m_60713_(Blocks.f_50016_)) && worldIn.m_45524_(p_223317_3_, 0) > 8;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.gorillaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public boolean m_6898_(ItemStack stack) {
        Item item = stack.m_41720_();
        return this.m_21824_() && stack.m_204117_(AMTagRegistry.GORILLA_BREEDABLES);
    }

    public int m_5792_() {
        return 8;
    }

    public boolean m_7296_(int sizeIn) {
        return false;
    }

    public boolean m_6469_(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        }
        Entity entity = source.m_7639_();
        this.m_21839_(false);
        if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
            amount = (amount + 1.0f) / 2.0f;
        }
        return super.m_6469_(source, amount);
    }

    protected void m_8099_() {
        super.m_8099_();
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new SitWhenOrderedToGoal((TamableAnimal)this));
        this.f_21345_.m_25352_(2, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.2, true));
        this.f_21345_.m_25352_(2, (Goal)new GorillaAIFollowCaravan(this, 0.8));
        this.f_21345_.m_25352_(3, (Goal)new GorillaAIChargeLooker(this, 1.6));
        this.f_21345_.m_25352_(4, (Goal)new TameableAITempt((Animal)this, 1.1, Ingredient.m_204132_(AMTagRegistry.GORILLA_TAMEABLES), false));
        this.f_21345_.m_25352_(4, (Goal)new AnimalAIRideParent((Animal)this, 1.25));
        this.f_21345_.m_25352_(6, (Goal)new AIWalkIdle(this, 0.8));
        this.f_21345_.m_25352_(5, (Goal)new GorillaAIForageLeaves(this));
        this.f_21345_.m_25352_(5, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21345_.m_25352_(8, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new CreatureAITargetItems((PathfinderMob)this, false));
        this.f_21346_.m_25352_(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]).m_26044_(new Class[0]));
        this.f_21346_.m_25352_(1, (Goal)new OwnerHurtByTargetGoal((TamableAnimal)this));
        this.f_21346_.m_25352_(2, (Goal)new OwnerHurtTargetGoal((TamableAnimal)this));
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.GORILLA_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.GORILLA_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.GORILLA_HURT.get();
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
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

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn instanceof AgeableMob.AgeableMobGroupData) {
            AgeableMob.AgeableMobGroupData lvt_6_1_ = (AgeableMob.AgeableMobGroupData)spawnDataIn;
            if (lvt_6_1_.m_146777_() == 0) {
                this.setSilverback(true);
            }
        } else {
            this.setSilverback(this.m_217043_().m_188499_());
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    public EntityGorilla getNearestSilverback(LevelAccessor world, double dist) {
        List list = world.m_45976_(this.getClass(), this.m_20191_().m_82377_(dist, dist / 2.0, dist));
        if (list.isEmpty()) {
            return null;
        }
        EntityGorilla gorilla = null;
        double d0 = Double.MAX_VALUE;
        for (EntityGorilla gorrila2 : list) {
            double d1;
            if (!gorrila2.isSilverback() || (d1 = this.m_20280_((Entity)gorrila2)) > d0) continue;
            d0 = d1;
            gorilla = gorrila2;
        }
        return gorilla;
    }

    public EntityDimensions m_6972_(Pose poseIn) {
        return this.isSilverback() && !this.m_6162_() ? SILVERBACK_SIZE.m_20388_(this.m_6134_()) : super.m_6972_(poseIn);
    }

    public void m_19956_(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            this.m_21839_(false);
            if (passenger instanceof EntityGorilla) {
                EntityGorilla babyGorilla = (EntityGorilla)passenger;
                babyGorilla.setStanding(this.isStanding());
                babyGorilla.m_21839_(this.isSitting());
                babyGorilla.f_20883_ = this.f_20883_;
            }
            float sitAdd = -0.03f * this.sitProgress;
            float standAdd = -0.03f * this.standProgress;
            float radius = standAdd + sitAdd;
            float angle = (float)Math.PI / 180 * this.f_20883_;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            passenger.m_6034_(this.m_20185_() + extraX, this.m_20186_() + this.m_6048_() + passenger.m_6049_(), this.m_20189_() + extraZ);
        }
    }

    public double m_6048_() {
        return (double)this.m_20206_() * (double)0.65f * (double)this.getGorillaScale() * (double)(this.isSilverback() ? 0.75f : 1.0f);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(SILVERBACK, (Object)false);
        this.f_19804_.m_135372_(STANDING, (Object)false);
        this.f_19804_.m_135372_(SITTING, (Object)false);
        this.f_19804_.m_135372_(EATING, (Object)false);
    }

    public boolean isSilverback() {
        return (Boolean)this.f_19804_.m_135370_(SILVERBACK);
    }

    public void setSilverback(boolean silver) {
        this.f_19804_.m_135381_(SILVERBACK, (Object)silver);
    }

    public boolean isStanding() {
        return (Boolean)this.f_19804_.m_135370_(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.m_135381_(STANDING, (Object)standing);
    }

    public boolean isSitting() {
        return (Boolean)this.f_19804_.m_135370_(SITTING);
    }

    public void m_21839_(boolean sit) {
        this.f_19804_.m_135381_(SITTING, (Object)sit);
    }

    public boolean isEating() {
        return (Boolean)this.f_19804_.m_135370_(EATING);
    }

    public void setEating(boolean eating) {
        this.f_19804_.m_135381_(EATING, (Object)eating);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Silverback", this.isSilverback());
        compound.m_128379_("Standing", this.isStanding());
        compound.m_128379_("GorillaSitting", this.isSitting());
        compound.m_128379_("ForcedToSit", this.forcedSit);
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setSilverback(compound.m_128471_("Silverback"));
        this.setStanding(compound.m_128471_("Standing"));
        this.m_21839_(compound.m_128471_("GorillaSitting"));
        this.forcedSit = compound.m_128471_("ForcedToSit");
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        if (itemstack.m_41720_() == Items.f_42656_) {
            return super.m_6071_(player, hand);
        }
        if (this.m_21824_() && EntityGorilla.isTameableFood(itemstack) && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(5.0f);
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.f_157806_);
            this.m_5496_(SoundEvents.f_11912_, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        }
        InteractionResult type = super.m_6071_(player, hand);
        InteractionResult interactionresult = itemstack.m_41647_(player, (LivingEntity)this, hand);
        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_((LivingEntity)player) && !this.m_6898_(itemstack)) {
            if (this.isSitting()) {
                this.forcedSit = false;
                this.m_21839_(false);
                return InteractionResult.SUCCESS;
            }
            this.forcedSit = true;
            this.m_21839_(true);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
        if (animation == ANIMATION_POUNDCHEST) {
            this.maxStandTime = 45;
            this.setStanding(true);
        }
        if (animation == ANIMATION_ATTACK) {
            this.maxStandTime = 10;
            this.setStanding(true);
        }
    }

    public void m_8119_() {
        super.m_8119_();
        if (!this.m_21120_(InteractionHand.MAIN_HAND).m_41619_() && this.canTargetItem(this.m_21120_(InteractionHand.MAIN_HAND))) {
            this.setEating(true);
            this.m_21839_(true);
            this.setStanding(false);
        }
        if (this.isEating() && !this.canTargetItem(this.m_21120_(InteractionHand.MAIN_HAND))) {
            this.setEating(false);
            this.eatingTime = 0;
            if (!this.forcedSit) {
                this.m_21839_(true);
            }
        }
        if (this.isEating()) {
            ++this.eatingTime;
            if (!this.m_21205_().m_204117_(ItemTags.f_13143_)) {
                for (int i = 0; i < 3; ++i) {
                    double d2 = this.f_19796_.m_188583_() * 0.02;
                    double d0 = this.f_19796_.m_188583_() * 0.02;
                    double d1 = this.f_19796_.m_188583_() * 0.02;
                    this.m_9236_().m_7106_((ParticleOptions)new ItemParticleOption(ParticleTypes.f_123752_, this.m_21120_(InteractionHand.MAIN_HAND)), this.m_20185_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, this.m_20186_() + (double)(this.m_20206_() * 0.5f) + (double)(this.f_19796_.m_188501_() * this.m_20206_() * 0.5f), this.m_20189_() + (double)(this.f_19796_.m_188501_() * this.m_20205_()) - (double)this.m_20205_() * 0.5, d0, d1, d2);
                }
            }
            if (this.eatingTime % 5 == 0) {
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_12181_, this.m_6121_(), this.m_6100_());
            }
            if (this.eatingTime > 100) {
                ItemStack stack = this.m_21120_(InteractionHand.MAIN_HAND);
                if (!stack.m_41619_()) {
                    this.m_5634_(4.0f);
                    if (EntityGorilla.isTameableFood(stack) && this.bananaThrowerID != null) {
                        if (this.m_217043_().m_188501_() < 0.3f) {
                            this.m_7105_(true);
                            this.m_21816_(this.bananaThrowerID);
                            Player player = this.m_9236_().m_46003_(this.bananaThrowerID);
                            if (player instanceof ServerPlayer) {
                                CriteriaTriggers.f_10590_.m_68829_((ServerPlayer)player, (Animal)this);
                            }
                            this.m_9236_().m_7605_((Entity)this, (byte)7);
                        } else {
                            this.m_9236_().m_7605_((Entity)this, (byte)6);
                        }
                    }
                    if (stack.hasCraftingRemainingItem()) {
                        this.m_19983_(stack.getCraftingRemainingItem());
                    }
                    stack.m_41774_(1);
                }
                this.eatingTime = 0;
            }
        }
        this.prevSitProgress = this.sitProgress;
        this.prevStandProgress = this.standProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 10.0f) {
                this.sitProgress += 1.0f;
            }
        } else if (this.sitProgress > 0.0f) {
            this.sitProgress -= 1.0f;
        }
        if (this.isStanding()) {
            if (this.standProgress < 10.0f) {
                this.standProgress += 1.0f;
            }
        } else if (this.standProgress > 0.0f) {
            this.standProgress -= 1.0f;
        }
        if (this.m_20159_() && this.m_20202_() instanceof EntityGorilla) {
            if (!this.m_6162_()) {
                this.m_6038_();
            } else {
                EntityGorilla mount = (EntityGorilla)this.m_20202_();
                this.m_146922_(mount.f_20883_);
                this.f_20885_ = mount.f_20883_;
                this.f_20883_ = mount.f_20883_;
            }
        }
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.m_188503_(50);
        }
        if (!this.forcedSit && this.isSitting() && ++this.sittingTime > this.maxSitTime) {
            this.m_21839_(false);
            this.sittingTime = 0;
            this.maxSitTime = 75 + this.f_19796_.m_188503_(50);
        }
        if (!this.forcedSit && this.isSitting() && (this.m_5448_() != null || this.isStanding()) && !this.isEating()) {
            this.m_21839_(false);
        }
        if (!(this.m_9236_().f_46443_ || this.getAnimation() != NO_ANIMATION || this.isStanding() || this.isSitting() || this.f_19796_.m_188503_(1500) != 0)) {
            this.maxSitTime = 300 + this.f_19796_.m_188503_(250);
            this.m_21839_(true);
        }
        if (this.forcedSit && !this.m_20160_() && this.m_21824_()) {
            this.m_21839_(true);
        }
        if (this.sitProgress == 0.0f && this.poundChestCooldown <= 0 && this.isSilverback() && this.f_19796_.m_188503_(800) == 0 && this.getAnimation() == NO_ANIMATION && !this.isSitting() && !this.m_21525_() && this.m_21205_().m_41619_()) {
            this.setAnimation(ANIMATION_POUNDCHEST);
        }
        if (!this.m_9236_().f_46443_ && this.m_5448_() != null && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 10) {
            float f1 = this.m_146908_() * ((float)Math.PI / 180);
            this.m_20256_(this.m_20184_().m_82520_((double)(-Mth.m_14031_((float)f1) * 0.02f), 0.0, (double)(Mth.m_14089_((float)f1) * 0.02f)));
            this.m_5448_().m_147240_(1.0, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
            this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21051_(Attributes.f_22281_).m_22115_());
        }
        if (!this.hasSilverbackAttributes && this.isSilverback() && !this.m_6162_()) {
            this.hasSilverbackAttributes = true;
            this.m_6210_();
            this.m_21051_(Attributes.f_22276_).m_22100_(50.0);
            this.m_21051_(Attributes.f_22281_).m_22100_(10.0);
            this.m_5634_(50.0f);
        }
        if (this.hasSilverbackAttributes && !this.isSilverback() && !this.m_6162_()) {
            this.hasSilverbackAttributes = false;
            this.m_6210_();
            this.m_21051_(Attributes.f_22276_).m_22100_(30.0);
            this.m_21051_(Attributes.f_22281_).m_22100_(8.0);
            this.m_5634_(30.0f);
        }
        if (this.poundChestCooldown > 0) {
            --this.poundChestCooldown;
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    @Nullable
    public LivingEntity m_6688_() {
        return null;
    }

    public PathNavigation m_21573_() {
        return this.f_21344_;
    }

    @Nullable
    public Entity m_275832_() {
        return this.m_20202_() instanceof EntityGorilla ? null : super.m_275832_();
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.GORILLA_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity targetEntity) {
        ItemStack duplicate = targetEntity.m_32055_().m_41777_();
        duplicate.m_41764_(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).m_41619_() && !this.m_9236_().f_46443_) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0f);
        }
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
        Entity thrower = targetEntity.m_19749_();
        if (EntityGorilla.isTameableFood(targetEntity.m_32055_()) && thrower != null && !this.m_21824_()) {
            this.bananaThrowerID = thrower.m_20148_();
        }
    }

    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_BREAKBLOCK_R, ANIMATION_BREAKBLOCK_L, ANIMATION_POUNDCHEST, ANIMATION_ATTACK};
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return (AgeableMob)((EntityType)AMEntityRegistry.GORILLA.get()).m_20615_((Level)p_241840_1_);
    }

    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }

    public void joinCaravan(EntityGorilla caravanHeadIn) {
        this.caravanHead = caravanHeadIn;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }

    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityGorilla getCaravanHead() {
        return this.caravanHead;
    }

    public float getGorillaScale() {
        return this.m_6162_() ? 0.5f : (this.isSilverback() ? 1.3f : 1.0f);
    }

    public boolean isDonkeyKong() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("donkey") && s.toLowerCase().contains("kong") || s.equalsIgnoreCase("dk"));
    }

    public boolean isFunkyKong() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("funky") && s.toLowerCase().contains("kong");
    }

    private class AIWalkIdle
    extends RandomStrollGoal {
        public AIWalkIdle(EntityGorilla entityGorilla2, double v) {
            super((PathfinderMob)entityGorilla2, v);
        }

        public boolean m_8036_() {
            this.f_25730_ = EntityGorilla.this.isSilverback() ? 10 : 120;
            return super.m_8036_();
        }

        @Nullable
        protected Vec3 m_7037_() {
            return LandRandomPos.m_148488_((PathfinderMob)this.f_25725_, (int)(EntityGorilla.this.isSilverback() ? 25 : 10), (int)7);
        }
    }
}

