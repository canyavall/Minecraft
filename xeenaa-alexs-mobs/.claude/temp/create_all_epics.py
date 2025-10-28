#!/usr/bin/env python3
"""Generate epic folders with requirements.md for all 90 Alex's Mobs animals"""

import os
import re

# All 90 mobs from catalog (lines 33-122 from CATALOG.md)
MOBS_DATA = """
1|Alligator Snapping Turtle|Defensive|Swamp|Medium|Aquatic, bite attack when provoked
2|Anaconda|Placatable|Swamp, Jungle|Medium|Constriction mechanic, color variants
3|Anteater|Neutral|Jungle, Savanna|Medium|Attacks leaf cutter ants
4|Bald Eagle|Neutral|Mountains, Grove|Complex|Falconry system, hood equipment
5|Banana Slug|Passive|Forest (wet)|Simple|4 random color variants, slime trail
6|Bison|Neutral|Plains, Meadow|Complex|Rideable, shearable, baby variants
7|Blobfish|Passive|Deep Ocean|Simple|Pressure variants (depressurized)
8|Blue Jay|Passive|Forest, Birch Forest|Simple|Flying passive, shiny variant
9|Bone Serpent|Placatable|Lava Ocean (Nether)|Very Complex|Multi-part entity (head/mid/tail)
10|Bunfungus|Neutral|Mushroom Fields|Medium|Sleeping, transforming states
11|Cachalot Whale|Neutral|Ocean (Deep)|Very Complex|Huge size, albino variant, echolocation
12|Caiman|Neutral|Swamp, Mangrove Swamp|Medium|Aquatic predator
13|Capuchin Monkey|Neutral|Jungle|Complex|Item interactions, 4 random variants
14|Catfish|Passive|River, Swamp|Medium|3 size variants (small/medium/large)
15|Cave Centipede|Placatable|Caves|Very Complex|Multi-segment entity, glow overlay
16|Cockroach|Passive|Underground, Buildings|Simple|Small pest, simple AI
17|Comb Jelly|Passive|Frozen Ocean|Simple|3 color variants + glow overlay
18|Cosmaw|Neutral|The End|Medium|Floating End dimension mob
19|Cosmic Cod|Passive|The End|Simple|Swimming fish variant
20|Crimson Mosquito|Placatable|Crimson Forest (Nether)|Medium|Blood variants, fly state
21|Crocodile|Placatable|Swamp, River|Complex|2 variants, crown equipment
22|Crow|Passive|Plains, Forest, Taiga|Medium|Tameable, item gathering, shoulder perch
23|Devil's Hole Pupfish|Passive|Desert (rare)|Simple|Tiny rare fish
24|Dropbear|Hostile|Old Growth Taiga|Medium|Ambush predator, glow eyes
25|Elephant|Neutral|Savanna|Very Complex|Rideable, inventory system, 17 decor variants
26|Emu|Neutral|Badlands, Savanna|Medium|Baby variants, 3 color types
27|Endergrade|Passive|The End|Simple|Rideable with saddle
28|Enderiophage|Defensive|The End, Nether, Overworld|Medium|3 dimension variants with glow
29|Farseer|Hostile|Ancient City|Very Complex|Portal mechanics, transmutation table
30|Flutter|Passive|Lush Caves|Simple|Butterfly-like, glow overlay
31|Fly|Passive|Multiple biomes|Simple|Tiny pest, minimal AI
32|Flying Fish|Passive|Ocean (Warm)|Simple|3 random variants
33|Frilled Shark|Neutral|Deep Ocean|Medium|Depressurized variant, kaiju variant
34|Froststalker|Placatable|Ice Spikes, Frozen Peaks|Medium|Ice projectiles, spike variants
35|Gazelle|Passive|Savanna|Simple|Herd animal, fleeing behavior
36|Gelada Monkey|Neutral|Mountain biomes|Medium|Leader variant, angry states
37|Giant Squid|Neutral|Deep Ocean|Complex|Huge size, 2 color variants, depressurized
38|Gorilla|Neutral|Jungle, Bamboo Jungle|Complex|Silverback leader, special variants (DK/Funky)
39|Grizzly Bear|Defensive|Forest, Taiga|Complex|Tameable, rideable, honey interaction, cubs
40|Guster|Hostile|Desert, Badlands|Medium|Sandstorm, 3 variants + glow overlays
41|Hammerhead Shark|Placatable|Warm Ocean|Medium|Aquatic predator
42|Hummingbird|Passive|Jungle, Forest|Simple|Tiny flying, 3 random variants
43|Jerboa|Passive|Desert|Simple|Small rodent, sleeping state
44|Kangaroo|Neutral|Badlands, Savanna|Complex|Unique movement, pouch mechanic
45|Komodo Dragon|Placatable|Sparse Jungle, Badlands|Complex|Rideable, saddle + maid variant
46|Laviathan|Passive|Lava Ocean (Nether)|Complex|Huge lava creature, obsidian variant, gear/helmet
47|Leafcutter Ant|Neutral|Jungle|Complex|Queen variant, leaf carrying (3 variants), angry
48|Lobster|Neutral|Ocean (Cold)|Medium|6 color variants
49|Maned Wolf|Passive|Savanna|Simple|Ender variant
50|Mantis Shrimp|Neutral|Warm Ocean|Medium|4 random variants, powerful punch
51|Mimic Octopus|Passive|Warm Ocean|Complex|Mimicry system (5 forms), overlay effects
52|Mimicube|Hostile|The End|Medium|Mimic mechanics, outer overlay
53|Moose|Neutral|Snowy Plains, Taiga|Complex|Rideable, antlered variants, snowy variants
54|Mudskipper|Passive|Mangrove Swamp|Simple|Amphibious, spit projectile
55|Mungus|Passive|Mushroom Fields|Complex|Sack equipment, shoes, special beam
56|Murmur|Hostile|Deep Dark|Medium|Sculk-related, angry state
57|Orca|Neutral|Cold/Frozen Ocean|Complex|4 directional variants (NE/NW/SE/SW)
58|Platypus|Defensive|River, Swamp|Simple|Aquatic, fedora/perry variants
59|Potoo|Passive|Jungle|Simple|Camouflage bird
60|Raccoon|Neutral|Forest, Taiga|Medium|Item theft, bandana/rigby variants, glow eyes
61|Rain Frog|Passive|Desert (rain only)|Simple|3 random variants
62|Rattlesnake|Defensive|Desert, Badlands|Simple|Venomous bite
63|Rhinoceros|Defensive|Savanna|Complex|Charge attack, potion overlay, angry
64|Roadrunner|Passive|Desert, Badlands|Simple|Fast movement, meep variant
65|Rocky Roller|Hostile|Dripstone Caves|Medium|Rolling attack, angry state
66|Sea Bear|Joke|Ocean (rare)|Medium|Joke mob, special spawn conditions
67|Seagull|Passive|Beach, Ocean|Simple|Flying scavenger, wingull variant
68|Seal|Passive|Cold Ocean, Frozen Ocean|Medium|2 arctic variants, 2 brown variants, baby/crying
69|Shoebill|Passive|Swamp|Simple|Static bird
70|Skelewag|Placatable|Shipwreck, Ocean|Medium|2 pirate variants
71|Skreecher|Hostile|Deep Dark|Medium|Sculk-related, glow overlay
72|Skunk|Defensive|Forest, Plains|Simple|Spray defense mechanism
73|Snow Leopard|Neutral|Mountains, Snowy Slopes|Medium|Stealth predator, sleeping state
74|Soul Vulture|Placatable|Soul Sand Valley (Nether)|Medium|3 flame variants + glow
75|Spectre|Passive|The End|Medium|Spectral entity, bone/glint/glow overlays
76|Straddler|Hostile|Basalt Deltas (Nether)|Medium|Lava strider
77|Stradpole|Passive|Basalt Deltas (Nether)|Simple|Baby form of Straddler
78|Sugar Glider|Passive|Birch Forest|Simple|Gliding, tameable
79|Sunbird|Defensive|Mountains (peak)|Medium|Fire-based, 3 variants (base/glow/shine)
80|Tarantula Hawk|Neutral|Desert, Badlands|Medium|Nether variant, baby, angry states
81|Tasmanian Devil|Neutral|Forest|Simple|Aggressive scavenger, angry state
82|Terrapin|Passive|Swamp, River|Complex|6 base colors + 6 shell patterns + 4 skin patterns
83|Tiger|Placatable|Jungle, Bamboo Jungle|Complex|White variant, sleeping/angry, glow eyes
84|Toucan|Passive|Jungle|Simple|4 random variants + 2 special (gold/sam)
85|Triops|Passive|Desert (water)|Simple|Small aquatic crustacean
86|Tusklin|Placatable|Ice Spikes, Frozen Peaks|Complex|Rideable, saddle + hooves overlay
87|Underminer|Neutral|Underground|Medium|2 variants + dwarf variant
88|Warped Toad|Neutral|Warped Forest (Nether)|Medium|3 variants (base/glow/pepe) with blink states
89|Void Worm|Boss|The End|Very Complex|Boss mob, multi-part, portal mechanics
90|Warped Mosco|Chief|Warped Forest (Nether)|Very Complex|Chief/mini-boss, glow overlay
"""

