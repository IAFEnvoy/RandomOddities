package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.registry.ROBlocks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "has", at = @At("HEAD"), cancellable = true)
    private static void stuckInPlasticChair(ItemStack stack, DataComponentType<?> componentType, CallbackInfoReturnable<Boolean> cir) {
        if (componentType == EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE && stack.is(ROBlocks.PLASTIC_CHAIR.get().asItem()))
            cir.setReturnValue(true);
    }
}
