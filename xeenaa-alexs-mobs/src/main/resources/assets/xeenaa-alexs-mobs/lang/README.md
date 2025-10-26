# Translations Directory

This directory contains language files for translating entity names, item names, and other text.

## Purpose
Language files provide localized names and descriptions for all mod content, supporting multiple languages.

## File Format
- **Extension**: `.json`
- **Format**: Key-value pairs (translation keys → localized text)
- **Encoding**: UTF-8 (supports international characters)

## File Naming Convention
Language files use ISO 639-1 language codes and ISO 3166-1 country codes:

**Format**: `language_country.json`

**Examples**:
- `en_us.json` - English (United States)
- `en_gb.json` - English (United Kingdom)
- `es_es.json` - Spanish (Spain)
- `es_mx.json` - Spanish (Mexico)
- `fr_fr.json` - French (France)
- `de_de.json` - German (Germany)
- `ja_jp.json` - Japanese (Japan)
- `zh_cn.json` - Chinese (Simplified)

## Translation Key Formats

### Entity Names
**Format**: `entity.<mod_id>.<entity_name>`

**Examples**:
```json
{
  "entity.xeenaa-alexs-mobs.test_animal": "Test Animal",
  "entity.xeenaa-alexs-mobs.capybara": "Capybara",
  "entity.xeenaa-alexs-mobs.flying_fish": "Flying Fish"
}
```

### Item Names
**Format**: `item.<mod_id>.<item_name>`

**Examples**:
```json
{
  "item.xeenaa-alexs-mobs.lasso": "Lasso",
  "item.xeenaa-alexs-mobs.animal_feed": "Animal Feed",
  "item.xeenaa-alexs-mobs.capybara_spawn_egg": "Capybara Spawn Egg"
}
```

### Block Names
**Format**: `block.<mod_id>.<block_name>`

**Examples**:
```json
{
  "block.xeenaa-alexs-mobs.animal_nest": "Animal Nest"
}
```

### Item Group (Creative Tab)
**Format**: `itemGroup.<mod_id>.<group_name>`

**Examples**:
```json
{
  "itemGroup.xeenaa-alexs-mobs.main": "Xeenaa's Alex's Mobs"
}
```

### Subtitles (for sounds)
**Format**: `subtitles.<mod_id>.<sound_name>`

**Examples**:
```json
{
  "subtitles.entity.capybara.ambient": "Capybara squeaks",
  "subtitles.entity.capybara.hurt": "Capybara hurts",
  "subtitles.entity.capybara.death": "Capybara dies"
}
```

### Death Messages
**Format**: `death.attack.<damage_type>`

**Examples**:
```json
{
  "death.attack.capybara": "%1$s was bitten by a Capybara"
}
```

### Advancements and Tooltips
**Format**: `advancement.<mod_id>.<advancement_name>.title`

**Examples**:
```json
{
  "advancement.xeenaa-alexs-mobs.find_capybara.title": "Friend Shaped",
  "advancement.xeenaa-alexs-mobs.find_capybara.description": "Encounter a Capybara",
  "item.xeenaa-alexs-mobs.lasso.tooltip": "Use to capture passive mobs"
}
```

## Integration
Translation keys are automatically resolved by Minecraft based on the player's language setting.

**Entity registration** (auto-generates translation key):
```java
Registry.register(Registries.ENTITY_TYPE, new ResourceLocation("xeenaa-alexs-mobs", "capybara"),
    CAPYBARA);
// Automatically looks for: "entity.xeenaa-alexs-mobs.capybara"
```

**Item registration** (auto-generates translation key):
```java
Registry.register(Registries.ITEM, new ResourceLocation("xeenaa-alexs-mobs", "lasso"),
    new Item(new Item.Settings()));
// Automatically looks for: "item.xeenaa-alexs-mobs.lasso"
```

## Base Language
**Primary**: `en_us.json` (English - United States)

All translation keys must exist in `en_us.json`. Other languages are optional and fall back to English if missing.

## Best Practices
- **Complete en_us.json first**: Ensure all keys exist in base language
- **Use descriptive text**: Make names clear and intuitive
- **Consistent capitalization**: Follow Minecraft's title case conventions
- **Test in-game**: Verify translations appear correctly
- **Community translations**: Accept contributions for additional languages
- **Avoid hardcoded text**: Always use translation keys in code

## Adding New Languages
1. Create new language file (e.g., `es_es.json`)
2. Copy structure from `en_us.json`
3. Translate all values (keep keys unchanged)
4. Test in-game by changing language settings

## Example: Complete en_us.json
```json
{
  "entity.xeenaa-alexs-mobs.test_animal": "Test Animal",
  "item.xeenaa-alexs-mobs.test_animal_spawn_egg": "Test Animal Spawn Egg",
  "itemGroup.xeenaa-alexs-mobs.main": "Xeenaa's Alex's Mobs"
}
```
