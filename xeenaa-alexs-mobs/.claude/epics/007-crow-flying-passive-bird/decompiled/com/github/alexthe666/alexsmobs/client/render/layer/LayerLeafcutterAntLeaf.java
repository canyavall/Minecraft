/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.client.model.AdvancedEntityModel
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 */
package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelLeafcutterAnt;
import com.github.alexthe666.alexsmobs.client.render.OctopusColorRegistry;
import com.github.alexthe666.alexsmobs.client.render.RenderLeafcutterAnt;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class LayerLeafcutterAntLeaf
extends RenderLayer<EntityLeafcutterAnt, AdvancedEntityModel<EntityLeafcutterAnt>> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant_leaf_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant_leaf_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("alexsmobs:textures/entity/leafcutter_ant_leaf_2.png");

    public LayerLeafcutterAntLeaf(RenderLeafcutterAnt render) {
        super((RenderLayerParent)render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityLeafcutterAnt entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.hasLeaf() && !entitylivingbaseIn.isQueen() && this.m_117386_() instanceof ModelLeafcutterAnt) {
            int leafType = entitylivingbaseIn.m_19879_() % 3;
            ResourceLocation res = switch (leafType) {
                case 2 -> TEXTURE_2;
                case 1 -> TEXTURE_1;
                default -> TEXTURE_0;
            };
            VertexConsumer ivertexbuilder = bufferIn.m_6299_(RenderType.m_110458_((ResourceLocation)res));
            int leafColor = Minecraft.m_91087_().getItemColors().m_92676_(new ItemStack((ItemLike)Items.f_41899_), 0);
            if (entitylivingbaseIn.getHarvestedPos() != null && entitylivingbaseIn.getHarvestedState() != null) {
                leafColor = OctopusColorRegistry.getBlockColor(entitylivingbaseIn.getHarvestedState());
            }
            float f = (float)(leafColor >> 16 & 0xFF) / 255.0f;
            float f1 = (float)(leafColor >> 8 & 0xFF) / 255.0f;
            float f2 = (float)(leafColor & 0xFF) / 255.0f;
            ((AdvancedEntityModel)this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.m_115338_((LivingEntity)entitylivingbaseIn, (float)0.0f), f, f1, f2, 1.0f);
        }
    }
}

