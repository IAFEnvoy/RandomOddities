package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.data.SingleStackComponent;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public final class RODataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, RandomOddities.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackComponent>> ELYTRA = register("elytra", () -> DataComponentType.<SingleStackComponent>builder().persistent(SingleStackComponent.CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Pair<NonNullList<ItemStack>, NonNullList<ItemStack>>>> AUTO_CRAFTING = register("auto_crafting", () -> DataComponentType.<Pair<NonNullList<ItemStack>, NonNullList<ItemStack>>>builder().persistent(Codec.pair(NonNullList.codecOf(ItemStack.OPTIONAL_CODEC), NonNullList.codecOf(ItemStack.OPTIONAL_CODEC))).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemStack>>> ITEM_DELETER = register("item_deleter", () -> DataComponentType.<List<ItemStack>>builder().persistent(ItemStack.OPTIONAL_CODEC.listOf()).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyeColor>> DYE_COLOR = register("dye_color", () -> DataComponentType.<DyeColor>builder().persistent(DyeColor.CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> CHAIR_WARNING = register("chair_warning", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).build());

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String id, Supplier<DataComponentType<T>> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
