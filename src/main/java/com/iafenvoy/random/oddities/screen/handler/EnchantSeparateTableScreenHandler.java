package com.iafenvoy.random.oddities.screen.handler;

import com.iafenvoy.random.oddities.screen.slot.InputPredicateSlot;
import com.iafenvoy.random.oddities.registry.ROBlocks;
import com.iafenvoy.random.oddities.registry.ROScreenHandlers;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnchantSeparateTableScreenHandler extends AbstractContainerMenu {
    private final ContainerLevelAccess context;
    private final Container input, result;
    private final DataSlot levelCost;
    private Object2IntMap.Entry<Holder<Enchantment>> lastEnchantment = null;

    public EnchantSeparateTableScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    public EnchantSeparateTableScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(ROScreenHandlers.ENCHANT_SEPARATE_TABLE.get(), syncId);
        this.context = context;
        this.input = new SimpleContainer(2) {
            public void setChanged() {
                super.setChanged();
                EnchantSeparateTableScreenHandler.this.slotsChanged(this);
            }
        };
        this.result = new ResultContainer();
        this.levelCost = DataSlot.standalone();
        this.addDataSlot(this.levelCost);
        this.addSlot(new InputPredicateSlot(this.input, 0, 37, 19, stack -> stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted()));
        this.addSlot(new InputPredicateSlot(this.input, 1, 65, 19, stack -> stack.is(Items.BOOK)));
        this.addSlot(new Slot(this.result, 2, 129, 19) {
            @Override
            public boolean mayPickup(@NotNull Player player) {
                return EnchantSeparateTableScreenHandler.this.canTakeItems(player);
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                EnchantSeparateTableScreenHandler.this.onTakeItem(player);
                super.onTake(player, stack);
            }
        });
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 57 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 115));
    }

    protected boolean canTakeItems(Player player) {
        return (player.getAbilities().instabuild || player.experienceLevel >= this.levelCost.get()) && this.levelCost.get() > 0;
    }

    protected void onTakeItem(Player player) {
        if (this.lastEnchantment != null) {
            ItemStack s = this.input.getItem(0);
            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(s.getTagEnchantments());
            mutable.removeIf(x -> x == this.lastEnchantment.getKey());
            EnchantmentHelper.setEnchantments(s, mutable.toImmutable());
        }
        this.input.getItem(1).shrink(1);
        player.giveExperienceLevels(-this.levelCost.get());
        this.context.execute((world, pos) -> world.levelEvent(1030, pos, 0));
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        ItemStack enchanted = this.input.getItem(0), book = this.input.getItem(1);
        this.result.setItem(0, ItemStack.EMPTY);
        this.lastEnchantment = null;
        this.levelCost.set(0);
        if (!book.isEmpty()) {
            ItemEnchantments enchantments = enchanted.getTagEnchantments();
            List<Object2IntMap.Entry<Holder<Enchantment>>> map = enchantments.entrySet().stream().toList();
            if (map.size() >= 2) {
                ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
                this.lastEnchantment = map.getLast();
                ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                mutable.set(this.lastEnchantment.getKey(), this.lastEnchantment.getIntValue());
                EnchantmentHelper.setEnchantments(stack, mutable.toImmutable());
                this.result.setItem(0, stack);
                this.levelCost.set(calculateExpLevel(this.lastEnchantment));
            }
        }
        super.slotsChanged(inventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot == 2) {
                if (!this.moveItemStackTo(itemStack2, 3, 39, true))
                    return ItemStack.EMPTY;
                slot2.onQuickCraft(itemStack2, itemStack);
            } else if (slot != 0 && slot != 1) {
                if (!this.input.getItem(0).isEmpty() && !this.input.getItem(1).isEmpty()) {
                    if (slot >= 3 && slot < 30) {
                        if (!this.moveItemStackTo(itemStack2, 30, 39, false))
                            return ItemStack.EMPTY;
                    } else if (slot >= 30 && slot < 39 && !this.moveItemStackTo(itemStack2, 3, 30, false))
                        return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(itemStack2, 0, 2, false))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemStack2, 3, 39, false))
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
        return stillValid(this.context, player, ROBlocks.ENCHANT_SEPARATE_TABLE.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.context.execute((world, pos) -> this.clearContainer(player, this.input));
    }

    public int getLevelCost() {
        return this.levelCost.get();
    }

    public static int calculateExpLevel(Object2IntMap.Entry<Holder<Enchantment>> enchantment) {
        return 10 / enchantment.getKey().value().getWeight() + enchantment.getIntValue();
    }
}
