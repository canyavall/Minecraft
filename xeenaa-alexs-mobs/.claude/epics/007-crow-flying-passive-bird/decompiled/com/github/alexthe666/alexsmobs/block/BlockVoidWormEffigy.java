/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.DirectionalBlock
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.MapColor
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.github.alexthe666.alexsmobs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockVoidWormEffigy
extends Block {
    public static final DirectionProperty FACING = DirectionalBlock.f_52588_;
    private static final VoxelShape UP_SHAPE = Shapes.m_83110_((VoxelShape)Block.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)7.0, (double)16.0), (VoxelShape)Block.m_49796_((double)4.0, (double)6.0, (double)4.0, (double)12.0, (double)16.0, (double)12.0));
    private static final VoxelShape DOWN_SHAPE = Shapes.m_83110_((VoxelShape)Block.m_49796_((double)0.0, (double)9.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0), (VoxelShape)Block.m_49796_((double)4.0, (double)0.0, (double)4.0, (double)12.0, (double)10.0, (double)12.0));
    private static final VoxelShape SOUTH_SHAPE = Shapes.m_83110_((VoxelShape)Block.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)7.0), (VoxelShape)Block.m_49796_((double)4.0, (double)4.0, (double)6.0, (double)12.0, (double)12.0, (double)16.0));
    private static final VoxelShape NORTH_SHAPE = Shapes.m_83110_((VoxelShape)Block.m_49796_((double)0.0, (double)0.0, (double)9.0, (double)16.0, (double)16.0, (double)16.0), (VoxelShape)Block.m_49796_((double)4.0, (double)4.0, (double)0.0, (double)12.0, (double)12.0, (double)10.0));
    private static final VoxelShape EAST_SHAPE = Shapes.m_83110_((VoxelShape)Block.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)7.0, (double)16.0, (double)16.0), (VoxelShape)Block.m_49796_((double)6.0, (double)4.0, (double)4.0, (double)16.0, (double)12.0, (double)12.0));
    private static final VoxelShape WEST_SHAPE = Shapes.m_83110_((VoxelShape)Block.m_49796_((double)9.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0), (VoxelShape)Block.m_49796_((double)0.0, (double)4.0, (double)4.0, (double)10.0, (double)12.0, (double)12.0));

    public BlockVoidWormEffigy() {
        super(BlockBehaviour.Properties.m_284310_().m_284180_(MapColor.f_283765_).m_60999_().m_60978_(1.5f));
        this.m_49959_((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)FACING, (Comparable)Direction.NORTH));
    }

    public BlockState m_5573_(BlockPlaceContext context) {
        return (BlockState)this.m_49966_().m_61124_((Property)FACING, (Comparable)context.m_43719_());
    }

    public BlockState m_6843_(BlockState state, Rotation rot) {
        return (BlockState)state.m_61124_((Property)FACING, (Comparable)rot.m_55954_((Direction)state.m_61143_((Property)FACING)));
    }

    public BlockState m_6943_(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.m_54846_((Direction)state.m_61143_((Property)FACING)));
    }

    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        builder.m_61104_(new Property[]{FACING});
    }

    public VoxelShape m_5940_(BlockState p_54561_, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        return switch ((Direction)p_54561_.m_61143_((Property)FACING)) {
            case Direction.NORTH -> NORTH_SHAPE;
            case Direction.SOUTH -> SOUTH_SHAPE;
            case Direction.EAST -> EAST_SHAPE;
            case Direction.WEST -> WEST_SHAPE;
            case Direction.UP -> UP_SHAPE;
            default -> DOWN_SHAPE;
        };
    }
}

