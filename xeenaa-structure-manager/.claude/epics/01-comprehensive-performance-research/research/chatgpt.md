# Minecraft Structure Placement — RandomSpreadStructurePlacement

**Status:** Draft (IN_PROGRESS)

**Location:** `.claude/epics/01-feature-research/research/algorithm/StructurePlacement_Algorithm.md`

---

## Executive summary

This document explains how Minecraft decides whether a structure should place at a candidate chunk/position, focusing on the `RandomSpreadStructurePlacement` pipeline used by many vanilla structures and by modded structure managers. The goal is to give a readable, step-by-step account for engineers unfamiliar with Minecraft internals, identify expensive checks, and provide concrete places in the decompiled Fabric dev environment (file:line placeholders) to verify claims.

This draft consolidates prior research assets and provides diagrams, complexity analysis, and an action plan to add concrete code references and profiling numbers.

---

## Table of contents

1. Background & terminology
2. High-level pipeline (one-paragraph flow)
3. Detailed step-by-step pipeline

    * candidate selection
    * spacing/radius check (`RandomSpreadStructurePlacement` core)
    * terrain/height checks
    * biome filtering
    * structure start stage (`STRUCTURE_START`) and gating
    * jigsaw assembly (if applicable)
    * placement / chunk-ticket work
4. Complexity analysis (O(n), O(n²), etc.)
5. Expensive vs cheap operations with justification
6. ASCII flow diagrams / decision trees
7. Where to find the code (placeholders for file:line references)
8. Suggested profiling runs & how to capture numbers
9. Acceptance checklist & next steps

---

## 1) Background & terminology (for non-experts)

* **Chunk**: 16×16 block column; Minecraft uses chunk-based generation.
* **Structure placement**: Decision to create a `StructureStart` at a chunk coordinate and then expand into pieces.
* **StructureStart**: Record that a structure exists and the root of its generated pieces.
* **StructureFeature vs StructurePlacement**: `StructureFeature` (or `Structure`) defines what to generate; `StructurePlacement` defines when/where it is attempted.
* **RandomSpreadStructurePlacement**: A placement policy that attempts to spread occurrences roughly evenly (used for villages, strongholds, etc.).
* **Jigsaw system**: Modular mechanism where a structure is assembled from pieces according to jigsaw pools (village buildings, bastions, shipwrecks, etc.).
* **STRUCTURE_START stage**: Worldgen stage where structure starts are attempted; occurs once per chunk generation pass at the designated step.

---

## 2) High-level pipeline (one paragraph)

For each candidate chunk where Minecraft *might* place a structure, the `RandomSpreadStructurePlacement` spacing algorithm first determines whether this chunk is an eligible slot based on configured grid/spacing and randomness. If it passes, Minecraft evaluates quick filters (dimension, feature toggle), then biome constraints, then nearby structure collisions, then attempts to instantiate a `StructureStart`. If the structure uses jigsaw pools it will perform recursive piece assembly (with branching and bounding checks). Successful starts are written to the world; failed attempts roll back any temporary state. The most expensive parts are often biome checks (when many biome lookups per candidate are needed), jigsaw recursive assembly (when many pieces spawn), and neighbor-structure collision checks when scanning many nearby chunks.

---

## 3) Step-by-step pipeline (detailed)

Below is the canonical sequence used by vanilla `RandomSpreadStructurePlacement` and typical Fabric-modded analogs. Each numbered step includes the rationale and the expected cost profile.

### Step 0 — Initialization / global filters (very cheap)

* Check whether the `StructureFeature` is enabled by world settings (config flags, datapack), dimension rules, and basic RNG seed checks. These are constant-time checks (O(1)).

### Step 1 — Calculate candidate chunk (cheap)

* `RandomSpreadStructurePlacement` partitions world into a grid using `spacing` and `separation` parameters. For a given chunk x/z, compute the grid cell and candidate chunk coordinate.
* Operations: integer division, modulus, and RNG derived from world seed. Complexity: O(1).

