/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.inventory.CraftingContainer
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.CraftingBookCategory
 *  net.minecraft.world.item.crafting.CustomRecipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.registries.ForgeRegistries
 */
package com.github.alexthe666.alexsmobs.misc;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMRecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeMimicreamRepair
extends CustomRecipe {
    public RecipeMimicreamRepair(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        if (!AMConfig.mimicreamRepair) {
            return false;
        }
        ItemStack damageableStack = ItemStack.f_41583_;
        int mimicreamCount = 0;
        for (int j = 0; j < inv.m_6643_(); ++j) {
            ItemStack itemstack1 = inv.m_8020_(j);
            if (itemstack1.m_41619_()) continue;
            if (itemstack1.m_41763_() && !this.isBlacklisted(itemstack1)) {
                damageableStack = itemstack1;
                continue;
            }
            if (itemstack1.m_41720_() != AMItemRegistry.MIMICREAM.get()) continue;
            ++mimicreamCount;
        }
        return !damageableStack.m_41619_() && mimicreamCount >= 8;
    }

    public boolean isBlacklisted(ItemStack stack) {
        ResourceLocation name = ForgeRegistries.ITEMS.getKey((Object)stack.m_41720_());
        return name != null && AMConfig.mimicreamBlacklist.contains(name.toString());
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack damageableStack = ItemStack.f_41583_;
        int mimicreamCount = 0;
        for (int j = 0; j < inv.m_6643_(); ++j) {
            ItemStack itemstack1 = inv.m_8020_(j);
            if (itemstack1.m_41619_()) continue;
            if (itemstack1.m_41763_() && !this.isBlacklisted(itemstack1)) {
                damageableStack = itemstack1;
                continue;
            }
            if (itemstack1.m_41720_() != AMItemRegistry.MIMICREAM.get()) continue;
            ++mimicreamCount;
        }
        if (!damageableStack.m_41619_() && mimicreamCount >= 8) {
            ItemStack itemstack2 = damageableStack.m_41777_();
            CompoundTag compoundnbt = damageableStack.m_41783_().m_6426_();
            if (damageableStack.m_150930_((Item)AMItemRegistry.GHOSTLY_PICKAXE.get()) && compoundnbt.m_128441_("Items")) {
                compoundnbt.m_128473_("Items");
            }
            ListTag oldNBTList = compoundnbt.m_128437_("Enchantments", 10);
            ListTag newNBTList = new ListTag();
            ResourceLocation mendingName = ForgeRegistries.ENCHANTMENTS.getKey((Object)Enchantments.f_44962_);
            for (int i = 0; i < oldNBTList.size(); ++i) {
                CompoundTag compoundnbt2 = oldNBTList.m_128728_(i);
                ResourceLocation resourcelocation1 = ResourceLocation.m_135820_((String)compoundnbt2.m_128461_("id"));
                if (resourcelocation1 != null && resourcelocation1.equals((Object)mendingName)) continue;
                newNBTList.add((Object)compoundnbt2);
            }
            compoundnbt.m_128365_("Enchantments", (Tag)newNBTList);
            itemstack2.m_41751_(compoundnbt);
            itemstack2.m_41721_(itemstack2.m_41776_());
            return itemstack2;
        }
        return ItemStack.f_41583_;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList nonnulllist = NonNullList.m_122780_((int)inv.m_6643_(), (Object)ItemStack.f_41583_);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.m_8020_(i);
            if (itemstack.hasCraftingRemainingItem()) {
                nonnulllist.set(i, (Object)itemstack.getCraftingRemainingItem());
                continue;
            }
            if (!itemstack.m_41720_().m_41465_()) continue;
            ItemStack itemstack1 = itemstack.m_41777_();
            itemstack1.m_41764_(1);
            nonnulllist.set(i, (Object)itemstack1);
            break;
        }
        return nonnulllist;
    }

    public RecipeSerializer<?> m_7707_() {
        return (RecipeSerializer)AMRecipeRegistry.MIMICREAM_RECIPE.get();
    }

    public boolean m_8004_(int width, int height) {
        return width >= 3 && height >= 3;
    }
}

