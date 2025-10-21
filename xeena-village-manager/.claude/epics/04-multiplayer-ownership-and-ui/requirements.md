# Epic 04: Multiplayer Ownership & Advanced UI

**Status**: 🔄 PLANNING
**Priority**: High
**Start Date**: October 19, 2025
**Target Completion**: TBD

---

## Epic Overview

Enhance multiplayer experience with player-based villager ownership system and modernize the UI for a professional, polished look. This epic focuses on protecting player investments in villagers and providing a superior user experience through modern interface design.

---

## Business Goals

### Primary Objectives
1. **Multiplayer Protection**: Prevent griefing by allowing players to claim and protect their villagers
2. **Ownership Transfer**: Enable safe transfer of valuable villagers between trusted players
3. **Modern UI**: Provide a professional, aesthetically pleasing interface that rivals modern mods
4. **Configuration Cleanup**: Remove unused/redundant settings to simplify user configuration

### Success Metrics
- Players can claim villagers and prevent unauthorized changes
- Ownership transfer works seamlessly between players
- Guards protect their bound owner from hostile players
- UI feels modern and intuitive compared to vanilla Minecraft
- Configuration file is clean and well-documented

---

## Phase Breakdown

### Phase 1: Testing & Configuration Cleanup (1-2 days)
**Goal**: Validate existing functionality and clean up technical debt

**Tasks**:
1. **P4-TASK-001**: Test all guard modes (STAND, FOLLOW, PATROL) - verify AI behavior
2. **P4-TASK-002**: Test patrol radius system - validate range detection and pathfinding
3. **P4-TASK-003**: Config file audit - identify unused settings, document active ones

**Dependencies**: None (can start immediately)

**Deliverables**:
- Test report for guard modes with any bugs identified
- Test report for patrol radius with validation results
- Clean, documented config file with unused settings removed

---

### Phase 2: Player Binding/Ownership System (1-2 weeks)
**Goal**: Implement comprehensive villager ownership and permission system

**Core Features**:

#### 2.1 Basic Ownership
- Players can "bind" or "claim" a villager
- Only bound player can:
  - Change villager profession
  - Manage guard settings (rank, mode, equipment)
  - Lock/unlock the villager
- Ownership persists through server restarts
- Visual indicator showing who owns a villager

#### 2.2 Permission System
- **Manage Permission**: Who can change settings (owner only)
- **Trade Permission**: Who can trade with the villager
  - Options: Everyone, Owner Only, Whitelist
- **Lock State**: Prevents all interactions except owner

#### 2.3 Ownership Transfer
- Owner can transfer villager to another player
- Confirmation dialog to prevent accidents
- All permissions reset on transfer
- Transfer history logged (optional)

#### 2.4 Guard Protection
- Guards attack players who attack their bound owner
- Only applies to PvP-enabled servers
- Configurable: Can be disabled per-server
- Does not attack owner's party/team members (if applicable)

**Technical Requirements**:
- New data structure: `VillagerOwnership`
  - Owner UUID
  - Permission flags (canTrade, canManage)
  - Lock state
  - Bind timestamp
- Server-side validation for all ownership checks
- Client-server synchronization for ownership display
- Integration with existing guard AI for protection behavior

**UI Requirements**:
- Ownership indicator in management screen header
- Permission toggle buttons
- Transfer ownership button with confirmation
- Lock/unlock button
- Visual feedback when attempting unauthorized actions

**Acceptance Criteria**:
- [x] Player can bind a villager from management screen
- [x] Non-owner cannot change profession or settings
- [x] Owner can control trade permissions
- [x] Owner can transfer villager to another player
- [x] Guard attacks players who damage the bound owner
- [x] All ownership data persists through restarts
- [x] Clear error messages when unauthorized actions attempted

---

### Phase 3: UI Modernization (2-3 weeks)
**Goal**: Redesign all GUI screens with modern aesthetics and improved UX

**Approach Options**:

