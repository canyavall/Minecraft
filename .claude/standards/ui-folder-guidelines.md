# UI Folder Guidelines

## Overview

The `{{project}}/.claude/ui/` folder is used for storing UI/UX design assets, specifications, and mockups that guide the development of graphical user interfaces in Minecraft mods.

## Purpose

The UI folder serves as a centralized location for:
- Visual design mockups and prototypes
- UI layout specifications
- Design system documentation
- Reference images for GUI implementation
- PSD/design source files

## Folder Structure

```
{{project}}/.claude/ui/
├── *.png                          # UI mockup images
├── *.psd                          # Photoshop design source files
├── *.md                           # UI specifications and documentation
└── layout-specification.md        # Layout guidelines (recommended)
```

## File Types and Usage

### Design Assets (.png, .jpg)

**Purpose**: Visual mockups showing desired UI appearance

**Examples**:
- `desired_ui.png` - Overall UI mockup showing target design
- `button_styles.png` - Button design reference
- `screen_layout.png` - Screen organization mockup

**Usage**:
- minecraft-developer references these when implementing GUI components
- Provides visual target for pixel-perfect implementation
- Used in design reviews and user feedback

### Design Source Files (.psd, .ai, .sketch)

**Purpose**: Editable source files for design iteration

**Examples**:
- `New layout.psd` - Photoshop file with design layers
- `ui_components.ai` - Illustrator vector components

**Usage**:
- Allows design modifications without starting from scratch
- Preserves layers, guides, and design elements
- Can be exported to various formats as needed

**Note**: Source files can be large (MB+). Consider if they should be tracked in version control or stored separately.

### Specification Documents (.md)

**Purpose**: Written specifications describing UI behavior and layout

**Examples**:
- `layout-specification.md` - Detailed layout dimensions and positioning
- `ui-specifications.md` - Component behavior and interaction specs
- `design-system.md` - Consistent design patterns and guidelines

**Content Should Include**:
- Exact pixel dimensions and positioning
- Color codes and styling
- Component behavior descriptions
- Interaction flows
- Accessibility requirements
- Responsive design considerations

## When to Use the UI Folder

### ✅ USE the UI folder for:
- Complex GUI screens requiring visual mockups
- Custom UI components with specific styling
- Multi-screen workflows needing layout planning
- Design specifications for pixel-perfect implementation
- Reference images for texture-based UI elements

### ❌ DON'T USE the UI folder for:
- Simple vanilla-style GUIs (standard Minecraft screens)
- Game textures (those go in `src/main/resources/assets/`)
- Entity textures or models (separate from UI)
- Temporary screenshots (use `temp/` folder)
- Documentation images (embed in markdown or use `docs/`)

## Specification Document Template

When creating UI specifications, include:

```markdown
# [Screen Name] UI Specification

## Overview
Brief description of the screen's purpose

## Layout
- Screen size: [width] x [height] pixels
- Centered/anchored: [center/top-left/etc]
- Background: [style/texture/color]

## Components

### [Component Name]
- Type: [button/text/slider/etc]
- Position: X=[x], Y=[y]
- Size: Width=[w], Height=[h]
- Style: [description]
- Behavior: [interaction description]
- Tooltip: [tooltip text if applicable]

## Visual Design
- Color palette: [list colors with hex codes]
- Font: [Minecraft default or custom]
- Icons: [list any icons needed]
- Textures: [list texture files]

## Interactions
- Click behavior: [description]
- Hover effects: [description]
- Keyboard shortcuts: [if applicable]
- Validation: [form validation rules]

## Responsive Behavior
- Scaling: [how UI adapts to different screen sizes]
- Minimum resolution: [if applicable]

## Accessibility
- Narration support: [screen reader text]
- Keyboard navigation: [tab order]
- Color contrast: [contrast ratios]
```

## Integration with Development

### For minecraft-developer:

1. **Before Implementation**:
   - Review all UI mockups in `ui/` folder
   - Read specification documents thoroughly
   - Ask questions if specs are unclear

2. **During Implementation**:
   - Match mockups as closely as possible
   - Use exact dimensions from specifications
   - Test in-game appearance against mockups

3. **After Implementation**:
   - Take screenshots of implemented UI
   - Compare against mockups for accuracy
   - Document any deviations and reasons

### For project-scope-manager:

1. **Feature Planning**:
   - Create UI mockups for complex features
   - Write detailed specifications
   - Store in `{{project}}/.claude/ui/`

2. **Design Review**:
   - Review implemented UI against mockups
   - Provide feedback for adjustments
   - Update specs based on implementation learnings

## Examples from xeena-village-manager

### Files Present:
```
xeena-village-manager/.claude/ui/
├── desired_ui.png              # Overall UI mockup (231 KB)
├── layout-specification.md     # Detailed layout specs (12 KB)
├── New layout.psd              # Editable source file (3.3 MB)
└── ui-specifications.md        # Component behavior specs (5 KB)
```

### Purpose:
- `desired_ui.png`: Shows target design for guard management GUI
- `layout-specification.md`: Specifies exact pixel positions for components
- `New layout.psd`: Source file for iterating on design
- `ui-specifications.md`: Describes component interactions and behaviors

### Usage in Development:
1. Developer reads specifications to understand requirements
2. Developer references mockup images during implementation
3. Developer uses Photoshop file if design tweaks are needed
4. Implemented GUI is validated against mockup

## Best Practices

### File Naming:
- Use descriptive names: `guard_management_screen.png` not `screen1.png`
- Include version if iterating: `layout_v2.png`
- Use lowercase with underscores or hyphens

### Organization:
- Group related assets together
- Consider subfolders for large projects:
  ```
  ui/
  ├── screens/
  │   ├── guard_management/
  │   └── profession_selection/
  └── components/
      ├── buttons/
      └── widgets/
  ```

### Version Control:
- Track mockup images (PNG, JPG) in git
- Consider `.gitignore` for large source files (PSD, AI)
- Store large files separately if needed (cloud storage)

### Documentation:
- Always include a specification document
- Update specs when design changes
- Link to mockups from task descriptions

### Collaboration:
- Use mockups to communicate design intent
- Get user feedback before implementation
- Iterate on designs in source files, export to PNG for reference

## Cleanup Guidelines

### When to Remove Files:
- UI has been fully implemented and validated
- Design mockups no longer needed for reference
- Source files archived elsewhere

### What to Keep:
- Final approved mockups (historical reference)
- Specification documents (implementation proof)
- Critical design decisions and rationale

### Archiving:
- Move old designs to `{{project}}/.claude/ui/archive/`
- Keep current design files in root `ui/` folder
- Document why old designs were replaced

---

**Related Guidelines**:
- `.claude/standards/minecraft-modding-standards.md` - General coding standards
- `{{project}}/.claude/temp/` - Temporary files and screenshots
- `src/main/resources/assets/` - Actual in-game textures and assets
