/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.client.model.AdvancedEntityModel
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelBison;
import com.github.alexthe666.alexsmobs.client.model.ModelBisonBaby;
import com.github.alexthe666.alexsmobs.entity.EntityBison;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class RenderBison
extends MobRenderer<EntityBison, AdvancedEntityModel<EntityBison>> {
    private static final ResourceLocation TEXTURE_BABY = new ResourceLocation("alexsmobs:textures/entity/bison_baby.png");
    private static final ResourceLocation TEXTURE_BABY_SNOWY = new ResourceLocation("alexsmobs:textures/entity/bison_baby_snowy.png");
    private static final ResourceLocation TEXTURE_SNOWY = new ResourceLocation("alexsmobs:textures/entity/bison_snowy.png");
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/bison.png");
    private static final ResourceLocation TEXTURE_SHEARED = new ResourceLocation("alexsmobs:textures/entity/bison_sheared.png");
    private final ModelBison modelBison = new ModelBison();
    private final ModelBisonBaby modelBaby = new ModelBisonBaby();

    public RenderBison(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, (EntityModel)new ModelBison(), 0.8f);
        this.m_115326_(new LayerSnow());
    }

    protected void scale(EntityBison entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        this.f_115290_ = entitylivingbaseIn.m_6162_() ? this.modelBaby : this.modelBison;
    }

    public ResourceLocation getTextureLocation(EntityBison entity) {
        return entity.m_6162_() ? TEXTURE_BABY : (entity.isSheared() ? TEXTURE_SHEARED : TEXTURE);
    }

    class LayerSnow
    extends RenderLayer<EntityBison, AdvancedEntityModel<EntityBison>> {
        public LayerSnow() {
            super((RenderLayerParent)RenderBison.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityBison entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.isSnowy()) {
                VertexConsumer ivertexbuilder = bufferIn.m_6299_(RenderType.m_110458_((ResourceLocation)(entitylivingbaseIn.m_6162_() ? TEXTURE_BABY_SNOWY : TEXTURE_SNOWY)));
                ((AdvancedEntityModel)this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.m_115338_((LivingEntity)entitylivingbaseIn, (float)0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
}