def slug(name):
    """Convert mob name to slug format"""
    return name.lower().replace("'", "").replace(" ", "-")

def get_category_slug(category):
    """Get slug for behavior category"""
    category_map = {
        "Passive": "passive",
        "Neutral": "neutral",
        "Defensive": "defensive",
        "Placatable": "placatable",
        "Hostile": "hostile",
        "Boss": "boss",
        "Chief": "boss",
        "Joke": "joke"
    }
    return category_map.get(category, "unknown")

def get_complexity_effort(complexity):
    """Get effort estimate based on complexity"""
    efforts = {
        "Simple": "2-3 days",
        "Medium": "3-5 days",
        "Complex": "5-7 days",
        "Very Complex": "7-14 days"
    }
    return efforts.get(complexity, "TBD")

def generate_requirements(num, name, category, biomes, complexity, features):
    """Generate requirements.md content"""
    epic_num = str(num + 10).zfill(2)  # Start at 11 since we have 01-10
    effort = get_complexity_effort(complexity)
    slug_name = slug(name)
    class_name = name.replace(" ", "").replace("'", "")

    return f"""# Epic {epic_num}: {name} - {category} Animal

**Status**: NOT STARTED
**Epic Type**: Single Animal Port
**Complexity**: {complexity}
**Priority**: Medium
**Estimated Effort**: {effort}

---

## Overview

Port the **{name}** entity from Alex's Mobs (Forge/Citadel) to Fabric/GeckoLib.

**Behavior**: {category}
**Biome(s)**: {biomes}
**Key Features**: {features}

---

## Business Value

### Player Experience Goals

1. **Content Addition** - Adds unique wildlife to Minecraft world
   - Players encounter {name.lower()} in {biomes.lower()}
   - Enriches biodiversity and exploration rewards
   - Creates memorable encounters

2. **Authentic Behavior** - Behaves according to its category ({category.lower()})
   - {category}-appropriate AI and interactions
   - Realistic animations and sounds
   - Environment-appropriate spawning

3. **Feature Implementation** - Key mechanics: {features}

### Game Mechanics

**Entity Type**: {category} Animal
**Size**: TBD (check original model)
**Behavior**: {category.lower()}
**Health**: TBD
**Drops**: TBD
**Spawning**: {biomes}

---

## Features & Functionality

### Core Features

1. **3D Animated Model**
   - Accurate anatomy for {name.lower()}
   - All body parts properly modeled
   - Appropriate texturing and colors

2. **Animations**
   - Idle animation
   - Movement animation (walk/swim/fly)
   - Attack animation (if applicable)
   - Special animations based on features
   - Death animation

3. **AI Behavior**
   - {category}-appropriate goal system
   - Environment interaction
   - Player interaction based on category
   - Special behaviors: {features}

4. **Sound Effects**
   - Ambient sounds
   - Movement sounds
   - Attack/hurt sounds (if applicable)
   - Death sound

5. **Rendering**
   - Proper hitbox sizing
   - Shadow rendering
   - Special effects (if applicable)
   - Variant textures: TBD

---

## Success Criteria

**Epic is COMPLETE when:**

- [ ] {name} entity is summonable (`/summon xeenaa-alexs-mobs:{slug_name}`)
- [ ] Model appears correctly with all body parts visible
- [ ] Animations play smoothly
- [ ] AI works according to {category} behavior
- [ ] Spawns naturally in appropriate biomes
- [ ] Size and positioning look correct
- [ ] Performance is acceptable
- [ ] Sounds play appropriately
- [ ] All special features implemented: {features}
- [ ] User validates in-game appearance and behavior

---

## Technical Scope

### In Scope

- {class_name}Entity class
- {class_name}Model class (GeckoLib model)
- {class_name}Renderer class (GeckoLib renderer)
- Geometry file ({slug_name}.geo.json)
- Animation file ({slug_name}.animation.json)
- Texture file(s) ({slug_name}.png + variants)
- Entity registration in ModEntities
- Renderer registration in AlexsMobsClient
- Spawn egg (creative tab)
- Sound effects
- Special features: {features}

### Out of Scope

- Advanced breeding mechanics (unless required)
- Complex equipment systems (unless in features)
- Multiple biome variants (unless specified)

---

## Performance Requirements

**Target Specifications**:
- Support multiple entities in loaded chunks
- No noticeable FPS drop
- Render time: <0.2ms per entity
- Entity tick time: <0.15ms per entity
- Memory: <5KB per entity

---

## Risks & Mitigations

**Potential Risks**:
1. **Complexity**: {complexity} - May require significant development time
   - Mitigation: Break into subtasks, reference similar mobs
2. **Special Features**: {features}
   - Mitigation: Research original implementation, study vanilla equivalents
3. **Performance**: Complex entities may impact performance
   - Mitigation: Profile and optimize, limit spawn rates

---

## Dependencies

**Required**:
- ✅ Epic 01: GeckoLib framework setup
- ✅ Epic 02: {name} cataloged in mob inventory
- ✅ Epic 03: Conversion process established

---

## Deliverables

### Code Files
- `{class_name}Entity.java`
- `{class_name}Model.java`
- `{class_name}Renderer.java`

### Resource Files
- `{slug_name}.geo.json`
- `{slug_name}.animation.json`
- `{slug_name}.png` (+ variants)
- Sound files

### Documentation
- Conversion notes
- Feature implementation details

---

## Timeline

**Estimated Timeline**: {effort}

---

## Validation & Testing

**Manual Testing Required**:
- [ ] Spawns correctly via `/summon`
- [ ] Spawns naturally in biomes
- [ ] Model renders properly
- [ ] All animations play correctly
- [ ] AI behaves according to {category} type
- [ ] Special features work: {features}
- [ ] Sounds play appropriately
- [ ] Size and scale correct
- [ ] Performance acceptable

---

## Notes

**Key Considerations**:
- Behavior: {category}
- Complexity: {complexity}
- Special features: {features}
- Biomes: {biomes}

**References**:
- Epic 03: Conversion process
- Epic 04: First completed mob (Fly)
- Original Alex's Mobs implementation
- Catalog entry: CATALOG.md line {num + 32}

---

**Created**: 2025-10-27
**Status**: 📋 **AWAITING START**
"""

