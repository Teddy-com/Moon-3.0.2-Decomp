/*
 * Decompiled with CFR 0.152.
 */
package me.moon.event.impl.player;

import java.util.List;
import me.moon.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventCollideUnderPlayer
extends Event {
    private Block block;
    private BlockPos.MutableBlockPos blockPos;
    private List<AxisAlignedBB> list;

    public EventCollideUnderPlayer(BlockPos.MutableBlockPos pos, Block block, List<AxisAlignedBB> bList) {
        this.block = block;
        this.blockPos = pos;
        this.list = bList;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockPos.MutableBlockPos getBlockPos() {
        return this.blockPos;
    }

    public List<AxisAlignedBB> getList() {
        return this.list;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setBlockPos(BlockPos.MutableBlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setList(List<AxisAlignedBB> boundingBox) {
        this.list = boundingBox;
    }
}

