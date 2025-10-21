---
name: ui-ux-design
description: UI/UX design guidelines for Minecraft GUI development including mockup creation, layout specifications, HandledScreen patterns, and texture coordinates. Use when designing or implementing custom Minecraft screens and widgets.
allowed-tools: [Read, Write, Edit]
---

# UI/UX Design Skill

Comprehensive guide for designing and implementing custom user interfaces in Minecraft mods.

## UI Folder System

### Purpose and Location

**Folder**: `{{project}}/.claude/ui/`

The UI folder stores design assets, specifications, and mockups that guide GUI implementation.

**Use for**:
- Visual design mockups and prototypes
- Layout specifications with exact dimensions
- Design system documentation
- Reference images for GUI implementation
- Source files (PSD, Sketch, Figma exports)

### When to Use UI Folder

#### ✅ CREATE UI specifications for:
- Complex custom GUI screens (multi-tab, dynamic layouts)
- Custom widgets with specific styling
- Multi-screen workflows needing layout planning
- Pixel-perfect implementation requirements
- Texture-based UI elements

#### ❌ DON'T USE UI folder for:
- Simple vanilla-style GUIs (standard Minecraft screens)
- Game textures (those go in `src/main/resources/assets/`)
- Entity textures or models
- Temporary screenshots (use `{{project}}/.claude/temp/`)
- Documentation images

### File Types and Organization

```
{{project}}/.claude/ui/
├── *.png                          # UI mockup images
├── *.psd, *.ai, *.sketch          # Design source files
├── layout-specification.md        # Exact layout dimensions
└── ui-specifications.md           # Component behavior specs
```

#### Design Assets (.png, .jpg)
**Purpose**: Visual mockups showing desired UI appearance

**Examples**:
- `desired_ui.png` - Overall UI mockup
- `guard_management_screen.png` - Specific screen design
- `button_styles.png` - Button design reference

**Usage**:
- implementation-agent references during GUI implementation
- Provides visual target for pixel-perfect implementation
- Used in design reviews and user feedback

#### Design Source Files (.psd, .ai, .sketch)
**Purpose**: Editable source files for design iteration

**Examples**:
- `New layout.psd` - Photoshop file with layers
- `ui_components.ai` - Illustrator vector components
- `design.sketch` - Sketch design file

**Usage**:
- Allows design modifications without starting from scratch
- Preserves layers, guides, and design elements
- Can be exported to various formats

**Note**: Source files can be large (MB+). Consider `.gitignore` for very large files.

#### Specification Documents (.md)
**Purpose**: Written specifications describing UI behavior and layout

**Required**:
- `layout-specification.md` - Exact pixel dimensions and positioning
- `ui-specifications.md` - Component behavior and interactions

**Optional**:
- `design-system.md` - Consistent design patterns

## Minecraft GUI Constraints

### HandledScreen System

**Server-side**: `ScreenHandler` (logic, inventory, slots)
**Client-side**: `HandledScreen` (rendering, user input)

```java
// Server-side: ScreenHandler
public class GuardManagementScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public GuardManagementScreenHandler(int syncId, PlayerInventory playerInv, Inventory inv) {
        super(ModScreenHandlers.GUARD_MANAGEMENT, syncId);
        this.inventory = inv;

        // Add slots (these define clickable areas)
        this.addSlot(new Slot(inv, 0, 80, 35)); // x=80, y=35
    }
}

// Client-side: HandledScreen
@Environment(EnvType.CLIENT)
public class GuardManagementScreen extends HandledScreen<GuardManagementScreenHandler> {
    private static final Identifier TEXTURE =
        Identifier.of(MOD_ID, "textures/gui/guard_management.png");

    public GuardManagementScreen(GuardManagementScreenHandler handler,
                                 PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 176;  // Standard chest width
        this.backgroundHeight = 166; // Standard chest height
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Draw background texture
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
```

### Standard Minecraft GUI Dimensions

