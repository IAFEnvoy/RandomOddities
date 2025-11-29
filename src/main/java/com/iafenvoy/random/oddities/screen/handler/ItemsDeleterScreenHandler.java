package com.iafenvoy.random.oddities.screen.handler;

import com.iafenvoy.random.oddities.registry.RODataComponents;
import com.iafenvoy.random.oddities.registry.ROItems;
import com.iafenvoy.random.oddities.registry.ROScreenHandlers;
import com.iafenvoy.random.oddities.screen.slot.SingleFakeItemSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class ItemsDeleterScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final Inventory playerInventory;

    public ItemsDeleterScreenHandler(int syncId, Inventory playerInventory) {
        super(ROScreenHandlers.ITEMS_DELETER.get(), syncId);
        this.inventory = new SimpleContainer(3);
        this.playerInventory = playerInventory;
        this.addSlot(new SingleFakeItemSlot(this, this.inventory, 0, 62, 20));
        this.addSlot(new SingleFakeItemSlot(this, this.inventory, 1, 80, 20));
        this.addSlot(new SingleFakeItemSlot(this, this.inventory, 2, 98, 20));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        List<ItemStack> stacks = playerInventory.getSelected().getOrDefault(RODataComponents.ITEM_DELETER, List.of());
        for (int i = 0; i < stacks.size() && i < this.inventory.getContainerSize(); i++)
            this.inventory.setItem(i, stacks.get(i));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        return this.getSlot(slot).getItem();
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(ROItems.ITEMS_DELETER.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.playerInventory.getSelected().set(RODataComponents.ITEM_DELETER, Stream.of(0, 1, 2).map(this.inventory::getItem).toList());
    }
}
