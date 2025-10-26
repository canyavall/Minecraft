# Entity Textures Directory

This directory contains texture files (skins) for entity models.

## Purpose
Entity textures define the visual appearance of 3D models, mapping colors and patterns onto the geometry.

## File Format
- **Extension**: `.png`
- **Color Mode**: RGBA (supports transparency)
- **Compression**: PNG (lossless)

## Naming Convention
`entity_name.png`

**Examples**:
- `test_animal.png`
- `capybara.png`
- `flying_fish.png`

## Texture Resolution
Common resolutions based on model complexity:
- **64x64**: Simple mobs (similar to vanilla animals)
- **128x128**: Detailed mobs with more features
- **256x256**: Highly detailed mobs with complex patterns

**Current Project Standard**: 64x64 (matches test_animal.png)

## UV Mapping
Texture layout must match the UV mapping defined in the corresponding `.geo.json` model file. Use Blockbench to:
1. Create the model
2. Paint the texture directly on the model
3. Export both model and texture together

## Texture Variants
For entities with multiple variants (e.g., different colors), use suffixes:
- `capybara_brown.png`
- `capybara_gray.png`
- `capybara_albino.png`

Logic in `GeoModel` class selects the appropriate variant:
```java
@Override
public ResourceLocation getTextureResource(CapybaraEntity entity) {
    return new ResourceLocation("xeenaa-alexs-mobs",
        "textures/entity/capybara_" + entity.getVariant().getName() + ".png");
}
```

## Integration
Textures are loaded by `GeoModel` classes:

```java
@Override
public ResourceLocation getTextureResource(TestAnimalEntity entity) {
    return new ResourceLocation("xeenaa-alexs-mobs", "textures/entity/test_animal.png");
}
```

## Best Practices
- Match texture resolution to model UV layout
- Use transparent pixels (alpha channel) for areas that should not render
- Keep file size reasonable (optimize with tools like OptiPNG)
- Use consistent art style across all entity textures
- Test textures in-game to verify correct mapping
- Save source files (e.g., .psd, .xcf) separately for future edits
