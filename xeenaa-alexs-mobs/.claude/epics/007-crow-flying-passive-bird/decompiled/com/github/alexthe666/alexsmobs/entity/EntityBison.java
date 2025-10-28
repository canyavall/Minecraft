/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.animation.Animation
 *  com.github.alexthe666.citadel.animation.AnimationHandler
 *  com.github.alexthe666.citadel.animation.IAnimatedEntity
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.AgeableMob$AgeableMobGroupData
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.Shearable
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.FollowParentGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.Wolf
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.ShovelItem
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SnowLayerBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.common.ForgeHooks
 *  net.minecraftforge.common.IForgeShearable
 *  net.minecraftforge.event.ForgeEventFactory
 *  net.minecraftforge.event.entity.living.LivingKnockBackEvent
 *  org.jetbrains.annotations.Nullable
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Shearable;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import org.jetbrains.annotations.Nullable;

public class EntityBison
extends Animal
implements IAnimatedEntity,
Shearable,
IForgeShearable {
    public static final Animation ANIMATION_PREPARE_CHARGE = Animation.create((int)40);
    public static final Animation ANIMATION_EAT = Animation.create((int)35);
    public static final Animation ANIMATION_ATTACK = Animation.create((int)15);
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.m_135353_(EntityBison.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> SNOWY = SynchedEntityData.m_135353_(EntityBison.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.m_135353_(EntityBison.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public float prevChargeProgress;
    public float chargeProgress;
    private int animationTick;
    private Animation currentAnimation;
    private int snowTimer = 0;
    private boolean permSnow = false;
    private int blockBreakCounter;
    private int chargeCooldown = this.f_19796_.m_188503_(2000);
    private EntityBison chargePartner;
    private boolean hasChargedSpeed = false;
    private int feedingsSinceLastShear = 0;

    protected EntityBison(EntityType<? extends Animal> animal, Level lvl) {
        super(animal, lvl);
        this.m_274367_(1.1f);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 40.0).m_22268_(Attributes.f_22281_, 8.0).m_22268_(Attributes.f_22277_, 32.0).m_22268_(Attributes.f_22279_, 0.25).m_22268_(Attributes.f_22282_, 2.0);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.bisonSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMob.AgeableMobGroupData(0.25f);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.BISON_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.BISON_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.BISON_HURT.get();
    }

    protected void m_7355_(BlockPos p_28301_, BlockState p_28302_) {
        this.m_5496_(SoundEvents.f_11834_, 0.1f, 1.0f);
    }

    public boolean isSnowy() {
        return (Boolean)this.f_19804_.m_135370_(SNOWY);
    }

    public void setSnowy(boolean honeyed) {
        this.f_19804_.m_135381_(SNOWY, (Object)honeyed);
    }

    protected void m_8099_() {
        super.m_8099_();
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.0, true));
        this.f_21345_.m_25352_(3, (Goal)new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.m_25352_(4, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_204132_(AMTagRegistry.BISON_BREEDABLES), false));
        this.f_21345_.m_25352_(5, (Goal)new FollowParentGoal((Animal)this, 1.1));
        this.f_21345_.m_25352_(6, (Goal)new AIChargeFurthest());
        this.f_21345_.m_25352_(7, (Goal)new AnimalAIWanderRanged((PathfinderMob)this, 70, 1.0, 18, 7));
        this.f_21345_.m_25352_(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 15.0f));
        this.f_21345_.m_25352_(7, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new AIAttackNearPlayers());
        this.f_21346_.m_25352_(2, (Goal)new AnimalAIHurtByTargetNotBaby(this, new Class[0]));
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.BISON_BREEDABLES);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(SHEARED, (Object)false);
        this.f_19804_.m_135372_(SNOWY, (Object)false);
        this.f_19804_.m_135372_(CHARGING, (Object)false);
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel level, AgeableMob mob) {
        return (AgeableMob)((EntityType)AMEntityRegistry.BISON.get()).m_20615_(this.m_9236_());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setSnowy(compound.m_128471_("Snowy"));
        this.setSheared(compound.m_128471_("Sheared"));
        this.permSnow = compound.m_128471_("SnowPerm");
        this.chargeCooldown = compound.m_128451_("ChargeCooldown");
        this.feedingsSinceLastShear = compound.m_128451_("Feedings");
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Snowy", this.isSnowy());
        compound.m_128379_("Sheared", this.isSheared());
        compound.m_128379_("SnowPerm", this.permSnow);
        compound.m_128405_("ChargeCooldown", this.chargeCooldown);
        compound.m_128405_("Feedings", this.feedingsSinceLastShear);
    }

    protected PathNavigation m_6037_(Level worldIn) {
        return new AdvancedPathNavigateNoTeleport((Mob)this, worldIn, true);
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevChargeProgress = this.chargeProgress;
        if (this.isCharging() && this.chargeProgress < 5.0f) {
            this.chargeProgress += 1.0f;
        }
        if (!this.isCharging() && this.chargeProgress > 0.0f) {
            this.chargeProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            if (this.snowTimer == 0) {
                this.snowTimer = 200 + this.f_19796_.m_188503_(400);
                if (this.isSnowy()) {
                    if (!(this.permSnow || this.m_20094_() <= 0 && !this.m_20072_() && EntityGrizzlyBear.isSnowingAt(this.m_9236_(), this.m_20183_().m_7494_()))) {
                        this.setSnowy(false);
                    }
                } else if (EntityGrizzlyBear.isSnowingAt(this.m_9236_(), this.m_20183_())) {
                    this.setSnowy(true);
                }
            }
            LivingEntity attackTarget = this.m_5448_();
            if (this.m_20184_().m_82556_() < 0.05 && this.getAnimation() == NO_ANIMATION && (attackTarget == null || !attackTarget.m_6084_()) && this.m_217043_().m_188503_(600) == 0 && this.m_9236_().m_8055_(this.m_20183_().m_7495_()).m_60713_(Blocks.f_50440_)) {
                this.setAnimation(ANIMATION_EAT);
            }
            if (this.getAnimation() == ANIMATION_EAT && this.getAnimationTick() == 30 && this.m_9236_().m_8055_(this.m_20183_().m_7495_()).m_60713_(Blocks.f_50440_)) {
                ++this.feedingsSinceLastShear;
                BlockPos down = this.m_20183_().m_7495_();
                this.m_9236_().m_46796_(2001, down, Block.m_49956_((BlockState)Blocks.f_50440_.m_49966_()));
                this.m_9236_().m_7731_(down, Blocks.f_50493_.m_49966_(), 2);
            }
            if (this.isCharging()) {
                if (!this.hasChargedSpeed) {
                    this.m_21051_(Attributes.f_22279_).m_22100_((double)0.65f);
                    this.hasChargedSpeed = true;
                }
            } else if (this.hasChargedSpeed) {
                this.m_21051_(Attributes.f_22279_).m_22100_(0.25);
                this.hasChargedSpeed = false;
            }
            if (attackTarget != null && attackTarget.m_6084_() && this.m_6084_()) {
                double dist = this.m_20270_((Entity)attackTarget);
                if (this.m_142582_((Entity)attackTarget)) {
                    this.m_21391_((Entity)attackTarget, 30.0f, 30.0f);
                    this.f_20883_ = this.m_146908_();
                }
                if (dist < (double)(this.m_20205_() + 3.0f)) {
                    if (this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() > 8 && dist < (double)(this.m_20205_() + 1.0f) && this.m_142582_((Entity)attackTarget)) {
                        float dmg = (float)this.m_21051_(Attributes.f_22281_).m_22115_();
                        if (attackTarget instanceof Wolf) {
                            dmg = 2.0f;
                        }
                        this.launch((Entity)attackTarget, this.isCharging());
                        if (this.isCharging()) {
                            dmg += 3.0f;
                            this.setCharging(false);
                        }
                        attackTarget.m_6469_(this.m_269291_().m_269333_((LivingEntity)this), dmg);
                    }
                } else if (!this.isCharging()) {
                    Animation animation = this.getAnimation();
                    if (animation == NO_ANIMATION) {
                        this.setAnimation(ANIMATION_PREPARE_CHARGE);
                    } else if (animation == ANIMATION_PREPARE_CHARGE) {
                        this.m_21573_().m_26573_();
                        if (this.getAnimationTick() > 30) {
                            this.setCharging(true);
                        }
                    }
                }
            }
        }
        if (this.chargeCooldown > 0) {
            --this.chargeCooldown;
        }
        if (this.feedingsSinceLastShear >= 5 && this.isSheared()) {
            this.feedingsSinceLastShear = 0;
            this.setSheared(false);
        }
        if (!this.m_9236_().f_46443_ && this.isCharging() && (this.m_5448_() == null && this.chargePartner == null || this.m_20072_())) {
            this.setCharging(false);
        }
        AnimationHandler.INSTANCE.updateAnimations((Entity)this);
    }

    public boolean m_7327_(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    public boolean isSheared() {
        return (Boolean)this.f_19804_.m_135370_(SHEARED);
    }

    public void setSheared(boolean b) {
        this.f_19804_.m_135381_(SHEARED, (Object)b);
    }

    private void launch(Entity launch, boolean huge) {
        float rot = 180.0f + this.m_146908_();
        float hugeScale = huge ? 4.0f : 0.6f;
        float strength = (float)((double)hugeScale * (1.0 - ((LivingEntity)launch).m_21133_(Attributes.f_22278_)));
        float rotRad = rot * ((float)Math.PI / 180);
        float x = Mth.m_14031_((float)rotRad);
        float z = -Mth.m_14089_((float)rotRad);
        launch.f_19812_ = true;
        Vec3 vec3 = this.m_20184_();
        Vec3 vec31 = vec3.m_82549_(new Vec3((double)x, 0.0, (double)z).m_82541_().m_82490_((double)strength));
        launch.m_20334_(vec31.f_82479_, huge ? 1.0 : 0.5, vec31.f_82481_);
        launch.m_6853_(false);
    }

    private void knockbackTarget(LivingEntity entity, float strength, float angle) {
        float rot = this.m_146908_() + angle;
        if (entity != null) {
            entity.m_147240_((double)strength, (double)Mth.m_14031_((float)(rot * ((float)Math.PI / 180))), (double)(-Mth.m_14089_((float)(rot * ((float)Math.PI / 180)))));
        }
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_9236_().f_46443_) {
            if (item == Items.f_41979_ && !this.isSnowy()) {
                this.m_142075_(player, hand, itemstack);
                this.permSnow = true;
                this.setSnowy(true);
                this.m_5496_(SoundEvents.f_12482_, this.m_6121_(), this.m_6100_());
                this.m_146850_(GameEvent.f_223708_);
                return InteractionResult.SUCCESS;
            }
            if (item instanceof ShovelItem && this.isSnowy()) {
                this.permSnow = false;
                if (!player.m_7500_()) {
                    itemstack.m_220157_(1, this.m_217043_(), player instanceof ServerPlayer ? (ServerPlayer)player : null);
                }
                this.setSnowy(false);
                this.m_5496_(SoundEvents.f_12474_, this.m_6121_(), this.m_6100_());
                this.m_146850_(GameEvent.f_223708_);
                return InteractionResult.SUCCESS;
            }
        }
        return type;
    }

    public void m_8024_() {
        super.m_8024_();
        this.breakBlock();
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            return;
        }
        boolean flag = false;
        if (!this.m_9236_().f_46443_ && this.blockBreakCounter == 0 && ForgeEventFactory.getMobGriefingEvent((Level)this.m_9236_(), (Entity)this)) {
            for (int a = (int)Math.round(this.m_20191_().f_82288_); a <= (int)Math.round(this.m_20191_().f_82291_); ++a) {
                for (int b = (int)Math.round(this.m_20191_().f_82289_) - 1; b <= (int)Math.round(this.m_20191_().f_82292_) + 1 && b <= 127; ++b) {
                    for (int c = (int)Math.round(this.m_20191_().f_82290_); c <= (int)Math.round(this.m_20191_().f_82293_); ++c) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = this.m_9236_().m_8055_(pos);
                        Block block = state.m_60734_();
                        if (block != Blocks.f_50125_ || (Integer)state.m_61143_((Property)SnowLayerBlock.f_56581_) > 1) continue;
                        this.m_20256_(this.m_20184_().m_82542_((double)0.6f, 1.0, (double)0.6f));
                        flag = true;
                        this.m_9236_().m_46961_(pos, true);
                    }
                }
            }
        }
        if (flag) {
            this.blockBreakCounter = this.isCharging() && this.m_5448_() != null ? 2 : 20;
        }
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
        return new Animation[]{ANIMATION_PREPARE_CHARGE, ANIMATION_ATTACK, ANIMATION_EAT};
    }

    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.m_6220_();
    }

    public void m_5851_(SoundSource category) {
        this.m_9236_().m_6269_(null, (Entity)this, SoundEvents.f_12344_, category, 1.0f, 1.0f);
        this.m_146850_(GameEvent.f_223708_);
        this.setSheared(true);
        this.feedingsSinceLastShear = 0;
        for (int i = 0; i < 2 + this.f_19796_.m_188503_(2); ++i) {
            this.m_19998_((ItemLike)AMItemRegistry.BISON_FUR.get());
        }
    }

    public boolean isCharging() {
        return (Boolean)this.f_19804_.m_135370_(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.f_19804_.m_135381_(CHARGING, (Object)charging);
    }

    public boolean m_6220_() {
        return !this.isSheared() && !this.m_6162_();
    }

    @Nonnull
    public List<ItemStack> onSheared(@javax.annotation.Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.m_6269_(null, (Entity)this, SoundEvents.f_12344_, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0f, 1.0f);
        this.m_146850_(GameEvent.f_223708_);
        ArrayList<ItemStack> list = new ArrayList<ItemStack>(6);
        for (int i = 0; i < 2 + this.f_19796_.m_188503_(2); ++i) {
            list.add(new ItemStack((ItemLike)AMItemRegistry.BISON_FUR.get()));
        }
        this.feedingsSinceLastShear = 0;
        this.setSheared(true);
        return list;
    }

    public boolean isValidCharging() {
        return !this.m_6162_() && this.m_6084_() && this.chargeCooldown == 0 && !this.m_20072_();
    }

    public void pushBackJostling(EntityBison bison, float strength) {
        this.applyKnockbackFromBuffalo(strength, bison.m_20185_() - this.m_20185_(), bison.m_20189_() - this.m_20189_());
    }

    private void applyKnockbackFromBuffalo(float strength, double ratioX, double ratioZ) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack((LivingEntity)this, (float)strength, (double)ratioX, (double)ratioZ);
        if (event.isCanceled()) {
            return;
        }
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        if (!(strength <= 0.0f)) {
            this.f_19812_ = true;
            Vec3 vector3d = this.m_20184_();
            Vec3 vector3d1 = new Vec3(ratioX, 0.0, ratioZ).m_82541_().m_82490_((double)strength);
            this.m_20334_(vector3d.f_82479_ / 2.0 - vector3d1.f_82479_, 0.3f, vector3d.f_82481_ / 2.0 - vector3d1.f_82481_);
        }
    }

    private void resetChargeCooldown() {
        this.setCharging(false);
        this.chargePartner = null;
        this.chargeCooldown = 1000 + this.f_19796_.m_188503_(2000);
    }

    private class AIChargeFurthest
    extends Goal {
        public AIChargeFurthest() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean m_8036_() {
            if (EntityBison.this.isValidCharging()) {
                if (EntityBison.this.chargePartner != null && EntityBison.this.chargePartner.isValidCharging() && EntityBison.this.chargePartner != EntityBison.this) {
                    EntityBison.this.chargePartner.chargePartner = EntityBison.this;
                    return true;
                }
                if (EntityBison.this.f_19796_.m_188503_(100) == 0) {
                    EntityBison furthest = null;
                    for (EntityBison bison : EntityBison.this.m_9236_().m_45976_(EntityBison.class, EntityBison.this.m_20191_().m_82400_(15.0))) {
                        if (bison.chargeCooldown != 0 || bison.m_6162_() || bison.m_7306_((Entity)EntityBison.this) || furthest != null && !(EntityBison.this.m_20270_((Entity)furthest) < EntityBison.this.m_20270_((Entity)bison))) continue;
                        furthest = bison;
                    }
                    if (furthest != null && furthest != EntityBison.this) {
                        EntityBison.this.chargePartner = furthest;
                        furthest.chargePartner = EntityBison.this;
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean m_8045_() {
            return EntityBison.this.isValidCharging() && EntityBison.this.chargePartner != null && EntityBison.this.chargePartner.isValidCharging() && !EntityBison.this.chargePartner.m_7306_((Entity)EntityBison.this);
        }

        public void m_8037_() {
            EntityBison.this.m_21391_((Entity)EntityBison.this.chargePartner, 30.0f, 30.0f);
            EntityBison.this.f_20883_ = EntityBison.this.m_146908_();
            if (!EntityBison.this.isCharging()) {
                Animation bisonAnimation = EntityBison.this.getAnimation();
                if (bisonAnimation == IAnimatedEntity.NO_ANIMATION || bisonAnimation == ANIMATION_PREPARE_CHARGE && EntityBison.this.getAnimationTick() > 35) {
                    EntityBison.this.setCharging(true);
                }
            } else {
                float dist = EntityBison.this.m_20270_((Entity)EntityBison.this.chargePartner);
                EntityBison.this.m_21573_().m_5624_((Entity)EntityBison.this.chargePartner, 1.0);
                if (EntityBison.this.m_142582_((Entity)EntityBison.this.chargePartner)) {
                    float flingAnimAt = EntityBison.this.m_20205_() + 1.0f;
                    if (dist < flingAnimAt && EntityBison.this.getAnimation() == ANIMATION_ATTACK) {
                        if (EntityBison.this.getAnimationTick() > 8) {
                            boolean flag = false;
                            if (EntityBison.this.m_20096_()) {
                                EntityBison.this.pushBackJostling(EntityBison.this.chargePartner, 0.2f);
                                flag = true;
                            }
                            if (EntityBison.this.chargePartner.m_20096_()) {
                                EntityBison.this.chargePartner.pushBackJostling(EntityBison.this, 0.9f);
                                flag = true;
                            }
                            if (flag) {
                                EntityBison.this.resetChargeCooldown();
                            }
                        }
                    } else {
                        float startFlingAnimAt = EntityBison.this.m_20205_() + 3.0f;
                        if (dist < startFlingAnimAt && EntityBison.this.getAnimation() != ANIMATION_ATTACK) {
                            EntityBison.this.setAnimation(ANIMATION_ATTACK);
                        }
                    }
                }
            }
        }
    }

    class AIAttackNearPlayers
    extends NearestAttackableTargetGoal<Player> {
        public AIAttackNearPlayers() {
            super((Mob)EntityBison.this, Player.class, 80, true, true, null);
        }

        public boolean m_8036_() {
            if (EntityBison.this.m_6162_() || EntityBison.this.m_27593_()) {
                return false;
            }
            return super.m_8036_();
        }

        protected double m_7623_() {
            return 3.0;
        }
    }
}

