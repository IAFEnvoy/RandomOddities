package com.iafenvoy.random.oddities.item;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.registry.RODataComponents;
import com.iafenvoy.random.oddities.screen.handler.ItemsDeleterScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemsDeleterItem extends Item implements MenuProvider {
    public ItemsDeleterItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC).component(RODataComponents.ITEM_DELETER, List.of()));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!ROCommonConfig.INSTANCE.items.itemsDeleter.getValue()) return;
        if (!(entity instanceof Player player)) return;
        List<ItemStack> stacks = stack.getOrDefault(RODataComponents.ITEM_DELETER, List.of());
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack s = inventory.getItem(i);
            if (stacks.stream().anyMatch(x -> ItemStack.isSameItemSameComponents(x, s))) {
                inventory.setItem(i, ItemStack.EMPTY);
                s.setCount(0);
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
        return Component.translatable("screen.%s.items_deleter".formatted(RandomOddities.MOD_ID));
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ItemsDeleterScreenHandler(syncId, playerInventory);
    }
}
