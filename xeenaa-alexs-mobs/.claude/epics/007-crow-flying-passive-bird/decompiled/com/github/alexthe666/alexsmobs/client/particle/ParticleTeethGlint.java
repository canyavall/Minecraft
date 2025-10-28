/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.github.alexthe666.alexsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleTeethGlint
extends TextureSheetParticle {
    private float initScale = 0.25f;

    private ParticleTeethGlint(ClientLevel p_i232444_1_, double p_i232444_2_, double p_i232444_4_, double p_i232444_6_, double p_i232444_8_, double p_i232444_10_, double p_i232444_12_) {
        super(p_i232444_1_, p_i232444_2_, p_i232444_4_, p_i232444_6_, p_i232444_8_, p_i232444_10_, p_i232444_12_);
        float lvt_14_1_;
        this.f_107227_ = lvt_14_1_ = this.f_107223_.m_188501_() * 0.1f + 0.2f;
        this.f_107228_ = lvt_14_1_;
        this.f_107229_ = lvt_14_1_;
        this.m_107250_(0.02f, 0.02f);
        this.f_107663_ = this.initScale = 0.1f * (this.f_107223_.m_188501_() * 0.3f + 0.5f);
        this.f_107215_ *= (double)0.02f;
        this.f_107216_ *= (double)0.02f;
        this.f_107217_ *= (double)0.02f;
        this.f_107225_ = 3 + this.f_107223_.m_188503_(5);
        this.m_107271_(1.0f - (float)this.f_107224_ / (float)this.f_107225_ * 0.5f);
    }

    public int m_6355_(float p_189214_1_) {
        int lvt_2_1_ = super.m_6355_(p_189214_1_);
        int lvt_4_1_ = lvt_2_1_ >> 16 & 0xFF;
        return 0xF0 | lvt_4_1_ << 16;
    }

    public ParticleRenderType m_7556_() {
        return ParticleRenderType.f_107431_;
    }

    public void m_6257_(double p_187110_1_, double p_187110_3_, double p_187110_5_) {
        this.m_107259_(this.m_107277_().m_82386_(p_187110_1_, p_187110_3_, p_187110_5_));
        this.m_107275_();
    }

    public void m_5989_() {
        super.m_5989_();
        this.f_107204_ = this.f_107231_;
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ *= 0.99;
            this.f_107216_ *= 0.99;
            this.f_107217_ *= 0.99;
        }
        this.f_107231_ = (float)((double)this.f_107231_ + 0.25 * Math.sin(this.f_107224_ * 2));
        this.f_107663_ = this.initScale * (1.0f - (float)this.f_107224_ / (float)this.f_107225_ * 0.5f);
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Factory
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet p_i50522_1_) {
            this.spriteSet = p_i50522_1_;
        }

        public Particle createParticle(SimpleParticleType p_199234_1_, ClientLevel p_199234_2_, double p_199234_3_, double p_199234_5_, double p_199234_7_, double p_199234_9_, double p_199234_11_, double p_199234_13_) {
            ParticleTeethGlint lvt_15_1_ = new ParticleTeethGlint(p_199234_2_, p_199234_3_, p_199234_5_, p_199234_7_, p_199234_9_, p_199234_11_, p_199234_13_);
            lvt_15_1_.m_108335_(this.spriteSet);
            lvt_15_1_.m_107253_(1.0f, 1.0f, 1.0f);
            return lvt_15_1_;
        }
    }
}

