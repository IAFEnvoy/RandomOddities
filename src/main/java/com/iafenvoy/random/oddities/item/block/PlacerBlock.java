package com.iafenvoy.random.oddities.item.block;

import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.item.block.entity.PlacerBlockEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlacerBlock extends RedstoneTriggerBlock {
    private static final MapCodec<PlacerBlock> CODEC = simpleCodec(x -> new PlacerBlock());

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PlacerBlockEntity(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PlacerBlockEntity placer)
                player.openMenu(placer);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity entity, @NotNull ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlacerBlockEntity placer)
            placer.applyComponents(stack.getComponents(), stack.getComponentsPatch());
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PlacerBlockEntity placer) {
                Containers.dropContents(world, pos, placer);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlacerBlockEntity placer && ROCommonConfig.INSTANCE.blocks.placer.getValue())
            placer.placeBlock(state, world);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
