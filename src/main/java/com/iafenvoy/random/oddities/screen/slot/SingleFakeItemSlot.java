package com.iafenvoy.random.oddities.screen.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SingleFakeItemSlot extends FakeItemSlot {
    public SingleFakeItemSlot(AbstractContainerMenu handler, Container inventory, int index, int x, int y) {
        super(handler, inventory, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return this.getMaxStackSize();
    }
}
