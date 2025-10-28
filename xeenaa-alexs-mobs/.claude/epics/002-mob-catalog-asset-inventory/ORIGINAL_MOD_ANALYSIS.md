# Original Mod Analysis - Alex's Mobs

## Mod Information

- **Version**: 1.22.9
- **Loader**: NeoForge/Forge
- **Minecraft Version**: 1.20.1
- **Dependencies**: Citadel (REQUIRED - animation/model library)
- **Release Date**: September 6, 2024
- **License**: GPL-3.0
- **Total JAR Size**: 26.35 MB

## Directory Structure

### Assets Directory (`assets/alexsmobs/`)

```
assets/alexsmobs/
├── blockstates/                  # Block state JSON definitions (37 files)
│   ├── banana_peel.json
│   ├── bison_carpet.json
│   ├── crocodile_egg.json
│   └── transmutation_table.json
│
├── book/                         # In-game Animal Dictionary
│   └── animal_dictionary/        # Encyclopedia entries for each mob
│       ├── *.json                # 91 animal entries + root.json
│       └── <lang>/               # Localized text files for 14 languages
│           ├── en_us/
│           ├── es_es/
│           ├── ja_jp/
│           ├── zh_cn/
│           └── ... (11+ more)
│
├── lang/                         # Language translation files (23 files)
│   ├── en_us.json
│   ├── es_es.json
│   ├── ja_jp.json
│   └── ... (20+ more languages)
│
├── models/                       # Block and item models (JSON)
│   ├── block/                    # Block models (196 files)
│   └── item/                     # Item models (242 files)
│
├── particles/                    # Particle effect definitions (JSON)
│   ├── acid_bubble.json
│   ├── bone_serpent_spawn.json
│   └── ... (20+ particle types)
│
├── skate_model_mappings/         # Custom model mapping system (unique to Alex's Mobs)
│   └── ... (texture variant mappings)
│
├── sounds/                       # Sound files directory (575 OGG files)
│   ├── <mob>_idle_<n>.ogg       # Idle sounds
│   ├── <mob>_hurt_<n>.ogg       # Hurt sounds
│   ├── <mob>_die_<n>.ogg        # Death sounds
│   ├── <mob>_attack_<n>.ogg     # Attack sounds
│   └── music_disc/               # Music disc tracks
│
├── sounds.json                   # Sound event registry (67.8 KB, comprehensive)
│
└── textures/                     # All textures (756 PNG files total)
    ├── armor/                    # Armor layer textures (28 files)
    │   ├── centipede_leggings.png
    │   ├── crocodile.png
    │   └── ... (various armor types)
    │
    ├── block/                    # Block textures (89 files)
    │   ├── banana_peel.png
    │   ├── bison_fur_block.png
    │   └── ...
    │
    ├── entity/                   # MOB TEXTURES (405 files - PRIMARY ASSETS)
    │   ├── <mob>.png             # Base mob texture
    │   ├── <mob>_<variant>.png   # Variant textures
    │   ├── <mob>/                # Subdirectory for complex mobs
    │   │   ├── <mob>.png
    │   │   ├── <state>.png       # State-based textures
    │   │   └── <variant>.png
    │   └── ... (see detailed breakdown below)
    │
    ├── gui/                      # GUI and interface textures (11 files)
    │   ├── animal_dictionary.png
    │   ├── capsid_ui.png
    │   └── transmutation_table.png
    │
    ├── item/                     # Item textures (164 files)
    │   ├── <item>.png
    │   └── ... (weapons, tools, food, materials)
    │
    ├── mob_effect/               # Status effect icons (12 files)
    │   ├── ender_flu.png
    │   ├── fear.png
    │   └── ...
    │
    ├── painting/                 # Painting textures (7 files)
    │   ├── tusklin.png
    │   └── ...
    │
    └── particle/                 # Particle textures (40 files)
        ├── soul_particle.png
        └── ...
```

### Data Directory (`data/alexsmobs/`)

