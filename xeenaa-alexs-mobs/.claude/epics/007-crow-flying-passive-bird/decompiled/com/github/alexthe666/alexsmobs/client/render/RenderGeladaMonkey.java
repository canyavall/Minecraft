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

import com.github.alexthe666.alexsmobs.client.model.ModelGeladaMonkey;
import com.github.alexthe666.alexsmobs.entity.EntityGeladaMonkey;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderGeladaMonkey
extends MobRenderer<EntityGeladaMonkey, ModelGeladaMonkey> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/gelada_monkey.png");
    private static final ResourceLocation TEXTURE_ANGRY = new ResourceLocation("alexsmobs:textures/entity/gelada_monkey_angry.png");
    private static final ResourceLocation TEXTURE_LEADER = new ResourceLocation("alexsmobs:textures/entity/gelada_monkey_leader.png");
    private static final ResourceLocation TEXTURE_LEADER_ANGRY = new ResourceLocation("alexsmobs:textures/entity/gelada_monkey_leader_angry.png");

    public RenderGeladaMonkey(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, (EntityModel)new ModelGeladaMonkey(), 0.45f);
    }

    protected void scale(EntityGeladaMonkey entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.m_85841_(entitylivingbaseIn.getGeladaScale(), entitylivingbaseIn.getGeladaScale(), entitylivingbaseIn.getGeladaScale());
    }

    public ResourceLocation getTextureLocation(EntityGeladaMonkey entity) {
        return entity.isLeader() ? (entity.isAggro() ? TEXTURE_LEADER_ANGRY : TEXTURE_LEADER) : (entity.isAggro() ? TEXTURE_ANGRY : TEXTURE);
    }
}

