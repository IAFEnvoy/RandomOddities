package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.screen.handler.AutoCraftingScreenHandler;
import com.iafenvoy.random.oddities.screen.handler.BreakerScreenHandler;
import com.iafenvoy.random.oddities.screen.handler.EnchantSeparateTableScreenHandler;
import com.iafenvoy.random.oddities.screen.handler.ItemsDeleterScreenHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ROScreenHandlers {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, RandomOddities.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ItemsDeleterScreenHandler>> ITEMS_DELETER = register("items_deleter", () -> new MenuType<>(ItemsDeleterScreenHandler::new, FeatureFlagSet.of(FeatureFlags.VANILLA)));
    public static final DeferredHolder<MenuType<?>, MenuType<EnchantSeparateTableScreenHandler>> ENCHANT_SEPARATE_TABLE = register("enchant_separate_table", () -> new MenuType<>(EnchantSeparateTableScreenHandler::new, FeatureFlagSet.of(FeatureFlags.VANILLA)));
    public static final DeferredHolder<MenuType<?>, MenuType<AutoCraftingScreenHandler>> AUTO_CRAFTING = register("auto_crafting", () -> new MenuType<>(AutoCraftingScreenHandler::new, FeatureFlagSet.of(FeatureFlags.VANILLA)));
    public static final DeferredHolder<MenuType<?>, MenuType<BreakerScreenHandler>> BREAKER = register("breaker", () -> new MenuType<>(BreakerScreenHandler::new, FeatureFlagSet.of(FeatureFlags.VANILLA)));

    public static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, Supplier<MenuType<T>> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
