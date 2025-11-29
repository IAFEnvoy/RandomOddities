package com.iafenvoy.random.oddities.screen.handler;

import com.iafenvoy.random.oddities.screen.slot.InputPredicateSlot;
import com.iafenvoy.random.oddities.screen.slot.TakeOnlySlot;
import com.iafenvoy.random.oddities.registry.ROBlocks;
import com.iafenvoy.random.oddities.registry.ROScreenHandlers;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BreakerScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerLevelAccess context;

    public BreakerScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(10), ContainerLevelAccess.NULL);
    }

    public BreakerScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerLevelAccess context) {
        super(ROScreenHandlers.BREAKER.get(), syncId);
        this.inventory = inventory;
        this.context = context;
        checkContainerSize(inventory, 10);
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                this.addSlot(new TakeOnlySlot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
        this.addSlot(new InputPredicateSlot(inventory, 9, 134, 35, stack -> stack.getItem() instanceof DiggerItem));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack2, this.inventory.getContainerSize(), this.inventory.getContainerSize() + 36, true))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemStack2, 9, this.inventory.getContainerSize(), false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setByPlayer(ItemStack.EMPTY);
            else slot2.setChanged();
            if (itemStack2.getCount() == itemStack.getCount())
                return ItemStack.EMPTY;
            slot2.onTake(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.context, player, ROBlocks.BREAKER.get());
    }
}
