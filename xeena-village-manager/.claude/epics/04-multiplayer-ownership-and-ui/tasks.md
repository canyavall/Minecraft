# Epic 04: Multiplayer Ownership & Advanced UI - Tasks

**Epic Status**: 🔄 PLANNING
**Start Date**: October 19, 2025
**Current Phase**: Phase 1 - Testing & Configuration Cleanup

---

## Phase 1: Testing & Configuration Cleanup

### P4-TASK-001: Test All Guard Modes
**Status**: ✅ COMPLETED
**Priority**: High
**Assignee**: minecraft-qa-specialist
**Estimated Effort**: 2-3 hours
**Actual Time**: ~1 hour
**Dependencies**: None
**Test Plan**: `.claude/epics/04-multiplayer-ownership-and-ui/P4-TASK-001-Guard-Modes-Test-Plan.md`
**Completion Date**: October 19, 2025

**Goal**: Thoroughly test all guard modes (STAND, FOLLOW, PATROL) to ensure AI behavior works correctly after recent changes.

**Test Results**: ✅ ALL PASSED
- ✅ STAND mode: Guards stay in place correctly
- ✅ FOLLOW mode: Guards follow player/villagers correctly
- ✅ PATROL mode: Guards patrol around Guard Post correctly
- ✅ Combat interruption: All modes properly break for combat
- ✅ Combat resumption: All modes properly resume after combat ends

**Test Scenarios**:

1. **STAND Mode**:
   - Guard stays at designated position
   - Guard rotates to face threats
   - Guard does NOT move when switching targets
   - Guard resumes STAND position after combat
   - Position persists through server restart

2. **FOLLOW Mode**:
   - Guard follows designated player
   - Guard stops at appropriate distance
   - Guard resumes following after combat
   - Guard handles player teleportation gracefully
   - Fallback to nearest player works if target unavailable

3. **PATROL Mode**:
   - Guard patrols within configured radius
   - Guard uses intelligent pathfinding (no getting stuck)
   - Guard engages threats during patrol
   - Guard resumes patrol after combat
   - Patrol center point persists through restart

**Combat Integration**:
- All modes properly interrupt for combat
- All modes properly resume after combat
- Mode switching works instantly
- No AI goal conflicts

**Acceptance Criteria**:
- [x] All 3 modes tested in single-player
- [x] All 3 modes tested in multiplayer (if applicable)
- [x] Mode switching tested (all combinations)
- [x] Combat interruption/resumption tested
- [x] Server restart persistence verified
- [x] Bug report created for any issues found (NONE FOUND)
- [x] Test report: All tests passed successfully

---

### P4-TASK-002: Test Patrol Radius System
**Status**: ✅ COMPLETED
**Priority**: High
**Assignee**: minecraft-qa-specialist
**Estimated Effort**: 2 hours
**Actual Time**: ~30 minutes
**Dependencies**: None
**Completion Date**: October 19, 2025

**Test Results**: ✅ ALL PASSED
- ✅ Patrol radius boundaries working correctly
- ✅ Guards stay within configured radius
- ✅ Pathfinding works in various terrains
- ✅ No performance issues detected

**Goal**: Validate that patrol radius detection and pathfinding work correctly at various distances.

**Test Cases**:

1. **Radius Validation**:
   - Test radius: 5, 10, 15, 20, 30, 50 blocks
   - Verify guard stays within configured radius
   - Verify guard doesn't exceed radius boundaries
   - Test radius update (change via GUI while active)

2. **Pathfinding**:
   - Test in flat terrain
   - Test in hilly/mountainous terrain
   - Test with obstacles (walls, water, lava)
   - Test multi-level areas (underground, buildings)
   - Verify guard doesn't get stuck

3. **Performance**:
   - Test with 1, 5, 10, 20 guards patrolling simultaneously
   - Monitor TPS (should stay >19.5)
   - Check for memory leaks during extended patrol
   - Verify patrol paths are efficient (not erratic)

4. **Edge Cases**:
   - Patrol center in unloaded chunks
   - Patrol radius crossing chunk boundaries
   - Guard spawned outside patrol radius
   - Patrol center moved while guard active

**Acceptance Criteria**:
- [x] All radius sizes tested and validated
- [x] Pathfinding works in various terrains
- [x] No performance degradation with multiple guards
- [x] Edge cases handled gracefully
- [x] Test report: All tests passed successfully
- [x] Performance metrics: No issues detected

