package com.iafenvoy.random.oddities.item.block.entity;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.block.BreakerBlock;
import com.iafenvoy.random.oddities.registry.ROBlockEntities;
import com.iafenvoy.random.oddities.screen.handler.BreakerScreenHandler;
import com.iafenvoy.random.oddities.util.ToolUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BreakerBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    private static final int TOOL_SLOT = 9;
    private static final int[] ITEM_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, TOOL_SLOTS = new int[]{TOOL_SLOT};
    private NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);
    private boolean canBreakBlock = false;
    private double breakDelta = 0;

    public BreakerBlockEntity(BlockPos pos, BlockState state) {
        super(ROBlockEntities.BREAKER.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt))
            ContainerHelper.loadAllItems(nbt, this.inventory, registries);
        this.canBreakBlock = nbt.getBoolean("canBreakBlock");
        this.breakDelta = nbt.getDouble("breakDelta");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        if (!this.trySaveLootTable(nbt))
            ContainerHelper.saveAllItems(nbt, this.inventory, registries);
        nbt.putBoolean("canBreakBlock", this.canBreakBlock);
        nbt.putDouble("breakDelta", this.breakDelta);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("screen.%s.breaker".formatted(RandomOddities.MOD_ID));
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
        return new BreakerScreenHandler(syncId, playerInventory, this, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    public void setCanBreakBlock(boolean canBreakBlock) {
        this.canBreakBlock = canBreakBlock;
    }

    public void insertOrDrop(ItemStack stack) {
        if (stack.isEmpty()) return;
        for (int i = 0; i < 9; i++) {
            ItemStack s = this.inventory.get(i);
            if (ItemStack.isSameItemSameComponents(s, stack)) {
                int count = s.getCount() + stack.getCount();
                if (count <= s.getMaxStackSize()) {
                    s.setCount(count);
                    return;
                } else {
                    s.setCount(s.getMaxStackSize());
                    stack.setCount(count - s.getMaxStackSize());
                }
            } else if (s.isEmpty()) {
                this.inventory.set(i, stack);
                return;
            }
        }
        if (!stack.isEmpty() && this.level != null)
            Block.popResource(this.level, this.worldPosition, stack);
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return side == Direction.DOWN ? ITEM_SLOTS : TOOL_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction dir) {
        if (slot == TOOL_SLOT) return stack.getItem() instanceof DiggerItem;
        else return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction dir) {
        return slot != TOOL_SLOT;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, BreakerBlockEntity blockEntity) {
        if (!(world instanceof ServerLevel serverWorld)) return;
        BlockPos targetPos = pos.offset(state.getValue(BreakerBlock.FACING).getNormal());
        BlockState targetState = world.getBlockState(targetPos);
        if (blockEntity.canBreakBlock && !targetState.isAir()) {
            ItemStack tool = blockEntity.getItem(TOOL_SLOT);
            float hardness = targetState.getDestroySpeed(world, pos);
            if (hardness >= 0) {
                blockEntity.breakDelta += ToolUtil.getDigSpeed(world.registryAccess(), targetState, tool) / hardness / (tool.isCorrectToolForDrops(targetState) ? 30 : 100);
                if (blockEntity.breakDelta >= 1) {
                    targetState.getDrops(new LootParams.Builder(serverWorld)
                                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(targetPos))
                                    .withParameter(LootContextParams.BLOCK_STATE, targetState)
                                    .withParameter(LootContextParams.TOOL, tool)
                                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(targetPos)))
                            .forEach(blockEntity::insertOrDrop);
                    world.destroyBlock(targetPos, false);
                    world.destroyBlockProgress(0, targetPos, -1);
                } else {
                    world.destroyBlockProgress(0, targetPos, (int) (blockEntity.breakDelta * 10));
                    return;
                }
            }
        }
        blockEntity.canBreakBlock = false;
        blockEntity.breakDelta = 0;
    }
}
