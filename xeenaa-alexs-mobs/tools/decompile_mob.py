#!/usr/bin/env python3
"""
Simple decompiler script using CFR (if available) or fallback to extracting from JAR.

Usage:
    python decompile_mob.py --mob Cockroach --jar .claude/epics/02-mob-catalog-asset-inventory/alexsmobs-1.22.9.jar
"""

import zipfile
import argparse
import subprocess
import os
from pathlib import Path


def extract_class_from_jar(jar_path: str, mob_name: str, output_dir: str):
    """Extract .class files from JAR for a specific mob."""
    output_path = Path(output_dir) / mob_name
    output_path.mkdir(parents=True, exist_ok=True)

    class_files = [
        f"com/github/alexthe666/alexsmobs/entity/Entity{mob_name}.class",
        f"com/github/alexthe666/alexsmobs/client/model/Model{mob_name}.class",
        f"com/github/alexthe666/alexsmobs/client/render/Render{mob_name}.class"
    ]

    with zipfile.ZipFile(jar_path, 'r') as jar:
        for class_file in class_files:
            try:
                jar.extract(class_file, output_path)
                print(f"✅ Extracted: {class_file}")
            except KeyError:
                print(f"⚠️  Not found: {class_file}")

    print(f"\n📁 Extracted to: {output_path}")
    print(f"\nℹ️  Note: You'll need to manually decompile these .class files")
    print(f"    Suggested tools:")
    print(f"    - IntelliJ IDEA (View > Show Bytecode, then decompile)")
    print(f"    - JD-GUI (http://java-decompiler.github.io/)")
    print(f"    - Online: http://www.javadecompilers.com/")


def main():
    parser = argparse.ArgumentParser(
        description='Extract class files from Alex\'s Mobs JAR for decompilation'
    )
    parser.add_argument('--mob', required=True, help='Mob name (e.g., Cockroach)')
    parser.add_argument('--jar', required=True, help='Path to Alex\'s Mobs JAR')
    parser.add_argument('--output', default='.claude/temp/decompiled', help='Output directory')

    args = parser.parse_args()

    extract_class_from_jar(args.jar, args.mob, args.output)


if __name__ == '__main__':
    main()
