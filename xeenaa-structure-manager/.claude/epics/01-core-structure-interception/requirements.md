# Epic 01: Core Structure Interception & Auto-Classification

**Created**: 2025-10-22
**Status**: PLANNING
**Priority**: Critical

## Business Value

This epic delivers the **core differentiator** that sets Xeenaa Structure Manager apart from all competitors (Sparse Structures, Structurify, Structure Essentials):

**For Players**:
- **Zero configuration needed** - Mod works perfectly out-of-the-box
- **Automatic world improvement** - Reduces structure clutter without manual work
- **Performance gains** - Faster world generation from day one

**For Modpack Creators**:
- **Instant modpack balancing** - No need to configure 200+ structures manually
- **Intelligent defaults** - Mod understands structure types automatically
- **Time savings** - Hours of config work reduced to zero

**Competitive Advantage**:
- Competitors require manual configuration of every structure
- We provide intelligent auto-classification with optional overrides
- **Result**: Better user experience with less work

## Overview

This epic establishes the foundation of the Xeenaa Structure Manager by creating:

1. **Structure Registry Scanner** - Discovers all structures from all mods/datapacks
2. **Auto-Classification Engine** - Automatically categorizes structures by size (small/medium/large)
3. **Configuration System** - TOML-based config with smart defaults and presets
4. **Structure Interception** - Mixin hooks to modify structure generation
5. **Basic Density Control** - Apply spacing/separation multipliers based on classification

**What makes this epic critical**:
- It's the minimum viable product (MVP) that's already better than competitors
- Delivers immediate value: install → it just works
- Provides the foundation for all future epics

## Features

### Feature 1: Structure Registry Scanner
**Description**: Automatically discover and catalog all structures loaded in the world

**User Experience**:
- Player installs mod → Loads world
- Mod silently scans all registered structures during world load
- Console shows: "Xeenaa: Discovered 247 structures from 23 mods"
- No player action required

**Success Criteria**:
- [ ] Detects vanilla Minecraft structures
- [ ] Detects structures from Fabric mods
- [ ] Detects structures from datapacks
- [ ] Completes scan within 5 seconds for 1000+ structures
- [ ] Logs structure count to console

### Feature 2: Two-Dimensional Auto-Classification (Size + Type)
**Description**: Automatically categorize structures using TWO dimensions: Size (physical footprint) and Type (behavioral category)

**Why Two Dimensions?**
- Size alone breaks edge cases (e.g., mineshafts are large but should stay common)
- Type alone ignores physical impact (e.g., large dungeons vs small dungeons)
- Combined approach handles all structure varieties correctly

**User Experience**:
- Mod analyzes each structure during world load
- Classifies in two independent dimensions
- Console shows: "Classified 247 structures: 89 small, 112 medium, 46 large | 67 dungeons, 34 towns, 12 temples, 8 mineshafts, 126 other"

**Dimension 1: SIZE (Bounding Box Volume)**
- **Small**: < 4,096 blocks³ (16×16×16)
  - Examples: Small ruins, single houses, ore veins, shrines
- **Medium**: 4,096 - 32,768 blocks³ (32×32×32)
  - Examples: Village houses, small temples, medium dungeons
- **Large**: > 32,768 blocks³
  - Examples: Mansions, strongholds, ancient cities, large villages

**Dimension 2: TYPE (Behavioral Category via Heuristics)**
- **Town** - Villages, settlements (name contains "village", "town")
- **Dungeon** - Underground structures (Y < 40, name contains "dungeon", "crypt", "catacombs")
- **Temple** - Monuments, pyramids (name contains "temple", "monument", "pyramid")
- **Mineshaft** - Linear mining structures (name contains "mineshaft", "mine")
- **Sky** - Aerial structures (Y > 100, name contains "sky", "floating", "aerial")
- **Ruin** - Scattered debris (name contains "ruin")
- **Unknown** - Fallback for unrecognized structures

