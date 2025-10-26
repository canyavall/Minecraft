# Porting Roadmap - Alex's Mobs to Fabric 1.21.1

**Project**: Xeenaa's Alex's Mobs (Fabric Port)
**Source**: Alex's Mobs v1.22.9 (NeoForge/Forge)
**Target**: Minecraft 1.21.1 Fabric
**Total Mobs**: 90 animals + 2 bosses (Void Worm, Warped Mosco)
**Estimated Timeline**: 60-75 weeks (14-18 months)
**Estimated Effort**: 2,160-3,580 hours (1.1-1.8 person-years)

---

## Executive Summary

This roadmap outlines the strategic plan for porting all 90 animal mobs from Alex's Mobs (Forge/NeoForge) to Fabric 1.21.1 over a 14-18 month period, organized into 13 logical epics (Epic 03-15). The approach prioritizes risk mitigation through gradual complexity scaling, early community engagement via popular mobs, and sustainable development through code reuse patterns.

### Key Highlights

- **Phased Approach**: Simple (25) → Medium (29) → Complex (20) → Very Complex (7)
- **Incremental Delivery**: Playable content every 1-4 weeks
- **Code Reuse**: 14 shared AI patterns, 5 asset type families
- **Popular Mobs Early**: Crow (Epic 07, week 13), Raccoon (Epic 07, week 13), Elephant (Epic 14, week 52)
- **Risk Management**: Hardest challenges (multi-part, bosses) tackled last when framework is mature
- **Asset Recreation**: All models/animations must be recreated in GeckoLib format (no source files available)

---

## Epic Overview Table

| Epic # | Name | Mob Count | Complexity Mix | Effort | Duration | Dependencies | Start Week |
|--------|------|-----------|----------------|--------|----------|--------------|------------|
| 03 | Proof of Concept | 3 | Simple (ultra-simple) | S (120-180h) | 1-2 weeks | Epic 01 (framework) | Week 1 |
| 04 | Simple Passive Land & Flying | 8 | Simple | S-M (200-240h) | 2-3 weeks | Epic 03 | Week 3 |
| 05 | Simple Aquatic & Amphibious | 6 | Simple | S (150-180h) | 1-2 weeks | Epic 03 | Week 6 |
| 06 | Simple Defensive & Scavengers | 8 | Simple | M (224-280h) | 3-4 weeks | Epic 04 | Week 8 |
| 07 | Medium Tameable Companions | 6 | Medium (popular) | M (210-270h) | 3-4 weeks | Epic 06 | Week 12 |
| 08 | Medium Aquatic Predators | 7 | Medium | M (245-315h) | 3-4 weeks | Epic 05 | Week 16 |
| 09 | Medium Hostile & Placatable | 8 | Medium | M (280-360h) | 3-4 weeks | Epic 07 | Week 20 |
| 10 | Medium Environmental Variants | 8 | Medium | M (280-360h) | 3-4 weeks | Epic 08 | Week 24 |
| 11 | Complex Tameable & Rideable | 6 | Complex | L (300-360h) | 5-6 weeks | Epic 09 | Week 28 |
| 12 | Complex Advanced AI & Social | 6 | Complex | L (300-360h) | 5-6 weeks | Epic 10 | Week 34 |
| 13 | Complex Equipment & Features | 8 | Complex | L (400-560h) | 6-7 weeks | Epic 11 | Week 40 |
| 14 | Very Complex Multi-Part & Inventory | 4 | Very Complex | XL (320-400h) | 8-10 weeks | Epic 12 | Week 47 |
| 15 | Very Complex Special Mechanics | 3 | Very Complex | XL (300-360h) | 8-10 weeks | Epic 13 | Week 57 |
| **TOTAL** | **13 epics** | **77 mobs** | **All tiers** | **2,910-3,885h** | **60-75 weeks** | **Sequential** | **14-18 months** |

**Phase Summary**:
- **Phase 1 (Simple)**: Epic 03-06, 25 mobs, 7-12 weeks, weeks 1-12
- **Phase 2 (Medium)**: Epic 07-10, 29 mobs, 12-16 weeks, weeks 13-28
- **Phase 3 (Complex)**: Epic 11-13, 20 mobs, 16-19 weeks, weeks 29-47
- **Phase 4 (Very Complex)**: Epic 14-15, 7 mobs, 16-20 weeks, weeks 48-67

**Note**: Void Worm (boss) and Warped Mosco (chief) may be added as Epic 16 (Boss Content) as optional stretch goal.

---

## Detailed Epic Breakdowns

### Epic 03: Proof of Concept ✅ READY TO PLAN

**Mobs** (3 ultra-simple):
- **Fly** - Flying, passive (4 AI goals, 3 animations)
- **Cockroach** - Land, passive (3 AI goals, 3 animations)
- **Triops** - Aquatic, passive (3 AI goals, 3 animations)

**Goals**:
- Validate GeckoLib framework integration with real animals
- Establish entity registration patterns (Fabric API)
- Test resource loading (textures, sounds, loot tables)
- Build porting workflow confidence (Blockbench → GeckoLib → Code)
- Create reusable templates for future mobs

**Success Criteria**:
- All 3 mobs functional: summon, render, AI, sounds, loot
- No framework blockers discovered
- Porting workflow documented
- Templates created (simple quadruped, flying, aquatic)

**Technical Focus**:
- GeckoLib model creation from scratch (Citadel has no source files)
- Basic AI goals (wander, flee, swim/fly)
- Sound integration (OGG files)
- Loot table porting (Forge → Fabric)
- Entity registration (Fabric API)

**Estimated Effort**: 120-180 hours (S)
- 3 mobs × 40-60 hours = 120-180 hours
- Model creation: 15-20h per mob (45-60h total)
- Animations: 15-20h per mob (45-60h total)
- Code/testing: 10-20h per mob (30-60h total)

**Duration**: 1-2 weeks
**Risk**: Low (simplest possible mobs)
**Dependencies**: Epic 01 (framework complete)

**Deliverables**:
- 3 working mobs (Fly, Cockroach, Triops)
- Porting workflow documentation
- GeckoLib templates (flying, land, aquatic)
- Asset pipeline established

---

### Epic 04: Simple Passive Land & Flying

**Mobs** (8):
1. **Gazelle** - Simple land (herd behavior)
2. **Jerboa** - Simple land (sleeping state, 2 variants)
3. **Potoo** - Simple flying (camouflage bird)
4. **Shoebill** - Simple flying (static bird)
5. **Blue Jay** - Simple flying (singing, shiny variant)
6. **Hummingbird** - Simple flying (hovering, 3 variants)
7. **Seagull** - Simple flying (scavenger, 2 variants)
8. **Flutter** - Simple flying (glow overlay, 2 textures)

**Goals**:
- Deliver visual variety (6 flying, 2 land)
- Establish bird AI templates (perching, hovering, gliding)
- Test quadruped patterns (Gazelle, Jerboa)
- Build momentum with user-favorite mobs (Blue Jay, Hummingbird)
- Introduce sleeping states, glow overlays, variants

**Success Criteria**:
- All 8 mobs functional
- Flying AI reusable (perching, hovering)
- Herd behavior working (Gazelle)
- Sleeping state functional (Jerboa)
- Glow overlay rendering (Flutter)

