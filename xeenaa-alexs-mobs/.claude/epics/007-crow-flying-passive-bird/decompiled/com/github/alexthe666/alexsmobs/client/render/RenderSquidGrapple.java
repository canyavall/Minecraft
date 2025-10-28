/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.LightTexture
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.LightLayer
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelSquidGrapple;
import com.github.alexthe666.alexsmobs.entity.EntitySquidGrapple;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderSquidGrapple
extends EntityRenderer<EntitySquidGrapple> {
    private static final ResourceLocation SQUID_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/giant_squid.png");
    private static final ModelSquidGrapple SQUID_MODEL = new ModelSquidGrapple();
    private static final float TENTACLES_COLOR_R = 0.70980394f;
    private static final float TENTACLES_COLOR_G = 0.34117648f;
    private static final float TENTACLES_COLOR_B = 0.33333334f;
    private static final float TENTACLES_COLOR_R2 = 0.7490196f;
    private static final float TENTACLES_COLOR_G2 = 0.38431373f;
    private static final float TENTACLES_COLOR_B2 = 0.34901962f;

    public RenderSquidGrapple(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    private static void addVertexPairAlex(VertexConsumer p_174308_, Matrix4f p_174309_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float)p_174321_ / 24.0f;
        int i = (int)Mth.m_14179_((float)f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.m_14179_((float)f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.m_109885_((int)i, (int)j);
        float f2 = 0.70980394f;
        float f3 = 0.34117648f;
        float f4 = 0.33333334f;
        if (p_174321_ % 2 == (p_174322_ ? 1 : 0)) {
            f2 = 0.7490196f;
            f3 = 0.38431373f;
            f4 = 0.34901962f;
        }
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0f ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0f - f) * (1.0f - f);
        float f7 = p_174312_ * f;
        p_174308_.m_252986_(p_174309_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).m_85950_(f2, f3, f4, 1.0f).m_85969_(k).m_5752_();
        p_174308_.m_252986_(p_174309_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).m_85950_(f2, f3, f4, 1.0f).m_85969_(k).m_5752_();
    }

    public static <E extends Entity> void renderTentacle(Entity mob, float partialTick, PoseStack p_115464_, MultiBufferSource p_115465_, LivingEntity player, boolean left, float zOffset) {
        p_115464_.m_85836_();
        float bodyRot = mob instanceof LivingEntity ? ((LivingEntity)mob).f_20883_ : mob.m_146908_();
        float bodyRot0 = mob instanceof LivingEntity ? ((LivingEntity)mob).f_20884_ : mob.f_19859_;
        Vec3 vec3 = player.m_7398_(partialTick);
        double d0 = (double)(Mth.m_14179_((float)partialTick, (float)bodyRot, (float)bodyRot0) * ((float)Math.PI / 180)) + 1.5707963267948966;
        Vec3 vec31 = new Vec3(0.0, 0.0, 0.0);
        double d1 = Math.cos(d0) * vec31.f_82481_ + Math.sin(d0) * vec31.f_82479_;
        double d2 = Math.sin(d0) * vec31.f_82481_ - Math.cos(d0) * vec31.f_82479_;
        double d3 = Mth.m_14139_((double)partialTick, (double)mob.f_19854_, (double)mob.m_20185_()) + d1;
        double d4 = Mth.m_14139_((double)partialTick, (double)mob.f_19855_, (double)mob.m_20186_()) + vec31.f_82480_;
        double d5 = Mth.m_14139_((double)partialTick, (double)mob.f_19856_, (double)mob.m_20189_()) + d2;
        p_115464_.m_85837_(d3, d4, d5);
        float f = (float)(vec3.f_82479_ - d3);
        float f1 = (float)(vec3.f_82480_ - d4);
        float f2 = (float)(vec3.f_82481_ - d5);
        VertexConsumer vertexconsumer = p_115465_.m_6299_(RenderType.m_110475_());
        Matrix4f matrix4f = p_115464_.m_85850_().m_252922_();
        float f4 = (float)(Mth.m_14193_((double)(f * f + f2 * f2)) * (double)0.025f / 2.0);
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = AMBlockPos.fromVec3(mob.m_20299_(partialTick));
        BlockPos blockpos1 = AMBlockPos.fromVec3(player.m_20299_(partialTick));
        int i = RenderSquidGrapple.getTentacleLightLevel(mob, blockpos);
        int j = mob.m_9236_().m_45517_(LightLayer.BLOCK, blockpos1);
        int k = mob.m_9236_().m_45517_(LightLayer.SKY, blockpos);
        int l = mob.m_9236_().m_45517_(LightLayer.SKY, blockpos1);
        float width = 0.2f;
        for (int i1 = 0; i1 <= 24; ++i1) {
            RenderSquidGrapple.addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, i1, false);
        }
        for (int j1 = 24; j1 >= 0; --j1) {
            RenderSquidGrapple.addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, j1, true);
        }
        p_115464_.m_85849_();
    }

    protected static int getTentacleLightLevel(Entity p_114496_, BlockPos p_114497_) {
        return p_114496_.m_6060_() ? 15 : p_114496_.m_9236_().m_45517_(LightLayer.BLOCK, p_114497_);
    }

    public boolean shouldRender(EntitySquidGrapple grapple, Frustum f, double d1, double d2, double d3) {
        return super.m_5523_((Entity)grapple, f, d1, d2, d3) || grapple.getOwner() != null && (f.m_113029_(grapple.getOwner().m_20191_()) || grapple.getOwner() == Minecraft.m_91087_().f_91074_);
    }

    public void render(EntitySquidGrapple entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.m_85836_();
        matrixStackIn.m_252781_(Axis.f_252392_.m_252977_(Mth.m_14179_((float)partialTicks, (float)entityIn.f_19859_, (float)entityIn.m_146908_())));
        matrixStackIn.m_252781_(Axis.f_252529_.m_252977_(180.0f + Mth.m_14179_((float)partialTicks, (float)entityIn.f_19860_, (float)entityIn.m_146909_())));
        matrixStackIn.m_252880_(0.0f, -1.5f, -0.25f);
        VertexConsumer ivertexbuilder = bufferIn.m_6299_(RenderType.m_110458_((ResourceLocation)this.getTextureLocation(entityIn)));
        SQUID_MODEL.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.f_118083_, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStackIn.m_85849_();
        super.m_7392_((Entity)entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        Entity entity = entityIn.getOwner();
        if (entity instanceof LivingEntity) {
            LivingEntity holder = (LivingEntity)entity;
            double d0 = Mth.m_14139_((double)partialTicks, (double)entityIn.f_19790_, (double)entityIn.m_20185_());
            double d1 = Mth.m_14139_((double)partialTicks, (double)entityIn.f_19791_, (double)entityIn.m_20186_());
            double d2 = Mth.m_14139_((double)partialTicks, (double)entityIn.f_19792_, (double)entityIn.m_20189_());
            matrixStackIn.m_85836_();
            matrixStackIn.m_85837_(-d0, -d1, -d2);
            RenderSquidGrapple.renderTentacle(entityIn, partialTicks, matrixStackIn, bufferIn, holder, holder.m_5737_() != HumanoidArm.LEFT, -0.1f);
            matrixStackIn.m_85849_();
        }
    }

    public ResourceLocation getTextureLocation(EntitySquidGrapple entity) {
        return SQUID_TEXTURE;
    }

    public void drawVertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, VertexConsumer p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_) {
        p_229039_3_.m_252986_(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).m_6122_(255, 255, 255, 255).m_7421_(p_229039_7_, p_229039_8_).m_86008_(OverlayTexture.f_118083_).m_85969_(p_229039_12_).m_252939_(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).m_5752_();
    }
}

