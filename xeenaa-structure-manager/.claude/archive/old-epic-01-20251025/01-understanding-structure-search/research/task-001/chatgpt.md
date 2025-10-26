# Structure placement algorithm — research

**Location:** `.claude/research/structure-placement-algorithm.md`

**Scope / goal**
Document how Minecraft decides whether *"this structure should place here?"* for structures using `RandomSpreadStructurePlacement` (the common grid/spread-based placement used by villages, temples, etc.). The doc explains the full check pipeline, highlights expensive vs cheap operations, shows a code-flow diagram, and explains biome filtering and spacing/separation impacts.

---

## Executive summary (for non-experts)

Minecraft decides structure placement in a pipeline of checks. The engine performs fast arithmetic and bitwise/modulo checks first (cheap), then progressively runs slightly heavier checks (random-grid seeding, exclusion/spacing), then biome and terrain queries (expensive-ish), and finally structure start generation and collision/overlap checks (very expensive). Short-circuiting early (cheap) checks prevents the majority of structure-placement work.

If you have hundreds of structure candidates ("569 structures"), each one going further into the pipeline multiplies cost — especially when the expensive height/biome/terrain lookups and structure start generation occur. That’s why a large number of structures makes generation slow.

---

## Source & versions examined

I used current public mappings/docs for `RandomSpreadStructurePlacement` and related `StructurePlacement` APIs (JavaDoc/mappings pages for 1.20.x — the structure-placement class evolved slightly across versions, so method names map to slightly different signatures in older/newer versions). The authoritative locations to inspect in your exact server version are the decompiled/mapped sources for that version (see links you provided in the review step).

> **Note:** exact line numbers vary by version and build; the algorithmic flow and method names are stable. Use the mappings/Javadocs listed in the references to find the exact lines in the matching version.

---

## High-level pipeline (step-by-step)

1. **Registry & StructureSet lookup (config):** For each candidate `StructureSet` / structure in a chunk, engine checks that a placement exists and reads the structure's `StructurePlacement` configuration (e.g. `RandomSpreadStructurePlacement`).

2. **Spacing grid coordinate calculation** (very cheap): compute which grid cell (grid of size `spacing`) the chunk belongs to. This uses integer division/modulo arithmetic.

3. **Separation randomness & seed** (cheap): compute the pseudo-random offset for the grid cell using the structure salt + world seed (deterministic). This yields the *potential structure chunk* for this grid cell (method often named `getPotentialStructureChunk` / `getPotentialStructurePos`). If current chunk != potential chunk, short-circuit (not a placement chunk).

4. **Separation distance check** (cheap): ensure generated candidate obeys `separation` parameter so structures don’t cluster too tightly inside spacing cell.

5. **Exclusion zone / other-structures interaction checks** (cheap-to-moderate): some placements consult nearby structures (via `StructureAccessor`) to avoid placing too close to other structure types (exclusion zones). This may touch cached structure indices and is moderate cost.

6. **Frequency reduction / random chance** (cheap): `frequencyReductionMethod` or chance-based filter may reduce placements — cheap RNG.

7. **Biome filter** (moderate): consult biome at the candidate location or check biome tags for structure compatibility. This requires fetching biome information for world coordinates — cost depends on whether chunk/biome data is already loaded.

8. **Terrain & heightmap queries** (moderate-to-expensive): to decide where a structure would actually start (e.g. find surface y), code queries heightmaps and/or sample block states. These are heavier because they may require accessing region/chunk data or computing noise if not already present. If generation runs during worldgen (chunk being generated) some of this data is cheap; in lookups outside generation this can trigger chunk loads.

9. **Structure start creation simulation / collision checks** (expensive): the engine attempts to place structure pieces (or at least initialize a `StructureStart`) to validate constraints like whether it fits/overlaps with existing structures, whether required blocks/ground meet structure rules, or custom `Structure`-specific placement tests. This can perform many block/state checks and can be CPU heavy.

10. **Finalize / register structure** (moderate): if all checks pass, create and register structure start and mark the chunk as containing the structure. This updates region structure indices and may write to chunks/region metadata.

---

## Cheap vs expensive operations (short list and justification)

**Cheap (low cost, should run first):**

* Integer arithmetic for spacing / grid coordinate calculation (division, modulus).
* PRNG using seeded LCG/xorshift for offsetting grid cell (deterministic). No per-block work.
* Simple RNG chance/frequency checks.
* Checking small fields in the `StructurePlacement` object (spacing, separation, salt).

**Moderate:**

* Accessing `StructureAccessor` to query nearby structures' bounding boxes or indices (usually cached, but can grow with the number of structures examined).
* Biome lookup at a chunk/pos: if the biome is already known (chunk loaded), cheap; otherwise moderate due to lookups.
* Heightmap read when chunk already present (moderate).

**Expensive (avoid early):**

* Forcing chunk/region loads to read many block states or heightmaps (I/O / memory overhead).
* Simulating/generating `StructureStart` / placing multiple `StructurePiece`s to check collisions (per-block operations, recursion). This is the most expensive step because it may perform many block-level queries and allocate temporary objects.
* Complex structure-specific placement tests (custom code inside Structure classes) — unpredictable cost.

