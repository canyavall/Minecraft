/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.PanicGoal
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
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.pathfinder.BlockPathTypes
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.registries.ForgeRegistries
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAITargetDroppedItems;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityToucan
extends Animal
implements ITargetsDroppedItems {
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> PECK_TICK = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Integer> GOLDEN_TIME = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final EntityDataAccessor<Boolean> ENCHANTED = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Optional<BlockState>> SAPLING_STATE = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_268618_);
    private static final EntityDataAccessor<Integer> SAPLING_TIME = SynchedEntityData.m_135353_(EntityToucan.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final HashMap<String, String> FEEDING_DATA = new HashMap();
    private static final List<ItemStack> FEEDING_STACKS = new ArrayList<ItemStack>();
    private static boolean initFeedingData = false;
    public float prevFlyProgress;
    public float flyProgress;
    public float prevPeckProgress;
    public float peckProgress;
    private boolean isLandNavigator;
    private int timeFlying;
    private int heldItemTime;
    private boolean aiItemFlag;

    protected EntityToucan(EntityType type, Level worldIn) {
        super(type, worldIn);
        EntityToucan.initFeedingData();
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0f);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, -1.0f);
        this.m_21441_(BlockPathTypes.COCOA, -1.0f);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0f);
        this.switchNavigator(true);
    }

    public static boolean canToucanSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return true;
    }

    private static void initFeedingData() {
        if (!initFeedingData || FEEDING_DATA.isEmpty()) {
            initFeedingData = true;
            for (String string : AMConfig.toucanFruitMatches) {
                String[] split = string.split("\\|");
                if (split.length < 2) continue;
                FEEDING_DATA.put(split[0], split[1]);
                FEEDING_STACKS.add(new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation(split[0]))));
            }
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 6.0).m_22268_(Attributes.f_22279_, (double)0.2f);
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.TOUCAN_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.TOUCAN_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.TOUCAN_HURT.get();
    }

    public boolean m_6914_(LevelReader p_29005_) {
        if (p_29005_.m_45784_((Entity)this) && !p_29005_.m_46855_(this.m_20191_())) {
            BlockPos blockpos = this.m_20183_();
            if (blockpos.m_123342_() < p_29005_.m_5736_()) {
                return false;
            }
            BlockState blockstate2 = p_29005_.m_8055_(blockpos.m_7495_());
            return blockstate2.m_60713_(Blocks.f_50440_) || blockstate2.m_204336_(BlockTags.f_13035_);
        }
        return false;
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.toucanSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    private BlockState getSaplingFor(ItemStack stack) {
        String str;
        Block block;
        ResourceLocation name = ForgeRegistries.ITEMS.getKey((Object)stack.m_41720_());
        if (!stack.m_41619_() && name != null && FEEDING_DATA.containsKey(name.toString()) && (block = (Block)ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str = FEEDING_DATA.get(name.toString())))) != null) {
            return block.m_49966_();
        }
        return null;
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (this.getSaplingFor(itemstack) != null && this.getSaplingTime() <= 0 && this.m_21205_().m_41619_()) {
            this.peck();
            ItemStack duplicate = itemstack.m_41777_();
            duplicate.m_41764_(1);
            this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    protected void m_8099_() {
        super.m_8099_();
        EntityToucan.initFeedingData();
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new PanicGoal((PathfinderMob)this, 1.3));
        this.f_21345_.m_25352_(2, (Goal)new AIPlantTrees());
        this.f_21345_.m_25352_(3, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_43921_(FEEDING_STACKS.stream()), false){

            public boolean m_8036_() {
                return !EntityToucan.this.aiItemFlag && super.m_8036_();
            }
        });
        this.f_21345_.m_25352_(5, (Goal)new AIWanderIdle());
        this.f_21345_.m_25352_(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21345_.m_25352_(8, (Goal)new LookAtPlayerGoal((Mob)this, PathfinderMob.class, 6.0f));
        this.f_21345_.m_25352_(9, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, (Goal)new FlyingAITargetDroppedItems((PathfinderMob)this, false, false, 15, 16));
    }

    @Override
    public void setItemFlag(boolean itemAIFlag) {
        this.aiItemFlag = itemAIFlag;
    }

    public boolean m_6898_(ItemStack stack) {
        Item item = stack.m_41720_();
        return stack.m_204117_(AMTagRegistry.TOUCAN_BREEDABLES);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(SAPLING_STATE, Optional.empty());
        this.f_19804_.m_135372_(FLYING, (Object)false);
        this.f_19804_.m_135372_(PECK_TICK, (Object)0);
        this.f_19804_.m_135372_(VARIANT, (Object)0);
        this.f_19804_.m_135372_(GOLDEN_TIME, (Object)0);
        this.f_19804_.m_135372_(SAPLING_TIME, (Object)0);
        this.f_19804_.m_135372_(ENCHANTED, (Object)false);
    }

    public boolean m_6147_() {
        return false;
    }

    public void m_8119_() {
        boolean flying;
        super.m_8119_();
        this.prevFlyProgress = this.flyProgress;
        this.prevPeckProgress = this.peckProgress;
        if (this.getGoldenTime() > 0 && !this.m_9236_().f_46443_) {
            this.setGoldenTime(this.getGoldenTime() - 1);
        }
        if (flying = this.isFlying()) {
            if (this.flyProgress < 5.0f) {
                this.flyProgress += 1.0f;
            }
        } else if (this.flyProgress > 0.0f) {
            this.flyProgress -= 1.0f;
        }
        if (!this.m_9236_().f_46443_) {
            if (flying) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (flying) {
                this.m_20242_(true);
                if (this.isFlying() && !this.m_20096_() && !this.m_20072_()) {
                    this.m_20256_(this.m_20184_().m_82542_(1.0, (double)0.6f, 1.0));
                }
                ++this.timeFlying;
            } else {
                this.m_20242_(false);
                this.timeFlying = 0;
            }
        }
        if ((Integer)this.f_19804_.m_135370_(PECK_TICK) > 0) {
            this.f_19804_.m_135381_(PECK_TICK, (Object)((Integer)this.f_19804_.m_135370_(PECK_TICK) - 1));
            if (this.peckProgress < 5.0f) {
                this.peckProgress += 1.0f;
            }
        } else if (this.peckProgress > 0.0f) {
            this.peckProgress -= 1.0f;
        }
        if (this.peckProgress >= 5.0f && this.m_21205_().m_41619_() && this.getSaplingState() != null) {
            this.peckBlockEffect();
        }
        if (!this.m_21205_().m_41619_()) {
            ++this.heldItemTime;
            if (this.heldItemTime > 10 && this.canTargetItem(this.m_21205_())) {
                ItemStack mainHandItem;
                this.heldItemTime = 0;
                this.m_5634_(4.0f);
                this.m_146850_(GameEvent.f_157806_);
                this.m_5496_(SoundEvents.f_11912_, this.m_6121_(), this.m_6100_());
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                if ((mainHandItem = this.m_21205_()).m_204117_(AMTagRegistry.TOUCAN_GOLDEN_FOODS)) {
                    this.setGoldenTime(12000);
                } else if (mainHandItem.m_204117_(AMTagRegistry.TOUCAN_ENCHANTED_GOLDEN_FOODS)) {
                    this.setGoldenTime(-1);
                    this.setEnchanted(true);
                }
                this.setSaplingState(this.getSaplingFor(this.m_21205_()));
                this.eatItemEffect(this.m_21205_());
                this.m_21205_().m_41774_(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (this.isFlying() && this.m_146900_().m_60713_(Blocks.f_50191_)) {
            float f = this.m_146908_() * ((float)Math.PI / 180);
            this.m_20256_(this.m_20184_().m_82520_((double)(-Mth.m_14031_((float)f) * 0.2f), (double)0.4f, (double)(Mth.m_14089_((float)f) * 0.2f)));
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    protected void m_7840_(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFlying() {
        return (Boolean)this.f_19804_.m_135370_(FLYING);
    }

    @Override
    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            return;
        }
        this.f_19804_.m_135381_(FLYING, (Object)flying);
    }

    @Override
    public void peck() {
        if (this.peckProgress == 0.0f) {
            this.f_19804_.m_135381_(PECK_TICK, (Object)7);
        }
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 7.0f + radiusAdd + (float)this.m_217043_().m_188503_(8);
        float neg = this.m_217043_().m_188499_() ? 1.0f : -1.0f;
        float renderYawOffset = this.f_20883_;
        float angle = (float)Math.PI / 180 * renderYawOffset + 3.15f + this.m_217043_().m_188501_() * neg;
        double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
        double extraZ = radius * Mth.m_14089_((float)angle);
        BlockPos radialPos = new BlockPos((int)(fleePos.m_7096_() + extraX), 0, (int)(fleePos.m_7094_() + extraZ));
        BlockPos ground = this.getToucanGround(radialPos);
        int distFromGround = (int)this.m_20186_() - ground.m_123342_();
        int flightHeight = 8 + this.m_217043_().m_188503_(4);
        int j = this.m_217043_().m_188503_(6) + 18;
        BlockPos newPos = ground.m_6630_(distFromGround > 9 ? flightHeight : j);
        if (this.m_9236_().m_8055_(ground).m_204336_(BlockTags.f_13035_)) {
            newPos = ground.m_6630_(1 + this.m_217043_().m_188503_(3));
        }
        if (!this.isTargetBlocked(Vec3.m_82512_((Vec3i)newPos)) && this.m_20238_(Vec3.m_82512_((Vec3i)newPos)) > 1.0) {
            return Vec3.m_82512_((Vec3i)newPos);
        }
        return null;
    }

    public BlockPos getToucanGround(BlockPos in) {
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
        BlockPos ground = this.getToucanGround(radialPos);
        if (ground.m_123342_() == -64) {
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

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl((Mob)this);
            this.f_21344_ = new GroundPathNavigation((Mob)this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController((Mob)this, 0.6f, false, true);
            this.f_21344_ = new DirectPathNavigator((Mob)this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        BlockState blockstate = this.getSaplingState();
        if (blockstate != null) {
            compound.m_128365_("SaplingState", (Tag)NbtUtils.m_129202_((BlockState)blockstate));
        }
        compound.m_128405_("Variant", this.getVariant());
        compound.m_128405_("GoldenTime", this.getGoldenTime());
        compound.m_128379_("Enchanted", this.isEnchanted());
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        BlockState blockstate = null;
        if (compound.m_128425_("SaplingState", 10) && (blockstate = NbtUtils.m_247651_((HolderGetter)this.m_9236_().m_246945_(Registries.f_256747_), (CompoundTag)compound.m_128469_("SaplingState"))).m_60795_()) {
            blockstate = null;
        }
        this.setSaplingState(blockstate);
        this.setVariant(compound.m_128451_("Variant"));
        this.setGoldenTime(compound.m_128451_("GoldenTime"));
        this.setEnchanted(compound.m_128471_("Enchanted"));
    }

    public boolean isSam() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("sam");
    }

    public int getVariant() {
        return (Integer)this.f_19804_.m_135370_(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.m_135381_(VARIANT, (Object)variant);
    }

    public int getSaplingTime() {
        return (Integer)this.f_19804_.m_135370_(SAPLING_TIME);
    }

    public void setSaplingTime(int time) {
        this.f_19804_.m_135381_(SAPLING_TIME, (Object)time);
    }

    public boolean isGolden() {
        return this.getGoldenTime() > 0 || this.getGoldenTime() == -1 || this.isEnchanted();
    }

    public int getGoldenTime() {
        return (Integer)this.f_19804_.m_135370_(GOLDEN_TIME);
    }

    public void setGoldenTime(int goldenTime) {
        this.f_19804_.m_135381_(GOLDEN_TIME, (Object)goldenTime);
    }

    public boolean isEnchanted() {
        return (Boolean)this.f_19804_.m_135370_(ENCHANTED);
    }

    public void setEnchanted(boolean enchanted) {
        this.f_19804_.m_135381_(ENCHANTED, (Object)enchanted);
    }

    @Nullable
    public BlockState getSaplingState() {
        return ((Optional)this.f_19804_.m_135370_(SAPLING_STATE)).orElse(null);
    }

    public void setSaplingState(@Nullable BlockState state) {
        this.f_19804_.m_135381_(SAPLING_STATE, Optional.ofNullable(state));
    }

    @Nullable
    public SpawnGroupData m_6518_(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.m_217043_().m_188503_(4));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel level, AgeableMob parent) {
        EntityToucan toucan = (EntityToucan)((EntityType)AMEntityRegistry.TOUCAN.get()).m_20615_(this.m_9236_());
        toucan.setVariant(this.getVariant());
        return toucan;
    }

    public float m_5610_(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_8055_(pos).m_204336_(BlockTags.f_13035_) ? 10.0f : super.m_5610_(pos, worldIn);
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -62 && this.m_9236_().m_46859_(position)) {
            position = position.m_7495_();
        }
        return !this.m_9236_().m_6425_(position).m_76178_() || this.m_9236_().m_8055_(position).m_60713_(Blocks.f_50191_) || position.m_123342_() <= 0;
    }

    private boolean isOverLeaves() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -62 && this.m_9236_().m_46859_(position)) {
            position = position.m_7495_();
        }
        return this.m_9236_().m_8055_(position).m_204336_(BlockTags.f_13035_) || this.m_9236_().m_8055_(position).m_60713_(Blocks.f_50191_);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return this.getSaplingTime() <= 0 && this.getSaplingFor(stack) != null;
    }

    private void peckBlockEffect() {
        BlockState beneath = this.m_20075_();
        if (this.m_9236_().f_46443_ && !beneath.m_60795_() && beneath.m_60819_().m_76178_()) {
            for (int i = 0; i < 2 + this.f_19796_.m_188503_(2); ++i) {
                double d2 = this.f_19796_.m_188583_() * 0.02;
                double d0 = this.f_19796_.m_188583_() * 0.02;
                double d1 = this.f_19796_.m_188583_() * 0.02;
                float radius = this.m_20205_() * 0.65f;
                float angle = (float)Math.PI / 180 * this.f_20883_;
                double extraX = radius * Mth.m_14031_((float)((float)Math.PI + angle));
                double extraZ = radius * Mth.m_14089_((float)angle);
                BlockParticleOption data = new BlockParticleOption(ParticleTypes.f_123794_, beneath);
                this.m_9236_().m_7106_((ParticleOptions)data, this.m_20185_() + extraX, this.m_20186_() + (double)0.1f, this.m_20189_() + extraZ, d0, d1, d2);
            }
        }
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

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.m_32055_().m_41777_();
        duplicate.m_41764_(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).m_41619_() && !this.m_9236_().f_46443_) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0f);
        }
        this.peck();
        this.setFlying(true);
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
    }

    private boolean hasLineOfSightSapling(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.m_82512_((Vec3i)destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
        return result.m_82425_().equals((Object)destinationBlock);
    }

    private class AIPlantTrees
    extends Goal {
        protected final EntityToucan toucan;
        protected BlockPos pos;
        private int runCooldown = 0;
        private int encircleTime = 0;
        private int plantTime = 0;
        private boolean clockwise;

        public AIPlantTrees() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.toucan = EntityToucan.this;
        }

        public boolean m_8036_() {
            if (this.toucan.getSaplingState() != null && this.runCooldown-- <= 0) {
                BlockPos target = this.getSaplingPlantPos();
                this.runCooldown = this.resetCooldown();
                if (target != null) {
                    this.pos = target;
                    this.clockwise = EntityToucan.this.f_19796_.m_188499_();
                    this.encircleTime = (this.toucan.isGolden() ? 20 : 100) + EntityToucan.this.f_19796_.m_188503_(100);
                    return true;
                }
            }
            return false;
        }

        private int resetCooldown() {
            return this.toucan.isGolden() && !this.toucan.isEnchanted() ? 50 + EntityToucan.this.f_19796_.m_188503_(40) : 200 + EntityToucan.this.f_19796_.m_188503_(200);
        }

        public void m_8037_() {
            this.toucan.aiItemFlag = true;
            double up = 3.0;
            if (this.encircleTime > 0) {
                --this.encircleTime;
            }
            if (this.isWithinXZDist(this.pos, this.toucan.m_20182_(), 5.0) && this.encircleTime <= 0) {
                up = 0.0;
            }
            if (this.toucan.m_20238_(Vec3.m_82512_((Vec3i)this.pos)) < 3.0) {
                this.toucan.setFlying(false);
                this.toucan.peck();
                ++this.plantTime;
                if (this.plantTime > 60) {
                    BlockState state = this.toucan.getSaplingState();
                    if (state != null && state.m_60710_((LevelReader)this.toucan.m_9236_(), this.pos) && this.toucan.m_9236_().m_8055_(this.pos).m_247087_()) {
                        this.toucan.m_9236_().m_46597_(this.pos, state);
                        if (!this.toucan.isEnchanted()) {
                            this.toucan.setSaplingState(null);
                        }
                    }
                    this.m_8041_();
                }
            } else {
                BlockPos moveTo = this.pos;
                if (this.encircleTime > 0) {
                    moveTo = this.getVultureCirclePos(this.pos, 3.0f, up);
                }
                if (moveTo != null) {
                    if (this.encircleTime <= 0 && !this.toucan.hasLineOfSightSapling(this.pos)) {
                        this.toucan.setFlying(false);
                        this.toucan.m_21573_().m_26519_((double)((float)moveTo.m_123341_() + 0.5f), (double)moveTo.m_123342_() + up + 0.5, (double)((float)moveTo.m_123343_() + 0.5f), 1.0);
                    } else {
                        this.toucan.setFlying(true);
                        this.toucan.m_21566_().m_6849_((double)((float)moveTo.m_123341_() + 0.5f), (double)moveTo.m_123342_() + up + 0.5, (double)((float)moveTo.m_123343_() + 0.5f), 1.0);
                    }
                }
            }
        }

        public BlockPos getVultureCirclePos(BlockPos target, float circleDistance, double yLevel) {
            float angle = 0.13962634f * (float)(this.clockwise ? -this.encircleTime : this.encircleTime);
            double extraX = circleDistance * Mth.m_14031_((float)angle);
            double extraZ = circleDistance * Mth.m_14089_((float)angle);
            BlockPos pos = new BlockPos((int)((double)((float)target.m_123341_() + 0.5f) + extraX), (int)((double)(target.m_123342_() + 1) + yLevel), (int)((double)((float)target.m_123343_() + 0.5f) + extraZ));
            if (this.toucan.m_9236_().m_46859_(pos)) {
                return pos;
            }
            return null;
        }

        public void m_8041_() {
            this.toucan.aiItemFlag = false;
            this.pos = null;
            this.plantTime = 0;
            this.encircleTime = 0;
        }

        public boolean m_8045_() {
            return this.pos != null && this.toucan.getSaplingState() != null;
        }

        private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
            return blockpos.m_123331_((Vec3i)new BlockPos((int)positionVec.m_7096_(), blockpos.m_123342_(), (int)positionVec.m_7094_())) < distance * distance;
        }

        private BlockPos getSaplingPlantPos() {
            BlockState state = this.toucan.getSaplingState();
            if (state != null) {
                for (int i = 0; i < 15; ++i) {
                    BlockPos pos = this.toucan.m_20183_().m_7918_(EntityToucan.this.f_19796_.m_188503_(10) - 8, EntityToucan.this.f_19796_.m_188503_(8) - 4, EntityToucan.this.f_19796_.m_188503_(16) - 8);
                    if (!state.m_60710_((LevelReader)this.toucan.m_9236_(), pos) || !this.toucan.m_9236_().m_46859_(pos.m_7494_()) || !this.toucan.hasLineOfSightSapling(pos)) continue;
                    return pos;
                }
            }
            return null;
        }
    }

    private class AIWanderIdle
    extends Goal {
        protected final EntityToucan toucan;
        protected double x;
        protected double y;
        protected double z;
        private boolean flightTarget = false;

        public AIWanderIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.toucan = EntityToucan.this;
        }

        public boolean m_8036_() {
            if (this.toucan.m_20160_() || this.toucan.getSaplingState() != null || EntityToucan.this.aiItemFlag || this.toucan.m_5448_() != null && this.toucan.m_5448_().m_6084_() || this.toucan.m_20159_()) {
                return false;
            }
            if (this.toucan.m_217043_().m_188503_(45) != 0 && !this.toucan.isFlying()) {
                return false;
            }
            this.flightTarget = this.toucan.m_20096_() ? EntityToucan.this.f_19796_.m_188503_(6) == 0 : EntityToucan.this.f_19796_.m_188503_(5) != 0 && this.toucan.timeFlying < 200;
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
            if (this.flightTarget) {
                this.toucan.m_21566_().m_6849_(this.x, this.y, this.z, 1.0);
            } else {
                this.toucan.m_21573_().m_26519_(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntityToucan.this.isFlying() && this.toucan.m_20096_()) {
                this.toucan.setFlying(false);
            }
            if (EntityToucan.this.isFlying() && this.toucan.m_20096_() && this.toucan.timeFlying > 10) {
                this.toucan.setFlying(false);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.toucan.m_20182_();
            if (this.toucan.isOverWaterOrVoid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                if (this.toucan.timeFlying > 50 && this.toucan.isOverLeaves() && !this.toucan.m_20096_()) {
                    return this.toucan.getBlockGrounding(vector3d);
                }
                if (this.toucan.timeFlying < 200 || this.toucan.isOverWaterOrVoid()) {
                    return this.toucan.getBlockInViewAway(vector3d, 0.0f);
                }
                return this.toucan.getBlockGrounding(vector3d);
            }
            if (!this.toucan.m_20096_()) {
                return this.toucan.getBlockGrounding(vector3d);
            }
            if (this.toucan.isOverLeaves()) {
                for (int i = 0; i < 15; ++i) {
                    BlockPos pos = this.toucan.m_20183_().m_7918_(EntityToucan.this.f_19796_.m_188503_(16) - 8, EntityToucan.this.f_19796_.m_188503_(8) - 4, EntityToucan.this.f_19796_.m_188503_(16) - 8);
                    if (this.toucan.m_9236_().m_8055_(pos.m_7494_()).m_280296_() || !this.toucan.m_9236_().m_8055_(pos).m_280296_() || !(this.toucan.m_21692_(pos) >= 0.0f)) continue;
                    return Vec3.m_82539_((Vec3i)pos);
                }
            }
            return LandRandomPos.m_148488_((PathfinderMob)this.toucan, (int)16, (int)7);
        }

        public boolean m_8045_() {
            if (this.toucan.aiItemFlag) {
                return false;
            }
            if (this.flightTarget) {
                return this.toucan.isFlying() && this.toucan.m_20275_(this.x, this.y, this.z) > 2.0;
            }
            return !this.toucan.m_21573_().m_26571_() && !this.toucan.m_20160_();
        }

        public void m_8056_() {
            if (this.flightTarget) {
                this.toucan.setFlying(true);
                this.toucan.m_21566_().m_6849_(this.x, this.y, this.z, 1.0);
            } else {
                this.toucan.m_21573_().m_26519_(this.x, this.y, this.z, 1.0);
            }
        }

        public void m_8041_() {
            this.toucan.m_21573_().m_26573_();
            super.m_8041_();
        }
    }
}