**Detection Methods**:
1. **Name analysis** - Parse structure identifier for keywords (80% accurate)
2. **Y-level constraints** - Detect underground vs surface vs aerial
3. **Bounding box shape** - Linear (mineshaft) vs compact (dungeon)
4. **Fallback** - Use size-based rules when type cannot be determined

**Classification Examples**:
```
Ancient City: Size=Large, Type=Dungeon
Village: Size=Large, Type=Town
Mineshaft: Size=Large, Type=Mineshaft (won't get large spacing!)
Sky Village: Size=Medium, Type=Sky
Small Ruin: Size=Small, Type=Ruin
Unknown Mod Structure: Size=Medium, Type=Unknown (uses size defaults)
```

**Success Criteria**:
- [ ] Correctly classifies vanilla structures (verified against manual list)
- [ ] Calculates bounding box volume from NBT data for SIZE
- [ ] Detects TYPE using name-based heuristics (80%+ accuracy)
- [ ] Handles edge cases: mineshafts, sky structures, dungeons
- [ ] Caches both SIZE and TYPE classifications
- [ ] Handles structures without NBT gracefully (fallback to heuristics)
- [ ] Console logs both dimensions for transparency

### Feature 3: Configuration System with Presets
**Description**: TOML configuration file with intelligent defaults and preset profiles

**User Experience**:
- Mod generates config file: `config/xeenaa-structure-manager.toml`
- Default preset: "Balanced" (works well for most modpacks)
- Players can choose presets or customize:

**Preset: Sparse (Explorer)**
```toml
[preset]
active = "sparse"

[global]
spacing_multiplier = 2.0
separation_multiplier = 1.5
```
Effect: Structures 2x further apart, great for exploration-focused gameplay

**Preset: Balanced (Default)**
```toml
[preset]
active = "balanced"

[global]
spacing_multiplier = 1.0
separation_multiplier = 1.0
```
Effect: Maintains vanilla-like density regardless of mod count

**Preset: Abundant (Builder)**
```toml
[preset]
active = "abundant"

[global]
spacing_multiplier = 0.8
separation_multiplier = 0.9
```
Effect: More structures, more variety for builders

**Two-Dimensional Multipliers**:
```toml
# SIZE-based multipliers (primary defaults)
[size.small]
spacing_multiplier = 1.2

[size.medium]
spacing_multiplier = 1.5

[size.large]
spacing_multiplier = 2.5

# TYPE-based multipliers (override size when specified)
[type.town]
spacing_multiplier = 2.0  # Towns spread out

[type.dungeon]
spacing_multiplier = 1.8  # Dungeons rarer but not extreme

[type.temple]
spacing_multiplier = 2.2  # Temples quite rare

[type.mineshaft]
spacing_multiplier = 1.0  # Don't modify mineshafts (already balanced)

[type.sky]
spacing_multiplier = 1.5  # Sky structures moderate spacing

[type.ruin]
spacing_multiplier = 1.3  # Ruins should be scattered but findable

# Priority: Type > Size > Global
# Example: Large dungeon uses type.dungeon (1.8x) not size.large (2.5x)
```

**Structure Overrides** (for advanced users):
```toml
[structures."minecraft:ancient_city"]
enabled = true
spacing = 48  # Override calculated value entirely
separation = 16
```

**Success Criteria**:
- [ ] Config file auto-generates on first run
- [ ] Three presets available: Sparse, Balanced, Abundant
- [ ] Preset selection applies correct multipliers
- [ ] SIZE-based multipliers work correctly
- [ ] TYPE-based multipliers work correctly and override SIZE
- [ ] Priority system works: Type > Size > Global
- [ ] Structure-specific overrides take highest priority
- [ ] Invalid config values use safe defaults with warning
- [ ] Config changes apply without world restart (hot-reload)

### Feature 4: Structure Placement Interception
**Description**: Inject into Minecraft's structure generation pipeline to modify spacing/separation

**User Experience**:
- Invisible to player - happens automatically during world generation
- Structures spawn with modified spacing based on config
- Console logs: "Applied 2.0x spacing to 46 large structures"

