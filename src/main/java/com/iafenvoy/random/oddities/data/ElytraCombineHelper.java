package com.iafenvoy.random.oddities.data;

import com.iafenvoy.random.oddities.registry.RODataComponents;
import net.minecraft.world.item.ItemStack;

public final class ElytraCombineHelper {
    public static ItemStack combine(ItemStack target, ItemStack elytra) {
        target.set(RODataComponents.ELYTRA, elytra);
        return target;
    }

    public static ItemStack get(ItemStack target) {
        return target.getOrDefault(RODataComponents.ELYTRA, ItemStack.EMPTY);
    }
}
