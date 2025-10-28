# Epic 02: Mob Catalog & Asset Inventory - Implementation Plan

**Epic**: 02-mob-catalog-asset-inventory
**Status**: ACTIVE
**Created**: 2025-10-26
**Total Tasks**: 8
**Estimated Effort**: 1-2 weeks (research only, no code)

---

## Task Overview

| Phase | Tasks | Status |
|-------|-------|--------|
| Foundation | TASK-001 to TASK-002 | COMPLETED (2/2) ✅ |
| Research | TASK-003 to TASK-006 | COMPLETED (4/4) ✅ |
| Analysis | TASK-007 to TASK-008 | TODO |

---

## Phase 1: Foundation (2 tasks)

### TASK-001: [COMPLETED] Download and Extract Alex's Mobs JAR

**Agent**: research-agent
**Estimated Time**: 30 minutes
**Priority**: Critical (blocks all other tasks)
**Dependencies**: None
**Status**: ✅ COMPLETED (2025-10-26)

**Goal**: Obtain the Alex's Mobs original mod JAR and extract its contents for analysis

**Requirements**:
1. Download Alex's Mobs latest version from Modrinth or CurseForge
   - Prefer NeoForge version (most recent updates)
   - Document exact version number (e.g., 1.22.9)
2. Extract JAR file contents:
   ```bash
   unzip alexsmobs-<version>.jar -d alexsmobs-extracted/
   ```
3. Verify directory structure exists:
   - `assets/alexsmobs/` - Contains models, textures, sounds
   - `data/alexsmobs/` - Contains loot tables, recipes
4. Create temporary extraction directory in project:
   - `xeenaa-alexs-mobs/.claude/epics/02-mob-catalog-asset-inventory/alexsmobs-extracted/`
5. Document extraction location in task notes

**Acceptance Criteria**:
- JAR file downloaded successfully
- Contents extracted to local directory
- Can navigate directory structure
- Version number documented
- No extraction errors

**References**:
- Modrinth: https://modrinth.com/mod/alexs-mobs
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/alexs-mobs

**Notes**:
- This JAR is for REFERENCE ONLY (asset analysis)
- Do NOT extract code - only analyzing structure and assets
- Respect original mod's GPL-3.0 license
- Assets will be recreated or properly attributed in final port

---

### TASK-002: [COMPLETED] Analyze Original Mod Structure

**Agent**: research-agent
**Estimated Time**: 1.5 hours
**Priority**: High
**Dependencies**: TASK-001 (extracted JAR)
**Status**: ✅ COMPLETED (2025-10-26)

**Goal**: Document the structure and organization of the original Alex's Mobs mod

**Requirements**:
1. Analyze directory structure:
   ```
   assets/alexsmobs/
   ├── geo/              # GeckoLib models
   ├── textures/
   │   ├── entity/       # Mob textures
   │   └── item/         # Item textures
   ├── animations/       # GeckoLib animations
   ├── sounds/           # Audio files
   └── lang/             # Translations

   data/alexsmobs/
   ├── loot_tables/entities/
   ├── recipes/
   └── tags/
   ```

2. Document naming conventions:
   - Entity file naming (e.g., `grizzly_bear.geo.json`)
   - Texture naming patterns
   - Sound file organization
   - Animation file structure

3. Identify key patterns:
   - How entities are registered
   - Model file format (GeckoLib vs other)
   - Animation file structure
   - Resource pack organization

4. Note differences from vanilla Minecraft:
   - Custom AI patterns
   - Special rendering techniques
   - Unique mechanics

5. Create deliverable: **ORIGINAL_MOD_ANALYSIS.md**

**Deliverable Format**:
```markdown
# Original Mod Analysis - Alex's Mobs

## Mod Information
- Version: [version number]
- Loader: NeoForge/Forge
- Minecraft Version: 1.20.1 or 1.21.x

## Directory Structure
[Complete structure tree]

## Naming Conventions
- Entities: [pattern]
- Models: [pattern]
- Textures: [pattern]
- Sounds: [pattern]

## Key Patterns
[Implementation patterns observed]

## Notable Observations
[Useful insights for porting]
```

**Acceptance Criteria**:
- ORIGINAL_MOD_ANALYSIS.md created
- Directory structure fully documented
- Naming patterns identified
- Key observations noted
- Document is clear and actionable

