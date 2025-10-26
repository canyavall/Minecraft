# Resource Directory Structure

This directory contains all mod resources following Minecraft's resource pack and data pack conventions.

## Directory Organization

```
resources/
├── assets/xeenaa-alexs-mobs/     # Client-side resources (textures, models, sounds, translations)
└── data/xeenaa-alexs-mobs/       # Server-side resources (loot tables, recipes, tags)
```

## ResourceLocation Path Conventions

All resources are referenced using `ResourceLocation` with the mod ID namespace:

```java
// General pattern
new ResourceLocation("xeenaa-alexs-mobs", "path/to/resource")

// Entity textures
new ResourceLocation("xeenaa-alexs-mobs", "textures/entity/example_mob.png")

// GeckoLib models
new ResourceLocation("xeenaa-alexs-mobs", "geo/example_mob.geo.json")

// GeckoLib animations
new ResourceLocation("xeenaa-alexs-mobs", "animations/example_mob.animation.json")

// Item textures
new ResourceLocation("xeenaa-alexs-mobs", "textures/item/example_item.png")

// Sounds
new ResourceLocation("xeenaa-alexs-mobs", "example_sound")

// Loot tables
new ResourceLocation("xeenaa-alexs-mobs", "entities/example_mob")
```

## Path Resolution

### Assets (Client-side)
- **Base Path**: `assets/xeenaa-alexs-mobs/`
- **File Extensions**: Required in ResourceLocation for direct file references
- **Examples**:
  - Texture: `assets/xeenaa-alexs-mobs/textures/entity/capybara.png`
  - Model: `assets/xeenaa-alexs-mobs/geo/capybara.geo.json`
  - Animation: `assets/xeenaa-alexs-mobs/animations/capybara.animation.json`

### Data (Server-side)
- **Base Path**: `data/xeenaa-alexs-mobs/`
- **File Extensions**: `.json` (automatically appended by game)
- **Examples**:
  - Loot Table: `data/xeenaa-alexs-mobs/loot_table/entities/capybara.json`
  - Recipe: `data/xeenaa-alexs-mobs/recipe/example_recipe.json`

## Naming Conventions

- **File Names**: Use `snake_case` (e.g., `test_animal.png`, `flying_fish.geo.json`)
- **Entity Resources**: Group by entity name for easy identification
  - `geo/capybara.geo.json`
  - `animations/capybara.animation.json`
  - `textures/entity/capybara.png`
  - `loot_table/entities/capybara.json`

## Build Integration

All resources in this directory are automatically included in the mod JAR by Fabric Loom's `processResources` task.
