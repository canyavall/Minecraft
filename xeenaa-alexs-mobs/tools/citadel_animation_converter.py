#!/usr/bin/env python3
"""
CitadelAnimationConverter - Convert Citadel procedural animations to GeckoLib keyframe JSON.

Usage:
    python citadel_animation_converter.py --input decompiled/ModelFly.java --output animations/fly_citadel.animation.json
"""

import re
import json
import argparse
from pathlib import Path
from typing import Dict, List, Optional
import math


class CitadelAnimationConverter:
    """Convert Citadel procedural animations to GeckoLib keyframe JSON."""

    def __init__(self, java_file_path: str):
        self.java_file_path = Path(java_file_path)
        with open(java_file_path, 'r', encoding='utf-8') as f:
            self.content = f.read()

        self.bone_vars = {}  # Map variable name to bone name
        self.animations = {}

    def parse(self) -> Dict:
        """Parse Citadel animations to extract animation logic."""
        print(f"Parsing animations from {self.java_file_path.name}...")

        # Find bone variable names
        self.find_bone_vars()
        print(f"  Found {len(self.bone_vars)} bones")

        # Extract different animation types
        self.animations["idle"] = self.extract_idle_animation()
        print(f"  Generated idle animation")

        self.animations["walk"] = self.extract_walk_animation()
        if self.animations["walk"]["bones"]:
            print(f"  Generated walk animation")
        else:
            del self.animations["walk"]

        self.animations["fly"] = self.extract_fly_animation()
        if self.animations["fly"]["bones"]:
            print(f"  Generated fly animation")
        else:
            del self.animations["fly"]

        self.animations["death"] = self.create_default_death_animation()
        print(f"  Generated death animation")

        return self.build_geckolib_animation_json()

    def find_bone_vars(self):
        """Find bone variable to name mappings."""
        # Pattern: this.varName = new AdvancedModelBox(this, "boneName");
        pattern = r'this\.(\w+)\s*=\s*new\s+AdvancedModelBox\(this(?:,\s*"([^"]+)")?\)'
        matches = re.finditer(pattern, self.content)

        for match in matches:
            var_name = match.group(1)
            bone_name = match.group(2) if match.group(2) else var_name
            self.bone_vars[var_name] = bone_name

    def extract_idle_animation(self) -> Dict:
        """Extract or create idle animation."""
        bones = {}

        # Look for subtle idle movements (often in setAngles with very small values)
        # Check for bob() calls in idle context
        bob_pattern = r'this\.bob\(this\.(\w+),\s*([^,]+),\s*([^,]+),\s*(true|false)'
        for match in re.finditer(bob_pattern, self.content):
            var_name = match.group(1)
            if var_name in self.bone_vars:
                bone_name = self.bone_vars[var_name]
                try:
                    degree = float(eval(match.group(3)))
                    bones[bone_name] = {
                        "position": {
                            "0.0": [0, 0, 0],
                            "1.0": [0, degree * 0.5, 0],
                            "2.0": [0, 0, 0]
                        }
                    }
                except:
                    pass

        # If no idle movement found, create a static idle with slight breathing
        if not bones:
            # Look for body/torso bones
            body_bones = ["body", "torso", "chest"]
            for var_name, bone_name in self.bone_vars.items():
                if bone_name.lower() in body_bones:
                    bones[bone_name] = {
                        "position": {
                            "0.0": [0, 0, 0],
                            "1.0": [0, 0.5, 0],
                            "2.0": [0, 0, 0]
                        }
                    }
                    break

        return {
            "loop": True,
            "animation_length": 2.0,
            "bones": bones
        }

    def extract_walk_animation(self) -> Dict:
        """Recognize walk() helper pattern and convert to keyframes."""
        bones = {}

        # Pattern: this.walk(this.varName, speed, degree, invert, ...);
        walk_pattern = r'this\.walk\(this\.(\w+),\s*([^,]+),\s*([^,]+),\s*(true|false)'
        matches = re.finditer(walk_pattern, self.content)

        for match in matches:
            var_name = match.group(1)
            if var_name not in self.bone_vars:
                continue

            bone_name = self.bone_vars[var_name]
            try:
                speed = float(eval(match.group(2)))
                degree = float(eval(match.group(3)))
                inverted = match.group(4) == "true"

                # Convert to degrees if in radians
                if abs(degree) < 10:  # Likely radians
                    degree = degree * 57.2958

                # Create walking keyframes
                angle = degree if not inverted else -degree
                bones[bone_name] = {
                    "rotation": {
                        "0.0": [angle, 0, 0],
                        "0.5": [-angle, 0, 0],
                        "1.0": [angle, 0, 0]
                    }
                }
            except Exception as e:
                print(f"    Warning: Failed to parse walk for {var_name}: {e}")

        return {
            "loop": True,
            "animation_length": 1.0,
            "bones": bones
        }

    def extract_fly_animation(self) -> Dict:
        """Recognize flap() helper pattern and convert to keyframes."""
        bones = {}

        # Pattern: this.flap(this.varName, speed, degree, invert, ...);
        flap_pattern = r'this\.flap\(this\.(\w+),\s*([^,]+),\s*([^,]+),\s*(true|false)'
        matches = re.finditer(flap_pattern, self.content)

        for match in matches:
            var_name = match.group(1)
            if var_name not in self.bone_vars:
                continue

            bone_name = self.bone_vars[var_name]
            try:
                speed = float(eval(match.group(2)))
                degree = float(eval(match.group(3)))
                inverted = match.group(4) == "true"

                # Convert to degrees if in radians
                if abs(degree) < 10:  # Likely radians
                    degree = degree * 57.2958

                # Create flapping keyframes (typically Z-axis for wings)
                angle = degree if not inverted else -degree

                # Flap animation has 4 keyframes for smooth wing motion
                bones[bone_name] = {
                    "rotation": {
                        "0.0": [0, 0, angle],
                        "0.25": [0, 0, -angle],
                        "0.5": [0, 0, angle],
                        "0.75": [0, 0, -angle],
                        "1.0": [0, 0, angle]
                    }
                }
            except Exception as e:
                print(f"    Warning: Failed to parse flap for {var_name}: {e}")

        # Also check for swing() on tail/appendages during flight
        swing_pattern = r'this\.swing\(this\.(\w+),\s*([^,]+),\s*([^,]+),\s*(true|false)'
        for match in re.finditer(swing_pattern, self.content):
            var_name = match.group(1)
            if var_name not in self.bone_vars:
                continue

            bone_name = self.bone_vars[var_name]
            # Skip if already has animation
            if bone_name in bones:
                continue

            try:
                degree = float(eval(match.group(3)))
                if abs(degree) < 10:
                    degree = degree * 57.2958

                # Swing on Y-axis (left-right)
                bones[bone_name] = {
                    "rotation": {
                        "0.0": [0, -degree, 0],
                        "0.5": [0, degree, 0],
                        "1.0": [0, -degree, 0]
                    }
                }
            except:
                pass

        return {
            "loop": True,
            "animation_length": 1.0,
            "bones": bones
        }

    def create_default_death_animation(self) -> Dict:
        """Create a default death animation (similar to idle but slower)."""
        # Use idle animation as base, make it slower
        idle_anim = self.animations.get("idle", {})
        death_bones = {}

        # Copy idle bones but make movement more pronounced
        for bone_name, transforms in idle_anim.get("bones", {}).items():
            death_bones[bone_name] = transforms.copy()

        # If no bones, create generic death animation
        if not death_bones:
            # Look for body bone
            for var_name, bone_name in self.bone_vars.items():
                if bone_name.lower() in ["body", "torso", "chest"]:
                    death_bones[bone_name] = {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "1.0": [0, 0, 90],  # Roll over
                            "2.0": [0, 0, 90]
                        }
                    }
                    break

        return {
            "loop": False,
            "animation_length": 2.0,
            "bones": death_bones
        }

    def build_geckolib_animation_json(self) -> Dict:
        """Build GeckoLib animation JSON structure."""
        return {
            "format_version": "1.8.0",
            "animations": self.animations
        }

    def save(self, output_path: str):
        """Save parsed animations to JSON file."""
        anim_json = self.parse()

        output_file = Path(output_path)
        output_file.parent.mkdir(parents=True, exist_ok=True)

        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(anim_json, f, indent=2)

        print(f"✅ Saved animations to {output_file}")
        print(f"   {len(self.animations)} animations generated")


def main():
    parser = argparse.ArgumentParser(
        description='Convert Citadel procedural animations to GeckoLib keyframe JSON'
    )
    parser.add_argument('--input', required=True, help='Input Java file (decompiled Citadel model)')
    parser.add_argument('--output', required=True, help='Output JSON file (GeckoLib .animation.json)')

    args = parser.parse_args()

    converter = CitadelAnimationConverter(args.input)
    converter.save(args.output)


if __name__ == '__main__':
    main()
