package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.PlasticChairItem;
import com.iafenvoy.random.oddities.util.RegistryUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Supplier;

public final class ROItemGroups {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RandomOddities.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.%s.main".formatted(RandomOddities.MOD_ID)))
            .icon(() -> new ItemStack(ROBlocks.ENCHANT_SEPARATE_TABLE.get()))
            .displayItems((ctx, entries) -> {
                EnchantedBookItem.createForEnchantment(new EnchantmentInstance(RegistryUtil.enchantment(ctx.holders(), ROEnchantments.AUTO_PLANT), 1));
                EnchantedBookItem.createForEnchantment(new EnchantmentInstance(RegistryUtil.enchantment(ctx.holders(), ROEnchantments.KINDNESS), 1));
                ROItems.REGISTRY.getEntries().stream().map(Supplier::get).filter(x -> !(x instanceof PlasticChairItem)).forEach(entries::accept);
                Arrays.stream(DyeColor.values()).map(PlasticChairItem::create).forEach(entries::accept);
            })
            .build());

    public static <T extends CreativeModeTab> DeferredHolder<CreativeModeTab, T> register(String id, Supplier<T> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
