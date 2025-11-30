package com.iafenvoy.random.oddities.recipe;

import com.iafenvoy.random.oddities.data.ElytraCombineHelper;
import com.iafenvoy.random.oddities.registry.ROItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ElytraCombineSmithingRecipe implements SmithingRecipe {
    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.is(ROItems.ELYTRA_COMBINE_SMITHING_TEMPLATE.get());
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armor && armor.getEquipmentSlot() == EquipmentSlot.CHEST && ElytraCombineHelper.get(stack).isEmpty();
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return stack.getItem() instanceof ElytraItem;
    }

    @Override
    public boolean matches(SmithingRecipeInput inventory, @NotNull Level world) {
        return this.isTemplateIngredient(inventory.getItem(0)) && this.isBaseIngredient(inventory.getItem(1)) && this.isAdditionIngredient(inventory.getItem(2));
    }

    @Override
    public @NotNull ItemStack assemble(SmithingRecipeInput inventory, HolderLookup.@NotNull Provider registries) {
        ItemStack itemStack = inventory.getItem(1).copyWithCount(1);
        if (this.isTemplateIngredient(inventory.getItem(0)) && this.isBaseIngredient(itemStack))
            return ElytraCombineHelper.combine(itemStack, inventory.getItem(2).copy());
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registryManager) {
        return ElytraCombineHelper.combine(new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.ELYTRA));
    }

    @Override
    public @NotNull RecipeSerializer<ElytraCombineSmithingRecipe> getSerializer() {
        return new RecipeSerializer<>() {
            @Override
            public @NotNull MapCodec<ElytraCombineSmithingRecipe> codec() {
                return MapCodec.unit(ElytraCombineSmithingRecipe::new);
            }

            @Override
            public @NotNull StreamCodec<RegistryFriendlyByteBuf, ElytraCombineSmithingRecipe> streamCodec() {
                return StreamCodec.unit(new ElytraCombineSmithingRecipe());
            }
        };
    }
}
