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
            System.out.println("║           CROW RENDER DEBUG - Frame " + entity.age + "              ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

            // === ENTITY POSITION ===
            System.out.println("\n[POSITION]");
            System.out.println("  World: X=" + String.format("%.3f", entity.getX()) +
                             " Y=" + String.format("%.3f", entity.getY()) +
                             " Z=" + String.format("%.3f", entity.getZ()));
            System.out.println("  OnGround: " + entity.isOnGround());

            // === ENTITY ROTATION ===
            System.out.println("\n[ROTATION - Entity Values]");
            System.out.println("  Pitch: " + String.format("%.2f", entity.getPitch()) + "° (up/down tilt)");
            System.out.println("  Yaw: " + String.format("%.2f", entity.getYaw()) + "° (facing direction)");
            System.out.println("  Render Yaw: " + String.format("%.2f", entityYaw) + "° (interpolated)");
            System.out.println("  Body Yaw: " + String.format("%.2f", entity.bodyYaw) + "°");
            System.out.println("  Head Yaw: " + String.format("%.2f", entity.headYaw) + "°");
            System.out.println("  Prev Yaw: " + String.format("%.2f", entity.prevYaw) + "°");

            // === VELOCITY ===
            System.out.println("\n[VELOCITY]");
            System.out.println("  X: " + String.format("%.4f", entity.getVelocity().x));
            System.out.println("  Y: " + String.format("%.4f", entity.getVelocity().y) +
                             (entity.getVelocity().y > 0 ? " (rising)" : entity.getVelocity().y < 0 ? " (falling)" : " (level)"));
            System.out.println("  Z: " + String.format("%.4f", entity.getVelocity().z));
            System.out.println("  Speed: " + String.format("%.4f", Math.sqrt(
                entity.getVelocity().x * entity.getVelocity().x +
                entity.getVelocity().y * entity.getVelocity().y +
                entity.getVelocity().z * entity.getVelocity().z)));

            // === ANIMATION STATE ===
            System.out.println("\n[ANIMATION STATE]");
            System.out.println("  Age: " + entity.age + " ticks");
            System.out.println("  Flying: " + !entity.isOnGround());
            System.out.println("  Moving: " + (entity.getVelocity().lengthSquared() > 0.001));

            // === CALCULATED ANGLES ===
            System.out.println("\n[CALCULATED ANGLES]");
            double horizontalSpeed = Math.sqrt(
                entity.getVelocity().x * entity.getVelocity().x +
                entity.getVelocity().z * entity.getVelocity().z);
            double totalSpeed = entity.getVelocity().length();
            double calculatedPitch = totalSpeed > 0.001 ?
                Math.toDegrees(Math.atan2(entity.getVelocity().y, horizontalSpeed)) : 0;
            System.out.println("  Calculated Pitch (from velocity): " + String.format("%.2f", calculatedPitch) + "°");
            System.out.println("  Yaw Change (from prev): " + String.format("%.2f", entity.getYaw() - entity.prevYaw) + "°");

            // === RENDERER TRANSFORMATIONS ===
            System.out.println("\n[RENDERER TRANSFORMATIONS]");
            System.out.println("  1. Scale: 0.8x (all axes)");
            System.out.println("  2. Rotations: NONE (testing natural)");
            System.out.println("  Note: Model has baked 57.5° X-rotation in geo.json");

            // === VISUAL EXPECTATIONS ===
            System.out.println("\n[EXPECTED VISUAL]");
            if (!entity.isOnGround()) {
                System.out.println("  Should be: Flying horizontally (laying flat)");
                System.out.println("  Turning: Entity yaw should rotate model around vertical axis");
            } else {
                System.out.println("  Should be: On ground (maybe walking)");
            }

            System.out.println("\n" + "═".repeat(60) + "\n");
        }

        // Scale the model to appropriate size (crows are medium-sized birds)
        poseStack.scale(0.8f, 0.8f, 0.8f);

        // Y-axis 180° fixes backwards flying (CONFIRMED WORKING)
        // Issue: Legs on top, head on bottom (needs additional rotation)
        poseStack.multiply(new Quaternionf().rotateY((float) Math.PI));

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
