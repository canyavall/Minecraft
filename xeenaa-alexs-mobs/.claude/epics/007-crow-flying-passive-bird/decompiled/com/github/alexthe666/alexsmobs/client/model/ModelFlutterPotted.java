/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.client.model.AdvancedEntityModel
 *  com.github.alexthe666.citadel.client.model.AdvancedModelBox
 *  com.github.alexthe666.citadel.client.model.basic.BasicModelPart
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFlutter;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ModelFlutterPotted
extends AdvancedEntityModel<EntityFlutter> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox pot;
    private final AdvancedModelBox left_foot;
    private final AdvancedModelBox right_foot;
    private final AdvancedModelBox body;
    private final AdvancedModelBox eyes;
    private final AdvancedModelBox petals;
    private final AdvancedModelBox front_petal;
    private final AdvancedModelBox left_petal;
    private final AdvancedModelBox right_petal;
    private final AdvancedModelBox back_petal;

    public ModelFlutterPotted() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox((AdvancedEntityModel)this, "root");
        this.root.setRotationPoint(0.0f, 23.0f, 0.0f);
        this.pot = new AdvancedModelBox((AdvancedEntityModel)this, "pot");
        this.pot.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.root.addChild((BasicModelPart)this.pot);
        this.pot.setTextureOffset(24, 43).addBox(-3.0f, -6.0f, -3.0f, 6.0f, 6.0f, 6.0f, 0.0f, false);
        this.pot.setTextureOffset(3, 42).addBox(-3.0f, -5.8f, -3.0f, 6.0f, 0.0f, 6.0f, 0.0f, false);
        this.left_foot = new AdvancedModelBox((AdvancedEntityModel)this, "left_foot");
        this.left_foot.setRotationPoint(1.6f, 0.0f, 0.8f);
        this.pot.addChild((BasicModelPart)this.left_foot);
        this.left_foot.setTextureOffset(1, 50).addBox(-1.0f, 0.0f, -2.0f, 2.0f, 1.0f, 2.0f, 0.0f, false);
        this.right_foot = new AdvancedModelBox((AdvancedEntityModel)this, "right_foot");
        this.right_foot.setRotationPoint(-1.6f, 0.0f, 0.8f);
        this.pot.addChild((BasicModelPart)this.right_foot);
        this.right_foot.setTextureOffset(1, 50).addBox(-1.0f, 0.0f, -2.0f, 2.0f, 1.0f, 2.0f, 0.0f, true);
        this.body = new AdvancedModelBox((AdvancedEntityModel)this, "body");
        this.body.setRotationPoint(0.0f, -7.9f, 0.0f);
        this.pot.addChild((BasicModelPart)this.body);
        this.body.setTextureOffset(0, 13).addBox(-3.5f, -3.0f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
        this.body.setTextureOffset(0, 0).addBox(-3.5f, -3.0f, -3.5f, 7.0f, 5.0f, 7.0f, -0.2f, false);
        this.eyes = new AdvancedModelBox((AdvancedEntityModel)this, "eyes");
        this.eyes.setRotationPoint(0.0f, -1.0f, -3.0f);
        this.body.addChild((BasicModelPart)this.eyes);
        this.eyes.setTextureOffset(23, 30).addBox(-1.5f, -0.5f, 0.0f, 3.0f, 1.0f, 0.0f, 0.0f, false);
        this.petals = new AdvancedModelBox((AdvancedEntityModel)this, "petals");
        this.petals.setRotationPoint(0.0f, -3.0f, 0.0f);
        this.body.addChild((BasicModelPart)this.petals);
        this.front_petal = new AdvancedModelBox((AdvancedEntityModel)this, "front_petal");
        this.front_petal.setRotationPoint(0.0f, 0.0f, -1.5f);
        this.petals.addChild((BasicModelPart)this.front_petal);
        this.setRotationAngle(this.front_petal, 1.1781f, 0.0f, 0.0f);
        this.front_petal.setTextureOffset(0, 26).addBox(-3.5f, -7.0f, 0.0f, 7.0f, 7.0f, 0.0f, 0.0f, false);
        this.left_petal = new AdvancedModelBox((AdvancedEntityModel)this, "left_petal");
        this.left_petal.setRotationPoint(1.5f, 0.0f, 0.0f);
        this.petals.addChild((BasicModelPart)this.left_petal);
        this.setRotationAngle(this.left_petal, 1.1781f, -1.5708f, 0.0f);
        this.left_petal.setTextureOffset(0, 26).addBox(-3.5f, -7.0f, 0.0f, 7.0f, 7.0f, 0.0f, 0.0f, false);
        this.right_petal = new AdvancedModelBox((AdvancedEntityModel)this, "right_petal");
        this.right_petal.setRotationPoint(-1.5f, 0.0f, 0.0f);
        this.petals.addChild((BasicModelPart)this.right_petal);
        this.setRotationAngle(this.right_petal, 1.1781f, 1.5708f, 0.0f);
        this.right_petal.setTextureOffset(0, 26).addBox(-3.5f, -7.0f, 0.0f, 7.0f, 7.0f, 0.0f, 0.0f, true);
        this.back_petal = new AdvancedModelBox((AdvancedEntityModel)this, "back_petal");
        this.back_petal.setRotationPoint(0.0f, 0.0f, 1.5f);
        this.petals.addChild((BasicModelPart)this.back_petal);
        this.setRotationAngle(this.back_petal, 1.1781f, 3.1416f, 0.0f);
        this.back_petal.setTextureOffset(0, 26).addBox(-3.5f, -7.0f, 0.0f, 7.0f, 7.0f, 0.0f, 0.0f, false);
        this.updateDefaultPose();
    }

    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of((Object)this.root);
    }

    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of((Object)this.root, (Object)this.body, (Object)this.eyes, (Object)this.petals, (Object)this.front_petal, (Object)this.left_petal, (Object)this.back_petal, (Object)this.right_petal, (Object)this.pot, (Object)this.left_foot, (Object)this.right_foot);
    }

    public void setupAnim(EntityFlutter entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.resetToDefaultPose();
        float idleSpeed = 0.25f;
        float idleDegree = 0.1f;
        float walkSpeed = 1.6f;
        float walkDegree = 1.2f;
        float partialTicks = ageInTicks - (float)entity.f_19797_;
        float shootProgress = entity.prevShootProgress + (entity.shootProgress - entity.prevShootProgress) * partialTicks;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTicks;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTicks;
        float groundProgress = (5.0f - flyProgress) * 0.2f;
        float tentacleProgress = (5.0f - limbSwingAmount * 5.0f) * flyProgress * 0.2f;
        float invertTentacle = (entity.prevTentacleProgress + (entity.tentacleProgress - entity.prevTentacleProgress) * partialTicks) * flyProgress * 0.2f;
        float flutterPitch = Maths.rad(Mth.m_14189_((float)partialTicks, (float)entity.prevFlutterPitch, (float)entity.getFlutterPitch()));
        Entity look = Minecraft.m_91087_().m_91288_();
        if (entity.isShakingHead()) {
            this.eyes.rotationPointX = (float)((double)this.eyes.rotationPointX + Math.sin(ageInTicks));
            this.body.rotateAngleY = (float)((double)this.body.rotateAngleY + Math.sin(ageInTicks) * (double)0.1f);
            this.eyes.rotationPointY = -0.5f;
        } else if (look != null) {
            float f1;
            Vec3 vector3d = look.m_20299_(0.0f);
            Vec3 vector3d1 = entity.m_20299_(0.0f);
            double d0 = vector3d.f_82480_ - vector3d1.f_82480_;
            this.eyes.rotationPointY = f1 = (float)Mth.m_14008_((double)(-d0 - 0.5), (double)-2.0, (double)0.0);
            Vec3 vector3d2 = entity.m_20252_(0.0f);
            vector3d2 = new Vec3(vector3d2.f_82479_, 0.0, vector3d2.f_82481_);
            Vec3 vector3d3 = new Vec3(vector3d1.f_82479_ - vector3d.f_82479_, 0.0, vector3d1.f_82481_ - vector3d.f_82481_).m_82541_().m_82524_(1.5707964f);
            double d1 = vector3d2.m_82526_(vector3d3);
            this.eyes.rotationPointX += Mth.m_14116_((float)((float)Math.abs(d1))) * 1.5f * (float)Math.signum(d1);
        } else {
            this.eyes.rotationPointY = -1.0f;
        }
        this.walk(this.left_foot, walkSpeed, walkDegree, false, 1.0f, -0.1f, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.right_foot, walkSpeed, walkDegree, false, 1.0f, -0.1f, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.root, walkSpeed, walkDegree * 0.2f, false, 1.0f, 0.1f, limbSwing, limbSwingAmount * groundProgress);
        this.flap(this.root, walkSpeed * 0.5f, walkDegree * 0.2f, false, 2.0f, 0.0f, limbSwing, limbSwingAmount * groundProgress);
        this.bob(this.root, walkSpeed * 0.5f, walkDegree * 6.0f, true, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.front_petal, idleSpeed, idleDegree, true, 1.0f, 0.1f, ageInTicks, 1.0f);
        this.walk(this.back_petal, idleSpeed, idleDegree, true, 1.0f, 0.1f, ageInTicks, 1.0f);
        this.flap(this.right_petal, idleSpeed, idleDegree, false, 1.0f, 0.1f, ageInTicks, 1.0f);
        this.flap(this.left_petal, idleSpeed, idleDegree, true, 1.0f, 0.1f, ageInTicks, 1.0f);
        this.flap(this.body, 0.4f, 0.2f, false, 2.0f, 0.0f, limbSwing, limbSwingAmount * flyProgress * 0.2f);
        this.flap(this.pot, 0.4f, 0.2f, true, 2.0f, 0.0f, limbSwing, limbSwingAmount * flyProgress * 0.2f);
        this.progressRotationPrev(this.front_petal, Math.max(shootProgress, invertTentacle), Maths.rad(-45.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.back_petal, Math.max(shootProgress, invertTentacle), Maths.rad(-45.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.right_petal, Math.max(shootProgress, invertTentacle), 0.0f, 0.0f, Maths.rad(45.0), 5.0f);
        this.progressRotationPrev(this.left_petal, Math.max(shootProgress, invertTentacle), 0.0f, 0.0f, Maths.rad(-45.0), 5.0f);
        this.progressRotationPrev(this.front_petal, Math.max(invertTentacle - shootProgress, 0.0f), Maths.rad(-45.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.back_petal, Math.max(invertTentacle - shootProgress, 0.0f), Maths.rad(-45.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.right_petal, Math.max(invertTentacle - shootProgress, 0.0f), 0.0f, 0.0f, Maths.rad(45.0), 5.0f);
        this.progressRotationPrev(this.left_petal, Math.max(invertTentacle - shootProgress, 0.0f), 0.0f, 0.0f, Maths.rad(-45.0), 5.0f);
        this.progressRotationPrev(this.front_petal, flyProgress, Maths.rad(15.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.back_petal, flyProgress, Maths.rad(15.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.right_petal, flyProgress, 0.0f, 0.0f, Maths.rad(-15.0), 5.0f);
        this.progressRotationPrev(this.left_petal, flyProgress, 0.0f, 0.0f, Maths.rad(15.0), 5.0f);
        this.progressPositionPrev(this.root, tentacleProgress, 0.0f, -3.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.front_petal, tentacleProgress, Maths.rad(5.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.back_petal, tentacleProgress, Maths.rad(5.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.right_petal, tentacleProgress, Maths.rad(5.0), 0.0f, 0.0f, 5.0f);
        this.progressRotationPrev(this.left_petal, tentacleProgress, Maths.rad(5.0), 0.0f, 0.0f, 5.0f);
        this.progressPositionPrev(this.root, sitProgress, 0.0f, 1.0f, 0.0f, 5.0f);
        this.progressPositionPrev(this.left_foot, Math.max(flyProgress, sitProgress), 0.0f, -1.0f, 0.0f, 5.0f);
        this.progressPositionPrev(this.right_foot, Math.max(flyProgress, sitProgress), 0.0f, -1.0f, 0.0f, 5.0f);
        this.root.rotateAngleX -= flutterPitch * flyProgress * 0.1f;
        this.body.rotateAngleY = (float)((double)this.body.rotateAngleY + Math.toRadians(Mth.m_14177_((float)(shootProgress * 360.0f * 0.2f))));
        float petalScale = 1.0f + invertTentacle * 0.05f;
        this.front_petal.setScale(1.0f, petalScale, 1.0f);
        this.back_petal.setScale(1.0f, petalScale, 1.0f);
        this.left_petal.setScale(1.0f, petalScale, 1.0f);
        this.right_petal.setScale(1.0f, petalScale, 1.0f);
        if (entity.m_6162_()) {
            this.pot.setScale(1.0f, 1.0f, 1.0f);
            this.body.rotationPointY += 1.5f;
            this.pot.rotationPointY += 0.5f;
            this.body.setShouldScaleChildren(true);
            this.body.setScale(0.5f, 0.5f, 0.5f);
            this.left_foot.setScale(0.5f, 0.5f, 0.5f);
            this.right_foot.setScale(0.5f, 0.5f, 0.5f);
        } else {
            this.pot.setScale(1.0f, 1.0f, 1.0f);
            this.body.setScale(1.0f, 1.0f, 1.0f);
            this.left_foot.setScale(1.0f, 1.0f, 1.0f);
            this.right_foot.setScale(1.0f, 1.0f, 1.0f);
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox2, float x, float y, float z) {
        AdvancedModelBox2.rotateAngleX = x;
        AdvancedModelBox2.rotateAngleY = y;
        AdvancedModelBox2.rotateAngleZ = z;
    }
}

