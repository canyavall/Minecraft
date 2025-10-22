# Minecraft Fabric Mod Template

This is a basic template for creating Fabric mods for Minecraft 1.21.1.

## Quick Start

1. **Copy this template** to a new folder for your mod
2. **Update mod information** in `gradle.properties`:
   - `mod_version` - Your mod version
   - `maven_group` - Your package group (e.g., `com.yourname`)
   - `archives_base_name` - Your mod's name (lowercase, no spaces)

3. **Update fabric.mod.json**:
   - Change `id` to your mod ID
   - Update `name` and `description`
   - Update `authors` with your name
   - Update `entrypoints` to match your package structure

4. **Rename packages and classes**:
   - Rename `com.example.helloworld` to your package structure
   - Update the main mod class name
   - Update mixins.json with your package name

5. **Build the mod**:
   ```bash
   ./gradlew build
   ```
   The compiled JAR will be in `build/libs/`

6. **Test the mod**:
   ```bash
   ./gradlew runClient
   ```

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/           # Main mod code
│   │   └── resources/       # Resources (lang files, textures, etc.)
│   │       ├── assets/      # Client assets
│   │       └── data/        # Server data (recipes, loot tables, etc.)
│   └── client/
│       └── java/           # Client-only code
├── build.gradle            # Build configuration
├── gradle.properties       # Project properties
└── settings.gradle        # Gradle settings
```

## Example Features

This template includes:
- Basic mod initialization
- A simple item registration example
- Client-side initialization
- Language file setup
- Mixin configuration (ready for use)

## Dependencies

- Minecraft 1.21.1
- Fabric Loader 0.16.7+
- Fabric API 0.116.5+
- Java 21

## Common Tasks

### Add a new item
1. Create the item in your main mod class
2. Register it in `onInitialize()`
3. Add translations to `lang/en_us.json`
4. Add textures to `assets/yourmodid/textures/item/`
5. Add model to `assets/yourmodid/models/item/`

### Add a mixin
1. Create mixin class in `com.yourpackage.mixin`
2. Add the mixin to `yourmod.mixins.json`
3. Target the vanilla class you want to modify

### Add a recipe
1. Create recipe JSON in `data/yourmodid/recipes/`
2. Follow vanilla recipe format

## Useful Commands

- `./gradlew build` - Build the mod
- `./gradlew runClient` - Run Minecraft with the mod
- `./gradlew runServer` - Run dedicated server with the mod
- `./gradlew clean` - Clean build files
- `./gradlew genSources` - Generate Minecraft sources for IDE

## License

This template is provided under the MIT license. Update the LICENSE file with your preferred license for your mod.