```
data/alexsmobs/
├── advancements/                 # Achievement/advancement definitions
│   └── alexsmobs/                # Mod-specific advancements
│       └── *.json                # Individual advancement files
│
├── capsid_recipes/               # Custom recipe type (capsid crafting system)
│   └── *.json                    # Special transformation recipes
│
├── damage_type/                  # Custom damage type definitions (1.19.4+)
│   └── *.json                    # Unique damage types for mod mechanics
│
├── forge/                        # Forge-specific data generation
│   ├── biome_modifier/           # Biome spawn configuration
│   └── structure_modifier/       # Structure generation modifiers
│
├── loot_modifiers/               # Global loot table modifiers
│   └── *.json                    # Inject items into vanilla loot
│
├── loot_tables/                  # Loot table definitions
│   ├── blocks/                   # Block drop loot (23 files)
│   ├── entities/                 # MOB DROP LOOT (104 files)
│   └── gameplay/                 # Special gameplay loot (fishing, etc.)
│
├── recipes/                      # Crafting/smelting recipes (100+ files)
│   ├── animal_dictionary.json
│   ├── <item>.json
│   └── ... (crafting, smelting, campfire, smoking)
│
├── tags/                         # Data tags for mod compatibility
│   ├── banner_pattern/           # Banner pattern tags
│   ├── blocks/                   # Block tags (mineable, etc.)
│   ├── entity_types/             # Entity behavior tags (30 files)
│   │   ├── passive_land_animals.json
│   │   ├── anaconda_targets.json
│   │   └── ... (targeting, behavior tags)
│   ├── items/                    # Item tags (compatibility)
│   └── worldgen/                 # World generation tags
│       ├── biome/                # Biome spawn tags
│       └── structure/            # Structure generation tags
│
└── worldgen/                     # World generation features
    ├── configured_feature/       # Feature configurations
    └── placed_feature/           # Feature placement rules
```

## Naming Conventions

### Entity Textures

**Pattern Analysis**: Entity textures follow a hierarchical organization strategy:

1. **Simple Mobs** (single texture or few variants):
   - `<mob_name>.png` - Base texture
   - `<mob_name>_<variant>.png` - Color/type variant
   - `<mob_name>_<state>.png` - State-based variant (sleeping, angry, etc.)

2. **Complex Mobs** (many variants or states):
   - `<mob_name>/` - Subdirectory for organization
   - `<mob_name>/<mob_name>.png` - Base texture
   - `<mob_name>/<variant>.png` - Variant textures
   - `<mob_name>/<state>.png` - State textures
   - `<mob_name>/overlay/<effect>.png` - Overlay textures

**Examples**:

```
# Simple mobs (root directory)
alligator_snapping_turtle.png
alligator_snapping_turtle_moss.png           # Environmental variant
anaconda.png
anaconda_yellow.png                          # Color variant
anaconda_shedding.png                        # State variant
anaconda_yellow_shedding.png                 # Combined variant + state
bison.png
bison_baby.png                               # Age variant
bison_snowy.png                              # Biome variant
bison_baby_snowy.png                         # Combined age + biome
bison_sheared.png                            # State variant
blobfish.png
blobfish_pressurized.png                     # State variant
blobfish_depressurized.png                   # State variant
blue_jay.png
blue_jay_shiny.png                           # Rare variant overlay

# Complex mobs (subdirectory organization)
banana_slug/banana_slug_0.png                # Numbered variants (0-3)
banana_slug/banana_slug_1.png
banana_slug/banana_slug_2.png
banana_slug/banana_slug_3.png
banana_slug/banana_slug_slime.png            # Special state

cachalot/cachalot_whale.png                  # Base
cachalot/cachalot_whale_albino.png           # Rare variant
cachalot/cachalot_whale_sleeping.png         # State
cachalot/cachalot_whale_albino_sleeping.png  # Combined variant + state
cachalot/whale_echo_0.png                    # Effect frames (0-3)
cachalot/whale_echo_0_green.png              # Effect variant

elephant/elephant.png                         # Base
elephant/elephant_chest.png                   # Equipment overlay
elephant/elephant_tusks.png                   # Feature overlay
elephant/decor/black.png                      # Decoration variants (16 colors)
elephant/decor/blue.png
elephant/decor/trader.png                     # Special decor

tiger/tiger.png                               # Base
tiger/tiger_white.png                         # Color variant
tiger/tiger_sleeping.png                      # State
tiger/tiger_angry.png                         # Emotion state
tiger/tiger_eyes.png                          # Glow overlay
tiger/tiger_angry_eyes.png                    # Combined state + glow
tiger/tiger_white_sleeping.png                # Combined variant + state
tiger/tiger_white_angry.png                   # Combined variant + state
tiger/tiger_white_eyes.png                    # Combined variant + glow

# Multi-part entities (segmented mobs)
bone_serpent_head.png                         # Head segment
bone_serpent_mid.png                          # Middle segment
bone_serpent_tail.png                         # Tail segment
```