---

### P4-TASK-003: Config File Audit & Cleanup
**Status**: ✅ COMPLETED
**Priority**: Medium
**Assignee**: minecraft-developer
**Estimated Effort**: 1-2 hours
**Actual Time**: ~1 hour
**Dependencies**: None
**Completion Date**: October 19, 2025

**Work Completed**:
- ✅ Audited all configuration settings for usage
- ✅ Removed 9 unused settings (guard_settings and all sub-settings)
- ✅ Kept only 2 active settings (blacklisted_professions, villager_display_mode)
- ✅ Added comprehensive JavaDoc documentation
- ✅ Created detailed Configuration Guide
- ✅ All 372 tests still passing after cleanup

**Goal**: Review configuration file, remove unused settings, improve documentation, and ensure sensible defaults.

**Audit Checklist**:

1. **Identify Active Settings**:
   - List all config options currently used in code
   - Mark which are referenced vs. never used
   - Document purpose of each setting

2. **Remove Unused Settings**:
   - Identify deprecated/unused config options
   - Remove from config class and JSON
   - Update config version if needed

3. **Improve Documentation**:
   - Add clear comments for each setting
   - Include valid value ranges
   - Provide example use cases
   - Note which settings require restart

4. **Validate Defaults**:
   - Ensure defaults match documentation
   - Test default config from scratch
   - Verify backwards compatibility with old configs

**Current Config Options to Review**:
```
- villager_display_mode (SHOW_ALL, GUARDS_ONLY, NONE)
- guard_detection_range (default: 20.0)
- patrol_radius (per-guard, default: 15)
- guard_modes (STAND, FOLLOW, PATROL)
- rank_costs (emerald prices per tier)
- profession_blacklist (blocked professions)
- [Any other settings to identify]
```

**Deliverables**:
- Clean config file with only active settings
- Comprehensive config documentation
- Migration guide if breaking changes
- Default config template

**Acceptance Criteria**:
- [x] All unused settings removed (9 settings removed)
- [x] Every setting has clear documentation (comprehensive JavaDoc added)
- [x] Defaults tested and validated (compilation + 372 tests passed)
- [x] Config version updated if needed (automatic migration)
- [x] Migration tested (old → new config compatible)
- [x] Configuration guide document created (Configuration-Guide.md)

---

### P4-TASK-003.1: Analyze and Compare Ranged Guard Stats
**Status**: ✅ COMPLETED
**Priority**: Medium
**Assignee**: minecraft-researcher + minecraft-developer
**Estimated Effort**: 1-2 hours
**Actual Time**: ~1.5 hours
**Dependencies**: None
**Start Date**: October 19, 2025
**Completion Date**: October 19, 2025
**Analysis Report**: `.claude/epics/04-multiplayer-ownership-and-ui/P4-TASK-003.1-Ranged-Guard-Stats-Analysis.md`

**Goal**: Research and compare ranged guard (Sharpshooter) combat statistics with vanilla Minecraft bow-using mobs (Skeleton, Pillager) to ensure guards are balanced and competitive.

**Analysis Completed**: ✅ ALL RESEARCH AREAS COVERED
- ✅ Attack range analyzed and compared
- ✅ Damage output breakdown by tier
- ✅ Attack speed/fire rate comparison
- ✅ Accuracy comparison
- ✅ Special abilities documented
- ✅ Tactical AI behavior evaluated
- ✅ Balance assessment and recommendations provided

**Key Findings**:
1. **Attack Range**: Guards match Skeleton (16 blocks), exceed Pillager (8 blocks)
2. **Damage**: Lower base (2.5 vs 3-4 vanilla), compensated by special abilities at higher tiers
3. **Attack Speed**: Tier 4 guards faster than both vanilla mobs (0.9s vs 1-3s)
4. **Accuracy**: Tier 4 matches Skeleton Normal difficulty (accuracy 6)
5. **Special Abilities**: Guards have significant advantage (Double Shot, Multishot, Precision Shot, Slowness arrows)
6. **Tactical AI**: Far superior positioning, kiting, and high-ground seeking

**Balance Assessment**:
✅ **Intentional progression curve** - weak early, strong late
✅ **Recommended**: Keep current balance (fits RPG investment model)
⚠️ **Optional**: Consider tier-based damage scaling if early guards feel too weak

