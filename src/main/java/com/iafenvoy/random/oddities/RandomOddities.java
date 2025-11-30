package com.iafenvoy.random.oddities;

import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(RandomOddities.MOD_ID)
public final class RandomOddities {
    public static final String MOD_ID = "random_oddities";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RandomOddities(IEventBus bus) {
        ConfigManager.getInstance().registerConfigHandler(ROCommonConfig.INSTANCE);
        ServerConfigManager.registerServerConfig(ROCommonConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);

        ROAttachmentTypes.REGISTRY.register(bus);
        ROBlockEntities.REGISTRY.register(bus);
        ROBlocks.REGISTRY.register(bus);
        RODataComponents.REGISTRY.register(bus);
        ROEntities.REGISTRY.register(bus);
        ROItemGroups.REGISTRY.register(bus);
        ROItems.REGISTRY.register(bus);
        ROScreenHandlers.REGISTRY.register(bus);
        ROSerializers.REGISTRY.register(bus);
    }
}