**Technical Approach**:
- Mixin into `StructurePlacementCalculator.create()`
- Modify spacing/separation values in structure set stream
- Apply global multipliers × size multipliers × structure overrides

**Success Criteria**:
- [ ] Mixin successfully injects without crashes
- [ ] Spacing values correctly multiplied (e.g., 24 → 48 for 2.0x)
- [ ] Separation values correctly multiplied
- [ ] Changes apply to ALL structures (vanilla, modded, datapack)
- [ ] No performance degradation from interception
- [ ] Compatible with other mods using structure generation

### Feature 5: Basic Performance Optimization
**Description**: Initial performance improvements through caching and efficient data structures

**User Experience**:
- Players experience faster world generation
- Console shows: "Structure generation: 2.1s per chunk (was 3.2s, 34% improvement)"

**Optimizations**:
- **Classification caching**: Classify once at world load, cache results
- **Parallel classification**: Use multiple threads during world load
- **Lazy evaluation**: Don't compute spacing until structure actually attempts generation
- **Efficient lookups**: Use HashMap for O(1) classification retrieval

**Success Criteria**:
- [ ] Classification results cached in memory (ConcurrentHashMap)
- [ ] Parallel classification completes in <5 seconds for 1000 structures
- [ ] No performance regression in structure generation
- [ ] Memory usage <10MB for classification cache
- [ ] Benchmark shows measurable improvement (even if small)

## Technical Considerations

### Structure Sources (Epic 01 Scope)
**Target**: ALL structures registered via Minecraft's standard `Registry<Structure>` system

