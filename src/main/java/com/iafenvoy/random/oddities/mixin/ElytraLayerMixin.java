package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.data.ElytraCombineHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@OnlyIn(Dist.CLIENT)
@Mixin(ElytraLayer.class)
public class ElytraLayerMixin {
    @ModifyExpressionValue(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack handlerCombinedElytra(ItemStack original) {
        ItemStack elytra = ElytraCombineHelper.get(original);
        return elytra.isEmpty() ? original : elytra;
    }
}