#### Option A: Integrate Modern UI Library
**Library**: [Modern UI for Minecraft](https://github.com/BloCamLimb/ModernUI)
- Pros: Pre-built modern components, maintained library, professional look
- Cons: Learning curve, dependency management, potential compatibility issues

#### Option B: Custom Modern Design
- Pros: Full control, no external dependencies, tailored to our needs
- Cons: More development time, need to maintain custom code

**Features to Implement** (regardless of approach):

#### 3.1 Visual Design
- Smooth animations and transitions
- Modern color palette (dark theme option)
- Gradient backgrounds and borders
- Icon-based navigation
- Tooltip improvements with rich formatting

#### 3.2 Layout Improvements
- Responsive design for different screen sizes
- Better spacing and visual hierarchy
- Collapsible sections for advanced options
- Search/filter for profession selection
- Tabbed interface with smooth transitions

#### 3.3 UX Enhancements
- Keyboard shortcuts
- Drag-and-drop for some operations
- Context menus (right-click actions)
- Progress indicators for async operations
- Toast notifications for important events

#### 3.4 Accessibility
- High contrast mode
- Scalable UI elements
- Screen reader support (if possible)
- Colorblind-friendly indicators

**Technical Requirements**:
- Maintain existing functionality while upgrading visuals
- Performance must not degrade (target: <5ms render time)
- All screens must adapt to different resolutions
- Backward compatibility with existing saved data

**Acceptance Criteria**:
- [x] All management screens redesigned with modern look
- [x] Smooth animations don't impact performance
- [x] UI scales properly on 1080p, 1440p, and 4K displays
- [x] Dark theme option available
- [x] User testing feedback is positive (>80% approval)

---

## Technical Architecture

### Ownership System Data Model

```java
public class VillagerOwnership {
    private UUID ownerUUID;              // Player who owns this villager
    private String ownerName;            // Cached player name for display
    private long bindTimestamp;          // When villager was claimed
    private OwnershipPermissions permissions;
    private boolean locked;              // Completely locked state

    public enum TradePermission {
        EVERYONE,      // Anyone can trade
        OWNER_ONLY,    // Only owner can trade
        WHITELIST      // Specific players can trade
    }
}

public class OwnershipPermissions {
    private TradePermission tradePermission = TradePermission.EVERYONE;
    private Set<UUID> tradeWhitelist = new HashSet<>();
    private boolean allowManagement = false;  // For delegation
}
```

### Ownership Manager

```java
public class VillagerOwnershipManager {
    // Singleton pattern
    private static final Map<UUID, VillagerOwnership> ownerships = new ConcurrentHashMap<>();

    // Core operations
    public static void bindVillager(UUID villagerUUID, ServerPlayerEntity owner);
    public static void unbindVillager(UUID villagerUUID);
    public static void transferOwnership(UUID villagerUUID, UUID newOwnerUUID);
    public static boolean canManage(UUID villagerUUID, ServerPlayerEntity player);
    public static boolean canTrade(UUID villagerUUID, ServerPlayerEntity player);

    // Persistence
    public static void save(ServerWorld world);
    public static void load(ServerWorld world);
}
```

### New Network Packets

```java
- BindVillagerPacket (C2S)
- TransferOwnershipPacket (C2S)
- UpdatePermissionsPacket (C2S)
- OwnershipSyncPacket (S2C)
- OwnershipDeniedPacket (S2C) // Error message when action denied
```

---

## Dependencies & Risks

### Dependencies
- **Phase 1**: None
- **Phase 2**: Requires Phase 1 testing complete
- **Phase 3**: Can run in parallel with Phase 2

### Technical Risks
1. **Multiplayer Sync Complexity**: Ownership needs to sync across all clients
   - **Mitigation**: Use existing packet infrastructure, comprehensive testing

2. **Performance Impact**: Additional checks for every villager interaction
   - **Mitigation**: Caching, efficient data structures, profiling

3. **Modern UI Library Compatibility**: May conflict with other mods
   - **Mitigation**: Have custom UI as fallback, thorough compatibility testing

### Design Risks
1. **Ownership UX**: Players might find claiming confusing
   - **Mitigation**: Clear tutorial, intuitive UI, good documentation

2. **Permission Complexity**: Too many options overwhelm users
   - **Mitigation**: Sensible defaults, progressive disclosure of advanced options

---

## Testing Strategy

### Phase 1 Testing
- Manual testing of guard modes in-game
- Automated tests for patrol radius calculations
- Config file validation

### Phase 2 Testing
- Unit tests for ownership logic
- Multiplayer testing with 2+ players
- Edge cases: ownership conflicts, server crashes, data corruption
- Performance testing with 100+ owned villagers

### Phase 3 Testing
- UI rendering performance tests
- Screen resolution compatibility tests
- User acceptance testing (gather feedback)
- Accessibility testing

---

## Documentation Requirements

### For Players
- Ownership system guide
  - How to claim villagers
  - How to set permissions
  - How to transfer ownership
- Modern UI user guide (if using library)
- Updated configuration guide

### For Developers
- Ownership system architecture documentation
- API documentation for ownership checks
- UI component library documentation
- Migration guide for custom UI (if applicable)

---

## Future Enhancements (Post-Epic 04)

Ideas to consider for future epics:
- **Team/Guild System**: Share villagers across a group
- **Villager Trading Post**: Marketplace for selling/buying villagers
- **Ownership History**: Track all previous owners
- **Remote Management**: Manage villagers from a distance
- **Statistics Dashboard**: Track villager productivity

---

## Success Criteria

### Phase 1
- [x] All guard modes tested and validated
- [x] Patrol radius system verified working
- [x] Config file cleaned and documented

### Phase 2
- [x] Ownership system fully functional in multiplayer
- [x] Players can protect their villagers from griefing
- [x] Guards defend bound owners in PvP
- [x] Zero data loss in ownership transfers
- [x] < 1ms performance overhead per ownership check

### Phase 3
- [x] Modern UI implemented on all screens
- [x] UI performance meets targets (<5ms render)
- [x] Positive user feedback (>80% approval)
- [x] No regressions in existing functionality

---

**Epic Owner**: minecraft-developer
**Reviewers**: project-scope-manager, minecraft-qa-specialist
**Last Updated**: October 19, 2025