**Why these distinctions matter:** the engine's cost multiplies with the number of candidate structures and the percentage that progress into moderate/expensive phases. The cheap checks are intentionally first to filter out most candidates.

---

## Code flow diagram (mermaid)

```mermaid
flowchart TD
  A[Start per-candidate structure] --> B{StructurePlacement present?}
  B -- no --> Z[skip]
  B -- yes --> C[Compute grid cell (spacing)]
  C --> D[Compute potential chunk via seeded PRNG]
  D --> E{currentChunk == potentialChunk?}
  E -- no --> Z
  E -- yes --> F[Separation distance check]
  F --> G[Exclusion zone & interactions?]
  G --> H[Frequency reduction / chance]
  H --> I[Biome filter check]
  I --> J[Heightmap / terrain queries]
  J --> K[Attempt StructureStart / collision test]
  K --> L{fits / passes tests?}
  L -- no --> Z
  L -- yes --> M[Register structure start]
  M --> N[Done]
```

> The flow intentionally orders cheap checks before heavy ones to allow early short-circuit.

---

## When checks get short-circuited (and why it matters)

* If **grid cell calculation** shows the current chunk is not the potential chunk, the algorithm returns quickly — this is the single biggest early win.
* If the **separation** parameter fails (candidate too close to cell border or to previously placed structure), stop early.
* **Frequency reduction** (chance) avoids doing expensive biome/terrain checks for randomly-thinned candidates.
* If **biome tag check** fails, the engine avoids heightmap lookups and structure start creation.

**Practical tip:** Re-ordering or adding more pre-checks (e.g., a cheap cached biome-tag index per chunk) can massively reduce expensive operations.

---

## Biome filtering — where it integrates

Biome checks happen after the grid/separation checks but before structure start generation. Typical sequence:

1. Candidate chunk -> compute (x,z).
2. Determine candidate position inside chunk (center or offset) -> query biome at that world position.
3. Compare biome or biome tags with structure's allowed list (some structures use tags to allow many biomes).

If biome lookup requires loading the chunk or neighboring chunks, that can become a heavy operation. Many mods optimize this by checking biome tags at chunk-level earlier (cache) or by using less granular checks.

---

## Spacing & separation explained

* **spacing**: size of the logical grid cell. Larger spacing means fewer cells to consider across the world (fewer *potential* placements).
* **separation**: minimum distance within the cell before a structure is allowed — it prevents clustering near cell edges.

Effect on checks:

* Larger `spacing` -> fewer candidate grid cells -> fewer times the pipeline runs per area, reducing overall cost.
* Larger `separation` -> more candidates rejected during the cheap separation test, reducing expensive work.

---

## Specific functions & where to look in code (method names)

Look for these key methods in your mapped/decompiled version (these are stable entry points across versions):

* `RandomSpreadStructurePlacement.getPotentialStructureChunk(...)` — computes potential chunk for a grid cell.
* `RandomSpreadStructurePlacement.isPlacementChunk(...)` — orchestrates whether this chunk is a structure chunk (calls getPotentialStructureChunk and other checks).
* `StructurePlacement.isStructureChunk(...)` / `StructurePlacement.applyAdditionalChunkRestrictions(...)` — higher-level placement hooks.
* `ChunkGenerator.locateRandomSpreadStructure(...)` — used for locating structures by seed; interacts with placement.
* `StructureStart` creation and `Structure.start()` / `Structure.checkStartPlacement(...)` — where actual piece placement / collision validation happens.

(Use the mappings/Javadoc for your specific Minecraft version to get the exact file and line numbers.)

---

## Example optimizations (practical)

* **Cache biome tags per chunk** so you can cheaply filter many structure candidates without reading full chunk or heightmap.
* **Increase spacing** for expensive, rare structures to avoid many candidate checks.
* **Short-circuit custom structure checks** by making a preliminary fast predicate (e.g., check surfaceY by sampling a single heightmap value before attempting full structure generation).
* **Avoid forcing chunk loads** during background lookups; prefer queries during chunk generation where data is already in-memory.

---

## Notes about code references & line numbers

Line numbers in the decompiled sources differ between versions (1.18 → 1.21+). For reproducible references, open the decompiled/mapped source for the exact version you run (Paper/Spigot/Vanilla mappings), and search for the method names listed above. The mapping pages used to craft this doc are good starting points.

---

## Next steps / deliverables checklist

* [x] Read `RandomSpreadStructurePlacement` and related mapping docs.
* [x] Reviewed mapping/Javadoc summaries and derived algorithmic pipeline.
* [x] Wrote step-by-step explanation and cheap/expensive classification.
* [x] Produced mermaid code flow diagram (in this doc).
* [ ] (Optional) Add exact line-numbered code excerpts from the exact Minecraft jar version you run — drop me your server jar or specify exact game version/build and I will extract line-numbered references.

---

## Appendix: mermaid (copy-paste)

Use the mermaid block above to render the flow in tools that support mermaid.

---

*End of document.*
