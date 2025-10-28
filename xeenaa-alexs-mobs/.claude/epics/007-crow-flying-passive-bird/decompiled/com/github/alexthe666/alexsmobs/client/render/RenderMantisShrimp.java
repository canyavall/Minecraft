/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.resources.ResourceLocation
 */
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelMantisShrimp;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerMantisShrimpItem;
import com.github.alexthe666.alexsmobs.entity.EntityMantisShrimp;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderMantisShrimp
extends MobRenderer<EntityMantisShrimp, ModelMantisShrimp> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("alexsmobs:textures/entity/mantis_shrimp_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("alexsmobs:textures/entity/mantis_shrimp_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("alexsmobs:textures/entity/mantis_shrimp_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("alexsmobs:textures/entity/mantis_shrimp_3.png");

    public RenderMantisShrimp(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, (EntityModel)new ModelMantisShrimp(), 0.6f);
        this.m_115326_(new LayerMantisShrimpItem(this));
    }

    protected void scale(EntityMantisShrimp entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.m_85841_(0.8f, 0.8f, 0.8f);
    }

    public ResourceLocation getTextureLocation(EntityMantisShrimp entity) {
        return entity.getVariant() == 3 ? TEXTURE_3 : (entity.getVariant() == 2 ? TEXTURE_2 : (entity.getVariant() == 1 ? TEXTURE_1 : TEXTURE_0));
    }
}

