package com.iafenvoy.random.oddities.item.block.entity;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.block.BreakerBlock;
import com.iafenvoy.random.oddities.registry.ROBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlacerBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

    public PlacerBlockEntity(BlockPos pos, BlockState state) {
        super(ROBlockEntities.PLACER.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt))
            ContainerHelper.loadAllItems(nbt, this.inventory, registries);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        if (!this.trySaveLootTable(nbt))
            ContainerHelper.saveAllItems(nbt, this.inventory, registries);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("screen.%s.placer".formatted(RandomOddities.MOD_ID));
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory) {
        return new DispenserMenu(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    public void placeBlock(BlockState state, ServerLevel world) {
        Direction facing = state.getValue(BreakerBlock.FACING);
        BlockPos targetPos = this.worldPosition.offset(facing.getNormal());
        ItemStack stack = ItemStack.EMPTY;
        for (ItemStack s : this.inventory)
            if (!s.isEmpty() && s.getItem() instanceof BlockItem) {
                stack = s;
                break;
            }
        if (stack.isEmpty()) return;
        BlockPlaceContext ctx = new DirectionalPlaceContext(world, targetPos, facing, stack, facing.getOpposite());
        if (world.getBlockState(targetPos).canBeReplaced() && stack.getItem() instanceof BlockItem blockItem)
            blockItem.place(ctx);
    }
}
