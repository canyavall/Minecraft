package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.BlobfishModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.BlobfishEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlobfishRenderer extends GeoEntityRenderer<BlobfishEntity> {
    public BlobfishRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new BlobfishModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public Identifier getTextureLocation(BlobfishEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/blobfish/blobfish.png");
    }
}
