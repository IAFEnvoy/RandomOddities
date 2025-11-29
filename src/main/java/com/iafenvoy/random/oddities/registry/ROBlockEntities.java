package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.item.block.entity.BreakerBlockEntity;
import com.iafenvoy.random.oddities.item.block.entity.PlacerBlockEntity;
import com.iafenvoy.random.oddities.item.block.entity.PlasticChairBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
public final class ROBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RandomOddities.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BreakerBlockEntity>> BREAKER = register("breaker", () -> BlockEntityType.Builder.of(BreakerBlockEntity::new, ROBlocks.BREAKER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlacerBlockEntity>> PLACER = register("placer", () -> BlockEntityType.Builder.of(PlacerBlockEntity::new, ROBlocks.PLACER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlasticChairBlockEntity>> PLASTIC_CHAIR = register("plastic_chair", () -> BlockEntityType.Builder.of(PlasticChairBlockEntity::new, ROBlocks.PLASTIC_CHAIR.get()).build(null));

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
