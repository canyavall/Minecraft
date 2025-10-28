# Alex's Mobs Reference

**Universal knowledge has been moved to centralized knowledge base.**

## Centralized Knowledge

See `.claude/knowledge/minecraft/alexs-mobs-reference.md` for:
- Complete mob catalog (92 creatures)
- Asset inventory (textures, sounds, animations)
- Complexity tiers and porting estimates
- Special mechanics categories
- Phased implementation strategy
- Asset patterns and naming conventions

**Tags to search**: `minecraft`, `entities`, `mobs`, `alex's-mobs`, `porting`, `reference`, `assets`, `creatures`

---

## Project-Specific Documentation

### Epic 002: Mob Catalog & Asset Inventory

Located in `.claude/epics/002-mob-catalog-asset-inventory/`:

**Asset Inventory Files**:
- `ASSET_INVENTORY.md` - Detailed per-mob asset breakdown
- `CATALOG.md` - Full mob catalog with all details (use CATALOG for comprehensive per-mob info)
- `COMPLEXITY_ANALYSIS.md` - AI/animation/mechanics complexity analysis
- `PORTING_ROADMAP.md` - Project-specific porting roadmap
- `EPIC_SUMMARY.md` - Epic completion summary

**Extracted Assets**:
- `alexsmobs-1.22.9.jar` - Original mod JAR
- `alexsmobs-extracted/` - Extracted assets (textures, sounds, models, loot tables)

### When to Use Each Resource

**Use Centralized Knowledge** (`.claude/knowledge/minecraft/alexs-mobs-reference.md`) when:
- Getting quick overview of mob complexity
- Estimating porting effort
- Understanding asset patterns
- Planning phased implementation
- Referencing from other projects

**Use Epic 002 CATALOG.md** (`.claude/epics/002-mob-catalog-asset-inventory/CATALOG.md`) when:
- Need comprehensive details for specific mob
- Looking up exact texture/sound counts
- Reviewing mob-specific mechanics
- Reading animal dictionary entries
- Getting full behavioral descriptions

**Use Extracted Assets** (`alexsmobs-extracted/`) when:
- Need actual texture/sound files
- Examining loot table JSON
- Checking sound file names
- Verifying asset structure

---

## Quick Reference

### Total Assets
- **92 creatures** (90 animals + 2 bosses)
- **402 textures** (avg 4.5 per mob)
- **571 sounds** (avg 6-7 per mob)
- **104 loot tables**

### Complexity Breakdown
- **Simple**: 20-25 mobs (3-5 hours each)
- **Medium**: 35-40 mobs (5-7 hours each)
- **Complex**: 20-25 mobs (7-10 hours each)
- **Very Complex**: 5-8 mobs (10-15+ hours each)

### Total Porting Estimate
- **Assets Only**: 1,282-2,185 hours
- **Full Implementation**: 2,227-3,715 hours (1.1-1.9 person-years)

---

## Project Status

**Current Phase**: Epic 003 - Proof of Concept (3 simple mobs)
**Target**: Validate GeckoLib conversion workflow

**Completed**:
- ✅ Epic 001 - Core framework setup (GeckoLib integration)
- ✅ Epic 002 - Complete mob catalog and asset inventory

**Next Steps**:
- Continue Epic 003 proof of concept
- Document conversion process
- Create automation tools for asset migration

---

**For universal knowledge**, see centralized knowledge base.
**For project-specific details**, see Epic 002 files in this project.
