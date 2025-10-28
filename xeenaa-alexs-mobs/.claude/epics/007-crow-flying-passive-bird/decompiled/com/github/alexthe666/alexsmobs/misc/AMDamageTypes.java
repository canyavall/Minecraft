/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package com.github.alexthe666.alexsmobs.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AMDamageTypes {
    public static final ResourceKey<DamageType> FARSEER = ResourceKey.m_135785_((ResourceKey)Registries.f_268580_, (ResourceLocation)new ResourceLocation("alexsmobs:farseer"));
    public static final ResourceKey<DamageType> FREDDY = ResourceKey.m_135785_((ResourceKey)Registries.f_268580_, (ResourceLocation)new ResourceLocation("alexsmobs:freddy"));

    public static DamageSource causeFarseerDamage(LivingEntity attacker) {
        return new DamageSourceRandomMessages((Holder<DamageType>)((Registry)attacker.m_9236_().m_9598_().m_6632_(Registries.f_268580_).get()).m_246971_(FARSEER), (Entity)attacker);
    }

    public static DamageSource causeFreddyBearDamage(LivingEntity attacker) {
        return new DamageSource((Holder)((Registry)attacker.m_9236_().m_9598_().m_6632_(Registries.f_268580_).get()).m_246971_(FREDDY), (Entity)attacker);
    }

    private static class DamageSourceRandomMessages
    extends DamageSource {
        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity1, @Nullable Entity entity2, @Nullable Vec3 from) {
            super(damageTypeHolder, entity1, entity2, from);
        }

        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity1, @Nullable Entity entity2) {
            super(damageTypeHolder, entity1, entity2);
        }

        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, Vec3 from) {
            super(damageTypeHolder, from);
        }

        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity) {
            super(damageTypeHolder, entity);
        }

        public DamageSourceRandomMessages(Holder<DamageType> p_270475_) {
            super(p_270475_);
        }

        public Component m_6157_(LivingEntity attacked) {
            int type = attacked.m_217043_().m_188503_(3);
            LivingEntity livingentity = attacked.m_21232_();
            String s = "death.attack." + this.m_19385_() + "_" + type;
            String s1 = s + ".player";
            return livingentity != null ? Component.m_237110_((String)s1, (Object[])new Object[]{attacked.m_5446_(), livingentity.m_5446_()}) : Component.m_237110_((String)s, (Object[])new Object[]{attacked.m_5446_()});
        }
    }
}

