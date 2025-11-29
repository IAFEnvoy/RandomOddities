package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.AutoCraftingItem;
import com.iafenvoy.random.oddities.item.ItemsDeleterItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ROItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(RandomOddities.MOD_ID);

    public static final DeferredItem<Item> ITEMS_DELETER = register("items_deleter", ItemsDeleterItem::new);
    public static final DeferredItem<Item> AUTO_CRAFTING = register("auto_crafting", AutoCraftingItem::new);
    public static final DeferredItem<Item> ELYTRA_COMBINE_SMITHING_TEMPLATE = register("elytra_combine_smithing_template", () -> new Item(new Item.Properties()));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> supplier) {
        return register(id, supplier, true);
    }

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> supplier, boolean appendGroup) {
        return REGISTRY.register(id, supplier);
    }
}