**Research Areas**:
1. **Attack Range**:
   - When guards start aiming/shooting
   - Comparison with Skeleton and Pillager ranges

2. **Damage Output**:
   - Guard arrow damage by tier
   - Skeleton arrow damage
   - Pillager crossbow damage

3. **Attack Speed/Cooldown**:
   - Time between shots for guards
   - Vanilla mob attack intervals

4. **Other Mechanics**:
   - Accuracy/spread
   - Movement while shooting
   - Special abilities (Tier 4 Sharpshooter double shot)

**Deliverables**:
- Comparison table of guard vs vanilla mob stats
- Recommendations for balance adjustments (if needed)
- Test results from in-game observations

**Acceptance Criteria**:
- [ ] Guard ranged attack range documented
- [ ] Skeleton and Pillager stats researched
- [ ] Damage comparison table created
- [ ] Attack speed comparison documented
- [ ] Recommendations provided (if imbalanced)
- [ ] In-game testing completed

---

## Phase 2: Player Binding/Ownership System

### P4-TASK-004: Design Ownership Data Model
**Status**: 📋 TODO
**Priority**: Critical
**Assignee**: minecraft-developer
**Estimated Effort**: 3-4 hours
**Dependencies**: None

**Goal**: Design and implement data structures for villager ownership system.

**Components to Design**:

1. **VillagerOwnership Class**:
```java
public class VillagerOwnership {
    private UUID villagerUUID;
    private UUID ownerUUID;
    private String ownerName;  // Cached for display
    private long bindTimestamp;
    private OwnershipPermissions permissions;
    private boolean locked;

    // Serialization methods
    public CompoundTag toNbt();
    public static VillagerOwnership fromNbt(CompoundTag nbt);
}
```

2. **OwnershipPermissions Class**:
```java
public class OwnershipPermissions {
    private TradePermission tradePermission;
    private Set<UUID> tradeWhitelist;
    private boolean allowOthersToManage;  // Future: delegation

    public enum TradePermission {
        EVERYONE,
        OWNER_ONLY,
        WHITELIST
    }
}
```

3. **VillagerOwnershipManager Class**:
```java
public class VillagerOwnershipManager {
    private static final Map<UUID, VillagerOwnership> ownerships;

    // Core operations
    public static void bindVillager(UUID villagerUUID, ServerPlayerEntity owner);
    public static void unbindVillager(UUID villagerUUID);
    public static void transferOwnership(UUID villagerUUID, UUID newOwner);
    public static boolean canManage(UUID villagerUUID, ServerPlayerEntity player);
    public static boolean canTrade(UUID villagerUUID, ServerPlayerEntity player);
    public static VillagerOwnership getOwnership(UUID villagerUUID);

    // Persistence
    public static void save(ServerWorld world);
    public static void load(ServerWorld world);
}
```

**Persistence Format**: NBT file in world save directory
```
world/data/xeenaa_villager_manager/ownership.dat
```

**Acceptance Criteria**:
- [ ] All classes implemented with full JavaDoc
- [ ] NBT serialization/deserialization working
- [ ] Thread-safe concurrent access
- [ ] Unit tests for all methods
- [ ] Persistence save/load tested
- [ ] No data loss on server restart

---

### P4-TASK-005: Implement Ownership Binding & Unbinding
**Status**: 📋 TODO
**Priority**: Critical
**Assignee**: minecraft-developer
**Estimated Effort**: 4-5 hours
**Dependencies**: P4-TASK-004

**Goal**: Implement ability for players to claim (bind) and release (unbind) villagers.

**Implementation Requirements**:

1. **Bind Villager**:
   - Add "Claim Villager" button to management GUI
   - Validation: Villager must not already be owned
   - Store ownership in VillagerOwnershipManager
   - Sync to all clients
   - Visual feedback (success message, owner badge)

2. **Unbind Villager**:
   - Add "Release Ownership" button (only visible to owner)
   - Confirmation dialog (prevent accidents)
   - Remove from VillagerOwnershipManager
   - Sync to all clients
   - Visual feedback

3. **Network Packets**:
```java
// Client → Server
public record BindVillagerPacket(int villagerEntityId) implements CustomPayload {}
public record UnbindVillagerPacket(int villagerEntityId) implements CustomPayload {}

// Server → Client
public record OwnershipSyncPacket(
    UUID villagerUUID,
    UUID ownerUUID,
    String ownerName,
    CompoundTag permissionsNbt
) implements CustomPayload {}

public record OwnershipDeniedPacket(
    String reason  // "already_owned", "not_owner", etc.
) implements CustomPayload {}
```

