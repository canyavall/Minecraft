/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicates
 *  javax.annotation.Nullable
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.FlyingAnimal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCosmicCod;
import com.github.alexthe666.alexsmobs.entity.IFollower;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAITemptDistance;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAIFollowOwner;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.base.Predicates;
import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCosmaw
extends TamableAnimal
implements ITargetsDroppedItems,
FlyingAnimal,
IFollower {
    private static final EntityDataAccessor<Float> COSMAW_PITCH = SynchedEntityData.m_135353_(EntityCosmaw.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.m_135353_(EntityCosmaw.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.m_135353_(EntityCosmaw.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.m_135353_(EntityCosmaw.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    public float clutchProgress;
    public float prevClutchProgress;
    public float openProgress;
    public float prevOpenProgress;
    public float prevCosmawPitch;
    public float biteProgress;
    public float prevBiteProgress;
    private float stuckRot;
    private UUID fishThrowerID;
    private int heldItemTime;
    private BlockPos lastSafeTpPosition;

    protected EntityCosmaw(EntityType<? extends TamableAnimal> type, Level lvl) {
        super(type, lvl);
        this.stuckRot = this.f_19796_.m_188503_(3) * 90;
        this.f_21342_ = new FlightMoveController((Mob)this, 1.0f, false, true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 20.0).m_22268_(Attributes.f_22277_, 32.0).m_22268_(Attributes.f_22281_, 1.0).m_22268_(Attributes.f_22279_, (double)0.3f);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.cosmawSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canCosmawSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return !worldIn.m_8055_(pos.m_7495_()).m_60795_();
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(COSMAW_PITCH, (Object)Float.valueOf(0.0f));
        this.f_19804_.m_135372_(ATTACK_TICK, (Object)0);
        this.f_19804_.m_135372_(COMMAND, (Object)0);
        this.f_19804_.m_135372_(SITTING, (Object)false);
    }

    protected void m_6088_() {
    }

    public boolean m_7327_(Entity entityIn) {
        if ((Integer)this.f_19804_.m_135370_(ATTACK_TICK) == 0 && this.biteProgress == 0.0f) {
            this.f_19804_.m_135381_(ATTACK_TICK, (Object)5);
        }
        return true;
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new AIAttack());
        this.f_21345_.m_25352_(2, (Goal)new SitWhenOrderedToGoal((TamableAnimal)this));
        this.f_21345_.m_25352_(3, (Goal)new FlyingAIFollowOwner(this, 1.3, 8.0f, 4.0f, false));
        this.f_21345_.m_25352_(4, (Goal)new AIPickupOwner());
        this.f_21345_.m_25352_(5, (Goal)new BreedGoal((Animal)this, 1.2));
        this.f_21345_.m_25352_(6, (Goal)new AnimalAITemptDistance((PathfinderMob)this, 1.1, Ingredient.m_204132_(AMTagRegistry.COSMAW_FOODSTUFFS), false, 25.0){

            @Override
            public boolean m_8036_() {
                return super.m_8036_() && EntityCosmaw.this.m_21205_().m_41619_();
            }

            @Override
            public boolean m_8045_() {
                return super.m_8045_() && EntityCosmaw.this.m_21205_().m_41619_();
            }
        });
        this.f_21345_.m_25352_(7, (Goal)new RandomFlyGoal(this));
        this.f_21345_.m_25352_(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0f));
        this.f_21345_.m_25352_(9, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new CreatureAITargetItems((PathfinderMob)this, true));
        this.f_21346_.m_25352_(2, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]){

            public boolean m_8036_() {
                LivingEntity livingentity = this.f_26135_.m_21188_();
                if (livingentity != null && EntityCosmaw.this.m_21830_(livingentity)) {
                    return false;
                }
                return super.m_8036_();
            }
        });
        this.f_21346_.m_25352_(3, new EntityAINearestTarget3D<EntityCosmicCod>((Mob)this, EntityCosmicCod.class, 80, true, false, (Predicate<LivingEntity>)Predicates.alwaysTrue()));
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.COSMAW_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.COSMAW_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.COSMAW_HURT.get();
    }

    public boolean m_6898_(ItemStack stack) {
        return this.m_21824_() && stack.m_204117_(AMTagRegistry.COSMAW_BREEDABLES);
    }

    public boolean m_20068_() {
        return true;
    }

    public boolean m_21526_() {
        return false;
    }

    @Nullable
    public LivingEntity m_6688_() {
        return null;
    }

    public float getClampedCosmawPitch(float partialTick) {
        float f = this.prevCosmawPitch + (this.getCosmawPitch() - this.prevCosmawPitch) * partialTick;
        return Mth.m_14036_((float)f, (float)-90.0f, (float)90.0f);
    }

    public float getCosmawPitch() {
        return ((Float)this.f_19804_.m_135370_(COSMAW_PITCH)).floatValue();
    }

    public void setCosmawPitch(float pitch) {
        this.f_19804_.m_135381_(COSMAW_PITCH, (Object)Float.valueOf(pitch));
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

    public void m_19956_(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float f = this.f_267362_.m_267756_();
            float f1 = this.f_267362_.m_267731_();
            float bob = (float)(Math.sin(f * 0.7f) * (double)f1 * 0.0625 * (double)1.6f - (double)(f1 * 0.0625f * 1.6f));
            passenger.m_6034_(this.m_20185_(), this.m_20186_() - (double)bob + (double)0.3f - this.m_6048_(), this.m_20189_());
        }
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("CosmawSitting", this.isSitting());
        compound.m_128405_("Command", this.getCommand());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.m_21839_(compound.m_128471_("CosmawSitting"));
        this.setCommand(compound.m_128451_("Command"));
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevOpenProgress = this.openProgress;
        this.prevClutchProgress = this.clutchProgress;
        this.prevBiteProgress = this.biteProgress;
        this.prevCosmawPitch = this.getCosmawPitch();
        if (!this.m_9236_().f_46443_) {
            float f2 = -((float)this.m_20184_().f_82480_ * 57.295776f);
            this.setCosmawPitch(this.getCosmawPitch() + 0.6f * (this.getCosmawPitch() + f2) - this.getCosmawPitch());
        }
        if (this.isMouthOpen()) {
            if (this.openProgress < 5.0f) {
                this.openProgress += 1.0f;
            }
        } else if (this.openProgress > 0.0f) {
            this.openProgress -= 1.0f;
        }
        if (this.m_20160_()) {
            if (this.clutchProgress < 5.0f) {
                this.clutchProgress += 1.0f;
            }
        } else if (this.clutchProgress > 0.0f) {
            this.clutchProgress -= 1.0f;
        }
        if ((Integer)this.f_19804_.m_135370_(ATTACK_TICK) > 0) {
            if (this.biteProgress < 5.0f) {
                this.biteProgress = Math.min(5.0f, this.biteProgress + 2.0f);
            } else {
                if (this.m_5448_() != null && (double)this.m_20270_((Entity)this.m_5448_()) < 3.3) {
                    if (this.m_5448_() instanceof EntityCosmicCod && !this.m_21824_()) {
                        EntityCosmicCod fish = (EntityCosmicCod)this.m_5448_();
                        CompoundTag fishNbt = new CompoundTag();
                        fish.m_7380_(fishNbt);
                        fishNbt.m_128359_("DeathLootTable", BuiltInLootTables.f_78712_.toString());
                        fish.m_7378_(fishNbt);
                    }
                    this.m_5448_().m_6469_(this.m_269291_().m_269333_((LivingEntity)this), (float)this.m_21133_(Attributes.f_22281_));
                }
                this.f_19804_.m_135381_(ATTACK_TICK, (Object)((Integer)this.f_19804_.m_135370_(ATTACK_TICK) - 1));
            }
        } else if (this.biteProgress > 0.0f) {
            this.biteProgress -= 1.0f;
        }
        if (!this.m_21205_().m_41619_()) {
            ++this.heldItemTime;
            if (this.heldItemTime > 30 && this.canTargetItem(this.m_21205_())) {
                this.heldItemTime = 0;
                this.m_5634_(4.0f);
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_11803_, this.m_6121_(), this.m_6100_());
                if (this.m_21205_().m_204117_(AMTagRegistry.COSMAW_TAMEABLES) && this.fishThrowerID != null && !this.m_21824_()) {
                    if (this.m_217043_().m_188501_() < 0.3f) {
                        this.m_7105_(true);
                        this.setCommand(1);
                        this.m_21816_(this.fishThrowerID);
                        Player player = this.m_9236_().m_46003_(this.fishThrowerID);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.f_10590_.m_68829_((ServerPlayer)player, (Animal)this);
                        }
                        this.m_9236_().m_7605_((Entity)this, (byte)7);
                    } else {
                        this.m_9236_().m_7605_((Entity)this, (byte)6);
                    }
                }
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                this.m_21205_().m_41774_(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (!this.m_9236_().f_46443_) {
            BlockPos pos;
            if ((this.f_19797_ % 100 == 0 || this.lastSafeTpPosition == null) && (pos = this.getCosmawGround(this.m_20183_())).m_123342_() > 1) {
                this.lastSafeTpPosition = pos;
            }
            if (this.m_20160_()) {
                if (this.lastSafeTpPosition != null) {
                    double dist = this.m_20238_(Vec3.m_82512_((Vec3i)this.lastSafeTpPosition));
                    float speed = 0.8f;
                    if (this.m_20186_() < -40.0) {
                        speed = 3.0f;
                    }
                    if (this.f_19863_ && dist > 14.0) {
                        this.m_146922_(this.stuckRot);
                        if (this.f_19796_.m_188503_(50) == 0) {
                            this.stuckRot = Mth.m_14177_((float)(this.stuckRot + 90.0f));
                        }
                        float angle = (float)Math.PI / 180 * this.stuckRot;
                        double extraX = -2.0f * Mth.m_14031_((float)((float)Math.PI + angle));
                        double extraZ = -2.0f * Mth.m_14089_((float)angle);
                        this.m_21566_().m_6849_(this.m_20185_() + extraX, this.m_20186_() + 2.0, this.m_20189_() + extraZ, (double)speed);
                    } else if ((double)this.lastSafeTpPosition.m_123342_() > this.m_20186_() + (double)2.3f) {
                        this.m_21566_().m_6849_(this.m_20185_(), this.m_20186_() + 2.0, this.m_20189_(), (double)speed);
                    } else {
                        this.m_21566_().m_6849_((double)this.lastSafeTpPosition.m_123341_(), (double)(this.lastSafeTpPosition.m_123342_() + 2), (double)this.lastSafeTpPosition.m_123343_(), (double)speed);
                    }
                    if (dist < 7.0 && this.getCosmawGround(this.m_20183_()).m_123342_() > 1) {
                        this.m_20153_();
                    }
                } else if (this.m_20186_() < 0.0) {
                    this.m_20184_().m_82520_(0.0, 0.75, 0.0);
                } else if (this.m_20186_() < 80.0) {
                    this.m_20184_().m_82520_(0.0, (double)0.1f, 0.0);
                }
            }
        }
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        InteractionResult interactionresult = stack.m_41647_(player, (LivingEntity)this, hand);
        if (this.canTargetItem(stack) && this.m_21205_().m_41619_()) {
            ItemStack rippedStack = stack.m_41777_();
            rippedStack.m_41764_(1);
            stack.m_41774_(1);
            this.m_21008_(InteractionHand.MAIN_HAND, rippedStack);
            if (rippedStack.m_204117_(AMTagRegistry.COSMAW_TAMEABLES)) {
                this.fishThrowerID = player.m_20148_();
            }
            return InteractionResult.SUCCESS;
        }
        if (this.m_21824_() && this.m_21830_((LivingEntity)player) && !this.m_6162_() && interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS) {
            boolean sit;
            this.setCommand(this.getCommand() + 1);
            if (this.getCommand() == 3) {
                this.setCommand(0);
            }
            player.m_5661_((Component)Component.m_237110_((String)("entity.alexsmobs.all.command_" + this.getCommand()), (Object[])new Object[]{this.m_7755_()}), true);
            boolean bl = sit = this.getCommand() == 2;
            if (sit) {
                this.m_21839_(true);
                return InteractionResult.SUCCESS;
            }
            this.m_21839_(false);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean isMouthOpen() {
        return !this.m_21205_().m_41619_();
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    protected PathNavigation m_6037_(Level level) {
        return new DirectPathNavigator((Mob)this, level, 0.5f);
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

    @Nullable
    public AgeableMob m_142606_(ServerLevel level, AgeableMob parent) {
        return (AgeableMob)((EntityType)AMEntityRegistry.COSMAW.get()).m_20615_(this.m_9236_());
    }

    private BlockPos getCosmawGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int)this.m_20186_(), in.m_123343_());
        while (position.m_123342_() < 256 && !this.m_9236_().m_6425_(position).m_76178_()) {
            position = position.m_7494_();
        }
        while (position.m_123342_() > 1 && this.m_9236_().m_46859_(position)) {
            position = position.m_7495_();
        }
        return position;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.COSMAW_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.m_32055_().m_41777_();
        duplicate.m_41764_(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).m_41619_() && !this.m_9236_().f_46443_) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0f);
        }
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
        Entity itemThrower = e.m_19749_();
        this.fishThrowerID = e.m_32055_().m_204117_(AMTagRegistry.COSMAW_TAMEABLES) && !this.m_21824_() && itemThrower != null ? itemThrower.m_20148_() : null;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this)).m_6662_() != HitResult.Type.MISS;
    }

    public boolean m_29443_() {
        return true;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1 && !this.m_20160_();
    }

    private boolean shouldWander() {
        if (this.m_20160_()) {
            return false;
        }
        if (this.m_21824_()) {
            int command = this.getCommand();
            if (command == 2 || this.isSitting()) {
                return false;
            }
            if (command == 1 && this.m_269323_() != null && this.m_20270_((Entity)this.m_269323_()) < 10.0f) {
                return true;
            }
            return command == 0;
        }
        return true;
    }

    public void m_7334_(Entity entity) {
        if (!(this.m_21824_() && entity instanceof LivingEntity && this.m_21830_((LivingEntity)entity))) {
            super.m_7334_(entity);
        }
    }

    public boolean canRiderInteract() {
        return true;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    private class AIAttack
    extends Goal {
        public AIAttack() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            return EntityCosmaw.this.m_5448_() != null && EntityCosmaw.this.m_5448_().m_6084_();
        }

        public void m_8037_() {
            double d = EntityCosmaw.this.m_20270_((Entity)EntityCosmaw.this.m_5448_());
            float f = EntityCosmaw.this.m_6162_() ? 0.5f : 1.0f;
            if (d < 3.0 * (double)f) {
                EntityCosmaw.this.m_7327_((Entity)EntityCosmaw.this.m_5448_());
            } else {
                EntityCosmaw.this.m_21573_().m_5624_((Entity)EntityCosmaw.this.m_5448_(), 1.0);
            }
        }
    }

    private class AIPickupOwner
    extends Goal {
        private LivingEntity owner;

        private AIPickupOwner() {
        }

        public boolean m_8036_() {
            if (EntityCosmaw.this.m_21824_() && EntityCosmaw.this.m_269323_() != null && !EntityCosmaw.this.isSitting() && !EntityCosmaw.this.m_269323_().m_20159_() && !EntityCosmaw.this.m_269323_().m_20096_() && EntityCosmaw.this.m_269323_().f_19789_ > 4.0f) {
                this.owner = EntityCosmaw.this.m_269323_();
                return true;
            }
            return false;
        }

        public void m_8037_() {
            if (this.owner != null && (!this.owner.m_21255_() || this.owner.m_20186_() < -30.0)) {
                double dist = EntityCosmaw.this.m_20270_((Entity)this.owner);
                if (dist < 3.0 || this.owner.m_20186_() <= -50.0) {
                    this.owner.f_19789_ = 0.0f;
                    this.owner.m_20329_((Entity)EntityCosmaw.this);
                } else if (dist > 100.0 || this.owner.m_20186_() <= -20.0) {
                    EntityCosmaw.this.m_6021_(this.owner.m_20185_(), this.owner.m_20186_() - 1.0, this.owner.m_20189_());
                } else {
                    EntityCosmaw.this.m_21573_().m_5624_((Entity)this.owner, 1.0 + Math.min(dist * (double)0.3f, 3.0));
                }
            }
        }
    }

    static class RandomFlyGoal
    extends Goal {
        private final EntityCosmaw parentEntity;
        private BlockPos target = null;

        public RandomFlyGoal(EntityCosmaw mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean m_8036_() {
            if (this.parentEntity.m_21573_().m_26571_() && this.parentEntity.shouldWander() && this.parentEntity.m_5448_() == null && this.parentEntity.m_217043_().m_188503_(4) == 0) {
                this.target = this.getBlockInViewCosmaw();
                if (this.target != null) {
                    this.parentEntity.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                    return true;
                }
            }
            return false;
        }

        public boolean m_8045_() {
            return this.target != null && this.parentEntity.shouldWander() && this.parentEntity.m_5448_() == null;
        }

        public void m_8041_() {
            this.target = null;
        }

        public void m_8037_() {
            if (this.target != null) {
                this.parentEntity.m_21566_().m_6849_((double)this.target.m_123341_() + 0.5, (double)this.target.m_123342_() + 0.5, (double)this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.m_82512_((Vec3i)this.target)) < 4.0 || this.parentEntity.f_19862_) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewCosmaw() {
            float radius = 5 + this.parentEntity.m_217043_().m_188503_(10);
            float neg = this.parentEntity.m_217043_().m_188499_() ? 1.0f : -1.0f;
            float renderYawOffset = this.parentEntity.m_146908_();
            float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f * (this.parentEntity.m_217043_().m_188501_() * neg);
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            BlockPos radialPos = AMBlockPos.fromCoords(this.parentEntity.m_20185_() + extraX, this.parentEntity.m_20186_(), this.parentEntity.m_20189_() + extraZ);
            BlockPos ground = this.parentEntity.getCosmawGround(radialPos);
            if (!this.parentEntity.isTargetBlocked(Vec3.m_82512_((Vec3i)(ground = ground.m_123342_() <= 1 ? ground.m_6630_(70 + this.parentEntity.f_19796_.m_188503_(4)) : ground.m_6630_(2 + this.parentEntity.f_19796_.m_188503_(2))).m_7494_()))) {
                return ground;
            }
            return null;
        }
    }
}

