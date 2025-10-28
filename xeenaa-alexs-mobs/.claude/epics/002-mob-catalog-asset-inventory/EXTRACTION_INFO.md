# Alex's Mobs JAR Extraction Information

## Version Details

- **Mod Name**: Alex's Mobs
- **Version**: 1.22.9
- **Minecraft Version**: 1.20.1
- **Mod Loader**: Forge & NeoForge
- **Download Source**: Modrinth (https://modrinth.com/mod/alexs-mobs/version/1.22.9)
- **Download Date**: 2025-10-26
- **JAR File Size**: 26,350,793 bytes (25.1 MB)
- **JAR File Location**: `C:\Users\canya\Documents\projects\Minecraft\xeenaa-alexs-mobs\.claude\epics\02-mob-catalog-asset-inventory\alexsmobs-1.22.9.jar`
- **Extraction Location**: `C:\Users\canya\Documents\projects\Minecraft\xeenaa-alexs-mobs\.claude\epics\02-mob-catalog-asset-inventory\alexsmobs-extracted\`

## Changelog Highlights (Version 1.22.9)

Published: September 6, 2024

### New Features
- Added new model and texture for shoebills
- Added numerous new tags for items, blocks, and mob interactions
- Added multithreaded pathfinding for resource-intensive mobs:
  - Elephant
  - Bison
  - Leafcutter Ant
  - Potoo
  - Gorilla
  - Rocky Roller
  - Snow Leopard
  - Tusklin
- Added tag blacklist for entities catfish cannot eat

### Bug Fixes
- Fixed pupfish chunk detection not timing out after five minutes (server lag fix)
- Fixed acacia blossom drop rate not being configurable
- Fixed numerous crashes (sugar glider, underminer, EntityModel, anteater, rocky roller)
- Fixed laviathan movement issues (speed, diagonal movement, lava performance)
- Fixed void worm bugs (AI segments, attribute modifiers)
- Fixed numerous item/entity interaction bugs

### Translations
- Updated Turkish, French, Spanish, Ukrainian, Polish translations

## Directory Structure Overview

```
alexsmobs-extracted/
├── assets/
│   └── alexsmobs/
│       ├── blockstates/          # Block state definitions
│       ├── book/                 # In-game encyclopedia/book content
│       ├── lang/                 # Language files (23 files)
│       ├── models/               # Block and item models (438 files)
│       │   ├── block/
│       │   └── item/
│       ├── particles/            # Particle definitions
│       ├── skate_model_mappings/ # Custom model mappings
│       ├── sounds/               # Sound files (575 files)
│       ├── sounds.json           # Sound registry (67.8 KB)
│       └── textures/             # Textures (756 files)
│           ├── armor/
│           ├── block/
│           ├── entity/           # 405 entity textures (MOBS!)
│           ├── gui/
│           ├── item/
│           ├── mob_effect/
│           ├── painting/
│           └── particle/
│
├── data/
│   └── alexsmobs/
│       ├── advancements/         # Achievement/advancement definitions
│       ├── capsid_recipes/       # Custom recipe type (capsid)
│       ├── damage_type/          # Custom damage types
│       ├── forge/                # Forge-specific data
│       ├── loot_modifiers/       # Global loot table modifiers
│       ├── loot_tables/          # Entity/block loot tables
│       ├── recipes/              # Crafting recipes
│       ├── tags/                 # Item/block/entity tags
│       └── worldgen/             # World generation data
│
├── com/                          # Java source code (NOT extracted for analysis)
├── META-INF/                     # JAR metadata and manifests
└── pack.mcmeta                   # Resource pack metadata
```

## Asset Statistics

### Assets (assets/alexsmobs/)
- **Total Textures**: 756 files
  - **Entity Textures**: 405 files (mob skins, variants, states)
  - **Block/Item/GUI/etc**: 351 files
- **Total Models**: 438 files
  - Block models
  - Item models
  - (Note: Entity models appear to be code-based, not JSON)
- **Sound Files**: 575 files
- **Language Files**: 23 translations

### Data (data/alexsmobs/)
- Advancements
- Custom recipes (capsid system)
- Damage types
- Loot tables
- Tags (comprehensive tagging system)
- World generation data

## Notable Findings

### Entity Textures Preview
Sample mobs identified from texture files:
- Alligator Snapping Turtle (with moss variant)
- Anaconda (multiple variants, shedding states)
- Anteater
- Bald Eagle
- Banana Slug
- Bison (adult, baby, snowy variants)
- Blobfish (pressurized/depressurized states)
- Blue Jay
- Bone Serpent (head, mid, tail segments)
- Bunfungus (sleeping, transforming variants)
- Cachalot Whale
- ... and many more

### Model System
- Alex's Mobs appears to use **code-based entity models** (not GeckoLib geo.json files)
- No `.geo.json` or GeckoLib animation files found in assets
- This means our Fabric port will need to:
  1. Either recreate models from scratch using GeckoLib
  2. Or reverse-engineer the original model code from the JAR's Java classes
  3. Models are likely defined in Java code (com/ directory)

### Sound System
- 575 sound files (.ogg audio)
- Comprehensive sounds.json registry (67.8 KB)
- Sounds will need to be analyzed and potentially recreated/licensed

### Tagging System
- Extensive use of tags for mod compatibility
- Tags for items, blocks, entities, biomes
- Important for integration with other mods

## Dependencies

According to the Modrinth API data, Alex's Mobs requires:
- **Citadel** (project_id: jJfV67b1) - REQUIRED dependency
  - Citadel is a library mod that provides animation and model support
  - This explains the absence of GeckoLib - they use Citadel instead!

## License & Usage Notes

- **License**: GPL-3.0 (as per original mod)
- **Purpose**: This extraction is for REFERENCE ONLY
- **Scope**: Asset analysis for Fabric port planning
- **Restrictions**:
  - Java code NOT analyzed (avoiding decompilation concerns)
  - Assets will be recreated or properly attributed in final port
  - This is pure research for understanding scope and requirements

## Next Steps

1. **Mob Catalog Creation** (TASK-002)
   - Analyze entity textures to identify all mobs
   - Create comprehensive list with variants
   - Document mob categories (passive, neutral, hostile)

2. **Asset Inventory** (TASK-003)
   - Catalog all textures by mob
   - Identify animation requirements
   - List sound requirements per mob

3. **Technical Analysis**
   - Research Citadel library (animation system)
   - Plan GeckoLib migration strategy
   - Evaluate model recreation vs. code analysis approach

## References

- **Modrinth Page**: https://modrinth.com/mod/alexs-mobs
- **Version Page**: https://modrinth.com/mod/alexs-mobs/version/1.22.9
- **Download URL**: https://cdn.modrinth.com/data/2cMuAZAp/versions/XoIASRVU/alexsmobs-1.22.9.jar
- **File Hash (SHA1)**: dbf93d2f1da19e16739e91e8ea0fc6f4bc2f228f
- **File Hash (SHA512)**: b50280057926d750f9ffbf6a8df36deb37529356895f6bf59f25d272ecf69afccec65ec1a5c3a8bfad24f9475c15b93c5ec4666447b1fce26a90c41445bbfb04

---

**Extraction completed successfully on 2025-10-26**
**No extraction errors encountered**
**Directory structure verified and matches expected layout**
