package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.registry.ROEnchantments;
import com.iafenvoy.random.oddities.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("TAIL"), cancellable = true)
    private static void doReplant(BlockState state, ServerLevel world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (!ROCommonConfig.INSTANCE.enchantments.autoPlant.getValue()) return;
        List<ItemStack> loots = cir.getReturnValue();
        if (entity instanceof Player player && state.getBlock() instanceof CropBlock crop && crop.getCloneItemStack(world, pos, state).getItem() instanceof BlockItem seed && seed != Items.AIR && tool.getEnchantmentLevel(RegistryUtil.enchantment(world.registryAccess(), ROEnchantments.AUTO_PLANT)) > 0) {
            ItemStack target = ItemStack.EMPTY;
            for (ItemStack stack : loots)
                if (!stack.isEmpty() && stack.is(seed)) {
                    target = stack;
                    break;
                }
            if (target.isEmpty()) {
                Inventory inventory = player.getInventory();
                int index = inventory.findSlotMatchingItem(new ItemStack(seed));
                if (index == -1) return;
                else target = inventory.getItem(index);
            } else if (loots.size() == 1 && target.getCount() == 1) return;
            seed.place(new DirectionalPlaceContext(world, pos, Direction.DOWN, target, Direction.UP));
        }
    }
}
