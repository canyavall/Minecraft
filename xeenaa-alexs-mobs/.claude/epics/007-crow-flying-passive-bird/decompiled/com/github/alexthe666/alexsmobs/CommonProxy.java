/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathfindingConstants
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.github.alexthe666.alexsmobs;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.misc.CapsidRecipeManager;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathfindingConstants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="alexsmobs", bus=Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {
    private CapsidRecipeManager capsidRecipeManager;

    public void init() {
    }

    public void clientInit() {
    }

    public Player getClientSidePlayer() {
        return null;
    }

    public void openBookGUI(ItemStack itemStackIn) {
    }

    public void openBookGUI(ItemStack itemStackIn, String page) {
    }

    public Object getArmorModel(int armorId, LivingEntity entity) {
        return null;
    }

    public void onEntityStatus(Entity entity, byte updateKind) {
    }

    public void updateBiomeVisuals(int x, int z) {
    }

    public void setRenderViewEntity(Entity entity) {
    }

    public void resetRenderViewEntity() {
    }

    public int getPreviousPOV() {
        return 0;
    }

    public boolean isFarFromCamera(double x, double y, double z) {
        return true;
    }

    public void resetVoidPortalCreation(Player player) {
    }

    public Object getISTERProperties() {
        return null;
    }

    public Object getArmorRenderProperties() {
        return null;
    }

    public void spawnSpecialParticle(int i) {
    }

    public void processVisualFlag(Entity entity, int flag) {
    }

    public void setPupfishChunkForItem(int chunkX, int chunkZ) {
    }

    public CapsidRecipeManager getCapsidRecipeManager() {
        if (this.capsidRecipeManager == null) {
            this.capsidRecipeManager = new CapsidRecipeManager();
        }
        return this.capsidRecipeManager;
    }

    public void setDisplayTransmuteResult(int slot, ItemStack stack) {
    }

    public ItemStack getDisplayTransmuteResult(int slot) {
        return ItemStack.f_41583_;
    }

    public int getSingingBlueJayId() {
        return -1;
    }

    public void initPathfinding() {
        PathfindingConstants.pathfindingThreads = Math.max(PathfindingConstants.pathfindingThreads, AMConfig.pathfindingThreads);
    }
}

