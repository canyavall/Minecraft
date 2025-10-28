#!/usr/bin/env python3
"""
AssetCopyScript - Batch copy textures, sounds, loot tables from original mod JAR.

Usage:
    python asset_copy_script.py --mob Fly --source alexsmobs-1.22.9.jar --dest src/main/resources/assets/xeenaa-alexs-mobs/
"""

import zipfile
import shutil
import argparse
from pathlib import Path
import os


class AssetCopyScript:
    """Copy assets from Alex's Mobs JAR to new mod structure."""

    def __init__(self, source_jar: str, destination_dir: str):
        self.source_jar = Path(source_jar)
        self.destination_dir = Path(destination_dir)

        if not self.source_jar.exists():
            raise FileNotFoundError(f"Source JAR not found: {source_jar}")

    def copy_mob_assets(self, mob_name: str):
        """Copy all assets for a specific mob."""
        mob_lower = mob_name.lower()

        print(f"Copying assets for {mob_name} from {self.source_jar.name}...")

        with zipfile.ZipFile(self.source_jar, 'r') as jar:
            # Copy textures
            textures_copied = self.copy_textures(jar, mob_lower)

            # Copy sounds
            sounds_copied = self.copy_sounds(jar, mob_lower)

            # Copy loot table
            loot_copied = self.copy_loot_table(jar, mob_lower)

        print(f"✅ Asset copy complete:")
        print(f"   Textures: {textures_copied} files")
        print(f"   Sounds: {sounds_copied} files")
        print(f"   Loot tables: {loot_copied} files")

    def copy_textures(self, jar: zipfile.ZipFile, mob_name: str) -> int:
        """Copy texture files for mob."""
        count = 0
        texture_pattern = f"assets/alexsmobs/textures/entity/{mob_name}"

        for file_info in jar.filelist:
            if texture_pattern in file_info.filename and file_info.filename.endswith('.png'):
                # Extract relative path after "textures/entity/"
                relative_path = file_info.filename.split("textures/entity/", 1)[1]

                # Destination path in new mod
                dest_path = self.destination_dir / "textures" / "entity" / relative_path
                dest_path.parent.mkdir(parents=True, exist_ok=True)

                # Extract file
                with jar.open(file_info) as source:
                    with open(dest_path, 'wb') as dest:
                        shutil.copyfileobj(source, dest)

                print(f"   Copied texture: {relative_path}")
                count += 1

        return count

    def copy_sounds(self, jar: zipfile.ZipFile, mob_name: str) -> int:
        """Copy sound files for mob."""
        count = 0

        # Sound files might be in different locations
        sound_patterns = [
            f"assets/alexsmobs/sounds/mob/{mob_name}",
            f"assets/alexsmobs/sounds/entity/{mob_name}",
            f"assets/alexsmobs/sounds/{mob_name}"
        ]

        for pattern in sound_patterns:
            for file_info in jar.filelist:
                if pattern in file_info.filename and file_info.filename.endswith('.ogg'):
                    # Extract relative path after "sounds/"
                    if "sounds/" in file_info.filename:
                        relative_path = file_info.filename.split("sounds/", 1)[1]

                        # Destination path in new mod
                        dest_path = self.destination_dir / "sounds" / relative_path
                        dest_path.parent.mkdir(parents=True, exist_ok=True)

                        # Extract file
                        with jar.open(file_info) as source:
                            with open(dest_path, 'wb') as dest:
                                shutil.copyfileobj(source, dest)

                        print(f"   Copied sound: {relative_path}")
                        count += 1

        return count

    def copy_loot_table(self, jar: zipfile.ZipFile, mob_name: str) -> int:
        """Copy loot table JSON for mob."""
        count = 0
        loot_pattern = f"data/alexsmobs/loot_tables/entities/{mob_name}.json"

        for file_info in jar.filelist:
            if file_info.filename == loot_pattern:
                # Destination in new mod data folder
                dest_path = Path("src/main/resources/data/xeenaa-alexs-mobs/loot_tables/entities") / f"{mob_name}.json"
                dest_path.parent.mkdir(parents=True, exist_ok=True)

                # Extract file
                with jar.open(file_info) as source:
                    with open(dest_path, 'wb') as dest:
                        shutil.copyfileobj(source, dest)

                print(f"   Copied loot table: {mob_name}.json")
                count += 1

        return count

    def list_available_mobs(self) -> list:
        """List all mob names found in JAR textures."""
        mobs = set()

        with zipfile.ZipFile(self.source_jar, 'r') as jar:
            for file_info in jar.filelist:
                if "textures/entity/" in file_info.filename and file_info.filename.endswith('.png'):
                    # Extract mob name from path
                    parts = file_info.filename.split("textures/entity/")[1].split('/')
                    if parts:
                        mobs.add(parts[0])

        return sorted(list(mobs))

    def copy_all_mobs(self, mob_list: list):
        """Copy assets for multiple mobs."""
        total_textures = 0
        total_sounds = 0
        total_loot = 0

        for mob in mob_list:
            print(f"\n{'='*60}")
            self.copy_mob_assets(mob)

        print(f"\n{'='*60}")
        print(f"Total assets copied for {len(mob_list)} mobs")


def main():
    parser = argparse.ArgumentParser(
        description='Copy assets from Alex\'s Mobs JAR to new mod'
    )
    parser.add_argument('--source', required=True, help='Path to Alex\'s Mobs JAR file')
    parser.add_argument('--dest', default='src/main/resources/assets/xeenaa-alexs-mobs',
                        help='Destination directory for assets')
    parser.add_argument('--mob', help='Mob name to copy (e.g., fly, cockroach)')
    parser.add_argument('--list', action='store_true', help='List all available mobs in JAR')
    parser.add_argument('--all', action='store_true', help='Copy assets for all mobs')

    args = parser.parse_args()

    copier = AssetCopyScript(args.source, args.dest)

    if args.list:
        print("Available mobs in JAR:")
        mobs = copier.list_available_mobs()
        for mob in mobs:
            print(f"  - {mob}")
        print(f"\nTotal: {len(mobs)} mobs")

    elif args.all:
        mobs = copier.list_available_mobs()
        print(f"Copying assets for all {len(mobs)} mobs...")
        copier.copy_all_mobs(mobs)

    elif args.mob:
        copier.copy_mob_assets(args.mob)

    else:
        parser.print_help()


if __name__ == '__main__':
    main()