**Technical Focus**:
- Flying AI with perching (6 birds)
- Hovering flight (Hummingbird)
- Basic herd behavior (Gazelle follow nearest)
- Simple state variants (sleeping, hovering)
- Emissive overlay rendering (Flutter glow)
- Rare variant spawning (Blue Jay shiny)

**Estimated Effort**: 200-240 hours (S-M)
- 8 simple mobs × 25-30 hours = 200-240 hours

**Duration**: 2-3 weeks
**Risk**: Low (simple AI, proven patterns)
**Dependencies**: Epic 03 (basic patterns)

**Deliverables**:
- 8 working mobs
- Flying AI template (perch, hover, glide)
- Herd behavior system
- State variant system
- Emissive texture rendering

---

### Epic 05: Simple Aquatic & Amphibious

**Mobs** (6):
1. **Cosmic Cod** - Simple aquatic (End dimension, glow, 2 textures)
2. **Devil's Hole Pupfish** - Simple aquatic (tiny rare fish)
3. **Flying Fish** - Simple aquatic (gliding, 3 variants)
4. **Comb Jelly** - Simple aquatic (glow, 4 variants)
5. **Blobfish** - Simple aquatic (pressure transformation, 3 states)
6. **Mudskipper** - Simple amphibious (land/water, spit, 2 textures)

**Goals**:
- Validate aquatic swimming AI (vanilla fish base)
- Test depth-based transformations (Blobfish)
- Introduce amphibious movement (Mudskipper)
- Test simple projectiles (Mudskipper spit)
- Underwater glow rendering (Cosmic Cod, Comb Jelly)

**Success Criteria**:
- All 6 mobs functional
- Aquatic AI reusable (swimming, schooling)
- Depth-based model swapping (Blobfish)
- Amphibious pathfinding (Mudskipper land + water)
- Projectile system working (Mudskipper spit)

**Technical Focus**:
- Aquatic swimming AI (vanilla fish base)
- Depth detection (Blobfish pressure transformation)
- Amphibious pathfinding (Mudskipper land + water)
- Simple projectile attacks (Mudskipper spit)
- Underwater emissive rendering (glows)
- End dimension spawning (Cosmic Cod)

**Estimated Effort**: 150-180 hours (S)
- 6 simple mobs × 25-30 hours = 150-180 hours

**Duration**: 1-2 weeks
**Risk**: Low (vanilla fish AI base)
**Dependencies**: Epic 03 (basic aquatic AI)

**Deliverables**:
- 6 working aquatic mobs
- Aquatic swimming AI template
- Depth detection system
- Amphibious pathfinding
- Simple projectile system

---

### Epic 06: Simple Defensive & Scavengers

**Mobs** (8):
1. **Roadrunner** - Simple land (fast movement, meep variant, 2 textures)
2. **Skunk** - Simple defensive (spray particle defense)
3. **Rattlesnake** - Simple defensive (venomous bite, rattle warning)
4. **Tasmanian Devil** - Simple neutral (scavenger, angry state, 2 variants)
5. **Banana Slug** - Simple passive (slime trail, 5 variants)
6. **Toucan** - Simple flying (6 variants, fruit eating)
7. **Rain Frog** - Simple passive (3 variants, rain spawn)
8. **Maned Wolf** - Simple passive (ender variant visual, 2 textures)

**Goals**:
- Implement defensive mechanics (spray, bite, rattle warning)
- Test particle effects (Skunk spray, Banana Slug slime)
- High variant systems (Toucan: 6, Banana Slug: 5, Rain Frog: 3)
- Weather-based spawning (Rain Frog)
- Scavenger AI (Tasmanian Devil corpse eating)

**Success Criteria**:
- All 8 mobs functional
- Defensive AI working (retreat + counter-attack)
- Particle effects rendering (spray, slime trail)
- Variant systems functional (random texture selection)
- Weather spawning working (Rain Frog)
- Status effects working (venom, poison)

**Technical Focus**:
- Defensive AI (retreat when threatened, counter-attack)
- Particle effects (spray particles, slime trail)
- Variant systems (random texture selection on spawn)
- Weather-based spawning (Rain Frog rain detection)
- Status effects (venom, poison)
- Scavenger AI (corpse detection, eating)
- Speed stat (Roadrunner fast movement)

**Estimated Effort**: 224-280 hours (M)
- 8 simple mobs × 28-35 hours = 224-280 hours (variants add complexity)

**Duration**: 3-4 weeks
**Risk**: Low-Medium (particle effects, variants)
**Dependencies**: Epic 04 (basic AI), Epic 05 (particle effects)

**Deliverables**:
- 8 working mobs
- Defensive AI template
- Particle effect system
- Variant selection system
- Weather-based spawning
- Status effect integration

---

### Epic 07: Medium Tameable Companions

**Mobs** (6):
1. **Crow** - Medium tameable (item gathering, shoulder perch, 8 AI goals)
2. **Raccoon** - Medium neutral (item theft, tameable, 4 variants, glow eyes)
3. **Sugar Glider** - Simple-Medium (gliding, tameable, shoulder perch)
4. **Endergrade** - Simple-Medium (rideable with saddle, End dimension, 2 textures)
5. **Stradpole** - Simple (lava swimming, baby form)
6. **Platypus** - Simple defensive (aquatic, 3 variants: fedora/perry)

**Goals**:
- Deliver most popular tameable mobs early (Crow, Raccoon, Sugar Glider)
- Establish taming mechanics (trust system, owner following)
- Implement item interaction (Crow gathering, Raccoon theft)
- Test shoulder entity mechanics (Crow, Sugar Glider)
- Introduce basic rideable system (Endergrade saddle)

**Success Criteria**:
- All 6 mobs functional
- Taming mechanics working (trust building, owner following)
- Item interaction functional (gathering, theft)
- Shoulder perch working (Crow, Sugar Glider)
- Basic rideable functional (Endergrade saddle)
- Inventory detection (Crow hay bale navigation)

**Technical Focus**:
- Taming mechanics (trust system, owner UUID)
- Item interaction (gathering from containers, theft from players)
- Shoulder entity mechanics (perch on player shoulder)
- Saddle/rideable system (basic mounting)
- Inventory detection (Crow detect hay bales)
- Owner following AI (tameable follow owner)
- Lava swimming (Stradpole Nether)
- Glow eyes (Raccoon emissive overlay)

**Estimated Effort**: 210-270 hours (M)
- 6 medium mobs × 35-45 hours = 210-270 hours

**Duration**: 3-4 weeks
**Risk**: Medium (item interaction, shoulder mechanics)
**Dependencies**: Epic 06 (defensive AI), Epic 05 (aquatic)

**Deliverables**:
- 6 working tameable mobs (3 popular community favorites)
- Taming system
- Item interaction system
- Shoulder entity mechanics
- Basic rideable system
- Inventory detection

---

### Epic 08: Medium Aquatic Predators

**Mobs** (7):
1. **Seal** - Medium passive (aquatic/land, 6 variants: arctic/brown + baby/crying)
2. **Catfish** - Medium passive (3 size variants, spit attack, growth stages, 6 textures)
3. **Alligator Snapping Turtle** - Medium defensive (aquatic, bite attack, moss variant, 2 textures)
4. **Caiman** - Medium neutral (aquatic predator, death roll)
5. **Hammerhead Shark** - Medium placatable (aquatic predator)
6. **Lobster** - Medium neutral (6 color variants)
7. **Mantis Shrimp** - Medium neutral (4 variants, powerful punch)

