package com.iafenvoy.random.oddities.item;

import com.iafenvoy.random.oddities.registry.ROBlocks;
import com.iafenvoy.random.oddities.registry.RODataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlasticChairItem extends BlockItem implements Equipable {
    public PlasticChairItem(Block block) {
        super(block, new Properties().stacksTo(16).component(RODataComponents.DYE_COLOR, DyeColor.WHITE));
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof LivingEntity living && living.getItemBySlot(this.getEquipmentSlot()) == stack)
            stack.set(RODataComponents.CHAIR_WARNING, Unit.INSTANCE);
        else stack.remove(RODataComponents.CHAIR_WARNING);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (stack.has(RODataComponents.CHAIR_WARNING))
            tooltip.add(Component.translatable("block.random_oddities.plastic_chair.tooltip.2"));
        else tooltip.add(Component.translatable("block.random_oddities.plastic_chair.tooltip.1"));
    }

    public static DyeColor readColor(ItemStack stack) {
        return stack.getOrDefault(RODataComponents.DYE_COLOR, DyeColor.WHITE);
    }

    public static ItemStack create(DyeColor color) {
        return writeColor(new ItemStack(ROBlocks.PLASTIC_CHAIR.get()), color);
    }

    public static ItemStack writeColor(ItemStack stack, DyeColor color) {
        stack.set(RODataComponents.DYE_COLOR, color);
        return stack;
    }
}