4. **GUI Changes**:
   - Owner badge in header (shows player head + name)
   - "Claim" button (only when unowned)
   - "Release" button (only when owner)
   - Visual state: Owned vs Unowned
   - Tooltip explaining ownership

**Security**:
- Server-side validation of all bind/unbind requests
- Distance check (player must be near villager)
- Prevent duplicate ownership
- Rate limiting (prevent spam)

**Acceptance Criteria**:
- [ ] Player can claim unowned villager
- [ ] Player can release owned villager
- [ ] Only owner sees "Release" button
- [ ] Ownership persists through restart
- [ ] Multiplayer sync works correctly
- [ ] Clear error messages for invalid actions
- [ ] Unit tests for bind/unbind logic
- [ ] Integration tests for multiplayer

---

### P4-TASK-006: Implement Permission System
**Status**: 📋 TODO
**Priority**: High
**Assignee**: minecraft-developer
**Estimated Effort**: 5-6 hours
**Dependencies**: P4-TASK-005

**Goal**: Implement trade permissions and management restrictions.

**Features**:

1. **Trade Permission Control**:
   - Toggle: Everyone / Owner Only / Whitelist
   - Whitelist UI: Add/remove players by name
   - Server-side enforcement in trading handler
   - Clear message when trade denied

2. **Management Restrictions**:
   - Check ownership before profession change
   - Check ownership before rank purchase
   - Check ownership before mode change
   - Check ownership before equipment changes
   - Deny with clear error message

3. **Lock System**:
   - Lock toggle button (owner only)
   - Locked state: NO interactions except owner
   - Visual indicator (padlock icon)
   - Prevents:
     - Trading (all players except owner)
     - GUI opening (non-owners)
     - Any modifications

4. **GUI Updates**:
   - Permissions panel in management screen
   - Trade permission dropdown
   - Whitelist player entry field
   - Lock/Unlock button with icon
   - Permission tooltips explaining each option

5. **Network Packets**:
```java
public record UpdatePermissionsPacket(
    UUID villagerUUID,
    TradePermission tradePermission,
    Set<UUID> tradeWhitelist,
    boolean locked
) implements CustomPayload {}
```

**Server-Side Hooks**:
- Intercept profession change (ServerPacketHandler)
- Intercept rank purchase (ServerPacketHandler)
- Intercept trading (villager trade event)
- Intercept GUI open (interaction handler)

**Acceptance Criteria**:
- [ ] Trade permissions work correctly
- [ ] Only owner can change profession/settings
- [ ] Lock prevents all non-owner interactions
- [ ] Whitelist system functions properly
- [ ] Server-side validation prevents bypasses
- [ ] Clear error messages shown
- [ ] Permissions persist through restart
- [ ] Multiplayer testing complete

---

### P4-TASK-007: Implement Ownership Transfer
**Status**: 📋 TODO
**Priority**: Medium
**Assignee**: minecraft-developer
**Estimated Effort**: 3-4 hours
**Dependencies**: P4-TASK-006

**Goal**: Allow owners to transfer villager ownership to another player.

**Implementation**:

1. **Transfer UI**:
   - "Transfer Ownership" button in management GUI
   - Player selection dropdown (online players)
   - Or text input for player name/UUID
   - Confirmation dialog with details:
     - Current owner
     - New owner
     - Villager info (profession, rank)
     - "Are you sure?" confirmation

2. **Transfer Logic**:
   - Validate: current player is owner
   - Validate: target player exists and is online
   - Update VillagerOwnershipManager
   - Reset permissions to defaults (optional)
   - Sync to all clients
   - Notify both players

3. **Network Packet**:
```java
public record TransferOwnershipPacket(
    UUID villagerUUID,
    UUID newOwnerUUID
) implements CustomPayload {}

public record OwnershipTransferNotification(
    UUID villagerUUID,
    String villagerName,
    String previousOwner,
    String newOwner
) implements CustomPayload {}
```

4. **Notifications**:
   - Previous owner: "You transferred [Villager] to [Player]"
   - New owner: "[Player] transferred [Villager] to you"
   - Toast/chat notification with clickable link

**Edge Cases**:
- Target player offline (error message)
- Transfer to self (prevent or allow?)
- Transfer during combat (prevent)
- Target player inventory full (not applicable)

