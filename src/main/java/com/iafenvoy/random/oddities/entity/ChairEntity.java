package com.iafenvoy.random.oddities.entity;

import com.iafenvoy.random.oddities.item.block.PlasticChairBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ChairEntity extends Entity {
    private BlockPos target = BlockPos.ZERO;

    public ChairEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
        this.setInvulnerable(true);
    }

    @Override
    protected void removePassenger(@NotNull Entity passenger) {
        super.removePassenger(passenger);
        this.kill();
    }

    @Override
    public void kill() {
        this.ejectPassengers();
        super.kill();
        BlockState state = this.level().getBlockState(this.getTarget());
        if (state.getBlock() instanceof PlasticChairBlock)
            this.level().setBlockAndUpdate(this.getTarget(), state.setValue(PlasticChairBlock.OCCUPIED, false));
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        return super.getDismountLocationForPassenger(passenger).relative(Direction.UP, 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.target = BlockPos.of(nbt.getLong("Target"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putLong("Target", this.target.asLong());
    }

    public BlockPos getTarget() {
        return this.target;
    }

    public void setTarget(BlockPos target) {
        this.target = target;
    }
}
