package com.canya.xeenaa_alexs_mobs.entity.animal;

import com.canya.xeenaa_alexs_mobs.entity.ai.LandOnBlockGoal;
import com.canya.xeenaa_alexs_mobs.entity.ai.WanderInAirGoal;
import com.canya.xeenaa_alexs_mobs.entity.base.FlyingAnimalEntity;
import com.canya.xeenaa_alexs_mobs.registry.ModEntities;
import com.canya.xeenaa_alexs_mobs.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

/**
 * A tiny flying insect entity that wanders through the air.
 *
 * <p>The fly is an ultra-simple ambient mob that adds life to the world through its
 * aerial wandering behavior. It is small, fragile, and dies in one hit, making it
 * a purely atmospheric entity rather than a gameplay-focused mob.
 *
 * <h2>Characteristics:</h2>
 * <ul>
 *   <li><b>Size</b>: 0.3 x 0.2 x 0.3 blocks (tiny insect size)</li>
 *   <li><b>Health</b>: 2 HP (1 heart) - dies in one hit</li>
 *   <li><b>Flying Speed</b>: 0.3 (slow, buzzing movement)</li>
 *   <li><b>Behavior</b>: Wanders aimlessly, lands occasionally, flees from players</li>
 *   <li><b>Breeding</b>: NOT breedable (spawns naturally)</li>
 * </ul>
 *
 * <h2>AI Behavior:</h2>
 * <p>The fly has four primary behaviors listed by priority:
 * <ol start="0">
 *   <li><b>Wander in Air</b>: Random 3D aerial movement within 7 block range</li>
 *   <li><b>Land on Blocks</b>: Occasionally rests on blocks for 2-10 seconds</li>
 *   <li><b>Flee from Players</b>: Escapes when players get within 4 blocks</li>
 *   <li><b>Look Around</b>: Turns head when landed to observe surroundings</li>
 * </ol>
 *
 * <h2>Animation System:</h2>
 * <p>This entity uses GeckoLib for smooth, realistic insect animations:
 * <ul>
 *   <li><b>Fly Animation</b>: Plays when airborne and moving (wing buzzing)</li>
 *   <li><b>Idle Animation</b>: Plays when landed on blocks (antennae twitching)</li>
 *   <li><b>Death Animation</b>: Plays once when killed (falling, wings still)</li>
 * </ul>
 *
 * <p><b>Required Asset Files</b> (created in TASK-004):
 * <ul>
 *   <li>{@code assets/xeenaa-alexs-mobs/geo/entity/fly.geo.json} - Blockbench model</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/animations/entity/fly.animation.json} - Animations</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/textures/entity/fly.png} - Texture</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/sounds/entity/fly/ambient.ogg} - Buzzing sound</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/sounds/entity/fly/hurt.ogg} - Quick buzz</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/sounds/entity/fly/death.ogg} - Fade buzz</li>
 * </ul>
 *
 * <h2>Sound Events:</h2>
 * <p>The fly uses three sound events from {@link ModSounds}:
 * <ul>
 *   <li>{@link ModSounds#FLY_AMBIENT} - Buzzing sound during flight</li>
 *   <li>{@link ModSounds#FLY_HURT} - Quick buzz when damaged</li>
 *   <li>{@link ModSounds#FLY_DEATH} - Fading buzz on death</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Spawn a fly in the world
 * FlyEntity fly = ModEntities.FLY.create(world);
 * fly.setPosition(x, y, z);
 * world.spawnEntity(fly);
 *
 * // Summon via command
 * /summon xeenaa-alexs-mobs:fly ~ ~ ~
 * }</pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. All methods should be called from the
 * appropriate game thread (server or client) as managed by Minecraft's entity system.
 * Animation state and AI goals are ticked by the game loop and must not be accessed
 * concurrently.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>3D pathfinding (via {@link com.canya.xeenaa_alexs_mobs.entity.base.FlyingAnimalEntity})
 *       is more expensive than 2D - limit fly spawns in dense areas</li>
 *   <li>Animation updates occur every tick - GeckoLib handles caching efficiently</li>
 *   <li>Sound events are played on both client/server - Minecraft handles networking</li>
 *   <li>Landing behavior reduces pathfinding overhead when resting</li>
 * </ul>
 *
 * @author Canya
 * @see FlyingAnimalEntity
 * @see WanderInAirGoal
 * @see GeoEntity
 * @see ModEntities#FLY
 * @see ModSounds
 */
