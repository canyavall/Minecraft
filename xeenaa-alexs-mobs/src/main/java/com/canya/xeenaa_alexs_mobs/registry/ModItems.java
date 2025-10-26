package com.canya.xeenaa_alexs_mobs.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Registry for all custom items in Alex's Mobs port.
 *
 * <p>This class provides a centralized registration system for items using Fabric patterns.
 * All items should be registered here using the {@link #register(String, Item)} method.
 *
 * <p>Items are organized into a custom creative mode tab (item group) for easy access.
 * The item group is automatically populated with all spawn eggs and other mod items.
 *
 * <p><b>Usage Example:</b>
 * <pre>{@code
 * public static final Item ALLIGATOR_SPAWN_EGG = register(
 *     "alligator_spawn_egg",
 *     new SpawnEggItem(ModEntities.ALLIGATOR, 0x2D5016, 0x1A3009,
 *         new Item.Settings())
 * );
 * }</pre>
 *
 * <p><b>Creative Tab:</b> All items appear in the "Xeenaa's Alex's Mobs" creative tab.
 *
 * @author Canya
 * @see ModEntities
 * @see net.minecraft.item.SpawnEggItem
 */
public class ModItems {

    // ============================================
    // Item Group (Creative Tab)
    // ============================================

    /**
     * Custom creative mode item group for all Alex's Mobs items.
     *
     * <p>This item group appears in the creative inventory menu and contains
     * all items added by this mod, including spawn eggs, food items, and
     * equipment.
     *
     * <p><b>Registry Name:</b> {@code xeenaa-alexs-mobs:main}
     *
     * <p><b>Display Name:</b> "Xeenaa's Alex's Mobs" (from en_us.json)
     *
     * <p><b>Icon:</b> Test Animal Spawn Egg (will be changed to a more
     * representative item in future updates)
     */
    public static final RegistryKey<ItemGroup> ALEXS_MOBS_GROUP = RegistryKey.of(
        RegistryKeys.ITEM_GROUP,
        Identifier.of("xeenaa-alexs-mobs", "main")
    );

    // ============================================
    // Spawn Eggs
    // ============================================

    /**
     * Spawn egg for the Test Animal entity.
     *
     * <p>This spawn egg can be used to spawn Test Animal entities in the world.
     * It serves as a proof-of-concept for the spawn egg registration system.
     *
     * <p><b>Colors:</b>
     * <ul>
     *   <li>Primary (base): {@code 0x76AF50} (green, matching entity texture)</li>
     *   <li>Secondary (spots): {@code 0x4B6B32} (darker green)</li>
     * </ul>
     *
     * <p><b>Registry Name:</b> {@code xeenaa-alexs-mobs:test_animal_spawn_egg}
     *
     * <p><b>Display Name:</b> "Test Animal Spawn Egg" (from en_us.json)
     *
     * <p><b>Usage:</b> Available in the creative inventory under the
     * "Xeenaa's Alex's Mobs" tab. Right-click to spawn a Test Animal.
     *
     * @see ModEntities#TEST_ANIMAL
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.TestAnimalEntity
     */
    public static final Item TEST_ANIMAL_SPAWN_EGG = register(
        "test_animal_spawn_egg",
        new SpawnEggItem(ModEntities.TEST_ANIMAL, 0x76AF50, 0x4B6B32,
            new Item.Settings())
    );

    /**
     * Spawn egg for the Fly entity.
     *
     * <p>This spawn egg can be used to spawn Fly entities in the world. The egg
     * represents a cluster of fly eggs with dark coloring and red compound eyes.
     *
     * <p><b>Colors:</b>
     * <ul>
     *   <li>Primary (base): {@code 0x404040} (dark grey - fly body/eggs)</li>
     *   <li>Secondary (spots): {@code 0x8B0000} (dark red - compound eyes)</li>
     * </ul>
     *
     * <p><b>Registry Name:</b> {@code xeenaa-alexs-mobs:fly_spawn_egg}
     *
     * <p><b>Display Name:</b> "Fly Spawn Egg" (from en_us.json)
     *
     * <p><b>Usage:</b> Available in the creative inventory under the
     * "Xeenaa's Alex's Mobs" tab. Right-click to spawn a Fly entity.
     *
     * <p><b>Spawned Entity:</b> Creates a {@link com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity}
     * with 2 HP that immediately begins flying around or landing on nearby blocks.
     *
     * @see ModEntities#FLY
     * @see com.canya.xeenaa_alexs_mobs.entity.animal.FlyEntity
     */
    public static final Item FLY_SPAWN_EGG = register(
        "fly_spawn_egg",
        new SpawnEggItem(ModEntities.FLY, 0x404040, 0x8B0000,
            new Item.Settings())
    );

    // Future spawn eggs will be added here following the pattern above
    // Examples: ALLIGATOR_SPAWN_EGG, ANACONDA_SPAWN_EGG, ANTEATER_SPAWN_EGG, etc.

    // ============================================
    // Registration Methods
    // ============================================

    /**
     * Registers an item with Minecraft's item registry.
     *
     * <p>This method uses Fabric's registry system to register items
     * with the mod's namespace. All items are registered under the
     * "xeenaa-alexs-mobs" namespace.
     *
     * <p><b>Thread Safety:</b> This method is called during mod initialization
     * on the main thread and is not thread-safe. All items must be registered
     * before the game starts.
     *
     * @param name the registry name for this item (e.g., "alligator_spawn_egg")
     * @param item the item instance to register
     * @param <T> the item class type extending {@link Item}
     * @return the registered item (for chaining or field assignment)
     * @throws IllegalArgumentException if an item with this name is already registered
     */
    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM,
            Identifier.of("xeenaa-alexs-mobs", name), item);
    }

    /**
     * Initializes all item registrations and creates the custom item group.
     *
     * <p>This method must be called during mod initialization to ensure all items
     * are properly registered with Fabric and appear in the creative inventory.
     *
     * <p><b>Call this from {@code AlexsMobsMod.onInitialize()}.</b>
     *
     * <p>This method performs two key operations:
     * <ol>
     *   <li>Triggers static initialization of all item fields (spawn eggs, etc.)</li>
     *   <li>Registers the custom creative mode item group</li>
     * </ol>
     *
     * <p><b>Item Group Registration:</b> The item group is registered with all
     * spawn eggs and other mod items. The icon is set to the Test Animal Spawn Egg.
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * @Override
     * public void onInitialize() {
     *     ModEntities.initialize();
     *     ModItems.initialize();  // Register items and item group
     * }
     * }</pre>
     */
    public static void initialize() {
        // Register the custom item group (creative tab)
        Registry.register(Registries.ITEM_GROUP, ALEXS_MOBS_GROUP,
            FabricItemGroup.builder()
                .icon(() -> new ItemStack(TEST_ANIMAL_SPAWN_EGG))
                .displayName(Text.translatable("itemGroup.xeenaa-alexs-mobs.main"))
                .entries((context, entries) -> {
                    // Add all spawn eggs to the creative tab
                    entries.add(TEST_ANIMAL_SPAWN_EGG);
                    entries.add(FLY_SPAWN_EGG);

                    // Future items will be added here as they are implemented
                    // This ensures all mod items appear in the custom creative tab
                })
                .build()
        );
    }
}
