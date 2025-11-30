package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.render.DynamicItemRenderer;
import com.iafenvoy.random.oddities.render.block.PlasticChairBlockEntityRenderer;
import com.iafenvoy.random.oddities.render.entity.NoopEntityRenderer;
import com.iafenvoy.random.oddities.screen.gui.AutoCraftingScreen;
import com.iafenvoy.random.oddities.screen.gui.BreakerScreen;
import com.iafenvoy.random.oddities.screen.gui.EnchantSeparateTableScreen;
import com.iafenvoy.random.oddities.screen.gui.ItemsDeleterScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public final class RORenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ROEntities.CHAIR.get(), NoopEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ROBlockEntities.PLASTIC_CHAIR.get(), ctx -> new PlasticChairBlockEntityRenderer());
    }

    @SubscribeEvent
    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        event.register(ROScreenHandlers.ITEMS_DELETER.get(), ItemsDeleterScreen::new);
        event.register(ROScreenHandlers.ENCHANT_SEPARATE_TABLE.get(), EnchantSeparateTableScreen::new);
        event.register(ROScreenHandlers.AUTO_CRAFTER.get(), AutoCraftingScreen::new);
        event.register(ROScreenHandlers.BREAKER.get(), BreakerScreen::new);
    }

    public static void registerItemRenderers() {
        DynamicItemRenderer.RENDERERS.put(ROBlocks.PLASTIC_CHAIR.get().asItem(), new PlasticChairBlockEntityRenderer());
    }
}
