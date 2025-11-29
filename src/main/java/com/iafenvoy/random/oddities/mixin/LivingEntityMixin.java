package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.data.ElytraCombineHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private boolean handleFlyingCheck(boolean original) {
        return original || !ElytraCombineHelper.get(this.getItemBySlot(EquipmentSlot.CHEST)).isEmpty();
    }

    @ModifyExpressionValue(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;elytraFlightTick(Lnet/minecraft/world/entity/LivingEntity;I)Z"))
    private boolean handleFlyingTick(boolean original) {
        return original || !ElytraCombineHelper.get(this.getItemBySlot(EquipmentSlot.CHEST)).isEmpty();
    }
}
