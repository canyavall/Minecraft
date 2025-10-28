# Villager Systems Reference

**Universal knowledge has been migrated to centralized knowledge base.**

## Centralized Knowledge

See `.claude/knowledge/minecraft/villagers.md` for:
- Villager profession texture system
- Brain AI and memory modules
- Bed claiming mechanics
- Point of Interest (POI) system
- How to prevent custom villagers from claiming beds

**Tags to search**: `minecraft`, `villagers`, `professions`, `textures`, `ai`, `pathfinding`, `brain-system`, `beds`, `poi`

## Project-Specific Notes for xeena-village-manager

### Guard Villager Implementation

Our guard villagers use the patterns from centralized knowledge:

**Prevent Bed Claiming**:
- Mixin into `wantsToSleep()` - returns false for guards
- Mixin into `canSleep()` - returns false for guards
- Clear `MemoryModuleType.HOME` on profession change

**Texture System**:
- Guard texture: `assets/xeenaa_villager_manager/textures/entity/villager/profession/guard.png`
- Metadata: `guard.png.mcmeta` with `"hat": "full"`
- No client registration needed (automatic)

### Implementation Files

- `VillagerSleepMixin.java` - Prevents guards from sleeping/claiming beds
- `ModProfessions.java` - Registers guard profession
- Guard texture in resources folder

### Testing Commands

```bash
# Check if guard has HOME memory (should be empty)
/data get entity @e[type=minecraft:villager,limit=1,sort=nearest] Brain.memories."minecraft:home"

# Verify guard profession
/data get entity @e[type=minecraft:villager,limit=1,sort=nearest] VillagerData.profession
```

---

**Archived Research** (now in centralized knowledge):
- ~~VILLAGER_TEXTURE_SYSTEM_EXPLANATION.md~~ → `.claude/knowledge/minecraft/villagers.md`
- ~~villager-bed-claiming-mechanics.md~~ → `.claude/knowledge/minecraft/villagers.md`
