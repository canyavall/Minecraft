#!/usr/bin/env python3
"""
CitadelModelParser - Parse decompiled Citadel model Java code to extract bone hierarchy and geometry.

Usage:
    python citadel_model_parser.py --input decompiled/ModelFly.java --output geo/fly_citadel.geo.json
"""

import re
import json
import argparse
from pathlib import Path
from typing import Dict, List, Tuple, Optional


class CitadelModelParser:
    """Parse Citadel model Java code and convert to GeckoLib geometry JSON."""

    def __init__(self, java_file_path: str):
        self.java_file_path = Path(java_file_path)
        with open(java_file_path, 'r', encoding='utf-8') as f:
            self.content = f.read()

        self.bones = []
        self.bone_vars = {}  # Map variable name to bone name
        self.texture_width = 64
        self.texture_height = 32

    def parse(self) -> Dict:
        """Parse Citadel model to extract bone structure."""
        print(f"Parsing {self.java_file_path.name}...")

        # Extract texture dimensions
        self.texture_width, self.texture_height = self.extract_texture_size()
        print(f"  Texture dimensions: {self.texture_width}x{self.texture_height}")

        # Find all AdvancedModelBox instantiations
        bone_definitions = self.find_bone_definitions()
        print(f"  Found {len(bone_definitions)} bones")

        # Parse each bone
        for var_name, bone_name in bone_definitions:
            bone = self.parse_bone(var_name, bone_name)
            if bone:
                self.bones.append(bone)

        # Build GeckoLib geometry JSON
        return self.build_geckolib_geo_json()

    def extract_texture_size(self) -> Tuple[int, int]:
        """Extract texture dimensions from constructor."""
        # Look for: super(pTexWidth, pTexHeight)
        pattern = r'super\((\d+),\s*(\d+)\)'
        match = re.search(pattern, self.content)
        if match:
            return int(match.group(1)), int(match.group(2))

        # Look for: textureWidth = X; textureHeight = Y;
        width_match = re.search(r'textureWidth\s*=\s*(\d+)', self.content)
        height_match = re.search(r'textureHeight\s*=\s*(\d+)', self.content)
        if width_match and height_match:
            return int(width_match.group(1)), int(height_match.group(1))

        # Default
        return 64, 32

    def find_bone_definitions(self) -> List[Tuple[str, str]]:
        """Find all AdvancedModelBox instantiations."""
        bones = []

        # Pattern: this.varName = new AdvancedModelBox(this, "boneName");
        pattern = r'this\.(\w+)\s*=\s*new\s+AdvancedModelBox\(this(?:,\s*"([^"]+)")?\)'
        matches = re.finditer(pattern, self.content)

        for match in matches:
            var_name = match.group(1)
            bone_name = match.group(2) if match.group(2) else var_name
            bones.append((var_name, bone_name))
            self.bone_vars[var_name] = bone_name

        # Also look for: this.varName = new AdvancedModelBox(this);
        # Then find: this.varName.setModelRendererName("boneName");
        pattern2 = r'this\.(\w+)\.setModelRendererName\("([^"]+)"\)'
        for match in re.finditer(pattern2, self.content):
            var_name = match.group(1)
            bone_name = match.group(2)
            if var_name not in self.bone_vars:
                bones.append((var_name, bone_name))
                self.bone_vars[var_name] = bone_name

        return bones

    def parse_bone(self, var_name: str, bone_name: str) -> Optional[Dict]:
        """Parse a single bone's properties."""
        bone = {
            "name": bone_name,
            "pivot": [0, 0, 0],
            "rotation": [0, 0, 0],
            "cubes": []
        }

        # Extract pivot point (setRotationPoint)
        bone["pivot"] = self.extract_pivot(var_name)

        # Extract rotation (rotateAngleX/Y/Z)
        bone["rotation"] = self.extract_rotation(var_name)

        # Extract cubes (addBox calls)
        bone["cubes"] = self.extract_cubes(var_name)

        # Extract parent relationship
        parent = self.extract_parent(var_name)
        if parent:
            bone["parent"] = parent

        return bone

    def extract_pivot(self, var_name: str) -> List[float]:
        """Extract setRotationPoint calls."""
        # Pattern: this.varName.setRotationPoint(x, y, z);
        pattern = rf'this\.{var_name}\.setRotationPoint\(([^,]+),\s*([^,]+),\s*([^\)]+)\)'
        match = re.search(pattern, self.content)
        if match:
            try:
                x = float(eval(match.group(1)))  # eval to handle expressions like -1.0F
                y = float(eval(match.group(2)))
                z = float(eval(match.group(3)))
                return [x, y, z]
            except:
                pass

        return [0, 0, 0]

    def extract_rotation(self, var_name: str) -> List[float]:
        """Extract initial rotation angles."""
        rotation = [0, 0, 0]

        # Look for: this.varName.rotateAngleX = value;
        patterns = [
            (rf'this\.{var_name}\.rotateAngleX\s*=\s*([^;]+)', 0),
            (rf'this\.{var_name}\.rotateAngleY\s*=\s*([^;]+)', 1),
            (rf'this\.{var_name}\.rotateAngleZ\s*=\s*([^;]+)', 2),
        ]

        for pattern, index in patterns:
            match = re.search(pattern, self.content)
            if match:
                try:
                    # Convert radians to degrees
                    value_str = match.group(1).strip()
                    value = float(eval(value_str))
                    rotation[index] = round(value * 57.2958, 2)  # radians to degrees
                except:
                    pass

        return rotation

    def extract_cubes(self, var_name: str) -> List[Dict]:
        """Extract addBox/setTextureOffset calls."""
        cubes = []

        # Find all addBox calls for this bone
        # Pattern: this.varName.addBox(x, y, z, width, height, depth);
        addbox_pattern = rf'this\.{var_name}\.addBox\(([^,]+),\s*([^,]+),\s*([^,]+),\s*([^,]+),\s*([^,]+),\s*([^\),]+)(?:,\s*[^\)]+)?\)'

        # Find texture offset for this bone
        texture_offset = [0, 0]
        texture_pattern = rf'this\.{var_name}\.setTextureOffset\(([^,]+),\s*([^\)]+)\)'
        texture_match = re.search(texture_pattern, self.content)
        if texture_match:
            try:
                texture_offset = [
                    int(eval(texture_match.group(1))),
                    int(eval(texture_match.group(2)))
                ]
            except:
                pass

        # Parse all addBox calls
        for match in re.finditer(addbox_pattern, self.content):
            try:
                x = float(eval(match.group(1)))
                y = float(eval(match.group(2)))
                z = float(eval(match.group(3)))
                width = float(eval(match.group(4)))
                height = float(eval(match.group(5)))
                depth = float(eval(match.group(6)))

                cube = {
                    "origin": [x, y, z],
                    "size": [width, height, depth],
                    "uv": texture_offset.copy()
                }
                cubes.append(cube)
            except Exception as e:
                print(f"    Warning: Failed to parse addBox for {var_name}: {e}")

        return cubes

    def extract_parent(self, var_name: str) -> Optional[str]:
        """Find parent bone via addChild relationship."""
        # Pattern: this.parentVar.addChild(this.varName);
        pattern = rf'this\.(\w+)\.addChild\(this\.{var_name}\)'
        match = re.search(pattern, self.content)
        if match:
            parent_var = match.group(1)
            return self.bone_vars.get(parent_var, parent_var)

        return None

    def build_geckolib_geo_json(self) -> Dict:
        """Build GeckoLib geometry JSON structure."""
        # Determine model identifier from class name
        class_name_match = re.search(r'class\s+Model(\w+)', self.content)
        identifier = class_name_match.group(1).lower() if class_name_match else "unknown"

        # Convert bones to GeckoLib format
        geckolib_bones = []
        for bone in self.bones:
            geckolib_bone = {
                "name": bone["name"],
                "pivot": bone["pivot"]
            }

            # Add parent if exists
            if "parent" in bone:
                geckolib_bone["parent"] = bone["parent"]

            # Add rotation if non-zero
            if bone["rotation"] != [0, 0, 0]:
                geckolib_bone["rotation"] = bone["rotation"]

            # Add cubes if exist
            if bone["cubes"]:
                geckolib_bone["cubes"] = bone["cubes"]

            geckolib_bones.append(geckolib_bone)

        return {
            "format_version": "1.12.0",
            "minecraft:geometry": [{
                "description": {
                    "identifier": f"geometry.{identifier}",
                    "texture_width": self.texture_width,
                    "texture_height": self.texture_height
                },
                "bones": geckolib_bones
            }]
        }

    def save(self, output_path: str):
        """Save parsed geometry to JSON file."""
        geo_json = self.parse()

        output_file = Path(output_path)
        output_file.parent.mkdir(parents=True, exist_ok=True)

        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(geo_json, f, indent=2)

        print(f"✅ Saved geometry to {output_file}")
        print(f"   {len(self.bones)} bones, {self.texture_width}x{self.texture_height} texture")


def main():
    parser = argparse.ArgumentParser(
        description='Parse Citadel model Java code to GeckoLib geometry JSON'
    )
    parser.add_argument('--input', required=True, help='Input Java file (decompiled Citadel model)')
    parser.add_argument('--output', required=True, help='Output JSON file (GeckoLib .geo.json)')

    args = parser.parse_args()

    citadel_parser = CitadelModelParser(args.input)
    citadel_parser.save(args.output)


if __name__ == '__main__':
    main()