**Variant Types Identified**:
- **Color variants**: `_yellow`, `_white`, `_albino`, `_blue`
- **Age variants**: `_baby`
- **Biome variants**: `_snowy`
- **State variants**: `_sleeping`, `_angry`, `_shedding`, `_sheared`, `_pressurized`
- **Equipment overlays**: `_chest`, `_tusks`, `_hood`, `_crown`
- **Glow overlays**: `_eyes`, `_glow`, `_overlay`
- **Special effects**: `_spit`, `_blood`, `_fly`
- **Numbered variants**: `_0`, `_1`, `_2`, `_3` (random variants)
- **Decoration variants**: `decor/<color>` (for customizable mobs)

### Sound Files

**Pattern**: `<mob_name>_<event>_<index>.ogg`

**Sound Event Types**:
- `_idle_<n>` - Ambient sounds (2-4 variants per mob)
- `_hurt_<n>` - Damage sounds (1-3 variants)
- `_die_<n>` or `_death_<n>` - Death sounds (1-2 variants)
- `_attack_<n>` - Attack sounds (1-3 variants)
- `_<special>_<n>` - Unique mob-specific sounds

**Examples**:
```
alligator_snapping_turtle_idle_0.ogg
alligator_snapping_turtle_idle_1.ogg
alligator_snapping_turtle_idle_2.ogg
alligator_snapping_turtle_hurt_0.ogg
alligator_snapping_turtle_hurt_1.ogg

anaconda_attack_0.ogg
anaconda_attack_1.ogg
anaconda_hurt_0.ogg
anaconda_hurt_1.ogg
anaconda_slither_0.ogg                        # Unique movement sound
anaconda_slither_1.ogg
anaconda_slither_2.ogg
anaconda_slither_3.ogg

bald_eagle_hurt_0.ogg
bald_eagle_hurt_1.ogg
bald_eagle_idle_0.ogg
bald_eagle_idle_1.ogg
bald_eagle_idle_2.ogg

roadrunner_idle_0.ogg
roadrunner_idle_1.ogg
roadrunner_hurt_0.ogg
roadrunner_hurt_1.ogg
roadrunner_meep.ogg                           # Special reference sound
```

**Sound Registry** (`sounds.json`):
- 67.8 KB comprehensive registry
- Sound events map to multiple file variants
- Categories: `neutral`, `hostile`, `ambient`, `music`
- Includes subtitles for accessibility

### Loot Tables

**Pattern**: `entities/<mob_name>.json`

**Examples**:
```
entities/anaconda.json                        # Minimal/empty (size: 2 bytes)
entities/bison.json                           # Complex multi-pool loot
entities/catfish.json                         # Base loot
entities/catfish_large.json                   # Size variant loot
entities/catfish_medium.json                  # Size variant loot
entities/crocodile.json                       # Multi-item drops
entities/centipede_head.json                  # Segmented mob loot
entities/centipede_body.json                  # Empty (no drops)
entities/centipede_tail.json                  # Empty (no drops)
```

**Notable Pattern**: Some mobs have size-variant loot tables:
- `catfish.json`, `catfish_large.json`, `catfish_medium.json`
- Different loot based on entity size/age

### Recipes

**Pattern**: `<output_item>.json` or `<output>_from_<input>.json`

**Examples**:
```
animal_dictionary.json                        # Main item recipe
bison_fur_block.json                          # Material recipe
bison_fur_from_block.json                     # Reverse crafting
bison_fur_to_wool.json                        # Conversion recipe
boiled_emu_egg.json                           # Furnace recipe
boiled_emu_egg_campfire.json                  # Campfire variant
boiled_emu_egg_smoke.json                     # Smoker variant
cooked_catfish.json                           # Cooking recipe
cooked_catfish_campfire.json                  # Campfire cooking
cooked_catfish_smoke.json                     # Smoker cooking
```

