/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelSoulVulture;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.RenderSoulVulture;
import com.github.alexthe666.alexsmobs.entity.EntitySoulVulture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class LayerSoulVultureGlow
extends RenderLayer<EntitySoulVulture, ModelSoulVulture> {
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexsmobs:textures/entity/soul_vulture/soul_vulture_glow.png");
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("alexsmobs:textures/entity/soul_vulture/soul_vulture_flames_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("alexsmobs:textures/entity/soul_vulture/soul_vulture_flames_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("alexsmobs:textures/entity/soul_vulture/soul_vulture_flames_2.png");

    public LayerSoulVultureGlow(RenderSoulVulture renderSoulVulture) {
        super((RenderLayerParent)renderSoulVulture);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntitySoulVulture entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ((ModelSoulVulture)this.m_117386_()).m_7695_(matrixStackIn, bufferIn.m_6299_(AMRenderTypes.getGhost(TEXTURE_GLOW)), 240, LivingEntityRenderer.m_115338_((LivingEntity)entitylivingbaseIn, (float)0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
        if (entitylivingbaseIn.hasSoulHeart()) {
            ((ModelSoulVulture)this.m_117386_()).m_7695_(matrixStackIn, bufferIn.m_6299_(AMRenderTypes.getGhost(this.getFlames(entitylivingbaseIn.f_19797_))), 240, LivingEntityRenderer.m_115338_((LivingEntity)entitylivingbaseIn, (float)0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private ResourceLocation getFlames(int tickCount) {
        int i = tickCount / 3 % 3;
        return switch (i) {
            case 2 -> TEXTURE_2;
            case 1 -> TEXTURE_1;
            default -> TEXTURE_0;
        };
    }
}

