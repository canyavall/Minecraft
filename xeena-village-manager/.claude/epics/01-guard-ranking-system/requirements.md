# Epic 01: Guard Ranking System

**Created**: September 2025
**Status**: COMPLETED
**Priority**: Critical

## Business Value

Provide players with a meaningful progression system for their guard villagers, creating an emerald-based economy that encourages strategic investment in village defense. Players can specialize their guards into distinct combat roles (melee or ranged), making each guard feel unique and valuable.

## Overview

Implement a comprehensive guard ranking system with dual specialization paths, emerald-based progression economy, and combat stat scaling. Guards start as basic recruits and can be upgraded through multiple tiers, with players choosing between melee (Man-at-Arms path) or ranged (Marksman path) specializations.

## Features

### Feature 1: Dual Specialization Paths
**Description**: Guards can progress through two distinct upgrade paths based on combat style preference.

**User Experience**:
- Players choose between melee and ranged specializations at Tier 2
- Melee path: Recruit → Man-at-Arms → Guard → Knight → Knight Commander
- Ranged path: Recruit → Marksman → Archer → Sharpshooter → Elite Marksman
- Visual feedback shows current rank and available upgrade paths

**Success Criteria**:
- [x] All rank tiers defined with clear progression
- [x] Dual specialization paths implemented
- [x] Path selection is permanent and meaningful

### Feature 2: Emerald-Based Progression Economy
**Description**: Players purchase rank upgrades using emeralds, creating a strategic resource management system.

**User Experience**:
- Each rank costs progressively more emeralds
- Players must collect emeralds through trading, mining, or looting
- Clear emerald cost display before purchase
- Immediate validation of emerald availability
- Secure server-side transaction processing

**Success Criteria**:
- [x] Emerald economy functional with validation
- [x] Transaction security prevents cheating
- [x] Clear cost communication to players
- [x] Emerald deduction works correctly

### Feature 3: Combat Stat Scaling
**Description**: Each rank tier provides significant stat increases, making upgrades feel impactful.

**User Experience**:
- Higher ranks deal more damage
- Higher ranks have more health
- Tier 4 ranks gain special abilities
- Stats are visible in guard management UI
- Combat effectiveness is immediately noticeable

**Success Criteria**:
- [x] Combat stats scale correctly with ranks
- [x] Tier 4 special abilities working (Knight knockback/area damage, Sharpshooter double shot)
- [x] Stat differences are meaningful and balanced

### Feature 4: Persistent Data & Multiplayer Sync
**Description**: Guard ranks persist across sessions and synchronize in multiplayer environments.

**User Experience**:
- Ranks save when server restarts
- Other players see correct rank information
- No rank loss or corruption
- Smooth multiplayer experience

**Success Criteria**:
- [x] Data persistence and network synchronization
- [x] No data loss on restart
- [x] Multiplayer sync works correctly

## Game Mechanics

### Progression System
**Rank Tiers**: 5 tiers (0-4) per specialization path
**Emerald Costs**: Escalating costs (15 → 30 → 50 → 75 emeralds approximate)
**Specialization Point**: Tier 2 (player chooses melee or ranged)

### Economy/Balance
- Emerald investment creates meaningful choices
- Higher ranks are expensive enough to be prestigious
- Progression feels rewarding without being grindy
- Early tiers accessible, late tiers aspirational

### Combat System Integration
- Melee guards excel at close combat, area control
- Ranged guards excel at distance, precision damage
- Tier 4 abilities are powerful but balanced
- Each specialization has distinct tactical advantages

## Technical Considerations

- Must integrate with existing villager data system
- Requires client-server packet synchronization
- GUI needs to display rank selection interface
- Server-side validation critical for economy security

## Dependencies

- Base guard system (villagers can become guards)
- Emerald item detection and counting
- Villager data persistence system

## Out of Scope

- Automatic rank progression (must be purchased by player)
- Rank downgrading or respeccing
- Custom rank names or player-created paths
- Experience-based ranking (emerald-only)

## Acceptance Criteria (Epic-Level)

- [x] All rank tiers defined with appropriate stats
- [x] Dual specialization paths implemented
- [x] Emerald economy functional with validation
- [x] Combat stats scale correctly with ranks
- [x] Tier 4 special abilities working
- [x] Data persistence and network synchronization
- [x] GUI integration complete
- [x] Players report satisfying progression experience

## Epic Completion

**Completed**: September 28, 2025
**Outcome**: Fully functional guard ranking system with both specialization paths, balanced emerald economy, and smooth multiplayer integration.
