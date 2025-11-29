package com.iafenvoy.random.oddities.screen.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FakeItemSlot extends Slot {
    private final AbstractContainerMenu handler;

    public FakeItemSlot(AbstractContainerMenu handler, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    @Override
    public boolean mayPickup(@NotNull Player playerEntity) {
        this.setByPlayer(ItemStack.EMPTY);
        this.handler.slotsChanged(this.container);
        return true;
    }

    @Override
    public @NotNull ItemStack safeInsert(ItemStack stack) {
        this.setByPlayer(stack.copyWithCount(1));
        this.handler.slotsChanged(this.container);
        return stack;
    }

    @Override
    public @NotNull ItemStack safeInsert(@NotNull ItemStack stack, int count) {
        return this.safeInsert(stack);
    }
}
