package com.iafenvoy.random.oddities.screen.slot;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InputPredicateSlot extends Slot {
    private final Predicate<ItemStack> insertPredicate;

    public InputPredicateSlot(Container inventory, int index, int x, int y, Predicate<ItemStack> insertPredicate) {
        super(inventory, index, x, y);
        this.insertPredicate = insertPredicate;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.insertPredicate.test(stack);
    }
}
