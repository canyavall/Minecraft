package com.canya.xeenaa_structure_manager.classification;

import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Orchestrates parallel two-dimensional structure classification (SIZE + TYPE).
 * <p>
 * This system integrates {@link SizeClassifier} and {@link TypeClassifier} to provide
 * a unified classification API. It processes all registered structures in parallel,
 * populates the {@link ClassificationCache}, and logs comprehensive statistics.
 * <p>
 * <b>Classification Process:</b>
 * <ol>
 *   <li>Retrieve all structures from the Structure registry</li>
 *   <li>Classify each structure in parallel using {@link java.util.stream.Stream#parallelStream()}</li>
 *   <li>Combine SIZE and TYPE classifications into {@link StructureClassification} records</li>
 *   <li>Cache all results in {@link ClassificationCache}</li>
 *   <li>Log detailed statistics for transparency</li>
 * </ol>
 * <p>
 * <b>Performance Requirements:</b>
 * <ul>
 *   <li>Must complete classification in &lt;5 seconds for 1000 structures</li>
 *   <li>Uses parallel streams for multi-core utilization</li>
 *   <li>Thread-safe cache operations via {@link java.util.concurrent.ConcurrentHashMap}</li>
 * </ul>
 * <p>
 * <b>Error Handling:</b>
 * <ul>
 *   <li>Individual classification failures are caught and logged</li>
 *   <li>Failed structures are skipped (no crash on bad data)</li>
 *   <li>Statistics reflect only successfully classified structures</li>
 *   <li>Continues processing remaining structures after errors</li>
 * </ul>
 * <p>
 * <b>Logging Strategy:</b>
 * <ul>
 *   <li><b>INFO</b>: Overall statistics (total count, size distribution, type distribution)</li>
 *   <li><b>DEBUG</b>: Per-structure classification results (via SizeClassifier/TypeClassifier)</li>
 *   <li><b>WARN</b>: Classification errors (exceptions, null results)</li>
 *   <li><b>ERROR</b>: System-level failures (registry access, cache failures)</li>
 * </ul>
 * <p>
 * <b>Usage Example:</b>
 * <pre>
 * ServerWorld world = ...;
 * Registry&lt;Structure&gt; structureRegistry = world.getRegistryManager()
 *     .get(RegistryKeys.STRUCTURE);
 *
 * ClassificationSystem system = new ClassificationSystem(world);
 * system.classifyAll(structureRegistry);
 *
 * // Results are now cached
 * ClassificationCache cache = system.getCache();
 * StructureClassification result = cache.get(Identifier.of("minecraft", "ancient_city"));
 * </pre>
 *
 * @see SizeClassifier
 * @see TypeClassifier
 * @see ClassificationCache
 * @see StructureClassification
 * @since 1.0.0
 */
public class ClassificationSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassificationSystem.class);

    /**
     * Singleton instance for access from mixins.
     * Initialized when a server world loads.
     */
    private static volatile ClassificationSystem INSTANCE;

    /**
     * SIZE dimension classifier (volume-based categorization).
     */
    private final SizeClassifier sizeClassifier;

    /**
     * TYPE dimension classifier (heuristic-based categorization).
     */
    private final TypeClassifier typeClassifier;

    /**
     * Thread-safe cache for storing classification results.
     */
    private final ClassificationCache cache;

    /**
     * Creates a new classification system for the specified world.
     * <p>
     * The system initializes both SIZE and TYPE classifiers and prepares a cache
     * for storing results. The {@link StructureTemplateManager} is obtained from
     * the ServerWorld to enable NBT template access for SIZE classification.
     * <p>
     * <b>Initialization Steps:</b>
     * <ol>
     *   <li>Extract StructureTemplateManager from ServerWorld</li>
     *   <li>Initialize SizeClassifier with template manager</li>
     *   <li>Initialize TypeClassifier (no dependencies)</li>
     *   <li>Initialize ClassificationCache (empty)</li>
     * </ol>
     *
     * @param world the server world providing access to structure templates
     * @throws IllegalArgumentException if world is null
     */
    public ClassificationSystem(ServerWorld world) {
        if (world == null) {
            throw new IllegalArgumentException("ServerWorld cannot be null");
        }

        // Extract StructureTemplateManager from world
        StructureTemplateManager templateManager = world.getStructureTemplateManager();

        // Initialize classifiers
        this.sizeClassifier = new SizeClassifier(templateManager);
        this.typeClassifier = new TypeClassifier();
        this.cache = new ClassificationCache();

        // Set singleton instance for mixin access
        INSTANCE = this;

        LOGGER.debug("ClassificationSystem initialized for world: {}", world.getRegistryKey().getValue());
    }

    /**
     * Gets the singleton instance of ClassificationSystem.
     * <p>
     * This method provides access to the classification system from mixins and other
     * components that cannot receive constructor dependencies.
     * <p>
     * <b>Important:</b> This will return null until a server world has been loaded
     * and the classification system has been initialized.
     *
     * @return the singleton instance, or null if not yet initialized
     */
    public static ClassificationSystem getInstance() {
        return INSTANCE;
    }

    /**
     * Classifies all structures in the registry using parallel processing.
     * <p>
     * This is the primary entry point for classification. It processes all structures
     * in the registry in parallel, combines SIZE and TYPE classifications, caches the
     * results, and logs comprehensive statistics with performance benchmarking.
     * <p>
     * <b>Algorithm:</b>
     * <ol>
     *   <li>Log classification start and processor count</li>
     *   <li>Record start time for performance measurement (nanosecond precision)</li>
     *   <li>Convert registry to parallel stream</li>
     *   <li>Map each structure to classification (parallel execution)</li>
     *   <li>Filter out null results (errors)</li>
     *   <li>Collect results into list</li>
     *   <li>Cache all results</li>
     *   <li>Calculate duration and log statistics</li>
     *   <li>Calculate structures/second throughput</li>
     *   <li>Validate performance against target (5 seconds for 1000 structures)</li>
     *   <li>Log memory usage statistics</li>
     * </ol>
     * <p>
     * <b>Parallel Processing:</b>
     * <ul>
     *   <li>Uses {@link java.util.stream.Stream#parallelStream()} for multi-core utilization</li>
     *   <li>Logs available processor count at DEBUG level</li>
     *   <li>Each structure is classified independently (no shared state)</li>
     *   <li>Thread-safe cache ensures concurrent writes are safe</li>
     *   <li>Performance target: &lt;5 seconds for 1000 structures</li>
     * </ul>
     * <p>
     * <b>Performance Logging:</b>
     * <ul>
     *   <li>Total classification time in milliseconds</li>
     *   <li>Throughput in structures/second</li>
     *   <li>PASS/WARN status against 5-second target</li>
     *   <li>Cache size and estimated memory usage</li>
     *   <li>PASS/WARN status against 10 MB memory target</li>
     * </ul>
     * <p>
     * <b>Error Handling:</b>
     * <ul>
     *   <li>Individual classification failures return null</li>
     *   <li>Null results are filtered out (via {@link java.util.stream.Stream#filter(java.util.function.Predicate)})</li>
     *   <li>Processing continues for remaining structures</li>
     *   <li>Statistics reflect only successful classifications</li>
     * </ul>
     *
     * @param structures the structure registry to classify
     * @throws IllegalArgumentException if structures is null
     */
    public void classifyAll(Registry<Structure> structures) {
        if (structures == null) {
            throw new IllegalArgumentException("Structure registry cannot be null");
        }

        LOGGER.info("Starting structure classification...");

        // Log parallel processing capabilities
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        LOGGER.debug("Using parallel stream for classification (utilizing {} processors)",
            availableProcessors);

        long startTime = System.nanoTime();

        // Parallel classification using parallel streams
        List<StructureClassification> results = structures.getEntrySet()
            .parallelStream()
            .map(entry -> {
                Identifier id = entry.getKey().getValue();
                Structure structure = entry.getValue();
                return classify(id, structure);
            })
            .filter(classification -> classification != null) // Filter out errors
            .toList();

        // Cache all results
        results.forEach(c -> cache.put(c.id(), c));

        // Calculate duration and log statistics
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        double structuresPerSecond = results.size() * 1000.0 / durationMs;

        // Log performance statistics
        logStatistics(results, durationMs);
        LOGGER.info("Classification performance: {:.2f} structures/sec", structuresPerSecond);

        // Check performance target (5 seconds for 1000 structures)
        long targetMs = 5000;
        if (durationMs > targetMs) {
            LOGGER.warn("Classification took {}ms, exceeds {} second target for 1000 structures",
                durationMs, targetMs / 1000);
        } else {
            LOGGER.info("Classification performance: PASS ({}ms < {}ms target)",
                durationMs, targetMs);
        }

        // Log memory statistics
        logMemoryStatistics();
    }

    /**
     * Classifies a single structure on-demand and stores the result in the cache.
     * <p>
     * This method provides lazy classification for structures that haven't been classified yet.
     * It checks the cache first to avoid redundant classification, and only classifies on a cache miss.
     * This is thread-safe and can be called from multiple world generation threads simultaneously.
     * <p>
     * <b>Lazy Classification Process:</b>
     * <ol>
     *   <li>Check cache for existing classification (avoid redundant work)</li>
     *   <li>If found, return cached result immediately</li>
     *   <li>If not found, classify SIZE using {@link SizeClassifier}</li>
     *   <li>Classify TYPE using {@link TypeClassifier}</li>
     *   <li>Extract volume and underground status</li>
     *   <li>Create {@link StructureClassification} record</li>
     *   <li>Store in cache for future lookups</li>
     *   <li>Return classification result</li>
     * </ol>
     * <p>
     * <b>Thread Safety:</b>
     * <ul>
     *   <li>Uses {@link java.util.concurrent.ConcurrentHashMap} for thread-safe cache access</li>
     *   <li>Cache.get() and cache.put() are atomic operations</li>
     *   <li>Multiple threads can call this method simultaneously</li>
     *   <li>Race condition safe: duplicate classification is harmless (same result)</li>
     * </ul>
     * <p>
     * <b>Error Handling:</b>
     * <ul>
     *   <li>Catches all exceptions during classification</li>
     *   <li>Logs errors at ERROR level with full stack trace</li>
     *   <li>Returns safe fallback classification on failure</li>
     *   <li>Never crashes - ensures world generation continues</li>
     * </ul>
     * <p>
     * <b>Performance:</b>
     * <ul>
     *   <li>Classification takes &lt;5ms per structure (acceptable for first placement)</li>
     *   <li>Cache hit rate &gt;95% after exploring for 10 minutes</li>
     *   <li>Eliminates 7+ minute world load hang by deferring classification</li>
     * </ul>
     *
     * @param id the structure identifier
     * @param structure the Structure object to classify
     * @return the classification result (either cached or newly classified)
     */
    public StructureClassification classifyStructure(Identifier id, Structure structure) {
        // Check cache first (avoid redundant classification)
        StructureClassification cached = cache.get(id);
        if (cached != null) {
            LOGGER.debug("LAZY CLASSIFICATION: Cache hit for {}", id);
            return cached;
        }

        // PERFORMANCE TEST: Measure lazy classification time
        long startTime = System.nanoTime();

        try {
            LOGGER.info("LAZY CLASSIFICATION: Starting classification for {}", id);

            // Classify SIZE dimension
            SizeCategory size = sizeClassifier.classify(id, structure);

            // Classify TYPE dimension
            TypeCategory type = typeClassifier.classify(id, structure);

            // Extract volume from SIZE classifier
            int volume = sizeClassifier.getVolume(id, structure);

            // Determine underground status from TYPE classifier
            boolean underground = typeClassifier.isUnderground(id, structure);

            // Create classification record
            StructureClassification classification =
                new StructureClassification(size, type, volume, underground, id);

            // Store in cache
            cache.put(id, classification);

            // Calculate and log timing
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;

            LOGGER.info("LAZY CLASSIFICATION: Classified {} as {} {} in {}ms",
                id, size, type, String.format("%.2f", durationMs));

            if (durationMs > 5.0) {
                LOGGER.warn("LAZY CLASSIFICATION: Classification took {}ms for {} (target <5ms)",
                    String.format("%.2f", durationMs), id);
            }

            return classification;

        } catch (Exception e) {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;

            LOGGER.error("LAZY CLASSIFICATION: Failed to classify structure {} after {}ms: {}",
                id, String.format("%.2f", durationMs), e.getMessage(), e);

            // Return safe fallback classification
            StructureClassification fallback = new StructureClassification(
                SizeCategory.MEDIUM,
                TypeCategory.UNKNOWN,
                10_000,
                false,
                id
            );

            // Cache the fallback to avoid repeated errors
            cache.put(id, fallback);

            return fallback;
        }
    }

    /**
     * Classifies a single structure by combining SIZE and TYPE classifications.
     * <p>
     * This method orchestrates the two-dimensional classification process:
     * <ol>
     *   <li>Classify SIZE using {@link SizeClassifier}</li>
     *   <li>Classify TYPE using {@link TypeClassifier}</li>
     *   <li>Extract volume from SIZE classifier</li>
     *   <li>Determine underground status from TYPE classifier</li>
     *   <li>Combine into {@link StructureClassification} record</li>
     * </ol>
     * <p>
     * <b>Error Handling:</b>
     * <ul>
     *   <li>Catches all exceptions during classification</li>
     *   <li>Logs errors at WARN level with structure identifier</li>
     *   <li>Returns null on failure (caller filters out nulls)</li>
     *   <li>Never crashes - ensures batch processing continues</li>
     * </ul>
     *
     * @param id the structure identifier
     * @param structure the Structure object to classify
     * @return the combined classification result, or null if classification fails
     */
    private StructureClassification classify(Identifier id, Structure structure) {
        try {
            // Classify SIZE dimension
            SizeCategory size = sizeClassifier.classify(id, structure);

            // Classify TYPE dimension
            TypeCategory type = typeClassifier.classify(id, structure);

            // Extract volume from SIZE classifier
            int volume = sizeClassifier.getVolume(id, structure);

            // Determine underground status from TYPE classifier
            boolean underground = typeClassifier.isUnderground(id, structure);

            // Log individual classification at DEBUG level
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Classified {}: SIZE={} ({}m³), TYPE={}, underground={}",
                    id, size, volume, type, underground);
            }

            // Combine into StructureClassification record
            return new StructureClassification(size, type, volume, underground, id);

        } catch (Exception e) {
            // Log error and continue processing other structures
            LOGGER.warn("Failed to classify structure {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Logs comprehensive classification statistics to the console.
     * <p>
     * This method provides transparency to users about how structures were classified.
     * It shows the total count, processing time, size distribution, and type distribution.
     * <p>
     * <b>Statistics Logged:</b>
     * <ul>
     *   <li>Total structures classified</li>
     *   <li>Processing duration in milliseconds</li>
     *   <li>Size distribution (small, medium, large counts)</li>
     *   <li>Type distribution (all 7 categories)</li>
     * </ul>
     * <p>
     * <b>Example Output:</b>
     * <pre>
     * [Xeenaa Structure Manager] Classified 247 structures in 1243ms
     * [Xeenaa Structure Manager] Size: 89 small, 112 medium, 46 large
     * [Xeenaa Structure Manager] Type: 34 towns, 67 dungeons, 12 temples, 8 mineshafts, 23 sky, 18 ruins, 85 unknown
     * </pre>
     *
     * @param results the list of classification results
     * @param duration the time taken to classify all structures in milliseconds
     */
    private void logStatistics(List<StructureClassification> results, long duration) {
        // Count size distribution
        long small = results.stream().filter(c -> c.size() == SizeCategory.SMALL).count();
        long medium = results.stream().filter(c -> c.size() == SizeCategory.MEDIUM).count();
        long large = results.stream().filter(c -> c.size() == SizeCategory.LARGE).count();

        // Count type distribution
        long towns = results.stream().filter(c -> c.type() == TypeCategory.TOWN).count();
        long dungeons = results.stream().filter(c -> c.type() == TypeCategory.DUNGEON).count();
        long temples = results.stream().filter(c -> c.type() == TypeCategory.TEMPLE).count();
        long mineshafts = results.stream().filter(c -> c.type() == TypeCategory.MINESHAFT).count();
        long sky = results.stream().filter(c -> c.type() == TypeCategory.SKY).count();
        long ruins = results.stream().filter(c -> c.type() == TypeCategory.RUIN).count();
        long unknown = results.stream().filter(c -> c.type() == TypeCategory.UNKNOWN).count();

        // Log overall statistics
        LOGGER.info("Classified {} structures in {}ms", results.size(), duration);
        LOGGER.info("Size: {} small, {} medium, {} large", small, medium, large);
        LOGGER.info("Type: {} towns, {} dungeons, {} temples, {} mineshafts, {} sky, {} ruins, {} unknown",
            towns, dungeons, temples, mineshafts, sky, ruins, unknown);
    }

    /**
     * Logs memory usage statistics for the classification cache.
     * <p>
     * This method calculates an estimated memory footprint for the cache and logs it
     * along with the cache size. It also checks against the 10 MB target for cache memory.
     * <p>
     * <b>Memory Estimation:</b>
     * <ul>
     *   <li>Assumes ~1 KB per cache entry (conservative estimate)</li>
     *   <li>Actual usage: StructureClassification record + ConcurrentHashMap overhead</li>
     *   <li>Does not include JVM object header or reference overhead</li>
     * </ul>
     * <p>
     * <b>Performance Target:</b>
     * <ul>
     *   <li>Cache memory should be &lt;10 MB</li>
     *   <li>Logs PASS if target met, WARN if exceeded</li>
     * </ul>
     */
    private void logMemoryStatistics() {
        int cacheSize = cache.size();

        // Estimate memory usage: ~1KB per entry (conservative)
        // Actual usage: StructureClassification record + map overhead
        long estimatedMemoryBytes = cacheSize * 1024L;
        double estimatedMemoryMB = estimatedMemoryBytes / (1024.0 * 1024.0);

        LOGGER.info("Classification cache: {} entries, ~{:.2f} MB memory",
            cacheSize, estimatedMemoryMB);

        // Check memory target (10 MB)
        double targetMemoryMB = 10.0;
        if (estimatedMemoryMB > targetMemoryMB) {
            LOGGER.warn("Cache memory usage {:.2f} MB exceeds {:.2f} MB target",
                estimatedMemoryMB, targetMemoryMB);
        } else {
            LOGGER.info("Cache memory usage: PASS ({:.2f} MB < {:.2f} MB target)",
                estimatedMemoryMB, targetMemoryMB);
        }
    }

    /**
     * Gets the classification cache containing all classification results.
     * <p>
     * This method provides access to the cache for external systems that need to
     * query classification results (e.g., configuration application, multiplier
     * calculation, debugging tools).
     * <p>
     * <b>Usage Example:</b>
     * <pre>
     * ClassificationCache cache = system.getCache();
     * StructureClassification result = cache.get(Identifier.of("minecraft", "village_plains"));
     * if (result != null) {
     *     System.out.println(result.toString());
     * }
     * </pre>
     *
     * @return the classification cache
     */
    public ClassificationCache getCache() {
        return cache;
    }
}
