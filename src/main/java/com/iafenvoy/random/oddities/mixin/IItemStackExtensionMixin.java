package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.data.ElytraCombineHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IItemStackExtension.class)
public interface IItemStackExtensionMixin {
    @SuppressWarnings("ShadowModifiers")
    @Shadow
    ItemStack self();

    @Inject(method = "canElytraFly", at = @At("HEAD"), cancellable = true)
    private void handleCombinedElytraFlyCheck(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.self();
        if (!ElytraCombineHelper.get(stack).isEmpty() && stack.getDamageValue() < stack.getMaxDamage() - 1)
            cir.setReturnValue(true);
    }

    @Inject(method = "elytraFlightTick", at = @At("HEAD"), cancellable = true)
    private void handleCombinedElytraFlyTick(LivingEntity entity, int flightTicks, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.self();
        if (!ElytraCombineHelper.get(stack).isEmpty()) {
            if (!entity.level().isClientSide) {
                int nextFlightTick = flightTicks + 1;
                if (nextFlightTick % 10 == 0) {
                    if (nextFlightTick % 20 == 0) stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
                    entity.gameEvent(GameEvent.ELYTRA_GLIDE);
                }
            }
            cir.setReturnValue(true);
        }
    }
}
