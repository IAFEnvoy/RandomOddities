package com.iafenvoy.random.oddities.screen.gui;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.screen.handler.ItemsDeleterScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ItemsDeleterScreen extends AbstractContainerScreen<ItemsDeleterScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "textures/gui/items_deleter.png");

    public ItemsDeleterScreen(ItemsDeleterScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 133;
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
    }
}
