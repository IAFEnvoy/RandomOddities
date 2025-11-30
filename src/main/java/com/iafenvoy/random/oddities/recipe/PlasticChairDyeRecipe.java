package com.iafenvoy.random.oddities.recipe;

import com.iafenvoy.random.oddities.item.PlasticChairItem;
import com.iafenvoy.random.oddities.registry.ROBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlasticChairDyeRecipe implements CraftingRecipe {
    @Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        Optional<DyeColor> color = findColor(input);
        if (color.isEmpty()) return false;
        boolean have = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ROBlocks.PLASTIC_CHAIR.asItem())) have = true;
            else if (!stack.isEmpty() && !(stack.getItem() instanceof DyeItem)) return false;
        }
        return have;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider provider) {
        Optional<DyeColor> color = findColor(input);
        if (color.isEmpty()) return ItemStack.EMPTY;
        int count = 0;
        for (int i = 0; i < input.size(); i++)
            if (input.getItem(i).is(ROBlocks.PLASTIC_CHAIR.asItem()))
                count++;
        return PlasticChairItem.writeColor(ROBlocks.PLASTIC_CHAIR.toStack(count), color.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    private static Optional<DyeColor> findColor(CraftingInput input) {
        DyeColor color = null;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof DyeItem dye) {
                if (color != null) return Optional.empty();
                color = dye.getDyeColor();
            }
        }
        return Optional.ofNullable(color);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return ROBlocks.PLASTIC_CHAIR.toStack();
    }

    @Override
    public @NotNull RecipeSerializer<PlasticChairDyeRecipe> getSerializer() {
        return Serializer.INSTANCE;
    }

    public enum Serializer implements RecipeSerializer<PlasticChairDyeRecipe> {
        INSTANCE;

        @Override
        public @NotNull MapCodec<PlasticChairDyeRecipe> codec() {
            return MapCodec.unit(PlasticChairDyeRecipe::new);
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, PlasticChairDyeRecipe> streamCodec() {
            return new StreamCodec<>() {
                @Override
                public @NotNull PlasticChairDyeRecipe decode(@NotNull RegistryFriendlyByteBuf buf) {
                    return new PlasticChairDyeRecipe();
                }

                @Override
                public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull PlasticChairDyeRecipe recipe) {
                }
            };
        }
    }
}
