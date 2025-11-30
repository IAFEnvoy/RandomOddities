package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ROItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(RandomOddities.MOD_ID);

    public static final DeferredItem<Item> AUTO_CRAFTER = register("auto_crafter", AutoCrafterItem::new);
    public static final DeferredItem<Item> ITEMS_DELETER = register("items_deleter", ItemsDeleterItem::new);
    public static final DeferredItem<Item> ELYTRA_COMBINE_SMITHING_TEMPLATE = register("elytra_combine_smithing_template", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CAPSULE = register("capsule", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BABY_CAPSULE = register("baby_capsule", BabyCapsuleItem::new);
    public static final DeferredItem<Item> GROWING_CAPSULE = register("growing_capsule", GrowingCapsuleItem::new);
    public static final DeferredItem<Item> STOP_GROWING_CAPSULE = register("stop_growing_capsule", StopGrowingCapsuleItem::new);

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> supplier) {
        return register(id, supplier, true);
    }

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> supplier, boolean appendGroup) {
        return REGISTRY.register(id, supplier);
    }
}
