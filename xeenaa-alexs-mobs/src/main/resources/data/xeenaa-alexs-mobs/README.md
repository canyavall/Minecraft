# Data Directory

Server-side resources for the Xeenaa's Alex's Mobs mod.

## Directory Structure

```
data/xeenaa-alexs-mobs/
├── loot_table/
│   └── entities/          # Entity loot tables
├── recipe/                # Crafting recipes
└── tags/                  # Resource tags
```

## Resource Types

### Loot Tables (loot_table/entities/)
- **Format**: `.json`
- **Purpose**: Define what items entities drop when killed
- **Naming**: `entity_name.json` (e.g., `capybara.json`)
- **Structure**: Follows vanilla loot table format

**Example Structure**:
```json
{
  "type": "minecraft:entity",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "minecraft:beef",
          "functions": [
            {
              "function": "minecraft:set_count",
              "count": {
                "min": 1,
                "max": 3
              }
            },
            {
              "function": "minecraft:looting_enchant",
              "count": {
                "min": 0,
                "max": 1
              }
            }
          ]
        }
      ]
    }
  ]
}
```

### Recipes (recipe/)
- **Format**: `.json`
- **Purpose**: Define crafting recipes for mod items
- **Naming**: `item_name_recipe.json`
- **Types**: `crafting_shaped`, `crafting_shapeless`, `smelting`, etc.

**Example (Shapeless)**:
```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "minecraft:string"
    },
    {
      "item": "minecraft:stick"
    }
  ],
  "result": {
    "item": "xeenaa-alexs-mobs:lasso",
    "count": 1
  }
}
```

### Tags (tags/)
- **Format**: `.json`
- **Purpose**: Group entities/items/blocks for logical categorization
- **Subdirectories**: `entity_types/`, `items/`, `blocks/`
- **Examples**: `entity_types/aquatic.json`, `items/animal_food.json`

**Example (Entity Type Tag)**:
```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:capybara",
    "xeenaa-alexs-mobs:flying_fish"
  ]
}
```

## Integration with Code

Data resources are loaded automatically by Minecraft's data pack system and referenced by `ResourceLocation`:

```java
// Loot table is automatically assigned to entity
@Override
public ResourceLocation getLootTable() {
    return new ResourceLocation("xeenaa-alexs-mobs", "entities/capybara");
}

// Tags can be checked in code
if (entity.getType().isIn(TagKey.of(RegistryKeys.ENTITY_TYPE,
    new ResourceLocation("xeenaa-alexs-mobs", "aquatic")))) {
    // Entity is tagged as aquatic
}
```

## Loading Priority

Data resources are loaded in this order:
1. Vanilla data packs
2. Mod data (this directory)
3. World-specific data packs
4. User-added data packs

Later data packs can override earlier ones unless `"replace": false` is specified in tags.