**References**:
- Requirements: Deliverable #4 (Original Mod Analysis)

---

## Phase 2: Research (4 tasks)

### TASK-003: [COMPLETED] Create Complete Mob Catalog

**Agent**: research-agent
**Estimated Time**: 4 hours
**Priority**: Critical
**Dependencies**: TASK-001 (extracted JAR), TASK-002 (structure analysis)
**Status**: ✅ COMPLETED (2025-10-26)

**Goal**: Document all 89+ animal mobs from Alex's Mobs with initial classification

**Requirements**:
1. Identify all animal mobs by examining:
   - Entity files in `assets/alexsmobs/geo/`
   - Texture files in `assets/alexsmobs/textures/entity/`
   - Cross-reference with Alex's Mobs wiki/documentation

2. For each mob, document:
   - Official name
   - Category (Passive/Neutral/Tameable)
   - Primary biome(s) where it spawns
   - Initial complexity estimate (Simple/Medium/Complex/Very Complex)

3. Cross-reference with multiple sources:
   - Mod JAR contents
   - Official Alex's Mobs wiki
   - Modrinth/CurseForge mod page
   - YouTube videos/gameplay

4. Filter out non-animal mobs:
   - Bosses (exclude)
   - Hostile-only creatures (exclude)
   - Focus on ANIMAL mobs only

5. Create initial catalog table (will be enhanced in TASK-005)

**Deliverable Format** (CATALOG.md - initial version):
```markdown
# Alex's Mobs - Complete Animal Catalog

**Total Mobs**: [count]
**Version Analyzed**: [version]
**Last Updated**: [date]

## Mob List (Alphabetical)

| Mob Name | Category | Biome(s) | Initial Complexity |
|----------|----------|----------|-------------------|
| [Name] | Passive | [biomes] | Simple |
| ... | ... | ... | ... |
```

**Acceptance Criteria**:
- All 89+ animal mobs listed (no duplicates)
- Each mob has category and biome
- Initial complexity estimate provided
- Cross-referenced with at least 2 sources
- Non-animal mobs excluded
- CATALOG.md created (initial version)

**References**:
- Requirements: Deliverable #1 (Mob Catalog)
- Requirements: User Story 1 (Porting Roadmap)

---

### TASK-004: [COMPLETED] Inventory Asset Requirements Per Mob

**Agent**: research-agent
**Estimated Time**: 5 hours
**Priority**: High
**Dependencies**: TASK-003 (mob catalog)
**Status**: ✅ COMPLETED (2025-10-26)

**Goal**: Document exactly which assets each mob requires

**Requirements**:
1. For each mob in catalog, identify:
   - **Model file(s)**: `.geo.json` files in `assets/alexsmobs/geo/`
   - **Texture file(s)**: `.png` files in `assets/alexsmobs/textures/entity/`
   - **Animation file(s)**: `.animation.json` files in `assets/alexsmobs/animations/`
   - **Sound file(s)**: `.ogg` files in `assets/alexsmobs/sounds/`
   - **Loot table**: JSON in `data/alexsmobs/loot_tables/entities/`

2. Document file paths for each asset:
   ```
   Grizzly Bear:
   - Model: assets/alexsmobs/geo/grizzly_bear.geo.json
   - Texture: assets/alexsmobs/textures/entity/grizzly_bear.png
   - Animation: assets/alexsmobs/animations/grizzly_bear.animation.json
   - Sounds: assets/alexsmobs/sounds/grizzly_bear_*.ogg
   - Loot: data/alexsmobs/loot_tables/entities/grizzly_bear.json
   ```

3. Identify shared assets:
   - Textures used by multiple mobs (variants)
   - Shared sound effects
   - Common animation patterns

4. Flag special cases:
   - Multi-part models
   - Texture variants (baby, adult, color variations)
   - Multiple sound files per event

5. Create deliverable: **ASSET_INVENTORY.md**

**Deliverable Format**:
```markdown
# Asset Inventory - Alex's Mobs

## Per-Mob Asset Breakdown

### [Mob Name]
- **Model**: [path]
- **Texture**: [path(s)]
- **Animations**: [path]
- **Sounds**: [list]
- **Loot Table**: [path]
- **Notes**: [variants, special cases]

## Shared Assets

### Textures
- [texture_file.png] used by: [mob1, mob2, mob3]

### Animations
- [animation.json] used by: [mob1, mob2]

## Asset Extraction Guide
[Instructions for extracting assets for future epics]
```