**Recipe Types**:
- Standard crafting (shaped/shapeless)
- Furnace smelting
- Campfire cooking
- Smoker cooking
- Special "capsid" recipes (custom recipe type)

### Animal Dictionary Entries

**Pattern**: `book/animal_dictionary/<mob_name>.json`

**Structure**:
```json
{
  "parent": "root.json",
  "text": "<mob_name>.txt",
  "title": "entity.alexsmobs.<mob_name>",
  "linked_page_buttons": [],
  "images": [],
  "item_renders": [],
  "recipes": [
    {
      "recipe": "alexsmobs:<recipe_id>",
      "shapeless": false,
      "x": 210,
      "y": 20,
      "page": 1,
      "scale": 1.3
    }
  ],
  "tabula_renders": [],
  "entity_renders": [
    {
      "entity": "alexsmobs:<mob_name>",
      "x": 90,
      "y": 75,
      "page": 0,
      "scale": 1.7,
      "rot_x": 30,
      "rot_y": 225,
      "rot_z": 0,
      "follow_cursor": false
    }
  ]
}
```

**Localized Text**: Each entry references a `.txt` file in language-specific subdirectories:
- `en_us/<mob_name>.txt` - English description
- `es_es/<mob_name>.txt` - Spanish description
- `ja_jp/<mob_name>.txt` - Japanese description
- ... (14 languages total)

### Entity Tags

**Pattern**: `tags/entity_types/<behavior_tag>.json`

**Examples**:
```
passive_land_animals.json                     # List of passive mobs
neutral_land_animals.json                     # List of neutral mobs
anaconda_targets.json                         # What anacondas can attack
bald_eagle_targets.json                       # Eagle prey list
crocodile_targets.json                        # Crocodile prey
bunfungus_ignores.json                        # Entities bunfungus ignores
fly_annoy_targets.json                        # Who flies annoy
ignores_kimono.json                           # Mobs that ignore kimono armor
villagers.json                                # Villager entity list
```

**Purpose**: Defines mob behaviors, targeting, and interactions.

## Key Patterns

### Resource Organization

1. **Hierarchical Texture System**:
   - Simple mobs: Flat structure in `textures/entity/`
   - Complex mobs: Subdirectories for organization
   - Overlays and effects: Separate textures composited in code

2. **Variant Handling**:
   - **Multiple files**: Each variant is a separate texture file
   - **Naming convention**: Variants use `_<descriptor>` suffix
   - **Combination variants**: Multiple descriptors combined (e.g., `tiger_white_angry_eyes.png`)
   - **Numbered variants**: Random variants use `_0`, `_1`, `_2`, `_3` suffix

3. **Localization Strategy**:
   - 14+ languages supported
   - Animal dictionary has per-language text files
   - Standard language JSON files for UI/items/entities

4. **Sound Organization**:
   - All sounds in flat directory structure
   - Naming convention provides all organization
   - Multiple variants per sound event (randomization)
   - Central `sounds.json` registry maps events to files

### Variant Handling

**Three-Tier System**:

1. **Basic Variants** (color, type):
   - File: `<mob>_<variant>.png`
   - Example: `anaconda_yellow.png`, `tiger_white.png`

2. **State Variants** (behavioral, environmental):
   - File: `<mob>_<state>.png`
   - Example: `bison_sheared.png`, `tiger_sleeping.png`

3. **Combined Variants** (variant + state):
   - File: `<mob>_<variant>_<state>.png`
   - Example: `tiger_white_sleeping.png`, `anaconda_yellow_shedding.png`

**Overlay System** (additive textures):
- Base texture + overlay texture
- Example: `tiger.png` + `tiger_eyes.png` (glow effect)
- Example: `elephant.png` + `elephant_chest.png` (equipment)

**Numbered Variants** (random selection):
- Pattern: `<mob>_<n>.png` where n = 0, 1, 2, 3
- Example: `capuchin_monkey_0.png` through `capuchin_monkey_3.png`
- Example: `banana_slug_0.png` through `banana_slug_3.png`

**Decoration System** (player customization):
- Pattern: `<mob>/decor/<color>.png`
- Example: `elephant/decor/red.png`, `elephant/decor/blue.png`
- 16 dye colors + special variants (e.g., `trader`)

### File Formats

