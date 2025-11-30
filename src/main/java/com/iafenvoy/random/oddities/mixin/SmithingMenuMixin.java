package com.iafenvoy.random.oddities.mixin;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.recipe.ElytraCombineSmithingRecipe;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(SmithingMenu.class)
public class SmithingMenuMixin {
    @Shadow
    @Final
    @Mutable
    private List<RecipeHolder<SmithingRecipe>> recipes;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void addElytraCombineRecipe(int syncId, Inventory playerInventory, ContainerLevelAccess context, CallbackInfo ci) {
        List<RecipeHolder<SmithingRecipe>> list = new LinkedList<>(this.recipes);
        list.addFirst(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "elytra_combine"), new ElytraCombineSmithingRecipe()));
        this.recipes = list;
    }

    @ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/List;"))
    private List<RecipeHolder<SmithingRecipe>> addElytraCombineRecipe(List<RecipeHolder<SmithingRecipe>> original) {
        List<RecipeHolder<SmithingRecipe>> list = new LinkedList<>(original);
        list.addFirst(new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "elytra_combine"), new ElytraCombineSmithingRecipe()));
        return list;
    }
}
