# Crow - Known Bugs

**Status**: PENDING FIX
**Reported**: 2025-10-27

## Bug 1: Texture Not Rendering

**Problem**: Crow appears completely black in-game
- No texture visible
- Not even eyes showing
- Model renders correctly but entirely black

**User Quote**: "the texturs are not there, the crow is completely black, or maybe is normal, but not even eyes?"

**Investigation Notes**:
- Texture file exists: `src/main/resources/assets/xeenaa-alexs-mobs/textures/entity/crow/crow.png`
- Texture extracted from Alex's Mobs JAR (468 bytes, 32×32 expected)
- CrowModel.java points to correct path: `"textures/entity/crow/crow.png"`
- CrowRenderer.java also references correct path
- Fly, Cockroach, and Triops textures work correctly with same pattern

**Possible Causes**:
1. Texture file corruption or format issue
2. UV mapping mismatch in crow.geo.json
3. Resource loading problem
4. Texture path resolution issue

**Next Steps**:
- Verify texture file format and dimensions
- Compare UV coordinates with original Citadel model
- Check if texture is being loaded by GeckoLib
- May need to re-extract texture from Alex's Mobs JAR

---

## Bug 2: Spawn Egg Missing

**Problem**: No spawn egg available in creative inventory

**User Quote**: "I could not find an egg for it neither"

**Cause**: Spawn egg not created

**Fix Required**:
1. Add CROW_SPAWN_EGG to ModItems.java
2. Register in creative tab (ALEXS_MOBS_TAB)
3. Follow pattern from existing eggs (FLY_SPAWN_EGG, COCKROACH_SPAWN_EGG, TRIOPS_SPAWN_EGG)

---

## Implementation Status

**Completed**:
- ✅ Entity (CrowEntity with flying/ground AI)
- ✅ Model (crow.geo.json with 9 bones)
- ✅ Animations (crow.animation.json with 6 animations: idle, walk, run, fly, sit, attack)
- ✅ Renderer (CrowRenderer)
- ✅ Registration (ModEntities, AlexsMobsClient)
- ✅ Build successful

**Blocked**:
- ❌ Texture rendering (black crow)
- ❌ Spawn egg (not created)

**Cannot Test Until Fixed**:
- Animation verification (all 6 animations)
- AI behavior (flying, landing, walking, sitting, tempting)
- Size and positioning adjustments
- Shadow rendering

---

**Resume Point**: Fix texture rendering first, then create spawn egg, then rebuild and test