- **Textures**: PNG format (all)
- **Models**: JSON format (blocks/items only)
- **Sounds**: OGG Vorbis format (all)
- **Data files**: JSON format (all)
- **Entity models**: Code-based (Citadel library, not in assets/)
- **Animations**: Code-based (Citadel library, not in assets/)

**Notable Absence**: No GeckoLib `.geo.json` or `.animation.json` files found. Alex's Mobs uses Citadel for models/animations.

### Multi-Part Entities

Some mobs consist of multiple segments with separate textures:

**Bone Serpent** (3 parts):
- `bone_serpent_head.png`
- `bone_serpent_mid.png`
- `bone_serpent_tail.png`

**Cave Centipede** (3 parts):
- `cave_centipede.png` (body texture used for all segments)
- `cave_centipede_eyes.png` (glow overlay)
- Loot tables: `centipede_head.json`, `centipede_body.json`, `centipede_tail.json`

**Void Worm** (complex multi-part):
- Multiple textures in `void_worm/` subdirectory
- Portal effects, shattered effects

### Size/Age Variants

**Catfish** (3 sizes):
- Textures: `catfish_small.png`, `catfish_medium.png`, `catfish_large.png`
- State variants: `_spit` suffix for each size
- Loot tables: `catfish.json`, `catfish_medium.json`, `catfish_large.json`

**Baby Variants**:
- Naming: `<mob>_baby.png`
- Example: `bison_baby.png`, `bison_baby_snowy.png`

## Notable Observations

### Animation System (Citadel vs GeckoLib)

**Citadel Library** (Alex's Mobs' choice):
- Code-based model and animation system
- Models defined in Java code (com/github/alexthe666/alexsmobs/...)
- Animations controlled via Java code
- No external JSON model files
- Tight integration with Forge/NeoForge
- **Advantage**: Better performance, more control
- **Disadvantage**: Harder to port, requires code analysis

**GeckoLib** (our Fabric port target):
- JSON-based model system (`*.geo.json`)
- JSON-based animation system (`*.animation.json`)
- Loader-agnostic (works on Fabric, Forge, Quilt)
- **Advantage**: Easier porting, visual tools available (Blockbench)
- **Disadvantage**: Need to recreate all models from scratch

**Porting Implication**: We cannot directly use Alex's Mobs' model files. All 91+ mob models must be recreated in GeckoLib format.

### In-Game Encyclopedia

**Animal Dictionary**:
- Comprehensive in-game documentation for all mobs
- 91 mob entries
- Each entry includes:
  - Localized description text (14 languages)
  - 3D entity render preview
  - Related recipes
  - Optional images/renders
- **Porting Note**: This is a major feature that adds educational value

### Custom Recipe System

**Capsid Recipes**:
- Custom recipe type: `alexsmobs:capsid`
- Special crafting mechanic unique to mod
- Recipes in `data/alexsmobs/capsid_recipes/`
- **Porting Note**: Will need Fabric equivalent or redesign

### Comprehensive Tagging

**Entity Behavior Tags** (30 files):
- Passive/neutral categorization
- Predator-prey relationships (`<mob>_targets.json`)
- Fear/ignore behaviors
- Equipment interactions
- **Porting Note**: Essential for mob AI and interactions

### Forge-Specific Features

**Biome Modifiers** (`data/alexsmobs/forge/biome_modifier/`):
- Controls mob spawning in biomes
- **Porting Note**: Fabric uses different system (Fabric API biome modifications)

**Structure Modifiers** (`data/alexsmobs/forge/structure_modifier/`):
- Adds mobs to vanilla structures
- **Porting Note**: Fabric equivalent needed

### Performance Optimizations (v1.22.9)

**Multithreaded Pathfinding**:
- Added for resource-intensive mobs:
  - Elephant, Bison, Gorilla, Snow Leopard
  - Leafcutter Ant, Rocky Roller, Tusklin
  - Potoo
- **Porting Note**: Fabric equivalent or alternative optimization needed

### Sound Complexity

**571 Sound Files**:
- Average 5-7 sounds per mob (idle, hurt, die, attack, special)
- Music disc tracks
- Ambient sounds
- Special effect sounds
- **Porting Note**: Major asset requirement, licensing concerns

### Texture Count

