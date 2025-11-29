package com.iafenvoy.random.oddities.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BabyCapsuleItem extends Item {
    public BabyCapsuleItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (target instanceof AgeableMob mob) {
            mob.setBaby(true);
            if (!player.isCreative()) stack.shrink(1);
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        } else return super.interactLivingEntity(stack, player, target, hand);
    }
}
