# Epic 02: Mob Catalog & Asset Inventory - Requirements

**Epic Number**: 02
**Epic Name**: Mob Catalog & Asset Inventory
**Status**: PLANNING
**Created**: 2025-10-26
**Priority**: Critical
**Estimated Effort**: Medium (1-2 weeks)

---

## Executive Summary

Before porting the 89+ animal mobs from Alex's Mobs (Forge/NeoForge) to Fabric, we need a comprehensive catalog and asset inventory. This epic establishes our roadmap by identifying all mobs, their complexity, required assets, and optimal porting order. This research phase ensures efficient execution of future porting epics.

**Business Value**: Strategic planning that prevents rework, identifies dependencies, and enables accurate effort estimation for all future mob porting work.

---

## Problem Statement

### Current Situation

Epic 01 successfully delivered a GeckoLib-based framework capable of porting Alex's Mobs animals to Fabric 1.21.1. However, we currently lack:

1. **Complete mob inventory** - We know there are 89+ mobs but don't have a definitive list
2. **Asset requirements** - Unknown which models, textures, sounds, and animations exist per mob
3. **Complexity assessment** - No classification of simple vs. complex mobs
4. **Porting strategy** - No prioritization or grouping plan for incremental delivery
5. **Dependency mapping** - Unknown which mobs share assets or behaviors

### Why This Matters

Without this catalog:
- We might port mobs in the wrong order (complex before simple)
- We could miss critical assets during extraction
- Effort estimation for future epics would be guesswork
- We might duplicate work (shared textures, animations, AI patterns)
- Risk of scope creep or incomplete ports

---

## Goals & Success Criteria

### Primary Goals

1. **Create Complete Mob List** - Document all 89+ animal mobs from Alex's Mobs 1.22.9
2. **Asset Inventory** - Identify models, textures, sounds, and animations for each mob
3. **Complexity Classification** - Categorize mobs by implementation difficulty
4. **Porting Roadmap** - Define optimal order and grouping for future epics

### Success Criteria

**Epic is COMPLETE when**:

- ✅ All animal mobs cataloged in structured spreadsheet/document
- ✅ Each mob has asset requirements documented (models, textures, sounds)
- ✅ Complexity rating assigned (Simple, Medium, Complex, Very Complex)
- ✅ Mobs organized into logical porting groups (by biome, size, or behavior)
- ✅ Recommended porting order established (simple → complex)
- ✅ Asset extraction strategy documented
- ✅ Shared assets identified (textures/animations used by multiple mobs)

### Out of Scope

- ❌ Actually porting any mobs (Epic 03+)
- ❌ Downloading models/textures (just identifying they exist)
- ❌ Writing any implementation code
- ❌ Non-animal mobs (bosses, hostile creatures)

---

## User Stories

### Story 1: Developer Needs Porting Roadmap

**As a** developer porting Alex's Mobs to Fabric
**I want** a complete catalog of all mobs with complexity ratings
**So that** I can plan incremental epic delivery from simple to complex mobs

**Acceptance Criteria**:
- Spreadsheet/document lists all 89+ mobs with names
- Each mob has complexity rating (Simple/Medium/Complex/Very Complex)
- Mobs grouped by recommended porting order
- Rationale documented for complexity ratings

---

### Story 2: Developer Needs Asset Inventory

**As a** developer preparing to port specific mobs
**I want** to know exactly which assets (models, textures, sounds) each mob requires
**So that** I can extract the right files and avoid missing critical resources

**Acceptance Criteria**:
- Each mob entry lists required asset types:
  - Model files (.geo.json or equivalent)
  - Texture files (.png)
  - Sound files (.ogg)
  - Animation files (.animation.json)
- Shared assets identified (e.g., texture used by 3 mobs)
- Asset locations documented in original mod JAR

---

### Story 3: Developer Needs Strategic Grouping

**As a** developer planning future epics
**I want** mobs organized into logical groups (biome, size, behavior)
**So that** I can port related mobs together and maximize code reuse

**Acceptance Criteria**:
- Mobs grouped by at least 2 criteria (e.g., biome and size)
- Each group has 3-10 mobs (not too small, not too large)
- Groups documented with rationale
- Suggested epic structure based on groups

---

### Story 4: Research Original Mod Structure

**As a** developer new to Alex's Mobs codebase
**I want** to understand how the original mod is structured
**So that** I can efficiently locate assets and understand implementation patterns