**Acceptance Criteria**:
- [ ] Owner can transfer to online players
- [ ] Both players receive notifications
- [ ] Ownership updates immediately
- [ ] Permissions reset on transfer (or preserved)
- [ ] Transfer history logged (optional)
- [ ] Can't transfer to offline players
- [ ] Confirmation prevents accidents
- [ ] Multiplayer testing complete

---

### P4-TASK-008: Implement Guard Protection for Bound Owner
**Status**: 📋 TODO
**Priority**: Medium
**Assignee**: minecraft-developer
**Estimated Effort**: 4-5 hours
**Dependencies**: P4-TASK-005

**Goal**: Guards attack players who damage their bound owner (PvP protection).

**Implementation**:

1. **Damage Detection**:
   - Hook into LivingEntityDamageEvent
   - Check if victim is a player
   - Check if attacker is a player
   - Find all guards owned by victim
   - Command guards to attack the attacker

2. **Guard AI Integration**:
   - Add "Protect Owner" goal (priority 0.5, between defend villager and direct attack)
   - Target: Players who damaged owner
   - Aggro duration: 30 seconds (configurable)
   - Range: Detection range (default 20 blocks)

3. **Configuration**:
```java
- enable_guard_owner_protection: boolean (default: true)
- guard_owner_protection_range: double (default: 20.0)
- guard_owner_protection_duration: int (default: 600 ticks / 30 seconds)
```

4. **Team/Party Integration** (Future-proof):
   - Don't attack party members
   - Don't attack allies
   - Placeholder for future team system

5. **Visual Feedback**:
   - Guard displays angry particle effect
   - Owner receives notification: "Your guards are protecting you!"
   - Attacker receives warning: "[Owner]'s guards are attacking you!"

**Balance Considerations**:
- Should only work on PvP servers
- Configurable per-server
- Don't make it impossible to PvP
- Consider cooldowns to prevent abuse

**Security & Fair Play**:
- Prevent exploits (guard swarms)
- Rate limiting on protection activation
- Maximum guards that can defend at once (e.g., 5)

**Acceptance Criteria**:
- [ ] Guards attack players who damage owner
- [ ] Protection range configurable
- [ ] Duration configurable and works correctly
- [ ] Can be disabled via config
- [ ] Visual/audio feedback working
- [ ] Balanced and not exploitable
- [ ] PvP testing completed
- [ ] No friendly fire issues

---

## Phase 3: UI Modernization

### P4-TASK-009: Research & Decide on UI Approach
**Status**: 📋 TODO
**Priority**: High
**Assignee**: minecraft-developer
**Estimated Effort**: 4-6 hours
**Dependencies**: None

**Goal**: Research Modern UI libraries and decide on implementation approach.

**Research Tasks**:

