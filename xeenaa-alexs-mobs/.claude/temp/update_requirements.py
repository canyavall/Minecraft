#!/usr/bin/env python3
"""
Update all epic requirements.md files with detailed features from mob catalog.
Epic-agent script to systematically update epics 004-100.
"""

import os
import re
from pathlib import Path
from typing import Dict, List, Optional

# Configuration
EPICS_DIR = Path(r"C:\Users\canya\Documents\projects\Minecraft\xeenaa-alexs-mobs\.claude\epics")
CATALOG_FILE = Path(r"C:\Users\canya\Documents\projects\Minecraft\xeenaa-alexs-mobs\.claude\research\mob-features-comprehensive-catalog.md")

class MobFeatures:
    """Container for all features of a mob"""
    def __init__(self, name: str):
        self.name = name
        self.behavior_category = ""
        self.biomes = []
        self.core_features = []
        self.taming = {}
        self.special_mechanics = []
        self.items = {}
        self.ai_behaviors = {}
        self.equipment = []
        self.breeding = {}
        self.variants = []
        self.combat = {}

    def has_taming(self) -> bool:
        return bool(self.taming)

    def has_special_mechanics(self) -> bool:
        return len(self.special_mechanics) > 0

    def has_equipment(self) -> bool:
        return len(self.equipment) > 0

    def has_breeding(self) -> bool:
        return bool(self.breeding)


def parse_catalog() -> Dict[str, MobFeatures]:
    """Parse the comprehensive catalog and extract all mob features"""
    print("Parsing mob-features-comprehensive-catalog.md...")

    with open(CATALOG_FILE, 'r', encoding='utf-8') as f:
        content = f.read()

    mobs = {}

    # Split by animal headers (### Animal Name)
    animal_sections = re.split(r'\n### ', content)

    for section in animal_sections[1:]:  # Skip intro
        lines = section.split('\n')
        name = lines[0].strip()

        # Skip the summary sections at the end
        if name in ['Taming Systems', 'Transportation', 'Inventory Systems',
                    'Item Collection', 'Combat Abilities', 'Environmental Interactions',
                    'Special Movement', 'Crafting Systems', 'Bunfungus (see Overworld Neutral)']:
            continue

        mob = MobFeatures(name)

        # Parse the section - just capture raw text under each header
        current_subsection = None
        collecting_list = False

        for line in lines[1:]:
            # Stop if we hit the separator or next section
            if line.strip() == '---' or line.startswith('###'):
                break

            # Check for subsection headers
            if line.startswith('**Behavior Category**:'):
                mob.behavior_category = line.split(':', 1)[1].strip()
                current_subsection = None
            elif line.startswith('**Biomes**:'):
                mob.biomes = [b.strip() for b in line.split(':', 1)[1].split(',')]
                current_subsection = None
            elif line.startswith('**Core Features**:'):
                current_subsection = 'core_features'
                collecting_list = True
                continue
            elif line.startswith('**Taming**:'):
                current_subsection = 'taming'
                collecting_list = True
                # Check if it's just "N/A"
                if 'N/A' in line:
                    current_subsection = None
                continue
            elif line.startswith('**Special Mechanics**:'):
                current_subsection = 'special_mechanics'
                collecting_list = True
                continue
            elif line.startswith('**Items/Drops**:'):
                current_subsection = 'items'
                collecting_list = True
                continue
            elif line.startswith('**AI Behaviors**:'):
                current_subsection = 'ai_behaviors'
                collecting_list = True
                continue
            elif line.startswith('**Equipment/Gear**:'):
                current_subsection = 'equipment'
                collecting_list = True
                if 'None' in line:
                    current_subsection = None
                continue
            elif line.startswith('**Breeding**:'):
                current_subsection = 'breeding'
                collecting_list = True
                if 'N/A' in line or 'Not specified' in line:
                    current_subsection = None
                continue
            elif line.startswith('**Variants**:'):
                current_subsection = 'variants'
                collecting_list = True
                if 'None' in line:
                    current_subsection = None
                continue
            elif line.startswith('**Combat**:'):
                current_subsection = 'combat'
                collecting_list = True
                if 'None' in line:
                    current_subsection = None
                continue

            # Collect content
            if current_subsection and line.strip():
                if line.startswith('- '):
                    content = line[2:].strip()
                    if current_subsection == 'core_features':
                        mob.core_features.append(content)
                    elif current_subsection == 'special_mechanics':
                        mob.special_mechanics.append(content)
                    elif current_subsection == 'equipment':
                        mob.equipment.append(content)
                    elif current_subsection == 'variants':
                        mob.variants.append(content)
                    elif current_subsection in ['taming', 'items', 'ai_behaviors', 'breeding', 'combat']:
                        # Parse key: value format
                        key_val = content.split(':', 1)
                        if len(key_val) == 2:
                            key = key_val[0].strip()
                            val = key_val[1].strip()
                            getattr(mob, current_subsection)[key] = val
                        else:
                            # Store as generic content
                            key = f"info_{len(getattr(mob, current_subsection))}"
                            getattr(mob, current_subsection)[key] = content

        mobs[name.lower()] = mob
        print(f"  Parsed: {name}")

    print(f"\nTotal mobs parsed: {len(mobs)}")
    return mobs


