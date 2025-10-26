package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.FlyModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib entity renderer for the Fly entity.
 *
 * <p>This renderer integrates with Minecraft's entity rendering system to display
 * animated 3D models using GeckoLib. It handles model rendering, texture application,
 * animation playback, and shadow rendering for the tiny flying insect.
 *
 * <h2>Rendering Pipeline:</h2>
 * <ol>
 *   <li>Minecraft's render system calls this renderer every frame</li>
 *   <li>Renderer retrieves model from {@link FlyModel}</li>
 *   <li>Model provides geometry, texture, and animation locations</li>
 *   <li>GeckoLib processes animations and updates bone transformations</li>
 *   <li>Final model is rendered with correct texture and shadow</li>
 * </ol>
 *
 * <h2>Entity Specifications:</h2>
 * <p>The fly is rendered as a tiny insect with the following characteristics:
 * <ul>
 *   <li><b>Size</b>: 0.3×0.2×0.3 blocks (extremely small)</li>
 *   <li><b>Model</b>: 12 cubes (body, head, 2 wings, 6 legs, 2 antennae)</li>
 *   <li><b>Animations</b>:
 *       <ul>
 *         <li><b>Idle</b>: Wings at rest, antennae twitching (when landed)</li>
 *         <li><b>Fly</b>: Rapid wing flapping, body bobbing (when airborne)</li>
 *         <li><b>Death</b>: Wings freeze, body tumbles/falls (when killed)</li>
 *       </ul>
 *   </li>
 *   <li><b>Texture</b>: 16×16 or 32×32 pixels
 *       <ul>
 *         <li>Black/grey segmented body</li>
 *         <li>Bright red compound eyes on head</li>
 *         <li>Semi-transparent wings with subtle veins</li>
 *         <li>Black thin legs and antennae</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <h2>Registration:</h2>
 * <p>This renderer must be registered in {@link com.canya.xeenaa_alexs_mobs.client.AlexsMobsClient}
 * using {@code EntityRendererRegistry.register()}:
 * <pre>{@code
 * EntityRendererRegistry.register(ModEntities.FLY, FlyRenderer::new);
 * }</pre>
 *
 * <h2>Features:</h2>
 * <ul>
 *   <li><b>Animated Model:</b> Plays idle/fly/death animations via GeckoLib controller</li>
 *   <li><b>Dynamic Animation:</b> Switches between animations based on entity state:
 *       <ul>
 *         <li>Idle when landed on ground ({@link FlyEntity#isOnGround()})</li>
 *         <li>Fly when airborne and moving ({@link FlyEntity#isMoving()})</li>
 *         <li>Death when entity is dead ({@link FlyEntity#isDead()})</li>
 *       </ul>
 *   </li>
 *   <li><b>Texture Mapping:</b> Single static texture for all flies (no variants currently)</li>
 *   <li><b>Shadow Rendering:</b> Automatic shadow beneath entity (scaled to tiny size)</li>
 *   <li><b>LOD Support:</b> GeckoLib handles level-of-detail automatically</li>
 *   <li><b>Culling:</b> Renderer skips rendering when entity is off-screen</li>
 * </ul>
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Renderer is created once and reused for all fly entities of this type</li>
 *   <li>Model instance is shared across all fly entities (stateless)</li>
 *   <li>Animation cache is per-entity (stored in {@link FlyEntity#getAnimatableInstanceCache()})</li>
 *   <li>Texture is cached by Minecraft's texture manager (loaded once, reused)</li>
 *   <li>Tiny model size (12 cubes) means minimal vertex processing overhead</li>
 *   <li>Fast animation transitions (0 ticks) for responsive state changes</li>
 * </ul>
 *
 * <p><b>Performance Note:</b> Flies are designed to spawn in large numbers as ambient mobs.
 * The lightweight model and efficient GeckoLib caching ensure minimal performance impact
 * even with dozens of flies rendered simultaneously.
 *
 * <h2>Thread Safety:</h2>
 * <p>This class is <b>client-side only</b> and is NOT thread-safe. All methods are called
 * from the client render thread. Do not access this renderer from other threads.
 *
 * <h2>Customization:</h2>
 * <p>To add custom rendering features (e.g., glowing eyes, variant textures, seasonal skins),
 * override:
 * <ul>
 *   <li>{@code getRenderType()} - Custom render layers (e.g., emissive eyes)</li>
 *   <li>{@code applyRotations()} - Custom entity orientation (e.g., bank during turns)</li>
 *   <li>{@code getTextureLocation()} - Dynamic texture selection (e.g., golden fly variant)</li>
 *   <li>{@code preRender()} - Pre-render modifications (e.g., scale based on age)</li>
 * </ul>
 *
 * <p><b>Example: Golden Fly Variant</b>
 * <pre>{@code
 * @Override
 * public Identifier getTextureLocation(FlyEntity entity) {
 *     if (entity.getDataTracker().get(VARIANT) == Variant.GOLDEN) {
 *         return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly_golden.png");
 *     }
 *     return super.getTextureLocation(entity);
 * }
 * }</pre>
 *
 * @author Canya
 * @version 0.1.0
 * @see FlyEntity
 * @see FlyModel
 * @see GeoEntityRenderer
 */
@Environment(EnvType.CLIENT)
public class FlyRenderer extends GeoEntityRenderer<FlyEntity> {

    /**
     * Constructs a new Fly renderer.
     *
     * <p>This constructor is called by Fabric's {@code EntityRendererRegistry} during
     * client initialization. It receives an {@link EntityRendererFactory.Context} that
     * provides access to the render dispatcher, texture manager, and model loaders.
     *
     * <p>The constructor passes the context and a new {@link FlyModel} instance
     * to the parent {@link GeoEntityRenderer} constructor, which sets up the rendering
     * pipeline for this entity type.
     *
     * <p><b>Model Sharing:</b> The {@link FlyModel} instance created here is
     * stateless and shared across all Fly entities. Each entity maintains its own
     * animation state in {@link FlyEntity#getAnimatableInstanceCache()}. This design
     * ensures memory efficiency when rendering large numbers of flies.
     *
     * <p><b>Renderer Lifecycle:</b> This constructor is called ONCE during game startup
     * (specifically during client resource loading). The same renderer instance is reused
     * for all fly entities throughout the game session.
     *
     * <p><b>Registration Example:</b>
     * <pre>{@code
     * // In AlexsMobsClient.registerEntityRenderers():
     * EntityRendererRegistry.register(ModEntities.FLY, FlyRenderer::new);
     * }</pre>
     *
     * @param context the entity renderer factory context providing access to render resources
     * @throws NullPointerException if context is null
     */
    public FlyRenderer(EntityRendererFactory.Context context) {
        super(context, new FlyModel());
    }

    /**
     * Gets the texture location for rendering this entity.
     *
     * <p>This method is called by GeckoLib's rendering system to determine which texture
     * to apply to the entity model. It delegates to {@link FlyModel#getTextureResource(FlyEntity)}
     * to allow for dynamic texture selection based on entity state.
     *
     * <p><b>Current Implementation:</b> Returns a static fly texture for all Fly entities.
     * The texture features:
     * <ul>
     *   <li>Black/dark grey segmented body (thorax and abdomen)</li>
     *   <li>Bright red compound eyes on head (2 red pixels)</li>
     *   <li>Semi-transparent wings with subtle vein patterns</li>
     *   <li>Black thin legs (6 total - 3 pairs)</li>
     *   <li>Black antennae (2 curved segments)</li>
     * </ul>
     *
     * <p><b>Dynamic Textures (Future):</b> This method could be overridden to support variants:
     * <pre>{@code
     * @Override
     * public Identifier getTextureLocation(FlyEntity entity) {
     *     // Seasonal variant (winter flies are slightly blue-tinted)
     *     if (entity.getWorld().getBiome(entity.getBlockPos()).isIn(BiomeTags.IS_COLD)) {
     *         return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly_winter.png");
     *     }
     *
     *     // Golden fly (rare spawn, 1% chance)
     *     if (entity.getDataTracker().get(VARIANT) == Variant.GOLDEN) {
     *         return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly_golden.png");
     *     }
     *
     *     // Diseased fly (spawns near rotten flesh)
     *     if (entity.hasStatusEffect(StatusEffects.POISON)) {
     *         return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly_diseased.png");
     *     }
     *
     *     return super.getTextureLocation(entity);
     * }
     * }</pre>
     *
     * <p><b>Texture Caching:</b> Minecraft caches loaded textures, so this method can be
     * called frequently (every frame) without performance concerns. The same texture identifier
     * always returns the same cached texture instance.
     *
     * <p><b>Performance Note:</b> This method is called during every render frame for each
     * visible fly entity. Keep logic simple (field lookups only) to avoid render thread lag.
     *
     * @param entity the entity being rendered
     * @return identifier pointing to the texture PNG file
     * @throws NullPointerException if entity is null
     */
    @Override
    public Identifier getTextureLocation(FlyEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/fly.png");
    }
}
