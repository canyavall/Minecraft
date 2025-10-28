/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.ItemInHandRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 */
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelCosmaw;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerBasicGlow;
import com.github.alexthe666.alexsmobs.entity.EntityCosmaw;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderCosmaw
extends MobRenderer<EntityCosmaw, ModelCosmaw> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/cosmaw.png");
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexsmobs:textures/entity/cosmaw_glow.png");

    public RenderCosmaw(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, (EntityModel)new ModelCosmaw(), 0.9f);
        this.m_115326_(new LayerHeldItem());
        this.m_115326_(new LayerBasicGlow(this, TEXTURE_GLOW));
    }

    protected void scale(EntityCosmaw entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.m_252880_(0.0f, -0.5f, 0.0f);
    }

    public ResourceLocation getTextureLocation(EntityCosmaw entity) {
        return TEXTURE;
    }

    class LayerHeldItem
    extends RenderLayer<EntityCosmaw, ModelCosmaw> {
        public LayerHeldItem() {
            super((RenderLayerParent)RenderCosmaw.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityCosmaw entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = entitylivingbaseIn.m_21205_();
            matrixStackIn.m_85836_();
            this.translateToHand(matrixStackIn);
            matrixStackIn.m_85837_(-0.0, (double)0.1f, (double)-1.35f);
            matrixStackIn.m_252781_(Axis.f_252529_.m_252977_(-45.0f));
            matrixStackIn.m_252781_(Axis.f_252436_.m_252977_(-180.0f));
            matrixStackIn.m_252781_(Axis.f_252403_.m_252977_(135.0f));
            matrixStackIn.m_85841_(2.0f, 2.0f, 2.0f);
            ItemInHandRenderer renderer = Minecraft.m_91087_().m_91290_().m_234586_();
            renderer.m_269530_((LivingEntity)entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.m_85849_();
        }

        protected void translateToHand(PoseStack matrixStack) {
            ((ModelCosmaw)this.m_117386_()).root.translateAndRotate(matrixStack);
            ((ModelCosmaw)this.m_117386_()).body.translateAndRotate(matrixStack);
            ((ModelCosmaw)this.m_117386_()).mouthArm1.translateAndRotate(matrixStack);
            ((ModelCosmaw)this.m_117386_()).mouthArm2.translateAndRotate(matrixStack);
        }
    }
}

