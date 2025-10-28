package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.HummingbirdModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.HummingbirdEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HummingbirdRenderer extends GeoEntityRenderer<HummingbirdEntity> {
    public HummingbirdRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HummingbirdModel());
        this.shadowRadius = 0.2f;
    }

    @Override
    public Identifier getTextureLocation(HummingbirdEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/hummingbird/hummingbird.png");
    }
}
