#!/usr/bin/env python3
"""
Batch Convert - Main automation pipeline for converting multiple mobs.

Usage:
    # Convert single mob
    python batch_convert.py --mob fly

    # Convert multiple mobs
    python batch_convert.py --mobs fly,cockroach,triops

    # Convert all mobs (requires configs for all)
    python batch_convert.py --all
"""

import argparse
import subprocess
import sys
from pathlib import Path
import json


class BatchConverter:
    """Main automation pipeline for mob conversion."""

    def __init__(self, config_dir="configs", decompiled_dir=".claude/temp/decompiled"):
        self.config_dir = Path(config_dir)
        self.decompiled_dir = Path(decompiled_dir)
        self.tools_dir = Path(__file__).parent

    def convert_mob(self, mob_name: str) -> bool:
        """Convert a single mob using the full pipeline."""
        print(f"\n{'='*80}")
        print(f"CONVERTING {mob_name.upper()}")
        print(f"{'='*80}\n")

        mob_lower = mob_name.lower()
        config_file = self.config_dir / f"{mob_lower}.json"

        if not config_file.exists():
            print(f"❌ ERROR: Config file not found: {config_file}")
            print(f"   Create {config_file} first!")
            return False

        try:
            # Step 1: Parse model geometry
            print(f"\n[1/7] Parsing model geometry...")
            success = self.run_tool(
                "citadel_model_parser.py",
                f"--input {self.decompiled_dir}/Model{mob_name}.java "
                f"--output src/main/resources/assets/xeenaa-alexs-mobs/geo/{mob_lower}_citadel.geo.json"
            )
            if not success:
                print(f"⚠️  Warning: Model parsing failed, continuing anyway...")

            # Step 2: Convert animations
            print(f"\n[2/7] Converting animations...")
            success = self.run_tool(
                "citadel_animation_converter.py",
                f"--input {self.decompiled_dir}/Model{mob_name}.java "
                f"--output src/main/resources/assets/xeenaa-alexs-mobs/animations/{mob_lower}_citadel.animation.json"
            )
            if not success:
                print(f"⚠️  Warning: Animation conversion failed, continuing anyway...")

            # Step 3: Generate entity code
            print(f"\n[3/7] Generating entity class...")
            success = self.run_tool(
                "entity_code_generator.py",
                f"--config {config_file} "
                f"--output src/main/java/com/canya/xeenaa_alexs_mobs/entity/animal/{mob_name}Entity.java"
            )
            if not success:
                print(f"❌ Entity generation failed!")
                return False

            # Step 4: Generate model and renderer
            print(f"\n[4/7] Generating model and renderer classes...")
            success = self.run_tool(
                "model_renderer_generator.py",
                f"--config {config_file} "
                f"--output src/main/java/com/canya/xeenaa_alexs_mobs"
            )
            if not success:
                print(f"❌ Model/renderer generation failed!")
                return False

            # Step 5: Copy assets (textures, sounds, loot tables)
            print(f"\n[5/7] Copying assets...")
            # Note: This requires Alex's Mobs JAR - skip if not available
            jar_path = Path("alexsmobs-1.22.9.jar")
            if jar_path.exists():
                success = self.run_tool(
                    "asset_copy_script.py",
                    f"--source {jar_path} --mob {mob_lower}"
                )
                if not success:
                    print(f"⚠️  Warning: Asset copy failed, continuing anyway...")
            else:
                print(f"⚠️  Skipping asset copy (alexsmobs-1.22.9.jar not found)")

            # Step 6: Generate registration code
            print(f"\n[6/7] Generating registration code...")
            success = self.run_tool(
                "registration_code_generator.py",
                f"--config {config_file} "
                f"--output .claude/temp/{mob_lower}_registration.txt "
                f"--create-spawn-egg-model"
            )
            if not success:
                print(f"⚠️  Warning: Registration code generation failed...")

            # Step 7: Summary
            print(f"\n[7/7] Conversion complete!")
            self.print_next_steps(mob_name, mob_lower)

            return True

        except Exception as e:
            print(f"\n❌ ERROR: Conversion failed with exception: {e}")
            import traceback
            traceback.print_exc()
            return False

    def run_tool(self, tool_name: str, args: str) -> bool:
        """Run a Python tool script."""
        tool_path = self.tools_dir / tool_name
        cmd = f"python \"{tool_path}\" {args}"

        try:
            result = subprocess.run(
                cmd,
                shell=True,
                check=True,
                capture_output=False,
                text=True
            )
            return True
        except subprocess.CalledProcessError as e:
            print(f"   Tool failed with exit code {e.returncode}")
            return False

    def print_next_steps(self, mob_name: str, mob_lower: str):
        """Print manual steps required after automation."""
        print(f"\n{'='*80}")
        print(f"NEXT STEPS (MANUAL)")
        print(f"{'='*80}\n")

        print(f"1. Review generated files:")
        print(f"   - src/main/java/.../entity/animal/{mob_name}Entity.java")
        print(f"   - src/main/java/.../client/model/{mob_name}Model.java")
        print(f"   - src/main/java/.../client/renderer/{mob_name}Renderer.java")
        print(f"   - src/main/resources/assets/.../geo/{mob_lower}_citadel.geo.json")
        print(f"   - src/main/resources/assets/.../animations/{mob_lower}_citadel.animation.json")

        print(f"\n2. Add registration code from:")
        print(f"   .claude/temp/{mob_lower}_registration.txt")

        print(f"\n3. Build and test:")
        print(f"   ./gradlew build")
        print(f"   ./gradlew runClient")

        print(f"\n4. Test in-game:")
        print(f"   - Spawn {mob_name} with spawn egg")
        print(f"   - Verify visual quality")
        print(f"   - Check animations (idle, walk/fly, death)")
        print(f"   - Test AI behavior")
        print(f"   - Tune speed if needed (in {mob_name}Entity.java)")

        print(f"\n5. Fix any issues:")
        print(f"   - Animation name mismatches")
        print(f"   - Texture paths")
        print(f"   - Speed tuning")

    def convert_multiple(self, mob_list: list):
        """Convert multiple mobs in sequence."""
        results = {}

        for mob in mob_list:
            success = self.convert_mob(mob)
            results[mob] = success

        # Print summary
        print(f"\n\n{'='*80}")
        print(f"BATCH CONVERSION SUMMARY")
        print(f"{'='*80}\n")

        successful = [mob for mob, success in results.items() if success]
        failed = [mob for mob, success in results.items() if not success]

        print(f"✅ Successful: {len(successful)}/{len(mob_list)}")
        for mob in successful:
            print(f"   - {mob}")

        if failed:
            print(f"\n❌ Failed: {len(failed)}/{len(mob_list)}")
            for mob in failed:
                print(f"   - {mob}")

    def list_configs(self):
        """List all available mob configs."""
        configs = list(self.config_dir.glob("*.json"))
        print(f"Available mob configs: {len(configs)}")
        for config in sorted(configs):
            mob_name = config.stem
            print(f"  - {mob_name}")


def main():
    parser = argparse.ArgumentParser(
        description='Batch convert Citadel mobs to GeckoLib'
    )
    parser.add_argument('--mob', help='Single mob name to convert (e.g., fly)')
    parser.add_argument('--mobs', help='Comma-separated list of mobs (e.g., fly,cockroach,triops)')
    parser.add_argument('--all', action='store_true', help='Convert all mobs with configs')
    parser.add_argument('--list', action='store_true', help='List available mob configs')
    parser.add_argument('--config-dir', default='configs', help='Directory containing mob configs')

    args = parser.parse_args()

    converter = BatchConverter(config_dir=args.config_dir)

    if args.list:
        converter.list_configs()

    elif args.mob:
        converter.convert_mob(args.mob)

    elif args.mobs:
        mob_list = [m.strip() for m in args.mobs.split(',')]
        converter.convert_multiple(mob_list)

    elif args.all:
        configs = list(Path(args.config_dir).glob("*.json"))
        mob_list = [config.stem for config in configs]
        print(f"Converting all {len(mob_list)} mobs...")
        converter.convert_multiple(mob_list)

    else:
        parser.print_help()


if __name__ == '__main__':
    main()
