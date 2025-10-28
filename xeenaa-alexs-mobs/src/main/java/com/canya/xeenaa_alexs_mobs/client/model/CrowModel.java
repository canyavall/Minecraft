package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.CrowEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model definition for the Crow entity.
 *
 * <p>This class provides resource paths for the crow's 3D model, animations, and textures.
 * GeckoLib uses this information to load and render the crow entity with its animations.
 *
 * <h2>Required Asset Files:</h2>
 * <ul>
 *   <li><b>Model</b>: {@code assets/xeenaa-alexs-mobs/geo/crow.geo.json}
 *       <ul>
 *         <li>GeckoLib 1.12.0 format geometry definition</li>
 *         <li>Defines bones: root, body, head, beak, wings, legs, tail</li>
 *         <li>Texture size: 32x32 pixels</li>
 *       </ul>
 *   </li>
 *   <li><b>Animations</b>: {@code assets/xeenaa-alexs-mobs/animations/crow.animation.json}
 *       <ul>
 *         <li>GeckoLib 1.8.0 format animation definitions</li>
 *         <li>Animations: idle, walk, run, fly, sit, attack</li>
 *         <li>Loop types: All loop continuously except attack (play once)</li>
 *       </ul>
 *   </li>
 *   <li><b>Texture</b>: {@code assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png}
 *       <ul>
 *         <li>32x32 pixel texture map</li>
 *         <li>Black crow with subtle gray highlights</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <h2>Animation Names (Defined in crow.animation.json):</h2>
 * <ul>
 *   <li>{@code animation.crow.idle} - Subtle head/tail movements (2.0s loop)</li>
 *   <li>{@code animation.crow.walk} - Ground walking gait (1.0s loop)</li>
 *   <li>{@code animation.crow.run} - Fast hopping movement (0.6s loop)</li>
 *   <li>{@code animation.crow.fly} - Wing flapping flight (0.8s loop)</li>
 *   <li>{@code animation.crow.sit} - Resting pose (1.0s loop)</li>
 *   <li>{@code animation.crow.attack} - Pecking attack (0.5s play once)</li>
 * </ul>
 *
 * <h2>Model Structure (From crow.geo.json):</h2>
 * <pre>
 * root (pivot: [0, 24, 0])
 *   ├─ body (cubes: 3x5x3, rotation: [57.5, 0, 0])
 *   │   ├─ head (cubes: 3x4x3, rotation: [-42.5, 0, 0])
 *   │   │   └─ beak (cubes: 1x2x3)
 *   │   ├─ wing_left (cubes: 1x6x3, rotation: [2.5, 0, 0])
 *   │   ├─ wing_right (cubes: 1x6x3, rotation: [2.5, 0, 0])
 *   │   ├─ leg_left (cubes: 1x2x3, rotation: [32.5, 0, 0])
 *   │   ├─ leg_right (cubes: 1x2x3, rotation: [32.5, 0, 0])
 *   │   └─ tail (cubes: 3x4x2, rotation: [-7.5, 0, 0])
 * </pre>
 *
 * <h2>Usage:</h2>
 * <p>This model is used by {@link com.canya.xeenaa_alexs_mobs.client.renderer.CrowRenderer}
 * to render the crow entity in-game. The model automatically loads assets from the paths
 * specified in {@link #getModelResource(CrowEntity)}, {@link #getTextureResource(CrowEntity)},
 * and {@link #getAnimationResource(CrowEntity)}.
 *
 * <p><b>Example Usage:</b>
 * <pre>{@code
 * // In CrowRenderer constructor:
 * super(renderManager, new CrowModel());
 *
 * // GeckoLib automatically calls model methods to load assets
 * }</pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>thread-safe</b>. All methods are pure functions that return constant
 * {@link Identifier} objects. GeckoLib may call these methods from rendering threads.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Asset paths are constant - no runtime computation</li>
 *   <li>GeckoLib caches loaded models and textures automatically</li>
 *   <li>Methods return the same Identifier every call (no allocation overhead)</li>
 * </ul>
 *
 * @author Canya
 * @see GeoModel
 * @see CrowEntity
 * @see com.canya.xeenaa_alexs_mobs.client.renderer.CrowRenderer
 */
public class CrowModel extends GeoModel<CrowEntity> {

    /**
     * Gets the resource path for the crow's 3D model file.
     *
     * <p>This method returns the path to the GeckoLib geometry JSON file that defines
     * the crow's 3D structure (bones, cubes, pivots, rotations). The model is in
     * GeckoLib 1.12.0 format exported from Blockbench.
     *
     * <p><b>Asset Location:</b>
     * {@code assets/xeenaa-alexs-mobs/geo/crow.geo.json}
     *
     * <p><b>Model Details:</b>
     * <ul>
     *   <li>Format: GeckoLib 1.12.0</li>
     *   <li>Identifier: geometry.crow</li>
     *   <li>Texture dimensions: 32x32 pixels</li>
     *   <li>Visible bounds: 2x2x2 blocks</li>
     *   <li>Bones: root, body, head, beak, wings (left/right), legs (left/right), tail</li>
     * </ul>
     *
     * <p>GeckoLib loads this file once and caches it for all crow entities.
     * The model is shared across all crow instances for efficiency.
     *
     * @param entity the crow entity being rendered (not used - same model for all crows)
     * @return the resource identifier for the crow geometry file
     */
    @Override
    public Identifier getModelResource(CrowEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/crow.geo.json");
    }

    /**
     * Gets the resource path for the crow's texture file.
     *
     * <p>This method returns the path to the PNG texture that is mapped onto the
     * crow's 3D model. The texture follows the UV mapping defined in crow.geo.json.
     *
     * <p><b>Asset Location:</b>
     * {@code assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png}
     *
     * <p><b>Texture Details:</b>
     * <ul>
     *   <li>Dimensions: 32x32 pixels</li>
     *   <li>Format: PNG with transparency (alpha channel)</li>
     *   <li>UV mapping: Matches crow.geo.json cube UVs</li>
     *   <li>Design: Black crow with subtle gray highlights and beak detail</li>
     * </ul>
     *
     * <p>GeckoLib loads this texture once and caches it. The same texture is used
     * for all crow entities unless variant textures are added in the future.
     *
     * <p><b>Future Enhancement:</b> This method could be extended to return different
     * textures based on entity data (e.g., rare albino crow variant):
     * <pre>{@code
     * if (entity.isAlbino()) {
     *     return Identifier.of("xeenaa-alexs-mobs", "textures/entity/crow/crow_albino.png");
     * }
     * }</pre>
     *
     * @param entity the crow entity being rendered (currently not used)
     * @return the resource identifier for the crow texture file
     */
    @Override
    public Identifier getTextureResource(CrowEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/crow/crow.png");
    }

    /**
     * Gets the resource path for the crow's animation file.
     *
     * <p>This method returns the path to the GeckoLib animation JSON file that defines
     * all of the crow's animations (idle, walk, run, fly, sit, attack). The animation
     * controller in {@link CrowEntity#animationPredicate(software.bernie.geckolib.animation.AnimationState)}
     * references these animations by name.
     *
     * <p><b>Asset Location:</b>
     * {@code assets/xeenaa-alexs-mobs/animations/crow.animation.json}
     *
     * <p><b>Animation File Details:</b>
     * <ul>
     *   <li>Format: GeckoLib 1.8.0</li>
     *   <li>Animation count: 6 (idle, walk, run, fly, sit, attack)</li>
     *   <li>All animations loop continuously except attack (play once)</li>
     *   <li>Animation names use full format: "animation.crow.idle", etc.</li>
     * </ul>
     *
     * <p><b>Animation List:</b>
     * <ul>
     *   <li>{@code animation.crow.idle} - Subtle movements (2.0s loop)</li>
     *   <li>{@code animation.crow.walk} - Ground walking (1.0s loop)</li>
     *   <li>{@code animation.crow.run} - Fast hopping (0.6s loop)</li>
     *   <li>{@code animation.crow.fly} - Wing flapping (0.8s loop)</li>
     *   <li>{@code animation.crow.sit} - Resting pose (1.0s loop)</li>
     *   <li>{@code animation.crow.attack} - Pecking (0.5s play once)</li>
     * </ul>
     *
     * <p>GeckoLib loads this file once and caches it. All crow entities share the
     * same animation definitions for consistency and performance.
     *
     * @param entity the crow entity being rendered (not used - same animations for all crows)
     * @return the resource identifier for the crow animation file
     */
    @Override
    public Identifier getAnimationResource(CrowEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/crow.animation.json");
    }
}