### Step 2 — Region-level randomness & slot acceptance (cheap)

* The placement computes a pseudo-random offset within the cell to vary exact placement. Typically involves seeded PRNG invocations (few calls). O(1).

### Step 3 — Quick location filters (cheap)

* Dimension whitelist/blacklist, blacklist for nether/end, or custom config toggles. O(1).

### Step 4 — Biome filtering (moderate — potentially expensive)

* Query biomes for a target region (usually a small rectangle or single block). Vanilla sometimes queries a `BiomeSource` at a single point; mods or validation code may sample multiple points to ensure large-structure biome consistency.
* Each biome query may involve noise sampling or climate parameter lookups, which can be moderately costly. If placement queries many sample points (k), cost is O(k) per candidate. If k scales with structure size, cost can grow with O(structure_area).

### Step 5 — Nearby structure collision and map check (moderate to expensive)

* The algorithm checks whether the candidate chunk conflicts with other already-placed structures per configured `minimumDistance` rules. This can be implemented by scanning a radius R in chunks and checking lookup tables of existing structure starts; naive implementations that query many neighbors yield O(R²) checks per candidate.

### Step 6 — Height / surface suitability checks (cheap to moderate)

* Some structures perform surface scanning to find a suitable Y coordinate. Depending on implementation this can be a few block reads or a scan over a small area (O(area)).

### Step 7 — Attempt to create `StructureStart` (moderate)

* If all filters pass, code calls the `generate`/`createStart` routine which initialises a `StructureStart` and begins assembling pieces.
* This may involve allocating objects and performing piece-creation logic — moderate CPU and memory.

### Step 8 — Jigsaw assembly (expensive for large structures)

* If the structure uses the jigsaw system, the generation triggers recursive placement of pieces using pools. Each piece may connect to multiple children, resulting in a branching tree. Worst-case number of piece attempts can be large (N pieces), and each piece may require bounding checks, block checks, and further biome checks.
* Complexity: O(N) where N is number of pieces attempted, but with branching and repeated neighbor queries and bounding box intersection checks, effective cost can be larger. If piece placement requires scanning local terrain or sampling biomes for each piece, per-piece cost multiplies.

### Step 9 — Finalize or rollback (cheap to moderate)

* If assembly produces a valid `StructureStart`, it is recorded. If it fails (e.g., pieces out-of-bounds or too many collisions), the attempt is discarded. Disk/region map write may occur later when chunks are saved.

---

## 4) Complexity patterns (summary)

* Constant-time checks: O(1) — seed math, mod/quotient, config flag checks.
* Biome sampling: O(k) per candidate where k is number of sample points. If k ~ structure footprint, this is O(area).
* Nearby-structure checks: O(R²) naive — scanning radius R chunks. With hashed lookup/set it becomes O(number_of_neighbors_examined).
* Jigsaw assembly: O(N) in number of pieces attempted. But if pieces branch and each triggers expensive biome or collision checks, effective cost is O(N * cost_per_piece).
* When many candidates are evaluated (e.g., 569 structure placements attempted during worldgen), total cost is `sum(cost_per_candidate)`; worst-case if many candidates reach jigsaw stage, cost multiplies dramatically.

---

## 5) Expensive vs cheap operations (with justification)

* **Cheap** (low CPU & memory): RNG math, grid arithmetic, single biome point query, dimension checks.
* **Moderate**: Biome queries relying on climate/noise sampling, small area height scans, object allocation for `StructureStart`.
* **Expensive**: Jigsaw recursive assembly (large N), scanning large neighbor radii to check collisions (R large), repeated biome sampling across many pieces, block-level terrain scans over large footprints.

**Why**: biome queries typically call into `BiomeSource` which uses noise eval; jigsaw assembly may create and evaluate dozens to hundreds of piece candidates and perform per-piece validation; neighbor collision checks over large radii multiply by number of candidate chunks.

---

## 6) ASCII diagrams & decision trees

Candidate chunk evaluated

