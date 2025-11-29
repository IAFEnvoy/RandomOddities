package com.iafenvoy.random.oddities;

import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.registry.RORenderers;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(Dist.CLIENT)
public final class RandomOdditiesClient {
    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        RORenderers.registerItemRenderers();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (container, parent) -> new ConfigSelectScreen<>(Component.translatable("screen.random_oddities.title"), parent, ROCommonConfig.INSTANCE, null));
    }
}
