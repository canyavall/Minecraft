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

import com.github.alexthe666.alexsmobs.client.model.ModelManedWolf;
import com.github.alexthe666.alexsmobs.entity.EntityManedWolf;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderManedWolf
extends MobRenderer<EntityManedWolf, ModelManedWolf> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/maned_wolf.png");
    private static final ResourceLocation TEXTURE_ENDER = new ResourceLocation("alexsmobs:textures/entity/maned_wolf_ender.png");

    public RenderManedWolf(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, (EntityModel)new ModelManedWolf(), 0.45f);
    }

    protected void scale(EntityManedWolf entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.m_85841_(0.85f, 0.85f, 0.85f);
    }

    public ResourceLocation getTextureLocation(EntityManedWolf entity) {
        return entity.isEnder() ? TEXTURE_ENDER : TEXTURE;
    }
}