**Goals**:
- Establish aquatic predator AI (hunting, chasing)
- Test amphibious movement (Seal land + water)
- Implement size-based variants (Catfish growth stages)
- Test special attacks (death roll, punch, spit)
- Introduce placatable mechanics (hostile → neutral)

**Success Criteria**:
- All 7 mobs functional
- Aquatic predator AI working (hunting, chasing)
- Amphibious pathfinding (Seal bask on land)
- Size variants functional (Catfish small/medium/large)
- Special attacks working (death roll, punch, spit)
- Placatable transition (Hammerhead Shark hostile → neutral)
- Baby entity variants (Seal crying babies)

**Technical Focus**:
- Aquatic predator AI (hunting, chasing, attacking)
- Amphibious pathfinding (Seal land basking + water swimming)
- Size-based variants (Catfish growth stages with different loot)
- Special attacks (Caiman death roll grab + spin, Mantis Shrimp punch knockback)
- Placatable mechanics (hostile → neutral transition with items)
- Baby entity variants (Seal baby/crying states)
- High variant counts (Seal: 6, Catfish: 6, Lobster: 6, Mantis Shrimp: 4)

**Estimated Effort**: 245-315 hours (M)
- 7 medium mobs × 35-45 hours = 245-315 hours

**Duration**: 3-4 weeks
**Risk**: Medium (size variants, special attacks)
**Dependencies**: Epic 05 (aquatic AI), Epic 07 (placatable)

**Deliverables**:
- 7 working aquatic predators
- Aquatic predator AI template
- Amphibious pathfinding system
- Size-based variant system
- Special attack mechanics (death roll, punch)
- Placatable transition system

---

### Epic 09: Medium Hostile & Placatable

**Mobs** (8):
1. **Anaconda** - Medium placatable (constriction, shedding, 4 variants)
2. **Dropbear** - Medium hostile (ambush attack, glow eyes, climb/drop, 2 textures)
3. **Guster** - Medium hostile (sandstorm attacks, 6 variants: 3 base + glows)
4. **Rocky Roller** - Medium hostile (rolling attack, 3 states)
5. **Straddler** - Medium hostile (lava strider, Nether)
6. **Murmur** - Medium hostile (sculk-related, angry, vibration detection, 2 textures)
7. **Skreecher** - Medium hostile (sculk-related, glow, screech attack, 2 textures)
8. **Mimicube** - Medium hostile (mimic block, ambush, outer overlay, 2 textures)

**Goals**:
- Implement ambush AI (Dropbear tree climbing, Mimicube block disguise)
- Test special attack patterns (constriction, sandstorm, rolling, screech)
- Integrate sculk mechanics (Murmur, Skreecher vibration detection)
- Test lava pathfinding (Straddler)
- Expand placatable mechanics (Anaconda)

**Success Criteria**:
- All 8 mobs functional
- Ambush AI working (waiting, detecting, pouncing)
- Special attacks functional (constriction, sandstorm, rolling)
- Climbing mechanics (Dropbear tree/wall detection, player clinging)
- Sculk integration (vibration detection)
- Mimic mechanics (Mimicube block disguise)
- Lava pathfinding (Straddler)

**Technical Focus**:
- Ambush AI (wait, detect player, pounce/attack)
- Special attack patterns (Anaconda constriction grab, Guster sandstorm AoE, Rocky Roller ball physics)
- Climbing mechanics (Dropbear tree/wall climbing, cling to player head)
- Sculk integration (Murmur, Skreecher vibration detection from sculk sensors)
- Mimic mechanics (Mimicube disguise as block, reveal on approach)
- Lava pathfinding (Straddler walk on lava surface)
- Placatable AI refinement (hostile → neutral)
- Glow overlays (Dropbear eyes, Guster, Skreecher)

**Estimated Effort**: 280-360 hours (M)
- 8 medium mobs × 35-45 hours = 280-360 hours

**Duration**: 3-4 weeks
**Risk**: Medium (ambush AI, sculk integration)
**Dependencies**: Epic 08 (predator AI), Epic 07 (placatable)

**Deliverables**:
- 8 working hostile/placatable mobs
- Ambush AI template
- Special attack system (constriction, sandstorm, rolling)
- Climbing mechanics
- Sculk integration (vibration detection)
- Mimic mechanics
- Lava pathfinding

---

### Epic 10: Medium Environmental Variants

**Mobs** (8):
1. **Emu** - Medium neutral (running, 5 variants: baby/blonde/blue)
2. **Bunfungus** - Medium neutral (sleeping, transforming states, 3 variants)
3. **Snow Leopard** - Medium neutral (stealth predator, sleeping, 2 variants)
4. **Anteater** - Medium neutral (attacks ants, 2 variants)
5. **Cosmaw** - Medium neutral (floating End mob, glow, projectile, 2 textures)
6. **Crimson Mosquito** - Medium placatable (blood variants, 6 states: fly/blood)
7. **Froststalker** - Medium placatable (ice projectiles, spike variants, 2 textures)
8. **Skelewag** - Medium placatable (2 pirate variants, undead)

**Goals**:
- Implement transformation states (Bunfungus sleeping → transforming)
- Test stealth AI (Snow Leopard stalking, pouncing)
- Target-specific AI (Anteater attacks Leafcutter Ants)
- Blood-filling mechanic (Crimson Mosquito visual states)
- Ice projectiles (Froststalker)
- Dimension-specific mobs (Cosmaw End, Crimson Mosquito Nether)

**Success Criteria**:
- All 8 mobs functional
- Transformation states working (Bunfungus)
- Stealth AI functional (Snow Leopard stalking, pouncing from hiding)
- Target-specific detection (Anteater detects Leafcutter Ants)
- Blood-filling mechanic (Crimson Mosquito visual progression)
- Ice projectiles (Froststalker ranged attack)
- Undead AI (Skelewag)
- Floating End mob (Cosmaw)

**Technical Focus**:
- Transformation mechanics (Bunfungus sleeping ↔ transforming state changes)
- Stealth AI (Snow Leopard stalking, pouncing from hiding)
- Target-specific detection (Anteater detect Leafcutter Ants exclusively)
- Blood/filling mechanic (Crimson Mosquito visual state progression on blood intake)
- Ice projectiles (Froststalker ranged ice attack)
- Spike variants (Froststalker with/without spikes loot variants)
- Pirate variants (Skelewag 2 undead pirate types)
- Floating AI (Cosmaw End dimension)
- Glow overlays (Cosmaw, Crimson Mosquito)

**Estimated Effort**: 280-360 hours (M)
- 8 medium mobs × 35-45 hours = 280-360 hours

**Duration**: 3-4 weeks
**Risk**: Medium (transformation states, blood mechanic)
**Dependencies**: Epic 09 (hostile AI), Epic 08 (predator)

**Deliverables**:
- 8 working environmental mobs
- Transformation system
- Stealth AI template
- Target-specific detection
- Blood-filling mechanic
- Ice projectile system
- Undead AI

---

### Epic 11: Complex Tameable & Rideable

