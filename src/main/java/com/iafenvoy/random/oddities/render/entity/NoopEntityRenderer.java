package com.iafenvoy.random.oddities.render.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NoopEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    public NoopEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(@NotNull T entity, @NotNull Frustum frustum, double x, double y, double z) {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return ResourceLocation.fromNamespaceAndPath("", "");
    }
}
