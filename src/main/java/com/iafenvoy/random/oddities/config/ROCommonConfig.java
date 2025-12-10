package com.iafenvoy.random.oddities.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
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
        public final BooleanEntry enchantSeparateTable = BooleanEntry.builder("config.%s.enable_enchant_separate_table".formatted(RandomOddities.MOD_ID), true).json("enable_enchant_separate_table").build();
        public final BooleanEntry breaker = BooleanEntry.builder("config.%s.enable_breaker".formatted(RandomOddities.MOD_ID), true).json("enable_breaker").build();
        public final BooleanEntry placer = BooleanEntry.builder("config.%s.enable_placer".formatted(RandomOddities.MOD_ID), true).json("enable_placer").build();

        public BlocksConfig() {
            super("blocks", "config.category.%s.blocks".formatted(RandomOddities.MOD_ID));
        }
    }

    public static class ItemsConfig extends AutoInitConfigCategoryBase {
        public final BooleanEntry autoCrafter = BooleanEntry.builder("config.%s.enable_auto_crafter".formatted(RandomOddities.MOD_ID), true).json("enable_auto_crafter").build();
        public final BooleanEntry itemsDeleter = BooleanEntry.builder("config.%s.enable_items_deleter".formatted(RandomOddities.MOD_ID), true).json("enable_items_deleter").build();

        public ItemsConfig() {
            super("items", "config.category.%s.items".formatted(RandomOddities.MOD_ID));
        }
    }

    public static class EnchantmentsConfig extends AutoInitConfigCategoryBase {
        public final BooleanEntry autoPlant = BooleanEntry.builder("config.%s.enable_auto_plant".formatted(RandomOddities.MOD_ID), true).json("enable_auto_plant").build();
        public final BooleanEntry kindness = BooleanEntry.builder("config.%s.enable_kindness".formatted(RandomOddities.MOD_ID), true).json("enable_kindness").build();

        public EnchantmentsConfig() {
            super("enchantments", "config.category.%s.enchantments".formatted(RandomOddities.MOD_ID));
        }
    }
}
