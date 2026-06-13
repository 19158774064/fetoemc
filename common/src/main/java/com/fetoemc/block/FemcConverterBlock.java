package com.fetoemc.block;

import com.fetoemc.block.entity.FemcConverterBlockEntity;
import com.fetoemc.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * FEMC 转换器方块
 * <p>
 * 实现 EntityBlock，拥有对应的 BlockEntity 用于 FE 能量存储。
 * 在方块放置时记录放置者 UUID，用于后续 EMC 发放。
 * </p>
 */
public class FemcConverterBlock extends Block implements EntityBlock {

    public FemcConverterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FemcConverterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return type == ModBlockEntities.FEMC_CONVERTER.get()
                ? (lvl, pos, st, be) -> ((FemcConverterBlockEntity) be).tick(lvl, pos, st)
                : null;
    }

    /**
     * 方块被放置时记录放置者 UUID 到 BlockEntity
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide()) return;
        if (level.getBlockEntity(pos) instanceof FemcConverterBlockEntity be) {
            be.setPlacer(placer instanceof net.minecraft.world.entity.player.Player player ? player : null);
        }
    }
}