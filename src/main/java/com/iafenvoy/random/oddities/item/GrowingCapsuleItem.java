package com.iafenvoy.random.oddities.item;

import com.iafenvoy.random.oddities.registry.ROAttachmentTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GrowingCapsuleItem extends Item {
    public GrowingCapsuleItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, LivingEntity target, @NotNull InteractionHand hand) {
        target.setData(ROAttachmentTypes.LOCK_AGE, false);
        if (!player.isCreative()) stack.shrink(1);
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }
}
