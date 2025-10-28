/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.phys.Vec3
 */
package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class SealAIDiveForItems
extends Goal {
    private final EntitySeal seal;
    private Player thrower;
    private BlockPos digPos;
    private boolean returnToPlayer = false;
    private int digTime = 0;
    public static final ResourceLocation SEAL_REWARD = new ResourceLocation("alexsmobs", "gameplay/seal_reward");

    public SealAIDiveForItems(EntitySeal seal) {
        this.seal = seal;
    }

    private static List<ItemStack> getItemStacks(EntitySeal seal) {
        LootTable loottable = seal.m_9236_().m_7654_().m_278653_().m_278676_(SEAL_REWARD);
        return loottable.m_287195_(new LootParams.Builder((ServerLevel)seal.m_9236_()).m_287286_(LootContextParams.f_81455_, (Object)seal).m_287235_(LootContextParamSets.f_81417_));
    }

    public boolean m_8036_() {
        if (this.seal.feederUUID == null || this.seal.m_9236_().m_46003_(this.seal.feederUUID) == null || this.seal.revengeCooldown > 0) {
            return false;
        }
        this.thrower = this.seal.m_9236_().m_46003_(this.seal.feederUUID);
        this.digPos = this.genDigPos();
        return this.thrower != null && this.digPos != null;
    }

    public boolean m_8045_() {
        return this.seal.m_5448_() == null && this.seal.revengeCooldown == 0 && this.seal.m_21188_() == null && this.thrower != null && this.seal.feederUUID != null && this.digPos != null && this.seal.m_9236_().m_6425_(this.digPos.m_7494_()).m_205070_(FluidTags.f_13131_);
    }

    public void m_8037_() {
        this.seal.setBasking(false);
        if (this.returnToPlayer) {
            this.seal.m_21573_().m_5624_((Entity)this.thrower, 1.0);
            if ((double)this.seal.m_20270_((Entity)this.thrower) < 2.0) {
                ItemStack stack = this.seal.m_21205_().m_41777_();
                this.seal.m_21008_(InteractionHand.MAIN_HAND, ItemStack.f_41583_);
                ItemEntity item = this.seal.m_19983_(stack);
                if (item != null) {
                    double d0 = this.thrower.m_20185_() - this.seal.m_20185_();
                    double d1 = this.thrower.m_20188_() - this.seal.m_20188_();
                    double d2 = this.thrower.m_20189_() - this.seal.m_20189_();
                    double lvt_7_1_ = Mth.m_14116_((float)((float)(d0 * d0 + d2 * d2)));
                    float pitch = (float)(-(Mth.m_14136_((double)d1, (double)lvt_7_1_) * 57.2957763671875));
                    float yaw = (float)(Mth.m_14136_((double)d2, (double)d0) * 57.2957763671875) - 90.0f;
                    float f8 = Mth.m_14031_((float)(pitch * ((float)Math.PI / 180)));
                    float f2 = Mth.m_14089_((float)(pitch * ((float)Math.PI / 180)));
                    float f3 = Mth.m_14031_((float)(yaw * ((float)Math.PI / 180)));
                    float f4 = Mth.m_14089_((float)(yaw * ((float)Math.PI / 180)));
                    float f5 = this.seal.m_217043_().m_188501_() * ((float)Math.PI * 2);
                    float f6 = 0.02f * this.seal.m_217043_().m_188501_();
                    item.m_20334_((double)(-f3 * f2 * 0.5f) + Math.cos(f5) * (double)f6, (double)(-f8 * 0.2f + 0.1f + (this.seal.m_217043_().m_188501_() - this.seal.m_217043_().m_188501_()) * 0.1f), (double)(f4 * f2 * 0.5f) + Math.sin(f5) * (double)f6);
                }
                this.seal.feederUUID = null;
                this.m_8041_();
            }
        } else {
            double dist = this.seal.m_20238_(Vec3.m_82512_((Vec3i)this.digPos.m_7494_()));
            double d0 = (double)this.digPos.m_123341_() + 0.5 - this.seal.m_20185_();
            double d1 = (double)this.digPos.m_123342_() + 0.5 - this.seal.m_20188_();
            double d2 = (double)this.digPos.m_123343_() + 0.5 - this.seal.m_20189_();
            float f = (float)(Mth.m_14136_((double)d2, (double)d0) * 57.2957763671875) - 90.0f;
            if (dist < 2.0) {
                this.seal.m_21573_().m_26573_();
                ++this.digTime;
                if (this.digTime % 5 == 0) {
                    SoundEvent sound = this.seal.m_9236_().m_8055_(this.digPos).m_60827_().m_56778_();
                    this.seal.m_5496_(sound, 1.0f, 0.5f + this.seal.m_217043_().m_188501_() * 0.5f);
                }
                if (this.digTime >= 100) {
                    List<ItemStack> lootList = SealAIDiveForItems.getItemStacks(this.seal);
                    if (lootList.size() > 0) {
                        ItemStack copy = lootList.remove(0);
                        copy = copy.m_41777_();
                        this.seal.m_21008_(InteractionHand.MAIN_HAND, copy);
                        for (ItemStack stack : lootList) {
                            this.seal.m_19983_(stack.m_41777_());
                        }
                        this.returnToPlayer = true;
                    }
                    this.seal.setDigging(false);
                    this.digTime = 0;
                } else {
                    this.seal.setDigging(true);
                }
            } else {
                this.seal.setDigging(false);
                this.seal.m_21573_().m_26519_((double)this.digPos.m_123341_(), (double)this.digPos.m_123342_(), (double)this.digPos.m_123343_(), 1.0);
                this.seal.m_146922_(f);
            }
        }
    }

    public void m_8041_() {
        this.seal.setDigging(false);
        this.digPos = null;
        this.thrower = null;
        this.digTime = 0;
        this.returnToPlayer = false;
        this.seal.fishFeedings = 0;
        if (!this.seal.m_21205_().m_41619_()) {
            this.seal.m_19983_(this.seal.m_21205_().m_41777_());
            this.seal.m_21008_(InteractionHand.MAIN_HAND, ItemStack.f_41583_);
        }
    }

    private BlockPos genSeafloorPos(BlockPos parent) {
        Level world = this.seal.m_9236_();
        RandomSource random = this.seal.m_217043_();
        int range = 15;
        for (int i = 0; i < 15; ++i) {
            BlockPos seafloor = parent.m_7918_(random.m_188503_(range) - range / 2, 0, random.m_188503_(range) - range / 2);
            while (world.m_6425_(seafloor).m_205070_(FluidTags.f_13131_) && seafloor.m_123342_() > 1) {
                seafloor = seafloor.m_7495_();
            }
            BlockState state = world.m_8055_(seafloor);
            if (!state.m_204336_(AMTagRegistry.SEAL_DIGABLES)) continue;
            return seafloor;
        }
        return null;
    }

    private BlockPos genDigPos() {
        RandomSource random = this.seal.m_217043_();
        int range = 15;
        if (this.seal.m_20069_()) {
            return this.genSeafloorPos(this.seal.m_20183_());
        }
        for (int i = 0; i < 15; ++i) {
            BlockPos pos3;
            BlockPos blockpos1 = this.seal.m_20183_().m_7918_(random.m_188503_(range) - range / 2, 3, random.m_188503_(range) - range / 2);
            while (this.seal.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.m_7495_();
            }
            if (!this.seal.m_9236_().m_6425_(blockpos1).m_205070_(FluidTags.f_13131_) || (pos3 = this.genSeafloorPos(blockpos1)) == null) continue;
            return pos3;
        }
        return null;
    }
}

