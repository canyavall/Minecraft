package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.TriopsEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model for Triops entity.
 */
public class TriopsModel extends GeoModel<TriopsEntity> {
    @Override
    public Identifier getModelResource(TriopsEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/triops_citadel.geo.json");
    }

    @Override
    public Identifier getTextureResource(TriopsEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/triops/triops.png");
    }

    @Override
    public Identifier getAnimationResource(TriopsEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/triops_citadel.animation.json");
    }
}
