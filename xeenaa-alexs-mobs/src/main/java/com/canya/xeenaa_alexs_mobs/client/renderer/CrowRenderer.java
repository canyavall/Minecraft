package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.CrowModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.CrowEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for the Crow entity.
 *
 * <p>This class handles rendering the crow entity in the game world using GeckoLib's
 * animation and rendering system. It extends {@link GeoEntityRenderer} to provide
 * 3D model rendering with animations, textures, and shadows.
 *
 * <h2>Rendering Features:</h2>
 * <ul>
 *   <li><b>3D Model</b>: Renders crow geometry from {@link CrowModel}</li>
 *   <li><b>Animations</b>: Plays GeckoLib animations (idle, walk, run, fly, sit, attack)</li>
 *   <li><b>Textures</b>: Applies crow texture (black corvid with gray highlights)</li>
 *   <li><b>Shadow</b>: Renders circular shadow beneath crow (0.3f radius)</li>
 *   <li><b>Lighting</b>: Applies world lighting to model (dynamic shading)</li>
 * </ul>
 *
 * <h2>Shadow Configuration:</h2>
 * <p>The crow uses a shadow radius of 0.3f, which creates a medium-sized shadow
 * appropriate for a bird of its size. For comparison:
 * <ul>
 *   <li>Chicken: 0.3f (same size as crow)</li>
 *   <li>Parrot: 0.25f (smaller bird)</li>
 *   <li>Rabbit: 0.4f (larger ground animal)</li>
 * </ul>
 *
 * <p>The shadow radius can be adjusted if the crow appears too small or too large
 * relative to other mobs. Testing in-game will determine if 0.3f is optimal.
 *
 * <h2>Transformations:</h2>
 * <p>This renderer applies <b>NO custom transformations</b> initially. The model's
 * pivot points and bone rotations (defined in crow.geo.json) should position the
 * crow correctly by default.
 *
 * <p>If the crow appears incorrectly positioned (e.g., floating above ground or
 * clipping into ground), transformations can be added via override:
 * <pre>{@code
 * @Override
 * public void render(CrowEntity entity, float entityYaw, float partialTick,
 *                   MatrixStack poseStack, VertexConsumerProvider bufferSource,
 *                   int packedLight) {
 *     // Adjust Y position if model is floating/clipping
 *     poseStack.translate(0.0D, -0.1D, 0.0D); // Lower by 0.1 blocks
 *     super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
 * }
 * }</pre>
 *
 * <h2>Render Layers:</h2>
 * <p>The crow uses standard GeckoLib rendering layers:
 * <ul>
 *   <li><b>Base Layer</b>: Main model with texture</li>
 *   <li><b>Shadow Layer</b>: Circular shadow (automatic)</li>
 *   <li><b>Outline Layer</b>: Entity outline when glowing (automatic)</li>
 * </ul>
 *
 * <p><b>Future Enhancement:</b> Additional render layers could be added for:
 * <ul>
 *   <li>Eyes layer (glowing eyes in dark)</li>
 *   <li>Emissive layer (glowing elements)</li>
 *   <li>Overlay layer (hurt tint, freezing effect)</li>
 * </ul>
 *
 * <h2>Registration:</h2>
 * <p>This renderer must be registered in the client initializer:
 * <pre>{@code
 * // In AlexsMobsClient.java or similar
 * EntityRendererRegistry.register(ModEntities.CROW, CrowRenderer::new);
 * }</pre>
 *
 * <p>Registration links the crow entity type to this renderer, telling Minecraft
 * to use {@link CrowRenderer} whenever a crow entity needs to be drawn.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>NOT thread-safe</b>. Renderers are created and used on the
 * client rendering thread only. Do not call rendering methods from other threads.
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>GeckoLib caches model geometry and textures automatically</li>
 *   <li>Animation state is computed every frame - kept lightweight in CrowEntity</li>
 *   <li>Shadow rendering is optimized by Minecraft's entity renderer</li>
 *   <li>No custom transformations means minimal overhead per frame</li>
 * </ul>
 *
 * <h2>Debugging:</h2>
 * <p>If the crow doesn't render correctly, check:
 * <ol>
 *   <li>Asset files exist at paths specified in {@link CrowModel}</li>
 *   <li>Renderer is registered in client initializer</li>
 *   <li>Entity is spawning (check with F3+B hitbox debug)</li>
 *   <li>Texture is valid PNG (check console for texture loading errors)</li>
 *   <li>Model JSON has no syntax errors (GeckoLib will log warnings)</li>
 *   <li>Animation names match between CrowEntity and crow.animation.json</li>
 * </ol>
 *
 * @author Canya
 * @see GeoEntityRenderer
 * @see CrowModel
 * @see CrowEntity
 */
