package com.iafenvoy.random.oddities.screen.inventory;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryUtil {
    public static boolean hasAllItems(Container target, Container items) {
        target = copy(target);
        Map<StackHolder, Integer> itemsMap = new HashMap<>();
        for (int i = 0; i < items.getContainerSize(); i++) {
            ItemStack stack = items.getItem(i);
            if (stack.isEmpty()) continue;
            StackHolder holder = new StackHolder(stack);
            itemsMap.put(holder, itemsMap.getOrDefault(holder, 0) + stack.getCount());
        }
        for (int i = 0; i < target.getContainerSize(); i++) {
            ItemStack targetStack = target.getItem(i);
            for (Map.Entry<StackHolder, Integer> entry : itemsMap.entrySet()) {
                StackHolder stack = entry.getKey();
                int neededCount = entry.getValue();
                if (stack.areEqual(targetStack)) {
                    int count = targetStack.getCount();
                    if (count >= neededCount) itemsMap.put(stack, 0);
                    else itemsMap.put(stack, neededCount - count);
                    break;
                }
            }
        }
        for (int count : itemsMap.values())
            if (count > 0) return false;
        return true;
    }

    public static boolean removeItems(Container target, Container items) {
        Map<StackHolder, Integer> itemsMap = new HashMap<>();
        for (int i = 0; i < items.getContainerSize(); i++) {
            ItemStack stack = items.getItem(i);
            if (stack.isEmpty()) continue;
            StackHolder holder = new StackHolder(stack);
            itemsMap.put(holder, itemsMap.getOrDefault(holder, 0) + stack.getCount());
        }
        for (int i = 0; i < target.getContainerSize(); i++) {
            ItemStack targetStack = target.getItem(i);
            for (Map.Entry<StackHolder, Integer> entry : itemsMap.entrySet()) {
                StackHolder stack = entry.getKey();
                int neededCount = entry.getValue();
                if (stack.areEqual(targetStack)) {
                    int count = targetStack.getCount();
                    if (count >= neededCount) {
                        targetStack.shrink(neededCount);
                        itemsMap.put(stack, 0);
                        break;
                    } else {
                        itemsMap.put(stack, neededCount - count);
                        targetStack.setCount(0);
                    }
                }
            }
        }
        for (int count : itemsMap.values())
            if (count > 0) return false;
        return true;
    }

    public static boolean canFitItems(Container inventory, Container in, Container out) {
        inventory = copy(inventory);
        return removeItems(inventory, out) && insertItems(inventory, in);
    }

    public static boolean insertItems(Container inventory, Container insert) {
        for (int i = 0; i < insert.getContainerSize(); i++) {
            ItemStack insertStack = insert.getItem(i);
            if (!tryAddItemToInventory(inventory, insertStack.copy()))
                return false;
        }
        return true;
    }

    private static boolean tryAddItemToInventory(Container inventory, ItemStack stack) {
        if (stack.isEmpty()) return true;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack inventoryStack = inventory.getItem(i);
            if (ItemStack.isSameItemSameComponents(inventoryStack, stack)) {
                if (stack.getMaxStackSize() - inventoryStack.getCount() > 0) {
                    int countToAdd = Math.min(stack.getCount(), stack.getMaxStackSize() - inventoryStack.getCount());
                    inventoryStack.grow(countToAdd);
                    stack.shrink(countToAdd);
                    if (stack.getCount() == 0) return true;
                }
            }
        }
        if (inventory instanceof Inventory playerInventory) {
            playerInventory.placeItemBackInInventory(stack);
            return true;
        } else for (int i = 0; i < inventory.getContainerSize(); i++)
            if (inventory.getItem(i).isEmpty()) {
                inventory.setItem(i, stack);
                return true;
            }
        return false;
    }

    public static Container copy(Container another) {
        Container inventory = new SimpleContainer(another.getContainerSize());
        for (int i = 0; i < another.getContainerSize(); i++)
            inventory.setItem(i, another.getItem(i).copy());
        return inventory;
    }

    public record StackHolder(Item item, @Nullable DataComponentMap components) {
        public StackHolder(ItemStack stack) {
            this(stack.getItem(), stack.getComponents());
        }

        public boolean areEqual(ItemStack stack) {
            return stack.is(this.item) && Objects.equals(stack.getComponents(), this.components);
        }
    }
}
