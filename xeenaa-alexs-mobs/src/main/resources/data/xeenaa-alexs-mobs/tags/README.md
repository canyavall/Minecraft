# Tags Directory

This directory contains tag definitions for grouping entities, items, and blocks into logical categories.

## Purpose
Tags provide flexible categorization for game content, allowing:
- Conditional logic based on groups (e.g., "all aquatic mobs")
- Recipe ingredients that accept multiple items (e.g., "any planks")
- Biome spawning rules (e.g., "spawn in all forest biomes")
- Consistent behavior across similar entities/items

## File Format
- **Extension**: `.json`
- **Format**: Vanilla tag JSON structure
- **Structure**: List of resource locations

## Directory Organization

```
tags/
├── entity_types/          # Entity type tags
├── items/                 # Item tags
├── blocks/                # Block tags
├── biomes/                # Biome tags (for spawning)
└── damage_types/          # Damage type tags
```

## Naming Convention
`category_name.json`

**Examples**:
- `entity_types/aquatic_animals.json`
- `entity_types/tameable.json`
- `items/animal_food.json`
- `items/mob_drops.json`
- `biomes/capybara_spawns.json`

## Basic Structure

```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:capybara",
    "xeenaa-alexs-mobs:flying_fish",
    "xeenaa-alexs-mobs:crocodile"
  ]
}
```

**Parameters**:
- **replace**: If `true`, replaces vanilla/other mod tags entirely. Usually `false` to append.
- **values**: List of resource locations to include in tag

## Tag Types

### Entity Type Tags

**File**: `entity_types/aquatic_animals.json`
```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:flying_fish",
    "xeenaa-alexs-mobs:seal",
    "xeenaa-alexs-mobs:orca"
  ]
}
```

**Use Cases**:
- Group entities by habitat (aquatic, forest, desert)
- Group by behavior (passive, neutral, hostile)
- Group by interaction (tameable, breedable)

**File**: `entity_types/tameable.json`
```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:capybara",
    "xeenaa-alexs-mobs:parrot_variant"
  ]
}
```

### Item Tags

**File**: `items/animal_food.json`
```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:animal_feed",
    "minecraft:wheat",
    "minecraft:carrot",
    "minecraft:apple"
  ]
}
```

**Use Cases**:
- Crafting recipe ingredients
- Food items for taming/breeding
- Items that drop from specific mob types

**File**: `items/capybara_food.json`
```json
{
  "replace": false,
  "values": [
    "minecraft:grass",
    "minecraft:wheat",
    "minecraft:melon_slice",
    "xeenaa-alexs-mobs:special_grass"
  ]
}
```

### Block Tags

**File**: `blocks/animal_spawnable_on.json`
```json
{
  "replace": false,
  "values": [
    "minecraft:grass_block",
    "minecraft:dirt",
    "minecraft:podzol",
    "minecraft:mycelium"
  ]
}
```

**Use Cases**:
- Valid spawn surfaces for entities
- Blocks entities can path through
- Blocks affected by mod mechanics

### Biome Tags

**File**: `biomes/capybara_spawns.json`
```json
{
  "replace": false,
  "values": [
    "minecraft:plains",
    "minecraft:sunflower_plains",
    "minecraft:river",
    "minecraft:swamp",
    "minecraft:mangrove_swamp"
  ]
}
```

**Use Cases**:
- Define where entities can spawn
- Environmental requirements
- Biome-specific behaviors

## Integration with Code

### Entity Behavior
Check if entity is in a tag:

```java
if (entity.getType().isIn(ModTags.AQUATIC_ANIMALS)) {
    // Apply aquatic-specific behavior
}
```

### Breeding Food
Check if item is valid breeding food:

```java
@Override
public boolean isBreedingItem(ItemStack stack) {
    return stack.isIn(ModTags.CAPYBARA_FOOD);
}
```

### Spawn Conditions
Check if biome supports spawning:

