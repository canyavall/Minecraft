/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class AMBlockPos {
    public static final BlockPos fromCoords(double x, double y, double z) {
        return new BlockPos((int)x, (int)y, (int)z);
    }

    public static final BlockPos fromVec3(Vec3 vec3) {
        return AMBlockPos.fromCoords(vec3.f_82479_, vec3.f_82480_, vec3.f_82481_);
    }
}

