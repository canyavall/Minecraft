package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.TestAnimalModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib entity renderer for the Test Animal entity.
 *
 * <p>This renderer integrates with Minecraft's entity rendering system to display
 * animated 3D models using GeckoLib. It handles model rendering, texture application,
 * animation playback, and shadow rendering.
 *
 * <h2>Rendering Pipeline:</h2>
 * <ol>
 *   <li>Minecraft's render system calls this renderer every frame</li>
 *   <li>Renderer retrieves model from {@link TestAnimalModel}</li>
 *   <li>Model provides geometry, texture, and animation locations</li>
 *   <li>GeckoLib processes animations and updates bone transformations</li>
 *   <li>Final model is rendered with correct texture and shadow</li>
 * </ol>
 *
 * <h2>Registration:</h2>
 * <p>This renderer must be registered in {@link com.canya.xeenaa_alexs_mobs.client.AlexsMobsClient}
 * using {@code EntityRendererRegistry.register()}:
 * <pre>{@code
 * EntityRendererRegistry.register(ModEntities.TEST_ANIMAL, TestAnimalRenderer::new);
 * }</pre>
 *
 * <h2>Features:</h2>
 * <ul>
 *   <li><b>Animated Model:</b> Plays idle/walk animations via GeckoLib controller</li>
 *   <li><b>Dynamic Texture:</b> Supports variant textures (if implemented in model)</li>
 *   <li><b>Shadow Rendering:</b> Automatic shadow beneath entity</li>
 *   <li><b>LOD Support:</b> GeckoLib handles level-of-detail automatically</li>
 *   <li><b>Culling:</b> Renderer skips rendering when entity is off-screen</li>
 * </ul>
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Renderer is created once and reused for all entities of this type</li>
 *   <li>Model instance is shared across all entities (stateless)</li>
 *   <li>Animation cache is per-entity (stored in {@link TestAnimalEntity})</li>
 *   <li>Texture is cached by Minecraft's texture manager</li>
 * </ul>
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>client-side only</b> and is NOT thread-safe. All methods are called
 * from the client render thread. Do not access this renderer from other threads.
 *
 * <h2>Customization:</h2>
 * <p>To add custom rendering features (e.g., glowing eyes, hurt overlay, saddle), override:
 * <ul>
 *   <li>{@code getRenderType()} - Custom render layers</li>
 *   <li>{@code applyRotations()} - Custom entity orientation</li>
 *   <li>{@code getTextureLocation()} - Dynamic texture selection</li>
 * </ul>
 *
 * @author Canya
 * @version 0.1.0
 * @see TestAnimalEntity
 * @see TestAnimalModel
 * @see GeoEntityRenderer
 */
@Environment(EnvType.CLIENT)
public class TestAnimalRenderer extends GeoEntityRenderer<TestAnimalEntity> {

    /**
     * Constructs a new Test Animal renderer.
     *
     * <p>This constructor is called by Fabric's {@code EntityRendererRegistry} during
     * client initialization. It receives an {@link EntityRendererFactory.Context} that
     * provides access to the render dispatcher, texture manager, and model loaders.
     *
     * <p>The constructor passes the context and a new {@link TestAnimalModel} instance
     * to the parent {@link GeoEntityRenderer} constructor, which sets up the rendering
     * pipeline for this entity type.
     *
     * <p><b>Model Sharing:</b> The {@link TestAnimalModel} instance created here is
     * stateless and shared across all Test Animal entities. Each entity maintains its own
     * animation state in {@link TestAnimalEntity#getAnimatableInstanceCache()}.
     *
     * <p><b>Registration Example:</b>
     * <pre>{@code
     * // In AlexsMobsClient.registerEntityRenderers():
     * EntityRendererRegistry.register(ModEntities.TEST_ANIMAL, TestAnimalRenderer::new);
     * }</pre>
     *
     * @param context the entity renderer factory context providing access to render resources
     * @throws NullPointerException if context is null
     */
    public TestAnimalRenderer(EntityRendererFactory.Context context) {
        super(context, new TestAnimalModel());
    }

    /**
     * Gets the texture location for rendering this entity.
     *
     * <p>This method is called by GeckoLib's rendering system to determine which texture
     * to apply to the entity model. It delegates to {@link TestAnimalModel#getTextureResource(TestAnimalEntity)}
     * to allow for dynamic texture selection based on entity state.
     *
     * <p><b>Current Implementation:</b> Returns a static green texture for all Test Animal
     * entities. This is suitable for testing visibility and model rendering.
     *
     * <p><b>Dynamic Textures (Future):</b> This method could be overridden to support variants:
     * <pre>{@code
     * @Override
     * public Identifier getTextureLocation(TestAnimalEntity entity) {
     *     if (entity.isBaby()) {
     *         return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal_baby.png");
     *     }
     *     if (entity.hasCustomName() && entity.getCustomName().getString().equals("Rainbow")) {
     *         return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal_rainbow.png");
     *     }
     *     return super.getTextureLocation(entity);
     * }
     * }</pre>
     *
     * <p><b>Texture Caching:</b> Minecraft caches loaded textures, so this method can be
     * called frequently without performance concerns. The same texture identifier always
     * returns the same cached texture instance.
     *
     * @param entity the entity being rendered
     * @return identifier pointing to the texture PNG file
     * @throws NullPointerException if entity is null
     */
    @Override
    public Identifier getTextureLocation(TestAnimalEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/test_animal.png");
    }
}
