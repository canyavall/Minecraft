package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model for the Test Animal entity.
 *
 * <p>This class provides resource locations for the entity's geometry, texture,
 * and animation files. GeckoLib uses these locations to load and render the entity
 * with animated 3D models.
 *
 * <h2>Resource Files:</h2>
 * <ul>
 *   <li><b>Geometry:</b> {@code assets/xeenaa-alexs-mobs/geo/test_animal.geo.json}</li>
 *   <li><b>Texture:</b> {@code assets/xeenaa-alexs-mobs/textures/entity/test_animal.png}</li>
 *   <li><b>Animations:</b> {@code assets/xeenaa-alexs-mobs/animations/test_animal.animation.json}</li>
 * </ul>
 *
 * <h2>Model Structure:</h2>
 * <p>The {@code .geo.json} file defines a simple cube model (1×1×1 blocks) for testing purposes.
 * In production, this would be exported from Blockbench with detailed geometry and bone structure.
 *
 * <h2>Animation System:</h2>
 * <p>The {@code .animation.json} file contains two basic animations:
 * <ul>
 *   <li><b>idle:</b> Subtle breathing animation when stationary</li>
 *   <li><b>walk:</b> Walking animation when the entity is moving</li>
 * </ul>
 *
 * <h2>Texture Mapping:</h2>
 * <p>The texture is a simple 16×16 pixel green square for visibility during testing.
 * Production entities would use detailed texture maps with UV coordinates defined in the model.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>client-side only</b> and is NOT thread-safe. All methods are called
 * from the client render thread by GeckoLib's rendering system.
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // In TestAnimalRenderer constructor:
 * public TestAnimalRenderer(EntityRendererFactory.Context context) {
 *     super(context, new TestAnimalModel());
 * }
 * }</pre>
 *
 * @author Canya
 * @version 0.1.0
 * @see TestAnimalEntity
 * @see com.canya.xeenaa_alexs_mobs.client.renderer.TestAnimalRenderer
 * @see GeoModel
 */
@Environment(EnvType.CLIENT)
public class TestAnimalModel extends GeoModel<TestAnimalEntity> {

    /**
     * Gets the resource location for the entity's geometry model.
     *
     * <p>This method is called by GeckoLib to load the {@code .geo.json} file
     * that defines the entity's 3D structure, bones, and UV mapping.
     *
     * <p><b>Expected File:</b> {@code assets/xeenaa-alexs-mobs/geo/test_animal.geo.json}
     *
     * <p><b>File Format:</b> Bedrock Edition geometry format (exported from Blockbench or
     * created programmatically for simple models).
     *
     * @param entity the entity being rendered (unused in this implementation,
     *               but could be used for variant models)
     * @return identifier pointing to the geometry JSON file
     */
    @Override
    public Identifier getModelResource(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/test_animal.geo.json");
    }

    /**
     * Gets the resource location for the entity's texture.
     *
     * <p>This method is called by GeckoLib to load the texture image that will
     * be applied to the entity's model geometry.
     *
     * <p><b>Expected File:</b> {@code assets/xeenaa-alexs-mobs/textures/entity/test_animal.png}
     *
     * <p><b>Texture Size:</b> 16×16 pixels (green square for testing visibility)
     *
     * <p><b>Production Note:</b> Real entities would use larger textures (e.g., 64×64, 128×128)
     * with detailed artwork and proper UV mapping to the model's geometry.
     *
     * <p><b>Dynamic Textures:</b> The entity parameter could be used to return different
     * textures based on variants, age, or other properties:
     * <pre>{@code
     * // Example for variants:
     * if (entity.getVariant() == Variant.ALBINO) {
     *     return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal_albino.png");
     * }
     * }</pre>
     *
     * @param entity the entity being rendered (unused in this basic implementation)
     * @return identifier pointing to the texture PNG file
     */
    @Override
    public Identifier getTextureResource(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal.png");
    }

    /**
     * Gets the resource location for the entity's animation definitions.
     *
     * <p>This method is called by GeckoLib to load the {@code .animation.json} file
     * that defines all animations for this entity (idle, walk, attack, etc.).
     *
     * <p><b>Expected File:</b> {@code assets/xeenaa-alexs-mobs/animations/test_animal.animation.json}
     *
     * <p><b>Animation List:</b>
     * <ul>
     *   <li>{@code idle} - Plays when entity is stationary</li>
     *   <li>{@code walk} - Plays when entity is moving</li>
     * </ul>
     *
     * <p><b>Animation Format:</b> Bedrock Edition animation format with keyframes,
     * rotation/position/scale transformations per bone, and easing functions.
     *
     * <p><b>Controller Integration:</b> Animations defined in this file are referenced
     * by name in {@link TestAnimalEntity#predicate(software.bernie.geckolib.animation.AnimationState)}.
     *
     * @param entity the entity being rendered (unused in this implementation)
     * @return identifier pointing to the animation JSON file
     */
    @Override
    public Identifier getAnimationResource(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/test_animal.animation.json");
    }
}
