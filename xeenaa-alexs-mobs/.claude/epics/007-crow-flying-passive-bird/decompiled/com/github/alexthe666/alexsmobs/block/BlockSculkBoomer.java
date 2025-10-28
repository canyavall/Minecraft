/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEventListener
 *  net.minecraft.world.level.material.MapColor
 *  org.jetbrains.annotations.Nullable
 */
package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntitySculkBoomer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class BlockSculkBoomer
extends BaseEntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.f_61448_;
    public static final BooleanProperty OPEN = BooleanProperty.m_61465_((String)"open");

    protected BlockSculkBoomer() {
        super(BlockBehaviour.Properties.m_284310_().m_284180_(MapColor.f_283743_).m_60913_(3.0f, 12.0f).m_60918_(SoundType.f_222472_));
        this.m_49959_((BlockState)((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)OPEN, (Comparable)Boolean.valueOf(false))).m_61124_((Property)POWERED, (Comparable)Boolean.valueOf(false)));
    }

    public void m_6861_(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.f_46443_) {
            this.updateState(state, worldIn, pos, blockIn);
        }
    }

    public void m_213898_(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!worldIn.f_46443_) {
            this.updateState(state, (Level)worldIn, pos, state.m_60734_());
        }
    }

    public void updateState(BlockState state, Level worldIn, BlockPos pos, Block blockIn) {
        boolean flag = (Boolean)state.m_61143_((Property)POWERED);
        boolean flag1 = worldIn.m_276867_(pos);
        if (flag1 != flag) {
            worldIn.m_7731_(pos, (BlockState)state.m_61124_((Property)POWERED, (Comparable)Boolean.valueOf(flag1)), 3);
            worldIn.m_46672_(pos.m_7495_(), (Block)this);
        }
    }

    public BlockState m_5573_(BlockPlaceContext context) {
        return (BlockState)((BlockState)this.m_49966_().m_61124_((Property)OPEN, (Comparable)Boolean.valueOf(false))).m_61124_((Property)POWERED, (Comparable)Boolean.valueOf(context.m_43725_().m_276867_(context.m_8083_())));
    }

    public RenderShape m_7514_(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity m_142194_(BlockPos pos, BlockState state) {
        return new TileEntitySculkBoomer(pos, state);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> m_142354_(Level p_222100_, BlockState p_222101_, BlockEntityType<T> p_222102_) {
        return BlockSculkBoomer.m_152132_(p_222102_, (BlockEntityType)((BlockEntityType)AMTileEntityRegistry.SCULK_BOOMER.get()), TileEntitySculkBoomer::commonTick);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> GameEventListener m_214009_(ServerLevel p_222092_, T p_222093_) {
        return p_222093_ instanceof TileEntitySculkBoomer ? (TileEntitySculkBoomer)p_222093_ : null;
    }

    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        builder.m_61104_(new Property[]{POWERED, OPEN});
    }
}

