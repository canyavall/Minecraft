package com.canya.xeenaa_alexs_mobs.client.renderer;

import com.canya.xeenaa_alexs_mobs.client.model.MudskipperModel;
import com.canya.xeenaa_alexs_mobs.entity.animal.MudskipperEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MudskipperRenderer extends GeoEntityRenderer<MudskipperEntity> {
    public MudskipperRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MudskipperModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public Identifier getTextureLocation(MudskipperEntity entity) {
        return Identifier.of("xeenaa-alexs-mobs", "textures/entity/mudskipper/mudskipper.png");
    }
}
