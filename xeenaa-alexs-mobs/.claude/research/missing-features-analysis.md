# Alex's Mobs - Missing Features Analysis

**Researched**: 2025-10-27
**Minecraft Version**: 1.21.1 (porting from 1.20.1)
**Related Project**: xeenaa-alexs-mobs
**Purpose**: Identify missing features in epic requirements for accurate implementation planning

## Research Question

**Question**: What gameplay features, mechanics, and behaviors are missing from our epic requirements.md files that exist in the original Alex's Mobs implementation?

**Context**: User noticed that some requirements.md files are missing critical features like taming, item gathering, shoulder perching (Crow), and other special mechanics. Need to audit all 90+ animals and update requirements with complete feature lists.

---

## Summary (For Agents)

**Quick Answer**: Most epic requirements.md files generated from CATALOG.md are missing detailed feature implementations. The catalog lists high-level features (e.g., "Tameable, item gathering, shoulder perch") but requirements need specific implementation details extracted from animal dictionary entries and decompiled entity code.

**Key Findings**:
- **91 animals cataloged** with comprehensive features extracted from animal dictionary
- **Crow example**: Requirements missing 8+ critical features (hay block healing, container deposit system, carved pumpkin fleeing, shoulder perching mechanics, item frame matching, extra undead damage, 4 command modes)
- **Common missing features**: Taming methods/items, breeding items, special AI modes, equipment systems, container interactions, damage modifiers, environmental triggers
- **Complex systems**: Many animals have multi-step mechanics (Blue Jay+Raccoon bonding, Elephant passenger system, Bald Eagle falconry, Mimic Octopus transformations) not captured in basic requirements

**Recommended Approach**:
1. Use `mob-features-comprehensive-catalog.md` as source of truth
2. For each epic, cross-reference catalog entry with requirements.md
3. Update requirements with ALL features from catalog (taming, special mechanics, items, AI behaviors, equipment, breeding, variants, combat)
4. Add implementation notes for complex systems (multi-step interactions, NBT data, custom goals)

---

## Detailed Findings

### Investigation Methods

1. **Animal Dictionary Analysis**: Read all 91 `.txt` files from `alexsmobs-extracted/assets/alexsmobs/book/animal_dictionary/en_us/`
2. **Decompiled Code Review**: Examined EntityCrow.java to understand implementation details
3. **Catalog Cross-Reference**: Compared CATALOG.md high-level features with detailed dictionary descriptions
4. **Requirements Audit**: Reviewed generated requirements.md files for completeness

### Critical Discovery: Feature Granularity Gap

**Problem**: Generated requirements.md files use condensed feature lists from CATALOG.md:
```
**Key Features**: Tameable, item gathering, shoulder perch
```

**Reality**: Actual implementation requires detailed mechanics:
```
Taming:
- Method: Toss Pumpkin Seeds
- 4 Command Modes: Stay/Wander/Follow/Gather
- Shoulder perching in follow mode (remove by sneaking)

Item Gathering System:
- Flies around hay block (gather mode)
- Picks up items within radius
- Matches item to item frames on containers
- Deposits from correct side into container

Special Blocks:
- Hay block: Health regeneration when sitting
- Hay block: Center point for gathering radius
- Carved Pumpkin: Fleeing trigger (pest control)

Combat:
- Defends owner (low damage pecks)
- Extra damage to undead targets
- Low enough damage not to aggro mobs

Environmental:
- Damages crops (pest behavior)
- Scared by carved pumpkins
- Picks up and eats food items
```

### Missing Features by Category

#### 1. Taming Systems (19 tameable mobs)

**Missing from requirements**:
- Exact taming item (e.g., Pumpkin Seeds, Acacia Blossoms, Maggots)
- Taming method (toss, feed, imprint from egg, multi-step)
- Command modes (sit/stay/wander/follow/gather)
- Benefits of taming (abilities unlocked, stat changes)

**Examples**:
- Crow: Pumpkin Seeds, 4 command modes
- Grizzly Bear: Honey→Salmon sequence
- Crocodile/Caiman: Egg hatching (imprinting)
- Tiger: Temporary blessing (not permanent taming)
- Elephant: Baby-only taming (adults untameable)

#### 2. Special Mechanics (150+ unique mechanics)

**Missing from requirements**:
- Block interactions (hay blocks, item frames, containers, nests)
- Multi-mob systems (Blue Jay + Raccoon bonding)
- Transformation systems (Bunfungus sleep state, Mimic Octopus forms)
- Crafting systems (Farseer Transmutation Table, Mungus red mushroom explosion)
- Environmental triggers (carved pumpkins for crows, rain for rain frogs)

