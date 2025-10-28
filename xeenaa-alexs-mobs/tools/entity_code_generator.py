#!/usr/bin/env python3
"""
EntityCodeGenerator - Generate boilerplate entity class with GeckoLib integration.

Usage:
    python entity_code_generator.py --config configs/fly.json --output src/main/java/.../entity/animal/FlyEntity.java
"""

import json
import argparse
from pathlib import Path
from typing import Dict


class EntityCodeGenerator:
    """Generate entity class from configuration."""

    def __init__(self, config_path: str):
        with open(config_path, 'r', encoding='utf-8') as f:
            self.config = json.load(f)

    def generate(self) -> str:
        """Generate complete entity class code."""
        name = self.config["name"]
        category = self.config.get("category", "animal")
        mob_type = self.config["type"]

        # Select base class
        base_class = self.get_base_class(mob_type)

        # Generate code sections
        package = f"com.canya.xeenaa_alexs_mobs.entity.{category}"
        imports = self.generate_imports(base_class, mob_type)
        attributes = self.generate_attributes()
        goals = self.generate_goals(mob_type)
        animation_logic = self.generate_animation_logic(mob_type)

        return self.build_entity_class(
            package=package,
            name=name,
            base_class=base_class,
            imports=imports,
            attributes=attributes,
            goals=goals,
            animation_logic=animation_logic
        )

    def get_base_class(self, mob_type: str) -> str:
        """Get base entity class based on mob type."""
        base_classes = {
            "flying": "FlyingAnimalEntity",
            "swimming": "FishEntity",
            "walking": "AnimalEntity",
            "hostile": "HostileEntity",
            "passive": "PassiveEntity"
        }
        return base_classes.get(mob_type, "AnimalEntity")

    def generate_imports(self, base_class: str, mob_type: str) -> str:
        """Generate import statements."""
        imports = [
            "import net.minecraft.entity.EntityType;",
            "import net.minecraft.entity.attribute.DefaultAttributeContainer;",
            "import net.minecraft.entity.attribute.EntityAttributes;",
        ]

        # Add base class import
        if base_class == "FlyingAnimalEntity":
            imports.append("import net.minecraft.entity.passive.FlyingAnimalEntity;")
        elif base_class == "FishEntity":
            imports.append("import net.minecraft.entity.passive.FishEntity;")
        elif base_class == "AnimalEntity":
            imports.append("import net.minecraft.entity.passive.AnimalEntity;")
        elif base_class == "HostileEntity":
            imports.append("import net.minecraft.entity.mob.HostileEntity;")
        elif base_class == "PassiveEntity":
            imports.append("import net.minecraft.entity.passive.PassiveEntity;")

        # Add AI goal imports
        imports.extend([
            "import net.minecraft.entity.ai.goal.*;",
            "import net.minecraft.world.World;",
            "import software.bernie.geckolib.animatable.GeoEntity;",
            "import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;",
            "import software.bernie.geckolib.animation.*;",
            "import software.bernie.geckolib.util.GeckoLibUtil;"
        ])

        return "\n".join(imports)

    def generate_attributes(self) -> str:
        """Generate attribute configuration."""
        attrs = self.config.get("attributes", {})
        health = attrs.get("health", 10.0)
        speed = attrs.get("speed", 0.25)
        follow_range = attrs.get("follow_range", 16.0)
        flying_speed = attrs.get("flying_speed", None)
        attack_damage = attrs.get("attack_damage", None)

        lines = [
            f"        .add(EntityAttributes.GENERIC_MAX_HEALTH, {health}D)",
            f"        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, {speed}D)"
        ]

        if flying_speed is not None:
            lines.append(f"        .add(EntityAttributes.GENERIC_FLYING_SPEED, {flying_speed}D)")

        if attack_damage is not None:
            lines.append(f"        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, {attack_damage}D)")

        lines.append(f"        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, {follow_range}D);")

        return "\n".join(lines)

    def generate_goals(self, mob_type: str) -> str:
        """Generate AI goal initialization."""
        custom_goals = self.config.get("ai_goals", [])

        if custom_goals:
            # Use custom goals from config
            goals = []
            priority = 0
            for goal in custom_goals:
                goals.append(f"        this.goalSelector.add({priority}, new {goal}(this));")
                priority += 1
            return "\n".join(goals)

        # Generate default goals based on type
        if mob_type == "flying":
            return """        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, net.minecraft.entity.player.PlayerEntity.class, 6.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));"""

        elif mob_type == "swimming":
            return """        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, net.minecraft.entity.player.PlayerEntity.class, 8.0F, 1.6D, 1.4D));
        this.goalSelector.add(4, new SwimAroundGoal(this, 1.0D, 10));"""

        elif mob_type == "hostile":
            return """        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, net.minecraft.entity.player.PlayerEntity.class, 8.0F));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, net.minecraft.entity.player.PlayerEntity.class, true));"""

        else:  # walking/passive
            return """        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, net.minecraft.entity.player.PlayerEntity.class, 6.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));"""

    def generate_animation_logic(self, mob_type: str) -> str:
        """Generate animation controller predicate logic."""
        if mob_type == "flying":
            return """        if (this.isOnGround()) {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
        }"""

        elif mob_type == "swimming":
            return """        if (this.isSubmergedInWater()) {
            state.getController().setAnimation(RawAnimation.begin().then("swim", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }"""

        else:  # walking/passive/hostile
            return """        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }"""

    def build_entity_class(self, package: str, name: str, base_class: str,
                           imports: str, attributes: str, goals: str,
                           animation_logic: str) -> str:
        """Build complete entity class from template."""
        return f"""package {package};

{imports}

/**
 * {name} entity with GeckoLib animation support.
 * Generated by EntityCodeGenerator.
 */
public class {name}Entity extends {base_class} implements GeoEntity {{
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public {name}Entity(EntityType<? extends {base_class}> entityType, World world) {{
        super(entityType, world);
    }}

    /**
     * Define entity attributes (health, speed, etc.)
     */
    public static DefaultAttributeContainer.Builder createAttributes() {{
        return {base_class}.createMobAttributes()
{attributes}
    }}

    /**
     * Initialize AI goals for entity behavior.
     */
    @Override
    protected void initGoals() {{
{goals}
    }}

    /**
     * Register GeckoLib animation controllers.
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {{
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }}

    /**
     * Animation controller predicate - determines which animation to play.
     */
    private PlayState predicate(AnimationState<{name}Entity> state) {{
{animation_logic}
        return PlayState.CONTINUE;
    }}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {{
        return cache;
    }}
}}
"""

    def save(self, output_path: str):
        """Generate and save entity class to file."""
        code = self.generate()

        output_file = Path(output_path)
        output_file.parent.mkdir(parents=True, exist_ok=True)

        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(code)

        print(f"✅ Generated entity class: {output_file}")
        print(f"   Type: {self.config['type']}")
        print(f"   Base class: {self.get_base_class(self.config['type'])}")


def main():
    parser = argparse.ArgumentParser(
        description='Generate entity class from configuration'
    )
    parser.add_argument('--config', required=True, help='Input JSON config file')
    parser.add_argument('--output', required=True, help='Output Java file path')

    args = parser.parse_args()

    generator = EntityCodeGenerator(args.config)
    generator.save(args.output)


if __name__ == '__main__':
    main()
