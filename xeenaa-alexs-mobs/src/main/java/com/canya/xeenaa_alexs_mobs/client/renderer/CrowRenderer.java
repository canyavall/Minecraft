package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.CrowModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.CrowEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
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

        // COMPREHENSIVE LOGGING - Log every 10 ticks (0.5 seconds) for better tracking
        if (entity.age % 10 == 0) {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║      CROW POSITIONING DEBUG - Frame " + entity.age + "           ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

            // === POSITION ===
            System.out.println("\n[WORLD POSITION]");
            System.out.println("  X: " + String.format("%.3f", entity.getX()));
            System.out.println("  Y: " + String.format("%.3f", entity.getY()));
            System.out.println("  Z: " + String.format("%.3f", entity.getZ()));
            System.out.println("  OnGround: " + entity.isOnGround());

            // === MODEL BONE STRUCTURE (from crow.geo.json) ===
            System.out.println("\n[MODEL BONE ROTATIONS - Baked in geo.json]");
            System.out.println("  Body:   57.5° X-rotation (tilted forward)");
            System.out.println("  Legs:   32.5° X-rotation (angled back)");
            System.out.println("  Tail:   -7.5° X-rotation (slightly down)");
            System.out.println("  Wings:  2.5° X-rotation (nearly flat)");
            System.out.println("  → Problem: Body tilt causes 'laying on belly' appearance");

            // === CURRENT RENDERER TRANSFORMATIONS ===
            System.out.println("\n[CURRENT RENDERER TRANSFORMS]");
            System.out.println("  1. Scale: 0.8x (reduces size)");
            System.out.println("  2. Y-Rotation: 180° (fixes backwards flight)");
            System.out.println("  3. X-Rotation: TESTING... (trying to fix vertical orientation)");

            // === ROTATION TESTING ===
            System.out.println("\n[ROTATION FIX PROGRESS]");
            System.out.println("  ✅ Y-Rotation: 180° - Fixes backwards flight (WORKING)");
            System.out.println("  ✅ X-Rotation: -57.5° - Fixes laying on belly (WORKING)");
            System.out.println("  🔧 Z-Rotation: TESTING - Fix left tilt/diagonal flight");

            // === DIAGONAL FLIGHT ISSUE ===
            System.out.println("\n[CURRENT ISSUE: LEFT TILT]");
            System.out.println("  Problem: Crow tilted to left, flies diagonally");
            System.out.println("  Cause: Model may have roll offset or combined rotations");
            System.out.println("  Testing: Z-axis rotation to level out the crow");

            // === HEAD & TAIL POSITIONING ===
            System.out.println("\n[BODY ALIGNMENT]");
            System.out.println("  Goal: Level crow horizontally");
            System.out.println("  Expected: No left/right tilt when flying");
            System.out.println("  Testing angles: 0°, ±15°, ±30°, ±45°");

            // === VELOCITY (for context) ===
            System.out.println("\n[VELOCITY]");
            System.out.println("  Y: " + String.format("%.3f", entity.getVelocity().y) +
                             (entity.getVelocity().y > 0 ? " ↑" : entity.getVelocity().y < 0 ? " ↓" : " →"));
            System.out.println("  Speed: " + String.format("%.3f", entity.getVelocity().length()));

            System.out.println("\n" + "═".repeat(60));
            System.out.println("ROTATION ORDER: Y (180°) → X (-57.5°) → Z (testing)");
            System.out.println("If still tilted, try different Z angles in increments of 15°");
            System.out.println("═".repeat(60) + "\n");
        }

        // Scale the model to appropriate size (crows are medium-sized birds)
        poseStack.scale(0.8f, 0.8f, 0.8f);

        // REVERT: Remove all custom rotations
        // Problem: Crow rotating around feet pivot (like clock) instead of body center
        // Solution: Let GeckoLib handle rotations naturally with its bone system
        // The model's bones should handle orientation, not renderer transforms

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