**Mobs** (6):
1. **Bison** - Complex neutral (rideable, shearable, 5 variants: baby/snowy/sheared)
2. **Grizzly Bear** - Complex defensive (tameable, rideable, honey mechanic, 5 variants)
3. **Moose** - Complex neutral (rideable, 4 variants: antlered/snowy)
4. **Komodo Dragon** - Complex placatable (rideable, venom, 3 variants: saddle/maid)
5. **Tusklin** - Complex placatable (rideable, 3 overlays: saddle/hooves)
6. **Tiger** - Complex placatable (tameable, 9 variants: white/sleeping/angry/glow)

**Goals**:
- Establish advanced riding mechanics (all 6 mobs rideable)
- Test equipment systems (saddles, overlays)
- Implement advanced taming (Grizzly Bear honey, Tiger meat)
- Test shearing mechanics (Bison wool drops)
- Deliver popular complex mobs (Grizzly Bear, Tiger)
- Complex state management (Tiger 9 texture combinations)

**Success Criteria**:
- All 6 mobs functional
- Riding mechanics working (player mounting, steering, dismounting)
- Riding animations (saddle, player on back)
- Advanced taming functional (honey, meat, trust building)
- Equipment systems (saddle overlays, decorative variants)
- Shearing mechanics (Bison)
- Venom/poison effects (Komodo Dragon)
- Complex state variants (Tiger 9 combinations)

**Technical Focus**:
- Riding mechanics (player mounting entity, steering control, dismounting)
- Riding animations (saddle visible, player on back animation)
- Advanced taming (Grizzly Bear honey interaction, Tiger meat feeding)
- Equipment systems (saddle overlays, Komodo Dragon maid variant, Tusklin hooves)
- Shearing mechanics (Bison wool drop, sheared state)
- Venom/poison effects (Komodo Dragon venomous bite)
- Biome-based variants (Bison/Grizzly/Moose snowy detection)
- Complex state management (Tiger white/sleeping/angry/glow 9 combinations)
- Glow overlays (Tiger eyes, Grizzly Freddy variant emissive)
- Antler variants (Moose antlered growth stages)

**Estimated Effort**: 300-360 hours (L)
- 6 complex mobs × 50-60 hours = 300-360 hours

**Duration**: 5-6 weeks
**Risk**: Medium-High (riding mechanics, complex states)
**Dependencies**: Epic 07 (taming), Epic 10 (environmental variants)

**Deliverables**:
- 6 working rideable mobs (popular community favorites)
- Advanced riding system
- Riding animation system
- Advanced taming mechanics
- Equipment overlay system
- Shearing mechanics
- Venom/poison system
- Complex state management

---

### Epic 12: Complex Advanced AI & Social

**Mobs** (6):
1. **Capuchin Monkey** - Complex neutral (item interactions, 4 random variants, tameable)
2. **Gorilla** - Complex neutral (silverback hierarchy, 4 variants: leader/DK/Funky)
3. **Leafcutter Ant** - Complex neutral (queen variant, 7 textures: leaf-carrying/angry)
4. **Kangaroo** - Complex neutral (unique hopping, pouch storage mechanic)
5. **Gelada Monkey** - Medium neutral (leader variant, 4 states: angry/leader)
6. **Frilled Shark** - Medium neutral (kaiju transformation, 5 variants: depressurized)

**Goals**:
- Implement social hierarchy (Gorilla silverback, Gelada leader, Leafcutter queen)
- Test item interaction (Capuchin tools, stealing, throwing)
- Implement unique movement (Kangaroo custom hopping physics)
- Test storage mechanics (Kangaroo pouch inventory)
- Implement transformation (Frilled Shark normal → kaiju)
- Queen/colony AI (Leafcutter Ant special spawning)

**Success Criteria**:
- All 6 mobs functional
- Social hierarchy working (leader detection, following)
- Item interaction functional (tools, throwing, stealing)
- Custom movement (Kangaroo hopping physics)
- Pouch storage (Kangaroo inventory access)
- Transformation (Frilled Shark kaiju model swap)
- Queen/colony AI (Leafcutter Ant spawning, leaf carrying)

**Technical Focus**:
- Item interaction systems (Capuchin use tools, throw items, steal from players)
- Social hierarchy (Gorilla silverback leader, Gelada leader, Leafcutter queen detection)
- Queen/colony AI (Leafcutter Ant special spawning, colony behavior)
- Leaf carrying (Leafcutter Ant 3 leaf types visible on model)
- Custom movement (Kangaroo unique hopping physics)
- Pouch storage (Kangaroo inventory access for baby/items)
- Transformation mechanics (Frilled Shark normal ↔ kaiju form model swapping)
- Depth-based triggers (Frilled Shark pressure detection)
- Special variants (Gorilla DK/Funky, Capuchin 4 random)
- Angry states (multiple mobs)

**Estimated Effort**: 300-360 hours (L)
- 6 complex mobs × 50-60 hours = 300-360 hours

**Duration**: 5-6 weeks
**Risk**: High (item interaction, custom movement, hierarchy)
**Dependencies**: Epic 11 (taming/riding), Epic 10 (transformations)

**Deliverables**:
- 6 working complex AI mobs
- Item interaction system
- Social hierarchy system
- Queen/colony AI
- Custom movement physics
- Pouch storage system
- Transformation system (advanced)

---

### Epic 13: Complex Equipment & Features

**Mobs** (8):
1. **Bald Eagle** - Complex neutral (falconry system, hood equipment, hunting AI, 2 textures)
2. **Crocodile** - Complex placatable (2 variants, crown equipment, death roll, 3 textures)
3. **Mungus** - Complex passive (sack/shoes equipment, special beam attack, 5 textures)
4. **Rhinoceros** - Complex defensive (charge attack, potion overlay, angry, 3 textures)
5. **Giant Squid** - Complex neutral (huge size, 3 variants, grab tentacle attack, depressurized)
6. **Laviathan** - Complex passive (huge lava creature, 5 variants: obsidian/gear/helmet/glow)
7. **Orca** - Complex neutral (4 directional variants, hunting, pod behavior)
8. **Terrapin** - Complex passive (144 combinations: 6 base + 6 shell + 4 skin, 17 textures)

**Goals**:
- Implement falconry command system (Bald Eagle)
- Test advanced equipment (hood, crown, sack, shoes, gear, helmet)
- Implement special attacks (charge, beam, death roll, tentacle grab)
- Test huge entity hitboxes (Giant Squid, Laviathan, Orca)
- Implement directional textures (Orca 4 directions)
- Test complex overlay rendering (Terrapin 144 combinations)
- Lava swimming (Laviathan)

**Success Criteria**:
- All 8 mobs functional
- Falconry system working (Bald Eagle commands, hunting, hood)
- Advanced equipment functional (all overlays rendering correctly)
- Special attacks working (charge, beam, death roll, grab)
- Huge hitboxes (Giant Squid, Laviathan)
- Directional textures (Orca NE/NW/SE/SW)
- Complex overlay rendering (Terrapin 3 layers)
- Lava pathfinding (Laviathan)
- Pod behavior (Orca social hunting)

**Technical Focus**:
- Falconry command system (Bald Eagle player commands, hunting prey, hood equipment)
- Equipment overlay rendering (Crocodile crown, Laviathan gear/helmet, Mungus sack/shoes)
- Special attack mechanics (Rhinoceros charge movement, Mungus beam ranged, Crocodile death roll, Giant Squid tentacle grab)
- Huge entity hitboxes (Giant Squid, Laviathan large collision boxes)
- Lava pathfinding (Laviathan walk on lava surface)
- Directional texture system (Orca 4 direction textures: NE/NW/SE/SW)
- Complex overlay rendering (Terrapin 3 layers: base + shell pattern + skin pattern = 144 combinations)
- Pod/hunting AI (Orca social hunting groups)
- Obsidian transformation (Laviathan state change)
- Potion overlay (Rhinoceros visual potion effects)

