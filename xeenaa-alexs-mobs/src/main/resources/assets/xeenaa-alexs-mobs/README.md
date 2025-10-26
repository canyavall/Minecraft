# Assets Directory

Client-side resources for the Xeenaa's Alex's Mobs mod.

## Directory Structure

```
assets/xeenaa-alexs-mobs/
├── geo/                    # GeckoLib 3D models (.geo.json)
├── animations/             # GeckoLib animation files (.animation.json)
├── textures/
│   ├── entity/            # Entity textures (.png)
│   └── item/              # Item textures (.png)
├── sounds/                # Sound files (.ogg)
├── lang/                  # Translation files (en_us.json, etc.)
└── icon.png               # Mod icon (128x128)
```

## Resource Types

### GeckoLib Models (geo/)
- **Format**: `.geo.json` (Bedrock Edition geometry format)
- **Purpose**: 3D models for animated entities
- **Naming**: `entity_name.geo.json` (e.g., `test_animal.geo.json`)
- **Created With**: Blockbench with GeckoLib plugin

### GeckoLib Animations (animations/)
- **Format**: `.animation.json` (Bedrock Edition animation format)
- **Purpose**: Animation definitions for entity models
- **Naming**: `entity_name.animation.json` (e.g., `test_animal.animation.json`)
- **Created With**: Blockbench with GeckoLib plugin

### Entity Textures (textures/entity/)
- **Format**: `.png`
- **Purpose**: Skins for entity models
- **Naming**: `entity_name.png` (e.g., `test_animal.png`)
- **Resolution**: Match model UV layout (typically 64x64, 128x128, or 256x256)

### Item Textures (textures/item/)
- **Format**: `.png`
- **Purpose**: Icons for items in inventory
- **Naming**: `item_name.png`
- **Resolution**: 16x16 (standard Minecraft item size)

### Sounds (sounds/)
- **Format**: `.ogg` (Vorbis audio)
- **Purpose**: Entity sounds (ambient, hurt, death, etc.)
- **Naming**: `entity_name_sound_type.ogg` (e.g., `capybara_ambient.ogg`)
- **Required**: `sounds.json` to define sound events

### Translations (lang/)
- **Format**: `.json`
- **Purpose**: Localized entity/item names and descriptions
- **Files**: `en_us.json` (English), `es_es.json` (Spanish), etc.
- **Keys**: `entity.xeenaa-alexs-mobs.entity_name`, `item.xeenaa-alexs-mobs.item_name`

## Integration with Code

Assets are referenced in code using `ResourceLocation`:

```java
// In GeoModel class
@Override
public ResourceLocation getModelResource(TestAnimalEntity entity) {
    return new ResourceLocation("xeenaa-alexs-mobs", "geo/test_animal.geo.json");
}

@Override
public ResourceLocation getTextureResource(TestAnimalEntity entity) {
    return new ResourceLocation("xeenaa-alexs-mobs", "textures/entity/test_animal.png");
}

@Override
public ResourceLocation getAnimationResource(TestAnimalEntity entity) {
    return new ResourceLocation("xeenaa-alexs-mobs", "animations/test_animal.animation.json");
}
```
