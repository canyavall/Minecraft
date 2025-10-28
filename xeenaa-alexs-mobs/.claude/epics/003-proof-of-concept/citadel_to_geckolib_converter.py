#!/usr/bin/env python3
"""
Citadel to GeckoLib Model Converter

Converts decompiled Citadel model Java files to GeckoLib geo.json format.
Extracts bone hierarchy, cube geometry, and UV mapping from Alex's Mobs models.

Usage:
    python citadel_to_geckolib_converter.py ModelCockroach.java cockroach_citadel.geo.json
"""

import re
import json
import sys
from typing import Dict, List, Tuple, Optional


class CitadelModelParser:
    """Parses decompiled Citadel model Java files."""

    def __init__(self, java_content: str):
        self.content = java_content
        self.texture_width = 64
        self.texture_height = 64
        self.bones = {}  # name -> bone data
        self.bone_hierarchy = {}  # child -> parent
        self.root_bone = None

    def parse(self) -> Dict:
        """Parse the entire model and return structured data."""
        self._extract_texture_dimensions()
        self._extract_bones()
        return self._build_geckolib_structure()

    def _extract_texture_dimensions(self):
        """Extract texture width and height."""
        width_match = re.search(r'this\.texWidth\s*=\s*(\d+)', self.content)
        height_match = re.search(r'this\.texHeight\s*=\s*(\d+)', self.content)

        if width_match:
            self.texture_width = int(width_match.group(1))
        if height_match:
            self.texture_height = int(height_match.group(1))

    def _extract_bones(self):
        """Extract all bones with their positions and cubes."""
        # Find all bone definitions
        bone_pattern = r'this\.(\w+)\s*=\s*new AdvancedModelBox.*?"(\w+)"'
        for match in re.finditer(bone_pattern, self.content):
            var_name = match.group(1)
            bone_name = match.group(2)

            # Extract setPos for this bone
            pos_pattern = rf'this\.{var_name}\.setPos\(([^)]+)\)'
            pos_match = re.search(pos_pattern, self.content)

            if pos_match:
                pos_str = pos_match.group(1)
                pos_values = [float(x.strip().replace('f', '')) for x in pos_str.split(',')]
                pivot = pos_values
            else:
                pivot = [0, 0, 0]

            # Find parent relationship
            parent_pattern = rf'this\.(\w+)\.addChild.*?this\.{var_name}\)'
            parent_match = re.search(parent_pattern, self.content)

            parent = parent_match.group(1) if parent_match else None

            # Extract cubes for this bone
            cubes = self._extract_cubes(var_name)

            # Extract default rotation if any
            rotation = self._extract_rotation(var_name)

            self.bones[bone_name] = {
                'var_name': var_name,
                'pivot': pivot,
                'parent': parent,
                'cubes': cubes,
                'rotation': rotation
            }

            if parent:
                self.bone_hierarchy[bone_name] = parent
            else:
                self.root_bone = bone_name

    def _extract_cubes(self, var_name: str) -> List[Dict]:
        """Extract all cube definitions for a bone."""
        cubes = []

        # Pattern for .addBox() calls
        # .setTextureOffset(u, v).addBox(x, y, z, sizeX, sizeY, sizeZ, ...)
        cube_pattern = (
            rf'this\.{var_name}\.setTextureOffset\((\d+),\s*(\d+)\)'
            rf'\.addBox\(([^)]+)\)'
        )

        for match in re.finditer(cube_pattern, self.content):
            uv_x = int(match.group(1))
            uv_y = int(match.group(2))
            box_params = match.group(3)

            # Parse box parameters: x, y, z, sizeX, sizeY, sizeZ, inflate, mirror
            params = [x.strip().replace('f', '') for x in box_params.split(',')]

            if len(params) >= 6:
                origin_x = float(params[0])
                origin_y = float(params[1])
                origin_z = float(params[2])
                size_x = float(params[3])
                size_y = float(params[4])
                size_z = float(params[5])

                # Check for mirror flag
                mirror = False
                if len(params) >= 8:
                    mirror_str = params[7].lower()
                    mirror = mirror_str == 'true'

                cubes.append({
                    'origin': [origin_x, origin_y, origin_z],
                    'size': [size_x, size_y, size_z],
                    'uv': [uv_x, uv_y],
                    'mirror': mirror
                })

        return cubes

    def _extract_rotation(self, var_name: str) -> Optional[List[float]]:
        """Extract default rotation for a bone using setRotationAngle."""
        rotation_pattern = rf'this\.setRotationAngle\(this\.{var_name},\s*([^)]+)\)'
        match = re.search(rotation_pattern, self.content)

        if match:
            rotation_str = match.group(1)
            rotation_values = [float(x.strip().replace('f', '')) for x in rotation_str.split(',')]
            if len(rotation_values) == 3:
                # Convert radians to degrees
                return [
                    rotation_values[0] * (180.0 / 3.14159),
                    rotation_values[1] * (180.0 / 3.14159),
                    rotation_values[2] * (180.0 / 3.14159)
                ]

        return None

    def _calculate_absolute_pivot(self, bone_name: str) -> List[float]:
        """Calculate absolute pivot position by traversing parent chain."""
        bone = self.bones[bone_name]
        pivot = bone['pivot'].copy()

        # Traverse parent chain
        current_parent_var = bone['parent']
        while current_parent_var:
            # Find parent bone by var_name
            parent_bone_name = None
            for name, data in self.bones.items():
                if data['var_name'] == current_parent_var:
                    parent_bone_name = name
                    break

            if not parent_bone_name:
                break

            parent_bone = self.bones[parent_bone_name]
            parent_pivot = parent_bone['pivot']

            # Add parent's pivot to current pivot
            pivot[0] += parent_pivot[0]
            pivot[1] += parent_pivot[1]
            pivot[2] += parent_pivot[2]

            current_parent_var = parent_bone['parent']

        return pivot

    def _build_geckolib_structure(self) -> Dict:
        """Build the final GeckoLib JSON structure."""
        geckolib_bones = []

        # Process bones in order (root first, then children)
        processed = set()

        def process_bone(bone_name: str):
            if bone_name in processed:
                return

            bone_data = self.bones[bone_name]
            processed.add(bone_name)

            # Calculate absolute pivot
            absolute_pivot = self._calculate_absolute_pivot(bone_name)

            # Find parent bone name (not var name)
            parent_bone_name = None
            if bone_data['parent']:
                for name, data in self.bones.items():
                    if data['var_name'] == bone_data['parent']:
                        parent_bone_name = name
                        break

            # Build bone structure
            geckolib_bone = {
                'name': bone_name,
                'pivot': [
                    round(absolute_pivot[0], 2),
                    round(absolute_pivot[1], 2),
                    round(absolute_pivot[2], 2)
                ]
            }

            if parent_bone_name:
                geckolib_bone['parent'] = parent_bone_name

            # Add default rotation if present
            if bone_data['rotation']:
                geckolib_bone['rotation'] = [
                    round(bone_data['rotation'][0], 2),
                    round(bone_data['rotation'][1], 2),
                    round(bone_data['rotation'][2], 2)
                ]

            # Convert cubes
            if bone_data['cubes']:
                geckolib_cubes = []
                for cube in bone_data['cubes']:
                    # Calculate absolute origin
                    abs_origin = [
                        cube['origin'][0],
                        absolute_pivot[1] + cube['origin'][1],
                        cube['origin'][2]
                    ]

                    geckolib_cube = {
                        'origin': [
                            round(abs_origin[0], 2),
                            round(abs_origin[1], 2),
                            round(abs_origin[2], 2)
                        ],
                        'size': [
                            round(cube['size'][0], 2),
                            round(cube['size'][1], 2),
                            round(cube['size'][2], 2)
                        ],
                        'uv': cube['uv']
                    }

                    if cube['mirror']:
                        geckolib_cube['mirror'] = True

                    geckolib_cubes.append(geckolib_cube)

                geckolib_bone['cubes'] = geckolib_cubes

            geckolib_bones.append(geckolib_bone)

            # Process children
            for child_name, child_data in self.bones.items():
                if child_data['parent'] == bone_data['var_name']:
                    process_bone(child_name)

        # Start with root bone
        if self.root_bone:
            process_bone(self.root_bone)

        # Build final structure
        identifier = f"geometry.{self.root_bone.lower()}" if self.root_bone else "geometry.model"

        return {
            'format_version': '1.12.0',
            'minecraft:geometry': [{
                'description': {
                    'identifier': identifier,
                    'texture_width': self.texture_width,
                    'texture_height': self.texture_height,
                    'visible_bounds_width': 2,
                    'visible_bounds_height': 2,
                    'visible_bounds_offset': [0, 0.5, 0]
                },
                'bones': geckolib_bones
            }]
        }


def convert_model(input_java_file: str, output_json_file: str):
    """Convert a Citadel model Java file to GeckoLib JSON."""
    # Read Java file
    with open(input_java_file, 'r', encoding='utf-8') as f:
        java_content = f.read()

    # Parse and convert
    parser = CitadelModelParser(java_content)
    geckolib_data = parser.parse()

    # Write JSON file
    with open(output_json_file, 'w', encoding='utf-8') as f:
        json.dump(geckolib_data, f, indent=2)

    print(f"✅ Converted {input_java_file} → {output_json_file}")
    print(f"   Texture: {parser.texture_width}x{parser.texture_height}")
    print(f"   Bones: {len(parser.bones)}")


if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: python citadel_to_geckolib_converter.py <input.java> <output.geo.json>")
        sys.exit(1)

    input_file = sys.argv[1]
    output_file = sys.argv[2]

    convert_model(input_file, output_file)