**Estimated Effort**: 400-560 hours (L)
- 8 complex mobs × 50-70 hours = 400-560 hours (Terrapin adds significant complexity)

**Duration**: 6-7 weeks
**Risk**: High (falconry system, overlay complexity)
**Dependencies**: Epic 12 (social AI), Epic 11 (equipment)

**Deliverables**:
- 8 working complex feature mobs
- Falconry command system
- Advanced equipment overlay system
- Special attack mechanics (charge, beam, grab)
- Huge entity hitbox system
- Directional texture system
- Complex multi-layer overlay rendering (Terrapin)
- Lava pathfinding
- Pod/hunting AI

---

### Epic 14: Very Complex Multi-Part & Inventory

**Mobs** (4):
1. **Bone Serpent** - Very Complex placatable (multi-part: head/mid/tail, lava, 3 textures)
2. **Cave Centipede** - Very Complex placatable (multi-segment, glow, wall climb, dynamic segments, 2 textures)
3. **Elephant** - Very Complex neutral (rideable, chest inventory, 27 textures: 17 carpet colors, social hierarchy)
4. **Mimic Octopus** - Complex passive (5 mimic forms, 7+ textures: creeper/guardian/pufferfish/mimicube/self)

**Goals**:
- Implement multi-part entity rendering (Bone Serpent, Cave Centipede)
- Test dynamic segment count (Cave Centipede variable length)
- Implement wall climbing physics (Cave Centipede)
- Test inventory UI system (Elephant chest access)
- Implement most complex equipment (Elephant 17 carpet colors)
- Test social hierarchy (Elephant leader/standard/calf)
- Implement mimicry transformation (Mimic Octopus 5 forms)

**Success Criteria**:
- All 4 mobs functional
- Multi-part rendering (Bone Serpent head/mid/tail, Cave Centipede segments)
- Dynamic segments (Cave Centipede variable body length)
- Wall climbing (Cave Centipede gravity-defying movement)
- Inventory UI (Elephant chest GUI)
- Equipment decoration (Elephant 17 carpet overlays)
- Social hierarchy (Elephant leader/calf logic)
- Mimicry transformation (Mimic Octopus 5 model swaps)
- Lava swimming (Bone Serpent)

**Technical Focus**:
- Multi-part entity rendering (Bone Serpent head/mid/tail segments coordinate movement)
- Dynamic segment count (Cave Centipede variable body length based on age/size)
- Wall-climbing physics (Cave Centipede custom pathfinding, gravity-defying)
- Inventory UI (Elephant chest GUI integration, storage access)
- Equipment decoration system (Elephant 17 carpet color overlays + tusks)
- Social hierarchy (Elephant leader/standard/calf roles, following behavior)
- Mimicry transformation (Mimic Octopus 5 model swaps: creeper/guardian/pufferfish/mimicube/self)
- Lava pathfinding (Bone Serpent lava swimming)
- Glow overlay (Cave Centipede emissive eyes)
- Placatable (Bone Serpent, Cave Centipede)

**Estimated Effort**: 320-400 hours (XL)
- 4 very complex mobs × 80-100 hours = 320-400 hours
- Bone Serpent: 70-90h (multi-part lava mob)
- Cave Centipede: 80-100h (dynamic segments, wall climbing)
- Elephant: 100-120h (inventory, 17 decorations, hierarchy)
- Mimic Octopus: 70-90h (5 transformation forms)

**Duration**: 8-10 weeks
**Risk**: Very High (multi-part, inventory, wall climbing)
**Dependencies**: Epic 13 (equipment), Epic 12 (social AI)

**Deliverables**:
- 4 working very complex mobs
- Multi-part entity rendering system
- Dynamic segment system
- Wall-climbing physics
- Inventory UI system
- Advanced equipment decoration (17 colors)
- Social hierarchy system (advanced)
- Mimicry transformation (5 forms)

---

### Epic 15: Very Complex Special Mechanics

**Mobs** (3):
1. **Farseer** - Very Complex hostile (portal mechanics, transmutation table, boss-like, 10+ textures, Ancient City)
2. **Cachalot Whale** - Very Complex neutral (huge size, echolocation: 12 textures, albino/sleeping, breach)
3. **Warped Mosco** - Very Complex chief (mini-boss, summon minions, glow, complex combat, 2 textures)

**Goals**:
- Implement portal creation/teleportation (Farseer)
- Test transmutation table integration (Farseer crafting station)
- Implement boss AI patterns (Farseer, Warped Mosco)
- Test echolocation effects (Cachalot Whale 8 echo textures)
- Implement huge size/breach (Cachalot Whale massive hitbox)
- Test minion summoning (Warped Mosco spawn Warped Toads)
- Deliver capstone achievement mobs

**Success Criteria**:
- All 3 mobs functional
- Portal mechanics (Farseer create/teleport)
- Transmutation table (Farseer crafting station)
- Boss AI (Farseer attack phases, Warped Mosco patterns)
- Echolocation effects (Cachalot Whale 8 echo textures: 4 stages × 2 colors)
- Huge size/breach (Cachalot Whale massive hitbox, leap animation)
- Minion summoning (Warped Mosco spawn logic)
- Chief combat (Warped Mosco advanced patterns)

**Technical Focus**:
- Portal creation/teleportation (Farseer create portals, teleport through them)
- Transmutation table integration (Farseer custom crafting station, recipe system)
- Boss AI patterns (Farseer attack phases, cooldowns, Warped Mosco patterns)
- Echolocation effects (Cachalot Whale layered particle/entity effects, 8 textures: 4 stages × 2 colors)
- Huge entity hitboxes (Cachalot Whale massive collision box)
- Breach animation (Cachalot Whale leap out of water)
- Minion summoning (Warped Mosco spawn Warped Toads on command)
- Chief combat AI (Warped Mosco advanced attack patterns)
- Albino variant (Cachalot Whale rare spawn)
- Sleeping state (Cachalot Whale)
- Glow overlay (Warped Mosco)

**Estimated Effort**: 300-360 hours (XL)
- 3 very complex mobs × 100-120 hours = 300-360 hours
- Farseer: 120-150h (portal, transmutation, boss AI)
- Cachalot Whale: 100-120h (huge size, echolocation, breach)
- Warped Mosco: 80-90h (chief combat, minion summoning)

**Duration**: 8-10 weeks
**Risk**: Very High (portal mechanics, boss AI, echolocation)
**Dependencies**: Epic 14 (multi-part, complex mechanics)

**Deliverables**:
- 3 working very complex capstone mobs
- Portal creation/teleportation system
- Transmutation table integration
- Boss AI system (attack phases, cooldowns)
- Echolocation effect system (8 textures, layered)
- Huge entity system (breach animation)
- Minion summoning system
- Chief combat AI

---

## Strategic Timeline

### Phase 1: Foundation & Simple Mobs (Weeks 1-12)

**Epics 03-06** - 25 simple mobs