1. **Modern UI Library Evaluation**:
   - Library: [Modern UI for Minecraft](https://github.com/BloCamLimb/ModernUI)
   - Download and test in development environment
   - Review documentation and examples
   - Test compatibility with our codebase
   - Assess performance impact
   - Check licensing (GPL-3.0)

2. **Alternative Libraries**:
   - Architectury (UI abstractions)
   - Custom widgets based on vanilla
   - Other modern UI mods

3. **Custom Implementation Evaluation**:
   - Estimate development time
   - Required components: buttons, panels, dropdowns, tooltips
   - Animation framework needed
   - Styling system (colors, fonts, spacing)

**Decision Criteria**:
- **Compatibility**: Works with Fabric 1.21.1
- **Performance**: No FPS drops
- **Maintainability**: Easy to update and extend
- **Aesthetics**: Looks modern and professional
- **Learning Curve**: Reasonable time to learn
- **Dependencies**: Minimal external deps preferred

**Deliverables**:
- Research document comparing options
- Proof-of-concept with chosen approach
- Decision rationale
- Implementation plan

**Acceptance Criteria**:
- [ ] Modern UI library tested in dev environment
- [ ] Performance benchmarked
- [ ] Compatibility verified
- [ ] Decision made: Library vs Custom
- [ ] Research document created
- [ ] POC demonstrates feasibility

---

### P4-TASK-010: Design Modern UI Mockups
**Status**: 📋 TODO
**Priority**: High
**Assignee**: minecraft-ui-ux-designer (or minecraft-developer)
**Estimated Effort**: 6-8 hours
**Dependencies**: P4-TASK-009

**Goal**: Create visual mockups for all management screens with modern design.

**Screens to Design**:

1. **Unified Guard Management Screen**:
   - Header with ownership info
   - Left panel: Rank progression (visual tree)
   - Right panel: Behavior settings, permissions
   - Footer: Action buttons

2. **Profession Selection Screen** (if separate):
   - Grid layout with profession icons
   - Search/filter bar
   - Hover previews
   - Category filtering (farmers, traders, etc.)

3. **Permission Management Panel**:
   - Trade permission dropdown
   - Player whitelist editor
   - Lock/unlock toggle
   - Transfer ownership button

4. **Ownership Transfer Dialog**:
   - Player selector
   - Confirmation screen
   - Success/error feedback

**Design Principles**:
- **Modern Aesthetics**: Gradients, shadows, rounded corners
- **Dark Theme**: Easy on the eyes for long sessions
- **Visual Hierarchy**: Important info stands out
- **Consistent**: Reusable components and patterns
- **Responsive**: Works at 1080p, 1440p, 4K
- **Accessible**: High contrast, readable fonts

**Color Palette** (example):
```
- Primary: #3B82F6 (blue)
- Secondary: #8B5CF6 (purple)
- Success: #10B981 (green)
- Warning: #F59E0B (amber)
- Error: #EF4444 (red)
- Background: #1F2937 (dark gray)
- Surface: #374151 (medium gray)
- Text: #F9FAFB (off-white)
```

**Deliverables**:
- Mockups for all screens (PNG/Figma)
- Component library design
- Color palette definition
- Typography system
- Spacing/layout grid

**Acceptance Criteria**:
- [ ] All screens designed with mockups
- [ ] Consistent design language
- [ ] User feedback gathered (if possible)
- [ ] Approved by stakeholders
- [ ] Assets prepared for implementation

---

### P4-TASK-011: Implement Modern UI Components
**Status**: 📋 TODO
**Priority**: High
**Assignee**: minecraft-developer
**Estimated Effort**: 1-2 weeks
**Dependencies**: P4-TASK-010

**Goal**: Implement reusable modern UI components based on mockups.

**Components to Create**:

1. **Modern Button**:
   - Gradient background
   - Hover animation
   - Click feedback
   - Disabled state
   - Icon support

2. **Modern Panel**:
   - Rounded corners
   - Shadow/glow effect
   - Transparent background option
   - Collapsible sections

3. **Modern Dropdown**:
   - Smooth open/close animation
   - Search/filter capability
   - Custom rendering per item
   - Keyboard navigation

4. **Modern Tooltip**:
   - Rich text formatting
   - Multi-line support
   - Icon integration
   - Smooth fade in/out

5. **Modern Text Input**:
   - Underline animation
   - Placeholder text
   - Validation feedback
   - Character limit indicator

6. **Modern Toggle Switch**:
   - Smooth slide animation
   - On/off states
   - Disabled state
   - Custom colors

7. **Modern Progress Bar**:
   - Animated fill
   - Percentage display
   - Custom colors
   - Glow effect

**Technical Requirements**:
- Extend vanilla widget classes where possible
- Rendering optimizations (batch rendering)
- Shader support (gradients, shadows)
- Animation framework (smooth transitions)
- Theme system (colors, fonts configurable)

**Performance Targets**:
- Render time: <2ms per component
- No FPS drop with 20+ components on screen
- Smooth 60 FPS animations

**Acceptance Criteria**:
- [ ] All components implemented
- [ ] Components match mockups
- [ ] Smooth animations working
- [ ] Performance targets met
- [ ] Reusable and well-documented
- [ ] Example usage provided

---

### P4-TASK-012: Redesign Unified Guard Management Screen
**Status**: 📋 TODO
**Priority**: High
**Assignee**: minecraft-developer
**Estimated Effort**: 1 week
**Dependencies**: P4-TASK-011

**Goal**: Rebuild the main management screen using modern UI components.

**Implementation Steps**:

1. **Header Section**:
   - Villager portrait (larger, centered)
   - Profession and level
   - Ownership badge (owner avatar + name)
   - Quick stats (HP, damage, emeralds)

2. **Left Panel - Rank Progression**:
   - Visual rank tree (nodes and connections)
   - Current rank highlighted
   - Unlocked ranks in color, locked grayed out
   - Purchase buttons with emerald icons
   - Hover tooltips with rank details

3. **Right Panel - Behavior & Permissions**:
   - Mode selector (STAND / FOLLOW / PATROL)
   - Patrol radius slider (visual feedback)
   - Permission toggles
   - Lock/unlock button
   - Transfer ownership button

4. **Footer Section**:
   - "Change Profession" button
   - "Close" button
   - Status bar (messages, notifications)

5. **Animations**:
   - Smooth panel transitions
   - Button hover effects
   - Rank purchase celebration
   - Permission change feedback

6. **Responsive Layout**:
   - Scale to different resolutions
   - Maintain aspect ratio
   - Readable on all screen sizes

**Acceptance Criteria**:
- [ ] All sections implemented
- [ ] Modern design matches mockups
- [ ] All functionality preserved
- [ ] Animations smooth and polished
- [ ] Ownership system integrated
- [ ] Performance meets targets
- [ ] User testing feedback positive
- [ ] No regressions in functionality

---

### P4-TASK-013: Polish & Final Testing
**Status**: 📋 TODO
**Priority**: Medium
**Assignee**: minecraft-qa-specialist, minecraft-developer
**Estimated Effort**: 3-5 days
**Dependencies**: P4-TASK-012

**Goal**: Final polish, bug fixing, and comprehensive testing of entire Epic 04.

**Polish Tasks**:
- Smooth out animations
- Adjust colors for better contrast
- Add particle effects for celebrations
- Improve sound effects
- Final UI tweaks based on feedback

**Testing Checklist**:

1. **Ownership System**:
   - [ ] Bind/unbind tested with 5+ players
   - [ ] Permission system tested thoroughly
   - [ ] Transfer ownership tested
   - [ ] Guard protection tested in PvP
   - [ ] Data persistence verified
   - [ ] Performance tested with 100+ villagers

2. **UI Testing**:
   - [ ] All screens tested at 1080p, 1440p, 4K
   - [ ] Dark theme validated
   - [ ] Animations smooth at 60 FPS
   - [ ] No UI overlap or clipping
   - [ ] Keyboard navigation works
   - [ ] Tooltips display correctly

3. **Integration Testing**:
   - [ ] Ownership + UI work together
   - [ ] No conflicts with guard AI
   - [ ] Config changes apply correctly
   - [ ] Multiplayer sync works
   - [ ] No duplication bugs

4. **Regression Testing**:
   - [ ] All Epic 01-03 features still work
   - [ ] Guard combat unchanged
   - [ ] Rank system unchanged
   - [ ] Profession changes still work

**Bug Fixes**:
- Create issues for all bugs found
- Prioritize by severity
- Fix all critical/high bugs
- Document known minor bugs

**Acceptance Criteria**:
- [ ] All critical bugs fixed
- [ ] Performance targets met
- [ ] User acceptance testing complete
- [ ] Documentation updated
- [ ] Ready for production release

---

## Task Summary

### Phase 1 (Testing & Config)
- **P4-TASK-001**: Test all guard modes *(2-3 hours)*
- **P4-TASK-002**: Test patrol radius system *(2 hours)*
- **P4-TASK-003**: Config file audit & cleanup *(1-2 hours)*

**Total Phase 1**: ~5-7 hours

### Phase 2 (Ownership System)
- **P4-TASK-004**: Design ownership data model *(3-4 hours)*
- **P4-TASK-005**: Implement binding & unbinding *(4-5 hours)*
- **P4-TASK-006**: Implement permission system *(5-6 hours)*
- **P4-TASK-007**: Implement ownership transfer *(3-4 hours)*
- **P4-TASK-008**: Implement guard protection *(4-5 hours)*

**Total Phase 2**: ~19-24 hours (3-4 days)

### Phase 3 (UI Modernization)
- **P4-TASK-009**: Research UI approach *(4-6 hours)*
- **P4-TASK-010**: Design UI mockups *(6-8 hours)*
- **P4-TASK-011**: Implement UI components *(40-80 hours / 1-2 weeks)*
- **P4-TASK-012**: Redesign management screen *(40 hours / 1 week)*
- **P4-TASK-013**: Polish & testing *(24-40 hours / 3-5 days)*

**Total Phase 3**: ~114-174 hours (3-4 weeks)

**Epic 04 Total**: ~138-205 hours (4-6 weeks)

---

**Next Task**: P4-TASK-001 (Test all guard modes)
**Ready to Start**: Yes (no dependencies)
