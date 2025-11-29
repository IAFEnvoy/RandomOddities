package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ROEnchantments {
    public static final ResourceKey<Enchantment> AUTO_PLANT = key("auto_plant");
    public static final ResourceKey<Enchantment> KINDNESS = key("kindness");

    public static ResourceKey<Enchantment> key(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, id));
    }
}