**Acceptance Criteria**:
- Original mod JAR analyzed (version 1.22.9 for Fabric or latest NeoForge)
- Directory structure documented
- Key patterns identified (naming conventions, package structure)
- Notable differences from vanilla Minecraft documented

---

## Game Mechanics & Design

### Mob Categories (Preliminary)

Based on typical Alex's Mobs content, we expect categories like:

1. **Passive Animals** (majority)
   - Land animals (bears, kangaroos, elephants)
   - Flying animals (birds, insects)
   - Aquatic animals (fish, dolphins, seals)
   - Underground animals (moles, cave-dwellers)

2. **Neutral Animals**
   - Territorial (grizzly bears, komodo dragons)
   - Defensive (gorillas, rhinos)

3. **Tameable/Rideable**
   - Rideable mounts (elephants, certain large animals)
   - Tameable pets (crows, raccoons)

### Complexity Factors

Mobs will be rated based on:

1. **AI Complexity**
   - Simple: Basic wander/flee/breed (butterfly, fly)
   - Medium: Custom goals, interactions (crow, raccoon)
   - Complex: Multi-stage behaviors, pack AI (gorilla, elephant)
   - Very Complex: Riding, special mechanics (flying mounts)

2. **Animation Complexity**
   - Simple: Walk, idle (small animals)
   - Medium: Attack, eat, custom animations
   - Complex: Flying, swimming with advanced rigging
   - Very Complex: Player riding animations, multi-part models

3. **Special Features**
   - Inventory (backpacks, storage)
   - Riding mechanics
   - Transformation/life stages
   - Environmental interactions (building nests, digging)

---

## Technical Considerations

### Asset Extraction Strategy

**Source**: Alex's Mobs JAR (version 1.22.9 or latest compatible)

**Asset Types to Extract**:
1. **Models** - GeckoLib .geo.json files or equivalent
2. **Textures** - PNG files for entity rendering
3. **Animations** - .animation.json files
4. **Sounds** - .ogg audio files
5. **Loot Tables** - JSON definitions for drops

**Extraction Process**:
- Unzip mod JAR
- Navigate to `assets/alexsmobs/` directory
- Document structure and file locations
- Identify shared assets (used by multiple entities)

### Complexity Assessment Framework

**Simple Mobs** (Recommended for Epic 03 - First Port):
- Single model part
- 2-4 basic AI goals (wander, look, flee)
- Minimal custom animations (idle, walk)
- No special features
- Examples: Fly, Butterfly, Small insects

**Medium Mobs**:
- Multi-part model
- 5-8 AI goals including custom behaviors
- Custom animations (attack, eat, special actions)
- 1-2 special features (drops, interactions)
- Examples: Crow, Raccoon, Seal

**Complex Mobs**:
- Advanced multi-part models
- 8+ AI goals with pack/social behavior
- Complex animation states
- Multiple special features
- Examples: Elephant, Gorilla, Grizzly Bear

**Very Complex Mobs**:
- Riding mechanics
- Inventory systems
- Life stage transformations
- Advanced environmental interactions
- Examples: Rideable animals, animals with storage

---

## Deliverables

### 1. Mob Catalog Spreadsheet

**Format**: Markdown table or CSV
**Columns**:
- Mob Name
- Category (Passive/Neutral/Tameable)
- Biome(s)
- Complexity (Simple/Medium/Complex/Very Complex)
- Model File(s)
- Texture File(s)
- Sound File(s)
- Animation File(s)
- Special Features
- Recommended Epic (03, 04, 05, etc.)
- Notes

**Location**: `.claude/epics/02-mob-catalog-asset-inventory/CATALOG.md`

---

### 2. Asset Inventory Document

**Format**: Markdown document
**Content**:
- Directory structure of original mod assets
- Naming conventions
- Shared assets list (textures/animations used by multiple mobs)
- Asset extraction instructions

**Location**: `.claude/epics/02-mob-catalog-asset-inventory/ASSET_INVENTORY.md`

---

### 3. Porting Roadmap

**Format**: Markdown document
**Content**:
- Recommended epic breakdown (Epic 03, 04, 05, etc.)
- Mob groups per epic with rationale
- Estimated effort per epic (T-shirt sizing: S/M/L/XL)
- Dependencies between epics
- Strategic considerations