**Goals**:
- Validate GeckoLib framework with real animals
- Build development momentum with fast iterations
- Deliver visual variety (flying, land, aquatic, amphibious)
- Establish reusable AI templates (wander, flee, swim, fly)
- Introduce basic special features (glow overlays, particle effects, variants)

**Highlights**:
- Fly, Cockroach, Triops (proof of concept)
- Blue Jay, Hummingbird, Seagull (popular birds)
- Toucan, Banana Slug, Rain Frog (high variants)
- Roadrunner, Skunk, Rattlesnake (defensive mechanics)

**Technical Learning**:
- GeckoLib model/animation creation workflow (Blockbench)
- Flying AI (perching, hovering, gliding)
- Swimming AI (vanilla fish base)
- Amphibious pathfinding (land + water)
- Defensive AI (retreat, counter-attack)
- Particle effects (spray, slime trail)
- Variant systems (random texture selection)
- Weather-based spawning (Rain Frog)

**User Value**:
- Diverse animals (air, land, water) quickly playable
- Visual variety (birds, mammals, fish, insects, reptiles)
- Early content (25 mobs in 12 weeks)
- Foundation for future complexity

**Deliverables**:
- 25 working simple mobs
- Porting workflow documentation
- GeckoLib templates (flying, land, aquatic, amphibious)
- Asset pipeline established
- Reusable AI templates (8 patterns)

---

### Phase 2: Medium Complexity & Popular Mobs (Weeks 13-28)

**Epics 07-10** - 29 medium mobs

**Goals**:
- Introduce taming/placatable mechanics
- Deliver most popular mobs (Crow, Raccoon, Sugar Glider)
- Tackle aquatic predators and hostile AI
- Implement environmental variants and transformations
- Test special attacks (constriction, sandstorm, death roll)

**Highlights**:
- Crow, Raccoon, Sugar Glider (community favorites)
- Seal, Catfish, Hammerhead Shark (aquatic predators)
- Anaconda, Dropbear, Guster (hostile/placatable)
- Bunfungus, Snow Leopard, Crimson Mosquito (environmental)

**Technical Learning**:
- Taming mechanics (trust system, owner following)
- Item interaction (gathering, theft)
- Shoulder entity mechanics (perch on player)
- Basic rideable system (Endergrade saddle)
- Aquatic predator AI (hunting, chasing)
- Amphibious pathfinding (Seal basking)
- Size-based variants (Catfish growth)
- Special attacks (death roll, punch, spit, constriction)
- Placatable mechanics (hostile → neutral)
- Ambush AI (Dropbear, Mimicube)
- Sculk integration (vibration detection)
- Transformation states (Bunfungus, Frilled Shark)
- Stealth AI (Snow Leopard stalking)
- Blood-filling mechanic (Crimson Mosquito)

**User Value**:
- Most popular tameable mobs delivered mid-project
- Maintains community engagement with favorites
- Diverse hostile/neutral/passive mix
- Rich environmental variety (Nether, End, Deep Dark, Overworld)

**Deliverables**:
- 29 working medium mobs (including Crow, Raccoon)
- Taming system
- Item interaction system
- Shoulder entity mechanics
- Basic rideable system
- Aquatic predator AI template
- Special attack system (4 types)
- Placatable transition system
- Ambush AI template
- Sculk integration
- Transformation system (basic)
- Stealth AI template

---

### Phase 3: Complex Features (Weeks 29-47)

**Epics 11-13** - 20 complex mobs

**Goals**:
- Advanced riding mechanics (6 rideable mobs in Epic 11)
- Social hierarchies and item interaction (Epic 12)
- Equipment systems (saddles, decorations, overlays) (Epic 13)
- Special attacks (charge, beam, death roll, grab)
- Large creatures (Orca, Giant Squid, Laviathan)
- Complex overlay rendering (Terrapin 144 combinations)

**Highlights**:
- Bison, Grizzly Bear, Moose, Tiger (popular rideable)
- Capuchin Monkey, Gorilla, Kangaroo (advanced AI)
- Bald Eagle, Orca, Elephant (partial), Terrapin (equipment/features)

**Technical Learning**:
- Advanced riding (player mounting, steering, dismounting)
- Riding animations (saddle, player on back)
- Advanced taming (honey, meat, trust building)
- Equipment systems (saddle overlays, decorative variants)
- Shearing mechanics (Bison)
- Venom/poison effects (Komodo Dragon)
- Item interaction (Capuchin tools, stealing, throwing)
- Social hierarchy (Gorilla silverback, Gelada leader, Leafcutter queen)
- Custom movement (Kangaroo hopping physics)
- Pouch storage (Kangaroo inventory)
- Transformation (Frilled Shark kaiju)
- Falconry command system (Bald Eagle)
- Special attacks (charge, beam, death roll, grab)
- Huge entity hitboxes (Giant Squid, Laviathan)
- Directional textures (Orca 4 directions)
- Complex overlay rendering (Terrapin 3 layers)
- Lava pathfinding (Laviathan)

**User Value**:
- Large iconic animals with advanced behaviors
- Rideable mobs (6 in Epic 11)
- Social/pack AI (Gorilla, Orca, Leafcutter Ant)
- Equipment customization (Terrapin 144 combinations)
- Unique mechanics (Kangaroo pouch, Bald Eagle falconry)

**Deliverables**:
- 20 working complex mobs
- Advanced riding system (full)
- Advanced taming mechanics
- Equipment overlay system (multi-layer)
- Social hierarchy system
- Item interaction system (advanced)
- Custom movement physics
- Pouch storage system
- Falconry command system
- Special attack mechanics (4 types)
- Huge entity hitbox system
- Directional texture system
- Complex overlay rendering (Terrapin)
- Lava pathfinding

---

### Phase 4: Very Complex & Capstone (Weeks 48-67)

**Epics 14-15** - 7 very complex + optional 2 bosses

**Goals**:
- Multi-part entities (Bone Serpent, Cave Centipede)
- Inventory systems (Elephant chest access)
- Boss mechanics (Farseer portal/transmutation, Warped Mosco minion summoning)
- Transformation (Mimic Octopus 5 forms)
- Echolocation (Cachalot Whale 8 echo textures)
- Capstone achievement mobs
- Complete port (90 animals)

**Highlights**:
- Bone Serpent, Cave Centipede (multi-part)
- Elephant (inventory, 17 carpet decorations, social hierarchy)
- Mimic Octopus (5 mimic forms)
- Farseer (portal mechanics, transmutation table, boss)
- Cachalot Whale (huge size, echolocation, breach)
- Warped Mosco (chief, minion summoning)
- Void Worm (optional boss - Epic 16)

**Technical Learning**:
- Multi-part entity rendering (head/mid/tail)
- Dynamic segment count (Cave Centipede variable length)
- Wall-climbing physics (Cave Centipede)
- Inventory UI (Elephant chest GUI)
- Equipment decoration (Elephant 17 carpets)
- Social hierarchy (Elephant leader/calf)
- Mimicry transformation (Mimic Octopus 5 forms)
- Portal creation/teleportation (Farseer)
- Transmutation table integration (Farseer crafting)
- Boss AI patterns (Farseer, Warped Mosco attack phases)
- Echolocation effects (Cachalot Whale 8 textures)
- Huge size/breach (Cachalot Whale)
- Minion summoning (Warped Mosco spawn Warped Toads)

