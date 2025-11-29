package com.iafenvoy.random.oddities.item.block.entity;

import com.iafenvoy.random.oddities.registry.ROBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class PlasticChairBlockEntity extends BlockEntity {
    public static final int MAX_CHAIRS = 8;
    private static final String COLORS_KEY = "Colors";
    private final List<DyeColor> colors = new LinkedList<>();

    public PlasticChairBlockEntity(BlockPos pos, BlockState state) {
        super(ROBlockEntities.PLASTIC_CHAIR.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.colors.clear();
        nbt.getList(COLORS_KEY, Tag.TAG_STRING).stream().map(Tag::getAsString).map(x -> DyeColor.byName(x, DyeColor.WHITE)).forEach(this.colors::add);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put(COLORS_KEY, this.colors.stream().map(DyeColor::getSerializedName).map(StringTag::valueOf).collect(ListTag::new, List::add, List::addAll));
    }

    public boolean addChair(DyeColor color) {
        if (this.isFull()) return false;
        this.colors.add(color);
        this.setChanged();
        return true;
    }

    public DyeColor removeChair() {
        DyeColor color = this.colors.removeLast();
        if (this.colors.isEmpty() && this.level != null)
            this.level.destroyBlock(this.worldPosition, false);
        else this.setChanged();
        return color;
    }

    public List<DyeColor> getColors() {
        return List.copyOf(this.colors);
    }

    public boolean isFull() {
        return this.colors.size() >= MAX_CHAIRS;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null)
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 0);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
