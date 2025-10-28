/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.client.extensions.common.IClientItemExtensions
 */
package com.github.alexthe666.alexsmobs.client.render.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.model.layered.AMModelLayers;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelAMElytra;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelFedora;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelFlyingFishBoots;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelFrontierCap;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelFroststalkerHelmet;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelMooseHeadgear;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelNoveltyHat;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelRoadrunnerBoots;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelRockyChestplate;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelSombrero;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelSpikedTurtleShell;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelUnsettlingKimono;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CustomArmorRenderProperties
implements IClientItemExtensions {
    private static boolean init;
    public static ModelAMElytra ELYTRA_MODEL;
    public static ModelRoadrunnerBoots ROADRUNNER_BOOTS_MODEL;
    public static ModelMooseHeadgear MOOSE_HEADGEAR_MODEL;
    public static ModelFrontierCap FRONTIER_CAP_MODEL;
    public static ModelSpikedTurtleShell SPIKED_TURTLE_SHELL_MODEL;
    public static ModelFedora FEDORA_MODEL;
    public static ModelSombrero SOMBRERO_MODEL;
    public static ModelSombrero SOMBRERO_GOOFY_FASHION_MODEL;
    public static ModelFroststalkerHelmet FROSTSTALKER_HELMET_MODEL;
    public static ModelRockyChestplate ROCKY_CHESTPLATE_MODEL;
    public static ModelFlyingFishBoots FLYING_FISH_BOOTS_MODEL;
    public static ModelNoveltyHat NOVELTY_HAT_MODEL;
    public static ModelUnsettlingKimono UNSETTLING_KIMONO_MODEL;

    public static void initializeModels() {
        init = true;
        ROADRUNNER_BOOTS_MODEL = new ModelRoadrunnerBoots(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.ROADRUNNER_BOOTS));
        MOOSE_HEADGEAR_MODEL = new ModelMooseHeadgear(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.MOOSE_HEADGEAR));
        FRONTIER_CAP_MODEL = new ModelFrontierCap(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.FRONTIER_CAP));
        FEDORA_MODEL = new ModelFedora(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.FEDORA));
        SPIKED_TURTLE_SHELL_MODEL = new ModelSpikedTurtleShell(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.SPIKED_TURTLE_SHELL));
        SOMBRERO_MODEL = new ModelSombrero(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.SOMBRERO));
        SOMBRERO_GOOFY_FASHION_MODEL = new ModelSombrero(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.SOMBRERO_GOOFY_FASHION));
        FROSTSTALKER_HELMET_MODEL = new ModelFroststalkerHelmet(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.FROSTSTALKER_HELMET));
        ELYTRA_MODEL = new ModelAMElytra(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.AM_ELYTRA));
        ROCKY_CHESTPLATE_MODEL = new ModelRockyChestplate(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.ROCKY_CHESTPLATE));
        FLYING_FISH_BOOTS_MODEL = new ModelFlyingFishBoots(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.FLYING_FISH_BOOTS));
        NOVELTY_HAT_MODEL = new ModelNoveltyHat(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.NOVELTY_HAT));
        UNSETTLING_KIMONO_MODEL = new ModelUnsettlingKimono(Minecraft.m_91087_().m_167973_().m_171103_(AMModelLayers.UNSETTLING_KIMONO));
    }

    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
        Item item;
        if (!init) {
            CustomArmorRenderProperties.initializeModels();
        }
        if ((item = itemStack.m_41720_()) == AMItemRegistry.TARANTULA_HAWK_ELYTRA.get()) {
            return ELYTRA_MODEL.withAnimations(entityLiving);
        }
        if (item == AMItemRegistry.ROADDRUNNER_BOOTS.get()) {
            return ROADRUNNER_BOOTS_MODEL;
        }
        if (item == AMItemRegistry.MOOSE_HEADGEAR.get()) {
            return MOOSE_HEADGEAR_MODEL;
        }
        if (item == AMItemRegistry.FRONTIER_CAP.get()) {
            return FRONTIER_CAP_MODEL.withAnimations(entityLiving);
        }
        if (item == AMItemRegistry.FEDORA.get()) {
            return FEDORA_MODEL;
        }
        if (item == AMItemRegistry.SPIKED_TURTLE_SHELL.get()) {
            return SPIKED_TURTLE_SHELL_MODEL;
        }
        if (item == AMItemRegistry.SOMBRERO.get()) {
            return AlexsMobs.isAprilFools() ? SOMBRERO_GOOFY_FASHION_MODEL : SOMBRERO_MODEL;
        }
        if (item == AMItemRegistry.FROSTSTALKER_HELMET.get()) {
            return FROSTSTALKER_HELMET_MODEL;
        }
        if (item == AMItemRegistry.ROCKY_CHESTPLATE.get()) {
            return ROCKY_CHESTPLATE_MODEL;
        }
        if (item == AMItemRegistry.FLYING_FISH_BOOTS.get()) {
            return FLYING_FISH_BOOTS_MODEL.withAnimations(entityLiving);
        }
        if (item == AMItemRegistry.NOVELTY_HAT.get()) {
            return NOVELTY_HAT_MODEL;
        }
        if (item == AMItemRegistry.UNSETTLING_KIMONO.get()) {
            return UNSETTLING_KIMONO_MODEL;
        }
        return _default;
    }
}

