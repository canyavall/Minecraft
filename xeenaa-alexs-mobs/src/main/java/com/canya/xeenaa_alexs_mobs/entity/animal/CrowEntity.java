package com.canya.xeenaa_alexs_mobs.entity.animal;

import com.canya.xeenaa_alexs_mobs.entity.ai.LandOnBlockGoal;
import com.canya.xeenaa_alexs_mobs.entity.ai.WanderInAirGoal;
import com.canya.xeenaa_alexs_mobs.entity.base.FlyingAnimalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Crow entity - intelligent corvid bird with flying and ground movement.
 *
 * <p>The crow is a medium-sized bird entity that exhibits both aerial and ground-based behavior.
 * Unlike purely flying entities, crows can walk on the ground, land to rest, and take flight when
 * needed. They are intelligent birds that can be tempted with seeds and will flee from danger.
 *
 * <h2>Characteristics:</h2>
 * <ul>
 *   <li><b>Size</b>: 0.5 x 0.5 x 0.5 blocks (medium bird size)</li>
 *   <li><b>Health</b>: 6 HP (3 hearts)</li>
 *   <li><b>Ground Speed</b>: 0.25 (standard passive mob)</li>
 *   <li><b>Flying Speed</b>: 0.4 (moderate aerial movement)</li>
 *   <li><b>Behavior</b>: Ground walking, flying, landing, fleeing from threats</li>
 *   <li><b>Breeding</b>: Tempted by seeds (wheat seeds, beetroot seeds, etc.)</li>
 * </ul>
 *
 * <h2>AI Behavior:</h2>
 * <p>The crow has a complex AI that balances ground and aerial movement:
 * <ol start="0">
 *   <li><b>Panic</b>: Flee quickly when taking damage (priority 0)</li>
 *   <li><b>Wander in Air</b>: Random 3D aerial movement when flying (priority 1)</li>
 *   <li><b>Land on Blocks</b>: Occasionally land and rest on solid blocks (priority 2)</li>
 *   <li><b>Tempt</b>: Follow players holding seeds (priority 3)</li>
 *   <li><b>Wander Around</b>: Walk on ground when landed (priority 4)</li>
 *   <li><b>Look at Entity</b>: Look at nearby players (priority 5)</li>
 *   <li><b>Look Around</b>: Turn head when idle (priority 6)</li>
 * </ol>
 *
 * <h2>Animation System:</h2>
 * <p>This entity uses GeckoLib for realistic corvid animations:
 * <ul>
 *   <li><b>Idle</b>: Subtle head and tail movements when stationary</li>
 *   <li><b>Walk</b>: Ground walking animation with hopping gait</li>
 *   <li><b>Run</b>: Faster ground movement when fleeing or tempted</li>
 *   <li><b>Fly</b>: Wing flapping and body tilt during flight</li>
 *   <li><b>Sit</b>: Resting pose when landed on blocks</li>
 *   <li><b>Attack</b>: Pecking animation (reserved for future hostile behavior)</li>
 * </ul>
 *
 * <p><b>Required Asset Files:</b>
 * <ul>
 *   <li>{@code assets/xeenaa-alexs-mobs/geo/crow.geo.json} - GeckoLib model</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/animations/crow.animation.json} - Animations</li>
 *   <li>{@code assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png} - Texture</li>
 * </ul>
 *
 * <h2>Sound Events:</h2>
 * <p>The crow uses vanilla parrot sounds as placeholders:
 * <ul>
 *   <li>Ambient: {@link SoundEvents#ENTITY_PARROT_AMBIENT} - Cawing sounds</li>
 *   <li>Hurt: {@link SoundEvents#ENTITY_PARROT_HURT} - Distressed call</li>
 *   <li>Death: {@link SoundEvents#ENTITY_PARROT_DEATH} - Final call</li>
 * </ul>
 *
 * <h2>Breeding Mechanics:</h2>
 * <p>Crows can be tempted with seeds (similar to chickens):
 * <ul>
 *   <li>Wheat seeds, beetroot seeds, melon seeds, pumpkin seeds</li>
 *   <li>Following player when seeds are held</li>
 *   <li>Can be bred using seeds (creates baby crow)</li>
 * </ul>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. All methods should be called from the
 * appropriate game thread (server or client) as managed by Minecraft's entity system.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Animation controller evaluates state every tick - kept lightweight</li>
 *   <li>3D pathfinding used only when flying (performance cost)</li>
 *   <li>Ground pathfinding used when landed (less expensive)</li>
 *   <li>Landing behavior reduces aerial pathfinding overhead</li>
 * </ul>
 *
 * @author Canya
 * @see FlyingAnimalEntity
 * @see GeoEntity
 * @see WanderInAirGoal
 * @see LandOnBlockGoal
 */
public class CrowEntity extends FlyingAnimalEntity implements GeoEntity {

    /**
     * GeckoLib animation cache for this entity instance.
     *
     * <p>This cache stores animation state and controllers for GeckoLib's animation system.
     * It is initialized once per entity instance and persists animation state across ticks.
     *
     * @see #getAnimatableInstanceCache()
     */
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /**
     * Tracks whether the crow is currently sitting/resting on a block.
     * Used by animation controller to determine when to play sit animation.
     */
    private boolean isSitting = false;

    /**
     * Timer for sit duration (in ticks). Decrements while sitting.
     */
    private int sitTimer = 0;

    /**
     * Constructs a new crow entity.
     *
     * <p>This constructor is called by Minecraft's entity spawning system when creating
     * a crow instance (via natural spawning, commands, or {@link EntityType#create(World)}).
     *
     * @param entityType the entity type (should be from ModEntities)
     * @param world the world this entity is spawning in (server or client)
     * @throws NullPointerException if entityType or world is null
     * @see FlyingAnimalEntity#FlyingAnimalEntity(EntityType, World)
     */
    public CrowEntity(EntityType<? extends FlyingAnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // ============================================
    // Entity Attributes
    // ============================================

    /**
     * Creates default attributes for the crow entity.
     *
     * <p>This method defines the crow's base stats to create a moderately resilient,
     * intelligent bird with balanced ground and aerial movement.
     *
     * <p><b>Attribute Breakdown:</b>
     * <ul>
     *   <li><b>Max Health</b>: 6.0 HP (3 hearts)
     *       <ul>
     *         <li>More resilient than tiny birds (fly has 2 HP)</li>
     *         <li>Can survive a few hits from predators</li>
     *       </ul>
     *   </li>
     *   <li><b>Movement Speed</b>: 0.25 (ground walking)
     *       <ul>
     *         <li>Standard passive mob speed</li>
     *         <li>Used for walking when landed</li>
     *       </ul>
     *   </li>
     *   <li><b>Flying Speed</b>: 0.4 (aerial movement)
     *       <ul>
     *         <li>Moderate flight speed (faster than ground)</li>
     *         <li>Allows quick escape by flying</li>
     *       </ul>
     *   </li>
     *   <li><b>Follow Range</b>: 16.0 blocks
     *       <ul>
     *         <li>Intelligent bird with good awareness</li>
     *         <li>Can spot players from distance</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p>This method is called during entity registration via FabricDefaultAttributeRegistry.
     *
     * @return an attribute container builder with configured default attributes
     * @see EntityAttributes
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return FlyingAnimalEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4D)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D);
    }

    // ============================================
    // AI Goal System
    // ============================================

    /**
     * Registers custom AI goals specific to the crow's behavior.
     *
     * <p>This method is called by {@link com.canya.xeenaa_alexs_mobs.entity.BaseAnimalEntity#initGoals()}
     * after common animal goals are registered. The crow's AI balances ground and aerial movement.
     *
     * <p><b>Goal Priority Structure:</b>
     * <pre>
     * Priority 0: {@link EscapeDangerGoal} - Flee when taking damage
     *   - Speed: 1.4 (40% faster than normal)
     *   - Behavior: Run/fly away from danger rapidly
     *
     * Priority 1: {@link WanderInAirGoal} - Random aerial wandering
     *   - Speed: 0.4 (matches flying speed)
     *   - Range: 10 blocks (moderate wandering area)
     *   - Behavior: Fly to random positions in 3D space
     *
     * Priority 2: {@link LandOnBlockGoal} - Occasionally land to rest
     *   - Rest Duration: 2-10 seconds (randomized)
     *   - Behavior: Land on nearby solid blocks, sit idle
     *
     * Priority 3: {@link TemptGoal} - Follow players holding seeds
     *   - Speed: 1.2 (20% faster - eager for food)
     *   - Items: All vanilla seeds (wheat, beetroot, melon, pumpkin)
     *   - Behavior: Ground approach when player has seeds
     *
     * Priority 4: {@link WanderAroundGoal} - Walk on ground when landed
     *   - Speed: 1.0 (normal ground speed)
     *   - Behavior: Random ground movement when not flying
     *
     * Priority 5: {@link LookAtEntityGoal} - Look at players
     *   - Range: 6 blocks
     *   - Behavior: Turn head toward nearby players
     *
     * Priority 6: {@link LookAroundGoal} - Look around randomly
     *   - Behavior: Turn head to observe surroundings
     * </pre>
     *
     * <p><b>Design Notes:</b>
     * <ul>
     *   <li>Escape danger has highest priority (0) to ensure immediate escape from danger</li>
     *   <li>Aerial wandering (1) allows flight when not landed</li>
     *   <li>Landing (2) interrupts wandering to create rest periods</li>
     *   <li>Tempt (3) overrides idle behaviors when player has food</li>
     *   <li>Ground wander (4) provides movement when landed</li>
     *   <li>Looking behaviors (5-6) add life-like awareness</li>
     * </ul>
     *
     * <p><b>Inherited Goals:</b> Crows also inherit common goals from BaseAnimalEntity:
     * swimming (priority 0), escape danger (priority 1), and mating (priority 2).
     *
     * @see WanderInAirGoal
     * @see LandOnBlockGoal
     * @see TemptGoal
     * @see EscapeDangerGoal
     */
    @Override
    protected void registerCustomGoals() {
        // Priority 0: Escape danger when taking damage (high-priority escape)
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.4D));

        // Priority 1: Wander in air when flying (moderate range)
        this.goalSelector.add(1, new WanderInAirGoal(this, 0.4D, 10.0D));

        // Priority 2: Occasionally land on blocks to rest
        this.goalSelector.add(2, new LandOnBlockGoal(this));

        // Priority 3: Follow players holding seeds (tempt ingredient)
        this.goalSelector.add(3, new TemptGoal(
            this,
            1.2D,
            stack -> stack.isOf(Items.WHEAT_SEEDS)
                || stack.isOf(Items.BEETROOT_SEEDS)
                || stack.isOf(Items.MELON_SEEDS)
                || stack.isOf(Items.PUMPKIN_SEEDS),
            false
        ));

        // Priority 4: Wander on ground when landed
        this.goalSelector.add(4, new WanderAroundGoal(this, 1.0D));

        // Priority 5: Look at nearby players
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));

        // Priority 6: Look around randomly when idle
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    // ============================================
    // GeckoLib Animation System
    // ============================================

    /**
     * Registers animation controllers for this entity.
     *
     * <p>This method sets up the GeckoLib animation system by registering a single controller
     * that handles all crow animations (idle, walk, run, fly, sit, attack). The controller
     * uses a predicate to determine which animation to play based on entity state.
     *
     * <p>The controller is named "crowController" and has a 5-tick transition time for smooth
     * animation blending between states.
     *
     * @param controllers the controller registrar provided by GeckoLib
     * @see #animationPredicate(AnimationState)
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "crowController", 5, this::animationPredicate));
    }

    /**
     * Animation predicate that determines which animation to play based on entity state.
     *
     * <p>This method is called every tick by the GeckoLib animation system to determine
     * the current animation. It checks entity state (sitting, flying, moving, idle) and
     * switches between appropriate animations.
     *
     * <p><b>Animation State Logic:</b>
     * <ul>
     *   <li><b>Fly</b>: Entity is airborne (not on ground)
     *       <ul>
     *         <li>Animation: "animation.crow.fly" (loop continuously)</li>
     *         <li>Behavior: Wing flapping, legs tucked, body tilted forward</li>
     *       </ul>
     *   </li>
     *   <li><b>Sit</b>: Entity is sitting on ground (resting state)
     *       <ul>
     *         <li>Animation: "animation.crow.sit" (loop continuously)</li>
     *         <li>Behavior: Relaxed pose, head movements, tail twitching</li>
     *       </ul>
     *   </li>
     *   <li><b>Run</b>: Entity is on ground and moving fast (velocity > 0.1)
     *       <ul>
     *         <li>Animation: "animation.crow.run" (loop continuously)</li>
     *         <li>Behavior: Rapid hopping gait, head bobbing</li>
     *       </ul>
     *   </li>
     *   <li><b>Walk</b>: Entity is on ground and moving slowly (velocity > 0)
     *       <ul>
     *         <li>Animation: "animation.crow.walk" (loop continuously)</li>
     *         <li>Behavior: Walking gait with leg alternation</li>
     *       </ul>
     *   </li>
     *   <li><b>Idle</b>: Entity is stationary on ground (default)
     *       <ul>
     *         <li>Animation: "animation.crow.idle" (loop continuously)</li>
     *         <li>Behavior: Subtle breathing, head looking, tail movements</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p><b>Animation File Format:</b> Animation names use full format with prefix:
     * "animation.crow.idle", "animation.crow.walk", etc. This matches the GeckoLib
     * animation format in crow.animation.json.
     *
     * <p><b>Performance Note:</b> This method is called every tick (20 times per second).
     * All state checks are lightweight field accesses, so performance overhead is minimal.
     *
     * @param state the current animation state provided by GeckoLib
     * @return {@link PlayState#CONTINUE} to continue playing the current animation
     * @see AnimationState
     * @see PlayState
     */
    private PlayState animationPredicate(AnimationState<CrowEntity> state) {
        AnimationController<CrowEntity> controller = state.getController();

        // Fly animation when airborne
        if (!this.isOnGround()) {
            controller.setAnimation(RawAnimation.begin().then("animation.crow.fly", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // Sit animation when resting on ground
        if (isSitting) {
            controller.setAnimation(RawAnimation.begin().then("animation.crow.sit", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // Get movement speed to determine walk vs run
        double velocity = this.getVelocity().horizontalLength();

        // Run animation when moving fast on ground
        if (velocity > 0.1D) {
            controller.setAnimation(RawAnimation.begin().then("animation.crow.run", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // Walk animation when moving slowly on ground
        if (velocity > 0.0D) {
            controller.setAnimation(RawAnimation.begin().then("animation.crow.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // Idle animation when stationary (default)
        controller.setAnimation(RawAnimation.begin().then("animation.crow.idle", Animation.LoopType.LOOP));
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
    // Entity Tick & State Management
    // ============================================

    /**
     * Ticks the entity every game tick.
     *
     * <p>This method handles per-tick state updates for the crow, including:
     * <ul>
     *   <li>Updating sit state based on ground status</li>
     *   <li>Decrementing sit timer when resting</li>
     *   <li>Calling parent tick logic for movement, AI, etc.</li>
     * </ul>
     *
     * <p><b>Sit State Logic:</b> The crow sits when on ground and not moving.
     * After landing, a random sit duration (2-5 seconds) is set. When the timer
     * expires, the crow resumes normal behavior.
     */
    @Override
    public void tick() {
        super.tick();

        // Update sitting state
        if (this.isOnGround() && !this.getNavigation().isFollowingPath()) {
            // Start sitting if not already sitting
            if (!isSitting) {
                isSitting = true;
                // Random sit duration: 2-5 seconds (40-100 ticks)
                sitTimer = 40 + this.random.nextInt(60);
            }

            // Decrement sit timer
            if (sitTimer > 0) {
                sitTimer--;
            } else {
                // Timer expired - stop sitting
                isSitting = false;
            }
        } else {
            // Not on ground or moving - stop sitting
            isSitting = false;
            sitTimer = 0;
        }
    }

    // ============================================
    // Breeding System
    // ============================================

    /**
     * Creates a baby crow entity for breeding mechanics.
     *
     * <p>This method is called when two crows breed after being fed seeds.
     * The baby crow inherits no special properties (standard baby entity).
     *
     * @param world the server world where the baby will spawn
     * @param entity the other parent crow
     * @return a new baby CrowEntity
     */
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return com.canya.xeenaa_alexs_mobs.registry.ModEntities.CROW.create(world);
    }

    /**
     * Checks if the given item stack can be used to breed this crow.
     *
     * <p>Crows can be bred with any vanilla seeds:
     * <ul>
     *   <li>Wheat seeds ({@link Items#WHEAT_SEEDS})</li>
     *   <li>Beetroot seeds ({@link Items#BEETROOT_SEEDS})</li>
     *   <li>Melon seeds ({@link Items#MELON_SEEDS})</li>
     *   <li>Pumpkin seeds ({@link Items#PUMPKIN_SEEDS})</li>
     * </ul>
     *
     * <p>This matches the tempt goal items, ensuring consistent behavior.
     *
     * @param stack the item stack to check
     * @return {@code true} if the item is a breeding item (seeds), {@code false} otherwise
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.WHEAT_SEEDS)
            || stack.isOf(Items.BEETROOT_SEEDS)
            || stack.isOf(Items.MELON_SEEDS)
            || stack.isOf(Items.PUMPKIN_SEEDS);
    }

    // ============================================
    // Sound System
    // ============================================

    /**
     * Gets the ambient sound event for this crow.
     *
     * <p>Crows use vanilla parrot ambient sounds as placeholders. These are periodic
     * cawing sounds that play when the crow is alive and idle.
     *
     * <p><b>Future Enhancement:</b> Replace with custom crow caw sounds for more
     * realistic corvid vocalizations.
     *
     * @return {@link SoundEvents#ENTITY_PARROT_AMBIENT} - ambient caw sounds
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    /**
     * Gets the hurt sound event for this crow.
     *
     * <p>Crows use vanilla parrot hurt sounds as placeholders. This plays when the
     * crow takes damage from any source.
     *
     * <p><b>Future Enhancement:</b> Replace with custom crow distress calls.
     *
     * @param source the source of the damage (ignored)
     * @return {@link SoundEvents#ENTITY_PARROT_HURT} - hurt sound
     */
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    /**
     * Gets the death sound event for this crow.
     *
     * <p>Crows use vanilla parrot death sounds as placeholders. This plays when the
     * crow's health reaches zero.
     *
     * <p><b>Future Enhancement:</b> Replace with custom crow death call.
     *
     * @return {@link SoundEvents#ENTITY_PARROT_DEATH} - death sound
     */
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    // ============================================
    // Flight Behavior
    // ============================================

    /**
     * Checks if this crow should be actively flying.
     *
     * <p>This method is called by {@link FlyingAnimalEntity} every tick to determine
     * if the crow should use flight movement control. Crows prefer to walk on ground
     * and only fly when necessary (escaping danger, crossing gaps, etc.).
     *
     * <p><b>Behavior Logic:</b>
     * <ul>
     *   <li><b>Airborne</b>: Crow should be flying (return true)</li>
     *   <li><b>On Ground</b>: Crow prefers walking (return false)</li>
     * </ul>
     *
     * <p>This creates a bird that spends most time on ground but can take flight
     * when needed, matching real crow behavior.
     *
     * @return {@code true} if crow should be actively flying, {@code false} if grounded
     * @see FlyingAnimalEntity#shouldFly()
     */
    @Override
    protected boolean shouldFly() {
        // Fly when airborne, walk when on ground
        // This creates a ground-preferring bird that can fly when needed
        return !this.isOnGround();
    }
}
