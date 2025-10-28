/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SimpleAnimatedParticle
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticlePlatypus
extends SimpleAnimatedParticle {
    private ParticlePlatypus(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0f);
        this.f_107215_ = (float)motionX;
        this.f_107216_ = (float)motionY;
        this.f_107217_ = (float)motionZ;
        this.f_107663_ *= 0.2f + this.f_107223_.m_188501_() * 0.6f;
        this.f_107225_ = 3 + this.f_107223_.m_188503_(2);
        this.f_107226_ = 0.0f;
        this.m_108339_(sprites);
    }

    public int m_6355_(float p_189214_1_) {
        int lvt_2_1_ = super.m_6355_(p_189214_1_);
        int lvt_4_1_ = lvt_2_1_ >> 16 & 0xFF;
        return 0xF0 | lvt_4_1_ << 16;
    }

    public void m_5989_() {
        float lvt_9_1_;
        super.m_5989_();
        this.f_107215_ *= 0.8;
        this.f_107216_ *= 0.8;
        this.f_107217_ *= 0.8;
        double lvt_1_1_ = this.f_107212_ - this.f_107209_;
        double lvt_5_1_ = this.f_107214_ - this.f_107211_;
        this.f_107204_ = this.f_107231_ = (lvt_9_1_ = (float)(Mth.m_14136_((double)lvt_5_1_, (double)lvt_1_1_) * 57.2957763671875) - 180.0f);
        this.m_108339_(this.f_107644_);
    }

    public ParticleRenderType m_7556_() {
        return ParticleRenderType.f_107430_;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Factory
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticlePlatypus p = new ParticlePlatypus(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            return p;
        }
    }
}