**402 Entity Textures**:
- 91 mobs with multiple variants each
- Average ~4.5 textures per mob
- Some mobs have 10+ variants (tiger, elephant, etc.)
- **Porting Note**: Texture recreation feasible, art asset scope

## Porting Implications

### Critical Challenges

1. **Model Recreation** (HIGHEST PRIORITY):
   - All 91 mob models must be recreated in GeckoLib format
   - Original Citadel models are code-based, not accessible
   - Options:
     - Reverse-engineer from Java code (legal gray area)
     - Recreate from scratch using reference screenshots
     - Use Blockbench to create GeckoLib models
   - **Estimate**: 40-80 hours per complex mob, 10-20 hours per simple mob

2. **Animation Recreation** (HIGHEST PRIORITY):
   - All animations must be recreated in GeckoLib
   - Citadel animations are code-controlled
   - Need to observe original mod behavior and replicate
   - **Estimate**: Similar time to models (combined task)

3. **Sound Assets** (LEGAL CONCERN):
   - 575 OGG files total
   - GPL-3.0 license permits reuse BUT:
     - Original sounds may be third-party assets
     - License may not cover sound files
   - **Options**:
     - Contact original author for permission
     - Create/commission new sounds
     - Use royalty-free sound libraries
   - **Estimate**: High effort if recreation needed

4. **Texture Assets** (MODERATE CONCERN):
   - 405 entity textures
   - GPL-3.0 license permits reuse BUT:
     - Better to recreate to avoid attribution issues
     - Opportunity to improve/modernize
   - **Options**:
     - Reuse with proper attribution (if legally sound)
     - Recreate in similar style
     - Commission new textures
   - **Estimate**: Moderate effort, 2-4 hours per mob

5. **Forge-to-Fabric Porting**:
   - Biome modifiers → Fabric API biome modifications
   - Structure modifiers → Fabric structure API
   - Recipe types → Fabric recipe API
   - Entity attributes → Fabric entity attributes
   - **Estimate**: Moderate complexity, well-documented

### Phased Approach Recommended

**Phase 1: Proof of Concept** (1-5 mobs):
- Choose 5 representative mobs (simple, medium, complex)
- Create GeckoLib models and animations
- Implement basic behaviors
- Test Fabric APIs
- **Goal**: Validate porting feasibility

**Phase 2: Core Mobs** (20-30 mobs):
- Prioritize most popular/iconic mobs
- Establish model creation pipeline
- Develop reusable behavior systems
- **Goal**: Playable alpha release

**Phase 3: Complete Port** (remaining 60+ mobs):
- Port remaining mobs systematically
- Polish behaviors and interactions
- Complete feature parity
- **Goal**: Full release

**Phase 4: Enhancements** (post-parity):
- Add Fabric-specific features
- Improve performance
- Add new content
- **Goal**: Exceed original mod

### Easiest Mobs to Port First

**Simple Mobs** (good starting points):
1. **Crow** - Simple bird, basic AI
2. **Cockroach** - Small passive mob
3. **Fly** - Tiny, simple animations
4. **Blue Jay** - Simple bird variant
5. **Raccoon** - Medium complexity, popular

**Medium Complexity**:
1. **Bison** - Ground mob, simple AI, variants
2. **Grizzly Bear** - Neutral mob, moderate AI
3. **Crocodile** - Water/land, medium complexity
4. **Alligator Snapping Turtle** - Unique model, medium

**Complex Mobs** (save for later):
1. **Void Worm** - Multi-part, complex animations
2. **Bone Serpent** - Multi-part, complex AI
3. **Elephant** - Large, complex interactions
4. **Cachalot Whale** - Huge, complex AI

### Asset Reuse Strategy

**Can Reuse (with attribution)**:
- ✅ Loot table structures (data-driven)
- ✅ Recipe patterns (data-driven)
- ✅ Tag organization (data-driven)
- ✅ Naming conventions (not copyrightable)
- ✅ Spawn rules/worldgen (data-driven)

**Should Recreate**:
- ⚠️ Entity textures (legal safety + quality control)
- ⚠️ Sound files (licensing unclear)
- ⚠️ Models (not accessible, different format)
- ⚠️ Animations (not accessible, different format)

**Must Recreate**:
- ❌ Java code (clean-room implementation)
- ❌ AI behaviors (observe and reimplement)
- ❌ Custom mechanics (original implementation)

