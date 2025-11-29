package com.iafenvoy.random.oddities.item.block;

import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.item.block.entity.BreakerBlockEntity;
import com.iafenvoy.random.oddities.registry.ROBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BreakerBlock extends RedstoneTriggerBlock {
    private static final MapCodec<BreakerBlock> CODEC = simpleCodec(x -> new BreakerBlock());

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BreakerBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, ROBlockEntities.BREAKER.get(), BreakerBlockEntity::tick);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BreakerBlockEntity breaker)
                player.openMenu(breaker);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BreakerBlockEntity breaker)
            breaker.applyComponents(stack.getComponents(), stack.getComponentsPatch());
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BreakerBlockEntity breaker) {
                Containers.dropContents(world, pos, breaker);
                world.updateNeighbourForOutputSignal(pos, this);
                breaker.setCanBreakBlock(false);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BreakerBlockEntity breaker && ROCommonConfig.INSTANCE.blocks.breaker.getValue())
            breaker.setCanBreakBlock(true);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