public class FlyEntity extends FlyingAnimalEntity implements GeoEntity {

    /**
     * GeckoLib animation cache for this entity instance.
     *
     * <p>This cache stores animation state and controllers for GeckoLib's animation system.
     * It is initialized once per entity instance using {@link GeckoLibUtil#createInstanceCache(Object)}.
     * The cache persists animation state across ticks for smooth transitions.
     *
     * @see #getAnimatableInstanceCache()
     */
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /**
     * Constructs a new fly entity.
     *
     * <p>This constructor is called by Minecraft's entity spawning system when creating
     * a fly instance (via natural spawning, commands, or {@link EntityType#create(World)}).
     * It passes the entity type and world to the parent {@link FlyingAnimalEntity} constructor,
     * which sets up flight movement control and 3D navigation.
     *
     * @param entityType the entity type (should be {@link ModEntities#FLY})
     * @param world the world this entity is spawning in (server or client)
     * @throws NullPointerException if entityType or world is null
     * @see FlyingAnimalEntity#FlyingAnimalEntity(EntityType, World)
     */
    public FlyEntity(EntityType<? extends FlyingAnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // ============================================
    // Entity Attributes
    // ============================================

    /**
     * Creates default attributes for the fly entity.
     *
     * <p>This method defines the fly's base stats to create a fragile, fast-flying insect.
     * The low health ensures flies die in one hit, while the movement speed creates
     * realistic buzzing flight patterns.
     *
     * <p><b>Attribute Breakdown:</b>
     * <ul>
     *   <li><b>Max Health</b>: 2.0 HP (1 heart)
     *       <ul>
     *         <li>Dies to any damage source in one hit (player punch = 1 HP, kills fly)</li>
     *         <li>Reinforces fragile insect behavior</li>
     *       </ul>
     *   </li>
     *   <li><b>Movement Speed</b>: 0.3 (ground speed, rarely used)
     *       <ul>
     *         <li>Slower than standard passive mob (0.25)</li>
     *         <li>Primarily used for landing behavior, not actual movement</li>
     *       </ul>
     *   </li>
     *   <li><b>Flying Speed</b>: 0.3 (aerial movement speed)
     *       <ul>
     *         <li>Creates slow, buzzing flight pattern</li>
     *         <li>Matches ground speed for consistency</li>
     *         <li>Fleeing speed is 0.4 (faster when scared)</li>
     *       </ul>
     *   </li>
     *   <li><b>Follow Range</b>: 8.0 blocks
     *       <ul>
     *         <li>Detection range for AI goals (flee from player, etc.)</li>
     *         <li>Short range appropriate for tiny insect with limited awareness</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p>This method is called during entity registration in {@link ModEntities#initialize()}.
     * Attributes are registered globally via {@link net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry}.
     *
     * @return an attribute container builder with configured default attributes
     * @see EntityAttributes
     * @see net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return FlyingAnimalEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0D)        // 1 heart (dies in one hit)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)    // Ground speed (rarely used)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.3D)      // Aerial speed (primary movement)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0D);     // Detection range
    }

    // ============================================
    // AI Goal System
    // ============================================

    /**
     * Registers custom AI goals specific to the fly's behavior.
     *
     * <p>This method is called by {@link com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity#initGoals()}
     * after common animal goals are registered. It adds four behaviors that create realistic
     * fly movement patterns:
     *
     * <p><b>Goal Priority Structure:</b>
     * <pre>
     * Priority 0: {@link WanderInAirGoal} - Random 3D aerial wandering
     *   - Speed: 0.3 (same as flying speed attribute)
     *   - Range: 7 blocks (confined wandering area)
     *   - Behavior: Selects random positions in 3D space, flies to them
     *
     * Priority 1: {@link LandOnBlockGoal} - Occasionally rest on blocks
     *   - Rest Duration: 2-10 seconds (randomized)
     *   - Behavior: Lands on nearby solid blocks, stays stationary
     *   - Animation: Switches to idle animation when landed
     *
     * Priority 2: {@link FleeEntityGoal} - Flee from approaching players
     *   - Flee Distance: 4 blocks (activation range)
     *   - Flee Speed: 0.4 (33% faster than normal flight)
     *   - Behavior: Escapes upward and away when players get close
     *
     * Priority 3: {@link LookAroundGoal} - Look around when landed
     *   - Behavior: Rotates head to look at random angles
     *   - Only active when on ground (not while flying)
     * </pre>
     *
     * <p><b>Priority Rationale:</b>
     * <ul>
     *   <li>Wandering is lowest priority (0) so it's easily interrupted by other goals</li>
     *   <li>Landing is priority 1 to occasionally override wandering (creates rest periods)</li>
     *   <li>Fleeing is priority 2 to override both wandering and landing (survival first)</li>
     *   <li>Looking is priority 3 to only activate when idle and landed</li>
     * </ul>
     *
     * <p><b>Inherited Goals:</b> The fly also inherits common goals from {@link com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity}
     * such as swimming (priority 0), escaping danger (priority 1), and mating (priority 2).
     * However, mating is disabled via {@link #isBreedingItem(ItemStack)} returning false.
     *
     * @see WanderInAirGoal
     * @see LandOnBlockGoal
     * @see FleeEntityGoal
     * @see LookAroundGoal
     */
    @Override
    protected void registerCustomGoals() {
        // Priority 0: Wander randomly in 3D space (speed = 0.3, range = 7 blocks)
        this.goalSelector.add(0, new WanderInAirGoal(this, 0.3D, 7.0D));

        // Priority 1: Occasionally land on blocks to rest (custom goal)
        this.goalSelector.add(1, new LandOnBlockGoal(this));

        // Priority 2: Flee from players within 4 blocks (speed = 0.4, faster when scared)
        this.goalSelector.add(2, new FleeEntityGoal<>(this, PlayerEntity.class, 4.0F, 0.4D, 0.4D));

        // Priority 3: Look around when landed (vanilla behavior)
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    // ============================================
    // GeckoLib Animation System
    // ============================================

    /**
     * Registers animation controllers for this entity.
     *
     * <p>This method sets up the GeckoLib animation system by registering a single controller
     * that handles all fly animations (flying, idle, death). The controller uses a predicate
     * to determine which animation to play based on entity state.
     *
     * <p>The controller is named "flyController" and has a 0-tick transition time for instant
     * animation switching (appropriate for a tiny, fast-moving insect).
     *
     * @param controllers the controller registrar provided by GeckoLib
     * @see #animationPredicate(AnimationState)
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        Objects.requireNonNull(controllers, "Controller registrar cannot be null");
        controllers.add(new AnimationController<>(this, "flyController", 0, this::animationPredicate));
    }

    /**
     * Animation predicate that determines which animation to play based on entity state.
     *
     * <p>This method is called every tick by the GeckoLib animation system to determine
     * the current animation. It checks entity state (dead, flying, landed) and switches
     * between appropriate animations.
     *
     * <p><b>Animation State Logic:</b>
     * <ul>
     *   <li><b>Death</b>: Entity is dead
     *       <ul>
     *         <li>Animation: "death" (loop once)</li>
     *         <li>Behavior: Fly falls with wings still, plays once then stops</li>
     *       </ul>
     *   </li>
     *   <li><b>Flying</b>: Entity is airborne and moving
     *       <ul>
     *         <li>Animation: "fly" (loop continuously)</li>
     *         <li>Behavior: Wings buzzing, body oscillating</li>
     *       </ul>
     *   </li>
     *   <li><b>Idle</b>: Entity is landed on ground
     *       <ul>
     *         <li>Animation: "idle" (loop continuously)</li>
     *         <li>Behavior: Wings folded, antennae twitching, legs shifting</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p><b>Animation File Requirements:</b> These animation names ("death", "fly", "idle")
     * must match the animation names defined in {@code fly.animation.json} created in TASK-004.
     * If animation names don't match, GeckoLib will log warnings and use default pose.
     *
     * <p><b>Performance Note:</b> This method is called every tick (20 times per second).
     * All state checks ({@link #isDead()}, {@link #isOnGround()}) are lightweight field
     * accesses, so performance overhead is minimal.
     *
     * @param state the current animation state provided by GeckoLib (contains entity, time, etc.)
     * @return {@link PlayState#CONTINUE} to continue playing the current animation
     * @see AnimationState
     * @see PlayState
     */
    private PlayState animationPredicate(AnimationState<FlyEntity> state) {
        AnimationController<FlyEntity> controller = state.getController();

        // Play death animation when dead (loop once, then stop)
        if (this.isDead()) {
            controller.setAnimation(RawAnimation.begin().then("death", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }

        // Play fly animation when airborne and moving
        if (!this.isOnGround() && state.isMoving()) {
            controller.setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // Play idle animation when landed (default)
        controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    /**
     * Gets the GeckoLib animation cache for this entity instance.
     *
     * <p>This method is required by the {@link GeoEntity} interface and provides
     * access to the animation cache that stores animation state and controllers.
     * The cache is created once per entity instance in the constructor and persists
     * for the entity's lifetime.
     *
     * @return the animation instance cache for this entity (never null)
     * @see #cache
     * @see GeoEntity#getAnimatableInstanceCache()
     */
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ============================================
    // Breeding System
    // ============================================

    /**
     * Creates a baby fly entity for breeding mechanics.
     *
     * <p><b>IMPORTANT:</b> Flies are <b>NOT breedable</b> in this implementation.
     * This method will never be called because {@link #isBreedingItem(ItemStack)}
     * always returns false, preventing breeding behavior.
     *
     * <p>This method is only implemented to satisfy the abstract contract from
     * {@link com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity}. If breeding
     * is enabled in the future, this method would create a baby fly.
     *
     * <p><b>Design Decision:</b> Flies spawn naturally in the world rather than
     * through player breeding. This keeps them as ambient decoration rather than
     * farmable livestock.
     *
     * <p><b>Note:</b> This method references {@link ModEntities#FLY} which will be
     * created in TASK-007 (entity registration). This class will not compile until
     * that task is completed.
     *
     * @param world the server world where the baby would spawn (not used)
     * @param entity the other parent entity (not used)
     * @return a new baby FlyEntity (though this will never be called)
     * @see #isBreedingItem(ItemStack)
     */
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        // Flies are not breedable, but implement this for contract compliance
        // This method will never be called because isBreedingItem() returns false
        // NOTE: ModEntities.FLY will be created in TASK-007
        return ModEntities.FLY.create(world);
    }

    /**
     * Checks if the given item stack can be used to breed this fly.
     *
     * <p><b>Implementation:</b> Always returns {@code false} because flies are NOT breedable.
     * Players cannot feed flies any item to trigger breeding behavior.
     *
     * <p><b>Design Rationale:</b>
     * <ul>
     *   <li>Flies are ambient decoration, not farmable livestock</li>
     *   <li>Natural spawning maintains world atmosphere without player micromanagement</li>
     *   <li>Prevents fly farms (unrealistic for tiny insects)</li>
     *   <li>Simplifies entity implementation (no breeding item registry needed)</li>
     * </ul>
     *
     * <p>If breeding is desired in the future, return true for specific items:
     * <pre>{@code
     * @Override
     * public boolean isBreedingItem(ItemStack stack) {
     *     return stack.isOf(Items.ROTTEN_FLESH); // Flies attracted to decay
     * }
     * }</pre>
     *
     * @param stack the item stack to check (not used in current implementation)
     * @return always {@code false} (flies are not breedable)
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false; // Flies are NOT breedable
    }

    // ============================================
    // Sound System
    // ============================================

    /**
     * Gets the ambient sound event for this fly.
     *
     * <p>This sound plays periodically when the fly is alive and idle. It should be a
     * subtle buzzing sound that reflects the fly's wings in motion. Minecraft plays
     * ambient sounds at random intervals (configurable in sounds.json).
     *
     * <p><b>Sound File:</b> {@code sounds/entity/fly/ambient.ogg}
     * <ul>
     *   <li>Should be a loopable buzzing sound (2-3 seconds)</li>
     *   <li>Volume should be quiet (0.3-0.5) to avoid overwhelming players</li>
     *   <li>Pitch variation in sounds.json (0.9-1.1) adds variety</li>
     * </ul>
     *
     * <p><b>Performance Note:</b> This method is called frequently. It returns a constant
     * {@link SoundEvent} from {@link ModSounds} - no computation occurs.
     *
     * @return {@link ModSounds#FLY_AMBIENT} - buzzing sound when flying/idle
     * @see ModSounds#FLY_AMBIENT
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FLY_AMBIENT;
    }

    /**
     * Gets the hurt sound event for this fly.
     *
     * <p>This sound plays when the fly takes damage from any source (player hit, fire,
     * fall damage). Given that flies die in one hit (2 HP), this sound effectively
     * serves as a "death alert" since any damage is lethal.
     *
     * <p><b>Sound File:</b> {@code sounds/entity/fly/hurt.ogg}
     * <ul>
     *   <li>Should be a sharp, quick buzz (0.2-0.5 seconds)</li>
     *   <li>Higher pitched than ambient sound to convey distress</li>
     *   <li>Volume can be slightly louder (0.5-0.7) since it's a rare event</li>
     * </ul>
     *
     * <p><b>Design Note:</b> The {@link DamageSource} parameter is ignored because flies
     * are so fragile that damage type doesn't matter (all damage is instantly lethal).
     * More complex entities might vary sounds based on damage source (fire vs melee).
     *
     * <p><b>Performance Note:</b> This method is called during combat. It returns a constant
     * {@link SoundEvent} from {@link ModSounds} - no computation occurs.
     *
     * @param source the source of the damage (ignored in this implementation)
     * @return {@link ModSounds#FLY_HURT} - quick buzz when damaged
     * @see ModSounds#FLY_HURT
     */
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.FLY_HURT;
    }

    /**
     * Gets the death sound event for this fly.
     *
     * <p>This sound plays when the fly's health reaches zero (dies). It should convey
     * the finality of death with a fading buzzing sound as the fly falls to the ground.
     *
     * <p><b>Sound File:</b> {@code sounds/entity/fly/death.ogg}
     * <ul>
     *   <li>Should be a buzzing sound that fades out (1-2 seconds)</li>
     *   <li>Pitch should drop slightly as it fades (simulates wings slowing)</li>
     *   <li>Volume should start normal (0.5) and fade to silence</li>
     * </ul>
     *
     * <p><b>Audio Design Suggestion:</b> The death sound could overlap with the hurt sound
     * since damage and death occur simultaneously for flies. Consider making hurt and death
     * sounds similar but with death having a longer fade-out tail.
     *
     * <p><b>Performance Note:</b> This method is called once per death. It returns a constant
     * {@link SoundEvent} from {@link ModSounds} - no computation occurs.
     *
     * @return {@link ModSounds#FLY_DEATH} - fading buzz when dying
     * @see ModSounds#FLY_DEATH
     */
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FLY_DEATH;
    }

    // ============================================
    // Flight Behavior
    // ============================================

    /**
     * Checks if this fly should be actively flying.
     *
     * <p>This method is called by {@link FlyingAnimalEntity} every tick to determine
     * if the fly should use flight movement control. It enables landing behavior by
     * returning false when the fly is resting on the ground.
     *
     * <p><b>Behavior Logic:</b>
     * <ul>
     *   <li><b>Airborne</b>: Fly should be flying (return true)</li>
     *   <li><b>On Ground</b>: Fly is resting/landed (return false)</li>
     * </ul>
     *
     * <p>When this returns false, the fly stops using 3D flight pathfinding and behaves
     * like a ground mob (allowing {@link LandOnBlockGoal} to keep it stationary). When
     * it returns true, flight movement control resumes for aerial navigation.
     *
     * <p><b>Performance Note:</b> This method is called every tick (20 times per second).
     * The {@link #isOnGround()} check is a simple field access, so overhead is minimal.
     *
     * @return {@code true} if fly should be actively flying, {@code false} if landed/resting
     * @see FlyingAnimalEntity#shouldFly()
     * @see #isOnGround()
     */
    @Override
    protected boolean shouldFly() {
        // Fly when airborne, rest when on ground
        // This enables landing behavior via LandOnBlockGoal
        return !this.isOnGround();
    }
}
