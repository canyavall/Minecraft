package com.canya.xeenaa_alexs_mobs.client.model;

import com.canya.xeenaa_alexs_mobs.entity.animal.BlobfishEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BlobfishModel extends GeoModel<BlobfishEntity> {
    @Override
    public Identifier getModelResource(BlobfishEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "geo/blobfish_citadel.geo.json");
    }

    @Override
    public Identifier getTextureResource(BlobfishEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/blobfish/blobfish.png");
    }

    @Override
    public Identifier getAnimationResource(BlobfishEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "animations/blobfish_citadel.animation.json");
    }
}
