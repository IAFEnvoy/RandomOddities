package com.iafenvoy.random.oddities.screen.gui;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.screen.handler.BreakerScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BreakerScreen extends AbstractContainerScreen<BreakerScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "textures/gui/breaker.png");

    public BreakerScreen(BreakerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
