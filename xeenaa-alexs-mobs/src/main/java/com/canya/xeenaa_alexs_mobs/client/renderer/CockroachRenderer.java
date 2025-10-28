package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.CockroachModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.CockroachEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * GeckoLib renderer for Cockroach entity.
 */
public class CockroachRenderer extends GeoEntityRenderer<CockroachEntity> {
    public CockroachRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CockroachModel());
        this.shadowRadius = 0.2f;
    }

    @Override
    public Identifier getTextureLocation(CockroachEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/cockroach/cockroach.png");
    }

    @Override
    public void render(CockroachEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();

        // Scale down by 37% (to 63% of original size - 30% + additional 10%)
        poseStack.scale(0.63f, 0.63f, 0.63f);

        // Lower the model to ground level (Citadel uses Y=24 as base, we need Y=0)
        // Adjusted for new scale
        poseStack.translate(0, -1.3, 0);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }
}
