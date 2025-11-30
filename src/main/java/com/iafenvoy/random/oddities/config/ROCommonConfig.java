package com.iafenvoy.random.oddities.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.random.oddities.RandomOddities;
import net.minecraft.resources.ResourceLocation;

public class ROCommonConfig extends AutoInitConfigContainer {
    public static final ROCommonConfig INSTANCE = new ROCommonConfig();

    public final BlocksConfig blocks = new BlocksConfig();
    public final ItemsConfig items = new ItemsConfig();
    public final EnchantmentsConfig enchantments = new EnchantmentsConfig();

    public ROCommonConfig() {
        super(ResourceLocation.tryBuild(RandomOddities.MOD_ID, "common"), "screen.%s.common.title".formatted(RandomOddities.MOD_ID), "./config/random_oddities.json");
    }

    public static class BlocksConfig extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Boolean> enchantSeparateTable = new BooleanEntry("config.%s.enable_enchant_separate_table".formatted(RandomOddities.MOD_ID), true).json("enable_enchant_separate_table");
        public final IConfigEntry<Boolean> breaker = new BooleanEntry("config.%s.enable_breaker".formatted(RandomOddities.MOD_ID), true).json("enable_breaker");
        public final IConfigEntry<Boolean> placer = new BooleanEntry("config.%s.enable_placer".formatted(RandomOddities.MOD_ID), true).json("enable_placer");

        public BlocksConfig() {
            super("blocks", "config.category.%s.blocks".formatted(RandomOddities.MOD_ID));
        }
    }

    public static class ItemsConfig extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Boolean> autoCrafter = new BooleanEntry("config.%s.enable_auto_crafter".formatted(RandomOddities.MOD_ID), true).json("enable_auto_crafter");
        public final IConfigEntry<Boolean> itemsDeleter = new BooleanEntry("config.%s.enable_items_deleter".formatted(RandomOddities.MOD_ID), true).json("enable_items_deleter");

        public ItemsConfig() {
            super("items", "config.category.%s.items".formatted(RandomOddities.MOD_ID));
        }
    }

    public static class EnchantmentsConfig extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Boolean> autoPlant = new BooleanEntry("config.%s.enable_auto_plant".formatted(RandomOddities.MOD_ID), true).json("enable_auto_plant");
        public final IConfigEntry<Boolean> kindness = new BooleanEntry("config.%s.enable_kindness".formatted(RandomOddities.MOD_ID), true).json("enable_kindness");

        public EnchantmentsConfig() {
            super("enchantments", "config.category.%s.enchantments".formatted(RandomOddities.MOD_ID));
        }
    }
}
