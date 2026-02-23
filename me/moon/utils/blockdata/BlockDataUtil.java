/*
 * Decompiled with CFR 0.152.
 */
package me.moon.utils.blockdata;

import java.util.Arrays;
import java.util.List;
import me.moon.utils.blockdata.BlockData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockDataUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final List<Block> invalidFace = Arrays.asList(Blocks.ladder, Blocks.anvil, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.sapling, Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.ender_chest, Blocks.lever, Blocks.noteblock, Blocks.stone_button, Blocks.wooden_button, Blocks.red_flower, Blocks.yellow_flower, Blocks.torch, Blocks.cocoa, Blocks.double_plant, Blocks.vine, Blocks.waterlily);

    public BlockData getBlockData2(BlockPos pos) {
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos3.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos2.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos1.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos1.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos1.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos1.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos1.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos1.add(1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos4.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos5.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos6.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos6.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos6.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos6.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos6.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos6.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos7.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos7.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos8.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(0, 1, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 1, -1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(0, 1, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 1, 1), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(-1, 1, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalidFace.contains(BlockDataUtil.mc.theWorld.getBlockState(pos9.add(1, 1, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 1, 0), EnumFacing.DOWN);
        }
        return null;
    }
}

