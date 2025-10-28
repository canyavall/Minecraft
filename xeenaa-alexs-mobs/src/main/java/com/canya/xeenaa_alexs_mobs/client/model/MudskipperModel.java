package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.MudskipperEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MudskipperModel extends GeoModel<MudskipperEntity> {
    @Override
    public Identifier getModelResource(MudskipperEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/mudskipper_citadel.geo.json");
    }

    @Override
    public Identifier getTextureResource(MudskipperEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/mudskipper/mudskipper.png");
    }

    @Override
    public Identifier getAnimationResource(MudskipperEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/mudskipper_citadel.animation.json");
    }
}
