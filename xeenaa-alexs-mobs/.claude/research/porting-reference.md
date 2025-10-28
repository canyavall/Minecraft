# Forge to Fabric Porting Reference

**Universal knowledge has been migrated to centralized knowledge base.**

## Centralized Knowledge

See `.claude/knowledge/libraries/porting-forge-to-fabric.md` for:
- Complete Forge/NeoForge to Fabric porting guide
- Entity registration patterns (Forge vs Fabric)
- Animation system migration (Citadel → GeckoLib)
- AI goals migration patterns
- Networking differences
- Common pitfalls and solutions
- Recommended porting strategy

**Tags to search**: `fabric`, `forge`, `neoforge`, `porting`, `geckolib`, `entities`, `animation`, `registry`, `migration`

## Project-Specific Notes for xeenaa-alexs-mobs

### Porting Source

**Source Version**: Alex's Mobs 1.22.9 (Minecraft 1.20.1, NeoForge)
**Target Version**: Minecraft 1.21.1 Fabric
**Total Mobs**: 89+

### Critical Changes for This Project

**Animation System**:
- Original: Citadel (Forge-only) with .tbl (Tabula) models
- Target: GeckoLib (Fabric-compatible)
- Workflow: Extract .tbl → Blockbench → Export GeckoLib → Integrate

**Dependencies**:
```gradle
modImplementation 'software.bernie.geckolib:geckolib-fabric-1.21:4.5.8'
```

### Known Bugs to Fix During Port

From Alex's Mobs GitHub issues:
1. Komodo Dragon taming/feeding issues
2. Dropbear spawning on invalid blocks (glass, slabs)
3. Bone serpent parts not despawning properly
4. Hummingbird feeder malfunction

### Porting Phases

**Phase 1**: Infrastructure setup (GeckoLib, registration framework)
**Phase 2**: Port 3-5 simple mobs first (validate workflow)
**Phase 3**: Port remaining mobs in logical groups
**Phase 4**: Items, drops, recipes
**Phase 5**: Polish and optimization

### Mob Complexity Tiers

**Simple** (start with these):
- Basic land animals with simple AI
- No special mechanics
- Simple animations

**Medium**:
- Flying/swimming entities
- Basic special mechanics
- Multiple animations

**Complex**:
- Boss mobs
- Complex AI behaviors
- Special interactions
- Advanced mechanics

### Asset Migration Checklist

- [ ] All textures copied to Fabric structure
- [ ] All sounds copied and registered
- [ ] Models converted to GeckoLib format
- [ ] Loot tables updated with Fabric paths
- [ ] Recipes updated with Fabric identifiers
- [ ] Lang files migrated

### Testing Per Mob

- [ ] Mob spawns correctly
- [ ] Model and texture render
- [ ] Animations play correctly
- [ ] AI goals function
- [ ] Combat/interactions work
- [ ] Drops generate correctly
- [ ] No console errors

---

**Project-Specific Research**:
- alexs-mobs-porting-strategy.md - Overall strategy and mob catalog

**Archived Research** (patterns now in centralized knowledge):
- General porting patterns → `.claude/knowledge/libraries/porting-forge-to-fabric.md`