**Common sizes**:
- **Chest**: 176×166 pixels
- **Furnace**: 176×166 pixels
- **Crafting Table**: 176×166 pixels
- **Hopper**: 176×133 pixels
- **Dispenser**: 176×166 pixels

**Slot size**: 18×18 pixels (16×16 item + 1px border)
**Slot spacing**: Usually 18px center-to-center

### Texture Coordinates

Minecraft GUIs use a 256×256 texture atlas:

```java
// drawTexture(texture, screenX, screenY, textureU, textureV, width, height)
context.drawTexture(
    TEXTURE,
    x + 10,      // Screen X position
    y + 20,      // Screen Y position
    0,           // Texture U (X in texture file)
    0,           // Texture V (Y in texture file)
    176,         // Width to draw
    166          // Height to draw
);
```

**Texture Layout** (256×256 PNG):
```
0,0 ───────────────────────────────► 256
│  ┌──────────────────┐
│  │  Background      │ ← Main GUI background (0,0) → (176,166)
│  │  (176×166)       │
│  └──────────────────┘
│  ┌────┐ ┌────┐ ┌────┐
│  │Tab1│ │Tab2│ │Tab3│ ← Tabs and widgets below main
│  └────┘ └────┘ └────┘
▼
256
```

### Widget Positioning

**Screen Positioning**:
- `x` and `y` are calculated from `backgroundWidth` and `backgroundHeight`
- Center of screen: `(width - backgroundWidth) / 2`, `(height - backgroundHeight) / 2`
- Widgets positioned relative to `x` and `y`

```java
@Override
protected void init() {
    super.init();

    // x and y are set by parent class
    int centerX = x + backgroundWidth / 2;
    int topY = y + 10;

    // Add button centered horizontally
    this.addDrawableChild(ButtonWidget.builder(
        Text.literal("Click Me"),
        button -> onButtonClick()
    )
    .dimensions(centerX - 50, topY, 100, 20) // Centered 100px wide button
    .build());
}
```

## Layout Specification Template

### Required Format

**File**: `{{project}}/.claude/ui/layout-specification.md`

```markdown
# [Screen Name] Layout Specification

## Overview
Brief description of the screen's purpose and user workflow.

## Screen Dimensions

**Base Dimensions**:
- Width: [width] pixels (e.g., 176 for standard chest width)
- Height: [height] pixels (e.g., 166 for standard chest height)
- Centered: Yes/No
- Resizable: Yes/No

**Calculated Positions** (relative to screen center):
- x = `(screenWidth - backgroundWidth) / 2`
- y = `(screenHeight - backgroundHeight) / 2`

## Visual Layout (ASCII Diagram)

```
┌─────────────────────────────────────────────┐
│ [Title Text]                             [X]│ ← Header (20px)
├─────────────────────────────────────────────┤
│                                             │
│              Main Content Area              │ ← Content (126px)
│                                             │
│  ┌───┐ ┌───┐ ┌───┐ ┌───┐                   │
│  │ 1 │ │ 2 │ │ 3 │ │ 4 │   ← Widget row    │
│  └───┘ └───┘ └───┘ └───┘                   │
│                                             │
├─────────────────────────────────────────────┤
│  [Cancel]                    [Confirm]      │ ← Footer (20px)
└─────────────────────────────────────────────┘
```

## Component Specifications

### [Component Name 1]
- **Type**: Button/Text/Slider/Custom Widget
- **Position**: X=[x], Y=[y] (relative to `this.x`, `this.y`)
- **Size**: Width=[w], Height=[h]
- **Texture Coords**: U=[u], V=[v] (in texture atlas)
- **Style**: [description]
- **Behavior**: [interaction description]
- **Tooltip**: [tooltip text if applicable]

### [Component Name 2]
...

## Texture Atlas Mapping

**Texture File**: `assets/[modid]/textures/gui/[screen_name].png`
**Size**: 256×256 pixels

```
U/V Coordinates (in 256×256 texture):
- Background: (0, 0) → (176, 166)
- Tab Active: (0, 166) → (32, 198)
- Tab Inactive: (32, 166) → (64, 198)
- Button Normal: (0, 200) → (100, 220)
- Button Hover: (100, 200) → (200, 220)
```

## Grid Calculations

**Example: 8×3 profession grid**
```
Grid: 8 columns × 3 rows
Button Size: 32×32 pixels
Spacing: 4 pixels between buttons
Total Width: 8 * 32 + 7 * 4 = 284 pixels
Total Height: 3 * 32 + 2 * 4 = 104 pixels

