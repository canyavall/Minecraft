# Alex's Mobs Fabric Porting Strategy

**Researched**: 2025-10-25
**Minecraft Version**: 1.21.1 (target Fabric port)
**Source Version**: Alex's Mobs 1.22.9 for Minecraft 1.20.1 (NeoForge/Forge)
**Related Epic**: Future Epic 01 - Research & Planning

## Research Question

How should we port Alex's Mobs from Forge/NeoForge to Fabric 1.21.1? What version should we port from? What needs to be changed? What can be kept? How can we address existing bugs?

## Summary (For Agents)

**Quick Answer**: Port from **Alex's Mobs 1.22.9** (Minecraft 1.20.1 NeoForge), the most stable and recent release. The port requires replacing Citadel animation system with GeckoLib, rewriting entity registration to use Fabric patterns, converting Tabula models to Fabric-compatible Java code, and migrating AI goals from Forge event system to Fabric's attribute registry. BluSpring's unofficial 1.19.2 Fabric port provides architectural reference but is not 1:1 and includes custom changes.

**Key Findings**:
- **Best source version**: Alex's Mobs 1.22.9 (Minecraft 1.20.1, NeoForge) - most stable, 3.28M downloads, bug fixes release
- **No official 1.21.x version exists** - we will need to update 1.20.1 to 1.21.1 during porting
- **Critical dependency replacement**: Citadel (Forge-only animation library) to GeckoLib (Fabric-compatible)
- **Entity system rewrite**: Forge event-based registration to Fabric's FabricEntityTypeBuilder and FabricDefaultAttributeRegistry
- **AI goals system**: Can mostly be preserved (uses vanilla Goal system), but registration differs
- **Model format challenge**: Citadel uses Tabula (.tbl) models; need conversion to Fabric Java models or GeckoLib format
- **Common bugs to fix**: Komodo Dragon taming/feeding, Dropbear spawning on invalid blocks, Citadel dependency issues
- **Existing reference**: BluSpring's Fabric port (1.19.2) - architecture reference but has custom modifications

**Recommended Approach**: 

1. **Phase 1**: Set up GeckoLib for animation, create Fabric entity registration framework
2. **Phase 2**: Convert models from Tabula to GeckoLib or handwritten Java models
3. **Phase 3**: Port entities in small groups, starting with simple animals
4. **Phase 4**: Migrate items, drops, and crafting recipes
5. **Phase 5**: Address known bugs during porting, test thoroughly

---

## Detailed Findings

### Background

Alex's Mobs is one of the most popular creature mods for Minecraft, adding 89+ animals with custom models, animations, AI behaviors, and an ethical drop system. The mod was created for Forge/NeoForge and relies heavily on Citadel, a Forge-only animation library. No official Fabric version exists for Minecraft 1.21.x, creating an opportunity for this port.

### Investigation Methods

