package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.data.ElytraCombineHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

    @ModifyExpressionValue(method = "tryToStartFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private boolean handleFlying(boolean original) {
        return original || !ElytraCombineHelper.get(this.getItemBySlot(EquipmentSlot.CHEST)).isEmpty();
    }
}
