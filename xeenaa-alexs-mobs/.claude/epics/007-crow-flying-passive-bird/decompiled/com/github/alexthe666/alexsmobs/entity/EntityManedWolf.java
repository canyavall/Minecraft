/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicates
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.FollowParentGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.PanicGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.ai.util.LandRandomPos
 *  net.minecraft.world.entity.ai.village.poi.PoiManager
 *  net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Ingredient$TagValue
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.IDancingMob;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.message.MessageStartDancing;
import com.github.alexthe666.alexsmobs.misc.AMPointOfInterestRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityManedWolf
extends Animal
implements ITargetsDroppedItems,
IDancingMob {
    private static final EntityDataAccessor<Float> EAR_PITCH = SynchedEntityData.m_135353_(EntityManedWolf.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Float> EAR_YAW = SynchedEntityData.m_135353_(EntityManedWolf.class, (EntityDataSerializer)EntityDataSerializers.f_135029_);
    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.m_135353_(EntityManedWolf.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Integer> SHAKING_TIME = SynchedEntityData.m_135353_(EntityManedWolf.class, (EntityDataSerializer)EntityDataSerializers.f_135028_);
    private static final Ingredient allFoods = Ingredient.m_43938_(Stream.of(new Ingredient.TagValue(AMTagRegistry.MANED_WOLF_BREEDABLES), new Ingredient.TagValue(AMTagRegistry.MANED_WOLF_STENCH_FOODS)));
    public float prevEarPitch;
    public float prevEarYaw;
    public float prevDanceProgress;
    public float danceProgress;
    public float prevShakeProgress;
    public float shakeProgress;
    private int earCooldown = 0;
    private float targetPitch;
    private float targetYaw;
    private boolean isJukeboxing;
    private BlockPos jukeboxPosition;
    private BlockPos nearestAnthill;

    protected EntityManedWolf(EntityType<? extends Animal> animal, Level level) {
        super(animal, level);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 16.0).m_22268_(Attributes.f_22277_, 32.0).m_22268_(Attributes.f_22281_, 2.0).m_22268_(Attributes.f_22279_, (double)0.3f);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.manedWolfSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    protected void m_8099_() {
        super.m_8099_();
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new PanicGoal((PathfinderMob)this, 1.5));
        this.f_21345_.m_25352_(2, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(3, (Goal)new TemptGoal((PathfinderMob)this, 1.1, allFoods, false));
        this.f_21345_.m_25352_(4, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0, 60));
        this.f_21345_.m_25352_(5, (Goal)new FollowParentGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21345_.m_25352_(7, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new CreatureAITargetItems((PathfinderMob)this, false, 30));
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(EAR_PITCH, (Object)Float.valueOf(0.0f));
        this.f_19804_.m_135372_(EAR_YAW, (Object)Float.valueOf(0.0f));
        this.f_19804_.m_135372_(SHAKING_TIME, (Object)0);
        this.f_19804_.m_135372_(DANCING, (Object)false);
    }

    public float getEarYaw() {
        return ((Float)this.f_19804_.m_135370_(EAR_YAW)).floatValue();
    }

    public void setEarYaw(float yaw) {
        this.f_19804_.m_135381_(EAR_YAW, (Object)Float.valueOf(yaw));
    }

    public float getEarPitch() {
        return ((Float)this.f_19804_.m_135370_(EAR_PITCH)).floatValue();
    }

    public void setEarPitch(float pitch) {
        this.f_19804_.m_135381_(EAR_PITCH, (Object)Float.valueOf(pitch));
    }

    public boolean isDancing() {
        return (Boolean)this.f_19804_.m_135370_(DANCING);
    }

    @Override
    public void setDancing(boolean dancing) {
        this.f_19804_.m_135381_(DANCING, (Object)dancing);
        this.isJukeboxing = dancing;
    }

    protected SoundEvent m_7515_() {
        return (SoundEvent)AMSoundRegistry.MANED_WOLF_IDLE.get();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.MANED_WOLF_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.MANED_WOLF_HURT.get();
    }

    private void attractAnimals() {
        if (this.getShakingTime() % 5 == 0) {
            List list = this.m_9236_().m_45976_(Animal.class, this.m_20191_().m_82377_(16.0, 8.0, 16.0));
            for (Animal e : list) {
                TamableAnimal tamedMob;
                if (e instanceof EntityManedWolf || e instanceof TamableAnimal && (tamedMob = (TamableAnimal)e).m_21825_()) continue;
                e.m_6710_(null);
                e.m_6703_(null);
                Vec3 vec = LandRandomPos.m_148492_((PathfinderMob)e, (int)20, (int)7, (Vec3)this.m_20182_());
                if (vec == null) continue;
                e.m_21573_().m_26519_(vec.f_82479_, vec.f_82480_, vec.f_82481_, 1.5);
            }
        }
    }

    private void pollinateAnthill() {
        if (this.nearestAnthill != null && this.m_9236_().m_7702_(this.nearestAnthill) instanceof TileEntityLeafcutterAnthill) {
            if (this.getShakingTime() % 5 == 0) {
                this.m_21573_().m_26519_((double)((float)this.nearestAnthill.m_123341_() + 0.5f), (double)((float)this.nearestAnthill.m_123342_() + 1.0f), (double)((float)this.nearestAnthill.m_123343_() + 0.5f), 1.0);
            }
            if (this.nearestAnthill.m_203195_((Position)this.m_20182_(), 6.0) && this.getShakingTime() % 20 == 0) {
                ((TileEntityLeafcutterAnthill)this.m_9236_().m_7702_(this.nearestAnthill)).growFungus();
            }
        }
    }

    private void findAnthill() {
        if (this.nearestAnthill == null || !(this.m_9236_().m_7702_(this.nearestAnthill) instanceof TileEntityLeafcutterAnthill)) {
            PoiManager pointofinterestmanager = ((ServerLevel)this.m_9236_()).m_8904_();
            Stream stream = pointofinterestmanager.m_27138_(poiTypeHolder -> poiTypeHolder.m_203565_(AMPointOfInterestRegistry.LEAFCUTTER_ANT_HILL.getKey()), (Predicate)Predicates.alwaysTrue(), this.m_20183_(), 10, PoiManager.Occupancy.ANY);
            List listOfHives = stream.collect(Collectors.toList());
            BlockPos nearest = null;
            for (BlockPos pos : listOfHives) {
                if (nearest != null && !(pos.m_123331_((Vec3i)this.m_20183_()) < nearest.m_123331_((Vec3i)this.m_20183_()))) continue;
                nearest = pos;
            }
            this.nearestAnthill = nearest;
        }
    }

    @Override
    public void setJukeboxPos(BlockPos pos) {
        this.jukeboxPosition = pos;
    }

    public boolean isShaking() {
        return this.getShakingTime() > 0;
    }

    public int getShakingTime() {
        return (Integer)this.f_19804_.m_135370_(SHAKING_TIME);
    }

    public void setShakingTime(int shaking) {
        this.f_19804_.m_135381_(SHAKING_TIME, (Object)shaking);
    }

    public InteractionResult m_6071_(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (itemstack.m_204117_(AMTagRegistry.MANED_WOLF_STENCH_FOODS) && !this.isShaking() && this.m_21205_().m_41619_()) {
            this.m_142075_(player, hand, itemstack);
            this.eatItemEffect(itemstack);
            this.setShakingTime(100 + this.f_19796_.m_188503_(30));
            return InteractionResult.SUCCESS;
        }
        return type;
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

    public void m_8119_() {
        super.m_8119_();
        this.prevEarPitch = this.getEarPitch();
        this.prevEarYaw = this.getEarYaw();
        this.prevDanceProgress = this.danceProgress;
        this.prevShakeProgress = this.shakeProgress;
        if (!this.m_9236_().f_46443_) {
            this.updateEars();
        }
        boolean dance = this.isDancing();
        if (this.jukeboxPosition == null || !this.jukeboxPosition.m_203195_((Position)this.m_20182_(), 15.0) || !this.m_9236_().m_8055_(this.jukeboxPosition).m_60713_(Blocks.f_50131_)) {
            this.isJukeboxing = false;
            this.setDancing(false);
            this.jukeboxPosition = null;
        }
        if (dance && this.danceProgress < 5.0f) {
            this.danceProgress += 1.0f;
        }
        if (!dance && this.danceProgress > 0.0f) {
            this.danceProgress -= 1.0f;
        }
        if (this.isShaking() && this.shakeProgress < 5.0f) {
            this.shakeProgress += 1.0f;
        }
        if (!this.isShaking() && this.shakeProgress > 0.0f) {
            this.shakeProgress -= 1.0f;
        }
        if (this.isShaking()) {
            this.setShakingTime(this.getShakingTime() - 1);
            if (this.m_9236_().f_46443_) {
                double d0 = this.f_19796_.m_188583_() * 0.02;
                double d1 = (double)0.05f + this.f_19796_.m_188583_() * 0.02;
                double d2 = this.f_19796_.m_188583_() * 0.02;
                this.m_9236_().m_7106_((ParticleOptions)AMParticleRegistry.SMELLY.get(), this.m_20208_(0.7f), this.m_20227_(0.6f), this.m_20262_(0.7f), d0, d1, d2);
            } else {
                this.attractAnimals();
                this.findAnthill();
                if (this.nearestAnthill != null) {
                    this.pollinateAnthill();
                }
            }
        }
    }

    private void updateEars() {
        float pitchDist = Math.abs(this.targetPitch - this.getEarPitch());
        float yawDist = Math.abs(this.targetYaw - this.getEarYaw());
        if (this.earCooldown <= 0 && this.f_19796_.m_188503_(30) == 0 && pitchDist <= 0.1f && yawDist <= 0.1f) {
            this.targetPitch = Mth.m_14036_((float)(this.f_19796_.m_188501_() * 60.0f - 30.0f), (float)-30.0f, (float)30.0f);
            this.targetYaw = Mth.m_14036_((float)(this.f_19796_.m_188501_() * 60.0f - 30.0f), (float)-30.0f, (float)30.0f);
            this.earCooldown = 8 + this.f_19796_.m_188503_(15);
        }
        if (pitchDist > 0.1f) {
            if (this.getEarPitch() < this.targetPitch) {
                this.setEarPitch(this.getEarPitch() + Math.min(pitchDist, 4.0f));
            }
            if (this.getEarPitch() > this.targetPitch) {
                this.setEarPitch(this.getEarPitch() - Math.min(pitchDist, 4.0f));
            }
        }
        if (yawDist > 0.1f) {
            if (this.getEarYaw() < this.targetYaw) {
                this.setEarYaw(this.getEarYaw() + Math.min(yawDist, 4.0f));
            }
            if (this.getEarYaw() > this.targetYaw) {
                this.setEarYaw(this.getEarYaw() - Math.min(yawDist, 4.0f));
            }
        }
        if (this.earCooldown > 0) {
            --this.earCooldown;
        }
    }

    public boolean m_6898_(ItemStack stack) {
        return !stack.m_204117_(AMTagRegistry.MANED_WOLF_STENCH_FOODS) && allFoods.test(stack);
    }

    public void m_7023_(Vec3 vec3d) {
        if (this.isDancing() || this.danceProgress > 0.0f) {
            if (this.m_21573_().m_26570_() != null) {
                this.m_21573_().m_26573_();
            }
            vec3d = Vec3.f_82478_;
        }
        super.m_7023_(vec3d);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return allFoods.test(stack) && !this.isShaking();
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.eatItemEffect(e.m_32055_());
        if (e.m_32055_().m_204117_(AMTagRegistry.MANED_WOLF_STENCH_FOODS)) {
            this.setShakingTime(100 + this.f_19796_.m_188503_(30));
        }
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return (AgeableMob)((EntityType)AMEntityRegistry.MANED_WOLF.get()).m_20615_((Level)serverWorld);
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_6818_(BlockPos pos, boolean isPartying) {
        AlexsMobs.sendMSGToServer(new MessageStartDancing(this.m_19879_(), isPartying, pos));
        this.setDancing(isPartying);
        if (isPartying) {
            this.setJukeboxPos(pos);
        } else {
            this.setJukeboxPos(null);
        }
    }

    public boolean isEnder() {
        String s = ChatFormatting.m_126649_((String)this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("plummet") || s.toLowerCase().contains("ender"));
    }
}

