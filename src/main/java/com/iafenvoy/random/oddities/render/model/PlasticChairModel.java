package com.iafenvoy.random.oddities.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
@OnlyIn(Dist.CLIENT)
public class PlasticChairModel extends EntityModel<Entity> {
    private final ModelPart bb_main;

    public PlasticChairModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition bb_main = modelPartData.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-8, 24).addBox(-4.0F, -13.6F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(-3, 21).addBox(-7.0F, 0.0F, -7.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(8, 21).addBox(4.0F, 0.0F, -7.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(8, 14).addBox(4.0F, 0.0F, 4.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(-3, 14).addBox(-7.0F, 0.0F, 4.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        bb_main.addOrReplaceChild("side_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.2182F));
        bb_main.addOrReplaceChild("side_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 2.9234F, 0.0F, 3.1416F));
        bb_main.addOrReplaceChild("side_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.2182F));
        bb_main.addOrReplaceChild("side_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, 0.0F, 14.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, -0.2182F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack matrices, @NotNull VertexConsumer vertexConsumer, int light, int overlay, int color) {
        this.bb_main.render(matrices, vertexConsumer, light, overlay, color);
    }
}