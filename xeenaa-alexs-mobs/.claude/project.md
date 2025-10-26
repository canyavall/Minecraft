# Xeenaa's Alex's Mobs (Fabric)

**Minecraft Version**: 1.21.1
**Mod Loader**: Fabric
**Java Version**: 21
**Created**: 2025-10-25
**Author**: Canya

## Project Vision

Port the animal mobs from Alex's Mobs (NeoForge/Forge) to Fabric 1.21.1 with maximum fidelity to the original mod while maintaining Fabric-native performance and patterns.

## Goals

- Port all animal mobs from Alex's Mobs with accurate models, textures, and animations
- Implement mob AI and behaviors using Fabric-optimized patterns
- Port mob drops and items (including the ethical drop system)
- Port craftable items, weapons, armor, and potions
- Maintain or improve performance compared to the original Forge version
- Ensure compatibility with Fabric ecosystem and best practices

## Technical Stack

### Dependencies
- **Fabric API**: Latest for 1.21.1
- **Fabric Loader**: 0.16.7+
- **Java**: 21

### Build System
- **Gradle**: 8.x
- **Fabric Loom**: Latest

## Architecture Overview

### Package Structure
```
src/main/java/com/canya/xeenaa_alexs_mobs/
├── AlexsMobsMod.java           # Main mod initializer
├── client/                     # Client-only code
│   └── AlexsMobsClient.java   # Client initializer
├── entity/                     # Entity classes
│   ├── animal/                # Animal entities
│   └── ai/                    # AI goals and behaviors
├── item/                       # Items and tools
│   ├── drops/                 # Mob drops
│   └── crafted/               # Craftable items
├── registry/                   # Registration
│   ├── ModEntities.java
│   ├── ModItems.java
│   └── ModSounds.java
└── util/                       # Utilities
```

### Core Systems

**Entity System**:
- Port mob models using Fabric's entity rendering
- Implement AI using vanilla EntityAI patterns optimized for Fabric
- Use Data Components (1.21.1 feature) for entity NBT where appropriate

**Item System**:
- Port all mob drops and craftable items
- Implement ethical drop system (drops without killing when possible)
- Use Fabric's item API for custom tools and armor

**Client Rendering**:
- Port entity models and textures from original mod
- Use Fabric's EntityRendererRegistry for registration
- Implement custom animations where needed

## Development Guidelines

### Code Standards
- Follow `coding-standards` skill
- Java 21 modern features (records, pattern matching, text blocks)
- Fabric events over mixins when possible
- Client-server separation (client code in client/ package)
- Performance-first mindset

### Epic Organization
- Each epic represents a major feature area or group of mobs
- Epic requirements define WHAT (business value, game experience)
- Epic tasks define HOW (technical implementation)
- User validates requirements before task creation

## Project Scope

### In Scope

- **All animal mobs** from Alex's Mobs (models, textures, AI, behaviors, sounds)
- **Mob drops** (both killing and ethical drop system)
- **Craftable items** from mob drops (weapons, arrows, potions, armor)
- **Animal Dictionary** (in-game documentation book)
- **Spawn mechanics** (biome-based spawning)
- **Mob interactions** (breeding, taming where applicable)

### Out of Scope

- Non-animal mobs (unless user requests expansion)
- Structures or world generation features
- Dimension-specific content
- Compatibility with other mod loaders (Fabric-only port)

## Performance Targets

- **Entity tick performance**: Match or exceed original mod efficiency
- **Rendering performance**: Optimize for Fabric's rendering pipeline
- **Memory usage**: Efficient model and texture loading
- **Spawn system**: No significant TPS impact from entity spawning

## Compatibility

- **Minecraft Versions**: 1.21.1 (may update to newer versions in future)
- **Mod Compatibility**: Compatible with Fabric ecosystem mods
- **Server/Client**: Both (client-side rendering, server-side logic)

## Attribution and Licensing

**Original Mod**: Alex's Mobs by AlexModGuy
**License**: GPL-3.0-only (using same license as indicated on Modrinth)
**Attribution**: This is a Fabric port of the original Forge/NeoForge mod with full credit to the original author

**Important**: User will contact original author (AlexModGuy) for explicit permission before public distribution.

## Epic Status

No epics created yet. Run `/create_epic "Epic Name"` to add your first epic.

## Development Roadmap

**Phase 1: Research & Setup**
- Research original mod structure and mob list
- Identify all animal mobs to port
- Catalog models, textures, and assets needed
- Plan epic breakdown

**Phase 2: Core Framework**
- Set up entity registration system
- Set up item registration system
- Create base entity classes
- Implement resource loading

**Phase 3: Mob Porting** (Epic-based)
- Port mobs in logical groups (biome-based, size-based, or behavior-based)
- Each epic focuses on a specific group of related mobs

**Phase 4: Items & Crafting**
- Port all mob drops
- Port all craftable items
- Implement ethical drop mechanics

**Phase 5: Polish & Testing**
- Performance optimization
- Bug fixes
- Compatibility testing
