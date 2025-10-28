package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.TriopsModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.TriopsEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for Triops entity.
 */
public class TriopsRenderer extends GeoEntityRenderer<TriopsEntity> {
    public TriopsRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new TriopsModel());
        this.shadowRadius = 0.2f;
    }

    @Override
    public Identifier getTextureLocation(TriopsEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/triops/triops.png");
    }

    @Override
    public void render(TriopsEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();

        // Lower the model to ground level (Citadel uses Y=24 as base, we need Y=0)
        // Adjusted to -1.3 to prevent ground clipping
        poseStack.translate(0, -1.3, 0);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
