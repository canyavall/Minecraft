/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.resources.ResourceLocation
 */
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelAnaconda;
import com.github.alexthe666.alexsmobs.entity.EntityAnaconda;
import com.github.alexthe666.alexsmobs.entity.util.AnacondaPartIndex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderAnaconda
extends MobRenderer<EntityAnaconda, ModelAnaconda<EntityAnaconda>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/anaconda.png");
    private static final ResourceLocation TEXTURE_SHEDDING = new ResourceLocation("alexsmobs:textures/entity/anaconda_shedding.png");
    private static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation("alexsmobs:textures/entity/anaconda_yellow.png");
    private static final ResourceLocation TEXTURE_YELLOW_SHEDDING = new ResourceLocation("alexsmobs:textures/entity/anaconda_yellow_shedding.png");

    public RenderAnaconda(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelAnaconda(AnacondaPartIndex.HEAD), 0.3f);
    }

    protected void scale(EntityAnaconda entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.m_85841_(entitylivingbaseIn.m_6134_(), entitylivingbaseIn.m_6134_(), entitylivingbaseIn.m_6134_());
    }

    public static ResourceLocation getAnacondaTexture(boolean yellow, boolean shedding) {
        return yellow ? (shedding ? TEXTURE_YELLOW_SHEDDING : TEXTURE_YELLOW) : (shedding ? TEXTURE_SHEDDING : TEXTURE);
    }

    public ResourceLocation getTextureLocation(EntityAnaconda entity) {
        return RenderAnaconda.getAnacondaTexture(entity.isYellow(), entity.isShedding());
    }
}

