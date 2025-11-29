package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.PlasticChairItem;
import com.iafenvoy.random.oddities.item.block.BreakerBlock;
import com.iafenvoy.random.oddities.item.block.EnchantSeparateTableBlock;
import com.iafenvoy.random.oddities.item.block.PlacerBlock;
import com.iafenvoy.random.oddities.item.block.PlasticChairBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ROBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(RandomOddities.MOD_ID);

    public static final DeferredBlock<Block> ENCHANT_SEPARATE_TABLE = register("enchant_separate_table", EnchantSeparateTableBlock::new, block -> new BlockItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> BREAKER = register("breaker", BreakerBlock::new, block -> new BlockItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> PLACER = register("placer", PlacerBlock::new, block -> new BlockItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> PLASTIC_CHAIR = register("plastic_chair", PlasticChairBlock::new, PlasticChairItem::new);

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> supplier, Function<T, Item> itemConstructor) {
        DeferredBlock<T> r = REGISTRY.register(id, supplier);
        ROItems.register(id, () -> itemConstructor.apply(r.get()));
        return r;
    }
}
