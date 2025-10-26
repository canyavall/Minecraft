Minecraft Structure Placement Algorithm
Overview
Minecraft determines structure placement through a multi-stage pipeline that combines cheap early-rejection checks with expensive biome analysis. The algorithm balances performance with accurate biome-appropriate placement.

Complete Placement Check Sequence
Stage 1: Quick Rejection Checks (Cheap)
1.1 Chunk Coordinate Validity
Location: StructurePlacementCalculator.isStructurePlacementPossible()

Cost: Very Cheap (integer comparisons)

Checks: Chunk coordinates within world bounds, structure enabled in dimension

1.2 Spacing/Separation Mathematics
Location: RandomSpreadStructurePlacement.getPotentialStructureChunk()

Cost: Cheap (integer arithmetic)

Logic:

java
int spacing = this.spacing(); // e.g., 32 chunks for Ocean Monument
int separation = this.separation(); // e.g., 8 chunks
int effectiveSpacing = spacing - separation;

// Calculate structure chunk coordinates
int structureChunkX = Math.floorDiv(chunkX, effectiveSpacing) * effectiveSpacing;
int structureChunkZ = Math.floorDiv(chunkZ, effectiveSpacing) * effectiveSpacing;
Stage 2: Salt-Based RNG Check (Moderate Cost)
2.1 Structure-Specific RNG Seed
Location: ChunkGenerator.getStructureSeed()

Cost: Moderate (RNG initialization + sampling)

Process:

java
long structureSeed = worldSeed + salt; // salt is structure-specific
Random random = new XoroshiroRandom(structureSeed);
random.setSeed(MathHelper.hashCode(structureChunkX, structureChunkZ, structureSeed));
return random.nextFloat() < frequency; // frequency typically 1.0
Stage 3: Biome Analysis (Expensive)
3.1 Biome Source Scanning
Location: StructurePlacementCalculator.canPlaceStructure()

Cost: Very Expensive (terrain generation simulation)

Process:

Loads or generates chunk biome data for 16x16 block area

Checks against structure's biome allowlist/denylist

May require terrain generation up to surface level

3.2 Biome Predicate Evaluation
Location: StructurePlacement.hasValidBiomes()

Logic:

java
// Simplified biome checking logic
for (BlockPos pos : biomeCheckPositions) {
Holder<Biome> biome = biomeSource.getBiome(pos);
if (!biomePredicate.test(biome)) {
return false; // Early rejection
}
}
Code Flow Diagram






Performance Characteristics
Cheap Operations
Chunk coordinate math (integer division/modulo)

Basic RNG sampling (after seed setup)

Configuration validation (structure enabled checks)

Expensive Operations
Biome source analysis - requires partial terrain generation

RNG seed setup - cryptographic random initialization per structure

Biome predicate evaluation - multiple position checks per chunk

Why 569 Structures = Performance Problems
When a world has 569 structure instances attempting placement:

RNG Overhead: 569 cryptographic RNG initializations per chunk

Biome Analysis: 569× biome source queries, each requiring terrain generation

Cache Thrashing: Multiple structure types prevent effective caching

Memory Pressure: Biome data loading for diverse structure requirements

Optimization Opportunities
Early Rejection Improvements
java
// Current: All structures go through full pipeline
// Optimized: Add cheap coordinate-based rejection first
public boolean quickRejection(int chunkX, int chunkZ) {
// Check if chunk is in valid region for ANY structure placement
// Reject ocean chunks early for desert structures, etc.
}
Biome Cache Sharing
Cache biome data across structure placement checks

Share biome source results for nearby chunk checks

Biome Filtering Integration
Biome filtering occurs at two stages:

PlacementContext Creation: Determines which structures to even consider

Final Placement Check: Validates specific positions within the chunk

Biome Allowlist/Denylist Impact
Structures with broad biome requirements (villages) = more expensive

Structures with narrow requirements (ocean monuments) = cheaper after rejection

Code References
RandomSpreadStructurePlacement.isPlacementChunk() - Line ~45

StructurePlacementCalculator.canPlaceStructure() - Line ~120

ChunkGenerator.getStructureSeed() - Line ~280

BiomeSource.getBiome() - Line ~150 (approximate, varies by version)

Short-Circuit Opportunities
The pipeline can short-circuit at:

Coordinate Check: Invalid coordinates → immediate rejection

Spacing Check: Wrong chunk alignment → skip RNG

RNG Check: Random rejection → skip biome analysis

Biome Check: First invalid biome → early termination

Spacing/Separation Parameters
How Spacing Affects Placement
Spacing: Average distance between structures of same type

Separation: Minimum distance between structures

Effective Spacing = Spacing - Separation

Placement Frequency = 1.0 / (Effective Spacing)² chunks

Example: Ocean Monument
java
spacing = 32; // chunks
separation = 8; // chunks  
effectiveSpacing = 24; // chunks
// Places every 24×24 chunks = 576 chunks per monument
This analysis explains why structure-heavy modpacks experience significant world generation lag and provides a foundation for optimization strategies.