package com.iafenvoy.random.oddities.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

public class ToolUtil {
    public static float getDigSpeed(RegistryAccess access, BlockState state, ItemStack stack) {
        float speed = stack.getDestroySpeed(state);
        if (speed > 1.0F) {
            int level = stack.getEnchantmentLevel(RegistryUtil.enchantment(access, Enchantments.EFFICIENCY));
            if (level > 0 && !stack.isEmpty())
                speed += level * level + 1;
        }
        return speed;
    }
}