def main():
    base_path = r"C:\Users\canya\Documents\projects\Minecraft\xeenaa-alexs-mobs\.claude\epics"

    # Parse mob data
    mobs = []
    for line in MOBS_DATA.strip().split('\n'):
        if not line:
            continue
        parts = line.split('|')
        if len(parts) == 6:
            mobs.append({
                'num': int(parts[0]),
                'name': parts[1],
                'category': parts[2],
                'biomes': parts[3],
                'complexity': parts[4],
                'features': parts[5]
            })

    print(f"Processing {len(mobs)} mobs...")

    for mob in mobs:
        # Epic number starts at 11 (we have 01-10 already)
        epic_num = str(mob['num'] + 10).zfill(2)
        slug_name = slug(mob['name'])
        category_slug = get_category_slug(mob['category'])

        folder_name = f"{epic_num}-{slug_name}-{category_slug}"
        folder_path = os.path.join(base_path, folder_name)

        # Create folder
        os.makedirs(folder_path, exist_ok=True)

        # Generate requirements.md
        requirements_content = generate_requirements(
            mob['num'],
            mob['name'],
            mob['category'],
            mob['biomes'],
            mob['complexity'],
            mob['features']
        )

        requirements_path = os.path.join(folder_path, "requirements.md")
        with open(requirements_path, 'w', encoding='utf-8') as f:
            f.write(requirements_content)

        print(f"✅ Created {folder_name}/requirements.md")

    print(f"\n✅ Successfully created {len(mobs)} epic folders!")

if __name__ == "__main__":
    main()