**Acceptance Criteria**:
- All mobs have asset requirements documented
- File paths verified to exist in extracted JAR
- Shared assets identified
- Special cases noted
- ASSET_INVENTORY.md created
- Spot-checked 10 mobs to verify accuracy

**References**:
- Requirements: Deliverable #2 (Asset Inventory)
- Requirements: User Story 2 (Asset Inventory)

---

### TASK-005: [COMPLETED] Assign Complexity Ratings

**Agent**: research-agent
**Estimated Time**: 3 hours
**Priority**: High
**Dependencies**: TASK-003 (mob catalog), TASK-004 (asset inventory)
**Status**: ✅ COMPLETED (2025-10-26)

**Goal**: Classify each mob by implementation difficulty using consistent framework

**Requirements**:
1. Review complexity framework from requirements:
   - **Simple**: Single model, 2-4 basic AI goals, no special features
   - **Medium**: Multi-part model, 5-8 AI goals, 1-2 special features
   - **Complex**: Advanced model, 8+ AI goals, multiple features
   - **Very Complex**: Riding, inventory, transformations

2. Analyze each mob across 3 dimensions:
   - **AI Complexity**: Number and sophistication of behaviors
   - **Animation Complexity**: Animation states and rigging
   - **Special Features**: Riding, inventory, unique mechanics

3. Research mob behaviors:
   - Watch gameplay videos
   - Read wiki descriptions
   - Examine AI goal patterns (if accessible)

4. Assign final complexity rating to each mob

5. Identify 3-5 "Simple" mobs for Epic 03 (first port)

6. Update CATALOG.md with detailed complexity breakdown

**Deliverable Format** (enhanced CATALOG.md):
```markdown
## Complete Mob Catalog

| Mob Name | Category | Biome(s) | Complexity | AI Goals | Animations | Special Features | Recommended Epic |
|----------|----------|----------|-----------|----------|------------|-----------------|-----------------|
| Fly | Passive | All | Simple | 3 | 2 (idle, fly) | None | 03 |
| Butterfly | Passive | Forest | Simple | 3 | 2 (idle, fly) | None | 03 |
| Crow | Passive | Plains | Medium | 6 | 4 (idle, walk, fly, peck) | Item pickup | 04 |
| ... | ... | ... | ... | ... | ... | ... | ... |

## Complexity Distribution
- Simple: [count] mobs
- Medium: [count] mobs
- Complex: [count] mobs
- Very Complex: [count] mobs

## Recommended Starting Mobs (Epic 03)
1. [Simple mob 1] - Rationale
2. [Simple mob 2] - Rationale
3. [Simple mob 3] - Rationale
```

**Acceptance Criteria**:
- All mobs have complexity rating (Simple/Medium/Complex/Very Complex)
- Ratings use consistent framework
- Rationale documented for complex ratings
- 3-5 simple mobs identified for Epic 03
- Complexity distribution analyzed
- CATALOG.md updated with full details

**References**:
- Requirements: Complexity Assessment Framework
- Requirements: User Story 1 (Porting Roadmap)

---

### TASK-006: [COMPLETED] Group Mobs for Epic Planning

**Agent**: research-agent
**Estimated Time**: 2 hours
**Priority**: Medium
**Dependencies**: TASK-005 (complexity ratings)
**Status**: ✅ COMPLETED (2025-10-26)

**Goal**: Organize mobs into logical groups for future porting epics

**Requirements**:
1. Create groupings by multiple criteria:
   - **By Complexity**: Simple → Medium → Complex → Very Complex
   - **By Biome**: Ocean, Plains, Forest, Desert, etc.
   - **By Size**: Small, Medium, Large
   - **By Behavior**: Flying, Swimming, Land, Underground

2. Balance group sizes:
   - Each group should have 3-10 mobs
   - Avoid too small (inefficient) or too large (overwhelming)

3. Consider code reuse opportunities:
   - Group mobs with similar AI patterns
   - Group mobs sharing assets (textures, animations)
   - Group mobs with related mechanics