public class CrowRenderer extends GeoEntityRenderer<CrowEntity> {

    /**
     * Constructs a new crow renderer.
     *
     * <p>This constructor is called by Minecraft's entity renderer registry when
     * the client initializes. It sets up the GeckoLib rendering pipeline with the
     * crow's model and configures the shadow radius.
     *
     * <p><b>Constructor Flow:</b>
     * <ol>
     *   <li>Call parent constructor with {@link CrowModel} instance</li>
     *   <li>GeckoLib loads model, texture, and animation assets</li>
     *   <li>Set shadow radius to 0.3f (medium bird size)</li>
     * </ol>
     *
     * <p>The {@link EntityRendererFactory.Context} parameter provides access to:
     * <ul>
     *   <li>Model loader (for loading entity models)</li>
     *   <li>Entity model set (for vanilla model access)</li>
     *   <li>Block render manager (for held blocks)</li>
     *   <li>Item renderer (for held items)</li>
     * </ul>
     *
     * @param renderManager the entity renderer factory context (provided by Minecraft)
     * @throws NullPointerException if renderManager is null
     */
    public CrowRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CrowModel());

        // Shadow radius: 0.3f = medium bird size (same as chicken)
        // Adjust if crow appears incorrectly sized relative to other mobs
        this.shadowRadius = 0.3f;
    }

    /**
     * Gets the texture location for rendering the crow entity.
     *
     * <p>This method is called by GeckoLib every frame to determine which texture
     * to apply to the crow model. It delegates to {@link CrowModel#getTextureResource(CrowEntity)}
     * to maintain consistency between model and renderer.
     *
     * <p><b>Texture Path:</b>
     * {@code assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png}
     *
     * <p><b>Current Implementation:</b> All crows use the same texture (black corvid).
     *
     * <p><b>Future Enhancement:</b> This method could return different textures based
     * on entity state or variants:
     * <pre>{@code
     * @Override
     * public Identifier getTextureLocation(CrowEntity entity) {
     *     if (entity.isBaby()) {
     *         return Identifier.of("xeenaa-alexs-mobs",
     *             "textures/entity/crow/crow_baby.png");
     *     }
     *     if (entity.isAlbino()) {
     *         return Identifier.of("xeenaa-alexs-mobs",
     *             "textures/entity/crow/crow_albino.png");
     *     }
     *     return super.getTextureLocation(entity);
     * }
     * }</pre>
     *
     * <p><b>Performance Note:</b> This method is called every frame during rendering.
     * The Identifier is constant, so no allocation occurs. GeckoLib caches the loaded
     * texture, so file I/O only happens once at startup.
     *
     * @param entity the crow entity being rendered
     * @return the resource identifier for the crow's texture file
     * @see CrowModel#getTextureResource(CrowEntity)
     */
    @Override
    public Identifier getTextureLocation(CrowEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/crow/crow.png");
    }

    /**
     * Custom render method to apply model transformations.
     *
     * <p>The crow model uses Citadel-style coordinates with Y=24 as the base pivot.
     * This method applies scaling and translation to position the crow correctly at
     * ground level (Y=0) and adjust its size to match the intended hitbox.
     *
     * @param entity the crow entity being rendered
     * @param entityYaw the entity's yaw rotation
     * @param partialTick partial tick for smooth interpolation
     * @param poseStack the matrix stack for transformations
     * @param bufferSource the vertex consumer provider
     * @param packedLight the packed light value for lighting
     */
    @Override
    public void render(CrowEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();

        // Scale the model to appropriate size (crows are medium-sized birds)
        // Original Citadel model may need scaling to match hitbox
        poseStack.scale(0.8f, 0.8f, 0.8f);

        // Translate down to ground level (Citadel models use Y=24 as pivot, we need Y=0)
        // Adjusted for the 0.8 scale factor
        poseStack.translate(0, -1.5, 0);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
