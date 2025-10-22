# Xeenaa Structure Manager

**Minecraft Version**: 1.21.1
**Mod Loader**: Fabric
**Java Version**: 21
**Created**: 2025-10-22
**Author**: Canya

## Project Vision

The Xeenaa Structure Manager aims to solve the problem of world clutter and poor structure distribution when using multiple mods and datapacks. By providing intelligent structure management with configurable density, distance, and type-based organization, this mod creates a cleaner, more organized Minecraft world while simultaneously improving performance.

## Goals

- **Reduce world clutter** by intelligently managing structure spawning across all loaded mods and datapacks
- **Provide player control** through configurable distance, weight, and density settings per structure type
- **Classify structures by type** (small, medium, large, town, dungeon, mineshaft, etc.) for better organization
- **Enhance performance** by optimizing structure generation and reducing unnecessary computations
- **Maintain compatibility** with existing mods and datapacks without requiring changes to them

## Technical Stack

### Dependencies
- **Fabric API**: Latest for 1.21.1
- **Fabric Loader**: Latest (0.16.7+)
- **Java**: 21

### Build System
- **Gradle**: 8.x
- **Fabric Loom**: Latest

## Architecture Overview

### Package Structure
```
src/main/java/com/canya/xeenaa_structure_manager/
├── config/          # Configuration system for user settings
├── registry/        # Structure registry and classification
├── manager/         # Core structure management logic
├── performance/     # Performance optimization systems
└── mixin/           # Mixins for structure generation hooks
```

### Core Systems

**To be defined as epics are created**

Initial system considerations:
- Structure classification engine (analyze and categorize structures)
- Configuration management (per-type distance/density/weight settings)
- Generation interceptor (hook into structure placement pipeline)
- Performance optimizer (reduce tick impact, cache decisions)
- Compatibility layer (handle mod/datapack variations)

## Development Guidelines

### Code Standards
- Follow `coding-standards skill`
- Java 21 modern features (records, pattern matching, text blocks)
- Fabric events over mixins when possible
- Client-server separation (config GUI on client, logic on server)

### Epic Organization
- Each epic represents a major feature area
- Epic requirements define WHAT (business value)
- Epic tasks define HOW (technical implementation)
- User validates requirements before task creation

## Project Scope

### In Scope

- **Structure classification system** - Automatic detection and categorization of structures from any source
- **Configurable density controls** - Per-type settings for spawn distance, weight, and frequency
- **Performance optimization** - Reduce computational overhead of structure generation
- **Multi-mod compatibility** - Work seamlessly with any mod or datapack that adds structures
- **Configuration** - User-friendly config file for managing structure settings
- **Preset system** - Pre-configured profiles for different playstyles (sparse, balanced, dense)

### Out of Scope

- **Custom structure generation** - This mod manages existing structures, doesn't create new ones
- **Terrain modification** - Only manages structure placement, not world generation
- **Biome control** - Structures remain in their intended biomes
- **Dimension-specific features** - Initial version focuses on Overworld (expansion possible later)
- **Structure editing/modification** - Only controls spawn behavior, not structure design

## Performance Targets

This is a **performance-focused mod**. All features must demonstrate measurable performance improvement:

- **Target**: Reduce structure generation overhead by at least 20-30%
- **Measurement**: Before/after profiling with Spark required for all optimization features
- **Constraint**: No feature should increase tick time or memory usage
- **Benchmark**: Test with 50+ structure-adding mods to validate scalability

## Compatibility

- **Minecraft Versions**: 1.21.1 (may expand to other versions later)
- **Mod Compatibility**: Universal compatibility with any mod/datapack adding structures via standard Minecraft systems
- **Server/Client**: Both (server-side logic, client-side configuration GUI)
- **Multiplayer**: Full server support with client config sync

## Special Considerations

### Structure Detection Strategy
The mod must handle structures from:
- Vanilla Minecraft
- Fabric mods using standard registries
- Datapacks using standard structure definitions
- Legacy mod structures (compatibility layer needed)

### Performance-First Design
Every system must be designed with performance in mind:
- Lazy evaluation where possible
- Caching of classification decisions
- Minimal impact on world generation pipeline
- Efficient data structures for lookups

## Epic Status

No epics created yet. Run `/create_epic "Epic Name"` to add your first epic.

## Notes

- Structure classification may require machine learning or heuristic-based categorization
- Configuration should support both global defaults and per-world overrides
- Consider integration with world preset system (1.21.1 feature)
- Research existing structure management approaches in other mods