4. Map groups to future epic structure:
   - Epic 03: First Port (3-5 simple mobs)
   - Epic 04: [Group name] (X mobs)
   - Epic 05: [Group name] (X mobs)
   - etc.

5. Document rationale for each grouping

**Deliverable** (updated CATALOG.md):
```markdown
## Mob Groups for Porting

### Epic 03: First Port (Proof of Concept)
**Mobs**: Fly, Butterfly, Cockroach, Hummingbird
**Rationale**: Simplest mobs, minimal AI, basic animations
**Estimated Effort**: Small (1-2 weeks)

### Epic 04: Small Passive Animals
**Mobs**: [list]
**Rationale**: [why grouped together]
**Estimated Effort**: [S/M/L/XL]

### Epic 05: [Group Name]
...
```

**Acceptance Criteria**:
- Mobs grouped by at least 2 criteria
- Each group has 3-10 mobs
- Epic 03 has 3-5 simple mobs
- Rationale documented for each group
- Estimated effort provided (T-shirt sizes)
- CATALOG.md updated with groupings

**References**:
- Requirements: User Story 3 (Strategic Grouping)
- Requirements: Deliverable #1 (Mob Catalog)

---

## Phase 3: Analysis (2 tasks)

### TASK-007: Create Porting Roadmap

**Agent**: research-agent
**Estimated Time**: 2 hours
**Priority**: High
**Dependencies**: TASK-006 (mob groupings)

**Goal**: Define strategic porting plan for all future epics

**Requirements**:
1. Create epic breakdown for all mobs:
   - Epic 03 through Epic XX (as many as needed)
   - Each epic with mob group, effort estimate, dependencies

2. Define porting order strategy:
   - Start with simple mobs (build confidence)
   - Progress to medium complexity
   - End with complex/very complex
   - Group related mobs together

3. Identify dependencies between epics:
   - Shared systems needed (AI patterns, rendering)
   - Feature dependencies (riding needs inventory system)
   - Asset dependencies (shared textures)

4. Provide effort estimates:
   - Use T-shirt sizing (S/M/L/XL)
   - Based on complexity and mob count
   - Include buffer for unknowns

5. Add strategic considerations:
   - Quick wins (visible progress early)
   - Risk mitigation (hardest problems addressed when?)
   - User value (most popular mobs prioritized?)

6. Create deliverable: **PORTING_ROADMAP.md**

**Deliverable Format**:
```markdown
# Porting Roadmap - Alex's Mobs to Fabric

## Epic Overview

| Epic # | Name | Mob Count | Complexity | Effort | Dependencies |
|--------|------|-----------|-----------|--------|--------------|
| 03 | First Port (Proof of Concept) | 4 | Simple | S | Epic 01 (framework) |
| 04 | Small Passive Animals | 8 | Simple-Medium | M | Epic 03 |
| 05 | Flying Creatures | 6 | Medium | M | Epic 04 |
| ... | ... | ... | ... | ... | ... |

## Epic Breakdown

### Epic 03: First Port
**Mobs**: [list]
**Goals**: Validate framework, establish patterns
**Success Criteria**: 4 simple mobs fully functional
**Estimated Duration**: 1-2 weeks
**Risk**: Low (simple mobs only)

### Epic 04: [Name]
...

## Strategic Considerations

### Quick Wins
- Epic 03 delivers visible progress fast
- Popular mobs in early epics (user engagement)

### Risk Mitigation
- Complex mobs delayed until patterns established
- Shared systems built incrementally

### Dependencies
- [Epic] depends on [Epic] because [reason]
```

**Acceptance Criteria**:
- All future epics outlined (03 through completion)
- Each epic has mob count, complexity, effort
- Dependencies documented
- Strategic rationale provided
- PORTING_ROADMAP.md created
- Plan is actionable and realistic

**References**:
- Requirements: Deliverable #3 (Porting Roadmap)
- Requirements: User Story 3 (Strategic Grouping)

---

### TASK-008: Validate and Review Deliverables

**Agent**: research-agent
**Estimated Time**: 1.5 hours
**Priority**: Critical
**Dependencies**: All previous tasks

**Goal**: Validate completeness and accuracy of all research deliverables