**Complex Examples**:
- **Bald Eagle Falconry**: Hood equipment → direct control → return to owner
- **Crow Container System**: Item frames → matching → deposit from correct side
- **Elephant Passenger System**: 4-seat saddle + carpet decoration + charge attack
- **Catfish Vacuum System**: 3 sizes → bucket capture → storage capacity scales
- **Leafcutter Ant Colony**: Queen → leaves → mushroom farming → nest expansion

#### 3. Item Interactions (85+ unique items)

**Missing from requirements**:
- What they eat (feeding, taming, breeding all different)
- What they drop (items, slime, special materials)
- What they craft (Banana Slug slime blocks, Roadrunner boots, Dimensional Carver)
- What they interact with (item frames, containers, specific blocks)

**Examples**:
- Crow: Eats food, tamed with Pumpkin Seeds, interacts with item frames
- Mantis Shrimp: Drops shell, eats lobster eggs, breaks blocks
- Mungus: Drops Mungal Spores, crafts into explosive mushroom converter
- Roadrunner: Feathers craft into speed boots

#### 4. AI Behaviors & Command Modes

**Missing from requirements**:
- Exact AI goal sets (gathering, guarding, hunting, farming)
- Command modes and how to switch (right-click, shift-right-click, feeding items)
- Autonomous behaviors (item collection, crop damage, ore revealing)
- Social behaviors (herding, bonding, colony systems)

**Examples**:
- Crow: Stay/Wander/Follow/Gather + autonomous food pickup
- Raccoon: Theft AI + washing items + Blue Jay bonding
- Leafcutter Ant: Colony AI + leaf harvesting + mushroom farming
- Underminer: Mining AI + ore revealing via particle effects

#### 5. Equipment & Gear Systems (20+ types)

**Missing from requirements**:
- Exact equipment slots (head, body, saddle, chest, decorative)
- Equipment items (hoods, saddles, carpets, armor, tusks)
- Equipment effects (stat boosts, abilities, cosmetic)
- How to apply/remove equipment

**Examples**:
- Bald Eagle: Hood (falconry control)
- Elephant: Chest (inventory), Carpet (decoration), Tusks (charge attack)
- Crocodile: Crown (cosmetic)
- Grizzly Bear: Saddle (riding)
- Komodo Dragon: Saddle + Maid outfit (variant)

#### 6. Breeding Systems (64 breedable mobs)

**Missing from requirements**:
- Exact breeding item (often different from taming/feeding item)
- Breeding conditions (biome, structure, time)
- Baby variants (special models, behaviors)
- Egg systems (hatching, imprinting)

**Examples**:
- Crow: Not specified (probably not breedable)
- Banana Slug: Brown Mushrooms
- Blue Jay: Maggots/insect larvae
- Elephant: Acacia Blossoms (rare drops from leaves)
- Crocodile: Eggs dropped → hatching → imprinting

#### 7. Combat & Damage Modifiers (45+ types)

**Missing from requirements**:
- Attack patterns (melee, ranged, area, status effects)
- Damage modifiers (extra vs undead, bonus vs specific mobs)
- Defense abilities (armor, evasion, immunities)
- Combat modes (defensive, aggressive, guard)

**Examples**:
- Crow: Low damage (no aggro), extra vs undead
- Mantis Shrimp: Powerful punch, breaks blocks
- Tiger: Bonus damage when blessed
- Grizzly Bear: Defensive of cubs
- Elephant: Charge attack (tusked only)

#### 8. Variants & Colors

**Missing from requirements**:
- Number of variants (random colors, special named variants)
- How variants are determined (spawn, breeding, crafting)
- Variant-specific behaviors (albino Cachalot Whale, Perry Platypus)
- Texture overlays (glow effects, patterns, equipment)

**Examples**:
- Crow: None (single variant)
- Banana Slug: 4+ random colors
- Lobster: 6 colors
- Terrapin: 6 base × 6 shell × 4 skin = 144 combinations
- Gorilla: DK/Funky special variants

---

## Examples: Before/After Requirements Updates

### Example 1: Crow (Epic 032)

**BEFORE** (Generic Template):
```markdown
**Key Features**: Tameable, item gathering, shoulder perch

**AI Behavior**:
- Passive-appropriate goal system
- Environment interaction
- Player interaction based on category
- Special behaviors: Tameable, item gathering, shoulder perch
```

