package com.iafenvoy.random.oddities.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface DynamicItemRenderer {
    Map<Item, DynamicItemRenderer> RENDERERS = new HashMap<>();

    void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light, int overlay);
}
