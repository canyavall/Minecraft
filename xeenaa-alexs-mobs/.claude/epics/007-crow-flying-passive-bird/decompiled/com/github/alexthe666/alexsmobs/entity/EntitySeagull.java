/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
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
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.control.MoveControl$Operation
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.navigation.GroundPathNavigation
 *  net.minecraft.world.entity.ai.util.LandRandomPos
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Ingredient$TagValue
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.level.saveddata.maps.MapDecoration$Type
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.SeagullAIRevealTreasure;
import com.github.alexthe666.alexsmobs.entity.ai.SeagullAIStealFromPlayers;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntitySeagull
extends Animal
implements ITargetsDroppedItems {
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.m_135353_(EntitySeagull.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Float> FLIGHT_LOOK_YAW = SynchedEntityData.m_135353_(EntitySeagull.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.m_135353_(EntitySeagull.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.m_135353_(EntitySeagull.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Optional<BlockPos>> TREASURE_POS = SynchedEntityData.m_135353_(EntitySeagull.class, (EntityDataSerializer)EntityDataSerializers.f_135039_);
    public float prevFlyProgress;
    public float flyProgress;
    public float prevFlapAmount;
    public float flapAmount;
    public boolean aiItemFlag = false;
    public float attackProgress;
    public float prevAttackProgress;
    public float sitProgress;
    public float prevSitProgress;
    public int stealCooldown = this.f_19796_.m_188503_(2500);
    private boolean isLandNavigator;
    private int timeFlying;
    private BlockPos orbitPos = null;
    private double orbitDist = 5.0;
    private boolean orbitClockwise = false;
    private boolean fallFlag = false;
    private int flightLookCooldown = 0;
    private float targetFlightLookYaw;
    private int heldItemTime = 0;
    public int treasureSitTime;
    public UUID feederUUID = null;

    protected EntitySeagull(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0f);
        this.m_21441_(BlockPathTypes.WATER, -1.0f);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0f);
        this.m_21441_(BlockPathTypes.COCOA, -1.0f);
        this.m_21441_(BlockPathTypes.FENCE, -1.0f);
        this.switchNavigator(false);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.SEAGULL_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.SEAGULL_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.SEAGULL_HURT.get();
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Flying", this.isFlying());
        compound.m_128379_("Sitting", this.isSitting());
        compound.m_128405_("StealCooldown", this.stealCooldown);
        compound.m_128405_("TreasureSitTime", this.treasureSitTime);
        if (this.feederUUID != null) {
            compound.m_128362_("FeederUUID", this.feederUUID);
        }
        if (this.getTreasurePos() != null) {
            compound.m_128405_("TresX", this.getTreasurePos().m_123341_());
            compound.m_128405_("TresY", this.getTreasurePos().m_123342_());
            compound.m_128405_("TresZ", this.getTreasurePos().m_123343_());
        }
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFlying(compound.m_128471_("Flying"));
        this.setSitting(compound.m_128471_("Sitting"));
        this.stealCooldown = compound.m_128451_("StealCooldown");
        this.treasureSitTime = compound.m_128451_("TreasureSitTime");
        if (compound.m_128403_("FeederUUID")) {
            this.feederUUID = compound.m_128342_("FeederUUID");
        }
        if (compound.m_128441_("TresX") && compound.m_128441_("TresY") && compound.m_128441_("TresZ")) {
            this.setTreasurePos(new BlockPos(compound.m_128451_("TresX"), compound.m_128451_("TresY"), compound.m_128451_("TresZ")));
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 8.0).m_22268_(Attributes.f_22281_, 1.0).m_22268_(Attributes.f_22279_, (double)0.2f);
    }

    protected void m_8099_() {
        super.m_8099_();
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new SeagullAIRevealTreasure(this));
        this.f_21346_.m_25352_(2, (Goal)new SeagullAIStealFromPlayers(this));
        this.f_21345_.m_25352_(3, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_43938_(Stream.of(new Ingredient.TagValue(AMTagRegistry.SEAGULL_BREEDABLES), new Ingredient.TagValue(AMTagRegistry.SEAGULL_OFFERINGS))), false){

            public boolean m_8036_() {
                return !EntitySeagull.this.aiItemFlag && super.m_8036_();
            }
        });
        this.f_21345_.m_25352_(5, (Goal)new AIWanderIdle());
        this.f_21345_.m_25352_(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21345_.m_25352_(7, (Goal)new LookAtPlayerGoal((Mob)this, PathfinderMob.class, 6.0f));
        this.f_21345_.m_25352_(8, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21345_.m_25352_(9, (Goal)new AIScatter());
        this.f_21346_.m_25352_(1, (Goal)new AITargetItems((PathfinderMob)this, false, false, 15, 16));
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.SEAGULL_BREEDABLES);
    }

    public static boolean canSeagullSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8 && worldIn.m_6425_(pos.m_7495_()).m_76178_();
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.seagullSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = new GroundPathNavigation((Mob)this, this.m_9236_());
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
        this.f_19804_.m_135372_(SITTING, (Object)false);
        this.f_19804_.m_135372_(ATTACK_TICK, (Object)0);
        this.f_19804_.m_135372_(TREASURE_POS, Optional.empty());
        this.f_19804_.m_135372_(FLIGHT_LOOK_YAW, (Object)Float.valueOf(0.0f));
    }

    @Override
    public boolean isFlying() {
        return (Boolean)this.f_19804_.m_135370_(FLYING);
    }

    @Override
    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.m_135381_(FLYING, (Object)flying);
    }

    public boolean isSitting() {
        return (Boolean)this.f_19804_.m_135370_(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.f_19804_.m_135381_(SITTING, (Object)sitting);
    }

    public float getFlightLookYaw() {
        return ((Float)this.f_19804_.m_135370_(FLIGHT_LOOK_YAW)).floatValue();
    }

    public void setFlightLookYaw(float yaw) {
        this.f_19804_.m_135381_(FLIGHT_LOOK_YAW, (Object)Float.valueOf(yaw));
    }

    public BlockPos getTreasurePos() {
        return ((Optional)this.f_19804_.m_135370_(TREASURE_POS)).orElse(null);
    }

    public void setTreasurePos(BlockPos pos) {
        this.f_19804_.m_135381_(TREASURE_POS, Optional.ofNullable(pos));
    }

    public boolean m_6469_(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        }
        boolean prev = super.m_6469_(source, amount);
        if (prev) {
            this.setSitting(false);
            if (!this.m_21205_().m_41619_()) {
                this.m_19983_(this.m_21205_());
                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.f_41583_);
                this.stealCooldown = 1500 + this.f_19796_.m_188503_(1500);
            }
            this.feederUUID = null;
            this.treasureSitTime = 0;
        }
        return prev;
    }

    public void m_8119_() {
        super.m_8119_();
        this.prevFlyProgress = this.flyProgress;
        this.prevFlapAmount = this.flapAmount;
        this.prevAttackProgress = this.attackProgress;
        this.prevSitProgress = this.sitProgress;
        float yMot = (float)(-((double)((float)this.m_20184_().f_82480_) * 57.2957763671875));
        float absYaw = Math.abs(this.m_146908_() - this.f_19859_);
        boolean flying = this.isFlying();
        boolean sitting = this.isSitting();
        if (flying) {
            if (this.flyProgress < 5.0f) {
                this.flyProgress += 1.0f;
            }
        } else if (this.flyProgress > 0.0f) {
            this.flyProgress -= 1.0f;
        }
        if (sitting) {
            if (this.sitProgress < 5.0f) {
                this.sitProgress += 1.0f;
            }
        } else if (this.sitProgress > 0.0f) {
            this.sitProgress -= 1.0f;
        }
        this.flapAmount = absYaw > 8.0f ? Math.min(1.0f, this.flapAmount + 0.1f) : (yMot < 0.0f ? Math.min(-yMot * 0.2f, 1.0f) : (this.flapAmount > 0.0f ? (this.flapAmount -= Math.min(this.flapAmount, 0.05f)) : 0.0f));
        if ((Integer)this.f_19804_.m_135370_(ATTACK_TICK) > 0) {
            this.f_19804_.m_135381_(ATTACK_TICK, (Object)((Integer)this.f_19804_.m_135370_(ATTACK_TICK) - 1));
            if (this.attackProgress < 5.0f) {
                this.attackProgress += 1.0f;
            }
        } else if (this.attackProgress > 0.0f) {
            this.attackProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            if (this.isFlying()) {
                float lookYawDist = Math.abs(this.getFlightLookYaw() - this.targetFlightLookYaw);
                if (this.flightLookCooldown > 0) {
                    --this.flightLookCooldown;
                }
                if (this.flightLookCooldown == 0 && this.f_19796_.m_188503_(4) == 0 && lookYawDist < 0.5f) {
                    this.targetFlightLookYaw = Mth.m_14036_((float)(this.f_19796_.m_188501_() * 120.0f - 60.0f), (float)-60.0f, (float)60.0f);
                    this.flightLookCooldown = 3 + this.f_19796_.m_188503_(15);
                }
                if (this.getFlightLookYaw() < this.targetFlightLookYaw && lookYawDist > 0.5f) {
                    this.setFlightLookYaw(this.getFlightLookYaw() + Math.min(lookYawDist, 4.0f));
                }
                if (this.getFlightLookYaw() > this.targetFlightLookYaw && lookYawDist > 0.5f) {
                    this.setFlightLookYaw(this.getFlightLookYaw() - Math.min(lookYawDist, 4.0f));
                }
                if (this.m_20096_() && !this.m_20072_() && this.timeFlying > 30) {
                    this.setFlying(false);
                }
                ++this.timeFlying;
                this.m_20242_(true);
                if (this.m_20159_() || this.m_27593_()) {
                    this.setFlying(false);
                }
            } else {
                this.fallFlag = false;
                this.timeFlying = 0;
                this.m_20242_(false);
            }
            if (this.isFlying() && this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (!this.isFlying() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (!this.m_21205_().m_41619_()) {
            ++this.heldItemTime;
            if (this.heldItemTime > 200 && this.canTargetItem(this.m_21205_())) {
                this.heldItemTime = 0;
                this.m_5634_(4.0f);
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_11912_, this.m_6121_(), this.m_6100_());
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                this.eatItemEffect(this.m_21205_());
                this.m_21205_().m_41774_(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (this.stealCooldown > 0) {
            --this.stealCooldown;
        }
        if (this.treasureSitTime > 0) {
            --this.treasureSitTime;
        }
        if (this.isSitting() && this.m_20072_()) {
            this.m_20256_(this.m_20184_().m_82520_(0.0, (double)0.02f, 0.0));
        }
    }

    public void eatItem() {
        this.heldItemTime = 200;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.m_41720_().m_41472_() && !this.isSitting();
    }

    private void eatItemEffect(ItemStack heldItemMainhand) {
        for (int i = 0; i < 2 + this.f_19796_.m_188503_(2); ++i) {
            double d2 = this.f_19796_.m_188583_() * 0.02;
            double d0 = this.f_19796_.m_188583_() * 0.02;
            double d1 = this.f_19796_.m_188583_() * 0.02;
            float radius = this.m_20205_() * 0.65f;
            float angle = (float)Math.PI / 180 * this.f_20883_;
            double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
            double extraZ = radius * Mth.m_14089_((float)angle);
            ItemParticleOption data = new ItemParticleOption(ParticleTypes.f_123752_, heldItemMainhand);
            if (heldItemMainhand.m_41720_() instanceof BlockItem) {
                data = new BlockParticleOption(ParticleTypes.f_123794_, ((BlockItem)heldItemMainhand.m_41720_()).m_40614_().m_49966_());
            }
            this.m_9236_().m_7106_((ParticleOptions)data, this.m_20185_() + extraX, this.m_20186_() + (double)(this.m_20206_() * 0.6f), this.m_20189_() + extraZ, d0, d1, d2);
        }
    }

    public void setDataFromTreasureMap(Player player) {
        boolean flag = false;
        for (ItemStack map : player.m_6167_()) {
            if (map.m_41720_() != Items.f_42573_ && map.m_41720_() != Items.f_42676_ || !map.m_41782_() || !map.m_41783_().m_128425_("Decorations", 9)) continue;
            ListTag listnbt = map.m_41783_().m_128437_("Decorations", 10);
            for (int i = 0; i < listnbt.size(); ++i) {
                CompoundTag nbt = listnbt.m_128728_(i);
                byte type = nbt.m_128445_("type");
                if (type != MapDecoration.Type.RED_X.m_77853_() && type != MapDecoration.Type.TARGET_X.m_77853_()) continue;
                int x = nbt.m_128451_("x");
                int z = nbt.m_128451_("z");
                if (!(this.m_20275_(x, this.m_20186_(), z) <= 400.0)) continue;
                flag = true;
                this.setTreasurePos(new BlockPos(x, 0, z));
            }
        }
        if (flag) {
            this.feederUUID = player.m_20148_();
            this.treasureSitTime = 300;
            this.stealCooldown = 1500 + this.f_19796_.m_188503_(1500);
        }
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

    public boolean isWingull() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && s.equalsIgnoreCase("wingull");
    }

    @Override
    public void onGetItem(ItemEntity e) {
        Player player;
        ItemStack duplicate = e.m_32055_().m_41777_();
        duplicate.m_41764_(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).m_41619_() && !this.m_9236_().f_46443_) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0f);
        }
        this.stealCooldown += 600 + this.f_19796_.m_188503_(1200);
        Entity thrower = e.m_19749_();
        if (thrower != null && e.m_32055_().m_204117_(AMTagRegistry.SEAGULL_OFFERINGS) && (player = this.m_9236_().m_46003_(thrower.m_20148_())) != null) {
            this.setDataFromTreasureMap(player);
            this.feederUUID = thrower.m_20148_();
        }
        this.setFlying(true);
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 5.0f + radiusAdd + (float)this.m_217043_().m_188503_(5);
        float neg = this.m_217043_().m_188499_() ? 1.0f : -1.0f;
        float renderYawOffset = this.f_20883_;
        float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.m_217043_().m_188501_() * neg;
        double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
        double extraZ = radius * Mth.m_14089_((float)angle);
        BlockPos radialPos = new BlockPos((int)(fleePos.m_7096_() + extraX), 0, (int)(fleePos.m_7094_() + extraZ));
        BlockPos ground = this.getSeagullGround(radialPos);
        int distFromGround = (int)this.m_20186_() - ground.m_123342_();
        int flightHeight = 8 + this.m_217043_().m_188503_(4);
        BlockPos newPos = ground.m_6630_(distFromGround > 3 ? flightHeight : this.m_217043_().m_188503_(4) + 8);
        if (!this.isTargetBlocked(Vec3.m_82512_((Vec3i)newPos)) && this.m_20238_(Vec3.m_82512_((Vec3i)newPos)) > 1.0) {
            return Vec3.m_82512_((Vec3i)newPos);
        }
        return null;
    }

    public BlockPos getSeagullGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int)this.m_20186_(), in.m_123343_());
        while (position.m_123342_() < 320 && !this.m_9236_().m_6425_(position).m_76178_()) {
            position = position.m_7494_();
        }
        while (position.m_123342_() > -64 && !this.m_9236_().m_8055_(position).m_280296_() && this.m_9236_().m_6425_(position).m_76178_()) {
            position = position.m_7495_();
        }
        return position;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = 10 + this.m_217043_().m_188503_(15);
        float neg = this.m_217043_().m_188499_() ? 1.0f : -1.0f;
        float renderYawOffset = this.f_20883_;
        float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.m_217043_().m_188501_() * neg;
        double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
        double extraZ = radius * Mth.m_14089_((float)angle);
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.m_7096_() + extraX, this.m_20186_(), fleePos.m_7094_() + extraZ);
        BlockPos ground = this.getSeagullGround(radialPos);
        if (ground.m_123342_() == 0) {
            return this.m_20182_();
        }
        ground = this.m_20183_();
        while (ground.m_123342_() > -62 && !this.m_9236_().m_8055_(ground).m_280296_()) {
            ground = ground.m_7495_();
        }
        if (!this.isTargetBlocked(Vec3.m_82512_((Vec3i)ground.m_7494_()))) {
            return Vec3.m_82512_((Vec3i)ground);
        }
        return null;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this)).m_6662_() != HitResult.Type.MISS;
    }

    private Vec3 getOrbitVec(Vec3 vector3d, float gatheringCircleDist) {
        float angle = (float)Math.PI / 180 * (float)this.orbitDist * (float)(this.orbitClockwise ? -this.f_19797_ : this.f_19797_);
        double extraX = gatheringCircleDist * Mth.m_14031_((float)angle);
        double extraZ = gatheringCircleDist * Mth.m_14089_((float)angle);
        if (this.orbitPos != null) {
            Vec3 pos = new Vec3((double)this.orbitPos.m_123341_() + extraX, (double)(this.orbitPos.m_123342_() + this.f_19796_.m_188503_(2)), (double)this.orbitPos.m_123343_() + extraZ);
            if (this.m_9236_().m_46859_(AMBlockPos.fromVec3(pos))) {
                return pos;
            }
        }
        return null;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -64 && this.m_9236_().m_46859_(position)) {
            position = position.m_7495_();
        }
        return !this.m_9236_().m_6425_(position).m_76178_() || position.m_123342_() <= -64;
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.m_41720_();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21205_().m_41619_() && type != InteractionResult.SUCCESS) {
            this.m_19983_(this.m_21205_().m_41777_());
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.f_41583_);
            this.stealCooldown = 1500 + this.f_19796_.m_188503_(1500);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return (AgeableMob)((EntityType)AMEntityRegistry.SEAGULL.get()).m_20615_((Level)serverWorld);
    }

    @Override
    public void peck() {
        this.f_19804_.m_135381_(ATTACK_TICK, (Object)7);
    }

    private class AIWanderIdle
    extends Goal {
        protected final EntitySeagull eagle;
        protected double x;
        protected double y;
        protected double z;
        private boolean flightTarget = false;
        private int orbitResetCooldown = 0;
        private int maxOrbitTime = 360;
        private int orbitTime = 0;

        public AIWanderIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.eagle = EntitySeagull.this;
        }

        public boolean m_8036_() {
            if (this.orbitResetCooldown < 0) {
                ++this.orbitResetCooldown;
            }
            if (this.eagle.m_5448_() != null && this.eagle.m_5448_().m_6084_() && !this.eagle.m_20160_() || this.eagle.isSitting() || this.eagle.m_20159_()) {
                return false;
            }
            if (this.eagle.m_217043_().m_188503_(20) != 0 && !this.eagle.isFlying() || this.eagle.aiItemFlag) {
                return false;
            }
            if (this.eagle.m_6162_()) {
                this.flightTarget = false;
            } else if (this.eagle.m_20072_()) {
                this.flightTarget = true;
            } else if (this.eagle.m_20096_()) {
                this.flightTarget = EntitySeagull.this.f_19796_.m_188503_(10) == 0;
            } else {
                if (this.orbitResetCooldown == 0 && EntitySeagull.this.f_19796_.m_188503_(6) == 0) {
                    this.orbitResetCooldown = 100 + EntitySeagull.this.f_19796_.m_188503_(300);
                    this.eagle.orbitPos = this.eagle.m_20183_();
                    this.eagle.orbitDist = 4 + EntitySeagull.this.f_19796_.m_188503_(5);
                    this.eagle.orbitClockwise = EntitySeagull.this.f_19796_.m_188499_();
                    this.orbitTime = 0;
                    this.maxOrbitTime = (int)(180.0f + 360.0f * EntitySeagull.this.f_19796_.m_188501_());
                }
                this.flightTarget = EntitySeagull.this.f_19796_.m_188503_(5) != 0 && this.eagle.timeFlying < 400;
            }
            Vec3 lvt_1_1_ = this.getPosition();
            if (lvt_1_1_ == null) {
                return false;
            }
            this.x = lvt_1_1_.f_82479_;
            this.y = lvt_1_1_.f_82480_;
            this.z = lvt_1_1_.f_82481_;
            return true;
        }

        public void m_8037_() {
            if (this.orbitResetCooldown > 0) {
                --this.orbitResetCooldown;
            }
            if (this.orbitResetCooldown < 0) {
                ++this.orbitResetCooldown;
            }
            if (this.orbitResetCooldown > 0 && this.eagle.orbitPos != null) {
                if (this.orbitTime < this.maxOrbitTime && !this.eagle.m_20072_()) {
                    ++this.orbitTime;
                } else {
                    this.orbitTime = 0;
                    this.eagle.orbitPos = null;
                    this.orbitResetCooldown = -400 - EntitySeagull.this.f_19796_.m_188503_(400);
                }
            }
            if (this.eagle.f_19862_ && !this.eagle.m_20096_()) {
                this.m_8041_();
            }
            if (this.flightTarget) {
                this.eagle.m_21566_().m_6849_(this.x, this.y, this.z, 1.0);
            } else if (!this.eagle.isFlying() || this.eagle.m_20096_()) {
                this.eagle.m_21573_().m_26519_(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntitySeagull.this.isFlying()) {
                this.eagle.fallFlag = true;
                if (this.eagle.m_20096_()) {
                    this.eagle.setFlying(false);
                    this.orbitTime = 0;
                    this.eagle.orbitPos = null;
                    this.orbitResetCooldown = -400 - EntitySeagull.this.f_19796_.m_188503_(400);
                }
            }
            if (EntitySeagull.this.isFlying() && (!EntitySeagull.this.m_9236_().m_46859_(this.eagle.m_20099_()) || this.eagle.m_20096_()) && !this.eagle.m_20072_() && this.eagle.timeFlying > 30) {
                this.eagle.setFlying(false);
                this.orbitTime = 0;
                this.eagle.orbitPos = null;
                this.orbitResetCooldown = -400 - EntitySeagull.this.f_19796_.m_188503_(400);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.eagle.m_20182_();
            if (this.orbitResetCooldown > 0 && this.eagle.orbitPos != null) {
                return this.eagle.getOrbitVec(vector3d, 4 + EntitySeagull.this.f_19796_.m_188503_(4));
            }
            if (this.eagle.m_20160_() || this.eagle.isOverWaterOrVoid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                if (this.eagle.timeFlying < 340 || this.eagle.m_20160_() || this.eagle.isOverWaterOrVoid()) {
                    return this.eagle.getBlockInViewAway(vector3d, 0.0f);
                }
                return this.eagle.getBlockGrounding(vector3d);
            }
            return LandRandomPos.m_148488_((PathfinderMob)this.eagle, (int)10, (int)7);
        }

        public boolean m_8045_() {
            if (this.flightTarget) {
                return this.eagle.isFlying() && this.eagle.m_20275_(this.x, this.y, this.z) > 5.0;
            }
            return !this.eagle.m_21573_().m_26571_() && !this.eagle.m_20160_();
        }

        public void m_8056_() {
            if (this.flightTarget) {
                this.eagle.setFlying(true);
                this.eagle.m_21566_().m_6849_(this.x, this.y, this.z, 1.0);
            } else {
                this.eagle.m_21573_().m_26519_(this.x, this.y, this.z, 1.0);
            }
        }

        public void m_8041_() {
            this.eagle.m_21573_().m_26573_();
            super.m_8041_();
        }
    }

    private class AIScatter
    extends Goal {
        protected final Sorter theNearestAttackableTargetSorter;
        protected final Predicate<? super Entity> targetEntitySelector;
        protected int executionChance = 8;
        protected boolean mustUpdate;
        private Entity targetEntity;
        private Vec3 flightTarget = null;
        private int cooldown = 0;

        AIScatter() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new Sorter((Entity)EntitySeagull.this);
            this.targetEntitySelector = new Predicate<Entity>(){

                public boolean apply(@Nullable Entity e) {
                    return e.m_6084_() && e.m_6095_().m_204039_(AMTagRegistry.SCATTERS_CROWS) || e instanceof Player && !((Player)e).m_7500_();
                }
            };
        }

        public boolean m_8036_() {
            List list;
            if (EntitySeagull.this.m_20159_() || EntitySeagull.this.isSitting() || EntitySeagull.this.aiItemFlag || EntitySeagull.this.m_20160_()) {
                return false;
            }
            if (!this.mustUpdate) {
                long worldTime = EntitySeagull.this.m_9236_().m_46467_() % 10L;
                if (EntitySeagull.this.m_21216_() >= 100 && worldTime != 0L) {
                    return false;
                }
                if (EntitySeagull.this.m_217043_().m_188503_(this.executionChance) != 0 && worldTime != 0L) {
                    return false;
                }
            }
            if ((list = EntitySeagull.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector)).isEmpty()) {
                return false;
            }
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.targetEntity = (Entity)list.get(0);
            this.mustUpdate = false;
            return true;
        }

        public boolean m_8045_() {
            return this.targetEntity != null;
        }

        public void m_8041_() {
            this.flightTarget = null;
            this.targetEntity = null;
        }

        public void m_8037_() {
            if (this.cooldown > 0) {
                --this.cooldown;
            }
            if (this.flightTarget != null) {
                EntitySeagull.this.setFlying(true);
                EntitySeagull.this.m_21566_().m_6849_(this.flightTarget.f_82479_, this.flightTarget.f_82480_, this.flightTarget.f_82481_, 1.0);
                if (this.cooldown == 0 && EntitySeagull.this.isTargetBlocked(this.flightTarget)) {
                    this.cooldown = 30;
                    this.flightTarget = null;
                }
            }
            if (this.targetEntity != null) {
                Vec3 vec;
                if ((EntitySeagull.this.m_20096_() || this.flightTarget == null || this.flightTarget != null && EntitySeagull.this.m_20238_(this.flightTarget) < 3.0) && (vec = EntitySeagull.this.getBlockInViewAway(this.targetEntity.m_20182_(), 0.0f)) != null && vec.m_7098_() > EntitySeagull.this.m_20186_()) {
                    this.flightTarget = vec;
                }
                if (EntitySeagull.this.m_20270_(this.targetEntity) > 20.0f) {
                    this.m_8041_();
                }
            }
        }

        protected double getTargetDistance() {
            return 4.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntitySeagull.this.m_20185_(), EntitySeagull.this.m_20186_() + 0.5, EntitySeagull.this.m_20189_());
            AABB aabb = new AABB(-2.0, -2.0, -2.0, 2.0, 2.0, 2.0);
            return aabb.m_82383_(renderCenter);
        }

        public record Sorter(Entity theEntity) implements Comparator<Entity>
        {
            @Override
            public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                double d0 = this.theEntity.m_20280_(p_compare_1_);
                double d1 = this.theEntity.m_20280_(p_compare_2_);
                return Double.compare(d0, d1);
            }
        }
    }

    private static class AITargetItems
    extends CreatureAITargetItems {
        public AITargetItems(PathfinderMob creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
            super(creature, checkSight, onlyNearby, tickThreshold, radius);
            this.executionChance = 1;
        }

        @Override
        public void m_8041_() {
            super.m_8041_();
            ((EntitySeagull)this.f_26135_).aiItemFlag = false;
        }

        @Override
        public boolean m_8036_() {
            return super.m_8036_() && !((EntitySeagull)this.f_26135_).isSitting() && (this.f_26135_.m_5448_() == null || !this.f_26135_.m_5448_().m_6084_());
        }

        @Override
        public boolean m_8045_() {
            return super.m_8045_() && !((EntitySeagull)this.f_26135_).isSitting() && (this.f_26135_.m_5448_() == null || !this.f_26135_.m_5448_().m_6084_());
        }

        @Override
        protected void moveTo() {
            EntitySeagull crow = (EntitySeagull)this.f_26135_;
            if (this.targetEntity != null) {
                crow.aiItemFlag = true;
                if (this.f_26135_.m_20270_((Entity)this.targetEntity) < 2.0f) {
                    crow.m_21566_().m_6849_(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                    crow.peck();
                }
                if (this.f_26135_.m_20270_((Entity)this.targetEntity) > 8.0f || crow.isFlying()) {
                    crow.setFlying(true);
                    float f = (float)(crow.m_20185_() - this.targetEntity.m_20185_());
                    float f2 = (float)(crow.m_20189_() - this.targetEntity.m_20189_());
                    if (!crow.m_142582_((Entity)this.targetEntity)) {
                        crow.m_21566_().m_6849_(this.targetEntity.m_20185_(), 1.0 + crow.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                    } else {
                        float f1 = 1.8f;
                        float xzDist = Mth.m_14116_((float)(f * f + f2 * f2));
                        if (xzDist < 5.0f) {
                            f1 = 0.0f;
                        }
                        crow.m_21566_().m_6849_(this.targetEntity.m_20185_(), (double)f1 + this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                    }
                } else {
                    this.f_26135_.m_21573_().m_26519_(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                }
            }
        }

        @Override
        public void m_8037_() {
            super.m_8037_();
            this.moveTo();
        }
    }

    static class MoveHelper
    extends MoveControl {
        private final EntitySeagull parentEntity;

        public MoveHelper(EntitySeagull bird) {
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
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().m_82549_(vector3d.m_82490_(this.f_24978_ * 0.03 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float)Mth.m_14136_((double)vector3d1.f_82479_, (double)vector3d1.f_82481_)) * 57.295776f);
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }
    }
}