### Technical Dependencies

**Fabric Mods Needed**:
- ✅ Fabric API (core)
- ✅ GeckoLib (models/animations)
- ⚠️ Cloth Config (configuration UI) - optional
- ⚠️ REI/JEI integration (recipe viewing) - optional

**Minecraft Systems Used**:
- Entity spawning and despawning
- AI goal system (custom goals needed)
- World generation (features, biome modifications)
- Loot tables and loot modifiers
- Recipe system (custom recipe types)
- Damage types (1.19.4+ feature)
- Entity attributes
- Status effects

### Recommended Tools

**Model Creation**:
- **Blockbench** - GeckoLib model editor (FREE)
- Visual model creation with animation timeline
- Direct GeckoLib export

**Texture Creation**:
- **Aseprite** - Pixel art editor (PAID)
- **GIMP** - Free alternative
- **Paint.NET** - Free Windows alternative

**Sound Creation**:
- **Audacity** - Free audio editor
- **Freesound.org** - Royalty-free sound library
- **Epidemic Sound** - Paid sound library

**Development**:
- **IntelliJ IDEA** - Java IDE (Community Edition FREE)
- **Fabric API documentation**
- **GeckoLib documentation and examples**

## Recommendations for Porting

### Strategic Recommendations

1. **Start Small, Prove Feasibility**:
   - Port 3-5 simple mobs first
   - Validate GeckoLib workflow
   - Confirm legal approach (clean-room vs. attribution)
   - Measure time/effort per mob

2. **Establish Asset Pipeline Early**:
   - Decide on texture approach (reuse vs. recreate)
   - Resolve sound asset licensing
   - Set up Blockbench workflow
   - Create model/animation templates

3. **Prioritize by Popularity**:
   - Survey community for most-wanted mobs
   - Port iconic mobs early (marketing value)
   - Save obscure mobs for later

4. **Build Reusable Systems**:
   - Generic AI goals (follow, flee, hunt, etc.)
   - Standard animations (walk, idle, attack, die)
   - Common behaviors (breeding, taming, feeding)
   - **Goal**: Reduce per-mob implementation time

5. **Legal Due Diligence**:
   - Contact original author (alexthe666) for guidance
   - Clarify asset licensing (especially sounds)
   - Consider legal review of GPL-3.0 compliance
   - Document clean-room process if needed

6. **Community Engagement**:
   - Open source from day one
   - Accept community contributions (textures, models)
   - Regular development updates
   - Beta testing program

### Technical Recommendations

1. **Use Data-Driven Design**:
   - Externalize mob stats (health, damage, speed)
   - JSON config files for behaviors
   - Easy balancing and modpack customization

2. **Modular Mob System**:
   - Each mob is self-contained module
   - Can enable/disable individual mobs
   - Reduces complexity during development
   - Allows incremental releases

3. **Performance from Day One**:
   - Profile early and often
   - Implement pathfinding optimizations
   - Efficient entity rendering
   - Chunk loading considerations

4. **Fabric API Best Practices**:
   - Use Fabric's entity attributes
   - Leverage Fabric's networking
   - Follow Fabric's recipe API
   - Use Fabric's biome API

5. **Testing Infrastructure**:
   - Unit tests for AI logic
   - Integration tests for spawning
   - Manual testing checklist per mob
   - Performance benchmarks

### Content Recommendations

**Phase 1 Mob Selection** (Proof of Concept):
1. **Crow** - Simplest, well-loved
2. **Raccoon** - Medium complexity, iconic
3. **Grizzly Bear** - Neutral mob, combat testing
4. **Crocodile** - Water/land testing
5. **Blue Jay** - Variant testing

**Phase 2 High-Priority Mobs** (Community Favorites):
- Capuchin Monkey (item interactions)
- Kangaroo (unique movement)
- Orca (aquatic, majestic)
- Snow Leopard (cool factor)
- Elephant (rideable, iconic)
- Roadrunner (speed, fun)
- Bald Eagle (falconry)
- Gorilla (strength)
- Platypus (unique)
- Komodo Dragon (venomous)

**Lower Priority** (Complex or Niche):
- Void Worm (boss, very complex)
- Bone Serpent (multi-part)
- Underminer (special mechanics)
- Mimicube (mimic mechanics)
- Warped Mosco (nether boss)

