/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 */
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelGrizzlyBear;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerGrizzlyHoney;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerGrizzlyItem;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class RenderGrizzlyBear
extends MobRenderer<EntityGrizzlyBear, ModelGrizzlyBear> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear.png");
    private static final ResourceLocation TEXTURE_SNOWY = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear_snowy.png");
    public static final ResourceLocation TEXTURE_FREDDY = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear_freddy.png");
    private static final ResourceLocation TEXTURE_FREDDY_EYES = new ResourceLocation("alexsmobs:textures/entity/grizzly_bear_freddy_eyes.png");

    public RenderGrizzlyBear(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, (EntityModel)new ModelGrizzlyBear(), 0.8f);
        this.m_115326_(new LayerFreddyEyes());
        this.m_115326_(new LayerGrizzlyHoney(this));
        this.m_115326_(new LayerSnow());
        this.m_115326_(new LayerGrizzlyItem(this));
    }

    public boolean shouldRender(EntityGrizzlyBear livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (livingEntityIn.getAprilFoolsFlag() == 5) {
            return false;
        }
        return super.m_5523_((Mob)livingEntityIn, camera, camX, camY, camZ);
    }

    public ResourceLocation getTextureLocation(EntityGrizzlyBear entity) {
        return entity.isFreddy() ? TEXTURE_FREDDY : TEXTURE;
    }

    class LayerFreddyEyes
    extends RenderLayer<EntityGrizzlyBear, ModelGrizzlyBear> {
        public LayerFreddyEyes() {
            super((RenderLayerParent)RenderGrizzlyBear.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGrizzlyBear entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.getAprilFoolsFlag() == 4 && entitylivingbaseIn.f_19797_ % 6 <= 2) {
                VertexConsumer ivertexbuilder = bufferIn.m_6299_(AMRenderTypes.getEyesNoFog(TEXTURE_FREDDY_EYES));
                ((ModelGrizzlyBear)this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.m_115338_((LivingEntity)entitylivingbaseIn, (float)0.0f), 1.0f, 1.0f, 1.0f, 0.1f);
            }
        }
    }

    class LayerSnow
    extends RenderLayer<EntityGrizzlyBear, ModelGrizzlyBear> {
        public LayerSnow() {
            super((RenderLayerParent)RenderGrizzlyBear.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGrizzlyBear entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.isSnowy()) {
                VertexConsumer ivertexbuilder = bufferIn.m_6299_(RenderType.m_110458_((ResourceLocation)TEXTURE_SNOWY));
                ((ModelGrizzlyBear)this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.m_115338_((LivingEntity)entitylivingbaseIn, (float)0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
}