def extract_animal_name(epic_folder: str) -> Optional[str]:
    """Extract animal name from epic folder (e.g., '004-fly-flying-passive-insect' -> 'Fly')"""
    # Remove number prefix
    parts = epic_folder.split('-', 1)
    if len(parts) < 2:
        return None

    name_parts = parts[1].split('-')

    # Handle multi-word names
    if name_parts[0] in ['alligator', 'bald', 'banana', 'blue', 'bone', 'cave',
                          'cachalot', 'capuchin', 'comb', 'cosmic', 'crimson', 'devils',
                          'flying', 'frilled', 'gelada', 'giant', 'grizzly',
                          'hammerhead', 'komodo', 'leafcutter', 'maned', 'mantis',
                          'mimic', 'rain', 'rocky', 'sea', 'snow', 'soul',
                          'sugar', 'tarantula', 'tasmanian', 'void', 'warped']:
        if len(name_parts) > 1:
            if name_parts[0] == 'alligator' and name_parts[1] == 'snapping':
                return 'Alligator Snapping Turtle'
            elif name_parts[0] == 'devils' and name_parts[1] == 'hole':
                return "Devil's Hole Pupfish"
            else:
                return ' '.join(name_parts[:2]).title()

    return name_parts[0].title()


def generate_features_section(mob: MobFeatures) -> str:
    """Generate the Features & Functionality section for requirements.md"""
    sections = []

    # Core Features
    if mob.core_features:
        sections.append("### Core Features\n")
        for feature in mob.core_features:
            sections.append(f"- {feature}\n")
        sections.append("\n")

    # Taming
    if mob.has_taming():
        sections.append("### Taming\n")
        for key, value in mob.taming.items():
            sections.append(f"- **{key}**: {value}\n")
        sections.append("\n")

    # Special Mechanics
    if mob.has_special_mechanics():
        sections.append("### Special Mechanics\n")
        for mechanic in mob.special_mechanics:
            if '**' in mechanic:
                sections.append(f"- {mechanic}\n")
            else:
                sections.append(f"- {mechanic}\n")
        sections.append("\n")

    # Items & Interactions
    if mob.items:
        sections.append("### Items & Interactions\n")
        for key, value in mob.items.items():
            sections.append(f"- **{key}**: {value}\n")
        sections.append("\n")

    # AI Behaviors
    if mob.ai_behaviors:
        sections.append("### AI Behaviors\n")
        for key, value in mob.ai_behaviors.items():
            sections.append(f"- **{key}**: {value}\n")
        sections.append("\n")

    # Equipment
    if mob.has_equipment():
        sections.append("### Equipment & Gear\n")
        for item in mob.equipment:
            sections.append(f"- {item}\n")
        sections.append("\n")

    # Breeding
    if mob.has_breeding():
        sections.append("### Breeding\n")
        for key, value in mob.breeding.items():
            sections.append(f"- **{key}**: {value}\n")
        sections.append("\n")

    # Variants
    if mob.variants:
        sections.append("### Variants\n")
        for variant in mob.variants:
            sections.append(f"- {variant}\n")
        sections.append("\n")

    # Combat
    if mob.combat:
        sections.append("### Combat\n")
        for key, value in mob.combat.items():
            sections.append(f"- **{key}**: {value}\n")
        sections.append("\n")

    return ''.join(sections)


