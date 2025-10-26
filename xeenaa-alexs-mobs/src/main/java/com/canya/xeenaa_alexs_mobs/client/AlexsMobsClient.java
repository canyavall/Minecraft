package com.canya.xeenaa_alexs_mobs.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side initializer for Xeenaa's Alex's Mobs (Fabric).
 *
 * <p>This class handles all client-only initialization including:
 * <ul>
 *   <li>Entity renderer registration using {@link EntityRendererRegistry}</li>
 *   <li>GeckoLib model and animation registration</li>
 *   <li>Client-side event handlers</li>
 *   <li>Custom resource loading (textures, sounds)</li>
 * </ul>
 *
 * <p><strong>Thread Safety:</strong> This class is initialized on the client render thread
 * during game startup. All registrations must complete before the game finishes loading.
 *
 * <p><strong>Environment:</strong> This class is only loaded in the client environment.
 * Methods annotated with {@code @Environment(EnvType.CLIENT)} are stripped from server builds.
 *
 * @author Canya
 * @version 0.1.0
 * @see EntityRendererRegistry
 * @see net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
 */
@Environment(EnvType.CLIENT)
public class AlexsMobsClient implements ClientModInitializer {

    /**
     * Logger for client-side operations.
     * Prefixed with "client" to distinguish from server-side logs.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("xeenaa-alexs-mobs-client");

    /**
     * Initializes client-side mod components.
     *
     * <p>Called by Fabric Loader during client initialization phase, after common initialization
     * but before the game window opens. This is the correct place to register entity renderers,
     * model layers, and other client-only resources.
     *
     * <p><strong>Execution Order:</strong>
     * <ol>
     *   <li>AlexsMobsMod.onInitialize() (common)</li>
     *   <li><strong>AlexsMobsClient.onInitializeClient() (this method)</strong></li>
     *   <li>Game window opens</li>
     *   <li>Resource packs loaded</li>
     * </ol>
     *
     * <p><strong>Performance:</strong> Keep initialization fast (< 100ms). Heavy operations
     * should be deferred to resource reload events or lazy initialization.
     */
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Alex's Mobs client");

        registerEntityRenderers();

        LOGGER.info("Alex's Mobs client initialization complete");
    }

    /**
     * Registers entity renderers for all custom entities.
     *
     * <p>This method uses {@link EntityRendererRegistry#register(EntityType, EntityRendererFactory)}
     * to associate each entity type with its corresponding renderer. Renderers must implement
     * GeckoLib's {@code GeoEntityRenderer} for animated models.
     *
     * <p><strong>Pattern:</strong> Entity types are registered in {@code ModEntities} (common),
     * but renderers must be registered here (client-only) to avoid class loading issues on servers.
     *
     * <p><strong>Example:</strong>
     * <pre>{@code
     * // For a Grizzly Bear entity:
     * EntityRendererRegistry.register(ModEntities.GRIZZLY_BEAR, GrizzlyBearRenderer::new);
     *
     * // The renderer constructor receives an EntityRendererFactory.Context:
     * public class GrizzlyBearRenderer extends GeoEntityRenderer<GrizzlyBearEntity> {
     *     public GrizzlyBearRenderer(EntityRendererFactory.Context context) {
     *         super(context, new GrizzlyBearModel());
     *     }
     * }
     * }</pre>
     *
     * <p><strong>Thread Safety:</strong> This method is called once during initialization.
     * EntityRendererRegistry is thread-safe, but renderers should not be registered elsewhere.
     *
     * @see EntityRendererRegistry#register(EntityType, EntityRendererFactory)
     * @see software.bernie.geckolib.renderer.GeoEntityRenderer
     */
    @Environment(EnvType.CLIENT)
    private void registerEntityRenderers() {
        LOGGER.debug("Registering entity renderers");

        // Register test animal renderer for framework validation
        registerEntityRenderer(
            com.canya.xeenaa_alexs_mobs.registry.ModEntities.TEST_ANIMAL,
            com.canya.xeenaa_alexs_mobs.client.renderer.TestAnimalRenderer::new
        );

        // Register Fly renderer (Epic 03 - Proof of Concept)
        registerEntityRenderer(
            com.canya.xeenaa_alexs_mobs.registry.ModEntities.FLY,
            com.canya.xeenaa_alexs_mobs.client.renderer.FlyRenderer::new
        );

        // Future entity renderers will be registered here
        // Example pattern for each entity:
        // EntityRendererRegistry.register(ModEntities.GRIZZLY_BEAR, GrizzlyBearRenderer::new);
        // EntityRendererRegistry.register(ModEntities.ROADRUNNER, RoadrunnerRenderer::new);
        // EntityRendererRegistry.register(ModEntities.CROCODILE, CrocodileRenderer::new);

        LOGGER.debug("Entity renderer registration complete (2 renderers registered)");
    }

    /**
     * Registers a single entity renderer.
     *
     * <p>Convenience method for registering entity renderers with consistent logging and error handling.
     * Use this method instead of calling {@code EntityRendererRegistry.register()} directly.
     *
     * <p><strong>Example Usage:</strong>
     * <pre>{@code
     * registerEntityRenderer(ModEntities.GRIZZLY_BEAR, GrizzlyBearRenderer::new);
     * registerEntityRenderer(ModEntities.ROADRUNNER, RoadrunnerRenderer::new);
     * }</pre>
     *
     * <p><strong>Error Handling:</strong> If renderer registration fails (e.g., due to missing model files),
     * this method logs an error but does not crash the game. The entity will render as a missing model instead.
     *
     * @param <T> the entity type
     * @param entityType the entity type to register a renderer for
     * @param rendererFactory factory function that creates the renderer
     * @throws NullPointerException if entityType or rendererFactory is null
     */
    @Environment(EnvType.CLIENT)
    private static <T extends Entity> void registerEntityRenderer(
            EntityType<? extends T> entityType,
            EntityRendererFactory<T> rendererFactory) {

        if (entityType == null || rendererFactory == null) {
            throw new NullPointerException("Entity type and renderer factory must not be null");
        }

        try {
            EntityRendererRegistry.register(entityType, rendererFactory);
            LOGGER.debug("Registered renderer for entity: {}",
                entityType.getUntranslatedName());
        } catch (Exception e) {
            LOGGER.error("Failed to register renderer for entity: {}",
                entityType.getUntranslatedName(), e);
        }
    }
}