**AFTER** (Detailed from Catalog):
```markdown
**Key Features**:
- Tameable with Pumpkin Seeds
- 4 Command Modes: Stay/Wander/Follow/Gather Items
- Shoulder perching (follow mode)
- Automated item collection and container deposit system
- Extra damage to undead
- Crop damage (pest behavior)
- Scared by carved pumpkins

**Taming**:
- Method: Toss Pumpkin Seeds to wild crow
- Command Modes:
  - Stay: Sit on hay block, regenerate health
  - Wander: Free roaming
  - Follow: Fly around owner, attempt shoulder perching after time
  - Gather Items: Fly around hay block, collect items, deposit in containers
- Benefits: Controllable, shoulder perch, item automation, undead damage boost

**Special Mechanics**:
- **Hay Block Healing**: Tamed crow sitting on hay block slowly regains health
- **Shoulder Perching**: In follow mode, crow sits on owner's shoulders after time. Remove by sneaking.
- **Item Gathering System**:
  - Flies around hay block in gather mode
  - Picks up nearby items
  - Looks for containers (chests, furnaces, etc.) with item frames
  - If item frame matches held item, deposits from that side
  - Enables automated item sorting and collection
- **Carved Pumpkin Fleeing**: Crows flee from fields with carved pumpkins (pest control)
- **Crop Damage**: Wild crows damage crop growth (pest behavior)
- **Extra Undead Damage**: Deals bonus damage to undead mobs

**Items/Drops**:
- Taming: Pumpkin Seeds
- Eats: Any food items (picks up and devours)
- Drops: Feathers (standard bird drop)

**AI Behaviors**:
- Wild: Scatter and fly when approached, circle and damage crops, pick up food
- Tamed Stay: Sit on hay block, regenerate health
- Tamed Wander: Free roaming, pick up food
- Tamed Follow: Fly around owner, perch on shoulders
- Tamed Gather: Fly around hay block, collect items within radius, deposit in matching containers
- Combat: Peck attackers with low damage (doesn't make target aggressive), extra damage vs undead

**Combat**:
- Defense Mode: Pecks owner's attacker
- Damage: Low (designed not to make attacked mob aggressive toward player)
- Bonus: Extra damage to undead targets
```

### Example 2: Elephant (Epic 035)

**BEFORE**:
```markdown
**Key Features**: Rideable, inventory system, 17 decor variants
```

**AFTER**:
```markdown
**Key Features**:
- Rideable multi-passenger mount (4 seats)
- Large chest inventory
- Carpet decoration (17+ variants)
- Charge attack (tusked elephants only)
- Three types: Calves, Tusked, Non-tusked
- Herd defensive behavior

**Taming**:
- Method: Feed Acacia Blossoms to babies or non-tusked adults
- Cannot tame wild tusked elephants (must tame as calf)
- Benefits: Rideable, inventory, charge attack (tusked)

**Special Mechanics**:
- **4-Seat Saddle**: Multiple passengers can ride
- **Chest Inventory**: Add chest to back for large storage, access by sneaking
- **Carpet Decoration**: Apply any carpet color/pattern, remove with shears
- **Charge Attack**: Feed wheat while riding tusked elephant, temporary speed boost and damage, requires cooldown
- **Acacia Blossom Drops**: Elephants tear leaves from Acacia trees, rare Blossom drops
- **Wandering Trader Mounts**: Traders can spawn riding elephants with chest loot

**Items/Drops**:
- Taming/Breeding: Acacia Blossoms
- Drops: Acacia Blossoms (from tearing leaves, rare)

**AI Behaviors**:
- Herd movement and coordination
- Defensive when herd threatened (tusked especially)
- Trunk toss attacks
- Leaf tearing for blossoms

**Equipment/Gear**:
- Saddle (4-passenger)
- Chest (large inventory)
- Carpet (17+ decoration variants, removed with shears)

**Breeding**:
- Item: Acacia Blossoms
- Produces calves (can be tamed)

**Variants**:
- Calf (baby)
- Non-tusked (tameable)
- Tusked (not tameable as adult, stronger, charge attack)

**Combat**:
- Trunk toss (throws entities)
- Charge attack (tusked, when ridden, wheat-activated)
- Defensive of herd
```

---

## Implementation Guidance

### Step 1: Audit Process

For each epic (001-100):

1. **Locate mob in catalog**: `mob-features-comprehensive-catalog.md`
2. **Extract complete feature set**: Copy all sections (Taming, Special Mechanics, Items, AI, Equipment, Breeding, Variants, Combat)
3. **Compare with requirements.md**: Identify missing details
4. **Update requirements.md**: Add all missing features with implementation details

### Step 2: Requirements Update Template

Update each `requirements.md` with these sections:

