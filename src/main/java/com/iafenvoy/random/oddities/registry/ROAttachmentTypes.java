package com.iafenvoy.random.oddities.registry;

import com.iafenvoy.random.oddities.RandomOddities;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ROAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RandomOddities.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> LOCK_AGE = register("lock_age", () -> AttachmentType.builder(x -> false).serialize(Codec.BOOL).sync(ByteBufCodecs.BOOL).build());

    public static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String id, Supplier<AttachmentType<T>> supplier) {
        return REGISTRY.register(id, supplier);
    }
}