**User Value**:
- Epic finale mobs with unique mechanics
- Complete port (90 animals)
- Boss challenges (Farseer, Warped Mosco, optional Void Worm)
- Most complex/impressive mobs (Elephant, Cachalot Whale)
- Full feature parity with original mod

**Deliverables**:
- 7 working very complex mobs (+ 2 optional bosses)
- Multi-part entity rendering system
- Dynamic segment system
- Wall-climbing physics
- Inventory UI system
- Advanced equipment decoration (17 colors)
- Social hierarchy system (advanced)
- Mimicry transformation (5 forms)
- Portal creation/teleportation system
- Transmutation table integration
- Boss AI system
- Echolocation effect system
- Huge entity system (breach)
- Minion summoning system
- **COMPLETE PORT** (90 animals)

---

## Effort Estimates

### Asset Recreation (1,260-2,140 hours)

**Models** (GeckoLib `.geo.json` creation):
- Simple mobs: 3-5h per mob × 30 mobs = 90-150h
- Medium mobs: 5-7h per mob × 35 mobs = 175-245h
- Complex mobs: 7-10h per mob × 20 mobs = 140-200h
- Very Complex: 10-15h per mob × 7 mobs = 70-105h
- **Total**: 450-700 hours

**Animations** (GeckoLib `.animation.json` creation):
- Simple mobs: 5-7h per mob × 30 mobs = 150-210h
- Medium mobs: 7-10h per mob × 35 mobs = 245-350h
- Complex mobs: 10-12h per mob × 20 mobs = 200-240h
- Very Complex: 12-15h per mob × 7 mobs = 84-105h
- **Total**: 540-900 hours (includes multi-part, riding, transformation animations)

**Textures** (recreation for quality/legal safety):
- 402 entity textures @ 0.5-1h each = 180-360 hours
- **Rationale**: Can trace/reference originals, pixel art is faster to recreate than models
- **Note**: Some complexity (Elephant 27 textures, Terrapin 17 textures) balanced by simple (Cockroach 1 texture)

**Sounds** (recreation due to licensing uncertainty):
- 571 sound files @ 0.15-0.3h each (sourcing/editing) = 90-180 hours
- **Rationale**: Can source royalty-free alternatives (Freesound.org, Epidemic Sound)
- **Note**: Faster than creation, slower than reuse

**Loot Tables** (conversion):
- 104 loot tables @ 0.25h each (Forge → Fabric syntax) = 22-45 hours
- **Rationale**: Data-driven, minimal changes needed

**Subtotal**: 1,282-2,185 hours

---

### Code Implementation (720-1,080 hours)

**Entity Logic** (registration, attributes, spawning):
- Simple mobs: 3-5h per mob × 30 mobs = 90-150h
- Medium mobs: 5-7h per mob × 35 mobs = 175-245h
- Complex mobs: 7-10h per mob × 20 mobs = 140-200h
- Very Complex: 10-15h per mob × 7 mobs = 70-105h
- **Total**: 360-540 hours

**AI Goals** (behavior implementation):
- Simple mobs: 2-3h per mob × 30 mobs = 60-90h
- Medium mobs: 3-5h per mob × 35 mobs = 105-175h
- Complex mobs: 5-7h per mob × 20 mobs = 100-140h
- Very Complex: 7-10h per mob × 7 mobs = 49-70h
- **Total**: 270-405 hours

**Special Features** (riding, inventory, transformations, equipment):
- Taming system: 20-30h
- Rideable system: 30-40h
- Inventory UI (Elephant): 15-20h
- Multi-part rendering: 25-35h
- Equipment overlays: 15-25h
- Transformation systems: 15-20h
- Portal mechanics (Farseer): 20-30h
- Echolocation (Cachalot): 10-15h
- Falconry (Bald Eagle): 15-20h
- Social hierarchy: 10-15h
- **Total**: 90-135 hours

**Subtotal**: 720-1,080 hours

---

### Testing & Validation (180-360 hours)

**Per-Epic Testing**:
- Simple epics (03-06): 10-15h per epic × 4 = 40-60h
- Medium epics (07-10): 15-20h per epic × 4 = 60-80h
- Complex epics (11-13): 20-30h per epic × 3 = 60-90h
- Very Complex (14-15): 30-40h per epic × 2 = 60-80h
- **Total**: 180-310 hours

**Integration Testing**:
- Cross-mob interactions: 10-20h
- Compatibility testing: 10-20h
- Performance profiling: 10-20h
- **Total**: 50 hours

**Subtotal**: 180-360 hours

---

### Total Project Effort

| Component | Low Estimate | High Estimate |
|-----------|--------------|---------------|
| Asset Recreation | 1,282 hours | 2,185 hours |
| Code Implementation | 720 hours | 1,080 hours |
| Testing & Validation | 180 hours | 360 hours |
| **TOTAL** | **2,182 hours** | **3,625 hours** |

**Time Range**: 2,182-3,625 hours = 1.1-1.8 person-years (assuming 2000 hours/year)

**Weekly Effort**: 32-54 hours per week (sustainable range: 35-45h/week)

---

## Dependencies & Critical Path

### Critical Path

1. **Epic 01** (Framework) → COMPLETE ✅
2. **Epic 02** (Research) → IN PROGRESS (TASK-007 final task)
3. **Epic 03** (Proof of Concept) → Model/animation dependencies
4. **Epic 04-06** (Simple mobs) → Pattern reuse from Epic 03
5. **Epic 07-10** (Medium) → Taming system from Epic 07, predator AI from Epic 08
6. **Epic 11-13** (Complex) → Riding system development (Epic 11), social AI (Epic 12), equipment (Epic 13)
7. **Epic 14-15** (Very Complex) → Multi-part system (Epic 14), boss AI (Epic 15)

**Sequential Dependencies**:
- Epic 04 depends on Epic 03 (basic patterns)
- Epic 06 depends on Epic 04 (defensive AI patterns)
- Epic 07 depends on Epic 06 (taming foundation)
- Epic 08 depends on Epic 05 (aquatic AI)
- Epic 09 depends on Epic 07 (placatable mechanics)
- Epic 10 depends on Epic 08 (predator AI)
- Epic 11 depends on Epic 09 (advanced taming)
- Epic 12 depends on Epic 10 (transformations)
- Epic 13 depends on Epic 11 (equipment)
- Epic 14 depends on Epic 12 (social hierarchy for Elephant)
- Epic 15 depends on Epic 14 (complex mechanics foundation)

### Parallelization Opportunities

**Asset Creation** can happen parallel to previous epic coding:
- While coding Epic 04, create assets for Epic 05-06
- While coding Epic 07, create assets for Epic 08-09
- Textures/sounds independent from models/animations

**Multiple Developers** can work on different complexity tiers simultaneously:
- Developer A: Epic 03 coding
- Developer B: Epic 04 assets (models/animations)
- Developer C: Epic 05 assets (textures/sounds)

**Team Scaling**:
- 1 developer: 60-75 weeks (14-18 months)
- 2 developers: 35-40 weeks (8-10 months) with parallelization
- 3 developers: 25-30 weeks (6-7 months) with parallelization

### Blockers to Watch

**GeckoLib Limitations**:
- May discover animations that don't translate well from Citadel
- Some Citadel features may be hard to replicate in GeckoLib
- **Mitigation**: Epic 03 validates framework early, pivot if needed

