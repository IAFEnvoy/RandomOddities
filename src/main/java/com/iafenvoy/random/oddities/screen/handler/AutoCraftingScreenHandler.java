package com.iafenvoy.random.oddities.screen.handler;

import com.iafenvoy.random.oddities.item.AutoCrafterItem;
import com.iafenvoy.random.oddities.registry.ROItems;
import com.iafenvoy.random.oddities.registry.ROScreenHandlers;
import com.iafenvoy.random.oddities.screen.slot.DisplayOnlySlot;
import com.iafenvoy.random.oddities.screen.slot.SingleFakeItemSlot;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutoCraftingScreenHandler extends AbstractContainerMenu {
    private final Inventory playerInventory;
    private final ContainerLevelAccess context;
    private final CraftingContainer input1, input2;
    private final ResultContainer result1, result2;

    public AutoCraftingScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    public AutoCraftingScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(ROScreenHandlers.AUTO_CRAFTER.get(), syncId);
        this.playerInventory = playerInventory;
        this.context = context;
        Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> inventories = AutoCrafterItem.getStacks(playerInventory.getSelected());
        this.input1 = this.createInventory(inventories.getFirst());
        this.input2 = this.createInventory(inventories.getSecond());
        this.result1 = new ResultContainer();
        this.result2 = new ResultContainer();
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                this.addSlot(new SingleFakeItemSlot(this, this.input1, j + i * 3, 26 + j * 18, 18 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                this.addSlot(new SingleFakeItemSlot(this, this.input2, j + i * 3, 98 + j * 18, 18 + i * 18));
        this.addSlot(new DisplayOnlySlot(this.result1, 0, 44, 108));
        this.addSlot(new DisplayOnlySlot(this.result2, 0, 116, 108));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        this.updateRecipes();
    }

    private TransientCraftingContainer createInventory(NonNullList<ItemStack> inventories) {
        NonNullList<ItemStack> list = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(inventories.size(), 9); i++) list.set(i, inventories.get(i));
        return new TransientCraftingContainer(this, 3, 3, list);
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        this.updateRecipes();
    }

    private void updateRecipes() {
        this.context.execute((world, pos) -> {
            RecipeManager manager = world.getRecipeManager();
            Optional<RecipeHolder<CraftingRecipe>> recipe1 = manager.getRecipeFor(RecipeType.CRAFTING, this.input1.asCraftInput(), world);
            if (recipe1.isPresent()) {
                RecipeHolder<CraftingRecipe> recipe = recipe1.get();
                this.result1.setItem(0, recipe.value().getResultItem(world.registryAccess()));
                this.result1.setRecipeUsed(recipe);
            } else {
                this.result1.setItem(0, ItemStack.EMPTY);
                this.result1.setRecipeUsed(null);
            }
            Optional<RecipeHolder<CraftingRecipe>> recipe2 = manager.getRecipeFor(RecipeType.CRAFTING, this.input2.asCraftInput(), world);
            if (recipe2.isPresent()) {
                RecipeHolder<CraftingRecipe> recipe = recipe2.get();
                this.result2.setItem(0, recipe.value().getResultItem(world.registryAccess()));
                this.result2.setRecipeUsed(recipe);
            } else {
                this.result2.setItem(0, ItemStack.EMPTY);
                this.result2.setRecipeUsed(null);
            }
        });
        this.broadcastChanges();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(ROItems.AUTO_CRAFTER.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        AutoCrafterItem.setStacks(this.playerInventory.getSelected(), new Pair<>(toDefaultedList(this.input1), toDefaultedList(this.input2)));
    }

    private static NonNullList<ItemStack> toDefaultedList(CraftingContainer inventory) {
        return NonNullList.of(ItemStack.EMPTY, inventory.getItems().toArray(new ItemStack[0]));
    }

    public boolean hasRecipe1() {
        return !this.result1.getItem(0).isEmpty();
    }

    public boolean hasRecipe2() {
        return !this.result2.getItem(0).isEmpty();
    }
}
