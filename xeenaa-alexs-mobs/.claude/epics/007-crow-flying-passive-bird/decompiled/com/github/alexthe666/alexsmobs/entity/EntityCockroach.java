/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntitySelector
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.Shearable
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.AvoidEntityGoal
 *  net.minecraft.world.entity.ai.goal.BreedGoal
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.PanicGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.TemptGoal
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.LightLayer
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.common.IForgeShearable
 */
package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCentipedeHead;
import com.github.alexthe666.alexsmobs.entity.ITargetsDroppedItems;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFleeLight;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;

public class EntityCockroach
extends Animal
implements Shearable,
IForgeShearable,
ITargetsDroppedItems {
    public static final ResourceLocation MARACA_LOOT = new ResourceLocation("alexsmobs", "entities/cockroach_maracas");
    public static final ResourceLocation MARACA_HEADLESS_LOOT = new ResourceLocation("alexsmobs", "entities/cockroach_maracas_headless");
    protected static final EntityDimensions STAND_SIZE = EntityDimensions.m_20398_((float)0.7f, (float)0.9f);
    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.m_135353_(EntityCockroach.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> HEADLESS = SynchedEntityData.m_135353_(EntityCockroach.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Boolean> MARACAS = SynchedEntityData.m_135353_(EntityCockroach.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    private static final EntityDataAccessor<Optional<UUID>> NEAREST_MUSICIAN = SynchedEntityData.m_135353_(EntityCockroach.class, (EntityDataSerializer)EntityDataSerializers.f_135041_);
    private static final EntityDataAccessor<Boolean> BREADED = SynchedEntityData.m_135353_(EntityCockroach.class, (EntityDataSerializer)EntityDataSerializers.f_135035_);
    public int randomWingFlapTick = 0;
    public float prevDanceProgress;
    public float danceProgress;
    private boolean prevStand = false;
    private boolean isJukeboxing;
    private BlockPos jukeboxPosition;
    private int laCucarachaTimer = 0;
    public int timeUntilNextEgg = this.f_19796_.m_188503_(24000) + 24000;

    public EntityCockroach(EntityType type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.m_33035_().m_22268_(Attributes.f_22276_, 6.0).m_22268_(Attributes.f_22279_, (double)0.35f);
    }

    public static boolean isValidLightLevel(ServerLevelAccessor p_223323_0_, BlockPos p_223323_1_, RandomSource p_223323_2_) {
        if (p_223323_0_.m_45517_(LightLayer.SKY, p_223323_1_) > p_223323_2_.m_188503_(32)) {
            return false;
        }
        int lvt_3_1_ = p_223323_0_.m_6018_().m_46470_() ? p_223323_0_.m_46849_(p_223323_1_, 10) : p_223323_0_.m_46803_(p_223323_1_);
        return lvt_3_1_ <= p_223323_2_.m_188503_(8);
    }

    public boolean m_5545_(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.cockroachSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canMonsterSpawnInLight(EntityType<? extends EntityCockroach> p_223325_0_, ServerLevelAccessor p_223325_1_, MobSpawnType p_223325_2_, BlockPos p_223325_3_, RandomSource p_223325_4_) {
        return EntityCockroach.isValidLightLevel(p_223325_1_, p_223325_3_, p_223325_4_) && EntityCockroach.m_217057_(p_223325_0_, (LevelAccessor)p_223325_1_, (MobSpawnType)p_223325_2_, (BlockPos)p_223325_3_, (RandomSource)p_223325_4_);
    }

    public static <T extends Mob> boolean canCockroachSpawn(EntityType<EntityCockroach> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || !iServerWorld.m_45527_(pos) && pos.m_123342_() <= 64 && EntityCockroach.canMonsterSpawnInLight(entityType, iServerWorld, reason, pos, random);
    }

    public boolean m_6785_(double distanceToClosestPlayer) {
        return !this.m_8023_();
    }

    public boolean m_8023_() {
        return super.m_8023_() || this.m_8077_() || this.isBreaded() || this.isDancing() || this.hasMaracas() || this.isHeadless();
    }

    protected SoundEvent m_7975_(DamageSource damageSourceIn) {
        return (SoundEvent)AMSoundRegistry.COCKROACH_HURT.get();
    }

    protected SoundEvent m_5592_() {
        return (SoundEvent)AMSoundRegistry.COCKROACH_HURT.get();
    }

    protected void m_8099_() {
        this.f_21345_.m_25352_(0, (Goal)new FloatGoal((Mob)this));
        this.f_21345_.m_25352_(1, (Goal)new PanicGoal((PathfinderMob)this, 1.1));
        this.f_21345_.m_25352_(2, (Goal)new BreedGoal((Animal)this, 1.0));
        this.f_21345_.m_25352_(3, (Goal)new TemptGoal((PathfinderMob)this, 1.0, Ingredient.m_204132_(AMTagRegistry.COCKROACH_FOODSTUFFS), false));
        this.f_21345_.m_25352_(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, EntityCentipedeHead.class, 16.0f, 1.3, 1.0));
        this.f_21345_.m_25352_(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, Player.class, 8.0f, 1.3, 1.0){

            public boolean m_8036_() {
                return !EntityCockroach.this.isBreaded() && super.m_8036_();
            }
        });
        this.f_21345_.m_25352_(5, (Goal)new AnimalAIFleeLight((PathfinderMob)this, 1.0){

            @Override
            public boolean m_8036_() {
                return !EntityCockroach.this.isBreaded() && super.m_8036_();
            }
        });
        this.f_21345_.m_25352_(6, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0, 80));
        this.f_21345_.m_25352_(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.f_21345_.m_25352_(8, (Goal)new RandomLookAroundGoal((Mob)this));
        this.f_21346_.m_25352_(1, new CreatureAITargetItems((PathfinderMob)this, false));
    }

    public boolean m_6469_(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev) {
            this.randomWingFlapTick = 5 + this.f_19796_.m_188503_(15);
            if (this.m_21223_() <= 1.0f && amount > 0.0f && !this.isHeadless() && this.m_217043_().m_188503_(3) == 0) {
                this.setHeadless(true);
                if (!this.m_9236_().f_46443_) {
                    ServerLevel serverLevel = (ServerLevel)this.m_9236_();
                    for (int i = 0; i < 3; ++i) {
                        serverLevel.m_8767_((ParticleOptions)ParticleTypes.f_123763_, this.m_20208_(0.52f), this.m_20227_(1.0), this.m_20262_(0.52f), 1, 0.0, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
        return prev;
    }

    public boolean m_6898_(ItemStack stack) {
        return stack.m_204117_(AMTagRegistry.COCKROACH_BREEDABLES);
    }

    public void m_7380_(CompoundTag compound) {
        super.m_7380_(compound);
        compound.m_128379_("Maracas", this.hasMaracas());
        compound.m_128379_("Dancing", this.isDancing());
        compound.m_128379_("Breaded", this.isBreaded());
        compound.m_128405_("EggTime", this.timeUntilNextEgg);
    }

    public void m_7378_(CompoundTag compound) {
        super.m_7378_(compound);
        this.setMaracas(compound.m_128471_("Maracas"));
        this.setDancing(compound.m_128471_("Dancing"));
        this.setBreaded(compound.m_128471_("Breaded"));
        if (compound.m_128441_("EggTime")) {
            this.timeUntilNextEgg = compound.m_128451_("EggTime");
        }
    }

    @Nullable
    protected ResourceLocation m_7582_() {
        return this.hasMaracas() ? (this.isHeadless() ? MARACA_HEADLESS_LOOT : MARACA_LOOT) : super.m_7582_();
    }

    public float m_5610_(BlockPos pos, LevelReader worldIn) {
        return 0.5f - (float)Math.max(worldIn.m_45517_(LightLayer.BLOCK, pos), worldIn.m_45517_(LightLayer.SKY, pos));
    }

    public MobType m_6336_() {
        return MobType.f_21642_;
    }

    public EntityDimensions m_6972_(Pose poseIn) {
        return this.isDancing() ? STAND_SIZE.m_20388_(this.m_6134_()) : super.m_6972_(poseIn);
    }

    public boolean m_6673_(DamageSource source) {
        return source.m_276093_(DamageTypes.f_268671_) || source.m_276093_(DamageTypes.f_268722_) || source.m_276093_(DamageTypes.f_268612_) || source.m_269533_(DamageTypeTags.f_268415_) || source.m_19385_().equals("anvil") || super.m_6673_(source);
    }

    public InteractionResult m_6071_(Player p_230254_1_, InteractionHand p_230254_2_) {
        ItemStack lvt_3_1_ = p_230254_1_.m_21120_(p_230254_2_);
        if (lvt_3_1_.m_41720_() == AMItemRegistry.MARACA.get() && this.m_6084_() && !this.hasMaracas()) {
            this.setMaracas(true);
            lvt_3_1_.m_41774_(1);
            return InteractionResult.m_19078_((boolean)this.m_9236_().f_46443_);
        }
        if (lvt_3_1_.m_41720_() != AMItemRegistry.MARACA.get() && this.m_6084_() && this.hasMaracas()) {
            this.setMaracas(false);
            this.setDancing(false);
            this.m_19983_(new ItemStack((ItemLike)AMItemRegistry.MARACA.get()));
            return InteractionResult.SUCCESS;
        }
        return super.m_6071_(p_230254_1_, p_230254_2_);
    }

    protected void m_8097_() {
        super.m_8097_();
        this.f_19804_.m_135372_(DANCING, (Object)false);
        this.f_19804_.m_135372_(HEADLESS, (Object)false);
        this.f_19804_.m_135372_(MARACAS, (Object)false);
        this.f_19804_.m_135372_(NEAREST_MUSICIAN, Optional.empty());
        this.f_19804_.m_135372_(BREADED, (Object)false);
    }

    public boolean isDancing() {
        return (Boolean)this.f_19804_.m_135370_(DANCING);
    }

    public void setDancing(boolean dancing) {
        this.f_19804_.m_135381_(DANCING, (Object)dancing);
    }

    public boolean isHeadless() {
        return (Boolean)this.f_19804_.m_135370_(HEADLESS);
    }

    public void setHeadless(boolean head) {
        this.f_19804_.m_135381_(HEADLESS, (Object)head);
    }

    public boolean hasMaracas() {
        return (Boolean)this.f_19804_.m_135370_(MARACAS);
    }

    public void setMaracas(boolean head) {
        this.f_19804_.m_135381_(MARACAS, (Object)head);
    }

    public boolean isBreaded() {
        return (Boolean)this.f_19804_.m_135370_(BREADED);
    }

    public void setBreaded(boolean breaded) {
        this.f_19804_.m_135381_(BREADED, (Object)breaded);
    }

    @Nullable
    public UUID getNearestMusicianId() {
        return ((Optional)this.f_19804_.m_135370_(NEAREST_MUSICIAN)).orElse(null);
    }

    public void m_8119_() {
        Entity musician;
        boolean dance;
        super.m_8119_();
        this.prevDanceProgress = this.danceProgress;
        boolean bl = dance = this.isJukeboxing || this.isDancing();
        if (this.jukeboxPosition == null || !this.jukeboxPosition.m_203195_((Position)this.m_20182_(), 3.46) || !this.m_9236_().m_8055_(this.jukeboxPosition).m_60713_(Blocks.f_50131_)) {
            this.isJukeboxing = false;
            this.jukeboxPosition = null;
        }
        if (this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
        if (dance) {
            if (this.danceProgress < 5.0f) {
                this.danceProgress += 1.0f;
            }
        } else if (this.danceProgress > 0.0f) {
            this.danceProgress -= 1.0f;
        }
        if (!this.m_20096_() || this.f_19796_.m_188503_(200) == 0) {
            this.randomWingFlapTick = 5 + this.f_19796_.m_188503_(15);
        }
        if (this.randomWingFlapTick > 0) {
            --this.randomWingFlapTick;
        }
        if (this.prevStand != dance) {
            if (this.hasMaracas()) {
                this.tellOthersImPlayingLaCucaracha();
            }
            this.m_6210_();
        }
        if (!this.hasMaracas() && (musician = this.getNearestMusician()) != null) {
            if (!musician.m_6084_() || this.m_20270_(musician) > 10.0f || musician instanceof EntityCockroach && !((EntityCockroach)musician).hasMaracas()) {
                this.setNearestMusician(null);
                this.setDancing(false);
            } else {
                this.setDancing(true);
            }
        }
        if (this.hasMaracas()) {
            ++this.laCucarachaTimer;
            if (this.laCucarachaTimer % 20 == 0 && this.f_19796_.m_188501_() < 0.3f) {
                this.tellOthersImPlayingLaCucaracha();
            }
            this.setDancing(true);
            if (!this.m_20067_()) {
                this.m_9236_().m_7605_((Entity)this, (byte)67);
            }
        } else {
            this.laCucarachaTimer = 0;
        }
        if (!this.m_9236_().f_46443_ && this.m_6084_() && !this.m_6162_() && --this.timeUntilNextEgg <= 0) {
            ItemEntity dropped = this.m_19998_((ItemLike)AMItemRegistry.COCKROACH_OOTHECA.get());
            if (dropped != null) {
                dropped.m_32060_();
            }
            this.timeUntilNextEgg = this.f_19796_.m_188503_(24000) + 24000;
        }
        this.prevStand = dance;
    }

    private void tellOthersImPlayingLaCucaracha() {
        List list = this.m_9236_().m_6443_(EntityCockroach.class, this.getMusicianDistance(), EntitySelector.f_20408_);
        for (EntityCockroach roach : list) {
            if (roach.hasMaracas()) continue;
            roach.setNearestMusician(this.m_20148_());
        }
    }

    private AABB getMusicianDistance() {
        return this.m_20191_().m_82377_(10.0, 10.0, 10.0);
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_7822_(byte id) {
        if (id == 67) {
            AlexsMobs.PROXY.onEntityStatus((Entity)this, id);
        } else {
            super.m_7822_(id);
        }
    }

    public Entity getNearestMusician() {
        UUID id = this.getNearestMusicianId();
        if (id != null && !this.m_9236_().f_46443_) {
            return ((ServerLevel)this.m_9236_()).m_8791_(id);
        }
        return null;
    }

    public void setNearestMusician(@Nullable UUID uniqueId) {
        this.f_19804_.m_135381_(NEAREST_MUSICIAN, Optional.ofNullable(uniqueId));
    }

    @OnlyIn(value=Dist.CLIENT)
    public void m_6818_(BlockPos pos, boolean isPartying) {
        this.jukeboxPosition = pos;
        this.isJukeboxing = isPartying;
    }

    @Nullable
    public AgeableMob m_142606_(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityCockroach roach = (EntityCockroach)((EntityType)AMEntityRegistry.COCKROACH.get()).m_20615_((Level)serverWorld);
        roach.setBreaded(true);
        return roach;
    }

    public boolean m_6220_() {
        return this.m_6084_() && !this.m_6162_() && !this.isHeadless();
    }

    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.m_6220_();
    }

    public void m_5851_(SoundSource category) {
        this.m_6469_(this.m_269291_().m_269264_(), 0.0f);
        this.m_9236_().m_6269_(null, (Entity)this, SoundEvents.f_12344_, category, 1.0f, 1.0f);
        this.m_146850_(GameEvent.f_223708_);
        this.setHeadless(true);
    }

    @Nonnull
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.m_6269_(null, (Entity)this, SoundEvents.f_12344_, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0f, 1.0f);
        this.m_146850_(GameEvent.f_223708_);
        this.m_6469_(this.m_269291_().m_269264_(), 0.0f);
        if (!world.f_46443_) {
            for (int i = 0; i < 3; ++i) {
                ((ServerLevel)this.m_9236_()).m_8767_((ParticleOptions)ParticleTypes.f_123763_, this.m_20208_(0.52f), this.m_20227_(1.0), this.m_20262_(0.52f), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        this.setHeadless(true);
        return Collections.emptyList();
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.m_41720_().m_41472_() || stack.m_204117_(AMTagRegistry.COCKROACH_BREEDABLES);
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
    public void onGetItem(ItemEntity e) {
        if (e.m_32055_().m_41720_() == AMItemRegistry.MARACA.get()) {
            this.setMaracas(true);
        } else {
            if (e.m_32055_().hasCraftingRemainingItem()) {
                this.m_19983_(e.m_32055_().getCraftingRemainingItem().m_41777_());
            }
            this.m_5634_(5.0f);
            if (e.m_32055_().m_204117_(AMTagRegistry.COCKROACH_FOODSTUFFS)) {
                this.setBreaded(true);
            }
        }
    }
}