**Includes**:
- ✅ Vanilla structures (villages, temples, dungeons, strongholds, etc.)
- ✅ Structure-focused mods (YUNG's Better X, When Dungeons Arise, Repurposed Structures)
- ✅ Biome mods with structures (Terralith, Biomes O' Plenty, Oh The Biomes You'll Go)
- ✅ General mods with structures (Create, tech mods, etc.)
- ✅ Datapack-defined structures

**Dimension**: Overworld only (Nether/End support in future epic)

**Excludes**:
- ❌ Legacy mod structures (pre-1.18 custom systems) - Future epic
- ❌ Custom placement bypassing StructurePlacementCalculator - Future epic

### Registry Access
- Structures register via `DynamicRegistryManager.get(RegistryKeys.STRUCTURE)`
- Must access during world load (not mod init) to catch datapack structures
- Use `ServerWorldEvents.LOAD` for Overworld dimension
- Single registry scan catches vanilla + mods + datapacks automatically

### Classification System
**Two-Dimensional Approach**:

**Dimension 1 - SIZE** (Bounding Box Analysis):
- Calculate volume from NBT template bounding box
- Fallback: Estimate from structure type if NBT unavailable
- Cache: Store calculated volumes permanently

**Dimension 2 - TYPE** (Heuristic Detection):
- Primary: Name-based keyword matching (80% accurate)
- Secondary: Y-level constraint analysis
- Tertiary: Bounding box shape (linear vs compact)
- Fallback: Type=Unknown, uses SIZE-based rules

**Priority System**:
```
Structure Override (manual config)
  ↓ (if not set)
Type-based multiplier (e.g., type.dungeon = 1.8x)
  ↓ (if not set or Type=Unknown)
Size-based multiplier (e.g., size.large = 2.5x)
  ↓ (if not set)
Global multiplier (e.g., global = 1.0x)
```

### NBT Template Access
- Structures may not have NBT templates (some are purely JSON-defined)
- Fallback: Use heuristic-based classification when NBT unavailable
- `StructureTemplateManager` provides access to NBT templates
- Handle missing templates gracefully (log warning, use defaults)

### Mixin Injection Point
- Target: `StructurePlacementCalculator.create()` method
- Modify `Stream<RegistryEntry<StructureSet>>` before calculator creation
- Apply SIZE and TYPE multipliers based on classification
- Must preserve structure set relationships (don't break exclusion zones)

### Configuration Format
- Use TOML (human-readable, standard in Minecraft modding)
- Two-dimensional structure: `[size.X]` and `[type.Y]` sections
- Fabric Config API or custom parser
- Support comments and validation
- Clear priority documentation in config file

### Performance Constraints
- Classification must complete before worldgen starts
- Async/parallel processing essential for large modpacks
- Cache both SIZE and TYPE results to avoid recomputation
- Heuristic detection is fast (string operations, no heavy analysis)

## Dependencies

**None** - This is Epic 01, the foundation for all future work

**Enables**:
- Epic 02: Advanced classification (type detection: town/dungeon/temple)
- Epic 03: Visual tools (density heatmap, GUI config editor)
- Epic 04: Performance optimization (advanced caching, Spark integration)

## Out of Scope

This epic explicitly does NOT include:

**Advanced Type Detection**:
- ❌ Complex multi-factor type analysis - Epic 02
- ❌ Machine learning classification - Epic 02+
- ❌ Biome-based type detection - Epic 02
- ❌ Feature analysis (spawners, loot chests) - Epic 02
- **Note**: Epic 01 DOES include basic type detection via simple heuristics (name + Y-level)

**Visual Features**:
- ❌ In-game GUI config editor - Epic 03
- ❌ Density heatmap overlay - Epic 03
- ❌ Structure locator enhancements - Epic 03

**Advanced Performance**:
- ❌ Spark profiler integration - Epic 04
- ❌ Performance dashboard - Epic 04
- ❌ Advanced caching strategies - Epic 04

**Cross-Mod Intelligence**:
- ❌ Modpack analysis - Epic 05
- ❌ Conflict detection - Epic 05
- ❌ Auto-tuning recommendations - Epic 05

**Community Features**:
- ❌ Config sharing - Epic 06
- ❌ Analytics - Epic 06

## Acceptance Criteria (Epic-Level)

### Functional Requirements
- [ ] Mod discovers all structures from all sources (vanilla, mods, datapacks)
- [ ] Structures automatically classified in TWO dimensions: SIZE and TYPE
- [ ] SIZE classification: Small/Medium/Large based on bounding box
- [ ] TYPE classification: Town/Dungeon/Temple/Mineshaft/Sky/Ruin/Unknown via heuristics
- [ ] Config file generates with working presets and two-dimensional multipliers
- [ ] Priority system works: Type > Size > Global
- [ ] Edge cases handled correctly: mineshafts stay common, dungeons differ from towns
- [ ] Spacing/separation multipliers apply correctly to structure generation
- [ ] Works with zero user configuration (Balanced preset by default)

### User Experience
- [ ] Install → Load world → It just works (no config needed)
- [ ] Console provides clear feedback during world load
- [ ] Preset switching is simple (edit one line in config)
- [ ] Advanced users can override specific structures
- [ ] Config errors provide helpful warnings, not crashes

### Performance
- [ ] Classification completes in <5 seconds for 1000 structures
- [ ] No increase in world generation time (ideally slight improvement)
- [ ] Memory usage <10MB for classification data
- [ ] Compatible with 50+ structure mods simultaneously

### Compatibility
- [ ] Works with vanilla Minecraft structures
- [ ] Works with Fabric mod structures
- [ ] Works with datapack structures
- [ ] Compatible with other structure-modifying mods
- [ ] No crashes or errors during normal operation

### Code Quality
- [ ] Follows `coding-standards skill` (naming, documentation, patterns)
- [ ] Comprehensive error handling (no silent failures)
- [ ] Logging at appropriate levels (INFO for user-facing, DEBUG for technical)
- [ ] Thread-safe classification caching
- [ ] Clean mixin implementation (minimal injection surface)

## Definition of Done

This epic is complete when:

1. **Installation Test**: User installs mod → Loads world with 50+ structure mods → Mod works without configuration
2. **Classification Test**: Console shows "Classified X structures: Y small, Z medium, W large"
3. **Preset Test**: User switches preset in config → Reloads world → Structure density changes appropriately
4. **Performance Test**: World generation time doesn't increase (preferably improves slightly)
5. **Compatibility Test**: Works with popular structure mods (YUNG's, When Dungeons Arise, etc.)
6. **User Validation**: Manual testing confirms structures spawn with correct spacing

**Next Epic Trigger**: When user can install mod and immediately see improved structure distribution without any configuration.

---

## Summary of Epic 01 Scope

### What Makes This Epic Special

**Two-Dimensional Classification** (SIZE + TYPE):
- **Solves edge cases**: Mineshafts, sky villages, dungeon vs town distinction
- **Better defaults**: Type-aware multipliers (dungeons 1.8x, towns 2.0x, mineshafts 1.0x)
- **Smarter than competitors**: All other mods use manual config only

**Universal Coverage**:
- **Works with ANY mod**: Vanilla, structure mods, biome mods, game mods, datapacks
- **One registry scan**: Catches everything using standard Minecraft systems
- **Zero special cases**: No allowlists, no mod-specific code

**Zero Configuration Required**:
- **Install → Works**: Balanced preset by default
- **Intelligent defaults**: Type-aware multipliers handle 99% of cases
- **Optional customization**: Advanced users can fine-tune

### Classification Examples (Real Structures)

| Structure | Size | Type | Multiplier Used | Why |
|-----------|------|------|----------------|-----|
| Ancient City | Large | Dungeon | 1.8x (type) | Underground dungeon, not 2.5x from size |
| Plains Village | Large | Town | 2.0x (type) | Town spread, not 2.5x from size |
| Abandoned Mineshaft | Large | Mineshaft | 1.0x (type) | Keep common despite large size! |
| Sky Village (mod) | Medium | Sky | 1.5x (type) | Aerial structure, moderate spacing |
| Small Ruin | Small | Ruin | 1.3x (type) | Scattered debris |
| Unknown Mod Castle | Large | Unknown | 2.5x (size) | Fallback to size when type unclear |

### Key Differentiators from Competitors

| Feature | Competitors | Epic 01 |
|---------|------------|---------|
| Classification | Manual config per structure | **Automatic SIZE + TYPE** |
| Mineshafts | Broken by size-only logic | **Handled correctly (type=1.0x)** |
| Dungeons vs Towns | Same treatment | **Different multipliers** |
| Sky structures | Not recognized | **Detected and handled** |
| Config needed | 200+ structures | **Zero (presets work)** |

## Notes for Implementation

### Recommended Task Breakdown
1. Structure registry scanner + basic logging
2. **SIZE classification engine** + bounding box analysis + caching
3. **TYPE detection engine** + heuristics (name, Y-level, shape) + caching
4. Configuration system + presets + two-dimensional multipliers
5. **Priority system** (Type > Size > Global)
6. Mixin into structure generation
7. Apply multipliers based on SIZE and TYPE classification
8. Performance optimization + benchmarking

### Key Success Factors
- **Two dimensions matter**: Size alone breaks edge cases, Type alone ignores physical impact
- **Heuristics work**: 80% accuracy with simple name parsing is good enough for MVP
- **Automatic is key**: If user must configure anything, we lose to competitors
- **Presets matter**: Make common use cases one-click simple
- **Performance counts**: Even small improvements prove our approach works
- **Logging is UX**: Console output is the only feedback players get

### Risk Mitigation
- **Risk**: TYPE detection inaccurate → **Mitigation**: Log classifications, fallback to SIZE, 80% is acceptable
- **Risk**: Classification too slow → **Mitigation**: Parallel processing, async execution, caching
- **Risk**: Mixin conflicts with other mods → **Mitigation**: Minimal injection surface, test compatibility
- **Risk**: NBT templates unavailable → **Mitigation**: Fallback classification heuristics
- **Risk**: Config too complex → **Mitigation**: Smart defaults, preset system, progressive disclosure
- **Risk**: Priority system confusing → **Mitigation**: Clear documentation, config file comments
