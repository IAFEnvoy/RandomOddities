package com.iafenvoy.random.oddities.item;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.registry.RODataComponents;
import com.iafenvoy.random.oddities.screen.handler.AutoCraftingScreenHandler;
import com.iafenvoy.random.oddities.screen.inventory.ImplementedInventory;
import com.iafenvoy.random.oddities.screen.inventory.InventoryUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AutoCrafterItem extends Item implements MenuProvider {
    public AutoCrafterItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC).component(RODataComponents.AUTO_CRAFTER, new Pair<>(NonNullList.withSize(9, ItemStack.EMPTY), NonNullList.withSize(9, ItemStack.EMPTY))));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!ROCommonConfig.INSTANCE.items.autoCrafter.getValue()) return;
        if (!world.isClientSide && entity instanceof Player player) {
            Inventory inventory = player.getInventory();
            Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> inventories = getStacks(stack);
            for (NonNullList<ItemStack> stacks : List.of(inventories.getFirst(), inventories.getSecond())) {
                if (stacks.isEmpty()) continue;
                ImplementedInventory ingredients = ImplementedInventory.of(stacks);
                if (InventoryUtil.hasAllItems(inventory, ingredients)) {
                    Optional<RecipeHolder<CraftingRecipe>> optional = world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, CraftingInput.of(3, 3, stacks), world);
                    if (optional.isPresent()) {
                        ImplementedInventory result = ImplementedInventory.of(NonNullList.of(ItemStack.EMPTY, optional.get().value().getResultItem(world.registryAccess())));
                        while (InventoryUtil.canFitItems(inventory, result, ingredients)) {
                            InventoryUtil.removeItems(inventory, ingredients);
                            InventoryUtil.insertItems(inventory, result);
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (!world.isClientSide) user.openMenu(this);
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("screen.%s.auto_crafter".formatted(RandomOddities.MOD_ID));
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, Player player) {
        return new AutoCraftingScreenHandler(syncId, playerInventory, ContainerLevelAccess.create(player.level(), player.blockPosition()));
    }

    public static Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> getStacks(ItemStack stack) {
        return stack.getOrDefault(RODataComponents.AUTO_CRAFTER, new Pair<>(NonNullList.withSize(9, ItemStack.EMPTY), NonNullList.withSize(9, ItemStack.EMPTY)));
    }

    public static void setStacks(ItemStack stack, Pair<NonNullList<ItemStack>, NonNullList<ItemStack>> stacks) {
        stack.set(RODataComponents.AUTO_CRAFTER, stacks);
    }
}