Start X = this.x + (backgroundWidth - 284) / 2
Start Y = this.y + 32 (below tab bar)

Button[col][row] position:
X = startX + col * (32 + 4)
Y = startY + row * (32 + 4)
```

## Color Palette

- **Primary**: #8B4513 (Saddle Brown) - RGB(139, 69, 19)
- **Secondary**: #D2691E (Chocolate) - RGB(210, 105, 30)
- **Text**: #404040 (Dark Gray) - RGB(64, 64, 64)
- **Accent**: #FFD700 (Gold) - RGB(255, 215, 0)

## Interactions

### Click Behavior
- Left-click: [action]
- Right-click: [action]
- Shift-click: [action]

### Hover Effects
- Mouse over: [visual change]
- Tooltip delay: [ms]

### Keyboard Shortcuts
- E: Close screen
- Escape: Close screen
- Tab: [if applicable]

## Responsive Behavior

**Scaling**: Fixed/Dynamic
**Minimum Resolution**: [e.g., 854×480]
**Max Content**: [how UI adapts if content exceeds space]

## Implementation Notes

**Special Considerations**:
- [Any non-obvious implementation details]
- [Performance considerations]
- [Compatibility notes]
```

## UI Behavior Specification Template

### Required Format

**File**: `{{project}}/.claude/ui/ui-specifications.md`

```markdown
# [Screen Name] UI Behavior Specification

## User Workflow

1. **Screen Opens**: [How user opens this screen]
2. **Initial State**: [What user sees immediately]
3. **User Actions**: [Available interactions]
4. **Screen Closes**: [How/when screen closes]

## State Management

### Screen States
- **Default**: [Initial state description]
- **Loading**: [If data is loading]
- **Error**: [Error state appearance]
- **Empty**: [No data state]

### State Transitions
```
Default → Loading → Success/Error
Empty → Default (when data added)
```

## Component Behaviors

### [Component Name]
**Type**: Button/Widget/Custom
**States**:
- Normal: [appearance]
- Hover: [appearance]
- Pressed: [appearance]
- Disabled: [appearance]

**Interactions**:
- Click: [action taken, packets sent]
- Hover: [tooltip shown, visual feedback]

**Validation**:
- [Input validation rules if applicable]
- [Error messages shown]

## Networking

### Packets Sent (Client → Server)
- **Open Screen**: [packet details]
- **User Action**: [packet details with payload]
- **Close Screen**: [cleanup packets]

### Packets Received (Server → Client)
- **Screen Data**: [initial data sync]
- **Updates**: [real-time updates]

## Accessibility

### Narration Support
- Screen title: "[screen name]"
- Component narration: "[button] [label]"

### Keyboard Navigation
- Tab order: [component order]
- Enter/Space: Activate focused component
- Escape: Close screen

### Visual Accessibility
- Color contrast ratio: [ratio] (WCAG AA: 4.5:1)
- Text size: [minimum size]

## Error Handling

### User Errors
- **Invalid Input**: [message shown, visual feedback]
- **Insufficient Resources**: [error message]
- **Permission Denied**: [feedback]

### System Errors
- **Network Error**: [how UI responds]
- **Server Error**: [fallback behavior]

## Performance Considerations

- **Render Budget**: [target FPS]
- **Update Frequency**: [how often UI updates]
- **Lazy Loading**: [if applicable]
```

## Minecraft GUI Best Practices

### Visual Design