**Citadel Parity**:
- Some Citadel code-based features may require creative solutions
- Multi-part entities (Bone Serpent, Cave Centipede) are high-risk
- **Mitigation**: Research existing GeckoLib multi-part examples, tackle late (Epic 14)

**Performance**:
- Large/complex mobs may need optimization (Elephant, Cachalot Whale, Laviathan)
- Multi-part entities (Bone Serpent, Cave Centipede) may impact FPS
- **Mitigation**: Profile early, optimize as needed, multithreaded pathfinding

**Licensing**:
- Sound/texture licensing may require full recreation
- Original author may contest port (unlikely with GPL-3.0)
- **Mitigation**: Contact original author, recreate assets for legal safety

---

## Risk Mitigation

### Low-Risk Early (Epic 03-06)

**Strategy**: Simple mobs reveal framework issues when fixing is cheapest
- Fast iteration builds team confidence
- Patterns established while stakes are low
- Framework blockers discovered early (Epic 03 validation)
- Asset pipeline proven before complex mobs

**Benefits**:
- If GeckoLib doesn't work, pivot early (weeks 1-2)
- If asset recreation is too slow, adjust timeline/scope early (weeks 3-6)
- If Fabric API has blockers, discover during simple mobs (weeks 1-12)

### Gradual Complexity (Epic 07-10)

**Strategy**: Each epic introduces only 1-2 new systems
- Medium mobs test systems before complex ones depend on them
- Taming (Epic 07) proven before riding (Epic 11)
- Aquatic predator AI (Epic 08) proven before huge creatures (Epic 13)
- Transformation (Epic 10) proven before mimicry (Epic 14)

**Benefits**:
- If taming is hard, fix during Epic 07 before Epic 11 needs it
- If transformations fail, Epic 10 reveals this before Epic 14 depends on it
- Systems mature incrementally, reducing risk of cascading failures

### High-Risk Late (Epic 14-15)

**Strategy**: Framework mature when tackling hardest problems
- Multi-part entities built on proven rendering systems
- Inventory UI (Elephant) uses proven Fabric GUI patterns
- Boss AI (Farseer, Warped Mosco) uses proven attack patterns
- Echolocation (Cachalot) uses proven particle/entity systems

**Benefits**:
- By week 47, framework has 46 weeks of refinement
- By Epic 14, 70 mobs already working (proven systems)
- If multi-part fails, can defer to optional Epic 16 (post-launch)
- If bosses fail, 90 animals still delivered (core value)

### Optional Content

**Epic 16** (Boss Content - Optional):
- Void Worm (most complex boss)
- Additional boss mechanics
- Post-launch stretch goal

**Benefits**:
- If timeline slips, can defer Epic 16 without breaking promise
- 90 animals is "complete port" milestone
- Bosses can be post-launch content update

---

## Recommendations

### For Epic 03 (Immediate Next Steps)

1. **Review Epic 03 mob choices** (Fly, Cockroach, Triops) ✅ VALIDATED
2. **Create Epic 03 requirements.md** (business goals, game mechanics)
3. **Create Epic 03 plan.md** (technical tasks breakdown)
4. **Begin GeckoLib model creation** for Fly (simplest, flying validation)
5. **Document porting workflow** as you go (Blockbench → GeckoLib → Code)

### For Long-Term Planning

**Establish Bi-Weekly Release Cadence**:
- Epic 03: Week 2 (3 mobs)
- Epic 04: Week 5 (8 mobs)
- Epic 05: Week 7 (6 mobs)
- Epic 06: Week 11 (8 mobs)
- Epic 07: Week 15 (6 mobs including Crow, Raccoon)
- Continue every 3-4 weeks...

**Create Asset Pipeline**:
- Blockbench templates (quadruped, biped, fish, bird, insect)
- Animation templates (walk, run, idle, attack, death)
- Sound library (royalty-free Freesound.org collection)
- Texture recreation workflow (Aseprite or GIMP)

**Document Porting Patterns**:
- GeckoLib model creation guide
- AI goal implementation guide
- Entity registration checklist
- Fabric API integration patterns

**Gather User Feedback**:
- Survey community for most-wanted mobs
- Prioritize popular mobs within each complexity tier
- Adjust roadmap based on feedback (Epic 07 already has Crow, Raccoon)

### Success Metrics

**Epic 03** (Weeks 1-2):
- ✅ Framework validated (no GeckoLib blockers)
- ✅ Workflow documented (repeatable process)
- ✅ 3 mobs working (Fly, Cockroach, Triops)

**Epic 04-06** (Weeks 3-12):
- ✅ 25 simple mobs ported (28% complete)
- ✅ AI templates established (8 patterns)
- ✅ Asset pipeline proven (models, animations, textures, sounds)

**Epic 07-10** (Weeks 13-28):
- ✅ Popular mobs delivered (Crow, Raccoon, Sugar Glider)
- ✅ 54 total mobs ported (60% complete)
- ✅ Taming, riding, placatable systems working

**Epic 11-13** (Weeks 29-47):
- ✅ Complex features working (riding, equipment, social AI)
- ✅ 74 total mobs ported (82% complete)
- ✅ Large creatures functional (Orca, Giant Squid, Laviathan)

**Epic 14-15** (Weeks 48-67):
- ✅ Full port complete (90 animals + 2 chiefs/bosses)
- ✅ Multi-part entities working (Bone Serpent, Cave Centipede)
- ✅ Inventory system (Elephant)
- ✅ Boss mechanics (Farseer, Warped Mosco)
- ✅ Feature parity with original mod

---

## Conclusion

This roadmap provides a realistic, achievable plan for porting Alex's Mobs to Fabric over 14-18 months. The phased approach balances:

**Technical Risk** (simple → complex):
- Simple mobs (Epic 03-06) validate framework early
- Medium mobs (Epic 07-10) build on proven patterns
- Complex mobs (Epic 11-13) introduce advanced features gradually
- Very Complex (Epic 14-15) tackle hardest problems when framework is mature

**User Value** (early variety, mid-project popular mobs):
- Epic 03: 3 mobs (week 2) - proof of concept
- Epic 04-06: 25 mobs (week 12) - early variety (flying, land, aquatic)
- Epic 07: Crow, Raccoon (week 16) - popular mobs delivered mid-project
- Epic 11: 6 rideable mobs (week 34) - major features
- Epic 14: Elephant (week 55) - most complex mob
- Epic 15: Farseer, Cachalot Whale (week 67) - finale

**Team Morale** (regular wins, gradual learning):
- Bi-weekly releases (regular achievements)
- Gradual complexity scaling (learning curve)
- Popular mobs mid-timeline (motivation)
- Capstone mobs at end (finale celebration)

**Code Quality** (patterns established early, reused extensively):
- Epic 03: Establish templates (flying, land, aquatic)
- Epic 04-06: Refine AI patterns (8 reusable templates)
- Epic 07-10: Build systems (taming, riding, placatable, transformation)
- Epic 11-13: Advanced features (equipment, social AI, special attacks)
- Epic 14-15: Capstone achievements (multi-part, inventory, bosses)

---

**Next Action**: Create Epic 03 requirements.md and plan.md to begin the journey! 🚀

**Estimated Completion**: Week 67 (14-18 months from start)
**Total Mobs**: 90 animals + 2 bosses (92 total)
**Total Effort**: 2,182-3,625 hours (1.1-1.8 person-years)

This is the AUTHORITATIVE strategic plan document guiding the next 14-18 months of development.