def update_requirements_file(epic_path: Path, mob: MobFeatures) -> bool:
    """Update a single requirements.md file with mob features"""
    req_file = epic_path / 'requirements.md'

    if not req_file.exists():
        print(f"  [WARN] requirements.md not found")
        return False

    with open(req_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find the Features & Functionality section
    features_pattern = r'(## Features & Functionality\s*\n)(.*?)(\n## Success Criteria|\n## Technical Scope|\n## User Stories|\Z)'

    match = re.search(features_pattern, content, re.DOTALL)

    if not match:
        print(f"  [WARN] Could not find Features & Functionality section")
        return False

    # Generate new features section
    new_features = generate_features_section(mob)

    # Replace the section
    new_content = content[:match.start(2)] + new_features + content[match.start(3):]

    # Write back
    with open(req_file, 'w', encoding='utf-8') as f:
        f.write(new_content)

    return True


def main():
    """Main execution"""
    print("=" * 80)
    print("EPIC-AGENT: Requirements.md Mass Update")
    print("=" * 80)
    print()

    # Parse catalog
    mobs_catalog = parse_catalog()
    print()

    # Get all epic folders
    epic_folders = sorted([d for d in os.listdir(EPICS_DIR)
                          if d.startswith(tuple(f'{i:03d}-' for i in range(4, 101)))])

    print(f"Found {len(epic_folders)} epic folders to process (004-100)")
    print()

    # Track results
    updated = []
    skipped = []
    not_found = []

    # Process each epic
    for epic_folder in epic_folders:
        epic_num = epic_folder.split('-')[0]
        print(f"[{epic_num}] Processing: {epic_folder}")

        # Extract animal name
        animal_name = extract_animal_name(epic_folder)
        if not animal_name:
            print(f"  [WARN] Could not extract animal name")
            skipped.append(epic_folder)
            continue

        print(f"  Animal: {animal_name}")

        # Look up in catalog
        mob = mobs_catalog.get(animal_name.lower())
        if not mob:
            print(f"  [NOT FOUND] Not in catalog")
            not_found.append((epic_folder, animal_name))
            continue

        # Update requirements.md
        epic_path = EPICS_DIR / epic_folder
        if update_requirements_file(epic_path, mob):
            print(f"  [OK] Updated requirements.md")
            updated.append(epic_folder)
        else:
            skipped.append(epic_folder)

        print()

    # Summary
    print("=" * 80)
    print("SUMMARY")
    print("=" * 80)
    print(f"Total epics processed: {len(epic_folders)}")
    print(f"Successfully updated: {len(updated)}")
    print(f"Skipped (issues): {len(skipped)}")
    print(f"Not found in catalog: {len(not_found)}")
    print()

    if not_found:
        print("Animals not found in catalog:")
        for epic, name in not_found:
            print(f"  - {epic}: {name}")
        print()

    if skipped:
        print("Skipped epics:")
        for epic in skipped:
            print(f"  - {epic}")
        print()

    print("=" * 80)
    print("COMPLETE")
    print("=" * 80)


if __name__ == '__main__':
    main()
