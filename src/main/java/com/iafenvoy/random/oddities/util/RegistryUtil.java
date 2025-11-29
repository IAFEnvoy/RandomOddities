package com.iafenvoy.random.oddities.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class RegistryUtil {
    public static <T> Holder<T> get(HolderLookup.Provider provider, ResourceKey<T> key) {
        return provider.holderOrThrow(key);
    }

    public static Holder<Enchantment> enchantment(HolderLookup.Provider access, ResourceKey<Enchantment> key) {
        return get(access, key);
    }
}