### Quality Standards

**Before Marking a Mob "Complete"**:
- ✅ Model matches original (90%+ visual parity)
- ✅ Animations are smooth and natural
- ✅ AI behavior matches original (major patterns)
- ✅ Sounds are present (original or replacement)
- ✅ Loot tables functional
- ✅ Spawning works in correct biomes
- ✅ Animal Dictionary entry complete
- ✅ No major bugs or crashes
- ✅ Performance acceptable (no lag spikes)

**Stretch Goals**:
- ⭐ Improved animations over original
- ⭐ Additional variants
- ⭐ Enhanced AI behaviors
- ⭐ Fabric-specific features
- ⭐ Integration with other Fabric mods

## Complete Mob List (91 Mobs)

1. Alligator Snapping Turtle
2. Anaconda
3. Anteater
4. Bald Eagle
5. Banana Slug
6. Bison
7. Blobfish
8. Blue Jay
9. Bone Serpent
10. Bunfungus
11. Cachalot Whale
12. Caiman
13. Capuchin Monkey
14. Catfish
15. Cave Centipede
16. Cockroach
17. Comb Jelly
18. Cosmaw
19. Cosmic Cod
20. Crimson Mosquito
21. Crocodile
22. Crow
23. Devil's Hole Pupfish
24. Dropbear
25. Elephant
26. Emu
27. Endergrade
28. Enderiophage
29. Farseer
30. Flutter
31. Fly
32. Flying Fish
33. Frilled Shark
34. Froststalker
35. Gazelle
36. Gelada Monkey
37. Giant Squid
38. Gorilla
39. Grizzly Bear
40. Guster
41. Hammerhead Shark
42. Hummingbird
43. Jerboa
44. Kangaroo
45. Komodo Dragon
46. Laviathan
47. Leafcutter Ant
48. Lobster
49. Maned Wolf
50. Mantis Shrimp
51. Mimic Octopus
52. Mimicube
53. Moose
54. Mudskipper
55. Mungus
56. Murmur
57. Orca
58. Platypus
59. Potoo
60. Raccoon
61. Rain Frog
62. Rattlesnake
63. Rhinoceros
64. Roadrunner
65. Rocky Roller
66. Sea Bear
67. Seagull
68. Seal
69. Shoebill
70. Skelewag
71. Skreecher
72. Skunk
73. Snow Leopard
74. Soul Vulture
75. Spectre
76. Straddler
77. Stradpole
78. Sugar Glider
79. Sunbird
80. Tarantula Hawk
81. Tasmanian Devil
82. Terrapin
83. Tiger
84. Toucan
85. Triops
86. Tusklin
87. Underminer
88. Void Worm
89. Warped Mosco
90. Warped Toad
91. (Unknown/Additional - verify count)

**Mob Categories**:
- **Passive**: 40+ mobs (herbivores, small animals)
- **Neutral**: 30+ mobs (defend when provoked)
- **Hostile**: 20+ mobs (aggressive)
- **Aquatic**: 15+ mobs (water-based)
- **Flying**: 10+ mobs (birds, flying creatures)
- **Arthropods**: 8+ mobs (insects, spiders)
- **Bosses**: 3 mobs (Void Worm, Warped Mosco, Bone Serpent?)

---

## Summary

Alex's Mobs is a **massive content mod** with 91 unique mobs, 571 sounds, 402 entity textures, and comprehensive game integration. Porting to Fabric is **feasible but requires substantial effort**:

- **Model/Animation Recreation**: 500-2000 hours (biggest challenge)
- **Texture Assets**: Reusable or recreatable (100-200 hours)
- **Sound Assets**: Licensing concerns, may need recreation (100-300 hours)
- **Code Porting**: Standard Forge→Fabric work (300-500 hours)
- **Testing/Polish**: Per-mob testing (200-400 hours)

**Total Estimate**: 1200-3400 hours (1-3 person-years)

**Recommended Approach**:
1. Incremental releases (5 mobs → 20 mobs → full port)
2. Community contributions encouraged
3. Legal consultation for asset reuse
4. Focus on quality over speed

**Biggest Win**: The data-driven approach (loot tables, tags, recipes) is directly portable. The biggest challenge is model/animation recreation, which is creative work, not technical porting.
