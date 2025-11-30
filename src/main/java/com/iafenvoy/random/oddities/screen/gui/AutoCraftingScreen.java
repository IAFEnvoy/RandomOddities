package com.iafenvoy.random.oddities.screen.gui;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.screen.handler.AutoCraftingScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AutoCraftingScreen extends AbstractContainerScreen<AutoCraftingScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "textures/gui/auto_crafter.png");

    public AutoCraftingScreen(AutoCraftingScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (!this.menu.hasRecipe1()) context.blit(TEXTURE, i + 42, j + 75, this.imageWidth, 0, 21, 28);
        if (!this.menu.hasRecipe2()) context.blit(TEXTURE, i + 114, j + 75, this.imageWidth, 0, 21, 28);
    }
}
