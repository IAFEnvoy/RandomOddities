package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.iafenvoy.random.oddities.entity.ChairEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ROEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, RandomOddities.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ChairEntity>> CHAIR = register("chair", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.001F, 0.001F));

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> supplier) {
        return REGISTRY.register(id, () -> supplier.get().build(id));
    }
}
