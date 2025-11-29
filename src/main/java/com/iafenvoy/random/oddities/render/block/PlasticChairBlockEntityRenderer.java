package com.iafenvoy.random.oddities.render.block;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.PlasticChairItem;
import com.iafenvoy.random.oddities.item.block.entity.PlasticChairBlockEntity;
import com.iafenvoy.random.oddities.render.DynamicItemRenderer;
import com.iafenvoy.random.oddities.render.model.PlasticChairModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PlasticChairBlockEntityRenderer implements BlockEntityRenderer<PlasticChairBlockEntity>, DynamicItemRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "textures/entity/plastic_chair.png");
    private final PlasticChairModel model = new PlasticChairModel(PlasticChairModel.getTexturedModelData().bakeRoot());

    @Override
    public void render(PlasticChairBlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
        this.render(entity.getColors(), matrices, vertexConsumers, light, overlay);
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        this.render(List.of(PlasticChairItem.readColor(stack)), matrices, vertexConsumers, light, overlay);
    }

    private void render(List<DyeColor> dyeColor, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.translate(0.5, -1.501, -0.5);
        for (DyeColor color : dyeColor) {
            int c = color.getTextureDiffuseColor();
            this.model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entityCutout(TEXTURE)), light, overlay, c);
            matrices.translate(0, -1.0 / PlasticChairBlockEntity.MAX_CHAIRS, 0);
        }
        matrices.popPose();
    }
}