```markdown
## Features & Functionality

### Core Features
[List from catalog "Core Features"]

### Taming (if applicable)
- Method: [How to tame - specific steps]
- Item: [Exact taming item]
- Command Modes: [List all modes if applicable]
- Benefits: [What tamed version can do]

### Special Mechanics
[List from catalog "Special Mechanics" - each with detailed description]

### Items & Interactions
- Taming: [Item if tameable]
- Feeding: [What they eat]
- Breeding: [Breeding item if breedable]
- Drops: [What they drop]
- Interacts with: [Special blocks, containers, equipment]
- Crafted items: [Items crafted from drops]

### AI Behaviors
- Wild behaviors: [Untamed/neutral behaviors]
- Tamed behaviors: [If tameable, what they do when tamed]
- Command modes: [Specific modes and how to activate]
- Autonomous behaviors: [What they do without commands]

### Equipment & Gear (if applicable)
[List all equippable items and their effects]

### Breeding (if applicable)
- Item: [Breeding item]
- Conditions: [Special conditions if any]
- Baby variants: [Special baby behaviors]

### Variants
[List all color/type variants]

### Combat
- Attack type: [Melee, ranged, etc.]
- Damage modifiers: [Extra vs certain mobs]
- Defense abilities: [Armor, evasion, etc.]
- Combat modes: [Aggressive, defensive, guard]
```

### Step 3: Implementation Priority

**Phase 1** (Basic Implementation):
- Entity class with correct base (TamableAnimal, Animal, Monster, etc.)
- Basic AI goals (movement, idle, flee)
- Model and renderer
- Health, size, spawn rules

**Phase 2** (Taming & Commands):
- Taming interaction handler
- Command mode switching
- NBT data for tamed state and command
- Sit/stay/follow/wander goals

**Phase 3** (Special Mechanics):
- Block interactions (hay blocks, item frames, containers)
- Item systems (pickup, deposit, matching)
- Special abilities (shoulder perch, gathering, fleeing)
- Multi-step interactions

**Phase 4** (Advanced Systems):
- Equipment systems
- Breeding mechanics
- Variant handling
- Complex AI (item gathering automation, bonding systems)

### Step 4: Epic-Agent Integration

Use epic-agent to update all requirements.md files:

```
For each epic from 001-100:
1. Read mob name from epic folder
2. Look up mob in mob-features-comprehensive-catalog.md
3. Extract complete feature set
4. Update requirements.md with detailed features
5. Ensure all sections populated (Taming, Special Mechanics, Items, AI, Equipment, Breeding, Variants, Combat)
```

---

## Pitfalls to Avoid

### 1. Don't Oversimplify Features

**Bad**: "Tameable"
**Good**: "Tameable with Pumpkin Seeds via tossing, unlocks 4 command modes: Stay/Wander/Follow/Gather Items"

### 2. Don't Miss Multi-Step Systems

**Bad**: "Item gathering"
**Good**: "Item gathering system: Fly around hay block → pick up items → match to item frames on containers → deposit from correct side"

### 3. Don't Forget Implementation Dependencies

Example: Crow's container deposit requires:
- Item frame entity detection
- Item stack matching
- Container capability access (Forge/Fabric)
- Pathfinding to container side
- Item insertion logic

### 4. Don't Ignore Variant Complexity

Example: Terrapin has 144 possible combinations (6 base × 6 shell × 4 skin)
- Requires variant data storage
- Texture overlay system
- Breeding inheritance logic

### 5. Don't Skip Damage Modifiers

Example: Crow's "extra damage to undead" requires:
- Target type checking
- Damage calculation override
- Tag-based mob categorization

---

## References

- **Animal Dictionary**: `xeenaa-alexs-mobs/.claude/epics/002-mob-catalog-asset-inventory/alexsmobs-extracted/assets/alexsmobs/book/animal_dictionary/en_us/`
- **Comprehensive Catalog**: `xeenaa-alexs-mobs/.claude/research/mob-features-comprehensive-catalog.md`
- **Decompiled Entities**: `xeenaa-alexs-mobs/.claude/epics/007-crow-flying-passive-bird/decompiled/com/github/alexthe666/alexsmobs/entity/`
- **CATALOG.md**: `xeenaa-alexs-mobs/.claude/epics/002-mob-catalog-asset-inventory/CATALOG.md`

---

## Next Steps for Implementation

1. ✅ Comprehensive catalog created (`mob-features-comprehensive-catalog.md`)
2. ⏳ **NEXT**: Use epic-agent to update all 90+ requirements.md files
3. ⏳ Prioritize epics by complexity and feature completeness
4. ⏳ Start with simple animals (Fly, Cockroach, Triops already done)
5. ⏳ Work up to complex animals (Crow, Elephant, Bald Eagle)
6. ⏳ Tackle very complex (Void Worm, Warped Mosco, Farseer) last

---

**Research Status**: ✅ Complete
**Confidence Level**: High
**Last Updated**: 2025-10-27
