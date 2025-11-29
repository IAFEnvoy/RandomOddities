package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.registry.ROAttachmentTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AgeableMob.class)
public class AgeableMobMixin extends PathfinderMob {
    protected AgeableMobMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AgeableMob;isAlive()Z"))
    private boolean handleAgeTick(boolean original) {
        return !this.getData(ROAttachmentTypes.LOCK_AGE) && original;
    }
}
