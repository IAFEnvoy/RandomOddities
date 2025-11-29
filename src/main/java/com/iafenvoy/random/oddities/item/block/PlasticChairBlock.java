package com.iafenvoy.random.oddities.item.block;

import com.iafenvoy.random.oddities.entity.ChairEntity;
import com.iafenvoy.random.oddities.item.PlasticChairItem;
import com.iafenvoy.random.oddities.item.block.entity.PlasticChairBlockEntity;
import com.iafenvoy.random.oddities.registry.ROBlocks;
import com.iafenvoy.random.oddities.registry.ROEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PlasticChairBlock extends BaseEntityBlock {
    private static final MapCodec<PlasticChairBlock> CODEC = simpleCodec(x -> new PlasticChairBlock());
    public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");

    public PlasticChairBlock() {
        super(Properties.of().mapColor(MapColor.SAND).sound(SoundType.SCAFFOLDING).isValidSpawn((state, world, pos, type) -> false).pushReaction(PushReaction.DESTROY).isRedstoneConductor((state, world, pos) -> false));
        this.registerDefaultState(this.getStateDefinition().any().setValue(OCCUPIED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OCCUPIED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PlasticChairBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof PlasticChairBlockEntity plasticChair)
            plasticChair.addChair(PlasticChairItem.readColor(itemStack));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, BlockPos pos) {
        BlockPos down = pos.below();
        return canSupportRigidBlock(world, down) || world.getBlockState(down).is(this) && world.getBlockEntity(down) instanceof PlasticChairBlockEntity plasticChair && !plasticChair.isFull();
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockPos target = findTopPos(level, pos);
        if (!state.getValue(OCCUPIED) && level.getBlockEntity(target) instanceof PlasticChairBlockEntity plasticChair) {
            if (stack.is(ROBlocks.PLASTIC_CHAIR.get().asItem())) {
                if (!plasticChair.addChair(PlasticChairItem.readColor(stack))) {
                    BlockPos up = target.above();
                    level.setBlockAndUpdate(up, state);
                    this.setPlacedBy(level, up, state, player, stack);
                }
                if (!player.isCreative()) stack.shrink(1);
                level.playLocalSound(target, this.soundType.getPlaceSound(), SoundSource.BLOCKS, 1, 0, true);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        BlockPos target = findTopPos(level, pos);
        if (!state.getValue(OCCUPIED) && level.getBlockEntity(target) instanceof PlasticChairBlockEntity plasticChair) {
            if (player.isShiftKeyDown()) {
                if (Math.random() < 0.5) {
                    Optional<DyeColor> color = plasticChair.removeChair();
                    if (color.isPresent()) {
                        player.getInventory().placeItemBackInInventory(PlasticChairItem.create(color.get()));
                        level.playLocalSound(target, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1, 0, true);
                    }
                } else
                    level.playLocalSound(target, this.soundType.getHitSound(), SoundSource.BLOCKS, 1, 0, true);
            } else {
                ChairEntity chair = new ChairEntity(ROEntities.CHAIR.get(), level);
                chair.setPos(Vec3.atCenterOf(target).relative(Direction.UP, plasticChair.getColors().size() * 1.0 / PlasticChairBlockEntity.MAX_CHAIRS));
                chair.setTarget(target);
                level.addFreshEntity(chair);
                level.setBlockAndUpdate(target, this.defaultBlockState().setValue(OCCUPIED, true));
                player.startRiding(chair);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        return List.of();
    }

    @Override
    public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        if (blockEntity instanceof PlasticChairBlockEntity plasticChair)
            plasticChair.getColors().stream().map(PlasticChairItem::create).forEach(x -> popResource(world, pos, x));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        if (level.getBlockEntity(pos) instanceof PlasticChairBlockEntity plasticChair) {
            ItemStack stack = new ItemStack(ROBlocks.PLASTIC_CHAIR.get());
            plasticChair.saveToItem(stack, level.registryAccess());
            return stack;
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        float height = 0;
        if (world.getBlockEntity(pos) instanceof PlasticChairBlockEntity plasticChair)
            height = 16.0f * (plasticChair.getColors().size() - 1) / PlasticChairBlockEntity.MAX_CHAIRS;
        return box(4, 0.01, 4, 12, 13 + height, 12);
    }

    public static BlockPos findTopPos(Level world, BlockPos pos) {
        while (world.getBlockState(pos).is(ROBlocks.PLASTIC_CHAIR.get())) pos = pos.above();
        return pos.below();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
