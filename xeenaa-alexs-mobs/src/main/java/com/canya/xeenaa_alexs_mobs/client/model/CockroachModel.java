package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.CockroachEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model for Cockroach entity.
 */
public class CockroachModel extends GeoModel<CockroachEntity> {
    @Override
    public Identifier getModelResource(CockroachEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/cockroach_citadel.geo.json");
    }

    @Override
    public Identifier getTextureResource(CockroachEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/cockroach/cockroach.png");
    }

    @Override
    public Identifier getAnimationResource(CockroachEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/cockroach_citadel.animation.json");
    }
}
