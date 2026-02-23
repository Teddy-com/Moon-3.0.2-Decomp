/*
 * Decompiled with CFR 0.152.
 */
package me.moon.command.impl;

import me.moon.Moon;
import me.moon.command.Command;
import me.moon.utils.game.Printer;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class VClipCommand
extends Command {
    public VClipCommand() {
        super("VClip", new String[]{"v", "vclip"});
    }

    @Override
    public void onRun(String[] args) {
        if (args.length == 1) {
            Printer.print(".vclip <value>");
            return;
        }
        if (args.length >= 2) {
            if (args[1].equals("up")) {
                for (int i = (int)(VClipCommand.mc.thePlayer.posY + 2.0); i < 255; ++i) {
                    BlockPos pos = new BlockPos(VClipCommand.mc.thePlayer.posX, (double)i, VClipCommand.mc.thePlayer.posZ);
                    if (!(VClipCommand.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) || i >= 254) continue;
                    BlockPos posUnder = new BlockPos(VClipCommand.mc.thePlayer.posX, (double)(i - 1), VClipCommand.mc.thePlayer.posZ);
                    BlockPos posOver = new BlockPos(VClipCommand.mc.thePlayer.posX, (double)(i + 1), VClipCommand.mc.thePlayer.posZ);
                    if (VClipCommand.mc.theWorld.getBlockState(posUnder).getBlock() instanceof BlockAir || !(VClipCommand.mc.theWorld.getBlockState(posOver).getBlock() instanceof BlockAir)) continue;
                    Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offsetAndUpdate(0.0, (double)pos.getY() - VClipCommand.mc.thePlayer.posY, 0.0);
                    Printer.print("VClipped to Y-Pos: " + pos.getY());
                    return;
                }
                Printer.print("Couldn't find a Block to teleport to!");
                return;
            }
            if (args[1].equals("down")) {
                for (int i = (int)(VClipCommand.mc.thePlayer.posY - 1.0); i > 0; --i) {
                    BlockPos posUnder;
                    BlockPos pos = new BlockPos(VClipCommand.mc.thePlayer.posX, (double)i, VClipCommand.mc.thePlayer.posZ);
                    if (!(VClipCommand.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) || i <= 1 || VClipCommand.mc.theWorld.getBlockState(posUnder = new BlockPos(VClipCommand.mc.thePlayer.posX, (double)(i - 1), VClipCommand.mc.thePlayer.posZ)).getBlock() instanceof BlockAir) continue;
                    Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offsetAndUpdate(0.0, -(VClipCommand.mc.thePlayer.posY - (double)pos.getY()), 0.0);
                    Printer.print("VClipped to Y-Pos: " + pos.getY());
                    return;
                }
                Printer.print("Couldn't find a Block to teleport to!");
                return;
            }
        }
        double distance = Double.parseDouble(args[1]);
        Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offsetAndUpdate(0.0, distance, 0.0);
        Moon.INSTANCE.getNotificationManager().addNotification("VClipped " + args[1] + "!", 2000L);
        Printer.print("VClipped " + args[1] + "!");
    }
}

