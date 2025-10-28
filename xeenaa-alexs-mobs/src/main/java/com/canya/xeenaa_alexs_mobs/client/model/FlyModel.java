package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model for the Fly entity.
 *
 * <p>This class provides resource locations for the entity's geometry, texture,
 * and animation files. GeckoLib uses these locations to load and render the entity
 * with animated 3D models.
 *
 * <h2>Resource Files:</h2>
 * <ul>
 *   <li><b>Geometry:</b> {@code assets/xeenaa-alexs-mobs/geo/fly.geo.json}</li>
 *   <li><b>Texture:</b> {@code assets/xeenaa-alexs-mobs/textures/entity/fly.png}</li>
 *   <li><b>Animations:</b> {@code assets/xeenaa-alexs-mobs/animations/fly.animation.json}</li>
 * </ul>
 *
 * <h2>Model Structure:</h2>
 * <p>The {@code .geo.json} file defines a tiny insect model with 12 distinct cubes:
 * <ul>
 *   <li><b>Body</b>: Elongated ellipsoid (abdomen + thorax fused for simplicity)</li>
 *   <li><b>Head</b>: Small sphere with red eyes texture region</li>
 *   <li><b>Wings (2)</b>: Thin, transparent planes attached to thorax (left/right)</li>
 *   <li><b>Legs (6)</b>: Three pairs of thin cylinders (front, middle, rear)</li>
 *   <li><b>Antennae (2)</b>: Two thin curved segments extending from head</li>
 * </ul>
 *
 * <p>The model is scaled to fit a 0.3×0.2×0.3 block bounding box (tiny insect size).
 * This will be exported from Blockbench with detailed geometry and bone structure
 * in TASK-004.
 *
 * <h2>Animation System:</h2>
 * <p>The {@code .animation.json} file contains three animations:
 * <ul>
 *   <li><b>idle:</b> Subtle animations when landed on blocks
 *       <ul>
 *         <li>Wings folded at rest (no rapid movement)</li>
 *         <li>Antennae twitching (keyframe: rotate ±10°, 1.5s cycle)</li>
 *         <li>Legs shifting slightly (simulate restlessness)</li>
 *       </ul>
 *   </li>
 *   <li><b>fly:</b> Rapid wing motion during flight
 *       <ul>
 *         <li>Wings flapping rapidly (keyframe: rotate 0° → 70° → 0°, 0.1s cycle)</li>
 *         <li>Body oscillating (subtle bobbing motion for flight realism)</li>
 *         <li>Legs tucked under body during flight</li>
 *       </ul>
 *   </li>
 *   <li><b>death:</b> Death animation when killed
 *       <ul>
 *         <li>Wings stop flapping (freeze at rest angle)</li>
 *         <li>Body rotates (simulate tumbling fall, 360° spin over 1s)</li>
 *         <li>Legs splay outward (lifeless pose)</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <h2>Texture Mapping:</h2>
 * <p>The texture is a 16×16 or 32×32 pixel image with distinct regions:
 * <ul>
 *   <li><b>Body</b>: Black/dark grey segmented texture (thorax/abdomen)</li>
 *   <li><b>Head</b>: Dark grey with two red pixels for eyes (compound eye appearance)</li>
 *   <li><b>Wings</b>: Semi-transparent white/light grey with subtle veining</li>
 *   <li><b>Legs</b>: Black thin lines (minimal detail at this scale)</li>
 *   <li><b>Antennae</b>: Black thin segments</li>
 * </ul>
 *
 * <p>UV coordinates in the model will map these texture regions to the appropriate
 * cubes for a realistic fly appearance. The wings should use transparency for realism.
 *
 * <p><b>Note:</b> All asset files (geometry, texture, animations) will be created
 * manually in TASK-004 using Blockbench for the model/animations and an image editor
 * for the texture. This class defines the <i>paths</i> where those files must be placed.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>client-side only</b> and is NOT thread-safe. All methods are called
 * from the client render thread by GeckoLib's rendering system.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // In FlyRenderer constructor:
 * public FlyRenderer(EntityRendererFactory.Context context) {
 *     super(context, new FlyModel());
 * }
 * }</pre>
 *
 * @author Canya
 * @version 0.1.0
 * @see FlyEntity
 * @see com.canya.xeenaa_alexs_mobs.client.renderer.FlyRenderer
 * @see GeoModel
 */
@Environment(EnvType.CLIENT)
public class FlyModel extends GeoModel<FlyEntity> {

