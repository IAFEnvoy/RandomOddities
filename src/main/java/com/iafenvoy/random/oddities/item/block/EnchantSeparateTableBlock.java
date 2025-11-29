package com.iafenvoy.random.oddities.item.block;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.screen.handler.EnchantSeparateTableScreenHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class EnchantSeparateTableBlock extends HorizontalDirectionalBlock {
    private static final MapCodec<EnchantSeparateTableBlock> CODEC = simpleCodec(x -> new EnchantSeparateTableBlock());

    public EnchantSeparateTableBlock() {
        super(Properties.ofFullCopy(Blocks.ENCHANTING_TABLE).lightLevel(state -> 15));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else if (ROCommonConfig.INSTANCE.blocks.enchantSeparateTable.getValue()) {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos) {
        return new SimpleMenuProvider((syncId, inventory, player) -> new EnchantSeparateTableScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, pos)), Component.translatable("screen.%s.enchant_separate_table".formatted(RandomOddities.MOD_ID)));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
