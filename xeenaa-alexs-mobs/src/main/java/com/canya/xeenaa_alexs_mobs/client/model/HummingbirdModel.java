package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.HummingbirdEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class HummingbirdModel extends GeoModel<HummingbirdEntity> {
    @Override
    public Identifier getModelResource(HummingbirdEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/hummingbird_citadel.geo.json");
    }

    @Override
    public Identifier getTextureResource(HummingbirdEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/hummingbird/hummingbird.png");
    }

    @Override
    public Identifier getAnimationResource(HummingbirdEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/hummingbird_citadel.animation.json");
    }
}
