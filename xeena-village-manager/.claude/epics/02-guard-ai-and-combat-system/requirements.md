# Epic 02: Guard AI and Combat System

**Created**: September 2025
**Status**: COMPLETED
**Priority**: Critical

## Business Value

Transform guards from static entities into intelligent, combat-ready protectors that actively defend villages. Players experience a living defense system where guards patrol, detect threats, coordinate attacks, and adapt their tactics based on their specialization. This creates an engaging gameplay loop where players feel their investment in guards pays off through visible, effective protection.

## Overview

Implement comprehensive guard AI with combat behaviors, pathfinding, threat detection, and group coordination. Guards will intelligently attack hostile mobs, defend villagers, patrol areas, and work together as a coordinated defense force. The system must be performant enough to support multiple guards without impacting gameplay.

## Features

### Feature 1: Intelligent Combat Behaviors
**Description**: Guards detect and engage hostile threats using combat tactics appropriate to their specialization.

**User Experience**:
- Melee guards charge into combat, using knockback and area damage
- Ranged guards maintain distance, using kiting tactics and precision shots
- Guards prioritize threats based on danger level
- Tier 4 guards use special abilities (Knight knockback/area damage, Sharpshooter double shot)
- Combat feels natural and strategic

**Success Criteria**:
- [x] Guards attack hostile mobs automatically
- [x] Melee and ranged specializations use distinct combat tactics
- [x] Threat detection system identifies dangerous targets
- [x] Tier 4 special abilities activate in combat
- [x] Combat is responsive and fluid

### Feature 2: Patrol and Movement Modes
**Description**: Players can configure guard behavior with three distinct modes to suit different defensive strategies.

**User Experience**:
- **STAND mode**: Guard stays at post, defends immediate area
- **FOLLOW mode**: Guard follows owner/villager, mobile protection
- **PATROL mode**: Guard patrols designated area, active defense
- Mode switching via GUI
- Guards respect mode boundaries and behavior constraints
- Smooth transitions between modes

**Success Criteria**:
- [x] All three modes implemented and functional
- [x] Patrol areas configurable by players
- [x] Guards follow pathfinding correctly
- [x] Mode persistence across server restarts
- [x] Intuitive mode selection in UI

### Feature 3: Village Defense System
**Description**: Guards actively defend villagers and village structures from hostile threats.

**User Experience**:
- Guards automatically detect mobs threatening villagers
- Multiple guards coordinate to defend against raids
- Guards position strategically around threatened areas
- Villagers are visibly protected by guard presence
- Players feel village is safe with guard coverage

**Success Criteria**:
- [x] Guards defend villagers from hostile mobs
- [x] Threat detection covers village area
- [x] Multiple guards coordinate in combat
- [x] Guards effectively repel raids and invasions

### Feature 4: Group Coordination and Tactics
**Description**: Multiple guards work together as a coordinated defense force rather than acting independently.

**User Experience**:
- Guards share threat information
- Guards avoid all attacking the same target inefficiently
- Formation behavior when patrolling together
- Coordinated response to large threats
- Feels like a tactical defense unit

**Success Criteria**:
- [x] Guards coordinate target allocation
- [x] Formation behavior implemented
- [x] Group tactics feel intelligent
- [x] No wasted effort from duplicate targeting

### Feature 5: Performance-Optimized AI
**Description**: AI system runs efficiently even with many guards active simultaneously.

**User Experience**:
- Game maintains 60 FPS with 10+ guards active
- No lag spikes during combat
- Smooth guard movement and actions
- Minimal performance impact on multiplayer servers

**Success Criteria**:
- [x] Target: <3% FPS impact with 10 guards
- [x] Optimized pathfinding and threat detection
- [x] Efficient tick-based AI updates
- [x] Performance benchmarks meet targets

### Feature 6: Visual and Audio Feedback
**Description**: Combat actions have appropriate visual and audio effects to enhance immersion.

**User Experience**:
- Combat sounds when guards attack
- Visual effects for special abilities
- Guard animations feel combat-appropriate
- Players can "read" combat state from feedback

**Success Criteria**:
- [x] Combat sound effects implemented
- [x] Visual effects for Tier 4 abilities
- [x] Feedback is clear and informative

## Game Mechanics

### Combat System
**Threat Detection**: Priority-based scanning (high threat enemies prioritized)
**Combat Range**: Melee (3 blocks), Ranged (15-20 blocks)
**Special Abilities**: Tier 4 only (Knight area damage/knockback, Sharpshooter double shot)

### AI Behaviors
**Attack Goal**: Primary combat behavior, target hostile mobs
**Defend Goal**: Protect villagers from threats
**Patrol Goal**: Area coverage and proactive defense
**Follow Goal**: Mobile protection for owner/villager
**Retreat Goal**: Tactical withdrawal when low health

### Group Tactics
- Target allocation prevents overkill
- Formation behavior during patrols
- Coordinated response to high-priority threats
- Load-balanced threat detection

### Performance Targets
- FPS Impact: <3% with 10 guards
- Tick Performance: <0.5ms per guard per tick
- Detection Range: Configurable (default 16-32 blocks)

## Technical Considerations

- AI goals must integrate with Minecraft's goal system
- Pathfinding must handle complex terrain
- Threat detection needs performance optimization
- Network sync for multiplayer combat
- Config file for behavior tuning

## Dependencies

- Epic 01: Guard Ranking System (stats and abilities)
- Base villager guard conversion
- Combat stat system

## Out of Scope

- PvP guard combat (guards don't attack players)
- Complex formation tactics (basic formations only)
- Siege equipment or advanced weaponry
- Dynamic difficulty scaling

## Acceptance Criteria (Epic-Level)

- [x] All core AI goals implemented (attack, defend, patrol, follow)
- [x] Threat detection system functional
- [x] Melee and ranged combat specializations working
- [x] Pathfinding handles terrain correctly
- [x] Rank integration affects combat effectiveness
- [x] Group coordination implemented
- [x] Three behavior modes configurable
- [x] Visual and audio feedback present
- [x] Performance targets met (<3% FPS impact)
- [x] Guards effectively defend villages in practice

## Epic Completion

**Completed**: October 5, 2025
**Outcome**: Fully functional guard AI system with intelligent combat behaviors, coordinated group tactics, and excellent performance. Guards successfully defend villages and create engaging gameplay.