- Web research on Alex's Mobs versions and changelogs (Modrinth, CurseForge, GitHub)
- Analysis of GitHub issues tracker (25 most recent open issues)
- Research on existing Fabric port (BluSpring's repository)
- Fabric documentation review (wiki.fabricmc.net entity tutorial)
- Forge vs Fabric architecture comparison
- Animation library research (Citadel, GeckoLib, vanilla approaches)
- Model format conversion research (Tabula, Java code, JSON)

### Version Selection & Dependencies

**Best Source Version: Alex's Mobs 1.22.9** (September 6, 2024)
- Minecraft: 1.20.1
- Mod Loaders: Both Forge and NeoForge
- Downloads: 3.28 million
- Status: Latest stable release, focused on bug fixes
- No 1.21.1 version exists yet (we will upgrade during porting)

**Critical Dependency Change**:
- Original: Citadel (Forge-only animation library for .tbl Tabula models)
- Replacement: GeckoLib (Fabric-compatible, supports all loaders, 1.21.x support)
- Why GeckoLib: Cross-platform, modern animations, active development, well-documented

### Known Bugs to Address

From GitHub issues analysis (25 most recent):
1. **Komodo Dragon taming/feeding issues** - review interaction logic during port
2. **Dropbear spawning on invalid blocks** (glass, slabs) - add proper spawn conditions
3. **Citadel dependency confusion** - eliminate by using GeckoLib
4. **Bone serpent parts not despawning** - ensure entity cleanup
5. **Hummingbird feeder malfunction** - test interactions carefully

### What Can Be Kept vs What Must Change

**Can Keep (Mostly Unchanged)**:
- Entity AI goals (vanilla system, just rename methods)
- Entity attributes (health, speed, damage values)
- Textures (PNG files, just copy to assets/)
- Sounds (OGG files, copy and register)
- Loot tables (JSON files, update paths)
- Recipes (JSON files, update paths)
- Entity behaviors and logic
- Drop mechanics (ethical drop system logic)

**Must Rewrite**:
- Entity registration (Forge events to Fabric direct registration)
- Attribute registration (FabricDefaultAttributeRegistry)
- Client renderer registration (EntityRendererRegistry)
- Model system (Citadel/Tabula to GeckoLib or Java models)
- Mod initialization (ModInitializer instead of @Mod)
- Config system (if using FML config, port to Fabric config)

**Recommended Porting Strategy**:

Phase 1: Infrastructure
- Add GeckoLib dependency
- Create entity registration framework
- Create item registration framework  
- Set up client initialization

Phase 2: Simple Animals First
- Start with 3-5 simple mobs (basic models, simple AI)
- Validate entire porting workflow
- Refine process before scaling

Phase 3: Incremental Porting
- Port mobs in logical groups (by biome, size, or complexity)
- Test each group thoroughly
- Fix bugs as they appear

Phase 4: Items & Crafting
- Port all mob drops
- Port craftable items
- Implement ethical drop mechanics

Phase 5: Polish & Optimization
- Performance testing
- Bug fixes
- Compatibility testing

## Implementation Guidance

### Entity Registration Pattern (Fabric)

```java
public class ModEntities {
    public static final EntityType<AlligatorEntity> ALLIGATOR = register(
        "alligator",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AlligatorEntity::new)
            .dimensions(EntityDimensions.fixed(2.0f, 0.8f))
            .build()
    );
    
    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, 
            Identifier.of("xeenaa-alexs-mobs", name), type);
    }
    
    public static void initialize() {
        FabricDefaultAttributeRegistry.register(ALLIGATOR, AlligatorEntity.createAttributes());
    }
}
```

### Model Conversion Workflow

1. Extract .tbl model from original mod JAR
2. Open in Blockbench
3. Export as GeckoLib .geo.json format
4. Create .animation.json for animations
5. Implement GeoEntity interface in entity class
6. Create GeoModel and GeoRenderer classes

### AI Goals Migration

Forge:
```java
protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
}
```

Fabric:
```java
protected void initGoals() {
    this.goalSelector.add(0, new SwimGoal(this));
}
```

### Client Rendering Registration

```java
@Environment(EnvType.CLIENT)
public class AlexsMobsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ALLIGATOR, AlligatorRenderer::new);
    }
}
```

## References

- [Fabric Wiki - Creating Entities](https://wiki.fabricmc.net/tutorial:entity)
- [GeckoLib Wiki](https://github.com/bernie-g/geckolib/wiki)
- [Alex's Mobs 1.22.9](https://modrinth.com/mod/alexs-mobs/version/1.22.9)
- [BluSpring Fabric Port](https://github.com/BluSpring/AlexsMobs-Fabric)
- [Alex's Mobs GitHub Issues](https://github.com/AlexModGuy/AlexsMobs/issues)

## Next Research Tasks

1. Download and catalog all 89+ mobs from 1.22.9
2. Create mob prioritization list (simple to complex)
3. Investigate specific model conversion workflow
4. Research 1.20.1 to 1.21.1 API changes

---

**Research Status**: ✅ Complete (Initial Phase)
**Confidence Level**: High  
**Last Updated**: 2025-10-25
