package com.iafenvoy.random.oddities.data;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

public record SingleStackComponent(ItemStack stack) {
    public static final Codec<SingleStackComponent> CODEC = ItemStack.OPTIONAL_CODEC.xmap(SingleStackComponent::new, SingleStackComponent::stack);
}