```
+-------------------------------------+
| compute grid cell & offset (O(1))   |
| -> quick filters (O(1))             |
| -> biome filter (O(k))              |
| -> neighbor/collision (O(R^2))      |
| -> height check (O(a))              |
| -> create StructureStart (O(1))     |
|    -> jigsaw assemble (O(N))        |
|    -> success -> record start       |
|    -> failure -> rollback           |
+-------------------------------------+
```

Jigsaw piece expansion (tree)

```
root piece
  ├─ child A
  │   ├─ child A1
  │   └─ child A2
  ├─ child B
  │   └─ child B1
  └─ child C
```

Each node may perform biome checks, bounding checks, and may spawn more children.

---

## 7) Where to find the code (placeholders)

> **IMPORTANT**: The lines below are placeholders. Replace them by opening your Fabric decompiled workspace and search for the following classes/identifiers and then paste exact `file:line` references.

* `RandomSpreadStructurePlacement` — class implementing the spacing logic. (search for `class RandomSpreadStructurePlacement`)

    * placeholder: `minecraft/server/level/levelgen/structure/RandomSpreadStructurePlacement.java:???`
* `StructureFeature` or `Structure` generation entrypoints — where `createStart` is invoked.

    * placeholder: `minecraft/server/level/levelgen/structure/StructureFeature.java:???`
* `NetherFortress` / `VillageFeature` / `JigsawStructure` — example jigsaw using features.

    * placeholder: `minecraft/server/level/levelgen/structure/JigsawStructure.java:???`
* `JigsawPlacement` / `PoolStructurePiece` / `StructurePoolBasedGenerator` — jigsaw piece creation.

    * placeholder: `minecraft/server/level/levelgen/structure/PoolStructurePiece.java:???`
* `BiomeSource` and `Biome` queries called during placement.

    * placeholder: `minecraft/world/level/biome/BiomeSource.java:???`

---

## 8) Suggested profiling runs & how to capture numbers (practical)

1. **Instrument candidate evaluation**: add counters around the `RandomSpreadStructurePlacement` entry to count how many candidates hit each pipeline stage.

    * Metrics: candidates_total, candidates_biome_fail, candidates_collision_fail, candidates_jigsaw_attempt.
2. **Per-piece profiling**: measure time spent in jigsaw piece generation and number of pieces attempted per start.
3. **Neighbor scan profiling**: measure cost of scanning radius R by counting neighbors checked per candidate.

**Commands / hints**:

* Run server with `-agentlib:jdwp=...` or use a lightweight profiler (e.g., async-profiler) to sample and produce flamegraphs for worldgen tick or chunk generation.
* For deterministic tests: create a small worldseed and trigger generation for a fixed region (e.g., 1024×1024 blocks) and count the number of candidate evaluations.

---

## 9) Acceptance checklist & next steps (what I will do next)

* [x] Draft high-level pipeline and diagrams (this document)
* [ ] Replace code placeholders with exact `file:line` references from Fabric decompiled source (requires opening your dev workspace)
* [ ] Add profiling numbers and justify expensive operations with real measurements (requires running profiling on your server)
* [ ] Produce final diagrams (SVG/PNG) from ASCII diagrams and embed into the research folder
* [ ] Final review to ensure "why 569 structures = slow" is illustrated with data and call stacks

**Immediate next actions I will take** (if you want me to continue without delay):

* Pull exact file:line refs from a Fabric decompiled workspace (I can search the web for vanilla references or, preferably, you can provide the decompiled tree). *I will not proceed unless you want me to fetch these references or provide access to your decompiled workspace.*

---

## Appendix A — Quick checklist to reproduce profiling

1. Launch a dedicated Fabric server with a small mod set and logging enabled.
2. Use a fixed seed and generate a 64×64 chunk region around spawn.
3. Add logging in `RandomSpreadStructurePlacement` entry to count candidates and time spent.
4. Run async-profiler during the generation run and capture flamegraph.

---

*End of draft*