**Location**: `.claude/epics/02-mob-catalog-asset-inventory/PORTING_ROADMAP.md`

---

### 4. Original Mod Analysis

**Format**: Markdown document
**Content**:
- Mod version analyzed (1.22.9 or latest)
- Package structure overview
- Key implementation patterns
- Notable differences from vanilla
- Useful observations for porting

**Location**: `.claude/epics/02-mob-catalog-asset-inventory/ORIGINAL_MOD_ANALYSIS.md`

---

## Testing Strategy

### Validation Steps

1. **Completeness Check**
   - Cross-reference catalog against official Alex's Mobs wiki/documentation
   - Verify all animal mobs included (not bosses or hostile-only)
   - Confirm no duplicates

2. **Asset Verification**
   - Spot-check 5-10 mobs to verify assets exist in original JAR
   - Confirm file paths are accurate
   - Test asset extraction process for sample mob

3. **Complexity Rating Review**
   - Review complexity ratings for consistency
   - Ensure "Simple" mobs are truly simple (good for Epic 03)
   - Validate "Very Complex" ratings have justification

4. **Roadmap Feasibility**
   - Ensure Epic 03 has 3-5 simple mobs for proof of concept
   - Verify groupings make logical sense
   - Check dependencies are in correct order

---

## Dependencies

### Prerequisites

- ✅ Epic 01 completed (framework exists)
- ✅ Access to Alex's Mobs JAR file (download from Modrinth/CurseForge)

### External Dependencies

- **Alex's Mobs Original Mod**: Version 1.22.9 (or latest for NeoForge/Fabric if available)
- **Blockbench** (optional): For viewing/analyzing models
- **Text Editor**: For analyzing JSON files

### No Code Dependencies

This epic is pure research - no code implementation required.

---

## Risks & Mitigation

### Risk 1: Version Differences

**Risk**: Alex's Mobs versions differ between loaders (Forge vs NeoForge)
**Impact**: Asset lists might be incomplete or incorrect
**Mitigation**:
- Use latest NeoForge version as reference (most recent)
- Document version used in analysis
- Flag any version-specific features

### Risk 2: Incomplete Catalog

**Risk**: Missing mobs due to incomplete analysis
**Impact**: Future epics miss content, rework needed
**Mitigation**:
- Cross-reference with official wiki/documentation
- Use multiple sources (mod JAR + wiki + videos)
- Peer review catalog before marking epic complete

### Risk 3: Inaccurate Complexity Ratings

**Risk**: Rating mobs wrong (too simple/too complex)
**Impact**: Epic 03 becomes too hard or wastes time on trivial work
**Mitigation**:
- Use consistent rating framework
- Review ratings with user before marking complete
- Accept ratings may adjust during actual implementation

### Risk 4: Asset Extraction Issues

**Risk**: Cannot extract or locate assets in original JAR
**Impact**: Delays future porting work
**Mitigation**:
- Test extraction process early
- Document any encryption/obfuscation
- Fallback: Recreate assets if extraction fails (with attribution)

---

## Performance Considerations

**Not Applicable**: This is a research epic with no code or performance impact.

---

## User Validation Required

Before proceeding to `/create_plan`, user should review:

1. **Scope Validation**
   - Confirm this epic should catalog ALL 89+ mobs (vs. subset)
   - Verify focus on animal mobs only (not bosses/hostiles)

2. **Deliverable Format**
   - Approve markdown tables/documents as format
   - Suggest any additional information to capture

3. **Complexity Framework**
   - Review proposed complexity factors (AI, animations, features)
   - Suggest adjustments to rating criteria

4. **Epic Grouping Strategy**
   - Confirm preference for biome-based or behavior-based grouping
   - Approve recommended epic breakdown approach

---

## Next Steps (After User Approval)

1. User reviews and validates this requirements document
2. User runs `/create_plan` to generate technical tasks
3. User runs `/next` to begin executing tasks
4. Research-agent performs analysis and creates deliverables
5. User validates catalog and roadmap
6. Epic marked complete, proceed to Epic 03 (First Animal Port)

---

## Notes

- This epic is **100% research** - no code implementation
- Estimated 1-2 weeks depending on depth of analysis
- Deliverables used to plan ALL future mob porting epics
- High-value investment that pays off in future efficiency
- Consider downloading Alex's Mobs JAR before starting work

---

**Status**: PLANNING - Awaiting user validation before creating plan.md
