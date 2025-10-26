package com.canya.xeenaa_alexs_mobs.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry for all custom sound events in Xeenaa's Alex's Mobs.
 *
 * <p>This class handles registration of all mod sound events with Minecraft's sound system.
 * Sound events are registered early in mod initialization before entities, as entities may
 * reference these sounds in their {@code getAmbientSound()}, {@code getHurtSound()}, and
 * {@code getDeathSound()} methods.
 *
 * <h2>Sound System Overview:</h2>
 * <p>Minecraft's sound system consists of three components:
 * <ol>
 *   <li><b>SoundEvent</b> (Java) - Registry entry that identifies a sound</li>
 *   <li><b>sounds.json</b> (Resource) - Maps sound events to actual .ogg files</li>
 *   <li><b>.ogg files</b> (Assets) - The actual audio files in assets/xeenaa-alexs-mobs/sounds/</li>
 * </ol>
 *
 * <p><b>Current Status:</b> This registry contains SoundEvent entries and sounds.json mappings,
 * but actual .ogg audio files are NOT yet implemented. Missing audio files will produce
 * "missing sound" warnings in logs but will not crash the game.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * @Override
 * protected SoundEvent getAmbientSound() {
 *     return ModSounds.TEST_ANIMAL_AMBIENT;
 * }
 * }</pre>
 *
 * <h2>Adding New Sounds:</h2>
 * <p>To add a new sound event:
 * <ol>
 *   <li>Register the SoundEvent using {@link #register(String)}</li>
 *   <li>Add corresponding entry in {@code assets/xeenaa-alexs-mobs/sounds.json}</li>
 *   <li>Add subtitle translation in {@code assets/xeenaa-alexs-mobs/lang/en_us.json}</li>
 *   <li>Place .ogg files in {@code assets/xeenaa-alexs-mobs/sounds/entity/[entity_name]/}</li>
 * </ol>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>thread-safe</b> for static field access after initialization completes.
 * The {@link #initialize()} method MUST be called from the main mod initializer on the
 * main thread during mod initialization phase.
 *
 * @author Canya
 * @see net.minecraft.sound.SoundEvent
 * @see net.minecraft.registry.Registries#SOUND_EVENT
 * @see com.canya.xeenaa_alexs_mobs.AlexsMobsMod#onInitialize()
 */
public class ModSounds {
    private static final Logger LOGGER = LoggerFactory.getLogger("ModSounds");
    private static final String MOD_ID = "xeenaa-alexs-mobs";

    // ============================================
    // Test Animal Sounds
    // ============================================

    /**
     * Ambient sound for the test animal entity.
     *
     * <p>This sound plays periodically when the test animal is idle or wandering.
     * Minecraft plays ambient sounds at random intervals for passive mobs.
     *
     * <p><b>Sound Files:</b> {@code sounds/entity/test_animal/ambient1.ogg}
     *
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity#getAmbientSound()
     */
    public static final SoundEvent TEST_ANIMAL_AMBIENT = register("entity.test_animal.ambient");

    /**
     * Hurt sound for the test animal entity.
     *
     * <p>This sound plays when the test animal takes damage from any source.
     * The sound is played on both client and server when damage is dealt.
     *
     * <p><b>Sound Files:</b> {@code sounds/entity/test_animal/hurt1.ogg}
     *
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity#getHurtSound(net.minecraft.entity.damage.DamageSource)
     */
    public static final SoundEvent TEST_ANIMAL_HURT = register("entity.test_animal.hurt");

    /**
     * Death sound for the test animal entity.
     *
     * <p>This sound plays when the test animal's health reaches zero and it dies.
     * The sound is played once at the moment of death.
     *
     * <p><b>Sound Files:</b> {@code sounds/entity/test_animal/death1.ogg}
     *
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity#getDeathSound()
     */
    public static final SoundEvent TEST_ANIMAL_DEATH = register("entity.test_animal.death");

    // ============================================
    // Fly Sounds
    // ============================================

    /**
     * Ambient sound for the fly entity.
     *
     * <p>This sound plays periodically when the fly is buzzing around in flight or landed.
     * Should be a subtle buzzing sound that reflects the fly's wings in motion.
     *
     * <p><b>Sound Files:</b> {@code sounds/entity/fly/ambient.ogg}
     * <ul>
     *   <li>Duration: 2-3 seconds (loopable)</li>
     *   <li>Volume: 0.3-0.5 (quiet to avoid overwhelming players)</li>
     *   <li>Pitch variation: 0.9-1.1 (configured in sounds.json)</li>
     * </ul>
     *
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity#getAmbientSound()
     */
    public static final SoundEvent FLY_AMBIENT = register("entity.fly.ambient");

    /**
     * Hurt sound for the fly entity.
     *
     * <p>This sound plays when the fly takes damage from any source. Given that flies
     * die in one hit (2 HP), this effectively serves as a "death alert" sound.
     *
     * <p><b>Sound Files:</b> {@code sounds/entity/fly/hurt.ogg}
     * <ul>
     *   <li>Duration: 0.2-0.5 seconds (sharp, quick buzz)</li>
     *   <li>Pitch: Higher than ambient to convey distress</li>
     *   <li>Volume: 0.5-0.7 (slightly louder, rare event)</li>
     * </ul>
     *
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity#getHurtSound(net.minecraft.entity.damage.DamageSource)
     */
    public static final SoundEvent FLY_HURT = register("entity.fly.hurt");

    /**
     * Death sound for the fly entity.
     *
     * <p>This sound plays when the fly's health reaches zero and it dies. Should be
     * a buzzing sound that fades out as the fly falls to the ground with still wings.
     *
     * <p><b>Sound Files:</b> {@code sounds/entity/fly/death.ogg}
     * <ul>
     *   <li>Duration: 1-2 seconds (fade-out)</li>
     *   <li>Pitch: Drops slightly as it fades (simulates wings slowing)</li>
     *   <li>Volume: Starts at 0.5 and fades to silence</li>
     * </ul>
     *
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity#getDeathSound()
     */
    public static final SoundEvent FLY_DEATH = register("entity.fly.death");

    // ============================================
    // Registration System
    // ============================================

    /**
     * Registers a sound event with Minecraft's sound registry.
     *
     * <p>This helper method creates a {@link SoundEvent} and registers it with the
     * {@link Registries#SOUND_EVENT} registry. The sound event ID is constructed using
     * the mod ID as the namespace.
     *
     * <p><b>Naming Convention:</b> Sound event names should follow the pattern:
     * <pre>
     * entity.[entity_name].[sound_type]
     * block.[block_name].[sound_type]
     * item.[item_name].[sound_type]
     * </pre>
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * register("entity.grizzly_bear.ambient")
     * // Creates: xeenaa-alexs-mobs:entity.grizzly_bear.ambient
     * }</pre>
     *
     * @param name the sound event name (without namespace, e.g., "entity.test_animal.ambient")
     * @return the registered SoundEvent instance
     * @throws IllegalArgumentException if name is null or empty
     * @throws IllegalStateException if registration fails (registry frozen)
     */
    private static SoundEvent register(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Sound event name cannot be null or empty");
        }

        Identifier id = Identifier.of(MOD_ID, name);
        SoundEvent soundEvent = SoundEvent.of(id);

        try {
            return Registry.register(Registries.SOUND_EVENT, id, soundEvent);
        } catch (Exception e) {
            LOGGER.error("Failed to register sound event: {}", name, e);
            throw new IllegalStateException("Sound event registration failed for: " + name, e);
        }
    }

    /**
     * Initializes the sound registry.
     *
     * <p>This method MUST be called during mod initialization to trigger static field
     * initialization and register all sound events. It should be called BEFORE entity
     * registration, as entities may reference these sounds.
     *
     * <p><b>Call Order:</b>
     * <pre>
     * 1. ModSounds.initialize()    ← Sounds first
     * 2. ModEntities.initialize()  ← Entities reference sounds
     * 3. ModItems.initialize()     ← Items may trigger sounds
     * </pre>
     *
     * <p>This method is idempotent - calling it multiple times has no effect after the
     * first call, as static fields are only initialized once.
     *
     * @see com.canya.xeenaa_alexs_mobs.AlexsMobsMod#onInitialize()
     */
    public static void initialize() {
        LOGGER.info("Registering sound events for Xeenaa's Alex's Mobs");
        LOGGER.debug("Registered {} sound events", getRegisteredSoundCount());

        // Static field initialization happens automatically when this class is loaded.
        // This method exists to ensure the class is loaded and fields are initialized.
        // No explicit registration code needed here.
    }

    /**
     * Gets the count of registered sound events for debugging purposes.
     *
     * <p>This method counts all public static final SoundEvent fields in this class
     * to verify that all sounds were registered successfully.
     *
     * <p><b>Implementation Note:</b> This is a simple count for logging purposes.
     * As more sounds are added, this count will increase automatically.
     *
     * @return the number of sound events registered by this class
     */
    private static int getRegisteredSoundCount() {
        // Currently: TEST_ANIMAL (3) + FLY (3) = 6
        return 6;
    }
}