**Requirements**:
1. Run validation checks from requirements.md:
   - **Completeness**: Cross-reference catalog with wiki (all mobs listed?)
   - **Asset Verification**: Spot-check 10 mobs to verify assets exist
   - **Complexity Consistency**: Review ratings for logical consistency
   - **Roadmap Feasibility**: Epic 03 has 3-5 simple mobs

2. Review all deliverables:
   - ✅ ORIGINAL_MOD_ANALYSIS.md
   - ✅ CATALOG.md
   - ✅ ASSET_INVENTORY.md
   - ✅ PORTING_ROADMAP.md

3. Check for common issues:
   - Missing mobs
   - Incorrect asset paths
   - Inconsistent complexity ratings
   - Unrealistic epic estimates

4. Create summary document:
   - **EPIC_SUMMARY.md** with key findings and recommendations

5. Prepare for user validation:
   - List any uncertainties or assumptions
   - Flag mobs needing further research
   - Note any extraction/licensing concerns

**Deliverable Format** (EPIC_SUMMARY.md):
```markdown
# Epic 02 Summary - Mob Catalog & Asset Inventory

## Key Findings

### Mob Count
- Total Animal Mobs: [count]
- Simple: [count]
- Medium: [count]
- Complex: [count]
- Very Complex: [count]

### Asset Summary
- Total Models: [count]
- Total Textures: [count]
- Total Animations: [count]
- Shared Assets: [count]

### Recommended Next Steps
1. Epic 03: Port [X] simple mobs ([names])
2. Epic 04: Port [Y] medium mobs ([names])
3. ...

## Uncertainties
- [List any unclear items needing user input]

## Assumptions
- [Document assumptions made during research]

## Risks
- [Note any concerns for future porting]
```

**Acceptance Criteria**:
- All 4 deliverables validated for accuracy
- Completeness checks passed
- Spot-check verification completed
- EPIC_SUMMARY.md created
- Ready for user validation
- No major gaps or errors

**References**:
- Requirements: Testing Strategy
- Requirements: Success Criteria

---

## Task Execution Order

**Critical Path** (must be done in order):
1. TASK-001 (Download JAR) → Blocks all
2. TASK-002 (Analyze structure) → TASK-003, TASK-004
3. TASK-003 (Mob catalog) → TASK-004, TASK-005
4. TASK-004 (Asset inventory) → TASK-005
5. TASK-005 (Complexity ratings) → TASK-006
6. TASK-006 (Groupings) → TASK-007
7. TASK-007 (Roadmap) → TASK-008
8. TASK-008 (Validation) → Epic complete

**Recommended Order**:
- Week 1: TASK-001, TASK-002, TASK-003, TASK-004
- Week 2: TASK-005, TASK-006, TASK-007, TASK-008

---

## Success Criteria (Epic Completion)

This epic is **COMPLETE** when:

- ✅ All 8 tasks marked COMPLETED
- ✅ All 4 primary deliverables created and validated:
  - ORIGINAL_MOD_ANALYSIS.md
  - CATALOG.md (all 89+ mobs with complexity ratings)
  - ASSET_INVENTORY.md (asset requirements per mob)
  - PORTING_ROADMAP.md (epic breakdown and order)
- ✅ Epic 03 has 3-5 simple mobs identified
- ✅ User validates deliverables meet expectations
- ✅ No major gaps or blocking issues

---

## Deliverables Summary

**Location**: `xeenaa-alexs-mobs/.claude/epics/02-mob-catalog-asset-inventory/`

1. **ORIGINAL_MOD_ANALYSIS.md** (TASK-002)
2. **CATALOG.md** (TASK-003, enhanced by TASK-005, TASK-006)
3. **ASSET_INVENTORY.md** (TASK-004)
4. **PORTING_ROADMAP.md** (TASK-007)
5. **EPIC_SUMMARY.md** (TASK-008)
6. **alexsmobs-extracted/** (temporary - TASK-001)

---

## Next Epic

After completing this epic, proceed to:

**Epic 03: First Animal Port (Proof of Concept)**
- Port 3-5 simple mobs identified in CATALOG.md
- Validate framework with real animal ports
- Establish porting patterns for future epics
- Build confidence before tackling complex mobs

---

**Ready to start?** Run `/next` to begin with TASK-001!
