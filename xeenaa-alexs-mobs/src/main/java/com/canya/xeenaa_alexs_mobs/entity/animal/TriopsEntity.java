package com.canya.xeenaa_alexs_mobs.entity.animal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Triops entity with GeckoLib animation support.
 * Generated for Epic 03 testing.
 */
public class TriopsEntity extends FishEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TriopsEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Define entity attributes (health, speed, etc.)
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return FishEntity.createFishAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 3.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4D)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0D);
    }

    /**
     * Initialize AI goals for entity behavior.
     */
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, net.minecraft.entity.player.PlayerEntity.class, 8.0F, 1.6D, 1.4D));
        this.goalSelector.add(4, new SwimAroundGoal(this, 1.0D, 10));
    }

    /**
     * Register GeckoLib animation controllers.
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    /**
     * Animation controller predicate - determines which animation to play.
     */
    private PlayState predicate(AnimationState<TriopsEntity> state) {
        if (this.isSubmergedInWater()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.triops_citadel.swim", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("animation.triops_citadel.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /**
     * Returns the sound played when the triops flops on land.
     * Uses the generic fish flop sound.
     */
    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
    }

    /**
     * Returns the bucket item used to capture this entity.
     * Uses a water bucket as triops cannot be bucketed.
     */
    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(Items.WATER_BUCKET);
    }
}