**Follow Minecraft Aesthetics**:
- Use vanilla Minecraft colors and fonts
- Match vanilla GUI style (wooden panels, stone backgrounds)
- Use Minecraft's default font (unless custom font is theme-appropriate)

**Texture Guidelines**:
- 256×256 PNG for GUI textures
- Use powers of 2 for texture dimensions
- Include hover states for interactive elements
- Maintain consistent style with vanilla GUIs

### Layout Design

**Grid-Based Layouts**:
```java
// Good: Grid-aligned, predictable spacing
int gridX = startX + col * (buttonSize + spacing);
int gridY = startY + row * (buttonSize + spacing);

// Bad: Random positioning
int buttonX = startX + 15; // Magic number, no pattern
int buttonY = startY + 23; // Arbitrary
```

**Responsive Positioning**:
```java
// Good: Relative to screen size
int centerX = x + backgroundWidth / 2;
int buttonX = centerX - buttonWidth / 2; // Centered

// Bad: Absolute positioning
int buttonX = 100; // Breaks on different screen sizes
```

### Widget Implementation

**Button Widgets**:
```java
// ✅ GOOD - Clear, reusable pattern
private ButtonWidget createActionButton(Text label, int x, int y, ButtonWidget.PressAction action) {
    return ButtonWidget.builder(label, action)
        .dimensions(x, y, 100, 20)
        .tooltip(Tooltip.of(Text.literal("Description")))
        .build();
}

// Usage
this.addDrawableChild(createActionButton(
    Text.literal("Hire Guard"),
    x + 10,
    y + 40,
    button -> onHireGuard()
));
```

**Custom Widgets**:
```java
// ✅ GOOD - Extends ClickableWidget for custom behavior
public class ProfessionButton extends ClickableWidget {
    private final Profession profession;
    private final Identifier icon;

    public ProfessionButton(int x, int y, Profession profession) {
        super(x, y, 32, 32, Text.of(profession.getName()));
        this.profession = profession;
        this.icon = profession.getIcon();
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Custom rendering
        int u = this.isHovered() ? 32 : 0;
        context.drawTexture(WIDGETS_TEXTURE, getX(), getY(), u, 0, width, height);
        context.drawTexture(icon, getX() + 8, getY() + 8, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        // Handle click
    }
}
```

### Performance Optimization

**Render Optimization**:
```java
// ✅ GOOD - Only render what's visible
@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    super.render(context, mouseX, mouseY, delta);

    // Only render widgets in viewport
    for (ProfessionButton button : professionButtons) {
        if (isVisible(button)) {
            button.render(context, mouseX, mouseY, delta);
        }
    }
}

// ❌ BAD - Render everything every frame
@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    // Renders 1000+ widgets even if not visible
    for (Widget widget : allWidgets) {
        widget.render(context, mouseX, mouseY, delta);
    }
}
```

**Cache Calculations**:
```java
// ✅ GOOD - Calculate once in init()
@Override
protected void init() {
    super.init();
    this.cachedPositions = calculateGridPositions(); // Calculate once
}

// ❌ BAD - Calculate every frame
@Override
public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    List<Position> positions = calculateGridPositions(); // Expensive!
}
```

## Integration with Development Workflow

### For epic-agent (Design Phase)

**When creating epic with GUI**:
1. Create `{{project}}/.claude/ui/` folder
2. Create mockup images (use design tool or hand-drawn)
3. Write `layout-specification.md` with exact dimensions
4. Write `ui-specifications.md` with behavior details
5. Add UI design task to epic requirements

### For implementation-agent (Implementation Phase)

**Before implementing GUI**:
1. Read `{{project}}/.claude/ui/layout-specification.md`
2. Read `{{project}}/.claude/ui/ui-specifications.md`
3. View mockup images for visual reference
4. Understand exact dimensions and positioning

**During implementation**:
1. Match mockups as closely as possible
2. Use exact dimensions from specifications
3. Implement all specified behaviors
4. Follow Minecraft GUI best practices

