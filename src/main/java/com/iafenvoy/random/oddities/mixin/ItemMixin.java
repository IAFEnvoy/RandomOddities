package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.data.ElytraCombineHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void appendCombinedElytraTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltips, TooltipFlag tooltipFlag, CallbackInfo ci) {
        ItemStack elytra = ElytraCombineHelper.get(stack);
        if (!elytra.isEmpty()) tooltips.add(Component.translatable("item.minecraft.elytra"));
    }
}