```java
public static boolean canSpawn(EntityType<?> type, ServerWorldAccess world,
                               SpawnReason spawnReason, BlockPos pos,
                               RandomGenerator random) {
    return world.getBiome(pos).isIn(ModTags.CAPYBARA_SPAWN_BIOMES);
}
```

### Recipe Ingredients
Use tag in recipe (see recipe/README.md):

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "tag": "xeenaa-alexs-mobs:animal_food"
    }
  ],
  "result": {
    "item": "xeenaa-alexs-mobs:taming_treat"
  }
}
```

## Creating Tag References in Code

**File**: `src/main/java/com/canya/alexsmobs/tags/ModTags.java`

```java
package com.canya.alexsmobs.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    // Entity tags
    public static final TagKey<EntityType<?>> AQUATIC_ANIMALS =
        TagKey.of(RegistryKeys.ENTITY_TYPE,
            new Identifier("xeenaa-alexs-mobs", "aquatic_animals"));

    public static final TagKey<EntityType<?>> TAMEABLE =
        TagKey.of(RegistryKeys.ENTITY_TYPE,
            new Identifier("xeenaa-alexs-mobs", "tameable"));

    // Item tags
    public static final TagKey<Item> ANIMAL_FOOD =
        TagKey.of(RegistryKeys.ITEM,
            new Identifier("xeenaa-alexs-mobs", "animal_food"));

    public static final TagKey<Item> CAPYBARA_FOOD =
        TagKey.of(RegistryKeys.ITEM,
            new Identifier("xeenaa-alexs-mobs", "capybara_food"));
}
```

## Vanilla Tags
You can add mod content to existing vanilla tags:

**File**: `items/fishes.json` (extends vanilla tag)
```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:flying_fish"
  ]
}
```

**Common Vanilla Tags**:
- `minecraft:fishes`
- `minecraft:animals_spawnable_on`
- `minecraft:logs`
- `minecraft:planks`
- `minecraft:meat`

## Optional Tags
Tags can reference entries that don't exist yet (for compatibility):

```json
{
  "replace": false,
  "values": [
    "xeenaa-alexs-mobs:future_entity",
    "#minecraft:passive_mobs",
    {
      "id": "other_mod:compatible_entity",
      "required": false
    }
  ]
}
```

**Syntax**:
- `"xeenaa-alexs-mobs:entity"` - Direct reference
- `"#minecraft:tag"` - Reference another tag
- `{"id": "...", "required": false}` - Optional entry (won't error if missing)

## Tag Inheritance
Tags can include other tags:

**File**: `entity_types/all_animals.json`
```json
{
  "replace": false,
  "values": [
    "#xeenaa-alexs-mobs:aquatic_animals",
    "#xeenaa-alexs-mobs:land_animals",
    "#xeenaa-alexs-mobs:flying_animals"
  ]
}
```

## Best Practices
- **Use `replace: false`**: Avoid breaking other mods' tags
- **Descriptive names**: Use clear, self-explanatory tag names
- **Logical grouping**: Group by shared behavior, not arbitrary categories
- **Vanilla compatibility**: Extend vanilla tags when appropriate
- **Document in code**: Create `ModTags.java` for easy reference
- **Test tags**: Verify tags work in recipes and spawn conditions
- **Consider modpacks**: Make tags compatible with other mods

## Common Tag Patterns

### Habitat-Based Entity Tags
```
entity_types/aquatic_animals.json
entity_types/forest_animals.json
entity_types/desert_animals.json
entity_types/snow_animals.json
```

### Behavior-Based Entity Tags
```
entity_types/tameable.json
entity_types/breedable.json
entity_types/neutral.json
entity_types/boss_mobs.json
```

### Food Item Tags
```
items/carnivore_food.json
items/herbivore_food.json
items/omnivore_food.json
items/breeding_food.json
```

### Drop Item Tags
```
items/mob_drops.json
items/rare_drops.json
items/boss_drops.json
```