**After implementation**:
1. Take screenshots of implemented UI
2. Compare against mockups for accuracy
3. Document any deviations and reasons
4. Store screenshots in `{{project}}/.claude/temp/` for user review

### For validation-agent (Testing Phase)

**UI Testing Checklist**:
- [ ] Screen opens correctly
- [ ] All widgets render at correct positions
- [ ] Hover states work as specified
- [ ] Click interactions trigger correct actions
- [ ] Keyboard navigation works (Escape closes, Tab cycles)
- [ ] Screen scales properly on different resolutions
- [ ] No visual glitches or z-fighting
- [ ] Tooltips appear correctly
- [ ] Accessibility features work (narration, contrast)

## File Naming Conventions

**Mockup Images**:
- `desired_ui.png` - Overall UI mockup
- `[screen_name]_layout.png` - Specific screen layout
- `[component]_states.png` - Component state variations

**Source Files**:
- `[screen_name]_design.psd` - Photoshop source
- `ui_components.ai` - Shared components library

**Specifications**:
- `layout-specification.md` - Layout and positioning (required)
- `ui-specifications.md` - Behavior and interactions (required)
- `design-system.md` - Design patterns (optional)

## Common Patterns

### Tabbed Interface

```java
public class TabbedManagementScreen extends HandledScreen<TabbedScreenHandler> {
    private static final int TAB_HEIGHT = 32;
    private List<TabButton> tabs = new ArrayList<>();
    private int activeTabIndex = 0;

    @Override
    protected void init() {
        super.init();

        // Create tabs
        int tabX = x;
        for (int i = 0; i < tabCount; i++) {
            TabButton tab = new TabButton(tabX, y, tabs.get(i), i == activeTabIndex);
            this.addDrawableChild(tab);
            tabX += 32;
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Draw background
        context.drawTexture(TEXTURE, x, y + TAB_HEIGHT, 0, 0, backgroundWidth, backgroundHeight - TAB_HEIGHT);

        // Draw active tab content
        tabs.get(activeTabIndex).renderContent(context, x, y + TAB_HEIGHT);
    }
}
```

### Scrollable List

```java
public class ScrollableListWidget extends EntryListWidget<Entry> {
    public ScrollableListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
    }

    public void addEntry(Entry entry) {
        this.addEntry(entry);
    }

    @Override
    public int getRowWidth() {
        return this.width - 20; // Leave space for scrollbar
    }
}
```

### Confirmation Dialog

```java
public void openConfirmDialog(Text message, Runnable onConfirm) {
    client.setScreen(new ConfirmScreen(
        confirmed -> {
            if (confirmed) {
                onConfirm.run();
            }
            client.setScreen(this); // Return to this screen
        },
        Text.literal("Confirm Action"),
        message
    ));
}
```

## When to Use This Skill

Use this skill when:
- Designing custom Minecraft GUI screens
- Creating UI mockups and specifications
- Implementing HandledScreen or custom widgets
- Planning multi-screen workflows
- Writing layout specifications
- Positioning widgets and calculating coordinates
- Creating texture atlases for GUIs
- Implementing tabbed interfaces, scrollable lists, or custom widgets
- Questions about "How should I design X screen?"
- Questions about "What dimensions should I use?"
- Questions about "How do I position widgets in Minecraft?"

## Key Principles

1. **Specification First** - Create layout spec before implementation
2. **Follow Vanilla Style** - Match Minecraft's aesthetic
3. **Grid-Based Layouts** - Use consistent spacing and alignment
4. **Relative Positioning** - Never use absolute screen coordinates
5. **Texture Atlas** - Organize all GUI textures in 256×256 atlas
6. **Performance Conscious** - Cache calculations, cull invisible widgets
7. **Accessibility** - Support narration, keyboard navigation, color contrast
8. **Document Deviations** - Explain why implementation differs from mockup
9. **User Testing** - Always test with user before finalizing
10. **Mockup Accuracy** - Implement as close to mockup as possible