    /**
     * Gets the resource location for the entity's geometry model.
     *
     * <p>This method is called by GeckoLib to load the {@code .geo.json} file
     * that defines the entity's 3D structure, bones, and UV mapping.
     *
     * <p><b>Expected File:</b> {@code assets/xeenaa-alexs-mobs/geo/fly.geo.json}
     *
     * <p><b>File Format:</b> Bedrock Edition geometry format exported from Blockbench.
     * The model should contain 12 cubes organized into a bone hierarchy:
     * <pre>
     * - root (pivot at entity center)
     *   - body (main bone)
     *     - head
     *       - antennae_left
     *       - antennae_right
     *     - wing_left
     *     - wing_right
     *     - leg_front_left
     *     - leg_front_right
     *     - leg_middle_left
     *     - leg_middle_right
     *     - leg_rear_left
     *     - leg_rear_right
     * </pre>
     *
     * <p>This bone hierarchy allows independent animation of each body part
     * (e.g., wings flapping while legs move independently).
     *
     * @param entity the entity being rendered (unused in this implementation,
     *               but could be used for variant models)
     * @return identifier pointing to the geometry JSON file
     */
    @Override
    public Identifier getModelResource(FlyEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/fly_citadel.geo.json");
    }

    /**
     * Gets the resource location for the entity's texture.
     *
     * <p>This method is called by GeckoLib to load the texture image that will
     * be applied to the entity's model geometry.
     *
     * <p><b>Expected File:</b> {@code assets/xeenaa-alexs-mobs/textures/entity/fly.png}
     *
     * <p><b>Texture Size:</b> 16×16 or 32×32 pixels
     * <ul>
     *   <li><b>16×16</b>: Simpler, more pixelated (recommended for tiny mob)</li>
     *   <li><b>32×32</b>: More detail possible (slight overkill for 0.3 block entity)</li>
     * </ul>
     *
     * <p><b>Texture Regions:</b>
     * <ul>
     *   <li>Body: Black/dark grey with subtle segmentation lines</li>
     *   <li>Head: Dark grey with two bright red pixels for compound eyes</li>
     *   <li>Wings: Semi-transparent white/light grey with vein patterns</li>
     *   <li>Legs: Black thin lines (1-2 pixels wide)</li>
     *   <li>Antennae: Black thin segments</li>
     * </ul>
     *
     * <p><b>Dynamic Textures:</b> The entity parameter could be used to return different
     * textures based on variants, age, or other properties. For example:
     * <pre>{@code
     * // Example for rare golden fly variant:
     * if (entity.getDataTracker().get(VARIANT) == Variant.GOLDEN) {
     *     return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly_golden.png");
     * }
     * }</pre>
     *
     * <p>However, the current implementation uses a single static texture for all flies.
     *
     * @param entity the entity being rendered (unused in this basic implementation)
     * @return identifier pointing to the texture PNG file
     */
    @Override
    public Identifier getTextureResource(FlyEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly/fly.png");
    }

    /**
     * Gets the resource location for the entity's animation definitions.
     *
     * <p>This method is called by GeckoLib to load the {@code .animation.json} file
     * that defines all animations for this entity (idle, fly, death).
     *
     * <p><b>Expected File:</b> {@code assets/xeenaa-alexs-mobs/animations/fly.animation.json}
     *
     * <p><b>Animation List:</b>
     * <ul>
     *   <li>{@code idle} - Plays when entity is landed on ground (wings rest, antennae twitch)</li>
     *   <li>{@code fly} - Plays when entity is airborne and moving (rapid wing flapping)</li>
     *   <li>{@code death} - Plays once when entity dies (wings freeze, body spins/falls)</li>
     * </ul>
     *
     * <p><b>Animation Details:</b>
     * <ul>
     *   <li><b>Idle Animation:</b>
     *       <ul>
     *         <li>Duration: 1.5-2 seconds looping</li>
     *         <li>Keyframes: Antennae rotate ±10°, legs shift slightly</li>
     *         <li>Easing: Smooth (sine or quadratic) for natural motion</li>
     *       </ul>
     *   </li>
     *   <li><b>Fly Animation:</b>
     *       <ul>
     *         <li>Duration: 0.1-0.15 seconds looping (rapid buzzing)</li>
     *         <li>Keyframes: Wings rotate 0° → 70° → 0° (fast cycle)</li>
     *         <li>Easing: Linear or slight ease-in-out for rapid motion</li>
     *         <li>Body bobbing: Subtle Y-position oscillation (±0.05 blocks)</li>
     *       </ul>
     *   </li>
     *   <li><b>Death Animation:</b>
     *       <ul>
     *         <li>Duration: 1.0 second, play once</li>
     *         <li>Keyframes: Body rotates 360° around X-axis (tumbling fall)</li>
     *         <li>Wing freeze: Wings locked at rest angle (no flapping)</li>
     *         <li>Leg splay: Legs rotate outward to lifeless pose</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p><b>Animation Format:</b> Bedrock Edition animation format with keyframes,
     * rotation/position/scale transformations per bone, and easing functions.
     *
     * <p><b>Controller Integration:</b> Animations defined in this file are referenced
     * by name in {@link FlyEntity#animationPredicate(software.bernie.geckolib.animation.AnimationState)}.
     * The animation names in this file <b>must</b> match the names used in that predicate
     * ("idle", "fly", "death") for GeckoLib to play them correctly.
     *
     * @param entity the entity being rendered (unused in this implementation)
     * @return identifier pointing to the animation JSON file
     */
    @Override
    public Identifier getAnimationResource(FlyEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/fly_citadel.animation.json");
    }
}
