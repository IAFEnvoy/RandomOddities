package com.iafenvoy.random.oddities.screen.gui;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.screen.handler.EnchantSeparateTableScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class EnchantSeparateTableScreen extends AbstractContainerScreen<EnchantSeparateTableScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RandomOddities.MOD_ID, "textures/gui/enchant_separate_table.png");
    private final Player player;

    public EnchantSeparateTableScreen(EnchantSeparateTableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.player = inventory.player;
        this.imageHeight = 139;
        this.inventoryLabelY = this.imageHeight - 93;
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBg(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem())
            context.blit(TEXTURE, i + 92, j + 16, this.imageWidth, 0, 28, 21);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics context, int mouseX, int mouseY) {
        super.renderLabels(context, mouseX, mouseY);
        int level = this.menu.getLevelCost();
        if (level > 0) {
            int color = 8453920;
            Component text;
            if (!this.menu.getSlot(2).hasItem())
                text = null;
            else {
                text = Component.translatable("container.repair.cost", level);
                if (!this.menu.getSlot(2).mayPickup(this.player))
                    color = 16736352;
            }
            if (text != null) {
                int width = this.imageWidth - 8 - this.font.width(text) - 2;
                context.fill(width - 2, 39, this.imageWidth - 8, 51, 1325400064);
                context.drawString(this.font, text, width, 41, color);
            }
        }
    }
}
